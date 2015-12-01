package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de devoluciones de recibos de Caja.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IDevolRecibosCajaDAO {
	
	
	/**
	 * Listado con las devoluciones asociadas a los recibos de caja de un turno de caja / cajero, ordenadas
	 * ascendentemente por consecutivo.
	 * 
	 * Se tiene en cuenta las devoluciones que estan en estado Aprobado y Anulado. Si el movimiento de caja se encuentra registrado
	 * en el sistema, se involucra en la b&uacute;squeda los filtros de fecha y hora de generaci&oacute;n de la devoluci&oacute;n.
	 * 
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCaja(MovimientosCaja movimientosCaja);
	
	/**
	 * Listado con las devoluciones asociadas a los recibos de caja de un turno de caja / cajero, ordenadas
	 * ascendentemente por consecutivo.
	 * 
	 * Se tiene en cuenta las devoluciones que estan en estado Aprobado y Anulado. Si el movimiento de caja se encuentra registrado
	 * en el sistema, se involucra en la b&uacute;squeda los filtros de fecha y hora de generaci&oacute;n de la devoluci&oacute;n.
	 * 
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCajaFecha(MovimientosCaja movimientosCaja);
	
	/**
	 * M&eacute;todo que retorna el total de las devoluciones de los recibos de caja realizados en un turno espec&iacute;fico
	 * teniendo en cuenta un estado. Se totalizan las devoluciones realizadas por cada una de las formas de pago.
	 * 
	 * @param turnoDeCaja
	 * @param estado
	 * @return total de las devoluciones de los recibos de caja realizados en un turno espec&iacute;fico por forma de pago
	 */
	public List<DtoReciboDevolucion> obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(TurnoDeCaja turnoDeCaja, String estado);
	
	
//	/**
//	 * Actualiza el estado de las devoluciones que han sido relacionadas (afectando el efectivo entregado) en un movimiento de caja
//	 * de tipo Arqueo (Arego Entrega Parcial - Cierre Turno de Caja)
//	
//	 * @param listaDtoDevolucionesRecibos
//	 * @return boolean indicando si la actualizaci&oacute;n fue realizada exitosamente
//	 */
//	public boolean cambiarEstadoArqueoDevoluciones (List<DtoReciboDevolucion> listaDtoDevolucionesRecibos);
//	

	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * devoluci&oacute;n de un recibo de caja que est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * Se toman en cuenta todas las devoluciones sin tener en cuenta el estado.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoDevoluciones(TurnoDeCaja turnoDeCaja);
}
