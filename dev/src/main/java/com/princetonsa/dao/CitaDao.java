/*
* @(#)CitaDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;

/**
* Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta
* el servicio de acceso a datos para el objeto <code>Cita</code>.
*
* @version 1.0, Sep 22, 2003
* @author <a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
*/
public interface CitaDao
{
	/**
	 * Al cancelar una cita por instituci�n, se debe generar una cita similar, con estado a reprogramar
	 * @param ac_con
	 * @param codigoPaciente
	 * @param estadoLiquidaci�n
	 * @param numeroSolicitud
	 * @param codigoAgenda
	 * @param unidadConsulta
	 * @param fechaGeneracion
	 * @param horaGeneracion
	 * @param usuario
	 * @return
	 */
	public int asignarCitaCanncelada(
						Connection con,
						int codigoPaciente,
						String estadoLiquidacion,
						int numeroSolicitud,
						int codigoAgenda,
						int unidadConsulta,
						String fechaGeneracion,
						String horaGeneracion,
						String usuario);	
	/**
	* Asigna una cita en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con				Conexi�n abierta con una fuente de datos
	 * @param ai_paciente		C�digo del paciente para el cual se asignar� la cita
	 * @param ai_agenda			C�digo del �tem de agenda de consulta que se asignar� como cita
	 * @param ai_unidadConsulta	C�digo de la unidad de consulta que se le asignara a esta cita
	 * @param as_usuario			C�digo del usuario que asigna la cita
	 * @param observacion @todo
	* @return C�digo asignado a la cita. Si es menor que 0 es un c�digo inv�lido
	*/
	public int asignarCita(
		Connection	ac_con,
		int			ai_paciente,
		int			ai_agenda,
		int			ai_unidadConsulta,
		String		as_usuario, 
		HashMap mapaServicios,
		String estado,
		String fechaSolicitada,
		String telefono,
		String prioridad,
		String motivoAutorizacionCita, 
		String usuarioAutoriza, 
		String citasIncumpl
	)throws Exception;

	/**
	* Asignar el n�mero de solicitud a una cita reservada
	* @param ac_con				Conexi�n abierta con una fuente de datos
	* @param ai_codigo			C�digo �nico de la cita a modificar
	* @param ai_numeroSolicitud	N�mero de solicitud a asignar
	* @return El n�mero de citas modificadas
	*/
	public int asignarSolicitud(
		Connection	ac_con,
		int			ai_codigo,
		int			ai_numeroSolicitud,
		int codigoServicio,
		String loginUsuario,
		String estado
	)throws Exception;

	/**
	* Cancela una cita en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_codigo				C�digo de la cita a modificar
	* @param ai_agenda				C�digo del �tem de agenda de consulta que se actualizar� en la
	*								lista
	* @param ai_estadoCita			Nuevo estado de la cita
	* @param as_motivoCancelacion	Motivo de cancelaci�n de la cita
	* @return Indicador de exito de la operaci�n de modificaci�n de la cita
	*/
	public boolean cancelarCita(
		Connection	ac_con,
		int			ai_codigo,
		int			ai_agenda,
		int			ai_estadoCita,
		String		as_motivoCancelacion,
		String      as_codigoMotivoCancelacion,
		boolean cupoLibre,
		String usuario,
		String loginUsuario
	)throws Exception;

	/**
	* Obtiene los datos de una cita, reutilizando una conexi�n existente
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	C�digo �nico de la cita a consultar
	* @return Datos de la cita solicitada
	*/
	public HashMap detalleCita(Connection ac_con, int ai_codigo)throws Exception;

	/**
	* Lista las citas para un paciente, reutiliazando una conexi�n existente a una fuente de datos
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_modo				Indicador de modo de b�squeda de las citas
	* @param ai_paciente			C�digo del paciente
	* @param ai_unidadConsulta		C�digo de la unidad de consulta a la cual debe estar asociada la
	*								cita
	* @param ai_consultorio			C�digo del consultoria en la cual se llevara a cabo la cita
	* @param ai_medico				C�digo del m�dico
	* @param as_fechaInicio			Fecha inicial del rango de fechas de las citas
	* @param as_fechaFin			Fecha final del rango de fechas de las citas
	* @param as_horaInicio			Hora inicial del rango de horas de las citas
	* @param as_horaFin				Hora final del rango de fechas de las citas
	* @param ai_estadoCita			Estado de la cita
	* @param as_estadoLiquidacion	Estado de liquidaci�n de la cita
	* @param ai_cuenta				Codigo de la cuenta del paciente
	 * @param centroAtencion 
	*/
	public Collection listar(
		Connection ac_con,
		int			ai_modo,
		int			ai_paciente,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_medico,
		String 		as_fecha_solicitada,
		String		as_fechaInicio,
		String		as_fechaFin,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_estadoCita,
		String		as_estadoLiquidacion,
		int			ai_cuenta,
		String centroAtencion,
		String centrosAtencion,
		String unidadesMap
	);

	/**
	* Reserva una cita en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con				Conexi�n abierta con una fuente de datos
	 * @param ai_paciente		C�digo del paciente para el cual se reservar� la cita
	 * @param ai_agenda			C�digo del �tem de agenda de consulta que se reservar� como cita
	 * @param ai_unidadConsulta	C�digo de la unidad de consulta que se reservar� a esta cita
	 * @param as_usuario			C�digo del usuario que reserva la cita
	 * @param mapaServicios
	 * @param prioridad 
	* @return C�digo asignado a la cita. Si es menor que 0 es un c�digo inv�lido
	*/
	public int reservarCita(
		Connection	ac_con,
		int			ai_paciente,
		int			ai_agenda,
		int			ai_unidadConsulta,
		String		as_usuario, 
		HashMap 	mapaServicios,
		String 		fecha_solicitada, 
		String prioridad, 
		String motivoAutorizacionCita, 
		String usuarioAutoriza, 
		String requiereAuto, 
		String verificarEstCitaPac
	)throws Exception;

	/**
	 * Modifica la programaci�n de varias cita en una fuente de datos, reutilizando una conexi�n
	 * existente
	 * @param ac_con		Conexi�n abierta con una fuente de datos
	 * @param ac_citas	Conjunto de citas a reprogramar
	 * @param usuario
	 * @return Indicador de �xito de la reprogramaci�n de las citas
	 */
	public boolean reprogramarCitas(
		Connection	ac_con,
		Collection	ac_citas, String usuario
	)throws Exception;

	/**
	* Valida si el paciente tiene o no citas reservada para la fecha de un �tem de agenda
	* especificado
	* @param ac_con				Conexi�n abierta con una fuente de datos
	* @param ai_paciente		C�digo del paciente para el cual se verifica el n�mero de citas
	*							asignadas
	* @param ai_agenda			C�digo del �tem de agenda de consulta paa el cual se verifica el
	*							n�mero de cita asignadas
	* @return true si el paciente tiene citas reservadas para la fecha de �tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public boolean validarReservaCitaFecha(
		Connection	ac_con,
		int			ai_paciente,
		int			ai_agenda
	)throws Exception;
	
	/**
	* Valida si el paciente tiene o no citas reservada para la fecha y hora de un �tem de agenda
	* especificado
	* @param ac_con				Conexi�n abierta con una fuente de datos
	* @param ai_paciente		C�digo del paciente para el cual se verifica el n�mero de citas
	*							asignadas
	* @param ai_agenda			C�digo del �tem de agenda de consulta paa el cual se verifica el
	*							n�mero de cita asignadas
	* @return true si el paciente tiene citas reservadas para la fecha y hora de �tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public boolean validarReservaCitaFechaHora(
		Connection	ac_con,
		int			ai_paciente,
		int			ai_agenda
	)throws Exception;

	/**
	* Actualiza el estado a la cita con numero de solicitud dado
	* @param con
	 * @param codigoCita
	 * @param codEstadoCita
	 * @param estado
	 * @param usuarioModifica
	* @return
	*/
	public ResultadoBoolean actualizarEstadoCitaTransaccional(Connection con, int codigoCita, int codEstadoCita,String motivoNoAtencion, String estado, String usuarioModifica);
	
	/**
	 * Actualiza el estado a la cita con codigo de cita dado
	 * @param con
	 * @param codigoCita
	 * @param codEstadoCita
	 * @param estado
	 * @param usuarioModifica
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoCitaXCodigoTransaccional(Connection con, String codigoCita, int codEstadoCita, String estado, String usuarioModifica);
	
	/**
	 * Dados ciertos criterios de b�squeda, muestra todas las citas que
	 * cumplen con estos criterios (un conjunto de booleans indican
	 * cuales aplican)
	 * 
	 * @param fechaInicio Fecha desde la cual se quiere buscar
	 * @param buscarFechaInicio Boolean que me dice si
	 * deseo buscar por la fechaInicio
	 * @param fechaFin Fecha hasta la cual se quiere buscar
	 * @param buscarFechaFin Boolean que me dice si
	 * deseo buscar por la fechaFin
	 * @param horaInicio Hora desde la que se quiere buscar
	 * @param buscarHoraInicio Boolean que me dice si
	 * deseo buscar por la horaInicio 
	 * @param horaFin Hora hasta la que se quiere buscar
	 * @param buscarHoraFin Boolean que me dice si
	 * deseo buscar por la horaFin
	 * @param codigoCita C�digo de la cita por la que se desea
	 * buscar  
	 * @param buscarPorCodigo Boolean que dice si quiero buscar
	 * por el c�digo 
	 * @return
	 */
	public Collection listarCitas(String fechaInicio, boolean buscarFechaInicio, String fechaFin, boolean buscarFechaFin, String horaInicio, boolean buscarHoraInicio, String horaFin, boolean buscarHoraFin, int codigoCita[], boolean buscarCodigo);
	
	/**
	 * M�todo que consulta las citas y sus servicios para la impresion
	 * @param codigoCita
	 * @return
	 */
	public HashMap listarCitas(int[] codigoCita);
	
	/**
	 * M�todo para verificar si la cita fue cancelada anteriormente
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public boolean fueCanceladaIns(Connection con, int codigoAgenda);
	
	/**
	 * M�todo para actualizar las observaciones de la cita
	 * @param con
	 * @param observacion
	 * @return
	 */
	public int actualizarObservacion(Connection con, int codigoCita, String observacion, int codigoServicio,String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param codigoAgenda
	 * @param centroAtencion
	 * @param institucion
	 * @param unidadConsulta
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCostoXUnidadDeConsulta(Connection con, int codigoAgenda, int centroAtencion, int institucion, int unidadConsulta) throws SQLException;
	
	/**
	 * Metodo implementado para consultar los campos adicionales de la reserva de cita
	 * para la posterior creaci?n de la cuenta
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap consultaCamposAdicionalesReserva(Connection con,String codigoCita);
	
	/**
	 * Metodo que actualiza en la cita la informaci�n de la cuenta para el caso
	 * de la reserva
	 * @param con
	 * @param codigoCita
	 * @param convenio
	 * @param contrato
	 * @param estratoSocial
	 * @param tipoAfiliado
	 * @param naturalezaPaciente 
	 * @param ii_paciente 
	 * @param telefono 
	 * @param otrosTelefonos 
	 * @param celular 
	 */
	public int actualizarInfoCuentaCita(Connection con, int codigoCita, int convenio, int contrato, int estratoSocial, String tipoAfiliado, int naturalezaPaciente, String telefono,String origenTelefono, int ii_paciente, String celular, String otrosTelefonos);
	
	/**
	 * M�todo que consulta los servicios de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarServiciosCita(Connection con,HashMap campos);
	
	/**
	 * M�todo que anula el servicio de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public int anularServicioCita(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los estados las solicitudes de una cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap consultarEstadosSolicitudesCita(Connection con,String codigoCita);
	
	/**
	 * M�todo que realiza la inserci�n del servicio a la cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarServicioCita(Connection con ,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param codigoEstadoCita
	 * @return
	 */
	public String obtenerDescripcionEstadoCita(Connection con, int codigoEstadoCita);

	/**
	 * 
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public String obtenerEstadoCita(Connection con, int codigoCita);
	
	/**
	 * Metodo encargado de consultar la peticion
	 * dependiendo del numero de solicitud
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public String consultPetXNumSol (Connection connection, String numSol);
	
	
	
	/**
	 * Metodo encargado de eliminar un servicio de una cita.
	 * @param connection
	 * @param codigo
	 * @return
	 */
	public boolean eliminarServicioCita (Connection connection, String codigo);
	
	/**
	 * Metodo encargado de verificar si un servicio indicado
	 * se encuentra en la tabla servicios_cita.
	 * @param codigoServicioCita
	 * @param connection
	 */
	public String existeServicioEnCita (Connection connection, String codigoCita, String codigoServicio);
	
	/**
	 * Inserta la informacion del log de Reprogramacion de citas
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public boolean guardarLogReprogramacionCita(Connection con,HashMap parametros);
	
	/**
	 * M�todo implementado para actualizar la prioridad de una cita
	 */
	public boolean actualizarPrioridadCita(Connection con,HashMap campos);
	
	/**
	 * M�todo para actualizar las observaciones de la cita
	 * @param Connection con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap getReportePdfBaseCita(Connection con,HashMap parametros);
	
	
	/**
	 * Metodo consulta si existe Autorizacion del Paciente
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean consultarAutorizacionPaciente(Connection con, int codigoIngreso, int codigoConvenio);
}