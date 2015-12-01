package com.princetonsa.actionform.inventarios;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.inventarios.ArticulosPorAlmacenAction;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * Clase para el manejo de la ubicacion de articulos por almacen
 * Date: 2008-01-22
 * @author garias@princetonsa.com
 */
public class ArticulosPorAlmacenForm extends ValidatorForm 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ArticulosPorAlmacenForm.class);
	
	/************************************************/
	//atributos para el uso del pager
	
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
	
	
	/************************************************/
	/**
	 * Estado del formulario
	 */
	private String estado;
	
	/**
	 * Llave primaria de articulos por almacen
	 */
	private String codigoPk;
	
	/**
	 * Codigo del centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * Mapa de los centors de atencion
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * Codigo del almacen
	 */
	private int almacen;
	
	/**
	 * Mapa de los almacenes
	 */
	private HashMap almacenesMap;
	
	/**
	 * Codigo de la seccion
	 */
	private String seccion;
	
	/**
	 * Mapa de las secciones
	 */
	private HashMap seccionesMap;
	
	/**
	 * Codigo de la subseccion
	 */
	private int subseccion;
	
	/**
	 * Mapa de las subsecciones
	 */
	private HashMap subseccionesMap;
	
	/**
	 * Mapa de los articulos
	 */
	private HashMap articulosMap;
	
	/**
	 * Mapa de los articulos de la busqueda avanzada
	 */
	private HashMap articulosBusquedaMap;
	
	/**
	 * 
	 */
	private int codigoArticulo;
	
	/**
	 * 
	 */
	private int codigoArticuloBusqueda;
	
	/**
	 * 
	 */
	private String descripcionArticulo;
	
	/**
	 * 
	 */
	private String ubicacion;
	
	/**
	 * Codigo del detalle de articulos por almacen
	 */	
	private int codigoDetArt;
	
	/**
	 * indica una posicion en el mapa
	 * */
	private String indexMap;
	
	/**
	 * Parametro encargado de mostrar o no la busqueda avanzada
	 */
	private String mostrarBusquedaAvanzada;
	
	/**
	 * 
	 */
	private String mostrarPopupBusquedaAvanzada;
	
	/**
	 * 
	 */
	private String descripcionArticuloBusqueda;
// --------------------- 	METODOS	
	
	public void reset( int codigoInstitucion, int centroAtencion) {
		this.centroAtencion=centroAtencion;
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.almacen = ConstantesBD.codigoNuncaValido;
		this.almacenesMap = UtilidadInventarios.listadoAlmacensActivos(codigoInstitucion, false);
		this.seccion = "";
		this.seccionesMap = new HashMap();
		this.seccionesMap.put("numRegistros", "0");
		this.subseccion = ConstantesBD.codigoNuncaValido;
		this.subseccionesMap = new HashMap();
		this.subseccionesMap.put("numRegistros", "0");
		this.articulosMap = new HashMap();
		this.articulosMap.put("numRegistros", "0");
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;
		this.codigoArticuloBusqueda=ConstantesBD.codigoNuncaValido;
		this.descripcionArticulo="";
		this.descripcionArticuloBusqueda="";
		this.ubicacion="";
		this.mostrarBusquedaAvanzada=ConstantesBD.acronimoNo;
		this.mostrarPopupBusquedaAvanzada=ConstantesBD.acronimoNo;
		this.articulosBusquedaMap = new HashMap();
		this.articulosBusquedaMap.put("numRegistros", "0");	
	}

// ----------------------   SET Y GET

	/**
	 * @return the almacenesMap
	 */
	public HashMap getAlmacenesMap() {
		return almacenesMap;
	}

	/**
	 * @param almacenesMap the almacenesMap to set
	 */
	public void setAlmacenesMap(HashMap almacenesMap) {
		this.almacenesMap = almacenesMap;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centrosAtencionMap
	 */
	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
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
	 * @return the seccionesMap
	 */
	public HashMap getSeccionesMap() {
		return seccionesMap;
	}

	/**
	 * @param seccionesMap the seccionesMap to set
	 */
	public void setSeccionesMap(HashMap seccionesMap) {
		this.seccionesMap = seccionesMap;
	}

	/**
	 * @return the subseccion
	 */
	public int getSubseccion() {
		return subseccion;
	}

	/**
	 * @param subseccion the subseccion to set
	 */
	public void setSubseccion(int subseccion) {
		this.subseccion = subseccion;
	}

	/**
	 * @return the subseccionesMap
	 */
	public HashMap getSubseccionesMap() {
		return subseccionesMap;
	}

	/**
	 * @param subseccionesMap the subseccionesMap to set
	 */
	public void setSubseccionesMap(HashMap subseccionesMap) {
		this.subseccionesMap = subseccionesMap;
	}

	/**
	 * @return the almacen
	 */
	public int getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen the almacen to set
	 */
	public void setAlmacen(int almacen) {
		this.almacen = almacen;
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the codigoPk
	 */
	public String getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the articulosMap
	 */
	public HashMap getArticulosMap() {
		return articulosMap;
	}
	
	/**
	 * @return the articulosMap
	 */
	public Object getArticulosMap(String key) {
		return articulosMap.get(key);
	}

	/**
	 * @param articulosMap the articulosMap to set
	 */
	public void setArticulosMap(HashMap articulosMap) {
		this.articulosMap = articulosMap;
	}
	
	public void setArticulosMap(String key,Object value) {
		this.articulosMap.put(key, value);
	}

	/**
	 * @return the codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the ubicacion
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * @return the descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * @param descripcionArticulo the descripcionArticulo to set
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	/**
	 * @return the codigoDetArt
	 */
	public int getCodigoDetArt() {
		return codigoDetArt;
	}

	/**
	 * @param codigoDetArt the codigoDetArt to set
	 */
	public void setCodigoDetArt(int codigoDetArt) {
		this.codigoDetArt = codigoDetArt;
	}

	/**
	 * @return the indexMap
	 */
	public String getIndexMap() {
		return indexMap;
	}

	/**
	 * @param indexMap the indexMap to set
	 */
	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
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
	 * @return the mostrarBusquedaAvanzada
	 */
	public String getMostrarBusquedaAvanzada() {
		return mostrarBusquedaAvanzada;
	}

	/**
	 * @param mostrarBusquedaAvanzada the mostrarBusquedaAvanzada to set
	 */
	public void setMostrarBusquedaAvanzada(String mostrarBusquedaAvanzada) {
		this.mostrarBusquedaAvanzada = mostrarBusquedaAvanzada;
	}

	/**
	 * @return the mostrarPopupBusquedaAvanzada
	 */
	public String getMostrarPopupBusquedaAvanzada() {
		return mostrarPopupBusquedaAvanzada;
	}

	/**
	 * @param mostrarPopupBusquedaAvanzada the mostrarPopupBusquedaAvanzada to set
	 */
	public void setMostrarPopupBusquedaAvanzada(String mostrarPopupBusquedaAvanzada) {
		this.mostrarPopupBusquedaAvanzada = mostrarPopupBusquedaAvanzada;
	}

	/**
	 * @return the articulosBusquedaMap
	 */
	public HashMap getArticulosBusquedaMap() {
		return articulosBusquedaMap;
	}

	/**
	 * @param articulosBusquedaMap the articulosBusquedaMap to set
	 */
	public void setArticulosBusquedaMap(HashMap articulosBusquedaMap) {
		this.articulosBusquedaMap = articulosBusquedaMap;
	}

	/**
	 * @return the codigoArticuloBusqueda
	 */
	public int getCodigoArticuloBusqueda() {
		return codigoArticuloBusqueda;
	}

	/**
	 * @param codigoArticuloBusqueda the codigoArticuloBusqueda to set
	 */
	public void setCodigoArticuloBusqueda(int codigoArticuloBusqueda) {
		this.codigoArticuloBusqueda = codigoArticuloBusqueda;
	}

	/**
	 * @return the descripcionArticuloBusqueda
	 */
	public String getDescripcionArticuloBusqueda() {
		return descripcionArticuloBusqueda;
	}

	/**
	 * @param descripcionArticuloBusqueda the descripcionArticuloBusqueda to set
	 */
	public void setDescripcionArticuloBusqueda(String descripcionArticuloBusqueda) {
		this.descripcionArticuloBusqueda = descripcionArticuloBusqueda;
	}
	
	
}
