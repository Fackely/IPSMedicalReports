/**
 * 
 */
package com.servinte.axioma.mundo.fabrica.odontologia;

import com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica.ReporteCitasOdontologicasMundo;
import com.servinte.axioma.mundo.interfaz.odontologian.IReporteCitasOdontologicasMundo;

/**
 * @author armando
 *
 */
public class OdontologiaMundoFabrica 
{

	private OdontologiaMundoFabrica()
	{
		
	}
	
	public static IReporteCitasOdontologicasMundo crearReporteCitasOdontologicasMundo()
	{
		return new ReporteCitasOdontologicasMundo();
	}
}
