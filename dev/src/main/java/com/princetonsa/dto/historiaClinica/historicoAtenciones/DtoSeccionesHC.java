/**
 * 
 */
package com.princetonsa.dto.historiaClinica.historicoAtenciones;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author axioma
 *
 */
public class DtoSeccionesHC implements Serializable
{
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private String activo;
	
	/**
	 * 
	 */
	private int numeroColumnas;
	
	/**
	 * 
	 */
	private ArrayList<DtoCamposSeccionesHistoricoHC> camposSecciones;

	/**
	 * @return the camposSecciones
	 */
	public ArrayList<DtoCamposSeccionesHistoricoHC> getCamposSecciones() {
		return camposSecciones;
	}

	/**
	 * @param camposSecciones the camposSecciones to set
	 */
	public void setCamposSecciones(
			ArrayList<DtoCamposSeccionesHistoricoHC> camposSecciones) {
		this.camposSecciones = camposSecciones;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the numeroColumnas
	 */
	public int getNumeroColumnas() {
		return numeroColumnas;
	}

	/**
	 * @param numeroColumnas the numeroColumnas to set
	 */
	public void setNumeroColumnas(int numeroColumnas) {
		this.numeroColumnas = numeroColumnas;
	}
	
	
	
	
}
