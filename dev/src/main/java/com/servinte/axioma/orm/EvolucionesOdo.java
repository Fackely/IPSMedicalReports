package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EvolucionesOdo generated by hbm2java
 */
public class EvolucionesOdo implements java.io.Serializable {

	private long codigoPk;
	private CitasOdontologicas citasOdontologicas;
	private Usuarios usuarios;
	private String horaModifica;
	private Date fechaModifica;
	private Set odontogramas = new HashSet(0);
	private Set hisConfProgServPlanTs = new HashSet(0);
	private Set hisConfPlanTratamientos = new HashSet(0);
	private Set hisConfDetPlanTs = new HashSet(0);

	public EvolucionesOdo() {
	}

	public EvolucionesOdo(long codigoPk, CitasOdontologicas citasOdontologicas) {
		this.codigoPk = codigoPk;
		this.citasOdontologicas = citasOdontologicas;
	}

	public EvolucionesOdo(long codigoPk, CitasOdontologicas citasOdontologicas,
			Usuarios usuarios, String horaModifica, Date fechaModifica,
			Set odontogramas, Set hisConfProgServPlanTs,
			Set hisConfPlanTratamientos, Set hisConfDetPlanTs) {
		this.codigoPk = codigoPk;
		this.citasOdontologicas = citasOdontologicas;
		this.usuarios = usuarios;
		this.horaModifica = horaModifica;
		this.fechaModifica = fechaModifica;
		this.odontogramas = odontogramas;
		this.hisConfProgServPlanTs = hisConfProgServPlanTs;
		this.hisConfPlanTratamientos = hisConfPlanTratamientos;
		this.hisConfDetPlanTs = hisConfDetPlanTs;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public CitasOdontologicas getCitasOdontologicas() {
		return this.citasOdontologicas;
	}

	public void setCitasOdontologicas(CitasOdontologicas citasOdontologicas) {
		this.citasOdontologicas = citasOdontologicas;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public Set getOdontogramas() {
		return this.odontogramas;
	}

	public void setOdontogramas(Set odontogramas) {
		this.odontogramas = odontogramas;
	}

	public Set getHisConfProgServPlanTs() {
		return this.hisConfProgServPlanTs;
	}

	public void setHisConfProgServPlanTs(Set hisConfProgServPlanTs) {
		this.hisConfProgServPlanTs = hisConfProgServPlanTs;
	}

	public Set getHisConfPlanTratamientos() {
		return this.hisConfPlanTratamientos;
	}

	public void setHisConfPlanTratamientos(Set hisConfPlanTratamientos) {
		this.hisConfPlanTratamientos = hisConfPlanTratamientos;
	}

	public Set getHisConfDetPlanTs() {
		return this.hisConfDetPlanTs;
	}

	public void setHisConfDetPlanTs(Set hisConfDetPlanTs) {
		this.hisConfDetPlanTs = hisConfDetPlanTs;
	}

}
