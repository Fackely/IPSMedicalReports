package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.IHistoMontosCobroDAO;
import com.servinte.axioma.orm.HistoMontosCobro;
import com.servinte.axioma.orm.delegate.facturacion.HistoMontosCobroDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad  HistoMontosCobro
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoMontosCobroHibernateDAO implements IHistoMontosCobroDAO {
	
	HistoMontosCobroDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public HistoMontosCobroHibernateDAO() {
		delegate = new HistoMontosCobroDelegate();
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
		return delegate.guardarHistoDetalleMontoCobro(histoMonto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los historicos de un monto
	 * de cobro por su id 
	 * 
	 * @param int id
	 * @return HistoMontosCobro
	 * @author, Angela Maria Aguirre
	 *
	 */
	public HistoMontosCobro buscarHistorialPorID(int id){
		return delegate.findById(id);
	}

}
