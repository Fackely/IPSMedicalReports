package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;


import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IPacienteServicio;


/**
 * 
 * @author axioma
 *
 */
public class PacienteServicio implements IPacienteServicio {
	
	
	private IPacientesMundo pacienteMundo;
	
	
	/**
	 * 
	 */
	public  PacienteServicio(){
		 pacienteMundo=AdministracionFabricaMundo.crearPacienteMundo();
	}
	
	

	@Override
	public DtoPersonas obtenerDatosPaciente(int codPaciente) {
		return pacienteMundo.obtenerDatosPaciente(codPaciente);
	}

	@Override
	public DtoPersonas obtenerPaciente(String identificacionBuscar,
			String acronimoTipoIdentificacion) {
		return  pacienteMundo.obtenerPaciente(identificacionBuscar, acronimoTipoIdentificacion);
	}

	@Override
	public boolean tienePacientePresupuestoEnEstados(int idPaciente,
			String[] listaEstadosPresupuesto) {
		return pacienteMundo.tienePacientePresupuestoEnEstados(idPaciente, listaEstadosPresupuesto);
	}



	@Override
	public DtoPaciente cargarPacienteCompleto(String identificacionBuscar,
			String acronimoTipoIdentificacion) {
		return pacienteMundo.cargarPacienteCompleto(identificacionBuscar, acronimoTipoIdentificacion);
	}



	@Override
	public Pacientes findById(int id) {
		return pacienteMundo.findById(id);
	}



	@Override
	public ArrayList<DtoUsuariosCapitados> buscarPacienteConvenio(DtoUsuariosCapitados parametrosBusqueda) {
		return pacienteMundo.buscarPacienteConvenio(parametrosBusqueda);
	}


	@Override
	public void attachDirty(Pacientes pacienteOrm, int institucion) {
		pacienteMundo.attachDirty(pacienteOrm, institucion);
	}



	@Override
	public DTOPacienteCapitado buscarPacienteAutorizacionIngresoEstancia(
			long codigoIngresoEstancia) {
		return pacienteMundo.buscarPacienteAutorizacionIngresoEstancia(codigoIngresoEstancia);
	}

	
	
}
