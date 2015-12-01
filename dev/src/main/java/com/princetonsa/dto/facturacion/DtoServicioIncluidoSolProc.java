package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoServicioIncluidoSolProc implements Serializable
{
	/**	* Objeto para manejar los logs de esta clase	*/
	private static Logger logger = Logger.getLogger(DtoServicioIncluidoSolProc.class);
	
	/**
	 * 
	 */
	private int solicitudPpal;
	
	/**
	 * 
	 */
	private int solicitudIncluida;
	
	/**
	 * 
	 */
	private int servicioPpal;
	
	/**
	 * 
	 */
	private String codigoPropietarioPpal;
	
	/**
	 * 
	 */
	private String descServicioPpal;
	
	/**
	 * 
	 */
	private int servicioIncluido;
	
	/**
	 * 
	 */
	private String codigoPropietarioIncluido;
	
	/**
	 * 
	 */
	private String descServicioIncluido;
	
	/**
	 * 
	 */
	private int centroCostoEjecuta;
	
	/**
	 * 
	 */
	private String descCentroCostoEjecuta;
	
	/**
	 * 
	 */
	private String usuarioModifica;

	/**
	 * 
	 */
	private boolean esPos;
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 
	 */
	public DtoServicioIncluidoSolProc() 
	{
		this.centroCostoEjecuta = ConstantesBD.codigoNuncaValido;
		this.descCentroCostoEjecuta= "";
		this.servicioIncluido = ConstantesBD.codigoNuncaValido;
		this.descServicioIncluido="";
		this.codigoPropietarioIncluido="";
		this.servicioPpal = ConstantesBD.codigoNuncaValido;
		this.descServicioPpal="";
		this.codigoPropietarioPpal="";
		this.solicitudIncluida = ConstantesBD.codigoNuncaValido;
		this.solicitudPpal = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = "";
		this.esPos=false;
		this.cantidad=ConstantesBD.codigoNuncaValido;
	}

	public void log()
	{
		logger.info("solPpal->"+this.getSolicitudPpal()+" solInclu->"+this.getSolicitudIncluida()+" servPppal->"+this.getServicioPpal()+" servIncl->"+this.getServicioIncluido()+" centroCostoEjecuta->"+this.getCentroCostoEjecuta()+" esPos->"+this.getEsPos());
	}
	
	/**
	 * @return the solicitudPpal
	 */
	public int getSolicitudPpal() {
		return solicitudPpal;
	}

	/**
	 * @param solicitudPpal the solicitudPpal to set
	 */
	public void setSolicitudPpal(int solicitudPpal) {
		this.solicitudPpal = solicitudPpal;
	}

	/**
	 * @return the solicitudIncluida
	 */
	public int getSolicitudIncluida() {
		return solicitudIncluida;
	}

	/**
	 * @param solicitudIncluida the solicitudIncluida to set
	 */
	public void setSolicitudIncluida(int solicitudIncluida) {
		this.solicitudIncluida = solicitudIncluida;
	}

	/**
	 * @return the servicioPpal
	 */
	public int getServicioPpal() {
		return servicioPpal;
	}

	/**
	 * @param servicioPpal the servicioPpal to set
	 */
	public void setServicioPpal(int servicioPpal) {
		this.servicioPpal = servicioPpal;
	}

	/**
	 * @return the servicioIncluido
	 */
	public int getServicioIncluido() {
		return servicioIncluido;
	}

	/**
	 * @param servicioIncluido the servicioIncluido to set
	 */
	public void setServicioIncluido(int servicioIncluido) {
		this.servicioIncluido = servicioIncluido;
	}

	/**
	 * @return the centroCostoEjecuta
	 */
	public int getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}

	/**
	 * @param centroCostoEjecuta the centroCostoEjecuta to set
	 */
	public void setCentroCostoEjecuta(int centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the esPos
	 */
	public boolean getEsPos() {
		return esPos;
	}

	/**
	 * @param esPos the esPos to set
	 */
	public void setEsPos(boolean esPos) {
		this.esPos = esPos;
	}

	/**
	 * @return the codigoPropietarioPpal
	 */
	public String getCodigoPropietarioPpal() {
		return codigoPropietarioPpal;
	}

	/**
	 * @param codigoPropietarioPpal the codigoPropietarioPpal to set
	 */
	public void setCodigoPropietarioPpal(String codigoPropietarioPpal) {
		this.codigoPropietarioPpal = codigoPropietarioPpal;
	}

	/**
	 * @return the descServicioPpal
	 */
	public String getDescServicioPpal() {
		return descServicioPpal;
	}

	/**
	 * @param descServicioPpal the descServicioPpal to set
	 */
	public void setDescServicioPpal(String descServicioPpal) {
		this.descServicioPpal = descServicioPpal;
	}

	/**
	 * @return the codigoPropietarioIncluido
	 */
	public String getCodigoPropietarioIncluido() {
		return codigoPropietarioIncluido;
	}

	/**
	 * @param codigoPropietarioIncluido the codigoPropietarioIncluido to set
	 */
	public void setCodigoPropietarioIncluido(String codigoPropietarioIncluido) {
		this.codigoPropietarioIncluido = codigoPropietarioIncluido;
	}

	/**
	 * @return the descServicioIncluido
	 */
	public String getDescServicioIncluido() {
		return descServicioIncluido;
	}

	/**
	 * @param descServicioIncluido the descServicioIncluido to set
	 */
	public void setDescServicioIncluido(String descServicioIncluido) {
		this.descServicioIncluido = descServicioIncluido;
	}

	/**
	 * @return the descCentroCostoEjecuta
	 */
	public String getDescCentroCostoEjecuta() {
		return descCentroCostoEjecuta;
	}

	/**
	 * @param descCentroCostoEjecuta the descCentroCostoEjecuta to set
	 */
	public void setDescCentroCostoEjecuta(String descCentroCostoEjecuta) {
		this.descCentroCostoEjecuta = descCentroCostoEjecuta;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
