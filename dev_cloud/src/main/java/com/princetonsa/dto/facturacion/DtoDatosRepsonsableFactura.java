package com.princetonsa.dto.facturacion;

public class DtoDatosRepsonsableFactura {
	
	private String nombreResponsable;
	private String direccionResponsable;
	private String telefonoResponsable;
	
	public DtoDatosRepsonsableFactura() {
		this.nombreResponsable="";
		this.direccionResponsable="";
		this.telefonoResponsable="";
	}

	public String getNombreResponsable() {
		return nombreResponsable;
	}

	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	public String getDireccionResponsable() {
		return direccionResponsable;
	}

	public void setDireccionResponsable(String direccionResponsable) {
		this.direccionResponsable = direccionResponsable;
	}

	public String getTelefonoResponsable() {
		return telefonoResponsable;
	}

	public void setTelefonoResponsable(String telefonoResponsable) {
		this.telefonoResponsable = telefonoResponsable;
	}
	
	
	

}
