package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IMontoArticuloEspecificoServicio {
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un artículo específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarArticuloEspecifico(int id);
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * de artículos específicos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoArticuloEspecifico> buscarMontoArticuloEspecifico(
			DTOBusquedaMontoArticuloEspecifico dto);

}
