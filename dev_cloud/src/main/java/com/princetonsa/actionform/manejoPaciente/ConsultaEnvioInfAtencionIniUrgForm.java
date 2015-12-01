package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;

public class ConsultaEnvioInfAtencionIniUrgForm extends ValidatorForm
{
	/**
	 * Maneja el estado del action
	 * */
	private String estado;
	
	/**
	 * Almancena la información de los parametros de busqueda
	 * */
	private HashMap parametrosBusqueda;
	
	/**
	 * Almacena el listado inicial por rango y por paciente
	 * */
	private ArrayList<DtoInformeAtencionIniUrg> listadoArray;
	
	/**
	 * Almacena la informacion de los convenios
	 * */
	private ArrayList<HashMap<String, Object>> listadoArrayConvenio;	
	
	//************************************************
	//************************************************
	
	public void reset()
	{
		this.parametrosBusqueda = new HashMap();
		this.listadoArray = new ArrayList<DtoInformeAtencionIniUrg>();
		this.listadoArrayConvenio = new ArrayList<HashMap<String,Object>>();		
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getParametrosBusqueda() {
		return parametrosBusqueda;
	}

	public void setParametrosBusqueda(HashMap parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
	}
	
	public Object getParametrosBusqueda(String key) {
		return parametrosBusqueda.get(key);
	}

	public void setParametrosBusqueda(String key, Object value) {
		this.parametrosBusqueda.put(key,value);
	}

	public ArrayList<DtoInformeAtencionIniUrg> getListadoArray() {
		return listadoArray;
	}

	public void setListadoArray(ArrayList<DtoInformeAtencionIniUrg> listadoArray) {
		this.listadoArray = listadoArray;
	}

	public ArrayList<HashMap<String, Object>> getListadoArrayConvenio() {
		return listadoArrayConvenio;
	}

	public void setListadoArrayConvenio(
			ArrayList<HashMap<String, Object>> listadoArrayConvenio) {
		this.listadoArrayConvenio = listadoArrayConvenio;
	}	
}