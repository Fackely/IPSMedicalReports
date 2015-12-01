package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con CierreCajaXDevolRecibo
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXDevolReciboMundo
 */

public interface ICierreCajaXDevolReciboMundo {


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
