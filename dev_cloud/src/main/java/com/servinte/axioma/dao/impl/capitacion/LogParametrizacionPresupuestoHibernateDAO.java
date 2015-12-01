package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO;
import com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.orm.LogParamPresupuestoCap;
import com.servinte.axioma.orm.delegate.capitacion.LogParametrizacionPresupuestoCapDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ILogParametrizacionPresupuestoCapDAO}
 * @author diecorqu
 * @see LogParametrizacionPresupuestoCapDelegate
 */
public class LogParametrizacionPresupuestoHibernateDAO implements
		ILogParametrizacionPresupuestoCapDAO {

	LogParametrizacionPresupuestoCapDelegate delegate;
	
	public LogParametrizacionPresupuestoHibernateDAO() {
		delegate = new LogParametrizacionPresupuestoCapDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#findById(long)
	 */
	@Override
	public LogParamPresupuestoCap findById(long codigoLog) {
		return delegate.findById(codigoLog);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#guardarLogParametrizacionPresupuesto(com.servinte.axioma.orm.LogParamPresupuestoCap)
	 */
	@Override
	public boolean guardarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap logParametrizacion) {
		return delegate.guardarLogParametrizacionPresupuesto(logParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#existeLogParametrizacionPresupuesto(long)
	 */
	@Override
	public boolean existeLogParametrizacionPresupuesto(long codParametrizacion) {
		return delegate.existeLogParametrizacionPresupuesto(codParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#existeLogParametrizacionMotivoModificacion(long)
	 */
	@Override
	public boolean existeLogParametrizacionMotivoModificacion(
			long codMotivoModificacion) {
		return delegate.existeLogParametrizacionMotivoModificacion(
				codMotivoModificacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#obtenerLogParametrizacionPresupuesto(long)
	 */
	@Override
	public LogParamPresupuestoCap obtenerLogParametrizacionPresupuesto(
			long codParametrizacion) {
		return delegate.obtenerLogParametrizacionPresupuesto(codParametrizacion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#modificarLogParametrizacionPresupuesto(com.servinte.axioma.orm.LogParamPresupuestoCap)
	 */
	@Override
	public LogParamPresupuestoCap modificarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap log) {
		return delegate.modificarLogParametrizacionPresupuesto(log);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#eliminarLogParametrizacionPresupuesto(com.servinte.axioma.orm.LogParamPresupuestoCap)
	 */
	@Override
	public void eliminarLogParametrizacionPresupuesto(LogParamPresupuestoCap log) {
		delegate.eliminarLogParametrizacionPresupuesto(log);

	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO#listarResultadosBusquedaLogParametrizacionPresupuesto(com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion)
	 */
	@Override
	public ArrayList<DtoLogParamPresupCap> listarResultadosBusquedaLogParametrizacionPresupuesto(
			DtoLogBusquedaParametrizacion dtoBusqueda) {
		return delegate.listarResultadosBusquedaLogParametrizacionPresupuesto(dtoBusqueda);
	}

}
