package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * CargosUsuarios generated by hbm2java
 */
public class CargosUsuarios implements java.io.Serializable {

	private int codigo;
	private Instituciones instituciones;
	private String nombre;
	private String activo;
	private String profesionalsalud;
	private Set respAutorizacionesesForCargoPersRecibe = new HashSet(0);
	private Set respAutorizacionesesForCargoPersRegistro = new HashSet(0);
	private Set usuarioses = new HashSet(0);

	public CargosUsuarios() {
	}

	public CargosUsuarios(int codigo, Instituciones instituciones,
			String nombre, String activo, String profesionalsalud) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.nombre = nombre;
		this.activo = activo;
		this.profesionalsalud = profesionalsalud;
	}

	public CargosUsuarios(int codigo, Instituciones instituciones,
			String nombre, String activo, String profesionalsalud,
			Set respAutorizacionesesForCargoPersRecibe,
			Set respAutorizacionesesForCargoPersRegistro, Set usuarioses) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.nombre = nombre;
		this.activo = activo;
		this.profesionalsalud = profesionalsalud;
		this.respAutorizacionesesForCargoPersRecibe = respAutorizacionesesForCargoPersRecibe;
		this.respAutorizacionesesForCargoPersRegistro = respAutorizacionesesForCargoPersRegistro;
		this.usuarioses = usuarioses;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getProfesionalsalud() {
		return this.profesionalsalud;
	}

	public void setProfesionalsalud(String profesionalsalud) {
		this.profesionalsalud = profesionalsalud;
	}

	public Set getRespAutorizacionesesForCargoPersRecibe() {
		return this.respAutorizacionesesForCargoPersRecibe;
	}

	public void setRespAutorizacionesesForCargoPersRecibe(
			Set respAutorizacionesesForCargoPersRecibe) {
		this.respAutorizacionesesForCargoPersRecibe = respAutorizacionesesForCargoPersRecibe;
	}

	public Set getRespAutorizacionesesForCargoPersRegistro() {
		return this.respAutorizacionesesForCargoPersRegistro;
	}

	public void setRespAutorizacionesesForCargoPersRegistro(
			Set respAutorizacionesesForCargoPersRegistro) {
		this.respAutorizacionesesForCargoPersRegistro = respAutorizacionesesForCargoPersRegistro;
	}

	public Set getUsuarioses() {
		return this.usuarioses;
	}

	public void setUsuarioses(Set usuarioses) {
		this.usuarioses = usuarioses;
	}

}
