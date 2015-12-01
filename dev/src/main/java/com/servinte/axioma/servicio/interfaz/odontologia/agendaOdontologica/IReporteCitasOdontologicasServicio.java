/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica;

import java.util.ArrayList;

import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;

/**
 * @author armando
 *
 */
public interface IReporteCitasOdontologicasServicio {

	ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(
			DtoFiltroReporteCitasOdontologicas dto);

}
