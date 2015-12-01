/**
 * @autor Jorge Armando Agudelo Quintero
 */
package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

/**
 * 
 * Clase que contiene la información de un servicio asociado a la cita
 * que debe imprimirse.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class DtoServicioImpresion {

	
	/**
	 * Código del servicio
	 */
	private String codigoServicio;
	
	/**
	 * Número de la pieza dental, Superficie ó Etiqueta Boca a la que aplica el respectivo Programa 
	 * asociado al servicio que se esta asignando en la cita
	 */
	private String aplicaA;
	
	/**
	 * Descripción del servicio asignado en la cita
	 */
	private String nombreServicio;
	
	/**
	 * Constructor de la clase
	 */
	public DtoServicioImpresion() {
		// TODO Auto-generated constructor stub
		
		aplicaA = "";
		codigoServicio = "";
		nombreServicio="";
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param aplicaA the aplicaA to set
	 */
	public void setAplicaA(String aplicaA) {
		this.aplicaA = aplicaA;
	}

	/**
	 * @return the aplicaA
	 */
	public String getAplicaA() {
		return aplicaA;
	}

	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}
	
	
}
