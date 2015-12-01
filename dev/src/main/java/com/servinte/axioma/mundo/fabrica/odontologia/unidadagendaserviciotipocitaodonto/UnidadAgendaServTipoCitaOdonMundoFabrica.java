
package com.servinte.axioma.mundo.fabrica.odontologia.unidadagendaserviciotipocitaodonto;

import com.servinte.axioma.mundo.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServCitaOdontoMundo;
import com.servinte.axioma.mundo.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadesConsultaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaMundo;
import com.servinte.axioma.orm.UnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * F&aacute;brica para objetos de la capa de negocio
 * relacionados con el anexo 1119 - Unidad de Agenda-Servicio X Tipo 
 * de Cita Odontol&oacute;gica
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public abstract class UnidadAgendaServTipoCitaOdonMundoFabrica {

	/**
	 * F&aacute;brica para el servicio de {@link UnidadesConsulta}
	 * @autor Jorge Armando Agudelo
	 * @return
	 */
	public static IUnidadesConsultaMundo crearUnidadesConsultaMundo()
	{
		return new UnidadesConsultaMundo();
	}
	
	
	/**
	 * F&aacute;brica para el servicio de {@link UnidadAgendaServCitaOdonto}
	 * @autor Jorge Armando Agudelo
	 * @return
	 */
	public static IUnidadAgendaServCitaOdontoMundo crearUnidadAgendaServCitaOdontoMundo()
	{
		return new UnidadAgendaServCitaOdontoMundo();
	}
}
