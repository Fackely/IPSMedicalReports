package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.CierreCajaAcepTrasCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con {@link CierreCajaAcepTrasCaja}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CierreCajaAcepTrasCajaMundo
 */

public interface ICierreCajaAcepTrasCajaMundo {

	
	/**
	 * M&eacute;todo que registra la asociaci&oacute;n entre las Aceptaciones de las 
	 * Solicitudes de Traslado a Caja con el movimiento de cierre espec&iacute;fico
	 *
	 * @param listaSolicitudesAceptadas
	 * @param movimientosCaja
	 * @return boolean indicando si se realiz&oacute; el proceso de actualizaci&oacute;n
	 */
	public boolean asociarAceptacionesSolicitudTrasladoCierreCaja (List<AceptacionTrasladoCaja> listaSolicitudesAceptadas,MovimientosCaja movimientosCaja);
}
