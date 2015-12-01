package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

/**
 * Esta clase se encarga de recibir los datos que se crean en el generador del formato pdf de plantillas
 * odontológicas en el tipo de componente odontograma de diagnóstico para crear y organizar un nuevo subreporte
 * de los hallazgos en el plan de tratamiento inicial
 * 
 * @author Fabian Becerra
 *
 */
public class DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial implements Serializable{

	private static final long serialVersionUID = 1L;
	private int piezaDental;
	private String fechaHora;
	private String hallazgo;
	private String superficie;
	private String programaServicio;
	private String clasificacion;
	
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getHallazgo() {
		return hallazgo;
	}
	public void setHallazgo(String hallazgo) {
		this.hallazgo = hallazgo;
	}
	public String getSuperficie() {
		return superficie;
	}
	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}
	public String getProgramaServicio() {
		return programaServicio;
	}
	public void setProgramaServicio(String programaServicio) {
		this.programaServicio = programaServicio;
	}
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	public void setPiezaDental(int piezaDental) {
		this.piezaDental = piezaDental;
	}
	public int getPiezaDental() {
		return piezaDental;
	}
	
	

}
