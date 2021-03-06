package com.servinte.axioma.orm;

// Generated Feb 18, 2011 11:00:55 AM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * TarifariosOficiales generated by hbm2java
 */
public class TarifariosOficiales implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private char tarifarios;
	private Set logRipsEntidadesSubs = new HashSet(0);
	private Set logLecturaPlanosEnts = new HashSet(0);
	private Set logFuripses = new HashSet(0);
	private Set esquemasTarifarioses = new HashSet(0);
	private Set convenioses = new HashSet(0);
	private Set formatoImpresionFacturas = new HashSet(0);
	private Set referenciasServicios = new HashSet(0);

	public TarifariosOficiales() {
	}

	public TarifariosOficiales(int codigo, char tarifarios) {
		this.codigo = codigo;
		this.tarifarios = tarifarios;
	}

	public TarifariosOficiales(int codigo, String nombre, char tarifarios,
			Set logRipsEntidadesSubs, Set logLecturaPlanosEnts,
			Set logFuripses, Set esquemasTarifarioses, Set convenioses,
			Set formatoImpresionFacturas, Set referenciasServicios) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.tarifarios = tarifarios;
		this.logRipsEntidadesSubs = logRipsEntidadesSubs;
		this.logLecturaPlanosEnts = logLecturaPlanosEnts;
		this.logFuripses = logFuripses;
		this.esquemasTarifarioses = esquemasTarifarioses;
		this.convenioses = convenioses;
		this.formatoImpresionFacturas = formatoImpresionFacturas;
		this.referenciasServicios = referenciasServicios;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public char getTarifarios() {
		return this.tarifarios;
	}

	public void setTarifarios(char tarifarios) {
		this.tarifarios = tarifarios;
	}

	public Set getLogRipsEntidadesSubs() {
		return this.logRipsEntidadesSubs;
	}

	public void setLogRipsEntidadesSubs(Set logRipsEntidadesSubs) {
		this.logRipsEntidadesSubs = logRipsEntidadesSubs;
	}

	public Set getLogLecturaPlanosEnts() {
		return this.logLecturaPlanosEnts;
	}

	public void setLogLecturaPlanosEnts(Set logLecturaPlanosEnts) {
		this.logLecturaPlanosEnts = logLecturaPlanosEnts;
	}

	public Set getLogFuripses() {
		return this.logFuripses;
	}

	public void setLogFuripses(Set logFuripses) {
		this.logFuripses = logFuripses;
	}

	public Set getEsquemasTarifarioses() {
		return this.esquemasTarifarioses;
	}

	public void setEsquemasTarifarioses(Set esquemasTarifarioses) {
		this.esquemasTarifarioses = esquemasTarifarioses;
	}

	public Set getConvenioses() {
		return this.convenioses;
	}

	public void setConvenioses(Set convenioses) {
		this.convenioses = convenioses;
	}

	public Set getFormatoImpresionFacturas() {
		return this.formatoImpresionFacturas;
	}

	public void setFormatoImpresionFacturas(Set formatoImpresionFacturas) {
		this.formatoImpresionFacturas = formatoImpresionFacturas;
	}

	public Set getReferenciasServicios() {
		return this.referenciasServicios;
	}

	public void setReferenciasServicios(Set referenciasServicios) {
		this.referenciasServicios = referenciasServicios;
	}

}
