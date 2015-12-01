package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaAcepTrasCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaAcepTrasCajaMundo;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.CierreCajaAcepTrasCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con
 * {@link CierreCajaAcepTrasCaja}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICierreCajaAcepTrasCajaMundo
 */

public class CierreCajaAcepTrasCajaMundo implements ICierreCajaAcepTrasCajaMundo {

	private ICierreCajaAcepTrasCajaDAO cierreCajaAcepTrasCajaDAO;
	
	public CierreCajaAcepTrasCajaMundo() {
		inicializar();
	}

	private void inicializar() {
		cierreCajaAcepTrasCajaDAO = TesoreriaFabricaDAO.crearCierreCajaAcepTrasCajaDAO();
	}
	
	@Override
	public boolean asociarAceptacionesSolicitudTrasladoCierreCaja(List<AceptacionTrasladoCaja> listaSolicitudesAceptadas, MovimientosCaja movimientosCaja) {
		
		return cierreCajaAcepTrasCajaDAO.asociarAceptacionesSolicitudTrasladoCierreCaja(listaSolicitudesAceptadas, movimientosCaja);
	}

}
