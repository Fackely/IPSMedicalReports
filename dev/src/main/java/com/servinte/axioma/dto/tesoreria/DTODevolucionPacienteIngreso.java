package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Esta clase almacena los registros de devoluci�n de abonos de un paciente por
 * ingreso.
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 09/03/2011
 */
public class DTODevolucionPacienteIngreso implements Serializable {

	private static final long serialVersionUID = 1L;
	private String numeroIngreso;
	private String nombreCentroAtencion;
	private BigDecimal valorNotaDevolucion;

	/**
	 * M�todo encargado de obtener el valor del atributo numeroIngreso.
	 * 
	 * @return numeroIngreso
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNumeroIngreso() {
		return numeroIngreso;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo numeroIngreso.
	 * 
	 * @param numeroIngreso
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNumeroIngreso(String numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo nombreCentroAtencion.
	 * 
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @param nombreCentroAtencion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo valorNotaDevolucion.
	 * 
	 * @return valorNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getValorNotaDevolucion() {
		return valorNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo valorNotaDevolucion.
	 * 
	 * @param valorNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setValorNotaDevolucion(BigDecimal valorNotaDevolucion) {
		this.valorNotaDevolucion = valorNotaDevolucion;
	}

}
