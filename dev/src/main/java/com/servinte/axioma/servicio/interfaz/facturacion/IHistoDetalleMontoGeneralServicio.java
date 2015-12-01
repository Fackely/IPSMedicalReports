package com.servinte.axioma.servicio.interfaz.facturacion;

import com.servinte.axioma.orm.HistoDetMontoGen;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad HistoMontosCobro
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public interface IHistoDetalleMontoGeneralServicio {
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico del detalle general
	 * de un monto de cobro
	 * 
	 * @param HistoDetMontoGenHome
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetMontoGen detalle);

}
