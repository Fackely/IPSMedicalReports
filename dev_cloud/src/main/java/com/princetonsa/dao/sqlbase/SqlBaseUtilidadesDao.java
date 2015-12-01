/*
 * Creado en 4/02/2005
 *
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase;
 
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.cache.CacheFacade;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Juan David Ramï¿½rez Lï¿½pez
 *
 * Princeton S.A.
 */
@SuppressWarnings("unchecked")
public class SqlBaseUtilidadesDao
{

	private static  Logger logger=Logger.getLogger(SqlBaseUtilidadesDao.class);

	/**
	 * 
	 * 
	 * 
	 */
	private static String inserccionProcAutoEst = "INSERT INTO odontologia.log_proc_auto_estados(   " +
												"codigo_pk , " +//1
												"inconsistencia , " +//2
												"fecha , " +//3
												"hora , " +//4
												"presupuesto , " +//5
												"institucion , " +//6
												"plan_tratamiento  ) " +//7
												
												
											"values (" +
												"? , " +//1
												"? , " +//2
												"? , " +//3
												"? , " +//4
												"? , " +//5
												"? , " +//6
												
												
												"? ) ";//7

	
	
	/**
	 * String SQL para verificar si una solicitud tiene desoachos
	 */
	private static String hayDespachosEnSolicitudStr="SELECT count(1) AS numResultados FROM (SELECT numero_solicitud FROM solicitudes WHERE tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+") s1 INNER JOIN despacho d on (s1.numero_solicitud=d.numero_solicitud) WHERE  s1.numero_solicitud=?";
	
	
	/**
	 * String SQL verifica cuantas solicitudes se encuentran o no en un o unos estado de historia clinica a partir de la cuenta
	 * */
	private static String strCuantosSolicitudesEstadoClinica = "SELECT count(numero_solicitud) FROM solicitudes WHERE cuenta = ? AND tipo = ? AND estado_historia_clinica ";
	
	/**
	 * String SQL para cargar la Fecha-Hora del egreso de la cuenta Urgencias cuando
	 * se hace un Asocio de Cuenta
	 */
	private static final String capturarFechayHoraEgresoUrgenciasEnAsocioStr="SELECT "+ 
		"coalesce(to_char(eg.fecha_egreso,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha, "+
		"coalesce(eg.hora_egreso,'') as hora "+ 
		"FROM egresos eg "+ 
		"INNER JOIN cuentas cu ON(cu.id=eg.cuenta) "+ 
		"WHERE "+ 
		"cu.id_ingreso=? "+ 
		"AND cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") and cu.via_ingreso = ? ";
	
	/**
	 * Sentencia para obtener el ï¿½ltimo
	 */
	private static final String obtenerUltimoContrtatoPacienteStr="SELECT " +
		"ct.codigo AS codigoContrato " +
		"FROM cuentas c " +
		"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso) " +
		"INNER JOIN contratos ct ON(ct.codigo=sc.contrato) " +
		"WHERE " +
		"c.codigo_paciente=? AND " +
		"c.fecha_apertura=(SELECT max(fecha_apertura) FROM cuentas WHERE codigo_paciente=?)";

	/**
	 * Cadena para obtener el destino de salida de un egreso evolutivo
	 * cuando el paciente estï¿½ vivo
	 */
	private static final String obtenerDestinoSalidaEgresoEvolucionStr="SELECT destino_salida as destino, CASE WHEN otro_destino_salida IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE otro_destino_salida end as otro_destino FROM manejopaciente.egresos where cuenta=? and estado_salida="+ValoresPorDefecto.getValorFalseParaConsultas();
	
	/**
	 * Cadena para obtener el numero de solicitudes de una cuenta
	 */
	private static final String obtenerNumeroSolicitudesCuentaStr="SELECT count(1) AS numero_solicitudes FROM solicitudes WHERE cuenta=?";
	
	/**
	 * Cadena para verificar si el tipo de identificaciï¿½n lleva
	 * consecutivo automï¿½tico
	 */
	private static final String esAutomaticoTipoIdStr=" SELECT es_consecutivo AS es_consecutivo FROM tipos_id_institucion WHERE acronimo=? AND institucion=?";
	
	/**
	 * Cadena para obtener el numero de la factura (prefijo+consecutivo)
	 */
	private static final String obtenerNumeroFacturaStr="SELECT "+
		"CASE WHEN f.pref_factura IS NULL THEN " +
		"' '||  f.consecutivo_factura " +
		"ELSE  " +
		"f.pref_factura ||' '|| f.consecutivo_factura " +
		"END AS documento " +
		"FROM facturas f " +
		"INNER JOIN det_factura_solicitud dfs ON (dfs.factura=f.codigo) " +
		"WHERE dfs.solicitud=?";
	
	/**
	 * cadena para actualizar el diagnostico del pago del paciente
	 */
	private static final String actualizarDiagnosticoTopesPacienteStr="UPDATE " +
			"pagos_paciente SET diagnostico=?,tipo_cie=? WHERE paciente=? AND documento=?";
	
	/**
	 * cadena para obtener la ï¿½tlima via de ingreso del paciente
	 */
	private static final String obtenerCodigoUltimaViaIngresoStr="SELECT " +
			"c.via_ingreso AS via_ingreso FROM cuentas c WHERE c.codigo_paciente=? ORDER BY c.fecha_apertura DESC,c.hora_apertura DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
	
	/**
	 * cadena para obtener el ID de la ï¿½tlima cuenta del paciente
	 */
	private static final String PostgObtenerIdUltimaCuentaStr="SELECT c.id " +
		"FROM cuentas c " +
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')) " +
		"WHERE c.codigo_paciente=? ORDER BY c.fecha_apertura DESC,c.hora_apertura DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
	
	private static final String OraObtenerIdUltimaCuentaStr="SELECT c.id " +
	"FROM manejopaciente.cuentas c " +
	"INNER JOIN manejopaciente.ingresos i ON(i.id=c.id_ingreso AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')) " +
	"WHERE c.codigo_paciente=? AND rownum = 1 ORDER BY c.fecha_apertura DESC,c.hora_apertura DESC";
	
	private static final String verificarAutorizacionStr="SELECT validar_autorizacion FROM atributos_solicitud WHERE codigo=?";
	
	/**
	 * cadena que cuenta el nï¿½mero de atributos de la justificacion de un medicamento
	 */
	private static final String esMedicamentoJustificadoStr="SELECT " +
			"count(1) AS cuenta " +
			"FROM desc_atributos_solicitud das, atributos_solicitud s " +
			"WHERE " +
			"das.numero_solicitud=? AND " +
			"das.articulo=? AND " +
			"s.institucion=? AND " +
			"das.atributo=s.codigo AND " +
			"s.es_requerido_ser="+ValoresPorDefecto.getValorTrueParaConsultas();
	
	/**
	 * cadena que cuenta el nï¿½mero de atributos de la justificaciï¿½n de un servicio
	 */
	private static final String esServicioJustificadoStr="SELECT " +
			"count(1) AS cuenta " +
			"FROM desc_atr_sol_int_proc das, atributos_solicitud s " +
			"WHERE " +
			"das.numero_solicitud=? AND " +
			"das.servicio=? AND " +
			"s.institucion=? AND " +
			"das.atributo=s.codigo " +
			"AND s.es_requerido_ser="+ValoresPorDefecto.getValorTrueParaConsultas();
	
	/**
	 * cadena para obtener los atributo y la descripcion de la justificacion de un medicamento
	 */
	private static final String obtenerAtributosJustificacionMedicamentosStr="SELECT " +
			"atributo AS atributo,descripcion AS descripcion " +
			"FROM desc_atributos_solicitud " +
			"WHERE " +
			"numero_solicitud=? AND articulo=?";
	
	/**
	 * cadena para obtener los atributos y la descripcion de la justificaciï¿½n de un servicio
	 */
	private static final String obtenerAtributosJustificacionServiciosStr="SELECT " +
			"atributo AS atributo,descripcion AS descripcion " +
			"FROM desc_atr_sol_int_proc " +
			"WHERE " +
			"numero_solicitud=? AND servicio=?";
	
	/**
	 * Sentencia para obtener el ï¿½ltimo cï¿½digo del embarazo del paciente activo
	 */
//	private static final String obtenerUltimoNumeroEmbarazoStr="SELECT max(embarazo) as numero_embarazo from hoja_obstetrica WHERE paciente = ?";
	private static final String obtenerUltimoNumeroEmbarazoStr="SELECT max(codigo) as numero_embarazo FROM  (SELECT max(codigo) as codigo from ant_gineco_embarazo angine " +
																	  " WHERE fin_embarazo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
																      " AND codigo_paciente = ? " + 
																	  " UNION " + 
																	  " SELECT max(embarazo) as codigo from hoja_obstetrica hoja WHERE paciente = ?) pacEmb ";
	
	
		

	/**
	 * carga el esquema tarifario de una solicitud de cx
	 */
	private static final String getEsquemaTarifarioSolCxStr= "SELECT esquema_tarifario AS esquemaTarifario FROM sol_cirugia_por_servicio WHERE codigo=?";
	
	
	
	/**
	 * obtiene los datos del tipo de monitoreo
	 */
	private static final String getTipoMonitoreoStr="SELECT codigo,nombre,prioridad_cobro,servicio FROM tipo_monitoreo WHERE codigo=?";
	
	/**
	 * obtiene la fecha de orden de salida de una cuenta de Urgencias u Hospitalizaciï¿½n
	 */
	private static final String obtenerFechaOrdenSalidaStr="SELECT e.fecha_evolucion AS fecha "+
		"FROM solicitudes s "+ 
		"INNER JOIN evoluciones e ON(s.numero_solicitud=e.valoracion) "+
		"INNER JOIN cuentas c ON(c.id=s.cuenta) "+
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+
		"WHERE " +
		"(s.tipo="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" OR " +
			"s.tipo="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") AND " +
		"e.orden_salida="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND s.cuenta=? AND i.institucion=? ORDER BY e.codigo DESC";
	
	/**
	 * Cadena que consulta el numero de una solicitud estancia de una cuenta
	 * en una fecha determinada
	 */
	private static final String haySolicitudEstanciaStr="SELECT s.numero_solicitud " +
			"FROM solicitudes s,ingresos i,cuentas c " +
			"WHERE s.cuenta=c.id AND c.id_ingreso=i.id AND " +
			"c.id=? AND s.tipo="+ConstantesBD.codigoTipoSolicitudEstancia+" AND s.fecha_solicitud=? AND i.institucion=?";
	
	/**
	 * Cadena que consulta los datos de una persona
	 */
	private static final String obtenerDatosPersonaStr="SELECT "+ 
		"p.codigo AS codigo,"+
		"p.numero_identificacion AS numero_id,"+
		"p.tipo_identificacion AS tipo_id,"+
		"getnombretipoidentificacion(p.tipo_identificacion) AS nom_tipo_id, " +
		"p.codigo_pais_nacimiento AS codigo_pais_nacimiento, "+
		"p.codigo_departamento_nacimiento AS codigo_depto_nacimiento,"+
		"p.codigo_ciudad_nacimiento AS codigo_ciudad_nacimiento,"+
		"getnombreciudad(p.codigo_pais_nacimiento,p.codigo_departamento_nacimiento,p.codigo_ciudad_nacimiento) AS ciudad_nacimiento,"+
		"to_char(p.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento,"+
		"to_char(p.fecha_nacimiento,'YYYY') AS anio,"+
		"to_char(p.fecha_nacimiento,'MM') AS mes,"+
		"to_char(p.fecha_nacimiento,'DD') AS dia,"+
		"p.estado_civil AS codigo_estado_civil,"+
		"ec.nombre AS estado_civil,"+
		"p.sexo AS codigo_sexo,"+
		"s.nombre AS sexo,"+
		"p.primer_nombre AS primer_nombre,"+
		"p.segundo_nombre AS segundo_nombre,"+
		"p.primer_apellido AS primer_apellido,"+
		"p.segundo_apellido AS segundo_apellido,"+
		"administracion.getnombremedico(p.codigo) AS nombre,"+
		"p.direccion AS direccion,"+
		"p.codigo_pais_vivienda AS codigo_pais_vivienda, "+
		"p.codigo_departamento_vivienda AS codigo_depto_vivienda,"+
		"p.codigo_ciudad_vivienda AS codigo_ciudad_vivienda,"+
		"getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda) AS ciudad_vivienda,"+
		"p.codigo_barrio_vivienda AS codigo_barrio,"+
		"CASE WHEN b.codigo_barrio is null then '' ELSE b.codigo_barrio || ' - ' || b.descripcion END AS barrio,"+
		"coalesce(p.telefono,'') AS telefono,"+
		"coalesce(p.telefono_celular,"+ConstantesBD.codigoNuncaValido+") AS telefono_celular,"+
		"coalesce(p.telefono_fijo,"+ConstantesBD.codigoNuncaValido+") AS telefono_fijo,"+
		"p.email AS email," +
		"p.codigo_pais_id AS codigo_pais_id, " +
		"p.codigo_ciudad_id AS codigo_ciudad_id, " +
		"p.codigo_depto_id As codigo_depto_id,  " +
		"getnombreciudad(p.codigo_pais_id,p.codigo_depto_id,p.codigo_ciudad_id) AS ciudad_id, "+
		"getnombredepto(p.codigo_pais_id,p.codigo_depto_id) AS depto_id "+
		"FROM personas p "+ 
		"LEFT OUTER JOIN estados_civiles ec ON(ec.acronimo=p.estado_civil) "+ 
		"INNER JOIN sexo s ON(s.codigo=p.sexo) "+ 
		"LEFT OUTER JOIN barrios b ON(b.codigo_pais=p.codigo_pais_vivienda AND b.codigo_departamento=p.codigo_departamento_vivienda AND b.codigo_ciudad=p.codigo_ciudad_vivienda AND b.codigo=p.codigo_barrio_vivienda) "+
		"WHERE p.numero_identificacion=? AND p.tipo_identificacion=?";
	
	/**
	 * Cadena que consulta una funcionalidad que no trabaja
	 * con paciente cargado en sesión
	 */
	private static final String deboEsconderPacienteStr="SELECT codigo_func FROM funcionalidad_sin_pac WHERE codigo_func=?";
	
	/**
	 * Cadena que consulta la descripciï¿½n de un tipo de sala
	 */
	private static final String obtenerNombreTipoSalaStr="SELECT descripcion AS tipo_sala FROM tipos_salas WHERE codigo=?";
	
	/**
	 * Cadena que consulta el nombre y acronimo de un tipo de asocio
	 */
	private static final String obtenerNombreTipoAsocioStr="SELECT " +
			"a.nombre_asocio AS nombre, a.codigo_asocio AS acronimo " +
			"FROM tipos_asocio a WHERE a.codigo=?";
	
	/**
	 * Cadena que consulta el nombre ed un convenio dado la cuenta
	 */
	private static final String obtenerNombreConvenioStr="SELECT getnombreconvenio(sc.convenio) as nombreConvenio FROM cuentas cue INNER JOIN sub_cuentas sc ON(sc.ingreso=cue.id_ingreso AND sc.nro_prioridad = 1) where cue.id=?";
	
	/**
	 * Cadena que cuenta la solicitud que tenga un pedido !x asociado 
	 */
	private static final String tienePedidoSolicitudQxStr = "SELECT count(1) AS resultado " +
			"FROM solicitudes_cirugia s " +
			"INNER JOIN peticion_qx p ON(p.codigo=s.codigo_peticion) " +
			"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=p.codigo) " +
			"WHERE " +
			"s.numero_solicitud=?";
	
	
	/**
	 * Cadena que cuenta el pedido  que tenga una solicitud asociado 
	 */
	private static final String tieneSolicitudPeticionQxStr = " SELECT count(1) AS resultado " +
															" FROM solicitudes_cirugia s " +
															" WHERE " +
															" s.numero_solicitud IS NOT NULL " +
															" AND s.codigo_peticion=?";
	
	/**
	 * Cadena que consulta la subcuenta de una solicitud
	 */
	private static final String getSubCuentaSolicitudStr = "SELECT " +
			"sub_cuenta AS subcuenta " +
			"FROM solicitudes_subcuenta WHERE solicitud = ? AND eliminado='"+ConstantesBD.acronimoNo+"' ";
	
	/**
	 * Cadena que consulta la cuenta de una solicitud
	 */
	private static final String  getCuentaSolicitudStr = "SELECT " +
			"cuenta AS cuenta " +
			"FROM solicitudes WHERE numero_solicitud = ?";
	
	/**
	 * Cadena que consulta el nombre de la especialidad
	 */
	private static final String getNombreEspecialidadStr = "SELECT " +
			"nombre AS nombre FROM especialidades WHERE codigo = ?";
	
	/**
	 * Cadena que consulta el nombre del pool
	 */
	private static final String getNombrePoolStr = "SELECT " +
			"descripcion AS descripcion FROM pooles WHERE codigo = ?";
	
	/**
	 * Cadena que consulta la peticion de una solicitud de cirugï¿½a
	 */
	private static final String getPeticionSolicitudCxStr ="SELECT " +
			"codigo_peticion AS peticion FROM solicitudes_cirugia WHERE numero_solicitud =?";
	
	/**
	 * Cadena que consulta la fecha del cargo de una orden de cirugï¿½a
	 */
	private static final String getFechaCargoOrdenCirugiaStr = "SELECT "+ 
		"CASE WHEN fecha IS NULL THEN '' ELSE to_char(fecha,'DD/MM/YYYY') END AS fecha_cargo "+
		"FROM liquidacion_qx "+
		"WHERE numero_solicitud = ? AND "+ 
		"(fecha,hora) NOT IN " +
		"(" +
			"SELECT fecha,hora " +
			"FROM cambios_liquidacion_qx cl,liquidacion_qx l " +
			"WHERE l.numero_solicitud=cl.numero_solicitud AND " +
			"cl.numero_solicitud = ? AND " +
			"(" +
				"cl.fecha_grabacion>l.fecha OR " +
				"(cl.fecha_grabacion=l.fecha AND cl.hora_grabacion>l.hora)" +
			") AND " +
			"tipo_cambio=" + ConstantesBD.codigoTipoCambioReversion + " "+
		") ";
	
	/**
	 * Cadena con el statement necesario para saber si un convenio tiene (False - True) el atributo
	 * de info_adic_ingreso_convenios
	 */
	private static final String convenioTieneIngresoInfoAdicStr=" SELECT c.info_adic_ingreso_convenios as infoAdicIngresoConvenios " +
																" FROM convenios c  " +
																" WHERE c.codigo=? ";
	
	/**
	 * Cadena constante con el statement necesario para obtener el saldo de una cuenta 
	 */
	private static final String obtenerSaldoConvenioStr=" SELECT sum(ip.valor_monto_autorizado) as saldo " +
														" FROM informacion_poliza ip " +
														" WHERE ip.sub_cuenta=? " ;
	
	
	/**
	 * Cadena constante para obtener el codigo del convenio de una cuenta dada
	 */
	public static final String obtenerCodigoConvenioStr=" SELECT sc.convenio as codigoConvenio " +
													    " FROM cuentas cue " +
													    " INNER JOIN sub_cuentas sc ON(sc.ingreso=cue.id_ingreso AND sc.nro_prioridad=1) " +
													    " WHERE cue.id=? ";
	
	/**
	 * Cadena constante para obtener la fecha de la admision de Urgencias
	 */
	public static final String obtenerFechaAdmisionUrgStr=" SELECT TO_CHAR (fecha_admision,'DD/MM/YYYY') as fechaAdmision " +
													      " FROM admisiones_urgencias" +
													      " WHERE cuenta=? ";
	
	/**
	 * Cadena constante para obtener la hora de la admision de Urgencias
	 */
	public static final String obtenerHoraAdmisionUrgStr=" SELECT hora_admision as horaAdmision " +
													      " FROM admisiones_urgencias" +
													      " WHERE cuenta=? ";
	
	/**
	 * Cadena constante para obtener la fecha de admision de Hospitalizacion
	 */
	public static final String obtenerFechaAdmisionHospStr=" SELECT TO_CHAR (fecha_admision,'DD/MM/YYYY') as fechaAdmision "+
	 													   " FROM admisiones_hospi " +
	 													   " WHERE cuenta=? ";
	
	/**
	 * Cadena constante para obtener la hora de admision de Hospitalizacion
	 */
	public static final String obtenerHoraAdmisionHospStr=" SELECT hora_admision as horaAdmision "+
	 													   " FROM admisiones_hospi " +
	 													   " WHERE cuenta=?";
	
	/**
	 * Cadena con el statement necesario para obtener el nombre de un grupo de servisio dado su codigo
	 */
	public static final String getNomberGrupoServiciosStr=" SELECT gs.descripcion as nombreGrupo " +
											              " FROM grupos_servicios gs " +
											              " WHERE gs.codigo=? ";
	
	/**
	 * Cadena con el statement necesario para obtener el total de u presupuesto dado
	 */
	public static final String obtenerTotalPresupuestoStr=" SELECT (SELECT case when sum(ps.cantidad*ps.valor_unitario)  is null then 0  " +
																		 " else sum(ps.cantidad*ps.valor_unitario) end " +
																		 " from presupuesto_servicios ps WHERE ps.presupuesto=?)+" +
																  "(SELECT case when sum(pa.cantidad*pa.valor_unitario) is null then 0 " +
																  		 " else sum(pa.cantidad*pa.valor_unitario) end " +
																  		 " from presupuesto_articulos pa WHERE pa.presupuesto=?) as totalPresupuesto";
	
	/**
	 * obtener el codigo de la cuenta asociada dado el codigo de la cuenta final
	 */
	public static final String getCuentaAsociadaStr="SELECT CASE WHEN cuenta_inicial IS NULL THEN '' ELSE cuenta_inicial||'' END AS cuentaInicial FROM asocios_cuenta WHERE cuenta_final=? ";
	
	/**
	 * Cadena que consulta la ï¿½ltima cama del traslado de una cuenta de hospitalizacion
	 */
	private static final String getUltimaCamaTrasladoStr = "SELECT codigo_nueva_cama AS cama FROM traslado_cama WHERE cuenta = ? ";
	
	
	/**
	 * Cadena que consulta la ï¿½ltima cama del traslado de una cuenta de hospitalizacion
	 */
	private static final String getCodigoTrasladoUltimoTrasladoStr = "SELECT codigo FROM traslado_cama WHERE cuenta = ? ORDER BY codigo DESC ";
	
	/**
	 * actualiza la fecha y hora de finalizacion
	 */
	private static final String actualizarFechaHoraFinalizacion=" UPDATE traslado_cama SET fecha_finalizacion=?, hora_finalizacion=? WHERE codigo=? ";
	
	/**
	 * Cadena que consulta los centros de costo x via ingreso x centro atencion
	 */
	private static final String getCentrosCostoXViaIngresoXCAtencionStr = " SELECT DISTINCT " +
		"c.codigo AS codigo, " +
		"c.nombre AS nombre " +
		"FROM centros_costo c "+
		"INNER JOIN centro_costo_via_ingreso cv ON(cv.centro_costo=c.codigo) ";
	
	
	/**
	 * Cadena que consulta los centros de costo x via ingreso x centro atencion
	 */
	private static final String getCentrosCostoXViaIngresoXCAtencionXtipoMonitoreoStr_1 = " SELECT DISTINCT " +
		"c.codigo AS codigo, " +
		"c.nombre AS nombre, " +
		"'' as nombre_via_ingreso, " +
		"'' as nombre_tipo_paciente " +
		"FROM centros_costo c "+
		"INNER JOIN centro_costo_via_ingreso cv ON(cv.centro_costo=c.codigo) ";
	
	/**
	 * Cadena que consulta los centros de costo x via ingreso x centro atencion
	 */
	private static final String getCentrosCostoXViaIngresoXCAtencionXtipoMonitoreoStr_2 = " SELECT DISTINCT " +
		"c.codigo AS codigo, " +
		"c.nombre AS nombre, " +
		"getnombreviaingreso(cv.via_ingreso) as nombre_via_ingreso, " +
		"getnombretipopaciente(cv.tipo_paciente) as nombre_tipo_paciente " +
		"FROM centros_costo c "+
		"INNER JOIN centro_costo_via_ingreso cv ON(cv.centro_costo=c.codigo) ";

	
	/**
	 * Consulta el valor inicial de la cuenta de cobro dada
	 */
	private static final String obtenerValorInicialCuentaCobroCapitacionStr="SELECT SUM(cc.valor_total) AS valor_inicial " +
																																	  "		FROM capitacion.contrato_cargue cc " +
																																	  "			INNER JOIN capitacion.cuentas_cobro_capitacion ccc ON (cc.cuenta_cobro=ccc.numero_cuenta_cobro) " +
																																	  "				WHERE cc.cuenta_cobro=? AND ccc.institucion=? " +
																																	  "					GROUP BY cc.cuenta_cobro,ccc.institucion";
	
	/**
	 * Sentencia para obtener el nï¿½mero de la ï¿½ltima cuenta de cobro de capitaciï¿½n
	 */
	private static final String obtenerUltimaCuentaCobroStr="SELECT max(numero_cuenta_cobro) AS ultima_cuenta_cobro " +
																											"		FROM capitacion.cuentas_cobro_capitacion " +
																											"			WHERE institucion=?";
	
	
	/**
	 * Cadena que consulta el diagnostico de la ultima evolucion del paciente
	 * (solo aplica para pacientes de hospitalizacion y urgencias)
	 */
	private static final String consultarDiagnosticoEvolucionStr = "SELECT "+ 
		"ed.acronimo_diagnostico AS acronimo, "+
		"ed.tipo_cie_diagnostico AS tipo_cie "+  
		"FROM solicitudes s "+ 
		"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+
		"INNER JOIN evoluciones e ON (e.valoracion=v.numero_solicitud) "+
		"INNER JOIN evol_diagnosticos ed ON(ed.evolucion=e.codigo and " +
			"ed.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
			"ed.definitivo="+ValoresPorDefecto.getValorTrueParaConsultas()+") " +
		"WHERE "+ 
		"s.cuenta = ? order by e.fecha_evolucion DESC,e.hora_evolucion DESC";
	
	/**
	 * Cadena que consulta el diagnostico egreso de las valoraciones del paciente
	 * (aplica para hopsitalizacion, urgencias y consulta externa)
	 */
	private static final String consultarDiagnosticosValoracionStr = "SELECT "+ 
		"vd.acronimo_diagnostico AS acronimo, "+
		"vd.tipo_cie_diagnostico AS tipo_cie "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+
		"INNER JOIN val_diagnosticos vd ON(vd.valoracion=v.numero_solicitud and " +
			"vd.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
			"vd.definitivo="+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
		"WHERE "+ 
		"s.cuenta = ? order by v.fecha_valoracion DESC,v.hora_valoracion DESC";
	
	/**
	 * Cadena que consulta la fecha de admision de una cirugia
	 */
	private static final String consultarFechaAdmisionCirugiaStr = "SELECT "+ 
		"to_char(getFechaAdmisionCx(numero_solicitud),'DD/MM/YYYY') AS fecha_admision "+ 
		"FROM sol_cirugia_por_servicio WHERE codigo = ?";
	
	
	
	/**
	 * Cadena que consulta los tipos de identificacion
	 */
	private static final String consultarTiposIdentificacionStr = "SELECT "+ 
	" ti.acronimo , ti.nombre , tii.es_consecutivo " +
	"FROM Tipos_Identificacion ti INNER JOIN tipos_id_institucion tii ON (ti.acronimo=tii.acronimo) " +
	"WHERE institucion = ? ";
			
	
	/**
	 * Cadena que consulta las Ciudades 
	 */
	private static final String consultarCiudadesStr = "SELECT c.codigo_ciudad as codigociudad,c.nombre as nombreciudad,d.codigo as codigodepartamento,d.nombre as nombredepartamento FROM administracion.ciudades c inner join administracion.departamentos d on (d.codigo=c.codigo_departamento) ORDER BY c.nombre ASC";

		
	/**
	 * Cadena que consulta los tipos de sangre
	 */
	private static final String consultarTiposSangreStr = "SELECT codigo AS codigo, nombre as nombre FROM manejopaciente.tipos_sangre  ORDER BY nombre ASC";

	/**
	 * Cadena que consulta los estados_civiles
	 */
	private static final String consultarEstadosCivilesStr = "SELECT acronimo AS acronimo, nombre as nombre FROM administracion.estados_civiles  ORDER BY acronimo ASC";


	/**
	 * Cadena que consulta los Zonas_Domicilio
	 */
	private static final String consultarZonasDomicilioStr = "SELECT acronimo AS acronimo, nombre as nombre FROM administracion.zonas_domicilio  ORDER BY acronimo DESC";

	/**
	 * Cadena que consulta los Ocupaciones
	 */
	private static final String consultarOcupacionesStr = "SELECT codigo AS codigo, nombre as nombre FROM manejopaciente.ocupaciones  ORDER BY nombre ASC";

	/**
	 * Cadena que actualiza como atendidos los registros de pacientes para triage que ya estï¿½n vencidos
	 */
	private static final String actualizarPacienteParaTriageVencidoStr = "UPDATE pacientes_triage SET atendido = "+ValoresPorDefecto.getValorTrueParaConsultas()+" WHERE codigo_paciente = ? and atendido = "+ValoresPorDefecto.getValorFalseParaConsultas();
	
	/**
	 * Cadena que consulta personas con el mismo numero id
	 */
	private static final String personasConMismoNumeroIdStr = "SELECT " +
		"tipo_identificacion AS tipo_id," +
		"numero_identificacion AS numero_id," +
		"getnombrepersona(codigo) AS nombre " +
		"FROM personas WHERE numero_identificacion = ?";

	/**
	 * Cadena que consulta el codigo UPGD del centro de atencion
	 */
	private static final String getCodigoUPGDCentroAtencionStr = "SELECT " +
		"CASE WHEN codupgd IS NULL THEN 0 ELSE codupgd END AS codigo_upgd " +
		"FROM centro_atencion WHERE consecutivo = ?";
	
	/**
	 * Cadena para obtener el nombre del esquema tarifario enviandole elcodigo
	 */
	private static final String getNombreEsquemaTarifarioStr="SELECT " +
		"nombre FROM esquemas_tarifarios WHERE codigo=?";
	
	/**
	 * Cadena para la consulta del tarifario oficial del esquema tarifario segun el codigo
	 */
	private static final String getTarifarioOficialStr="SELECT " +
	"tarifario_oficial FROM esquemas_tarifarios WHERE codigo=?";
	
	private static String [] indicesUltimoEgreso={"cuenta_","evolucion_","fecha_","hora_","acronimo_","cie_","nombre_"};
	
	private static String [] indicesCentrosCFiltros={"codigo_","nombre_","centroAtencion_","descripcion_","viaIngreso_","tipoPaciente_","selec_","idcc_"};	
	
	private static String getUltimoIngresoAnterior="SELECT id FROM ingresos WHERE codigo_paciente=? AND estado = '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' ";
	
	private static String marcarReingreso="UPDATE ingresos SET reingreso=? WHERE id=?";
	
	/**
	 * Consulta la informacion de la tabla Unidades Campo Param
	 * */
	private static String strConsultarUnidadesCampoParam = "SELECT codigo, nombre,acronimo FROM unidades_campo_param WHERE activo = '"+ConstantesBD.acronimoSi+"' ORDER BY acronimo ";
	
	
	/**
	 * Consulta la informacion de la tabla Unidades Campo Param
	 * */
	private static String strConsultarEscalasParam = " SELECT codigo_pk, codigo, nombre FROM escalas ";
	
	
	/**
	 * Consulta la informacion de la tabla Unidades Campo Param
	 * */
	private static String strConsultarComponentesParam = " SELECT c.codigo as codigo, tc.nombre as nombre, c.tipo_componente as tipocomponente FROM componentes c inner join tipos_componente tc on(c.tipo_componente=tc.codigo) inner join fun_param_tip_comp f on(f.tipo_componente=tc.codigo) where tc.activo='"+ConstantesBD.acronimoSi+"' AND f.fun_param=? ";
	 
	
	/**
	 * Consulta la informacion de la tabla Tipos Campo
	 * */
	private static String strConsultarTiposCampo = " SELECT codigo,nombre FROM tipos_campo_param ORDER BY nombre ";
	
	/**
	 * Consulta de los Centros de Costo por via de Ingreso, por TIpo de Area
	 */
	private static String strConsultaCentrosCostoFiltros="SELECT cc.codigo, " +
																	"cc.identificador AS idcc, " +
																	"cc.nombre, " +
																	"ca.codigo AS centro_atencion, " +
																	"ca.descripcion, " +
																	"ccvi.via_ingreso, " +
																	"ccvi.tipo_paciente, " +
																	"'"+ConstantesBD.acronimoNo+"' AS selec " +
															"FROM centro_costo_via_ingreso ccvi " +
																	"INNER JOIN centros_costo cc ON (ccvi.centro_costo=cc.codigo) " +
																	"INNER JOIN centro_atencion ca ON (cc.centro_atencion=ca.consecutivo) " +
															"WHERE cc.tipo_area="+ConstantesBD.codigoTipoAreaDirecto+" " +
																	"AND ccvi.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" AND ccvi.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' ";
	
	/**
	 * Consulta la existencia de un Tipo de Monitoreo por Centro de Costo
	 */
	private static String strConsultaTipoMonitoreoxCC="SELECT tm.codigo, " +
																"tm.nombre, " +
																"ctm.centro_costo AS cc, " +
																"tm.prioridad_cobro AS prioridad " +
														"FROM tipo_monitoreo tm " +
																"INNER JOIN centros_costo_x_tipo_m ctm ON (tm.codigo=ctm.tipo_monitoreo) " +
														"WHERE ctm.centro_costo=? " +
														"ORDER BY tm.nombre ";
	
	
	/**
	 * Actualiza registro Ingreso de Cuidados Especiales a estado Finalizado
	 */
	private static String strActualizarEstadoCuidadosEspeciales="UPDATE ingresos_cuidados_especiales SET estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"', usuario_finaliza = ?, fecha_finaliza = ?, hora_finaliza = ? WHERE ingreso=? ";
	
	/**
	 * Consulta la existencia de un ingreso en Ingresos Cuidados Especiales 
	 */
	private static String strExistenciaIngresoCuidadosEspeciales="SELECT codigo, " +
																		"ingreso, " +
																		"estado, " +
																		"indicativo, " +
																		"tipo_monitoreo AS tipom, " +
																		"coalesce(valoracion,"+ConstantesBD.codigoNuncaValido+") as valoracion " +
																"FROM ingresos_cuidados_especiales " +
																"WHERE ingreso=? AND estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
	/**	
	 * Consultar Tipo de Monitoreo de ingreso en Cuidados Especiales
	 */
	private static String strConsultaTipoMonitoreoxIngresoCE="SELECT tm.nombre FROM ingresos_cuidados_especiales ice " +
																	"INNER JOIN tipo_monitoreo tm ON (ice.tipo_monitoreo=tm.codigo) " +
															"WHERE ice.ingreso=? AND ice.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
	
	
	/**
	 * consulta el nombre del piso por el codigo
	 */
	private static String strConsultaNombrePiso =" SELECT p.nombre FROM pisos p  WHERE p.codigo=? ";
	
	private static String strConsultaNombreEstadoCama =" SELECT ec.nombre FROM estados_cama ec  WHERE ec.codigo=? "; 
	
	/**
	 * Verificar Requiere Valoracion en Tipos Monitoreo
	 */
	private static String strRequiereValoracionTipoM="SELECT codigo, requiere_valoracion AS requierev FROM tipo_monitoreo WHERE codigo=? ";
	
	/**
	 * Consulta los totales de una factura para su Impresion
	 */
//	private static String strTotalesFactura="SELECT " +
//			        				"f.valor_total AS totalaf, " +
//			        				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEps+" " +
//			        						"OR " +
//			        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArs+" " +
//			        						"OR " +
//			        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPos+" " +
//			        						"OR " +
//			        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioVinculado+" " +
//			        						"OR " +
//			        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPrepagada+" " +
//			        				"THEN UPPER(tp.nombre) || ' LIQUIDADO' ELSE " +
//			        					"(CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArp+" " +
//			        							"OR " +
//			        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioAseguradora+" " +
//			        							"OR " +
//			        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEmpresarial+" " +
//			        							"OR " +
//			        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEventoCatastrofico+" " +
//			        							"OR " +
//			        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioOtra+" " +
//			        							"OR " +
//			        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioSoat+" " +
//			        					"THEN (CASE WHEN f.valor_neto_paciente=0 THEN '' ELSE UPPER(tp.nombre) || ' LIQUIDADO' END) " +
//			        					"ELSE '' END) " +
//			        				"END AS etiquetavpl, " +
//			        				"f.valor_liquidado_paciente AS valorpl, " +
//			        				"CASE WHEN f.val_desc_pac=0 THEN 0 ELSE f.val_desc_pac END AS valordp, " +
//			        				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEps+" " +
//											"OR " +
//											"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArs+" " +
//											"OR " +
//											"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPos+" " +
//											"OR " +
//											"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioVinculado+" " +
//											"OR " +
//											"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPrepagada+" " +
//									"THEN UPPER(tp.nombre) || ' RECAUDADO' ELSE " +
//										"(CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArp+" " +
//												"OR " +
//												"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioAseguradora+" " +
//												"OR " +
//												"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEmpresarial+" " +
//												"OR " +
//												"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEventoCatastrofico+" " +
//												"OR " +
//												"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioOtra+" " +
//												"OR " +
//												"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioSoat+" " +
//										"THEN (CASE WHEN f.valor_neto_paciente=0 THEN '' ELSE UPPER(tp.nombre) || ' RECAUDADO' END) " +
//										"ELSE (CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioParticular+" " +
//												"THEN 'ABONO' ELSE '' END) END) " +
//									"END AS etiquetavap, " +
//									"f.valor_abonos AS valornp, " +
//									"f.valor_neto_paciente AS valornp1, " +
//									"getdescuentoscargos(f.codigo) AS descuentos, " +
//									"coalesce((f.valor_bruto_pac-f.val_desc_pac),0) AS valori " +
//			    			"FROM facturas f " +
//			    				"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
//			    				"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
//			    				"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) " +
//			    				"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) " +
//			    			"WHERE f.codigo=? ";
	

	private static String strTotalesFactura="SELECT "+
											"f.valor_total vtotal,"+
											"f.valor_abonos vabonos,"+
											"f.valor_bruto_pac vbruto,"+
											"f.valor_neto_paciente vneto "+
											"FROM facturas f "+
											"WHERE f.codigo=?";
	
	/**
	 * 
	 */
	private static final String strEvolucionXCuenta = "SELECT " +
													   " COUNT (*) " +
													" FROM admisiones_urgencias au " +
													" INNER JOIN egresos e ON (e.cuenta=au.cuenta)" +
													" WHERE au.codigo=? AND anio=? AND usuario_responsable is null AND destino_salida is not null";
	
	
	
	/**
	 * Encargado d consultar si un ingreso es reingreso
	 */
	private static final String strEsReingreso=" SELECT " +
														" coalesce(i.reingreso,'-1') As reingreso " +
														" FROM ingresos i " +
														" WHERE i.id=? ";
	
	/**
	 * Carga Areas de Ingreso Paciente segun Centro Atencion, Via Ingreso, Tipo Paciente y Tipo Monitoreo (Egreso por Valoracion)
	 */
	private static String strConsultaAreasAsocioCuidadosEspecialesV="SELECT DISTINCT cc.codigo || '-' || tp.codigo AS codigo, " +
																			"cc.nombre || '-' || tp.codigo || '-' || tp.nombre AS nombre " +
																	"FROM centros_costo cc " +
																			"INNER JOIN centro_costo_via_ingreso ccva ON (ccva.centro_costo=cc.codigo) " +
																			"INNER JOIN centros_costo_x_tipo_m ctpm ON (ctpm.centro_costo=cc.codigo) " +
																			"INNER JOIN tipo_monitoreo tp ON (tp.codigo=ctpm.tipo_monitoreo) " +
																	"WHERE cc.centro_atencion=? " +
																			"AND ccva.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" " +
																			"AND ccva.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' " +
																			"AND tp.codigo=gettipomonitoreovaloracion(?) " +
																	"ORDER BY nombre ";
	
	/**
	 * Carga Areas de Ingreso Paciente segun Centro Atencion, Via Ingreso, Tipo Paciente y Tipo Monitoreo (Egreso por Evolucion)
	 */
	private static String strConsultaAreasAsocioCuidadosEspecialesE="SELECT DISTINCT cc.codigo || '-' || tp.codigo AS codigo, " +
																			"cc.nombre || '-' || tp.codigo || '-' || tp.nombre AS nombre " +
																	"FROM centros_costo cc " +
																			"INNER JOIN centro_costo_via_ingreso ccva ON (ccva.centro_costo=cc.codigo) " +
																			"INNER JOIN centros_costo_x_tipo_m ctpm ON (ctpm.centro_costo=cc.codigo) " +
																			"INNER JOIN tipo_monitoreo tp ON (tp.codigo=ctpm.tipo_monitoreo) " +
																	"WHERE cc.centro_atencion=? " +
																			"AND ccva.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" " +
																			"AND ccva.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' " +
																			"AND tp.codigo=gettipomonitoreoevolucion(?) " +
																	"ORDER BY nombre ";
	
	/**
	 * Consulta la Ultima Valoracion de una Cuenta de Urgencias
	 */
	private static String strConsultaValoracionUrgencias="SELECT numero_solicitud AS numsol FROM solicitudes WHERE cuenta=? and tipo="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+"";
	
	/**
	 * Consulta la Ultima Evolucion de una Cuenta de Urgencias
	 */
	private static String strConsultaEvolucionUrgencias="SELECT evolucion FROM egresos WHERE cuenta=? ";
	
	/**
	 * Consulta si un Ingreso es un Preingreso sin Importar su Estado
	 */
	private static String consultaPreingresoNormal="SELECT preingreso FROM ingresos where id=? AND preingreso='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
				
	private static String strConsultaTipoTratamientoOdonlogcio =" SELECT" +
																		" ttoi.codigo As codigo," +
																		" ttoi.nombre As nombre " +
																	" FROM  tipo_tratamiento_odo_inst ttoi " +
																	" WHERE ttoi.cod_institucion=? " ;
											
	private static String strConsultaTipoHabitacion = " SELECT " +
															   " th.codigo As codigo," +
															   " th.nombre As nombre " +
														" FROM tipo_habitacion th " +
														" WHERE th.institucion=? ";
	
	
	private static String strConsultaDatosCentroAtencion=" SELECT " +
																  " codigo As codigo," +
																  " descripcion As descripcion, " +
																  " codigo_inst_sirc As codigo_inst_sirc," +
																  " empresa_institucion As empresa_institucion," +
																  " direccion As direccion " +
															" FROM centro_atencion ";
	
	/**
	 * consulta centros de atención por usuario
	 * */
	private static String strConsultaCentroAtencionxUsuarioEstadoActivo = 
			"SELECT consecutivo, descripcion " + 
			"FROM centro_atencion " +
			"WHERE consecutivo IN " +
			"  (SELECT c.centro_atencion " +
			"  FROM centros_costo_usuario ccu " + 
			"  INNER JOIN centros_costo c " +
			"  ON(ccu.centro_costo=c.codigo) " +
			"  WHERE ccu.usuario  = ? " +
			"  ) " +
			"AND activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
			" ORDER BY descripcion";  
			
	/**
	 * consulta centros de atención por usuario
	 * */
	private static String strConsultaCentroCostoxUsuario = 
			"SELECT c.codigo, " +
			"  c.nombre, " +
			"  ca.descripcion, " +
			"  ca.consecutivo " +
			"FROM centros_costo_usuario ccu " +
			"INNER JOIN centros_costo c " +
			"ON(ccu.centro_costo=c.codigo) " +
			"INNER JOIN centro_atencion ca " +
			"ON(ca.consecutivo =c.centro_atencion) " +
			"WHERE ccu.usuario = ? " +
			"ORDER BY c.nombre";		
	
	/**
	 * consulta si un centro de costos esta parametrizado como Registro Respuesta Proced X Terceros
	 * */
	private static String strConsultaEsCentroCostoRespuestaProcTercero = 
			"SELECT " +
			"coalesce(reg_res_porc_ter,'"+ConstantesBD.acronimoNo+"') AS campo1 " +
			"FROM centros_costo WHERE " +
			"codigo = ? "; 
	
	/**
	 * Consulta si un centro de costos esta parametrizado como un Registro Respuesta Proce X Terceros
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static String esCentroCostoRespuestaProcTercero(Connection con, HashMap parametros)
	{	
		String resultado=ConstantesBD.acronimoNo;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs  = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaEsCentroCostoRespuestaProcTercero));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				resultado=rs.getString(1);
			}
		}
		catch (Exception e) {
			logger.info("error consultando respuesta proc x terceros "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return resultado;
	}
													
	/**
	 * Mï¿½todo encargado de consultar los datos del Centro
	 * de Atencion, pudiendo se filtrar por diferentes criterios.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- consecutivo --> Requerido
	 * -- institucion --> Opcional
	 * -- codigo --> Opcional
	 * -- activo --> Opcional
	 * @return Mapa
	 * ---------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------
	 * codigo,descripcion,codigo_inst_sirc,
	 * empresa_institucion,direccion
	 */
	public static HashMap obtenerDatosCentroAtencion (Connection connection,HashMap criterios)
	{
		logger.info("\n entre obtenerDatosCentroAtencion criterios --> "+criterios);
		HashMap result = new HashMap ();
		String consulta=strConsultaDatosCentroAtencion;
		
		
		consulta+=" WHERE consecutivo="+criterios.get("consecutivo"); 
		
		if (UtilidadCadena.noEsVacio(criterios.get("institucion")+""))
			consulta+=" AND cod_institucion="+criterios.get("institucion"); 
		
		if (UtilidadCadena.noEsVacio(criterios.get("codigo")+""))
			consulta+=" AND codigo='"+criterios.get("codigo")+"'"; 
		
		if (UtilidadCadena.noEsVacio(criterios.get("activo")+""))
			consulta+=" AND activo='"+criterios.get("activo")+"'"; 
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, false);
			ps.close();
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema en obtenerDatosCentroAtencion "+e);
		}
		
		
		return result;
	}
	
	
	
	/**
	 * Mï¿½todo encargado de obtener los tipos de servicio
	 * de vehiculos
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param codigo --> Opcional
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposServiciosVehiculos (Connection connection,String codigo)
	{
		ArrayList<HashMap<String, Object>> resultado= new ArrayList<HashMap<String,Object>>();
		String cadena=" SELECT codigo,descripcion from tipo_serv_vehiculos ";
		String order=" ORDER BY descripcion ";
		if (UtilidadCadena.noEsVacio(codigo) && Utilidades.convertirAEntero(codigo)>0)
			cadena+=" WHERE codigo="+codigo;
		
		cadena+=order;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				HashMap dato = new HashMap ();
				
				dato.put("codigo", rs.getObject("codigo"));
				dato.put("descripcion", rs.getObject("descripcion"));
				resultado.add(dato);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.info("\n problema obteniendo los tipos de servicios de vehiculos "+e);
		}
		
		return resultado;
	}
	/**
	 * Mï¿½todo encargado de obtener los tipos de Habitacion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposHabitacion (Connection connection,String institucion)
	{
		ArrayList<HashMap<String, Object>> resultado= new ArrayList<HashMap<String,Object>>();
		String cadena=strConsultaTipoHabitacion;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setObject(1, institucion);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				HashMap dato = new HashMap ();
				
				dato.put("codigo", rs.getObject("codigo"));
				dato.put("nombre", rs.getObject("nombre"));
				resultado.add(dato);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.info("\n problema obteniendo los tipos de habitacion "+e);
		}
		
		return resultado;
	}
	
	/**
	 * Mï¿½todo encargado de consultar los tipos de tratamiento
	 * odontologico
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- activo --> Opcional (S --> filtra los activos, N --> filtra inactivos, "" --> no filtra)
	 * -- codigo --> Opcional
	 * -- institucion --> Requerido
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposTratamientoOdontologico (Connection connection,HashMap criterios)
	{
		logger.info("\n entre a obtenerTiposTratamientoOdontologico  criterios --> "+criterios);
		
		ArrayList<HashMap<String, Object>> resultado= new ArrayList<HashMap<String,Object>>();
		String  cadena=strConsultaTipoTratamientoOdonlogcio;
		String where="";
		
		//se valida el activo
		if (UtilidadCadena.noEsVacio(criterios.get("activo")+""))
			if (UtilidadTexto.getBoolean(criterios.get("activo")+""))
				where+=" AND ttoi.activo="+ValoresPorDefecto.getValorTrueCortoParaConsultas();
			else
				where+=" AND ttoi.activo="+ValoresPorDefecto.getValorFalseCortoParaConsultas();
				
		//se valida el codigo
		if (UtilidadCadena.noEsVacio(criterios.get("codigo")+""))
			where+=" AND ttoi.codigo="+criterios.get("codigo");
			
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setObject(1, criterios.get("institucion"));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				HashMap dato = new HashMap ();
				
				dato.put("codigo", rs.getObject("codigo"));
				dato.put("nombre", rs.getObject("nombre"));
				
				resultado.add(dato);
			}
			rs.close();
			ps.close();			
		} 
		catch (Exception e) 
		{
			logger.info("\n problema consultando los tipos de tratamiento odontologico "+e);
		} 
		return resultado;
	}
	
	
	
	
	
	/**
	 * Mï¿½todo encargado de Actualizar la fecha y hora de finalizacion del 
	 * traslado de la cama
	 * @param connection
	 * @param codigoTrasladoCama
	 * @return
	 */
	public static boolean actualizarFechaHoraActualizacion (Connection connection, HashMap datos)
	{
		logger.info("\n etro a actualizarFechaHoraActualizacion "+datos);
		String cadena = actualizarFechaHoraFinalizacion;
		boolean resultado=false;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//fecha finalizacion
			if (UtilidadCadena.noEsVacio(datos.get("fechaFinaliza")+""))
				ps.setObject(1, UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaFinaliza")+""));
			else
				ps.setNull(1, Types.DATE);
					
			//hora finalizacion
			if (UtilidadCadena.noEsVacio(datos.get("horaFinaliza")+""))
				ps.setObject(2, datos.get("horaFinaliza"));
			else
				ps.setNull(2, Types.VARCHAR);
			// codigo traslado
			ps.setObject(3, datos.get("codigoTraslado"));
			
			if (ps.executeUpdate()>0)
				resultado=true;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando la fecha y hora de finalizacion de la tabla traslado_cama "+e);
		}
		
		return resultado;
	}
	
	
	/**
	 * Mï¿½todo encargado de identificar si un ingreso es reingreso;
	 * si es reingreso devuelve el ingreso asociado, si no devuelve -1
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static int esIngresoReingreso (Connection connection, String ingreso)
	{
		logger.info("\n etro a esIngresoReingreso ingreso -->"+ingreso);
		String cadena = strEsReingreso;
		int resultado=ConstantesBD.codigoNuncaValido;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			// ingreso
			ps.setObject(1, ingreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				resultado=rs.getInt(1);
			rs.close();
			ps.close();
			
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando si un ingreso es reingreso "+e);
			
		}
		
		
		return resultado;
	}
	
	/**
	 * Mï¿½todo encargado de verificar si existe evolucion con 
	 * orden de salida por Admision de Urgencias
	 * @param con
	 * @param codigoAdminUrg
	 * @param anioAdminUrg
	 * @return
	 */
	public static boolean  tieneEvolucionConSalidaXAdminUrg  (Connection con, String codigoAdminUrg, String anioAdminUrg)
	{
		logger.info("\n entro a tieneEvolucionConSalidaXAdminUrg codigoAdminUrg-->"+codigoAdminUrg+" anioAdminUrg-->"+anioAdminUrg);
		String cadena=strEvolucionXCuenta;
		boolean valorRetorno=false;
		logger.info("\n cadena --> "+cadena);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//codigo admision de urgensias
			ps.setObject(1, codigoAdminUrg);
			//anio admision de urgencias
			ps.setObject(2, anioAdminUrg);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				if(resultado.getInt(1)>0)
					valorRetorno=true;
			}	
			resultado.close();
			ps.close();
				
		}
		catch (SQLException e)
		{
			logger.error("Error si una admision de urgencias tiene evolucion con salida "+e);
		}
		return valorRetorno;
	}
	
	
	
	/**
	 * Mï¿½todo encargado de consultar la cantidad de camas
	 * de una institucion
	 * @param con
	 * @param centroAtencion 
	 * @return
	 */
	public static int obtenerNumeroCamas(Connection con,String institucion, String centroAtencion)
	{
		int resultadoRetorno=ConstantesBD.codigoNuncaValido;
		
		String strNumeroCamas = "SELECT count (*) " +
								"FROM camas1 c ";
		
		if(!centroAtencion.equals(""))
			strNumeroCamas += 	"INNER JOIN centros_costo cc ON (cc.codigo = c.centro_costo) ";
								
		strNumeroCamas +=		"WHERE c.institucion="+institucion+" ";
		
		if(!centroAtencion.equals(""))
			strNumeroCamas += 	"AND cc.centro_atencion="+centroAtencion;
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strNumeroCamas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				resultadoRetorno=resultado.getInt(1);
			}
			resultado.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la cantidad de camas "+e);
		}
		return resultadoRetorno;
	}
	
	
	/**
	 * Mï¿½todo encargado de consultar el nombre del piso
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public static String obtenerNombrePiso(Connection con, String codigoPiso)
	{
		String cadena=strConsultaNombrePiso;
		String resultadoRetorno="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//codigo piso
			ps.setObject(1, codigoPiso);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				resultadoRetorno=resultado.getString(1);
			}
			resultado.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nombre del piso "+e);
		}
		return resultadoRetorno;
	}
	
	/**
	 * Mï¿½todo encargado de consultar el nombre del estado de la cama
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public static String obtenerNombreEstadoCama (Connection con, String codigoEstadoCama)
	{
		String cadena=strConsultaNombreEstadoCama;
		String resultadoRetorno="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//codigo estado cama
			ps.setObject(1, codigoEstadoCama);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				resultadoRetorno=resultado.getString(1);
			}
			resultado.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nombre del estado de la cama "+e);
		}
		return resultadoRetorno;
	}
	
	/**
	 * Mï¿½todo para verificar si una solicitud de medicamentos tiene despachos
	 * @param con Conexiï¿½n con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene despachos, false de lo contrario
	 */
	public static boolean hayDespachosEnSolicitud(Connection con, int numeroSolicitud)
	{
		boolean valorRetorno=false;
		try
		{
			PreparedStatementDecorator hayDesp= new PreparedStatementDecorator(con.prepareStatement(hayDespachosEnSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			hayDesp.setInt(1, numeroSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(hayDesp.executeQuery());
			if(resultado.next())
			{
				if(resultado.getInt("numResultados")>0)
				{
					valorRetorno= true;
				}
				else
				{
					valorRetorno= false;
				}
			}
			else
			{
				valorRetorno= false;
			}
			resultado.close();
			hayDesp.close();			
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si hay despachos en la solicitud ("+numeroSolicitud+") "+e);
			valorRetorno=false;
		}
		return valorRetorno;
	}

	/**
	 * Mï¿½todo que carga la fecha de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @param consultaFecha String con la sentencia para consultar la fecha de acuerdo a la BD
	 * @return String con la fecha del sistema formato BD
	 */
	public static String capturarFechaBD(Connection con, String consultaFecha)
	{
		String resultadoR="";
		try
		{
			PreparedStatementDecorator fechaSt= new PreparedStatementDecorator(con.prepareStatement(consultaFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(fechaSt.executeQuery());
			if(resultado.next())
			{
				resultadoR=resultado.getString("fecha");
			}
			resultado.close();
			fechaSt.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la fecha de la BD "+e);
		}
		return resultadoR;
	}
	
	/**
	 * Mï¿½todo que carga la hora de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @param consultaHora String con la sentencia para consultar la hora de acuerdo a la BD
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public static String capturarHoraBD(Connection con, String consultaHora)
	{
		String hora=null;
		try
		{
			//logger.info("consultaHora->"+consultaHora);
			PreparedStatementDecorator horaSt= new PreparedStatementDecorator(con.prepareStatement(consultaHora,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(horaSt.executeQuery());
			if(resultado.next())
			{
				hora= resultado.getString("hora");
			}
			horaSt.close();
			resultado.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la hora de la BD "+e);
		}
		return hora;
	}
	/**
	 * Mï¿½todo para cargar la Fecha-Hora del egreso de la cuenta Urgencias cuando
	 * se hace un Asocio de Cuenta
	 * @param con
	 * @param codigo_paciente
	 * @return
	 */
	public static String capturarFechayHoraEgresoUrgenciasEnAsocio(Connection con,String idIngreso,String viaIngreso){
			ResultSetDecorator rs;
			String fecha_hora="";
			try{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(capturarFechayHoraEgresoUrgenciasEnAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(idIngreso));
				
				if(viaIngreso.equals(""))
					pst.setObject(2,ConstantesBD.codigoViaIngresoUrgencias);
				else
					pst.setObject(2,viaIngreso);
				
				
				logger.info("valior sql >> "+capturarFechayHoraEgresoUrgenciasEnAsocioStr.replace("?",idIngreso)+" via de ingreso >> "+viaIngreso);
				
				rs=new ResultSetDecorator(pst.executeQuery());
				if(rs.next()){
					fecha_hora=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha"));
					fecha_hora+="-"+rs.getString("hora");
					return fecha_hora;
				}
				else{
					return null;
				}
			}
			catch(SQLException e){
				logger.error("Error al cargar fecha y hora de Egreso de Urgencias en Asocio SqlBaseUtilidadesDao: "+e);
				return null;
			}
		}
		
		
	/**
	 * Mï¿½todo para obtener el codigo del contrato el cual
	 * fue utilizado en su ï¿½ltima cuenta abierta
	 * @param con Conexiï¿½n con la BD
	 * @param codigoPaciente cï¿½digo del paciente
	 * @return Codigo del ultimo contrato del paciente
	 */
	public static int obtenerUltimoContrtatoPaciente(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(obtenerUltimoContrtatoPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoPaciente);
			statement.setInt(2, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigoContrato");
			}
			else
			{
				return 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error buscando el cï¿½digo del contrato "+e);
			return 0;
		}
	}


	/**
	 * @param con
	 * @param cadena
	 * @return
	 */
	public static int codigoCieActual(Connection con, String cadena) 
	{
		ResultSetDecorator rs;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigo");
		}
		catch(SQLException e){
			logger.error("Error en la consulta: "+e);
		}
		return -1;
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el destino de salida de un egreso de evoluciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param obtenerDestinoSalidaStr
	 * @return
	 */
	public static int obtenerDestinoSalidaEgresoEvolucion(Connection con,int idCuenta){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerDestinoSalidaEgresoEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				int destino=rs.getInt("destino");
				if(rs.getString("otro_destino").equals(ConstantesBD.codigoNuncaValido+"") && rs.getInt("destino")==0)
				{
					return -1;
				}
				rs.close();
				pst.close();
				return destino;
			}
			else{
				return -1;
			}
		}
		catch(SQLException e){
			logger.error("Error obteniendo el destino de salida de un egreso de evoluciï¿½n en SqlBaseUtilidadesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Mï¿½todo que carga el numero de solicitud
	 * de la ultima valoraciï¿½n de interconsulta
	 * @param con
	 * @param codigoPersona
	 * @return numeroSolicitud ï¿½ltima interconsulta
	 */
	public static int obtenerUltimaValoracionInterconsulta(Connection con, int codigoPersona)
	{
		String obtenerUltimaValoracionInterconsultaStr="SELECT max(s.numero_solicitud) AS numeroSolicitud from solicitudes s inner join cuentas c ON (c.id=s.cuenta) where c.codigo_paciente="+codigoPersona+" and s.tipo="+ConstantesBD.codigoTipoSolicitudInterconsulta+" AND c.estado_cuenta IN("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+")";
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(obtenerUltimaValoracionInterconsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n**************************obtenerUltimaValoracionInterconsultaStr-->"+obtenerUltimaValoracionInterconsultaStr);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numeroSolicitud");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nuï¿½mero de solicitud de la ï¿½ltima interconsulta: "+e);
		}
		return 0;
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para saber el nï¿½mero de solicitudes de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return nï¿½mero de solicitudes
	 */
	public static int obtenerNumeroSolicitudesCuenta(Connection con,int idCuenta){
			try{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerNumeroSolicitudesCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,idCuenta);
				ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
				if(rs.next()){
					return rs.getInt("numero_solicitudes");
				}
				else{	
						return -1;
					}
			}
			catch(SQLException e){
				logger.error("Error en obtenerNumeroSolicitudesCuenta de SqlBaseUtilidadesDao :"+e);
				return -1;
			}
		}

	/**
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static int obtenerCodigoFactura(Connection con, int consecutivoFactura)
	{
		String sentencia="SELECT codigo as codigo from facturas where consecutivo_factura = ?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigo");
			}
			else{	
					return ConstantesBD.codigoNuncaValido;
				}
		}
		catch(SQLException e){
			logger.error("Error el codigo de la factura. de SqlBaseUtilidadesDao :"+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}


	/**
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static int obtenerCodigoFactura(Connection con, int consecutivoFactura,int institucion)
	{
		String sentencia="SELECT codigo as codigo from facturas where consecutivo_factura = ? and institucion=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoFactura);
			pst.setInt(2,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigo");
			}
			else{	
					return ConstantesBD.codigoNuncaValido;
				}
		}
		catch(SQLException e){
			logger.error("Error el codigo de la factura. de SqlBaseUtilidadesDao :"+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerFechaFacturacion(Connection con, int consecutivoFactura) 
	{
		String sentencia="SELECT fecha as fecha from facturas where codigo = ?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString("fecha");
			}
			else{	
					return "";
				}
		}
		catch(SQLException e){
			logger.error("Error fecha de la factura. de SqlBaseUtilidadesDao :"+e);
			return "";
		}
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerCuentaCobroFactura(Connection con, int codigoFactura) 
	{
		String sentencia=" SELECT case when numero_cuenta_cobro is NULL then "+ConstantesBD.codigoNuncaValido+" else numero_cuenta_cobro end  as numero_cuenta_cobro from facturas where codigo=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getDouble("numero_cuenta_cobro");
			}
			else{	
					return ConstantesBD.codigoNuncaValido;
				}
		}
		catch(SQLException e){
			logger.error("Error el cuenta cobro de la factura. de SqlBaseUtilidadesDao :"+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerEstadoFactura(Connection con, int codigoFactura) 
	{
		String sentencia="SELECT f.estado_facturacion as codigoEstado,ef.nombre as nombreEstado from facturas f inner join estados_factura_f ef on (f.estado_facturacion=ef.codigo) where f.codigo=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString("codigoEstado")+ConstantesBD.separadorSplit+rs.getString("nombreEstado");
			}
			else{	
					return "-1"+ConstantesBD.separadorSplit+"no existe";
				}
		}
		catch(SQLException e){
			logger.error("Error el estado de factura. de SqlBaseUtilidadesDao :"+e);
			return "-1"+ConstantesBD.separadorSplit+"no existe";
		}
	}

	/**
	 * Este Mï¿½todo devuelve los cï¿½digos de las justificaciones
	 * filtrando por instituciï¿½n
	 * @param con
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @param esArticulo Restringir si es justificaciï¿½n de Artï¿½culo o de Servicio
	 * @return Array de enteros con los cï¿½digos de las justificaciones
	 */
	public static int[] buscarCodigosJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esAtriculo)
	{
		String consulta="SELECT codigo AS codigo FROM atributos_solicitud WHERE institucion=?";
		String esRequerido;
		if(esAtriculo)
		{
			consulta+=" AND es_articulo=?";
			esRequerido="es_requerido_art";
		}
		else
		{
			consulta+=" AND es_servicio=?";
			esRequerido="es_requerido_ser";
		}
		if(restringirSoloRequeridas)
		{
			consulta+=" AND "+esRequerido+"=?";
		}
		try
		{
			PreparedStatementDecorator codigosJustificacionStm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			codigosJustificacionStm.setInt(1, codigoInstitucion);
			/*
			 * Primero se filtra si es de servicios o artï¿½culos
			 * como la restricciï¿½n siempre debe ir se pone en el parï¿½metro un true
			 */
			codigosJustificacionStm.setBoolean(2, true);
			/*
			 * Ahora se filtra por campos requeridos
			 */
			if(restringirSoloRequeridas)
			{
				codigosJustificacionStm.setBoolean(3, true);
			}
			ResultSetDecorator resultado=new ResultSetDecorator(codigosJustificacionStm.executeQuery());
			Vector codTempo=new Vector();
			while(resultado.next())
			{
				codTempo.add(new Integer(resultado.getInt("codigo")));
			}
			int codigos[]=new int[codTempo.size()];
			for(int i=0; i<codTempo.size(); i++)
			{
				codigos[i]=((Integer)codTempo.elementAt(i)).intValue();
			}
			return codigos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los cï¿½digos de la justificaciï¿½n: "+e);
			return null;
		}
	}
	
	/**
	 * Este Mï¿½todo devuelve los cï¿½digos y los nombres de las justificaciones
	 * filtrando por instituciï¿½n del usuario
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @param esArticulo Restringir si es justificaciï¿½n de Artï¿½culo o de Servicio
	 * @return Vector con los cï¿½digos y los nombres de los Atributos de la justificaciï¿½n
	 */
	public static Vector buscarCodigosNombresJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esAtriculo)
	{
		String restriccion;
		String esRequerido;
		if(esAtriculo)
		{
			restriccion=" AND es_articulo=?";
			esRequerido="es_requerido_art";
		}
		else
		{
			restriccion=" AND es_servicio=?";
			esRequerido="es_requerido_ser";
		}
		String consulta="SELECT codigo AS codigo, nombre AS nombre, "+esRequerido+" AS requerido FROM atributos_solicitud WHERE institucion=?";
		consulta+=restriccion;
		if(restringirSoloRequeridas)
		{
			consulta+=" AND "+esRequerido+"=?";
		}
		try
		{
			PreparedStatementDecorator nombresJustificacionStm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			nombresJustificacionStm.setInt(1, codigoInstitucion);
			/*
			 * Primero se filtra si es de servicios o artï¿½culos
			 * como la restricciï¿½n siempre debe ir se pone en el parï¿½metro un true
			 */
			nombresJustificacionStm.setBoolean(2, true);
			/*
			 * Ahora se filtra por campos requeridos
			 */
			if(restringirSoloRequeridas)
			{
				nombresJustificacionStm.setBoolean(3, true);
			}
			ResultSetDecorator resultado=new ResultSetDecorator(nombresJustificacionStm.executeQuery());
			Vector nombres=new Vector();
			while(resultado.next())
			{
				Vector atributo=new Vector();
				atributo.add(new Integer(resultado.getInt("codigo")));
				atributo.add(resultado.getString("nombre"));
				if(resultado.getBoolean("requerido"))
				{
					atributo.add("requerido");
				}
				nombres.add(atributo);
			}
			return nombres;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los nombres de los atributos de la justificaciï¿½n: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si el tipo de identificaciï¿½n maneja consecutivo
	 * automï¿½tico
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static boolean esAutomaticoTipoId(Connection con,String acronimo,int codigoInstitucion){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esAutomaticoTipoIdStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,acronimo);
			pst.setInt(2,codigoInstitucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next()){
				String valor=rs.getString("es_consecutivo");
				if(valor.equals("false")||valor.equals("f")||valor.equals("0"))
					return false;
				else
					return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException e){
			logger.error("Error en esAutomaticoTipoId de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el nï¿½mero de la factura (prefijo+consecutivo)
	 * @param con
	 * @param numeroSolicitud
	 * @return numero de factura (prefijo+consecutivo)
	 */
	public static String obtenerNumeroFactura(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerNumeroFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,numeroSolicitud);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			
			if(rs.next())
			{
				return rs.getString("documento");
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNumeroFactura de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo que adiciona el diagnï¿½stico y tipo cie a pagos paciente.
	 * @param con
	 * @param codigoPaciente
	 * @param numeroDocumento
	 * @param diagnostico
	 * @param tipoCie
	 * @return -1 no exitoso, 1 exitoso
	 */
	public static int actualizarDiagnosticoTopesPaciente(Connection con,
			int codigoPaciente,String numeroDocumento,String diagnostico, String tipoCie)
	{
		int resp=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDiagnosticoTopesPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setString(1,diagnostico);
			pst.setInt(2,Integer.parseInt(tipoCie));
			pst.setInt(3,codigoPaciente);
			pst.setString(4,numeroDocumento);
			
			resp=pst.executeUpdate();
			if(resp>0)
				return resp;
			else
				return -1;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarDiagnosticoTopesPaciente de SqlUtilidadesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Mï¿½todo para capturar el cï¿½digo de la ultima via de ingreso del paciente
	 * que no tenga cuenta activa.
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static int obtenerCodigoUltimaViaIngreso(Connection con,int codigoPaciente)
	{
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexiï¿½n (SqlBaseUtilidadesDao)"+e1.toString());
		}
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerCodigoUltimaViaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPaciente);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("via_ingreso");
			}
			else
			{
				return 0;
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoUltimaViaIngreso de SqlBaseUtilidadesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo obtener el ID de la ï¿½ltima cuenta del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param tipoBD 
	 * @return
	 */
	public static int obtenerIdUltimaCuenta(Connection con,int codigoPaciente, int tipoBD)
	{
		try
		{
			String obtenerIdUltimaCuentaStr = "";
			switch(tipoBD){
			case DaoFactory.ORACLE:
				obtenerIdUltimaCuentaStr = OraObtenerIdUltimaCuentaStr;
				break;
			case DaoFactory.POSTGRESQL:
				obtenerIdUltimaCuentaStr = PostgObtenerIdUltimaCuentaStr;
				break;
			default:
				break;
			}
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerIdUltimaCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPaciente);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("id");
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerIdUltimaCuenta de SqlBaseUtilidadesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * funciï¿½n que verifica el estado true o false de la autorizacion en la tabla atributo_solicitud
	 * @param con Conexiï¿½n con la BD
	 * @param codAtributoSolicitud 
	 * @return true si el atributo solicitud tiene true en validar_autorizacion
	 */
	public static boolean verificarAutorizacion (Connection con, int codAtributoSolicitud)
	{
	    boolean respuesta;
	    try
		{
			PreparedStatementDecorator autorizacion= new PreparedStatementDecorator(con.prepareStatement(verificarAutorizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			autorizacion.setInt(1, codAtributoSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(autorizacion.executeQuery());
			if(resultado.next())
			{
			     respuesta=resultado.getBoolean("validar_autorizacion");
				 return respuesta;
			}
			else
			{
			    return false; 
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la autorizacion  ("+codAtributoSolicitud+") "+e);
			return false;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para verificar si un servicio o un artï¿½culo que requiera justificaciï¿½n
	 * ha sido justificado. 
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param codigoInstitucion
	 * @param esArticulo
	 * @return
	 */
	public static boolean esSolicitudJustificada(Connection con,int numeroSolicitud,int parametro,int codigoInstitucion,boolean esArticulo)
	{
		try
		{
			PreparedStatementDecorator pst;
			if(esArticulo)
				pst= new PreparedStatementDecorator(con.prepareStatement(esMedicamentoJustificadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			else
				pst= new PreparedStatementDecorator(con.prepareStatement(esServicioJustificadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,parametro);
			pst.setInt(3,codigoInstitucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
					return true;
				else
					return false;
			}
			else
			{
				return false;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en esMedicamentoJustificado de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el cï¿½digo y la descripcion de cada atributo de la justificacion del medicamento
	 * o servicio
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param esArticulo
	 * @return
	 */
	public static ResultSetDecorator obtenerAtributosJustificacion(Connection con,int numeroSolicitud,int parametro,boolean esArticulo)
	{
		try
		{
			PreparedStatementDecorator pst;
			if(esArticulo)
				pst= new PreparedStatementDecorator(con.prepareStatement(obtenerAtributosJustificacionMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			else
				pst= new PreparedStatementDecorator(con.prepareStatement(obtenerAtributosJustificacionServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,parametro);
			
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAtributosJustificacion de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}

	/**
	 * Mï¿½todo para cargar los datos del embarazo necesarios en la valoracion de gineco!
	 * Embarazada - FUR - FPP - Edad Gestacional
	 * @param con
	 * @param codigoPersona
	 * @return Vector con los datos del embarazo
	 */
	public static Vector cargarDatosEmbarazo(Connection con, int codigoPersona)
	{
		String consultaDatosEmbarazoStr="SELECT fin_embarazo AS fin_embarazo, to_char(fur,'dd/mm/yyyy') AS fur, to_char(fpp,'dd/mm/yyyy') AS fpp, edad_gestacional || '' AS edadGestacional FROM hoja_obstetrica WHERE codigo=(SELECT MAX(codigo) FROM hoja_obstetrica WHERE paciente=?)";
		try
		{
			PreparedStatementDecorator datosEmbarazoStm= new PreparedStatementDecorator(con.prepareStatement(consultaDatosEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			datosEmbarazoStm.setInt(1, codigoPersona);
			ResultSetDecorator resultado=new ResultSetDecorator(datosEmbarazoStm.executeQuery());
			if(resultado.next())
			{
				Vector datos=new Vector();
				Boolean embarazada=new Boolean(!resultado.getBoolean("fin_embarazo"));
				String fur=resultado.getString("fur");
				String fpp=resultado.getString("fpp");
				String edadGestacional=resultado.getString("edadGestacional");
				datos.add(embarazada);
				datos.add(fur);
				datos.add(fpp);
				datos.add(edadGestacional);
				return datos;
			}
			else
			{
				return null; 
			}
		}
		catch (SQLException e)
		{
			logger.error("Error cargando los datos del embarazo : "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo para el ï¿½ltimo tipo de parto
	 * @param con
	 * @param codigoPaciente
	 * @return InfoDatosInt con codigo y nombre del ï¿½ltimo tipo de parto
	 */
	public static InfoDatosInt obtenerUltimoTipoParto(Connection con, int codigoPaciente)
	{
		String obtenerUltimoTipoParto="SELECT emb.trabajo_parto AS tipoParto, tp.nombre AS nombre FROM ant_gineco_embarazo emb INNER JOIN tipos_trabajo_parto tp ON(emb.trabajo_parto=tp.codigo) WHERE emb.codigo_paciente=? AND emb.trabajo_parto=tp.codigo AND emb.codigo=(SELECT MAX(codigo) FROM ant_gineco_embarazo WHERE codigo_paciente=?)";
		try
		{
			PreparedStatementDecorator ultimoTipoPartoStm= new PreparedStatementDecorator(con.prepareStatement(obtenerUltimoTipoParto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ultimoTipoPartoStm.setInt(1, codigoPaciente);
			ultimoTipoPartoStm.setInt(2, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(ultimoTipoPartoStm.executeQuery());
			InfoDatosInt tipoParto=new InfoDatosInt();
			if(resultado.next())
			{
				tipoParto.setCodigo(resultado.getInt("tipoParto"));
				tipoParto.setNombre(resultado.getString("nombre"));
			}
			else
			{
				tipoParto.setCodigo(-1);
				tipoParto.setDescripcion("No se encontrï¿½ tipo de parto");
			}
			return tipoParto;
		}
		catch (SQLException e)
		{
			logger.error("Error obteniendo el ultimo tipo de parto : "+e);
			return null;
		}
	}

	/**
	 * Mï¿½todo para cargar los datos de los antecedentes gineco-obstetricos necesarios en la valoracion de gineco!
	 * EdadMenarquia - OtraEdadMenarquia - EdadMenopausia - OtraEdadMenopausia
	 * @param con
	 * @param codigoPersona
	 * @return Vector con los datos del embarazo
	 */
	public static Vector cargarDatosAntGineco(Connection con, int codigoPersona)
	{
		String consultaDatosEmbarazoStr=
			"SELECT a.edad_menarquia || '' AS edadMenarquia, ma.nombre AS nombreEdadMenarquia, a.otra_edad_menarquia || '' AS otraEdadMenarquia, a.edad_menopausia || '' AS edadMenopausia, mo.nombre AS nombreEdadMenopausia, a.otra_edad_menopausia || '' AS otraEdadMenopausia" +
			" FROM ant_gineco_obste a" +
			" INNER JOIN rangos_edad_menarquia ma ON(a.edad_menarquia=ma.codigo)" +
			" INNER JOIN rangos_edad_menopausia mo ON(a.edad_menopausia=mo.codigo)" +
			" WHERE codigo_paciente=?";
		
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexiï¿½n (SqlBaseSolicitudDao)"+e1.toString());
		}
		
		try
		{
			PreparedStatementDecorator datosEmbarazoStm= new PreparedStatementDecorator(con.prepareStatement(consultaDatosEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			datosEmbarazoStm.setInt(1, codigoPersona);
			ResultSetDecorator resultado=new ResultSetDecorator(datosEmbarazoStm.executeQuery());
			if(resultado.next())
			{
				Vector datos=new Vector();
				String edadMenarquia=resultado.getString("edadMenarquia");
				String otraEdadMenarquia=resultado.getString("otraEdadMenarquia");
				String edadMenopausia=resultado.getString("edadMenopausia");
				String otraEdadMenopausia=resultado.getString("otraEdadMenopausia");
				String nombreEdadMenarquia=resultado.getString("nombreEdadMenarquia");
				String nombreEdadMenopausia=resultado.getString("nombreEdadMenopausia");
				datos.add(edadMenarquia);
				datos.add(otraEdadMenarquia);
				datos.add(edadMenopausia);
				datos.add(otraEdadMenopausia);
				datos.add(nombreEdadMenarquia);
				datos.add(nombreEdadMenopausia);
				return datos;
			}
			else
			{
				return null; 
			}
		}
		catch (SQLException e)
		{
			logger.error("Error cargando los datos de los antecedentes gineco-obstetricos : "+e);
			return null;
		}
	}

	/**
	 * Mï¿½todo para obtener el ï¿½ltimo nï¿½mero del embarazo del paciente
	 * @param con -> Connection
	 * @param codigoPaciente -> int
	 * @return numeroEmbarazo -> int
	 */
	public static int obtenerUltimoNumeroEmbarazo(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerUltimoNumeroEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("consulta-->"+obtenerUltimoNumeroEmbarazoStr+"  codPac->"+codigoPaciente);
			pst.setInt(1,codigoPaciente);
			pst.setInt(2,codigoPaciente);

			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("numero_embarazo");
			}
			else
			{
				return 0;
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUltimoNumeroEmbarazo de SqlBaseUtilidadesDao: "+e);
			return -1;
		}
		
	}

	/**
	 * @param con
	 * @param padre
	 * @return
	 */
	public static int[] consultarTiposPartoVaginal(Connection con, String padre)
	{
		String sql="SELECT codigo AS codigo FROM tipos_parto WHERE padre=?";
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setString(1, padre);
			ResultSetDecorator res=new ResultSetDecorator(stm.executeQuery());
			int cont=0;
			while(res.next())
			{
				cont++;
			}
			res=new ResultSetDecorator(stm.executeQuery());
			int[] dev=new int[cont];
			int i=0;
			while(res.next())
			{
				dev[i]=res.getInt("codigo");
				i++;
			}
			return dev;
		}
		catch (SQLException e)
		{
			logger.error("error consultando los tipos de parto : "+e);
			return null;
		}
	}

	/**
	 * Mï¿½todo para consultar embarazos sin finalizar
	 * @param con Conexion con la BD
	 * @param codigoPaciente Codigo del paciente
	 * @param codigoEmbarazo Codigo del embarazo
	 * @return true si el embarazo no se a finalizado
	 */
	public static boolean esEmbarazoSinTerminar(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		try
		{
			String consulta="SELECT count(1) AS numResultados from ant_gineco_embarazo WHERE codigo_paciente=? AND codigo=? AND fin_embarazo="+ValoresPorDefecto.getValorFalseParaConsultas();
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoPaciente);
			statement.setInt(2, codigoEmbarazo);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0?true:false;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando embarazos sin terminar: "+e);
			return false;
		}
	}

	/**
	 * Mï¿½todo para consultar en la BD el nombre de la edad menarquia
	 * o menopausia
	 * @param con
	 * @param codigoRango
	 * @param esMenarquia true consulta la menarqui, false la menopausia
	 * @return Striong con el nombre obtenido
	 */
	public static String obtenerNombreEdadMenarquiaOMenopausia(Connection con, int codigoRango, boolean esMenarquia)
	{
		String tabla="";
		if(esMenarquia)
		{
			tabla="rangos_edad_menarquia";
		}
		else
		{
			tabla="rangos_edad_menopausia";
		}
		String consultarNombreRango="SELECT nombre AS nombre FROM "+tabla+" WHERE codigo=?";
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consultarNombreRango,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoRango);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getString("nombre");
			}
			else
			{
				return "";
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nombre de la edad menarquia o menopausia : "+e);
			return null;
		}
	}

	/**
	 * Mï¿½todo para obtener el ï¿½ltimo valor de la altura uterina en la valoraciï¿½n gineco-obstï¿½trica
	 * 
	 * @param con
	 * @param sentencia
	 * @return
	 */
	public static String obtenerAlturaUterinaValoracion(Connection con, String sentencia)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===>Consulta Altura Uterina: "+sentencia);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getString("altura_uterina");
			}
			else
			{
				return "";
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el ï¿½ltimo valor de la altura uterina : "+e);
			return null;
		}
		
	}
	
	/**	 
	 * Mï¿½todo usado para obtener los pooles vigentes en los cuales 
	 * se encuentra el medico, 
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param fecha String
	 * @param codMedico int  
	 * @return ResultSet
	 * @author jarloc
	 */
	public static ResultSetDecorator obtenerPoolesMedico(Connection con, String fecha, int codMedico, boolean activos)
	{
		String obtenerPoolesMedicoStr="SELECT DISTINCT " +
										"pp.pool AS codPool," +
										"p.descripcion AS descripcion " +
									"FROM " +
										"participaciones_pooles pp " +
									"INNER JOIN " +
										"pooles p ON (p.codigo=pp.pool) " +
									"WHERE " +
										"(pp.fecha_retiro >= ? OR pp.fecha_retiro is NULL) ";
		
		if(codMedico!=ConstantesBD.codigoNuncaValido)
			obtenerPoolesMedicoStr+= "AND pp.medico="+codMedico+" ";
		
		if(activos)
			obtenerPoolesMedicoStr+= "AND p.activo=1 ";
		else
			obtenerPoolesMedicoStr+= "AND p.activo=0 ";
		
		obtenerPoolesMedicoStr += "ORDER BY p.descripcion ";
		
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexiï¿½n (SqlBaseUtilidadesDao)"+e1.toString());
		}
		
		try
		{		   
		    PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerPoolesMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			return new ResultSetDecorator(pst.executeQuery());		
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPoolesMedico de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	

	/**
	 * Mï¿½todo para obtener el noimbre del concepto de menstruaciï¿½n
	 * recibiendo como parï¿½mero el cï¿½digo
	 * @param con Connection Conexiï¿½n con la BD
	 * @param conceptoMenstruacion int Codigo del concepto Menstruaciï¿½n
	 * @return
	 */
	public static String obtenerNombreConceptoMenstruacion(Connection con, int conceptoMenstruacion)
	{
		String consultarNombreConeptoMenstruacion="SELECT getNombreConceptoMenstruacion(?) AS nombreConcepto FROM conceptos_menstruacion";
		try
		{
			ResultSetDecorator resultado=UtilidadBD.ejecucionGenericaResultSetDecorator(con, conceptoMenstruacion, 1, consultarNombreConeptoMenstruacion);
			if(resultado.next())
			{
				return resultado.getString("nombreConcepto");
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nombre del concepto de la menstruaciï¿½n : "+e);
			return null;
		}
	}

	/**
	 * @param con
	 * @param cadena
	 * @return
	 */
	public static int getSiguienteValorSecuencia(Connection con, String cadena) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("secuencia");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	

	
	

	/**
	 * carga el esuqema tarifario de una solicitud de cx
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int getEsquemaTarifarioSolCx(Connection con, String codigo)
	{
	    try
		{
	    	PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(getEsquemaTarifarioSolCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("esquemaTarifario");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static int obtenerEstadoCuentaCobro(Connection con, double cuentaCobro, int institucion)
	{
		String sentencia="SELECT estado as estado from cuentas_cobro where numero_cuenta_cobro=? and institucion=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("estado");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error el estado de la cuenta cobro. de SqlBaseUtilidadesDao :"+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @return
	 */
	public static String obtenerFechaRadicacionCuentaCobro(Connection con, double cuentaCobro) 
	{
		String sentencia="SELECT fecha_radicacion as fecharadicacion from cuentas_cobro where numero_cuenta_cobro=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,cuentaCobro);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString("fecharadicacion");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error el estado de la cuenta cobro. de SqlBaseUtilidadesDao :"+e);
		}
		return null;
	}
	
	/**
	 * Mï¿½todo que consulta los datos del tipo de monitoreo
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public static String[] getTipoMonitoreo(Connection con,int tipoMonitoreo)
	{
		String datos[]=new String[4];
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(getTipoMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tipoMonitoreo);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				datos[0]=rs.getString("codigo");
				datos[1]=rs.getString("nombre");
				datos[2]=rs.getString("prioridad_cobro");
				datos[3]=rs.getString("servicio");
				return datos;
			}
			else
			{
				return null;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getTipoMonitoreo de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo usado para consultar la fecha de la orden de salida
	 * de una cuenta de Urgencias u Hospitalizaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param institucion
	 * @return si no se encuentra la fecha devuelve cadena vacï¿½a
	 */
	public static String obtenerFechaOrdenSalida(Connection con,int idCuenta,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerFechaOrdenSalidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			pst.setInt(2,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("fecha");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaOrdenSalida de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que retorna TRUE para saber si la cuenta tiene solicitud de estancia
	 * para la fecha estipulada, de lo contrario retorna false
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public static boolean haySolicitudEstancia(Connection con,int idCuenta,String fechaEstancia,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(haySolicitudEstanciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setInt(3,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en haySolicitudEstancia de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo para consultar los datos generales de una persona en AXIOMA
	 * sin importar si es mï¿½dico, usuario o paciente
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public static HashMap obtenerDatosPersona(Connection con,String tipoId,String numeroId)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con,obtenerDatosPersonaStr);
			pst.setString(1,numeroId);
			pst.setString(2,tipoId);
						
			logger.info(pst);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosPersona de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerSaldoFactura(Connection con, int codigoFactura) 
	{
		String cadena="SELECT (valor_cartera - valor_pagos - ajustes_credito + ajustes_debito) as saldo from facturas where codigo=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				return rs.getDouble("saldo");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValidoDouble;	
	}
	
	/**
	 * Mï¿½todo usado para verificar si en la fucnionalidad de 2ï¿½ Nivel
	 * se debe esconder los datos del paciente del cabezaote superior
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean deboEsconderPaciente(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(deboEsconderPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en deboEsconderPaciente de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}

	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @return
	 */
	public static boolean existeAjusteNivelFacturaEmpresa(Connection con, double codigo, int factura) 
	{
		String cadena="SELECT codigo from  ajus_fact_empresa where codigo="+codigo+" and factura="+factura;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param con
	 * @param cuentacobro
	 * @param institucion
	 * @return
	 */
	public static int numFacturasCuentaCobro(Connection con, double cuentacobro, int institucion) 
	{
		String cadena="SELECT count(1) as facturas from facturas where numero_cuenta_cobro ="+cuentacobro+" and institucion="+institucion;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("facturas");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param con
	 * @param factura
	 * @return
	 */
	public static int numServicosArticulosFactura(Connection con, int factura) 
	{
		String cadena="";
		PreparedStatementDecorator ps=null,ps1=null;
		ResultSetDecorator rs,rs1;
		int temp=0;
		try 
		{
			cadena="SELECT count(1) as servicios from det_factura_solicitud where articulo is null and factura="+factura;
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				temp=rs.getInt("servicios");
			}
			cadena="select count(1) as articulos from (select 1  from det_factura_solicitud where servicio is null and factura="+factura+" group by articulo)  tabla";
			ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs1 = new ResultSetDecorator(ps1.executeQuery());
			if(rs1.next())
			{
				temp=temp+rs1.getInt("articulos");
			}
		} catch (SQLException e) {
			temp=0;
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * @param con
	 * @param detFactSolicitud
	 * @return
	 */
	public static double obtenerPorcentajePoolFacturacion(Connection con, int detFactSolicitud) 
	{
		String cadena="SELECT porcentaje_pool as porcentajepool from det_factura_solicitud where codigo="+detFactSolicitud;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getDouble("porcentajepool");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param con
	 * @param loginUsuario
	 * @param codigo_func
	 * @return
	 */
	public static boolean tieneRolFuncionalidad(Connection con, String loginUsuario, int codigo_func) 
	{
		String cadena="SELECT count(1) as numRol  from roles_funcionalidades rf inner join roles_usuarios ru on(ru.nombre_rol=rf.nombre_rol) where ru.login='"+loginUsuario+"' and rf.codigo_func="+codigo_func;
		boolean respuesta=false;
		ResultSetDecorator rs;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				respuesta=(rs.getInt("numRol")>0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean isDefinidoConsecutivoAjustesDebito(Connection con, int institucion)
	{
        
		/*String cadena="SELECT nombre from consecutivos where nombre='"+ConstantesBD.nombreConsecutivoAjustesDebito+"' and (valor ='' or valor is null) and institucion="+institucion;
		ResultSetDecorator rs;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			return (!rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;*/
        return isDefinidoConsecutivo(con, institucion, ConstantesBD.nombreConsecutivoAjustesDebito);
	}

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean isDefinidoConsecutivoAjustesCredito(Connection con, int institucion) 
	{
        /*
		String cadena="SELECT nombre from consecutivos where nombre='"+ConstantesBD.nombreConsecutivoAjustesCredito+"' and (valor ='' or valor is null) and institucion="+institucion;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			return (!rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
        */
        return isDefinidoConsecutivo(con, institucion, ConstantesBD.nombreConsecutivoAjustesCredito);
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static InfoDatosInt obtenerConvenioCuentaCobro(Connection con, double cuentaCobro, int institucion) 
	{
		String cadena="SELECT cc.convenio as codigo,c.nombre as nombre from cuentas_cobro cc inner join convenios c on (cc.convenio=c.codigo) where cc.numero_cuenta_cobro="+cuentaCobro+" and cc.institucion="+institucion;
		InfoDatosInt info=new InfoDatosInt();
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				info.setCodigo(rs.getInt("codigo"));
				info.setNombre(rs.getString("nombre"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return info;	}

	/**
	 * @param con
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public static String obtenerDescripcionConceptoAjuste(Connection con, String conceptoAjuste, int institucion) 
	{
		String cadena="SELECT descripcion as descripcion from concepto_ajustes_cartera where codigo='"+conceptoAjuste+"' and institucion="+institucion;
		String desc="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				desc=rs.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return desc;
		
	}

	/**
	 * @param con
	 * @param Mï¿½todoAjuste
	 * @return
	 */
	public static String obtenerDescripcionMetodoAjuste(Connection con, String MetodoAjuste) 
	{
		String cadena="SELECT descripcion as descripcion from metodo_ajustes_cartera where codigo='"+MetodoAjuste+"'";
		String desc="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				desc=rs.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return desc;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static int obtenerFacturaAjuste(Connection con, double codigoAjuste) 
	{
		String cadena=" SELECT factura as factura from  ajus_fact_empresa where codigo="+codigoAjuste;
		int fac=ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				fac=rs.getInt("factura");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fac;
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerConsecutivoFactura(Connection con, int codigoFactura) 
	{
		String cadena="SELECT consecutivo_factura as consecutivofactura from facturas where codigo="+codigoFactura;
		String cod="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				cod=rs.getString("consecutivofactura");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cod;
	}

	/**
	 * @param con
	 * @param factura
	 * @return
	 */
	public static InfoDatosInt obtenerConvenioFactura(Connection con, int factura) 
	{
		String cadena="SELECT f.convenio as codigo,c.nombre as nombre from facturas f inner join convenios c on (f.convenio=c.codigo) where f.codigo="+factura;
		InfoDatosInt info=new InfoDatosInt();
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				info.setCodigo(rs.getInt("codigo"));
				info.setNombre(rs.getString("nombre"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static String obtenerFechaAprobacionAjustes(Connection con, double codigoAjuste)
	{
		String cadena="SELECT fecha_aprobacion as fechaaprobacion from ajus_empresa_aprobados where codigo_ajuste="+codigoAjuste;
		String cod="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				cod=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaaprobacion"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cod;
	}
	
	/**
	 * Mï¿½todo usado para consultar la fecha de reversiï¿½n de egreso
	  * @param con
	 * @param idCuenta
	 * @return fecha de reversiï¿½n
	 */
	public static String obtenerFechaReversionEgreso(Connection con,int idCuenta)
	{
		String sentencia="SELECT fecha_reversion_egreso as fecha from egresos where cuenta = ?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getString("fecha")==null)
					return "";
				else
				return rs.getString("fecha");
			}
			else
				{	
					return "";
				}
		}
		
		catch(SQLException e)
		{
			logger.error("Error obteniendo la fecha de reversion de egreso en SqlBaseUtilidadesDao :"+e);
			return "";
		}
		
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean esAjusteDeReversion(Connection con, double codigoAjuste) 
	{
		
		String cadena="SELECT * FROM ajustes_empresa WHERE codigo ="+codigoAjuste+" AND cod_ajuste_reversado is not null";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static String obtenerestadoAjuste(Connection con, double codigoAjuste) 
	{
		String cadena="SELECT ae.estado as codigo,ec.descripcion as descripcion from ajustes_empresa ae inner join estados_cartera ec on (ae.estado=ec.codigo) where ae.codigo="+codigoAjuste;
		String cod="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				cod=rs.getString("codigo")+ConstantesBD.separadorSplit+rs.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cod;
	}

	/**
	 * @param con
	 * @param numeroAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @return
	 */
	public static double obtenercodigoAjusteEmpresa(Connection con, String numeroAjuste, String tipoAjuste, int institucion) 
	{
		double codigo=ConstantesBD.codigoNuncaValido;
		String tipAjuste="";
		if(tipoAjuste.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
		{
			tipAjuste=ConstantesBD.codigoAjusteCreditoCuentaCobro+","+ConstantesBD.codigoAjusteCreditoFactura;
		}
		else if(tipoAjuste.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
		{
			tipAjuste=ConstantesBD.codigoAjusteDebitoCuentaCobro+","+ConstantesBD.codigoAjusteDebitoFactura;
		}
		
		String cadena="SELECT codigo as codigo from ajustes_empresa where consecutivo_ajuste = '"+numeroAjuste+"' and tipo_ajuste in ("+tipAjuste+") and institucion="+institucion;
		
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigo= rs.getDouble("codigo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return codigo;
	}
	
	/**
	 * Salas Cirugï¿½a:
	 * Mï¿½todo usado para obtener el nombre de un tipo de sala
	 * parametrizado en el sistema
	 * @param con
	 * @param codigoTipoSala
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreTipoSala(Connection con,int codigoTipoSala)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerNombreTipoSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoTipoSala);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("tipo_sala");
			else
				return null;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoSala de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Salas Cirugï¿½a:
	 * Mï¿½todo que consulta el nombre [0] y acrï¿½nimo [1] del tipo
	 * de asocio.
	 * @param con
	 * @param codigoTipoAsocio
	 * @return
	 */
	public static String[] obtenerNombreTipoAsocio(Connection con,int codigoTipoAsocio)
	{
		try
		{
			//vector donde se alamcenarï¿½ el nombre y el acrï¿½nimo del tipo de asocio
			String vector[]=new String[2];
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerNombreTipoAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoTipoAsocio);
			logger.info("-->"+obtenerNombreTipoAsocioStr+"-->"+codigoTipoAsocio);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				vector[0]=rs.getString("nombre");
				vector[1]=rs.getString("acronimo");
				return vector;
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoAsocio de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta la descripciï¿½n de un esquema tarifario
	 * esGeneral => "true" se consultarï¿½ un esquema tarifario general de la tabla  esq_tar_porcen_cx
	 * esGeneral=> "false" se cosnultarï¿½ un esquema tarifario especï¿½fico de la tabla esquemas_tarifarios
	 * @param con
	 * @param esGeneral
	 * @param codigo
	 * @param obtenerNomEsquemaTarifarioStr
	 * @return retorna null si no hay resultados
	 */
	public static String obtenerNomEsquemaTarifario(Connection con,String esGeneral,int codigo,String obtenerNomEsquemaTarifarioStr)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerNomEsquemaTarifarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			pst.setString(2,esGeneral);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("esquema_tarifario");
			else
				return null;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNomEsquemaTarifario de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta el nombre del convenio dado la cuenta
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNombreConvenio(Connection con, int cuenta)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerNombreConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,cuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getString("nombreConvenio");
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener el nombre de un convenio de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta el nombre del convenio dado el codigo del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNombreConvenioOriginal(Connection con, int codigoConvenio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement("SELECT nombre FROM convenios WHERE codigo = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoConvenio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				return (rs.getString("nombre")!=null)?rs.getString("nombre"):"";
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerNombreConvenioOriginal",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerNombreConvenioOriginal", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return "";
	}

    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static String obtenerEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        String cadena="SELECT rc.estado as codigo,erc.descripcion as descripcion from recibos_caja rc inner join estados_recibos_caja erc on (rc.estado=erc.codigo) where rc.numero_recibo_caja='"+numeroReciboCaja+"' and rc.institucion="+institucion;
		String Resultado="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			if(Utilidades.convertirAEntero(numeroReciboCaja)>0)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs =new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
				    Resultado=rs.getString("codigo")+ConstantesBD.separadorSplit+rs.getString("descripcion");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Resultado;
    }

    
    /**
     * @param con
     * @param institucion
     * @param nombreConsecutivo
     * @return
     */
    public static boolean isDefinidoConsecutivo(Connection con, int institucion, String nombreConsecutivo)
    {
        //String cadena="SELECT nombre from consecutivos where nombre='"+nombreConsecutivo+"' and (valor ='' or valor is null) and institucion="+institucion;
        /*ResultSetDecorator rs;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			return (!rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
        //cambio con el nuevo manejo de los consecutivos
        String cadena="SELECT nombre from consecutivos where nombre='"+nombreConsecutivo+"' and institucion="+institucion;
        ResultSetDecorator rs;
        PreparedStatementDecorator ps=null;
        try {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery());
            return (rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    /**
     * @param con
     * @param codigoFactura
     * @param estado
     * @return
     */
    public static boolean cambiarEstadoPacienteFactura(Connection con, int codigoFactura, int estado)
    {
        String cadena="UPDATE facturas SET estado_paciente = ? where codigo=?";
        PreparedStatementDecorator ps=null;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,estado);
            ps.setInt(2,codigoFactura);
            return ps.executeUpdate()>0;
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }

    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @return
     */
    public static int obtenerCodigoFacturaPagosPaciente(Connection con, int codigoPagoPaciente)
    {
        String cadena="SELECT factura as factura from pagos_facturas_paciente where codigo="+codigoPagoPaciente;
		ResultSetDecorator rs;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
			    return rs.getInt("factura");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
    }

    
    /**
     * @param con
     * @param codigoPago
     * @param codigoEstado
     * @return
     */
    public static boolean cambiarEstadoPagoFacturaPacient(Connection con, int codigoPago, int codigoEstado)
    {
        String cadena="UPDATE pagos_facturas_paciente set estado = ? where codigo=?";
        PreparedStatementDecorator ps=null;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoEstado);
            ps.setInt(2,codigoPago);
            return ps.executeUpdate()>0;
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }

    
    /**
     * @param con
     * @param codigoPago
     * @param codigoEstado
     * @return
     */
    public static boolean cambiarEstadoPagosGeneralEmpresa(Connection con, int codigoPago, int codigoEstado)
    {
        String cadena="UPDATE pagos_general_empresa set estado = ? where codigo=?";
        PreparedStatementDecorator ps=null;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoEstado);
            ps.setInt(2,codigoPago);
            return ps.executeUpdate()>0;
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }

    
    /**
     * @param con
     * @param codigoPago
     * @return
     */
    public static String obtenerEstadoPagosEmpresa(Connection con, int codigoPago)
    {
        String cadena="SELECT pge.estado as codigoestado,ep.descripcion as descripcion from pagos_general_empresa pge inner join estados_pagos ep  on ( pge.estado=ep.codigo) where pge.codigo="+codigoPago;
		ResultSetDecorator rs;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
			    return rs.getInt("codigoestado")+ConstantesBD.separadorSplit+rs.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "-1"+ConstantesBD.separadorSplit+"-1";
    }

    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static int pacienteAbonoReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        String cadena="SELECT distinct  paciente as paciente from movimientos_abonos where codigo_documento="+numeroReciboCaja+" and tipo="+codigoTipoDocumento+" and institucion="+institucion;
		ResultSetDecorator rs;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
			    return rs.getInt("paciente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
    }

    
    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public static int numCajasAsociadasUsuario(Connection con, String loginUsuario)
    {
        String cadena="SELECT count(1) as cajas from cajas_cajeros cc inner join cajas c on(cc.caja=c.consecutivo)where cc.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"  and usuario='"+loginUsuario+"'";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("cajas");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
    }
    
    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public static int numCajasAsociadasUsuarioXcentroAtencion(Connection con, String loginUsuario, String codigoCentroAtencion)
    {
        String cadena="SELECT count(1) as cajas from tesoreria.cajas_cajeros cc inner join tesoreria.cajas c on(cc.caja=c.consecutivo)where cc.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"  and usuario='"+loginUsuario+"' and c.centro_atencion="+codigoCentroAtencion;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("cajas");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
    }

    /**
     * 
     * @param con
     * @param loginUsuario
     * @param codigoCentroAtencion
     * @param tipos
     * @return
     */
    public static int numCajasAsociadasUsuarioXcentroAtencion(Connection con, String loginUsuario, String codigoCentroAtencion, int[] tipos)
    {
        String cadena="SELECT count(1) as cajas from cajas_cajeros cc inner join cajas c on(cc.caja=c.consecutivo)where cc.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"  and usuario='"+loginUsuario+"' and c.centro_atencion="+codigoCentroAtencion;
        
        if(tipos!=null && tipos.length>0)
        {
        	cadena+=" AND(";
        	String cadenaTipos="";
            for(int tipo: tipos)
            {
            	if(!cadenaTipos.isEmpty())
            	{
            		cadenaTipos+=" OR ";
            	}
            	cadenaTipos+="c.tipo="+tipo;
            }
            cadena+=cadenaTipos+")";
        }
        
        logger.info(cadena);
        
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("cajas");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
    }

    
    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public static ResultSetDecorator getConsecutivoCajaUsuario(Connection con, String loginUsuario, String codigoCentroAtencion)
    {
    	String cadena="SELECT cc.caja as consecutivocaja from tesoreria.cajas_cajeros cc inner join tesoreria.cajas c on(cc.caja=c.consecutivo)where cc.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"  and usuario='"+loginUsuario+"' and c.centro_atencion=" +codigoCentroAtencion;
        //String cadena="SELECT caja as consecutivocaja from cajas_cajeros where activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and usuario='"+loginUsuario+"'";
		PreparedStatementDecorator ps=null;
		try
        {
		    ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            return new ResultSetDecorator(ps.executeQuery());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public static int getCodigoCaja(Connection con, int consecutivoCaja)
    {
        String cadena="SELECT codigo as codigo from tesoreria.cajas where consecutivo ="+consecutivoCaja;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return ConstantesBD.codigoNuncaValido;
    }

    
    /**
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public static String getDescripcionCaja(Connection con, int consecutivoCaja)
    {
        String cadena="SELECT descripcion as descripcion from tesoreria.cajas where consecutivo ="+consecutivoCaja;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
    }
	
    /**
     * Mï¿½todo que verifica si una solicitud de cirugï¿½as tiene un pedido Qx.
     * asociado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean tienePedidoSolicitudQx(Connection con,int numeroSolicitud)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(tienePedidoSolicitudQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroSolicitud);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    		{
    			if(rs.getInt("resultado")>0)
    				return true;
    			else
    				return false;
    		}
    		else
    			return false;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en tienePedidoSolicitudQx de SqlBaseUtilidadesDao: "+e);
    		return false;
		}
    }
    
    /**
     * Mï¿½todo que verifica si una solicitud de cirugï¿½as tiene un peticion Qx.
     * asociado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean tieneSolicitudPeticionQxStr(Connection con,int numeroPeticion)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(tieneSolicitudPeticionQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroPeticion);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    		{
    			if(rs.getInt("resultado")>0)
    			{
    				return true;
    			}
    			else
    			{
    				return false;
    			}
    		}
    		else
    			return false;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en tieneSolicitudPeticionQx de SqlBaseUtilidadesDao: "+e);
    		return false;
		}
    }
    
    /**
     * Mï¿½todo que consulta la subcuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int getSubCuentaSolicitud(Connection con,int numeroSolicitud)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getSubCuentaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroSolicitud);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		int resp = 0;
    		
    		if(rs.next())
    			resp = rs.getInt("subcuenta");
    		
    		return resp;
    		
    			
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getSubCuentaSolicitud de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
    }
    
    /**
     * Mï¿½todo que consulta la cuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int getCuentaSolicitud(Connection con,int numeroSolicitud)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getCuentaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroSolicitud);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		int resp = 0;
    		
    		if(rs.next())
    			resp = rs.getInt("cuenta");
    		
    		pst.close();
    		rs.close();
    		
    		return resp;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getCuentaSolicitud de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
    }
    
    /**
     * Mï¿½todo que consulta el nombre de una especialidad
     * @param con
     * @param especialidad
     * @return
     */
    public static String getNombreEspecialidad(Connection con,int especialidad)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getNombreEspecialidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,especialidad);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    			return rs.getString("nombre");
    		else
    			return "";
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getNombreEspecialidad de SqlBaseUtilidadesDao: "+e);
    		return null;
		}
    }
    
    /**
     * Mï¿½todo que consulta el nombre de un pool
     * @param con
     * @param pool
     * @return
     */
    public static String getNombrePool(Connection con,int pool)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getNombrePoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,pool);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    			return rs.getString("descripcion");
    		else
    			return "";
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getNombrePool de SqlBaseUtilidadesDao: "+e);
    		return null;
		}
    }
    
    /**
     * Mï¿½todo que retorna la peticiï¿½n de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int getPeticionSolicitudCx(Connection con,int numeroSolicitud)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(getPeticionSolicitudCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroSolicitud);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    			return rs.getInt("peticion");
    		else
    			return -1;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getPeticionSolicitudCx de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
    }
    
    /**
     * Mï¿½todo que consulta la fecha del cargo de una orden quirï¿½rgica
     * retorna una cadena vacï¿½a en el caso de que la orden no tenga cargo generado
     * @param con
     * @param numeroSolicitud
     * @param orderBy
     * @return
     */
    public static String getFechaCargoOrdenCirugia(Connection con,int numeroSolicitud,String orderBy)
    {
    	try
		{
    		String consulta = getFechaCargoOrdenCirugiaStr + orderBy;
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,numeroSolicitud);
    		pst.setInt(2,numeroSolicitud);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    			return rs.getString("fecha_cargo");
    		else
    			return "";
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getFechaCargoOrdenCirugia de SqlBaseUtilidadesDao: "+e);
    		return "";
		}
    }
    
    /**
     * Mï¿½todo para saber si un convenio tiene (False - True) el atributo
	 * de info_adic_ingreso_convenios
     * @param con
     * @param codigoConvenio
     * @return
     */
	public static boolean convenioTieneIngresoInfoAdic(Connection con, int codigoConvenio)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(convenioTieneIngresoInfoAdicStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				if(resultado.getBoolean("infoAdicIngresoConvenios") == true)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando sisi un convenio tiene (False - True) el atributo de info_adic_ingreso_convenios "+e);
			return false;
		}
	}
	
	
	/**
	 * Mï¿½todo para obtener el saldo de un convenio dada su cuenta y el convenio
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @return
	 */
    public static double obtenerSaldoConvenio(Connection con,String idSubCuenta)
    {
    	try
		{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerSaldoConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setLong(1, Utilidades.convertirALong(idSubCuenta));
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		
    		if(rs.next())
    			return rs.getDouble("saldo");
    		else
    			return 0;
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerSaldoConvenio de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
    }
    
    /**
     * Mï¿½todo para obtener el codigo de un convenio segun una cuenta dada
     * @param con
     * @param idCuenta
     * @return
     */
    public static int obtenerCodigoConvenio(Connection con, int idCuenta)
    {
    	int resp=0;
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerCodigoConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, idCuenta);
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{
    			resp=rs.getInt("codigoConvenio");
    		}
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerCodigoConvenio de SqlBaseUtilidadesDao: "+e);
    		resp= -1;
		}
    	return resp;
    }
    
    /**
     * Mï¿½todo para obtener la fecha de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerFechaAdmisionUrg(Connection con, int idCuenta)
    {
    	String fechaAdmision ="";
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerFechaAdmisionUrgStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, idCuenta);
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{
    			fechaAdmision=rs.getString("fechaAdmision");
    		}
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerFechaAdmisionUrg de SqlBaseUtilidadesDao: "+e);
    		fechaAdmision=null;
		}
    	return fechaAdmision;
    }
    
   
    
    /**
     * Mï¿½todo para obtener la fecha de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerFechaAdmisionHosp(Connection con, int idCuenta)
    {
    	String fechaAdmision ="";
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerFechaAdmisionHospStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, idCuenta);
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{
    			fechaAdmision=rs.getString("fechaAdmision");
    		}
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerFechaAdmisionHosp de SqlBaseUtilidadesDao: "+e);
    		fechaAdmision=null;
		}
    	return fechaAdmision;
    }
    
    
    /**
     * Mï¿½todo para obtener la hora de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerHoraAdmisionHosp(Connection con, int idCuenta)
    {
    	String horaAdmision ="";
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerHoraAdmisionHospStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, idCuenta);
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{
    			horaAdmision=rs.getString("horaAdmision");
    		}
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerHoraAdmisionHosp de SqlBaseUtilidadesDao: "+e);
    		horaAdmision=null;
		}
    	return horaAdmision;
    }
    
    /**
     * Mï¿½todo para obtener la hora de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public static String obtenerHoraAdmisionUrg(Connection con, int idCuenta)
    {
    	String horaAdmision ="";
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerHoraAdmisionUrgStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, idCuenta);
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{
    			horaAdmision=rs.getString("horaAdmision");
    		}
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerHoraAdmisionUrg de SqlBaseUtilidadesDao: "+e);
    		horaAdmision=null;
		}
    	return horaAdmision;
    }
    
    /**
     * Mï¿½todo para obtener el tipo de anestesia de una solicitud de cirugï¿½as
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static InfoDatosInt obtenerTipoAnestesia(Connection con, int numeroSolicitud)
    {
    	String obtenerTipoAnestesiaStr="SELECT h.tipo_anestesia AS tipo_anestesia, t.descripcion AS descripcion FROM hoja_tipo_anestesia h INNER JOIN tipos_anestesia t ON(t.codigo=h.tipo_anestesia) WHERE numero_solicitud=?";
    	try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerTipoAnestesiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(pst.executeQuery());
			if(resultado.next())
			{
				return new InfoDatosInt(resultado.getInt("tipo_anestesia"), resultado.getString("descripcion"));
			}
			return new InfoDatosInt();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el tipo de anestesia : "+e);
			return null;
		}
    }
    
    /**
	 * Mï¿½todo que carga la hora de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @param consultaHora String con la sentencia para consultar la hora y los segundos de acuerdo a la BD
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public static String capturarHoraSegundosBD(Connection con, String consultaHoraSegundos)
	{
		try
		{
			PreparedStatementDecorator horaSt= new PreparedStatementDecorator(con.prepareStatement(consultaHoraSegundos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(horaSt.executeQuery());
			if(resultado.next())
			{
				return resultado.getString("hora");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la hora copn segundos de la BD "+e);
		}
		return null;
	}
    
	/**
	 * Mï¿½todo para obtener el nombre de un grupo de servicio dado su codigo
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public static String getNombreGrupoServicios(Connection con, int codigoGrupo)
    {
    	String grupoServicios ="";
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(getNomberGrupoServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, codigoGrupo);
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{
    			grupoServicios=rs.getString("nombreGrupo");
    		}
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getNomberGrupoServicios de SqlBaseUtilidadesDao: "+e);
    		grupoServicios="";
		}
    	return grupoServicios;
    }
	
	/**
	 * Mï¿½todo para obtener el total de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 */
	public static double obtenerTotalPresupuesto(Connection con, int codigoPresupuesto)
    {
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerTotalPresupuestoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, codigoPresupuesto);
    		pst.setInt(2, codigoPresupuesto);
    		
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{	
    			if(rs.getString("totalPresupuesto")!=null&&!rs.getString("totalPresupuesto").equals(""))
				{
    				return Double.parseDouble(rs.getString("totalPresupuesto")+"");
				}
    			else
    			{
    				return -1;
    			}
    		}
    		else
    		{
    			return -1;
    		}
			
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerTotalPresupuesto de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
    }
	
	/**
	 * Mï¿½todo para obtener el total de un grupo de servicios dado, relacionado
	 * con un presupuesto de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public static double obtenertotalGrupoServicios(Connection con, int presupuesto, int grupo)
	{
		String obtenertotalGrupoServiciosStr="SELECT sum((cantidad*valor_unitario)) as valorTotal FROM presupuesto_servicios ps INNER JOIN servicios s ON(ps.servicio=s.codigo)  WHERE ps.presupuesto=? AND s.grupo_servicio=? group by s.grupo_servicio";
		double totalGrupo=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenertotalGrupoServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, presupuesto);
			pst.setInt(2, grupo);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getString("valorTotal")!=null&&!rs.getString("valorTotal").equals(""))
				{
					totalGrupo=Double.parseDouble(rs.getString("valorTotal"));
				}
			}
			return totalGrupo;
			
		}
		catch(SQLException e)
		{
    		logger.error("Error en obtenertotalGrupoServicios de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el total de un grupo de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public static int obtenerTotalGrupoArticulos(Connection con, int presupuesto, int grupo)
	{
		String obtenerTotalGrupoArticulosStr=" SELECT sum((cantidad*valor_unitario)) as valorTotal FROM presupuesto_articulos pa INNER JOIN articulo a ON(pa.articulo=a.codigo) INNER JOIN subgrupo_inventario si ON(a.subgrupo=si.codigo) INNER JOIN grupo_inventario gi ON(si.grupo=gi.codigo)  WHERE pa.presupuesto=? AND gi.codigo=? group by gi.codigo";
		int totalGrupo=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerTotalGrupoArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, presupuesto);
			pst.setInt(2, grupo);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getString("valorTotal")!=null&&!rs.getString("valorTotal").equals(""))
				{
					totalGrupo=Integer.parseInt(rs.getString("valorTotal"));
				}
			}
			return totalGrupo;
			
		}
		catch(SQLException e)
		{
    		logger.error("Error en obtenerTotalGrupoArticulos de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el total de un subgrupo de articulos
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public static int obtenerTotalSubGrupoArticulos(Connection con, int presupuesto, int subgrupo)
	{
		String obtenerTotalSubGrupoArticulosStr=" SELECT sum((cantidad*valor_unitario)) as valorTotal FROM presupuesto_articulos pa INNER JOIN articulo a ON(pa.articulo=a.codigo) INNER JOIN subgrupo_inventario si ON(a.subgrupo=si.codigo) WHERE pa.presupuesto=? AND si.codigo=? group by si.codigo";
		int totalGrupo=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerTotalSubGrupoArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, presupuesto);
			pst.setInt(2, subgrupo);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getString("valorTotal")!=null&&!rs.getString("valorTotal").equals(""))
				{
					totalGrupo=Integer.parseInt(rs.getString("valorTotal"));
				}
			}
			return totalGrupo;
			
		}
		catch(SQLException e)
		{
    		logger.error("Error en obtenerTotalSubGrupoArticulos de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el total de los articulos de  un presupuesto dado asociado por calse de inventario
	 * @param con
	 * @param presupuesto
	 * @param claseinventario
	 * @return
	 */
	public static int obtenerTotalClaseInventarioArticulos(Connection con, int presupuesto, int claseinventario)
	{
		String obtenerTotalCalseInventarioArticulosStr=" SELECT sum((cantidad*valor_unitario)) as valorTotal FROM presupuesto_articulos pa INNER JOIN articulo a ON(pa.articulo=a.codigo) INNER JOIN subgrupo_inventario si ON(a.subgrupo=si.codigo) INNER JOIN clase_inventario ci ON(si.clase=ci.codigo) WHERE pa.presupuesto=? AND ci.codigo=? group by ci.codigo";
		int totalGrupo=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerTotalCalseInventarioArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, presupuesto);
			pst.setInt(2, claseinventario);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getString("valorTotal")!=null&&!rs.getString("valorTotal").equals(""))
				{
					totalGrupo=Integer.parseInt(rs.getString("valorTotal"));
				}
			}
			return totalGrupo;
			
		}
		catch(SQLException e)
		{
    		logger.error("Error en obtenerTotalCalseInventarioArticulos de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el total de los articulos de un presupuesto
	 * @param con
	 * @param presupuesto
	 * @return
	 */
	public static double obtenerTotalArticulosPresupuesto(Connection con, int presupuesto)
	{
		String obtenerTotalArticulosPresupuestoStr="SELECT sum((cantidad*valor_unitario)) as valorTotal from presupuesto_articulos where presupuesto=?";
		double totalArt=0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerTotalArticulosPresupuestoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, presupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getString("valorTotal")!=null&&!rs.getString("valorTotal").equals(""))
				{
					totalArt=Double.parseDouble(rs.getString("valorTotal"));
				}
			}
			return totalArt;
			
		}
		catch(SQLException e)
		{
    		logger.error("Error en obtenerTotalArticulosPresupuesto de SqlBaseUtilidadesDao: "+e);
    		return totalArt;
		}
	}
	

    /***
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static int obtenerNumeroOrdenSolicitud(Connection con, int numeroSolicitud)
    {
        String cadena="SELECT consecutivo_ordenes_medicas as consecutivo from solicitudes where numero_solicitud="+numeroSolicitud;
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
                return rs.getInt("consecutivo");
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultando la hora copn segundos de la BD "+e);
        }
        return ConstantesBD.codigoNuncaValido;
    }

    /**
     * 
     * @param con
     * @param codDetFactura
     * @return
     */
    public static int numAsociosCirugiaDetFactura(Connection con, int codDetFactura)
    {
        String cadena="";
        PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        int temp=0;
        try 
        {
            cadena="SELECT count(1) as cantidad from asocios_det_factura where codigo="+codDetFactura;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                temp=rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            temp=0;
            e.printStackTrace();
        }
        return temp;
    }

	/**
	 * Mï¿½todo que me indica si una cuenta tiene subcuentas
	 * independiente del estado de la misma
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean haySubcuentasParaCuentaDada(Connection con, int codigoCuenta)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) AS numResultados FROM cuentas c INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso) AND c.id=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoCuenta);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0;
			}
			return false;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si la cuenta "+codigoCuenta+" tiene subcuentas: "+e);
			return false;
		}
	}
	
	
	/**
     * Mï¿½todo que consulta la fecha de apertura de la cuenta de un paciente dado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static String getFechaAperturaCuenta(Connection con,int numeroCuenta)
    {
		try
		{
			String consulta = "	SELECT to_char(fecha_apertura, 'YYYY-MM-DD') AS fecha			 " +
							  "		   FROM cuentas " +
							  "		     	WHERE id = " + numeroCuenta;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery(consulta));
			
			if(rs.next())
			{
				return rs.getString("fecha");
			}
			else
			{
				return "";
			}	
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la fecha de apertura de la cuenta nro " + numeroCuenta + " [ SqlBaseUtilidad ] "+e);
			return "";
		}
	
    }

    
	/**
	 * Mï¿½todo para Obtener la fecha de una solicitud dada. a traves del 
	 * numero de la solicitud.
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String getFechaSolicitud(Connection con, int numeroSolicitud)
	{
		try
		{
			String consulta = "	SELECT to_char(fecha_solicitud, 'YYYY-MM-DD') AS fecha			 " +
							  "		   FROM solicitudes " +
							  "		     	WHERE numero_solicitud = " + numeroSolicitud;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery(consulta));
			
			if(rs.next())
			{
				return rs.getString("fecha");
			}
			else
			{
				return "";
			}	
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la fecha de la solicitud nro " + numeroSolicitud + " [ SqlBaseUtilidadValidacionDao] "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo para obtener el consecutivo de ordenes medicas dado su numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int getConsecutivoOrdenesMedicas(Connection con, int numeroSolicitud)
    {
        String cadena="";
        PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        int temp=0;
        try 
        {
            cadena="SELECT consecutivo_ordenes_medicas as consecutivo FROM solicitudes WHERE numero_solicitud ="+numeroSolicitud;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                temp=rs.getInt("consecutivo");
            }
        }
        catch (SQLException e) 
        {
            temp=0;
            e.printStackTrace();
        }
        return temp;
    }
	
	
	/**
	 * Mï¿½todo para obtener las observaciones de la informacion adicional de un paciente cargado en session
	 * @param con
	 * @param codPaciente
	 * @return
	 */
	public static String getObservacionesPacienteActivo(Connection con, int codPaciente)
    {
        String cadena="";
        PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        String temp="";
        try 
        {
            cadena="SELECT observaciones as observaciones FROM pacientes WHERE codigo_paciente = "+codPaciente;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                temp=rs.getString("observaciones");
            }
        }
        catch (SQLException e) 
        {
            temp="";
            e.printStackTrace();
        }
        return temp;
    }
	
	/**
	 * obtener el codigo de la cuenta asociada dado el codigo de la cuenta final
	 * retorna "" en caso de que no exista
	 */
	public static String getCuentaAsociada(Connection con, String codigoCuentaFinal,boolean activo)
    {
        PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        try 
        {
        	String consulta = getCuentaAsociadaStr;
        	if(activo)
        		consulta += " AND activo= "+ValoresPorDefecto.getValorTrueParaConsultas();
            ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,codigoCuentaFinal);
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                return rs.getString("cuentaInicial");
            }
        }
        catch (SQLException e) 
        {
        	e.printStackTrace();
            return "";
        }
		return "";
    }

	
	/**
	 * 
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static String getResponsableFactura(Connection con, int codFactura) 
	{
		PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        try 
        {
            ps= new PreparedStatementDecorator(con.prepareStatement("SELECT case when f.convenio ="+ConstantesBD.codigoConvenioParticular+" then ter.descripcion else getnombreconvenio(f.convenio) end as responsable from facturas f left outer join terceros ter on (f.cod_res_particular=ter.codigo) where f.codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codFactura);
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                return rs.getString("responsable");
            }
        }
        catch (SQLException e) 
        {
        	e.printStackTrace();
            return "";
        }
		return "";
	}

	public static double obtenerValorTotalReciboCaja(Connection con, int codigoInstitucionInt, String numReciboCaja) 
	{
		String cadena="";
        PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        double temp=0;
        try 
        {
            cadena="SELECT getTotalReciboCaja(rc.numero_recibo_caja,rc.institucion) as valortotal FROM recibos_caja rc WHERE numero_recibo_caja='"+numReciboCaja +"' and institucion="+codigoInstitucionInt+"";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                temp=rs.getDouble("valortotal");
            }
        }
        catch (SQLException e) 
        {
            temp=0;
            e.printStackTrace();
        }
        return temp;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param numReciboCaja
	 * @return
	 */
	public static String reciboCajaRecibidoDe(Connection con, int institucion, String numReciboCaja) 
	{
		PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        try 
        {
        	String cadena="SELECT rc.recibido_de as recibidode FROM recibos_caja rc WHERE numero_recibo_caja ='"+numReciboCaja +"' and institucion="+institucion+"";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                return rs.getString("recibidode");
            }
        }
        catch (SQLException e) 
        {
        	e.printStackTrace();
            return "";
        }
		return "";
	}

	
	/**
	 * Mï¿½todo que retorna la informacion basica de la anulacion de una factura.
	 * codigoFactura+separadorSplit+fechaanulacion+separadorSplit+horaanulacion+separadorSplit+usuarioanula+separadorSplit+consecutivoanulacion;
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerInformacionAnulacionFactura(Connection con, String codigoFactura) 
	{
		PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        String info="";
        try 
        {
        	String cadena="SELECT codigo as codigo,consecutivo_anulacion as consecutivoanulacion,to_char(fecha_grabacion,'dd/mm/yyyy') as fechaanulacion,substr(hora_grabacion||'',0,6) as horaanulacion,usuario as usuarioanula from anulaciones_facturas where codigo="+codigoFactura;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
            {
                info=rs.getString("codigo")+ConstantesBD.separadorSplit+rs.getString("fechaanulacion")+ConstantesBD.separadorSplit+rs.getString("horaanulacion")+ConstantesBD.separadorSplit+rs.getString("usuarioanula")+ConstantesBD.separadorSplit+rs.getString("consecutivoanulacion");
                return info;
            }
        }
        catch (SQLException e) 
        {
        	e.printStackTrace();
            return "";
        }
		return info;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerCodigoEstadoFacturacionFactura(Connection con, int codigoFactura) 
	{
		String sentencia="SELECT f.estado_facturacion as codigoEstado from facturas f where f.codigo=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigoEstado");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error el estado de factura. de SqlBaseUtilidadesDao :"+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerCodigoEstadoPacienteFactura(Connection con, int codigoFactura) 
	{
		String sentencia="SELECT f.estado_paciente as codigoEstado from facturas f where f.codigo=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigoEstado");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error el estado de factura. de SqlBaseUtilidadesDao :"+e);
		}
		return ConstantesBD.codigoNuncaValido;	}
	
	/**
	 * obtiene el total admin farmacia
	 * @param con
	 * @param codigoArticuloStr
	 * @param numeroSolicitudStr
	 * @param traidoUsuario
	 * @return
	 */
	public static int getTotalAdminFarmacia(Connection con, String codigoArticuloStr, String numeroSolicitudStr, boolean traidoUsuario, String fromYTabla)
	{
		PreparedStatementDecorator ps=null;
        ResultSetDecorator rs;
        int resp=-1;
        String cadena="";
        try 
        {
        	cadena="SELECT coalesce(gettotaladminfarmacia(?, ?, ";
        	if(traidoUsuario)
        		cadena+=ValoresPorDefecto.getValorTrueParaConsultas();
        	else
        		cadena+=ValoresPorDefecto.getValorFalseParaConsultas();
        	cadena+="), 0) AS total "+fromYTabla;
        	
        	ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setString(1, codigoArticuloStr);
        	ps.setString(2, numeroSolicitudStr);
        	
        	rs =new ResultSetDecorator(ps.executeQuery()); 
            if(rs.next())
                resp=rs.getInt("total");
            return resp;
        }
        catch (SQLException e) 
        {
        	e.printStackTrace();
            return -1;
        }
	}
	
	/**
	 * Mï¿½todo que devuelve el nombre del ï¿½ltimo tipo de monitoreo del paciente cuando estï¿½ en Cama UCI,
	 * ya sea de la orden mï¿½dica o la evoluciï¿½n
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @param obtenerNombre Booleano que indica si devuelve el nombre o el codigo
	 * @return nombre del ï¿½ltimo tipo de monitoreo
	 */
	public static String obtenerUltimoTipoMonitoreo (Connection con, int codigoCuenta, int institucion, boolean obtenerNombre)
	{
		String consultaStr;
		if(obtenerNombre)
		{
			consultaStr="SELECT tm.nombre AS monitoreo ";
		}
		else
		{
			consultaStr="SELECT tm.codigo AS monitoreo ";
		}
		consultaStr+=
					"FROM tipo_monitoreo tm " +
						"INNER JOIN orden_tipo_monitoreo otmon ON (otmon.tipo_monitoreo = tm.codigo) " +
						/*"INNER JOIN (SELECT MAX(otm.codigo_histo_encabezado) as codmax  " +
													"FROM orden_tipo_monitoreo otm " +
														"INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = otm.codigo_histo_encabezado)  " +
														"INNER JOIN ordenes_medicas om ON (enc.orden_medica = om.codigo)  " +
															"WHERE om.cuenta = ? ) tabla ON (tabla.codmax = otmon.codigo_histo_encabezado) " +*/
						"WHERE " +
						+obtenerMaximoMonitoreo(con, codigoCuenta)+"= otmon.codigo_histo_encabezado "+
						/*ESTE PEDAZO NO SE ESTABA USANDO,, NO LE VEO UTILIDAD"((SELECT cam.es_uci FROM traslado_cama tc  " +
											"INNER JOIN camas1 cam ON (tc.codigo_nueva_cama=cam.codigo) " +
												"WHERE tc.cuenta = ? ORDER BY tc.codigo DESC "+ValoresPorDefecto.getValorLimit1()+" 1)  " +
										"UNION " +
										"(SELECT cm.es_uci as es_uci FROM camas1 cm " +
											"INNER JOIN admisiones_urgencias au ON (cm.codigo=au.cama_observacion) " +
												"WHERE au.cuenta=?)) " +*/
						"AND institucion=?";

		try
		{
			logger.info("consultaStr-->"+consultaStr+"     codcu->"+codigoCuenta+" inst->"+institucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
		
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
		
			if(resultado.next())
			{
				return resultado.getString("monitoreo");
			}
			else
			{
				return "";
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+"Consultando el ï¿½ltimo tipo de monitoreo del paciente en Cama UCI: SqlBaseRegistroEnfermeriaDao "+e.toString());
			return "";
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	private static double obtenerMaximoMonitoreo(Connection con, int codigoCuenta)
	{
		String consultaStr="SELECT MAX(otm.codigo_histo_encabezado) as codmax  " +
							"FROM orden_tipo_monitoreo otm " +
							"INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = otm.codigo_histo_encabezado)  " +
							"INNER JOIN ordenes_medicas om ON (enc.orden_medica = om.codigo)  " +
							"WHERE om.cuenta = ? ";
		
		double retorna=ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{
			logger.info("consultaStr-->"+consultaStr+"     codcu->"+codigoCuenta);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				retorna= resultado.getDouble(1);
			}
			resultado.close();
			ps.close();
				
		}
		catch(SQLException e)
		{
			logger.warn(e+"Consultando el ï¿½ltimo tipo de monitoreo del paciente en Cama UCI: SqlBaseRegistroEnfermeriaDao "+e.toString());
		}
		return retorna;
	}
	
	
	
	/**
	 * Mï¿½todo implementado para obtener el codigo de la cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public static int getUltimaCamaTraslado(Connection con,int cuenta,String consulta)
	{
		try
		{
			/**MT 4452 Diana Ruiz*/
			//String consulta = getUltimaCamaTrasladoStr + adicion;			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,cuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt("cama");
			else 
				return 0;
		}
		catch(SQLException e)
		{
			logger.error("Error en getUltimaCamaTraslado de SqlBaseUtilidadesDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el codigo de la ocupacion Medica dado el codigo del medico.
	 * Retorna -1 si no encuentra la ocupacion con el codigo del medico dado.
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
    public static int obtenerOcupacionMedica(Connection con, int codigoMedico)
    {
        try
        {
            int resp=0;
            String obtenerOcupacionMedicaStr="SELECT ocupacion_medica as ocupacionmedica FROM medicos WHERE codigo_medico = ? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerOcupacionMedicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1,codigoMedico);
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
            {
                resp= Integer.parseInt(rs.getInt("ocupacionmedica")+"");
            }
            if(resp>0)
            {
                return resp;
            }
            else
            {
                return -1;
            }
        }
        catch(SQLException e)
        {
            logger.error("Error en  obtenerOcupacionMedica de SqlBaseUtilidadesDao: "+e);
            return -1;
        }
    }

    /**
     * 
     * @param con
     * @param codigoCama
     * @return
     */
	public static int obtenerCodigoEstadoCama(Connection con, int codigoCama) 
	{
		String sentencia="SELECT estado as estado from camas1 where codigo=?";
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCama);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("estado");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error el estado de la cama. de SqlBaseUtilidadesDao :"+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Mï¿½todo para retornar el numero de registros.
	 * @param con
	 * @param consulta
	 * @return
	 */
	public static int nroRegistrosConsulta(Connection con, String consulta) 
	{
		int rows = 0;
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				rows++;
			}
			rs.close();
			pst.close();
			
			return rows;
		}
		catch(SQLException e)
		{
			logger.error("Error al tratar de retornar el numero de la consulta :[ " + consulta + "]   "+e);
			return -1;
		}
	}
	
	/**
	 * Mï¿½todo que consulta la fecha de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getFechaEgreso(Connection con,int idCuenta)
	{
		try
		{
			String consulta = "SELECT CASE WHEN  fecha_egreso IS NULL THEN '' ELSE to_char(fecha_egreso,'DD/MM/YYYY') END  AS fecha FROM egresos WHERE cuenta = ? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("fecha");
			else
				return "";
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getFechaEgreso de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar la descripcion completa
	 * del diagnï¿½stico de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getNombreDiagnosticoEgreso(Connection con,int idCuenta)
	{
		try
		{
			String consulta = " SELECT "+  
				"'(' || e.diagnostico_principal || '-' || e.diagnostico_principal_cie || ') ' || d.nombre AS diagnostico "+ 
				"FROM  egresos e "+ 
				"INNER JOIN diagnosticos d ON(d.acronimo=e.diagnostico_principal AND d.tipo_cie = e.diagnostico_principal_cie) "+ 
				"WHERE  e.cuenta=? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("diagnostico");
			else
				return "";
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getNombreDiagnosticoEgreso de SQlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar la descripcion compelta
	 * del diagnï¿½stico de ingreso de un admision hospitalaria
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getNombreDiagnosticoIngreso(Connection con,int idCuenta)
	{
		try
		{
			String consulta = " SELECT "+ 
				"'(' || vh.diagnostico_ingreso || '-' || vh.diagnostico_cie_ingreso || ') ' || d.nombre AS diagnostico "+ 
				"FROM solicitudes s "+ 
				"INNER JOIN val_hospitalizacion vh ON (vh.numero_solicitud=s.numero_solicitud) "+ 
				"INNER JOIN diagnosticos d ON(d.acronimo=vh.diagnostico_ingreso AND d.tipo_cie = vh.diagnostico_cie_ingreso) "+ 
				"WHERE s.cuenta = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("diagnostico");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en getNombreDiagnosticoIngreso de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de servicios parametrizado en un formato de impresion de factura
	 * tien true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getImprimirDetalleTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato)
	{
		boolean resultado=false;
		try
		{
			String consulta = "SELECT imprimir_detalle as imprimirdetalle FROM det_sec_serv_formato_fact WHERE tipo_rompimiento=? AND codigo_formato=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, tipoRompimiento);
			pst.setInt(2, codigoFormato);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado=rs.getBoolean("imprimirdetalle");
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en getImprimirDetalleNivelRompimientoServ de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de servicios parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getImprimirSubTotalTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato)
	{
		boolean resultado=false;
		try
		{
			String consulta = "SELECT imprimir_subtotal as imprimirsubtotal FROM det_sec_serv_formato_fact WHERE tipo_rompimiento=? AND codigo_formato=? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, tipoRompimiento);
			pst.setInt(2, codigoFormato);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado=rs.getBoolean("imprimirsubtotal");
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en getImprimirSubTotalTipoRompimientoServ de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tien true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getImprimirDetalleTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato)
	{
		boolean resultado=false;
		try
		{
			String consulta = "SELECT imprimir_detalle as imprimirdetalle FROM det_art_formato_fact  WHERE tipo_rompimiento=? AND codigo_formato=? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, tipoRompimiento);
			pst.setInt(2, codigoFormato);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado=rs.getBoolean("imprimirdetalle");
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en getImprimirDetalleTipoRompimientoArt de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getImprimirSubTotalTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato)
	{
		boolean resultado=false;
		try
		{
			String consulta = "SELECT imprimir_subtotal as imprimirsubtotal FROM det_art_formato_fact WHERE tipo_rompimiento=? AND codigo_formato=? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, tipoRompimiento);
			pst.setInt(2, codigoFormato);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado=rs.getBoolean("imprimirsubtotal");
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en getImprimirSubTotalTipoRompimientoArt de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo para actualizar en la agenda el medico que atiende una cita 
	 * @param con
	 * @param codigoMedico
	 * @param codigoAgenda
	 * @return
	 */
	public static boolean actualizarMedicoAgenda(Connection con, int codigoMedico, int codigoAgenda)
	{
		boolean deboActualizar = false;
		boolean exito = true;
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexiï¿½n (SqlBaseUtilidadesDao)"+e1.toString());
		}
		
		try
		{
			//Primero se verifica si no hay mï¿½dico en la agenda
			String consulta = "SELECT count(1) as cuenta FROM agenda WHERE codigo="+codigoAgenda+" AND codigo_medico IS NULL";
			Statement st = con.createStatement(ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
					deboActualizar = true;
			}
			else
				exito = false;
			
			if(deboActualizar)
			{
			
				PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(" UPDATE agenda SET codigo_medico=? WHERE codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, codigoMedico);
				stm.setInt(2, codigoAgenda);
				if(stm.executeUpdate()>0)
					exito = true;
				else
					exito = false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando el medico de la agenda "+codigoAgenda+": "+e);
			exito =  false;
		}
		
		return exito;
	}
	
	/**
	 * Mï¿½todo para saber si un centro de costo esta asociado a un usuairo x alamacen
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static boolean centroCostoAsociadoUsuarioXAlmacen(Connection con, int centroCosto, int institucion)
	{
		ResultSetDecorator rs;
		int temp = 0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) as cantidad from usuarios_x_almacen_inv WHERE centros_costo=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroCosto);
			pst.setInt(2, institucion);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp = rs.getInt("cantidad");
			}
			if(temp > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error centroCostoAsociadoUsuarioXAlmacen : [SqlBaseUtilidadesDao] : "+e);
			return false;
		}
	}

	/**
	 * Mï¿½todo para saber si existe un centro de costo en la tabla centros_costo
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static boolean existeCentroCosto(Connection con, int centroCosto, int institucion)
	{
		ResultSetDecorator rs;
		int temp = 0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1)as cantidad FROM centros_costo WHERE codigo=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroCosto);
			pst.setInt(2, institucion);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp = rs.getInt("cantidad");
			}
			if(temp > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error existeCentroCosto : [SqlBaseUtilidadesDao] : "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo para saber si un centro de costo esta asociado a algun usuario
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static boolean centroCostoAsociadoUsuario(Connection con, int centroCosto)
	{
		ResultSetDecorator rs;
		int temp = 0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1)as cantidad FROM centros_costo_usuario WHERE centro_costo = ?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroCosto);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp = rs.getInt("cantidad");
			}
			if(temp > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error centroCostoAsociadoUsuario : [SqlBaseUtilidadesDao] : "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un centro de atenciï¿½n,
	 * de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreCentroAtencion(Connection con, int consecutivoCentroAtencion)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consultaStr="SELECT descripcion AS descripcion FROM centro_atencion " +
														"WHERE consecutivo=?";
			
			pst= con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,consecutivoCentroAtencion);
			rs=pst.executeQuery();
			if(rs.next()){
				return (rs.getString("descripcion") != null)?rs.getString("descripcion"):"";
			}
			else{
				return null;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerNombreCentroAtencion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerNombreCentroAtencion", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return null;
	}
	
	/**
	 * Mï¿½todo para obtener el nombre de un centro de costo dado su codigo para una
	 * institucion determinada
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreCentroCosto(Connection con, int centroCosto, int institucion)
	{
		try
		{
			String consultaStr=" SELECT nombre as descripcion FROM centros_costo WHERE codigo = ? AND institucion = ?";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroCosto);
			pst.setInt(2, institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("descripcion");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreCentroCosto [SqlBaseUtilidadesDao] : "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo para obtener el nombre de una via de ingreso dado su codigo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static String obtenerNombreViaIngreso(Connection con, int codigo)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consultaStr=" SELECT nombre as descripcion FROM vias_ingreso WHERE codigo = ? ";
			
			pst= con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigo);
			
			rs=pst.executeQuery();
			if(rs.next()){
				return (rs.getString("descripcion")!=null)?rs.getString("descripcion"):"";
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerNombreViaIngreso",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerNombreViaIngreso", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return "";
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoArea
	 * @return
	 */
	public static int obtenerCentroAtencionPaciente(Connection con, int codigoArea) 
	{
		try
		{
			String consultaStr=" SELECT centro_atencion  FROM centros_costo WHERE codigo = ? ";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoArea);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getInt(1);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreViaIngreso [SqlBaseUtilidadesDao] : "+e);
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCentroCostoTratante
	 * @return
	 */
	public static boolean actualizarAreaCuenta(Connection con, int codigoCuenta, int codigoCentroCostoTratante) 
	{
		try
		{
			String consultaStr=" update cuentas set area = ? where id= ? ";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoCentroCostoTratante);
			pst.setInt(2,codigoCuenta);
			
			return pst.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreViaIngreso [SqlBaseUtilidadesDao] : "+e);
		}
		return false;
	}
	
	/**
	 * Mï¿½todo que consulta los centros de costo de una vï¿½a de ingreso
	 * en un centro de atenciï¿½n especï¿½fico
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public static HashMap getCentrosCostoXViaIngresoXCAtencion(Connection con,int viaIngreso,int centroAtencion,int institucion, String tipoPaciente)
	{
		try
		{
			String consulta = getCentrosCostoXViaIngresoXCAtencionStr;
			
			
			
			consulta += " WHERE c.centro_atencion = ? and ";
			
			//Si hay via de ingreso, signfica que se busca por una vï¿½a de ingreso especï¿½fica,
			//de lo constrario quiere decir que se estï¿½ buscando por todas
			if(viaIngreso>0)
				consulta += " cv.via_ingreso = "+viaIngreso+" and ";
			
			if(!tipoPaciente.equals(""))
				consulta += " cv.tipo_paciente='"+tipoPaciente+"' and ";
			
			if(tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria) && viaIngreso == ConstantesBD.codigoViaIngresoHospitalizacion)
				consulta+=" cv.centro_costo NOT IN (SELECT ctm.centro_costo FROM tipo_monitoreo tm INNER JOIN centros_costo_x_tipo_m ctm ON (tm.codigo=ctm.tipo_monitoreo) WHERE tm.institucion="+institucion+") AND ";
			
			consulta += " c.institucion = ? and "+
			"c.codigo <> "+ConstantesBD.codigoCentroCostoTodos+" and "+
			"c.codigo <> "+ConstantesBD.codigoCentroCostoNoSeleccionado+" and "+
			"c.codigo <> "+ConstantesBD.codigoCentroCostoExternos+" and "+
			"c.tipo_area = "+ConstantesBD.codigoTipoAreaDirecto+" and "+
			"c.es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+
			" order by c.nombre";
			logger.info("\n\n\nIMPRESION DE AREAS DESDE SQL UTIL AREAAAAASSSSSSSS>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+consulta+">>>>>>>>"+centroAtencion+">>>>>>>>>>>>"+institucion+">>>>>>>><"+tipoPaciente+">>>>>>>>>>>"+viaIngreso+"\n\n\n");
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,centroAtencion);
			pst.setInt(2,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en getCentrosCostoXViaIngresoXCAtencion de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Mï¿½todo encargado de consultar los centros de costo
	 * y devolverlos en un ArrayList de HashMap con los keys
	 * codigo,nombre.
	 * @author Jhony Alexander Duque A.
	 * Se puede filtrar por los siguientes criterios:
	 * -- viaIngreso --> Opcional 
	 * -- centroAtencion --> Opcional
	 * -- institucion --> Requerido
	 * -- tipoPaciente --> Opcional
	 * -- tipoMonitoreo --> Opcional
	 * -- noTipoMonitoreo -->opcional
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @param tipoPaciente
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getCentrosCosto(Connection con,String viaIngreso,int centroAtencion,int institucion, String tipoPaciente, int tipoMonitoreo, boolean filtroTipoMonitoreo,boolean noTipoMonitoreo, boolean consultarViaIngreso)
	{
		logger.info("\n entro a getCentrosCosto ");
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		
		try
		{
			String[] vectorViaIngreso = viaIngreso.split(",");
			String consulta = consultarViaIngreso?getCentrosCostoXViaIngresoXCAtencionXtipoMonitoreoStr_2:getCentrosCostoXViaIngresoXCAtencionXtipoMonitoreoStr_1;
			
			if(filtroTipoMonitoreo)
				{
					consulta+=" INNER JOIN centros_costo_x_tipo_m tm ON (cv.centro_costo=tm.centro_costo) ";
				}
			else{
					consulta+=" LEFT OUTER JOIN centros_costo_x_tipo_m tm ON (cv.centro_costo=tm.centro_costo) ";
				}
			
			
			consulta += " WHERE ";
			if (centroAtencion>0)
				consulta+=" c.centro_atencion = "+centroAtencion+" and ";
			if (tipoMonitoreo>0 && filtroTipoMonitoreo && !noTipoMonitoreo)
				consulta+=" tm.tipo_monitoreo = "+tipoMonitoreo+" and ";
			
			if (noTipoMonitoreo)
				consulta+=" c.codigo not in (select centro_costo from centros_costo_x_tipo_m) and ";
			
			//Si hay via de ingreso, signfica que se busca por una vï¿½a de ingreso especï¿½fica,
			//de lo constrario quiere decir que se estï¿½ buscando por todas
			if((vectorViaIngreso.length==1&&Utilidades.convertirAEntero(vectorViaIngreso[0])>0)||vectorViaIngreso.length>1)
				consulta += " cv.via_ingreso IN ("+viaIngreso+") and ";
			
			if(!tipoPaciente.equals(""))
				consulta += " cv.tipo_paciente in ('"+tipoPaciente+"') and ";
			
			/**
			 * Se comenta porque no se le vio un uso y se necesitaba quitar para otras busquedas
			 * Atte Sebastian
			 * 
			 * if(tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria) && vectorViaIngreso.length==1&&Utilidades.convertirAEntero(vectorViaIngreso[0])==ConstantesBD.codigoViaIngresoHospitalizacion)
				consulta+= " cv.centro_costo <> tm.centro_costo and ";**/
				
			consulta += " c.institucion = ? and "+
			"c.codigo <> "+ConstantesBD.codigoCentroCostoTodos+" and "+
			"c.codigo <> "+ConstantesBD.codigoCentroCostoNoSeleccionado+" and "+
			"c.codigo <> "+ConstantesBD.codigoCentroCostoExternos+" and "+
			"c.tipo_area = "+ConstantesBD.codigoTipoAreaDirecto+" and "+
			"c.es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+
			" order by c.nombre";
			
			logger.info("\n cadena -->"+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap elemento = new HashMap ();
				elemento.put("codigo", rs.getObject(1));
				elemento.put("nombre", rs.getObject(2));
				elemento.put("nombreViaIngreso", rs.getObject(3));
				elemento.put("nombreTipoPaciente", rs.getObject(4));
				result.add(elemento);
			}
				
			
			
			return result;
		}
		catch(SQLException e)
		{
			logger.error("Error en getCentrosCostoXViaIngresoXCAtencion de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	
	
	
	
	
	
	
	

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerLLaveTraigeAdmisionUrgeciasXCuenta(Connection con, int idCuenta) 
	{
		String respuesta=ConstantesBD.codigoNuncaValido+ConstantesBD.separadorSplit+ConstantesBD.codigoNuncaValido;
		try
		{
			String consultaStr="SELECT consecutivo_triage,consecutivo_triage_fecha from admisiones_urgencias where cuenta=? and consecutivo_triage is not null and consecutivo_triage_fecha is not null ";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				respuesta=rs.getString(1)+ConstantesBD.separadorSplit+rs.getString(2);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerLLaveTraigeAdmisionUrgeciasXCuenta [SqlBaseUtilidadesDao] : "+e);
		}
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @param numOrdenDieta
	 * @param codigoArticulo
	 * @param cantidad
	 * @return
	 */
	public static boolean actualizarCantidadArticuloSolMedicamentos(Connection con, String numSolicitud, String codigoArticulo, String cantidad) 
	{
		try
		{
			String consultaStr=" update detalle_solicitudes SET cantidad = ? where numero_solicitud =? and articulo=?";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, cantidad);
			pst.setString(2,numSolicitud);
			pst.setString(3,codigoArticulo);
			//return pst.executeUpdate()>0;
			
			if(pst.executeUpdate()>0){
				return true;
			}
			else{	// Si no pudo actualizar el registro debe insertar uno nuevo
				String consultaInsert="INSERT INTO detalle_solicitudes(numero_solicitud, articulo, cantidad) " + "values (?,?,?)";
				PreparedStatementDecorator ps;
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaInsert));
				ps.setInt(1, Integer.parseInt(numSolicitud));
				ps.setInt(2, Integer.parseInt(codigoArticulo));
				ps.setInt(3, Integer.parseInt(cantidad));
				Log4JManager.info("insert: "+consultaInsert);
				return ps.executeUpdate()>0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarCantidadArticuloSolMedicamentos [SqlBaseUtilidadesDao] : "+e);
		}
		return false;
	}

	public static boolean getOrdenDietaFinaliza(Connection con, String codigoOrdenDieta, int codigoCuenta, int codigoCuentaAsocio)
	{
		String cue = "";
		
		if (codigoCuentaAsocio!=0)
			cue = " IN (" + codigoCuenta + "," + codigoCuentaAsocio + ")";
		else
			cue = " = " + codigoCuenta;
				
		
		String consulta = "  SELECT COUNT(1) as  nroRegistros																				" +
						  "			FROM orden_dieta od 																	" +
						  "		 		INNER JOIN encabezado_histo_orden_m ehom ON ( od.codigo_histo_enca = ehom.codigo )	" +
						  "	 	 		INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )   				" +		
						  "	   	 			 WHERE om.cuenta " + cue +
						  "					   AND od.codigo_histo_enca > " + codigoOrdenDieta + 
						  "					   AND od.via_parenteral = " + ValoresPorDefecto.getValorTrueParaConsultas(); 
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if ( rs.next() )
			{	
				 if  ( rs.getInt("nroRegistros") > 0  )
				 {
						rs.close();
						return true; 
	 			 }
				 else
				 {
					rs.close();
					return false; 
	 			 }
			}
			else
			{
				rs.close();
				return false; 
			}
		}
	    catch (SQLException e) 
	    {            
	        return false;
	    }
    }
	
	/**
	 * Mï¿½todo para obtener el valor inicial de la cuenta de cobro
	 * @param con
	 * @param codigoCuentaCobro
	 * @param codigoInstitucion
	 * @return
	 */
	public static  double obtenerValorInicialCuentaCobroCapitacion(Connection con, int codigoCuentaCobro, int codigoInstitucion)
	{
    	ResultSetDecorator rs;
    	try
		{
    		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerValorInicialCuentaCobroCapitacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, codigoCuentaCobro);
    		pst.setInt(2, codigoInstitucion);
    		
    		
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{	
    			if(rs.getString("valor_inicial")!=null&&!rs.getString("valor_inicial").equals(""))
				{
    				return rs.getDouble("valor_inicial");
				}
    			else
    			{
    				return -1;
    			}
    		}
    		else
    		{
    			return -1;
    		}
			
		}
    	catch(SQLException e)
		{
    		logger.error("Error en obtenerValorInicialCuentaCobroCapitacion de SqlBaseUtilidadesDao: "+e);
    		return -1;
		}
	}
	
	/**
	 * Mï¿½etodo apra saber si como tiene parametrizado un formato de impresion 
	 * determinado el campo que indica si se deben mostrar los servicios qx o no 
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public static boolean getMostrarServiciosQx(Connection con, int codigoFormato)
	{
    	ResultSetDecorator rs;
    	String temp = "false";
    	boolean mostrarQx = false; 
    	try
		{
    		String consulta = " SELECT case when mostrar_det_serv_qx ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'si' else 'no' end as mostrarqx " +
    						  " from sec_serv_formato_imp_fact " +
    						  " where codigo_formato = ? ";
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		pst.setInt(1, codigoFormato);
    		
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{	
    			temp =rs.getString("mostrarqx");
    			if(temp.equals("si"))
    			{
    				mostrarQx =  true ;
    			}
    			else
    			{
    				mostrarQx = false;
    			}
				
    		}
    		return mostrarQx;
			
		}
    	catch(SQLException e)
		{
    		logger.error("Error en getMostrarServiciosQx [SqlBaseUtilidadesDao] : "+e);
    		return mostrarQx;
		}
	}
	
	/**
	 * Utilidad para obtener los datos de los campos parametrizables
	 * @param con
	 * @param medico
	 * @param funcionalidad
	 * @param seccion
	 * @param centroCosto
	 * @param institucion
	 * @param codigoTabla
	 * @return
	 */
	public static HashMap<Object, Object> obtenerInformacionParametrizada(Connection con, int medico, String funcionalidad, String seccion, int centroCosto, String institucion, int codigoTabla)
	{
		String consulta="	SELECT" +
						"		pa.nombre AS nombre," +
						"		ip.valor AS valor," +
						"		pa.tipo AS tipo" +
						"	FROM info_parametrizada ip" +
						"	INNER JOIN param_asociadas pa ON(ip.parametrizacion_asociada=pa.codigo)" +
						"	INNER JOIN seccion_parametriza sp ON(pa.seccion=sp.codigo)" +
						"	WHERE" +
						"		pa.codigo_medico="+medico +
						"	AND" +
						"		pa.activo="+ValoresPorDefecto.getValorTrueParaConsultas() +
						"	AND" +
						"		sp.funcionalidad="+funcionalidad +
						"	AND" +
						"		pa.alcance_campo="+ConstantesBD.codigoAlcanceMedico +
						"	AND" +
						"		ip.codigo_tabla="+codigoTabla +
						"	AND" +
						"		pa.seccion="+seccion +
						"UNION ALL" +
						"	SELECT" +
						"		pa.nombre AS nombre," +
						"		ip.valor AS valor," +
						"		pa.tipo AS tipo" +
						"	FROM info_parametrizada ip" +
						"	INNER JOIN param_asociadas pa ON(ip.parametrizacion_asociada=pa.codigo)" +
						"	INNER JOIN seccion_parametriza sp ON(pa.seccion=sp.codigo)" +
						"	WHERE" +
						"		pa.centro_costo="+centroCosto +
						"	AND" +
						"		pa.activo="+ValoresPorDefecto.getValorTrueParaConsultas() +
						"	AND" +
						"		sp.funcionalidad="+funcionalidad +
						"	AND" +
						"		pa.alcance_campo="+ConstantesBD.codigoAlcanceCentroCosto +
						"	AND" +
						"		ip.codigo_tabla="+codigoTabla +
						"	AND" +
						"		pa.seccion="+seccion +
						"UNION ALL " +
						"	SELECT" +
						"		pa.nombre AS nombre," +
						"		ip.valor AS valor," +
						"		pa.tipo AS tipo" +
						"	FROM info_parametrizada ip" +
						"	INNER JOIN param_asociadas pa ON(ip.parametrizacion_asociada=pa.codigo)" +
						"	INNER JOIN seccion_parametriza sp ON(pa.seccion=sp.codigo)" +
						"	WHERE" +
						"		pa.institucion="+institucion +
						"	AND" +
						"		pa.activo="+ValoresPorDefecto.getValorTrueParaConsultas() +
						"	AND" +
						"		sp.funcionalidad="+funcionalidad +
						"	AND" +
						"		pa.alcance_campo="+ConstantesBD.codigoAlcanceInstitucion +
						"	AND" +
						"		ip.codigo_tabla="+codigoTabla+"" +
						"	AND" +
						"		pa.seccion="+seccion;
		
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap<Object, Object> cargarValueObject = UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			return cargarValueObject;
		}
		catch (SQLException e)
		{
			logger.error("Problemas obteniendo campos parametrizables "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el nï¿½mero de la ï¿½ltima cuenta de cobro
	 * de capitaciï¿½n
	 * @param con -> Connection
	 * @param codigoInstitucion
	 * @return ultimaCuentaCobro
	 */
	public static int obtenerUltimaCuentaCobroCapitacion(Connection con, int codigoInstitucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerUltimaCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoInstitucion);

			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("ultima_cuenta_cobro");
			}
			else
			{
				return -1;
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUltimaCuentaCobroCapitacion de SqlBaseUtilidadesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el codigo del registro de enfermeria para una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCodigoRegistroEnfermeria(Connection con, int idCuenta)
	{
		try
		{
			String consultaStr = "SELECT re.codigo as codigo FROM registro_enfermeria re WHERE re.cuenta = ? ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("codigo");
			}
			else
			{
				return -1;
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoRegistroEnfermeria [SqlBaseUtilidadesDao]: "+e);
			return -1;
		}
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un tipo de calificaciï¿½n
	 * pyp de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreTipoCalificacionPyP(Connection con, int consecutivo)
	{
		try
		{
			String consultaStr="SELECT descripcion AS descripcion FROM pyp.tipo_calificacion_pyp " +
														"WHERE consecutivo=?";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivo);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("descripcion");
			else
				return null;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoCalificacionPyP de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un tipo de tipo de transacciï¿½n de inventario
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreTransaccionInventario(Connection con,int consecutivo)
	{
		try
		{
			String consultaStr="SELECT descripcion AS descripcion FROM tipos_trans_inventarios WHERE consecutivo=?";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivo);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("descripcion");
			else
				return null;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTransaccionInventario de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar el nombre de una ocupaciï¿½n mï¿½dica
	 * @param con
	 * @param codigoOcupacion
	 * @return
	 */
	public static String obtenerNombreOcupacionMedica(Connection con,int codigoOcupacion)
	{
		try
		{
			String consultaStr = "SELECT nombre as nombre FROM ocupaciones_medicas WHERE codigo = ? ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoOcupacion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("nombre");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreOcupacionMedica de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoSubCuenta
	 * @param totalSubCuenta
	 * @return
	 */
	public static boolean actualizarTotalSubCuenta(Connection con, int codigoSubCuenta, double totalSubCuenta) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE sub_cuentas SET total = ? where sub_cuenta=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,totalSubCuenta);
			ps.setInt(2,codigoSubCuenta);
			return ps.executeUpdate()>0;
		}
		catch(SQLException e){
			logger.error("Error en Utilidades.actualizarTotalSubCuenta: "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public static String obtenerTipoServicio(Connection con, String servicio)
	{
		try
		{
			String consultaStr = "SELECT tipo_servicio as tiposervicio FROM servicios WHERE codigo = ? ";
			int codigoServicio = Integer.parseInt(servicio);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoServicio);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("tiposervicio");
			else
				return "";
		} catch(SQLException e)	{
			logger.error("Error en obtenerTipoServicio de SqlBaseUtilidadesDao : "+e);
			return "";
		} catch(Exception e)	{
			logger.error("Error en obtenerTipoServicio de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifario
	 * @return
	 */
	public static String obtenerCodigoPropietarioServicio(Connection con, String servicio, int codigoTarifario) 
	{
		try
		{
			String consultaStr = "SELECT coalesce(codigo_propietario,'') as codpro FROM referencias_servicio WHERE servicio ="+servicio+" and tipo_tarifario="+codigoTarifario;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("codpro");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoPropietarioServicio de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerEstadoHistoriaClinicaSolicitud(Connection con, String numeroSolicitud) {
		try
		{
			String consultaStr = "SELECT estado_historia_clinica as estado from solicitudes where numero_solicitud ="+numeroSolicitud;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("estado");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstadoHistoriaClinicaSolicitud de SqlBaseUtilidadesDao : "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerTipoSolicitud(Connection con, String numeroSolicitud) {
		try
		{
			String consultaStr = "SELECT tipo as tipo from solicitudes where numero_solicitud ="+numeroSolicitud;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("tipo");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoSolicitud de SqlBaseUtilidadesDao : "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}

	/**
	 * 
	 * @param con
	 * @param grEtareo
	 * @return
	 */
	public static String[] obtenerRango_UnidadMedidaGrupoEtareoPYP(Connection con, String grEtareo) 
	{

		String[] resp={"0","0","0"};
		try
		{
			String consultaStr ="SELECT rango_inicial,rango_final ,unidad_medida from grup_etareo_creci_desa where codigo="+grEtareo;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp[0]=rs.getString(1);
				resp[1]=rs.getString(2);
				resp[2]=rs.getString(3);
			}
		}
		catch(SQLException e)
		{ 
			logger.error("Error en obtenerTipoSolicitud de SqlBaseUtilidadesDao : "+e);
		}
		return resp;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudPYP(Connection con, int numeroSolicitud) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		try
		{
			logger.info("############## Inicio esSolicitudPYP");
			String consultaStr = "SELECT case when pyp is null or pyp ="+ValoresPorDefecto.getValorFalseParaConsultas()+" then 'false' else 'true' end as pyp from solicitudes where numero_solicitud="+numeroSolicitud;
			pst =  con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			if(rs.next())
				resultado = UtilidadTexto.getBoolean(rs.getString("pyp"));
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esSolicitudPYP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esSolicitudPYP", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin esSolicitudPYP");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param numeroOrden
	 * @param institucion
	 * @return
	 */
	public static String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroOrden, int institucion) 
	{
		try
		{
			String consultaStr = "SELECT codigo as codigo from ordenes_ambulatorias where consecutivo_orden = ? and institucion = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroOrden);
			pst.setInt(2,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("codigo");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoOrdenAmbulatoria de SqlBaseUtilidadesDao : "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}

	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean asignarSolicitudToActividadPYP(Connection con, String codigoOrden, String numeroSolicitud) 
	{
		try
		{
			String consultaStr = "UPDATE act_prog_pyp_pac set numero_solicitud = ? where numero_orden=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			pst.setString(2,codigoOrden);
			return pst.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoOrdenAmbulatoria de SqlBaseUtilidadesDao : "+e);
		}
		return false;
	}
	
	/**
	 * M+etodo que verifica si el convenio de una solicitud tiene
	 * activo el campo de unificar PYP
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean tieneConvenioUnificarPYP(Connection con,String numeroSolicitud)
	{
		try
		{
			boolean resultado = false;
			String consulta = "SELECT conv.unificar_pyp AS unificar FROM solicitudes sol " +
				"INNER JOIN cuentas c ON(c.id=sol.cuenta) " +
				"INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso AND sc.nro_prioridad = 1)"+ 
				"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) "+ 
				"WHERE numero_solicitud = "+numeroSolicitud;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resultado = rs.getBoolean("unificar");
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneConvenioUnificarPYP de SQlBaseUtilidadesDao: "+e);
			return false;
		}
	}

	/**
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public static HashMap consultarDetalleOrdenAmbulatoriaArticulos(Connection con, String ordenAmbulatoria) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena="SELECT " +
						" doar.articulo as articulo," +
						" doar.dosis as dosis," +
						" doar.via as via," +
						" vad.nombre as nomvia," +
						" doar.observaciones as observaciones," +
						" doar.frecuencia as frecuencia," +
						" tf.nombre as tipofrecuencia," +
						" doar.cantidad as cantidad ," +
						" doar.medicamento as medicamento," +
						" va.es_pos as espos, " +
						" doar.duracion_tratamiento as duraciontratamiento," +
						" doar.unidosis_articulo as unidosis "+
					" from det_orden_amb_articulo doar " +
					" inner join view_articulos va on(doar.articulo=va.codigo) " +
					" left outer join vias_administracion vad on (doar.via=vad.codigo) " +
					" left outer join tipos_frecuencia tf on(tf.codigo=doar.tipo_frecuencia) " +
					"where doar.codigo_orden=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("-->"+cadena+"---"+ordenAmbulatoria);
			ps.setString(1,ordenAmbulatoria);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDetalleOrdenAmbulatoriaArticulos de SqlBaseUtilidadesDao : "+e);
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo implementado para actualizar el acumulado de PYP por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @param secuencia
	 * @return
	 */
	public static int actualizarAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion,String secuencia)
	{
		logger.info("\n entro a actualizarAcumuladoPYP solicitud -->"+numeroSolicitud+" centroate -->"+centroAtencion);
		try
		{
			String codigoConvenio = "", tipoRegimen = "",codigoPrograma="",codigoActividad="",institucion="";
			String consecutivoAcumulado = "",consulta="",tipoSol="";
			int resp = 0;
			//1) Consulta de los datos del paciente***************************************************************
			
			
			consulta=" SELECT tipo from solicitudes WHERE numero_solicitud="+numeroSolicitud;
			logger.info("la cinsulta -->"+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rst =new ResultSetDecorator(ps.executeQuery());
			if(rst.next())
				tipoSol = rst.getString("tipo");
			logger.info(" tipoSol -->"+tipoSol);
			if (!tipoSol.equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
			{
				//Se consulta el convenio pyp asociado a la solicitud
				consulta = "SELECT "+ 
					"conv.codigo As codigo_convenio, "+
					"conv.tipo_regimen As tipo_regimen "+ 
					"FROM solicitudes s " +
					"INNER JOIN solicitudes_subcuenta ss ON(ss.solicitud=s.numero_solicitud) " +
					"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=ss.sub_cuenta) "+
					"INNER JOIN convenios conv ON(conv.codigo=sc.convenio AND conv.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
					"WHERE s.numero_solicitud = ? AND ss.eliminado='"+ConstantesBD.acronimoNo+"' ";
				
			}
			else
			{
//				Se consulta el convenio pyp asociado a la solicitud
				consulta = "SELECT "+ 
					"conv.codigo As codigo_convenio, "+
					"conv.tipo_regimen As tipo_regimen "+ 
					"FROM solicitudes s " +
					"INNER JOIN solicitudes_cirugia scx ON(scx.numero_solicitud=s.numero_solicitud) " +
					"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=scx.sub_cuenta) "+
					"INNER JOIN convenios conv ON(conv.codigo=sc.convenio AND conv.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
					"WHERE s.numero_solicitud = ? ";
			}
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				logger.info("\n**********1********** ");
				codigoConvenio = rs.getString("codigo_convenio");
				tipoRegimen = rs.getString("tipo_regimen");
				
				//2) Se consultan los datos de la actividad
				consulta = "SELECT "+ 
					"a.actividad AS codigo_actividad, "+
					"p.programa AS codigo_programa, "+
					"p.institucion As institucion "+ 
					"FROM act_prog_pyp_pac a "+ 
					"INNER JOIN programas_pyp_paciente p ON(p.codigo=a.codigo_prog_pyp_pac) "+ 
					"WHERE "+ 
					"a.numero_solicitud = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setObject(1,numeroSolicitud);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					logger.info("\n**********2********** ");
					codigoActividad = rs.getString("codigo_actividad");
					codigoPrograma = rs.getString("codigo_programa");
					institucion = rs.getString("institucion");
					
					//3) Se verifica si ya existe un registro para la actividad acumulada
					consulta = "SELECT codigo " +
						"FROM acumulado_act_prog_pac_pyp " +
						"WHERE " +
						"centro_atencion = ? and " +
						"regimen = ? and " +
						"programa = ? and " +
						"actividad = ? AND " +
						"convenio = ? and " +
						"institucion = ? and " +
						"fecha = CURRENT_DATE ";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setObject(1,centroAtencion);
					pst.setObject(2,tipoRegimen);
					pst.setObject(3,codigoPrograma);
					pst.setObject(4,codigoActividad);
					pst.setObject(5,codigoConvenio);
					pst.setObject(6,institucion);
					rs = new ResultSetDecorator(pst.executeQuery());
					
					if(rs.next())
					{
						logger.info("\n**********3********** ");
						//4) Se realiza la actualizacion del registro
						consecutivoAcumulado = rs.getString("codigo");
						consulta = "UPDATE acumulado_act_prog_pac_pyp SET cantidad = cantidad +1 WHERE codigo = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setObject(1,consecutivoAcumulado);
						resp = pst.executeUpdate();
					}
					else
					{
						logger.info("\n**********4********** ");
						//5) Se ingresa un registro nuevo
						consulta =  "INSERT INTO acumulado_act_prog_pac_pyp " +
						"(codigo,centro_atencion,regimen,programa,actividad,convenio,fecha,cantidad,institucion) "+
						 " VALUES ("+secuencia+",?,?,?,?,?,CURRENT_DATE,1,?)";
						
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setObject(1,centroAtencion);
						pst.setObject(2,tipoRegimen);
						pst.setObject(3,codigoPrograma);
						pst.setObject(4,codigoActividad);
						pst.setObject(5,codigoConvenio);
						pst.setObject(6,institucion);
						
						resp = pst.executeUpdate();
						
					}
				}
				
				logger.info("\n**********5********** ");
			}
			logger.info("\n**********sali**********> "+resp);
			return resp;
				
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarAcumuladoPYP de SQlBaseUtilidadesDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo implementado para disminuir el acumulado de PYP por solicitud 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @param secuencia
	 * @param fechaSalPac --> dd/mm/yyyy
	 * @return
	 */
	public static int disminuirAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion,String secuencia, String fechaSalPac)
	{
		logger.info("\n entro a disminuirAcumuladoPYP solicitud -->"+numeroSolicitud+" centroate -->"+centroAtencion);
		try
		{
			String codigoConvenio = "", tipoRegimen = "",codigoPrograma="",codigoActividad="",institucion="";
			String consecutivoAcumulado = "", consulta="",tipoSol="";
			int resp = 0;
			//1) Consulta de los datos del paciente***************************************************************
			
			consulta=" SELECT tipo from solicitudes WHERE numero_solicitud="+numeroSolicitud;
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rst =new ResultSetDecorator(ps.executeQuery());
			if(rst.next())
				tipoSol = rst.getString("tipo");
			
			if (!tipoSol.equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
			{
				//Se consulta el convenio pyp asociado a la solicitud
				consulta = "SELECT "+ 
					"conv.codigo As codigo_convenio, "+
					"conv.tipo_regimen As tipo_regimen "+ 
					"FROM solicitudes s " +
					"INNER JOIN solicitudes_subcuenta ss ON(ss.solicitud=s.numero_solicitud) " +
					"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=ss.sub_cuenta) "+
					"INNER JOIN convenios conv ON(conv.codigo=sc.convenio AND conv.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
					"WHERE s.numero_solicitud = ? AND ss.eliminado='"+ConstantesBD.acronimoNo+"' ";
				
			}
			else
			{
//				Se consulta el convenio pyp asociado a la solicitud
				consulta = "SELECT "+ 
					"conv.codigo As codigo_convenio, "+
					"conv.tipo_regimen As tipo_regimen "+ 
					"FROM solicitudes s " +
					"INNER JOIN solicitudes_cirugia scx ON(scx.numero_solicitud=s.numero_solicitud) " +
					"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=scx.sub_cuenta) "+
					"INNER JOIN convenios conv ON(conv.codigo=sc.convenio AND conv.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
					"WHERE s.numero_solicitud = ? ";
			}
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			
			
			if(rs.next())
			{
				codigoConvenio = rs.getString("codigo_convenio");
				tipoRegimen = rs.getString("tipo_regimen");
				
				//2) Se consultan los datos de la actividad
				consulta = "SELECT "+ 
					"a.actividad AS codigo_actividad, "+
					"p.programa AS codigo_programa, "+
					"p.institucion As institucion "+ 
					"FROM act_prog_pyp_pac a "+ 
					"INNER JOIN programas_pyp_paciente p ON(p.codigo=a.codigo_prog_pyp_pac) "+ 
					"WHERE "+ 
					"a.numero_solicitud = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setObject(1,numeroSolicitud);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					codigoActividad = rs.getString("codigo_actividad");
					codigoPrograma = rs.getString("codigo_programa");
					institucion = rs.getString("institucion");
					
					logger.info("centro atencion "+centroAtencion);
					logger.info("tipoRegimen  "+tipoRegimen);
					logger.info("codigoPrograma "+codigoPrograma);
					logger.info("codigoActividad "+codigoActividad);
					logger.info("codigoConvenio "+codigoConvenio);
					logger.info("institucion "+institucion);
					logger.info("fecha "+UtilidadFecha.conversionFormatoFechaABD(fechaSalPac));
					
					//3) Se verifica si ya existe un registro para la actividad acumulada
					consulta = "SELECT codigo " +
						"FROM acumulado_act_prog_pac_pyp " +
						"WHERE " +
						"centro_atencion = ? and " +
						"regimen = ? and " +
						"programa = ? and " +
						"actividad = ? AND " +
						"convenio = ? and " +
						"institucion = ? and " +
						"fecha = '"+UtilidadFecha.conversionFormatoFechaABD(fechaSalPac)+"'";
					
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setObject(1,centroAtencion);
					pst.setObject(2,tipoRegimen);
					pst.setObject(3,codigoPrograma);
					pst.setObject(4,codigoActividad);
					pst.setObject(5,codigoConvenio);
					pst.setObject(6,institucion);
					rs = new ResultSetDecorator(pst.executeQuery());
					
					if(rs.next())
					{	
						logger.info("***************1********");
						//4) Se realiza la actualizacion del registro
						consecutivoAcumulado = rs.getString("codigo");
						consulta = "UPDATE acumulado_act_prog_pac_pyp SET cantidad = cantidad -1 WHERE codigo = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setObject(1,consecutivoAcumulado);
						resp = pst.executeUpdate();
					}
					
				}
				
			}
			return resp;
				
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarAcumuladoPYP de SQlBaseUtilidadesDao: "+e);
			return 0;
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(Connection con, int numeroSolicitud) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			
			String consultaStr = "SELECT codigo as codigo from act_prog_pyp_pac where  numero_solicitud =  ?";
			pst =  con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,numeroSolicitud);
			rs = pst.executeQuery();
			if(rs.next()){
				return (rs.getString("codigo")!=null)?rs.getString("codigo"):"";
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCodigoActividadProgramaPypPacienteDadaSolicitud",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigoActividadProgramaPypPacienteDadaSolicitud", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido+"";
	}

	/**
	 * 
	 * @param con
	 * @param codigoActividad
	 * @param estado
	 * @param loginUsuario
	 * @param comentario
	 * @return
	 */
	public static boolean actualizarEstadoActividadProgramaPypPaciente(Connection con, String codigoActividad, String estado, String loginUsuario, String comentario) 
	{
		try
		{
			if(estado==ConstantesBD.codigoEstadoProgramaPYPSolicitado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_solicitar=?,fecha_solicitar=current_date,hora_solicitar=? where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,estado+"");
				ps.setString(2,loginUsuario+"");
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setString(4,codigoActividad+"");
				return ps.executeUpdate()>0;
			}
			else if(estado==ConstantesBD.codigoEstadoProgramaPYPCancelado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_cancelar=?,fecha_cancelar=current_date,hora_cancelar=?,motivo_cancelacion=? where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,estado+"");
				ps.setString(2,loginUsuario+"");
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setString(4,comentario+"");
				ps.setString(5,codigoActividad+"");
				return ps.executeUpdate()>0;
			}
			else if(estado==ConstantesBD.codigoEstadoProgramaPYPEjecutado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_ejecutar=?,fecha_ejecutar=current_date,hora_ejecutar=? where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,estado+"");
				ps.setString(2,loginUsuario+"");
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setString(4,codigoActividad+"");
				return ps.executeUpdate()>0;
			}
			else if(estado==ConstantesBD.codigoEstadoProgramaPYPProgramado)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("UPDATE act_prog_pyp_pac set estado = ?,usuario_programar=?,fecha_programar=current_date,hora_programar=?,usuario_ejecutar=null,fecha_ejecutar=null,hora_ejecutar=null,usuario_cancelar=null,fecha_cancelar=null,hora_cancelar=null where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,estado+"");
				ps.setString(2,loginUsuario+"");
				ps.setString(3,UtilidadFecha.getHoraActual());
				ps.setString(4,codigoActividad+"");
				return ps.executeUpdate()>0;
			}
			
		} 
		catch (SQLException e) 
		{
			logger.error("error actualizarEstadoActividadProgramaPYPPAcienteNumOrden - [SqlBaseOrdenesAmbulatoriasDao] ");
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoOrdenAmbulatoriaActividad(Connection con, String numeroSolicitud) 
	{
		try
		{
			String consultaStr = "SELECT numero_orden as orden from act_prog_pyp_pac where  numero_solicitud =  ? and numero_orden is not null";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("orden");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoOrdenAmbulatoria de SqlBaseUtilidadesDao : "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroSolicitud) 
	{
		try
		{
			String consultaStr = "SELECT orden as codigo from ordenes_amb_solicitudes where numero_solicitud=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("codigo");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoOrdenAmbulatoria de SqlBaseUtilidadesDao : "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	
	/**
	 * Mï¿½todo que verifica si en Actividades por Programa estï¿½
	 * definida como requerida y estï¿½ activo
	 * @param con
	 * @param codigoActividadProgPypPac
	 * @return el codigo de la actividad programa pyp si estï¿½ requerido sino retorna vacï¿½o
	 */
	public static String esRequeridoActividadesXPrograma(Connection con, String codigoActividadProgPypPac)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consultaStr ="select app.codigo as codigo from act_prog_pyp_pac appp inner join programas_pyp_paciente ppp on (appp.codigo_prog_pyp_pac=ppp.codigo) inner join actividades_programa_pyp app on (app.institucion=ppp.institucion and app.programa=ppp.programa and app.actividad=appp.actividad) where appp.codigo=? and app.requerido="+ValoresPorDefecto.getValorTrueParaConsultas();
			pst = con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,codigoActividadProgPypPac);
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				return rs.getString("codigo");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esRequeridoActividadesXPrograma",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esRequeridoActividadesXPrograma", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return "";
	}
	
	/**
	 * Mï¿½todo que consulta si existen programas y actividades por convenio, y
	 * verifica si estï¿½n activas 
	 * @param con
	 * @param actividadProgramaPyp
	 * @param convenio 
	 * @return true si hay actividad(es) en true para la actividad de programa pyp
	 */
	public static boolean activaProgramaActividadesConvenio (Connection con, String actividadProgramaPyp, String convenio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consultaStr = "SELECT count(1) AS num FROM prog_act_pyp_convenio" +
												  "			WHERE actividad_programa_pyp = ? AND convenio="+convenio+" AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
			
			pst =  con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,actividadProgramaPyp);
			
			rs = pst.executeQuery();
			if(rs.next()){
				if(Integer.parseInt(rs.getString("num"))>=1){
					return true;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR activaProgramaActividadesConvenio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR activaProgramaActividadesConvenio", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param grEtareo
	 * @return
	 */
	public static String obtenerSexoGrupoEtareoPYP(Connection con, String grEtareo) 
	{
		try
		{
			String consultaStr ="SELECT case when sexo is null then "+ConstantesBD.codigoSexoAmbos+" else sexo end from grup_etareo_creci_desa where codigo="+grEtareo;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch(SQLException e)
		{ 
			logger.error("Error en obtenerTipoSolicitud de obtenerSexoGrupoEtareoPYP : "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de una cuenta contable
	 * de acuerdo al codigo enviado como parï¿½metro
	 * @param con
	 * @param codigo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreCuentaContable(Connection con,int codigo)
	{
		try
		{
			String consultaStr="SELECT descripcion AS descripcion FROM cuentas_contables " +
														"WHERE codigo=?";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigo);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("descripcion");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreCuentaContable de SqlBaseUtilidadesDao: "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo que obtiene el nombre de la finalidad del servicio dado el codigo de la finalidad
	 * @param con
	 * @param codigoFinalidad
	 * @return
	 */
	public static String getNombreFinalidadServicio(Connection con, String codigoFinalidad)
	{
		try
		{
			String consulta= "SELECT nombre as nombre FROM finalidades_servicio WHERE codigo=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoFinalidad);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("nombre");
		}
		catch (SQLException e) 
		{
			logger.error("Error en getNombreFinalidadServicio de SqlBaseUtilidadesDao: "+e);
		}
		return "";
	}
	
	/**
	 * Mï¿½todo que obtiene tipo del servicio dado el codigo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static String getTipoServicio(Connection con, String codigoServicio)
	{
		try
		{
			String consulta= "SELECT tipo_servicio as tipo_servicio FROM servicios WHERE codigo=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoServicio);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("tipo_servicio");
		}
		catch (SQLException e) 
		{
			logger.error("Error en getTipoServicio de SqlBaseUtilidadesDao: "+e);
		}
		return "";
	}

	
	/**
	 * 
	 * @param con 
	 * @param codigoConvenio
	 * @return
	 */
	public static double obtenerPorcentajePypContratoConvenio(Connection con, String codigoConvenio)
	{
		try
		{
			String consulta= " SELECT case when porcentaje_pyp is null then -1 else porcentaje_pyp end from contratos where convenio =?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getDouble(1);
		}
		catch (SQLException e) 
		{
			logger.error("Error en obtenerPorcentajePypContratoConvenio de SqlBaseUtilidadesDao: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerFrecuenciaTipoFrecSolPoc(Connection con, int numeroSolicitud) 
	{
		try
		{
			String consulta= " SELECT frecuencia,tipo_frecuencia from sol_procedimientos where numero_solicitud =?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				
				return rs.getString(1)+ConstantesBD.separadorSplit+rs.getString(2);
		}
		catch (SQLException e) 
		{
			logger.error("Error en obtenerPorcentajePypContratoConvenio de SqlBaseUtilidadesDao: "+e);
		}
		return "-1"+ConstantesBD.separadorSplit+"-1";
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerViaIngresoFactura(Connection con, String codigoFactura) {
		try
		{
			String consulta= "SELECT case when via_ingreso is null then -1 else via_ingreso end from facturas where codigo = ?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e) 
		{
			logger.error("Error en obtenerViaIngresoFactura de SqlBaseUtilidadesDao: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerCuentaFactura(Connection con, String codigoFactura) 
	{
		try
		{
			String consulta= "SELECT cuenta from facturas where codigo = ?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch (SQLException e) 
		{
			logger.error("Error en obtenerSubCuentaFactura de SqlBaseUtilidadesDao: "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esActividadSolicitudCubiertaConvenio(Connection con, String numeroSolicitud, String codigoPaciente, String codigoConvenio) 
	{
		try
		{
			String consultaStr= "";
			if(UtilidadValidacion.esSolicitudMedicamentos(con,numeroSolicitud))
			{
				consultaStr = "SELECT " +
										" count(1) " +
									" from act_prog_pyp_pac appp " +
									" inner join programas_pyp_paciente ppp on (appp.codigo_prog_pyp_pac=ppp.codigo) " +
									" inner join actividades_art_pyp_programa app on (app.institucion=ppp.institucion and app.programa=ppp.programa and app.articulo=appp.actividad) " +
									" where appp.numero_solicitud=? and ppp.paciente=?" ;	
			}
			else
			{
			consultaStr = "SELECT " +
										" count(1) " +
								" from act_prog_pyp_pac appp " +
								" inner join programas_pyp_paciente ppp on (appp.codigo_prog_pyp_pac=ppp.codigo) " +
								" inner join actividades_programa_pyp app on (app.institucion=ppp.institucion and app.programa=ppp.programa and app.actividad=appp.actividad) " +
								" inner join prog_act_pyp_convenio papc on(app.codigo=papc.actividad_programa_pyp) " +
								" where appp.numero_solicitud=? and ppp.paciente=? and papc.convenio="+codigoConvenio+" and papc.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			}
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			pst.setString(2,codigoPaciente);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt(1)>0)
					return true;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en esActividadSolicitudCubiertaConvenio de SqlBaseUtilidadesDao : "+e);
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String esSolicitudFacturada(Connection con, String numeroSolicitud) 
	{
		String cadena="SELECT f.codigo from facturas f inner join det_factura_solicitud dfs on (dfs.factura=f.codigo) where dfs.solicitud=?  and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada;
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido+"";	
	}
	
	/**
	 * Mï¿½todo que verifica que el paciente no tenga cuentas abiertas en otros centros de atencion
	 * y en el caso de que la encuentre retorna la descripcion de su centro de atencion asociado
	 * @param con
	 * @param codigoPaciente
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static String getCentroAtencionIngresoAbiertoPaciente(Connection con,String codigoPaciente,String institucion,String centroAtencion)
	{
		try
		{
			String centroAtencionStr = "";
			String consulta = "SELECT "+ 
				"getnomcentroatencion(cc.centro_atencion) as centro_atencion "+ 
				"FROM cuentas c " +
				"INNER JOIN ingresos ing ON(ing.id=c.id_ingreso) "+
				"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
				"WHERE "+ 
				"c.codigo_paciente = ? and ing.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and ing.institucion=? and cc.centro_atencion != ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoPaciente);
			pst.setString(2,institucion);
			pst.setString(3,centroAtencion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				centroAtencionStr = rs.getString("centro_atencion");
			
			return centroAtencionStr;
		}
		catch(SQLException e)
		{
			logger.error("Error en getCentroAtencionCuentaActivaPaciente de SqlBaseUtilidadesDao: "+e);
			return "";
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String[] obtenerCentroAtencionFactura(Connection con, int codigoFactura) 
	{
		String[] resp={"-1",""};
		try
		{
			String consulta = "SELECT centro_aten,getnomcentroatencion(centro_aten) from facturas where codigo=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp[0]=rs.getString(1);
				resp[1]=rs.getString(2);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCentroAtencionFactura de SqlBaseUtilidadesDao: "+e);
		}
		return resp;
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre del usuario dado el login y el parï¿½metro primeroApellidos
	 * para indicar como se quiere el orden primero los nombres o los apellidos
	 * @param con
	 * @param login
	 * @param primeroApellidos
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public static String obtenerNombreUsuarioXLogin(Connection con, String login, boolean primeroApellidos ,String query)
	{
		try
		{
			String consultaStr="";
			consultaStr=query;

			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, login);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("nombre_usuario");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Login Usuario : "+login);
			logger.error("Error en obtenerNombreUsuarioXLogin [SqlBaseUtilidadesDao] : "+e);
			
			return "";
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar el centro de atencion de la ultima cuenta
	 * activa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static String getNomCentroAtencionIngresoAbierto(Connection con,String codigoPaciente)
	{
		try
		{
			String centroAtencion = "";
			String consulta = "SELECT "+ 
				"getcentroatencioncc(c.area) as centro_atencion "+ 
				"FROM cuentas c " +
				"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+  
				"WHERE "+ 
				"c.codigo_paciente = ? AND "+ 
				"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
				"ORDER BY c.fecha_apertura DESC, c.hora_apertura DESC ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoPaciente);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				centroAtencion = rs.getString("centro_atencion");
			return centroAtencion;
		}
		catch(SQLException e)
		{
			logger.error("Error en getNomCentroAtencionCuentaActiva de SqlBaseUtilidadesDao: "+e);
			return "";
		}
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @return
	 */
	public static String obtenerCodigoActividadPorPrograma(Connection con, int institucion, String programa, String actividad) 
	{
		try
		{
			String codigo = "";
			String consulta = "SELECT codigo FROM actividades_programa_pyp where institucion=? and programa=? and actividad=? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			pst.setString(2,programa);
			pst.setString(3,actividad);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				codigo = rs.getString("codigo");
			return codigo;
		}
		catch(SQLException e)
		{
			logger.error("Error en getNomCentroAtencionCuentaActiva de SqlBaseUtilidadesDao: "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo que verifica si una solicitud estï¿½ asociada a una cita
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeCitaParaSolicitud(Connection con,String numeroSolicitud)
	{
		try
		{
			boolean existe = false;
			String consulta = "select count(1) As cuenta " +
				"FROM cita c " +
				"INNER JOIN servicios_cita sc ON(sc.codigo_cita=c.codigo) " +
				"WHERe sc.numero_solicitud = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeCitaParaSolicitud para SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public static String obtenerCodigoActividadDadaOrdenAmbulatoria(Connection con, String ordenAmbulatoria) 
	{
		String codigo = "-1";
		try
		{
			if(!ordenAmbulatoria.trim().equals(""))
			{
				String consulta = "SELECT actividad FROM act_prog_pyp_pac where numero_orden=? ";
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,ordenAmbulatoria);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					codigo = rs.getString(1);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getNomCentroAtencionCuentaActiva de SqlBaseUtilidadesDao: "+e);
		}
		return codigo;
	}

	public static String obtenerCodigoConvenioSolicitud(Connection con, String numeroSolicitud) 
	{
		String codigo = "-1";
		try
		{
			if(!numeroSolicitud.trim().equals(""))
			{
				String consulta = "select " +
					"con.convenio " +
					"from solicitudes s " +
					"inner join cuentas c on(s.cuenta=c.id) " +
					"inner join sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad=1) "+
					"inner join contratos con on(sc.contrato=con.codigo) where s.numero_solicitud=? ";
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,numeroSolicitud);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					codigo = rs.getString(1);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoConvenioSolicitud de SqlBaseUtilidadesDao: "+e);
		}
		return codigo;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap obtenerEspecialidades(Connection con,HashMap parametros) 
	{		
		String codigos = "";
		if(parametros.containsKey("especialidadesIngresadas") 
				&& !parametros.get("especialidadesIngresadas").toString().equals("") )
			codigos = parametros.get("especialidadesIngresadas").toString();			
			
		String cadena = "select codigo as codigo,nombre as descripcion,tipo_especialidad as tipoespecialidad from especialidades where codigo NOT IN ("+codigos+"-1) order by nombre ASC ";		
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEspecialidades de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	
	/**
	 * Obtener Especialidades Medicos
	 * @param Connection con
	 * @param String codigoMedico 
	 * @return
	 */
	public static HashMap obtenerEspecialidadesMedico(Connection con,String codigoMedico) 
	{
		try
		{
			String sql = "select em.codigo_especialidad as codigo,e.nombre as descripcion from especialidades e INNER JOIN especialidades_medicos em ON (em.codigo_especialidad = e.codigo AND em.codigo_medico = "+codigoMedico+" AND em.activa_sistema = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"') ";
			logger.info("obtenerEspecialidadesMedico / "+sql);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEspecialidadesMedico de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	public static HashMap obtenerMedicosEspecialidad(Connection con, String codigoEspecilidad) 
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement("SELECT codigo_medico as codigo,getnombrepersona(codigo_medico) as nombre from especialidades_medicos where codigo_especialidad="+codigoEspecilidad+" order by nombre ASC ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMedicosEspecialidad de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	public static HashMap obtenerUnidadesConsulta(Connection con) {
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement("SELECT codigo,descripcion from unidades_consulta",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUnidadesConsulta de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEspecialidad
	 * @return
	 */
	public static HashMap obtenerMedicosUnidadConsulta(Connection con, String codigoUnidadConsulta)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement("select es.codigo_medico as codigo, administracion.getnombremedico(es.codigo_medico) as nombre from especialidades_medicos es INNER JOIN unidades_consulta uc ON uc.especialidad=es.codigo_especialidad where uc.codigo="+codigoUnidadConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUnidadesConsulta de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoUnidadConsulta
	 * @return
	 */
	public static HashMap obtenerAgendaDisponibleMedicoUnidadConsulta(Connection con, String codigoUnidadConsulta,String codigoMedico) 
	{
		String consulta="SELECT " +
							" a.codigo as codigo," +
							" c.descripcion as consultorio," +
							" a.unidad_consulta as codigouc," +
							" getnombreunidadconsulta(a.unidad_consulta) as nombreuc," +
							" a.fecha, " +
							" a.hora_inicio," +
							" a.hora_fin," +
							" a.codigo_medico as codigomedico," +
							" getnombrepersona(a.codigo_medico) as nombremedico," +
							" a.centro_atencion as codigosede," +
							" getnomcentroatencion(a.centro_atencion) as sede " +
						"	from agenda  a " +
						"	inner join consultorios c on(c.codigo=a.consultorio) " +
						"	where a.unidad_consulta="+codigoUnidadConsulta+" ";
		if(Integer.parseInt(codigoMedico)>0)
			consulta=consulta+" and a.codigo_medico="+codigoMedico;
		
		consulta=consulta+" AND fecha > current_date and cupos > 0";
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAgendaDisponibleMedicoUnidadConsulta de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public static String obtenerNaturalezaServicio(Connection con, String servicio) 
	{
		try
		{
			String codigo = "";
			int codigoServicio = Integer.parseInt(servicio);
			String consulta = "SELECT naturaleza_servicio from servicios where codigo=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoServicio);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				codigo = rs.getString(1);
			return codigo;
		}
		catch(SQLException e) {
			logger.error("Error en obtenerNaturalezaServicio de SqlBaseUtilidadesDao: "+e);
			return ConstantesBD.codigoNuncaValido+"";
		} catch (Exception e) {
			logger.error("Error en obtenerNaturalezaServicio de SqlBaseUtilidadesDao: "+e);
			return ConstantesBD.codigoNuncaValido+"";
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param actividad
	 * @return
	 */
	public static String obtenerArtSerActividadPYP(Connection con, String actividad) 
	{
		try
		{
			String codigo = "";
			long consecutivoActividad = Long.parseLong(actividad);
			String consulta = "SELECT case when servicio is not null then servicio else articulo end from actividades_pyp where consecutivo=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,consecutivoActividad);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				codigo = rs.getString(1);
			return codigo;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerArtSerActividadPYP de SqlBaseUtilidadesDao: "+e);
			return ConstantesBD.codigoNuncaValido+"";
		}
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerCiudades(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar
			String consulta = "SELECT c.codigo_ciudad as codigociudad,c.descripcion as nombreciudad,d.codigo_departamento as codigodepartamento,d.descripcion as nombredepartamento, p.codigo_pais as codigopais,p.descripcion as nombrepais from ciudades c inner join departamentos d on (d.codigo_departamento=c.codigo_departamento) inner join paises p on (p.codigo_pais=c.codigo_pais) order by c.descripcion desc ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoCiudad",rs.getObject("codigociudad"));
				mapa.put("nombreCiudad",rs.getObject("nombreciudad"));
				mapa.put("codigoDepartamento",rs.getObject("codigodepartamento"));
				mapa.put("nombreDepartamento",rs.getObject("nombredepartamento"));
				mapa.put("codigoPais",rs.getObject("codigopais"));
				mapa.put("nombrePais",rs.getObject("nombrepais"));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCiudades de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que consulta las localidades relacionadas con la ciudad
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerLocalidadesCiudades(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar
			String consulta = "SELECT 	c.codigo_ciudad as codigociudad,c.descripcion as nombreciudad, " +
										"d.codigo_departamento as codigodepartamento,d.descripcion as nombredepartamento, " +
										"p.codigo_pais as codigopais,p.descripcion as nombrepais, " +
										"l.codigo_localidad AS codigolocalidad, l.descripcion AS nombrelocalidad " +//-										
										"from ciudades c " +
										"inner join departamentos d on (d.codigo_departamento=c.codigo_departamento) " +
										"inner join paises p on (p.codigo_pais=c.codigo_pais) " +
										"INNER JOIN localidades l 	ON(l.codigo_ciudad=c.codigo_ciudad) " +//-										
										"order by c.descripcion desc ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoLocalidad",rs.getObject("codigolocalidad"));
				mapa.put("nombreLocalidad",rs.getObject("nombrelocalidad"));//--
				mapa.put("codigoCiudad",rs.getObject("codigociudad"));
				mapa.put("nombreCiudad",rs.getObject("nombreciudad"));
				mapa.put("codigoDepartamento",rs.getObject("codigodepartamento"));
				mapa.put("nombreDepartamento",rs.getObject("nombredepartamento"));
				mapa.put("codigoPais",rs.getObject("codigopais"));
				mapa.put("nombrePais",rs.getObject("nombrepais"));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerLocalidadesCiudades de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que consulta las ciudades por pais
	 * @param con
	 * @param codigoPais
	 * @return
	 */
	public static ArrayList obtenerCiudadesXPais(Connection con,String codigoPais)
	{
//		long ini = System.currentTimeMillis();
		CacheFacade fachada = new CacheFacade();
		ArrayList<HashMap<String, Object>> mapaCiudades = null;
		Object objetosEnChache = fachada.obtenerDeCache("mapaCiudades_pais_"+codigoPais, HashMap.class);
		if (objetosEnChache != null )
		{
			mapaCiudades = (ArrayList<HashMap<String, Object>>)objetosEnChache;
		}
		else
		{
			mapaCiudades = new ArrayList<HashMap<String, Object>>();
			try
			{
				//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar
				String consulta = "SELECT " +
					"c.codigo_ciudad as codigociudad," +
					"c.descripcion as nombreciudad," +
					"d.codigo_departamento as codigodepartamento," +
					"d.descripcion as nombredepartamento, " +
					"p.codigo_pais as codigopais," +
					"p.descripcion as nombrepais " +
					"from ciudades c " +
					"inner join departamentos d on (d.codigo_departamento=c.codigo_departamento AND d.codigo_pais=c.codigo_pais) " +
					"inner join paises p on (p.codigo_pais=c.codigo_pais) " +
					"WHERE c.codigo_pais = '"+codigoPais+"' " +
					"order by c.descripcion desc ";
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con, consulta);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				int i=0;
				while (rs.next())
				{
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("codigoCiudad",rs.getObject("codigociudad"));
					mapa.put("nombreCiudad",rs.getObject("nombreciudad"));
					mapa.put("codigoDepartamento",rs.getObject("codigodepartamento"));
					mapa.put("nombreDepartamento",rs.getObject("nombredepartamento"));
					mapa.put("codigoPais",rs.getObject("codigopais"));
					mapa.put("nombrePais",rs.getObject("nombrepais"));
					mapaCiudades.add(i, mapa);
				}
				fachada.guardarEnCache("mapaCiudades_pais_"+codigoPais, mapaCiudades);
			}
			catch(SQLException e)
			{
				logger.error("Error en obtenerCiudades de SqlBaseUtilidadesDao: "+e);
			}
		}
//		long fin = System.currentTimeMillis();
//		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTIEMPO: " + (fin - ini) + "\n\n\n");
		return mapaCiudades;
	}
	
	/**
	 * Mï¿½todo que retorna los esquemas tarifarios de ISS
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEsquemastarifariosIss(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT " +
								"codigo as codigoesquema," +
								"nombre as nombreesquema " +							
							"FROM esquemas_tarifarios " +
							"WHERE tarifario_oficial="+ConstantesBD.codigoEsqTarifarioGeneralTodos+
							" UNION" +
							" SELECT " +
								"codigo as codigoesquema," +
								"nombre as nombreesquema " +
							"FROM esq_tar_porcen_cx " +
							"WHERE codigo="+ConstantesBD.codigoEsqTarifarioGeneralTodosIss;
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoesquema", rs.getObject(1));
				mapa.put("nombreesquema", rs.getObject(2));
				resultado.add(i,mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener los esquemas tarifarios de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que retorna todos los tipos de asocio
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerTiposAsocio(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT " +
								"codigo as codigoasocio," +
								"nombre as nombreasocio " +
							"FROM grupos_asocio ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoasocio", rs.getObject(1));
				mapa.put("nombreasocio", rs.getObject(2));
				resultado.add(i,mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener los Tipos de asocio de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que llena un array list con los tipos de anestesia si se quiere filtrar por el campo mostrar_en_hqx se le envia 'true' o 'false' sino se le envia un string vacio
	 * @param con
	 * @param mostrarEnHqx
	 * @return
	 */
	public static ArrayList obtenerTiposAnestesia(Connection con,String mostrarEnHqx)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT " +
								"codigo as codigoanestesia," +
								"descripcion as nombreanestesia " +
							"FROM tipos_anestesia ";
			if(!mostrarEnHqx.equals(""))
				cadena+=" WHERE mostrar_en_hqx='"+mostrarEnHqx+"'";
			
			cadena +=" ORDER BY nombreanestesia DESC";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoanestesia", rs.getObject(1));
				mapa.put("nombreanestesia", rs.getObject(2));
				resultado.add(i,mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener los Tipos de asocio de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que carga un array list con las ocupaciones medicas 
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerOcupacionesMedicas(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT " +
								"codigo as codigoocupacion," +
								"nombre as nombreocupacion " +
							"FROM ocupaciones_medicas ";
			
			cadena+=" ORDER BY nombreocupacion DESC";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoocupacion", rs.getObject(1));
				mapa.put("nombreocupacion", rs.getObject(2));
				resultado.add(i,mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener las ocupaciones medicas de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que retorna un array list con las especialidades
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEspecialidadesEnArray(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//*****************FILTROS PARA OBTENER ESPECIALIDADES*****************************
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString(), true);
			int codigoMedico = Utilidades.convertirAEntero(campos.get("codigoMedico").toString(), true);
			String tipoEspecialidad = campos.get("tipoEspecialidad").toString();
			//*********************************************************************************
			
			String seccionSELECT="SELECT " +
				"e.codigo as codigoespecialidad," +
				"e.nombre as nombreespecialidad " ;
			
			if(codigoMedico>0)
				seccionSELECT += "FROM especialidades_medicos em " +
					"INNER JOIN especialidades e ON(e.codigo=em.codigo_especialidad) ";
			else
				seccionSELECT += "FROM especialidades e " ;
			
			
			
			String seccionWHERE = "WHERE " + (codigoMedico>0?"em.codigo_medico = "+codigoMedico+" AND e.codigo >0":"e.codigo >0 ");
			
			if(codigoEspecialidad>0)
				seccionWHERE += " AND e.codigo = "+codigoEspecialidad;
			
			if (!tipoEspecialidad.isEmpty())
				seccionWHERE+=" AND e.tipo_especialidad='"+tipoEspecialidad+"'";
			
			
			String cadena = seccionSELECT + seccionWHERE+" ORDER BY e.nombre DESC" ;
			logger.info("\n la cadena --> "+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoespecialidad", rs.getObject(1));
				mapa.put("nombreespecialidad", rs.getObject(2));
				resultado.add(i,mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener las especialidades de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que retorna un array list con los tarifarios oficiales filtrando por la columna tarifarios si se quiere se le envia un string con 'S' , 'N' o vacio, ejemplo sin filtro-->>Utilidades.obtenerTarifariosOficiales(con,""); ejemplo con filtro-->>Utilidades.obtenerTarifariosOficiales(con,"S");
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerTarifariosOficiales(Connection con,String tarifarios)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT " +
								"codigo as codigotarifario," +
								"nombre as nombretarifario " +
							"FROM tarifarios_oficiales ";
			if(!UtilidadTexto.isEmpty(tarifarios))
				cadena+=" WHERE tarifarios='"+tarifarios+"'";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while(rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigotarifario", rs.getObject(1));
				mapa.put("nombretarifario", rs.getObject(2));
				resultado.add(i,mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener las especialidades de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param tipo_BD 
	 * @param tipoRegimen
	 * @param tipoContrato
	 * @return
	 */
	public static ArrayList obtenerConvenios(Connection con, HashMap campos, int tipo_BD)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
		//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar			
			String consulta = "SELECT " +
									"c.codigo," +
									"c.empresa," +
									"c.tipo_regimen," +
									"c.nombre," +
									"c.tipo_contrato," +
									"c.pyp,"+
									"case when activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'ACTIVO' else 'INACTIVO' end as activo, "+
									"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion, " +
									"coalesce(c.cod_aseguradora,'') As cod_aseguradora "+				
								"FROM " +
									"convenios c " +
								" inner join facturacion.tipos_convenio tc on (c.tipo_convenio=tc.codigo and c.institucion=tc.institucion) ";
			
			String where = "";
			//1) Validacion del tipo de regimen ******************************************************
			if(!campos.get("tipoRegimen").toString().trim().equals(""))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.tipo_regimen="+campos.get("tipoRegimen");
			
			//2) Validacion del tipo de contrato ********************************************************
			if(!campos.get("tipoContrato").toString().trim().equals(""))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.tipo_contrato="+campos.get("tipoContrato");
			
			//3) Validacion de validacion de fecha vencimiento del contrato ******************************
			if(UtilidadTexto.getBoolean(campos.get("verificarVencimientoContrato").toString()))
			{
				where += " "+(where.equals("")?"WHERE":"AND")+" c.codigo IN " +
					"(SELECT con.convenio FROM contratos con " +
					"WHERE " +
					"con.convenio = c.codigo AND " +
					"("+(campos.get("fechaReferencia").toString().equals("")?"CURRENT_DATE":"'"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())+"'")+" BETWEEN fecha_inicial AND fecha_final))";
			}

			//si la validacion 	verificarVencimientoContrato se encuentra activa, incluye la que tenga contratos asociados
			if(!UtilidadTexto.getBoolean(campos.get("verificarVencimientoContrato")+"") && UtilidadTexto.getBoolean(campos.get("validarContratosAsociados")+""))
			{
				where += " "+(where.equals("")?"WHERE":"AND")+" (SELECT count(1) from contratos where convenio = c.codigo) > 0 ";
			}
			

			//4) Validacion de convenios activos **************************************************************
			if(UtilidadTexto.getBoolean(campos.get("activo").toString()))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
			
			//5) Validaciï¿½n de convenios por el tipo convenio ****************************************************
			if(UtilidadCadena.noEsVacio(campos.get("tipoConvenio")+""))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.tipo_convenio = '"+campos.get("tipoConvenio")+"' ";
			
			//6)validacion si es aseguradora
			if  (UtilidadCadena.noEsVacio(campos.get("aseguradora")+""))
				if(UtilidadTexto.getBoolean(campos.get("aseguradora")+""))
					where += " "+(where.equals("")?"WHERE":"AND")+" tc.aseg_at_ec = '"+ConstantesBD.acronimoSi+"' ";
				else
					where += " "+(where.equals("")?"WHERE":"AND")+" tc.aseg_at_ec = '"+ConstantesBD.acronimoNo+"' ";
			
			//7)Validacion Reporte Atencion Inicial de Urgencias			
			if  (UtilidadCadena.noEsVacio(campos.get("ReporteAtencioniniUrge")+""))
				if(UtilidadTexto.getBoolean(campos.get("ReporteAtencioniniUrge")+""))
					where += " "+(where.equals("")?"WHERE":"AND")+" c.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' ";			
			
			if (UtilidadCadena.noEsVacio(campos.get("tipoAtencion")+""))
				where+=" "+(where.equals("")?"WHERE": " AND")+" c.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"' "; 
			
			if (UtilidadCadena.noEsVacio(campos.get("manejaBonos")+""))
			{
				where+=" "+(where.equals("")?"WHERE": " AND")+" c.maneja_bonos='"+campos.get("manejaBonos")+"' ";
			}
			
			if (UtilidadCadena.noEsVacio(campos.get("manejaPromociones")+""))
			{
				where+=" "+(where.equals("")?"WHERE": " AND")+" c.maneja_promociones='"+campos.get("manejaPromociones")+"' ";
			}
			
			if(  UtilidadCadena.noEsVacio(campos.get("manejeConveniosTarjetaCliente")+""))
			{
				where+=" "+(where.equals("")?"WHERE": " AND")+" c.es_conv_tar_cli='"+campos.get("manejeConveniosTarjetaCliente")+"' ";
			}
			
			
			
			if (UtilidadCadena.noEsVacio(campos.get("atencionOdontologica")+""))
				if(UtilidadTexto.getBoolean(campos.get("atencionOdontologica")+""))
					where+=" "+(where.equals("")?"WHERE": " AND")+" c.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"' ";
				else
					where+=" "+(where.equals("")?"WHERE": " AND")+" c.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral+"' ";
			
			consulta += where;
			logger.info("consulta de los convenios=> "+consulta+" \n\n campos "+campos);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta+" order by c.nombre desc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoConvenio",rs.getObject(1));
				mapa.put("codigoEmpresa",rs.getObject(2));
				mapa.put("codigoTipoRegimen",rs.getObject(3));
				mapa.put("nombreConvenio",rs.getObject(4));
				mapa.put("codigoTipoContrato",rs.getObject(5));
				mapa.put("pyp",rs.getObject(6));
				mapa.put("activo",rs.getObject(7));
				
				if(rs.getString(8) != null)
					mapa.put("empresasInstitucion",rs.getObject(8));
				else
					mapa.put("empresasInstitucion",ConstantesBD.codigoNuncaValido+"");
				
				mapa.put("codAseguradora",rs.getObject(9));
				
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConvenios de SqlBaseUtilidadesDao: "+e);
		}
		return resultado; 
	}

	/**
	 * 
	 * @param con
	 * @param campos
	 * @param oracle
	 * @return
	 */
	public static ArrayList obtenerConveniosContrato(Connection con,HashMap campos, int oracle) {
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
		//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar			
			String consulta = "SELECT " +
									"con.codigo as codigo_contrato," +
									"con.numero_contrato, " +
									"c.codigo as codigo_convenio," +
									"c.empresa," +
									"c.tipo_regimen," +
									"c.nombre," +
									"c.tipo_contrato," +
									"c.pyp,"+
									"case when activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'ACTIVO' else 'INACTIVO' end as activo, "+
									"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion, " +
									"coalesce(c.cod_aseguradora,'') As cod_aseguradora "+				
								"FROM " +
									"convenios c " +
								"INNER JOIN " +
									"contratos con ON (con.convenio=c.codigo)";
			
			String where = "";
			//1) Validacion del tipo de regimen ******************************************************
			if(!campos.get("tipoRegimen").toString().trim().equals(""))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.tipo_regimen="+campos.get("tipoRegimen");
			
			//2) Validacion del tipo de contrato ********************************************************
			if(!campos.get("tipoContrato").toString().trim().equals(""))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.tipo_contrato="+campos.get("tipoContrato");
			
			//3) Validacion de validacion de fecha vencimiento del contrato ******************************
			if(UtilidadTexto.getBoolean(campos.get("verificarVencimientoContrato").toString()))
			{
				where += " "+(where.equals("")?"WHERE":"AND")+" c.codigo IN " +
					"(SELECT con.convenio FROM contratos con " +
					"WHERE " +
					"con.convenio = c.codigo AND " +
					"("+(campos.get("fechaReferencia").toString().equals("")?"CURRENT_DATE":"'"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())+"'")+" BETWEEN fecha_inicial AND fecha_final))";
			}

			//si la validacion 	verificarVencimientoContrato se encuentra activa, incluye la que tenga contratos asociados
			if(!UtilidadTexto.getBoolean(campos.get("verificarVencimientoContrato")+"") && UtilidadTexto.getBoolean(campos.get("validarContratosAsociados")+""))
			{
				where += " "+(where.equals("")?"WHERE":"AND")+" (SELECT count(1) from contratos where convenio = c.codigo) > 0 ";
			}
			

			//4) Validacion de convenios activos **************************************************************
			if(UtilidadTexto.getBoolean(campos.get("activo").toString()))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
			
			//5) Validaciï¿½n de convenios por el tipo convenio ****************************************************
			if(UtilidadCadena.noEsVacio(campos.get("tipoConvenio")+""))
				where += " "+(where.equals("")?"WHERE":"AND")+" c.tipo_convenio = '"+campos.get("tipoConvenio")+"' ";
			
			//6)validacion si es aseguradora
			if  (UtilidadCadena.noEsVacio(campos.get("aseguradora")+""))
				if(UtilidadTexto.getBoolean(campos.get("aseguradora")+""))
					where += " "+(where.equals("")?"WHERE":"AND")+" c.aseguradora = '"+ConstantesBD.acronimoSi+"' ";
				else
					where += " "+(where.equals("")?"WHERE":"AND")+" c.aseguradora = '"+ConstantesBD.acronimoNo+"' ";
			
			//7)Validacion Reporte Atencion Inicial de Urgencias			
			if  (UtilidadCadena.noEsVacio(campos.get("ReporteAtencioniniUrge")+""))
				if(UtilidadTexto.getBoolean(campos.get("ReporteAtencioniniUrge")+""))
					where += " "+(where.equals("")?"WHERE":"AND")+" c.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' ";			
			
			if (UtilidadCadena.noEsVacio(campos.get("tipoAtencion")+""))
				where+=" "+(where.equals("")?"WHERE": " AND")+" c.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"' "; 
			
			if (UtilidadCadena.noEsVacio(campos.get("manejaBonos")+""))
			{
				where+=" "+(where.equals("")?"WHERE": " AND")+" c.maneja_bonos='"+campos.get("manejaBonos")+"' ";
			}
			
			consulta += where;
			logger.info("consulta de los convenios=> "+consulta+" \n\n campos "+campos);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta+" order by c.nombre desc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoContrato", rs.getObject(1));
				mapa.put("numeroContrato", rs.getObject(2));
				mapa.put("codigoConvenio",rs.getObject(3));
				mapa.put("codigoEmpresa",rs.getObject(4));
				mapa.put("codigoTipoRegimen",rs.getObject(5));
				mapa.put("nombreConvenio",rs.getObject(6));
				mapa.put("codigoTipoContrato",rs.getObject(7));
				mapa.put("pyp",rs.getObject(8));
				mapa.put("activo",rs.getObject(9));
				
				if(rs.getString(10) != null)
					mapa.put("empresasInstitucion",rs.getObject(10));
				else
					mapa.put("empresasInstitucion",ConstantesBD.codigoNuncaValido+"");
				
				mapa.put("codAseguradora",rs.getObject(11));
				
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConvenios de SqlBaseUtilidadesDao: "+e);
		}
		return resultado; 
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoFiltro : manejo del filtro
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerTiposIdentificacion(Connection con,String tipoFiltro,int institucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String consulta = "";
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			if(tipoFiltro.equals("ingresoPaciente"))
			{
				consulta = "SELECT ti.acronimo As acronimo, ti.nombre As nombre, tii.es_consecutivo AS es_consecutivo " +
					"FROM tipos_Identificacion ti " +
					"INNER JOIN tipos_id_institucion tii ON (ti.acronimo=tii.acronimo) " +
					"WHERE ti.acronimo <> 'NI' AND tii.institucion = "+institucion+" order by 2 desc";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs = new ResultSetDecorator(pst.executeQuery());
				int i=0;
				while (rs.next())
				{
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("acronimo",rs.getObject(1));
					mapa.put("nombre",rs.getObject(2));
					mapa.put("esConsecutivo",rs.getObject(3));
					resultado.add(i, mapa);
				}
			}
			else if(tipoFiltro.equals("ingresoPoliza"))
			{
				consulta = "SELECT ti.acronimo As acronimo, ti.nombre As nombre, tii.es_consecutivo AS es_consecutivo " +
				"FROM tipos_Identificacion ti " +
				"INNER JOIN tipos_id_institucion tii ON (ti.acronimo=tii.acronimo) " +
				"WHERE tii.es_consecutivo = "+ValoresPorDefecto.getValorFalseParaConsultas()+" AND tii.institucion = "+institucion+" order by 2 desc";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs = new ResultSetDecorator(pst.executeQuery());
				while (rs.next())
				{
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("acronimo",rs.getObject(1));
					mapa.put("nombre",rs.getObject(2));
					resultado.add(mapa);
				}
			}
			else
			{
				consulta = "SELECT acronimo,nombre from tipos_identificacion  order by 2 desc";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs = new ResultSetDecorator(pst.executeQuery());
				int i=0;
				while (rs.next())
				{
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("acronimo",rs.getObject(1));
					mapa.put("nombre",rs.getObject(2));
					resultado.add(i, mapa);
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConvenios de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	
	/**
	 * RETORNA EL NOMBRE TIPO DE IDENTIFICACION
	 * RECIBE EL ACRONIMO TIPO DE IDENTIFICACION
	 * @return
	 */
	public static String obtenerNombreTipoIdentificacion(String acronimo ){
		
		String nombreIdentificacion="";
		String consulta="select nombre from administracion.tipos_identificacion where acronimo=?";
		
		if(!UtilidadTexto.isEmpty(acronimo))
		{	
			try 
			{
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator( con, consulta);
				ps.setString(1, acronimo);
				logger.info("CONSULTA TIPOS DE IDENTIFICACION \n\n\n"+ps+"\n\n\n");
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					nombreIdentificacion=rs.getString("nombre");
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (SQLException e) 
			{
					logger.error("error en carga==> "+e);
			}
			
			
			
		}
	
		
		return nombreIdentificacion;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerCodigoIngresoDadaCuenta(Connection con, String idCuenta)
	{
		String consulta=" SELECT id_ingreso from cuentas where id=?";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoIngresoDadaCuenta de SqlBaseUtilidadesDao: "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}

	/**
	 * 
	 * @param con
	 * @param estado
	 * @return
	 */
	public static ArrayList obtenerSolicitudesEstado(Connection con, int estado, int tipoSolicitud) 
	{
		ArrayList resultado=new ArrayList();
		try
		{
			String consulta = " SELECT numero_solicitud from solicitudes where estado_historia_clinica = ? and tipo=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,estado);
			pst.setInt(2,tipoSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next())
			{
				resultado.add(rs.getObject(1));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConvenios de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo implementado para consultar la fecha de ingreso de una cuenta dependiendo de su via de ingreso
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @return
	 */
	public static String getFechaIngreso(Connection con,String idCuenta,int viaIngreso)
	{
		try
		{
			String consulta = "";
			String fecha = "";
			switch(viaIngreso)
			{
				case ConstantesBD.codigoViaIngresoHospitalizacion:
					consulta = "SELECT to_char(fecha_admision,'DD/MM/YYYY') as fecha FROM admisiones_hospi WHERE cuenta = ?";
				break;
				case ConstantesBD.codigoViaIngresoUrgencias:
					consulta = "SELECT to_char(fecha_admision,'DD/MM/YYYY') as fecha FROM admisiones_urgencias WHERE cuenta =  ?";
				break;
				case ConstantesBD.codigoViaIngresoAmbulatorios:
				case ConstantesBD.codigoViaIngresoConsultaExterna:
					consulta = " SELECT to_char(fecha_apertura,'DD/MM/YYYY') as fecha FROM cuentas WHERE id = ?";
				break;
				
			}
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				fecha = rs.getString("fecha");
			
			return fecha;
		}
		catch(SQLException e)
		{
			logger.error("Error en getFechaIngreso de SqlBaseUtilidadesDao: "+e);
			return "";
		}
	}

	/**
	 * 
	 * @param con
	 * @param tipoId
	 * @param numeroIdentificacion
	 * @return
	 * @throws SQLException 
	 */
	public static String getCodigoPaisDeptoCiudadPersona(Connection con, String tipoId, String numeroIdentificacion) 
	{
		String consulta="SELECT codigo_pais_id, codigo_depto_id, codigo_ciudad_id "+ 
						"		FROM personas per "+ 
						"			 WHERE per.tipo_identificacion = ? " +
						"			   AND per.numero_identificacion = ? ";
		
		
		try {
			
			
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 
			stm.setString(1, tipoId);
			stm.setString(2, numeroIdentificacion);

			HashMap mp = UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()),true,false);
			
		if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
		{
			return (mp.get("codigo_pais_id_0") +ConstantesBD.separadorSplit+ mp.get("codigo_depto_id_0")+ "" + ConstantesBD.separadorSplit + mp.get("codigo_ciudad_id_0"));
		}
		else
		{
			return ""; 
		}
		} catch (SQLException e) {
			e.printStackTrace();
			return ""; 
		}
	
	}
	
	/**
	 * 
	 * @param idCuenta
	 * @param fechaFormatoApp
	 * @return
	 */
	public static boolean esCamaUciDadaFecha(Connection con, String idCuenta, String fechaFormatoApp, String hora)
	{
		String fechaBD= UtilidadFecha.conversionFormatoFechaABD(fechaFormatoApp);
		//String consulta="SELECT count(1) as contador FROM traslado_cama t INNER JOIN camas1 cam ON (t.codigo_nueva_cama=cam.codigo) where t.cuenta=?   and cam.es_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" and ('"+fechaBD+"'>=t.fecha_asignacion and (fecha_finalizacion IS NULL or '"+fechaBD+"'<=fecha_finalizacion))";
		String consulta="SELECT " +
							"count(1) as contador " +
						"FROM " +
							"traslado_cama t " +
							"INNER JOIN camas1 cam ON (t.codigo_nueva_cama=cam.codigo) " +
						"where " +
							"t.cuenta=? " +
							"and cam.es_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"and (" +
									"t.fecha_asignacion<'"+fechaBD+"' " +
									"and " +
									"(" +
										"t.fecha_finalizacion IS NULL " +
										"or t.fecha_finalizacion>'"+fechaBD+"' " +
										"or " +
										"(" +
											"t.fecha_finalizacion='"+fechaBD+"' " +
											"and t.hora_finalizacion>='"+hora+"'" +
										")" +
									") " +
									"or " +
									"(" +
										"t.fecha_asignacion='"+fechaBD+"' " +
										"and t.hora_asignacion<='"+hora+"'" +
									") " +
									"and " +
									"(" +
										"t.fecha_finalizacion IS NULL " +
										"or t.fecha_finalizacion>'"+fechaBD+"' " +
										"or " +
										"(" +
											"t.fecha_finalizacion='"+fechaBD+"' " +
											"and t.hora_finalizacion>='"+hora+"'" +
										")" +
									")" +
								")"; 
		
		logger.info("\n\nesCamaUciDadaFecha-->"+consulta+" -->idCuenta="+idCuenta);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("contador")>0)
					return true;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en esCamaUciDadaFecha de SqlBaseUtilidadesDao: "+e);
		}
		return false;
	}
	
	
	/**	  
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public static boolean esMotivoSircUsado(Connection con, String consecutivo){
		boolean esServicioSircUsado=false;
		try
		{
			String cadena="SELECT numero_referencia FROM referencia where motivo_referencia="+consecutivo;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next()){
				esServicioSircUsado=true;
			}
			else {
				cadena="SELECT numero_referencia_tramite FROM servic_instit_referencia where motivo="+consecutivo;
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs =new ResultSetDecorator(ps.executeQuery());
				if (rs.next()){
					esServicioSircUsado=true;
				}
				else {
					cadena="SELECT numero_referencia_tramite FROM instituc_traslado_paciente where motivo="+consecutivo;
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					rs =new ResultSetDecorator(ps.executeQuery());
					if (rs.next()){
						esServicioSircUsado=true;
					} else {
						cadena="SELECT codigo FROM his_serv_inst_referencia where motivo="+consecutivo;
						ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						rs =new ResultSetDecorator(ps.executeQuery());
						if (rs.next()){
							esServicioSircUsado=true;
						}
						else {
							cadena="SELECT codigo FROM his_institu_traslado_paciente where motivo="+consecutivo;
							ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							rs =new ResultSetDecorator(ps.executeQuery());
							if (rs.next()){
								esServicioSircUsado=true;
							}
						}
					}
				}
					
			}
				
				
		}
		catch (SQLException e)
		{
			logger.error("Error en esServicioSircUsado el servicio esta siendo utilizado: "+e);
		}
		return esServicioSircUsado;
	}
	
	/**	  
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public static boolean esServicioSircUsado(Connection con, String codigo, String institucion)
	{
		String cadena="DELETE FROM servicios_sirc where codigo="+codigo+" AND institucion="+institucion;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return !(ps.executeUpdate()>0);
		}
		catch (SQLException e)
		{
			logger.error("Error en esServicioSircUsado el servicio esta siendo utilizado: "+e);
		}
		return true;
	}
	
	/**	  
	 * Mï¿½todo que indica si se esta
	 * usando un registro de entidades subcontratadas
	 * en otra tabla
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public static boolean esEntidadSubContratadaUsado (Connection con, String codigo, String institucion)
	{
		HashMap mapa = new HashMap ();
		//cadena de eliminacion de entidades subcontratadas
		String cadenaEliminacion="DELETE FROM facturacion.entidades_subcontratadas where codigo_pk="+codigo+" AND institucion="+institucion;
		//cadena de consulta del detalle de la entidad
		String cadenaConsultaDetalle="SELECT codigo AS codigo_det FROM det_entidades_subcontratadas where codigo_ent_sub="+codigo;
		//cadena de eliminacion del los detalles
		String cadenaEliminacionDetalle="DELETE FROM det_entidades_subcontratadas WHERE codigo=?";
	
		boolean operacionTrue=false; 
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			if (Integer.parseInt(mapa.get("numRegistros")+"")>0){
				for (int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"") && operacionTrue ;i++)
				{
					try 
					{
						PreparedStatementDecorator psed= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						//codigo detalle
						psed.setObject(1, mapa.get("codigoDet_"+i));
						int consulta=psed.executeUpdate();
						if(consulta>0)
							operacionTrue=true;
						
					} 
					catch (SQLException e) 
					{
						logger.info("\n problema eliminando el detalle "+e);
						operacionTrue=false;
					}
					
				
				}
			}
			
			if (operacionTrue)
			{
				try 
				{
					PreparedStatementDecorator pse= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					return !(pse.executeUpdate()>0);
				}
				catch (Exception e) 
				{
					//e.printStackTrace();
				}
			}
			
		}
		catch (SQLException e)
		{
			logger.info("\n problema consultando el detalle "+e);
		}	
	
		return true;
	}
	
	
	/**	  
	 * Mï¿½todo que indica si concepto esta asociado en otra tabala.
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public static boolean esConceptosPagoPoolesUsado(Connection con, String codigo, String institucion)
	{
		logger.info("ntre ..."+codigo);
		String cadena="DELETE FROM conceptos_pagos_pooles where codigo='"+codigo+"' AND institucion="+institucion;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return !(ps.executeUpdate()>0);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap obtenerCodigoNombreTablaMap(Connection con, String nombreTabla) 
	{
		String consulta="SELECT codigo as codigo, nombre as nombre from "+nombreTabla+" order by nombre ";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPosiciones de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta el id de la ultima cuenta valida del paciente de su ingreso abierto o cerrado,
	 * 
	 * en el caso de que no haya cuenta abierta se devuelve cadena vacï¿½a
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static String obtenerIdCuentaValidaIngresoAbiertoCerrado(Connection con,String codigoPaciente)
	{
		try
		{
			String idCuenta = "";
			String consulta = "SELECT c.id FROM " +
				"cuentas c " +
				"INNER JOIN ingresos i ON(i.id=c.id_ingreso) " +
				"WHERE " +
				"c.codigo_paciente = "+codigoPaciente+" AND " +
				"(" +
					"c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") OR " +
					"(c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" AND getcuentafinal(c.id) IS NULL)" +
				") AND " +
				"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
				"ORDER BY i.estado,c.fecha_apertura DESC,c.hora_apertura DESC ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				idCuenta = rs.getString("id");
			
			return idCuenta;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerIdCuentaAbierta de SqlBaseUtilidadesDao: "+e);
			return "";
		}
	}

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static Object obtenerViasIngreso(Connection con,boolean usarVector)
	{
		String cadena="SELECT codigo,nombre from vias_ingreso order by nombre";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(!usarVector)
			{
				HashMap mapa=new HashMap();
				mapa.put("numRegistros", "0");
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
				return mapa;
			}
			else
			{
				Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
				while(rs.next())
				{
					resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
				}
				return resultado;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Mï¿½todo encargado de devolver las vias de ingreso-tipo paciente 
	 * en un HashMap
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerViasIngresoTipoPaciente(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
			try
			{
				
				String cadena="SELECT case when getcuantosTipoPacViaIngreso(vp.via_ingreso)>1  " +
												"	then getnombreviaingreso(vp.via_ingreso)||' - '||getnombretipopaciente(vp.tipo_paciente) " +
												"	else getnombreviaingreso(vp.via_ingreso)||'' end  " +
												"				As viaingresotipopac,  " +
												" vp.via_ingreso as codvia," +
												" vp.tipo_paciente as codpac " +
										" from tip_pac_via_ingreso vp ORDER BY viaingresotipopac";

				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				while (rs.next())
				{
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("codvia",rs.getObject("codvia"));
					mapa.put("codtipopac", rs.getObject("codpac"));
					mapa.put("viaingresotipopac",rs.getObject("viaingresotipopac"));
					resultado.add(mapa);
				}
			}
			catch(SQLException e)
			{
				logger.error("Error en obtenerViasIngresoTipoPaciente de SqlBaseUtilidadesDao: "+e);
			}
			return resultado;
	}
	
	
	/**
	 * Mï¿½todo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerViasIngreso(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//*********SE TOMAN LOS PARï¿½METROS DE LAS Vï¿½AS DE INGRESO***************
			String codigosViasIngreso = campos.get("codigosViasIngreso").toString();
			//***********************************************************************
			
			
			String cadena="SELECT codigo,nombre from vias_ingreso ";
			if(!codigosViasIngreso.equals(""))
				cadena += " WHERE codigo in ("+codigosViasIngreso+") ";
			cadena += "order by nombre";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("nombre",rs.getObject("nombre"));
			
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerViasIngreso de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerViasIngresoBloqueaDeudorBloqueaPaciente(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//*********SE TOMAN LOS PARï¿½METROS DE LAS Vï¿½AS DE INGRESO***************
			String codigosViasIngreso = campos.get("codigosViasIngreso").toString();
			//***********************************************************************
			
			
			String cadena="SELECT DISTINCT codigo,nombre from vias_ingreso vi INNER JOIN garantia_paciente gp ON(gp.codigo_via_ingreso=vi.codigo) ";
			if(!codigosViasIngreso.equals(""))
				cadena += " WHERE codigo in ("+codigosViasIngreso+")";
			
			cadena+="AND gp.bloquea_ing_deu_x_saldo_mora='"+ConstantesBD.acronimoSi+"' OR gp.bloquea_ing_pac_x_saldo_mora='"+ConstantesBD.acronimoSi+"' ";
		
			cadena += "order by nombre";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			logger.info("CONSULTA "+cadena);
			
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("nombre",rs.getObject("nombre"));
			
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerViasIngreso de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String obtenerViaIngresoCuenta(Connection con, String codigoCuenta)
	{
		String cadena="SELECT via_ingreso  as viaingreso from cuentas c where c.id="+codigoCuenta;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	/**
	 * Mï¿½todo que verifica si un procedimiento tiene respuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean tieneProcedimientoRespuestaMultiple(Connection con,String numeroSolicitud)
	{
		try
		{
			boolean resp = false;
			String consulta = "SELECT respuesta_multiple FROM sol_procedimientos WHERE numero_solicitud = "+numeroSolicitud;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resp = UtilidadTexto.getBoolean(rs.getString("respuesta_multiple"));
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneProcedimientoRespuestaMultiple de SqlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que verifica si un procedimiento ya tiene finalizada
	 * la repsuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean estaFinalizadaRespuestaMultiple(Connection con,String numeroSolicitud)
	{
		try
		{
			boolean resp = false;
			String consulta = "SELECT finalizada_respuesta FROM sol_procedimientos WHERE numero_solicitud = "+numeroSolicitud;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resp = UtilidadTexto.getBoolean(rs.getString("finalizada_respuesta"));
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en estaFinalizadaRespuestaMultiple de SQlBaseUtilidadesDao: "+e);
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */	
	public static boolean esServicioRespuestaMultiple(Connection con, String codigoServicio)
	{
		try
		{
			String consulta = "SELECT codigo from servicios where codigo="+codigoServicio+" and respuesta_multiple='"+ConstantesBD.acronimoSi+"'";
			logger.info("\n\n\n XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			logger.info("esServicioRespuestaMultiple->"+consulta);
			logger.info("\n\n\n XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return (new ResultSetDecorator(ps.executeQuery())).next();
		}
		catch(SQLException e)
		{
			logger.error("Error en esServicioRespuestaMultiple de SQlBaseUtilidadesDao: "+e);
		}
		return false;
	}


	/**
	 * Mï¿½todo que consulta los numeros de solicitud de procedimiento que tienen el
	 * estado de historia clï¿½nica especificado en el parï¿½metro, para el paciente de acuedo a la cuenta
	 * @param con
	 * @param cuenta
	 * @param estadoHistoriaClinica
	 * @return
	 */
	public static HashMap getSolProcedimientosEstadoHistoClinica(Connection con, int cuenta, int estadoHistoriaClinica)
	{
		String cadena="SELECT sol.numero_solicitud AS numero_solicitud " +
					  "		FROM solicitudes sol " +
					  "			INNER JOIN sol_procedimientos sp on (sp.numero_solicitud=sol.numero_solicitud) " +
					  "				WHERE sol.estado_historia_clinica="+estadoHistoriaClinica+" AND cuenta="+cuenta;
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en getSolProcedimientosEstadoHistoClinica de SqlBaseUtilidadesDao: "+e);
		}
		return mapa;
	}

	
	/**
	 * 
	 */
	public static HashMap obtenerResultadosRespuestasSolicitudesProcedimientos(Connection con, String numeroSolicitud)
	{
		
		String cadena="SELECT resultados,observaciones from res_sol_proc where numero_solicitud='"+numeroSolicitud+"' order by fecha_grabacion,hora_grabacion";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * Mï¿½todo que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 * acronimo + ConstantesBD.separadorSplit + tipoCie 
	 * si no encuentra nada retorna vacï¿½o
	 */
	public static String consultarDiagnosticosPaciente(Connection con,String idCuenta,int viaIngreso)
	{
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;

		try
		{
			String diagnosticos = "";
			boolean encontro = false;
			try{
				//Se consulta el diagnostico de la ultima evolucion
				if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion||viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticoEvolucionStr));
					pst.setObject(1,idCuenta);
				
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						diagnosticos = rs.getString("acronimo")+ConstantesBD.separadorSplit+rs.getString("tipo_cie");
						encontro = true;
					}
				}
			}catch(Exception e )
			{
				logger.error("Error en consultarDiagnosticosPaciente de SqlBaseUtilidadesDao: "+e);
				throw e;
			}
			finally{
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			}
			
			try 
			{
				//se consulta el diagnostico de la valoracion, en caso de que la hopsitalizacion o uirgencias no tengan
				//la del egreso o que sea de via de ingreso consulta externa
				if(viaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna||!encontro)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosValoracionStr));
					pst.setObject(1,idCuenta);
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
						diagnosticos = rs.getString("acronimo")+ConstantesBD.separadorSplit+rs.getString("tipo_cie");
				}
			}catch(Exception e )
			{
				logger.error("Error en consultarDiagnosticosPaciente de SqlBaseUtilidadesDao: "+e);
				throw e;
			}
			finally{
				UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			}
			return diagnosticos;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDiagnosticosPaciente de SqlBaseUtilidadesDao: "+e);
			return "";
		}
		catch(Exception e )
		{
			logger.error("Error en consultarDiagnosticosPaciente de SqlBaseUtilidadesDao: "+e);
			return  "";
		}
	
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitudTraslado
	 * @return
	 */
	public static int obtenerEstadoSolicitudTraslado(Connection con, String numeroSolicitudTraslado)
	{
		try
		{
			String consultaStr = " SELECT estado from solicitud_traslado_almacen where numero_traslado ="+numeroSolicitudTraslado;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstadoSolicitudTraslado de SqlBaseUtilidadesDao : "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Mï¿½todo que consulta los usuarios por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerUsuarios(Connection con,int institucion , boolean estado)
	{
		try
		{
			String consulta="";
			
			if(estado=true)
			{
			consulta = "SELECT getnombrepersona(codigo_persona) AS nombre,login FROM usuarios s where s.login not in (select ua.login from usuarios_inactivos ua where ua.login = s.login and ua.codigo_institucion = ?) ORDER BY nombre";
			}
			else
			{
				consulta ="SELECT getnombrepersona(codigo_persona) AS nombre,login FROM usuarios where institucion = ? ORDER BY nombre";
			}
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUsuarios de SqlBaseUrtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta los conceptos generales por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerConceptosGenerales(Connection con,int institucion, String tipoGlosa)
	{
		logger.info("===> SqlBase: Vamos a obtener los conceptos generales");
		try
		{
			String consulta = "SELECT " +
					"codigo AS codigo, " +
					" consecutivo AS consecutivo, " +
					"descripcion AS descripcion, " +
					"tipo_glosa AS tipoGlosa " +
					"FROM glosas.conceptos_generales " +
					"WHERE institucion = ? ";
			
			// Si se necesita cargar un tipo de glosa especifico
			if(UtilidadCadena.noEsVacio(tipoGlosa)) {
				logger.info("ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ se Carga el tipo glosa  " + tipoGlosa);
				consulta += " AND tipo_glosa='" + tipoGlosa + "' "; 
			}
			
			consulta += " ORDER BY codigo";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConceptosGenerales de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Mï¿½todo que consulta los conceptos especificos por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerConceptosEspecificos(Connection con,int institucion)
	{
		logger.info("===> SqlBase: Vamos a obtener los conceptos especificos");
		try
		{
			String consulta = "SELECT " +
					"codigo AS codigo, " +
					"consecutivo AS consecutivo, " +
					"descripcion AS descripcion " +
					"FROM glosas.conceptos_especificos " +
					"WHERE institucion = ? " +
					"ORDER BY codigo";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConceptosEspecificos de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}

	
	
	/**
	 * Obtiene el listado de Conceptos Glosas Almacenados en el sistema
	 * @param con, institucion
	 * @return */
	public static HashMap obtenerConceptosGlosa(Connection con,int institucion) {
		logger.info("===> SqlBaseUtilidades: Obtener los Conceptos Glosas");
		try {
			String consulta = "SELECT " +
									"codigo AS codigo, " +
									"institucion AS institucion, " +
									"descripcion AS descripcion, " +
									"tipo_concepto AS tipoConcepto, " +
									"concepto_general AS conceptoGeneral, " +
									"concepto_especifico AS conceptoEspecifico "+
								"FROM concepto_glosas " +
								"WHERE institucion = ? " +
								"ORDER BY descripcion";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e) {
			logger.error("Error en obtener el listado de ConceptosGlosas desde SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo para obtener los conceptos de glosas parametrizados
	 * @param con
	 * @param tipo
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosGlosa(Connection con,String tipo,int institucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT " +
				"codigo AS codigo, " +
				"institucion AS institucion, " +
				"descripcion AS descripcion, " +
				"tipo_concepto AS tipoConcepto, " +
				"concepto_general AS conceptoGeneral, " +
				"concepto_especifico AS conceptoEspecifico "+
				"FROM concepto_glosas " +
				"WHERE institucion =  "+institucion;
			
			if(!tipo.equals(""))
				consulta += " AND tipo_concepto = '"+tipo+"' ";
			consulta += " ORDER BY descripcion ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("institucion", rs.getObject("institucion"));
				elemento.put("nombre", rs.getObject("descripcion"));
				elemento.put("tipoConcepto", rs.getObject("tipoConcepto"));
				elemento.put("conceptoGeneral", rs.getObject("conceptoGeneral"));
				elemento.put("conceptoEspecifico", rs.getObject("conceptoEspecifico"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConceptosGlosa: "+e);
		}
		return resultados;
	}
	

	/**
	 * Obtiene el listado de Conceptos Ajustes Almacenados en el sistema
	 * @param con, institucion
	 * @return */
	public static HashMap obtenerConceptosAjuste(Connection con,int institucion) {
		logger.info("===> SqlBaseUtilidades: Obtener los Conceptos Ajustes");
		try {
			String consulta = "SELECT " +
									"cac.codigo AS codigo, " +
									"cac.institucion AS institucion, " +
									"cac.descripcion AS descripcion, " +
									"cac.naturaleza AS naturaleza, " +
									"coalesce(tc.nombre, '') AS tipoCartera " +
								"FROM concepto_ajustes_cartera cac " +
								"LEFT OUTER JOIN tipo_cartera tc ON (tc.codigo=cac.tipo_cartera)" +
								"WHERE institucion = ? " +
								"AND (tc.codigo= -1 OR tc.codigo=1 OR tc.codigo=2)" +
								"ORDER BY descripcion";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			logger.info("\n\nCONSULTA OBTENER CONCEPTOS AJUSTES-->"+consulta);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e) {
			logger.error("Error en obtener el listado de ConceptosAjuste desde SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	
	

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */	
	public static boolean esSolicitudProcedimientosFinalizada(Connection con, String numeroSolicitud)
	{
		try
		{
			String consulta = "SELECT CASE WHEN finalizada IS NULL then case when solicitud_multiple="+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end  else case when finalizada="+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end  end as finalizada from sol_procedimientos where numero_solicitud=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getString("finalizada").equals(ConstantesBD.acronimoSi+""))
				{
					return true;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en esServicioRespuestaMultiple de SQlBaseUtilidadesDao: "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param login
	 * @return
	 */
	public static String obtenerCentrosCostoUsuario(Connection con, String login)
	{
		String resultado="";
		try
		{
			String consulta = "SELECT centro_costo from centros_costo_usuario where usuario ='"+login+"'";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			int cont=0;
			while(rs.next())
			{
				if(cont>0)
					resultado+=",";
				resultado+=rs.getString(1);
				cont++;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCentrosCostoUsuario de SqlBaseUrtilidadesDao: "+e);
		}
		if(resultado.trim().equals(""))
			return ConstantesBD.codigoNuncaValido+"";
		return resultado;
	}

	
	
/**
 * 
 * @param con
 * @param institucion
 * @return
 */
	public static HashMap obtenerEmpresasInstitucion(Connection con, int institucion) 
	{
		String consulta="";
		HashMap resultado=new HashMap();
		try
		{
			consulta = "SELECT " +
							"	codigo, " +
							"	razon_social, " +
							"	digito_verificacion, " +
							"	vigente, " +
							"	institucion_publica as tipoins " +
							" from empresas_institucion where institucion ='"+institucion+"'";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEmpresasInstitucion de SqlBaseUrtilidadesDao: "+e+"\n\n\n"+consulta+"\n\n\n");
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreInstitucion(Connection con, int institucion) {
		
		String consulta="";
		HashMap resultado=new HashMap();
		try
		{
			consulta = "SELECT razon_social as nombre from instituciones where codigo ='"+institucion+"'";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreInstitucion de SqlBaseUrtilidadesDao: "+e+"\n\n\n"+consulta+"\n\n\n");
		}
		
		return resultado.get("nombre_0").toString();
		
	}

	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @return
	 */
	public static String obtenerDescripcionEstadoHC(Connection con, String codigoEstado)
	{
		try
		{
			String consulta = "SELECT nombre from estados_sol_his_cli where codigo="+codigoEstado;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDescripcionEstadoHC de SqlBaseUrtilidadesDao: "+e);
		}
		return "";
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static HashMap consultarDetalleAjustesImpresion(Connection con, String codigoAjuste)
	{
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		try
		{
			String consulta = "SELECT " +
									"adfe.factura as factura," +
									"s.consecutivo_ordenes_medicas as solicitud," +
									"CASE WHEN dfs.servicio is null then getdescarticulo(dfs.articulo) else getnombreservicio(dfs.servicio,0) end as artserv," +
									"adfe.valor_ajuste as valorajuste," +
									"cac.descripcion as concepto, " +
									"dfs.articulo as codigo_art, "+
									"dfs.servicio as codigo_ser "+
								"FROM " +
									"ajus_det_fact_empresa adfe " +
								"INNER JOIN " +
									"det_factura_solicitud dfs on(adfe.det_fact_solicitud=dfs.codigo) " +
								"INNER JOIN " +
									"solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
								"INNER JOIN " +
									"concepto_ajustes_cartera cac on (adfe.concepto_ajuste=cac.codigo and adfe.institucion=cac.institucion) " +
								"WHERE " +
								    "adfe.valor_ajuste > 0  AND adfe.codigo="+codigoAjuste;
			
			logger.info("consultarDetalleAjustesImpresion "+consulta);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDetalleAjustesImpresion de SqlBaseUrtilidadesDao: ", e);
		}
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static HashMap consultarEncabezadoAjuste(Connection con, String codigoAjuste)
	{
		HashMap resultado=new HashMap();
		resultado.put("numRegistros", "0");
		try
		{
			String consulta = "SELECT DISTINCT  " +
									" case when ae.tipo_ajuste in (1,3,5) then 'Debito' else 'Credito' end as nombretipoajuste," +
									" ae.consecutivo_ajuste as consecutivoajuste," +
									" getnombreusuario(ae.usuario) as usuarioelaboracion," +
									" to_char(ae.fecha_elaboracion,'dd/mm/yyyy') as fechaelaboracion, " +
									" case when castigo_cartera="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as castigocartera," +
									" ec.descripcion as nombreestado, " +
									" case when aea.fecha_aprobacion is null then '' else to_char(aea.fecha_aprobacion,'dd/mm/yyyy') end as fechaaprobacion," +
									" getnombreusuario(aea.usuario) as usuarioaprobacion," +
									" ae.valor_ajuste as valorajuste," +
									" case when ae.cuenta_cobro is null then getnombreconvenio(f.convenio) else getnombreconvenio(cc.convenio) end as nombreconvenio," +
									" ae.observaciones as observaciones, " +
									" case when ajuste_reversado ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as ajustereversado  " +
								"FROM " +
									"ajustes_empresa ae " +
								"INNER JOIN " +
									"estados_cartera ec on (ae.estado=ec.codigo) " +
								"INNER JOIN " +
									"ajus_fact_empresa afe on(afe.codigo=ae.codigo) " +
								"LEFT OUTER JOIN " +
									"ajus_empresa_aprobados aea on(ae.codigo=aea.codigo_ajuste) " +
								"LEFT OUTER JOIN " +
									"cuentas_cobro cc on(ae.cuenta_cobro=cc.numero_cuenta_cobro and ae.institucion=cc.institucion) " +
								"LEFT OUTER JOIN " +
									"facturas f on(afe.factura=f.codigo) " +
								"WHERE " +
									"ae.codigo="+codigoAjuste;
			
			logger.info("consultarEncabezadoAjuste / "+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),false,false);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarEncabezadoAjuste de SqlBaseUrtilidadesDao: ", e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList obtenerMedicosInstitucion(Connection con, String codigoInstitucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//Agrego la convencion a la busqueda
			String consulta = " SELECT mi.codigo_medico,getnombrepersona(mi.codigo_medico),m.convencion from medicos_instituciones mi inner join medicos m  on (m.codigo_medico=mi.codigo_medico) where codigo_institucion = "+codigoInstitucion;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta+" order by 2 desc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoMedico",rs.getObject(1));
				mapa.put("medico",rs.getObject(2));
				mapa.put("convencion",rs.getObject(3));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMedicosInstitucion de SqlBaseUtilidadesDao: "+e);
		}
		return resultado; 
	}

	
	/**
	 * Mï¿½todo para Extraer el numero de embarazos de la funcionalidad informacion del parto.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerNrosEmbarazosInformacionParto(Connection con, int codigoPersona)
	{
		HashMap resultado=new HashMap();
		String numeros = "";
		try
		{
			String consulta = "	SELECT numero_embarazo as embarazo 	" +
							  "		   FROM informacion_parto  		" +
							  "				 WHERE  codigo_paciente = " + codigoPersona;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			
			int rw = UtilidadCadena.vInt(resultado.get("numRegistros")+"");
			for (int i = 0; i < rw; i++)
			{
				if (numeros.equals(""))
					numeros+=resultado.get("embarazo_" + i);
				else
					numeros+= ConstantesBD.separadorSplit+resultado.get("embarazo_" + i);					
			}
			 return numeros;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNrosEmbarazosInformacionParto de SqlBaseUrtilidadesDao: "+e);
			return numeros;
		}
	}
	
	/**
     * Mï¿½todo que consulta la informacion del paciente que se coloca en cada una
     * de las fichas de epidemiologï¿½a
     * @param con
     * @param codigoPaciente
     * @return
     */
	public static ResultSetDecorator consultarDatosPacienteFicha(Connection con, int codigoPaciente)
	{

	    String consultaCad =  "" +
   		"SELECT  per.primer_nombre, per.segundo_nombre,											" +
		"		 per.primer_apellido, per.segundo_apellido,										" +
		"		 dep.nombre AS dep_nacimiento, ciu.nombre AS ciu_nacimiento,					" +
		"		 dep2.nombre AS dep_vivienda, ciu2.nombre AS ciu_vivienda,						" +
		"		 per.direccion AS direccion_paciente, per.telefono AS telefono_paciente,		" +
		"		 per.fecha_nacimiento, per.sexo, per.estado_civil, per.numero_identificacion, 	" +
		
		"		 bar.nombre AS barrio, pac.zona_domicilio AS zonaDomicilio, 					" +
		"		 ocup.nombre AS ocupacionPaciente, per.tipo_identificacion AS tipoId, 			" +
		"		 conv.nombre AS aseguradora, regs.nombre AS regimenSalud, 						" +
		"		 pac.etnia AS etnia									 							" +
		"		 FROM personas per 																" +
		"			  INNER JOIN departamentos dep ON ( dep.codigo=per.codigo_departamento_nacimiento ) 		" +
		"			  INNER JOIN ciudades ciu ON ( ciu.codigo_ciudad=per.codigo_ciudad_nacimiento AND ciu.codigo_departamento=per.codigo_departamento_nacimiento ) " +
		"			  INNER JOIN departamentos dep2 ON ( dep2.codigo=per.codigo_departamento_vivienda  ) 		" +
		"			  INNER JOIN ciudades ciu2 ON  ( ciu2.codigo_ciudad=per.codigo_ciudad_vivienda AND ciu2.codigo_departamento=per.codigo_departamento_vivienda ) 		" +
		"			  INNER JOIN barrios bar ON ( per.codigo_ciudad_vivienda=bar.codigo_ciudad AND				" +
		"										  per.codigo_barrio_vivienda=bar.codigo_barrio 	AND per.codigo_departamento_vivienda=bar.codigo_departamento )" +
		"			  INNER JOIN pacientes pac ON ( per.codigo=pac.codigo_paciente  ) 							" +
		"			  INNER JOIN ocupaciones ocup ON ( pac.ocupacion=ocup.codigo  )								" +
		"			  INNER JOIN cuentas cue ON ( cue.codigo_paciente = per.codigo ) 							" +
		"			  INNER JOIN sub_cuentas sc ON ( sc.ingreso = cue.id_ingreso AND sc.nro_prioridad = 1 )		" +
		"			  INNER JOIN contratos cont ON (cont.codigo = sc.contrato) 								" +
		"			  INNER JOIN convenios conv ON (conv.codigo = cont.convenio) 								" +
		"			  INNER JOIN tipos_regimen regs ON ( conv.tipo_regimen = regs.acronimo )					 " +
		"			  WHERE per.codigo = ? ";
		
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigoPaciente);
			
			return new ResultSetDecorator(consulta.executeQuery());
		}
		catch (SQLException sqle) {
			
			logger.error("Error En SqlBaseUtilidadesDao consultando (consultarDatosPacienteFicha) ["+sqle+"]");
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta la fecha de admisionde la cuenta de una cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public static String consultarFechaAdmisionCirugia(Connection con,String codigoCirugia)
	{
		try
		{
			String fechaAdmision = "";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaAdmisionCirugiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,codigoCirugia);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				fechaAdmision = rs.getString("fecha_admision");
			
			return fechaAdmision;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFechaAdmisionCirugia: "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo para obtener de la informacion del parto el numero del embarazo de acuerdo al codigo
	 * de la cirugï¿½a y al paciente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCirugia
	 * @return -1 si no existe de lo contrario retorna el nro del embarazo
	 */
	public static int obtenerNroEmbarazoCirugiaPaciente(Connection con, int codigoPaciente, String codigoCirugia)
	{
		int codigoCirugiaInt=UtilidadCadena.vInt(codigoCirugia);
		
		String consultaStr="SELECT numero_embarazo AS numero_embarazo " +
						   " 	FROM informacion_parto" +
						   "		WHERE codigo_paciente="+codigoPaciente+
						   "			AND cirugia="+codigoCirugiaInt;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("numero_embarazo");
			}
			else
			{
				return -1;
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNroEmbarazoCirugiaPaciente de SqlBaseUtilidadesDao: "+e);
			return -1;
		}	
	}
	
	
	
	/**
	 * Mï¿½todo que consulta los Tipos de Identificacion 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarTiposidentificacion(Connection con,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarTiposIdentificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTiposidentificacion de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	/**
	 * M?todo que consulta las Ciudades 
	 * @param con
	 * @return
	 */
	public static HashMap consultarCiudades(Connection con)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCiudadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCiudades de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}	
	
	public static HashMap consultarTiposSangre(Connection con)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarTiposSangreStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTiposSangre de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	public static HashMap consultarEstadosCiviles(Connection con)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarEstadosCivilesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarEstadosCiviles de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	public static HashMap consultarZonasDomicilio(Connection con)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarZonasDomicilioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno; 
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarZonasDomicilio de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	public static HashMap consultarOcupaciones(Connection con)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarOcupacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarOcupaciones de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerNumeroMedicamentosSolicitud(Connection con, String numeroSolicitud)
	{
		int numeroSolicitudes=0;
		try
		{
			String cadena="SELECT count(1) from detalle_solicitudes ds inner join articulo a on(a.codigo=ds.articulo) inner join naturaleza_articulo na ON (na.acronimo=a.naturaleza and na.institucion=a.institucion) where na.es_medicamento='"+ConstantesBD.acronimoSi+"' and ds.numero_solicitud="+numeroSolicitud;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				numeroSolicitudes = rs.getInt(1);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFechaAdmisionCirugia: "+e);
			numeroSolicitudes=0;
		}
		return numeroSolicitudes;
	}

	
	/**
	 * Mï¿½todo para saber si el tipo y nï¿½mero de identificaciï¿½n del paciente
	 * existe en usuario capitados, si existe retorna el codigo sino retorna -1
	 * @param con
	 * @param tipoIdentificacion
	 * @param nroIdentificacion
	 * @return
	 */
	public static int getCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String nroIdentificacion)
	{
		String consultaStr="SELECT codigo AS codigo " +
						   " 	FROM usuarios_capitados" +
						   "		WHERE numero_identificacion= ?"+
						   "			AND tipo_identificacion= ?";
		
		try
		{
			//logger.info("getCodigoUsuarioCapitado-->"+consultaStr+" -->nroId->"+nroIdentificacion+" tipo->"+tipoIdentificacion);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, nroIdentificacion);
			pst.setString(2, tipoIdentificacion);

			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				return rs.getInt("codigo");
			}
			else
			{
				return -1;
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getCodigoUsuarioCapitado de SqlBaseUtilidadesDao: "+e);
			return -1;
		}	
	}
	
	/**
	 * Mï¿½todo que obtiene la informaciï¿½n de los convenio/contratos capitados, de acuerdo
	 * al codigo del usuario capitado enviado por parï¿½metro y la vigencia
	 * @param con
	 * @param codigoUsuarioCapitado
	 */
	public static HashMap convenioUsuarioCapitado(Connection con, int codigoUsuarioCapitado)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapaRetorno= new HashMap();
		try{
			mapaRetorno.put("numRegistros","0");
			String consultaStr=	"SELECT  cont.codigo AS codigo_contrato, " +
										"con.codigo AS codigo_convenio, " +
										"con.nombre AS nombre_convenio, " +
										"con.maneja_montos	AS maneja_montos, "+
										"con.tipo_regimen As codigo_tipo_regimen," +
										"getnomtiporegimen(con.tipo_regimen) AS nombre_tipo_regimen, "+
										"CASE WHEN con.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_pyp, "+
										"CASE WHEN con.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_capitado," +
										"CASE WHEN cont.numero_contrato IS NULL THEN 'SIN NUMERO CONTRATO' ELSE cont.numero_contrato END AS numero_contrato, "+
										"CASE WHEN cuc.clasificacion_socio_economica IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE cuc.clasificacion_socio_economica END AS clasificacion_socio_economica, " +
										"getnombreestrato(clasificacion_socio_economica) AS nombre_estrato, " +
										"CASE WHEN cuc.tipo_afiliado IS NULL THEN '' ELSE cuc.tipo_afiliado END AS tipo_afiliado, " +
										"getnombretipoafiliado(tipo_afiliado) AS nombre_tipo_afiliado, " +
										"CASE WHEN cuc.naturaleza_pacientes IS NULL THEN -1 ELSE cuc.naturaleza_pacientes END AS naturaleza_paciente, " +
										"np.nombre AS nombre_naturaleza "+
								"FROM usuarios_capitados uc " +
								"INNER JOIN conv_usuarios_capitados cuc ON(cuc.usuario_capitado=uc.codigo) "+
								"INNER JOIN contratos cont ON (cont.codigo=cuc.contrato) "+
								"INNER JOIN convenios con ON (con.codigo=cont.convenio) " +
								"LEFT JOIN naturaleza_pacientes np ON (np.codigo = cuc.naturaleza_pacientes) " +
								"WHERE uc.codigo = ? AND "+ 
								"(TO_DATE(?,'dd/MM/yyyy') BETWEEN cuc.fecha_inicial AND cuc.fecha_final) " +
								"ORDER BY cuc.fecha_inicial ASC ";
	
			
			pst= con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoUsuarioCapitado);
			pst.setString(2,UtilidadFecha.getFechaActual(con));
			rs=pst.executeQuery();
			int cont=0;
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapaRetorno.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaRetorno.put("numRegistros", cont+"");
		}
		catch (SQLException sqe)
		{
			logger.error("Error en convenioUsuarioCapitado: "+sqe);
		}
		catch (Exception e)
		{
			logger.error("Error en convenioUsuarioCapitado: "+e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("Error cerrando los objetos persistentes", se);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * Mï¿½todo implementado para consultar tipo id, numero id y nombre de las personas
	 * que tienen el mismo numero id
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 */
	public static HashMap personasConMismoNumeroId(Connection con,String numeroIdentificacion,String tipoIdentificacion)
	{
		try
		{
			//el tipo de identificacion no es requerido
			String consulta = personasConMismoNumeroIdStr;
			if(!tipoIdentificacion.equals(""))
				consulta += " AND tipo_identificacion <> '"+tipoIdentificacion+"'";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroIdentificacion);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en personasConMismoNumeroId: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que marca como atendido los registros de pacientes para triage
	 * del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static int actualizarPacienteParaTriageVencido(Connection con,String codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarPacienteParaTriageVencidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, codigoPaciente);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPacienteParaTriageVencido: "+e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo que condulta el codigo UPGD del centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static String getCodigoUPGDCentroAtencion(Connection con,int centroAtencion)
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			String  codigo = "";
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(getCodigoUPGDCentroAtencionStr));
			pst.setInt(1, centroAtencion);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigo = rs.getString("codigo_upgd");
			
			return codigo;
		}
		catch(SQLException e)
		{
			logger.error("Error en getCodigoUPGDCentroAtencion :"+e);
			return "";
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
	}

	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static String getLoginUsuarioSolicitaOrdenAmbulatoria(Connection con,String consecutivoOrden, int institucion)
	{
		String cadena="SELECT usuario_solicita as loginusuario FROM ordenes_ambulatorias where consecutivo_orden= ? and institucion=? ";
		String resultado="";
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, consecutivoOrden);
			pst.setString(2, institucion+"");
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado=rs.getString("loginusuario");
			
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en getLoginUsuarioSolicitaOrdenAmbulatoria :"+e);
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static HashMap consultarInformacionServicio(Connection con, String codigoServicio, int institucion)
	{
		String cadena="SELECT " +
							" s.codigo as codigo," +
							//Ya no aplica consultar el formulario porque el servicio ya puede tener N formularios
							" "+ConstantesBD.codigoNuncaValido+" as formulario," +
							" s.espos as espos," +
							" s.especialidad as especialidad," +
							" getnombreservicio(s.codigo,'"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+"') as nombre," +
							" 'false' as esExcepcion," +
							" getcodigopropservicio(s.codigo,'"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+"') as codigoPropietario," +
							" e.nombre as nombreEspecialidad," +
							" s.grupo_servicio as gruposervicio," +
							" s.toma_muestra as tomamuestra," +
							"s.tipo_servicio as tiposervicio," +
							" s.respuesta_multiple as respuestamultiple, " +
							" gs.acro_dias_urgente as acrodiasurgente, " +
							" gs.acro_dias_normal as acrodiasnormal " +
						" FROM servicios s " +
						" inner join grupos_servicios gs on (gs.codigo=s.grupo_servicio)" +
						" inner join especialidades e on(especialidad=e.codigo) " +
						" where s.codigo="+codigoServicio;
		
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarInformacionServicio: "+e);
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoEnfermedad
	 * @param institucion
	 * @return
	 */
	public static ArrayList consultarServiciosEnfermedadEpidemiologia(Connection con, String codigoEnfermedad, String institucion)
	{
		String cadena="SELECT codigoservicio from epidemiologia.vigidetalleservicios where institucion="+institucion+" and activo=1 and tiposolicitud="+ConstantesBD.codigoSolicitudLabInterna+"  and codigoenfnotificable ='"+codigoEnfermedad+"'";//" and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
		
		ArrayList codigos=new ArrayList();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				codigos.add(rs.getString(1));
			}
		}
		catch(SQLException e)
		{
		logger.error("Error en consultarServiciosEnfermedadEpidemiologia: "+e);
		}
		return codigos;
	}
	
	/**
	 * 
	 * @param con
	 * @param sexo
	 * @return
	 */
	public static String getDescripcionSexo(Connection con, int sexo)
	{
        String cadena="SELECT nombre as descripcion from sexo where codigo ="+sexo;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static int obtenerNumeroSolicitudesPypXCuenta(Connection con, String cuenta,String busquedaPYP)
	{
		String cadena="SELECT count(1) from solicitudes where cuenta='"+cuenta+"' and pyp='"+busquedaPYP+"'";
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());	
			if(rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerCajasCajero(Connection con, String loginUsuario, int codigoInstitucion, int codigoTipoCaja, int codigoCentroAtencion)
	{
		HashMap mapa= new HashMap();
		String cadena=	"SELECT " +
							"c.codigo as codigo, " +
							"c.consecutivo as consecutivo, " +
							"c.descripcion as descripcion, " +
							"c.centro_atencion as centro_atencion, " +
							"getnomcentroatencion(c.centro_atencion) as nombre_centro_atencion, " +
							"c.tipo AS tipo_caja " +
						"FROM " +
							"cajas c " +
						"INNER JOIN cajas_cajeros cc on (cc.caja=c.consecutivo and cc.institucion=c.institucion and cc.usuario='"+loginUsuario+"') " +
						"INNER JOIN administracion.centro_atencion ca on (ca.consecutivo = c.centro_atencion) " +
						"where " +
							"cc.activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"and c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
						
						if (codigoTipoCaja>0)
							cadena+="and c.tipo="+codigoTipoCaja;			
		
		logger.info(cadena);
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerCajas(Connection con, int codigoInstitucion, int codigoTipoCaja)
	{
		HashMap mapa= new HashMap();
		String cadena=	"SELECT " +
							"c.consecutivo as consecutivo, " +
							"c.descripcion as descripcion, " +
							"c.centro_atencion as centro_atencion, " +
							"getnomcentroatencion(c.centro_atencion) as nombre_centro_atencion " +
						"FROM " +
							"cajas c " +
						"where " +
							"c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"and c.tipo="+codigoTipoCaja+" " +
							"and c.institucion="+codigoInstitucion;
		PreparedStatementDecorator ps=null;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param numeroDocumento
	 * @return
	 */
	public static HashMap consultarDatosGeneralesPaciente(Connection con, String numeroDocumento)
	{
		String cadena="select " +
							" per.tipo_identificacion as tipoid," +
							" getnombretipoidentificacion(per.tipo_identificacion) as desctipoid," +
							" per.numero_identificacion as numeroidentificacion," +
							" case when per.codigo_departamento_nacimiento is null then '' else per.codigo_departamento_nacimiento end as coddepnacimiento, " +
							" getnombredepto(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento) as depnacimiento," +
							" case when per.codigo_ciudad_nacimiento is null then '' else per.codigo_ciudad_nacimiento end as codciunacimiento," +
							" getnombreciudad(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento,per.codigo_ciudad_nacimiento) as ciunacimiento," +
							" per.primer_nombre as primernombre," +
							" case when per.segundo_nombre is null then '' else per.segundo_nombre end as segundonombre," +
							" per.primer_apellido as primerapellido," +
							" per.segundo_apellido as segundoapellido," +
							" per.sexo as sexo,getdescripcionsexo(per.sexo) as descsexo," +
							" per.fecha_nacimiento as fechanacimiento," +
							" per.telefono as telefono," +
							" per.email as email," +
							" per.codigo_departamento_vivienda as coddepres," +
							" getnombredepto(per.codigo_pais_vivienda,per.codigo_departamento_vivienda) as depres," +
							" per.codigo_ciudad_vivienda as codciures," +
							" getnombreciudad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda,per.codigo_ciudad_vivienda) as ciures," +
							" case when per.tipo_identificacion='PA' then 'Extranjero' else 'Colombiano' end as nacionalidad," +
							" per.direccion as direccion," +
							" per.estado_civil as acroestadocivil," +
							" ec.nombre as estadocivil," +
							" pac.ocupacion as ocupacion," +
							" pac.tipo_sangre as tiposangre," +
							" ts.nombre as desctiposangre," +
							" pac.zona_domicilio as zonadomicilio," +
							" zd.nombre as desczonadomicilio " +
						" from personas per " +
						" inner join estados_civiles ec on(per.estado_civil=ec.acronimo) " +
						" inner join pacientes pac on(per.codigo=pac.codigo_paciente) " +
						" inner join tipos_sangre ts on(pac.tipo_sangre=ts.codigo) " +
						" inner join zonas_domicilio zd on(pac.zona_domicilio=zd.acronimo) " +
						" where per.numero_identificacion='"+numeroDocumento+"'";
		
		HashMap mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
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
	 * @param codigoPersona
	 * @param fecha
	 * @param institucion
	 * @return
	 */
	public static InfoDatosString obtenerTipoRegimenSolicitudUsuarioCapitado(Connection con, String codigoPersona, String fecha, int institucion)
	{
		String consulta=	"SELECT " +
								"conv.tipo_regimen as cod, " +
								"getnomtiporegimen(conv.tipo_regimen) as nombre " +
							"FROM " +
								"usuario_x_convenio u " +
								"INNER JOIN contratos c ON (c.codigo=u.contrato) " +
								"INNER JOIN convenios conv ON (c.convenio=conv.codigo) " +
							"WHERE " +
								"persona="+codigoPersona+" " +
								" AND c.fecha_inicial <= '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' AND c.fecha_final>= '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' ";
		InfoDatosString info= new InfoDatosString();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("obtenerTipoRegimenSolicitudUsuarioCapitado-->"+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				info.setCodigo(rs.getString("cod"));
				info.setNombre(rs.getString("nombre"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap obtenerCentrosAtencion(Connection con, int codigoInstitucion) 
	{
		HashMap mapa= new HashMap();
		String cadena=	" SELECT consecutivo AS consecutivo, descripcion AS descripcion from centro_atencion where cod_institucion=? and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" order by descripcion";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo encargado de obtener los proveedores para un movimiento almacen consignacion
	 * Si el codproveedor es -1, no tenemos en cuenta el where para poder cargar TODOS los
	 * proveedores
	 * Se ordena alfï¿½beticamente para solucionar la tarea: 55136
	 * @param con
	 * @param codProveedor
	 * @return mapa HashMap
	 */
	public static HashMap obtenerProveedores(Connection con, int codProveedor) 
	{
		HashMap mapa= new HashMap();
		String cadena=	" SELECT DISTINCT " +
				" t.numero_identificacion AS numId, " +
				" t.descripcion AS descripcion" +
				" FROM terceros t  " +
				" INNER JOIN convenio_proveedor cp on(cp.proveedor=t.numero_identificacion) ";
		if(codProveedor!=ConstantesBD.codigoNuncaValido)
		{
			cadena +=" WHERE cp.proveedor = '" +codProveedor+"' ";
		}
		cadena += "ORDER BY descripcion";
		PreparedStatementDecorator ps=null;
		try
		{
			logger.info("===> Aquï¿½ obtenemos a los proveedores: "+cadena);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.setInt(1, codProveedor);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true,true );
		} 
		catch (SQLException e) 
		{
			logger.error("===> Error al consultar los proveedores");
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable
	 * @param descripcion
	 * @param anioVigencia 
	 * @return
	 */
	public static HashMap obtenerCuentasContables(Connection con, int codigoInstitucion, String cuentaContable, String descripcion, String anioVigencia, String naturCuenta) 
	{
		HashMap mapa= new HashMap();
		String cadena=	"SELECT " +
							"codigo AS codigo, " +
							"anio_vigencia as anio_vigencia," +
							"cuenta_contable AS cuenta_contable, " +
							"descripcion as descripcion, " +
							"naturaleza_cuenta AS naturaleza_cuenta, " +
							"manejo_terceros AS manejo_terceros, " +
							"manejo_centros_costo AS manejo_centros_costo, " +
							"manejo_base_gravable AS manejo_base_gravable " +
						"from " +
							"cuentas_contables " +
						"where " +
							"institucion=? " +
							"and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		
		if(!cuentaContable.trim().equals(""))
			cadena+=" and upper (cuenta_contable) like upper('%"+cuentaContable+"%') ";
		if(!descripcion.trim().equals(""))
			cadena+=" and upper (descripcion) like upper('%"+descripcion+"%') "; 
		if(!anioVigencia.trim().equals(""))
			cadena+=" and anio_vigencia = '"+anioVigencia+"'";
		
		//Adicion para filtar la busqueda a cuentas debito o credito
		if(!naturCuenta.trim().equals(""))
			cadena+=" and naturaleza_cuenta = '"+naturCuenta+"'";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by anio_vigencia,convertiranumero(cuenta_contable) ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
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
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerNombreArticulo(Connection con, int codigoArticulo)
	{
		String cadena="select descripcion from articulo where codigo=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	
	/**
	 * 
	 * @param con
	 * @param unidosis
	 * @return
	 */
	public static String obtenerUnidadMedidadUnidosisArticulo(Connection con, String unidosis)
	{
		String cadena="select unidad_medida  from unidosis_x_articulo where codigo=?";
		
		if(UtilidadTexto.isEmpty(unidosis)){
			return "";
		}
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, Integer.parseInt(unidosis));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarTerceros(Connection con, int institucion)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=	"SELECT codigo as codigo,numero_identificacion as numeroid,descripcion as descripcion from terceros where institucion = ?";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
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
	 * @param consecutivoCaja
	 * @return
	 */
	public static int obtenerTipoCaja(Connection con, int consecutivoCaja)
	{
		String cadena="SELECT tipo from cajas where consecutivo ="+consecutivoCaja;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Mï¿½todo encargado de consultar si una forma farmacï¿½utica estï¿½ siendo utilizada o no
	 * @modificated Felipe Pï¿½rez Granda, 3 Dic 2008
	 * @param con
	 * @param consecutivo
	 * @return boolean true/false
	 */
	public static boolean esFormaFarmaceuticaUsada(Connection con, String consecutivo)
	{
		/*
		 * Para la soluciï¿½n de la tarea 54082 se modificarï¿½ la cadena de tal manera que se pueda ejecutar bien la consulta:
		 * La cadena en este momento se encuentra asi: 
		 * String cadena="DELETE FROM forma_farmaceutica where acronimo="+consecutivo;
		 * Al consultar la informaciï¿½n en la BD me enterï¿½ que el filtro por el acrï¿½nimo no se estï¿½ haciendo de una forma correcta,
		 * ya que los resultados comprueban que ï¿½ste campo es un "VARCHAR" y no un "INTEGER", entonces se agregarï¿½n las comillas.
		 * 
		 * shaio_jul3108=# \d forma_farmaceutica
		 * Table "inventarios.forma_farmaceutica"
		 * Column    |          Type          | Modifiers
		 * -------------+------------------------+-----------
		 * acronimo    | character varying(10)  | not null
		 * nombre      | character varying(128) | not null
		 * institucion | integer                | not null
		 * Indexes:
		 * "pk_forma_farmaceutica" PRIMARY KEY, btree (acronimo, institucion)
		 * Foreign-key constraints:
		 * "fk_forma_farma_inst" FOREIGN KEY (institucion) REFERENCES instituciones(codigo)
		 * 
		 */
		//String cadena="DELETE FROM forma_farmaceutica where acronimo="+consecutivo;
		String cadena = "DELETE FROM forma_farmaceutica where acronimo='"+consecutivo+"' ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return !(ps.executeUpdate()>0);
		}
		catch (SQLException e)
		{
			//logger.info("La forma farmaceutica "+consecutivo+" se estï¿½ usando en otras funcionalidades: "+e);
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean esClaseInventarioUsada(Connection con, int consecutivo)
	{
		String cadena="DELETE FROM inventarios.clase_inventario where codigo="+consecutivo;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return !(ps.executeUpdate()>0);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param clase
	 * @return
	 */
	public static boolean esGrupoInventarioUsada(Connection con, int consecutivo, int clase)
	{
		String cadena="DELETE FROM inventarios.grupo_inventario where codigo="+consecutivo + " AND clase=" + clase;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return !(ps.executeUpdate()>0);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param clase
	 * @return
	 */
	public static boolean esSubgrupoInventarioUsada(Connection con, int consecutivo, int subgrupo , int grupo, int clase)
	{
		String cadena="DELETE FROM inventarios.subgrupo_inventario where codigo="+consecutivo + " AND subgrupo=" + subgrupo + " AND grupo=" + grupo + " AND clase=" + clase;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return !(ps.executeUpdate()>0);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerTercerosInstitucionActivos(Connection con, int institucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar
			String consulta = "SELECT codigo,numero_identificacion,descripcion from terceros where institucion="+institucion+" and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" order by descripcion desc ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("numeroid",rs.getObject("numero_identificacion"));
				mapa.put("descripcion",rs.getObject("descripcion"));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTercerosInstitucionActivos de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}	
	
	
	/**
	 * Mï¿½todo encargado de consultar los terceros que se encuentren activos
	 * y no se encuentren relacionados a alguna empresa.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return
	 * ArrayList<HashMap<String, Object>>
	 * -----------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * -----------------------------------
	 * codigo,numeroid,descripcion
	 */
	public static ArrayList obtenerTercerosSinEmpresaInstitucionActivos(Connection con, int institucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar
			//String consulta = "SELECT codigo,numero_identificacion,descripcion from terceros where institucion="+institucion+" and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" order by descripcion desc ";
			String consulta = "SELECT t.codigo,t.descripcion,t.numero_identificacion  FROM terceros t WHERE t.codigo NOT IN (SELECT e.tercero FROM empresas e ) AND t.institucion = "+institucion+" AND t.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
			logger.info("\n cadena --> "+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("numeroid",rs.getObject("numero_identificacion"));
				mapa.put("descripcion",rs.getObject("descripcion"));
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTercerosSinEmpresaInstitucionActivos de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}
	
	
	/**
	 * Mï¿½todo que consulta las unidades funcionales por institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarUnidadesFuncionales(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "SELECT acronimo,descripcion,institucion FROM unidades_funcionales WHERE institucion = ? ORDER BY descripcion";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(campos.get("institucion").toString()));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUnidadesFuncionales: "+e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codEstadoCuenta
	 * @return
	 */
	public static int obtenerCuentaDadoPacienteEstado(Connection con, int codPaciente, int codEstadoCuenta)
	{
		String consulta="SELECT max(id) as cu from cuentas where  codigo_paciente=? and estado_cuenta=?";
		try
		{
			logger.info("obtenerCuentaDadoPacienteEstado-->"+consulta+" codPac->"+codPaciente+" estadio->"+codEstadoCuenta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codPaciente);
			pst.setInt(2,codEstadoCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt("cu");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCuentaDadoPacienteEstado: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Mï¿½todo que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerTipoRegimenConvenio(Connection con,String codigoConvenio)
	{
		try
		{
			String tipoRegimen = "" ;
			if(!UtilidadTexto.isEmpty(codigoConvenio)   && codigoConvenio.matches("\\d+")){
				String consulta = " SELECT tipo_regimen || '-' || getnomtiporegimen(tipo_regimen) AS tipo_regimen FROM convenios WHERE codigo = ?";
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(codigoConvenio));

				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

				if(rs.next()){
					tipoRegimen = rs.getString("tipo_regimen");
				}
			}
			return tipoRegimen;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoRegimenConvenio: "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerCodigoTipoRegimenConvenio(Connection con,String codigoConvenio)
	{
		try
		{
			String tipoRegimen = "";
			String consulta = " SELECT tipo_regimen AS tipo_regimen FROM convenios WHERE codigo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(codigoConvenio));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				tipoRegimen = rs.getString("tipo_regimen");
			
			return tipoRegimen;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoTipoRegimenConvenio: "+e);
			return "";
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param todos 
	 * @return
	 */
	public static ArrayList obtenerContratos(Connection con, int codigoConvenio, boolean todos, boolean validarNumeroContrato, boolean pendientesFinalizar,int tipoBD) 
	{
		ArrayList<HashMap<String, Object>> contratos = new ArrayList<HashMap<String,Object>>();
		String cadena="SELECT  " +
								" codigo as codigo," +
								" convenio as convenio," +
								" fecha_inicial as fechainicial," +
								" fecha_final as fechafinal," +
								" numero_contrato as numerocontrato, " +
								" valor as valor, " +
								" acumulado as acumulado," +
								" tipo_pago as tipopago, " +
								" upc as upc, " +
								" porcentaje_pyp as porcentajepyp, " +
								" contrato_secretaria as contratosecretaria, " +
								" porcentaje_upc as porcentajeupc, " +
								" base as base, " +
								" to_char(fecha_inicial, 'DD/MM/YYYY') as fechainicialbd, " +
								" to_char(fecha_final, 'DD/MM/YYYY') as fechafinalbd," +
								" paciente_paga_atencion as pacientepagaatencion " +
						" from contratos " +
						" where convenio=?";
		if(!todos)
		{
			cadena=cadena+" and (current_date between fecha_inicial and fecha_final) ";
		}
		else
		{
			cadena=cadena+" and (current_date <= fecha_final) ";
		}
		if(validarNumeroContrato)
		{
			switch(tipoBD)
			{
				case DaoFactory.POSTGRESQL:
				cadena+= " AND (numero_contrato is not null and numero_contrato <> '') ";
				break;
				case DaoFactory.ORACLE:
					cadena+= " AND numero_contrato is not null  ";
				break;
			}
			
		}
			
		
		cadena=cadena+" order by numero_contrato desc";
		logger.info("consulta contratos: "+cadena.replace("?", codigoConvenio+""));
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigo", rs.getString("codigo"));
				mapa.put("convenio", rs.getString("convenio"));
				mapa.put("fechainicial", rs.getString("fechainicial"));
				mapa.put("fechafinal", rs.getString("fechafinal"));
				mapa.put("numerocontrato", rs.getString("numerocontrato"));
				mapa.put("valor", rs.getString("valor"));
				mapa.put("acumulado", rs.getString("acumulado"));
				mapa.put("tipopago", rs.getString("tipopago"));
				mapa.put("upc", rs.getString("upc"));
				mapa.put("porcentajepyp", rs.getString("porcentajepyp"));
				mapa.put("contratosecretaria", rs.getString("contratosecretaria"));
				mapa.put("porcentajeupc", rs.getString("porcentajeupc"));
				mapa.put("base", rs.getString("base"));
				mapa.put("fechainicialbd", rs.getString("fechainicialbd"));
				mapa.put("fechafinalbd", rs.getString("fechafinalbd"));
				mapa.put("pacientepagaatencion", rs.getString("pacientepagaatencion"));
				
				contratos.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return contratos;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> obtenerNaturalezasEventosCatastroficos(Connection con)
	{
		HashMap<String, Object> mapa=new HashMap<String,Object>();
		HashMap<String, Object> mapaTemp=new HashMap<String,Object>();
		mapaTemp.put("numRegistros", "0");
		mapa.put(ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoNatural,mapaTemp );
		mapa.put(ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTecnologico,mapaTemp );
		mapa.put(ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTerrorista,mapaTemp );
		String consulta="";
		PreparedStatementDecorator ps=null;
		try
		{
			consulta="SELECT codigo,descripcion from nat_evento_catastrofico where tipo_evento = '"+ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoNatural+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa.put(ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoNatural,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())) );

			consulta="SELECT codigo,descripcion from nat_evento_catastrofico where tipo_evento = '"+ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTecnologico+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa.put(ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTecnologico,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())) );

			consulta="SELECT codigo,descripcion from nat_evento_catastrofico where tipo_evento = '"+ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTerrorista+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa.put(ConstantesIntegridadDominio.acronimoTipoEventoCatastroficoTerrorista,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())) );

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
	 * @param codigoPaciente
	 * @param idIngreso
	 * @return
	 */
	public static HashMap<String, Object> obtenerInformacionReferenciaTramite(Connection con, int codigoPaciente, int idIngreso)
	{
		String cadena="";
		PreparedStatementDecorator ps=null;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			cadena="SELECT r.numero_referencia as numeroreferencia,r.tipo_referencia as tiporeferencia,r.profesional_salud as profesional,to_char(r.fecha_referencia,'dd/mm/yyyy') as fecha,c.descripcion as ciudad, p.descripcion as pais from referencia r inner join ciudades c on(c.codigo_departamento=r.codigo_departamento and c.codigo_ciudad=r.codigo_ciudad and c.codigo_pais=r.codigo_pais) inner join paises p on(p.codigo_pais=r.codigo_pais) where codigo_paciente=? and ingreso=? and r.estado <>'"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPaciente);
			ps.setInt(2, idIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				mapa.put("numeroReferencia", rs.getString("numeroreferencia"));
				mapa.put("tipoReferencia", rs.getString("tiporeferencia"));
				mapa.put("profesionalRefiere", rs.getString("profesional"));
				mapa.put("fechaRefiere", rs.getString("fecha"));
				mapa.put("ciudadRefiere", rs.getString("ciudad"));
				mapa.put("paisRefiere", rs.getString("pais"));
				String cadenaTramite="SELECT intsir.descripcion as institucionreferida,c.descripcion as ciudadreferida,p.descripcion as paisreferida,hsir.fecha_tramite as fecha from instit_tramite_referencia itr inner join his_serv_inst_referencia hsir on(hsir.numero_referencia_tramite=itr.numero_referencia_tramite and hsir.institucion_referir=itr.institucion_referir and hsir.institucion=itr.institucion) inner join ciudades c on(c.codigo_departamento=itr.departamento_referir and c.codigo_ciudad=itr.ciudad_referir and c.codigo_pais=itr.pais_referir) inner join paises p on(p.codigo_pais=itr.pais_referir) inner join instituciones_sirc intsir on (intsir.codigo=itr.institucion_referir and intsir.institucion=itr.institucion) where hsir.estado='ACE' and itr.numero_referencia_tramite=? order by fecha_tramite desc "+ValoresPorDefecto.getValorLimit1()+" 1";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTramite,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, rs.getInt("numeroreferencia"));
				ResultSetDecorator rs2=new ResultSetDecorator(ps.executeQuery());
				if(rs2.next())
				{
					mapa.put("institucionReferida", rs2.getString("institucionreferida"));
					mapa.put("ciudadReferida", rs2.getString("ciudadreferida"));
					mapa.put("fechaReferida", rs2.getString("fecha"));
					mapa.put("paisReferida", rs2.getString("paisreferida"));
				}
				else
				{
					mapa.put("institucionReferida", "");
					mapa.put("ciudadReferida", "");
					mapa.put("fechaReferida", "");
					mapa.put("paisReferida", "");
				}
				mapa.put("numRegistros", "1");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso 
	 * @param convenio 
	 * @param tipoRegimen 
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerNaturalezasPaciente(Connection con, String tipoRegimen, int convenio, int viaIngreso)
	{
		String cadena=" SELECT DISTINCT np.codigo AS codigo,np.nombre AS nombre from naturaleza_pacientes np";
		
		//Si se ingresa convenio se debe filtrar por monto de cobro
		if(convenio>0)
		{
			// davgommo MT 6932, Se cambia la consulta porque no manejaba el caso de pacientes sin naturaleza
			cadena="SELECT DISTINCT CASE WHEN dmc.naturaleza_codigo IS NULL THEN "+ ConstantesBD.codigoNuncaValido +" ELSE np.codigo END AS codigo, " +
					"  CASE WHEN dmc.naturaleza_codigo IS NULL THEN '"+ ConstantesBD.valorNaturalezaPacientesSinNaturaleza + "' ELSE np.nombre END AS nombre " +
					" from naturaleza_pacientes np right join detalle_monto dmc on (dmc.naturaleza_codigo=np.codigo) inner join montos_cobro mc on(mc.codigo=dmc.monto_codigo AND " +
					" mc.convenio = "+convenio+" "+(viaIngreso>0?" and dmc.via_ingreso_codigo = "+viaIngreso+" ":"")+" and dmc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+") ";
		}
			
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
			logger.info("CONSULTA NATURALEZA PACIENTE: >>>> " + cadena);
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
			}
			return resultado;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
		
	/**
	 * 
	 * @param con
	 * @param viaIngreso 
	 * @param convenio 
	 * @param tipoRegimen 
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
			Connection con, String tipoRegimen, int convenio, int viaIngreso,
			String codigoTipoAfiliado, int codigoEstratoSocial, int tipoBD, String fechaReferencia)
	{
		String cadena = "";
		Vector<InfoDatosString> resultado = new Vector<InfoDatosString>();
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try {
			cadena += "SELECT CASE WHEN np.codigo is null THEN " + ConstantesBD.codigoNuncaValido + " ELSE np.codigo END as codigo, " +
				"CASE WHEN np.codigo is null THEN '"+ ConstantesBD.valorNaturalezaPacientesSinNaturaleza + "' ELSE np.nombre END as nombre " +
				"FROM ( " +
				"SELECT DISTINCT dmc.naturaleza_codigo " +
				"FROM detalle_monto dmc " +
				"INNER JOIN " +
				"(SELECT * FROM montos_cobro mc " +
				"WHERE mc.convenio        = " + convenio +
				" AND mc.vigencia_inicial = GETVIGENCIACONVENIO(" + convenio + ", '" + fechaReferencia + "' ) " +
				" ORDER BY mc.codigo DESC) mc " +
				"ON (mc.codigo = dmc.monto_codigo " +
				"AND mc.convenio = " + convenio + 
				" AND dmc.via_ingreso_codigo = " + viaIngreso +
				" AND dmc.tipo_afiliado_codigo = '" + codigoTipoAfiliado + "' " +
				" AND dmc.estrato_social_codigo =  " + codigoEstratoSocial +
				" AND dmc.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + ")) dmc2 " +
				"LEFT JOIN naturaleza_pacientes np " +
				"ON (dmc2.naturaleza_codigo=np.codigo) ";

			ps = new PreparedStatementDecorator(con.prepareStatement(cadena+" order by np.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(ps.executeQuery());
			logger.info("CONSULTA NATURALEZA PACIENTE: >>>> " + cadena);
			
			while(rs.next()) {
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
			}
			return resultado;
		} catch (SQLException e) {
			logger.error("obtenerNaturalezasPacienteXTipoAfiliadoEstrato: " + e);
		} finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String getNombreEstadoCartera(Connection con, int codigo)
	{
		String consulta="SELECT descripcion AS descrip FROM estados_cartera where codigo=?";
		try
		{
			ResultSetDecorator resultado=UtilidadBD.ejecucionGenericaResultSetDecorator(con, codigo, 1, consulta);
			if(resultado.next())
			{
				return resultado.getString("descrip");
			}
			else
			{
				return "";
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nombre del concepto de la menstruaciï¿½n : "+e);
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion)
	{
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT codigo_cobertura,descrip_cobertura from cobertura where institucion = ? and activo='"+ConstantesBD.acronimoSi+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param tiposArea
	 * @param todos
	 * @param area
	 * @param filtro_externos 
	 * @return
	 */
	public static HashMap obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion, boolean filtro_externos)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String tipoArea=tiposArea;
		if(tiposArea.indexOf(ConstantesBD.separadorSplit)>0)
		{
			String[] temp=tiposArea.split(ConstantesBD.separadorSplit);
			tipoArea=temp[0];
			for(int i=1;i<temp.length;i++)
			{
				tipoArea+=","+temp[i];
			}
		}
		String cadena="select cc.codigo as codigo,cc.identificador as identificador,cc.nombre as nomcentrocosto,cc.tipo_area as codigotipoarea,ta.nombre as nombretipoarea,case when es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo,identificador,case when manejo_camas = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as manejacamas,cc.unidad_funcional as unidadfuncional,uf.descripcion as descunidadfuncional,cc.centro_atencion as codcentroatencion,getnomcentroatencion(cc.centro_atencion) as nomcentroatencion, cc.tipo_entidad_ejecuta AS tipoentidad from centros_costo cc inner join tipos_area ta on(cc.tipo_area=ta.codigo) inner join unidades_funcionales uf on(uf.acronimo=cc.unidad_funcional and uf.institucion=cc.institucion) where cc.codigo>0 and cc.institucion=? ";
		if(!tipoArea.trim().equals(""))
		{
			cadena+=" AND cc.tipo_area in("+tipoArea+")";
		}
		if(!todos)
		{
			cadena+=" and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
		}
		if(centroAtencion>0)
		{
			cadena+=" and cc.centro_atencion="+centroAtencion;
		}
		if(filtro_externos)
		{
			cadena+=" and cc.codigo <> "+ConstantesBD.codigoCentroCostoExternos+" ";
		}
		logger.info("\n\n [CONSULTA_CENTROS_COSTO] \n\n"+cadena);
		logger.info("===> institucion = "+institucion);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by cc.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
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
	 * @param institucion
	 * @param tiposArea
	 * @param todos
	 * @param area
	 * @param filtro_externos 
	 * @return
	 */
	
	public static HashMap obtenerCentrosCostoTipoConsignacion(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion, boolean filtro_externos)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String tipoArea=tiposArea;
		if(tiposArea.indexOf(ConstantesBD.separadorSplit)>0)
		{
			String[] temp=tiposArea.split(ConstantesBD.separadorSplit);
			tipoArea=temp[0];
			for(int i=1;i<temp.length;i++)
			{
				tipoArea+="','"+temp[i];
			}
		}
		String cadena="select cc.codigo as codigo," +
				" cc.identificador as identificador," +
				" cc.nombre as nomcentrocosto," +
				" cc.tipo_area as codigotipoarea," +
				" ta.nombre as nombretipoarea," +
				" case when es_activo = '"+
				ValoresPorDefecto.getValorTrueParaConsultas()+
				"' then 'true' else 'false' end as activo,identificador,case when manejo_camas = '"+
				ValoresPorDefecto.getValorTrueParaConsultas()+
				"' then 'true' else 'false' end as manejacamas," +
				" cc.unidad_funcional as unidadfuncional," +
				" uf.descripcion as descunidadfuncional," +
				" cc.centro_atencion as codcentroatencion," +
				" getnomcentroatencion(cc.centro_atencion) as nomcentroatencion " +
				" from centros_costo cc " +
				" inner join tipos_area ta on(cc.tipo_area=ta.codigo) " +
				" inner join almacen_parametros ap on(cc.codigo=ap.codigo) "+
				" inner join unidades_funcionales uf on(uf.acronimo=cc.unidad_funcional and uf.institucion=cc.institucion) " +
				" where cc.codigo>0 and cc.institucion=? AND ap.tipo_consignac='S'";
		
		if(!tipoArea.trim().equals(""))
		{
			cadena+=" AND cc.tipo_area in('"+tipoArea+"')";
		}
		if(!todos)
		{
			cadena+=" and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
		}
		if(centroAtencion>0)
		{
			cadena+=" and cc.centro_atencion="+centroAtencion;
		}
		if(filtro_externos)
		{
			cadena+=" and cc.codigo <> "+ConstantesBD.codigoCentroCostoExternos+" ";
		}
		logger.info("\n\n [CONSULTA_CENTROS_COSTO_CONSIGNACION] \n\n"+cadena);
		logger.info("===> Institucion: "+institucion);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by cc.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
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
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerTiposComplejidad(Connection con)
	{
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(" SELECT codigo,descripcion from tipos_complejidad order by descripcion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param identificadorTabla
	 * @return
	 */
	public static String consultarNombreTablaInterfaz(Connection con, String identificadorTabla,int institucion) 
	{
		String consulta="SELECT valor from tablas_interfaces where nombre=? and institucion=?";
		String resultado="";
		try 
		{
			logger.info("CONSULTA=> "+consulta);
			logger.info("nombre=>*"+identificadorTabla+"*");
			logger.info("institucion=> "+institucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, identificadorTabla);
			ps.setInt(2, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getString(1);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param abono
	 * @return
	 */
	public static boolean insertarAbono(Connection con, DtoInterfazAbonos abono,String cadenaInsercion) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
			String codPacTemp=abono.getCodigoPaciente();
			if(codPacTemp.indexOf(".")>0)
			{
				codPacTemp=codPacTemp.substring(0,codPacTemp.indexOf("."));
			}
			ps.setInt(1, Integer.parseInt(codPacTemp));
			String numDoc=abono.getNumeroDocumento();
			if(numDoc.indexOf(".")>0)
			{
				numDoc=numDoc.substring(0,numDoc.indexOf("."));
			}
			ps.setObject(2, numDoc);
			String tipoMov=abono.getTipoMov();
			if(tipoMov.indexOf(".")>0)
			{
				tipoMov=tipoMov.substring(0,tipoMov.indexOf("."));
			}
			ps.setInt(3, Integer.parseInt(tipoMov));
			ps.setObject(4, abono.getValor());
			ps.setDate(5, Date.valueOf(abono.getFecha()));
			ps.setString(6, abono.getHora());
			ps.setInt(7,abono.getInstitucion());
			return(ps.executeUpdate()>0);
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
	 * @return
	 */
	public static Vector<String> obetenerCodigosInstituciones(Connection con) 
	{
		String cadena="SELECT codigo from instituciones where codigo>0";
		Vector<String> instituciones=new Vector<String>();
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				instituciones.add(rs.getInt(1)+"");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return instituciones;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public static InfoDatosString obtenerInstitucionSirCentroAtencion(Connection con, int codigoCentroAtencion) 
	{
		InfoDatosString resultado=new InfoDatosString(ConstantesBD.codigoNuncaValido+"","");
		String cadena="SELECT istsirc.codigo,istsirc.descripcion from instituciones_sirc istsirc inner join centro_atencion ca on(ca.codigo_inst_sirc=istsirc.codigo and ca.cod_institucion=istsirc.institucion) where ca.consecutivo=?";
		
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCentroAtencion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				resultado=new InfoDatosString(rs.getString(1),rs.getString(2));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap obtenerInstituciones(Connection con) 
	{
		HashMap resultado=new HashMap();
		String cadena="SELECT codigo, razon_social from instituciones where codigo > 0 order by codigo";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	
	
	/** 
	 * Mï¿½todo implementado parta obtener Tipos de Sala
	 * si se envian vacios esQuirurjica y esUrgencias no se toman en cuenta
	 * @param con
	 * @param int institucion
	 * @param String esQuirurjica (t,f) 
	 * @param String esUrgencias (t,f)
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposSala(Connection con,int institucion,String esQuirurjica,String esUrgencias)
	{
		ArrayList<HashMap<String, Object>> resultado = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo, descripcion FROM tipos_salas WHERE institucion = "+institucion+" ";
			
			if(!esQuirurjica.equals(""))
				consulta+=" AND (es_quirurgica = "+(UtilidadTexto.getBoolean(esQuirurjica)?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas())+" OR es_quirurgica IS NULL) ";
			
			if(!esUrgencias.equals(""))
				consulta+=" AND (es_urgencias = "+(UtilidadTexto.getBoolean(esUrgencias)?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas())+" OR es_urgencias IS NULL) ";	
			
			
			consulta+=" ORDER BY descripcion ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getString("codigo"));
				elemento.put("descripcion",rs.getString("descripcion"));
				resultado.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPaises: "+e);
			
		}
		return resultado;
	}
	
	
	/**
	 * Mï¿½todo implementado parta obtener los paises
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPaises(Connection con)
	{
		ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo_pais, descripcion FROM paises order by descripcion";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getString("codigo_pais"));
				elemento.put("descripcion",rs.getString("descripcion"));
				paises.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPaises: "+e);
			
		}
		return paises;
	}
	
	/**
	 * Mï¿½todo que consulta las localidades 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerLocalidades(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> localidades = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo_pais,codigo_departamento,codigo_ciudad,codigo_localidad,descripcion " +
				"FROM localidades ";
			String seccionWHERE = "";
			
			//**************FILTROS (no son obligatorios)**************************************************
			if(!campos.get("codigoPais").toString().equals(""))
				seccionWHERE = " WHERE codigo_pais = '"+campos.get("codigoPais")+"' ";
			if(!campos.get("codigoCiudad").toString().equals(""))
			{
				if(!seccionWHERE.equals(""))
					seccionWHERE += " AND ";
				else
					seccionWHERE += " WHERE ";
				seccionWHERE += " codigo_ciudad = '"+campos.get("codigoCiudad")+"' ";
			}
			if(!campos.get("codigoDepartamento").toString().equals(""))
			{
				if(!seccionWHERE.equals(""))
					seccionWHERE += " AND ";
				else
					seccionWHERE += " WHERE ";
				seccionWHERE += " codigo_departamento = '"+campos.get("codigoDepartamento")+"' ";
			}
			seccionWHERE += " ORDER BY descripcion ";
			//***********************************************************************
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta+seccionWHERE));
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoPais", rs.getObject("codigo_pais"));
				elemento.put("codigoCiudad", rs.getObject("codigo_ciudad"));
				elemento.put("codigoDepartamento", rs.getObject("codigo_departamento"));
				elemento.put("codigoLocalidad", rs.getObject("codigo_localidad"));
				elemento.put("nombreLocalidad", rs.getObject("descripcion"));
				
				localidades.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerLocalidades: "+e);
			
		}
		
		return localidades;
	}
	
	/**
	 * Mï¿½todo que consulta la localidad de un barrio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerLocalidadDeBarrio(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> localidades = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT ba.codigo_pais,ba.codigo_departamento,ba.codigo_ciudad,ba.codigo_barrio,loc.codigo_localidad,loc.descripcion " +
				"FROM barrios ba, localidades loc ";
			String seccionWHERE = " WHERE ba.codigo_localidad = loc.codigo_localidad ";
			
			if(!campos.get("codigoPais").toString().equals(""))
			{
				if(!seccionWHERE.equals(""))
					seccionWHERE += " AND ";
				else
					seccionWHERE += " WHERE ";
				seccionWHERE += " ba.codigo_pais = '"+campos.get("codigoPais")+"' ";
			}
			if(!campos.get("codigoCiudad").toString().equals(""))
			{
				if(!seccionWHERE.equals(""))
					seccionWHERE += " AND ";
				else
					seccionWHERE += " WHERE ";
				seccionWHERE += " ba.codigo_ciudad = '"+campos.get("codigoCiudad")+"' ";
			}
			if(!campos.get("codigoDepartamento").toString().equals(""))
			{
				if(!seccionWHERE.equals(""))
					seccionWHERE += " AND ";
				else
					seccionWHERE += " WHERE ";
				seccionWHERE += " ba.codigo_departamento = '"+campos.get("codigoDepartamento")+"' ";
			}
			if(!campos.get("codigoBarrio").toString().equals(""))
			{
				if(!seccionWHERE.equals(""))
					seccionWHERE += " AND ";
				else
					seccionWHERE += " WHERE ";
				seccionWHERE += " ba.codigo_barrio = '"+campos.get("codigoBarrio")+"' ";
			}
			seccionWHERE += " ORDER BY descripcion ";
			//***********************************************************************
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta+seccionWHERE));
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoPais", rs.getObject("codigo_pais"));
				elemento.put("codigoCiudad", rs.getObject("codigo_ciudad"));
				elemento.put("codigoDepartamento", rs.getObject("codigo_departamento"));
				elemento.put("codigoBarrio", rs.getObject("codigo_barrio"));
				elemento.put("codigoLocalidad", rs.getObject("codigo_localidad"));
				elemento.put("nombreLocalidad", rs.getObject("descripcion"));
				
				localidades.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerLocalidadDeBarrio: "+e);
			
		}
		
		return localidades;
	}
	
	
	/**
	 * Mï¿½todo que consulta los sexos parametrizados en el sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerSexos(Connection con)
	{
		ArrayList<HashMap<String, Object>> sexos = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo, nombre FROM sexo order by nombre";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap elemento = new HashMap();
				elemento.put("codigo",rs.getString("codigo"));
				elemento.put("nombre",rs.getString("nombre"));
				
				sexos.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSexos: "+e);
			
		}
		
		return sexos;
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static int getCodigoResponsable(Connection con, int idIngreso, int codigoConvenio) 
	{
		String cadena="SELECT sub_cuenta from sub_cuentas where ingreso = ? and convenio= ? order by facturado";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, idIngreso);
			ps.setInt(2, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch(SQLException e)
		{
			logger.error("Error en getCodigoResponsable: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static int obtenerPrioridadResponsabe(Connection con, String subCuenta) throws BDException
	{
		String cadena="SELECT nro_prioridad from sub_cuentas where sub_cuenta = ?";
		try	{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaComplejidad(Connection con, int codigoConvenio) 
	{
		String cadena="SELECT maneja_complejidad from convenios where codigo = ?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch(SQLException e)
		{
			logger.error("Error en convenioManejaComplejidad: "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaPyp(Connection con, int codigoConvenio) 
	{
		String cadena="SELECT pyp FROM convenios WHERE codigo = ?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch(SQLException e)
		{
			logger.error("Error en convenioManejaPyp: "+e);
		}
		return false;
	}
	
	public static InfoDatosInt obtenerTipoComplejidadCuenta(Connection con, int cuenta) throws BDException
	{
		InfoDatosInt complejidad=new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		String cadena=" SELECT tipo_complejidad,coalesce(tc.descripcion,'') from cuentas c left outer join tipos_complejidad tc on(tc.codigo=c.tipo_complejidad) where id=?";
		
		try	{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				complejidad.setCodigo(rs.getInt(1));
				complejidad.setNombre(rs.getString(2));
			}
		}
		catch(SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return complejidad;
	}
	
	/**
	 * 
	 * @param connection connection
	 * @param HashMap grupoServicios
	 * @return ArrayList<HashMap>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerGrupoServicios (Connection connection, HashMap grupoServicios)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>> ();
		
		
		try{
			 String consulta = "SELECT"+
			 							" codigo as codigo,"+
			 							" descripcion as descripcion,"+
			 							" acronimo as acronimo,"+
			 							" activo as activo,"+
			 							" tipo as tipo,"+
			 							" multiple as multiple"+
			 					" FROM grupos_servicios " +
			 					"WHERE institucion="+grupoServicios.get("institucion");
			 
			 if(grupoServicios.containsKey("activo"))
					consulta+=" AND activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			 consulta+=" ORDER BY descripcion";
				Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				while(rs.next())
				{
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigo",rs.getObject("codigo"));
					elemento.put("descripcion",rs.getObject("descripcion"));
					elemento.put("acronimo",rs.getObject("acronimo"));
					elemento.put("activo",rs.getObject("activo"));
					elemento.put("tipo",rs.getObject("tipo"));
					elemento.put("multiple",rs.getObject("multiple"));
					resultados.add(elemento);
				}
			 
			 
			 
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		
		return resultados;		
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerViasIngresoCuenta(Connection con, int cuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cuentas=obtenerCuentas(con,cuenta);
			String cadena="SELECT DISTINCT via_ingreso as viaingreso,getnombreviaingreso(via_ingreso) as nomviaingreso from cuentas where id in("+cuentas+")";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Encargado de devolver la via de ingreso de una cuenta.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerViaIngresoCuenta(Connection con, int cuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
			String cadena="SELECT DISTINCT via_ingreso as viaingreso,getnombreviaingreso(via_ingreso) as nomviaingreso from cuentas where id in("+cuenta+")";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
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
	 * @param cuenta
	 * @return
	 */
	private static String obtenerCuentas(Connection con, int cuenta) 
	{
		String cuentas=cuenta+"";
		boolean tieneMasCuenta=true;
		while (tieneMasCuenta)
		{
			String cadena="SELECT cuenta_inicial from asocios_cuenta  where cuenta_final ="+cuenta;
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					cuenta=rs.getInt(1);
					cuentas=cuentas+","+cuenta;
				}
				else
				{
					tieneMasCuenta=false;
				}
			}
			catch(SQLException e)
			{
				//no tiene mas cuentas ya que produjo una excepecion
				tieneMasCuenta=false;
			}
		}
		return cuentas;
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerTiposPacienteCuenta(Connection con, int cuenta) 
	{
		HashMap mapa=new HashMap();  
		mapa.put("numRegistros", "0");
		try
		{
			String cuentas=obtenerCuentas(con,cuenta);
			String cadena="SELECT DISTINCT tipo_paciente as tipopaciente,getnombretipopaciente(tipo_paciente) as nomtipopaciente,via_ingreso as viaingreso,getnombreviaingreso(via_ingreso) as nomviaingreso from cuentas  where id in("+cuentas+")";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
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
	 * @param subCuenta
	 * @return
	 */
	public static String obtenerPorcentajeAutorizadoVerficacionDerechos(Connection con, String subCuenta) 
	{
		String cadena="SELECT coalesce(porcentaje_cobertura||'','') from verificaciones_derechos where sub_cuenta = "+Utilidades.convertirALong(subCuenta);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static HashMap obtenerCoberturaAccidenteTransitos(Connection con, int institucion) 
	{
		String cadena="SELECT responsable,cobertura  from cob_accidentes_transito where institucion=? order by responsable";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
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
	 * @param codigoIngreso
	 * @return
	 */
	public static String obtenerFechaAccidenteTransito(Connection con, int codigoIngreso) 
	{
		String cadena="SELECT fecha_accidente from view_registro_accid_transito where estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' and ingreso=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadFecha.conversionFormatoFechaABD(rs.getDate(1));
				//return rs.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param fecha
	 * @return
	 */
	public static double obtenerSalarioMinimoVigente(Connection con, String fecha) 
	{
		String cadena="SELECT salario from salario_minimo where ? between fecha_inicial and fecha_final";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			String fechaBD=UtilidadFecha.conversionFormatoFechaABD(fecha);
			
			ps.setDate(1,Date.valueOf(fechaBD));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getDouble(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException 
	 */
	public static InfoDatosString obtenerEstadoHC(Connection con, String numeroSolicitud) throws SQLException,Exception
	{
		String cadena="SELECT codigo,nombre from solicitudes s  inner join estados_sol_his_cli esh on (esh.codigo=s.estado_historia_clinica) where numero_solicitud ="+numeroSolicitud;
		InfoDatosString resultado=new InfoDatosString();
		try
		{
			if(con!=null && !con.isClosed()){
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado=new InfoDatosString(rs.getString(1),rs.getString(2));
				}
			}else{
				logger.error("La conexion viene nula por favor verificar  - Conexion nula - funcionalidad SqlBaseUtilidadesDao.obtenerEstadoHC");
				throw new Exception("La conexion está cerrada por verificar");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error generando ejecucion de la consulta",e);
			throw new SQLException("Error generando ejecucion de la consulta "+cadena);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoMonto
	 * @return
	 * @throws Exception 
	 * @deprecated
	 */
	public static ResultadoBoolean esPorcentajeMonto(Connection con, int codigoMonto) throws Exception
	{
		ResultadoBoolean resultado=null;
		String cadena="SELECT case when porcentaje is NULL then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as esporcentaje, coalesce(porcentaje,valor) as valor from montos_cobro where codigo="+codigoMonto;
		try
		{
			if(con!=null){
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado=new ResultadoBoolean(UtilidadTexto.getBoolean(rs.getString(1)),rs.getString(2));
				}
			}else{
				throw new Exception("La coneccion es nula, por favor revisar");
			}
		}
		catch (SQLException e)
		{
			throw new Exception("Se presento un error Generenado la consulta esPorcentajeMonto");
			
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static double obtenerTotalCargadoResponsable(Connection con, String subCuenta) 
	{
		String cadena="SELECT SUM((coalesce(valor_unitario_cargado,0)+coalesce(valor_unitario_recargo,0)-coalesce(valor_unitario_dcto,0))*coalesce(cantidad_cargada,0)) as totalcargado from det_cargos where sub_cuenta=? and estado in ("+ConstantesBD.codigoEstadoFCargada+") and paquetizado='"+ConstantesBD.acronimoNo+"' and eliminado='"+ConstantesBD.acronimoNo+"'";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--> "+cadena+"---"+subCuenta);
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("--->"+rs.getDouble(1));
				return rs.getDouble(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerNombreTipoComplejidad (Connection con, int codigo)
	{
		String consulta="SELECT descripcion as descrip from tipos_complejidad where codigo=?";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("descrip");
		}	
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoComplejidad: "+e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param subcuenta
	 * @return
	 */
	public static String obtenerNumeroCarnet(Connection con, double subcuenta)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT nro_carnet as carnet FROM sub_cuentas WHERE sub_cuenta = "+(long)subcuenta;
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs = pst.executeQuery(consulta);
			
			if(rs.next()){
				return (rs.getString("carnet")!=null)?rs.getString("carnet"):"";
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerNumeroCarnet",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerNumeroCarnet", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public static String obtenerPathFuncionalidad(Connection con, int codigoFuncionalidad)
	{
		String consulta="SELECT archivo_func as ruta FROM funcionalidades where codigo_func=?";
		String resultado="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, codigoFuncionalidad);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getString("ruta");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap obtenerEsquemasTarifarios(Connection con, boolean esInventarios, int codigoInstitucion) 
	{
		HashMap mapa= new HashMap();
		String esInventariosStr="";
		if(esInventarios)
			esInventariosStr=ValoresPorDefecto.getValorTrueCortoParaConsultas();
		else
			esInventariosStr=ValoresPorDefecto.getValorFalseCortoParaConsultas();
		
		String cadena=	" SELECT codigo as codigo, nombre as nombre FROM esquemas_tarifarios WHERE es_inventario='"+esInventariosStr+"' AND activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' and institucion=? order by nombre";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo que devuelve un arraylist con los valores de esquemas tarifarios
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosInArray(Connection con, boolean esInventarios, int codigoInstitucion,int tarifarioOficial) 
	{
		ArrayList<HashMap<String, Object>> mapa = new ArrayList<HashMap<String,Object>>();
		String esInventariosStr="";
		if(esInventarios)
			esInventariosStr=ValoresPorDefecto.getValorTrueCortoParaConsultas();
		else
			esInventariosStr=ValoresPorDefecto.getValorFalseCortoParaConsultas();
		
		String cadena=	" SELECT codigo as codigo, nombre as nombre FROM esquemas_tarifarios WHERE es_inventario='"+esInventariosStr+"' AND activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' and institucion=? ";
		
		if(tarifarioOficial!=ConstantesBD.codigoNuncaValido)
			cadena += " AND tarifario_oficial = "+tarifarioOficial+" ";
		cadena += "order by nombre";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap elemento = new HashMap ();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				mapa.add(elemento);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * Mï¿½todo que devuelve un arraylist con los valores de esquemas tarifarios Generales y Particulares
	 * adicionalmente se carga el codigo del encabezado asociado a la parametrica (porcentajescx,asociosserv)
	 * @param con
	 * @param codigoInstitucion
	 * @param String parametrica
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosGenPartInArray(
			Connection con,			
			int codigoInstitucion,
			String parametrica) 
	{
		ArrayList<HashMap<String, Object>> mapa = new ArrayList<HashMap<String,Object>>();	
		
		String cadena=	" SELECT " +
				"codigo AS codigo, " +
				"nombre AS nombre," +
				"'1' AS tipo_esquema," +
				"coalesce(cantidad,0) AS cantidad ";
		
		if(parametrica.equals("porcentajescx"))
			cadena+=",getcodigoencabporcentajes(codigo,null) AS codigo_encab " ;
		else if(parametrica.equals("asociosserv"))
			cadena+=",getcodigoencabasocioservtar(codigo,null) AS codigo_encab " ;
				
		cadena+=					
			"FROM esquemas_tarifarios " +
			"WHERE activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +
			"AND institucion=? " +
			"AND es_inventario = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' " +				
			"UNION " +
			"SELECT " +
			"codigo," +
			"nombre, " +
			"'0' AS tipo_esquema, " +
			"0 AS cantidad ";		
		
		if(parametrica.equals("porcentajescx"))
			cadena+=",getcodigoencabporcentajes(null,codigo) AS codigo_encab " ;
		else if(parametrica.equals("asociosserv"))
			cadena+=",getcodigoencabasocioservtar(null,codigo) AS codigo_encab " ;
		
		cadena+=											
			"FROM esq_tar_porcen_cx "+	
			"ORDER BY nombre ";
		
		PreparedStatementDecorator ps=null;
		
		try 
		{
			logger.info("valor sql esquemas >> "+cadena);
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap elemento = new HashMap ();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				elemento.put("tipo_esquema", rs.getObject("tipo_esquema")+"");
				elemento.put("cantidad", rs.getObject("cantidad"));
				
				if(rs.getObject("codigo_encab")!=null)
					elemento.put("codigo_encab", rs.getString("codigo_encab"));
				else
					elemento.put("codigo_encab",ConstantesBD.codigoNuncaValido+"");				
				
				mapa.add(elemento);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * Mï¿½todo que consulta los dias de la semana
	 * @param con	
	 * @return
	 */
	public static HashMap obtenerDiasSemana(Connection con)
	{
		HashMap mapa = new HashMap();
		
		try
		{
			String consulta = "SELECT codigo, dia FROM dias_semana ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));		
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDiasSemana: "+e);			
		}
		return mapa;
	}	
	
	/**
	 * Mï¿½todo que consulta el nombre del estado de liquidacion
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static String getNombreEstadoLiquidacion(Connection con,String acronimo)
	{
		String nombre = "";
		try
		{
			String consulta = "SELECT nombre FROM estados_liquidacion WHERE acronimo = '"+acronimo+"'";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				nombre = rs.getString("nombre");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getNombreEstadoLiquidacion: "+e);
			
		}
		return nombre;
	}
	
	/**
	 * Mï¿½todo que consulta el nombre del estado de la cita
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String getNombreEstadoCita(Connection con,int codigo)
	{
		String nombre = "";
		try
		{
			String consulta = "SELECT nombre FROM estados_cita WHERE codigo = "+codigo;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				nombre = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error getNombreEstadoCita: "+e);
		}
		return nombre;
	}
	
	/**
	 * Mï¿½todo que consulta las finalidades de un servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerFinalidadesServicio(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT "+
				"fs.codigo AS codigo, "+ 
				"fs.nombre AS descripcion "+ 
				"FROM servicios s "+ 
				"INNER JOIN fin_serv_naturaleza fsn ON(fsn.naturaleza=s.naturaleza_servicio) "+ 
				"INNER JOIN finalidades_servicio fs ON(fs.codigo=fsn.finalidad) "+ 
				"WHERE s.codigo = "+campos.get("codigoServicio")+" and fsn.institucion = "+campos.get("institucion");
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("descripcion",rs.getObject("descripcion"));
				resultados.add(elemento);
			}
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerFinalidadesServicio", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		
		return resultados;
	}
	
	/**
	 * Mï¿½todo que consulta el nombre del esquema tarifario segun su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String getNombreEsquemaTarifario(Connection con, int codigo)
	{
	    try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(getNombreEsquemaTarifarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("nombre");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Mï¿½todo que consulta el tarifario oficial del esquema tarifario segun el codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int getTarifarioOficial(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(getTarifarioOficialStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("tarifario_oficial");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerSexoServicio(Connection con, String codigoServicio) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT case when sexo is null then -1 else sexo end as sexo from servicios where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("sexo");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}	
	
	/**
	 * Mï¿½todo encargado de consultar todos los servicios,
	 * pudiendolos filtrar por diferentes tipos
	 * de servicio y por el estado activo:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --tiposServicio --> ejemplo, este es el valor de esta variable  'R','Q','D'
	 * --activo --> debe indicar "t" o "f". 
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerServicos(Connection connection,String tiposServicio, String activo)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT codigo AS codigo," +
				"especialidad AS especialidad," +
				"tipo_servicio AS tipo_servicio," +
				"naturaleza_servicio AS naturaleza_servicio," +
				"espos AS espos," +
				"activo AS activo," +
				"grupo_servicio AS grupo_servicio," +
				"nivel AS nivel," +
				"respuesta_multiple AS respuesta_multiple," +
				"toma_muestra AS toma_muestra," +
				"requiere_interpretacion AS requiere_interpretacion " +
				"FROM  servicios ",
				where="WHERE 1=1";
	
		if (UtilidadCadena.noEsVacio(tiposServicio))
			where+=" AND tipo_servicio IN ("+tiposServicio+")";
		    
		if(UtilidadCadena.noEsVacio(activo))
			where+=" AND activo='"+activo+"'";
		    
		cadena +=where;
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("especialidad",rs.getObject("especialidad"));
				elemento.put("tipo_servicio",rs.getObject("tipo_servicio"));
				elemento.put("naturaleza_servicio",rs.getObject("naturaleza_servicio"));
				elemento.put("espos",rs.getObject("espos"));
				elemento.put("activo",rs.getObject("activo"));
				elemento.put("grupo_servicio",rs.getObject("grupo_servicio"));
				elemento.put("nivel",rs.getObject("nivel"));
				elemento.put("respuesta_multiple",rs.getObject("respuesta_multiple"));
				elemento.put("toma_muestra",rs.getObject("toma_muestra"));
				elemento.put("requiere_interpretacion",rs.getObject("requiere_interpretacion"));
				
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtenerServicos: "+e);
		}
		      
		return resultados;
		
	}
	
	/**
	 * Mï¿½todo encargado de consultar todos los tipos de cirugia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTipoCirugia(Connection connection,String acronimo)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT acronimo AS acronimo," +
				"nombre AS nombre " +
				"FROM  tipos_cirugia ",
				where="WHERE 1=1";
	
		if (UtilidadCadena.noEsVacio(acronimo))
			where+=" AND acronimo IN ("+acronimo+")";
		    
		cadena +=where;
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("acronimo",rs.getObject("acronimo"));
				elemento.put("nombre",rs.getObject("nombre"));
								
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtenerTipoCirugia: "+e);
		}
		      
		return resultados;
		
	}
	
	/**
	 * Mï¿½todo encargado de consultar las instituciones Sirc
	 * pudiendo filtrar por diferentes criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * --institucion --> Requerido
	 * --nivelServicio --> Opcional
	 * --tipoRed --> Opcional
	 * --tipoInstReferencia --> Opcional
	 * --tipoInstAmbulancia --> Opcional
	 * --activo --> Opcional
	 * --codigo --> Opcional
	 * @param connection
	 * @param criterios
	 * @return ArrayList<HashMap>
	 * --------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * --------------------------------------
	 * codigo,descripcion,tipoRed,
	 * tipoInstReferencia,tipoInstAmbulancia,
	 * nivelServicio, activo
	 */
	public static ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection connection,HashMap criterios)
	{
		logger.info("\n entre a consultar las instituciones sirc "+criterios);
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT codigo AS codigo," +
				" descripcion AS descripcion, " +
				" nivel_servicio As nivelservicio, " +
				" tipo_red As tipored," +
				" tipo_inst_referencia As tipoinstreferencia, " +
				" tipo_inst_ambulancia As tipoinstambulancia, " +
				" activo As activo " +
				" FROM  instituciones_sirc ";
		
		String where=" WHERE institucion="+criterios.get("institucion");
	
		String order=" ORDER BY descripcion";
		
		if (UtilidadCadena.noEsVacio(criterios.get("nivelServicio")+""))
		    where+=" AND nivel_servicio="+criterios.get("nivelServicio");
			
		if (UtilidadCadena.noEsVacio(criterios.get("tipoRed")+""))
		    where+=" AND tipo_red='"+criterios.get("tipoRed")+"'";
		
		if (UtilidadCadena.noEsVacio(criterios.get("tipoInstReferencia")+""))
		    where+=" AND tipo_inst_referencia='"+criterios.get("tipoInstReferencia")+"'";
		
		if (UtilidadCadena.noEsVacio(criterios.get("tipoInstAmbulancia")+""))
		    where+=" AND tipo_inst_ambulancia='"+criterios.get("tipoInstAmbulancia")+"'";
		
		if (UtilidadCadena.noEsVacio(criterios.get("activo")+""))
		    where+=" AND activo='"+criterios.get("activo")+"'";
		
		if (UtilidadCadena.noEsVacio(criterios.get("codigo")+""))
		    where+=" AND codigo='"+criterios.get("codigo")+"'";
		
		
		cadena +=where+order;
		
		logger.info("\n cadena -->"+cadena);
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("descripcion",rs.getObject("descripcion"));
				elemento.put("tipoRed",rs.getObject("tipored"));
				elemento.put("tipoInstReferencia",rs.getObject("tipoinstreferencia"));
				elemento.put("tipoInstAmbulancia",rs.getObject("tipoinstambulancia"));
				elemento.put("nivelServicio",rs.getObject("nivelservicio"));
				elemento.put("activo",rs.getObject("activo"));
								
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtener las instituciones Sirc: "+e);
		}
		      
		return resultados;
		
	}
	
	/**
	 * Mï¿½todo encargado de consultar todos los tipos de anestecia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTipoAnestecia(Connection connection,String acronimos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT codigo AS codigo," +
				"descripcion AS descripcion," +
				"acronimo AS acronimo," +
				"mostrar_en_hqx AS mostrar_en_hqx " +
				"FROM  tipos_anestesia ",
				where="WHERE 1=1";
	
		if (UtilidadCadena.noEsVacio(acronimos))
			where+=" AND acronimo IN ("+acronimos+")";
		    
		cadena +=where;
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("descripcion",rs.getObject("descripcion"));
				elemento.put("acronimo",rs.getObject("acronimo"));
				elemento.put("mostrar_en_hqx",rs.getObject("mostrar_en_hqx"));
								
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtenerTipoAnestecia: "+e);
		}
		      
		return resultados;
		
	}
	
	
	
	/**
	 * Mï¿½todo encargado de consultar todos los asocios,
	 * pudiendolos filtrar por el codigo_asocio,
	 * tipo de servicio y si participa por cirugia.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --codAsocio --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 * --tipoServ --> ejemplo, este es el valor de esta variable  'Q','R','D'
	 * --participa --> es S o N
	 * @param connection
	 * @param codAsocio
	 * @param tipoServ
	 * @param participa
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerAsocios(Connection connection,String codAsocio,String tipoServ,String participa)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT codigo AS codigo," +
				"codigo_asocio AS codigo_asocio," +
				"nombre_asocio AS nombre_asocio," +
				"tipos_servicio AS tipos_servicio, " +
				"getnombretiposervicio(tipos_servicio) as nombre_tipo_servicio, " +
				"participa_cir AS participa_cir,  " +
				"CASE WHEN centro_costo_ejecuta IS NULL THEN '' ELSE getnomcentrocosto(centro_costo_ejecuta) end as centro_costo_ejecuta " +
				"FROM  tipos_asocio ",
				where="WHERE 1=1";
	
		if (UtilidadCadena.noEsVacio(codAsocio))
			where+=" AND codigo_asocio IN ("+codAsocio+")";
		
		if (UtilidadCadena.noEsVacio(tipoServ))
			where+=" AND tipos_servicio IN ('"+tipoServ+"')";
		
		if (UtilidadCadena.noEsVacio(participa))
			where+=" AND participa_cir ='"+participa+"'";
		    
		cadena +=where+" ORDER BY nombre_asocio ASC ";
		logger.info("\n cadena -->"+cadena);
		try 
		{
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("codigo_asocio",rs.getObject("codigo_asocio"));
				elemento.put("nombre_asocio",rs.getObject("nombre_asocio"));
				elemento.put("tipos_servicio",rs.getObject("tipos_servicio"));
				elemento.put("nombre_tipo_servicio",rs.getObject("nombre_tipo_servicio"));
				elemento.put("participa_cir",rs.getObject("participa_cir"));
				elemento.put("centro_costo_ejecuta", rs.getString("centro_costo_ejecuta"));
				elemento.put("seleccionado",ConstantesBD.acronimoNo);
								
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtenerAsocios: "+e);
		}
		      
		return resultados;
		
	}
	
		
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> obtenerTransaccionesCentroCosto(Connection con, int institucion) 
	{
		HashMap mapa=new HashMap<String,Object>();
		mapa.put("numRegistros", "0");
		try
		{
			String consulta = "SELECT distinct t2.consecutivo as transaccion,t1.centros_costo as centrocosto,t2.descripcion as descripcion,coalesce(t2.indicativo_consignacion,'N') as indicativoconsignacion,t2.tipos_conceptos_inv as tipoconcepto from trans_validas_x_cc_inven t1 inner join tipos_trans_inventarios t2 on(t1.tipos_trans_inventario=t2.consecutivo) where t1.institucion=?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTransaccionesCentroCosto: "+e);
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param almacen
	 * @param grupoInventario 
	 * @param claseInventario 
	 * @param existenciasPositivas
	 * @return
	 */
	public static HashMap<String, Object> obtenerArticulosCantidadPosNeg(Connection con, String almacen, String claseInventario, String grupoInventario, boolean existenciasPositivas) 
	{
		String consulta="";
		String filtroClaseGrupo="";
		if(!UtilidadTexto.isEmpty(claseInventario))
		{
			filtroClaseGrupo+=" AND si.clase="+claseInventario;
			if(!UtilidadTexto.isEmpty(grupoInventario))
			{
				filtroClaseGrupo+=" AND si.grupo="+grupoInventario;
			}
		}
		if(existenciasPositivas)
			consulta="SELECT articulo,abs(sumaartalmacen) as cantidad from exis_arti_almacen eaa inner join articulo a on (a.codigo=eaa.articulo) inner join subgrupo_inventario si on(a.subgrupo=si.codigo) where almacen = ? and sumaartalmacen>0"+filtroClaseGrupo;
		else
			consulta="SELECT articulo,abs(sumaartalmacen) as cantidad from exis_arti_almacen eaa inner join articulo a on (a.codigo=eaa.articulo) inner join subgrupo_inventario si on(a.subgrupo=si.codigo) where almacen = ? and sumaartalmacen<0"+filtroClaseGrupo;
		HashMap mapa=new HashMap<String,Object>();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,almacen);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTransaccionesCentroCosto: "+e);
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param almacen
	 * @param transaccionEntrada
	 * @param transaccionSalida
	 * @param observaciones
	 * @return
	 */
	public static boolean generarLogTransSaldosInventario(Connection con, String loginUsuario, String almacen, String transaccionEntrada, String transaccionSalida, String observaciones) 
	{
		try
		{
			String cadenaInsert=" INSERT INTO log_tran_saldos_inventario (codigo,fecha,hora,usuario,almacen,codigo_trans_entrada,codigo_trans_salida,observaciones) values(?,current_date,?,?,?,?,?,?)";
			PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(cadenaInsert, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_transalinv"));
			pst.setObject(2,UtilidadFecha.getHoraActual());
			pst.setObject(3,loginUsuario);
			pst.setObject(4,almacen);
			pst.setObject(5,transaccionEntrada);
			pst.setObject(6,transaccionSalida);
			pst.setObject(7,observaciones);
			pst.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en generarLogTransSaldosInventario: "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param factura
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap consultarFacturasMismoConsecutivo(Connection con, int factura, int codigoInstitucionInt) 
	{
		String consulta="SELECT codigo,consecutivo_factura as consecutivo,getDescempresainstitucion(empresa_institucion) as entidad from facturas where consecutivo_factura = ? and institucion=?";
		HashMap mapa=new HashMap<String,Object>();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,factura+"");
			pst.setInt(2, codigoInstitucionInt);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTransaccionesCentroCosto: "+e);
		}
		return mapa;
	}
	
		
	 /**
     * @param con
     * @param Acronimo Tipo Identificacion
     * @return
     */
    public static String getDescripcionTipoIdentificacion(Connection con, String acronimoTipoIdentificacion)
    {
        String cadena="SELECT nombre FROM tipos_identificacion WHERE acronimo ='"+acronimoTipoIdentificacion+"'";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("nombre");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
    }

	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public static String obtenerRazonSocialEmpresaInstitucion(Connection con, String empresaInstitucion) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT razon_social as nombre from empresas_institucion where codigo = "+empresaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("nombre");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * Mï¿½todo encargado de consultar las empresas parametrizadas en le sistema, con un deudor
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con, String institucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		
		String cadena=" SELECT  e.codigo, e.razon_social, t.numero_identificacion FROM empresas e inner join  terceros t  on (t.codigo=e.tercero)  where t.institucion ="+institucion+"  AND t.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND e.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY e.razon_social ";
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("numeroid",rs.getObject("numero_identificacion"));
				elemento.put("descripcion",rs.getObject("razon_social"));
											
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtener Empresas: "+e);
		}
		
		return resultados;				
	} 
	
	/**
	 * Mï¿½todo encargado de consultar las empresas parametrizadas en el sistema
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		
		String cadena=" " +
				"SELECT  " +
				"e.codigo, " +
				"e.razon_social " +
				"FROM empresas e " +
				"where " +
				"e.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		
		if(parametros.containsKey("dirTerritorial") && 
				parametros.get("dirTerritorial").equals(ConstantesBD.acronimoSi))
			cadena += " AND e.direccion_territorial = '"+ConstantesBD.acronimoSi+"' ";
				
				
		cadena += "ORDER BY e.razon_social ";
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));				
				elemento.put("descripcion",rs.getObject("razon_social"));
											
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtener Empresas: "+e);
		}
		
		return resultados;				
	} 
	
	/**
	 * Mï¿½todo encargado de Consultar los subgrupos
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * -- codigo
	 * -- subgrupo
	 * -- grupo
	 * -- clase
	 * -- cuentaInventario
	 * -- cuentaCosto
	 * @return ARRAYLIST <HASHMAP< > >
	 * ---------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ---------------------------------------
	 * codigo,subgrupo,grupo,clase,nombre,
	 * cuentaInventario,cuentaCosto
	 */
	public static ArrayList<HashMap<String, Object>> obtenerSubGrupo(Connection con, HashMap criterios)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
	
		String cadena=" SELECT codigo, subgrupo, grupo, clase, nombre, cuenta_inventario, cuenta_costo   " +
					  " FROM subgrupo_inventario " +
					  " WHERE  institucion="+criterios.get("institucion");
					  
		String where="";
		
		//codigo
		if (UtilidadCadena.noEsVacio(criterios.get("codigo")+""))
			where+=" AND  codigo="+criterios.get("codigo");
		
		//subgrupo
		if (UtilidadCadena.noEsVacio(criterios.get("subGrupo")+""))
			where+=" AND  subgrupo="+criterios.get("subGrupo");
		
		//grupo
		if (UtilidadCadena.noEsVacio(criterios.get("grupo")+""))
			where+=" AND  grupo="+criterios.get("grupo");
		
		//clase
		if (UtilidadCadena.noEsVacio(criterios.get("clase")+""))
			where+=" AND  clase="+criterios.get("clase");
		
		//cuenta_inventario
		if (UtilidadCadena.noEsVacio(criterios.get("cuentaInventario")+""))
			where+=" AND  cuenta_inventario="+criterios.get("cuentaInventario");
		
		//cuenta_costo
		if (UtilidadCadena.noEsVacio(criterios.get("cuentaCosto")+""))
			where+=" AND  cuenta_costo="+criterios.get("cuentaCosto");
		
		
		cadena+=where;
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("subgrupo",rs.getObject("subgrupo"));
				elemento.put("grupo",rs.getObject("grupo"));
				elemento.put("clase",rs.getObject("clase"));
				elemento.put("nombre",rs.getObject("nombre"));
				elemento.put("cuentaInventario",rs.getObject("cuenta_inventario"));
				elemento.put("cuentaCosto",rs.getObject("cuenta_costo"));
											
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtener subGrupos: "+e);
		}
		
		return resultados;				
	} 
	
	
	public static ArrayList<HashMap<String, Object>> obtenerEmpresasXInstitucion(Connection con, HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		
		String cadena = "SELECT codigo, institucion, razon_social, valor_consecutivo_fact, anio_vigencia " +
				"FROM empresas_institucion WHERE institucion = "+parametros.get("institucion").toString();
		
		try 
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("institucion",rs.getObject("institucion"));
				elemento.put("razonSocial",rs.getObject("razon_social"));
				
				if(rs.getObject("valor_consecutivo_fact") != null)				
					elemento.put("valorConsecutivoFact",rs.getObject("valor_consecutivo_fact"));
				else
					elemento.put("valorConsecutivoFact","");
				
				if(rs.getObject("anio_vigencia") != null)
					elemento.put("anioVigencia",rs.getObject("anio_vigencia"));
				else
					elemento.put("anioVigencia","");
				
				resultados.add(elemento);
			}
		}
		catch (SQLException e)
		{
			logger.error("\n\n *** Error en obtener MultiEmpresas de la Institucion: "+e);
		}
		
		return resultados;				
	} 
	
	/**
	 * 
	 * @param con
	 * @param numDiasControl
	 * @param articulos
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarArticulosSolicitadosUltimosXDias(Connection con, int numDiasControl, HashMap articulos, int codigoPersona) 
	{
		HashMap<String, Object> resultado=new HashMap<String, Object>();
		resultado.put("numRegistros", "0");
		int contRegistros=0;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		PreparedStatementDecorator ps1=null;
		ResultSetDecorator rs1=null;
		try
		{
			int numReg=Utilidades.convertirAEntero(articulos.get("numRegistros")+"");
			if(numReg>0)
			{
				String cadenaArticulos="";
				for(int i=0;i<numReg;i++)
				{
					if(!UtilidadTexto.getBoolean(articulos.containsKey("fueEliminadoArticulo_"+i)?articulos.get("fueEliminadoArticulo_"+i)+"":"N"))
					{
						if(UtilidadTexto.getBoolean(articulos.containsKey("medicamento_"+i)?articulos.get("medicamento_"+i)+"":"S"))
						{
							if(i>0)
								cadenaArticulos+=",";
							cadenaArticulos+=articulos.get("articulo_"+i);
						}
					}
				}
				String[] codArticulos=cadenaArticulos.split(",");
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				for(int i=0;i<codArticulos.length;i++)
				{
					String cadena=	" select det.articulo as articulo,oa.codigo as codigoorden,oa.consecutivo_orden as consecutivoorden,det.duracion_tratamiento as diastratamiento,oa.fecha as fecha,'N' as solicitudmedicamento " +
									" from ordenes_ambulatorias oa " +
									" inner join det_orden_amb_articulo det on(det.codigo_orden=oa.codigo) " +
									" where oa.codigo_paciente=? and det.articulo = "+codArticulos[i]+" and oa.fecha>(current_date-"+numDiasControl+") and (current_date-oa.fecha)<det.duracion_tratamiento order by fecha desc ";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
					ps.setObject(1, codigoPersona+"");
					rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					{
						resultado.put("articulo_"+contRegistros, rs.getObject("articulo"));
						resultado.put("codigoorden_"+contRegistros, rs.getObject("codigoorden"));
						resultado.put("consecutivoorden_"+contRegistros, rs.getObject("consecutivoorden"));
						if(rs.getDate("fecha") != null){
							resultado.put("fecha_"+contRegistros, format.format(rs.getDate("fecha")));
						}
						else{
							resultado.put("fecha_"+contRegistros, "");
						}
						resultado.put("solicitudmedicamento_"+contRegistros, rs.getObject("solicitudmedicamento"));
						resultado.put("diastratamiento_"+contRegistros,rs.getObject("diastratamiento"));
						contRegistros++;
					}
					else
					{
						cadena=	" select det.articulo as articulo,sol.numero_solicitud as codigoorden,sol.consecutivo_ordenes_medicas as consecutivoorden,det.dias_tratamiento as diastratamiento,sol.fecha_solicitud as fecha,'S' as solicitudmedicamento " +
								" from solicitudes sol " +
								" inner join detalle_solicitudes det on(sol.numero_solicitud=det.numero_solicitud) " +
								" inner join cuentas c on(c.id=sol.cuenta) " +
								" where c.codigo_paciente=? and det.articulo  = "+codArticulos[i]+" and sol.fecha_solicitud>(current_date-"+numDiasControl+") and (current_date-sol.fecha_solicitud)<det.dias_tratamiento order by sol.fecha_solicitud desc" ;
						ps1= new PreparedStatementDecorator(con.prepareStatement(cadena));
						ps1.setObject(1, codigoPersona+"");
						rs1=  new ResultSetDecorator(ps1.executeQuery());
						if(rs1.next())
						{
							resultado.put("articulo_"+contRegistros, rs1.getObject("articulo"));
							resultado.put("codigoorden_"+contRegistros, rs1.getObject("codigoorden"));
							resultado.put("consecutivoorden_"+contRegistros, rs1.getObject("consecutivoorden"));
							resultado.put("fecha_"+contRegistros, rs1.getObject("fecha"));
							resultado.put("solicitudmedicamento_"+contRegistros, rs1.getObject("solicitudmedicamento"));
							resultado.put("diastratamiento_"+contRegistros,rs1.getObject("diastratamiento"));
							contRegistros++;
						}
					}
				}
			}
		}
		catch (SQLException e) 
		{
			contRegistros=0;
			Log4JManager.error("consultarArticulosSolicitadosUltimosXDias");
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(ps1!=null){
					ps1.close();
				}
				if(rs1!=null){
					rs1.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("consultarArticulosSolicitadosUltimosXDias: cerrando ps -rs");
			}
		}
		resultado.put("numRegistros", contRegistros+"");
		return resultado;
	}


	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param loginUsuario
	 * @param esSolOrdenConfirmada 
	 * @param articulosConfirmacion
	 * @return
	 */
	public static boolean generarLogConfirmacionOrdenAmbSolMed(Connection con, String codigoOrden, String loginUsuario, String esSolOrdenConfirmada, HashMap<String, Object> articulosConfirmacion) 
	{
		String cadena="INSERT INTO ordenes.log_confirmacion_ordenes(" +
													" codigo," +
													" numero_orden," +
													" es_solmedicamentos," +
													" articulo," +
													" fecha_solicitud," +
													" dias_tratamiento," +
													" numero_orden_confirmada," +
													" es_solmedicamentos_cofirmada," +
													" usuario_confirma," +
													" fecha_confirma," +
													" hora_confirma" +
													" ) " +
													"values(?,?,?,?,?,?,?,?,?,?,?)";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			for(int i=0;i<Utilidades.convertirAEntero(articulosConfirmacion.get("numRegistros")+"");i++)
			{
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_conf_ordenes"));
				ps.setObject(2, articulosConfirmacion.get("codigoorden_"+i)+"");
				ps.setObject(3, articulosConfirmacion.get("solicitudmedicamento_"+i)+"");
				ps.setObject(4, articulosConfirmacion.get("articulo_"+i)+"");
				ps.setObject(5, articulosConfirmacion.get("fecha_"+i)+"");
				ps.setObject(6, articulosConfirmacion.get("diastratamiento_"+i)+"");
				ps.setObject(7, codigoOrden+"");
				ps.setObject(8, esSolOrdenConfirmada+"");
				ps.setObject(9, loginUsuario+"");
				ps.setObject(10, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))+"");
				ps.setObject(11, UtilidadFecha.getHoraActual(con)+"");
				ps.executeUpdate();

			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Obtener centros Costos usuario
	 * @param Connection con
	 * @param String usuarioLogin
	 * @param int Institucion 
	 * @param String tipoArea
	 * @param String codigoViaIngreso
	 * @return
	 */
	public static HashMap obtenerCentrosCostoUsuario(Connection con,String usuarioLogin, int institucion, String tipoArea, String codigoViaIngreso) 
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement("SELECT cc.codigo, cc.nombre " +
					"FROM centros_costo_usuario u " +
					"INNER JOIN centros_costo cc ON (cc.codigo = u.centro_costo AND u.usuario='"+usuarioLogin+"') " +
					"INNER JOIN centro_costo_via_ingreso vi ON(vi.centro_costo=cc.codigo) " +
					"WHERE cc.institucion = "+institucion+" AND cc.tipo_area !="+tipoArea+" " +
							"AND cc.codigo != 0 AND vi.via_ingreso = "+codigoViaIngreso+" " +
					"GROUP BY cc.codigo, cc.nombre ORDER BY cc.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener Centros Costo Usuario de SqlBaseUtilidadDao: "+e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static double obtenerAbonosDisponiblesPaciente(Connection con, int codigoPaciente, int ingreso) 
	{
		String consulta="SELECT getabonodisponible(?,?) AS abono_disponible FROM dual ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setObject(1, codigoPaciente+"");
			ps.setInt(2, ingreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getDouble(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param concepto
	 * @param institucion
	 * @return
	 */
	public static int obtenerTipoConceptoTesoreria(Connection con, String concepto, int institucion) 
	{
		String consulta="SELECT  codigo_tipo_ingreso from conceptos_ing_tesoreria where codigo=? and institucion=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setObject(1, concepto+"");
			ps.setInt(2, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param reciboCaja
	 * @param institucion
	 * @return
	 */
	public static boolean reciboCajaConPagosGeneralEmpresa(Connection con, String reciboCaja, int institucion) 
	{
		String consulta="SELECT case when count(1) >0 then 'S' else 'N' end from pagos_general_empresa where tipo_doc = ? and documento=? and institucion=? and estado=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, ConstantesBD.codigoTipoDocumentoPagosReciboCaja);
			ps.setString(2, reciboCaja);
			ps.setInt(3, institucion);
			ps.setInt(4, ConstantesBD.codigoEstadoPagosAplicado);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return UtilidadTexto.getBoolean(rs.getString(1));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Mï¿½todo para encontrar el esquema tarifario que se debe tomar para calcular la tarifa de un servicio/articulo.
	 * @param con
	 * @param subCuenta 
	 * @param servicio
	 * @param esServicio
	 * @return
	 */
	public static int obtenerEsquemasTarifarioServicioArticulo(	Connection con, 
																String subCuenta,
																int contrato, 
																int servart, 
																boolean esServicio,
																String fechaCalculoVigencia,
																int centroAtencion) throws BDException 
	{
		
		int resultado=ConstantesBD.codigoNuncaValido;
		if(servart<=0){
			return resultado;
		}
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		PreparedStatement pst4=null;
		PreparedStatement pst5=null;
		PreparedStatement pst6=null;
		PreparedStatement pst7=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		ResultSet rs4=null;
		ResultSet rs5=null;
		ResultSet rs6=null;
		ResultSet rs7=null;
		
		try	{
			//solo se hace el proceso si se envio el servicio o el articulo.
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			if(fecha.length()>10)
			{
				fecha=fecha.substring(0, 10);
			}
			if(esServicio)
			{
				String consulta="SELECT grupo_servicio as gruposervicio from servicios where codigo = "+servart;
				pst= con.prepareStatement(consulta);
				rs=pst.executeQuery();
				if(rs.next())
				{
					int grupo=rs.getInt(1);
					String consulta2="SELECT esquema_tarifario from esq_tar_proc_sub_cuentas where grupo_servicio="+grupo+" and sub_cuenta="+Utilidades.convertirALong(subCuenta);
					pst2= con.prepareStatement(consulta2);
					rs2=pst2.executeQuery();
					if(rs2.next()){
						resultado=rs2.getInt(1); 
					}
					else
					{
						String consulta3="SELECT esquema_tarifario from esq_tar_proc_sub_cuentas where grupo_servicio is null and sub_cuenta="+Utilidades.convertirALong(subCuenta);
						pst3= con.prepareStatement(consulta3);
						rs3=pst3.executeQuery();
						if(rs3.next()){
							resultado=rs3.getInt(1); 
						}
						else
						{
							String consulta4="SELECT esquema_tarifario from esq_tar_procedimiento_contrato where fecha_vigencia <='"+fecha+"' and contrato="+contrato+" and centro_atencion="+centroAtencion+" and grupo_servicio="+grupo+" order by fecha_vigencia desc";
							pst4= con.prepareStatement(consulta4);
							rs4=pst4.executeQuery();
							if(rs4.next()){
								resultado=rs4.getInt(1); 
							}
							else
							{
								String consulta5="SELECT esquema_tarifario from esq_tar_procedimiento_contrato where fecha_vigencia <='"+fecha+"' and contrato="+contrato+" and centro_atencion="+centroAtencion+" and grupo_servicio is null order by fecha_vigencia desc";
								pst5= con.prepareStatement(consulta5);
								rs5=pst5.executeQuery();
								if(rs5.next()){
									resultado=rs5.getInt(1); 
								}
								else
								{
									String consulta6="SELECT esquema_tarifario from esq_tar_procedimiento_contrato where fecha_vigencia <='"+fecha+"' and contrato="+contrato+" and centro_atencion is null and grupo_servicio="+grupo+" order by fecha_vigencia desc";
									pst6= con.prepareStatement(consulta6);
									rs6=pst6.executeQuery();
									if(rs6.next()){
										resultado=rs6.getInt(1); 
									}
									else
									{
										String consulta7="SELECT esquema_tarifario from esq_tar_procedimiento_contrato where fecha_vigencia <='"+fecha+"' and contrato="+contrato+" and centro_atencion is null and grupo_servicio is null order by fecha_vigencia desc";
										pst7= con.prepareStatement(consulta7);										
										rs7=pst7.executeQuery();
										if(rs7.next())
										{
											resultado=rs7.getInt(1); 
										}
									}
								}
							}
						}
					}
				}
			}
			else
			{
				String consulta="SELECT clase from articulo a inner join subgrupo_inventario sui on(sui.codigo=a.subgrupo) where a.codigo = "+servart;
				pst= con.prepareStatement(consulta);
				rs=pst.executeQuery();
				if(rs.next())
				{
					int clase=rs.getInt(1);
					String consulta2="SELECT esquema_tarifario from esq_tar_invt_sub_cuentas where clase_inventario="+clase+" and sub_cuenta="+Utilidades.convertirALong(subCuenta);
					pst2= con.prepareStatement(consulta2);
					rs2=pst2.executeQuery();
					if(rs2.next()){
						resultado=rs2.getInt(1);
					}
					else
					{
						String consulta3="SELECT esquema_tarifario from esq_tar_invt_sub_cuentas where clase_inventario is null and sub_cuenta="+Utilidades.convertirALong(subCuenta);
						pst3= con.prepareStatement(consulta3);
						rs3=pst3.executeQuery();
						if(rs3.next()){
							resultado=rs3.getInt(1);
						}
						else
						{
							String consulta4="SELECT esquema_tarifario from esq_tar_inventarios_contrato where to_char(fecha_vigencia, '"+ConstantesBD.formatoFechaBD+"') <='"+fecha+"' and contrato="+contrato+" and centro_atencion="+centroAtencion+" and clase_inventario="+clase+" order by fecha_vigencia desc";
							pst4= con.prepareStatement(consulta4);
							rs4=pst4.executeQuery();
							if(rs4.next()){
								resultado=rs4.getInt(1); 
							}
							else
							{
								String consulta5="SELECT esquema_tarifario " +
										"FROM " +
											"esq_tar_inventarios_contrato " +
										"WHERE " +
											"to_char(fecha_vigencia, '"+ConstantesBD.formatoFechaBD+"') <='"+fecha+"' " +
											"and contrato="+contrato+" " +
											"and centro_atencion="+centroAtencion+" "+
											"and clase_inventario is null order by fecha_vigencia desc";
								pst5= con.prepareStatement(consulta5);
								rs5=pst5.executeQuery();
								if(rs5.next()){
									resultado=rs5.getInt(1); 
								}
								else
								{
									String consulta6="SELECT esquema_tarifario " +
									"FROM " +
										"esq_tar_inventarios_contrato " +
									"WHERE " +
										"to_char(fecha_vigencia, '"+ConstantesBD.formatoFechaBD+"') <='"+fecha+"' " +
										"and contrato="+contrato+" " +
										"and centro_atencion is null "+
										"and clase_inventario="+clase+" order by fecha_vigencia desc";
									pst6= con.prepareStatement(consulta6);
									rs6=pst6.executeQuery();
									if(rs6.next()){
										resultado=rs6.getInt(1);
									}
									else
									{
										String consulta7="SELECT esquema_tarifario " +
										"FROM " +
											"esq_tar_inventarios_contrato " +
										"WHERE " +
											"to_char(fecha_vigencia, '"+ConstantesBD.formatoFechaBD+"') <='"+fecha+"' " +
											"and contrato="+contrato+" " +
											"and centro_atencion is null "+
											"and clase_inventario is null order by fecha_vigencia desc";
										pst7= con.prepareStatement(consulta7);
										rs7=pst7.executeQuery();
										if(rs7.next()){
											resultado=rs7.getInt(1); 
										}
									}
								}
							}
						}
					}
				}
				
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs7 != null){
					rs7.close();
				}
				if(pst7 != null){
					pst7.close();
				}
				if(rs6 != null){
					rs6.close();
				}
				if(pst6 != null){
					pst6.close();
				}
				if(rs5 != null){
					rs5.close();
				}
				if(pst5 != null){
					pst5.close();
				}
				if(rs4 != null){
					rs4.close();
				}
				if(pst4 != null){
					pst4.close();
				}
				if(rs3 != null){
					rs3.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerEsquemasTarifarioServicioArticulo");
		return resultado;
	}
	
	/**
	 * @param consulta 
	 * @return
	 */
	public static boolean betweenFechas(String consulta)
	{
		
		
		Connection con=UtilidadBD.abrirConexion();
		
		logger.info("betweenFechas-->"+consulta);
		boolean retorna=false;
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna=rs.getInt(1)>0;
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		logger.info("retorna-->"+retorna);
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param esServicios
	 * @return
	 */
	public static boolean requiererJustificacionServiciosArticulos(Connection con, int codigoIngreso, boolean esServicios) 
	{
		String cadena="SELECT count(1) from convenios c inner join sub_cuentas sc on(sc.convenio=c.codigo) where sc.ingreso=?";
		if(esServicios)
			cadena=cadena+" and requiere_justificacion_serv ='"+ConstantesBD.acronimoSi+"'";
		else
			cadena=cadena+" and requiere_justificacion_art ='"+ConstantesBD.acronimoSi+"'";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, codigoIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				int resultado=rs.getInt(1);
				rs.close();
				ps.close();
				return resultado>0;
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
	 * @param subCuenta
	 * @return
	 */
	public static int obtenerCodigoContratoSubcuenta(Connection con, double subCuenta) 
	{
		String cadena="SELECT contrato from sub_cuentas where sub_cuenta="+(long)subCuenta;

		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
		//	ps.setObject(1, subCuenta+"");
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				int resultado=rs.getInt(1);
				rs.close();
				ps.close();
				return resultado;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Mï¿½todo encargado de  obtener los tipos de convenio
	 * en un arrayList de Hashmap
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerTiposConvenio(Connection con, String institucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String consulta = "SELECT tc.codigo As codigo, tc.descripcion As descripcion, tc.tipo_regimen As tipo_regimen, tc.cuenta_contable As cuenta_contable," +
							  "		  tc.clasificacion As clasificacion FROM tipos_convenio tc  ";
			
			
			if (Utilidades.convertirAEntero(institucion)>0)
				consulta+="  WHERE tc.institucion="+institucion;
			
			
			consulta+=" order by tc.descripcion asc";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("descripcion",rs.getObject("descripcion"));
				mapa.put("tipoRegimen",rs.getObject("tipo_regimen"));
				mapa.put("cuentaContable",rs.getObject("cuenta_contable"));
				mapa.put("clasificacion",rs.getObject("clasificacion"));
			
				resultado.add(mapa);
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener tipos de convenio : "+e);
		}
		return resultado;
	}

	/**
	 * Mï¿½todo que consulta los motivos de apertura o de cierre
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMotivosAperturaCierre(Connection con, String tipo)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String consulta = "SELECT codigo, descripcion, tipo, activo from mot_cierre_apertura_ingresos where tipo='"+tipo+"'";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());

			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("descripcion",rs.getObject("descripcion"));
				mapa.put("tipo",rs.getObject("tipo"));
				mapa.put("activo",rs.getObject("activo"));
				resultado.add(mapa);
			}
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener Tipos de Motivo Cierre/Apertura Ingresos : "+e);
		}
		return resultado;
	}
	
	/**
	 * Mï¿½todo que consulta el ultimo egreso que viene de una Evolucion
	 * @param con
	 * @param codigoPaciente
	 * @param viaIngreso
	 * @param tipoBD 
	 * @return
	 */
	public static HashMap<String, Object> consultaUltimoEgresoEvolucion (Connection con, int codigoPaciente, int viaIngreso, int tipoBD)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try
		{
			String consulta = "SELECT "+ 
				"e.cuenta, "+ 
				"e.fecha_egreso AS fecha, "+ 
				"substr(e.hora_egreso||'',0,6) AS hora, "+ 
				"d.acronimo, "+ 
				"d.tipo_cie AS cie, "+ 
				"d.nombre "+ 
				"FROM cuentas c "+ 
				"INNER JOIN egresos e ON(e.cuenta = c.id) "+ 
				"INNER JOIN diagnosticos d ON (d.acronimo=e.diagnostico_principal AND d.tipo_cie=e.diagnostico_principal_cie) "+ 
				"WHERE "+ 
				"c.codigo_paciente = ? AND "+ 
				"c.via_ingreso = ? AND "+ 
				"e.fecha_egreso is not null AND "+ 
				"e.usuario_responsable is not null ";
			switch(tipoBD)
			{
				case DaoFactory.POSTGRESQL:
					consulta += "ORDER BY e.fecha_egreso DESC, e.hora_egreso DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
				break;
				case DaoFactory.ORACLE:
					consulta += " AND rownum = 1 ORDER BY e.fecha_egreso DESC, e.hora_egreso DESC";
				break;
			}
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			logger.info("consulta>>>>>>>>>>>"+consulta);
			logger.info("codigoPaciente>>>>>>>>>>>"+codigoPaciente);
			logger.info("viaIngreso>>>>>>>>>>>"+viaIngreso);
			
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, viaIngreso);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
					
			resultados.put("INDICES",indicesUltimoEgreso);
			pst.close();
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de ultimo egreso por devolucion");
		}
		return resultados;
	}
	
	/**
	 * Mï¿½todo para consultar Ultimo Ingreso Anterior y Maracar Reingreso Actual
	 * @param con
	 * @param codIngreso
	 * @param codPaciente
	 * @param tipoBD 
	 * @return
	 */
	public static boolean marcarReingreso (Connection con, int codIngreso, int codPaciente, int tipoBD)
	{
		
		int codigoIngresoAnterior = ConstantesBD.codigoNuncaValido;
		PreparedStatementDecorator pst;
		 
		try
		{
			String consulta = getUltimoIngresoAnterior;
			
			switch(tipoBD)
			{
				case DaoFactory.ORACLE:
					consulta += " AND rownum = 1 ORDER BY fecha_ingreso DESC, hora_ingreso DESC";
				break;
				case DaoFactory.POSTGRESQL:
					consulta += " ORDER BY fecha_ingreso DESC, hora_ingreso DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
				break;
			}
			
			
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			logger.info("consulta>>>>>>>>>>>>>"+consulta);
			logger.info("codPaciente>>>>>>>>>>>>>"+codPaciente);
			
			pst.setInt(1, codPaciente);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigoIngresoAnterior = rs.getInt("id");
					
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de ultimo ingreso anterior por evolucion");
		}
		if(codigoIngresoAnterior>0)
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(marcarReingreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("marcarReingreso>>>>>>>>>>>>>"+marcarReingreso);
				logger.info("codigoIngresoAnterior>>>>>>>>>>>>>"+codigoIngresoAnterior);
				logger.info("codIngreso>>>>>>>>>>>>>"+codIngreso);
				
				ps.setInt(1, codigoIngresoAnterior);
				ps.setInt(2, codIngreso);
				
				if(ps.executeUpdate()>0)
				{
					ps.close();
					return true;
				}
				
			}
			catch (SQLException e)
			{
				logger.info("Error. Maracando el Reingreso>>>>>>>>>"+e);
			}
		}
		else
			logger.info("Error. Marcando el Reingreso. La Consulta del Ultimo ingresoAnterior viene vacia");
		return false;
	}
	
	/**
	 * Mï¿½todo que consulta los usuarios de apertura o de cierre de ingresos
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerUsuarioAperturaCierre(Connection con, String tipo)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		String consulta = "";
		try
		{
			if(tipo.equals(ConstantesIntegridadDominio.acronimoCierreIngreso))
			{
				consulta = "SELECT "+
								"ci.usuario_cierre AS usuario, "+
								"getnombreusuario2(ci.usuario_cierre) AS nombre "+
							"FROM "+
								"cierre_ingresos ci "+
								"INNER JOIN mot_cierre_apertura_ingresos mcai ON (ci.motivo_cierre = mcai.codigo AND ci.institucion = mcai.institucion) "+
							"WHERE "+
								"mcai.tipo = '"+tipo+"' "+
							"GROUP BY "+
								"usuario_cierre "+
							"ORDER BY "+
								"nombre";
			}
			else if(tipo.equals(ConstantesIntegridadDominio.acronimoAperturaIngreso))
			{
				consulta = "SELECT "+
								"ci.usuario_apertura AS usuario, "+
								"getnombreusuario2(ci.usuario_apertura) AS nombre "+
							"FROM "+
								"cierre_ingresos ci "+
								"INNER JOIN mot_cierre_apertura_ingresos mcai ON (ci.motivo_apertura = mcai.codigo AND ci.institucion = mcai.institucion) "+
							"WHERE "+
								"mcai.tipo = '"+tipo+"' "+
							"GROUP BY "+
								"usuario_apertura "+
							"ORDER BY "+
								"nombre";
			}
			
			logger.info("\n===>Consulta de Usuario segun el Tipo "+tipo+": "+consulta);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("usuario", rs.getObject("usuario"));
				mapa.put("nombre", rs.getObject("nombre"));
				resultado.add(mapa);
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtener los usuarios segun el motivo (Cierre/Apertura) ingreso: "+e);
		}
		return resultado;
	}
	
	
	/**
	 * Consulta la informaciï¿½n de la Unidades de Campo
	 * @param Connection con
	 * @param HasMap parametros
	 * */
	public static ArrayList consultarUnidadesCampoParam(Connection con, HashMap parametros)
	{	
		ArrayList array = new ArrayList();
		HashMap mapa;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarUnidadesCampoParam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				mapa = new HashMap();
				mapa.put("codigo",rs.getString(1));
				mapa.put("nombre",rs.getString(2));
				mapa.put("acronimo",rs.getString(3));
				array.add(mapa);
			}	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return array;		
	}
	
	/**
	 * Consulta la informaciï¿½n de Tipo Campos
	 * @param Connection con
	 * @param HasMap parametros
	 * */
	public static ArrayList consultarTiposCamp(Connection con, HashMap parametros)
	{	
		ArrayList array = new ArrayList();
		HashMap mapa;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarTiposCampo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				mapa = new HashMap();
				mapa.put("codigo",rs.getString(1));
				mapa.put("nombre",rs.getString(2));				
				array.add(mapa);
			}	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return array;		
	}
		
	/**
	 * Mï¿½todo que consulta todos los centros de costo por via de ingreso segun tipo de area
	 * @param con
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public static HashMap<String, Object> consultaCentrosCostoFiltros (Connection con)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaCentrosCostoFiltros, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
					
			resultados.put("INDICES",indicesCentrosCFiltros);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de centros de costo por via de ingreso filtros");
		}
		return resultados;
	}
	
	/**
	 * 
	 * @param con
	 * @param funParam
	 * @param parametros
	 * @return
	 */
	public static ArrayList consultarComponentesParam(Connection con, String funParam, HashMap parametros) 
	{
		ArrayList array = new ArrayList();
		HashMap mapa;
		
		
		String cadena = strConsultarComponentesParam;
		
		if(parametros.containsKey("codigoComponentesInsertados") 
				&& !parametros.get("codigoComponentesInsertados").toString().equals(""))
			cadena+=" AND c.codigo NOT IN ("+parametros.get("codigoComponentesInsertados")+"-1)  ";
		
		cadena+=" ORDER BY nombre ";
		
		logger.info("Consulta strConsultarComponentesParam >>"+strConsultarComponentesParam+"   funParam>>"+funParam );
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, Utilidades.convertirAEntero(funParam));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				mapa = new HashMap();
				mapa.put("codigo",rs.getString(1));
				mapa.put("nombre",rs.getString(2));
				mapa.put("tipocomponente",rs.getString(3));
				array.add(mapa);
			}	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return array;
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static ArrayList consultarEscalasParam(Connection con, HashMap parametros) 
	{
		ArrayList array = new ArrayList();
		boolean indW = false;
		HashMap mapa;
		
		String cadena = strConsultarEscalasParam;
		
		if(parametros.containsKey("codigoEscalaInsertados") 
				&& !parametros.get("codigoEscalaInsertados").toString().equals(""))
		{
			cadena+=" WHERE codigo_pk NOT IN ("+parametros.get("codigoEscalaInsertados")+"-1) ";
			indW = true;
		}
		
		if(parametros.containsKey("mostrarModificacion") 
				&& !parametros.get("mostrarModificacion").toString().equals(""))
		{
			if(indW)
				cadena+=" AND mostrar_modificacion = '"+parametros.get("mostrarModificacion")+"' ";
			else				
				cadena+=" WHERE mostrar_modificacion = '"+parametros.get("mostrarModificacion")+"' ";
			
			indW = true;
		}		
		
		cadena+= " ORDER BY nombre ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				mapa = new HashMap();
				mapa.put("codigo_pk",rs.getString(1));
				mapa.put("codigo",rs.getString(2));
				mapa.put("nombre",rs.getString(3));
				array.add(mapa);
			}	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return array;
	}
	
	/**
	 * Mï¿½todo consulta Tipo de Monitoreo por Centro de Costo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static HashMap<String, Object> consultaTipoMonitoreoxCC (Connection con, int centroCosto)
	{
		String [] indices={"codigo_","cc_","prioridad_","nombre_"};
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaTipoMonitoreoxCC, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1, centroCosto);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
					
			resultados.put("INDICES",indices);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Tipo de Monitoreo por Centros de Costo");
		}
		return resultados;
	}
	
	/**
	 * Mï¿½todo para Insertar un registro a Ingresos Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @param indicativo
	 * @param tipoMonitoreo
	 * @param usuario
	 * @param strIsertarIngresoCuidadosEspeciales 
	 * @return
	 */
	public static boolean insertarIngresoCuidadosEspeciales (Connection con, int ingreso, String estado, String indicativo, int tipoMonitoreo, String usuario, int evolucion, int valoracion, String centroCosto, String strIsertarIngresoCuidadosEspeciales, String secuenciaStr)
	{
		logger.info("INGRESOOOOOOOOOOO JOJEEEEE>>>>>>>>>>>>>>>>>>>>>>"+ingreso);
		logger.info("TIPO JOOOJSEEEEEEEEE>>>>>>>>>>>>>>>"+tipoMonitoreo);
		String cadenaEV="INSERT INTO ingresos_cuidados_especiales (codigo," +
									"ingreso," +
									"estado," +
									"indicativo," +
									"tipo_monitoreo,";
									if(evolucion!=0)
									{
										cadenaEV+="evolucion_orden,";
									}
									else
									{
										if(valoracion!=0)
											cadenaEV+="valoracion_orden,";
									}
									cadenaEV+="usuario_resp," +
									"fecha_resp," +
									"hora_resp," +
									"usuario_modifica," +
									"fecha_modifica," +
									"hora_modifica," +
									"centro_costo" +
									") " +
						"VALUES ("+secuenciaStr+" ," +
									"?," +
									"?," +
									"?," +
									"?,";
									if(evolucion!=0)
									{
										cadenaEV+="?,";
									}
									else
									{
										if(valoracion!=0)
											cadenaEV+="?,";
									}
									cadenaEV+="?," +
									"CURRENT_DATE," +
									""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
									"?," +
									"CURRENT_DATE," +
									""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
									"?" +
									") ";							
		
		if(evolucion != 0 || valoracion != 0)
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEV,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1, ingreso);
				ps.setString(2, estado);
				ps.setString(3, indicativo);
				ps.setInt(4, tipoMonitoreo);
				logger.info("valor de la evolucion: "+evolucion);
				logger.info("valor de la valoracion: "+valoracion);
				if(evolucion!=0)
				{
					ps.setInt(5, evolucion);
				}
				else
				{
					if(valoracion!=0)
						ps.setInt(5, valoracion);
				}
				ps.setString(6, usuario);
				ps.setString(7, usuario);
				ps.setString(8, centroCosto);
				
				if(ps.executeUpdate()>0)
					return true;
			}
			catch (SQLException e)
			{
				logger.error(e+" Error Insertando un Ingreso Cuidados Especiales CON Evolucion o Valoracion");
			}
		}
		else
		{		
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strIsertarIngresoCuidadosEspeciales,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1, ingreso);
				ps.setString(2, estado);
				ps.setString(3, indicativo);
				ps.setInt(4, tipoMonitoreo);
				ps.setString(5, usuario);
				ps.setString(6, usuario);
				ps.setString(7, centroCosto);
				
				if(ps.executeUpdate()>0)
					return true;
			}
			catch (SQLException e)
			{
				logger.error(e+" Error Insertando un Ingreso Cuidados Especiales sin Evolucion o Valoracion");
			}
		}
		return false;
	}
	
	/**
	 * Mï¿½todo para actualizar estado a Finalizado de Ingresos Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean actualizarEstadoCuidadosEspeciales(Connection con, HashMap campos)
	{
		try
		{
			int idIngreso = Integer.parseInt(campos.get("idIngreso").toString());
			String usuario = campos.get("usuario").toString();
			String fecha = campos.get("fecha").toString();
			String hora = campos.get("hora").toString();
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strActualizarEstadoCuidadosEspeciales,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1, usuario);
			if(fecha.equals(""))
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			else
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			if(hora.equals(""))
				ps.setString(3,UtilidadFecha.getHoraActual(con));
			else
				ps.setString(3,UtilidadFecha.convertirHoraACincoCaracteres(hora));
			ps.setInt(4,idIngreso);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.error(e+" Error Actualizadno el estado a Finalizado de un Ingreso Cuidados Especiales");
		}
		return false;
	}
	
	/**
	 * Mï¿½todo verificar Ingreso en Cuidados Especiales y requiere valoracion
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int verificacionValoracionIngresoCuidadosEspeciales(Connection con, int ingreso)
	{
		String [] indices={"codigo_","ingreso_","estado_","indicativo_","tipom_","valoracion_"};
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		int tipom=0, valoracion=0;
		String requierev="";
		
		PreparedStatementDecorator pst;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strExistenciaIngresoCuidadosEspeciales, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1, ingreso);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
					
			resultados.put("INDICES",indices);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Existencia de Ingreso Cuidados Especiales");
		}
		
		if(Integer.parseInt(resultados.get("numRegistros").toString())>0)
		{
			for(int z=0;z<Integer.parseInt(resultados.get("numRegistros").toString());z++)
			{
				tipom=Integer.parseInt(resultados.get("tipom_"+z).toString());
				valoracion=Integer.parseInt(resultados.get("valoracion_"+z).toString());
			}
			
			if(tipom>0)
			{
				String [] indicesr={"codigo_","requierev_"};
				try
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(strRequiereValoracionTipoM, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					
					pst.setInt(1, tipom);
					
					resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
							
					resultados.put("INDICES",indicesr);
				}
				catch (SQLException e)
				{
					logger.error(e+" Error en consulta de Requiere Valoracion en Tipos Monitoreo");
				}
				
				if(Integer.parseInt(resultados.get("numRegistros").toString())>0)
				{
					for(int a=0;a<Integer.parseInt(resultados.get("numRegistros").toString());a++)
					{
						requierev=resultados.get("requierev_"+a).toString();
					}
					
					if(!requierev.equals(""))
					{
						if(requierev.equals("S"))
						{
							if(valoracion>0)
								return 2;
							else
								return 3;
						}
						else
							return 4;
					}
				}
			}
		}
		
		return 1;
	}
	
	
	/**
	 * Mï¿½todo consulta Tipo de Mpnitoreo poir un Ingreso de Cuidados Especiales
	 * @param ingreso
	 * @return
	 */
	public static String consultaTipoMonitoreoxCE(int ingreso)
	{
		String tipo="";
		Connection con=null;
		ResultSetDecorator rs;
		PreparedStatementDecorator ps;
        try
        {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
        }
        catch(SQLException e)
        {
            logger.warn("No se pudo abrir la conexion "+e.toString());
        }
        try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(strConsultaTipoMonitoreoxIngresoCE,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ingreso);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				tipo = rs.getString("nombre");
			}
			UtilidadBD.closeConnection(con);
			return tipo;
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta del Tipo de Monitoreo por Ingreso de Cuidados Espceciales");
		}
		
		UtilidadBD.closeConnection(con);
		return "";
	}

	/**
	 * Mï¿½todo encargado de devolver los tipo de paciente
	 * segun la via de ingreso en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerTiposPacientePorViaIngreso(Connection con, String viaIngreso)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena= "";
			PreparedStatementDecorator pst = null;
			
			if(viaIngreso.equals(""))
			{
				cadena="SELECT DISTINCT '' AS viaingreso, tipo_paciente AS tipopaciente, getnombretipopaciente(tipo_paciente) AS nomtipopaciente FROM tip_pac_via_ingreso ORDER BY tipo_paciente";
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else if(viaIngreso.split(",").length>1)
			{
				cadena="SELECT DISTINCT '' AS viaingreso, tipo_paciente AS tipopaciente, getnombretipopaciente(tipo_paciente) AS nomtipopaciente FROM tip_pac_via_ingreso WHERE via_ingreso IN ("+viaIngreso+") ORDER BY tipo_paciente";
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				cadena="SELECT via_ingreso AS viaingreso, tipo_paciente AS tipopaciente, getnombretipopaciente(tipo_paciente) AS nomtipopaciente FROM tip_pac_via_ingreso WHERE via_ingreso = ? ORDER BY tipo_paciente";
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, Integer.parseInt(viaIngreso));
			}
			
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("viaingreso",rs.getObject("viaingreso"));
				mapa.put("tipopaciente",rs.getObject("tipopaciente"));
				mapa.put("nomtipopaciente",rs.getObject("nomtipopaciente"));
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerViasIngreso de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}

	/**
	 * Mï¿½todo encargado de devolver los motivos de devolucion
	 * en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerMotivosDevolucionInventarios(Connection con, int institucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT codigo AS codigomotivo, descripcion AS desmotivo FROM mot_devolucion_inventario WHERE activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND institucion = ? ORDER BY descripcion";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigomotivo",rs.getObject("codigomotivo"));
				mapa.put("desmotivo",rs.getObject("desmotivo"));
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerViasIngreso de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public static int obtenerGrupoServicio(Connection con, int servicio) 
	{
		String cadena="SELECT grupo_servicio from servicios where codigo = "+servicio;
		int respuesta=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error obtenerGrupoServicio "+ e);
		}
		return respuesta;
	}

	
		
	/**
	 * Consulta cuantas solicitudes se encuentran o no en los estado medicos dados a partir de una cuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int consultarCuantosSolicitudesEstadoMedico(Connection con, HashMap parametros) 
	{		
		try
		{
			String cadena = strCuantosSolicitudesEstadoClinica;
			
			if(parametros.get("noEsten").toString().equals(ConstantesBD.acronimoNo))
			{
				cadena += " NOT IN ("+parametros.get("estadosHistoriaMed").toString()+") ";
			}
			else
			{
				cadena += " IN ("+parametros.get("estadosHistoriaMed").toString()+") ";
			}
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,parametros.get("cuenta"));
			ps.setObject(2,parametros.get("tipo"));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())			
				return rs.getInt(1);			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @return
	 */
	public static int obtenertipoTarifarioEsquema(Connection con, int esquemaTarifario) 
	{
		String cadena="SELECT tarifario_oficial from esquemas_tarifarios where codigo="+esquemaTarifario;
		int respuesta=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error obtenertipoTarifarioEsquema "+ e);
		}
		return respuesta;
	}

	/**
	 * Mï¿½todo encargado de devolver los motivos de devolucion
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerNaturalezasArticulo(Connection con, int institucion)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena="SELECT acronimo AS acronimo, nombre AS nombre, codigo_interfaz AS codigointerfaz FROM naturaleza_articulo WHERE institucion = ? ORDER BY nombre";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("acronimo",rs.getObject("acronimo"));
				mapa.put("nombre",rs.getObject("nombre"));
				mapa.put("codigointerfaz",rs.getObject("codigointerfaz"));
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNaturalezasArticulo de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}

	
	/**
	 * Utilidad para determinar si un articulo es medicamento o no segun la naturaleza del mismo
	 * param1 -> conexion
	 * param2 -> acronimo de la naturaleza del articulo
	 * paarm3 -> codigo de la institucion 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String esMedicamento(Connection con, String acronimo, int Institucion)
	{
		String numeroSolicitudes="";
		try
		{
			String cadena="SELECT na.es_medicamento AS esMedicamento FROM naturaleza_articulo na WHERE na.acronimo='" + acronimo +
			"' AND na.institucion= " + Institucion + "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				numeroSolicitudes = rs.getString(1);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFechaAdmisionCirugia: "+e);
			numeroSolicitudes = Integer.toString(ConstantesBD.codigoNuncaValido);
		}
		return numeroSolicitudes;
	}
	
	
	
	/**
	 * Mï¿½todo implementado para traer las
	 * naturalezas de articulo filtradas por
	 * la institucion
	 * @param con
	 * @return
	 */
	public static String obtenerNombreNaturalezasArticulo(Connection con, String acronimoNaturaleza)
	{
		try
		{
			String cadena="SELECT nombre AS nombre FROM naturaleza_articulo WHERE acronimo = '"+acronimoNaturaleza+"' ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===>Nombre Naturaleza: "+cadena);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if (rs.next())
				return rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreNaturalezasArticulo de SqlBaseUtilidadesDao: "+e);
			return "";
		}
		return "";
	}

	/**
	 * Mï¿½todo encargado de consultar el codigo del centro
	 * de costo principal segun el centro de costo solicitado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static String obtenerCentroCostoPrincipal(Connection con, String centroCosto, String institucion)
	{
		String cadena = "SELECT centro_costo_principal FROM almacen_parametros WHERE codigo = ? AND institucion = ? ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, centroCosto);
			ps.setObject(2, institucion);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getString(1);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}
	
	/**
	 * Mï¿½todo que consuilta los totales de un factura para su impresion
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static HashMap<String, Object> consultaTotalesFactura (Connection con, int codFactura)
	{
		logger.info("VALOR DEL LA FACTURAA>>>>>>>>>>>>>>>>>>"+codFactura);
		logger.info("CONSULTA DE LOS TOTALES DE LA FACTURA>>>>>>>>>>>>>>>>>>>>>>>>>"+strTotalesFactura);
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strTotalesFactura, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1, codFactura);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			logger.info("ENTRO Y MIRE EL MAPAAA>>>>>>>>>>>>>>>>>>"+resultados);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Totales Factura.>>>");
		}
		return resultados;
	}

	/**
	 * Mï¿½todo encargado de consultar la descripcion del estado 
	 * de aplicacion de pagos segun el codigo del estado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static String obtenerNombreEstadoAplicacionPagos(Connection con, int codigoEstado)
	{
		String cadena = "SELECT descripcion FROM estados_aplicaciones_pagos WHERE codigo = ? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoEstado);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}

	/**
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static String obtenerNombreComponente(Connection con, String tipoComponente) 
	{
		String cadena = "SELECT nombre FROM tipos_componente WHERE codigo = ? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, tipoComponente);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @return
	 */
	public static String obtenerNombrePlantilla(Connection con, String plantilla) 
	{
		String cadena = "SELECT nombre FROM fun_param WHERE codigo = ? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, plantilla);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}
	
	
	/**
	 * Consulta los Centros de Costo para Area de Asocio de Cuentas segun Ingreso Cuidados Especiales
	 * @param con
	 * @param centroAtencion
	 * @param cuenta
	 * @param egresoV
	 * @return
	 */
	public static HashMap<String, Object> areasAsocioEspeciales (Connection con, int centroAtencion, int cuenta, boolean egresoV)
	{
		logger.info("CENTRO DE ATENCION>>>>>>>>>>>>>>>"+centroAtencion);
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		try
		{
			if(egresoV)
				pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaAreasAsocioCuidadosEspecialesV, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			else
				pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaAreasAsocioCuidadosEspecialesE, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1, centroAtencion);
			pst.setInt(2, cuenta);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			logger.info("MAPA RESULTADOS DESDE SQL >>>>>>>>>>>>>>>>>>>>>"+resultados);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Centros de Costo para Area de Asocio de Cuenta por Ingreso Cuidados Especiales.>>>");
		}
		if(egresoV)
			logger.info("CUENTA>>>>>>>>"+cuenta+">>>>>>>CONSULTA>>>>>>"+strConsultaAreasAsocioCuidadosEspecialesV);
		else
			logger.info("CUENTA>>>>>>>>"+cuenta+">>>>>>>CONSULTA>>>>>>"+strConsultaAreasAsocioCuidadosEspecialesE);
		return resultados;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public static String obtenerDescripcionComponente(Connection con, String codigoComponente) 
	{
		String cadena = "select nombre from componentes c inner join tipos_componente tc on(tc.codigo=c.tipo_componente) where c.codigo=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoComponente);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @return
	 */
	public static String obtenerDescripcionEscala(Connection con, String codigoEscala) 
	{
		String cadena = "select nombre from escalas where codigo_pk= ? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoEscala);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}

	/**
	 * @param con
	 * @param codigoInstitucion
	 * @param inactivos
	 * @return
	 */
	public static HashMap obtenerCentrosAtencionInactivos(Connection con, int codigoInstitucion, boolean inactivos)
	{
		HashMap mapa= new HashMap();
		String cadena =	"";
		
		if(inactivos)
			cadena = "SELECT consecutivo AS consecutivo, descripcion AS descripcion, activo AS activo FROM centro_atencion WHERE cod_institucion = ? ORDER BY descripcion";
		else
			cadena = "SELECT consecutivo AS consecutivo, descripcion AS descripcion, activo AS activo FROM centro_atencion WHERE cod_institucion = ? AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY descripcion";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo para Consultar la Evolucion de la cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static int consultaEvolucionCuentaUrgencias(Connection con, int cuenta)
	{
		PreparedStatementDecorator ps5;
		ResultSetDecorator rs2;
		int evolucion=0;
		try
		{
			ps5= new PreparedStatementDecorator(con.prepareStatement(strConsultaEvolucionUrgencias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps5.setInt(1, cuenta);
			
			rs2=new ResultSetDecorator(ps5.executeQuery());
			if(rs2.next()){
				evolucion = rs2.getInt("evolucion");
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta de la Sub Cuenta");
			return -1;
		}
		return evolucion;
	}
	
	/**
	 * Mï¿½todo para Consultar la Valoracion de la cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static int consultaValoracionCuentaUrgencias(Connection con, int cuenta)
	{
		PreparedStatementDecorator ps5;
		ResultSetDecorator rs2;
		int valoracion=0;
		try
		{
			ps5= new PreparedStatementDecorator(con.prepareStatement(strConsultaValoracionUrgencias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps5.setInt(1, cuenta);
			
			rs2=new ResultSetDecorator(ps5.executeQuery());
			if(rs2.next()){
				valoracion = rs2.getInt("numsol");
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta de la Sub Cuenta");
			return -1;
		}
		return valoracion;
	}

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerConceptosPagos(Connection con, int institucion)
	{
		ArrayList<HashMap<String, Object>> resultado = new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena = "SELECT codigo, descripcion, tipo, porcentaje FROM concepto_pagos WHERE institucion = ? ORDER BY descripcion";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("descripcion", rs.getObject("descripcion"));
				mapa.put("tipo", rs.getObject("tipo"));
				mapa.put("porcentaje", rs.getObject("porcentaje"));
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConceptosPagos de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}

	/**
	 * Mï¿½todo que consulta la SubCuenta de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int obtenerSubCuentaIngreso(Connection con, int ingreso)
	{
		@SuppressWarnings("unused")
		int evolucion = ConstantesBD.codigoNuncaValido;
		String consulta = "SELECT sub_cuenta AS subcuenta FROM sub_cuentas WHERE ingreso = ?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ingreso);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return evolucion = rs.getInt("subcuenta");
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta de la Sub Cuenta");
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public static String obtenerNombreTipoMonitoreo(Connection con, String tipoMonitoreo) 
	{
		String cadena = "select nombre from tipo_monitoreo where codigo= ? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, tipoMonitoreo);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el centro de costo principal "+e);
		}
		return "";
	}
	
	
	/**
	 * Mï¿½todo implementado para obtener el codigo del tralado de cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public static int getCodigoTrasladoUltimoTraslado(Connection con,int cuenta)
	{
		try
		{
			String consulta = getCodigoTrasladoUltimoTrasladoStr;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,cuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt(1);
			else 
				return ConstantesBD.codigoNuncaValido;
		}
		catch(SQLException e)
		{
			logger.error("Error en getUltimaCamaTraslado de SqlBaseUtilidadesDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Mï¿½todo que consulta si un Ingreso es un Preingreso sin importar estado
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean esPreingresoNormal(Connection con, int ingreso)
	{
		String preingreso="";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaPreingresoNormal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ingreso);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				preingreso = rs.getString("preingreso");
			if(preingreso!=null && !preingreso.equals(""))
				return true;
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta de mirar Ingreso es Preingreso Normal");
		}
		return false;
	}

	/**
	 * Mï¿½todo que consulta la descripcion de un tipo de convenio especifico
	 * @param con
	 * @param tipoConvenio
	 * @param codigoInstitucion
	 * @return
	 */
	public static String obtenerDescripcionTipoConvenio(Connection con, String tipoConvenio, int codigoInstitucion)
	{
		String cadena = "SELECT descripcion AS descripcion FROM tipos_convenio WHERE codigo = ? AND institucion = ? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, tipoConvenio);
			ps.setObject(2, codigoInstitucion);
			ResultSetDecorator resultado =new ResultSetDecorator(ps.executeQuery());
			logger.info("\n====>Consulta descripcion tipo convenio: "+cadena+" ===>Codigo Tipo Convenio: "+tipoConvenio+" ===>Institucion: "+codigoInstitucion);
			if(resultado.next())
				return resultado.getString(1);
		}
		catch (SQLException e)
		{
			logger.error("ERROR CONSULTANDO LA DESCRIPCION DEL TIPO DE CONVENIO"+e);
		}
		return "";
	}

	/**
	 * Mï¿½todo para obtener el nombre del tipo de paciente
	 * @param con
	 * @param tipoPaciente
	 * @return
	 */
	public static String obtenerNombreTipoPaciente(Connection con, String tipoPaciente)
	{
		try
		{
			String consultaStr = "SELECT nombre AS descripcion FROM tipos_paciente WHERE acronimo = ? ";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, tipoPaciente);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("descripcion");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoPaciente [SqlBaseUtilidadesDao] : "+e);
			return "";
		}
	}

	/**
	 * Mï¿½todo para obtener los deudores segï¿½n el tipo
	 * de deudores
	 * @param con
	 * @param tipoDeudor
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerDeudores(Connection con, String tipoDeudor)
	{
		ArrayList<HashMap<String, Object>> resultado = new ArrayList<HashMap<String, Object>>();
		try
		{
			String cadena = "SELECT d.tipo AS tipodeudor, d.codigo AS deudor, getidentificaciondeudor(d.codigo) AS identificacion, getdescripciondeudor(d.codigo,d.tipo) AS descripcion FROM deudores d WHERE d.tipo = '"+tipoDeudor+"' GROUP BY d.tipo, d.codigo ORDER BY descripcion ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("====>Consulta Deudores: "+cadena);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("tipodeudor", rs.getObject("tipodeudor"));
				mapa.put("deudor", rs.getObject("deudor"));
				mapa.put("identificacion", rs.getObject("identificacion"));
				mapa.put("descripcion", rs.getObject("descripcion"));
				resultado.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDeudores de SqlBaseUtilidadesDao: "+e);
		}
		return resultado;
	}

	/**
	 * Mï¿½todo que indica si una Entidad Financiera se encuentra
	 * inactiva ï¿½ activa, segï¿½n el consecutivo de la entidad
	 * financiera
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public static boolean obtenerActivoInactivoEntidadFinanciera(Connection con, int consecutivoEntidad)
	{
		try
		{
			String consultaStr = "SELECT activo AS activo FROM entidades_financieras WHERE consecutivo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, consecutivoEntidad);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getBoolean("activo");
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoPaciente [SqlBaseUtilidadesDao] : "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo para obtener el nombre del tipo de paciente
	 * @param con
	 * @param tipoPaciente
	 * @return
	 */
	public static String obtenerGruposTransValidasXCC(Connection con,int institucion,int centro_costo,int tipos_trans_inventario, int clase_inventario)
	{
		String resultado="";
		try
		{
			String consultaStr = "SELECT grupo_inventario||'' as grupos FROM trans_validas_x_cc_inven WHERE institucion = "+institucion+" AND centros_costo="+centro_costo+" AND tipos_trans_inventario="+tipos_trans_inventario+" AND clase_inventario="+clase_inventario;
			logger.info("obtenerGruposTransValidasXCC -- "+consultaStr);
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				resultado+=rs.getString("grupos");
				if(!rs.isLast())
				{
					resultado+=", ";
				}
			}
			
			pst.close();
			rs.close();
			
			if (UtilidadTexto.isEmpty(resultado))
				return ConstantesBD.codigoNuncaValido+"";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerGruposTransValidasXCC [SqlBaseUtilidadesDao] : "+e);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuentaConvenio
	 * @return
	 */
	public static String obtenerCuentaContable(Connection con, String cuentaConvenio) 
	{
		try
		{
			String consultaStr = "SELECT coalesce(getcuentacontable(?),'') as cuentaContable";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1, Utilidades.convertirADouble(cuentaConvenio));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("cuentaContable");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCuentaContable [SqlBaseUtilidadesDao] : "+e);
			return "";
		}
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoEmpresa
	 * @return
	 */
	public static String obtenerNombreEmpresa(Connection con, String codigoEmpresa) 
	{
		try
		{
			String consultaStr = "SELECT razon_social AS nombre FROM empresas WHERE codigo= ? ";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1, codigoEmpresa);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("nombre");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreTipoPaciente [SqlBaseUtilidadesDao] : "+e);
			return "";
		}
	}

	/**
	 * Mï¿½todo que retorna el codigo de procedimiento principal asociado al servicio principal antes ingresado
	 * @param con, codigoServicio
	 * @return
	 */
	public static int obtenerCodigoProcedimientoPrincipalIncluidos(Connection con, String codigoServicio)
	{
		String cadena = "SELECT codigo AS codigo FROM servi_ppalincluidos WHERE cod_servi_ppal = "+codigoServicio+" ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getInt(1);
			
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DE PROCEDIMIENTOS INCLUIDOS "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	
	/** Mï¿½todo que retorna el codigo de procedimiento principal asociado al servicio principal antes ingresado
	 * @param con, codigoServicio
	 * @return	 */
	public static int obtenerCodigoServicioIncluido(Connection con, String codigoServiPpal, String codigoServiInclu)
	{
		String cadena = "SELECT codigo AS codigo " +
				"FROM servi_incluido_servippal " +
				"WHERE cod_servippal = "+codigoServiPpal+" AND cod_servicio= "+ codigoServiInclu + " ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getInt(1);
			
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DE PROCEDIMIENTOS INCLUIDOS "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	/**
	 * Mï¿½todo encargado de consultar si hay una descripciï¿½n a textos respuesta procedimientos
	 * 
	 * 
	 */
	public static boolean existeDescripcionTextoProcedimiento (Connection con, String descripcionTexto, String servicio)
	{
		String cadenaConsulta = "select descripcion_texto from textos_resp_proc where descripcion_texto='"+descripcionTexto+"' " +
				" AND servicio="+servicio;
		try 
		{
			logger.info("===> Aquï¿½ estï¿½ la descripciï¿½n de texto: "+descripcionTexto);
			logger.info("===> Aquï¿½ estï¿½ el servicio: "+servicio);
			logger.info("===> Aquï¿½ estï¿½ la cadena de la consulta: "+cadenaConsulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DE PROCEDIMIENTOS INCLUIDOS "+e);
		}
		return false;
	}
	
	
	/**
	 * Mï¿½todo para Traer el codigo del Convenio a partir de un codigo Interfaz
	 * @author Andres Silva Monsalve
	 * @param codigoInterfaz
	 * @return
	 */
	public static int obtenerCodigoConvenioDeCodInterfaz(Connection con, String codigoInterfaz) 
	{
		String cadenaStr= "SELECT codigo from convenios where interfaz =?";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoInterfaz);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigo");	
						
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DE CONVENIO "+e);
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Mï¿½todo para Traer el codigo interfaz del Convenio a partir de un codigo del convenio
	 * @author Andres Silva Monsalve
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerCodigoInterfazConvenioDeCodigo(Connection con, int codigoConvenio) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String cadenaStr= "SELECT coalesce(interfaz,'') as interfaz from convenios where codigo =?";
			pst = con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoConvenio);
			rs = pst.executeQuery();
			if(rs.next()){
				return (rs.getString("interfaz") != null)?rs.getString("interfaz"):"";
			}
						
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCodigoInterfazConvenioDeCodigo",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigoInterfazConvenioDeCodigo", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return "";
	}

	/**
	 * Mï¿½todo que devuelve la fecha del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String obtenerFechaIngreso(Connection con, String idIngreso)
	{
		String cadenaStr = "SELECT to_char(fecha_ingreso, 'DD/MM/YYYY') AS fecha FROM ingresos WHERE id = ?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(idIngreso));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("fecha");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Mï¿½todo que devuelve la hora del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String obtenerHoraIngreso(Connection con, String idIngreso)
	{
		String cadenaStr = "SELECT substr(hora_ingreso, 1,5) AS hora FROM ingresos WHERE id = ?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(idIngreso));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("hora");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	/**
	 * Mï¿½todo que devuelve el consecutivo de ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String obtenerConsecutivoIngreso(Connection con, String idIngreso)
	{
		String cadenaStr = "SELECT consecutivo AS consecutivo FROM ingresos WHERE id = ?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(idIngreso));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("consecutivo");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Mï¿½todo que devuelve el nombre del tipo
	 * de solicitud segï¿½n un tipo de solicitud
	 * dado
	 * @param con
	 * @param tipoSolicitud
	 * @return
	 */
	public static String obtenerNombreTipoSolicitud(Connection con, String tipoSolicitud)
	{
		String cadenaStr = "SELECT nombre AS nombre FROM tipos_solicitud WHERE codigo = ?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(tipoSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("nombre");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}

	
	/**
	 * Consultar el Centro de Costo Interfaz a partir del Codigo del Centro de Costo. 
	 * Tener en cuenta para los centros de costo SubAlmacen enviar el codigoInterfaz del Centro de Costo Interfaz
	 * Octubre 20 de 2008
	 * @author Andres Silva M
	 * @param codigoCentroCosto
	 * @return
	 */
	public static String obtenerCentroCostoInterfaz(Connection con, int codigoCentroCosto) 
	{
		String consultaEsSubAlmacen = "SELECT tipo_area from centros_costo where codigo=?";
		String consultaCentroCostoInterfazdeCCPrincipal="SELECT cp.codigo_interfaz as interfaz from centros_costo cp where cp.codigo= (SELECT ap.centro_costo_principal from almacen_parametros ap where ap.codigo=?)";
		String consultaCodigoInterfazdeCentroCosto="SELECT codigo_interfaz as interfaz from centros_costo where codigo=?";
		
		
		int esSubAlmacen=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaEsSubAlmacen, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCentroCosto);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
					esSubAlmacen=rs.getInt("tipo_area");
			else
				esSubAlmacen=ConstantesBD.codigoNuncaValido;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		if(esSubAlmacen == ConstantesBD.codigoTipoAreaSubalmacen)
		{
			logger.info("--------EL CENTRO DE COSTO ES SUBALMACEN SE ENVIA EL CC: PRINCIPAL----");
			try
			{
				PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(consultaCentroCostoInterfazdeCCPrincipal, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setInt(1, codigoCentroCosto);
				ResultSetDecorator rs1 = new ResultSetDecorator(ps1.executeQuery());
				if(rs1.next())
					return rs1.getString("interfaz");	
							
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return "";
		}
		else
		{
			try
			{
				PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(consultaCodigoInterfazdeCentroCosto, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps2.setInt(1, codigoCentroCosto);
				ResultSetDecorator rs2 = new ResultSetDecorator(ps2.executeQuery());
				if(rs2.next())
					return rs2.getString("interfaz");	
							
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return "";
		}
	}

	/**
	 * Consulta el NIT del Convenio por medio del codigo interfaz del mismo
	 * @author Andres Silva M --Oct 21 / 08
	 * @param con
	 * @param codigoInterfazConvenio
	 * @return
	 */
	public static String obtenerNitConveniodeCodInterfaz(Connection con, String codigoInterfazConvenio) 
	{
		String cadenaStr = "SELECT ter.numero_identificacion as nit from convenios con INNER JOIN empresas emp ON(emp.codigo=con.empresa) INNER JOIN terceros ter ON(ter.codigo=emp.tercero ) where con.interfaz=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoInterfazConvenio);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("nit");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Mï¿½todo encargado de traer los tipos de regimen
	 * segï¿½n el filtro
	 * @author Carlos Mauricio Jaramillo H.
	 * @param con
	 * @param todos
	 * @param codigoFiltros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposRegimen(Connection con, boolean todos, String codigoFiltros)
	{
		ArrayList<HashMap<String, Object>> resultado= new ArrayList<HashMap<String,Object>>();
		String cadena = "SELECT acronimo, nombre, requiere_deudor, req_verific_derechos FROM tipos_regimen ";
		String order = "ORDER BY nombre ";
		
		if(!todos)
			if(UtilidadCadena.noEsVacio(codigoFiltros))
				cadena += "WHERE acronimo IN ("+codigoFiltros+") "; 
		
		cadena += order;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap dato = new HashMap ();
				dato.put("acronimo", rs.getObject("acronimo"));
				dato.put("nombre", rs.getObject("nombre"));
				dato.put("requiereDeudor", rs.getObject("requiere_deudor"));
				dato.put("reqVerificDerechos", rs.getObject("req_verific_derechos"));
				resultado.add(dato);
			}
		}
		catch (SQLException e) 
		{
			logger.info("\n PROBLEMA OBTENIENDO LOS TIPOS REGIMEN "+e);
		}
		return resultado;
	}
	
	/**
	 * Consulta el NIT del convenio por medio del codigo de la factura
	 * @author Andres Silva M --Oct 24 /08
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerNitConveniodeCodfactura(Connection con, int codigoFactura) 
	{
		logger.info("CODIGO FACTURA -->"+codigoFactura+"<-");
		String cadenaStr = "SELECT ter.numero_identificacion as nit from facturas fa INNER JOIN convenios con ON(con.codigo=fa.convenio) INNER JOIN empresas emp ON(emp.codigo=con.empresa) INNER JOIN terceros ter ON(ter.codigo=emp.tercero ) where fa.codigo=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("nit");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Consulta el codigo Interfaz del tipo de Identificacion
	 * @author Andres Silva Monsalve --Oct 25/08
	 * @param codigoTipoIdentificacionPersona
	 * @return
	 */	
	public static String codigoInterfaztipoIdentificacion(Connection con, String codigoTipoIdentificacionPersona) 
	{
		String cadenaStr = "SELECT cod_interfaz from tipos_identificacion where acronimo=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoTipoIdentificacionPersona);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("CODIGO INTERFAZ TIPO ID ->"+rs.getString("cod_interfaz")+"<-");
				return rs.getString("cod_interfaz");
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Consulta para saber si el paciente tiene informacion en la historia del sistema anterior
	 * @author Andres Silva M
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public static boolean existeHistoriaSistemaAnterior(Connection con, String tipoIdentificacion, String numeroIdentificacion) 
	{
		String cadenaConsulta = "SELECT pac.histo_sistema_anterior as historia from pacientes pac INNER JOIN personas per ON(per.codigo=pac.codigo_paciente) where per.numero_identificacion=? and per.tipo_identificacion=?";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroIdentificacion);
			ps.setString(2, tipoIdentificacion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getBoolean("historia");
			
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DE PROCEDIMIENTOS INCLUIDOS "+e);
		}
		return false;
	}
	
	/**
	 * * Consulta codigo Interfaz del centro de costo subalmacen en Parametros Almacen
	 * @author Andres Silva M Oct28/08
	 * @param con
	 * @param codigoAlmacen
	 * @return
	 */
	public static String obtenerCodigoInterfazParametroAlmacen(Connection con, int codigoAlmacen) 
	{
		String cadenaStr = "SELECT codigo_interfaz from almacen_parametros where codigo=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoAlmacen);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("\nCODIGO INTERFAZ ALMACEN PARAMETROS ->"+rs.getString("codigo_interfaz")+"<-");
				return rs.getString("codigo_interfaz");
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * Consulta el Codigo Interfaz para un Articulo
	 * @author Andres Silva M Oct30/08
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazArticulo(Connection con, int codigoArticulo) 
	{
		logger.info("--- Codigo Articulo->"+codigoArticulo+"<-");
		String cadenaStr = "SELECT codigo_interfaz from articulo where codigo=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("\nCODIGO INTERFAZ ARTICULO ->"+rs.getString("codigo_interfaz")+"<-");
				return rs.getString("codigo_interfaz");
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Consulta el Codigo del Articulo a partir del codigo interfaz del mismo
	 * @author Andres Silva M Oct30/08
	 * @param codigoInterfazArticulo
	 * @return
	 */
	public static int obtenerCodigoArticulodeCodInterfaz(Connection con, String codigoInterfazArticulo) 
	{
		logger.info("<<<<<< CODIGO INTERFAZ ARTICULO -->"+codigoInterfazArticulo.trim()+"<-");
		
		String cadenaStr= "SELECT codigo from articulo where codigo_interfaz=?";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoInterfazArticulo.trim());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				{	
					logger.info("\n\n >>>>>>> CODIGO ARTICULO ->"+rs.getInt("codigo")+"<-\n");
					return rs.getInt("codigo");	
				}
						
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DEL ARTICULO "+e);
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consulta el codigo del centro de costo asociado al interfaz del almacen en la tabla parametros almacen
	 * @author Andres Silva Monsalve Oct30/08
	 * @param codigoInterfazAlmacen
	 * @return
	 */
	public static int obtenerAlmacenDeCodigoInterfaz(Connection con, String codigoInterfazAlmacen) 
	{
		logger.info("<<<<<< CODIGO INTERFAZ ALMACEN -->"+codigoInterfazAlmacen.trim()+"<-");
		
		String cadenaStr= "SELECT codigo from almacen_parametros where codigo_interfaz=?";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoInterfazAlmacen.trim());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				{	
					logger.info("\n\n >>>>>>> CODIGO ALMACEN ->"+rs.getInt("codigo")+"<-\n");
					return rs.getInt("codigo");	
				}
						
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CODIGO DEL ALMACEN "+e);
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consultar Consecutivo de la Transaccion Inventarios a Partir del Codigo
	 * @author Andres Silva M Oct30/08
	 * @param codigoTrans
	 * @return
	 */
	public static int obtenerConsecutivoDeTransaccionInv(Connection con, String codigoTrans) 
	{	
		logger.info("<<<<<< ACRONIMO DE TRANSACCION -->"+codigoTrans.trim()+"<-");
	
		String cadenaStr= "SELECT consecutivo from tipos_trans_inventarios where codigo=?";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoTrans.trim());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				{	
					logger.info("\n\n >>>>>>> CONSECUTIVO TRANSACCION ->"+rs.getInt("consecutivo")+"<-\n");
					return rs.getInt("consecutivo");	
				}
						
		}
		catch (SQLException e) 
		{
			logger.info("ERROR CONSULTANDO EL CONSECUTIVO DE LA TRANSACCION "+e);
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Mï¿½todo encargado de identificar si un ingreso tiene facturas
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static int esIngresoFacturado(Connection connection, String ingreso)
	{
		String cadena =" SELECT COUNT(*) FROM facturas f INNER JOIN cuentas c ON (c.id=f.cuenta) WHERE c.id_ingreso="+ingreso+" AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada;
		
		try {
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena));
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e) 
		{
			logger.info("\n problema consultan si un ingreso ya tiene facturas "+e);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consulta el Codigo del Tercero A partir del Numero de Identificacion del Mismo
	 * @author Andres Silva Monsalve Oct31/08
	 * @param numeroIdTercero
	 * @return
	 */
	public static int obtenercodigoTercerodeNumeroIdentificacion(Connection con, String numeroIdTercero) 
	{
		logger.info("NUMERO ID TERCERO ->"+numeroIdTercero+"<-");
		String cadenaStr = "SELECT codigo from terceros where numero_identificacion =? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroIdTercero.trim());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("\n\n>>>>>> CODIGO TERCERO -->"+rs.getInt("codigo")+"<-\n");
				return rs.getInt("codigo");
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Pregunta Si el login del usuario Existe en Axioma
	 * @author Andres Silva M Nov06/08
	 * @param loginUsuario
	 * @return
	 */
	public static boolean existeUsuario(Connection con, String loginUsuario) 
	{
		
		String cadenaStr = "SELECT codigo_persona from usuarios where login=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, loginUsuario.trim());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("\n\n>>>>>> CODIGO PERSONA LOGIN -->"+rs.getInt("codigo_persona")+"<-\n");
				return true;
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param con
	 * @param consecutivo
	 * @param todos
	 * @return
	 */
	public static HashMap consultarTransaccionesInventarios(Connection con, String consecutivo, boolean todos, boolean validarCodigos, String codigosValidados) 
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		String cadena = "SELECT " +
							"tti.consecutivo AS consecutivo, " +
							"tti.tipos_conceptos_inv AS tiposconceptos,  " +
							"tti.codigo AS codigo, " +
							"tti.descripcion AS descripcion, " +
							"tti.activo AS activo, " +
							"tti.codigo_interfaz AS codigointerfaz " +
						"FROM " +
							"tipos_trans_inventarios tti " +
						"WHERE " +
							"tti.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		
		if(!todos)
			cadena += "AND tti.consecutivo = "+consecutivo+" ";
		
		if(validarCodigos)
			if(UtilidadCadena.noEsVacio(codigosValidados))
				cadena += "AND tti.consecutivo NOT IN ("+codigosValidados+") ";
		
		cadena += "ORDER BY descripcion ";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===>Cadena Consulta Transacciones Inventarios: "+cadena);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTransaccionesInventarios de SqlBaseUtilidadesDao: "+e);
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo encargado de obtener el tipo de sala standar
	 * que esta en el grupo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerTipoSalaStandar(Connection con, String codigoServicio ) 
	{
		logger.info("\n obtenerTipoSalaStandar -->"+codigoServicio);
		String cadenaStr = "SELECT gs.tipo_sala_standar As codigo from servicios s INNER JOIN grupos_servicios gs  ON (gs.codigo=s.grupo_servicio) where s.codigo="+codigoServicio;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigo");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	

	/**
	 * Mï¿½todo para Consultar el Consecutivo del Ingreso de un paciente y el Codigo Persona a partir de un Numero de Pedido QX
	 * @param con
	 * @param numeroPedido
	 * @return
	 */
	public static HashMap consultarIngresoYpersonadeunPedidoQx(Connection con, int numeroPedido) 
	{
		String strCadenaConsultarFacturas = "SELECT i.consecutivo,c.codigo_paciente " +
										"from pedidos_peticiones_qx ppqx inner join solicitudes_cirugia sc on(sc.codigo_peticion=ppqx.peticion) " +
										"inner join solicitudes s on (s.numero_solicitud=sc.numero_solicitud) " +
										"inner join cuentas c on(c.id=s.cuenta) " +
										"inner join ingresos i on(i.id=c.id_ingreso) where ppqx.pedido=?";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultarFacturas, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroPedido);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo encargado de consultar el nombre de la 
	 * salida del paciente.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreSalidaPaciente(Connection con, String codigo ) 
	{
		logger.info("\n obtenerNombreSalidaPaciente -->"+codigo);
		String cadenaStr = "SELECT  sp.descripcion AS nombre from salida_paciente sp where codigo="+codigo;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("nombre");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	

	/**
	 * Mï¿½todo encargado de consultar el codigo de un servicio 
	 * dependiendo del tarifario y del codigo.
	 * @param con
	 * @param codigo
	 * @param ta
	 * @return
	 */
	public static int obtenerCodigoServicio(Connection con, String codigo,String tarifario ) 
	{
		logger.info("\n obtenerCodigoServicio codigo -->"+codigo+"  tarifario-->"+tarifario);
		String cadenaStr = "SELECT  servicio from referencias_servicio where tipo_tarifario="+tarifario+" AND codigo_propietario='"+codigo+"'";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("servicio");
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema en obtenerCodigoServicio "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Mï¿½todo que devuelve la sumatoria del valor total cargado
	 * de los N paquetes que tiene uns subcuenta
	 * @param con
	 * @param subCuenta
	 */
	public static double obtenerValorTotalPaquetesResponsable(Connection con, String subCuenta) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		double resultado=ConstantesBD.codigoNuncaValidoDouble;
		
		try	{
			Log4JManager.info("############## Inicio obtenerValorTotalPaquetesResponsable");
			String cadenaStr = "SELECT " +
				"coalesce(sum(valor_total_cargado),0) AS valorTotal " +
			"FROM " +
				"det_cargos " +
			"WHERE " +
				"sub_cuenta = "+Utilidades.convertirALong(subCuenta)+" " +
				"AND estado = "+ConstantesBD.codigoEstadoFCargada+" " +
				"AND tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudPaquetes+" ";
			pst =  con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			if(rs.next())
				resultado= rs.getDouble("valorTotal");
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerValorTotalPaquetesResponsable");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoTercero
	 * @return
	 */
	public static String obtenerNitProveedor(Connection con, String codigoTercero) 
	{
		String cadenaStr = "SELECT numero_identificacion as nit from terceros where codigo=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoTercero);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("nit");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Verificar si Abono generado por otro sistema existe en Axioma
	 * @param con
	 * @param reciboCaja
	 * @param tipoMovimiento
	 * @return
	 */
	public static boolean existeRegistroAbonosRC(Connection con, String reciboCaja, String tipoMovimiento) 
	{
		String cadenaStr = "SELECT * from movimientos_abonos where  codigo_documento =? and tipo=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(reciboCaja));
			ps.setInt(2, Utilidades.convertirAEntero(tipoMovimiento));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
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
	 * @param codigoConvenio
	 * @return
	 */
	public static String asignarValorPacienteValorAbonos(Connection con, String codigoConvenio)
	{
		String cadenaStr = "SELECT asignarfac_valorpac_valorabo AS asignarConvenio FROM convenios WHERE codigo = "+Utilidades.convertirAEntero(codigoConvenio)+" ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("asignarConvenio");	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Mï¿½todo que consulta todos los Rublos Presupuestales
	 * @param con
	 * @param anioVigenciaRublo
	 * @param codigoRublo
	 * @param descripcionRublo
	 * @param institucion 
	 * @return
	 */
	public static HashMap obtenerRublosPresupuestales(Connection con, String anioVigenciaRublo, String codigoRublo, String descripcionRublo, int institucion) 
	{
		String strCadenaConsultarRublos = "SELECT codigo,codigo_rubro, upper(descripcion) as descripcion, anio_vigencia from rubro_presupuestal " +
											"where anio_vigencia='"+anioVigenciaRublo+"' " +
											"and activo='"+ConstantesBD.acronimoSi+"' " +
											"and movimiento='"+ConstantesBD.acronimoSi+"' " +
											"and institucion="+institucion+" ";
		if(!codigoRublo.equals(""))
			strCadenaConsultarRublos+=" and codigo_rubro='"+codigoRublo+"'";
		
		if(!descripcionRublo.equals(""))
			strCadenaConsultarRublos+=" and upper(descripcion) like upper('%"+descripcionRublo+"%')";
		
		strCadenaConsultarRublos+=" order by descripcion";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;
		try 
		{
		logger.info("\n Cadena-->"+strCadenaConsultarRublos+" con abierta->"+!con.isClosed());
		ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultarRublos, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
		e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo encargado de Obtener el codigo de un centro de atenciï¿½n dado su nombre
	 * @author Felipe Pï¿½rez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public static int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion)
	{
		logger.info("===> Entrï¿½ a obtenerCodigoCentroAtencion, nombre centro de atenciï¿½n = "+nombreCentroAtencion);
		String consulta = "SELECT consecutivo FROM centro_atencion WHERE descripcion = '"+nombreCentroAtencion+"'";
		logger.info("===> La consulta es : "+consulta);

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getInt(1);
			
		}
		catch (SQLException e) 
		{
			logger.error("===> Se produjeron errores al consultar "+e);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Encargado de devolver las solicitudes de procedimientos, cirugias, interconsultas, cargos directos y asociados a una factra
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap obtenerSolicitudesFacturas(Connection con, int codFactura)
	{
		HashMap solicitudesFacturaMap = new HashMap();
		PreparedStatementDecorator ps; 
		
		String consulta = "(SELECT " +
								"dfs.tipo_solicitud AS tiposolicitud, " +
								"dfs.solicitud AS solicitud," +
								"dfs.articulo AS codigo, " +
								"'"+ConstantesBD.acronimoNo+"' AS esservicio, " +
								"CASE WHEN jas.numero_solicitud IS null THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS tienejustificacion " +
							"FROM " +
								"det_factura_solicitud dfs " +
							"LEFT OUTER JOIN " +
								"justificacion_art_sol jas ON (jas.numero_solicitud=dfs.solicitud AND jas.articulo=dfs.articulo) " +
							"WHERE " +
								"dfs.factura="+codFactura+" AND dfs.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") " +
						") UNION ( " +
							"SELECT " +
								"dfs.tipo_solicitud AS tiposolicitud, " +
								"dfs.solicitud AS solicitud," +
								"dfs.servicio AS codigo, " +
								"'"+ConstantesBD.acronimoSi+"' AS esservicio, " +
								"CASE WHEN jss.solicitud IS null THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS tienejustificacion " +
							"FROM " +
								"det_factura_solicitud dfs " +
							"LEFT OUTER JOIN " +
								"justificacion_serv_sol jss ON (jss.solicitud=dfs.solicitud AND jss.servicio=dfs.servicio) " +
							"WHERE " +
								"dfs.factura="+codFactura+" AND dfs.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudProcedimiento+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+", "+ConstantesBD.codigoTipoSolicitudInterconsulta+", "+ConstantesBD.codigoTipoSolicitudCirugia+") " +
						") ";
						//+"ORDER BY " +
						//		"dfs.solicitud ";
		logger.info("obtenerSolicitudesFacturas / "+consulta);
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			solicitudesFacturaMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.info("ERROR / obtenerSolicitudesFacturas / "+e);
		}					
		
		// Obtener los numeros de solicitud asociados a la factura concatenados por comas
		String aux="", solicitudes="";
		for(int i=0; i<Utilidades.convertirAEntero(solicitudesFacturaMap.get("numRegistros").toString()); i++){
			if(!aux.equals(solicitudesFacturaMap.get("solicitud_"+i))){
				solicitudes += solicitudesFacturaMap.get("solicitud_"+i)+",";
				aux = solicitudesFacturaMap.get("solicitud_"+i).toString();
			}
		}
		solicitudes += ConstantesBD.codigoNuncaValido;
		
		solicitudesFacturaMap.put("solicitudes", solicitudes);
		return solicitudesFacturaMap;
	}



	/**
	 * 
	 * @param con
	 * @param consecutivos
	 * @param matrizLaboratorios
	 * @return
	 */
	public static boolean insertarRegistrosInterfazWINLAB(Connection con,ArrayList consecutivos, HashMap matrizLaboratorios) 
	{
		try 
		{
			String consulta="insert into interfaz_winlab (" +
															" nombre,apellidos,fecha,sexo,tipopaciente," +
															" claveservicio,cama,observaciones,direccion,ciudad," +
															" telefono,convenio,edad,fechatoma,horatoma," +
															" tipoedad,identificacion,ingreso,registromedico,consecutivo) " +
														" VALUES (" +
															" ?,?,?,?,?," +
															" ?,?,?,?,?," +
															" ?,?,?,?,?," +
															" ?,?,?,?,?)";
			String consulta1="insert into det_interfaz_winlab(cod_interfaz_winlab,cups) values (?,?)";
			PreparedStatementDecorator ps;
			for(int i=0;i<consecutivos.size();i++)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
				ps.setString(1,matrizLaboratorios.get("nombre_"+consecutivos.get(i))+"");
				ps.setString(2,matrizLaboratorios.get("apellidos_"+consecutivos.get(i))+"");
				ps.setString(3,matrizLaboratorios.get("fecha_"+consecutivos.get(i))+"");
				ps.setString(4,matrizLaboratorios.get("sexo_"+consecutivos.get(i))+"");
				ps.setString(5,matrizLaboratorios.get("tipopaciente_"+consecutivos.get(i))+"");
				ps.setString(6,matrizLaboratorios.get("claveservicio_"+consecutivos.get(i))+"");
				ps.setString(7,matrizLaboratorios.get("cama_"+consecutivos.get(i))+"");
				ps.setString(8,matrizLaboratorios.get("observaciones_"+consecutivos.get(i))+"");
				ps.setString(9,matrizLaboratorios.get("direccion_"+consecutivos.get(i))+"");
				ps.setString(10,matrizLaboratorios.get("ciudad_"+consecutivos.get(i))+"");
				ps.setString(11,matrizLaboratorios.get("telefono_"+consecutivos.get(i))+"");
				ps.setString(12,matrizLaboratorios.get("convenio_"+consecutivos.get(i))+"");
				ps.setString(13,matrizLaboratorios.get("edad_"+consecutivos.get(i))+"");
				ps.setString(14,matrizLaboratorios.get("fechatoma_"+consecutivos.get(i))+"");
				ps.setString(15,matrizLaboratorios.get("horatoma_"+consecutivos.get(i))+"");
				ps.setString(16,matrizLaboratorios.get("tipoedad_"+consecutivos.get(i))+"");
				ps.setString(17,matrizLaboratorios.get("identificacion_"+consecutivos.get(i))+"");
				ps.setString(18,matrizLaboratorios.get("ingreso_"+consecutivos.get(i))+"");
				ps.setString(19,matrizLaboratorios.get("identificacionmed_"+consecutivos.get(i))+"");
				ps.setString(20,matrizLaboratorios.get("consecutivo_"+consecutivos.get(i))+"");
				ps.executeUpdate();
				int numExa=Utilidades.convertirAEntero(matrizLaboratorios.get("numExamenes_"+consecutivos.get(i))+"");
				for(int j=0;j<numExa;j++)
				{
					String aux=(matrizLaboratorios.get("examen_"+consecutivos.get(i)+"_"+j)+"");
					ps= new PreparedStatementDecorator(con.prepareStatement(consulta1));
					ps.setString(1, matrizLaboratorios.get("consecutivo_"+consecutivos.get(i))+"");
					ps.setString(2, aux+"");
					ps.executeUpdate();
				}
			}
		} 
		catch (SQLException e) 
		{
			logger.info("ERROR insertarRegistrosInterfazWINLAB "+e);
		}	
		return true;
	}


	/**
	 * 
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public static String obtenerCodigoPisoCama(Connection con, String codigoCama) 
	{
		String cadenaStr = "SELECT p.codigo_piso from pisos p inner join habitaciones h  on(h.piso=p.codigo) inner join camas1 c on(c.habitacion=h.codigo) where c.codigo="+codigoCama;
		try
		{
			logger.info("consulta piso cama -->"+cadenaStr);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}



	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarEncabezadoLaboratoriosPendientesWINLAB(Connection con) 
	{
		HashMap mapa= new HashMap();
		String cadena =	"SELECT coalesce(nombre,'') as nombre," +
								" coalesce(apellidos,'') as apellidos," +
								" coalesce(fecha,'') as fecha," +
								" coalesce(sexo,'') as sexo," +
								" coalesce(tipopaciente,'') as tipopaciente," +
								" coalesce(claveservicio,'') as claveservicio," +
								" coalesce(cama,'') as cama," +
								" coalesce(observaciones,'') as observaciones," +
								" coalesce(direccion,'') as direccion," +
								" coalesce(ciudad,'') as ciudad," +
								" coalesce(telefono,'') as telefono," +
								" coalesce(convenio,'') as convenio," +
								" coalesce(edad,'') as edad," +
								" coalesce(fechatoma,'') as fechatoma," +
								" coalesce(horatoma,'') as horatoma," +
								" coalesce(tipoedad,'') as tipoedad," +
								" coalesce(identificacion,'') as identificacion," +
								" coalesce(ingreso,'') as ingreso," +
								" coalesce(registromedico,'') as identificacionmed," +
								" coalesce(consecutivo,'')  as consecutivo" +
						" from interfaz_winlab";
		try 
		{
			logger.info(cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param consecutivo
	 * @return
	 */
	public static HashMap consultarDetalleLaboratoriosPendientesWINLAB(Connection con, String consecutivo) 
	{
		HashMap mapa= new HashMap();
		String cadena =	"SELECT cups as laboratorio from det_interfaz_winlab where cod_interfaz_winlab='"+consecutivo+"'";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param consecutivos
	 * @return
	 */
	public static boolean eliminarLaboratoriosPendientesWINLAB(Connection con,String consecutivos) 
	{
		String cadena =	"DELETE from interfaz_winlab where consecutivo IN ("+consecutivos+")";
		String cadena1 =	"DELETE from det_interfaz_winlab where cod_interfaz_winlab IN ("+consecutivos+")";
		PreparedStatementDecorator ps;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			
		}
		return false;
	}
	
	/**
	 * Mï¿½todo encargado de verificar si existe algun registro de la cuenta
	 * en la tabla egresos.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static boolean existeAlgunRegistroEgreso(Connection con, int cuenta)
	{
		logger.info("\n\n++++++++++++++++++existeAlgunRegistroEgreso  ---------- cuenta-->"+cuenta+"\n\n");
		String consulta="SELECT cuenta from egresos where cuenta=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cuenta);

			
			
			if (new ResultSetDecorator(ps.executeQuery()).next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema en existeAlgunRegistroEgreso "+e);
		}
		return false;
	}

	/**
	 * Mï¿½todo encargado para consultar el codigo de clase inventario de un articulo especifico
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static String consultarClaseInterfazArticulo(Connection con, String articulo) 
	{
		String cadenaStr = "SELECT coalesce(ci.codigo_interfaz,'') as codigo " +
			"from articulo a " +
			"INNER JOIN subgrupo_inventario sub ON(sub.codigo=a.subgrupo) " +
			"INNER JOIN clase_inventario ci ON(ci.codigo = sub.clase) " +
			"where a.codigo="+articulo.trim();
		try
		{
			logger.info("\n\n Consultar Articulo >>>>>>> "+cadenaStr);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				{
					logger.info("VALOR DE LA CLASE ---->"+rs.getString("codigo"));
					return rs.getString("codigo");
				}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifario
	 * @return
	 */
	public static String getTipoEntidadEjecutaCC(Connection con, String codigoCC) 
	{
		try
		{
			String consultaStr = 	"SELECT tipo_entidad_ejecuta AS tipoent FROM centros_costo WHERE codigo ="+codigoCC;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("tipoent");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en getTipoEntidadEjecutaCC de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifario
	 * @return
	 */
	public static String obtenerCCSolicitadoSolicitud(Connection con, String numeroSolicitud) 
	{
		try
		{
			String consultaStr = 	"SELECT centro_costo_solicitado AS ccsol FROM solicitudes WHERE numero_solicitud ="+numeroSolicitud;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("ccsol");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCCSolicitadoSolicitud de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo encargado de verificar si existe algun registro de la cuenta
	 * en la tabla egresos.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static boolean existeAutorizacionSolicitud(Connection con, String numeroSolicitud)
	{
		// PermitirAutorizarDiferenteDeSolicitudes
		logger.info("ESTOY BUSCANDO LA EXISTENCIA DE UNA AUTORIZACION DE ENT SUB PARA LA SOLICITUD");
		String consulta="SELECT consecutivo FROM autorizaciones_entidades_sub a " +
				"INNER JOIN ordenes.auto_entsub_solicitudes aas ON (aas.autorizacion_ent_sub = a.consecutivo) " +
				"WHERE aas.numero_solicitud=? ";  
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(numeroSolicitud));

			if (new ResultSetDecorator(ps.executeQuery()).next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema en existeAutorizacionSolicitud "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifario
	 * @return
	 */
	public static String obtenerTipoAutorizacionEntidadSubcontratada(Connection con, String numeroSolicitud) 
	{
		try
		{
			// PermitirAutorizarDiferenteDeSolicitudes
			String consultaStr = 	"SELECT tipo AS tipoauto FROM autorizaciones_entidades_sub a " +
					"INNER JOIN ordenes.auto_entsub_solicitudes aas ON (aas.autorizacion_ent_sub = a.consecutivo) " +
					"WHERE aas.numero_solicitud ="+numeroSolicitud; 
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("tipoauto");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoAutorizacionEntidadSubcontratada de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}
	
	/**
	 * Mï¿½todo para verificar si el usuario actual puede responder ordenes de acuerdo si ï¿½ste se encuentra registrado en la tabla usuarios_entidad_sub
	 * en la tabla egresos.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static String verificarregistroUsuarioEntidadSubcontratada(Connection con, String numeroSolicitud, String login)
	{
		logger.info("ESTOY BUSCANDO SI EL USUARIO ESTA REGISTRADO PARA ESTA AUTORIZACION");
		String consulta=	"SELECT  " +
								"au.entidad_autorizada_sub AS entidad " +
							"FROM " +
								"autorizaciones_entidades_sub au " +
							"INNER JOIN  " +
								"entidades_subcontratadas es ON (es.codigo_pk=au.entidad_autorizada_sub) " +
							"INNER JOIN " +
								"usuarios_entidad_sub ue ON (ue.entidad_subcontratada=es.codigo_pk) " +
								
							// PermitirAutorizarDiferenteDeSolicitudes	
							"INNER JOIN ordenes.auto_entsub_solicitudes aas ON (aas.autorizacion_ent_sub = au.consecutivo) " +
								
							"WHERE " +
								"aas.numero_solicitud=? " +  
							"AND " +
								"ue.usuario=? ";
		logger.info("consulta=> "+consulta+", numero solicitud: "+numeroSolicitud+", login: "+login);
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroSolicitud));
			pst.setString(2, login);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString("entidad");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoAutorizacionEntidadSubcontratada de SqlBaseUtilidadesDao : "+e);
			return "";
		}
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable
	 * @param descripcion
	 * @param anioVigencia 
	 * @return
	 */
	public static HashMap obtenerCuentaContableXCodigo(Connection con, int codigoInstitucion, String cuentaContable) 
	{
		HashMap mapa= new HashMap();
		String cadena=	"SELECT " +
							"codigo AS codigo, " +
							"anio_vigencia as anio_vigencia," +
							"cuenta_contable AS cuenta_contable " +
						"from " +
							"cuentas_contables " +
						"where " +
							"institucion=? " +
							"and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		
		if(!cuentaContable.isEmpty())
			cadena+=" and codigo='"+cuentaContable+"'";
	
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	public static ArrayList<ArrayList<Object>> listarProgramasPyP(Connection con, boolean activo, int codigoInstitucion) {
		try 
		{
			String cadena="SELECT codigo, descripcion FROM pyp.programas_salud_pyp where activo=? and institucion=?";
/*			logger.info(cadena);
			logger.info(activo);
			logger.info(codigoInstitucion);*/
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setBoolean(1, activo);
			ps.setInt(2, codigoInstitucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			ArrayList<ArrayList<Object>> lista=new ArrayList<ArrayList<Object>>();
			while(rs.next())
			{
				ArrayList<Object> programa=new ArrayList<Object>();
				programa.add(rs.getString("codigo")+"");
				programa.add(rs.getString("descripcion")+"");
				lista.add(programa);
			}
			rs.close();
			ps.close();
			return lista;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Mï¿½todo para verificar si un convenio tiene chekeado Reporte de Atencion de Urgencias
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public static boolean esConvenioConReportAtencionUrg(Connection con, String codConvenio) {
		
		
		String cadena = "SELECT reporte_atencion_ini_urg FROM convenios WHERE codigo = ? AND  reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' ";
		try{
            PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, Utilidades.convertirAEntero(codConvenio));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				return true;
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Mï¿½todo que consulta los dias de la semana
	 * @param con	
	 * @return ArrayList<HashMap>
	 * @author Vï¿½ctor Gï¿½mez 
	 */
	public static ArrayList<HashMap> obtenerDiasSemanaArray(Connection con)
	{
		ArrayList<HashMap> array = new ArrayList<HashMap>();
		String consulta = "";
		try
		{
			consulta = "SELECT codigo, dia FROM dias_semana ";
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo", rs.getInt("codigo"));
				mapa.put("dia", rs.getString("dia"));
				array.add(mapa);
			}
			logger.info("Consulta: "+consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDiasSemana: "+e);
			logger.info("Consulta: "+consulta);
		}
		return array;
	}

	
	/**
	 * Mï¿½todo que consulta los parentezcos
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarParentezcos(Connection con) {
		
		ArrayList<HashMap<String, Object>> parentezcos = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo_pk, descripcion FROM odontologia.parentezco order by descripcion";
			
			 PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));				    
			 ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getString("codigo_pk"));
				elemento.put("descripcion",rs.getString("descripcion"));
				parentezcos.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar parentezcos: "+e);
			
		}
		return parentezcos;
	}
	
	
	/**
	 * Mï¿½todo que consulta los motivos de una cita
	 * @param con
	 * @param tipoMotivo 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarMotivosCita(Connection con,int codInstitucion, String tipoMotivo) {
		
		ArrayList<HashMap<String, Object>> motivosCita = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo_pk, descripcion FROM  odontologia.motivos_cita WHERE institucion=? and tipo_motivo = ? order by descripcion";
			
			 PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			 ps.setInt(1, codInstitucion);		    
			 ps.setString(2, tipoMotivo);
			 ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getString("codigo_pk"));
				elemento.put("descripcion",rs.getString("descripcion"));
				motivosCita.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar Motivos Cita: "+e);
			
		}
		return motivosCita;
	}

	public static ArrayList<HashMap<String, Object>> consultarMediosConocimientoServ(Connection con, int codInstitucion) {
		ArrayList<HashMap<String, Object>> conocimientos = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo, descripcion FROM  manejopaciente.conocimiento_servicio WHERE institucion=? order by descripcion";
			
			 PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			 ps.setInt(1, codInstitucion);		    
			 ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getString("codigo"));
				elemento.put("descripcion",rs.getString("descripcion"));
				conocimientos.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar Motivos Cita: "+e);
			
		}
		return conocimientos;
	}

	/**
	 * Obtener los convenios del parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * 
	 * Keys del mapa
	 * 		- codigo
	 * 		- descripcion
	 * 
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConveniosAMostrarPresupuestoOdo(Connection con, int codInstitucion) {
		ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT " +
									"convenio AS codigoConvenio, " +
									"contrato AS codigoContrato, " +
									"getnumerocontrato(contrato) AS numeroContrato, " +
									"facturacion.getnombreconvenio(convenio) AS descripcionConvenio, " +
									"con.es_conv_tar_cli AS esconvtarjcliente," +
									"con.req_bono_ing_pac AS  requierebono, " +
									"con.req_ing_val_auto AS reqingvalauto, " +
									"con.ing_pac_req_aut AS ingpacreqauto, " +
									"con.activo AS activo, " +
									"con.ajuste_servicios AS ajusteServicio "+
								"FROM " +
									"administracion.convenios_presupuesto_odo cpo " +
									"INNER JOIN facturacion.convenios con ON(con.codigo = cpo.convenio) " +
								"WHERE " +
									"cpo.institucion="+codInstitucion+" " +
								"ORDER BY descripcionConvenio";
			
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	    
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoConvenio",rs.getString("codigoConvenio"));
				elemento.put("descripcionConvenio",rs.getString("descripcionConvenio"));
				elemento.put("codigoContrato",rs.getString("codigoContrato"));
				elemento.put("numeroContrato",rs.getString("numeroContrato"));
				elemento.put("esConvenioTarjCliente", rs.getString("esconvtarjcliente"));
				elemento.put("requiereBono", rs.getString("requierebono"));
				elemento.put("requiereIngValAuto", rs.getString("reqingvalauto"));
				elemento.put("ingresoPacienteReqAuto", rs.getString("ingpacreqauto"));
				elemento.put("eliminado",ConstantesBD.acronimoNo);
				elemento.put("nuevo",ConstantesBD.acronimoNo);
				elemento.put("activo", rs.getBoolean("activo"));
				elemento.put("ajusteServicio", rs.getString("ajusteServicio"));
				convenios.add(elemento);
			}
			
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERROR", e);
		}
		return convenios;
	}

	public static Object actualizarConveniosAMostrarPresupuestoOdo(Connection con,ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo, String usuario, int codigoInstitucion) {
		
		String sql="";
		
		for (int i=0; i<conveniosAMostrarPresupuestoOdo.size(); i++) {
			
			// Ingresar nuevo registro
			if(conveniosAMostrarPresupuestoOdo.get(i).get("nuevo").toString().equals(ConstantesBD.acronimoSi)){
				try
				{
					sql="INSERT INTO administracion.convenios_presupuesto_odo (institucion, convenio, usuario_modifica, fecha_modifica, hora_modifica, contrato) VALUES ("+codigoInstitucion+", "+conveniosAMostrarPresupuestoOdo.get(i).get("codigoConvenio")+", '"+usuario+"', current_date, '"+UtilidadFecha.getHoraActual()+"', "+conveniosAMostrarPresupuestoOdo.get(i).get("codigoContrato")+")";
					logger.info("sql > "+sql);
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					logger.error("ERROR", e);
				}
			}
			
			// Eliminar registro
			if(conveniosAMostrarPresupuestoOdo.get(i).get("eliminado").toString().equals(ConstantesBD.acronimoSi)){
				try
				{
					sql="DELETE FROM administracion.convenios_presupuesto_odo WHERE convenio="+conveniosAMostrarPresupuestoOdo.get(i).get("codigoConvenio")+" AND contrato="+conveniosAMostrarPresupuestoOdo.get(i).get("codigoContrato");
					logger.info("sql > "+sql);
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					logger.error("ERROR", e);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Obtener informacion sobre la cuenta solicitante de una orden ambulatoria
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public static HashMap obtenerInfoCuentaSolicitanteOrdenAmbulatoria(Connection con, int codigoOrden) {
		HashMap mapa= new HashMap();
		String cadena=	"SELECT " +
							"getnombreconvenio(sc.convenio) As convenio, " +
							"getnombretipoafiliado(sc.tipo_afiliado) AS tipoAfiliado, " +
							"getnombreestrato(sc.clasificacion_socioeconomica) AS clasificacionSocioeconomica " +
						"FROM " +
							"ordenes_ambulatorias oa " +
						"INNER JOIN " +
							"cuentas c ON (oa.cuenta_solicitante = c.id) " +
						"INNER JOIN " +
							"sub_cuentas sc ON (sc.ingreso=c.id_ingreso) " +
						"WHERE " +
							"oa.codigo='"+codigoOrden+"' AND sc.nro_prioridad=1";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+cadena);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			Utilidades.imprimirMapa(mapa);
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR", e);
		}
		return mapa;
	}

public static ArrayList<DtoIntegridadDominio> generarListadoConstantesIntegridadDominio(Connection con, String[] listaConstantes, boolean ordenar) {
		int tamanio=listaConstantes.length;
		if(tamanio<1)
		{
			logger.error("No se recibiï¿½ ningï¿½n elemento de integridad dominio");
			return null;
		}
		String sentencia="SELECT acronimo AS acronimo, descripcion AS descripcion FROM administracion.integridad_dominio WHERE acronimo IN(";
		for(int i=0; i<tamanio; i++)
		{
			if(i>0)
			{
				sentencia+=",";
			}
			sentencia+="?";
		}
		
		if(ordenar){
			
			sentencia+=") ORDER BY descripcion asc";
		
		}else{
			
			sentencia+=")";
		}
		
		try
		{
			ArrayList<DtoIntegridadDominio> lista=new ArrayList<DtoIntegridadDominio>();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			for(int i=0; i<tamanio; i++)
			{
					psd.setString(i+1, listaConstantes[i]);
			}
			logger.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			while(rsd.next())
			{
				DtoIntegridadDominio dtoIntegridadDominio=new DtoIntegridadDominio();
				dtoIntegridadDominio.setAcronimo(rsd.getString("acronimo"));
				dtoIntegridadDominio.setDescripcion(rsd.getString("descripcion"));
				lista.add(dtoIntegridadDominio);
			}
			rsd.close();
			psd.close();
			
			return lista;
		} catch (SQLException e) {
			logger.info("Error buscando datos constantes integridad dominio", e);
		}
		
		return null;
	}

	/**
	 * obtener motivos de atencion odontologica
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMotivosAtencionOdontologica(Connection con, int tipo) {
		ArrayList<HashMap<String, Object>> motivosAtencion = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta="SELECT " +
								"codigo_pk as codigo_pk, " +
								"nombre as nombre " +
							"FROM " +
								"odontologia.motivos_atencion "+
							"WHERE 1=1 ";
			
			if(tipo!=ConstantesBD.codigoNuncaValido)
				consulta += "AND tipo="+tipo;
			
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	    
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoPk",rs.getString("codigo_pk"));
				elemento.put("nombre",rs.getString("nombre"));
				motivosAtencion.add(elemento);
			}
			
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERROR", e);
		}
		return motivosAtencion;
	}
	
	
	/***
	 * 
	 * 
	 * 
	 */
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarProcAutoEstados(DtoLogProcAutoEstados dto) 
	{
		
		
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_log_pro_aut_est");
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionProcAutoEst,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			
			ps.setDouble(1, secuencia);				
		    ps.setString(2, dto.getInconsistencia());		    
		    ps.setString(3, dto.getFecha());
		    ps.setString(4, dto.getHora());
		    		
			ps.setInt(5, dto.getPresupuesto());
			ps.setInt(6, dto.getInstitucion());
			ps.setInt(7, dto.getPlanTratamiento());
			
			
 
			
			
			
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getCodigoPk();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert proc aut est " + e + dto.getCodigoPk());
			
			
		}
		return dto.getCodigoPk();
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerListadoCoberturasXTipo(Connection con, int institucion, String tipoCobertura)
	{
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT codigo_cobertura,descrip_cobertura from cobertura where institucion = ? and activo='"+ConstantesBD.acronimoSi+"' and tipo_cobertura=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2, tipoCobertura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 */
	public static ArrayList<DtoConceptosIngTesoreria> obtenerConceptosIngresoTesoreria(int tipoIngreso,String valorFiltro)
	{
		ArrayList<DtoConceptosIngTesoreria> listado=new ArrayList<DtoConceptosIngTesoreria>();
		
		String consulta	=	"SELECT codigo,descripcion FROM tesoreria.conceptos_ing_tesoreria WHERE codigo_tipo_ingreso=? AND valor=?";
	
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipoIngreso);
			ps.setString(2, valorFiltro);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoConceptosIngTesoreria dto=new DtoConceptosIngTesoreria();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				listado.add(dto);
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return listado;
	}
	
	/**
	 * Utilidad que me permite saber en que estado se encuentra el indicativo itnerfaz  para una persona
	 */
	public static String obtenerIndicativoInterfazPersona(Connection con,String nroId)
	{
		String indInterfaz="";
		try
		{
			String consulta=" SELECT indicativo_interfaz FROM administracion.personas where numero_identificacion=?";
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setString(1, nroId);
			

			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())		
				indInterfaz=rs.getString("indicativo_interfaz");;
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs,null);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return indInterfaz;
	}
	
	/**
	 * Utilidad para saber con el codigo de la persona ell indicativo de interfaz del medico
	 */
	public static  String obtenerIndicativoInterfazMedico(Connection con, int codigoPersona)
	{
		String indInterfaz="";
		try
		{
			String consulta=" SELECT indicativo_interfaz FROM administracion.medicos where codigo_medico=?";
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setInt(1, codigoPersona);
			

			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())		
				indInterfaz=rs.getString("indicativo_interfaz");;
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs,null);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return indInterfaz;
	}
	
	/**
	 * 
	 */
	public static HashMap obtenerDatosTercero(Connection con, int codigoTercero)
	{
		HashMap mapaRetorno=new HashMap();
		try
		{	
			String consulta = "SELECT direccion,telefono,descripcion FROM facturacion.terceros WHERE codigo=?";
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setInt(1, codigoTercero);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
		 
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapaRetorno;
		 
		
	}
	
	

	public static boolean esOrdenAmbulatoriaPYP(Connection con, String orden,int institucion) 
	{
		boolean esPYP=false;
		try
		{
			String consulta="SELECT pyp from ordenes_ambulatorias where consecutivo_orden=? and institucion=?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setString(1,orden);
			pst.setInt(2,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				esPYP = rs.getBoolean("pyp");
			}

		}
		catch(SQLException e)
		{
			logger.error("Error en esOrdenAmbulatoriaPYP: "+e);
		}
		return esPYP;
	}

	/**
	 * 
	 * @param parametros
	 * @return
	 */
	public static boolean arreglarConsecutivos(HashMap<String, Object> parametros) {
		boolean exito = true;
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		// Se consultan todos los consecutivos que se encuentran repetidos
		String sql = "SELECT * FROM (SELECT "+parametros.get("columna")+" AS consecutivo, COUNT(*) AS total FROM "+parametros.get("tabla")+" GROUP BY "+parametros.get("columna")+") WHERE total > 1 ";
		
		try
		{
			PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			ResultSetDecorator rs1 = new ResultSetDecorator(pst1.executeQuery());
			
			while(rs1.next()) {
				
				// Se consultan los id(s) del consecutivo repetido
				sql = "SELECT id FROM ingresos WHERE consecutivo='"+rs1.getString("consecutivo")+"' ";
				PreparedStatementDecorator pst2 = new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
				ResultSetDecorator rs2 = new ResultSetDecorator(pst2.executeQuery());
				
				for(int i=1; i<Utilidades.convertirAEntero(rs1.getString("total")); i++){
					// Se obtiene el consecutivo a utilizar
					String consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(parametros.get("nombreConsecutivo")+"", Utilidades.convertirAEntero(parametros.get("institucion")+""));
					rs2.next();
					sql = "UPDATE ingresos SET consecutivo='"+consecutivo+"' WHERE id="+rs2.getInt("id");
					PreparedStatementDecorator pst3 = new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
					if(pst3.executeUpdate()>0){
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, parametros.get("nombreConsecutivo")+"", Utilidades.convertirAEntero(parametros.get("institucion")+""), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
						logger.info("Creado el consecutivo "+consecutivo);
					} else {
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, parametros.get("nombreConsecutivo")+"", Utilidades.convertirAEntero(parametros.get("institucion")+""), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						exito=false;
					}
					pst3.close();
				}
				
				if(rs1.getString("consecutivo").equals(ConstantesBD.codigoNuncaValido+"")){
					// Se obtiene el consecutivo a utilizar
					String consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(parametros.get("nombreConsecutivo")+"", Utilidades.convertirAEntero(parametros.get("institucion")+""));
					rs2.next();
					sql = "UPDATE ingresos SET consecutivo='"+consecutivo+"' WHERE id="+rs2.getInt("id");
					PreparedStatementDecorator pst3 = new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
					if(pst3.executeUpdate()>0){
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, parametros.get("nombreConsecutivo")+"", Utilidades.convertirAEntero(parametros.get("institucion")+""), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
						logger.info("Creado el consecutivo "+consecutivo);
					} else {
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, parametros.get("nombreConsecutivo")+"", Utilidades.convertirAEntero(parametros.get("institucion")+""), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						exito=false;
					}
					pst3.close();
				}
				
				
				pst2.close();
				rs2.close();
				
			}
			
			pst1.close();
			rs1.close();
		}
		catch(SQLException e)
		{
			logger.error("ERROR",e);
			exito=false;
		}
		
		if(exito)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		UtilidadBD.closeConnection(con);
		return exito;
	}

	
	/**
	 * 
	 * @param loginUsuario
	 * @return
	 */
	public static String obtenerEmailUsuario(String loginUsuario) 
	{
		String email="";
		try
		{
			String consulta="select email as email from personas p inner join usuarios u on(p.codigo=u.codigo_persona) where u.login=?";
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setString(1, loginUsuario);
			

			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())		
				email=rs.getString("email");;
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs,null);
			con.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return email;
	}

	/**
	 * 
	 * @param codigoPersona
	 * @param tipoMovimiento
	 * @param codigoCita
	 * @return
	 */
	public static double obtenerAbonoPacienteTipoYNumeroDocumento(int codigoPersona, int tipoMovimiento, int codigoCita) 
	{
		double valor=0;
		try
		{
			String consulta=" SELECT coalesce(sum(valor),0) from movimientos_abonos where tipo = ? and paciente=? and codigo_documento=?;";
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setInt(1,tipoMovimiento );
			ps.setInt(2,codigoPersona );
			ps.setInt(3,codigoCita );
			

			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())		
				valor=rs.getDouble(1);
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs,null);
			con.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return valor;
	}

	public static HashMap obtenerInformacionUsuarioCapitado(Connection con,
			String tipoID, String numeroID) {
		String consultaStr="SELECT primer_nombre,segundo_nombre,primer_apellido,segundo_apellido " +
		   " 	FROM usuarios_capitados" +
		   "		WHERE numero_identificacion= ?"+
		   "			AND tipo_identificacion= ?";

		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, numeroID);
			pst.setString(2, tipoID);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosPersona de SqlBaseUtilidadesDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * 
	 * Método que se encarga de consultar el filtro asociado al concepto de ingreso
	 * de tesoreria.
	 * 
	 * @param con
	 * @param concepto
	 * @return
	 */
	public  static String obtenerFiltroValorConceptoIngreso(Connection con, int concepto)
	{
		String filtroValorIngreso="";
		HashMap mapaRetorno=new HashMap();
		try
		{	
			String consulta="SELECT valor FROM tesoreria.conceptos_ing_tesoreria WHERE codigo=? ";
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setString(1, concepto+"");
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
				filtroValorIngreso=rs.getString("valor");
				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("error en obtenerFiltroValorConceptoIngreso----->"+e);
			
		}
		return filtroValorIngreso;
	}
	
	

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static DtoDiagnostico getDiagnosticoPacienteIngreso(Connection con,int idIngreso,int tipoBD) 
	{
		int idCuenta=obtenerUltimaCuentaIngreso(con,idIngreso);
		return getDiagnosticoPacienteCuenta(con,idCuenta,tipoBD);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoBD 
	 * @param idIngreso
	 * @return
	 */
	public static DtoDiagnostico getDiagnosticoPacienteCuenta(Connection con,int idCuenta, int tipoBD) 
	{
		DtoDiagnostico dto=new DtoDiagnostico();
		try
		{
			HashMap tiposPacViaIng=obtenerTiposPacienteCuenta(con, idCuenta);
			String tipoPaciente=tiposPacViaIng.get("tipopaciente_0")+"";
			int viaIngreso=Utilidades.convertirAEntero(tiposPacViaIng.get("viaingreso_0")+"");
			if(viaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
			{
				//tomar los diagnosticos de la ultimva consulta,
				String consulta="SELECT val.fecha_valoracion as fecha,val.hora_valoracion as hora,vd.acronimo_diagnostico as diagnostico,vd.tipo_cie_diagnostico as cie  from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join valoraciones_consulta vc on (vc.numero_solicitud=val.numero_solicitud) inner join val_diagnosticos vd on (vd.valoracion=val.numero_solicitud and vd.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') and cuenta="+idCuenta+" order by fecha desc,hora desc";
				PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
					dto.setTipoCieDiagnostico(rs.getString("cie"));
					if(rs.getString("cie") != null){
						dto.setTipoCieDiagnosticoInt(new Integer(rs.getString("cie")));
					}
				}
				rs.close();
				ps.close();
			}
			else if(viaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios)
			{
				
				StringBuffer queryUltimoDiagnostico=new StringBuffer("");
				queryUltimoDiagnostico.append("SELECT tipo_sol,fecha_grabacion,hora_grabacion,acronimo_diagnostico,tipo_cie_diagnostico ")
					.append("FROM (SELECT * ")
						.append("FROM ")
	/*CARGOS DIRECTOS*/
							.append("(SELECT sol.tipo as tipo_sol, ")
								.append("sol.numero_solicitud as numero_solicitud, ")
								.append("case when sol.acronimo_diagnostico is not null then sol.acronimo_diagnostico else dxcd.acronimo_diagnostico end as acronimo_diagnostico, ")
								.append("case when sol.tipo_cie_diagnostico is not null then sol.tipo_cie_diagnostico else dxcd.tipo_cie_diagnostico end as tipo_cie_diagnostico, ")
								.append("sol.fecha_grabacion as fecha_grabacion, ")
								.append("sol.hora_grabacion as hora_grabacion ")
							.append("FROM  ")
							.append("ordenes.SOLICITUDES sol ")
								.append("INNER JOIN facturacion.cargos_directos CD ")
								.append("ON cd.numero_solicitud = sol.numero_solicitud ")
								.append("LEFT JOIN facturacion.cargos_directos_hc CDHC ")
								.append("ON cdhc.codigo=cd.codigo_datos_hc ")
								.append("LEFT JOIN diag_cargos_directos_hc DXCD ")
								.append("ON dxcd.codigo_cargo_directo=cdhc.codigo ")
							.append("WHERE sol.cuenta = ? ")
								.append("and sol.tipo= ").append(ConstantesBD.codigoTipoSolicitudCargosDirectosServicios).append(" ")
						.append("UNION ")
	/*PROCEDIMIENTOS*/
							.append("SELECT sol.tipo as tipo_sol, ")
								.append("sol.numero_solicitud as numero_solicitud, ")
								.append("dp.acronimo as acronimo_diagnostico, ")
								.append("dp.tipo_cie as tipo_cie_diagnostico, ")
								.append("sol.fecha_grabacion as fecha_grabacion, ")
								.append("sol.hora_grabacion as hora_grabacion ")
							.append("FROM ") 
								.append("ordenes.SOLICITUDES sol ")
								.append("LEFT JOIN ordenes.res_sol_proc rsp ")
								.append("ON rsp.numero_solicitud = sol.numero_solicitud ")
								.append("LEFT JOIN ordenes.diag_procedimientos DP ")
								.append("ON dp.codigo_respuesta=rsp.codigo ")
							.append("WHERE sol.cuenta = ? ")
								.append("and (dp.principal is null ");
				
								if(tipoBD==DaoFactory.ORACLE){
									queryUltimoDiagnostico.append("or dp.principal = 1) ");
								}else{
									if(tipoBD==DaoFactory.POSTGRESQL){
										queryUltimoDiagnostico.append("or dp.principal = true) ");
									}
								}
								
								queryUltimoDiagnostico.append("and sol.tipo= ").append(ConstantesBD.codigoTipoSolicitudProcedimiento).append(" ")
						.append("UNION ")
	/*INTERCONSULTAS*/
							.append("SELECT sol.tipo as tipo_sol, ")
								.append("sol.numero_solicitud as numero_solicitud, ")
								.append("vd.acronimo_diagnostico as acronimo_diagnostico, ")
								.append("vd.tipo_cie_diagnostico as tipo_cie_diagnostico, ")
								.append("sol.fecha_grabacion as fecha_grabacion, ")
								.append("sol.hora_grabacion as hora_grabacion ")
							.append("FROM  ")
								.append("ordenes.SOLICITUDES sol ")
								.append("LEFT JOIN historiaclinica.valoraciones val ")
								.append("ON val.numero_solicitud = sol.numero_solicitud ")
								.append("LEFT JOIN historiaclinica.val_diagnosticos VD ")
								.append("ON vd.valoracion=val.numero_solicitud ")
							.append("WHERE sol.cuenta = ? ")
								.append("and sol.tipo= ").append(ConstantesBD.codigoTipoSolicitudInterconsulta).append(" ")
						.append("UNION ")
	/*CIRUGIAS*/
							.append("SELECT sol.tipo as tipo_sol, ")
								.append("sol.numero_solicitud as numero_solicitud, ")
								.append("CASE WHEN sol.acronimo_diagnostico IS NOT NULL THEN sol.acronimo_diagnostico ELSE ")
								.append("(CASE WHEN dpcx.diagnostico IS NOT NULL THEN dpcx.diagnostico ELSE dposc.diagnostico END) ")
								.append("END AS acronimo_diagnostico, ")
								.append("CASE WHEN sol.tipo_cie_diagnostico IS NOT NULL THEN sol.tipo_cie_diagnostico ELSE ")
								.append("(CASE WHEN dpcx.tipo_cie IS NOT NULL THEN dpcx.tipo_cie ELSE dposc.tipo_cie END) ")
								.append("END AS tipo_cie_diagnostico, ")
								.append("sol.fecha_grabacion as fecha_grabacion, ")
								.append("sol.hora_grabacion as hora_grabacion ")
							.append("FROM ") 
								.append("ordenes.SOLICITUDES sol ")
								.append("LEFT JOIN salascirugia.diag_preoperatorio_cx dpcx ")
								.append("ON dpcx.numero_solicitud = sol.numero_solicitud ")
								.append("LEFT JOIN sol_cirugia_por_servicio scps ")
								.append("ON scps.numero_solicitud = sol.numero_solicitud ")
								.append("LEFT JOIN INFORME_QX_POR_ESPECIALIDAD informe ")
								.append("ON scps.COD_INFORME_ESPECIALIDAD = informe.codigo ")
								.append("LEFT JOIN diag_post_opera_sol_cx dposc ")
								.append("ON dposc.COD_INFORME_ESPECIALIDAD=informe.codigo ")								
							.append("WHERE sol.cuenta = ? ")
								.append("and sol.tipo=  ").append(ConstantesBD.codigoTipoSolicitudCirugia).append(" ) primer_resultado ")
					.append("ORDER BY fecha_grabacion desc,hora_grabacion desc,acronimo_diagnostico asc,tipo_cie_diagnostico asc) resultado ");
				
				if(tipoBD==DaoFactory.ORACLE){
					queryUltimoDiagnostico.append("WHERE ROWNUM <= 1 ");
				}else{
					if(tipoBD==DaoFactory.POSTGRESQL){
						queryUltimoDiagnostico.append("LIMIT 1 ");
					}
				}
				
				//tomar los diagnosticos de la ultima solicitud u orden,
				//String consulta="select diagnostico,cie from ( SELECT acronimo_diagnostico as diagnostico,tipo_cie_diagnostico as cie,fecha as fecha,hora as hora  from ordenes.ordenes_ambulatorias where cuenta_solicitante="+idCuenta+" UNION SELECT acronimo_diagnostico as diagnostico,tipo_cie_diagnostico as cie,fecha_grabacion as fecha,hora_grabacion as hora  from ordenes.solicitudes where cuenta="+idCuenta+") tabla order by fecha desc,hora desc";
				String consulta=queryUltimoDiagnostico.toString();
				PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
				ps.setInt(1, idCuenta);
				ps.setInt(2, idCuenta);
				ps.setInt(3, idCuenta);
				ps.setInt(4, idCuenta);
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					dto.setAcronimoDiagnostico(rs.getString("acronimo_diagnostico"));
					dto.setTipoCieDiagnostico(rs.getString("tipo_cie_diagnostico"));
				}
				rs.close();
				ps.close();
			}
			else if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			{
				if(SqlBaseUtilidadValidacionDao.existeEgresoCompleto(con, idCuenta))
				{
					//se toma del egreso.
					String consulta="SELECT diagnostico_principal as diagnostico,diagnostico_principal_cie as cie from egresos where codigo_medico is not null and usuario_responsable is not null and cuenta="+idCuenta;
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					{
						dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
						dto.setTipoCieDiagnostico(rs.getString("cie"));
					}
					rs.close();
					ps.close();
				}
				else
				{
					//se toma de la ultima evolucion o interconsulta.
					String consulta="select diagnostico,cie from ( SELECT fecha_evolucion as fecha,hora_evolucion as hora,ed.acronimo_diagnostico as diagnostico,ed.tipo_cie_diagnostico as cie from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join evoluciones evo on (evo.valoracion=val.numero_solicitud) inner join evol_diagnosticos ed on(evo.codigo=ed.evolucion and ed.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta+" UNION SELECT val.fecha_valoracion as fecha,val.hora_valoracion as hora,vd.acronimo_diagnostico as diagnostico,vd.tipo_cie_diagnostico as cie  from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join valoraciones_consulta vc on (vc.numero_solicitud=val.numero_solicitud) inner join val_diagnosticos vd on (vd.valoracion=val.numero_solicitud and vd.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta+") tabla order by fecha desc,hora desc";
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					{
						dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
						dto.setTipoCieDiagnostico(rs.getString("cie"));
					}
					else
					{
						//tomar de la valoracion de urgencias.
						consulta=" SELECT vd.acronimo_diagnostico as diagnostico,vd.tipo_cie_diagnostico as cie  from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join valoraciones_urgencias vu on (vu.numero_solicitud=val.numero_solicitud) inner join val_diagnosticos vd on (vd.valoracion=val.numero_solicitud and vd.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta;
						ps=new PreparedStatementDecorator(con,consulta);
						rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
							dto.setTipoCieDiagnostico(rs.getString("cie"));
						}
					}
					rs.close();
					ps.close();
				}
			}
			else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			{
				HashMap campos=new HashMap<String, String>();
				campos.put("idCuenta", idCuenta+"");
				/*
				if(tipoPaciente.equals(ConstantesBD.tipoPacienteHospitalizado))
				{
				*/
					if(SqlBaseUtilidadValidacionDao.existeEgresoCompleto(con, idCuenta))
					{
						//se toma del egreso.
						String consulta="SELECT diagnostico_principal as diagnostico,diagnostico_principal_cie as cie from egresos where codigo_medico is not null and usuario_responsable is not null and cuenta="+idCuenta;
						PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
						ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
							dto.setTipoCieDiagnostico(rs.getString("cie"));
						}
						rs.close();
						ps.close();
					}
					else
					{
						//se toma de la ultima evolucion o interconsulta.
						String consulta="select diagnostico,cie from ( SELECT fecha_evolucion as fecha,hora_evolucion as hora,ed.acronimo_diagnostico as diagnostico,ed.tipo_cie_diagnostico as cie from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join evoluciones evo on (evo.valoracion=val.numero_solicitud) inner join evol_diagnosticos ed on(evo.codigo=ed.evolucion and ed.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta+" UNION SELECT val.fecha_valoracion as fecha,val.hora_valoracion as hora,vd.acronimo_diagnostico as diagnostico,vd.tipo_cie_diagnostico as cie  from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join valoraciones_consulta vc on (vc.numero_solicitud=val.numero_solicitud) inner join val_diagnosticos vd on (vd.valoracion=val.numero_solicitud and vd.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta+") tabla order by fecha desc,hora desc";
						PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
						ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
							dto.setTipoCieDiagnostico(rs.getString("cie"));
						}
						else
						{
							//tomar de la valoracion de hospitalizacion.
							consulta=" SELECT vd.acronimo_diagnostico as diagnostico,vd.tipo_cie_diagnostico as cie  from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join val_hospitalizacion vh on (vh.numero_solicitud=val.numero_solicitud) inner join val_diagnosticos vd on (vd.valoracion=val.numero_solicitud and vd.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta;
							ps=new PreparedStatementDecorator(con,consulta);
							rs=new ResultSetDecorator(ps.executeQuery());
							if(rs.next())
							{
								dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
								dto.setTipoCieDiagnostico(rs.getString("cie"));
							}
						}
						rs.close();
						ps.close();
					}
				/*
				}
				else if (tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
				{
					if(SqlBaseUtilidadValidacionDao.existeEgresoCompleto(con, idCuenta))
					{
						//tomar el diagnostico y tipo cie del egreso.
						String consulta="SELECT diagnostico_principal as diagnostico,diagnostico_principal_cie as cie from egresos where codigo_medico is not null and usuario_responsable is not null and cuenta="+idCuenta;
						PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
						ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
							dto.setTipoCieDiagnostico(rs.getString("cie"));
						}
						rs.close();
						ps.close();
					}
					else
					{
						//tomar el diagnostico de la ultima evolucion o interconsulta
						//se toma de la ultima evolucion o interconsulta.
						String consulta="select diagnostico,cie from ( SELECT fecha_evolucion as fecha,hora_evolucion as hora,ed.acronimo_diagnostico as diagnostico,ed.tipo_cie_diagnostico as cie from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join evoluciones evo on (evo.valoracion=val.numero_solicitud) inner join evol_diagnosticos ed on(evo.codigo=ed.evolucion and ed.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta+" UNION SELECT val.fecha_valoracion as fecha,val.hora_valoracion as hora,vd.acronimo_diagnostico as diagnostico,vd.tipo_cie_diagnostico as cie  from solicitudes sol inner join valoraciones val on (sol.numero_solicitud=val.numero_solicitud) inner join valoraciones_consulta vc on (vc.numero_solicitud=val.numero_solicitud) inner join val_diagnosticos vd on (vd.valoracion=val.numero_solicitud and vd.principal='"+ValoresPorDefecto.getValorTrueParaConsultas()+"') where sol.cuenta="+idCuenta+") tabla order by fecha desc,hora desc";
						PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
						ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							dto.setAcronimoDiagnostico(rs.getString("diagnostico"));
							dto.setTipoCieDiagnostico(rs.getString("cie"));
						}
					}
				}
				*/
			}
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
			
		return dto;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static int obtenerUltimaCuentaIngreso(Connection con,int idIngreso)
	{
		String consulta="SELECT max(id) as cuenta from cuentas where id_ingreso="+idIngreso;
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
		int codigoCuenta=ConstantesBD.codigoNuncaValido;
		try
		{
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				codigoCuenta=rs.getInt("cuenta");
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error consultando la cuenta",e);
		}
		return codigoCuenta;
	}

	/**
	 * 
	 * @param con
	 * @param numReciboCaja
	 * @param codigoInstitucionInt
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static String obtenerCodigoReciboCaja(Connection con,
			String numReciboCaja, int codigoInstitucionInt,
			int codigoCentroAtencion) {
		String consulta=" SELECT numero_recibo_caja from recibos_caja where consecutivo_recibo='"+numReciboCaja+"' and institucion="+codigoInstitucionInt+" and centro_atencion="+codigoCentroAtencion;
		String resultado="";
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
		resultado=ConstantesBD.codigoNuncaValido+"";
		try
		{
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getString(1);
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error consultando la cuenta",e);
		}
		return resultado;
	}

	/**
	 * Obtiene el siguiente consecutivo disponible
	 * @param consulta
	 * @param sentenciaBloqueo
	 */
	public static String obtenerValorConsecutivoDisponible(String consulta, String sentenciaBloqueo)
	{
		String resultado=ConstantesBD.codigoNuncaValido+"";
		
		PreparedStatement psd=null;
		ResultSet rs=null;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		try
		{
			//psd=new PreparedStatementDecorator(con, sentenciaBloqueo);
			//psd.execute();
			//psd.cerrarPreparedStatement();
			psd=con.prepareStatement(consulta);
			rs=psd.executeQuery();
			if(rs.next())
			{
				resultado=rs.getString("valor");
			}
		} catch (SQLException e)
		{
			Log4JManager.error("Error capturando el valor del consecutivo disponible", e);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
		finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(psd!=null){
					psd.close();
				}
				UtilidadBD.finalizarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
			catch (Exception e) {
				logger.error("ERROR obtenerValorConsecutivoDisponible",e);
			}
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param clasificacionSE
	 * @return
	 */
	public static String obtenerTipoRegimenClasificacionSocioEconomica(Connection con, int clasificacionSE) 
	{
		String resultado="";
		PreparedStatementDecorator psd;
		try
		{
			psd=new PreparedStatementDecorator(con, "select tipo_regimen from estratos_sociales where codigo="+clasificacionSE);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				resultado=rsd.getString(1);
			}
			rsd.close();
			psd.close();
		}
		catch (SQLException e)
		{
			Log4JManager.error("Error", e);
		}
		return resultado;
	}
	
	/**
	 * Verfica si existen conceptos notas pacientes creados
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public static boolean existenConceptosNotasPaciente(Connection con, String sentencia) {
		boolean resultado = false;
		try{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, sentencia);
			ResultSetDecorator rsd=new ResultSetDecorator(stm.executeQuery());
			if (rsd.next()) {
				resultado = (rsd.getInt("num_registros") > 0);
			}
			rsd.close();
			stm.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			Log4JManager.error("Error consultando las conceptos notas paciente");
		}
		
		return resultado;
	}
	
	/**
	 * Verfica si existen notas pacientes creadas
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public static boolean existenNotasPaciente(Connection con, String sentencia) {
		boolean resultado = false;
		try{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, sentencia);
			ResultSetDecorator rsd=new ResultSetDecorator(stm.executeQuery());
			if (rsd.next()) {
				resultado = (rsd.getInt("num_registros") > 0);
			}
			rsd.close();
			stm.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			Log4JManager.error("Error consultando las notas pacientes");
		}
		
		return resultado;
	}
	
	/**
	 * Verfica si existen movimientos abono ingresados creadas
	 * @param sentencia Sentencia SQL
	 * @return boolean con resultado
	 * @since 27 Jul 2011
	 * @author diecorqu
	 */
	public static boolean existenMovimientosAbonos(Connection con, String sentencia) {
		boolean resultado = false;
		try{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, sentencia);
			ResultSetDecorator rsd=new ResultSetDecorator(stm.executeQuery());
			if (rsd.next()) {
				resultado = (rsd.getInt("num_registros") > 0);
			}
			rsd.close();
			stm.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			Log4JManager.error("Error consultando los movimientos abono");
		}
		
		return resultado;
	}
	
	/**
	 * Este metodo consulta los centros de atención por usuario y estado activo
	 * @param con
	 * @param usuario
	 * @return Un ArrayList<String[]> donde la posición 0 tiene el consecutivo del centro de atención
	 * 		   y en la posición 1 la descripción del mismo
	 */
	public static ArrayList<String[]> obtenerCentrosAtencionxUsarioEstadoActivo(Connection con, String usuario) {
		PreparedStatementDecorator psd;
		ArrayList<String[]> listaCentrosCosto = new ArrayList<String[]>();
		try	{
			psd = new PreparedStatementDecorator(con, strConsultaCentroAtencionxUsuarioEstadoActivo);
			psd.setString(1, usuario);
			ResultSetDecorator rsd = new ResultSetDecorator(psd.executeQuery());
			while(rsd.next()) {
				String[] datosCentroCosto = new String[2];
				datosCentroCosto[0] = String.valueOf(rsd.getInt(1));
				datosCentroCosto[1] = String.valueOf(rsd.getString(2));
				listaCentrosCosto.add(datosCentroCosto);
			}
			rsd.close();
			psd.close();
		}
		catch (SQLException e)
		{
			Log4JManager.error("Error", e);
		}
		return listaCentrosCosto;
	}
	
	/**
	 * Este metodo consulta los centros de costo por usuario
	 * @param con
	 * @param usuario
	 * @return Un ArrayList<String[]> donde la posición 0 tiene el codigo del centro de costo,
	 * 		   en la posición 1 el nombrem en la 2 la descripción del centro de atención
	 * 		   y la posición 3 el consecutivo del centro de atención al que pertenece
	 */
	public static ArrayList<String[]> obtenerCentrosCostoxUsario(Connection con, String usuario) {
		PreparedStatementDecorator psd;
		ArrayList<String[]> listaCentrosCosto = new ArrayList<String[]>();
		try	{
			psd = new PreparedStatementDecorator(con, strConsultaCentroCostoxUsuario);
			psd.setString(1, usuario);
			ResultSetDecorator rsd = new ResultSetDecorator(psd.executeQuery());
			while(rsd.next()) {
				String[] datosCentroCosto = new String[4];
				datosCentroCosto[0] = String.valueOf(rsd.getInt(1));
				datosCentroCosto[1] = String.valueOf(rsd.getString(2));
				datosCentroCosto[2] = String.valueOf(rsd.getString(3));
				datosCentroCosto[3] = String.valueOf(rsd.getInt(4));
				listaCentrosCosto.add(datosCentroCosto);
			}
			rsd.close();
			psd.close();
		}
		catch (SQLException e)
		{
			Log4JManager.error("Error", e);
		}
		return listaCentrosCosto;
	}
	
	/**
	 * @param con
	 * @param centroAtencion
	 * @param usuario
	 * @param valorParametro
	 * @return cantidad de registros de caja 
	 */
	public static int obtenerCantidadCajasCajero(Connection con,int centroAtencion, String usuario,String valorParametro)
	{
		Integer contador = new Integer (0);
		
		String consulta="SELECT v.codigo from tesoreria.view_cajas_cajeros_reca_mayor v"+
						" where v.centro_atencion=? " +
						" and v.usuario=? " ;
		
		
		if(valorParametro.equals(String.valueOf(ConstantesBD.codigoNuncaValido)) || 
				valorParametro.equals(String.valueOf(ConstantesBD.acronimoNo))){
			
			consulta+= " and v.tipo_caja='recaudo'  " +
			           " and v.cajero  = ? ";
			
		}else{
			consulta+= "and v.tipo_caja='recaudo_caja_mayor'  " +
			   "and v.cajero in  (?,'cajero_estatico')";
		}
		try
		{
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
		ps.setInt(1, centroAtencion);
		ps.setString(2, usuario);
		ps.setString(3, usuario);
		
		int codigoCuenta=ConstantesBD.codigoNuncaValido;
		
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				contador++;
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error consultando la cuenta",e);
		}
		return contador;
	}
	
	/**
	 * @param con
	 * @param numeroSolcitud
	 * @return numero de la cuenta de autorización por orden ambulatoria
	 */
	public static Integer obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(Connection con,Integer numeroSolcitud){
		String consulta =" SELECT cuenta_solicitante AS cuenta FROM ordenes_ambulatorias WHERE codigo=? ";
		Integer  cuenta = new Integer(0);
		try {
			PreparedStatement pst = con.prepareStatement(consulta);
			pst.setInt(1,numeroSolcitud);
			ResultSet res = pst.executeQuery();

			if(res.next()){
				cuenta = res.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Error consultando numero de cuenta de autorizacion por orden ambulatoria -->"+e.getMessage());
		}
		return cuenta;
	}
	
	/**
	 * @param con
	 * @param codigoVia
	 * @return nombre via de administracion 
	 */
	public static String obtenerNombreViaAdministracion(Connection con,int codigoVia)
	{
		String resp="";
		
		String consulta="Select va.nombre "+
						"From vias_administracion va "+
						"where va.codigo=? ";
		
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setInt(1, codigoVia);
		
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resp=rs.getString(1);
				
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error consultando la via de administracion",e);
		}
		return resp;
	}
	
	
	/**
	 * Validad por el codigo ingresado si el convenio es capitacion subcontrada 
	 * @param con
	 * @param codigoConvenio
	 * @return si la entidad es capitacion subcontratada 
	 */
	public static Boolean esCapitacionSubcontratada(Connection con,Integer codigoConvenio){
		String consulta = " select c.codigo from  convenios c " +
				"  WHERE c.capitacion_subcontratada= ?"+ 
				"  AND c.codigo = ?";
		
		try {
			PreparedStatement pst = con.prepareStatement(consulta);
			pst.setString(1, ConstantesBD.acronimoSi);
			pst.setInt(2, codigoConvenio);
			ResultSet res = pst.executeQuery();
			if(res.next()){
				return true;
			}
		} catch (SQLException e) {
			logger.error("Error consultado si el convenio es capitacion subcontratada "+e.getMessage());
		}
		return false;
	}

	public static boolean liberarUltimaCamaPaciente(Connection con, Integer codigoPersona, String consultaUltimaCamaPaciente, String consultaPacienteEnCama)
	{
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		PreparedStatement ps3 = null;
		boolean huboCambio = false;
		try
		{
			String cuenta = null;
			String cama = null;
			String fecha = null;
			String hora = null;
			
			ps1 = con.prepareStatement(consultaUltimaCamaPaciente);
			ps1.setInt(1, codigoPersona);
			ps1.setInt(2, codigoPersona);
			ps1.setInt(3, codigoPersona);
			rs1 = ps1.executeQuery();
			if(rs1.next())
			{
				cuenta = rs1.getString("cuenta");
				cama = rs1.getString("cama");
				fecha = rs1.getString("fecha");
				hora = rs1.getString("hora");
			}
			
			if(cama != null)
			{
				ps2 = con.prepareStatement(consultaPacienteEnCama);
				ps2.setInt(1, Integer.valueOf(cama));
				ps2.setInt(2, Integer.valueOf(cama));
				ps2.setInt(3, Integer.valueOf(cama));
				rs2 = ps2.executeQuery();
				if(rs2.next())
				{
					if
					(
						cuenta.equals(rs2.getInt("cuenta")+"") &&
						cama.equals(rs2.getInt("cama")+"") &&
						fecha.equals(rs2.getString("fecha"))&&
						hora.equals(rs2.getString("hora"))
					)
					{
						ps3 = con.prepareStatement("UPDATE manejopaciente.camas1 SET estado = 0 WHERE codigo = ?");
						ps3.setInt(1, Integer.valueOf(cama));
						ps3.executeUpdate();
						huboCambio = true;
					}
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultado camas: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(ps1 != null)
				{
					ps1.close();
				}
				if(rs1 != null)
				{
					rs1.close();
				}
				if(ps2 != null)
				{
					ps2.close();
				}
				if(rs2 != null)
				{
					rs2.close();
				}
				if(ps3 != null)
				{
					ps3.close();
				}
			}
			catch (SQLException e2)
			{
				logger.error("Error consultado camas: " + e2.getMessage());
				e2.printStackTrace();
			}
		}
		return huboCambio;
	}

	/**
	 * Consulta todas las especialidades que posee un profesional de la salud
	 * 
	 * @param connection
	 * @param profesionalHQxDto
	 * @param descartarEspecialidades
	 * @return lista de especialidades de un profesional
	 * @throws BDException
	 * @author jeilones
	 * @created 5/07/2013
	 */
	public static List<EspecialidadDto> consultarEspecialidadesProfesional(Connection connection,ProfesionalHQxDto profesionalHQxDto,List<EspecialidadDto>descartarEspecialidades,int codigoInstitucion, Boolean especiliadadesActivas) throws BDException{
		
		StringBuffer consulta=new StringBuffer("SELECT esp.codigo ,esp.nombre ")
			.append("FROM administracion.personas per ")
			.append("INNER JOIN administracion.medicos med ON med.codigo_medico = per.codigo  ")
			.append("INNER JOIN administracion.especialidades_medicos em ON em.codigo_medico = med.codigo_medico ")
			.append("INNER JOIN administracion.especialidades esp on esp.codigo = em.codigo_especialidad ")
			.append("where per.codigo = ? ")
			.append("AND esp.institucion = ? ");
		
		if(especiliadadesActivas!=null ){
			consulta.append("AND em.activa_sistema = ? ");
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<EspecialidadDto>especialidadesProfesional=new ArrayList<EspecialidadDto>(0);
		
		try {
			String cadena = null;
			if(descartarEspecialidades!=null&&!descartarEspecialidades.isEmpty()){
				consulta.append("and esp.codigo not in(");
				for(EspecialidadDto especialidad:descartarEspecialidades){
					consulta.append(especialidad.getCodigo()).append(", ");
				}
				cadena = consulta.substring(0, consulta.length()-2);
				consulta=new StringBuffer(cadena).append(") ");
			}
			consulta.append("order by esp.codigo asc,esp.nombre asc ");
			
			cadena=consulta.toString();
			
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, profesionalHQxDto.getIdMedico());
			ps.setInt(2, codigoInstitucion);
			
			if(especiliadadesActivas!=null){
				ps.setBoolean(3, especiliadadesActivas.booleanValue());
			}
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				EspecialidadDto especialidadDto=new EspecialidadDto();
				especialidadDto.setCodigo(rs.getInt("codigo"));
				especialidadDto.setNombre(rs.getString("nombre"));
				
				especialidadesProfesional.add(especialidadDto);
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
		return especialidadesProfesional;
	}
}