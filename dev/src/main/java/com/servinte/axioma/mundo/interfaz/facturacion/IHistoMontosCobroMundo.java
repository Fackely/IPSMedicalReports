package com.servinte.axioma.mundo.interfaz.facturacion;

import com.servinte.axioma.orm.HistoMontosCobro;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad HistoMontosCobro
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public interface IHistoMontosCobroMundo {
	
	/**
	 * 
	 * Este M�todo se encarga de guardar el hist�rico de un
	 * monto de cobro
	 * 
	 * @param HistoMontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarHistoDetalleMontoCobro(HistoMontosCobro histoMonto);

}
