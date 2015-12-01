package com.princetonsa.actionform.glosas;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

/**
 * Anexo 685
 * Fecha: Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class ConceptosEspecificosForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase ConceptosEspecificosForm
	 */
	transient Logger logger = Logger.getLogger(ConceptosEspecificosForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
	/*-----------------------------------------------
	 * 	ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
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
	 * 	FIN ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	

	/*---------------------------------------------------
	 * 	 ATRIBUTOS PARA LA CONCEPTOS ESPECIFICOS GLOSAS
	 ---------------------------------------------------*/
	/**
	 * maneja los estados del action
	 */
	private String estado="";

	/**
	 * Almacena los datos de conceptos especificos
	 */
	private HashMap conceptosEspecificos = new HashMap ();
	
	/**
	 * indicala la posicion seleccionada 
	 * del mapa
	 */
	private String index=""; 
	
	/**
	 * almacena los datos a modificar
	 */
	private HashMap conceptoModificar = new HashMap();
	/**
	 * Almacena una copia de los datos a modificar.
	 */
	private HashMap conceptoModificarClone= new HashMap();
	
	/**
	 * mensaje de exito o fracaso en de la operacion
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * Redireccion para indicar que mensajes se deben de mostrar
	 */
	private boolean redireccion = false;
	
	/**
	 * Mantener Datos es una variable buleana que me indica si debo mantener los datos en los html:text
	 */
	private boolean mantenerDatos=false;
	
	/**
	 * La variable modificación me indica si en la inserción de los datos es una modificación
	 */
	private boolean modificacion=false;
	
	
	/*---------------------------------------------------
	 * 	 FIN ATRIBUTOS PARA LA CONCEPTOS ESPECIFICOS GLOSAS
	 ---------------------------------------------------*/
	
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	//----------------------------------------------------------
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	//----------------------------------------------------------
	
	//-------------------------------------------------------
	public HashMap getConceptoModificar() {
		return conceptoModificar;
	}
	public void setConceptoModificar(HashMap conceptoModificar) {
		this.conceptoModificar = conceptoModificar;
	}
	public Object getConceptoModificar(String key) {
		return conceptoModificar.get(key);
	}
	public void setConceptoModificar(String key, Object value) {
		this.conceptoModificar.put(key, value);
	}
	//------------------------------------------------------------	
	
	//------------------------------------------------------------
	public HashMap getConceptoModificarClone() {
		return conceptoModificarClone;
	}
	public void setConceptoModificarClone(HashMap conceptoModificarClone) {
		this.conceptoModificarClone = conceptoModificarClone;
	}
	public Object getConceptoModificarClone(String key) {
		return conceptoModificarClone.get(key);
	}
	public void setConceptoModificarClone(String key, Object value) {
		this.conceptoModificarClone.put(key, value);
	}
	//---------------------------------------------------------------------
		
	//---------index-----------------------------------------------------
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	//----------------------------------------------------------------------
	//---------- conceptos especificos--------------------------------------
	public HashMap getConceptosEspecificos() {
		return conceptosEspecificos;
	}
	public void setConceptosEspecificos(HashMap conceptosEspecificos) {
		this.conceptosEspecificos = conceptosEspecificos;
	}
	public Object getConceptosEspecificos(String key) {
		return conceptosEspecificos.get(key);
	}
	public void setConceptosEspecificos(String key,Object value) {
		this.conceptosEspecificos.put(key, value);
	}
	//--------------------------------------------------------
	
	
	//------------ patron ordenar------------------------------
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	//--------------------------------------------------------
	//------------------ultimo patron ordenar--------------------------------------
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	//--------------------------------------------------------
	//---------------------link siguiente-----------------------------------
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	//--------------------------------------------------------
	//--------------------estado------------------------------------
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	//----------------------------------------------------
	//-------------------redireccion----------------------
	public void setRedireccion(boolean redireccion) {
		this.redireccion = redireccion;
	}
	public boolean getRedireccion() {
		return redireccion;
	}
	//----------------------------------------------------
	//--------------------------------------------------------

	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	/*-------------------------------------------------------
	 * 						METODOS
	 --------------------------------------------------------*/
	
	
	public void reset()
	{
		this.conceptosEspecificos = new HashMap ();
		this.setConceptosEspecificos("numRegistros",0);
		this.conceptoModificar = new HashMap();
		this.setConceptoModificar("numRegistros",0);
		this.conceptoModificarClone = new HashMap();
		this.setConceptoModificarClone("numRegistros",0);
	}
	
	public void resetModicacion ()
	{
		this.conceptoModificar = new HashMap();
		this.setConceptoModificar("numRegistros",0);
		this.conceptoModificarClone = new HashMap();
		this.setConceptoModificarClone("numRegistros",0);
	}
	
	public void resetpager ()
	{
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
	}
	
	public void resetMensaje ()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
	
	/**
	 * Método encargado de resetear la variable mantenDatos
	 */
	public void resetMantenerDatos()
	{
		this.mantenerDatos=false;
	}
	
	public void setMantenerDatos(boolean mantenerDatos) {
		this.mantenerDatos = mantenerDatos;
	}
	public boolean getMantenerDatos() {
		return mantenerDatos;
	}
	
	
	public void setModificacion(boolean modificacion) {
		this.modificacion = modificacion;
	}
	public boolean getModificacion() {
		return modificacion;
	}
	
	/*-------------------------------------------------------
	 *					FIN METODOS
	 --------------------------------------------------------*/
}