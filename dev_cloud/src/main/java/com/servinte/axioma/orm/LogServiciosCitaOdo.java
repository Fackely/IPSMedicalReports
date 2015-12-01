package com.servinte.axioma.orm;

// Generated Nov 18, 2010 5:00:08 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;

/**
 * LogServiciosCitaOdo generated by hbm2java
 */
public class LogServiciosCitaOdo implements java.io.Serializable {

	private long codigoPk;
	private UnidadesConsulta unidadesConsulta;
	private AgendaOdontologica agendaOdontologica;
	private Contratos contratos;
	private Solicitudes solicitudes;
	private ServiciosCitaOdontologica serviciosCitaOdontologica;
	private Usuarios usuarios;
	private Servicios servicios;
	private ProgramasHallazgoPieza programasHallazgoPieza;
	private Integer duracion;
	private String activo;
	private Date fechaModifica;
	private String horaModifica;
	private BigDecimal valorTarifa;
	private String estadoCita;
	private Date fechaCita;
	private String horaInicio;
	private String horaFin;
	private String aplicaAbono;
	private String aplicaAnticipo;

	public LogServiciosCitaOdo() {
	}

	public LogServiciosCitaOdo(long codigoPk,
			ServiciosCitaOdontologica serviciosCitaOdontologica,
			Usuarios usuarios, Servicios servicios, String activo,
			Date fechaModifica, String horaModifica, String estadoCita,
			Date fechaCita, String aplicaAbono, String aplicaAnticipo) {
		this.codigoPk = codigoPk;
		this.serviciosCitaOdontologica = serviciosCitaOdontologica;
		this.usuarios = usuarios;
		this.servicios = servicios;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.estadoCita = estadoCita;
		this.fechaCita = fechaCita;
		this.aplicaAbono = aplicaAbono;
		this.aplicaAnticipo = aplicaAnticipo;
	}

	public LogServiciosCitaOdo(long codigoPk,
			UnidadesConsulta unidadesConsulta,
			AgendaOdontologica agendaOdontologica, Contratos contratos,
			Solicitudes solicitudes,
			ServiciosCitaOdontologica serviciosCitaOdontologica,
			Usuarios usuarios, Servicios servicios,
			ProgramasHallazgoPieza programasHallazgoPieza, Integer duracion,
			String activo, Date fechaModifica, String horaModifica,
			BigDecimal valorTarifa, String estadoCita, Date fechaCita,
			String horaInicio, String horaFin, String aplicaAbono,
			String aplicaAnticipo) {
		this.codigoPk = codigoPk;
		this.unidadesConsulta = unidadesConsulta;
		this.agendaOdontologica = agendaOdontologica;
		this.contratos = contratos;
		this.solicitudes = solicitudes;
		this.serviciosCitaOdontologica = serviciosCitaOdontologica;
		this.usuarios = usuarios;
		this.servicios = servicios;
		this.programasHallazgoPieza = programasHallazgoPieza;
		this.duracion = duracion;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.valorTarifa = valorTarifa;
		this.estadoCita = estadoCita;
		this.fechaCita = fechaCita;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.aplicaAbono = aplicaAbono;
		this.aplicaAnticipo = aplicaAnticipo;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public UnidadesConsulta getUnidadesConsulta() {
		return this.unidadesConsulta;
	}

	public void setUnidadesConsulta(UnidadesConsulta unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
	}

	public AgendaOdontologica getAgendaOdontologica() {
		return this.agendaOdontologica;
	}

	public void setAgendaOdontologica(AgendaOdontologica agendaOdontologica) {
		this.agendaOdontologica = agendaOdontologica;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public Solicitudes getSolicitudes() {
		return this.solicitudes;
	}

	public void setSolicitudes(Solicitudes solicitudes) {
		this.solicitudes = solicitudes;
	}

	public ServiciosCitaOdontologica getServiciosCitaOdontologica() {
		return this.serviciosCitaOdontologica;
	}

	public void setServiciosCitaOdontologica(
			ServiciosCitaOdontologica serviciosCitaOdontologica) {
		this.serviciosCitaOdontologica = serviciosCitaOdontologica;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public ProgramasHallazgoPieza getProgramasHallazgoPieza() {
		return this.programasHallazgoPieza;
	}

	public void setProgramasHallazgoPieza(
			ProgramasHallazgoPieza programasHallazgoPieza) {
		this.programasHallazgoPieza = programasHallazgoPieza;
	}

	public Integer getDuracion() {
		return this.duracion;
	}

	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public BigDecimal getValorTarifa() {
		return this.valorTarifa;
	}

	public void setValorTarifa(BigDecimal valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	public String getEstadoCita() {
		return this.estadoCita;
	}

	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	public Date getFechaCita() {
		return this.fechaCita;
	}

	public void setFechaCita(Date fechaCita) {
		this.fechaCita = fechaCita;
	}

	public String getHoraInicio() {
		return this.horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return this.horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public String getAplicaAbono() {
		return this.aplicaAbono;
	}

	public void setAplicaAbono(String aplicaAbono) {
		this.aplicaAbono = aplicaAbono;
	}

	public String getAplicaAnticipo() {
		return this.aplicaAnticipo;
	}

	public void setAplicaAnticipo(String aplicaAnticipo) {
		this.aplicaAnticipo = aplicaAnticipo;
	}

}
