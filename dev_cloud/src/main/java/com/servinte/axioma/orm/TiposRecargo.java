package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TiposRecargo generated by hbm2java
 */
public class TiposRecargo implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set recargosTarifases = new HashSet(0);
	private Set cargosDirectoses = new HashSet(0);

	public TiposRecargo() {
	}

	public TiposRecargo(int codigo) {
		this.codigo = codigo;
	}

	public TiposRecargo(int codigo, String nombre, Set recargosTarifases,
			Set cargosDirectoses) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.recargosTarifases = recargosTarifases;
		this.cargosDirectoses = cargosDirectoses;
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

	public Set getRecargosTarifases() {
		return this.recargosTarifases;
	}

	public void setRecargosTarifases(Set recargosTarifases) {
		this.recargosTarifases = recargosTarifases;
	}

	public Set getCargosDirectoses() {
		return this.cargosDirectoses;
	}

	public void setCargosDirectoses(Set cargosDirectoses) {
		this.cargosDirectoses = cargosDirectoses;
	}

}
