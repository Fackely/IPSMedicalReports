package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

/**
 * Esta clase se encarga de recibir los datos que se crean en el generador del formato pdf de plantillas
 * odontológicas en el tipo de componente odontograma de evolución para crear y organizar un nuevo subreporte
 * de los programas y servicios de la cita asignada
 * 
 * @author Fabian Becerra
 *
 */
public class DtoFormatoPlantillaProgServCitaAsignada implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int piezaDental;
	private String superficie;
	private String hallazgo;
	private String programaServicio;
	private String estado;
	private String estadoPlanT;
	
	public int getPiezaDental() {
		return piezaDental;
	}
	public void setPiezaDental(int piezaDental) {
		this.piezaDental = piezaDental;
	}
	public String getSuperficie() {
		return superficie;
	}
	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}
	public String getHallazgo() {
		return hallazgo;
	}
	public void setHallazgo(String hallazgo) {
		this.hallazgo = hallazgo;
	}
	public String getProgramaServicio() {
		return programaServicio;
	}
	public void setProgramaServicio(String programaServicio) {
		this.programaServicio = programaServicio;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void setEstadoPlanT(String estadoPlanT) {
		this.estadoPlanT = estadoPlanT;
	}
	public String getEstadoPlanT() {
		return estadoPlanT;
	}
	
	
}
