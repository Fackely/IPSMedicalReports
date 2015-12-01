package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.mundo.manejoPaciente.AperturaIngresos;
import util.ConstantesBD;




/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class AperturaIngresosForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase AperturaIngresosForm
	 */
	Logger logger = Logger.getLogger(AperturaIngresosForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	String [] indicesListado = AperturaIngresos.indicesListado;
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 * estas variables no son usadas actualmente
	 * pero se ponen por si adiciona un pager
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
	
	/*------------------------------------------------
	 * 			ATRIBUTOS DE APERTURA INGRESOS  
	 -------------------------------------------------*/
	
	/**
	 * Mapa encargado de almacenar los ingresos de 
	 * un paciente.
	 */
	private HashMap listadoIngresos = new HashMap ();
	
	/**
	 * maneja los estados del action
	 */
	private String estado = "";
	
	/**
	 * maneja el indice seleccionado en la vista
	 */
	private String index=ConstantesBD.codigoNuncaValido+"";
	
	/**
	 * almacena la informacion del ingreso a reabrir
	 */
	private HashMap ingreso = new HashMap ();

	/**
	 * Almacena el listado de motivos de cierre o de apertura
	 */
	private ArrayList<HashMap<String, Object>> motivoCierre = new ArrayList<HashMap<String,Object>>();
	
	/**
	 */
	private boolean operacionTrue=false;
	/*------------------------------------------------
	 * 		FIN	ATRIBUTOS DE APERTURA INGRESOS  
	 -------------------------------------------------*/
	
	
	/*---------------------------------------------------------
	 * 				METODOS SETTER AND GETTERS
	 -----------------------------------------------------------*/
	
//-----motivo cierre---------------------------------------------------------
	public ArrayList<HashMap<String, Object>> getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(ArrayList<HashMap<String, Object>> motivoCierre) {
		this.motivoCierre = motivoCierre;
	}
//--------------------------------------------------------------------------------
	
	
//----ingreso --------------------------------
	public HashMap getIngreso() {
		return ingreso;
	}

	public void setIngreso(HashMap ingreso) {
		this.ingreso = ingreso;
	}
	
	public Object getIngreso(String key) {
		return ingreso.get(key);
	}

	public void setIngreso(String key,Object value) {
		this.ingreso.put(key, value);
	}

	
//--------------------------------------------------
	//----index--------------------------------------
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
//------------------------------------------------

	
	//----------pager ------------------------------	
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
//-----------------------------------------------------------------
	
//-------------------estado---------------------------------	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
//----------------------------------------------------------
	
//-----listado ingresos ------------------------------------	
	public HashMap getListadoIngresos() {
		return listadoIngresos;
	}

	public void setListadoIngresos(HashMap listadoIngresos) {
		this.listadoIngresos = listadoIngresos;
	}

	public Object getListadoIngresos(String key) {
		return listadoIngresos.get(key);
	}

	public void setListadoIngresos(String key,Object value) {
		this.listadoIngresos.put(key, value);
	}
//---------------------------------------------------------------
	
	//------ operacion true----------------------------------
	
	

	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
//----------------------------------------------------------
	
	/*---------------------------------------------------------
	 * 				FIN METODOS SETTERS AND GETTERS
	 ---------------------------------------------------------*/
	
	public void reset ()
	{
		this.listadoIngresos= new HashMap ();
		this.setListadoIngresos("numRegistros", 0);
		this.motivoCierre = new ArrayList<HashMap<String,Object>>();
		this.operacionTrue = false;
		
		
	}
	
	
	/*---------------------------------------------------------
	 * 						METODO VALIDATE
	 ---------------------------------------------------------*/
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores = super.validate(mapping, request);
		
		if (estado.equals("guardar"))
		{
			if ((getIngreso(indicesListado[14])+"").equals("") || (getIngreso(indicesListado[14])+"").equals("null") || (getIngreso(indicesListado[14])+"").equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("descripcion",new ActionMessage("errors.required","El Motivo Apertura"));
		}
		
		
			
		return errores;
	}
	/*---------------------------------------------------------
	 * 					  FIN METODO VALIDATE
	 ---------------------------------------------------------*/

	
	
	
}