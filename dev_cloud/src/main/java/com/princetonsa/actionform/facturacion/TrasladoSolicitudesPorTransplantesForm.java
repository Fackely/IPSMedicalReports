package com.princetonsa.actionform.facturacion;



import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import com.princetonsa.mundo.facturacion.TrasladoSolicitudesPorTransplantes;






/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public class TrasladoSolicitudesPorTransplantesForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase TransladoSolicitudesPorTransplantesForm
	 */
	Logger logger = Logger.getLogger(TrasladoSolicitudesPorTransplantesForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	//indices
	String [] indicesListado = TrasladoSolicitudesPorTransplantes.indicesListado;
	
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
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DEL TRANSLADO SOLICITUDES POR TRANSPLANTES
	 ------------------------------------------------------*/
	private String estado;
	
	private String index;
	
	/**
	 * Listado de solicitudes del Donante
	 */
	private HashMap listadoSolicitudes = new HashMap ();
	
	private ArrayList<HashMap<String, Object>> centCosto = new ArrayList<HashMap<String,Object>>();
	
	private String fechaApertura="";
	
	private HashMap listadoIngresosReceptores = new HashMap ();
	
	boolean operacionTrue=false;
	/*-----------------------------------------------------------
	 * FIN ATRIBUTOS DEL TRASLADO SOLICITUDES POR TRANSPLANTES
	 -----------------------------------------------------------*/


	/*-------------------------------------------------------
	 * 						METODOS
	 --------------------------------------------------------*/
	
	


	public void reset (boolean resetOperaTrue)
	{
		this.index="";
		this.listadoSolicitudes = new HashMap ();
		this.setListadoSolicitudes("numRegistros", 0);
		this.setListadoSolicitudes(indicesListado[0], false);
		this.setListadoSolicitudes(indicesListado[17], ConstantesBD.acronimoNo);
		this.centCosto = new ArrayList<HashMap<String,Object>>();
		this.fechaApertura="";
		this.listadoIngresosReceptores = new HashMap (); 
		this.setListadoIngresosReceptores("numRegistros", 0);
		if (resetOperaTrue)
			this.setOperacionTrue(false);
	}
	

	/*-------------------------------------------------------
	 * 					FIN	METODOS
	 --------------------------------------------------------*/
	


	/*-------------------------------------------------------
	 * 					METODO VALIDATE
	 --------------------------------------------------------*/
	 @Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		 
		 logger.info("\n\nESTDO EN EL VAIDATE-->"+this.getEstado());
		 
			ActionErrors errores = new ActionErrors();
			errores=super.validate(mapping, request);
			
			if (estado.equals("busquedaAvanzada"))
			{
				if (!UtilidadCadena.noEsVacio(this.getListadoSolicitudes(indicesListado[1])+"") && !UtilidadCadena.noEsVacio(this.getListadoSolicitudes(indicesListado[2])+""))
					errores.add("descripcion",new ActionMessage("errors.required","La fecha Inicial y Final "));
				else
					if (!UtilidadCadena.noEsVacio(this.getListadoSolicitudes(indicesListado[1])+""))
						errores.add("descripcion",new ActionMessage("errors.required","La fecha Inicial"));
					else
						if (!UtilidadCadena.noEsVacio(this.getListadoSolicitudes(indicesListado[2])+""))
							errores.add("descripcion",new ActionMessage("errors.required","La fecha Final"));
						else
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),this.getListadoSolicitudes(indicesListado[1])+""))
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getListadoSolicitudes(indicesListado[1])+"","Actual "+UtilidadFecha.getFechaActual()));
							else
								if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getListadoSolicitudes(indicesListado[1])+"",this.getFechaApertura()))
									errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Inicial "+this.getListadoSolicitudes(indicesListado[1])+"","de Apertura "+this.getFechaApertura()));
								else			
									if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getListadoSolicitudes(indicesListado[2])+"",this.getListadoSolicitudes(indicesListado[1])+""))
										errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Final "+this.getListadoSolicitudes(indicesListado[2])+"","Inicial "+this.getListadoSolicitudes(indicesListado[1])));
									else
										if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),this.getListadoSolicitudes(indicesListado[2])+""))
											errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.getListadoSolicitudes(indicesListado[2])+"","Actual "+UtilidadFecha.getFechaActual()));
			
							
				
			}
			
			
			
			
		return errores;
	}
	
	
	/*-------------------------------------------------------
	 * 					FIN METODO VALIDATE
	 --------------------------------------------------------*/


	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	 
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
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


	public HashMap getListadoSolicitudes() {
		return listadoSolicitudes;
	}


	public void setListadoSolicitudes(HashMap listadoSolicitudes) {
		this.listadoSolicitudes = listadoSolicitudes;
	}	
	

	public Object getListadoSolicitudes(String key) {
		return listadoSolicitudes.get(key);
	}


	public void setListadoSolicitudes(String key,Object value) {
		this.listadoSolicitudes.put(key, value);
	}


	public ArrayList<HashMap<String, Object>> getCentCosto() {
		return centCosto;
	}


	public void setCentCosto(ArrayList<HashMap<String, Object>> centCosto) {
		this.centCosto = centCosto;
	}


	public String getFechaApertura() {
		return fechaApertura;
	}


	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}


	public HashMap getListadoIngresosReceptores() {
		return listadoIngresosReceptores;
	}


	public void setListadoIngresosReceptores(HashMap listadoIngresosReceptores) {
		this.listadoIngresosReceptores = listadoIngresosReceptores;
	}


	public Object getListadoIngresosReceptores( String key) {
		return listadoIngresosReceptores.get(key);
	}


	public void setListadoIngresosReceptores(String key, Object value) {
		this.listadoIngresosReceptores.put(key, value);
	}




	public boolean isOperacionTrue() {
		return operacionTrue;
	}


	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/


}