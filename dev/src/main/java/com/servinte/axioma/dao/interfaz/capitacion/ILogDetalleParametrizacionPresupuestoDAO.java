package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.LogDetalleParamPresup;

/**
 * Define la logica de negocio relacionada con el detalle del
 * Log para la parametrizaci�n del presupuesto de capitaci�n
 * @author diecorqu
 *
 */
public interface ILogDetalleParametrizacionPresupuestoDAO {

	/**
	 * Este m�todo retorna el log detalle de la parametrizaci�n por codigo
	 * 
	 * @param codigo Log
	 * @return LogParamPresupuestoCap
	 */
	public LogDetalleParamPresup findById(long codigoLogDetalle);

	/**
	 * Este m�todo guarda el log detalle para la parametrizaci�n del presupuesto
	 * 
	 * @param Log Detalle Parametrizacion del presupuesto
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalleParametrizacion);

	/**
	 * Este m�todo verifica si existe log detalle para la parametrizaci�n del presupuesto
	 * de una parametrizaci�n dada
	 * 
	 * @param codigo de la parametrizaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeLogDetalleParametrizacionPresupuesto(
			long codLogParametrizacion);

	/**
	 * Este m�todo obtiene el Log detallado para la parametrizaci�n del presupuesto
	 * 
	 * @param codigo del log general
	 * @return boolean con el resultado de la operaci�n
	 */
	public ArrayList<LogDetalleParamPresup> obtenerLogDetalladoParametrizacionPresupuesto(
			long codigoLogGeneral);

	/**
	 * Este m�todo modifica el Log para la parametrizaci�n del presupuesto
	 * 
	 * @param LogParamPresupuestoCap Log Parametrizaci�n Presupuesto
	 * @return LogParamPresupuestoCap modificado
	 */
	public LogDetalleParamPresup modificarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalle);

	/**
	 * Este m�todo elimina el Log detalle para la parametrizaci�n del presupuesto
	 * 
	 * @param c�digo Log detalle a eliminar
	 * @return boolean con el resultado de la operaci�n de eliminado
	 */
	public void eliminarLogDetalleParametrizacion(LogDetalleParamPresup logDetalle);
	
}
