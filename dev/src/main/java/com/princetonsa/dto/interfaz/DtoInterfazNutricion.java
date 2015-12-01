package com.princetonsa.dto.interfaz;

import java.io.Serializable;

/**
 * 
 * @author Andres Silva
 *
 */

public class DtoInterfazNutricion implements Serializable
{
	/**
	 * NUMERO DE INGRESO
	 */
	private String ingreso;
	
	/**
	 * VIA INGRESO
	 */
	private String viaing;
	
	/**
	 * SECCION DE LA CAMA
	 */
	private String secama;
	
	/**
	 * NUMERO DE LA CAMA
	 */
	private String nucama;
	
	/**
	 * NUMERO DE HISTORIA CLINICA
	 */
	private String numhis;
	
	/**
	 * CODIGO DE LA DIETA ORDENADA
	 */
	private String coddie;
	
	/**
	 * DESCRIPCION 1 DE LA DIETA
	 */
	private String descr1;
	
	/**
	 * DESCRIPCION 2 DE LA DIETA
	 */
	private String descr2;
	
	/**
	 * REGISTRO DE USUARIO
	 */
	private String regusu;
	
	/**
	 * ESTADO DE LA DIETA
	 */
	private String estdie;
	
	/**
	 * FECHA DE LA DIETA ORDENADA
	 */
	private String fecdie;
	
	/**
	 * HORA DE LA DIETA ORDENADA
	 */
	private String hordie;
	
	/**
	 * ESTADO DEL REGISTRO
	 */
	private String estreg;
	
	/**
	 * FECHA DE ENVIO AAAA/MM/DD
	 */
	private String fecenv;
	
	/**
	 * HORA DE ENVIO
	 */
	private String horenv;
	
	/**
	 * PACIENTE VIP
	 */
	private String pacvip;
	
	/**
	 * Tipo de Paciente
	 */
	private String tipopac;
	
	
	//Cambios en la tabla ax_nutri Agosto 25 de 2008
	private String idvia;
	
	
	private String consecutivo;
	
	
	/**	
	 * INICIALIZACION DE LOS CAMPOS
	 */
	public DtoInterfazNutricion()
	{
		ingreso="";
		viaing="";
		secama="";
		nucama="";
		numhis="";
		coddie="";
		descr1="";
		descr2="";
		regusu="";
		estdie="";
		fecdie="";
		hordie="";
		estreg="";
		fecenv="";
		horenv="";
		pacvip="";
		idvia="";
		tipopac="";
		consecutivo="";
	}
	
	/**
	 * CONSTRUCTOR DTO INTERFAZ NUTRICION
	 * @param ingreso
	 * @param viaing
	 * @param secama
	 * @param nucama
	 * @param numhis
	 * @param coddie
	 * @param descr1
	 * @param descr2
	 * @param regusu
	 * @param estdie
	 * @param fecdie
	 * @param hordie
	 * @param estreg
	 * @param fecenv
	 * @param horenv
	 * @param pacvip
	 * @param idvia
	 */
	public DtoInterfazNutricion(String ingreso, String viaing, String secama, String nucama, String numhis, String coddie, String descr1, String descr2, String regusu, String estdie, String fecdie, String hordie, String estreg, String fecenv, String horenv, String pacvip, String idvia, String tipopac)
	{
		this.ingreso=ingreso;
		this.viaing=viaing;
		this.secama=secama;
		this.nucama=nucama;
		this.numhis=numhis;
		this.coddie=coddie;
		this.descr1=descr1;
		this.descr2=descr2;
		this.regusu=regusu;
		this.estdie=estdie;
		this.fecdie=fecdie;
		this.hordie=hordie;
		this.estreg=estreg;
		this.fecenv=fecenv;
		this.horenv=horenv;
		this.pacvip=pacvip;
		this.idvia=idvia;
		this.tipopac=tipopac;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getCoddie() {
		return coddie;
	}

	public void setCoddie(String coddie) {
		this.coddie = coddie;
	}

	public String getDescr1() {
		return descr1;
	}

	public void setDescr1(String descr1) {
		this.descr1 = descr1;
	}

	public String getDescr2() {
		return descr2;
	}

	public void setDescr2(String descr2) {
		this.descr2 = descr2;
	}

	public String getEstdie() {
		return estdie;
	}

	public void setEstdie(String estdie) {
		this.estdie = estdie;
	}

	public String getEstreg() {
		return estreg;
	}

	public void setEstreg(String estreg) {
		this.estreg = estreg;
	}

	public String getFecdie() {
		return fecdie;
	}

	public void setFecdie(String fecdie) {
		this.fecdie = fecdie;
	}

	public String getFecenv() {
		return fecenv;
	}

	public void setFecenv(String fecenv) {
		this.fecenv = fecenv;
	}

	public String getHordie() {
		return hordie;
	}

	public void setHordie(String hordie) {
		this.hordie = hordie;
	}

	public String getHorenv() {
		return horenv;
	}

	public void setHorenv(String horenv) {
		this.horenv = horenv;
	}

	public String getIngreso() {
		return ingreso;
	}

	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	public String getNucama() {
		return nucama;
	}

	public void setNucama(String nucama) {
		this.nucama = nucama;
	}

	public String getNumhis() {
		return numhis;
	}

	public void setNumhis(String numhis) {
		this.numhis = numhis;
	}

	public String getPacvip() {
		return pacvip;
	}

	public void setPacvip(String pacvip) {
		this.pacvip = pacvip;
	}

	public String getRegusu() {
		return regusu;
	}

	public void setRegusu(String regusu) {
		this.regusu = regusu;
	}

	public String getSecama() {
		return secama;
	}

	public void setSecama(String secama) {
		this.secama = secama;
	}

	public String getViaing() {
		return viaing;
	}

	public void setViaing(String viaing) {
		this.viaing = viaing;
	}

	public String getIdvia() {
		return idvia;
	}

	public void setIdvia(String idvia) {
		this.idvia = idvia;
	}
	
	public String getTipopac() {
		return tipopac;
	}
	
	public void setTipopac(String tipopac) {
		this.tipopac = tipopac;
	}
	
	
}