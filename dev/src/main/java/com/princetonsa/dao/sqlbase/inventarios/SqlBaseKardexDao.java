/*
 * Creado el 27-dic-2005
 * por Joan López
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Joan López
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class SqlBaseKardexDao 
{
	/**
     * manejador de los logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseKardexDao.class);
    /**
     * query para realizar la consulta de articulos
     */
    private static final String consultaArticulosStr="SELECT DISTINCT " +
											    		"va.codigo as cod_articulo," +
											    		"va.descripcion as desc_articulo," +
											    		"getNomUnidadMedida(va.unidad_medida) as unidad_medida " +
											    	 "FROM view_articulos va ";
    /**
     * query para consultar el detalle de los articulos
     */
    private static final String consultaDetalleArticulosStr="SELECT " +
                                                                "codigomovimiento as codigomovimiento," +
													    		"codigotransaccion as cod_transaccion," +
													    		"desctransaccion as desc_transaccion," +
													    		"documento as documento," +
													    		"fechaelaboracion as fecha_elaboracion," +
													    		"fechaatencion as fecha_atencion," +
													    		"costounitario as costo_unitario," +
													    		"cantidadentrada as cantidad_entrada," +
													    		"valorentrada as valor_entrada," +
													    		"cantidadsalida as cantidad_salida," +
													    		"valorsalida as valor_salida," +
													    		"lote as lote," +
													    		"to_char(fechavencimiento,'yyyy-mm-dd') as fechavencimiento " +
													    	 "FROM view_movimientos_inventarios ";
    /**
     * query para realizar la consulta del detalle de cierres
     */
    private static final String consultaDetalleCierreStr="SELECT " +
												    		"almacen as almacen," +
												    		"articulo as articulo," +
												    		"cantidad_total_entradas as cantidad_total_entradas," +
												    		"cantidad_total_salidas as cantidad_total_salidas," +
												    		"cantidad_total_entradas_anio as cantidad_total_entradas_anio," +
												    		"cantidad_total_salidas_anio as cantidad_total_salidas_anio " +
												    	 "FROM det_cierre_inventarios " +
												    	 "WHERE codigo_cierre=? AND articulo=?";   
    /**
     * query para consultar los ultimos movimientos de un articulo en un rago de fechas
     */
    private static final String consultaUltimosMovimientosArticuloStr="SELECT " +
															    		"cantidadentrada as cantidad_entrada," +
															    		"cantidadsalida as cantidad_salida " +
															    		"FROM view_movimientos_inventarios ";
    
    /**
     * metodo para realizar la busqueda avanzada de articulos
     * @param con Connection
     * @param vo HashMap 
     * @return HashMap     
     */
    public static HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
	    String query=consultaArticulosStr+" INNER JOIN view_movimientos_inventarios vm on(to_number(vm.articulo,'9999999')=va.codigo) INNER JOIN centros_costo cc on (cc.codigo=to_number(vm.almacen,'9999999'))";
	    String rest="";
     	if(Utilidades.convertirAEntero(vo.get("codigoAlmacen")+"")>0)
        {
              rest+=" AND vm.almacen='"+(vo.get("codigoAlmacen")+"")+"'";
        }
     	if(Utilidades.convertirAEntero(vo.get("centroAtencion")+"")>0)
        {
              rest+=" AND cc.centro_atencion="+(vo.get("centroAtencion")+"")+"";
        }
     	if(!vo.get("fechaInicial").equals("") && !vo.get("fechaFinal").equals(""))
        {
     		rest+=" AND vm.fechaatencion between '"+(vo.get("fechaInicial")+"")+"' AND '"+(vo.get("fechaFinal")+"")+"'" ;
        }
        if(!vo.get("codigoClase").equals("0"))
        {            
            rest+=" AND va.clase='"+(vo.get("codigoClase")+"")+"'";
        }
        if(!vo.get("codigoGrupo").equals("0"))
        {
            rest+=" AND va.grupo='"+(vo.get("codigoGrupo")+"")+"'";
        }
        if(!vo.get("codigoSubGrupo").equals("0"))
        {
            rest+=" AND va.subgrupo='"+(vo.get("codigoSubGrupo")+"")+"'";
        }
        if(!vo.get("codigoArticulo").equals("-1"))
        {
            rest+=" AND va.codigo='"+(vo.get("codigoArticulo")+"")+"'";
        }
        query+=" WHERE 1=1 "+rest;
	    
        //query+=" ORDER BY va.codigo,vm.almacen";
        
        try
		{
	        PreparedStatementDecorator ps = null;     
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   
	        
	        logger.info("\n\nCONSULTA::"+query);
	        
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
	        
	        
		}
	    catch (SQLException e)
	    {
	        logger.error("Error busqueda de articulos"+e);
	        e.printStackTrace();		        
	    }
		return null; 
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
    public static HashMap ejecutarConsultaDetalleArticulos(Connection con, String consulta, int almacen, String articulo,String fechaInicial,String fechaFinal)
    {
    	 try
			{
		        PreparedStatementDecorator ps = null;
		   
				logger.info("\n\nquery::"+consulta+"\n\narticulo::"+articulo+"  fecha inicial::"+fechaInicial+"   fecha final::"+fechaFinal);
		        ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		        
		        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
		        rs.close();
		        ps.close();
		        return mapaRetorno;
			}
		    catch (SQLException e)
		    {
		        logger.error("Error consulta del detalle de articulos"+e);
		        e.printStackTrace();		        
		    }
			return null; 
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
    public static HashMap ejecutarConsultaDetalleCierres(Connection con,String codigoCierre,int codigoArticulo,int codigoAlmacen)
    {
    	try
		{
	        PreparedStatementDecorator ps = null;            
	        String cadena=consultaDetalleCierreStr;
	        if(codigoAlmacen>0)
	        	cadena+=" AND almacen='"+codigoAlmacen+"' ";
	        
	        logger.info(cadena);
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1, codigoCierre);
	        ps.setInt(2, codigoArticulo); 
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		        
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
	    catch (SQLException e)
	    {
	        logger.error("Error consulta del detalle de cierres"+e);
	        e.printStackTrace();		        
	    }
		return null; 
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
    public static HashMap consultarUltimosMovimientosArticulo(Connection con,String fechaInicial,String fechaFinal,String articulo,String almacen)
    {
    	try
		{
    		PreparedStatementDecorator ps = null;
    		String cadena=consultaUltimosMovimientosArticuloStr+" WHERE articulo='"+articulo+"' AND fechaatencion between '"+fechaInicial+"' AND '"+fechaFinal+"' ";
	        if(Utilidades.convertirAEntero(almacen)>0)
	        	cadena+=" AND almacen='"+almacen+"' ";
	        logger.info(cadena);
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		        
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
    	catch (SQLException e)
	    {
	        logger.error("Error consultarUltimosMovimientosArticulo"+e);
	        e.printStackTrace();		        
	    }
		return null;
    }
    
    /**
     * 
     * @param con
     * @param vo
     * @return
     */
	public static HashMap accionEjecutarBusquedaArticulosLote(Connection con, HashMap vo)
	{
		String query="SELECT DISTINCT " +
							" va.codigo as cod_articulo," +
							" va.descripcion as desc_articulo," +
							" getNomUnidadMedida(va.unidad_medida) as unidad_medida, " +
							" vm.lote as lote," +
							" to_char(vm.fechavencimiento,'yyyy-mm-dd') as fechavencimiento " +
						" FROM view_articulos va " +
						" INNER JOIN view_movimientos_inventarios vm on(vm.articulo=va.codigo||'')" +
						" INNER JOIN centros_costo cc on (cc.codigo||''=vm.almacen) ";
		
		String rest=" ";
     	if(Utilidades.convertirAEntero(vo.get("codigoAlmacen")+"")>0)
        {
              rest+=" AND vm.almacen='"+(vo.get("codigoAlmacen")+"")+"'";
        }
     	if(Utilidades.convertirAEntero(vo.get("centroAtencion")+"")>0)
        {
              rest+=" AND cc.centro_atencion="+(vo.get("centroAtencion")+"")+"";
        }
     	if(!vo.get("fechaInicial").equals("") && !vo.get("fechaFinal").equals(""))
        {
     		rest+=" AND vm.fechaatencion between '"+(vo.get("fechaInicial")+"")+"' AND '"+(vo.get("fechaFinal")+"")+"'" ;
        }
        if(!vo.get("codigoArticulo").equals("-1"))
        {
            rest+=" AND va.codigo='"+(vo.get("codigoArticulo")+"")+"'";
        }
        query+=" WHERE 1=1 "+rest;
	    
        try
		{
	        PreparedStatementDecorator ps = null;
	        logger.info("\n\nConsulta:\n"+query);
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
	    catch (SQLException e)
	    {
	        logger.error("Error busqueda de articulos"+e);
	        e.printStackTrace();		        
	    }
		return null; 
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @param codigoArticulo
	 * @param codigoAlmacen
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static HashMap ejecutarConsultaDetalleCierresLote(Connection con, String codigoCierre, int codigoArticulo, int codigoAlmacen, String lote, String fechaVencimiento)
	{
    	try
		{
	        PreparedStatementDecorator ps = null;            
	        String cadena=consultaDetalleCierreStr+" ";
	        if(UtilidadTexto.isEmpty(lote))
			{
				cadena=cadena+" and lote is null and fecha_vencimiento is null ";
			}
			else
			{
				cadena=cadena+" and lote = '"+lote+"'";
				if(UtilidadTexto.isEmpty(fechaVencimiento))
				{
					cadena=cadena+" and fecha_vencimiento is null ";
				}
				else
				{
					cadena=cadena+" and to_char(fecha_vencimiento,'yyyy-mm-dd') = '"+fechaVencimiento+"'";
				}
			}
	        if(codigoAlmacen>0)
	        	cadena+=" AND almacen='"+codigoAlmacen+"' ";
	        
	        logger.info(cadena);
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1, codigoCierre);
	        ps.setInt(2, codigoArticulo); 
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		        
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
	    catch (SQLException e)
	    {
	        logger.error("Error consulta del detalle de cierres"+e);
	        e.printStackTrace();		        
	    }
		return null; 
	}
	
	  /**
     * 
     * @param lote
     * @param fechaVencimiento
     * @return
     */
    private static String restriccionLoteFechaVencimiento(String lote, String fechaVencimiento)
	{
		String cadena=" ";
		if(UtilidadTexto.isEmpty(lote))
		{
			cadena=cadena+" and lote is null and fechavencimiento is null ";
		}
		else
		{
			cadena=cadena+" and lote = '"+lote+"'";
			if(UtilidadTexto.isEmpty(fechaVencimiento))
			{
				cadena=cadena+" and fechavencimiento is null ";
			}
			else
			{
				cadena=cadena+" and to_char(fechavencimiento,'yyyy-mm-dd') = '"+fechaVencimiento+"'";
			}
		}
		return cadena;	
	}
    
    /**
     * 
     * @param con
     * @param fechaInicial
     * @param fechaFinal
     * @param articulo
     * @param almacen
     * @param lote
     * @param fechaVencimiento
     * @return
     */
	public static HashMap consultarUltimosMovimientosArticuloLote(Connection con, String fechaInicial, String fechaFinal, String articulo, String almacen, String lote, String fechaVencimiento)
	{

    	try
		{
    		PreparedStatementDecorator ps = null;
    		String cadena=consultaUltimosMovimientosArticuloStr+" WHERE articulo='"+articulo+"' AND fechaatencion between '"+fechaInicial+"' AND '"+fechaFinal+"' "+restriccionLoteFechaVencimiento(lote, fechaVencimiento);
	        if(Utilidades.convertirAEntero(almacen)>0)
	        	cadena+=" AND almacen='"+almacen+"' ";
	        logger.info(cadena);
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		        
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
    	catch (SQLException e)
	    {
	        logger.error("Error consultarUltimosMovimientosArticulo"+e);
	        e.printStackTrace();		        
	    }
		return null;
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
	public static HashMap ejecutarConsultaDetalleArticulosLote(Connection con, int almacen, String articulo, String fechaInicial, String fechaFinal, String lote, String fechaVencimiento)
	{
   	 try
		{
	        PreparedStatementDecorator ps = null;
	        String cadena=consultaDetalleArticulosStr+" WHERE articulo='"+articulo+"' AND fechaatencion between '"+fechaInicial+"' AND '"+fechaFinal+"' " + restriccionLoteFechaVencimiento(lote, fechaVencimiento);
	        if(almacen>0)
	        {
	        	cadena+=" AND almacen='"+almacen+"' ";
	        }
	        cadena +=" ORDER BY fechaatencion,horaatencion asc ";
	        logger.info(cadena);
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		        
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
	    catch (SQLException e)
	    {
	        logger.error("Error consulta del detalle de articulos",e);
	    }
		return null; 
	}
}
