/**
 * 
 */
package com.servinte.axioma.mundo.impl.administracion;

import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IConsecutivosSistemaDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IConsecutivosSistemaMundo;

/**
 * @author Juan David Ramírez
 * @since Jan 18, 2011
 */
public class ConsecutivosSistemaMundo implements IConsecutivosSistemaMundo
{

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.impl.administracion.IConsecutivosSistemaMundo#inicializarConsecutivosDisponibles()
	 */
	@Override
	public boolean inicializarConsecutivosDisponibles()
	{
		IConsecutivosSistemaDAO consecutivosSistemaDAO=AdministracionFabricaDAO.crearConsecutivosSistemaDAO();
		return consecutivosSistemaDAO.consultarConsecutivosDisponiblesInestables();
	}
	
}
