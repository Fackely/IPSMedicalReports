package com.princetonsa.enu.interfaz;

public enum CampoDocumentoContable 
{
	NUMERO_REGISTRO(0),
	TIPO_REGISTRO(1),
	COMPANIA(4),
	CENTRO_OPERACION(6),
	TIPO_DOCUMENTO(7),
	NRO_DOCUMENTO(8),
	FECHA_DOCUMENTO(9),
	TERCERO_DOCUMENTO(10),
	OBSERVACIONES_DOCUMENTO(14)
	;
	
	private CampoDocumentoContable(int posicion) 
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
