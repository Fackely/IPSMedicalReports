package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de CierreCajaTransportadora 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface ICierreCajaTransportadoraDAO {


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
