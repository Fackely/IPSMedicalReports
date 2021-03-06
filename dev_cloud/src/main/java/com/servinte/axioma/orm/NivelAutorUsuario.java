package com.servinte.axioma.orm;

// Generated Sep 23, 2010 2:39:33 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * NivelAutorUsuario generated by hbm2java
 */
public class NivelAutorUsuario implements java.io.Serializable {

	private int codigoPk;
	private NivelAutorizacion nivelAutorizacion;
	private Usuarios usuarios;
	private Date fechaRegistro;
	private String horaRegistro;
	private Set nivelAutorUsuEspecs = new HashSet(0);
	private Set nivelAutorOcupMedicas = new HashSet(0);

	public NivelAutorUsuario() {
	}

	public NivelAutorUsuario(int codigoPk, NivelAutorizacion nivelAutorizacion,
			Usuarios usuarios, Date fechaRegistro, String horaRegistro) {
		this.codigoPk = codigoPk;
		this.nivelAutorizacion = nivelAutorizacion;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
	}

	public NivelAutorUsuario(int codigoPk, NivelAutorizacion nivelAutorizacion,
			Usuarios usuarios, Date fechaRegistro, String horaRegistro,
			Set nivelAutorUsuEspecs, Set nivelAutorOcupMedicas) {
		this.codigoPk = codigoPk;
		this.nivelAutorizacion = nivelAutorizacion;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.nivelAutorUsuEspecs = nivelAutorUsuEspecs;
		this.nivelAutorOcupMedicas = nivelAutorOcupMedicas;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public NivelAutorizacion getNivelAutorizacion() {
		return this.nivelAutorizacion;
	}

	public void setNivelAutorizacion(NivelAutorizacion nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
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

	public Set getNivelAutorUsuEspecs() {
		return this.nivelAutorUsuEspecs;
	}

	public void setNivelAutorUsuEspecs(Set nivelAutorUsuEspecs) {
		this.nivelAutorUsuEspecs = nivelAutorUsuEspecs;
	}

	public Set getNivelAutorOcupMedicas() {
		return this.nivelAutorOcupMedicas;
	}

	public void setNivelAutorOcupMedicas(Set nivelAutorOcupMedicas) {
		this.nivelAutorOcupMedicas = nivelAutorOcupMedicas;
	}

}
