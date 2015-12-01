package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con CierreCajaXEntregaCajaMundo
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXEntregaCajaMundo
 */
public interface ICierreCajaXEntregaCajaMundo {

	
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
