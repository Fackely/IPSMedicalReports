package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ReporteCitasOdontologicasDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseReporteCitasOdontologicasDao;
import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;

public class PostgresqlReporteCitasOdontologicasDao implements
		ReporteCitasOdontologicasDao {

	@Override
	public ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(
			DtoFiltroReporteCitasOdontologicas dto) {
		return SqlBaseReporteCitasOdontologicasDao.consultarCitasOdontologiaDetallado(dto);
	}
}
