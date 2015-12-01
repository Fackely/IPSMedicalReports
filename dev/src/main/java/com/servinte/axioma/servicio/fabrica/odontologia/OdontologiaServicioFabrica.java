/**
 * 
 */
package com.servinte.axioma.servicio.fabrica.odontologia;

import com.servinte.axioma.servicio.impl.odontologia.ProximaCitaProgramadaServicio;
import com.servinte.axioma.servicio.impl.odontologia.ReporteCitasOdontologicasServicio;
import com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica.CitaOdontologicaServicio;
import com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica.InicioAtencionCitaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.IProximaCitaProgramadaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IReporteCitasOdontologicasServicio;

/**
 * @author armando
 *
 */
public class OdontologiaServicioFabrica 
{

	private OdontologiaServicioFabrica()
	{
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static IReporteCitasOdontologicasServicio crearReporteCitasOdontologicas()
	{
		return new ReporteCitasOdontologicasServicio();
	}
	
	/**
	 * Este M&eacute;todo se encarga de crear una instancia de la 
	 * entidad ICitaOdontologicaServicio
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICitaOdontologicaServicio}.
	 *
	 */
	public static ICitaOdontologicaServicio crearCitaOdontologicaServicio(){
		return new CitaOdontologicaServicio();
	}
	
	/**
	 * Este Método se encarga de crear una instancia de la 
	 * entidad IInicioAtencionCitaServicio
	 * 
	 * @return objeto que es implementación de {@link IInicioAtencionCitaServicio}.
	 *
	 */
	public static IInicioAtencionCitaServicio crearInicioAtencionCitaServicio(){
		return new InicioAtencionCitaServicio();
	}

	/**
	 * Este Método se encarga de crear una instancia de la 
	 * entidad IProximaCitaProgramadaServicio
	 * 
	 * @return objeto que es implementación de {@link IProximaCitaProgramadaServicio}.
	 *
	 */
	public static IProximaCitaProgramadaServicio crearProximaCitaProgramadaServicio()
	{
		return new ProximaCitaProgramadaServicio();
	}
	
	
}
