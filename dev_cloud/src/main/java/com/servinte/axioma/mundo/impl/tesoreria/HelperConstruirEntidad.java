package com.servinte.axioma.mundo.impl.tesoreria;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.servinte.axioma.mundo.fabrica.ConstruccionEntidadFabrica;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Helper utilizado para construir una entidad espec&iacute;fica
 * @author Jorge Armando Agudelo
 *
 */
public abstract class HelperConstruirEntidad {

	/**
	 * Constructor de la clase
	 */
	private HelperConstruirEntidad() {}
	
	/**
	 * M&eacute;todo que ayuda a construir un movimiento de caja con la informacion asociada seg&uacute;n el tipo al
	 * que corresponda.
	 * 
	 * @param dtoConsolidadoMovimiento
	 * @return un objeto de tipo MovimientosCaja con la informaci&oacute;n debidamente relacionada y listo para continuar con el
	 * proceso de registro
	 */
	public static MovimientosCaja construirEntidad (DtoConsolidadoMovimiento dtoConsolidadoMovimiento){

		return ConstruccionEntidadFabrica.construirEntidad(dtoConsolidadoMovimiento);
	}
}
