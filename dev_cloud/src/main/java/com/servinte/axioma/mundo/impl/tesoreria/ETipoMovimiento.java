package com.servinte.axioma.mundo.impl.tesoreria;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;

/**
 * Enumeraci&oacute;n con los tipos de Movimientos relacionados con una caja.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see DtoConsolidadoMovimiento
 */
public enum ETipoMovimiento {
	
	ARQUEO_CAJA, 
	ARQUEO_ENTREGA_PARCIAL, 
	CIERRE_CAJA,
	ACEPTACION_APERTURA_TURNO, 
	ENTREGA_TRANSPORTADORA_VALORES, 
	ENTREGA_CAJA_MAYOR_PRINCIPAL,
	SOLICITUD_TRASLADO;
}