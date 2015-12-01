package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;




/**
 *@author Luis Gabriel Chavez Salazar
 *lgchavez@princetonsa.com 
 */
public class AsignacionCamaCuidadoEspecialForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase AsignacionCamaCuidadoEspecialForm
	 */
	Logger logger = Logger.getLogger(AsignacionCamaCuidadoEspecialForm.class);
	
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
	

	private String estado="";
	
	
	private String index="";
	
	
	private ArrayList<HashMap<String, Object>> tipoMonitoreo = new ArrayList<HashMap<String,Object>> ();
	
	private String monitoreo;
	
	private HashMap listadoPacientes = new HashMap ();
	
	private HashMap detalleIngresar= new HashMap();
	
	private int newArea;
	
	private boolean operacionTrue=false;
	
	private HashMap mapaCamaNuevaPaciente = new HashMap();
	
	/**
	 * se almacenan los centros de costo
	 */
	private ArrayList<HashMap<String, Object>> CCosto = new ArrayList<HashMap<String,Object>>();

	
	public void reset ()
	{
		this.newArea=ConstantesBD.codigoNuncaValido;
		this.monitoreo="";
		this.index="";
		tipoMonitoreo = new ArrayList<HashMap<String,Object>> ();
		this.listadoPacientes = new HashMap ();
		this.setListadoPacientes("numRegistros", 0);
		this.detalleIngresar=new HashMap();
		this.setDetalleIngresar("numRegistros", 0);
		this.CCosto = new ArrayList<HashMap<String,Object>>();
		this.mapaCamaNuevaPaciente= new HashMap ();
		
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (this.estado.equals("guardar"))
		{
		
			if (!this.detalleIngresar.containsKey("fecha") || this.detalleIngresar.get("fecha").toString().equals(""))
			{
				errores.add("La Fecha es Requerido ", new ActionMessage("errors.required"," Fecha "));
			}
			if (!this.detalleIngresar.containsKey("hora") || this.detalleIngresar.get("hora").toString().equals(""))
			{
				errores.add("La Hora es Requerido ", new ActionMessage("errors.required"," Hora "));
			}
			else
			{
				Utilidades.imprimirMapa(this.detalleIngresar);
				
				String orden=this.detalleIngresar.get("fechaHoraOrden")+"";
				String [] fechao=orden.split(" ");
				
				logger.info("\n\n [Fecha/Hora Orden] "+this.detalleIngresar.get("fechaHoraOrden"));
				logger.info("\n\n [Fecha Orden]	("+fechao[0].trim()+")");
				logger.info("\n\n [Hora Orden]	("+fechao[1].trim()+")");
				
				if(!UtilidadFecha.validacionHora(this.detalleIngresar.get("hora").toString()).puedoSeguir)
        			errores.add("Motivo", new ActionMessage("errors.formatoHoraInvalido", "Hora "));
				else
					if(!UtilidadFecha.validarFecha(this.detalleIngresar.get("fecha")+""))
						errores.add("Motivo", new ActionMessage("errors.formatoFechaInvalido", "Fecha "));
					else
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.detalleIngresar.get("fecha").toString()),UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
							errores.add("Fecha mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","Asignacion cama cuidado especial","Actual"));
						else
							if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechao[0].trim()), UtilidadFecha.conversionFormatoFechaAAp(this.detalleIngresar.get("fecha").toString())))
								errores.add("Fecha menor que o igual a la fecha de la orden de traslado", new ActionMessage("errors.fechaPosteriorIgualActual","Orden de traslado del paciente","Asignacion cama cuidado especial"));
							else
								if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechao[0].trim()), UtilidadFecha.conversionFormatoFechaAAp(this.detalleIngresar.get("fecha").toString())))
									//Validacion de hora proque es el mismo dia
										if(!UtilidadFecha.esHoraMenorQueOtraReferencia(fechao[1].trim(),this.detalleIngresar.get("hora").toString().trim()))
											errores.add("Fecha/Hora menor o igual a la Fecha/Hora de la orden de traslado", new ActionMessage("errors.fechaHoraAnteriorIgualActual","Asignacion cama cuidado especial","Orden de traslado del paciente"));
										
	    		
			}			
			
			
			
			
			
		}
		
		return errores;
	}

	
	

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

	public HashMap getMapaCamaNuevaPaciente() {
		return mapaCamaNuevaPaciente;
	}

	public void setMapaCamaNuevaPaciente(HashMap mapaCamaNuevaPaciente) {
		this.mapaCamaNuevaPaciente = mapaCamaNuevaPaciente;
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

	/**
	 * @return the tipoMonitoreo
	 */
	public ArrayList<HashMap<String, Object>> getTipoMonitoreo() {
		return tipoMonitoreo;
	}
	/**
	 * @param tipoMonitoreo the tipoMonitoreo to set
	 */
	public void setTipoMonitoreo(ArrayList<HashMap<String, Object>> tipoMonitoreo) {
		this.tipoMonitoreo = tipoMonitoreo;
	}

	/**
	 * @return the monitoreo
	 */
	public String getMonitoreo() {
		return monitoreo;
	}

	/**
	 * @param monitoreo the monitoreo to set
	 */
	public void setMonitoreo(String monitoreo) {
		this.monitoreo = monitoreo;
	}	

	public HashMap getListadoPacientes() {
		return listadoPacientes;
	}

	public void setListadoPacientes(HashMap listadoPacientes) {
		this.listadoPacientes = listadoPacientes;
	}	

	public Object getListadoPacientes(String key) {
		return listadoPacientes.get(key);
	}

	public void setListadoPacientes(String key, Object value) {
		this.listadoPacientes.put(key, value);
	}

	public Object getDetalleIngresar(String key) {
		return detalleIngresar.get(key);
	}

	public void setDetalleIngresar(String key, Object value) {
		this.detalleIngresar.put(key, value);
	}
	
	/**
	 * @return the detalleIngresar
	 */
	public HashMap getDetalleIngresar() {
		return detalleIngresar;
	}

	/**
	 * @param detalleIngresar the detalleIngresar to set
	 */
	public void setDetalleIngresar(HashMap detalleIngresar) {
		this.detalleIngresar = detalleIngresar;
	}

	/**
	 * @return the cCosto
	 */
	public ArrayList<HashMap<String, Object>> getCCosto() {
		return CCosto;
	}

	/**
	 * @param costo the cCosto to set
	 */
	public void setCCosto(ArrayList<HashMap<String, Object>> costo) {
		CCosto = costo;
	}

	/**
	 * @return the newArea
	 */
	public int getNewArea() {
		return newArea;
	}

	/**
	 * @param newArea the newArea to set
	 */
	public void setNewArea(int newArea) {
		this.newArea = newArea;
	}

	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}	

}