package com.servinte.axioma.orm;

// Generated 24/06/2011 03:09:11 PM by Hibernate Tools 3.4.0.CR1

/**
 * AutoEntsubPeticiones generated by hbm2java
 */
public class AutoEntsubPeticiones implements java.io.Serializable {

	private long codigoPk;
	private PeticionQx peticionQx;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;

	public AutoEntsubPeticiones() {
	}

	public AutoEntsubPeticiones(long codigoPk, PeticionQx peticionQx,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.codigoPk = codigoPk;
		this.peticionQx = peticionQx;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public PeticionQx getPeticionQx() {
		return this.peticionQx;
	}

	public void setPeticionQx(PeticionQx peticionQx) {
		this.peticionQx = peticionQx;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

}
