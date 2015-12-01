package com.princetonsa.dao.postgresql.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.ReportePagosCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseReportePagosCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;


public class PostgresqlReportePagosCarteraPacienteDao implements ReportePagosCarteraPacienteDao
{
	public ArrayList<DtoDatosFinanciacion> consultarDocumentos(int centroAtencion, String tipoDoc)
	{
		return SqlBaseReportePagosCarteraPacienteDao.consultarDocumentos(centroAtencion,tipoDoc);
	}
	
	public HashMap consultarAplicPagos(int codigoPk, String fechaIni, String fechaFin, int anioIni, int anioFin, String tipoPeriodo)
	{
		return SqlBaseReportePagosCarteraPacienteDao.consultarAplicPagos(codigoPk, fechaIni, fechaFin, anioIni, anioFin, tipoPeriodo);
	}
	
	public ArrayList<DtoDatosFinanciacion> ejecutarConsulta(String newquery)
	{
		return SqlBaseReportePagosCarteraPacienteDao.ejecutarConsulta(newquery);
	}
}