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
public class EstadisticasIngresosForm extends ValidatorForm 
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
	 * 	- viasIngreso
	 * 	- tipoPaciente
	 * 	- sexo
	 * 	- convenio
	 */
	private HashMap filtros;
	
	/**
	 * Centros de Atención
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	
	/**
	 * Vias de ingreso
	 */
	private ArrayList<HashMap<String,Object>> viasIngreso;
	
	/**
	 * Tipos Paciente
	 */
	private ArrayList<HashMap<String,Object>> tiposPaciente;
	
	/**
	 * Sexos
	 */
	private ArrayList<HashMap<String,Object>> sexos;
	
	/**
	 * Convenios
	 */
	private ArrayList<HashMap<String,Object>> convenios;
	
	/**
	 * Convenios
	 */
	private ArrayList<HashMap<String,Object>> empresas;
	
	/**
	 * Numero de diagnosticos de egreso
	 */
	private int numDiagEgreso;
	
	/**
	 * Diagnosticos Egreso
	 */
	private HashMap diagnosticosEgreso;
	
	/**
	 * Diagnosticos seleccionados
	 */
	private String diagSeleccionados;
	
	/**
	 * Convenios seleccionados
	 */
	private HashMap conveniosMap;
	
	/**
	 * Empresas seleccionadas
	 */
	private HashMap empresasMap;
	
	/**
	 * referencia de mapa
	 */
	private int posMap;
	
	/**
	 *
	 */
	public void reset()
	{
		this.filtros = new HashMap();
		this.diagnosticosEgreso = new HashMap();
		this.filtros.put("tipoReporte", "");
		this.centrosAtencion=new ArrayList();
		this.sexos=new ArrayList();
		this.viasIngreso=new ArrayList();
		this.tiposPaciente=new ArrayList();
		this.convenios=new ArrayList();
		this.empresas=new ArrayList();
		this.numDiagEgreso=0;
		this.diagSeleccionados="";
		this.conveniosMap=new HashMap();
		this.conveniosMap.put("numRegistros", 0);
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.empresasMap=new HashMap();
		this.empresasMap.put("numRegistros", 0);
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
			
			// Validación Via de Ingreso y tipo de paciente para el reporte tres
			if (filtros.get("viaIngreso").equals(""))
				errores.add("viaIngreso", new ActionMessage("errors.required", "Via de Ingreso"));
			
			
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
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}

	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the viasIngreso
	 */
	public ArrayList<HashMap<String, Object>> getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(ArrayList<HashMap<String, Object>> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	/**
	 * @return the tiposPaciente
	 */
	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	/**
	 * @return the empresas
	 */
	public ArrayList<HashMap<String, Object>> getEmpresas() {
		return empresas;
	}

	/**
	 * @param empresas the empresas to set
	 */
	public void setEmpresas(ArrayList<HashMap<String, Object>> empresas) {
		this.empresas = empresas;
	}

	/**
	 * @return the numDiagEgreso
	 */
	public int getNumDiagEgreso() {
		return numDiagEgreso;
	}

	/**
	 * @param numDiagEgreso the numDiagEgreso to set
	 */
	public void setNumDiagEgreso(int numDiagEgreso) {
		this.numDiagEgreso = numDiagEgreso;
	}

	/**
	 * @return the diagnosticosEgreso
	 */
	public HashMap getDiagnosticosEgreso() {
		return diagnosticosEgreso;
	}

	/**
	 * @param diagnosticosEgreso the diagnosticosEgreso to set
	 */
	public void setDiagnosticosEgreso(HashMap diagnosticosEgreso) {
		this.diagnosticosEgreso = diagnosticosEgreso;
	}
	
	/**
	 * @return the diagnosticosEgreso
	 */
	public Object getDiagnosticosEgreso(String llave) {
		return diagnosticosEgreso.get(llave);
	}

	/**
	 * @param diagnosticosEgreso the diagnosticosEgreso to set
	 */
	public void setDiagnosticosEgreso(String llave, Object obj) {
		this.diagnosticosEgreso.put(llave, obj);
	}

	/**
	 * @return the diagSeleccionados
	 */
	public String getDiagSeleccionados() {
		return diagSeleccionados;
	}

	/**
	 * @param diagSeleccionados the diagSeleccionados to set
	 */
	public void setDiagSeleccionados(String diagSeleccionados) {
		this.diagSeleccionados = diagSeleccionados;
	}

	/**
	 * @return the conveniosMap
	 */
	public HashMap getConveniosMap() {
		return conveniosMap;
	}

	/**
	 * @param conveniosMap the conveniosMap to set
	 */
	public void setConveniosMap(HashMap conveniosMap) {
		this.conveniosMap = conveniosMap;
	}
	
	/**
	 * @return the conveniosMap
	 */
	public Object getConveniosMap(String llave) {
		return conveniosMap.get(llave);
	}

	/**
	 * @param conveniosMap the conveniosMap to set
	 */
	public void setConveniosMap(String llave, Object obj) {
		this.conveniosMap.put(llave, obj);
	}

	/**
	 * @return the posMap
	 */
	public int getPosMap() {
		return posMap;
	}

	/**
	 * @param posMap the posMap to set
	 */
	public void setPosMap(int posMap) {
		this.posMap = posMap;
	}

	/**
	 * @return the empresasMap
	 */
	public HashMap getEmpresasMap() {
		return empresasMap;
	}

	/**
	 * @param empresasMap the empresasMap to set
	 */
	public void setEmpresasMap(HashMap empresasMap) {
		this.empresasMap = empresasMap;
	}
	
	/**
	 * @return the empresasMap
	 */
	public Object getEmpresasMap(String llave) {
		return empresasMap.get(llave);
	}

	/**
	 * @param empresasMap the empresasMap to set
	 */
	public void setEmpresasMap(String llave, Object obj) {
		this.empresasMap.put(llave, obj);
	}
	
}	