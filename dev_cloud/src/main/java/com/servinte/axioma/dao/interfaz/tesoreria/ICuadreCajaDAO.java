package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de {@link CuadreCaja} 
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface ICuadreCajaDAO {

	
	/**
	 * M&eacute;todo que se encarga de realizar la consulta del cuadre de caja registrado
	 * para un movimiento Arqueo Caja.
	 *
	 * @param arqueoCaja
	 * @return List<{@link CuadreCaja}> con la informaci&oacute;n registrada para el movimiento de Arqueo Caja.
	 */
	public List<CuadreCaja> consultarCuadreCajaPorMovimiento (MovimientosCaja arqueoCaja);
	
}
