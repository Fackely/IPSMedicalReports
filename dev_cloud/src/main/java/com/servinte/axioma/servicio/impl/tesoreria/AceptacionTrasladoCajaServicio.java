package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.IAceptacionTrasladoCajaServicio;

/**
 * Esta clase se encarga de definir los métodos de
 * negocio para la entidad Aceptacion Solicitud Traslado Caja Recaudo
 * 
 * @author Jorge Armando Agudelo Quintero
 * @since 04/11/2010
 */

public class AceptacionTrasladoCajaServicio implements IAceptacionTrasladoCajaServicio {


	private IAceptacionTrasladoCajaMundo aceptacionTrasladoCajaMundo;
	
	public AceptacionTrasladoCajaServicio() {
		inicializar();
	}

	private void inicializar() {
		aceptacionTrasladoCajaMundo = TesoreriaFabricaMundo.crearAceptacionTrasladoCajaMundo();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo#obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<AceptacionTrasladoCaja> obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(
			TurnoDeCaja turnoDeCaja) {
		
		return aceptacionTrasladoCajaMundo.obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo#obtenerTotalesAceptacionSolicitudFormaPagoNinguno(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesAceptacionSolicitudFormaPagoNinguno(TurnoDeCaja turnoDeCaja) {
		
		return aceptacionTrasladoCajaMundo.obtenerTotalesAceptacionSolicitudFormaPagoNinguno(turnoDeCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio#existeAceptacionTrasladoPorSolicitudes(java.lang.String)
	 */
	@Override
	public boolean existeAceptacionTrasladoPorSolicitudes(ArrayList<Long> codigosSolicitudes) {
		
		return aceptacionTrasladoCajaMundo.existeAceptacionTrasladoPorSolicitudes(codigosSolicitudes);
	}
}
