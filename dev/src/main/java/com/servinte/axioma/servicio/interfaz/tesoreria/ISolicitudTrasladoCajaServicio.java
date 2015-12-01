package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Esta clase se encarga de definir los m&eacute;todos de
 * negocio para la entidad Solicitud Traslado Caja Recaudo
 * 
 * @author Angela Maria Aguirre
 * @since 6/08/2010
 */
public interface ISolicitudTrasladoCajaServicio {

	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los estados de una solicitud
	 * de traslado de caja
	 * 
	 * @param String[], filtro
	 * @return  ArrayList<DtoIntegridadDominio> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoSolicitudTrasladoCaja(String[] filtro);
	
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
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Solicitud de Traslado a Caja en estado aceptada. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoSolicitudAcept(TurnoDeCaja turnoDeCaja);
	
}
