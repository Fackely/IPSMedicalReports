package com.servinte.axioma.dto.historiaClinica;

/**
 * Dto Coordenadas almacenadas en la grafica de la Curva de Crecimimento
 * @author hermorhu
 * @created 09-Oct-2012 
 */
public class CoordenadasCurvaCrecimientoDto {

	private Integer id;
	private Integer coordenadaX;
	private Integer coordenadaY;
	
	/**
	 * 
	 */
	public CoordenadasCurvaCrecimientoDto() {
		super();
	}

	/**
	 * @param id
	 * @param coordenadaX
	 * @param coordenadaY
	 */
	public CoordenadasCurvaCrecimientoDto(Integer id, Integer coordenadaX,
			Integer coordenadaY) {
		super();
		this.id = id;
		this.coordenadaX = coordenadaX;
		this.coordenadaY = coordenadaY;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the coordenadaX
	 */
	public Integer getCoordenadaX() {
		return coordenadaX;
	}
	
	/**
	 * @param coordenadaX the coordenadaX to set
	 */
	public void setCoordenadaX(Integer coordenadaX) {
		this.coordenadaX = coordenadaX;
	}
	
	/**
	 * @return the coordenadaY
	 */
	public Integer getCoordenadaY() {
		return coordenadaY;
	}
	
	/**
	 * @param coordenadaY the coordenadaY to set
	 */
	public void setCoordenadaY(Integer coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

}
