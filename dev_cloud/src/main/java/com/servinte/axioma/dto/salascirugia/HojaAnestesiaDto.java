/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Oscar Pulido
 * @created 19/07/2013
 *
 */
public class HojaAnestesiaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fechaIniciaAnestesia;
	private String horaIniciaAnestesia;
	private Date fechaFinAnestesia;
	private String horaFinAnestesia;
	private String datosMedico;
	private boolean finalizada;
	private Date fechaFinalizada;
	private String horaFinalizada;
	private String observacionesSignosVitales;
	private int peso;
	private int talla;
	private boolean anestesiologoCobrable;
	private String descripcion;

	public HojaAnestesiaDto() {
	}

	public HojaAnestesiaDto(Date fechaIniciaAnestesia,
			String horaIniciaAnestesia, Date fechaFinAnestesia,
			String horaFinAnestesia, String datosMedico, boolean finalizada,
			Date fechaFinalizada, String horaFinalizada,
			String observacionesSignosVitales, int peso, int talla,
			boolean anestesiologoCobrable, String descripcion) {
		this.fechaIniciaAnestesia = fechaIniciaAnestesia;
		this.horaIniciaAnestesia = horaIniciaAnestesia;
		this.fechaFinAnestesia = fechaFinAnestesia;
		this.horaFinAnestesia = horaFinAnestesia;
		this.datosMedico = datosMedico;
		this.finalizada = finalizada;
		this.fechaFinalizada = fechaFinalizada;
		this.horaFinalizada = horaFinalizada;
		this.observacionesSignosVitales = observacionesSignosVitales;
		this.peso = peso;
		this.talla = talla;
		this.anestesiologoCobrable = anestesiologoCobrable;
		this.descripcion = descripcion;
	}
	
	public Date getFechaIniciaAnestesia() {
		return fechaIniciaAnestesia;
	}
	public void setFechaIniciaAnestesia(Date fechaIniciaAnestesia) {
		this.fechaIniciaAnestesia = fechaIniciaAnestesia;
	}
	public String getHoraIniciaAnestesia() {
		return horaIniciaAnestesia;
	}
	public void setHoraIniciaAnestesia(String horaIniciaAnestesia) {
		this.horaIniciaAnestesia = horaIniciaAnestesia;
	}
	public Date getFechaFinAnestesia() {
		return fechaFinAnestesia;
	}
	public void setFechaFinAnestesia(Date fechaFinAnestesia) {
		this.fechaFinAnestesia = fechaFinAnestesia;
	}
	public String getHoraFinAnestesia() {
		return horaFinAnestesia;
	}
	public void setHoraFinAnestesia(String horaFinAnestesia) {
		this.horaFinAnestesia = horaFinAnestesia;
	}
	public String getDatosMedico() {
		return datosMedico;
	}
	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}
	public boolean isFinalizada() {
		return finalizada;
	}
	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}
	public Date getFechaFinalizada() {
		return fechaFinalizada;
	}
	public void setFechaFinalizada(Date fechaFinalizada) {
		this.fechaFinalizada = fechaFinalizada;
	}
	public String getHoraFinalizada() {
		return horaFinalizada;
	}
	public void setHoraFinalizada(String horaFinalizada) {
		this.horaFinalizada = horaFinalizada;
	}
	public String getObservacionesSignosVitales() {
		return observacionesSignosVitales;
	}
	public void setObservacionesSignosVitales(String observacionesSignosVitales) {
		this.observacionesSignosVitales = observacionesSignosVitales;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public int getTalla() {
		return talla;
	}
	public void setTalla(int talla) {
		this.talla = talla;
	}
	public boolean isAnestesiologoCobrable() {
		return anestesiologoCobrable;
	}
	public void setAnestesiologoCobrable(boolean anestesiologoCobrable) {
		this.anestesiologoCobrable = anestesiologoCobrable;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
