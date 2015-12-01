/*
 * Created on 08-oct-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
/**
 * @author armando
 *
 * Princeton 08-oct-2004
 * Clase para manejar todo el SqlBase de la recepcion 
 * devolucion pedidos.
 */
public class SqlBaseRecepcionDevolucionMedicamentos 
{
	    /**
	     * Variable para manejar los errores loger de la funcionalidad
	     */
	    private static Logger logger = Logger.getLogger(SqlBaseRecepcionDevolucionMedicamentos.class);
	    /**
	     * Variable para realizar la consulta de todas las devoluciones que se tienen
	     */
	     private static String consutalDevoluciones= "SELECT " +
	     												" devm.codigo as numeroDevolucion," +
	     												" c.codigo_paciente as codigopaciente, " +
	     												" per.tipo_identificacion||' '||per.numero_identificacion as idpaciente," +
	     												" getnombrepersona(per.codigo) as nompaciente," +
	     												" s.consecutivo_ordenes_medicas as orden," +
	     												" c.id as cuenta, " +
	     												" devm.estado as codigoEstadoDevolucion, " +
	     												" getEstadoDevolucion(devm.estado) as estadoDevolucion, " +
	     												" devm.fecha||' '||devm.hora as fechaHoraDevolucion,	" +
	     												" devm.usuario as usuario, " +
	     												" devm.tipo_devolucion as tipodevolucion, " +
	     												" case when devm.tipo_devolucion=1 then 'Automatica' else 'Manual' end as nomtipodevolucion, " +
	     												" devm.centro_costo_devuelve as codigocentro," +
	     												" getnomcentrocosto(devm.centro_costo_devuelve) as nombrecentrocosto, " +
	     												" getnomcentroatencion(cc.centro_atencion) as centroatencion, " +
	     												" devm.farmacia as farmacia," +
	     												//5703 se crea función en Oracle y Postgres
	     												" ORDENES.getultimacamahcc(c.id)   as cama, " +
	     												//5703 se elimina: coalesce('P. '||hcc.codigopiso||' H. '||hcc.codigohabitacion||' C. '||hcc.numerocama,'') as cama, " +
	     												" getnomcentrocosto(devm.farmacia) as nombrefarmacia, " +
	     												" getMotivoDevolInventarios(devm.motivo, devm.institucion) as motivo," +
	     												//Mt6088 se eliminan referencia de lote y fecha de vencimiento ya que em esta consulta no es necesario y nos presenta muchos registros
	     												//de una devolucion
	     												//" case when ddm.lote is null then '' else ddm.lote end as lote," +
	     											    //" case when ddm.fecha_vencimiento is null then '' else to_char(ddm.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento, " +
	     												" devm.observaciones as observaciones "+
	     											" from devolucion_med devm	" +
	     											" inner join detalle_devol_med ddm on (ddm.devolucion=devm.codigo) " +
	     											" inner join solicitudes s on s.numero_solicitud=ddm.numero_solicitud " +
	     											" inner join cuentas c on c.id=s.cuenta " +
	     											" inner join personas per on(c.codigo_paciente=per.codigo)  " +
	     											" inner join centros_costo cc on(cc.codigo=devm.centro_costo_devuelve) " +
	     											" left outer join his_camas_cuentas hcc on(hcc.cuenta=s.cuenta) "+
	     											//5703 se elimina: " inner join admisiones_hospi ah on(ah.cama=hcc.codigocama) " +
	     											" where " +
	     												" devm.estado="+ConstantesBD.codigoEstadoDevolucionGenerada+" and " +
	     												" devm.farmacia=? ";
	     /**
	      * 
	      */
	     public static String groupConsultaDevol=""+
	     											" group by " +
	     												" devm.codigo," +
	     												" devm.estado," +
	     												" devm.fecha," +
	     												" devm.hora," +
	     												//" fechaHoraDevolucion," +
	     												" devm.usuario," +
	     												" devm.tipo_devolucion," +
	     												" devm.centro_costo_devuelve," +
	     												" devm.observaciones, "+
	     												" cc.centro_atencion," +
	     												" devm.farmacia," +
	     												" motivo," +
	     												" c.codigo_paciente," +
	     												" c.id," +
	     												" per.tipo_identificacion," +
	     												" per.numero_identificacion," +
	     												" per.codigo," +
	     												" s.consecutivo_ordenes_medicas, " +
	     												" devm.institucion," +
	     												" ddm.lote," +
	     												//5703 se elimina: " ddm.fecha_vencimiento ,hcc.codigopiso,hcc.codigohabitacion,hcc.numerocama"+
	     												" ddm.fecha_vencimiento" +
	     											" order by devm.codigo";

	    /**
	     * Varible para realizar la consulta del detalle de una devolucion.
	     */
	    private static String consultarDetalleDevolucion="SELECT " +
	    															" ddm.codigo as codigo, " +
	    															" ddm.numero_solicitud as numerosolicitud," +
	    															" s.consecutivo_ordenes_medicas as orden, " +
	    															" ddm.devolucion as numerodevolucion, " +
	    															" ddm.articulo as codigoArticulo, " +
	    															" getDescArticulo(ddm.articulo) as articulo, " +
	    															" u.nombre as unidadmedida, " +
	    															" ddm.cantidad as cantidad, " +
	    		     												" case when ddm.lote is null then '' else ddm.lote end as lote," +
	    		     												" case when ddm.fecha_vencimiento is null then '' else to_char(ddm.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento " +
	    												" from detalle_devol_med ddm	" +
	    												" inner join solicitudes s on s.numero_solicitud=ddm.numero_solicitud " +
	    												" inner join articulo a on articulo=a.codigo	" +
	    												" inner join unidad_medida u on (a.unidad_medida=u.acronimo)	" +
	    												" where devolucion=? and cantidad>0";
	    //Antes de cambiar las tablas 
	    //private static String consultarDetalleDevolucion="SELECT ddm.codigo as codigo, ddm.tipo_devolucion as tipodevolucion, ddm.numero_solicitud as numerosolicitud, ddm.devolucion as numerodevolucion, ddm.articulo as codigoArticulo, getfulldescriptionarticulo(ddm.articulo) as articulo, u.nombre as unidadmedida, ddm.cantidad as cantidad from detalle_devol_med ddm	inner join articulo a on articulo=a.codigo	inner join unidad_medida u on (a.unidad_medida=u.acronimo)	where devolucion=? and cantidad>0"; 
	    /**
	     *Variable para manejar la insercion del maestro de una recepcion de devolucion 
	     */
	    private static String insertarRecepcionDevolucion="insert into recepciones_medicamentos(devolucion,fecha,hora,usuario) values (?,?,?,?)";
	    /**
	     *Variable para manejar la insercion del maestro de una recepcion de devolucion 
	     */
	    private static String insertarRecepcionDevolucionDetalle="insert into detalle_recep_medicamentos (codigo, numerodevolucion, cantidadrecibida, costo_unitario,lote,fecha_vencimiento,articulo,tipo_recepcion,almacen_consignacion,proveedor_compra,proveedor_catalogo )values (?,?,?, (SELECT a.costo_promedio FROM articulo a WHERE a.codigo=(SELECT ddm.articulo FROM detalle_devol_med ddm WHERE ddm.codigo=?)),?,?,?,?,?,?,?)";
	    /**
	     * Variable para actualizar una devolucion y pasarla de generada a recibida
	     */
	    private static String actualizarDevolucion="update devolucion_med set estado=? where codigo=?";
	    
	    /**
	     * Cadena para realizar la consulta del resumen de recepcion de devolucion de medicamentos.
	     */
	    private static String consultarMaestroRecepcion="select	" +
																	" rm.devolucion as numerodevolucion," +
																	" rm.fecha||' '||rm.hora as fechahorarecepcion," +
																	" devm.farmacia as codigofarmacia," +
																	" getnomcentrocosto(devm.farmacia) as nombrefarmacia," +
																	" rm.usuario as usuariorecibe," +
																	" s.cuenta as cuenta," +
																	" p.primer_apellido||' '||segundo_apellido as apellidos," +
																	" p.primer_nombre||' '||segundo_nombre as nombres," +
																	" devm.estado as codigoEstadoDevolucion," +
																	" getEstadoDevolucion(devm.estado) as estadoDevolucion," +
																	" devm.usuario as usuariodevuelve," +
																	" devm.fecha||' '||devm.hora as fechaHoraDevolucion," +
																	" devm.centro_costo_devuelve as codigocentro," +
																	" getMotivoDevolInventarios(devm.motivo, devm.institucion) as motivo," +
																	" getnomcentrocosto(devm.centro_costo_devuelve) as nombrecentrocosto," +
	    		     												" case when drm.lote is null then '' else drm.lote end as lote," +
	    		     												" case when drm.fecha_vencimiento is null then '' else to_char(drm.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento " +
														" from recepciones_medicamentos rm " +
														" inner join detalle_recep_medicamentos drm on (drm.numerodevolucion=rm.devolucion) " +
														" inner join detalle_devol_med ddm on (drm.codigo=ddm.codigo) " +
														" inner join solicitudes s on s.numero_solicitud=ddm.numero_solicitud " +
														" inner join cuentas c on c.id=s.cuenta " +
														" inner join personas  p on p.codigo=c.codigo_paciente " +
														" inner join devolucion_med devm on devm.codigo=rm.devolucion " +
														" where rm.devolucion=?" +
														" group by" +
															" rm.devolucion," +
															" rm.fecha," +
															" rm.hora," +
															" devm.farmacia," +
															" rm.usuario," +
															" s.cuenta," +
															" primer_apellido," +
															" segundo_apellido," +
															" primer_nombre," +
															" segundo_nombre," +
															" devm.estado," +
															" devm.fecha," +
															" devm.hora," +
															" devm.usuario," +
															" devm.centro_costo_devuelve," +
															" motivo, " +
															" drm.lote," +
		     												" drm.fecha_vencimiento, " +
		     												" devm.institucion ";
	    /**
	     * Cadena para realizar la consulta de detalle
	     */
	    private static String consultarDetalleRecepcion="SELECT" +
											    			" drm.codigo as codigo," +
											    			" drm.numerodevolucion as numerodevolucion," +
											    			" ddm.numero_solicitud as numerosolicitud," +
											    			" s.consecutivo_ordenes_medicas as orden," +
											    			" drm.articulo as codigoarticulo," +
											    			" getDescArticulo(drm.articulo) as articulo," +
											    			" u.nombre as unidadmedida," +
											    			" ddm.cantidad as cantidaddevuelta," +
											    			" drm.cantidadrecibida as cantidadrecibida," +
															" case when drm.lote is null then '' else drm.lote end as lote," +
															" case when drm.fecha_vencimiento is null then '' else to_char(drm.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento " +
											    		" from detalle_recep_medicamentos drm" +
											    		" inner join detalle_devol_med ddm on (drm.codigo=ddm.codigo) " +
											    		" inner join solicitudes s on (s.numero_solicitud=ddm.numero_solicitud) "  +
											    		" inner join articulo a on (ddm.articulo=a.codigo )" +
											    		" inner join unidad_medida u on (a.unidad_medida=u.acronimo)" +
											    		" where drm.numerodevolucion=?";
	    /**
	     * para Manejo de transacciones y acceso a la fuente de datos
	     */
	    private static String tipoBD = System.getProperty("TIPOBD");
		
	    private static DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	    /**
	     * Metodo que realiza la consulta de todas las devoluciones en estado
	     * Generada.
	     * @param codigoPaciente 
	     * @param vo 
	     * @param con, Conexxion
	     * @return, colecciion, coleccion con los datos de la consulta.
	     */
	    public static Collection consultarDevoluciones(Connection con,int centroCostoSolicitado, int codigoPaciente, HashMap<String, Object> vo)
	    {
	    	 Collection coleccion=null;
	    	 PreparedStatementDecorator ps;
	    	 try
	         {
	    		String cadena=consutalDevoluciones;
	    		if(codigoPaciente>0)
	    		{
	    			cadena=cadena+" and c.codigo_paciente="+codigoPaciente;
	    		}
	    		else if(vo!=null)//filtro
	    		{
	    			if(!(vo.get("areaFiltro")+"").equals(""))
	    			{
	    				cadena+=" AND c.area = '"+(vo.get("areaFiltro")+"")+"'";
	    			}
	    			if(!(vo.get("pisoFiltro")+"").equals(""))
	    			{
	    				cadena+=" AND hcc.codigopkpiso = '"+(vo.get("pisoFiltro")+"")+"'";
	    			}
	    			if(!(vo.get("habitacionFiltro")+"").equals(""))
	    			{
	    				cadena+=" AND hcc.codigopkhabitacion = '"+(vo.get("habitacionFiltro")+"")+"'";
	    			}
	    			if(!(vo.get("camaFiltro")+"").equals(""))
	    			{
	    				cadena+=" AND hcc.codigocama = '"+(vo.get("camaFiltro")+"")+"'";
	    			}
	    		}
	    		ps= new PreparedStatementDecorator(con.prepareStatement(cadena+groupConsultaDevol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    		ps.setInt(1,centroCostoSolicitado);
	    		//logger.info("\n\nConsulta: "+cadena+groupConsultaDevol);
	    		//logger.info("\n\ncentro de Costo: "+centroCostoSolicitado);
	         	coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
	         }
	         catch(SQLException e)
	         {
	             logger.error("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DEVOLUCION",e);
	         }
	         return coleccion;
	    }
		
	    /**
	     * Metodo para realizar la consulta de una devolucion
	     * @param con, Conexion
	     * @param devolucion, Devolucion que se desa consultar
	     * @return collection,Coleccion con los datos
	     */
	    public static Collection consultarUnaDevolucion(Connection con,int devolucion)
	    {
	    	Collection coleccion=null;
	        PreparedStatementDecorator ps;
	        try
	        {
	            ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ps.setInt(1,devolucion);
	            coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
	        }
	        catch(SQLException e)
	        {
	            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DETALLE DE DEVOLUCION"+e.toString());
	        }
	        return coleccion;
	    }
	    /**
	     * Metodo para realizar la consulta de una recepcion de una devolucion
	     * @param con, Conexion
	     * @param devolucion, Devolucion que se desa consultar
	     * @return Resultset,Resultado de la consulta
	     */
	    
	    public static ResultSetDecorator consultarMaestroRecepcion(Connection con,int devolucion)
	    {
	    	ResultSetDecorator rs=null;
	        PreparedStatementDecorator ps;
	        try
	        {
	        	ps= new PreparedStatementDecorator(con.prepareStatement(consultarMaestroRecepcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ps.setInt(1,devolucion);
	            rs=new ResultSetDecorator(ps.executeQuery());
	        }
	        catch(SQLException e)
	        {
	            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DEVOLUCION"+e.toString());
	        }
	        return rs;
	    }
	    
	    /**
	     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion
	     * @param con
	     * @param devolucion
	     * @return Collection. Informacion obtenidad de la B.D.
	     */
	    public static Collection consultarDetalleRecepcion(Connection con,int devolucion)
	    {
	    	Collection coleccion=null;
	        PreparedStatementDecorator ps;
	        try
	        {
	            ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleRecepcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ps.setInt(1,devolucion);
	            coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
	        }
	        catch(SQLException e)
	        {
	            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE DETALLE RECEPCION DE LA DEVOLUCION"+e.toString());
	        }
	        return coleccion;
	    }
	    
	    /**
	     * Metodo que realiza la insercion de un registro
	     * @param con
	     * @param devolucion
	     * @param tipo
	     * @param farmacia
	     * @param fecha
	     * @param hora
	     * @param usuario
	     * 
	     */
	    public static int insertarRecepcionMaestro(Connection con,int devolucion,String fecha,String hora,String usuario)
	    {
	    	PreparedStatementDecorator ps;
	    	try 
		        {  
		        ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRecepcionDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        ps.setInt(1,devolucion);
		        ps.setString(2,fecha);
		        ps.setString(3,hora);
		        ps.setString(4,usuario);
		        return ps.executeUpdate();
		    } catch (SQLException e) 
		        {
		    		try {
						myFactory.abortTransaction(con);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					logger.warn("No se pudo realizar la insercion, la transacion se ha abortado "+e.toString());
					return Integer.parseInt(e.getSQLState())*(-1);
		        }
	    }
	    /**
	     * Metodo que realiza la insercion de un registro
	     * @param con
	     * @param fechaVencimiento 
	     * @param lote 
	     * @param articulo 
	     * @param codigo, del detalle de la devolucion de medicamentos
	     * @param tipo, del detalle de la devolucion de medicamentos
	     * @param devolucion,
	     * @param articulo
	     * @param proveedorCatalogo 
	     * @param proveedorCompra 
	     * @param almacenConsignacion 
	     * @param tipoRecepcion 
	     * @param numeroSolicitud.
	     * @param cantidaddevuelta
	     * @param cantidadrecibida
	     * 
	     */
	    public static int insertarRecepcionDetalle(Connection con,int codigo, int devolucion,int cantidadRecibida, String lote, String fechaVencimiento, int articulo, String tipoRecepcion, String almacenConsignacion, String proveedorCompra, String proveedorCatalogo)
		{
	    	PreparedStatementDecorator ps;
	    	try 
	        { 
	    		ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRecepcionDevolucionDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    	ps.setInt(1,codigo);
		    	ps.setInt(2,devolucion);
		    	ps.setInt(3,cantidadRecibida);
                ps.setInt(4,codigo);
    	        if(lote.trim().equals(""))
    	        {
    	        	ps.setObject(5, null);
    	        	ps.setObject(6, null);
    	        }
    	        else
    	        {
    	        	ps.setString(5, lote);
    	        	if(fechaVencimiento.trim().equals(""))
    	        	{
    	        		ps.setObject(6, null);
    	        	}
    	        	else
    	        	{
    	        		ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));	
    	        	}
    	        }   
    	        ps.setInt(7,articulo);
    	        
    	        if(!UtilidadTexto.isEmpty(tipoRecepcion))
    	        	ps.setString(8, tipoRecepcion);
    	        else 
    	        	ps.setObject(8, null);
    	        
    	        if(!UtilidadTexto.isEmpty(almacenConsignacion))
    	        	ps.setString(9, almacenConsignacion);
    	        else 
    	        	ps.setObject(9, null);
    	        
    	        if(!UtilidadTexto.isEmpty(proveedorCompra))
    	        	ps.setString(10, proveedorCompra);
    	        else 
    	        	ps.setObject(10, null);
    	        if(!UtilidadTexto.isEmpty(proveedorCatalogo))
    	        	ps.setString(11, proveedorCatalogo);
    	        else 
    	        	ps.setObject(11, null);
		    	return ps.executeUpdate();
			} catch (SQLException e) 
		    {
				try {
					myFactory.abortTransaction(con);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		        logger.warn("No se pudo realizar la insercion, la transacion se ha abortado "+e.toString());
		    }
			return -1;
			}
		/**
		 * Metodo que actualiza el estado de una devolucion.
		 * @param con, conexion
		 * @return
		 */
		public static int actualizarEstadoDevolucion(Connection con,int devolucion,int estadoDevolucion) 
		{
			PreparedStatementDecorator ps;
		try 
	    { 
	    	ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1,estadoDevolucion);
	    	ps.setInt(2,devolucion);
	    	return ps.executeUpdate();
		} catch (SQLException e) 
	    {
			try {
				myFactory.abortTransaction(con);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        logger.warn("No se pudo realizar la insercion, la transacion se ha abortado "+e.toString());
	    }
		return -1;
		}
		//@todo aqui se puede realizar la actualizacion de cantidades recibidas en los saldos de la frarmaci y grabar el costo de lo recibdo. debe generarse el metodo.
}