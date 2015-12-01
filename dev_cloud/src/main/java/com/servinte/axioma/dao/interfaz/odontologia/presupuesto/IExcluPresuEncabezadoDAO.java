
package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import java.util.List;

import com.servinte.axioma.orm.ExcluPresuEncabezado;

/**
 * 
 * Interface que define las operaciones
 * para gestionar lo relacionado con el objeto
 * 
 * {@link ExcluPresuEncabezado}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IExcluPresuEncabezadoDAO {

	
	/**
	 * M�todo que permite realizar un registro de la entidad 
	 * 
	 * Con la informaci�n del proceso de aprobaci�n de exclusiones de programas
	 * al plan de tratamiento y al presupuesto del paciente.
	 * 
	 * @param excluPresuEncabezado
	 * @return
	 */
	public long registrarExclusionPresupuestoEncabezado (ExcluPresuEncabezado excluPresuEncabezado);
	
	
	/**
	 * M�todo que consulta las exclusiones asociadas al presupuesto activo del paciente
	 * 
	 * @param codigoPresupuesto
	 */
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion(long codigoPresupuesto);
}
