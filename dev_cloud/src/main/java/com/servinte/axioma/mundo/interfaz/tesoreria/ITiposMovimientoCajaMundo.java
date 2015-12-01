package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.orm.TiposMovimientoCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con los Tipos de Movimiento de caja
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see com.servinte.axioma.mundo.impl.tesoreria.TiposMovimientoCajaMundo
 */

public interface ITiposMovimientoCajaMundo {
	
	
	/**
	 * Listado con los tipos de arqueo registrados en la entidad tipos_movimiento_caja
	 * @return ArrayList<{@link TiposMovimientoCaja}>
	 */
	public List<TiposMovimientoCaja> obtenerListadoTiposArqueo();
	
	
	/**
	 * Retorna un Tipo de Movimiento espec&iacute;fico identificado con el codigoTipoMovimiento
	 * @param codigoTipoMovimiento
	 * @return TiposMovimientoCaja
	 */
	public TiposMovimientoCaja findById (int codigoTipoMovimiento);
	
	/**
	 * 
	 * Este método se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMovimientoCaja> obtenerTiposMovimientoCajaFiltradoPorID(Integer[] filtro);
}
