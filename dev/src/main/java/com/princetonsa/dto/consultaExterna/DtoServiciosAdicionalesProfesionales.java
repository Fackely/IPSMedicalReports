package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

public class DtoServiciosAdicionalesProfesionales implements Serializable{

	/**
	 * Version serial
	 */
	private static final long serialVersionUID = 1L;
	private String codigoMedico;
	private String codigoServicio;
    private String descripcionServicio;
	private String institucion;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String codigoPropietario;
	
	public DtoServiciosAdicionalesProfesionales(){
		this.clean();
	}
	
	public void clean()
	{
		this.codigoMedico=new String("");
		this.codigoServicio=new String("");
		this.descripcionServicio=new String("");
		this.institucion=new String("");
		this.fechaModificacion=new String("");
		this.horaModificacion=new String("");
		this.usuarioModificacion=new String("");
		this.codigoPropietario= new String("");
	}

	/**
	 * @return the codigoMedico
	 */
	public String getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(String codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}

	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
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
	 * @return the descripcionServicio
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * @param descripcionServicio the descripcionServicio to set
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * @return the codigoPropietario
	 */
	public String getCodigoPropietario() {
		return codigoPropietario;
	}

	/**
	 * @param codigoPropietario the codigoPropietario to set
	 */
	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}
	
}
