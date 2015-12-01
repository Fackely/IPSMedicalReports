/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * @author Jairo Andrés Gomez
 * noviembre 9 de 2009
 *
 */
public class DtoReasignarProfesionalOdonto implements Serializable{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(DtoReasignarProfesionalOdonto.class);
	
	/*
	 * Atributos Tabla
	 */
	
	private int codigoPk;
	private String fechaProceso;
	private String horaProceso;
	private String usuarioProceso;
	private int codigoAgenda;
	private int codigoMedicoAnterior;
	private int codigoMedicoAsignado;
	private int centroAtencion;
	private int institucion;
	private String fechaAgenda;
	private String horaAgendaInicial;
	private String horaAgendaFinal;
	private int unidadConsulta;
	
	/*
	 * Atributos busqueda Servicios
	 */
	private String fechaInicialBusqueda;
	private String fechaFinalBusqueda;
	private int unidadAgendaBusqueda;
	private int centroAtencionBusqueda;
	private int profesionalSaludInicial;
	
	/*
	 * Atributos resultado busqueda
	 */
	private String centroAtencionNombre;
	private String profesionalSaludNombre;
	private String profesionalSaludNombreFinal;
	private String unidadConsultaNombre;
	private String consultorioNombre;
	private int codigoPkAgendaOdonto;
	
	/*
	 * Atributo seleccion registro
	 */
	private String registroSeleccionado;
	
	public DtoReasignarProfesionalOdonto(){
		this.reset();
	}
	
	public void reset(){
		
//		 * Atributos Tabla
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.fechaProceso = "";
		this.horaProceso = "";
		this.usuarioProceso = "";
		this.codigoAgenda = ConstantesBD.codigoNuncaValido;
		this.codigoMedicoAnterior = ConstantesBD.codigoNuncaValido;
		this.codigoMedicoAsignado = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaAgenda = "";
		this.horaAgendaInicial = "";
		this.horaAgendaFinal = "";
		this.unidadConsulta = ConstantesBD.codigoNuncaValido;
		
//		 * Atributos busqueda Servicios
		this.fechaInicialBusqueda = "";
		this.fechaFinalBusqueda = "";
		this.unidadAgendaBusqueda = 0;
		this.centroAtencionBusqueda = ConstantesBD.codigoNuncaValido;
		this.profesionalSaludInicial = ConstantesBD.codigoNuncaValido;
		
//		 * Atributos resultado busqueda
		this.centroAtencionNombre = "";
		this.profesionalSaludNombre = "";
		this.profesionalSaludNombreFinal = "";
		this.unidadConsultaNombre = "";
		this.consultorioNombre = "";
		this.codigoPkAgendaOdonto = ConstantesBD.codigoNuncaValido;
		
//		 * Atributo seleccion registro
		this.registroSeleccionado = ConstantesBD.acronimoNo;
	}
	
	/**
	 * Método q permite imprimir todos los atributos del dto
	 */
	public void imprimir(){
//		 * Atributos Tabla
		logger.info("codigoPk:: "+this.codigoPk);
		logger.info("fechaProceso:: "+this.fechaProceso);
		logger.info("horaProceso:: "+this.horaProceso);
		logger.info("usuarioProceso:: "+this.usuarioProceso);
		logger.info("codigoAgenda:: "+this.codigoAgenda);
		logger.info("codigoMedicoAnterior:: "+this.codigoMedicoAnterior);
		logger.info("codigoMedicoAsignado:: "+this.codigoMedicoAsignado);
		logger.info("centroAtencion:: "+this.centroAtencion);
		logger.info("institucion:: "+this.institucion);
		logger.info("fechaAgenda:: "+this.fechaAgenda);
		logger.info("horaAgendaInicial:: "+this.horaAgendaInicial);
		logger.info("horaAgendaFinal:: "+this.horaAgendaFinal);
		logger.info("unidadConsulta:: "+this.unidadConsulta);
		
//		 * Atributos busqueda Servicios
		logger.info("fechaInicialBusqueda:: "+this.fechaInicialBusqueda);
		logger.info("fechaFinalBusqueda:: "+this.fechaFinalBusqueda);
		logger.info("unidadAgendaBusqueda:: "+this.unidadAgendaBusqueda);
		logger.info("centroAtencionBusqueda:: "+this.centroAtencionBusqueda);
		logger.info("profesionalSaludInicial:: "+this.profesionalSaludInicial);
		
//		 * Atributos resultado busqueda
		logger.info("centroAtencionNombre:: "+this.centroAtencionNombre);
		logger.info("profesionalSaludNombre:: "+this.profesionalSaludNombre);
		logger.info("profesionalSaludNombreFinal:: "+this.profesionalSaludNombreFinal);
		logger.info("unidadConsultaNombre:: "+this.unidadConsultaNombre);
		logger.info("consultorioNombre:: "+this.consultorioNombre);
		logger.info("codigoPkAgendaOdonto:: "+this.codigoPkAgendaOdonto);
		
//		 * Atributo seleccion registro
		logger.info("registroSeleccionado:: "+this.registroSeleccionado);
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the fechaProceso
	 */
	public String getFechaProceso() {
		return fechaProceso;
	}

	/**
	 * @param fechaProceso the fechaProceso to set
	 */
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	/**
	 * @return the horaProceso
	 */
	public String getHoraProceso() {
		return horaProceso;
	}

	/**
	 * @param horaProceso the horaProceso to set
	 */
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}

	/**
	 * @return the usuarioProceso
	 */
	public String getUsuarioProceso() {
		return usuarioProceso;
	}

	/**
	 * @param usuarioProceso the usuarioProceso to set
	 */
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}

	/**
	 * @return the codigoAgenda
	 */
	public int getCodigoAgenda() {
		return codigoAgenda;
	}

	/**
	 * @param codigoAgenda the codigoAgenda to set
	 */
	public void setCodigoAgenda(int codigoAgenda) {
		this.codigoAgenda = codigoAgenda;
	}

	/**
	 * @return the codigoMedicoAnterior
	 */
	public int getCodigoMedicoAnterior() {
		return codigoMedicoAnterior;
	}

	/**
	 * @param codigoMedicoAnterior the codigoMedicoAnterior to set
	 */
	public void setCodigoMedicoAnterior(int codigoMedicoAnterior) {
		this.codigoMedicoAnterior = codigoMedicoAnterior;
	}

	/**
	 * @return the codigoMedicoAsignado
	 */
	public int getCodigoMedicoAsignado() {
		return codigoMedicoAsignado;
	}

	/**
	 * @param codigoMedicoAsignado the codigoMedicoAsignado to set
	 */
	public void setCodigoMedicoAsignado(int codigoMedicoAsignado) {
		this.codigoMedicoAsignado = codigoMedicoAsignado;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the fechaAgenda
	 */
	public String getFechaAgenda() {
		return fechaAgenda;
	}

	/**
	 * @param fechaAgenda the fechaAgenda to set
	 */
	public void setFechaAgenda(String fechaAgenda) {
		this.fechaAgenda = fechaAgenda;
	}

	/**
	 * @return the horaAgendaInicial
	 */
	public String getHoraAgendaInicial() {
		return horaAgendaInicial;
	}

	/**
	 * @param horaAgendaInicial the horaAgendaInicial to set
	 */
	public void setHoraAgendaInicial(String horaAgendaInicial) {
		this.horaAgendaInicial = horaAgendaInicial;
	}

	/**
	 * @return the horaAgendaFinal
	 */
	public String getHoraAgendaFinal() {
		return horaAgendaFinal;
	}

	/**
	 * @param horaAgendaFinal the horaAgendaFinal to set
	 */
	public void setHoraAgendaFinal(String horaAgendaFinal) {
		this.horaAgendaFinal = horaAgendaFinal;
	}

	/**
	 * @return the unidadConsulta
	 */
	public int getUnidadConsulta() {
		return unidadConsulta;
	}

	/**
	 * @param unidadConsulta the unidadConsulta to set
	 */
	public void setUnidadConsulta(int unidadConsulta) {
		this.unidadConsulta = unidadConsulta;
	}

	/**
	 * @return the fechaInicialBusqueda
	 */
	public String getFechaInicialBusqueda() {
		return fechaInicialBusqueda;
	}

	/**
	 * @param fechaInicialBusqueda the fechaInicialBusqueda to set
	 */
	public void setFechaInicialBusqueda(String fechaInicialBusqueda) {
		this.fechaInicialBusqueda = fechaInicialBusqueda;
	}

	/**
	 * @return the fechaFinalBusqueda
	 */
	public String getFechaFinalBusqueda() {
		return fechaFinalBusqueda;
	}

	/**
	 * @param fechaFinalBusqueda the fechaFinalBusqueda to set
	 */
	public void setFechaFinalBusqueda(String fechaFinalBusqueda) {
		this.fechaFinalBusqueda = fechaFinalBusqueda;
	}

	/**
	 * @return the unidadAgendaBusqueda
	 */
	public int getUnidadAgendaBusqueda() {
		return unidadAgendaBusqueda;
	}

	/**
	 * @param unidadAgendaBusqueda the unidadAgendaBusqueda to set
	 */
	public void setUnidadAgendaBusqueda(int unidadAgendaBusqueda) {
		this.unidadAgendaBusqueda = unidadAgendaBusqueda;
	}

	/**
	 * @return the centroAtencionBusqueda
	 */
	public int getCentroAtencionBusqueda() {
		return centroAtencionBusqueda;
	}

	/**
	 * @param centroAtencionBusqueda the centroAtencionBusqueda to set
	 */
	public void setCentroAtencionBusqueda(int centroAtencionBusqueda) {
		this.centroAtencionBusqueda = centroAtencionBusqueda;
	}

	/**
	 * @return the profesionalSaludInicial
	 */
	public int getProfesionalSaludInicial() {
		return profesionalSaludInicial;
	}

	/**
	 * @param profesionalSaludInicial the profesionalSaludInicial to set
	 */
	public void setProfesionalSaludInicial(int profesionalSaludInicial) {
		this.profesionalSaludInicial = profesionalSaludInicial;
	}

	/**
	 * @return the centroAtencionNombre
	 */
	public String getCentroAtencionNombre() {
		return centroAtencionNombre;
	}

	/**
	 * @param centroAtencionNombre the centroAtencionNombre to set
	 */
	public void setCentroAtencionNombre(String centroAtencionNombre) {
		this.centroAtencionNombre = centroAtencionNombre;
	}

	/**
	 * @return the profesionalSaludNombre
	 */
	public String getProfesionalSaludNombre() {
		return profesionalSaludNombre;
	}

	/**
	 * @param profesionalSaludNombre the profesionalSaludNombre to set
	 */
	public void setProfesionalSaludNombre(String profesionalSaludNombre) {
		this.profesionalSaludNombre = profesionalSaludNombre;
	}

	/**
	 * @return the unidadConsultaNombre
	 */
	public String getUnidadConsultaNombre() {
		return unidadConsultaNombre;
	}

	/**
	 * @param unidadConsultaNombre the unidadConsultaNombre to set
	 */
	public void setUnidadConsultaNombre(String unidadConsultaNombre) {
		this.unidadConsultaNombre = unidadConsultaNombre;
	}

	/**
	 * @return the consultorioNombre
	 */
	public String getConsultorioNombre() {
		return consultorioNombre;
	}

	/**
	 * @param consultorioNombre the consultorioNombre to set
	 */
	public void setConsultorioNombre(String consultorioNombre) {
		this.consultorioNombre = consultorioNombre;
	}

	/**
	 * @return the registroSeleccionado
	 */
	public String getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	/**
	 * @param registroSeleccionado the registroSeleccionado to set
	 */
	public void setRegistroSeleccionado(String registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	/**
	 * @return the codigoPkAgendaOdonto
	 */
	public int getCodigoPkAgendaOdonto() {
		return codigoPkAgendaOdonto;
	}

	/**
	 * @param codigoPkAgendaOdonto the codigoPkAgendaOdonto to set
	 */
	public void setCodigoPkAgendaOdonto(int codigoPkAgendaOdonto) {
		this.codigoPkAgendaOdonto = codigoPkAgendaOdonto;
	}

	/**
	 * @return the profesionalSaludNombreFinal
	 */
	public String getProfesionalSaludNombreFinal() {
		return profesionalSaludNombreFinal;
	}

	/**
	 * @param profesionalSaludNombreFinal the profesionalSaludNombreFinal to set
	 */
	public void setProfesionalSaludNombreFinal(String profesionalSaludNombreFinal) {
		this.profesionalSaludNombreFinal = profesionalSaludNombreFinal;
	}
	
	
}
