package com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosIngresoPacienteMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.IAgendaOdontologicaMundo;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con AgendaOdontologica
 * 
 * @author Cristhian Murillo
 * @see IAgendaOdontologicaMundo
 */
public class AgendaOdontologicaMundo implements IAgendaOdontologicaMundo {


	IConveniosIngresoPacienteMundo conveniosIngresoPacienteMundo;
	IConveniosMundo	conveniosMundo;
	
	
	public AgendaOdontologicaMundo() {
		inicializar();
	}

	private void inicializar() {
		conveniosIngresoPacienteMundo = FacturacionFabricaMundo.crearconConveniosIngresoPacienteMundo();
		conveniosMundo = FacturacionFabricaMundo.crearcConveniosMundo();
	}

	
	@Override
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado(int codPaciente, char acronimoEstadoActivo) 
	{
		return conveniosIngresoPacienteMundo.obtenerConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}

	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado(
			int codPaciente, char acronimoEstadoActivo) {
		return conveniosIngresoPacienteMundo.obtenerDtoConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}

	
	
	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(
			int codPaciente) {
		return conveniosIngresoPacienteMundo.obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(codPaciente);
	}

	
	@Override
	public Convenios findByIdConvenio(int id) {
		return conveniosMundo.findById(id);
	}


}
