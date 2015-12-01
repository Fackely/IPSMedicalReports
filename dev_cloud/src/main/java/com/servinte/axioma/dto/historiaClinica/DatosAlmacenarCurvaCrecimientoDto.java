package com.servinte.axioma.dto.historiaClinica;

import java.util.Date;
import java.util.List;

/**
 * Dto Curva Crecimiento Paciente a almacenar
 * @author hermorhu
 * @created 09-Oct-2012
 */
public class DatosAlmacenarCurvaCrecimientoDto {
	
	private Integer numeroSolicitud;
	private Integer codigoCurvaParametrizada;
	private String imagenBase64;
	private Integer codigoImagenParametrizada;
	private List<CoordenadasCurvaCrecimientoDto> coordenadasPuntos;
	private boolean esValoracion;
	private Date fechaCreacion;
	private String rutaImagen;
	
	/**
	 * 
	 */
	public DatosAlmacenarCurvaCrecimientoDto() {
		super();
	}
	
	/**
	 * @param numeroSolicitud
	 * @param codigoCurvaParametrizada
	 * @param imagenBase64
	 * @param codigoImagenParametrizada
	 * @param coordenadasPuntos
	 * @param esValoracion
	 * @param fechaCreacion
	 * @param rutaImagen
	 */
	public DatosAlmacenarCurvaCrecimientoDto(Integer numeroSolicitud,
			Integer codigoCurvaParametrizada, String imagenBase64,
			Integer codigoImagenParametrizada,
			List<CoordenadasCurvaCrecimientoDto> coordenadasPuntos,
			boolean esValoracion, Date fechaCreacion, String rutaImagen) {
		super();
		this.numeroSolicitud = numeroSolicitud;
		this.codigoCurvaParametrizada = codigoCurvaParametrizada;
		this.imagenBase64 = imagenBase64;
		this.codigoImagenParametrizada = codigoImagenParametrizada;
		this.coordenadasPuntos = coordenadasPuntos;
		this.esValoracion = esValoracion;
		this.fechaCreacion = fechaCreacion;
		this.rutaImagen = rutaImagen;
	}

	/**
	 * @return the codigoCurvaParametrizada
	 */
	public Integer getCodigoCurvaParametrizada() {
		return codigoCurvaParametrizada;
	}
	
	/**
	 * @param codigoCurvaParametrizada the codigoCurvaParametrizada to set
	 */
	public void setCodigoCurvaParametrizada(Integer codigoCurvaParametrizada) {
		this.codigoCurvaParametrizada = codigoCurvaParametrizada;
	}
	
	/**
	 * @return the imagenBase64
	 */
	public String getImagenBase64() {
		return imagenBase64;
	}
	
	/**
	 * @param imagenBase64 the imagenBase64 to set
	 */
	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}
	
	/**
	 * @return the codigoImagenParametrizada
	 */
	public Integer getCodigoImagenParametrizada() {
		return codigoImagenParametrizada;
	}
	
	/**
	 * @param codigoImagenParametrizada the codigoImagenParametrizada to set
	 */
	public void setCodigoImagenParametrizada(Integer codigoImagenParametrizada) {
		this.codigoImagenParametrizada = codigoImagenParametrizada;
	}
	
	/**
	 * @return the coordenadasPuntos
	 */
	public List<CoordenadasCurvaCrecimientoDto> getCoordenadasPuntos() {
		return coordenadasPuntos;
	}
	
	/**
	 * @param coordenadasPuntos the coordenadasPuntos to set
	 */
	public void setCoordenadasPuntos(
			List<CoordenadasCurvaCrecimientoDto> coordenadasPuntos) {
		this.coordenadasPuntos = coordenadasPuntos;
	}
	
	/**
	 * @return the esValoracion
	 */
	public boolean isEsValoracion() {
		return esValoracion;
	}
	
	/**
	 * @param esValoracion the esValoracion to set
	 */
	public void setEsValoracion(boolean esValoracion) {
		this.esValoracion = esValoracion;
	}
	
	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	
	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	/**
	 * @return the rutaImagen
	 */
	public String getRutaImagen() {
		return rutaImagen;
	}
	
	/**
	 * @param rutaImagen the rutaImagen to set
	 */
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	
}
