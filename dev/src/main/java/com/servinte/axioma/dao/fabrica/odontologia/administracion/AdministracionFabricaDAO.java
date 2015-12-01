/**
 * 
 */
package com.servinte.axioma.dao.fabrica.odontologia.administracion;

import com.servinte.axioma.dao.impl.odontologia.administracion.EmisionBonosDAO;
import com.servinte.axioma.dao.impl.odontologia.administracion.EmisionTarjetaClienteDAO;
import com.servinte.axioma.dao.interfaz.odontologia.administracion.IEmisionBonosDAO;
import com.servinte.axioma.dao.interfaz.odontologia.administracion.IEmisionTarjetaClienteDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.administracion.IEmisionBonosMundo;

/**
 * Fábrica DAO para el submódulo Administración del módulo Odontología
 * @author Juan David Ramírez
 * @since 08 Septiembre 2010
 * @version 1.0
 */
public class AdministracionFabricaDAO
{
	public static final IEmisionTarjetaClienteDAO crearEmisionTarjetaClienteDAO()
	{
		return new EmisionTarjetaClienteDAO();
	}

	/**
	 * Cerea una instancia concreta para la interfaz {@link IEmisionBonosMundo}
	 * @return {@link IEmisionBonosMundo} Instancia concreta
	 */
	public static IEmisionBonosDAO crearEmisionBonosDAO()
	{
		return new EmisionBonosDAO();
	}
}
