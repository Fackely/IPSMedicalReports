package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;

public interface ReporteCitasOdontologicasDao 
{

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public abstract ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(DtoFiltroReporteCitasOdontologicas dto);


}
