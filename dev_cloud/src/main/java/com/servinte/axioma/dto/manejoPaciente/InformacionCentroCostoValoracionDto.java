package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
/**
 * Dto para almacenar la informacion del area en la que se encuentra el paciente
 * para las valoraciones
 * @author javrammo
 *
 */
public class InformacionCentroCostoValoracionDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8463127407845396104L;
	/**
	 * id del centro de costo o area
	 */
	private Integer idCentroCosto;
	/**
	 * descripcion del centro de costo
	 */
	private String descripcionCentroCosto;
	/**
	 * id del tipo de monitoreo
	 */
	private Integer idTipoMonitoreo;
	/**
	 * descripcion del tipo Monitoreo
	 */
	private String descripcionTipoMonitoreo;
	
	
	/**
	 * @return the idCentroCosto
	 */
	public Integer getIdCentroCosto() {
		return idCentroCosto;
	}
	/**
	 * @param idCentroCosto the idCentroCosto to set
	 */
	public void setIdCentroCosto(Integer idCentroCosto) {
		this.idCentroCosto = idCentroCosto;
	}
	/**
	 * @return the descripcionCentroCosto
	 */
	public String getDescripcionCentroCosto() {
		return descripcionCentroCosto;
	}
	/**
	 * @param descripcionCentroCosto the descripcionCentroCosto to set
	 */
	public void setDescripcionCentroCosto(String descripcionCentroCosto) {
		this.descripcionCentroCosto = descripcionCentroCosto;
	}
	/**
	 * @return the idTipoMonitoreo
	 */
	public Integer getIdTipoMonitoreo() {
		return idTipoMonitoreo;
	}
	/**
	 * @param idTipoMonitoreo the idTipoMonitoreo to set
	 */
	public void setIdTipoMonitoreo(Integer idTipoMonitoreo) {
		this.idTipoMonitoreo = idTipoMonitoreo;
	}
	/**
	 * @return the descripcionTipoMonitoreo
	 */
	public String getDescripcionTipoMonitoreo() {
		return descripcionTipoMonitoreo;
	}
	/**
	 * @param descripcionTipoMonitoreo the descripcionTipoMonitoreo to set
	 */
	public void setDescripcionTipoMonitoreo(String descripcionTipoMonitoreo) {
		this.descripcionTipoMonitoreo = descripcionTipoMonitoreo;
	}
	
	

}
