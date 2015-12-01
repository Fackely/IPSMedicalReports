package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoPlanTratamiento;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAntecedentesAlerta;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetSuperficieIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetalleIndicePlaca;
import com.princetonsa.dto.odontologia.DtoFiltroConsultaServiciosPaciente;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicos;
import com.princetonsa.dto.odontologia.DtoParentesco;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;
import com.princetonsa.dto.odontologia.DtoTratamientoInterno;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.odontologia.NumeroSuperficiesPresupuesto;

public class SqlBaseUtilidadOdontologia 
{
	
	private static Logger logger = Logger.getLogger(SqlBaseUtilidadOdontologia.class);
	
	
	/**
	 * sentencia sql de consulta de indice de placa
	 * */
	private static final String strConsultaInterIndicePlaca = "SELECT " +
			"consecutivo," +
			"porcentaje_inicial," +
			"porcentaje_final," +
			"valor " +
			"FROM " +
			"odontologia.interpretacion_indice_placa " +
			"ORDER BY porcentaje_inicial ASC ";
	
	/**
	 * sentencia sql de inserciont tabla de indice de placa
	 * */
	private static final String strInsertarIndicePlaca = "INSERT INTO historiaclinica.comp_indice_placa( " +
			"codigo_pk," +
			"imagen," +
			"porcentaje," +
			"interpretacion," +
			"plantilla_ingreso," +
			"plantilla_evolucion_odo," +
			"fecha_modifica," +
			"hora_modifica," +
			"usuario_modifica," +
			"por_confirmar) VALUES" +
			"(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?) ";
	
	/**
	 * sentencia que actializa el componente indice de placa 
	 */
	private static final String strUpdateCompIndicePlaca = "UPDATE historiaclinica.comp_indice_placa SET " +
			"imagen = ? , " +
			"porcentaje = ? , " +
			"interpretacion = ? , " +
			"por_confirmar = ? , " +
			"fecha_modifica = CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" , " +
			"usuario_modifica = ? " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * sentencia sql de insercion de detalle componente indice placa
	 */
	private static final String strInsertDetalleCompIndicePlaca = "INSERT INTO historiaclinica.det_indice_placa (" +
			"codigo_pk," +
			"pieza," +
			"superficie," +
			"indicador," +
			"comp_indice_placa" +
			") VALUES (?,?,?,?,?) ";
	
	/**
	 * senetencia sql de eliminacion de detalle componente indice de placa
	 */
	private static final String strDeleteDetalleCompIndicePlaca = "DELETE FROM historiaclinica.det_indice_placa " +
			  "WHERE codigo_pk = ? ";
			//"WHERE comp_indice_placa = ?  AND pieza = ? ";
	
	/**
	 * senetencia sql de modificación de detalle componente indice de placa
	 */
	private static final String strUpdateDetalleCompIndicePlaca = "UPDATE historiaclinica.det_indice_placa SET indicador = ? WHERE codigo_pk = ? ";
	
	/**
	 * sentencia sql que consulta el indice de placa para la plantilla de valoracion odontologica (ultimo) 
	 */
	private static final String strConsultarComponenteIndicePlacaVal = "select distinct " + 
			"cip.codigo_pk as codigo,  " +
			"cip.imagen as path_img,  " +
			"cip.porcentaje as porcentaje, " + 
			"cip.interpretacion as interpretacion, " + 
			"coalesce(cip.plantilla_ingreso,"+ConstantesBD.codigoNuncaValido+") as plt_ingreso, " + 
			"coalesce(cip.plantilla_evolucion_odo,"+ConstantesBD.codigoNuncaValido+") as plt_evolucion, " + 
			"cip.por_confirmar as por_confirmar  " +
			"from historiaclinica.comp_indice_placa cip " +
			"WHERE " +
			"cip.plantilla_ingreso = ? " +
			"ORDER BY cip.codigo_pk DESC ";
	
	/**
	 * sentencia sql que consulta el indice de placa para la plantilla de valoracion odontologica (ultimo) 
	 */
	private static final String strConsultarComponenteIndicePlacaEvo = "select distinct " + 
			"cip.codigo_pk as codigo,  " +
			"cip.imagen as path_img,  " +
			"cip.porcentaje as porcentaje, " + 
			"cip.interpretacion as interpretacion, " + 
			"coalesce(cip.plantilla_ingreso,"+ConstantesBD.codigoNuncaValido+") as plt_ingreso, " + 
			"coalesce(cip.plantilla_evolucion_odo,"+ConstantesBD.codigoNuncaValido+") as plt_evolucion, " + 
			"cip.por_confirmar as por_confirmar  " +
			"from historiaclinica.comp_indice_placa cip " +
			"WHERE " +
			"cip.plantilla_evolucion_odo = ? " +
			"ORDER BY cip.codigo_pk DESC ";

	
	/**
	 * sentencia sql que consulta el detalle del indice de placa del componente
	 */
	private static final String strConsultarDetalleCompIndicePlaca = "select distinct " +  
			"dip.codigo_pk as codigo_ind_placa, " +
			"dip.comp_indice_placa as comp_ind_placa, " +
			"dip.pieza as pieza, " +
			"pd.descripcion as desp_pieza, " +
			"dip.indicador as indicador, " +
			"coalesce(dip.superficie,"+ConstantesBD.codigoNuncaValido+") as superficie, " +
			"coalesce(sd.nombre,' ') as nom_superficie, " +
			"ssc.sector as sector "+
			"from historiaclinica.det_indice_placa dip " +
			"INNER JOIN odontologia.pieza_dental pd ON (dip.pieza = pd.codigo_pk) " + 
			"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON (dip.superficie = sd.codigo and sd.cod_institucion = ? and sd.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+") " +
			"INNER JOIN odontologia.sector_superficie_cuadrante ssc ON (ssc.pieza = dip.pieza and ssc.superficie = dip.superficie) " +
			"where dip.comp_indice_placa = ? " +
			"group by dip.pieza, dip.superficie, sd.nombre, " +
			"dip.indicador, dip.codigo_pk, " +
			"dip.comp_indice_placa, pd.descripcion, ssc.sector  ORDER BY dip.pieza ";
	
	/**
	 * sentencia sql verifica si el paciente tiene plan de tratamiento
	 */
	private static final String strVerificarPlanTratamientoPac = "SELECT " +  
			//"CASE WHEN COUNT(lpt.plan_tratamiento)>0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS tiene_pt " +
			"lpt.plan_tratamiento as cod_plan_trat, " +
			"lpt.estado as estado_plan_trat " +
			"FROM odontologia.plan_tratamiento pt " +
			"INNER JOIN odontologia.log_plan_tratamiento lpt ON (lpt.plan_tratamiento = pt.codigo_pk) " + 
			"WHERE pt.codigo_paciente = ? " +
			"AND lpt.por_confirmar = '"+ConstantesBD.acronimoNo+"' " +
			"AND (" +
			"		lpt.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
			"		OR lpt.estado = '"+ConstantesIntegridadDominio.acronimoEnProceso+"' " +
			"		OR lpt.estado = '"+ConstantesIntegridadDominio.acronimoTerminado+"' " +
			"		OR lpt.estado = '"+ConstantesIntegridadDominio.acronimoInactivo+"' " +
			" ) " +
			"ORDER BY lpt.plan_tratamiento DESC ";

	/**
	 * sentencia sql que determina si un plan de tratamiento tiene servicios en garantia
	 */
	private static final String strCountServicioGarantiaPT = "SELECT " +
			"CASE WHEN COUNT(pspt.codigo_pk)>0 THEN 'S' ELSE 'N' END AS servici_garantia " +
			"FROM odontologia.plan_tratamiento pt " +
			"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.plan_tratamiento = pt.codigo_pk) " +
			"INNER JOIN odontologia.programas_servicios_plan_t pspt ON (pspt.det_plan_tratamiento = dpt.codigo_pk) " +
			"WHERE pt.codigo_pk = ? " +
			"AND pspt.garantia = ? ";
	
	/**
	 * sentencia sql que obtiene las piezas dentales
	 */
	private static final String strConsultarPiezasDentales = "select " +
			"codigo_pk, " +
			"descripcion, " +
			"edad_aplica  " +
			"from odontologia.pieza_dental ";
	
	/**
	 * sentencia sql que obtiene el ultimo antecedente odontologico (esto para los nuevos antecedentes)
	 */
	private static final String strConsultaAntecedenteOdon = "SELECT " +
			"hao.codigo_pk as codigo, " +
			"hao.codigo_paciente as cod_paciente, " +
			"hao.ingreso as ingreso, " +
			"hao.centro_atencion as centro_aten, " +
			"hao.institucion as institucion,  " +
			"hao.codigo_medico as cod_medico, " +
			"coalesce(hao.especialidad,"+ConstantesBD.codigoNuncaValido+") as especialidad, " +
			"hao.por_confirmar as por_confirmar, " +
			"hao.observaciones as observaciones, " +
			"hao.mostrar_por as mostrar_por, " +
			"coalesce(hao.valoracion_odo,"+ConstantesBD.codigoNuncaValido+") as valoracion," +
			"coalesce(hao.evolucion_odo,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +
			"MAX(hao.fecha_modifica) as fecha_mod, " +
			"MAX(hao.hora_modifica) as hora_mod, " +
			"hao.usuario_modifica as usu_mod " +
			"FROM historiaclinica.his_antecedentes_odo hao " +
			"WHERE hao.codigo_paciente = ? AND hao.por_confirmar= '"+ConstantesBD.acronimoNo+"' " +
			"GROUP BY hao.codigo_pk, hao.codigo_paciente, hao.ingreso, " +
			"hao.centro_atencion, hao.institucion, hao.codigo_medico, " +
			"hao.especialidad, hao.por_confirmar, hao.observaciones, " +
			"hao.mostrar_por, hao.valoracion_odo, hao.evolucion_odo, " +
			"hao.fecha_modifica, hao.hora_modifica, hao.usuario_modifica " +
			"ORDER BY hao.codigo_pk DESC ";
	
	/**
	 * sentencia sql que obtiene el ultimo antecedente odontologico (esto para el antecedente ya existente)
	 */
	private static final String strConsultaAntecedenteOdonExistente = "SELECT " +
			"hao.codigo_pk as codigo, " +
			"hao.codigo_paciente as cod_paciente, " +
			"hao.ingreso as ingreso, " +
			"hao.centro_atencion as centro_aten, " +
			"hao.institucion as institucion,  " +
			"hao.codigo_medico as cod_medico, " +
			"coalesce(hao.especialidad,"+ConstantesBD.codigoNuncaValido+") as especialidad, " +
			"hao.por_confirmar as por_confirmar, " +
			"hao.observaciones as observaciones, " +
			"hao.mostrar_por as mostrar_por, " +
			"coalesce(hao.valoracion_odo,"+ConstantesBD.codigoNuncaValido+") as valoracion," +
			"coalesce(hao.evolucion_odo,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +
			"hao.fecha_modifica as fecha_mod, " +
			"hao.hora_modifica as hora_mod, " +
			"hao.usuario_modifica as usu_mod " +
			"FROM historiaclinica.his_antecedentes_odo hao ";
	
	
	
	/**
	 * Sentencia Sql para consultar los antecendentes Odontologicos asociados a un paciente
	 */
	private static final String strConsultaAntecedentesOdontologicos = "SELECT " +
			"hao.codigo_pk as codigo, " +
			"hao.codigo_paciente as cod_paciente, " +
			"hao.ingreso as ingreso, " +
			"hao.centro_atencion as centro_aten, " +
			"hao.institucion as institucion,  " +
			"hao.codigo_medico  as  cod_medico, " +
			"administracion.getNombrePersona2(hao.codigo_medico) AS nombresmedico, " +
			"coalesce(hao.especialidad,"+ConstantesBD.codigoNuncaValido+") as especialidad, " +
			"hao.por_confirmar as por_confirmar, " +
			"hao.observaciones as observaciones, " +
			"hao.mostrar_por as mostrar_por, " +
			"coalesce(hao.valoracion_odo,"+ConstantesBD.codigoNuncaValido+") as valoracion," +
			"coalesce(hao.evolucion_odo,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +
			"hao.fecha_modifica as fecha_mod, " +
			"hao.hora_modifica as hora_mod, " +
			"hao.usuario_modifica as usu_mod, " +
			"hao.hora_modifica, " +
			"hao.fecha_modifica||' '||hao.hora_modifica AS  fecha_hora " +
			"FROM historiaclinica.his_antecedentes_odo hao " +
			"WHERE hao.codigo_paciente = ? AND hao.por_confirmar = '"+ConstantesBD.acronimoNo+"'  " +
			"AND 1=1 " +
			"ORDER BY fecha_hora DESC ";
			
	
	/**
	 * Sentencia Sql para consultar los Tratamientos Internos de los Atecedentes 
	 * de un Paciente por Programas 
	 */
	private static final String strConsultaTratamientosOdontInternosAntXProgramas = "SELECT DISTINCT " +
			"pspt.programa AS codprograma , " +
			"pr.nombre AS nombreprograma ," +
			"haoti.antecedente_odo AS antecedenteodo ," +
			"haoti.prog_serv_plan_trat AS codprog_serv_plan_trat , " +
			"coalesce(to_char(haoti.fecha_inicio,'"+ConstantesBD.formatoFechaAp+"'),' ') as fechainicio, " +
			"coalesce(to_char(haoti.fecha_final,'"+ConstantesBD.formatoFechaAp+"'),' ') as fechafinal, " +
			"haoti.pieza_dental AS codpiezadental, " +
			"coalesce(pd.descripcion,' ') as descrip_pieza, " +
			"haoti.especialidad AS codespecialidad, " +
			"coalesce(esp.nombre,' ') as nombre_esp " +			
			"FROM  historiaclinica.his_ant_odo_trat_int haoti " +
			"INNER JOIN historiaclinica.his_antecedentes_odo hao ON (hao.codigo_pk = haoti.antecedente_odo) " +
			"INNER JOIN odontologia.programas_servicios_plan_t pspt ON ( pspt.codigo_pk = haoti.prog_serv_plan_trat) " +
			"INNER JOIN odontologia.programas pr  ON (pr.codigo= pspt.programa) "+
			"LEFT OUTER JOIN odontologia.pieza_dental pd  ON (pd.codigo_pk = haoti.pieza_dental) " +
			"LEFT OUTER JOIN administracion.especialidades esp ON (esp.codigo = haoti.especialidad ) " +
			"WHERE haoti.antecedente_odo = ? " +
			"GROUP BY pspt.programa, pr.nombre, haoti.antecedente_odo, haoti.prog_serv_plan_trat, haoti.fecha_inicio, " +
			"haoti.fecha_final, haoti.pieza_dental, haoti.especialidad, esp.nombre, pd.descripcion "; 
			 
	         
	   
	
	/**
	 * Sentencia Sql para consultar los Tratamientos Internos de los Atecedentes Odontologicos
	 * de un paciente  por servicios
	 */
	 private static final String strConsultaTratamientosOdontInternosAntXServicios = "SELECT DISTINCT " +
			"pspt.servicio AS codservicio , " +
			"getnombreservicio(pspt.servicio, "+ConstantesBD.codigoTarifarioCups+")  AS nombreservicio , "+
			"haoti.antecedente_odo AS antecedenteodo ," +
			"haoti.prog_serv_plan_trat AS codprog_serv_plan_trat , " +
			"coalesce(to_char(haoti.fecha_inicio,'"+ConstantesBD.formatoFechaAp+"'),' ') as fechainicio, " +
			"coalesce(to_char(haoti.fecha_final,'"+ConstantesBD.formatoFechaAp+"'),' ') as fechafinal, " +
			"haoti.pieza_dental AS codpiezadental, " +
			"haoti.especialidad AS codespecialidad, " +
			"coalesce(esp.nombre,' ') as nombre_esp, " +
			"coalesce(pd.descripcion,' ') as descrip_pieza " +
			"FROM  historiaclinica.his_ant_odo_trat_int haoti " +
			"INNER JOIN historiaclinica.his_antecedentes_odo hao ON (hao.codigo_pk = haoti.antecedente_odo) " +
			"INNER JOIN odontologia.programas_servicios_plan_t pspt ON ( pspt.codigo_pk = haoti.prog_serv_plan_trat) " +
			"LEFT OUTER JOIN odontologia.pieza_dental pd  ON (pd.codigo_pk = haoti.pieza_dental) " +
			"LEFT OUTER JOIN administracion.especialidades esp ON (esp.codigo = haoti.especialidad ) " +
			"WHERE haoti.antecedente_odo = ? " +
			"GROUP BY haoti.antecedente_odo, haoti.prog_serv_plan_trat, haoti.fecha_inicio, " +
			"haoti.fecha_final, haoti.pieza_dental, haoti.especialidad, esp.nombre, pd.descripcion, pspt.servicio "; 
		     
	/**
	 * sentencia sql que consulta los tratamientos externos
	 */
	private static final String strConsultarTratamientoExterno = "SELECT " +
			"haote.codigo_pk as codigo, " +
			"haote.antecedente_odo as antece_odo, " +
			"coalesce(to_char(haote.fecha_inicio,'"+ConstantesBD.formatoFechaAp+"'),' ') as fecha_ini, " +
			"coalesce(to_char(haote.fecha_final,'"+ConstantesBD.formatoFechaAp+"'),' ') as fecha_fin, " +
			"coalesce(haote.programa_servicio,' ') as prog_serv, " +
			"coalesce(haote.pieza_dental,"+ConstantesBD.codigoNuncaValido+") as pieza_den, " +
			"coalesce(pd.descripcion,' ') as desp_pieza, " +
			"coalesce(haote.especialidad,"+ConstantesBD.codigoNuncaValido+") as especialidad, " +
			"coalesce(esp.nombre,' ') as nombre_esp " +
			"FROM his_ant_odo_trat_ext haote " +
			"LEFT OUTER JOIN odontologia.pieza_dental pd  ON (pd.codigo_pk = haote.pieza_dental) " +
			"LEFT OUTER JOIN administracion.especialidades esp ON (esp.codigo = haote.especialidad ) " +
			"WHERE haote.antecedente_odo = ? ORDER BY haote.fecha_final  DESC ";
	
	/**
	 * sentencia sql actualizar antecedente odontologico
	 */
	private static final String strUpdateAntecedenteOdontologico = "UPDATE historiaclinica.his_antecedentes_odo SET " +
			"codigo_medico = ? , " +
			"por_confirmar = ? , " +
			"observaciones = ? , " +
			"mostrar_por = ? " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * sentencia sql eliminar tratamientos internos
	 */
	private static final String strDeleteTratamientosInternos = "DELETE " +
			"FROM historiaclinica.his_ant_odo_trat_int " +
			"WHERE antecedente_odo = ? ";
	
	/**
	 * sentencia sql que elimina un tratamiento externos 
	 */
	private static final String strDeleteTratamientoExterno = "DELETE " +
			"FROM historiaclinica.his_ant_odo_trat_ext " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * sentencia sql que modifica un tratamiento externo 
	 */
	private static final String strUpdateTratamientoExterno = "UPDATE historiaclinica.his_ant_odo_trat_ext SET " +
			"fecha_inicio = ? , " +
			"fecha_final = ? , " +
			"programa_servicio = ? , " +
			"pieza_dental = ? , " +
			"especialidad = ?  " +
			"WHERE codigo_pk = ? ";
	/**
	 * sentencia sql que inserta un antecedente odontologico
	 */
	private static final String strInsertHisAntecedenteOdonto = "INSERT INTO historiaclinica.his_antecedentes_odo (" +
			"codigo_pk," +
			"codigo_paciente," +
			"ingreso," +
			"centro_atencion," +
			"institucion," +
			"codigo_medico," +
			"especialidad," +
			"por_confirmar," +
			"observaciones," +
			"mostrar_por," +
			"valoracion_odo," +
			"evolucion_odo," +
			"usuario_modifica," +
			"fecha_modifica," +
			"hora_modifica" +
			")VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	/**
	 * sentencia sql que inserta tratamientos externos
	 */
	private static final String strInsertHisAntOdontoTratamientoExterno = "INSERT INTO historiaclinica.his_ant_odo_trat_ext (" +
			"codigo_pk," +
			"antecedente_odo," +
			"fecha_inicio," +
			"fecha_final," +
			"programa_servicio," +
			"pieza_dental," +
			"especialidad" +
			") VALUES (?,?,?,?,?,?,?)";
	
	/**
	 * sentencia sql que inserta tratamientos internos 
	 */
	private static final String strInsertHisAntOdontoTratamientoInterno = "INSERT INTO historiaclinica.his_ant_odo_trat_int (" +
			"antecedente_odo," +
			"prog_serv_plan_trat," +
			"fecha_inicio," +
			"fecha_final," +
			"pieza_dental," +
			"especialidad" +
			") VALUES (?,?,?,?,?,?)";
	
	private static final String sqlConsultarProfesionalTieneAgendaGenerada = " SELECT SUM(t.cuenta) AS total " +
								"FROM (" +
								"(SELECT COUNT(1) AS cuenta " +
								"FROM odontologia.agenda_odontologica ao " +
								"WHERE ao.codigo_medico           = ? " +
								"AND (TO_CHAR(ao.fecha,'yyyy-mm-dd') > ? OR (TO_CHAR(ao.fecha,'yyyy-mm-dd') = ? AND ao.hora_inicio >= ?)) " +
								"AND ao.cupos                       > 0 " +
								"AND ao.activo                      = ? " +
								") " +
								"UNION " +
								"(SELECT COUNT(1) AS cuenta " +
								"FROM odontologia.agenda_odontologica ao " +
								"INNER JOIN odontologia.citas_odontologicas co " +
								"ON (ao.codigo_pk              = co.agenda) " +
								"WHERE ao.codigo_medico           = ? " +
								"AND (TO_CHAR(ao.fecha,'yyyy-mm-dd') > ? OR (TO_CHAR(ao.fecha,'yyyy-mm-dd') = ? AND ao.hora_inicio >= ?)) " +
								"AND co.estado                     IN (?,?) " +
								"AND ao.activo                      = 'S' " +
								") ) t";
	
	/**
	 * Cadena para confirmar el indice de placa
	 */
	private static final String sqlConfirmarIndicePlaca = "UPDATE historiaclinica.comp_indice_placa set por_confirmar = ?, usuario_modifica = ?, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE ";
	
	/**
	 * Inserta el indice de placa
	 * @param DtoIndicePlaca dto
	 * */
	public static int insertarIndicePlaca(Connection con, DtoComponenteIndicePlaca dto)
	{
		try
		{
			String nombreImagen = "";
			int secuencia = Utilidades.getSiguienteValorSecuencia(con,"historiaclinica.seq_comp_indice_placa");
			
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			logger.info("Imagen Dto: "+dto.getImagen());
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			
			if(dto.getPlantillaIngreso()>0)
				nombreImagen = "indicePlacaDental_"+dto.getPlantillaIngreso()+".jpg";
			else if (dto.getPlantillaEvolucionOdo()>0)
				nombreImagen = "indicePlacaDental_"+dto.getPlantillaEvolucionOdo()+".jpg";
			
			logger.info("nombreImagen: "+nombreImagen);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarIndicePlaca,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,secuencia);
			ps.setString(2,nombreImagen);
			ps.setDouble(3,dto.getPorcentaje().doubleValue());
			ps.setString(4,dto.getInterpretacion());
			
			if(dto.getPlantillaIngreso()>0)
				ps.setInt(5,dto.getPlantillaIngreso());
			else
				ps.setNull(5,Types.INTEGER);
			
			if(dto.getPlantillaEvolucionOdo()>0)
				ps.setInt(6,dto.getPlantillaEvolucionOdo());
			else
				ps.setNull(6,Types.INTEGER);
			
			ps.setString(7,dto.getUsuarioModifica());
			ps.setString(8, dto.getPorConfirmar());
			
			if(ps.executeUpdate()>0)
			{
				// borrar el archivo de generacion inicial
				
				logger.info("estoy renombrendo la imagen de "+CarpetasArchivos.IMAGENES_INDICEPLACA.getRutaFisica()+""+dto.getImagen()+".jpg");
				logger.info("a "+dto.getImagen()+".jpg");
				
				if(UtilidadFileUpload.generarCopiaArchivoLibre(CarpetasArchivos.IMAGENES_INDICEPLACA.getRutaFisica(),dto.getImagen()+".jpg", 
						CarpetasArchivos.IMAGENES_INDICEPLACA.getRutaFisica(), nombreImagen))
					UtilidadFileUpload.borrrarArchivo(dto.getImagen()+".jpg", CarpetasArchivos.IMAGENES_INDICEPLACA.getRutaFisica());
				dto.setCodigoPk(secuencia);
				dto.setImagen(nombreImagen);
				return secuencia;
			}
		}
		catch (Exception e) {
			logger.info("error ==> "+e.toString());
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consulta las interpretaciones de indice de placa, la informacion la devuelve como lenguaje de marcas (XML)
	 * */
	public static String cargarInterpretacionIndicePlaca()
	{
		String resultadoXML = "";
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaInterIndicePlaca,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				resultadoXML += "<Rango>" +
									"<consecutivo>"+rs.getString("consecutivo")+"</consecutivo>" +
									"<rangoinicial>"+rs.getString("porcentaje_inicial")+"</rangoinicial>" +
									"<rangofinal>"+rs.getString("porcentaje_final")+"</rangofinal>" +
									"<descripcion>"+rs.getString("valor")+"</descripcion>" +
								"</Rango>";
			}
			
			if(!resultadoXML.equals(""))
				resultadoXML = "<Rangos>"+resultadoXML+"</Rangos>";
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (Exception e) {
			logger.info("error ==> "+e.toString());
		}
		logger.info("Rangos: "+resultadoXML);
		return resultadoXML;
	}
	
	
	/**
	 * Metodo para cargar la lista de Parentezcos 
	 * @param dto recibe un tipo Parentezco
	 * @return
	 */
	
	public static ArrayList<DtoParentesco> cargar( DtoParentesco dto){
	 
		ArrayList<DtoParentesco> arrayDto= new ArrayList<DtoParentesco>();
		String consultaStr=" select  codigo_pk as codigo, " +
									"descripcion as descripcion" +
									" from  odontologia.parentezco " +
									" where " +
									"1=1 ";
		
		consultaStr+=(dto.getCodigoPk()>0)?"codigo_pk="+dto.getCodigoPk():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getDescripcion())?"":"descripcion='"+dto.getDescripcion()+"'";
				
		
		logger.info(consultaStr);
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoParentesco newdto = new DtoParentesco();
				newdto.setCodigoPk(rs.getInt("codigo"));
				newdto.setDescripcion(rs.getString("descripcion"));
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}
	
	/**
	 * metodo que verifica si el paciente posee plan de tratamiento 
	 * @param codigoPaciente
	 * @return String
	 */
	public static ArrayList<InfoDatosInt> tienePlanTratamientoPaciente(int codigoPaciente)
	{
		ArrayList<InfoDatosInt> array = new ArrayList<InfoDatosInt>();
		try{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strVerificarPlanTratamientoPac);
			ps.setInt(1, codigoPaciente);
			logger.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt dato = new InfoDatosInt(rs.getInt("cod_plan_trat"),rs.getString("estado_plan_trat"));
				array.add(dato);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			logger.error("Error", e);
		}
		return array;
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServicios(DtoFiltroConsultaServiciosPaciente filtro) 
	{
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("codigoInstitucion", filtro.getInstitucion());
		parametros.put("unidadConsulta", filtro.getUnidadAgenda());
		parametros.put("codigoMedico", filtro.getCodigoMedico());
		parametros.put("codigoPaciente",filtro.getCodigoPaciente());
		parametros.put("tipoServicio",filtro.getTipoServicio());
		parametros.put("activos", filtro.isActivo());
		parametros.put("validaPresCont",filtro.getValidadrPresupuestoContratado());
		parametros.put("cambiarSerOdo", filtro.getCambiarServicioOdontologico());
		parametros.put("casoBusSer",filtro.getCasoBusquedaServicio());
		parametros.put("codigoCitaNoVinculado",filtro.getCodigoCita());
		parametros.put("buscarPlanTratamiento",filtro.isBuscarPlanTratamiento());
		parametros.put("ordenarPorPHP", filtro.isOrdenarPorPHP());
		
		
		return obtenerServicios(parametros);
	}
	
	/**
	 * M&eacute;todo que carga los servicios odont&oacute;logicos. Si se envia el c&oacute;digo y tipo de servicio, 
	 * solo trae el registro correspondiente a ese servicio.
	 * 
	 * @param codigoInstitucion
	 * @param unidadConsulta
	 * @param codigoMedico
	 * @param codigoPaciente
	 * @param tipoServicio
	 * @param activos
	 * @param validaPresCont
	 * @param cambiarSerOdo
	 * @param casoBusSer
	 * @param codigoCita cita a la cual no deben estar asociados los servicios
	 * @param buscarPlanTratamiento
	 * @param codigoServicio
	 * @param codigosCitasVinculado - Codigo de la cita (s) con las que deben estar vinculados los servicios.
	 * @param ordenarPorPHP  par&aacute;metro que indica si se debe ordenar por ProgramaHallazgoPieza
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServicios(HashMap<String, Object> parametros)
	{

		int codigoInstitucion = (Integer) parametros.get("codigoInstitucion");
		int unidadConsulta = (Integer) parametros.get("unidadConsulta");
		int codigoMedico = (Integer) parametros.get("codigoMedico");
		int codigoPaciente = (Integer) parametros.get("codigoPaciente");
		String tipoServicio = (String) parametros.get("tipoServicio");
		boolean activos = (Boolean) parametros.get("activos");
		String validaPresCont = (String) parametros.get("validaPresCont");
		String cambiarSerOdo = (String) parametros.get("cambiarSerOdo");
		String casoBusSer = (String) parametros.get("casoBusSer");
		int codigoCitaNoVinculado = (Integer) parametros.get("codigoCitaNoVinculado");
		boolean buscarPlanTratamiento = (Boolean) parametros.get("buscarPlanTratamiento");
		
		String codigosServiciosNoIncluir = "";
		
		if(parametros.get("codigosServiciosNoIncluir")!=null && parametros.get("codigosServiciosNoIncluir")!=""){
			
			codigosServiciosNoIncluir = (String) parametros.get("codigosServiciosNoIncluir");
		}
		
		String codigoServicio = "";
		
		if(parametros.get("codigosServicios")!=null){
			
			codigoServicio = (String) parametros.get("codigosServicios");
		}
		
		String isServicioVinculadoCita = "";
		
		if(parametros.get("isServicioVinculadoCita")!=null){
			
			isServicioVinculadoCita = (String) parametros.get("isServicioVinculadoCita");
		}

		String estadosCitasVinculado = (String) parametros.get("estadosCitasVinculado");
		
		if(estadosCitasVinculado==null || estadosCitasVinculado.isEmpty()){
			
			estadosCitasVinculado = " '"+ConstantesIntegridadDominio.acronimoReservado+"' , '"+ConstantesIntegridadDominio.acronimoAsignado+"' , '"+ConstantesIntegridadDominio.acronimoReprogramado+"' ";
		}

		boolean ordenarPorPHP = (Boolean) parametros.get("ordenarPorPHP");
		

		ArrayList<DtoServicioOdontologico> array = new ArrayList<DtoServicioOdontologico>();
		ArrayList<InfoDatosInt> dato = new ArrayList<InfoDatosInt>();
		String casoBusquedaSer = "";
		boolean band = false;
		
		String strConsultaServicios = "SELECT DISTINCT " + 
										"ser.codigo AS codigo_servicio, " +
										"ser.tipo_servicio as codigo_tipo_ser, " +
										" getcodigopropservicio(ser.codigo, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)+")||' '||getnombreservicio(ser.codigo,?) AS descripcion_servicio, " +
										"coalesce(ser.minutos_duracion,0) AS duracion, " +
										"ser.especialidad AS especialidad ";
		
		//consultaAux: tiene los servicios de la unidad de consulta y del profesional de la salud.
		String consultaAux = strConsultaServicios;
		String consulta = strConsultaServicios;
		String tablas = "";
		String where = "WHERE ";
		String groupby = "";
		String orderby = "";
		
		try{
			
			consultaAux+=", '"+ConstantesBD.acronimoSi+"' as habilitar_seleccion ";
			consultaAux+= "FROM facturacion.servicios ser " +
					"LEFT OUTER JOIN consultaexterna.servicios_unidades_consulta suc ON (suc.codigo_servicio = ser.codigo) " +
					"LEFT OUTER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = suc.unidad_consulta) " +
					"LEFT OUTER JOIN odontologia.serv_adicionales_profesionales sap ON (sap.codigo_servicio = ser.codigo ) ";
			
			
			consultaAux += where+" ((uc.codigo = "+unidadConsulta+") OR (sap.codigo_medico = "+codigoMedico+" AND sap.institucion = "+codigoInstitucion+"))  ";
			

			if(!"".equals(tipoServicio))
			{
				consultaAux += " AND ser.tipo_servicio IN ("+tipoServicio+") ";
			}
			
			if(activos)
			{
				consultaAux += " AND ser.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			}
			
			if(!UtilidadTexto.isEmpty(codigoServicio)){
				
				consultaAux += " AND ser.codigo IN ( "+codigoServicio+" )";			
			}
			
			if(!UtilidadTexto.isEmpty(codigosServiciosNoIncluir)){
				
				consultaAux += " AND ser.codigo NOT IN ( "+codigosServiciosNoIncluir+" )";			
			}

			if((casoBusSer.equals("") || cambiarSerOdo.equals(ConstantesBD.acronimoNo)))
			{
				if(buscarPlanTratamiento)
				{
					dato = tienePlanTratamientoPaciente(codigoPaciente);
				}
				if(dato.size()<=0)
				{
					consulta=consultaAux;
					casoBusquedaSer = ConstantesBD.acronimoSinPlanTratamiento;
				}
				else
				{
					if(dato.get(0).getNombre().equals(ConstantesIntegridadDominio.acronimoEstadoActivo)||dato.get(0).getNombre().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso))
					{
						if(validaPresCont.equals(ConstantesBD.acronimoSi))
						{
							/*
							 * Debe validar si el presupuesto esta en estado Contratado, para mostrar sólo los servicios en estado Contratado de ese presupuesto
							 * si el presupuesto esta en otro estado o no existe debe mostrar los servicios de la unidad de agenda y las validaciones de paciente sin plan
							 * de tratamiento
							*/
							
							String consultaPresupuesto="SELECT count(po.codigo_pk) AS numReg FROM  odontologia.plan_tratamiento pt " + 
														"INNER JOIN odontologia.presupuesto_odontologico po ON (po.codigo_paciente = pt.codigo_paciente ) " +
														"WHERE pt.codigo_paciente=? AND po.estado= '"+ConstantesIntegridadDominio.acronimoContratadoContratado+"'";
							
							Connection con = UtilidadBD.abrirConexion();
							PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaPresupuesto);
							ps.setInt(1, codigoPaciente);
							ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
							
							logger.info("\n\npresupuesto: "+ps);
							
							if(rs.next()){
								if(rs.getInt("numReg") > 0)
								{
									consulta= "SELECT DISTINCT ser.codigo AS codigo_servicio, " +
											"ser.tipo_servicio AS codigo_tipo_ser, " +
											"  getcodigopropservicio(ser.codigo, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)+")|| ' '||getnombreservicio(ser.codigo,?) AS descripcion_servicio, " +
											"ser.minutos_duracion AS duracion, " +
											"ser.especialidad AS especialidad ";
									
									//VALIDA si el servicio esta en los sericios de la unidad de consulta o los servicios adicionales del profesional para habilitarlo en la seccion.
									consulta += ",CASE WHEN ser.codigo IN ( " +
										"	SELECT DISTINCT  " +
										"	ser.codigo " +
									    "	FROM facturacion.servicios ser " +
										"	LEFT OUTER JOIN consultaexterna.servicios_unidades_consulta suc ON (suc.codigo_servicio = ser.codigo) " +
										"	LEFT OUTER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = suc.unidad_consulta) " +
										"	LEFT OUTER JOIN odontologia.serv_adicionales_profesionales sap ON (sap.codigo_servicio = ser.codigo ) " +
										"	WHERE ((uc.codigo = "+unidadConsulta+" ) OR (sap.codigo_medico = "+codigoMedico+" AND sap.institucion = "+codigoInstitucion+" )) ";
									
									if(!"".equals(tipoServicio)){
										
										consulta += "	AND ser.tipo_servicio IN ("+tipoServicio+")  ";
									}
									
									if(activos)
									{
										consulta+= "AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  ";
									}
									consulta+= ") THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS habilitar_seleccion, " +
											"(SELECT ao.fecha " +
												"FROM odontologia.servicios_cita_odontologica sco " +
												"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
													"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
													"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
												"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
												"WHERE sco.programa_hallazgo_pieza = php.codigo_pk and sco.servicio=pspt.servicio ";
									if(codigoCitaNoVinculado>0)
									{
										consulta+="AND co.codigo_pk!= "+codigoCitaNoVinculado+" ";
									}
									
//									if(!UtilidadTexto.isEmpty(codigosCitasVinculado))
//									{
//										consulta+="AND co.codigo_pk IN ( "+codigosCitasVinculado+" ) ";
//									}
//									
									
									consulta+=	ValoresPorDefecto.getValorLimit1()+" 1) " +
											"AS fecha_cita, " +
											"(SELECT co.hora_inicio " +
												"FROM odontologia.servicios_cita_odontologica sco " +
												"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
													"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
													"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
												"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
												" WHERE sco.programa_hallazgo_pieza = php.codigo_pk  and sco.servicio=pspt.servicio  " ;
									
									if(codigoCitaNoVinculado>0){
										consulta+="AND co.codigo_pk!= "+codigoCitaNoVinculado+" ";
									}
									 
//									if(!UtilidadTexto.isEmpty(codigosCitasVinculado))
//									{
//										consulta+="AND co.codigo_pk IN ( "+codigosCitasVinculado+" ) ";
//									}
									
									consulta+=ValoresPorDefecto.getValorLimit1()+" 1) " +
											"AS hora_cita, " +
											"(SELECT CASE WHEN  COUNT( sco.codigo_pk)>0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END " +
													" FROM odontologia.servicios_cita_odontologica sco " +
													" INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
													//" (co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' OR co.estado= '"+ConstantesIntegridadDominio.acronimoReprogramado+"')) " +
													" (co.estado IN ("+estadosCitasVinculado+"))) " +
													
													" WHERE sco.programa_hallazgo_pieza = php.codigo_pk  and sco.servicio=pspt.servicio AND sco.activo = '"+ConstantesBD.acronimoSi+"' " ;
									
									if(codigoCitaNoVinculado>0)
						    		{
										consulta+="AND co.codigo_pk!= "+codigoCitaNoVinculado+" ";
								    }								 
									    
//									if(!UtilidadTexto.isEmpty(codigosCitasVinculado))
//									{
//										consulta+="AND co.codigo_pk IN ( "+codigosCitasVinculado+" ) ";
//									}
									
								    consulta+=	") as esta_vin_ser, "+
											"pt.estado AS estado_plan_trata, " +
											"pspt.garantia AS garantia_ser, " +
											//"pspt.codigo_pk AS codigo_pspt, " +
											"pden.descripcion AS desc_pieza_dental, " +
											"pden.edad_aplica AS edad_apli_pd, " +
											"pspt.orden_servicio AS orden_servicio, " +
											"coalesce(prog.codigo,-1) AS cod_programa, " +
											"coalesce(prog.nombre,' ') AS nombre_prog, " +
											"coalesce(prog.codigo_programa,' ') AS sigla_programa, " +
											"dpt.seccion AS seccion, " +
											"dpt.pieza_dental AS cod_pieza_dental, " +
											"php.codigo_pk AS codigo_hallazgo_pieza," +
											"php.color_letra as color_letra ";
											//"dpt.superficie AS superficie "; 
									
									//FIXME EVALUAR LO DE INCLUSIONES EN ESTE METODO PUES ES DIFICIL DETERMINARLO EN ESTA "COSA"    
									    
									tablas = "FROM odontologia.plan_tratamiento pt " +
											"INNER JOIN odontologia.presupuesto_odontologico po ON (po.plan_tratamiento = pt.codigo_pk) " +
											"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON (pops.presupuesto = po.codigo_pk) " +
											"INNER JOIN odontologia.presupuesto_piezas pp ON (pp.presupuesto_odo_prog_serv = pops.codigo_pk) " +
											"INNER JOIN odontologia.programas_hallazgo_pieza php on (php.plan_tratamiento=pt.codigo_pk) " +
											"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
											"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
											"INNER JOIN  odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and pspt.programa=php.programa AND pspt.activo = 'S' ) " +
											"LEFT OUTER JOIN odontologia.pieza_dental pden ON (pden.codigo_pk = dpt.pieza_dental) " +
											"LEFT OUTER JOIN odontologia.programas prog ON ( prog.codigo = pspt.programa AND prog.institucion = 2 AND prog.activo = 'S') " +
											"LEFT OUTER JOIN facturacion.servicios ser ON (ser.codigo = pspt.servicio) ";
									
									where = "WHERE pt.codigo_pk = "+dato.get(0).getCodigo()+" " +
									
										"AND po.estado = '"+ConstantesIntegridadDominio.acronimoContratadoContratado+"' " + 
										"AND po.institucion = "+codigoInstitucion+" " + 
										"AND pspt.estado_servicio = '"+ConstantesIntegridadDominio.acronimoContratado+"' ";
									
									if(!UtilidadTexto.isEmpty(codigoServicio)){
										
										where += " AND ser.codigo IN ( "+codigoServicio+" )";
									}
									
									if(!UtilidadTexto.isEmpty(codigosServiciosNoIncluir)){
										
										where += " AND ser.codigo NOT IN ( "+codigosServiciosNoIncluir+" )";			
									}
									
									groupby = "GROUP BY ser.codigo, prog.codigo, dpt.seccion, dpt.pieza_dental, pspt.orden_servicio, " +
											"ser.codigo,ser.especialidad, ser.tipo_servicio, ser.minutos_duracion, prog.nombre, " +
											"prog.codigo_programa, pden.descripcion, pden.edad_aplica, pspt.garantia,pspt.servicio, " +
											"pt.estado, pops.codigo_pk, php.codigo_pk,php.color_letra ";
									
									if(ordenarPorPHP)
										orderby = "ORDER BY nombre_prog,codigo_hallazgo_pieza,desc_pieza_dental,seccion,pden.descripcion,orden_servicio, descripcion_servicio ";
									else
										orderby = "ORDER BY nombre_prog,desc_pieza_dental,seccion,pden.descripcion,orden_servicio, descripcion_servicio ";
									
									consulta+=tablas+where+groupby+orderby;
									casoBusquedaSer = ConstantesBD.acronimoConPlanTrataActivoEnProceso;
									band = true;
								}
								else
								{
									consulta=consultaAux;
								}
							}
							
							rs.close();
							ps.close();
							UtilidadBD.closeConnection(con);
							
						}else if(validaPresCont.equals(ConstantesBD.acronimoNo)){ //Acá se organizó la validación para cuando la validaPresCont esta vacio. Este cambio s ehace para la tarea 142612. Según lo consultado con Sebastián, si validaPresCont llega vacio no se debe permitir asignar la cita   
							
							logger.info("listar servicios al plan de tratamiento que se encuentren en estado pendiente");
							consulta += ",CASE WHEN ser.codigo IN ( " +
								"	SELECT DISTINCT  " +
								"	ser.codigo " +
							    "	FROM facturacion.servicios ser " +
								"	LEFT OUTER JOIN consultaexterna.servicios_unidades_consulta suc ON (suc.codigo_servicio = ser.codigo) " +
								"	LEFT OUTER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = suc.unidad_consulta) " +
								"	LEFT OUTER JOIN odontologia.serv_adicionales_profesionales sap ON (sap.codigo_servicio = ser.codigo ) " +
								"	WHERE ((uc.codigo = "+unidadConsulta+" ) OR (sap.codigo_medico = "+codigoMedico+" AND sap.institucion = "+codigoInstitucion+" )) ";
							
							if(!"".equals(tipoServicio)){
								
								consulta += " AND ser.tipo_servicio IN ("+tipoServicio+") ";
							}
							
							if(activos){
								consulta+= " AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  ";
							}
							
							consulta+= ") THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS habilitar_seleccion," +
										"(SELECT ao.fecha " +
											"FROM odontologia.servicios_cita_odontologica sco " +
											"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
												"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
												"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
											"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
											"WHERE sco.programa_hallazgo_pieza = php.codigo_pk   and sco.servicio=pspt.servicio  "; 												
						   
						   if(codigoCitaNoVinculado>0){
							   consulta+="AND co.codigo_pk!= "+codigoCitaNoVinculado+" ";
						   }
//						   
//						   if(!UtilidadTexto.isEmpty(codigosCitasVinculado)){
//							   consulta+="AND co.codigo_pk IN ( "+codigosCitasVinculado+" ) ";
//						   }
											
						   consulta+= ") AS fecha_cita, (SELECT co.hora_inicio " +
										"FROM odontologia.servicios_cita_odontologica sco " +
										"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
											"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
											"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
										"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
										"WHERE sco.programa_hallazgo_pieza = php.codigo_pk  and sco.servicio=pspt.servicio  " ;
						   
						   if(codigoCitaNoVinculado>0){
							   
							   consulta+="AND co.codigo_pk!= "+codigoCitaNoVinculado+" ";
						   }
//						   
//						   if(!UtilidadTexto.isEmpty(codigosCitasVinculado)){
//							   consulta+="AND co.codigo_pk IN ( "+codigosCitasVinculado+" ) ";
//						   }
						   
						   consulta+= ") " +
									"AS hora_cita, " +
									"(SELECT CASE WHEN  COUNT( sco.codigo_pk)>0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END " +
										" FROM odontologia.servicios_cita_odontologica sco " +
										" INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
										" (co.estado IN ("+estadosCitasVinculado+"))) " +
										" WHERE sco.programa_hallazgo_pieza = php.codigo_pk   and sco.servicio=pspt.servicio AND sco.activo = '"+ConstantesBD.acronimoSi+"' " ;
							
							if(codigoCitaNoVinculado>0){
								consulta+="AND co.codigo_pk!= "+codigoCitaNoVinculado+" ";
							}
							
//							if(!UtilidadTexto.isEmpty(codigosCitasVinculado)){
//								consulta+="AND co.codigo_pk IN ( "+codigosCitasVinculado+" ) ";
//							}
									
							consulta+= " ) as esta_vin_ser, " +

									"pt.estado as estado_plan_trata, " +
									"pspt.garantia as garantia_ser, " +
									//"pspt.codigo_pk as codigo_pspt, " +
									"pspt.orden_servicio as orden_servicio, " +
									"coalesce(prog.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_programa, " +
									"coalesce(prog.nombre,' ') as nombre_prog, " +
									"coalesce(prog.codigo_programa,' ') as sigla_programa, " +
									"dpt.seccion as seccion, " +
									"coalesce(pden.codigo_pk,"+ConstantesBD.codigoNuncaValido+") as cod_pieza_dental, " +
									"coalesce(pden.descripcion,' ') as desc_pieza_dental, " +
									"php.codigo_pk AS codigo_hallazgo_pieza," +
									"php.color_letra as color_letra, "+
									"coalesce(pden.edad_aplica,' ') as edad_apli_pd ";
							
							tablas = "FROM odontologia.plan_tratamiento pt " +
								"INNER JOIN odontologia.programas_hallazgo_pieza php on (php.plan_tratamiento=pt.codigo_pk) " +
								"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
								"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
								"INNER JOIN odontologia.programas_servicios_plan_t pspt ON (pspt.det_plan_tratamiento = dpt.codigo_pk  and pspt.programa=php.programa AND pspt.activo = '"+ConstantesBD.acronimoSi+"' ) " +
								"LEFT OUTER JOIN odontologia.pieza_dental pden ON (pden.codigo_pk = dpt.pieza_dental ) " +
								"LEFT OUTER JOIN odontologia.programas prog ON ( prog.codigo = pspt.programa AND prog.institucion = "+codigoInstitucion+" AND prog.activo = '"+ConstantesBD.acronimoSi+"') " +
								"LEFT OUTER JOIN facturacion.servicios ser ON (ser.codigo = pspt.servicio) ";
							
							where += " pt.codigo_pk = "+dato.get(0).getCodigo()+" " + 
								"AND pspt.estado_servicio = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
							
							if(!UtilidadTexto.isEmpty(codigoServicio)){
								
								where += " AND ser.codigo IN ( "+codigoServicio+" )";			
							}
							
							if(!UtilidadTexto.isEmpty(codigosServiciosNoIncluir)){
								
								where += " AND ser.codigo NOT IN ( "+codigosServiciosNoIncluir+" )";			
							}
							
							groupby = "GROUP BY prog.codigo, dpt.seccion, pden.codigo_pk, pspt.orden_servicio,pspt.servicio, " +
								"ser.codigo,ser.especialidad, ser.tipo_servicio, ser.minutos_duracion, " +
								"prog.nombre, prog.codigo_programa, pden.descripcion, " +
								"pden.edad_aplica, pspt.garantia, pt.estado ";
							
							//orderby = "ORDER BY nombre_prog,desc_pieza_dental,seccion,orden_servicio, descripcion_servicio ";
							if(ordenarPorPHP)
								orderby = "ORDER BY nombre_prog,codigo_hallazgo_pieza,desc_pieza_dental,seccion,pden.descripcion,orden_servicio, descripcion_servicio ";
							else
								orderby = "ORDER BY nombre_prog,desc_pieza_dental,seccion,pden.descripcion,orden_servicio, descripcion_servicio ";
	
							
							consulta+=tablas+where+groupby+orderby;
							casoBusquedaSer = ConstantesBD.acronimoConPlanTrataActivoEnProceso;
							band = false;
	
							logger.info("\n\nparametro en NO: ");
						}else{
							logger.info("esta parte no se tiene definida por documentacion");
						}
						
					}else
					{
						//ARMANDO, MIRAR ESTA CONSULTA, ESTO ES CAUSADO CUANDO EL PLAN DE TRATAMIENTO SE ENCUENTRA TERMINADO.
						if(dato.get(0).getNombre().equals(ConstantesIntegridadDominio.acronimoTerminado))
						{// plan de tratamiento en estado terminado
							
							if(serviciosGarantiaPlanTratamiento(dato.get(0).getCodigo(),ConstantesBD.acronimoSi).equals(ConstantesBD.acronimoSi))
							{
								consulta += ",CASE WHEN ser.codigo IN ( " +
									"	SELECT DISTINCT  " +
									"	ser.codigo " +
								    "	FROM facturacion.servicios ser " +
									"	LEFT OUTER JOIN consultaexterna.servicios_unidades_consulta suc ON (suc.codigo_servicio = ser.codigo) " +
									"	LEFT OUTER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = suc.unidad_consulta) " +
									"	LEFT OUTER JOIN odontologia.serv_adicionales_profesionales sap ON (sap.codigo_servicio = ser.codigo ) " +
									"	WHERE ((uc.codigo = "+unidadConsulta+" ) OR (sap.codigo_medico = "+codigoMedico+" AND sap.institucion = "+codigoInstitucion+" )) ";
								
								if(!"".equals(tipoServicio)){
									
									consulta += "	AND ser.tipo_servicio IN ("+tipoServicio+")  ";
								}
								
								if(activos){
									consulta+= "AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  ";
								}
								
								consulta+= ") THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS habilitar_seleccion," +
										"(SELECT ao.fecha " +
											"FROM odontologia.servicios_cita_odontologica sco " +
											"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
												"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
												"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
											"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
											"WHERE sco.programa_hallazgo_pieza = php.codigo_pk  and sco.servicio=pspt.servicio ) " +
										"AS fecha_cita, " +
										"(SELECT co.hora_inicio " +
											"FROM odontologia.servicios_cita_odontologica sco " +
											"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
												"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
												"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
											"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
											"WHERE sco.programa_hallazgo_pieza = php.codigo_pk  and sco.servicio=pspt.servicio ) " +
										"AS hora_cita, " +
										"pt.estado as estado_plan_trata, " +
										"pspt.garantia as garantia_ser, " +
										//"pspt.codigo_pk as codigo_pspt, " +
										"pspt.orden_servicio as orden_servicio, " +
										"coalesce(prog.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_programa, " +
										"coalesce(prog.nombre,' ') as nombre_prog, " +
										"coalesce(prog.codigo_programa,' ') as sigla_programa, " +
										"dpt.seccion as seccion, " +
										"coalesce(pden.codigo_pk,"+ConstantesBD.codigoNuncaValido+") as cod_pieza_dental, " +
										"coalesce(pden.descripcion,' ') as desc_pieza_dental, " +
										"php.codigo_pk AS codigo_hallazgo_pieza," +
										"php.color_letra as color_letra, "+
										"coalesce(pden.edad_aplica,' ') as edad_apli_pd ";
								
								tablas = "FROM odontologia.plan_tratamiento pt " + 
									"INNER JOIN odontologia.programas_hallazgo_pieza php on (php.plan_tratamiento=pt.codigo_pk) " +
									"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
									"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
									//"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.plan_tratamiento = pt.codigo_pk) " +
									"INNER JOIN odontologia.programas_servicios_plan_t pspt ON (pspt.det_plan_tratamiento = dpt.codigo_pk  and pspt.programa=php.programa AND pspt.activo = '"+ConstantesBD.acronimoSi+"' ) " +
									"LEFT OUTER JOIN odontologia.pieza_dental pden ON (pden.codigo_pk = dpt.pieza_dental ) " +
									"LEFT OUTER JOIN odontologia.programas prog ON ( prog.codigo = pspt.programa AND prog.institucion = "+codigoInstitucion+" AND prog.activo = '"+ConstantesBD.acronimoSi+"') " +
									"LEFT OUTER JOIN facturacion.servicios ser ON (ser.codigo = pspt.servicio) ";
								
								where += " pt.codigo_pk = "+dato.get(0).getCodigo()+" " +
									"AND pspt.garantia = '"+ConstantesBD.acronimoSi+"' ";
								
								
							
								
								if(validaPresCont.equals(ConstantesBD.acronimoNo))
									where += "AND pspt.estado_servicio = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
								else
									where += "AND pspt.estado_servicio = '"+ConstantesIntegridadDominio.acronimoContratado+"' ";
								
								if(!UtilidadTexto.isEmpty(codigoServicio)){
									
									where += " AND ser.codigo IN ( "+codigoServicio+" )";
								}
								
								if(!UtilidadTexto.isEmpty(codigosServiciosNoIncluir)){
									
									where += " AND ser.codigo NOT IN ( "+codigosServiciosNoIncluir+" )";			
								}

								groupby = "GROUP BY prog.codigo, dpt.seccion, pden.codigo_pk, pspt.orden_servicio,pspt.servicio, " +
									"ser.codigo,ser.especialidad, ser.tipo_servicio, ser.minutos_duracion, " +
									"prog.nombre, prog.codigo_programa, pden.descripcion, " +
									"pden.edad_aplica, pspt.garantia, pt.estado ";
								
								//orderby = "ORDER BY nombre_prog,desc_pieza_dental,seccion,orden_servicio, descripcion_servicio ";
								if(ordenarPorPHP)
									orderby = "ORDER BY nombre_prog,codigo_hallazgo_pieza,desc_pieza_dental,seccion,pden.descripcion,orden_servicio, descripcion_servicio ";
								else
									orderby = "ORDER BY nombre_prog,desc_pieza_dental,seccion,pden.descripcion,orden_servicio, descripcion_servicio ";

								
								consulta+=tablas+where+groupby+orderby;
							}else{
								consulta=consultaAux;
							}
							casoBusquedaSer = ConstantesBD.acronimoConPlanTratamientoTerminado;
							
						}else{
							if(dato.get(0).getNombre().equals(ConstantesIntegridadDominio.acronimoInactivo))
							{
								// el caso en el que el plan de tratamiento esta inactivo actua de la misma forma en
								// que actua si no se tien plan de tratamiento.
								logger.info("caso en el que el plan de tratamiento esta inactivo");
								consulta=consultaAux;
								casoBusquedaSer = ConstantesBD.acronimoConPlanTratamientoInactivo;
							}else{
								consulta=consultaAux;
								casoBusquedaSer = ConstantesBD.acronimoSinPlanTratamiento;
							}
						}
					}
				}
			}else if(casoBusSer.equals(ConstantesBD.acronimoConPlanTrataActivoEnProceso)){
				
				consulta=consultaAux;
				casoBusquedaSer = ConstantesBD.acronimoConPlanTrataActivoEnProceso;
				
			}else if(casoBusSer.equals(ConstantesBD.acronimoConPlanTratamientoTerminado)){
				
				consulta=consultaAux;
				casoBusquedaSer = ConstantesBD.acronimoConPlanTratamientoTerminado;
			}
			
			int codigoTarifario = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)); 
			Connection con = UtilidadBD.abrirConexion();
			
			if(!UtilidadTexto.isEmpty(isServicioVinculadoCita) && buscarPlanTratamiento){
				
				consulta = " SELECT * FROM ( " + consulta + " ) AS servicios WHERE servicios.esta_vin_ser = '"+isServicioVinculadoCita+"'";
			}
	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoTarifario);			
			logger.info("\n\nla consulta final: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoServicioOdontologico dto = new DtoServicioOdontologico();
				if(dato.size()>0)
					dto.setCodigoPlanTratamiento(dato.get(0).getCodigo());
				else
					dto.setCodigoPlanTratamiento(ConstantesBD.codigoNuncaValido);
				dto.setCodigoServicio(rs.getInt("codigo_servicio"));
				dto.setMinutosduracion(rs.getInt("duracion"));
				dto.setMinutosduracionNuevos(rs.getInt("duracion"));
				dto.setEspecialidad(rs.getInt("especialidad"));
				dto.setDescripcionServicio(rs.getString("descripcion_servicio"));
				dto.setCodigoTipoServicio(rs.getString("codigo_tipo_ser"));
				dto.setHabilitarSeleccion(rs.getString("habilitar_seleccion"));
				dto.setCasoBusServicio(casoBusquedaSer);
				if(casoBusquedaSer.equals(ConstantesBD.acronimoConPlanTrataActivoEnProceso)
					&& cambiarSerOdo.equals(ConstantesBD.acronimoNo))
				{
					dto.setEstadoPlanTratamiento(rs.getString("estado_plan_trata"));
					dto.setGarantiaServicio(rs.getString("garantia_ser"));
					//este campo se elimina
					dto.getProgramaHallazgoPieza().setCodigoPk(rs.getInt("codigo_hallazgo_pieza"));
					dto.getProgramaHallazgoPieza().setColorLetra(rs.getString("color_letra"));
					
					dto.setCodigoPrograma(rs.getInt("cod_programa"));
					dto.setNombrePrograma(rs.getString("nombre_prog").equals(" ")?"":rs.getString("nombre_prog"));
					dto.setSiglaPrograma(rs.getString("sigla_programa").equals(" ")?"":rs.getString("sigla_programa"));
					dto.setSeccionPlanTrata(rs.getString("seccion").equals(ConstantesIntegridadDominio.acronimoBoca)?rs.getString("seccion"):ConstantesIntegridadDominio.acronimoDetalle);
					dto.setCodigoPiezaDental(rs.getInt("cod_pieza_dental"));
					
					dto.setSuperficies(cargarSuperficiesXPrograma(con,rs.getInt("codigo_hallazgo_pieza")));
					//dto.setSuperficieDental(rs.getInt("superficie"));
					//se debe cargar una estrucutra aparte para las superficies.
					//ademas se debe agregar al dto el color de la letra y el codigo_pk de programa_hallazgo_pieza.
					dto.setDespPiezaDental(rs.getString("desc_pieza_dental").equals(" ")?"":rs.getString("desc_pieza_dental"));
					dto.setEdadAplicaPD(rs.getString("edad_apli_pd").trim());
					dto.setOrdenServicio(rs.getInt("orden_servicio"));
					dto.setEstaVinculadoSerCita(rs.getString("esta_vin_ser"));
					dto.setFechaCita(rs.getString("fecha_cita"));
					dto.setHoraCita(rs.getString("hora_cita"));
					
					if(rs.getInt("codigo_hallazgo_pieza")>0)
					{
						dto.setCodigoPresuOdoProgSer(obtenerCodigoPkPresupuestoOdonProgramaServicio(rs.getBigDecimal("codigo_hallazgo_pieza")));
						dto.setPaquete(cargarPaqueteOdontoloPresupuesto(dto.getCodigoPresuOdoProgSer()));
					}
				}
				
				if(dto.getCodigoServicio()>0)
				{// adicionar las condiciones toma de servicio
					dto.setCondicionesToma(obtenerCondicionesTomaServicio(con,codigoTarifario, dto.getCodigoServicio(),codigoInstitucion));
					// dto de abonos por servicio   
				}
				array.add(dto);
			}
			
			if(array.size()<=0 && casoBusquedaSer.equals(ConstantesBD.acronimoConPlanTrataActivoEnProceso) && band)
			{
			  // no se encontraton servicios asociados al presupuesto 
			  // se listaran los servicios como si no tuviera plan de tratamiento
				casoBusquedaSer = ConstantesBD.acronimoConPlanTrataActivoEnProceso;
				int codigoTarifario1 = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion));
				ps =  new PreparedStatementDecorator(con,consultaAux);
				ps.setInt(1, codigoTarifario1);				
				logger.info("\n\nla consulta aux final: "+ps);
				rs = new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					DtoServicioOdontologico dto = new DtoServicioOdontologico();
					if(dato.size()>0)
						dto.setCodigoPlanTratamiento(dato.get(0).getCodigo());
					else
						dto.setCodigoPlanTratamiento(ConstantesBD.codigoNuncaValido);
					dto.setCodigoServicio(rs.getInt("codigo_servicio"));
					dto.setMinutosduracion(rs.getInt("duracion"));
					dto.setEspecialidad(rs.getInt("especialidad"));
					dto.setDescripcionServicio(rs.getString("descripcion_servicio"));
					dto.setCodigoTipoServicio(rs.getString("codigo_tipo_ser"));
					dto.setHabilitarSeleccion(rs.getString("habilitar_seleccion"));
					dto.setCasoBusServicio(casoBusquedaSer);
					if(dto.getCodigoServicio()>0)
					{	// adicionar las condiciones toma de servicio
						dto.setCondicionesToma(obtenerCondicionesTomaServicio(con,codigoTarifario1, dto.getCodigoServicio(),codigoInstitucion));
					}
				}
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}
	
	private static DtoPaquetesOdontologicos cargarPaqueteOdontoloPresupuesto(int codigoPresuOdoProgSer) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String consulta="select po.codigo_pk as codigopk,po.codigo as codigo,po.descripcion as descripcion FROM odontologia.paquetes_odontologicos po INNER JOIN odontologia.det_paq_odont_convenio dpoc on(dpoc.codigo_pk_paquete=po.codigo_pk) inner join odontologia.presupuesto_paquetes pp ON(pp.det_paq_odon_convenio=dpoc.codigo_pk) INNER JOIN odontologia.presupuesto_odo_convenio poc ON(poc.presupuesto_paquete=pp.codigo_pk) where poc.presupuesto_odo_prog_serv=? and poc.contratado='"+ConstantesBD.acronimoSi+"'";
		DtoPaquetesOdontologicos infoPaquete=new DtoPaquetesOdontologicos();
		PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consulta);
		try
		{
			ps.setInt(1, codigoPresuOdoProgSer);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				infoPaquete.setCodigo(rs.getString("codigo"));
				infoPaquete.setCodigoPk(rs.getInt("codigopk"));
				infoPaquete.setDescripcion(rs.getString("descripcion"));
			}
			rs.close();
			ps.close();
			
		}catch(SQLException e){
			logger.error("error",e);
		}
		
		UtilidadBD.closeConnection(con);
		return infoPaquete;
	}

	/**
	 * 
	 * @param codigoProgHallazgoPeiza
	 * @return
	 */
	public static ArrayList<DtoSuperficiesPorPrograma> cargarSuperficiesXPrograma(Connection con,int codigoProgHallazgoPeiza) 
	{
		ArrayList<DtoSuperficiesPorPrograma> array=new ArrayList<DtoSuperficiesPorPrograma>();
		String consulta="SELECT codigo_pk,superficie_dental as codigosuperficie,sd.nombre as nombresuperficie,prog_hallazgo_pieza,usuario_modifica,hora_modifica,det_plan_trata from odontologia.superficies_x_programa sxp inner join superficie_dental sd on (sxp.superficie_dental=sd.codigo) where prog_hallazgo_pieza=?";
		PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consulta);
		try
		{
			ps.setInt(1, codigoProgHallazgoPeiza);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				
				DtoSuperficiesPorPrograma dto=new DtoSuperficiesPorPrograma();
				dto.setDescripcionSuperficie(rs.getString("nombresuperficie"));
				dto.setDetPlanTratamiento(rs.getInt("det_plan_trata"));
				dto.setSuperficieDental(rs.getInt("codigosuperficie"));
				array.add(dto);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("error",e);
		}
		return array;
	}

	/**
	 * 
	 * @param codigoPkProgramaHallazgo
	 * @param codigoServicio
	 * @param codigoPlanTratamiento
	 * @return
	 */
	private static int obtenerCodigoPkPresupuestoOdonProgramaServicio(BigDecimal codigoPkProgramaHallazgo) 
	{
		int resultado= ConstantesBD.codigoNuncaValido;
		
		DtoProgHallazgoPieza dtoBusqueda= new DtoProgHallazgoPieza(true);
		dtoBusqueda.setCodigoPk(codigoPkProgramaHallazgo.intValue());
		Connection con = UtilidadBD.abrirConexion();
		DtoProgHallazgoPieza dtoProgramaHallazgo= NumeroSuperficiesPresupuesto.obtenerProgramaHallazgoPiezaPlanTratamiento(con, dtoBusqueda);
		UtilidadBD.closeConnection(con);
		
		if(dtoProgramaHallazgo!=null && dtoProgramaHallazgo.getCodigoPk()>0)
		{
			String consultaStr=" SELECT  " +
									"max(pops.codigo_pk) " +
								"from " +
									"odontologia.presupuesto_odontologico po " +
									"inner join odontologia.presupuesto_odo_prog_serv pops on(pops.presupuesto=po.codigo_pk) " +
									"inner join odontologia.presupuesto_piezas pp on(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
								"where " +
									"po.plan_tratamiento=? " +
									"and po.estado ='"+ConstantesIntegridadDominio.acronimoContratadoContratado+"' " +
									"and pops.programa= ? " +
									"and pp.hallazgo= ? " +
									"and pp.seccion=?  ";
			boolean puseSuperficie=false;
			if(dtoProgramaHallazgo.getPiezaDental()>0)
			{
				consultaStr+=" and pp.pieza = "+dtoProgramaHallazgo.getPiezaDental();
				if(dtoProgramaHallazgo.getSuperficiesPorProgramaNoBocaNiDiente().size()>0)
				{
					consultaStr+=" and pp.superficie in ("+cargarSuperficiesSeparadasComa(dtoProgramaHallazgo.getSuperficiesPorProgramaNoBocaNiDiente())+") ";
				}
				else
				{
					consultaStr+=" and (pp.superficie is null OR superficie = "+ConstantesBD.codigoSuperficieDiente+") ";
					puseSuperficie=true;
				}
			}
			if(dtoProgramaHallazgo.getPiezaDental()<=0)
			{
				consultaStr+=" and pp.pieza is null ";
				if(!puseSuperficie)
				{
					consultaStr+=" and (pp.superficie is null OR superficie = "+ConstantesBD.codigoSuperficieBoca+") ";
				}
			}
			
			/*
			consultaStr+=(dtoProgramaHallazgo.getPiezaDental()>0)?" and pp.pieza = "+dtoProgramaHallazgo.getPiezaDental()+" ": " and pp.pieza is null ";
			consultaStr+=(dtoProgramaHallazgo.getSuperficiesPorProgramaNoBocaNiDiente().size()>0)?" and pp.superficie in ("+cargarSuperficiesSeparadasComa(dtoProgramaHallazgo.getSuperficiesPorProgramaNoBocaNiDiente())+") ": " and pp.superficie is null ";
			*/
			
			try
			{
				con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
				ps.setInt(1, dtoProgramaHallazgo.getPlanTratamiento());
				ps.setInt(2, dtoProgramaHallazgo.getPrograma());
				ps.setInt(3, dtoProgramaHallazgo.getHallazgo());
				ps.setString(4, dtoProgramaHallazgo.getSeccion());
				
				Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n\nCONSULTAAAAAAAAAAAAAAAAAAAAAA------>"+ps.toString()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if (rs.next())
				{	
					resultado = rs.getInt(1);
				}	
				rs.close();
				ps.close();
				UtilidadBD.cerrarConexion(con);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return resultado;
	}

	/**
	 * 
	 * Metodo para .......
	 * @param superficiesPorPrograma
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static String cargarSuperficiesSeparadasComa(ArrayList<DtoSuperficiesPorPrograma> superficiesPorPrograma) 
	{
		String superficies="";
		for(DtoSuperficiesPorPrograma dto: superficiesPorPrograma)
		{
			superficies+= (!superficies.isEmpty())?", ":" ";
			superficies+= dto.getSuperficieDental();
		}
		return superficies;
	}

	/**
	 * metodo que obtiene la condiciones toma del servicio
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerCondicionesTomaServicio(
			Connection con,
			int codigoTarifario,
			int codigoServicio, 
			int codigoInstitucion)
	{
		
		String strConsultaCondicionesTomaServicio = "SELECT " +  
								"ect.codigo_examenct as cod_examen_ct, " +
								"ect.descrip_examenct as desp_examen_ct, " +
								" getcodigopropservicio(ser.codigo, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)+") ||' '||getnombreservicio(ser.codigo,?) AS descripcion_servicio " +
								"FROM  facturacion.examen_conditoma ect " +
								"LEFT OUTER JOIN facturacion.servicios ser ON (ser.codigo = ? ) " +
								"WHERE ect.codigo_examenct IN ( " +
								"  select condicion from  " +
								"  facturacion.condi_serv_condiciones csc " + 
								"  INNER JOIN facturacion.condi_serv_servicios css ON (css.consecutivo = csc.consecutivo) " +
								"  WHERE css.servicio = ?  " +
								")  " +
								"AND ect.activo_examenct = '"+ConstantesBD.acronimoSi+"' " +
								"AND ect.institucion = ? ";
		
		ArrayList<InfoDatosInt> array = new ArrayList<InfoDatosInt>();
		try{
			//logger.info("Consulta: "+strConsultaCondicionesTomaServicio);
			//logger.info("codigoServicio: "+codigoServicio+" codigoInstitucion: "+codigoInstitucion);
			//Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaCondicionesTomaServicio);
			ps.setInt(1, codigoTarifario);
			ps.setInt(2, codigoServicio);
			ps.setInt(3, codigoServicio);
			ps.setInt(4, codigoInstitucion);
			//logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt dato = new InfoDatosInt(rs.getInt("cod_examen_ct"),rs.getString("desp_examen_ct"), rs.getString("descripcion_servicio"));
				array.add(dato); 
			}
			rs.close();
			ps.close();
			//UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+strConsultaCondicionesTomaServicio);
			logger.info("codigoServicio: "+codigoServicio+" codigoInstitucion: "+codigoInstitucion);
		}
		return array;
	}

	/**
	 * metodo que cuenta los servicios que estan  en gasrantia de un plan de tratamiento especifico
	 * @param codigoPlanTrat
	 * @param estadoGarantia
	 * @return
	 */
	public static String serviciosGarantiaPlanTratamiento(
			int codigoPlanTrat,
			String estadoGarantia)
	{
		String result = ConstantesBD.acronimoNo; 
		try{
			logger.info("Consulta: "+strCountServicioGarantiaPT);
			logger.info("codigo plan trata: "+codigoPlanTrat+" estado garantia: "+estadoGarantia);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCountServicioGarantiaPT,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPlanTrat);
			ps.setString(2, estadoGarantia);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				result = rs.getString("servici_garantia");
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+strCountServicioGarantiaPT);
			logger.info("codigo plan trata: "+codigoPlanTrat+" estado garantia: "+estadoGarantia);
		}
		return result;
	}

	/**
	 * meodo que retorna las piezas dentales
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasDentales()
	{
		ArrayList<InfoDatosInt> array = new ArrayList<InfoDatosInt>();
		try{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultarPiezasDentales);
			logger.info("Consulta: "+strConsultarPiezasDentales);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt dato = new InfoDatosInt();
				dato.setCodigo(rs.getInt("codigo_pk"));
				dato.setDescripcion(rs.getString("descripcion")); // descripcion de la pieza
				dato.setNombre(rs.getString("edad_aplica")); // tipo de persona a la que aplica
				array.add(dato);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+strCountServicioGarantiaPT);
		}
		return array;
		
	}

	/**
	 * metodo que obtiene los tratamientos externos 
	 * @return
	 */
	public static ArrayList<DtoTratamientoExterno> obtenerAnteOdoTratamientosExternos(int codigoAnteOdo)
	{
		ArrayList<DtoTratamientoExterno> array = new ArrayList<DtoTratamientoExterno>();
		try{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultarTratamientoExterno);
			ps.setInt(1, codigoAnteOdo);
			//logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoTratamientoExterno dto = new DtoTratamientoExterno();
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCodigoAnteOdo(rs.getInt("antece_odo"));
				dto.setFechaInicio(rs.getString("fecha_ini").equals(" ")?"":rs.getString("fecha_ini"));
				dto.setFechaFinal(rs.getString("fecha_fin").equals(" ")?"":rs.getString("fecha_fin"));
				dto.setProgramaServicio(rs.getString("prog_serv").equals(" ")?"":rs.getString("prog_serv"));
				dto.setCodigoPiezaDen(rs.getInt("pieza_den"));
				dto.setDescripcionPiezaDen(rs.getString("desp_pieza").equals(" ")?"":rs.getString("desp_pieza"));
				dto.setCodigoEspecialidad(rs.getInt("especialidad"));
				dto.setDescripcionEsp(rs.getString("nombre_esp").equals(" ")?"":rs.getString("nombre_esp"));
				array.add(dto);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+strConsultarTratamientoExterno);
		}
		return array;
	}
	
	
	/**
	 * Metodo para Consultar los tratamientos Internos
	 * @param codAntecedenteOdo
	 * @return
	 */
	public static ArrayList<DtoTratamientoInterno> obtenerAnteOdontoTratamientosInternos(int codAntecedenteOdo, boolean porProgramas)
	{
		ArrayList<DtoTratamientoInterno> array = new ArrayList<DtoTratamientoInterno>();
		String consulta= "";
		if(porProgramas)
		{
			consulta= strConsultaTratamientosOdontInternosAntXProgramas;
		}else
		{
			consulta= strConsultaTratamientosOdontInternosAntXServicios;
		}
		Connection con = UtilidadBD.abrirConexion();
		try{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codAntecedenteOdo);
			logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoTratamientoInterno dto = new DtoTratamientoInterno();
				dto.setCodigoAnteOdo(rs.getInt("antecedenteodo"));
				dto.setFechaInicio(rs.getString("fechainicio").equals(" ")?"":rs.getString("fechainicio"));
				dto.setFechaFinal(rs.getString("fechafinal").equals(" ")?"":rs.getString("fechafinal"));
				if(porProgramas){
					logger.info("\n\nENTRO POR TRATAMIENTO  Programa >>" );
					dto.setCodPrograma(rs.getInt("codprograma"));
				    dto.setNombrePrograma(rs.getString("nombreprograma"));
				    logger.info("\n\nNOMBRE TRATAMIENTO  Programa >>"+dto.getNombrePrograma());
				}else
				 {
					logger.info("\n\nENTRO POR TRATAMIENTO  Servicio >>" );
					dto.setCodServicio(rs.getInt("codservicio"));
					dto.setNombreServicio(rs.getString("nombreservicio"));
					logger.info("\n\nNOMBRE TRATAMIENTO  Servicio >>"+dto.getNombreServicio());
				 }
				dto.setCodigoPiezaDen(rs.getInt("codpiezadental"));
				dto.setDescripcionPiezaDen(rs.getString("descrip_pieza").equals(" ")?"":rs.getString("descrip_pieza"));
				dto.setCodigoEspecialidad(rs.getInt("codespecialidad"));
				dto.setDescripcionEsp(rs.getString("nombre_esp").equals(" ")?"":rs.getString("nombre_esp"));
				array.add(dto);
			
				
			}
			rs.close();
			ps.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta Tratamientos Internos: "+consulta);
		}
		UtilidadBD.closeConnection(con);
		return array;
			
	}
	
	
	/**
	 * metodo que obtiene el antecedente odontologico
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoAntecendenteOdontologico obtenerAntecedenteOdontologico(int codigoPaciente)
	{
		DtoAntecendenteOdontologico dto = new DtoAntecendenteOdontologico();
		Connection con = UtilidadBD.abrirConexion();
		try{
		
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaAntecedenteOdon);
			ps.setInt(1, codigoPaciente);
			//logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setCentroAtencion(rs.getInt("centro_aten"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setCodigoMedico(rs.getInt("cod_medico"));
				dto.setEspecialidad(rs.getInt("especialidad"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
				dto.setObservaciones(rs.getString("observaciones"));
				dto.setMostrarPor(rs.getString("mostrar_por"));
				dto.setValoracion(rs.getInt("valoracion"));
				dto.setEvolucion(rs.getInt("evolucion"));
				dto.setFechaModifica(rs.getString("fecha_mod"));
				dto.setHoraModifica(rs.getString("hora_mod"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));
				if(dto.getCodigoPk()>0)
					dto.setTratamientosExternos(obtenerAnteOdoTratamientosExternos(dto.getCodigoPk()));
				     
			}
			rs.close();
			ps.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			UtilidadBD.closeConnection(con);
			logger.info("Error Consulta : "+strConsultaAntecedenteOdon);
		}
		UtilidadBD.closeConnection(con);
		return dto;
	}
	
	/**
	 * Método implementado para cargar la informacion de antecedentes bien sea desde la valoracion o evolucion odontologica
	 * @param con
	 * @param antecedenteOdo
	 */
	public static void obtenerAntecedenteOdontologicoHistorico(Connection con,DtoAntecendenteOdontologico antecedenteOdo)
	{
		try
		{
			String consulta = strConsultaAntecedenteOdonExistente + " WHERE ";
			if(antecedenteOdo.getValoracion()>0)
			{
				consulta += " hao.valoracion_odo = ?";
			}
			else
			{
				consulta += " hao.evolucion_odo = ?";
			}
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			if(antecedenteOdo.getValoracion()>0)
			{
				pst.setBigDecimal(1, new BigDecimal(antecedenteOdo.getValoracion()));
			}
			else
			{
				pst.setBigDecimal(1, new BigDecimal(antecedenteOdo.getEvolucion()));
			}
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				antecedenteOdo.setCodigoPk(rs.getInt("codigo"));
				antecedenteOdo.setCodigoPaciente(rs.getInt("cod_paciente"));
				antecedenteOdo.setIngreso(rs.getInt("ingreso"));
				antecedenteOdo.setCentroAtencion(rs.getInt("centro_aten"));
				antecedenteOdo.setInstitucion(rs.getInt("institucion"));
				antecedenteOdo.setCodigoMedico(rs.getInt("cod_medico"));
				antecedenteOdo.setEspecialidad(rs.getInt("especialidad"));
				antecedenteOdo.setPorConfirmar(rs.getString("por_confirmar"));
				antecedenteOdo.setObservaciones(rs.getString("observaciones"));
				antecedenteOdo.setMostrarPor(rs.getString("mostrar_por"));
				antecedenteOdo.setValoracion(rs.getInt("valoracion"));
				antecedenteOdo.setEvolucion(rs.getInt("evolucion"));
				antecedenteOdo.setFechaModifica(rs.getString("fecha_mod"));
				antecedenteOdo.setHoraModifica(rs.getString("hora_mod"));
				antecedenteOdo.setUsuarioModifica(rs.getString("usu_mod"));
				if(antecedenteOdo.getCodigoPk()>0)
				{
					antecedenteOdo.setTratamientosExternos(obtenerAnteOdoTratamientosExternos(antecedenteOdo.getCodigoPk()));
				}
				if(antecedenteOdo.getCodigoPk()>0)
				{
					antecedenteOdo.setTratamientosInternos(obtenerAnteOdontoTratamientosInternos(antecedenteOdo.getCodigoPk(),(antecedenteOdo.getMostrarPor().equals(ConstantesIntegridadDominio.acronimoMostrarProgramas)?true:false)));
				}
				     
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAntecedenteOdontologicoHistorico: ",e);
		}
	}
	
	/**
	 * metodo que obtiene el antecedente odontologico existente
	 * @param esEvolucion 
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoAntecendenteOdontologico obtenerAntecedenteOdontExistente(int codigoAnteOdon, boolean esEvolucion)
	{
		DtoAntecendenteOdontologico dto = new DtoAntecendenteOdontologico();
		try{
			Connection con = UtilidadBD.abrirConexion();
			String consulta = strConsultaAntecedenteOdonExistente + " WHERE "+(esEvolucion?"hao.evolucion_odo = ?":"hao.valoracion_odo = ?");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoAnteOdon);
			logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setCentroAtencion(rs.getInt("centro_aten"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setCodigoMedico(rs.getInt("cod_medico"));
				dto.setEspecialidad(rs.getInt("especialidad"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
				dto.setObservaciones(rs.getString("observaciones"));
				dto.setMostrarPor(rs.getString("mostrar_por"));
				dto.setValoracion(rs.getInt("valoracion"));
				dto.setEvolucion(rs.getInt("evolucion"));
				dto.setFechaModifica(rs.getString("fecha_mod"));
				dto.setHoraModifica(rs.getString("hora_mod"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));
				if(dto.getCodigoPk()>0)
					dto.setTratamientosExternos(obtenerAnteOdoTratamientosExternos(dto.getCodigoPk()));
				     
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta : "+strConsultaAntecedenteOdonExistente);
		}
		return dto;
	}
	
	/**
	 * Metodo que obtiene TODOS lo antecendentes Odontologicos actuales
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoAntecendenteOdontologico> obtenerAntecedentesOdontologicos(int codigoPaciente, int ingreso, String fechaInicio,String  fechaFinal)
	{
		ArrayList<DtoAntecendenteOdontologico> arrayAntecedentes = new ArrayList<DtoAntecendenteOdontologico>();
		String consulta= strConsultaAntecedentesOdontologicos;
		String filtros = "";
		
		if(ingreso > 0)
		{
			filtros += " hao.ingreso = "+ingreso +" "  ;
		}
			
		if(!fechaInicio.equals("") && !fechaFinal.equals(""))
		{
			if(!filtros.equals(""))
			{
			  filtros += " AND  ";		
			}
			
			filtros += " to_char(hao.fecha_modifica,'YYYY-MM-DD') BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicio)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' " ;
			          
		}
		
		if (!filtros.equals(""))
		{
			consulta=consulta.replace("1=1", filtros);
		}
			
		Connection con = UtilidadBD.abrirConexion();
		try{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoPaciente);
			
			
			
			logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			 {
				DtoAntecendenteOdontologico dto = new DtoAntecendenteOdontologico();
				
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setNombresMedico(rs.getString("nombresmedico"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setCentroAtencion(rs.getInt("centro_aten"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setCodigoMedico(rs.getInt("cod_medico"));
				dto.setEspecialidad(rs.getInt("especialidad"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
				dto.setObservaciones(rs.getString("observaciones"));
				dto.setMostrarPor(rs.getString("mostrar_por"));
				dto.setValoracion(rs.getInt("valoracion"));
				dto.setEvolucion(rs.getInt("evolucion"));
				dto.setFechaModifica(rs.getString("fecha_mod"));
				dto.setHoraModifica(rs.getString("hora_mod"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));				
			    dto.setTratamientosExternos(obtenerAnteOdoTratamientosExternos(dto.getCodigoPk()));
			    
			    if(dto.getMostrarPor().equals(ConstantesIntegridadDominio.acronimoMostrarProgramas))
			    {
			    	dto.setTratamientosInternos(obtenerAnteOdontoTratamientosInternos(dto.getCodigoPk(), true));
			    	
			    }else
			    {
			    	dto.setTratamientosInternos(obtenerAnteOdontoTratamientosInternos(dto.getCodigoPk(), false));
			    }
			    
			    arrayAntecedentes.add(dto);
			    
			}
			rs.close();
			ps.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			UtilidadBD.closeConnection(con);
			logger.info("Error Consulta Todos los Antecedentes Odontologicos: "+strConsultaAntecedentesOdontologicos);
		}
		UtilidadBD.closeConnection(con);
		return arrayAntecedentes;
	}
	

	/**
	 * metodo que actualiza el antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarAntecedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateAntecedenteOdontologico);
			ps.setInt(1, dto.getCodigoMedico());
			ps.setString(2, dto.getPorConfirmar());
			ps.setString(3, dto.getObservaciones());
			ps.setString(4, dto.getMostrarPor());
			ps.setInt(5, dto.getCodigoPk());
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
			{
				logger.info("Fue exitosa la ejecución del update!!!!!!!!!11");
				result = true;
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			
			String consulta = "SELECT por_confirmar as por_confirmar from historiaclinica.his_antecedentes_odo WHERE codigo_pk = ?";
			ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1,dto.getCodigoPk());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("valor del por confirmar: "+rs.getString("por_confirmar"));
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strUpdateAntecedenteOdontologico);
		}
		return result;
	}

	/**
	 * metodo que actualiza Tratamiento Externo 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateTratamientoExterno);
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicio()));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinal()));
			ps.setString(3, dto.getProgramaServicio());
			ps.setInt(4, dto.getCodigoPiezaDen());
			ps.setInt(5, dto.getCodigoEspecialidad());
			ps.setInt(6, dto.getCodigoPk());
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strUpdateAntecedenteOdontologico);
		}
		return result;
	}
	
	/**
	 * metodo eliminar tratamientos internos de un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean deleteTratamientoInterno(Connection con, int codigoAnteOdon)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strDeleteTratamientosInternos);
			ps.setInt(1, codigoAnteOdon);
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strDeleteTratamientosInternos);
		}
		return result;
	}
	
	/**
	 * metodo que elimina un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean deleteTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strDeleteTratamientoExterno);
			ps.setInt(1, dto.getCodigoPk());
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strDeleteTratamientoExterno);
		}
		return result;
	}

	/**
	 * metodo que inserta un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarAntcedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto )
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "historiaclinica.seq_his_antecedentes_odo");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertHisAntecedenteOdonto);
			ps.setInt(1, codigoPK);
			ps.setInt(2, dto.getCodigoPaciente());
			
			ps.setInt(3, dto.getIngreso());
			ps.setInt(4, dto.getCentroAtencion());
			ps.setInt(5, dto.getInstitucion());
			ps.setInt(6, dto.getCodigoMedico());
			
			if(dto.getEspecialidad()>0)
				ps.setInt(7, dto.getEspecialidad());
			else
				ps.setNull(7, Types.INTEGER);
				
			ps.setString(8, dto.getPorConfirmar());
			
			if(!dto.getObservaciones().equals(""))
			{
				String observaciones = UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+
				" "+dto.getNombresMedico()+". "+dto.getObservaciones()+".\n";
				ps.setString(9, observaciones);
			}else
				ps.setNull(9, Types.VARCHAR);
		
			if(!dto.getMostrarPor().equals(""))
				ps.setString(10, dto.getMostrarPor());
			else
				ps.setNull(10, Types.VARCHAR);
			
			if(dto.getValoracion()>0)
				ps.setInt(11, dto.getValoracion());
			else
				ps.setNull(11, Types.INTEGER);
			
			if(dto.getEvolucion()>0)
				ps.setInt(12, dto.getEvolucion());
			else
				ps.setNull(12, Types.INTEGER);
				
			ps.setString(13, dto.getUsuarioModifica());
			
			logger.info("Consulta: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				logger.info("codigo pk a retornar: "+codigoPK);
				// insert de tratamientos externos
				for(DtoTratamientoExterno elem: dto.getTratamientosExternos())
				{
					elem.setCodigoAnteOdo(codigoPK);
					if(insertarTratamientoExterno(con, elem)<=0)
						return ConstantesBD.codigoNuncaValido;
				}
				// insert de tratamientos internos
				for(DtoTratamientoInterno elem: dto.getTratamientosInternos())
				{
					elem.setCodigoAnteOdo(codigoPK);
					if(!insertarTratamientoInterno(con, elem))
						return ConstantesBD.codigoNuncaValido;
				}
				ps.close();
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strInsertHisAntecedenteOdonto);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * metodo que inserta un tratamiento interno 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarTratamientoInterno(Connection con, DtoTratamientoInterno dto)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertHisAntOdontoTratamientoInterno);
			ps.setInt(1, dto.getCodigoAnteOdo());
			ps.setInt(2, dto.getCodigoProgSerPlanTrat());
			
			if(!dto.getFechaInicio().equals(""))
				ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicio()));
			else
				ps.setNull(3, Types.VARCHAR);
			
			if(!dto.getFechaFinal().equals(""))
				ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinal()));
			else
				ps.setNull(4, Types.VARCHAR);
			
			if(dto.getCodigoPiezaDen()>0)
				ps.setInt(5, dto.getCodigoPiezaDen());
			else
				ps.setNull(5, Types.INTEGER);
			
			if(dto.getCodigoEspecialidad()>0)
				ps.setInt(6, dto.getCodigoEspecialidad());
			else
				ps.setNull(6, Types.INTEGER);
			
			logger.info("Consulta: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strInsertHisAntOdontoTratamientoInterno);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * metodo que inserta el un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "historiaclinica.seq_his_ant_odo_trat_ext");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertHisAntOdontoTratamientoExterno);
			ps.setInt(1, codigoPK);
			ps.setInt(2, dto.getCodigoAnteOdo());
			
			if(!dto.getFechaInicio().equals(""))
				ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicio()));
			else
				ps.setNull(3, Types.VARCHAR);
			
			if(!dto.getFechaFinal().equals(""))
				ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinal()));
			else
				ps.setNull(4, Types.VARCHAR);
			
			if(!dto.getProgramaServicio().equals(""))
				ps.setString(5, dto.getProgramaServicio());
			else
				ps.setNull(5, Types.VARCHAR);
			
			if(dto.getCodigoPiezaDen()>0)
				ps.setInt(6, dto.getCodigoPiezaDen());
			else
				ps.setNull(6, Types.INTEGER);
			
			if(dto.getCodigoEspecialidad()>0)
				ps.setInt(7, dto.getCodigoEspecialidad());
			else
				ps.setNull(7, Types.INTEGER);
			
			logger.info("Consulta: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				logger.info("codigo pk a retornar: "+codigoPK);
				ps.close();
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strInsertHisAntOdontoTratamientoExterno);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que retorna un booleano indicando si tiene o no agenda generada un profesional de la salud
	 * @param connection
	 * @param codigoProfesional
	 * @return
	 */
	public static boolean consultarProfesionalTieneAgendaGenerada(Connection connection, int codigoProfesional){
		boolean tiene = false;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, sqlConsultarProfesionalTieneAgendaGenerada);
			ps.setInt(1, codigoProfesional);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setString(5, ConstantesBD.acronimoSi);
			ps.setInt(6, codigoProfesional);
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setString(10, ConstantesBD.codigoEstadoCitaAsignada+"");
			ps.setString(11, ConstantesBD.codigoEstadoCitaReservada+"");
			logger.info("sqlConsultarProfesionalTieneAgendaGenerada-->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				if(rs.getInt("total") > 0)
					tiene = true;
			}
		} catch (Exception e) {
			logger.info("error consultando si el profesional tiene agenda odontologica asignada-->"+e);
			e.printStackTrace();
		}
		return tiene;
	}
	
	/**
	 * Método implementado para confirmar el indice de placa
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean confirmarIndicePlaca(Connection con,HashMap campos)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		boolean existe = false;
		try
		{
			//*************SE TOMAN LOS PARÁMETROS*******************************************
			String loginUsuario = campos.get("loginUsuario").toString();
			String codigoPlantillaIngreso = campos.get("codigoPlantillaIngreso").toString();
			String codigoPlantillaEvolucion = campos.get("codigoPlantillaEvolucion").toString();
			//*********************************************************************************
			
			//********SOLO SE CONFIRMA SI EXISTE************************************************
			String consulta = "SELECT count(1) as cuenta from historiaclinica.comp_indice_placa WHERE ";
			if(Utilidades.convertirALong(codigoPlantillaIngreso)>0)
			{
				consulta += " plantilla_ingreso = "+codigoPlantillaIngreso;
			}
			else if(Utilidades.convertirALong(codigoPlantillaEvolucion)>0)
			{
				consulta += " plantilla_evolucion_odo = "+codigoPlantillaEvolucion;
			}
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					existe = true;
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//************************************************************************************
			
			if(existe)
			{
				consulta = sqlConfirmarIndicePlaca;
				
				if(Utilidades.convertirALong(codigoPlantillaIngreso)>0)
				{
					consulta += " plantilla_ingreso = "+codigoPlantillaIngreso;
				}
				else if(Utilidades.convertirALong(codigoPlantillaEvolucion)>0)
				{
					consulta += " plantilla_evolucion_odo = "+codigoPlantillaEvolucion;
				}
					
				
				pst = new PreparedStatementDecorator(con,consulta);
				pst.setString(1,ConstantesBD.acronimoSi);
				pst.setString(2,loginUsuario);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Falló el proceso de confirmación del indice de placa");
				}
				pst.close();
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en confirmarIndicePlaca: ",e);
			resultado.setResultado(false);
			resultado.setDescripcion("Falló el proceso de confirmación del indice de placa: "+e);
		}
		return resultado;
	}
	
	/**
	 * metodo que actualiza el componente indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public static boolean actualizarComponenteIndicePlaca(Connection con, DtoComponenteIndicePlaca dto)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateCompIndicePlaca);
			ps.setString(1, dto.getImagen());
			ps.setDouble(2, dto.getPorcentaje().doubleValue());
			ps.setString(3, dto.getInterpretacion());
			ps.setString(4, dto.getPorConfirmar());
			ps.setString(5, dto.getUsuarioModifica());
			ps.setInt(6, dto.getCodigoPk());
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strUpdateCompIndicePlaca);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * metodo de insercion de detalle de componente indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return int
	 */
	public static int insertDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		try
		{
			int codigoPK =  UtilidadBD.obtenerSiguienteValorSecuencia(con, "historiaclinica.seq_det_indice_placa");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertDetalleCompIndicePlaca);
			ps.setInt(1, codigoPK);
			ps.setInt(2, dto.getPiezaDental().getCodigo());
			
			if(dto.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente))
				ps.setNull(3, Types.INTEGER);
			else
				if(dto.getSuperficie().getCodigoPk()!=ConstantesBD.codigoNuncaValido)
					ps.setInt(3, dto.getSuperficie().getCodigoPk());
				else
					ps.setNull(3, Types.INTEGER);
				
			ps.setString(4, dto.getIndicador());
			ps.setInt(5, dto.getCodigoIndicePlaca());
			
			logger.info("Consulta: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				dto.setCodigoPk(codigoPK);
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strInsertDetalleCompIndicePlaca);
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * metodo de eliminacion de detalle componente  indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public static boolean eliminarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strDeleteDetalleCompIndicePlaca);
			ps.setInt(1, dto.getCodigoPk());
			//ps.setInt(1, dto.getCodigoIndicePlaca());
			//ps.setInt(2, dto.getPiezaDental().getCodigo());
			
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strDeleteDetalleCompIndicePlaca);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * metod que actualiza el indicador del detalle del indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return boolean
	 */
	public static boolean actualizarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateDetalleCompIndicePlaca);
			ps.setString(1, dto.getIndicador());
			ps.setInt(2, dto.getCodigoPk());
			logger.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}catch (Exception e) {
			logger.info("ERROR Consulta: "+strUpdateDetalleCompIndicePlaca);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * metodo que obtine el ultimo indice de placa
	 * @param connection
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoComponenteIndicePlaca consultarComponenteIndicePlaca(Connection connection, int plantillaIngreso,int plantillaEvolucion, int codigoPaciente)
	{
		DtoComponenteIndicePlaca dto = new DtoComponenteIndicePlaca();
		String consulta = "";
		try 
		{
			
			
			if(plantillaIngreso>0)
			{
				consulta = strConsultarComponenteIndicePlacaVal;
			}
			else if(plantillaEvolucion>0)
			{
				consulta = strConsultarComponenteIndicePlacaEvo;
			}
			else
			{
				return dto;
			}
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, consulta);
			if(plantillaIngreso>0)
			{
				ps.setInt(1, plantillaIngreso);
			}
			else if(plantillaEvolucion>0)
			{
				ps.setInt(1, plantillaEvolucion);
			}
			
			logger.info("Consulta -->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setImagen(rs.getString("path_img"));
				dto.setPorcentaje(rs.getBigDecimal("porcentaje"));
				dto.setInterpretacion(rs.getString("interpretacion"));
				dto.setPlantillaIngreso(rs.getInt("plt_ingreso"));
				dto.setPlantillaEvolucionOdo(rs.getInt("plt_evolucion"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		} catch (Exception e) {
			logger.info("ERROR Consulta-->"+consulta);
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * metodo que obtiene un listadod e piezas con las repectivas combinaciones de superficies 
	 * @param connection
	 * @param codigoInstitucion
	 * @param codigoCompIndPlaca
	 * @return
	 */
	public static ArrayList<DtoDetalleIndicePlaca> consultarDetalleCompIndicePlaca(Connection connection, int codigoInstitucion, int codigoCompIndPlaca )
	{
		ArrayList<DtoDetalleIndicePlaca> array = new ArrayList<DtoDetalleIndicePlaca>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, strConsultarDetalleCompIndicePlaca);
			ps.setInt(1, codigoInstitucion);
			ps.setInt(2, codigoCompIndPlaca);
			logger.info("Consulta -->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDetalleIndicePlaca dto = new DtoDetalleIndicePlaca();
				DtoDetSuperficieIndicePlaca detSup = new DtoDetSuperficieIndicePlaca();
				dto.setCodigoPk(rs.getInt("codigo_ind_placa"));
				dto.setCodigoIndicePlaca(rs.getInt("comp_ind_placa"));
				dto.setPiezaDental(new InfoDatosInt(rs.getInt("pieza"),rs.getString("desp_pieza")));
				dto.setIndicador(rs.getString("indicador"));
				
				if(!dto.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente))
				{
					detSup.setCodigoPk(rs.getInt("superficie"));
					detSup.setNombre(rs.getString("nom_superficie"));
					detSup.setSector(rs.getInt("sector"));
//					detSup.setEliminar(ConstantesBD.acronimoSi);
//					detSup.setNuevo(ConstantesBD.acronimoNo);
//					detSup.setModificar(ConstantesBD.acronimoNo);
					dto.setSuperficie(detSup);
					
				}
				array.add(dto);
			}
		} catch (Exception e) {
			logger.info("ERROR Consulta-->"+strConsultarDetalleCompIndicePlaca);
			e.printStackTrace();
		}
		
		/*for(DtoDetalleIndicePlaca elem: array)
		{
			logger.info("###############################################################");
			logger.info("Codigo Pieza: "+elem.getPiezaDental().getCodigo());
			logger.info("Superficie: "+elem.getSuperficie().getCodigoPk()+" - "+elem.getSuperficie().getNombre()+" - "+elem.getSuperficie().getSector());
			logger.info("###############################################################");
		}*/
		
		return array;
	}
	
	/**
	 * Obtine el c&oacute;digo y el nombre de la superficie seg&uacute;n el sector, estado y la institucion
	 * @param codigoInstitucion
	 * @param activo
	 * @param sector
	 * @param piezaDental
	 * @return
	 */
	public static InfoDatosInt obtenerSuperficieDental(int codigoInstitucion, String activo, int sector, int piezaDental)
	{
		InfoDatosInt dato = new InfoDatosInt();
		String consulta = 
					"SELECT " +
						"sd.codigo as codigo, " +
						"sd.nombre as nombre, " +
						"ssc.sector as sector " +
					"FROM " +
						"historiaclinica.superficie_dental sd " +
					"INNER JOIN " +
						"odontologia.sector_superficie_cuadrante ssc " +
							"ON(ssc.superficie=sd.codigo) " +
					"WHERE " +
							"sd.cod_institucion = ? " +
						"AND " +
							"ssc.sector = ? " +
						"AND " +
							"pieza = ? ";
		try
		{
			if(!UtilidadTexto.isEmpty(activo))
			{
				if(activo.equals(ConstantesBD.acronimoSi))
				{
					consulta += "AND sd.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
				}
				else if (activo.equals(ConstantesBD.acronimoNo))
				{
					consulta += "AND sd.activo = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
				}
			}
			
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoInstitucion);
			ps.setInt(2, sector);
			ps.setInt(3, piezaDental);
			logger.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dato.setCodigo(rs.getInt("codigo"));
				dato.setNombre(rs.getString("nombre"));
				
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("ERROR Consulta: "+consulta);
		}
		return dato;
	}
	
	/**
	 * metodo que retorna una lista de antecedentes que requieren de alerta
	 * @param codigoPaciente
	 * @return ArrayList<DtoAntecedentesAlerta>
	 */
	public static ArrayList<DtoAntecedentesAlerta> obtenerAntecedentesAlerta(int codigoPaciente)
	{
		ArrayList<DtoAntecedentesAlerta> array  = new ArrayList<DtoAntecedentesAlerta>();  
		// 1. obtener el ultimo antecedente odontologico por confirmar en 'N' del paciente respectivo
		DtoAntecendenteOdontologico dtoAnteOdo = new DtoAntecendenteOdontologico();
		dtoAnteOdo = obtenerAntecedenteOdontologico(codigoPaciente);
		try{
			// 2. se construye la consulta segun la información obtenida
			if(dtoAnteOdo.getCodigoPk()>0)
			{
				String consulta = "select " +
					"getnombremedico("+dtoAnteOdo.getCodigoMedico()+") as nombre_medico, " +
					"coalesce(cp.nombre,' ') as nombre_alerta, " +
					"coalesce(etiqueta,' ') as etiqueta_alerta ";
				String table = "";
				String where = "where ";
				String order = "order by nombre_medico, nombre_alerta, etiqueta_alerta ";
				if(dtoAnteOdo.getValoracion()>0) // se verifican los antecedentes de alerta de la valoracion
				{
					table += "from historiaclinica.componentes_ingreso cing "+
						"inner join historiaclinica.campos_parametrizables cp ON (cp.codigo_pk = cing.campo_parametrizable) ";
					where += "cing.valoracion_odo = "+dtoAnteOdo.getValoracion()+" " +
						"and cing.generar_alerta = '"+ConstantesBD.acronimoSi+"' ";
				}else{
					if(dtoAnteOdo.getEvolucion()>0) // se verifican los antecedentes de alerta de la evolucion
					{
						table += "from historiaclinica.componentes_evolucion cevo " +
							"inner join historiaclinica.campos_parametrizables cp ON (cp.codigo_pk = cevo.campo_parametrizable) ";
						where += "cevo.evolucion_odonto = "+dtoAnteOdo.getEvolucion()+" " +
							"and cevo.generar_alerta = '"+ConstantesBD.acronimoSi+"' ";
					}
				}
				
				// 3. se hace la consulta
				consulta+=table+where+order;
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
				//logger.info("Consulta: "+ps);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					DtoAntecedentesAlerta dto = new DtoAntecedentesAlerta();
					dto.setNombreMedico(rs.getString("nombre_medico"));
					dto.setAntAlertaEtiqueta(rs.getString("etiqueta_alerta").equals(" ")?"":rs.getString("etiqueta_alerta"));
					dto.setAntAlertaNombre(rs.getString("nombre_alerta").equals(" ")?"":rs.getString("nombre_alerta"));
					dto.setFecha(dtoAnteOdo.getFechaModifica());
					dto.setHora(dtoAnteOdo.getHoraModifica());
					array.add(dto);
				 }
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}
	

	/**
	 * Método implementado para confirmar los antecedentes odontológicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean confirmarAntecedenteOdontologico(Connection con,HashMap campos)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		boolean existe = false;
		try
		{
			//**************SE TOMAN LOS PARÁMETROS**********************************************
			String loginUsuario = campos.get("loginUsuario").toString();
			int codigoMedico = Utilidades.convertirAEntero(campos.get("codigoMedico").toString());
			String codigoPlantillaIngreso = campos.get("codigoPlantillaIngreso").toString();
			String codigoPlantillaEvolucion = campos.get("codigoPlantillaEvolucion").toString();
			//***********************************************************************************
			
			//***********PRIMERO SE VERIFICA QUE HAYA UN HISTORIAL DE ANTECEDENTES ODONTOLOGICOS**************************
			String consulta = "SELECT count(1) as cuenta from historiaclinica.his_antecedentes_odo WHERE ";
			if(Utilidades.convertirALong(codigoPlantillaIngreso)>0)
			{
				consulta += " valoracion_odo IN (SELECT valoracion_odonto FROM historiaclinica.plantillas_ingresos WHERE codigo_pk = "+codigoPlantillaIngreso+") ";
			}
			else if(Utilidades.convertirALong(codigoPlantillaEvolucion)>0)
			{
				consulta += " evolucion_odo IN (SELECT evolucion_odonto FROM historiaclinica.plantillas_evolucion WHERE codigo_pk = "+codigoPlantillaEvolucion+") ";
			}
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					existe = true;
					
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			//*************************************************************************************************************
			
			if(existe)
			{
				consulta = "UPDATE historiaclinica.his_antecedentes_odo SET " +
					"codigo_medico = ?, " +
					"por_confirmar = ?," +
					"usuario_modifica = ?," +
					"fecha_modifica = current_date,hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE ";
				
				if(Utilidades.convertirALong(codigoPlantillaIngreso)>0)
				{
					consulta += " valoracion_odo IN (SELECT valoracion_odonto FROM historiaclinica.plantillas_ingresos WHERE codigo_pk = "+codigoPlantillaIngreso+") ";
				}
				else if(Utilidades.convertirALong(codigoPlantillaEvolucion)>0)
				{
					consulta += " evolucion_odo IN (SELECT evolucion_odonto FROM historiaclinica.plantillas_evolucion WHERE codigo_pk = "+codigoPlantillaEvolucion+") ";
				}
				pst = new PreparedStatementDecorator(con,consulta);
				pst.setInt(1,codigoMedico);
				pst.setString(2,ConstantesBD.acronimoNo);
				pst.setString(3,loginUsuario);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Falló el proceso de confirmación de los antecedentes odontológicos");
				}
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en confirmarAntecedenteOdontologico: ",e);
			resultado.setResultado(false);
			resultado.setDescripcion("Falló el proceso de confirmación de los antecedentes odontológicos: "+e);
			UtilidadBD.closeConnection(con);
		}
		//UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * metodo que obtiene la descripcion del servicios
	 * @param codigoTarifario
	 * @param codigoServicio
	 * @return
	 */
	public static String obenerNombreServicio (int codigoServicio, int codigoInstitucion)
	{
		String strConsultarNombreServicio = "SELECT " +
		" getcodigopropservicio(ser.codigo, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)+") ||' '||getnombreservicio(ser.codigo,?) AS descripcion_servicio " +
		"FROM facturacion.servicios ser " +
		"WHERE ser.codigo = ? " ;
		String nombreServicio = "";
		Connection con = UtilidadBD.abrirConexion();
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultarNombreServicio);
			ps.setInt(1, Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)));
			ps.setInt(2, codigoServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				nombreServicio = rs.getString("descripcion_servicio");
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}catch (Exception e) {
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
		}
		return nombreServicio;
	}
	
	/**
	 * Método implementado para consultar el id de la cuenta de una cita de odontologia
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static BigDecimal consultarIngresoCitaOdontologica(Connection con,BigDecimal codigoCita)
	{
		BigDecimal idIngreso = new BigDecimal(ConstantesBD.codigoNuncaValido);
		try
		{
			/**
			 * Lo que se busca es tomar la informacion de la primera solicitud asociada a la cita
			 */
			String consulta = "SELECT "+
				"c.id_ingreso as id_ingreso "+
				"FROM odontologia.citas_odontologicas co "+ 
				"INNER JOIN odontologia.servicios_cita_odontologica sco ON(sco.cita_odontologica = co.codigo_pk) "+ 
				"INNER JOIN ordenes.solicitudes s ON(s.numero_solicitud = sco.numero_solicitud) " +
				"INNER JOIN manejopaciente.cuentas c ON(c.id = s.cuenta) " +
				""+ 
				"WHERE co.codigo_pk = ? and sco.activo = ?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setBigDecimal(1, codigoCita);
			pst.setString(2,ConstantesBD.acronimoSi);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				idIngreso = rs.getBigDecimal("id_ingreso");
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarIngresoCitaOdontologica: ",e);
		}
		return idIngreso;
	}
	
	/**
	 * Método implementado para obtener el último registro de antecedentes odontologicos del paciente
	 * @param con
	 * @param dtoAntecedente
	 * @param camposParametrizables: atributo para validar que los ultimos antecedentes encontrados deben tener campos parametrizables
	 */
	public static void obtenerUltimoRegistroAntecedentesOdontologia(Connection con,DtoAntecendenteOdontologico dtoAntecedente,boolean camposParametrizables)
	{
		try
		{
			String consulta = "SELECT " +
				"coalesce(ha.valoracion_odo,"+ConstantesBD.codigoNuncaValido+") as valoracion," +
				"coalesce(ha.evolucion_odo,"+ConstantesBD.codigoNuncaValido+") as evolucion " +
				"from historiaclinica.his_antecedentes_odo ha " +
				"WHERE " +
				"ha.codigo_paciente = ? and ha.por_confirmar = ? " +
				"order by ha.codigo_pk desc";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,dtoAntecedente.getCodigoPaciente());
			pst.setString(2,ConstantesBD.acronimoNo);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				dtoAntecedente.setValoracion(rs.getInt("valoracion"));
				dtoAntecedente.setEvolucion(rs.getInt("evolucion"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
			if(camposParametrizables)
			{
			
				if(dtoAntecedente.getValoracion()>0)
				{
					consulta = "SELECT count(1) as cuenta from historiaclinica.componentes_ingreso WHERE valoracion_odo = ?";
					pst = new PreparedStatementDecorator(con,consulta);
					pst.setInt(1,dtoAntecedente.getValoracion());
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						if(rs.getInt("cuenta")<=0)
						{
							dtoAntecedente.setValoracion(ConstantesBD.codigoNuncaValido);
						}
					}
					else
					{
						dtoAntecedente.setValoracion(ConstantesBD.codigoNuncaValido);
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				}
				else if(dtoAntecedente.getEvolucion()>0)
				{
				
					//historiaclinica.componentes_evolucion
					consulta = "SELECT count(1) as cuenta from historiaclinica.componentes_evolucion WHERE evolucion_odonto = ?";
					pst = new PreparedStatementDecorator(con,consulta);
					pst.setInt(1,dtoAntecedente.getEvolucion());
					rs = new ResultSetDecorator(pst.executeQuery());
					
					if(rs.next())
					{
						if(rs.getInt("cuenta")<=0)
						{
							dtoAntecedente.setEvolucion(ConstantesBD.codigoNuncaValido);
						}
					}
					else
					{
						dtoAntecedente.setEvolucion(ConstantesBD.codigoNuncaValido);
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUltimoRegistroAntecedentesOdontologia: "+e);
		}
	}
	
	/**
	 * Método para consultar la cita de un servicio del plan de tratamiento
	 * @param con
	 * @param progServPlanT
	 * @return
	 */
	public static ArrayList<BigDecimal> consultarCitaXProgSerPlanTrat(BigDecimal progServPlanT)
	{
		ArrayList<BigDecimal> citaArray=new ArrayList<BigDecimal>();
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			
			String consulta = "SELECT cita_odontologica as cita " +
							" from odontologia.servicios_cita_odontologica sco" +
							" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
							" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
							" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
							" INNER JOIN  odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and pspt.servicio=sco.servicio AND pspt.activo = 'S' ) " +
							" where pspt.codigo_pk = ? and sco.activo = ?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setBigDecimal(1, progServPlanT);
			pst.setString(2,ConstantesBD.acronimoSi);
			//logger.info("\n consulta consultarCitaXProgSerPlanTrat >> "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				citaArray.add(rs.getBigDecimal("cita"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error tratando de consultar la cita de un servicio de un plan de tratamiento ",e);
		}
		return citaArray;
	}
	
	
	/**
	 * Método para consultar la cita de un servicio en Historico del plan de tratamiento
	 * @param progServPlanT
	 * @return
	 */
	public static ArrayList<BigDecimal> consultarCitaXProgSerPlanTratHisConf(BigDecimal progServPlanT, InfoPlanTratamiento infoPlanTrat)
	{
		ArrayList<BigDecimal> citaArray=new ArrayList<BigDecimal>();
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			
			String consulta = "SELECT cita_odontologica as cita " +
							" from odontologia.servicios_cita_odontologica sco" +
							" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
							" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
							" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
							" INNER JOIN  odontologia.his_conf_prog_serv_plan_t  pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and pspt.servicio=sco.servicio AND pspt.activo = 'S' ) " +
							" where pspt.codigo_pk = ? and sco.activo = ?";

			if(infoPlanTrat.getValoracion()!=null && infoPlanTrat.getValoracion().longValue()>0)
			{
				consulta += " and pspt.valoracion = ?";
			}
			else if(infoPlanTrat.getEvolucion()!=null && infoPlanTrat.getEvolucion().longValue()>0)
			{
				consulta += " and pspt.evolucion = ?";
			}
			
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setBigDecimal(1, progServPlanT);
			pst.setString(2,ConstantesBD.acronimoSi);
			
			if(infoPlanTrat.getValoracion()!=null && infoPlanTrat.getValoracion().longValue()>0)
			{
				pst.setBigDecimal(3, infoPlanTrat.getValoracion());
			}
			else if(infoPlanTrat.getEvolucion()!=null && infoPlanTrat.getEvolucion().longValue()>0)
			{
				pst.setBigDecimal(3, infoPlanTrat.getEvolucion());
			}
			//logger.info("\n consulta consultarCitaXProgSerPlanTrat >> "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				citaArray.add(rs.getBigDecimal("cita"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error tratando de consultar la cita de un servicio de un plan de tratamiento ",e);
		}
		return citaArray;
	}
	
	
	
	/**
	 * Método que verifica si un convenio de in ingreso esta relacionado a un presupuesto que está contratado
	 * @param con
	 * @param dtoSubCuenta
	 * @return
	 */
	public static boolean esConvenioRelacionadoAPresupuestoOdoContratado(Connection con,DtoSubCuentas dtoSubCuenta)
	{
		boolean resultado = false;
		try
		{
			String consulta = "SELECT "+
				"po.codigo_pk as codigo_presupuesto "+
				"FROM odontologia.presupuesto_odontologico po "+ 
				"INNER JOIN odontologia.view_presupuesto_totales_conv vptc ON(vptc.presupuesto = po.codigo_pk) "+ 
				"WHERE po.ingreso = ? and po.estado = ? and vptc.convenio = ? and vptc.contrato = ?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,dtoSubCuenta.getIngreso());
			pst.setString(2,ConstantesIntegridadDominio.acronimoContratadoContratado);
			pst.setInt(3,dtoSubCuenta.getConvenio().getCodigo());
			pst.setInt(4,dtoSubCuenta.getContrato());
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resultado = true;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en esConvenioRelacionadoAPresupuestoOdoContratado: ",e);
		}
		return resultado;
	}

	public static boolean validarTipoCitaInterconsulta(int codigoCuentaActiva, int codigoCuentaAsocio) 
	{
		String sentencia=	"SELECT " +
								"count(1) AS num_solicitudes " +
							"FROM " +
								"ordenes.solicitudes sol " +
							"WHERE ";
		if(codigoCuentaAsocio>0)
		{
			sentencia+="(sol.cuenta=? OR sol.cuenta=?) ";
		}
		else
		{
			sentencia+="sol.cuenta=? ";
		}
		sentencia+=	"AND " +
						"sol.tipo="+ConstantesBD.codigoTipoSolicitudInterconsulta+" " +
					"AND " +
						"sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada;

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd = new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, codigoCuentaActiva);
			if(codigoCuentaAsocio>0)
			{
				psd.setInt(2, codigoCuentaAsocio);
			}
			logger.info("\n\n ************* tipoCitaInterconsulta >>"+ psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				int cantidad=rsd.getInt("num_solicitudes");
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
				return cantidad>0?true:false;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
		} 
		catch (SQLException e) 
		{
			logger.error("Error validando tipo cita interconsulta ",e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param pieza
	 * @return
	 */
	public static String obtenerNombrePieza(int pieza)
	{
		String retorna="";
		String consultaStr="select descripcion from odontologia.pieza_dental where codigo_pk=?";
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consultaStr);
			pst.setInt(1, pieza);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				retorna = rs.getString(1);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error tratando de consultar nombre pieza ",e);
		}
		return retorna;
	}

	public static boolean pacienteConValoracionInicial(int codigoPersona,int idIngreso) 
	{
		boolean retorna=false;
		String consultaStr="SELECT count(1) from odontologia.citas_odontologicas where codigo_paciente=? and tipo='"+ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial+"' and estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoCitaOdontoAtendida+"', '"+ConstantesIntegridadDominio.acronimoAsignado+"')";
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consultaStr);
			pst.setInt(1, codigoPersona);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				
				retorna = rs.getInt(1)>0;
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(Exception e)
		{
			logger.error("Error tratando de consultar validacion ",e);
		}
		return retorna;
	}
	
	/**
	 * M&eacute;todo encargado de verificar que la cita no 
	 * tenga servicios asignados a otra cita de menor orden para la
	 * misma pieza.
	 * @param codigoCita C&oacute;digo de la cita que se desea verificar
	 * @return boolean True en caso de existir servicios de menor órden asignados
	 */
	public static boolean existeServicioAsignadoDeMenorOrden(int codigoCita)
	{
		Connection con=UtilidadBD.abrirConexion();
		
		String sentenciaOrdenMaximo=
				"SELECT max(pspt.orden_servicio) AS orden_maximo, php.codigo_pk AS codigo_proghallpieza " +
				"FROM odontologia.programas_servicios_plan_t pspt " +
				"INNER JOIN odontologia.det_plan_tratamiento dpt ON(dpt.codigo_pk=pspt.det_plan_tratamiento) " +
				"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.det_plan_trata=dpt.codigo_pk) " +
				"INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sxp.prog_hallazgo_pieza) " +
				"INNER JOIN odontologia.servicios_cita_odontologica sco ON(sco.programa_hallazgo_pieza=php.codigo_pk) " +
				"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk=sco.cita_odontologica) " +
				"WHERE " +
					" sco.cita_odontologica=? AND " +
					" co.estado='"+ConstantesIntegridadDominio.acronimoAsignado+"' " +
				"GROUP BY " +
					" php.codigo_pk";
		
		String sentenciaCantidadRegistrosOrdenmenor=
				"SELECT count(1) AS num_resultados " +
				"FROM odontologia.programas_servicios_plan_t pspt " +
				"INNER JOIN odontologia.det_plan_tratamiento dpt ON(dpt.codigo_pk=pspt.det_plan_tratamiento) " +
				"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.det_plan_trata=dpt.codigo_pk) " +
				"INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sxp.prog_hallazgo_pieza) " +
				"INNER JOIN odontologia.servicios_cita_odontologica sco ON(sco.programa_hallazgo_pieza=php.codigo_pk) " +
				"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk=sco.cita_odontologica) " +
				"WHERE " +
					"php.codigo_pk=? " +
					"AND " +
					"orden_servicio<? " +
					"AND " +
					"co.estado='"+ConstantesIntegridadDominio.acronimoAsignado+"' " +
					"AND " +
					"co.codigo_pk!=?";
		
		int orden=0;
		int codigoProgHallPiez=0;
		
		try {
			// Consulto el máximo orden de los servicios de la cita
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaOrdenMaximo);
			psd.setInt(1, codigoCita);
			Log4JManager.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				orden=rsd.getInt("orden_maximo");
				codigoProgHallPiez=rsd.getInt("codigo_proghallpieza");
			}
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			
		} catch (SQLException e) {
			Log4JManager.info("Error en la consulta de maximo órden servicios cita", e);
			UtilidadBD.closeConnection(con);
		}

		boolean existenRegistros=false;
		
		try {
			// Consulto el máximo orden de los servicios de la cita
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaCantidadRegistrosOrdenmenor);
			psd.setInt(1, codigoProgHallPiez);
			psd.setInt(2, orden);
			psd.setInt(3, codigoCita);
			Log4JManager.info(psd);
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				existenRegistros=rsd.getInt("num_resultados")>0;
			}
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			
		} catch (SQLException e) {
			Log4JManager.info("Error en la consulta de existencia de servicios cita con órden menor a "+orden, e);
			UtilidadBD.closeConnection(con);
		}

		UtilidadBD.closeConnection(con);
		return existenRegistros;
	}

	/**
	 * 
	 * @param superficie
	 * @return
	 */
	public static String obtenerNombreSuperficie(BigDecimal superficie) 
	{
		String retorna="";
		String consultaStr="select nombre from historiaclinica.superficie_dental where codigo=?";
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consultaStr);
			pst.setBigDecimal(1, superficie);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				retorna = rs.getString(1);
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error tratando de consultar nombre pieza ",e);
		}
		return retorna;
	}

	/**
	 * Metodo que obtiene el profesional asociado a la primera valoracion y plan de tratamiento del paciente ya confirmada
	 * @param codigoPersona
	 * @param idIngreso
	 * @return
	 */
	public static HashMap<String, Object> obtenerNombreSuperficie(int codigoPersona, int idIngreso) {
		return null;
		
	}

	/**
	 * Metodo que obtiene los servicios Asociados a una Solicitud InterConsulta
	 * @param codCuentaPaciente
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServiciosInterconsulta(int codCuentaPaciente, int codigoInstitucion, int codigoCita) {
		
		ArrayList<DtoServicioOdontologico> array= new ArrayList<DtoServicioOdontologico>();
		
		String codigoManEstandart=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
		
		if(codigoManEstandart.equals(""))
		{
			codigoManEstandart = ConstantesBD.codigoTarifarioCups+"";
		}
		
		String sentencia =	"SELECT " +
		                    "CASE WHEN detcar.servicio IS NOT NULL THEN detcar.servicio ELSE detcar.articulo END AS codigoser, " +   
		                    "CASE WHEN detcar.servicio IS NOT NULL THEN getcodigoespecialidad(detcar.servicio) || '-' || getcodigoservicio(detcar.servicio, "+codigoManEstandart+") ELSE detcar.articulo || '' END AS codigotarif, " +
		                    "CASE WHEN detcar.servicio IS NOT NULL THEN getnombreservicio(detcar.servicio,"+codigoManEstandart+") ELSE getdescarticulo(detcar.articulo) END AS descripcion,   " +
		                    "ser.especialidad AS especialidad, " +
		                    "getnombreespecialidad(ser.especialidad) AS nomespecialidad, " +
		                    "ser.minutos_duracion AS duracion, " +		                    
		                    "(SELECT ao.fecha " +
								"FROM odontologia.servicios_cita_odontologica sco " +
								"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
									"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
									"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
								"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
								"WHERE sco.numero_solicitud = sol.numero_solicitud ";
		if(codigoCita>0){
			sentencia+= "AND co.codigo_pk!= "+codigoCita+" ";
		}
		
		sentencia+= ValoresPorDefecto.getValorLimit1()+" 1) " +
			   "AS fecha_cita, " +
			   "(SELECT co.hora_inicio " +
					"FROM odontologia.servicios_cita_odontologica sco " +
					"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
						"(co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' " +
						"OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' )) " +
					"INNER JOIN odontologia.agenda_odontologica ao ON(co.agenda=ao.codigo_pk) " +
					"WHERE sco.numero_solicitud = sol.numero_solicitud " ;
		
		if(codigoCita>0){
			sentencia+="AND co.codigo_pk!= "+codigoCita+" ";
		}
				 
	    sentencia+=ValoresPorDefecto.getValorLimit1()+" 1) " +
			    " AS hora_cita, " +	                    
                "(SELECT CASE WHEN  COUNT( sco.numero_solicitud)>0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END " +
        		" FROM odontologia.servicios_cita_odontologica sco " +
        		" INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica AND " +
        		" (co.estado= '"+ConstantesIntegridadDominio.acronimoReservado+"' OR co.estado= '"+ConstantesIntegridadDominio.acronimoAsignado+"' OR co.estado= '"+ConstantesIntegridadDominio.acronimoReprogramado+"' OR co.estado= '"+ConstantesIntegridadDominio.acronimoAtendida+"')) " +
        		" WHERE sco.numero_solicitud = sol.numero_solicitud  " ;
	    
       if(codigoCita>0){
    	   sentencia+="AND co.codigo_pk!= "+codigoCita+" ";
       }								 

	   sentencia+=	" ) AS esta_vin_ser ";
	   
	   
	   if(codigoCita>0){
    	   sentencia+=", sco.cita_odontologica AS codigoCita "+
		    	   	  " FROM odontologia.servicios_cita_odontologica sco, "+
					  " facturacion.det_cargos detcar, "+
					  " odontologia.citas_odontologicas co, "+
					  " ordenes.solicitudes sol, "+
					  " facturacion.servicios ser ";

       }else{
    	   
    	   sentencia+="FROM facturacion.det_cargos detcar, "+
			  " ordenes.solicitudes sol, "+
			  " facturacion.servicios ser ";
       }
	   
	   
	   
//	   sentencia+= "FROM facturacion.det_cargos detcar,  " +
//	                " odontologia.servicios_cita_odontologica sco " +
//	                "INNER JOIN ordenes.solicitudes sol ON (sol.numero_solicitud = detcar.solicitud ) " +
//	                "INNER JOIN facturacion.servicios ser ON (ser.codigo = detcar.servicio ) " +
//	                
//					"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk = sco.cita_odontologica) ";
	                
		
	   sentencia+="WHERE sol.cuenta = ?  AND " +
		"sol.tipo="+ConstantesBD.codigoTipoSolicitudInterconsulta+" " +
		"AND sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada +
		" AND sol.numero_solicitud = detcar.solicitud AND ser.codigo = detcar.servicio ";
	   
	   if(codigoCita>0){
		   
    	   sentencia+=" AND  sco.servicio = detcar.servicio AND sco.cita_odontologica = "+codigoCita  +" AND co.codigo_pk = sco.cita_odontologica ";

       }
	   
	   try{
		   
		   Connection con = UtilidadBD.abrirConexion();
		   PreparedStatementDecorator psd = new PreparedStatementDecorator(con, sentencia);
		   psd.setInt(1, codCuentaPaciente);
		   logger.info("Consulta Servicios Interconsulta >>"+psd);
		   ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
		   
		   while(rsd.next()){
		   
			   DtoServicioOdontologico dtoServicio= new DtoServicioOdontologico();
			   dtoServicio.setCodigoServicio(rsd.getInt("codigoser"));
			   dtoServicio.setCodigoServTarifario(rsd.getString("codigotarif"));
			   dtoServicio.setDescripcionServicio(rsd.getString("codigotarif")+" - " + rsd.getString("descripcion")+" - "+rsd.getString("nomespecialidad"));
			   dtoServicio.setEspecialidad(rsd.getInt("especialidad"));
			   dtoServicio.setMinutosduracion(rsd.getInt("duracion"));
			   dtoServicio.setMinutosduracionNuevos(rsd.getInt("duracion"));
			   dtoServicio.setHabilitarSeleccion(ConstantesBD.acronimoSi);
			   dtoServicio.setEstaVinculadoSerCita(rsd.getString("esta_vin_ser"));
			   dtoServicio.setHoraCita(rsd.getString("hora_cita"));
			   dtoServicio.setFechaCita(rsd.getString("fecha_cita"));
			   array.add(dtoServicio);
		   }
	   
		   SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
	
		}catch (SQLException e){
			
			logger.error("Error listando Servicios  ",e);
		}
		
		return array;
	}
	
	
	/**
	 * M&eacute;todo que obtiene los c&oacute;digos de los servicios asociados a un paciente,
	 * con un estado y tipo de cita que debe estar asociada a un ingreso espec&iacute;fico
	 * 
	 * Si se envia el código de la cita, lo involucra en el filtro.
	 * 
	 * @param codigoPaciente
	 * @param codigoIngreso
	 * @param estadoCita
	 * @param tipoCita
	 * @param codigoCita 
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServiciosXEstadoCitaXTipoCita (int codigoPaciente, int codigoIngreso, String estadoCita, String tipoCita, int codigoCita) {
	
		ArrayList<DtoServicioOdontologico> servicios = new ArrayList<DtoServicioOdontologico>();
		
		String sentencia =	"SELECT sco.codigo_pk as codigo, sco.servicio as codigoServicio, sco.programa_hallazgo_pieza as codigoPrograma, sco.cita_odontologica as codigoCita " +
							"FROM servicios_cita_odontologica sco, odontologia.citas_odontologicas co, "+
								"manejopaciente.ingresos ingreso" +
							" WHERE sco.cita_odontologica = co.codigo_pk "+
								" AND ingreso.codigo_paciente = co.codigo_paciente " +
								" AND co.codigo_pk  NOT IN " +
								" (SELECT caap.cita_programada FROM odontologia.citas_asociadas_a_programada caap) "+
								" AND co.codigo_paciente = ? AND ingreso.id = ? AND co.estado = ? AND co.tipo = ? ";
	   try{
		   
		   Connection con = UtilidadBD.abrirConexion();
		   
		   if(codigoCita > ConstantesBD.codigoNuncaValido){
			   
			   sentencia += " AND sco.cita_odontologica = ?";
		   }
		   
		   PreparedStatementDecorator psd = new PreparedStatementDecorator(con, sentencia);
		   
		   psd.setInt(1, codigoPaciente);
		   psd.setInt(2, codigoIngreso);
		   psd.setString(3, estadoCita);
		   psd.setString(4, tipoCita);
		   
		   if(codigoCita > ConstantesBD.codigoNuncaValido){
			   
			   psd.setInt(5, codigoCita);
		   }
 
		   logger.info("Consulta Servicios >> "+psd);
		   ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
		   
		  // codigoServicios = new HashMap<Integer, Integer>();
	
		   while(rsd.next()){
			   
			   DtoServicioOdontologico servicio = new DtoServicioOdontologico();
			   
			   servicio.setCodigoPk(rsd.getLong("codigo"));
			   servicio.setCodigoServicio(rsd.getInt("codigoServicio"));
			   servicio.getProgramaHallazgoPieza().setCodigoPk(rsd.getInt("codigoPrograma"));
			   
			   if(rsd.getInt("codigoCita")+"" != null){
				   servicio.setCodigoCita(rsd.getInt("codigoCita"));
			   }
			   
			   servicios.add(servicio);
		   }
	   
		   SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
	
		}catch (SQLException e){
			
			logger.error("Error listando Servicios  ",e);
		}
		
		return servicios;
	}
	
	/**
	 * Metodo utilizado para validar si Un profesional Tiene Asociado un Servicio 
	 * según la parametrica de  servicios adicionales por profesional de atención Odontológica
	 * @param codigoServicio
	 * @param codigoMedico
	 * @param codUnidadConsulta
	 * @param codInstitucion
	 * @return
	 */
	public static boolean profesionalTieneAsosiadoServicioAdd( int codigoServicio, int codigoMedico, int codInstitucion)
	{
      boolean existe=false;
      String sentencia="SELECT COUNT(*) AS numser " +
      		           " FROM " +
	      		           "odontologia.serv_adicionales_profesionales sap "+	      		        
			               "WHERE sap.codigo_servicio = "+codigoServicio+" " +
			               "AND sap.codigo_medico = "+codigoMedico+" AND sap.institucion = "+codInstitucion+" "; 
      
      try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd = new PreparedStatementDecorator(con, sentencia);
			logger.info("Consulta Servicio Adicional Profesional  >>"+psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			if(rsd.next())
			 {
				if(rsd.getInt("numser")>0)
				{
					existe= true;
				}
			 }
						
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
		
		} 
		catch (SQLException e) 
		{
			logger.error("Error Consultando servicios Adicionales Profesional ",e);
		}
      
      return existe;
	}
	
	
	
	/**
	 * Metodo utilizado para Obtener el Numero de Solicitud de un Servicio de InterConsulta
	 * @param codigoServicio
	 * @return
	 */
	public static int consultarNumeroSolicitudServicio(int codigoServicio, int codCuentaPaciente)
	{
		
		String consulta= " SELECT sol.numero_solicitud AS numsolicitud " +
		                 "   FROM facturacion.det_cargos detcar  " +
                         "   INNER JOIN ordenes.solicitudes sol ON (sol.numero_solicitud = detcar.solicitud ) " +
                         "   INNER JOIN facturacion.servicios ser ON (ser.codigo = detcar.servicio ) " +		
		                 "   WHERE ser.codigo = ?  " +
		                 "   AND  sol.cuenta = ? " +
		                 "   AND sol.tipo="+ConstantesBD.codigoTipoSolicitudInterconsulta+" ";		
			try 
			{
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator psd = new PreparedStatementDecorator(con, consulta);
				psd.setInt(1, codigoServicio);
				psd.setInt(2, codCuentaPaciente);
				
				logger.info("Consulta Numero Solicitud Servicio  >>"+psd);
				ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
				
				if(rsd.next())
				 {
					if(rsd.getInt("numsolicitud")>0)
					{
						return rsd.getInt("numsolicitud");
					}
				 }
							
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
			
			} 
			catch (SQLException e) 
			{
				logger.error("Error Consultando Numero Solicitud Servicio ",e);
			}
			
		
	   return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param codigoProgramaServicioPT
	 * @return
	 */
	public static int obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(Connection con, int codigoProgramaServicioPT) 
	{
		String consulta= " select prog_hallazgo_pieza from odontologia.programas_servicios_plan_t pspt inner join odontologia.det_plan_tratamiento dpt on (dpt.codigo_pk=pspt.det_plan_tratamiento) inner join odontologia.superficies_x_programa sxp on(sxp.det_plan_trata=dpt.codigo_pk)  where pspt.codigo_pk=?";
		int valor=ConstantesBD.codigoNuncaValido;
		try 
		{
			PreparedStatementDecorator psd = new PreparedStatementDecorator(con, consulta);
			psd.setInt(1, codigoProgramaServicioPT);
			
			logger.info("Consulta >>"+psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			if(rsd.next())
			{
				if(rsd.getInt(1)>0)
				{
					valor=rsd.getInt(1);
				}
			}
						
			rsd.close();
			psd.close();
			
		} 
		catch (SQLException e) 
		{
			logger.error("Error",e);
		}
		
		
		return valor;
	}

	/**
	 * 
	 * @param con
	 * @param estado
	 * @param codigoPAciente 
	 * @return
	 */
	public static boolean pacienteConPlanTratamientoEnEstado(Connection con,String estado, int codigoPAciente) 
	{
		boolean existe=false;
	    String sentencia="SELECT count(1) from odontologia.plan_tratamiento where codigo_paciente =? and estado=?"; 
	      
	      try 
			{
				PreparedStatementDecorator psd = new PreparedStatementDecorator(con, sentencia);
				psd.setInt(1, codigoPAciente);
				psd.setString(2, estado);
				ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
				
				if(rsd.next())
				 {
					if(rsd.getInt(1)>0)
					{
						existe= true;
					}
				 }
							
				psd.close();
				rsd.close();
			} 
			catch (SQLException e) 
			{
				logger.error("Error Consultando servicios Adicionales Profesional ",e);
			}
	      
	      return existe;
	}

	/**
	 * 
	 * @param con
	 * @param estados
	 * @param codigoPAciente
	 * @return
	 */
	public static boolean pacienteConPlanTratamientoEnEstados(Connection con,ArrayList<String> estados, int codigoPAciente) 
	{
		String estado="''";
		for(String varEstado:estados)
			estado=estado+",'"+varEstado+"'";
		boolean existe=false;
	    String sentencia="SELECT count(1) from odontologia.plan_tratamiento where codigo_paciente =? and estado in ("+estado+")"; 
	      
	      try 
			{
				PreparedStatementDecorator psd = new PreparedStatementDecorator(con, sentencia);
				psd.setInt(1, codigoPAciente);
				ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
				
				if(rsd.next())
				 {
					if(rsd.getInt(1)>0)
					{
						existe= true;
					}
				 }
							
				psd.close();
				rsd.close();
			} 
			catch (SQLException e) 
			{
				logger.error("Error Consultando servicios Adicionales Profesional ",e);
			}
	      
	      return existe;
	}


	
	
	
}

