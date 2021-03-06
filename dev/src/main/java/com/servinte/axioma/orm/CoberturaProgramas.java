package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * CoberturaProgramas generated by hbm2java
 */
public class CoberturaProgramas implements java.io.Serializable {

	private long codigo;
	private DetalleCobertura detalleCobertura;
	private Programas programas;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;
	private Integer cantidad;

	public CoberturaProgramas() {
	}

	public CoberturaProgramas(long codigo, DetalleCobertura detalleCobertura,
			Programas programas, String usuarioModifica, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.detalleCobertura = detalleCobertura;
		this.programas = programas;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public CoberturaProgramas(long codigo, DetalleCobertura detalleCobertura,
			Programas programas, String usuarioModifica, Date fechaModifica,
			String horaModifica, Integer cantidad) {
		this.codigo = codigo;
		this.detalleCobertura = detalleCobertura;
		this.programas = programas;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.cantidad = cantidad;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public DetalleCobertura getDetalleCobertura() {
		return this.detalleCobertura;
	}

	public void setDetalleCobertura(DetalleCobertura detalleCobertura) {
		this.detalleCobertura = detalleCobertura;
	}

	public Programas getProgramas() {
		return this.programas;
	}

	public void setProgramas(Programas programas) {
		this.programas = programas;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
