/**
 * Esta clase se encarga de
 * @author, Angela Maria Aguirre
 * @since 15/07/2010
 */
package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITiposMovimientoCajaMundo;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposMovimientoCajaServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 15/07/2010
 */
public class TiposMovimientoCajaServicio implements ITiposMovimientoCajaServicio {
	
	/**
	 * Instancia de la interface de ITiposMovimientoMundo 
	 */
	ITiposMovimientoCajaMundo tiposMovimiento = TesoreriaFabricaMundo.crearTiposMovimientoCajaMundo();
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<DtoIntegridadDominio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<TiposMovimientoCaja> obtenerTiposMovimientoCajaFiltradoPorID(
			Integer[] filtro) {		
		return tiposMovimiento.obtenerTiposMovimientoCajaFiltradoPorID(filtro);
	}	
}
