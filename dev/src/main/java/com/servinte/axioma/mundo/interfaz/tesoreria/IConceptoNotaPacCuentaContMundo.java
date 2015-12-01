package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.orm.ConcNotaPacCuentaCont;

/**
 * Define la logica de negocio relacionada con 
 * Conceptos Notas de Paciente Cuenta Contable
 * @author diecorqu
 * 
 */
public interface IConceptoNotaPacCuentaContMundo {
	
	/**
	 * Método encargado de persistir la entidad ConcNotaPacCuentaCont
	 * @param conceptoNotaPacCuentaCont
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarConceptoNotaPacCuentaCont(ConcNotaPacCuentaCont conceptoNotaPacCuentaCont);

	/**
	 * Método encargado de eliminar un registro de ConcNotaPacCuentaCont
	 * @param conceptoNotaPacCuentaCont
	 */
	public void eliminarConceptoNotaPacCuentaCont(ConcNotaPacCuentaCont conceptoNotaPacCuentaCont);

	/**
	 * Método encargado de modificar la entidad ConcNotaPacCuentaCont
	 * @param conceptoNotaPaciente
	 * @return ConcNotaPacCuentaCont
	 */
	public ConcNotaPacCuentaCont modificarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont);

	/**
	 * Método encargado de buscar un ConcNotaPacCuentaCont por codigo
	 * @param codigo
	 * @return ConcNotaPacCuentaCont
	 */
	public ConcNotaPacCuentaCont findById(long codigo);

	/**
	 * Método encargado de retornar una lista de ConcNotaPacCuentaCont con los 
	 * Cuentas Conbles Asociadas a un Concepto de Notas de Paciente
	 * @return
	 */
	public ArrayList<DtoConcNotaPacCuentaCont> listarConceptoNotaPacCuentaCont(long codigoConceptoNotas);
}
