package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoProcedimientosEpicrisis implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private int numeroSolicitud;
	
	
	/**
	 * 
	 */
	private String fechaRespuesta;
	
	/**
	 * 
	 */
	private String horaRespuesta;
	
	/**
	 * 
	 */
	private String responsableRespuesta;
	
	/**
	 * 
	 */
	private String fechaOrden;
	
	/**
	 * 
	 */
	private String horaOrden;
	
	/**
	 * 
	 */
	private String responsableOrden;
	
	/**
	 * 
	 */
	private String especialidadOrdena;
	
	/**
	 * 
	 */
	private String centroCostoOrdena;
	
	/**
	 * 
	 */
	private String centroCostoEjecuta;
	
	/**
	 * 
	 */
	private String especialidadResponde;
	
	/**
	 * 
	 */
	private String servicio;
	
	/**
	 * 
	 */
	private String interpretacion;

	/**
	 * 
	 */
	private boolean incluyeServiciosArticulos;
	
	/**
	 * 
	 */
	private String justificacionNoPos;
	
	
	/**
	 * 
	 *
	 */
	public DtoProcedimientosEpicrisis() 
	{
		this.numeroSolicitud=ConstantesBD.codigoNuncaValido;
		this.fechaRespuesta="";
		this.horaRespuesta="";
		this.responsableRespuesta="";
		this.fechaOrden="";
		this.horaOrden="";
		this.responsableOrden="";
		this.especialidadOrdena="";
		this.centroCostoOrdena="";
		this.centroCostoEjecuta="";
		this.especialidadResponde="";
		this.servicio="";
		this.interpretacion="";
		this.incluyeServiciosArticulos=false;
		this.justificacionNoPos="";
	}

	/**
	 * @return the centroCostoEjecuta
	 */
	public String getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}

	/**
	 * @param centroCostoEjecuta the centroCostoEjecuta to set
	 */
	public void setCentroCostoEjecuta(String centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}

	/**
	 * @return the centroCostoOrdena
	 */
	public String getCentroCostoOrdena() {
		return centroCostoOrdena;
	}

	/**
	 * @param centroCostoOrdena the centroCostoOrdena to set
	 */
	public void setCentroCostoOrdena(String centroCostoOrdena) {
		this.centroCostoOrdena = centroCostoOrdena;
	}

	/**
	 * @return the especialidadOrdena
	 */
	public String getEspecialidadOrdena() {
		return especialidadOrdena;
	}

	/**
	 * @param especialidadOrdena the especialidadOrdena to set
	 */
	public void setEspecialidadOrdena(String especialidadOrdena) {
		this.especialidadOrdena = especialidadOrdena;
	}

	/**
	 * @return the especialidadResponde
	 */
	public String getEspecialidadResponde() {
		return especialidadResponde;
	}

	/**
	 * @param especialidadResponde the especialidadResponde to set
	 */
	public void setEspecialidadResponde(String especialidadResponde) {
		this.especialidadResponde = especialidadResponde;
	}

	/**
	 * @return the fechaOrden
	 */
	public String getFechaOrden() {
		return fechaOrden;
	}

	/**
	 * @param fechaOrden the fechaOrden to set
	 */
	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	/**
	 * @return the fechaRespuesta
	 */
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}

	/**
	 * @param fechaRespuesta the fechaRespuesta to set
	 */
	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	/**
	 * @return the horaOrden
	 */
	public String getHoraOrden() {
		return horaOrden;
	}

	/**
	 * @param horaOrden the horaOrden to set
	 */
	public void setHoraOrden(String horaOrden) {
		this.horaOrden = horaOrden;
	}

	/**
	 * @return the horaRespuesta
	 */
	public String getHoraRespuesta() {
		return horaRespuesta;
	}

	/**
	 * @param horaRespuesta the horaRespuesta to set
	 */
	public void setHoraRespuesta(String horaRespuesta) {
		this.horaRespuesta = horaRespuesta;
	}

	/**
	 * @return the interpretacion
	 */
	public String getInterpretacion() {
		return interpretacion;
	}

	/**
	 * @param interpretacion the interpretacion to set
	 */
	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	/**
	 * @return the responsableOrden
	 */
	public String getResponsableOrden() {
		return responsableOrden;
	}

	/**
	 * @param responsableOrden the responsableOrden to set
	 */
	public void setResponsableOrden(String responsableOrden) {
		this.responsableOrden = responsableOrden;
	}

	/**
	 * @return the responsableRespuesta
	 */
	public String getResponsableRespuesta() {
		return responsableRespuesta;
	}

	/**
	 * @param responsableRespuesta the responsableRespuesta to set
	 */
	public void setResponsableRespuesta(String responsableRespuesta) {
		this.responsableRespuesta = responsableRespuesta;
	}

	/**
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the incluyeServiciosArticulos
	 */
	public boolean isIncluyeServiciosArticulos() {
		return incluyeServiciosArticulos;
	}

	/**
	 * @param incluyeServiciosArticulos the incluyeServiciosArticulos to set
	 */
	public void setIncluyeServiciosArticulos(boolean incluyeServiciosArticulos) {
		this.incluyeServiciosArticulos = incluyeServiciosArticulos;
	}
	
	/**
	 * @return the incluyeServiciosArticulos
	 */
	public boolean getIncluyeServiciosArticulos() {
		return incluyeServiciosArticulos;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the justificacionNoPos
	 */
	public String getJustificacionNoPos() {
		return justificacionNoPos;
	}

	/**
	 * @param justificacionNoPos the justificacionNoPos to set
	 */
	public void setJustificacionNoPos(String justificacionNoPos) {
		this.justificacionNoPos = justificacionNoPos;
	}
	
}
