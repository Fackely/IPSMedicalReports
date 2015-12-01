
package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO;
import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.orm.delegate.facturacion.HistoricoEncabezadoDelegate;

/**
 * Implementación de la interfaz {@link IHistoricoEncabezadoDAO}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class HistoricoEncabezadoHibernateDAO implements IHistoricoEncabezadoDAO{

	
	private HistoricoEncabezadoDelegate historicoEncabezadoDelegate;

	
	/**
	 * Constructor de la Clase
	 */
	public HistoricoEncabezadoHibernateDAO() {
		
		historicoEncabezadoDelegate =  new HistoricoEncabezadoDelegate();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO#insertar(com.servinte.axioma.orm.HistoricoEncabezado)
	 */
	@Override
	public boolean insertar(HistoricoEncabezado historicoEncabezado) {
	
		return historicoEncabezadoDelegate.insertar(historicoEncabezado);
		
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO#eliminar(com.servinte.axioma.orm.HistoricoEncabezado)
	 */
	@Override
	public boolean eliminar(HistoricoEncabezado historicoEncabezado) {
		
		return historicoEncabezadoDelegate.eliminar(historicoEncabezado);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO#findById(long)
	 */
	@Override
	public HistoricoEncabezado findById(long codigoHistoricoEncabezado) {
		return historicoEncabezadoDelegate.findById(codigoHistoricoEncabezado);
	}

}
