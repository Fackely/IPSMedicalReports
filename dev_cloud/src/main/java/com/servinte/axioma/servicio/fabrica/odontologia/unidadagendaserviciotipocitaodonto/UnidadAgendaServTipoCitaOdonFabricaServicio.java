
package com.servinte.axioma.servicio.fabrica.odontologia.unidadagendaserviciotipocitaodonto;

import com.servinte.axioma.servicio.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdontoServicio;
import com.servinte.axioma.servicio.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadesConsultaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadAgendaServTipoCitaOdontoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadesConsultaServicio;

/**
 * F&aacute;brica para objetos de la capa de servicios
 * relacionados con la parametrizaci&oacute;n de Unidad de Agenda-Servicio por
 * Tipo de Cita Odontol&oacute;gica
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public abstract class UnidadAgendaServTipoCitaOdonFabricaServicio {

	
	private UnidadAgendaServTipoCitaOdonFabricaServicio(){}
	
	/**
	 * F&aacute;brica para el servicio de todo lo relacionado con el Anexo
	 * 1119 - Unidad de Agenda-Servicio X Tipo de Cita Odontol&oacute;gica 
	 * 
	 * @autor Jorge Armando Agudelo
	 * @return
	 */
	public static IUnidadAgendaServTipoCitaOdontoServicio crearUnidadAgendaServCitaOdontoServicio()
	{
		return new UnidadAgendaServTipoCitaOdontoServicio();
	}
	
	
	/**
	 * Crear una instancia de la IUnidadesConsultaServicio
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IUnidadesConsultaServicio crearUnidadConsultaServicio(){
		
		return new UnidadesConsultaServicio();
		
	}
}
