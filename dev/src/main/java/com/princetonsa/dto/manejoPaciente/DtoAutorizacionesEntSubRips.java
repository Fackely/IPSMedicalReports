package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase utilizada para almacenar los datos de autorizaciones a las que se les ha realizado
 * el proceso rips entidades subcontratadas con éxito
 * @author Fabián Becerra
 */
public class DtoAutorizacionesEntSubRips implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena la fecha del proceso rips
	 */
	private Date fechaProceso;
	
	/** 
	 * Atributo que almacena la hora del proceso rips
	 */
	private String horaProceso;
	
	/**
	 * Atributo que almacena el login del usuario que realizó el proceso
	 */
	private String loginUsuarioProceso;
	
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getHoraProceso() {
		return horaProceso;
	}
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}
	public void setLoginUsuarioProceso(String loginUsuarioProceso) {
		this.loginUsuarioProceso = loginUsuarioProceso;
	}
	public String getLoginUsuarioProceso() {
		return loginUsuarioProceso;
	}
	
	
}
