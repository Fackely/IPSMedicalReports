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
 * DTO que represetna la tabla art_incluidos_servippal
 * @author Sebastián Gómez R.
 *
 */
public class DtoArtIncluidoServiPpal implements Serializable 
{
	private BigDecimal codigo;
	private InfoDatosInt articulo;
	private int cantidad;
	private InfoDatosInt farmacia;
	
	public void reset()
	{
		this.codigo = new BigDecimal(0);
		this.articulo = new InfoDatosInt(ConstantesBD.codigoNuncaValido);
		this.farmacia = new InfoDatosInt(ConstantesBD.codigoNuncaValido);
		this.cantidad = 0;
	}
	
	/**
	 * Cosntructor
	 */
	public DtoArtIncluidoServiPpal()
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
	 * @return the articulo
	 */
	public InfoDatosInt getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(InfoDatosInt articulo) {
		this.articulo = articulo;
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

	/**
	 * @return the farmacia
	 */
	public InfoDatosInt getFarmacia() {
		return farmacia;
	}

	/**
	 * @param farmacia the farmacia to set
	 */
	public void setFarmacia(InfoDatosInt farmacia) {
		this.farmacia = farmacia;
	}
}
