package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseDevolucionInventariosPacienteDao {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger=Logger.getLogger(SqlBaseDevolucionInventariosPacienteDao.class);
	
	/**
	 * Cadena para consultar los ingresos/cuentas validos para la devolucion de inventarios por paciente.
	 */
	public static String cadenaConsultaIngresoCuentaPaciente="SELECT " +
																		" i.id as idingreso," +
																		" i.consecutivo as consecutivoingreso," +
																		" i.estado as estadoingreso," +
																		" to_char(i.fecha_ingreso,'yyyy-mm-dd') as fechaingreso," +
																		" i.hora_ingreso as horaingreso,"+
																		" to_char(i.fecha_egreso,'yyyy-mm-dd') as fechaegreso," +
																		" i.hora_egreso as horaegreso,"+
																		" c.id as cuenta," +
																		" c.estado_cuenta as codestadocuenta," +
																		" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
																		" c.via_ingreso as codviaingreso, " +
																		" getnombreviaingreso(c.via_ingreso) as nomviaingreso " +
																		" from cuentas c " +
																		" inner join ingresos i on(c.id_ingreso=i.id) " +
																		" inner join centros_costo cc on(cc.codigo=c.area) " +
																		" where " +
																		" c.codigo_paciente= ? and " +
																		" cc.centro_atencion=?  and " +
																		" i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and " +
																		" (c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") or "+
																		" (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" and getEsCuentaAsocioincompleto(c.id)='"+ConstantesBD.acronimoSi+"') ) "+
																		" ORDER BY c.fecha_apertura";
	
	/**
	 * Cadena para la consulta de los pedidos a devolucion
	 */
	public static String cadenaConsultaPedidos="SELECT p.codigo as codigo,to_char(p.fecha,'yyyy-mm-dd') as fecha,p.centro_costo_solicitado as codfarmacia,"+
												"p.centro_costo_solicitante as codcentrodevuelve,"+
												"getnomcentrocosto(centro_costo_solicitado) as nombrefarmacia,"+
												"getnomcentrocosto(centro_costo_solicitante) as nombrecentrodevuelve "+
												"FROM peticion_qx pq "+
												"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=pq.codigo) "+
												"INNER JOIN pedido p ON (p.codigo=ppq.pedido) "+
												"WHERE pq.paciente=? AND p.estado="+ConstantesBD.codigoEstadoPedidoDespachado+" "+
												"ORDER BY p.codigo";
	
	/**
	 * Cadena para la consulta de las solicitudes a devolucion
	 */
	public static String cadenaConsultaSolicitudes="SELECT distinct s.numero_solicitud as solicitud,to_char(fecha_solicitud,'yyyy-mm-dd') as fecha, s.centro_costo_solicitado as codfarmacia,"+
													"getnomcentrocosto(centro_costo_solicitado) as nombrefarmacia,d.orden as despacho, "+
													"s.centro_costo_solicitante as codcentrodevuelve, "+
													"getnomcentrocosto(centro_costo_solicitante) as nombrecentrodevuelve "+
													"FROM solicitudes s "+
													"INNER JOIN cuentas c ON (s.cuenta=c.id) "+
													"INNER JOIN despacho d ON (s.numero_solicitud=d.numero_solicitud) "+
													"INNER JOIN detalle_despachos dd on (d.orden=dd.despacho) "+
													"LEFT OUTER JOIN admin_medicamentos am ON (am.numero_solicitud=s.numero_solicitud) "+
													"INNER JOIN det_cargos dc ON(dc.solicitud=s.numero_solicitud) "+
													"WHERE c.codigo_paciente=? "+
													" AND s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+
													" AND dc.eliminado='"+ConstantesBD.acronimoNo+"' "+
													" AND d.es_directo="+ValoresPorDefecto.getValorFalseParaConsultas()+
													" AND dc.facturado='"+ConstantesBD.acronimoNo+"' AND getdespacho(dc.articulo,d.numero_solicitud)>0 AND gettotaladmin(dc.articulo,d.numero_solicitud) < 1 and (dd.cantidad-getcantidaddevolucion(dd.articulo,d.numero_solicitud)) > 0";

	
	/**
	 * Cadena para la consulta del detalle de las solicitudes despachadas
	 */
	public static String cadenaConsultaDetalleSolicitudes="SELECT articulo,(cantidad - getcantidaddevolucion(dd.articulo,d.numero_solicitud)) as cantidad,lote,to_char(fecha_vencimiento,'yyyy-mm-dd') as fechavencimiento, 'false' as devolver, '' as cantidaddevolver "+
															"FROM detalle_despachos dd inner join despacho d on(d.orden=dd.despacho) "+
															"WHERE dd.despacho=? and (cantidad-getcantidaddevolucion(dd.articulo,d.numero_solicitud)) > 0";
	
	/**
	 * Cadena que consulta la fecha y hora despacho de solicitudes
	 */
	private static final String consultarFechaHoraDespachoSolicitudes= "SELECT to_char(fecha,'yyyy-mm-dd') as fecha, hora as hora FROM despacho WHERE orden= ?";
	
	/**
	 * Cadena para la consulta de la devolucion de solicitudes
	 */
	private static final String consultarDevolucionSolicitudes="SELECT observaciones,motivo,estado,centro_costo_devuelve,farmacia "+
																"FROM devolucion_med WHERE codigo= ?";
	
	/**
	 * Cadena para la consulta del detalle de la devolucion de solicitud
	 */
	private static final String consultarDetalleDevolucionSolicitudes="SELECT articulo,lote,to_char(fecha_vencimiento,'yyyy-mm-dd'),cantidad "+
																		"FROM detalle_devol_med WHERE devolucion=?";
	
	/**
	 * Consulta de ingresos/cuenta del paciente cargado
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona,int codigoCentroAtencion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaIngresoCuentaPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			ps.setInt(2, codigoCentroAtencion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE INGRESOS VALIDOS PARA LA DEVOLUCION "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consulta de los pedidos del paciente cargado
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarPedidos(Connection con, int codigoPersona)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaPedidos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			logger.info("consulta de los pedidos del paciente=> "+cadenaConsultaPedidos+", paciente=> "+codigoPersona);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE LOS PEDIDOS POR PACIENTE "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	
	/**
	 * Consulta de las solicitudes del paciente cargado
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarSolicitudes(Connection con, int codigoPersona)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			logger.info("\n\nCONSULTA DE SOLICITUDES DE MEDICAMENTOS-->>"+cadenaConsultaSolicitudes+" CODIGO PERSONA-->>"+codigoPersona);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE LAS SOLICITUDES POR PACIENTE "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consulta del detalle de las solicitudes del paciente cargado
	 * @param con
	 * @param despacho
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleSolicitudes(Connection con,int despacho)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, despacho);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LAS SOLICITUDES POR PACIENTE "+e);
			e.printStackTrace();
		}
		return mapa;	
	}
	
	/**
	 * Método que consulta la fecha y hora de despacho de una solicitud
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public static HashMap<String, Object> consultarFechaHoraDespachoSolicitudes(Connection con,int solicitud)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaHoraDespachoSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,solicitud);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFechaHoraDespacho :"+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Insertar la devolucion de solicitudes
	 * @param con
	 * @param vo
	 * @param cadena 
	 * @return
	 */
	public static int insertarDevolucionSolicitudes(Connection con, HashMap vo, String cadenaDevolucion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDevolucion));
			
			/**
			 * INSERT INTO devolucion_med (codigo,observaciones,fecha,hora,fecha_grabacion,"+
																"hora_grabacion,
																usuario,
																estado,
																centro_costo_devuelve,
																farmacia,
																tipo_devolucion,"+
																"motivo,
																institucion,
																tipo_comprobante,
																nro_comprobante,
																contabilizado) "+
																"VALUES ('seq_devolucion_med'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("observaciones")+"");
			ps.setDate(2, Date.valueOf(vo.get("fecha")+""));
			ps.setString(3, vo.get("hora")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, vo.get("usuario")+"");
			ps.setInt(7, ConstantesBD.codigoEstadoDevolucionGenerada);
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("codcentrodevuelve")+""));
			ps.setInt(9, Utilidades.convertirAEntero(vo.get("codfarmacia")+""));
			ps.setInt(10, ConstantesBD.codigoTipoDevolucionManual);
			ps.setString(11, vo.get("motivo")+"");
			ps.setInt(12, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setNull(13, Types.CHAR);
			ps.setDouble(14, ConstantesBD.codigoNuncaValidoDouble);
			ps.setString(15, ConstantesBD.acronimoNo);
			if(ps.executeUpdate()>0)
			{
				String cadena="select max(codigo) from devolucion_med";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL INSERTAR LA DEVOLICION DE SOLICITUDES ",e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * insertar el detalle de la devolucion de solicitudes
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarDetalleDevolucionSolicitudes(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			
			/**
			 * INSERT INTO detalle_devol_med (codigo,devolucion,"+
																	"numero_solicitud,articulo,cantidad,lote,fecha_vencimiento) "+
																	"VALUES ('seq_detalle_devol_med'),?,?,?,?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("devolucion")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("numerosolicitud")+""));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo")+""));
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setInt(4, 0);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("cantidad")+""));
			ps.setString(5, vo.get("lote")+"");
			if(UtilidadTexto.isEmpty(vo.get("fechavencimiento")+""))
			{
				ps.setNull(6, Types.DATE);
			}
			else
				ps.setDate(6, Date.valueOf(vo.get("fechavencimiento")+""));
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL INSERTAR EL DETALLE DE LA DEVOLUCION DE SOLICITUDES "+e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consultar devolucion de solicitudes
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap<String, Object> consultaDevolucionSolicitudes(Connection con,int codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarDevolucionSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("CONSULTA DE DEVOLUCION DE SOLICITUDES-->> "+consultarDevolucionSolicitudes+" "+codigo);
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE LA DEVOLUCION "+e);
			e.printStackTrace();
		}
		return mapa;	
	}
	
	/**
	 * Consultar detalle de la devolucion de solicitudes
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleDevolucionSolicitudes(Connection con,int codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucionSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("CONSULTA DE DETALLE DEVOLUCION-->> "+consultarDetalleDevolucionSolicitudes);
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LA DEVOLUCION "+e);
			e.printStackTrace();
		}
		return mapa;	
	}
	
}
