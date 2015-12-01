/**
 * 
 */
package com.servinte.axioma.dto.administracion;

/**
 * @author armando
 *
 */
public class DtoCiudades 
{
	/**
	 * 
	 */
	private String codigoCiudad;
	
	/**
	 * 
	 */
	private String descripcionCiudad;

	/**
	 * 
	 */
	private String localidad;
	
	/**
	 * 
	 */
	private DtoDepartamentos departamento;

	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public DtoDepartamentos getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DtoDepartamentos departamento) {
		this.departamento = departamento;
	}
}
