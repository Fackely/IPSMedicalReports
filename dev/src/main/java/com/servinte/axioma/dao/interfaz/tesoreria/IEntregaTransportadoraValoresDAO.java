package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Entregas a Transportadora de Valores
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface IEntregaTransportadoraValoresDAO {
	
	/**
	 * M&eacute;todo que retorna una entrega a Transportadora de valores asociada a un c&oacute;digo espec&iacute;fico.
	 * 
	 * @param codigoPk
	 * @return
	 */
	public EntregaTransportadora obtenerEntregaTransportadoraPorCodigo (long codigoPk);
	
	
	/**
	 * Retorna un listado con las Entregas a Transportadora de Valores realizadas en un Turno de Caja
	 * espec&iacute;fico
	 * 
	 * @param turnoDeCaja
	 * @return
	 */
	public List<EntregaTransportadora> obtenerEntregasTransportadoraXTurnoCaja(TurnoDeCaja turnoDeCaja);
	
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Entrega a Transportadora de valores. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoEntTransValores(TurnoDeCaja turnoDeCaja);
	

	/**
	 * 
	 * M&eacute;todo utilizado para listar toda la informaci&oacute;n relacionada
	 * con las Entregas a Transportadora de valores asociadas a un Turno de caja
	 * o a un movimiento de caja espec&iacute;fico, si el movimiento pasado como par&aacute;metro 
	 * se encuentra registrado en el sistema
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValores(MovimientosCaja movimientosCaja);
	
	/**
	 * 
	 * M&eacute;todo utilizado para listar toda la informaci&oacute;n relacionada
	 * con las Entregas a Transportadora de valores asociadas a un Turno de caja
	 * o a un movimiento de caja espec&iacute;fico, si el movimiento pasado como par&aacute;metro 
	 * se encuentra registrado en el sistema
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValoresPorFecha(MovimientosCaja movimientosCaja);
	
}
