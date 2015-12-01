/*
 * Marzo 16, 2006 
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
/**
 * @author Sebastián Gómez 
 * @author Modificado, Nov 07 Jose Eduardo Arias Doncel
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Excepciones Tarifas Asocios
 */
public class ExTarifasAsociosForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExTarifasAsociosForm.class);
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Código del convenio
	 */
	private int convenio;
	
	/**
	 * Nombre Convenio
	 * */
	private String nombreConvenio;
	
	/**
	 * Código de la institucion
	 */
	private String institucion;
	
	/**
	 * Seccion de Busqueda Avanzada
	 * */
	private boolean seccionBusquedaAvanzada;
	
	/**
	 * HashMap busquedaAvanzada
	 * */
	private HashMap busquedaAvanzadaMap;
	
	/**
	 * Codigo via ingreso para realizar la busqueda de Tipos Paciente
	 * */
	private String codigoViaIngreso;

	
	/**
	 * Link Siguiende del pager
	 * */
	private String linkSiguiente;
	
	
	//****************************Atributos Encabezado
	
	/**
	 * Almacena la informacion del encabezado  
	 * */
	private HashMap encabezadoMap = new HashMap();
	
	
	/**
	 * Indicador del Encabezado Map
	 * */
	private String indicadorEncabezado;
	
	/**
	 * Indicador del ingreso 
	 * */
	private String indicadorIngreso;
	
	//****************************Atributos Medio
	
	/**
	 * Almacena la informacion de Asocios por Via Ingreso, Tipo Paciente, Centro de Costos
	 * */
	private HashMap mediaMap = new HashMap();	

	/**
	 * Indicador del Medio Map
	 * */
	private String indicadorMedio;
	
	/**
	 * Via de Ingreso
	 * */
	private HashMap viaIngresoMap;
	
	/**
	 * Tipo Paciente
	 * */
	private ArrayList tipoPacienteMap;
	
	/**
	 * Centro de Costp
	 * */
	private HashMap centroCostoMap;
	
	//******************************Atributos Detalle
	
	/**
	 * Almacena la informacion del Detalle de Excepciones Tarifas Asocios 
	 * */
	private HashMap detalleMap;
	
	/**
	 * Indicador del Detalle Map 
	 * */
	private String indicadorDetalle;
	
	/**
	 * Almacena la informacion del tipo de servicio 
	 * */
	private ArrayList<HashMap<String,Object>> tipoServicioArray;
	
	/**
	 * Almacena la informacion del Grupo de Servicio 
	 * */
	private ArrayList grupoServicioArray;	

	/**
	 * Almancen la informacion de Tipo Cirugia 
	 * */
	private ArrayList tipoCirugiaArray;	
	
	/**
	 * Almacen la informacion del Asocio
	 * */
	private ArrayList asocioArray;	
	
	/**
	 * Almacenla la informacion de la especialidad
	 * */
	private HashMap especialidadMap;
	
	
	//*****************************Atributos Forma
	
		
	/**
	 * reset de los datos de la forma	 
	 */
	public void reset()
	{
		this.estado = "";
		this.encabezadoMap = new HashMap();
		this.encabezadoMap.put("numRegistros","0");
		this.mediaMap = new HashMap();
		this.detalleMap = new HashMap();
		this.busquedaAvanzadaMap = new HashMap();
		this.convenio = 0;		
		this.institucion = "";
		this.nombreConvenio = "";
		this.codigoViaIngreso = "";
		this.tipoServicioArray = new ArrayList<HashMap<String,Object>>();
		this.grupoServicioArray = new ArrayList();
		this.tipoCirugiaArray = new ArrayList();
		this.asocioArray = new ArrayList();
		this.especialidadMap = new HashMap();
		this.linkSiguiente = "";
	}
	
	
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
		ActionErrors errores= new ActionErrors();
		boolean huboError = false;
		
		if(this.estado.equals("guardar"))
		{
			/*for(int i=0;i<this.numRegistros;i++)
			
			
		else if (estado.equals("busquedaServicio")||estado.equals("servicioTodos")||estado.equals("eliminar"))
		{
			//VALIDACION CAMPO ASOCIO************************************************
			for(int i=0;i<this.numRegistros;i++)
			{
				String aux[] = this.getRegistros("asocio_"+i).toString().split("-");
				if(!aux[0].equals("0"))
				{
					this.setRegistros("codigoasocio_"+i,aux[0]);
					this.setRegistros("nombreasocio_"+i,aux[1]);
					this.getRegistros().remove("asocio_"+i);
				}
			}
			*/
		}
		
		return errores;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getConvenio() {
		return convenio;
	}


	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}


	public String getInstitucion() {
		return institucion;
	}


	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}


	/**
	 * @return the detalleMap
	 */
	public HashMap getDetalleMap() {
		return detalleMap;
	}


	/**
	 * @param detalleMap the detalleMap to set
	 */
	public void setDetalleMap(HashMap detalleMap) {
		this.detalleMap = detalleMap;
	}
	
	
	/**
	 * @return the detalleMap
	 */
	public Object getDetalleMap(String key) {
		return detalleMap.get(key);
	}


	/**
	 * @param detalleMap the detalleMap to set
	 */
	public void setDetalleMap(String key, Object value) {
		this.detalleMap.put(key, value);
	}


	/**
	 * @return the encabezadoMap
	 */
	public HashMap getEncabezadoMap() {
		return encabezadoMap;
	}


	/**
	 * @param encabezadoMap the encabezadoMap to set
	 */
	public void setEncabezadoMap(HashMap encabezadoMap) {
		this.encabezadoMap = encabezadoMap;
	}
	
	
	/**
	 * @return the encabezadoMap
	 */
	public Object getEncabezadoMap(String key) {
		return encabezadoMap.get(key);
	}


	/**
	 * @param encabezadoMap the encabezadoMap to set
	 */
	public void setEncabezadoMap(String key, Object value) {
		this.encabezadoMap.put(key, value);
	}



	/**
	 * @return the indicadorDetalle
	 */
	public String getIndicadorDetalle() {
		return indicadorDetalle;
	}


	/**
	 * @param indicadorDetalle the indicadorDetalle to set
	 */
	public void setIndicadorDetalle(String indicadorDetalle) {
		this.indicadorDetalle = indicadorDetalle;
	}


	/**
	 * @return the indicadorEncabezado
	 */
	public String getIndicadorEncabezado() {
		return indicadorEncabezado;
	}


	/**
	 * @param indicadorEncabezado the indicadorEncabezado to set
	 */
	public void setIndicadorEncabezado(String indicadorEncabezado) {
		this.indicadorEncabezado = indicadorEncabezado;
	}


	/**
	 * @return the indicadorMedio
	 */
	public String getIndicadorMedio() {
		return indicadorMedio;
	}


	/**
	 * @param indicadorMedio the indicadorMedio to set
	 */
	public void setIndicadorMedio(String indicadorMedio) {
		this.indicadorMedio = indicadorMedio;
	}


	/**
	 * @return the mediaMap
	 */
	public HashMap getMediaMap() {
		return mediaMap;
	}


	/**
	 * @param mediaMap the mediaMap to set
	 */
	public void setMediaMap(HashMap mediaMap) {
		this.mediaMap = mediaMap;
	}
	
	
	/**
	 * @return the mediaMap
	 */
	public Object getMediaMap(String key) {
		return mediaMap.get(key);
	}


	/**
	 * @param mediaMap the mediaMap to set
	 */
	public void setMediaMap(String key, Object value) {
		this.mediaMap.put(key, value);
	}


	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}


	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}


	/**
	 * @return the centroCostoMap
	 */
	public HashMap getCentroCostoMap() {
		return centroCostoMap;
	}


	/**
	 * @param centroCostoMap the centroCostoMap to set
	 */
	public void setCentroCostoMap(HashMap centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}


	/**
	 * @return the tipoPacienteMap
	 */
	public ArrayList getTipoPacienteMap() {
		return tipoPacienteMap;
	}


	/**
	 * @param tipoPacienteMap the tipoPacienteMap to set
	 */
	public void setTipoPacienteMap(ArrayList tipoPacienteMap) {
		this.tipoPacienteMap = tipoPacienteMap;
	}


	/**
	 * @return the viaIngresoMap
	 */
	public HashMap getViaIngresoMap() {
		return viaIngresoMap;
	}


	/**
	 * @param viaIngresoMap the viaIngresoMap to set
	 */
	public void setViaIngresoMap(HashMap viaIngresoMap) {
		this.viaIngresoMap = viaIngresoMap;
	}


	/**
	 * @return the seccionBusquedaAvanzada
	 */
	public boolean isSeccionBusquedaAvanzada() {
		return seccionBusquedaAvanzada;
	}


	/**
	 * @param seccionBusquedaAvanzada the seccionBusquedaAvanzada to set
	 */
	public void setSeccionBusquedaAvanzada(boolean seccionBusquedaAvanzada) {
		this.seccionBusquedaAvanzada = seccionBusquedaAvanzada;
	}


	/**
	 * @return the busquedaAvanzadaMap
	 */
	public HashMap getBusquedaAvanzadaMap() {
		return busquedaAvanzadaMap;
	}


	/**
	 * @param busquedaAvanzadaMap the busquedaAvanzadaMap to set
	 */
	public void setBusquedaAvanzadaMap(HashMap busquedaAvanzadaMap) {
		this.busquedaAvanzadaMap = busquedaAvanzadaMap;
	}
	
	
	/**
	 * @return the busquedaAvanzadaMap
	 */
	public Object getBusquedaAvanzadaMap(String key) {
		return busquedaAvanzadaMap.get(key);
	}


	/**
	 * @param busquedaAvanzadaMap the busquedaAvanzadaMap to set
	 */
	public void setBusquedaAvanzadaMap(String key, Object value) {
		this.busquedaAvanzadaMap.put(key, value);
	}


	/**
	 * @return the codigoViaIngreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}


	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}


	/**
	 * @return the asocioArray
	 */
	public ArrayList getAsocioArray() {
		return asocioArray;
	}


	/**
	 * @param asocioArray the asocioArray to set
	 */
	public void setAsocioArray(ArrayList asocioArray) {
		this.asocioArray = asocioArray;
	}


	/**
	 * @return the grupoServicioArray
	 */
	public ArrayList getGrupoServicioArray() {
		return grupoServicioArray;
	}


	/**
	 * @param grupoServicioArray the grupoServicioArray to set
	 */
	public void setGrupoServicioArray(ArrayList grupoServicioArray) {
		this.grupoServicioArray = grupoServicioArray;
	}


	/**
	 * @return the tipoCirugiaArray
	 */
	public ArrayList getTipoCirugiaArray() {
		return tipoCirugiaArray;
	}


	/**
	 * @param tipoCirugiaArray the tipoCirugiaArray to set
	 */
	public void setTipoCirugiaArray(ArrayList tipoCirugiaArray) {
		this.tipoCirugiaArray = tipoCirugiaArray;
	}
	

	/**
	 * @return the especialidadMap
	 */
	public HashMap getEspecialidadMap() {
		return especialidadMap;
	}


	/**
	 * @param especialidadMap the especialidadMap to set
	 */
	public void setEspecialidadMap(HashMap especialidadMap) {
		this.especialidadMap = especialidadMap;
	}


	/**
	 * @return the tipoServicioArray
	 */
	public ArrayList<HashMap<String, Object>> getTipoServicioArray() {
		return tipoServicioArray;
	}


	/**
	 * @param tipoServicioArray the tipoServicioArray to set
	 */
	public void setTipoServicioArray(
			ArrayList<HashMap<String, Object>> tipoServicioArray) {
		this.tipoServicioArray = tipoServicioArray;
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
	 * @return the indicadorIngreso
	 */
	public String getIndicadorIngreso() {
		return indicadorIngreso;
	}


	/**
	 * @param indicadorIngreso the indicadorIngreso to set
	 */
	public void setIndicadorIngreso(String indicadorIngreso) {
		this.indicadorIngreso = indicadorIngreso;
	}
}