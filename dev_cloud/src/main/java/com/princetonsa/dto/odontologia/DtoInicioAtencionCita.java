package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import com.princetonsa.mundo.UsuarioBasico;

public class DtoInicioAtencionCita implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String horaInicioAtencion;
	private Date fechaInicioAtencion;
	private String horaInicioAtencionGuardada;
	private Date fechaInicioAtencionGuardada;
	private Date fechaModificacion;
	private String horaModificacion;
	private String loginUsuario;
	private long codigoCita;
	private UsuarioBasico usuario;
	private DtoCitaOdontologica cita;
	

	
	public UsuarioBasico getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}
	public long getCodigoCita() {
		return codigoCita;
	}
	public void setCodigoCita(long codigoCita) {
		this.codigoCita = codigoCita;
	}
	public String getHoraInicioAtencion() {
		return horaInicioAtencion;
	}
	public void setHoraInicioAtencion(String horaInicioAtencion) {
		this.horaInicioAtencion = horaInicioAtencion;
	}
	public Date getFechaInicioAtencion() {
		return fechaInicioAtencion;
	}
	public void setFechaInicioAtencion(Date fechaInicioAtencion) {
		this.fechaInicioAtencion = fechaInicioAtencion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getHoraModificacion() {
		return horaModificacion;
	}
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setCita(DtoCitaOdontologica cita) {
		this.cita = cita;
	}
	public DtoCitaOdontologica getCita() {
		return cita;
	}
	public void setHoraInicioAtencionGuardada(String horaInicioAtencionGuardada) {
		this.horaInicioAtencionGuardada = horaInicioAtencionGuardada;
	}
	public String getHoraInicioAtencionGuardada() {
		return horaInicioAtencionGuardada;
	}
	public void setFechaInicioAtencionGuardada(
			Date fechaInicioAtencionGuardada) {
		this.fechaInicioAtencionGuardada = fechaInicioAtencionGuardada;
	}
	public Date getFechaInicioAtencionGuardada() {
		return fechaInicioAtencionGuardada;
	}
	
	

}
