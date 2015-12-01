/**
 * 
 */
package com.servinte.axioma.dao.fabrica.odontologia;

import com.servinte.axioma.dao.impl.odontologia.mantenimiento.DescuentoOdontologicoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.mantenimiento.IDescuentoOdontologicoDAO;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
public class MantenimientoOdontologiaFabricaDao
{

	/**
	 * Crea la instancia concreta de {@link DescuentoOdontologicoDAO} 
	 */
	public static IDescuentoOdontologicoDAO crearDescuentoOdontologicoDAO()
	{
		return new DescuentoOdontologicoDAO();
	}

}
