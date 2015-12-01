/*
 * Agosto 2, 2009
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;

/**
 * DTO: que representa una especialidad en Axioam
 * @author Sebastián Gómez
 *
 */
public class DtoEspecialidad implements Serializable
{
	private int codigo;
	private String consecutivo;
	private String nombre;
	private DtoCentroCosto centroCostoHonorarios;
	
	/**
	 * Reset
	 */
	public void clean()
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.consecutivo = "";
		this.nombre = "";
		this.centroCostoHonorarios = new DtoCentroCosto();
	}
	
	/**
	 * Constructor
	 */
	public DtoEspecialidad()
	{
		this.clean();
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
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
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
	 * @return the centroCostoHonorarios
	 */
	public DtoCentroCosto getCentroCostoHonorarios() {
		return centroCostoHonorarios;
	}

	/**
	 * @param centroCostoHonorarios the centroCostoHonorarios to set
	 */
	public void setCentroCostoHonorarios(DtoCentroCosto centroCostoHonorarios) {
		this.centroCostoHonorarios = centroCostoHonorarios;
	}
}
