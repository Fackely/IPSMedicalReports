package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * AccesoIlimitadoRoles generated by hbm2java
 */
public class AccesoIlimitadoRoles implements java.io.Serializable {

	private long codigoPk;
	private String carpeta;

	public AccesoIlimitadoRoles() {
	}

	public AccesoIlimitadoRoles(long codigoPk, String carpeta) {
		this.codigoPk = codigoPk;
		this.carpeta = carpeta;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getCarpeta() {
		return this.carpeta;
	}

	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}

}
