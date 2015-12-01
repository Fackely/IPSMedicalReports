package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaTransportadoraDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaTransportadoraMundo;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * CierreCajaTransportadora
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICierreCajaTransportadoraMundo
 */

public class CierreCajaTransportadoraMundo implements ICierreCajaTransportadoraMundo{

	
	/**
	 * DAO de los CierreCajaTransportadora.
	 */
	private ICierreCajaTransportadoraDAO cierreCajaTransportadoraDAO;

	
	public CierreCajaTransportadoraMundo() {
		inicializar();
	}
	
	private void inicializar() {
		cierreCajaTransportadoraDAO = TesoreriaFabricaDAO.crearCierreCajaTransportadoraDAO();
	}
	
	@Override
	public boolean asociarEntregaTransportadoraCierreCaja(List<EntregaTransportadora> listaEntregaTransportadora,MovimientosCaja movimientosCaja) {
		
		return cierreCajaTransportadoraDAO.asociarEntregaTransportadoraCierreCaja(listaEntregaTransportadora, movimientosCaja);
	}

}
