package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * HallazgosVsProgSer generated by hbm2java
 */
public class HallazgosVsProgSer implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private HallazgosOdontologicos hallazgosOdontologicos;
	private Instituciones instituciones;
	private Date fechaModifica;
	private String horaModifica;
	private Set detHallProgSers = new HashSet(0);

	public HallazgosVsProgSer() {
	}

	public HallazgosVsProgSer(long codigo, Usuarios usuarios,
			HallazgosOdontologicos hallazgosOdontologicos,
			Instituciones instituciones, Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.hallazgosOdontologicos = hallazgosOdontologicos;
		this.instituciones = instituciones;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public HallazgosVsProgSer(long codigo, Usuarios usuarios,
			HallazgosOdontologicos hallazgosOdontologicos,
			Instituciones instituciones, Date fechaModifica,
			String horaModifica, Set detHallProgSers) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.hallazgosOdontologicos = hallazgosOdontologicos;
		this.instituciones = instituciones;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.detHallProgSers = detHallProgSers;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
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

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
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

	public Set getDetHallProgSers() {
		return this.detHallProgSers;
	}

	public void setDetHallProgSers(Set detHallProgSers) {
		this.detHallProgSers = detHallProgSers;
	}

}
