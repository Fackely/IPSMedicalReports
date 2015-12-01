package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.HistoMontosCobro;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * de la entidad  HistoMontosCobro
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public interface IHistoMontosCobroDAO {
	
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
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los historicos de un monto
	 * de cobro por su id 
	 * 
	 * @param int id
	 * @return HistoMontosCobro
	 * @author, Angela Maria Aguirre
	 *
	 */
	public HistoMontosCobro buscarHistorialPorID(int id);

}
