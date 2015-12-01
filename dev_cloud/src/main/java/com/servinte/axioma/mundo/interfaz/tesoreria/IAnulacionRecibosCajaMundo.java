package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.Date;
import java.util.List;

import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Define la lógica de negocio relacionada con las funciones 
 * de la Anulación de Recibos de Caja
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface IAnulacionRecibosCajaMundo {

	
	/**
	 * Listado con las Anulaciones de Recibos de Caja asociadas a un Turno de Caja espec&iacute;fico
	 * 
	 * @param turnoDeCaja
	 * @return List<{@link AnulacionRecibosCaja}> con las Anulaciones de Recibos de Caja asociadas a un Turno de Caja espec&iacute;fico
	 */
	public List<AnulacionRecibosCaja> obtenerAnulacionesRecibosCajaXTurnoCaja (TurnoDeCaja turnoDeCaja);
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * Anulaci&oacute;n de un recibo de caja que est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * Se toman en cuenta las Anulaciones en estado aprobado.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoAnulaciones(TurnoDeCaja turnoDeCaja);
	
}
