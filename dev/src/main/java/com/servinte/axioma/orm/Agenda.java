package com.servinte.axioma.orm;

// Generated 24/10/2012 09:25:00 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Agenda generated by hbm2java
 */
public class Agenda implements java.io.Serializable {

	private int codigo;
	private Medicos medicos;
	private DiasSemana diasSemana;
	private CentroAtencion centroAtencion;
	private UnidadesConsulta unidadesConsulta;
	private Usuarios usuarios;
	private Date fechaGen;
	private String horaGen;
	private int consultorio;
	private Date fecha;
	private String horaInicio;
	private String horaFin;
	private int tiempoSesion;
	private int cupos;
	private boolean activo;
	private Set serviciosCitas = new HashSet(0);
	private Set citas = new HashSet(0);

	public Agenda() {
	}

	public Agenda(int codigo, DiasSemana diasSemana,
			CentroAtencion centroAtencion, UnidadesConsulta unidadesConsulta,
			Usuarios usuarios, Date fechaGen, String horaGen, int consultorio,
			Date fecha, String horaInicio, String horaFin, int tiempoSesion,
			int cupos, boolean activo) {
		this.codigo = codigo;
		this.diasSemana = diasSemana;
		this.centroAtencion = centroAtencion;
		this.unidadesConsulta = unidadesConsulta;
		this.usuarios = usuarios;
		this.fechaGen = fechaGen;
		this.horaGen = horaGen;
		this.consultorio = consultorio;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.tiempoSesion = tiempoSesion;
		this.cupos = cupos;
		this.activo = activo;
	}

	public Agenda(int codigo, Medicos medicos, DiasSemana diasSemana,
			CentroAtencion centroAtencion, UnidadesConsulta unidadesConsulta,
			Usuarios usuarios, Date fechaGen, String horaGen, int consultorio,
			Date fecha, String horaInicio, String horaFin, int tiempoSesion,
			int cupos, boolean activo, Set serviciosCitas, Set citas) {
		this.codigo = codigo;
		this.medicos = medicos;
		this.diasSemana = diasSemana;
		this.centroAtencion = centroAtencion;
		this.unidadesConsulta = unidadesConsulta;
		this.usuarios = usuarios;
		this.fechaGen = fechaGen;
		this.horaGen = horaGen;
		this.consultorio = consultorio;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.tiempoSesion = tiempoSesion;
		this.cupos = cupos;
		this.activo = activo;
		this.serviciosCitas = serviciosCitas;
		this.citas = citas;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public DiasSemana getDiasSemana() {
		return this.diasSemana;
	}

	public void setDiasSemana(DiasSemana diasSemana) {
		this.diasSemana = diasSemana;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public UnidadesConsulta getUnidadesConsulta() {
		return this.unidadesConsulta;
	}

	public void setUnidadesConsulta(UnidadesConsulta unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Date getFechaGen() {
		return this.fechaGen;
	}

	public void setFechaGen(Date fechaGen) {
		this.fechaGen = fechaGen;
	}

	public String getHoraGen() {
		return this.horaGen;
	}

	public void setHoraGen(String horaGen) {
		this.horaGen = horaGen;
	}

	public int getConsultorio() {
		return this.consultorio;
	}

	public void setConsultorio(int consultorio) {
		this.consultorio = consultorio;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public int getTiempoSesion() {
		return this.tiempoSesion;
	}

	public void setTiempoSesion(int tiempoSesion) {
		this.tiempoSesion = tiempoSesion;
	}

	public int getCupos() {
		return this.cupos;
	}

	public void setCupos(int cupos) {
		this.cupos = cupos;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Set getServiciosCitas() {
		return this.serviciosCitas;
	}

	public void setServiciosCitas(Set serviciosCitas) {
		this.serviciosCitas = serviciosCitas;
	}

	public Set getCitas() {
		return this.citas;
	}

	public void setCitas(Set citas) {
		this.citas = citas;
	}

}
