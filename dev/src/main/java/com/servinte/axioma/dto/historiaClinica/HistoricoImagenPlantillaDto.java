package com.servinte.axioma.dto.historiaClinica;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Dto encaragado de manejar la informacion de la endidad HistoricoImagenPlantilla
 * @author Juan Camilo Gaviria Acosta
 */
public class HistoricoImagenPlantillaDto implements java.io.Serializable {
	
	public static final int VALORACION = 1;
	public static final int EVOLUCION = 2;
	private String imagenIzquierda;
	private String tituloGrafica;
	private String colorTitulo;
	private Integer edadInicial;
	private Integer edadFinal;
	private String descripcion;
	private String colorDescripcion;
	private String imagenDerecha;
	private String urlImagen;
	private Integer numeroSolicitud;
	private Date fecha;
	private int tipo;
	private String talla;
	private String peso;
	private String masaMuscular;
	private String perimetroCefalico;
	private String unidadTalla;
	private String unidadPeso;
	private String unidadMasaMuscular;
	private String unidadPerimetroCefalico;
	
	public HistoricoImagenPlantillaDto() {
	}
	
	public HistoricoImagenPlantillaDto(String imagenIzquierda,
			String tituloGrafica, String colorTitulo, Integer edadInicial,
			Integer edadFinal, String descripcion, String colorDescripcion,
			String imagenDerecha, String urlImagen, Integer numeroSolicitud,
			Date fecha, int tipo, String talla, String peso,
			String masaMuscular, String perimetroCefalico, String unidadTalla,
			String unidadPeso, String unidadMasaMuscular,
			String unidadPerimetroCefalico) {
		this.imagenIzquierda = imagenIzquierda;
		this.tituloGrafica = tituloGrafica;
		this.colorTitulo = colorTitulo;
		this.edadInicial = edadInicial;
		this.edadFinal = edadFinal;
		this.descripcion = descripcion;
		this.colorDescripcion = colorDescripcion;
		this.imagenDerecha = imagenDerecha;
		this.urlImagen = urlImagen;
		this.numeroSolicitud = numeroSolicitud;
		this.fecha = fecha;
		this.tipo = tipo;
		this.talla = talla;
		this.peso = peso;
		this.masaMuscular = masaMuscular;
		this.perimetroCefalico = perimetroCefalico;
		this.unidadTalla = unidadTalla;
		this.unidadPeso = unidadPeso;
		this.unidadMasaMuscular = unidadMasaMuscular;
		this.unidadPerimetroCefalico = unidadPerimetroCefalico;
	}
	
	public HistoricoImagenPlantillaDto(Object...datos) {
		if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
		{
			this.imagenIzquierda = (String) datos[0];
			this.tituloGrafica = (String) datos[1];
			this.colorTitulo = (String) datos[2];
			this.edadInicial = (Integer) datos[3];
			this.edadFinal = (Integer) datos[4];
			this.descripcion = (String) datos[5];
			this.colorDescripcion = (String) datos[6];
			this.imagenDerecha = (String) datos[7];
			this.urlImagen = (String) datos[8];
			this.numeroSolicitud = (Integer) datos[9];
			this.fecha = (Date) datos[10];
			this.tipo = (Integer) datos[11];
			this.talla = (String) datos[12];
			this.peso = (String) datos[13];
			this.masaMuscular = (String) datos[14];
			this.perimetroCefalico = (String) datos[15];
			this.unidadTalla = (String) datos[16];
			this.unidadPeso = (String) datos[17];
			this.unidadMasaMuscular = (String) datos[18];
			this.unidadPerimetroCefalico = (String) datos[19];
		}
		else
		{
			this.imagenIzquierda = (String) datos[0];
			this.tituloGrafica = (String) datos[1];
			this.colorTitulo = (String) datos[2];
			this.edadInicial = ((BigDecimal) datos[3]).intValue();
			this.edadFinal = ((BigDecimal) datos[4]).intValue();
			this.descripcion = (String) datos[5];
			this.colorDescripcion = (String) datos[6];
			this.imagenDerecha = (String) datos[7];
			this.urlImagen = (String) datos[8];
			this.numeroSolicitud = ((BigDecimal) datos[9]).intValue();
			this.fecha = (Date) datos[10];
			this.tipo = ((BigDecimal) datos[11]).intValue();
			this.talla = (String) datos[12];
			this.peso = (String) datos[13];
			this.masaMuscular = (String) datos[14];
			this.perimetroCefalico = (String) datos[15];
			this.unidadTalla = (String) datos[16];
			this.unidadPeso = (String) datos[17];
			this.unidadMasaMuscular = (String) datos[18];
			this.unidadPerimetroCefalico = (String) datos[19];
		}
	}
	
	public String getImagenIzquierda() {
		return imagenIzquierda;
	}
	public void setImagenIzquierda(String imagenIzquierda) {
		this.imagenIzquierda = imagenIzquierda;
	}
	public String getTituloGrafica() {
		return tituloGrafica;
	}
	public void setTituloGrafica(String tituloGrafica) {
		this.tituloGrafica = tituloGrafica;
	}
	public String getColorTitulo() {
		return colorTitulo;
	}
	public void setColorTitulo(String colorTitulo) {
		this.colorTitulo = colorTitulo;
	}
	public Integer getEdadInicial() {
		return edadInicial;
	}
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
	}
	public Integer getEdadFinal() {
		return edadFinal;
	}
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getColorDescripcion() {
		return colorDescripcion;
	}
	public void setColorDescripcion(String colorDescripcion) {
		this.colorDescripcion = colorDescripcion;
	}
	public String getImagenDerecha() {
		return imagenDerecha;
	}
	public void setImagenDerecha(String imagenDerecha) {
		this.imagenDerecha = imagenDerecha;
	}
	public String getUrlImagen() {
		return urlImagen;
	}
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getTalla() {
		return talla;
	}
	public void setTalla(String talla) {
		this.talla = talla;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public String getMasaMuscular() {
		return masaMuscular;
	}
	public void setMasaMuscular(String masaMuscular) {
		this.masaMuscular = masaMuscular;
	}
	public String getPerimetroCefalico() {
		return perimetroCefalico;
	}
	public void setPerimetroCefalico(String perimetroCefalico) {
		this.perimetroCefalico = perimetroCefalico;
	}
	public String getUnidadTalla() {
		return unidadTalla;
	}
	public void setUnidadTalla(String unidadTalla) {
		this.unidadTalla = unidadTalla;
	}
	public String getUnidadPeso() {
		return unidadPeso;
	}
	public void setUnidadPeso(String unidadPeso) {
		this.unidadPeso = unidadPeso;
	}
	public String getUnidadMasaMuscular() {
		return unidadMasaMuscular;
	}
	public void setUnidadMasaMuscular(String unidadMasaMuscular) {
		this.unidadMasaMuscular = unidadMasaMuscular;
	}
	public String getUnidadPerimetroCefalico() {
		return unidadPerimetroCefalico;
	}
	public void setUnidadPerimetroCefalico(String unidadPerimetroCefalico) {
		this.unidadPerimetroCefalico = unidadPerimetroCefalico;
	}
}