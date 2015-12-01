package com.princetonsa.actionform.manejoPaciente;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;

import java.util.ArrayList;
import java.util.HashMap;


public class PacientesEntidadesSubConForm extends ValidatorForm
{
	//-----Atributos
	//*****Atributos Generales o de Administracion
	
	/**
	 * String estado
	 * */
	private String estado;
		
	/**
	 * ArrayList TiposIdentificacion
	 * */
	private ArrayList tiposIdentificacionList;	
	
	/**
	 *  HashMap Zonas Domicilios
	 * */
	private HashMap zonasDomiciliosMap;
	
	/**
	 * HashMap ocupacionesMap
	 * */
	private HashMap ocupacionesMap;
	
	/**
	 * ArrayList Sexos
	 * */
	private ArrayList sexosList;
	
	/**
	 * ArrayList Paises
	 * */
	private ArrayList paisesList;
	
	/**
	 * ArrayList ciudadExpedicion
	 * */
	private ArrayList ciudadExpedicionList;
	
	/**
	 * ArrayList ciudadNacimiento
	 * */
	private ArrayList ciudadNacimientoList;
	
	/**
	 * ArrayList ciudadResidencia
	 * */
	private ArrayList ciudadResidenciaList;
	
	/**
	 * ArrayList entidadesSubContratadas
	 * */
	private ArrayList entidadesSubContratadasList;
	
	/**
	 * ArrayList convenios
	 * */
	private ArrayList conveniosList;
	
	/**
	 * ArrayList ContratosList
	 * */
	private ArrayList usuariosList;
	
	/**
	 * ArrayList Usuarios Pacientes Entidades SubContratadas
	 * */
	private ArrayList contratoList;
	
	/**
	 * String codigo Pais, codigo del pais que sirve de busqueda
	 * */
	private String codigoPais;
		
	/**
	 * String indicador del componente en jsp donde se llena la busqueda
	 * */
	private String indicadorComponenteBusqueda;
	
	/**
	 * String indicador del flujo de la busqueda
	 * */
	private String indicadorFlujo;
	
	/**
	 * String indicador del codigo del convenio de Busqueda
	 * */
	private String indicadorConvenioBusqueda;
	
	/**
	 * int indexEliminado
	 * */
	private int indexEliminado;
	
	/**
	 * int indexImpresion
	 */
	private int indexImpresion;
	
	/**
	 * Mapa donde se almacenan los datos del usuario capitado que todavía no existe en la base de datos
	 */
	private HashMap datosCapitacionMap;
	
	/**
     * Almacena el numero de autorización pedido para el caso en que  
     * el parametros 'Realizar Comprobacion solo en cargues vigentes' este en Si 
     * */
    private InfoDatosString autorizacionIngEvento = new InfoDatosString("","");
	
	
	
	//********************************************
	
	//*******Atributos del mundo
	
	/**
	 * HashMap que contiene los datos personales del Paciente 
	 * */
	private HashMap datosPacienteMap;	
	
	/**
	 * HashMap validacionesMap
	 * */
	private HashMap validacionesMap;
	
	/**
	 * HashMap resgitroEntidadesSub
	 * */
	private HashMap registroEntidadesSubMap;
	
	/**
	 * HashMap serviciosAutorizados
	 * */
	private HashMap serviciosAutorizadosMap;
	
	/**
	 * HashMap  Servicios Autorizados Eliminados
	 * */
	private HashMap serviciosAutorizadosEliminadosMap;

	//**************************
	
	//-----Fin Atributos
	
	
	//------Medodos
	
	
	/**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no recorded
     * error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
    */
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request);
    	
    	return errores;
	}	
	
	
	/**
	 * Inicializa los valores de la forma
	 * */
	public void reset()
	{
		this.datosPacienteMap = new HashMap();
		this.tiposIdentificacionList = new ArrayList();		
		this.zonasDomiciliosMap = new HashMap();
		this.ocupacionesMap = new HashMap();
		this.sexosList = new ArrayList();
		this.validacionesMap = new HashMap();
		this.paisesList = new ArrayList();		
		this.ciudadExpedicionList = new ArrayList();
		this.ciudadNacimientoList = new ArrayList();
		this.ciudadResidenciaList = new ArrayList();	
		this.registroEntidadesSubMap = new HashMap();
		this.serviciosAutorizadosMap = new HashMap();
		this.entidadesSubContratadasList = new ArrayList();
		this.serviciosAutorizadosEliminadosMap = new HashMap();
		this.conveniosList = new ArrayList();
		this.contratoList = new ArrayList();
		this.usuariosList = new ArrayList();
		
		this.indexImpresion = ConstantesBD.codigoNuncaValido;
		
		this.datosCapitacionMap = new HashMap();
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the datosPacienteMap
	 */
	public HashMap getDatosPacienteMap() {
		return datosPacienteMap;
	}


	/**
	 * @param datosPacienteMap the datosPacienteMap to set
	 */
	public void setDatosPacienteMap(HashMap datosPacienteMap) {
		this.datosPacienteMap = datosPacienteMap;
	}
	
	
	public String getTipoIdentificacion()
	{
		return datosPacienteMap.get("tipoIdentificacion").toString();
	}
	
	public void setTipoIdentificacion(String value)
	{
		datosPacienteMap.put("tipoIdentificacion",value);
	}
	
	public String getNumeroIdentificacion()
	{
		return datosPacienteMap.get("numeroIdentificacion").toString();
	}
	
	public void setNumeroIdentificacion(String value)
	{
		datosPacienteMap.put("numeroIdentificacion",value);
	}	
	
	/**
	 * @return the datosPacienteMap
	 */
	public Object getDatosPacienteMap(String key) {
		return datosPacienteMap.get(key);
	}


	/**
	 * @param datosPacienteMap the datosPacienteMap to set
	 */
	public void setDatosPacienteMap(String key, Object value) {
		this.datosPacienteMap.put(key, value);
	}


	/**
	 * @return the tiposIdentificacionList
	 */
	public ArrayList getTiposIdentificacionList() {
		return tiposIdentificacionList;
	}


	/**
	 * @param tiposIdentificacionList the tiposIdentificacionList to set
	 */
	public void setTiposIdentificacionList(ArrayList tiposIdentificacionList) {
		this.tiposIdentificacionList = tiposIdentificacionList;
	}


	/**
	 * @return the paisesList
	 */
	public ArrayList getPaisesList() {
		return paisesList;
	}


	/**
	 * @param paisesList the paisesList to set
	 */
	public void setPaisesList(ArrayList paisesList) {
		this.paisesList = paisesList;
	}


	/**
	 * @return the zonasDomiciliosMap
	 */
	public HashMap getZonasDomiciliosMap() {
		return zonasDomiciliosMap;
	}


	/**
	 * @param zonasDomiciliosMap the zonasDomiciliosMap to set
	 */
	public void setZonasDomiciliosMap(HashMap zonasDomiciliosMap) {
		this.zonasDomiciliosMap = zonasDomiciliosMap;
	}
	
	/**
	 * @return the zonasDomiciliosMap
	 */
	public Object getZonasDomiciliosMap(String key) {
		return zonasDomiciliosMap.get(key);
	}


	/**
	 * @param zonasDomiciliosMap the zonasDomiciliosMap to set
	 */
	public void setZonasDomiciliosMap(String key, Object value) {
		this.zonasDomiciliosMap.put(key, value);
	}


	/**
	 * @return the ocupacionesMap
	 */
	public HashMap getOcupacionesMap() {
		return ocupacionesMap;
	}


	/**
	 * @param ocupacionesMap the ocupacionesMap to set
	 */
	public void setOcupacionesMap(HashMap ocupacionesMap) {
		this.ocupacionesMap = ocupacionesMap;
	}
	
	
	/**
	 * @return the ocupacionesMap
	 */
	public Object getOcupacionesMap(String key) {
		return ocupacionesMap.get(key);
	}


	/**
	 * @param ocupacionesMap the ocupacionesMap to set
	 */
	public void setOcupacionesMap(String key, Object value) {
		this.ocupacionesMap.put(key, value);
	}


	/**
	 * @return the sexosList
	 */
	public ArrayList getSexosList() {
		return sexosList;
	}


	/**
	 * @param sexosList the sexosList to set
	 */
	public void setSexosList(ArrayList sexosList) {
		this.sexosList = sexosList;
	}


	/**
	 * @return the validacionesMap
	 */
	public HashMap getValidacionesMap() {
		return validacionesMap;
	}


	/**
	 * @param validacionesMap the validacionesMap to set
	 */
	public void setValidacionesMap(HashMap validacionesMap) {
		this.validacionesMap = validacionesMap;
	}
	
	/**
	 * @return the validacionesMap
	 */
	public Object getValidacionesMap(String key) {
		return validacionesMap.get(key);
	}


	/**
	 * @param validacionesMap the validacionesMap to set
	 */
	public void setValidacionesMap(String key, Object value) {
		this.validacionesMap.put(key, value);
	}

	/**
	 * @return the ciudadExpedicionList
	 */
	public ArrayList getCiudadExpedicionList() {
		return ciudadExpedicionList;
	}


	/**
	 * @param ciudadExpedicionList the ciudadExpedicionList to set
	 */
	public void setCiudadExpedicionList(ArrayList ciudadExpedicionList) {
		this.ciudadExpedicionList = ciudadExpedicionList;
	}


	/**
	 * @return the ciudadNacimientoList
	 */
	public ArrayList getCiudadNacimientoList() {
		return ciudadNacimientoList;
	}


	/**
	 * @param ciudadNacimientoList the ciudadNacimientoList to set
	 */
	public void setCiudadNacimientoList(ArrayList ciudadNacimientoList) {
		this.ciudadNacimientoList = ciudadNacimientoList;
	}


	/**
	 * @return the ciudadResidenciaList
	 */
	public ArrayList getCiudadResidenciaList() {
		return ciudadResidenciaList;
	}


	/**
	 * @param ciudadResidenciaList the ciudadResidenciaList to set
	 */
	public void setCiudadResidenciaList(ArrayList ciudadResidenciaList) {
		this.ciudadResidenciaList = ciudadResidenciaList;
	}


	/**
	 * @return the registroEntidadesSubMap
	 */
	public HashMap getRegistroEntidadesSubMap() {
		return registroEntidadesSubMap;
	}


	/**
	 * @param registroEntidadesSubMap the registroEntidadesSubMap to set
	 */
	public void setRegistroEntidadesSubMap(HashMap registroEntidadesSubMap) {
		this.registroEntidadesSubMap = registroEntidadesSubMap;
	}
	
	
	/**
	 * @return the registroEntidadesSubMap
	 */
	public Object getRegistroEntidadesSubMap(String key) {
		return registroEntidadesSubMap.get(key);
	}


	/**
	 * @param registroEntidadesSubMap the registroEntidadesSubMap to set
	 */
	public void setRegistroEntidadesSubMap(String key, Object value) {
		this.registroEntidadesSubMap.put(key, value);
	}


	/**
	 * @return the serviciosAutorizadosMap
	 */
	public HashMap getServiciosAutorizadosMap() {
		return serviciosAutorizadosMap;
	}


	/**
	 * @param serviciosAutorizadosMap the serviciosAutorizadosMap to set
	 */
	public void setServiciosAutorizadosMap(HashMap serviciosAutorizadosMap) {
		this.serviciosAutorizadosMap = serviciosAutorizadosMap;
	}
	
	/**
	 * @return the serviciosAutorizadosMap
	 */
	public Object getServiciosAutorizadosMap(String key) {
		return serviciosAutorizadosMap.get(key);
	}


	/**
	 * @param serviciosAutorizadosMap the serviciosAutorizadosMap to set
	 */
	public void setServiciosAutorizadosMap(String key, Object value) {
		this.serviciosAutorizadosMap.put(key, value);
	}


	/**
	 * @return the entidadesSubContratadas
	 */
	public ArrayList getEntidadesSubContratadasList() {
		return entidadesSubContratadasList;
	}


	/**
	 * @param entidadesSubContratadas the entidadesSubContratadas to set
	 */
	public void setEntidadesSubContratadasList(ArrayList entidadesSubContratadasList) {
		this.entidadesSubContratadasList = entidadesSubContratadasList;
	}


	/**
	 * @return the conveniosList
	 */
	public ArrayList getConveniosList() {
		return conveniosList;
	}


	/**
	 * @param conveniosList the conveniosList to set
	 */
	public void setConveniosList(ArrayList conveniosList) {
		this.conveniosList = conveniosList;
	}


	/**
	 * @return the contratoList
	 */
	public ArrayList getContratoList() {
		return contratoList;
	}


	/**
	 * @param contratoList the contratoList to set
	 */
	public void setContratoList(ArrayList contratoList) {
		this.contratoList = contratoList;
	}


	/**
	 * @return the codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}


	/**
	 * @param codigoPais the codigoPais to set
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}
	
	/**
	 * @return the indicadorConvenioBusqueda
	 */
	public String getIndicadorConvenioBusqueda() {
		return indicadorConvenioBusqueda;
	}


	/**
	 * @param indicadorConvenioBusqueda the indicadorConvenioBusqueda to set
	 */
	public void setIndicadorConvenioBusqueda(String indicadorConvenioBusqueda) {
		this.indicadorConvenioBusqueda = indicadorConvenioBusqueda;
	}


	/**
	 * @return the indicadorComponenteBusqueda
	 */
	public String getIndicadorComponenteBusqueda() {
		return indicadorComponenteBusqueda;
	}


	/**
	 * @param indicadorComponenteBusqueda the indicadorComponenteBusqueda to set
	 */
	public void setIndicadorComponenteBusqueda(String indicadorComponenteBusqueda) {
		this.indicadorComponenteBusqueda = indicadorComponenteBusqueda;
	}


	/**
	 * @return the serviciosAutorizadosEliminadosMap
	 */
	public HashMap getServiciosAutorizadosEliminadosMap() {
		return serviciosAutorizadosEliminadosMap;
	}


	/**
	 * @param serviciosAutorizadosEliminadosMap the serviciosAutorizadosEliminadosMap to set
	 */
	public void setServiciosAutorizadosEliminadosMap(
			HashMap serviciosAutorizadosEliminadosMap) {
		this.serviciosAutorizadosEliminadosMap = serviciosAutorizadosEliminadosMap;
	}
	
	/**
	 * @return the serviciosAutorizadosEliminadosMap
	 */
	public Object getServiciosAutorizadosEliminadosMap(String key) {
		return serviciosAutorizadosEliminadosMap.get(key);
	}


	/**
	 * @param serviciosAutorizadosEliminadosMap the serviciosAutorizadosEliminadosMap to set
	 */
	public void setServiciosAutorizadosEliminadosMap(String key, Object value) {
		this.serviciosAutorizadosEliminadosMap.put(key, value);
	}


	/**
	 * @return the indexEliminado
	 */
	public int getIndexEliminado() {
		return indexEliminado;
	}


	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(int indexEliminado) {
		this.indexEliminado = indexEliminado;
	}


	/**
	 * @return the usuariosList
	 */
	public ArrayList getUsuariosList() {
		return usuariosList;
	}


	/**
	 * @param usuariosList the usuariosList to set
	 */
	public void setUsuariosList(ArrayList usuariosList) {
		this.usuariosList = usuariosList;
	}


	/**
	 * @return the indicadorFlujo
	 */
	public String getIndicadorFlujo() {
		return indicadorFlujo;
	}


	/**
	 * @param indicadorFlujo the indicadorFlujo to set
	 */
	public void setIndicadorFlujo(String indicadorFlujo) {
		this.indicadorFlujo = indicadorFlujo;
	}


	/**
	 * @return the indexImpresion
	 */
	public int getIndexImpresion() {
		return indexImpresion;
	}


	/**
	 * @param indexImpresion the indexImpresion to set
	 */
	public void setIndexImpresion(int indexImpresion) {
		this.indexImpresion = indexImpresion;
	}


	/**
	 * @return the datosCapitacionMap
	 */
	public HashMap getDatosCapitacionMap() {
		return datosCapitacionMap;
	}


	/**
	 * @param datosCapitacionMap the datosCapitacionMap to set
	 */
	public void setDatosCapitacionMap(HashMap datosCapitacionMap) {
		this.datosCapitacionMap = datosCapitacionMap;
	}
	
	/**
	 * @return the datosCapitacionMap
	 */
	public Object getDatosCapitacionMap(String key) {
		return datosCapitacionMap.get(key);
	}


	/**
	 * @param datosCapitacionMap the datosCapitacionMap to set
	 */
	public void setDatosCapitacionMap(String key,Object obj) {
		this.datosCapitacionMap.put(key,obj);
	}
	
	
	public String getCodigoContrato()
	{
		return registroEntidadesSubMap.get("contrato").toString();
	}
	
	public void setCodigoContrato(String value)
	{
		registroEntidadesSubMap.put("contrato",value);
	}
	
	public String getCodigoConvenio()
	{
		if(registroEntidadesSubMap.containsKey("codigoConvenio"))
			return registroEntidadesSubMap.get("codigoConvenio").toString();
		else
			return "";
	}
	
	public void setCodigoConvenio(String value)
	{
		registroEntidadesSubMap.put("codigoConvenio",value);
	}


	/**
	 * @return the autorizacionIngEvento
	 */
	public InfoDatosString getAutorizacionIngEvento() {
		return autorizacionIngEvento;
	}


	/**
	 * @param autorizacionIngEvento the autorizacionIngEvento to set
	 */
	public void setAutorizacionIngEvento(InfoDatosString autorizacionIngEvento) {
		this.autorizacionIngEvento = autorizacionIngEvento;
	}	
	
	//-----Fin Metodos
}