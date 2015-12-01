package com.princetonsa.dto.interfaz;

import java.io.Serializable;

public class DtoTipoMovimientoArchivo implements Serializable
{
	boolean preguntarSobreEscribir;
	boolean sobreEscribir;
	boolean generado;
	boolean existeInformacion;
	
	String indicadorTipoMov;
	String indicadorArchivo;
	String pathArchivo;
	String nombreArchivo;
	String fechaGeneracion;
	String horaGeneracion;	
	
	public DtoTipoMovimientoArchivo()
	{
		reset();
	}
	
	public void reset()
	{
		this.indicadorTipoMov = "";
		this.indicadorArchivo = "";
		this.nombreArchivo = "";
		this.pathArchivo = "";
		this.preguntarSobreEscribir = false;
		this.sobreEscribir = false;
		this.fechaGeneracion = "";
		this.horaGeneracion = "";
		this.generado = false;
		this.existeInformacion = false;
	}

	public boolean isPreguntarSobreEscribir() {
		return preguntarSobreEscribir;
	}

	public void setPreguntarSobreEscribir(boolean preguntarSobreEscribir) {
		this.preguntarSobreEscribir = preguntarSobreEscribir;
	}

	public boolean isSobreEscribir() {
		return sobreEscribir;
	}

	public void setSobreEscribir(boolean sobreEscribir) {
		this.sobreEscribir = sobreEscribir;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public boolean isGenerado() {
		return generado;
	}

	public void setGenerado(boolean generado) {
		this.generado = generado;
	}

	public boolean isExisteInformacion() {
		return existeInformacion;
	}

	public void setExisteInformacion(boolean existeInformacion) {
		this.existeInformacion = existeInformacion;
	}

	public String getIndicadorArchivo() {
		return indicadorArchivo;
	}

	public void setIndicadorArchivo(String indicadorArchivo) {
		this.indicadorArchivo = indicadorArchivo;
	}

	public String getPathArchivo() {
		return pathArchivo;
	}

	public void setPathArchivo(String pathArchivo) {
		this.pathArchivo = pathArchivo;
	}

	public String getIndicadorTipoMov() {
		return indicadorTipoMov;
	}

	public void setIndicadorTipoMov(String indicadorTipoMov) {
		this.indicadorTipoMov = indicadorTipoMov;
	}
}