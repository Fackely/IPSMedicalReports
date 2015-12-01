/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologian;

import java.util.ArrayList;

import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;

/**
 * @author armando
 *
 */
public interface IReporteCitasOdontologicasMundo {

	ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(
			DtoFiltroReporteCitasOdontologicas dto);


}
