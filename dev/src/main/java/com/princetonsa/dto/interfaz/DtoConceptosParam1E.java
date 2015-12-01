package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */
public class DtoConceptosParam1E implements Serializable{
	
	private int consecutivo;
	private String claseDocumento;
	private String seccion;
	private int codigoConceptoRetencion;
	private int codigoParamGeneral1E;
	private String activo;
	private String usuarioModifica;
	private String usuarioAnulacion;
	private String desConceptoRetencion;

	//**************************
	// Atributos Accion Ejecutar
	private String eliminar;
	private String ingresar;
	//**************************
	
	/**
	 * Constructor
	 */
	public DtoConceptosParam1E()
	{
		this.reset();
	}
	
	/**
	 * Inicializacion de Atributos del Dto
	 */
	public void reset()
	{
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.claseDocumento = "";
		this.seccion = "";
		this.codigoConceptoRetencion = ConstantesBD.codigoNuncaValido;
		this.codigoParamGeneral1E = ConstantesBD.codigoNuncaValido;
		this.activo = "";
		this.usuarioModifica = "";
		this.usuarioAnulacion = "";
		this.eliminar = ConstantesBD.acronimoNo;
		this.ingresar = ConstantesBD.acronimoNo;
		this.desConceptoRetencion = "";
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
	 * @return the claseDocumento
	 */
	public String getClaseDocumento() {
		return claseDocumento;
	}

	/**
	 * @param claseDocumento the claseDocumento to set
	 */
	public void setClaseDocumento(String claseDocumento) {
		this.claseDocumento = claseDocumento;
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the codigoConceptoRetencion
	 */
	public int getCodigoConceptoRetencion() {
		return codigoConceptoRetencion;
	}

	/**
	 * @param codigoConceptoRetencion the codigoConceptoRetencion to set
	 */
	public void setCodigoConceptoRetencion(int codigoConceptoRetencion) {
		this.codigoConceptoRetencion = codigoConceptoRetencion;
	}

	/**
	 * @return the codigoParamGeneral1E
	 */
	public int getCodigoParamGeneral1E() {
		return codigoParamGeneral1E;
	}

	/**
	 * @param codigoParamGeneral1E the codigoParamGeneral1E to set
	 */
	public void setCodigoParamGeneral1E(int codigoParamGeneral1E) {
		this.codigoParamGeneral1E = codigoParamGeneral1E;
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
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
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

	/**
	 * @return the ingresar
	 */
	public String getIngresar() {
		return ingresar;
	}

	/**
	 * @param ingresar the ingresar to set
	 */
	public void setIngresar(String ingresar) {
		this.ingresar = ingresar;
	}

	/**
	 * @return the desConceptoRetencion
	 */
	public String getDesConceptoRetencion() {
		return desConceptoRetencion;
	}

	/**
	 * @param desConceptoRetencion the desConceptoRetencion to set
	 */
	public void setDesConceptoRetencion(String desConceptoRetencion) {
		this.desConceptoRetencion = desConceptoRetencion;
	}
	
}
