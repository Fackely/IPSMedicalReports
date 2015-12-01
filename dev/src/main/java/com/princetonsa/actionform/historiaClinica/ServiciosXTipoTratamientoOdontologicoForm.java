package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.historiaClinica.ServiciosXTipoTratamientoOdontologico;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.Utilidades;




/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class ServiciosXTipoTratamientoOdontologicoForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase ServiciosXTipoTratamientoOdontologicoForm
	 */
	Logger logger = Logger.getLogger(ServiciosXTipoTratamientoOdontologicoForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	//-----------indices-------------------------------
	private static String  [] indicesServicioXTipoTratamientoOdontologico = ServiciosXTipoTratamientoOdontologico.indicesServicioXTipoTratamientoOdontologico;

	/*--------------------------------------------------------
	 * 	ATRIBUTOS DE SERVICIOS X TIPO TRATAMIENTO ODONTOLOGICO
	 ---------------------------------------------------------*/
	
	private String estado= "";

	/**
	 * almacena la informacion parametrizada de 
	 * servicios por tipo de tratamiento odontologico
	 */
	private HashMap servXTipTratOdont = new HashMap ();
	
	private HashMap servXTipTratOdontOld = new HashMap ();
	
	private ArrayList<HashMap<String, Object>> tiposTratamientoOdontologico = new ArrayList<HashMap<String,Object>>();
	
	private boolean operacionTrue = false;
	
	private String index = "";
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	private String tipoTratamiento = "";
	
	private HashMap eliminados = new HashMap ();
	
	/*--------------------------------------------------------
	 * FIN ATRIBUTOS DE SERVICIOS X TIPO TRATAMIENTO ODONTOLOGICO
	 ---------------------------------------------------------*/
	
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	//------------------------------------------------------------------------------------
	public HashMap getEliminados() {
		return eliminados;
	}
	public void setEliminados(HashMap eliminados) {
		this.eliminados = eliminados;
	}
	public Object getEliminados(String key) {
		return eliminados.get(key);
	}
	public void setEliminados(String key,Object value) {
		this.eliminados.put(key, value);
	}
	//------------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------------
	public HashMap getServXTipTratOdontOld() {
		return servXTipTratOdontOld;
	}
	public void setServXTipTratOdontOld(HashMap servXTipTratOdontOld) {
		this.servXTipTratOdontOld = servXTipTratOdontOld;
	}
	public Object getServXTipTratOdontOld(String key) {
		return servXTipTratOdontOld.get(key);
	}
	public void setServXTipTratOdontOld(String key,Object value) {
		this.servXTipTratOdontOld.put(key, value);
	}
	//--------------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------------
	public String getTipoTratamiento() {
		return tipoTratamiento;
	}
	public void setTipoTratamiento(String tipoTratamiento) {
		this.tipoTratamiento = tipoTratamiento;
	}
	//---------------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------------
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	//-------------------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------------
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	//------------------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------------------
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	//-----------------------------------------------------------------------------------------------
	
	//----------------------------------------------------------------------------------------------
	public ArrayList<HashMap<String, Object>> getTiposTratamientoOdontologico() {
		return tiposTratamientoOdontologico;
	}
	public void setTiposTratamientoOdontologico(
			ArrayList<HashMap<String, Object>> tiposTratamientoOdontologico) {
		this.tiposTratamientoOdontologico = tiposTratamientoOdontologico;
	}
	//------------------------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------------------------
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	//-----------------------------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------------------------
	public HashMap getServXTipTratOdont() {
		return servXTipTratOdont;
	}
	public void setServXTipTratOdont(HashMap servXTipTratOdont) {
		this.servXTipTratOdont = servXTipTratOdont;
	}
	public Object getServXTipTratOdont(String key) {
		return servXTipTratOdont.get(key);
	}
	public void setServXTipTratOdont(String key,Object value) {
		this.servXTipTratOdont.put(key, value);
	}
	//-------------------------------------------------------------------------------------------------------
	
	
	/*---------------------------------------------------------
	 * 			FIN	METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	/*---------------------------------------------------------
	 * 						METODOS  ADICIONALES
	 ----------------------------------------------------------*/
	public void reset ()
	{
		this.servXTipTratOdont = new HashMap ();
		this.setServXTipTratOdont("numRegistros", 0);
		this.servXTipTratOdontOld = new HashMap ();
		this.setServXTipTratOdontOld("numRegistros", 0);
		this.operacionTrue=false;
		this.setIndex("");
		this.setLinkSiguiente("");
		this.setTipoTratamiento("");
		this.eliminados = new HashMap ();
		this.setEliminados("numRegistros", 0);
	}
	
	public void reset_servicios ()
	{
		this.servXTipTratOdont= new HashMap ();
		this.setServXTipTratOdont("numRegistros", 0);
		this.eliminados= new HashMap();
		this.setEliminados("numRegistros", 0);
		this.setIndex("");
		this.operacionTrue=false;
	}
	
	
	/*---------------------------------------------------------
	 *					FIN METODOS  ADICIONALES
	 ----------------------------------------------------------*/
	
	/*--------------------------------------------------------
	 * 						VALIDATE
	 --------------------------------------------------------*/	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{	
		ActionErrors errores = super.validate(mapping, request); 
		
		if (this.estado.equals("guardar"))
		{
			int numReg = Utilidades.convertirAEntero(this.servXTipTratOdont.get("numRegistros")+"");
			for (int i=0;i<numReg;i++)
			{
				if (!UtilidadCadena.noEsVacio(this.servXTipTratOdont.get(indicesServicioXTipoTratamientoOdontologico[5]+i)+"") ||(this.servXTipTratOdont.get(indicesServicioXTipoTratamientoOdontologico[5]+i)+"").equals(ConstantesBD.codigoNuncaValido+"") )
						errores.add("descripcion",new ActionMessage("errors.required","El servicio del registro Nro. "+(i+1)));	
				
				String llave1=this.servXTipTratOdont.get(indicesServicioXTipoTratamientoOdontologico[5]+i)+this.tipoTratamiento;
				
				for (int j=(i+1);j<numReg;j++)
				{
					String llave2=this.servXTipTratOdont.get(indicesServicioXTipoTratamientoOdontologico[5]+j)+this.tipoTratamiento;
					
					if (llave1.equals(llave2))
						errores.add("descripcion",new ActionMessage("errors.yaExisteAmplio","El registro Nro. "+(i+1)));	
						
						
				}
		
			}
		}
		
		
		
		return errores;
	}
	
	/*--------------------------------------------------------
	 * 					FIN VALIDATE
	 --------------------------------------------------------*/	
	
}