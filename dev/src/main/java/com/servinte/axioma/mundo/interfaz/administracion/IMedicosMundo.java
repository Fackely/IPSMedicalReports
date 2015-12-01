package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;

public interface IMedicosMundo {

	/**
	 * Este m&eacute;todo se encarga de obtener los profesionales
	 * de la salud activos e inactivos en el sistema con ocupaciones 
	 * de odont&oacute;logo y auxiliar de odontolog&iacute;.
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoPersonas> obtenerTodosMedicosOdonto(int codigoInstitucion);
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener los profesionales
	 * de la salud activos e inactivos en el sistema con ocupaciones 
	 * de odont&oacute;logo.
	 * @return
	 * @author Carolina G&oacute;mez
	 */
	public ArrayList<DtoPersonas> obtenerMedicosOdontologos (int codigoInstitucion);


	/**
	 * Retorna Los profesionales de la salud con permisos para centros de costo dependiendo del centro
	 * de atención enviado como parámetro
	 * @return ArrayList<DtoPersonas>
	 * @author Fabian Becerra
	 */
	ArrayList<DtoPersonas> obtenerProfesionalesConPermisosCentroCostoPorCentroAtencion(
			int codigoInstitucion, int consecutivoCentroAtencion);
	
	/**
	 * Retorna la ocupación médica de un profesional de la salud
	 * @param login Login del profesional de la salud
	 * @return {@link DTONivelAutorizacionOcupacionMedica}
	 */
	public DTONivelAutorizacionOcupacionMedica obtenerOcupacionMedicaDeUsuarioProfesional(String login);
	

	/**
	 * Retorna especialidades del medico
	 * @param loginMedico
	 * @return ArrayList<String>
	 */
	public ArrayList<String> obtenerEspecialidadesMedico(String loginMedico);
}
