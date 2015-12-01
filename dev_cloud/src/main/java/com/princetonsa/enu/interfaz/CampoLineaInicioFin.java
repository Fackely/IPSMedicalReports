package com.princetonsa.enu.interfaz;

public enum CampoLineaInicioFin 
{
	NUMERO_REGISTRO(0),
	COMPANIA(4),
	CENTRO_OPRACION(6);
	
	private CampoLineaInicioFin(int posicion) 
	{
		this.posicion = posicion;
	}
	
	private int posicion;

	public int getPosicion() 
	{
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
}
