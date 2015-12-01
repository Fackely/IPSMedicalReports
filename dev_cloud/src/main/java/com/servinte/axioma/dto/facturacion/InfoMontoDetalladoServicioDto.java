package com.servinte.axioma.dto.facturacion;

public class InfoMontoDetalladoServicioDto {

	
	/**
	 * 
	 */
	private Integer codigo;
	
	/**
	 * 
	 */
	private Integer servicio;
	
	/**
	 * 
	 */
	private Integer grupoServicio;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private Integer especialidad;
	
	/**
	 * 
	 */
	private Integer cantidadServicios;
	
	/**
	 * 
	 */
	private Integer cantidadMonto;
	
	/**
	 * 
	 */
	private Double valorMonto;
	
	/**
	 * 
	 */
	private boolean porAgrupacion;

	/**
	 * Campo encargado de la sumatoria de
	 * los servicios en la agrupacion
	 */
	private int sumatoriaServicios;
	
	
	public InfoMontoDetalladoServicioDto(){};
	
	/**
	 * @param codigo
	 * @param servicio
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @param cantidad
	 * @param cantidadMonto
	 * @param valorMonto
	 * @param porAgrupacion
	 */
	public InfoMontoDetalladoServicioDto(Integer codigo, Integer grupoServicio, 
			String tipoServicio, Integer especialidad, Integer cantidadServicios, 
			Integer cantidadMonto, Double valorMonto) {
		super();
		this.codigo = codigo;
		this.grupoServicio = grupoServicio;
		this.tipoServicio = tipoServicio;
		this.especialidad = especialidad;
		this.cantidadServicios = cantidadServicios;
		this.cantidadMonto = cantidadMonto;
		this.valorMonto = valorMonto;
	}
	
	/**
	 * @param codigo
	 * @param servicio
	 * @param cantidadMonto
	 * @param valorMonto
	 */
	public InfoMontoDetalladoServicioDto(Integer codigo, Integer servicio,
			Integer cantidadServicios, Integer cantidadMonto, Double valorMonto) {
		super();
		this.codigo = codigo;
		this.servicio = servicio;
		this.cantidadServicios = cantidadServicios;
		this.cantidadMonto = cantidadMonto;
		this.valorMonto = valorMonto;
	}


	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the servicio
	 */
	public Integer getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(Integer servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the grupoServicio
	 */
	public Integer getGrupoServicio() {
		return grupoServicio;
	}

	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(Integer grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the especialidad
	 */
	public Integer getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(Integer especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the cantidadServicios
	 */
	public Integer getCantidadServicios() {
		return cantidadServicios;
	}

	/**
	 * @param cantidadServicios the cantidadServicios to set
	 */
	public void setCantidadServicios(Integer cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}

	/**
	 * @return the cantidadMonto
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}

	/**
	 * @param cantidadMonto the cantidadMonto to set
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}

	/**
	 * @return the valorMonto
	 */
	public Double getValorMonto() {
		return valorMonto;
	}

	/**
	 * @param valorMonto the valorMonto to set
	 */
	public void setValorMonto(Double valorMonto) {
		this.valorMonto = valorMonto;
	}

	/**
	 * @return the porAgrupacion
	 */
	public boolean isPorAgrupacion() {
		return porAgrupacion;
	}

	/**
	 * @param porAgrupacion the porAgrupacion to set
	 */
	public void setPorAgrupacion(boolean porAgrupacion) {
		this.porAgrupacion = porAgrupacion;
	}

	/**
	 * @return the sumatoriaServicios
	 */
	public int getSumatoriaServicios() {
		return sumatoriaServicios;
	}

	/**
	 * @param sumatoriaServicios the sumatoriaServicios to set
	 */
	public void setSumatoriaServicios(int sumatoriaServicios) {
		this.sumatoriaServicios = sumatoriaServicios;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfoMontoDetalladoServicioDto [codigo=" + codigo
				+ ", servicio=" + servicio + ", grupoServicio=" + grupoServicio
				+ ", tipoServicio=" + tipoServicio + ", especialidad="
				+ especialidad + ", cantidadServicios=" + cantidadServicios
				+ ", cantidadMonto=" + cantidadMonto + ", valorMonto="
				+ valorMonto + ", porAgrupacion=" + porAgrupacion
				+ ", sumatoriaServicios=" + sumatoriaServicios + "]";
	}
	
}
