package com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.agendaOdontologica.AgendaOdontologicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosIngresoPacienteMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.IAgendaOdontologicaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo;
import com.servinte.axioma.orm.CitasAsociadasAProgramada;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IAgendaOdontologicaServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link IAgendaOdontologicaServicio}
 * 
 * @author Cristhian Murillo
 *
 */
public class AgendaOdontologicaServicio implements IAgendaOdontologicaServicio 
{


	private IAgendaOdontologicaMundo agendaOdontologicaMundo;
	private ICentroCostoMundo centroCostoMundo;
	private IConveniosIngresoPacienteMundo conveniosIngresoPacienteMundo;
	private ICitaOdontologicaMundo citaOdontologicaMundo;
		
	public AgendaOdontologicaServicio() {
		agendaOdontologicaMundo 		= AgendaOdontologicaFabricaMundo.crearAgendaOdontologicaMundo();
		centroCostoMundo 				= AdministracionFabricaMundo.crearCentroCostoMundo();
		conveniosIngresoPacienteMundo	= FacturacionFabricaMundo.crearconConveniosIngresoPacienteMundo();
		citaOdontologicaMundo			= AgendaOdontologicaFabricaMundo.crearCitaOdontologicaMundo();
	}
	

	@Override
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado(int codPaciente, char acronimoEstadoActivo) 
	{
		return agendaOdontologicaMundo.obtenerConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}


	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado(
			int codPaciente, char acronimoEstadoActivo) {
		return agendaOdontologicaMundo.obtenerDtoConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}


	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(
			int consecutivoCentroAttencion, int tipoArea) {
		return centroCostoMundo.obtenerCentrosCostoPorCentroAtencion(consecutivoCentroAttencion, tipoArea);
	}


	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(
			int codPaciente) {
		return agendaOdontologicaMundo.obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(codPaciente);
	}


	@Override
	public Convenios findByIdConvenio(int id) {
		return agendaOdontologicaMundo.findByIdConvenio(id);
	}


	@Override
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato(
			int codPaciente, int contrato) {
		return conveniosIngresoPacienteMundo.obtenerConvenioIngresoPacientePorContrato(codPaciente, contrato);
	}


	@Override
	public boolean guardarCitaAsociadasProgramada(CitasAsociadasAProgramada transientInstance) {
		return citaOdontologicaMundo.guardarCitaAsociadasProgramada(transientInstance);
	}

}
