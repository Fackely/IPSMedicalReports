package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo G�mez Fecha Junio de 2009
 */

public class SqlBaseMultasDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseMultasDao.class);

	private static final String strModificarEstado = "UPDATE multas_citas "
			+ "SET estado = ? " + "WHERE consecutivo = ? ";

	private static final String strVerificarEstado = "SELECT "
			+ "CASE WHEN estado = ? THEN 'S' " + "ELSE 'N' END AS estado "
			+ "FROM multas_citas WHERE  consecutivo = ? ";

	public static ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasRango(
			Connection connection, HashMap criterios, UsuarioBasico usuario) {

		DtoCitasNoRealizadas dtoCitasNoRealizadas;

		ArrayList<DtoCitasNoRealizadas> resultado = new ArrayList<DtoCitasNoRealizadas>();

		String consulta = "SELECT "
				+ "distinct(cita.codigo) AS codigo_cita, "
				+ "agen.codigo AS codigo_agenda, "
				+ "getnombreconsultorio(agen.consultorio) AS consultorio, "
				+ "getnomcentroatencion(agen.centro_atencion) AS centro_atencion, "
				+ "agen.unidad_consulta AS cod_unidad_agenda, "
				+ "agen.codigo_medico AS cod_medico, "
				+ "cita.codigo_paciente AS cod_paciente, "
				+ "cita.estado_cita AS cod_estado_cita, "
				+ "getnombreunidadconsulta(agen.unidad_consulta) AS unidad_agenda, "
				+ "to_char(agen.fecha,'yyyy-mm-dd') AS fecha, "
				+ "agen.hora_inicio AS hora, "
				+ "getnombreestadocita(cita.estado_cita) AS estado_cita, "
				+ "p1.primer_apellido || coalesce(' '||p1.segundo_apellido,' ') || p1.primer_nombre || coalesce(' '||p1.segundo_nombre,' ') AS paciente, "
				+ "p1.tipo_identificacion||' '||p1.numero_identificacion AS id, "
				+ "coalesce(getnombrepersona(agen.codigo_medico),'No Asignado') AS profesional "
				+ "FROM agenda agen "
				+ "inner join cita cita on (cita.codigo_agenda = agen.codigo) "
				+ "inner join personas p1 on (p1.codigo = cita.codigo_paciente) "
				+ "left outer join multas_citas mult on (cita.codigo = mult.cita) "
				+ "where to_char(agen.fecha, '"+ConstantesBD.formatoFechaBD+"') between ? and ? "
				+ "AND agen.centro_atencion IN ("
				+ criterios.get("centAten0").toString() + ") ";

		String consultaMultaConvenio = "select "
				+ "con.man_mul_inc_citas AS maneja_multa, "
				+ "con.val_mul_inc_citas AS valor_multa "
				+ "from "
				+ "cita cita "
				+ "inner join servicios_cita servC on (cita.codigo = servC.codigo_cita) "
				+ "inner join solicitudes sol on (servC.numero_solicitud = sol.numero_solicitud) "
				+ "inner join cuentas cue on (sol.cuenta = cue.id) "
				+ "inner join sub_cuentas sCue on (cue.id_ingreso = sCue.ingreso) "
				+ "inner join convenios con on (sCue.convenio = con.codigo) "
				+ "where cita.codigo = ? " + "and sCue.nro_prioridad = 1";

		String consultaMultaConvenioCitResStr = "select "
				+ "con.man_mul_inc_citas AS maneja_multa, "
				+ "con.val_mul_inc_citas AS valor_multa " + "from "
				+ "cita cita "
				+ "inner join convenios con on (cita.convenio = con.codigo) "
				+ "where cita.codigo = ? ";

		if (criterios.get("estado6").equals("-1")) {
//			consulta += "AND (cita.estado_cita in (1,2,3) OR (cita.estado_cita = 8 and (mult.estado = 'ANU' OR mult.estado is null))) ";
			consulta += "AND (cita.estado_cita in ("
				+ ConstantesBD.codigoEstadoCitaReservada
				+ ","
				+ ConstantesBD.codigoEstadoCitaAsignada
				+ ","
				+ ConstantesBD.codigoEstadoCitaReprogramada
				+ ") OR (cita.estado_cita = 8 and (mult.estado = 'ANU' OR mult.estado is null)" +
				
					"	AND (SELECT COUNT(1) "+
					"     FROM agenda agen_inter "+
					"  INNER JOIN cita cita_inter "+
					"       ON (cita_inter.codigo_agenda = agen_inter.codigo) "+
					"  INNER JOIN personas p_inter "+
					"       ON (p_inter.codigo = cita_inter.codigo_paciente) "+
					"  LEFT OUTER JOIN multas_citas mult_inter "+
					"       ON (cita_inter.codigo = mult_inter.cita) "+
					"    WHERE cita_inter.codigo_paciente=p1.codigo "+
					"  AND mult_inter.estado ='"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"' )=0 "+
					
					"AND (SELECT COUNT(1) " +
					"FROM agenda agen_inter " +
					"INNER JOIN cita cita_inter " +
					"ON (cita_inter.codigo_agenda = agen_inter.codigo) " +
					"LEFT OUTER JOIN multas_citas mult_inter " +
					"ON (cita_inter.codigo        = mult_inter.cita) " +
					"WHERE cita_inter.codigo=cita.codigo " +
					"AND mult_inter.estado             = '"+ConstantesIntegridadDominio.acronimoEstadoCondonado+"' )=0"+
					")"
					
				+ ") ";
		consulta += "AND "
				+ "(SELECT count(1) FROM solicitudes sol "
				+ "INNER JOIN servicios_cita sc ON(sol.numero_solicitud=sc.numero_solicitud) "
				+ "WHERE codigo_cita=cita.codigo AND sol.estado_historia_clinica not in ("
				+ ConstantesBD.codigoEstadoHCSolicitada + "," + ConstantesBD.codigoEstadoHCAnulada + "))=0 ";

		} else {
			if (criterios.get("estado6").equals("8")) {
				consulta += "AND cita.estado_cita = 8 and (mult.estado = 'ANU' OR mult.estado is null)" +
						"	AND (SELECT COUNT(1) "+
						"     FROM agenda agen_inter "+
						"  INNER JOIN cita cita_inter "+
						"       ON (cita_inter.codigo_agenda = agen_inter.codigo) "+
						"  INNER JOIN personas p_inter "+
						"       ON (p_inter.codigo = cita_inter.codigo_paciente) "+
						"  LEFT OUTER JOIN multas_citas mult_inter "+
						"       ON (cita_inter.codigo = mult_inter.cita) "+
						"    WHERE cita_inter.codigo_paciente=p1.codigo "+
						"  AND mult_inter.estado ='"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"' )=0 " +
						"AND (SELECT COUNT(1) " +
						"FROM agenda agen_inter " +
						"INNER JOIN cita cita_inter " +
						"ON (cita_inter.codigo_agenda = agen_inter.codigo) " +
						"LEFT OUTER JOIN multas_citas mult_inter " +
						"ON (cita_inter.codigo        = mult_inter.cita) " +
						"WHERE cita_inter.codigo=cita.codigo " +
						"AND mult_inter.estado             = '"+ConstantesIntegridadDominio.acronimoEstadoCondonado+"' )=0";
			} else {
				logger.info("esta cita:_::::::::::..."
						+ criterios.get("estado6"));
				consulta += "AND cita.estado_cita = ? ";
			}
		}
		if (!criterios.get("UnidadAgen1").equals("")) {
			consulta += "AND cita.unidad_consulta IN ("
					+ criterios.get("UnidadAgen1") + ") ";
		}
		if (!criterios.get("profesional5").equals("-1")) {
			logger.info("esta cita:_::::::::::..."
					+ criterios.get("profesional5"));
			consulta += "AND agen.codigo_medico = ? ";
		}

		consulta += "ORDER BY unidad_agenda, fecha, hora ASC ";
		logger.info("Consulta:_::::::::::..." + consulta);
		logger.info("fechas:_::::::::::..." + criterios.get("fechaIniCita2")
				+ ".........." + criterios.get("fechaFinCita3"));

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(consulta,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			int i = 1;
			ps.setDate(i, Date.valueOf(UtilidadFecha
					.conversionFormatoFechaABD(criterios.get("fechaIniCita2")
							.toString())));
			i++;
			ps.setDate(i, Date.valueOf(UtilidadFecha
					.conversionFormatoFechaABD(criterios.get("fechaFinCita3")
							.toString())));
			i++;
			if (!criterios.get("estado6").equals("-1")
					&& !criterios.get("estado6").equals("8")) {
				ps.setInt(i, Utilidades.convertirAEntero(criterios.get(
						"estado6").toString()));
				i++;
			}
			if (!criterios.get("profesional5").equals("-1")) {
				ps.setInt(i, Utilidades.convertirAEntero(criterios.get(
						"profesional5").toString()));
				i++;
			}

			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				dtoCitasNoRealizadas = new DtoCitasNoRealizadas();

				dtoCitasNoRealizadas
						.setConsultorio(rs.getString("consultorio"));
				dtoCitasNoRealizadas.setUnidadAgenda(rs
						.getString("unidad_agenda"));
				dtoCitasNoRealizadas.setCodUnidadAgenda(rs
						.getInt("cod_unidad_agenda"));
				dtoCitasNoRealizadas.setFechaCita(rs.getString("fecha"));
				dtoCitasNoRealizadas.setHoraCita(rs.getString("hora"));
				dtoCitasNoRealizadas.setEstadoCita(rs.getString("estado_cita"));
				dtoCitasNoRealizadas.setCodEstadoCita(rs
						.getInt("cod_estado_cita"));
				dtoCitasNoRealizadas
						.setNombrePaciente(rs.getString("paciente"));
				dtoCitasNoRealizadas.setIdPaciente(rs.getString("id"));
				dtoCitasNoRealizadas.setCodPaciente(rs.getInt("cod_paciente"));
				dtoCitasNoRealizadas
						.setProfesional(rs.getString("profesional"));
				dtoCitasNoRealizadas.setCodProfesional(rs.getInt("cod_medico"));
				dtoCitasNoRealizadas
						.setCodCentroAtencion(Utilidades
								.convertirAEntero(criterios.get("centAten0")
										.toString()));
				dtoCitasNoRealizadas.setNombreCentroAtencion(rs
						.getString("centro_atencion"));
				dtoCitasNoRealizadas.setCodAgenda(rs.getInt("codigo_agenda"));
				dtoCitasNoRealizadas.setCodCita(rs.getInt("codigo_cita"));

				resultado.add(dtoCitasNoRealizadas);
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			logger.error("error consultando " + e);
			e.printStackTrace();
		}

		if (ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(
				usuario.getCodigoInstitucionInt()).equals(
				ConstantesBD.acronimoSi)) {

			for (Iterator iterador = resultado.listIterator(); iterador
					.hasNext();) {

				DtoCitasNoRealizadas dtCitasNoRealizadas = (DtoCitasNoRealizadas) iterador
						.next();

				try {

					PreparedStatementDecorator ps = new PreparedStatementDecorator(
							connection.prepareStatement(consultaMultaConvenio,
									ConstantesBD.typeResultSet,
									ConstantesBD.concurrencyResultSet));

					ps.setInt(1, dtCitasNoRealizadas.getCodCita());

					ResultSetDecorator rs = new ResultSetDecorator(ps
							.executeQuery());

					while (rs.next()) {
						dtCitasNoRealizadas.setConManMulta(rs
								.getString("maneja_multa"));
						dtCitasNoRealizadas.setValMulta(rs
								.getString("valor_multa"));
					}
					ps.close();
					rs.close();

					if (dtCitasNoRealizadas.getCodEstadoCita() == ConstantesBD.codigoEstadoCitaReservada
							&& (dtCitasNoRealizadas.getValMulta() == null || dtCitasNoRealizadas
									.getValMulta().equals(""))) {
						PreparedStatementDecorator pst = new PreparedStatementDecorator(
								connection.prepareStatement(
										consultaMultaConvenioCitResStr,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));

						pst.setInt(1, dtCitasNoRealizadas.getCodCita());

						ResultSetDecorator rst = new ResultSetDecorator(pst
								.executeQuery());

						while (rst.next()) {
							dtCitasNoRealizadas.setConManMulta(rst
									.getString("maneja_multa"));
							dtCitasNoRealizadas.setValMulta(rst
									.getString("valor_multa"));
						}
						pst.close();
						rst.close();
					}

					if (dtCitasNoRealizadas.getConManMulta() != null) {
						if (dtCitasNoRealizadas.getConManMulta().equals(
								ConstantesBD.acronimoNo)) {
							dtCitasNoRealizadas
									.setChkMultaActivo(ConstantesBD.acronimoNo);
						} else {
							if (UtilidadFecha
									.esFechaMenorQueOtraReferencia(
											UtilidadFecha
													.conversionFormatoFechaAAp(dtCitasNoRealizadas
															.getFechaCita()),
											ValoresPorDefecto
													.getFechaInicioControlMultasIncumplimientoCitas(usuario
															.getCodigoInstitucionInt()))) {
								dtCitasNoRealizadas
										.setChkMultaActivo(ConstantesBD.acronimoNo);
							} else {
								dtCitasNoRealizadas
										.setChkMultaActivo(ConstantesBD.acronimoSi);
							}
						}
					} else {
						dtCitasNoRealizadas
								.setChkMultaActivo(ConstantesBD.acronimoSi);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (ValoresPorDefecto
				.getInstitucionManejaMultasPorIncumplimiento(
						usuario.getCodigoInstitucionInt()).equals(
						ConstantesBD.acronimoNo)) {
			for (Iterator iterador = resultado.listIterator(); iterador
					.hasNext();) {

				DtoCitasNoRealizadas dtCitasNoRealizadas = (DtoCitasNoRealizadas) iterador
						.next();
				dtCitasNoRealizadas.setChkMultaActivo(ConstantesBD.acronimoNo);

			}
		}

		return resultado;
	}

	public static ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasPaciente(
			Connection connection, HashMap criterios, UsuarioBasico usuario,
			PersonaBasica paciente) {

		DtoCitasNoRealizadas dtoCitasNoRealizadas;

		ArrayList<DtoCitasNoRealizadas> resultado = new ArrayList<DtoCitasNoRealizadas>();

		String consulta = "SELECT "
				+ "distinct(cita.codigo) AS codigo_cita, "
				+ "agen.codigo AS codigo_agenda, "
				+ "getnombreconsultorio(agen.consultorio) AS consultorio, "
				+ "getnomcentroatencion(agen.centro_atencion) AS centro_atencion, "
				+ "agen.unidad_consulta AS cod_unidad_agenda, "
				+ "agen.codigo_medico AS cod_medico, "
				+ "cita.codigo_paciente AS cod_paciente, "
				+ "cita.estado_cita AS cod_estado_cita, "
				+ "getnombreunidadconsulta(agen.unidad_consulta) AS unidad_agenda, "
				+ "to_char(agen.fecha, 'YYYY-MM-DD') AS fecha, "
				+ "agen.hora_inicio AS hora, "
				+ "getnombreestadocita(cita.estado_cita) AS estado_cita, "
				+ "p1.primer_apellido || coalesce(' '||p1.segundo_apellido,' ') || p1.primer_nombre || coalesce(' '||p1.segundo_nombre,' ') AS paciente, "
				+ "p1.tipo_identificacion||' '||p1.numero_identificacion AS id, "
				+ "coalesce(getnombrepersona(agen.codigo_medico),'No Asignado') AS profesional, "
				+ "agen.centro_atencion AS codigo_centro_atencion "
				+ "FROM agenda agen "
				+ "inner join cita cita on (cita.codigo_agenda = agen.codigo) "
				+ "inner join personas p1 on (p1.codigo = cita.codigo_paciente) "
				+ "left outer join multas_citas mult on (cita.codigo = mult.cita) "
				+ "where to_char(agen.fecha, '"+ConstantesBD.formatoFechaBD+"') <= ? "
				+ "AND p1.codigo = ? ";

		String consultaMultaConvenio = "select "
				+ "con.man_mul_inc_citas AS maneja_multa, "
				+ "con.val_mul_inc_citas AS valor_multa "
				+ "from "
				+ "cita cita "
				+ "inner join servicios_cita servC on (cita.codigo = servC.codigo_cita) "
				+ "inner join solicitudes sol on (servC.numero_solicitud = sol.numero_solicitud) "
				+ "inner join cuentas cue on (sol.cuenta = cue.id) "
				+ "inner join sub_cuentas sCue on (cue.id_ingreso = sCue.ingreso) "
				+ "inner join convenios con on (sCue.convenio = con.codigo) "
				+ "where cita.codigo = ? " + "and sCue.nro_prioridad = 1";

		String consultaMultaConvenioCitResStr = "select "
				+ "con.man_mul_inc_citas AS maneja_multa, "
				+ "con.val_mul_inc_citas AS valor_multa " + "from "
				+ "cita cita "
				+ "inner join convenios con on (cita.convenio = con.codigo) "
				+ "where cita.codigo = ? ";

		logger.info("Consulta:_::::::::::..." + consultaMultaConvenio);
		consulta += "AND (cita.estado_cita in ("
				+ ConstantesBD.codigoEstadoCitaReservada
				+ ","
				+ ConstantesBD.codigoEstadoCitaAsignada
				+ ","
				+ ConstantesBD.codigoEstadoCitaReprogramada
				+ ") OR (cita.estado_cita = 8 and (mult.estado = 'ANU' OR mult.estado is null)" +
				
				" AND ( "+
			        "          SELECT COUNT(1) from agenda agen_inter "+
			        "  INNER JOIN cita cita_inter "+
			        "       ON (cita_inter.codigo_agenda = agen_inter.codigo) "+
			        "  LEFT OUTER JOIN multas_citas mult_inter "+
			        "       ON (cita_inter.codigo = mult_inter.cita) "+
			        "  WHERE cita_inter.codigo_paciente=? AND mult_inter.estado='"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"' "+
			        ")=0 "+
			        "AND (SELECT COUNT(1) " +
					"FROM agenda agen_inter " +
					"INNER JOIN cita cita_inter " +
					"ON (cita_inter.codigo_agenda = agen_inter.codigo) " +
					"LEFT OUTER JOIN multas_citas mult_inter " +
					"ON (cita_inter.codigo        = mult_inter.cita) " +
					"WHERE cita_inter.codigo=cita.codigo " +
					"AND mult_inter.estado             = '"+ConstantesIntegridadDominio.acronimoEstadoCondonado+"' )=0"+			        
				")" 
				
				+ ") ";
		consulta += "AND "
				+ "(SELECT count(1) FROM solicitudes sol "
				+ "INNER JOIN servicios_cita sc ON(sol.numero_solicitud=sc.numero_solicitud) "
				+ "WHERE codigo_cita=cita.codigo AND sol.estado_historia_clinica not in ("
				+ ConstantesBD.codigoEstadoHCSolicitada + "," + ConstantesBD.codigoEstadoHCAnulada + "))=0 ";
		consulta += "ORDER BY unidad_agenda, fecha, hora ASC ";
		logger.info("Consulta:-------->"
				+ consulta
				+ "\n "
				+ paciente.getCodigoPersona()
				+ "\n"
				+ Date.valueOf(UtilidadFecha
						.conversionFormatoFechaABD(UtilidadFecha
								.getFechaActual())));

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(consulta,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(
											UtilidadFecha.getFechaActual())));

			ps.setInt(2, paciente.getCodigoPersona());
			ps.setInt(3, paciente.getCodigoPersona());

			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//			
			while (rs.next()) {
				dtoCitasNoRealizadas = new DtoCitasNoRealizadas();

				dtoCitasNoRealizadas
						.setConsultorio(rs.getString("consultorio"));
				dtoCitasNoRealizadas.setUnidadAgenda(rs
						.getString("unidad_agenda"));
				dtoCitasNoRealizadas.setCodUnidadAgenda(rs
						.getInt("cod_unidad_agenda"));
				dtoCitasNoRealizadas.setFechaCita(rs.getString("fecha"));
				dtoCitasNoRealizadas.setHoraCita(rs.getString("hora"));
				dtoCitasNoRealizadas.setEstadoCita(rs.getString("estado_cita"));
				dtoCitasNoRealizadas.setCodEstadoCita(rs
						.getInt("cod_estado_cita"));
				dtoCitasNoRealizadas
						.setNombrePaciente(rs.getString("paciente"));
				dtoCitasNoRealizadas.setIdPaciente(rs.getString("id"));
				dtoCitasNoRealizadas.setCodPaciente(rs.getInt("cod_paciente"));
				dtoCitasNoRealizadas
						.setProfesional(rs.getString("profesional"));
				dtoCitasNoRealizadas.setCodProfesional(rs.getInt("cod_medico"));
				dtoCitasNoRealizadas.setCodCentroAtencion(rs
						.getInt("codigo_centro_atencion"));
				dtoCitasNoRealizadas.setNombreCentroAtencion(rs
						.getString("centro_atencion"));
				dtoCitasNoRealizadas.setCodAgenda(rs.getInt("codigo_agenda"));
				dtoCitasNoRealizadas.setCodCita(rs.getInt("codigo_cita"));

				resultado.add(dtoCitasNoRealizadas);
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			logger.error("error consultando " + e);
			e.printStackTrace();
		}

		if (ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(
				usuario.getCodigoInstitucionInt()).equals(
				ConstantesBD.acronimoSi)) {

			for (Iterator iterador = resultado.listIterator(); iterador
					.hasNext();) {

				DtoCitasNoRealizadas dtCitasNoRealizadas = (DtoCitasNoRealizadas) iterador
						.next();

				try {

					PreparedStatementDecorator ps = new PreparedStatementDecorator(
							connection.prepareStatement(consultaMultaConvenio,
									ConstantesBD.typeResultSet,
									ConstantesBD.concurrencyResultSet));

					ps.setInt(1, dtCitasNoRealizadas.getCodCita());

					ResultSetDecorator rs = new ResultSetDecorator(ps
							.executeQuery());

					while (rs.next()) {
						dtCitasNoRealizadas.setConManMulta(rs
								.getString("maneja_multa"));
						dtCitasNoRealizadas.setValMulta(rs
								.getString("valor_multa"));
					}
					ps.close();
					rs.close();

					if (dtCitasNoRealizadas.getCodEstadoCita() == ConstantesBD.codigoEstadoCitaReservada
							&& (dtCitasNoRealizadas.getValMulta() == null || dtCitasNoRealizadas
									.getValMulta().equals(""))) {
						logger.info("buscar convenio de la cita");
						PreparedStatementDecorator pst = new PreparedStatementDecorator(
								connection.prepareStatement(
										consultaMultaConvenioCitResStr,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));

						pst.setInt(1, dtCitasNoRealizadas.getCodCita());

						ResultSetDecorator rst = new ResultSetDecorator(pst
								.executeQuery());

						while (rst.next()) {
							dtCitasNoRealizadas.setConManMulta(rst
									.getString("maneja_multa"));
							dtCitasNoRealizadas.setValMulta(rst
									.getString("valor_multa"));
						}
						pst.close();
						rst.close();
					}

					if (dtCitasNoRealizadas.getConManMulta() != null) {
						if (dtCitasNoRealizadas.getConManMulta().equals(
								ConstantesBD.acronimoNo)) {
							dtCitasNoRealizadas
									.setChkMultaActivo(ConstantesBD.acronimoNo);
						} else {
							if (UtilidadFecha
									.esFechaMenorQueOtraReferencia(
											UtilidadFecha
													.conversionFormatoFechaAAp(dtCitasNoRealizadas
															.getFechaCita()),
											ValoresPorDefecto
													.getFechaInicioControlMultasIncumplimientoCitas(usuario
															.getCodigoInstitucionInt()))) {
								dtCitasNoRealizadas
										.setChkMultaActivo(ConstantesBD.acronimoNo);
							} else {
								dtCitasNoRealizadas
										.setChkMultaActivo(ConstantesBD.acronimoSi);
							}
						}
					} else {
						dtCitasNoRealizadas
								.setChkMultaActivo(ConstantesBD.acronimoSi);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (ValoresPorDefecto
				.getInstitucionManejaMultasPorIncumplimiento(
						usuario.getCodigoInstitucionInt()).equals(
						ConstantesBD.acronimoNo)) {
			for (Iterator iterador = resultado.listIterator(); iterador
					.hasNext();) {

				DtoCitasNoRealizadas dtCitasNoRealizadas = (DtoCitasNoRealizadas) iterador
						.next();
				dtCitasNoRealizadas.setChkMultaActivo(ConstantesBD.acronimoNo);

			}
		}

		return resultado;
	}

	public static ArrayList<DtoServiciosCitas> buscarDetalleCitasRango(
			Connection connection, UsuarioBasico usuario,
			DtoCitasNoRealizadas dtoCitasNoRealizadas) {

		ArrayList<DtoServiciosCitas> arrayServicios = new ArrayList<DtoServiciosCitas>();

		DtoServiciosCitas dtoServiciosCitas;

		String consulta = "SELECT "
				+ "getcodigopropservicio2(serCita.servicio, "
				+ ValoresPorDefecto
						.getCodigoManualEstandarBusquedaServicios(usuario
								.getCodigoInstitucionInt())
				+ ") "
				+ "||'-'||getnombreservicio(serCita.servicio, "
				+ ValoresPorDefecto
						.getCodigoManualEstandarBusquedaServicios(usuario
								.getCodigoInstitucionInt()) + ") "
				+ "AS servicio, "
				+ "serCita.numero_solicitud AS numero_solicitud " + "FROM "
				+ "servicios_cita serCita " + "INNER JOIN agenda agen "
				+ "ON (serCita.codigo_agenda = agen.codigo) " + "WHERE "
				+ "agen.codigo = ?";

		String consultaConvenio = "SELECT "
				+ "getnombreconvenio(subC.convenio) AS convenio " + "FROM "
				+ "det_cargos cargos INNER JOIN sub_cuentas subC "
				+ "ON (cargos.sub_cuenta = subC.sub_cuenta) "
				+ "WHERE cargos.solicitud = ? " + "AND subC.nro_prioridad = 1";

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(consulta,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dtoCitasNoRealizadas.getCodAgenda());

			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {

				dtoServiciosCitas = new DtoServiciosCitas();

				dtoServiciosCitas.reset();

				dtoServiciosCitas.setServicio(rs.getString("servicio"));

				PreparedStatementDecorator psCon = new PreparedStatementDecorator(
						connection.prepareStatement(consultaConvenio,
								ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));

				psCon.setInt(1, rs.getInt("numero_solicitud"));

				ResultSetDecorator rsCon = new ResultSetDecorator(psCon
						.executeQuery());

				while (rsCon.next()) {
					logger.info("rsCon.getString(convenio)"
							+ rsCon.getString("convenio"));
					dtoServiciosCitas.setConvenio(rsCon.getString("convenio")
							.equals(null) ? "" : rsCon.getString("convenio"));
				}

				psCon.close();
				rsCon.close();

				arrayServicios.add(dtoServiciosCitas);
			}

			ps.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrayServicios;
	}

	public static ArrayList<DtoConceptoFacturaVaria> setArrayConcepFact(
			Connection connection, UsuarioBasico usuario) {

		DtoConceptoFacturaVaria dtoConceptoFacturaVaria;
		ArrayList<DtoConceptoFacturaVaria> arrayList = new ArrayList<DtoConceptoFacturaVaria>();

		String consultaConceptosFacturaVaria = "select consecutivo AS codigo, "
				+ "descripcion AS descripcion "
				+ "from conceptos_facturas_varias "
				+ "where tipo_concepto = 'MULT' and activo = 'S' "
				+ "and institucion = ?";

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(consultaConceptosFacturaVaria,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			ps.setInt(1, usuario.getCodigoInstitucionInt());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				dtoConceptoFacturaVaria = new DtoConceptoFacturaVaria();
				dtoConceptoFacturaVaria.setCodigo(rs.getString("codigo"));
				dtoConceptoFacturaVaria.setDescripcion(rs
						.getString("descripcion"));
				arrayList.add(dtoConceptoFacturaVaria);
			}

			ps.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrayList;
	}

	public static int insertarMulta(Connection connection,
			UsuarioBasico usuario, String convenioManejaMulta,
			String valorMultaConvenio, int codCita) {

		String cadenaInsercionMultas = "INSERT INTO multas_citas "
				+ "(consecutivo, fecha_generacion, hora_generacion, estado, usuario_generacion, valor, cita) "
				+ "VALUES (?, current_date, ?,'"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"', ?, ?, ?)";

		int secuencia = 0;

		PreparedStatementDecorator ps = null;

		try {
			ps = new PreparedStatementDecorator(connection.prepareStatement(
					cadenaInsercionMultas, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));

			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(connection,
					"seq_multas_citas");

			ps.setInt(1, secuencia);
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, usuario.getLoginUsuario());

			int valorMulta = 0;
			if (convenioManejaMulta != null
					&& convenioManejaMulta.equals(ConstantesBD.acronimoSi))
				valorMulta = Utilidades.convertirAEntero(valorMultaConvenio);
			else
				valorMulta = Utilidades.convertirAEntero(ValoresPorDefecto
						.getValorMultaPorIncumplimientoCitas(usuario
								.getCodigoInstitucionInt()));

			ps.setInt(4, valorMulta);

			ps.setInt(5, codCita);

			logger.info("actualizarEstadoCitaStr : " + cadenaInsercionMultas);
			logger.info(" 1 : consecutivo: " + secuencia);
			logger.info(" 2 : HoraActual: " + UtilidadFecha.getHoraActual());
			logger.info(" 3 : LoginUsuario: " + usuario.getLoginUsuario());
			logger.info(" 4 : valorMulta: " + valorMulta);
			logger.info(" 4 : codigoCita: " + codCita);

			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert == 0)
				return 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return secuencia;
	}

	/**
	 * Modificar el estado multa cita
	 * 
	 * @param Connection
	 *            connection
	 * @param HashMap
	 *            parametros
	 * @return boolean
	 */
	public static boolean actualizarEstadoMultaCita(Connection connection,
			HashMap parametros) {

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(strModificarEstado,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("estado").toString());
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(
					"consecutivo").toString()));
			return (ps.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.info("La  consuta >>>>>> " + strModificarEstado);
			logger.info("Estaddo >>>> " + parametros.get("estado"));
			logger.info("Estaddo >>>> " + parametros.get("consecutivo"));
			logger.error("Error actualizando el estado de la Multa Cita " + e);
		}
		return false;
	}

	/**
	 * Verificar el estado de la cita
	 * 
	 * @param Connection
	 *            connection
	 * @param HashMap
	 *            parametros
	 * @return boolean
	 */
	public static boolean verificarEstadoMultaCita(Connection connection,
			HashMap parametros) {

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(strVerificarEstado,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("estado").toString());
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(
					"consecutivo").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				if (rs.getString("estado").equals(ConstantesBD.acronimoSi))
					return true;
				else
					return false;
		} catch (SQLException e) {
			logger.info("La  consuta >>>>>> " + strVerificarEstado);
			logger.info("Estaddo >>>> " + parametros.get("estado"));
			logger.info("Estaddo >>>> " + parametros.get("consecutivo"));
			logger.error("Error verificando el estado de la Multa Cita " + e);
		}
		return false;
	}

	/**
	 * se Obtiene los conceptos que estan activo
	 * 
	 * @param Connection
	 *            connection
	 * @param UsuarioBasico
	 *            usuario
	 * @return ArrayList<DtoConceptoFacturaVaria>
	 */
	public static ArrayList<DtoConceptoFacturaVaria> getArrayConcepFactVarias(
			Connection connection, UsuarioBasico usuario) {

		DtoConceptoFacturaVaria dtoConceptoFacturaVaria;
		ArrayList<DtoConceptoFacturaVaria> arrayList = new ArrayList<DtoConceptoFacturaVaria>();

		String consultaConceptosFacturaVaria = "SELECT consecutivo AS codigo, "
				+ "descripcion AS descripcion "
				+ "FROM conceptos_facturas_varias " + "WHERE activo = 'S' "
				+ "AND institucion = ?";

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(consultaConceptosFacturaVaria,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			ps.setInt(1, usuario.getCodigoInstitucionInt());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				dtoConceptoFacturaVaria = new DtoConceptoFacturaVaria();
				dtoConceptoFacturaVaria.setCodigo(rs.getString("codigo"));
				dtoConceptoFacturaVaria.setDescripcion(rs
						.getString("descripcion"));
				arrayList.add(dtoConceptoFacturaVaria);
			}

			ps.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public static ResultadoBoolean actualizarEstadoMedicoSolicitudYDetCargos (Connection con, int numSolicitud)
	{
		String sqlActualizarEstadoMedicoSolicitud = "UPDATE ordenes.solicitudes SET estado_historia_clinica = "+ConstantesBD.codigoEstadoHCAnulada+" WHERE numero_solicitud = ?";
		String sqlActualizarEstadoDetCargos = "UPDATE facturacion.det_cargos SET estado = "+ConstantesBD.codigoEstadoFAnulada+" WHERE solicitud = ?";
		boolean flag = true;
		ResultadoBoolean rb = new ResultadoBoolean(false);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(sqlActualizarEstadoMedicoSolicitud,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numSolicitud);
			
			if(ps.executeUpdate() <= 0)
			{
				rb.setDescripcion("Registro(s) Actualizado");
			}
			ps.close();
			rb.setResultado(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			rb = new ResultadoBoolean(false," Error en la actualizacion del estado M�dico: "+e);
		}
		if(!rb.isTrue())
		{
			return rb;
		}
		
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(sqlActualizarEstadoDetCargos,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numSolicitud);
			
			if(ps.executeUpdate() <= 0)
			{
				rb.setDescripcion("Registro(s) Actualizado");
			}
			ps.close();
			rb.setResultado(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			rb = new ResultadoBoolean(false," Error en la actualizacion del estado Del Cargo: "+e);
		}
		return rb;
		
	}
}