package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoEventosParam1E implements Serializable
{
	private String consecutivoPk;
	private String paramGenerales1E;
	private String evento;
	private String codigoEvento;
	private String notasEvento;
	private String activo;
	private String fechaInactivacion;
	private String horaInactivacion;
	private String usuarioInactivacion;
	private String fecha;
	private String hora;
	private String usuario;
	private String nombreEvento;
	
	public void reset()
	{
		this.consecutivoPk="";
		this.paramGenerales1E="";
		this.evento="";
		this.codigoEvento="";
		this.notasEvento="";
		this.activo="";
		this.fechaInactivacion="";
		this.horaInactivacion="";
		this.usuarioInactivacion="";
		this.fecha="";
		this.hora="";
		this.usuario="";
		this.nombreEvento="";
	}

	public String getConsecutivoPk() {
		return consecutivoPk;
	}

	public void setConsecutivoPk(String consecutivoPk) {
		this.consecutivoPk = consecutivoPk;
	}

	public String getParamGenerales1E() {
		return paramGenerales1E;
	}

	public void setParamGenerales1E(String paramGenerales1E) {
		this.paramGenerales1E = paramGenerales1E;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getCodigoEvento() {
		return codigoEvento;
	}

	public void setCodigoEvento(String codigoEvento) {
		this.codigoEvento = codigoEvento;
	}

	public String getNotasEvento() {
		return notasEvento;
	}

	public void setNotasEvento(String notasEvento) {
		this.notasEvento = notasEvento;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getFechaInactivacion() {
		return fechaInactivacion;
	}

	public void setFechaInactivacion(String fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}

	public String getHoraInactivacion() {
		return horaInactivacion;
	}

	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}

	public String getUsuarioInactivacion() {
		return usuarioInactivacion;
	}

	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNombreEvento() {
		return nombreEvento;
	}

	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}
}