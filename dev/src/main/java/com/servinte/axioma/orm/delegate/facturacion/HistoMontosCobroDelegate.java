package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.HistoMontosCobro;
import com.servinte.axioma.orm.HistoMontosCobroHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad HistoMontosCobro
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoMontosCobroDelegate extends HistoMontosCobroHome {
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico de un
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
					"histórico monto de cobro: ",e);
		}				
		return save;				
	}	

}
