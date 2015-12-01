package com.princetonsa.dao.oracle.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.ConsultarRefinanciarCuotaPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseConsultarRefinanciarCuotaPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoHistoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;

public class OracleConsultarRefinanciarCuotaPacienteDao implements
ConsultarRefinanciarCuotaPacienteDao {

	@Override
	public ArrayList<DtoIngresosFacturasAtenMedica> listarIngresosFacAtenMedica(int codigoPersona) {
		
		return SqlBaseConsultarRefinanciarCuotaPacienteDao.listarIngresosFacAtenMedica(codigoPersona);
	}

	@Override
	public HashMap guardarRefinanciacion(Connection con, HashMap datos) {
		
		return SqlBaseConsultarRefinanciarCuotaPacienteDao.guardarDatosRefinanciacion(con, datos);
	}

	@Override
	public ArrayList<DtoHistoDatosFinanciacion> consultarHistoricoRefinanciacion(String codDatosFinanciacion) {
		
		return SqlBaseConsultarRefinanciarCuotaPacienteDao.consultarHistoricoRefinanciacion(codDatosFinanciacion);
	}

	@Override
	public ArrayList<DtoIngresosFacturasAtenMedica> consultarDocsCarteraPacienteXRango(HashMap parametrosBusqueda) {
		
		return SqlBaseConsultarRefinanciarCuotaPacienteDao.consultarDocsCarteraPacienteXRango(parametrosBusqueda);
	}

}
