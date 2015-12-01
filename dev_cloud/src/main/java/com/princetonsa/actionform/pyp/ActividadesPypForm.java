/*
 * Ago 02, 2006
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de actividades 
 */
public class ActividadesPypForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto que almacena las actividades
	 */
	private HashMap actividades = new HashMap();
	
	/**
	 * Número de registros en el mapa de actividades
	 */
	private int numRegistros;
	
	/**
	 * Almacena el número de filas por página máximo
	 */
	private int maxPageItems;
	
	/**
	 * Posicion del registro que se desea eliminar
	 */
	private int posicion;
	
	/**
	 * Tipo de actividad
	 */
	private String tipoActividad;
	
	/**
	 * Variable donde se almacenan los codigos de los servicios/artículos
	 * separados por ,
	 */
	private String codigosExistentes;
	
	//***ATRIBUTOS PARA LA ORDENACION Y PAGINACION**********************************************
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	private int offset;
	
	
	//---------------------------------------------------------------------------------------
	//-----Variables Para la Funcionalidad de Actividades PYP por Centro de Atencion--------- 
	
	/**
	 * Mapa Para Ingresar/Consultar la Actividades por centros de Atencion
	 */
	private HashMap mapaCA;
	
	/**
	 * Variable para almacenar el centro de Atencion Seleccionado (Funcionalidad)
	 */
	private int centroAtencion;
	
	/**
	 * Variable para almacenar el codigo de la actividad que se va a eliminar.
	 */
	private int codigoActividad;

	/**
	 * Variable para saber si se envia a la ultima pagina del paginador.
	 */
	private boolean enviarUltimaPagina;
	
	/**
	 * 
	 */
	private String isGuardoCA;
	
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		
		this.actividades = new HashMap();
		this.numRegistros = 0;
		this.maxPageItems = 0;
		
		this.posicion = 0;
		
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.offset = 0;
		
		this.tipoActividad = "";
		
		this.codigosExistentes = "";
		
		this.enviarUltimaPagina = false;
		
		this.isGuardoCA = ConstantesBD.acronimoNo;
	}

	/**
	 * metodo para resetear solo las variables de la Funcionalidad CA (Actividades de Centros de Atencion).
	 *
	 */
	public void resetCA()
	{
		this.mapaCA = new HashMap();
		this.setMapaCA("nroRegistrosNv","0");
		this.centroAtencion = 0;
		this.codigoActividad = 0;
		
		this.isGuardoCA = ConstantesBD.acronimoNo;
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
		ActionErrors errores = new ActionErrors();
		
		return errores;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the actividades.
	 */
	public HashMap getActividades() {
		return actividades;
	}

	/**
	 * @param actividades The actividades to set.
	 */
	public void setActividades(HashMap actividades) {
		this.actividades = actividades;
	}
	
	/**
	 * @return Retorna un elemento del mapa actividades.
	 */
	public Object getActividades(String key) {
		return actividades.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa actividades.
	 */
	public void setActividades(String key,Object obj) {
		this.actividades.put(key,obj);
	}

	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return Returns the posicion.
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion The posicion to set.
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return Returns the tipoActividad.
	 */
	public String getTipoActividad() {
		return tipoActividad;
	}

	/**
	 * @param tipoActividad The tipoActividad to set.
	 */
	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Returns the codigosExistentes.
	 */
	public String getCodigosExistentes() {
		return codigosExistentes;
	}

	/**
	 * @param codigosExistentes The codigosExistentes to set.
	 */
	public void setCodigosExistentes(String codigosExistentes) {
		this.codigosExistentes = codigosExistentes;
	}


	/**
	 * @return Retorna mapaCargue.
	 */
	public HashMap getMapaCA() {
		return mapaCA;
	}


	/**
	 * @param Asigna mapaCargue.
	 */
	public void setMapaCA(HashMap mapaCA) {
		this.mapaCA = mapaCA;
	}

	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaCA(String key) {
		return this.mapaCA.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaCA(String key, String valor) 
	{
		this.mapaCA.put(key, valor); 
	}

	/**
	 * @return Retorna centroAtencion.
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param Asigna centroAtencion.
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Retorna codigoActividad.
	 */
	public int getCodigoActividad() {
		return codigoActividad;
	}

	/**
	 * @param Asigna codigoActividad.
	 */
	public void setCodigoActividad(int codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	public boolean getEnviarUltimaPagina() {
		return enviarUltimaPagina;
	}

	public void setEnviarUltimaPagina(boolean enviarUltimaPagina) {
		this.enviarUltimaPagina = enviarUltimaPagina;
	}

	/**
	 * @return the isGuardoCA
	 */
	public String getIsGuardoCA() {
		return isGuardoCA;
	}

	/**
	 * @param isGuardoCA the isGuardoCA to set
	 */
	public void setIsGuardoCA(String isGuardoCA) {
		this.isGuardoCA = isGuardoCA;
	}
	
}
