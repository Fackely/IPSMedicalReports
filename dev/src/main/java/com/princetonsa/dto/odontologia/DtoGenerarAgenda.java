package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;

import util.ConstantesBD;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class DtoGenerarAgenda implements Serializable
{
	/**
	 * Manejo de versiones
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * agenda odontologica generada correctamente
	 */
	private ArrayList<DtoAgendaOdontologica> agendaOdonGen;
	/**
	 * agenda odontologica generada parcialmente 
	 */
	private ArrayList<DtoAgendaOdontologica> agendaOdonXGen;
	/**
	 * agendas odontologicas que no se pudieron generar por al existencia de excepciones para ese dia y ese centro de costo
	 */
	private ArrayList<DtoExcepcionesHorarioAtencion> agendaOdonConExcepciones;
	private int agenGeneradas;
	private int agenXGenerar;
	private int agenOdonConExcep;
	private String usuarioModifica;
	
	// Atributos de ayuda generacion agenda odontologica
	/*
	private int codigoActividadAutorizacion;
	private ArrayList<HashMap> listCentroAtencion;
	private ArrayList<HashMap> listUnidadesAgendaXUsuario;
	private ArrayList<HashMap> listConsultorios;
	private ArrayList<HashMap<String, Object>> listProfesionalesActivos;
	private ArrayList<HashMap> listDiaSemana;
	private ArrayList<DtoHorarioAtencion> arrayHorarioAten;
	*/
	
	// Atributos Formulario
	private String centroAtencion;
	private String fechaInicial;
	private String fechaFinal;
	private String unidadAgenda;
	private String consultorio;
	private String diaSemana;
	private String profesionalSalud;
	
	private String regenerarAgendaOdon;
	private String codigosHorariosAtencion;
	
	public DtoGenerarAgenda()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.agendaOdonGen = new ArrayList<DtoAgendaOdontologica>();
		this.agendaOdonXGen = new ArrayList<DtoAgendaOdontologica>();
		this.agendaOdonConExcepciones = new ArrayList<DtoExcepcionesHorarioAtencion>();
		this.usuarioModifica = "";
		this.agenGeneradas = 0;
		this.agenXGenerar = 0;
		this.agenOdonConExcep = 0;
		
		// Atributos de ayuda generacion agenda odontologica
		/*
		this.codigoActividadAutorizacion = ConstantesBD.codigoNuncaValido;
		this.listCentroAtencion = new ArrayList<HashMap>();
		this.listUnidadesAgendaXUsuario = new  ArrayList<HashMap>();
		this.listConsultorios = new  ArrayList<HashMap>();
		this.listProfesionalesActivos = new ArrayList<HashMap<String, Object>>();
		this.listDiaSemana = new  ArrayList<HashMap>();
		this.arrayHorarioAten = new ArrayList<DtoHorarioAtencion>();
		*/
		
		// atributos formulario
		this.centroAtencion = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.unidadAgenda = "";
		this.consultorio = "";
		this.diaSemana = "";
		this.profesionalSalud = "";
		
		
		this.regenerarAgendaOdon =  ConstantesBD.acronimoNo;
		this.codigosHorariosAtencion = ConstantesBD.codigoNuncaValido+"";
	}

	
	/**
	 * @return the agendaOdonGen
	 */
	public ArrayList<DtoAgendaOdontologica> getAgendaOdonGen() {
		return agendaOdonGen;
	}

	/**
	 * @param agendaOdonGen the agendaOdonGen to set
	 */
	public void setAgendaOdonGen(ArrayList<DtoAgendaOdontologica> agendaOdonGen) {
		this.agendaOdonGen = agendaOdonGen;
	}

	/**
	 * @return the agendaOdonXGen
	 */
	public ArrayList<DtoAgendaOdontologica> getAgendaOdonXGen() {
		return agendaOdonXGen;
	}

	/**
	 * @param agendaOdonXGen the agendaOdonXGen to set
	 */
	public void setAgendaOdonXGen(ArrayList<DtoAgendaOdontologica> agendaOdonXGen) {
		this.agendaOdonXGen = agendaOdonXGen;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the consultorio
	 */
	public String getConsultorio() {
		return consultorio;
	}

	/**
	 * @param consultorio the consultorio to set
	 */
	public void setConsultorio(String consultorio) {
		this.consultorio = consultorio;
	}

	/**
	 * @return the diaSemana
	 */
	public String getDiaSemana() {
		return diaSemana;
	}

	/**
	 * @param diaSemana the diaSemana to set
	 */
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * @return the profesionalSalud
	 */
	public String getProfesionalSalud() {
		return profesionalSalud;
	}

	/**
	 * @param profesionalSalud the profesionalSalud to set
	 */
	public void setProfesionalSalud(String profesionalSalud) {
		this.profesionalSalud = profesionalSalud;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the agenGeneradas
	 */
	public int getAgenGeneradas() {
		return agenGeneradas;
	}

	/**
	 * @param agenGeneradas the agenGeneradas to set
	 */
	public void setAgenGeneradas(int agenGeneradas) {
		this.agenGeneradas = agenGeneradas;
	}

	/**
	 * @return the agenXGenerar
	 */
	public int getAgenXGenerar() {
		return agenXGenerar;
	}

	/**
	 * @param agenXGenerar the agenXGenerar to set
	 */
	public void setAgenXGenerar(int agenXGenerar) {
		this.agenXGenerar = agenXGenerar;
	}

	/**
	 * @return the agendaOdonConExcepciones
	 */
	public ArrayList<DtoExcepcionesHorarioAtencion> getAgendaOdonConExcepciones() {
		return agendaOdonConExcepciones;
	}

	/**
	 * @param agendaOdonConExcepciones the agendaOdonConExcepciones to set
	 */
	public void setAgendaOdonConExcepciones(
			ArrayList<DtoExcepcionesHorarioAtencion> agendaOdonConExcepciones) {
		this.agendaOdonConExcepciones = agendaOdonConExcepciones;
	}

	/**
	 * @return the agenOdonConExcep
	 */
	public int getAgenOdonConExcep() {
		return agenOdonConExcep;
	}

	/**
	 * @param agenOdonConExcep the agenOdonConExcep to set
	 */
	public void setAgenOdonConExcep(int agenOdonConExcep) {
		this.agenOdonConExcep = agenOdonConExcep;
	}

	/**
	 * @return the regenerarAgendaOdon
	 */
	public String getRegenerarAgendaOdon() {
		return regenerarAgendaOdon;
	}

	/**
	 * @param regenerarAgendaOdon the regenerarAgendaOdon to set
	 */
	public void setRegenerarAgendaOdon(String regenerarAgendaOdon) {
		this.regenerarAgendaOdon = regenerarAgendaOdon;
	}

	/**
	 * @return the codigosHorariosAtencion
	 */
	public String getCodigosHorariosAtencion() {
		return codigosHorariosAtencion;
	}

	/**
	 * @param codigosHorariosAtencion the codigosHorariosAtencion to set
	 */
	public void setCodigosHorariosAtencion(String codigosHorariosAtencion) {
		this.codigosHorariosAtencion = codigosHorariosAtencion;
	}
	
	
	public void resetListGenAgenda()
	{
		this.agendaOdonGen = new ArrayList<DtoAgendaOdontologica>();
		this.agendaOdonXGen = new ArrayList<DtoAgendaOdontologica>();
		this.agendaOdonConExcepciones = new ArrayList<DtoExcepcionesHorarioAtencion>();
	}
}
