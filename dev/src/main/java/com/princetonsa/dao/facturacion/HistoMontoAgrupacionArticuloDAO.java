package com.princetonsa.dao.facturacion;

import java.sql.Connection;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.HistoDetalleMonto;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public interface HistoMontoAgrupacionArticuloDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar un registro del hist�rico de
	 * una agrupaci�n de art�culos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOHistoMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int insertarHistoMontoAgrupacionArticulo(Connection conn,DTOBusquedaMontoAgrupacionArticulo registro, 
			HistoDetalleMonto histoDetalle, int TIPO_BD);

}
