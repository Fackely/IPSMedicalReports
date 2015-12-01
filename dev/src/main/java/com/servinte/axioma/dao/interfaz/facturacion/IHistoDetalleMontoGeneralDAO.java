package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.HistoDetMontoGen;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad HistoMontosCobro
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public interface IHistoDetalleMontoGeneralDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de guardar el hist�rico de un
	 * monto de cobro
	 * 
	 * @param HistoDetMontoGen
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetMontoGen detalle);

}
