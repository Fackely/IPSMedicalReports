package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXReciboCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXReciboCajaMundo;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con CierreCajaReciboCaja
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICierreCajaXReciboCajaMundo
 */
public class CierreCajaXReciboCajaMundo implements ICierreCajaXReciboCajaMundo {

	/**
	 * DAO de los CierreCajaReciboCaja.
	 */
	private ICierreCajaXReciboCajaDAO cierreCajaXReciboCajaDAO;

	
	public CierreCajaXReciboCajaMundo() {
		inicializar();
	}
	
	private void inicializar() {
		cierreCajaXReciboCajaDAO = TesoreriaFabricaDAO.crearCierreCajaXReciboCajaDAO();
	}

	@Override
	public boolean asociarReciboCajaConCierreTurno(List<RecibosCaja> listaRecibosCajaTurno,	MovimientosCaja movimientosCaja) {
		
		return cierreCajaXReciboCajaDAO.asociarReciboCajaConCierreTurno(listaRecibosCajaTurno, movimientosCaja);
	}
}
