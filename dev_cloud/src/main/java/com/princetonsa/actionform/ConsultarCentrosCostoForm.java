/*
 * @(#)ConsultarCentrosCostoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform;

import java.util.HashMap;
import org.apache.struts.action.ActionForm;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Consulta de Centros de Costo
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 11 /May/ 2006
 */
public class ConsultarCentrosCostoForm extends ActionForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaCentrosCosto;
	
	/**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
    /**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
 	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Int para el codigo del centro de atencion
	 */
	private int codCentroAtencion;
	
	/**
	 * String para el Identificador del centro de costo
	 */
	private String identificador;
	
	/**
	 * String para la descripcion(nombre) del centro de costo
	 */
	private String descripcion;
	
	/**
	 * Entero para el codigo del tipo de area
	 */
	private int codigoTipoArea;
	
	/**
	 * String para el manejo de camas de un centro de costo
	 */
	private String manejoCamas;
	
	/**
	 * String para el acronimo de las unidades funcionales
	 */
	private String acronimoUnidadFuncional="";
	
	/**
	 * 
	 */
	private String codigo_interfaz="";
	
	/**
	 * String para saber si un centro de costo esta activo
	 */
	private String activo;
	
	
	/**
	 * String descripcion del Centro de Atencion
	 * */
	private String descripcionCentroAtencion;
	
	
	/**
	 * String descripcion del area
	 * */
	private String descripcionArea;
	
	/**
	 * String Unidad Funcional
	 * */
	private String descripcionUnidadFuncional;
 	
	
	private String tipoEntidad;
	
 	/**
 	 * Reset generla de la Forma
 	 */
	
	public void reset ()
	{
		this.mapaCentrosCosto = new HashMap ();
		this.estado = "";
		this.linkSiguiente = "";
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.codCentroAtencion = 0;
	 	this.identificador = "";
	 	this.descripcion = "";
	 	this.codigoTipoArea = 0;
	 	this.manejoCamas = "";
	 	this.acronimoUnidadFuncional = "";
	 	this.codigo_interfaz="";
	 	this.activo = "";
	 	this.tipoEntidad="";
	}
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaCentrosCosto = new HashMap ();
	}
 	
	
	
	
	public String getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	/**
	 * @return Returns the acronimoUnidadFuncional.
	 */
	public String getAcronimoUnidadFuncional()
	{
		return acronimoUnidadFuncional;
	}

	/**
	 * @param acronimoUnidadFuncional The acronimoUnidadFuncional to set.
	 */
	public void setAcronimoUnidadFuncional(String acronimoUnidadFuncional)
	{
		this.acronimoUnidadFuncional=acronimoUnidadFuncional;
	}

	/**
	 * @return Returns the activo.
	 */
	public String getActivo()
	{
		return activo;
	}

	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo)
	{
		this.activo=activo;
	}

	/**
	 * @return Returns the codCentroAtencion.
	 */
	public int getCodCentroAtencion()
	{
		return codCentroAtencion;
	}

	/**
	 * @param codCentroAtencion The codCentroAtencion to set.
	 */
	public void setCodCentroAtencion(int codCentroAtencion)
	{
		this.codCentroAtencion=codCentroAtencion;
	}

	/**
	 * @return Returns the codigoTipoArea.
	 */
	public int getCodigoTipoArea()
	{
		return codigoTipoArea;
	}

	/**
	 * @param codigoTipoArea The codigoTipoArea to set.
	 */
	public void setCodigoTipoArea(int codigoTipoArea)
	{
		this.codigoTipoArea=codigoTipoArea;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}

	/**
	 * @return Returns the identificador.
	 */
	public String getIdentificador()
	{
		return identificador;
	}

	/**
	 * @param identificador The identificador to set.
	 */
	public void setIdentificador(String identificador)
	{
		this.identificador=identificador;
	}

	/**
	 * @return Returns the manejoCamas.
	 */
	public String getManejoCamas()
	{
		return manejoCamas;
	}

	/**
	 * @param manejoCamas The manejoCamas to set.
	 */
	public void setManejoCamas(String manejoCamas)
	{
		this.manejoCamas=manejoCamas;
	}

	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron=ultimoPatron;
	}

	/**
	 * @return Returns the mapaCentrosCosto.
	 */
	public HashMap getMapaCentrosCosto()
	{
		return mapaCentrosCosto;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCentrosCosto to set.
	 */
	public void setMapaCentrosCosto(HashMap mapaCentrosCosto)
	{
		this.mapaCentrosCosto = mapaCentrosCosto;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCosto(String key) 
	{
		return mapaCentrosCosto.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCosto(String key, Object value) 
	{
		mapaCentrosCosto.put(key, value);
	}

	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset=offset;
	}

	public String getCodigo_interfaz() {
		return codigo_interfaz;
	}

	public void setCodigo_interfaz(String codigo_interfaz) {
		this.codigo_interfaz = codigo_interfaz;
	}

	/**
	 * @return the descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}

	/**
	 * @param descripcionCentroAtencion the descripcionCentroAtencion to set
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}

	/**
	 * @return the descripcionArea
	 */
	public String getDescripcionArea() {
		return descripcionArea;
	}

	/**
	 * @param descripcionArea the descripcionArea to set
	 */
	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}

	/**
	 * @return the descripcionUnidadFuncional
	 */
	public String getDescripcionUnidadFuncional() {
		return descripcionUnidadFuncional;
	}

	/**
	 * @param descripcionUnidadFuncional the descripcionUnidadFuncional to set
	 */
	public void setDescripcionUnidadFuncional(String descripcionUnidadFuncional) {
		this.descripcionUnidadFuncional = descripcionUnidadFuncional;
	}
}