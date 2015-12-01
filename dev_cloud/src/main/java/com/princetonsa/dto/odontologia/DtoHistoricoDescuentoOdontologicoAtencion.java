package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadFecha;

public class DtoHistoricoDescuentoOdontologicoAtencion implements Serializable{
	
	

	private double codigo;
	private double consecutivo;	
	private InfoDatosInt centroAtencion;
	private double porcentajeDescuento;
	private double porcentajeDescuentoMod;
	private int diasVigencia;
	private int diasVigenciaMod;
	private InfoDatosDouble nivelAutorizacion;
	private InfoDatosDouble nivelAutorizacionMod;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	private String eliminado;
	
	
	public DtoHistoricoDescuentoOdontologicoAtencion(){
	  this.reset();	
	}
	
	void reset(){
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = new InfoDatosInt();
		this.porcentajeDescuentoMod = ConstantesBD.codigoNuncaValido;
		this.porcentajeDescuento = ConstantesBD.codigoNuncaValido;
		this.nivelAutorizacion = new InfoDatosDouble();
		this.nivelAutorizacionMod = new InfoDatosDouble();
		this.diasVigencia = ConstantesBD.codigoNuncaValido;
		this.diasVigenciaMod = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
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
	 * @return the consecutivo
	 */
	public double getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(double consecutivo) {
		this.consecutivo = consecutivo;
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
	 * @return the porcentajeDescuento
	 */
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the porcentajeDescuentoMod
	 */
	public double getPorcentajeDescuentoMod() {
		return porcentajeDescuentoMod;
	}

	/**
	 * @param porcentajeDescuentoMod the porcentajeDescuentoMod to set
	 */
	public void setPorcentajeDescuentoMod(double porcentajeDescuentoMod) {
		this.porcentajeDescuentoMod = porcentajeDescuentoMod;
	}

	/**
	 * @return the diasVigencia
	 */
	public int getDiasVigencia() {
		return diasVigencia;
	}

	/**
	 * @param diasVigencia the diasVigencia to set
	 */
	public void setDiasVigencia(int diasVigencia) {
		this.diasVigencia = diasVigencia;
	}

	/**
	 * @return the diasVigenciaMod
	 */
	public int getDiasVigenciaMod() {
		return diasVigenciaMod;
	}

	/**
	 * @param diasVigenciaMod the diasVigenciaMod to set
	 */
	public void setDiasVigenciaMod(int diasVigenciaMod) {
		this.diasVigenciaMod = diasVigenciaMod;
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
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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

	/**
	 * @return the nivelAutorizacion
	 */
	public InfoDatosDouble getNivelAutorizacion() {
		return nivelAutorizacion;
	}

	/**
	 * @param nivelAutorizacion the nivelAutorizacion to set
	 */
	public void setNivelAutorizacion(InfoDatosDouble nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}

	/**
	 * @return the nivelAutorizacionMod
	 */
	public InfoDatosDouble getNivelAutorizacionMod() {
		return nivelAutorizacionMod;
	}

	/**
	 * @param nivelAutorizacionMod the nivelAutorizacionMod to set
	 */
	public void setNivelAutorizacionMod(InfoDatosDouble nivelAutorizacionMod) {
		this.nivelAutorizacionMod = nivelAutorizacionMod;
	}

}
