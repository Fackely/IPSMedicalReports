package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import com.princetonsa.dto.cargos.DtoDetalleCargo;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Dto que contiene las solicitudes de los responsables
 * de la factura odontologica
 * @author axioma
 *
 */
public class DtoSolicitudesResponsableFacturaOdontologica implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * informacion del detalle del cargo
	 */
	private DtoDetalleCargo detalleCargo;  
	
	/**
	 * estado de historia clinica 
	 */
	private InfoDatosInt estadoHC;
	
	/**
	 * codigo y nombre del pool
	 */
	private InfoDatosInt pool;
	
	/**
	 * consecutivo de ordenes medicas
	 */
	private String consecutivoSolicitud;
	
	/**
	 * medico que responde la solicitud
	 */
	private InfoDatosInt medicoResponde;
	
	/**
	 * 
	 */
	private String tipoLiquidacionMedico;
	
	/**
	 * 
	 */
	private double porcentajeParticipacionPool;
	/**
	 * Constructor
	 */
	public DtoSolicitudesResponsableFacturaOdontologica() 
	{
		super();
		this.estadoHC = new InfoDatosInt();
		this.detalleCargo= new DtoDetalleCargo();
		this.pool= new InfoDatosInt();
		this.consecutivoSolicitud="";
		this.medicoResponde= new InfoDatosInt();
		this.tipoLiquidacionMedico="";
		this.porcentajeParticipacionPool= ConstantesBD.codigoNuncaValidoDouble;
	}

	/**
	 * @return the estadoHC
	 */
	public InfoDatosInt getEstadoHC() {
		return estadoHC;
	}

	/**
	 * @param estadoHC the estadoHC to set
	 */
	public void setEstadoHC(InfoDatosInt estadoHC) {
		this.estadoHC = estadoHC;
	}

	/**
	 * @return the detalleCargo
	 */
	public DtoDetalleCargo getDetalleCargo() {
		return detalleCargo;
	}

	/**
	 * @param detalleCargo the detalleCargo to set
	 */
	public void setDetalleCargo(DtoDetalleCargo detalleCargo) {
		this.detalleCargo = detalleCargo;
	}

	/**
	 * @return the pool
	 */
	public InfoDatosInt getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(InfoDatosInt pool) {
		this.pool = pool;
	}

	/**
	 * @return the consecutivoSolicitud
	 */
	public String getConsecutivoSolicitud() {
		return consecutivoSolicitud;
	}

	/**
	 * @param consecutivoSolicitud the consecutivoSolicitud to set
	 */
	public void setConsecutivoSolicitud(String consecutivoSolicitud) {
		this.consecutivoSolicitud = consecutivoSolicitud;
	}

	/**
	 * @return the medicoResponde
	 */
	public InfoDatosInt getMedicoResponde() {
		return medicoResponde;
	}

	/**
	 * @param medicoResponde the medicoResponde to set
	 */
	public void setMedicoResponde(InfoDatosInt medicoResponde) {
		this.medicoResponde = medicoResponde;
	}

	/**
	 * @return the tipoLiquidacionMedico
	 */
	public String getTipoLiquidacionMedico() {
		return tipoLiquidacionMedico;
	}

	/**
	 * @param tipoLiquidacionMedico the tipoLiquidacionMedico to set
	 */
	public void setTipoLiquidacionMedico(String tipoLiquidacionMedico) {
		this.tipoLiquidacionMedico = tipoLiquidacionMedico;
	}

	/**
	 * @return the porcentajeParticipacionPool
	 */
	public double getPorcentajeParticipacionPool() {
		return porcentajeParticipacionPool;
	}

	/**
	 * @param porcentajeParticipacionPool the porcentajeParticipacionPool to set
	 */
	public void setPorcentajeParticipacionPool(double porcentajeParticipacionPool) {
		this.porcentajeParticipacionPool = porcentajeParticipacionPool;
	}

}