/**
 * Esta clase se encarga de
 * @author, Angela Maria Aguirre
 * @since 15/07/2010
 */
package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposMovimientoCaja;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 15/07/2010
 */
public interface ITiposMovimientoCajaServicio {
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMovimientoCaja> obtenerTiposMovimientoCajaFiltradoPorID(Integer[] filtro);

}
