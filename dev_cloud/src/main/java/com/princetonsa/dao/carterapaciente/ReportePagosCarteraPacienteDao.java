package com.princetonsa.dao.carterapaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;


public interface ReportePagosCarteraPacienteDao
{

	ArrayList<DtoDatosFinanciacion> consultarDocumentos(int centroAtencion,String tipoDoc);

	HashMap consultarAplicPagos(int codigoPk, String fechaIni, String fechaFin, int anioIni, int anioFin, String tipoPeriodo);

	ArrayList<DtoDatosFinanciacion> ejecutarConsulta(String newquery);
}