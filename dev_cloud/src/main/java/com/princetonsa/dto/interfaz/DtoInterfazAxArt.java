package com.princetonsa.dto.interfaz;

import java.io.Serializable;

public class DtoInterfazAxArt implements Serializable{
	
	/**
	 * Codigo Axioma generado desde la tabla articulo a la hora de creacion de este
	 */
	private String codaxi;
	
	/**
	 * Descripcion del articulo asignado desde axioma
	 */
	private String descri;
	
	/**
	 * Tipo de creacion del articulo indica si el articulo se ha creado manual o automaticamente
	 */
	private String tipcrea;
	
	/**
	 * Tipo de inventario indica si el articulo pertenece a un medicamento o elemento
	 */
	private String tipinv;
	
	/**
	 * Codigo Shaio se actualiza desde el sistema Shaio
	 */
	private String codsha;
	
	/**
	 * Estado de registro
	 */
	private String estreg;
	
	/**
	 * Fecha de generacion del registro con la fecha del sistema
	 */
	private String fecha;
	
	/**
	 * Hora de creacion del registro
	 */
	private String hora;
	
	/**
	 * Campo para Almacenar la clase del Articulo
	 */
	private String clase;
	
	/**
	 * 
	 */
	private String consecutivo;
	/**
	 * INICIALIZACION DE LOS CAMPOS
	 */
	
	public DtoInterfazAxArt()
	{
		this.codaxi="";
		this.descri="";
		this.tipcrea="";
		this.tipinv="";
		this.clase="";
		this.codsha="";
		this.estreg="";
		this.fecha="";
		this.hora="";
		this.consecutivo="";
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

	/**
	 * 
	 * @param codaxi
	 * @param descri
	 * @param tipcrea
	 * @param tipinv
	 * @param codsha
	 * @param estreg
	 * @param fecha
	 * @param hora
	 */
	public DtoInterfazAxArt(String codaxi,String descri,String tipcrea,String tipinv,String clase ,String codsha,String estreg,String fecha,String hora)
	{
		this.codaxi=codaxi;
		this.descri=descri;
		this.tipcrea=tipcrea;
		this.tipinv=tipinv;
		this.clase=clase;
		this.codsha=codsha;
		this.estreg=estreg;
		this.fecha=fecha;
		this.hora=hora;
	}

	public String getCodaxi() {
		return codaxi;
	}

	public void setCodaxi(String codaxi) {
		this.codaxi = codaxi;
	}

	public String getCodsha() {
		return codsha;
	}

	public void setCodsha(String codsha) {
		this.codsha = codsha;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public String getEstreg() {
		return estreg;
	}

	public void setEstreg(String estreg) {
		this.estreg = estreg;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getTipcrea() {
		return tipcrea;
	}

	public void setTipcrea(String tipcrea) {
		this.tipcrea = tipcrea;
	}

	public String getTipinv() {
		return tipinv;
	}

	public void setTipinv(String tipinv) {
		this.tipinv = tipinv;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}
	
	

}
