/*
* @(#)AgendaDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao;


import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.InfoDatosInt;

/**
* Esta <i>interface</i> define el contrato de operaciones que debe
* implementar la clase que presta el servicio de acceso a datos para
* el objeto <code>Agenda</code>.
*
* @version 1.0, Sep 12, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public interface AgendaDao
{
	/**
	* Indica que el nivel de b�squeda que se requiere incluye todos los �tems de agenda que
	* coincidan con los par�metros solicitados, sin importar el n�mero de cupos disponibles
	*/
	public static final int LISTAR_TODOS = 1;

	/**
	* Indica que el nivel de b�squeda que se requiere incluye todos los �tems de agenda con cupos
	* disponibles que coincidan con los par�metros solicitados
	*/
	public static final int LISTAR_CUPOS_DISPONIBLES = 2;

	/**
	* Elimina los items de agenda de una fuente de datos, reutilizando una conexi�n existente
	* @param ac_con				Conexi�n abierta con una fuente de datos
	* @param as_fechaInicio		Fecha de inicio de la agenda a eliminar
	* @param as_fechaFin		Fecha de finalizaci�n de la agenda a eliminar
	* @param as_horaInicio		Hora de inicio que se eliminar� de la agenda
	* @param as_horaFin			Hora de finalizaci�n que se eliminar� de la agenda
	* @param ai_unidadConsulta	Unidad de consulta que se eliminar� de la agenda. Si es menor que 0
	*							ser�n eliminados todos los �tems de agenda que coincidan con los
	*							otros criterios
	* @param ai_consultorio		Consultorio que se eliminar� de la agenda. Si es menor que 0 ser�n
	*							eliminados todos los �tems de agenda que coincidan con los otros
	*							criterios
	* @param ai_diaSemana		D�a de la semana que se eliminar� de la agenda. Si es menor que 0
	*							ser�n eliminados los items de agenda que coincidan cn los otros
	*							criterios
	* @param ai_codigoMedico	C�digo del m�dico que se eliminar� de la agenda. Si es vacia ser�n
	*							eliminados todos los �tems de agenda que coincidan con los dem�s
	*							criterios
	*@param centroAtencion C�digo del centro de atenci�n que se eliminar� de la agenda. Si es vacia ser�n
	*							eliminados todos los �tems de agenda que coincidan con los dem�s
	*							criterios
	* @return N�mero de items de agenda eliminados
	*/
	public HashMap cancelarAgenda(
		Connection	ac_con,
		String		as_fechaInicio,
		String		as_fechaFin,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico,
		int 			centroAtencion,
		String centrosAtencion,
		String unidadesAgenda
	)
	throws Exception;

	/**
	* Obtiene los datos de un �tem de agenda, reutilizando una conexi�n existente
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	C�digo �nico del �tem de agenda a consultar
	* @return Datos del �tem de agenda de consultas solicitado
	*/
	public HashMap detalleAgenda(Connection ac_con, int ai_codigo) throws Exception;

	/**
	* Genera un agenda en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param as_fechaInicio			Fecha de inicio de la agenda a generar
	* @param as_fechaFin			Fecha de finalizaci�n de la agenda a generar
	* @param ai_unidadConsulta		Unidad de consulta sobre la cual se generar� la agenda. Si es
	*								menor que 0 la agenda ser� generada para todas las unidades de
	*								consulta con horarios de atenci�n definidos
	* @param ai_consultorio			Consultorio sobre el cual se generar� la agenda. Si es menor que
	*								0 la agenda ser� generada para todos los consultorios con
	*								horarios de atenci�n definidos
	* @param ai_diaSemana			D�a de la semana sobre el cual se generar� la agenda. Si es
	*								menor que 0 la agenda ser� generada para todos los di�s de la
	*								semana con horarios de atenci�n definidos
	* @param ai_codigoMedico		C�digo del m�dico sobre el cual se generar� la agenda. Si es
	*								vacia la agenda ser� generada para todos los m�dicos con
	*								horarios de atenci�n definidos
	* @param as_codigoUsuario		C�digo del usuario que genera la agenda
	* @param centroAtencion
	* @return N�mero de items de agenda generados
	*/
	public InfoDatosInt generarAgenda(
		Connection	ac_con,
		String		as_fechaInicio,
		String		as_fechaFin,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico,
		String		as_codigoUsuario,
		int 		centroAtencion,
		HashMap 	horaAtencionConMap,
		String 		centrosAtencion,
		String		unidadesAgenda
	)
	throws Exception;

	/**
	* Lista una agenda, desde una fuente de datos, reutilizando una conexi�n existente
	* @param ac_con				Conexi�n abierta con una fuente de datos
	 * @param ai_nivelBusqueda	Indica el nivel de b�squeda que se requiere para el listado
	 * @param ai_codigoPaciente	Codigo del paciente para el cual se reservar� la cita
	 * @param ai_sexoPaciente	C�digo del sexo del paciente para el cual se reservar� la cita
	 * @param as_fechaInicio		Fecha de inicio de la agenda a listar
	 * @param as_fechaFin		Fecha de finalizaci�n de la agenda a listar
	 * @param as_horaInicio		Hora de inicio de la agenda a listar
	 * @param as_horaFin			Hora de finalizaci�n de la agenda a listar
	 * @param ai_unidadConsulta	Unidad de consulta de la agenda a listar. Si es menor que 0 ser�n
	*							listados todos los �tems de agenda que coincidan con los otros
	*							criterios
	 * @param ai_consultorio		Consultorio de la agenda a listar. Si es menor que 0 ser�n listados
	*							todos los �tems de agenda que coincidan con los otros criterios
	 * @param ai_diaSemana		D�a de la semana de la agenda a listar. Si es menor que 0 ser�n
	*							listados los items de agenda que coincidan cn los otros criterios
	 * @param ai_codigoMedico	C�digo del m�dico de la agenda a listar. Si es vacia ser�n listados
	*							todos los �tems de agenda que coincidan con los dem�s criterios
	 * @param institucion @todo
	 * @param centroAtencion
	 * @param esReserva @todo
	 * @param disponibles solo se listan los cupos disponibles
	 * @return Datos del conjunto de items de agenda solicitados
	*/
	public Collection listarAgenda(
		Connection	ac_con,
		int			ai_nivelBusqueda,
		int			ai_codigoPaciente,
		int			ai_sexoPaciente,
		String		as_fechaInicio,
		String		as_fechaFin,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico, 
		int institucion,
		int centroAtencion,
		boolean esReserva,
		boolean disponibles,
		String centrosAtencion,
		String unidadesAgenda
	)
	throws Exception;
	
	/**
	 * 
	 * @param con
	 * @param horaInicio
	 * @param horaFin
	 * @param fechaInicio
	 * @param fechaFin
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @param centroAtencion
	 * @return
	 */
	public Collection listarCitasOcupadasAEliminar(Connection con,
			String horaInicio, 
			String horaFin, 
			String fechaInicio, 
			String fechaFin, 
			int unidadConsulta,
			int consultorio,
			int diaSemana,
			int codigoMedico,
			int centroAtencion,
			String centrosAtencion,
			String unidadesAgenda);

	/**
	 * 
	 * @param con
	 * @param is_horaInicio
	 * @param is_horaFin
	 * @param is_fechaInicio
	 * @param is_fechaFin
	 * @param ii_unidadConsulta
	 * @param ii_consultorio
	 * @param ii_diaSemana
	 * @param ii_codigoMedico
	 * @param centroAtencion
	 * @return
	 */
	public HashMap listarCitasNoAtendidasAEliminar(Connection con, String is_horaInicio, String is_horaFin, String is_fechaInicio, String is_fechaFin, int ii_unidadConsulta, int ii_consultorio, int ii_diaSemana, int ii_codigoMedico, int centroAtencion, String centrosAtencion, String unidadesAgenda);

	
	
	/**
	 * Consulta los Horarios de Atencion que no poseen Consultorios
	 * @param Connection con 
	 * @param String as_fechaInicio
	 * @param String as_fechaFin
	 * @param ai_unidadConsulta
	 * @param ai_diaSemana
	 * @param ai_codigoMedico,
	 * @param centroAtencion
	 * */
	public  HashMap getHorariosAtencionSinConsultorios(
													  Connection 	con,
													  String		as_fechaInicio,
													  String		as_fechaFin,
													  int			ai_unidadConsulta,
													  int			ai_diaSemana,
													  int			ai_codigoMedico,															  
													  int 			centroAtencion,
													  String 		unidadesAgenda,
													  String		centrosAtencion
													 );

	
}