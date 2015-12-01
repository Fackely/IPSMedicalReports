package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import util.UtilidadTexto;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;

public class SqlBaseRegistroEventosCatastroficosDao {
	
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger(SqlBaseRegistroEventosCatastroficosDao.class);

	public static String cadenaInsert = "INSERT INTO registro_evento_catastrofico ("
											+ " codigo,"
											+ " empresa_trabaja,"
											+ " ciudad_empresa,"
											+ " departamento_empresa,"
											+ " fecha_evento,"
											+ " direccion_evento,"
											+ " ciudad_evento,"
											+ " departamento_evento,"
											+ " zona_urbana_evento,"
											+ " naturaleza_evento,"
											+ " apellido_nombre_transporta,"
											+ " tipo_id_transporta,"
											+ " numero_id_transporta,"
											+ " ciudad_exp_id_transporta,"
											+ " depto_exp_id_transporta,"
											+ " telefono_transporta,"
											+ " direccion_transporta,"
											+ " ciudad_transporta,"
											+ " departamento_transporta,"
											+ " transporta_victima_desde,"
											+ " transporta_victima_hasta,"
											+ " tipo_transporte,"
											+ " placa_vehiculo_tranporta,"
											+ " estado,"
											+ " fecha_modifica,"
											+ " hora_modifica,"
											+ " usuario_modifica,"
											+ " pais_empresa,"
											+ " pais_evento,"
											+ " pais_exp_id_transporta,"
											+ " pais_transporta,"
											+ " sgsss,"
											+ " tipo_regimen,"
											+ " hora_ocurrencia,"
											+ " desc_ocurrencia,"
											+ " desc_otro_evento,"
											+ " tipo_referencia,"
											+ " fecha_remision,"
											+ " hora_remision,"
											+ " institucion,"
											+ " cod_inscrip_remitente,"
											+ " profesional_remite,"
											+ " cargo_profesional_remitente,"
											+ " fecha_aceptacion,"
											+ " hora_aceptacion,"
											+ " cod_inscrip_receptor,"
											+ " profesional_recibe,"
											+ " cargo_profesional_recibe,"
											+ " otro_tipo_trans,"
											+ " zona_transporte,"
											+ " total_fac_amp_qx,"
											+ " total_reclamo_amp_qx,"
											+ " total_fac_amp_tx,"
											+ " total_reclamo_amp_tx,"
											+ " es_reclamacion,"
											+ " furips,"
											+ " furpro,"
											+ " furtran,"
											+ " resp_glosa,"
											+ " nro_radicado_anterior,"
											+ " nro_cons_reclamacion,"
											+ " protesis,"
											+ " valor_protesis,"
											+ " adaptacion_protesis,"
											+ " valor_adaptacion_protesis,"
											+ " rehabilitacion,"
											+ " valor_rehabilitacion"
											+ ")"
										+ " VALUES ( ?, "
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ "	?,?,?,?,?,"
										+ " ?,?,?,?,?,"
										+ " ?,?,?,?,?,"
										+ " ?,?,?,?,?,"
										+ " ?)";

	/**
	 * 
	 */
	public static String cadenaConsultaGeneralEventoCatastrofico = " SELECT "
																		+ " codigo as codigo,"
																		+ " ingreso as ingreso,"
																		+ " empresa_trabaja as empresatrabaja,"
																		+ " coalesce(ciudad_empresa,'') as ciudadempresa,"
																		+ " coalesce(departamento_empresa,'') as departamentoempresa,"
																		+ " direccion_evento as direvento, "
																		+ " CASE WHEN fecha_evento IS NULL THEN '' ELSE fecha_evento||'' end as fechaevento,"
																		+ " coalesce(ciudad_evento,'') as ciudadevento,"
																		+ " coalesce(departamento_evento,'') as departamentoevento,"
																		+ " coalesce(zona_urbana_evento,'') as zonaurbanaevento,"
																		+ " coalesce(naturaleza_evento,-1) as naturalezaevento,"
																		+ " apellido_nombre_transporta as apellidonombretransporta,"
																		+ " coalesce(tipo_id_transporta,'') as tipoidtransporta,"
																		+ " numero_id_transporta as numeroidtransporta,"
																		+ " coalesce(ciudad_exp_id_transporta,'') as ciudadexpidtransporta,"
																		+ " coalesce(depto_exp_id_transporta,'') as deptoexpidtransporta,"
																		+ " telefono_transporta as telefonotransporta,"
																		+ " direccion_transporta as direcciontransporta,"
																		+ " coalesce(ciudad_transporta,'') as ciudadtransporta,"
																		+ " coalesce(departamento_transporta,'') as deptotransporta,"
																		+ " transporta_victima_desde as transportadesde,"
																		+ " transporta_victima_hasta as transportahasta,"
																		+ " tipo_transporte as tipotransporte,"
																		+ " placa_vehiculo_tranporta as placavehiculo, "
																		+ " fecha_modifica as fechamodifica,"
																		+ " hora_modifica as horamodifica,"
																		+ " usuario_modifica as usuariomodifica,"
																		+ " estado as estado, "
																		+ " coalesce(pais_empresa,'') as paisempresa,"
																		+ " coalesce(pais_evento,'') as paisevento,"
																		+ " coalesce(pais_exp_id_transporta,'') as paisexpidtransporta,"
																		+ " coalesce(pais_transporta,'') as paistransporta,"
																		+ " sgsss AS sgsss,"
																		+ " coalesce(tipo_regimen,'') AS tiporegimen,"
																		+ " coalesce(hora_ocurrencia,'') AS horaocurrencia,"
																		+ " coalesce(desc_ocurrencia,'') AS descocurrencia,"
																		+ " coalesce(desc_otro_evento,'') AS descotroevento,"
																		+ " coalesce(tipo_referencia,'') AS tiporeferencia,"
																		+ " coalesce(to_char(fecha_remision,'DD/MM/YYYY'),'') AS fecharemision,"
																		+ " coalesce(hora_remision,'') AS horaremision,"
																		+ " coalesce(cod_inscrip_remitente,'') AS prestadorremite,"
																		+ " coalesce(profesional_remite,'') AS profesionalremite,"
																		+ " coalesce(cargo_profesional_remitente,'') AS cargoprofesionalremite,"
																		+ " coalesce(cod_inscrip_receptor,'') AS prestadorrecibe,"
																		+ " coalesce(profesional_recibe,'') AS profesionalrecibe,"
																		+ " coalesce(cargo_profesional_recibe,'') AS cargoprofesionalrecibe,"
																		+ " coalesce(to_char(fecha_aceptacion,'DD/MM/YYYY'),'') AS fechaaceptacion,"
																		+ " coalesce(hora_aceptacion,'') AS horaaceptacion,"
																		+ " coalesce(otro_tipo_trans,'') AS otrotipotransporte,"
																		+ " coalesce(zona_transporte,'') AS zonatransporte,"
																		+ " coalesce(total_fac_amp_qx, 0.0) AS totalfacturadomedqx,"
																		+ " coalesce(total_reclamo_amp_qx, 0.0) AS totalreclamadomedqx,"
																		+ " coalesce(total_fac_amp_tx, 0.0) AS totalfacturadotrans,"
																		+ " coalesce(total_reclamo_amp_tx, 0.0) AS totalreclamadotrans,"
																		+ " es_reclamacion AS esreclamacion,"
																		+ " furips AS furips,"
																		+ " furpro AS furpro,"
																		+ " furtran AS furtran,"
																		+ " coalesce(resp_glosa, '') AS respglosa,"
																		+ " coalesce(nro_radicado_anterior, '') AS numradicadoanterior,"
																		+ " nro_cons_reclamacion AS numconsecutivoreclamacion,"
																		+ " coalesce(protesis,'') AS protesis,"
																		+ " coalesce(valor_protesis, 0.0) AS valorprotesis,"
																		+ " coalesce(adaptacion_protesis,'') AS adaptacionprotesis,"
																		+ " coalesce(valor_adaptacion_protesis, 0.0) AS valoradaptacionprotesis,"
																		+ " coalesce(rehabilitacion,'') AS rehabilitacion,"
																		+ " coalesce(valor_rehabilitacion, 0.0) AS valorrehabilitacion"
																	+ " FROM view_reg_evento_catastrofico " + " WHERE 1=1 ";

	/**
	 *encargado de consultar el listado de accidented de transito en estado
	 * procesado;
	 */
	private static final String strConsultaListadoEventosCatastroficos = " SELECT "
			+ " case when vrec.fecha_evento is null then '' else to_char(vrec.fecha_evento,'YYYY/MM/DD') end as fecha_evento0,"
			+ " case when vrec.departamento_evento is null then '' else getnombredepto(vrec.pais_evento,vrec.departamento_evento) end as departamento_evento1,"
			+ " case when vrec.ciudad_evento is null then '' else getnombreciudad(vrec.pais_evento,vrec.departamento_evento,vrec.ciudad_evento) end as ciudad_evento2,"
			+ " vrec.direccion_evento as direccion_evento3,"
			+ " getdescnatevent(vrec.naturaleza_evento) As tipo_evento4, "
			+ " vrec.codigo As codigo5, " +
			" i.id as idingreso,"+
			"  i.consecutivo as consecutivoingreso,"+
			"  to_char(i.fecha_ingreso,'dd/mm/yyyy') as fechaingreso,"+
			"  i.estado as estadoingreso,"+
			"  i.centro_atencion as centroatencion,"+
			"  cc.descripcion as desccentroatencion "
			+ " FROM  view_reg_evento_catastrofico vrec "
			+ " INNER JOIN ingresos i ON (i.id=vrec.ingreso)"
			+ " inner join centro_atencion cc on(cc.consecutivo=i.centro_atencion) " ;

	/**
	 * string encargado de asociar el ingreso a un accidente
	 */
	private static final String strAsociarEvento = " INSERT INTO ingresos_reg_even_catastrofico (ingreso,codigo_registro) VALUES (?,?)";

	/**
	 * 
	 */
	private static String cadenaModificacion = "UPDATE registro_evento_catastrofico SET "
													+ " empresa_trabaja=?,"
													+ " ciudad_empresa=?,"
													+ " departamento_empresa=?,"
													+ " fecha_evento=?,"
													+ " direccion_evento=?,"
													+ " ciudad_evento=?,"
													+ " departamento_evento=?,"
													+ " zona_urbana_evento=?,"
													+ " naturaleza_evento=?,"
													+ " apellido_nombre_transporta=?,"
													+ " tipo_id_transporta=?,"
													+ " numero_id_transporta=?,"
													+ " ciudad_exp_id_transporta=?,"
													+ " depto_exp_id_transporta=?,"
													+ " telefono_transporta=?,"
													+ " direccion_transporta=?,"
													+ " ciudad_transporta=?,"
													+ " departamento_transporta=?,"
													+ " transporta_victima_desde=?,"
													+ " transporta_victima_hasta=?,"
													+ " tipo_transporte=?,"
													+ " placa_vehiculo_tranporta=?,"
													+ " estado=?,"
													+ " fecha_modifica=?,"
													+ " hora_modifica=?,"
													+ " usuario_modifica=?,"
													+ " pais_empresa=?,"
													+ " pais_evento=?,"
													+ " pais_exp_id_transporta=?,"
													+ " pais_transporta=?, "
													+ " sgsss=?, "
													+ " tipo_regimen=?, "
													+ " hora_ocurrencia=?, "
													+ " desc_ocurrencia=?, "
													+ " desc_otro_evento=?, "
													+ " tipo_referencia=?, "
													+ " fecha_remision=?, "
													+ " hora_remision=?, "
													+ " institucion=?, "
													+ " cod_inscrip_remitente=?, "
													+ " profesional_remite=?, "
													+ " cargo_profesional_remitente=?, "
													+ " fecha_aceptacion=?, "
													+ " hora_aceptacion=?, "
													+ " cod_inscrip_receptor=?, "
													+ " profesional_recibe=?, "
													+ " cargo_profesional_recibe=?, "
													+ " otro_tipo_trans=?,"
													+ " zona_transporte=?,"
													+ " total_fac_amp_qx=?,"
													+ " total_reclamo_amp_qx=?,"
													+ " total_fac_amp_tx=?,"
													+ " total_reclamo_amp_tx=?,"
													+ " es_reclamacion=?,"
													+ " furips=?,"
													+ " furpro=?,"
													+ " furtran=?,"
													+ " resp_glosa=?,"
													+ " nro_radicado_anterior=?,"
													+ " nro_cons_reclamacion=?,"
													+ " protesis=?,"
													+ " valor_protesis=?,"
													+ " adaptacion_protesis=?,"
													+ " valor_adaptacion_protesis=?,"
													+ " rehabilitacion=?,"
													+ " valor_rehabilitacion=?"
												+ " WHERE codigo=? ";

	/**
	 * Cadena que actualiza el estado del registro de eventos catastroficos
	 */
	private static final String actualizarEstadoRegistroEventoCatastroficoStr = "UPDATE registro_evento_catastrofico SET "
			+ "estado = ?, "
			+ "fecha_modifica = ?, "
			+ "hora_modifica = ?, "
			+ "usuario_modifica = ? " + "WHERE codigo = ?";
	
	/**
	 * Metodo encargado de asociar un evento catastrofico a un ingreso
	 * 
	 * @param connection
	 * @param ingreso
	 * @param codigoEvento
	 * @return
	 */
	public static boolean asociarEvento(Connection connection, String ingreso,
			String codigoEvento) {
		logger.info("\n entro a asociarEvento ingreso --> " + ingreso
				+ " codigo Evento -->" + codigoEvento);

		String cadena = strAsociarEvento;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			// pk del ingreso
			ps.setInt(1, Utilidades.convertirAEntero(ingreso));
			// pk del evento catastrofico
			ps.setDouble(2, Utilidades.convertirADouble(codigoEvento));

			if (ps.executeUpdate() > 0)
				return true;

		} catch (SQLException e) {
			logger.info("\n problema asociando un evento al ingreso " + e);
		}

		return false;
	}

	/**
	 * Metodo encargado de consultar todos los registros de eventos
	 * catastroficos en estado finalizado
	 * 
	 * @param connection
	 * @param criterios
	 *            ------------------------- KEY'S DEL MAPA CRITERIOS
	 *            ------------------------- -- estado
	 * @return --------------------------- KEY'S DEL MAPA RESULTADO
	 *         --------------------------- -- fechaEvento0_ --
	 *         departamentoEvento1_ -- ciudadEvento2_ -- direccionEvento3_ --
	 *         tipoEvento4_ -- codigo5_
	 */
	public static HashMap cargarListadoEventosCatastroficos(Connection connection, HashMap criterios)
	{
		logger.info("\n entro  cargarListadoEventosCatastroficos --> "+ criterios);
		String cadena = strConsultaListadoEventosCatastroficos;


		if(criterios.containsKey("esPorIngresos")&&(criterios.get("esPorIngresos")+"").equals(ConstantesBD.acronimoSi))
		{
			return cargarListadoEventosIngresos(connection,criterios);
		}
		cadena=cadena+ " WHERE 1=1 ";
		if(criterios.containsKey("estado") && !UtilidadTexto.isEmpty(criterios.get("estado")+""))
		{
			cadena=cadena+" and vrec.estado= '"+ criterios.get("estado")+"'";
		}
		
		if(criterios.containsKey("paciente") && !UtilidadTexto.isEmpty(criterios.get("paciente")+""))
		{
			cadena=cadena+" and i.codigo_paciente="+ criterios.get("paciente");
		}
		
		cadena += " order by fecha_evento desc";
		HashMap resultado = new HashMap();
		try
		{
			logger.info("\n consulta --> " + cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));

			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);

		}
		catch (SQLException e)
		{
			logger.info("\n problema en cargarListadoEventosCatastroficos "
							+ e);
		}

		return resultado;
	}

	/**
	 * 
	 * @param connection
	 * @param criterios
	 * @return
	 */
	private static HashMap cargarListadoEventosIngresos(Connection connection,HashMap criterios) 
	{
		String cadena=" SELECT "
					+ " case when vrec.fecha_evento is null then '' else to_char(vrec.fecha_evento,'YYYY/MM/DD') end as fecha_evento0,"
					+ " case when vrec.departamento_evento is null then '' else getnombredepto(vrec.pais_evento,vrec.departamento_evento) end as departamento_evento1,"
					+ " case when vrec.ciudad_evento is null then '' else getnombreciudad(vrec.pais_evento,vrec.departamento_evento,vrec.ciudad_evento) end as ciudad_evento2,"
					+ " vrec.direccion_evento as direccion_evento3,"
					+ " getdescnatevent(vrec.naturaleza_evento) As tipo_evento4, "
					+ " vrec.codigo As codigo5, " +
					" i.id as idingreso,"+
					"  i.consecutivo as consecutivoingreso,"+
					"  to_char(i.fecha_ingreso,'dd/mm/yyyy') as fechaingreso,"+
					"  i.estado as estadoingreso,"+
					"  i.centro_atencion as centroatencion,"+
					"  cc.descripcion as desccentroatencion "
					+ " FROM ingresos i" 
					+ " inner join cuentas c on (c.id_ingreso=i.id and c.tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"')" 
					+ " left outer join view_reg_evento_catastrofico vrec ON (i.id=vrec.ingreso)"
					+ " inner join centro_atencion cc on(cc.consecutivo=i.centro_atencion) " 
					+ " WHERE 1=1 ";
		HashMap resultado= new HashMap();
		try 
		{
			//estado del accidente de transito
			cadena+=" and  (i.estado ='ABI' or (i.estado ='CER'  and vrec.estado= '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"'))";
			cadena+="   AND i.codigo_paciente="+criterios.get("paciente");
			cadena += " order by fecha_evento desc";
			logger.info("\n consulta --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException  e)
		{
		 logger.info("\n problema en cargarListadoAccidentesTransito "+e);
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 */
	public static DtoRegistroEventosCatastroficos consultarRegistroEventoCatastroficoIngreso(Connection con, int codigoIngreso, String codigoEvento)
	{
		DtoRegistroEventosCatastroficos dto = new DtoRegistroEventosCatastroficos();
		try
		{
			String cadena = cadenaConsultaGeneralEventoCatastrofico;

			if (codigoIngreso > ConstantesBD.codigoNuncaValido)
				cadena += " AND ingreso=" + codigoIngreso;

			if (UtilidadCadena.noEsVacio(codigoEvento))
				cadena += " AND codigo=" + codigoEvento;

			logger.info("===>Cadena Consulta General Evento Catastrofico: "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			dto = cargarDTORegistroEventosCatastroficos(codigoIngreso, new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger
					.error("[consultarRegistroEventoCatastroficoIngreso - SqlBaseRegistroEventosCatastroficosDao] ERROR CONSULTANDO EL REGISTRO EVENTO CATASTROFICO LLAVE ");
			e.printStackTrace();
		}
		return dto;
	}

	/**
	 * 
	 * @param set
	 * @return
	 */
	private static DtoRegistroEventosCatastroficos cargarDTORegistroEventosCatastroficos(int ingreso,ResultSetDecorator rs)
	{
		DtoRegistroEventosCatastroficos dto = new DtoRegistroEventosCatastroficos();
		try
		{
			if (rs.next()) {
				dto.setCodigo(rs.getInt("codigo"));
				dto.setIngreso(ingreso);
				dto.setEmpresaTrabaja(rs.getString("empresatrabaja"));
				dto.setCiudadEmpresa(rs.getString("ciudadempresa"));
				dto.setDepartamentoEmpresa(rs.getString("departamentoempresa"));
				dto.setDireccionEvento(rs.getString("direvento"));
				dto.setFechaEvento(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaevento")));
				dto.setCiudadEvento(rs.getString("ciudadevento"));
				dto.setDepartamentoEvento(rs.getString("departamentoevento"));
				dto.setZonaUrbanaEvento(rs.getString("zonaurbanaevento"));
				dto.setNaturalezaEvento(rs.getInt("naturalezaevento"));
				/*
				 * dto.setTipoReferencia(rs.getString("tiporeferencia"));
				 * dto.setPersonaRefiere(rs.getInt("personarefiere"));
				 * dto.setCiudadRefiere(rs.getString("ciudadrefiere"));
				 * dto.setDepartamentoRefiere(rs.getString("deptorefiere"));
				 * dto.
				 * setFechaReferencia(UtilidadFecha.conversionFormatoFechaAAp
				 * (rs.getString("fechareferencia")));
				 * dto.setReferidoA(rs.getInt("referidoa"));
				 * dto.setCiudadReferido(rs.getString("ciudadreferido"));
				 * dto.setDepartamentoReferido(rs.getString("deptoreferido"));
				 * dto
				 * .setFechaReferido(UtilidadFecha.conversionFormatoFechaAAp(rs
				 * .getString("fechareferido")));
				 */
				dto.setApellidoNombreTransporta(rs.getString("apellidonombretransporta"));
				dto.setTipoIdTransporta(rs.getString("tipoidtransporta"));
				dto.setNumeroIdTransporta(rs.getString("numeroidtransporta"));
				dto.setCiudadExpIdTransporta(rs.getString("ciudadexpidtransporta"));
				dto.setDeptoExpIdTransporta(rs.getString("deptoexpidtransporta"));
				dto.setTelefonoTransporta(rs.getString("telefonotransporta"));
				dto.setDireccionTransporta(rs.getString("direcciontransporta"));
				dto.setCiudadTranporta(rs.getString("ciudadtransporta"));
				dto.setDeptoTransporta(rs.getString("deptotransporta"));
				dto.setTransportaDesde(rs.getString("transportadesde"));
				dto.setTransportaHasta(rs.getString("transportahasta"));
				dto.setTipoTransporte(rs.getString("tipotransporte"));
				dto.setPlacaVehiculoTransporta(rs.getString("placavehiculo"));
				dto.setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechamodifica")));
				dto.setHoraModifica(rs.getString("horamodifica"));
				dto.setUsuarioModifica(rs.getString("usuariomodifica"));
				dto.setEstadoRegistro(rs.getString("estado"));
				dto.setPaisEmpresa(rs.getString("paisempresa"));
				dto.setPaisEvento(rs.getString("paisevento"));
				dto.setPaisExpIdTransporta(rs.getString("paisexpidtransporta"));
				dto.setPaisTransporta(rs.getString("paistransporta"));
				dto.setSgsss(rs.getString("sgsss"));
				dto.setTipoRegimen(rs.getString("tiporegimen"));
				dto.setHoraEvento(rs.getString("horaocurrencia"));
				dto.setDescOcurrencia(rs.getString("descocurrencia"));
				dto.setDescOtroEvento(rs.getString("descotroevento"));
				dto.setTipoReferencia(rs.getString("tiporeferencia"));
				dto.setFechaRemision(rs.getString("fecharemision"));
				dto.setHoraRemision(rs.getString("horaremision"));
				dto.setPrestadorRemite(rs.getString("prestadorremite"));
				dto.setProfesionalRemite(rs.getString("profesionalremite"));
				dto.setCargoProfesionalRemite(rs.getString("cargoprofesionalremite"));
				dto.setPrestadorRecibe(rs.getString("prestadorrecibe"));
				dto.setProfesionalRecibe(rs.getString("profesionalrecibe"));
				dto.setCargoProfesionalRecibe(rs.getString("cargoprofesionalrecibe"));
				dto.setFechaAceptacion(rs.getString("fechaaceptacion"));
				dto.setHoraAceptacion(rs.getString("horaaceptacion"));
				dto.setOtroTipoTransporte(rs.getString("otrotipotransporte"));
				dto.setZonaTransporte(rs.getString("zonatransporte"));
				dto.setTotalFacturadoMedicoQuirurgico(rs.getString("totalfacturadomedqx"));
				dto.setTotalReclamadoMedicoQuirurgico(rs.getString("totalreclamadomedqx"));
				dto.setTotalFacturadoTransporte(rs.getString("totalfacturadotrans"));
				dto.setTotalReclamadoTransporte(rs.getString("totalreclamadotrans"));
				dto.setEsReclamacion(rs.getString("esreclamacion"));
				dto.setFurips(rs.getString("furips"));
				dto.setFurpro(rs.getString("furpro"));
				dto.setFurtran(rs.getString("furtran"));
				dto.setRespuestaGlosa(rs.getString("respglosa"));
				dto.setNumeroRadicadoAnterior(rs.getString("numradicadoanterior"));
				dto.setNumeroConsecutivoReclamacion(rs.getString("numconsecutivoreclamacion"));
				dto.setProtesis(rs.getString("protesis"));
				dto.setValorProtesis(rs.getString("valorprotesis"));
				dto.setAdaptacionProtesis(rs.getString("adaptacionprotesis"));
				dto.setValorAdaptacionProtesis(rs.getString("valoradaptacionprotesis"));
				dto.setRehabilitacion(rs.getString("rehabilitacion"));
				dto.setValorRehabilitacion(rs.getString("valorrehabilitacion"));
			}
		}
		catch (SQLException e)
		{
			logger.error("[cargarDTORegistroAccidentesTransito - SqlBaseRegistroAccidentesTransito] ERROR CARGANDO EL DTO ");
			e.printStackTrace();
		}
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @param cadena
	 * @return
	 */
	public static boolean insertarRegistroAccidentesTransito(Connection con,DtoRegistroEventosCatastroficos dto, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsert));
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_reg_even_cat");
			dto.setCodigo(codigo);
			ps.setDouble(1, Utilidades.convertirADouble(codigo + ""));
			String empresa=dto.getEmpresaTrabaja().equals("")?" ":dto.getEmpresaTrabaja();
			ps.setString(2, empresa);

			if (dto.getCiudadEmpresa().trim().equals(""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, dto.getCiudadEmpresa());

			if (dto.getDepartamentoEmpresa().trim().equals(""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, dto.getDepartamentoEmpresa());

			if (dto.getFechaEvento().trim().equals(""))
				ps.setNull(5, Types.DATE);
			else
				ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaEvento())));

			String direccion=dto.getDireccionEvento().equals("")?" ":dto.getDireccionEvento();
			ps.setString(6, direccion);

			if (dto.getCiudadEvento().trim().equals(""))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7, dto.getCiudadEvento());

			if (dto.getDepartamentoEvento().trim().equals(""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, dto.getDepartamentoEvento());

			if (dto.getZonaUrbanaEvento().trim().equals(""))
				ps.setNull(9, Types.CHAR);
			else
				ps.setString(9, dto.getZonaUrbanaEvento());

			if (dto.getNaturalezaEvento() > 0)
				ps.setInt(10, dto.getNaturalezaEvento());
			else
				ps.setNull(10, Types.INTEGER);

			String apellidoNT=dto.getApellidoNombreTransporta().equals("")?" ":dto.getApellidoNombreTransporta();
			ps.setString(11, apellidoNT);

			if (dto.getTipoIdTransporta().trim().equals(""))
				ps.setNull(12, Types.VARCHAR);
			else
				ps.setString(12, dto.getTipoIdTransporta());

			String numeorIDT=dto.getNumeroIdTransporta().equals("")?" ":dto.getNumeroIdTransporta();
			ps.setString(13,numeorIDT);

			if (dto.getCiudadExpIdTransporta().trim().equals(""))
				ps.setNull(14, Types.VARCHAR);
			else
				ps.setString(14, dto.getCiudadExpIdTransporta());

			if (dto.getDeptoExpIdTransporta().trim().equals(""))
				ps.setNull(15, Types.VARCHAR);
			else
				ps.setString(15, dto.getDeptoExpIdTransporta());

			String telTra=dto.getTelefonoTransporta().equals("")?" ":dto.getTelefonoTransporta();
			String dirTra=dto.getDireccionTransporta().equals("")?" ":dto.getDireccionTransporta();
			ps.setString(16, telTra);
			ps.setString(17, dirTra);

			if (dto.getCiudadTranporta().trim().equals(""))
				ps.setNull(18, Types.VARCHAR);
			else
				ps.setString(18, dto.getCiudadTranporta());

			if (dto.getDeptoTransporta().trim().equals(""))
				ps.setNull(19, Types.VARCHAR);
			else
				ps.setString(19, dto.getDeptoTransporta());

			
			String traDes=dto.getTransportaDesde().equals("")?" ":dto.getTransportaDesde();
			String traHas=dto.getTransportaHasta().equals("")?" ":dto.getTransportaHasta();
			String tipoBD= System.getProperty("TIPOBD");
			String tipoTra=" ";
			if (tipoBD.equals("POSTGRESQL"))
			{ tipoTra=dto.getTipoTransporte().equals("")?"":dto.getTipoTransporte();}
			else
			{ tipoTra=dto.getTipoTransporte().equals("")?" ":dto.getTipoTransporte();}
			String vehTra=dto.getPlacaVehiculoTransporta().equals("")?" ":dto.getPlacaVehiculoTransporta();
			ps.setString(20, traDes);
			ps.setString(21, traHas);
			ps.setString(22, tipoTra);
			ps.setString(23, vehTra);
			
			ps.setString(24, dto.getEstadoRegistro());
			ps.setDate(25, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica())));
			ps.setString(26, dto.getHoraModifica());
			ps.setString(27, dto.getUsuarioModifica());

			if (dto.getPaisEmpresa().trim().equals(""))
				ps.setNull(28, Types.VARCHAR);
			else
				ps.setString(28, dto.getPaisEmpresa());

			if (dto.getPaisEvento().trim().equals(""))
				ps.setNull(29, Types.VARCHAR);
			else
				ps.setString(29, dto.getPaisEvento());

			if (dto.getPaisExpIdTransporta().trim().equals(""))
				ps.setNull(30, Types.VARCHAR);
			else
				ps.setString(30, dto.getPaisExpIdTransporta());

			if (dto.getPaisTransporta().trim().equals(""))
				ps.setNull(31, Types.VARCHAR);
			else
				ps.setString(31, dto.getPaisTransporta());

			String sgss=dto.getSgsss().equals("")?" ":dto.getSgsss();
			ps.setString(32, sgss);

			if (!UtilidadCadena.noEsVacio(dto.getTipoRegimen()))
				ps.setNull(33, Types.VARCHAR);
			else
				ps.setString(33, dto.getTipoRegimen());

			String horaO=dto.getHoraEvento().equals("")?" ":dto.getHoraEvento();
			ps.setString(34, horaO);
			ps.setString(35, dto.getDescOcurrencia());

			if (!UtilidadCadena.noEsVacio(dto.getDescOtroEvento()))
				ps.setNull(36, Types.VARCHAR);
			else
				ps.setString(36, dto.getDescOtroEvento());

			if (!UtilidadCadena.noEsVacio(dto.getTipoReferencia()))
				ps.setNull(37, Types.VARCHAR);
			else
				ps.setString(37, dto.getTipoReferencia());

			if (!UtilidadCadena.noEsVacio(dto.getFechaRemision()))
				ps.setNull(38, Types.DATE);
			else
				ps.setDate(38, Date.valueOf(UtilidadFecha
						.conversionFormatoFechaABD(dto.getFechaRemision())));

			if (!UtilidadCadena.noEsVacio(dto.getHoraRemision()))
				ps.setNull(39, Types.VARCHAR);
			else
				ps.setString(39, dto.getHoraRemision());

			ps.setInt(40, institucion);

			if (!UtilidadCadena.noEsVacio(dto.getPrestadorRemite()))
				ps.setNull(41, Types.VARCHAR);
			else
				ps.setString(41, dto.getPrestadorRemite());

			if (!UtilidadCadena.noEsVacio(dto.getProfesionalRemite()))
				ps.setNull(42, Types.VARCHAR);
			else
				ps.setString(42, dto.getProfesionalRemite());

			if (!UtilidadCadena.noEsVacio(dto.getCargoProfesionalRemite()))
				ps.setNull(43, Types.VARCHAR);
			else
				ps.setString(43, dto.getCargoProfesionalRemite());

			if (!UtilidadCadena.noEsVacio(dto.getFechaAceptacion()))
				ps.setNull(44, Types.VARCHAR);
			else
				ps.setDate(44, Date.valueOf(UtilidadFecha
						.conversionFormatoFechaABD(dto.getFechaAceptacion())));

			if (!UtilidadCadena.noEsVacio(dto.getHoraAceptacion()))
				ps.setNull(45, Types.VARCHAR);
			else
				ps.setString(45, dto.getHoraAceptacion());

			if (!UtilidadCadena.noEsVacio(dto.getPrestadorRecibe()))
				ps.setNull(46, Types.VARCHAR);
			else
				ps.setString(46, dto.getPrestadorRecibe());

			if (!UtilidadCadena.noEsVacio(dto.getProfesionalRecibe()))
				ps.setNull(47, Types.VARCHAR);
			else
				ps.setString(47, dto.getProfesionalRecibe());

			if (!UtilidadCadena.noEsVacio(dto.getCargoProfesionalRecibe()))
				ps.setNull(48, Types.VARCHAR);
			else
				ps.setString(48, dto.getCargoProfesionalRecibe());
			
			if (!UtilidadCadena.noEsVacio(dto.getOtroTipoTransporte()))
				ps.setNull(49, Types.VARCHAR);
			else
				ps.setString(49, dto.getOtroTipoTransporte());

			if (!UtilidadCadena.noEsVacio(dto.getZonaTransporte()))
				ps.setNull(50, Types.VARCHAR);
			else
				ps.setString(50, dto.getZonaTransporte());
			
			logger.info("===>Total Facturado Gastos Médicos: "+dto.getTotalFacturadoMedicoQuirurgico());
			if (!UtilidadCadena.noEsVacio(dto.getTotalFacturadoMedicoQuirurgico()))
				ps.setNull(51, Types.NUMERIC);
			else
				ps.setDouble(51, Utilidades.convertirADouble(dto.getTotalFacturadoMedicoQuirurgico()));
			
			logger.info("===>Total Reclamado Gastos Médicos: "+dto.getTotalReclamadoMedicoQuirurgico());
			if (!UtilidadCadena.noEsVacio(dto.getTotalReclamadoMedicoQuirurgico()))
				ps.setNull(52, Types.NUMERIC);
			else
				ps.setDouble(52, Utilidades.convertirADouble(dto.getTotalReclamadoMedicoQuirurgico()));
			
			logger.info("===>Total Facturado Gastos Transporte: "+dto.getTotalFacturadoTransporte());
			if (!UtilidadCadena.noEsVacio(dto.getTotalFacturadoTransporte()))
				ps.setNull(53, Types.NUMERIC);
			else
				ps.setDouble(53, Utilidades.convertirADouble(dto.getTotalFacturadoTransporte()));
			
			logger.info("===>Total Reclamado Gastos Transporte: "+dto.getTotalReclamadoTransporte());
			if (!UtilidadCadena.noEsVacio(dto.getTotalReclamadoTransporte()))
				ps.setNull(54, Types.NUMERIC);
			else
				ps.setDouble(54, Utilidades.convertirADouble(dto.getTotalReclamadoTransporte()));
			
			if (!UtilidadCadena.noEsVacio(dto.getEsReclamacion()))
				ps.setNull(55, Types.CHAR);
			else
				ps.setString(55, dto.getEsReclamacion());
						
			if (!UtilidadCadena.noEsVacio(dto.getFurips()))
				ps.setNull(56, Types.CHAR);
			else
				ps.setString(56, dto.getFurips());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurpro()))
				ps.setNull(57, Types.CHAR);
			else
				ps.setString(57, dto.getFurpro());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurtran()))
				ps.setNull(58, Types.CHAR);
			else
				ps.setString(58, dto.getFurtran());
			
			if (!UtilidadCadena.noEsVacio(dto.getRespuestaGlosa()))
				ps.setNull(59, Types.VARCHAR);
			else
				ps.setString(59, dto.getRespuestaGlosa());
			
			if (!UtilidadCadena.noEsVacio(dto.getNumeroRadicadoAnterior()))
				ps.setNull(60, Types.VARCHAR);
			else
				ps.setString(60, dto.getNumeroRadicadoAnterior());
			
			if (!UtilidadCadena.noEsVacio(dto.getNumeroConsecutivoReclamacion()))
				ps.setNull(61, Types.NUMERIC);
			else
				ps.setDouble(61, Utilidades.convertirADouble(dto.getNumeroConsecutivoReclamacion()));
			
			if (!UtilidadCadena.noEsVacio(dto.getProtesis()))
				ps.setNull(62, Types.VARCHAR);
			else
				ps.setString(62, dto.getProtesis());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorProtesis()))
				ps.setNull(63, Types.NUMERIC);
			else
				ps.setDouble(63, Utilidades.convertirADouble(dto.getValorProtesis()));
			
			if (!UtilidadCadena.noEsVacio(dto.getAdaptacionProtesis()))
				ps.setNull(64, Types.VARCHAR);
			else
				ps.setString(64, dto.getAdaptacionProtesis());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorAdaptacionProtesis()))
				ps.setNull(65, Types.NUMERIC);
			else
				ps.setDouble(65, Utilidades.convertirADouble(dto.getValorAdaptacionProtesis()));
			
			if (!UtilidadCadena.noEsVacio(dto.getRehabilitacion()))
				ps.setNull(66, Types.VARCHAR);
			else
				ps.setString(66, dto.getRehabilitacion());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorRehabilitacion()))
				ps.setNull(67, Types.NUMERIC);
			else
				ps.setDouble(67, Utilidades.convertirADouble(dto.getValorRehabilitacion()));
			
			logger.info("PAIS TRANSPORTA-->> " + dto.getPaisTransporta());
			if (ps.executeUpdate() > 0)
			{ 
				ps =  new PreparedStatementDecorator(con.prepareStatement("INSERT INTO ingresos_reg_even_catastrofico(ingreso,codigo_registro) values (?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(dto.getIngreso()+""));
				ps.setDouble(2, Utilidades.convertirADouble(codigo + ""));
				int io=0;
				return ps.executeUpdate() > 0;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarRegistroAccidentesTransito(Connection con,DtoRegistroEventosCatastroficos dto, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion));

			String empresa=dto.getEmpresaTrabaja().equals("")?" ":dto.getEmpresaTrabaja();
			ps.setString(1, empresa);
			
			if (dto.getCiudadEmpresa().trim().equals(""))
				ps.setNull(2, Types.VARCHAR);
			else
				ps.setString(2, dto.getCiudadEmpresa());

			if (dto.getDepartamentoEmpresa().trim().equals(""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, dto.getDepartamentoEmpresa());

			if (dto.getFechaEvento().trim().equals(""))
				ps.setNull(4, Types.DATE);
			else
				ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaEvento())));

			String direccion=dto.getDireccionEvento().equals("")?" ":dto.getDireccionEvento();
			ps.setString(5, direccion);

			if (dto.getCiudadEvento().trim().equals(""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, dto.getCiudadEvento());

			if (dto.getDepartamentoEvento().trim().equals(""))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7, dto.getDepartamentoEvento());

			if (dto.getZonaUrbanaEvento().trim().equals(""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, dto.getZonaUrbanaEvento());

			if (dto.getNaturalezaEvento() > 0)
				ps.setInt(9, dto.getNaturalezaEvento());
			else
				ps.setNull(9, Types.INTEGER);

			String apellidoNT=dto.getApellidoNombreTransporta().equals("")?" ":dto.getApellidoNombreTransporta();
			ps.setString(10, apellidoNT);
			
			if (dto.getTipoIdTransporta().trim().equals(""))
				ps.setNull(11, Types.VARCHAR);
			else
				ps.setString(11, dto.getTipoIdTransporta());

			String numeorIDT=dto.getNumeroIdTransporta().equals("")?" ":dto.getNumeroIdTransporta();
			ps.setString(12,numeorIDT);


			if (dto.getCiudadExpIdTransporta().trim().equals(""))
				ps.setNull(13, Types.VARCHAR);
			else
				ps.setString(13, dto.getCiudadExpIdTransporta());

			if (dto.getDeptoExpIdTransporta().trim().equals(""))
				ps.setNull(14, Types.VARCHAR);
			else
				ps.setString(14, dto.getDeptoExpIdTransporta());
			
			
			String telTra=dto.getTelefonoTransporta().equals("")?" ":dto.getTelefonoTransporta();
			String dirTra=dto.getDireccionTransporta().equals("")?" ":dto.getDireccionTransporta();
			ps.setString(15, telTra);
			ps.setString(16, dirTra);
			
			
			if (dto.getCiudadTranporta().trim().equals(""))
				ps.setNull(17, Types.VARCHAR);
			else
				ps.setString(17, dto.getCiudadTranporta());

			if (dto.getDeptoTransporta().trim().equals(""))
				ps.setNull(18, Types.VARCHAR);
			else
				ps.setString(18, dto.getDeptoTransporta());
			
			String traDes=dto.getTransportaDesde().equals("")?" ":dto.getTransportaDesde();
			String traHas=dto.getTransportaHasta().equals("")?" ":dto.getTransportaHasta();
			String tipoTra=dto.getTipoTransporte().equals("")?" ":dto.getTipoTransporte();
			String vehTra=dto.getPlacaVehiculoTransporta().equals("")?" ":dto.getPlacaVehiculoTransporta();
			ps.setString(19, traDes);
			ps.setString(20, traHas);
			ps.setString(21, tipoTra);
			ps.setString(22, vehTra);
			
			ps.setString(23, dto.getEstadoRegistro());
			ps.setDate(24, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica())));
			ps.setString(25, dto.getHoraModifica());
			ps.setString(26, dto.getUsuarioModifica());

			if (dto.getPaisEmpresa().trim().equals(""))
				ps.setNull(27, Types.VARCHAR);
			else
				ps.setString(27, dto.getPaisEmpresa());

			if (dto.getPaisEvento().trim().equals(""))
				ps.setNull(28, Types.VARCHAR);
			else
				ps.setString(28, dto.getPaisEvento());

			if (dto.getPaisExpIdTransporta().trim().equals(""))
				ps.setNull(29, Types.VARCHAR);
			else
				ps.setString(29, dto.getPaisExpIdTransporta());

			if (dto.getPaisTransporta().trim().equals(""))
				ps.setNull(30, Types.VARCHAR);
			else
				ps.setString(30, dto.getPaisTransporta());

			String sgss=dto.getSgsss().equals("")?" ":dto.getSgsss();
			ps.setString(31, sgss);

			if (!UtilidadCadena.noEsVacio(dto.getTipoRegimen()))
				ps.setNull(32, Types.VARCHAR);
			else
				ps.setString(32, dto.getTipoRegimen());

			String horaO=dto.getHoraEvento().equals("")?" ":dto.getHoraEvento();
			ps.setString(33, horaO);
			ps.setString(34, dto.getDescOcurrencia());

			if (!UtilidadCadena.noEsVacio(dto.getDescOtroEvento()))
				ps.setNull(35, Types.VARCHAR);
			else
				ps.setString(35, dto.getDescOtroEvento());

			if (!UtilidadCadena.noEsVacio(dto.getTipoReferencia()))
				ps.setNull(36, Types.VARCHAR);
			else
				ps.setString(36, dto.getTipoReferencia());

			if (!UtilidadCadena.noEsVacio(dto.getFechaRemision()))
				ps.setNull(37, Types.DATE);
			else
				ps.setDate(37, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaRemision())));

			if (!UtilidadCadena.noEsVacio(dto.getHoraRemision()))
				ps.setNull(38, Types.VARCHAR);
			else
				ps.setString(38, dto.getHoraRemision());

			ps.setInt(39, institucion);

			if (!UtilidadCadena.noEsVacio(dto.getPrestadorRemite()))
				ps.setNull(40, Types.VARCHAR);
			else
				ps.setString(40, dto.getPrestadorRemite());

			if (!UtilidadCadena.noEsVacio(dto.getProfesionalRemite()))
				ps.setNull(41, Types.VARCHAR);
			else
				ps.setString(41, dto.getProfesionalRemite());

			if (!UtilidadCadena.noEsVacio(dto.getCargoProfesionalRemite()))
				ps.setNull(42, Types.VARCHAR);
			else
				ps.setString(42, dto.getCargoProfesionalRemite());

			if (!UtilidadCadena.noEsVacio(dto.getFechaAceptacion()))
				ps.setNull(43, Types.VARCHAR);
			else
				ps.setDate(43, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaAceptacion())));

			if (!UtilidadCadena.noEsVacio(dto.getHoraAceptacion()))
				ps.setNull(44, Types.VARCHAR);
			else
				ps.setString(44, dto.getHoraAceptacion());

			if (!UtilidadCadena.noEsVacio(dto.getPrestadorRecibe()))
				ps.setNull(45, Types.VARCHAR);
			else
				ps.setString(45, dto.getPrestadorRecibe());

			if (!UtilidadCadena.noEsVacio(dto.getProfesionalRecibe()))
				ps.setNull(46, Types.VARCHAR);
			else
				ps.setString(46, dto.getProfesionalRecibe());

			if (!UtilidadCadena.noEsVacio(dto.getCargoProfesionalRecibe()))
				ps.setNull(47, Types.VARCHAR);
			else
				ps.setString(47, dto.getCargoProfesionalRecibe());

			if (!UtilidadCadena.noEsVacio(dto.getOtroTipoTransporte()))
				ps.setNull(48, Types.VARCHAR);
			else
				ps.setString(48, dto.getOtroTipoTransporte());
			
			logger.info("===>Zona Transporte: "+dto.getZonaTransporte());
			if (!UtilidadCadena.noEsVacio(dto.getZonaTransporte()))
				ps.setNull(49, Types.VARCHAR);
			else
				ps.setString(49, dto.getZonaTransporte());
			
			logger.info("===>Total Facturado Gastos Médicos: "+dto.getTotalFacturadoMedicoQuirurgico());
			if (!UtilidadCadena.noEsVacio(dto.getTotalFacturadoMedicoQuirurgico()))
				ps.setNull(50, Types.NUMERIC);
			else
				ps.setDouble(50, Utilidades.convertirADouble(dto.getTotalFacturadoMedicoQuirurgico()));
			
			logger.info("===>Total Reclamado Gastos Médicos: "+dto.getTotalReclamadoMedicoQuirurgico());
			if (!UtilidadCadena.noEsVacio(dto.getTotalReclamadoMedicoQuirurgico()))
				ps.setNull(51, Types.NUMERIC);
			else
				ps.setDouble(51, Utilidades.convertirADouble(dto.getTotalReclamadoMedicoQuirurgico()));
			
			logger.info("===>Total Facturado Gastos Transporte: "+dto.getTotalFacturadoTransporte());
			if (!UtilidadCadena.noEsVacio(dto.getTotalFacturadoTransporte()))
				ps.setNull(52, Types.NUMERIC);
			else
				ps.setDouble(52, Utilidades.convertirADouble(dto.getTotalFacturadoTransporte()));
			
			logger.info("===>Total Reclamado Gastos Transporte: "+dto.getTotalReclamadoTransporte());
			if (!UtilidadCadena.noEsVacio(dto.getTotalReclamadoTransporte()))
				ps.setNull(53, Types.NUMERIC);
			else
				ps.setDouble(53, Utilidades.convertirADouble(dto.getTotalReclamadoTransporte()));

			if (!UtilidadCadena.noEsVacio(dto.getEsReclamacion()))
				ps.setNull(54, Types.CHAR);
			else
				ps.setString(54, dto.getEsReclamacion());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurips()))
				ps.setNull(55, Types.CHAR);
			else
				ps.setString(55, dto.getFurips());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurpro()))
				ps.setNull(56, Types.CHAR);
			else
				ps.setString(56, dto.getFurpro());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurtran()))
				ps.setNull(57, Types.CHAR);
			else
				ps.setString(57, dto.getFurtran());
			
			if (!UtilidadCadena.noEsVacio(dto.getRespuestaGlosa()))
				ps.setNull(58, Types.VARCHAR);
			else
				ps.setString(58, dto.getRespuestaGlosa());
			
			if (!UtilidadCadena.noEsVacio(dto.getNumeroRadicadoAnterior()))
				ps.setNull(59, Types.VARCHAR);
			else
				ps.setString(59, dto.getNumeroRadicadoAnterior());
			
			if (!UtilidadCadena.noEsVacio(dto.getNumeroConsecutivoReclamacion()))
				ps.setNull(60, Types.NUMERIC);
			else
				ps.setDouble(60, Utilidades.convertirADouble(dto.getNumeroConsecutivoReclamacion()));
			
			if (!UtilidadCadena.noEsVacio(dto.getProtesis()))
				ps.setNull(61, Types.VARCHAR);
			else
				ps.setString(61, dto.getProtesis());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorProtesis()))
				ps.setNull(62, Types.NUMERIC);
			else
				ps.setDouble(62, Utilidades.convertirADouble(dto.getValorProtesis()));
			
			if (!UtilidadCadena.noEsVacio(dto.getAdaptacionProtesis()))
				ps.setNull(63, Types.VARCHAR);
			else
				ps.setString(63, dto.getAdaptacionProtesis());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorAdaptacionProtesis()))
				ps.setNull(64, Types.NUMERIC);
			else
				ps.setDouble(64, Utilidades.convertirADouble(dto.getValorAdaptacionProtesis()));
			
			if (!UtilidadCadena.noEsVacio(dto.getRehabilitacion()))
				ps.setNull(65, Types.VARCHAR);
			else
				ps.setString(65, dto.getRehabilitacion());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorRehabilitacion()))
				ps.setNull(66, Types.NUMERIC);
			else
				ps.setDouble(66, Utilidades.convertirADouble(dto.getValorRehabilitacion()));
			
			ps.setDouble(67, Utilidades.convertirADouble(dto.getCodigo() + ""));

			return ps.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Método implementado para realizar la actualizacion del estado de un
	 * evento catastrofico
	 * 
	 * @param con
	 * @param dtoCatastrofico
	 * @return
	 */
	public static int actualizarEstadoRegistroEventoCatastrofico(Connection con, DtoRegistroEventosCatastroficos dtoCatastrofico)
	{
		PreparedStatementDecorator pst = null;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement("SELECT codigo_registro FROM ingresos_reg_even_catastrofico WHERE ingreso=?"));
			pst.setObject(1, dtoCatastrofico.getIngreso());
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if (rs.next()) {
				String codigo = rs.getObject(1) + "";
				pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoRegistroEventoCatastroficoStr));
				pst.setString(1, dtoCatastrofico.getEstadoRegistro());
				pst.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoCatastrofico.getFechaModifica())));
				pst.setString(3, dtoCatastrofico.getHoraModifica());
				pst.setString(4, dtoCatastrofico.getUsuarioModifica());
				pst.setDouble(5, Utilidades.convertirADouble(codigo));

				return pst.executeUpdate();
			}
		} catch (SQLException e) {
			logger.error("Error en actualizarEstadoRegistroEventoCatastrofico :"+ e);
		}
		return 0;
	}

	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion de Amparos
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public static boolean actualizarAmparos(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		String cadena = "UPDATE manejopaciente.registro_evento_catastrofico " +
								"SET total_fac_amp_qx = ?, " +
									"total_reclamo_amp_qx = ?, " +
									"total_fac_amp_tx = ?, " +
									"total_reclamo_amp_tx = ?, " +
									"fecha_modifica = ?," +
									"hora_modifica = ?," +
									"usuario_modifica = ? " +
								"WHERE " +
									"codigo = ?"; 
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1, Utilidades.convertirADouble(dto.getTotalFacturadoMedicoQuirurgico()));
			pst.setDouble(2, Utilidades.convertirADouble(dto.getTotalReclamadoMedicoQuirurgico()));
			pst.setDouble(3, Utilidades.convertirADouble(dto.getTotalFacturadoTransporte()));
			pst.setDouble(4, Utilidades.convertirADouble(dto.getTotalReclamadoTransporte()));
			pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica())));
			pst.setString(6, dto.getHoraModifica());
			pst.setString(7, dto.getUsuarioModifica());
			pst.setDouble(8, Utilidades.convertirADouble(dto.getCodigo()+""));
			
			if (pst.executeUpdate() > 0)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			logger.error("Error en actualizarAmparos:"+ e);
		}
		return false;
	}

	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion de Amparos
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public static boolean actualizarDatosReclamacion(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		String cadena = "UPDATE manejopaciente.registro_evento_catastrofico " +
								"SET es_reclamacion = ?, " +
									"furips = ?, " +
									"furpro = ?, " +
									"furtran = ?, " +
									"resp_glosa = ?, " +
									"nro_radicado_anterior = ?, " +
									"nro_cons_reclamacion = ?, " +
									"fecha_modifica = ?, " +
									"hora_modifica = ?, " +
									"usuario_modifica = ? " +
								"WHERE " +
									"codigo = ?"; 
		try
		{
			logger.info("===>Es Reclamacion: "+dto.getEsReclamacion());
			logger.info("===>FURIPS: "+dto.getFurips());
			logger.info("===>FURPRO: "+dto.getFurpro());
			logger.info("===>FURTRAN: "+dto.getFurtran());
			logger.info("===>Respuesta Glosa: "+dto.getRespuestaGlosa());
			logger.info("===>Numero Radicado Anterior: "+dto.getNumeroRadicadoAnterior());
			logger.info("===>Numero Consecutivo Reclamacion: "+dto.getNumeroConsecutivoReclamacion());
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setString(1, dto.getEsReclamacion());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurips()))
				pst.setNull(2, Types.VARCHAR);
			else
				pst.setString(2, dto.getFurips());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurpro()))
				pst.setNull(3, Types.VARCHAR);
			else
				pst.setString(3, dto.getFurpro());
			
			if (!UtilidadCadena.noEsVacio(dto.getFurtran()))
				pst.setNull(4, Types.VARCHAR);
			else
				pst.setString(4, dto.getFurtran());
			
			if (!UtilidadCadena.noEsVacio(dto.getRespuestaGlosa()))
				pst.setNull(5, Types.VARCHAR);
			else
				pst.setString(5, dto.getRespuestaGlosa());
			
			if (!UtilidadCadena.noEsVacio(dto.getNumeroRadicadoAnterior()))
				pst.setNull(6, Types.VARCHAR);
			else
				pst.setString(6, dto.getNumeroRadicadoAnterior());
			
			if (!UtilidadCadena.noEsVacio(dto.getNumeroConsecutivoReclamacion()))
				pst.setNull(7, Types.NUMERIC);
			else
				pst.setDouble(7, Utilidades.convertirADouble(dto.getNumeroConsecutivoReclamacion()));
			
			pst.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica())));
			pst.setString(9, dto.getHoraModifica());
			pst.setString(10, dto.getUsuarioModifica());
			pst.setDouble(11, Utilidades.convertirADouble(dto.getCodigo()+""));
			
			if (pst.executeUpdate() > 0)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			logger.error("Error en actualizarAmparos:"+ e);
		}
		return false;
	}
	
	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion de Amparos
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public static boolean actualizarServiciosReclamados(Connection con, DtoRegistroEventosCatastroficos dto)
	{
		/**
		 * Cadena que los servicios Reclamados 
		 */
		String cadena = 
			"UPDATE registro_evento_catastrofico SET "
				+ "protesis = ?, "
				+ "valor_protesis = ?, "
				+ "adaptacion_protesis = ?, "
				+ "valor_adaptacion_protesis = ?, " 
				+ "rehabilitacion = ?, "
				+ "valor_rehabilitacion = ? " 
			+ "WHERE codigo = ?";
		
		try
		{
			logger.info("===>Codigo: "+dto.getCodigo());
			logger.info("===>Protesis: "+dto.getProtesis());
			logger.info("===>Valor Protesis: "+dto.getValorProtesis());
			logger.info("===>Adaptacion Protesis: "+dto.getAdaptacionProtesis());
			logger.info("===>Vlr Adaptacion Protesis: "+dto.getValorAdaptacionProtesis());
			logger.info("===>Rehabilitacion: "+dto.getRehabilitacion());
			logger.info("===>Vlr Rehabilitacion: "+dto.getValorAdaptacionProtesis());
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if (!UtilidadCadena.noEsVacio(dto.getProtesis()))
				pst.setNull(1, Types.VARCHAR);
			else
				pst.setString(1, dto.getProtesis());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorProtesis()))
				pst.setNull(2, Types.NUMERIC);
			else
				pst.setDouble(2, Utilidades.convertirADouble(dto.getValorProtesis()));
			
			if (!UtilidadCadena.noEsVacio(dto.getAdaptacionProtesis()))
				pst.setNull(3, Types.VARCHAR);
			else
				pst.setString(3, dto.getAdaptacionProtesis());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorAdaptacionProtesis()))
				pst.setNull(4, Types.NUMERIC);
			else
				pst.setDouble(4, Utilidades.convertirADouble(dto.getValorAdaptacionProtesis()));
			
			if (!UtilidadCadena.noEsVacio(dto.getRehabilitacion()))
				pst.setNull(5, Types.VARCHAR);
			else
				pst.setString(5, dto.getRehabilitacion());
			
			if (!UtilidadCadena.noEsVacio(dto.getValorRehabilitacion()))
				pst.setNull(6, Types.NUMERIC);
			else
				pst.setDouble(6, Utilidades.convertirADouble(dto.getValorRehabilitacion()));
			
			pst.setDouble(7, Utilidades.convertirADouble(dto.getCodigo()+""));
			
			if (pst.executeUpdate() > 0)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			logger.error("Error en actualizarServicioReclamos:"+ e);
		}
		return false;
	}
}