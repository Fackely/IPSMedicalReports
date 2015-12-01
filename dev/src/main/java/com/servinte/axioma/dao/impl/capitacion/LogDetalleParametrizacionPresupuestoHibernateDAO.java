package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO;
import com.servinte.axioma.orm.LogDetalleParamPresup;
import com.servinte.axioma.orm.delegate.capitacion.LogDetalleParametrizacionPresupuestoDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ILogDetalleParametrizacionPresupuestoDAO}
 * @author diecorqu
 * @see LogDetalleParametrizacionPresupuestoDelegate
 */
public class LogDetalleParametrizacionPresupuestoHibernateDAO implements
		ILogDetalleParametrizacionPresupuestoDAO {

	LogDetalleParametrizacionPresupuestoDelegate delegate;
	
	public LogDetalleParametrizacionPresupuestoHibernateDAO() {
		delegate = new LogDetalleParametrizacionPresupuestoDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO#findById(long)
	 */
	@Override
	public LogDetalleParamPresup findById(long codigoLogDetalle) {
		return delegate.findById(codigoLogDetalle);
	}

	@Override
	public boolean guardarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalleParametrizacion) {
		return delegate.guardarLogDetalleParametrizacionPresupuesto(
				logDetalleParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO#existeLogDetalleParametrizacionPresupuesto(long)
	 */
	@Override
	public boolean existeLogDetalleParametrizacionPresupuesto(
			long codLogParametrizacion) {
		return delegate.existeLogDetalleParametrizacionPresupuesto(
				codLogParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO#obtenerLogDetalladoParametrizacionPresupuesto(long)
	 */
	@Override
	public ArrayList<LogDetalleParamPresup> obtenerLogDetalladoParametrizacionPresupuesto(
			long codigoLogGeneral) {
		return delegate.obtenerLogDetalladoParametrizacionPresupuesto(codigoLogGeneral);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO#modificarLogDetalleParametrizacionPresupuesto(com.servinte.axioma.orm.LogDetalleParamPresup)
	 */
	@Override
	public LogDetalleParamPresup modificarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalle) {
		return delegate.modificarLogDetalleParametrizacionPresupuesto(logDetalle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO#eliminarLogDetalleParametrizacion(com.servinte.axioma.orm.LogDetalleParamPresup)
	 */
	@Override
	public void eliminarLogDetalleParametrizacion(
			LogDetalleParamPresup logDetalle) {
		delegate.eliminarLogDetalleParametrizacion(logDetalle);

	}

}
