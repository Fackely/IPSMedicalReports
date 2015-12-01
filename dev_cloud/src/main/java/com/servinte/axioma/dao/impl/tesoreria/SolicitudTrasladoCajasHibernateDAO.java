package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoRecaudoMayorEnCierre;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoTrasladoCaja;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.SolicitudTrasladoCajaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ICajasDAO}.
 * 
 * @author Cristhian Murillo
 * @see CajasDelegate.
 */

public class SolicitudTrasladoCajasHibernateDAO implements ISolicitudTrasladoCajaDAO{

	
	private SolicitudTrasladoCajaDelegate delegate = new SolicitudTrasladoCajaDelegate();
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO#obtenerSolicitudesPendientes(com.servinte.axioma.orm.Cajas)
	 */
	@Override
	public ArrayList<DtoSolicitudTrasladoPendiente> obtenerSolicitudesPendientes(Cajas caja) {
		return delegate.obtenerSolicitudesPendientes(caja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO#solicitudesAceptadasXTurnoCajaCajero(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoTrasladoCaja> obtenerSolicitudesAceptadasXTurnoCajaCajero(TurnoDeCaja turnoDeCaja) {
		
		return delegate.obtenerSolicitudesAceptadasXTurnoCajaCajero(turnoDeCaja);
	}
	
	/**
	 * Este método se encarga de consultar las solicitudes de traslados a caja de 
	 * recaudo y a caja mayor realizados en el cierre
	 * @param TurnoDeCaja
	 * @return ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre>
	 */
	public ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre> consultarTrasladosCajaRecaudoMayorEnCierre(TurnoDeCaja turnoDeCaja){
		return delegate.consultarTrasladosCajaRecaudoMayorEnCierre(turnoDeCaja);
	}
	
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
	@Override
	public ArrayList<DtoConsultaTrasladosCajasRecaudo> consultarRegistrosSolicitudTrasladoCaja(
			DtoConsultaTrasladosCajasRecaudo dto){
		return delegate.consultarRegistrosSolicitudTrasladoCaja(dto);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO#obtenerSolicitudPorConsecutivo(long)
	 */
	@Override
	public DtoTrasladoCaja obtenerSolicitudPorConsecutivo(long consecutivo) {

		return delegate.obtenerSolicitudPorConsecutivo(consecutivo);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO#obtenerFechaUltimoMovimientoAnulaciones(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoSolicitudAcept(TurnoDeCaja turnoDeCaja) {
	
		return delegate.obtenerFechaUltimoMovimientoAnulaciones(turnoDeCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO#obtenerDocSopSolicitudTrasladoCaja(long)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerDocSopSolicitudTrasladoCaja(	long codigoSolicitud) {
	
		return delegate.obtenerDocSopSolicitudTrasladoCaja(codigoSolicitud);
	}
		
}
