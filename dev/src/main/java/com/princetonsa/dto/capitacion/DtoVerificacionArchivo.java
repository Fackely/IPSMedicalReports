package com.princetonsa.dto.capitacion;

import java.io.Serializable;

/**
 * Dto para consolidar las inconsistencioas del archivo de usuarios
 * capitados en cuanto a numero de columnas y separador
 * 
 * @author Ricardo Ruiz
 *
 */
public class DtoVerificacionArchivo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2925367011674368807L;
		
	/**
	 * Atributo que representa el número del archivo en el que se presenta la inconsistencia
	 */
	private String numeroRegistro;
	
	/**
	 * Atributo que representa el número de columnas para la inconsistencia
	 */
	private String numeroColumnas;
	
	/**
	 * Atributo que representa si se utiliza correctamente el separador
	 */
	private String separador;

	
	public DtoVerificacionArchivo(String numeroRegistro, String numeroColumnas,
			String separador) {
		this.numeroRegistro = numeroRegistro;
		this.numeroColumnas = numeroColumnas;
		this.separador = separador;
	}

	/**
	 * @return the numeroRegistro
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * @return the numeroColumnas
	 */
	public String getNumeroColumnas() {
		return numeroColumnas;
	}

	/**
	 * @param numeroColumnas the numeroColumnas to set
	 */
	public void setNumeroColumnas(String numeroColumnas) {
		this.numeroColumnas = numeroColumnas;
	}

	/**
	 * @return the separador
	 */
	public String getSeparador() {
		return separador;
	}

	/**
	 * @param separador the separador to set
	 */
	public void setSeparador(String separador) {
		this.separador = separador;
	}
	
	

}
