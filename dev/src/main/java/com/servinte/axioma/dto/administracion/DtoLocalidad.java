package com.servinte.axioma.dto.administracion;

/**
 * Dto utilizado para cargar la información de todos las localidades
 * parametrizados en el sistema con el fin de validar los datos de 
 * entrada de los archivos del cargue masivo de pacientes 
 * 
 * @author Ricardo Ruiz
 *
 */
public class DtoLocalidad {
	/**
	 * Atributo que representa el código de la ciudad
	 */
	private String codigoCiudad;
	
	/**
	 * Atributo que representa el código del departamento
	 */
	private String codigoDepartamento;
	
	/**
	 * Atributo que representa el código del país
	 */
	private String codigoPais;
	
	/**
	 * Atributo que representa el código de la localidad
	 */
	private String codigoLocalidad;
	
	/**
	 * Constructor de la clase
	 */
	public DtoLocalidad() {
		
	}
	
	/**
	 * Constructor con atributos
	 *
	 * @param codigoCiudad
	 * @param codigoDepartamento
	 * @param codigoPais
	 * @param codigoLocalidad
	 */
	public DtoLocalidad(String codigoCiudad, String codigoDepartamento, String codigoPais, String codigoLocalidad) {
		this.codigoCiudad=codigoCiudad;
		this.codigoDepartamento=codigoDepartamento;
		this.codigoPais=codigoPais;
		this.codigoLocalidad=codigoLocalidad;
	}

	/**
	 * @return the codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * @param codigoCiudad the codigoCiudad to set
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * @return the codigoDepartamento
	 */
	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	/**
	 * @param codigoDepartamento the codigoDepartamento to set
	 */
	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	/**
	 * @return the codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * @param codigoPais the codigoPais to set
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * @return the codigoLocalidad
	 */
	public String getCodigoLocalidad() {
		return codigoLocalidad;
	}

	/**
	 * @param codigoLocalidad the codigoLocalidad to set
	 */
	public void setCodigoLocalidad(String codigoLocalidad) {
		this.codigoLocalidad = codigoLocalidad;
	}
}
