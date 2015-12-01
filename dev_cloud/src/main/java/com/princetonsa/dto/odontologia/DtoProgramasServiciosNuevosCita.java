/**
 * 
 */
package com.princetonsa.dto.odontologia;

/**
 * @author armando
 *
 */
public class DtoProgramasServiciosNuevosCita 
{

	/**
	 * 
	 */
	private DtoProgHallazgoPieza programaHallazgoPieza;
	
	/**
	 * 
	 */
	private int codigoPk;

	/**
	 * 
	 */
	private int solicitud;

	/**
	 * 
	 */
	private DtoServicioOdontologico servicio;

	/**
	 * 
	 */
	private String cambio;
	
	/**
	 * 
	 */
	private int codigoPrograma;
	
	/**
	 * 
	 */
	private DtoMotivosCambioServicio motivo;
	
	/**
	 * 
	 */
	private String observaciones;

	public DtoProgHallazgoPieza getProgramaHallazgoPieza() {
		return programaHallazgoPieza;
	}

	public void setProgramaHallazgoPieza(DtoProgHallazgoPieza programaHallazgoPieza) {
		this.programaHallazgoPieza = programaHallazgoPieza;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	public DtoServicioOdontologico getServicio() {
		return servicio;
	}

	public void setServicio(DtoServicioOdontologico servicio) {
		this.servicio = servicio;
	}

	public String getCambio() {
		return cambio;
	}

	public void setCambio(String cambio) {
		this.cambio = cambio;
	}

	public DtoMotivosCambioServicio getMotivo() {
		return motivo;
	}

	public void setMotivo(DtoMotivosCambioServicio motivo) {
		this.motivo = motivo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(int codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
}
