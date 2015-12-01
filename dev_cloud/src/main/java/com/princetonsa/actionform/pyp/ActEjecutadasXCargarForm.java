/*
 * Nov 14, 2006
 */
package com.princetonsa.actionform.pyp;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Actividades PYP Ejecutadas por Cargar 
 */
public class ActEjecutadasXCargarForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto que almacena el listado de ordenes ambulatorias
	 */
	private HashMap listadoOrdenes = new HashMap();
	/**
	 * Número de registros del mapa listadoOrdenes
	 */
	private int numOrdenes;
	
	/**
	 * Arreglo de convenios
	 */
	private ArrayList convenios = new ArrayList();
	
	/**
	 * Código+ConstantesBD.separadorSplit+Nombre de la seleccion
	 */
	private String seleccion;
	
	/**
	 * Código del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Nombre del convenio
	 */
	private String nombreConvenio;
	
	/**
	 * Variable que indica si hubo una seleccion de convenio
	 */
	private boolean seleccionConvenio;
	
	/**
	 * Número de registros máximos por pagina
	 */
	private int maxPageItems;
	
	/**
	 * Posicion de un registro del mapa
	 */
	private int posicion;
	
	/**
	 * Variable que indica si se seleccionan todas las ordenes
	 */
	private boolean seleccionarTodas;
	
	/**
	 * Variable que almacena la ruta del archivo de inconsistencias
	 */
	private String rutaArchivo;
	/**
	 * Objeto donde se almacena el contenido del archivo
	 */
	private ArrayList archivo = new ArrayList();
	/**
	 * Variable que verifica si existe archivo
	 */
	private boolean existeArchivo;
	
	/**
	 * Objeto donde se almacena el detalle de una orden ambulatoria
	 */
	private HashMap detalle = new HashMap();
	
	/**
	 * Indica si se debe mostrar el botno cargar de la página de detalle
	 */
	private boolean mostrarBoton;
	
	//***ATRIBUTOS PARA LA ORDENACION Y PAGINACION**********************************************
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	private int offset;
	
	
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		
		this.listadoOrdenes = new HashMap();
		this.numOrdenes = 0;
		
		this.convenios = new ArrayList();
		
		this.seleccion = "";
		this.codigoConvenio = 0;
		this.nombreConvenio = "";
		this.seleccionConvenio = false;
		
		this.maxPageItems = 0;
		
		this.posicion = 0;
		
		this.seleccionarTodas = false;
		
		this.rutaArchivo = "";
		this.archivo = new ArrayList();
		this.existeArchivo = false;
		
		this.detalle = new HashMap();
		this.mostrarBoton = false;
		
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.offset = 0;
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
	 * @return Returns the listadoOrdenes.
	 */
	public HashMap getListadoOrdenes() {
		return listadoOrdenes;
	}

	/**
	 * @param listadoOrdenes The listadoOrdenes to set.
	 */
	public void setListadoOrdenes(HashMap listadoOrdenes) {
		this.listadoOrdenes = listadoOrdenes;
	}
	
	/**
	 * @return Retorna un elemento del mapa listadoOrdenes.
	 */
	public Object getListadoOrdenes(String key) {
		return listadoOrdenes.get(key);
	}

	/**
	 * @param Asigna elemento al mapa listadoOrdenes.
	 */
	public void setListadoOrdenes(String key,Object obj) {
		this.listadoOrdenes.put(key,obj);
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
	 * @return Returns the numOrdenes.
	 */
	public int getNumOrdenes() {
		return numOrdenes;
	}

	/**
	 * @param numOrdenes The numOrdenes to set.
	 */
	public void setNumOrdenes(int numOrdenes) {
		this.numOrdenes = numOrdenes;
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
	 * @return Returns the codigoConvenio.
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio The codigoConvenio to set.
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return Returns the nombreConvenio.
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * @return Returns the seleccion.
	 */
	public String getSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion The seleccion to set.
	 */
	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}

	/**
	 * @return Returns the seleccionarTodas.
	 */
	public boolean isSeleccionarTodas() {
		return seleccionarTodas;
	}

	/**
	 * @param seleccionarTodas The seleccionarTodas to set.
	 */
	public void setSeleccionarTodas(boolean seleccionarTodas) {
		this.seleccionarTodas = seleccionarTodas;
	}

	/**
	 * @return Returns the convenios.
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios The convenios to set.
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return Returns the seleccionConvenio.
	 */
	public boolean isSeleccionConvenio() {
		return seleccionConvenio;
	}

	/**
	 * @param seleccionConvenio The seleccionConvenio to set.
	 */
	public void setSeleccionConvenio(boolean seleccionConvenio) {
		this.seleccionConvenio = seleccionConvenio;
	}

	/**
	 * @return Returns the archivo.
	 */
	public ArrayList getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo The archivo to set.
	 */
	public void setArchivo(ArrayList archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return Returns the existeArchivo.
	 */
	public boolean isExisteArchivo() {
		return existeArchivo;
	}

	/**
	 * @param existeArchivo The existeArchivo to set.
	 */
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}

	/**
	 * @return Returns the rutaArchivo.
	 */
	public String getRutaArchivo() {
		return rutaArchivo;
	}

	/**
	 * @param rutaArchivo The rutaArchivo to set.
	 */
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	/**
	 * @return Returns the detalle.
	 */
	public HashMap getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle The detalle to set.
	 */
	public void setDetalle(HashMap detalle) {
		this.detalle = detalle;
	}
	
	/**
	 * @return Retorna elemento del mapa detalle.
	 */
	public Object getDetalle(String key) {
		return detalle.get(key);
	}

	/**
	 * @param Asigna elemento al mapa detalle.
	 */
	public void setDetalle(String key,Object obj) {
		this.detalle.put(key,obj);
	}

	/**
	 * @return Returns the mostrarBoton.
	 */
	public boolean isMostrarBoton() {
		return mostrarBoton;
	}

	/**
	 * @param mostrarBoton The mostrarBoton to set.
	 */
	public void setMostrarBoton(boolean mostrarBoton) {
		this.mostrarBoton = mostrarBoton;
	}
}
