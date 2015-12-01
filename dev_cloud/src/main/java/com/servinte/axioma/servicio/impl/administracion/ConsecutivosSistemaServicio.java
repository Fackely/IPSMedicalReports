/**
 * 
 */
package com.servinte.axioma.servicio.impl.administracion;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IConsecutivosSistemaMundo;
import com.servinte.axioma.servicio.interfaz.administracion.IConsecutivosSistemaServicio;

/**
 * @author Juan David Ramírez
 * @since Jan 18, 2011
 */
public class ConsecutivosSistemaServicio implements
		IConsecutivosSistemaServicio
{

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.fabrica.administracion.IConsecutivosDisponiblesServicio#inicializarConsecutivosDisponibles()
	 */
	@Override
	public boolean inicializarConsecutivosDisponibles()
	{
		IConsecutivosSistemaMundo consecutivosSistemaMundo=AdministracionFabricaMundo.crearConsecutivosSistemaMundo();
		return consecutivosSistemaMundo.inicializarConsecutivosDisponibles();
	}

}
