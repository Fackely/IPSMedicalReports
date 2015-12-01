package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.orm.LogParamPresupuestoCap;

/**
 * Define la logica de negocio relacionada con el Log de parametrizaci�n
 * de presupuesto de capitaci�n 
 * @author diecorqu
 *
 */
public interface ILogParametrizacionPresupuestoCapMundo {

	/**
	 * Este m�todo retorna el log de parametrizaci�n por id
	 * 
	 * @param codigo Log
	 * @return LogParamPresupuestoCap
	 */
	public LogParamPresupuestoCap findById(long codigoLog);
	
	/**
	 * Este m�todo guarda el log para la parametrizacion del presupuesto
	 * 
	 * @param Log Parametrizacion del presupuesto
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap logParametrizacion);
	
	/**
	 * Este m�todo verifica si existe log para la parametrizaci�n del presupuesto
	 * de una parametrizaci�n dada
	 * 
	 * @param codigo de la parametrizaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeLogParametrizacionPresupuesto(
			long codParametrizacion);
	
	/**
	 * Este m�todo verifica si existe log con un motivo de modificaci�n asociado
	 * 
	 * @param codigo motivo de modificaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeLogParametrizacionMotivoModificacion(
			long codMotivoModificacion);
	
	/**
	 * Este m�todo obtiene Log para la parametrizaci�n del presupuesto
	 * de una parametrizaci�n dada
	 * 
	 * @param codigo de la parametrizaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public LogParamPresupuestoCap obtenerLogParametrizacionPresupuesto(
			long codParametrizacion);
	
	/**
	 * Este m�todo obtiene la lista de resultados para la consulta de logs de parametrizaci�n
	 * del presupuesto entre fechas, convenio, contrato y a�o de vigencia 
	 * 
	 * @param DtoLogBusquedaParametrizacion Dto con los datos requeridos para la consulta
	 * @return ArrayList<DtoLogParamPresupCap> lista con resultados
	 */
	public ArrayList<DtoLogParamPresupCap> listarResultadosBusquedaLogParametrizacionPresupuesto(
			DtoLogBusquedaParametrizacion dtoBusqueda);
	
	/**
	 * Este m�todo modifica el Log para la parametrizaci�n del presupuesto
	 * 
	 * @param LogParamPresupuestoCap Log Parametrizaci�n Presupuesto
	 * @return LogParamPresupuestoCap modificado
	 */
	public LogParamPresupuestoCap modificarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap log);
	
	/**
	 * Este m�todo elimina el Log para la parametrizaci�n del presupuesto
	 * 
	 * @param c�digo Log a eliminar
	 * @return boolean con el resultado de la operaci�n de eliminado
	 */
	public void eliminarLogParametrizacionPresupuesto(
			LogParamPresupuestoCap log);
}
