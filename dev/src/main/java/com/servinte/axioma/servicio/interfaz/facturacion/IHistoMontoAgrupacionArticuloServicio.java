package com.servinte.axioma.servicio.interfaz.facturacion;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.HistoDetalleMonto;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public interface IHistoMontoAgrupacionArticuloServicio {
	
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
	public int insertarHistoMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo registro, 
			HistoDetalleMonto histoDetalle);

}
