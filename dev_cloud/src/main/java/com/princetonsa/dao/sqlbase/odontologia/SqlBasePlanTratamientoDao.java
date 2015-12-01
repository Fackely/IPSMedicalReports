package com.princetonsa.dao.sqlbase.odontologia;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.InfoIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoIngresoPlanTratamiento;
import util.odontologia.InfoNumSuperficiesPresupuesto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoDetallePlanTratamiento;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogDetPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSuperficieDental;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.odontologia.ComponenteOdontograma;
import com.princetonsa.mundo.odontologia.PlanTratamiento;

/**
 * 
 * @author axioma
 *
 */
public class SqlBasePlanTratamientoDao {

	/**
	* InsertarStrProgromasServicioPlan
	*/
	private static String InsertarStrProgromasServicioPlan="insert into odontologia.programas_servicios_plan_t " +
																		" (codigo_pk, "+//1            
																		" det_plan_tratamiento, "+ //2 
																		" programa, "+     //3       
																		"servicio, "+     //4        
																		"estado_programa, "+ //5     
																		"motivo, "+             //6   
																		" convencion ,"+         //7
																		" inclusion, "+            //8
																		" exclusion, "+           //9
																		" garantia ,"+            //10
																		" estado_servicio ,"+     //11
																		" indicativo_programa, "+ //12
																		" indicativo_servicio, "+//13
																		" por_confirmar, "+     //14 
																		" usuario_modifica, "+  //15
																		" fecha_modifica,"+    	//16
																		" hora_modifica, "+     //17 
																		" especialidad ," +		//18
																		" orden_servicio ," +	//19
																		" activo," + //20
																		" cita,	" + //21
																		" valoracion," + //22
																		" evolucion, " +//23
																		" estado_autorizacion)" + //24
																		"values " +
																		"(	" +
																			"? , ?, " +//2
																			"? , ? , " +//4
																			"? , ? , " +//6
																			"? , ? , " +//8
																			"? , ? , " +//10
																			"? , ? , " +//12
																			"? , ? , " +//14
																			"? , ? , " +//16
																			"? , ? , " +//18
																			"? , ? , " +//20
																			"? , ? ," +//22
																			"? , ?)";//24

	/**
	* Inserta datos en la tabla plan tratamiento 
	* */
	private static String InsertarStrPlanTratamientoOdo = "INSERT INTO odontologia.plan_tratamiento (" +
														 "codigo_pk," +//1
														 "consecutivo," +//2
														 "codigo_paciente," +//3
														 "ingreso," +//4
														 "especialidad," +//5
														 "fecha_grabacion," +//6
														 "hora_grabacion," +//7
														 "usuario_grabacion," +//8
														 "odontograma_diagnostico," +//9
														 "estado," +//10
														 "motivo," +//11
														 "odontograma_evolucion," +//12
														 "indicativo," +//12
														 "por_confirmar," +//14
														 "institucion," +//15
														 "centro_atencion," +//16
														 "usuario_modifica," +//17
														 "fecha_modifica," +//18
														 "hora_modifica," + //19
														 "valoracion, " +//20
														 "evolucion " +//21
														 ") " +
														 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	/**
	* Inserta datos en la tabla detalle de plan de tratamiento
	* */
	private static String insertarStrDetPlanTratamiento = "INSERT INTO odontologia.det_plan_tratamiento ( " +
														 "codigo_pk," + //1
														 "plan_tratamiento," + //2
														 "pieza_dental," +//3
														 "superficie," +//4
														 "hallazgo," +//5
														 "seccion," +//6
														 "clasificacion," +//7
														 "por_confirmar," +//8
														 "convencion," +//9
														 "usuario_modifica," +//10
														 "fecha_modifica," +//11
														 "hora_modifica," +//12
														 "especialidad," +//13
														 "activo," +//14
														 "cita," +//15
														 "valoracion," +//16
														 "evolucion " +//17
														 ") " +
														 "VALUES(?," + //1
														 "?," +//2
														 "?," +//3
														 "?," + //4
														 "?," + //5
														 "?," + //6
														 "?," + //7
														 "?," + //8
														 "?," + //9
														 "?," + //10
														 "?," + //11
														 "?," + //12
														 "?," + //13
														 "?," +//14
														 "?," +//15
														 "?," +//16
														 "? ) "; //17
	
	//**********************************************************************************************************************************************
	//***********************************  ANEXO 880 PLAN DE TRATAMIENTO ***************************************************************************
	
	/**
	* Cadena Sql para consultar Plan de tratamiento Asociado a un Paciente cuya via ingreso sea Consulta Externa 
	*/
	private static final String consultarPlandeTratamientoStr = "SELECT  " +
																"plt.codigo_pk AS codigopk, " +
																"plt.consecutivo AS consecutivo, " +
																"plt.codigo_paciente AS codpaciente, " +
																"plt.ingreso AS ingreso, " +
																"plt.fecha_grabacion AS fechagrabacion, " +
																"plt.hora_grabacion AS horagrabacion, " +
																"plt.odontograma_diagnostico AS odontogramadiag, " +
																"plt.estado AS estado, " +
																"coalesce(plt.odontograma_evolucion,0) AS odontogramaevol, " +
																"plt.por_confirmar AS porconfirmar, " +
																"plt.institucion AS institucion, " +
																"plt.centro_atencion AS centroatencion " +
																"FROM odontologia.plan_tratamiento plt " +
																"INNER JOIN ingresos ing ON (ing.id = plt.ingreso) " +
																"INNER JOIN cuentas cu ON (cu.id_ingreso = ing.id)  " +
																"WHERE  ing.codigo_paciente = ? AND cu.via_ingreso = "+ConstantesBD.codigoViaIngresoConsultaExterna+" " +
																"ORDER BY ing.fecha_ingreso DESC "; 
	
	/**
	* Cadena Sql para Consultar el estado por_confirmar en la Tabla log de Plan de Tratamiento
	*/
	private static final String consultaLogPlanTratamiento = "SELECT " +
															"codigo_pk AS codigopk, " +
															"plan_tratamiento AS plantratamiento, " +
															"por_confirmar AS porconfirmar," +
															"codigo_medico AS codigomedico, " +
															"fecha AS fecha, " +
															"hora AS hora " +
															"FROM odontologia.log_plan_tratamiento " +
															"WHERE plan_tratamiento = ? ORDER BY codigo_pk DESC ";
	
	
	
	/**
	* Cadena Sql para consultar los antecedetnes odontologicos de un paciente
	*/
	private static final String consultarHistAntecedenteOdontologia = "SELECT " +
															"codigo_pk AS codigopk, " +
															"codigo_paciente AS codigopaciente, " +
															"ingreso AS ingreso, " +
															"centro_atencion AS centroatencion, " +
															"institucion AS institucion, " +
															"codigo_medico AS codmedico," +
															"especialidad AS especialidad, " +
															"por_confirmar AS porconfirmar, " +
															"observaciones AS observaciones, " +
															"mostrar_por AS mostrarpor, " +
															"valoracion_odo AS valoracionodo, " +
															"evolucion_odo AS evulucionodo, " +
															"usuario_modifica  AS usuariomodifica, " +
															"fecha_modifica  AS fechamodifica, " +
															"hora_modifica AS horamodifica " +
															"FROM historiaclinica.his_antecedentes_odo " +
															"WHERE codigo_paciente = ?";
	
	
	//*************************************************************************************************
	//*************************************************************************************************
	//PLAN TRATAMIENTO ODONTOGRAMA*********************************************************************
	
	/**
	* Cadena Sql para consultar Plan de tratamiento 
	*/
	private static final String consultarPlandeTratamientoPrincipalStr = "SELECT  " +
																		"plt.codigo_pk AS codigopk, " +
																		"plt.consecutivo AS consecutivo, " +
																		"plt.codigo_paciente AS codpaciente, " +
																		"plt.ingreso AS ingreso, " +
																		"plt.fecha_grabacion AS fechagrabacion, " +
																		"plt.hora_grabacion AS horagrabacion, " +
																		"plt.odontograma_diagnostico AS odontogramadiag, " +
																		"plt.estado AS estado, " +
																		"coalesce(plt.odontograma_evolucion,0) AS odontogramaevol, " +
																		"plt.por_confirmar AS porconfirmar, " +
																		"plt.institucion AS institucion, " +
																		"plt.centro_atencion AS centroatencion ," +
																		"plt.usuario_modifica as usuarioModifica, " +
																		"plt.fecha_modifica as fechaModifica," +
																		"plt.hora_modifica as horaModifica, "+
																		"plt.valoracion as valoracion, "+
																		"plt.evolucion as evolucion "+
																		"FROM odontologia.plan_tratamiento plt " +
																		"WHERE plt.institucion = ? ";
	
	/**
	* Cadena Sql para consultar Plan de tratamiento 
	*/
	private static final String consultarPlandeTratamientoPrincipalHistConfStr = "SELECT  " +
																		"plt.codigo_pk AS codigopk, " +
																		"plt.consecutivo AS consecutivo, " +
																		"plt.codigo_paciente AS codpaciente, " +
																		"plt.ingreso AS ingreso, " +
																		"plt.fecha_grabacion AS fechagrabacion, " +
																		"plt.hora_grabacion AS horagrabacion, " +
																		"plt.odontograma_diagnostico AS odontogramadiag, " +
																		"plt.estado AS estado, " +
																		"coalesce(plt.odontograma_evolucion,0) AS odontogramaevol, " +
																		"plt.por_confirmar AS porconfirmar, " +
																		"plt.institucion AS institucion, " +
																		"plt.centro_atencion AS centroatencion " +
																		"FROM odontologia.his_conf_plan_tratamiento plt " +
																		"WHERE plt.institucion = ? ";
	
	/**
	* Consulta un programa
	* */
	private static final String consultarProgramaGenerico = 
															"SELECT " +
															"progra.codigo as codigoPkProgramaServicio," +
															"progra.codigo_programa," +
															"progra.nombre  as nombre," +
															"coalesce(co.archivo_convencion,'') as archivo_convencion " +
															"FROM odontologia.programas progra " +
															"LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON (co.consecutivo = progra.convencion) " +
															"WHERE progra.codigo = ? AND progra.activo = '"+ConstantesBD.acronimoSi+"'  ";

	
	/**
	* 
	* */
	private static final String consultarServProgramaParamStr = 
																	"SELECT " +
																	"dp.servicio as codigoPkServicio," +
																	"getnombreservicio(dp.servicio, "+ConstantesBD.separadorSplitComplejo+")  as nombre, " +
																	"getcodigoservicio(dp.servicio, "+ConstantesBD.separadorSplitComplejo+") as codigoamostrar, "+
																	"dp.orden " +
																	"FROM " +
																	"odontologia.detalle_programas dp " +
																	"WHERE " +
																	"dp.programas = ? AND activo = '"+ConstantesBD.acronimoSi+"' " +
																	"ORDER BY dp.orden ASC ";
	
	/**
	* Consulta los servicios parametrizados de un hallazgo
	* */
	private static final String consultarServiciosParamHallazgoStr = 
																	"SELECT DISTINCT " +
																	"case when (detHalla.servicio is null or detHalla.servicio<=0) then detHalla.programa else detHalla.servicio end as codigoPkProgramaServicio," +
																	"getnombreservicio(detHalla.servicio, "+ConstantesBD.separadorSplitComplejo+")  as nombre, " +
																	"getcodigoservicio(detHalla.servicio, "+ConstantesBD.separadorSplitComplejo+") as codigoamostrar, " +
																	"detHalla.orden," +
																	"detHalla.por_defecto," +
																	"detHalla.numero_superficies "+
																	"FROM odontologia.hallazgos_odontologicos hallazgo " +
																	"INNER JOIN odontologia.hallazgos_vs_prog_ser hallaPro ON (hallazgo.consecutivo = hallaPro.hallazgo) "+
																	"INNER JOIN odontologia.det_hall_prog_ser detHalla ON(detHalla.hallazgo_vs_prog_ser = hallaPro.codigo) "+
																	"WHERE detHalla.servicio IS NOT NULL ? " +
																	"ORDER BY detHalla.por_defecto DESC, detHalla.orden ASC ";

	/**
	* 
	* */
	private static String InsertarStrLogPlanTratamiento = "INSERT INTO odontologia.log_plan_tratamiento ( " +
														 "codigo_pk," +//1
														 "plan_tratamiento," +//2
														 "estado," +//3
														 "motivo," +//4
														 "especialidad," +//5
														 "codigo_medico," +//6
														 "por_confirmar," +//7
														 "cita," +//8
														 "fecha," +//9
														 "hora," +//10
														 "valoracion," +//11
														 "evolucion," +//12
														 "imagen ) " +//13
														 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
	/**
	* 
	* */
	private static String InsertarStrLogDetPlanTratamiento = "INSERT INTO odontologia.log_det_plan_tratamiento ( " +
															"codigo_pk," + //1
															"det_plan_tratamiento," + //2
															"pieza_dental," +//3
															"superficie," +//4
															"hallazgo," +//5
															"clasificacion," +//6
															"por_confirmar," +//7
															"convencion," +//8
															"usuario_modifica," +//9
															"fecha_modifica," +//10
															"hora_modifica," +//11
															"cita," +//12
															"valoracion," +//13
															"evolucion," +//14
															"especialidad " +//15
															") " +
															"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	* 
	* */
	private static String InsertarStrLogProgramasServiciosPlanT = "INSERT INTO odontologia.log_programas_servicios_plan_t (" +
																 "codigo_pk," + //1
																 "programa_servicio_plan_t," +//2
																 "estado_programa," +//3
																 "motivo," +//4
																 "convencion," +//5
																 "inclusion," +//6
																 "exclusion," +//7
																 "garantia," +//8
																 "estado_servicio," +//9
																 "indicativo_programa," +//10
																 "indicativo_servicio," +//11
																 "por_confirmar," +//12
																 "usuario_modifica," +//13
																 "fecha_modifica," +//14
																 "hora_modifica," +//15
																 "especialidad," +//16
																 "valoracion," +//17
																 "evolucion," +//18
																 "orden_servicio," +//19
																 "activo " +//20
																 ") " +
																 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	* sentencia sql que inserta en el his_conf_plan_tratamiento segun los datos del plan de tratamiento
	*/
	private static final String strInsertHisConfPlanTratamiento = "INSERT INTO odontologia.his_conf_plan_tratamiento ( " +
						"codigo_pk, " +
						"consecutivo, " +
						"codigo_paciente, " +
						"ingreso, " +
						"especialidad, " +
						"fecha_grabacion, " +
						"hora_grabacion, " +
						"usuario_grabacion, " +
						"odontograma_diagnostico, " +
						"estado, " +
						"motivo, " +
						"odontograma_evolucion, " +
						"indicativo, " +
						"por_confirmar, " +
						"institucion, " +
						"centro_atencion, " +
						"usuario_modifica, " +
						"fecha_modifica, " +
						"hora_modifica, " +
						"valoracion, " +
						"evolucion, " +
						"cita, " +
						"imagen, " +
						"codigo_medico, " +
						"plan_tratamiento) " + 
						"SELECT  " +
						"nextval('odontologia.seq_his_conf_plan_t'), consecutivo, " +
						"codigo_paciente, " +
						"ingreso,especialidad, " +
						"fecha_grabacion, " +
						"hora_grabacion, " +
						"usuario_grabacion, " +
						"odontograma_diagnostico, " +
						"estado, " +
						"motivo, " +
						"odontograma_evolucion, " +
						"indicativo, " +
						"por_confirmar, " +
						"institucion, " +
						"centro_atencion, " +
						"usuario_modifica, " +
						"fecha_modifica, " +
						"hora_modifica, " +
						"?, " +
						"?, " +
						"?, " +
						"imagen, " +
						"codigo_medico, " +
						"codigo_pk  " +
						"FROM  odontologia.plan_tratamiento " + 
						"WHERE codigo_pk = ? ";
	
	/**
	* sentencia sql que inserta el his_conf_prog_serv_plan_t
	*/
	private static final String strInsertHisConfProgramasServiciosPlanTrat = "INSERT INTO odontologia.his_conf_prog_serv_plan_t ( " +
						"codigo_pk, " +
						"det_plan_tratamiento, " +
						"programa, " +
						"servicio, " +
						"estado_programa, " +
						"motivo, " +
						"convencion, " +
						"inclusion, " +
						"exclusion, " +
						"garantia, " +
						"estado_servicio, " +
						"indicativo_programa, " +
						"indicativo_servicio, " +
						"por_confirmar, " +
						"usuario_modifica, " +
						"fecha_modifica, " +
						"hora_modifica, " +
						"especialidad, " +
						"orden_servicio, " +
						"activo, " +
						"estado_autorizacion, " +
						"valoracion, " +
						"evolucion, " +
						"cita, " +
						"programa_servicio_plan_t ) " +
						"SELECT nextval('odontologia.seq_his_conf_prog_serv_pt'), " +
						"det_plan_tratamiento," +
						"programa," +
						"servicio," +
						"estado_programa," +
						"motivo," +
						"convencion," +
						"inclusion," +
						"exclusion," +
						"garantia," +
						"estado_servicio," +
						"indicativo_programa," +
						"indicativo_servicio," +
						"por_confirmar," +
						"usuario_modifica," +
						"fecha_modifica," +
						"hora_modifica," +
						"especialidad," +
						"orden_servicio," +
						"activo," +
						"estado_autorizacion," +
						"?," +
						"?," +
						"?," +
						"codigo_pk " +
						"FROM odontologia.programas_servicios_plan_t " +
						"WHERE det_plan_tratamiento = ? " +
						"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	* Cadena que verifica si un detalle de plan de tratamiento tiene detalle
	*/
	private static final String verificarDetalleProgramasServiciosStr = "SELECT " +
			"count(1) as cuenta " +
			"FROM odontologia.programas_servicios_plan_t " +
			"WHERE det_plan_tratamiento = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	 * Cadena que verifica si un plan de tratamiento tiene detalle
	 */
	private static final String verificarDetallePlanTratamientoStr =  "SELECT " +
			"count(1) as cuenta " +
			"FROM odontologia.det_plan_tratamiento " +
			"WHERE plan_tratamiento  = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	* Actualiza informacion de la tabla de detalle del plan de tratamiento
	* */
	private static String ActualizarStrDetPlanT = "UPDATE odontologia.det_plan_tratamiento SET ";
	
	/**
	* sentencia sql que verifica si se cambia el estado del programa a terminado
	*/
	private static String strVerificarEstSerProg = "select " +
			"case when count(codigo_pk)>0 then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as terminar_prog " +
			"from odontologia.programas_servicios_plan_t " +
			"where det_plan_tratamiento = ? " +
			"and programa = ? " +
			"and servicio<> ? " +
			"and (estado_servicio = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' or estado_servicio ='"+ConstantesIntegridadDominio.acronimoContratado+"' ) ";
	
	/**
	* sentencia sql que verifica si se cambia el estado del programa a contratado
	*/
	private static String strVerificarEstSerProg1 = "select " +
			"case when count(codigo_pk)>0 then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as contratado_prog " +
			"from odontologia.programas_servicios_plan_t " +
			"where det_plan_tratamiento = ? " +
			"and programa = ? " +
			"and servicio<> ? " +
			"and (estado_servicio <> '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' and estado_servicio <> '"+ConstantesIntegridadDominio.acronimoContratado+"' ) ";
	
	private static String strActualizarEstadoProg = "UPDATE odontologia.programas_servicios_plan_t SET " +
			"estado_programa = ?, " +
			"usuario_modifica = ?, " +
			"fecha_modifica = CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE det_plan_tratamiento = ? " +
			"AND programa = ? ";
	
	/**
	* Cadena que consulta el historial encabezado del plan det ratamietno
	*/
	private static final String consultarHisConfPlanTratamientoStr = "SELECT "+ 
		"hcpt.codigo_pk as codigo_pk, "+
		"hcpt.consecutivo as consecutivo, "+
		"hcpt.codigo_paciente as codigo_paciente, "+
		"hcpt.ingreso as ingreso, "+
		"hcpt.especialidad as especialidad, "+
		"to_char(hcpt.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_grabacion, "+
		"hcpt.hora_grabacion as hora_grabacion, "+
		"hcpt.usuario_grabacion as usuario_grabacion, "+
		"hcpt.odontograma_diagnostico as odontograma_diagnostico, "+
		"hcpt.estado as estado, "+
		"hcpt.motivo as motivo, "+
		"hcpt.odontograma_evolucion as odontograma_evolucion, "+ 
		"hcpt.indicativo as indicativo, "+
		"hcpt.por_confirmar as por_confirmar, "+ 
		"hcpt.institucion as institucion, "+
		"hcpt.centro_atencion as centro_atencion, "+
		"hcpt.usuario_modifica as usuario_modifica, "+
		"to_char(hcpt.fecha_modifica,'"+ConstantesBD.formatoFechaBD+"') as fecha_modifica, "+
		"hcpt.hora_modifica as hora_modifica, "+
		"hcpt.valoracion as valoracion, "+ 
		"hcpt.evolucion as evolucion, "+
		"hcpt.cita as cita, "+
		"hcpt.imagen as imagen, "+
		"hcpt.codigo_medico as codigo_medico, "+
		"hcpt.plan_tratamiento as plan_tratamiento "+ 
		"FROM odontologia.his_conf_plan_tratamiento hcpt  "+
		"WHERE ";
	
	/**
	* Cadena que consulta los hallazgos de un plan de tratamiento
	*/
	private static final String consultarHallazgosOdoHisConfStr = "SELECT "+  
		"d.det_plan_tratamiento as codigodetallepk, "+
		"d.hallazgo as codigohallazgo, "+
		"h.nombre as nombrehallazgo, "+ 
		"coalesce(d.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as piezadental, "+  
		"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, "+  
		"coalesce(s.nombre, '') as nombresuperficie, "+ 
		"coalesce(ssc.sector, "+ConstantesBD.codigoNuncaValido+") as sector, "+ 
		"coalesce(c.archivo_convencion,'') AS pathconvencion, "+
		"to_char(d.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fechamodifica, "+
		"d.hora_modifica AS horamodifica, "+ 
		"coalesce(d.clasificacion,'') AS clasificacion, "+
		"d.por_confirmar "+  
		"FROM odontologia.his_conf_det_plan_t d "+  
		"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) "+  
		"LEFT OUTER JOIN " +
			"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) "+  
		"LEFT OUTER JOIN " +
			"odontologia.convenciones_odontologicas c ON(c.consecutivo = d.convencion) "+   
		"LEFT OUTER JOIN " +
			"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
		"WHERE  d.plan_tratamiento = ? ";
	
	//****************************************************************************************************************************
	//METODOS PLAN TRATAMIENTO ODONTOGRAMA****************************************************************************************
	
	/**
	 * Consulta el plan de tratamiento, recibe como parametro un DtoPlanTratamiento, evalua los campos
	 * llenos del dto y con estos realiza los filtros. El Dto debe tener el campo institucion lleno
	 * 
	 * @param Connection con
	 * @param DtoPlanTratamientoOdo parametros
	 * */
	public static DtoPlanTratamientoOdo consultarPlanTratamiento(DtoPlanTratamientoOdo parametros)
	{
		Connection con = UtilidadBD.abrirConexion();
		
		
		DtoPlanTratamientoOdo dtoTratamiento = new DtoPlanTratamientoOdo();
		String consulta = consultarPlandeTratamientoPrincipalStr;
		

		consulta +=(parametros.getCodigoPk().doubleValue()>0)?" AND plt.codigo_pk ="+parametros.getCodigoPk(): "";
		consulta +=(parametros.getIngreso()>0)?" AND  plt.ingreso="+parametros.getIngreso():"";
		consulta +=UtilidadTexto.isEmpty(parametros.getPorConfirmar())?"": "AND plt.por_confirmar='"+parametros.getPorConfirmar()+"'";
		consulta+=UtilidadTexto.isEmpty(parametros.getEstado())?" ": "plt.estado='"+parametros.getEstado()+"'";
		
 		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setInt(1,parametros.getInstitucion());
			Log4JManager.info(ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dtoTratamiento.setCodigoPk(rs.getBigDecimal("codigopk"));
				dtoTratamiento.setConsecutivo(rs.getBigDecimal("consecutivo"));
				dtoTratamiento.setCodigoPaciente(rs.getInt("codpaciente"));
				dtoTratamiento.setIngreso(rs.getInt("ingreso"));
				dtoTratamiento.setFechaGrabacion(rs.getString("fechagrabacion"));
				dtoTratamiento.setHoraGrabacion(rs.getString("horagrabacion"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
				dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
				dtoTratamiento.setEstado(rs.getString("estado"));
				dtoTratamiento.setPorConfirmar(rs.getString("porconfirmar"));
				dtoTratamiento.setInstitucion(rs.getInt("institucion"));
				dtoTratamiento.setCentroAtencion(rs.getInt("centroatencion"));
				dtoTratamiento.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				dtoTratamiento.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				dtoTratamiento.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
				dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
				dtoTratamiento.setCodigoEvolucion(rs.getBigDecimal("evolucion"));
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			//Log4JManager.info(UtilidadLog.obtenerStringHerencia(dtoTratamiento, true));	
			Log4JManager.info("\nConsultar Sql Plan de tratamiento------------>"+ps);	
			Log4JManager.info("\n\n\n\n\n\n\n\n\n\n");
			Log4JManager.info("**************************************************************************************************************");
			
		}catch (Exception e) {
			e.printStackTrace();
			Log4JManager.info("Error--Consulta: "+consulta);
		}
		
		return dtoTratamiento;
	}
	
	
	
	
	/**
	 * Consulta el plan de tratamiento, recibe como parametro un DtoPlanTratamiento, evalua los campos
	 * llenos del dto y con estos realiza los filtros. El Dto debe tener el campo institucion lleno
	 * 
	 * @param Connection con
	 * @param DtoPlanTratamientoOdo parametros
	 * */
	public static DtoPlanTratamientoOdo consultarPlanTratamientoHistConf(DtoPlanTratamientoOdo parametros)
	{
		Connection con = UtilidadBD.abrirConexion();
		
		Log4JManager.info("**************************************************************************************************************");
		Log4JManager.info("*****************	-------CONSULTANDO PLAN DE TRATAMIENTO ---**************************************************");
		
		DtoPlanTratamientoOdo dtoTratamiento = new DtoPlanTratamientoOdo();
		String consulta = consultarPlandeTratamientoPrincipalHistConfStr;
		

		consulta +=(parametros.getCodigoPk().doubleValue()>0)?" AND plt.codigo_pk ="+parametros.getCodigoPk(): "";
		consulta +=(parametros.getIngreso()>0)?" AND  plt.ingreso="+parametros.getIngreso():"";
		consulta +=UtilidadTexto.isEmpty(parametros.getPorConfirmar())?"": "AND plt.por_confirmar='"+parametros.getPorConfirmar()+"'";
		
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setInt(1,parametros.getInstitucion());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dtoTratamiento.setCodigoPk(rs.getBigDecimal("codigopk"));
				dtoTratamiento.setConsecutivo(rs.getBigDecimal("consecutivo"));
				dtoTratamiento.setCodigoPaciente(rs.getInt("codpaciente"));
				dtoTratamiento.setIngreso(rs.getInt("ingreso"));
				dtoTratamiento.setFechaGrabacion(rs.getString("fechagrabacion"));
				dtoTratamiento.setHoraGrabacion(rs.getString("horagrabacion"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
				dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
				dtoTratamiento.setEstado(rs.getString("estado"));
				dtoTratamiento.setPorConfirmar(rs.getString("porconfirmar"));
				dtoTratamiento.setInstitucion(rs.getInt("institucion"));
				dtoTratamiento.setCentroAtencion(rs.getInt("centroatencion"));
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}catch (Exception e) {
			e.printStackTrace();
			Log4JManager.info("Error--Consulta: "+consulta);
		}
		
		return dtoTratamiento;
	}
	
	
	
	
	/**
	 * RETORNA UNA LISTA DE PLANES DE TRATAMIENTO
	 * RECIBE UN DTO Y UNA LISTA DE METODOS
	 * @param Connection con
	 * @param DtoPlanTratamientoOdo parametros, ArrayList estadosPlan
	 * */
	public static ArrayList<DtoPlanTratamientoOdo>	consultarPlanTratamiento( Connection con, DtoPlanTratamientoOdo parametros, ArrayList<String> estadosPlan)
	{
		
		ArrayList<DtoPlanTratamientoOdo> listaPlantTramiento = new ArrayList<DtoPlanTratamientoOdo>();
		
		
		
		Log4JManager.info("**************************************************************************************************************");
		Log4JManager.info("*****************	-------CONSULTANDO PLAN DE TRATAMIENTO ---**************************************************");
		
		
		String consulta = consultarPlandeTratamientoPrincipalStr;
		

		consulta +=(parametros.getCodigoPk().doubleValue()>0)?" AND plt.codigo_pk ="+parametros.getCodigoPk(): "";
		consulta +=(parametros.getIngreso()>0)?" AND  plt.ingreso="+parametros.getIngreso():"";
		consulta +=UtilidadTexto.isEmpty(parametros.getPorConfirmar())?"": "AND plt.por_confirmar='"+parametros.getPorConfirmar()+"'";
		if (estadosPlan.size()>0 )
			consulta += "and plt.estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosPlan)+") ";
		
		consulta+= (!UtilidadTexto.isEmpty(parametros.getUsuarioModifica().getFechaModifica()))? " and plt.fecha_modifica||'' <= '"+parametros.getUsuarioModifica().getFechaModificaFromatoBD()+"' ":" ";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setInt(1,parametros.getInstitucion());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoPlanTratamientoOdo dtoTratamiento = new DtoPlanTratamientoOdo();
				dtoTratamiento.setCodigoPk(rs.getBigDecimal("codigopk"));
				dtoTratamiento.setConsecutivo(rs.getBigDecimal("consecutivo"));
				dtoTratamiento.setCodigoPaciente(rs.getInt("codpaciente"));
				dtoTratamiento.setIngreso(rs.getInt("ingreso"));
				dtoTratamiento.setFechaGrabacion(rs.getString("fechagrabacion"));
				dtoTratamiento.setHoraGrabacion(rs.getString("horagrabacion"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
				dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
				dtoTratamiento.setEstado(rs.getString("estado"));
				dtoTratamiento.setPorConfirmar(rs.getString("porconfirmar"));
				dtoTratamiento.setInstitucion(rs.getInt("institucion"));
				dtoTratamiento.setCentroAtencion(rs.getInt("centroatencion"));
				dtoTratamiento.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				dtoTratamiento.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				dtoTratamiento.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				listaPlantTramiento.add(dtoTratamiento);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
			Log4JManager.info("\nConsultar Sql Plan de tratamiento------------>"+ps);	
			
		}catch (Exception e) {
			e.printStackTrace();
			Log4JManager.info("Error--Consulta: "+consulta);
		}
		
		return listaPlantTramiento;
	}
	
	
	
	
	/**
	 * Obtiene los hallazgos 
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(DtoDetallePlanTratamiento parametros)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		
		String consultaStr= "SELECT " +
									"d.codigo_pk as codigodetallepk, " +
									"d.hallazgo as codigohallazgo, " +
									"h.nombre as nombrehallazgo, " +
									"coalesce(d.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as piezadental, " +
									"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
									"coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie, " +
									"coalesce(ssc.sector, "+ConstantesBD.codigoNuncaValido+") as sectordiente, " +
									"coalesce(c.archivo_convencion,'') AS pathconvencion," +
									"to_char(d.fecha_modifica,'dd/mm/yyyy') AS fechamodifica," +
									"d.hora_modifica AS horamodifica," +
									"coalesce(d.clasificacion,'') AS clasificacion," +
									"por_confirmar as porconfirmar, " +
									"d.convencion AS convencion, " +
									"d.activo AS activo " +
							"FROM " +
								"odontologia.det_plan_tratamiento d " +
							"INNER JOIN " +
								"odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
							"LEFT OUTER JOIN " +
								"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							"LEFT OUTER JOIN " +
								"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"LEFT OUTER JOIN " +
								"odontologia.convenciones_odontologicas c ON(c.consecutivo = d.convencion) "  +
							"WHERE " +
								"d.plan_tratamiento = ?";

		if(parametros.getPiezaDental() > 0)
		{
			consultaStr += " AND d.pieza_dental = "+parametros.getPiezaDental();
		}
		
		if(!parametros.getSeccion().equals(""))
		{
			consultaStr += " AND d.seccion = '"+parametros.getSeccion()+"' ";
		}
		
		if(UtilidadTexto.getBoolean(parametros.getActivo()))
		{
			consultaStr += " AND d.activo = '"+parametros.getActivo()+"' ";
		}
		
		
		
		
		/*XPLANNER 148136, SEGUN DOCUMENTO DEBE ORDENAR DESENDENTEMENTE, esto fue organizado con aprobaciï¿½n de Germï¿½n y Felipe*/
		consultaStr+= " ORDER BY d.superficie desc ";
		/*
		Log4JManager.info("\n\n\n\n\n\n\n\n");
		Log4JManager.info(consultaStr);
		Log4JManager.info("\n\n\n\n\n\n\n\n");
		*/
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setDouble(1,parametros.getPlanTratamiento());
			
			Log4JManager.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.getClasificacion().setValue(rs.getString("clasificacion"));
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.getHallazgoREQUERIDO().setActivo(true);
				hallazgoSuperficie.setPorConfirmar(rs.getString("porconfirmar"));
				hallazgoSuperficie.getInfoRegistroHallazgo().setFechaModifica(rs.getString("fechamodifica"));
				hallazgoSuperficie.getInfoRegistroHallazgo().setHoraModifica(rs.getString("horamodifica"));
				
				if(!rs.getString("pathconvencion").equals(""))
				{
					hallazgoSuperficie.getHallazgoREQUERIDO().setDescripcion(CarpetasArchivos.CONVENCION.getRutaAbsoluta(parametros.getPath())+""+rs.getString("pathconvencion"));
				}
				hallazgoSuperficie.setCodigoConvencion(rs.getString("convencion"));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.getSuperficieOPCIONAL().setCodigo2(rs.getInt("sectordiente"));
				hallazgoSuperficie.getSuperficieOPCIONAL().setActivo(true);
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
				hallazgoSuperficie.getExisteBD().setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ", e);
		}
			
		return array;
	}
	
	
	/**
	 * Obtiene los hallazgos para las secci&oacute;n de Otros y Boca
	 * @return
	 */
	public static ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBoca(DtoDetallePlanTratamiento parametros)
	{
		ArrayList<InfoDetallePlanTramiento> array=new ArrayList<InfoDetallePlanTramiento>();
		
		String consultaStr= "SELECT " +
							"d.codigo_pk as codigodetallepk, " +
							"d.hallazgo as codigohallazgo, " +
							"h.nombre as nombrehallazgo, " +
							"coalesce(d.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as piezadental, " +
							"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
							"coalesce(s.nombre, '') as nombresuperficie, " +
							"coalesce(ssc.sector, "+ConstantesBD.codigoNuncaValido+") as sectordiente, " +
							"coalesce(c.archivo_convencion,'') AS pathconvencion," +
							"to_char(d.fecha_modifica,'dd/mm/yyyy') AS fechamodifica," +
							"d.hora_modifica AS horamodifica," +
							"coalesce(d.clasificacion,'') AS clasificacion," +
							"por_confirmar as porconfirmar " +
							"FROM odontologia.det_plan_tratamiento d " +
							"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
							"LEFT OUTER JOIN " +
								"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							"LEFT OUTER JOIN " +
								"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"LEFT OUTER JOIN " +
								"odontologia.convenciones_odontologicas c ON(c.consecutivo = d.convencion) "  +
							"WHERE " +
								"d.plan_tratamiento = ? ";
		
		if(parametros.getPiezaDental() > 0)
			consultaStr += " AND d.pieza_dental = "+parametros.getPiezaDental();
		
		if(!parametros.getSeccion().equals(""))
			consultaStr += " AND d.seccion = '"+parametros.getSeccion()+"' ";
		
		if(!parametros.getActivo().equals(""))
			consultaStr += " AND d.activo = '"+parametros.getActivo()+"' ";
		
		//
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setDouble(1,parametros.getPlanTratamiento());
			
			Log4JManager.info("obtenerHallazgosSuperficiesSeccionOtrayBoca >> "+ps);
			
			ResultSetDecorator rsTemp=new ResultSetDecorator(ps.executeQuery());
			HashMap mapaTemporal=UtilidadBD.cargarValueObject(rsTemp);
			//Log4JManager.info("--->"+mapaTemporal.get("sectordiente_"+i));
			
			Utilidades.imprimirMapa(mapaTemporal);
			
			//while(rs.next())
			for(int i=0;i<Utilidades.convertirAEntero(mapaTemporal.get("numRegistros")+"");i++)
			{
				Log4JManager.info("--->"+mapaTemporal.get("sectordiente_"+i));
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.getClasificacion().setValue(mapaTemporal.get("clasificacion_"+i)+"");
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(Utilidades.convertirAEntero(mapaTemporal.get("codigohallazgo_"+i)+""), mapaTemporal.get("nombrehallazgo_"+i)+""));
				hallazgoSuperficie.setHallazgoREQUERIDOOld(new InfoDatosInt(Utilidades.convertirAEntero(mapaTemporal.get("codigohallazgo_"+i)+""), mapaTemporal.get("nombrehallazgo_"+i)+""));
				hallazgoSuperficie.getHallazgoREQUERIDO().setActivo(true);
				hallazgoSuperficie.setPorConfirmar(mapaTemporal.get("porconfirmar_"+i)+"");
				hallazgoSuperficie.getInfoRegistroHallazgo().setFechaModifica(mapaTemporal.get("fechamodifica_"+i)+"");
				hallazgoSuperficie.getInfoRegistroHallazgo().setHoraModifica(mapaTemporal.get("horamodifica_"+i)+"");
				
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(Utilidades.convertirAEntero(mapaTemporal.get("codigosuperficie_"+i)+""), mapaTemporal.get("nombresuperficie_"+i)+""));
				hallazgoSuperficie.getSuperficieOPCIONAL().setCodigo2(Utilidades.convertirAEntero(mapaTemporal.get("sectordiente_"+i)+""));
				hallazgoSuperficie.setSuperficieOPCIONALOld(new InfoDatosInt(Utilidades.convertirAEntero(mapaTemporal.get("codigosuperficie_"+i)+""), mapaTemporal.get("nombresuperficie_"+i)+""));
				hallazgoSuperficie.getSuperficieOPCIONALOld().setCodigo2(Utilidades.convertirAEntero(mapaTemporal.get("sectordiente_"+i)+""));
				hallazgoSuperficie.getSuperficieOPCIONAL().setActivo(true);
				if(Utilidades.convertirAEntero(mapaTemporal.get("sectordiente_"+i)+"")>0)
					hallazgoSuperficie.getHallazgoREQUERIDO().setCodigo2(ComponenteOdontograma.codigoTipoHallazgoSuper);
				else
					hallazgoSuperficie.getHallazgoREQUERIDO().setCodigo2(ComponenteOdontograma.codigoTipoHallazgoDiente);
				Log4JManager.info("---armando 1-->"+hallazgoSuperficie.getSuperficieOPCIONALOld().getCodigo2());
				Log4JManager.info("---armando 2-->"+hallazgoSuperficie.getSuperficieOPCIONAL().getCodigo2());
				Log4JManager.info("---por confirmar armando 2-->"+hallazgoSuperficie.getPorConfirmar());
				hallazgoSuperficie.setCodigoPkDetalle(BigDecimal.valueOf(Utilidades.convertirADouble(mapaTemporal.get("codigodetallepk_"+i)+"")));
				hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
				
				if(!(mapaTemporal.get("pathconvencion_"+i)+"").equals(""))
						hallazgoSuperficie.getHallazgoREQUERIDO().setDescripcion(CarpetasArchivos.CONVENCION.getRuta()+""+mapaTemporal.get("pathconvencion_"+i)+"");
				
				
				
				InfoDetallePlanTramiento pieza = new InfoDetallePlanTramiento();
				pieza.getPieza().setCodigo(Utilidades.convertirAEntero(mapaTemporal.get("piezadental_"+i)+""));
				pieza.getPiezaOld().setCodigo(Utilidades.convertirAEntero(mapaTemporal.get("piezadental_"+i)+""));
				pieza.getExisteBD().setActivo(true);
				pieza.getExisteBD().setValue(ConstantesBD.acronimoSi);
				pieza.setCodigoPkDetalle(new BigDecimal(Utilidades.convertirAEntero(mapaTemporal.get("codigodetallepk_"+i)+"")));
				
				
				
				pieza.getDetalleSuperficie().add(hallazgoSuperficie);
				array.add(pieza);
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rsTemp, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr,e);
		}
			
		return array;
	}
	
	
	
	/**
	 * Carga la informacion de un programa/servicio especifico
	 * @param DtoProgramasServiciosPlanT parametros
	 * */
	public static InfoProgramaServicioPlan obtenerInfoProgramaServicios(DtoProgramasServiciosPlanT parametros)
	{ 
		InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
		String codigoTarifario=parametros.getCodigoTarifario();
		
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
			
		try 
		{	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarProgramaGenerico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,parametros.getPrograma().getCodigo());
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
			parametrosServ.setDetPlanTratamiento(parametros.getDetPlanTratamiento());
			
			if(rs.next())
			{ 
				info.setCodigoPkProgramaServicio(rs.getBigDecimal(1));
				info.setCodigoAmostrar(rs.getString(2));
				info.setNombreProgramaServicio(rs.getString(3));
				info.getExisteBD().setValue(ConstantesBD.acronimoNo);
				info.getExisteBD().setActivo(true);
				info.setArchivoConvencionPrograma(rs.getString("archivo_convencion"));
				
				parametrosServ.getPrograma().setCodigo(rs.getBigDecimal(1).doubleValue()); 
				parametrosServ.setCodigoTarifario(codigoTarifario);
				
				
				Log4JManager.info("-------------------->"+parametros.isCargarServicios());
				
				//siempre cargar los servicios del programa. ya que si es por programa debe cargar los servicios relacionados.
				//if(parametros.isCargarServicios())
					info.setListaServicios(cargarServiciosDeProgramasGenerico(parametrosServ));

				Log4JManager.info("----num servicios-->"+info.getListaServicios().size());

			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);

		}
		catch (SQLException e) 
		{
			Log4JManager.error("error "+e.toString());
		}
		
		return info;
	}
	
	
	/**
	 *	METODO POR VERIFICAR ESTA COMO REGULAR--
	 * Consulta los programas y servicios del detalle del plan de tratamiento
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(DtoProgramasServiciosPlanT parametros) 
	{
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		String consultaStr = "", despuesWhere = "";
		
		if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
		{
			consultaStr = 
							"SELECT DISTINCT " +
								"pro.codigo as codigo ," +//1
								"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +//2
								"pro.codigo_programa as codigoamostrar, " +//3
								"plant.estado_programa as est_prog, " +//4
								"max(plant.fecha_modifica) as fecha_modifica," +//5
								"max(plant.hora_modifica)as hora_modifica," +//6
								"coalesce(co.archivo_convencion,'') as archivo_convencion, " +//7
								"coalesce(plant.inclusion,'') as inclusion, " +//8
								"coalesce(plant.exclusion,'') as exclusion, " +//9
								"coalesce(plant.motivo,"+ConstantesBD.codigoNuncaValido+") as motivo_can, " +//10
								"coalesce(mcc.nombre,'') as nom_motivo_can, " +//11
								"coalesce(mcc.codigo,'') as cod_motivo_can, " +//12
								"dpt.hallazgo AS hallazgo, " +
								"dpt.pieza_dental AS pieza_dental, " +
								"dpt.superficie AS superficie," +
								"dpt.plan_tratamiento AS plan_tratamiento, " +//13
								"pt.codigo_paciente as codigo_paciente, "+
								"pt.ingreso as ingreso " + 
							"FROM " +
								"odontologia.programas_servicios_plan_t plant " +
							"INNER JOIN " +
								"odontologia.det_plan_tratamiento dpt " +
									"ON(dpt.codigo_pk=plant.det_plan_tratamiento) "+
							"INNER JOIN " +
								"odontologia.programas pro ON (plant.programa = pro.codigo) " +
							"INNER JOIN " +
								"odontologia.plan_tratamiento pt ON (pt.codigo_pk = dpt.plan_tratamiento) " +
							"LEFT OUTER JOIN " +
								"odontologia.convenciones_odontologicas co ON (co.consecutivo = pro.convencion) " +
							"LEFT OUTER JOIN " +
								"odontologia.motivos_atencion mcc ON (mcc.codigo_pk = plant.motivo) " +
							"WHERE " +
								"plant.det_plan_tratamiento="+parametros.getDetPlanTratamiento()+" AND plant.activo = '"+ConstantesBD.acronimoSi+"' ";
			
			if(parametros.getEstadosProgramasOservicios().size() >0 )
				consultaStr += "and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(parametros.getEstadosProgramasOservicios())+") ";
			
			despuesWhere  =
							" GROUP BY " +
								"pro.codigo," +
								"pro.codigo_programa," +
								"pro.nombre," +
								"pro.codigo_programa," +
								"plant.por_confirmar," +
								"plant.estado_programa," +
								"co.archivo_convencion," +
								"plant.inclusion," +
								"plant.exclusion," +
								"plant.motivo," +
								"mcc.nombre," +
								"mcc.codigo," +
								"dpt.hallazgo," +
								"dpt.pieza_dental," +
								"dpt.superficie," +
								"dpt.plan_tratamiento, " +
								"pt.codigo_paciente, "+
								"pt.ingreso " +
							" ORDER BY fecha_modifica DESC,hora_modifica DESC ";
		}
		else
		{
			consultaStr = 	"SELECT DISTINCT " +
							"plant.codigo_pk as codigopk," +//1
							"plant.servicio as codigoServicio ," +//2
							"getnombreservicio(plant.servicio)  as nombreServicio, " +//3
							"getcodigoservicio(plant.servicio, "+parametros.getCodigoTarifario()+") as codigoamostrar, " +//4
							"plant.por_confirmar," +//5
							"plant.estado_servicio as est_serv," +//6
							"coalesce(plant.cita,"+ConstantesBD.codigoNuncaValido+") as cita, " +//7
							"coalesce(plant.valoracion,"+ConstantesBD.codigoNuncaValido+") as valoracion, " +//8
							"coalesce(plant.evolucion,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +//9
							"coalesce(co.archivo_convencion,'') as archivo_convencion, " +//10
							"coalesce(plant.inclusion,'') as inclusion, " +//11
							"coalesce(plant.exclusion,'') as exclusion, " +//12
							"coalesce(plant.garantia,'') as garantia, " +//13
							"coalesce(plant.motivo,"+ConstantesBD.codigoNuncaValido+") as motivo_can, " +//14
							"coalesce(mcc.nombre,'') as nom_motivo_can, " +//15
							"coalesce(mcc.codigo,'') as cod_motivo_can, " +//16
							"pt.codigo_paciente as codigo_paciente, "+
							"pt.ingreso as ingreso " + 
							"FROM " +
							"odontologia.programas_servicios_plan_t plant " +
							"INNER JOIN facturacion.servicios s ON (s.codigo = plant.servicio) " +
							"LEFT OUTER JOIN " +
							"odontologia.det_plan_tratamiento dpt ON(dpt.codigo_pk=plant.det_plan_tratamiento) " +
							"INNER JOIN odontologia.plan_tratamiento pt ON (pt.codigo_pk = dpt.plan_tratamiento) " +
							"LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON (co.consecutivo = s.convencion) " +
							"LEFT OUTER JOIN odontologia.motivos_atencion mcc ON (mcc.codigo_pk = plant.motivo) " +
							"WHERE " +
							"plant.programa IS NOT NULL AND det_plan_tratamiento="+parametros.getDetPlanTratamiento()+" AND plant.activo = '"+ConstantesBD.acronimoSi+"' ";
			
			if(parametros.getEstadosProgramasOservicios().size() >0 )
			{
				consultaStr += "AND plant.estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(parametros.getEstadosProgramasOservicios())+") ";
			}
			
			despuesWhere = " ORDER BY plant.orden_servicio ASC ";
		}
		
		if(!parametros.getActivo().equals(""))
		{
			consultaStr += " AND plant.activo = '"+parametros.getActivo()+"' ";
		}
		
		try 
		{ 
			consultaStr += despuesWhere;
			
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
			{
				DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
				parametrosServ.setDetPlanTratamiento(parametros.getDetPlanTratamiento());
				parametrosServ.setCodigoTarifario(parametros.getCodigoTarifario());
				parametrosServ.setCodigoCita(parametros.getCodigoCita());
				
				while(rs.next())
				{
					InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.getExisteBD().setValue(ConstantesBD.acronimoSi);
					info.getExisteBD().setActivo(true);
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setProgHallazgoPieza(consultarProgramaHallazgoPieza(con, parametros.getDetPlanTratamiento().intValue(), info.getCodigoPkProgramaServicio().intValue()));
					//info.setColorLetra(info.getProgHallazgoPieza().getColorLetra());
					
					int hallazgo=rs.getInt("hallazgo");
					
					/*
					 * Cargo el numero de superficies que fueron seleccionadas para este programa
					 */
					int numeroSuperficies=PlanTratamiento.consultarNumeroSuperficiesPorPrograma(hallazgo, info.getCodigoPkProgramaServicio().intValue(), rs.getInt("pieza_dental"), rs.getInt("superficie"), rs.getInt("plan_tratamiento"));
					
					info.setNumeroSuperficies(numeroSuperficies);
					
					if(rs.getString("exclusion").equals(ConstantesBD.acronimoSi))// se valida si esta por autorizar exclusion
					{
						if(rs.getString("est_prog").equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						{
							info.setEstadoPrograma(rs.getString("est_prog")+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido);
							info.setNewEstadoProg(rs.getString("est_prog")+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido);
						}else{
							info.setEstadoPrograma(rs.getString("est_prog"));
							info.setNewEstadoProg(rs.getString("est_prog"));
						}
					}else if(rs.getString("inclusion").equals(ConstantesBD.acronimoSi)) // se valida si esta por autorizar inclusion
					{
						if(rs.getString("est_prog").equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						{
							info.setEstadoPrograma(rs.getString("est_prog")+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
							info.setNewEstadoProg(rs.getString("est_prog")+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
						}else{
							info.setEstadoPrograma(rs.getString("est_prog"));
							info.setNewEstadoProg(rs.getString("est_prog"));
						}
					}else{
						
						info.setEstadoPrograma(rs.getString("est_prog"));
						info.setNewEstadoProg(rs.getString("est_prog"));
					}
					
					info.setArchivoConvencionPrograma(rs.getString("archivo_convencion"));
					info.setMotivoCancelacion(new InfoDatosStr(rs.getInt("motivo_can")+"",rs.getString("nom_motivo_can"), rs.getString("cod_motivo_can")));
					parametrosServ.getPrograma().setCodigo(rs.getBigDecimal("codigo").doubleValue());
					parametrosServ.setProgramaHallazgoPieza(info.getProgHallazgoPieza());
					
					parametrosServ.setCodigoPaciente(rs.getInt("codigo_paciente"));
					parametrosServ.setIngresoPaciente(rs.getInt("ingreso"));
					
					info.setListaServicios(cargarServiciosDeProgramasPlanT(parametrosServ));
					//El estado por confirmar del programa depende si todos sus servicios estï¿½n por confirmar o confirmados
					info.setPorConfirmar(info.misServiciosSonTodosConfirmados(true)?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);
					//El estado por garantï¿½a del programa depende si todos sus servicios estï¿½n con garantï¿½a o no
					info.setGarantia(info.misServiciosSonTodosConfirmados(false)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					
					array.add(info);
				}
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			else
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				
				while(rs.next())
				{
					InfoServicios serv = new InfoServicios();
					serv.setCodigoPkProgServ(rs.getBigDecimal("codigopk"));
					serv.setCodigoMostrar(rs.getString("codigoamostrar"));
					serv.getServicio().setCodigo(rs.getInt("codigoServicio"));
					serv.getServicio().setNombre(rs.getString("nombreServicio"));
					serv.getExisteBD().setValue(ConstantesBD.acronimoSi);
					serv.setPorConfirmar(rs.getString("por_confirmar"));
					serv.setEstadoServicio(rs.getString("est_serv"));
					serv.setNewEstado(rs.getString("est_serv"));
					serv.setCodigoCita(new BigDecimal(rs.getInt("cita")));
					serv.setCodigoValoracion(new BigDecimal(rs.getInt("valoracion")));
					serv.setCodigoEvolucion(new BigDecimal(rs.getInt("evolucion")));
					serv.setInclusion(rs.getString("inclusion"));
					serv.setExclusion(rs.getString("exclusion"));
					serv.setGarantia(rs.getString("garantia"));
					info.setMotivoCancelacion(new InfoDatosStr(rs.getInt("motivo_can")+"",rs.getString("nom_motivo_can"), rs.getString("cod_motivo_can")));
					serv.getExisteBD().setActivo(true);
					serv.setArchivoConvencionPrograma(CarpetasArchivos.CONVENCION.getRuta()+""+rs.getString("archivo_convencion"));
					info.getListaServicios().add(serv);
				}
				
				array.add(info);
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}	
		/*
		Log4JManager.info("--------------REVISION ESTADOS--------------------------------");
		for(InfoProgramaServicioPlan progsev:array)
		{
			Log4JManager.info("Estado programa: "+progsev.getEstadoPrograma());
			for(InfoServicios serv:progsev.getListaServicios())
			{
				Log4JManager.info("estado servicio: "+serv.getEstadoServicio());
			}
		}
		Log4JManager.info("--------------FIN REVISION ESTADOS--------------------------------");
		*/
		return array;
	}

	/**
 	 * Consulta el color de la letra en la relaci&oacute;n de N superficies por programa. 
	 * @param con Conexi&oacute;n con la BD.
	 * @param codigoDetallePlanTratamiento C&oacute;digo del plan de tratamiento.
	 * @param codigoPrograma C&oacute;digo del programa que se desea consultar.
	 * @return {@link String} Color de la letra en formato #RRGGBB
	 * @author Juan David Ram&iacute;rez
	 */
	private static DtoProgHallazgoPieza consultarProgramaHallazgoPieza(Connection con, int codigoDetallePlanTratamiento, int codigoPrograma)
	{
		DtoDetallePlanTratamiento dtoDetallePlanTratamiento=new DtoDetallePlanTratamiento();
		dtoDetallePlanTratamiento.setCodigo(codigoDetallePlanTratamiento);
		PlanTratamiento.consultarDetPlanTratamiento(dtoDetallePlanTratamiento, con, false);

		DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
		
		String sentencia=
				"SELECT " +
					"php.codigo_pk AS codigo_pk, "+ 
					"php.plan_tratamiento AS plan_tratamiento, "+
					"php.programa AS programa, "+
					"php.hallazgo AS hallazgo, "+
					"php.pieza_dental AS pieza_dental, "+
					"php.seccion AS seccion, "+
					"php.usuario_modifica AS usuario_modifica, "+
					"php.fecha_modifica AS fecha_modifica, "+
					"php.hora_modifica AS hora_modifica, "+
					"php.color_letra AS color_letra " +
					"" +
				"FROM " +
					"odontologia.programas_hallazgo_pieza php " +
				"INNER JOIN " +
					"odontologia.superficies_x_programa sxp " +
						"ON(sxp.prog_hallazgo_pieza=php.codigo_pk) " +
				"WHERE " +
						"php.plan_tratamiento=? " +
					"AND " +
						"sxp.det_plan_trata=? " +
					"AND " +
						"php.programa=? " +
					"AND " +
						"php.seccion=? " +
					"AND ";
		if(dtoDetallePlanTratamiento.getPiezaDental()>0)
		{
			sentencia+="php.pieza_dental="+dtoDetallePlanTratamiento.getPiezaDental() + " ";
		}
		else
		{
			sentencia+="php.pieza_dental IS NULL ";
		}
		sentencia+=
					"AND ";
		if(dtoDetallePlanTratamiento.getSuperficie()>0)
		{
			sentencia+="sxp.superficie_dental="+dtoDetallePlanTratamiento.getSuperficie()+" ";
		}
		else
		{
			if(dtoDetallePlanTratamiento.getPiezaDental()>0)
			{
				sentencia+="sxp.superficie_dental="+ConstantesBD.codigoSuperficieDiente+" ";
			}
			else
			{
				sentencia+="sxp.superficie_dental="+ConstantesBD.codigoSuperficieBoca+" ";
			}
		}
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setDouble(1, dtoDetallePlanTratamiento.getPlanTratamiento());
			psd.setInt(2, codigoDetallePlanTratamiento);
			psd.setInt(3, codigoPrograma);
			psd.setString(4, dtoDetallePlanTratamiento.getSeccion());
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				dtoProgHallazgoPieza.setCodigoPk(rsd.getInt("codigo_pk"));
				dtoProgHallazgoPieza.setPlanTratamiento(rsd.getInt("plan_tratamiento"));
				dtoProgHallazgoPieza.setPrograma(rsd.getInt("programa"));
				dtoProgHallazgoPieza.setHallazgo(rsd.getInt("hallazgo"));
				dtoProgHallazgoPieza.setPiezaDental(rsd.getInt("pieza_dental"));
				dtoProgHallazgoPieza.setSeccion(rsd.getString("seccion"));
				dtoProgHallazgoPieza.setColorLetra(rsd.getString("color_letra"));
			}
			rsd.close();
			psd.close();
			return dtoProgHallazgoPieza;
		}catch (SQLException e) {
			Log4JManager.error("Error consultando el color de la letra del progrmama seleccionado",e);
		}
		return null;
	}




	/**
	 * Carga los servicios de un programa Plant Tratamiento
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasGenerico(DtoProgramasServiciosPlanT parametros)
	{
		
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
		String codigoTarifario=parametros.getCodigoTarifario();
		Log4JManager.info("\n\n CODIGO TARIFARIO cargarServiciosDeProgramasGenerico >>>>>>>>>>>> "+codigoTarifario);
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		String order = "";
		String consultaStr= "SELECT DISTINCT " +
							"dp.servicio as codigoServicio ," +
							"getnombreservicio(dp.servicio, "+codigoTarifario+")  as nombreServicio, " +
							"getcodigoservicio(dp.servicio, "+codigoTarifario+") as  codigoamostrar ," +
							"dp.orden " +
							"FROM  " +
							"odontologia.detalle_programas dp " +
							"WHERE dp.activo = '"+ConstantesBD.acronimoSi+"' ";
		
		if(parametros.getPrograma().getCodigo() > 0)
			consultaStr +=  " AND dp.programas = "+parametros.getPrograma().getCodigo()+" ";
	
		order = " ORDER BY dp.orden ASC ";
		consultaStr += order;  
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.getExisteBD().setActivo(true);
				info.setOrderServicio(rs.getInt("orden"));
				listaServicios.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
		
		return listaServicios;
	}
	
	/**
	 *
	 * Carga los servicios de un programa Plant Tratamiento
	 * 
	 * @param parametros
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(DtoProgramasServiciosPlanT parametros)
	{
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>(); 
		String codigoTarifario=parametros.getCodigoTarifario();
		Log4JManager.info("\n\n CODIGO TARIFARIO >>>>>>>>>>>> "+codigoTarifario);
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		String order = "";
		String consultaStr= "SELECT DISTINCT " +
							"plant.codigo_pk as codigopk," +
							"plant.servicio as codigoServicio ," +
							"getnombreservicio(plant.servicio, "+codigoTarifario+")  as nombreServicio, " +
							"getcodigoservicio(plant.servicio, "+codigoTarifario+") as  codigoamostrar ," +
							"plant.estado_servicio as estadoServicio, " +
							"plant.orden_servicio as orden_serv, " +
							"coalesce(plant.cita,"+ConstantesBD.codigoNuncaValido+") as cita, " +
							"coalesce(plant.valoracion,"+ConstantesBD.codigoNuncaValido+") as valoracion, " +
							"coalesce(plant.evolucion,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +
							"plant.por_confirmar AS por_confirmar, "+
							"coalesce(plant.inclusion,'') as inclusion, " +
							"coalesce(plant.exclusion,'') as exclusion, " +
							"coalesce(plant.garantia,'') as garantia, " +
							"pt.codigo_pk AS codigo_plan_tratamiento, " +
							"plant.codigo_pk AS codigo_prog_serv_plant," +
							"coalesce(mcc.nombre,'') as nom_motivo_can, " +
							"coalesce(mcc.codigo,'') as cod_motivo_can, " +
							"coalesce(plant.motivo,"+ConstantesBD.codigoNuncaValido+") as motivo_can, " +
							"CASE WHEN plant.estado_servicio='"+ConstantesIntegridadDominio.acronimoRealizadoInterno+"' THEN " +
									"plant.usuario_modifica " +
								"ELSE '' " +
									"END " +
								"AS usuario_confirmacion " +
							"FROM  " +
							"odontologia.programas_servicios_plan_t  plant " +
							"INNER JOIN odontologia.det_plan_tratamiento dpt " +
							"ON (dpt.codigo_pk=plant.det_plan_tratamiento) " +
							"INNER JOIN odontologia.plan_tratamiento pt " +
							"ON (pt.codigo_pk=dpt.plan_tratamiento) " +
							
							" LEFT OUTER JOIN " +
							" odontologia.motivos_atencion mcc ON (mcc.codigo_pk = plant.motivo) " +
							
							"WHERE plant.servicio IS NOT NULL AND plant.activo = '"+ConstantesBD.acronimoSi+"' ";
		
		if(parametros.getDetPlanTratamiento().intValue() > 0)
			consultaStr += " AND plant.det_plan_tratamiento = "+parametros.getDetPlanTratamiento().intValue()+" ";
		
		if(parametros.getPrograma().getCodigo() > 0)
			consultaStr +=  " AND plant.programa = "+parametros.getPrograma().getCodigo()+" ";
	
		if(parametros.getCodigoPlanTratamiento() > ConstantesBD.codigoNuncaValido){
			
			consultaStr +=  " AND pt.codigo_pk = "+parametros.getCodigoPlanTratamiento()+" ";
		}

		if(parametros.getServicio()!=null &&  parametros.getServicio().getCodigo() > 0){
			
			consultaStr +=  " AND pt.codigo_pk = "+parametros.getCodigoPlanTratamiento()+" ";
		}

		if(!UtilidadTexto.isEmpty(parametros.getEstadoServicio())){
			
			consultaStr +=  " AND plant.estado_servicio = '"+parametros.getEstadoServicio()+"' ";
		}
		
		if(!UtilidadTexto.isEmpty(parametros.getPorConfirmado())){
			
			consultaStr +=  " AND plant.por_confirmar = '"+parametros.getPorConfirmado()+"' ";
		}
		
		order = " ORDER BY plant.orden_servicio ";
		consultaStr += order;  
		
		Connection con = UtilidadBD.abrirConexion();
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			Log4JManager.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				info.setCodigoPkProgServ(rs.getBigDecimal("codigopk"));
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				/*
				 * Cargar minutos duracion del servicio
				 */
				info.setDuracionCita(cargarMinutosDuracionServicio(con ,info.getServicio().getCodigo()));
				
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.setPorConfirmar(rs.getString("por_confirmar"));
				info.setEstadoServicio(rs.getString("estadoServicio"));
				info.setNewEstado(rs.getString("estadoServicio"));
				info.getExisteBD().setValue(ConstantesBD.acronimoSi);
				info.getExisteBD().setActivo(true);
				info.setOrderServicio(rs.getInt("orden_serv"));
				info.setCodigoCita(new BigDecimal(rs.getInt("cita")));
				info.setCodigoValoracion(new BigDecimal(rs.getInt("valoracion")));
				info.setCodigoEvolucion(new BigDecimal(rs.getInt("evolucion")));
				info.setInclusion(rs.getString("inclusion"));
				info.setExclusion(rs.getString("exclusion"));
				info.setGarantia(rs.getString("garantia"));
				info.setMotivoCancelacion(new InfoDatosStr(rs.getInt("motivo_can")+"",rs.getString("nom_motivo_can"), rs.getString("cod_motivo_can")));
				info.setUsuarioConfirmacion(rs.getString("usuario_confirmacion"));
				
				ArrayList<DtoServicioOdontologico> serviciosOdontologicos = ObtenerServicioAsociadoAOtraCita(con, parametros.getCodigoCita().intValue(), 
						 parametros.getProgramaHallazgoPieza().getCodigoPk(), info.getServicio().getCodigo(), parametros.getCodigoPaciente());
				/*
				 * Esto quiere decir que si existe un registro de cita en cualquier estado
				 * asociado a ese servicio, Por lo cual debe ser marcado y no permitir su selección en el proceso
				 * de próxima cita
				 */
				if(serviciosOdontologicos!=null && serviciosOdontologicos.size()>0){
					
					info.setAsociadoAOtraCita(true);
					
					for (DtoServicioOdontologico servicio : serviciosOdontologicos) {

						info.setFechaCitaAsociada(servicio.getFechaCita());
						break;
					}
					
					Log4JManager.info("Si esta asociado");
				}
				
				listaServicios.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga",e);
		}
		SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(null, null, con);
		
		return listaServicios;
	}

	
//	/**
//	 * 
//	 * Método que determina si el servicio se encuentra asociado a una cita en estado programada
//	 * @param parametros
//	 * @param info 
//	 */
//	private static void obtenerServicioAsociadoAProgramada(	DtoProgramasServiciosPlanT parametros, InfoServicios info) {
//		
//
//		ArrayList<DtoServicioOdontologico> l = SqlBaseUtilidadOdontologia.obtenerServiciosXEstadoCitaXTipoCita(parametros.getCodigoPaciente(), parametros.getIngresoPaciente(), 
//				ConstantesIntegridadDominio.acronimoProgramado, ConstantesIntegridadDominio.acronimoTratamiento, ConstantesBD.codigoNuncaValido);
//		
//		
//	}




	/**
	 * 
	 * @param codigoServicio
	 * @return
	 */
	public static int  cargarMinutosDuracionServicio(Connection con , int codigoServicio){
		
		
		
		String consulta="select  ser2.minutos_duracion as minutosDuracion from servicios  ser2 where  ser2.codigo=?";
		
		int numeroDuracionTMP=ConstantesBD.codigoNuncaValido;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoServicio);
			
			Log4JManager.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				 numeroDuracionTMP=rs.getInt("minutosDuracion");
			}
		}
		catch (SQLException e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		
		return numeroDuracionTMP;
		
	}
	
	
	
	/**
	 * Método que verifica si el servicio se encuentra asociado a otra cita
	 * @param con
	 * @param codigoCita
	 * @param codigoProServPlanTrat
	 * @return true en caso de estar asociado, false de lo contrario
	 */
	public static boolean estaServicioAsociadoAOtraCita(Connection con, int codigoCita, int codigoProgHallPieza, int servicio, int codigoPaciente)
	{
		String sentenciaSQL="";
		ResultSetDecorator rsd = null;
		PreparedStatementDecorator psd = null;
		
		try{

			if(codigoProgHallPieza>0){
				
				sentenciaSQL = "SELECT " +
					"count(1) AS numResultados " +
				"FROM odontologia.programas_hallazgo_pieza php " +
				"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
				"INNER JOIN odontologia.servicios_cita_odontologica sco ON(sco.programa_hallazgo_pieza=php.codigo_pk) " +
				"INNER JOIN odontologia.citas_odontologicas co ON(co.codigo_pk=sco.cita_odontologica) " +
	  		    " INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
				" INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk  and sco.servicio=pspt.servicio AND pspt.activo = 'S' ) " +
				"WHERE " +
						"(co.estado='"+ConstantesIntegridadDominio.acronimoAsignado+"' OR co.estado='"+ConstantesIntegridadDominio.acronimoReservado+"' "+" OR co.estado='"+ConstantesIntegridadDominio.acronimoAtendida+"' )" +
					" AND " +
						"co.codigo_pk!= ? " +
					" AND " +
						"php.codigo_pk=? " +
					" AND " +
						" sco.servicio=? " +
					" AND " +
					"	co.codigo_pk IN (SELECT cita_asociada FROM citas_asociadas_a_programada WHERE cita_programada = ?) "+
					" AND " +
					"	sco.activo = '"+ConstantesBD.acronimoSi +"'";

			}else{
				
				sentenciaSQL = "SELECT " +
				"count(1) AS numResultados " +
				"FROM odontologia.servicios_cita_odontologica sco, odontologia.citas_odontologicas co " +
				"WHERE  sco.cita_odontologica = co.codigo_pk AND " +
					" (co.estado='"+ConstantesIntegridadDominio.acronimoAsignado+"' OR co.estado='"+ConstantesIntegridadDominio.acronimoReservado+"' "+" OR co.estado='"+ConstantesIntegridadDominio.acronimoAtendida+"' ) " +
					" AND " +
						"co.codigo_pk!= ? " +
					" AND " +
						"co.codigo_paciente = ? "+
					" AND sco.servicio=? " +
					" AND " +
						"	co.codigo_pk IN (SELECT cita_asociada FROM citas_asociadas_a_programada  WHERE cita_programada = ?) "+
					" AND " +
					"	sco.activo = '"+ConstantesBD.acronimoSi +"'";
			}
		
			if(!UtilidadTexto.isEmpty(sentenciaSQL)){
				
				psd=new PreparedStatementDecorator(con, sentenciaSQL);
				psd.setInt(1, codigoCita);
				
				if(codigoProgHallPieza>0){
					
					psd.setInt(2, codigoProgHallPieza);
					
				}else {
					
					psd.setInt(2, codigoPaciente);
				}
				
				psd.setInt(3, servicio);
				psd.setInt(4, codigoCita);
				
				Log4JManager.info(psd);
				rsd=new ResultSetDecorator(psd.executeQuery());
			}
			
			boolean resultado=false;
			
			if(rsd!=null && rsd.next()){
				
				resultado=rsd.getInt("numResultados")!=0;
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			return resultado;
		}
		catch (Exception e) {
			Log4JManager.info("Error consultando el uso de los servicios",e);
			return false;
		}
	}


	/**
	 * obtiene el listado de programas o servicios
	 * @param DtoProgramasServiciosPlanT parametros
	 * */
	public static ArrayList<InfoProgramaServicioPlan> obtenerListadoProgramasServicio(DtoProgramasServiciosPlanT parametros)
	{
		ArrayList<InfoProgramaServicioPlan> array = new ArrayList<InfoProgramaServicioPlan>();
		String codigoTarifario=parametros.getCodigoTarifario();
               
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}	
		
		String consultarProgramaParamHallazoStr = 
			"SELECT " +
				"progra.codigo as codigoPkProgramaServicio," +
				"progra.codigo_programa ||' '|| progra.nombre AS nombre, " +
				"progra.codigo_programa AS codigo_mostrar, " +
				/*"dp.orden AS orden," +
				"detHalla.por_defecto AS por_defecto," +*/
				"detHalla.numero_superficies AS numero_superficies "+
			"FROM " +
				"odontologia.hallazgos_odontologicos hallazgo " +
			"INNER JOIN " +
				"odontologia.hallazgos_vs_prog_ser hallaPro " +
					"ON (hallazgo.consecutivo= hallaPro.hallazgo) "+
			"INNER JOIN " +
				"odontologia.det_hall_prog_ser detHalla " +
					"ON(detHalla.hallazgo_vs_prog_ser=hallaPro.codigo) "+
			"INNER JOIN " +
				"odontologia.programas progra " +
					"ON(progra.codigo=detHalla.programa AND progra.activo = '"+ConstantesBD.acronimoSi+"' ) " +
			"INNER JOIN " +
				"odontologia.detalle_programas dp " +
					"ON(dp.programas=progra.codigo) " +
			"WHERE " +
				"detHalla.programa IS NOT NULL ? " +
			"GROUP BY " +
				"progra.codigo,progra.codigo_programa,progra.nombre, "+
				//"dp.orden,detHalla.por_defecto,"+
				"detHalla.numero_superficies " /*++",dethalla.orden,hallazgo.consecutivo " +*/ +
			/*"ORDER BY " +
				"detHalla.por_defecto DESC, detHalla.orden ASC "*/
		" ORDER BY " +
		" detHalla.numero_superficies ASC";

		String consultaStr = "", reemplazo = "";
		
		if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
		{
			consultaStr = consultarProgramaParamHallazoStr;		
		}
		else
		{
			consultaStr = consultarServiciosParamHallazgoStr;
			consultaStr=consultaStr.replace(ConstantesBD.separadorSplitComplejo, codigoTarifario);
		}
		if(parametros.getCodigoHallazgo() > 0){
			
			reemplazo += " AND hallazgo.consecutivo= "+parametros.getCodigoHallazgo();
		
		}else {
			
			if(parametros.getCodigoTipoHallazgo() == ComponenteOdontograma.codigoTipoHallazgoDiente){ // Diente
				
				reemplazo += " AND detHalla.numero_superficies = 0 ";
				
			}else if(parametros.getCodigoTipoHallazgo() == ComponenteOdontograma.codigoTipoHallazgoSuper){ // Superficie
				
				reemplazo += " AND detHalla.numero_superficies > 0 ";
				
			}else if(parametros.getCodigoTipoHallazgo() == ComponenteOdontograma.codigoTipoHallazgoBoca){ // Boca
				
				reemplazo += " AND detHalla.numero_superficies IS NULL ";
			}
		}
		
		
		if(parametros.getPrograma().getCodigo() > 0)
			reemplazo += " AND progra.codigo = "+parametros.getPrograma().getCodigo();
		
		consultaStr = consultaStr.replace("?",reemplazo);
		Log4JManager.info("..:por borrar >> "+consultaStr);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			
			boolean tieneElementos=false;
			if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					tieneElementos=true;
					InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setNumeroSuperficies(rs.getInt("numero_superficies"));
					info.setCodigoAmostrar(rs.getString("codigo_mostrar"));
					
					if(parametros.isCargarServicios())
						info.setListaServicios(cargarServiciosParamPrograma(rs.getInt("codigoPkProgramaServicio"),codigoTarifario));
					
					array.add(info);
				}
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			else
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
				
				while(rs.next())
				{
					tieneElementos=true;
					InfoServicios serv = new InfoServicios();
					serv.setCodigoMostrar(rs.getString("codigoamostrar"));
					serv.getServicio().setCodigo(rs.getInt("codigoPkProgramaServicio"));
					serv.getServicio().setNombre(rs.getString("nombre"));
					serv.setNumeroSuperficies(rs.getInt("numero_superficies"));
					info.getListaServicios().add(0,serv);
				}
				
				array.add(info);
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			if(!tieneElementos && parametros.getCodigoHallazgo()>0)
			{
				parametros.setCodigoHallazgo(0);
				return obtenerListadoProgramasServicio(parametros);
			}
			
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" >> "+consultaStr);
		}
		
		return array;
	}
	
	
	
	//		OJO --------------------FALTA CERRA LA CONEXION 
	/**
	 * Obtiene el programa parametrizado por defecto para un hallazgo
	 * @param DtoProgramasServiciosPlanT
	 * @return
	 */
	public static InfoProgramaServicioPlan obtenerProgramaServicioParamHallazgo(DtoProgramasServiciosPlanT parametros)
	{
		InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
		String codigoTarifarioServ=parametros.getCodigoTarifario();
        
		if(UtilidadTexto.isEmpty(codigoTarifarioServ))
		{
			codigoTarifarioServ=ConstantesBD.codigoTarifarioCups+"";
		}
		
		String consultaStr = 
			"SELECT " +
			"progra.codigo as codigoPkProgramaServicio," +
			"progra.codigo_programa ||' '|| progra.nombre AS nombre, " +
			"dp.orden AS orden," +
			"detHalla.por_defecto AS por_defecto," +
			"detHalla.numero_superficies AS numero_superficies, " +
			"detHalla.permite_tratar_varias_veces AS permite_tratar_varias_veces "+
			"FROM odontologia.hallazgos_odontologicos hallazgo " +
			"INNER JOIN odontologia.hallazgos_vs_prog_ser  hallaPro ON (hallazgo.consecutivo= hallaPro.hallazgo) "+
			"INNER JOIN odontologia.det_hall_prog_ser detHalla ON(detHalla.hallazgo_vs_prog_ser=hallaPro.codigo) "+
			"INNER JOIN odontologia.programas progra ON(progra.codigo=detHalla.programa AND progra.activo = '"+ConstantesBD.acronimoSi+"' ) " +
			"INNER JOIN odontologia.detalle_programas dp ON(dp.programas=progra.codigo) " +
			"WHERE " +
			"detHalla.programa IS NOT NULL ? " +
			"group by progra.codigo,progra.codigo_programa,progra.nombre, dp.orden,detHalla.por_defecto,detHalla.numero_superficies,dethalla.orden,hallazgo.consecutivo " +
			"ORDER BY " +
			"detHalla.por_defecto DESC, detHalla.orden ASC ";

		String reemplazo="";
		
		int cont = 0;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
//			Log4JManager.info("..:Buscar programas servicios del hallazgo codigo >> "+parametros.getCodigoHallazgo()+" buscar programa >> "+parametros.getBuscarProgramas());
			
			if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
			{
				if(parametros.getCodigoHallazgo() > 0)
				{
					reemplazo = " AND hallazgo.consecutivo = "+parametros.getCodigoHallazgo();
				}
				
				consultaStr = consultaStr.replace("?",reemplazo);
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
				Log4JManager.info("\n\nconsultarProgramaParamHallazoStr:: "+ps);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					if(cont == 0 && rs.getString("por_defecto").equals(ConstantesBD.acronimoSi))
					{
						info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
						info.setNombreProgramaServicio(rs.getString("nombre"));
						info.setListaServicios(cargarServiciosParamPrograma(rs.getInt("codigoPkProgramaServicio"), codigoTarifarioServ));
						info.setNumeroSuperficies(rs.getInt("numero_superficies"));
						info.setPermiteTratarVariasVeces(UtilidadTexto.getBoolean(rs.getString("permite_tratar_varias_veces")));
						SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
						return info;
					}
					else if(cont == 0)
					{
						info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
						info.setNombreProgramaServicio(rs.getString("nombre"));
						info.setNumeroSuperficies(rs.getInt("numero_superficies"));
					}
					
					if(cont > 0 
						&& (rs.getString("por_defecto").equals(ConstantesBD.acronimoSi)	|| rs.getInt("orden") == 1))
					{
						if(rs.getInt("orden") == 1)
						{
							info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
							info.setNombreProgramaServicio(rs.getString("nombre"));
							info.setNumeroSuperficies(rs.getInt("numero_superficies"));
						}
						
						if(rs.getString("por_defecto").equals(ConstantesBD.acronimoSi))
						{
							info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
							info.setNombreProgramaServicio(rs.getString("nombre"));
							info.setListaServicios(cargarServiciosParamPrograma(rs.getInt("codigoPkProgramaServicio"), codigoTarifarioServ));
							info.setNumeroSuperficies(rs.getInt("numero_superficies"));
							SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
							return info;
						}
					}
					
					cont++;
				}
				
				info.setListaServicios(cargarServiciosParamPrograma(info.getCodigoPkProgramaServicio().intValue(), codigoTarifarioServ));
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			else
			{
				consultaStr =	consultarServiciosParamHallazgoStr;
				consultaStr=consultaStr.replace(ConstantesBD.separadorSplitComplejo, codigoTarifarioServ);
				
				if(parametros.getCodigoHallazgo() > 0)
					reemplazo = " AND hallazgo.consecutivo = "+parametros.getCodigoHallazgo();
				
				consultaStr = consultaStr.replace("?",reemplazo);
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
				
				Log4JManager.info("\n\nconsulta:: "+ps);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				InfoServicios serv = new InfoServicios();
				while(rs.next())
				{	
					if(cont == 0 && rs.getString("por_defecto").equals(ConstantesBD.acronimoSi))
					{
						serv.setCodigoMostrar(rs.getString("codigoamostrar"));
						serv.getServicio().setCodigo(rs.getInt("codigoPkProgramaServicio"));
						serv.getServicio().setNombre(rs.getString("nombre"));
						serv.setOrderServicio(rs.getInt("orden"));
						serv.setNumeroSuperficies(rs.getInt("numero_superficies"));
						info.getListaServicios().add(0,serv);
						SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
						return info;
					}
					else if(cont == 0)
					{
						serv.setCodigoMostrar(rs.getString("codigoamostrar"));
						serv.getServicio().setCodigo(rs.getInt("codigoPkProgramaServicio"));
						serv.getServicio().setNombre(rs.getString("nombre"));
						serv.setOrderServicio(rs.getInt("orden"));
						serv.setNumeroSuperficies(rs.getInt("numero_superficies"));
					}
					
					if(cont > 0 
						&& (rs.getString("por_defecto").equals(ConstantesBD.acronimoSi)	|| rs.getInt("orden") == 1))
					{
						if(rs.getInt("orden") == 1)
						{
							serv.setCodigoMostrar(rs.getString("codigoamostrar"));
							serv.getServicio().setCodigo(rs.getInt("codigoPkProgramaServicio"));
							serv.getServicio().setNombre(rs.getString("nombre"));
							serv.setOrderServicio(rs.getInt("orden"));
							serv.setNumeroSuperficies(rs.getInt("numero_superficies"));
						}
						
						if(rs.getString("por_defecto").equals(ConstantesBD.acronimoSi))
						{
							serv.setCodigoMostrar(rs.getString("codigoamostrar"));
							serv.getServicio().setCodigo(rs.getInt("codigoPkProgramaServicio"));
							serv.getServicio().setNombre(rs.getString("nombre"));
							serv.setOrderServicio(rs.getInt("orden"));
							serv.setNumeroSuperficies(rs.getInt("numero_superficies"));
							info.getListaServicios().add(0,serv);
							SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
							return info;
						}
					}
					
					cont++;
				}
				
				if(serv.getServicio().getCodigo() > 0)
				{
					info.getListaServicios().add(0,serv);
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);	
				
			}
			// SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(null, null, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}	
		
		return info;
	} 
	
	/**
	 * Lista los programas seg&uacute;n los par&aacute;metros dados
	 * @param DtoProgramasServiciosPlanT dtoBusqueda Dto en el cual se encapcsulan los par&acute;metros de b&uacute;squeda
	 * @return {@link ArrayList}<{@link InfoProgramaServicioPlan}> Lista los programas/servicios encontrados con los parï¿½metros dados
	 */
	public static ArrayList<InfoProgramaServicioPlan> listarProgramaServicioParamHallazgo(DtoProgramasServiciosPlanT dtoBusqueda)
	{
		ArrayList<InfoProgramaServicioPlan> retorno=new ArrayList<InfoProgramaServicioPlan>();

		String reemplazo="";
        String codigoTarifarioServ=dtoBusqueda.getCodigoTarifario();
        
		if(UtilidadTexto.isEmpty(codigoTarifarioServ))
		{
			codigoTarifarioServ=ConstantesBD.codigoTarifarioCups+"";
		}
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			
			/*
			 * Verifico si tengo que buscar por programas o por servicios
			 */
			if(dtoBusqueda.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
			{
				String consultaProgramasStr = 
					"SELECT " +
						"progra.codigo as codigoPkProgramaServicio, " +
						"progra.codigo_programa AS codigo_programa, " +
						"progra.codigo_programa || ' ' || progra.nombre AS nombre, " + // TODO Cambiar en todo lado para que se cargue el nombre solo! agregar el cï¿½digo en la vista
						"detHalla.por_defecto AS por_defecto, " +
						"detHalla.numero_superficies AS numero_superficies " +
					"FROM " +
						"odontologia.hallazgos_odontologicos hallazgo " +
					"INNER JOIN " +
						"odontologia.hallazgos_vs_prog_ser  hallaPro ON (hallazgo.consecutivo= hallaPro.hallazgo) "+
					"INNER JOIN " +
						"odontologia.det_hall_prog_ser detHalla ON(detHalla.hallazgo_vs_prog_ser=hallaPro.codigo) "+
					"INNER JOIN " +
						"odontologia.programas progra ON(progra.codigo=detHalla.programa AND progra.activo = '"+ConstantesBD.acronimoSi+"' ) " +
					"INNER JOIN " +
						"odontologia.detalle_programas dp ON(dp.programas=progra.codigo) " +
					"WHERE " +
						"detHalla.programa IS NOT NULL ";
				
				String groupYorder=
					"GROUP BY " +
						"progra.codigo, " +
						"progra.codigo_programa, " +
						"progra.nombre, " +
						"detHalla.por_defecto, " +
						"detHalla.numero_superficies, " +
						"detHalla.orden " +
					"ORDER BY " +
						"detHalla.por_defecto ASC, " +
						"detHalla.orden ASC ";

				/*
				 * Si se enviï¿½ por parï¿½metro el cï¿½digo de algï¿½n hallazgo lo filtro
				 */
				if(dtoBusqueda.getCodigoHallazgo() > 0)
				{
					consultaProgramasStr += " AND hallazgo.consecutivo = "+dtoBusqueda.getCodigoHallazgo()+" ";
				}
				if(dtoBusqueda.getNumeroSuperficies() != null)
				{
					consultaProgramasStr += " AND detHalla.numero_superficies = "+dtoBusqueda.getNumeroSuperficies()+" ";
				}
				
				consultaProgramasStr+=groupYorder;
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaProgramasStr);
//				Log4JManager.info("\n\nconsultarProgramaParamHallazoStr:: "+ps);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigo_programa"));
					info.setListaServicios(cargarServiciosParamPrograma(rs.getInt("codigoPkProgramaServicio"), codigoTarifarioServ));
					info.setNumeroSuperficies(rs.getInt("numero_superficies"));
					info.setPorDefecto(UtilidadTexto.getBoolean(rs.getString("por_defecto")));
					info.setListaServicios(cargarServiciosParamPrograma(info.getCodigoPkProgramaServicio().intValue(), codigoTarifarioServ));
					retorno.add(info);
				}
				rs.close();
				ps.close();

			}
			else
			{
				String consultarServiciosStr = 
					"SELECT DISTINCT " +
						"case when (detHalla.servicio is null or detHalla.servicio<=0) then detHalla.programa else detHalla.servicio end as codigoPkProgramaServicio," +
						"getnombreservicio(detHalla.servicio, "+codigoTarifarioServ+")  as nombre, " +
						"getcodigoservicio(detHalla.servicio, "+codigoTarifarioServ+") as codigoamostrar, " +
						"detHalla.orden," +
						"detHalla.por_defecto," +
						"detHalla.numero_superficies "+
					"FROM " +
						"odontologia.hallazgos_odontologicos hallazgo " +
					"INNER JOIN " +
						"odontologia.hallazgos_vs_prog_ser hallaPro ON (hallazgo.consecutivo = hallaPro.hallazgo) "+
					"INNER JOIN " +
						"odontologia.det_hall_prog_ser detHalla ON(detHalla.hallazgo_vs_prog_ser = hallaPro.codigo) "+
					"WHERE " +
						"detHalla.servicio IS NOT NULL ? " +
					"ORDER BY " +
						"detHalla.por_defecto ASC, " +
						"detHalla.orden ASC ";

				if(dtoBusqueda.getCodigoHallazgo() > 0)
				{
					reemplazo = " AND hallazgo.consecutivo = "+dtoBusqueda.getCodigoHallazgo();
				}
				
				consultarServiciosStr = consultarServiciosStr.replace("?",reemplazo);
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultarServiciosStr);
				
				Log4JManager.debug("\n\nconsulta:: "+ps);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
				while(rs.next())
				{	
					InfoServicios serv = new InfoServicios();
					serv.setCodigoMostrar(rs.getString("codigoamostrar"));
					serv.getServicio().setCodigo(rs.getInt("codigoPkProgramaServicio"));
					serv.getServicio().setNombre(rs.getString("nombre"));
					serv.setOrderServicio(rs.getInt("orden"));
					serv.setNumeroSuperficies(rs.getInt("numero_superficies"));
					info.setPorDefecto(UtilidadTexto.getBoolean(rs.getString("por_defecto")));
					info.getListaServicios().add(serv);
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
					retorno.add(info);
				}
				rs.close();
				ps.close();
			}
			UtilidadBD.closeConnection(con);
			return retorno;
			
		}
		catch (SQLException e)
		{
			Log4JManager.error("error en carga==> ",e);
		}	
		
		return null;
	} 
	
	/**
	 * Carga los servicios de un programa Plant Tratamiento
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosParamPrograma(int codigoPkPrograma, String codigoTarifarioServ)
	{
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
        String codigoTarifario=codigoTarifarioServ;
        String consultaStr= consultarServProgramaParamStr;
        
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		consultaStr=consultaStr.replace(ConstantesBD.separadorSplitComplejo, codigoTarifario);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ps.setInt(1,codigoPkPrograma);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				info.getServicio().setCodigo(rs.getInt("codigoPkServicio"));
				info.getServicio().setNombre(rs.getString("nombre"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.setOrderServicio(rs.getInt("orden"));
				
				listaServicios.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		
		return listaServicios;
	}
	
	/**
	 * @deprecated
	 * @param DtoPlanTratamientoOdo dto
	 * */
	public static double guardarLogPlanTratamiento(Connection con,DtoLogPlanTratamiento dto)
	{
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{		
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_plan_tratamiento");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(InsertarStrLogPlanTratamiento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,codigo_pk);
			ps.setInt(2,Utilidades.convertirAEntero(dto.getPlanTratamiento()+""));
			ps.setString(3,dto.getEstado());

			if(dto.getMotivo().getCodigo() > 0)
				ps.setInt(4,dto.getMotivo().getCodigo());
			else
				ps.setNull(4,Types.INTEGER);
			
			if(dto.getEspecialidad().getCodigo() > 0)
				ps.setInt(5,dto.getEspecialidad().getCodigo());
			else
				ps.setNull(5,Types.INTEGER);
			
			
			if( dto.getCodigoMedico().getCodigo() > 0)
			
			{
					ps.setInt(6,dto.getCodigoMedico().getCodigo());
			}
			else
			{
				ps.setNull(6,Types.DOUBLE);
			}
			ps.setString(7,dto.getPorConfirmar());
			
			if(dto.getCita() > 0)
				ps.setInt(8,dto.getCita());
			else
				ps.setNull(8,Types.DOUBLE);
			
			ps.setDate(9,Date.valueOf(dto.getModificacion().getFechaModificaFromatoBD()));
			ps.setString(10,dto.getModificacion().getHoraModifica());
			
			if(dto.getValoracion() > 0)
				ps.setDouble(11,dto.getValoracion());
			else
				ps.setNull(11,Types.DOUBLE);
			
			if(dto.getEvolucion() > 0)
				ps.setDouble(12,dto.getEvolucion());
			else
				ps.setNull(12,Types.DOUBLE);
			
			if(!dto.getImagen().equals(""))
				ps.setString(13,dto.getImagen()+Utilidades.convertirAEntero(codigo_pk+""));
			else
				ps.setNull(13,Types.VARCHAR);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return codigo_pk;
			}
			
			ps.close();
			
		}
		catch (SQLException  e) {
			Log4JManager.info(e.toString()+" "+UtilidadLog.obtenerString(dto,true));
		}
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	
	/**
	 * @deprecated
	 * @param DtoLogDetPlanTratamiento dto
	 * */
	public static double guardarLogDetPlanTratamiento(Connection con,DtoLogDetPlanTratamiento dto)
	{
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_plan_tratamiento");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(InsertarStrLogDetPlanTratamiento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,codigo_pk);
			ps.setDouble(2,dto.getDetPlanTratamiento());

			if(dto.getPiezaDental() > 0)
				ps.setInt(3,dto.getPiezaDental());
			else
				ps.setNull(3,Types.INTEGER);

			if(dto.getSuperficie() > 0)
				ps.setInt(4,dto.getSuperficie());
			else
				ps.setNull(4,Types.INTEGER);

			ps.setInt(5,dto.getHallazgo());

			if(!dto.getClasificacion().equals(""))
				ps.setString(6,dto.getClasificacion());
			else
				ps.setNull(6,Types.VARCHAR);

			ps.setString(7,dto.getPorConfirmar());
			
			if(dto.getConvencion() > 0)
				ps.setInt(8,dto.getConvencion());
			else
				ps.setNull(8,Types.INTEGER);
			
			ps.setString(9,dto.getUsuarioModifica().getUsuarioModifica());
			ps.setDate(10,Date.valueOf(dto.getUsuarioModifica().getFechaModificaFromatoBD()));
			ps.setString(11,dto.getUsuarioModifica().getHoraModifica());
			
			if(dto.getCita() > 0)
				ps.setInt(12,dto.getCita());
			else
				ps.setNull(12,Types.NUMERIC);
			
			if(dto.getValoracion() > 0)
				ps.setDouble(13,dto.getValoracion());
			else
				ps.setNull(13,Types.DOUBLE);
			
			if(dto.getEvolucion() > 0)
				ps.setDouble(14,dto.getEvolucion());
			else
				ps.setNull(14,Types.DOUBLE);
			
			if(dto.getEspecialidad().getCodigo() > 0)
				ps.setInt(15,dto.getEspecialidad().getCodigo());
			else
				ps.setNull(15,Types.INTEGER);
			
			if(ps.executeUpdate()>0){
				ps.close();
				return codigo_pk;
				}
			ps.close();
		
			
		}
		catch (SQLException  e) {
			Log4JManager.info(e.toString()+" "+UtilidadLog.obtenerString(dto,true));
		}
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	/**
	 * @deprecated
	 * @param DtoLogProgServPlant dto
	 * */
	public static double guardarLogProgramasServiciosPlanT(Connection con,DtoLogProgServPlant dto)
	{
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{	
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_pro_ser_plan_t");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(InsertarStrLogProgramasServiciosPlanT,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,codigo_pk);
			ps.setDouble(2,dto.getProgServPlant());

			if(dto.getEstadoPrograma().equals(""))
				ps.setString(3,dto.getEstadoPrograma());
			else
				ps.setNull(3,Types.VARCHAR);

			if(dto.getMotivo().getCodigo() > 0)
				ps.setInt(4,dto.getMotivo().getCodigo());
			else
				ps.setNull(4,Types.INTEGER);
			
			if(dto.getConvencion() > 0)
				ps.setInt(5,dto.getConvencion());
			else
				ps.setNull(5,Types.INTEGER);
			
			if(!dto.getInclusion().equals(""))
				ps.setString(6,dto.getInclusion());
			else
				ps.setNull(6,Types.VARCHAR);
			
			if(!dto.getExclusion().equals(""))
				ps.setString(7,dto.getExclusion());
			else
				ps.setNull(7,Types.VARCHAR);
			
			if(!dto.getGarantia().equals(""))
				ps.setString(8,dto.getGarantia());
			else
				ps.setNull(8,Types.VARCHAR);
			
			ps.setString(9,dto.getEstadoServicio());
			ps.setString(10,dto.getIndPrograma());
			ps.setString(11,dto.getIndServicio());
			ps.setString(12,dto.getPorConfirmar());
			ps.setString(13,dto.getUsuarioModifica().getUsuarioModifica());
			ps.setDate(14,Date.valueOf(dto.getUsuarioModifica().getFechaModificaFromatoBD()));
			ps.setString(15,dto.getUsuarioModifica().getHoraModifica());
			
			if(dto.getEspecialidad().getCodigo() > 0)
				ps.setInt(16,dto.getEspecialidad().getCodigo());
			else
				ps.setNull(16,Types.INTEGER);
			
			if(dto.getValoracion() > 0)
				ps.setDouble(17,dto.getValoracion());
			else
				ps.setNull(17,Types.DOUBLE);
			
			if(dto.getEvolucion() > 0)
				ps.setDouble(18,dto.getEvolucion());
			else
				ps.setNull(18,Types.DOUBLE);
			
			ps.setInt(19,dto.getOrdenServicio());
			ps.setString(20,dto.getActivo());
			
			if(ps.executeUpdate()>0){			
				ps.close();
				return codigo_pk;
			}
			ps.close();	
		}
		catch (SQLException  e) {
			Log4JManager.info(e.toString()+" "+UtilidadLog.obtenerString(dto,true));
		}
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	
	/**
	 * Carga las Superficies del Diente
	 * @return
	 */
	public static ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int institucion)
	{
		ArrayList<DtoSectorSuperficieCuadrante> lista = new ArrayList<DtoSectorSuperficieCuadrante>();

		String consultarStrSuperficiesPlanT = 
			"SELECT " +
				"s.codigo AS codigo, " +
				"s.nombre AS nombre, " +
				"s.cod_institucion AS cod_institucion, " +
				"s.activo AS activo, " +
				"ssc.codigo_pk AS codigo_pk, " +
				"ssc.superficie AS superficie, " +
				"ssc.sector AS sector, " +
				"ssc.pieza AS pieza " +
			"FROM " +
				"historiaclinica.superficie_dental s " +
			"INNER JOIN " +
				"odontologia.sector_superficie_cuadrante ssc " +
			"ON(ssc.superficie=s.codigo) "+
				"AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
				"AND cod_institucion = ? " ;

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultarStrSuperficiesPlanT );
			ps.setInt(1,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoSuperficieDental superficie=new DtoSuperficieDental();
				superficie.setCodigo(rs.getDouble("codigo"));
				superficie.setNombre(rs.getString("nombre"));
				superficie.setInstitucion(rs.getInt("cod_institucion"));
				superficie.setActivo(rs.getBoolean("activo")?1:0);
				DtoSectorSuperficieCuadrante sectorSuperficieCuadrante=new DtoSectorSuperficieCuadrante();
				sectorSuperficieCuadrante.setCodigoPk(rs.getInt("codigo_pk")); // Cï¿½digo en BD
				sectorSuperficieCuadrante.setSuperficie(superficie); // Dto con datos de la superficie
				sectorSuperficieCuadrante.setSector(rs.getInt("sector")); // Sector al que aplica
				sectorSuperficieCuadrante.setPieza(rs.getInt("pieza")); // Pieza para la cual aplica
				lista.add(sectorSuperficieCuadrante);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		
		return lista;
	}
	
	/**
	 * Actualiza el estado activo del detalle del plan de tratamiento
	 * @param Connection con
	 * @param DtoProgramasServiciosPlanT dto
	 */
	public static boolean actualizarActivoDetallePlanTrat(Connection con,DtoDetallePlanTratamiento dto)
	{
		String consultaStr = 
				"UPDATE " +
				"odontologia.det_plan_tratamiento d " +
				"SET activo='"+dto.getActivo()+"' " +
				"WHERE 1=1 ";
		
		if(dto.getPlanTratamiento()>0)
			consultaStr+=" AND plan_tratamiento= "+dto.getPlanTratamiento();

		if(dto.getCodigo()>0)
			consultaStr+=" AND codigo_pk= "+dto.getCodigo();
		
		if(dto.getHallazgo() > 0)
			consultaStr+=" AND hallazgo= "+dto.getHallazgo();
			
		try 
		{
			//Log4JManager.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN actualizar solo prog ser " + e);
		}
		
		return false;
	}
	
	/**
	 * Actualiza el estado activo de los programas del plan de tratamiento
	 * @param Connection con
	 * @param DtoProgramasServiciosPlanT dto
	 */
	public static boolean actualizarActivoProgServPlanTr(Connection con,DtoProgramasServiciosPlanT dto)
	{
		String consultaStr = 
				"UPDATE " +
				"odontologia.programas_servicios_plan_t  " +
				"SET activo='"+dto.getActivo()+"' " +
				"WHERE 1=1 ";
		
		if(dto.getDetPlanTratamiento().doubleValue()>0)
			consultaStr+=" AND det_plan_tratamiento="+dto.getDetPlanTratamiento();

		if(dto.getCodigoPk().doubleValue()>0)		
			consultaStr+=" AND codigo_pk= "+dto.getCodigoPk();
		
		if(dto.getPrograma().getCodigo()>0)
			consultaStr+=" AND programa= "+dto.getPrograma().getCodigo();
		
		else if(dto.getServicio().getCodigo()>0)
			consultaStr+=" AND servicio= "+dto.getServicio().getCodigo();
			
		try 
		{
			Log4JManager.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN actualizar solo prog ser " + e);
		}	
		
		
		return false;
	}
	
		/**
	  * Metodo que actuliza el campo INCLUSION 
	  * @param con
	  * @param dto
	  * @return
	  */
	
	 public static boolean actualizarInclusionProgServPlanTr(Connection con,DtoProgramasServiciosPlanT dto)
	 {
		 String consultaStr = 
			 	"UPDATE " +
			 	"odontologia.programas_servicios_plan_t  " +
			 	"SET inclusion='"+dto.getInclusion()+"' " +
			 	" " +ConstantesBD.separadorSplit+" "+
		 		"WHERE 1=1 ";
		 
		 if(dto.getDetPlanTratamiento().doubleValue()>0)
			 consultaStr+=" AND det_plan_tratamiento="+dto.getDetPlanTratamiento();

		 if(dto.getCodigoPk().doubleValue()>0)		 
			 consultaStr+=" AND codigo_pk= "+dto.getCodigoPk();
		 
		 if(!dto.getActivo().equals(""))
		   { 
			 consultaStr=consultaStr.replace(ConstantesBD.separadorSplit, ", activo = '"+dto.getActivo()+"' ");
		   }else
		   {
			   consultaStr=consultaStr.replace(ConstantesBD.separadorSplit, "");
		   }
		 
		 if(dto.getPrograma().getCodigo()>0)
			 consultaStr+=" AND programa= "+dto.getPrograma().getCodigo();
		 	 
		 else if(dto.getServicio().getCodigo()>0)
			 consultaStr+=" AND servicio= "+dto.getServicio().getCodigo();
			
		try 
		{
			Log4JManager.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN actualizar solo prog ser " + e);
		}	
		
		
		return false;
	 }
	
	
	
	/**
	 * Actualiza informacion en detalla de plan de tratamiento
	 * @param Connection con
	 * @param DtoDetallePlanTratamiento dto
	 * */
	public static boolean actualizarDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto)
	{
		String cadena = ActualizarStrDetPlanT;
		boolean campoAnte = false;
		
		if(dto.getPiezaDental() > 0)
		{
			cadena += " pieza_dental = "+dto.getPiezaDental();
			campoAnte = true;
		}
		
		if(dto.getSuperficie() > 0)
		{
			cadena += (campoAnte?",":"")+" superficie = "+dto.getSuperficie();
			campoAnte = true;
		}
		
		if(dto.getHallazgo() > 0)
		{
			cadena += (campoAnte?",":"")+" hallazgo = "+dto.getHallazgo();
			campoAnte = true;
		}
		
		if(!UtilidadTexto.isEmpty(dto.getActivo()))
		{
			cadena += (campoAnte?",":"")+" activo = '"+dto.getActivo()+"' ";
			campoAnte = true;
		}
		
		if(!UtilidadTexto.isEmpty(dto.getClasificacion()))
		{
			cadena += (campoAnte?",":"")+" clasificacion = '"+dto.getClasificacion()+"' ";
			campoAnte = true;
		}
		
		
		cadena += " ,usuario_modifica = '"+dto.getFechaUsuarioModifica().getUsuarioModifica() +"', " +
				 "fecha_modifica = '"+dto.getFechaUsuarioModifica().getFechaModificaFromatoBD()+"', " +
				 "hora_modifica = '"+dto.getFechaUsuarioModifica().getHoraModifica()+"' ";
		
		cadena += " WHERE codigo_pk = "+dto.getCodigo()+" ";
		
		try 
		{
			//Log4JManager.info("borrar actualizara >> "+cadena);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			
			ps.close();
			
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN actualizarDetPlanTratamiento >> " +e+" "+cadena);
		}	
		
		return false;
	}
	
	/**
	 * Método que se encarga de consultar si existen registros de detalle
	 * plan tratamiento asociados a una cita específica y que no tienen asociada
	 * una clasificación
	 * 
	 * 
	 * @param codigoCita
	 * @return
	 */
	public static boolean existeDetPlanTratamientoSinClasificacion (int codigoCita){
		
		boolean resultado = false;
		
		String sentencia=
			"SELECT COUNT (*) AS sin_clasificacion " +
			"FROM " +
				"odontologia.det_plan_tratamiento " +
			"WHERE " +
				"cita=? " +
			"AND " +
				"clasificacion IS NULL";
		
		try {
			
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, codigoCita);
			
			Log4JManager.info(psd);
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			if(rsd.next())
			{
				if(rsd.getInt("sin_clasificacion")>0){
					
					resultado = true;
				}
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
			
		} catch (SQLException e) {
			
			Log4JManager.error("Error consultando registros det_plan_tratamiento sin clasificacion",e);
		}
		
		return resultado;
	}
	
	
	//********************************************************************************************************************************
	//FIM METODOS PLAN TRATAMIENTO ODONTOGRAMA****************************************************************************************
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
		
	/**
	 * CARGA EL CODIGO DEL PLAN DE TRATAMIENTO 
	 * TODO VALIDAR QUE EL STRING DE ESTADOS SI LLEGE CORRECTAMENTE Y QUE LA CUENTEA  ESTE ACTIVA
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public static BigDecimal obtenerUltimoCodigoPlanTratamiento ( int ingreso, ArrayList<String> estados, String porConfirmarOPCIONAL) 
	{
		String consultaStr=" select coalesce(max(codigo_pk), "+ConstantesBD.codigoNuncaValido+") as codigoPk from odontologia.plan_tratamiento where ingreso="+ingreso+" "; 
		
		if(estados.size() > 0)
			consultaStr += " and estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
		
		if(!UtilidadTexto.isEmpty(porConfirmarOPCIONAL))
		{
			consultaStr+=" and por_confirmar= '"+UtilidadTexto.convertirSN(porConfirmarOPCIONAL)+"' ";
		}
		
		BigDecimal codigo= new BigDecimal(ConstantesBD.codigoNuncaValido); 
		
		Log4JManager.info("obtenerUltimoCodigoPlanTratamiento--->"+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigo= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}
			
		return codigo;
	}
	
	/**
	 * CARGA EL CODIGO DEL PLAN DE TRATAMIENTO x ingreso y codigo de cita
	 * @param ingreso
	 * @param estados
	 * @param codigoCita
	 * @return
	 */
	public static int obtenerUltimoCodigoPlanTratamientoXIngresoXCita ( int ingreso, ArrayList<String> estados, int codigoCita) 
	{
		String consultaStr=" select coalesce(max(codigo_pk), "+ConstantesBD.codigoNuncaValido+") as codigoPk from " +
				"odontologia.his_conf_plan_tratamiento where ingreso="+ingreso+" and cita = "+ codigoCita +" "; 
		
		if(estados.size() > 0)
			consultaStr += " and estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
		
		int codigo= ConstantesBD.codigoNuncaValido; 
		
		Log4JManager.info("obtenerUltimoCodigoPlanTratamientoXingresoXcita--->"+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigo= rs.getInt(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return codigo;
	}
	
	
	
	/**
	 * CARGA LAS PIEZAS 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezas(BigDecimal codigoPkPlanTratamiento, String seccion, String porConfirmarOPCIONAL)
	{
		String consultaStr="SELECT DISTINCT d.pieza_dental AS pieza_dental, p.descripcion AS descripcion FROM odontologia.det_plan_tratamiento d INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=d.pieza_dental) WHERE d.plan_tratamiento="+codigoPkPlanTratamiento+" and d.seccion= '"+seccion+"' ";
		
		consultaStr+=(!UtilidadTexto.isEmpty(porConfirmarOPCIONAL))?" AND d.por_confirmar='"+UtilidadTexto.convertirSN(porConfirmarOPCIONAL)+"' ": " ";
		
		/*XPLANNER 148136, SEGUN DOCUMENTO DEBE ORDENAR DESENDENTEMENTE, esto fue organizado con aprobaciï¿½n de Germï¿½n y Felipe*/
		consultaStr+=" order by p.descripcion desc ";
		
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		
		Log4JManager.info("obtenerPiezas--->"+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt("pieza_dental"));
				info.setNombre(rs.getString("descripcion"));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	
	
	
	/**1088
	 * CARGAR LA PIEZAS PARA EL PLAN DE TRATAMIENTO
	 */
	public static  ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantias(DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento , int institucion )
	{
		Log4JManager.info("****************************************************************************************************");
		Log4JManager.info("****************************************************************************************************");
		Log4JManager.info("CARGAR PIEZAS INCLUSION o Garantias");
		Log4JManager.info("****************************************************************************************************");
		String consultaStr="SELECT DISTINCT d.pieza_dental, p.descripcion FROM odontologia.det_plan_tratamiento d " +
							"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=d.pieza_dental) INNER JOIN " +
							"odontologia.programas_servicios_plan_t progServ on(progServ.det_plan_tratamiento=d.codigo_pk) " +
							"where  1=1 ";
		
		consultaStr+=" AND d.plan_tratamiento= "+codigoPlanTratamiento;
		consultaStr+=(dto.getCodigoPk().doubleValue()>0)?" AND progServ.codigo_pk= "+dto.getCodigoPk():" ";
		consultaStr+= UtilidadTexto.isEmpty(dto.getGarantia())?"":" AND  progServ.garantia='"+dto.getGarantia()+"' ";
		consultaStr+= UtilidadTexto.isEmpty(dto.getInclusion())?"":" AND  progServ.inclusion='"+dto.getInclusion()+"'";
		consultaStr+= UtilidadTexto.isEmpty(dto.getEstadoAutorizacion())? "": " AND  progServ.estado_autorizacion='"+dto.getEstadoAutorizacion()+"'";
		consultaStr+= " AND progServ.activo='"+dto.getActivo()+"'";
		consultaStr+= !UtilidadTexto.isEmpty(dto.getPorConfirmado())?" AND progServ.por_confirmar='"+dto.getPorConfirmado()+"' ": " "; 
		
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		
		Log4JManager.info("OBTENER PIEZAS PARA LAS INCLUSIONES o GARANTIAS--->\n\n\n"+consultaStr+"\n\n\n");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt(1));
				info.setNombre(rs.getString(2));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
		
	}
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(BigDecimal codigoPkPlanTratamiento, int pieza, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios, BigDecimal codigoPkPresupuesto , int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		Log4JManager.info("\n ********************************************************************************************************************************");
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		String consultaStr="SELECT " +
								"d.codigo_pk as codigodetallepk, " +
								"d.hallazgo as codigohallazgo, " +
								"h.nombre as nombrehallazgo, " +
								"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie " +
							"FROM " +
								"odontologia.det_plan_tratamiento d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN " +
									"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
								"LEFT OUTER JOIN " +
									"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"WHERE " +
								"d.plan_tratamiento="+codigoPkPlanTratamiento+" " +
								"and d.pieza_dental="+pieza ;
		
							//	"and d.seccion='"+seccion+"'";
				consultaStr+=(UtilidadTexto.isEmpty(seccion))?"":" AND d.seccion='"+seccion+"'";	
				consultaStr+=(UtilidadTexto.isEmpty(porConfirmar))?"": " AND d.por_confirmar='"+porConfirmar+"'";
				consultaStr+=" AND d.activo='"+ConstantesBD.acronimoSi+"'";
		Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				//CARGAR LOS PROGRAMAS O SERVICIOS --------------
				
				if(codigoPkPresupuesto.doubleValue()<=0)
				{	
					hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServicios(rs.getBigDecimal("codigodetallepk"), estadosProgramasOservicios, utilizaProgramas, porConfirmar,cargaServicios, institucion));
				}	
				else
				{
					hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosXPresupuesto(codigoPkPresupuesto, rs.getBigDecimal("codigodetallepk"), estadosProgramasOservicios, utilizaProgramas, porConfirmar));
				}
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}
		return array;
	}
	
	
	
	
	/**
	 * Obtener la hallazgos de una superficie utilizando:
	 *  DtoPlanTratamiento
	 * 	DtoDetallePlanTratamiento
	 *	DtoProgramasServiciosPlanT
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTO(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlanT,  DtoProgramasServiciosPlanT dtoProgramasServicios , ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios , BigDecimal codigoPkPresupuesto , int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		Log4JManager.info("\n ********************************************************************************************************************************");
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		
		String consultaStr="SELECT " +
								"d.codigo_pk as codigodetallepk, " +
								"d.hallazgo as codigohallazgo, " +
								"h.nombre as nombrehallazgo, " +
								"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie " +
							"FROM " +
								"odontologia.det_plan_tratamiento d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN " +
									"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
								"LEFT OUTER JOIN " +
									"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"WHERE " +
								"d.plan_tratamiento="+dtoPlanTratamiento.getCodigoPk()+" " +
								"and d.pieza_dental="+dtoDetallePlanT.getPiezaDental();
		
				//FILTROS
				consultaStr+=(UtilidadTexto.isEmpty(dtoDetallePlanT.getSeccion()))?"":" AND d.seccion='"+dtoDetallePlanT.getSeccion()+"'";	
				consultaStr+=(UtilidadTexto.isEmpty(porConfirmar))?"": " AND d.por_confirmar='"+porConfirmar+"'";
		
				Log4JManager.info("\n\n\n");		
		Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
		Log4JManager.info("\n\n\n");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			// CARGA TODOS LOS HALLAZGOS Y LOS DETALLES DEL PLAN DE TRATAMIENTO
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
			
				
				//CARGAR LOS PROGRAMAS O SERVICIOS --------------
				
				if(codigoPkPresupuesto.doubleValue()<=0)
				{	
					dtoDetallePlanT.setCodigo(rs.getDouble("codigodetallepk"));
					hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosDTO(dtoDetallePlanT, dtoProgramasServicios,  estadosProgramasOservicios, utilizaProgramas, porConfirmar,cargaServicios, institucion ));
				}	
				else
				{
					hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosXPresupuesto(codigoPkPresupuesto, rs.getBigDecimal("codigodetallepk"), estadosProgramasOservicios, utilizaProgramas, porConfirmar));
				}
				
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return array;
	}
	
	
	/**
	 * CARGAR LAS SUPERFICIES CON LOS PROGRAMAS Y SERVICIOS
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerSuperficies(BigDecimal codigoPkPlanTratamiento, int pieza, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios, int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		Log4JManager.info("\n ********************************************************************************************************************************");
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		
		String consultaStr="SELECT " +
								"d.codigo_pk as codigodetallepk, " +
								"d.hallazgo as codigohallazgo, " +
								"h.nombre as nombrehallazgo, " +
								"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(s.nombre, '') as nombresuperficie " +
							"FROM " +
								"odontologia.det_plan_tratamiento d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN  historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							"WHERE " +
								"d.plan_tratamiento="+codigoPkPlanTratamiento+" " +
								"and d.pieza_dental="+pieza+" " +
								"and d.seccion='"+seccion+"'";
								consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" and d.por_confirmar='"+porConfirmar+"'";
		
		Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				//CARGAR LOS PROGRAMAS O SERVICIOS --------------
				hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServicios(rs.getBigDecimal("codigodetallepk"), estadosProgramasOservicios, utilizaProgramas, porConfirmar, cargaServicios , institucion));
				
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(BigDecimal codigoPkPlanTratamiento, int hallazgoOPCIONAL, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar,  boolean cargaServicios, BigDecimal codigoPkPresupuesto , int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		Log4JManager.info("\n\n\n\n\n\n\n ********************************************************************************************************************************");
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		String consultaStr="SELECT " +
								"d.codigo_pk as codigodetallepk, " +
								"d.hallazgo as codigohallazgo, " +
								"h.nombre as nombrehallazgo, " +
								"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(s.nombre, '') as nombresuperficie " +
							"FROM " +
								"odontologia.det_plan_tratamiento d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN  historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							"WHERE " +
								"d.plan_tratamiento="+codigoPkPlanTratamiento+" " ;
		
		consultaStr+=(hallazgoOPCIONAL>0)?"and d.hallazgo="+hallazgoOPCIONAL+" ":" ";
		consultaStr+=	"and d.seccion='"+seccion+"' ";
		consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND d.por_confirmar='"+porConfirmar+"'";
		
		
		Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
		Log4JManager.info("\n\n\n\n\n\n\n ********************************************************************************************************************************");
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				if(codigoPkPresupuesto.doubleValue()<=0)
				{	
					hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServicios(rs.getBigDecimal("codigodetallepk"), estadosProgramasOservicios, utilizaProgramas, porConfirmar, cargaServicios, institucion)); //false ???? falta verifica si todos son porconfirmar
				}
				else
				{
					hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosXPresupuesto(codigoPkPresupuesto, rs.getBigDecimal("codigodetallepk"), estadosProgramasOservicios, utilizaProgramas, porConfirmar)); //false ???? falta verifica si todos son porconfirmar
				}
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	 
	
	/**
	 * RETORNAR PROGRAMAS PARA EL PLAN DE TRATAMIENTO
	 * Faltan que la condiciones iniciales para cargar el programa
	 */
	public static ArrayList<InfoProgramaServicioPlan>  cargarListaProgramasPlanTratamiento (BigDecimal detallePlanTratamiento,String porConfirmar, int institucion)
	{
		ArrayList<InfoProgramaServicioPlan> listaProgramasPlantratamiento = new ArrayList<InfoProgramaServicioPlan>();
		
		String consultaStr="SELECT DISTINCT " +
													"pro.codigo as codigo ," +
													"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
													"pro.codigo_programa as codigoamostrar ," +
													"plant.estado_programa as estadoPrograma ," +
													"plant.inclusion as inclusion, " +
													"plant.exclusion as exclusion," +
													"plant.garantia as garantia ," +
													"plant.activo as activo ," +
													"plant.indicativo_programa as indicativoPrograma ," +
													"plant.estado_autorizacion as estadoAutorizacion"+
												"from " +
													"odontologia.programas_servicios_plan_t plant " +
													"LEFT OUTER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
												"where " +
													" plant.det_plan_tratamiento="+detallePlanTratamiento+
													" AND plant.por_confirmar='"+porConfirmar+"'  "+ //falta verificar
													" AND plant.activo='"+ConstantesBD.acronimoSi+"'";
		
		Log4JManager.info("CONSULTA PROGRAMAS -->"+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
				info.setNombreProgramaServicio(rs.getString("nombre"));
				info.setCodigoAmostrar(rs.getString("codigoamostrar"));
				info.setEstadoPrograma(rs.getString("estadoPrograma"));
				info.setNewEstadoProg(rs.getString("estadoPrograma"));
				info.setInclusion(rs.getString("inclusion"));
				info.setExclusion(rs.getString("exclusion"));
				info.setGarantia(rs.getString("garantia"));
				//cargar los servicio del plan de tratamientos
				info.setListaServicios(cargarServiciosDeProgramasPlanT(detallePlanTratamiento,rs.getBigDecimal("codigo"), institucion ));
				listaProgramasPlantratamiento.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		
		return listaProgramasPlantratamiento;
	}
	
	
	
	/**
	 * CARGA LOS SERVICIOS DE UN PROGRAMA
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(BigDecimal detallePlanTratamiento, BigDecimal codigoPrograma , int institucion){
		Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n**************************************************************/************************************************************");
		Log4JManager.info("******************************************OBTENER SERVICIOS DEL PROGRAMA **************************************************");
		
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
								String consultaStr= "SELECT DISTINCT " +
														" plant.servicio as codigoServicio ," +
														" getnombreservicio(plant.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+")  as nombreServicio , " +
														" getcodigoservicio(plant.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as  codigoamostrar , " +
														" plant.estado_servicio as estadoServicio, "+
														"plant.orden_servicio AS orden_servicio"+
													" from " +
														" odontologia.programas_servicios_plan_t  plant " +
													"where " +
														"plant.det_plan_tratamiento="+detallePlanTratamiento+" " +
														" AND plant.activo='"+ConstantesBD.acronimoSi+"' " +
														"AND plant.programa="+codigoPrograma.doubleValue()+" AND plant.servicio IS NOT NULL "+
														//	" and estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+") "; //Falta especificaciones
														"ORDER BY plant.orden_servicio";
		Log4JManager.info("****************************************	CONSULTA SERVICIOS  ****************************************");
		Log4JManager.info(consultaStr);						
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				//cargar los servicio del plan de tratamientos
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.setEstadoServicio(rs.getString("estadoServicio"));
				info.setTieneArticulos(cargarServArtIncCitaOdo(  rs.getInt("codigoServicio") ,detallePlanTratamiento )); // cargar los articulos
				info.setOrderServicio(rs.getInt("orden_servicio"));
				listaServicios.add(info);
			}
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		

		
		return listaServicios;
	}
	
	
	
	
	/**
	 * CARGA LOS SERVICIOS DE UN PROGRAMA
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanTDTO(BigDecimal detallePlanTratamiento, BigDecimal codigoPrograma, int institucion){
		Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n**************************************************************/************************************************************");
		Log4JManager.info("******************************************OBTENER SERVICIOS DEL PROGRAMA **************************************************");
		
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
								String consultaStr= "SELECT DISTINCT " +
														"plant.servicio as codigoServicio ," +
														"getnombreservicio(plant.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+")  as nombreServicio , " +
														"getcodigoservicio(plant.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as  codigoamostrar , " +
														"plant.estado_servicio as estadoServicio"+
													" from " +
														" odontologia.programas_servicios_plan_t  plant " +
													"where " +
														"plant.det_plan_tratamiento="+detallePlanTratamiento+" " +
														" AND plant.activo='"+ConstantesBD.acronimoSi+"' " +
														"AND plant.programa="+codigoPrograma.doubleValue()+" AND plant.servicio IS NOT NULL ";
														//	" and estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+") "; //Falta especificaciones
		
		Log4JManager.info("****************************************	CONSULTA SERVICIOS  ****************************************");
		Log4JManager.info(consultaStr);						
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				//cargar los servicio del plan de tratamientos
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.setEstadoServicio(rs.getString("estadoServicio"));
				info.setTieneArticulos(cargarServArtIncCitaOdo(  rs.getInt("codigoServicio") ,detallePlanTratamiento )); // cargar los articulos
				listaServicios.add(info);
			}
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		

		
		return listaServicios;
	}
	/**
	 * RETORNA LOS PROGRAMAS RELACIONADOS CON LOS HALLAZGOS Y LOS PROGRAMAS SERVICIOS PLAN T
	 * @param CodigoHallazgo
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasServiciosHallazgosPlanTramiento(double codigoHallazgo, String codigoProgramas, BigDecimal codigoDetalle , boolean utilizaProgramas, double codigoServicio, BigDecimal presupuesto, String codigosAsignadosPresupuesto, ArrayList<InfoNumSuperficiesPresupuesto> superficies )
	{
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		Log4JManager.info("*****************************************************************************************************");
		Log4JManager.info("-----------------------------------------------------------------------------------------------------");
		Log4JManager.info("----------------------------CARGAR PROGRAMAS SERVICIOS HALLAZGOS DETALLE PROGRAMAS SERVICIOS----------");
		String consultaStr="";
		
		String hallazgoAplicaA=  SqlBaseHallazgosOdontologicosDao.obtenerAplicaAXHallazgo(codigoHallazgo);
		
		if(utilizaProgramas)
		{
			consultaStr =	"select  " +
								"progra.codigo as codigoPkProgramaServicio, " +
								"progra.codigo_programa  ||' '||  progra.nombre  as nombrePrograma, " +
								"coalesce(detHalla.numero_superficies,0) as num_superficies,  ";
								if(!UtilidadTexto.isEmpty(codigoProgramas))
								{
									consultaStr +=" case when progra.codigo in ("+codigoProgramas+") then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as yaCheck, ";
								}
								else
								{
									consultaStr +=" '"+ConstantesBD.acronimoNo+"' as yaCheck, ";
								}
								if(!UtilidadTexto.isEmpty(codigosAsignadosPresupuesto))
								{
									consultaStr +=" case when progra.codigo in ("+codigosAsignadosPresupuesto+") then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as yaSelPresupuesto ";
								}
								else
								{
									consultaStr +=" '"+ConstantesBD.acronimoNo+"' as yaSelPresupuesto ";
								}
			consultaStr+=	"from " +
								"odontologia.hallazgos_odontologicos hallazgo " +
								"INNER JOIN odontologia.hallazgos_vs_prog_ser hallaPro ON (hallazgo.consecutivo= hallaPro.hallazgo) "+
								"INNER JOIN odontologia.det_hall_prog_ser detHalla ON(detHalla.hallazgo_vs_prog_ser=hallaPro.codigo)"+
								"INNER JOIN odontologia.programas progra ON(progra.codigo=detHalla.programa ) " +
							"where " +
								"hallazgo.consecutivo="+codigoHallazgo+" " +
								"and hallazgo.activo='"+ConstantesBD.acronimoSi+"' ";
			
			/*if(hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaADiente))
			{
				consultaStr+=" and (detHalla.numero_superficies is null or detHalla.numero_superficies=0) ";
			}
			else if(hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaASuperficie))
			{
				consultaStr+=" and (detHalla.numero_superficies is not null and detHalla.numero_superficies <= "+superficies.size()+" ) ";
			}*/
			
			/*if(hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaADiente))
			{
				consultaStr+=" and (detHalla.numero_superficies is null or detHalla.numero_superficies=0) ";
			}
			else if(hallazgoAplicaA.equals(ConstantesIntegridadDominio.acronimoAplicaASuperficie))
			{
				consultaStr+=" and (detHalla.numero_superficies is not null and detHalla.numero_superficies <= "+superficies.size()+" ) ";
			}*/
			
			//si no viene de un presupuesto entonces cargamos los del plan de tratamiento
			if(presupuesto.doubleValue()<=0)
			{	
				consultaStr+=	" union "+ 
									"select " +
										"progra.codigo as codigoPkProgramaServicio, " +
										"progra.codigo_programa  ||' '||  progra.nombre  as nombrePrograma, " +
										" (" +
										"  SELECT DISTINCT coalesce(dhps.numero_superficies,0) " +
										"    FROM odontologia.hallazgos_vs_prog_ser hvps " +
										"    INNER JOIN odontologia.det_hall_prog_ser dhps ON(dhps.hallazgo_vs_prog_ser=hvps.codigo) " +
										"    INNER JOIN odontologia.det_plan_tratamiento detpt ON(detpt.codigo_pk=detPlan.codigo_pk) " +
										"    WHERE  detpt.hallazgo=detPlan.hallazgo and dhps.programa=progra.codigo " +
										"  ) as num_superficies, ";
										
										if(!UtilidadTexto.isEmpty(codigoProgramas))
										{
											consultaStr +=" case when progra.codigo in ("+codigoProgramas+") then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as yaCheck, ";
										}
										else
										{
											consultaStr +=" '"+ConstantesBD.acronimoNo+"' as yaCheck, ";
										}
										if(!UtilidadTexto.isEmpty(codigosAsignadosPresupuesto))
										{
											consultaStr +=" case when progra.codigo in ("+codigosAsignadosPresupuesto+") then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as yaSelPresupuesto ";
										}
										else
										{
											consultaStr +=" '"+ConstantesBD.acronimoNo+"' as yaSelPresupuesto ";
										}
				consultaStr+=	"from " +
									"odontologia.det_plan_tratamiento detPlan "+ 
									"INNER JOIN  odontologia.programas_servicios_plan_t proServ ON (detPlan.codigo_pk=proServ.det_plan_tratamiento ) " +
									"INNER JOIN  odontologia.programas progra ON (progra.codigo= proServ.programa) " +
								"where " +
									"detPlan.codigo_pk="+codigoDetalle+" " +
									"AND proServ.activo='"+ConstantesBD.acronimoSi+"' ";
			}
			else
			{	
				consultaStr+=	" union "+ 
									"select distinct " +
										"progra.codigo as codigoPkProgramaServicio, " +
										"progra.codigo_programa  ||' '||  progra.nombre  as nombrePrograma, "+
										" (" +
										"  SELECT DISTINCT coalesce(dhps.numero_superficies,0) " +
										"    FROM odontologia.hallazgos_vs_prog_ser hvps " +
										"    INNER JOIN odontologia.det_hall_prog_ser dhps ON(dhps.hallazgo_vs_prog_ser=hvps.codigo) " +
										"    INNER JOIN odontologia.det_plan_tratamiento detpt ON(detpt.codigo_pk=detPlan.codigo_pk) " +
										"    WHERE  detpt.hallazgo=detPlan.hallazgo and dhps.programa=progra.codigo" +
										"  ) as num_superficies, ";
				
										if(!UtilidadTexto.isEmpty(codigoProgramas))
										{
											consultaStr +=" case when progra.codigo in ("+codigoProgramas+") then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as yaCheck, ";
										}
										else
										{
											consultaStr +=" '"+ConstantesBD.acronimoNo+"' as yaCheck, ";
										}
										if(!UtilidadTexto.isEmpty(codigosAsignadosPresupuesto))
										{
											consultaStr +=" case when progra.codigo in ("+codigosAsignadosPresupuesto+") then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as yaSelPresupuesto ";
										}
										else
										{
											consultaStr +=" '"+ConstantesBD.acronimoNo+"' as yaSelPresupuesto ";
										}
				consultaStr+=	"from " +
									"odontologia.presu_plan_tto_prog_ser pptt "+
									"INNER JOIN odontologia.det_plan_tratamiento detPlan on(detPlan.codigo_pk=pptt.det_plan_tratamiento) "+
									"INNER JOIN odontologia.programas progra ON (progra.codigo= pptt.programa) " +
								"where " +
									"detPlan.codigo_pk="+codigoDetalle+" " +
									"AND pptt.presupuesto= "+presupuesto;
			}
		}
		else 
		{
			//FIXME consulta pendiente de realizar
			/*consultaStr+= "select coalesce(getnombreservicio( "+codigoServicio+", "+ConstantesBD.codigoTarifarioCups+"), 'SIN SERVICIO') as nombreServicio " +
							"from 	odontologia.hallazgos_odontologicos hallazgo INNER JOIN " +
								"odontologia.hallazgos_vs_prog_ser  hallaPro ON (hallazgo.consecutivo= hallaPro.hallazgo) " +
								"LEFT OUTER JOIN odontologia.det_hall_prog_ser detHalla ON(detHalla.codigo=hallaPro.codigo) "+
								"LEFT OUTER JOIN facturacion.servicios serv on (detHalla.servicio=serv.codigo)  where serv.codigo="+codigoServicio+" " +
									
								"UNION" +
								
						"select coalesce(getnombreservicio( "+codigoServicio+", "+ConstantesBD.codigoTarifarioCups+"), 'SIN SERVICIO') as nombreServicio " +
							"from odontologia.det_plan_tratamiento detPlan "+ 
							"INNER JOIN  odontologia.programas_servicios_plan_t proServ ON (detPlan.codigo_pk=proServ.det_plan_tratamiento ) " +
							"LEFT OUTER JOIN facturacion.servicios serv ON (serv.codigo=proServ.servicio) " +
							"" +
							"where   detPlan.codigo_pk="+codigoDetalle;*/
		}							
						
		Log4JManager.info("CONSULTA-------------> "+consultaStr);
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoProgramaServicioPlan info= new InfoProgramaServicioPlan();
				
				info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkProgramaServicio"));
				info.setNombreProgramaServicio(rs.getString("nombrePrograma"));
				
				if(!UtilidadTexto.isEmpty(codigoProgramas))
				{
					if(UtilidadTexto.getBoolean(rs.getString("yaCheck")))
					{
						info.setCodigopk(codigoDetalle);
					}
					else
					{
						info.setCodigopk(new BigDecimal(ConstantesBD.codigoNuncaValido));
					}
					
					info.setActivo(UtilidadTexto.getBoolean(rs.getString("yaCheck")));
					info.setAsignadoAlPresupuesto(UtilidadTexto.getBoolean(rs.getString("yaSelPresupuesto")));
				}
				else
				{
					info.setActivo(false);
					info.setCodigopk(new BigDecimal(ConstantesBD.codigoNuncaValido));
				}
				
				info.setNumeroSuperficies(rs.getInt("num_superficies"));
				info.setHallazgoAplicaA(hallazgoAplicaA);
				
				//copia no referencia
				ArrayList<InfoNumSuperficiesPresupuesto> listaCopiada= new ArrayList<InfoNumSuperficiesPresupuesto>();
				for(InfoNumSuperficiesPresupuesto infoSup: superficies)
				{
					listaCopiada.add(infoSup.copiar());
				}
				info.setSuperficiesAplicaPresupuesto(listaCopiada);
				
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		
		return array;
	}
	
	
	/**
	 * 
	 * @param codigoDetallePlanTratamiento
	 * @param utilizaProgramas
	 * @return
	 */
	 public static String obtenerEstadoProgramaServicioPlanTratamiento(BigDecimal codigoDetallePlanTratamiento, boolean utilizaProgramas, double codigoPkProgramaOServicio)
	{
		String resultado="";
		String consultaStr=""; 
		
		if(utilizaProgramas)
		{	
			consultaStr=	"SELECT DISTINCT " +
								"estado_programa " +
							"FROM " +
								"odontologia.programas_servicios_plan_t " +
							"WHERE " +
								"det_plan_tratamiento=? " +
								"AND programa=? " +
								"AND activo='"+ConstantesBD.acronimoSi+"' ";
		}
		else
		{
			consultaStr=	"SELECT DISTINCT " +
								"estado_servicio " +
							"FROM " +
								"odontologia.programas_servicios_plan_t " +
							"WHERE " +
								"det_plan_tratamiento=? " +
								"AND servicio=? " +
								"AND activo='"+ConstantesBD.acronimoSi+"' ";
		}
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, codigoDetallePlanTratamiento);
			ps.setDouble(2, codigoPkProgramaOServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getString(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}
		return resultado;
	}
	
	/**
	 * 
	 * 
	 */
	public static boolean modicarEstadosDetalleProgServ(DtoProgramasServiciosPlanT dto, Connection con)
	{
		
		boolean retorna=false;
		boolean adicionaronFiltros=false;
		
		String consultaStr = "UPDATE odontologia.programas_servicios_plan_t  " +
				" set estado_programa='"+dto.getEstadoPrograma()+"' , estado_servicio ='"+dto.getEstadoServicio()+"' " +
				", usuario_modifica = '"+dto.getUsuarioModifica().getUsuarioModifica()+"' "+   
				", fecha_modifica =  '"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'"+
				", hora_modifica='"+dto.getUsuarioModifica().getHoraModifica()+"'";
				
		if(!UtilidadTexto.isEmpty(dto.getEstadoAutorizacion()))	
		{
			consultaStr+=", estado_autorizacion='"+dto.getEstadoAutorizacion()+"' ";
		}
		if(!UtilidadTexto.isEmpty(dto.getActivo()))
		{
			consultaStr+=", activo='"+dto.getActivo()+"' ";
		}
		//consultaStr+=" where activo='"+ConstantesBD.acronimoSi+"' ";
		boolean puseWhere=false;
		
		if(dto.getDetPlanTratamiento().doubleValue()>0)
		{
			if(!puseWhere)
			{
				consultaStr+=" where ";
				puseWhere=true;
			}
			else
			{
				consultaStr+=" AND ";
			}
			consultaStr+= " det_plan_tratamiento="+dto.getDetPlanTratamiento();
			adicionaronFiltros=true;
		}	
		
		
		if(dto.getCodigoPk().doubleValue()>0)
		{
			if(!puseWhere)
			{
				consultaStr+=" where ";
				puseWhere=true;
			}
			else
			{
				consultaStr+=" AND ";
			}
			consultaStr+=" codigo_pk= "+dto.getCodigoPk();
			adicionaronFiltros=true;
		}
		
		if(!adicionaronFiltros)
		{
			return false;
		}
		
		
		if(dto.getPrograma().getCodigo()>0)
		{
			if(!puseWhere)
			{
				consultaStr+=" where ";
				puseWhere=true;
			}
			else
			{
				consultaStr+=" AND ";
			}
			consultaStr+=" programa= "+dto.getPrograma().getCodigo().intValue();
		}
		else if(dto.getServicio().getCodigo()>0)
		{
			if(!puseWhere)
			{
				consultaStr+=" where ";
				puseWhere=true;
			}
			else
			{
				consultaStr+=" AND ";
			}
			consultaStr+=" servicio= "+dto.getServicio().getCodigo();
		}
		
		if(!UtilidadTexto.isEmpty(dto.getPorConfirmado()))
		{
			if(!puseWhere)
			{
				consultaStr+=" where ";
				puseWhere=true;
			}
			else
			{
				consultaStr+=" AND ";
			}
			consultaStr+=  " por_confirmar='"+dto.getPorConfirmado() +"'";
		}
		
		
			
			try 
			{
				Log4JManager.info("modificar-->"+consultaStr);
				PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				retorna=ps.executeUpdate() >0; 
				ps.close();
				
			} catch (SQLException e) 
			{
				Log4JManager.error("ERROR EN actualizar solo prog ser " , e);
				
			}	
			
			return retorna;
			
	}

	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double guardarDetalle(DtoDetallePlanTratamiento dto , Connection con)
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;

		Log4JManager.info("LA PIEZA  A INSERTAR ES ------------------------------>"+dto.getPiezaDental());
		try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, " odontologia.seq_det_plan_tratamiento");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStrDetPlanTratamiento ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,secuencia );
			ps.setDouble(2, dto.getPlanTratamiento());
			Log4JManager.info("ES ---------------->"+ (dto.getPiezaDental() <= 0));
			if(dto.getPiezaDental() <= 0)
			{
				ps.setNull(3, Types.DECIMAL);
			}
			else
			{
				ps.setInt(3, dto.getPiezaDental());
			}

			if (dto.getSuperficie() > 0)
			{
				ps.setInt(4, dto.getSuperficie());
			}
			else
			{
				ps.setNull(4, Types.DECIMAL);
			}

			ps.setInt(5, dto.getHallazgo());
			ps.setString(6, dto.getSeccion());

			if(!UtilidadTexto.isEmpty(dto.getClasificacion()))
			{	
				ps.setString(7,dto.getClasificacion());
			}
			else
			{
				ps.setNull(7, Types.VARCHAR);
			}

			ps.setString(8, dto.getPorConfirmar() );

			if(dto.getConvencion()<=0)
			{	
				ps.setNull(9,  Types.DECIMAL);
			}
			else
			{
				ps.setInt(9, dto.getConvencion());
			}

			ps.setString(10, dto.getFechaUsuarioModifica().getUsuarioModifica());
			ps.setString(11, dto.getFechaUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(12, dto.getFechaUsuarioModifica().getHoraModifica());
			
			if(dto.getEspecialidad().getCodigo()<=0)
			{	
				ps.setNull(13, Types.DECIMAL );
			}
			else
			{
				ps.setInt(13, dto.getEspecialidad().getCodigo());
			}
			
			ps.setString(14, dto.getActivo());
			
			if(dto.getCodigoCita().doubleValue()<=0)
			{
				ps.setNull(15, Types.NUMERIC); 
			}
			else
			{	
				ps.setBigDecimal(15, dto.getCodigoCita());
			}
			
			if(dto.getValoracion().doubleValue()<=0)
			{
				ps.setNull(16, Types.NUMERIC); 
			}
			else
			{	
				ps.setBigDecimal(16, dto.getValoracion());
			}
			
			if(dto.getEvolucion().doubleValue()<=0)
			{
				ps.setNull(17, Types.NUMERIC); 
			}
			else
			{	
				ps.setBigDecimal(17, dto.getEvolucion());
			}
			
			Log4JManager.info("\n\n\n\n\n DET INSERTAR PLAN DE TRATAMIENTO "+ps+"\n\n\n\n");
			
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			Log4JManager.error("ERROR en insert " , e);
			secuencia= ConstantesBD.codigoNuncaValidoDouble;
		}
		return secuencia;
	}


	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double  guardarProgramasServicio(DtoProgramasServiciosPlanT dto , Connection con )
	{

		Log4JManager.info("*****************************************************************************"); 
		Log4JManager.info("---------------------------------SQL BASE PROGRAMA SERVICIO-----------------");
		Log4JManager.info("Log4JManager---");
		Log4JManager.info(" \n Por Confirmar >"+dto.getPorConfirmado());
		//Log4JManager.info(UtilidadLog.obtenerStringHerencia(dto , true));
   
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		int tmpCodigoEspecialidad=cargarEspecialidadCita(Utilidades.convertirAEntero(dto.getCodigoCita()+""));
		try 
		{
			//Objetos de Conexion

			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_programas_servicios_plan_t"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator (con, InsertarStrProgromasServicioPlan);

			ps.setDouble(1,secuencia );
			ps.setBigDecimal(2, dto.getDetPlanTratamiento());

			if(dto.getPrograma().getCodigo()>0){
				
				ps.setDouble(3, dto.getPrograma().getCodigo());	
			
			}else{
				
				ps.setNull(3, Types.NUMERIC);
			}
			
			ps.setInt(4, dto.getServicio().getCodigo());

			if(!UtilidadTexto.isEmpty(dto.getEstadoPrograma()))
			{
				ps.setString(5, dto.getEstadoPrograma());
			}
			else
			{
				ps.setNull(5, Types.VARCHAR);
			}

			if(dto.getMotivo().getCodigo()>0)
			{
				ps.setInt(6, dto.getMotivo().getCodigo());
			}

			else
			{
				ps.setNull(6, Types.INTEGER);
			}


			if(dto.getConvencion()>0)
			{
				ps.setInt(7, dto.getConvencion());
			}
			else
			{
				ps.setNull(7, Types.INTEGER);
			}


			if(!UtilidadTexto.isEmpty(dto.getInclusion()))
			{
				ps.setString(8, dto.getInclusion());
			}
			else
			{
				ps.setNull(8, Types.VARCHAR);
			}

			if(!UtilidadTexto.isEmpty(dto.getExclusion()))
			{
				ps.setString(9, dto.getExclusion());
			}
			else
			{
				ps.setNull(9, Types.VARCHAR);	
			}

			if(!UtilidadTexto.isEmpty(dto.getGarantia()))
			{
				ps.setString(10, dto.getGarantia());
			}
			else
			{
				ps.setNull(10, Types.VARCHAR);
			}

			ps.setString(11, dto.getEstadoServicio());
			ps.setString(12, dto.getIndicativoPrograma());
			ps.setString(13, dto.getIndicativoServicio());
			ps.setString(14, dto.getPorConfirmado());
			ps.setString(15, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(16, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(17, dto.getUsuarioModifica().getHoraModifica());
			
			if(dto.getEspecialidad().getCodigo()>0)
			{	
				ps.setInt(18, dto.getEspecialidad().getCodigo());
			}
			else
			{
				if(tmpCodigoEspecialidad>0)
				{ 
					ps.setInt(18,tmpCodigoEspecialidad);
				}else
				{				
				  ps.setNull(18, Types.INTEGER);
				}
			}

			ps.setInt(19, dto.getOrdenServicio());
			ps.setString(20,dto.getActivo()); 

			if(dto.getCodigoCita().intValue() > 0)
			{	
				ps.setBigDecimal(21,dto.getCodigoCita());
			}	
			else
			{	
				ps.setNull(21,Types.INTEGER);
			}	

			if(dto.getValoracion().intValue() > 0)
			{	
				ps.setBigDecimal(22,dto.getValoracion());
			}	
			else
			{	
				ps.setNull(22,Types.INTEGER);
			}	
			
			if(dto.getEvolucion().intValue() > 0)
			{	
				ps.setBigDecimal(23,dto.getEvolucion());
			}	
			else
			{	
				ps.setNull(23,Types.INTEGER);
			}	

			if(!UtilidadTexto.isEmpty( dto.getEstadoAutorizacion()))
			{
				ps.setString(24, dto.getEstadoAutorizacion());
			}
			else
			{
				ps.setNull(24, Types.VARCHAR);
			}
			Log4JManager.info("\n\n Cadena Insercion Programa >> "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return secuencia;
			}	
			ps.close();
		}

		catch (SQLException e) 
		{
			e.printStackTrace();
			Log4JManager.error("ERROR en insert ", e);
		}
		return ConstantesBD.codigoNuncaValidoDouble;

	}
	
	 /**
	  * 
	  * @param dto
	  * @return
	  */
	public  static ArrayList<DtoProgramasServiciosPlanT> cargarProgramasServiciosPlanT(DtoProgramasServiciosPlanT dto)
	{
		
		ArrayList<DtoProgramasServiciosPlanT> array = new ArrayList<DtoProgramasServiciosPlanT>();
			
		String consultarStrPrograma=" select 	codigo_pk  as codigoPk,"+
										" plantPS.det_plan_tratamiento as detPlanTratamiento,"+
										" plantPS.programa as programa ,"+
										" plantPS.servicio as servicio,"+
										" plantPS.estado_programa as estadoProgrma,"+
										" plantPS.motivo as motivo,"+              
										" plantPS.convencion as convencion,"+
										" plantPS.inclusion as inclusion,"+         
										" plantPS.exclusion  as exclusion,"+         
										" plantPS.garantia   as  garantia,"+     
										" plantPS.estado_servicio as    estado_servicio,"+
										" plantPS.indicativo_programa as indicativo_programa,"+
										" plantPS.indicativo_servicio as indicativo_servicio,"+
										" plantPS.por_confirmar    as    por_confirmar,"+
										" plantPS.usuario_modifica  as   usuario_modifica,"+
										" plantPS.fecha_modifica    as  fecha_modifica,"+ 
										" plantPS.hora_modifica   as  hora_modifica,"+   
										" plantPS.especialidad  as  especialidad, " +
										" plantPS.orden_servicio as orden, " +
										" plantPS.activo as activo, " +
										" plantPS.estado_autorizacion as estado_autorizacion " +        
										" from "+
										"odontologia.programas_servicios_plan_t plantPS " +
										"" +
										"where 1=1 ";
		
		if(dto.getCodigoPk().doubleValue()>0)
		{	
			consultarStrPrograma+=" AND codigo_pk="+dto.getCodigoPk();
		}
		if(dto.getDetPlanTratamiento().doubleValue()>0)
		{
			consultarStrPrograma+=" AND plantPS.det_plan_tratamiento="+dto.getDetPlanTratamiento();
		}
		if(!UtilidadTexto.isEmpty(dto.getPorConfirmado()))
		{
			consultarStrPrograma+=" AND plantPS.por_confirmar='"+UtilidadTexto.convertirSN(dto.getPorConfirmado())+"' ";
		}
		if(!UtilidadTexto.isEmpty(dto.getActivo()))
		{
			consultarStrPrograma+=" AND plantPS.activo='"+UtilidadTexto.convertirSN(dto.getActivo())+"' ";
		}
		if(dto.getPrograma().getCodigo()>0)
		{
			consultarStrPrograma+=" AND plantPS.programa="+dto.getPrograma().getCodigo()+" ";
		}
		else if(dto.getServicio().getCodigo()>0)
		{
			consultarStrPrograma+=" AND plantPS.servicio="+dto.getServicio().getCodigo()+" ";
		}
		
		Log4JManager.info("consultarStrPrograma-->"+consultarStrPrograma);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarStrPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoProgramasServiciosPlanT dt = new DtoProgramasServiciosPlanT();
				dt.setCodigoPk(rs.getBigDecimal("codigoPk"));
				dt.setDetPlanTratamiento(rs.getBigDecimal("detPlanTratamiento"));
				dt.getPrograma().setCodigo(rs.getDouble("programa"));
				dt.getServicio().setCodigo(rs.getInt("servicio"));
				dt.setEstadoPrograma(rs.getString("estadoProgrma"));
				dt.getMotivo().setCodigo(rs.getInt("motivo"));
				dt.setConvencion(rs.getInt("convencion"));
				dt.setInclusion(rs.getString("inclusion"));
				dt.setExclusion(rs.getString("exclusion"));
				dt.setGarantia(rs.getString("garantia"));
				dt.setEstadoServicio(rs.getString("estado_servicio"));
				dt.setIndicativoPrograma(rs.getString("indicativo_programa"));
				dt.setIndicativoServicio(rs.getString("indicativo_servicio"));
				dt.setPorConfirmado(rs.getString("por_confirmar"));
				dt.getUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				dt.getUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
				dt.getUsuarioModifica().setFechaModifica(rs.getString("fecha_modifica"));
				dt.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad"), ""));
				dt.setOrdenServicio(rs.getInt("orden"));
				dto.setActivo(rs.getString("activo"));
				dto.setEstadoAutorizacion(rs.getString("estado_autorizacion"));
				array.add(dt);
				
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			Log4JManager.error("ERROR en insert " + e);
		}
				
					
		return array;
	}
	 
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param estadosAEliminar
	 * @param utilizaProgramas
	 * @return
	 */
	public static boolean inactivarProgramasServicios(DtoProgramasServiciosPlanT dto, Connection con, ArrayList<String> estadosAEliminar, boolean utilizaProgramas )
	{		
		boolean retorna=false;
		String consultaStr= "update odontologia.programas_servicios_plan_t set activo='"+ConstantesBD.acronimoNo+"'  WHERE 1=1 AND det_plan_tratamiento="+dto.getDetPlanTratamiento()+" ";
		
		consultaStr+=(utilizaProgramas)?" AND programa="+dto.getPrograma().getCodigo().intValue()+" ":" AND servicio="+dto.getServicio().getCodigo()+" ";
		
		if(estadosAEliminar.size()>0)
		{
			consultaStr+=(utilizaProgramas)?" AND estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosAEliminar)+") " : " AND estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosAEliminar)+") ";
		}
		
		if(!UtilidadTexto.isEmpty(dto.getPorConfirmado()))
		{
			consultaStr+=" and por_confirmar ='"+UtilidadTexto.convertirSN(dto.getPorConfirmado())+"' ";
		}
		
		consultaStr+=" AND activo= '"+ConstantesBD.acronimoSi+"' ";
		
		Log4JManager.info("**************************************************************************************");
		Log4JManager.info("*****************************	INACTIVAR PROGRAMAS SERVICIO ***************************");
		Log4JManager.info("\n SQL INACTIVAR "+consultaStr);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna= ps.executeUpdate()>0;
			Log4JManager.info("INACTIVAR "+consultaStr);
			ps.close();
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR AL INACTIVAR EN PLAN TRATAMIENTO  ", e);
			e.printStackTrace();
		}
		return retorna;
	 }
	
	/**
	 * @param DtoDetallePlanTratamiento dto
	 * */
	public static double guardarDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto)
	{
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_plan_tratamiento");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStrDetPlanTratamiento);
			
			ps.setDouble(1,codigo_pk);
			ps.setDouble(2,dto.getPlanTratamiento());
			
			if(dto.getPiezaDental() > 0)
				ps.setInt(3,dto.getPiezaDental());
			else
				ps.setNull(3,Types.INTEGER);
			
			if(dto.getSuperficie() > 0)
				ps.setInt(4,dto.getSuperficie());
			else
				ps.setNull(4,Types.INTEGER);
			
			ps.setInt(5,dto.getHallazgo());
			ps.setString(6,dto.getSeccion());
			
			if(!dto.getClasificacion().equals(""))
				ps.setString(7,dto.getClasificacion());
			else
				ps.setNull(7,Types.VARCHAR);
			
			ps.setString(8,dto.getPorConfirmar());
			
			if(dto.getConvencion() > 0)
				ps.setInt(9,dto.getConvencion());
			else
				ps.setNull(9,Types.INTEGER);
			
			ps.setString(10,dto.getFechaUsuarioModifica().getUsuarioModifica());
			ps.setDate(11,Date.valueOf(dto.getFechaUsuarioModifica().getFechaModificaFromatoBD()));
			ps.setString(12,dto.getFechaUsuarioModifica().getHoraModifica());
			
			if(dto.getEspecialidad().getCodigo() > 0)
				ps.setInt(13,dto.getEspecialidad().getCodigo());
			else
				ps.setNull(13,Types.INTEGER);
			
			ps.setString(14,dto.getActivo());
			
			if(dto.getCodigoCita().doubleValue() > 0)
				ps.setBigDecimal(15,dto.getCodigoCita());
			else
				ps.setNull(15,Types.INTEGER);
			
			if(dto.getValoracion().doubleValue() > 0)
				ps.setBigDecimal(16,dto.getValoracion());
			else
				ps.setNull(16,Types.INTEGER);
			
			if(dto.getEvolucion().doubleValue() > 0)
				ps.setBigDecimal(17,dto.getEvolucion());
			else
				ps.setNull(17,Types.INTEGER);

			Log4JManager.info(ps);
			
			if(ps.executeUpdate()>0){
			ps.close();
				return codigo_pk;
			}
			ps.close();
		}
		catch (SQLException e) {
			Log4JManager.info("error >> "+e.toString()+" "+UtilidadLog.obtenerString(dto,true));
		}
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	/**
	 * @param DtoPlanTratamientoOdo dto
	 * */
	public static double guardarPlanTratamiento(Connection con,DtoPlanTratamientoOdo dto)
	{
		
		Log4JManager.info("***********************************************CREAR PLAN DE TRATAMIENTO******************************** ");
		Log4JManager.info("********************************************************************************************************");
		
		Log4JManager.info(" 1 Sql \n\n\n\n"+InsertarStrPlanTratamientoOdo+"\n\n\n\n");
		
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		double consecutivo = ConstantesBD.codigoNuncaValidoDouble;
        Log4JManager.info("MOTIVO ------------------>"+ dto.getMotivo());		
		try
		{
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_plan_tratamiento");
			consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_plan_tratamiento_con");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, InsertarStrPlanTratamientoOdo);
			
			ps.setDouble(1,codigo_pk);
			ps.setDouble(2,consecutivo);
			ps.setInt(3,dto.getCodigoPaciente());
			ps.setInt(4,dto.getIngreso());
			
			if(dto.getEspecialidad().getCodigo() > 0)
				ps.setInt(5,dto.getEspecialidad().getCodigo());
			else
				ps.setNull(5,Types.NUMERIC);
			
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGrabacion())));
			ps.setString(7,dto.getHoraGrabacion());
			ps.setString(8,dto.getUsuarioGrabacion());
			
			if(dto.getOdontogramaDiagnostico().doubleValue() > 0)
				ps.setBigDecimal(9,dto.getOdontogramaDiagnostico());
			else
				ps.setNull(9,Types.NUMERIC);
			
			ps.setString(10,dto.getEstado());
			
			if(dto.getMotivo() > 0)
				ps.setInt(11,dto.getMotivo());
			else
				ps.setNull(11,Types.INTEGER);
			
			if(dto.getOdontogramaEvolucion().doubleValue() > 0)
				ps.setBigDecimal(12,dto.getOdontogramaEvolucion());
			else
				ps.setNull(12,Types.NUMERIC);
			
			ps.setString(13,dto.getIndicativo());
			ps.setString(14,dto.getPorConfirmar());
			ps.setInt(15,dto.getInstitucion());
			ps.setInt(16,dto.getCentroAtencion());
			ps.setString(17,dto.getUsuarioModifica().getUsuarioModifica());
			ps.setDate(18,Date.valueOf(dto.getUsuarioModifica().getFechaModificaFromatoBD()));
			ps.setString(19,dto.getUsuarioModifica().getHoraModifica());
			
			if(dto.getCodigoValoracion().doubleValue() > 0)
				ps.setBigDecimal(20,dto.getCodigoValoracion());
			else
				ps.setNull(20,Types.NUMERIC);
			
			if(dto.getCodigoEvolucion().doubleValue() > 0)
				ps.setBigDecimal(21,dto.getCodigoEvolucion());
			else
				ps.setNull(21,Types.NUMERIC);
			
			Log4JManager.info("GUARDAR PLAN TRATAMIENTO "+ps);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return codigo_pk;
			}
			
			ps.close();
		}
		catch (SQLException  e) {
			Log4JManager.info(e.toString()+" "+UtilidadLog.obtenerString(dto,true));
		}
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	/**
 	 * Realiza la inserci&oacute;n del odontograma
	 * @param {@link DtoPlanTratamientoOdo} Objeto con la informaci&oacute;n del odontograma 
	 * @return codigo_pk Consecutivo de la inserci&oacute;n, -1 en caso de error. 
	 */
	public static double guardarOdontograma(Connection con,DtoOdontograma dto)
	{
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		double consecutivo = ConstantesBD.codigoNuncaValidoDouble;

		String insertarStrOdontograma = "INSERT INTO odontologia.odontograma ( " +
		  "codigo_pk," +//1
		  "consecutivo," +//2
		  "codigo_paciente," +//3
		  "ingreso," +//4
		  "valoracion," +//5
		  "indicativo," +//6
		  "evolucion," +//7
		  "institucion," +//8
		  "centro_atencion," +//9
		  "usuario_modifica," +//10
		  "fecha_modifica," +//11
		  "hora_modifica," +//12
		  "imagen " +//13
		  ") " +
		  "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		PreparedStatementDecorator ps = new PreparedStatementDecorator(con, insertarStrOdontograma);

		try
		{	
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_odontograma");
			consecutivo=codigo_pk; 
			//consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_odontograma_con");




			ps.setDouble(1,codigo_pk);
			ps.setDouble(2,consecutivo);
			ps.setInt(3,dto.getCodigoPaciente());
			ps.setInt(4,dto.getIngreso().getCodigo());
			
			if(dto.getValoracion() > 0)
			{
				ps.setInt(5,dto.getValoracion());
			}
			else
			{
				ps.setNull(5,Types.INTEGER);
			}
			
			ps.setString(6,dto.getIndicativo());
			
			if(dto.getEvolucion() > 0)
			{
				ps.setDouble(7,dto.getEvolucion());
			}
			else
			{
				ps.setNull(7,Types.NUMERIC);
			}
			
			ps.setInt(8,dto.getInstitucion());
			ps.setInt(9,dto.getCentroAtencion().getCodigo());
			ps.setString(10,dto.getUsuarioModifica().getUsuarioModifica());
			ps.setDate(11,Date.valueOf(dto.getUsuarioModifica().getFechaModificaFromatoBD()));
			ps.setString(12,dto.getUsuarioModifica().getHoraModifica());
			ps.setString(13,dto.getImagen());
					
			Log4JManager.info(ps);
			
			int resultado=ps.executeUpdate();
			
			if(resultado>0)
			{
				ps.cerrarPreparedStatement();
				if(dto.getHistorico()!=null)
				{
					String insertatHistopricoOdontograma=
						"INSERT INTO odontologia.datos_histo_odontograma " +
						"(" +
							"odontograma, "+             
							"codigo_plan_tratamiento, "+
							"estado_plan_tratamiento, "+
							"codigo_presupuesto, "+
							"estado_presupuesto, "+
							"fecha_generacion_presupuesto, "+
							"hora)" +
						"VALUES(?,?,?,?,?,CURRENT_DATE,?)";
					ps=new PreparedStatementDecorator(con, insertatHistopricoOdontograma);
					ps.setDouble(1, codigo_pk);
					ps.setBigDecimal(2, dto.getHistorico().getCodigoPlanTratamiento());
					ps.setString(3, dto.getHistorico().getEstadoPlanTratamiento());
					ps.setBigDecimal(4, dto.getHistorico().getCodigoPresupuesto());
					ps.setString(5, dto.getHistorico().getEstadoPresupuesto());
					ps.setString(6, UtilidadFecha.getHoraActual());
					resultado=ps.executeUpdate();
					ps.cerrarPreparedStatement();
					if(resultado>0)
					{
						return codigo_pk;
					}
				}
			}
			
			ps.cerrarPreparedStatement();
		}
		catch (SQLException  e) {
			Log4JManager.info("Error insertando los datos del odontograma.",e);
		}
		finally{
			ps.cerrarPreparedStatement();
		}
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}

	/**
	* Mï¿½todo para cargar el odontograma
	* @param esValoracion
	* @param valoracion
	* */
	public static DtoOdontograma cargarOdontograma(BigDecimal codigoPk, boolean esValoracion)
	{
		DtoOdontograma dto=new DtoOdontograma();
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			
			String cargarOdontogramaStr = "SELECT " +
						"o.codigo_pk AS codigo_pk, " +
						"o.consecutivo AS consecutivo, " +
						"o.codigo_paciente As codigo_paciente, " +
						"o.ingreso As ingreso, " +
						"o.valoracion AS valoracion, " +
						"o.indicativo AS indicativo, " +
						"o.evolucion AS evolucion, " +
						"o.institucion AS institucion, " +
						"o.centro_atencion AS centro_atencion, " +
						"o.usuario_modifica AS usuario_modifica, " +
						"o.fecha_modifica AS fecha_modifica, " +
						"o.hora_modifica AS hora_modifica, " +
						"o.imagen AS imagen " +
					"FROM odontologia.odontograma o " +
					"WHERE ";
			if(esValoracion)
			{
				cargarOdontogramaStr+="o.valoracion=? ";
			}
			else
			{
				cargarOdontogramaStr+="o.evolucion=? ";
			}
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,cargarOdontogramaStr);
			
			ps.setBigDecimal(1,codigoPk);
			
			Log4JManager.info("La consulta del Odontograma "+ps+"\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setConsecutivo(rs.getDouble("consecutivo"));
	
				dto.setCodigoPaciente(rs.getInt("codigo_paciente"));
	
				dto.setIngreso(new InfoDatosInt(rs.getInt("ingreso")));
	
				dto.setValoracion(rs.getInt("valoracion"));
				dto.setIndicativo(rs.getString("indicativo"));
				dto.setEvolucion(rs.getDouble("evolucion"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion")));
				dto.setUsuarioModifica(new DtoInfoFechaUsuario(rs.getString("hora_modifica"), rs.getString("fecha_modifica"), rs.getString("usuario_modifica")));
				dto.setImagen(rs.getString("imagen"));
			}			
			ps.close();
			rs.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException  e) {
			Log4JManager.info("Error consultando el odontograma para la valoraciï¿½n "+codigoPk,e);
		}
		
		return dto;
	}

	

	/**
	 * 
	 * @param codigoPrograma
	 * @param codigoDetPlan
	 * @return
	 */
	public static int cargarOrdenServicio(int codigoPrograma, BigDecimal codigoDetPlan , Connection con, boolean inclusion){
		  	int ordenServcioBD= 1;
		  	String consultaStr="";
		  	if(!inclusion)
		  		consultaStr="select coalesce(max(orden_servicio),1) as orden from odontologia.programas_servicios_plan_t  where programa="+codigoPrograma  +" and det_plan_tratamiento="+codigoDetPlan;
		  	else
		  		consultaStr="select (coalesce(max(orden_servicio),0))+1 as orden from odontologia.programas_servicios_plan_t  where programa="+codigoPrograma  +" and det_plan_tratamiento="+codigoDetPlan;
		  	Log4JManager.info("CONSULTA ORDEN -------->"+consultaStr);
		  
		  	try 
			{
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					ordenServcioBD= rs.getInt("orden");
				}
				ps.close();
				rs.close();
				
			}
			catch (SQLException e) 
			{
				Log4JManager.error("error en carga==> "+e);
			}		
		Log4JManager.info("ORDEN --------------------->"+ordenServcioBD);
		return ordenServcioBD;
		
	}
	
	
	//****************************************************************************************************************************
	//************************************** ANEXO 880 PLAN DE TRATAMIENTO *******************************************************
	//*****************************************      ANDRES ORTIZ       **********************************************************

	/**
		* Metodo para consultar el plan de tratamiendo asociado a un ingreso
		* @param con
		* @param codigoPaciente
		* @return
		*/
		public static DtoPlanTratamientoOdo consultarPlanTratamiento(Connection con, int codigoPaciente)
		{
			DtoPlanTratamientoOdo dtoTratamiento = new DtoPlanTratamientoOdo();
			
			String consulta = consultarPlandeTratamientoStr;
			
			try{
				Log4JManager.info("Consulta: "+consulta);
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigoPaciente);
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					dtoTratamiento.setCodigoPk(rs.getBigDecimal("codigopk"));
					dtoTratamiento.setConsecutivo(rs.getBigDecimal("consecutivo"));
					dtoTratamiento.setCodigoPaciente(rs.getInt("codpaciente"));
					dtoTratamiento.setIngreso(rs.getInt("ingreso"));
					dtoTratamiento.setFechaGrabacion(rs.getString("fechagrabacion"));
					dtoTratamiento.setHoraGrabacion(rs.getString("horagrabacion"));
					dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
					dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
					dtoTratamiento.setEstado(rs.getString("estado"));
					dtoTratamiento.setPorConfirmar(rs.getString("porconfirmar"));
					dtoTratamiento.setInstitucion(rs.getInt("institucion"));
					dtoTratamiento.setCentroAtencion(rs.getInt("centroatencion"));
				}
				ps.close();
				rs.close();
				
			}catch (Exception e) {
				e.printStackTrace();
				Log4JManager.info("Consulta: "+consulta);
			}		
			
			return dtoTratamiento;
		}
		
		
		
		/**
		* Metodo para confirmar en el Log de Plan de Tratamiento si el campo por_confirmar esta en NO
		* @param codigoPlanTratamiento
		* @return
		*/
		public static boolean confirmacionenLogPlanTratamiento(int codigoPlanTratamiento)
		{
		   boolean confirmacion= false;
		   String consulta = consultaLogPlanTratamiento;
		   Connection con = null;
			con = UtilidadBD.abrirConexion();
			try{
				Log4JManager.info("Consulta: "+consulta);
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigoPlanTratamiento);
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{			
					if(rs.getString("porconfirmar").equals(ConstantesBD.acronimoNo))
					{
						confirmacion= true;
					}
				}
				
				
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
				
			}catch (Exception e) {
				e.printStackTrace();
				Log4JManager.info("Consulta: "+consulta);
			}	
		   
			
		   
			return confirmacion;
		}
	
	/**
	 * Metodo para consultar el ultimo registro en el Log de un Plan de Tratamiento
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static DtoLogPlanTratamiento consultarLogTratamiento(int codigoPlanTratamiento)
	{
			DtoLogPlanTratamiento dtoLog= new DtoLogPlanTratamiento();
			String consulta = consultaLogPlanTratamiento;
		   Connection con = null;
			con = UtilidadBD.abrirConexion();
			try{
				Log4JManager.info("Consulta Log Tratamiento: "+consulta);
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigoPlanTratamiento);
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{	
					dtoLog.setCodigoPk(rs.getDouble("codigopk"));
					dtoLog.setCodigoMedico(new InfoDatosInt(rs.getInt("codigomedico"), ""));
					dtoLog.setPorConfirmar(rs.getString("porconfirmar"));
					DtoInfoFechaUsuario modificacion = new DtoInfoFechaUsuario();
					modificacion.setFechaModifica(rs.getString("fecha"));
					modificacion.setHoraModifica(rs.getString("hora"));		
					
				}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);	
				
			}catch (Exception e) {
				e.printStackTrace();
				Log4JManager.info("Consulta: "+consulta);
			}	
		   
			
		
		
	    return dtoLog;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @param dtoNuevo
	 * @param con
	 * @return
	 */
	
	public static boolean modificar(DtoPlanTratamientoOdo dtoWhere , DtoPlanTratamientoOdo dtoNuevo , Connection con)
	{
		
		boolean retorna=false;
		// CAmbiar al tener un caragar implemenetado en esta clase 
		//dtoNuevo.setEstado(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente);
		String consultaStr = "UPDATE odontologia.plan_tratamiento set codigo_pk=codigo_pk "; 
		
		int tmpCodigoEspecialidad=0;
		
		if(dtoNuevo.getCodigoCita().intValue()>0)
		{
			 tmpCodigoEspecialidad=cargarEspecialidadCita(dtoNuevo.getCodigoCita().intValue());
		}
		
		if(!UtilidadTexto.isEmpty(dtoNuevo.getEstado()))
		{
			consultaStr+= ", estado='"+dtoNuevo.getEstado()+"'";
		}
		
		if(dtoNuevo.getMotivo()>0)
		{	
			consultaStr+=" , motivo = "+dtoNuevo.getMotivo()+" ";
		}
		
		if(!dtoNuevo.getUsuarioModifica().getUsuarioModifica().equals(""))
		{
			consultaStr+= " ,usuario_modifica = '"+dtoNuevo.getUsuarioModifica().getUsuarioModifica()+"', fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ";
		}
		
		if(dtoNuevo.getCodigoCita().intValue()>0)
		{
			consultaStr += " , cita = "+dtoNuevo.getCodigoCita().intValue();
		}
		
		if(dtoNuevo.getCodigoValoracion().intValue()>0)
		{
			consultaStr += " , valoracion = "+dtoNuevo.getCodigoValoracion().intValue();
		}
		
		if(dtoNuevo.getCodigoEvolucion().intValue()>0)
		{
			consultaStr += " , evolucion = "+dtoNuevo.getCodigoEvolucion().intValue();
		}
		if(dtoNuevo.getOdontogramaDiagnostico().doubleValue()>0)
		{
			consultaStr+=" , odontograma_diagnostico="+dtoNuevo.getOdontogramaDiagnostico();
		}
			
		if(!dtoNuevo.getPorConfirmar().equals(""))
		{
			consultaStr += " , por_confirmar = '"+dtoNuevo.getPorConfirmar()+"' ";
		}
		if(dtoNuevo.getEspecialidad().getCodigo()>0)
		{
			consultaStr += " , especialidad = "+dtoNuevo.getEspecialidad().getCodigo()+" ";
		}
		else if(tmpCodigoEspecialidad>0)
		{
			consultaStr += " , especialidad = "+tmpCodigoEspecialidad+" ";
		}
		
		//SEGURIDAD 
		if(dtoNuevo.getCodigoPk().doubleValue()<=0)
		{
			return Boolean.FALSE;
		}
		
		consultaStr+= " where codigo_pk="+dtoNuevo.getCodigoPk();
		
			
		try 
		{	
				Log4JManager.info(" modificar 			PLAN TRATAMIENTO--> \n\n\n\n"+consultaStr+"\n\n\n\n");
				PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				retorna=ps.executeUpdate() >0; 
				ps.close();
				
				
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN actualizar encabezado PLANNNNNNNNNNNNNNN " , e);
				
		}	
		
		return retorna;
			
	}
	
	 
	/**
	* 
	* @param codigo
	* @return
	*/
	public static String cargarEstadoPlanTratamiento(BigDecimal codigo)
	{
			
		String estado="";	
		
		String consultaStr=" select  pt.estado as estado from odontologia.plan_tratamiento pt where pt.codigo_pk="+codigo;
			try 
			{
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			    if(rs.next())
				{
			        	   
					estado= rs.getString("estado");
				    	
				}
			    
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (SQLException e) 
			{
				Log4JManager.error("error en carga==> "+e);
			}		
		return estado;
				
	}
	
	/**
	 * Carga si el plan de tratamiento es migrado o no
	 * @param codigo Código del plan de tratamiento 
	 * @return S en caso de ser migrado, N de lo contrario
	 * @author Luis Fernando Hincapié
	 * @since 12 Enero 2011
	 */
	public static String cargarMigradoPlanTratamiento(BigDecimal codigo) {

		String migrado = "";

		String consultaStr = " select pt.migrado as migrado from odontologia.plan_tratamiento pt where pt.codigo_pk="
				+ codigo;
		try {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con
					.prepareStatement(consultaStr, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));

			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next()) {
				migrado = rs.getString("migrado");
			}

			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} catch (SQLException e) {
			Log4JManager.error("error en carga==> " + e);
		}

		return migrado;

	}
	
	public static String  historicoAntecedentesOdontologicos()
	{
		String consulta= consultarHistAntecedenteOdontologia;
		
		return consulta;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * METODO PARA CARGAR LA ULTIMA FECHA DE EVOLUCION 
	 */
	public static String  cargarFechaLogPlant(DtoPlanTratamientoOdo dto )
	{
		String consulta="select max(fecha) as fecha from odontologia.log_plan_tratamiento where plan_tratamiento=? and evolucion=null";
		String fecha="";
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con ,consulta);
			ps.setBigDecimal(1, dto.getCodigoPk());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
			{
		   	fecha= rs.getString("fecha");
			}
		    SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
	return fecha;
		
	}
	
	/**
	 * se consulta los los codigos del plan de tratamiento segun el ArrayList de estado y el codigo del paciente
	 * @param estados
	 * @return
	 */
	public static ArrayList<BigDecimal> obtenerCodigoPlanTratamiento (int codigoPaciente, ArrayList<String> estados, String porConfirmar) 
	{
		ArrayList<BigDecimal> array = new ArrayList<BigDecimal>();
		try 
		{
			String consultaStr=" select " +
					"coalesce(codigo_pk, "+ConstantesBD.codigoNuncaValido+") as codigoPk " +
					"from odontologia.plan_tratamiento " +
					"where codigo_paciente="+codigoPaciente+" ";
			
			if(porConfirmar!=null && !porConfirmar.equals(""))
			{
				consultaStr += "and por_confirmar = '"+porConfirmar+"' "; 
			}
			
			if(estados!= null && estados.size() > 0)
			{
				consultaStr += " and estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
			}
			
			Log4JManager.info("obtenerUltimoCodigoPlanTratamiento--->"+consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				BigDecimal codigo = new BigDecimal(rs.getInt("codigoPk"));
				array.add(codigo);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return array;
	}

	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param seccion
	 * @param porConfirmar
	 * @param activo
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezas(BigDecimal codigoPkPlanTratamiento, String seccion, String porConfirmar, String activo)
	{
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		try 
		{
			String consultaStr="SELECT DISTINCT " +
									"d.pieza_dental, " +
									"p.descripcion " +
								"FROM " +
									"odontologia.det_plan_tratamiento d " +
									"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=d.pieza_dental) " +
								"WHERE " +
									"plan_tratamiento="+codigoPkPlanTratamiento+" " +
									"and seccion= '"+seccion+"' ";
			
			if(!activo.equals(""))
				consultaStr+="and activo = '"+activo+"' ";

			if(!porConfirmar.equals(""))
				consultaStr+="and por_confirmar = '"+porConfirmar+"' ";
				
			Log4JManager.info("obtenerPiezas--->"+consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt(1));
				info.setNombre(rs.getString(2));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	/**
	 * 
	 * @param bigDecimal
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @param activo 
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento, 
			ArrayList<String> estadosProgramasOservicios, 
			boolean utilizaProgramas, 
			String porConfirmar,
			String activo) 
	{
		Log4JManager.info("\nCARGANDO PROGRAMAS O SERVICIOS-----------------------");
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		String fechaIni = "";
		String fechaFin = "";
		String fecha = "";
		BigDecimal aux = new BigDecimal(ConstantesBD.codigoNuncaValido); 
		try 
		{
			String consultaStr= (utilizaProgramas)?		"SELECT DISTINCT " +
															"plant.codigo_pk as codigo_pk_progservplant, " +
															"pro.codigo as codigo, " +
															"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
															"pro.codigo_programa as codigoamostrar, " +
															"pro.fecha_modifica as fecha_mod, " +
															"coalesce(esp.nombre,' ') as nombre_especialidad, " +
															"coalesce(esp.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_esp "+
														"FROM " +
															"odontologia.programas_servicios_plan_t plant " +
															"LEFT OUTER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
															"LEFT OUTER JOIN administracion.especialidades esp ON (esp.codigo = plant.especialidad ) " +
														"WHERE " +
															"plant.det_plan_tratamiento="+detallePlanTratamiento+" " +
															"AND plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+") " +
															"AND plant.por_confirmar = '"+porConfirmar+"' " +
															"AND plant.activo = '"+activo+"' " +
														 "group by pro.codigo, pro.codigo_programa, pro.nombre, " +
														 "plant.codigo_pk, pro.fecha_modifica, esp.nombre, esp.codigo " +
														 "order by pro.codigo ASC "
													:  "SELECT DISTINCT " +
															"plant.codigo_pk as codigo_pk_progservplant, " +
															"plant.servicio as codigo ," +
															"getnombreservicio(plant.servicio, "+ConstantesBD.codigoTarifarioCups+")  as nombre, " +
															"getcodigoservicio(plant.servicio, "+ConstantesBD.codigoTarifarioCups+") as codigoamostrar, "+
															"plant.fecha_modifica as fecha_mod, " +
															"coalesce(esp.nombre,' ') as nombre_especialidad, " +
															"coalesce(esp.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_esp "+
														"FROM " +
															"odontologia.programas_servicios_plan_t  plant " +
															"LEFT OUTER JOIN administracion.especialidades esp ON (esp.codigo = plant.especialidad ) " +
														"WHERE " +
															"plant.det_plan_tratamiento="+detallePlanTratamiento+" " +
															"AND plant.estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+") " +
															"AND plant.por_confirmar = '"+porConfirmar+"' " +
															"AND plant.activo = '"+activo+"' ";
															
			Log4JManager.info("CONSULTA PROGRAMAS -->"+consultaStr);												
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				info.setCodigopk(rs.getBigDecimal("codigo_pk_progservplant"));
				info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
				info.setNombreProgramaServicio(rs.getString("nombre"));
				info.setCodigoAmostrar(rs.getString("codigoamostrar"));
				fecha = UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_mod"));
				if(array.size()>0)
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fecha, fechaIni))
						fechaIni = fecha;
					else
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin, fecha))
							fechaFin = fecha;
				}else{
					fechaIni = fecha;
					fechaFin = fecha;
				}
				info.setFechaInicio(fechaIni);
				info.setFechaFinal(fechaFin);
				info.setEspecialidad(rs.getString("nombre_especialidad"));
				info.setCodigoEspecialidad(rs.getInt("cod_esp"));
				info.setProgHallazgoPieza(SqlBasePlanTratamientoDao.consultarProgramaHallazgoPieza(con, detallePlanTratamiento.intValue(), info.getCodigopk().intValue()));
				info.setColorLetra(info.getProgHallazgoPieza().getColorLetra());
				if(utilizaProgramas)
				{
					if(aux.compareTo(info.getCodigoPkProgramaServicio())!=0)
					{
						array.add(info);
						aux = info.getCodigoPkProgramaServicio();
					}
				}else	
					array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			// se actualizan los datos de la fecha inicial y la final de los servicios
			for(InfoProgramaServicioPlan elem: array)
			{
				elem.setFechaInicio(fechaIni);
				elem.setFechaFinal(fechaFin);
			}
		}catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		return array;
	}
	
	
	
	
	/**
	 * 
	 * @param bigDecimal
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas 
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios , int institucion) 
	{
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		String consultaStr="";
		if(utilizaProgramas)
		{
			consultaStr= "SELECT DISTINCT " +
				"pro.codigo as codigo ," +
				"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
				"pro.codigo_programa as codigoamostrar ,"+
				"plant.estado_programa as estadoPrograma ," +
				"plant.inclusion as inclusion, " +
				"plant.exclusion as exclusion," +
				"plant.garantia as garantia ," +
				"plant.activo as activo ," +
				"plant.indicativo_programa as indicativoPrograma ," +
				"plant.estado_autorizacion as estadoAutorizacion ," +
				"plant.por_confirmar as porConfirmar, " +
				"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codsuperficie, "+
				"dpt.hallazgo as hallazgo, "+
				"coalesce(dpt.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as pieza, " +
				"dpt.seccion as seccion," +
				"sxp.prog_hallazgo_pieza  as proghallpieza, " +
				"cont_mirg.contrato AS contrato_migrado, " +
				"cont_mirg.estado_contrato AS estado_contrato_migrado "+
			"FROM " +
				" odontologia.programas_servicios_plan_t plant " +
				" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk=plant.det_plan_tratamiento) " +
				" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.det_plan_trata=dpt.codigo_pk)" +
				" INNER JOIN odontologia.programas_hallazgo_pieza php on(sxp.prog_hallazgo_pieza=php.codigo_pk and plant.programa=php.programa) " +
				" LEFT OUTER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
				" LEFT OUTER JOIN odontologia.progr_contrato_estado_migrado cont_mirg ON(cont_mirg.programa_hallazgo_pieza=php.codigo_pk) "+
			
			" WHERE " +
				"plant.det_plan_tratamiento="+detallePlanTratamiento+" ";
				consultaStr+=(estadosProgramasOservicios.size()>0)?" and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+")  ":"";
				consultaStr+="and plant.activo='"+ConstantesBD.acronimoSi+"' " ;
				consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND plant.por_confirmar='"+porConfirmar+"'"; 
		}
		else
		{
			consultaStr+= "SELECT DISTINCT " +
							"plant.servicio as codigoServicio ," +
							"getnombreservicio(plant.servicio,"+ConstantesBD.codigoTarifarioCups+" )  as nombreServicio, " +
							"getcodigoservicio(plant.servicio, "+ConstantesBD.codigoTarifarioCups+") as codigoamostrar ," +
							"plant.inclusion as inclusion, " +
							"plant.exclusion as exclusion," +
							"plant.garantia as garantia ," +
							"plant.activo as activo ," +
							"plant.indicativo_programa as indicativoPrograma ," +
							"plant.estado_autorizacion as estadoAutorizacion ," +
							"plant.estado_servicio as estadoServicio ," +
							"plant.por_confirmar as porConfirmar, "+ 
							"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codsuperficie, "+
							"dpt.hallazgo as hallazgo, "+
							"coalesce(dpt.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as pieza, " +
							"dpt.seccion as seccion," +
							"sxp.prog_hallazgo_pieza  as proghallpieza, " +
							"dpt.plan_tratamiento AS plan_tratamiento "+
						"FROM " +
								"odontologia.programas_servicios_plan_t plant " +
								"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk=plant.det_plan_tratamiento) " +
								"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.det_plan_trata=dpt.codigo_pk) " +
						"WHERE " +
								"plant.det_plan_tratamiento="+detallePlanTratamiento+" ";
								consultaStr+=(estadosProgramasOservicios.size()>0)?" and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+")  ":"";
								consultaStr+="and plant.activo='"+ConstantesBD.acronimoSi+"' ";
								consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND por_confirmar='"+porConfirmar+"'"; 
								
		}
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			
			Log4JManager.info(consultaStr);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				if(utilizaProgramas)
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoPrograma(rs.getString("estadoPrograma"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					info.setContratoMigrado(rs.getString("contrato_migrado"));
					info.setEstadoContratoMigrado(rs.getString("estado_contrato_migrado"));
					// info.setCodigoPKProgramasServiciosPlanTratamiento(rs.getBigDecimal("codigoProgServPlan"));
					//cargar los servicio del plan de tratamientos
					if(cargaServicios)
					{
						info.setListaServicios(cargarServiciosDeProgramasPlanT(detallePlanTratamiento,rs.getBigDecimal("codigo") , institucion));
					}
					//	info.setNumeroSuperficies(rs.getInt("numerosuperficies"));
					
					DtoPlanTratamientoNumeroSuperficies dtoNumSup= new DtoPlanTratamientoNumeroSuperficies();
					dtoNumSup.setHallazgo(new InfoDatosInt(rs.getInt("hallazgo")));
					dtoNumSup.setPiezaDental(new InfoDatosInt(rs.getInt("pieza")));
					dtoNumSup.setPlanTratamiento(obtenerCodigoPlanTratamientoDatoPkDetalle(con, detallePlanTratamiento));
					dtoNumSup.setPrograma(new InfoDatosInt(info.getCodigoPkProgramaServicio().intValue(), info.getNombreProgramaServicio()));
					dtoNumSup.setSeccion(new InfoIntegridadDominio(rs.getString("seccion")));
					info.setSuperficiesAplicaPresupuesto(SqlBaseNumeroSuperficiesPresupuestoDao.obtenerInfoNumSuperficiesPlanTratamiento(con,dtoNumSup, rs.getInt("codsuperficie")));
					info.setColorLetra(SqlBaseNumeroSuperficiesPresupuestoDao.obtenerColorLetra(info.getSuperficiesAplicaPresupuesto()));
					info.setProgHallazgoPieza(new DtoProgHallazgoPieza(rs.getInt("proghallpieza"), ""));
					info.setHallazgoAplicaA(SqlBaseHallazgosOdontologicosDao.obtenerAplicaAXHallazgo(dtoNumSup.getHallazgo().getCodigo()));
					
					cargarNumeroSuperficies(con, info);
				}
				else
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoServicio"));
					info.setNombreProgramaServicio(rs.getString("nombreServicio"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoServicio(rs.getString("estadoServicio"));
					info.setTieneArticulos(cargarServArtIncCitaOdo(rs.getBigDecimal("codigoServicio").intValue(), detallePlanTratamiento));
					// info.setCodigoPKProgramasServiciosPlanTratamiento(rs.getBigDecimal("codigoProgServPlan"));
				}
				
				
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}		
		return array;
	}
	
	/**
	 * Consulta el numero de superficies por programa
	 * utilizando como par&aacute;metro el codigo_pk de la tabla programas_hallazgo_pieza
	 * @param con Conexi&oacute;n con la BD
	 * @param info Dto con el codigo_pk asignado de la tabla programas_hallazgo_pieza a consultar
	 * @autor Juan David Ram&iacute;rez
	 * @since 28 Julio 2010
	 * @version 1.0.1
	 */
	private static void cargarNumeroSuperficies(Connection con, InfoProgramaServicioPlan info)
	{
		/*
		 * Tomo el codigo_pk del programa hallazgo pieza
		 */
		String sqlNumeroSuperficies=
			"SELECT " +
				"count(1) as numeroSuperficies " +
			"FROM " +
				"odontologia.superficies_x_programa sxp " +
			"INNER JOIN " +
				"odontologia.programas_hallazgo_pieza php " +
					"ON(php.codigo_pk=sxp.prog_hallazgo_pieza) " +
			"where " +
					"php.codigo_pk=? ";
		
		PreparedStatementDecorator psdPhp=new PreparedStatementDecorator(con, sqlNumeroSuperficies);
		try
		{
			psdPhp.setInt(1, info.getProgHallazgoPieza().getCodigoPk());
			ResultSetDecorator rsd=new ResultSetDecorator(psdPhp.executeQuery());
			if(rsd.next())
			{
				info.setNumeroSuperficies(rsd.getInt("numeroSuperficies"));
			}
			rsd.close();
			psdPhp.close();
		} catch (SQLException e)
		{
			Log4JManager.info("error consultando el número de superficies del programa", e);
		}

		
	}




	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param detallePlanTratamiento
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static BigDecimal obtenerCodigoPlanTratamientoDatoPkDetalle(
			Connection con, BigDecimal detallePlanTratamiento) {
		String consulta=" select plan_tratamiento from odontologia.det_plan_tratamiento where codigo_pk=?";
		BigDecimal retorna= BigDecimal.ZERO;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, detallePlanTratamiento);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			Log4JManager.info(ps.toString());
			
			if(rs.next())
			{
				retorna= rs.getBigDecimal(1);
			}
			
			Log4JManager.info("retornma--->"+retorna);
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return retorna;
	}




	/**
	 * Metodo que Carga los Programa y los Servicio del  Plan de tratamiento
	 * Utilizando Dto
	 * retorna un infoProgramasServicios
	 * @param bigDecimal
	 * @param estadosProgramasOservicios
	* @param utilizaProgramas 
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosDTO(
																					DtoDetallePlanTratamiento dtoDetallePlan,
																					DtoProgramasServiciosPlanT dtoProgramaServicios,  
																					ArrayList<String> estadosProgramasOservicios, 
																					boolean utilizaProgramas, 
																					String porConfirmar, 
																					boolean cargaServicios ,
																					int institucion
																					) 
	{
		Log4JManager.info("\n\n\n **********************************************************************************************");
		Log4JManager.info("\n---------------------------------------CARGANDO PROGRAMAS O SERVICIOS----------------------------");
		Log4JManager.info("****************************************************************************************************");
		
		
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		
		String consultaStr="";
		
		if(utilizaProgramas)
		{
			consultaStr= "SELECT DISTINCT " +
				"pro.codigo as codigo ," +
				"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
				"pro.codigo_programa as codigoamostrar ,"+
				"plant.estado_programa as estadoPrograma ," +
				"plant.inclusion as inclusion, " +
				"plant.exclusion as exclusion," +
				"plant.garantia as garantia ," +
				"plant.activo as activo ," +
				"plant.indicativo_programa as indicativoPrograma ," +
				"plant.estado_autorizacion as estadoAutorizacion ," +
				"plant.por_confirmar as porConfirmar " +
			"FROM " +
				"odontologia.programas_servicios_plan_t plant " +
				"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk=plant.det_plan_tratamiento) " +
				"LEFT OUTER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
			"WHERE " +
				"plant.det_plan_tratamiento="+dtoDetallePlan.getCodigo()+" ";
			
				//Filtros
				consultaStr+=(estadosProgramasOservicios.size()>0)?" and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+")  ":"";
				consultaStr+="and plant.activo='"+ConstantesBD.acronimoSi+"' " ; // SIEMPRE SON ACTIVOS 
				consultaStr+=!UtilidadTexto.isEmpty(dtoProgramaServicios.getGarantia())?" AND plant.garantia='"+dtoProgramaServicios.getGarantia()+"' ": " ";
				consultaStr+=!UtilidadTexto.isEmpty(dtoProgramaServicios.getInclusion())?" AND plant.inclusion='"+dtoProgramaServicios.getInclusion()+"' ": " ";
				consultaStr+=!UtilidadTexto.isEmpty(dtoProgramaServicios.getExclusion())? " AND plant.exclusion='"+dtoProgramaServicios.getExclusion()+"' ": " "; 
				consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND plant.por_confirmar='"+porConfirmar+"'"; 
		}
		else
		{
			consultaStr+= "SELECT DISTINCT " +
							"plant.servicio as codigoServicio ," +
							"getnombreservicio(plant.servicio,"+ConstantesBD.codigoTarifarioCups+" )  as nombreServicio, " +
							"getcodigoservicio(plant.servicio, "+ConstantesBD.codigoTarifarioCups+") as codigoamostrar ," +
							"plant.inclusion as inclusion, " +
							"plant.exclusion as exclusion," +
							"plant.garantia as garantia ," +
							"plant.activo as activo ," +
							"plant.indicativo_programa as indicativoPrograma ," +
							"plant.estado_autorizacion as estadoAutorizacion ," +
							"plant.estado_servicio as estadoServicio ," +
							"plant.por_confirmar as porConfirmar "+ 
							
							" "+
						"FROP " +
								"odontologia.programas_servicios_plan_t plant " +
						"WHERE " +
								"plant.det_plan_tratamiento="+dtoDetallePlan.getCodigo()+" ";
								consultaStr+=(estadosProgramasOservicios.size()>0)?" and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+")  ":"";
								consultaStr+="and plant.activo='"+ConstantesBD.acronimoSi+"' ";
								consultaStr+=!UtilidadTexto.isEmpty(dtoProgramaServicios.getGarantia())?" AND plant.garantia='"+dtoProgramaServicios.getGarantia()+"' ": " ";
								consultaStr+=!UtilidadTexto.isEmpty(dtoProgramaServicios.getInclusion())?" AND plant.inclusion='"+dtoProgramaServicios.getInclusion()+"'": " ";
								consultaStr+=!UtilidadTexto.isEmpty(dtoProgramaServicios.getExclusion())? " AND plant.exclusion='"+dtoProgramaServicios.getExclusion()+"'": "";	
								consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND por_confirmar='"+porConfirmar+"'"; 
								
		}
		
		
		
		Log4JManager.info("CONSULTA PROGRAMAS \n\n\n\n-->"+consultaStr);
		Log4JManager.info(" \n\n\n");
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				if(utilizaProgramas)
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoPrograma(rs.getString("estadoPrograma"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					// info.setCodigoPKProgramasServiciosPlanTratamiento(rs.getBigDecimal("codigoProgServPlan"));
					//cargar los servicio del plan de tratamientos
					
					if(cargaServicios) //CARGAR TODOS LOS SERVICIOS DEL PROGRAMA
					{
						//TODO OJO CON LA CONVERSION A BIGDECIMAL
						info.setListaServicios(cargarServiciosDeProgramasPlanT(new BigDecimal(dtoDetallePlan.getCodigo()) ,rs.getBigDecimal("codigo") , institucion));
					}
					
					//info.setNumeroSuperficies(rs.getInt("numerosuperficies"));
					cargarNumeroSuperficies(con, info);
				 }
				else
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoServicio"));
					info.setNombreProgramaServicio(rs.getString("nombreServicio"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoServicio(rs.getString("estadoServicio"));
					info.setTieneArticulos(cargarServArtIncCitaOdo(rs.getBigDecimal("codigoServicio").intValue(), new BigDecimal(dtoDetallePlan.getCodigo())));
					
				}
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		return array;
	}
	
	
	
	
	/**
	*  
	* @param codigoPkPresupuesto
	* @param bigDecimal
	* @param estadosProgramasOservicios
	* @param utilizaProgramas
	* @param porConfirmar
	* @return
	*/
	private static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosXPresupuesto(	BigDecimal codigoPkPresupuesto, 
																								BigDecimal detallePlanTratamiento,
																								ArrayList<String> estadosProgramasOservicios,
																								boolean utilizaProgramas, 
																								String porConfirmar) 
	{
		Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n **********************************************************************************************");
		Log4JManager.info("\n---------------------------------------CARGANDO PROGRAMAS O SERVICIOS-----------------------");
		Log4JManager.info("**********************************************************************************************");
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		String consultaStr="";
		if(utilizaProgramas)
		{
			consultaStr= "SELECT DISTINCT " +
								"pro.codigo as codigo ," +
								"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
								"pro.codigo_programa as codigoamostrar ,"+
								"plant.estado_programa as estadoPrograma ," +
								"plant.inclusion as inclusion, " +
								"plant.exclusion as exclusion," +
								"plant.garantia as garantia ," +
								"plant.activo as activo ," +
								"plant.indicativo_programa as indicativoPrograma ," +
								"plant.estado_autorizacion as estadoAutorizacion ," +
								"plant.por_confirmar as porConfirmar, " +
								" (	" +
								" SELECT " +
								" coalesce(dhps.numero_superficies,0) " +
								" FROM " +
								" odontologia.hallazgos_vs_prog_ser hvps " +
								" INNER JOIN odontologia.det_hall_prog_ser dhps ON(dhps.hallazgo_vs_prog_ser=hvps.codigo)  " +
								" where " +
								" hvps.hallazgo=dpt.hallazgo and dhps.programa=pro.codigo" +
								" ) as numerosuperficies, " +
								"pptt.activo as asignadopresupuesto, " +
								"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codsuperficie, " +
								"dpt.hallazgo as hallazgo, " +
								"coalesce(dpt.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as pieza, " +
								"dpt.seccion as seccion," +
								"sxp.prog_hallazgo_pieza  as proghallpieza  "+
							"FROM " +
								"odontologia.presu_plan_tto_prog_ser pptt " +
								"INNER JOIN odontologia.programas_servicios_plan_t plant on (pptt.programa_servicio_plan_t=plant.codigo_pk) " +
								"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk=plant.det_plan_tratamiento) " +
								"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.det_plan_trata=dpt.codigo_pk ) " +
								"INNER JOIN odontologia.programas_hallazgo_pieza php on(php.codigo_pk=sxp.prog_hallazgo_pieza and php.programa=plant.programa) "+ 
								"INNER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
							"WHERE " +
								"pptt.presupuesto = "+codigoPkPresupuesto+" "+
								"and plant.det_plan_tratamiento="+detallePlanTratamiento+" ";
								consultaStr+=(estadosProgramasOservicios.size()>0)?" and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+")  ":"";
								consultaStr+="and plant.activo='"+ConstantesBD.acronimoSi+"' " ;
								consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND plant.por_confirmar='"+porConfirmar+"' ";
				
			consultaStr+="UNION "+
							"SELECT DISTINCT " +
								"pro.codigo as codigo ," +
								"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
								"pro.codigo_programa as codigoamostrar ,"+
								"'"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' as estadoPrograma ," +
								"'' as inclusion, " +
								"'' as exclusion," +
								"'' as garantia ," +
								"'"+ConstantesBD.acronimoSi+"' as activo ," +
								"'' as indicativoPrograma ," +
								"'' as estadoAutorizacion ," +
								"'' as porConfirmar, " +
								" (	" +
										"SELECT " +
											"coalesce(dhps.numero_superficies,0) " +
										"FROM " +
											"odontologia.hallazgos_vs_prog_ser hvps " +
											"INNER JOIN odontologia.det_hall_prog_ser dhps ON(dhps.hallazgo_vs_prog_ser=hvps.codigo)  " +
										"where " +
											"hvps.hallazgo=dpt.hallazgo and dhps.programa=pro.codigo" +
								" ) as numerosuperficies, " +
								"pptt.activo as asignadopresupuesto, "+
								"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codsuperficie, "+
								"dpt.hallazgo as hallazgo, "+
								"coalesce(dpt.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as pieza, " +
								"dpt.seccion as seccion," +
								"sxp.prog_hallazgo_pieza  as proghallpieza  "+
							" from " +
								"odontologia.presu_plan_tto_prog_ser pptt " +
								"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk=pptt.det_plan_tratamiento) " +
								"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.det_plan_trata=dpt.codigo_pk) " +
								"INNER JOIN odontologia.programas_hallazgo_pieza php on(php.codigo_pk=sxp.prog_hallazgo_pieza and php.programa=pptt.programa) "+ 
								"INNER JOIN odontologia.programas pro ON (pptt.programa=pro.codigo) " +
							"where " +
								"pptt.presupuesto = "+codigoPkPresupuesto+" "+
								"and pptt.det_plan_tratamiento="+detallePlanTratamiento+" " +
								"and programa_servicio_plan_t is null ";
								
		}
		else
		{
			consultaStr+= "SELECT DISTINCT " +
								"plant.servicio as codigoServicio ," +
								"getnombreservicio(plant.servicio,"+ConstantesBD.codigoTarifarioCups+" )  as nombreServicio, " +
								"getcodigoservicio(plant.servicio, "+ConstantesBD.codigoTarifarioCups+") as codigoamostrar ," +
								"plant.inclusion as inclusion, " +
								"plant.exclusion as exclusion," +
								"plant.garantia as garantia ," +
								"plant.activo as activo ," +
								"plant.indicativo_programa as indicativoPrograma ," +
								"plant.estado_autorizacion as estadoAutorizacion ," +
								"plant.estado_servicio as estadoServicio ," +
								"plant.por_confirmar as porConfirmar "+ 
								
								" "+
							"from " +
									"odontologia.programas_servicios_plan_t plant " +
							"where " +
									"plant.det_plan_tratamiento="+detallePlanTratamiento+" ";
									consultaStr+=(estadosProgramasOservicios.size()>0)?" and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosProgramasOservicios)+")  ":"";
									consultaStr+="and plant.activo='"+ConstantesBD.acronimoSi+"' ";
									consultaStr+=UtilidadTexto.isEmpty(porConfirmar)?" ":" AND por_confirmar='"+porConfirmar+"'"; 
									
		}
		
		Log4JManager.info("CONSULTA PROGRAMAS \n\n\n\n-->"+consultaStr);
		Log4JManager.info(" \n\n\n\n\n\n\n\n");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				if(utilizaProgramas)
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoPrograma(rs.getString("estadoPrograma"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					// info.setCodigoPKProgramasServiciosPlanTratamiento(rs.getBigDecimal("codigoProgServPlan"));
					//cargar los servicio del plan de tratamientos
					/*if(cargaServicios)
					{
						info.setListaServicios(cargarServiciosDeProgramasPlanT(detallePlanTratamiento,rs.getBigDecimal("codigo")));
					}*/
					//info.setNumeroSuperficies(rs.getInt("numerosuperficies"));
					cargarNumeroSuperficies(con, info);
					
					info.setAsignadoAlPresupuesto(UtilidadTexto.getBoolean(rs.getString("asignadopresupuesto")));
					
					//FIXME PRESUPUESTO info.setColorLetra(colorLetra)  info.setSuperficiesAplicaPresupuesto(superficiesAplica) info.setHallazgoAplicaA(hallazgoAplicaA)
					
					DtoPresupuestoPlanTratamientoNumeroSuperficies dtoNumSup= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
					dtoNumSup.setHallazgo(new InfoDatosInt(rs.getInt("hallazgo")));
					dtoNumSup.setPiezaDental(new InfoDatosInt(rs.getInt("pieza")));
					dtoNumSup.setPresupuesto(codigoPkPresupuesto);
					dtoNumSup.setPrograma(new InfoDatosInt(info.getCodigoPkProgramaServicio().intValue(), info.getNombreProgramaServicio()));
					dtoNumSup.setSeccion(new InfoIntegridadDominio(rs.getString("seccion")));
					info.setSuperficiesAplicaPresupuesto(SqlBaseNumeroSuperficiesPresupuestoDao.obtenerInfoNumSuperficiesPresupuesto(con,dtoNumSup, rs.getInt("codsuperficie")));
					info.setColorLetra(SqlBaseNumeroSuperficiesPresupuestoDao.obtenerColorLetra(info.getSuperficiesAplicaPresupuesto()));
					info.setProgHallazgoPieza(new DtoProgHallazgoPieza(rs.getInt("proghallpieza"), ""));
					info.setHallazgoAplicaA(SqlBaseHallazgosOdontologicosDao.obtenerAplicaAXHallazgo(dtoNumSup.getHallazgo().getCodigo()));
				 }
				else
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoServicio"));
					info.setNombreProgramaServicio(rs.getString("nombreServicio"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoServicio(rs.getString("estadoServicio"));
					info.setTieneArticulos(cargarServArtIncCitaOdo(rs.getBigDecimal("codigoServicio").intValue(), detallePlanTratamiento));
					// info.setCodigoPKProgramasServiciosPlanTratamiento(rs.getBigDecimal("codigoProgServPlan"));
					info.setAsignadoAlPresupuesto(UtilidadTexto.getBoolean(rs.getString("asignadopresupuesto")));
				}
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		return array;
	}
	
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param pieza
	 * @param seccion
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @param activo
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(
			BigDecimal codigoPkPlanTratamiento, 
			int pieza, 
			String seccion, 
			ArrayList<String> estadosProgramasOservicios, 
			boolean utilizaProgramas, 
			String porConfirmar,
			String activo)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		try 
		{
			Log4JManager.info("\n ********************************************************************************************************************************");
			Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
			String consultaStr="SELECT " +
									"d.codigo_pk as codigodetallepk, " +
									"d.hallazgo as codigohallazgo, " +
									"h.nombre as nombrehallazgo, " +
									"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
									"coalesce(s.nombre, '') as nombresuperficie " +
								"FROM " +
									"odontologia.det_plan_tratamiento d " +
									"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
									"LEFT OUTER JOIN  historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
								"WHERE " +
									"d.plan_tratamiento="+codigoPkPlanTratamiento+" " +
									"and d.pieza_dental="+pieza+" " +
									"and d.seccion='"+seccion+"' " +
									"and d.por_confirmar = '"+porConfirmar+"' " +
									"and d.activo = '"+activo+"' ";
			
			Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				hallazgoSuperficie.setProgramasOservicios(
						obtenerProgramasOServicios(
									rs.getBigDecimal("codigodetallepk"), 
									estadosProgramasOservicios, 
									utilizaProgramas, porConfirmar,
									activo));
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param hallazgoOPCIONAL
	 * @param seccion
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @param activo
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(
			BigDecimal codigoPkPlanTratamiento, 
			int hallazgoOPCIONAL, 
			String seccion, 
			ArrayList<String> estadosProgramasOservicios, 
			boolean utilizaProgramas, 
			String porConfirmar,
			String activo)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		try 
		{
			Log4JManager.info("\n ********************************************************************************************************************************");
			Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
			
			String consultaStr="SELECT " +
									"d.codigo_pk as codigodetallepk, " +
									"d.hallazgo as codigohallazgo, " +
									"h.nombre as nombrehallazgo, " +
									"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
									"coalesce(s.nombre, '') as nombresuperficie " +
								"FROM " +
									"odontologia.det_plan_tratamiento d " +
									"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
									"LEFT OUTER JOIN  historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
								"WHERE " +
									"d.plan_tratamiento="+codigoPkPlanTratamiento+" " ;
			
			consultaStr+=(hallazgoOPCIONAL>0)?"and d.hallazgo="+hallazgoOPCIONAL+" ":" ";
			consultaStr+=	"and d.seccion='"+seccion+"' " +
							"and d.por_confirmar = '"+porConfirmar+"' " +
							"and d.activo = '"+activo+"' ";
			
			Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				hallazgoSuperficie.setProgramasOservicios(
						obtenerProgramasOServicios(
							rs.getBigDecimal("codigodetallepk"), 
							estadosProgramasOservicios, 
							utilizaProgramas, porConfirmar,
							activo));
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	
	public static ArrayList<DtoLogPlanTratamiento> cargarLogs(DtoLogPlanTratamiento dtoWhere)
	{
		ArrayList<DtoLogPlanTratamiento> arrayDto= new ArrayList<DtoLogPlanTratamiento>();
		String consultaStr= "";	
		

		
		   Log4JManager.info("-****************************************************************************************************");
			Log4JManager.info("---------------------------------------CONSULTAR LOG PLAN T-----------------------------------------");
			
			Log4JManager.info("DTO CONSULTA--->"+UtilidadLog.obtenerString(dtoWhere, true));
			
			if(dtoWhere.getHistoricoPlanT().equals(ConstantesBD.acronimoSi)){
				
				consultaStr=" select " +
				"  DISTINCT "+ //1
				"   lpt.plan_tratamiento as plan_tratamiento, "+ //2
				"	lpt.estado as estado ,"+ //3
				"	lpt.motivo as motivo  ,"+ //4
				"	( select mt.nombre from odontologia.motivos_atencion mt where mt.codigo_pk=lpt.motivo ) as nombreMotivo  ,"+ //4
				"	lpt.especialidad as especialidad, "+ //5
				"   getnombreespecialidad(lpt.especialidad) as nombreEspecialidad  ,"+ // 6
				
				"	lpt.fecha  as fecha , "+ //8
				"	lpt.hora  as hora , "+ //9
				"	lpt.codigo_medico  as  codigo_medico , "+ //10
				"	getnombremedico(lpt.codigo_medico)  as  nombre_medico, " +//11
				
				"   lpt.fecha ||' '||lpt.hora   AS  fecha_hora " + 
				
				
				"   from " +
				" odontologia.log_plan_tratamiento lpt " +
				" 	where 1=1 ";
				
				consultaStr+=(dtoWhere.getCodigoPk().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.codigo_pk="+dtoWhere.getCodigoPk():"";
				consultaStr+=(dtoWhere.getPlanTratamiento().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.plan_tratamiento="+dtoWhere.getPlanTratamiento():"";
				consultaStr+=UtilidadTexto.isEmpty(dtoWhere.getEstado())?"":" AND estado='"+dtoWhere.getEstado()+"'";
				consultaStr+=(dtoWhere.getMotivo().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.motivo="+dtoWhere.getMotivo().getCodigo():"";
				consultaStr+=(dtoWhere.getEspecialidad().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.especialidad="+dtoWhere.getEspecialidad().getCodigo():"";
				
				consultaStr+= "order by fecha_hora ASC";
				
			}else
			{
			consultaStr=" select " +
										"  distinct "+ //1
										"  lpt.plan_tratamiento as plan_tratamiento, "+ //2
										"	lpt.estado as estado ,"+ //3
										"	lpt.motivo as motivo  ,"+ //4
										"	( select mt.nombre from odontologia.motivos_atencion mt where mt.codigo_pk=lpt.motivo ) as nombreMotivo  ,"+ //4
										"	lpt.especialidad as especialidad, "+ //5
										"   getnombreespecialidad(lpt.especialidad) as nombreEspecialidad  ,"+ // 6
										"	lpt.fecha  as fecha , "+ //8
										"	lpt.hora  as hora , "+ //9
										"	lpt.usuario_modifica as  loginUsuarioModifica , "+ //10
										"	getnombreusuario(lpt.usuario_modifica)  as  nombreUsuario  "+ //11
										"   from " +
										" odontologia.log_plan_tratamiento lpt " +
										" 	where 1=1 ";
			
			        
			
			
			
			consultaStr+=(dtoWhere.getCodigoPk().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.codigo_pk="+dtoWhere.getCodigoPk():"";
			consultaStr+=(dtoWhere.getPlanTratamiento().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.plan_tratamiento="+dtoWhere.getPlanTratamiento():"";
			consultaStr+=UtilidadTexto.isEmpty(dtoWhere.getEstado())?"":" AND estado='"+dtoWhere.getEstado()+"'";
			consultaStr+=(dtoWhere.getMotivo().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.motivo="+dtoWhere.getMotivo().getCodigo():"";
			consultaStr+=(dtoWhere.getEspecialidad().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpt.especialidad="+dtoWhere.getEspecialidad().getCodigo():"";
			
			consultaStr+= "order by lpt.fecha ";
			
			}

		   
			
			Log4JManager.info("CONSULTA HISTORICOS PLAN DE TRATAMIENTO \n\n\n\n"+consultaStr+"\n\n\n");
			
		   try 
			{
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+"  ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					DtoLogPlanTratamiento newdto = new DtoLogPlanTratamiento();
					
				if(!dtoWhere.getHistoricoPlanT().equals(ConstantesBD.acronimoSi))
					{
					newdto.setCodigoPk(rs.getDouble("codigoPk"));
					}
					newdto.setPlanTratamiento(rs.getDouble("plan_tratamiento"));
					newdto.setEstado(rs.getString("estado"));
					newdto.setMotivo(new InfoDatosInt(rs.getInt("motivo"), rs.getString("nombreMotivo")));
					newdto.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad"),rs.getString("nombreEspecialidad")));
					newdto.getModificacion().setFechaModifica(rs.getString("fecha"));
					newdto.getModificacion().setHoraModifica(rs.getString("hora"));
					newdto.getModificacion().setUsuarioModifica(rs.getString("nombreUsuario"));
					
                    //newdto.setCodigoMedico(new InfoDatosInt(rs.getInt("loginUsuarioModifica"),rs.getString("nombreUsuario")));
					arrayDto.add(newdto);
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
		   
			catch (SQLException e) 
			{
				Log4JManager.error("error en carga log plan==> "+e);
			}
			
			return arrayDto;
		
	}
	
	/**
	 * 
	 * @param codPkDetallePlanT
	 * @param codPrograma
	 * @param codServicio
	 * @return
	 */
	public static int obtenerCodPkLogServPlanT (int codPkDetallePlanT, int codPrograma, int codServicio)
	{
		String consultaStr= "SELECT codigo_pk AS codigoprog " +
				            "FROM odontologia.programas_servicios_plan_t " +
				            "WHERE det_plan_tratamiento = ?  AND programa = ? ";
		if(codServicio>0)
			consultaStr += " AND servicio = ?";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, codPkDetallePlanT);
			ps.setInt(2, codPrograma);
			if(codServicio>0)
			 ps.setInt(3, codServicio);
			
			
			Log4JManager.error("Cadena obtener Codigo==> "+consultaStr +" Codigo Detalle>>"+codPkDetallePlanT+"  CodigoPrograma>>"+codPrograma+" Codigo Servicio>>"+ codServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			  {
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
				return rs.getInt("codigoprog");
			  } 
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			
		}catch (SQLException e) 
			{
				Log4JManager.error("error en carga codigo Programa/Servicio  plan==> "+e);
			}
		
		return 0;
	}
	
	
	
	public static ArrayList<DtoLogProgServPlant> cargarLogProgramas(DtoLogProgServPlant dtoWhere)
	{
		ArrayList<DtoLogProgServPlant> arrayDto= new ArrayList<DtoLogProgServPlant>();
		String consultaStr="", estadoAnt="", estadoNuevo="";
		
		

		
		    Log4JManager.info("-****************************************************************************************************");
			Log4JManager.info("---------------------------------------CONSULTAR LOG PROG PLAN T-----------------------------------------");
			
			Log4JManager.info("DTO CONSULTA--->"+UtilidadLog.obtenerString(dtoWhere, true));
			Log4JManager.info("Histoirico >> "+dtoWhere.getHistoricoProgServ());
			
			if(dtoWhere.getHistoricoProgServ().equals(ConstantesBD.acronimoSi))
			{
				
				consultaStr=" select " +				
			   "   lps.programa_servicio_plan_t as programa_servicio_plan_t, "+ //2
				"	lps.estado_programa as estado_programa ,"+ //3
				"	lps.estado_servicio as estado_servicio ,"+ //4
				"	lps.motivo as motivo  ,"+ //4
				"	( select mt.nombre from odontologia.motivos_atencion mt where mt.codigo_pk=lps.motivo ) as nombreMotivo  ,"+ //4
				"	lps.especialidad as especialidad, "+ //5
				"   getnombreespecialidad(lps.especialidad) as nombreEspecialidad  ,"+ // 6
				
				"	lps.fecha_modifica  as fecha_modifica , "+ //8
				"	lps.hora_modifica  as hora_modifica , "+ //9
				"	getnombreusuario(lps.usuario_modifica)  as  usuario_modifica, "+ //10
				"   lps.fecha_modifica||' '||lps.hora_modifica  AS  fecha_hora " + 
				
				"   from " +
				"   odontologia.log_programas_servicios_plan_t lps " +
				" 	where 1=1 " ;
				
						
				consultaStr+=(dtoWhere.getCodigoPk().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lps.codigo_pk="+dtoWhere.getCodigoPk():"";
				consultaStr+=(dtoWhere.getProgServPlant()>ConstantesBD.codigoNuncaValidoDouble)?" AND lps.programa_servicio_plan_t="+dtoWhere.getProgServPlant():"";
				
				consultaStr+= " group by lps.programa_servicio_plan_t,lps.estado_servicio, lps.estado_programa,lps.motivo,lps.especialidad,lps.usuario_modifica, lps.fecha_modifica,lps.hora_modifica " +
				             "  order by lps.programa_servicio_plan_t,lps.fecha_modifica,lps.hora_modifica ASC";
						
			}
			else
			{
			consultaStr=" select " +
										"   lps.codigo_pk as codigoPk, "+ //1
										"   lps.programa_servicio_plan_t as programa_servicio_plan_t, "+ //2
										"	lps.estado_programa as estado_programa ,"+ //3
										"	lps.estado_servicio as estado_servicio ,"+ //4
										"	lps.motivo as motivo  ,"+ //5
										"	( select mt.nombre from odontologia.motivos_atencion mt where mt.codigo_pk=lps.motivo ) as nombreMotivo  ,"+ //6
										"	lps.especialidad as especialidad, "+ //7
										"   getnombreespecialidad(lps.especialidad) as nombreEspecialidad  ,"+ // 8
										
										"	lps.fecha_modifica  as fecha_modifica , "+ //9
										"	lps.hora_modifica  as hora_modifica , "+ //10
										"	getnombreusuario(lps.usuario_modifica)  as  usuario_modifica "+ //11
										
										
										
										"  from " +
										"  odontologia.log_programas_servicios_plan_t lps " +
										"  where 1=1 ";
			
			consultaStr+=(dtoWhere.getCodigoPk().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lps.codigo_pk="+dtoWhere.getCodigoPk():"";
			consultaStr+=(dtoWhere.getProgServPlant()>ConstantesBD.codigoNuncaValidoDouble)?" AND lps.programa_servicio_plan_t="+dtoWhere.getProgServPlant():"";
			consultaStr+=  "order by  lps.codigo_pk, lps.fecha_modifica , lps.estado_programa desc ";
			
			}	   

		   
		   try 
			{
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				estadoAnt="";
				while(rs.next())
				{
					DtoLogProgServPlant newdto = new DtoLogProgServPlant();
					
					if(dtoWhere.getHistoricoProgServ().equals(ConstantesBD.acronimoSi))
						estadoNuevo= rs.getString("estado_programa");
					else
						estadoNuevo= rs.getString("estado_servicio");
					
					if(!estadoAnt.equals(estadoNuevo))
					{
					 if(!dtoWhere.getHistoricoProgServ().equals(ConstantesBD.acronimoSi))
					   {
					    newdto.setCodigoPk(rs.getDouble("codigoPk"));
					   }
									
						newdto.setProgServPlant(rs.getDouble("programa_servicio_plan_t"));
						newdto.setEstadoPrograma(rs.getString("estado_programa"));
						newdto.setEstadoServicio(rs.getString("estado_servicio"));
						newdto.setMotivo(new InfoDatosInt(rs.getInt("motivo"), rs.getString("nombreMotivo")));
						newdto.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad"),rs.getString("nombreEspecialidad")));
						newdto.getUsuarioModifica().setFechaModifica(rs.getString("fecha_modifica"));
						newdto.getUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
						newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
						arrayDto.add(newdto);
					}
					
					if(dtoWhere.getHistoricoProgServ().equals(ConstantesBD.acronimoSi))
					{	
						estadoAnt= rs.getString("estado_programa");			  
					}else
					{
					    estadoAnt=rs.getString("estado_servicio");
					}
					
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				Log4JManager.info("***********************	CONSULTA LOG PROG PLAN T	**************************************************");
				Log4JManager.info(ps);
			}
		   
			catch (SQLException e) 
			{
				Log4JManager.error("error en carga log plan==> "+e);
			}
			
			return arrayDto;
		
	}
	
	
	
	/**
	 * MODICAR EL ESTADO DEL PLAN DE TRATAMIENTO
	 * @param dto
	 * @param con
	 * @return
	 */
	public static boolean inactivarPlan(DtoPlanTratamientoOdo dto , Connection con  )
	{
		boolean retorna=false;

		String consulta= "update odontologia.plan_tratamiento set codigo_pk=codigo_pk , "+
						"estado =?,"+                  
						"usuario_modifica =?,"+
						"fecha_modifica =?,"+       
						"hora_modifica =?" +
						"" +
						"WHERE " +
						"codigo_pk= ? "; 

		consulta+= (dto.getInstitucion()>0)? "and institucion="+dto.getInstitucion(): "";

		try 
		{

			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con, consulta);
			ps.setString(1, dto.getEstado());
			ps.setString(2, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(3, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(4, dto.getUsuarioModifica().getHoraModifica());
			ps.setBigDecimal(5, dto.getCodigoPk());
			retorna=ps.executeUpdate() > 0;
			Log4JManager.info("retorna -------------------------------------------------------------------->"+retorna);
			Log4JManager.info("---INACTIVAR PLAN DE TRATAMIENTO CONSULTA--"+ps);
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN updatePrograma ", e);
		}
		return retorna;
	}
	 
	 /**
	  * FALTA EL USUARIO MODIFICA 
	  * MODICAR EL ESTADO DEL PLAN DE TRATAMIENTO
	  * @param dto
	  * @param con
	  * @return
	  */
	 public static boolean modificarPlan(int institucion, Connection con , boolean utilizaProgramas )
	 {
		 boolean retorna=false;

		 String consulta="";
		 if(utilizaProgramas)
		 {
			 consulta= 	"update " +
			 				"odontologia.plan_tratamiento " +
			 			"set " +
			 				"estado='"+ConstantesIntegridadDominio.acronimoTerminado+"', " +
			 				"fecha_modifica = CURRENT_DATE, " +
			 				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			 			"where " +
			 				"codigo_pk in" +
			 				"( " +
			 					"SELECT " +
			 						"tabla.codigo_pk " +
			 					"FROM " +
			 						"(" +
			 							"select distinct " +
			 								"pt.codigo_pk, " +
			 								"(" +
			 									"select " +
			 										"count(1) " +
			 									"from " +
			 										"odontologia.det_plan_tratamiento dptinterno " +
			 										"INNER JOIN odontologia.programas_servicios_plan_t psptinterno ON(psptinterno.det_plan_tratamiento=dptinterno.codigo_pk )  " +
			 									"WHERE " +
			 										"dptinterno.plan_tratamiento=pt.codigo_pk " +
			 										"and dptinterno.activo='"+ConstantesBD.acronimoSi+"' " +
			 										"and pspt.estado_programa ='"+ConstantesIntegridadDominio.acronimoTerminado+"'" +
			 								") " +
			 								"as num_prog_serv_term," +
			 								"(" +
			 									"select " +
			 										"count(1) " +
			 									"from " + 
			 										"odontologia.det_plan_tratamiento dptinterno " +
			 										"INNER JOIN odontologia.programas_servicios_plan_t psptinterno ON(psptinterno.det_plan_tratamiento=dptinterno.codigo_pk ) " +
			 									"WHERE " +
			 										"dptinterno.plan_tratamiento=pt.codigo_pk " +
			 										"and dptinterno.activo='"+ConstantesBD.acronimoSi+"' " +
			 										"and psptinterno.estado_programa not in ('"+ConstantesIntegridadDominio.acronimoTerminado+"', '"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"', '"+ConstantesIntegridadDominio.acronimoNoAautorizado+"', '"+ConstantesIntegridadDominio.acronimoExcluido+"', '"+ConstantesIntegridadDominio.acronimoPorAutorizar+"' )" +
			 								") " +
			 								"as num_prog_serv_otros_estados  " +
			 							"FROM " +
			 								"odontologia.plan_tratamiento pt " +
			 								"INNER JOIN odontologia.det_plan_tratamiento dpt ON(dpt.plan_tratamiento=pt.codigo_pk and dpt.activo='"+ConstantesBD.acronimoSi+"') " +
			 								"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk )  " +
			 							"WHERE " +
			 								"pt.institucion="+institucion+" " +
			 								"and pt.estado='"+ConstantesIntegridadDominio.acronimoEstadoEnProceso+"' " +
			 						")" +
			 						"tabla " +
			 					"WHERE " +
			 						"tabla.num_prog_serv_term>0 " +
			 						"and tabla.num_prog_serv_otros_estados<=0)";

		 }
		 else
		 {
			 consulta= 	"update " +
				"odontologia.plan_tratamiento " +
			"set " +
				"estado='"+ConstantesIntegridadDominio.acronimoTerminado+"' " +
			"where " +
				"codigo_pk in" +
				"( " +
					"SELECT " +
						"tabla.codigo_pk " +
					"FROM " +
						"(" +
							"select distinct " +
								"pt.codigo_pk, " +
								"(" +
									"select " +
										"count(1) " +
									"from " +
										"odontologia.det_plan_tratamiento dptinterno " +
										"INNER JOIN odontologia.programas_servicios_plan_t psptinterno ON(psptinterno.det_plan_tratamiento=dptinterno.codigo_pk )  " +
									"WHERE " +
										"dptinterno.plan_tratamiento=pt.codigo_pk " +
										"and dptinterno.activo='"+ConstantesBD.acronimoSi+"' " +
										"and pspt.estado_servicio ='"+ConstantesIntegridadDominio.acronimoTerminado+"'" +
								") " +
								"as num_prog_serv_term," +
								"(" +
									"select " +
										"count(1) " +
									"from " +
										"odontologia.det_plan_tratamiento dptinterno " +
										"INNER JOIN odontologia.programas_servicios_plan_t psptinterno ON(psptinterno.det_plan_tratamiento=dptinterno.codigo_pk ) " +
									"WHERE " +
										"dptinterno.plan_tratamiento=pt.codigo_pk " +
										"and dptinterno.activo='"+ConstantesBD.acronimoSi+"' " +
										"and psptinterno.estado_servicio not in ('"+ConstantesIntegridadDominio.acronimoTerminado+"', '"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"', '"+ConstantesIntegridadDominio.acronimoNoAautorizado+"', '"+ConstantesIntegridadDominio.acronimoExcluido+"')" +
								") " +
								"as num_prog_serv_otros_estados  " +
							"FROM " +
								"odontologia.plan_tratamiento pt " +
								"INNER JOIN odontologia.det_plan_tratamiento dpt ON(dpt.plan_tratamiento=pt.codigo_pk and dpt.activo='"+ConstantesBD.acronimoSi+"') " +
								"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk )  " +
							"WHERE " +
								"pt.institucion="+institucion+" " +
								"and pt.estado='"+ConstantesIntegridadDominio.acronimoEstadoEnProceso+"' " +
						")" +
						"tabla " +
					"WHERE " +
						"tabla.num_prog_serv_term>0 " +
						"and tabla.num_prog_serv_otros_estados<=0)";

		 }

		 try
		 { 	
			 PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con, consulta);
			 retorna=ps.executeUpdate() > 0;
			 Log4JManager.info("retorna -------------------------------------------------------------------->"+retorna);
			 Log4JManager.info("--TERMINAR EL PLAN DE TRATAMIENTO ---"+ps);
			 ps.close();
		 }
		 catch (SQLException e)
		 {
			 Log4JManager.error("ERROR EN updatePrograma ", e);
		 }
		 return retorna;
	 }

	/**
	* metodo que inserta el his_conf_plan_tratamiento
	* @param con
	* @param valoracion
	* @param evolucion
	* @param codigoCita
	* @param codigoPlanTratamiento
	* @return
	*/
	public static boolean insertHisConfPlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoPlanTratamiento)
	{
		boolean result = false;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertHisConfPlanTratamiento);
			if(valoracion>0)
				ps.setInt(1, valoracion);
			else
				ps.setNull(1, Types.INTEGER);
			
			if(evolucion>0)
				ps.setInt(2, evolucion);
			else
				ps.setNull(2, Types.INTEGER);
			
			if(codigoCita>0)
				ps.setInt(3, codigoCita);
			else
				ps.setNull(3, Types.INTEGER);
			
			ps.setInt(4, codigoPlanTratamiento);
			//Log4JManager.info("Cosulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	* metodo que inserta his_conf_det_plan_t
	* @param con
	* @param valoracion
	* @param evolucion
	* @param codigoCita
	* @param codigoPlanTratamiento
	* @return
	*/
	public static boolean insertHisConfDetallePlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoPlanTratamiento)
	{
		boolean result = false,  tieneDetalle = false;;
		
	  try
		{
			//***********PRIMERO SE VERIFICA SI EL PLAN TRATAMIENTO TIENE DETALLE **********************************
			PreparedStatementDecorator ps  = new PreparedStatementDecorator(con,verificarDetallePlanTratamientoStr);
			
			ps.setInt(1,codigoPlanTratamiento);
			Log4JManager.info("\n\n Verificar detalle > "+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					tieneDetalle = true;
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			//******************************************************************
			
		
		if(tieneDetalle)
		{			
		 String strInsertHisConfDetallePlanTratamiento = "INSERT INTO odontologia.his_conf_det_plan_t ( " +
								"codigo_pk, " +
								"plan_tratamiento, " +
								"pieza_dental, " +
								"superficie, " +
								"hallazgo, " +
								"seccion, " +
								"clasificacion, " +
								"por_confirmar, " +
								"convencion, " +
								"usuario_modifica, " +
								"fecha_modifica, " +
								"hora_modifica, " +
								"especialidad, " +
								"valoracion, " +
								"evolucion, " +
								"cita, " +
								"det_plan_tratamiento ) " +
							"SELECT "+
									ValoresPorDefecto.obtenerSentenciaNextval("odontologia.seq_his_conf_det_plan_t") + ", "+
									"plan_tratamiento," +
									"pieza_dental," +
									"superficie," +
									"hallazgo," +
									"seccion," +
									"clasificacion," +
									"por_confirmar," +
									"convencion," +
									"usuario_modifica," +
									"fecha_modifica," +
									"hora_modifica," +
									"especialidad," +
									"?," +
									"?," +
									"?," +
									"codigo_pk " +
									"FROM odontologia.det_plan_tratamiento " +
									"WHERE plan_tratamiento = ? " +
									"AND activo = '"+ConstantesBD.acronimoSi+"' ";
		 
			ps =  new PreparedStatementDecorator(con,strInsertHisConfDetallePlanTratamiento);
			if(valoracion>0)
				ps.setInt(1, valoracion);
			else
				ps.setNull(1, Types.INTEGER);
			
			if(evolucion>0)
				ps.setInt(2, evolucion);
			else
				ps.setNull(2, Types.INTEGER);
			
			if(codigoCita>0)
				ps.setInt(3, codigoCita);
			else
				ps.setNull(3, Types.INTEGER);
			
			ps.setInt(4, codigoPlanTratamiento);
			Log4JManager.info("Cosulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		  }
		  else
		  {
			result=true;
		  }
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		return result;
  }
	
	/**
	* metodo que inserta his_conf_prog_serv_plan_t
	* @param con
	* @param valoracion
	* @param evolucion
	* @param codigoCita
	* @param codigoProgServPlanTrat
	* @return
	*/
	public static boolean insertHisConfProgServPlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoDetPlanTrat)
	{
		boolean result = false, tieneDetalle = false;
		
		try
		{
			//***********PRIMERO SE VERIFICA SI EL DETALLE PLAN TRATAMIENTO TIENE DETALLE DE PROGRAMAS/SERVICIOS**********************************
			PreparedStatementDecorator ps  = new PreparedStatementDecorator(con,verificarDetalleProgramasServiciosStr);
			
			ps.setInt(1,codigoDetPlanTrat);
			Log4JManager.info("\n\n Verificar > "+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					tieneDetalle = true;
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			//******************************************************************
			
			
			if(tieneDetalle)
			{
			
				ps =  new PreparedStatementDecorator(con,strInsertHisConfProgramasServiciosPlanTrat);
				if(valoracion>0)
					ps.setInt(1, valoracion);
				else
					ps.setNull(1, Types.INTEGER);
				
				if(evolucion>0)
					ps.setInt(2, evolucion);
				else
					ps.setNull(2, Types.INTEGER);
				
				if(codigoCita>0)
					ps.setInt(3, codigoCita);
				else
					ps.setNull(3, Types.INTEGER);
				
				ps.setInt(4, codigoDetPlanTrat);
				Log4JManager.info("Cosulta: "+ps);
				if(ps.executeUpdate()>0)
					result = true;
				ps.close();
			}
			else
			{
				result = true;
			}
		}catch (Exception e) 
		{
			Log4JManager.error("Ocurrio errror al insertar en his_conf_programas_serv_plan_t: ",e);
		}
		return result;
	}
	
	/**
	* 
	* @param con
	* @param codigoPlanTrat
	* @param valoracion
	* @param evolucion
	* @param usuario
	* @return
	*/
	public static boolean confirmarPlanTratamiento(Connection con, int codigoPlanTrat, int valoracion, int evolucion, String usuario)
	{
		boolean result = false, deboActualizar = false;
		try
		{
			//***********Se verifica si el plan de tratamiento estï¿½ siendo tratado por la valoraciï¿½n o evolucion***************
			
			String consulta = "SELECT count(1) as cuenta from odontologia.plan_tratamiento WHERE codigo_pk = ? ";
			double tmpCodigoOdontograma=ConstantesBD.codigoNuncaValidoDouble;
			if(valoracion>0)
			{
				consulta += " AND valoracion = "+valoracion;
			}
			if(evolucion>0)
			{
				consulta += " AND evolucion = "+evolucion;
				tmpCodigoOdontograma=cargarCodigoOdontograma(evolucion);
			}
			
			//Log4JManager.info("\n\n\n entro >> "+consulta+" >> "+codigoPlanTrat);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			ps.setInt(1,codigoPlanTrat);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					deboActualizar = true;
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			//*******************************************************************************************************************
			
			if(deboActualizar)
			{
			
				consulta = "UPDATE odontologia.plan_tratamiento SET " +
						" por_confirmar = '"+ConstantesBD.acronimoNo+"', " +
						" usuario_modifica = '"+usuario+"', " +
						" fecha_modifica = CURRENT_DATE, " +
						" hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ";
			
				if(tmpCodigoOdontograma>0)
				{
					consulta+= ", odontograma_diagnostico= "+tmpCodigoOdontograma;
				}
							
				consulta+=" WHERE codigo_pk = "+codigoPlanTrat+" ";
					
				if(valoracion>0)
				{
					consulta += " AND valoracion = "+valoracion+" ";
				}
				else
				{
					consulta += " AND evolucion = "+evolucion+" ";
				}
					
				ps =  new PreparedStatementDecorator(con,consulta);
				Log4JManager.info("Consulta: "+ps);
				if(ps.executeUpdate()>0)
					result = true;
				UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			}
			else
			{
				result = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//Log4JManager.info("valor para responder >> "+result);
		return result;
	}
	
	/**
	* 
	* @param con
	* @param codigoPlanTrat
	* @param valoracion
	* @param evolucion
	* @param usuario
	* @return
	*/
	public static boolean confirmarDetallePlanTratamiento(Connection con, int codigoPlanTrat, int valoracion, int evolucion, String usuario)
	{
		boolean result = false, deboActualizar = false;
		try
		{
			//************SE VERIFICA SI EXISTEN REGISTROS PENDIENTES POR CONFIRMAR*********************************
			String consulta = "SELECT count(1) as cuenta from odontologia.det_plan_tratamiento WHERE plan_tratamiento = ? ";
			if(valoracion>0)
			{
				consulta += " AND valoracion = "+valoracion;
			}
			if(evolucion>0)
			{
				consulta += " AND evolucion = "+evolucion;
			}
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			ps.setInt(1,codigoPlanTrat);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					deboActualizar = true;
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			//*******************************************************************************************************
			
			if(deboActualizar)
			{
				consulta = "UPDATE odontologia.det_plan_tratamiento SET " +
						"por_confirmar = '"+ConstantesBD.acronimoNo+"', " + // QUITAR 
						"usuario_modifica = '"+usuario+"', " +
						"fecha_modifica = CURRENT_DATE, " +
						"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
						"WHERE plan_tratamiento = "+codigoPlanTrat+" ";
				
				if(valoracion>0)
					consulta += " AND valoracion = "+valoracion+" ";
				else
					consulta += " AND evolucion = "+evolucion+" ";
				
				ps =  new PreparedStatementDecorator(con,consulta);
				Log4JManager.info("Consulta: "+ps);
				if(ps.executeUpdate()>0)
					result = true;
				UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			}
			else
			{
				result = true;
			}
			
			
			
		}catch (Exception e) {
		}
		return result;
	}
	
	/**
	* 
	* @param con
	* @param codigoDetPlanTrat
	* @param valoracion
	* @param evolucion
	* @param usuario
	* @return
	*/
	public static boolean confirmarProgServPlanTratamiento(Connection con, int codigoDetPlanTrat, int valoracion, int evolucion, String usuario)
	{
		boolean result = false, deboActualizar = false;
		try
		{
			//*****************SE VERIFICA SI EXISTEN REGISTROS PENDIENTES POR CONFIRMAR*********************************************
			String consulta = "SELECT count(1) as cuenta from odontologia.programas_servicios_plan_t WHERE det_plan_tratamiento = ? ";
			if(valoracion>0)
			{
				consulta += " AND valoracion = "+valoracion;
			}
			if(evolucion>0)
			{
				consulta += " AND evolucion = "+evolucion;
			}
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			ps.setInt(1,codigoDetPlanTrat);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					deboActualizar = true;
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			//***********************************************************************************************************************
			
			
			if(deboActualizar)
			{
				consulta = evolucion>0?"UPDATE odontologia.programas_servicios_plan_t SET " +
								"estado_servicio = case when estado_programa = '"+ConstantesIntegridadDominio.acronimoPorAutorizar+"' THEN '"+ConstantesIntegridadDominio.acronimoPorAutorizar+"' else estado_servicio end, " +
								"por_confirmar = '"+ConstantesBD.acronimoNo+"', " +
								"usuario_modifica = '"+usuario+"', " +
								"fecha_modifica = CURRENT_DATE, " +
								"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
								"WHERE det_plan_tratamiento = "+codigoDetPlanTrat+" "
							:"UPDATE odontologia.programas_servicios_plan_t SET " +
								"por_confirmar = '"+ConstantesBD.acronimoNo+"', " +
								"usuario_modifica = '"+usuario+"', " +
								"fecha_modifica = CURRENT_DATE, " +
								"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
								"WHERE det_plan_tratamiento = "+codigoDetPlanTrat+" ";
				
				if(valoracion>0)
					consulta += " AND valoracion = "+valoracion+" ";
				else
					consulta += " AND evolucion = "+evolucion+" ";
					
				ps =  new PreparedStatementDecorator(con,consulta);
				Log4JManager.info("Consulta: "+ps);
				if(ps.executeUpdate()>0)
					result = true;
				UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
				
			}
			else
			{
				result = true;
			}
		}catch (Exception e) {
		}
		
		Log4JManager.info("lo que se responde >> "+result);
		return result;
	}
	
	/**
	* metodo de actualizacion de estados programas servicios de otra evoluciones del plan de tratamietno
	* @param con
	* @param casoActualizacion
	* @param estado
	* @param aplica
	* @param usuario
	* @param codigoDetPlanTrat
	* @param codigoPrograma
	* @param codigoServicio
	* @param porConfirmar 
	* @return boolean
	*/
	public static boolean actualizacionEstadosPSOtrasEvoluciones(
			Connection con, 
			char casoActualizacion, 
			String estado, 
			String aplica,
			String estPrograma,
			String usuario,
			int codigoDetPlanTrat,
			int codigoPrograma,
			int codigoServicio,
			int motivo,
			int codigoCita,
			int valoracion,
			int evolucion, String porConfirmar)
	{
		
		Log4JManager.info("*********************************************************************************");
		Log4JManager.info("\n\n\n\n\n\n");
		Log4JManager.info("******************************************>>>actualizacionEstadosPSOtrasEvoluciones<<<***********");
		Log4JManager.info("\n\n\n\n\n\n");
		Log4JManager.info("*********************************************************************************");
		
		int tmpCodigoEspecialidad=cargarEspecialidadCita(codigoCita);
		
		/////
		boolean result = false;
		try{
			boolean bandActServProg = false;
			boolean bandPAUProg = false;
			boolean bandGarantiaProg = false;
			boolean cancelarProceso = false;
			boolean enProceso = true;
			boolean contratado = false;
			boolean enTransaccion = true;
			boolean exclusion = false;
			String where = " WHERE 1=1 ";
			String consulta = "UPDATE odontologia.programas_servicios_plan_t SET " +
				"usuario_modifica = ?, " +
				"fecha_modifica = CURRENT_DATE, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"por_confirmar = '"+porConfirmar+"' ";
			
			
				if(tmpCodigoEspecialidad>0)
				{
					consulta+=", especialidad="+tmpCodigoEspecialidad+" ";
				}
			

			// cosntrucion del la sentencia sql segun el caso de actualizacion
			switch (casoActualizacion) 
			{
			
				case ConstantesBD.acronimoRegistrarContratado:
				case ConstantesBD.acronimoRegistrarPendiente:
					consulta += ", estado_servicio = '"+estado+"' ";
					contratado = verificarEstServProg1(con, codigoDetPlanTrat, codigoPrograma, codigoServicio);
					break;
			
				case ConstantesBD.acronimoRegistroServicioEvolucionar:
				case ConstantesBD.acronimoRegistrarAtencionExterna:
					consulta += ", estado_servicio = '"+estado+"' ";
					// evaluar el estado de los servicios del programa para asi determinar
					// el estado al que evoluciona el programa.
				
					if(!verificarEstServProg(con, codigoDetPlanTrat, codigoPrograma, codigoServicio))
						consulta += " , estado_programa = '"+ConstantesIntegridadDominio.acronimoEnProceso+"' ";
					else
					{
						consulta += " , estado_programa = '"+ConstantesIntegridadDominio.acronimoTerminado+"' ";
						enProceso = false;
					}
					break;
					
				case ConstantesBD.acronimoRegistrarCancelaciones:
					consulta +=  aplica.equals(ConstantesIntegridadDominio.acronimoMostrarServicios)?", estado_servicio = '"+estado+"' ":" , estado_programa = '"+estado+"' ";
					if(aplica.equals(ConstantesIntegridadDominio.acronimoMostrarProgramas))
					{
						cancelarProceso = true;
					}
					
					consulta += " , motivo = "+motivo+" ";
					break;
				
				case ConstantesBD.acronimoRegistrarExclusiones:
					consulta += ", exclusion = '"+ConstantesBD.acronimoSi+"'  ";
					consulta +=  aplica.equals(ConstantesIntegridadDominio.acronimoMostrarServicios)?", estado_servicio = '"+estado+"' ":" , estado_programa = '"+estado+"' ";
					if(!estado.equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						consulta += ", estado_autorizacion = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' ";
					
					// si el programa es excluido sus servicios toman ese estado
					if(aplica.equals(ConstantesIntegridadDominio.acronimoMostrarProgramas))
					{
						bandActServProg = true;
					}
					
					if(motivo>0)
					{
						consulta += " , motivo = "+motivo+" ";
					}
					
					
					exclusion = true;
					break;
				case ConstantesBD.acronimoRegistrarInclusiones:
					consulta += ", inclusion = '"+ConstantesBD.acronimoSi+"'  ";
					consulta +=  aplica.equals(ConstantesIntegridadDominio.acronimoMostrarServicios)?", estado_servicio = '"+estado+"' ":", estado_programa = '"+estado+"' ";
					if(!estado.equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						consulta += ", estado_autorizacion = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' ";
					break;
				case ConstantesBD.acronimoRegistrarGarantias:
					consulta += ", garantia = '"+ConstantesBD.acronimoSi+"' ";
					if(!estado.equals(ConstantesIntegridadDominio.acronimoGarantia))
					{
						consulta +=  aplica.equals(ConstantesIntegridadDominio.acronimoMostrarServicios)?", estado_servicio = '"+estado+"' ":", estado_programa = '"+estado+"' ";
					}	
					if(!estado.equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						consulta += ", estado_autorizacion = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' ";
					
					if(aplica.equals(ConstantesIntegridadDominio.acronimoMostrarProgramas))
					{
						bandGarantiaProg = true;
					}
					if(motivo>0)
					{
						consulta += " , motivo = "+motivo+" ";
					}
					
					break;
				default:
					break;
			}
			
			// actualizar el estado del programa de los servicios asociados
			if(contratado)
			{
				enProceso = estado.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)?false:true;
				if(!actualizarEstadoProgramaServicios2(con, enProceso, ConstantesBD.acronimoRegistrarContratado, usuario, codigoDetPlanTrat, codigoPrograma, ConstantesBD.codigoNuncaValido, codigoCita, valoracion, evolucion))
					enTransaccion = false;
			}else if(bandPAUProg)
			{
				if(!actualizarEstadoProgramaServicios2(con, enProceso, ConstantesBD.acronimoAutorizarExcInc, usuario, codigoDetPlanTrat, codigoPrograma, exclusion?motivo:ConstantesBD.codigoNuncaValido , codigoCita, valoracion, evolucion))
					enTransaccion = false;
			}else if(cancelarProceso)
			{
				if(!actualizarEstadoProgramaServicios2(con, enProceso, ConstantesBD.acronimoRegistrarCancelaciones, usuario, codigoDetPlanTrat, codigoPrograma, motivo, codigoCita, valoracion, evolucion))
					enTransaccion = false;
			}else if(bandGarantiaProg)
			{
				if(!actualizarEstadoProgramaServicios2(con, enProceso, ConstantesBD.acronimoRegistrarGarantias, usuario, codigoDetPlanTrat, codigoPrograma, motivo, codigoCita, valoracion, evolucion))
					enTransaccion = false;
			}else if(!actualizarEstadoProgramaServicios2(con, enProceso, ConstantesBD.acronimoEnprocesoTerminado, usuario, codigoDetPlanTrat, codigoPrograma, ConstantesBD.codigoNuncaValido, codigoCita, valoracion, evolucion))
			{
					enTransaccion = false;
			}
			consulta+= ", cita = "+codigoCita+" ";
			if(valoracion>0)
				consulta+= ", valoracion = "+valoracion+" ";
			else if(evolucion>0)
				consulta+= ", evolucion = "+evolucion+" ";
			
			where +=" AND det_plan_tratamiento = ? " +
					" AND programa = ? ";
			where += aplica.equals(ConstantesIntegridadDominio.acronimoMostrarServicios)?" AND servicio = "+codigoServicio+" ":"";
			consulta+=where;
			// si se ha realizado satisfactoriamente la actualizacion del estado del programa 
			// se procede a realizar la actualizacion del estado del servicio
			if(enTransaccion)
			{		
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
				ps.setString(1, usuario);
				ps.setInt(2, codigoDetPlanTrat);
				ps.setInt(3, codigoPrograma);
				//ps.setInt(4, codigoServicio);
				Log4JManager.info("Consulta: "+ps);
				if(ps.executeUpdate()>0)
				{
					Log4JManager.info("ejecuta bien la consulta de actualizacion ");
					Log4JManager.info("bandActServProg: "+bandActServProg);
					if(bandActServProg)// se actualiza el estado de los servicios a excluidos dependiendo de un programa y det_plan_tratamiento
						result = actualizarServiciosPrograma(con,estado,codigoDetPlanTrat, codigoPrograma,tmpCodigoEspecialidad);
					else
						result = true;
				}
				ps.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		Log4JManager.info("result: "+result);
		return result;
	}

	/**
	* metodo que actualiza el estado del programa de los servicios asociados
	* @param con
	* @param enProceso
	* @param usuario
	* @param codigoPlanT
	* @param codigoPrograma
	* @return
	*/
	public static boolean actualizarEstadoProgramaServicios(Connection con, boolean enProceso, String usuario, int codigoPlanT, int codigoPrograma)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strActualizarEstadoProg);
			ps.setString(1, enProceso?ConstantesIntegridadDominio.acronimoEnProceso:ConstantesIntegridadDominio.acronimoTerminado);
			ps.setString(2, usuario);
			ps.setInt(3, codigoPlanT);
			ps.setInt(4, codigoPrograma);
			
			Log4JManager.info("\n\n ACTUALIZAR ESTADOS PROGRAMAS SERVICIOS SQL-> : "+ps+"\n\n\n\n\n");
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	* metodo que actualiza el estado del programa reversandolo al estado contrado o pendiente segun es caso
	* @param con
	* @param enProceso
	* @param usuario
	* @param codigoPlanT
	* @param codigoPrograma
	* @return
	*/
	public static boolean actualizarEstadoProgramaServicios1(Connection con, boolean enProceso, String usuario, int codigoPlanT, int codigoPrograma)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strActualizarEstadoProg);
			ps.setString(1, enProceso?ConstantesIntegridadDominio.acronimoContratado:ConstantesIntegridadDominio.acronimoEstadoPendiente);
			ps.setString(2, usuario);
			ps.setInt(3, codigoPlanT);
			ps.setInt(4, codigoPrograma);
			//Log4JManager.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	* verificar se el o los servicios del programa, alguno de ellos se encuentra agendado
	* @param detPlanTratamiento
	* @param codigoPrograma
	* @param codigoServicio
	* @param codigoCita
	* @return
	*/
	public static boolean verificarServiciosAgendados(int detPlanTratamiento, int codigoPrograma, int codigoServicio, int codigoCita)
	{
		boolean result = false;
		String consulta = "select " +
				"case when count(sco.codigo_pk)>0 then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as excluir " + 
				"from odontologia.servicios_cita_odontologica sco " +
				"where sco.activo = '"+ConstantesBD.acronimoSi+"' and sco.cita_odontologica <> ? and sco.programa_hallazgo_pieza in ( " +
				"	select php.codigo_pk " +
				"	from odontologia.programas_hallazgo_pieza php " +
				"   inner join odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
				"   INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
				"   INNER JOIN  odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk) " +
				
				"   where " +
				"   sxp.det_plan_trata = ? " +
				"	and php.programa = ? ";
		try{
			if(codigoServicio>0)
				consulta+="	and pspt.servicio = "+codigoServicio+"  ) ";
			else
				consulta+="	) ";
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoCita);
			ps.setInt(2, detPlanTratamiento);
			ps.setInt(3, codigoPrograma);
			//Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result = UtilidadTexto.getBoolean(rs.getString("excluir"));
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result; 
	}
	
	/**
	* metodo que actualiza el estado del programa reversandolo al estado contrado o pendiente segun es caso
	* @param con
	* @param enProceso
	* @param usuario
	* @param codigoPlanT
	* @param codigoPrograma
	* @return
	*/
	public static boolean actualizarEstadoProgramaServicios2(
			Connection con, 
			boolean enProceso,
			char casoActualizacion,
			String usuario, 
			int codigoPlanT, 
			int codigoPrograma,
			int motivo,
			int codigoCita,
			int valoracion,
			int evolucion)
	{
		boolean result = false;
		
		Log4JManager.info("\n\n\n\n actualizarEstadoProgramaServicios2 \n\n\n");
		
		int tmpCodigoEspecialidad=cargarEspecialidadCita(codigoCita);
		
		String consulta = motivo>0?"UPDATE odontologia.programas_servicios_plan_t SET " +
							"usuario_modifica = ?, " +
							"fecha_modifica = CURRENT_DATE, " +
							"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
							"estado_programa = ?, " +
							"motivo = ?, " +
							"cita = ? ," +
							"especialidad =?"
						:"UPDATE odontologia.programas_servicios_plan_t SET " +
							"usuario_modifica = ?, " +
							"fecha_modifica = CURRENT_DATE, " +
							"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
							"estado_programa = ?, " +
							"cita = ? ," +
							"especialidad =?";
							
		String where = "WHERE det_plan_tratamiento = ? " +
				"AND programa = ? ";
		try{
			if(valoracion>0)
				consulta+= ", valoracion = "+valoracion+" ";
			else if(evolucion>0)
				consulta+= ", evolucion = "+evolucion+" ";
			consulta+=where;
			
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setString(1, usuario);
			
			
			Log4JManager.info("casoActualizacion: "+casoActualizacion);
			
			switch (casoActualizacion) 
			{
			
			case ConstantesBD.acronimoRegistrarContratado:
				ps.setString(2, enProceso?ConstantesIntegridadDominio.acronimoContratado:ConstantesIntegridadDominio.acronimoEstadoPendiente);
				break;
			
			case ConstantesBD.acronimoAutorizarExcInc:
				ps.setString(2, ConstantesIntegridadDominio.acronimoPorAutorizar);
				if(motivo>0)
					ps.setInt(3, motivo);
				break;
			
			case ConstantesBD.acronimoRegistrarCancelaciones:
				ps.setString(2, ConstantesIntegridadDominio.acronimoEstadoCancelado);
				ps.setInt(3, motivo);
				break;
			
			case ConstantesBD.acronimoEnprocesoTerminado:
				ps.setString(2, enProceso?ConstantesIntegridadDominio.acronimoEnProceso:ConstantesIntegridadDominio.acronimoTerminado);
				break;
			
			case ConstantesBD.acronimoRegistrarGarantias:
				ps.setString(2, ConstantesIntegridadDominio.acronimoPorAutorizar);
				ps.setInt(3, motivo);
				break;
			default:
				break;
			}
			
			
			if(motivo>0)
			{
				ps.setInt(4, codigoCita);
				ps.setInt(5, tmpCodigoEspecialidad);
				ps.setInt(6, codigoPlanT);
				ps.setInt(7, codigoPrograma);
				
			}else{
				ps.setInt(3, codigoCita);
				ps.setInt(4, tmpCodigoEspecialidad);
				ps.setInt(5, codigoPlanT);
				ps.setInt(6, codigoPrograma);
			}
			
			Log4JManager.info("\n\n\n\n MODIFICACION DE PROGRAMAS SERVICIOS PLAN TRATAMIENTO \n\n\n"+ps+"\n\n\n\n\n\n");
			
			if(ps.executeUpdate()>0)
			{
				result = true;
			}
			ps.close();
		
			cancelarServiciosProgramasPlanT(con, codigoPlanT, codigoPrograma, motivo, tmpCodigoEspecialidad, codigoCita, casoActualizacion, usuario );
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * Metodo que Cancela los Servicios de los programas del Plan Tratamiento con lo siguientes estados con los siguientes estados:
	 * Pendientes y Contratados 
	 * @param codigoPlanT
	 * @param codigoPrograma
	 * @param motivo
	 * @param especialidad
	 * @return
	 */
	public static boolean cancelarServiciosProgramasPlanT(Connection con ,int codigoPlanT, int codigoPrograma, int motivo, int especialidad, int codigoCita, char casoActualizacion ,String usuario )
	{
		boolean retorno=Boolean.FALSE;
	
		if(  (codigoPlanT>0) && (codigoPrograma>0) ) // Existe codigoDetPlan y codigoProgrma
		{
			
			if( ConstantesBD.acronimoRegistrarCancelaciones==casoActualizacion )
			{
			
				String consulta=" " +
				 
							"Update odontologia.programas_servicios_plan_t " +
							" set estado_servicio='"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"' ," +
							" cita="+codigoCita+", "+
							" especialidad="+especialidad+","+
							" usuario_modifica ='"+usuario+"'," +
							" fecha_modifica = CURRENT_DATE, " +
							" hora_modifica ="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
							" where estado_servicio in('"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"','"+ConstantesIntegridadDominio.acronimoContratado+"' ) and " +
							" programa="+codigoPrograma+" and  det_plan_tratamiento="+codigoPlanT ;
				
				Log4JManager.info("\n\n\n\n MODIFICAR CANCELAR PROGRAMAS Y SERVICIOS PLAN DE TRATAMIENTO \n-->"+consulta+" \n\n\n");
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
				try 
				{	
					if(ps.executeUpdate()>0)
					{
						retorno = Boolean.TRUE;
					}
					ps.close();
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		return retorno;
	}
	
	
	
	
	/**
	 * CARGA EL CODIGO DE LA ESPECIALIDAD DE LA UNIDAD DE AGENDA TABLA: consultaexterna.unidades_consulta
	 * 
	 * @param codigoPkCita
	 * @return
	 */
	public static int cargarEspecialidadCita(int codigoPkCita)
	{
		int codigoEspecialidad=0;
		
		if(codigoPkCita>0)
		{
			String consulta="select  uc.especialidad  as especialidad " +
								"from  odontologia.citas_odontologicas co inner join" +
								"		 odontologia.agenda_odontologica ao on(ao.codigo_pk=co.agenda and co.codigo_pk= ? ) " +
								"inner join " +
								"		consultaexterna.unidades_consulta uc on(ao.unidad_agenda=uc.codigo) " ;
			try
			{
				Connection con = UtilidadBD.abrirConexion();
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
				ps.setInt(1, codigoPkCita);
				
				Log4JManager.info("\n\n\n CONSULTAR ESPECIALIDAD DE LA CITA ODONTOLOGICA\n\n\n");
				Log4JManager.info("SQL-\n\n"+ps+"\n\n\n\n");
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					codigoEspecialidad= rs.getInt("especialidad");
				}
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else 
		{
			codigoEspecialidad= 0;
		}
		Log4JManager.info("****-   ESPECIALIDAD -->"+codigoEspecialidad+"\n\n\n");

		return codigoEspecialidad;
	}

	/**
	 * CARGA EL CODIGO DE LA ESPECIALIDAD DE LA UNIDAD DE AGENDA TABLA: consultaexterna.unidades_consulta
	 * 
	 * @param codigoPkCita
	 * @return
	 */
	private static double  cargarCodigoOdontograma(double evolucion)
	{
		
		
		String consulta="select codigo_pk as codigoOdontograma  from odontologia.odontograma where evolucion= ?";
		double codigoOdontograma=ConstantesBD.codigoNuncaValidoDouble;
		
		
			try
			{
				Connection con = UtilidadBD.abrirConexion();
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
				ps.setDouble(1, evolucion);
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					codigoOdontograma=rs.getDouble("codigoOdontograma");
				}
				
				
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		
		
	
		return codigoOdontograma;
	}
	
	
	
	/**
	* metodo que actualiza los servicios de un programa especifico 
	* @param con
	* @param estado
	* @param codigoDetPlanTrat
	* @param codigoPrograma
	* @return boolean
	*/
	public static boolean actualizarServiciosPrograma(Connection con, String estado, int codigoDetPlanTrat, int codigoPrograma, int codigoEspecialidad)
	{
		boolean result = false;
		try{
			String consulta = "UPDATE odontologia.programas_servicios_plan_t SET " +
					"estado_servicio = ? ";
					if(codigoEspecialidad>0)
					{
						consulta+=" , especialidad="+codigoEspecialidad;
					}
					consulta+=" WHERE det_plan_tratamiento = ? " +
					"AND programa = ? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setString(1, estado);
			ps.setInt(2, codigoDetPlanTrat);
			ps.setInt(3, codigoPrograma);
			Log4JManager.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	* metodo que verifica el estado de los servicios del programa
	* @param con
	* @param codigoDetPlanTrat
	* @param codigoPrograma
	* @param codigoServicio
	* @return boolean
	*/
	public static boolean verificarEstServProg(Connection con, int codigoDetPlanTrat, int codigoPrograma, int codigoServicio)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strVerificarEstSerProg);
			ps.setInt(1, codigoDetPlanTrat);
			ps.setInt(2, codigoPrograma);
			ps.setInt(3, codigoServicio);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result = UtilidadTexto.getBoolean(rs.getString("terminar_prog"));
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		Log4JManager.info("result: "+result);
		return result;
	}
	

	/**
	* metodo que verifica el estado de los servicios del programa
	* @param con
	* @param codigoDetPlanTrat
	* @param codigoPrograma
	* @param codigoServicio
	* @return boolean
	*/
	public static boolean verificarEstServProg1(Connection con, int codigoDetPlanTrat, int codigoPrograma, int codigoServicio)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strVerificarEstSerProg1);
			ps.setInt(1, codigoDetPlanTrat);
			ps.setInt(2, codigoPrograma);
			ps.setInt(3, codigoServicio);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result = UtilidadTexto.getBoolean(rs.getString("contratado_prog"));
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		Log4JManager.info("result: "+result);
		return result;
	}
	
	/**
	 * @deprecated
	* Mï¿½todo que carga el encabezado del historial de ocnfirmacion del plan de tratamiento
	* @param con
	* @param infoPlanTrat
	*/
	public static void consultarHisConfPlanTratamiento(Connection con,InfoPlanTratamiento infoPlanTrat)
	{
		try
		{
			String consulta = consultarHisConfPlanTratamientoStr;
			
			if(infoPlanTrat.getValoracion().longValue()>0)
			{
				consulta += " hcpt.valoracion = ?";
			}
			else if(infoPlanTrat.getEvolucion().longValue()>0)
			{
				consulta += " hcpt.evolucion = ?";
			}
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
		
			if(infoPlanTrat.getValoracion().longValue()>0)
			{
				pst.setBigDecimal(1, infoPlanTrat.getValoracion());
			}
			else if(infoPlanTrat.getEvolucion().longValue()>0)
			{
				pst.setBigDecimal(1, infoPlanTrat.getEvolucion());
			}
			
			Log4JManager.info("\n\n Consulta consultarHisConfPlanTratamiento >>> "+pst);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				infoPlanTrat.setCodigoPk(rs.getBigDecimal("plan_tratamiento"));
				infoPlanTrat.setPorConfirmar(rs.getString("por_confirmar"));
				infoPlanTrat.setValoracion(rs.getBigDecimal("valoracion"));
				infoPlanTrat.setEvolucion(rs.getBigDecimal("evolucion"));
				infoPlanTrat.setImagen(rs.getString("imagen")); //la tiene el odontograma
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en consultarHisConfPlanTratamiento: ",e);
		}
	}
	
	/**
	 * CARGA LAS PIEZAS del historial de confirmacion
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasHisConf(Connection con,InfoPlanTratamiento infoPlanTrat, String seccion)
	{
		String consultaStr="SELECT DISTINCT " +
			"d.pieza_dental as pieza_dental, " +
			"p.descripcion as descripcion, " +
			"d.por_confirmar as por_confirmar " +
			"FROM odontologia.his_conf_det_plan_t d " +
			"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=d.pieza_dental) " +
			"WHERE d.plan_tratamiento=? and seccion= ?";
	
		
		if(infoPlanTrat.getValoracion()!=null && infoPlanTrat.getValoracion().longValue()>0)
		{
			consultaStr += " and d.valoracion = ?";
		}
		else if(infoPlanTrat.getEvolucion()!=null && infoPlanTrat.getEvolucion().longValue()>0)
		{
			consultaStr += " and d.evolucion = ?";
		}
		
		
		
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		
		
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1,infoPlanTrat.getCodigoPk());
			ps.setString(2,seccion);
			if(infoPlanTrat.getValoracion()!=null && infoPlanTrat.getValoracion().longValue()>0)
			{
				ps.setBigDecimal(3, infoPlanTrat.getValoracion());
			}
			else if(infoPlanTrat.getEvolucion()!=null && infoPlanTrat.getEvolucion().longValue()>0)
			{
				ps.setBigDecimal(3, infoPlanTrat.getEvolucion());
			}
			
			Log4JManager.info("obtenerPiezas--->"+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt(1));
				info.setNombre(rs.getString(2));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	
	
	/**
	* Obtiene los hallazgos 
	* @return
	*/
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHisConf(Connection con,DtoDetallePlanTratamiento parametros)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		
		String consultaStr = consultarHallazgosOdoHisConfStr;
		
		if(parametros.getValoracion()!=null && parametros.getValoracion().longValue()>0)
		{
			consultaStr += " AND d.valoracion = ? ";
		}
		else if(parametros.getEvolucion()!=null && parametros.getEvolucion().longValue()>0)
		{
			consultaStr += " AND d.evolucion = ? ";
		}
		
		
		if(parametros.getPiezaDental() > 0)
		{
			consultaStr += " AND d.pieza_dental = ?";
		}
		
		if(!parametros.getSeccion().equals(""))
		{
			consultaStr += " AND d.seccion = ? ";
		}
		
		consultaStr+= " ORDER BY d.superficie ASC ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setDouble(1, parametros.getPlanTratamiento());
			int contador = 2;
			
			if(parametros.getValoracion()!=null && parametros.getValoracion().longValue()>0)
			{
				ps.setBigDecimal(contador, parametros.getValoracion());
				contador++;
			}
			else if(parametros.getEvolucion()!=null &&  parametros.getEvolucion().longValue()>0)
			{
				ps.setBigDecimal(contador, parametros.getEvolucion());
				contador++;
			}
			if(parametros.getPiezaDental() > 0)
			{
				ps.setInt(contador,parametros.getPiezaDental());
				contador++;
			}
			
			if(!parametros.getSeccion().equals(""))
			{
				ps.setString(contador,parametros.getSeccion());
				contador++;
			}
			
			Log4JManager.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.getClasificacion().setValue(rs.getString("clasificacion"));
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.getHallazgoREQUERIDO().setActivo(true);
				hallazgoSuperficie.setPorConfirmar(rs.getString("por_confirmar"));
				hallazgoSuperficie.getInfoRegistroHallazgo().setFechaModifica(rs.getString("fechamodifica"));
				hallazgoSuperficie.getInfoRegistroHallazgo().setHoraModifica(rs.getString("horamodifica"));
				
				if(!rs.getString("pathconvencion").equals(""))
				{
					hallazgoSuperficie.getHallazgoREQUERIDO().setDescripcion(CarpetasArchivos.CONVENCION.getRutaAbsoluta(parametros.getPath())+""+rs.getString("pathconvencion"));
				}
				
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.getSuperficieOPCIONAL().setCodigo2(rs.getInt("sector"));
				hallazgoSuperficie.getSuperficieOPCIONAL().setActivo(true);
				
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
				
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr,e);
		}
			
		return array;
	}
	
	
	/**
		
	 * Consulta los programas y servicios del detalle del plan de tratamiento
	 * Mirando las tablas de historial de confirmacion
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHisConf(Connection con,DtoProgramasServiciosPlanT parametros) 
	{
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		String consultaStr = "", despuesWhere = "";
	   
        String codigoTarifario=parametros.getCodigoTarifario();
		
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
		{
			consultaStr =  "SELECT DISTINCT " +
							"pro.codigo as codigo ," +
							"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
							"pro.codigo_programa as codigoamostrar, " +
							"plant.por_confirmar," +
							"plant.estado_programa as est_prog  " +
							"FROM " +
							"odontologia.his_conf_prog_serv_plan_t plant " +
							"INNER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
							"WHERE " +
							"plant.det_plan_tratamiento="+parametros.getDetPlanTratamiento()+" ";
			
			/*if(parametros.getEstadosProgramasOservicios().size() >0 )
				consultaStr += "and plant.estado_programa in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(parametros.getEstadosProgramasOservicios())+") ";*/
			
			
		}
		else
		{
			consultaStr = 	"SELECT DISTINCT " +
							"plant.codigo_pk as codigopk," +
							"plant.servicio as codigoServicio ," +
							"getnombreservicio(plant.servicio)  as nombreServicio, " +
							"getcodigoservicio(plant.servicio, "+codigoTarifario+") as codigoamostrar, " +
							"plant.por_confirmar," +
							"plant.estado_servicio as est_serv," +
							"coalesce(plant.cita,"+ConstantesBD.codigoNuncaValido+") as cita, " +
							"coalesce(plant.valoracion,"+ConstantesBD.codigoNuncaValido+") as valoracion, " +
							"coalesce(plant.evolucion,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +
							"coalesce(plant.inclusion,'') as inclusion, " +
							"coalesce(plant.exclusion,'') as exclusion, " +
							"coalesce(plant.garantia,'') as garantia " +
							"FROM " +
							"odontologia.programas_servicios_plan_t plant " +
							"WHERE " +
							"plant.programa IS NOT NULL AND det_plan_tratamiento="+parametros.getDetPlanTratamiento()+" AND plant.activo = '"+ConstantesBD.acronimoSi+"' ";
			
			/*if(parametros.getEstadosProgramasOservicios().size() >0 )
				consultaStr += "and plant.estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(parametros.getEstadosProgramasOservicios())+") ";*/
			
			despuesWhere = " ORDER BY plant.orden_servicio ASC ";
		}
		
		if(parametros.getValoracion()!=null && parametros.getValoracion().longValue()>0)
		{
			consultaStr += " AND plant.valoracion = "+parametros.getValoracion().longValue();
		}
		else if(parametros.getEvolucion()!=null && parametros.getEvolucion().longValue()>0)
		{
			consultaStr += " AND plant.evolucion = "+parametros.getEvolucion().longValue();
		}
		
		
		
		try 
		{
			consultaStr += despuesWhere;
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(parametros.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
			{
				DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
				parametrosServ.setDetPlanTratamiento(parametros.getDetPlanTratamiento());
				
				while(rs.next())
				{
					InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.getExisteBD().setValue(ConstantesBD.acronimoSi);
					info.getExisteBD().setActivo(true);
					info.setPorConfirmar(rs.getString("por_confirmar"));
					info.setEstadoPrograma(rs.getString("est_prog"));
					parametrosServ.getPrograma().setCodigo(rs.getBigDecimal(1).doubleValue());
					parametrosServ.setCodigoTarifario(codigoTarifario);
					// parametrosServ.setPorConfirmado(ConstantesBD.acronimoNo);
					info.setListaServicios(cargarServiciosDeProgramasPlanTHisConf(con,parametrosServ));
					array.add(info);
				}
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
			}
			else
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				
				while(rs.next())
				{
					InfoServicios serv = new InfoServicios();
					serv.setCodigoPkProgServ(rs.getBigDecimal("codigopk"));
					serv.setCodigoMostrar(rs.getString("codigoamostrar"));
					serv.getServicio().setCodigo(rs.getInt("codigoServicio"));
					serv.getServicio().setNombre(rs.getString("nombreServicio"));
					serv.getExisteBD().setValue(ConstantesBD.acronimoSi);
					serv.setPorConfirmar(rs.getString("por_confirmar"));
					serv.setEstadoServicio(rs.getString("est_serv"));
					serv.setCodigoCita(new BigDecimal(rs.getInt("cita")));
					serv.setCodigoValoracion(new BigDecimal(rs.getInt("valoracion")));
					serv.setCodigoEvolucion(new BigDecimal(rs.getInt("evolucion")));
					serv.setInclusion(rs.getString("inclusion"));
					serv.setExclusion(rs.getString("exclusion"));
					serv.setGarantia(rs.getString("garantia"));
					serv.getExisteBD().setActivo(true);
					info.getListaServicios().add(serv);
				}
				
				array.add(info);
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
			}
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		return array;
	}
	
	/**
	 * Carga los servicios de un programa Plant Tratamiento
	 * @return
	 */
	private static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanTHisConf(Connection con,DtoProgramasServiciosPlanT parametros)
	{
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
		String order = "";
        String codigoTarifario=parametros.getCodigoTarifario();
		
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		String consultaStr= "SELECT DISTINCT " +
							"plant.codigo_pk as codigopk," +
							"plant.servicio as codigoServicio ," +
							"getnombreservicio(plant.servicio, "+codigoTarifario+")  as nombreServicio, " +
							"getcodigoservicio(plant.servicio, "+codigoTarifario+") as  codigoamostrar ," +
							"plant.estado_servicio as estadoServicio, " +
							"plant.orden_servicio as orden_serv, " +
							"coalesce(plant.cita,"+ConstantesBD.codigoNuncaValido+") as cita, " +
							"coalesce(plant.valoracion,"+ConstantesBD.codigoNuncaValido+") as valoracion, " +
							"coalesce(plant.evolucion,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +
							"plant.por_confirmar, "+
							"coalesce(plant.inclusion,' ') as inclusion, " +
							"coalesce(plant.exclusion,' ') as exclusion, " +
							"coalesce(plant.garantia,' ') as garantia " +
							"FROM  " +
							"odontologia.his_conf_prog_serv_plan_t plant " +
							"WHERE plant.servicio IS NOT NULL  ";
		
		if(parametros.getDetPlanTratamiento().intValue() > 0)
			consultaStr += " AND plant.det_plan_tratamiento = "+parametros.getDetPlanTratamiento().intValue()+" ";
		
		if(parametros.getPrograma().getCodigo() > 0)
			consultaStr +=  " AND plant.programa = "+parametros.getPrograma().getCodigo()+" ";
		
		if(parametros.getValoracion()!=null && parametros.getValoracion().longValue()>0)
		{
			consultaStr += " AND plant.valoracion = "+parametros.getValoracion().longValue();
		}
		else if(parametros.getEvolucion()!=null && parametros.getEvolucion().longValue()>0)
		{
			consultaStr += " AND plant.evolucion = "+parametros.getEvolucion().longValue();
		}
	
		order = " ORDER BY plant.orden_servicio ";
		consultaStr += order;  
		
		//Log4JManager.info("valor de la cadena >> "+consultaStr);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				info.setCodigoPkProgServ(rs.getBigDecimal("codigopk"));
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.setPorConfirmar(rs.getString("por_confirmar"));
				info.setEstadoServicio(rs.getString("estadoServicio"));
				info.getExisteBD().setValue(ConstantesBD.acronimoSi);
				info.getExisteBD().setActivo(true);
				info.setOrderServicio(rs.getInt("orden_serv"));
				info.setCodigoCita(new BigDecimal(rs.getInt("cita")));
				info.setCodigoValoracion(new BigDecimal(rs.getInt("valoracion")));
				info.setCodigoEvolucion(new BigDecimal(rs.getInt("evolucion")));
				info.setInclusion(rs.getString("inclusion").equals(" ")?"":rs.getString("inclusion"));
				info.setExclusion(rs.getString("exclusion").equals(" ")?"":rs.getString("exclusion"));
				info.setGarantia(rs.getString("garantia").equals(" ")?"":rs.getString("garantia"));
				listaServicios.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
		
		return listaServicios;
	}
	
	
	/**
	 * Obtiene los hallazgos para las seccion de Otros y Boca
	 * @return
	 */
	public static ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(Connection con,DtoDetallePlanTratamiento parametros)
	{
		ArrayList<InfoDetallePlanTramiento>  array= new ArrayList<InfoDetallePlanTramiento>();
		
		String consultaStr = consultarHallazgosOdoHisConfStr;
		
		if(parametros.getValoracion()!=null &&  parametros.getValoracion().longValue()>0)
		{
			consultaStr += " AND d.valoracion = ? ";
		}
		else if(parametros.getEvolucion()!=null && parametros.getEvolucion().longValue()>0)
		{
			consultaStr += " AND d.evolucion = ? ";
		}
		
		if(parametros.getPiezaDental() > 0)
			consultaStr += " AND d.pieza_dental = "+parametros.getPiezaDental();
		
		if(!parametros.getSeccion().equals(""))
			consultaStr += " AND d.seccion = '"+parametros.getSeccion()+"' ";

		/*
		if(!parametros.getActivo().equals(""))
			consultaStr += " AND d.activo = '"+parametros.getActivo()+"' ";
		 */			
		
		//
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setDouble(1,parametros.getPlanTratamiento());
			if(parametros.getValoracion()!=null && parametros.getValoracion().longValue()>0)
			{
				ps.setBigDecimal(2, parametros.getValoracion());
			}
			else if(parametros.getEvolucion()!=null && parametros.getEvolucion().longValue()>0)
			{
				ps.setBigDecimal(2, parametros.getEvolucion());
			}
			
			//Log4JManager.info("obtenerHallazgosSuperficiesSeccionOtrayBoca >> "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDetallePlanTramiento pieza = new InfoDetallePlanTramiento();
				pieza.getPieza().setCodigo(rs.getInt("piezadental"));
				pieza.getPiezaOld().setCodigo(rs.getInt("piezadental"));
				pieza.getExisteBD().setActivo(true);
				pieza.getExisteBD().setValue(ConstantesBD.acronimoSi);
				pieza.setCodigoPkDetalle(new BigDecimal(rs.getInt("codigodetallepk")));
				
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.getClasificacion().setValue(rs.getString("clasificacion"));
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setHallazgoREQUERIDOOld(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.getHallazgoREQUERIDO().setActivo(true);
				hallazgoSuperficie.setPorConfirmar(rs.getString("por_confirmar"));
				hallazgoSuperficie.getInfoRegistroHallazgo().setFechaModifica(rs.getString("fechamodifica"));
				hallazgoSuperficie.getInfoRegistroHallazgo().setHoraModifica(rs.getString("horamodifica"));
				
				if(!rs.getString("pathconvencion").equals(""))
						hallazgoSuperficie.getHallazgoREQUERIDO().setDescripcion(CarpetasArchivos.CONVENCION.getRuta()+""+rs.getString("pathconvencion"));
				
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.getSuperficieOPCIONAL().setCodigo2(rs.getInt("sector"));
				hallazgoSuperficie.setSuperficieOPCIONALOld(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.getSuperficieOPCIONALOld().setCodigo2(rs.getInt("sector"));
				hallazgoSuperficie.getSuperficieOPCIONAL().setActivo(true);
				
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
				
				pieza.getDetalleSuperficie().add(hallazgoSuperficie);
				array.add(pieza);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
			
		return array;
	}
		
	
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	
	
	public static boolean guardarServArtIncPlant(Connection con,DtoServArtIncCitaOdo dto)
	{
		boolean retorna= false; 
		
		
		double codigo_pk = ConstantesBD.codigoNuncaValidoDouble;
		String insertStr=" INSERT INTO " +
								"odontologia.serv_art_inc_cita_odo ( "+
									"codigo_pk , "+
									"servicio_cita_odo ,"+ 
									"servicio , "+
									"articulo , "+
									"cantidad, "+
									"solicitud,"+
									"fecha_modifica , "+
									"hora_modifica , "+
									"usuario_modifica)"+ 
								"values (" +
									"?, " +
									"?, " +
									"?, " +
									"?, " +
									"?, " +
									"?, " +
									" CURRENT_DATE, " +
									ValoresPorDefecto.getSentenciaHoraActualBD()+", "+
									"?" +
								")";
		
		
		try
		{		
			codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_serv_art_inc_p");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , insertStr);
			
			ps.setDouble(1,codigo_pk);
			
			if (dto.getServicioCitaOdo().compareTo(BigDecimal.ZERO)!=0)
				ps.setBigDecimal(2,dto.getServicioCitaOdo());
			else
				ps.setNull(2, Types.DOUBLE);
				
				
			if (dto.getServicio().getCodigo()>0)
			{
				ps.setInt(3, dto.getServicio().getCodigo());
			}
			else
			{
				ps.setNull(3,Types.INTEGER);
			}
			
			if(dto.getArticulo().getCodigo()>0)
			{
				ps.setInt(4, dto.getArticulo().getCodigo());
			}
			else
			{
				ps.setNull(4,Types.INTEGER);
			}
			
			ps.setInt(5, dto.getCantidad());
			ps.setInt(6, dto.getSolicitud().getCodigo());
			ps.setString(7, dto.getUsuarioModifica().getUsuarioModifica());
			Log4JManager.info(ps);
			retorna=ps.executeUpdate()>0;
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			
		}
		catch (SQLException  e) {
			Log4JManager.info(UtilidadLog.obtenerString(dto,true), e);
		}
		
		return retorna;
	}
	
	/**
	 * 
	 * @return
	 */
	public static  ArrayList<DtoServArtIncCitaOdo> cargarServArtIncPlanT(DtoServArtIncCitaOdo dto, BigDecimal codigoDetallePlanTratamiento, int institucion){
		ArrayList<DtoServArtIncCitaOdo>listaDto = new ArrayList<DtoServArtIncCitaOdo>();
			
		 Log4JManager.info(" CARGANDO SERVICIOS ARTICULO INCLUIDO PLAN DE TRATAMIENTO ");
		 
		String consultaStr=" select	saic.codigo_pk as codigoPk, " +
										"saic.servicio  as servicio," +
										"getnombreservicio(saic.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+")  as nombreServicio, "+
										"saic.articulo as articulo," +
										"getdescripcionarticulo(saic.articulo) as nombreArticulo , " + 
										"saic.cantidad as cantidad," + 
										"saic.solicitud as solicitud , " +
										"saic.fecha_modifica as fechaModica ," + 
										"saic.hora_modifica as horaModifica , " +
										"saic.usuario_modifica as usuarioModifica " +
										"FROM "+
										" odontologia.servicios_cita_odontologica sco " +
										" INNER JOIN odontologia.serv_art_inc_cita_odo saic ON (sco.codigo_pk=saic.servicio_cita_odo) " +
										"	where sco.programa_hallazgo_pieza in (select php.codigo_pk from odontologia.programas_hallazgo_pieza php inner join superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) inner join det_plan_tratamiento dpt on (dpt.codigo_pk=sxp.det_plan_trata) inner join odontologia.programas_servicios_plan_t pspt on (pspt.det_plan_tratamiento=dpt.codigo_pk) where pspt.servicio="+dto.getServicio().getCodigo()+" and pspt.det_plan_tratamiento="+codigoDetallePlanTratamiento+" )" +
										"	and  sco.activo='"+ConstantesBD.acronimoSi+"'";
									
									
									
		Log4JManager.info("CONSULTA");
		Log4JManager.info(consultaStr);
			
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoServArtIncCitaOdo dtoServArt = new DtoServArtIncCitaOdo();
				dtoServArt.setCodigo_pK(rs.getBigDecimal("codigoPk"));
				dtoServArt.getServicio().setCodigo("servicio");
				dtoServArt.getServicio().setNombre("nombreServicio");
				dtoServArt.getArticulo().setCodigo(rs.getInt("articulo"));
				dtoServArt.getArticulo().setNombre("nombreArticulo");
				dtoServArt.setCantidad(rs.getInt("cantidad"));
				dtoServArt.getSolicitud().setCodigo(rs.getInt("solicitud"));
				dtoServArt.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				dtoServArt.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				dtoServArt.getUsuarioModifica().setFechaModifica(rs.getString("fechaModica"));
				listaDto.add(dtoServArt);
			}
			Log4JManager.info("CONSULTA "+ps);
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
		return listaDto;	
		
	}
	
	
	/**
	* ACCION CARGAR SERVICIO ARTICULOS CITA ODON
	* @param codigoServicio
	* @param codigoDetallePlanT
	* @return
	*/
	public static  boolean cargarServArtIncCitaOdo( int codigoServicio , BigDecimal detPlanTratamiento ){
		
		boolean retorna= false;	
		Log4JManager.info("\n\n\n\n\n\n \n\n\n\n\n\n \n\n\n\n\n\n " +
				"CARGANDO SERVICIOS ARTICULO INCLUIDO PLAN DE TRATAMIENTO \n\n\n");
		 /*
		String consultaStr=" select	servart.codigo_pk as codigoPk from  odontologia.serv_art_inc_cita_odo servart  where 1=1 "  ; 
		consultaStr+= codigoServicio>0?" AND servart.servicio= "+codigoServicio : "";
		consultaStr+= codigoDetallePlanT.doubleValue() >0?" AND  servart.programa_serv_plan_t="+codigoDetallePlanT: "";
		*/
		String consultaStr=" select sco.codigo_pk " +
								" from odontologia.servicios_cita_odontologica sco " +
								" INNER JOIN odontologia.serv_art_inc_cita_odo saic ON (sco.codigo_pk=saic.servicio_cita_odo) " +
								"	where sco.programa_hallazgo_pieza in (select php.codigo_pk from odontologia.programas_hallazgo_pieza php " +
								" inner join odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
								" inner join odontologia.det_plan_tratamiento dpt on (dpt.codigo_pk=sxp.det_plan_trata) " +
								" inner join odontologia.programas_servicios_plan_t pspt on (pspt.det_plan_tratamiento=dpt.codigo_pk) where pspt.servicio="+codigoServicio+" and pspt.det_plan_tratamiento="+detPlanTratamiento+" )" +
								"	and  sco.activo='"+ConstantesBD.acronimoSi+"'";
		
		Log4JManager.info("**********************************************************************************************************");
		Log4JManager.info("	CODIGO SERVICIO---->"+codigoServicio+"        PLAN ---->"+detPlanTratamiento);
		Log4JManager.info("**********************************************************************************************************");
		Log4JManager.info("CONSULTA");
		Log4JManager.info(consultaStr);
		
		Log4JManager.info("\n\n\n\n\n");	
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
			{
				retorna= true;
			}
			
			if(retorna)
			{
				Log4JManager.info("**********************************************************************************************************");
				Log4JManager.info("SIiiiiiiiiiiiiiiiiiiiiiiiii ENCUENTRA -------------------");
				Log4JManager.info("-*********************************************************************************************************");
			}
			else
			{
				Log4JManager.info("**********************************************************************************************************");
				Log4JManager.info(" NOoooooooooooooooooooooooooo              ENCUENTRA -------------------");
				Log4JManager.info("-*********************************************************************************************************");
			}
			Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");	
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param listapkProgramasServiciosPlant
	 * @return
	 */
	public static  boolean validarCargosGeneradosProgramasServicios( ArrayList<String> listapkProgramasServiciosPlant, Connection con) {
		
		boolean retornar= true;
		Log4JManager.info("******************************************************************************************");
		Log4JManager.info(" ------------------2.  VALIDAR CARGOS GENERADOS FACTURADOS PARA LOS PROGRAMAS Y SERVICIOS ----------");
		Log4JManager.info("******************************************************************************************");
		
		
		
		
		String consultaStr= " select car.facturado as facturado " +
												" from facturacion.det_cargos car , " +
												" INNER JOIN odontologia.servicios_cita_odontologica sco on(sco.servicio=car.servicio and sco.numero_solicitud=car.solicitud) " +
												" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
												" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
												" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
												" INNER JOIN  odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and sco.servicio=pspt.servicio AND pspt.activo = 'S' ) " +
												" where pspt.codigo_pk in ("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(listapkProgramasServiciosPlant)+ ")"; 
		
		
		Log4JManager.info("consulta  SQL \n\n\n\n\n"+consultaStr);
		
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				if (rs.getString("facturado").equals(ConstantesBD.acronimoNo))
				{
					retornar= false;
				}
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
		return retornar; 
	}
	
	/**
	 * 
	 * @param listapkProgramasServiciosPlant
	 * @return
	 */
	public static boolean validarCargos( ArrayList<String> listapkProgramasServiciosPlant) {
		
		Log4JManager.info("******************************************************************************************");
		Log4JManager.info(" ------------------ 1. VALIDAR CARGOS GENERADOS PARA LOS PROGRAMAS Y SERVICIOS ----------");
		Log4JManager.info("******************************************************************************************");
		boolean retorna=true;
		
		
		
		String consultaStr= " select psp.codigo_pk as codigoProgramaServicioPlan, " +
									"ser.codigo_pk  as codigoServicioCargo	" +
							"from odontologia.programas_servicios_plan_t psp " +
				" LEFT OUTER JOIN odontologia.servicios_cita_odontologica ser ON(ser.codigo_pk=psp.codigo_pk) where psp.codigo_pk in " +
				"("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(listapkProgramasServiciosPlant)+ ")"; 
		
		
		Log4JManager.info("consulta  SQL \n\n\n\n\n"+consultaStr);
		Connection con = null;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try 
		{
			con = UtilidadBD.abrirConexion();
			ps =  new PreparedStatementDecorator(con,consultaStr );
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				 if ( rs.getDouble("codigoServicioCargo")<=0)
				 {
					 retorna=false;  
				 }
			}
			Log4JManager.info("	validar 2. ");
			if (retorna)
			{
				Log4JManager.info("************************************************************************************************************");
				Log4JManager.info("----------------------- ENCUENTRA CARGOS PARA LOS CODIGOS ------------------------------------------------");
				Log4JManager.info("**********************************************************************************************************");
				if (!validarCargosGeneradosProgramasServicios( listapkProgramasServiciosPlant ,con))
				{
					Log4JManager.info("*******************************************");
					Log4JManager.info("NO SE ENCONTRARON CARGOS FACTURADOS");
					retorna=false; 
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			Log4JManager.error("error en carga==> "+e+" "+consultaStr);
		}
		
		
		return retorna;
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param programa
	 * @param piezaDental
	 * @param superficie
	 * @param hallazgo
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(BigDecimal planTratamiento, Double programa, Integer piezaDental, Integer superficie, Integer hallazgo, int institucion)
	{
		ArrayList<InfoServicios> retorna= new ArrayList<InfoServicios>();
		String consulta=	" SELECT " +
								"pspt.codigo_pk as cod, " +
								"pspt.servicio as servicio, " +
								"getnombreservicio(pspt.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as nomserv, " +
								"orden_servicio as orden, " +
								"s.minutos_duracion as duracion "+
							"FROM " +
								"odontologia.programas_servicios_plan_t pspt " +
								"INNER JOIN odontologia.det_plan_tratamiento dpt ON(dpt.codigo_pk=pspt.det_plan_tratamiento) " +
								"INNER JOIN facturacion.servicios s ON (s.codigo = pspt.servicio) "+
							"where " +
								"dpt.plan_tratamiento=? "+
								"and pspt.programa=? ";
								
		consulta+= (piezaDental==null || piezaDental<=0)? "and dpt.pieza_dental is null ":"and dpt.pieza_dental ="+piezaDental+" ";
		consulta+= (superficie==null || superficie<=0)? "and dpt.superficie is null ":"and dpt.superficie ="+superficie+" ";
		consulta+= (hallazgo==null || hallazgo<=0)? "and dpt.hallazgo is null ":"and dpt.hallazgo ="+hallazgo+" ";
		consulta+=	" and dpt.activo='"+ConstantesBD.acronimoSi+"' " +
					" and dpt.por_confirmar= '"+ConstantesBD.acronimoNo+"' ";
		consulta+=" order by pspt.orden_servicio ";
		
		
		Log4JManager.info("consulta ------------->"+consulta+"     plan->"+planTratamiento+"  prog->"+programa);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setBigDecimal(1, planTratamiento);
			ps.setDouble(2, programa);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios info= new InfoServicios();
				info.setCodigoPkProgServ(rs.getBigDecimal("cod"));
				info.setOrderServicio(rs.getInt("orden"));
				info.setServicio(new InfoDatosInt(rs.getInt("servicio"), rs.getString("nomserv")));
				info.setDuracionCita(rs.getInt("duracion"));
				retorna.add(info);
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return retorna;
	}
	
	

	/**
	 * 
	 * @param pieza
	 * @param hallazgo
	 * @param superficie
	 * @param seccion
	 * @return
	 */
	public static BigDecimal obtenerDetPlanTratamiento(BigDecimal planTratamiento, BigDecimal pieza, BigDecimal hallazgo, BigDecimal superficie, String seccion) 
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr=" SELECT " +
								"d.codigo_pk " +
							"from " +
								"odontologia.det_plan_tratamiento d " +
							"WHERE " +
								"1=1 ";
		
		consultaStr+=(pieza.doubleValue()>0)?" and pieza_dental="+pieza+" ":" and pieza_dental is null ";
		consultaStr+=(superficie.doubleValue()>0)?" and superficie="+superficie+" ":" and superficie is null ";
		consultaStr+=" and hallazgo= "+hallazgo+" ";
		consultaStr+= (UtilidadTexto.isEmpty(seccion))?" AND seccion='"+seccion+"' ":" "; 
		consultaStr+= " AND activo='"+ConstantesBD.acronimoSi+"' AND plan_tratamiento=? ";
		
		try	
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, planTratamiento);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			if(rs.next())
			{
				retorna=rs.getBigDecimal(1);	
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPlanTratamiento
	 * @param utilizaProgramas
	 * @return
	 */
	public static boolean existenProgramasServiciosContratadosNoTerminados(Connection con, BigDecimal codigoPkPlanTratamiento, boolean utilizaProgramas)
	{
		boolean retorna=false;
		String consultaStr=" SELECT " +
								"count(1) as contador " +
							"from " +
								"odontologia.plan_tratamiento pt " +
								"inner join odontologia.det_plan_tratamiento dpt on(dpt.plan_tratamiento=pt.codigo_pk) " +
								"inner join odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
							"WHERE " +
								"pt.codigo_pk=? " +
								"and pspt.activo= '"+ConstantesBD.acronimoSi+"' ";
		
		consultaStr+=(utilizaProgramas)?" and pspt.estado_programa in ('"+ConstantesIntegridadDominio.acronimoContratado+"') ": " and pspt.estado_servicio in ('"+ConstantesIntegridadDominio.acronimoContratado+"') ";
		
		try	
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, codigoPkPlanTratamiento);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			if(rs.next())
			{
				retorna= rs.getInt("contador")>0;	
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return retorna;
	}

	/**
	 * Verifica la existencia de almenos una pieza activa
	 * @param codigoPkPlanTratamiento C&oacute;digo del plan de tratamiento
	 * @param piezaDental C&oacute;digo de la pieza dental
	 * @param seccion Secci&oacute;n que se desea verificar
	 * @return true en caso de tener elementos activos
	 */
	public static boolean tieneAlgunaSuperficieActiva(BigDecimal codigoPkPlanTratamiento, int piezaDental, String seccion)
	{
		String sentenciaSql=
				"SELECT " +
					"count(d.activo) AS resultados " +
				"FROM " +
					"odontologia.det_plan_tratamiento d " +
				"INNER JOIN " +
					"odontologia.pieza_dental p " +
						"ON(p.codigo_pk=d.pieza_dental) " +
				"WHERE " +
						"d.plan_tratamiento=? " +
					"AND " +
						"d.seccion=?" +
					"AND " +
						"d.pieza_dental=? " +
					"AND activo=?";
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaSql);
			psd.setInt(1, codigoPkPlanTratamiento.intValue());
			psd.setString(2, seccion);
			psd.setInt(3, piezaDental);
			psd.setString(4, ConstantesBD.acronimoSi);
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			int resultado=0;
			try{
				if(rsd.next())
				{
					resultado=rsd.getInt("resultados");
				}
				rsd.close();
			}
			catch (SQLException eRsd) {
				Log4JManager.error("Error obteniendo el resultSet",eRsd);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
			return resultado>0;
		} 
		catch (SQLException e)
		{
			Log4JManager.error("Error consultando al menos una superficie activa del diente",e);
			Log4JManager.error("Error cerrando el prerparedStatement",e);
			return false;
		}
	}




	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public static InfoDatosDouble obtenerPlanTratamientoXCuenta(BigDecimal cuenta) 
	{
		InfoDatosDouble retorna= new InfoDatosDouble();
		
		String consultaStr=	"SELECT " +
								"p.codigo_pk  as codigo, " +
								"p.estado as estado " +
							"from " +
								"odontologia.plan_tratamiento p " +
							"where " +
								"p.ingreso= (select c.id_ingreso from cuentas c where c.id=?) " +
								"and p.estado in('"+ConstantesIntegridadDominio.acronimoEstadoActivo+"', '"+ConstantesIntegridadDominio.acronimoEstadoEnProceso+"', '"+ConstantesIntegridadDominio.acronimoTerminado+"') " +
								"order by p.codigo_pk desc ";
		try	
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, cuenta);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

			if(rs.next())
			{
				retorna= new InfoDatosDouble(rs.getDouble(1), rs.getString(2));	
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return retorna;
	}
	
	
	
	
	
	

	
	/**
	 *METODO QUE CONSULTA LOS PLANES DE TRATAMIENTO HISTORICOS.
	 *RECIBE UN DTO
	 *Y RETORNA UN ARRAY DE PLANES DE TRATAMIENTO.
	 * 
	 * @param Connection con
	 * @param DtoPlanTratamientoOdo parametros
	 * */
	public static ArrayList<DtoPlanTratamientoOdo> consultarPlanTratamientoHistorico(DtoPlanTratamientoOdo parametros)
	{
	
		
		/*
		 * CARGAR EL MAXIMO CODIGO PLAN TRATAMIENTO HISTORICO
		 */
		BigDecimal codigologPlan=consultarPlanTratamientoHistoricoMaximo(parametros);
	
		/**
		* Cadena Sql para Consultar los Histï¿½ricos del plan de tratamiento
		*/
		   String consultaLogPlanTratamientoHistorico = "SELECT " +
																		 " lp.codigo_pk as codigoPk ,"+
																		 " lp.plan_tratamiento as planTratamiento, "+ 
																		 " lp.estado as estado ,"+          
																		 " lp.motivo as motivo ,"+         
																		 " lp.especialidad as especialidad ,"+ 
																		 " lp.codigo_medico as codigoMedico , "+ 
																		 " lp.por_confirmar  as porConfirmar ,"+
																		 " lp.cita  as cita ,"+ 
																		 " lp.fecha as fecha ,"+
																		 " lp.hora as hora ,"+
																		 " lp.valoracion as valoracion,"+ 
																		 " lp.evolucion as evolucion  ,"+
																		 " lp.imagen as imagen ,"+           
																		 " lp.usuario_modifica as usuario_modifica , " +
																		 " lp.odontograma_diagnostico as odontogramaDiagnostico ," +
																		 " lp.ingreso as ingreso "+ 
																		 "from  odontologia.log_plan_tratamiento lp  where 1=1 "; 
		
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoPlanTratamientoOdo> listHistoricosPlan= new ArrayList<DtoPlanTratamientoOdo>();
		

		
		String consulta = consultaLogPlanTratamientoHistorico;
		
		
		consulta +=(codigologPlan.doubleValue()>0)?" AND lp.codigo_pk ="+codigologPlan: " ";
		// TODO COLOCAR LA SEGURIDAD RESPECTIVA SI NO CARGAR UN CODIGO PK
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			Log4JManager.info("Historico Plan tratamiento"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				DtoPlanTratamientoOdo dtoTratamiento = new DtoPlanTratamientoOdo();
				dtoTratamiento.setCodigoPlanHistorico(rs.getBigDecimal("codigoPk"));
				dtoTratamiento.setCodigoPk(rs.getBigDecimal("planTratamiento"));
				dtoTratamiento.setEstado(rs.getString("estado"));
				dtoTratamiento.setMotivo(rs.getInt("motivo"));
				dtoTratamiento.setIngreso(rs.getInt("ingreso"));
				dtoTratamiento.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad")));
				dtoTratamiento.setPorConfirmar(rs.getString("porConfirmar"));
				dtoTratamiento.setCodigoCita(rs.getBigDecimal("cita"));
				dtoTratamiento.setFechaGrabacion(rs.getString("fecha"));
				dtoTratamiento.setHoraGrabacion(rs.getString("hora"));
				dtoTratamiento.setCodigoValoracion(rs.getBigDecimal("valoracion"));
				dtoTratamiento.setCodigoEvolucion(rs.getBigDecimal("evolucion"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramaDiagnostico"));
				Log4JManager.info("-->"+dtoTratamiento.getCodigoPk());
				Log4JManager.info("-->"+dtoTratamiento.getEstado());
				listHistoricosPlan.add(dtoTratamiento);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (Exception e) 
		{
			Log4JManager.info("Error-consultarPlanTratamientoHistorico-Consulta: "+e+"   "+consulta);
		}
		
		return listHistoricosPlan;
	}
	
	
	
	
	/**
	 * CARGA LAS PIEZAS PARA DETALLE PLAN DE TRATAMIENTO HISTORICO  
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasHistorico(DtoDetallePlanTratamiento dtoDetallePlan)
	{
		
		String consultaStr= "select distinct " +
								 " ld.pieza_dental as pieza_dental , " +
								 " p.descripcion as descripcion " +
								 " from odontologia.log_det_plan_tratamiento ld " +
								 "	inner join " +
								 " odontologia.pieza_dental p ON(p.codigo_pk=ld.pieza_dental)" +
								 " where 1=1 " +
								 "and ld.log_plan_tratamiento=? "; 
								
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoDetallePlan.getSeccion())?" and ld.seccion='"+dtoDetallePlan.getSeccion()+"'":""; 
		consultaStr+= !UtilidadTexto.isEmpty(dtoDetallePlan.getPorConfirmar())?"and  ld.por_confirmar='"+dtoDetallePlan.getPorConfirmar()+"' ": " ";
		//TODO HAY QUE TENER ENCUENTA LA INSTITUCION
		consultaStr+=" order by p.descripcion desc ";
		
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setDouble(1, dtoDetallePlan.getPlanTratamiento());
			Log4JManager.info("CARGAR PIEZAS> \n"+ps+"\n");
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt("pieza_dental"));
				info.setNombre(rs.getString("descripcion"));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 * @deprecated 
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento, 
																						DtoDetallePlanTratamiento dtoDetallePlan, 
																						boolean utilizaProgramas, 
																						DtoProgramasServiciosPlanT dtoProgramaServicios,
																						int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		String consultaStr="SELECT " +
								"d.codigo_pk as codigodetallepk, " +
								"d.det_plan_tratamiento as codigoDetallePlan , " +
								"d.hallazgo as codigohallazgo, " +
								"h.nombre as nombrehallazgo, " +
								"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie " +
							"FROM " +
								"odontologia.log_det_plan_tratamiento d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN " +
									"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
								"LEFT OUTER JOIN " +
									"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"WHERE " +
								" d.log_plan_tratamiento="+dtoPlanTratamiento.getCodigoPlanHistorico()+" " +
								" and d.por_confirmar='"+dtoDetallePlan.getPorConfirmar()+"' "+
								" and d.pieza_dental="+dtoDetallePlan.getPiezaDental() ;
		
				consultaStr+=(UtilidadTexto.isEmpty(dtoDetallePlan.getSeccion()))?" ":" AND d.seccion='"+dtoDetallePlan.getSeccion()+"'";	
				
				
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			Log4JManager.info("consulta"+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigoDetallePlan"));
				//CARGAR LOS PROGRAMAS O SERVICIOS --------------
				dtoDetallePlan.setCodigo(rs.getDouble("codigoDetallePlan"));// encapsular
				dtoDetallePlan.setCodigoPkDetalleHistorico((rs.getDouble("codigodetallepk")));
				hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosHistoricos( dtoDetallePlan, 
																								dtoProgramaServicios,
																								utilizaProgramas ,
																								institucion));
				
				
				
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return array;
	}
	
	
	
	/**
	 * 
	 * @param bigDecimal
	 * @param estadosProgramasOservicios
	* @param utilizaProgramas 
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHistoricos(
																	DtoDetallePlanTratamiento dtoDetPlan, 
																	DtoProgramasServiciosPlanT dtoProgramaServicio,
																	boolean utilizaProgramas,
																	int institucion
																	) 
	{
		
        String codigoTarifario=dtoProgramaServicio.getCodigoTarifario();
		
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		dtoDetPlan.setCodigoTarifarioBusqServ(codigoTarifario);
		
		Log4JManager.info("\n---------------------------------------CARGANDO PROGRAMAS O SERVICIOS  HISTORICOS -----------------------");
		Log4JManager.info("***********************************************************************************************************");
		
		ArrayList<InfoProgramaServicioPlan> array= new ArrayList<InfoProgramaServicioPlan>();
		String consultaStr="";
		
		// SI UTILIZA PROGRAMAS SERVICIOS
		if(utilizaProgramas)
		{
			consultaStr= "SELECT DISTINCT " +
				"pro.codigo as codigo ," +
				"pro.codigo_programa  ||' '|| pro.nombre  as nombre, " +
				"pro.codigo_programa as codigoamostrar ,"+
				"plant.estado_programa as estadoPrograma ," +
				"plant.inclusion as inclusion, " +
				"plant.exclusion as exclusion," +
				"plant.garantia as garantia ," +
				"plant.activo as activo ," +
				"plant.indicativo_programa as indicativoPrograma ," +
				"plant.estado_autorizacion as estadoAutorizacion ," +
				"plant.por_confirmar as porConfirmar, " +
				" (	" +
						"SELECT " +
							"coalesce(dhps.numero_superficies,0) " +
						"FROM " +
							"odontologia.hallazgos_vs_prog_ser hvps " +
							"INNER JOIN odontologia.det_hall_prog_ser dhps ON(dhps.hallazgo_vs_prog_ser=hvps.codigo)  " +
						"WHERE " +
							"hvps.hallazgo=dpt.hallazgo and dhps.programa=pro.codigo" +
				" ) as numerosuperficies "+
				" "+
			"FROM " +
				" odontologia.log_programas_servicios_plan_t plant " +
				" INNER JOIN odontologia.log_det_plan_tratamiento dpt ON (dpt.codigo_pk=plant.log_det_plan) " +
				" LEFT OUTER JOIN odontologia.programas pro ON (plant.programa=pro.codigo) " +
			"WHERE " +
				" plant.log_det_plan="+dtoDetPlan.getCodigoPkDetalleHistorico()+" " +
				"  and plant.por_confirmar='"+dtoDetPlan.getPorConfirmar()+"'"; // SIEMPRE ES POR CONFIRMAR EN NO
				
		}
	
		//CARGAR POR SERVICIOS
		else
		{
			consultaStr+= "SELECT DISTINCT " +
							" plant.servicio as codigoServicio ," +
							" getnombreservicio(plant.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+")  as nombreServicio , " +
							" getcodigoservicio(plant.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as  codigoamostrar , " +
							" plant.inclusion as inclusion, " +
							" plant.exclusion as exclusion," +
							" plant.garantia as garantia ," +
							" plant.activo as activo ," +
							" plant.indicativo_programa as indicativoPrograma ," +
							" plant.estado_autorizacion as estadoAutorizacion ," +
							" plant.estado_servicio as estadoServicio ," +
							" plant.por_confirmar as porConfirmar "+ 
							
							" "+
						"FROM " +
								"odontologia.log_programas_servicios_plan_t plant " +
						"WHERE " +
								"plant.log_det_plan="+dtoDetPlan.getCodigoPkDetalleHistorico()+" " +
								"  and plant.por_confirmar='"+dtoDetPlan.getPorConfirmar()+"'"; // SIEMPRE ES POR CONFIRMAR EN NO";
								
		}
		//FILTROS
	  
		consultaStr+= !UtilidadTexto.isEmpty(dtoProgramaServicio.getInclusion())?" and plant.inclusion='"+dtoProgramaServicio.getInclusion()+"' ": " ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoProgramaServicio.getExclusion())?" and plant.exclusion='"+dtoProgramaServicio.getExclusion()+"' ": " ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoProgramaServicio.getGarantia())?" and plant.garantia='"+dtoProgramaServicio.getGarantia()+"' ": " ";
		
		
		Log4JManager.info("CONSULTA PROGRAMAS \n\n\n\n-->"+consultaStr);
		Log4JManager.info(" \n\n\n\n\n\n\n\n");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				if(utilizaProgramas)
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoPrograma(rs.getString("estadoPrograma"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					
					//cargar los servicio del plan de tratamientos
					info.setListaServicios(cargarServiciosDeProgramasPlanTHistorico(dtoDetPlan,rs.getBigDecimal("codigo") , institucion ));
					info.setNumeroSuperficies(rs.getInt("numerosuperficies"));
					
				 }
				else
				{
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoServicio"));
					info.setNombreProgramaServicio(rs.getString("nombreServicio"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
					info.setEstadoAutorizacion(rs.getString("estadoAutorizacion"));
					info.setPorConfirmar(rs.getString("porConfirmar"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					info.setEstadoServicio(rs.getString("estadoServicio"));
				}
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		
		return array;
	}
	
	
	
	
	
	/**
	 * CARGA LOS SERVICIOS DE UN PROGRAMA HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanTHistorico(DtoDetallePlanTratamiento dtoDetallePlan, BigDecimal codigoPrograma, int institucion ){
		Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n**************************************************************/************************************************************");
		Log4JManager.info("******************************************OBTENER SERVICIOS DEL PROGRAMA **************************************************");
		
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>();
		String consultaStr= "SELECT DISTINCT " +
								"plant.servicio as codigoServicio ," +
								" getnombreservicio(plant.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+")  as nombreServicio , " +
								" getcodigoservicio(plant.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as  codigoamostrar , " +
								"plant.estado_servicio as estadoServicio"+
							" from " +
								" odontologia.log_programas_servicios_plan_t  plant " +
							"where " +
								" plant.log_det_plan="+dtoDetallePlan.getCodigoPkDetalleHistorico() +"" + //MALO setCodigoPkDetalleHistorico
								" AND plant.por_confirmar='"+dtoDetallePlan.getPorConfirmar()+"'  " + // SIEMPRE ES POR CONFIRMAR NO
								" AND plant.programa="+codigoPrograma.doubleValue()+"" +
								" AND plant.servicio IS NOT NULL ";
								
		
		Log4JManager.info("****************************************	CONSULTA SERVICIOS HISTORICOS ****************************************");
		Log4JManager.info(consultaStr);						
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				//cargar los servicio del plan de tratamientos
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				info.setEstadoServicio(rs.getString("estadoServicio"));
				listaServicios.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}		

		return listaServicios;
	}
	
	
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCAHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento , 
																				DtoDetallePlanTratamiento dtoDetallePlan ,  
																				boolean utilizaProgramas, 
																				DtoProgramasServiciosPlanT dtoProgramaServicioPlan ,
																				int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		Log4JManager.info("\n ********************************************************************************************************************************");
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		
		String consultaStr="SELECT " +
								"d.codigo_pk as codigodetallepk," +
								"d.det_plan_tratamiento as codigoDetallePlan , " +
								"d.hallazgo as codigohallazgo, " +
								"h.nombre as nombrehallazgo, " +
								"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(s.nombre, '') as nombresuperficie " +
							"FROM " +
								"odontologia.log_det_plan_tratamiento d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN  historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							"WHERE " +
								" d.plan_tratamiento="+dtoPlanTratamiento.getCodigoPk()+" " +
								" and d.por_confirmar='"+dtoDetallePlan.getPorConfirmar()+"'" ; // SIEMPRE ES POR CONFIRMAR EN NO
		
		consultaStr+=	"and d.seccion='"+dtoDetallePlan.getSeccion()+"' ";
		
		Log4JManager.info("obtenerHallazgosSuperficies    \n--->"+consultaStr+"\n");
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
			
				dtoDetallePlan.setCodigo(rs.getDouble("codigodetallepk"));
				dtoDetallePlan.setCodigoPkDetalleHistorico(rs.getDouble("codigoDetallePlan"));
				
				hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosHistoricos(dtoDetallePlan,  dtoProgramaServicioPlan ,utilizaProgramas , institucion ));
				array.add(hallazgoSuperficie);
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
	}
	
	
	
	/**
	 * CARGAR LA PIEZAS PARA EL PLAN DE TRATAMIENTO INCLUSIONES
	 */
	public static  ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantiasHistorico(DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento  )
	{
		Log4JManager.info("****************************************************************************************************");
		Log4JManager.info("****************************************************************************************************");
		Log4JManager.info("CARGAR PIEZAS INCLUSION o Garantias");
		Log4JManager.info("****************************************************************************************************");
		String consultaStr="SELECT DISTINCT d.pieza_dental, p.descripcion FROM odontologia.log_det_plan_tratamiento d " +
							"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=d.pieza_dental) INNER JOIN " +
							"odontologia.log_programas_servicios_plan_t progServ on(progServ.det_plan_tratamiento=d.codigo_pk) " +
							"where  1=1 ";
		
		consultaStr+=" AND d.plan_tratamiento= "+codigoPlanTratamiento;
		consultaStr+=(dto.getCodigoPk().doubleValue()>0)?" AND progServ.codigo_pk= "+dto.getCodigoPk():" ";
		consultaStr+= UtilidadTexto.isEmpty(dto.getGarantia())?"":" AND  progServ.garantia='"+dto.getGarantia()+"' ";
		consultaStr+= UtilidadTexto.isEmpty(dto.getInclusion())?"":" AND  progServ.inclusion='"+dto.getInclusion()+"'";
		consultaStr+= UtilidadTexto.isEmpty(dto.getEstadoAutorizacion())? "": " AND  progServ.estado_autorizacion='"+dto.getEstadoAutorizacion()+"'";
		consultaStr+=!UtilidadTexto.isEmpty(dto.getPorConfirmado())?" AND progServ.por_confirmar='"+dto.getPorConfirmado()+"'": " ";
		//consultaStr+= " AND progServ.activo='"+dto.getActivo()+"'";
		
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		
		Log4JManager.info("OBTENER PIEZAS PARA LAS INCLUSIONES o GARANTIAS--->\n\n\n"+consultaStr+"\n\n\n");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt(1));
				info.setNombre(rs.getString(2));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return array;
		
	}
	
	/**
	 * Metodo para obtener la hallazgos de una superficie utilizando:
	 *  DtoPlanTratamiento
	 * 	DtoDetallePlanTratamiento
	 *	DtoProgramasServiciosPlanT
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTOHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento , 
																							DtoDetallePlanTratamiento dtoDetallePlanT,  
																							DtoProgramasServiciosPlanT dtoProgramasServicios, 
																							boolean utilizaProgramas ,
																							int institucion)
	{
		ArrayList<InfoHallazgoSuperficie>  array= new ArrayList<InfoHallazgoSuperficie>();
		Log4JManager.info("\n ********************************************************************************************************************************");
		Log4JManager.info("\n CARGANDO obtenerHallazgosSuperficies  -----------------------");
		
		
		String consultaStr="SELECT " +
								" d.codigo_pk as codigodetallepk, " +
								" d.det_plan_tratamiento as codigoDetallePlan , "  +
								" d.hallazgo as codigohallazgo, " +
								" h.nombre as nombrehallazgo, " +
									"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
								"coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie " +
							"FROM " +
								"odontologia.log_det_plan_tratamiento  d " +
								"INNER JOIN odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
								"LEFT OUTER JOIN " +
									"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
								"LEFT OUTER JOIN " +
									"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"WHERE " +
								" d.plan_tratamiento="+dtoPlanTratamiento.getCodigoPk()+" " +
								" and d.pieza_dental="+dtoDetallePlanT.getPiezaDental()+" "+
								" and d.por_confirmar="+dtoDetallePlanT.getPorConfirmar(); // SIEMPRE ES POR CONFIRMAR EN NO 
				//FILTROS
				consultaStr+=(UtilidadTexto.isEmpty(dtoDetallePlanT.getSeccion()))?"":" AND d.seccion='"+dtoDetallePlanT.getSeccion()+"'";	
				
		
		Log4JManager.info("obtenerHallazgosSuperficies   --->"+consultaStr);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			// CARGA TODOS LOS HALLAZGOS Y LOS DETALLES DEL PLAN DE TRATAMIENTO
			while(rs.next())
			{
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigodetallepk"));
				dtoDetallePlanT.setCodigo(rs.getDouble("codigodetallepk"));
				dtoDetallePlanT.setCodigoPkDetalleHistorico(rs.getDouble("codigoDetallePlan"));
				//CARGAR LOS PROGRAMAS O SERVICIOS 
				hallazgoSuperficie.setProgramasOservicios(obtenerProgramasOServiciosHistoricos(dtoDetallePlanT,dtoProgramasServicios, utilizaProgramas , institucion));
				array.add(hallazgoSuperficie);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
		return array;
	}

	/**
	 * Elimina todas las relaciones entre programas de N superficies
	 * @param con Conexión con la BD
	 * @param planTratamiento Código del plan de tratamiento para el cual se van a eliminar las relaciones
	 * @param seccion Sección para la cual se va a eliminar la información
	 * @return retorna true en caso de éxito, false en caso de error
	 */
	public static boolean eliminarRelacionSuperficiesProgramas(Connection con, int planTratamiento, String seccion)
	{
		String codigosNoEliminables="";
		
		/* 
		 * Se buscan los programas que no se pueden eliminar debido a que están relacionados
		 * en una cita
		 */
		codigosNoEliminables = codigosNoEliminablesCitas(con, planTratamiento, seccion, codigosNoEliminables);
		
		/* 
		 * Se buscan los programas que no se pueden eliminar debido a que fueron migrados
		 */
		codigosNoEliminables = codigosNoEliminablesMigrados(con, planTratamiento, seccion, codigosNoEliminables);

		/* 
		 * Se buscan los programas que no se pueden eliminar debido a que tienen relación de inclusiones
		 */
		codigosNoEliminables = codigosNoEliminablesInclusiones(con, planTratamiento, seccion, codigosNoEliminables);

		
		String sentenciaCodigosNoEliminables="";
		if(!codigosNoEliminables.isEmpty())
		{
			sentenciaCodigosNoEliminables=" AND php.codigo_pk NOT IN("+codigosNoEliminables+")";
		}
		
		String sentenciaEliminarConsentimientoInfo="DELETE FROM odontologia.consentimiento_info_odonto coninfo WHERE coninfo.programa_hallazgo_pieza IN(SELECT php.codigo_pk FROM odontologia.programas_hallazgo_pieza php WHERE php.plan_tratamiento=? AND php.seccion=?"+sentenciaCodigosNoEliminables+")";
		String sentenciaEliminarDetalle="DELETE FROM odontologia.superficies_x_programa sxp WHERE sxp.prog_hallazgo_pieza IN(SELECT php.codigo_pk FROM odontologia.programas_hallazgo_pieza php WHERE php.plan_tratamiento=? AND php.seccion=?"+sentenciaCodigosNoEliminables+")";
		String sentenciaEliminarEncabezado="DELETE FROM odontologia.programas_hallazgo_pieza php WHERE php.plan_tratamiento=? AND seccion=?"+sentenciaCodigosNoEliminables;
		
		try{
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaEliminarConsentimientoInfo);
			psd.setInt(1, planTratamiento);
			psd.setString(2, seccion);
			psd.executeUpdate();
			psd.close();
			
			psd=new PreparedStatementDecorator(con, sentenciaEliminarDetalle);
			psd.setInt(1, planTratamiento);
			psd.setString(2, seccion);
			psd.executeUpdate();
			psd.close();

			psd=new PreparedStatementDecorator(con, sentenciaEliminarEncabezado);
			psd.setInt(1, planTratamiento);
			psd.setString(2, seccion);
			psd.executeUpdate();
			psd.close();

			return true;
			
		}catch (SQLException e) {
			Log4JManager.error("Error eliminando las relaciones de las superficies por programa", e);
		}
		return false;
	}

	/**
	 * @param con
	 * @param planTratamiento
	 * @param seccion
	 * @param codigosNoEliminables
	 * @return
	 */
	private static String codigosNoEliminablesInclusiones(Connection con,
			int planTratamiento, String seccion, String codigosNoEliminables)
	{
		String buscarHallazgosSuperficieNumeroContrato=
			"SELECT " +
				"php.codigo_pk AS codigo_pk " +
			"FROM " +
				"odontologia.programas_hallazgo_pieza php " +
			"INNER JOIN " +
				"odontologia.inclusiones_presupuesto ip " +
					"ON (ip.programa_hallazgo_pieza=php.codigo_pk)" +
			"WHERE " +
					"php.plan_tratamiento=? " +
				"AND " +
					"seccion=? ";

		return codigosNoEliminables+extraerCodigosNoEliminables(con, planTratamiento, seccion, codigosNoEliminables, buscarHallazgosSuperficieNumeroContrato);
	}




	/**
	 * Busca los códigos de los programas hallazgo pieza que no
	 * estén asociados a las citas
	 * @param con Conexión con la BD
	 * @param planTratamiento Código del plan de tratamiento
	 * @param seccion Sección a la que aplica
	 * @param codigosNoEliminables Variable donde se almacenarán los codigos no eliminables
	 * @return codigosNoEliminables Códigos no eliminables anteriores mas los nuevos
	 * @author Juan David Ramírez
	 * @since 13 Enero 2011
	 */
	private static String codigosNoEliminablesMigrados(Connection con, int planTratamiento, String seccion, String codigosNoEliminables)
	{
		String buscarHallazgosSuperficieNumeroContrato=
			"SELECT " +
				"php.codigo_pk AS codigo_pk " +
			"FROM " +
				"odontologia.programas_hallazgo_pieza php " +
			"INNER JOIN " +
				"odontologia.progr_contrato_estado_migrado pcem " +
					"ON (pcem.programa_hallazgo_pieza=php.codigo_pk)" +
			"WHERE " +
					"php.plan_tratamiento=? " +
				"AND " +
					"seccion=? ";

		return codigosNoEliminables+extraerCodigosNoEliminables(con, planTratamiento, seccion, codigosNoEliminables, buscarHallazgosSuperficieNumeroContrato);
	}

	/**
	 * Busca los códigos de los programas hallazgo pieza que no
	 * estén asociados a las citas
	 * @param con Conexión con la BD
	 * @param planTratamiento Código del plan de tratamiento
	 * @param seccion Sección a la que aplica
	 * @param codigosNoEliminables Variable donde se almacenarán los codigos no eliminables
	 * @return codigosNoEliminables Códigos no eliminables anteriores mas los nuevos
	 * @author Juan David Ramírez
	 * @since 13 Enero 2011
	 */
	private static String codigosNoEliminablesCitas(Connection con, int planTratamiento, String seccion, String codigosNoEliminables)
	{
		String buscarHallazgosSuperficie=
			"SELECT " +
				"php.codigo_pk AS codigo_pk " +
			"FROM " +
				"odontologia.programas_hallazgo_pieza php " +
			"INNER JOIN " +
				"odontologia.servicios_cita_odontologica sco " +
					"ON (sco.programa_hallazgo_pieza=php.codigo_pk)" +
			"WHERE " +
					"php.plan_tratamiento=? " +
				"AND " +
					"seccion=? ";

		codigosNoEliminables+=extraerCodigosNoEliminables(con, planTratamiento, seccion, codigosNoEliminables, buscarHallazgosSuperficie);

		buscarHallazgosSuperficie=
			"SELECT " +
				"php.codigo_pk AS codigo_pk " +
			"FROM " +
				"odontologia.programas_hallazgo_pieza php " +
			"INNER JOIN " +
				"odontologia.log_servicios_cita_odo sco " +
					"ON (sco.programa_hallazgo_pieza=php.codigo_pk)" +
			"WHERE " +
					"php.plan_tratamiento=? " +
				"AND " +
					"seccion=? ";

		codigosNoEliminables+=extraerCodigosNoEliminables(con, planTratamiento, seccion, codigosNoEliminables, buscarHallazgosSuperficie);
		
		return codigosNoEliminables;
	}


	/**
	 * Extrae y almacena en una variable separada por comas los códigos no
	 * eliminables resultado de la consulta enviada por parámetros
	 * @param con Conexión con la BD
	 * @param planTratamiento Código del plan de tratamiento
	 * @param seccion Sección a la que aplica
	 * @param codigosNoEliminables Variable donde se almacenarán los codigos no eliminables
	 * @param buscarHallazgosSuperficie Sentencia enviada para la búsqueda
	 * @return codigosNoEliminables Códigos no eliminables anteriores mas los nuevos
	 * @author Juan David Ramírez
	 * @since 13 Enero 2011
	 */
	private static String extraerCodigosNoEliminables(Connection con, int planTratamiento, String seccion, String codigosNoEliminables, String buscarHallazgosSuperficie)
	{
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, buscarHallazgosSuperficie);
			psd.setInt(1, planTratamiento);
			psd.setString(2, seccion);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean esPrimero=codigosNoEliminables.length()==0;
			while(rsd.next())
			{
				if(!esPrimero)
				{
					codigosNoEliminables+=", ";
				}
				else
				{
					esPrimero=false;
				}
				codigosNoEliminables+=rsd.getInt("codigo_pk");
			}
		}
		catch (SQLException e) {
			Log4JManager.error("Error buscando programas no eliminables", e);
		}
		return codigosNoEliminables;
	}

	/**
	 * Guarda las relaciones de los programas con las superficies
	 * @param con Conexi&oacute;n con la base de datos.
	 * @param listaEncabezados {@link ArrayList}<{@link DtoProgHallazgoPieza}>} Listado con las relaciones a guardar,
	 * los encabezados son de tipo {@link DtoProgHallazgoPieza} y los detalles de las superficies
	 * son de tipo {@link DtoSuperficiesPorPrograma}.
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-11
	 */
	public static ArrayList<DtoProgHallazgoPieza> guardarRelacionesProgSuperficies(Connection con, ArrayList<DtoProgHallazgoPieza> listaEncabezados)
	{
		String insertarEncabezado="INSERT INTO odontologia.programas_hallazgo_pieza ("+
											"codigo_pk, "+
											"plan_tratamiento, "+ 
											"programa, "+
											"hallazgo, "+
											"pieza_dental, "+
											"seccion, "+
											"usuario_modifica, "+ 
											"fecha_modifica, "+
											"hora_modifica," +
											"color_letra" +
										") " +
										" VALUES(" +
											"?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
										")";

		String insertarSuperficie=
					"INSERT INTO odontologia.superficies_x_programa (" +
						"codigo_pk, "+
						"superficie_dental, "+
						"prog_hallazgo_pieza, "+
						"usuario_modifica, "+
						"fecha_modifica, "+
						"hora_modifica, " +
						"det_plan_trata" +
					") " +
					"VALUES(" +
						"?, ?, ?, ?, ?, ?, ?" +
					")";
	
		for(DtoProgHallazgoPieza encabezado:listaEncabezados)
		{
			String sentenciaVerificarExistenciaEncabezado=
				"SELECT " +
					"php.codigo_pk AS codigo_pk " +
				"FROM " +
					"odontologia.programas_hallazgo_pieza php "+
				"WHERE " +
						"php.plan_tratamiento=? " +
					"AND php.programa=? " +
					"AND php.hallazgo=? " +
					"AND php.seccion=? " +
					"AND php.color_letra=? " +
					"AND php.codigo_pk=? " +
					"AND php.pieza_dental";
			try{

				if(encabezado.getPiezaDental()!=null && encabezado.getPiezaDental().intValue()>0)
				{
					sentenciaVerificarExistenciaEncabezado+="="+encabezado.getPiezaDental().intValue();
				}
				else
				{
					sentenciaVerificarExistenciaEncabezado+=" IS NULL";
				}

				PreparedStatementDecorator psdVerificacion=new PreparedStatementDecorator(con, sentenciaVerificarExistenciaEncabezado);
				
				psdVerificacion.setDouble(1, encabezado.getPlanTratamiento());
				psdVerificacion.setInt(2, encabezado.getPrograma());
				psdVerificacion.setInt(3, encabezado.getHallazgo());
				psdVerificacion.setString(4, encabezado.getSeccion());
				psdVerificacion.setString(5, encabezado.getColorLetra());
				psdVerificacion.setInt(6, encabezado.getCodigoPk());
				
				ResultSetDecorator rsd=new ResultSetDecorator(psdVerificacion.executeQuery());
				
				/*
				 * Si no existe el encabezado, se inserta
				 */
				int codigoPkencabezado=ConstantesBD.codigoNuncaValido;
				
				if(rsd.next())
				{
					codigoPkencabezado=rsd.getInt("codigo_pk");
					rsd.close();
				}
				else{
					codigoPkencabezado=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_programas_hallazgo");
					try{
						PreparedStatementDecorator psd=new PreparedStatementDecorator(con, insertarEncabezado);
						psd.setInt(1, codigoPkencabezado);
						psd.setDouble(2, encabezado.getPlanTratamiento());
						psd.setInt(3, encabezado.getPrograma());
						psd.setInt(4, encabezado.getHallazgo());
						if(encabezado.getPiezaDental()!=null && encabezado.getPiezaDental().intValue()>0)
						{
							psd.setInt(5, encabezado.getPiezaDental());
						}
						else
						{
							psd.setNull(5, Types.INTEGER);
						}
						psd.setString(6, encabezado.getSeccion());
						psd.setString(7, encabezado.getInfoFechaUsuario().getUsuarioModifica());
						psd.setString(8, encabezado.getInfoFechaUsuario().getFechaModificaFromatoBD());
						psd.setString(9, encabezado.getInfoFechaUsuario().getHoraModifica());
						psd.setString(10, encabezado.getColorLetra());
						
						Log4JManager.info(psd);
						
						boolean resultado=psd.executeUpdate()>0;
						psd.close();
						
						if(!resultado)
						{
							return null;
						}
					}catch (SQLException e) {
						Log4JManager.error("Error ingresando la relaciï¿½n entre programas y superficies", e);
						return null;
					}
				}
				
				encabezado.setCodigoPk(codigoPkencabezado);
				rsd.close();

				if(codigoPkencabezado>ConstantesBD.codigoNuncaValido)
				{
					encabezado.setRegistrado(true);
					String validacionSuperficiesXPrograma=
								"SELECT " +
									"sxp.codigo_pk AS codigo_pk " +
								"FROM " +
									"odontologia.superficies_x_programa sxp " +
								"WHERE " +
										"sxp.superficie_dental=? " +
									"AND sxp.prog_hallazgo_pieza=? " +
									"AND sxp.det_plan_trata=?";
					
					for(DtoSuperficiesPorPrograma superficie:encabezado.getSuperficiesPorPrograma())
					{
						/*
						 * Se consulta el detalle_plan_tratamiento para relacionarlo
						 */
						DtoDetallePlanTratamiento dtoPlanTratamiento=new DtoDetallePlanTratamiento();
						dtoPlanTratamiento.setPlanTratamiento(encabezado.getPlanTratamiento());
						if(encabezado.getPiezaDental()!=null)
						{
							dtoPlanTratamiento.setPiezaDental(encabezado.getPiezaDental());
						}
						dtoPlanTratamiento.setHallazgo(encabezado.getHallazgo());
						dtoPlanTratamiento.setSuperficie(superficie.getSuperficieDental());
						dtoPlanTratamiento.setActivo(ConstantesBD.acronimoSi);
						dtoPlanTratamiento.setSeccion(encabezado.getSeccion());
						PlanTratamiento.consultarDetPlanTratamiento(dtoPlanTratamiento, con, true);
					
						
						/*
						 * Se verifica la existencia de la superficie en BD
						 */
						PreparedStatementDecorator psdVerificacionSuperficies=new PreparedStatementDecorator(con, validacionSuperficiesXPrograma);
						psdVerificacionSuperficies.setInt(1, superficie.getSuperficieDental());
						psdVerificacionSuperficies.setInt(2, codigoPkencabezado);
						psdVerificacionSuperficies.setDouble(3, dtoPlanTratamiento.getCodigo());
						boolean resultado=false;
						ResultSetDecorator rsdSuperficies=new ResultSetDecorator(psdVerificacionSuperficies.executeQuery());
						/*
						 * Si no existe la superficie en BD se inserta
						 */
						if(!rsdSuperficies.next())
						{
							int seqSuperficiesXPrograma=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_superficies_x_programa");
							PreparedStatementDecorator psdSuperficie=new PreparedStatementDecorator(con, insertarSuperficie);
							psdSuperficie.setDouble(1, seqSuperficiesXPrograma);
							psdSuperficie.setInt(2, superficie.getSuperficieDental());
							psdSuperficie.setInt(3, codigoPkencabezado);
							psdSuperficie.setString(4, superficie.getInfoFechaUsuario().getUsuarioModifica());
							psdSuperficie.setString(5, superficie.getInfoFechaUsuario().getFechaModificaFromatoBD());
							psdSuperficie.setString(6, superficie.getInfoFechaUsuario().getHoraModifica());
							psdSuperficie.setDouble(7, dtoPlanTratamiento.getCodigo());

							resultado=psdSuperficie.executeUpdate()>0;
							psdSuperficie.close();
							if(!resultado)
							{
								Log4JManager.error("Error insertando las superficies por programa");
								rsdSuperficies.close();
								return null;
							}
						}
					}
				}
			}catch (SQLException e) {
				Log4JManager.error("Error consultando la existencia del encabezado", e);
				return null;
			}
		}		
		return listaEncabezados;
	}

	/**
	 * Consulta un registro especï¿½fico del plan de tratamiento con los parï¿½metros
	 * pasados a travï¿½s del DTO.
	 * @param dto {@link DtoDetallePlanTratamiento} DTO con los par&aacute;metros de b&uacute;squeda
	 * @param con {@link Connection} Conexi&oacute;n con la BD
	 * @param buscarTerminado Si true solamente consulta detalle del plan de tratamiento terminado.
	 * @author Juan David Ram&iacute;rez.
	 * @since 2010-05-11 
	 */
	public static boolean consultarDetPlanTratamiento(DtoDetallePlanTratamiento dto, Connection con, boolean buscarTerminado)
	{
		String consultaDetallePlan=
				"SELECT " +
						"d.codigo_pk AS codigo_pk, "+
						"d.plan_tratamiento AS plan_tratamiento, "+
						"d.pieza_dental AS pieza_dental, "+
						"d.superficie AS superficie, "+
						"d.hallazgo AS hallazgo, "+
						"d.seccion AS seccion, "+
						"d.clasificacion AS clasificacion, "+ 
						"d.por_confirmar AS por_confirmar, "+
						"d.convencion AS convencion, "+
						"d.usuario_modifica AS usuario_modifica, "+
						"d.fecha_modifica AS fecha_modifica, "+
						"d.hora_modifica AS hora_modifica, "+
						"d.especialidad AS especialidad, "+
						"d.valoracion AS valoracion, "+
						"d.evolucion AS evolucion, "+
						"d.cita AS cita, "+
						"d.activo AS activo " +
				"FROM " +
					"odontologia.det_plan_tratamiento d " +
				"WHERE " +
					"codigo_pk IS NOT NULL ";
		if(dto.getCodigo()>0)
		{
			consultaDetallePlan+="AND d.codigo_pk = "+dto.getCodigo()+" ";
		}
		if(dto.getPlanTratamiento()>0)
		{
			consultaDetallePlan+="AND d.plan_tratamiento = "+dto.getPlanTratamiento()+" ";
		}
		if(dto.getHallazgo()>0)
		{
			consultaDetallePlan+="AND d.hallazgo = "+dto.getHallazgo()+" ";
		}
		if(dto.getPiezaDental()>0)
		{
			consultaDetallePlan+="AND d.pieza_dental = "+dto.getPiezaDental()+" ";
		}
		buscarTerminado=false; // FIXME Esto se agrega temporalmente ya que está dañando las inclusiones
		if(buscarTerminado)
		{
			consultaDetallePlan+="AND " +
							"(" +
								"SELECT " +
									"count(1) " +
								"FROM " +
									"odontologia.programas_servicios_plan_t pspt " +
								"WHERE " +
									"pspt.det_plan_tratamiento = d.codigo_pk " +
								"AND " +
									"pspt.estado_programa!='"+ConstantesIntegridadDominio.acronimoContratado +"' " +
								"AND " +
									"pspt.estado_programa!='"+ConstantesIntegridadDominio.acronimoEnProceso +"' " +
							")>0 ";
		}
		if(dto.getSuperficie()>0)
		{
			/*
			 * Si la superficie es de boca o de diente se
			 * almacena null en detalle plan tratamiento
			 */
			if(dto.getSuperficie()==ConstantesBD.codigoSuperficieBoca)
			{
				consultaDetallePlan+="AND ( d.superficie IS NULL OR d.superficie = 1) ";
			
			}else if(dto.getSuperficie()==ConstantesBD.codigoSuperficieDiente){
				
				consultaDetallePlan+="AND ( d.superficie IS NULL OR d.superficie = 2) ";
			}
			else
			{
				consultaDetallePlan+="AND d.superficie = "+dto.getSuperficie()+" ";
			}
		}
		if(!UtilidadTexto.isEmpty(dto.getSeccion()))
		{
			consultaDetallePlan+="AND d.seccion = '"+dto.getSeccion()+"' ";
		}
		if(!UtilidadTexto.isEmpty(dto.getPorConfirmar()))
		{
			consultaDetallePlan+="AND d.por_confirmar = '"+dto.getPorConfirmar()+"' ";
		}
		if(!UtilidadTexto.isEmpty(dto.getActivo()))
		{
			consultaDetallePlan+="AND d.activo = '"+dto.getActivo()+"' ";
		}
		
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consultaDetallePlan);
			Log4JManager.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());

			if(rsd.next())
			{
				dto.setCodigo(rsd.getDouble("codigo_pk"));
				dto.setPlanTratamiento(rsd.getDouble("plan_tratamiento"));
				dto.setPiezaDental(rsd.getInt("pieza_dental"));
				dto.setSuperficie(rsd.getInt("superficie"));
				dto.setHallazgo(rsd.getInt("hallazgo"));
				dto.setSeccion(rsd.getString("seccion"));
				dto.setClasificacion(rsd.getString("clasificacion"));
				dto.setPorConfirmar(rsd.getString("por_confirmar"));
				dto.setConvencion(rsd.getInt("convencion"));
				DtoInfoFechaUsuario fechaUsuario=new DtoInfoFechaUsuario();
				fechaUsuario.setUsuarioModifica(rsd.getString("usuario_modifica"));
				fechaUsuario.setFechaModifica(rsd.getString("fecha_modifica"));
				fechaUsuario.setHoraModifica(rsd.getString("hora_modifica"));
				dto.setFechaUsuarioModifica(fechaUsuario);
				InfoDatosInt especialidad=new InfoDatosInt();
				especialidad.setCodigo(rsd.getInt("especialidad"));
				dto.setEspecialidad(especialidad);
				dto.setValoracion(rsd.getBigDecimal("valoracion"));
				dto.setEvolucion(rsd.getBigDecimal("evolucion"));
				dto.setCodigoCita(rsd.getBigDecimal("cita"));
				dto.setActivo(rsd.getString("activo"));
			}
			rsd.close();
			psd.close();
		}catch (SQLException e) {
			Log4JManager.error("Error consultando el detalle plan tratamiento", e);
			UtilidadBD.closeConnection(con);
		}
		return true;
	}
	
	/**
	 * Obtener el n&uacute;mero de superficies que fueron relacionadas en un programa de N superficies.
	 * @param hallazgo C&oacute;digo del hallazgo.
	 * @param programa C&oacute;digo del programa buscado.
	 * @param pieza C&oacute;digo de la pieza dental a la cual se le vincul&oacute; el hallazgo. 
	 * @param superficie C&oacute;digo de la superficie evaluada.
	 * @param codigoPlanTratamiento C&oacute;digo del plan de tratamiento evaluado
	 * @return N&uacute;mero de superficies relacionadas, si no existe relaci&oacute;n en BD
	 * indica que es un diente, por lo tanto retorna 0
	 * @author Juan David Ram&uacute;rez
	 * @since 2010-05-14
	 */
	public static int consultarNumeroSuperficiesPorPrograma(int hallazgo, int programa, int pieza, int superficie, int codigoPlanTratamiento)
	{
		Connection con=UtilidadBD.abrirConexion();
		/*
		 * Se consulta el encabezado con los datos de pieza, hallazgo, programa para un plan de tratamiento
		 */
		try{
			String consultaEncabezado=
				"SELECT " +
					"php.codigo_pk AS prog_hallazgo_pieza " +
				"FROM " +
					"odontologia.programas_hallazgo_pieza php " +
				"INNER JOIN " +
					"odontologia.superficies_x_programa sxp " +
						"ON (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
				"WHERE " +
						"php.hallazgo=? " +
					"AND " +
						"php.programa=? " +
					"AND " +
						"php.pieza_dental=? " +
					"AND " +
						"sxp.superficie_dental=? " +
					"AND " +
						"plan_tratamiento=?";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consultaEncabezado);
			psd.setInt(1, hallazgo);
			psd.setInt(2, programa);
			psd.setInt(3, pieza);
			psd.setInt(4, superficie);
			psd.setInt(5, codigoPlanTratamiento);
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			int codigoEncabezado=0;
			if(rsd.next())
			{
				codigoEncabezado=rsd.getInt("prog_hallazgo_pieza");
			}
			psd.close();
			rsd.close();
			
			/*
			 * Si el codigo del encabezado e 0 es hallazgo que aplicï¿½ al diente
			 * por lo tanto tiene 0 superficies
			 */
			if(codigoEncabezado>0)
			{
				String consultaCantidadSuperficies=
						"SELECT " +
							"COUNT(1) AS cantidad_superficies " +
						"FROM " +
							"odontologia.superficies_x_programa sxp " +
						"WHERE " +
							"sxp.prog_hallazgo_pieza = ?";
				psd=new PreparedStatementDecorator(con, consultaCantidadSuperficies);
				psd.setInt(1, codigoEncabezado);
				rsd=new ResultSetDecorator(psd.executeQuery());
				int numeroSuperficies=0;
				if(rsd.next())
				{
					numeroSuperficies=rsd.getInt("cantidad_superficies");
				}
				rsd.close();
				psd.close();
				return numeroSuperficies;
			}
			return codigoEncabezado;
		}catch (SQLException e) {
			Log4JManager.error("Error en la consulta del numero de superficies",e);
		}
		finally{
			UtilidadBD.closeConnection(con);	
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * CARGA EL INGRESO CON EL RESPECTIVO PLAN DE TRATAMIENTO
	 * @param infoDto
	 * @return
	 */
    public static ArrayList<InfoIngresoPlanTratamiento> cargarIngresosPlanTratamiento(InfoIngresoPlanTratamiento infoDto ) 
	{
		
    	ArrayList<InfoIngresoPlanTratamiento> listaIngreso = new ArrayList<InfoIngresoPlanTratamiento>();  
    	
    	
    	
    	 
    	
    	String consulta= " select  p.codigo_pk  as codigoPlan, " +
    							"p.estado as estado ," +
    							"p.motivo as motivo  , " +
    							//"p.especialidad as especialidad ,  " +
    							"p.por_confirmar as porConfirmar , " +
    							//"p.valoracion  as valoracion,  " +
    							//"p.cita as cita, " +
    							"p.evolucion  as evolucion," +
    							//"p.imagen as imagen , " +
    							"p.odontograma_diagnostico as odontogramaDiagnostico , " +
    							"p.ingreso as ingreso, "+ 
    							"i.id as id ,"+                    
    							"i.codigo_paciente as codigoPaciente ,"+             
    							"i.institucion as institucion ,"+                  
    							"i.estado as estadoIngreso ,"+                       
    							"i.fecha_ingreso as fechaIngreso ," +
    							"getnombrecentatenxing(i.id) as nombreCentroAtencion"+
    							"" +
    						" from  " +
    						"	manejopaciente.ingresos i inner join odontologia.plan_tratamiento p " +
    						"	on (p.ingreso=i.id and i.codigo_paciente="+infoDto.getDtoIngresoPaciente().getCodigoPaciente()+" ) ";
    	
    				
    	
    	Log4JManager.info(consulta);
    	
    	
    	

		
		try 
		{
			
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con ,consulta);
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				InfoIngresoPlanTratamiento ingresoPla = new InfoIngresoPlanTratamiento();
				
				DtoPlanTratamientoOdo dtoPlan= new DtoPlanTratamientoOdo();
				dtoPlan.setCodigoPk(rs.getBigDecimal("codigoPlan"));
				dtoPlan.setEstado(rs.getString("estado"));
				//dtoPlan.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad") ));
				dtoPlan.setPorConfirmar(rs.getString("porConfirmar"));
				dtoPlan.setCodigoEvolucion(rs.getBigDecimal("evolucion"));
				dtoPlan.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramaDiagnostico"));
				dtoPlan.setIngreso(rs.getInt("ingreso"));
				dtoPlan.setInstitucion(rs.getInt("institucion"));
				
				
				//Settear dto Plan Tratamiento
				ingresoPla.setDtoPlanTratamiento(dtoPlan);
				
				
				
				DtoOtrosIngresosPaciente dtoPaciente = new DtoOtrosIngresosPaciente();
				dtoPaciente.setIngreso(String.valueOf(rs.getInt("ingreso")));
				dtoPaciente.setEstadoIngreso(rs.getString("estadoIngreso"));
				dtoPaciente.setFechaIngreso(rs.getString("fechaIngreso"));
				dtoPaciente.setCentroAtencion(rs.getString("nombreCentroAtencion"));
				
				//Setteo 
				ingresoPla.setDtoIngresoPaciente(dtoPaciente);
				
				//adicionar lista
				listaIngreso.add(ingresoPla);
		
			}
			Log4JManager.info(" Consulta >>"+ps);
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			Log4JManager.info("Error "+e);
		}
		return listaIngreso;
	}
	
	
    
    /**
     * 
     * @param codigoPresupuesto
     * @return
     */
    public static String  cargarNombreUsuarioModificaPlanTratamiento(BigDecimal codigoPresupuesto )
    {

    	String usuario="";
    	
    	String consulta =" select distinct pl.usuario_modifica AS usuario from  odontologia.presupuesto_odontologico p inner join odontologia.plan_tratamiento   pl on (p.plan_tratamiento=pl.codigo_pk) where p.codigo_pk=? ";
		
    	
    	try 
		{
			
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con ,consulta);
			ps.setBigDecimal(1, codigoPresupuesto);
			Log4JManager.info("consulta  MEDICO PRESUPUESTO PLAN "+ps);
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next()) 
			{
				usuario=rs.getString(1);
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch(SQLException e)
		{
			Log4JManager.error(e);
		}
    
    return usuario;
    }
    
    
    /**
	 * 
	 * Método para obtener el ultimo codigo pk de la cita de un programa servicio del plan tratamiento dado el codigo_pk de la tabla programas servicios plan t
	 * También verifica si el programa o servicio Excluido tiene motivo
	 *  
	 * @param codigoPkProgServPlanT
	 * @param fechaFormatoAppNOREQUERIDA
	 * @param estado
	 * 
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado) 
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr="select sco.codigo_pk" +
							" from odontologia.servicios_cita_odontologica sco" +
							" INNER JOIN odontologia.citas_odontologicas co on(co.codigo_pk=sco.cita_odontologica) " +
							" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
							" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
							" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
							" INNER JOIN  odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and pspt.servicio=sco.servicio AND pspt.activo = '"+ConstantesBD.acronimoSi+"' ) " +
							" where pspt.codigo_pk = ? AND sco.activo = '"+ConstantesBD.acronimoSi+"' ";
		
		if(UtilidadFecha.esFechaValidaSegunAp(fechaFormatoAppNOREQUERIDA))
		{
			consultaStr+=" and sco.fecha_cita= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFormatoAppNOREQUERIDA)+"' ";
		}
		if(estado.size()>0)
		{
			String cadenaEstados="";
			for(String varEstado:estado)
			{
				if(!UtilidadTexto.isEmpty(cadenaEstados))
					cadenaEstados=cadenaEstados+",";
				cadenaEstados=cadenaEstados+"'"+varEstado+"'";
			}
			consultaStr+=" and co.estado in ("+cadenaEstados+")";
		}
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, codigoPkProgServPlanT);
			
			Log4JManager.info("LA CONSUTLA DE LA CITA-------->"+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				ps.close();
				retorna= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			Log4JManager.info("Error Consulta obtenerCodigoPkUltimaCitaProgServPlanT ",e);
		}
		return retorna;
	}
	
	
	

	/**
	 *METODO QUE CONSULTA LOS PLANES DE TRATAMIENTO HISTORICOS.
	 *RECIBE UN DTO
	 *Y RETORNA UN ARRAY DE PLANES DE TRATAMIENTO.
	 * 
	 * @param Connection con
	 * @param DtoPlanTratamientoOdo parametros
	 * */
	public static BigDecimal  consultarPlanTratamientoHistoricoMaximo(DtoPlanTratamientoOdo parametros)
	{
	
		BigDecimal codigoRetorno=BigDecimal.ZERO;
		
		
		/**
		* Cadena Sql para Consultar los Histï¿½ricos del plan de tratamiento
		*/
		
		String consulta="select max(codigo_pk) as codigoPk from odontologia.log_plan_tratamiento lp where 1=1 ";
		 
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoPlanTratamientoOdo> listHistoricosPlan= new ArrayList<DtoPlanTratamientoOdo>();
		

		
	
		
		
		consulta +=(parametros.getCodigoPk().doubleValue()>0)?" AND lp.codigo_pk ="+parametros.getCodigoPk(): " ";
		consulta +=(parametros.getIngreso()>0)?" AND  lp.ingreso="+parametros.getIngreso():" ";
		consulta +=UtilidadTexto.isEmpty(parametros.getPorConfirmar())?"": " AND lp.por_confirmar='"+parametros.getPorConfirmar()+"'";
		consulta+=UtilidadTexto.isEmpty(parametros.getEstado())?" ": " AND  lp.estado='"+parametros.getEstado()+"'";
		consulta+=parametros.getOdontogramaDiagnostico().doubleValue()>0?" AND lp.odontograma_diagnostico ="+parametros.getOdontogramaDiagnostico(): " ";
		consulta+=parametros.getCodigoEvolucion().doubleValue()>0?" AND lp.evolucion="+parametros.getCodigoEvolucion()+" ": " ";
		consulta+=parametros.getCodigoPlanHistorico().doubleValue()>0?"  AND lp.plan_tratamiento="+parametros.getCodigoPlanHistorico() : " ";
	
		
		
		
		
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			Log4JManager.info("Historico Plan tratamiento"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				codigoRetorno= new BigDecimal(rs.getDouble("codigoPk"));
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Log4JManager.info("Error-consultarPlanTratamientoHistorico-Consulta: "+consulta);
		}
		
		
		return codigoRetorno;
		
	}
	
	
	
	
	/**
	 * MODIFICAR EL DETALLE DEL PLAN DE TRATAMIENTO
	 * METODO QUE MODIFICA EL PLAN DE TRATAMIENTO PARA REALIZAR LA TRAZABILIDAD DEL ODONTOGRAMA
	 * @author Edgar Carvajal
	 * @param dto
	 * @return
	 */
	
	public static boolean   modificarDetallePlanTratamiento (DtoDetallePlanTratamiento dto, Connection con)
	{
		
		boolean retorno=Boolean.FALSE;
		
		String strModifica = " update odontologia.det_plan_tratamiento set "+
									" fecha_modifica=CURRENT_DATE ,"+   
									" hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+ 
							 " where codigo_pk=? and " +
							 	  "  plan_tratamiento=? ";
		
		
		try
		{
			
		
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , strModifica);
			ps.setDouble(1, dto.getCodigo());
			ps.setDouble(2, dto.getPlanTratamiento());
			
			Log4JManager.info(ps);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				retorno=Boolean.TRUE;
			}
			
			
		}
		catch(SQLException e)
		{
			Log4JManager.info(e);
		}
		
		return retorno;
	}
	
	
	
	/*///////////////////////////////////////////////////////////////////////////////////////////////////////
	 * ******************************************************************************************************
	 *///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	 
	// 	METODO PARA CARGAR LOS HISTORICOS DEL ODONTOGRAMA
	
	

	/**
	 * CARGA LAS PIEZAS  DE LOS HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param codigoPkPlanTratamiento
	 * @return
	 * 
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasHistoricoPlanTratamiento(DtoPlanTratamientoOdo dtoPlan, DtoDetallePlanTratamiento dtoDet)
	{
	
		
		String consultaStr="SELECT DISTINCT d.pieza_dental AS pieza_dental, " +
											"	p.descripcion AS descripcion " +
								"	FROM odontologia.log_det_plan_tratamiento d " +
								"INNER JOIN " +
								"	odontologia.pieza_dental p ON(p.codigo_pk=d.pieza_dental) " +
								"WHERE d.log_plan_tratamiento="+dtoPlan.getCodigoPlanHistorico()+" and d.seccion= '"+dtoDet.getSeccion()+"' ";
		
		/*
		 * TODO SIEMPRE DEBE SER POR CONFIRMA EN NO
		 */
		consultaStr+=(!UtilidadTexto.isEmpty(dtoDet.getPorConfirmar()))?" AND d.por_confirmar='"+dtoDet.getPorConfirmar()+"' ": " ";
		consultaStr+=" order by p.descripcion desc ";
		
		
		
		ArrayList<InfoDatosInt> array= new ArrayList<InfoDatosInt>();
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			Log4JManager.info("CONSULTAR PIEZAS HISTORICOS ");
			Log4JManager.info("Sql "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt("pieza_dental"));
				info.setNombre(rs.getString("descripcion"));
				array.add(info);
			}
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
		
			Log4JManager.info(""+e);
		}
			
		
		
		return array;
	}
	
	
	
	
	
	

	/**
	 * OBTENER LISTA DE HALLAZGOS SUPERFICIE HISTORICOS PLAN 
	 * BUSCAR LOS HALLAZGOS DE UNA SUPEFICIE EL  LOS HIISTORICOS DEL DETALLE DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoDetalle
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(DtoDetallePlanTratamiento dtoDetalle)
	{
		
		/*
		 * MODIFICAR ESTO 
		 */
		ArrayList<InfoHallazgoSuperficie>  listaHallazgosSuperficie= new ArrayList<InfoHallazgoSuperficie>();
		
		String consultaStr= "SELECT " +
									
									"distinct " +
									"d.hallazgo as codigohallazgo, " +
									"h.nombre as nombrehallazgo, " +
									"coalesce(d.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as piezadental, " +
									"coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
									"coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie, " +
									"coalesce(ssc.sector, "+ConstantesBD.codigoNuncaValido+") as sectordiente " +
									
							"FROM " +
								"odontologia.log_det_plan_tratamiento d " +
							"INNER JOIN " +
								"odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
							"LEFT OUTER JOIN " +
								"historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							"LEFT OUTER JOIN " +
								"odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							"LEFT OUTER JOIN " +
								"odontologia.convenciones_odontologicas c ON(c.consecutivo = d.convencion) "  +
							"WHERE " +
								"d.log_plan_tratamiento = ?  and " +
								"d.por_confirmar=? ";
								
								

		
		
		/*
		 * TODO FALTA VALIDAR SI ES POR CONFIMAR EN NO EL DETALLE DEL PLAN
		 */
		
		
		
		if(dtoDetalle.getPiezaDental() > 0)
		{
			consultaStr += " AND d.pieza_dental = "+dtoDetalle.getPiezaDental();
		}
		
		if(!dtoDetalle.getSeccion().equals(""))
		{
			consultaStr += " AND d.seccion = '"+dtoDetalle.getSeccion()+"' ";
		}
		
		
		//consultaStr+= " ORDER BY d.superficie desc ";
		
		
		
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setDouble(1,dtoDetalle.getCodigoPkPlanTratmientoHistorico());
			ps.setString(2, dtoDetalle.getPorConfirmar());
		
			Log4JManager.info("\n\n CONSULTA HALLAZGOS SUPERFICIE HISTORICOS ");
			Log4JManager.info("\n sql consulta "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		
			while(rs.next())
			{
				/*
				 *SETTEO 
				 */
				InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
				hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo")));
				hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
				hallazgoSuperficie.getSuperficieOPCIONAL().setCodigo2(rs.getInt("sectordiente"));
				hallazgoSuperficie.getSuperficieOPCIONAL().setActivo(true);
				
				/*
				 *CARGAR LA INFORMANCION RESPECTIVA DEL HALLAZGO SUPERFICIE 
				 */
				cargarMaximoCodigoPkDetalleHallazgoPiezaSeccion(hallazgoSuperficie, con, dtoDetalle);
				/*
				 * ADICIONAR
				 */
				listaHallazgosSuperficie.add(hallazgoSuperficie);
			}
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ", e);
		}
		
		return listaHallazgosSuperficie;
	}
	
	
	
	
	
	/**
	 * CARGAR EL CODIGO MAXIMO DEL DETALLE HALLAZGO PIEZA SECCION DEL PLAN  DETRATAMIENTO HISTORICO
	 * @author Edgar Carvajal Ruiz
	 * @param hallazgoSuperficie
	 * @param con
	 * @param dtoDetalle
	 * @return
	 */
	public static void  cargarMaximoCodigoPkDetalleHallazgoPiezaSeccion( InfoHallazgoSuperficie hallazgoSuperficie, Connection con , DtoDetallePlanTratamiento dtoDetalle ){
		
		String consulta= 
				" select d.codigo_pk as codigoPk ," +
						" d.clasificacion as clasificacion "+
				" FROM " +
					" odontologia.log_det_plan_tratamiento d "+ 
				" INNER JOIN " +
					" odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) "+ 
				" LEFT OUTER JOIN " +
					" historiaclinica.superficie_dental s ON(s.codigo=d.superficie) "+
				" where "+ 
					" d.log_plan_tratamiento = ? "+
				" AND " +
					" d.hallazgo= ? " +
				" AND  " +
					" d.por_confirmar='"+ConstantesBD.acronimoNo+"'";

			
				if(dtoDetalle.getPiezaDental() > 0)
				{
					consulta += " AND d.pieza_dental = "+dtoDetalle.getPiezaDental();
				}
				
				if(!dtoDetalle.getSeccion().equals(""))
				{
					consulta += " AND d.seccion = '"+dtoDetalle.getSeccion()+"' ";
				}
				
				if(hallazgoSuperficie.getSuperficieOPCIONAL().getCodigo()>0)
				{
					consulta+=" AND  d.superficie="+hallazgoSuperficie.getSuperficieOPCIONAL().getCodigo() ;
				}
				
				consulta+=" order by codigo_pk desc limit 1"; // TODO CAMBIAR PARA ORACLE
				
		
		try{
		
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setDouble(1,dtoDetalle.getCodigoPkPlanTratmientoHistorico());
			ps.setInt(2, hallazgoSuperficie.getHallazgoREQUERIDO().getCodigo());
			Log4JManager.info("\n\n CONSULTA HALLAZGOS SUPERFICIE HISTORICOS ");
			Log4JManager.info("\n sql cargar Maximo CodigoPk Detalle Hallazgo Pieza Seccion  consulta "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		
			if(rs.next())
			{
				hallazgoSuperficie.setCodigoPkDetalle(rs.getBigDecimal("codigoPk"));
				hallazgoSuperficie.getClasificacion().setNombre(rs.getString("clasificacion"));
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch(SQLException e)
		{
			Log4JManager.info("Error "+e);
		}
		
	}
	
	
	/**
	 * 
	 * ESTE ALGORITMO ES TOMADO DEL  METODO obtenerProgramasOServicios(DtoProgramasServiciosPlanT).
	 * SIRVE PARA CARGAR PROGRAMAS Y SERVICIOS DEL HISTORICO DEL PLAN DE TRATAMIENTO
	 *  
	 * @author Edgar Carvajal Ruiz
	 * @param dtoProgramaServicios
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHistoricos(DtoProgramasServiciosPlanT dtoProgramaServicios) 
	{
		ArrayList<InfoProgramaServicioPlan> listaProgramasServicios= new ArrayList<InfoProgramaServicioPlan>();
		
		String consultaStr = "", despuesWhere = "";
		
		if(dtoProgramaServicios.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
		{
			consultaStr = 
				
				/*1. PRIMERO CARGARMOS TODOS LOS PROGRAMAS DEL LOG DETALLE PROGRAMAS SERVICIOS PLAN T
				 * DADO QUE EXISTEN MUCHOS DATOS REPETIDOS  
				 */
				" SELECT DISTINCT " +
				" pro.codigo as codigo ,"+ 
				" pro.codigo_programa  ||' '|| pro.nombre  as nombre, "+ 
				" pro.codigo_programa as codigoamostrar "+ 
				" FROM  odontologia.log_programas_servicios_plan_t plant "+ 
				" INNER JOIN "+ 
				" 	odontologia.log_det_plan_tratamiento dpt ON(dpt.codigo_pk=plant.log_det_plan)  "+ 
				" INNER JOIN  "+
				" 	odontologia.programas pro ON (plant.programa = pro.codigo) "+ 
				" WHERE "+
				" plant.log_det_plan="+dtoProgramaServicios.getLogDetPlanTratamiento();     
		
		}else{
			
			/*
			 *para servicio aplica de la misma manera 
			 *TODO este consutla ESTA POR REEVALUAR DADO QUE PARA SERVICIO TIENE APLICACION   OJO
			 */
			consultaStr = 	"SELECT DISTINCT " +
							"plant.codigo_pk as codigopk," +//1
							"plant.servicio as codigoServicio ," +//2
							
							/*OJO MIRAR ESTO ?????????????
							 * 
							 */
							"getnombreservicio(plant.servicio)  as nombreServicio, " +//3
							/* TODO  OJO MODIFICAR AQUI EL CODIGO TARIFARIO
							 * 
							 */
							"getcodigoservicio(plant.servicio, "+dtoProgramaServicios.getCodigoTarifario()+") as codigoamostrar, " +//4
							
							
							"plant.por_confirmar," +//5
							"plant.estado_servicio as est_serv," +//6
							"coalesce(plant.cita,"+ConstantesBD.codigoNuncaValido+") as cita, " +//7
							"coalesce(plant.valoracion,"+ConstantesBD.codigoNuncaValido+") as valoracion, " +//8
							"coalesce(plant.evolucion,"+ConstantesBD.codigoNuncaValido+") as evolucion, " +//9
							"coalesce(co.archivo_convencion,'') as archivo_convencion, " +//10
							"coalesce(plant.inclusion,'') as inclusion, " +//11
							"coalesce(plant.exclusion,'') as exclusion, " +//12
							"coalesce(plant.garantia,'') as garantia, " +//13
							"coalesce(plant.motivo,"+ConstantesBD.codigoNuncaValido+") as motivo_can, " +//14
							"coalesce(mcc.nombre,'') as nom_motivo_can, " +//15
							"coalesce(mcc.codigo,'') as cod_motivo_can " +//16
							"FROM " +
							"odontologia.log_programas_servicios_plan_t plant " +
							"INNER JOIN facturacion.servicios s ON (s.codigo = plant.servicio) " +
							"LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON (co.consecutivo = s.convencion) " +
							"LEFT OUTER JOIN odontologia.motivos_atencion mcc ON (mcc.codigo_pk = plant.motivo) " +
							"WHERE " +
							"plant.programa IS NOT NULL AND plant.log_det_plan="+dtoProgramaServicios.getLogDetPlanTratamiento() ;
			
			/* 
			 * TODO CAMBIAR AQUI TAMBIEN APLICA EL ESTADO -->RI 
			 * SOLO DEBE APLICA PARA SERVICIO NO PARA PROGRAMAS  
			 
			if(dtoProgramaServicios.getEstadosProgramasOservicios().size() >0 )
			{
				consultaStr += "AND plant.estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(dtoProgramaServicios.getEstadosProgramasOservicios())+") ";
			}
			*/
			
			despuesWhere = " ORDER BY plant.orden_servicio ASC ";
		}
		
		//falta por confirma en no
		
		
		
		
		try 
		{ 
			consultaStr += despuesWhere;
			
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
		
			Log4JManager.info(" PROGRAMAS HISTORICOS  \n constuta sql: ");
			Log4JManager.info(" "+ps);
			
			
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(dtoProgramaServicios.getBuscarProgramas().equals(ConstantesBD.acronimoSi))
			{
				DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT(); // instacia tmp programs servicios
				parametrosServ.setLogDetPlanTratamiento(dtoProgramaServicios.getLogDetPlanTratamiento());
				parametrosServ.setCodigoTarifario(dtoProgramaServicios.getCodigoTarifario()); // setteo desde el plan tratamiento
				parametrosServ.setEstadosProgramasOservicios(dtoProgramaServicios.getEstadosProgramasOservicios()); // estados Servicios  solo tiene aplicacion cuando se requieren mostrar los servicios evolucionados
				parametrosServ.setPorConfirmado(ConstantesBD.acronimoNo);
			
				
				while(rs.next())
				{
					InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
					info.setCodigoPkProgramaServicio(rs.getBigDecimal("codigo"));
					info.setNombreProgramaServicio(rs.getString("nombre"));
					info.setCodigoAmostrar(rs.getString("codigoamostrar"));
					parametrosServ.getPrograma().setCodigo(rs.getBigDecimal("codigo").doubleValue());
					
					/*
					 * 2. CARGA LA INFORMACION COMPLEMENTARIA DEL PROGRAMA 
					 * COMO:
					 * ESTADO PROGRAMA
					 * INCLUSION
					 * EXCLUSION 
					 * GARANTIA 
					 * PK-LOG PROGRAMAS SERVICIOS PLAN TRATAMIENTO
					 */
					cargarInformacionProgramaHistorico(parametrosServ, con, info);
					/*
					 * 3. CARGAR TODOS LOS SERVICIOS DEL PROGRAMA 
					 *   
					 */
					info.setListaServicios(cargarServiciosDeProgramasPlanTHistoricoOdongrama(parametrosServ));
					
					/*
					 * 4.ADICIONAR A LA LISTA 
					 */
					listaProgramasServicios.add(info);
				}
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}// FIN DE BUSQUEDA DE PROGRAMAS
			
			
			// BUSQUEDA DE SERVICIOS 
			else
			{
				
				InfoProgramaServicioPlan info = new InfoProgramaServicioPlan();
				
				while(rs.next())
				{
					InfoServicios serv = new InfoServicios();
					serv.setCodigoPkProgServ(rs.getBigDecimal("codigopk"));
					serv.setCodigoMostrar(rs.getString("codigoamostrar"));
					serv.getServicio().setCodigo(rs.getInt("codigoServicio"));
					serv.getServicio().setNombre(rs.getString("nombreServicio"));
					serv.getExisteBD().setValue(ConstantesBD.acronimoSi);
					serv.setPorConfirmar(rs.getString("por_confirmar"));
					serv.setEstadoServicio(rs.getString("est_serv"));
					serv.setNewEstado(rs.getString("est_serv"));
					serv.setCodigoCita(new BigDecimal(rs.getInt("cita")));
					serv.setCodigoValoracion(new BigDecimal(rs.getInt("valoracion")));
					serv.setCodigoEvolucion(new BigDecimal(rs.getInt("evolucion")));
					serv.setInclusion(rs.getString("inclusion"));
					serv.setExclusion(rs.getString("exclusion"));
					serv.setGarantia(rs.getString("garantia"));
					info.setMotivoCancelacion(new InfoDatosStr(rs.getInt("motivo_can")+"",rs.getString("nom_motivo_can"), rs.getString("cod_motivo_can")));
					serv.getExisteBD().setActivo(true);
					serv.setArchivoConvencionPrograma(CarpetasArchivos.CONVENCION.getRuta()+""+rs.getString("archivo_convencion"));
					info.getListaServicios().add(serv);
				}
				
				
				listaProgramasServicios.add(info);
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				
				
			}
			
		}
		
		
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}	
		
		
		return listaProgramasServicios;
	}
	
	
	
	/**
	 * METODO PARA CONSULTA LA INFORMACION DE UN PROGRAMA EN LA LOG DETALLE PROGRAMAS SERVICIO PLAN TRATAMIENTO 
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoProgramaServicios
	 * @param con
	 */
	public static  void cargarInformacionProgramaHistorico(DtoProgramasServiciosPlanT dtoProgramaServicios , Connection con , InfoProgramaServicioPlan info )
	{
		
		String consulta= "" +
			"	select "+ 
			" plant.codigo_pk as codigoPk, "+  
			" plant.estado_programa as estado, "+
			" plant.inclusion as inclusion ,  "+                 
			" plant.exclusion as exclusion ,  "+              
			" plant.garantia as garantia  "+               
			" from odontologia.log_programas_servicios_plan_t plant "+  
			" where   "+
			" plant.log_det_plan=? "+ 
			" and  "+
			" plant.programa=? " +
			" and" +
			" plant.por_confirmar='"+ConstantesBD.acronimoNo+"' "+ 
			" order by codigo_pk desc "+ 
			" limit 1 " // TODO URGENTE nota cambiar para oracle
			;
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, dtoProgramaServicios.getLogDetPlanTratamiento());
			ps.setDouble(2, dtoProgramaServicios.getPrograma().getCodigo());
			
			Log4JManager.info("CONSULTA INFORMACION PROGRAMA ");
			Log4JManager.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				info.setCodigoPKProgramasServiciosPlanTratamiento(rs.getBigDecimal("codigoPk"));
				info.setEstadoPrograma(rs.getString("estado"));
				info.setInclusion(rs.getString("inclusion"));
				info.setExclusion(rs.getString("exclusion"));
				info.setGarantia(rs.getString("garantia"));
			}
			
		}
		catch(Exception e)
		{
			Log4JManager.info("Errores "+e);
		}
		
	}
	
	 
	
	
	
	
	
	
	/**
	 * Obtiene los hallazgos para las secci&oacute;n de Otros y Boca HISTORICOS
	 * ESTE METODO ES TOMADO DE -> obtenerHallazgosSuperficiesSeccionOtrayBoca()
	 * @return
	 */
	public static ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(DtoDetallePlanTratamiento dtoDetallePlan)
	{
		ArrayList<InfoDetallePlanTramiento> array=new ArrayList<InfoDetallePlanTramiento>();
		
		String consultaStr= "SELECT " +
							
							" distinct " +
							" d.hallazgo as codigohallazgo, " +
							" h.nombre as nombrehallazgo, " +
							"coalesce(d.pieza_dental, "+ConstantesBD.codigoNuncaValido+") as piezadental, " +
							" coalesce(d.superficie, "+ConstantesBD.codigoNuncaValido+") as codigosuperficie, " +
							" coalesce(getNombreSuperficie(ssc.sector, d.pieza_dental), '') as nombresuperficie, " +
							" coalesce(ssc.sector, "+ConstantesBD.codigoNuncaValido+") as sectordiente " +
							
							" FROM " +
							" odontologia.log_det_plan_tratamiento d " +
							" INNER JOIN " +
							" odontologia.hallazgos_odontologicos h ON(h.consecutivo=d.hallazgo) " +
							" LEFT OUTER JOIN " +
							" historiaclinica.superficie_dental s ON(s.codigo=d.superficie) " +
							" LEFT OUTER JOIN " +
							" odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND d.pieza_dental=ssc.pieza) " +
							" LEFT OUTER JOIN " +
							" odontologia.convenciones_odontologicas c ON(c.consecutivo = d.convencion) "  +
							" WHERE " +
							" d.log_plan_tratamiento = ?  and " +
							" d.por_confirmar='"+ConstantesBD.acronimoNo+"' ";

								
		// NO aplica ??						
		if(dtoDetallePlan.getPiezaDental() > 0)
		{
			consultaStr += " AND d.pieza_dental = "+dtoDetallePlan.getPiezaDental();
		}
		
		
		if(!UtilidadTexto.isEmpty(dtoDetallePlan.getSeccion()))
		{
			consultaStr += " AND d.seccion = '"+dtoDetallePlan.getSeccion()+"' ";
		}
		
			
			
			
			
			try 
			{
				Connection con = UtilidadBD.abrirConexion();
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
				ps.setDouble(1,dtoDetallePlan.getCodigoPkPlanTratmientoHistorico());
				
			
				Log4JManager.info("\n\n CONSULTA HALLAZGOS  BOCA Y OTROS SUPERFICIE HISTORICOS ");
				Log4JManager.info("\n sql consulta "+ps);
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
				while(rs.next())
				{
					
					InfoHallazgoSuperficie hallazgoSuperficie= new InfoHallazgoSuperficie();
					hallazgoSuperficie.setHallazgoREQUERIDO(new InfoDatosInt(rs.getInt("codigohallazgo"), rs.getString("nombrehallazgo") ));
					hallazgoSuperficie.setSuperficieOPCIONAL(new InfoDatosInt(rs.getInt("codigosuperficie"), rs.getString("nombresuperficie")));
					
					cargarMaximoCodigoPkDetalleHallazgoPiezaSeccion(hallazgoSuperficie, con, dtoDetallePlan);
					
					
					InfoDetallePlanTramiento pieza = new InfoDetallePlanTramiento();
					
					pieza.getPieza().setCodigo(rs.getInt("piezadental")); // TODO FALTA EL NOMBRE DE LA PIZA OJO
					
					/*
					 *BUSCAR CODIGO DETALLE
					 * 
					 */
					pieza.getDetalleSuperficie().add(hallazgoSuperficie);
					array.add(pieza);
				}
			}
			catch(SQLException e)
			{
				Log4JManager.info(e); // TODO MODIFICAR ESTE AL .ERROR 
			}
		
			
		return array;
	}
	

	/**
	 *	CARGAR LOS SERVICIOS DE UN PROGRAMA DE LOS PLANES DE TRATAMIENTO HISTORICOS  
	 * @author Edgar Carvajal Ruiz
	 * @param parametros
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanTHistoricoOdongrama(DtoProgramasServiciosPlanT parametros)
	{
		ArrayList<InfoServicios> listaServicios = new ArrayList<InfoServicios>(); 
		String codigoTarifario=parametros.getCodigoTarifario();
		Log4JManager.info("\n\n CODIGO TARIFARIO >>>>>>>>>>>> "+codigoTarifario);
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+""; //TODO CAMBIAR ESTO  ojooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
		}
		String order = "";
		
		
		
		String consultaStr= " SELECT DISTINCT "+ 
		
		" plant.servicio as codigoServicio , "+
		" getnombreservicio(plant.servicio, "+ codigoTarifario + ")  as nombreServicio, "+  
		" getcodigoservicio(plant.servicio, " + codigoTarifario + ") as  codigoamostrar  "+
		" FROM odontologia.log_programas_servicios_plan_t  plant "+ 
		" INNER JOIN odontologia.log_det_plan_tratamiento dpt ON (dpt.codigo_pk =plant.log_det_plan) "+ 
		" INNER JOIN odontologia.log_plan_tratamiento pt ON (pt.codigo_pk=dpt.log_plan_tratamiento ) "+  
		" LEFT OUTER JOIN  odontologia.motivos_atencion mcc ON (mcc.codigo_pk = plant.motivo) "+ 
		" WHERE plant.servicio IS NOT NULL "+
		" AND plant.log_det_plan = "+parametros.getLogDetPlanTratamiento()+  
		" AND plant.programa = "+ parametros.getPrograma().getCodigo()+
		" AND plant.por_confirmar='"+ConstantesBD.acronimoNo+"'"; // SIEMPRE ES POR CONFIRMAR EN NO
		
		/*
		 *VALIDAR ESTADO DE LOS PROGRAMAS SERVICIOS SI ESTA EVOLUCION  
		 */
		if(parametros.getEstadosProgramasOservicios().size() >0 )
		{
			consultaStr += "and plant.estado_servicio in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(parametros.getEstadosProgramasOservicios())+") ";
		}
		
		Connection con = UtilidadBD.abrirConexion();
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			Log4JManager.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios info = new InfoServicios();
				info.getServicio().setCodigo(rs.getInt("codigoServicio"));
				info.getServicio().setNombre(rs.getString("nombreServicio"));
				info.setCodigoMostrar(rs.getString("codigoamostrar"));
				/*
				 * CARGAR INFORMACION SERVICIOS 
				 */
				consultaServicioProgramasDetalle( info ,  con,  parametros);
			
				listaServicios.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("error en carga"+e);
		}
		SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(null, null, con);
		
		return listaServicios;
	}
	
	
	
	
	/**
	 * CONSULTA INFORMACION DE UN SERVICIO ESPECIFICO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoServicio
	 * @param con
	 */
	public static void consultaServicioProgramasDetalle(InfoServicios info , Connection con, DtoProgramasServiciosPlanT parametros)
	{
		
			String consulta="select " +
							" plant.codigo_pk as codigoPk , "+ 
							" plant.estado_servicio as estadoServicio , "+  
							" coalesce(plant.cita,-1) as cita, "+
							" coalesce(plant.inclusion,'') as inclusion, "+  
							" coalesce(plant.exclusion,'') as exclusion,  "+
							" coalesce(plant.garantia,'') as garantia  "+
							" from odontologia.log_programas_servicios_plan_t " +
							" plant " +
							" where servicio= ? " +
							" AND plant.log_det_plan = ? " +
							" AND plant.programa = ? " +
							" AND plant.por_confirmar='"+ConstantesBD.acronimoNo+"'";
			
			consulta+=" order by plant.codigo_pk desc limit 1"; // TODO CAMBIAR A ORACLE
			
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
				ps.setInt(1, info.getServicio().getCodigo());
				ps.setBigDecimal(2,parametros.getLogDetPlanTratamiento());
				ps.setDouble(3, parametros.getPrograma().getCodigo());
				Log4JManager.info("CONSULTA DE SERVCIOS ");
				Log4JManager.info(ps);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					info.setEstadoServicio(rs.getString("estadoServicio"));
					info.setCodigoCita(rs.getBigDecimal("cita"));
					info.setInclusion(rs.getString("inclusion"));
					info.setExclusion(rs.getString("exclusion"));
					info.setGarantia(rs.getString("garantia"));
				}
				
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			}
			catch(Exception e)
			{
				Log4JManager.info(" Error "+e);
			}
	}
	
	
	
	
	/**
	 * CARGAR EL ULTIMO CODIGO PK DE LOS HISTORICOS DE PROGRAMAS SERVICIOS PLAN TRATAMIENTO 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoProgSer
	 * @return
	 * @deprecated
	 * 
	 */
	public static BigDecimal cargarUltimoCodigoPkOdontogramaPlanTratamientoHistoricos(DtoProgramasServiciosPlanT dtoProgSer)
	{
		
		BigDecimal codigoPk=BigDecimal.ZERO;
		
		String consulta=" select max(plant.codigo_pk) as codigoPKprograma FROM  odontologia.log_programas_servicios_plan_t plant "+ 
						" INNER JOIN "+ 
						" odontologia.log_det_plan_tratamiento dpt ON(dpt.codigo_pk=plant.log_det_plan)"+ 
						" INNER JOIN odontologia.programas pro ON (plant.programa = pro.codigo) "+
						" LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON (co.consecutivo = pro.convencion) "+ 
						" LEFT OUTER JOIN odontologia.motivos_atencion mcc ON (mcc.codigo_pk = plant.motivo)"+ 
						" WHERE plant.log_det_plan=? and plant.por_confirmar='"+ConstantesBD.acronimoNo+"' " ;
		try 
		{
			Connection con=UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setBigDecimal(1, dtoProgSer.getLogDetPlanTratamiento());
			
			Log4JManager.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				codigoPk=rs.getBigDecimal("codigoPKprograma");
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		
		catch (SQLException e) 
		{
			Log4JManager.info("Error "+e);
		}
		
		return codigoPk;
	}
	
	
	
	/**
	 * CONSULTAR  PROGRAMAS PARA LA PROXIMA CITA DE UN ODONTOGRAMA DE EVOLUCION
	 * @author Edgar Carvajal Ruiz
	 */
	public static ArrayList<InfoProgramaServicioPlan> consultarProximaCitaProgramasServicios(DtoOdontograma dtoOdongrama , DtoPlanTratamientoOdo dtoPlanTrata ,int institucion )
	{ 
		ArrayList<InfoProgramaServicioPlan> listaProgramas = new ArrayList<InfoProgramaServicioPlan>();
		
		/*
		 * 1. PRIMERO CONSULTAR EL PROGRAMA 
		 */
		String consulta= 
					" select " +
					"	p.codigo as codigoPkPrograma , " +
					"	p.nombre as nombrePrograma  , " +
					"	p.codigo_programa as codigoPrograma " +
					"   " +
					" FROM "+
					" 	odontologia.programas_hallazgo_pieza php "+ 
					" INNER JOIN "+ 
					"	odontologia.servicios_cita_odontologica sco  on(sco.programa_hallazgo_pieza=php.codigo_pk and php.plan_tratamiento=?) "+
					" INNER JOIN "+  	
					"	odontologia.citas_odontologicas co on(co.codigo_pk=sco.cita_odontologica and co.evo_genera_prox_cita=?)  " + //  
					" INNER JOIN  " +
					"	odontologia.programas p on(p.codigo=php.programa)" +
					"		";

		try{
			
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			ps.setBigDecimal(1, dtoPlanTrata.getCodigoPk());
			ps.setDouble(2, dtoOdongrama.getEvolucion());
			Log4JManager.info("CONSULTA- BUSQUEDA DE PROXIMAS CITAS "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			
			
			
			
			while(rs.next())
			{
				InfoProgramaServicioPlan dtoInfoPrograma = new InfoProgramaServicioPlan();
				dtoInfoPrograma.setCodigoAmostrar(rs.getString("codigoPrograma"));
				dtoInfoPrograma.setCodigoPkProgramaServicio(rs.getBigDecimal("codigoPkPrograma"));
				dtoInfoPrograma.setNombreProgramaServicio(rs.getString("nombrePrograma"));
				
				/*
				 * CARGAR SERVICIOS DEL PROGRAMA
				 */
				dtoInfoPrograma.setListaServicios(consultarProximoServicioCita(dtoOdongrama, institucion, rs.getBigDecimal("codigoPkPrograma"), dtoPlanTrata ));
				/*
				 * ADICIONAR
				 */
				listaProgramas.add(dtoInfoPrograma);
			}
			
		}
		catch (SQLException e) 
		{
			Log4JManager.info("Error "+e);
		}
	
		
		
		return listaProgramas;
		
	}
	
	
	
	
	
	/**
	 * CONSULTAR SERVICIOS PROXIMA CITA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static  ArrayList<InfoServicios> consultarProximoServicioCita(DtoOdontograma dtoOdongrama ,int institucion , BigDecimal codigoPrograma, DtoPlanTratamientoOdo dtoPlan )
	
	{
		
		ArrayList<InfoServicios> listaServicios = new  ArrayList<InfoServicios>();
		
		String consulta=
		" select " +
		" sco.servicio as servicio , " +
		" getnombreservicio(sco.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+")  as nombreServicio , " +
		" getcodigoservicio(sco.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") as  codigoamostrar  " +
		"   " +
		
		" FROM "+
		"	 odontologia.programas_hallazgo_pieza php "+ 
		" INNER JOIN "+ 
		" 	odontologia.servicios_cita_odontologica sco on(sco.programa_hallazgo_pieza=php.codigo_pk and php.programa=? and php.plan_tratamiento=?) "+
		" INNER JOIN "+  	
		"	odontologia.citas_odontologicas co on(co.codigo_pk=sco.cita_odontologica and co.evo_genera_prox_cita=?)  " +
		" INNER JOIN  " +
		"	odontologia.programas p on(p.codigo=php.programa) "; 
		
		
		try{
			
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			ps.setBigDecimal(1, codigoPrograma);
			ps.setBigDecimal(2, dtoPlan.getCodigoPk());
			ps.setDouble(3, dtoOdongrama.getEvolucion());
			
			Log4JManager.info("CONSULTA- BUSQUEDA DE PROXIMAS CITAS "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoServicios dtoInfoServicio = new InfoServicios();
				dtoInfoServicio.getServicio().setCodigo(rs.getInt("servicio"));
				dtoInfoServicio.getServicio().setNombre(rs.getString("nombreServicio"));
				dtoInfoServicio.setCodigoMostrar(rs.getString("codigoamostrar"));
				listaServicios.add(dtoInfoServicio);
			}
		}
		catch (SQLException e) 
		{
			Log4JManager.info("Error "+e);
		}
		
		return listaServicios;
		
	}
	
	
	
	
	
	
	
	/**
	 * CARGAR MAXIMO CODIGO  LOG PLAN TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @return
	 */
	public static BigDecimal cargarMaximoCodigoLogPlantratamiento(DtoPlanTratamientoOdo dtoPlan, Connection con )
	
	{
		
		BigDecimal codigoPk=BigDecimal.ZERO;
		
		String consultaStr= " select max(codigo_pk) as codigoPk from odontologia.log_plan_tratamiento where plan_tratamiento=? ";
		
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, dtoPlan.getCodigoPk() );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		
			
			if( rs.next())
			{
				codigoPk = rs.getBigDecimal("codigoPk");
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			
			
		}
		catch (SQLException e) {
			Log4JManager.info(e); // TODO LIMPIAR ESTO
			Log4JManager.error(e);
			
		}

		
		
		
		
		return codigoPk;
	}
	
	
		
	
	
	
	
	/**
	 * Carga el último plan de tratamiento del paciente
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static BigDecimal obtenerUltimoCodigoPlanTratamiento (int codigoPaciente) 
	{
		String consultaStr=" select coalesce(max(codigo_pk), "+ConstantesBD.codigoNuncaValido+") as codigoPk from odontologia.plan_tratamiento where codigo_paciente="+codigoPaciente+" "; 
		
		BigDecimal codigo= new BigDecimal(ConstantesBD.codigoNuncaValido); 
		
		Log4JManager.info("obtenerUltimoCodigoPlanTratamiento");
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigo= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> ",e);
		}
			
		return codigo;
	}




	/**
	 * Consultar todos los planes de tratamiento del paciente sin importar
	 * ingreso.
	 * @param codigoPaciente Código del paciente
	 * @param institucion Institución a la cual pertenece el paciente
	 * @return {@link DtoPlanTratamientoOdo} con los datos del último plan de tratamiento
	 */
	public static DtoPlanTratamientoOdo consultarPlanTratamientoPaciente(int codigoPaciente, int institucion)
	{
		Connection con = UtilidadBD.abrirConexion();
		
		DtoPlanTratamientoOdo dtoTratamiento = new DtoPlanTratamientoOdo();
		String consulta = 	
			"SELECT  " +
				"plt.codigo_pk AS codigopk, " +
				"plt.consecutivo AS consecutivo, " +
				"plt.codigo_paciente AS codpaciente, " +
				"plt.ingreso AS ingreso, " +
				"plt.fecha_grabacion AS fechagrabacion, " +
				"plt.hora_grabacion AS horagrabacion, " +
				"plt.odontograma_diagnostico AS odontogramadiag, " +
				"plt.estado AS estado, " +
				"coalesce(plt.odontograma_evolucion,0) AS odontogramaevol, " +
				"plt.por_confirmar AS porconfirmar, " +
				"plt.institucion AS institucion, " +
				"plt.centro_atencion AS centroatencion " +
			"FROM " +
				"odontologia.plan_tratamiento plt " +
			"WHERE " +
				"plt.institucion = ? "+
				"AND codigo_paciente=? " +
			"ORDER BY plt.codigo_pk";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setInt(1,institucion);
			ps.setInt(2,codigoPaciente);
			Log4JManager.info(ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dtoTratamiento.setCodigoPk(rs.getBigDecimal("codigopk"));
				dtoTratamiento.setConsecutivo(rs.getBigDecimal("consecutivo"));
				dtoTratamiento.setCodigoPaciente(rs.getInt("codpaciente"));
				dtoTratamiento.setIngreso(rs.getInt("ingreso"));
				dtoTratamiento.setFechaGrabacion(rs.getString("fechagrabacion"));
				dtoTratamiento.setHoraGrabacion(rs.getString("horagrabacion"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
				dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
				dtoTratamiento.setEstado(rs.getString("estado"));
				dtoTratamiento.setPorConfirmar(rs.getString("porconfirmar"));
				dtoTratamiento.setInstitucion(rs.getInt("institucion"));
				dtoTratamiento.setCentroAtencion(rs.getInt("centroatencion"));
				dtoTratamiento.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				dtoTratamiento.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				dtoTratamiento.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				dtoTratamiento.setOdontogramaDiagnostico(rs.getBigDecimal("odontogramadiag"));
				dtoTratamiento.setOdontogramaEvolucion(rs.getBigDecimal("odontogramaevol"));
				dtoTratamiento.setCodigoEvolucion(rs.getBigDecimal("evolucion"));
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			//Log4JManager.info(UtilidadLog.obtenerStringHerencia(dtoTratamiento, true));	
			Log4JManager.info("\nConsultar Sql Plan de tratamiento------------>"+ps);	
			Log4JManager.info("\n\n\n\n\n\n\n\n\n\n");
			Log4JManager.info("**************************************************************************************************************");
			
		}catch (Exception e) {
			e.printStackTrace();
			Log4JManager.info("Error--Consulta: "+consulta);
		}
		
		return dtoTratamiento;
	}
 
	
	/**
	 * Método que obtiene el codigo de los detalles de plan de tratamiento asociados a un
	 * programa Hallazgo pieza específico.
	 * 
	 * @param programaHallazgoPieza
	 * @return
	 */
	public static int obtenerDetPlanTratamientoXProgramaHallazgoPieza(int programaHallazgoPieza)
	{
		int detPlanTratamiento = ConstantesBD.codigoNuncaValido;
		
		String consultaStr="SELECT " +
								"spp.det_plan_trata as det_plan_trata " +
							"FROM " +
								"odontologia.superficies_x_programa spp " +
							"WHERE " +
								"spp.prog_hallazgo_pieza = ? ";
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, programaHallazgoPieza);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				detPlanTratamiento = rs.getInt("det_plan_trata");
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga==> "+e);
		}
			
		return detPlanTratamiento;
	}
	
	/**
	 * Método que verifica si el servicio se encuentra asociado a otra cita
	 * @param con
	 * @param codigoCita
	 * @param codigoProServPlanTrat
	 * @return true en caso de estar asociado, false de lo contrario
	 */
	public static ArrayList<DtoServicioOdontologico> ObtenerServicioAsociadoAOtraCita(Connection con, int codigoCita, int codigoProgHallPieza, int servicio, int codigoPaciente)
	{
		ArrayList<DtoServicioOdontologico> serviciosOdontologicos = new  ArrayList<DtoServicioOdontologico>();
		
		String sentenciaSQL="";
		ResultSetDecorator rsd = null;
		PreparedStatementDecorator psd = null;
		
		try{
			sentenciaSQL =  " SELECT " +
			"co.codigo_pk AS codigo, co.estado as estado, ao.fecha as fecha " +
			"FROM odontologia.programas_hallazgo_pieza php " +
			"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
			"INNER JOIN odontologia.servicios_cita_odontologica sco ON (sco.programa_hallazgo_pieza=php.codigo_pk) " +
			"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk=sco.cita_odontologica) " +
			"INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
			"INNER JOIN odontologia.programas_servicios_plan_t pspt ON (pspt.det_plan_tratamiento = dpt.codigo_pk  and sco.servicio=pspt.servicio AND pspt.activo = 'S' ) " +
			"INNER JOIN odontologia.agenda_odontologica ao ON (ao.codigo_pk = co.agenda ) "+
			"WHERE " +
				"(co.estado='"+ConstantesIntegridadDominio.acronimoAsignado+"' OR co.estado='"+ConstantesIntegridadDominio.acronimoReservado+"')" +
				" AND " +
					" co.codigo_pk!= ? " +
				" AND " +
					" php.codigo_pk=? " +
				" AND " +
					" sco.servicio=? " +
				" AND " +
				"	sco.activo = '"+ConstantesBD.acronimoSi +"'";
			
			
			psd=new PreparedStatementDecorator(con, sentenciaSQL);
			psd.setInt(1, codigoCita);
			psd.setInt(2, codigoProgHallPieza);
			psd.setInt(3, servicio);	
				
			Log4JManager.info(psd);
			rsd=new ResultSetDecorator(psd.executeQuery());
				
			if(rsd==null || !rsd.next()){
				
				sentenciaSQL =  "SELECT " +
				" co.codigo_pk AS codigo, co.estado as estado, co.fecha_programacion as fecha " +
				"FROM odontologia.programas_hallazgo_pieza php " +
				"INNER JOIN odontologia.superficies_x_programa sxp ON (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
				"INNER JOIN odontologia.servicios_cita_odontologica sco ON (sco.programa_hallazgo_pieza=php.codigo_pk) " +
				"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk=sco.cita_odontologica) " +
	  		    "INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
				"INNER JOIN odontologia.programas_servicios_plan_t pspt ON (pspt.det_plan_tratamiento = dpt.codigo_pk  and sco.servicio=pspt.servicio AND pspt.activo = 'S' ) " +
				"WHERE " +
						"(co.estado='"+ConstantesIntegridadDominio.acronimoProgramado+"')" +
					" AND " +
						"php.codigo_pk=? " +
					" AND " +
						" sco.servicio=? " +
					" AND " +
					"	co.codigo_pk NOT IN (SELECT cita_programada FROM citas_asociadas_a_programada) "+
					" AND " +
					"	sco.activo = '"+ConstantesBD.acronimoSi +"'";

				
				psd=new PreparedStatementDecorator(con, sentenciaSQL);
	
				psd.setInt(1, codigoProgHallPieza);
				psd.setInt(2, servicio);
				
				rsd=new ResultSetDecorator(psd.executeQuery());
				
				if(rsd!=null && rsd.next()){
					
					DtoServicioOdontologico dtoServicio = new DtoServicioOdontologico();
					dtoServicio.setCodigoCita(rsd.getInt("codigo"));
					dtoServicio.setFechaCita(UtilidadFecha.conversionFormatoFechaAAp(rsd.getDate("fecha")));
					dtoServicio.setEstadoCitaVinculadoServicio(rsd.getString("estado"));
					serviciosOdontologicos.add(dtoServicio);
				}
				
			}else{
				
				DtoServicioOdontologico dtoServicio = new DtoServicioOdontologico();
				dtoServicio.setCodigoCita(rsd.getInt("codigo"));
				dtoServicio.setFechaCita(UtilidadFecha.conversionFormatoFechaAAp(rsd.getDate("fecha")));
				dtoServicio.setEstadoCitaVinculadoServicio(rsd.getString("estado"));
				serviciosOdontologicos.add(dtoServicio);
				
			}
		
	
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			return serviciosOdontologicos;
		}
		catch (Exception e) {
			Log4JManager.info("Error consultando el uso de los servicios",e);
			return null;
		}
	}
}