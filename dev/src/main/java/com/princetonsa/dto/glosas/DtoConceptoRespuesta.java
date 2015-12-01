/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.glosas;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo del concepto de una glosa
 * 
 * @author Sebastián Gómez R.
 *
 */
public class DtoConceptoRespuesta implements Serializable
{
	private String codigo;
	private int codigoInstitucion;
	private String descripcion;
	private String tipo;
	private DtoConceptoGlosa conceptoglosa;
	private String conceptoajuste;
	private String naturalezaajuste;
	private String usuariomodifica;
	private String fechamodifica;
	private String horamodifica;
		
	
	/**
	 * Constructor
	 */
	public DtoConceptoRespuesta()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.codigo = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.descripcion = "";
		this.tipo = "";
		this.conceptoglosa= new DtoConceptoGlosa();
		this.conceptoajuste="";
		this.naturalezaajuste="";
		this.usuariomodifica="";
		this.fechamodifica="";
		this.horamodifica="";
	}

	public String getNaturalezaajuste() {
		return naturalezaajuste;
	}

	public void setNaturalezaajuste(String naturalezaajuste) {
		this.naturalezaajuste = naturalezaajuste;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
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

	public DtoConceptoGlosa getConceptoglosa() {
		return conceptoglosa;
	}

	public void setConceptoglosa(DtoConceptoGlosa conceptoglosa) {
		this.conceptoglosa = conceptoglosa;
	}

	public String getConceptoajuste() {
		return conceptoajuste;
	}

	public void setConceptoajuste(String conceptoajuste) {
		this.conceptoajuste = conceptoajuste;
	}

	public String getUsuariomodifica() {
		return usuariomodifica;
	}

	public void setUsuariomodifica(String usuariomodifica) {
		this.usuariomodifica = usuariomodifica;
	}

	public String getFechamodifica() {
		return fechamodifica;
	}

	public void setFechamodifica(String fechamodifica) {
		this.fechamodifica = fechamodifica;
	}

	public String getHoramodifica() {
		return horamodifica;
	}

	public void setHoramodifica(String horamodifica) {
		this.horamodifica = horamodifica;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
