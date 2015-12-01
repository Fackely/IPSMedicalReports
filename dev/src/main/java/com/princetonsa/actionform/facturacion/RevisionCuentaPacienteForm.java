package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap; 

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.ValoresPorDefecto;

public class RevisionCuentaPacienteForm extends ValidatorForm
{
	
	//****************Atributos*********************************
	
	//***********Action********** 
	/**
	 * Estado de la Forma
	 * */
	private String estado;	 
	
	/**
	 * HashMap de Busqueda
	 * */
	private HashMap busqueda;
	
	/**
	 * HashMap de Estados Solicitud
	 * */
	private HashMap estadosSolicitudMap;
	
	/**
	 * HashMap de Tipos Solicitud
	 * */
	private HashMap tiposSolicitudMap;
	
	/**
	 * String de cuenta
	 * */
	private String cuenta;	
	
	/**
	 * ArrayList de Mensajes de advertencia 
	 * */
	private ArrayList mensajes;
	
	/**
	 * Tamaño del array de mensajes 
	 * */
	private int sizeMensajes;
	
	/**
	 * String parametros 
	 * */
	private String parametros;
	
	//**********Fin Action********
	
	//*****Pager*************
	/**
	 * Patron de Ordenamiento 
	 * */
	private String patronOrdenar;	
	
	/**
	 * Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;	
	
	/**
	 * Link Siguiente del Pager 
	 * */
	private String linkSiguiente;
	
	/**
	 * Link Siguiente para el pager pagina Solicitudes
	 */
	private String linkSiguienteSolicitudes;
	
	/**
	 * Link Siguiente para el pager pagina Asocios
	 */
	private String linkSiguienteAsocios;
	
	/**
	 * offset
	 * */
	private int offset;
	
	/**
	 * 
	 * */
	private int maxPageItems;
	//*****Fin Pager*************
	
	
	//*****Responsables*************
	/**
	 * HashMap de Responsables 
	 * */
	private HashMap responsablesMap;
	
	/**
	 * HashMap de Requisitos Paciente
	 * */
	private HashMap requisitosMap;	
		
	/**
	 * Indica la posicion dentro del HashMap del convenio elgido
	 * */
	private String indexResponsablesMap;
	
	/**
	 * Indica el tipo de requisito 
	 * */
	private String tipoRequisito;
	
	//**********Fin Responsables*******
	
	
	//*****Solicitudes Responsables*************
	/**
	 * HashMap de Solicitudes por Responsable
	 * */
	private HashMap listadoSolicitudesMap;
	
	/**
	 * indica la posicion en el mapa de solicitudes
	 * */
	private String indexSolicitudesMap;
	
	//*****Fin Solicitudes Responsables*************

	
	//*************Detalle Solicitudes*******************
	/**
	 * HashMap detalle de solicitud
	 * */
	private HashMap detalleSolicitudesMap;
	
	/**
	 * HashMap Index del detalle de solicitudes 
	 * */
	private String indexDetalleSolicitudes;
	
	/**
	 * HashMap indicador del forward para el detalle de la solicitus
	 * */
	private String indexForwardDetalleSolicitudes;
	
	//*************Fin Detalle Solicitudes***************
	
	
	//*************Detalle Solicitud Tipo Paquete*******************
	/**
	 * HashMap detalle de solicitud
	 * */
	private HashMap listadoDetallePaqueteMap;
	
	/**
	 * Indicador del forward para el detalle de la solicitus
	 * */
	private String indexListadoDetallePaquete;	
	
	/**
	 * Indicador del tipo de listadoDetalleActual (Paquete o No paquete)
	 * */
	private String indicadorTipoListadoDetalle;
	
	//*************Detalle Solicitud Tipo Paquete***************
	
	
	//****************Pooles**************************************
	
	/**
	 * Indica si existe pooles pendientes
	 * */	
	private String hayPoolesPendientes;
	
	/**
	 * HasMap de los Pooles por medico 
	 * */
	private HashMap listadoPoolesMedico;
	
	/**
	 * Index del listado del pool del medico
	 * */
	private String indexListadoPoolMedico;
	
	//****************Fin Pooles**********************************
	
	
	//***************Manejo Estado del Cargo*******************
	
	/**
	 * Variable para Manejar el estado del Check Excenta
	 */
	private String esExcenta;
	
	/**
	 * Variable para Manejar el estado del Check Seleccion Todos
	 */
	private String todosEstadoCargo;
	
	/**
	 * HashMap para generar el log del estado cargo
	 */
	private HashMap detEstadoAnterior;
	
	/**
	 * HashMap para generar el log del estado cargo
	 */
	private HashMap detEstadoActual;
	
	
	//**************Fin Manejo Estado del Cargo **************
	

	//****************Metodos*********************************
	
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
	 * Incializa los Atributos de la Forma
	 * */
	public void reset()
	{	
		this.responsablesMap = new HashMap();		
		this.setResponsablesMap("numRegistros","0");
		this.busqueda = new HashMap();
		
		this.tiposSolicitudMap = new HashMap();
		this.estadosSolicitudMap = new HashMap();		
		
		this.indexResponsablesMap = "";
		this.requisitosMap = new HashMap();		
		this.tipoRequisito="";
		this.linkSiguiente = "";
		this.linkSiguienteSolicitudes = "";
		this.linkSiguienteAsocios = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
		this.offset = 0;
		this.maxPageItems = 0;
		this.hayPoolesPendientes = "";
		this.mensajes = new ArrayList();
		this.sizeMensajes = 0;
		this.parametros ="";
	}
	
	/**
	 * Inicializa los valores de las solicitudes del responsable
	 * */
	public void resetSolicitudes()
	{			
		this.listadoSolicitudesMap = new HashMap();
		this.detalleSolicitudesMap = new HashMap();
		this.listadoPoolesMedico = new HashMap();
		this.indexSolicitudesMap = "";
		this.esExcenta="";
		this.todosEstadoCargo=ConstantesBD.acronimoNo;
		this.mensajes = new ArrayList();
		this.sizeMensajes = 0;
		this.detEstadoAnterior = new HashMap();
		this.detEstadoActual = new HashMap();
		
	}
	
	/**
	 * Inicializa los valores de las solicitudes del paquete
	 * */
	public void resetDetallePaquete()
	{
		this.listadoDetallePaqueteMap = new HashMap();
		this.detalleSolicitudesMap = new HashMap();
	}
		
	
	/**
	 * @return the responsablesMap
	 */
	public HashMap getResponsablesMap() {
		return responsablesMap;
	}

	/**
	 * @param responsablesMap the responsablesMap to set
	 */
	public void setResponsablesMap(HashMap responsablesMap) {
		this.responsablesMap = responsablesMap;
	}
	
	/**
	 * @return the Object
	 */
	public Object getResponsablesMap(String key) {
		return this.responsablesMap.get(key);
	}

	/**
	 * @param String key
	 * @param Object value
	 */
	public void setResponsablesMap(String key,Object value) {
		this.responsablesMap.put(key, value);
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
	 * @return the indexResponsablesMap
	 */
	public String getIndexResponsablesMap() {
		return indexResponsablesMap;
	}


	/**
	 * @param indexResponsablesMap the indexResponsablesMap to set
	 */
	public void setIndexResponsablesMap(String indexResponsablesMap) {
		this.indexResponsablesMap = indexResponsablesMap;
	}


	/**
	 * @return the requisitosMap
	 */
	public HashMap getRequisitosMap() {
		return requisitosMap;
	}


	/**
	 * @param requisitosMap the requisitosMap to set
	 */
	public void setRequisitosMap(HashMap requisitosMap) {
		this.requisitosMap = requisitosMap;
	}	
	
	
	/**
	 * @return the requisitosMap
	 */
	public Object getRequisitosMap(String key) {
		return requisitosMap.get(key);
	}


	/**
	 * @param requisitosMap the requisitosMap to set
	 */
	public void setRequisitosMap(String key, Object value) {
		this.requisitosMap.put(key, value);
	}


	/**
	 * @return the tipoRequisito
	 */
	public String getTipoRequisito() {
		return tipoRequisito;
	}


	/**
	 * @param tipoRequisito the tipoRequisito to set
	 */
	public void setTipoRequisito(String tipoRequisito) {
		this.tipoRequisito = tipoRequisito;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	/**
	 * @return the solicitudesResponsablesMap
	 */
	public HashMap getListadoSolicitudesMap() {
		return listadoSolicitudesMap;
	}


	/**
	 * @param solicitudesResponsablesMap the solicitudesResponsablesMap to set
	 */
	public void setListadoSolicitudesMap(HashMap solicitudesMap) {
		this.listadoSolicitudesMap = solicitudesMap;
	}
	
	
	/**
	 * @return the solicitudesResponsablesMap
	 */
	public Object getListadoSolicitudesMap(String key) {
		return listadoSolicitudesMap.get(key);
	}


	/**
	 * @param solicitudesResponsablesMap the solicitudesResponsablesMap to set
	 */
	public void setListadoSolicitudesMap(String key, Object value) {
		this.listadoSolicitudesMap.put(key, value);
	}


	/**
	 * @return the indexSolicitudesMap
	 */
	public String getIndexSolicitudesMap() {
		return indexSolicitudesMap;
	}


	/**
	 * @param indexSolicitudesMap the indexSolicitudesMap to set
	 */
	public void setIndexSolicitudesMap(String indexSolicitudesMap) {
		this.indexSolicitudesMap = indexSolicitudesMap;
	}


	/**
	 * @return the busqueda
	 */
	public HashMap getBusqueda() {
		return busqueda;
	}


	/**
	 * @param busqueda the busqueda to set
	 */
	public void setBusqueda(HashMap busqueda) {
		this.busqueda = busqueda;
	}
	
	/**
	 * @return the busqueda
	 */
	public Object getBusqueda(String key) {
		return busqueda.get(key);
	}


	/**
	 * @param busqueda the busqueda to set
	 */
	public void setBusqueda(String key, Object value) {
		this.busqueda.put(key, value);
	}


	/**
	 * @return the estadosSolicitud
	 */
	public HashMap getEstadosSolicitudMap() {
		return estadosSolicitudMap;
	}


	/**
	 * @param estadosSolicitud the estadosSolicitud to set
	 */
	public void setEstadosSolicitudMap(HashMap estadosSolicitudMap) {
		this.estadosSolicitudMap = estadosSolicitudMap;
	}


	/**
	 * @return the tiposSolicitud
	 */
	public HashMap getTiposSolicitudMap() {
		return tiposSolicitudMap;
	}


	/**
	 * @param tiposSolicitud the tiposSolicitud to set
	 */
	public void setTiposSolicitudMap(HashMap tiposSolicitudMap) {
		this.tiposSolicitudMap = tiposSolicitudMap;
	}
	
	/**
	 * @return the tiposSolicitud
	 */
	public Object getTiposSolicitudMap(String key) {
		return tiposSolicitudMap.get(key);
	}


	/**
	 * @param tiposSolicitud the tiposSolicitud to set
	 */
	public void setTiposSolicitudMap(String key, Object value) {
		this.tiposSolicitudMap.put(key, value);
	}


	/**
	 * @return the detalleSolicitudesMap
	 */
	public HashMap getDetalleSolicitudesMap() {
		return detalleSolicitudesMap;
	}


	/**
	 * @param detalleSolicitudesMap the detalleSolicitudesMap to set
	 */
	public void setDetalleSolicitudesMap(HashMap detalleSolicitudesMap) {
		this.detalleSolicitudesMap = detalleSolicitudesMap;
	}
	
	/**
	 * @return the detalleSolicitudesMap
	 */
	public Object getDetalleSolicitudesMap(String key) {
		return detalleSolicitudesMap.get(key);
	}


	/**
	 * @param detalleSolicitudesMap the detalleSolicitudesMap to set
	 */
	public void setDetalleSolicitudesMap(String key, Object value) {
		this.detalleSolicitudesMap.put(key, value);
	}


	/**
	 * @return the indexForwardDetalleSolicitudes
	 */
	public String getIndexForwardDetalleSolicitudes() {
		return indexForwardDetalleSolicitudes;
	}


	/**
	 * @param indexForwardDetalleSolicitudes the indexForwardDetalleSolicitudes to set
	 */
	public void setIndexForwardDetalleSolicitudes(
			String indexForwardDetalleSolicitudes) {
		this.indexForwardDetalleSolicitudes = indexForwardDetalleSolicitudes;
	}


	/**
	 * @return the indexListadoDetallePaquete
	 */
	public String getIndexListadoDetallePaquete() {
		return indexListadoDetallePaquete;
	}


	/**
	 * @param indexListadoDetallePaquete the indexListadoDetallePaquete to set
	 */
	public void setIndexListadoDetallePaquete(String indexListadoDetallePaquete) {
		this.indexListadoDetallePaquete = indexListadoDetallePaquete;
	}


	/**
	 * @return the listadoDetallePaqueteMap
	 */
	public HashMap getListadoDetallePaqueteMap() {
		return listadoDetallePaqueteMap;
	}


	/**
	 * @param listadoDetallePaqueteMap the listadoDetallePaqueteMap to set
	 */
	public void setListadoDetallePaqueteMap(HashMap listadoDetallePaqueteMap) {
		this.listadoDetallePaqueteMap = listadoDetallePaqueteMap;
	}
	
	
	/**
	 * @return the listadoDetallePaqueteMap
	 */
	public Object getListadoDetallePaqueteMap(String key) {
		return listadoDetallePaqueteMap.get(key);
	}


	/**
	 * @param listadoDetallePaqueteMap the listadoDetallePaqueteMap to set
	 */
	public void setListadoDetallePaqueteMap(String key, Object value) {
		this.listadoDetallePaqueteMap.put(key, value);
	}


	/**
	 * @return the indicadorTipoListadoDetalle
	 */
	public String getIndicadorTipoListadoDetalle() {
		return indicadorTipoListadoDetalle;
	}


	/**
	 * @param indicadorTipoListadoDetalle the indicadorTipoListadoDetalle to set
	 */
	public void setIndicadorTipoListadoDetalle(String indicadorTipoListadoDetalle) {
		this.indicadorTipoListadoDetalle = indicadorTipoListadoDetalle;
	}


	/**
	 * @return the hayPoolesPendientes
	 */
	public String getHayPoolesPendientes() {
		return hayPoolesPendientes;
	}


	/**
	 * @param hayPoolesPendientes the hayPoolesPendientes to set
	 */
	public void setHayPoolesPendientes(String hayPoolesPendientes) {
		this.hayPoolesPendientes = hayPoolesPendientes;
	}


	/**
	 * @return the listadoPoolesMedico
	 */
	public HashMap getListadoPoolesMedico() {
		return listadoPoolesMedico;
	}


	/**
	 * @param listadoPoolesMedico the listadoPoolesMedico to set
	 */
	public void setListadoPoolesMedico(HashMap listadoPoolesMedico) {
		this.listadoPoolesMedico = listadoPoolesMedico;
	}	
	
	/**
	 * @return the listadoPoolesMedico
	 */
	public Object getListadoPoolesMedico(String key) {
		return listadoPoolesMedico.get(key);
	}


	/**
	 * @param listadoPoolesMedico the listadoPoolesMedico to set
	 */
	public void setListadoPoolesMedico(String key, Object value) {
		this.listadoPoolesMedico.put(key, value);
	}


	/**
	 * @return the indexDetalleSolicitudes
	 */
	public String getIndexDetalleSolicitudes() {
		return indexDetalleSolicitudes;
	}


	/**
	 * @param indexDetalleSolicitudes the indexDetalleSolicitudes to set
	 */
	public void setIndexDetalleSolicitudes(String indexDetalleSolicitudes) {
		this.indexDetalleSolicitudes = indexDetalleSolicitudes;
	}


	/**
	 * @return the indexListadoPoolMedico
	 */
	public String getIndexListadoPoolMedico() {
		return indexListadoPoolMedico;
	}


	/**
	 * @param indexListadoPoolMedico the indexListadoPoolMedico to set
	 */
	public void setIndexListadoPoolMedico(String indexListadoPoolMedico) {
		this.indexListadoPoolMedico = indexListadoPoolMedico;
	}


	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}


	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}


	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}


	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}


	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	/**
	 * @return the mensajes
	 */
	public ArrayList getMensajes() {
		return mensajes;
	}


	/**
	 * @param mensajes the mensajes to set
	 */
	public void setMensajes(ArrayList mensajes) {
		this.mensajes = mensajes;
	}


	/**
	 * @return the sizeMensajes
	 */
	public int getSizeMensajes() {
		return sizeMensajes;
	}


	/**
	 * @param sizeMensajes the sizeMensajes to set
	 */
	public void setSizeMensajes(int sizeMensajes) {
		this.sizeMensajes = sizeMensajes;
	}


	/**
	 * @return the parametros
	 */
	public String getParametros() {
		return parametros;
	}


	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsExcenta() {
		return esExcenta;
	}

	/**
	 * 
	 * @param esExcenta
	 */
	public void setEsExcenta(String esExcenta) {
		this.esExcenta = esExcenta;
	}


	/**
	 * @return todosEstadoCargo
	 */
	public String getTodosEstadoCargo() {
		return todosEstadoCargo;
	}

	/**
	 * @param todosEstadoCargo
	 */
	public void setTodosEstadoCargo(String todosEstadoCargo) {
		this.todosEstadoCargo = todosEstadoCargo;
	}


	public HashMap getDetEstadoActual() {
		return detEstadoActual;
	}


	public void setDetEstadoActual(HashMap detEstadoActual) {
		this.detEstadoActual = detEstadoActual;
	}


	public HashMap getDetEstadoAnterior() {
		return detEstadoAnterior;
	}


	public void setDetEstadoAnterior(HashMap detEstadoAnterior) {
		this.detEstadoAnterior = detEstadoAnterior;
	}
	
	/**
	 * @param detEstadoActual the detEstadoActual to set
	 */
	public void setDetEstadoActual(String key, Object value) {
		this.detEstadoActual.put(key, value);
	}
	
	/**
	 * @param detEstadoAnterior the detEstadoAnterior to set
	 */
	public void setDetEstadoAnterior(String key, Object value) {
		this.detEstadoAnterior.put(key, value);
	}


	/**
	 * @return the linkSiguienteSolicitudes
	 */
	public String getLinkSiguienteSolicitudes() {
		return linkSiguienteSolicitudes;
	}


	/**
	 * @param linkSiguienteSolicitudes the linkSiguienteSolicitudes to set
	 */
	public void setLinkSiguienteSolicitudes(String linkSiguienteSolicitudes) {
		this.linkSiguienteSolicitudes = linkSiguienteSolicitudes;
	}


	/**
	 * @return the linkSiguienteAsocios
	 */
	public String getLinkSiguienteAsocios() {
		return linkSiguienteAsocios;
	}


	/**
	 * @param linkSiguienteAsocios the linkSiguienteAsocios to set
	 */
	public void setLinkSiguienteAsocios(String linkSiguienteAsocios) {
		this.linkSiguienteAsocios = linkSiguienteAsocios;
	}
	
	
	
}