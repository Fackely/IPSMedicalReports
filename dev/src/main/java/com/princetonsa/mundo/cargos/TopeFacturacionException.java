package com.princetonsa.mundo.cargos;

/**
 * Clase Excepcion para controlar la restricción
 * UNIQUE INDEX 
 * "FACTURACION"."TOPES_FACTURACION" ("TIPO_REGIMEN", "ESTRATO_SOCIAL", "TIPO_MONTO", "INSTITUCION","VIGENCIA_INICIAL")
 * @author javrammo
 *
 */
public class TopeFacturacionException extends Exception {	

	public TopeFacturacionException(Exception e) {
		super(e);
	}

}
