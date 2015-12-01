package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoRecaudoMayorEnCierre;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoTrasladoCaja;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface ISolicitudTrasladoCajaDAO {
	
	/**
	 * Retorna las solicitudes pendientes para la caja
	 * @param caja
	 * @return {@link DtoSolicitudTrasladoPendiente}
	 */
	public ArrayList<DtoSolicitudTrasladoPendiente> obtenerSolicitudesPendientes(Cajas caja);
	
	
	/**
	 * Lista de las solicitudes de apertura de turno de caja con faltante sobrante y 
	 * con valor recibido por caja y cajero pasados como par&aacute;metro
	 * 
	 * @param turnoDeCaja
	 * @return  List<@link DtoTrasladoCaja}>
	 */
	public List<DtoTrasladoCaja> obtenerSolicitudesAceptadasXTurnoCajaCajero(TurnoDeCaja turnoDeCaja);
	
	/**
	 * Este método se encarga de consultar las solicitudes de traslados a caja de 
	 * recaudo y a caja mayor realizados en el cierre
	 * @param TurnoDeCaja
	 * @return ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre>
	 */
	public ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre> consultarTrasladosCajaRecaudoMayorEnCierre(TurnoDeCaja turnoDeCaja);
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar los registros de las 
	 * solicitudes de traslado de caja recaudo
	 * 
	 * @param DtoConsultaTrasladosCajasRecaudo
	 * @return ArrayList<DtoConsultaTrasladosCajasRecaudo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoConsultaTrasladosCajasRecaudo> consultarRegistrosSolicitudTrasladoCaja(
			DtoConsultaTrasladosCajasRecaudo dto);
	
	/**
	 * Solicitud espec&iacute;fica, seg&uacute;n el consecutivo pasado como par&aacute;metro. 
	 * 
	 * @param consecutivo
	 * @return {@link DtoTrasladoCaja} con la Solicitud espec&iacute;fica
	 */
	public DtoTrasladoCaja obtenerSolicitudPorConsecutivo (long consecutivo);
	
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Solicitud de Traslado a Caja en estado aceptada. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoSolicitudAcept(TurnoDeCaja turnoDeCaja);
	
	/**
	 * M&eacute;todo que se encarga de obtener toda la informaci&oacute;n asociada
	 * a una Solicitud de Traslado a Caja espec&iacute;fica.
	 * 
	 * @param codigoSolicitud
	 * @return
	 */
	public List<DtoDetalleDocSopor> obtenerDocSopSolicitudTrasladoCaja (long codigoSolicitud);
}
