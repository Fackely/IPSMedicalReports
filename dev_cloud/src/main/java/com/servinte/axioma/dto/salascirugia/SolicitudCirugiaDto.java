/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class SolicitudCirugiaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 893702409535983156L;
	
	private int numeroSolicitud;
	private int consecutivoSolicitud;
	
	private List<ServicioHQxDto>serviciosHQx;
	private EstadoSolicitudDto estadoSolicitud;
	
	private EspecialidadDto especialidadSolicita;
	
	private boolean esUrgente;
	private int codigoCuenta;
	
	private int codigoCentroCostoSolicitado;
	
	private Date fechaSolicitud;
	
	public SolicitudCirugiaDto() {
	}
	
	public SolicitudCirugiaDto(int numeroSolicitud, int consecutivoSolicitud,
			List<ServicioHQxDto> serviciosHQx,
			EstadoSolicitudDto estadoSolicitud,
			EspecialidadDto especialidadSolicita, boolean esUrgente,
			int codigoCuenta) {
		this.numeroSolicitud = numeroSolicitud;
		this.consecutivoSolicitud = consecutivoSolicitud;
		this.serviciosHQx = serviciosHQx;
		this.estadoSolicitud = estadoSolicitud;
		this.especialidadSolicita = especialidadSolicita;
		this.esUrgente = esUrgente;
		this.codigoCuenta = codigoCuenta;
	}
	public List<ServicioHQxDto> getServiciosHQx() {
		return serviciosHQx;
	}
	public void setServiciosHQx(List<ServicioHQxDto> serviciosHQx) {
		this.serviciosHQx = serviciosHQx;
	}
	public EstadoSolicitudDto getEstadoSolicitud() {
		return estadoSolicitud;
	}
	public void setEstadoSolicitud(EstadoSolicitudDto estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public boolean isEsUrgente() {
		return esUrgente;
	}
	public void setEsUrgente(boolean esUrgente) {
		this.esUrgente = esUrgente;
	}
	public int getCodigoCuenta() {
		return codigoCuenta;
	}
	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	public int getConsecutivoSolicitud() {
		return consecutivoSolicitud;
	}
	public void setConsecutivoSolicitud(int consecutivoSolicitud) {
		this.consecutivoSolicitud = consecutivoSolicitud;
	}
	public EspecialidadDto getEspecialidadSolicita() {
		return especialidadSolicita;
	}
	public void setEspecialidadSolicita(EspecialidadDto especialidadSolicita) {
		this.especialidadSolicita = especialidadSolicita;
	}
	public int getCodigoCentroCostoSolicitado() {
		return codigoCentroCostoSolicitado;
	}
	public void setCodigoCentroCostoSolicitado(int codigoCentroCostoSolicitado) {
		this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
	}

	/**
	 * @return the fechaSolicitud
	 */
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

}
