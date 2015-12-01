package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * LogDetPlanTratamiento generated by hbm2java
 */
public class LogDetPlanTratamiento implements java.io.Serializable {

	private long codigoPk;
	private PiezaDental piezaDental;
	private CitasOdontologicas citasOdontologicas;
	private DetPlanTratamiento detPlanTratamiento;
	private Usuarios usuarios;
	private HallazgosOdontologicos hallazgosOdontologicos;
	private ConvencionesOdontologicas convencionesOdontologicas;
	private Especialidades especialidades;
	private ValoracionesOdonto valoracionesOdonto;
	private Integer superficie;
	private String clasificacion;
	private String porConfirmar;
	private Date fechaModifica;
	private String horaModifica;
	private Long evolucion;
	private String activo;

	public LogDetPlanTratamiento() {
	}

	public LogDetPlanTratamiento(long codigoPk,
			DetPlanTratamiento detPlanTratamiento, Usuarios usuarios,
			HallazgosOdontologicos hallazgosOdontologicos, String porConfirmar,
			Date fechaModifica, String horaModifica, String activo) {
		this.codigoPk = codigoPk;
		this.detPlanTratamiento = detPlanTratamiento;
		this.usuarios = usuarios;
		this.hallazgosOdontologicos = hallazgosOdontologicos;
		this.porConfirmar = porConfirmar;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.activo = activo;
	}

	public LogDetPlanTratamiento(long codigoPk, PiezaDental piezaDental,
			CitasOdontologicas citasOdontologicas,
			DetPlanTratamiento detPlanTratamiento, Usuarios usuarios,
			HallazgosOdontologicos hallazgosOdontologicos,
			ConvencionesOdontologicas convencionesOdontologicas,
			Especialidades especialidades,
			ValoracionesOdonto valoracionesOdonto, Integer superficie,
			String clasificacion, String porConfirmar, Date fechaModifica,
			String horaModifica, Long evolucion, String activo) {
		this.codigoPk = codigoPk;
		this.piezaDental = piezaDental;
		this.citasOdontologicas = citasOdontologicas;
		this.detPlanTratamiento = detPlanTratamiento;
		this.usuarios = usuarios;
		this.hallazgosOdontologicos = hallazgosOdontologicos;
		this.convencionesOdontologicas = convencionesOdontologicas;
		this.especialidades = especialidades;
		this.valoracionesOdonto = valoracionesOdonto;
		this.superficie = superficie;
		this.clasificacion = clasificacion;
		this.porConfirmar = porConfirmar;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.evolucion = evolucion;
		this.activo = activo;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public PiezaDental getPiezaDental() {
		return this.piezaDental;
	}

	public void setPiezaDental(PiezaDental piezaDental) {
		this.piezaDental = piezaDental;
	}

	public CitasOdontologicas getCitasOdontologicas() {
		return this.citasOdontologicas;
	}

	public void setCitasOdontologicas(CitasOdontologicas citasOdontologicas) {
		this.citasOdontologicas = citasOdontologicas;
	}

	public DetPlanTratamiento getDetPlanTratamiento() {
		return this.detPlanTratamiento;
	}

	public void setDetPlanTratamiento(DetPlanTratamiento detPlanTratamiento) {
		this.detPlanTratamiento = detPlanTratamiento;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public HallazgosOdontologicos getHallazgosOdontologicos() {
		return this.hallazgosOdontologicos;
	}

	public void setHallazgosOdontologicos(
			HallazgosOdontologicos hallazgosOdontologicos) {
		this.hallazgosOdontologicos = hallazgosOdontologicos;
	}

	public ConvencionesOdontologicas getConvencionesOdontologicas() {
		return this.convencionesOdontologicas;
	}

	public void setConvencionesOdontologicas(
			ConvencionesOdontologicas convencionesOdontologicas) {
		this.convencionesOdontologicas = convencionesOdontologicas;
	}

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public ValoracionesOdonto getValoracionesOdonto() {
		return this.valoracionesOdonto;
	}

	public void setValoracionesOdonto(ValoracionesOdonto valoracionesOdonto) {
		this.valoracionesOdonto = valoracionesOdonto;
	}

	public Integer getSuperficie() {
		return this.superficie;
	}

	public void setSuperficie(Integer superficie) {
		this.superficie = superficie;
	}

	public String getClasificacion() {
		return this.clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getPorConfirmar() {
		return this.porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
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

	public Long getEvolucion() {
		return this.evolucion;
	}

	public void setEvolucion(Long evolucion) {
		this.evolucion = evolucion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

}
