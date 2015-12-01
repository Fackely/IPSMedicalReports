package com.princetonsa.enu.interfaz;

public enum CampoMoviDocumentoContable 
{
	NUMERO_REGISTRO(0),
	COMPANIA(4),
	CENTRO_OPERACION(5),
	TIPO_DOCUMENTO(6),
	NRO_DOCUMENTO(7),
	AUX_CUENT_CONTABLE(8),
	TERCERO(9),
	CENTRO_OPER_MOVI(10),
	UNIDAD_NEGOCIO(11),
	AUX_CENTRO_COSTO(12),
	AUX_CONCEP_FLUJO_EFE(13),
	VALOR_DEBITO(14),
	VALOR_CREDITO(15),
	VALOR_BASE_GRAVABLE(18),
	TIPO_DOCUMENTO_BANCO(19),
	NUMERO_DOCUMENTO_BANCO(20),
	OBSERVACIONES_MOVIMI(21);
	
	private CampoMoviDocumentoContable(int posicion) 
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