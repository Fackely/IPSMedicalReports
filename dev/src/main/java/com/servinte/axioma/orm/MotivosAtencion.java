package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MotivosAtencion generated by hbm2java
 */
public class MotivosAtencion implements java.io.Serializable {

	private int codigoPk;
	private TiposMotivoAtencion tiposMotivoAtencion;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String codigo;
	private String nombre;
	private Date fechaModifica;
	private String horaModifica;
	private Set logPlanTratamientos = new HashSet(0);
	private Set logProgramasServiciosPlanTs = new HashSet(0);
	private Set logPresupuestoOdos = new HashSet(0);
	private Set planTratamientos = new HashSet(0);
	private Set hisConfProgServPlanTs = new HashSet(0);
	private Set programasServiciosPlanTs = new HashSet(0);
	private Set hisConfPlanTratamientos = new HashSet(0);

	public MotivosAtencion() {
	}

	public MotivosAtencion(int codigoPk,
			TiposMotivoAtencion tiposMotivoAtencion,
			Instituciones instituciones, String codigo, String nombre) {
		this.codigoPk = codigoPk;
		this.tiposMotivoAtencion = tiposMotivoAtencion;
		this.instituciones = instituciones;
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public MotivosAtencion(int codigoPk,
			TiposMotivoAtencion tiposMotivoAtencion, Usuarios usuarios,
			Instituciones instituciones, String codigo, String nombre,
			Date fechaModifica, String horaModifica, Set logPlanTratamientos,
			Set logProgramasServiciosPlanTs, Set logPresupuestoOdos,
			Set planTratamientos, Set hisConfProgServPlanTs,
			Set programasServiciosPlanTs, Set hisConfPlanTratamientos) {
		this.codigoPk = codigoPk;
		this.tiposMotivoAtencion = tiposMotivoAtencion;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.codigo = codigo;
		this.nombre = nombre;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.logPlanTratamientos = logPlanTratamientos;
		this.logProgramasServiciosPlanTs = logProgramasServiciosPlanTs;
		this.logPresupuestoOdos = logPresupuestoOdos;
		this.planTratamientos = planTratamientos;
		this.hisConfProgServPlanTs = hisConfProgServPlanTs;
		this.programasServiciosPlanTs = programasServiciosPlanTs;
		this.hisConfPlanTratamientos = hisConfPlanTratamientos;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public TiposMotivoAtencion getTiposMotivoAtencion() {
		return this.tiposMotivoAtencion;
	}

	public void setTiposMotivoAtencion(TiposMotivoAtencion tiposMotivoAtencion) {
		this.tiposMotivoAtencion = tiposMotivoAtencion;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public Set getLogPlanTratamientos() {
		return this.logPlanTratamientos;
	}

	public void setLogPlanTratamientos(Set logPlanTratamientos) {
		this.logPlanTratamientos = logPlanTratamientos;
	}

	public Set getLogProgramasServiciosPlanTs() {
		return this.logProgramasServiciosPlanTs;
	}

	public void setLogProgramasServiciosPlanTs(Set logProgramasServiciosPlanTs) {
		this.logProgramasServiciosPlanTs = logProgramasServiciosPlanTs;
	}

	public Set getLogPresupuestoOdos() {
		return this.logPresupuestoOdos;
	}

	public void setLogPresupuestoOdos(Set logPresupuestoOdos) {
		this.logPresupuestoOdos = logPresupuestoOdos;
	}

	public Set getPlanTratamientos() {
		return this.planTratamientos;
	}

	public void setPlanTratamientos(Set planTratamientos) {
		this.planTratamientos = planTratamientos;
	}

	public Set getHisConfProgServPlanTs() {
		return this.hisConfProgServPlanTs;
	}

	public void setHisConfProgServPlanTs(Set hisConfProgServPlanTs) {
		this.hisConfProgServPlanTs = hisConfProgServPlanTs;
	}

	public Set getProgramasServiciosPlanTs() {
		return this.programasServiciosPlanTs;
	}

	public void setProgramasServiciosPlanTs(Set programasServiciosPlanTs) {
		this.programasServiciosPlanTs = programasServiciosPlanTs;
	}

	public Set getHisConfPlanTratamientos() {
		return this.hisConfPlanTratamientos;
	}

	public void setHisConfPlanTratamientos(Set hisConfPlanTratamientos) {
		this.hisConfPlanTratamientos = hisConfPlanTratamientos;
	}

}
