package com.servinte.axioma.dto.facturacion;

public class InfoMontoDetalladoMedicamentoInsumoDto {
	
	/**
	 * 
	 */
	private Integer codigo;
	
	/**
	 * 
	 */
	private Integer articulo;
	
	/**
	 * 
	 */
	private Integer clase;
	
	/**
	 * 
	 */
	private Integer grupo;

	/**
	 * 
	 */
	private Integer subGrupo;
	
	/**
	 * 
	 */
	private String naturaleza;

	/**
	 * 
	 */
	private Integer cantidadArticulos;
	
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
	 * los articulos en la agrupacion
	 */
	private int sumatoriaArticulos;
	

	public InfoMontoDetalladoMedicamentoInsumoDto(){};

	/**
	 * @param codigo
	 * @param articulo
	 * @param cantidad
	 * @param cantidadMonto
	 * @param valorMonto
	 */
	public InfoMontoDetalladoMedicamentoInsumoDto(Integer codigo,
			Integer articulo, Integer cantidadArticulos, 
			Integer cantidadMonto, Double valorMonto) {
		this.codigo = codigo;
		this.articulo = articulo;
		this.cantidadArticulos = cantidadArticulos;
		this.cantidadMonto = cantidadMonto;
		this.valorMonto = valorMonto;
	}

	/**
	 * @param codigo
	 * @param clase
	 * @param grupo
	 * @param subGrupo
	 * @param naturaleza
	 * @param cantidadMonto
	 * @param valorMonto
	 */
	public InfoMontoDetalladoMedicamentoInsumoDto(Integer codigo,
			Integer clase, Integer grupo, Integer subGrupo, String naturaleza,
			Integer cantidadArticulos, Integer cantidadMonto, Double valorMonto) {
		super();
		this.codigo = codigo;
		this.clase = clase;
		this.grupo = grupo;
		this.subGrupo = subGrupo;
		this.naturaleza = naturaleza;
		this.cantidadArticulos = cantidadArticulos;
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
	 * @return the articulo
	 */
	public Integer getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the clase
	 */
	public Integer getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(Integer clase) {
		this.clase = clase;
	}

	/**
	 * @return the grupo
	 */
	public Integer getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the subGrupo
	 */
	public Integer getSubGrupo() {
		return subGrupo;
	}

	/**
	 * @param subGrupo the subGrupo to set
	 */
	public void setSubGrupo(Integer subGrupo) {
		this.subGrupo = subGrupo;
	}

	/**
	 * @return the naturaleza
	 */
	public String getNaturaleza() {
		return naturaleza;
	}

	/**
	 * @param naturaleza the naturaleza to set
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	/**
	 * @return the cantidadArticulos
	 */
	public Integer getCantidadArticulos() {
		return cantidadArticulos;
	}

	/**
	 * @param cantidadArticulos the cantidadArticulos to set
	 */
	public void setCantidadArticulos(Integer cantidadArticulos) {
		this.cantidadArticulos = cantidadArticulos;
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
	 * @return the sumatoriaArticulos
	 */
	public int getSumatoriaArticulos() {
		return sumatoriaArticulos;
	}

	/**
	 * @param sumatoriaArticulos the sumatoriaArticulos to set
	 */
	public void setSumatoriaArticulos(int sumatoriaArticulos) {
		this.sumatoriaArticulos = sumatoriaArticulos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfoMontoDetalladoMedicamentoInsumoDto [codigo=" + codigo
				+ ", articulo=" + articulo + ", clase=" + clase + ", grupo="
				+ grupo + ", subGrupo=" + subGrupo + ", naturaleza="
				+ naturaleza + ", cantidadArticulos=" + cantidadArticulos
				+ ", cantidadMonto=" + cantidadMonto + ", valorMonto="
				+ valorMonto + ", porAgrupacion=" + porAgrupacion + "]";
	}

}
