package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.mundo.impl.administracion.MedicosMundo;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;

public class MedicosServicio implements IMedicosServicio {
	
	private MedicosMundo mundo = new MedicosMundo();
	
	@Override
	public ArrayList<DtoPersonas> obtenerTodosMedicosOdonto (int codigoInstitucion){
		
		return mundo.obtenerTodosMedicosOdonto(codigoInstitucion);
	}

	@Override
	public ArrayList<DtoPersonas> obtenerMedicosOdontologos(
			int codigoInstitucion) {
		
		return mundo.obtenerMedicosOdontologos(codigoInstitucion);
	}
	
	@Override
	public ArrayList<DtoPersonas> obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(
			int codigoInstitucion, int consecutivoCentroAtencion){
		return mundo.obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(codigoInstitucion, consecutivoCentroAtencion);
	}

	/**
	 * Retorna la ocupación médicas de un profesional de la salud
	 * @param login Login del profesional de la salud
	 * @return {@link DTONivelAutorizacionOcupacionMedica}
	 */
	@Override
	public DTONivelAutorizacionOcupacionMedica obtenerOcupacionMedicaDeUsuarioProfesional(String login){
		return mundo.obtenerOcupacionMedicaDeUsuarioProfesional(login);
	}
}
