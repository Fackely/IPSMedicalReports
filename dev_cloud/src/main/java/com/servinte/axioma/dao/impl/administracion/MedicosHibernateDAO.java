package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.dao.interfaz.administracion.IMedicosDAO;
import com.servinte.axioma.orm.delegate.administracion.MedicosDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IMedicosDAO}.
 *
 * @author Yennifer Guerrero
 * @since  07/09/2010
 *
 */
public class MedicosHibernateDAO implements IMedicosDAO {
	
	private MedicosDelegate delegate = new MedicosDelegate();
	
	@Override
	public ArrayList<DtoPersonas> obtenerTodosMedicosOdonto (int codigoInstitucion){
		
		return delegate.obtenerTodosMedicosOdonto(codigoInstitucion);
	}

	@Override
	public ArrayList<DtoPersonas> obtenerMedicosOdontologos(
			int codigoInstitucion) {
		
		return delegate.obtenerMedicosOdontologos(codigoInstitucion);
	} 
	
	@Override
	public ArrayList<DtoPersonas> obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(
			int codigoInstitucion, int consecutivoCentroAtencion){
		return delegate.obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(codigoInstitucion, consecutivoCentroAtencion);
	}

	/**
	 * Retorna la ocupación médica de un profesional de la salud
	 * @param login Login del profesional de la salud
	 * @return {@link DTONivelAutorizacionOcupacionMedica}
	 */
	@Override
	public DTONivelAutorizacionOcupacionMedica obtenerOcupacionMedicaDeUsuarioProfesional(String login){
		return delegate.obtenerOcupacionMedicaDeUsuarioProfesional(login);
	}

	/**
	 * Retorna especialidades del medico
	 * @param loginMedico
	 * @return ArrayList<String>
	 */
	public ArrayList<String> obtenerEspecialidadesMedico(String loginMedico){
		return delegate.obtenerEspecialidadesMedico(loginMedico);
	}
}
