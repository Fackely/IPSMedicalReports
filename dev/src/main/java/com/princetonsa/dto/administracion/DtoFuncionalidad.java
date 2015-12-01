package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo de las funcionalidades
 * @author Gio.
 */
public class DtoFuncionalidad implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private int codigo;
	private String nombre;
	private String archivo;
	private String etiqueta;
	private String deboImprimir;
	private String esParametrizable;
		
	/**
	 * Constructor
	 */
	public DtoFuncionalidad()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTO
	 */
	public void clean()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.archivo = "";
		this.etiqueta = "";
		this.deboImprimir = "";
		this.esParametrizable = "";
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getDeboImprimir() {
		return deboImprimir;
	}

	public void setDeboImprimir(String deboImprimir) {
		this.deboImprimir = deboImprimir;
	}

	public String getEsParametrizable() {
		return esParametrizable;
	}

	public void setEsParametrizable(String esParametrizable) {
		this.esParametrizable = esParametrizable;
	}
	
	
}
