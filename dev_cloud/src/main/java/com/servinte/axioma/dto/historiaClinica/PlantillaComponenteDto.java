package com.servinte.axioma.dto.historiaClinica;

import java.util.Date;

/**
 * Dto Plantilla Curva de Crecimiento asociada al Componente
 * @author hermorhu
 * @created 09-Oct-2012
 */
public class PlantillaComponenteDto {
	
	private Integer id;
	private CurvaCrecimientoParametrizabDto plantillaCurvaCrecimiento;
	private Boolean activo;
	private Date fechaCreacion;

	public PlantillaComponenteDto() {

	}

	/**
	 * @param id
	 * @param plantillaCurvaCrecimiento
	 * @param activo
	 * @param fechaCreacion
	 */
	public PlantillaComponenteDto(Integer id,
			CurvaCrecimientoParametrizabDto plantillaCurvaCrecimiento,
			Boolean activo, Date fechaCreacion) {
		super();
		this.id = id;
		this.plantillaCurvaCrecimiento = plantillaCurvaCrecimiento;
		this.activo = activo;
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @param id
	 * @param idCurvaParametrizada
	 * @param codigoSexo
	 * @param idCurvaPredecesor
	 * @param tituloGrafica
	 * @param colorTitulo
	 * @param descripcion
	 * @param colorDescripcion
	 * @param edadInicial
	 * @param edadFinal
	 * @param activoCurvaParametrizada
	 * @param indicadorError
	 * @param fechaCreacionCruvaParametrizada
	 * @param activo
	 * @param fechaCreacion
	 */
	public PlantillaComponenteDto(Integer id, int idCurvaParametrizada,
			Integer codigoSexo, Integer idCurvaPredecesor,
			String tituloGrafica, String colorTitulo, String descripcion,
			String colorDescripcion, Integer edadInicial, Integer edadFinal,
			Boolean activoCurvaParametrizada, Boolean indicadorError,
			Date fechaCreacionCruvaParametrizada, Boolean activo,
			Date fechaCreacion) {
		
		this.id = id;
		
		this.plantillaCurvaCrecimiento = new CurvaCrecimientoParametrizabDto();
		
		this.plantillaCurvaCrecimiento.setId(idCurvaParametrizada);
		this.plantillaCurvaCrecimiento.setCodigoSexo(codigoSexo);
		this.plantillaCurvaCrecimiento.setIdCurvaPredecesor(idCurvaPredecesor);
		this.plantillaCurvaCrecimiento.setTituloGrafica(tituloGrafica);
		this.plantillaCurvaCrecimiento.setColorTitulo(colorTitulo);
		this.plantillaCurvaCrecimiento.setDescripcion(descripcion);
		this.plantillaCurvaCrecimiento.setColorDescripcion(colorDescripcion);
		this.plantillaCurvaCrecimiento.setEdadInicial(edadInicial);
		this.plantillaCurvaCrecimiento.setEdadFinal(edadFinal);
		this.plantillaCurvaCrecimiento.setActivo(activoCurvaParametrizada);
		this.plantillaCurvaCrecimiento.setIndicadorError(indicadorError);
		this.plantillaCurvaCrecimiento.setFechaCreacion(fechaCreacionCruvaParametrizada);
		
		this.activo = activo;
		this.fechaCreacion = fechaCreacion;	
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the plantillaCurvaCrecimiento
	 */
	public CurvaCrecimientoParametrizabDto getPlantillaCurvaCrecimiento() {
		return plantillaCurvaCrecimiento;
	}

	/**
	 * @param plantillaCurvaCrecimiento
	 *            the plantillaCurvaCrecimiento to set
	 */
	public void setPlantillaCurvaCrecimiento(
			CurvaCrecimientoParametrizabDto plantillaCurvaCrecimiento) {
		this.plantillaCurvaCrecimiento = plantillaCurvaCrecimiento;
	}

	/**
	 * @return the activo
	 */
	public Boolean getActivo() {
		return activo;
	}

	/**
	 * @param activo
	 *            the activo to set
	 */
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

}
