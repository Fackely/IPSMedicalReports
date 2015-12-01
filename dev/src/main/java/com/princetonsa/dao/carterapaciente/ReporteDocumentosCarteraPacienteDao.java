package com.princetonsa.dao.carterapaciente;

import java.util.ArrayList;

import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoReporteDocumentosCarteraPaciente;

public interface ReporteDocumentosCarteraPacienteDao
{
	public ArrayList<DtoReporteDocumentosCarteraPaciente> consultarDocumentos(DtoDocumentosGarantia dto);
}
