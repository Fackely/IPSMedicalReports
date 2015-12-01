package com.princetonsa.enu.interfaz;

public enum CampoLineaEvento 
{
	NUMERO_REGISTRO(0),
	COMPANIA(4),
	CODIGO_SEGUIMIENTO_EVENTO(6),
	FECHA_SEGUIMIENTO(7),
	HORA_SEGUIMIENTO(8),
	NOTAS_SEGUIMIENTO_EVENTO(9),
	FECHA(14),
	AUXILIAR_CUENTA_CONTABLE(15),
	TERCERO(16),
	CENTRO_OPERACION_MOVIMIENTO(17),
	UNIDAD_NEGOCIO(18),
	TIPO_DOC_CRUCE(20),
	NUMERO_DOCUMENTO_CRUCE(21);
	
	private CampoLineaEvento(int posicion) 
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
