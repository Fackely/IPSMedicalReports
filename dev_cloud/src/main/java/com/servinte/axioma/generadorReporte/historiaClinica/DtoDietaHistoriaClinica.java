package com.servinte.axioma.generadorReporte.historiaClinica;

public class DtoDietaHistoriaClinica {
	
	private String nombresDieta;
	
	private Integer codigo;
	
	private String viaOral;
	
	private String suspendido;
	
	private String suspender;
	
	private String descripcion;
	
	
	
	public DtoDietaHistoriaClinica() {
		this.codigo= new Integer(0);
		this.nombresDieta="";
		this.viaOral="";
		this.suspendido="";
		this.descripcion="";
		this.suspender="";
	}



	/**
	 * @return the nombresDieta
	 */
	public String getNombresDieta() {
		return nombresDieta;
	}



	/**
	 * @param nombresDieta the nombresDieta to set
	 */
	public void setNombresDieta(String nombresDieta) {
		this.nombresDieta = nombresDieta;
	}



	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}



	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}



	/**
	 * @return the viaOral
	 */
	public String getViaOral() {
		return viaOral;
	}



	/**
	 * @param viaOral the viaOral to set
	 */
	public void setViaOral(String viaOral) {
		this.viaOral = viaOral;
	}



	/**
	 * @return the suspendido
	 */
	public String getSuspendido() {
		return suspendido;
	}



	/**
	 * @param suspendido the suspendido to set
	 */
	public void setSuspendido(String suspendido) {
		this.suspendido = suspendido;
	}



	/**
	 * @return the suspender
	 */
	public String getSuspender() {
		return suspender;
	}



	/**
	 * @param suspender the suspender to set
	 */
	public void setSuspender(String suspender) {
		this.suspender = suspender;
	}



	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}



	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	
	
	
}
