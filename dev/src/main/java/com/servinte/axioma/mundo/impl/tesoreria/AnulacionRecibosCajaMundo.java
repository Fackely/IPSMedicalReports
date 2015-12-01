package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IAnulacionRecibosCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAnulacionRecibosCajaMundo;
import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con
 * las Anulaciones de Recibos de Caja
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IAnulacionRecibosCajaMundo
 */

public class AnulacionRecibosCajaMundo implements IAnulacionRecibosCajaMundo {


	private IAnulacionRecibosCajaDAO anulacionRecibosCajaDAO;
	
	public AnulacionRecibosCajaMundo() {
		inicializar();
	}

	private void inicializar() {
		anulacionRecibosCajaDAO = TesoreriaFabricaDAO.crearAnulacionRecibosCajaDAO();
	}


	@Override
	public List<AnulacionRecibosCaja> obtenerAnulacionesRecibosCajaXTurnoCaja(TurnoDeCaja turnoDeCaja) {
	
		return anulacionRecibosCajaDAO.obtenerAnulacionesRecibosCajaXTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IAnulacionRecibosCajaMundo#obtenerFechaUltimoMovimientoAnulaciones(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoAnulaciones(TurnoDeCaja turnoDeCaja) {

		return anulacionRecibosCajaDAO.obtenerFechaUltimoMovimientoAnulaciones(turnoDeCaja);
	}

}
