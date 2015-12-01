package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoArticuloIncluidoSolProc implements Serializable
{
	/**	* Objeto para manejar los logs de esta clase	*/
	private static Logger logger = Logger.getLogger(DtoArticuloIncluidoSolProc.class);
	
	/**
	 * 
	 */
	private int solicitudPpal;
	
	/**
	 * 
	 * */
	private int consecutivoOrdenMedPpal;
	
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
	 * */
	private String descripcionServicioPpal;
	
	/**
	 * 
	 * */
	private int codigoEspecialidadServPpal;	

	/**
	 * 
	 */
	private int articuloIncluido;
	
	/**
	 * 
	 * */
	private String descripcionArticuloIncluido;
	
	/**
	 * 
	 * */
	private String codigoInterfazArtIncluido;	
			
	/**
	 * 
	 */
	private int farmacia;	

	/**
	 * 
	 * */
	private String descripcionFarmacia;
	
	/**
	 * 
	 */
	private String usuarioModifica;

	/**
	 * 
	 */
	private boolean esServicioIncluido;
 
	/**
	 * 
	 */
	private int cantidadMaxima;
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 
	 * */
	private boolean esPos;
	
	/**
	 * 
	 * */
	private boolean tieneJusticacionNoPos;

	/**
	 * 
	 */
	public DtoArticuloIncluidoSolProc() 
	{
		this.articuloIncluido = ConstantesBD.codigoNuncaValido;
		this.codigoInterfazArtIncluido = "";
		this.descripcionArticuloIncluido = "";
		this.cantidad = ConstantesBD.codigoNuncaValido;
		this.cantidadMaxima = ConstantesBD.codigoNuncaValido;
		this.esServicioIncluido = false;
		this.farmacia = ConstantesBD.codigoNuncaValido;
		this.servicioPpal = ConstantesBD.codigoNuncaValido;
		this.codigoEspecialidadServPpal = ConstantesBD.codigoNuncaValido;
		this.descripcionServicioPpal = "";
		this.consecutivoOrdenMedPpal = ConstantesBD.codigoNuncaValido;
		this.solicitudIncluida = ConstantesBD.codigoNuncaValido;
		this.solicitudPpal = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = "";
		this.esPos = false;	
		this.tieneJusticacionNoPos = false;
	}

	/**
	 * 
	 */
	public void log()
	{
		logger.info("SOLICITUD PPAL-->"+this.getSolicitudPpal()+" SOL INCLUIDA-->"+this.getSolicitudIncluida()+" SERV PPAL-*>"+this.getServicioPpal()+" ARTICULO INCLI->"+this.getArticuloIncluido()+" FARMACIA->"+this.getFarmacia()+" CANTIDAD->"+this.getCantidad()+" CANTIDAD MAXIMA->"+this.getCantidadMaxima()+" esServicioIncluido->"+this.getEsServicioIncluido()+" usuario->"+this.getUsuarioModifica());
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
	 * @return the articuloIncluido
	 */
	public int getArticuloIncluido() {
		return articuloIncluido;
	}

	/**
	 * @param articuloIncluido the articuloIncluido to set
	 */
	public void setArticuloIncluido(int articuloIncluido) {
		this.articuloIncluido = articuloIncluido;
	}

	/**
	 * @return the farmacia
	 */
	public int getFarmacia() {
		return farmacia;
	}

	/**
	 * @param farmacia the farmacia to set
	 */
	public void setFarmacia(int farmacia) {
		this.farmacia = farmacia;
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
	 * @return the esServicioIncluido
	 */
	public boolean getEsServicioIncluido() {
		return esServicioIncluido;
	}

	/**
	 * @param esServicioIncluido the esServicioIncluido to set
	 */
	public void setEsServicioIncluido(boolean esServicioIncluido) {
		this.esServicioIncluido = esServicioIncluido;
	}

	/**
	 * @return the cantidadMaxima
	 */
	public int getCantidadMaxima() {
		return cantidadMaxima;
	}

	/**
	 * @param cantidadMaxima the cantidadMaxima to set
	 */
	public void setCantidadMaxima(int cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
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

	/**
	 * @return the descripcionArticuloIncluido
	 */
	public String getDescripcionArticuloIncluido() {
		return descripcionArticuloIncluido;
	}

	/**
	 * @param descripcionArticuloIncluido the descripcionArticuloIncluido to set
	 */
	public void setDescripcionArticuloIncluido(String descripcionArticuloIncluido) {
		this.descripcionArticuloIncluido = descripcionArticuloIncluido;
	}

	/**
	 * @return the codigoInterfazArtIncluido
	 */
	public String getCodigoInterfazArtIncluido() {
		return codigoInterfazArtIncluido;
	}

	/**
	 * @param codigoInterfazArtIncluido the codigoInterfazArtIncluido to set
	 */
	public void setCodigoInterfazArtIncluido(String codigoInterfazArtIncluido) {
		this.codigoInterfazArtIncluido = codigoInterfazArtIncluido;
	}

	/**
	 * @return the consecutivoOrdenMedPpal
	 */
	public int getConsecutivoOrdenMedPpal() {
		return consecutivoOrdenMedPpal;
	}

	/**
	 * @param consecutivoOrdenMedPpal the consecutivoOrdenMedPpal to set
	 */
	public void setConsecutivoOrdenMedPpal(int consecutivoOrdenMedPpal) {
		this.consecutivoOrdenMedPpal = consecutivoOrdenMedPpal;
	}

	/**
	 * @return the descripcionServicioPpal
	 */
	public String getDescripcionServicioPpal() {
		return descripcionServicioPpal;
	}

	/**
	 * @param descripcionServicioPpal the descripcionServicioPpal to set
	 */
	public void setDescripcionServicioPpal(String descripcionServicioPpal) {
		this.descripcionServicioPpal = descripcionServicioPpal;
	}
	
	/**
	 * @return the codigoEspecialidadServPpal
	 */
	public int getCodigoEspecialidadServPpal() {
		return codigoEspecialidadServPpal;
	}

	/**
	 * @param codigoEspecialidadServPpal the codigoEspecialidadServPpal to set
	 */
	public void setCodigoEspecialidadServPpal(int codigoEspecialidadServPpal) {
		this.codigoEspecialidadServPpal = codigoEspecialidadServPpal;
	}

	/**
	 * @return the descripcionFarmacia
	 */
	public String getDescripcionFarmacia() {
		return descripcionFarmacia;
	}

	/**
	 * @param descripcionFarmacia the descripcionFarmacia to set
	 */
	public void setDescripcionFarmacia(String descripcionFarmacia) {
		this.descripcionFarmacia = descripcionFarmacia;
	}
	
	/**
	 * @return the esPos
	 */
	public boolean isEsPos() {
		return esPos;
	}

	/**
	 * @param esPos the esPos to set
	 */
	public void setEsPos(boolean esPos) {
		this.esPos = esPos;
	}	

	/**
	 * @return the tieneJusticacionNoPos
	 */
	public boolean isTieneJusticacionNoPos() {
		return tieneJusticacionNoPos;
	}

	/**
	 * @param tieneJusticacionNoPos the tieneJusticacionNoPos to set
	 */
	public void setTieneJusticacionNoPos(boolean tieneJusticacionNoPos) {
		this.tieneJusticacionNoPos = tieneJusticacionNoPos;
	}
}