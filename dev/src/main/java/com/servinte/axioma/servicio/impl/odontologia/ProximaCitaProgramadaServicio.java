/**
 * 
 */
package com.servinte.axioma.servicio.impl.odontologia;

import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.IProximaCitaProgramadaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;

/**
 * @author Juan David Ramírez
 * @since 27 Diciembre 2010
 */
public class ProximaCitaProgramadaServicio implements IProximaCitaProgramadaServicio
{

	@Override
	public void eliminarRegistroProximaCita(int codigoProximaCitaRegistrada)
	{
		ICitaOdontologicaServicio citaOdonServicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
		
		UtilidadTransaccion.getTransaccion().begin();

		citaOdonServicio.eliminarCitaProgramada(codigoProximaCitaRegistrada);
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
}
