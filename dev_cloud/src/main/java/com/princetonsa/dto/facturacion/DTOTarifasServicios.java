package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * Esta clase se encarga de almacenar los datos de las tarifas de servicios
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 17/11/2010
 */
public class DTOTarifasServicios implements Serializable {

	private static final long serialVersionUID = 1L;
	private long codigoPrograma;
	private String nombrePrograma;
	private String nombreEspecialdad;
	private String codigoServicio;
	private String descripcionServicio;
	private Double tarifaServicio;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoPrograma.
	 * 
	 * @return codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoPrograma.
	 * 
	 * @param codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombrePrograma.
	 * 
	 * @return nombrePrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombrePrograma.
	 * 
	 * @param nombrePrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEspecialdad.
	 * 
	 * @return nombreEspecialdad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEspecialdad() {
		return nombreEspecialdad;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEspecialdad.
	 * 
	 * @param nombreEspecialdad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEspecialdad(String nombreEspecialdad) {
		this.nombreEspecialdad = nombreEspecialdad;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoServicio.
	 * 
	 * @return codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoServicio.
	 * 
	 * @param codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * descripcionServicio.
	 * 
	 * @return descripcionServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * descripcionServicio.
	 * 
	 * @param descripcionServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo tarifaServicio.
	 * 
	 * @return tarifaServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getTarifaServicio() {
		return tarifaServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * tarifaServicio.
	 * 
	 * @param tarifaServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setTarifaServicio(Double tarifaServicio) {
		this.tarifaServicio = tarifaServicio;
	}

}
