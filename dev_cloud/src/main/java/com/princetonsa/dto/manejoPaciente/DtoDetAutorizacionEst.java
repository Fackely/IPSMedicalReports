package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.mundo.UsuarioBasico;

public class DtoDetAutorizacionEst implements Serializable{
	
	private String codigo;
	private String detAutorizacion;
	private String numeroSolicitud;
	private String consecutivoSolicitud;
	private String detCargo;
	private String activo;
	
	/**
	 * Atributo para identificar si se debe preguntar o no
	 */
	private String preguntar = "";
	
	/**
	 * Atributo para saber si desea remplazar la respuesta o no 
	 */
	private boolean remplazarResp;
	
	/**
	 * Constructor
	 */
	public DtoDetAutorizacionEst()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigo = "";
		this.detAutorizacion = "";
		this.numeroSolicitud = "";
		this.detCargo = "";
		this.activo = ConstantesBD.acronimoNo;
		this.preguntar = "";
		this.remplazarResp = false;
		this.consecutivoSolicitud = "";
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the detAutorizacion
	 */
	public String getDetAutorizacion() {
		return detAutorizacion;
	}

	/**
	 * @param detAutorizacion the detAutorizacion to set
	 */
	public void setDetAutorizacion(String detAutorizacion) {
		this.detAutorizacion = detAutorizacion;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the detCargo
	 */
	public String getDetCargo() {
		return detCargo;
	}

	/**
	 * @param detCargo the detCargo to set
	 */
	public void setDetCargo(String detCargo) {
		this.detCargo = detCargo;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the preguntar
	 */
	public String getPreguntar() {
		return preguntar;
	}

	/**
	 * @param preguntar the preguntar to set
	 */
	public void setPreguntar(String preguntar) {
		this.preguntar = preguntar;
	}

	/**
	 * @return the remplazarResp
	 */
	public boolean isRemplazarResp() {
		return remplazarResp;
	}

	/**
	 * @param remplazarResp the remplazarResp to set
	 */
	public void setRemplazarResp(boolean remplazarResp) {
		this.remplazarResp = remplazarResp;
	}

	public String getConsecutivoSolicitud() {
		return consecutivoSolicitud;
	}

	public void setConsecutivoSolicitud(String consecutivoSolicitud) {
		this.consecutivoSolicitud = consecutivoSolicitud;
	}
	
}
