/**
 * Dto para la informacion mas revelante de un Ingreso
 * @author javrammo
 */
package com.servinte.axioma.dto.historiaClinica;

import java.util.Date;

public class InfoIngresoDto {

	/**
	 * Id del Ingreso
	 */
	private int IdIngreso;
	/**
	 * Fecha de Egreso
	 */
	private Date fechaEgreso;
	/**
	 * Hora del Egreso
	 */
	private String horaEgreso;

	/**
	 * @return the idIngreso
	 */
	public int getIdIngreso() {
		return IdIngreso;
	}
	
	/**
	 * @param idIngreso
	 * @param fechaEgreso
	 * @param horaEgreso
	 */
	public InfoIngresoDto(int idIngreso, Date fechaEgreso, String horaEgreso) {
		super();
		IdIngreso = idIngreso;
		this.fechaEgreso = fechaEgreso;
		this.horaEgreso = horaEgreso;
	}	
	
	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(int idIngreso) {
		IdIngreso = idIngreso;
	}
	/**
	 * @return the fechaEgreso
	 */
	public Date getFechaEgreso() {
		return fechaEgreso;
	}
	/**
	 * @param fechaEgreso the fechaEgreso to set
	 */
	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}
	/**
	 * @return the horaEgreso
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}
	/**
	 * @param horaEgreso the horaEgreso to set
	 */
	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}
	
}
