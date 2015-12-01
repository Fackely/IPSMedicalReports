package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.CuentasConveniosDao;

public class CuentasRegimen
{
	private String acronimoTipoRegimen;
	
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
    
	public CuentasRegimen()
	{
		this.clean();
	}
	
	private void clean()
	{
		this.acronimoTipoRegimen="";
		this.codigoInstitucion=-1;
		this.cuentas = new HashMap();
		this.cantidadCuentas = 0;
	}
	
	public String getAcronimoTipoRegimen()
	{
		return this.acronimoTipoRegimen;
	}
	
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen)
	{
		this.acronimoTipoRegimen = acronimoTipoRegimen;
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
	        			
	        			String rubro=this.getCuenta("rubro_"+codigoTipoCuenta)+"";
	        			
	        			//Cambios Anexo 809
	        			String capitacion=(String)this.getCuenta("capitacion_"+codigoTipoCuenta);
	        			
	        			
	        			if(!CuentasRegimen.getCuentasConveniosDao().existeCuentaRegimen(con, Integer.parseInt(codigoTipoCuenta), this.getAcronimoTipoRegimen(), this.getCodigoInstitucion()))
			        	{
	        				CuentasRegimen.getCuentasConveniosDao().insertarCuentaRegimen(
				        			con, Integer.parseInt(codigoTipoCuenta), this.getAcronimoTipoRegimen(), this.getCodigoInstitucion(), valorCuenta, rubro.trim(), capitacion);
			        	}
			        	else
			        	{
			        		CuentasRegimen.getCuentasConveniosDao().actualizarCuentaRegimen(
				        			con, Integer.parseInt(codigoTipoCuenta), this.getAcronimoTipoRegimen(), this.getCodigoInstitucion(), valorCuenta, rubro.trim(),capitacion);
			        	}
	        		}
	        	}
	        }
	        
	        myFactory.endTransaction(con);
        }
        catch(SQLException se)
        {
            if(inicioTransaccion)
                myFactory.abortTransaction(con);
            throw se;
        }
	}
	
	public void consultarCuentasRegimen(Connection con, String acronimoTipoRegimen, int codigoInstitucion) throws Exception
	{
		HashMap mapaCuentasRegimen=CuentasRegimen.getCuentasConveniosDao().consultarCuentasRegimen(con, acronimoTipoRegimen, codigoInstitucion);
		this.setCantidadCuentas(Utilidades.convertirAEntero((String)mapaCuentasRegimen.get("numRegistros")));
		Utilidades.imprimirMapa(mapaCuentasRegimen);
		for(int i=0; i<this.getCantidadCuentas(); i++)
		{
			Integer codigoTipoCuenta = Utilidades.convertirAEntero(mapaCuentasRegimen.get("codtipocuenta_"+i)+"");
			this.setCuenta("codigoTipoCuenta_"+i, codigoTipoCuenta);
			this.setCuenta("nombreTipoCuenta_"+codigoTipoCuenta, mapaCuentasRegimen.get("nomtipocuenta_"+i)+"");
			this.setCuenta("valor_"+codigoTipoCuenta, mapaCuentasRegimen.get("valor_"+i)+"");
			this.setCuenta("rubro_"+codigoTipoCuenta, mapaCuentasRegimen.get("rubro_"+i)+"".trim());
			this.setCuenta("capitacion_"+codigoTipoCuenta, mapaCuentasRegimen.get("capitacion_"+i)+"".trim());
		}
	}
}