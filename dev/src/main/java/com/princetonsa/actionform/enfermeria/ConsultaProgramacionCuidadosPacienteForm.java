package com.princetonsa.actionform.enfermeria;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public class ConsultaProgramacionCuidadosPacienteForm extends ValidatorForm {

	private String estado;
    private int indicePaciente;
	private int indiceCuidado;
	private HashMap mapaListaPacientes;
	private ArrayList<HashMap<String,Object>> arrayTipoFrecuencias;	
	private ArrayList<DtoCuidadosEnfermeria> programacionCuidados;

	/**
	 * Constructor por defecto de ConsultaProgramacionCuidadosArea
	 */
	public ConsultaProgramacionCuidadosPacienteForm(){
		this.estado=new String("");
	    this.estado=new String("");
		this.indicePaciente=ConstantesBD.codigoNuncaValido;
		this.mapaListaPacientes=new HashMap();
		this.mapaListaPacientes.put("numRegistros", "0");		
		this.arrayTipoFrecuencias = new ArrayList<HashMap<String,Object>>();
		this.programacionCuidados = new ArrayList<DtoCuidadosEnfermeria>();
		
	}
	
	/**
	 * Metodo para reiniciar los valores de los atributos
	 * 
	 */
	public void reset(){
		this.estado=new String("");
	    this.estado=new String("");
		this.indicePaciente=ConstantesBD.codigoNuncaValido;
		this.mapaListaPacientes=new HashMap();
		this.mapaListaPacientes.put("numRegistros", "0");		
		this.arrayTipoFrecuencias = new ArrayList<HashMap<String,Object>>();
		this.programacionCuidados = new ArrayList<DtoCuidadosEnfermeria>();
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
	 * @return the indicePaciente
	 */
	public int getIndicePaciente() {
		return indicePaciente;
	}

	/**
	 * @param indicePaciente the indicePaciente to set
	 */
	public void setIndicePaciente(int indicePaciente) {
		this.indicePaciente = indicePaciente;
	}

	/**
	 * @return the indiceCuidado
	 */
	public int getIndiceCuidado() {
		return indiceCuidado;
	}

	/**
	 * @param indiceCuidado the indiceCuidado to set
	 */
	public void setIndiceCuidado(int indiceCuidado) {
		this.indiceCuidado = indiceCuidado;
	}



	/**
	 * @return the programacionCuidados
	 */
	public ArrayList<DtoCuidadosEnfermeria> getProgramacionCuidados() {
		return programacionCuidados;
	}

	/**
	 * @param programacionCuidados the programacionCuidados to set
	 */
	public void setProgramacionCuidados(
			ArrayList<DtoCuidadosEnfermeria> programacionCuidados) {
		this.programacionCuidados = programacionCuidados;
	}

	/**
	 * @return the mapaListaPacientes
	 */
	public HashMap getMapaListaPacientes() {
		return mapaListaPacientes;
	}

	/**
	 * @param mapaListaPacientes the mapaListaPacientes to set
	 */
	public void setMapaListaPacientes(HashMap mapaListaPacientes) {
		this.mapaListaPacientes = mapaListaPacientes;
	}

	/**
	 * @return the arrayTipoFrecuencias
	 */
	public ArrayList<HashMap<String, Object>> getArrayTipoFrecuencias() {
		return arrayTipoFrecuencias;
	}

	/**
	 * @param arrayTipoFrecuencias the arrayTipoFrecuencias to set
	 */
	public void setArrayTipoFrecuencias(
			ArrayList<HashMap<String, Object>> arrayTipoFrecuencias) {
		this.arrayTipoFrecuencias = arrayTipoFrecuencias;
	}

	
	
}
