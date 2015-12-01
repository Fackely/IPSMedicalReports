package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.orm.HistoDetalleMontoHome;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * de la entidad HistoDetalleMonto
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoDelegate extends HistoDetalleMontoHome {
	
	/**
	 * 
	 * Este M�todo se encarga de guardar el hist�rico del detalle
	 * de un monto de cobro
	 * 
	 * @param HistoDetalleMonto
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetalleMonto histoDetalleMonto){
		boolean save = true;					
		try{
			super.persist(histoDetalleMonto);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"hist�rico del detalle de montos de cobro: ",e);
		}				
		return save;				
	}

}
