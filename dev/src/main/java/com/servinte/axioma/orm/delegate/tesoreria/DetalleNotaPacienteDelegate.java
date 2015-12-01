package com.servinte.axioma.orm.delegate.tesoreria;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.DetalleNotaPaciente;
import com.servinte.axioma.orm.DetalleNotaPacienteHome;


/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link DetalleNotaPaciente} de la relaci�n detalle_nota_paciente
 * @author diecorqu
 *  
 */
public class DetalleNotaPacienteDelegate extends DetalleNotaPacienteHome {

	/**
	 * M�todo encargado de persistir la entidad DetalleNotaPaciente
	 * @param DetalleNotaPaciente
	 * @return boolean - resultado de la operaci�n
	 */
	public boolean guardarDetalleNotaPaciente(DetalleNotaPaciente detalleNotaPaciente) {
		boolean save = false;
		try{
			super.persist(detalleNotaPaciente);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el detalle de la nota paciente con c�digo: " + 
					detalleNotaPaciente.getCodigoPk(),e);
		}		
		return save;
	}

	/**
	 * M�todo encargado de eliminar un registro de DetalleNotaPaciente
	 * @param DetalleNotaPaciente
	 * @return boolean - resultado de la operaci�n
	 */
	public boolean eliminarDetalleNotaPaciente(DetalleNotaPaciente detalleNotaPaciente) {
		boolean save = false;
		try{
			super.delete(detalleNotaPaciente);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el detalle de la nota paciente con c�digo: " + 
					detalleNotaPaciente.getCodigoPk(),e);
		}		
		return save;
	}

	/**
	 * M�todo encargado de modificar la entidad DetalleNotaPaciente
	 * @param DetalleNotaPaciente
	 * @return DetalleNotaPaciente
	 */
	public DetalleNotaPaciente modificarDetalleNotaPaciente(DetalleNotaPaciente detalleNotaPaciente) {
		DetalleNotaPaciente nota = null;
		try{
			nota = super.merge(detalleNotaPaciente);
		}catch (Exception e) {
			Log4JManager.error("No se pudo guardar el detalle de la nota paciente con c�digo: " + 
					detalleNotaPaciente.getCodigoPk(),e);
		}		
		return nota;
	}

	/**
	 * M�todo encargado de buscar una DetalleNotaPaciente por codigo
	 * @param codigo
	 * @return DetalleNotaPaciente
	 */
	public DetalleNotaPaciente findById(long codigo) {
		return super.findById(codigo);
	}

}
