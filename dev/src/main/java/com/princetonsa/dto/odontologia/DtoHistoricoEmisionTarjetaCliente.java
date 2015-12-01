package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;

@SuppressWarnings("serial")
public class DtoHistoricoEmisionTarjetaCliente implements Serializable{

	private double codigo;
	private double codigoEmisionTarjeta;
	private String fechaModifica;
	private String horaModifica;
	private InfoDatosStr tipoTarjeta;
	private double serialInicial;
	private double serialFinal;
	private String usuarioModifica;
	private String eliminado;
	
	public DtoHistoricoEmisionTarjetaCliente (){
		this.reset();
	}

	
	/**
	 * 
	 */
	
	public void reset(){
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.codigoEmisionTarjeta =  ConstantesBD.codigoNuncaValido;
		this.tipoTarjeta = new InfoDatosStr();
		
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
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
	 * @return the codigoEmisionTargeta
	 */
	public double getCodigoEmisionTarjeta() {
		return codigoEmisionTarjeta;
	}


	/**
	 * @param codigoEmisionTargeta the codigoEmisionTargeta to set
	 */
	public void setCodigoEmisionTarjeta(double codigoEmisionTargeta) {
		this.codigoEmisionTarjeta = codigoEmisionTargeta;
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
	 * @return the tipoTargeta
	 */
	public InfoDatosStr getTipoTarjeta() {
		return tipoTarjeta;
	}


	/**
	 * @param tipoTargeta the tipoTargeta to set
	 */
	public void setTipoTarjeta(InfoDatosStr tipoTargeta) {
		this.tipoTarjeta = tipoTargeta;
	}
	
}
