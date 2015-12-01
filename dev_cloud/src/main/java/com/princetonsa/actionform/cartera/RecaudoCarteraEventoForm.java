package com.princetonsa.actionform.cartera;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import com.princetonsa.mundo.cartera.RecaudoCarteraEvento;


/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class RecaudoCarteraEventoForm extends ValidatorForm
{
	
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manejar los logger de la clase RecaudoCarteraEventoForm
	 */
	Logger logger = Logger.getLogger(RecaudoCarteraEventoForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/*--------------------------------------------------------
	 * 	atributos para el manejo de los indices
	 ---------------------------------------------------------*/
		private static String [] indicesCriterios = RecaudoCarteraEvento.indicesCriterios;
	/*----------------------------------------------------------
	 * fin de atributos para el manejo de los indices
	 *---------------------------------------------------------/
	/*--------------------------------------------------------
	 * Atributos para de la forma
	 -----------------------------------------------------------*/
	
	/**
	 * Estado de la forma.
	 */
	private String estado;
	
	/**
	 * Encargado de almacenar los criterios para la busqueda
	 */
	private HashMap criterios;
	
	/**
	 * almacena el listado de los tipos de convenios
	 */
	private ArrayList<HashMap<String, Object>> tiposConvenio;
	
	/**
	 * almacena el listado de los convenios
	 */
	private ArrayList<HashMap<String, Object>> convenios;
	
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
	
	/*--------------------------------------
	 *  fin de atributos de la forma
	 ----------------------------------------*/
	
	
	
	

	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	

	public HashMap getCriterios() {
		return criterios;
	}


	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}

	
	public Object getCriterios(String key) {
		return criterios.get(key);
	}


	public void setCriterios(String key, Object value) {
		this.criterios.put(key, value);
	}

	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	


	public ArrayList<HashMap<String, Object>> getTiposConvenio() {
		return tiposConvenio;
	}


	public void setTiposConvenio(ArrayList<HashMap<String, Object>> tiposConvenio) {
		this.tiposConvenio = tiposConvenio;
	}


	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}


	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}


	public boolean isOperacionTrue() {
		return operacionTrue;
	}


	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}


	public String getRuta() {
		return ruta;
	}


	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	

	public String getUrlArchivo() {
		return urlArchivo;
	}


	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}


	public boolean isExisteArchivo() {
		return existeArchivo;
	}


	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}


	
	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	
	
	public void reset ()
	{
		this.criterios = new HashMap ();
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.tiposConvenio = new ArrayList<HashMap<String,Object>>();
		this.operacionTrue=false;
		this.ruta="";
		this.urlArchivo="";
		
	}
	
	
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		
		
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
		
		if (estado.equals("buscar"))
		{
			
			//1)validacion requrido fecha incial
			if ((criterios.get(indicesCriterios[0])+"").equals("") || (criterios.get(indicesCriterios[0])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Inicial"));
			else//2) validacion de fecha inicial sea menor o igual a la fecha del sistema
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(criterios.get(indicesCriterios[0])+"",UtilidadFecha.getFechaActual()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+criterios.get(indicesCriterios[0]), "Actual "+UtilidadFecha.getFechaActual()));
			//3) validacion de requerido fecha final
			if ((criterios.get(indicesCriterios[1])+"").equals("") || (criterios.get(indicesCriterios[1])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Final"));
			else//4)validacion de que la fecha final sea mayor o igual a la fecha inicial
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(criterios.get(indicesCriterios[0])+"", criterios.get(indicesCriterios[1])+""))
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final "+criterios.get(indicesCriterios[1]), "Inicial "+criterios.get(indicesCriterios[0])));
				else//5)validacion de que la fecha final sea menor a la fecha actual
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(criterios.get(indicesCriterios[1])+"",UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+criterios.get(indicesCriterios[1]), "Actual "+UtilidadFecha.getFechaActual()));

			//6) se pregunta si las fechas vienen sin errores; de ser asi se evalua
			//si la cantidad de dias entre fechas supera 90.
			if (errores.isEmpty())
				if(UtilidadFecha.numeroMesesEntreFechasExacta(criterios.get(indicesCriterios[0])+"", criterios.get(indicesCriterios[1])+"")>3)
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "90 dias"));
			
		
			//7)validacion requrido tipo de reporte
			if ((criterios.get(indicesCriterios[2])+"").equals("") || (criterios.get(indicesCriterios[2])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el tipo de reporte"));
		
			//8)validacion requerido el tipo de salida del reporte
			if ((criterios.get(indicesCriterios[5])+"").equals("") || (criterios.get(indicesCriterios[5])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar salida reporte"));
			
			if (!errores.isEmpty())
				setEstado("inicial");
		}
		
		
		
		return errores;
	}




	
	
}