package com.princetonsa.actionform.manejoPaciente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.historiaClinica.UtilidadesHistoriaClinica;


/**
 * Clase para el manejo de los reportes
 * de calidad en atención
 * Date: 2008-08-11
 * @author garias@princetonsa.com
 */
public class CalidadAtencionForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * llaves con los filtros del reporte
	 * ----------------------------------
	 * 	- tipoReporte
	 * 	- centroAtencion
	 * 	- fechaInicial
	 * 	- fechaFinal
	 * 	- centroCosto
	 * 	- tipoMotivo
	 * 	- numViaIngresoTipoPaciente
	 * 	- viaIngresoTipoPaciente_[pos]
	 */
	private HashMap filtros;
	
	/**
	 * Centros de Atención
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	
	/**
	 * Centros de Costo
	 */
	private ArrayList<HashMap<String,Object>> centrosCosto;
	
	/**
	 * Vias de ingreso y tipo de paciente
	 */
	private ArrayList<HashMap<String,Object>> viaIngresoTipoPaciente;
	
	/**
	 *
	 */
	public void reset()
	{
		this.filtros = new HashMap();
		this.filtros.put("tipoReporte", "");
		this.centrosAtencion=new ArrayList();
		this.centrosCosto=new ArrayList();
		this.viaIngresoTipoPaciente=new ArrayList();
	}
	
	/**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no recorded
     * error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
    */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        errores = super.validate(mapping,request);
       
        if (this.estado.equals("generarReporte")){
        	
        	// Validacion ingreso fecha inicial
        	if(filtros.get("fechaInicial").equals(""))
				errores.add("fechaInicial", new ActionMessage("errors.required", "Fecha Inicial"));
				
	        // Validacion ingreso fecha final
			if(filtros.get("fechaFinal").equals(""))
				errores.add("fechaFinal", new ActionMessage("errors.required", "Fecha Final"));
			
			if(errores.isEmpty()){
				// Validacion formato fecha inicial
	        	if(!UtilidadFecha.validarFecha(filtros.get("fechaInicial").toString()))
	        		errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido", "Inicial"));
					
		        // Validacion formato fecha final
				if(!UtilidadFecha.validarFecha(filtros.get("fechaFinal").toString()))
					errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido", "Final"));
			}
			
			if(errores.isEmpty()){
				// Validacion Fecha final menor igual a la actual
		        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(filtros.get("fechaFinal").toString(), UtilidadFecha.getFechaActual())){
		        	errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual"));
		        }
		        
		        // Validacion fecha inicial menor o igual a la fecha final
		        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(filtros.get("fechaInicial").toString(), filtros.get("fechaFinal").toString())){
		        	errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Final"));
		        }
	        }
			
			if(errores.isEmpty()){
				// Validacion rango de fechas
				if(UtilidadFecha.numeroMesesEntreFechas(filtros.get("fechaInicial").toString(), filtros.get("fechaFinal").toString(),true)>12)
					errores.add("Rango de fechas", new ActionMessage("errors.rangoDeFechasSuperior"));
			}
        }
        return errores; 
    }

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the filtros
	 */
	public HashMap getFiltros() {
		return filtros;
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(HashMap filtros) {
		this.filtros = filtros;
	}
    
	/**
	 * @return the filtros
	 */
	public Object getFiltros(String llave) {
		return filtros.get(llave);
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(String llave, Object obj) {
		this.filtros.put(llave, obj);
	}

	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the centrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the viaIngresoTipoPaciente
	 */
	public ArrayList<HashMap<String, Object>> getViaIngresoTipoPaciente() {
		return viaIngresoTipoPaciente;
	}

	/**
	 * @param viaIngresoTipoPaciente the viaIngresoTipoPaciente to set
	 */
	public void setViaIngresoTipoPaciente(
			ArrayList<HashMap<String, Object>> viaIngresoTipoPaciente) {
		this.viaIngresoTipoPaciente = viaIngresoTipoPaciente;
	}

	
	
	
}	