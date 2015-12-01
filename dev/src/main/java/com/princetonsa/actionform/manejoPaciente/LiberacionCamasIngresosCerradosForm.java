package com.princetonsa.actionform.manejoPaciente;



import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class LiberacionCamasIngresosCerradosForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase LiberacionCamasIngresosCerradosForm
	 */
	Logger logger = Logger.getLogger(LiberacionCamasIngresosCerradosForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
	/*---------------------------------------------------------------
	 * 		ATRIBUTOS DE LIBERACION DE CAMAS INGRESOS CERRADOS 
	 ---------------------------------------------------------------*/
	
	private String estado="";
	
	
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
	
	
	private HashMap listadoIngresos = new HashMap ();
	
	private String index="";
	
	
	private HashMap detalle = new HashMap ();
	/*---------------------------------------------------------------
	 * 		FIN ATRIBUTOS DE LIBERACION DE CAMAS INGRESOS CERRADOS 
	 ---------------------------------------------------------------*/
	
	/*----------------------------------------------------------------------
	 *  						 SETTERS AND GETTERS
	 ----------------------------------------------------------------------*/
	
public HashMap getDetalle() {
		return detalle;
	}

	public void setDetalle(HashMap detalle) {
		this.detalle = detalle;
	}

	public Object getDetalle(String key) {
		return detalle.get(key);
	}

	public void setDetalle(String key,Object value) {
		this.detalle.put(key, value);
	}
	
	//-------estado--------------------------------------------------
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
//-----------------------------------------------------------------------
	
//---listado ingresos ---------------------------------------------------
	public HashMap getListadoIngresos() {
		return listadoIngresos;
	}

	public void setListadoIngresos(HashMap listadoIngresos) {
		this.listadoIngresos = listadoIngresos;
	}
	
	public Object getListadoIngresos(String key) {
		return listadoIngresos.get(key);
	}

	public void setListadoIngresos(String key, Object value) {
		this.listadoIngresos.put(key, value);
	}	
//----------------------------------------------------------------------

//---------index -------------------------------------------------	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
//----------------------------------------------------------------	
//---------manejo pager--------------------------------------------
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

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
//-------------------------------------------------------------------
	
	/*-----------------------------------------------------------------------
	 * 							FIN SETTERS AND GETTERS
	 ----------------------------------------------------------------------*/
	
	public void reset ()
	{
		this.listadoIngresos= new HashMap ();
		this.setListadoIngresos("numRegistros", 0);
		this.index="";
	}

	
	
}