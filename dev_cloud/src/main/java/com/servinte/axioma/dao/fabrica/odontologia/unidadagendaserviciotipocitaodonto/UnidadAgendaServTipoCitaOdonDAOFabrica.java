
package com.servinte.axioma.dao.fabrica.odontologia.unidadagendaserviciotipocitaodonto;

import com.servinte.axioma.dao.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServCitaOdontoHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadesConsultaHibernateDAO;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO;
import com.servinte.axioma.orm.UnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.UnidadesConsulta;


/**
 * F&aacute;brica para construir objetos DAO para la l&oacute;gica
 * relacionada con el anexo 1119 - Unidad de Agenda-Servicio X Tipo de Cita 
 * Odontol&oacute;gica
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public abstract class UnidadAgendaServTipoCitaOdonDAOFabrica{

	/**
	 * F&aacute;brica para el DAO de {@link UnidadesConsulta}
	 * @autor Jorge Armando Agudelo
	 * @return
	 */
	public static IUnidadesConsultaDAO crearUnidadesConsultaDAO()
	{
		return new UnidadesConsultaHibernateDAO();
	}
	
	
	/**
	 * F&aacute;brica para el DAO de {@link UnidadAgendaServCitaOdonto}
	 * @autor Jorge Armando Agudelo
	 * @return
	 */
	public static IUnidadAgendaServCitaOdontoDAO crearUnidadAgendaServCitaOdontoDAO()
	{
		return new UnidadAgendaServCitaOdontoHibernateDAO();
	}
	
}
