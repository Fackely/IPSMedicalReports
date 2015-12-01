package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.orm.TiposMovimientoCaja;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Tipo Movimiento de Caja.
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 *
 */

public interface ITiposMovimientoCajaDAO {
	
	
	
	/**
	 * Listado con los tipos de arqueo registrados en tipos_movimiento
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
