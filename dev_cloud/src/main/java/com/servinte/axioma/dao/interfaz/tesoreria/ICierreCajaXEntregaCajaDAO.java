package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de CierreCajaXEntregaCaja 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface ICierreCajaXEntregaCajaDAO {

	/**
	 * 
	 * M&eacute;todo que registra la asociaci&oacute;n entre las Entregas a Caja Mayor / Principal realizadas
	 * durante un turno de caja espec&iacute;fico junto con el movimiento de cierre pasado por par&aacute;metro
	 *
	 * @param listaEntregasCajaMayorPrincipal
	 * @param movimientosCaja
	 * @return boolean indicando si la asociaci&oacute;n fue exitosa o no
	 */
	public boolean asociarEntregaCajaCierreCaja (List<EntregaCajaMayor> listaEntregasCajaMayorPrincipal, MovimientosCaja movimientosCaja);
	
	
}
