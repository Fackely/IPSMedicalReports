package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

public class DtoPaquetesOdontologicos  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private int codigoEspecialidad;
	
	/**
	 * 
	 */
	private String descripcionEspecialidad;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private String fechaModifica;
	
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private ArrayList<DtoProgramasPaqueteOdonto> programasPaqueteOdonto=new ArrayList<DtoProgramasPaqueteOdonto>();
	
	/**
	 * 
	 */
	private ArrayList<DtoServiciosPaqueteOdon> serviciosPaqueteOdonto=new ArrayList<DtoServiciosPaqueteOdon>();
	
	
	/**
	 * 
	 */
	private String horaModifica;
	
	/**
	 * 
	 */
	private boolean esUsado;

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public ArrayList<DtoProgramasPaqueteOdonto> getProgramasPaqueteOdonto() {
		return programasPaqueteOdonto;
	}

	public void setProgramasPaqueteOdonto(
			ArrayList<DtoProgramasPaqueteOdonto> programasPaqueteOdonto) {
		this.programasPaqueteOdonto = programasPaqueteOdonto;
	}

	public ArrayList<DtoServiciosPaqueteOdon> getServiciosPaqueteOdonto() {
		return serviciosPaqueteOdonto;
	}

	public void setServiciosPaqueteOdonto(
			ArrayList<DtoServiciosPaqueteOdon> serviciosPaqueteOdonto) {
		this.serviciosPaqueteOdonto = serviciosPaqueteOdonto;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getDescripcionEspecialidad() {
		return descripcionEspecialidad;
	}

	public void setDescripcionEspecialidad(String descripcionEspecialidad) {
		this.descripcionEspecialidad = descripcionEspecialidad;
	}


	public boolean isEsUsado() {
		return esUsado;
	}

	public void setEsUsado(boolean esUsado) {
		this.esUsado = esUsado;
	}


}
