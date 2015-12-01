package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con CierreCajaXReciboCaja
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXReciboCajaMundo
 */

public interface ICierreCajaXReciboCajaMundo {

	
	/**
	 * M&eacute;todo que registra la asociaci&oacute;n entre los recibos de caja y un movimiento de cierre 
	 * espec&iacute;fico
	 *
	 * @param listaRecibosCajaTurno
	 * @param movimientosCaja
	 * @return boolean indicando si se realiz&oacute; el proceso de actualizaci&oacute;n
	 */
	public boolean asociarReciboCajaConCierreTurno (List<RecibosCaja> listaRecibosCajaTurno, MovimientosCaja movimientosCaja);
}
