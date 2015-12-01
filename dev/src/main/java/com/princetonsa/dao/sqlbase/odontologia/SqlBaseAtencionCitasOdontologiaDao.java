/*
 * Nov 05, 2009
 */
package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;

/**
 * 
 * Clase que maneja la implementación de métodos genéricos en BD
 * 
 * @author Sebastián Gómez R.
 *
 */
public class SqlBaseAtencionCitasOdontologiaDao 
{
	/**
	 * Atributo para el manejo del log catalina out
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAtencionCitasOdontologiaDao.class);
	
	/**
	 * Cadena para consultar las citas odontologicas
	 */
	private static final String consultarCitasStr = "SELECT "+ 
		"ao.codigo_pk as codigo_agenda, "+
		"co.codigo_pk as codigo_cita, "+
		"co.fecha_modifica as fecha_modifica, "+
		"co.hora_modifica as hora_modifica, "+
		"ao.unidad_agenda as codigo_unidad_agenda, " +
		"coalesce(ao.codigo_medico,"+ConstantesBD.codigoNuncaValido+") as codigo_medico, "+
		"uc.descripcion as nombre_unidad_agenda, "+
		"uc.descripcion as nombre_unidad_agenda, " +
		"uc.especialidad as codigo_especialidad, " +
		"administracion.getnombreespecialidad(uc.especialidad) as nombre_especialidad, "+                                                          
		"to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"co.hora_inicio as hora_inicio, "+
		"co.hora_fin as hora_fin, "+
		"co.codigo_paciente as codigo_paciente, "+
		"administracion.getnombrepersona(co.codigo_paciente) as nombre_paciente, " +
		"co.estado as estado, " +
		"co.por_confirmar as por_confirmar," +
		"co.tipo as tipo_cita, " +
		"co.agenda as codigo_agenda, "+ 
		"co.duracion as duracion, "+
		"co.agenda as codigo_agenda, "+
		"coalesce(to_char(co.fecha_reserva,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_reserva, "+
		"co.hora_reserva as hora_reserva, "+
		"co.usuario_reserva as usuario_reserva, "+
		"coalesce(co.motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") as motivo_cancelacion, "+
		"co.usuario_modifica as usuario_modifica, "+
		"coalesce(co.centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, "+
		"coalesce(to_char(co.fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_programacion," +
		"coalesce(co.usuario_registra,'') as usuario_registra, " +
		"coalesce(to_char(co.fecha_registra,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_registra, " +
		"coalesce(co.hora_registra,'') as hora_registra, " +
		"coalesce(co.usuario_confirma,'') as usuario_confirma, " +
		"coalesce(to_char(co.fecha_confirma,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_confirma, " +
		"coalesce(co.hora_confirma,'') as hora_confirma "+
		"FROM odontologia.agenda_odontologica ao "+ 
		"INNER JOIN odontologia.citas_odontologicas co ON(co.agenda = ao.codigo_pk) "+ 
		"INNER JOIN consultaexterna.unidades_consulta uc on(uc.codigo = ao.unidad_agenda) "+
		"WHERE ";
	
	/**
	 * Cadena para consultar las citas odontologicas
	 */
	private static final String consultarCitasLeftOterStr = "SELECT "+ 
		"coalesce(ao.codigo_pk, -1) as codigo_agenda, "+
		"co.codigo_pk as codigo_cita, "+
		"coalesce(ao.unidad_agenda,-1) as codigo_unidad_agenda, " +
		"coalesce(ao.codigo_medico,"+ConstantesBD.codigoNuncaValido+") as codigo_medico, "+
		"uc.descripcion as nombre_unidad_agenda, "+
		"uc.descripcion as nombre_unidad_agenda, " +
		"uc.especialidad as codigo_especialidad, " +
		"administracion.getnombreespecialidad(uc.especialidad) as nombre_especialidad, "+                                                          
		"to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
		"co.hora_inicio as hora_inicio, "+
		"co.hora_fin as hora_fin, "+
		"co.codigo_paciente as codigo_paciente, "+
		"administracion.getnombrepersona(co.codigo_paciente) as nombre_paciente, " +
		"co.estado as estado, " +
		"co.por_confirmar as por_confirmar," +
		"co.tipo as tipo_cita, " +
		"co.agenda as codigo_agenda, "+ 
		"co.duracion as duracion, "+
		"co.agenda as codigo_agenda, "+
		"coalesce(to_char(co.fecha_reserva,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_reserva, "+
		"co.hora_reserva as hora_reserva, "+
		"co.usuario_reserva as usuario_reserva, "+
		"coalesce(co.motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") as motivo_cancelacion, "+
		"co.usuario_modifica as usuario_modifica, "+
		"coalesce(co.centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, "+
		"coalesce(to_char(co.fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_programacion," +
		"coalesce(co.usuario_registra,'') as usuario_registra, " +
		"coalesce(to_char(co.fecha_registra,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_registra, " +
		"coalesce(co.hora_registra,'') as hora_registra, " +
		"coalesce(co.usuario_confirma,'') as usuario_confirma, " +
		"coalesce(to_char(co.fecha_confirma,'"+ConstantesBD.formatoFechaBD+"'),'') as fecha_confirma, " +
		"coalesce(co.hora_confirma,'') as hora_confirma "+
		"FROM odontologia.citas_odontologicas co "+ 
		"LEFT OUTER JOIN odontologia.agenda_odontologica ao ON(co.agenda = ao.codigo_pk) "+ 
		"LEFT OUTER JOIN consultaexterna.unidades_consulta uc on(uc.codigo = ao.unidad_agenda) "+
		"WHERE ";
	
	/**
	 * Cadena para consultar los servicios de la cita
	 */
	private static final String consultarServiciosCitaStr = "SELECT "+ 
		"sco.codigo_pk as codigo_servicio_cita, "+
		"sco.servicio as codigo_servicio, "+ 
		"facturacion.getnombreservicio(sco.servicio,?) as nombre_servicio, "+
		"facturacion.getcodigopropservicio2(sco.servicio,?) as cod_cups_servicio, "+ 
		"s.tipo_servicio as tipo_servicio," +
		"historiaclinica.getnombretiposervicio(s.tipo_servicio) as nom_tipo_servicio, "+
		"sco.numero_solicitud as numero_solicitud," +
		"sol.estado_historia_clinica as estado_historia_clinica, " +
		"coalesce(sco.programa_hallazgo_pieza,"+ConstantesBD.codigoNuncaValido+") as programa_hallazgo_pieza, "+
		"php.programa as programa," +
		"php.codigo_pk as codigophp," +
		"php.plan_tratamiento as plantratamiento " +
		"FROM odontologia.servicios_cita_odontologica sco " +
		"INNER JOIN facturacion.servicios s ON(s.codigo = sco.servicio) " +
		"INNER JOIN ordenes.solicitudes sol ON(sol.numero_solicitud = sco.numero_solicitud) "+ 
		"left outer join odontologia.programas_hallazgo_pieza php on(sco.programa_hallazgo_pieza=php.codigo_pk) "+ 
		"WHERE "+ 
		"sco.cita_odontologica = ? AND sco.activo = '"+ConstantesBD.acronimoSi+"' and sol.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" "+ 
		"ORDER BY tipo_servicio,nombre_servicio";
	
	
	/**
	 * Método implementado para cargar las plantillas que se han registrado para la cita
	 */
	private static final String cargarPlantillasRegistradasStr =  "SELECT "+ 
		"vo.codigo_pk as codigo, "+
		"pi.codigo_pk as codigo_plantilla_his, "+
		"pi.plantilla as codigo_plantilla, "+
		"'"+ConstantesBD.acronimoSi+"' as valoracion," +
		"p.nombre_plantilla as nombre_plantilla "+ 
		"FROM odontologia.valoraciones_odonto vo "+ 
		"INNER JOIN historiaclinica.plantillas_ingresos pi on (pi.valoracion_odonto = vo.codigo_pk) " +
		"INNER JOIN historiaclinica.plantillas p on (p.codigo_pk = pi.plantilla) "+ 
		"WHERE "+ 
		"vo.cita = ? "+ 
		"UNION "+ 
		"SELECT "+ 
		"eo.codigo_pk as codigo, "+
		"pe.codigo_pk as codigo_plantilla_his, "+
		"pe.plantilla as codigo_plantilla, "+
		"'"+ConstantesBD.acronimoNo+"' as valoracion, "+
		"p.nombre_plantilla as nombre_plantilla "+
		"FROM odontologia.evoluciones_odo eo "+ 
		"INNER JOIN historiaclinica.plantillas_evolucion pe on(pe.evolucion_odonto = eo.codigo_pk) " +
		"INNER JOIN historiaclinica.plantillas p on (p.codigo_pk = pe.plantilla) "+ 
		"WHERE "+ 
		"eo.cita = ?"; 
	
	/**
	 * Cadena para consultar las plantillas por ingreso de un paciente
	 */
	private static final String cargarPlantillasPorIngresoStr= "SELECT " +
			"vo.codigo_pk as codigo, pi.codigo_pk as codigo_plantilla_his, " +
			"pi.plantilla as codigo_plantilla,vo.fecha_modifica as fecha, " +
			"vo.hora_modifica as hora, "+
			"fp.nombre as tipo,p.nombre_plantilla as nombre_plantilla, " +
			"per.primer_nombre as primer_nombre_profesional, per.segundo_nombre as segundo_nombre_profesional, "+
			"per.primer_apellido as primer_apellido_profesional, per.segundo_apellido as segundo_apellido_profesional, "+
			"esp.nombre as nombre_especialidad, "+
			"vo.cita as codigo_pk_cita "+
			"FROM odontologia.valoraciones_odonto vo " +
			"INNER JOIN historiaclinica.plantillas_ingresos pi on (pi.valoracion_odonto = vo.codigo_pk) " +
			"INNER JOIN historiaclinica.plantillas p on (p.codigo_pk = pi.plantilla) " +
			"INNER JOIN administracion.fun_param fp on (p.fun_param = fp.codigo) " +
			"INNER JOIN manejopaciente.ingresos ing on (pi.ingreso = ing.id) " +
			"INNER JOIN administracion.usuarios usu on (pi.usuario_modifica = usu.login) "+
			"INNER JOIN administracion.personas per on (usu.codigo_persona = per.codigo) "+
			"INNER JOIN administracion.medicos med on (med.codigo_medico = per.codigo) "+
			"INNER JOIN administracion.especialidades_medicos em on (em.codigo_medico = med.codigo_medico) "+
			"INNER JOIN administracion.especialidades esp on (esp.codigo = em.codigo_especialidad) "+
			"WHERE pi.codigo_paciente = ? " +
			"AND ing.consecutivo = ? " +
			"UNION " +
			"SELECT eo.codigo_pk as codigo, pe.codigo_pk as codigo_plantilla_his, " +
			"pe.plantilla as codigo_plantilla,eo.fecha_modifica as fecha, " +
			"eo.hora_modifica as hora, "+
			"fp.nombre as tipo,p.nombre_plantilla as nombre_plantilla, " +
			"per.primer_nombre as primer_nombre_profesional, per.segundo_nombre as segundo_nombre_profesional, "+ 
			"per.primer_apellido as primer_apellido_profesional, per.segundo_apellido as segundo_apellido_profesional, "+
			"esp.nombre as nombre_especialidad, "+
			"co.codigo_pk as codigo_pk_cita "+
			"FROM odontologia.evoluciones_odo eo " +
			"INNER JOIN historiaclinica.plantillas_evolucion pe on(pe.evolucion_odonto = eo.codigo_pk) " +
			"INNER JOIN historiaclinica.plantillas p on (p.codigo_pk = pe.plantilla) " +
			"INNER JOIN odontologia.citas_odontologicas co on (co.codigo_pk = eo.cita) " +
			"INNER JOIN administracion.fun_param fp on (p.fun_param = fp.codigo) " +
			"INNER JOIN manejopaciente.ingresos ing on (co.codigo_paciente = ing.codigo_paciente) " +
			"INNER JOIN administracion.usuarios usu on (eo.usuario_modifica = usu.login) "+
			"INNER JOIN administracion.personas per on (usu.codigo_persona = per.codigo) "+
			"INNER JOIN administracion.medicos med on (med.codigo_medico = per.codigo) "+
			"INNER JOIN administracion.especialidades_medicos em on (em.codigo_medico = med.codigo_medico) "+
			"INNER JOIN administracion.especialidades esp on (esp.codigo = em.codigo_especialidad) "+
			"WHERE co.codigo_paciente = ? " +
			"AND ing.consecutivo=? " +
			"ORDER BY fecha DESC, hora DESC, nombre_especialidad";
		
	/**
	 * Cadena para consultar la información de una plantilla por su codigo pk
	 * y codigo de cita
	 */
	private static final String cargarPlantillaPorCodigoPk =  "SELECT " +
			"vo.codigo_pk as codigo, pi.codigo_pk as codigo_plantilla_his, " +
			"fp.nombre as tipo, "+
			"pi.plantilla as codigo_plantilla, '"+ConstantesBD.acronimoSi+"' as valoracion,p.nombre_plantilla as nombre_plantilla " +
			"FROM odontologia.valoraciones_odonto vo " +
			"INNER JOIN historiaclinica.plantillas_ingresos pi on (pi.valoracion_odonto = vo.codigo_pk) " +
			"INNER JOIN historiaclinica.plantillas p on (p.codigo_pk = pi.plantilla) " +
			"INNER JOIN administracion.fun_param fp on (p.fun_param = fp.codigo) " +
			"WHERE p.codigo_pk = ? " +
			"and vo.cita= ? " +
			"UNION " +
			"SELECT eo.codigo_pk as codigo, pe.codigo_pk as codigo_plantilla_his, " +
			"fp.nombre as tipo, "+
			"pe.plantilla as codigo_plantilla,'"+ConstantesBD.acronimoNo+"' as valoracion, p.nombre_plantilla as nombre_plantilla " +
			"FROM odontologia.evoluciones_odo eo " +
			"INNER JOIN historiaclinica.plantillas_evolucion pe on(pe.evolucion_odonto = eo.codigo_pk) " +
			"INNER JOIN historiaclinica.plantillas p on (p.codigo_pk = pe.plantilla) " +
			"INNER JOIN administracion.fun_param fp on (p.fun_param = fp.codigo) " +
			"WHERE p.codigo_pk = ? " +
			"and eo.cita= ? ";
	
	/**
	 * Cadena para cargar los datos del paciente
	 */
	private static final String cargarDatosPacienteStr = "SELECT "+ 
		"administracion.getcentroatencioncc(c.area) as centro_atencion, "+
		"to_char(c.fecha_apertura,'"+ConstantesBD.formatoFechaAp+"') as fecha_ingreso, "+
		"manejopaciente.getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, " +
		"p.primer_nombre as primer_nombre, "+
		"coalesce(' '||p.segundo_nombre,'') as segundo_nombre, "+
		"p.primer_apellido as primer_apellido, "+
		"coalesce(' '||p.segundo_apellido,'') as segundo_apellido, "+
		"p.tipo_identificacion as tipo_identificacion, "+
		"p.numero_identificacion as numero_identificacion, " +
		"to_char(p.fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"') as fecha_nacimiento, " +
		"administracion.getdescripcionsexo(p.sexo) as nombre_sexo, "+ 
		"p.direccion as direccion, " +
		"p.telefono_fijo as telefono_fijo, " +
		"p.telefono_celular as telefono_celular, " +
		"ciu.descripcion as descripcion_ciudad, "+
		"ing.fecha_egreso as fecha_egreso, "+
		"coalesce(manejopaciente.getnombretipoafiliado(sc.tipo_afiliado),'') as tipo_afiliado, "+
		"facturacion.getnombreconvenio(sc.convenio) as nombre_convenio "+ 
		"FROM manejopaciente.cuentas c "+ 
		"INNER JOIN administracion.personas p ON(p.codigo = c.codigo_paciente) "+
		"INNER JOIN administracion.ciudades ciu ON (p.codigo_ciudad_vivienda=ciu.codigo_ciudad and p.codigo_departamento_vivienda=ciu.codigo_departamento and p.codigo_pais_vivienda=ciu.codigo_pais) "+
		"INNER JOIN manejopaciente.sub_cuentas sc on(sc.ingreso = c.id_ingreso) "+ 
		"INNER JOIN manejopaciente.ingresos ing on(ing.codigo_paciente = p.codigo) "+
		"WHERE c.id_ingreso = ? order by sc.nro_prioridad";
	
	
	/**
	 * Método para obtener las citas odontologicas para atender
	 * @param parametros
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitas(HashMap<String, Object> parametros)
	{
		ArrayList<DtoCitaOdontologica> citas = new ArrayList<DtoCitaOdontologica>();
		try
		{
			//*******************SE TOMAN LOS PARÁMETROS***********************************************+++
			UsuarioBasico usuario = (UsuarioBasico)parametros.get("usuario");
			String fecha = UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha").toString());
			String codigosEspecialidades = ConstantesBD.codigoNuncaValido+"";
			for(InfoDatosInt especialidad:usuario.getEspecialidades())
			{
				if(especialidad.isActivo())
				{
					codigosEspecialidades += "," + especialidad.getCodigo();
				}
			}
			//********************************************************************************************
			
			Connection con = UtilidadBD.abrirConexion();
			String consulta = consultarCitasStr + 
				"to_char(ao.fecha,'"+ConstantesBD.formatoFechaBD+"') = '"+fecha+"' and "+ 
				"ao.centro_atencion = "+usuario.getCodigoCentroAtencion()+" and "+
				//Que corresponda al medico o si es no asignado que tenga la misma especialidad de la unidad de agenda o el mismo centro de costo de la unidad de agenda
				"( "+
					"ao.codigo_medico = "+usuario.getCodigoPersona()+" "+ 
					"or "+ 
					"( "+ 
						"ao.codigo_medico is null and "+ 
						"( "+
							"(" +
								"uc.especialidad in("+codigosEspecialidades+") " +
								" and " +
								"(select count(1) from odontologia.servicios_cita_odontologica sco  WHERE sco.cita_odontologica = co.codigo_pk and facturacion.gettiposervicio(sco.servicio) in ('"+ConstantesBD.codigoServicioInterconsulta+"', '"+ConstantesBD.codigoServicioProcedimiento+"' ))>0 " +
							") " +
							
							/*
							 *Se elimina esta validación- Cambio en el documento 886 - Xplanner: 20497 
							 */
//							"or " +
//							"( "+ 
//								"uc.codigo in (select cc.unidad_consulta from consultaexterna.cen_costo_x_un_consulta cc WHERE cc.centro_costo = "+usuario.getCodigoCentroCosto()+") " +
//								" and " +
//								"(select count(1) from odontologia.servicios_cita_odontologica sco WHERE sco.cita_odontologica = co.codigo_pk and facturacion.gettiposervicio(sco.servicio) = '"+ConstantesBD.codigoServicioProcedimiento+"' )>0 " +
//							")"+
						") "+
					") "+
				") and "+ 
				//Verificar estado Asignada - Reporgramada
				"co.estado = '"+ConstantesIntegridadDominio.acronimoAsignado+"'  and "+
				//Verificar que mínimo una solicitud este pendiente por responder o que no tenga servicios asociados.
				"( " +
					" co.codigo_pk in (" +
						"SELECT sco.cita_odontologica " +
						"from odontologia.servicios_cita_odontologica sco " +
						"inner join ordenes.solicitudes s on(s.numero_solicitud = sco.numero_solicitud) " +
						"WHERE sco.cita_odontologica = co.codigo_pk and s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" " +
						") " +
						"or" +
						"(select count(1) from odontologia.servicios_cita_odontologica sco WHERE sco.cita_odontologica = co.codigo_pk and activo='"+ConstantesBD.acronimoSi+"') = 0 " +
				")"+
				"ORDER BY fecha,hora_inicio,nombre_unidad_agenda";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con, consulta);
			Log4JManager.info(pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoCitaOdontologica cita = new DtoCitaOdontologica();
				
				cita.getAgendaOdon().setCodigoPk(rs.getInt("codigo_agenda"));
				cita.setAgenda(rs.getInt("codigo_agenda"));
				cita.setCodigoPk(rs.getInt("codigo_cita"));
				cita.getAgendaOdon().setUnidadAgenda(rs.getInt("codigo_unidad_agenda"));
				cita.getAgendaOdon().setDescripcionUniAgen(rs.getString("nombre_unidad_agenda"));
				cita.getAgendaOdon().setEspecialidadUniAgen(rs.getInt("codigo_especialidad"));
				cita.getAgendaOdon().setCodigoMedico(rs.getInt("codigo_medico"));
				cita.getAgendaOdon().setNombreEspecialidadUniAgen(rs.getString("nombre_especialidad"));
				cita.getAgendaOdon().setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				cita.getAgendaOdon().setFechaBD(rs.getString("fecha"));
				cita.setHoraInicio(rs.getString("hora_inicio"));
				cita.setHoraFinal(rs.getString("hora_fin"));
				cita.setCodigoPaciente(rs.getInt("codigo_paciente"));
				cita.setNombrePaciente(rs.getString("nombre_paciente"));
				cita.setEstado(rs.getString("estado"));
				cita.setEstadoAnterior(cita.getEstado());
				cita.setPorConfirmar(rs.getString("por_confirmar"));
				cita.setTipo(rs.getString("tipo_cita"));
				cita.setDuracion(rs.getInt("duracion"));
				cita.setFechaReserva(rs.getString("fecha_reserva"));
				cita.setHoraReserva(rs.getString("hora_reserva"));
				cita.setUsuarioReserva(rs.getString("usuario_reserva"));
				cita.setMotivoCancelacion(rs.getInt("motivo_cancelacion"));
				cita.setCodigoCentroCosto(rs.getInt("centro_costo"));
				cita.setFechaProgramacion(rs.getString("fecha_programacion"));
				
				cita.getUsuarioRegistra().setLoginUsuario(rs.getString("usuario_registra"));
				if(!cita.getUsuarioRegistra().getLoginUsuario().equals(""))
				{
					cita.getUsuarioRegistra().cargarUsuarioBasico(con, cita.getUsuarioRegistra().getLoginUsuario());
					cita.setFechaRegistra(rs.getString("fecha_registra"));
					cita.setHoraRegistra(rs.getString("hora_registra"));
				}
				
				cita.getUsuarioConfirma().setLoginUsuario(rs.getString("usuario_confirma"));
				if(!cita.getUsuarioConfirma().getLoginUsuario().equals(""))
				{
					cita.getUsuarioConfirma().cargarUsuarioBasico(con, cita.getUsuarioConfirma().getLoginUsuario());
					cita.setFechaConfirma(rs.getString("fecha_confirma"));
					cita.setHoraConfirma(rs.getString("hora_confirma"));
				}
				
				cita.setUsuarioModifica(rs.getString("usuario_modifica"));
				if(!cita.getUsuarioModifica().equals(""))
				{
					cita.setFechaModifica(rs.getString("fecha_modifica"));
					cita.setHoraModifica(rs.getString("hora_modifica"));
				}
				
				if(cita.getAgendaOdon().getCodigoMedico()>0)
				{
					cita.getAgendaOdon().getUsuarioMedico().cargarUsuarioBasico(con, cita.getAgendaOdon().getCodigoMedico());
				}
				
				
				
				//cargar la informacion de la ultima solicitud de cambio de servicio en estado solicitada.
				String consultaSolicitudCambioServicio="SELECT codigo_pk as codigo,estado as estado from odontologia.solicitud_cambio_servicio where codigo_cita=? and estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"' order by codigo_pk desc";
				PreparedStatementDecorator psSCS =  new PreparedStatementDecorator(con,consultaSolicitudCambioServicio);
				psSCS.setInt(1, cita.getCodigoPk());
				ResultSetDecorator rsSCS = new ResultSetDecorator(psSCS.executeQuery());
				if(rsSCS.next())
				{
					cita.setCodigoSolicitudCambioServicio(rsSCS.getInt("codigo"));
					cita.setEstadoSolicitudCambioServicio(rsSCS.getString("estado"));
				}
				else
				{
					cita.setCodigoSolicitudCambioServicio(ConstantesBD.codigoNuncaValido);
					cita.setEstadoSolicitudCambioServicio("");
				}
				
				psSCS.close();
				rsSCS.close();
				
				//Cargar los servicios de la cita
				cargarServiciosCita(con, cita, usuario);
				
				//cita.setExistenServiciosDeOrdenMenorAsignados(!UtilidadOdontologia.existeServicioAsignadoDeMenorOrden(cita.getCodigoPk()));
				// Esto no se valida por el momento, se quitaron todas las validaciones de orden de los servicios hasta definirlo bien
				cita.setExistenServiciosDeOrdenMenorAsignados(true);
				
				citas.add(cita);
				
				
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCitas: ",e);
		}
		return citas;
	}
	
	/**
	 * Método para cargar las plantillas de atención para la cita
	 * @param cita
	 * @param usuario
	 * @return
	 */
	public static ArrayList<DtoPlantilla> cargarPlantillasAtencion(DtoCitaOdontologica cita,UsuarioBasico usuario)
	{
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoPlantilla> plantillas = new ArrayList<DtoPlantilla>();
		
		//Variables que aplican para el filtro
		String especialidades = "";
		String codigoEspecialidad = "";
		
		
		//Se llenan las especialidades que podrían aplicar
		codigoEspecialidad = cita.getAgendaOdon().getEspecialidadUniAgen()+"";
		for(InfoDatosInt infoEspecialidad:usuario.getEspecialidades())
		{
			especialidades += "," + infoEspecialidad.getCodigo();
		}
		
		//Dependiendo del tipo de cita se filtra las plantillas que apliquen
		if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial))
		{
			plantillas = cargarPlantillasValoracionOdontologica(con,cita,codigoEspecialidad+especialidades,ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial);
		}
		else if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoPrioritaria))
		{
			plantillas = cargarPlantillasValoracionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionPrioritariaUrgencias);
			
			//si la cita tiene plan de tratamiento se consultan las plantillas de evolución
			if(tienePlanTratamiento(cita))
			{
				plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad+especialidades, /*ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento+"','"+ Creo que esta parte no se debe mostrar ya que esta es solo para evolucionar el plan de tratamiento contratado y los servicios son diferentes*/ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, true));
			}
			
		}
		else if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta))
		{
			plantillas = cargarPlantillasValoracionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial);
			
			//si la cita tiene plan de tratamiento se consultan las plantillas de evolución
			if(tienePlanTratamiento(cita))
			{
				plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad+especialidades, ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, true));
			}
		}
		else if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento))
		{
			if(cita.getServiciosCitaOdon().size()>0)
			{
				plantillas = cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento+"','"+ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, true);
				
				plantillas.addAll(cargarPlantillasValoracionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionValoracionPrimeraVezXEspecialista));
			}
			else
			{
				plantillas = cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, true);
			}
		}
		else if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon))
		{
			/*
			 * Si el paciente tiene plan de tratamiento 'En Proceso' 
			 * se consultan las plantillas de evolución
			 */

			ArrayList<String> estados=new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
			estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
			
			if(tienePlanTratamiento(cita, estados))
			{
				estados=new ArrayList<String>();
				estados.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
				DtoPresupuestoOdontologico presupuestoOdontologico=new DtoPresupuestoOdontologico();
				presupuestoOdontologico.setCodigoPaciente(new InfoDatosInt(cita.getCodigoPaciente()));
				ArrayList<DtoPresupuestoOdontologico> listaPresupuestos=PresupuestoOdontologico.cargarPresupuesto(presupuestoOdontologico, estados);
				if(listaPresupuestos!=null && listaPresupuestos.size()>0)
				{
					//plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad+especialidades, ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento, true));
					// En este caso no se muestran las plantillas para las especialidades del médico
					plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento, true));
				}
			}
			
			// Las de historia siempre se deben mostrar
			//plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, false));
			// En este caso no se muestran las plantillas para las especialidades del médico XPlanner 
			if (cita.getPorConfirmar().equals(ConstantesBD.acronimoSi)) {
				plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, true));
			} else {
				plantillas.addAll(cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, false));
			}
		}
		else if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoAuditoria))
		{
			plantillas = cargarPlantillasEvolucionOdontologica(con, cita, codigoEspecialidad, ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento+"','"+ConstantesIntegridadDominio.acronimoTipoAtencionRegistroHistoria, false);
		}
		else if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoRevaloracion))
		{
			plantillas = cargarPlantillasValoracionOdontologica(con,cita,codigoEspecialidad,ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial);
		}
			
		UtilidadBD.closeConnection(con);
		return plantillas;
	}


	/**
	 * Método implementado para cargar la o las plantillas de la atencion de cita Valoracion Inicial
	 * @param con
	 * @param cita
	 * @param especialidades
	 * @return
	 */
	private static ArrayList<DtoPlantilla> cargarPlantillasValoracionOdontologica(Connection con, DtoCitaOdontologica cita, String especialidades,String tipoAtencion) 
	{
		ArrayList<DtoPlantilla> plantillas = new ArrayList<DtoPlantilla>();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		String consulta = "";
		String codigosPlantillasEncontradas = "";
		
		try
		{
			//1) Se verifica si la cita ya tiene una plantilla
			consulta = "SELECT "+  
				"p.codigo_pk as codigo_pk, "+
				"p.tipo_atencion as tipo_atencion, " +
				"p.fun_param as fun_param, " +
				"fp.nombre as nom_fun_param, "+
				"p.codigo_plantilla as codigo_plantilla, "+
				"p.nombre_plantilla as nombre_plantilla, " +
				"'"+ConstantesBD.acronimoSi+"' as en_proceso," +
				"coalesce(pi.codigo_pk,"+ConstantesBD.codigoNuncaValido+") as codigo_historico "+ 
				"FROM odontologia.valoraciones_odonto vo "+ 
				"INNER JOIN historiaclinica.plantillas_ingresos pi ON(pi.valoracion_odonto = vo.codigo_pk) "+ 
				"INNER JOIN historiaclinica.plantillas p ON(p.codigo_pk = pi.plantilla) " +
				"INNER JOIN administracion.fun_param fp ON(fp.codigo = p.fun_param) "+ 
				"WHERE "+ 
				"vo.cita  = "+cita.getCodigoPk()+"  " + 
				"ORDER BY p.nombre_plantilla";
			
			logger.info("consulta N°1 para las valoraciones: "+consulta);
			pst = new PreparedStatementDecorator(con,consulta);
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoPlantilla plantilla = new DtoPlantilla();
				plantilla.setCodigoPK(rs.getString("codigo_pk"));
				plantilla.setTipoAtencion(rs.getString("tipo_atencion"));
				plantilla.setCodigo(rs.getString("codigo_plantilla"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				plantilla.setCodigoFuncionalidad(rs.getInt("fun_param"));
				plantilla.setNombreFuncionalidad(rs.getString("nom_fun_param"));
				plantilla.setPlantillaEnProceso(UtilidadTexto.getBoolean(rs.getString("en_proceso")));
				plantilla.setConsecutivoHistorico(rs.getString("codigo_historico"));
				plantillas.add(plantilla);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
			//Se toman los codigos de las plantillas ya insertadas
			for(DtoPlantilla plantilla:plantillas)
			{
				codigosPlantillasEncontradas += (codigosPlantillasEncontradas.equals("")?"":",") +plantilla.getCodigoPK();
			}
			
			
			//2) Se consultan las plantillas que aplicarían para la cita valoracion inicial
			consulta = "SELECT "+
				"p.codigo_pk as codigo_pk, "+
				"p.tipo_atencion as tipo_atencion, "+
				"p.fun_param as fun_param, " +
				"fp.nombre as nom_fun_param, "+
				"p.codigo_plantilla as codigo_plantilla, "+
				"p.nombre_plantilla as nombre_plantilla, " +
				"'"+ConstantesBD.acronimoNo+"' as en_proceso "+ 
				"FROM historiaclinica.plantillas p "+
				"INNER JOIN administracion.fun_param fp ON(fp.codigo = p.fun_param) "+
				"WHERE "+ 
				"p.fun_param = "+ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia+" and " +
				"p.tipo_atencion = '"+tipoAtencion+"' and " +
				"(p.especialidad in ("+especialidades+") or p.especialidad = "+ConstantesBD.codigoEspecialidadMedicaTodos+") and " +
				"p.mostrar_modificacion = '"+ConstantesBD.acronimoSi+"'  " +
				(codigosPlantillasEncontradas.equals("")?"":" and p.codigo_pk not in ("+codigosPlantillasEncontradas+") ") +
				"ORDER BY p.nombre_plantilla";	
			
			logger.info("consulta N°2 para las valoraciones: "+consulta);
			pst = new PreparedStatementDecorator(con,consulta);
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoPlantilla plantilla = new DtoPlantilla();
				plantilla.setCodigoPK(rs.getString("codigo_pk"));
				plantilla.setTipoAtencion(rs.getString("tipo_atencion"));
				plantilla.setCodigo(rs.getString("codigo_plantilla"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				plantilla.setCodigoFuncionalidad(rs.getInt("fun_param"));
				plantilla.setNombreFuncionalidad(rs.getString("nom_fun_param"));
				plantilla.setPlantillaEnProceso(UtilidadTexto.getBoolean(rs.getString("en_proceso")));
				plantillas.add(plantilla);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPlantillasValoracionOdontologica: ",e);
		}
		return plantillas;
	}
	
	
	/**
	 * 
	 * Método implementado para cargar las plantillas de evolución odontológica
	 * 
	 * @param con
	 * @param cita
	 * @param especialidades
	 * @param tipoAtencion
	 * @param incluirPlantillasAsociadasCita -- Indica si se incluyen o no las plantillas asociadas previamente a la cita
	 * @return
	 */
	private static ArrayList<DtoPlantilla> cargarPlantillasEvolucionOdontologica(Connection con,DtoCitaOdontologica cita, String especialidades,String tipoAtencion, boolean incluirPlantillasAsociadasCita)
	{
		ArrayList<DtoPlantilla> plantillas = new ArrayList<DtoPlantilla>();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		String consulta = "";
		String codigosPlantillasEncontradas = "";
		
		try
		{
			//1) Se verifica si la cita ya tiene una plantilla
			consulta = "SELECT "+  
				"p.codigo_pk as codigo_pk, "+
				"p.tipo_atencion as tipo_atencion, " +
				"p.fun_param as fun_param, " +
				"fp.nombre as nom_fun_param, "+
				"p.codigo_plantilla as codigo_plantilla, "+
				"p.nombre_plantilla as nombre_plantilla, " +
				"'"+ConstantesBD.acronimoSi+"' as en_proceso," +
				"coalesce(pe.codigo_pk,"+ConstantesBD.codigoNuncaValido+") as codigo_historico "+ 
			"FROM " +
				"odontologia.evoluciones_odo eo "+ 
			"INNER JOIN " +
				"historiaclinica.plantillas_evolucion pe ON(pe.evolucion_odonto = eo.codigo_pk) "+ 
			"INNER JOIN " +
				"historiaclinica.plantillas p ON(p.codigo_pk = pe.plantilla) "+
			"INNER JOIN " +
				"administracion.fun_param fp ON(fp.codigo = p.fun_param) "+
			"WHERE "+ 
				"eo.cita  = "+cita.getCodigoPk()+ " "+ 
			"ORDER BY " +
				"p.nombre_plantilla";
			
			pst = new PreparedStatementDecorator(con,consulta);
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoPlantilla plantilla = new DtoPlantilla();
				plantilla.setCodigoPK(rs.getString("codigo_pk"));
				plantilla.setTipoAtencion(rs.getString("tipo_atencion"));
				plantilla.setCodigo(rs.getString("codigo_plantilla"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				plantilla.setCodigoFuncionalidad(rs.getInt("fun_param"));
				plantilla.setNombreFuncionalidad(rs.getString("nom_fun_param"));
				plantilla.setPlantillaEnProceso(UtilidadTexto.getBoolean(rs.getString("en_proceso")));
				plantilla.setConsecutivoHistorico(rs.getString("codigo_historico"));
				plantillas.add(plantilla);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
			//Se toman los codigos de las plantillas ya insertadas
			for(DtoPlantilla plantilla:plantillas)
			{
				codigosPlantillasEncontradas += (codigosPlantillasEncontradas.equals("")?"":",") +plantilla.getCodigoPK();
			}
			
			/*
			 * Este atributo indica si se debe o no incluir 
			 * las plantillas asociadas a la cita
			 */
			if (!incluirPlantillasAsociadasCita){
				
				plantillas.clear();
			}
		
			//2) Se consultan las plantillas que aplicarían para la cita evolución 
			consulta = "SELECT "+
				"p.codigo_pk as codigo_pk, "+
				"p.tipo_atencion as tipo_atencion, " +
				"p.fun_param as fun_param, " +
				"fp.nombre as nom_fun_param, "+
				"p.codigo_plantilla as codigo_plantilla, "+
				"p.nombre_plantilla as nombre_plantilla, " +
				"'"+ConstantesBD.acronimoNo+"' as en_proceso "+ 
				"FROM historiaclinica.plantillas p "+
				"INNER JOIN administracion.fun_param fp ON(fp.codigo = p.fun_param) "+
				"WHERE "+ 
				"p.fun_param = "+ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica+" and " +
				"p.tipo_atencion IN ('"+tipoAtencion+"') and " +
				"(p.especialidad in ("+especialidades+") or p.especialidad = "+ConstantesBD.codigoEspecialidadMedicaTodos+") and " +
				"p.mostrar_modificacion = '"+ConstantesBD.acronimoSi+"' " +
				(codigosPlantillasEncontradas.equals("")?"":" and p.codigo_pk not in ("+codigosPlantillasEncontradas+") ") +
				"ORDER BY p.nombre_plantilla";	
			
			pst = new PreparedStatementDecorator(con,consulta);
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoPlantilla plantilla = new DtoPlantilla();
				plantilla.setCodigoPK(rs.getString("codigo_pk"));
				plantilla.setTipoAtencion(rs.getString("tipo_atencion"));
				plantilla.setCodigoFuncionalidad(rs.getInt("fun_param"));
				plantilla.setNombreFuncionalidad(rs.getString("nom_fun_param"));
				plantilla.setCodigo(rs.getString("codigo_plantilla"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				plantilla.setPlantillaEnProceso(UtilidadTexto.getBoolean(rs.getString("en_proceso")));
				plantillas.add(plantilla);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPlantillasEvolucionOdontologica: ",e);
		}
		
		return plantillas;
	}
	
	/**
	 * M&eacute;todo para identificar si la cita pertenece a 
	 * un paciente con plan de tratamiento
	 * @param cita {@link DtoCitaOdontologica} Informaci&oacute;n de la cita
	 * @return true en caso de tener plan de tratamiento, false de lo contrario
	 */
	private static boolean tienePlanTratamiento(DtoCitaOdontologica cita)
	{
		ArrayList<String> estados=new ArrayList<String>();
		estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estados.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
		ArrayList<BigDecimal> codigos=PlanTratamiento.obtenerCodigoPlanTratamiento(cita.getCodigoPaciente(), estados, null);
		if(codigos!=null && codigos.size()>0)
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * Método para identificar si la cita pertenece a un paciente con plan de tratamiento,
	 * pasandole los estados posibles del Plan de Tratamiento
	 * 
	 * @param cita {@link DtoCitaOdontologica} Informaci&oacute;n de la cita
	 * @param estados
	 * @return true en caso de tener plan de tratamiento, false de lo contrario
	 */
	private static boolean tienePlanTratamiento(DtoCitaOdontologica cita, ArrayList<String> estados)
	{
		ArrayList<BigDecimal> codigos=PlanTratamiento.obtenerCodigoPlanTratamiento(cita.getCodigoPaciente(), estados, null);
		if(codigos!=null && codigos.size()>0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Método para cargar los datos de la cita para la atencion
	 * @param cita
	 * @param usuario
	 */
	public static void cargarDatosCita(DtoCitaOdontologica cita,UsuarioBasico usuario, boolean leftOuterUnidadAgenda)
	{
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			String consulta =  (leftOuterUnidadAgenda)?consultarCitasLeftOterStr+" co.codigo_pk = ? ": consultarCitasStr + " co.codigo_pk = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setLong(1,cita.getCodigoPk());
			logger.info(pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				cita.getAgendaOdon().setCodigoPk(rs.getInt("codigo_agenda"));
				cita.setAgenda(rs.getInt("codigo_agenda"));
				cita.setCodigoPk(rs.getInt("codigo_cita"));
				cita.getAgendaOdon().setUnidadAgenda(rs.getInt("codigo_unidad_agenda"));
				cita.getAgendaOdon().setDescripcionUniAgen(rs.getString("nombre_unidad_agenda"));
				cita.getAgendaOdon().setEspecialidadUniAgen(rs.getInt("codigo_especialidad"));
				cita.getAgendaOdon().setCodigoMedico(rs.getInt("codigo_medico"));
				cita.getAgendaOdon().setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				cita.getAgendaOdon().setFechaBD(rs.getString("fecha"));
				cita.setHoraInicio(rs.getString("hora_inicio"));
				cita.setHoraFinal(rs.getString("hora_fin"));
				cita.setCodigoPaciente(rs.getInt("codigo_paciente"));
				cita.setNombrePaciente(rs.getString("nombre_paciente"));
				cita.setEstado(rs.getString("estado"));
				cita.setEstadoAnterior(cita.getEstado());
				cita.setPorConfirmar(rs.getString("por_confirmar"));
				cita.setTipo(rs.getString("tipo_cita"));
				cita.setDuracion(rs.getInt("duracion"));
				cita.setFechaReserva(rs.getString("fecha_reserva"));
				cita.setHoraReserva(rs.getString("hora_reserva"));
				cita.setUsuarioReserva(rs.getString("usuario_reserva"));
				cita.setMotivoCancelacion(rs.getInt("motivo_cancelacion"));
				cita.setCodigoCentroCosto(rs.getInt("centro_costo"));
				cita.setFechaProgramacion(rs.getString("fecha_programacion"));
				
				cita.getUsuarioRegistra().setLoginUsuario(rs.getString("usuario_registra"));
				if(!cita.getUsuarioRegistra().getLoginUsuario().equals(""))
				{
					cita.getUsuarioRegistra().cargarUsuarioBasico(con, cita.getUsuarioRegistra().getLoginUsuario());
					cita.setFechaRegistra(rs.getString("fecha_registra"));
					cita.setHoraRegistra(rs.getString("hora_registra"));
				}
				
				cita.getUsuarioConfirma().setLoginUsuario(rs.getString("usuario_confirma"));
				if(!cita.getUsuarioConfirma().getLoginUsuario().equals(""))
				{
					cita.getUsuarioConfirma().cargarUsuarioBasico(con, cita.getUsuarioConfirma().getLoginUsuario());
					cita.setFechaConfirma(rs.getString("fecha_confirma"));
					cita.setHoraConfirma(rs.getString("hora_confirma"));
				}
				
				if(cita.getAgendaOdon().getCodigoMedico()>0)
				{
					cita.getAgendaOdon().getUsuarioMedico().cargarUsuarioBasico(con, cita.getAgendaOdon().getCodigoMedico());
				}
				
				//Se cargan los servicios de la cita
				cargarServiciosCita(con, cita, usuario);
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosCita: ",e);
		}
	}
	
	/**
	 * Método implementado para cargar los servicios de una cita
	 * @param con
	 * @param cita
	 * @param usuario
	 */
	public static void cargarServiciosCita(Connection con,DtoCitaOdontologica cita,UsuarioBasico usuario)
	{
		try
		{
			int numeroServiciosPendientes = 0; 
			
			PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con,consultarServiciosCitaStr);
			pst1.setInt(1,Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
			pst1.setInt(2,Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
			pst1.setInt(3,cita.getCodigoPk());
			
			logger.info("Consulta servicios de la cita: "+pst1);
			
			
			ResultSetDecorator rs1 = new ResultSetDecorator(pst1.executeQuery());
			
			
			
			while(rs1.next())
			{
				DtoServicioCitaOdontologica servicioCita = new DtoServicioCitaOdontologica();
				servicioCita.setCodigoPk(rs1.getInt("codigo_servicio_cita"));
				servicioCita.setServicio(rs1.getInt("codigo_servicio"));
				servicioCita.setCodigoPropietarioServicio(rs1.getString("cod_cups_servicio"));
				servicioCita.setNombreServicio(rs1.getString("nombre_servicio"));
				servicioCita.setCodigoTipoServicio(rs1.getString("tipo_servicio"));
				servicioCita.setNombreTipoServicio(rs1.getString("nom_tipo_servicio"));
				servicioCita.setNumeroSolicitud(rs1.getInt("numero_solicitud"));
				servicioCita.getEstadoHistoriaClinicaSolicitud().setCodigo(rs1.getInt("estado_historia_clinica"));
				servicioCita.setCodigoProgramaHallazgoPieza(rs1.getInt("programa_hallazgo_pieza"));
				servicioCita.setCodigoPrograma(rs1.getInt("programa"));
				servicioCita.setCodigoProgramaHallazgoPieza(rs1.getInt("codigophp"));
				if(rs1.getObject("plantratamiento")!=null)
					servicioCita.setPlanTratamiento(rs1.getInt("plantratamiento"));
				DtoProgHallazgoPieza php=new DtoProgHallazgoPieza();
				php.setCodigoPk(rs1.getInt("codigophp"));
				servicioCita.setProgramaHallazgoPieza(php);
				
				if(servicioCita.getEstadoHistoriaClinicaSolicitud().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada)
				{
					numeroServiciosPendientes ++;
				}
				
				cita.getServiciosCitaOdon().add(servicioCita);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst1, rs1, null);
			
			//***********SE VERIFICA SI LA CITA TIENE TODOS LOS SERVICIOS PENDIENTES********************
			if(cita.getServiciosCitaOdon().size()==numeroServiciosPendientes)
			{
				cita.setTodasSolicitudesPendientes(true);
			}
			else
			{
				cita.setTodasSolicitudesPendientes(false);
			}
			//******************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarServiciosCita: ",e);
		}
	}
	
	/**
	 * Método implementado para realizar la confirmación de los datos de la cita
	 * @param con
	 * @param cita
	 * @param usuarioConfirmacion
	 * @return
	 */
	public static ResultadoBoolean confirmarDatosCita(Connection con, DtoCitaOdontologica cita, UsuarioBasico usuarioConfirmacion)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		try
		{
			//Se actualizan los datos de la cita
			String consulta = "UPDATE odontologia.citas_odontologicas SET estado = '"+ConstantesIntegridadDominio.acronimoAtendida+"', por_confirmar = '"+ConstantesBD.acronimoNo+"', fecha_confirma = current_date, hora_confirma = "+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_confirma = ? WHERE codigo_pk = ? ";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setString(1,usuarioConfirmacion.getLoginUsuario());
			pst.setInt(2,cita.getCodigoPk());
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("Problemas al actualiza el estado de la cita");
			}
			/*pst.close();
			
			//Se actualiza el profesional de la agenda.
			consulta = "UPDATE odontologia.agenda_odontologica SET codigo_medico = ? WHERE codigo_pk = ?";
			pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,usuarioConfirmacion.getCodigoPersona());
			pst.setInt(2,cita.getAgenda());
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("Problemas al actualizar el profesional de la salud de la agenda");
			}*/
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, null, null);
		}
		catch(SQLException e)
		{
			logger.error("Error en confirmarDatosCita: ",e);
			resultado.setResultado(false);
			resultado.setDescripcion("Error al actualizar los datos de la cita: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método implementado 
	 * @param con
	 * @param codigoPkProgServ
	 * @return
	 */
	public static BigDecimal consultarNumeroSolicitudServicioCitaXPlanTratamiento(Connection con,BigDecimal codigoPkProgServ)
	{
		BigDecimal numeroSolicitud = new BigDecimal(ConstantesBD.codigoNuncaValido);
		try
		{
			String consulta = "SELECT DISTINCT coalesce(sco.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") as numero_solicitud " +
								" from odontologia.servicios_cita_odontologica sco" +
								" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
								" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
								" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
								" INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and sco.servicio=pspt.servicio AND pspt.activo = 'S' ) " +
								" where pspt.codigo_pk = ? and sco.activo = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setLong(1,codigoPkProgServ.longValue());
			pst.setString(2,ConstantesBD.acronimoSi);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				numeroSolicitud = new BigDecimal(rs.getLong("numero_solicitud"));
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, null);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarNumeroSolicitudServicioCitaXPlanTratamiento: ",e);
		}
		return numeroSolicitud;
	}
	
	/**
	 * Método implementado para consultar el codigo_pk del servicio de la cita asociado
	 * al registro del programa/servicio plan de tratamiento
	 * @param con
	 * @param codigoPkProgServ
	 * @return
	 */
	public static BigDecimal consultarCodigoServicioCitaOdoXPlanTratamiento(Connection con,BigDecimal codigoPkProgServ)
	{
		BigDecimal codServCitaOdo = new BigDecimal(ConstantesBD.codigoNuncaValido);
		try
		{
			String consulta = "select distinct codigo_pk as codigo " +
							  " FROM odontologia.servicios_cita_odontologica sco " +
							  " INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
							  " INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
							  " INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
							  " INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk  and sco.servicio=pspt.servicio AND pspt.activo = 'S' ) " +
							  " where pspt.codigo_pk = ? and sco.activo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,consulta);
			pst.setBigDecimal(1, codigoPkProgServ);
			pst.setString(2,ConstantesBD.acronimoSi);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codServCitaOdo = rs.getBigDecimal("codigo");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, null);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCodigoServicioCitaOdoXPlanTratamiento: ",e);
		}
		return codServCitaOdo;
	}
	
	
	/**
	 * Método implementado para cargar las plantillas registradas de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static ArrayList<DtoPlantilla> cargarPlantillasRegistradas(Connection con,BigDecimal codigoCita)
	{
		ArrayList<DtoPlantilla> plantillas = new ArrayList<DtoPlantilla>();
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,cargarPlantillasRegistradasStr);
			pst.setBigDecimal(1, codigoCita);
			pst.setBigDecimal(2, codigoCita);
			 
			logger.info("\n\n Consulta cargarPlantillasRegistradas >>"+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoPlantilla plantilla = new DtoPlantilla();
				plantilla.setCodigoPK(rs.getString("codigo_plantilla"));
				plantilla.setConsecutivoHistorico(rs.getString("codigo_plantilla_his"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				if(UtilidadTexto.getBoolean(rs.getString("valoracion")))
				{
					plantilla.setCodigoValoracionOdontologia(rs.getBigDecimal("codigo"));
				}
				else
				{
					plantilla.setCodigoEvolucionOdontologia(rs.getBigDecimal("codigo"));
				}
				plantillas.add(plantilla);
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, null);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPlantillasRegistradas: ",e);
		}
		return plantillas;
	}
	
	/**
	 * Método implementado para cargar los datos del paciente
	 * @param con
	 * @param paciente
	 */
	public static void cargarDatosPaciente(Connection con,DtoPaciente paciente)
	{
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,cargarDatosPacienteStr);
			pst.setBigDecimal(1, paciente.getIdIngreso());
			logger.info("\n\n Consulta cargarDatosPaciente >>"+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				paciente.setNombreCentroAtencion(rs.getString("centro_atencion"));
				paciente.setEdadIngreso(UtilidadFecha.calcularEdadDetallada(rs.getString("fecha_nacimiento"), rs.getString("fecha_ingreso")));
				paciente.setEdadPaciente(Integer.toString(UtilidadFecha.calcularEdad(rs.getString("fecha_nacimiento"))));
				paciente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
				paciente.setFechaIngreso(rs.getString("fecha_ingreso"));
				paciente.setNombreViaIngreso(rs.getString("nombre_via_ingreso"));
				paciente.setPrimerApellido(rs.getString("primer_apellido"));
				paciente.setSegundoApellido(rs.getString("segundo_apellido"));
				paciente.setPrimerNombre(rs.getString("primer_nombre"));
				paciente.setSegundoNombre(rs.getString("segundo_nombre"));
				paciente.setTipoIdentificacion(rs.getString("tipo_identificacion"));
				paciente.setNumeroIdentificacion(rs.getString("numero_identificacion"));
				paciente.setSexo(rs.getString("nombre_sexo"));
				paciente.setDireccion(rs.getString("direccion"));
				paciente.setTelefonoFijo(rs.getString("telefono_fijo"));
				paciente.setTelefonoCelular(rs.getString("telefono_celular"));
				paciente.setNombreCiudadResidencia(rs.getString("descripcion_ciudad"));
				paciente.setFechaEgreso(rs.getString("fecha_egreso"));
				
				paciente.setNombreTipoAfiliado((paciente.getNombreTipoAfiliado().equals("")?"":", ")+rs.getString("tipo_afiliado"));
				paciente.setNombresResponsables((paciente.getNombresResponsables().equals("")?"":", ")+rs.getString("nombre_convenio"));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, null);
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error cargarDatosPaciente: ",e);
		}
	}
	
	/**
	 * Método para obtener el historico de plantillas por ingreso de un paciente
	 * @param codigoPaciente
 	 * @param consecutivoIngreso
	 * @return plantillas Lista de plantillas por ingreso de un paciente
	 */
	public static ArrayList<DtoPlantilla> consultarPlantillasPorIngresos(int codigoPaciente, String consecutivoIngreso)
	{
		ArrayList<DtoPlantilla> plantillas = new ArrayList<DtoPlantilla>();
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,cargarPlantillasPorIngresoStr);
			pst.setInt(1, codigoPaciente);
			pst.setString(2, consecutivoIngreso);
			pst.setInt(3, codigoPaciente);
			pst.setString(4, consecutivoIngreso);
			
			 
			logger.info("\n\n Consulta cargarPlantillasPorIngreso >>"+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				
				DtoPlantilla plantilla = new DtoPlantilla();
				plantilla.setFechaRegistro(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				plantilla.setCodigo(rs.getString("codigo"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				plantilla.setNombreFuncionalidad(rs.getString("tipo"));
				plantilla.setProfesionalRegistro(rs.getString("primer_nombre_profesional")+" "+rs.getString("segundo_nombre_profesional")+" "+rs.getString("primer_apellido_profesional")+" "+rs.getString("segundo_apellido_profesional"));
				plantilla.setCodigoPK(rs.getString("codigo_plantilla"));
				plantilla.setNombreEspecialidad(rs.getString("nombre_especialidad"));
				plantilla.setCodigoPkCita(rs.getBigDecimal("codigo_pk_cita"));
				
				plantillas.add(plantilla);
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, null);
			UtilidadBD.closeConnection(con);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPlantillasPorIngresos: ",e);
		}
		return plantillas;
	}
	
	
	/**
	 * Método implementado para cargar la plantilla seleccionada en el historico 
	 * @param con
	 * @param codigoCita
	 * @param codigoPkPlantilla
	 * @return 
	 */
	public static DtoPlantilla cargarPlantillaSeleccionadaDeHistorico(Connection con,BigDecimal codigoCita,BigDecimal codigoPkPlantilla)
	{
		DtoPlantilla plantilla = null;
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,cargarPlantillaPorCodigoPk);
			pst.setBigDecimal(1, codigoPkPlantilla);
			pst.setBigDecimal(2, codigoCita);
			pst.setBigDecimal(3, codigoPkPlantilla);
			pst.setBigDecimal(4, codigoCita);
			 
			logger.info("\n\n Consulta cargarPlantillasRegistradas >>"+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				plantilla = new DtoPlantilla();
				plantilla.setCodigoPK(rs.getString("codigo_plantilla"));
				plantilla.setConsecutivoHistorico(rs.getString("codigo_plantilla_his"));
				plantilla.setNombre(rs.getString("nombre_plantilla"));
				plantilla.setNombreFuncionalidad(rs.getString("tipo"));
				if(UtilidadTexto.getBoolean(rs.getString("valoracion")))
				{
					plantilla.setCodigoValoracionOdontologia(rs.getBigDecimal("codigo"));
				}
				else
				{
					plantilla.setCodigoEvolucionOdontologia(rs.getBigDecimal("codigo"));
				}
				
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, null);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPlantillaSeleccionadaDeHistorico: ",e);
		}
		return plantilla;
	}
	
	
	
}
