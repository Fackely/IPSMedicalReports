package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con CierreCajaTransportadora
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CierreCajaTransportadoraMundo
 */

public interface ICierreCajaTransportadoraMundo {


	/**
	 * 
	 * M&eacute;todo que registra la asociaci&oacute;n entre las Entregas a Transportadora de valores realizadas
	 * durante un turno de caja espec&iacute;fico junto con el movimiento de cierre pasado por par&aacute;metro
	 *
	 * @param listaEntregaTransportadora
	 * @param movimientosCaja
	 * @return boolean indicando si la asociaci&oacute;n fue exitosa o no
	 */
	public boolean asociarEntregaTransportadoraCierreCaja (List<EntregaTransportadora> listaEntregaTransportadora, MovimientosCaja movimientosCaja);
	
	
}
