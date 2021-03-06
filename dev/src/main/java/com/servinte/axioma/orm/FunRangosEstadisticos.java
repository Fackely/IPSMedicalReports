package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * FunRangosEstadisticos generated by hbm2java
 */
public class FunRangosEstadisticos implements java.io.Serializable {

	private int codigo;
	private Modulos modulos;
	private String nombre;
	private String parametrizable;
	private Set reportesRangosEstadisticoses = new HashSet(0);

	public FunRangosEstadisticos() {
	}

	public FunRangosEstadisticos(int codigo, Modulos modulos, String nombre,
			String parametrizable) {
		this.codigo = codigo;
		this.modulos = modulos;
		this.nombre = nombre;
		this.parametrizable = parametrizable;
	}

	public FunRangosEstadisticos(int codigo, Modulos modulos, String nombre,
			String parametrizable, Set reportesRangosEstadisticoses) {
		this.codigo = codigo;
		this.modulos = modulos;
		this.nombre = nombre;
		this.parametrizable = parametrizable;
		this.reportesRangosEstadisticoses = reportesRangosEstadisticoses;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Modulos getModulos() {
		return this.modulos;
	}

	public void setModulos(Modulos modulos) {
		this.modulos = modulos;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getParametrizable() {
		return this.parametrizable;
	}

	public void setParametrizable(String parametrizable) {
		this.parametrizable = parametrizable;
	}

	public Set getReportesRangosEstadisticoses() {
		return this.reportesRangosEstadisticoses;
	}

	public void setReportesRangosEstadisticoses(Set reportesRangosEstadisticoses) {
		this.reportesRangosEstadisticoses = reportesRangosEstadisticoses;
	}

}
