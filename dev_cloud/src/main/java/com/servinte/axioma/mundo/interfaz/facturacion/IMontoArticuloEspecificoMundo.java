

package com.servinte.axioma.mundo.interfaz.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IMontoArticuloEspecificoMundo {
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un art�culo espec�fico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarArticuloEspecifico(int id);
	
	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de
	 * de art�culos espec�ficos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoArticuloEspecifico> buscarMontoArticuloEspecifico(
			DTOBusquedaMontoArticuloEspecifico dto);

}
