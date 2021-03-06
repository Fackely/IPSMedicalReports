package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * DiasFestivos generated by hbm2java
 */
public class DiasFestivos implements java.io.Serializable {

	private Date fecha;
	private TiposDiaFestivo tiposDiaFestivo;
	private String descripcion;

	public DiasFestivos() {
	}

	public DiasFestivos(Date fecha, TiposDiaFestivo tiposDiaFestivo,
			String descripcion) {
		this.fecha = fecha;
		this.tiposDiaFestivo = tiposDiaFestivo;
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TiposDiaFestivo getTiposDiaFestivo() {
		return this.tiposDiaFestivo;
	}

	public void setTiposDiaFestivo(TiposDiaFestivo tiposDiaFestivo) {
		this.tiposDiaFestivo = tiposDiaFestivo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
