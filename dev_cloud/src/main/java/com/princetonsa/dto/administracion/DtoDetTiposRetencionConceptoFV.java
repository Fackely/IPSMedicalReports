package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoDetTiposRetencionConceptoFV implements Serializable{
	
	private int consecutivo;
	private int codigoTipoRetencion;
	private int codigoConcepto;
	private String activo;
	private String usuarioModificacion;
	private String usuarioAnulacion;
	private String descripcionConcFV;
	
	//***************************
	// Atributos Accion Ejecutar
	private String eliminar;
	//***************************
	
	/**
	 * Constructor
	 */
	public DtoDetTiposRetencionConceptoFV()
	{
		this.reset();
	}
	
	/**
	 * Método que inicia los datos Detalle tipos Retencion clase invetario
	 *
	 */
	private void reset() 
	{
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.codigoTipoRetencion = ConstantesBD.codigoNuncaValido;
		this.codigoConcepto = ConstantesBD.codigoNuncaValido;
		this.activo = "";
		this.usuarioModificacion = "";
		this.usuarioAnulacion = "";
		this.descripcionConcFV = "";
		this.eliminar = ConstantesBD.acronimoNo;
	}

	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigoTipoRetencion
	 */
	public int getCodigoTipoRetencion() {
		return codigoTipoRetencion;
	}

	/**
	 * @param codigoTipoRetencion the codigoTipoRetencion to set
	 */
	public void setCodigoTipoRetencion(int codigoTipoRetencion) {
		this.codigoTipoRetencion = codigoTipoRetencion;
	}

	/**
	 * @return the codigoConcepto
	 */
	public int getCodigoConcepto() {
		return codigoConcepto;
	}

	/**
	 * @param codigoConcepto the codigoConcepto to set
	 */
	public void setCodigoConcepto(int codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the usuarioAnulacion
	 */
	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}

	/**
	 * @param usuarioAnulacion the usuarioAnulacion to set
	 */
	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	/**
	 * @return the descripcionConcFV
	 */
	public String getDescripcionConcFV() {
		return descripcionConcFV;
	}

	/**
	 * @param descripcionConcFV the descripcionConcFV to set
	 */
	public void setDescripcionConcFV(String descripcionConcFV) {
		this.descripcionConcFV = descripcionConcFV;
	}

	/**
	 * @return the eliminar
	 */
	public String getEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}
	
}
