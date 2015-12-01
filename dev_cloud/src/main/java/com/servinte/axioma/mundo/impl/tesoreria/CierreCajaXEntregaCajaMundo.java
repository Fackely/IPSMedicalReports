
package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXEntregaCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXEntregaCajaMundo;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * CierreCajaXEntregaCaja
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICierreCajaXEntregaCaja
 */
public class CierreCajaXEntregaCajaMundo implements ICierreCajaXEntregaCajaMundo{


	/**
	 * DAO de los CierreCajaXEntregaCaja.
	 */
	private ICierreCajaXEntregaCajaDAO cierreCajaXEntregaCajaDAO;

	
	public CierreCajaXEntregaCajaMundo() {
		inicializar();
	}
	
	private void inicializar() {
		cierreCajaXEntregaCajaDAO = TesoreriaFabricaDAO.crearCierreCajaXEntregaCajaDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXEntregaCajaMundo#asociarEntregaCajaCierreCaja(java.util.List, com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public boolean asociarEntregaCajaCierreCaja(List<EntregaCajaMayor> listaEntregasCajaMayorPrincipal,
			MovimientosCaja movimientosCaja) {
	
		return cierreCajaXEntregaCajaDAO.asociarEntregaCajaCierreCaja(listaEntregasCajaMayorPrincipal, movimientosCaja);
	}
	
}
