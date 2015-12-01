package com.princetonsa.actionform.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;




/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class CensoCamasForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase censocamasform
	 */
	Logger logger = Logger.getLogger(CensoCamasForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	
	/*--------------------------------------------------
	 * 			ATRIBUTOS DEL CENSO DE CAMAS
	 ---------------------------------------------------*/
	/**
	 * HashMap CensoCamas; en este hashmap se almacena
	 * la respuesta a la consulta del censo de camas.
	 */
	private HashMap censoCamasMap;
	
	/**
	 * Hasmapdonde se almacenan las alertas
	 */
	private HashMap alertasMap;
	/**
	 * String estado; este es el encargado de administrar
	 * las acciones dentro del Action.
	 */
	private String estado;
	
	/**
	 * String indexCensoCamasMap; indica a cual indice del
	 * hashmap ir
	 */
	private String indexCensoCamasMap;
	
	
	/*--------------------------------------------------
	 * 			FIN ATRIBUTOS DEL CENSO DE CAMAS
	 ---------------------------------------------------*/
	
	
	/*---------------------------------------------------
	 * 	 ATRIBUTOS PARA LA BUSQUEDA DEL CENSO DE CAMAS
	 ---------------------------------------------------*/

	
	/**
	 * ArrayList de HashMap de Centros de Atencion, utilizado en la 
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencionMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * ArrayList de HashMap de Censtros de Costo, Utilizado en la
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String,Object>> centroCostoMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * ArrayList de Hasmap de Convenios, Utilizado en la
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String,Object>> conveniosMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * ArrayList de Hasmap de pisos, Utilizado en la
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String, Object>> pisosMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * ArrayList de Hasmap de estados de las camas, Utilizado en la
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String, Object>> estadosCamaMap = new ArrayList<HashMap<String,Object>>();	
	
	/**
	 * String donde se almacena el valor del criterio
	 * de busqueda Convenio.
	 */
	private String convenioId;
	
	/**
	 * String donde se almacena el valor del criterio
	 * de busqueda Centro de Atencion.
	 */
	private String centroAtencionId;
	
	/**
	 * String donde se almacena el valor del criterio
	 * de busqueda Centro de Costo.
	 */
	private String centroCostoId;
	
	/**
	 * String donde se almacena el valor del criterio
	 * de busqueda Piso.
	 */
	private String pisoId;
	
	/**
	 * String donde se almacena el valor del criterio
	 * de busqueda Estado de la Cama.
	 */
	private String [] estadoCamaId;
	
	/**
	 * String donde se almacena el valor del criterio
	 * de busqueda Tipo de Reporte.
	 */
	private String tipoReporte;
	
	/**
	 * String que indica por que tipo se va a hacer
	 * el rompimiento al momento de mostrar la informacion
	 */
	private String tipoRompimiento;
	
	
	/**
	 *String que indica a que estado se va a pasar la cama. 
	 */
	private String nuevoEstadoCama;
	
	private String tipoSalida ="";
	
	private String ruta="";
	
	private String urlArchivo="";
	
	private boolean existeArchivo=false;
	
	private boolean operacionTrue = false;
	
	private String mostrarResumen="";
	
	private String incluirAsignableAdmin="";
	/*---------------------------------------------------
	 * FIN ATRIBUTOS PARA LA BUSQUEDA DEL CENSO DE CAMAS
	 ---------------------------------------------------*/
	

	/*--------------------------------------------------------------
	 * ATRIBUTOS PARA EL MANEJO DE PERMISOS A OTRAS FUNCIONALIDADES
	 --------------------------------------------------------------*/
	/**
	 * Boleano que indica si el usuario tiene o no permiso 
	 * para imprimir el reporte.
	 */
	private boolean permisoImpresion;
	
	/**
	 * indica si se tiene el permiso de cambiar el estado
	 * a pendinete por remitir
	 */
	private boolean cambioPendientePorRemitir;
	
	/**
	 * indica si se tiene el permiso de cambiar el estado
	 * a pendinete por trasladar
	 */
	private boolean cambioPendientePorTrasladar;
	
	/**
	 *indica si tiene el permiso para cambiar el estado 
	 *de la cama a ocupado. 
	 */
	private boolean cambioOcupada;
	
	/**
	 * indica si se tiene el permiso para entrar a
	 * la funcionalidad traslado de camas
	 */
	private boolean accesoTrasladoCamas;
	
	/**
	 * indica si se tiene el permiso para entrar a
	 * la funcionalidad reserva de camas
	 */
	private boolean accesoReservaCamas;
	
	/**
	 * indica si tiene permiso para entrar 
	 * a la funcionalidad liberacion de camas
	 * reservadas
	 */
	private boolean accesoLiberCamRes;
	

	/*----------------------------------------------------------------
	 *FIN ATRIBUTOS PARA EL MANEJO DE PERMISOS A OTRAS FUNCIONALIDADES
	 ----------------------------------------------------------------*/
	
	
	/*-------------------------------------------------------
	 * 						METODOS
	 --------------------------------------------------------*/
	
	
	public boolean isAccesoLiberCamRes() {
		return accesoLiberCamRes;
	}


	public void setAccesoLiberCamRes(boolean accesoLiberCamRes) {
		this.accesoLiberCamRes = accesoLiberCamRes;
	}


	public String getTipoRompimiento() {
		return tipoRompimiento;
	}


	public void setTipoRompimiento(String tipoRompimiento) {
		this.tipoRompimiento = tipoRompimiento;
	}


	/*-------------------------------------------------------
	 * 					METODO VALIDATE
	 --------------------------------------------------------*/
	/**
	 * Metodo encargado de hacer las validaciones y retornarlas al jsp.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errors = new ActionErrors();
		errors = super.validate(mapping, request);
		
		
			
		return errors;
	}
	/*-------------------------------------------------------
	 * 					FIN METODO VALIDATE
	 --------------------------------------------------------*/

	
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	public HashMap getCensoCamasMap() {
		return censoCamasMap;
	}

	public void setCensoCamasMap(HashMap censoCamasMap) {
		this.censoCamasMap = censoCamasMap;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	

	public String getCentroAtencionId() {
		return centroAtencionId;
	}


	public void setCentroAtencionId(String centroAtencionId) {
		this.centroAtencionId = centroAtencionId;
	}


	public String getCentroCostoId() {
		return centroCostoId;
	}


	public void setCentroCostoId(String centroCostoId) {
		this.centroCostoId = centroCostoId;
	}


	public ArrayList<HashMap<String, Object>> getCentroCostoMap() {
		return centroCostoMap;
	}


	public void setCentroCostoMap(ArrayList<HashMap<String, Object>> centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}


	public ArrayList<HashMap<String, Object>> getCentrosAtencionMap() {
		return centrosAtencionMap;
	}


	public void setCentrosAtencionMap(
			ArrayList<HashMap<String, Object>> centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}


	public String getConvenioId() {
		return convenioId;
	}


	public void setConvenioId(String convenioId) {
		this.convenioId = convenioId;
	}


	public ArrayList<HashMap<String, Object>> getConveniosMap() {
		return conveniosMap;
	}


	public void setConveniosMap(ArrayList<HashMap<String, Object>> conveniosMap) {
		this.conveniosMap = conveniosMap;
	}


	

	public ArrayList<HashMap<String, Object>> getEstadosCamaMap() {
		return estadosCamaMap;
	}


	public void setEstadosCamaMap(ArrayList<HashMap<String, Object>> estadosCamaMap) {
		this.estadosCamaMap = estadosCamaMap;
	}


	public String getPisoId() {
		return pisoId;
	}


	public void setPisoId(String pisoId) {
		this.pisoId = pisoId;
	}


	public ArrayList<HashMap<String, Object>> getPisosMap() {
		return pisosMap;
	}


	public void setPisosMap(ArrayList<HashMap<String, Object>> pisosMap) {
		this.pisosMap = pisosMap;
	}


	public String getTipoReporte() {
		return tipoReporte;
	}


	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	
	
	
	public String getIndexCensoCamasMap() {
		return indexCensoCamasMap;
	}


	public void setIndexCensoCamasMap(String indexCensoCamasMap) {
		this.indexCensoCamasMap = indexCensoCamasMap;
	}
	
	
	public boolean isPermisoImpresion() {
		return permisoImpresion;
	}


	public void setPermisoImpresion(boolean permisoImpresion) {
		this.permisoImpresion = permisoImpresion;
	}
	
	
	public boolean isAccesoReservaCamas() {
		return accesoReservaCamas;
	}


	public void setAccesoReservaCamas(boolean accesoReservaCamas) {
		this.accesoReservaCamas = accesoReservaCamas;
	}


	public boolean isAccesoTrasladoCamas() {
		return accesoTrasladoCamas;
	}


	public void setAccesoTrasladoCamas(boolean accesoTrasladoCamas) {
		this.accesoTrasladoCamas = accesoTrasladoCamas;
	}


	public boolean isCambioPendientePorRemitir() {
		return cambioPendientePorRemitir;
	}


	public void setCambioPendientePorRemitir(boolean cambioPendientePorRemitir) {
		this.cambioPendientePorRemitir = cambioPendientePorRemitir;
	}


	public boolean isCambioPendientePorTrasladar() {
		return cambioPendientePorTrasladar;
	}


	public void setCambioPendientePorTrasladar(boolean cambioPendientePorTrasladar) {
		this.cambioPendientePorTrasladar = cambioPendientePorTrasladar;
	}

	

	public String getNuevoEstadoCama() {
		return nuevoEstadoCama;
	}


	public void setNuevoEstadoCama(String nuevoEstadoCama) {
		this.nuevoEstadoCama = nuevoEstadoCama;
	}

	public boolean isCambioOcupada() {
		return cambioOcupada;
	}


	public void setCambioOcupada(boolean cambioOcupada) {
		this.cambioOcupada = cambioOcupada;
	}
	
	

	public HashMap getAlertasMap() {
		return alertasMap;
	}

	public Object getAlertasMap (String key)
	{
		return this.alertasMap.get(key);
	}

	public void setAlertasMap(HashMap alertasMap) {
		this.alertasMap = alertasMap;
	}
	
	public void setAlertasMap (String key,Object value)
	{
		this.alertasMap.put(key, value);
	}
	
	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	
	/*----------------------------------------------------------
	 * 			METODOS ADICIONALES PARA LOS HASHMAP
	 ----------------------------------------------------------*/
	
	/**
	 * Metodo que retorna el valor en el hashmap
	 * censoCamasMap del key que se indique.
	 * @param key
	 * @return Object 
	 **/
	public Object getCensoCamasMap(String key)
	{
		return this.censoCamasMap.get(key);
	}
	
	/**
	 * Metodo que ingresa en el HashMap
	 * censoCamasMap un valor mediante el key y el valor.
	 * @param key
	 * @param value
	 */
	public void setCensoCamasMap (String key,Object value)
	{
		this.censoCamasMap.put(key, value);
	}

	
	/*----------------------------------------------------------
	 * 		  FIN METODOS ADICIONALES PARA LOS HASHMAP
	 ----------------------------------------------------------*/
	
	
	/*-----------------------------------------------------------
	 * 			  METODOS PARA EL MANEJO DE LA FORMA
	 -----------------------------------------------------------*/
	
	/**
	 * Metodo que crea las instancias nuevas 
	 * de busquedaCensoCamasMap y de censoCamasMap.
	 */
	public void resetCensoCamas ()
	{
		this.centroAtencionId = "";
		this.centroCostoId = "";
		this.convenioId = "";
		this.estadoCamaId = new String []{""};
		this.pisoId = "";
		this.tipoReporte = "";
		this.tipoRompimiento = "";
		this.accesoReservaCamas=false;
		this.accesoTrasladoCamas=false;
		this.accesoLiberCamRes=false;
		this.cambioPendientePorRemitir=false;
		this.cambioPendientePorTrasladar=false;
		this.nuevoEstadoCama = "";
		this.cambioOcupada=false;
		this.tipoSalida="";
		this.urlArchivo="";
		this.ruta="";
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.mostrarResumen=ConstantesBD.acronimoSi;
		this.incluirAsignableAdmin="";
		
			
	}
	
	public void resetDescarga ()
	{
		this.urlArchivo="";
		this.ruta="";
		this.existeArchivo=false;
		this.operacionTrue=false;
	}
	
	/**
	 * inicializa la consulta del censo.
	 *
	 */
	public void resetconsulta ()
	{
		this.censoCamasMap = new HashMap ();
		this.setCensoCamasMap("numRegistros", 0);
	}
	/**
	 * Metodo que inicializa los valores
	 * para el trabajo con el paginador.
	 */
	public void resetpager ()
	{
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
	}

	public void resetAlertas ()
	{
		this.alertasMap = new HashMap();
		this.setAlertasMap("numRegistros",0);
	}

	
	/*-----------------------------------------------------------
	 * 			 FIN METODOS PARA EL MANEJO DE LA FORMA
	 -----------------------------------------------------------*/
	
	
	/*------------------------------------------------------------
	 * 	METODO PARA CARGAR LOS ARRAYLIST DE HASHMAP POR DEFECTO
	 ------------------------------------------------------------*/
	
	
	public void initBusquedaCensoCamas (Connection connection,int institucion, int centroAtencion)
	{
		HashMap parametros = new HashMap ();
		//se cargan todos los convenios existentes.
		this.setConveniosMap(Utilidades.obtenerConvenios(connection, "", "", false, "", true));
		//se cargan todos los centros de atencion de la institucion.
		this.setCentrosAtencionMap(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, institucion,""));
		//se cargan todos los centros de costo de la institucion.
		this.setCentroCostoMap(UtilidadesManejoPaciente.obtenerCentrosCosto(connection, institucion, "", true, centroAtencion,true+""));
		//parametros para la carga de pisos
		parametros.put("institucion", institucion);
		parametros.put("centroatencion", centroAtencion);
		this.setPisosMap(UtilidadesManejoPaciente.obtenerPisos(connection, parametros));
		//se cargan todos los estados de la cama.
		this.setEstadosCamaMap(UtilidadesManejoPaciente.obtenerEstadosCama(connection));
		
		this.estadoCamaId = new String [this.estadosCamaMap.size()];
		if (this.estadosCamaMap.size()>0)
			this.estadoCamaId [0]="";
		//se inicializa el estado del tipo de reporte en --> detallado
		this.setTipoReporte("detallado");
		this.setTipoRompimiento("codigocentrocosto");
		
	}


	public String[] getEstadoCamaId() {
		return estadoCamaId;
	}


	public void setEstadoCamaId(String[] estadoCamaId) {
		this.estadoCamaId = estadoCamaId;
	}


	public String getTipoSalida() {
		return tipoSalida;
	}


	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	public String getRuta() {
		return ruta;
	}


	public void setRuta(String ruta) {
		this.ruta = ruta;
	}


	public String getUrlArchivo() {
		return urlArchivo;
	}


	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}


	public boolean isExisteArchivo() {
		return existeArchivo;
	}


	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}


	public boolean isOperacionTrue() {
		return operacionTrue;
	}


	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}




	public String getMostrarResumen() {
		return mostrarResumen;
	}


	public void setMostrarResumen(String mostrarResumen) {
		this.mostrarResumen = mostrarResumen;
	}


	public String getIncluirAsignableAdmin() {
		return incluirAsignableAdmin;
	}


	public void setIncluirAsignableAdmin(String incluirAsignableAdmin) {
		this.incluirAsignableAdmin = incluirAsignableAdmin;
	}


	



	/*------------------------------------------------------------
	 * FIN METODO PARA CARGAR LOS ARRAYLIST DE HASHMAP POR DEFECTO
	 ------------------------------------------------------------*/
	
	
	/*-------------------------------------------------------
	 * 					FIN METODOS
	 --------------------------------------------------------*/
	
}

