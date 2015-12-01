/*
 * Creado el 27-dic-2005
 * por Joan López
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.KardexDao;

/**
 * @author Joan López
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class Kardex 
{
	/**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(Kardex.class);
    /**
	 * DAO de este objeto, para trabajar con
	 * registro kardex en la fuente de datos
	 */
    private static KardexDao kardex;
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */   
    public void reset ()
    {   
    	
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( kardex== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			kardex= myFactory.getKardexDao();			
			if( kardex!= null )
				return true;
		}
		return false;
	}	
	/**
	 * Constructor de la clase	 
	 */
	public Kardex ()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
     * metodo para realizar la busqueda avanzada de articulos
     * @param con Connection
     * @param vo HashMap 
     * @return HashMap     
     */
    public HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
    	return kardex.ejecutarBusquedaAvanzada(con, vo);
    }
    /**
     * metodo para consultar el detalle de los
     * articulos
     * @param con Connection
     * @param almacen 
     * @param articulo String
     * @param fechaInicial String
     * @param fechaFinal String
     * @return HashMap
     */
    public HashMap ejecutarConsultaDetalleArticulos(Connection con, int almacen, String articulo,String fechaInicial,String fechaFinal)
    {
    	HashMap mapa=kardex.ejecutarConsultaDetalleArticulos(con, almacen,articulo,fechaInicial,fechaFinal);
    	for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
    	{
    	    mapa.put("fecha_elaboracion_"+k, UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion_"+k)+""));
    	    mapa.put("fecha_atencion_"+k, UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_atencion_"+k)+""));
    	}
        return mapa;
    }
    /**
     * metodo para realizar la consulta del 
     * detalle de cierres
     * @param con Connection
     * @param codigoCierre String
     * @param codigoArticulo int
     * @param codigoAlmacen int
     * @return HashMap
     */
    public HashMap ejecutarConsultaDetalleCierres(Connection con,String codigoCierre,int codigoArticulo,int codigoAlmacen)
    {
    	return kardex.ejecutarConsultaDetalleCierres(con, codigoCierre, codigoArticulo, codigoAlmacen);
    }
    /**
     * metodo para consultar los ultimos movimientos
     * de un articulo para generar el kardex
     * @param con Connection 
     * @param fechaInicial String 
     * @param fechaFinal String
     * @param articulo String
     * @param almacen String
     * @return HashMap
     */
    public HashMap consultarUltimosMovimientosArticulo(Connection con,String fechaInicial,String fechaFinal,String articulo,String almacen)
    {
    	return kardex.consultarUltimosMovimientosArticulo(con,UtilidadFecha.conversionFormatoFechaABD(fechaInicial),UtilidadFecha.conversionFormatoFechaABD(fechaFinal),articulo,almacen);
    }    
    
    /**
     * metodo para consultar los ultimos movimientos
     * de un articulo para generar el kardex
     * @param con Connection 
     * @param fechaInicial String 
     * @param fechaFinal String
     * @param articulo String
     * @param almacen String
     * @return HashMap
     */
    public HashMap consultarUltimosMovimientosArticuloLote(Connection con,String fechaInicial,String fechaFinal,String articulo,String almacen,String lote,String fechaVencimiento)
    {
    	return kardex.consultarUltimosMovimientosArticuloLote(con,UtilidadFecha.conversionFormatoFechaABD(fechaInicial),UtilidadFecha.conversionFormatoFechaABD(fechaFinal),articulo,almacen,lote,fechaVencimiento);
    } 
    /**
	 * metodo para consultar el costo del saldo,
	 * toma el ultimo regisrro desde la fecha/hora
	 * del movimiento que se esta relacionando.	
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String	 
	 * @return  String
	 */
    public HashMap generarCalculoCostoUnitarioArticulos(HashMap mapa,String articulo,int institucion)
    {    	
        String movimiento="",costoUnitario="";
        int pos=-1;        
        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
    	{
            pos=(mapa.get("codigomovimiento_"+k)+"").indexOf("-");
            movimiento=pos!=-1?movimiento=(mapa.get("codigomovimiento_"+k)+"").substring(0,pos):(mapa.get("codigomovimiento_"+k)+"");            
            costoUnitario=UtilidadInventarios.obtenerCostoUnitarioKardex(institucion,Integer.parseInt(articulo),mapa.get("fecha_atencion_"+k)+"",movimiento);
            costoUnitario=costoUnitario.equals(ConstantesBD.codigoNuncaValido+"")?mapa.get("costo_unitario_"+k)+"":costoUnitario;
            mapa.put("costo_unitario_dos_"+k,costoUnitario);
    	}
    	return mapa;
    }
    /**
     * metodo para calcular la cantidad de saldo
     * por movimiento del articulo
     * @param mapa HashMap
     * @param cantidadInicial String
     * @return HashMap
     */
    public HashMap generarCalculoCantidadArticulos(HashMap mapa,String cantidadInicial)
    {
    	int cantidad=Integer.parseInt(cantidadInicial);
    	for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
    	{
    		cantidad=cantidad+(Integer.parseInt(mapa.get("cantidad_entrada_"+k)+"")-Integer.parseInt(mapa.get("cantidad_salida_"+k)+""));
    		mapa.put("cantidad_saldo_"+k, cantidad+"");
    	}
    	return mapa;
    }
    /**
     * metodo para generar el saldo de los
     * movimientos por articulo
     * @param mapa
     * @param cantidadInicial
     * @return
     */
	public HashMap generarCalculoValorSaldoArticulos(HashMap mapa)
	{
		double valor=0;
        String valorConFormato="";
		for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
    	{			
            valor=Integer.parseInt(mapa.get("cantidad_saldo_"+k)+"")*Double.parseDouble(mapa.get("costo_unitario_dos_"+k)+"");  
            valorConFormato=UtilidadTexto.formatearValores(valor,"0.00");
			mapa.put("valor_saldo_"+k, valorConFormato);
    	}
		return mapa;
	}
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap accionEjecutarBusquedaArticulosLote(Connection con, HashMap vo)
	{
		return kardex.accionEjecutarBusquedaArticulosLote(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCierreAnual
	 * @param i
	 * @param codAlmacen
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public HashMap ejecutarConsultaDetalleCierresLote(Connection con,String codigoCierre,int codigoArticulo,int codigoAlmacen, String lote, String fechaVencimiento)
	{
    	return kardex.ejecutarConsultaDetalleCierresLote(con, codigoCierre, codigoArticulo, codigoAlmacen,lote,fechaVencimiento);

	}
	
	/**
	 * 
	 * @param con
	 * @param almacen
	 * @param articulo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public HashMap ejecutarConsultaDetalleArticulosLote(Connection con, int almacen, String articulo,String fechaInicial,String fechaFinal,String lote,String fechaVencimiento)
    {
    	HashMap mapa=kardex.ejecutarConsultaDetalleArticulosLote(con, almacen,articulo,UtilidadFecha.conversionFormatoFechaABD(fechaInicial),UtilidadFecha.conversionFormatoFechaABD(fechaFinal),lote,fechaVencimiento);
    	for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
    	{
    	    mapa.put("fecha_elaboracion_"+k, UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion_"+k)+""));
    	    mapa.put("fecha_atencion_"+k, UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_atencion_"+k)+""));
    	}
        return mapa;
    }
}
