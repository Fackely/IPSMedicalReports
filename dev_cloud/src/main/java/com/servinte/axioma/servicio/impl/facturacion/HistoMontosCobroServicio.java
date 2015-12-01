package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontosCobroMundo;
import com.servinte.axioma.orm.HistoMontosCobro;
import com.servinte.axioma.servicio.interfaz.facturacion.IHistoMontosCobroServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoMontosCobroServicio implements IHistoMontosCobroServicio {
	
	IHistoMontosCobroMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public HistoMontosCobroServicio(){
		mundo = FacturacionFabricaMundo.crearHistoMontosCobroMundo();
	}
	
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
		return mundo.guardarHistoDetalleMontoCobro(histoMonto);
	}

}
