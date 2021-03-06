package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * LogProgramasServiciosPlanT generated by hbm2java
 */
public class LogProgramasServiciosPlanT implements java.io.Serializable {

	private long codigoPk;
	private Usuarios usuarios;
	private ProgramasServiciosPlanT programasServiciosPlanT;
	private ConvencionesOdontologicas convencionesOdontologicas;
	private Especialidades especialidades;
	private ValoracionesOdonto valoracionesOdonto;
	private MotivosAtencion motivosAtencion;
	private String estadoPrograma;
	private Character inclusion;
	private Character exclusion;
	private Character garantia;
	private String estadoServicio;
	private String indicativoPrograma;
	private String indicativoServicio;
	private String porConfirmar;
	private Date fechaModifica;
	private String horaModifica;
	private Long evolucion;
	private int ordenServicio;
	private String activo;
	private Long cita;
	private String estadoAutorizacion;

	public LogProgramasServiciosPlanT() {
	}

	public LogProgramasServiciosPlanT(long codigoPk, Usuarios usuarios,
			ProgramasServiciosPlanT programasServiciosPlanT,
			String estadoServicio, String indicativoPrograma,
			String indicativoServicio, String porConfirmar, Date fechaModifica,
			String horaModifica, int ordenServicio, String activo) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.programasServiciosPlanT = programasServiciosPlanT;
		this.estadoServicio = estadoServicio;
		this.indicativoPrograma = indicativoPrograma;
		this.indicativoServicio = indicativoServicio;
		this.porConfirmar = porConfirmar;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.ordenServicio = ordenServicio;
		this.activo = activo;
	}

	public LogProgramasServiciosPlanT(long codigoPk, Usuarios usuarios,
			ProgramasServiciosPlanT programasServiciosPlanT,
			ConvencionesOdontologicas convencionesOdontologicas,
			Especialidades especialidades,
			ValoracionesOdonto valoracionesOdonto,
			MotivosAtencion motivosAtencion, String estadoPrograma,
			Character inclusion, Character exclusion, Character garantia,
			String estadoServicio, String indicativoPrograma,
			String indicativoServicio, String porConfirmar, Date fechaModifica,
			String horaModifica, Long evolucion, int ordenServicio,
			String activo, Long cita, String estadoAutorizacion) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.programasServiciosPlanT = programasServiciosPlanT;
		this.convencionesOdontologicas = convencionesOdontologicas;
		this.especialidades = especialidades;
		this.valoracionesOdonto = valoracionesOdonto;
		this.motivosAtencion = motivosAtencion;
		this.estadoPrograma = estadoPrograma;
		this.inclusion = inclusion;
		this.exclusion = exclusion;
		this.garantia = garantia;
		this.estadoServicio = estadoServicio;
		this.indicativoPrograma = indicativoPrograma;
		this.indicativoServicio = indicativoServicio;
		this.porConfirmar = porConfirmar;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.evolucion = evolucion;
		this.ordenServicio = ordenServicio;
		this.activo = activo;
		this.cita = cita;
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public ProgramasServiciosPlanT getProgramasServiciosPlanT() {
		return this.programasServiciosPlanT;
	}

	public void setProgramasServiciosPlanT(
			ProgramasServiciosPlanT programasServiciosPlanT) {
		this.programasServiciosPlanT = programasServiciosPlanT;
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

	public MotivosAtencion getMotivosAtencion() {
		return this.motivosAtencion;
	}

	public void setMotivosAtencion(MotivosAtencion motivosAtencion) {
		this.motivosAtencion = motivosAtencion;
	}

	public String getEstadoPrograma() {
		return this.estadoPrograma;
	}

	public void setEstadoPrograma(String estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}

	public Character getInclusion() {
		return this.inclusion;
	}

	public void setInclusion(Character inclusion) {
		this.inclusion = inclusion;
	}

	public Character getExclusion() {
		return this.exclusion;
	}

	public void setExclusion(Character exclusion) {
		this.exclusion = exclusion;
	}

	public Character getGarantia() {
		return this.garantia;
	}

	public void setGarantia(Character garantia) {
		this.garantia = garantia;
	}

	public String getEstadoServicio() {
		return this.estadoServicio;
	}

	public void setEstadoServicio(String estadoServicio) {
		this.estadoServicio = estadoServicio;
	}

	public String getIndicativoPrograma() {
		return this.indicativoPrograma;
	}

	public void setIndicativoPrograma(String indicativoPrograma) {
		this.indicativoPrograma = indicativoPrograma;
	}

	public String getIndicativoServicio() {
		return this.indicativoServicio;
	}

	public void setIndicativoServicio(String indicativoServicio) {
		this.indicativoServicio = indicativoServicio;
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

	public int getOrdenServicio() {
		return this.ordenServicio;
	}

	public void setOrdenServicio(int ordenServicio) {
		this.ordenServicio = ordenServicio;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Long getCita() {
		return this.cita;
	}

	public void setCita(Long cita) {
		this.cita = cita;
	}

	public String getEstadoAutorizacion() {
		return this.estadoAutorizacion;
	}

	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

}
