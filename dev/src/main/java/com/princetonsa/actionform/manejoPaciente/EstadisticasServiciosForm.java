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
import util.manejoPaciente.ConstantesBDManejoPaciente;


/**
 * Clase para el manejo de los reportes
 * de calidad en atención
 * Date: 2008-08-11
 * @author garias@princetonsa.com
 */
public class EstadisticasServiciosForm extends ValidatorForm 
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
	 * 	- numViaIngresoTipoPaciente
	 * 	- viaIngresoTipoPaciente_[pos]
	 * 	- sexo
	 * 	- especialidad
	 * 	- grupo
	 * 	- convenio
	 * 	- tipoServicio
	 */
	private HashMap filtros;
	
	/**
	 * Centros de Atención
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	
	/**
	 * Vias de ingreso y tipo de paciente
	 */
	private ArrayList<HashMap<String,Object>> viaIngresoTipoPaciente;
	
	/**
	 * Sexos
	 */
	private ArrayList<HashMap<String,Object>> sexos;
	
	/**
	 * Especialidades
	 */
	private ArrayList<HashMap<String,Object>> especialidades;
	
	/**
	 * Grupos de servicio
	 */
	private ArrayList<HashMap<String,Object>> grupos;
	
	/**
	 * Convenios
	 */
	private ArrayList<HashMap<String,Object>> convenios;
	
	/**
	 * Tipos Servicio
	 */
	private ArrayList<HashMap<String,Object>> tiposServicio;
	
	/**
	 * Mapa de los convenios seleccionados
	 */
	private HashMap conveniosMap;
	
	/**
	 * Mapa de las especialidades seleccionadas
	 */
	private HashMap especialidadesMap;
	
	/**
	 * Grupos de Servicios
	 */
	private HashMap gruposServiciosMap;
	
	/**
	 * Posicion del mapa
	 */
	int posMap;
	
	/**
	 *
	 */
	public void reset()
	{
		this.filtros = new HashMap();
		this.filtros.put("tipoReporte", "");
		this.centrosAtencion=new ArrayList();
		this.sexos=new ArrayList();
		this.viaIngresoTipoPaciente=new ArrayList();
		this.especialidades=new ArrayList();
		this.grupos=new ArrayList();
		this.convenios=new ArrayList();
		this.conveniosMap=new HashMap();
		this.conveniosMap.put("numRegistros", 0);
		this.especialidadesMap=new HashMap();
		this.especialidadesMap.put("numRegistros", 0);
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.gruposServiciosMap=new HashMap();
		this.gruposServiciosMap.put("numRegistros", 0);
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
			
			// Validacion tipo de servicio
        	if(filtros.get("tipoServicio").equals(""))
        		errores.add("tipoServicio", new ActionMessage("errors.required", "Tipo de servicio"));
			
			// Validación Via de Ingreso y tipo de paciente para el reporte tres
			if (filtros.get("tipoReporte").equals("3"))
				if(Integer.parseInt(filtros.get("numViaIngresoTipoPaciente").toString())<1)
					errores.add("viaIngresoTipoPaciente", new ActionMessage("errors.required", "Via de Ingreso / Tipo de Paciente"));
			if (filtros.get("tipoReporte").equals(""+ConstantesBDManejoPaciente.tipoReporteServiciosRealizadosXEspecialidad)){
				int contadorSeleccionados=0;
				for(int i=0;i<this.viaIngresoTipoPaciente.size();i++){
					HashMap<String, Object>valores=this.viaIngresoTipoPaciente.get(i);
					if(filtros.get("viaIngresoTipoPaciente_"+i).equals("S")){
						contadorSeleccionados++;
					}
				}
				if(contadorSeleccionados==0){
					errores.add("viaIngresoTipoPaciente", new ActionMessage("errors.required", "Via de Ingreso / Tipo de Paciente"));
				}
			}
					
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
	 * @return the especialidades
	 */
	public ArrayList<HashMap<String, Object>> getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<HashMap<String, Object>> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * @return the grupos
	 */
	public ArrayList<HashMap<String, Object>> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos the grupos to set
	 */
	public void setGrupos(ArrayList<HashMap<String, Object>> grupos) {
		this.grupos = grupos;
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
	 * @return the tiposServicio
	 */
	public ArrayList<HashMap<String, Object>> getTiposServicio() {
		return tiposServicio;
	}

	/**
	 * @param tiposServicio the tiposServicio to set
	 */
	public void setTiposServicio(ArrayList<HashMap<String, Object>> tiposServicio) {
		this.tiposServicio = tiposServicio;
	}

	/**
	 * @return the especialidesMap
	 */
	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}

	/**
	 * @param especialidesMap the especialidesMap to set
	 */
	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}
	
	/**
	 * @return the especialidesMap
	 */
	public Object getEspecialidadesMap(String llave) {
		return especialidadesMap.get(llave);
	}

	/**
	 * @param especialidesMap the especialidesMap to set
	 */
	public void setEspecialidadesMap(String llave, Object obj) {
		this.especialidadesMap.put(llave, obj);
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
	 * @return the gruposServiciosMap
	 */
	public HashMap getGruposServiciosMap() {
		return gruposServiciosMap;
	}

	/**
	 * @param gruposServiciosMap the gruposServiciosMap to set
	 */
	public void setGruposServiciosMap(HashMap gruposServiciosMap) {
		this.gruposServiciosMap = gruposServiciosMap;
	}
	
	/**
	 * @return the gruposServiciosMap
	 */
	public Object getGruposServiciosMap(String llave) {
		return gruposServiciosMap.get(llave);
	}

	/**
	 * @param gruposServiciosMap the gruposServiciosMap to set
	 */
	public void setGruposServiciosMap(String llave, Object obj) {
		this.gruposServiciosMap.put(llave, obj);
	}
}	