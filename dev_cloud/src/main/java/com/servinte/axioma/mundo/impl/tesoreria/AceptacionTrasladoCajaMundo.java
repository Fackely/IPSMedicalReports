package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IAceptacionTrasladoCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con
 * Aceptaciones de Solicitudes de Traslado a Caja
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IAceptacionTrasladoCajaMundo
 */

public class AceptacionTrasladoCajaMundo implements IAceptacionTrasladoCajaMundo {


	private IAceptacionTrasladoCajaDAO aceptacionTrasladoCajaDAO;
	
	public AceptacionTrasladoCajaMundo() {
		inicializar();
	}

	private void inicializar() {
		aceptacionTrasladoCajaDAO = TesoreriaFabricaDAO.crearAceptacionTrasladoCajaDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo#obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<AceptacionTrasladoCaja> obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(
			TurnoDeCaja turnoDeCaja) {
		
		return aceptacionTrasladoCajaDAO.obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo#obtenerTotalesAceptacionSolicitudFormaPagoNinguno(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesAceptacionSolicitudFormaPagoNinguno(TurnoDeCaja turnoDeCaja) {
		
		return aceptacionTrasladoCajaDAO.obtenerTotalesAceptacionSolicitudFormaPagoNinguno(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo#existeAceptacionTrasladoPorSolicitudes(java.lang.String)
	 */
	@Override
	public boolean existeAceptacionTrasladoPorSolicitudes(ArrayList<Long> codigosSolicitudes) {
		
		return aceptacionTrasladoCajaDAO.existeAceptacionTrasladoPorSolicitudes(codigosSolicitudes);
		
	}

}
