package com.servinte.axioma.orm;

// Generated Dec 17, 2010 9:37:44 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * LogActiinactiUsuarios generated by hbm2java
 */
public class LogActiinactiUsuarios implements java.io.Serializable {

	private int codigo;
	private Usuarios usuariosByLoginUsuario;
	private Usuarios usuariosByUsuarioProcesa;
	private Date fechaProceso;
	private String horaProceso;
	private String tipoInactivacion;
	private String estadoUsuario;

	public LogActiinactiUsuarios() {
	}

	public LogActiinactiUsuarios(int codigo, Usuarios usuariosByLoginUsuario,
			Date fechaProceso, String horaProceso, String tipoInactivacion,
			String estadoUsuario) {
		this.codigo = codigo;
		this.usuariosByLoginUsuario = usuariosByLoginUsuario;
		this.fechaProceso = fechaProceso;
		this.horaProceso = horaProceso;
		this.tipoInactivacion = tipoInactivacion;
		this.estadoUsuario = estadoUsuario;
	}

	public LogActiinactiUsuarios(int codigo, Usuarios usuariosByLoginUsuario,
			Usuarios usuariosByUsuarioProcesa, Date fechaProceso,
			String horaProceso, String tipoInactivacion, String estadoUsuario) {
		this.codigo = codigo;
		this.usuariosByLoginUsuario = usuariosByLoginUsuario;
		this.usuariosByUsuarioProcesa = usuariosByUsuarioProcesa;
		this.fechaProceso = fechaProceso;
		this.horaProceso = horaProceso;
		this.tipoInactivacion = tipoInactivacion;
		this.estadoUsuario = estadoUsuario;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuariosByLoginUsuario() {
		return this.usuariosByLoginUsuario;
	}

	public void setUsuariosByLoginUsuario(Usuarios usuariosByLoginUsuario) {
		this.usuariosByLoginUsuario = usuariosByLoginUsuario;
	}

	public Usuarios getUsuariosByUsuarioProcesa() {
		return this.usuariosByUsuarioProcesa;
	}

	public void setUsuariosByUsuarioProcesa(Usuarios usuariosByUsuarioProcesa) {
		this.usuariosByUsuarioProcesa = usuariosByUsuarioProcesa;
	}

	public Date getFechaProceso() {
		return this.fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getHoraProceso() {
		return this.horaProceso;
	}

	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}

	public String getTipoInactivacion() {
		return this.tipoInactivacion;
	}

	public void setTipoInactivacion(String tipoInactivacion) {
		this.tipoInactivacion = tipoInactivacion;
	}

	public String getEstadoUsuario() {
		return this.estadoUsuario;
	}

	public void setEstadoUsuario(String estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

}
