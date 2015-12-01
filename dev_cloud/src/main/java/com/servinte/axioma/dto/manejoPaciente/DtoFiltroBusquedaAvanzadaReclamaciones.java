/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

/**
 * @author JorOsoVe
 *
 */
public class DtoFiltroBusquedaAvanzadaReclamaciones 
{
	private String tipoEvento;
	
	private String tipoReclamacion;
	
	private String estadoReclamacion;
	
	private String convenioResponsable;
	
	private String fechaInicialReclamacion;
	
	private String fechaFinalReclamacion;
	
	private String nroInicialReclamacion;
	
	private String nroFinalReclamacion;

	/**
	 * 
	 */
	public DtoFiltroBusquedaAvanzadaReclamaciones() 
	{
		this.tipoEvento ="";
		this.tipoReclamacion = "";
		this.estadoReclamacion = "";
		this.convenioResponsable = "";
		this.fechaInicialReclamacion = "";
		this.fechaFinalReclamacion = "";
		this.nroInicialReclamacion = "";
		this.nroFinalReclamacion = "";
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getTipoReclamacion() {
		return tipoReclamacion;
	}

	public void setTipoReclamacion(String tipoReclamacion) {
		this.tipoReclamacion = tipoReclamacion;
	}

	public String getEstadoReclamacion() {
		return estadoReclamacion;
	}

	public void setEstadoReclamacion(String estadoReclamacion) {
		this.estadoReclamacion = estadoReclamacion;
	}

	public String getConvenioResponsable() {
		return convenioResponsable;
	}

	public void setConvenioResponsable(String convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}

	public String getFechaInicialReclamacion() {
		return fechaInicialReclamacion;
	}

	public void setFechaInicialReclamacion(String fechaInicialReclamacion) {
		this.fechaInicialReclamacion = fechaInicialReclamacion;
	}

	public String getFechaFinalReclamacion() {
		return fechaFinalReclamacion;
	}

	public void setFechaFinalReclamacion(String fechaFinalReclamacion) {
		this.fechaFinalReclamacion = fechaFinalReclamacion;
	}

	public String getNroInicialReclamacion() {
		return nroInicialReclamacion;
	}

	public void setNroInicialReclamacion(String nroInicialReclamacion) {
		this.nroInicialReclamacion = nroInicialReclamacion;
	}

	public String getNroFinalReclamacion() {
		return nroFinalReclamacion;
	}

	public void setNroFinalReclamacion(String nroFinalReclamacion) {
		this.nroFinalReclamacion = nroFinalReclamacion;
	}
	
	
	

}
