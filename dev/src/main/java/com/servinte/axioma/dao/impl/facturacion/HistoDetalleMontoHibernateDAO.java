package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.IHistoDetalleMontoDAO;
import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.orm.delegate.facturacion.HistoDetalleMontoDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoHibernateDAO implements IHistoDetalleMontoDAO {
	
	HistoDetalleMontoDelegate delegate;
	
	public HistoDetalleMontoHibernateDAO(){
		delegate = new HistoDetalleMontoDelegate();
	}
	
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
		return delegate.guardarDetalleMontoCobro(histoDetalleMonto);
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar el histórico del detalle de un monto
	 * de cobro
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public HistoDetalleMonto buscarHistoricoDetallePorID(int idHistorico){
		return delegate.findById(idHistorico);
	}
	
}
