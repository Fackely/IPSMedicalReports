package com.princetonsa.dto.historiaClinica;

public class DtoMedicamentosOriginales {
	
	/**
	 * codigo medicamento
	 */
	private Integer codigo;
	
	/**
	 *nombre medicamento 
	 */
	private String medicamento;
	
	/**
	 * concentracion asociada
	 */
	private String concentracion;
	
	/**
	 *forma farmaceutica 
	 */
	private String formaFarmaceutica;
	
	/**
	 *unidad de mediad  
	 */
	private String unidadMedida;
	
	/**
	 * Contructor de clase 
	 */
	public DtoMedicamentosOriginales() {
		this.codigo= new Integer(0);
		this.concentracion= "";
		this.formaFarmaceutica= "";
		this.medicamento= "";
		this.unidadMedida= "";
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
	 * @return the medicamento
	 */
	public String getMedicamento() {
		return medicamento;
	}

	/**
	 * @param medicamento the medicamento to set
	 */
	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
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
	
	
	
	

}
