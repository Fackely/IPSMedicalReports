package com.servinte.axioma.dto.administracion;

import java.io.Serializable;

/**
 * Dto utilizado para cargar la información de todos los barrios
 * parametrizados en el sistema con el fin de validar los datos de 
 * entrada de los archivos del cargue masivo de pacientes 
 * 
 * @author Ricardo Ruiz
 *
 */
public class DtoBarrio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2353339906752510544L;

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
	 * Atributo que representa el código del barrio (NO es el primary Key)
	 */
	private String codigoBarrio;
	
	/**
	 * Constructor de la clase
	 */
	public DtoBarrio() {
		
	}
	
	/**
	 * Constructor con atributos
	 *
	 * @param codigoCiudad
	 * @param codigoDepartamento
	 * @param codigoPais
	 * @param codigoBarrio
	 */
	public DtoBarrio(String codigoCiudad, String codigoDepartamento, String codigoPais, String codigoBarrio) {
		this.codigoCiudad=codigoCiudad;
		this.codigoDepartamento=codigoDepartamento;
		this.codigoPais=codigoPais;
		this.codigoBarrio=codigoBarrio;
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
	 * @return the codigoBarrio
	 */
	public String getCodigoBarrio() {
		return codigoBarrio;
	}

	/**
	 * @param codigoBarrio the codigoBarrio to set
	 */
	public void setCodigoBarrio(String codigoBarrio) {
		this.codigoBarrio = codigoBarrio;
	}
}
