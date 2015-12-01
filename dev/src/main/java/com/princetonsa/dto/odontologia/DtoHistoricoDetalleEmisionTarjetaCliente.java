package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;

@SuppressWarnings("serial")
public class DtoHistoricoDetalleEmisionTarjetaCliente implements Serializable , Cloneable {

	private double codigo;
	private double codigoDetalleEmision;
	private String fechaModifica;
	private String horaModifica;
	private InfoDatosInt centroAtencion;
	private double serialInicial;
	private double serialFinal;
	private String usuarioModifica;
	private String usuarioResponsable;
	private String eliminado;
	
	/**
	 * 
	 */
	public DtoHistoricoDetalleEmisionTarjetaCliente(){
		
      this.reset();
	}
	
	/**
	 * 
	 * 
	 */
	
	public void reset(){
		
		 this.codigo = ConstantesBD.codigoNuncaValido;
			this.codigoDetalleEmision = ConstantesBD.codigoNuncaValido;
			this.centroAtencion= new InfoDatosInt();
			
			this.fechaModifica = "";
			this.horaModifica = "";		
			this.usuarioModifica = "";
			this.usuarioResponsable = "";
			this.serialInicial = ConstantesBD.codigoNuncaValido;
			this.serialFinal = ConstantesBD.codigoNuncaValido;
			this.eliminado = "";
	}
	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}
	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}
	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the serialInicial
	 */
	public double getSerialInicial() {
		return serialInicial;
	}
	/**
	 * @param serialInicial the serialInicial to set
	 */
	public void setSerialInicial(double serialInicial) {
		this.serialInicial = serialInicial;
	}
	/**
	 * @return the serialFinal
	 */
	public double getSerialFinal() {
		return serialFinal;
	}
	/**
	 * @param serialFinal the serialFinal to set
	 */
	public void setSerialFinal(double serialFinal) {
		this.serialFinal = serialFinal;
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
	 * @return the usuarioResponsable
	 */
	public String getUsuarioResponsable() {
		return usuarioResponsable;
	}
	/**
	 * @param usuarioResponsable the usuarioResponsable to set
	 */
	public void setUsuarioResponsable(String usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}

	/**
	 * @return the codigoDetalleEmision
	 */
	public double getCodigoDetalleEmision() {
		return codigoDetalleEmision;
	}

	/**
	 * @param codigoDetalleEmision the codigoDetalleEmision to set
	 */
	public void setCodigoDetalleEmision(double codigoDetalleEmision) {
		this.codigoDetalleEmision = codigoDetalleEmision;
	}

	/**
	 * @return the eliminado
	 */
	public String getEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}
}
