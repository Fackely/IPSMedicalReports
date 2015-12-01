/*
* @(#)OracleCitaDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dao.CitaDao;
import com.princetonsa.dao.sqlbase.SqlBaseCitaDao;
import com.princetonsa.dao.sqlbase.SqlBaseCitaDaoSinglenton;

/**
* Esta clase implementa el contrato estipulado en <code>CitaDao</code>, y presta los servicios de
* acceso a una base de datos Oracle requeridos por la clase <code>Cita</code>.
*
* @version 1.0, Sep 22, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class OracleCitaDao implements CitaDao
{

	/**
	 * Cadena que aumenta en uno la secuencia de servicios cita
	 */
	private static final String secuenciaServiciosCita = "seq_servicios_cita.nextval";
	
	/**
	* Cadena constante con el <i>Statement</i> necesario para aumentar
	* en 1 la secuencia de la cita en una BD de datos Oracle
	*/
	private static final String is_aumentarSeqCita=
	"SELECT seq_cita.NEXTVAL from dual ";

	/**
	* Cadena constante con el <i>Statement</i> necesario para detallar los datos de una cita desde
	* una base de datos Oracle
	*/
	private static final String is_detalle =
		"SELECT "	+	"DISTINCT(ci.codigo)" + "AS codigoCita," +
						"ci.estado_cita "			+ "AS codigoEstadoCita," +
						"ec.nombre "				+ "AS nombreEstadoCita," +
						"ci.usuario "				+ "AS usuario," +
						"ci.codigo_paciente "	+ "AS codigoPaciente," +
						"ci.codigo_agenda "	+ "AS codigoAgenda," +
						"ci.estado_liquidacion "	+ "AS codigoEstadoLiquidacion,"	+
						"el.nombre "				+ "AS nombreEstadoLiquidacion,"	+
						"CASE "						+
							"WHEN a.codigo IS NULL THEN -1 "						+
							"ELSE a.codigo "										+
						"END "						+ "AS codigoAgenda,"			+
						"TO_CHAR(ci.fecha_gen,'YY/MM/DD')"		+ "AS fechaGeneracion,"			+
						"TO_CHAR(substr(ci.hora_gen,0,6))"			+ "AS horaGeneracion,"			+
						"ci.unidad_consulta "		+ "AS codigoUnidadConsulta,"	+
						"a.centro_atencion as codigocentroatencion," +
						"getnomcentroatencion(a.centro_atencion) as nombrecentroatencion,"	+
						"uc.descripcion "			+ "AS nombreUnidadConsulta,"	+
						"-1 "		+ "AS codigoServicio,"			+
						"coalesce(TO_CHAR(a.fecha,'YY/MM/DD'),'') AS fecha,"					+
						"coalesce(TO_CHAR(substr(a.hora_inicio,0,6)),'') AS horaInicio,"				+
						"coalesce(TO_CHAR(substr(a.hora_fin,0,6)),'') AS horaFin,"					+
						"coalesce(co.descripcion,'') AS nombreConsultorio,"		+
						"coalesce(p1.primer_nombre,'') AS primerNombreMedico,"		+
						"coalesce(p1.segundo_nombre,'') AS segundoNombreMedico,"		+
						"coalesce(p1.primer_apellido,'') AS primerApellidoMedico,"	+
						"coalesce(p1.segundo_apellido,'') AS segundoApellidoMedico,"	+
						"p2.primer_nombre "			+ "AS primerNombreUsuario,"		+
						"p2.segundo_nombre "		+ "AS segundoNombreUsuario,"	+
						"p2.primer_apellido "		+ "AS primerApellidoUsuario,"	+
						"p2.segundo_apellido "		+ "AS segundoApellidoUsuario,"	+
						"p3.primer_nombre "			+ "AS primerNombrePaciente,"	+
						"p3.segundo_nombre "		+ "AS segundoNombrePaciente,"	+
						"p3.primer_apellido "		+ "AS primerApellidoPaciente,"	+
						"p3.segundo_apellido "		+ "AS segundoApellidoPaciente "	+
		"FROM "		+	"cita "								+ "ci "	+
						"LEFT OUTER JOIN("					+
							"agenda "						+ "a "	+
							"INNER JOIN consultorios "		+ "co "	+ "ON(co.codigo"		+ "=a.consultorio)"			+
							"LEFT OUTER JOIN medicos "			+ "m "	+ "ON(m.codigo_medico=a.codigo_medico)"							+
							"LEFT OUTER JOIN personas "			+ "p1 "	+ "ON(p1.codigo"		+ "=m.codigo_medico)"		+
						")"											+ "ON(a.codigo"			+ "=ci.codigo_agenda)"		+
						"INNER JOIN estados_cita "			+ "ec "	+ "ON(ec.codigo"		+ "=ci.estado_cita)"		+
						"INNER JOIN estados_liquidacion "	+ "el "	+ "ON(el.acronimo"		+ "=ci.estado_liquidacion)"	+
						"INNER JOIN unidades_consulta "		+ "uc "	+ "ON(uc.codigo"		+ "=ci.unidad_consulta)"	+
						"INNER JOIN("						+
							"usuarios "						+ "u "	+
							"INNER JOIN personas "			+ "p2 "	+ "ON(p2.codigo"		+ "=u.codigo_persona)"		+
						")"											+ "ON(u.login"			+ "=ci.usuario)"			+
						"INNER JOIN personas "				+ "p3 "	+ "ON(p3.codigo"		+ "=ci.codigo_paciente)"	+
		"WHERE "	+	"ci.codigo"				+ "=?";



	/**
	* Cadena constante con el <i>Statement</i> necesario para validar el numero de citras asiganas a
	* un paciente en una base de datos Oracle
	*/
	private static final String is_validarCitasFecha =
		"SELECT "	+	"COUNT(*) "				+
		"FROM "		+	"cita "					+ "c "		+
						"INNER JOIN agenda "	+ "a1 "		+ "ON(a1.codigo=c.codigo_agenda)"	+
						"INNER JOIN agenda "	+ "a2 "		+ "on(a1.fecha=a2.fecha)"					+
		"WHERE "	+	"c.codigo_paciente"		+	"=? AND "	+
						"a2.codigo"				+	"=? AND "	+
						"c.estado_cita IN("		+
							ConstantesBD.codigoEstadoCitaAsignada		+ ","	+
							ConstantesBD.codigoEstadoCitaReservada		+ ","	+
							ConstantesBD.codigoEstadoCitaReprogramada	+
						")";
	
	/**
	* Cadena constante con el <i>Statement</i> necesario para validar el numero de citas asiganas a
	* un paciente en una base de datos Oracle en una fecha y hora
	*/
	private static final String is_validarCitasFechaHora =
		"SELECT "	+	"COUNT(*) "				+
		"FROM "		+	"cita "					+ "c "		+
						"INNER JOIN agenda "	+ "a1 "		+ "ON(a1.codigo=c.codigo_agenda)"	+
						"INNER JOIN agenda "	+ "a2 "		+ "on(a2.fecha=a1.fecha and a1.hora_inicio=a2.hora_inicio)"					+
		"WHERE "	+	"c.codigo_paciente"		+	"=? AND "	+
						"a2.codigo"				+	"=? AND "	+
						"c.estado_cita IN("		+
							ConstantesBD.codigoEstadoCitaAsignada		+ ","	+
							ConstantesBD.codigoEstadoCitaReservada		+ ","	+
							ConstantesBD.codigoEstadoCitaReprogramada	+
						")";

	/**
	* Cadena constante con el <i>Statement</i> necesario para validar si la cita puede o no ser
	* reservada en una base de datos Oracle
	*/
	private static final String is_validarReserva =
		"SELECT "	+	"COUNT(*) "				+
		"FROM "		+	"cita "					+ "c "	+
						"INNER JOIN agenda "	+ "a1 "	+ "ON(a1.codigo=c.codigo_agenda)"	+
						"INNER JOIN agenda "	+ "a2 "	+ "on(a1.fecha=a2.fecha)"		+
		"WHERE "	+	"c.codigo_paciente"		+	"=?"				+ " AND "	+
						"c.estado_cita IN("		+
							ConstantesBD.codigoEstadoCitaAsignada		+ ","	+
							ConstantesBD.codigoEstadoCitaReservada		+ ","	+
							ConstantesBD.codigoEstadoCitaReprogramada	+
						")"												+ "AND "	+
						"a2.codigo"				+	"=?"				+ " AND "	+
						"a2.cupos"				+	">0"				+ " AND"	+
						"("						+
							"a1.hora_inicio BETWEEN a2.hora_inicio AND a2.hora_fin"	+ " OR "	+
							"a1.hora_fin BETWEEN a2.hora_inicio AND a2.hora_fin"	+ " OR "	+
							"a2.hora_inicio BETWEEN a1.hora_inicio AND a1.hora_fin"	+
						")";

	private static String asignarCitaCancelada=" seq_cita.nextval ";

	/**
	 * Al cancelar una cita por instituciï¿½n, se debe generar una cita similar, con estado a reprogramar
	 * @param ac_con
	 * @param codigoPaciente
	 * @param estadoLiquidaciï¿½n
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
						String usuario)
	{
		return SqlBaseCitaDao.asignarCitaCanncelada(con,
				codigoPaciente,
				estadoLiquidacion,
				numeroSolicitud,
				codigoAgenda,
				unidadConsulta,
				fechaGeneracion,
				horaGeneracion,
				usuario,
				asignarCitaCancelada);
	}

	/**
	* Lista las citas para un paciente, reutilizando una conexiï¿½n existente 
	* en una BD Oracle
	* 
	* @param ac_con					Conexiï¿½n abierta con una fuente de datos
	* @param ai_modo				Indicador de modo de bï¿½squeda de las citas
	* @param ai_paciente			Cï¿½digo del paciente
	* @param ai_unidadConsulta		Cï¿½digo de la unidad de consulta a la cual debe estar asociada la
	*								cita
	* @param ai_consultorio			Cï¿½digo del consultoria en la cual se llevara a cabo la cita
	* @param ai_medico				Cï¿½digo del mï¿½dico
	* @param as_fechaInicio			Fecha inicial del rango de fechas de las citas
	* @param as_fechaFin			Fecha final del rango de fechas de las citas
	* @param as_horaInicio			Hora inicial del rango de horas de las citas
	* @param as_horaFin				Hora final del rango de fechas de las citas
	* @param ai_estadoCita			Estado de la cita
	* @param as_estadoLiquidacion	Estado de liquidaciï¿½n de la cita
	* @param ai_cuenta				Codigo de la cuenta del paciente
	*/
	public Collection listar(
		Connection ac_con,
		int			ai_modo,
		int			ai_paciente,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_medico,
		String 		as_fechaSolicitada,
		String		as_fechaInicio,
		String		as_fechaFin,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_estadoCita,
		String		as_estadoLiquidacion,
		int			ai_cuenta,
		String centroAtencion,
		String centrosAtencion,
		String unidadesAgenda
		
	)
	{
		return SqlBaseCitaDao.listar(ac_con, ai_modo, ai_paciente, ai_unidadConsulta, ai_consultorio, ai_medico, as_fechaSolicitada, as_fechaInicio, as_fechaFin, as_horaInicio, as_horaFin, ai_estadoCita, as_estadoLiquidacion, ai_cuenta, centroAtencion, centrosAtencion, unidadesAgenda);
	}
		
		

	/**
	* Modifica la programaciï¿½n de varias cita en una fuente de datos, reutilizando una conexiï¿½n
	* existente en una BD Oracle
	* 
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	 * @param ac_citas	Conjunto de citas a reprogramar
	* @return Indicador de ï¿½xito de la reprogramaciï¿½n de las citas
	*/
	public boolean reprogramarCitas(
		Connection	ac_con,
		Collection	ac_citas, String usuario
	)throws Exception
	{
		return SqlBaseCitaDao.reprogramarCitas( ac_con, ac_citas, is_validarReserva,secuenciaServiciosCita, usuario);
	}

	/**
	* Reserva una cita en una fuente de datos, reutilizando una conexion 
	* existente en una BD Oracle
	* 
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	 * @param ai_paciente		Cï¿½digo del paciente para el cual se reservarï¿½ la cita
	 * @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta que se reservarï¿½ como cita
	 * @param ai_unidadConsulta	Cï¿½digo de la unidad de consulta que se reservarï¿½ a esta cita
	 * @param as_usuario			Cï¿½digo del usuario que reserva la cita
	* @return Cï¿½digo asignado a la cita. Si es menor que 0 es un cï¿½digo invï¿½lido
	*/
	public int reservarCita(
		Connection	ac_con,
		int			ai_paciente,
		int			ai_agenda,
		int			ai_unidadConsulta,
		String		as_usuario, 
		HashMap 	mapaServicios,
		String 		fechaSolicitada,
		String prioridad, 
		String motivoAutorizacionCita, 
		String usuarioAutoriza, 
		String requiereAuto, 
		String verificarEstCitaPac
	)throws Exception
	{
		//Se hace el llamado al método sincronizado del proceso de Reserva de Citas, esta implementación se realiza
		//porque por temas de concurrencia se estan presentando inconsitencia de datos en la tabla citas
		SqlBaseCitaDaoSinglenton singleton = SqlBaseCitaDaoSinglenton.getInstance(ai_agenda);
		
		return singleton.reservarCita(ac_con, ai_paciente, ai_agenda, ai_unidadConsulta, as_usuario, is_validarReserva, is_aumentarSeqCita, mapaServicios,secuenciaServiciosCita,fechaSolicitada,prioridad,
				motivoAutorizacionCita, usuarioAutoriza, requiereAuto, verificarEstCitaPac);
	}

	/**
	* Valida si el paciente tiene o no citas reservada para la fecha de un ï¿½tem de agenda
	* especificado en una BD Oracle
	* 
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	* @param ai_paciente		Cï¿½digo del paciente para el cual se verifica el nï¿½mero de cita
	*							asignadas
	* @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta paa el cual se verifica el
	*							nï¿½mero de cita asignadas
	* @return true si el paciente tiene citas reservadas para la fecha de ï¿½tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public boolean validarReservaCitaFecha(
		Connection	ac_con,
		int			ai_paciente,
		int ai_agenda
	)throws Exception
	{
		return SqlBaseCitaDao.validarReservaCitaFecha( ac_con, ai_paciente, ai_agenda, is_validarCitasFecha) ;
	}
	
	/**
	* Valida si el paciente tiene o no citas reservada para la fecha y hora de un ï¿½tem de agenda
	* especificado en una BD Oracle
	* 
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	* @param ai_paciente		Cï¿½digo del paciente para el cual se verifica el nï¿½mero de cita
	*							asignadas
	* @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta paa el cual se verifica el
	*							nï¿½mero de cita asignadas
	* @return true si el paciente tiene citas reservadas para la fecha y hora de ï¿½tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public boolean validarReservaCitaFechaHora(
		Connection	ac_con,
		int			ai_paciente,
		int ai_agenda
	)throws Exception
	{
		return SqlBaseCitaDao.validarReservaCitaFechaHora( ac_con, ai_paciente, ai_agenda, is_validarCitasFechaHora) ;
	}

	/**
	* * Actualiza el estado a la cita con numero de solicitud dado 
	* en una BD Oracle
	*/
	public ResultadoBoolean actualizarEstadoCitaTransaccional(Connection con, int codigoCita, int codEstadoCita,String motivoNoAtencion, String estado, String usuarioModifica)
	{
		return SqlBaseCitaDao.actualizarEstadoCitaTransaccional(con, codigoCita, codEstadoCita,motivoNoAtencion, estado, usuarioModifica);
	}	
	/**
	 * Actualiza el estado a la cita con codigo de cita dado
	 * @param con
	 * @param codigoCita
	 * @param codEstadoCita
	 * @param estado
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoCitaXCodigoTransaccional(Connection con, String codigoCita, int codEstadoCita, String estado, String usuarioModifica)
	{
		return SqlBaseCitaDao.actualizarEstadoCitaXCodigoTransaccional(con, codigoCita, codEstadoCita, estado, usuarioModifica);
	}

	/**
	* Asigna una cita en una fuente de datos, reutilizando una conexion 
	* existente en una BD Oracle.
	* 
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	 * @param ai_paciente		Cï¿½digo del paciente para el cual se asignarï¿½ la cita
	 * @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta que se asignarï¿½ como cita
	 * @param ai_unidadConsulta	Cï¿½digo de la unidad de la consulta que se le asignara a esta cita
	 * @param as_usuario			Cï¿½digo del usuario que asigna la cita
	 * @param ai_codigoSolicitud	Cï¿½digo de la solicitud generada para esta cita
	* @return Cï¿½digo asignado a la cita. Si es menor que 0 es un cï¿½digo invï¿½lido
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
	)throws Exception
	{
		return SqlBaseCitaDao.asignarCita(ac_con, ai_paciente, ai_agenda, ai_unidadConsulta, as_usuario, is_validarReserva, is_aumentarSeqCita, mapaServicios, estado, secuenciaServiciosCita, fechaSolicitada, telefono, prioridad,
				motivoAutorizacionCita,usuarioAutoriza,citasIncumpl);
	}

	/**
	* Asignar el nï¿½mero de solicitud a una cita reservada 
	* en una BD Oracle
	* 
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo			Cï¿½digo ï¿½nico de la cita a modificar
	* @param ai_numeroSolicitud	Nï¿½mero de solicitud a asignar
	* @return El nï¿½mero de citas modificadas
	*/
	public int asignarSolicitud(
		Connection	ac_con,
		int			ai_codigo,
		int			ai_numeroSolicitud,
		int codigoServicio,
		String loginUsuario,
		String estado
	)throws Exception
	{
		return SqlBaseCitaDao.asignarSolicitud(ac_con, ai_codigo, ai_numeroSolicitud, codigoServicio,loginUsuario, estado);
	}

	/**
	* Cancela una cita en una fuente de datos, reutilizando una 
	* conexion existente en una BD Oracle.
	* 
	* @param ac_con					Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo				Cï¿½digo de la cita a modificar
	* @param ai_agenda				Cï¿½digo del ï¿½tem de agenda de consulta que se actualizarï¿½ en la
	*								lista
	* @param ai_estadoCita			Nuevo estado de la cita
	* @param as_motivoCancelacion	Motivo de cancelaciï¿½n de la cita
	* @return Indicador de exito de la operaciï¿½n de modificaciï¿½n de la cita
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
	)throws Exception
	{
		return SqlBaseCitaDao.cancelarCita( ac_con, ai_codigo, ai_agenda, ai_estadoCita, as_motivoCancelacion, cupoLibre, usuario, as_codigoMotivoCancelacion, loginUsuario);
	}

	/**
	* Obtiene los datos de una cita, reutilizando una conexiï¿½n existente
	* en una BD Oracle
	* 
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo	Cï¿½digo ï¿½nico de la cita a consultar
	* @return Datos de la cita solicitada
	*/
	public HashMap detalleCita(Connection ac_con, int ai_codigo)throws Exception
	{
		return SqlBaseCitaDao.detalleCita(ac_con, ai_codigo, is_detalle);
	}
	
	/**
	 * Mï¿½todo que implementa la bï¿½squeda de citas(Con toda la informaciï¿½n
	 * necesaria para imprimirlas) para una BD Oracle
	 * 
	* @see com.princetonsa.dao.CitaDao#listarCitas(String , boolean , String , boolean , String , boolean , String , boolean , int , boolean )
	 */
	public Collection listarCitas(String fechaInicio, boolean buscarFechaInicio, String fechaFin, boolean buscarFechaFin, String horaInicio, boolean buscarHoraInicio, String horaFin, boolean buscarHoraFin, int codigoCita[], boolean buscarCodigo)
	{
		return SqlBaseCitaDao.listarCitas(fechaInicio, buscarFechaInicio, fechaFin, buscarFechaFin, horaInicio, buscarHoraInicio, horaFin, buscarHoraFin, codigoCita, buscarCodigo);
	}
	
	/**
	 * Mï¿½todo que consulta las citas y sus servicios para la impresion
	 * @param codigoCita
	 * @return
	 */
	public HashMap listarCitas(int[] codigoCita)
	{
		return SqlBaseCitaDao.listarCitas(codigoCita);
	}
	
	/**
	 * Mï¿½todo para verificar si la cita fue cancelada anteriormente
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public boolean fueCanceladaIns(Connection con, int codigoAgenda)
	{
	    return SqlBaseCitaDao.fueCanceladaIns(con, codigoAgenda);
	}
	
	/**
	 * Mï¿½todo para actualizar las observaciones de la cita
	 * @param con
	 * @param observacion
	 * @return numero de actualizaciones en BD
	 */
	public int actualizarObservacion(Connection con, int codigoCita, String observacion, int codigoServicio,String loginUsuario)
	{
		return SqlBaseCitaDao.actualizarObservacion(con, codigoCita, observacion, codigoServicio,loginUsuario);
	}
	
	/**
	 *  Mï¿½todo para consultar los centros de costo de un medico 
	 * para un centro de atencion y una unidad de consulta dada
	 */
	public HashMap consultarCentrosCostoXUnidadDeConsulta(Connection con, int codigoAgenda, int centroAtencion, int institucion, int unidadConsulta) throws SQLException
	{
		return SqlBaseCitaDao.consultarCentrosCostoXUnidadDeConsulta(con, codigoAgenda, centroAtencion, institucion, unidadConsulta);
	}
	
		
	/**
	 * Metodo implementado para consultar los campos adicionales de la reserva de cita
	 * para la posterior creaci?n de la cuenta
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap consultaCamposAdicionalesReserva(Connection con,String codigoCita)
	{
		return SqlBaseCitaDao.consultaCamposAdicionalesReserva(con, codigoCita);
	}
	
	
	/**
	 * Metodo que actualiza en la cita la informaciï¿½n de la cuenta para el caso
	 * de la reserva
	 * @param con
	 * @param codigoCita
	 * @param convenio
	 * @param contrato
	 * @param estratoSocial
	 * @param tipoAfiliado
	 */
	public int actualizarInfoCuentaCita(Connection con, int codigoCita, int convenio, int contrato, int estratoSocial, String tipoAfiliado, int naturalezaPaciente,String telefono,String origenTelefono,int codigoPaciente,String celular,String otrosTelefonos)
	{
		return SqlBaseCitaDao.actualizarInfoCuentaCita (con, codigoCita, convenio, contrato, estratoSocial, tipoAfiliado,naturalezaPaciente,telefono,origenTelefono,codigoPaciente,celular,otrosTelefonos);
	}
	
	/**
	 * Mï¿½todo que consulta los servicios de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarServiciosCita(Connection con,HashMap campos)
	{
		return SqlBaseCitaDao.consultarServiciosCita(con, campos);
	}
	
	/**
	 * Mï¿½todo que anula el servicio de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public int anularServicioCita(Connection con,HashMap campos)
	{
		
		return SqlBaseCitaDao.anularServicioCita(con, campos);
	}
	
	/**
	 * Mï¿½todo que consulta los estados las solicitudes de una cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public HashMap consultarEstadosSolicitudesCita(Connection con,String codigoCita)
	{
		return SqlBaseCitaDao.consultarEstadosSolicitudesCita(con, codigoCita);
	}
	
	/**
	 * Mï¿½todo que realiza la inserciï¿½n del servicio a la cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarServicioCita(Connection con ,HashMap campos)
	{
		campos.put("secuencia","seq_servicios_cita.nextval");
		return SqlBaseCitaDao.insertarServicioCita(con, campos);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEstadoCita
	 * @return
	 */
	public String obtenerDescripcionEstadoCita(Connection con, int codigoEstadoCita)
	{
		return SqlBaseCitaDao.obtenerDescripcionEstadoCita(con, codigoEstadoCita);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEstadoCita
	 * @return
	 */
	public String obtenerEstadoCita(Connection con, int codigoCita)
	{
		return SqlBaseCitaDao.obtenerEstadoCita(con, codigoCita);
	}

	/**
	 * Metodo encargado de consultar la peticion
	 * dependiendo del numero de solicitud
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public String consultPetXNumSol (Connection connection, String numSol)
	{
		return SqlBaseCitaDao.consultPetXNumSol(connection, numSol);
	}
	
	/**
	 * Metodo encargado de eliminar un servicio de una cita.
	 * @param connection
	 * @param codigo
	 * @return
	 */
	public boolean eliminarServicioCita (Connection connection, String codigo)
	{
		return SqlBaseCitaDao.eliminarServicioCita(connection, codigo);
	}
	
	/**
	 * Metodo encargado de verificar si un servicio indicado
	 * se encuentra en la tabla servicios_cita.
	 * @param codigoServicioCita
	 * @param connection
	 */
	public String existeServicioEnCita (Connection connection, String codigoCita,String codigoServicio)
	{
		return SqlBaseCitaDao.existeServicioEnCita(connection, codigoCita,codigoServicio);
	}
	
	/**
	 * Inserta la informacion del log de Reprogramacion de citas
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public boolean guardarLogReprogramacionCita(Connection con,HashMap parametros)
	{
		return SqlBaseCitaDao.guardarLogReprogramacionCita(con, parametros);
	}
	
	/**
	 * Mï¿½todo implementado para actualizar la prioridad de una cita
	 */
	public boolean actualizarPrioridadCita(Connection con,HashMap campos)
	{
		return SqlBaseCitaDao.actualizarPrioridadCita(con, campos);
	}
	
	/**
	 * Mï¿½todo para actualizar las observaciones de la cita
	 * @param Connection con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap getReportePdfBaseCita(Connection con,HashMap parametros)
	{
		return SqlBaseCitaDao.getReportePdfBaseCita(con,parametros);
	}	

	/**
	 * Metodo consulta si existe Autorizacion del Paciente
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	@Override
	public boolean consultarAutorizacionPaciente(Connection con,
			int codigoIngreso, int codigoConvenio) {
		return SqlBaseCitaDao.consultarAutorizacionPaciente(con, codigoIngreso, codigoConvenio);
	}	
}