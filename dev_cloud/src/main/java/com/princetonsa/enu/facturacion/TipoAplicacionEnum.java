/*
 * Ago 04,2008
 */
package com.princetonsa.enu.facturacion;

/**
 * Eum para los tiposd e plaicaciones de terceros
 * @author Sebastián Gómez
 *
 */
public enum TipoAplicacionEnum 
{
	SUJETO_A_RETENCION(1),
	NO_SUJETO_A_RETENCION(2),
	AUTORETENEDEOR(3);
	
	private TipoAplicacionEnum(int posicion) 
	{
		this.posicion = posicion;
	}
	
	private int posicion;

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
}
