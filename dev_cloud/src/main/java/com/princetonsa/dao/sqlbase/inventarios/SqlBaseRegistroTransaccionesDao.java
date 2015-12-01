
/*
 * Creado   8/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase.inventarios;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.ConstantesBDInventarios;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase que implementa los metodos para realizar las
 * respectivas operaciones sobre la BD.
 *
 * @version 1.0, 8/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseRegistroTransaccionesDao 
{
    /**
     * manejador de los logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseRegistroTransaccionesDao.class);
    
    /**
     * Cadena generica para la consulta de las transacciones de inventarios de una institucion
     */
    private static final String cadenaConsultaTransaccionesInstitucion="SELECT DISTINCT " +
    																	" tvcc.tipos_trans_inventario as consecutivo_tipo_trans," +    																	
    																	" tti.tipos_costo_inv as tipo_costo," +
    																	" tti.descripcion as desctransaccion," +
    																	" tti.codigo as codigo_tipo_trans," +
    																	" tti.indicativo_consignacion as indicativo_consignacion," +
    																	" CASE WHEN txa.entidad IS NULL THEN -1 ELSE txa.entidad END as codigo_entidad," +
    																	" txa.codigo as codigo," +
    																	" txa.consecutivo as numero," +
    																	" txa.estado as estado," +
    																	" txa.fecha_elaboracion as fechaelaboracion," +
    																	" txa.usuario as usuarioelabora, " +
    																	" txa.almacen as codigoalmacen, " +
    																	" cc.nombre as nombrealmacen," +
    																	" cc.centro_atencion as codigocentroatencion," +
    																	" getnomcentroatencion(cc.centro_atencion) as nombrecentroatencion," +    																																	
    																	" case when txa.observaciones is null then '' else txa.observaciones end as observaciones," +
    																	" tci.codigo as codtipo," +
    																	" tci.descripcion as desctipo," +   																	
    																	" eti.descripcion as descestado," +	
    																	" txa.fecha_cierre as fechacierre," +
    																	" txa.hora_cierre as horacierre," +
    																	" case when txa.usuario_cierre is null then '' else txa.usuario_cierre end as usuariocirre," +    																	
    																	" '' as destipoidentidad," +
    																	" t.numero_identificacion as numeroidentidad," +
    																	" '' as codtipoidentidad," +
    																	" case when t.descripcion is null then 'Seleccione' else t.descripcion end as nomentidad," +
    																	" (SELECT sum(cantidad * val_unitario) from det_trans_x_almacen where transaccion=txa.codigo) as valortotal, " +
    																	" (SELECT sum(cantidad) from det_trans_x_almacen where transaccion=txa.codigo) as cantidadtotal " +
    																" from transacciones_x_almacen txa " +
    																" inner join tipos_trans_inventarios tti on (txa.transaccion=tti.consecutivo) " +
                                                                    " inner join trans_validas_x_cc_inven tvcc on(tti.consecutivo=tvcc.tipos_trans_inventario) " +
    																" inner join tipos_conceptos_inv tci on (tti.tipos_conceptos_inv=tci.codigo) " +
    																" inner join estados_transacciones_inv eti on (txa.estado=eti.codigo) " +
    																" inner join centros_costo cc on(cc.codigo=txa.almacen) " +
    																" left join terceros t on(txa.entidad=t.codigo) ";
    																

    /**
     * Cadena para realizar la consuta de los articulos de una transaccion.
     */
    private static final String cadenaConsultaArticulosTran="SELECT " +
    															" dtxa.codigo as codigo, " +
    															" dtxa.articulo as codarticulo," +
    															" coalesce(a.codigo_interfaz,'') as codigo_interfaz," +
    															//se pone repetido debido a que en algunas vistas se usan los 2 alias, esto no da carga a la consulta.
    															" coalesce(a.codigo_interfaz,'') as codigointerfaz," +
    															" coalesce(va.descripcion,'')||' '||coalesce(va.concentracion,'')||' '||coalesce(va.descripcionff,'') as descripcion," +
    															" va.descripcionum as unidadmedida," +
    															" dtxa.cantidad as cantidadarticulo," +
    															" dtxa.val_unitario as valorunitario," +
    															" dtxa.cantidad*convertiranumero(dtxa.val_unitario||'') as valortotal," +
    															" case when dtxa.lote is null then ''  else dtxa.lote end as lote_articulo," +
    															" case when dtxa.fecha_vencimiento is null then ''  else to_char(dtxa.fecha_vencimiento,'dd/mm/yyyy') end as fecha_vencimiento_articulo, " +    															
    															" (SELECT sum(dtxa1.cantidad * dtxa1.val_unitario) " +
    															" from det_trans_x_almacen dtxa1 " +
    															" where dtxa1.transaccion=dtxa.transaccion) as totaltransaccion" ;
    														
    /**
     * query para consultar si un registro dado existe en la BD
     */
    private static final String existeTransaccionEnBDStr=" SELECT" +
													    		" txa.codigo as codigo," +
													    		" txa.fecha_elaboracion as fecha_elaboracion," +
													    		" case when txa.entidad is null then -1 else txa.entidad end as entidad," +
													    		" case when txa.observaciones is null then '' else txa.observaciones end as observaciones," +
													    		" t.numero_identificacion as numero_identificacion," +
																" '' as tipo_identificacion," +
																" case when t.descripcion is null then 'Seleccione' else t.descripcion end as nombre_entidad" +
													     " FROM transacciones_x_almacen txa" +
													     " left join terceros t on(txa.entidad=t.codigo)" +
													     " WHERE txa.codigo=?";
    /**
     * query para realizar la modificación de la información general
     */
    private static final String actualizarInformacionGeneralStr="UPDATE transacciones_x_almacen SET";
    /**
     * query para actualizar el estado de la transacción segun el caso
     */
    private static final String actualizarEstadoTransaccionStr="UPDATE transacciones_x_almacen SET estado=? where codigo=?";
    /**
     * query para consultar el ultimo registro insertado
     */
    private static final String ultimoCodigoTransaccionInsertadoStr="SELECT max(convertiranumero(codigo||'')) as codigo from transacciones_x_almacen";
    /**
     * query para insertar el detalle de la transacción
     */
    private static final String insertarDetalleTransaccionStr="INSERT INTO det_trans_x_almacen(transaccion,articulo,cantidad,val_unitario,lote,fecha_vencimiento,proveedor_compra,proveedor_catalogo,codigo) VALUES (?,?,?,?,?,?,?,?,?)";
    
    /**
     * query para eliminar el detalle de la transacción
     */
    private static final String eliminarDetalleTransaccionStr="DELETE FROM det_trans_x_almacen WHERE codigo=?";

    /**
     * query para consultar si existe registro detalle en BD
     */
    private static final String existeDetalleTransaccionStr=" SELECT " +
    															"codigo as codigo," +
    															"articulo as articulo," +
													    	    "cantidad as cantidad," +
													    	    "val_unitario as val_unitario," +
													    	    "lote," +
													    	    "fecha_vencimiento " +
												    		" FROM det_trans_x_almacen " +
												    		" WHERE codigo=? ";

    
    /**
     * query para actualizar el detalle de una transacción
     */
    private static final String actualizarDetalleTransaccionStr="UPDATE det_trans_x_almacen SET";
    /**
     * query para generar el respectivo registro del ajuste al costo promedio
     */
    private static final String registrarAjusteStr="INSERT " +
			    											"INTO ajustes_costo_articulos" +
															"(codigo,codarticulo,fecha," +
															"hora,almacen,tipo_transaccion," +
															"numero_transaccion,valor_costo_antes," +
															"valor_costo_despues,tipo_ajuste_costo) " +
															"VALUES (?,?,?,?,?,?,?,?,?,?)";
   /**
    * query para inseetar el registro de cierre de la trnasaccion
    */
    private static final String registrarCierreTransaccionStr=" UPDATE transacciones_x_almacen set estado=?, usuario_cierre=?,fecha_cierre=?,hora_cierre=? where codigo=?";
    /**
     * query para insertar laanulación de la transacción
     */
    private static final String generarAnulacionTransaccionStr="UPDATE transacciones_x_almacen set estado=?, usuario_anula=?,fecha_anula=?,hora_anula=?,motivo_anula=? where codigo=?";
    /**
     * query para consultar la información general de una transacción
     */
    private static final String existeModificacionInfoGeneralStr="SELECT " +
                                                                    "txa.fecha_elaboracion as fecha_elaboracion," +
                                                                    "txa.entidad as entidad," +
                                                                    "txa.observaciones as observaciones " +
                                                                 "FROM transacciones_x_almacen txa " +
                                                                 "INNER JOIN tipos_trans_inventarios tti ON (tti.consecutivo=txa.transaccion) " +
                                                                 "WHERE txa.codigo=? and tti.institucion=?";
    
    /**
     * Cadena para la busqueda de la entidad en la tabla terceros
     */
    private static final String busquedaEntidadStr="SELECT codigo, numero_identificacion, descripcion FROM terceros WHERE 1=1 ";
    
    /**
     * Metodo para verificar si existe alguna
     * modificación en la información general
     * de una transacción
     * @param con Connection
     * @param vo HashMap, con los datos a verificar
     * @return ResultadoBoolean, true si existe modificación
     * @author jarloc
     */
    public static ResultadoBoolean existeModificacionInfoGeneral(Connection con,HashMap vo)
    {        
        ResultadoBoolean r=new ResultadoBoolean(false);
        PreparedStatementDecorator ps;
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(existeModificacionInfoGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,vo.get("codigo")+"");
            ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion")+""));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
                if(!rs.getString("fecha_elaboracion").equals(vo.get("fecha_elaboracion")+""))
                {
                    r.setResultado(true);
                }
                if(!(rs.getString("entidad")==null?ConstantesBD.codigoNuncaValido+"":rs.getString("entidad")).equals(vo.get("entidad")+""))
                {
                    r.setResultado(true);
                }
                if(!rs.getString("observaciones").equals(vo.get("observaciones")+""))
                {
                    r.setResultado(true);
                }                
            }
                        
        } catch (SQLException e) 
        {            
            e.printStackTrace();            
        }
        return r;        
    }
    
    /**
     * 
     * @param con Connection
     * @param vo HashMap
     * @return HashMap
     * @author artotor
     */
    public static HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros", "0");
        String cadena=" where tvcc.institucion = "+vo.get("institucion");
        if(!vo.get("usuarioElabora").equals(""))
            cadena = cadena+" AND txa.usuario = '"+vo.get("usuarioElabora")+"'";
        if(!vo.get("fechaElaboracionInicial").equals("")&&!vo.get("fechaElaboracionFinal").equals(""))
            cadena = cadena+" AND to_char(txa.fecha_elaboracion, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaElaboracionInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaElaboracionFinal")+"")+"'";
        if(!vo.get("fechaCierreInicial").equals("")&&!vo.get("fechaCierreFinal").equals(""))
            cadena = cadena+" AND to_char(txa.fecha_cierre, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaCierreInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaCierreFinal")+"")+"'";
        if(Utilidades.convertirAEntero(vo.get("numTransaccionInicial")+"")>0&&Utilidades.convertirAEntero(vo.get("numTransaccionFinal")+"")>0)
            //Modificado por la Tarea 62076; ya que no hace el filtro correctamente
        	//Modificado nuevamente por la tarea 68682; 
        	cadena = cadena+" AND convertiranumero(txa.consecutivo||'') BETWEEN "+vo.get("numTransaccionInicial")+" AND "+vo.get("numTransaccionFinal");
        	//cadena = cadena+" AND txa.codigo BETWEEN '"+vo.get("numTransaccionInicial")+"' AND '"+vo.get("numTransaccionFinal")+"'";
        if(Utilidades.convertirAEntero(vo.get("codAlmacen")+"")>0)
            cadena = cadena+" AND txa.almacen = "+vo.get("codAlmacen");
        if(!vo.get("usuarioCierra").equals(""))
            cadena = cadena+" AND txa.usuario_cierre = '"+vo.get("usuarioCierra")+"'";
        if(!vo.get("codTransaccion").equals("-1"))
            cadena = cadena+" AND tti.consecutivo = "+vo.get("codTransaccion");
        if(!vo.get("estado").equals("-1"))
            cadena = cadena+" AND txa.estado = "+vo.get("estado");
        if(Utilidades.convertirAEntero(vo.get("centroAtencion")+"")>0)
            cadena = cadena+" AND cc.centro_atencion = "+vo.get("centroAtencion");
        if(!vo.get("indicativo_consignacion").equals(""))
            cadena = cadena+" AND tti.indicativo_consignacion = '"+vo.get("indicativo_consignacion")+"'";
        if(vo.containsKey("codTipo")&&!vo.get("codTipo").equals(""))
        {
	        if(Utilidades.convertirAEntero(vo.get("codTipo")+"")>0)
	            cadena = cadena+" AND tci.codigo = "+vo.get("codTipo");
        }

        cadena += " order by tvcc.tipos_trans_inventario, txa.fecha_elaboracion, txa.consecutivo";
        String sql = cadenaConsultaTransaccionesInstitucion+cadena;
        try
        {
            logger.info("===>Consulta Registro Transacciones: "+sql);
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            mapa.put("consultaWhere", cadena);
        }
        catch (SQLException e)
        {
            logger.error("Error generando la consulta del listado de transaccionses [SqlBaseRegistroTransacionesDao] "+e);
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    
    /**
     * Metodo para consultar el detalle de
     * articulos de una transacción de inventarios
     * @param con Connection
     * @param codTransaccion int
     * @return HashMap
     * @author artotor
     */
    public static HashMap consultarDetalleTransaccion(Connection con, String codTransaccion)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros", "0");
        String query=cadenaConsultaArticulosTran;
        query+=" from det_trans_x_almacen dtxa " +
    		   " inner join view_articulos va on(dtxa.articulo=va.codigo) " +
    		   " inner join articulo a on(va.codigo=a.codigo) " +
    		   " where dtxa.transaccion=?";
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            ps.setString(1,codTransaccion);               
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
            logger.info("----------------CONSULTA DETALLADA DE ARTICULO--------->>> "+query);
        }
        catch (SQLException e)
        {
            logger.error("Error generando la consulta del detalle de articulos de la transaccion [SqlBaseRegistroTransacionesDao] "+e);
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    /**
     * Metodo para consultar el detalle de
     * articulos de una transacción de inventarios
     * por almacen
     * @param con Connection
     * @param codTransaccion int
     * @param almacen int
     * @param institucion int 
     * @return HashMap
     * @author jarloc
     */
    public static HashMap consultarDetalleTransaccion(Connection con, String codTransaccion,int almacen,int institucion)
    {
        HashMap mapa=new HashMap();  
        //Double valorTotal=0.0;
        int k=0;
        try
        {
            String query=cadenaConsultaArticulosTran;
            query+=",getTotalExisArticulosXAlmacen(?,dtxa.articulo,?) as existenciaxalmacen,cantidad*val_unitario as valortotal " +
            	   " from det_trans_x_almacen dtxa " +
        		   " inner join view_articulos va on(dtxa.articulo=va.codigo) " +
        		   " inner join articulo a on(va.codigo=a.codigo) " +
        		   " where dtxa.transaccion=?";
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,almacen);
            ps.setInt(2,institucion);
            ps.setString(3,codTransaccion); 
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            while(rs.next())
            {
            	mapa.put("codigo_"+k, rs.getString("codigo"));
                mapa.put("codigoArticulo_"+k, rs.getString("codarticulo"));
                mapa.put("codigoInterfaz_"+k, rs.getString("codigointerfaz"));
                mapa.put("descripcionArticulo_"+k, rs.getString("descripcion"));
                mapa.put("unidadMedidaArticulo_"+k, rs.getString("unidadmedida"));
                mapa.put("cantidadArticulo_"+k, rs.getString("cantidadarticulo"));
                mapa.put("existenciaArticulo_"+k, rs.getString("existenciaxalmacen"));
                mapa.put("valorUnitarioArticulo_"+k, rs.getString("valorunitario"));
                mapa.put("loteArticulo_"+k, rs.getString("lote_articulo"));
                mapa.put("fechaVencimientoArticulo_"+k, rs.getString("fecha_vencimiento_articulo"));
                mapa.put("valorUnitarioArticuloTemp_"+k, rs.getString("valorunitario"));
                //valorTotal=(Utilidades.convertirAEntero(rs.getString("cantidadarticulo"))*Double.parseDouble(rs.getString("valorunitario")));
                mapa.put("valorTotalArticulo_"+k, UtilidadTexto.formatearValores(rs.getString("valortotal"),"#0.###"));
                k++;
            } 
            logger.info("----------------CONSULTA DETALLADA DE ARTICULO EN EL SEGUNDO METODO--------->>> "+query);
            mapa.put("numRegistros", k+"");
        }
        catch (SQLException e)
        {
            logger.error("Error generando la consulta del detalle de articulos de la transaccion [SqlBaseRegistroTransacionesDao] "+e);
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    /**
     * metodo para insertar la información general
     * de la transacción
     * @param con Connection
     * @param query String, con la consulta
     * @param consecutivo int
     * @param transaccion int
     * @param fechaElaboracion String
     * @param usuario String
     * @param entidad int
     * @param obsevaciones String
     * @param estado int
     * @return boolean
     * @author jarloc
     * @param almacen 
     * @param automatica 
     */
    public static boolean insertarInformacionGeneralTransaccion(Connection con,String query,int consecutivo,int transaccion,String fechaElaboracion,String usuario,int entidad,String obsevaciones,int estado, int almacen, boolean automatica)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	       
	        logger.info("\n\n ENCABEZADO DE LA TRANSACCION----->"+query);
	        //" " usu->"+usuario+" entidad->"+entidad+" observ->"+obsevaciones+" automatica->"+automatica
	        
	        logger.info("consecutivo -->"+consecutivo);
	        logger.info("transaccion -->"+transaccion);
	        logger.info("fechaElaboracion -->"+fechaElaboracion);
	        logger.info("usuario -->"+usuario);
	        logger.info("entidad -->"+entidad);
	        logger.info("obsevaciones -->"+obsevaciones);
	        logger.info("estado -->"+estado);
	        logger.info("almacen -->"+almacen);
	        logger.info("automatica -->"+automatica);

	        /**
	         * INSERT INTO transacciones_x_almacen " +
										    		"(codigo,consecutivo," +
										    		"transaccion,fecha_elaboracion," +
										    		"usuario,entidad,observaciones,estado,almacen,automatica) " +
										    		"VALUES ('seq_transacciones_x_almacen'),?,?,?,?,?,?,?,?,?)
	         */
	        
	        ps.setString(1,consecutivo+"");
	        ps.setInt(2,transaccion);
	        ps.setDate(3,Date.valueOf(fechaElaboracion));
	        ps.setString(4,usuario);
	        if(entidad!=ConstantesBD.codigoNuncaValido)
	            ps.setInt(5,entidad);
	        else
	            ps.setNull(5,Types.INTEGER);
	        ps.setString(6,obsevaciones);
	        ps.setInt(7,estado);
	        ps.setInt(8,almacen==ConstantesBD.codigoNuncaValido?null:almacen);
	        if(automatica)
	        	ps.setString(9, ConstantesBD.acronimoSi);
	        else
	        	ps.setString(9, ConstantesBD.acronimoNo);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error insertarInformacionGeneralTransaccion"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para verificar si un registro
     * posee entrda en la BD
     * @param con Connection
     * @param codigo int
     * @return HashMap
     * @author jarloc
     */
    public static HashMap existeRegistroTransaccionEnBD(Connection con,int codigo)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(existeTransaccionEnBDStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        logger.info("--->"+existeTransaccionEnBDStr);
	        logger.info("--->"+codigo);
	        ps.setString(1,codigo+"");
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch (SQLException e)
        {
            logger.error("Error existeRegistroTransaccionEnBD",e);
            e.printStackTrace();
            return null;
        }
    }
   /**
    * metodo para actualizar la información general 
    * de la transacción en caso de ser modificada
    * @param con Connection
    * @param codigo int
    * @param observaciones String
    * @param entidad int
    * @param fechaElaboracion String
    * @return boolean
    * @author jarloc
    */
    public static boolean actualizarInformacionGeneral(Connection con,int codigo,String observaciones,int entidad,String fechaElaboracion)
    {
        try
        {
        	
	        int resp=0;
	        String query=actualizarInformacionGeneralStr;
	        boolean esPrimero=true;
	        if(!observaciones.equals(""))
	        {
	            query+=" observaciones='"+observaciones+"'";
	            esPrimero=false;
	        }
	        if(entidad!=ConstantesBD.codigoNuncaValido)
	        {
	            if(!esPrimero)
	                query+=" , ";
	            query+=" entidad="+entidad;
	            esPrimero=false;
	        }
	        if(!fechaElaboracion.equals(""))
	        {
	            if(!esPrimero)
	                query+=" , ";
	            query+=" fecha_elaboracion='"+fechaElaboracion+"'";
	            esPrimero=false;
	        }       
	        query+=" WHERE codigo="+codigo;
	        PreparedStatementDecorator ps = null;
	        logger.info("--->"+query);
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   
	        resp=ps.executeUpdate();
	        ps.close();
	        if(resp>0)
	            return true;
	        else
	            return false;
        }
        catch (SQLException e)
        {
            logger.error("Error actualizarInformacionGeneral",e);
            return false;
        } 
    }
    /**
     * metodo para insertar el detalle de 
     * la transacción
     * @param con Connection
     * @param transaccion int
     * @param articulo int
     * @param cantidad int
     * @param valorUnitario String
     * @return boolean
     * @author jarloc
     * @param fechaVencimiento 
     * @param lote 
     * @param proveedorCatalogo 
     * @param proveedorCompra 
     */
    public static boolean insertarDetalleTransaccion(Connection con,int transaccion,int articulo,int cantidad,String valorUnitario, String lote, String fechaVencimiento, String proveedorCompra, String proveedorCatalogo)
    {
        try
        {
	        PreparedStatementDecorator ps = null;
	        logger.info("DETALLE TRANSACCION->"+insertarDetalleTransaccionStr+" transa->"+transaccion+" art->"+articulo+" cant->"+cantidad+" valUni->"+valorUnitario+" lote->"+lote+" fechaVen->"+fechaVencimiento+" proveedorCompra->"+proveedorCompra+" proveedorCatalogo->"+proveedorCatalogo);
	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(insertarDetalleTransaccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         * INSERT INTO det_trans_x_almacen(transaccion,articulo,cantidad,val_unitario,lote,fecha_vencimiento) VALUES (?,?,?,?,?,?)
	         */
	        
	        ps.setString(1,transaccion+"");
	        ps.setInt(2,articulo);
	        ps.setInt(3,cantidad);
	        ps.setDouble(4,Utilidades.convertirADouble(valorUnitario));
	        if(lote.trim().equals(""))
	        {
	        	ps.setNull(5, Types.VARCHAR);
	        	ps.setNull(6, Types.DATE);
	        }
	        else
	        {
	        	ps.setString(5, lote);
	        	if(fechaVencimiento.trim().equals(""))
	        	{
	        		ps.setNull(6, Types.DATE);
	        	}
	        	else
	        	{
	        		ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento)));	
	        	}
	        }    
	        if(UtilidadTexto.isEmpty(proveedorCompra))
	        {
	        	ps.setObject(7, null);
	        }
	        else
	        {
	        	ps.setString(7, proveedorCompra);
	        	
	        }
	        if(UtilidadTexto.isEmpty(proveedorCatalogo))
	        {
	        	ps.setObject(8, null);
	        }
	        else
	        {
	        	ps.setString(8, proveedorCatalogo);
	        }
	        ps.setInt(9, UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_trans_x_almacen"));
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error insertarDetalleTransaccion"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para insertar el detalle de la
     * transacción
     * @param con Connection
     * @param codigoTrans int
     * @param articulo int
     * @return HashMap
     * @author jarloc
     */
    public static HashMap existeDetalleTransaccionEnBD(Connection con,String codigoDetalle)
    {
        try
        {
	        PreparedStatementDecorator ps = null;
	        String cadena=existeDetalleTransaccionStr;
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,Utilidades.convertirAEntero(codigoDetalle));

	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        return mapa;
        }
        catch (SQLException e)
        {
            logger.error("Error existeDetalleTransaccionEnBD"+e);
            e.printStackTrace();
            return null;
        }
    }
    /**
     * metodo para actualizar la información
     * del detalle de la transacción
     * @param con Connection
     * @param codigoTrans int
     * @param articulo int
     * @param cantidad int
     * @param valorUnitario String
     * @return boolean
     * @author jarloc
     * @param fechaVencimiento 
     * @param lote 
     */
    public static boolean actualizarDetalleTransaccion(Connection con,int codigo,int cantidad,String valorUnitario, String lote, String fechaVencimiento)
    {
        try
        {
	        int resp=0;
	        String query=actualizarDetalleTransaccionStr;
	        boolean esPrimero=true;
	        
	        if(cantidad!=ConstantesBD.codigoNuncaValido)
	        {	            
	            query+=" cantidad="+cantidad;
	            esPrimero=false;
	        }
	        if(!valorUnitario.equals(""))
	        {
	            if(!esPrimero)
	                query+=" , ";
	            query+=" val_unitario='"+valorUnitario+"'";
	            esPrimero=false;
	        }     
	        query+=" ,lote=?,fecha_vencimiento=? ";
	        query+=" WHERE codigo="+codigo;
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        if(lote.trim().equals(""))
	        {
	        	ps.setNull(1, Types.VARCHAR);
	        	ps.setNull(2, Types.DATE);
	        }
	        else
	        {
	        	ps.setString(1, lote);
	        	if(fechaVencimiento.trim().equals(""))
	        	{
	        		ps.setNull(2, Types.DATE);
	        	}
	        	else
	        	{
	        		ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento)));	
	        	}
	        }
	        resp=ps.executeUpdate();
	        if(resp>0)
	            return true;
	        else
	            return false;
        }
        catch (SQLException e)
        {
            logger.error("Error actualizarDetalleTransaccion"+e);
            e.printStackTrace();
            return false;
        } 
    }
    /**
     * metodo para obtener el ultimo codigo
     * de la transacción insertada
     * @param con Connection
     * @return int
     * @author jarloc
     */
    public static int obtnerCodigoTransaccionInsertada(Connection con)
    {        
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(ultimoCodigoTransaccionInsertadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	        
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	            return rs.getInt("codigo");	         
        }
        catch (SQLException e)
        {
            logger.error("Error obtnerCodigoTransaccionInsertada"+e);
            e.printStackTrace();            
        }
        return ConstantesBD.codigoNuncaValido;
    }
    /**
     * metodo para actualizar el estado de la
     * transacción
     * @param con Connection
     * @param estado int
     * @param codigoTransaccion int
     * @return boolean
     * @author jarloc
     */
    public static boolean actualizarEstadoTransaccion(Connection con,int estado,int codigoTransaccion)
    {
        try
        {            
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoTransaccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,estado);
	        ps.setString(2,codigoTransaccion+"");
	        int resp=ps.executeUpdate();
	        if(resp>0)
	            return true;
	        else
	            return false;
	        
        }
        catch (SQLException e)
        {
            logger.error("Error actualizarEstadoTransaccion"+e);
            e.printStackTrace();  
            return false;
        }
    }
    /**
     * metodo para generar el registro de ajustes,
     * al costo promedio
     * @param con Connection
     * @param codigo String
     * @param articulo int
     * @param almacen int
     * @param tipoTransaccion int
     * @param transaccion String
     * @param vlrCostoAntes double
     * @param vlrCostoDespues double
     * @param tipoCosto int
     * @return boolean
     * @author jarloc
     */
    public static boolean generarRegistroAjuste(Connection con,String codigo,int articulo,int almacen,int tipoTransaccion,String transaccion,double vlrCostoAntes,double vlrCostoDespues,int tipoCosto)
    {        
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(registrarAjusteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("CODIGO DEL AJUSTE DEL COSTO DEL ARTICULO=> "+codigo);
            
            /**
             * INSERT " +
							"INTO ajustes_costo_articulos" +
							"(codigo,codarticulo,fecha," +
							"hora,almacen,tipo_transaccion," +
							"numero_transaccion,valor_costo_antes," +
							"valor_costo_despues,tipo_ajuste_costo) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?)
             */
            
            
            ps.setString(1,codigo);
            ps.setInt(2,articulo);
            ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
            ps.setString(4,UtilidadFecha.getHoraActual());
            ps.setInt(5,almacen);
            ps.setInt(6,tipoTransaccion);
            ps.setString(7,transaccion);
            ps.setDouble(8,vlrCostoAntes);
            ps.setDouble(9,vlrCostoDespues);
            ps.setInt(10,tipoCosto);
            int resp=ps.executeUpdate();
            if(resp>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para insertar el registro del 
     * cierre de la transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String
     * @param fecha String
     * @param hora String
     * @return boolean
     */
    public static boolean generarRegistroCierreTransaccion(Connection con,String transaccion,String usuario,String fecha,String hora)
    {
    	 try
	        {
    		 	
    		 	//actualizar el resto de informacion.
	            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(registrarCierreTransaccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            logger.info("generarRegistroCierreTransaccion->"+registrarCierreTransaccionStr+" ->"+ConstantesBDInventarios.codigoEstadoTransaccionInventarioCerrada+" usu->"+usuario+" fecha->"+fecha+" hora->"+hora+" tran->"+transaccion);
	            /**
	             *  UPDATE transacciones_x_almacen set estado=?, usuario_cierre=?,fecha_cierre=?,hora_cierre=? where codigo=?
	             */
	            
	            ps.setInt(1,ConstantesBDInventarios.codigoEstadoTransaccionInventarioCerrada);
	            ps.setString(2,usuario);
	            ps.setDate(3,Date.valueOf(fecha));
	            ps.setString(4,hora);
	            ps.setString(5,transaccion);
	            int resp=ps.executeUpdate();	            
	            if(resp>0)
	                return true;
	            else
	                return false;
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	            return false;
	        }
    }
    /**
     * metodo para generar la anulación de la
     * transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String 
     * @param motivo String
     * @return boolean
     */
    public static boolean generarAnulacionTransaccion(Connection con,String transaccion,String usuario,String motivo)
    {
    	 try
	        {
	            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(generarAnulacionTransaccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            /**
	             * UPDATE transacciones_x_almacen set estado=?, usuario_anula=?,fecha_anula=?,hora_anula=?,motivo_anula=? where codigo=?
	             */
	            
	            ps.setInt(1, ConstantesBDInventarios.codigoEstadoTransaccionInventarioAnulada);
	            ps.setString(2, usuario);
	            ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
	            ps.setString(4, UtilidadFecha.getHoraActual());
	            ps.setString(5, motivo);
	            ps.setString(6, transaccion);
	            int resp=ps.executeUpdate();	            
	            if(resp>0)
	                return true;
	            else
	                return false;
	        }
    	 catch (SQLException e)
	        {
	            e.printStackTrace();
	            return false;
	        }
    }

    /**
     * 
     * @param con
     * @param transaccion
     * @param codArt
     * @return
     */
    public static boolean eliminarDetalleTransaccion(Connection con, int codigo) 
	{
		try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleTransaccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigo);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error insertarDetalleTransaccion"+e);
            e.printStackTrace();
            return false;
        }
	}
    
    /**
     * obtiene los nombres para adicionarlos a la consulta de birt
     * @param con
     * @param mapaNombresYRestricciones
     * @return
     */
    public static HashMap getNombresReporteBirt(Connection con, HashMap mapaNombresYRestricciones) 
	{
    	String consulta="";
    	HashMap mapToReturn= new HashMap();
    	if(mapaNombresYRestricciones.containsKey("esBusquedaPorFarmacia"))
    	{	
    		consulta="select nombre AS descripcion from centros_costo where codigo="+mapaNombresYRestricciones.get("esBusquedaPorFarmacia");
    		mapToReturn.put("nombreFarmacia", ejecucionGenericaConsultaNombres(con, consulta));
    	}
    	if(mapaNombresYRestricciones.containsKey("esBusquedaPorTransaccion"))
    	{	
    		consulta="select descripcion from tipos_trans_inventarios where consecutivo="+mapaNombresYRestricciones.get("esBusquedaPorTransaccion");
    		mapToReturn.put("nombreTransaccion", ejecucionGenericaConsultaNombres(con, consulta));
    	}	
    	if(mapaNombresYRestricciones.containsKey("esBusquedaPorEstado"))
    	{	
    		consulta="select descripcion from estados_transacciones_inv where codigo ="+mapaNombresYRestricciones.get("esBusquedaPorEstado");
    		mapToReturn.put("nombreEstado", ejecucionGenericaConsultaNombres(con, consulta));
    	}
    	logger.info("--CONSULTA CON BIRT-->>"+consulta+"<--");
    	return mapToReturn	;
   }
    
    /**
     * 
     * @param con
     * @param consulta
     * @return
     */
    private static String ejecucionGenericaConsultaNombres(Connection con, String consulta)
    {
    	String desc="";
    	try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	        	desc=rs.getString("descripcion");
        }
        catch (SQLException e)
        {
            logger.error("Error ejecucionGenericaConsultaNombres"+e);
            e.printStackTrace();
        }
        return desc;
    }
    
    /**
     * Busqueda de la entidad
     */
    public static Collection buscarEntidad(Connection con, String codigoEntidad, String descripcionEntidad)
    {
    	String cadena=busquedaEntidadStr;
    	if(!codigoEntidad.equals(""))
    	{
    		cadena+="AND numero_identificacion='"+codigoEntidad+"' ";
    	}
    	if(!descripcionEntidad.equals(""))
    	{
    		cadena+="AND UPPER(descripcion) LIKE UPPER('%"+descripcionEntidad+"%')";
    	}
    	cadena+=" ORDER BY descripcion";
    	try
    	{
    		PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(busqueda.executeQuery()));
    	}
    	catch(SQLException e)
    	{
    		logger.info("Error en la B&uacute;squeda de la Entidad "+e);
    		return null;
    	}
    }
    
    /**
     * Método encargado de consultar los tipos de transacción dado un condigo de transacción
     * @param con
     * @param codTransaccion
     * @return HashMap
     */
    public static HashMap consultaTipoTransaccion(Connection con, String codTransaccion) 
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = 
				"SELECT " +
					"tipos_conceptos_inv " +
				"FROM tipos_trans_inventarios " +
				"WHERE codigo='"+codTransaccion+"'";
				
			logger.info("====>Consulta Tipo Transaccion: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("===> Error en consultaTipoTransaccion: "+e);
		}
		return mapa;
	}
    
}
