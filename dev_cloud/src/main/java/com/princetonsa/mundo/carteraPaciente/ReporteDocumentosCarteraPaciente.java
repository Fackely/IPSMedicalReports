package com.princetonsa.mundo.carteraPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.ReporteDocumentosCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;
import com.princetonsa.dto.carteraPaciente.DtoReporteDocumentosCarteraPaciente;

public class ReporteDocumentosCarteraPaciente
{
	private static ReporteDocumentosCarteraPacienteDao getReporteDocumentosCarteraPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteDocumentosCarteraPacienteDao();
	}
	
	public static ArrayList<DtoReporteDocumentosCarteraPaciente> consultarDocumentos(DtoDocumentosGarantia dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteDocumentosCarteraPacienteDao().consultarDocumentos(dto);
	}
}