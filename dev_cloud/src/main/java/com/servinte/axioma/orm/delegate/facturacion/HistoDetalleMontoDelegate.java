package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.orm.HistoDetalleMontoHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad HistoDetalleMonto
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoDelegate extends HistoDetalleMontoHome {
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico del detalle
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
					"histórico del detalle de montos de cobro: ",e);
		}				
		return save;				
	}

}
