/**
 * 
 */
package com.servinte.axioma.servicio.impl.odontologia;

import java.util.ArrayList;

import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;
import com.servinte.axioma.mundo.fabrica.odontologia.OdontologiaMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologian.IReporteCitasOdontologicasMundo;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IReporteCitasOdontologicasServicio;

/**
 * @author armando
 *
 */
public class ReporteCitasOdontologicasServicio implements
		IReporteCitasOdontologicasServicio {
	
	IReporteCitasOdontologicasMundo mundo;
	
	public ReporteCitasOdontologicasServicio()
	{
		mundo=OdontologiaMundoFabrica.crearReporteCitasOdontologicasMundo();
	}

	@Override
	public ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(
			DtoFiltroReporteCitasOdontologicas dto) {
		return mundo.consultarCitasOdontologiaDetallado(dto);
	}


}
