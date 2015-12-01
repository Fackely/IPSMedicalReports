package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ReporteCitasOdontologicasDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseReporteCitasOdontologicasDao;
import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;

public class OracleReporteCitasOdontologicasDao implements
		ReporteCitasOdontologicasDao {

	@Override
	public ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(
			DtoFiltroReporteCitasOdontologicas dto) {
		return SqlBaseReporteCitasOdontologicasDao.consultarCitasOdontologiaDetallado(dto);
	}

}
