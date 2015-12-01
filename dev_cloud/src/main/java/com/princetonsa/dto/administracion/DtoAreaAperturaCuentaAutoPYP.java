package com.princetonsa.dto.administracion;

import java.io.Serializable;
import util.ConstantesBD;

public class DtoAreaAperturaCuentaAutoPYP  implements Serializable
{
	int institucion;
	int centroAtencion;
	int area;
	String nombreCentroAtencion;
	String nombreArea;
	
	void reset()
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.area = ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencion = "";
		this.nombreArea = "";
	}
	
	public DtoAreaAperturaCuentaAutoPYP ()
	{
		this.reset();
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the area
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the nombreArea
	 */
	public String getNombreArea() {
		return nombreArea;
	}

	/**
	 * @param nombreArea the nombreArea to set
	 */
	public void setNombreArea(String nombreArea) {
		this.nombreArea = nombreArea;
	}

	
	
}