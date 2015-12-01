package com.servinte.axioma.mundo.interfaz.tesoreria;

import com.servinte.axioma.orm.DetalleNotaPaciente;

/**
 * Define la logica de negocio relacionada con 
 * Detalle Notas de Paciente
 * @author diecorqu
 * 
 */
public interface IDetalleNotaPacienteMundo {

	/**
	 * Método encargado de persistir la entidad DetalleNotaPaciente
	 * @param DetalleNotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarDetalleNotaPaciente(DetalleNotaPaciente detalleNotaPaciente);
	
	/**
	 * Método encargado de eliminar un registro de DetalleNotaPaciente
	 * @param DetalleNotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean eliminarDetalleNotaPaciente(DetalleNotaPaciente detalleNotaPaciente);
	
	/**
	 * Método encargado de modificar la entidad DetalleNotaPaciente
	 * @param DetalleNotaPaciente
	 * @return DetalleNotaPaciente
	 */
	public DetalleNotaPaciente modificarDetalleNotaPaciente(DetalleNotaPaciente detalleNotaPaciente);
	
	/**
	 * Método encargado de buscar una DetalleNotaPaciente por codigo
	 * @param codigo
	 * @return DetalleNotaPaciente
	 */
	public DetalleNotaPaciente findById(long codigo);
}
