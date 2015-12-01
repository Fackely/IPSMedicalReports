package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.servinte.axioma.dao.interfaz.capitacion.IUnidadPagoDAO;
import com.servinte.axioma.orm.UnidadPago;
import com.servinte.axioma.orm.delegate.capitacion.UnidadPagoDelegate;

/**
 * Clase para el acceso a datos de la entidad UnidadPago
 * @author Ricardo Ruiz
 *
 */
public class UnidadPagoHibernateDAO implements IUnidadPagoDAO{

	UnidadPagoDelegate delegate;
	
	public UnidadPagoHibernateDAO(){
		delegate=new UnidadPagoDelegate();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUnidadPagoDAO#consultarUnidadPagoPorFecha(java.util.Date)
	 */
	@Override
	public ArrayList<UnidadPago> consultarUnidadPagoPorFecha(Date fecha) {
		return delegate.consultarUnidadPagoPorFecha(fecha);
	}

}
