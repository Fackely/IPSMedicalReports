package com.servinte.axioma.dao.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.DevolRecibosCajaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IDevolRecibosCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see DevolRecibosCajaDelegate
 */

public class DevolRecibosCajaHibernateDAO implements IDevolRecibosCajaDAO{

	private DevolRecibosCajaDelegate devolRecibosCajaDAO;

	public DevolRecibosCajaHibernateDAO() {
	
		devolRecibosCajaDAO  = new DevolRecibosCajaDelegate();
		
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO#obtenerDevolRecibosCajaXMovimientoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCaja(MovimientosCaja movimientosCaja) {
		
		return devolRecibosCajaDAO.obtenerDevolRecibosCajaXMovimientoCaja(movimientosCaja);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO#obtenerDevolRecibosCajaXMovimientoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCajaFecha(MovimientosCaja movimientosCaja) {
		
		return devolRecibosCajaDAO.obtenerDevolRecibosCajaXMovimientoCajaFecha(movimientosCaja);
	}

	

//	/* (non-Javadoc)
//	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO#cambiarEstadoArqueoDevoluciones(java.util.List)
//	 */
//	@Override
//	public boolean cambiarEstadoArqueoDevoluciones(	List<DtoReciboDevolucion> listaDtoDevolucionesRecibos) {
//		
//		return devolRecibosCajaDAO.cambiarEstadoArqueoDevoluciones(listaDtoDevolucionesRecibos);
//	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO#obtenerFechaUltimoMovimientoDevoluciones(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoDevoluciones(TurnoDeCaja turnoDeCaja) {
	
		return devolRecibosCajaDAO.obtenerFechaUltimoMovimientoDevoluciones(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO#obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja, java.lang.String)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(	TurnoDeCaja turnoDeCaja, String estado) {
		
		return devolRecibosCajaDAO.obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(turnoDeCaja, estado);
	}

}
