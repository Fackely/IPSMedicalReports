package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXDevolReciboDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXDevolReciboMundo;
import com.servinte.axioma.orm.MovimientosCaja;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con CierreCajaXDevolRecibo
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICierreCajaXDevolReciboMundo
 */
public class CierreCajaXDevolReciboMundo implements ICierreCajaXDevolReciboMundo {

	/**
	 * DAO de CierreCajaDevolRecibo.
	 */
	private ICierreCajaXDevolReciboDAO cierreCajaXDevolReciboCajaDAO;

	
	public CierreCajaXDevolReciboMundo() {
		inicializar();
	}
	
	private void inicializar() {
		cierreCajaXDevolReciboCajaDAO = TesoreriaFabricaDAO.crearCierreCajaXDevolReciboDAO();
	}

	@Override
	public boolean asociarDevolucionesCierreCaja(List<DtoReciboDevolucion> listaDevolRecibosCajaTurno,MovimientosCaja movimientosCaja) {
		
		return cierreCajaXDevolReciboCajaDAO.asociarDevolucionesCierreCaja(listaDevolRecibosCajaTurno, movimientosCaja);
	}
}
