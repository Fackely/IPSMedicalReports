package com.servinte.axioma.dao.impl.facturacion.convenio;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConveniosIngresoPacienteDAO;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConveniosIngresoPacienteDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad ConveniosIngresoPaciente
 * @authorCristhian Murillo
 */
public class ConveniosIngresoPacienteHibernateDAO implements IConveniosIngresoPacienteDAO {
	
	
	ConveniosIngresoPacienteDelegate delegate;

	public ConveniosIngresoPacienteHibernateDAO(){
		
		 delegate = new ConveniosIngresoPacienteDelegate();
	}	
	
	@Override
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado(int codPaciente, char acronimoEstadoActivo) 
	{
		return delegate.obtenerConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}

	
	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado(
			int codPaciente, char acronimoEstadoActivo) {
		return delegate.obtenerDtoConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}

	
	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(
			int codPaciente) {
		return delegate.obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(codPaciente);
	}

	
	@Override
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato(
			int codPaciente, int contrato) {
		return delegate.obtenerConvenioIngresoPacientePorContrato(codPaciente, contrato);
	}
	

}
