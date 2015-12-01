package com.servinte.axioma.orm;

// Generated 11/06/2011 11:20:57 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Localidades generated by hbm2java
 */
public class Localidades implements java.io.Serializable {

	private LocalidadesId id;
	private Departamentos departamentos;
	private Usuarios usuarios;
	private Paises paises;
	private Ciudades ciudades;
	private String descripcion;
	private Date fechaModifica;
	private String horaModifica;
	private Set barrioses = new HashSet(0);

	public Localidades() {
	}

	public Localidades(LocalidadesId id, Departamentos departamentos,
			Usuarios usuarios, Paises paises, Ciudades ciudades,
			String descripcion, Date fechaModifica, String horaModifica) {
		this.id = id;
		this.departamentos = departamentos;
		this.usuarios = usuarios;
		this.paises = paises;
		this.ciudades = ciudades;
		this.descripcion = descripcion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public Localidades(LocalidadesId id, Departamentos departamentos,
			Usuarios usuarios, Paises paises, Ciudades ciudades,
			String descripcion, Date fechaModifica, String horaModifica,
			Set barrioses) {
		this.id = id;
		this.departamentos = departamentos;
		this.usuarios = usuarios;
		this.paises = paises;
		this.ciudades = ciudades;
		this.descripcion = descripcion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.barrioses = barrioses;
	}

	public LocalidadesId getId() {
		return this.id;
	}

	public void setId(LocalidadesId id) {
		this.id = id;
	}

	public Departamentos getDepartamentos() {
		return this.departamentos;
	}

	public void setDepartamentos(Departamentos departamentos) {
		this.departamentos = departamentos;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Paises getPaises() {
		return this.paises;
	}

	public void setPaises(Paises paises) {
		this.paises = paises;
	}

	public Ciudades getCiudades() {
		return this.ciudades;
	}

	public void setCiudades(Ciudades ciudades) {
		this.ciudades = ciudades;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public Set getBarrioses() {
		return this.barrioses;
	}

	public void setBarrioses(Set barrioses) {
		this.barrioses = barrioses;
	}

}
