package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.Date;

import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.TurnoDeCaja;

public interface IEntregaTransportadoraValoresServicio {
	
	/**
	 * M&eacute;todo que retorna una entrega a Transportadora de valores asociada a un c&oacute;digo espec&iacute;fico.
	 * 
	 * @param codigoPk
	 * @return
	 */
	public EntregaTransportadora obtenerEntregaTransportadoraPorCodigo(long codigoPk);


	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Entrega a Transportadora de valores. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoEntTransValores(TurnoDeCaja turnoDeCaja);
}
