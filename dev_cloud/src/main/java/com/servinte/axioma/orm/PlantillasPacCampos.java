package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PlantillasPacCampos generated by hbm2java
 */
public class PlantillasPacCampos implements java.io.Serializable {

	private long codigoPk;
	private Usuarios usuarios;
	private PlantillasPacientes plantillasPacientes;
	private int plantillaCampoSec;
	private String nombreArchivoOriginal;
	private Date fechaModificacion;
	private String horaModificacion;
	private Set valoresPlanPacCampos = new HashSet(0);

	public PlantillasPacCampos() {
	}

	public PlantillasPacCampos(long codigoPk,
			PlantillasPacientes plantillasPacientes, int plantillaCampoSec,
			Date fechaModificacion) {
		this.codigoPk = codigoPk;
		this.plantillasPacientes = plantillasPacientes;
		this.plantillaCampoSec = plantillaCampoSec;
		this.fechaModificacion = fechaModificacion;
	}

	public PlantillasPacCampos(long codigoPk, Usuarios usuarios,
			PlantillasPacientes plantillasPacientes, int plantillaCampoSec,
			String nombreArchivoOriginal, Date fechaModificacion,
			String horaModificacion, Set valoresPlanPacCampos) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.plantillasPacientes = plantillasPacientes;
		this.plantillaCampoSec = plantillaCampoSec;
		this.nombreArchivoOriginal = nombreArchivoOriginal;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
		this.valoresPlanPacCampos = valoresPlanPacCampos;
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

	public PlantillasPacientes getPlantillasPacientes() {
		return this.plantillasPacientes;
	}

	public void setPlantillasPacientes(PlantillasPacientes plantillasPacientes) {
		this.plantillasPacientes = plantillasPacientes;
	}

	public int getPlantillaCampoSec() {
		return this.plantillaCampoSec;
	}

	public void setPlantillaCampoSec(int plantillaCampoSec) {
		this.plantillaCampoSec = plantillaCampoSec;
	}

	public String getNombreArchivoOriginal() {
		return this.nombreArchivoOriginal;
	}

	public void setNombreArchivoOriginal(String nombreArchivoOriginal) {
		this.nombreArchivoOriginal = nombreArchivoOriginal;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getHoraModificacion() {
		return this.horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public Set getValoresPlanPacCampos() {
		return this.valoresPlanPacCampos;
	}

	public void setValoresPlanPacCampos(Set valoresPlanPacCampos) {
		this.valoresPlanPacCampos = valoresPlanPacCampos;
	}

}
