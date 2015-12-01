/**
 * 
 */
package com.servinte.axioma.mundo.fabrica.odontologia.mantenimiento;

import com.servinte.axioma.mundo.impl.odontologia.facturacion.DescuentoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.facturacion.IDescuentoOdontologicoMundo;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
public class MantenimientoOdontologiaFabricaMundo
{

	/**
	 * Crea la instancia concreta de {@link IDescuentoOdontologicoMundo}
	 * @return implementación para IDescuentoOdontologicoMundo 
	 */
	public static IDescuentoOdontologicoMundo crearDescuentoOdontologicoMundo()
	{
		return new DescuentoOdontologicoMundo();
	}
	
}
