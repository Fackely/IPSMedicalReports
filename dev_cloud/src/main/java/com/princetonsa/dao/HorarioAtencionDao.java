/*
* @(#)HorarioAtencionDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao;

import java.lang.String;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

/**
* Esta <i>interface</i> define el contrato de operaciones que debe
* implementar la clase que presta el servicio de acceso a datos para
* el objeto <code>HorarioAtencion</code>.
*
* @version 1.0, Sep 1, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public interface HorarioAtencionDao
{
	/**
	* Dada la identificaci�n de un horario de atenci�n, carga los datos correspondientes desde la
	* fuente de datos.
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	Identificador �nico del horario de atenci�n
	* @return <code>HashMap</code> con los datos pedidos y una conexi�n abierta con la fuente de
	* datos
	*/
	public HashMap  cargarHorarioAtencion(Connection ac_con, int ai_codigo)throws SQLException;

	/**
	* Dada la identificaci�n de un horario de atenci�n, elimina el horario de atenci�n de la fuente
	* de datos.
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	Identificador �nico del horario de atenci�n
	* @return N�mero de horarios de atenci�n eliminados
	*/
	public int eliminarHorarioAtencion(Connection ac_con, int ai_codigo)throws SQLException;

	/**
	* Dado un conjunto de identificaciones de horarios de atenci�n, elimina los horario de atenci�n
	* de la fuente de datos.
	* @param ac_con			Conexi�n abierta con una fuente de datos
	* @param aia_codigos	Conjunto de identificadores �nicos de los horario de atenci�n
	* @return N�mero de horarios de atenci�n eliminados
	*/
	public int eliminarHorarioAtencion(Connection ac_con, int[] ai_codigo)throws SQLException;

	/**
	* Inserta un horario de atenci�n en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_unidadConsulta		Unidad de consulta a la que ser� asignado el horario de atenci�n
	* @param ai_consultorio			Consultorio al cual ser� asignado el horario de atenci�n
	* @param ai_diaSemana			D�a de la semana a la cual ser� asignado el horario de atenci�n
	* @param ai_codigoMedico		C�digo del m�dico al cual ser� asignado el horario de atenci�n
	* @param as_horaInicio			Hora de inicio del horario de atenci�n para el d�a de la semana
	*								especificado
	* @param as_horaFin				Hora de finalizaci�n del horario de atenci�n para el d�a de la
	*								semana especificado
	* @param ai_duracionConsulta	Tiempo de duraci�n (en minutos) de una consulta en este horario
	*								de atenci�n
	* @param ai_pacientesSesion		M�ximo n�mero de pacientes que se pueden atender por sesi�n de
	*								consulta
	* @return C�digo asignado al horario de atenci�n
	*/
	public int insertarHorarioAtencion(
		Connection	ac_con,
		int 		ai_centroAtencion,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_duracionConsulta,
		int			ai_pacientesSesion
	)throws SQLException;

	/**
	* Lista los c�digo de los horario de atenci�n
	* @param ac_con Conexi�n abierta con una fuente de datos
	* @param centroAtencion
	 * @param institucion 
	* @return <code>Collection</code> con los c�digos de los horarios de atenci�n
	*/
	public Collection listarCodigosHorarioAtencion(Connection ac_con, int centroAtencion, String unidadesAgenda, int institucion)throws SQLException;

	/**
	* Modifica un horario de atenci�n en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_codigo				Identificador �nico del horario de atenci�n
	* @param ai_unidadConsulta		Unidad de consulta a la que ser� asignado el horario de atenci�n
	* @param ai_consultorio			Consultorio al cual ser� asignado el horario de atenci�n
	* @param ai_diaSemana			D�a de la semana a la cual ser� asignado el horario de atenci�n
	* @param ai_codigoMedico		C�digo del m�dico al cual ser� asignado el horario de atenci�n
	* @param as_horaInicio			Hora de inicio del horario de atenci�n para el d�a de la semana
	*								especificado
	* @param as_horaFin				Hora de finalizaci�n del horario de atenci�n para el d�a de la
	*								semana especificado
	* @param ai_duracionConsulta	Tiempo de duraci�n (en minutos) de una consulta en este horario
	*								de atenci�n
	* @param ai_pacientesSesion		M�ximo n�mero de pacientes que se pueden atender por sesi�n de
	*								consulta
	* @return n�mero de horarios de atenci�n modificados
	*/
	public int modificarHorarioAtencion(
		Connection	ac_con,
		int			ai_centroAtencion,
		int			ai_codigo,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_duracionConsulta,
		int			ai_pacientesSesion
	)throws SQLException;
	
	/**
	* consultar la Especialidad Unidad de Agenda
	* @param con Conexi�n abierta con una fuente de datos
	* @param HashMap parametros
	* @return HashMap<String,Object>
	*/
	public HashMap<String,Object> getEspecialidad(Connection con, HashMap parametros);

	public ArrayList<DtoHorarioAtencion> consultarHA(HashMap<String, Object> parametros);
	
	public ArrayList<HashMap<String, Object>> consultarHAAvanzada(Connection con, HashMap<String, Object> parametros);
	
}