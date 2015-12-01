package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DtoTarjetasFinancieras implements Serializable{
	private int consecutivo;
	private String codigo;
	private String descripcion;
	private DtoTipoTarjetaFinanciera tipoTrjeta;
	public int getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public DtoTipoTarjetaFinanciera getTipoTrjeta() {
		return tipoTrjeta;
	}
	public void setTipoTrjeta(DtoTipoTarjetaFinanciera tipoTrjeta) {
		this.tipoTrjeta = tipoTrjeta;
	}
}
