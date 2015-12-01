package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO;
import com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo;
import com.servinte.axioma.orm.LogParamPresupuestoCap;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Implementaci&oacute;n de la interfaz {@link ILogParametrizacionPresupuestoCapMundo}
 * @author diecorqu
 */
public class LogParametrizacionPresupuestoMundo implements
		ILogParametrizacionPresupuestoCapMundo {

	ILogParametrizacionPresupuestoCapDAO logParametrizacionDAO;
	
	public LogParametrizacionPresupuestoMundo() {
		logParametrizacionDAO = CapitacionFabricaDAO.crearLogParametrizacionPresupuestoCapDAO();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#findById(long)
	 */
	@Override
	public LogParamPresupuestoCap findById(long codigoLog) {
		LogParamPresupuestoCap logParametrizacion = null;
		try {
			UtilidadTransaccion.getTransaccion().begin();
			logParametrizacion = logParametrizacionDAO.findById(codigoLog);
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logParametrizacion;
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#guardarLogParametrizacionPresupuesto(com.servinte.axioma.orm.LogParamPresupuestoCap)
	 */
	@Override
	public boolean guardarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap logParametrizacion) {
		return logParametrizacionDAO.guardarLogParametrizacionPresupuesto(
				logParametrizacion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#existeLogParametrizacionPresupuesto(long)
	 */
	@Override
	public boolean existeLogParametrizacionPresupuesto(long codParametrizacion) {
		return logParametrizacionDAO.existeLogParametrizacionPresupuesto(
				codParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#existeLogParametrizacionMotivoModificacion(long)
	 */
	@Override
	public boolean existeLogParametrizacionMotivoModificacion(
			long codMotivoModificacion) {
		return logParametrizacionDAO.existeLogParametrizacionMotivoModificacion(
				codMotivoModificacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#obtenerLogParametrizacionPresupuesto(long)
	 */
	@Override
	public LogParamPresupuestoCap obtenerLogParametrizacionPresupuesto(
			long codParametrizacion) {
		return logParametrizacionDAO.obtenerLogParametrizacionPresupuesto(
				codParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#modificarLogParametrizacionPresupuesto(com.servinte.axioma.orm.LogParamPresupuestoCap)
	 */
	@Override
	public LogParamPresupuestoCap modificarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap log) {
		return logParametrizacionDAO.modificarLogParametrizacionPresupuesto(log);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#eliminarLogParametrizacionPresupuesto(com.servinte.axioma.orm.LogParamPresupuestoCap)
	 */
	@Override
	public void eliminarLogParametrizacionPresupuesto(LogParamPresupuestoCap log) {
		logParametrizacionDAO.eliminarLogParametrizacionPresupuesto(log);

	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo#listarResultadosBusquedaLogParametrizacionPresupuesto(com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion)
	 */
	@Override
	public ArrayList<DtoLogParamPresupCap> listarResultadosBusquedaLogParametrizacionPresupuesto(
			DtoLogBusquedaParametrizacion dtoBusqueda) {
		return logParametrizacionDAO.listarResultadosBusquedaLogParametrizacionPresupuesto(dtoBusqueda);
	}

}
