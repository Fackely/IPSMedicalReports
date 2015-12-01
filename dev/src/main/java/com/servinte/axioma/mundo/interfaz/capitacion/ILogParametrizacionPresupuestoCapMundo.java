package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.orm.LogParamPresupuestoCap;

/**
 * Define la logica de negocio relacionada con el Log de parametrización
 * de presupuesto de capitación 
 * @author diecorqu
 *
 */
public interface ILogParametrizacionPresupuestoCapMundo {

	/**
	 * Este método retorna el log de parametrización por id
	 * 
	 * @param codigo Log
	 * @return LogParamPresupuestoCap
	 */
	public LogParamPresupuestoCap findById(long codigoLog);
	
	/**
	 * Este método guarda el log para la parametrizacion del presupuesto
	 * 
	 * @param Log Parametrizacion del presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap logParametrizacion);
	
	/**
	 * Este método verifica si existe log para la parametrización del presupuesto
	 * de una parametrización dada
	 * 
	 * @param codigo de la parametrización
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeLogParametrizacionPresupuesto(
			long codParametrizacion);
	
	/**
	 * Este método verifica si existe log con un motivo de modificación asociado
	 * 
	 * @param codigo motivo de modificación
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeLogParametrizacionMotivoModificacion(
			long codMotivoModificacion);
	
	/**
	 * Este método obtiene Log para la parametrización del presupuesto
	 * de una parametrización dada
	 * 
	 * @param codigo de la parametrización
	 * @return boolean con el resultado de la operación
	 */
	public LogParamPresupuestoCap obtenerLogParametrizacionPresupuesto(
			long codParametrizacion);
	
	/**
	 * Este método obtiene la lista de resultados para la consulta de logs de parametrización
	 * del presupuesto entre fechas, convenio, contrato y año de vigencia 
	 * 
	 * @param DtoLogBusquedaParametrizacion Dto con los datos requeridos para la consulta
	 * @return ArrayList<DtoLogParamPresupCap> lista con resultados
	 */
	public ArrayList<DtoLogParamPresupCap> listarResultadosBusquedaLogParametrizacionPresupuesto(
			DtoLogBusquedaParametrizacion dtoBusqueda);
	
	/**
	 * Este método modifica el Log para la parametrización del presupuesto
	 * 
	 * @param LogParamPresupuestoCap Log Parametrización Presupuesto
	 * @return LogParamPresupuestoCap modificado
	 */
	public LogParamPresupuestoCap modificarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap log);
	
	/**
	 * Este método elimina el Log para la parametrización del presupuesto
	 * 
	 * @param código Log a eliminar
	 * @return boolean con el resultado de la operación de eliminado
	 */
	public void eliminarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap log);
}
