package com.servinte.axioma.orm;

// Generated 15/06/2011 05:09:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * InconsistenSubirPaciente generated by hbm2java
 */
public class InconsistenSubirPaciente implements java.io.Serializable {

	private long codigoPk;
	private LogSubirPacientes logSubirPacientes;
	private String tipoInconsistencia;
	private String descripcion;
	private Set capitadoInconsistencias = new HashSet(0);
	private Set inconsistenciasCamposes = new HashSet(0);

	public InconsistenSubirPaciente() {
	}

	public InconsistenSubirPaciente(long codigoPk,
			LogSubirPacientes logSubirPacientes) {
		this.codigoPk = codigoPk;
		this.logSubirPacientes = logSubirPacientes;
	}

	public InconsistenSubirPaciente(long codigoPk,
			LogSubirPacientes logSubirPacientes, String tipoInconsistencia,
			String descripcion, Set capitadoInconsistencias,
			Set inconsistenciasCamposes) {
		this.codigoPk = codigoPk;
		this.logSubirPacientes = logSubirPacientes;
		this.tipoInconsistencia = tipoInconsistencia;
		this.descripcion = descripcion;
		this.capitadoInconsistencias = capitadoInconsistencias;
		this.inconsistenciasCamposes = inconsistenciasCamposes;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public LogSubirPacientes getLogSubirPacientes() {
		return this.logSubirPacientes;
	}

	public void setLogSubirPacientes(LogSubirPacientes logSubirPacientes) {
		this.logSubirPacientes = logSubirPacientes;
	}

	public String getTipoInconsistencia() {
		return this.tipoInconsistencia;
	}

	public void setTipoInconsistencia(String tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set getCapitadoInconsistencias() {
		return this.capitadoInconsistencias;
	}

	public void setCapitadoInconsistencias(Set capitadoInconsistencias) {
		this.capitadoInconsistencias = capitadoInconsistencias;
	}

	public Set getInconsistenciasCamposes() {
		return this.inconsistenciasCamposes;
	}

	public void setInconsistenciasCamposes(Set inconsistenciasCamposes) {
		this.inconsistenciasCamposes = inconsistenciasCamposes;
	}

}
