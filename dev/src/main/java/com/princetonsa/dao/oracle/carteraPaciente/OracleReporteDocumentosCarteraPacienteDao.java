package com.princetonsa.dao.oracle.carteraPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.carterapaciente.ReporteDocumentosCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseReporteDocumentosCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoReporteDocumentosCarteraPaciente;

public class OracleReporteDocumentosCarteraPacienteDao implements ReporteDocumentosCarteraPacienteDao
{
	public ArrayList<DtoReporteDocumentosCarteraPaciente> consultarDocumentos(DtoDocumentosGarantia dto)
	{
		return SqlBaseReporteDocumentosCarteraPacienteDao.consultarDocumentos(dto);
	}
}