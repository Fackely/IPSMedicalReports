package com.princetonsa.actionform.facturasVarias;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.mundo.facturasVarias.ConsultaImpresionAjustesFacturasVarias;
import util.ConstantesBD;

/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsultaImpresionAjustesFacturasVariasForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manejar los logger de la clase ConsultaImpresionAjustesFacturasVariasForm
	 */
	Logger logger = Logger.getLogger(ConsultaImpresionAjustesFacturasVariasForm.class);
	
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
	
	
	
	/*--------------------------------------------------------------
	 * 	TRIBUTOS DE GEMERACION MODIFICACION AJUSTES FACTURAS VARIAS
	 ---------------------------------------------------------------*/
	/**
	 * Estado de la forma.
	 */
	private String estado;
	
	/**
	 * Mapa que almacena los diferentes filtros que puede
	 * contener la busqueda de ajustes a facturas.
	 * este mapa no tiene y no necita un key numRegistros.
	 */
	private HashMap filtrosBusqueda;
	
	/**
	 * Listado con la informacion de la consulta
	 */
	private HashMap listado;
	
	/**
	 * String encargado de manejar la posicion dentro del
	 * mapa de la busqueda.
	 */
	private String index = ConstantesBD.codigoNuncaValido+"";
	

	/**
	 * Mapa que almacena la informacion del ajuste.
	 * este mapa no necita un key numRegistros.
	 */
	private HashMap mapaAjustes;
	
	/**
	 * Mapa que almacena la informacion de la factura
	 */
	private HashMap mapaInfoFac;
	
	private boolean operacionTrue = false;
	
	private String codigoDeudor;
	private String descripDeudor;
	private String tipoDeudor;
	
	
	private String [] criteriosBusqueda= ConsultaImpresionAjustesFacturasVarias.indicesCriteriosBusqueda;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> conceptosFacVar = new ArrayList<HashMap<String,Object>>();
	
	
	/*-------------------------------------------------------------------
	 * 	FIN TRIBUTOS DE GEMERACION MODIFICACION AJUSTES FACTURAS VARIAS
	 -------------------------------------------------------------------*/
	
	/*-----------------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
//-----------------------------------------------------------------------
	public ArrayList<HashMap<String, Object>> getConceptosFacVar() {
		return conceptosFacVar;
	}
	public void setConceptosFacVar(
			ArrayList<HashMap<String, Object>> conceptosFacVar) {
		this.conceptosFacVar = conceptosFacVar;
	}
	//-------------------------------------------------------------------
	//------------------------------------------------------------------
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	//-----------------------------------------------------------------
	
	//----------- mapa ajustes --------------------------------------	
	public HashMap getMapaAjustes() {
		return mapaAjustes;
	}
	public void setMapaAjustes(HashMap mapaAjustes) {
		this.mapaAjustes = mapaAjustes;
	}
	public Object getMapaAjustes(String key) {
		return mapaAjustes.get(key);
	}
	public void setMapaAjustes(String key,Object value) {
		this.mapaAjustes.put(key, value);
	}
	//-----------------------------------------------------------------
	
	//------------ mapa iformacion de factura ------------------------
	public HashMap getMapaInfoFac() {
		return mapaInfoFac;
	}
	public void setMapaInfoFac(HashMap mapaInfoFac) {
		this.mapaInfoFac = mapaInfoFac;
	}
	public Object getMapaInfoFac(String key) {
		return mapaInfoFac.get(key);
	}
	public void setMapaInfoFac(String key,Object value) {
		this.mapaInfoFac.put(key, value);
	}
	//-----------------------------------------------------------------
	//---------index----------------------------------------------------
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	//------------------------------------------------------------------
	//--------- FILTROS BUSQUEDA --------------------------------------------
	public HashMap getFiltrosBusqueda() {
		return filtrosBusqueda;
	}
	public void setFiltrosBusqueda(HashMap filtrosBusqueda) {
		this.filtrosBusqueda = filtrosBusqueda;
	}
	public Object getFiltrosBusqueda(String key) {
		return filtrosBusqueda.get(key);
	}
	public void setFiltrosBusqueda(String key,Object value) {
		this.filtrosBusqueda.put(key, value);
	}
	//------------------------------------------------------------------------
	//---------LISTADO---------------------------------------------------------
	public HashMap getListado() {
		return listado;
	}
	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	public Object getListado(String key) {
		return listado.get(key);
	}
	public void setListado(String key,Object value) {
		this.listado.put(key, value);
	}
	//-------------------------------------------------------------------
	//------------------------------------------------------------------------
	//-----------manejo pager y ordenamiento -----------------------
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
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	//----------------------------------------------------------------------------
	
	//--------manejo de estados controlador --------------------------------------
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
		
			}
	
	
	
	
	public String getTipoDeudor() {
		return tipoDeudor;
	}
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}
	public String getCodigoDeudor() {
		return codigoDeudor;
	}
	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}
	public String getDescripDeudor() {
		return descripDeudor;
	}
	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
	}
	
//--------------------------------------------------------------------------------
	
	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/

	
	
	
	/*-------------------------------------------------------
	 * 				  METODOS ADICIONALES
	 --------------------------------------------------------*/
	public void reset ()
	{
		this.listado = new HashMap ();
		this.setListado("numRegistros", 0);
		this.filtrosBusqueda = new HashMap ();
		this.index=ConstantesBD.codigoNuncaValido+"";
		this.descripDeudor="";
		this.codigoDeudor="";
		this.tipoDeudor="";
	}
	
	public void resetBusqueda()
	{
		this.listado = new HashMap ();
		this.setListado("numRegistros", 0);
	}

	public void resetFactura ()
	{
		this.mapaInfoFac = new HashMap ();
	}
	
	public void resetAjsutes()
	{
		this.mapaAjustes = new HashMap ();
		this.operacionTrue=false;
	}

	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
				
		return errores;
	}
	
}