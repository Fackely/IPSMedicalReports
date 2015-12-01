package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoGeneralMundo;
import com.servinte.axioma.orm.HistoDetMontoGen;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoDetalleMontoGeneralServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio 
 * para la entidad HistoDetalleMontoGeneral
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoGeneralServicio implements
		IHistoDetalleMontoGeneralServicio {
	
	IHistoDetalleMontoGeneralMundo mundo;
	
	public HistoDetalleMontoGeneralServicio(){
		mundo = FacturacionFabricaMundo.crearHistoDetalleMontoGeneralMundo();
	}

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
		return mundo.guardarDetalleMontoCobro(detalle);
	}

}
