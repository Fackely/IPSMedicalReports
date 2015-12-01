/*
 * @(#)OracleSolicitudDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ValoresPorDefecto;

import com.princetonsa.dao.SolicitudDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;


/**
* Esta clase implementa el contrato estipulado en <code>SolicitudDao</code>, y presta los
* servicios de acceso a una base de datos Oracle requeridos por la clase
* <code>Solicitud</code>.
*
 * @version 2.0, Jan 14, 2003
 */
public class OracleSolicitudDao implements SolicitudDao
{
	/**
	 * obtiene el numero de solicitud a insertar para evitar problemas de consurrencia
	 */
	private static final String getNumeroSolicitudAInsertarStr="SELECT seq_real_solicitudes.nextval as codigosolic from dual";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar la solicitud
	 * de una valoraci�n inicial en una BD PostgreSQL.
	 *
	 * Es un Statement y M�todo particular pues muchos datos vienen definidos.
	 * En el Statement:
	 * fecha y hora de solicitud: Autom�tico el del sistema (En Interc. si se puede cambiar)
	 * especialidad solicitante: En general las valoraciones iniciales (Urg y
	 * Hosp) las hace personal administrativo, no m�dico
	 * ocupaci�n solicitada: La puede resp. cualquier m�dico
	 * Siempre utiliza el n�mero de la secuencia que se rastrea (base)
	 * Siempre va a epicrisis y siempre es urgente
	 */
	// modificar
	// numero_autorizacion
	private static final String insertarSolicitudValoracionInicialStr="INSERT  into solicitudes (numero_solicitud, fecha_grabacion, hora_grabacion, fecha_solicitud, hora_solicitud, especialidad_solicitante, ocupacion_solicitada, centro_costo_solicitante, centro_costo_solicitado, consecutivo_ordenes_medicas, cuenta, cobrable, va_epicrisis, urgente, estado_historia_clinica, tipo ) values (?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", 0, 0, ?, ?, seq_sol_consecutivo_base.nextval, ?, ?, "+ValoresPorDefecto.getValorTrueCortoParaConsultas()+", "+ValoresPorDefecto.getValorTrueCortoParaConsultas()+", 1, ? )";

	/**
	 * String para insertar una solicitud general interna (debido al
	 * consecutivo existen estas diferencias)
	 */
	// modificar
	// numero_autorizacion,
	//private static final String insertarSolicitudGeneralInternaStr ="INSERT INTO solicitudes (numero_solicitud, consecutivo_ordenes_medicas, fecha_grabacion, hora_grabacion, fecha_solicitud, hora_solicitud, tipo, cobrable, especialidad_solicitante, ocupacion_solicitada, centro_costo_solicitante, centro_costo_solicitado, codigo_medico, cuenta, va_epicrisis, urgente, estado_historia_clinica, datos_medico,pyp, tiene_cita, liq_asocios,justificacion_solicitud, especialidad_solicitada, codigo_medico_responde, datos_medico_responde) VALUES (?, seq_sol_consecutivo_base.nextval, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?||' '||?||' '||getnombremedico(?)||' R.M. '||getregmedico(?)||' '||getespecialidadesmedico(?,' '))";
	private static final String insertarSolicitudGeneralInternaStr ="INSERT INTO solicitudes (numero_solicitud, consecutivo_ordenes_medicas, " +
	"fecha_grabacion, hora_grabacion, fecha_solicitud, hora_solicitud, tipo, cobrable, especialidad_solicitante, ocupacion_solicitada, " +
	"centro_costo_solicitante, centro_costo_solicitado, codigo_medico, cuenta, " +
	"va_epicrisis, urgente, estado_historia_clinica, datos_medico,pyp, tiene_cita, liq_asocios, " +
	"justificacion_solicitud, especialidad_solicitada, codigo_medico_responde, datos_medico_responde, " +
	"acronimo_diagnostico, tipo_cie_diagnostico,pool) " +
	"VALUES (?, seq_sol_consecutivo_base.nextval, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+
	", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?||' '||?||' '||getnombremedico(?)||' R.M. '||" +
	"getregmedico(?)||' '||getespecialidadesmedico(?,' '), ?, ?,?)";
	
	
	
	
	/**
	 * String para insertar una solicitud general externa (debido al
	 * consecutivo existen estas diferencias)
	 */
	// modificar
	// numero_autorizacion, 
	private static final String insertarSolicitudGeneralExternaStr="INSERT INTO solicitudes (numero_solicitud, consecutivo_ordenes_medicas, fecha_grabacion, hora_grabacion, fecha_solicitud, hora_solicitud, tipo, cobrable, especialidad_solicitante, ocupacion_solicitada, centro_costo_solicitante, centro_costo_solicitado, codigo_medico, cuenta, va_epicrisis, urgente, estado_historia_clinica, datos_medico,pyp, tiene_cita, liq_asocios,justificacion_solicitud, especialidad_solicitada, codigo_medico_responde, datos_medico_responde) VALUES (?, seq_sol_consecutivo_otros.nextval, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?||' '||?||' '||getnombremedico(?))";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una
	 * definici�n de tratante en una BD PostgreSQL.
	 */
	private static final String insertarTratanteStr="INSERT INTO tratantes_cuenta " +
		"(codigo, solicitud, codigo_medico, fecha_inicio, fecha_fin, hora_inicio, hora_fin, centro_costo,cuenta) " +
		"values " +
		"(seq_tratantes_cuenta.nextval, ?, ?, CURRENT_DATE, null, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", null, ?,(SELECT cuenta FROM solicitudes WHERE numero_solicitud = ?))";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar
	 * una entrada en solicitud de cambio tratante en una BD PostgreSQL.
	 */
	private static final String insertarEntradaSolicitudesCambioTratanteStr="INSERT INTO sol_cambio_tratante (codigo, cuenta, solicitud, activa) values (SEQ_SOL_CAMBIO_TRAT.nextval, ?, ?, "+ValoresPorDefecto.getValorTrueCortoParaConsultas()+")";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar
	 * el adjunto para una cuenta en una BD PostgreSQL.
	 */
	private static final String insertarAdjuntoCuentaStr="INSERT INTO adjuntos_cuenta " +
		"(codigo, solicitud, codigo_medico, fecha_inicio, hora_inicio, centro_costo, cuenta) " +
		"values " +
		"(seq_adjuntos_cuenta.nextval, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, (SELECT cuenta FROM solicitudes WHERE numero_solicitud = ? ))";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar
	 * el adjunto para una cuenta en una BD PostgreSQL, manteniendo fecha inicio
	 * y fecha fin (Al mover adjuntos de un lado a otro).
	 */
	private static final String insertarAdjuntoAnteriorStr="INSERT into adjuntos_cuenta " +
			"(codigo, solicitud, codigo_medico, fecha_inicio, hora_inicio, centro_costo, cuenta) " +
			"values " +
			"(seq_adjuntos_cuenta.nextval, ?, ?, ?, ?, ?, (SELECT cuenta FROM solicitudes WHERE numero_solicitud = ?))";
	
	/**
	 * Implementaci�n del m�todo que cambia la solicitud de m�dico
	 * tratante en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cambiarSolicitudMedicoTratante (Connection , int , int , int , String ) throws SQLException
	 */
	public int cambiarSolicitudMedicoTratante (Connection con, int numeroSolicitud, int idCuenta, int codigoMedico, String estado) throws SQLException
	{
	    return SqlBaseSolicitudDao.cambiarSolicitudMedicoTratante (con, numeroSolicitud, idCuenta, codigoMedico,  estado,  insertarEntradaSolicitudesCambioTratanteStr) ;
	}

	/**
	 * M�todo que implementa la inserci�n de una entrada
	 * de tratante en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarTratante (Connection , int , int , String , String ) throws SQLException
	 */
	public int insertarTratante (Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoTratante) throws SQLException
	{
	    return SqlBaseSolicitudDao.insertarTratante (con, numeroSolicitud, codigoMedico, codigoCentroCostoTratante, insertarTratanteStr) ;
	}

	/**
	 * M�todo la inserci�n de una solicitud general
	 * en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarSolicitudGeneral (Connection , String , String , int , boolean , String , int , int , int , int  , int , int , boolean , boolean , String, int , int ) throws SQLException
	 */
	// modificar
	// String numeroAutorizacion,
	public int insertarSolicitudGeneral (Connection con, String fechaSolicitud, String horaSolicitud, int codigoTipoSolicitud, 
			boolean cobrable, int codigoEspecialidadSolicitante, int codigoOcupacionSolicitada, int codigoCentroCostoSolicitante, 
			int  codigoCentroCostoSolicitado, int codigoMedicoCreador, int codigoCuenta, boolean vaEpicrisis, boolean urgente, 
			int estadoClinico, String datosMedico,boolean solPYP, boolean tieneCita, String liquidarAsocio,String justificacionSolicitud, 
			int especialidadSolicitadaOrdAmbulatorias, int codigoMedicoResponde, DtoDiagnostico dtoDiagnostico) throws SQLException
	{
		int numeroSolicitudAInsertar=this.getNumeroSolicitudAinsertar(con);
		if(numeroSolicitudAInsertar>0)
			return SqlBaseSolicitudDao.insertarSolicitudGeneral (con, fechaSolicitud, horaSolicitud, codigoTipoSolicitud, cobrable, 
					codigoEspecialidadSolicitante, codigoOcupacionSolicitada, codigoCentroCostoSolicitante, codigoCentroCostoSolicitado, 
					codigoMedicoCreador, codigoCuenta, vaEpicrisis, urgente, estadoClinico, datosMedico,solPYP, tieneCita, liquidarAsocio,
					insertarSolicitudGeneralExternaStr, insertarSolicitudGeneralInternaStr, numeroSolicitudAInsertar,justificacionSolicitud,
					especialidadSolicitadaOrdAmbulatorias, codigoMedicoResponde, dtoDiagnostico) ;
		return 0;
	}
 
	/**
	 * M�todo que implementa la inserci�n de la solicitud inicial de
	 * valoraci�n en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarSolicitudValoracionInicial (Connection , String , int , int , int , boolean , int ) throws SQLException
	 */
	// modificar
	// String numeroAutorizacion,
	public int insertarSolicitudValoracionInicial (Connection con, int codigoCentroCostoSolicitante, int codigoCentroCostoSolicitado, int codigoCuenta, boolean cobrable, int codigoTipoSolicitud) throws SQLException
	{
		int numeroSolicitudAInsertar=this.getNumeroSolicitudAinsertar(con);
		if(numeroSolicitudAInsertar>0)			
			return SqlBaseSolicitudDao.insertarSolicitudValoracionInicial (con, codigoCentroCostoSolicitante, codigoCentroCostoSolicitado, codigoCuenta, cobrable, codigoTipoSolicitud, insertarSolicitudValoracionInicialStr, numeroSolicitudAInsertar) ;
		return -1;
	}

	/**
	 * M�todo para la creaci�n (sin validaciones) de un adjunto para
	 * cuenta en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#crearAdjuntoCuenta (Connection , int , int ) throws SQLException
	 */
	public int crearAdjuntoCuenta (Connection con, int numeroSolicitud, int codigoMedicoSolicitante, int codigoCentroCostoSolicitante) throws SQLException
	{
	    return SqlBaseSolicitudDao.crearAdjuntoCuenta (con, numeroSolicitud, codigoMedicoSolicitante, codigoCentroCostoSolicitante, insertarAdjuntoCuentaStr);
	}

	/**
	 * M�todo que implementa la inserci�n de un tratante para
	 * urgencias en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarTratanteUrgencias (Connection , int , int ) throws SQLException
	 */
	public int insertarTratanteUrgencias (Connection con, int numeroSolicitud,int area) throws SQLException
	{
		return this.insertarTratante(con, numeroSolicitud,  -1, area);
	}

	/**
	 * M�todo que implementa la cerrada /eliminaci�n de un tratante para
	 * urgencias en una BD Postgresql (Queda en BD registro con fecha/
	 * hora inicio y fecha/hora fin)
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cerrarAntiguaEntradaTratanteDadaSolicitud(Connection, int ) throws SQLException
	 */
	public int cerrarAntiguaEntradaTratanteDadaSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudDao.cerrarAntiguaEntradaTratanteDadaSolicitud (con, numeroSolicitud) ;
	}

	/**
	* Implementaci�n para PostgreSQL del m�todo que interpreta una solicitud
	*
	* @param ac_con				Conexi�n abierta a una fuente de datos
	* @param ai_numeroSolicitud	N�mero de la solicitud a ser interpretada
	* @param ai_codigoMedico	C�digo del m�dico que realiza la interpretaci�n de la consulta
	* @param as_interpretacion	Texto de la interpretaci�n de la solicitud
	* @param ab_vaEpicrisis		Indicador de si la solicitud va o no a la epicrisis
	* @return N�mero de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos PostgreSQL
	*/
	public int interpretarSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		int			ai_codigoMedico,
		String		as_interpretacion

	)throws SQLException
	{
		return SqlBaseSolicitudDao.interpretarSolicitud(ac_con, ai_numeroSolicitud, ai_codigoMedico, as_interpretacion);
	}
	
	/**
	 * M�todo que inserta la interpretacion de una solicitud de procedimientos,
	 * se diferencia del otro m�todo de que la fecha/hora interpretacion se mandan como par�metros
	 * @param con
	 * @param campos
	 * @return
	 */
	public int interpretarSolicitud(Connection con,HashMap campos)
	{
		return SqlBaseSolicitudDao.interpretarSolicitud(con,campos);
	}


	/**
	 * M�todo que implementa la cerrada /eliminaci�n de un tratante para
	 * urgencias en una BD Postgresql (Queda en BD registro con fecha/
	 * hora inicio y fecha/hora fin)
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cerrarAntiguaEntradaTratante (Connection, int ) throws SQLException
	 */
	public int cerrarAntiguaEntradaTratante (Connection con, int idCuenta ) throws SQLException
	{
		return SqlBaseSolicitudDao.cerrarAntiguaEntradaTratante (con, idCuenta ) ;
	}


/*
	/**
	 * M�todo que revisa si existe una entrada previa para el centro
	 * de costo y cuenta definidos en una solicitud en adjuntos_cuenta
	 *
	 * @param con Conexi�n con la BD Postgresql
	 * @param numeroSolicitud N�mero de la solicitud que se quiere
	 * postular como futuro adjunto
	 * @return
	 * @throws SQLException
	 */
/*	private boolean hayAdjuntoPrevio (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudDao.hayAdjuntoPrevio (con, numeroSolicitud) ;
	}
*/

	/**
	* Implementaci�n para PostgreSQL del m�todo que modifica la interpretaci�n de una solicitud
	*
	* @param ac_con				Conexi�n abierta a una fuente de datos
	* @param ai_numeroSolicitud	N�mero de la solicitud a ser interpretada
	* @param as_interpretacion	Texto de la interpretaci�n de la solicitud
	* @param ab_vaEpicrisis		Indicador de si la solicitud va o no a la epicrisis
	* @return N�mero de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos PostgreSQL
	*/
	public int modificarInterpretacionSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_interpretacion
	)throws SQLException
	{
		return SqlBaseSolicitudDao.modificarInterpretacionSolicitud(ac_con, ai_numeroSolicitud, as_interpretacion);
	}

/*
	/**
	 * M�todo que obtiene el n�mero de la �ltima solicitud insertada
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
/*	private int obtenerNumeroSolicitudRecienInsertada (Connection con) throws SQLException
	{
		return SqlBaseSolicitudDao.obtenerNumeroSolicitudRecienInsertada (con) ;
	}
*/

	/**
	 * M�todo que implementa la cargada de una solicitud
	 * en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cargarSolicitud (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargarSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudDao.cargarSolicitud (con, numeroSolicitud) ;
	}

	/**
	 * Metodo que carga todas las solicitudes de una cuenta, y retorna una colleccion de solicitudes
	 * @armando
	 * @param con, conexion
	 * @param cuenta, cuenta que se desea consultar
	 * @return  collection, datos de la consulta.
	 */
	
	public Collection cargarSolicitudesInternas(Connection con,int cuenta)
	{
		return SqlBaseSolicitudDao.cargarSolicitudesInternas(con,cuenta);	
	}
	/**
	 * M�todo que implementa la inactivaci�n de una solicitud
	 * de cambio tratante en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#inactivarSolicitudCambioTratante(Connection , int )
	 */
	public ResultadoBoolean inactivarSolicitudCambioTratante(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudDao.inactivarSolicitudCambioTratante(con, numeroSolicitud);
	}

	/**
	 * Implementaci�n del m�todo que realiza los cambios necesarios
	 * para aceptar una solicitud de cambio de m�dico tratante en una 
	 * BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#(Connection , int , int ) throws SQLException
	 */
	public boolean cambiosRequeridosAceptacionTratanteCasoAsocio (Connection con, int numeroSolicitud, int idCuentaDestino) throws SQLException
	{
		return SqlBaseSolicitudDao.cambiosRequeridosAceptacionTratanteCasoAsocio (con, numeroSolicitud, idCuentaDestino) ;
	}

	/**
	 * M�todo que implementa la revisi�n de existencia de solicitud
	 * de cambio tratante en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#haySolicitudCambioTratantePrevia (Connection , int ) throws SQLException
	 */
	public boolean haySolicitudCambioTratantePrevia (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseSolicitudDao.haySolicitudCambioTratantePrevia (con, idCuenta);
	}

	/**
	 * M�todo que implementa la anulacion de una solicitud en
	 * una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#anularSolicitud (Connection , int , String , int  ) throws SQLException
	 */
	public int anularSolicitud (Connection con, int numeroSolicitud, String motivoAnulacion, int  codigoMedicoAnulacion) throws SQLException
	{
		return SqlBaseSolicitudDao.anularSolicitud (con, numeroSolicitud, motivoAnulacion, codigoMedicoAnulacion) ;
	}

	/**
	 * M�todo que implementa la finalizaci�n de atenci�n conjunta
	 * una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#finalizarAtencionConjunta (Connection , int , int , String ) throws SQLException
	 */
	public int finalizarAtencionConjunta (Connection con, int idCuenta, int codigoCentroCostoAdjunto, String notaFinalizacion) throws SQLException
	{
		return SqlBaseSolicitudDao.finalizarAtencionConjunta (con, idCuenta, codigoCentroCostoAdjunto, notaFinalizacion) ;
	}

	/**
	 * M�todo que implementa los cambios de solicitud
	 * una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cambiarEstadosSolicitud (Connection , int , int , int )
	 */
	public ResultadoBoolean cambiarEstadosSolicitud (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica)
	{
		return SqlBaseSolicitudDao.cambiarEstadosSolicitud (con, numeroSolicitud, codigoEstadoFacturacion, codigoEstadoHistoriaClinica);
	}

	/**
	 * Cambia dentro de una transaccion los estados de facturaci�n o de historica clinica, o ambos,
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cambiarEstadosSolicitudTransaccional (Connection , int , int , int , String )
	 */
	public ResultadoBoolean cambiarEstadosSolicitudTransaccional (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica, String estado)
	{
		return SqlBaseSolicitudDao.cambiarEstadosSolicitudTransaccional (con, numeroSolicitud, codigoEstadoFacturacion, codigoEstadoHistoriaClinica, estado);
	}

	/**
	 * Actualiza el n�mero de autorizaci�n
	 *
	 * @see com.princetonsa.dao.SolicitudDao#actualizarNumeroAutorizacionTransaccional(Connection , int , String , String )
	 */
	//modificar
	/*public ResultadoBoolean actualizarNumeroAutorizacionTransaccional(Connection con, int numeroSolicitud, String numeroAutorizacion, String estado)
	{
		return SqlBaseSolicitudDao.actualizarNumeroAutorizacionTransaccional(con, numeroSolicitud, numeroAutorizacion, estado);
	}*/	
	/**
	 * Retorna el n�mero de autorizaci�n
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cargarNumeroAutorizacion(Connection , int )
	 */
	/*
	public ResultadoCollectionDB cargarNumeroAutorizacion(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudDao.cargarNumeroAutorizacion(con, numeroSolicitud);
	}
	*/
	/**
	* M�todo que carga el motivo de anulaci�n de una solicitud
	* de Interconsulta en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cargarMotivoAnulacionSolicitudInterconsulta (Connection , int ) throws SQLException
	*/
	public ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseSolicitudDao.cargarMotivoAnulacionSolicitudInterconsulta (con, numeroSolicitud) ;
	}

	/**
	* M�todo que carga el motivo de anulaci�n de una solicitud
	* de Interconsulta en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#modificarEpicrisis(Connection , int , boolean ) throws SQLException
	*/
	public int modificarEpicrisis(Connection con , int numeroSolicitud,boolean estado) throws SQLException
	{
		return SqlBaseSolicitudDao.modificarEpicrisis(con , numeroSolicitud,estado) ;
	}

	/**
	* M�todo que implementa el cambio de centro de costo solicitado
	* en una BD Postgresql
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cambiarCentroCostoSolicitado(Connection , int , int )
	*/
	public ResultadoBoolean cambiarCentroCostoSolicitado(Connection con, int numeroSolicitud, int centroCostoSolicitado)
	{
		return SqlBaseSolicitudDao.cambiarCentroCostoSolicitado(con, numeroSolicitud, centroCostoSolicitado);
	}
	
	/**
	* M�todo que implementa el cambio de centro de costo solicitante
	* en una BD Gen�rica 
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#cambiarCentroCostoSolicitante(Connection , int , int )
	*/
	public ResultadoBoolean cambiarCentroCostoSolicitante(Connection con, int numeroSolicitud, int centroCostoSolicitante)
	{
		return SqlBaseSolicitudDao.cambiarCentroCostoSolicitante(con, numeroSolicitud, centroCostoSolicitante);
	}

	/**
	 * Implementaci�n del m�todo mueve / copia datos en caso de asocio
	 * de cuenta en una BD Hsqldb
	 *
	 * @see com.princetonsa.dao.SolicitudDao#moverPorAsocio (Connection , int , int ) throws SQLException
	 */
	public int moverPorAsocio (Connection con, int cuentaOrigen, int cuentaDestino, int codigoMedicoTratante, int codigoCentroCostoTratante, int institucion) throws SQLException
	{
		return SqlBaseSolicitudDao.moverPorAsocio (con, cuentaOrigen, cuentaDestino, insertarAdjuntoAnteriorStr, codigoMedicoTratante, insertarTratanteStr, codigoCentroCostoTratante, institucion);
	}

	/**
	 * M�todo para actualizar la fecha y la hora de la solicitud
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param fecha Fecha actualizada
	 * @param hora Hora actualizada
	 * @param estado Estado de la transacci�n
	 * @return entero mayor que 1 si se realiz� correctamente la modificaci�n 
	 */
	public int actualizarFechaHoraTransaccional(Connection con, int numeroSolicitud, String fecha, String hora, String estado)
	{
		return SqlBaseSolicitudDao.actualizarFechaHoraTransaccional(con, numeroSolicitud, fecha, hora, estado);
	}
	
	/**
	 * M�todo para actualizar la prioridad de la solicitud
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param urgente Prioridad (true --> urgente)
	 * @param estado Estado de la transacci�n
	 * @return entero mayor que 1 si se realiz� correctamente la modificaci�n
	 */
	public int actualizarPrioridadUrgenteTransaccional(Connection con, int numeroSolicitud, boolean urgente, String estado)
	{
		return SqlBaseSolicitudDao.actualizarPrioridadUrgenteTransaccional(con, numeroSolicitud, urgente, estado);
	}
	
	/**
	 * Implementaci�n del m�todo que actualiza el
	 * m�dico que responde en una BD Posgresql
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#actualizarMedicoResponde (con, numeroSolicitud, idMedico, detalleMedico) ;
	 */
	public int actualizarMedicoResponde (Connection con, int numeroSolicitud, int idMedico, String detalleMedico) throws SQLException
	{
	    return SqlBaseSolicitudDao.actualizarMedicoResponde (con, numeroSolicitud, idMedico, detalleMedico) ;
	}












/*
	/**
	 * Cadena constante para cambiar la opcion de manejo
	 * de la solicitud, se utiliza para el caso
	 * en el cual se hizo otra solicitud de interconsulta
	 * con opcion cambio m�dico tratante
	 */
//	private static final String cambiarAManejoConjuntoStr="UPDATE solicitudes_inter SET codigo_manejo_interconsulta="+ConstantesBD.codigoManejoConjunto+", manejo_conjunto_finalizado=false WHERE numero_solicitud=?";

/*
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * las solicitudes activas en una BD PostgreSQL.
	 */
//	private static final String buscarSolicitudesActivasStr="SELECT ct.solicitud as numeroSolicitud, sol.codigo_medico as codigoMedico, sol.centro_costo_solicitado as codigoCentroCosto from sol_cambio_tratante ct, solicitudes sol where ct.cuenta=? and activa="+ValoresPorDefecto.getValorTrueCortoParaConsultas()+" and ct.solicitud=sol.numero_solicitud";

/*
	/**
	 * Cadena constante con el <i>statement</i> necesario para cancelar
	 * un tratante en una BD PostgreSQL.
	 */
//	private static final String insertarCancelacionTratanteStr="INSERT INTO cancelacion_tratantes (codigo, codigo_medico, fecha, hora) values ((SELECT max(codigo) from sol_cambio_tratante where cuenta =?), ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";

/*
	/**
	 * Cadena constante con el <i>statement</i> necesario para cancelar
	 * las solicitudes de cambio de manejo para una cuenta en una
	 * BD PostgreSQL.
	 */
//	private static final String cancelarSolicitudCambioManejoAnteriorDadaCuentaStr="UPDATE sol_cambio_tratante set activa=false where solicitud = (SELECT (vali.numero_solicitud) from sol_cambio_tratante solct, valoraciones_consulta vali where solct.solicitud=vali.numero_solicitud and solct.activa="+ValoresPorDefecto.getValorTrueCortoParaConsultas()+" and solct.cuenta=?)";

/*
	/**
	 * Cadena constante con el <i>statement</i> necesario para cancelar
	 * las solicitudes de cambio de manejo para una cuenta en una
	 * BD PostgreSQL.
	 */
//	private static final String cancelarSolicitudCambioManejoAnteriorStr="UPDATE sol_cambio_tratante set activa=false where cuenta = ?";



	/**
	 * M�todo para ingresar la justificaci�n del procedimiento
	 * este m�todo solo inserta un atributo, as� que se llama
	 * cada vez que se quiere insertar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param atributo Par�metro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacci�n
	 * @return int mayor que 0 si se insert� correctamente el atributo espec�fico.
	 */
	public int ingresarAtributoTransaccional(Connection con, int numeroSolicitud, int servicio, int atributo, String descripcion, String estado){
		return SqlBaseSolicitudDao.ingresarAtributoTransaccional(con,numeroSolicitud,servicio,atributo,descripcion,estado);
	}

	/**
	 * M�todo que actualiza el tipo de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudValoracionGinecoObstetrica
	 * @return Numero de registros modificados
	 */
	public int actualizarTipoSolicitud(Connection con, int numeroSolicitud, int tipoSolicitud)
	{
		return SqlBaseSolicitudDao.actualizarTipoSolicitud(con, numeroSolicitud, tipoSolicitud);
	}

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @param pool
	 * @return
	 */
	public int actualizarPoolSolicitud(Connection con, int numeroSolicitud, int pool)
	{
		return SqlBaseSolicitudDao.actualizarPoolSolicitud(con, numeroSolicitud, pool);
	}
	
	/**
	 * Obtiene el numero de solicitud a inasertar para evitar problemas
	 * de concurrencia
	 * @param con
	 * @param consulta
	 * @return
	 */
	public int getNumeroSolicitudAinsertar(Connection con)
	{
		return SqlBaseSolicitudDao.getNumeroSolicitudAinsertar(con, getNumeroSolicitudAInsertarStr);
	}
	
	/**
	 * M�todo que actualiza el indicativo pyp de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param pyp
	 * @return
	 */
	public int actualizarIndicativoPYP(Connection con,String numeroSolicitud,boolean pyp)
	{
		return SqlBaseSolicitudDao.actualizarIndicativoPYP(con,numeroSolicitud,pyp);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int getCodigoTipoSolicitud(Connection con, String numeroSolicitud) throws BDException
	{
		return SqlBaseSolicitudDao.getCodigoTipoSolicitud(con, numeroSolicitud);
	}
	

	/**
	 * 
	 */
	public int insertarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dto) throws BDException 
	{
		int codigo=ConstantesBD.codigoNuncaValido;
		int secuencia=ConstantesBD.codigoNuncaValido;
		String cadena="select seq_sol_sub_cuentas.nextval from dual ";
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try	{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				secuencia=rs.getInt(1);
			
			codigo = SqlBaseSolicitudDao.insertarSolicitudSubCuenta(con,dto,secuencia);
		}
		catch(SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally {
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close PreparedStatement", se);
			}
		}
		return codigo;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoCentroCostoSolicitado(Connection con, String numeroSolicitud)
	{
		return SqlBaseSolicitudDao.obtenerCodigoCentroCostoSolicitado(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoSolicitudesSubCuenta
	 * @return
	 */
	public boolean eliminarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta)
	{
		return SqlBaseSolicitudDao.eliminarSolicitudSubCuenta(con, dtoSolicitudesSubCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoCentroCostoSolicitante(Connection con, String numeroSolicitud) throws BDException
	{
		return SqlBaseSolicitudDao.obtenerCodigoCentroCostoSolicitante(con, numeroSolicitud);
	}	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap obteneInfoSolicitudInterfaz(Connection con, String numeroSolicitud)
	{
		return SqlBaseSolicitudDao.obteneInfoSolicitudInterfaz(con, numeroSolicitud);
	}
	
	/**
	 * Actualiza el valor de liquidar Asocios de la tabla Solicitudes
	 * @param Connection con
	 * @param int NumeroSolicitud
	 * @param String liqudarAsocios
	 * */
	public boolean cambiarLiquidacionAsociosSolicitud(Connection con, int numeroSolicitud, String liquidarAsocios)	
	{
		return SqlBaseSolicitudDao.cambiarLiquidacionAsociosSolicitud(con, numeroSolicitud, liquidarAsocios);
	}
	
	/**
	 * Metodo para consultar el Valor del Parametro General de Validar Registro Evoluciones
	 * @param con
	 * @return
	 */
	public String consultarParametroEvoluciones(Connection con) 
	{
		return SqlBaseSolicitudDao.consultarParametroEvoluciones(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public String obtenerFirmaDigitalMedico(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudDao.obtenerFirmaDigitalMedico(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerInfoMedicoSolicita(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudDao.obtenerInfoMedicoSolicita(con, numeroSolicitud);
	}
	
	/**
	 * metodo que actualiza el numero de orden de medicamentos con diferente dosificacion
	 * @param con
	 * @param vectorNumeroSolicitudes
	 * @return
	 */
	public boolean actualizarNumeroOrdenMedDiferenteDosificacion(Connection con, Vector<String> vectorNumeroSolicitudes)
	{
		return SqlBaseSolicitudDao.actualizarNumeroOrdenMedDiferenteDosificacion(con, vectorNumeroSolicitudes);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public String obtenerLoginMedicoSolicita(Connection con, int numeroSolicitud, int institucion)
	{
		return SqlBaseSolicitudDao.obtenerLoginMedicoSolicita(con, numeroSolicitud, institucion);
	}	
	
	/**
	 * M�todo para actualizar los centros de costo de medicamentos x despachar
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarCentrosCostoMedicamentosXDespachar(Connection con,HashMap<String, Object> campos)
	{
		return SqlBaseSolicitudDao.actualizarCentrosCostoMedicamentosXDespachar(con, campos);
	}
	
	/**
	 * Actualiza la informacion de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarInfoSolicitud(Connection con, HashMap parametros)
	{
		return SqlBaseSolicitudDao.actualizarInfoSolicitud(con, parametros);
	}

	@Override
	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(
			Connection con, int codigo, int codigoInstitucionInt) {
		

		return SqlBaseSolicitudDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigo, codigoInstitucionInt);
	}

	public HashMap<String, Object> obtenerEspecilidadSolicitada(Connection con,HashMap parametros) 
	{
		return SqlBaseSolicitudDao.obtenerEspecilidadSolicitada(con, parametros);
	}

	@Override
	public int actualizarEspecialidadProfResponde(Connection con,int numeroSolicitud, int codEspecialidad) throws SQLException {
		
		return SqlBaseSolicitudDao.actualizarEspecialidadProfResponde(con, numeroSolicitud, codEspecialidad);
	}

	@Override
	public ArrayList<DtoArticulos> buscarArticulosCargosDirectosArticulos(
			Connection conn, int numeroSolicitud) {
		return SqlBaseSolicitudDao.buscarArticulosCargosDirectosArticulos(conn, numeroSolicitud);
	}
	
	@Override
	public String obtenerInterpretacionSolicitud(int numeroSolicitud) {
		return SqlBaseSolicitudDao.obtenerInterpretacionSolicitud( numeroSolicitud);
	}
	

	@Override
	public int obtenerCodigoCita(Connection con, int numeroSolicitud) {
		return SqlBaseSolicitudDao.obtenerCodigoCita(con, numeroSolicitud);
	}

	@Override
	public int obtenerEspecilidadSolicitadaCita(Connection con,
			int numeroSolicitud) {
		return SqlBaseSolicitudDao.obtenerEspecilidadSolicitadaCita(con, numeroSolicitud);
	}

	/**
	 * @see com.princetonsa.dao.SolicitudDao#esConsultaExterna(java.sql.Connection, int)
	 */
	public  Boolean esConsultaExterna(Connection con,int numeroSolicitud) throws Exception{
		return SqlBaseSolicitudDao.esConsultaExterna(con, numeroSolicitud);
	}
}