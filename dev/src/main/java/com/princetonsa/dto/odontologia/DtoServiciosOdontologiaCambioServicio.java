/**
 * 
 */
package com.princetonsa.dto.odontologia;

/**
 * @author armando
 *
 */
public class DtoServiciosOdontologiaCambioServicio 
{

	/**
	 * 
	 */
	private DtoServicioOdontologico servicioOdontologico;
	
	/**
	 * 
	 */
	private boolean cambiarServicio;
	
	/**
	 * 
	 */
	private int motivoCambioServicio;
	
	/**
	 * 
	 */
	private String observacionesCambioServicio;
	
	/**
	 * 
	 */
	private boolean servicioCitaAsignada;
	
	/**
	 * Habilita la selecci&oacute;n de los servicios seg&uacute;n
	 * el orden de estos
	 */
	private boolean habilitarSeleccion;

	public boolean isServicioCitaAsignada() {
		return servicioCitaAsignada;
	}

	public void setServicioCitaAsignada(boolean servicioCitaAsignada) {
		this.servicioCitaAsignada = servicioCitaAsignada;
	}

	public DtoServicioOdontologico getServicioOdontologico() {
		return servicioOdontologico;
	}

	public void setServicioOdontologico(DtoServicioOdontologico servicioOdontologico) {
		this.servicioOdontologico = servicioOdontologico;
	}

	public boolean isCambiarServicio() {
		return cambiarServicio;
	}

	public void setCambiarServicio(boolean cambiarServicio) {
		this.cambiarServicio = cambiarServicio;
	}

	public int getMotivoCambioServicio() {
		return motivoCambioServicio;
	}

	public void setMotivoCambioServicio(int motivoCambioServicio) {
		this.motivoCambioServicio = motivoCambioServicio;
	}

	public String getObservacionesCambioServicio() {
		return observacionesCambioServicio;
	}

	public void setObservacionesCambioServicio(String observacionesCambioServicio) {
		this.observacionesCambioServicio = observacionesCambioServicio;
	}

	/**
	 * @return Retorna atributo habilitarSeleccion
	 */
	public boolean isHabilitarSeleccion()
	{
		return habilitarSeleccion;
	}

	/**
	 * @param habilitarSeleccion Asigna atributo habilitarSeleccion
	 */
	public void setHabilitarSeleccion(boolean habilitarSeleccion)
	{
		this.habilitarSeleccion = habilitarSeleccion;
	}
	
	
}
