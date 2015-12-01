package com.princetonsa.enu.interfaz;

import util.ConstantesBD;

public enum TipoInconsistencia {

	NINGUNO(ConstantesBD.codigoNuncaValido,""),
	DATOS(0,"Inconsistencia Datos."),
	SUMAS_IGUALES(1,"Inconsistencia Sumas Iguales.");
	
	
	private TipoInconsistencia(int inconsistencia,String descripcion) 
	{
		this.inconsistencia = inconsistencia;
		this.descripcion = descripcion;
	}
	
	private int inconsistencia;
	private String descripcion;

	public int getInconsistencia() {
		return inconsistencia;
	}

	public void setInconsistencia(int inconsistencia) {
		this.inconsistencia = inconsistencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
