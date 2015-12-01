/*
 * @(#)SqlBasePersonaBasicaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.Persona;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a PersonaBasica
 * 
 *	@version 1.0, Mar 29, 2004
 */
public class SqlBasePersonaBasicaDao 
{
	
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBasePersonaBasicaDao.class);

	/**
	 * Cadena para verificar si el paciente tiene o no manejo conjunto
	 */
	private static final String tieneManejoConjuntoStr="SELECT c.id from manejopaciente.adjuntos_cuenta ac INNER JOIN manejopaciente.cuentas c ON(c.id=ac.cuenta) where ac.fecha_fin IS NULL AND c.codigo_paciente=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una
	 * persona básica desde la base de datos Genérica.
	 */
	//Con inner Join: La velocidad es mas o menos la misma, por eso no se cambio: SELECT p.codigo as codigoPersona, ti.nombre AS tipoIdentificacionPersona, p.tipo_identificacion AS codTipoIdentificacionPer, p.numero_identificacion AS numeroIdentificacionPersona, p.primer_nombre || ' ' || p.segundo_nombre || ' ' || p.primer_apellido || ' ' || p.segundo_apellido AS nombrePersona, p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS nombrePersona2, to_char(p.fecha_nacimiento, 'YYYY-MM-DD')  AS fechaNacimiento, p.sexo AS codigoSexo, s.nombre as sexo, ts.nombre as tipoSangre FROM personas p INNER JOIN tipos_identificacion ti ON (p.tipo_identificacion = ti.acronimo) INNER JOIN sexo s ON (p.sexo = s.codigo) INNER JOIN pacientes pac ON (pac.codigo_paciente = p.codigo ) INNER JOIN tipos_sangre ts ON (pac.tipo_sangre = ts.codigo) WHERE p.tipo_identificacion = ? AND p.numero_identificacion = ? 
	private static final String cargarPersonaBasicaStr = "SELECT " +
		"p.codigo as codigoPersona, " +
		"ti.nombre AS tipoIdentificacionPersona, " +
		"p.tipo_identificacion AS codTipoIdentificacionPer, " +
		"p.numero_identificacion AS numeroIdentificacionPersona, " +
		"coalesce(p.primer_nombre||' ','') || coalesce(p.segundo_nombre||' ','') || coalesce(p.primer_apellido||' ','') ||  coalesce(p.segundo_apellido||' ','') AS nombrePersona, " +
		"getnombrepersona(p.codigo) AS nombrePersona2, " +
		"to_char(p.fecha_nacimiento, 'YYYY-MM-DD')  AS fechaNacimiento, " +
		"p.sexo AS codigoSexo, " +
		"s.nombre as sexo, " +
		"ts.nombre as tipoSangre, " +
		"p.primer_nombre AS primer_nombre, " +
		"coalesce(p.segundo_nombre,'') AS segundo_nombre, " +
		"p.primer_apellido AS primer_apellido, " +
		"coalesce(p.segundo_apellido,'') AS segundo_apellido, " +
		"p.direccion AS direccion, " +
		"coalesce(p.telefono,'') AS telefono, " +
		"coalesce(p.telefono_fijo||'','') as telefonofijo, " +
		"coalesce(p.telefono_celular||'','') AS telefonocelular, " +
		"p.email as email, " +
		"getnombreciudad(p.codigo_pais_id,p.codigo_depto_id,p.codigo_ciudad_id) AS ciudad_expedicion," +
		"getnombredepto(p.codigo_pais_id,p.codigo_depto_id) AS depto_expedicion, " +
		"getdescripcionpais(p.codigo_pais_id) AS pais_expedicion, "+
		"getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda) AS ciudad," +
		"coalesce(codigo_ciudad_vivienda,'') AS codigo_ciudad, " +
		"getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) AS depto, " +
		"coalesce(codigo_departamento_vivienda,'') AS codigo_depto, " +
		"getdescripcionpais(p.codigo_pais_vivienda) AS pais, "+
		"coalesce(codigo_pais_vivienda,'') AS codigo_pais, " +
		"CASE WHEN pac.centro_atencion_pyp IS NULL THEN 0 ELSE pac.centro_atencion_pyp END AS codigo_centro_atencion_pyp, " +
		"CASE WHEN getNomCentroAtencion(pac.centro_atencion_pyp) IS NULL THEN '' ELSE getNomCentroAtencion(pac.centro_atencion_pyp) END AS nombre_centro_atencion_pyp,   " +
		"pac.lee_escribe AS lee_escribe, " +
		"pac.historia_clinica as historia_clinica, " +
		"CASE WHEN pac.etnia IS NULL THEN 0 ELSE pac.etnia END AS etnia, " +
		"CASE WHEN pac.estudio IS NULL THEN 0 ELSE pac.estudio END AS estudio," +
		"CASE WHEN pac.fecha_muerte IS NULL THEN '' ELSE to_char(fecha_muerte,'DD/MM/YYYY') END AS fecha_muerte, " +
		"pac.grupo_poblacional, " +
		"ba.descripcion as barrio," +
		"p.codigo_barrio_vivienda as codigo_barrio " +
		/*"es.descripcion as clasificacionse," +//
		"ta.nombre as tipoafiliado," +//
		"subc.nro_carnet as numerocarnet" +//*/
		" " +
		"from administracion.personas p " +
		"inner join  administracion.tipos_identificacion ti on (p.tipo_identificacion=ti.acronimo) " +
		/**MT3625 - Diana Ruiz*/
		"left join administracion.sexo s on(p.sexo=s.codigo) " +
		"inner join manejopaciente.pacientes pac on (pac.codigo_paciente=p.codigo) " +
		"left outer join manejopaciente.tipos_sangre ts on(ts.codigo=pac.tipo_sangre) " +
		"left outer join administracion.barrios ba ON(ba.codigo=p.codigo_barrio_vivienda) " +
		/*"left outer join capitacion.usuario_x_convenio  uxc ON(uxc.persona=p.codigo)"+//
		"left outer join manejopaciente.estratos_sociales es ON(es.codigo=uxc.clasificacion_socio_economica)"+//
		"left outer join manejopaciente.tipos_afiliado ta ON(ta.acronimo=uxc.tipo_afiliado)" +//
		"left outer join manejopaciente.sub_cuentas subc ON(subc.codigo_paciente=pac.codigo_paciente)" +//*/
		
		"where " +
		"p.tipo_identificacion = ? AND " +
		"p.numero_identificacion = ? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una
	 * persona básica desde la base de datos Genérica.
	 */
	private static final String cargarPersonaBasicaDadoCodigoStr = "SELECT " +
		"p.codigo as codigoPersona, " +
		"ti.nombre AS tipoIdentificacionPersona, " +
		"p.tipo_identificacion AS codTipoIdentificacionPer, " +
		"p.numero_identificacion AS numeroIdentificacionPersona, " +
		"coalesce(p.primer_nombre,'') || ' ' || coalesce(p.segundo_nombre,'') || ' ' || coalesce(p.primer_apellido,'') || ' ' || coalesce(p.segundo_apellido,'') AS nombrePersona, " +
		"coalesce(p.primer_apellido,'') || ' ' || coalesce(p.segundo_apellido,'') || ' ' || coalesce(p.primer_nombre,'') || ' ' || coalesce(p.segundo_nombre,'') AS nombrePersona2, " +
		"to_char(p.fecha_nacimiento, 'YYYY-MM-DD') AS fechaNacimiento, " +
		"p.sexo AS codigoSexo, " +
		"s.nombre as sexo, " +
		"ts.nombre as tipoSangre, " +
		"p.primer_nombre AS primer_nombre, " +
		"coalesce(p.segundo_nombre,'') AS segundo_nombre, " +
		"p.primer_apellido AS primer_apellido, " +
		"coalesce(p.segundo_apellido,'') AS segundo_apellido, " +
		"p.direccion AS direccion, " +
		"coalesce(p.telefono,'') AS telefono, " +
		"coalesce(p.telefono_fijo||'','') as telefonofijo, " +
		"coalesce(p.telefono_celular||'','') AS telefonocelular, " +
		"p.email as email, " +
		"getnombreciudad(p.codigo_pais_id,p.codigo_depto_id,p.codigo_ciudad_id) AS ciudad_expedicion," +
		"getnombredepto(p.codigo_pais_id,p.codigo_depto_id) AS depto_expedicion, " +
		"getdescripcionpais(p.codigo_pais_id) AS pais_expedicion, "+
		"getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda) AS ciudad," +
		"coalesce(codigo_ciudad_vivienda,'') AS codigo_ciudad, " +
		"getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) AS depto, " +
		"coalesce(codigo_departamento_vivienda,'') AS codigo_depto, " +
		"getdescripcionpais(p.codigo_pais_vivienda) AS pais, "+
		"coalesce(codigo_pais_vivienda,'') AS codigo_pais, " +
		"CASE WHEN pac.centro_atencion_pyp IS NULL THEN 0 ELSE pac.centro_atencion_pyp END AS codigo_centro_atencion_pyp, " +
		"CASE WHEN getNomCentroAtencion(pac.centro_atencion_pyp) IS NULL THEN '' ELSE getNomCentroAtencion(pac.centro_atencion_pyp) END AS nombre_centro_atencion_pyp,  " +
		"pac.lee_escribe AS lee_escribe, " +
		"pac.historia_clinica as historia_clinica, " +
		"CASE WHEN pac.etnia IS NULL THEN 0 ELSE pac.etnia END AS etnia, " +
		"CASE WHEN pac.estudio IS NULL THEN 0 ELSE pac.estudio END AS estudio, " +
		"CASE WHEN pac.fecha_muerte IS NULL THEN '' ELSE to_char(fecha_muerte,'DD/MM/YYYY') END AS fecha_muerte, " +
		"ba.descripcion as barrio," +
		"p.codigo_barrio_vivienda as codigo_barrio," +
		"pac.grupo_poblacional " +
		"FROM personas p " +
		"LEFT OUTER JOIN pacientes pac ON (pac.codigo_paciente = p.codigo) " +
		"LEFT OUTER JOIN tipos_identificacion ti ON (ti.acronimo = p.tipo_identificacion) " +
		"LEFT OUTER JOIN sexo s ON (s.codigo = p.sexo) " +
		"LEFT OUTER JOIN tipos_sangre ts ON (ts.codigo=pac.tipo_sangre) " +
		"left outer join administracion.barrios ba ON(ba.codigo=p.codigo_barrio_vivienda) " +
		"WHERE " +
		"p.codigo = ? ";

	/**
	 * Como cargar paciente necesita varias consultas (debido a que mucha de la
	 * información NO es obligatoria), aqui encontramos la primera que nos
	 * devuelve el codigo de la cuenta, el codigo del tipo de régimen, el código del
	 * convenio, el código de la empresa a la que pertenece este convenio, el nombre
	 * de la empresa, la via de ingreso y el codigo de la via de ingreso.
	 * NO se carga la cuenta a menos que exista Y que este abierta, o en su defecto que 
	 * este en estado Facturada Parcial.
	 */
	private static final String cargarPacienteConsultaGeneralStr=" SELECT " +
		"cue.id as codigoCuenta, " +
		"cue.estado_cuenta As estadoCuenta, " +
		"coalesce(rp.numero_identificacion,'') AS idResponsable," +
		"coalesce(rp.tipo_identificacion,'')   AS tipoIdResponsable, "+
		"conv.tipo_regimen as codigoTipoRegimen," +
		"coalesce(getnomtiporegimen(conv.tipo_regimen),'') AS nombreTipoRegimen, " +
		"conv.nombre as convenio, " +
		"conv.empresa as codigoEmpresa, " +
		"getnombreviaingresotipopac(cue.id) as viaIngreso, " +
		"CASE WHEN getFechaIngreso(cue.id,cue.via_ingreso) IS NULL THEN '' ELSE to_char(getFechaIngreso(cue.id,cue.via_ingreso),'DD/MM/YYYY') END AS fechaIngreso, "+
		"coalesce(substr(getHoraIngreso(cue.id,cue.via_ingreso),0,6),'') AS horaIngreso, "+
		"cue.via_ingreso as codigoViaIngreso, " +
		"cue.tipo_paciente AS codigoTipoPaciente, " +
		"getnombretipopaciente(cue.tipo_paciente) AS nombreTipoPaciente, "+
		"cue.tipo_evento as tipo_evento," +
		"cue.hospital_dia AS hospitalDia," +
		"coalesce(gettransplante(cue.id_ingreso),'') As transplante " +
		"from cuentas cue  " +		
		"INNER JOIN sub_cuentas subc ON(subc.ingreso=cue.id_ingreso AND subc.nro_prioridad = 1) " +
		"INNER JOIN convenios conv ON(conv.codigo=subc.convenio) " +
		"left outer join responsables_pacientes rp ON(rp.codigo = cue.codigo_responsable_paciente) "+
		"where " +
		"cue.id_ingreso =? " +
		"AND getcuentafinal(cue.id) IS NULL " ;
				
		

	/**
	* Método que carga el ingreso del paciente a partir del documento de identidad
	* NO se carga el ingreso  a menos que exista Y que este abierta
	*/
	private static final String cargarPacienteIngresoStr="SELECT "+ 
		"ing.id as codigoIngreso, " +
		"ing.consecutivo || CASE WHEN ing.anio_consecutivo IS NOT NULL AND ing.anio_consecutivo <> '' THEN '-' || ing.anio_consecutivo ELSE '' END as consecutivoIngreso, "+
		"c.estado_cuenta AS estadoCuenta, " +
		"rp.numero_identificacion AS idResponsable," +
		"rp.tipo_identificacion   AS tipoIdResponsable," +
		"coalesce(ing.pac_entidades_subcontratadas||'','') AS pacEntidadSubcontratada "+
		"from administracion.personas per "+ 
		"inner join manejopaciente.ingresos ing ON(ing.codigo_paciente=per.codigo) "+
		"inner join manejopaciente.cuentas c ON(c.id_ingreso=ing.id) "+		
		"inner join administracion.centros_costo cc ON(cc.codigo=c.area) "+
		"left outer join manejopaciente.responsables_pacientes rp ON(rp.codigo = c.codigo_responsable_paciente) "+
		"where "+  
		"per.tipo_identificacion=? and "+ 
		"per.numero_identificacion=? and "+ 
		"ing.institucion=? and "+
		"ing.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";

	
	
	/**
	 * Una vez se tienen los ingresos, se puede sacar la información de la
	 * admisión. Esta puede o no existir y por eso esta separada de las
	 * consultas anteriores. Se sabe que una admision es de hospitalizacion si
	 * el anio es 0
	 */
	private static final String cargarPacienteConsultaAdmisionesStr=
		"  SELECT codigo as codigoAdmision, anio as anioAdmision from admisiones_urgencias where cuenta=? " +
		" UNION " +
		"  SELECT codigo, 0 as anio from admisiones_hospi where cuenta=? and estado_admision=1 ";
	

	/**
	* Consulta que carga el ingreso del paciente a partir del código de la persona
	* NO se carga el ingreso  a menos que exista Y que este abierta
	*/
	private static final String is_cargarPacienteIngreso = "SELECT "+ 
		"ing.id as codigoIngreso, "+
		"ing.consecutivo || CASE WHEN ing.anio_consecutivo IS NOT NULL AND ing.anio_consecutivo <> '' THEN '-' || ing.anio_consecutivo ELSE '' END as consecutivoIngreso, "+
		"c.estado_cuenta AS estadoCuenta," +		
		"rp.numero_identificacion AS idResponsable," +
		"rp.tipo_identificacion   AS tipoIdResponsable, " +
		"coalesce(ing.pac_entidades_subcontratadas||'','') AS pacEntidadSubcontratada "+
		"from personas per "+ 
		"inner join ingresos ing ON(ing.codigo_paciente=per.codigo) "+
		"inner join cuentas c ON(c.id_ingreso=ing.id) "+ 
		"inner join centros_costo cc ON(cc.codigo=c.area) " +
		"left outer join responsables_pacientes rp ON(rp.codigo = c.codigo_responsable_paciente) "+
		"where "+  
		"per.codigo=? and "+ 
		"ing.institucion=? and "+ 
		"ing.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'";

	/**
	 * Consulta que carga el código del tarifario actual para un paciente
	 * dado
	 */
	private static final String getCodigoTarifarioActualStr ="SELECT " +
		"0 as codigoTarifarioOficial , " +
		"conv.codigo as codigoConvenio, " +
		"subc.contrato as codigoContrato " +
		"from cuentas cue " +
		"INNER JOIN sub_cuentas subc ON(subc.ingreso=cue.id_ingreso AND subc.nro_prioridad = 1)" +
		"INNER JOIN convenios conv ON (conv.codigo=subc.convenio) " +
		"where " +
		"cue.id=? ";
	
	
	
	
	//***********************************************************************************
	//********************************Atributos de la funcionalidad Cargar Otros Ingresos
	
	/**
	 * Cadena de Consulta de otros ingresos del paciente 
	 * */
	private static final String CadenaConsultaOtrosIngresosStr = "SELECT " +
			"ing.id AS ingreso," +
			"ing.consecutivo AS ingreso_consecutivo," +
			"CASE WHEN ing.preingreso IS NOT NULL THEN ing.preingreso ELSE '' END AS preingreso, " +
			"CASE WHEN ing.reingreso IS NOT NULL THEN ing.reingreso ELSE 0 END AS reingreso, " +
			"CASE WHEN ing.anio_consecutivo IS NULL THEN '' ELSE ing.anio_consecutivo END  AS anio_consecutivo," +
			"ing.codigo_paciente AS codigo_paciente," +
			"ing.estado AS estado_ingreso," +
			"ing.institucion AS institucion," +
			"CASE WHEN ing.fecha_ingreso IS NULL THEN '' ELSE to_char(ing.fecha_ingreso,'DD/MM/YYYY') || ' ' || ing.hora_ingreso END AS fecha_ingreso, " +
			"CASE WHEN ci.fecha_cierre  IS NULL THEN '' ELSE to_char(ci.fecha_cierre, 'DD/MM/YYYY') || ' ' || ci.hora_cierre END AS fecha_egreso, " +			
			"cue.id AS numero_cuenta, " +
			"cue.estado_cuenta AS estado_cuenta," +
			"getnombreestadocuenta(cue.estado_cuenta) AS nombre_estado_cuenta," +
			"cue.via_ingreso AS via_ingreso," +
			"getnombreviaingresotipopac(cue.id) AS nombre_via_ingreso, " +
			"ca.consecutivo AS consecutivo_catencion, " +
			"ca.codigo || ' ' || ca.descripcion AS descripcion_catencion " +
			"FROM manejopaciente.ingresos ing " +
			"INNER JOIN manejopaciente.cuentas cue ON (cue.id_ingreso=ing.id) " +
			"INNER JOIN administracion.centros_costo cc ON (cc.codigo=cue.area) " +
			"INNER JOIN administracion.centro_atencion ca ON (ca.consecutivo=cc.centro_atencion) " +
			"LEFT OUTER JOIN manejopaciente.cierre_ingresos ci ON(ci.id_ingreso = ing.id AND ci.activo = '"+ConstantesBD.acronimoSi+"') " +
			"WHERE " +
			"   (cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaActiva+" OR " +
			"	 cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" OR " +
			"	 cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+" OR " +
			"    cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+" OR " +
			"    cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" ) "+
			"	AND getcuentafinal(cue.id) IS NULL AND " +
			"(ing.estado ='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' OR ing.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' ) AND " +
			"ing.codigo_paciente=? ORDER BY ing.consecutivo DESC ";

	private static final String consultaTodosIngresosStr=" "+
		" SELECT ing.id    AS ingreso            , "+
		"  ing.consecutivo AS ingreso_consecutivo, "+
		"  CASE "+
		"    WHEN ing.preingreso IS NOT NULL "+
		"    THEN ing.preingreso "+
		"    ELSE '' "+
		"  END AS preingreso, "+
		"  CASE "+
		"    WHEN ing.reingreso IS NOT NULL "+
		"    THEN ing.reingreso "+
		"    ELSE 0 "+
		"  END AS reingreso, "+
		"  CASE "+
		"    WHEN ing.anio_consecutivo IS NULL "+
		"    THEN '' "+
		"    ELSE ing.anio_consecutivo "+
		"  END                 AS anio_consecutivo, "+
		"  ing.codigo_paciente AS codigo_paciente , "+
		"  ing.estado          AS estado_ingreso  , "+
		"  ing.institucion     AS institucion     , "+
		"  CASE "+
		"    WHEN ing.fecha_ingreso IS NULL "+
		"    THEN '' "+
		"    ELSE TO_CHAR(ing.fecha_ingreso,'"+ConstantesBD.formatoFechaAp+"') "+
		"      || ' ' "+
		"      || ing.hora_ingreso "+
		"  END AS fecha_ingreso, "+
		"  CASE "+
		"    WHEN ci.fecha_cierre IS NULL "+
		"    THEN '' "+
		"    ELSE TO_CHAR(ci.fecha_cierre, '"+ConstantesBD.formatoFechaAp+"') "+
		"      || ' ' "+
		"      || ci.hora_cierre "+
		"  END                                      AS fecha_egreso         , "+
		"  cue.id                                   AS numero_cuenta        , "+
		"  cue.estado_cuenta                        AS estado_cuenta        , "+
		"  getnombreestadocuenta(cue.estado_cuenta) AS nombre_estado_cuenta , "+
		"  cue.via_ingreso                          AS via_ingreso          , "+
		"  getnombreviaingresotipopac(cue.id)       AS nombre_via_ingreso   , "+
		"  ca.consecutivo                           AS consecutivo_catencion, "+
		"  ca.codigo "+
		"  || ' ' "+
		"  || ca.descripcion AS descripcion_catencion "+
		"   FROM manejopaciente.ingresos ing "+
		"INNER JOIN manejopaciente.cuentas cue "+
		"     ON (cue.id_ingreso=ing.id) "+
		"INNER JOIN administracion.centros_costo cc "+
		"     ON (cc.codigo=cue.area) "+
		"INNER JOIN administracion.centro_atencion ca "+
		"     ON (ca.consecutivo=cc.centro_atencion) "+
		"LEFT OUTER JOIN manejopaciente.cierre_ingresos ci "+
		"     ON(ci.id_ingreso       = ing.id "+
		"AND ci.activo               = 'S') "+
		"  WHERE "+ 
		"ing.codigo_paciente     =? "+
		"ORDER BY ing.consecutivo DESC";
	
	/**
	* Método que carga el ingreso del paciente a partir del codigo del Ingreso,
	* este codigo lo da la buqueda de la funcionalidad Cargar Otros Ingresos del 
	* Paciente
	*/
	private static final String cargarPacienteOtrosIngresoStr="SELECT "+ 
		"ing.id as codigoIngreso, "+
		"ing.consecutivo || CASE WHEN ing.anio_consecutivo IS NOT NULL AND ing.anio_consecutivo <> '' THEN '-' || ing.anio_consecutivo ELSE '' END as consecutivoIngreso, "+
		"c.estado_cuenta AS estadoCuenta, "+
		"coalesce(ing.pac_entidades_subcontratadas||'','') AS pacEntidadSubcontratada "+
		"from ingresos ing "+
		"inner join cuentas c ON(c.id_ingreso=ing.id) "+		
		"where "+  
		"ing.id = ? order by c.id desc ";	
	//***********************************************************************************
	//********************************Fin Atributos de la funcionalidad Cargar Otros Ingresos
	
	/**
	 * Consulta que dado un convenio busca el último contrato válido
	 */
	private static final String getCodigoUltimoContratoStr="select " +
																" cont.codigo as codigoContrato " +
															" from contratos cont " +
															" where cont.convenio =? " +
															" order by cont.fecha_final desc"; 

	/**
	 * Consulta que averigua las cuentas inicial de un asocio de cuenta
	 */
	private static final String getInformacionAsocioCuentaStr =
			"SELECT " +
			"cuenta_inicial as cuentaInicial, " +
			"cuenta_final as cuentaFinal " +
			"FROM asocios_cuenta " +
			"WHERE ingreso = ? AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +			
			"ORDER BY codigo DESC,fecha DESC, hora DESC ";
	
	/**
	 * Consulta la informacion de las cuentas de un paciente
	 * */
	private static final String getCuentasPacieteStr = "" +
			"SELECT " +
			"c.via_ingreso AS viaIngreso," +
			"vi.nombre AS descripcionViaIngreso," +
			"c.tipo_paciente AS tipoPaciente," +
			"tp.nombre AS descripcionTipoPaciente," +
			"c.id AS cuenta," +
			"c.estado_cuenta AS estadoCuenta," +
			"getnombreestadocuenta(c.estado_cuenta) AS descripcionEstadoCuenta," +
			"c.id_ingreso AS idIngreso " +			
			"FROM cuentas c " +
			"LEFT OUTER JOIN vias_ingreso vi ON (vi.codigo = c.via_ingreso) " +
			"LEFT OUTER JOIN tipos_paciente tp ON (tp.acronimo = c.tipo_paciente) " +
			"WHERE c.id_ingreso = ?  " +
			"ORDER BY c.fecha_apertura DESC, c.hora_apertura DESC, c.id DESC ";
	
	/**
	 * Consulta si un ingreso esta marcado como preingreso pendiente
	 */
	private static String consultaPreingresoP="SELECT CASE WHEN preingreso IS NOT NULL THEN preingreso ELSE '' END AS preingreso FROM ingresos WHERE id=? ";
	
	/**
	 * Consulta si un ingreso es un reingreso
	 */
	private static String consultaReingreso="SELECT CASE WHEN reingreso IS NOT NULL THEN reingreso ELSE 0 END AS reingreso FROM ingresos WHERE id=? ";
	
	/**
	 * Implementación de cargar persona básica para Genérica.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargar(Connection, String, String)
	 */
	public static void cargar(Connection con, String tipoId, String numeroId, PersonaBasica personaBasica)
	{
		PreparedStatementDecorator cargarPersonaBasicaStatement = null;
		ResultSetDecorator rsd = null;
		try {
			Log4JManager.info("consulta persona:"+cargarPersonaBasicaStr);
			cargarPersonaBasicaStatement = 
				new PreparedStatementDecorator(con.prepareStatement(cargarPersonaBasicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarPersonaBasicaStatement.setString(1, tipoId);
			cargarPersonaBasicaStatement.setString(2, numeroId);
			rsd = new ResultSetDecorator(cargarPersonaBasicaStatement.executeQuery());
			if(rsd.next()) {
				llenarEnCarga(rsd, personaBasica);
				if (!UtilidadTexto.isEmpty(personaBasica.getFechaNacimiento())) {
					String fechaSistema = UtilidadFecha.getFechaActual(con);
					String fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(personaBasica.getFechaNacimiento());
					personaBasica.setEdadMeses(UtilidadFecha.numeroMesesEntreFechas(fechaNacimiento, fechaSistema,false));
					personaBasica.setEdadDias(UtilidadFecha.numeroDiasEntreFechas(fechaNacimiento, fechaSistema));
				}
			}
		} catch (Exception e) {
			Log4JManager.error("Error cargando PersonaBasica: " + e);
		} finally {
			try {
				if (cargarPersonaBasicaStatement != null) {
					cargarPersonaBasicaStatement.close();
				}
				if(rsd != null) {
					rsd.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
	}
  
	/**
	 * Implementación de cargar persona básica para Genérica.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargar(Connection, int)
	 */
	public static boolean cargar(Connection con, int codigoPersona, PersonaBasica personaBasica)
	{
		PreparedStatement cargarPersonaBasicaStatement = null;
		ResultSet rsd = null;
		try {
			logger.info("cargar Persona-->"+cargarPersonaBasicaDadoCodigoStr+" >>> "+ codigoPersona);
			cargarPersonaBasicaStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarPersonaBasicaDadoCodigoStr));
			cargarPersonaBasicaStatement.setInt(1, codigoPersona);	
			rsd = cargarPersonaBasicaStatement.executeQuery();
			if(rsd.next()) {
				llenarEnCarga(rsd, personaBasica);
				if(!UtilidadTexto.isEmpty(personaBasica.getFechaNacimiento())) {
					String fechaSistema = UtilidadFecha.getFechaActual(con);
					String fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(personaBasica.getFechaNacimiento());
					personaBasica.setEdadMeses(UtilidadFecha.numeroMesesEntreFechas(fechaNacimiento, fechaSistema,false));
					personaBasica.setEdadDias(UtilidadFecha.numeroDiasEntreFechas(fechaNacimiento, fechaSistema));
					return true;
				}
			}
			//return new ResultSetDecorator(cargarPersonaBasicaStatement.executeQuery());
		} catch (Exception e) {
			Log4JManager.error("Error cargando PersonaBasica: " + e);
		} finally {
			
			UtilidadBD.cerrarObjetosPersistencia(cargarPersonaBasicaStatement, rsd, null);
		}
		return false;
	}
	
	
	/**
	 * Metodo que retorna un resultset con la empresa y la razon socila vinculados
	 * a un paciente 
	 * @param con
	 * @param codigoPersona
	 * @param razonSocila
	 * @return ResultSet
	 * @throws SQLException
	 */
	public static void cargarEmpresaRazon(Connection con,int codigoPersona,String razonSocila,String consulta, PersonaBasica personaBasica) throws SQLException
	{
		PreparedStatement ps = null;
		ResultSet rsd = null;
		try {
			ps =con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigoPersona);
			ps.setString(2,razonSocila);

			rsd = ps.executeQuery();

			if (rsd.next()) {
				personaBasica.setNombreEmpresa(rsd.getString("empresa"));
				personaBasica.setNombreTipoRegimen(rsd.getString("regimen"));
			}
		} catch (Exception e) {
			Log4JManager.error("Error cargando EmpresaRazon : " + e);
		} finally {
			try {
				if(rsd != null) {
					rsd.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
	}
	/**
	* Implementación de cargar persona básica para Genérica.
	* @see com.princetonsa.dao.PersonaBasicaDao#cargarPaciente2 (Connection , String , String , String, String ) throws SQLException
	*/
	@SuppressWarnings("rawtypes")
	public static Hashtable cargarPaciente2(Connection con, String tipoId, String numeroId, String codigoInstitucion,String codigoCentroAtencion, String cargarPacienteConsultaResponsableStr,boolean validarCentroAtencion) throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		Hashtable result=null;
		try{
		String cadena=cargarPacienteIngresoStr;
		if(validarCentroAtencion)
			cadena=cadena+"and cc.centro_atencion =  "+codigoCentroAtencion;
		cadena=cadena+ " order by c.fecha_apertura DESC,c.hora_apertura DESC";
			
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, tipoId);
			pst.setString(2, numeroId);
			pst.setInt(3, Utilidades.convertirAEntero(codigoInstitucion));
			rs=pst.executeQuery();
			result= cargarPaciente2(con, rs, true, cargarPacienteConsultaResponsableStr );
		} catch (Exception e) {
			Log4JManager.error("Error cargarPaciente2 : " + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
		return result;
	}
	/**
	* Implementación de cargar persona básica con institución para 
	* Genérica.
	* @see com.princetonsa.dao.PersonaBasicaDao#cargarPaciente2 (Connection , int , String, String ) throws SQLException
	*/
	@SuppressWarnings("rawtypes")
	public static Hashtable cargarPaciente2(
		Connection	ac_con,
		int			ai_codigoPersona,
		String		as_codigoInstitucion,
		String codigoCentroAtencion, String cargarPacienteConsultaResponsableStr,boolean validarCentroAtencion
	)throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		Hashtable result=null;
		try{
		String cadena=is_cargarPacienteIngreso;
		if(validarCentroAtencion)
			cadena=cadena+" and cc.centro_atencion =  "+codigoCentroAtencion;
		cadena=cadena+ " order by c.fecha_apertura DESC,c.hora_apertura DESC";
		
			pst= ac_con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, ai_codigoPersona);
			pst.setString(2, as_codigoInstitucion);
			rs=pst.executeQuery();
			result= cargarPaciente2(ac_con, rs,true, cargarPacienteConsultaResponsableStr);
		} catch (Exception e) {
			Log4JManager.error("Error cargarPaciente2 : " + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
		return result;
	}

	/**
	* Implementación de cargar persona básica con ResultSetDecorator  y
	* resultados en Tabla de Hash para Genérica.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargarPaciente2(Connection , ResultSetDecorator )throws SQLException
	*/
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Hashtable cargarPaciente2(Connection con, ResultSet rsGeneral, boolean validarEstadoCuenta, String cargarPacienteConsultaResponsableStr)throws SQLException
	{
		Hashtable resultados= new Hashtable();
		PreparedStatement pst1=null;
		ResultSet rs1=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		PreparedStatement pst3=null;
		ResultSet rs3=null;
		PreparedStatement pst4=null;
		ResultSet rs4=null;
		/*
		* 0					codigoTipoRegimen
		* 1 				convenioPersonaResponsable
		* 2					codigoIngreso
		* 3					codigoCuenta
		* 4					codigoAdmision
		* 5					anioAdmision
		* 6					ultimaViaIngreso
		* 7					codigoUltimaViaIngreso
		*/
		try{
			
		//Variables necesarias en algún punto
		//Consulta General
		String codigoCuenta="", codigoIngreso="", consecutivoIngreso="",codigoTipoRegimen="",nombreTipoRegimen= "",  convenio="",   viaIngreso   ="", codigoViaIngreso="",fechaIngreso="",estadoCuenta = "", tipoIdResponsable= "", idResponsable = "",pacEntidadSubcontratada = "";		
		//Responsable Cuenta
		String responsableCuenta="";
		

		//****************************************************************************************************
		if(rsGeneral.next())
		{			
			codigoIngreso=rsGeneral.getInt("codigoIngreso")+"";
			estadoCuenta=rsGeneral.getString("estadoCuenta");
			consecutivoIngreso = rsGeneral.getString("consecutivoIngreso");			
			pacEntidadSubcontratada = rsGeneral.getString("pacEntidadSubcontratada")==null?"":rsGeneral.getString("pacEntidadSubcontratada");					
			resultados.put("codigoIngreso",codigoIngreso);
			resultados.put("consecutivoIngreso",consecutivoIngreso);
		}
		else
		{
			//Devolvemos los datos en vacio
			resultados.put("codigoTipoRegimen", "");			
			resultados.put("nombreTipoRegimen", "");
			resultados.put("convenioPersonaResponsable", "");
			resultados.put("codigoCuenta","");
			resultados.put("consecutivoIngreso","");
			resultados.put("codigoAdmision", "");
			resultados.put("anioAdmision", "");
			resultados.put("ultimaViaIngreso", "");
			resultados.put("codigoUltimaViaIngreso", "");
			resultados.put("cuentasPaciente","");

			//Si no hay ingreso menos va a haber cuenta
			return resultados;
		}
		//******************************************************************************************************

		
		//******************************************************************************************************
		//Ahora revisamos el asocio de cuenta	
				
			pst1 = con.prepareStatement(getInformacionAsocioCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst1.setInt(1, Utilidades.convertirAEntero(codigoIngreso));		
			rs1 = pst1.executeQuery();	
		
			if (rs1.next())
		{						
			resultados.put("esCuentaAsociada", "true");
				String cuentaOriginal = rs1.getString("cuentaInicial");
			resultados.put("codigoCuentaAsociada", cuentaOriginal);
		}
		else
		{
			resultados.put("codigoCuentaAsociada", "");
			resultados.put("esCuentaAsociada", "false");
		}
		//********************************************************************************************************
		
		//********************************************************************************************************
		//Carga la información de las cuentas del paciente
		resultados.put("cuentasPaciente", cargarCuentasPaciente(con,codigoIngreso));		
		//********************************************************************************************************
		
		
		//logger.info("Se consulta la ifnromacion del paciente=> "+cargarPacienteConsultaGeneralStr+"\n\ningreso=>"+codigoIngreso);
		String cargarPacienteConsultaGeneral = cargarPacienteConsultaGeneralStr;
		if(validarEstadoCuenta)
		{
			cargarPacienteConsultaGeneral += " AND (cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaActiva+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" ) ";
		}
		else
		{
			cargarPacienteConsultaGeneral += " AND (cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaActiva+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" OR " +
			"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+") ";
		}
		cargarPacienteConsultaGeneral += " ORDER BY cue.fecha_apertura DESC,cue.hora_apertura DESC";
		
		//logger.info("cargarPacienteConsultaGeneral-->"+cargarPacienteConsultaGeneral+"--->"+codigoIngreso);
			pst2= con.prepareStatement(cargarPacienteConsultaGeneral,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst2.setInt(1, Utilidades.convertirAEntero(codigoIngreso));

			rs2=pst2.executeQuery();
			if (rs2.next())
		{			
				//Si llego a este punto es porque la consulta general dio un resultado
				//no tenemos en cuenta si tiene más, porque entre otras, NO puede haber
				//más de un ingreso/cuenta abierta por paciente. El orden en que nos piden
				//entregar los resultados es el siguiente:
	
				/*
				*
						* 0					codigoTipoRegimen
						* 1 				convenioPersonaResponsable
						* 2					codigoIngreso
						* 3					codigoCuenta
						* 4					codigoAdmision
						* 5					anioAdmision
						* 6					ultimaViaIngreso
						* 7					codigoUltimaViaIngreso
						* 8					Tipo Evento
				*/		
			
					tipoIdResponsable = rs2.getString("idResponsable");
				resultados.put("idResponsable",tipoIdResponsable==null?"":tipoIdResponsable);
				
					idResponsable = rs2.getString("tipoIdResponsable");
				resultados.put("tipoIdResponsable",idResponsable==null?"":idResponsable);
				
				resultados.put("pacEntidadSubcontratada",pacEntidadSubcontratada);
				
					codigoTipoRegimen=rs2.getString("codigoTipoRegimen");
				resultados.put("codigoTipoRegimen", codigoTipoRegimen);
				
					nombreTipoRegimen=rs2.getString("nombreTipoRegimen");
				resultados.put("nombreTipoRegimen", nombreTipoRegimen);
	
					codigoCuenta=rs2.getString("codigoCuenta");
					int codigoCuentaInt=rs2.getInt("codigoCuenta");
				resultados.put("codigoCuenta", codigoCuenta);
				
				//Si el estado de la cuenta es asociada entonces no se carga la información
				//de la cuenta asociada pues en la consulta se verifica que si el estado de 
				//la cuenta es asociada el asocio este incompleto  
					if(rs2.getInt("estadoCuenta")==ConstantesBD.codigoEstadoCuentaAsociada)
				{
					resultados.put("codigoCuentaAsociada", "");	
					
				}
				
				/**arreglo de la parte de facturacion, cuando se tiene un asocio con distribucion independiente
				//y se facturan todas las subcuentas de Hospitalizacion entonces al darle el observable se carga la
				//informacion de la cuenta de urgencias cargando el mismo codigo de la cuenta en el codigo cuenta asocio,
				if(resultados.get("codigoCuentaAsociada").toString().equals(codigoCuenta+""))
				{
					resultados.put("codigoCuentaAsociada", cuentaFinal);
				}**/
									
				
				//logger.info("\n\n cuenta-->"+codigoCuenta+" asociada-->"+resultados.get("codigoCuentaAsociada"));
				
					viaIngreso=rs2.getString("viaIngreso");
				resultados.put("viaIngreso", viaIngreso);
	
					codigoViaIngreso=rs2.getString("codigoViaIngreso");
				resultados.put("codigoViaIngreso", codigoViaIngreso);
	
				//rsGeneral.getString("codigoEmpresa");
					convenio=rs2.getString("convenio");
				
					fechaIngreso = rs2.getString("fechaIngreso");
				resultados.put("fechaIngreso",fechaIngreso);
				
					resultados.put("horaIngreso",rs2.getString("horaIngreso"));
				
					resultados.put("tipoEvento",rs2.getString("tipo_evento")==null?"":rs2.getString("tipo_evento"));
				
					resultados.put("hospitalDia",rs2.getString("hospitalDia"));
				
					resultados.put("codigoTipoPaciente",rs2.getString("codigoTipoPaciente"));
					resultados.put("nombreTipoPaciente",rs2.getString("nombreTipoPaciente"));
	////////////////////////////////////////////////////////////////////////////////////////
	//adicionado por anexo 655 transplante
					resultados.put("transplante",rs2.getString("transplante")==null?"":rs2.getString("transplante"));
				
	///////////////////////////////////////////////////////////////////////////////////////
				//Llegados a este punto revisamos si la empresa es particular (codigo 1)
				
				
				/**
				 * Varificar Cambio de Flujo del metodo actual  
				 * */
				//codigo quemado estaba cargado como particular los convenios relacionados a la empresa con codigo 1
				//la comparacion debe hacerse con el codigoTipoRegimen;
				//if (Integer.parseInt(codigoEmpresa)==1)
				if(codigoTipoRegimen.equals(ConstantesBD.codigoTipoRegimenParticular+""))
				{
					//Si entramos acá , es porque debemos sacar el nombre del responsable de la cuenta
					//Si este no existe, ponemos "No seleccionado"
						pst3= con.prepareStatement(cargarPacienteConsultaResponsableStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst3.setInt(1,Utilidades.convertirAEntero(codigoIngreso));
						rs3=pst3.executeQuery();
					
						if (rs3.next())
					{								
							if(!rs3.getString(1).equals(""))								
								responsableCuenta=rs3.getString(1);
						else
							responsableCuenta="No seleccionado";
					}
					else
						responsableCuenta="No seleccionado";
						
					
					resultados.put("convenioPersonaResponsable", responsableCuenta);
				}
				else
				{
					//Si la empresa NO es particular, entonces el responsable es el convenio
					resultados.put("convenioPersonaResponsable", convenio);
				}
				
				consultarAdmisionPaciente(con,resultados,codigoCuenta);
				
				
				//Por último vamos a cargar el codigo del convenio,
				//el código del contrato y el código del tarifario actual
				
					pst4= con.prepareStatement(getCodigoTarifarioActualStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst4.setInt(1, codigoCuentaInt);
				int codigoTarifarioOficial=0, codigoConvenio=0, codigoContrato=0; 
					rs4=pst4.executeQuery();
					if (rs4.next())
				{
						codigoTarifarioOficial=rs4.getInt("codigoTarifarioOficial");
						codigoConvenio=rs4.getInt("codigoConvenio");
						codigoContrato=rs4.getInt("codigoContrato");
	
					if (codigoTarifarioOficial>0)
					{
						resultados.put("codigoTarifarioOficial", ""+codigoTarifarioOficial);
					}
					else
					{
						resultados.put("codigoTarifarioOficial", "");
					}
					if (codigoConvenio>0)
					{
						resultados.put("codigoConvenio", "" + codigoConvenio);
					}
					else
					{
						resultados.put("codigoConvenio", "");
					}
	
					if (codigoContrato>0)
					{
						resultados.put("codigoContrato", "" + codigoContrato);
					}
					else
					{
						resultados.put("codigoContrato", "");
					}
				}
		}
		else
		{
		    //Devolvemos los datos en vacio
			resultados.put("codigoTipoRegimen", "");
			resultados.put("nombreTipoRegimen", "");
			resultados.put("convenioPersonaResponsable", "");
			resultados.put("codigoCuenta","");
			resultados.put("codigoAdmision", "");
			resultados.put("anioAdmision", "");
			resultados.put("ultimaViaIngreso", "");
			resultados.put("codigoUltimaViaIngreso", "");
			resultados.put("pacEntidadSubcontratada","");
			
			resultados.put("tipoEvento","");
			resultados.put("nombreTipoEvento","");			
			resultados.put("codigoTarifarioOficial", "");
			resultados.put("codigoConvenio", "");
			resultados.put("codigoContrato", "");
			resultados.put("enProcesoFact", "0");
			resultados.put("hospitalDia", ConstantesBD.acronimoNo);
			resultados.put("codigoTipoPaciente", "");
			resultados.put("nombreTipoPaciente", "");
			//resultados.put("cuentasPaciente","");
			///////////////////////////////////////////////////////
			//adicionado por anexo 655 transplante
			resultados.put("transplante","");
			///////////////////////////////////////////////////////
		}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR cargarPaciente2", e);
		}
		finally{
			try{
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
				if(rs1 != null){
					rs1.close();
				}
				if(pst1 != null){
					pst1.close();
				}
			}
			catch (SQLException sqle) {
				Log4JManager.error("ERROR cargarPaciente2", sqle);
			}
		}
		//logger.info("impresion del mapa resultados=>"+resultados);
		return resultados;
	}

	/**
	 * Método que realiza la consulta de la admision de un paciente
	 * @param con
	 * @param resultados
	 * @param codigoCuenta
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void consultarAdmisionPaciente(Connection con, Hashtable resultados, String codigoCuenta) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			//Ahora debemos sacamos los datos de la admision abierta (Si existe)
			pst= con.prepareStatement(cargarPacienteConsultaAdmisionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, codigoCuenta);
			pst.setString(2, codigoCuenta);
			rs=pst.executeQuery();

			if (rs.next())
			{
				//Entramos acá solo si hay una admisión, bien sea hospitalaria o de urgencias
				//y dejamos los datos que correspondan (año 0= Admision Hospitalaria)
				int codigoAdmision =rs.getInt("codigoAdmision");
				int anioAdmision =rs.getInt("anioAdmision");

				if (anioAdmision==0)
				{
					//Es una admision hospitalaria, solo se necesita el codigo
					resultados.put("codigoAdmision", codigoAdmision+"");
				}
				else
				{
					resultados.put("codigoAdmision", codigoAdmision+"");
					resultados.put("anioAdmision", anioAdmision+"");
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR consultarAdmisionPaciente", e);
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
			catch (SQLException sqle) {
				Log4JManager.error("ERROR consultarAdmisionPaciente", sqle);
			}
		}
		
	}

	/**
	* Implementación de carga el último contrato y su tarifario oficial en 
	* una BD Genérica.
	* 
	* @see com.princetonsa.dao.PersonaBasicaDao#getCodigoUltimoContrato (Connection , int ) throws SQLException
	*/
	public static int getCodigoUltimoContrato (Connection con, int codigoConvenio) throws SQLException
	{
		int resultados;
		PreparedStatementDecorator getCodigoTarifarioActualStatement= new PreparedStatementDecorator(con.prepareStatement(getCodigoUltimoContratoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		getCodigoTarifarioActualStatement.setInt(1, codigoConvenio);
		ResultSetDecorator rs=new ResultSetDecorator(getCodigoTarifarioActualStatement.executeQuery());
		if (rs.next())
		{
			//Solo nos interesa el primer resultado
			resultados=rs.getInt("codigoContrato");

			rs.close();
			
			return resultados;
		}
		else
		{
			//El paciente no tiene ningún contrato
			rs.close();
			resultados=0;

			rs.close();
			return resultados;
		}
	}

	/**
	 * Método para verificar si el paciente tiene o no manejo conjunto
	 * @param con
	 * @param codigoPersona
	 * @return true si el paciente tiene manejo conjunto
	 */
	public static boolean tieneManejoConjunto(Connection con, int codigoPersona)
	{
		PreparedStatementDecorator manejo;
		try
		{
			manejo =  new PreparedStatementDecorator(con.prepareStatement(tieneManejoConjuntoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			manejo.setInt(1,codigoPersona);
			ResultSetDecorator resultadoManejo=new ResultSetDecorator(manejo.executeQuery());
			if(resultadoManejo.next())
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
			logger.error("Error en la consulta de manejo conjunto");
			return false;
		}
	}
	
	/**
	 * Implementación del método que dado el código de una
	 * cuenta devuelve la fecha y hora de apertura de la misma,
	 * si esta existe
	 * @param TIPO_BD 
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getFechaHoraApertura (Connection , int ) throws SQLException
	 */
	public static String getFechaHoraApertura (Connection con, int idCuenta, int TIPO_BD) throws SQLException
	{
		String getFechaHoraAperturaStr="";
	    switch (TIPO_BD) {
		case DaoFactory.ORACLE:
			getFechaHoraAperturaStr="SELECT to_char(fecha_apertura, 'YYYY-MM-DD') as fechaApertura, hora_apertura as horaApertura from cuentas where id = ?";
			break;
		case DaoFactory.POSTGRESQL:
			getFechaHoraAperturaStr="SELECT to_char(fecha_apertura, 'YYYY-MM-DD') as fechaApertura, to_char(hora_apertura,'HH:MM') as horaApertura from cuentas where id = ?";
				break;

		default:
			break;
		}
		
	    
	    PreparedStatementDecorator getFechaHoraAperturaStatement= new PreparedStatementDecorator(con.prepareStatement(getFechaHoraAperturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    getFechaHoraAperturaStatement.setInt(1, idCuenta);
	      ResultSetDecorator rs=new ResultSetDecorator(getFechaHoraAperturaStatement.executeQuery());
	    if (rs.next())
	    {
	        String respuesta=rs.getString("fechaApertura");
	        respuesta+="   "+rs.getString("horaApertura");
	        rs.close();
	        return respuesta;
	    }
	    else
	    {
	        throw new SQLException ("La cuenta Especificada no tiene apertura");
	    }
	}
	
	
	
	//***********************************************************************************
	//********************************Metodos de la funcionalidad Cargar Otros Ingresos
	
	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, valida que las cuentas al ingreso sean Cuenta valida
	 *  
	 * @param int codigoPaciente 
	 * */
	@SuppressWarnings("rawtypes")
	public static ArrayList cargarOtrosIngresosPaciente(Connection con, 
														int codigoPaciente)
	{
		
		ArrayList<DtoOtrosIngresosPaciente> arregloAux = new ArrayList<DtoOtrosIngresosPaciente> ();		
		DtoOtrosIngresosPaciente dto;		
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(CadenaConsultaOtrosIngresosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPaciente);			
			
			logger.info("valor sql >>> "+CadenaConsultaOtrosIngresosStr.replace("?", codigoPaciente+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		
			
			//se almacena el resultado en el Dto
			while(rs.next())
			{				
				dto = new DtoOtrosIngresosPaciente();
				dto.reset();
				
				//logger.info("valor cargar otros ingresos >>"+rs.getString("descripcion_catencion"));
								
				dto.setCentroAtencion(rs.getString("consecutivo_catencion"));
				dto.setDescripcionCentroAtencion(rs.getString("descripcion_catencion"));
				dto.setIngreso(rs.getString("ingreso"));
				dto.setPreingreso(rs.getString("preingreso"));
				dto.setReingreso(rs.getInt("reingreso"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setIngresoConsecutivo(rs.getString("ingreso_consecutivo"));
				dto.setAnioConsecutivo(rs.getString("anio_consecutivo"));
				dto.setEstadoIngreso(rs.getString("estado_ingreso"));
				dto.setNombreEstadoingreso(ValoresPorDefecto.getIntegridadDominio(rs.getString("estado_ingreso")).toString());
				dto.setFechaAperturaIngreso(rs.getString("fecha_ingreso"));
				dto.setFechaCierreIngreso(rs.getString("fecha_egreso"));
				dto.setNumeroCuenta(rs.getString("numero_cuenta"));
				dto.setEstadoCuenta(rs.getString("estado_cuenta"));
				dto.setNombreEstadoCuenta(rs.getString("nombre_estado_cuenta"));
				dto.setViaIngresoCuenta(rs.getString("via_ingreso"));
				dto.setNombreViaIngreso(rs.getString("nombre_via_ingreso"));				
				dto.setCodigoPaciente(rs.getString("codigo_paciente"));
				
				arregloAux.add(dto);				
			}
			rs.close();
			ps.close();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return arregloAux;
	}
	
	
	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, no valida estados de cuenta
	 *
	 * @param con Conexion con la BD
	 * @param int codigoPaciente 
	 * @return Retorna ArrayList<DtoOtrosIngresosPaciente> con el listado de los ingresos
	 */
	public static ArrayList<DtoOtrosIngresosPaciente> consultaTodosIngresos(Connection con, int codigoPaciente)
	{
		ArrayList<DtoOtrosIngresosPaciente> arregloAux = new ArrayList<DtoOtrosIngresosPaciente> ();		
		DtoOtrosIngresosPaciente dto;		
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaTodosIngresosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoPaciente);			
			
			logger.info("valor sql >>> "+consultaTodosIngresosStr.replace("?", codigoPaciente+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			//se almacena el resultado en el Dto
			while(rs.next())
			{				
				dto = new DtoOtrosIngresosPaciente();
				dto.reset();
				
				//logger.info("valor cargar otros ingresos >>"+rs.getString("descripcion_catencion"));
								
				dto.setCentroAtencion(rs.getString("consecutivo_catencion"));
				dto.setDescripcionCentroAtencion(rs.getString("descripcion_catencion"));
				dto.setIngreso(rs.getString("ingreso"));
				dto.setPreingreso(rs.getString("preingreso"));
				dto.setReingreso(rs.getInt("reingreso"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setIngresoConsecutivo(rs.getString("ingreso_consecutivo"));
				dto.setAnioConsecutivo(rs.getString("anio_consecutivo"));
				dto.setEstadoIngreso(rs.getString("estado_ingreso"));
				dto.setNombreEstadoingreso(ValoresPorDefecto.getIntegridadDominio(rs.getString("estado_ingreso")).toString());
				dto.setFechaAperturaIngreso(rs.getString("fecha_ingreso"));
				dto.setFechaCierreIngreso(rs.getString("fecha_egreso"));
				dto.setNumeroCuenta(rs.getString("numero_cuenta"));
				dto.setEstadoCuenta(rs.getString("estado_cuenta"));
				dto.setNombreEstadoCuenta(rs.getString("nombre_estado_cuenta"));
				dto.setViaIngresoCuenta(rs.getString("via_ingreso"));
				dto.setNombreViaIngreso(rs.getString("nombre_via_ingreso"));				
				dto.setCodigoPaciente(rs.getString("codigo_paciente"));
				
				arregloAux.add(dto);				
			}			
			rs.close();
			ps.close();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return arregloAux;
	}

	/**
	* Carga la informacion del Ingreso dado por la busqueda de Otros Ingresos
	* @param Connection con
	* @param String ingreso 
	*/
	@SuppressWarnings("rawtypes")
	public static Hashtable cargarPacienteXingreso(Connection con, String ingreso, String cargarPacienteConsultaResponsableStr) 
	{	
		PreparedStatement cargarPacienteIngresoStatement = null;
		ResultSet rs = null;
		Hashtable result= null;
		try
		{
			cargarPacienteIngresoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarPacienteOtrosIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarPacienteIngresoStatement.setString(1,ingreso);
			rs = cargarPacienteIngresoStatement.executeQuery();
			result = cargarPaciente2(con, rs, false, cargarPacienteConsultaResponsableStr);
		} catch(SQLException e) {
			Log4JManager.error("ERROR cargarPacienteXingreso",e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (cargarPacienteIngresoStatement != null) {
					cargarPacienteIngresoStatement.close();
				}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
		return result;
	}	
	//***********************************************************************************
	//********************************Fin Metodos de la funcionalidad Cargar Otros Ingresos	
	
	/**
	 * Consulta la información de las cuentas del paciente
	 * @param Connection con
	 * @param String ingresos
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList cargarCuentasPaciente(Connection con, String ingreso)
	{
		ArrayList array = new ArrayList();
		CuentasPaciente cuenta;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(getCuentasPacieteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Utilidades.convertirAEntero(ingreso));
			
			rs = pst.executeQuery();
			
			while(rs.next())
			{
				cuenta = new CuentasPaciente();				
				cuenta.setCodigoViaIngreso(rs.getString("viaIngreso"));
				cuenta.setDescripcionViaIngreso(rs.getString("descripcionViaIngreso"));
				cuenta.setCodigoTipoPaciente(rs.getString("tipoPaciente"));
				cuenta.setDescripcionTipoPaciente(rs.getString("descripcionTipoPaciente"));
				cuenta.setCodigoCuenta(rs.getString("cuenta"));
				cuenta.setEstadoCuenta(rs.getString("estadoCuenta"));
				cuenta.setDescripcionEstadoCuenta(rs.getString("descripcionEstadoCuenta"));
				cuenta.setCodigoIngreso(rs.getInt("idIngreso")+"");
				
				array.add(cuenta);
			}			
		} catch(SQLException e) {
			Log4JManager.error("ERROR cargarCuentasPaciente",e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if(pst != null) {
					pst.close();
		}
			} catch (Exception e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResultSetDecorator :" + e);
			}
		}
		
		return array;
	}
	
	/**
	 * Metodo que consulta si un Igreso esta marcado como Preingreso Pendiente
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean consultarPreingresoP(int codigoIngreso)
	{
		//logger.info("CODIGO DEL INGRESO DESDE PERSONA BASICA>>>>>>>>>>>"+codigoIngreso);
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
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaPreingresoP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				tipo = rs.getString("preingreso")==null?"":rs.getString("preingreso");
			}
			//logger.info("TIPO DE PREINGRESO>>>>>>>>>>>"+tipo);
			if(tipo.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
			{
				UtilidadBD.closeConnection(con);
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta del ingreso marcado como Preingreso Pendiente");
		}
		UtilidadBD.closeConnection(con);
		return false;
	}
	
	/**
	 * Metodo para consultar si un Ingreso es un Reingreso
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean consultarReingreso(int codigoIngreso)
	{
		int reingreso=0;
		Connection con = null;
		ResultSetDecorator rs = null;
		PreparedStatementDecorator ps = null;
        try {
            con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
            if (con != null) {
            	ps = new PreparedStatementDecorator(con.prepareStatement(consultaReingreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    			ps.setInt(1, codigoIngreso);
    			
    			rs = new ResultSetDecorator(ps.executeQuery());
    			if(rs.next()){
    				reingreso = rs.getInt("reingreso");
    			}
    			if(reingreso > 0) {
    				return true;
    			}
			}
        } catch(SQLException e) {
            logger.error(e+" Error en la consulta del ingreso marcado como Reingreso");
        } finally {
        	try {
				ps.close();
				rs.close();
	        	UtilidadBD.closeConnection(con);
			} catch (SQLException e) {
				logger.error("Error cerrando conexiones " + e);
				e.printStackTrace();
			}
        }
		return false;
	}
	
	
	

	/**
	 * Implementación de cargar persona básica para Genérica.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargar(Connection, int)
	 */
	public static void cargarObjeto(Connection con, PersonaBasica personaBasica) throws SQLException
	{
		PreparedStatementDecorator cargarPersonaBasicaStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarPersonaBasicaDadoCodigoStr));
		logger.info("cargar Persona-->"+cargarPersonaBasicaDadoCodigoStr+" >>> "+personaBasica.getCodigoPersona());
		cargarPersonaBasicaStatement.setInt(1, personaBasica.getCodigoPersona());
		
		ResultSetDecorator rs = new ResultSetDecorator(cargarPersonaBasicaStatement.executeQuery());
		
		if(rs.next())
		{
			llenarEnCarga(rs, personaBasica);
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			if(!UtilidadTexto.isEmpty(personaBasica.getFechaNacimiento()))
			{
				String fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(personaBasica.getFechaNacimiento());
				personaBasica.setEdadMeses(UtilidadFecha.numeroMesesEntreFechas(fechaNacimiento, fechaSistema,false));
				personaBasica.setEdadDias(UtilidadFecha.numeroDiasEntreFechas(fechaNacimiento, fechaSistema));
			}
			//return true;
		}
		UtilidadBD.cerrarObjetosPersistencia(cargarPersonaBasicaStatement, rs, null);
		
		
		//return new ResultSetDecorator(cargarPersonaBasicaStatement.executeQuery());
	}
	
	
	
	
	private static void llenarEnCarga (ResultSetDecorator rs, PersonaBasica personaBasica ) throws SQLException
	{
		personaBasica.setCodigoPersona(rs.getInt("codigoPersona"));
		personaBasica.setTipoIdentificacionPersona(rs.getString("tipoIdentificacionPersona"));
		personaBasica.setCodigoTipoIdentificacionPersona(rs.getString("codTipoIdentificacionPer"));
		personaBasica.setNumeroIdentificacionPersona(rs.getString("numeroIdentificacionPersona"));
		personaBasica.setNombrePersona(rs.getString("nombrePersona"));
		personaBasica.setApellidosNombresPersona(rs.getString("nombrePersona2"));
		personaBasica.setCodigoSexo(rs.getInt("codigoSexo"));
		personaBasica.setSexo(rs.getString("sexo"));
		personaBasica.setTipoSangre(rs.getString("tipoSangre"));
		
		personaBasica.setPrimerNombre(rs.getString("primer_nombre"));
		personaBasica.setSegundoNombre(rs.getString("segundo_nombre"));
		personaBasica.setPrimerApellido(rs.getString("primer_apellido"));
		personaBasica.setSegundoApellido(rs.getString("segundo_apellido"));
		
		personaBasica.setDireccion(rs.getString("direccion"));
		personaBasica.setTelefono(rs.getString("telefono"));
		personaBasica.setTelefonoFijo(rs.getString("telefonofijo"));
		personaBasica.setTelefonoCelular(rs.getString("telefonocelular"));
		personaBasica.setFechaNacimiento(rs.getString("fechaNacimiento"));
		personaBasica.setEmail(rs.getString("email"));

		if(!UtilidadTexto.isEmpty(personaBasica.getFechaNacimientoOriginal()))
		{
			StringTokenizer fechaTokenizer   =
				new StringTokenizer(personaBasica.getFechaNacimientoOriginal(), "-");
			personaBasica.setAnioNacimiento     ( fechaTokenizer.nextToken());
			personaBasica.setMesNacimiento	    ( fechaTokenizer.nextToken());
			personaBasica.setDiaNacimiento	    ( fechaTokenizer.nextToken());
		}
	
		String fechaMuerte = rs.getString("fecha_muerte");		
		personaBasica.setHistoriaClinica(rs.getString("historia_clinica"));						
		
		if(fechaMuerte==null||fechaMuerte.equals(""))
		{
			personaBasica.setEdad (
				Persona.calcularEdad(
					personaBasica.getDiaNacimiento(),
					personaBasica.getMesNacimiento(),
					personaBasica.getAnioNacimiento()
				));
			personaBasica.setEdadDetallada (
				Persona.calcularEdadDetallada(
						personaBasica.getAnioNacimiento(),
						personaBasica.getMesNacimiento(),
						personaBasica.getDiaNacimiento()
				));
		}
		else
		{
			int diaMuerte = Integer.parseInt(fechaMuerte.split("/")[0]);
			int mesMuerte = Integer.parseInt(fechaMuerte.split("/")[1]);
			int anioMuerte = Integer.parseInt(fechaMuerte.split("/")[2]);
			
			personaBasica.setEdad (
				UtilidadFecha.calcularEdad(
						personaBasica.getDiaNacimiento(),
						personaBasica.getMesNacimiento(),
						personaBasica.getAnioNacimiento(),
						diaMuerte,
						mesMuerte,
						anioMuerte
				));
			personaBasica.setEdadDetallada (
				UtilidadFecha.calcularEdadDetallada(
					Integer.parseInt(personaBasica.getAnioNacimiento()),
					Integer.parseInt(personaBasica.getMesNacimiento()),
					Integer.parseInt(personaBasica.getDiaNacimiento()),
					diaMuerte+"",
					mesMuerte+"",
					anioMuerte+""
				));
		}
		
		personaBasica.setNombreCiudadVivienda ( rs.getString("ciudad"));
		personaBasica.setNombreDeptoVivienda ( rs.getString("depto"));
		personaBasica.setNombrePaisVivienda ( rs.getString("pais"));
		
		personaBasica.setCodigoCiudadVivienda ( rs.getString("codigo_ciudad"));
		personaBasica.setCodigoDeptoVivienda ( rs.getString("codigo_depto"));
		personaBasica.setCodigoPaisVivienda ( rs.getString("codigo_pais"));
		
		personaBasica.setNombrePaisExpedicion ( rs.getString("pais_expedicion"));
		personaBasica.setNombreCiudadExpedicion ( rs.getString("ciudad_expedicion"));
		personaBasica.setNombreDepartamentoExpedicion ( rs.getString("depto_expedicion"));
		
		String barrio = rs.getString("barrio");
		if(barrio!=null && barrio!=""){
			personaBasica.setBarrio(rs.getString("barrio")+"#"+rs.getString("codigo_barrio"));
		}
		
		personaBasica.setCodigoCentroAtencionPYP(rs.getInt("codigo_centro_atencion_pyp"));
		personaBasica.setNombreCentroAtencionPYP(rs.getString("nombre_centro_atencion_pyp"));
		
		personaBasica.setLee_escribe( UtilidadTexto.getBoolean(rs.getString("lee_escribe")) );
		
		
		personaBasica.setEstudio( rs.getInt("estudio") );
		personaBasica.setEtnia( rs.getInt("etnia") );
		
		personaBasica.setCodigoGrupoPoblacional(rs.getString("grupo_poblacional"));
		personaBasica.setNombreGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(personaBasica.getCodigoGrupoPoblacional())+"");
		
		//************************** Antecedentes de Alerta *******************************************
		personaBasica.setAntecedentesAlerta(UtilidadOdontologia.obtenerAntecedentesAlerta(personaBasica.getCodigoPersona()));
		//************************** Antecedentes de Alerta *******************************************
	}

	private static void llenarEnCarga (ResultSet rs, PersonaBasica personaBasica ) throws SQLException
	{
		personaBasica.setCodigoPersona(rs.getInt("codigoPersona"));
		personaBasica.setTipoIdentificacionPersona(rs.getString("tipoIdentificacionPersona"));
		personaBasica.setCodigoTipoIdentificacionPersona(rs.getString("codTipoIdentificacionPer"));
		personaBasica.setNumeroIdentificacionPersona(rs.getString("numeroIdentificacionPersona"));
		personaBasica.setNombrePersona(rs.getString("nombrePersona"));
		personaBasica.setApellidosNombresPersona(rs.getString("nombrePersona2"));
		personaBasica.setCodigoSexo(rs.getInt("codigoSexo"));
		personaBasica.setSexo(rs.getString("sexo"));
		personaBasica.setTipoSangre(rs.getString("tipoSangre"));
		
		personaBasica.setPrimerNombre(rs.getString("primer_nombre"));
		personaBasica.setSegundoNombre(rs.getString("segundo_nombre"));
		personaBasica.setPrimerApellido(rs.getString("primer_apellido"));
		personaBasica.setSegundoApellido(rs.getString("segundo_apellido"));
		
		personaBasica.setDireccion(rs.getString("direccion"));
		personaBasica.setTelefono(rs.getString("telefono"));
		personaBasica.setTelefonoFijo(rs.getString("telefonofijo"));
		personaBasica.setTelefonoCelular(rs.getString("telefonocelular"));
		personaBasica.setFechaNacimiento(rs.getString("fechaNacimiento"));
		personaBasica.setEmail(rs.getString("email"));

		if(!UtilidadTexto.isEmpty(personaBasica.getFechaNacimientoOriginal()))
		{
			StringTokenizer fechaTokenizer   =
				new StringTokenizer(personaBasica.getFechaNacimientoOriginal(), "-");
			personaBasica.setAnioNacimiento     ( fechaTokenizer.nextToken());
			personaBasica.setMesNacimiento	    ( fechaTokenizer.nextToken());
			personaBasica.setDiaNacimiento	    ( fechaTokenizer.nextToken());
		}
	
		String fechaMuerte = rs.getString("fecha_muerte");		
		personaBasica.setHistoriaClinica(rs.getString("historia_clinica"));						
		
		if(fechaMuerte==null||fechaMuerte.equals(""))
		{
			personaBasica.setEdad (
				Persona.calcularEdad(
					personaBasica.getDiaNacimiento(),
					personaBasica.getMesNacimiento(),
					personaBasica.getAnioNacimiento()
				));
			personaBasica.setEdadDetallada (
				Persona.calcularEdadDetallada(
						personaBasica.getAnioNacimiento(),
						personaBasica.getMesNacimiento(),
						personaBasica.getDiaNacimiento()
				));
		}
		else
		{
			int diaMuerte = Integer.parseInt(fechaMuerte.split("/")[0]);
			int mesMuerte = Integer.parseInt(fechaMuerte.split("/")[1]);
			int anioMuerte = Integer.parseInt(fechaMuerte.split("/")[2]);
			
			personaBasica.setEdad (
				UtilidadFecha.calcularEdad(
						personaBasica.getDiaNacimiento(),
						personaBasica.getMesNacimiento(),
						personaBasica.getAnioNacimiento(),
						diaMuerte,
						mesMuerte,
						anioMuerte
				));
			personaBasica.setEdadDetallada (
				UtilidadFecha.calcularEdadDetallada(
					Integer.parseInt(personaBasica.getAnioNacimiento()),
					Integer.parseInt(personaBasica.getMesNacimiento()),
					Integer.parseInt(personaBasica.getDiaNacimiento()),
					diaMuerte+"",
					mesMuerte+"",
					anioMuerte+""
				));
		}
		
		personaBasica.setNombreCiudadVivienda ( rs.getString("ciudad"));
		personaBasica.setNombreDeptoVivienda ( rs.getString("depto"));
		personaBasica.setNombrePaisVivienda ( rs.getString("pais"));
		
		personaBasica.setCodigoCiudadVivienda ( rs.getString("codigo_ciudad"));
		personaBasica.setCodigoDeptoVivienda ( rs.getString("codigo_depto"));
		personaBasica.setCodigoPaisVivienda ( rs.getString("codigo_pais"));
		
		personaBasica.setNombrePaisExpedicion ( rs.getString("pais_expedicion"));
		personaBasica.setNombreCiudadExpedicion ( rs.getString("ciudad_expedicion"));
		personaBasica.setNombreDepartamentoExpedicion ( rs.getString("depto_expedicion"));
		personaBasica.setBarrio(rs.getString("barrio")+"#"+rs.getString("codigo_barrio"));
		
		personaBasica.setCodigoCentroAtencionPYP(rs.getInt("codigo_centro_atencion_pyp"));
		personaBasica.setNombreCentroAtencionPYP(rs.getString("nombre_centro_atencion_pyp"));
		
		personaBasica.setLee_escribe( UtilidadTexto.getBoolean(rs.getString("lee_escribe")) );
		
		
		personaBasica.setEstudio( rs.getInt("estudio") );
		personaBasica.setEtnia( rs.getInt("etnia") );
		
		personaBasica.setCodigoGrupoPoblacional(rs.getString("grupo_poblacional"));
		personaBasica.setNombreGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(personaBasica.getCodigoGrupoPoblacional())+"");
		
		//************************** Antecedentes de Alerta *******************************************
		personaBasica.setAntecedentesAlerta(UtilidadOdontologia.obtenerAntecedentesAlerta(personaBasica.getCodigoPersona()));
		//************************** Antecedentes de Alerta *******************************************
	}

public static String obtenerViaEgreso(Connection con,String codigoPaciente){
		
		try
		{
		String consultaCuenta="select cu.id_ingreso AS ID_INGRESO from cuentas cu where cu.CODIGO_PACIENTE = ? " +
				"order by cu.fecha_modifica,cu.hora_modifica desc";
		String numeroIngreso="";
		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		pst.setInt(1,Integer.valueOf(codigoPaciente));
		
		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
		if(rs.next()){
			numeroIngreso=String.valueOf(rs.getInt("ID_INGRESO"));
		}

		if (numeroIngreso!=null && !numeroIngreso.equals("")) {

			
			String consultaAsocios="SELECT  ac.cuenta_inicial, ac.cuenta_final cuentaFinal FROM asocios_cuenta ac  " +
			"WHERE ac.ingreso = ? ORDER BY ac.fecha,ac.hora desc";
			String cuentaFinal="";
			PreparedStatementDecorator pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaAsocios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst2.setInt(1,Integer.valueOf(numeroIngreso));
			rs = new ResultSetDecorator(pst2.executeQuery());
			if(rs.next()){
				cuentaFinal=String.valueOf(rs.getInt("cuentaFinal"));
			}
			
			if (cuentaFinal.equals("")) {
				return cuentaFinal;
			}else{
				
				String consultaCuentaViaEgreso="select cu.via_ingreso via from cuentas cu where cu.ID = ?";
				String viaEgreso="";
				PreparedStatementDecorator pst3 =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaViaEgreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst3.setInt(1,Integer.valueOf(cuentaFinal));
				rs = new ResultSetDecorator(pst3.executeQuery());
				if(rs.next()){
					viaEgreso=String.valueOf(rs.getInt("via"));
				}
				return viaEgreso;
			}
		}
		
		
		}
		catch(SQLException e)
		{
			logger.error("Error en existeResultadosLaboratorios de existeResultadosLaboratorios: "+e);
		}
		
		return null;
		
	}
	
	
	/**
	 * @param con
	 * @param solciitud
	 * @return lista de especialidades 
	 */
	public static String consultarEspecialidadMedicoXSolicitud(Connection con,Integer solciitud){
		String especialidades="";
		Boolean nombre=true;
		String nombreMedico="";
		try
		{

			String consulta = "SELECT s.numero_solicitud AS numerosolicitud, sc.hora_salida_sala AS horasalpac, " +
			" per.primer_nombre||' '||per.segundo_nombre||' '||per.primer_apellido||' '||per.segundo_apellido||' '||me.numero_registro  as nombre_profesional," +
			"  esp.nombre as nombre_especialidad FROM solicitudes s INNER JOIN cuentas c ON(s.cuenta=c.id) INNER JOIN solicitudes_cirugia sc" +
			" ON(sc.numero_solicitud = s.numero_solicitud AND (sc.ind_qx         = 'CX' OR sc.ind_qx           = 'NCTO')) " +
			"INNER JOIN peticion_qx pq  ON(pq.codigo=sc.codigo_peticion) LEFT OUTER JOIN hoja_quirurgica hq " +
			"ON(hq.numero_solicitud = s.numero_solicitud) LEFT OUTER JOIN (anulacion_sol_cx anulsol INNER JOIN motivos_anul_qx_inst maqi" +
			" ON(anulsol.motivo=maqi.codigo) INNER JOIN motivo_anulacion_pet map ON(map.codigo                 =maqi.motivos_anulacion)) " +
			"ON(anulsol.numero_solicitud   =s.numero_solicitud) LEFT JOIN medicos me ON(me.codigo_medico = s.codigo_medico) " +
			"LEFT JOIN personas per ON (per.codigo = me.codigo_medico) LEFT JOIN especialidades_medicos espmedi " +
			"ON (me.codigo_medico=espmedi.codigo_medico) LEFT JOIN especialidades esp ON (espmedi.codigo_especialidad=esp.codigo) WHERE s.tipo  " +
			"  ='14' AND s.estado_historia_clinica<>4 " +
			"AND s.numero_solicitud =? " +
			"ORDER BY s.fecha_solicitud,  s.hora_solicitud ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.valueOf(solciitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next()){
				if(nombre){
					nombreMedico=rs.getString("nombre_profesional")+" ";
					nombre=false;
				}


				especialidades+=","+rs.getString("nombre_especialidad");
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeResultadosLaboratorios de existeResultadosLaboratorios: "+e);
		}
		return nombreMedico+especialidades;
	}

	/**
	 * @param con
	 * @param codigoPaciente
	 * @param idIngreso
	 * @return fecha y hora de Ingreso 
	 */
	public static String consultarFechaHoraIngreso(Connection con,String codigoPaciente,String idIngreso){
		String res = "";
		
		String consulta = "SELECT i.fecha_ingreso||' '||i.hora_ingreso " +
				" fecha_hora FROM ingresos i " +
				" where i.id = ? and i.codigo_paciente = ?";
		try
		{
		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		pst.setInt(1,Integer.valueOf(idIngreso));
		pst.setInt(2,Integer.valueOf(codigoPaciente));
	
		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
		
		while(rs.next()){
			
			res= rs.getString("fecha_hora");
		}
		
		}
		catch(SQLException e)
		{
			logger.error("Error en existeResultadosLaboratorios de existeResultadosLaboratorios: "+e);
		}
		
		return res;
	}

	
}