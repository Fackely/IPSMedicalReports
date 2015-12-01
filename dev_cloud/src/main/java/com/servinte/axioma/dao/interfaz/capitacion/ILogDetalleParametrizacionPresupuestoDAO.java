package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.LogDetalleParamPresup;

/**
 * Define la logica de negocio relacionada con el detalle del
 * Log para la parametrización del presupuesto de capitación
 * @author diecorqu
 *
 */
public interface ILogDetalleParametrizacionPresupuestoDAO {

	/**
	 * Este método retorna el log detalle de la parametrización por codigo
	 * 
	 * @param codigo Log
	 * @return LogParamPresupuestoCap
	 */
	public LogDetalleParamPresup findById(long codigoLogDetalle);

	/**
	 * Este método guarda el log detalle para la parametrización del presupuesto
	 * 
	 * @param Log Detalle Parametrizacion del presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalleParametrizacion);

	/**
	 * Este método verifica si existe log detalle para la parametrización del presupuesto
	 * de una parametrización dada
	 * 
	 * @param codigo de la parametrización
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeLogDetalleParametrizacionPresupuesto(
			long codLogParametrizacion);

	/**
	 * Este método obtiene el Log detallado para la parametrización del presupuesto
	 * 
	 * @param codigo del log general
	 * @return boolean con el resultado de la operación
	 */
	public ArrayList<LogDetalleParamPresup> obtenerLogDetalladoParametrizacionPresupuesto(
			long codigoLogGeneral);

	/**
	 * Este método modifica el Log para la parametrización del presupuesto
	 * 
	 * @param LogParamPresupuestoCap Log Parametrización Presupuesto
	 * @return LogParamPresupuestoCap modificado
	 */
	public LogDetalleParamPresup modificarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalle);

	/**
	 * Este método elimina el Log detalle para la parametrización del presupuesto
	 * 
	 * @param código Log detalle a eliminar
	 * @return boolean con el resultado de la operación de eliminado
	 */
	public void eliminarLogDetalleParametrizacion(LogDetalleParamPresup logDetalle);
	
}
