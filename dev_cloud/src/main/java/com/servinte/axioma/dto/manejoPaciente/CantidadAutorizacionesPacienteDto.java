package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;

/**
 * @author ricruico
 *
 */
public class CantidadAutorizacionesPacienteDto implements Serializable{

	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 744250894515428283L;
	
	private long cantidadAutorizaciones;
	private long cantidadIngresos;
	

	public CantidadAutorizacionesPacienteDto(){
	this.reset();
		}
	public void reset()
	{
		this.setCantidadAutorizaciones(0);
		this.setCantidadIngresos(0);
		
	}
	public CantidadAutorizacionesPacienteDto(long cantidadAutorizaciones){
	this.cantidadAutorizaciones=cantidadAutorizaciones;
	this.cantidadIngresos=0;
		}
	/**
	 * @return the cantidadAutorizaciones
	 */
	public long getCantidadAutorizaciones() {
		return cantidadAutorizaciones;
	}
	/**
	 * @param cantidadAutorizaciones the cantidadAutorizaciones to set
	 */
	public void setCantidadAutorizaciones(long cantidadAutorizaciones) {
		this.cantidadAutorizaciones = cantidadAutorizaciones;
	}
	/**
	 * @return the cantidadIngresos
	 */
	public long getCantidadIngresos() {
		return cantidadIngresos;
	}
	/**
	 * @param cantidadIngresos the cantidadIngresos to set
	 */
	public void setCantidadIngresos(long cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}

	

	
	
}