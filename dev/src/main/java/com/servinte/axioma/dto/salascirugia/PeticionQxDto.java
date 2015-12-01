package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto que guarda informacion de peticiones de cirugia 
 * @author jeilones
 * @created 17/08/2012
 *
 */
public class PeticionQxDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5222918596765563902L;
	private int codigoPeticion;
	
	private int codigoEstado;
	private String nombreEstado;
	
	private List<ServicioHQxDto>serviciosHQx;
	
	private int codigoPaciente;
	private Date fechaCirugia;
	private ProfesionalHQxDto medicoSolicitante;
	
	private boolean esSolicitud;
	private Boolean finalizada;
	
	private boolean esUrgente;
	private int codigoCuenta;
	
	private SolicitudCirugiaDto solicitudCirugia;
	
	public PeticionQxDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param codigoEstado
	 * @param nombreEstado
	 * @author jeilones
	 * @created 17/08/2012
	 */
	public PeticionQxDto(int codigoEstado, String nombreEstado) {
		super();
		this.codigoEstado = codigoEstado;
		this.nombreEstado = nombreEstado;
	}
	
	public PeticionQxDto(int codigoPeticion, int codigoEstado,
			String nombreEstado, List<ServicioHQxDto> serviciosHQx,
			int codigoPaciente, Date fechaCirugia,
			ProfesionalHQxDto medicoSolicitante, boolean esSolicitud,
			Boolean finalizada, boolean esUrgente, int codigoCuenta,
			SolicitudCirugiaDto solicitudCirugia) {
		this.codigoPeticion = codigoPeticion;
		this.codigoEstado = codigoEstado;
		this.nombreEstado = nombreEstado;
		this.serviciosHQx = serviciosHQx;
		this.codigoPaciente = codigoPaciente;
		this.fechaCirugia = fechaCirugia;
		this.medicoSolicitante = medicoSolicitante;
		this.esSolicitud = esSolicitud;
		this.finalizada = finalizada;
		this.esUrgente = esUrgente;
		this.codigoCuenta = codigoCuenta;
		this.solicitudCirugia = solicitudCirugia;
	}
	/**
	 * @return the codigoEstado
	 */
	public int getCodigoEstado() {
		return codigoEstado;
	}
	/**
	 * @param codigoEstado the codigoEstado to set
	 */
	public void setCodigoEstado(int codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	/**
	 * @return the nombreEstado
	 */
	public String getNombreEstado() {
		return nombreEstado;
	}
	/**
	 * @param nombreEstado the nombreEstado to set
	 */
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	public List<ServicioHQxDto> getServiciosHQx() {
		return serviciosHQx;
	}
	public void setServiciosHQx(List<ServicioHQxDto> serviciosHQx) {
		this.serviciosHQx = serviciosHQx;
	}
	public int getCodigoPeticion() {
		return codigoPeticion;
	}
	public void setCodigoPeticion(int codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}
	public Date getFechaCirugia() {
		return fechaCirugia;
	}
	public void setFechaCirugia(Date fechaCirugia) {
		this.fechaCirugia = fechaCirugia;
	}
	public ProfesionalHQxDto getMedicoSolicitante() {
		return medicoSolicitante;
	}
	public void setMedicoSolicitante(ProfesionalHQxDto medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}
	public boolean isEsSolicitud() {
		return esSolicitud;
	}
	public void setEsSolicitud(boolean esAsociado) {
		this.esSolicitud = esAsociado;
	}
	public SolicitudCirugiaDto getSolicitudCirugia() {
		return solicitudCirugia;
	}
	public void setSolicitudCirugia(SolicitudCirugiaDto solicitudCirugia) {
		this.solicitudCirugia = solicitudCirugia;
	}
	public Boolean getFinalizada() {
		return finalizada;
	}
	public void setFinalizada(Boolean finalizada) {
		this.finalizada = finalizada;
	}
	public boolean isEsUrgente() {
		return esUrgente;
	}
	public void setEsUrgente(boolean esUrgente) {
		this.esUrgente = esUrgente;
	}
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	public int getCodigoCuenta() {
		return codigoCuenta;
	}
	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	
	
}
