package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.HistoMontosCobro;
import com.servinte.axioma.orm.HistoMontosCobroHome;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad HistoMontosCobro
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoMontosCobroDelegate extends HistoMontosCobroHome {
	
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
	public boolean guardarHistoDetalleMontoCobro(HistoMontosCobro histoMonto){
		boolean save = true;					
		try{
			super.persist(histoMonto);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"hist�rico monto de cobro: ",e);
		}				
		return save;				
	}	

}
