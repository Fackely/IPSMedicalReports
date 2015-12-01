package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de CierreCajaXAnulaReciboC 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface ICierreCajaXAnulaReciboCDAO {

	
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
