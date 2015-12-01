package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoDetTiposRetencionGrupoSer implements Serializable{
	
	private int consecutivo;
	private int codigoTipoRetencion;
	private int codigoGrupoSer;
	private String activo;
	private String usuarioModificacion;
	private String usuarioAnulacion;
	private String descripcionGrupoSer;
	private String acronimoGrupoSer;
	
	//***************************
	// Atributos Accion Ejecutar
	private String eliminar;
	//***************************
	/**
	 * Constructor
	 */
	public DtoDetTiposRetencionGrupoSer()
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
		this.codigoGrupoSer = ConstantesBD.codigoNuncaValido;
		this.activo = "";
		this.usuarioModificacion = "";
		this.usuarioAnulacion = "";
		this.descripcionGrupoSer = "";
		this.acronimoGrupoSer = "";
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
	 * @return the codigoGrupoSer
	 */
	public int getCodigoGrupoSer() {
		return codigoGrupoSer;
	}

	/**
	 * @param codigoGrupoSer the codigoGrupoSer to set
	 */
	public void setCodigoGrupoSer(int codigoGrupoSer) {
		this.codigoGrupoSer = codigoGrupoSer;
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
	 * @return the descripcionGrupoSer
	 */
	public String getDescripcionGrupoSer() {
		return descripcionGrupoSer;
	}

	/**
	 * @param descripcionGrupoSer the descripcionGrupoSer to set
	 */
	public void setDescripcionGrupoSer(String descripcionGrupoSer) {
		this.descripcionGrupoSer = descripcionGrupoSer;
	}

	/**
	 * @return the acronimoGrupoSer
	 */
	public String getAcronimoGrupoSer() {
		return acronimoGrupoSer;
	}

	/**
	 * @param acronimoGrupoSer the acronimoGrupoSer to set
	 */
	public void setAcronimoGrupoSer(String acronimoGrupoSer) {
		this.acronimoGrupoSer = acronimoGrupoSer;
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
