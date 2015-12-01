package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con {@link CuadreCaja}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.CuadreCajaMundo
 */
public interface ICuadreCajaMundo {

	
	/**
	 * M&eacute;todo que se encarga de realizar la consulta del cuadre de caja registrado
	 * para un movimiento Arqueo Caja.
	 *
	 * @param arqueoCaja
	 * @return List<{@link CuadreCaja}> con la informaci&oacute;n registrada para el movimiento de Arqueo Caja.
	 */
	public List<CuadreCaja> consultarCuadreCajaPorMovimiento (MovimientosCaja arqueoCaja);
	
}
