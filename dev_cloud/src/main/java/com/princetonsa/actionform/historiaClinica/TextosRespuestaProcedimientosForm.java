package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * Anexo 714
 * Creado el 17 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
*/
public class TextosRespuestaProcedimientosForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/**
	 * Para manjar los logger de la clase TextosRespuestaProcedimientosForm
	 */
	
	Logger logger = Logger.getLogger(TextosRespuestaProcedimientosForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/*-----------------------------------------------
	 * 	ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	
	/**
	 * String Patron Ordenar 
	 */
	
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 **/
	private String ultimoPatron;
	
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber;	
	
	/*-----------------------------------------------
	 * 	FIN ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/

	/*---------------------------------------------------
	 * 	 ATRIBUTOS PARA TEXTOS RESPUESTA PROCEDIMIENTOS HISTORIA CLINICA
	 ---------------------------------------------------*/
	
	/**
	 * Maneja los estados del action
	 */
	
	private String estado="";

	/**
	 * Almacena los datos de Textos Respuesta Procedimientos
	 */
	
	private HashMap textosRespuestaProcedimientos = new HashMap ();
	
	/**
	 * Indica la posicion seleccionada 
	 * del mapa principal. Primer listado
	 */
	private String index=""; 
	
	/**
	 * Mensaje de exito o fracaso en de la operacion
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * Código del servicio, para ser modificado o cargado
	 */
	private String codigoServicio;
	
	/**
	 * Código Cups del servicio, para ser modificado o cargado
	 */
	private String codigoServicioCups;
	
	/**
	 * Descripción del servicio, para ser modificado o cargado
	 */
	private String descripcionServicio;
	
	/**
	 * String para la descripción del texto 
	 */
	private String descripcionTexto;
	
	/**
	 * String para la descripción del texto 
	 */
	private String textoPredeterminado;
	
	/**
	 * String que indica si el texto de respuesta de procedimientos es activo o no?
	 */
	private String activo;
	
	/**
	 * HashMap con la información de textos de respuesta de procedimientos del servicio seleccionado
	 */
	private HashMap textosProcedimientosServicio;
	
	/**
	 * Posición seleccionada del mapa donde se cargan los textos de respuesta de procedimiento
	 */
	private String posicion;
	
	/**
	 * textoMenorA4000 indica si el texto predeterminado es menor a 4000 caracteres
	 */
	private boolean textoMenorA4000;

	/*---------------------------------------------------
	 * 	 FIN ATRIBUTOS PARA TEXTOS RESPUESTA PROCEDIMIENTOS HISTORIA CLINICA
	 ---------------------------------------------------*/
	
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	//----------------------------------------------------------
	
	//---------index-----------------------------------------------------
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	//----------------------------------------------------------------------
	
	//---------- Textos Procedimientos Servicio--------------------------------------
	public HashMap getTextosProcedimientosServicio() {
		return textosProcedimientosServicio;
	}
	public void setTextosProcedimientosServicio(HashMap textosProcedimientosServicio) {
		this.textosProcedimientosServicio = textosProcedimientosServicio;
	}
	public Object getTextosProcedimientosServicio(String key) {
		return textosProcedimientosServicio.get(key);
	}
	public void setTextosProcedimientosServicio(String key, Object value) {
		this.textosProcedimientosServicio.put(key, value);
	}
	//--------------------------------------------------------
	
	//---------- Textos Respuesta Procedimientos--------------------------------------
	public HashMap getTextosRespuestaProcedimientos() {
		return textosRespuestaProcedimientos;
	}
	public void setTextosRespuestaProcedimientos(HashMap textosRespuestaProcedimientos) {
		this.textosRespuestaProcedimientos = textosRespuestaProcedimientos;
	}
	public Object getTextosRespuestaProcedimientos(String key) {
		return textosRespuestaProcedimientos.get(key);
	}
	public void setTextosRespuestaProcedimientos(String key,Object value) {
		this.textosRespuestaProcedimientos.put(key, value);
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
	//--------------------------------------------------------

	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/

	/*-------------------------------------------------------
	 * 						METODOS
	 --------------------------------------------------------*/
	
	public void reset()
	{
		logger.info("\nMira aqui Pipe !!! -> Entre al forma.reset !!!");
		this.textosRespuestaProcedimientos = new HashMap ();
		this.setTextosRespuestaProcedimientos("numRegistros",0);
		this.textosProcedimientosServicio = new HashMap ();
		this.setTextosProcedimientosServicio("numRegistros",0);
		this.codigoServicio = "";
		this.codigoServicioCups = "";
		this.descripcionServicio = "";
		this.descripcionTexto = "";
		this.textoPredeterminado = "";
		this.activo = ConstantesBD.acronimoSi;
		this.index = "";
		this.posicion = "";
	}
	
	public void resetpager()
	{
		logger.info("\nMira aqui Pipe !!! -> Entre al forma.resetpager :P !!!");
		//PARA EL MANEJO DEL PAGER
    	this.currentPageNumber = 1;
        this.linkSiguiente = "";
        this.offset = 0;
        this.patronOrdenar = "";
    	this.ultimoPatron = "";
	}
	
	public void resetMensaje()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
	
	public void resetNuevo()
	{
		this.descripcionTexto = "";
		this.textoPredeterminado = "";
		this.activo = ConstantesBD.acronimoSi;
	}
	
	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		return errores;
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
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}
	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	/**
	 * @return the descripcionTexto
	 */
	public String getDescripcionTexto() {
		return descripcionTexto;
	}
	/**
	 * @param descripcionTexto the descripcionTexto to set
	 */
	public void setDescripcionTexto(String descripcionTexto) {
		this.descripcionTexto = descripcionTexto;
	}
	/**
	 * @return the textoPredeterminado
	 */
	public String getTextoPredeterminado() {
		return textoPredeterminado;
	}
	/**
	 * @param textoPredeterminado the textoPredeterminado to set
	 */
	public void setTextoPredeterminado(String textoPredeterminado) {
		this.textoPredeterminado = textoPredeterminado;
	}
	/**
	 * @return the descripcionServicio
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}
	/**
	 * @param descripcionServicio the descripcionServicio to set
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}
	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	/**
	 * @return the codigoServicioCups
	 */
	public String getCodigoServicioCups() {
		return codigoServicioCups;
	}
	/**
	 * @param codigoServicioCups the codigoServicioCups to set
	 */
	public void setCodigoServicioCups(String codigoServicioCups) {
		this.codigoServicioCups = codigoServicioCups;
	}
	/**
	 * @return the posicion
	 */
	public String getPosicion() {
		return posicion;
	}
	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	public void setTextoMenorA4000(boolean textoMenorA4000) {
		this.textoMenorA4000 = textoMenorA4000;
	}
	public boolean getTextoMenorA4000() {
		return textoMenorA4000;
	}
	
	/*-------------------------------------------------------
	 *					FIN METODOS
	 --------------------------------------------------------*/
}