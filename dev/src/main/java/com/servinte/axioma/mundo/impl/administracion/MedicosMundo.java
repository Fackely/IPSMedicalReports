package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.dao.impl.administracion.MedicosHibernateDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IMedicosMundo;

public class MedicosMundo implements IMedicosMundo {
	
	private MedicosHibernateDAO dao = new MedicosHibernateDAO();
	
	@Override
	public ArrayList<DtoPersonas> obtenerTodosMedicosOdonto (int codigoInstitucion){
		
		return dao.obtenerTodosMedicosOdonto(codigoInstitucion);
	}

	@Override
	public ArrayList<DtoPersonas> obtenerMedicosOdontologos(int codigoInstitucion) {
		
		return dao.obtenerMedicosOdontologos(codigoInstitucion);
	}
	
	@Override
	public ArrayList<DtoPersonas> obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(
			int codigoInstitucion, int consecutivoCentroAtencion){
		return dao.obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(codigoInstitucion, consecutivoCentroAtencion);
	}
	
	/**
	 * Retorna las ocupaciones medicas de un profesional de la salud
	 * @param login Login del profesional de la salud
	 * @return {@link DTONivelAutorizacionOcupacionMedica}
	 */
	@Override
	public DTONivelAutorizacionOcupacionMedica obtenerOcupacionMedicaDeUsuarioProfesional(String login){
		return dao.obtenerOcupacionMedicaDeUsuarioProfesional(login);
	}

	/**
	 * Retorna especialidades del medico
	 * @param loginMedico
	 * @return ArrayList<String>
	 */
	public ArrayList<String> obtenerEspecialidadesMedico(String loginMedico){
		return dao.obtenerEspecialidadesMedico(loginMedico);
	}
}
