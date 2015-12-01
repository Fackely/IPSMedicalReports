package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * HisConfProgServPlanT generated by hbm2java
 */
public class HisConfProgServPlanT implements java.io.Serializable {

	private long codigoPk;
	private EvolucionesOdo evolucionesOdo;
	private CitasOdontologicas citasOdontologicas;
	private DetPlanTratamiento detPlanTratamiento;
	private Usuarios usuarios;
	private Servicios servicios;
	private ProgramasServiciosPlanT programasServiciosPlanT;
	private ConvencionesOdontologicas convencionesOdontologicas;
	private Especialidades especialidades;
	private ValoracionesOdonto valoracionesOdonto;
	private Programas programas;
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
	private int ordenServicio;
	private String activo;
	private String estadoAutorizacion;

	public HisConfProgServPlanT() {
	}

	public HisConfProgServPlanT(long codigoPk,
			DetPlanTratamiento detPlanTratamiento, Usuarios usuarios,
			Servicios servicios,
			ProgramasServiciosPlanT programasServiciosPlanT,
			String estadoServicio, String indicativoPrograma,
			String indicativoServicio, String porConfirmar, Date fechaModifica,
			String horaModifica, int ordenServicio, String activo) {
		this.codigoPk = codigoPk;
		this.detPlanTratamiento = detPlanTratamiento;
		this.usuarios = usuarios;
		this.servicios = servicios;
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

	public HisConfProgServPlanT(long codigoPk, EvolucionesOdo evolucionesOdo,
			CitasOdontologicas citasOdontologicas,
			DetPlanTratamiento detPlanTratamiento, Usuarios usuarios,
			Servicios servicios,
			ProgramasServiciosPlanT programasServiciosPlanT,
			ConvencionesOdontologicas convencionesOdontologicas,
			Especialidades especialidades,
			ValoracionesOdonto valoracionesOdonto, Programas programas,
			MotivosAtencion motivosAtencion, String estadoPrograma,
			Character inclusion, Character exclusion, Character garantia,
			String estadoServicio, String indicativoPrograma,
			String indicativoServicio, String porConfirmar, Date fechaModifica,
			String horaModifica, int ordenServicio, String activo,
			String estadoAutorizacion) {
		this.codigoPk = codigoPk;
		this.evolucionesOdo = evolucionesOdo;
		this.citasOdontologicas = citasOdontologicas;
		this.detPlanTratamiento = detPlanTratamiento;
		this.usuarios = usuarios;
		this.servicios = servicios;
		this.programasServiciosPlanT = programasServiciosPlanT;
		this.convencionesOdontologicas = convencionesOdontologicas;
		this.especialidades = especialidades;
		this.valoracionesOdonto = valoracionesOdonto;
		this.programas = programas;
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
		this.ordenServicio = ordenServicio;
		this.activo = activo;
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public EvolucionesOdo getEvolucionesOdo() {
		return this.evolucionesOdo;
	}

	public void setEvolucionesOdo(EvolucionesOdo evolucionesOdo) {
		this.evolucionesOdo = evolucionesOdo;
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

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
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

	public Programas getProgramas() {
		return this.programas;
	}

	public void setProgramas(Programas programas) {
		this.programas = programas;
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

	public String getEstadoAutorizacion() {
		return this.estadoAutorizacion;
	}

	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

}
