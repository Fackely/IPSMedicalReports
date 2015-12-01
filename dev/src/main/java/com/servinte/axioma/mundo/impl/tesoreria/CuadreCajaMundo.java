
package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICuadreCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICuadreCajaMundo;
import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * {@link CuadreCaja}
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICuadreCajaMundo
 */
public class CuadreCajaMundo implements ICuadreCajaMundo{


	/**
	 * DAO para los objeto {@link CuadreCaja}.
	 */
	private ICuadreCajaDAO cuadreCajaDAO;

	
	public CuadreCajaMundo() {
		inicializar();
	}
	
	private void inicializar() {
		
		cuadreCajaDAO = TesoreriaFabricaDAO.crearCuadreCajaDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ICuadreCajaMundo#consultarCuadreCajaPorMovimiento(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<CuadreCaja> consultarCuadreCajaPorMovimiento(MovimientosCaja arqueoCaja) {
	
		return cuadreCajaDAO.consultarCuadreCajaPorMovimiento(arqueoCaja);
	}

	
}
