/**
 * 
 */
package com.princetonsa.dto.odontologia;

/**
 * @author armando
 *
 */
public class DtoProgramasServiciosAnterioresCita 
{

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
	private int servicio;
	
	
	/**
	 * 
	 */
	private int codigoPrograma;
	
	/**
	 * 
	 */
	private DtoServicioOdontologico dtoServicio;
	
	/**
	 * 
	 */
	private String cambio;
	
	/**
	 * 
	 */
	private DtoMotivosCambioServicio motivo;
	
	/**
	 * 
	 */
	private String observaciones;

	/**
	 * 
	 */
	private DtoProgHallazgoPieza programaHallazgoPieza;

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

	public int getServicio() {
		return servicio;
	}

	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	public DtoProgHallazgoPieza getProgramaHallazgoPieza() {
		return programaHallazgoPieza;
	}

	public void setProgramaHallazgoPieza(DtoProgHallazgoPieza programaHallazgoPieza) {
		this.programaHallazgoPieza = programaHallazgoPieza;
	}

	public int getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(int codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public DtoServicioOdontologico getDtoServicio() {
		return dtoServicio;
	}

	public void setDtoServicio(DtoServicioOdontologico dtoServicio) {
		this.dtoServicio = dtoServicio;
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

}
