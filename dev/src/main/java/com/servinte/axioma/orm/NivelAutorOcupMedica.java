package com.servinte.axioma.orm;

// Generated Sep 24, 2010 5:55:45 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * NivelAutorOcupMedica generated by hbm2java
 */
public class NivelAutorOcupMedica implements java.io.Serializable {

	private int codigoPk;
	private OcupacionesMedicas ocupacionesMedicas;
	private NivelAutorUsuario nivelAutorUsuario;
	private Usuarios usuarios;
	private Date fechaRegistro;
	private String horaRegistro;
	private Set prioridadOcupMedicas = new HashSet(0);

	public NivelAutorOcupMedica() {
	}

	public NivelAutorOcupMedica(int codigoPk,
			OcupacionesMedicas ocupacionesMedicas,
			NivelAutorUsuario nivelAutorUsuario, Usuarios usuarios,
			Date fechaRegistro, String horaRegistro) {
		this.codigoPk = codigoPk;
		this.ocupacionesMedicas = ocupacionesMedicas;
		this.nivelAutorUsuario = nivelAutorUsuario;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
	}

	public NivelAutorOcupMedica(int codigoPk,
			OcupacionesMedicas ocupacionesMedicas,
			NivelAutorUsuario nivelAutorUsuario, Usuarios usuarios,
			Date fechaRegistro, String horaRegistro, Set prioridadOcupMedicas) {
		this.codigoPk = codigoPk;
		this.ocupacionesMedicas = ocupacionesMedicas;
		this.nivelAutorUsuario = nivelAutorUsuario;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.prioridadOcupMedicas = prioridadOcupMedicas;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public OcupacionesMedicas getOcupacionesMedicas() {
		return this.ocupacionesMedicas;
	}

	public void setOcupacionesMedicas(OcupacionesMedicas ocupacionesMedicas) {
		this.ocupacionesMedicas = ocupacionesMedicas;
	}

	public NivelAutorUsuario getNivelAutorUsuario() {
		return this.nivelAutorUsuario;
	}

	public void setNivelAutorUsuario(NivelAutorUsuario nivelAutorUsuario) {
		this.nivelAutorUsuario = nivelAutorUsuario;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getHoraRegistro() {
		return this.horaRegistro;
	}

	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	public Set getPrioridadOcupMedicas() {
		return this.prioridadOcupMedicas;
	}

	public void setPrioridadOcupMedicas(Set prioridadOcupMedicas) {
		this.prioridadOcupMedicas = prioridadOcupMedicas;
	}

}
