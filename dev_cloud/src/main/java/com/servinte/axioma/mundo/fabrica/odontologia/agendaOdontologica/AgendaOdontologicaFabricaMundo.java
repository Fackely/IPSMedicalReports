package com.servinte.axioma.mundo.fabrica.odontologia.agendaOdontologica;

import com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica.AgendaOdontologicaMundo;
import com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica.CitaOdontologicaMundo;
import com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica.InicioAtencionCitaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.IAgendaOdontologicaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaMundo;

/**
 * F&aacute;brica para contruir objetos para la Agenda Odontológica
 * @author Cristhian Murillo
 *
 */
public abstract class AgendaOdontologicaFabricaMundo 
{
	
	private AgendaOdontologicaFabricaMundo(){}
	

	/**
	 * @return AgendaOdontologicaMundo
	 */
	public static IAgendaOdontologicaMundo crearAgendaOdontologicaMundo()
	{
		return new AgendaOdontologicaMundo();
	}
	
	
	/**
	 * @return CitaOdontologicaMundo
	 */
	public static ICitaOdontologicaMundo crearCitaOdontologicaMundo()
	{
		return new CitaOdontologicaMundo();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la 
	 * entidad IInicioAtencionCitaMundo
	 * 
	 * @return objeto que es implementación de {@link IInicioAtencionCitaMundo}.
	 *
	 */
	public static IInicioAtencionCitaMundo crearInicioAtencionCitaMundo(){
		return new InicioAtencionCitaMundo();
	}

}
