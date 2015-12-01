package com.servinte.axioma.dto.historiaClinica;

import java.util.Date;

/**
 * Dto Imagenes utilizadas en la Curva de Crecimiento
 * 
 * @author hermorhu
 * @created 09-Oct-2012
 */
public class ImagenParametrizadaDto {

	private Integer id;
	private String imagenIzquierda = "";
	private String imagenDerecha = "";
	private String imagenCurva = "";
	private Boolean activo;
	private Date fechaCreacion;

	public ImagenParametrizadaDto() {
	}

	public ImagenParametrizadaDto(Integer id, String imagenIzquierda,
			String imagenDerecha, String imagenCurva, Boolean activo,
			Date fechaCreacion) {
		this.id = id;
		this.imagenIzquierda = imagenIzquierda;
		this.imagenDerecha = imagenDerecha;
		this.imagenCurva = imagenCurva;
		this.activo = activo;
		this.fechaCreacion = fechaCreacion;
	}

	public ImagenParametrizadaDto(String imagenIzquierda, String imagenDerecha,
			String imagenCurva, Boolean activo) {
		this.imagenIzquierda = imagenIzquierda;
		this.imagenDerecha = imagenDerecha;
		this.imagenCurva = imagenCurva;
		this.activo = activo;
		this.fechaCreacion = new Date(System.currentTimeMillis());
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImagenIzquierda() {
		if(imagenIzquierda==null)
			return "";
		return imagenIzquierda;
	}
	public void setImagenIzquierda(String imagenIzquierda) {
		this.imagenIzquierda = imagenIzquierda;
	}
	public String getImagenDerecha() {
		if(imagenDerecha==null)
			return "";
		return imagenDerecha;
	}
	public void setImagenDerecha(String imagenDerecha) {
		this.imagenDerecha = imagenDerecha;
	}
	public String getImagenCurva() {
		if(imagenCurva==null)
			return "";
		return imagenCurva;
	}
	public void setImagenCurva(String imagenCurva) {
		this.imagenCurva = imagenCurva;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}