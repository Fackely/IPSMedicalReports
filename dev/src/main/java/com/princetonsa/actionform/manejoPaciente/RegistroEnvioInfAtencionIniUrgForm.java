package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;

public class RegistroEnvioInfAtencionIniUrgForm extends ValidatorForm
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
	
	/**
	 * Almacena el listado de Convenios para generar el informe
	 * */
	private ArrayList<DtoInformeAtencionIniUrg> listadoConvenios;
	
	/**
	 * Almacena la informacion de los Medios de Envio
	 * */
	private ArrayList<HashMap<String, Object>> listadoArrayMediosEnvio;
	
	/**
	 * Almacena la informacion general
	 * */
	private ArrayList<HashMap<String, Object>> arrayUtilitario;
	
	/**
	 * Almacena los parametros para filtro desde un llamado a esta funcionalidad
	 * */
	private HashMap parametrosExternos = new HashMap();
	
	/**
	 * Dto que almacena la informacion del informe de un pacienteIngresp
	 * */
	private DtoInformeAtencionIniUrg dtoInformeAtencionXpacienteIngreso;
	
	//************************************************
	//************************************************
	
	public void reset()
	{
		this.parametrosBusqueda = new HashMap();
		this.listadoArray = new ArrayList<DtoInformeAtencionIniUrg>();
		this.listadoArrayConvenio = new ArrayList<HashMap<String,Object>>();
		this.dtoInformeAtencionXpacienteIngreso = new DtoInformeAtencionIniUrg();
		this.arrayUtilitario = new ArrayList<HashMap<String,Object>>();
		this.listadoArrayMediosEnvio = new ArrayList<HashMap<String,Object>>();
		this.listadoConvenios = new ArrayList<DtoInformeAtencionIniUrg>();
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

	public HashMap getParametrosExternos() {
		return parametrosExternos;
	}

	public void setParametrosExternos(HashMap parametrosExternos) {
		this.parametrosExternos = parametrosExternos;
	}
	
	public Object getParametrosExternos(String key) {
		return parametrosExternos.get(key);
	}

	public void setParametrosExternos(String key, Object value) {
		this.parametrosExternos.put(key,value);
	}
	
	public String getCuentaFiltro() {
		return this.parametrosExternos.get("cuentaFiltro").toString();
	}

	public void setCuentaFiltro(String valor) {
		this.parametrosExternos.put("cuentaFiltro",valor);
	}
	
	public String getCodigoPkInforme() {
		return this.parametrosExternos.get("codigoPkInforme").toString();
	}

	public void setCodigoPkInforme(String valor) {
		this.parametrosExternos.put("codigoPkInforme",valor);
	}
	
	public String getIngresoFiltro() {
		return this.parametrosExternos.get("ingresoFiltro").toString();
	}

	public void setIngresoFiltro(String valor) {
		this.parametrosExternos.put("ingresoFiltro",valor);
	}
	
	public String getConvenioFiltro() {
		return this.parametrosExternos.get("convenioFiltro").toString();
	}

	public void setConvenioFiltro(String valor) {
		this.parametrosExternos.put("convenioFiltro",valor);
	}
	
	public String getConvenioEnvioFiltro() {
		return this.parametrosExternos.get("convenioEnvioFiltro").toString();
	}

	public void setConvenioEnvioFiltro(String valor) {
		this.parametrosExternos.put("convenioEnvioFiltro",valor);
	}
	
	public String getFechaInicialEnvio() {
		return this.parametrosExternos.get("fechaInicialEnvio").toString();
	}

	public void setFechaInicialEnvio(String valor) {
		this.parametrosExternos.put("fechaInicialEnvio",valor);
	}
	
	public String getFechaFinalEnvio() {
		return this.parametrosExternos.get("fechaFinalEnvio").toString();
	}

	public void setFechaFinalEnvio(String valor) {
		this.parametrosExternos.put("fechaFinalEnvio",valor);
	}
	
	public String getPosicionInfor() {
		return this.parametrosExternos.get("posicionInfor").toString();
	}

	public void setPosicionInfor(String valor) {
		this.parametrosExternos.put("posicionInfor",valor);
	}

	public DtoInformeAtencionIniUrg getDtoInformeAtencionXpacienteIngreso() {
		return dtoInformeAtencionXpacienteIngreso;
	}

	public void setDtoInformeAtencionXpacienteIngreso(
			DtoInformeAtencionIniUrg dtoInformeAtencionXpacienteIngreso) {
		this.dtoInformeAtencionXpacienteIngreso = dtoInformeAtencionXpacienteIngreso;
	}

	public ArrayList<HashMap<String, Object>> getArrayUtilitario() {
		return arrayUtilitario;
	}

	public void setArrayUtilitario(
			ArrayList<HashMap<String, Object>> arrayUtilitario) {
		this.arrayUtilitario = arrayUtilitario;
	}

	public ArrayList<HashMap<String, Object>> getListadoArrayMediosEnvio() {
		return listadoArrayMediosEnvio;
	}

	public void setListadoArrayMediosEnvio(
			ArrayList<HashMap<String, Object>> listadoArrayMediosEnvio) {
		this.listadoArrayMediosEnvio = listadoArrayMediosEnvio;
	}

	public ArrayList<DtoInformeAtencionIniUrg> getListadoConvenios() {
		return listadoConvenios;
	}

	public void setListadoConvenios(
			ArrayList<DtoInformeAtencionIniUrg> listadoConvenios) {
		this.listadoConvenios = listadoConvenios;
	}	
}