package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de CierreCajaXReciboCaja 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface ICierreCajaXReciboCajaDAO {

	
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
