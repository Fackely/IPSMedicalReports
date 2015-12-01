package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con los 
 * documentos de soporte de los movimientos de caja
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.DocSopMovimCajasMundo
 */

public interface IDocSopMovimCajasMundo {


	
	/**
	 * Listado de los detalles de documentos/movimientos para la aceptacion
	 * @param idMovimiento
	 * @return 
	 */
	public DtoInformacionEntrega consolidarInformacionAceptacion(long idMovimiento, int codigoInstitucion);
	
	
	/**
	 * Retorna un listado con los detalles de las entregas a Caja Mayor / Principal y/o Transportadora de valores realizadas en un turno 
	 * espec&iacute;fico. Adem&aacute;s si el movimiento enviado esta registrado en el sistema, solo se lista la infomaci&oacute;n asociada 
	 * a este movimiento, sin tener en cuenta el tipo.
	 * 
	 * No se involucran los detalles de entregas realizadas que esten asociados a Formas de Pago de Tipo "NINGUNO"
	 *  
	 * @param movimientosCaja
	 * @return listado con las Entregas a Caja Mayor / Principal y/o Transportadora de valores realizadas en un turno espec&iacute;fico. 
	 * (Sin formas de pago tipo "NINGUNO")
	 * 
	 * @see DtoDetalleDocSopor
	 */
	public List<DtoDetalleDocSopor> obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna (MovimientosCaja movimientosCaja);
	
	
	/**
	 * Retorna un listado con las Aceptaciones realizadas en un turno espec&iacute;fico,
	 * pero sin incluir las que involucren la(s) forma de pago tipo "NINGUNO".
	 * 
	 * @param movimientosCaja
	 * @return listado con las Aceptaciones realizadas en un turno espec&iacute;fico sin incluir 
	 * las que involucren la(s) forma de pago tipo "NINGUNO"
	 * @see DtoDetalleDocSopor
	 */
	public List<DtoDetalleDocSopor> obtenerAceptacionesCajaNoFormaPagoNinguno (MovimientosCaja movimientosCaja);

	/**
	 * M&eacute;todo que retorna los {@link DocSopMovimCajas} asociados a un movimiento espec&iacute;fico.
	 * @param codigoMovimientosCaja
	 * @return
	 */
	public List<DocSopMovimCajas> obtenerDocSoportePorMovimiento(long codigoMovimientosCaja);
	
	
	/**
	 * Listado con los detalles de los documentos de soporte para todas las formas de pago
	 * diferentes de tipo efectivo de un movimiento de Solicitud espec&iacute;fico.
	 * 
	 * @param idMovimiento
	 * @return List<{@link DtoDetalleDocSopor}>
	 */
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacion(long idMovimiento);
	
	
	/**
	 * Listado con los detalles de los documentos de soporte asociados a la forma de pago tipo efectivo
	 * de un movimiento de Solicitud espec&iacute;fico. 
	 * 
	 * @param idMovimiento
	 * @return List<{@link DtoDetalleDocSopor}>
	 */
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacionEfectivo(long idMovimiento);
	
	
	/**
	 * M&eacute;todo que se encarga de totalizar los valores entregados para las formas de pago de tipo "NINGUNO"
	 * a Transportadora de Valores y/o Caja Mayor / Principal asociadas a un turno de caja espec&iacute;fico.
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	public List<DtoDetalleDocSopor>  obtenerTotalesEntregasFormaPagoNinguno (MovimientosCaja movimientosCaja);
	
	
	
//	/**
//	 * Retorna un listado con las Aceptaciones de efectivo realizadas en un turno espec&iacute;fico.
//	 * 
//	 * @param movimientosCaja
//	 * @return listado con las Aceptaciones  de efectivo realizadas en un turno espec&iacute;fico.
//	 * @see DtoDetalleDocSopor
//	 */
//	public List<DtoDetalleDocSopor> obtenerListadoAceptacionesEfectivo (MovimientosCaja movimientosCaja);

	
//	/**
//	 * M&eacute;todo que retorna un listado con los documentos de soporte para la forma de pago tipo efectivo, asociados a un Turno de Caja y
//	 * que se han entregado en un movimiento de caja de tipo (s) incluido en el arreglo codigosTipoMovimiento.
//	 * 
//	 * Tambi&eacute;n se puede realizar la b&uacute;squeda por el c&oacute;digo espec&iacute;fico de un movimiento de caja.
//	 * 
//	 * @param turnoDeCaja
//	 * @param codigosTipoMovimiento
//	 * @param codigoMovimiento
//	 * @return
//	 */
//	public List<DocSopMovimCajas> obtenerListaDocSoporEfectPorTipoMovimiento(TurnoDeCaja turnoDeCaja, ArrayList<Integer> codigosTipoMovimiento, long codigoMovimiento);
	
//	
//	/**
//	 * Listado con las Entregas a caja mayor principal realizadas por 
//	 * la caja que realiza el arqueo
//	 * 
//	 * @param movimientosCaja
//	 * @return List<{@link DtoEntregaCaja}>
//	 */
//	public List<DtoEntregaCaja> obtenerEntregasCajaMayor(MovimientosCaja movimientosCaja);
	
	
//	/**
//	 * Listado con las Entregas a Transportadora de valores realizadas
//	 * por la caja que realiza el arqueo
//	 * 
//	 * @param movimientosCaja
//	 * @return List<{@link DtoEntregaCaja}>
//	 */
//	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValores(MovimientosCaja movimientosCaja);
//	
}
