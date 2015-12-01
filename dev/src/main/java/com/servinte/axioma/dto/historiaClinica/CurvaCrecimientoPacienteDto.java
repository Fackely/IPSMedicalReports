package com.servinte.axioma.dto.historiaClinica;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto Curva de Crecimiento del Paciente
 * @author hermorhu
 * @created 09-Oct-2012 
 */
public class CurvaCrecimientoPacienteDto {

	private CurvaCrecimientoParametrizabDto dtoCurvaCrecimientoParametrizab = new CurvaCrecimientoParametrizabDto();
	private Integer idImagenParametrizada;
	private Integer idCurvaModificada;
	private String urlUltimaCurvaModificada;
	private String imagenBase64;
	private List<CoordenadasCurvaCrecimientoDto> coordenadasCurvaCrecimiento = new ArrayList<CoordenadasCurvaCrecimientoDto>();
	private List<CoordenadasCurvaCrecimientoDto> coordenadasErroresPuntos = new ArrayList<CoordenadasCurvaCrecimientoDto>();
	private boolean graficaDiligenciada;
	
	/**
	 * 
	 */
	public CurvaCrecimientoPacienteDto() {
		super();
	}
	
	/**
	 * @param dtoCurvaCrecimientoParametrizab
	 * @param idImagenParametrizada
	 * @param urlUltimaCurvaModificada
	 * @param idCurvaModificada
	 * @param coordenadasCurvaCrecimiento
	 * @param imagenBase64
	 * @param graficaDiligenciada
	 * @param coordenadasErroresPuntos
	 */
	public CurvaCrecimientoPacienteDto(
			CurvaCrecimientoParametrizabDto dtoCurvaCrecimientoParametrizab,
			Integer idImagenParametrizada, String urlUltimaCurvaModificada,
			Integer idCurvaModificada,
			List<CoordenadasCurvaCrecimientoDto> coordenadasCurvaCrecimiento,
			String imagenBase64, boolean graficaDiligenciada,
			List<CoordenadasCurvaCrecimientoDto> coordenadasErroresPuntos) {
		super();
		this.dtoCurvaCrecimientoParametrizab = dtoCurvaCrecimientoParametrizab;
		this.idImagenParametrizada = idImagenParametrizada;
		this.urlUltimaCurvaModificada = urlUltimaCurvaModificada;
		this.idCurvaModificada = idCurvaModificada;
		this.coordenadasCurvaCrecimiento = coordenadasCurvaCrecimiento;
		this.imagenBase64 = imagenBase64;
		this.graficaDiligenciada = graficaDiligenciada;
		this.coordenadasErroresPuntos = coordenadasErroresPuntos;
	}

	/**
	 * @return the dtoCurvaCrecimientoParametrizab
	 */
	public CurvaCrecimientoParametrizabDto getDtoCurvaCrecimientoParametrizab() {
		return dtoCurvaCrecimientoParametrizab;
	}
	
	/**
	 * @param dtoCurvaCrecimientoParametrizab the dtoCurvaCrecimientoParametrizab to set
	 */
	public void setDtoCurvaCrecimientoParametrizab(
			CurvaCrecimientoParametrizabDto dtoCurvaCrecimientoParametrizab) {
		this.dtoCurvaCrecimientoParametrizab = dtoCurvaCrecimientoParametrizab;
	}
	
	/**
	 * @return the idImagenParametrizada
	 */
	public Integer getIdImagenParametrizada() {
		return idImagenParametrizada;
	}
	
	/**
	 * @param idImagenParametrizada the idImagenParametrizada to set
	 */
	public void setIdImagenParametrizada(Integer idImagenParametrizada) {
		this.idImagenParametrizada = idImagenParametrizada;
	}
	
	/**
	 * @return the urlUltimaCurvaModificada
	 */
	public String getUrlUltimaCurvaModificada() {
		return urlUltimaCurvaModificada;
	}
	
	/**
	 * @param urlUltimaCurvaModificada the urlUltimaCurvaModificada to set
	 */
	public void setUrlUltimaCurvaModificada(String urlUltimaCurvaModificada) {
		this.urlUltimaCurvaModificada = urlUltimaCurvaModificada;
	}
	
	/**
	 * @return the idCurvaModificada
	 */
	public Integer getIdCurvaModificada() {
		return idCurvaModificada;
	}
	
	/**
	 * @param idCurvaModificada the idCurvaModificada to set
	 */
	public void setIdCurvaModificada(Integer idCurvaModificada) {
		this.idCurvaModificada = idCurvaModificada;
	}
	
	/**
	 * @return the coordenadasCurvaCrecimiento
	 */
	public List<CoordenadasCurvaCrecimientoDto> getCoordenadasCurvaCrecimiento() {
		return coordenadasCurvaCrecimiento;
	}
	
	/**
	 * @param coordenadasCurvaCrecimiento the coordenadasCurvaCrecimiento to set
	 */
	public void setCoordenadasCurvaCrecimiento(
			List<CoordenadasCurvaCrecimientoDto> coordenadasCurvaCrecimiento) {
		this.coordenadasCurvaCrecimiento = coordenadasCurvaCrecimiento;
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
	 * @return the graficaDiligenciada
	 */
	public boolean isGraficaDiligenciada() {
		return graficaDiligenciada;
	}

	/**
	 * @param graficaDiligenciada the graficaDiligenciada to set
	 */
	public void setGraficaDiligenciada(boolean graficaDiligenciada) {
		this.graficaDiligenciada = graficaDiligenciada;
	}

	/**
	 * @return the coordenadasErroresPuntos
	 */
	public List<CoordenadasCurvaCrecimientoDto> getCoordenadasErroresPuntos() {
		return coordenadasErroresPuntos;
	}

	/**
	 * @param coordenadasErroresPuntos the coordenadasErroresPuntos to set
	 */
	public void setCoordenadasErroresPuntos(
			List<CoordenadasCurvaCrecimientoDto> coordenadasErroresPuntos) {
		this.coordenadasErroresPuntos = coordenadasErroresPuntos;
	}

}
