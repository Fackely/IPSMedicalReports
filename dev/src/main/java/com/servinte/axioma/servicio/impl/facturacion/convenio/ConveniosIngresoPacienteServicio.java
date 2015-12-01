package com.servinte.axioma.servicio.impl.facturacion.convenio;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosIngresoPacienteMundo;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConveniosIngresoPacienteServicio;

/**
 * 
 * @author axioma
 *
 */
public class ConveniosIngresoPacienteServicio implements IConveniosIngresoPacienteServicio {
	
	
	 private IConveniosIngresoPacienteMundo convenioMundo;
	/**
	 * 
	 */
	public ConveniosIngresoPacienteServicio(){
		convenioMundo= FacturacionFabricaMundo.crearconConveniosIngresoPacienteMundo();
		
	}
	
	

	@Override
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato(
			int codPaciente, int contrato) {
		
		return convenioMundo.obtenerConvenioIngresoPacientePorContrato(codPaciente, contrato);
	}

	@Override
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado(
			int codPaciente, char acronimoEstadoActivo) {
		return convenioMundo.obtenerConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}

	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado(
			int codPaciente, char acronimoEstadoActivo) {
	
		return convenioMundo.obtenerDtoConveniosIngresoPacientePorEstado(codPaciente, acronimoEstadoActivo);
	}

	@Override
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(
			int codPaciente) {
		return convenioMundo.obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(codPaciente);
	}
	
	
	
	
	

}
