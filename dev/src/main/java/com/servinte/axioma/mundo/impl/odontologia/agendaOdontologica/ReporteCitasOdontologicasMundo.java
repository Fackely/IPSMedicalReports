/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;
import com.servinte.axioma.mundo.interfaz.odontologian.IReporteCitasOdontologicasMundo;

/**
 * @author armando
 *
 */
public class ReporteCitasOdontologicasMundo implements
		IReporteCitasOdontologicasMundo {

	@Override
	public ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(
			DtoFiltroReporteCitasOdontologicas dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteCitasOdontologicasDao().consultarCitasOdontologiaDetallado(dto);
	}
}
