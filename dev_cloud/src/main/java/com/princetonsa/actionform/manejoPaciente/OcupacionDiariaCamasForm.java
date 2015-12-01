package com.princetonsa.actionform.manejoPaciente;


import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.mundo.manejoPaciente.OcupacionDiariaCamas;

import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import javax.servlet.http.HttpServletRequest;



/**
 * Fecha: Febrero - 2008
 * @author Jhony Alexander Duque A.
 *
 */

public class OcupacionDiariaCamasForm extends ValidatorForm 
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase OcupacionDiariaCamasForm
	 */
	Logger logger = Logger.getLogger(OcupacionDiariaCamasForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
/// indices -------------------
	
	String [] indicesCriterios=OcupacionDiariaCamas.indicesCriterios;
	
	
	/*--------------------------------------------------
	 * 		ATRIBUTOS OCUPACION DIARIA DE CAMAS
	 ---------------------------------------------------*/
	/**
	 * Maneja las acciones a realizar en el action
	 */
	private String estado="";

	/**
	 * Almacena la informacion que devuelve la consulta
	 */
	private HashMap datosCamas = new HashMap ();
	
	/**
	 * Atributo que le indica a la vista si se
	 * genero el archivo plano
	 */
	private boolean operacionTrue;
	
	/**
	 * Atributo que indica donde se almaceno el archivo,
	 * este es para mostrar la ruta excata donde se genero
	 * el archivo dentro del sistema de directorios del
	 * servidor 
	 */
	private String ruta;
	
	/**
	 * atributo que indica la direccion para poder
	 * descargar el archivo
	 */
	private String urlArchivo;
	
	/**
	 * Atributo que almacena si el archivo
	 * .ZIP si se genero
	 */
	private boolean existeArchivo=false; 
	
	/*--------------------------------------------------
	 * 		FIN ATRIBUTOS OCUPACION DIARIA DE CAMAS
	 ---------------------------------------------------*/
	
	/*---------------------------------------------------------
	 *ATRIBUTOS PARA LA BUSQUEDA DE OCUPACION DIARIA DE CAMAS
	 --------------------------------------------------------*/
	/**
	 * almacena los criterios de busqueda
	 * por los cuales se va a filtrar en la consulta.
	 */
	private HashMap criterios = new HashMap ();
	
	/**
	 * almacena los centros de atencion a mostrar en el select
	 */
	private ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
		
	/**
	 * almacena los estados de camas selecionados
	 */
	private HashMap estadosCamas = new HashMap ();   
	
	
	/*-------------------------------------------------------------
	 * FIN ATRIBUTOS PARA LA BUSQUEDA DE OCUPACION DIARIA DE CAMAS
	 -----------------------------------------------------------*/
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	//------------- criterios de busqueda -----------------------------------
	public HashMap getCriterios() {
		return criterios;
	}
	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}
	public Object getCriterios(String key) {
		return criterios.get(key);
	}
	public void setCriterios(String key,Object value) {
		this.criterios.put(key, value);
	}
	//------------------------------------------------------------------------
	
	//-------------------- Centros de Atencion ------------------------------
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	//-------------------------------------------------------------------------
		
	//------------------------datos Cama -------------------------------------
	public HashMap getDatosCamas() {
		return datosCamas;
	}
	public void setDatosCamas(HashMap datosCamas) {
		this.datosCamas = datosCamas;
	}
	public Object getDatosCamas(String key) {
		return datosCamas.get(key);
	}
	public void setDatosCamas(String key, Object value) {
		this.datosCamas.put(key, value);
	}
	//------------------------------------------------------------------------
	
	//-------------------Estado ---------------------------------------------
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	//-----------------------------------------------------------------------
	
	//-------------- estados de la camas -----------------------------------
	public HashMap getEstadosCamas() {
		return estadosCamas;
	}
	public void setEstadosCamas(HashMap estadosCamas) {
		this.estadosCamas = estadosCamas;
	}
	public Object getEstadosCamas(String key) {
		return estadosCamas.get(key);
	}
	public void setEstadosCamas(String key,Object value) {
		this.estadosCamas.put(key, value);
	}
	//-----------------------------------------------------------------------
	
	//---------------operacion true ------------------------------------------
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	//--------------------------------------------------------------------------
	//-------ruta --------------------------------------------------------------
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	//----------------------------------------------------------------------------
	//-------------url archivo----------------------------------------------------
	public String getUrlArchivo() {
		return urlArchivo;
	}
	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}
	//-----------------------------------------------------------------------------
	//--------existe archivo ------------------------------------------------------
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
	//------------------------------------------------------------------------------
	
	
	
	/*---------------------------------------------------------
	 * 			  FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	/*-----------------------------------------------------------
	 * 			  METODOS PARA EL MANEJO DE LA FORMA
	 -----------------------------------------------------------*/
	
	public void reset ()
	{
		this.criterios = new HashMap ();
		this.datosCamas = new HashMap ();
		this.setDatosCamas("numRegistros", 0);
		this.centrosAtencion = new ArrayList<HashMap<String,Object>>();
		this.estadosCamas = new HashMap ();
		this.operacionTrue=false;
		this.ruta="";
		this.urlArchivo="";
		this.existeArchivo=false;
		
	}
	
	/*-----------------------------------------------------------
	 * 			  FIN METODOS PARA EL MANEJO DE LA FORMA
	 -----------------------------------------------------------*/
	
	

	/**
	 * Control de Errores (Validaciones)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (estado.equals("generar"))
		{
			boolean fecha=true;
			if(!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[0])+""))
				errores.add("centroAtencion", new ActionMessage("errors.required", "El Centro de Atención "));
			
			//1)validacion requrido fecha incial
			if (!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[1])+""))
			{
				fecha=false;
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Inicial"));
			}
			else//2)validacion de que la fecha inicial sea menor a la fecha actual
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriterios(indicesCriterios[1])+"",UtilidadFecha.getFechaActual()))
				{
					fecha=false;
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.getCriterios(indicesCriterios[1]), "Actual "+UtilidadFecha.getFechaActual()));
				}
			
			//1)validacion requrido tipo de salida
			if (!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[7])+""))
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el Tipo de Salida"));
			
			//3)validacion requrido fecha Final
			if (!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[2])+""))
			{
				fecha=false;
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Final"));
			}
			else//4)validacion de que la fecha final sea mayor o igual a la fecha inicial
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriterios(indicesCriterios[2])+"",this.getCriterios(indicesCriterios[1])+""))
				{
					
					fecha=false;
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final "+this.getCriterios(indicesCriterios[2]), "Inicial "+this.getCriterios(indicesCriterios[1])));
				}
				else//5)validacion de que la fecha final sea menor a la fecha actual
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriterios(indicesCriterios[2])+"",UtilidadFecha.getFechaActual()))
					{
						fecha=false;
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.getCriterios(indicesCriterios[2]), "Actual "+UtilidadFecha.getFechaActual()));
					}
			
			if (fecha)
			{
				logger.info(" eel rando de fechas da --> "+UtilidadFecha.numeroMesesEntreFechasExacta(this.getCriterios(indicesCriterios[1])+"", this.getCriterios(indicesCriterios[2])+""));
				if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getCriterios(indicesCriterios[1])+"", this.getCriterios(indicesCriterios[2])+"")>0)
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "30 dias"));
			}
		
			int numRegEst=Utilidades.convertirAEntero(this.getEstadosCamas("numRegistros")+"");
			boolean estadoRequerido=false;
			
			for (int i=0;i<numRegEst;i++)
			{
				//logger.info("\n ---1---->> "+this.getEstadosCamas("check_"+i));
				if (UtilidadTexto.getBoolean(this.getEstadosCamas("check_"+i)+""))
					estadoRequerido=true;
			}
			
			if (!estadoRequerido)
				errores.add("tipoSalida", new ActionMessage("errors.required", "Seleccionar almenos un estado de la cama "));
			
			
			
		}
		
		
		return errores;
	}



	

	
}