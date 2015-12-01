package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoDetalleMontoGeneralDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoGeneralMundo;
import com.servinte.axioma.orm.HistoDetMontoGen;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoGeneralMundo implements
		IHistoDetalleMontoGeneralMundo {
	
	IHistoDetalleMontoGeneralDAO dao;
	
	public HistoDetalleMontoGeneralMundo(){
		dao = FacturacionFabricaDAO.crearHistoDetalleMontoGeneralDAO();
	}

	/**
	 * 
	 * Este Método se encarga de guardar el histórico del detalle general
	 * de un monto de cobro
	 * 
	 * @param HistoDetMontoGen
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetMontoGen detalle){
		return dao.guardarDetalleMontoCobro(detalle);
	}

}
