
package com.servinte.axioma.mundo.interfaz.odontologia.presupuesto;

import java.util.List;

import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * 
 * Interface que define las operaciones
 * para gestionar lo relacionado con el objeto
 * 
 * {@link IncluPresuEncabezado}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IIncluPresuEncabezadoMundo {

	

	/**
	 * M�todo que permite realizar un registro de la entidad {@link IncluPresuEncabezado}
	 * 
	 * Con la informaci�n del proceso de contrataci�n y precontrataci�n 
	 * de Inclusiones de programas al plan de tratamiento y al presupuesto del paciente.
	 * 
	 * @param excluPresuEncabezado
	 * @return
	 */
	public long registrarInclusionPresupuestoEncabezado (IncluPresuEncabezado incluPresuEncabezado);
	
	
	/**
	 * M�todo que consulta las inclusiones asociadas al presupuesto activo del paciente
	 * 
	 * @param codigoPresupuesto
	 */
	public List<IncluPresuEncabezado> cargarRegistrosInclusion(long codigoPresupuesto);
	
	
	/**
	 * M�todo que consulta un registro del proceso de contrataci�n de Inclusiones
	 * 
	 * @param codigoPresupuesto
	 */
	public IncluPresuEncabezado cargarDetalleRegistroInclusion(long codigoIncluPresuEncabezado);
	
	
	/**
	 * M�todo que realiza el proceso de actualizaci�n de un registro de 
	 * Inclusi�n precontratada
	 * 
	 * @param incluPresuEncabezado
	 * @return
	 */
	public boolean actualizarIncluPresuEncabezado (IncluPresuEncabezado incluPresuEncabezado); 
	
	

	/**
	 * M�todo que carga un encabezado de Inclusi�n espec�fico
	 * 
	 * @param codigoPresupuesto
	 */
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado);


	/**
	 * Eliminar todo el detalle de la inclusi�n sin eliminar el encabezado
	 * ya que no se puede perder el consecutivo asignado
	 * @param encabezadoInclusionPresupuesto Llave primaria del encabezado (Tabla inclu_presu_encabezado, Columna codigo_pk)
	 * @return true en caso de guardar correctamente, false de lo contrario
	 */
	public boolean eliminarDetalleInclusiones(long encabezadoInclusionPresupuesto);
	
}
