package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoTiposDeUsuario implements Serializable{

	private double codigo;
	private String descripcion;
	private String rol;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	
	
	public DtoTiposDeUsuario(){
		
		this.reset();
		
	}
	
	public void reset(){
		
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.rol = "";
		this.descripcion = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
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

	/**
	 * @return the rol
	 */
	public String getRol() {
		return rol;
	}

	/**
	 * @param rol the rol to set
	 */
	public void setRol(String rol) {
		this.rol = rol;
	}


	
}
