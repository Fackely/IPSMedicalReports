/*
 * Ene 06, 2010
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * 
 * DTO que cosntituye la representacion de la tabla servi_incluido_servippal
 * @author Sebastián Gómez R.
 *
 */
public class DtoServiIncluidoServiPpal implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal codigo;
	private InfoDatosInt servicio;
	private InfoDatosInt centroCosto;
	private int cantidad;
	
	public void reset()
	{
		this.codigo = new BigDecimal(0);
		this.servicio = new InfoDatosInt(ConstantesBD.codigoNuncaValido);
		this.centroCosto = new InfoDatosInt(ConstantesBD.codigoNuncaValido);
		this.cantidad = 0;
	}
	
	/*
	 * Constructor
	 */
	public DtoServiIncluidoServiPpal()
	{
		this.reset();
	}

	/**
	 * @return the codigo
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the centroCosto
	 */
	public InfoDatosInt getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(InfoDatosInt centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
