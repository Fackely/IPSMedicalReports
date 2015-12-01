package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * las Devoluciones de Recibos de Caja
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IDevolRecibosCajaMundo
 */

/**
 * @author axioma
 *
 */
/**
 * @author axioma
 *
 */
/**
 * @author axioma
 *
 */
public class DevolRecibosCajaMundo implements IDevolRecibosCajaMundo{

	
	private IDevolRecibosCajaDAO devolRecibosCajaDAO;

	public DevolRecibosCajaMundo() {
		inicializar();
	}
	
	private void inicializar() {
		devolRecibosCajaDAO = TesoreriaFabricaDAO.crearDevolRecibosCajaDAO();
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo#obtenerDevolRecibosCajaXMovimientoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCaja(MovimientosCaja movimientosCaja) {
		
		return devolRecibosCajaDAO.obtenerDevolRecibosCajaXMovimientoCaja(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo#obtenerDevolRecibosCajaXMovimientoCajaFecha(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCajaFecha(MovimientosCaja movimientosCaja) {
		
		return devolRecibosCajaDAO.obtenerDevolRecibosCajaXMovimientoCajaFecha(movimientosCaja);
	}
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo#obtenerFechaUltimoMovimientoDevoluciones(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoDevoluciones(TurnoDeCaja turnoDeCaja) {
		
		return devolRecibosCajaDAO.obtenerFechaUltimoMovimientoDevoluciones(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo#obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja, java.lang.String)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(TurnoDeCaja turnoDeCaja, String estado) {
	
		return devolRecibosCajaDAO.obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(turnoDeCaja, estado);
	}

}
