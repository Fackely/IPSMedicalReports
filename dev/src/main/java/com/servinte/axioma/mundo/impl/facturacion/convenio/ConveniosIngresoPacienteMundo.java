package com.servinte.axioma.mundo.impl.facturacion.convenio;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConveniosIngresoPacienteDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosIngresoPacienteMundo;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;

/**
 * @author Cristhian Murillo
 */
public class ConveniosIngresoPacienteMundo implements IConveniosIngresoPacienteMundo {

	
	private IConveniosIngresoPacienteDAO conveniosIngresoPacienteDAO;
	
	
	public ConveniosIngresoPacienteMundo(){
		conveniosIngresoPacienteDAO = FacturacionFabricaDAO.crearConveniosIngresoPacienteDAO();
	}
	
	
	@Override
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado(int codPaciente, char acronimoEstadoActivo) 
	{
		return conveniosIngresoPacienteDAO.obtenerConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}


	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado(
			int codPaciente, char acronimoEstadoActivo) {
		return conveniosIngresoPacienteDAO.obtenerDtoConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}


	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(
			int codPaciente) {
		return conveniosIngresoPacienteDAO.obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(codPaciente);
	}


	@Override
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato(
			int codPaciente, int contrato) {
		return conveniosIngresoPacienteDAO.obtenerConvenioIngresoPacientePorContrato(codPaciente, contrato);
	}

	
}
