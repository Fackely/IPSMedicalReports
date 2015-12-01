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
public class DtoConceptoGlosa implements Serializable
{
	private String codigo;
	private String codigoConcepto;
	private int codigoInstitucion;
	private String descripcion;
	private String tipo;
	//Atributos que almacenan las llaves con otras tablas (son excluyentes)
	private String codigoFacturaGlosa;
	private String codigoDetalleFacturaGlosa;
	private String codigoDetalleAsocioGlosa;

	
	//Atributos para el manejo de los procesos del concepto
	private boolean eliminado;
	
	
	/**
	 * Constructor
	 */
	public DtoConceptoGlosa()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.codigo = "";
		this.codigoConcepto = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.descripcion = "";
		this.tipo = "";
		this.codigoFacturaGlosa = "";
		this.codigoDetalleFacturaGlosa = "";
		this.codigoDetalleAsocioGlosa = "";
		this.eliminado = false;
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

	

	/**
	 * @return the codigoDetalleAsocioGlosa
	 */
	public String getCodigoDetalleAsocioGlosa() {
		return codigoDetalleAsocioGlosa;
	}

	/**
	 * @param codigoDetalleAsocioGlosa the codigoDetalleAsocioGlosa to set
	 */
	public void setCodigoDetalleAsocioGlosa(String codigoDetalleAsocioGlosa) {
		this.codigoDetalleAsocioGlosa = codigoDetalleAsocioGlosa;
	}

	/**
	 * @return the codigoFacturaGlosa
	 */
	public String getCodigoFacturaGlosa() {
		return codigoFacturaGlosa;
	}

	/**
	 * @param codigoFacturaGlosa the codigoFacturaGlosa to set
	 */
	public void setCodigoFacturaGlosa(String codigoFacturaGlosa) {
		this.codigoFacturaGlosa = codigoFacturaGlosa;
	}

	/**
	 * @return the codigoDetalleFacturaGlosa
	 */
	public String getCodigoDetalleFacturaGlosa() {
		return codigoDetalleFacturaGlosa;
	}

	/**
	 * @param codigoDetalleFacturaGlosa the codigoDetalleFacturaGlosa to set
	 */
	public void setCodigoDetalleFacturaGlosa(String codigoDetalleFacturaGlosa) {
		this.codigoDetalleFacturaGlosa = codigoDetalleFacturaGlosa;
	}

	/**
	 * @return the codigoConcepto
	 */
	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	/**
	 * @param codigoConcepto the codigoConcepto to set
	 */
	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	/**
	 * @return the eliminado
	 */
	public boolean isEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
