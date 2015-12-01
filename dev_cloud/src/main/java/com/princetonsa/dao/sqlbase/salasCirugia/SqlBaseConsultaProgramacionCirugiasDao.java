package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;
import com.princetonsa.mundo.salasCirugia.ConsultaProgramacionCirugias;



public class SqlBaseConsultaProgramacionCirugiasDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseConsultaProgramacionCirugiasDao.class);
	
	/**
	 * Sentencia SQL para consultar el detalle de los servicios asociados a una petición de cirugía
	 */
	private static String consultarServiciosXPeticionStr = 	"SELECT " +
														"getnombreservicio(servicio, ?) AS servicio, " +
														"numero_servicio AS num_servicio, " +
														"observaciones AS observaciones, " +
														"getespecialidadservicio(servicio) AS especialidad " +
													"FROM " +
														"peticiones_servicio " +
													"WHERE " +
														"peticion_qx=? " +
													"ORDER BY " +
														"num_servicio ASC";
	
	/**
	 * Sentencia SQL para consultar el detalle de los servicios asociados a una petición de cirugía
	 */
	private static String consultarXPacienteStr = 	"SELECT " +
														"getnomestadopeticion(pq.estado_peticion) AS estado_peticion, " +
														"getnomcentroatencion(pq.centro_atencion) AS centro_atencion, " +
														"psq.peticion AS peticion, " +
														"to_char(pq.fecha_cirugia, 'DD-MM-YYYY') AS fecha_programacion, " +
														"to_char(psq.hora_inicio, 'HH24:MI') AS hora_inicio, " +
														"to_char(psq.hora_fin, 'HH24:MI') AS hora_final, " +
														"getDescripcionSala(psq.sala) AS sala, " +
														"getnombreservicio(ps.servicio, ?) AS servicio, " +
														"getnombrepersona(pq.solicitante) AS cirujano, " +
														"getnumserviciospeticionqx(pq.codigo) AS num_servicios, " +
														"coalesce(sc.numero_solicitud, "+ConstantesBD.codigoNuncaValido+") AS solicitud, " +
														"pq.fecha_peticion AS fecha_peticion," +
														"CASE WHEN pq.requiere_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS uci, " +
														"getdesctipoanestesia(pq.tipo_anestesia) AS tipo_anestesia, " +
														"to_char(psq.fecha_programacion, 'DD-MM-YYYY') AS fecha_grabacion, " +
														"psq.hora_programacion AS hora_grabacion, " +
														"psq.usuario AS usuario_grabacion  " +
													"FROM " +
														"peticion_qx pq " +
													"INNER JOIN " +
														"programacion_salas_qx psq ON (psq.peticion=pq.codigo) " +
													"INNER JOIN " +
														"peticiones_servicio ps ON (ps.peticion_qx=pq.codigo) " +
													"LEFT OUTER JOIN " +
														"solicitudes_cirugia sc on (sc.codigo_peticion=pq.codigo) " +
													"WHERE " +
														"pq.institucion=? " +
														"AND pq.programable=? " +
														"AND estado_peticion NOT IN (?, ?) " +
														"AND ps.numero_servicio=? " +
														"AND pq.paciente=? " +
													"ORDER BY " +
														"psq.fecha_cirugia DESC";
	
	/**
	 * Sentencia SQL para consultar los profesionales asociados a una peticion
	 */
	private static String consultarProfesionalesXPeticionStr = "SELECT " +
														"getnombrepersona(pppq.codigo_medico) AS profesional, " +
														"tpq.nombre AS tipo " +
													"FROM " +
														"prof_partici_peticion_qx pppq " +
													"INNER JOIN " +
														"tipos_participantes_qx tpq ON (tpq.codigo = pppq.tipo_participante) " +
													"WHERE " +
														"pppq.peticion_qx=? ";
	
	/**
	 * Sentencia SQL para consultar el ingreso de un paciente segun el codigo de una solicitud
	 */
	private static String consultarIngresoStr = 	"SELECT " +
														"getconsecutivoingreso(c.id_ingreso) AS ingreso," +
														"getfechaingreso(c.id, c.via_ingreso) AS fecha_ingreso, " +
														"coalesce(c.id, -1) AS cuenta " +
													"FROM " +
														"solicitudes s " +
													"INNER JOIN " +
														"cuentas c ON (c.id = s.cuenta) " +
													"WHERE " +
														"s.numero_solicitud=? ";
	
	/**
	 * Sentencia SQL para consultar los materiales especiales de una solicitud
	 */
	private static String consultarMaterialesEspecialesStr = 	"SELECT " +
														"getdescarticulo(articulo) AS articulo, " +
														"articulo AS cod_articulo " +
													"FROM " +
														"det_cargos " +
													"WHERE " +
														"solicitud=? " +
													"GROUP BY " +
														"articulo, cod_articulo ";
	
	/**
	 * Sentencia SQL para consultar los pedidos de una petición
	 */
	private static String consultarPedidosStr = 	"SELECT " +
														"ppq.pedido as pedido, " +
														"getnomcentrocosto(p.centro_costo_solicitado) as farmacia, " +
														"ep.descripcion " +
													"FROM " +
														"pedidos_peticiones_qx ppq " +
													"INNER JOIN " +
														"pedido p ON (ppq.pedido=p.codigo) " +
													"INNER JOIN " +
														"estados_pedido ep ON (p.estado = ep.codigo) " +
													"WHERE " +
														"ppq.peticion=?";
	
	/**
	 * Sentencia SQL que consulta el listado de articulos de un pedido
	 */
	private static String consultarArticulosPedidoStr = 	"SELECT " +
														"a.descripcion as articulo, " +
														"a.unidad_medida as umedida, " +
														"dp.cantidad " +
													"FROM " +
														"detalle_pedidos dp " +
													"INNER JOIN " +
														"articulo a ON (dp.articulo=a.codigo) " +
													"WHERE " +
														"dp.pedido=?";
	
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarPedidos(Connection con, ConsultaProgramacionCirugias mundo) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarPedidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, mundo.getPeticion());
			logger.info("Param 1 -- "+mundo.getPeticion());
			logger.info("Consulta --- "+consultarPedidosStr);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarArticulosPedido(Connection con, ConsultaProgramacionCirugias mundo) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosPedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, mundo.getPedido());
			logger.info("Param 1 -- "+mundo.getPedido());
			logger.info("Consulta --- "+consultarArticulosPedidoStr);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consulta las peticiones con indicativo de programable en S y estados de peticion diferentes a pendientes o anulada
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarXRango(Connection con, ConsultaProgramacionCirugias mundo) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		
		String consulta=" SELECT " +
							"getnomestadopeticion(pq.estado_peticion) AS estado_peticion, " +
							"getnomcentroatencion(pq.centro_atencion) AS centro_atencion, " +
							"psq.peticion AS peticion, " +
							"to_char(pq.fecha_cirugia, 'DD-MM-YYYY') AS fecha_programacion, " +
							"to_char(psq.hora_inicio, 'HH24:MI') AS hora_inicio, " +
							"to_char(psq.hora_fin, 'HH24:MI') AS hora_final, " +
							"getDescripcionSala(psq.sala) AS sala, " +
							"getnombreservicio(ps.servicio, "+ConstantesBD.codigoTarifarioCups+") AS servicio, " +
							"getnombrepersona(pq.solicitante) AS cirujano, " +
							"getnumserviciospeticionqx(pq.codigo) AS num_servicios, " +
							"coalesce(sc.numero_solicitud, -1) AS solicitud, " +
							"pq.fecha_peticion AS fecha_peticion, " +
							"pq.requiere_uci AS uci, " +
							"getdesctipoanestesia(pq.tipo_anestesia) AS tipo_anestesia, " +
							"to_char(psq.fecha_programacion, 'DD-MM-YYYY') AS fecha_grabacion, " +
							"psq.hora_programacion AS hora_grabacion, " +
							"psq.usuario AS usuario_grabacion, " +
							"pq.paciente AS cod_paciente, " +
							"getnombrepersona(pq.paciente) AS paciente, " +
							"getidpaciente(pq.paciente) AS id_paciente, " +
							"coalesce(getProfesionalPeticionQx(pq.codigo, "+ConstantesBD.codigoTipoParticipanteAnestesiologo+"), "+ConstantesBD.codigoNuncaValido+") AS cod_anestesiologo, " +
							"coalesce(getnombrepersona(coalesce(getProfesionalPeticionQx(pq.codigo, "+ConstantesBD.codigoTipoParticipanteAnestesiologo+"), "+ConstantesBD.codigoNuncaValido+")), '') AS anestesiologo " +
						"FROM " +
							"peticion_qx pq " +
						"INNER JOIN " +
							"programacion_salas_qx psq ON (psq.peticion=pq.codigo) " +
						"INNER JOIN " +
							"peticiones_servicio ps ON (ps.peticion_qx=pq.codigo) " +
						"LEFT OUTER JOIN " +
							"solicitudes_cirugia sc on (sc.codigo_peticion=pq.codigo) " +
						"WHERE " +
							"pq.institucion="+mundo.getInstitucion()+" "+
							"AND pq.programable='"+ConstantesBD.acronimoSi+"' " +
							"AND pq.estado_peticion NOT IN ("+ConstantesBD.codigoEstadoPeticionPendiente+", "+ConstantesBD.codigoEstadoPeticionAnulada+") " +
							"AND ps.numero_servicio=1 ";
		
		// Filtro por centro de atención
		if (!mundo.getFiltrosMap().get("centroAtencion").equals("")){
			consulta += " AND pq.centro_atencion="+mundo.getFiltrosMap().get("centroAtencion");
		}
		 
		// Filtro por sala
		if (!mundo.getFiltrosMap().get("sala").equals("")){
			consulta += " AND psq.sala="+mundo.getFiltrosMap().get("sala");
		}
		
		// Filtro por cirujano
		if (!mundo.getFiltrosMap().get("cirujano").equals("")){
			consulta += " AND pq.solicitante="+mundo.getFiltrosMap().get("cirujano");
		}
		
		// Filtro por anestesiologo
		if (!mundo.getFiltrosMap().get("anestesiologo").equals("")){
			consulta += " AND getProfesionalPeticionQx(pq.codigo, "+ConstantesBD.codigoTipoParticipanteAnestesiologo+")="+mundo.getFiltrosMap().get("anestesiologo");
		}
		
		// Filtro por tipo de anestesia
		if (!mundo.getFiltrosMap().get("tipoAnestesia").equals("")){
			consulta += " AND pq.tipo_anestesia="+mundo.getFiltrosMap().get("tipoAnestesia");
		}
		
		// Filtro Cancelacion
		
		// Filtro por el estado de la peticion
		if (!mundo.getFiltrosMap().get("estadoPeticion").equals("")){
			consulta += " AND pq.estado_peticion="+mundo.getFiltrosMap().get("estadoPeticion");
		}
		
		// Filtro por el usuario que programa
		if (!mundo.getFiltrosMap().get("usuario").equals("")){
			consulta += " AND psq.usuario='"+mundo.getFiltrosMap().get("usuario")+"' ";
		}
		
		// Filtro por la fecha
		if(!mundo.getFiltrosMap().get("horaInicial").equals("")&&!mundo.getFiltrosMap().get("horaFinal").equals(""))
			consulta += " AND (" +
					"psq.fecha_cirugia || '-' || psq.hora_inicio >= '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap().get("fechaInicial").toString())+"-"+mundo.getFiltrosMap().get("horaInicial")+"' AND " +
					"psq.fecha_cirugia || '-' || psq.hora_inicio <= '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap().get("fechaFinal").toString())+"-"+mundo.getFiltrosMap().get("horaFinal")+"' ) ";
		else
			consulta += " AND (psq.fecha_cirugia BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap().get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap().get("fechaFinal").toString())+"') ";
			
		// Ejecución de la sentencia
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		logger.info("CONSULTA ---> [ "+consulta+" ]");
		return mapa;
	}
	
	/**
	 * Consulta el ingreso de un paciente segun el numero de solicitud
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarMaterialesEspeciales(Connection con, ConsultaProgramacionCirugias mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarMaterialesEspecialesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, mundo.getSolicitud());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consulta el ingreso de un paciente segun el numero de solicitud
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarIngreso(Connection con, ConsultaProgramacionCirugias mundo){
		HashMap mapa = new HashMap();
		logger.info("------------------------- "+mundo.getSolicitud());
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, mundo.getSolicitud());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * Consulta los profesionales asociados a una peticion de cirugia
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarProfesionalesXPeticion(Connection con, ConsultaProgramacionCirugias mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarProfesionalesXPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, mundo.getPeticion());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consulta el detalle de los servicios asociados a una petición de cirugía 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarServiciosXPeticion(Connection con, ConsultaProgramacionCirugias mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarServiciosXPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ConstantesBD.codigoTarifarioCups);
			ps.setInt(2, mundo.getPeticion());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consulta las peticiones de un paciente con indicativo de programable en S y estados de peticion diferentes a pendientes o anulada
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarXPaciente(Connection con, ConsultaProgramacionCirugias mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarXPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ConstantesBD.codigoTarifarioCups);
			ps.setString(2, mundo.getInstitucion());
			ps.setString(3, ConstantesBD.acronimoSi);
			ps.setInt(4, ConstantesBD.codigoEstadoPeticionPendiente);
			ps.setInt(5, ConstantesBD.codigoEstadoPeticionAnulada);
			ps.setInt(6, 1);
			ps.setInt(7, mundo.getPaciente());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
}