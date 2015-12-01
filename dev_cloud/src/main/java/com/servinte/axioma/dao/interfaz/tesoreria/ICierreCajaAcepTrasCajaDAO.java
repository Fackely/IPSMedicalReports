package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de CierreCajaAcepTrasCaja 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface ICierreCajaAcepTrasCajaDAO {

	
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
