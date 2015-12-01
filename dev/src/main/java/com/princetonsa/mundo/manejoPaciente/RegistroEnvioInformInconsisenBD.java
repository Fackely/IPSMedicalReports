package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.RegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEnvioInformInconsisenBD;
import com.princetonsa.mundo.PersonaBasica;


public class RegistroEnvioInformInconsisenBD {

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(RegistroEnvioInfAtencionIniUrg.class);	
	
	/**
	* 
	* */
	public static RegistroEnvioInformInconsisenBDDao getRegistroEnvioInformInconsisenBDDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnvioInformInconsisenBDDao();
	}
   
	
	/**
	 * Valida si el paciente es valido
	 * @param Connection con
	 * @param PersonaBasica paciente
	 * */
	public static ActionErrors validarPaciente(Connection con,PersonaBasica paciente, int institucion)
	{
		ActionErrors errores = new ActionErrors();
		
		if(paciente.getCodigoPersona()<1)
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No hay ningún paciente cargado. Para acceder a esta funcionalidad por el Flujo de Paciente debe cargar un paciente"));
		else
		{
			//if(!tienePacienteIngresoValoracionUrg(con, institucion,paciente.getCodigoPersona()))
			//	errores.add("descripcion",new ActionMessage("errors.notEspecific","No hay ningun paciente cargado. Para acceder a esta funcionalidad por el Flujo de Paciente debe cargar un paciente"));			
		}	
		
		return errores;
	}
	
	/**
	 * Realiza la consulta de Ingresos asociados a un paciente
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarIngresosPaciente(Connection con, int codigoInstitucion, int codigoPaciente)
	{
		return getRegistroEnvioInformInconsisenBDDao().consultarIngresosPaciente(con, codigoInstitucion, codigoPaciente);
	
	}
	
	/**
	 * Realiza la conslta de Convenios Responsables asociados a un ingreso
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static  HashMap consultarConveniosResponsables(Connection con,int codigoIngreso)
	{
		return getRegistroEnvioInformInconsisenBDDao().consultarConveniosResponsables(con, codigoIngreso);
	}
	
	/**
	 * Realiza la consulta de un Informe de Incosistencias en BD asociado a un Ingreso y convenio Responsable
	 * devuelve el Dto de informe para ser cargado  
	 * @param con
	 * @param codIngreso
	 * @param codigoInstitucion
	 * @param codigoPaciente
	 * @param codConvenio
	 * @return
	 */
	public static  DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con, String codIngreso, int codigoInstitucion, int codigoPaciente, String codConvenio, String codSubcuenta,String cuenta){
		
		HashMap filtros=new HashMap();	
		filtros.put("codingreso", codIngreso);
		filtros.put("codpaciente", codigoPaciente);
		filtros.put("codconvenio", codConvenio);
		filtros.put("codinstitucion", codigoInstitucion);
		filtros.put("codSubcuenta", codSubcuenta);
		filtros.put("cuenta",cuenta);
		
		return getRegistroEnvioInformInconsisenBDDao().cargarInformeIncosistencias(con, filtros);
	}
	
	
	/**
	 * Realiza la consulta de los tipo de Inconsistencias existentes en el sistema
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap  cargarTiposInconsistencias(Connection con, int codigoInstitucion)
	{
		return getRegistroEnvioInformInconsisenBDDao().cargarTiposInconsistencias(con,codigoInstitucion);
	}
	
	
	/**
	 * Realiza la consulta de todos los tipos de Documentos, este metodo devuelve un Mapa 
	 * @param con
	 * @return
	 */
	public static ArrayList cargarTiposDocumentos(Connection con)
	{
		return getRegistroEnvioInformInconsisenBDDao().cargarTiposDocumentos(con);
	}
	
	
	/**
	 * Realiza la consulta de los Tipos de Documentos que no manejan consecutivo automantico este
	 * metodo devuelve un ArrayList
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public static ArrayList tiposDocumentoSinConsecutivo(Connection con, int codInstitucion)
	{
	  return Utilidades.obtenerTiposIdentificacion(con, "ingresoPoliza", codInstitucion);
	}
	
	
	/**
	 * Realiza la consulta de las ciudades 
	 * @param con
	 * @return
	 */
	public static HashMap cargarCiudades(Connection con)
	{
		return SqlBaseRegistroEnvioInformInconsisenBDDao.cargarCiudades(con);
	}
	
	/**
	 * Realiza la Consulta de los departamentos 
	 * @param con
	 * @return
	 */
	public static HashMap cargarDepartamentos(Connection con)
	{
		return SqlBaseRegistroEnvioInformInconsisenBDDao.cargarDepartamentos(con);
	}
	
	/**
	 * Realiza la consulta de las ciudades de un departamento
	 * @param con
	 * @param codDepartamento
	 * @return
	 */
	 public static HashMap cargarCiudadesDepto(Connection con, String codDepartamento)
	 {
		 return SqlBaseRegistroEnvioInformInconsisenBDDao.cargarCiudadesDepto(con,codDepartamento);
	 }
	 
	 /**
	  * Realiza la consulta de las posibles Variables Incorrectas del sistema 
	  * @param con
	  * @return
	  */
	 public static HashMap cargarVariablesIncorrectas(Connection con)
	 {
		 return SqlBaseRegistroEnvioInformInconsisenBDDao.cargarVariablesIncorrectas(con);
	 }

 /**
  * Realiza la insercion de los datos registrados para generar el informe de Inconsistencias
  * @param con
  * @param informeInconsistencias
  * @param codigoInstitucionInt
  * @param loginUsuario
  * @param codIngreso
  * @param codConvenio
  * @return
  */
	public HashMap insertarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias,int codigoInstitucionInt, String loginUsuario,String codIngreso, String codConvenio, String codSubcuenta,HashMap datosPaciente) {
		
		HashMap parametros=new HashMap();	
		parametros.put("codinstitucion", codigoInstitucionInt);
		parametros.put("loginUsuario", loginUsuario);
		parametros.put("codIngreso",codIngreso);
		parametros.put("codConvenio", codConvenio);
		parametros.put("codSubcuenta",codSubcuenta);
		parametros.put("primernombre",datosPaciente.get("primernombre").toString());
		parametros.put("segundonombre",datosPaciente.get("segundonombre").toString());
		parametros.put("primerapellido",datosPaciente.get("primerapellido").toString());
		parametros.put("segundoapellido",datosPaciente.get("segundoapellido").toString());
		parametros.put("tipodocumento",datosPaciente.get("tipodocumento").toString());
		parametros.put("numeroidentificacion",datosPaciente.get("numeroidentificacion").toString());
		parametros.put("fechanacimiento",datosPaciente.get("fechanacimiento").toString());
		
		return getRegistroEnvioInformInconsisenBDDao().insertarInformeInconsistencia(con,informeInconsistencias,parametros);
		
	}
	
	/**
	 * Realiza la insercion de los datos de envio del Informe
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap insertarEnvioInformeInconsistencias(Connection con,HashMap parametros)
	{
		return getRegistroEnvioInformInconsisenBDDao().insertarEnvioInformeInconsistencias(con,parametros);
	}
	/**
	 * Realiza la consulta de un Informe y verifica si ya se ha enviado
	 * @param con
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public int consultarEstadoEnvioInforme(Connection con,String ingreso, String convenio)
	{
		return SqlBaseRegistroEnvioInformInconsisenBDDao.consultarEstadoEnvioInforme(con,ingreso,convenio);
	}

   /**
    * Realiza la modificacion de un Informe existente, siempre y cuando este en estado Generado
    * @param con
    * @param informeInconsistencias
    * @param codigoInstitucionInt
    * @param loginUsuario
    * @param codIngreso
    * @param codConvenio
    * @param datosPaciente
    * @return
    */
	public HashMap modificarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias,int codigoInstitucionInt, String loginUsuario,String codIngreso, String codConvenio,HashMap datosPaciente) {
		HashMap parametros=new HashMap();	
		parametros.put("codinstitucion", codigoInstitucionInt);
		parametros.put("loginUsuario", loginUsuario);
		parametros.put("codIngreso",codIngreso);
		parametros.put("codConvenio", codConvenio);
		parametros.put("primernombre",datosPaciente.get("primernombre").toString());
		parametros.put("segundonombre",datosPaciente.get("segundonombre").toString());
		parametros.put("primerapellido",datosPaciente.get("primerapellido").toString());
		parametros.put("segundoapellido",datosPaciente.get("segundoapellido").toString());
		parametros.put("tipodocumento",datosPaciente.get("tipodocumento").toString());
		parametros.put("numeroidentificacion",datosPaciente.get("numeroidentificacion").toString());
		parametros.put("fechanacimiento",datosPaciente.get("fechanacimiento").toString());
		
		return getRegistroEnvioInformInconsisenBDDao().modificarInformeInconsistencia(con,informeInconsistencias,parametros);
	}
	
	/**
	 * Realiza la consulta de las variables incorrectas asociadas a un Informe
	 * @param con
	 * @param codInforme
	 * @return
	 */
	public String[] consultarVariablesIncorrectasInforme(Connection con, int codInforme)
	{
		return SqlBaseRegistroEnvioInformInconsisenBDDao.consultarVariablesIncorrectasInforme(con,codInforme);
	}

    /**
     * Realiza la consulta de los historicos ( Los envios que se han realizado a un informe) 
     * @param con
     * @param codInforme
     * @return
     */
	public ArrayList<DtoRegistroEnvioInformInconsisenBD> consultaHistoricosInforme(Connection con, int codInforme) {
		
		return SqlBaseRegistroEnvioInformInconsisenBDDao.consultaHistoricosInforme(con,codInforme);
	}
	
	
	
	public static HashMap consultarCoberturasSaludPaciente(Connection con, int codConvenio)
	  {
		return  SqlBaseRegistroEnvioInformInconsisenBDDao.consultarCoberturasSaludPaciente(con,codConvenio);
	  }
	
	
	/**
	 * Se carga el DtoInformeInconsisenBD con los datos requeridos para imprimir el informe de inconsistencias
	 * @param con
	 * @param codigo_info_inco
	 * @return DtoInformeInconsisenBD
	 */
	public static DtoInformeInconsisenBD getInformeInconsistencias(Connection con , int codigo_info_inco)
	{
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("codigopk_info_inco", codigo_info_inco);
		return SqlBaseRegistroEnvioInformInconsisenBDDao.getInformeInconsistencias(con, parametros);
	}
	
	/**
	   * 
	   * @param con
	   * @param parametros
	   * @return
	   */
	public boolean actualizarPatharchivoIncoBD(Connection con, String patharchivo, int cod_informe_inco)
	{
		HashMap parametros = new HashMap();
		parametros.put("patharchivo", patharchivo);
		parametros.put("informe_inconsist", cod_informe_inco);
		return getRegistroEnvioInformInconsisenBDDao().actualizarPatharchivoIncoBD(con, parametros);
	}
}
