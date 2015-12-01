/**
 * 
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface MontoArticuloEspecificoDAO {
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * de artículos específicos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoArticuloEspecifico> buscarMontoArticuloEspecifico(Connection conn,
			DTOBusquedaMontoArticuloEspecifico dto);

}
