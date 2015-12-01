package com.servinte.axioma.dao.fabrica.odontologia.agendaOdontologica;

import com.servinte.axioma.dao.impl.odontologia.agendaOdontologica.CitaOdontologicaHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.agendaOdontologica.InicioAtencionCitaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaDAO;

/**
 * F�brica para objetos de la capa de servicios
 * relacionados con las funcionalidades de 
 * agenda odontologica
 * 
 * @author Fabian Becerra
 * 
 */
public abstract class AgendaOdontologicaFabricaDAO {

	/**
	 * Crea y retorna un objeto que es implementaci�n de
	 * {@link ICitaOdontologicaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICitaOdontologicaDAO}.
	 */
	public static final ICitaOdontologicaDAO crearAgendaOdontologicaDAO()
	{
		return new CitaOdontologicaHibernateDAO();
	}
	
	/**
	 * Este M�todo se encarga de crear una instancia de la 
	 * entidad IInicioAtencionCitaDAO
	 * 
	 * @return objeto que es implementaci�n de {@link IInicioAtencionCitaDAO}.
	 *
	 */
	public static IInicioAtencionCitaDAO crearInicioAtencionCitaDAO(){
		return new InicioAtencionCitaDAO();
	}
}
