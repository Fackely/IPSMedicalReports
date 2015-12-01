package com.servinte.axioma.dto.manejoPaciente;

import java.util.Date;

/**
 * @author jeilones
 *
 */
public class ArticuloAutorizadoCapitacionDto {
	
	private long tipoAutorizacion;
	private Long codigo;
	private Long consecutivo;
	private Date fechaGeneracion;
	private long codArt;
	private String nomArt;
	
	private String concentracion;
	private String formaFarmaceutica;
	private String unidadMedida;
	
	private String naturalezaArticulo;
	private String acronimoNatArt;
	private Integer codigoSubgrupo;
	private long cantidad;
	private String acronimoDiag;
	private Integer tipoCieDiag;
	private String diagDescripcion;
	private Double valorTarifa;
	private String indicativoTemporal;
	private int viaIngreso;
	private int tipoSolicitud;
	private String pyp;
	/**
	 * @param tipoAutorizacion
	 * @param codigo
	 * @param consecutivo
	 * @param fechaGeneracion
	 * @param codArt
	 * @param nomArt
	 * @param concentracion
	 * @param formaFarmaceutica
	 * @param unidadMedida
	 * @param naturalezaArticulo
	 * @param acronimoNatArt
	 * @param codigoSubgrupo
	 * @param cantidad
	 * @param acronimoDiag
	 * @param tipoCieDiag
	 * @param diagDescripcion
	 * @param valorTarifa
	 * @param indicativoTemporal
	 * @param viaIngreso
	 * @author jeilones
	 * @param pyp 
	 * @created 08/08/2012
	 */
	public ArticuloAutorizadoCapitacionDto(long tipoAutorizacion, Long codigo,
			Long consecutivo, Date fechaGeneracion, long codArt, String nomArt,
			String concentracion, String formaFarmaceutica,
			String unidadMedida, String naturalezaArticulo, String acronimoNatArt,Integer codigoSubgrupo, long cantidad,
			String acronimoDiag, Integer tipoCieDiag, String diagDescripcion,
			Double valorTarifa, String indicativoTemporal, int viaIngreso, int tipoSolicitud, String pyp) {
		super();
		this.tipoAutorizacion = tipoAutorizacion;
		this.codigo = codigo;
		this.consecutivo = consecutivo;
		this.fechaGeneracion = fechaGeneracion;
		this.codArt = codArt;
		this.nomArt = nomArt;
		this.concentracion = concentracion;
		this.formaFarmaceutica = formaFarmaceutica;
		this.unidadMedida = unidadMedida;
		this.naturalezaArticulo = naturalezaArticulo;
		this.acronimoNatArt=acronimoNatArt;
		this.codigoSubgrupo=codigoSubgrupo;
		this.cantidad = cantidad;
		this.acronimoDiag = acronimoDiag;
		this.tipoCieDiag = tipoCieDiag;
		this.diagDescripcion = diagDescripcion;
		this.valorTarifa = valorTarifa;
		this.indicativoTemporal = indicativoTemporal;
		this.viaIngreso=viaIngreso;
		this.tipoSolicitud=tipoSolicitud;
		this.pyp=pyp;
	}
	
	/**
	 * @return the tipoAutorizacion
	 */
	public long getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	/**
	 * @param tipoAutorizacion the tipoAutorizacion to set
	 */
	public void setTipoAutorizacion(long tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the consecutivo
	 */
	public Long getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the fechaGeneracion
	 */
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	/**
	 * @param fechaGeneracion the fechaGeneracion to set
	 */
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	/**
	 * @return the codArt
	 */
	public long getCodArt() {
		return codArt;
	}
	/**
	 * @param codArt the codArt to set
	 */
	public void setCodArt(long codArt) {
		this.codArt = codArt;
	}
	/**
	 * @return the nomArt
	 */
	public String getNomArt() {
		return nomArt;
	}
	/**
	 * @param nomArt the nomArt to set
	 */
	public void setNomArt(String nomArt) {
		this.nomArt = nomArt;
	}
	/**
	 * @return the cantidad
	 */
	public long getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return the acronimoDiag
	 */
	public String getAcronimoDiag() {
		return acronimoDiag;
	}
	/**
	 * @param acronimoDiag the acronimoDiag to set
	 */
	public void setAcronimoDiag(String acronimoDiag) {
		this.acronimoDiag = acronimoDiag;
	}
	/**
	 * @return the tipoCieDiag
	 */
	public Integer getTipoCieDiag() {
		return tipoCieDiag;
	}
	/**
	 * @param tipoCieDiag the tipoCieDiag to set
	 */
	public void setTipoCieDiag(Integer tipoCieDiag) {
		this.tipoCieDiag = tipoCieDiag;
	}
	/**
	 * @return the diagDescripcion
	 */
	public String getDiagDescripcion() {
		return diagDescripcion;
	}
	/**
	 * @param diagDescripcion the diagDescripcion to set
	 */
	public void setDiagDescripcion(String diagDescripcion) {
		this.diagDescripcion = diagDescripcion;
	}
	/**
	 * @return the valorTarifa
	 */
	public Double getValorTarifa() {
		return valorTarifa;
	}
	/**
	 * @param valorTarifa the valorTarifa to set
	 */
	public void setValorTarifa(Double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}
	/**
	 * @return the indicativoTemporal
	 */
	public String getIndicativoTemporal() {
		return indicativoTemporal;
	}
	/**
	 * @param indicativoTemporal the indicativoTemporal to set
	 */
	public void setIndicativoTemporal(String indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}
	/**
	 * @return the naturalezaArticulo
	 */
	public String getNaturalezaArticulo() {
		return naturalezaArticulo;
	}
	/**
	 * @param naturalezaArticulo the naturalezaArticulo to set
	 */
	public void setNaturalezaArticulo(String naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}
	/**
	 * @return the concentracion
	 */
	public String getConcentracion() {
		return concentracion;
	}
	/**
	 * @param concentracion the concentracion to set
	 */
	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}
	/**
	 * @return the formaFarmaceutica
	 */
	public String getFormaFarmaceutica() {
		return formaFarmaceutica;
	}
	/**
	 * @param formaFarmaceutica the formaFarmaceutica to set
	 */
	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}
	/**
	 * @return the unidadMedida
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}
	/**
	 * @param unidadMedida the unidadMedida to set
	 */
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	/**
	 * @return the acronimoNatArt
	 */
	public String getAcronimoNatArt() {
		return acronimoNatArt;
	}

	/**
	 * @param acronimoNatArt the acronimoNatArt to set
	 */
	public void setAcronimoNatArt(String acronimoNatArt) {
		this.acronimoNatArt = acronimoNatArt;
	}

	/**
	 * @return the codigoSubgrupo
	 */
	public Integer getCodigoSubgrupo() {
		return codigoSubgrupo;
	}

	/**
	 * @param codigoSubgrupo the codigoSubgrupo to set
	 */
	public void setCodigoSubgrupo(Integer codigoSubgrupo) {
		this.codigoSubgrupo = codigoSubgrupo;
	}

	/**
	 * @return the viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the pyp
	 */
	public String getPyp() {
		return pyp;
	}

	/**
	 * @param pyp the pyp to set
	 */
	public void setPyp(String pyp) {
		this.pyp = pyp;
	}

	
	
}
