package util.integracion.dtoIntegracion;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import com.princetonsa.dto.historiaClinica.DtoIngresoHistoriaClinica;
import com.princetonsa.mundo.InstitucionBasica;

/**
* Clase  ComandoPeticion, reune  los atributos  necesarios  para  poder  utilizar  el  contexto de  reportes.<br><br>
* <b>Fecha:<b> 30/05/2013
* @author: Jesrioro
* 
*/
public class ComandoPeticion  implements Serializable{
	/**
	 * Version actual de esta clase para serializacion
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo para  generar  logs  en  esta  clase
	 */
	private transient Logger logger = Logger.getAnonymousLogger();	
	/**
	 * Cadena  que  almacena  el  valor  del queryString  del  contexto  que envia  la peticion  
	 */
	private String queryString;
	/**
	 * Cadena  que  almacena  el  path del Action al que se  enviarán los datos  para  generar  el  reporte
	 */
	private String pathInfo;
	/**
	 * Objeto  que  permite  almacenar  la  forma  ImpresionResumenAtenciones para  enviarla  al  contexto de  reportes
	 */
	private Object formulario;
	/**
	 * Cadena para  hacer  unica  la  peticion del  reporte
	 */
	private String clave;
	/**
	 * Cadena  en  la  que  se  almacena  la  respuesta  del  contexto de  reportes
	 */
	private String respuesta;
	/**
	 * Objeto  para  almacenar  el  Usuario
	 */
	private Object usuario;
	/**
	 * Objeto  para  almacenar  el  Paciente
	 */
	private Object paciente;
	/**
	 * Objeto  que  almacena  la  lista  de  DTO  que se  enviará  al  contexto  de  reportes.
	 */
	private Object listaDtoHc;
	/**
	 * Objeto  que  almacena  la  Institucion Actual para  enviarla  al  contexto de  reportes
	 */
	private Object institucionActual;
		
	/**
	 * Constructor por defecto, la clave es un dato obligatorio para que la sincronizacion con el contenedor
	 * alterno funcione.
	 * @param clave
	 */
	ComandoPeticion(String clave)
	{
		this.clave = clave;
	}

	/**
	 * Metodo para  Obtener  el  logger de  la  clase
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Metodo para  asignar  el  logger de  la  clase
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Metodo para  Obtener  el  queryString de  la  clase
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * Metodo para  asignar  el  queryString de  la  clase
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Metodo para  Obtener  el  pathInfo de  la  clase
	 * @return the pathInfo
	 */
	public String getPathInfo() {
		return pathInfo;
	}

	/**
	 *  Metodo para  Asignar  el  pathInfo de  la  clase
	 * @param pathInfo the pathInfo to set
	 */
	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	/**
	 *  Metodo para  Obtener  el  formulario de  la  clase
	 * @return the formulario
	 */
	public Object getFormulario() {
		return formulario;
	}

	/**
	 *  Metodo para  Asignar  el  formulario de  la  clase
	 * @param formulario the formulario to set
	 */
	public void setFormulario(Object formulario) {
		this.formulario = formulario;
	}

	/**
	 * Metodo para  Obtener  la  clave de  la  clase
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * Metodo para  Asignar  la  clave de  la  clase
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * Metodo para  Obtener  la  Respuesta de  la  clase
	 * @return the respuesta
	 */
	public String getRespuesta() {
		return respuesta;
	}

	/**
	 * Metodo para  Asignar  la  Respuesta de  la  clase
	 * @param respuesta the respuesta to set
	 */
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	/**
	 * Metodo para  Obtener  el Usuario de  la  clase
	 * @return the usuario
	 */
	public Object getUsuario() {
		return usuario;
	}

	/**
	 * Metodo para  Obtener  el Usuario  de  la  clase
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Object usuario) {
		this.usuario = usuario;
	}

	/**
	 * Metodo para  Obtener  el Paciente de  la  clase
	 * @return the paciente
	 */
	public Object getPaciente() {
		return paciente;
	}

	/**
	 * Metodo para  Asignar el paciente  de  la  clase
	 * @param paciente the paciente to set
	 */
	public void setPaciente(Object paciente) {
		this.paciente = paciente;
	}

	/**
	 * Metodo para  Obtener  la  lista de  DTO de  la  clase
	 * @return the listaDtoHc
	 */
	public Object getListaDtoHc() {
		return listaDtoHc;
	}

	/**
	 * Metodo para  Asignar  la  lista de  DTO de  la  clase
	 * @param listaDtoHc the listaDtoHc to set
	 */
	public void setListaDtoHc(Object listaDtoHc) {
		this.listaDtoHc = listaDtoHc;
	}

	/**
	 * Metodo para  Obtener  la  Institucion de  la  clase
	 * @return the institucionActual
	 */
	public Object getInstitucionActual() {
		return institucionActual;
	}

	/**
	 * Metodo para  Asignar  la  Institucion de  la  clase
	 * @param institucionActual the institucionActual to set
	 */
	public void setInstitucionActual(Object institucionActual) {
		this.institucionActual = institucionActual;
	}
	
	
	
	
	
	
	
	
}
