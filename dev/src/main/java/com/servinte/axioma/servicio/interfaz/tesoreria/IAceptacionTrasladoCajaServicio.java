package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;


/**
 * Esta clase se encarga de definir los métodos de
 * negocio para la entidad Aceptacion Solicitud Traslado Caja Recaudo
 * 
 * @author Jorge Armando Agudelo Quintero
 * @since 04/11/2010
 */
public interface IAceptacionTrasladoCajaServicio {

	
	/**
	 * Aceptaciones por Turno de Caja
	 * 
	 * @param turnoDeCaja
	 * @return List<{@link AceptacionTrasladoCaja}> de Aceptaciones por turno de Caja
	 */
	
	public List<AceptacionTrasladoCaja> obtenerAceptacionesSolicitudTrasladoPorTurnoCaja (TurnoDeCaja turnoDeCaja);

	
	/**
	 * M&eacute;todo que se encarga de totalizar por forma de pago asociada a tipo "NINGUNO", las Aceptaciones de Solicitud de 
	 * Traslado a Caja de Recaudo realizadas.
	 * 
	 * @param turnoDeCaja
	 * @return
	 */
	public List<DtoDetalleDocSopor>  obtenerTotalesAceptacionSolicitudFormaPagoNinguno (TurnoDeCaja turnoDeCaja);
	
	/**
	 * Método que se encarga de verificar que no exista un registro de Aceptacion para Solicitudes
	 * de traslado a Cajas de Recaudo realizadas.
	 * 
	 * @param codigosSolicitudes
	 * @return
	 */
	public boolean existeAceptacionTrasladoPorSolicitudes (ArrayList<Long> codigosSolicitudes);
}
