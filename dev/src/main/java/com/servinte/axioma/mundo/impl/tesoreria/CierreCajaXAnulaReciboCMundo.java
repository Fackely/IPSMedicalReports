package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXAnulaReciboCDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXAnulaReciboCMundo;
import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con CierreCajaXAnulaReciboC
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICierreCajaXAnulaReciboCMundo
 */
public class CierreCajaXAnulaReciboCMundo implements ICierreCajaXAnulaReciboCMundo{

	/**
	 * DAO de los CierreCajaXAnulaReciboC.
	 */
	private ICierreCajaXAnulaReciboCDAO cierreCajaXAnulaReciboCDAO;

	
	public CierreCajaXAnulaReciboCMundo() {
		inicializar();
	}
	
	private void inicializar() {
		cierreCajaXAnulaReciboCDAO = TesoreriaFabricaDAO.crearCierreCajaXAnulaReciboCDAO();
	}

	@Override
	public boolean asociarAnulacionesRecibosCajaConCierreTurno(	List<AnulacionRecibosCaja> listaAnulacionesRecibosCaja, MovimientosCaja movimientosCaja) {
		
		return cierreCajaXAnulaReciboCDAO.asociarAnulacionesRecibosCajaConCierreTurno(listaAnulacionesRecibosCaja, movimientosCaja);
	}
}
