package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.HistoDetMontoGen;
import com.servinte.axioma.orm.HistoDetMontoGenHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio 
 * para la entidad HistoDetalleMontoGeneral
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoGeneralDelegate extends HistoDetMontoGenHome {
	
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
	public boolean guardarDetalleMontoCobro(HistoDetMontoGen detalle){
		boolean save = true;					
		try{
			super.persist(detalle);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"histórico del detalle general de montos de cobro: ",e);
		}				
		return save;				
	}	


}
