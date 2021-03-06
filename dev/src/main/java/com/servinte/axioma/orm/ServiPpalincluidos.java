package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ServiPpalincluidos generated by hbm2java
 */
public class ServiPpalincluidos implements java.io.Serializable {

	private int codigo;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private Servicios servicios;
	private char activo;
	private Date fechaModifica;
	private String horaModifica;
	private Set artIncluidosServippals = new HashSet(0);
	private Set serviIncluidoServippals = new HashSet(0);

	public ServiPpalincluidos() {
	}

	public ServiPpalincluidos(int codigo, Usuarios usuarios,
			Instituciones instituciones, Servicios servicios, char activo,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.servicios = servicios;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public ServiPpalincluidos(int codigo, Usuarios usuarios,
			Instituciones instituciones, Servicios servicios, char activo,
			Date fechaModifica, String horaModifica,
			Set artIncluidosServippals, Set serviIncluidoServippals) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.servicios = servicios;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.artIncluidosServippals = artIncluidosServippals;
		this.serviIncluidoServippals = serviIncluidoServippals;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
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

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public char getActivo() {
		return this.activo;
	}

	public void setActivo(char activo) {
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

	public Set getArtIncluidosServippals() {
		return this.artIncluidosServippals;
	}

	public void setArtIncluidosServippals(Set artIncluidosServippals) {
		this.artIncluidosServippals = artIncluidosServippals;
	}

	public Set getServiIncluidoServippals() {
		return this.serviIncluidoServippals;
	}

	public void setServiIncluidoServippals(Set serviIncluidoServippals) {
		this.serviIncluidoServippals = serviIncluidoServippals;
	}

}
