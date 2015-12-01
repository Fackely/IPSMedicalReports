package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoRolesTipoDeUsuario implements Serializable , Cloneable {

	private static final long serialVersionUID = 1L;
	private double codigo;
	private String descripcion;
	
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	
	/*------------------------------------------------------------------
	 * Estos atributos son utilizados para validar los roles de usuario 
	 -------------------------------------------------------------------*/
	/** Código de la funcionalidad a validar */
	private int codigoFuncionalidad;
	/** Indica si el usuario tiene rol/permiso sobre la funcionalidad indicada (codigoFuncionalidad)  */
	private boolean tieneRol;
	
	
	
	public DtoRolesTipoDeUsuario(){
		this.reset();
	}
	
	
	public void reset(){
		
		this.codigo = ConstantesBD.codigoNuncaValido;
	
		this.descripcion = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
		this.codigoFuncionalidad = ConstantesBD.codigoNuncaValido;
		this.tieneRol = false;
		
	}

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
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
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}

	
	public int getCodigoFuncionalidad() {
		return codigoFuncionalidad;
	}

	
	public void setCodigoFuncionalidad(int codigoFuncionalidad) {
		this.codigoFuncionalidad = codigoFuncionalidad;
	}
	

	public boolean isTieneRol() {
		return tieneRol;
	}

	
	public void setTieneRol(boolean tieneRol) {
		this.tieneRol = tieneRol;
	}

}
