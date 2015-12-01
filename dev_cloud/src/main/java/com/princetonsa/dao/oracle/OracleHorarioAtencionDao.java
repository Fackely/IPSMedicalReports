/*
* @(#)OracleHorarioAtencion.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.oracle;


import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HorarioAtencionDao;
import com.princetonsa.dao.sqlbase.SqlBaseHorarioAtencionDao;

import util.ConstantesBD;
import util.UtilidadBD;

/**
* Esta clase implementa el contrato estipulado en <code>HorarioAtencionDao</code>, y presta los
* servicios de acceso a una base de datos Oracle requeridos por la clase
* <code>HorarioAtencion</code>.
*
* @version 2.0, Enero 27 de 2004
* @author <a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
*/
public class OracleHorarioAtencionDao implements HorarioAtencionDao
{
	/**
	* Cadena constante con el <i>Statement</i> necesario para cargar un horario de atenci�n en una
	* base de datos Oracle
	*/
	private static final String is_cargar =
		"SELECT "	+	"DISTINCT (ha.codigo) "	+ "AS codigo," +
						"ha.centro_atencion "   + "AS centroAtencion,"			+
						"ha.unidad_consulta "	+ "AS codigoUnidadConsulta,"	+
						"CASE WHEN ha.consultorio IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE ha.consultorio END AS codigoConsultorio,"+
						"CASE WHEN ha.dia IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ElSE ha.dia END AS codigoDiaSemana,"			+						
						"CASE WHEN ha.codigo_medico IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE ha.codigo_medico END AS codigoMedico,"+
						"ha.hora_inicio "		+ "AS horaInicio,"				+
						"ha.hora_fin "			+ "AS horaFin,"					+
						"ha.tiempo_sesion "		+ "AS tiempoSesion,"			+
						"ha.pacientes_sesion "	+ "AS pacientesSesion,"			+
						"uc.descripcion "		+ "AS nombreUnidadConsulta,"	+
						"c.descripcion "		+ "AS nombreConsultorio,"		+
						"d.dia   "				+ "AS nombreDiaSemana,"			+
						"p.primer_nombre "		+ "AS primerNombreMedico,"		+
						"p.segundo_nombre "		+ "AS segundoNombreMedico,"		+
						"p.primer_apellido "	+ "AS primerApellidoMedico,"	+
						"p.segundo_apellido "	+ "AS segundoApellidoMedico," +
						"c.centro_atencion AS codigocentroatencion," +
						"getnomcentroatencion(c.centro_atencion) AS nombreCentroAtencion "	+
						"FROM "		+	"horario_atencion "		+ "ha "							+
						"LEFT OUTER JOIN unidades_consulta "	+ "uc "	+ "ON(uc.codigo"	+ "=ha.unidad_consulta)"	+
						"LEFT OUTER JOIN consultorios "		+ "c "	+ "ON(c.codigo"		+ "=ha.consultorio)"		+
						"LEFT OUTER JOIN dias_semana "		+ "d "	+ "ON(d.codigo"		+ "=ha.dia)"				+
						"LEFT OUTER JOIN("					+
							"medicos "					+ "m "	+
							"LEFT OUTER JOIN personas "		+ "p "	+ "ON(p.codigo"		+ "=m.codigo_medico)"		+
						")"										+ "on(m.codigo_medico=ha.codigo_medico)"						+
						"WHERE "	+	"ha.codigo=?";	
	

	/**
	* Dada la identificaci�n de un horario de atenci�n, carga los datos correspondientes desde la
	* fuente de datos. No utiliza la base por que esta consulta est� optimizada para Oracle
	*
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	Identificador �nico del horario de atenci�n
	* @return <code>HashMap</code> con los datos pedidos y una conexi�n abierta con la fuente de
	* datos
	*/
	private static Logger logger=Logger.getLogger(SqlBaseHorarioAtencionDao.class);
	public HashMap  cargarHorarioAtencion(Connection ac_con, int ai_codigo)throws SQLException
	{
		
		return SqlBaseHorarioAtencionDao.cargarHorarioAtencion(ac_con, ai_codigo);
	}

	/**
	* Dada la identificaci�n de un horario de atenci�n, elimina el horario de atenci�n de la fuente
	* de datos.
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	Identificador �nico del horario de atenci�n
	* @return N�mero de horarios de atenci�n eliminados
	*/
	
	public int eliminarHorarioAtencion(Connection ac_con, int ai_codigo)throws SQLException
	{
		
		return SqlBaseHorarioAtencionDao.eliminarHorarioAtencion(ac_con, ai_codigo);
	}

	/**
	* Dado un conjunto de identificaciones de horarios de atenci�n, elimina los horario de atenci�n
	* de la fuente de datos.
	* @param ac_con			Conexi�n abierta con una fuente de datos
	* @param aia_codigos	Conjunto de identificadores �nicos de los horario de atenci�n
	* @return N�mero de horarios de atenci�n eliminados
	*/
	public int eliminarHorarioAtencion(Connection ac_con, int[] aia_codigos)throws SQLException
	{
		return SqlBaseHorarioAtencionDao.eliminarHorarioAtencion(ac_con, aia_codigos);
	}

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
	)throws SQLException
	{
		return SqlBaseHorarioAtencionDao.insertarHorarioAtencion(ac_con,ai_centroAtencion, ai_unidadConsulta, ai_consultorio, ai_diaSemana, ai_codigoMedico, as_horaInicio, as_horaFin, ai_duracionConsulta, ai_pacientesSesion,DaoFactory.ORACLE);
	}

	/**
	* Lista los c�digo de los horario de atenci�n
	* @param ac_con Conexi�n abierta con una fuente de datos
	* @param centroAtencion
	* @return <code>Collection</code> con los c�digos de los horarios de atenci�n
	*/
	public Collection listarCodigosHorarioAtencion(Connection ac_con, int centroAtencion, String unidadesAgenda, int institucion)throws SQLException
	{
		return SqlBaseHorarioAtencionDao.listarCodigosHorarioAtencion(ac_con, centroAtencion, unidadesAgenda, institucion);
	}

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
		int 		ai_centroAtencion,
		int			ai_codigo,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_duracionConsulta,
		int			ai_pacientesSesion
	)throws SQLException
	{
		return SqlBaseHorarioAtencionDao.modificarHorarioAtencion(ac_con,ai_centroAtencion,ai_codigo, ai_unidadConsulta, ai_consultorio, ai_diaSemana, ai_codigoMedico, as_horaInicio, as_horaFin, ai_duracionConsulta, ai_pacientesSesion);
	}
	
	/**
	* consultar la Especialidad Unidad de Agenda
	* @param con Conexi�n abierta con una fuente de datos
	* @param HashMap parametros
	* @return HashMap<String,Object>
	*/
	public HashMap<String,Object> getEspecialidad(Connection con, HashMap parametros)
	{
		return SqlBaseHorarioAtencionDao.getEspecialidad(con, parametros);
	}
	
	public ArrayList<DtoHorarioAtencion> consultarHA(HashMap<String, Object> parametros)
	{
		return SqlBaseHorarioAtencionDao.consultarHA(parametros);
	}

	@Override
	public ArrayList<HashMap<String, Object>> consultarHAAvanzada(
			Connection con,
			HashMap<String, Object> parametros) {
		return SqlBaseHorarioAtencionDao.consultarHAAvanzada(con, parametros);
	}
}