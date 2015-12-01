package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.CierreCajaXAnulaReciboC;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con {@link CierreCajaXAnulaReciboC}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXAnulaReciboCMundo
 */

public interface ICierreCajaXAnulaReciboCMundo {

	
	/**
	 * M&eacute;todo que registra la asociaci&oacute;n entre las anulaciones de recibos de caja 
	 * y un movimiento de cierre espec&iacute;fico
	 *
	 * @param listaRecibosCajaTurno
	 * @param movimientosCaja
	 * @return boolean indicando si se realiz&oacute; el proceso de actualizaci&oacute;n
	 */
	public boolean asociarAnulacionesRecibosCajaConCierreTurno (List<AnulacionRecibosCaja> listaAnulacionesRecibosCaja, MovimientosCaja movimientosCaja);
}
