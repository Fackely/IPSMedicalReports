package com.princetonsa.dto.odontologia;

import java.io.Serializable;

public class DtoMotivoCitaPaciente implements Serializable{

	/**
	 * Verión serial
	 */
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String codigoPaciente;
	private String codMotivo;
	private String descripcionMotivo;
	private String mediodeConocimiento;
	private String codMedioConocimiento;
	private String fechaProgramacion;
	private String horaProgramacion;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String observaciones;
	
	
	public DtoMotivoCitaPaciente()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigo= new String("");
		this.codigoPaciente= new String("");
		this.codMotivo= new String("");
		this.descripcionMotivo= new String("");
		this.fechaModificacion= new String("");
		this.horaModificacion= new String("");
		this.fechaProgramacion= new String("");
		this.horaProgramacion= new String("");
		this.usuarioModificacion= new String("");
		this.mediodeConocimiento= new String("");
		this.codMedioConocimiento= new String("");
		this.observaciones= new String("");
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
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	/**
	 * @return the codMotivo
	 */
	public String getCodMotivo() {
		return codMotivo;
	}
	/**
	 * @param codMotivo the codMotivo to set
	 */
	public void setCodMotivo(String codMotivo) {
		this.codMotivo = codMotivo;
	}
	/**
	 * @return the descripcionMotivo
	 */
	public String getDescripcionMotivo() {
		return descripcionMotivo;
	}
	/**
	 * @param descripcionMotivo the descripcionMotivo to set
	 */
	public void setDescripcionMotivo(String descripcionMotivo) {
		this.descripcionMotivo = descripcionMotivo;
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
	 * @return the mediodeConocimiento
	 */
	public String getMediodeConocimiento() {
		return mediodeConocimiento;
	}

	/**
	 * @param mediodeConocimiento the mediodeConocimiento to set
	 */
	public void setMediodeConocimiento(String mediodeConocimiento) {
		this.mediodeConocimiento = mediodeConocimiento;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the fechaProgramacion
	 */
	public String getFechaProgramacion() {
		return fechaProgramacion;
	}

	/**
	 * @param fechaProgramacion the fechaProgramacion to set
	 */
	public void setFechaProgramacion(String fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}

	/**
	 * @return the horaProgramacion
	 */
	public String getHoraProgramacion() {
		return horaProgramacion;
	}

	/**
	 * @param horaProgramacion the horaProgramacion to set
	 */
	public void setHoraProgramacion(String horaProgramacion) {
		this.horaProgramacion = horaProgramacion;
	}

	/**
	 * @return the codMedioConocimiento
	 */
	public String getCodMedioConocimiento() {
		return codMedioConocimiento;
	}

	/**
	 * @param codMedioConocimiento the codMedioConocimiento to set
	 */
	public void setCodMedioConocimiento(String codMedioConocimiento) {
		this.codMedioConocimiento = codMedioConocimiento;
	}
	
}
