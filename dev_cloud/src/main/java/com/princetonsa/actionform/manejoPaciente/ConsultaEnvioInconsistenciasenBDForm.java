package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;

import util.ConstantesBD;

public class ConsultaEnvioInconsistenciasenBDForm extends ValidatorForm {

	/**
	 * Maneja el estado del action
	 * */
	private String estado;
	
	/**
	 *  Mapa que contiene los ingresos asociados a un paciente
	 */
    private HashMap ingresosPaciente;
    
    /**
     *  Dto que contiene los datos del Informe de Iconsistencias
     */
    private DtoInformeInconsisenBD informeIconsistencias = new DtoInformeInconsisenBD();
    
    /**
	 * Almacena los parametros para filtro desde un llamado a esta funcionalidad
	 * */
	private HashMap parametrosFiltros = new HashMap();
    
    /**
	 * Contiene la posicion  del ingreso dentro del listado de Ingresos asociados al  Paciente
	 */
	private int posIngreso;
	
	/**
	 *  Almacena el listado de empresas con Indicador Direccion Territorial = Si
	 */
    private ArrayList<HashMap<String, Object>> arrayEmpresas;
	
    /**
	 * Almacena el listado inicial por rango y por paciente
	 * */
    private ArrayList<DtoInformeInconsisenBD> listadoArray;
    
    /**
	 * Almacena la informacion de los Medios de Envio
	 * */
	private ArrayList<HashMap<String, Object>> listadoArrayMediosEnvio;

	/**
	 * Almacena la informacion de los convenios
	 * */
	private HashMap listadoConvenio;
	
    /**
     * Almacena el estado del Informe
     */
	private String estadoEnviado;
	
	/**
	 * Establece la confiracion del envio 
	 */
	private String confirmarEnvio;
	
	/**
	 * Almacena las variables incorrectas del informe
	 */
	private String[] seleccionVariablesInco;
	
	
	/**
	 * Almancena la información de los parametros de busqueda
	 * */
	private HashMap parametrosBusqueda;
	
	
	
	
	public ConsultaEnvioInconsistenciasenBDForm()
	{
		this.reset();
	}
	
	
	public void reset()
	{
		this.estado=new String("");
		this.confirmarEnvio=new String("N");
		this.estadoEnviado=new String("");
		this.informeIconsistencias = new DtoInformeInconsisenBD();
		this.posIngreso=ConstantesBD.codigoNuncaValido;
		this.ingresosPaciente=new HashMap();
		this.ingresosPaciente.put("numRegistros", "0");
		this.parametrosFiltros=new HashMap();
		this.parametrosFiltros.put("numRegistros", "0");
		this.parametrosBusqueda=new HashMap();
		this.parametrosBusqueda.put("numRegistros", "0");
		this.arrayEmpresas=new  ArrayList<HashMap<String, Object>>();
		this.listadoArray=new ArrayList<DtoInformeInconsisenBD>();
		this.listadoConvenio=new HashMap();
		this.listadoConvenio.put("numRegistros", "0");
		this.listadoArrayMediosEnvio= new ArrayList<HashMap<String, Object>>();
		this.seleccionVariablesInco=new String [15];
		
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public HashMap getIngresosPaciente() {
		return ingresosPaciente;
	}


	public void setIngresosPaciente(HashMap ingresosPaciente) {
		this.ingresosPaciente = ingresosPaciente;
	}


	public int getPosIngreso() {
		return posIngreso;
	}


	public void setPosIngreso(int posIngreso) {
		this.posIngreso = posIngreso;
	}
	
	public Object getParametrosFiltros(String key) {
		return parametrosFiltros.get(key);
	}

	public void setParametrosFiltros(String key, Object value) {
		this.parametrosFiltros.put(key,value);
	}
	
	public String getCuentaFiltro() {
		return this.parametrosFiltros.get("cuentaFiltro").toString();
	}

	public void setCuentaFiltro(String valor) {
		this.parametrosFiltros.put("cuentaFiltro",valor);
	}
	
	public String getCodigoPkInforme() {
		return this.parametrosFiltros.get("codigoPkInforme").toString();
	}

	public void setCodigoPkInforme(String valor) {
		this.parametrosFiltros.put("codigoPkInforme",valor);
	}
	
	public String getIngresoFiltro() {
		return this.parametrosFiltros.get("ingresoFiltro").toString();
	}
	
	public void setIngresoFiltro(String valor) {
		this.parametrosFiltros.put("ingresoFiltro",valor);
	}
	
	public void setViaIngresoFiltro(String valor) {
		this.parametrosFiltros.put("viaIngresoFiltro",valor);
	}
	
	public String getViaIngresoFiltro() {
		return this.parametrosFiltros.get("viaIngresoFiltro").toString();
	}
	
	public String getConvenioFiltro() {
		return this.parametrosFiltros.get("convenioFiltro").toString();
	}

	public void setConvenioFiltro(String valor) {
		this.parametrosFiltros.put("convenioFiltro",valor);
	}
	
	public void setSubCuentaFiltro(String valor) {
		this.parametrosFiltros.put("subCuentaFiltro",valor);
	}
	
	public String getSubCuentaFiltro() {
		return this.parametrosFiltros.get("subCuentaFiltro").toString();
	}
	
	public String getConvenioEnvioFiltro() {
		return this.parametrosFiltros.get("convenioEnvioFiltro").toString();
	}

	public void setConvenioEnvioFiltro(String valor) {
		this.parametrosFiltros.put("convenioEnvioFiltro",valor);
	}
	
	public String getFechaInicialEnvio() {
		return this.parametrosFiltros.get("fechaInicialEnvio").toString();
	}

	public void setFechaInicialEnvio(String valor) {
		this.parametrosFiltros.put("fechaInicialEnvio",valor);
	}
	
	public String getFechaInicialGeneracion() {
		return this.parametrosFiltros.get("fechaInicialGeneracion").toString();
	}

	public void setFechaInicialGeneracion(String valor) {
		this.parametrosFiltros.put("fechaInicialGeneracion",valor);
	}
	
	public String getFechaFinalGeneracion() {
		return this.parametrosFiltros.get("fechaFinalGeneracion").toString();
	}

	public void setFechaFinalGeneracion(String valor) {
		this.parametrosFiltros.put("fechaFinalGeneracion",valor);
	}

	public String getFechaFinalEnvio() {
		return this.parametrosFiltros.get("fechaFinalEnvio").toString();
	}

	public void setFechaFinalEnvio(String valor) {
		this.parametrosFiltros.put("fechaFinalEnvio",valor);
	}
	
	public HashMap getParametrosFiltros() {
		return parametrosFiltros;
	}


	public void setParametrosFiltros(HashMap parametrosFiltros) {
		this.parametrosFiltros = parametrosFiltros;
	}


	public DtoInformeInconsisenBD getInformeIconsistencias() {
		return informeIconsistencias;
	}


	public void setInformeIconsistencias(
			DtoInformeInconsisenBD informeIconsistencias) {
		this.informeIconsistencias = informeIconsistencias;
	}


	public ArrayList<HashMap<String, Object>> getArrayEmpresas() {
		return arrayEmpresas;
	}


	public void setArrayEmpresas(ArrayList<HashMap<String, Object>> arrayEmpresas) {
		this.arrayEmpresas = arrayEmpresas;
	}


	public ArrayList<HashMap<String, Object>> getListadoArrayMediosEnvio() {
		return listadoArrayMediosEnvio;
	}


	public void setListadoArrayMediosEnvio(
			ArrayList<HashMap<String, Object>> listadoArrayMediosEnvio) {
		this.listadoArrayMediosEnvio = listadoArrayMediosEnvio;
	}


	public String getEstadoEnviado() {
		return estadoEnviado;
	}


	public void setEstadoEnviado(String estadoEnviado) {
		this.estadoEnviado = estadoEnviado;
	}


	public String getConfirmarEnvio() {
		return confirmarEnvio;
	}


	public void setConfirmarEnvio(String confirmarEnvio) {
		this.confirmarEnvio = confirmarEnvio;
	}


	public String[] getSeleccionVariablesInco() {
		return seleccionVariablesInco;
	}


	public void setSeleccionVariablesInco(String[] seleccionVariablesInco) {
		this.seleccionVariablesInco = seleccionVariablesInco;
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


	public ArrayList<DtoInformeInconsisenBD> getListadoArray() {
		return listadoArray;
	}


	public void setListadoArray(ArrayList<DtoInformeInconsisenBD> listadoArray) {
		this.listadoArray = listadoArray;
	}


	public HashMap getListadoConvenio() {
		return listadoConvenio;
	}


	public void setListadoConvenio(HashMap listadoConvenio) {
		this.listadoConvenio = listadoConvenio;
	}


	
	
	
}
