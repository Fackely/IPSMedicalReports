package com.princetonsa.enu.interfaz;

public enum CampoMoviCuentasXCobrar 
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
	VALOR_DEBITO(13),
	VALOR_CREDITO(14),
	OBSERVACIONES_MOVIMIENTO(17),
	TIPO_DOC_CRUCE(19),
	NRO_DOC_CRUCE(20),
	FECHA_VEN_DOC(22),
	FECHA_PAG_DOC(23),
	TERCERO_VENDEDOR(31),
	OBS_SALDO_ABI(32)
	;
	
	private CampoMoviCuentasXCobrar(int posicion) 
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
