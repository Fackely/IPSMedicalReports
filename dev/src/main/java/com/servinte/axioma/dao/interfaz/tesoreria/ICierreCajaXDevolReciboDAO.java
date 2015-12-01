package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de CierreCajaXDevolRecibo 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface ICierreCajaXDevolReciboDAO {

	
	/**
	 * M&eacute;todo que registra la asociaci&oacute;n entre las devoluciones de recibos de caja y un movimiento de cierre 
	 * espec&iacute;fico
	 *
	 * @param listaRecibosCajaTurno
	 * @param movimientosCaja
	 * @return boolean indicando si se realiz&oacute; el proceso de actualizaci&oacute;n
	 */
	public boolean asociarDevolucionesCierreCaja (List<DtoReciboDevolucion> listaDevolRecibosCajaTurno, MovimientosCaja movimientosCaja);
}
