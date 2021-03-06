package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * AsociosCuenta generated by hbm2java
 */
public class AsociosCuenta implements java.io.Serializable {

	private int codigo;
	private Ingresos ingresos;
	private Cuentas cuentasByCuentaInicial;
	private Usuarios usuariosByUsuarioDesasocio;
	private Usuarios usuariosByUsuario;
	private Cuentas cuentasByCuentaFinal;
	private Date fecha;
	private Date hora;
	private boolean activo;
	private Date fechaDesasocio;
	private String horaDesasocio;

	public AsociosCuenta() {
	}

	public AsociosCuenta(int codigo, Ingresos ingresos,
			Cuentas cuentasByCuentaInicial, Usuarios usuariosByUsuario,
			Date fecha, Date hora, boolean activo) {
		this.codigo = codigo;
		this.ingresos = ingresos;
		this.cuentasByCuentaInicial = cuentasByCuentaInicial;
		this.usuariosByUsuario = usuariosByUsuario;
		this.fecha = fecha;
		this.hora = hora;
		this.activo = activo;
	}

	public AsociosCuenta(int codigo, Ingresos ingresos,
			Cuentas cuentasByCuentaInicial,
			Usuarios usuariosByUsuarioDesasocio, Usuarios usuariosByUsuario,
			Cuentas cuentasByCuentaFinal, Date fecha, Date hora,
			boolean activo, Date fechaDesasocio, String horaDesasocio) {
		this.codigo = codigo;
		this.ingresos = ingresos;
		this.cuentasByCuentaInicial = cuentasByCuentaInicial;
		this.usuariosByUsuarioDesasocio = usuariosByUsuarioDesasocio;
		this.usuariosByUsuario = usuariosByUsuario;
		this.cuentasByCuentaFinal = cuentasByCuentaFinal;
		this.fecha = fecha;
		this.hora = hora;
		this.activo = activo;
		this.fechaDesasocio = fechaDesasocio;
		this.horaDesasocio = horaDesasocio;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public Cuentas getCuentasByCuentaInicial() {
		return this.cuentasByCuentaInicial;
	}

	public void setCuentasByCuentaInicial(Cuentas cuentasByCuentaInicial) {
		this.cuentasByCuentaInicial = cuentasByCuentaInicial;
	}

	public Usuarios getUsuariosByUsuarioDesasocio() {
		return this.usuariosByUsuarioDesasocio;
	}

	public void setUsuariosByUsuarioDesasocio(
			Usuarios usuariosByUsuarioDesasocio) {
		this.usuariosByUsuarioDesasocio = usuariosByUsuarioDesasocio;
	}

	public Usuarios getUsuariosByUsuario() {
		return this.usuariosByUsuario;
	}

	public void setUsuariosByUsuario(Usuarios usuariosByUsuario) {
		this.usuariosByUsuario = usuariosByUsuario;
	}

	public Cuentas getCuentasByCuentaFinal() {
		return this.cuentasByCuentaFinal;
	}

	public void setCuentasByCuentaFinal(Cuentas cuentasByCuentaFinal) {
		this.cuentasByCuentaFinal = cuentasByCuentaFinal;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Date getFechaDesasocio() {
		return this.fechaDesasocio;
	}

	public void setFechaDesasocio(Date fechaDesasocio) {
		this.fechaDesasocio = fechaDesasocio;
	}

	public String getHoraDesasocio() {
		return this.horaDesasocio;
	}

	public void setHoraDesasocio(String horaDesasocio) {
		this.horaDesasocio = horaDesasocio;
	}

}
