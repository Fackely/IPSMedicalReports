package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.orm.EmpresasInstitucion;

public class ConceptoNotasPacientesForm extends ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String estado;
	
	private String naturalezaNotasPacientes;
	
	private boolean institucionMultiempresa;
	
	private boolean operacionExitosa;
	
	private int contadorFilasNuevas;
	
	private String codigoBusqueda;
	
	private String descripcionBusqueda;
	
	private String naturalezaDebitoBusqueda;
	
	private String naturalezaCreditoBusqueda;

	private String activoBusqueda;
	
	/**
	 * Atributo que almacena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;	
	
	private boolean busquedaAvanzada;
	
	private ArrayList<EmpresasInstitucion> listaEmpresasInstitucion;
	
	private ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes;
	
	private ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesInicial;
	
	private ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesGuardada;
	
	private ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesTmp;
	
	private HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucion;
	
	private HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucionTmp;
	
	/**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		
		return errores;
	}

	public void resetForma() {
		contadorFilasNuevas = 0;
		operacionExitosa = false;
		esDescendente = "";
		patronOrdenar = "";
		busquedaAvanzada = false;
		listaEmpresasInstitucion = new ArrayList<EmpresasInstitucion>();
		listaConceptosNotasPacientes = new ArrayList<DtoConceptoNotasPacientes>();
		listaConceptosNotasPacientesTmp = new ArrayList<DtoConceptoNotasPacientes>();
		setListaConceptosNotasPacientesGuardada(new ArrayList<DtoConceptoNotasPacientes>());
		setListaConceptosNotasPacientesInicial(new ArrayList<DtoConceptoNotasPacientes>());
		mapaCuentasContablesMultiInsitucion = new HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>>();
		mapaCuentasContablesMultiInsitucionTmp = new HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>>();
	}
	
	public void resetBusqueda() {
		codigoBusqueda = "";
		descripcionBusqueda = "";
		naturalezaDebitoBusqueda = "";
		naturalezaCreditoBusqueda = "";
		activoBusqueda = "";
		busquedaAvanzada = false;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNaturalezaNotasPacientes() {
		return naturalezaNotasPacientes;
	}

	public void setNaturalezaNotasPacientes(String naturalezaNotasPacientes) {
		this.naturalezaNotasPacientes = naturalezaNotasPacientes;
	}

	public boolean isInstitucionMultiempresa() {
		return institucionMultiempresa;
	}

	public void setInstitucionMultiempresa(boolean institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}

	public int getContadorFilasNuevas() {
		return contadorFilasNuevas;
	}

	public void setContadorFilasNuevas(int contadorFilasNuevas) {
		this.contadorFilasNuevas = contadorFilasNuevas;
	}

	public ArrayList<EmpresasInstitucion> getListaEmpresasInstitucion() {
		return listaEmpresasInstitucion;
	}

	public void setListaEmpresasInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresasInstitucion) {
		this.listaEmpresasInstitucion = listaEmpresasInstitucion;
	}

	public ArrayList<DtoConceptoNotasPacientes> getListaConceptosNotasPacientes() {
		return listaConceptosNotasPacientes;
	}

	public void setListaConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes) {
		this.listaConceptosNotasPacientes = listaConceptosNotasPacientes;
	}

	/**
	 * @param listaConceptosNotasPacientesInicial the listaConceptosNotasPacientesInicial to set
	 */
	public void setListaConceptosNotasPacientesInicial(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesInicial) {
		this.listaConceptosNotasPacientesInicial = listaConceptosNotasPacientesInicial;
	}

	/**
	 * @return the listaConceptosNotasPacientesInicial
	 */
	public ArrayList<DtoConceptoNotasPacientes> getListaConceptosNotasPacientesInicial() {
		return listaConceptosNotasPacientesInicial;
	}

	/**
	 * @param listaConceptosNotasPacientesGuardada the listaConceptosNotasPacientesGuardada to set
	 */
	public void setListaConceptosNotasPacientesGuardada(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesGuardada) {
		this.listaConceptosNotasPacientesGuardada = listaConceptosNotasPacientesGuardada;
	}

	/**
	 * @return the listaConceptosNotasPacientesGuardada
	 */
	public ArrayList<DtoConceptoNotasPacientes> getListaConceptosNotasPacientesGuardada() {
		return listaConceptosNotasPacientesGuardada;
	}

	public ArrayList<DtoConceptoNotasPacientes> getListaConceptosNotasPacientesTmp() {
		return listaConceptosNotasPacientesTmp;
	}

	public void setListaConceptosNotasPacientesTmp(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesTmp) {
		this.listaConceptosNotasPacientesTmp = listaConceptosNotasPacientesTmp;
	}	
	
	public HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> getMapaCuentasContablesMultiInsitucion() {
		return mapaCuentasContablesMultiInsitucion;
	}

	public void setMapaCuentasContablesMultiInsitucion(
			HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucion) {
		this.mapaCuentasContablesMultiInsitucion = mapaCuentasContablesMultiInsitucion;
	}

	public HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> getMapaCuentasContablesMultiInsitucionTmp() {
		return mapaCuentasContablesMultiInsitucionTmp;
	}

	public void setMapaCuentasContablesMultiInsitucionTmp(
			HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucionTmp) {
		this.mapaCuentasContablesMultiInsitucionTmp = mapaCuentasContablesMultiInsitucionTmp;
	}

	public boolean isOperacionExitosa() {
		return operacionExitosa;
	}

	public void setOperacionExitosa(boolean operacionExitosa) {
		this.operacionExitosa = operacionExitosa;
	}
	
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public String getCodigoBusqueda() {
		return codigoBusqueda;
	}

	public void setCodigoBusqueda(String codigoBusqueda) {
		this.codigoBusqueda = codigoBusqueda;
	}

	public String getDescripcionBusqueda() {
		return descripcionBusqueda;
	}

	public void setDescripcionBusqueda(String descripcionBusqueda) {
		this.descripcionBusqueda = descripcionBusqueda;
	}

	public String getNaturalezaDebitoBusqueda() {
		return naturalezaDebitoBusqueda;
	}

	public void setNaturalezaDebitoBusqueda(String naturalezaDebitoBusqueda) {
		this.naturalezaDebitoBusqueda = naturalezaDebitoBusqueda;
	}

	public String getNaturalezaCreditoBusqueda() {
		return naturalezaCreditoBusqueda;
	}

	public void setNaturalezaCreditoBusqueda(String naturalezaCreditoBusqueda) {
		this.naturalezaCreditoBusqueda = naturalezaCreditoBusqueda;
	}

	public String getActivoBusqueda() {
		return activoBusqueda;
	}

	public void setActivoBusqueda(String activoBusqueda) {
		this.activoBusqueda = activoBusqueda;
	}

	/**
	 * @param busquedaAvanzada the busquedaAvanzada to set
	 */
	public void setBusquedaAvanzada(boolean busquedaAvanzada) {
		this.busquedaAvanzada = busquedaAvanzada;
	}

	/**
	 * @return the busquedaAvanzada
	 */
	public boolean isBusquedaAvanzada() {
		return busquedaAvanzada;
	}
}
