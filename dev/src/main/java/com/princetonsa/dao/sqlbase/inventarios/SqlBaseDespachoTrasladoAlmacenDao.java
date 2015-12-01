/*
 * Creado   23/01/2006
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
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase que implementa los metodos que se comunican
 * con la fuente de datos.
 *
 * @version 1.0, 23/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseDespachoTrasladoAlmacenDao 
{
    /**
    * manejador de los logs de la clase
    */
   private static Logger logger=Logger.getLogger(SqlBaseDespachoTrasladoAlmacenDao.class);
   /**
    * query para consultar el listado de traslados almacen
    */
   private static final String consultaTrasladosAlmacenesStr="SELECT " +
   																" ta.numero_traslado as numero_traslado," +
   																" ta.hora_elaboracion as hora_elaboracion," +
   																" ta.fecha_elaboracion as fecha_elaboracion," +
   																" ta.prioritario as prioritario," +
   																" ta.usuario_elabora as usuario_elabora," +
   																" getnomcentrocosto(ta.almacen_solicita) as almacen_solicitante," +
   																" ta.almacen_solicita as cod_almacen_solicitante," +
   																" psolicitante.tipo_consignac as tipo_consignacion_solicitante," +
   																" ta.almacen_solicitado as cod_almacen_solicitado," +
   																" ccsolicitado.nombre as almacen_solicitado," +
   																" psolicitado.tipo_consignac as tipo_consignacion_solicitado," +
   																" ta.observaciones as observaciones," +
   																" getnomcentroatencion(ccsolicitante.centro_atencion) as nombre_centro_atencion," +
   																" getnomcentrocosto(ta.almacen_solicitado) as almacen_solicitado" +
   															" from solicitud_traslado_almacen ta inner " +
   															" join centros_costo ccsolicitado on(ccsolicitado.codigo=ta.almacen_solicitado) " +
   															" inner join almacen_parametros psolicitado on (psolicitado.codigo=ccsolicitado.codigo) " +
   															" inner join centros_costo ccsolicitante on(ccsolicitante.codigo=ta.almacen_solicita) " +
   															" inner join almacen_parametros psolicitante on (psolicitante.codigo=ccsolicitante.codigo) " +
   															" where estado=? and almacen_solicitado=? order by ta.numero_traslado";
   
   /**
    * query para consultar el detalle de los articulos de la solicitud
    */
   private static final String consultaDetalleSolicitudStr="SELECT " +
   															"ddta.codigo as codigo," +
   															"ddta.cantidad as cantidaddepachada, " +												    		
												    		"dts.articulo as articulo," +
												    		"va.codigo_interfaz as codigointerfaz,"+
												    		"dts.cantidad as cantidadsolicitada," +
												    		"getdescarticulosincodigo(dts.articulo) AS descripcion, " +
												    		"getNomUnidadMedida(va.unidad_medida) AS unidadmedida," +
												    		"CASE WHEN ddta.cantidad IS NULL THEN 0 ELSE ddta.cantidad END as cantidaddespachada," +
												    		"CASE WHEN ddta.lote is null then '' else ddta.lote end as lote," +
												    		"CASE WHEN ddta.fecha_vencimiento is null then '' else to_char(ddta.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento " +
											    		 "from det_sol_traslado_almacen dts " +
											    		 "inner join articulo va on(dts.articulo=va.codigo) " +
											    		 "left outer join det_des_traslado_almacen ddta on(ddta.articulo=dts.articulo and ddta.numero_traslado=?) " +
											    		 "where dts.numero_traslado=? order by va.descripcion";
   /**
    * query para insertar la información del despacho
    */
   private static final String insertarDespachoStr="INSERT INTO despacho_traslado_almacen(numero_traslado,fecha_despacho,hora_despacho,usuario_despacho) VALUES (?,?,?,?)";
   /**
    * query para insertar el detalle del despacho, el despacho de traslado solo maneja proveedor de consignacion.
    */
   private static final String insertarDetalleDespachoStr="INSERT INTO det_des_traslado_almacen(codigo,numero_traslado,articulo,cantidad,valor_unitario,lote,fecha_vencimiento,proveedor) VALUES (?,?,?,?,?,?,?,?)";
   /**
    * query para actualizar el detalle del despacho para un articulo
    */
   private static final String actualizarDetalleDespachoStr="UPDATE det_des_traslado_almacen SET cantidad=?,lote=?, fecha_vencimiento=?,proveedor=? where codigo=?";
   /**
    * query para obtener la cantidad de un registro del detalle
    */
   private static final String existeRegistroDetalleDespachoStr="SELECT cantidad as cantidad,lote as lote,fecha_vencimiento as fechavencimiento from det_des_traslado_almacen where codigo=?";
   /**
    * query para verificar si existe registro en BD
    */
   private static final String existeRegistroDespachoStr="SELECT count(1) as valor from despacho_traslado_almacen where numero_traslado=?";
   /**
    * query para actualizar el usuario que genero el despacho
    */
   private static final String actualizarUsuarioDespachoStr="UPDATE despacho_traslado_almacen SET usuario_despacho=? where numero_traslado=?";
   
   
   /**
    * metodo para consultar el listado de 
    * traslados almacen para un almacen
    * dado y en estad cerrado
    * @param con Connection
    * @param estado int
    * @param almacenSolicitado int
    * @return HashMap
    */
   public static HashMap consultaSolicitudesTraslados(Connection con,int estado,int almacenSolicitado)
   {
       try
       {
           logger.info("Almacen>>>>>>>>>"+almacenSolicitado+"Estado  >>>>>>>"+estado);
           logger.info("Sql>>>>>>>>>"+consultaTrasladosAlmacenesStr);
    	   PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTrasladosAlmacenesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setInt(1,estado);
           ps.setInt(2,almacenSolicitado);
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery()); 
           HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
       }
       catch (SQLException e)
       {
           logger.error("Error en consultaDetalleSolicitud "+e);
           e.printStackTrace();
           return null;
       }      
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
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setInt(1,numeroSolicitud);
           ps.setInt(2,numeroSolicitud);
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery()); 
           HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
       }
       catch (SQLException e)
       {
           logger.error("Error en consultaDetalleSolicitud "+e);
           e.printStackTrace();
           return null;
       }      
   }
   /**
    * metodo para insertar la información del 
    * despacho
    * @param con Connection
    * @param vo HashMap
    * @return boolean
    */
   public static boolean insertarDespacho(Connection con,HashMap vo)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
           /**
            * INSERT INTO despacho_traslado_almacen(numero_traslado,fecha_despacho,hora_despacho,usuario_despacho) VALUES (?,?,?,?)
            */
           
           ps.setInt(1,Utilidades.convertirAEntero(vo.get("numeroSolicitud")+""));
           ps.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaDespacho")+"")));
           ps.setString(3,vo.get("horaDespacho")+"");
           if((vo.get("usuarioDespacho")+"").equals(""))
               ps.setNull(4,Types.VARCHAR);
           else
               ps.setString(4,vo.get("usuarioDespacho")+"");
           int resp=ps.executeUpdate();
           if(resp>0)
               return true;
           else
               return false;
                     
       }
       catch (SQLException e)
       {
           logger.error("Error en insertarDespacho "+e);
           e.printStackTrace();
           return false;
       }   
   }
   /**
    * metodo para insertar el detalle del 
    * despacho
    * @param con Connection
    * @param vo HashMap
    * @return boolean
    */
   public static boolean insertarDetalleDespacho(Connection con,HashMap vo)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDetalleDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
           logger.info("\n\n\n 	PASA POR (insertarDetalleDespacho)!!!!");
           
           
           /**
            * INSERT INTO det_des_traslado_almacen(numero_traslado,articulo,cantidad,valor_unitario,lote,fecha_vencimiento) VALUES (?,?,?,?,?,?)
            */
           ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_des_trasalm"));
           ps.setInt(2,Utilidades.convertirAEntero(vo.get("numeroSolicitud")+""));
           ps.setInt(3,Utilidades.convertirAEntero(vo.get("articulo")+""));
           ps.setInt(4,Utilidades.convertirAEntero(vo.get("cantidad")+""));
           ps.setDouble(5,Double.parseDouble(vo.get("valorUnitario")+""));
	       if((vo.get("lote")+"").trim().equals(""))
	       {
	    	   ps.setNull(6, Types.VARCHAR);
	       		ps.setNull(7, Types.DATE);
	       }
	       else
	       {
	        	ps.setString(6, (vo.get("lote")+""));
	        	if((vo.get("fechavencimiento")+"").trim().equals(""))
	        	{
	        		ps.setNull(7, Types.DATE);
	        	}
	        	else
	        	{
	        		ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD((vo.get("fechavencimiento")+""))));	
	        	}
	       }    
	       if(UtilidadTexto.isEmpty(vo.get("proveedorconsignacion")+""))
	    	   ps.setString(8, null);
	       else
	    	   ps.setString(8, vo.get("proveedorconsignacion")+"");
           

           
           int resp=ps.executeUpdate();
           if(resp>0)
               return true;
           else
               return false;
                     
       }
       catch (SQLException e)
       {
           logger.error("Error en insertarDetalleDespacho ",e);
           return false;
       }      
   }
   /**
    * metodo para actualizar el detalle de un
    * despacho
    * @param con Connection
    * @param vo Hashmap
    * @return boolean
    */
   public static boolean actualizarDetalleDespacho(Connection con,HashMap vo)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDetalleDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
           logger.info("actualizarDetalleDespachoStr    "+actualizarDetalleDespachoStr);
           
           /**
            * UPDATE det_des_traslado_almacen SET cantidad=?,lote=?, fecha_vencimiento=? where codigo=?
            */
           
           ps.setInt(1,Utilidades.convertirAEntero(vo.get("cantidad")+""));
           if((vo.get("lote")+"").trim().equals(""))
	       {
	    	   ps.setNull(2, Types.VARCHAR);
	       		ps.setNull(3, Types.DATE);
	       }
	       else
	       {
	        	ps.setString(2, (vo.get("lote")+""));
	        	if((vo.get("fechavencimiento")+"").trim().equals(""))
	        	{
	        		ps.setNull(3, Types.DATE);
	        	}
	        	else
	        	{
	        		ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD((vo.get("fechavencimiento")+""))));	
	        	}
	       }    
	       if(UtilidadTexto.isEmpty(vo.get("proveedorconsignacion")+""))
	    	   ps.setString(4, null);
	       else
	    	   ps.setString(4, vo.get("proveedorconsignacion")+"");

           ps.setInt(5,Utilidades.convertirAEntero(vo.get("codigo")+""));
           
           logger.info("codigo    "+vo.get("codigo"));
           
           int resp=ps.executeUpdate();
           if(resp>0)
               return true;
           else
               return false;
                     
       }
       catch (SQLException e)
       {
           logger.error("Error en actualizarDetalleDespacho ",e);
           return false;
       }      
   }
   /**
    * metodo para consultar la cantidad 
    * @param con Connection
    * @param vo HashMap
    * @return int
    */
   public static HashMap consultarCantidadRegistroDetalleDespacho(Connection con,HashMap vo)
   {
	   HashMap mapa=new HashMap();
	   mapa.put("numRegistros", "0");
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(existeRegistroDetalleDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
           ps.setInt(1,Utilidades.convertirAEntero(vo.get("codigo")+""));
           mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
       }
       catch (SQLException e)
       {
           logger.error("Error en consultarCantidadRegistroDetalleDespacho "+e);
           e.printStackTrace();           
       }      
       return mapa;
   }
   /**
    * metodo para verificar si un registro
    * se encuentra insertado en la BD
    * @param con Connection
    * @param numeroSolicitud int 
    * @return boolean 
    */
   public static boolean existeRegistroDespachoBD(Connection con,int numeroSolicitud)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(existeRegistroDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
           ps.setInt(1,numeroSolicitud);                     
           ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
           if(rs.next())
               if(rs.getInt("valor")>0)
                   return true;
       }
       catch (SQLException e)
       {
           logger.error("Error en existeRegistroDespachoBD "+e);
           e.printStackTrace();           
       }      
       return false;
   }  
   /**
    * metodo para insertar el usuario que realiza
    * el despacho
    * @param con Connection
    * @param usuario String
    * @param numeroSolicitud int
    * @return boolean
    */
   public static boolean actualizarUsuarioDespacho(Connection con,String usuario,int numeroSolicitud)
   {
       try
       {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarUsuarioDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
           /**
            * UPDATE despacho_traslado_almacen SET usuario_despacho=? where numero_traslado=?
            */
           
           ps.setString(1,usuario);
           ps.setInt(2,numeroSolicitud);               
           int resp=ps.executeUpdate();
           if(resp>0)
               return true;
           else
               return false;                     
       }
       catch (SQLException e)
       {
           logger.error("Error en actualizarUsuarioDespacho "+e);
           e.printStackTrace();
           return false;
       }      
   }
}
