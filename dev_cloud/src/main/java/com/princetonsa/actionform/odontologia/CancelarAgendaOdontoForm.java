/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCancelarAgendaOdonto;

/**
 * @author axioma
 *
 */
public class CancelarAgendaOdontoForm extends ValidatorForm{

	/*
	 * Atributos
	 */
	
	private String estado;
	private ResultadoBoolean mensaje;
	private DtoCancelarAgendaOdonto datosBusqueda;
	private ArrayList<HashMap<String,Object>> listCentroAtencion;
	private ArrayList<HashMap> listUnidadesAgendaXUsuario;
	private ArrayList<HashMap> listConsultorios;
	private ArrayList<HashMap<String, Object>> listProfesionalesActivos;
	private ArrayList<HashMap> listDiaSemana;
	private String mostrarResumenCanAgenOdon;
	private int noItemCancelados;
	private ArrayList<String> listItemCancelados;
	
	/*
	 * Atributos listar
	 */
	private ArrayList<DtoAgendaOdontologica> listAgenda;
	
	/*
	 * Atrubuto Ajax
	 */
	private int index;
	
	/*
	 * Atributo pager
	 */
	private String linkSiguiente;
	
	/*
	 * Atributos para ordenar
	 */
	private String patronOrdenar;
	private String esDescendente;
	
	private ArrayList<DtoCancelarAgendaOdonto> listaCancelacionAgenda;
	
	/*
	 * Fin atributos
	 */
	
	/**
	 * M�todo que reinicia los atrubutos del form
	 */
	public void reset(){
		this.estado = "";
		this.mensaje = new ResultadoBoolean(false);
		this.datosBusqueda = new DtoCancelarAgendaOdonto();
		this.listCentroAtencion = new ArrayList<HashMap<String,Object>>();
		this.listUnidadesAgendaXUsuario = new ArrayList<HashMap>();
		this.listConsultorios = new ArrayList<HashMap>();
		this.listProfesionalesActivos = new ArrayList<HashMap<String, Object>>();
		this.listDiaSemana = new ArrayList<HashMap>();
		this.mostrarResumenCanAgenOdon = "";
		this.noItemCancelados = ConstantesBD.codigoNuncaValido;
		this.listItemCancelados = new ArrayList<String>();
		this.listaCancelacionAgenda = new ArrayList<DtoCancelarAgendaOdonto>();
		
		
//		Atributo ajax
		this.index = ConstantesBD.codigoNuncaValido;
		
//		 * Atributos listar
		this.listAgenda = new ArrayList<DtoAgendaOdontologica>();
		
//		Atributo pager
		this.linkSiguiente = "";
		
//		 * Atributos para ordenar
		this.patronOrdenar = "";
		this.esDescendente = "";
	}
	
	public ArrayList<DtoCancelarAgendaOdonto> getListaCancelacionAgenda() {
		return listaCancelacionAgenda;
	}

	public void setListaCancelacionAgenda(
			ArrayList<DtoCancelarAgendaOdonto> listaCancelacionAgenda) {
		this.listaCancelacionAgenda = listaCancelacionAgenda;
	}

	/**
	 * M�todo que realiza la validacion de los atributos del form
	 * @param mapping
	 * @param request
	 * @return
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		if (this.estado.equals("validaCancelarAgenda") || this.estado.equals("listarAgenda")){
			boolean flag = true;
			if (this.datosBusqueda.getFechaInicial().equals("")){
				errores.add("", new ActionMessage("errors.required", "La fecha inicial"));
				flag = false;
			}
			if (this.datosBusqueda.getFechaFinal().equals("")){
				errores.add("", new ActionMessage("errors.required", "La fecha final"));
				flag = false;
			}
			if(flag){
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.datosBusqueda.getFechaInicial(), UtilidadFecha.getFechaActual()))
					errores.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La fecha inicial", "la fecha actual"));
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.datosBusqueda.getFechaFinal(), this.datosBusqueda.getFechaInicial()))
					errores.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La fecha final", "la fecha inicial"));
				if(this.datosBusqueda.getFechaInicial().equals(this.datosBusqueda.getFechaFinal()))
					if (!this.datosBusqueda.getHoraInicio().equals("") && !this.datosBusqueda.getHoraFin().equals("") &&
							UtilidadFecha.esHoraMenorQueOtraReferencia(this.datosBusqueda.getHoraFin(), this.datosBusqueda.getHoraInicio()))
						errores.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La hora final", "la hora inicial"));
			}
			if (!errores.isEmpty() && this.estado.equals("validaCancelarAgenda"))
				this.estado = "empezar";
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
	 * @return the datosBusqueda
	 */
	public DtoCancelarAgendaOdonto getDatosBusqueda() {
		return datosBusqueda;
	}

	/**
	 * @param datosBusqueda the datosBusqueda to set
	 */
	public void setDatosBusqueda(DtoCancelarAgendaOdonto datosBusqueda) {
		this.datosBusqueda = datosBusqueda;
	}

	/**
	 * @return the listCentroAtencion
	 */
	public ArrayList<HashMap<String,Object>> getListCentroAtencion() {
		return listCentroAtencion;
	}

	/**
	 * @param listCentroAtencion the listCentroAtencion to set
	 */
	public void setListCentroAtencion(ArrayList<HashMap<String,Object>> listCentroAtencion) {
		this.listCentroAtencion = listCentroAtencion;
	}

	/**
	 * @return the listUnidadesAgendaXUsuario
	 */
	public ArrayList<HashMap> getListUnidadesAgendaXUsuario() {
		return listUnidadesAgendaXUsuario;
	}

	/**
	 * @param listUnidadesAgendaXUsuario the listUnidadesAgendaXUsuario to set
	 */
	public void setListUnidadesAgendaXUsuario(
			ArrayList<HashMap> listUnidadesAgendaXUsuario) {
		this.listUnidadesAgendaXUsuario = listUnidadesAgendaXUsuario;
	}

	/**
	 * @return the listDiaSemana
	 */
	public ArrayList<HashMap> getListDiaSemana() {
		return listDiaSemana;
	}

	/**
	 * @param listDiaSemana the listDiaSemana to set
	 */
	public void setListDiaSemana(ArrayList<HashMap> listDiaSemana) {
		this.listDiaSemana = listDiaSemana;
	}

	/**
	 * @return the listConsultorios
	 */
	public ArrayList<HashMap> getListConsultorios() {
		return listConsultorios;
	}

	/**
	 * @param listConsultorios the listConsultorios to set
	 */
	public void setListConsultorios(ArrayList<HashMap> listConsultorios) {
		this.listConsultorios = listConsultorios;
	}

	/**
	 * @return the listProfesionalesActivos
	 */
	public ArrayList<HashMap<String, Object>> getListProfesionalesActivos() {
		return listProfesionalesActivos;
	}

	/**
	 * @param listProfesionalesActivos the listProfesionalesActivos to set
	 */
	public void setListProfesionalesActivos(
			ArrayList<HashMap<String, Object>> listProfesionalesActivos) {
		this.listProfesionalesActivos = listProfesionalesActivos;
	}

	/**
	 * @return the mostrarResumenCanAgenOdon
	 */
	public String getMostrarResumenCanAgenOdon() {
		return mostrarResumenCanAgenOdon;
	}

	/**
	 * @param mostrarResumenCanAgenOdon the mostrarResumenCanAgenOdon to set
	 */
	public void setMostrarResumenCanAgenOdon(String mostrarResumenCanAgenOdon) {
		this.mostrarResumenCanAgenOdon = mostrarResumenCanAgenOdon;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the listAgenda
	 */
	public ArrayList<DtoAgendaOdontologica> getListAgenda() {
		return listAgenda;
	}

	/**
	 * @param listAgenda the listAgenda to set
	 */
	public void setListAgenda(ArrayList<DtoAgendaOdontologica> listAgenda) {
		this.listAgenda = listAgenda;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the noItemCancelados
	 */
	public int getNoItemCancelados() {
		return noItemCancelados;
	}

	/**
	 * @param noItemCancelados the noItemCancelados to set
	 */
	public void setNoItemCancelados(int noItemCancelados) {
		this.noItemCancelados = noItemCancelados;
	}

	/**
	 * @return the listItemCancelados
	 */
	public ArrayList<String> getListItemCancelados() {
		return listItemCancelados;
	}

	/**
	 * @param listItemCancelados the listItemCancelados to set
	 */
	public void setListItemCancelados(ArrayList<String> listItemCancelados) {
		this.listItemCancelados = listItemCancelados;
	}
}
