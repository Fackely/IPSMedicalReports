package com.princetonsa.dao.sqlbase.ordenesmedicas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.ordenesmedicas.DtoIncapacidad;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;

/**
 * @author Jairo Gï¿½mez Fecha Octubre de 2009
 */

public class SqlBaseRegistroIncapacidadDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRegistroIncapacidadDao.class);
	
	private final static String sqlConsultarIncapacidadPorIngreso = 
							"SELECT CODIGO_PK         AS codigo_pk                ," +
							"CODIGO_PACIENTE          AS codigo_paciente          ," +
							"INGRESO                  AS ingreso                  ," +
							"to_char(FECHA_INICIO_INCAPACIDAD,'yyyy-mm-dd') AS fecha_inicio_incapacidad ," +
							"to_char(FECHA_FIN_INCAPACIDAD,'yyyy-mm-dd')    AS fecha_fin_incapacidad    ," +
							"DIAS_INCAPACIDAD         AS dias_incapacidad         ," +
							"TIPO_INCAPACIDAD         AS tipo_incapacidad         ," +
							"PRORROGA                 AS prorroga                 ," +
							"CODIGO_MEDICO            AS codigo_medico            ," +
							"ESPECIALIDAD             AS especialidad             ," +
							"ESTADO                   AS estado                   ," +
							"MOTIVO_ANULACION         AS motivo_anulacion         ," +
							"CONSECUTIVO              AS consecutivo              ," +
							"ANIO_CONSECUTIVO         AS anio_consecutivo         ," +
							"OBSERVACIONES            AS observaciones            ," +
							"VALORACION               AS valoracion               ," +
							"EVOLUCION                AS evolucion                ," +
							"ACRONIMO_DIAGNOSTICO     AS acronimo_diagnostico     ," +
							"TIPO_CIE                 AS tipo_cie                 ," +
							"TO_CHAR(FECHA_GRABACION,'yyyy-mm-dd')AS fecha_grabacion," +
							"HORA_GRABACION           AS hora_grabacion           ," +
							"manejopaciente.getdiagnostico(ACRONIMO_DIAGNOSTICO,TIPO_CIE) AS diagnostico," +
							"ACTIVO                   AS activo " +
							"FROM ordenes.registro_incapacidades " +
							"WHERE estado = ? " +
							"AND ingreso = ?";
	
	private final static String sqlConsultarTiposIncapacidad = "SELECT codigo AS codigo," +
										"descripcion  AS descripcion," +
										"valor_maximo AS valor_maximo " +
										"FROM ordenes.tipos_incapacidad";
	
	private final static String sqlInsertarIncapacidad = "insert into ordenes.registro_incapacidades" +
			"(CODIGO_PK,CODIGO_PACIENTE,INGRESO,FECHA_INICIO_INCAPACIDAD,FECHA_FIN_INCAPACIDAD,DIAS_INCAPACIDAD,TIPO_INCAPACIDAD,PRORROGA,FECHA_GRABACION," +
			"HORA_GRABACION,CODIGO_MEDICO,ESPECIALIDAD,FECHA_MODIFICA,HORA_MODIFICA,USUARIO_MODIFICA,ESTADO,MOTIVO_ANULACION,CONSECUTIVO,ANIO_CONSECUTIVO," +
			"OBSERVACIONES,ACRONIMO_DIAGNOSTICO,TIPO_CIE,ACTIVO)" +
			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private final static String sqlInsertarLogIncapacidad = "INSERT INTO ORDENES.log_reg_incapacidades" +
										"(codigo_pk, registro_incapacidad, fecha_inicio_incapacidad, fecha_fin_incapacidad," +
										"dias_incapacidad, tipo_incapacidad, prorroga, observaciones, codigo_medico," +
										"fecha_modifica, hora_modifica, usuario_modifica)" +
										"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private final static String sqlActualizarIncapacidad = "UPDATE ordenes.registro_incapacidades " +
						  "SET fecha_inicio_incapacidad = ?," +
							"fecha_fin_incapacidad      = ?," +
							"dias_incapacidad           = ?," +
							"tipo_incapacidad           = ?," +
							"prorroga                   = ?," +
							"Observaciones              = ?," +
							"fecha_modifica             = ?," +
							"hora_modifica              = ?," +
							"usuario_modifica           = ?," +
							"consecutivo                = ?," +
							"anio_consecutivo           = ?," +
							"activo                     = ? " +
							"WHERE codigo_pk            = ?";
	
	private final static String sqlActualizarEstadoIncapacidad = "UPDATE ordenes.registro_incapacidades " +
															"SET estado        = ? ";
	
	private final static String sqlEgresoPosteriorConReversion = " SELECT COUNT(*) as numero " +
			"FROM manejopaciente.egresos " +
			"WHERE cuenta                                    = ? " +
			"AND (TO_CHAR(fecha_reversion_egreso,'yyyy-mm-dd') > ? " +
			"OR (TO_CHAR(fecha_reversion_egreso,'yyyy-mm-dd')  = ? " +
			"AND hora_reversion_egreso                         > ?)) " +
			"AND hora_egreso                                  IS NOT NULL " +
			"AND codigo_persona_reversion                     IS NOT NULL " +
			"AND motivo_reversion                             IS NOT NULL " +
			"AND fecha_reversion_egreso                       IS NOT NULL ";
	
	private final static String sqlConsultarFechaHoraValoracionEvolucion = 
		" SELECT " +
			"TO_CHAR(fecha_evolucion,'dd/mm/yyyy')||' '||hora_evolucion AS fecha_hora_evolucion," +
			"TO_CHAR(fecha_valoracion,'dd/mm/yyyy')||' '||hora_valoracion AS fecha_hora_valoracion " +
			"FROM manejopaciente.cuentas cu " +
			"INNER JOIN ordenes.solicitudes sol " +
			"ON (cu.id = sol.cuenta) " +
			"INNER JOIN historiaclinica.valoraciones val " +
			"ON (sol.numero_solicitud = val.numero_solicitud) " +
			"LEFT OUTER JOIN historiaclinica.evoluciones evo " +
			"ON (val.numero_solicitud = evo.valoracion) " +
			"WHERE cu.id_ingreso         = ? " +
			"ORDER BY evo.fecha_evolucion DESC," +
			"evo.hora_evolucion DESC        ," +
			"val.fecha_valoracion DESC      ," +
			"val.hora_valoracion DESC"; 
	
	private final static String sqlConsultarIncapacidadesPaciente = " SELECT " +
			"			administracion.getnombrepersona(regi.codigo_medico) AS usuario_generacion," +
			"TO_CHAR(regi.fecha_grabacion, 'dd/mm/yyyy')|| ' '|| regi.hora_grabacion AS fecha_hora_generacion," +
			"ing.consecutivo        AS no_ingreso           ," +
			"administracion.getnomcentroatencion(ing.centro_atencion) as centro_atencion," +
			"ing.id AS ingreso," +
			"regi.codigo_pk as codigo_pk "+
			"FROM ordenes.registro_incapacidades regi " +
			"INNER JOIN manejopaciente.ingresos ing " +
			"ON (regi.ingreso        = ing.id) " +
			"WHERE regi.codigo_paciente = ? " +
			"AND regi.estado        = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
	
	private final static String sqlConsultarPacientesIncapacidad = "" +
			"SELECT administracion.getnombrepersona(regi.codigo_paciente)   AS nombre_paciente        ," +
			"manejopaciente.getidentificacionpaciente(regi.codigo_paciente) AS identificacion_paciente," +
			"regi.codigo_paciente                                           AS codigo_paciente        ," +
			"ing.centro_atencion                                            AS centro_atencion        ," +
			"cue.via_ingreso                                                AS via_ingreso            ," +
			"scue.convenio                                                  AS convenio               ," +
			"facturacion.getnombreconvenio(scue.convenio)                   AS nombre_convenio        ," +
			"manejopaciente.getnombreviaingreso(cue.via_ingreso)            AS nombre_via_ingreso     ," +
			"regi.codigo_pk                                                 AS codigo_pk " +
			"FROM ordenes.registro_incapacidades regi " +
			"INNER JOIN manejopaciente.ingresos ing " +
			"ON (regi.ingreso = ing.id) " +
			"INNER JOIN manejopaciente.cuentas cue " +
			"ON (ing.id = cue.id_ingreso) " +
			"INNER JOIN manejopaciente.sub_cuentas scue " +
			"ON (ing.id = scue.ingreso) " +
			"WHERE TO_CHAR(regi.fecha_grabacion,'yyyy-mm-dd') BETWEEN ? AND ? " +
			"AND ing.centro_atencion = ? " +
			"AND regi.estado        = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
	
	private final static String sqlConsultaConveniosIngreso = "SELECT facturacion.getnombreconvenio(scue.convenio) AS nombre_convenio," +
			"scue.nro_prioridad                                 AS prioridad      ," +
			"scue.convenio                                      AS convenio " +
			"FROM ordenes.registro_incapacidades regi " +
			"INNER JOIN manejopaciente.sub_cuentas scue " +
			"ON(regi.ingreso   = scue.ingreso) " +
			"WHERE regi.codigo_pk = ? " +
			"AND regi.estado        = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
			"ORDER BY scue.nro_prioridad";
	
	private final static String sqlConsultarDiagnosticoEvoluciones = "SELECT acronimo_diagnostico AS acronimo," +
			"tipo_cie_diagnostico       AS tipo_cie, manejopaciente.getdiagnostico(acronimo_diagnostico,tipo_cie_diagnostico) AS nombre " +
			"FROM historiaclinica.evol_diagnosticos " +
			"WHERE evolucion = ? " +
			"AND principal      = "+ValoresPorDefecto.getValorTrueParaConsultas();
	
	private final static String sqlConsultarDiagnosticoValoraciones = "SELECT acronimo_diagnostico AS acronimo," +
			"tipo_cie_diagnostico       AS tipo_cie, manejopaciente.getdiagnostico(acronimo_diagnostico,tipo_cie_diagnostico) AS nombre " +
			"FROM historiaclinica.val_diagnosticos " +
			"WHERE valoracion = ? " +
			"AND principal      = "+ValoresPorDefecto.getValorTrueParaConsultas();
	
	private final static String sqlEliminarIncapacidadesInactivasXRegistro = " DELETE " +
			"FROM ordenes.registro_incapacidades " +
			"WHERE activo = '" + ConstantesBD.acronimoNo + "' " +
			"AND ingreso  = ? " +
			"AND estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
	
	private static final String sqlEliminarRegistrosLogReg = " DELETE " +
			"FROM ordenes.log_reg_incapacidades " +
			"WHERE registro_incapacidad in " +
			"(SELECT codigo_pk FROM ordenes.registro_incapacidades WHERE activo = ? and ingreso = ?)";
	
	private static final String sqlConsultarIncapacidadesCambios = "SELECT codigo_pk AS codigo_pk FROM ordenes.registro_incapacidades WHERE activo = ? AND ingreso = ?";
	
	private static final String sqlActualizarEstado = "UPDATE ordenes.registro_incapacidades SET estado = ?, " +
			"										fecha_anulacion = null, hora_anulacion = null, usuario_anulacion = null, " +
			"										motivo_anulacion = null WHERE codigo_pk = ?";
	
	private static final String sqlConsultaUltimaTransaccionLog = " SELECT CODIGO_PK          AS codigo_pk               ," +
			"REGISTRO_INCAPACIDAD     AS registro_incapacidad    ," +
			"FECHA_INICIO_INCAPACIDAD AS fecha_inicio_incapacidad," +
			"FECHA_FIN_INCAPACIDAD    AS fecha_fin_incapacidad   ," +
			"DIAS_INCAPACIDAD         AS dias_incapacidad        ," +
			"TIPO_INCAPACIDAD         AS TIPO_INCAPACIDAD        ," +
			"PRORROGA                 AS PRORROGA                ," +
			"OBSERVACIONES            AS OBSERVACIONES           ," +
			"CODIGO_MEDICO            AS CODIGO_MEDICO           ," +
			"FECHA_MODIFICA           AS FECHA_MODIFICA          ," +
			"HORA_MODIFICA            AS HORA_MODIFICA           ," +
			"USUARIO_MODIFICA         AS USUARIO_MODIFICA" +
			"FROM ordenes.log_reg_incapacidades" +
			"WHERE codigo_pk =" +
			"(SELECT MAX(codigo_pk)" +
			"FROM ordenes.log_reg_incapacidades" +
			"WHERE registro_incapacidad = ?" +
			") ";
	
	private static final String sqlRevierteAUltimoLog = " UPDATE ordenes.registro_incapacidades " +
			"SET " +
			"FECHA_INICIO_INCAPACIDAD= ?," +
			"FECHA_FIN_INCAPACIDAD   = ?," +
			"DIAS_INCAPACIDAD        = ?," +
			"TIPO_INCAPACIDAD        = ?," +
			"PRORROGA                = ?," +
			"OBSERVACIONES           = ?," +
			"CODIGO_MEDICO           = ?," +
			"FECHA_MODIFICA          = ?," +
			"HORA_MODIFICA           = ?," +
			"USUARIO_MODIFICA        = ? " +
			"WHERE codigo_pk         = ? ";
	
	private static final String sqlEliminarLogrevertido = "DELETE " +
			"FROM ordenes.log_reg_incapacidades " +
			"WHERE codigo_pk = ?";
	
	private static final String sqlVerificaSolicitudFacturada = " SELECT " +
			"CASE " +
			"WHEN COUNT(facturado)>0 " +
			"THEN 'N' " +
			"ELSE 'S' " +
			"END AS cantidad_no_facturados " +
			"FROM facturacion.det_cargos " +
			"WHERE solicitud = ? " +
			"AND facturado     = '"+ConstantesBD.acronimoNo+"'";
	
	
	//*********************************************+SEBASTIAN*********************************************************************************
	/**
	 * Cadena que obtiene los registros de incapacidad x ingreso
	 */
	private static final String obtenerRegistrosIncapacidadXIngresoStr = " SELECT codigo_pk as codigo_pk, estado as estado,activo as activo from ordenes.registro_incapacidades WHERE ingreso = ?";
	
	//***************************************************************************************************************************************************
	
	
	public static int egresoPosteriorConReversion(Connection connection, DtoInfoFechaUsuario fechaUsuario, int cuenta)
	{
		logger.info("sqlEgresoPosteriorConReversion: "+sqlEgresoPosteriorConReversion);
		int resultado = 0;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(sqlEgresoPosteriorConReversion,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			ps.setInt   (1, cuenta);
			ps.setDate  (2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaUsuario.getFechaModifica())));
			ps.setDate  (3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaUsuario.getFechaModifica())));
			ps.setString(4, fechaUsuario.getHoraModifica());
			logger.info("cuenta:::"+cuenta);
			logger.info("fechaUsuario.getFechaModifica()::::"+UtilidadFecha.conversionFormatoFechaABD(fechaUsuario.getFechaModifica()));
			logger.info("fechaUsuario.getHoraModifica()::::::"+fechaUsuario.getHoraModifica());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				resultado = rs.getInt("numero");
			}
			ps.close();
			rs.close();
		}
		catch(SQLException s)
		{
			logger.error("Se ha generado un error consultando si esxiste un egreso con reversion posterior a la incapacidad---->"+s);
			s.printStackTrace();
		}
		logger.info("resultadivirris:::"+resultado);
		return resultado;
	}
	
	public static DtoRegistroIncapacidades consultarIncapacidadPorIngreso(Connection connection, int ingreso) {
		DtoRegistroIncapacidades registroIncapacidades = new DtoRegistroIncapacidades();
		boolean flag = false;
		logger.info("sqlConsultarIncapacidadPorIngreso: "+sqlConsultarIncapacidadPorIngreso);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(sqlConsultarIncapacidadPorIngreso,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			ps.setString(1, ConstantesIntegridadDominio.acronimoEstadoActivo);
			ps.setInt(2, ingreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			if (rs.next()) {
				logger.info("codigo_pk:::"+rs.getInt("codigo_pk"));
				flag = true;
				registroIncapacidades.setCodigoPk(rs.getInt("codigo_pk"));
				registroIncapacidades.setCodigoPaciente(rs.getInt("codigo_paciente"));
				registroIncapacidades.setIngreso(rs.getInt("ingreso"));
				registroIncapacidades.setFechaInicioIncapacidad(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_inicio_incapacidad")));
				registroIncapacidades.setFechaFinIncapacidad(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_fin_incapacidad")));
				registroIncapacidades.setDiasIncapacidad(rs.getInt("dias_incapacidad")+"");
				registroIncapacidades.setTipoIncapacidad(rs.getInt("tipo_incapacidad"));
				registroIncapacidades.setProrroga(rs.getString("prorroga"));
				registroIncapacidades.setCodigoMedico(rs.getInt("codigo_medico"));
				registroIncapacidades.setEspecialidad(rs.getInt("especialidad"));
				registroIncapacidades.setEstado(rs.getString("estado"));
				registroIncapacidades.setMotivoAnulacion(rs.getString("motivo_anulacion"));
				registroIncapacidades.setConsecutivo(rs.getString("consecutivo"));
				registroIncapacidades.setAnioConsecutivo(rs.getString("anio_consecutivo"));
				registroIncapacidades.setObservaciones(rs.getString("observaciones"));
				registroIncapacidades.setValoracion(rs.getInt("valoracion"));
				registroIncapacidades.setEvolucion(rs.getInt("evolucion"));
				registroIncapacidades.setAcronimoDiagnostico(rs.getString("acronimo_diagnostico"));
				registroIncapacidades.setTipoCie(rs.getInt("tipo_cie"));
				registroIncapacidades.getGrabacion().setFechaModifica(rs.getString("fecha_grabacion"));
				registroIncapacidades.getGrabacion().setHoraModifica(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("hora_grabacion")));
				registroIncapacidades.setDiagnostico(rs.getString("diagnostico"));
				registroIncapacidades.setActivo(rs.getString("activo"));
			}
			logger.info("getFechaModifica:::"+registroIncapacidades.getGrabacion().getFechaModifica());
			logger.info("getHoraModifica :::"+registroIncapacidades.getGrabacion().getHoraModifica());
			ps.close();
			rs.close();
		}
		catch (Exception e) {
			logger.error("Se ha generado un error consultando la incapacidad por el ingreso No. "+ingreso+"---->"+e);
			e.printStackTrace();
		}
		if(!flag)
		{
			registroIncapacidades.setEstado("NADA");
		}
		logger.info("codigo_pk devuelve:::"+registroIncapacidades.getCodigoPk());
		return registroIncapacidades;	
	}
	
	public static HashMap consultarTiposIncapacidad(Connection connection) 
	{
		HashMap map = new HashMap<String, Object>();
		logger.info("sqlConsultarTiposIncapacidad: "+sqlConsultarTiposIncapacidad);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(sqlConsultarTiposIncapacidad,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			
			map = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (Exception e) {
			logger.error("Se ha generado un error consultando los tipops de incapacidad---->"+e);
			e.printStackTrace();
		}
		return map;
		
	}
	
	public static String insertarIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario) {
		logger.info("sqlInsertarIncapacidad: "+sqlInsertarIncapacidad);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlInsertarIncapacidad, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));

			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(connection,"ordenes.seq_registro_incapacidades");
			logger.info("incapacidad.getEspecialidad():::"+incapacidad.getEspecialidad());
			ps.setInt   (1, secuencia);
			ps.setInt   (2, incapacidad.getCodigoPaciente());
			ps.setInt   (3, incapacidad.getIngreso());
			ps.setDate  (4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaInicioIncapacidad())));
			ps.setDate  (5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaFinIncapacidad())));
			ps.setInt   (6, Utilidades.convertirAEntero(incapacidad.getDiasIncapacidad()));
			ps.setInt   (7, incapacidad.getTipoIncapacidad());
			ps.setString(8, incapacidad.getProrroga());
			ps.setDate  (9, Date.valueOf(fechaUsuario.getFechaModificaFromatoBD()));
			ps.setString(10, fechaUsuario.getHoraModifica());
			ps.setInt   (11, incapacidad.getCodigoMedico());
			ps.setInt   (12, incapacidad.getEspecialidad());
			ps.setDate  (13, Date.valueOf(fechaUsuario.getFechaModificaFromatoBD()));
			ps.setString(14, fechaUsuario.getHoraModifica());
			ps.setString(15, fechaUsuario.getUsuarioModifica());
			ps.setString(16, incapacidad.getEstado());
			
			if (!incapacidad.getMotivoAnulacion().equals(""))
				ps.setString(17, incapacidad.getMotivoAnulacion());
			else
				ps.setNull(17, Types.VARCHAR);
			
			if(!incapacidad.getConsecutivo().equals(""))
				ps.setString(18, incapacidad.getConsecutivo());
			else
				ps.setNull(18, Types.VARCHAR);
			
			if(!incapacidad.getAnioConsecutivo().equals(""))
				ps.setString(19, incapacidad.getAnioConsecutivo());
			else
				ps.setNull(19, Types.VARCHAR);
			
			if(!incapacidad.getObservaciones().equals(""))
				ps.setString(20, incapacidad.getObservaciones());
			else
				ps.setNull(20, Types.VARCHAR);
			
			if(!incapacidad.getAcronimoDiagnostico().equals(""))
				ps.setString(21, incapacidad.getAcronimoDiagnostico());
			else
				ps.setNull(21, Types.VARCHAR);
			
			if(incapacidad.getTipoCie() != ConstantesBD.codigoNuncaValido)
				ps.setInt(22, incapacidad.getTipoCie());
			else
				ps.setNull(22, Types.INTEGER);
			
			ps.setString(23, incapacidad.getActivo());
					
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error Insertando la Incapacidad---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static String actualizarIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario) {
		logger.info("sqlActualizarIncapacidad: "+sqlActualizarIncapacidad);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlActualizarIncapacidad, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ps.setDate  (1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaInicioIncapacidad())));
			ps.setDate  (2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaFinIncapacidad())));
			ps.setInt   (3, Utilidades.convertirAEntero(incapacidad.getDiasIncapacidad()));
			ps.setInt   (4, incapacidad.getTipoIncapacidad());
			ps.setString(5, incapacidad.getProrroga());
			ps.setString(6, incapacidad.getObservaciones());
			ps.setDate  (7, Date.valueOf(fechaUsuario.getFechaModificaFromatoBD()));
			ps.setString(8, fechaUsuario.getHoraModifica());
			ps.setString(9, fechaUsuario.getUsuarioModifica());
			
			if(!incapacidad.getConsecutivo().equals(""))		ps.setString(10, incapacidad.getConsecutivo());
			else												ps.setNull  (10, Types.VARCHAR);
			
			if(!incapacidad.getAnioConsecutivo().equals(""))	ps.setString(11, incapacidad.getAnioConsecutivo());
			else												ps.setNull  (11, Types.VARCHAR);
			
			ps.setString(12, incapacidad.getActivo());
			
			ps.setInt   (13, incapacidad.getCodigoPk());
			
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error Actualizando la Incapacidad---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static String actualizarEstadoIncapacidad(Connection connection, String estado, int codigoPK, DtoInfoFechaUsuario fechaHoraUsuarioAnulacion)
	{
		logger.info("estado:::"+estado);
		logger.info("fechaHoraUsuarioAnulacion.getFechaModificaFromatoBD():::"+fechaHoraUsuarioAnulacion.getFechaModificaFromatoBD());
		logger.info("fechaHoraUsuarioAnulacion.getHoraModifica();;::::"+fechaHoraUsuarioAnulacion.getHoraModifica());
		logger.info("fechaHoraUsuarioAnulacion.getUsuarioModifica();;::"+fechaHoraUsuarioAnulacion.getUsuarioModifica());
		logger.info("codigoPK:::"+codigoPK);
		
		String consulta = sqlActualizarEstadoIncapacidad;
		if (estado.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			consulta += ",fecha_anulacion = ?," +
						"hora_anulacion = ?," +
						"usuario_anulacion = ? ";
			
		consulta += "WHERE codigo_pk   = ?";
		logger.info("sqlActualizarEstadoIncapacidad: "+consulta);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					consulta, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ps.setString(1, estado);
			
			int i = 2;
			if (estado.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			{
				ps.setDate(i, Date.valueOf(fechaHoraUsuarioAnulacion.getFechaModificaFromatoBD()));
				i++;
				ps.setString(i, fechaHoraUsuarioAnulacion.getHoraModifica());
				i++;
				ps.setString(i, fechaHoraUsuarioAnulacion.getUsuarioModifica());
				i++;
			}
			ps.setInt(i, codigoPK);
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error Actualizando el estado de la Incapacidad---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static String insertarLogIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario) {
		logger.info("sqlInsertarLogIncapacidad: "+sqlInsertarLogIncapacidad);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlInsertarLogIncapacidad, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(connection,"ordenes.seq_log_reg_incapacidades");
			
			ps.setInt   (1, secuencia);
			ps.setInt   (2, incapacidad.getCodigoPk());
			ps.setDate  (3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaInicioIncapacidad())));
			ps.setDate  (4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaFinIncapacidad())));
			ps.setInt   (5, Utilidades.convertirAEntero(incapacidad.getDiasIncapacidad()));
			ps.setInt   (6, incapacidad.getTipoIncapacidad());
			ps.setString(7, incapacidad.getProrroga());
			ps.setString(8, incapacidad.getObservaciones());
			ps.setInt   (9, incapacidad.getCodigoMedico());
			ps.setDate  (10, Date.valueOf(fechaUsuario.getFechaModificaFromatoBD()));
			ps.setString(11, fechaUsuario.getHoraModifica());
			ps.setString(12, fechaUsuario.getUsuarioModifica());
			
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error insertando en el log la Incapacidad---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static String consultarFechaHoraAtencion(Connection connection, int ingreso)
	{
		logger.info("sqlConsultarFechaHoraValoracionEvolucion-->"+sqlConsultarFechaHoraValoracionEvolucion);
		String resultado = "";
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlConsultarFechaHoraValoracionEvolucion, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, ingreso);
			logger.info("ingreso: "+ingreso);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				
				
				if(!rs.getString(1).isEmpty() && rs.getString(1)!= null)	resultado = rs.getString(1);
				else														resultado = rs.getString(2);
			}
			
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.info("error consultando la hora fecha atencion ----> "+ e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static ArrayList<DtoIncapacidad> consultarIncapacidadesPaciente (Connection connection, int codigoPaciente)
	{
		logger.info("sqlConsultarIncapacidadesPaciente--->"+sqlConsultarIncapacidadesPaciente);
		DtoIncapacidad incapacidad;
		ArrayList<DtoIncapacidad> list = new ArrayList<DtoIncapacidad>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlConsultarIncapacidadesPaciente, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoPaciente);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()){
				incapacidad = new DtoIncapacidad();
				incapacidad.setCentroAtencion(rs.getString("centro_atencion"));
				incapacidad.setNoIngreso(rs.getInt("no_ingreso"));
				incapacidad.setIngreso(rs.getInt("ingreso"));
				incapacidad.setFechaHoraAtencion(consultarFechaHoraAtencion(connection, incapacidad.getIngreso()));
				incapacidad.setFechaHoraGeneracion(rs.getString("fecha_hora_generacion"));
				incapacidad.setUsuarioGeneracion(rs.getString("usuario_generacion"));
				incapacidad.setCodigoPk(rs.getInt("codigo_pk"));
				list.add(incapacidad);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// consultaIncapacidades Paciente: handle exception
			logger.info("error consultando las incapacidacdes x paciente----> "+ e);
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<DtoIncapacidad> consultarPacientesIncapacidad(Connection connection, DtoIncapacidad parametros){
		String consulta = sqlConsultarPacientesIncapacidad;
		if (parametros.getViaIngreso() != ConstantesBD.codigoNuncaValido)
			consulta += "AND cue.via_ingreso     = ? ";
		if (parametros.getConvenio() != ConstantesBD.codigoNuncaValido)
			consulta += "AND scue.convenio       = ? ";

		logger.info("sqlConsultarPacientesIncapacidad--->"+consulta);
		
		DtoIncapacidad incapacidad;
		ArrayList<DtoIncapacidad> list = new ArrayList<DtoIncapacidad>();
		
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					consulta+" order by scue.convenio, cue.via_ingreso", ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.getFechaInicialGeneracion())));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.getFechaFinalGeneracion())));
			ps.setInt(3, parametros.getCodCentroAtencion());
			
			int i = 4;
			
			if (parametros.getViaIngreso() != ConstantesBD.codigoNuncaValido){
				ps.setInt(i, parametros.getViaIngreso());
				i++;
			}
			
			if (parametros.getConvenio() != ConstantesBD.codigoNuncaValido){
				ps.setInt(i, parametros.getConvenio());
				i++;
			}
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		
			while (rs.next()){
				incapacidad = new DtoIncapacidad();
				incapacidad.setNomPaciente(rs.getString("nombre_paciente"));
				incapacidad.setIdenPaciente(rs.getString("identificacion_paciente"));
				incapacidad.setCodigoPaciente(rs.getInt("codigo_paciente"));
				incapacidad.setCodCentroAtencion(rs.getInt("centro_atencion"));
				incapacidad.setViaIngreso(rs.getInt("via_ingreso"));
				incapacidad.setConvenio(rs.getInt("convenio"));
				incapacidad.setNomConvenio(rs.getString("nombre_convenio"));
				incapacidad.setNomViaIngreso(rs.getString("nombre_via_ingreso"));
				incapacidad.setCodigoPk(rs.getInt("codigo_pk"));
				list.add(incapacidad);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// consultaIncapacidades Paciente: handle exception
			logger.info("error consultando los paciente con incapacidades----> "+ e);
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<DtoIncapacidad> consultaConveniosIngreso (Connection connection, int codigoPk)
	{
		logger.info("sqlConsultaConveniosIngreso--->"+sqlConsultaConveniosIngreso);
		DtoIncapacidad incapacidad;
		ArrayList<DtoIncapacidad> list = new ArrayList<DtoIncapacidad>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlConsultaConveniosIngreso, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("codigo_pk::::"+codigoPk);
			
			ps.setInt(1, codigoPk);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()){
				incapacidad = new DtoIncapacidad();
				incapacidad.setNomConvenio(rs.getString("nombre_convenio"));
				incapacidad.setConvenio(rs.getInt("convenio"));
				incapacidad.setPrioridad(rs.getInt("prioridad"));
				list.add(incapacidad);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.info("error consultando los convenios por ingreso----> "+ e);
			e.printStackTrace();
		}
		return list;
	}
	
	public static InfoDatos consultarDiagnosticoEvoluciones(Connection connection, int solicitud)
	{
		InfoDatos datos = new InfoDatos();
		logger.info("sqlConsultarDiagnosticoEvoluciones--->"+sqlConsultarDiagnosticoEvoluciones);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlConsultarDiagnosticoEvoluciones, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, solicitud);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next()){
				datos.setCodigo(rs.getInt("tipo_cie"));
				datos.setDescripcion(rs.getString("acronimo"));
				datos.setDescripcionInd(rs.getString("nombre"));
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.info("error consultando los dx de la ult evoluvion----> "+ e);
			e.printStackTrace();
		}
		return datos;
	}
	
	public static InfoDatos consultarDiagnosticoValoraciones(Connection connection, int solicitud)
	{
		InfoDatos datos = new InfoDatos();
		logger.info("sqlConsultarDiagnosticoValoraciones--->"+sqlConsultarDiagnosticoValoraciones);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlConsultarDiagnosticoValoraciones, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, solicitud);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next()){
				datos.setCodigo(rs.getInt("tipo_cie"));
				datos.setDescripcion(rs.getString("acronimo"));
				datos.setDescripcionInd(rs.getString("nombre"));
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.info("error consultando los dx de la ult evoluvion----> "+ e);
			e.printStackTrace();
		}
		return datos;
	}
	
	public static String eliminarIncapacidadesInactivasXRegistro(Connection connection, int noIngreso) {
		logger.info("sqlEliminarIncapacidadesInactivasXRegistro: "+sqlEliminarIncapacidadesInactivasXRegistro);
		String respuesta =  ConstantesBD.acronimoNo;
		logger.info("ingersooooo:...."+noIngreso);
		if(UtilidadBD.iniciarTransaccion(connection)){
			if (eliminarRegistrosLogReg(connection, noIngreso).equals(ConstantesBD.acronimoSi)){
				try {
					PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
							sqlEliminarIncapacidadesInactivasXRegistro, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
					
					ps.setInt   (1, noIngreso);
					
					int resultadoInsert = ps.executeUpdate();
		
					if (resultadoInsert > 0)
						respuesta = ConstantesBD.acronimoSi;
					else
						respuesta = ConstantesBD.acronimoNo;
					
					ps.close();
		
				} catch (Exception e) {
					respuesta = ConstantesBD.acronimoNo;
					logger.error("Se ha generado un error eliminando los registros con el campo activo N x ingreso---->"+e);
					e.printStackTrace();
				}
			}
		}
		if(respuesta.equals(ConstantesBD.acronimoSi)){
				UtilidadBD.finalizarTransaccion(connection);
		}
		else{
			UtilidadBD.abortarTransaccion(connection);
		}
			
		return respuesta;
	}
	
	public static String eliminarRegistrosLogReg(Connection connection, int noIngreso) {
		logger.info("sqlEliminarRegistrosLogReg: "+sqlEliminarRegistrosLogReg);
		String respuesta =  ConstantesBD.acronimoSi;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlEliminarRegistrosLogReg, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setInt   (2, noIngreso);
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error eliminando los registros con el campo activo N x ingreso---->"+e);
			e.printStackTrace();
		}
		logger.info("respuesta:::___"+respuesta);
		return respuesta;
	}
	
	public static String consultarIncapacidadesCambios(Connection connection, int noIngreso)
	{
		String resultado = ConstantesBD.acronimoNo;
		logger.info("sqlConsultarIncapacidadesCambios--->"+sqlConsultarIncapacidadesCambios);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlConsultarIncapacidadesCambios, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, ConstantesBD.acronimoCambio);
			ps.setInt(2, noIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next()){
				if (rs.getString("estado").equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
					resultado = actualizarEstado(connection, rs.getInt("codigo_pk"));
				}
				else{
					resultado = consultaUltimaTransaccionLog(connection, rs.getInt("codigo_pk"));
				}
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.info("error consultando los dx de la ult evoluvion----> "+ e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	public static String actualizarEstado(Connection connection, int codigoPk) {
		logger.info("sqlActualizarEstado: "+sqlActualizarEstado);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlActualizarEstado, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setString (1, ConstantesIntegridadDominio.acronimoEstadoActivo);
			ps.setInt    (2, codigoPk);
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha reversado el estado a activo satisfactoriamente---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static String consultaUltimaTransaccionLog(Connection connection, int registroIncapacidad)
	{
		String resultado = ConstantesBD.acronimoNo;
		if(UtilidadBD.iniciarTransaccion(connection)){
			DtoRegistroIncapacidades incapacidad = new DtoRegistroIncapacidades();
			logger.info("sqlConsultaUltimaTransaccionLog--->"+sqlConsultaUltimaTransaccionLog);
			try {
				PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
						sqlConsultaUltimaTransaccionLog, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setInt(1, registroIncapacidad);
				
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next()){
					incapacidad.setCodigoPk(rs.getInt("registro_incapacidad"));
					incapacidad.setFechaInicioIncapacidad(rs.getString("fecha_inicio_incapacidad"));
					incapacidad.setFechaFinIncapacidad(rs.getString("fecha_fin_incapacidad"));
					incapacidad.setDiasIncapacidad(rs.getString("dias_incapacidad"));
					incapacidad.setTipoIncapacidad(rs.getInt("tipo_incapacidad"));
					incapacidad.setProrroga(rs.getString("prorroga"));
					incapacidad.setObservaciones(rs.getString("observaciones"));
					incapacidad.setCodigoMedico(rs.getInt("codigo_medico"));
					incapacidad.getGrabacion().setFechaModifica(rs.getString("fecha_modifica"));
					incapacidad.getGrabacion().setHoraModifica(rs.getString("hora_modifica"));
					incapacidad.getGrabacion().setUsuarioModifica(rs.getString("usuario_modifica"));
					if(revierteAUltimoLog(connection, incapacidad).equals(ConstantesBD.acronimoSi)){
						resultado = eliminarLogrevertido(connection, rs.getInt("codigo_pk"));
					}
				}
				
				rs.close();
				ps.close();
			} catch (SQLException e) {
				logger.info("error consultando el ult registro del log segun el reg. deincapacidad No. "+registroIncapacidad+"----> "+ e);
				e.printStackTrace();
			}
		}
		if(resultado.equals(ConstantesBD.acronimoSi)){
			UtilidadBD.finalizarTransaccion(connection);
		}
		else{
			UtilidadBD.abortarTransaccion(connection);
		}
		return resultado;
	}
	
	public static String revierteAUltimoLog(Connection connection, DtoRegistroIncapacidades incapacidad) {
		logger.info("sqlRevierteAUltimoLog: "+sqlRevierteAUltimoLog);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlActualizarEstado, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaInicioIncapacidad())));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getFechaFinIncapacidad())));
			ps.setInt(3, Utilidades.convertirAEntero(incapacidad.getDiasIncapacidad()));
			ps.setInt (4, incapacidad.getTipoIncapacidad());
			ps.setString(5, incapacidad.getProrroga());
			ps.setString(6, incapacidad.getObservaciones());
			ps.setInt(7, incapacidad.getCodigoMedico());
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(incapacidad.getGrabacion().getFechaModifica())));
			ps.setString(9, incapacidad.getGrabacion().getHoraModifica());
			ps.setString(10, incapacidad.getGrabacion().getUsuarioModifica());
			ps.setInt(11, incapacidad.getCodigoPk());
			
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha reversado registro del log satisfactoriamente---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public static String eliminarLogrevertido(Connection connection, int codigoPk) {
		logger.info("sqlEliminarLogrevertido: "+sqlEliminarLogrevertido);
		String respuesta =  ConstantesBD.acronimoNo;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlEliminarLogrevertido, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoPk);
			
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert > 0)
				respuesta = ConstantesBD.acronimoSi;
			else
				respuesta = ConstantesBD.acronimoNo;
			
			ps.close();

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha eliminado el registro reversado del log satisfactoriamente---->"+e);
			e.printStackTrace();
		}
		return respuesta;
	}
	
	//*********************************************************SEBASTIAN****************************************************************************
	
	/**
	 * Método para activar la información del registro de incapacidades
	 */
	public static ResultadoBoolean activarRegistrosIncapacidades(Connection con,DtoRegistroIncapacidades incapacidad)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String adicionConsulta = "";
			if(incapacidad.getValoracion()>0)
			{
				adicionConsulta+=", valoracion = "+incapacidad.getValoracion();
			}
			else if(incapacidad.getEvolucion()>0)
			{
				adicionConsulta+=", evolucion = "+incapacidad.getEvolucion();
			}
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(obtenerRegistrosIncapacidadXIngresoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,incapacidad.getIngreso());
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next() && resultado.isTrue())
			{
				String codigoPK = rs.getInt("codigo_pk")+"";
				String estado = rs.getString("estado");
				String activo = rs.getString("activo");
				logger.info("tipoCie:::"+incapacidad.getTipoCie());
				logger.info("acronimo::"+incapacidad.getAcronimoDiagnostico());
				if(estado.equals(ConstantesIntegridadDominio.acronimoEstadoActivo)&&(activo.equals("C")||activo.equals(ConstantesBD.acronimoNo)))
				{
					String consulta = "UPDATE ordenes.registro_incapacidades  SET activo = ?, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica = ?, especialidad = ?, acronimo_diagnostico = ?, tipo_cie = ? "+adicionConsulta+"  WHERE codigo_pk = ?";
					
					
					
					PreparedStatementDecorator pst01 = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst01.setString(1,ConstantesBD.acronimoSi);
					pst01.setString(2,incapacidad.getGrabacion().getUsuarioModifica());
					pst01.setInt(3,incapacidad.getEspecialidad());
					pst01.setString(4,incapacidad.getAcronimoDiagnostico());
					pst01.setInt(5,incapacidad.getTipoCie());
					pst01.setLong(6,Long.parseLong(codigoPK));
					if(pst01.executeUpdate()<=0)
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Problemas al activar la información de la incapacidad.");
					}
					
				}
				else
				{
					String consulta = "UPDATE ordenes.registro_incapacidades  SET activo = ?, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica = ?, acronimo_diagnostico = ?, tipo_cie = ? "+adicionConsulta+" WHERE codigo_pk = ?";
					PreparedStatementDecorator pst01 = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst01.setString(1,ConstantesBD.acronimoSi);
					pst01.setString(2,incapacidad.getGrabacion().getUsuarioModifica());
					pst01.setString(3,incapacidad.getAcronimoDiagnostico());
					pst01.setInt(4,incapacidad.getTipoCie());
					pst01.setLong(5,Long.parseLong(codigoPK));
					if(pst01.executeUpdate()<=0)
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Problemas al activar la información de la incapacidad.");
					}
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en activarRegistrosIncapacidades: "+e);
			e.printStackTrace();
			resultado.setResultado(false);
			resultado.setDescripcion("Problemas al activar la información de la incapacidad: "+e);
		}
		return resultado;
	}
	
	public static String verificaSolicitudFacturada(Connection connection, int solicitud)
	{
		logger.info("sqlVerificaSolicitudFacturada-->"+sqlVerificaSolicitudFacturada);
		String resultado = "";
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection.prepareStatement(
					sqlVerificaSolicitudFacturada, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, solicitud);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				resultado = rs.getString("cantidad_no_facturados");
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.info("error verificandoo si todos los servicios de una solicitud ya estan facturados ----> "+ e);
			e.printStackTrace();
		}
		return resultado;
	}
}
