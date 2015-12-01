package com.servinte.axioma.servicio.impl.facturacion;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontoAgrupacionArticuloMundo;
import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoMontoAgrupacionArticuloServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public class HistoMontoAgrupacionArticuloServicio implements
		IHistoMontoAgrupacionArticuloServicio {
	
	IHistoMontoAgrupacionArticuloMundo mundo;
	
	public HistoMontoAgrupacionArticuloServicio(){
		mundo = FacturacionFabricaMundo.crearHistoMontoAgrupacionArticuloMundo();
	}
	
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
			HistoDetalleMonto histoDetalle){
		return mundo.insertarHistoMontoAgrupacionArticulo(registro, histoDetalle);
	}

}
