/**
 * 
 */
package com.servinte.axioma.servicio.fabrica;

import com.servinte.axioma.servicio.impl.odontologia.MotivosCambioServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.IMotivosCambioServicio;

/**
 * Fabrica que construye objetos de servicios relacionado con MotivosCambioServicio
 * @see IMotivosCambioServicio
 * @author armando
 *
 */
public abstract class MotivosCambioServicioFabrica 
{

	private MotivosCambioServicioFabrica()
	{
		
	}
	
	public static IMotivosCambioServicio crearMotivosCambioServicio()
	{
		return new MotivosCambioServicio();
	}
}
