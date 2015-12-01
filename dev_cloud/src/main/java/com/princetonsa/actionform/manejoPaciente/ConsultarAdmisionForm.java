package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ConsultarAdmisionForm extends ValidatorForm {
	
	/**
	 * Variable para manejar los posibles estados dentro de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa que almacena los datos de la entidad responsable por paciente
	 */
	private HashMap<String, Object> entidadResponsbaleMap;
	
	/**
	 * Mapa que almacena la informacion de la cuenta del paciente 
	 */
	private HashMap<String, Object> cuentaPacienteMap;
	
	/**
	 * Mapa que maneja la informacion del paciente
	 */
	private HashMap<String, Object> pacienteMap;
	
	/**
	 * Mapa que almacena la informacion de responsable paciente
	 */
	private HashMap<String, Object> responsablePacienteMap;
	
	/**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
	
	/**
	 * Variable para mostrar los mensajes
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Indice seleccionado del ingreso.
	 */
	private int indiceIngresoSeleccionado;
	
	/**
	 * Variable que almacena el codigo de cuenta del paciente
	 */
	private int cuenta;
	
	/**
	 * Variable que almacena la fecha de ingreso de la admision
	 */
	private String fechaIngreso;
	
	/**
	 * Variable que almacena la hora de ingreso de la admision
	 */
	private String horaIngreso;
	
	public void reset()
	{
		this.cuenta=ConstantesBD.codigoNuncaValido;
		this.fechaIngreso="";
		this.horaIngreso="";
		
		this.cuentaPacienteMap=new HashMap<String, Object>();
		this.cuentaPacienteMap.put("numRegistros", "0");
		
		this.entidadResponsbaleMap=new HashMap<String, Object>();
		this.entidadResponsbaleMap.put("numRegistros", "0");
		
		this.pacienteMap=new HashMap<String, Object>();
		this.pacienteMap.put("numRegistros", "0");
		
		this.responsablePacienteMap=new HashMap<String, Object>();
		this.responsablePacienteMap.put("numRegistros", "0");
	}

	public HashMap<String, Object> getCuentaPacienteMap() {
		return cuentaPacienteMap;
	}

	public void setCuentaPacienteMap(HashMap<String, Object> cuentaPacienteMap) {
		this.cuentaPacienteMap = cuentaPacienteMap;
	}

	public HashMap<String, Object> getEntidadResponsbaleMap() {
		return entidadResponsbaleMap;
	}

	public void setEntidadResponsbaleMap(
			HashMap<String, Object> entidadResponsbaleMap) {
		this.entidadResponsbaleMap = entidadResponsbaleMap;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIndiceIngresoSeleccionado() {
		return indiceIngresoSeleccionado;
	}

	public void setIndiceIngresoSeleccionado(int indiceIngresoSeleccionado) {
		this.indiceIngresoSeleccionado = indiceIngresoSeleccionado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public HashMap<String, Object> getPacienteMap() {
		return pacienteMap;
	}

	public void setPacienteMap(HashMap<String, Object> pacienteMap) {
		this.pacienteMap = pacienteMap;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public HashMap<String, Object> getResponsablePacienteMap() {
		return responsablePacienteMap;
	}

	public void setResponsablePacienteMap(
			HashMap<String, Object> responsablePacienteMap) {
		this.responsablePacienteMap = responsablePacienteMap;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public int getCuenta() {
		return cuenta;
	}

	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getHoraIngreso() {
		return horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

}
