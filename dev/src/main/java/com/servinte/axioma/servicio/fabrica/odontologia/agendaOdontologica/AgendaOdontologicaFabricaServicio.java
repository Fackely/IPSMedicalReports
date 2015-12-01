package com.servinte.axioma.servicio.fabrica.odontologia.agendaOdontologica;

import com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica.AgendaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IAgendaOdontologicaServicio;

/**
 * F&aacute;brica para objetos de la capa de servicios
 * relacionados con las funcionalidades de 
 * Tesorer&iacute;a
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio
 */

public abstract class AgendaOdontologicaFabricaServicio {

	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IAgendaOdontologicaServicio}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IAgendaOdontologicaServicio}.
	 */
	public static IAgendaOdontologicaServicio crearAgendaOdontologicaServicio()
	{
		return new AgendaOdontologicaServicio();
	}
	
	
}
