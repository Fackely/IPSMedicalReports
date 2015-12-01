/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import java.util.Date;

/**
 * @author jeilones
 *
 */
public class AutorizacionesSolicitudesDto {

	public static int SOL_ORDEN_AMB=1;
	public static int SOL_PETICION_QX=2;
	
	private Long numeroSolicitud;
	private Long numeroOrden;
	private Boolean esSolOrdenAmb;
	private Boolean esSolPeticionQx;
	private Date fechaGeneracion;
	
	public AutorizacionesSolicitudesDto(Long numeroSolicitud, Long numeroOrden,
			Date fechaGeneracion,Boolean esSolOrdenAmb, Boolean esSolPeticionQx) {
		super();
		this.numeroSolicitud = numeroSolicitud;
		this.numeroOrden = numeroOrden;
		this.esSolOrdenAmb = esSolOrdenAmb;
		this.esSolPeticionQx = esSolPeticionQx;
		this.fechaGeneracion = fechaGeneracion;
	}

	public Long getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(Long numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public Long getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Boolean isEsSolOrdenAmb() {
		return esSolOrdenAmb;
	}

	public void setEsSolOrdenAmb(Boolean esSolOrdenAmb) {
		this.esSolOrdenAmb = esSolOrdenAmb;
	}

	public Boolean isEsSolPeticionQx() {
		return esSolPeticionQx;
	}

	public void setEsSolPeticionQx(Boolean esSolPeticionQx) {
		this.esSolPeticionQx = esSolPeticionQx;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public Boolean getEsSolOrdenAmb() {
		return esSolOrdenAmb;
	}

	public Boolean getEsSolPeticionQx() {
		return esSolPeticionQx;
	}
	
	
}
