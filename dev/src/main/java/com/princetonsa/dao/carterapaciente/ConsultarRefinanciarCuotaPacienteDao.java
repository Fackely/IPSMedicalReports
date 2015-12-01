package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.actionform.carteraPaciente.ConsultarRefinanciarCuotaPacienteForm;
import com.princetonsa.dto.carteraPaciente.DtoHistoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;

public interface ConsultarRefinanciarCuotaPacienteDao {

	ArrayList<DtoIngresosFacturasAtenMedica> listarIngresosFacAtenMedica(int codigoPersona);

	HashMap guardarRefinanciacion(Connection con,HashMap datos);

	ArrayList<DtoHistoDatosFinanciacion> consultarHistoricoRefinanciacion(String codDatosFinanciacion);

	ArrayList<DtoIngresosFacturasAtenMedica> consultarDocsCarteraPacienteXRango(HashMap parametrosBusqueda);

}
