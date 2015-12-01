package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Date;

import com.ibm.icu.util.ULocale.Type;
import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInformInconsisenBDForm;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEnvioInformInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoVariablesIncorrectasenBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.pdf.InconsistenciasBDPdf;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.Utilidades;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class SqlBaseRegistroEnvioInformInconsisenBDDao {
 
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseRegistroEnvioInformInconsisenBDDao.class);
	

  /**
   * Cadena Sql que realiza la consulta de los datos de un Paciente	
   */
   private static String consultarInformacionPaciente="SELECT " +
				   		"per.codigo AS codigopaciente, " +
				   		"per.numero_identificacion AS numidentificacion, " +
				   		"per.tipo_identificacion AS tipoidentificacion, " +
				   		"coalesce(to_char(per.fecha_nacimiento,'dd/mm/yyyy'),'') AS fechanacimiento, " +
				   		"per.codigo_departamento_vivienda AS coddepartamento, " +
				   		"per.codigo_ciudad_vivienda AS codmunicipio, " +
				   		"getNombreCiudad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda,per.codigo_ciudad_vivienda) AS nombreciudad, " +
				   		"getNombreDepto(per.codigo_pais_vivienda,per.codigo_departamento_vivienda) AS nombredepartamento, " +
				   		"per.direccion AS direccion, " +
				   		"coalesce(per.telefono_fijo||'','') AS telefono, " +
				   		"per.primer_nombre AS primernombre, " +
				   		"per.segundo_nombre AS segundonombre, " +
				   		"per.primer_apellido AS primerapellido, " +
				   		"per.segundo_apellido AS segundoapellido, " +
				   		"coalesce(subcuent.tipo_cobertura,"+ConstantesBD.codigoNuncaValido+") AS codcobertura, " +
				   		"con.nombre AS nombreconvenio, " +
				   		"con.codigo AS codigoconvenio, " +
				   		"cu.id AS codigocuenta, "+
				   		"subcuent.sub_cuenta AS codigosubcuenta, "+
				   		"getNombreCobertura(subcuent.tipo_cobertura) AS nombrecobertura "+
				   		"FROM personas per " +
				   		"INNER JOIN manejopaciente.ingresos ing ON (ing.codigo_paciente = per.codigo) " +
				   		"LEFT OUTER  JOIN manejopaciente.cuentas cu ON (ing.codigo_paciente = cu.codigo_paciente) " +
				   		"INNER JOIN manejopaciente.sub_cuentas subcuent ON (subcuent.ingreso = ing.id) " +	
				   		"INNER JOIN facturacion.convenios con ON (con.reporte_incon_bd = '"+ConstantesBD.acronimoSi+"' AND con.codigo = subcuent.convenio) " +
				   		"WHERE ing.institucion = ? AND per.codigo = ?  AND subcuent.convenio = ? ";	
	

     /**
	 * Cadena Sql que realiza la consulta de Ingresos asociados a un paciente
	 */
	private static String consultaIngresosAsociadosStr="SELECT DISTINCT " +
			              "ing.id AS numingreso, " +
			              "ing.codigo_paciente AS codigopaciente, " +
			              "getidpaciente(ing.codigo_paciente) AS cedpersona,"+
			              "getnombrepersona(ing.codigo_paciente) AS nombrepersona, "+
			              "ing.consecutivo AS consecutivo, " +
			              "ing.anio_consecutivo AS anioconsecutivo, " +
			              "getnombrecentatenxing(ing.id) AS nombrecentroatencion, " +
			              "coalesce(to_char(ing.fecha_ingreso,'dd/mm/yyyy'),'') AS fechaapertura, " +
						  "coalesce(ing.hora_ingreso,'') AS horaapertura, "+
						  "coalesce(to_char(ci.fecha_cierre,'dd/mm/yyyy'),'') AS fechacierre, " +
						  "coalesce(ci.hora_cierre,'') AS horacierre, "+
						  "cu.id AS numcuenta, " +
						  "cu.estado_cuenta AS estadocuenta, " +
						  "getnombreestadocuenta(cu.estado_cuenta) AS nombreestdocuenta, " +
						  "cu.via_ingreso AS viaingreso, " +
						  "ing.fecha_ingreso AS fechaingreso, " +
						  "ing.fecha_egreso AS fechaegreso, " +
						  "ing.hora_egreso AS horaegreso, " +
						  "getNombreViaIngreso(cu.via_ingreso) AS nombreviaingreso " +
						 // "incon.estado AS estdoinforme, " +
						  //"coalesce(incon.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigoinforme, " +
						  //"coalesce(incon.convenio,"+ConstantesBD.codigoNuncaValido+") AS codigoconvenioinforme, " +
						  //"coalesce(con.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigoconvenio, " +
						  //"coalesce(incon.sub_cuenta, "+ConstantesBD.codigoNuncaValido+" ) AS codresponsable, " +
						  //"getnombreconvenio(incon.convenio) AS nombreresponsable " +
						  "FROM ingresos ing " +
						  "INNER JOIN manejopaciente.cuentas cu ON (cu.id_ingreso = ing.id ) " +
						  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = ing.id) " +
						  "INNER JOIN facturacion.convenios con ON (con.reporte_incon_bd = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
						  "LEFT OUTER JOIN manejopaciente.cierre_ingresos ci ON (ci.id_ingreso = ing.id) " +
						  "LEFT OUTER JOIN manejopaciente.informe_inconsistencias incon ON (incon.ingreso = ing.id ) "+
						  "WHERE ing.institucion = ? AND ing.codigo_paciente = ? " +
						  "ORDER BY ing.fecha_ingreso DESC";
	
	/**
	 * Cadena Sql que realiza la consulta de Convenios Responsables asociados a un Ingreso
	 */
	private static String consultaConveniosSrt="SELECT " +
			              "sc.sub_cuenta AS codresponsable, " +
			              "sc.nro_prioridad AS prioridadsubcuenta, " +
						  "sc.convenio AS codconvenio, "+
						  "getnombreconvenio(sc.convenio) AS nombreconvenio, "+
						  "coalesce(c.reporte_incon_bd,'"+ConstantesBD.acronimoNo+"') AS  reporteinconbd, "+
						  "coalesce(incon.estado,'') AS  estadoinforme, " +
						  "coalesce (incon.codigo_pk, "+ConstantesBD.codigoNuncaValido+") AS codigoinforme, " +
						  "coalesce(incon.convenio,"+ConstantesBD.codigoNuncaValido+") AS codigoconvenioinforme " +
						  "FROM "+
						  "sub_cuentas  sc " +
						  "INNER JOIN convenios c ON (c.codigo = sc.convenio AND c.reporte_incon_bd = '"+ConstantesBD.acronimoSi+"') "+
						  "INNER JOIN manejopaciente.ingresos ing ON (sc.ingreso = ing.id) "+
						  "LEFT OUTER JOIN manejopaciente.informe_inconsistencias incon ON (incon.sub_cuenta = sc.sub_cuenta ) "+
						  "WHERE "+ 
						  "sc.ingreso = ? ";   
	
	
	 
	
	
	/**
	 *Cadena Sql que realiza la consulta de Tipos de Inconsistencias  asociadas a una institucion
	 */
	private static String consultaTiposIncosistenciasStr="SELECT " +
						  "codigo AS codigo, " +
						  "nombre AS descripcion, " +
						  "activo AS activo, " +
						  "institucion AS institucion, " +
						  "filtrovariable AS filtrovariable " +
						  "FROM " +
						  "tipos_inconsistencias " +
						  "WHERE institucion = ? ";
	
	
	/**
	 * Cadena Sql que realiza la consulta del estado de envio de un informe asociado a un ingreso y un convenio responsable (sub_cuenta) 
	 */
	private static String consultaEstadoEnvioInformeStr="SELECT " +
						"codigo_pk AS codigo, " +
						"estado AS estado " +
						"FROM  " +
						"informe_inconsistencias " +
						"WHERE ingreso = ?  AND  sub_cuenta = ? AND estado = '"+ConstantesIntegridadDominio.acronimoEstadoEnviado +"'";
	
	
	/**
	 * Cadena Sql que realiza la consulta del Informe de Inconsistencias asociado a un ingreso 
	 */
	private static String consultaInformeInconsistenciaStr="SELECT " +
						"codigo_pk  AS codigo, " + //1
						"convenio AS codconvenio, " +//2
						"sub_cuenta AS codresponable, " +//3
						"tipo_inconsistencia AS tipoincosist, " +//4
						"nombre_convenio AS  nombreconvenio, " +//5
						"primer_apellido AS primerapellido, " +//6
						"segundo_apellido AS segundoapellido, " +//7
						"primer_nombre AS primernombre, " +//8
						"segundo_nombre AS segundonombre, " +//9
						"tipo_identificacion AS tipoidentificacion, " +//10
						"numero_identificacion AS numidentificacion, " +//11
						"fecha_nacimiento AS fechanacimiento, " +//12
						"direccion_residencia AS direccionresidencia, " +//13
						"telefono AS telefono, " +//14
						"departamento  AS departamento, " +//15
						"codigo_departamento AS codigodepartamento, " +//16
						"municipio AS municipio, " +//17
						"codigo_municipio AS codigomunicipio, " +//18
						"coalesce (cobertura_salud,"+ConstantesBD.codigoNuncaValido+") AS coberturasalud, " +//19
						"observaciones AS observaciones, " +//20
						"consecutivo AS consecutivo, " +//21
						"anio_consecutivo AS anioconsecutivo, " +//22
						"ingreso AS codingreso, " +//23
						"cuenta AS codcuenta, " +//24
						"estado AS estado, " +//25
						"coalesce(to_char(fecha_generacion,'dd/mm/yyyy'),'') AS fechageneracion, " + //26
						"coalesce(hora_generacion,'') AS horageneracion, "+ //27
						"usuario_generacion  AS usuariogeneracion, " +//28
						"coalesce(to_char(fecha_modificacion,'dd/mm/yyyy'),'') AS fechamodificacion, " +//29
						"coalesce(hora_modificacion,'') AS horamodificacion, "+ // 30
						"usuario_modificacion AS usuariomodificacion, " +//31
						"paciente AS codpaciente, " +//32
						"via_ingreso AS codviaingreso, " + //33
						"getNombreViaIngreso(via_ingreso) AS nombreviaingreso, " + //34
						"getNombreInconsistencia(tipo_inconsistencia) AS descripcionInconsistencia, " + //35
						"getNombreCobertura(cobertura_salud) AS nombrecobertura "+
						"FROM " +
						"informe_inconsistencias " +
						"WHERE ingreso = ? " ;
	
	
	
	/**
	 * Cadena Sql que realiza la consulta de los envios realizados a un informe
	 * */
	public static String consultaEnviosInformeStr = "SELECT " +
						"codigo_pk AS codigoenvio, " +
						"coalesce (informe_inconsist,"+ConstantesBD.codigoNuncaValido+") AS numinforme, " +
						"coalesce(to_char(fecha_envio,'dd/mm/yyyy'),'') AS fechaenvio, " +
						"coalesce (hora_envio,'') AS horaenvio, "+
						"coalesce(empresa_envio,"+ConstantesBD.codigoNuncaValido+") AS codentidadenvio, " +
						"medio_envio AS medioenvio, " +
						"coalesce(path_archivo,'') as patharchivo, " +
						"coalesce(convenio_envio,"+ConstantesBD.codigoNuncaValido+") AS convenioenvio, " +
						"CASE WHEN convenio_envio IS NULL THEN '' ELSE getnombreconvenio(convenio_envio) END AS nombreconvenioenvio, " +
						"CASE WHEN empresa_envio  IS NULL THEN '' ELSE e.razon_social END AS nombreentidadenvio, " +
						"coalesce (usuario_envio, '') AS usuarioenvio " +
						"FROM " +
						"envio_info_inconsistencias " +
						"LEFT OUTER JOIN facturacion.empresas e ON (e.codigo = empresa_envio) " +
						"WHERE informe_inconsist = ? ";
	
	
	/**
	 * Cadena Sql para Insertar un Informe de Inconsistencias
	 */
	public static String ingresarInformeInconsistenciasStr= "INSERT INTO informe_inconsistencias " +
						"(codigo_pk, " + //--(1)
						"convenio, " +//1
						"sub_cuenta, " +//2
						"tipo_inconsistencia, " +//3
						"nombre_convenio, " +//4
						"primer_apellido, " +//5
						"segundo_apellido, " +//6
						"primer_nombre, " +//7
						"segundo_nombre, " +//8
						"tipo_identificacion, " +//9
						"numero_identificacion, " +//10
						"fecha_nacimiento, " +//11
						"direccion_residencia, " +//12
						"telefono, " +//13
						"departamento, " +//14
						"codigo_departamento, " +//15
						"municipio, " +//16
						"codigo_municipio, " +//17
						"cobertura_salud, " +//18
						"observaciones, " +//19
						"consecutivo, " +//20
						"anio_consecutivo, " +//21
						"ingreso, " +//22
						"cuenta, " +//23
						"estado, " +//24
						"fecha_generacion, " + //25
						"hora_generacion, " +  //26
						"usuario_generacion, " +//27
						"fecha_modificacion, " + //28
						"hora_modificacion, " +  //29
						"usuario_modificacion, " +//30
						"paciente, " +//31
						"via_ingreso ) " +//32					
						"VALUES  (? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?) " ;
	
	
	/**
	 * Cadena Sql para Modificar un Informe de Inconsistencias en BD especifico
	 */
	public static String modificarInformeInconsistenciasStr= "UPDATE manejopaciente.informe_inconsistencias SET " +
						"convenio = ?, " +//1
						"sub_cuenta = ?, " +//2
						"tipo_inconsistencia = ?, " +//3
						"nombre_convenio = ?, " +//4
						"primer_apellido = ?, " +//5
						"segundo_apellido = ?, " +//6
						"primer_nombre = ?, " +//7
						"segundo_nombre = ?, " +//8
						"tipo_identificacion = ?, " +//9
						"numero_identificacion = ?, " +//10
						"fecha_nacimiento = ?, " +//11
						"direccion_residencia = ?, " +//12
						"telefono = ?, " +//13
						"departamento = ?, " +//14
						"codigo_departamento = ?, " +//15
						"municipio = ?, " +//16
						"codigo_municipio = ?, " +//17
						"cobertura_salud = ?, " +//18
						"observaciones = ?, " +//19
						"ingreso = ?, " +//20
						"cuenta = ?, " +//21
						"estado = ?, " +//22
						"fecha_modificacion = CURRENT_DATE, " + 
						"hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " + 
						"usuario_modificacion = ?, " +//23
						"paciente = ?, " +//24
						"via_ingreso = ? "+ // 25
	                    "WHERE  " +
	                    "codigo_pk = ? "; // 26
	
	/**
	 * Cadena que actualiza el path_archivo de la tabla envio inconsistencia BD
	 */
	private static String actualizarPathArchivoIncoBD = "UPDATE manejopaciente.envio_info_inconsistencias SET " +
			"path_archivo = ? " +
			"WHERE codigo_pk IN (SELECT MAX(codigo_pk) FROM manejopaciente.envio_info_inconsistencias WHERE informe_inconsist = ?) ";
	
	/**
	 * Cadena Sql que consulta las diferentes variables incorrectas del sistema
	 */
	public static String consultarVairiablesIncorrectasStr= "SELECT " +
						"codigo AS codigo, " +
						"nombre AS descripcion, " +
						"activo AS activo, " +
						"institucion AS institucion, " +
						"indicador AS indicador " +
						"FROM variables_incorrectas ";
	
	/**
	 * Cadena Sql que consulta las variables incorrectas asociadas a un Informe de Inconsistencias especifico
	 */
	public static String consultarVariablesIncorrectasInformeIncoStr="SELECT " +
			"infovar.codigo_pk AS codigo, " +
			"infovar.informe_inconsistencias AS codinforme, " +
			"infovar.tipo_variable AS codvariable, " +
			"getNombreVariableIncorrecta(infovar.tipo_variable) AS descripcionvariable, " +
			"varinco.indicador AS indicador, " +
			"infovar.valor AS valorbd " +
			"FROM info_incon_var_incorrecta infovar  " +
			"INNER JOIN variables_incorrectas varinco ON (infovar.tipo_variable = varinco.codigo)" +
			"WHERE informe_inconsistencias = ? ";
	
	
	public static String eliminarVariablesIncorrectasInforme="DELETE FROM info_incon_var_incorrecta " +
						"WHERE informe_inconsistencias = ? ";
	/**
	 * Cadena Sql que consulta los tipos de documentos existentes en el sistema
	 */
	public static String consultarTiposDocumentoStr="SELECT " +
						"acronimo AS acronimo, " +
						"nombre AS nombre, " +
						"cod_interfaz AS codigointerfaz " +
						"FROM tipos_identificacion ";
	
	
	/**
	 * Cadena Sql que consulta los departamentos existentes
	 */
	public static String consultarDepartamentosStr="SELECT " +
						"codigo_departamento AS codigodepartamento, " +
						"descripcion AS nombredepartamento, " +
						"codigo_pais AS codigopais " +
						"FROM departamentos ORDER BY descripcion ASC"; 
			
	
	/**
	 * Cadena Sql que consulta las ciudades existentes
	 */
	public static String consultarCiudadesStr="SELECT " +
						"codigo_departamento AS codigodepartamento, " +
						"codigo_ciudad AS codigociudad, " +
						"descripcion AS nombreciudad, " +
						"codigo_pais AS codigopais " +
						"FROM ciudades WHERE 1=1 ORDER BY descripcion ASC";
	
	
	
	public static String consultaCoberturaSaludPacientesStr="SELECT DISTINCT " +
						"cobtipreg.tipo_regimen AS tiporegimen, " +
						"coalesce (cobtipreg.cobertura,"+ConstantesBD.codigoNuncaValido+") AS codcobertura, " +
						"cobtipreg.activo AS activo, " +
						"cobtipreg.institucion AS institucion, " +
						"getNombreCobertura(cobtipreg.cobertura) AS nombrecobertura " +
						"FROM cober_salud_tipo_reg cobtipreg " +
						"INNER JOIN convenios con ON (con.tipo_regimen = cobtipreg.tipo_regimen) " +
						"WHERE con.codigo = ? ";
	
	
	public static String strConsultaInfomeInconsitencias = "SELECT " +
						"infc.codigo_pk AS codigopk_info_inco, " +
						"infc.consecutivo AS consecutivo, " +
						"infc.estado AS estado, " +
						"to_char(infc.fecha_generacion,'yyyy-mm-dd') AS fecha_generacion, " + 
						"infc.hora_generacion AS hora_generacion, " +
						"con.nombre AS nombre_convenio, " +
						"con.codigo_min_salud AS cod_min_salud_convenio, " +
						"tinc.codigo AS cod_tip_inconsistencia, " +
						"tinc.nombre AS nombre_tip_inconsistencia, " +
						"infc.primer_apellido AS p_apellido, " +
						"infc.segundo_apellido AS s_apellido, " +
						"infc.primer_nombre AS p_nombre, " +
						"infc.segundo_nombre AS s_nombre, " +
						"infc.tipo_identificacion AS tipo_identificacion, " +
						"infc.numero_identificacion AS num_identificacion, " +
						"to_char(infc.fecha_nacimiento,'yyyy-mm-dd') AS fecha_nacimiento, " +
						"infc.direccion_residencia AS dir_residencia, " +
						"infc.telefono AS telefono, " +
						"infc.departamento AS nom_depto, " +
						"infc.codigo_departamento AS cod_depto, " +
						"infc.municipio AS nom_municipio, " +
						"infc.codigo_municipio AS cod_municipio, " +
						"coalesce (cobs.codigo, "+ConstantesBD.codigoNuncaValido+") AS cod_cobertura_salud, " +
						"cobs.nombre AS nom_cobertura_salud, " +
						"coalesce(infc.observaciones,'') AS observaciones, " +
						"getnombreusuario2(infc.usuario_generacion) AS nombre_usua_gene, " +
						"manejopaciente.gettelefonopersona(infc.usuario_generacion) AS telfono_usua_gene, " +
						"manejopaciente.gettelefonocelularpersona(infc.usuario_generacion) AS telfono_celular_usua_gene, " +
						"CASE WHEN usu.cargo IS NULL THEN '' ELSE getnomcargousuario(usu.cargo) END AS nombre_cargo " +
						"FROM informe_inconsistencias infc " +
						"INNER JOIN convenios con ON (con.codigo = infc.convenio) " +
						"INNER JOIN tipos_inconsistencias tinc ON (tinc.codigo = infc.tipo_inconsistencia) " +
						"INNER JOIN coberturas_salud cobs ON (cobs.codigo = infc.cobertura_salud) " +
						"INNER JOIN usuarios usu ON (usu.login = infc.usuario_generacion) " +
						"WHERE infc.codigo_pk = ? ";
	/**
	 * Metodo que realiza la consulta de Ingresos de un Paciente
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarIngresosPaciente (Connection con, int codigoInstitucion, int codigoPaciente)
	{
		String cadena = consultaIngresosAsociadosStr;	
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoInstitucion);
			ps.setInt(2,codigoPaciente);
			
			
			cadena=cadena.replace("ing.institucion = ?", "ing.institucion = "+codigoInstitucion);
			cadena=cadena.replace("ing.codigo_paciente = ?", "ing.codigo_paciente = "+codigoPaciente);
			
			logger.info("\n\nCadena de consulta Ingresos Paciente>> "+cadena);
			logger.info("codigo Paciente: >> "+codigoPaciente + " Codigo Institucion: >> "+codigoInstitucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("error en  consultarIngresosPaciente >>  cadena >> "+cadena+" ");
			logger.info("codigo Paciente: >> "+codigoPaciente + " Codigo Institucion: >> "+codigoInstitucion);
		}
		return null;
	}
	
	
	
	/**
	 * Metodo que realiza la consulta de Convenios Responsables (sub_cuentas) de un paciente dado un Ingreso
	 * @param con
	 * @param codIngreso
	 * @return
	 */
	public static  HashMap consultarConveniosResponsables(Connection con, int codIngreso)
	{ 		
		 String cadena = consultaConveniosSrt;	
			try
			{
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1,codIngreso);
				
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());	
				
				cadena=cadena.replace("ingreso = ?", "ingreso = " +codIngreso);
								
				logger.info("\n\nCadena de consulta Convenios Responsables>> "+cadena);
				logger.info("codigo Ingreso: >> "+codIngreso );
				
				HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
				ps.close();
				return mapaRetorno;
				
			}
			catch (Exception e) {			
				e.printStackTrace();
				logger.info("error en  consultarConveniosPaciente >>  cadena >> "+cadena+" ");
				logger.info("codigo Ingreso: >> "+codIngreso);
			}
		 
		 
		return null;
	}
	
	/**
	 * Metodo que carga la informaicion del Informe de Inconsistencias en BD de un paciente
	 * en caso de no existir carga la informacion basica del paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con,HashMap parametros)
	{
		DtoInformeInconsisenBD informe= new DtoInformeInconsisenBD();
		String datosPaciente=consultarInformacionPaciente;
		String cadenaConsultaInforme=consultaInformeInconsistenciaStr;
		cadenaConsultaInforme = cadenaConsultaInforme + " AND sub_cuenta = ? "; 
		int inst=0, convenio=0, subcuenta=0, codPersona=0, codIngreso=0 , codCuenta=0; 
		HashMap envio=new HashMap();
		
		
		
		if(parametros.containsKey("codinstitucion") && 
				!parametros.get("codinstitucion").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()) > 0)
		            inst= Utilidades.convertirAEntero(parametros.get("codinstitucion").toString());
		
		if(parametros.containsKey("codpaciente") && 
				!parametros.get("codpaciente").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codpaciente").toString()) > 0)
		            codPersona= Utilidades.convertirAEntero(parametros.get("codpaciente").toString());
		
		if(parametros.containsKey("codconvenio") && 
				!parametros.get("codconvenio").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codconvenio").toString()) > 0)
		            convenio= Utilidades.convertirAEntero(parametros.get("codconvenio").toString());
		
		if(parametros.containsKey("codSubcuenta") && 
				!parametros.get("codSubcuenta").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codSubcuenta").toString()) > 0)
		            subcuenta= Utilidades.convertirAEntero(parametros.get("codSubcuenta").toString());
		
		if(parametros.containsKey("codingreso") && 
				!parametros.get("codingreso").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codingreso").toString()) > 0)
		            codIngreso= Utilidades.convertirAEntero(parametros.get("codingreso").toString());
		
		if(parametros.containsKey("cuenta") && 
				!parametros.get("cuenta").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("cuenta").toString()) > 0)
		            codCuenta= Utilidades.convertirAEntero(parametros.get("cuenta").toString());
		
		
		 try{
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaInforme,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			  ps.setInt(1,codIngreso);
			  ps.setInt(2, subcuenta);
			  
			  cadenaConsultaInforme=cadenaConsultaInforme.replace("ingreso = ?", "ingreso = " +codIngreso);
			  cadenaConsultaInforme=cadenaConsultaInforme.replace("sub_cuenta = ?", "sub_cuenta = " +subcuenta);
			  ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());	
			
	           
			  
			  if(rs.next())
				{
				  logger.info("\n\nEXISTE UN INFORME  >> ");
				  		
					informe.setCodigoPk(rs.getInt("codigo"));
					informe.setCodigoConvenio(rs.getInt("codconvenio"));
					informe.setSubcuenta(rs.getInt("codresponable"));
					informe.setTipoInconsistencia(rs.getInt("tipoincosist"));
					informe.setDescripcionTipoInconsistencia(rs.getString("descripcionInconsistencia"));
					informe.setDescripcionConvenio(rs.getString("nombreconvenio"));
					informe.setPrimerNombre(rs.getString("primernombre"));
					informe.setSegundoNombre(rs.getString("segundonombre"));
					informe.setPrimerApellido(rs.getString("primerapellido"));
					informe.setSegundoApellido(rs.getString("segundoapellido"));
					informe.setIdPersona(rs.getString("numidentificacion"));
					informe.setTipoIdentificacion(rs.getString("tipoidentificacion"));
					informe.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechanacimiento")));
					informe.setDireccionResidencia(rs.getString("direccionresidencia"));
					informe.setTelefono(rs.getString("telefono"));
					informe.setDepartamento(rs.getString("departamento"));
					informe.setCodigoDepartamento(rs.getString("codigodepartamento"));
					informe.setMunicipio(rs.getString("municipio"));
					informe.setCodigoMunicipio(rs.getString("codigomunicipio"));
					informe.setCobertura(rs.getString("nombrecobertura"));
					informe.setCodCobertura(rs.getInt("coberturasalud"));
				    informe.setObservaciones(rs.getString("observaciones"));
				    informe.setConsecutivoIngreso(rs.getString("consecutivo"));
				    informe.setAnioConsecutivoIngreso(rs.getString("anioconsecutivo"));
				    informe.setIdIngreso(rs.getInt("codingreso"));
				    informe.setCodigoCuenta(rs.getInt("codcuenta"));
				    informe.setEstadoInforme(rs.getString("estado"));
				    informe.setFechaGeneracion(rs.getString("fechageneracion"));
				    informe.setHoraGeneracion(rs.getString("horageneracion"));
				    informe.setUsuarioGeneracion(rs.getString("usuariogeneracion"));
				    informe.setFechaModificacion(rs.getString("fechamodificacion"));
				    informe.setHoraModificacion(rs.getString("horamodificacion"));
				    informe.setUsuarioModificacion(rs.getString("usuariomodificacion"));
				    informe.setCodigoPaciente(rs.getInt("codpaciente"));
				    informe.setCodigoViaIngreso(rs.getInt("codviaingreso"));
				    informe.setDescripcionViaIngreso(rs.getString("nombreviaingreso")); 
				    informe.setVariablesInco(consultarVariablesIncorrectasInforme(con,rs.getInt("codigo")));  
				    envio=consultarDatosUltimoenvioInforme(con,rs.getInt("codigo"));
				    informe.setEntidad(envio.get("entidad").toString());
				    informe.setMedioEnvio(envio.get("medioenvio").toString());
				    informe.setHistorialEnvios(consultaHistoricosInforme( con, rs.getInt("codigo")));
				    
				    logger.info("\n\nCadena de consulta informe Inconsistencias>> "+cadenaConsultaInforme);		 
				    return informe; 
				}
				 else
			      {
			    	 try{
			   		  PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(datosPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			   		  ps1.setInt(1,inst);
			   		  ps1.setInt(2,codPersona);
			   		  ps1.setInt(3,convenio);
			   		  
			   		datosPaciente=datosPaciente.replace("ing.institucion = ?", "ing.institucion = " +inst);
			   		datosPaciente=datosPaciente.replace("per.codigo = ?", "per.codigo = " +codPersona);
			   		datosPaciente=datosPaciente.replace("subcuent.convenio = ?", "subcuent.convenio = " +convenio);
			   		
			   		ResultSetDecorator rs1 = new ResultSetDecorator(ps1.executeQuery());
			   			
			 		if(rs1.next())
					  {
			   			logger.info("\n\n ES INFORME NUEVO >> ");
				   		informe.setPrimerNombre(rs1.getString("primernombre"));
						informe.setSegundoNombre(rs1.getString("segundonombre"));
						informe.setPrimerApellido(rs1.getString("primerapellido"));
						informe.setSegundoApellido(rs1.getString("segundoapellido"));
						informe.setIdPersona(rs1.getString("numidentificacion"));
						informe.setTipoIdentificacion(rs1.getString("tipoidentificacion"));
						informe.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(rs1.getString("fechanacimiento")));
						
						if(rs1.getString("numidentificacion").length()<17)
						{
							informe.setIdPersona(rs1.getString("numidentificacion"));
						}else
						{
							informe.setIdPersona(rs1.getString("numidentificacion").substring(0, 17));
						}
						
						if(rs1.getString("direccion").length()<80)
						{
						informe.setDireccionResidencia(rs1.getString("direccion"));
						}
						else
						{
							informe.setDireccionResidencia(rs1.getString("direccion").substring(0, 80));
						}
						if(rs1.getString("telefono").length()<7)
						{
							informe.setTelefono(rs1.getString("telefono"));
						}
						else
						{
							informe.setTelefono(rs1.getString("telefono").substring(0, 7));
						}
						
						informe.setDepartamento(rs1.getString("nombredepartamento"));
						informe.setCodigoDepartamento(rs1.getString("coddepartamento"));
						informe.setMunicipio(rs1.getString("nombreciudad"));
						informe.setCodigoMunicipio(rs1.getString("codmunicipio"));
						informe.setCobertura(rs1.getString("nombrecobertura"));
						informe.setCodCobertura(rs1.getInt("codcobertura"));
						informe.setDescripcionConvenio(rs1.getString("nombreconvenio"));
						informe.setCodigoConvenio(rs1.getInt("codigoconvenio"));
						informe.setCodigoPaciente(rs1.getInt("codigopaciente"));
						informe.setCodigoCuenta(codCuenta);
						informe.setSubcuenta(rs1.getInt("codigosubcuenta"));
						
						logger.info("\n\nCadena de consultadatosPaciente>> "+datosPaciente);	
						return informe;
					  } 
						
			   		 }
			   		 catch (Exception e) {			
			   			e.printStackTrace();
			   			logger.info("error en  consultar Datos paciente >>  cadena >>  "+datosPaciente+" ");
			   		 } 
				  }
			  
			 }
			 catch (Exception e) {			
				e.printStackTrace();
				logger.info("error en  consultar Informe Incosistencias >>  cadena >>  "+cadenaConsultaInforme+" ");
			 }	
		
	
		return informe;
	}
	
	
	/**
	 * Metodo que carga los tipos de Inconsistencias
	 * @param con
	 * @param codinstitucion
	 * @return
	 */
	public static HashMap cargarTiposInconsistencias(Connection con, int codinstitucion){
	     
		HashMap inconsistencias=new HashMap();
		String consulta=consultaTiposIncosistenciasStr;
		
		try{
	   		  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
              consulta=consulta.replace("institucion = ?", "institucion = " +codinstitucion);
	  
	   		  ps.setInt(1,codinstitucion);
	   		  ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    	  inconsistencias=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false); 
	   		  
	    	  logger.info("\n\nCadena de consulta Tipos Inconsistencias>> "+consulta);
			  Utilidades.imprimirMapa(inconsistencias);  
			
	   		  
		   }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar Tipos de Incosistencias >>  cadena >>  "+consulta+" ");
		 }	  
		
		return inconsistencias;
	}
	
	
	/**
	 * Metodo que carga los tipos de Documentos existentes en la BD
	 * @param con
	 * @return
	 */
	public static  ArrayList cargarTiposDocumentos(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		String consulta=consultarTiposDocumentoStr;
		try{
	    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    
	    while(rs.next())
	      {
	    	HashMap<String, Object> mapa=new HashMap<String, Object>();
			mapa.put("acronimo",rs.getString("acronimo"));
			mapa.put("nombre",rs.getString("nombre"));
			resultado.add(mapa);
	      }
	    
	       return resultado;   
		 }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar Tipos de Documentos >>  cadena >>  "+consulta+" ");
		 }	
	 return null;
	}
	
	/**
	 * Metodo para cargas las ciudades
	 * @param con
	 * @return
	 */
	public static HashMap cargarCiudades(Connection con)
	{
		String consulta=consultarCiudadesStr;
		try{
	    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
	        rs.close();
			ps.close();
			return mapaRetorno;
		 }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar las Ciudades >>  cadena >>  "+consulta+" ");
		 }	
	 return null;
	}
	
	/**
	 * Metodo para cargar los departamentos
	 * @param con
	 * @return
	 */
	public static HashMap cargarDepartamentos(Connection con)
	{
		String consulta=consultarDepartamentosStr;
		try{
	    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    
		    HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
	        rs.close();
			ps.close();
			return mapaRetorno;  
		 }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar las Departamentos >>  cadena >>  "+consulta+" ");
		 }	
	 return null;
	}


/**
 * Metodo para cargar las ciudades de un departamento dado
 * @param con
 * @param codDepartamento
 * @return
 */
	public static HashMap cargarCiudadesDepto(Connection con,String codDepartamento) {
		
		String consulta=consultarCiudadesStr;
		consulta=consulta.replace("WHERE 1=1", "WHERE codigo_departamento = ? ");
		try{
	    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ps.setString(1, codDepartamento);
	    consulta=consulta.replace("codigo_departamento = ?", "codigo_departamento = '"+codDepartamento+"'");
	    ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    
		    HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
	        rs.close();
			ps.close();
			return mapaRetorno;  
		 }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar las Ciudades >>  cadena >>  "+consulta+" ");
		 }	
	 return null;
	}
	
 /**
  * Metodo para cargar las posibles variables Incorrectas en BD del sistema
  * @param con
  * @return
  */
	public static HashMap cargarVariablesIncorrectas(Connection con) {
		
		String consulta=consultarVairiablesIncorrectasStr+" ORDER BY codigo "+ConstantesBD.tipoOrdenamientoAscendente ;
		logger.info("Entró a cargar las variables incorrectas");
	    try{
	     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    
	     	HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
	        rs.close();
			ps.close();
			return mapaRetorno;  
		 }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar las Variables Incorrectas >>  cadena >>  "+consulta+" ");
		 }	
	 return null;
	}


/**
 * Metodo que inserta la informacion de un Informe de Inconsistencias 
 * @param con
 * @param informeInconsistencias
 * @param parametros
 * @return
 */
public static HashMap insertarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias, HashMap parametros, String ingresarEnvioInformeIncosist, String ingresarVariablesIncorrectasInformeInconsist) {
	
	ActionErrors errores = new ActionErrors();
	HashMap resultado = new HashMap();
	resultado.put("codigoPk",ConstantesBD.codigoNuncaValido+"");
	resultado.put("error",errores);	
	int ingreso = Utilidades.convertirAEntero(parametros.get("codIngreso").toString());
	int convenio = Utilidades.convertirAEntero(parametros.get("codConvenio").toString());
	int subcuenta= Utilidades.convertirAEntero(parametros.get("codSubcuenta").toString());
	
	String usuario=parametros.get("loginUsuario").toString();
	String cadenaInsercion=ingresarInformeInconsistenciasStr;
	String cadenaInsercionVariables=ingresarVariablesIncorrectasInformeInconsist;
	String cadenaInsercionEnvio=ingresarEnvioInformeIncosist;
	int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejopaciente.seq_informe_inconsistencias");	
	logger.info("\n\n Valor consecutivo del INFORME Obtenido >>"+consecutivo);
	String valorConsecutivoInfo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()));
	String anioConsecutivoInfo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()),valorConsecutivoInfo);
		
	if(Utilidades.convertirAEntero(valorConsecutivoInfo) < 0)
	{
		errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Inicializar el consecutivo [Informe Inconsistencias en Verificación de Base de Datos] ubicado en el modulo de Administración."));
		resultado.put("error",errores);
		return resultado;
	}
	
	
	if(informeInconsistencias.getTipoInconsistencia()<=0)
    {
		errores.add("Campo Tipo Inconsistencia", new ActionMessage("errors.required","El campo Tipo de Inconsistencia"));
	    resultado.put("error",errores);	    
    }

    if(informeInconsistencias.getPrimerApellido().trim().equals(""))
    {
    	errores.add("Campo Primer Apellido", new ActionMessage("errors.required","El campo Primer Apellido"));
   	    resultado.put("error",errores);
    }

    
    if(informeInconsistencias.getPrimerNombre().trim().equals(""))
	{
		errores.add("Campo Primer Nombre", new ActionMessage("errors.required","El campo Primer Nombre"));
		resultado.put("error",errores);
	}
	
	if(informeInconsistencias.getTipoIdentificacion().trim().equals(""))
	{
		errores.add("Campo Tipo Identificacion", new ActionMessage("errors.required","El campo Tipo de Identificacion"));
		resultado.put("error",errores);
	}
	if(informeInconsistencias.getIdPersona().trim().equals(""))
	{
		errores.add("Campo Numero Identificacion", new ActionMessage("errors.required","El campo Numero de Documento"));
		resultado.put("error",errores);
	}
	if(informeInconsistencias.getFechaNacimiento().trim().equals(""))
	{
		errores.add("Campo Fecha de Nacimiento", new ActionMessage("errors.required","El campo Fecha de Nacimiento"));
		resultado.put("error",errores);
	}			
	if(informeInconsistencias.getDireccionResidencia().trim().equals(""))
	{
		errores.add("Campo Direccion Residencia", new ActionMessage("errors.required","El campo Direccion de Residencia"));
		resultado.put("error",errores);
	}

	if(informeInconsistencias.getTelefono().trim().equals(""))
	{
		errores.add("Campo Telefono", new ActionMessage("errors.required","El campo Telefono"));
		resultado.put("error",errores);
	}
	if(informeInconsistencias.getDepartamento().trim().equals(""))
	{
		errores.add("Campo Deparatamento", new ActionMessage("errors.required","El campo Deparatamento"));
		resultado.put("error",errores);
	}
	if(informeInconsistencias.getCodigoDepartamento().trim().equals(""))
	{
		errores.add("Campo Codigo Departamento", new ActionMessage("errors.required","El campo Codigo Departamento"));
		resultado.put("error",errores);
	}
	if(informeInconsistencias.getMunicipio().trim().equals(""))
	{
		errores.add("Campo Municipio", new ActionMessage("errors.required","El campo Municipio"));
		resultado.put("error",errores);			
	}
	if(informeInconsistencias.getCodigoMunicipio().trim().equals(""))
	{
		errores.add("Campo Codigo Municipio", new ActionMessage("errors.required","El campo Codigo Municipio"));
		resultado.put("error",errores);		
	}
	if(informeInconsistencias.getCodCobertura() <= 0)
	{
		errores.add("Campo Cobertura", new ActionMessage("errors.required","El campo Cobertura"));
		resultado.put("error",errores);
	}
	
	if(!errores.isEmpty())
	{
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		return resultado;
	}
			
	
     if(consultarEstadoEnvioInforme(con,parametros.get("codIngreso").toString(),parametros.get("codConvenio").toString())==0)
	     {
    	 try{
    		 
    		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		 cadenaInsercion=cadenaInsercion.replace("? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?)",
    		 		""+consecutivo+", " +
    		 		convenio +","+ subcuenta+","+informeInconsistencias.getTipoInconsistencia()+",'"+informeInconsistencias.getSubcuenta()+"'," +
    		 		"'"+informeInconsistencias.getPrimerApellido() +"','"+informeInconsistencias.getSegundoApellido() +"','"+ informeInconsistencias.getPrimerNombre() +"','"+informeInconsistencias.getSegundoNombre()+"','"+informeInconsistencias.getTipoIdentificacion()+"'," +
    		 	    "'"+informeInconsistencias.getIdPersona()+"','"+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(informeInconsistencias.getFechaNacimiento()))+"'," +
    		 	    "'"+informeInconsistencias.getDireccionResidencia()+"','"+informeInconsistencias.getTelefono()+"','"+informeInconsistencias.getDepartamento()+"'," +
    		 	    "'"+informeInconsistencias.getCodigoDepartamento()+"','"+informeInconsistencias.getMunicipio()+"','"+informeInconsistencias.getCodigoMunicipio()+"'," +
    		 	    " "+informeInconsistencias.getCodCobertura()+",'"+informeInconsistencias.getObservaciones()+"','"+valorConsecutivoInfo+"','"+anioConsecutivoInfo+"',"+ingreso+","+informeInconsistencias.getCodigoCuenta()+"," +
    		 	    "'"+informeInconsistencias.getEstadoInforme()+"', " +
    		 		"CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",'"+usuario+"', CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",'"+usuario+"',"+informeInconsistencias.getCodigoPaciente()+","+informeInconsistencias.getCodigoViaIngreso()+")");
    		 
    		 ps.setInt(1, consecutivo);
    		 ps.setInt(2,convenio);
    	     ps.setInt(3, subcuenta);
    	     ps.setInt(4, informeInconsistencias.getTipoInconsistencia());
    	     if(!informeInconsistencias.getDescripcionConvenio().equals(""))
    	    	 ps.setString(5,informeInconsistencias.getDescripcionConvenio());
    	     else
    	    	 ps.setNull(5, Types.VARCHAR);
    	     
    	     ps.setString(6,informeInconsistencias.getPrimerApellido());
    	     ps.setString(7,informeInconsistencias.getSegundoApellido());
    	     ps.setString(8,informeInconsistencias.getPrimerNombre());
    	     ps.setString(9,informeInconsistencias.getSegundoNombre());
    	     ps.setString(10,informeInconsistencias.getTipoIdentificacion());
    	     ps.setString(11,informeInconsistencias.getIdPersona());
    	     ps.setString(12, UtilidadFecha.conversionFormatoFechaABD(informeInconsistencias.getFechaNacimiento()));
    	     ps.setString(13,informeInconsistencias.getDireccionResidencia());
    	     ps.setString(14,informeInconsistencias.getTelefono());
    	     ps.setString(15,informeInconsistencias.getDepartamento());
    	     ps.setString(16,informeInconsistencias.getCodigoDepartamento());
    	     ps.setString(17,informeInconsistencias.getMunicipio()); 
    	     ps.setString(18,informeInconsistencias.getCodigoMunicipio());
    	     ps.setInt(19,informeInconsistencias.getCodCobertura());
    	     ps.setString(20,informeInconsistencias.getObservaciones());
    	     ps.setString(21, valorConsecutivoInfo);
    	     if(!anioConsecutivoInfo.equals(""))
    	    	 ps.setString(22,anioConsecutivoInfo);
    	     else
    	    	 ps.setNull(22, Types.VARCHAR);
    	     
    	     ps.setInt(23,ingreso);
    	     ps.setInt(24, informeInconsistencias.getCodigoCuenta());
    	     ps.setString(25,informeInconsistencias.getEstadoInforme());
    	     ps.setString(26,usuario);
    	     ps.setString(27,usuario);
    	     ps.setInt(28,informeInconsistencias.getCodigoPaciente());
    	     ps.setInt(29,informeInconsistencias.getCodigoViaIngreso());
    	     
    	     logger.info("cadena insercion INFORME INCONSISTENCIAS >> "+cadenaInsercion);
    	     if(!(ps.executeUpdate()>0))
    	        {
    	    	 errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion del Informe de Inconsistencias"));
				 resultado.put("error",errores);
				 UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				 return resultado;
    	        }
    	         else
    	        {
    	    	 
    	    	 logger.info("Tamaño del vector de variables inconsistencias >> "+informeInconsistencias.getVariablesInco().length);
    	    	 for(int i=0;i<informeInconsistencias.getVariablesInco().length;i++)
    		        {
    	    		 if(informeInconsistencias.getVariablesInco()[i]!=null && !informeInconsistencias.getVariablesInco()[i].equals("") )
   	    	          {
    	    		  try
   	    	          {
    	    	      String[] variableInconsistencia=informeInconsistencias.getVariablesInco()[i].split(ConstantesBD.separadorSplit); 	
    	    	      int codVariable=Utilidades.convertirAEntero(variableInconsistencia[0]);
    	    	      String indicadorVariable=variableInconsistencia[2];
    	    	      String descripcion=new String("");
    	    	      if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoPrimerNombre))
    	    	      {
    	    	    	  descripcion=parametros.get("primernombre").toString();  
    	    	      }else
   	    	            if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoSegundoNombre))
   	    	            {
   	    	            	descripcion=parametros.get("segundonombre").toString();
   	    	            }else
   	    	            	if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoPrimerApellido))
   	    	            	{
   	    	            		descripcion=parametros.get("primerapellido").toString();
   	    	            	}else
   	    	            	if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoSegundoApellido))
   	    	            	{
   	    	            		descripcion=parametros.get("segundoapellido").toString();
   	    	            	}else
   	    	            		if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoTipoDocIdentificacion))
   	    	            		{
   	    	            			descripcion=parametros.get("tipodocumento").toString();
   	    	            		}else
   	    	            			if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoNumDocIdentificacion))
   	    	            			{
   	    	            				descripcion=parametros.get("numeroidentificacion").toString();
   	    	            			}else
   	    	            				if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoFechaNacimiento))
   	    	            				{
   	    	            					descripcion=parametros.get("fechanacimiento").toString();
   	    	            				}
    	    	        cadenaInsercionVariables=ingresarVariablesIncorrectasInformeInconsist;
    	    	        PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionVariables,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));   	    		         
   	    		        ps2.setInt(1,consecutivo);
   	    		        ps2.setInt(2, codVariable);
   	    		        ps2.setString(3, descripcion);   		        
   	    		        
   	    		       ps2.executeUpdate();
   	    		        
   	    	           
   	    	          } 
    	    	         catch (Exception e) {			
      	    			 e.printStackTrace();    	    		 
      	    			 UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
      	    			 logger.info("error en Insertar las variables Incorrectas del Informe>>  cadena >>  "+cadenaInsercionVariables+" ");
    		          }
   	    	        }
    		        }
    	    	 
    	    	 
    	    	  if(informeInconsistencias.getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoEnviado))
    	    	  { 
    	    		 
    	    		 String convenioEnvio = informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoSi)?informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[0]:"";
    	    		 String empresaEnvio =  informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoNo)?informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[0]:""; 
    	    	     try{
    	    	     PreparedStatementDecorator ps3 =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionEnvio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    	    	     cadenaInsercionEnvio=cadenaInsercionEnvio.replace("?, CURRENT_DATE",""+informeInconsistencias.getCodigoPk()+", CURRENT_DATE");
    	    	     cadenaInsercionEnvio=cadenaInsercionEnvio.replace("?, ?, ?, ?", "'"+usuario+"',"+convenio+","+informeInconsistencias.getCodigoEntidad()+",'"+informeInconsistencias.getMedioEnvio()+"'");
    	    	     ps3.setInt(1, consecutivo);
    	    	     ps3.setString(2,usuario);
    	    	     
    	    	     if(!convenioEnvio.equals(""))
    	    	        ps3.setInt(3,Utilidades.convertirAEntero(convenioEnvio));
    	    		   else
    	    		  ps3.setNull(3,Types.INTEGER);
    	    	     
    	    	    if(!empresaEnvio.equals(""))
    	    		  ps3.setInt(4,Utilidades.convertirAEntero(empresaEnvio));    
    	    	    else
    	    		  ps3.setNull(4,Types.INTEGER);
    	    	       
    	    	     ps3.setString(5,informeInconsistencias.getMedioEnvio());
    	    	     
    	    	     ps3.setString(6, informeInconsistencias.getPathArchivoIncoXml());
    	    	     
    	    	     if(!(ps3.executeUpdate()>0))
    	    	     {
    	    	    	errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion de datos de Envio"));
   					   	resultado.put("error",errores);
   					   	resultado.put("codigoPk",ConstantesBD.codigoNuncaValido);
   					    UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
   					   	logger.info("error en la Insercion del Envio  >>  cadena >>  "+cadenaInsercionEnvio+" ");
   					   	return resultado; 
    	    	     }

    	       	   }
    	    	      catch (Exception e) {			
    	    		   e.printStackTrace();    	    		   
    	    		
    	    	       }
    	    	  } 
    	    	    	 
    	    	 resultado.put("codigoPk",consecutivo);
    	    	 UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeInconVeribd,Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
    	    	 return resultado;
    	        }  
    		  }
    		   catch (Exception e) {			
    			 e.printStackTrace(); 
    			 
    			 logger.info("error en  Insertar el Informe de Inconsistencias>>  cadena >>  "+cadenaInsercion+" ");
    			
    		 }	
	     }
		 
	return resultado;
} 


/**
 * Metodo que Modifica la informacion de un Informe de Inconsistencias siempre y cuando no se haya enviado
 * @param con
 * @param informeInconsistencias
 * @param parametros
 * @return
 */
public static HashMap modificarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias, HashMap parametros, String ingresarEnvioInformeIncosist, String ingresarVariablesIncorrectasInformeInconsist) 
{
	ActionErrors errores = new ActionErrors();
	HashMap resultado = new HashMap();
	resultado.put("codigoPk",ConstantesBD.codigoNuncaValido+"");
	resultado.put("error",errores);	
	int ingreso = Utilidades.convertirAEntero(parametros.get("codIngreso").toString());
	int convenio = Utilidades.convertirAEntero(parametros.get("codConvenio").toString());
	
	String usuario=parametros.get("loginUsuario").toString();
	
	 String cadenaModificacion=modificarInformeInconsistenciasStr;
	 String cadenaInsercionEnvio=ingresarEnvioInformeIncosist;
	 
	 if(informeInconsistencias.getTipoInconsistencia()<=0)
      {
    	errores.add("Campo Tipo Inconsistencia", new ActionMessage("errors.required","El campo Tipo de Inconsistencia"));
	    resultado.put("error",errores);
	    return resultado;
      }

       if(informeInconsistencias.getPrimerApellido().trim().equals(""))
        {
    	 errores.add("Campo Primer Apellido", new ActionMessage("errors.required","El campo Primer Apellido"));
    	 resultado.put("error",errores);
    	 return resultado;
        }
     
     if(informeInconsistencias.getPrimerNombre().trim().equals(""))
		{
			errores.add("Campo Primer Nombre", new ActionMessage("errors.required","El campo Primer Nombre"));
			resultado.put("error",errores); 
			return resultado;  
		}
     
	
		if(informeInconsistencias.getTipoIdentificacion().trim().equals(""))
		{
			errores.add("Campo Tipo Identificacion", new ActionMessage("errors.required","El campo Tipo de Identificacion"));
			resultado.put("error",errores);
			return resultado; 
		}
		if(informeInconsistencias.getIdPersona().trim().equals(""))
		{
			errores.add("Campo Numero Identificacion", new ActionMessage("errors.required","El campo Numero de Documento"));
			resultado.put("error",errores);
			return resultado;
		}
		if(informeInconsistencias.getFechaNacimiento().trim().equals(""))
		{
			errores.add("Campo Fecha de Nacimiento", new ActionMessage("errors.required","El campo Fecha de Nacimiento"));
			resultado.put("error",errores);
			return resultado;    
		}			
		if(informeInconsistencias.getDireccionResidencia().trim().equals(""))
		{
			errores.add("Campo Direccion Residencia", new ActionMessage("errors.required","El campo Direccion de Residencia"));
			resultado.put("error",errores);
			return resultado;
		}
	
		if(informeInconsistencias.getTelefono().trim().equals(""))
		{
			errores.add("Campo Telefono", new ActionMessage("errors.required","El campo Telefono"));
			resultado.put("error",errores);
			return resultado;
		}
		if(informeInconsistencias.getDepartamento().trim().equals(""))
		{
			errores.add("Campo Deparatamento", new ActionMessage("errors.required","El campo Deparatamento"));
			resultado.put("error",errores);
			return resultado;
		}
		if(informeInconsistencias.getCodigoDepartamento().trim().equals(""))
		{
			errores.add("Campo Codigo Departamento", new ActionMessage("errors.required","El campo Codigo Departamento"));
			resultado.put("error",errores);
			return resultado;
		}
		if(informeInconsistencias.getMunicipio().trim().equals(""))
		{
			errores.add("Campo Municipio", new ActionMessage("errors.required","El campo Municipio"));
			resultado.put("error",errores);
			return resultado;
		}
		if(informeInconsistencias.getCodigoMunicipio().trim().equals(""))
		{
			errores.add("Campo Codigo Municipio", new ActionMessage("errors.required","El campo Codigo Municipio"));
			resultado.put("error",errores);
			return resultado;
		}
	 	 
	 
	 logger.info("ENTRO A MODIFICAR INFORME INCONSISTENCIAS ");
	
	   try{  
		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		 ps.setInt(1,convenio);
	     ps.setInt(2, informeInconsistencias.getSubcuenta());
	     ps.setInt(3, informeInconsistencias.getTipoInconsistencia());
	     ps.setString(4,informeInconsistencias.getDescripcionConvenio());
	     ps.setString(5,informeInconsistencias.getPrimerApellido());
	     ps.setString(6,informeInconsistencias.getSegundoApellido());
	     ps.setString(7,informeInconsistencias.getPrimerNombre());
	     ps.setString(8,informeInconsistencias.getSegundoNombre());
	     ps.setString(9,informeInconsistencias.getTipoIdentificacion());
	     ps.setString(10,informeInconsistencias.getIdPersona());
	     ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(informeInconsistencias.getFechaNacimiento())));
	     ps.setString(12,informeInconsistencias.getDireccionResidencia());
	     ps.setString(13,informeInconsistencias.getTelefono());
	     ps.setString(14,informeInconsistencias.getDepartamento());
	     ps.setString(15,informeInconsistencias.getCodigoDepartamento());
	     ps.setString(16,informeInconsistencias.getMunicipio()); 
	     ps.setString(17,informeInconsistencias.getCodigoMunicipio());
	     ps.setInt(18,informeInconsistencias.getCodCobertura());
	     ps.setString(19,informeInconsistencias.getObservaciones());
	    // ps.setString(20, informeInconsistencias.getConsecutivoIngreso());
	    // ps.setString(21,informeInconsistencias.getAnioConsecutivoIngreso());
	     ps.setInt(20,ingreso);
	     ps.setInt(21, informeInconsistencias.getCodigoCuenta());
	     ps.setString(22,informeInconsistencias.getEstadoInforme());
	     ps.setString(23,usuario);
	     ps.setInt(24,informeInconsistencias.getCodigoPaciente());
	     ps.setInt(25,informeInconsistencias.getCodigoViaIngreso());
	     ps.setInt(26, informeInconsistencias.getCodigoPk());
	     
	     
	     logger.info("\n \n CONSECUTIVO INGRESO:  >> "+informeInconsistencias.getConsecutivoIngreso());
	     logger.info("\n \n Va Ejecutar la actualizacion:  >> "+cadenaModificacion);
	     
	     if(!(ps.executeUpdate()>0))
	     {
	    	 errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la modificacion del Informe"));
			 resultado.put("error",errores);
			 return resultado;
	     }
	     
	     logger.info("\n \n Ejecuto la actualizacion:  >> "+cadenaModificacion);
	     
	     modificarVariablesInconsistencia(con,informeInconsistencias.getCodigoPk(),informeInconsistencias.getVariablesInco(),informeInconsistencias.getFiltroVariable(),parametros, ingresarVariablesIncorrectasInformeInconsist);
	     
	     if(informeInconsistencias.getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoEnviado))
   	      { 
	      String convenioEnvio = informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoSi)?informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[0]:"";
	      String empresaEnvio =  informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoNo)?informeInconsistencias.getEntidad().split(ConstantesBD.separadorSplit)[0]:""; 
   	     
	      try{
   	      PreparedStatementDecorator ps3 =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionEnvio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   	      cadenaInsercionEnvio=cadenaInsercionEnvio.replace("?, CURRENT_DATE",""+informeInconsistencias.getCodigoPk()+", CURRENT_DATE");
   	      cadenaInsercionEnvio=cadenaInsercionEnvio.replace("?, ?, ?, ?", "'"+usuario+"',"+convenioEnvio+","+empresaEnvio+",'"+informeInconsistencias.getMedioEnvio()+"'");
   	      logger.info("CADENA DE ENVIO:  >> "+cadenaInsercionEnvio);
   	      ps3.setInt(1, informeInconsistencias.getCodigoPk());
   	      ps3.setString(2,usuario);
   	  
   	      if(!convenioEnvio.equals(""))
	        ps3.setInt(3,Utilidades.convertirAEntero(convenioEnvio));
		   else
		  ps3.setNull(3,Types.INTEGER);
	     
	    if(!empresaEnvio.equals(""))
		  ps3.setInt(4,Utilidades.convertirAEntero(empresaEnvio));    
	    else
		  ps3.setNull(4,Types.INTEGER); 
   	      
   	      ps3.setString(5,informeInconsistencias.getMedioEnvio());
   	       
   	      ps3.setString(6,informeInconsistencias.getPathArchivoIncoXml());
   	      
   	      if(!(ps3.executeUpdate()>0))
	       {
	    	errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion de datos de Envio"));
		   	resultado.put("error",errores);
		   	resultado.put("codigoPk",ConstantesBD.codigoNuncaValido);
		   	logger.info("error en la Insercion del Envio  >>  cadena >>  "+cadenaInsercionEnvio+" ");
		   	return resultado; 
	        }
   	     
   	      }
   	      catch (Exception e) {			
   		   e.printStackTrace(); 
			   logger.info("error en la Insercion del Envio  >>  cadena >>  "+cadenaInsercionEnvio+" ");
			  
   		
   	       }
   	  }
	       
	       
	   resultado.put("codigoPk",informeInconsistencias.getCodigoPk());
	     
	   }
	   catch (Exception e) {			
		 e.printStackTrace();    
		 
		 logger.info("error al MODIFICAR el Informe de Inconsistencias>>  cadena >>  "+cadenaModificacion+" ");
		
	   }	
       
	
	return resultado;
}

/**
 * Metodo que mdifica las variables de Inconsistencia de un informe especifico
 * @param con
 * @param codigoInforme
 * @param variablesInco
 * @param parametros
 * @return
 */
 public static int modificarVariablesInconsistencia(Connection con,int codigoInforme, String[] variablesInco, String filtroVariable, HashMap parametros, String ingresarVariablesIncorrectasInformeInconsist)
 {
	 String cadenaInsercionVariables=ingresarVariablesIncorrectasInformeInconsist;
	 String cadenaEliminarVarialbles=eliminarVariablesIncorrectasInforme;
	 try{
	   
		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarVarialbles,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     ps.setInt(1, codigoInforme);
	     ps.executeUpdate();
	     logger.info("ESTE ES EL VALOR DEL FILTRO VARIABLES INCORRECTAS >>  "+filtroVariable+" ");
	 
	 if(filtroVariable.equals(ConstantesBD.acronimoSi))
	     {
	     for(int i=0;i<variablesInco.length;i++)
		     {
				 if(variablesInco[i]!=null && !variablesInco[i].equals("") )
		          {
				  try
		          {
			      String[] variableInconsistencia=variablesInco[i].split(ConstantesBD.separadorSplit); 	
			      int codVariable=Utilidades.convertirAEntero(variableInconsistencia[0]);
			      String indicadorVariable=variableInconsistencia[2];
			      String descripcion=new String("");
			      
			      if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoPrimerNombre))
			      {
			    	  descripcion=parametros.get("primernombre").toString();  
			      }else
		            if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoSegundoNombre))
		            {
		            	descripcion=parametros.get("segundonombre").toString();
		            }else
		            	if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoPrimerApellido))
		            	{
		            		descripcion=parametros.get("primerapellido").toString();
		            	}else
		            	if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoSegundoApellido))
		            	{
		            		descripcion=parametros.get("segundoapellido").toString();
		            	}else
		            		if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoTipoDocIdentificacion))
		            		{
		            			descripcion=parametros.get("tipodocumento").toString();
		            		}else
		            			if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoNumDocIdentificacion))
		            			{
		            				descripcion=parametros.get("numeroidentificacion").toString();
		            			}else
		            				if(indicadorVariable.equals(ConstantesIntegridadDominio.acronimoFechaNacimiento))
		            				{
		            					descripcion=parametros.get("fechanacimiento").toString();
		            				}
			        cadenaInsercionVariables=ingresarVariablesIncorrectasInformeInconsist;
			        
			        PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionVariables,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));   	    		         
			        ps2.setInt(1,codigoInforme);
			        ps2.setInt(2, codVariable);
			        ps2.setString(3, descripcion);   		        
			       
			        ps2.executeUpdate();
			        
              }
		     catch (Exception e) {			
			  e.printStackTrace();    	    		 
			  logger.info("error al MODIFICAR INFORME al momento de Insertar las variables Incorrectas del Informe>>>  cadena >>  "+cadenaInsercionVariables+" ");
		   }
          }
        }
	   }
	 }
	   catch (Exception e) {			
		 e.printStackTrace();    	    		 
		 logger.info("error al MODIFICAR INFORME al momento de eliminar las variables de Inconsistencias>>  cadena >>  "+cadenaEliminarVarialbles+" ");
	   }
	  return 0;	
 }

 /**
  * Metodo que consulta el estado de un informe
  * @param con
  * @param ingreso
  * @param convenio
  * @return
  */

 public static int consultarEstadoEnvioInforme(Connection con,String ingreso, String codSubCuenta)
   {
		String consultaEstadoEnvio=consultaEstadoEnvioInformeStr;
		 try{
		     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaEstadoEnvio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     ps.setInt(1,Utilidades.convertirAEntero(ingreso));
		     ps.setInt(2, Utilidades.convertirAEntero(codSubCuenta));
		     consultaEstadoEnvio=consultaEstadoEnvio.replace("ingreso = ?", "ingreso = "+ingreso); 
		     consultaEstadoEnvio=consultaEstadoEnvio.replace("sub_cuenta = ?", "sub_cuenta = "+codSubCuenta);
		    
		     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		     
		     if(rs.next()){
		    	 return 1;
		      }
		      
			 }
			   catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("error en  consultar el estado de Envio del Informe >>  cadena >>  "+consultaEstadoEnvio+" ");
			 }
	 return 0;
   }

 /**
  * Metodo para Consultar las variables incorretas asociadas a un Informe
  * @param con
  * @param codInforme
  * @return
  */
 public static String[] consultarVariablesIncorrectasInforme(Connection con, int codInforme)
 {
	 String[] variables=new String[10];
	 String consulta=consultarVariablesIncorrectasInformeIncoStr;
	 HashMap mapVariables=new HashMap();
	 mapVariables.put("numRegistros","0");
	 int cont=0;
	 try{
	     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     ps.setInt(1, codInforme);
	     consulta= consulta.replace("informe_inconsistencias = ?", "informe_inconsistencias = "+ codInforme);
	     
	     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	     mapVariables = UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
	     logger.info("cadena consulta Variables>>  "+consulta+" ");
	     
	     if(Utilidades.convertirAEntero(mapVariables.get("numRegistros")+"")>0)
	      {
	    	variables=new String[Utilidades.convertirAEntero(mapVariables.get("numRegistros")+"")]; 	 
	        while(cont < Utilidades.convertirAEntero(mapVariables.get("numRegistros")+""))
	          {	
	    	  variables[cont] = mapVariables.get("codvariable_"+cont).toString()+ConstantesBD.separadorSplit+mapVariables.get("descripcionvariable_"+cont).toString()+ConstantesBD.separadorSplit+mapVariables.get("indicador_"+cont).toString();
	    	  cont++;
	          }
	          return variables;
	       }
		 }
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar las Variables Incorrectas del Informe >>  cadena >>  "+consulta+" ");
		 }	
		   
	 return variables;
 }


/**
 * Metodo que realiza la insercion de los datos de  Envio del Informe de Inconsistencias
 * @param con
 * @param parametros
 * @return
 */
public static HashMap insertarEnvioInformeInconsistencias(Connection con,HashMap parametros, String ingresarEnvioInformeIncosist) {
	String cadenaInsercion=ingresarEnvioInformeIncosist;
	String usuarioModificacion=new String("");
	String medioEnvio=new String("");
	String pathArchivo="";
	ActionErrors errores = new ActionErrors();
	HashMap resultado = new HashMap();
	resultado.put("codigoPk",ConstantesBD.codigoNuncaValido+"");
	resultado.put("error",errores);
	int convenio=0;
	int codInforme=0;
	int codEntidad=0;
	
	
	if(parametros.containsKey("convenio") && 
			!parametros.get("convenio").toString().equals("") && 
				Utilidades.convertirAEntero(parametros.get("convenio").toString()) > 0)
	            convenio= Utilidades.convertirAEntero(parametros.get("convenio").toString());
     
	if(parametros.containsKey("usuario") && 
			!parametros.get("usuario").toString().equals("") )
	            usuarioModificacion= parametros.get("usuario").toString();
	
	if(parametros.containsKey("codInforme") && 
			!parametros.get("codInforme").toString().equals("") && 
				Utilidades.convertirAEntero(parametros.get("codInforme").toString()) > 0)
		        codInforme= Utilidades.convertirAEntero(parametros.get("codInforme").toString());
	
	if(parametros.containsKey("empresa") && 
			!parametros.get("empresa").toString().equals("") && 
				Utilidades.convertirAEntero(parametros.get("empresa").toString()) > 0)
	           	codEntidad= Utilidades.convertirAEntero(parametros.get("empresa").toString());

	if(parametros.containsKey("medioenvio") && 
			!parametros.get("medioenvio").toString().equals(""))
	           	medioEnvio= parametros.get("medioenvio").toString();
	
	if(parametros.containsKey("patharchivo") && 
			!parametros.get("patharchivo").toString().equals(""))
	           	pathArchivo= parametros.get("patharchivo").toString();
	
	try{
	     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     cadenaInsercion=cadenaInsercion.replace("?, CURRENT_DATE",""+codInforme+", CURRENT_DATE");
	     cadenaInsercion=cadenaInsercion.replace("?, ?, ?, ?", "'"+usuarioModificacion+"',"+convenio+","+codEntidad+",'"+medioEnvio+"'");
	     ps.setInt(1, codInforme);
	     ps.setString(2,usuarioModificacion);
	    
	     if(convenio>0)
		        ps.setInt(3,convenio);
			   else
			  ps.setNull(3,Types.INTEGER);
		     
		    if(codEntidad>0)
			  ps.setInt(4,codEntidad);    
		    else
			  ps.setNull(4,Types.INTEGER);
	    
	     ps.setString(5,medioEnvio);
	    
	     ps.setString(6, pathArchivo);
	     
	     if (ps.executeUpdate()>0)
	     {
	    	 resultado.put("codigoPk",codInforme);
	     }
	     else{
	    	 errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion de datos de Envio"));
			 resultado.put("error",errores);
			 return resultado;
	     }
	   }
	    catch (Exception e) {			
		 e.printStackTrace();
		 
		 logger.info("error en la Insercion del Envio  >>  cadena >>  "+cadenaInsercion+" ");
		 
	    }	
	return resultado;
  }


/**
 * Netodo que realiza la consulta de los datos del ultimo envio realizado
 * @param con
 * @param codigoInconsistencia
 * @return
 */
public static HashMap consultarDatosUltimoenvioInforme(Connection con,int codigoInconsistencia)
 {
  HashMap mapaInforme=new HashMap();
  String consultaEnvioIncon=consultaEnviosInformeStr;
  consultaEnvioIncon=consultaEnvioIncon + " ORDER BY fecha_envio || ' ' || hora_envio DESC ";
  try{
	     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaEnvioIncon,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     consultaEnvioIncon=consultaEnvioIncon.replace("informe_inconsist = ?", "informe_inconsist = "+codigoInconsistencia +" ");
	     ps.setInt(1,codigoInconsistencia);
	     logger.info("\n\n esta es la CADENA ULTIMO ENVIO >> "+consultaEnvioIncon);
	     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	    if(rs.next())
	    {  	
	     if(rs.getInt("codentidadenvio")<0)
	       mapaInforme.put("entidad", rs.getString("convenioenvio")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi);
	     else
	       mapaInforme.put("entidad", rs.getString("codentidadenvio")+ConstantesBD.separadorSplit+ConstantesBD.acronimoNo);
	     
	        mapaInforme.put("medioenvio",rs.getString("medioenvio"));
	     
	     if(rs.getString("nombreentidadenvio")==null || rs.getString("nombreentidadenvio").equals(""))
	    	 mapaInforme.put("nomentidad",rs.getString("nombreconvenioenvio"));
	     else
	    	 mapaInforme.put("nomentidad",rs.getString("nombreentidadenvio"));
	    	 
	     
	     
	     logger.info("Esta es la Entidad Envio  >>  dato >>  "+mapaInforme.get("entidad")+" ");
	    }
	    else{
    	     mapaInforme.put("entidad", "");
		     mapaInforme.put("medioenvio","");
		     mapaInforme.put("nomentidad","" );
	    	
	    }
  }
	     catch (Exception e) {			
			 e.printStackTrace();
			 
			 logger.info("error en la Consulta de Datos de  Informes Enviados  >>  cadena >>  "+consultaEnvioIncon+" ");
			 
		    }
  
  return mapaInforme;
 }
 

/**
 * Metodo qeu realiza la consulta del Historico ( Envíos realizados al informe)
 * @param con
 * @param codInforme
 * @return
 */
public static ArrayList< DtoRegistroEnvioInformInconsisenBD> consultaHistoricosInforme(Connection con, int codInforme)
 {
	ArrayList< DtoRegistroEnvioInformInconsisenBD> historico = new ArrayList< DtoRegistroEnvioInformInconsisenBD>();
	String consultaHistoricos=consultaEnviosInformeStr;
	consultaHistoricos=consultaHistoricos + " ORDER BY fecha_envio || ' ' || hora_envio DESC ";
	String path = "";
	 try{
		 logger.info("consulta: "+consultaHistoricos);
		 logger.info("codigoInforme: "+codInforme);
	     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaHistoricos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     consultaHistoricos=consultaHistoricos.replace("informe_inconsist = ?", "informe_inconsist = "+codInforme +" ");
	     ps.setInt(1,codInforme);
	     logger.info("\n\n ENTRO A CARGAR EL HISTORICO  >> ");
	     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	       
	        while(rs.next())
	         {  
	        	DtoRegistroEnvioInformInconsisenBD dtoEnvio=new DtoRegistroEnvioInformInconsisenBD();
	    	    dtoEnvio.setCodigoPk(rs.getInt("codigoenvio"));
	    	    dtoEnvio.setCodigoInformInconsisenBD(codInforme);
	    	    dtoEnvio.setCodigoEntidadEnvio(rs.getInt("codentidadenvio"));
	    	    dtoEnvio.setNombreEntidadEnvio(rs.getString("nombreentidadenvio"));
	    	    dtoEnvio.setNombreConvenioEnvio(rs.getString("nombreconvenioenvio"));
	    	    dtoEnvio.setCodigoConvenioEnvio(rs.getInt("convenioenvio"));
	    	    dtoEnvio.setFechaEnvio(rs.getString("fechaenvio"));
	    	    dtoEnvio.setHoraEnvio(rs.getString("horaenvio"));
	    	    dtoEnvio.setMedioEnvio(rs.getString("medioenvio"));
	    	    dtoEnvio.setUrlArchivoIncoXmlDes(rs.getString("patharchivo")!=null?rs.getString("patharchivo"):"");
	    	    dtoEnvio.setUsuarioEnvio(rs.getString("usuarioenvio"));
	    	    
	        	historico.add(dtoEnvio);
	         }
	   }
         catch (Exception e) {			
		 e.printStackTrace();
		 
		 logger.info("error en la Consulta del Historico de Informes Enviados  >>  cadena >>  "+consultaHistoricos+" ");
		 
	    }
	
	return historico;
 }
 
/**
 * Metodo que realiza la consulta de Coberturas Salud de un Paciente Asociado a un convenio
 * @param con
 * @param codConvenio
 * @return
 */
  public static HashMap consultarCoberturasSaludPaciente(Connection con, int codConvenio)
  {
  HashMap listado = new HashMap(); 
  String consulta=consultaCoberturaSaludPacientesStr;
  try{
	     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     consulta=consulta.replace("con.codigo = ?", "con.codigo = "+codConvenio +" ");
	     ps.setInt(1,codConvenio);
	     
	     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	     listado=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
	     
	     logger.info("cadena consulta Coberturas>>  "+consulta+" ");
  }
  catch (Exception e) {			
		 e.printStackTrace();
		 
		 logger.info("error en la Consulta de Coberturas Salud del Paciente  >>  cadena >>  "+consulta+" ");
		 
	    }
  return listado;
  }
  
  
  
  
  public static DtoInformeInconsisenBD getInformeInconsistencias(Connection con , HashMap parametros)
  {
	  DtoInformeInconsisenBD informe = new DtoInformeInconsisenBD();
	  try{
		  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaInfomeInconsitencias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
		  ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigopk_info_inco").toString()));
		  ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());	
		  if(rs.next())
			{
			  logger.info("\n\nEXISTE UN INFORME  >> ");
			  		
				informe.setCodigoPk(rs.getInt("codigopk_info_inco"));
				informe.setConsecutivoIngreso(rs.getString("consecutivo"));
				informe.setEstadoInforme(rs.getString("estado"));
				informe.setFechaGeneracion(rs.getString("fecha_generacion"));
			    informe.setHoraGeneracion(rs.getString("hora_generacion"));
			    informe.getConveniosPaciente().add(new InfoDatosString(rs.getString("cod_min_salud_convenio"),rs.getString("nombre_convenio")));
			    informe.getConvenio().setNombre(rs.getString("nombre_convenio"));
			    informe.getConvenio().setCodigo(rs.getString("cod_min_salud_convenio"));
			    informe.getTipoInconsistenciaInfo().setCodigo(rs.getInt("cod_tip_inconsistencia"));
			    informe.getTipoInconsistenciaInfo().setNombre(rs.getString("nombre_tip_inconsistencia"));
			    informe.setPrimerApellido(rs.getString("p_apellido"));
				informe.setSegundoApellido(rs.getString("s_apellido"));
			    informe.setPrimerNombre(rs.getString("p_nombre"));
				informe.setSegundoNombre(rs.getString("s_nombre"));
				informe.setIdPersona(rs.getString("num_identificacion"));
				informe.setTipoIdentificacion(rs.getString("tipo_identificacion"));
				informe.setFechaNacimiento(rs.getString("fecha_nacimiento"));
				informe.setDireccionResidencia(rs.getString("dir_residencia"));
				informe.setTelefono(rs.getString("telefono"));
				informe.setDepartamento(rs.getString("nom_depto"));
				informe.setCodigoDepartamento(rs.getString("cod_depto"));
				informe.setMunicipio(rs.getString("nom_municipio"));
				informe.setCodigoMunicipio(rs.getString("cod_municipio"));
				informe.setCobertura(rs.getString("nom_cobertura_salud"));
				informe.setCodCobertura(rs.getInt("cod_cobertura_salud"));
			    informe.setObservaciones(rs.getString("observaciones"));
			    informe.setUsuarioGeneracion(rs.getString("nombre_usua_gene"));
			    informe.setTelefonoPer(rs.getString("telfono_usua_gene"));
			    informe.setTelefonoCelularPer(rs.getString("telfono_celular_usua_gene"));
			    informe.setCargo(rs.getString("nombre_cargo"));
			    // se carga las variable incorrectas		    
			    informe.setVariablesIncorrectas(getVariableIncorrectas(con,parametros));
			    logger.info("\n\nCadena de consulta Informe Inconsistencias>> "+strConsultaInfomeInconsitencias+" "+parametros.get("codigopk_info_inco").toString());
			    rs.close();
			    ps.close();
			}
	  }catch(Exception e){
		  logger.info("Error en la Consulta Informe Inconsistencias>> "+strConsultaInfomeInconsitencias);
	  }
	  return informe;
  }
  
 
  public static ArrayList<DtoVariablesIncorrectasenBD> getVariableIncorrectas(Connection con, HashMap parametros)
  {
	  ArrayList<DtoVariablesIncorrectasenBD> array = new ArrayList<DtoVariablesIncorrectasenBD>();
	  String cadena = consultarVariablesIncorrectasInformeIncoStr+" ORDER BY infovar.tipo_variable "+ConstantesBD.tipoOrdenamientoAscendente;
	  try{
		  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
		  ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigopk_info_inco").toString()));
		  ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());	
		  while(rs.next())
		  {
			DtoVariablesIncorrectasenBD dto = new DtoVariablesIncorrectasenBD(); 
			dto.setCodigoPk(rs.getInt("codigo"));
			dto.setCodigoInformeInconsistencia(rs.getInt("codinforme"));
			dto.setTipoVariable(rs.getInt("codvariable"));
			dto.setDescripcionVariable(rs.getString("descripcionvariable"));
			dto.setIndicador(rs.getString("indicador"));
			dto.setValor(rs.getString("valorbd"));
			array.add(dto);			 
		  }
		  rs.close();
		  ps.close();
	  }catch(Exception e){
		  logger.info("Error en la Consulta Variables Incorrectas >> "+cadena);
	  }
	  return array;
  }
  
  /**
   * 
   * @param con
   * @param parametros
   * @return
   */
  public static boolean actualizarPatharchivoIncoBD(Connection con, HashMap parametros)
  {
	 try{
		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarPathArchivoIncoBD,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		 ps.setString(1,parametros.get("patharchivo")+"");
	     ps.setInt(2, Utilidades.convertirAEntero(parametros.get("informe_inconsist")+""));
	     if(ps.executeUpdate()>0){
	    	 ps.close();
			 return true;
	     }
	     ps.close();
		 return false;
	 }catch (Exception e) {
		 logger.info("\n informe_inconsist :  >> "+parametros.get("informe_inconsist")+"");
	     logger.info("\n Va Ejecutar la actualizacion:  >> "+actualizarPathArchivoIncoBD);
	}
	return false;
  }
  
}


