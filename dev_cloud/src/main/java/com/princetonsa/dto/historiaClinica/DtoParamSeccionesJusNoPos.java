package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

public class DtoParamSeccionesJusNoPos implements Serializable
{
	private String codigo;
	private String nombre;
	private String mostrar;
	private String etiqueta;
	private String orden;
	private String columnas;
	private String mostrarEtiqueta;
	private String seccionPadre;
	private ArrayList<DtoParamCamposJusNoPos> campos;
	
	/**
	 * Resetea todas las variables del DTO
	 */
	public void clean()
	{
		this.codigo="";
		this.nombre="";
		this.mostrar="";
		this.etiqueta="";
		this.orden="";
		this.columnas="";
		this.mostrarEtiqueta="";
		this.seccionPadre="";
		this.campos = new ArrayList<DtoParamCamposJusNoPos>();
	}
	
	/**
	 * 
	 */
	public DtoParamSeccionesJusNoPos()
	{
		this.clean();
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
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
	 * @return the mostrar
	 */
	public String getMostrar() {
		return mostrar;
	}

	/**
	 * @param mostrar the mostrar to set
	 */
	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * @return the orden
	 */
	public String getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}

	/**
	 * @return the campos
	 */
	public ArrayList<DtoParamCamposJusNoPos> getCampos() {
		return campos;
	}

	/**
	 * @param campos the campos to set
	 */
	public void setCampos(ArrayList<DtoParamCamposJusNoPos> campos) {
		this.campos = campos;
	}

	/**
	 * @return the columnas
	 */
	public String getColumnas() {
		return columnas;
	}

	/**
	 * @param columnas the columnas to set
	 */
	public void setColumnas(String columnas) {
		this.columnas = columnas;
	}

	/**
	 * @return the mostrarEtiqueta
	 */
	public String getMostrarEtiqueta() {
		return mostrarEtiqueta;
	}

	/**
	 * @param mostrarEtiqueta the mostrarEtiqueta to set
	 */
	public void setMostrarEtiqueta(String mostrarEtiqueta) {
		this.mostrarEtiqueta = mostrarEtiqueta;
	}

	/**
	 * @return the seccionPadre
	 */
	public String getSeccionPadre() {
		return seccionPadre;
	}

	/**
	 * @param seccionPadre the seccionPadre to set
	 */
	public void setSeccionPadre(String seccionPadre) {
		this.seccionPadre = seccionPadre;
	}
	
	
}
