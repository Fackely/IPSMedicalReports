package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.IHistoDetalleMontoGeneralDAO;
import com.servinte.axioma.orm.HistoDetMontoGen;
import com.servinte.axioma.orm.delegate.facturacion.HistoDetalleMontoGeneralDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoGeneralHibernateDAO implements
		IHistoDetalleMontoGeneralDAO {
	
	HistoDetalleMontoGeneralDelegate delegate;
	
	public HistoDetalleMontoGeneralHibernateDAO(){
		delegate = new HistoDetalleMontoGeneralDelegate();
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
		return delegate.guardarDetalleMontoCobro(detalle);
	}

}
