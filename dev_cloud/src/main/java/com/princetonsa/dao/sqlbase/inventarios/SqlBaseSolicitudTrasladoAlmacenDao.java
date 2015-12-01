/*
 * Creado   11/01/20056 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.inventarios.ConstantesBDInventarios;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase que implementa los metodos para realizar las
 * respectivas operaciones sobre la BD.
 *
 * @version 1.0, 11/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseSolicitudTrasladoAlmacenDao 
{
    /**
     * manejador de los logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseSolicitudTrasladoAlmacenDao.class);
    /**
     * query para consultar los almacenes validos
     */
    private static final String insertarInfoGeneralSolicitudStr="INSERT INTO " +
																    		"solicitud_traslado_almacen" +
																    		"(numero_traslado," +
																    		"almacen_solicitado," +
																    		"almacen_solicita," +
																    		"estado," +
																    		"prioritario," +
																    		"fecha_elaboracion," +
																    		"hora_elaboracion," +
																    		"usuario_elabora," +
																    		"fecha_grabacion," +
																    		"hora_grabacion," +
																    		"observaciones) " +
																    		"VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    
    /**
     * query para actualizar el estado de la transacción
     */
    private static final String actualizarEstadoSolicitudStr="UPDATE solicitud_traslado_almacen SET estado=? where numero_traslado=? and almacen_solicita in(select codigo from centros_costo where institucion=?)";
    /**
     * query para insertar el detalle de la solicitud
     */
    private static final String insertarDetalleSolicitudStr="INSERT INTO det_sol_traslado_almacen(numero_traslado,articulo,cantidad) VALUES (?,?,?)";
    /**
     * query para insertar la información del cierre
     */
    private static final String cerrarTraslado="UPDATE solicitud_traslado_almacen SET estado=?,usuario_cierre=?,fecha_cierre=?,hora_cierre=? where numero_traslado=? ";
    /**
     * query para eliminar articulos del detalle de la solicitud
     */
    private static final String eliminarArticuloDetalleSolicitudStr="DELETE FROM det_sol_traslado_almacen where numero_traslado=? and articulo=?";
    /**
     * query para insertar la informacion de la anulación de la solicitud
     */
    private static final String insertarAnulacionSolicitudStr=" UPDATE solicitud_traslado_almacen SET estado=?,usuario_anula=?,fecha_anula=?,hora_anula=?,motivo_anula=? where numero_traslado=? ";
    /**
     * query para consultar solicitudes
     */
    private static final String consultaSolicitudStr="SELECT " +
											    		"sa.numero_traslado as numero_traslado," +
											    		"sa.estado as estado," +
											    		"e.descripcion as nombre_estado," +
											    		"sa.prioritario as prioritario," +
											    		"sa.fecha_elaboracion as fecha_elaboracion," +
											    		"sa.hora_elaboracion as hora_elaboracion," +
											    		"sa.usuario_elabora as usuario_elabora," +
											    		"sa.almacen_solicita as almacen_solicita," +
											    		"getnomcentrocosto(sa.almacen_solicita) as nombre_almacen_solicita," +
											    		"getnomcentrocosto(sa.almacen_solicitado) as almacen_despacha," +
											    		"sa.observaciones as observaciones " +
										    		 "from solicitud_traslado_almacen sa " +
										    		 "inner join estados_traslado_almacen e on(sa.estado=e.codigo) " +
										    		 "where sa.almacen_solicita in(select codigo from centros_costo where institucion=?) " +
										    		 "and sa.estado="+ConstantesBDInventarios.codigoEstadoTrasladoInventarioPendiente;
    /**
     * query para consultar el detalle de los articulos de la solicitud
     */
    private static final String consultaDetalleSolicitudStr="SELECT " +												    		
												    		"dts.articulo as articulo," +
												    		"dts.cantidad as cantidad," +
												    		"getdescarticulosincodigo(va.codigo) AS descripcion," +
												    		/*"va.descripcion " +
												    		"||' CONC:'|| va.concentracion " +
												    		"||' F.F:'|| getNomFormaFarmaceutica(va.forma_farmaceutica) " +
												    		"|| ' NAT:' || va.descripcionnaturaleza " +
												    		"|| CASE WHEN va.es_pos= '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN ' - POS' WHEN va.es_pos ='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN ' - NOPOS' ELSE '' END AS descripcion," +*/
												    		"getNomUnidadMedida(va.unidad_medida) AS unidadmedida  " +
											    		 "from det_sol_traslado_almacen dts " +
											    		 "inner join view_articulos va on(dts.articulo=va.codigo) " +
											    		 "where dts.numero_traslado=?";
   /**
    * query para actualizar la información general de la solicitud
    */
    private static final String actualizarInformacionGeneralSolicitudStr="UPDATE solicitud_traslado_almacen SET ";
    /**
     * query para actualizar el detalle de la solicitud
     */
    private static final String actualizarDetalleSolicitudStr="UPDATE det_sol_traslado_almacen SET cantidad=? where numero_traslado=? and articulo=?";
    /**
     * query para consultar la cantidad de un articulo
     */
    private static final String consultarArticuloSolicitudStr="SELECT cantidad as cantidad from det_sol_traslado_almacen where numero_traslado=? and articulo=?";
    
    /**
     * metodo para insertar la información
     * general de la solicitud
     * @param con Connection
     * @param numeroSol int 
     * @param almacenSolicitado int
     * @param almacenSolicita int
     * @param estado int 
     * @param esPrioritario boolean 
     * @param fechaElaboracion String
     * @param horaElaboracion String
     * @param usuarioElabora String
     * @param fechaGrabacion String
     * @param horaGrabacion String
     * @param observaciones String
     * @return booelan 
     */    
    public static boolean insertarInformacionGeneralSolicitud(Connection con,int numeroSol,int almacenSolicitado,int almacenSolicita,int estado,boolean esPrioritario,String fechaElaboracion,String horaElaboracion,String usuarioElabora,String fechaGrabacion,String horaGrabacion,String observaciones)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(insertarInfoGeneralSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         * INSERT INTO " +
				    		"solicitud_traslado_almacen" +
				    		"(numero_traslado," +
				    		"almacen_solicitado," +
				    		"almacen_solicita," +
				    		"estado," +
				    		"prioritario," +
				    		"fecha_elaboracion," +
				    		"hora_elaboracion," +
				    		"usuario_elabora," +
				    		"fecha_grabacion," +
				    		"hora_grabacion," +
				    		"observaciones) " +
				    		"VALUES (?,?,?,?,?,?,?,?,?,?,?)"
	         */
	        if((observaciones+"").length()>255)
	        observaciones=(observaciones+"").substring(0, 255);
	        ps.setInt(1, numeroSol);
	        ps.setInt(2, almacenSolicitado);
	        ps.setInt(3, almacenSolicita);
	        ps.setInt(4, estado);
	        ps.setBoolean(5,esPrioritario);
	        ps.setDate(6, Date.valueOf(fechaElaboracion));
	        ps.setString(7, horaElaboracion);
	        ps.setString(8, usuarioElabora);
	        ps.setDate(9, Date.valueOf(fechaGrabacion));
	        ps.setString(10, horaGrabacion);
	        ps.setString(11, observaciones);
	        int r=ps.executeUpdate();
            if(r>0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error insertarInformacionGeneralSolicitud"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para insertar el detalle de la 
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param codArticulo int
     * @param cantidad int
     * @return booelan
     */
    public static boolean insertarDetalleSolicitud(Connection con,int numeroSolicitud,int codArticulo,int cantidad)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(insertarDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         * INSERT INTO det_sol_traslado_almacen(numero_traslado,articulo,cantidad) VALUES (?,?,?)
	         */
	        
	        ps.setInt(1,numeroSolicitud);
	        ps.setInt(2, codArticulo);
	        ps.setInt(3, cantidad);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error insertarDetalleSolicitud"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para actualizar el estado de la solicitud
     * @param con Connection 
     * @param estado int
     * @param numeroSolicitud int
     * @param institucion int
     * @return boolean
     */
    public static boolean actualizarEstadoSolicitud(Connection con,int estado,int numeroSolicitud,int institucion)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         * UPDATE solicitud_traslado_almacen SET 
	         * estado=? 
	         * where 
	         * numero_traslado=? 
	         * and almacen_solicita in(select codigo from centros_costo 
	         * where institucion=?)
	         */
	        
	        ps.setInt(1,estado);
	        ps.setInt(2,numeroSolicitud);
	        ps.setInt(3, institucion);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error actualizarEstadoSolicitud"+e);
            e.printStackTrace();
            return false;
        }        
    }
    /**
     * metodo para insertar la información del cierre dela
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param usuario Stirng
     * @return boolean
     */
    public static boolean insertarInformacionCierre(Connection con,int numeroSolicitud,String usuario)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(cerrarTraslado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         * UPDATE solicitud_traslado_almacen SET estado=?,usuario_cierre=?,fecha_cierre=?,hora_cierre=? where numero_traslado=? 
	         */
	        
	        ps.setInt(1, ConstantesBDInventarios.codigoEstadoTrasladoInventarioCerrada);
	        ps.setString(2, usuario);
	        ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
	        ps.setString(4,UtilidadFecha.getHoraActual());
	        ps.setInt(5,numeroSolicitud);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error insertarInformacionCierreStr"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para eliminar articulos del detalle
     * de la solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return boolean
     */
    public static boolean eliminarArticuloDetalleSolicitud(Connection con,int numeroSolicitud,int articulo)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(eliminarArticuloDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,numeroSolicitud);
	        ps.setInt(2,articulo);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error eliminarArticuloDetalleSolicitud"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * metodo para insertar la información de la 
     * anulación de la solicitud
     * @param con Connection
     * @param numeroSolicitud int 
     * @param usuario String
     * @param motivo String
     * @return boolean
     */
    public static boolean insertarAnulacionSolicitud(Connection con,int numeroSolicitud,String usuario,String motivo)
    {
        try
        {
	        PreparedStatementDecorator ps = null;            
	        ps= new PreparedStatementDecorator(con.prepareStatement(insertarAnulacionSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         *  UPDATE solicitud_traslado_almacen SET estado=?,usuario_anula=?,fecha_anula=?,hora_anula=?,motivo_anula=? where numero_traslado=? 
	         */
	        
	        
	        ps.setInt(1,ConstantesBDInventarios.codigoEstadoTrasladoInventarioAnulada);
	        ps.setString(2,usuario);
	        ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
	        ps.setString(4,UtilidadFecha.getHoraActual());
	        ps.setString(5,motivo);
	        ps.setInt(6,numeroSolicitud);
	        int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
        }
        catch (SQLException e)
        {
            logger.error("Error insertarAnulacionSolicitud"+e);
            e.printStackTrace();
            return false;
        }
    }
    /**
    * metodo para generar la busqueda avanzada
    * de solicitudes
    * @param con Connection
    * @param vo HashMap
    * @return HashMap 
    */
   public static HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
   {
       HashMap mapa=new HashMap();
       String cadena="";
       if(vo.containsKey("numero_traslado_inicial"))
       {
	       if(!vo.get("numero_traslado_inicial").equals("") && !vo.get("numero_traslado_final").equals(""))
	           cadena=cadena+" and sa.numero_traslado between "+vo.get("numero_traslado_inicial")+" and "+vo.get("numero_traslado_final");
	       if(!vo.get("numero_traslado_inicial").equals("") && vo.get("numero_traslado_final").equals(""))
	           cadena=cadena+" and sa.numero_traslado="+vo.get("numero_traslado_inicial");
	       if(!vo.get("numero_traslado_final").equals("") && vo.get("numero_traslado_inicial").equals(""))
	           cadena=cadena+" and sa.numero_traslado="+vo.get("numero_traslado_final");             
	       if(!vo.get("fecha_elaboracion_inicial").equals("") && !vo.get("fecha_elaboracion_final").equals(""))
	           cadena=cadena+" and sa.fecha_elaboracion between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_elaboracion_inicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_elaboracion_final")+"")+"'";
	       if(!vo.get("fecha_elaboracion_inicial").equals("") && vo.get("fecha_elaboracion_final").equals(""))
	           cadena=cadena+" and sa.fecha_elaboracion='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_elaboracion_inicial")+"")+"'";
	       if(!vo.get("fecha_elaboracion_final").equals("") && vo.get("fecha_elaboracion_inicial").equals(""))
	           cadena=cadena+" and sa.fecha_elaboracion='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_elaboracion_final")+"")+"'";           
	       if(!vo.get("almacen_despacha").equals(""))
	           cadena=cadena+" and sa.almacen_solicitado="+vo.get("almacen_despacha");
	       if(!vo.get("almacen_solicitante").equals(""))
	           cadena=cadena+" and sa.almacen_solicita="+vo.get("almacen_solicitante");
	       if(!vo.get("usuario_elabora").equals(""))
	           cadena=cadena+" and sa.usuario_elabora='"+vo.get("usuario_elabora")+"'";
	       if(!vo.get("estado").equals(""))
	           cadena=cadena+" and sa.estado="+vo.get("estado");
       }
       String sql=consultaSolicitudStr+cadena+" order by sa.numero_traslado ";
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setInt(1,Utilidades.convertirAEntero(vo.get("institucion")+""));
           mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
       }
       catch (SQLException e)
       {
           logger.error("Error generando la consulta del listado de solicitudes [SqlBaseSolicitudTrasladoAlmacenDao] "+e);
           e.printStackTrace();
       }
       return (HashMap)mapa.clone();       
   }
   /**
    * metodo para consultar el detalle de la
    * solicitud
    * @param con Connection
    * @param numeroSolicitud int
    * @return HashMap
    */
   public static HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud)
   {
	   logger.info("\n consultaDetalleSolicitud numero solicitud --> "+numeroSolicitud);
       HashMap mapa=new HashMap();
       int pos=0;
       logger.info("\n cadena -->"+consultaDetalleSolicitudStr);
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setInt(1,numeroSolicitud);
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
           while(rs.next())
           {               
               mapa.put("codigoArticulo_"+pos,rs.getString("articulo"));
               mapa.put("cantidadArticulo_"+pos,rs.getString("cantidad"));
               if (UtilidadCadena.noEsVacio(rs.getString("descripcion")))
            	   mapa.put("descripcionArticulo_"+pos,rs.getString("descripcion"));
               else
            	   mapa.put("descripcionArticulo_"+pos,"");
               
               mapa.put("unidadMedidaArticulo_"+pos,rs.getString("unidadmedida"));
               mapa.put("tipoRegistro_"+pos,"BD");
               pos ++;
           }           
           mapa.put("numRegistros",pos+"");
       }
       catch (SQLException e)
       {
           logger.error("Error generando la consulta del detalle de la solicitud [SqlBaseSolicitudTrasladoAlmacenDao] "+e);
           e.printStackTrace();
       }
       return (HashMap)mapa.clone();
   }
   /**
    * metodo para actualizar la información 
    * general de la solicitud
    * @param con Connection
    * @param vo HashMap
    * @return boolean
    */
   public static boolean actualizarInformacionGeneralSolicitud(Connection con,HashMap vo)
   {
       String cadena="";
       boolean esPrimero=true;       
       if(!vo.get("prioritario").equals(""))
       {
           cadena=cadena+" prioritario='"+vo.get("prioritario")+"'";
           esPrimero=false;
       }
       if(!vo.get("fecha_elaboracion").equals(""))
       {
          if(!esPrimero)
              cadena+=" , ";
           cadena=cadena+" fecha_elaboracion='"+vo.get("fecha_elaboracion")+"'";
           esPrimero=false;
       }
       if(!vo.get("hora_elaboracion").equals(""))
       {
          if(!esPrimero)
              cadena+=" , ";
           cadena=cadena+" hora_elaboracion='"+vo.get("hora_elaboracion")+"'";
           esPrimero=false;
       }
       if(!vo.get("observaciones").equals(""))
       {
          if(!esPrimero)
              cadena+=" , ";
          
          logger.info("observaciones -> "+vo.get("observaciones"));
          
          if(vo.get("observaciones").toString().length()>255)
        	  cadena=cadena+" observaciones='"+vo.get("observaciones").toString().substring(0, 255)+"' ";
          else
        	  cadena=cadena+" observaciones='"+vo.get("observaciones")+"' ";
          
           esPrimero=false;
       }
       cadena+=" where numero_traslado=? and almacen_solicita in(select codigo from centros_costo where institucion=?)";
       String query=actualizarInformacionGeneralSolicitudStr+cadena;
       try
       {
    	   logger.info("QUERY -> "+query);
           logger.info("Traslado - "+vo.get("numero_traslado"));
           logger.info("Institucion - "+vo.get("institucion"));
    	   
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setInt(1,Utilidades.convertirAEntero(vo.get("numero_traslado")+""));
           ps.setInt(2,Utilidades.convertirAEntero(vo.get("institucion")+""));
           
           int r=ps.executeUpdate();
           if(r>0)
               return true;
           else
               return false;
       }
       catch (SQLException e)
       {
           logger.error("Error actualizarInformacionGeneralSolicitud"+e);
           e.printStackTrace();
           return false;
       }
   }
   /**
    * metodo para actualizar el detalle de
    * la solicitud
    * @param con Connection
    * @param numeroSolicitud int
    * @param cantidad int
    * @param articulo int
    * @return boolean
    */
   public static boolean actualizarDetalleSolicitud(Connection con,int numeroSolicitud,int articulo,int cantidad)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
           
           /**
            * UPDATE det_sol_traslado_almacen SET cantidad=? where numero_traslado=? and articulo=?
            */
           
           ps.setInt(1,cantidad);
           ps.setInt(2,numeroSolicitud);
           ps.setInt(3,articulo);
           int r=ps.executeUpdate();
           if(r>0)
               return true;
           else
               return false;
       }
       catch (SQLException e)
       {
           logger.error("Error actualizarDetalleSolicitud"+e);
           e.printStackTrace();
           return false;
       }
   }
   /**
    * metodo para consultar la cantidad de un
    * articulo para una solicitud
    * @param con Connection
    * @param numeroSolicitud int
    * @param articulo int
    * @return int
    */
   public static int consultarInfoArticuloSolicitud(Connection con,int numeroSolicitud,int articulo)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarArticuloSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 
           ps.setInt(1, numeroSolicitud);
           ps.setInt(2, articulo);
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
           if(rs.next())
               return rs.getInt("cantidad");
           else
               return ConstantesBD.codigoNuncaValido;
       }
       catch (SQLException e)
       {
           logger.error("Error consultarInfoArticuloSolicitud"+e);
           e.printStackTrace();
           return ConstantesBD.codigoNuncaValido;
       }       
   }
   /**
    * metodo para consultar la información
    * del detalle de una solicitud para un
    * articulo
    * @param con Connection
    * @param numeroSolicitud int
    * @param articulo int
    * @return HashMap
    */
   public static HashMap consultarDetalleSolicitudUnArticulo(Connection con,int numeroSolicitud,int articulo)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleSolicitudStr+" and dts.articulo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 
           ps.setInt(1, numeroSolicitud);
           ps.setInt(2, articulo);
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());           
           HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;          
       }
       catch (SQLException e)
       {
           logger.error("Error consultarDetalleSolicitudUnArticulo"+e);
           e.printStackTrace();
           return null;
       }       
   }
}
