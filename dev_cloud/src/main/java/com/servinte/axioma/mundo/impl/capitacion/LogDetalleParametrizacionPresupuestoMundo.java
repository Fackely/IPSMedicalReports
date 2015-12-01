package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo;
import com.servinte.axioma.orm.LogDetalleParamPresup;

/**
 * Implementaci&oacute;n de la interfaz {@link ILogDetalleParametrizacionPresupuestoMundo}
 * @author diecorqu
 *
 */
public class LogDetalleParametrizacionPresupuestoMundo implements
		ILogDetalleParametrizacionPresupuestoMundo {


	ILogDetalleParametrizacionPresupuestoDAO logDetalleDAO;
	
	public LogDetalleParametrizacionPresupuestoMundo() {
		logDetalleDAO = CapitacionFabricaDAO.crearLogDetalleParametrizacionPresupuestoDAO();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo#findById(long)
	 */
	@Override
	public LogDetalleParamPresup findById(long codigoLogDetalle) {
		return logDetalleDAO.findById(codigoLogDetalle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo#guardarLogDetalleParametrizacionPresupuesto(com.servinte.axioma.orm.LogDetalleParamPresup)
	 */
	@Override
	public boolean guardarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalleParametrizacion) {
		return logDetalleDAO.guardarLogDetalleParametrizacionPresupuesto(
				logDetalleParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo#existeLogDetalleParametrizacionPresupuesto(long)
	 */
	@Override
	public boolean existeLogDetalleParametrizacionPresupuesto(
			long codLogParametrizacion) {
		return logDetalleDAO.existeLogDetalleParametrizacionPresupuesto(
				codLogParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo#obtenerLogDetalladoParametrizacionPresupuesto(long)
	 */
	@Override
	public ArrayList<LogDetalleParamPresup> obtenerLogDetalladoParametrizacionPresupuesto(
			long codigoLogGeneral) {
		return logDetalleDAO.obtenerLogDetalladoParametrizacionPresupuesto(
				codigoLogGeneral);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo#modificarLogDetalleParametrizacionPresupuesto(com.servinte.axioma.orm.LogDetalleParamPresup)
	 */
	@Override
	public LogDetalleParamPresup modificarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalle) {
		return logDetalleDAO.modificarLogDetalleParametrizacionPresupuesto(logDetalle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo#eliminarLogDetalleParametrizacion(com.servinte.axioma.orm.LogDetalleParamPresup)
	 */
	@Override
	public void eliminarLogDetalleParametrizacion(
			LogDetalleParamPresup logDetalle) {
		logDetalleDAO.eliminarLogDetalleParametrizacion(logDetalle);

	}

}
