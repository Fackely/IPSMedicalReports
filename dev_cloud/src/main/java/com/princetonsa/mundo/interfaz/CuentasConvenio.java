package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.CuentasConveniosDao;

public class CuentasConvenio
{
	private int codigoConvenio;
	
	private int codigoInstitucion;
	
	private HashMap cuentas;
	
	private int cantidadCuentas;	
	
    private static CuentasConveniosDao cuentasConveniosDao;
    
    private static CuentasConveniosDao getCuentasConveniosDao()
    {
        if(cuentasConveniosDao==null)
        {
            String tipoBD = System.getProperty("TIPOBD" );
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            cuentasConveniosDao=myFactory.getCuentasConveniosDao();
        }
        return cuentasConveniosDao;
    }
    
	public CuentasConvenio()
	{
		this.clean();
	}
	
	private void clean()
	{
		this.codigoConvenio=-1;
		this.codigoInstitucion=-1;
		this.cuentas = new HashMap();
		this.cantidadCuentas = 0;
	}
	
	public int getCodigoConvenio()
	{
		return this.codigoConvenio;
	}
	
	public void setCodigoConvenio(int codigoConvenio)
	{
		this.codigoConvenio = codigoConvenio;
	}
	
	public int getCodigoInstitucion()
	{
		return this.codigoInstitucion;
	}
	
	public void setCodigoInstitucion(int codigoInstitucion)
	{
		this.codigoInstitucion = codigoInstitucion;
	}
	
	public int getCantidadCuentas()
	{
		return this.cantidadCuentas;
	}
	
	public void setCantidadCuentas(int cantidadCuentas)
	{
		this.cantidadCuentas = cantidadCuentas;
	}
	
	public void setCuentas(HashMap cuentas)
	{
		this.cuentas = cuentas;
	}
	
	public HashMap getCuentas()
	{
		return this.cuentas;
	}
	
	public void setCuenta(String key, Object value)
	{
		this.cuentas.put(key, value);
	}
	
	public Object getCuenta(String key)
	{
		return this.cuentas.get(key);
	}
	
	public void guardar(Connection con) throws Exception
	{
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

        try
        {
	        inicioTransaccion = myFactory.beginTransaction(con);
	        if(inicioTransaccion)
	        {
	        	for(int i=0; i<cantidadCuentas; i++)
	        	{
	        		String codigoTipoCuenta = (String)this.getCuenta("codigoTipoCuenta_"+i);
	        		if(UtilidadCadena.noEsVacio(codigoTipoCuenta))
	        		{
	        			String valorCuenta = (String)this.getCuenta("valor_"+codigoTipoCuenta);
	        			String rubro = this.getCuenta("rubro_"+codigoTipoCuenta)+"".trim();
	        			String capitacion = this.getCuenta("capitacion_"+codigoTipoCuenta)+"".trim();
			        	if(!CuentasConvenio.getCuentasConveniosDao().existeCuentaConvenio(con, Integer.parseInt(codigoTipoCuenta), this.getCodigoConvenio(), this.getCodigoInstitucion()))
			        	{
			        		CuentasConvenio.getCuentasConveniosDao().insertarCuentaConvenio(
				        			con, Integer.parseInt(codigoTipoCuenta), this.getCodigoConvenio(), this.getCodigoInstitucion(), valorCuenta, rubro, capitacion);
			        	}
			        	else
			        	{
			        		CuentasConvenio.getCuentasConveniosDao().actualizarCuentaConvenio(
				        			con, Integer.parseInt(codigoTipoCuenta), this.getCodigoConvenio(), this.getCodigoInstitucion(), valorCuenta, rubro, capitacion);
			        	}
	        		}
	        	}
	        }
	        else
	        	throw new Exception("No se pudo iniciar la transacción");
	        
	        myFactory.endTransaction(con);
        }
        catch(SQLException se)
        {
            if(inicioTransaccion)
                myFactory.abortTransaction(con);
            throw se;
        }
	}
	
	public void consultarCuentasConvenio(Connection con, int codigoConvenio, int codigoInstitucion) throws Exception
	{
		HashMap mapaCuentasConvenio=CuentasConvenio.getCuentasConveniosDao().consultarCuentasConvenio(con, codigoConvenio, codigoInstitucion);
		
		this.setCantidadCuentas(Integer.parseInt((String)mapaCuentasConvenio.get("numRegistros")));
		this.setCodigoConvenio(codigoConvenio);
		this.setCodigoInstitucion(this.codigoInstitucion);
		
		for(int i=0; i<this.getCantidadCuentas(); i++)
		{
			int codigoTipoCuenta = Utilidades.convertirAEntero(mapaCuentasConvenio.get("codtipocuenta_"+i)+"");
			this.setCuenta("codigoTipoCuenta_"+i, codigoTipoCuenta);
			this.setCuenta("nombreTipoCuenta_"+codigoTipoCuenta, (String)mapaCuentasConvenio.get("nomtipocuenta_"+i));
			this.setCuenta("valor_"+codigoTipoCuenta, (String)mapaCuentasConvenio.get("valor_"+i));
			this.setCuenta("rubro_"+codigoTipoCuenta, mapaCuentasConvenio.get("rubro_"+i)+"".trim());
			this.setCuenta("capitacion_"+codigoTipoCuenta, mapaCuentasConvenio.get("capitacion_"+i)+"".trim());
		}
	}
}