/*
 * @(#)OracleUtilidadValidacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosString;
import util.RespuestaValidacion;
import util.RespuestaValidacionTratante;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UtilidadValidacionDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadValidacionDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Esta clase implementa el contrato estipulado en <code>UtilidadValidacionDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>UtilidadValidacion</code>
 *
 *	@version 1.0, Mar 24, 2003
 */
public class OracleUtilidadValidacionDao  implements UtilidadValidacionDao
{
	/**
	 * Para hacer logs de esta clase.
	 */
	private Logger logger = Logger.getLogger(OracleUtilidadValidacionDao.class);

	/**
	 * Implementación del método que permite obtener los permisos de acceso
	 * a una solicitud en una BD Hsqldb
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerPermisosAcceso_validacionAccesoSolicitud (Connection , int , String ) throws SQLException
	 */
	public int[] obtenerPermisosAcceso_validacionAccesoSolicitud (Connection con, int numeroSolicitud, String tipoSolicitud) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.obtenerPermisosAcceso_validacionAccesoSolicitud (con, numeroSolicitud, tipoSolicitud);
	}

	/**
	 * Implementación del método que permite averiguar el número de
	 * valoraciones que hay en una cuenta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#numeroSolicitudesValoracionCuenta (Connection , int ) throws SQLException
	 */
	public int numeroSolicitudesValoracionCuenta (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.numeroSolicitudesValoracionCuenta (con, idCuenta) ;
	}

	/**
	 * Implementación del método que permite averiguar la fecha y
	 * hora de entrada a observación de un paciente con admisión
	 * de urgencias en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerFechayHoraObservacionAdmisionUrgencias (Connection , int , int ) throws SQLException
	 */
	public String[] obtenerFechayHoraObservacionAdmisionUrgencias (Connection con, int codigoAdmision, int anioAdmision) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.obtenerFechayHoraObservacionAdmisionUrgencias (con, codigoAdmision, anioAdmision) ;
	}

	/**
	 * Implementación del método que permite averiguar si existe una
	 * evolucion para una cuenta particular con fecha superior a la dada.
	 * esta debe presentarse en el formato de la BD
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEvolucionConFechaSuperior (Connection , int , String , String ) throws SQLException
	 */ 
	public boolean existeEvolucionConFechaSuperior (Connection con, int idCuenta, String fechaDada, String horaDada) throws SQLException
	{
		String existeEvolucionConFechaSuperiorStr="SELECT ev.codigo from evoluciones ev, solicitudes sol1 where ev.valoracion=sol1.numero_solicitud and sol1.cuenta=? and to_date(ev.fecha_evolucion||'','yyyy-mm-dd') > to_date('"+fechaDada+"','yyyy-mm-dd') UNION SELECT ev.codigo from evoluciones ev, solicitudes sol2 where ev.valoracion=sol2.numero_solicitud and sol2.cuenta=? and to_date(ev.fecha_evolucion||'','yyyy-mm-dd') = to_date('"+fechaDada+"','yyyy-mm-dd') and to_timestamp(ev.hora_evolucion||'','HH24:MI') >  to_timestamp('"+horaDada+"','HH24:MI')";
		return SqlBaseUtilidadValidacionDao.existeEvolucionConFechaSuperior (con, idCuenta, existeEvolucionConFechaSuperiorStr) ;
	}

	/**
	 * Implementación del método que busca la fecha y hora máxima
	 * de evolución para una cuenta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerMaximaFechaYHoraEvolucion (Connection , int ) throws SQLException
	 */
	public String[] obtenerMaximaFechaYHoraEvolucion (Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.obtenerMaximaFechaYHoraEvolucion (con, idCuenta);
	}

	/**
	 * Implementación del método que busca la fecha y hora máxima
	 * de valoración para una cuenta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerMaximaFechaYHoraValoracion (Connection , int ) throws SQLException
	 */
	public String[] obtenerMaximaFechaYHoraValoracion (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.obtenerMaximaFechaYHoraValoracion (con, idCuenta);
	}

	/**
	 * Implementación del método que revisa si una cuenta tiene
	 * valoraciones para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneValoraciones (Connection , int ) throws SQLException
	 */
	public boolean tieneValoraciones (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneValoraciones (con, idCuenta);
	}

	/**
	 * Implementación del método que busca la fecha y hora de la
	 * primera valoración para una cuenta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerFechaYHoraPrimeraValoracion (Connection , int ) throws SQLException
	 */
	public String[] obtenerFechaYHoraPrimeraValoracion (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.obtenerFechaYHoraPrimeraValoracion (con, idCuenta) ;
	}

	/**
	 * Implementación del método que encuentra el número de solicitud
	 * primera valoración para una cuenta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerNumeroSolicitudPrimeraValoracion (Connection , int ) throws SQLException
	 */
	public int obtenerNumeroSolicitudPrimeraValoracion (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.obtenerNumeroSolicitudPrimeraValoracion (con, idCuenta) ;
	}

	/**
	 * Retorna si existe una solicitud de valoración de hospitalización o
	 * urgencias para la cuenta dada
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeSolicitudValoracionCuenta(Connection , int , int ) throws SQLException
	 */
	public HashMap<String,Object> existeSolicitudValoracionCuenta(Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeSolicitudValoracionCuenta(con, idCuenta);
	}

	/**
	 * Método que implementa la recuperación del centro
	 * de costo tratante siempre y cuando no sea de urgencias
	 * en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoCentroCostoTratante_noUrgencias (Connection , int ) throws SQLException
	 */
	public int getCodigoCentroCostoTratante_noUrgencias (Connection con, int idCuenta, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.getCodigoCentroCostoTratante_noUrgencias (con, idCuenta, institucion);
	}
	
	/**
	 * Método que verifica si un medico es tratante:
	 * Se consulta si el area de la cuenta del paciente hace 
	 * parte de alguno de los centros de costos del usuario del médico
	 * @param con
	 * @param login
	 * @param idCuenta
	 * @return
	 */
	public boolean esMedicoTratante(Connection con,String login,int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.esMedicoTratante(con,login,idCuenta);
	}

	/**
	 * Método que implementa la revisión de evoluciones suficientes
	 * con orden de salida para dar orden de salida en una BD
	 * Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existenEvolucionesSuficientesOrdenSalida (Connection , int , int , int , int , int ) throws SQLException
	 */
	public RespuestaValidacion existenEvolucionesSuficientesOrdenSalida (Connection con, int idCuenta, int codigoCentroCostoTratante, int diaEvolucion, int mesEvolucion, int anioEvolucion, boolean esUrgencias, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existenEvolucionesSuficientesOrdenSalida (con, idCuenta, codigoCentroCostoTratante, diaEvolucion, mesEvolucion, anioEvolucion, esUrgencias, institucion);
	}

	/**
	 * Implementación del método que revisa si hay una evolución
	 * del día de hoy en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEvolucionHoy (Connection , int , int ) throws SQLException
	 */
	public boolean existeEvolucionHoy (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeEvolucionHoy (con, idCuenta) ;
	}

	/**
	 * Implementación del método que revisa el tipo de evolucion
	 * en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#revisarTipoEvolucion (Connection , int ) throws SQLException
	 */
	public RespuestaValidacion revisarTipoEvolucion (Connection con, int numeroEvolucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.revisarTipoEvolucion (con, numeroEvolucion) ;
	}

	/**
	 * Implementación del método que revisa la cuenta a la
	 * que pertenece una evolución en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeCuentaParaEvolucion (Connection , int ) throws SQLException
	 */
	public RespuestaValidacion existeCuentaParaEvolucion (Connection con, int codigoEvolucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeCuentaParaEvolucion (con, codigoEvolucion) ;
	}


	/**
	 * Implementación del método que revisa si un centro de costo
	 * particular puede modificar una evolución y en que medida en
	 * una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionModificarEvolucion (Connection con, int , int , int ) throws SQLException
	 */
	public RespuestaValidacionTratante validacionModificarEvolucion (Connection con, int codigoEvolucion, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionModificarEvolucion (con, codigoEvolucion, idCuenta, codigoCentroCosto, institucion) ;
	}

	/**
	 * Método que dice si una evolución pertenece a una cuenta dada
	 * en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#evolucionPerteneceACuenta (Connection , int , int ) throws SQLException
	 */
	public boolean evolucionPerteneceACuenta (Connection con, int codigoEvolucion, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.evolucionPerteneceACuenta (con, codigoEvolucion, idCuenta);
	}

	/**
	 * Método que revisa si una cuenta esta cerrada en una
	 * BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#estaCuentaCerrada (Connection , int ) throws SQLException
	 */
	public boolean estaCuentaCerrada (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.estaCuentaCerrada (con, idCuenta) ;
	}

	/**
	 * Método que revisa si una cuenta es de urgencias en una
	 * BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esCuentaUrgencias (Connection , int )  throws SQLException
	 */
	public boolean esCuentaUrgencias (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.esCuentaUrgencias (con, idCuenta) ;
	}

	/**
	 * Método que revisa si una cuenta es de urgencias en una
	 * BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esCuentaUrgencias (Connection , int )  throws SQLException
	 */
	public boolean existeEvolucionDadaFecha (Connection con, int idCuenta, int dia, int mes, int anio, int idCuentaAsociada) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeEvolucionDadaFecha (con, idCuenta, dia, mes, anio, idCuentaAsociada);
	}

	/**
	 * Método que implementa la revision de permisos para epicrisis
	 * en su sección de notas y definición para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#permisosEpicrisisNotaYDefinicion (Connection , int , PersonaBasica, UsuarioBasico, boolean ) throws SQLException
	 */
	public RespuestaValidacionTratante permisosEpicrisisNotaYDefinicion (Connection con, int idEpicrisis, PersonaBasica paciente, UsuarioBasico usuario, boolean esEpicrisisHospitalizacion, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.permisosEpicrisisNotaYDefinicion (con, idEpicrisis, paciente, usuario, esEpicrisisHospitalizacion, institucion);
	}

	/**
	 * Método que implementa la búsqueda lenta del centro de costo
	 * tratante para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoCentroCostoTratanteMetodoLento (Connection , int ) throws SQLException
	 */
	public int getCodigoCentroCostoTratanteMetodoLento (Connection con, int idCuenta, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.getCodigoCentroCostoTratanteMetodoLento (con, idCuenta, institucion) ;
	}
	
	/**
	 * Método que busca el código del centro de costo 
	 * para una solicitud determinada 
	 * @param con
	 * @param numeroSolicitud (índice solicitides),
	 * @return cód
	 * @throws SQLException
	 */
	public int getCodigoCentroCostoSolicitanteXNumeroSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
	    return SqlBaseUtilidadValidacionDao.getCodigoCentroCostoSolicitanteXNumeroSolicitud(con, numeroSolicitud);
	}

	/**
	 * Método que implementa la revisión de número de evoluciones
	 * de acuerdo a criterios de epicrisis para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#faltanEvolucionesEpicrisis (Connection , int , boolean ) throws SQLException
	 */
	public RespuestaValidacion faltanEvolucionesEpicrisis (Connection con, int idIngreso, boolean esEpicrisisHospitalizacion, boolean esUrgencias, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.faltanEvolucionesEpicrisis (con, idIngreso, esEpicrisisHospitalizacion, esUrgencias, institucion);
	}

	/**
	 * Método que revisa si  una cuenta está en cama de observación
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#estaEnCamaObservacion(Connection , int ) throws SQLException
	 */
	public boolean estaEnCamaObservacion(Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.estaEnCamaObservacion(con, idCuenta) ;
	}

	/**
	 * Método que revisa si  una cuenta tiene una valoración con conducta
	 * a seguir cama de observación para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneValoracionConConductaCamaObservacion (Connection , int ) throws SQLException
	 */
	public boolean tieneValoracionConConductaCamaObservacion (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneValoracionConConductaCamaObservacion (con, idCuenta) ;
	}

	/**
	 * Método que revisa si una cuenta tiene una valoración la
	 * cual tenga una de las conductas a seguir
	 * pasadas por parámetro
	 * @param con Conexión con la BD
	 * @param idCuenta id de la cuenta que se desea validar
	 * @param conductas Array int con los códigos de las conductas a validar
	 * @return true si hay valoraciones con alguna de las conductas dadas.
	 */
	public boolean validacionConductaASeguir (Connection con, int idCuenta, int[] conductas)
	{
		return SqlBaseUtilidadValidacionDao.validacionConductaASeguir(con, idCuenta, conductas);
	}

	/**
	 * Método que revisa si  se puede reversar un egreso de
	 * hospitalización para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoReversarEgresoHospitalizacion (Connection , int , int ) throws SQLException
	 */
	public RespuestaValidacionTratante puedoReversarEgresoHospitalizacion (Connection con, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.puedoReversarEgresoHospitalizacion (con, idCuenta, codigoCentroCosto, institucion);
	}

	/**
	 * Método que revisa si  para una cuenta dada se debe se
	 * debe mostar el motivo de reversión de egreso para una
	 * BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#deboMostrarMotivoReversionEgreso (Connection , int ) throws SQLException
	 */
	public RespuestaValidacion deboMostrarMotivoReversionEgreso (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.deboMostrarMotivoReversionEgreso (con, idCuenta);
	}

	/**
	 * Método implementa la revisión de si existe epicrisis dado su
	 * código para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEpicrisis (Connection , int , boolean ) throws SQLException
	 */
	public RespuestaValidacion existeEpicrisis (Connection con, int idEpicrisis, boolean esEpicrisisHospitalizacion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeEpicrisis (con, idEpicrisis, esEpicrisisHospitalizacion) ;
	}

	/**
	 * Método implementa la revisión de si existe un paciente
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existePaciente (Connection , String , String ) throws SQLException
	 */
	public boolean existePaciente (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existePaciente (con, codigoPaciente) ;
	}

	/**
	 * Método implementa la revisión de si existe un paciente
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existePaciente (Connection , String , String ) throws SQLException
	 */
	public boolean existePaciente (Connection con, String tipoId, String numeroId) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existePaciente (con, tipoId, numeroId) ;
	}

	/**
	 * Método implementa la revisión del ingreso de cuentas
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarCuenta (Connection ,  String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionIngresarCuenta (Connection con,  String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionIngresarCuenta (con,  tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Método implementa la revisión de la creación de ingreso
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarIngreso (Connection , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionIngresarIngreso (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionIngresarIngreso (con, tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Método implementa la validación del ingreso de una persona
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarPersona (Connection , String , String , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionIngresarPersona (Connection con, String tipoId, String numeroId, String codBarrio, String rolPersona) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionIngresarPersona (con, tipoId, numeroId, codBarrio,  rolPersona) ;
	}

	/**
	 * Método implementa la validación de permisos de acceso
	 * a un paciente en una institución para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionPermisosInstitucionPaciente (Connection , int , String ) throws SQLException
	 */
	public RespuestaValidacion validacionPermisosInstitucionPaciente (Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionPermisosInstitucionPaciente (con, codigoPaciente, codigoInstitucion) ;
	}

	/**
	 * Método implementa la validación de permisos de acceso
	 * a un paciente en una institución a segundo nivel para una
	 * BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionPermisosInstitucionPaciente2 (Connection , int , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionPermisosInstitucionPaciente2 (Connection con, int codigoPaciente, String codigoInstitucionBrinda, String codigoInstitucionRecibe) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionPermisosInstitucionPaciente2 (con, codigoPaciente, codigoInstitucionBrinda, codigoInstitucionRecibe) ;
	}

	/**
	 * Método implementa la búsqueda del código de un
	 * paciente dada su identificación para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoPersona (Connection , String , String ) throws SQLException
	 */
	public int getCodigoPersona (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion ) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.getCodigoPersona (con, codigoTipoIdentificacion, numeroIdentificacion ) ;
	}
	
	/**
	 * Método implementa la búsqueda del código de un
	 * paciente dada su identificación para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#buscarCodigoPaciente (Connection , String , String ) throws SQLException
	 */
	public int buscarCodigoPaciente (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion ) 
	{
		return SqlBaseUtilidadValidacionDao.buscarCodigoPaciente (con, codigoTipoIdentificacion, numeroIdentificacion ) ;
	}

	/**
	 * Método que implementa la revisión de permisos para ver
	 * un paciente dada una institución para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoImprimirPaciente(Connection , int , String ) throws SQLException
	 */
	public boolean puedoImprimirPaciente(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.puedoImprimirPaciente(con, codigoPaciente, codigoInstitucion) ;
	}

	/**
	 * Método que implementa las validaciones para ingresar
	 * una admisión hospitalaria para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarAdmisionHospitalaria (Connection ,  int , int , String ) throws SQLException
	 */
	public RespuestaValidacion validacionIngresarAdmisionHospitalaria (Connection con,  int codigoPaciente, int idCuenta, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionIngresarAdmisionHospitalaria (con,  codigoPaciente, idCuenta, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la revisión de permisos de ingreso
	 * de evolución para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validarIngresarEvolucion (Connection , PersonaBasica, UsuarioBasico ) throws SQLException
	 */
	public RespuestaValidacionTratante validarIngresarEvolucion (Connection con, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validarIngresarEvolucion (con,  paciente, usuario) ;
	}

	/**
	 * Método que implementa la revisión de si una cuenta tiene
	 * egreso (solo tiene en cuenta egreso normales y automáticos)
	 * para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneEgreso (Connection , int ) throws SQLException
	 */
	public boolean tieneEgreso (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneEgreso (con, idCuenta) ;
	}

	/**
	 * Método que implementa la revisión de si un egreso puede
	 * ser finalizado para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoFinalizarEgreso (Connection , int ) throws SQLException
	 */
	public RespuestaValidacion puedoFinalizarEgreso (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.puedoFinalizarEgreso (con, idCuenta) ;
	}

	/**
	 * Método que implementa la revisión de si un egreso está
	 * completo para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEgresoCompleto (Connection , int ) throws SQLException
	 */
	public boolean existeEgresoCompleto (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeEgresoCompleto (con, idCuenta) ;
	}

	/**
	 * Método que implementa la revisión de si existe egreso un
	 * egreso automático para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEgresoAutomatico (Connection , int ) throws SQLException
	 */
	public RespuestaValidacion existeEgresoAutomatico (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeEgresoAutomatico (con, idCuenta) ;
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public boolean existeEgresoMedico(Connection con, int cuenta)
	{
		return SqlBaseUtilidadValidacionDao.existeEgresoMedico(con, cuenta);
	}
	
	/**
	 * Método que implementa las validaciones necesarias para saber
	 * si se puede ingresar un usuario para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarUsuario (Connection , String ,  String , String , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionIngresarUsuario (Connection con, String login,  String tipoBD, String tipoId, String numeroId, String codBarrio) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionIngresarUsuario (con, login,  tipoId, numeroId,  codBarrio) ;
	}

	/**
	 * Método que implementa las validaciones necesarias para saber
	 * si se puede modificar un usuario para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionModificarUsuario (Connection , String , String , String , String , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionModificarUsuario (Connection con, String tipoBD, String tipoId, String numeroId, String tipoIdViejo, String numeroIdViejo, String codBarrio) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionModificarUsuario (con, tipoId, numeroId, tipoIdViejo, numeroIdViejo, codBarrio) ;
	}

	/**
	 * Método que implementa la revisión de si se puede modificar una
	 * persona no usuario para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionModificarPersonaNoUsuario (Connection ,  String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionModificarPersonaNoUsuario (Connection con,  String tipoBD, String codBarrio) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionModificarPersonaNoUsuario (con,  codBarrio) ;
	}

	/**
	 * Método que implementa la revisión de si se puede ingresar una
	 * una historia clínica para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarHistoriaClinica (Connection ,  String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionIngresarHistoriaClinica (Connection con,  String tipoBD, String tipoId, String numeroId) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionIngresarHistoriaClinica (con,  tipoId, numeroId) ;
	}

	/**
	 * Método que valida si se puede cambiar un documento de
	 * identificación para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCambiarDocumento (Connection , String , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionCambiarDocumento (Connection con, String tipoBD, String tipoIdNuevo, String numeroIdNuevo, String tipoIdViejo, String numeroIdViejo) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionCambiarDocumento (con, tipoIdNuevo, numeroIdNuevo, tipoIdViejo, numeroIdViejo) ;
	}

	/**
	 * Método que implementa la revisión de si existe un médico
	 * para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeMedico (Connection , String , String , String ) throws SQLException
	 */
	public boolean existeMedico (Connection con, String tipoBD, String tipoId, String numeroId) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeMedico (con, tipoId, numeroId) ;
	}

	/**
	 * Método que implementa la revisión de si existe un usuario
	 * para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeUsuario (Connection , String , String , String ) throws SQLException
	 */
	public boolean existeUsuario (Connection con, String tipoBD, String tipoId, String numeroId) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeUsuario (con, tipoId, numeroId);
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria dado su código, para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaAbiertaCodigo (Connection , String , String , String ) throws SQLException
	 */
	public boolean existeAdmisionHospitalariaAbiertaCodigo (Connection con, String tipoBD, String codigoAdmision, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeAdmisionHospitalariaAbiertaCodigo (con, codigoAdmision, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria dado su código, para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaCodigo (Connection , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion existeAdmisionHospitalariaCodigo (Connection con, String tipoBD, String codigoAdmision, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeAdmisionHospitalariaCodigo (con, codigoAdmision, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria abierta dado el código del paciente, para una BD
	 * Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaAbiertaPaciente (Connection , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion existeAdmisionHospitalariaAbiertaPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeAdmisionHospitalariaAbiertaPaciente (con, tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria dado el código del paciente, para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaPaciente (Connection , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion existeAdmisionHospitalariaPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeAdmisionHospitalariaPaciente (con, tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la revisión de si se puede insertar
	 * un usuario médico para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionInsertarUsuarioMedico (Connection , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionInsertarUsuarioMedico (Connection con, String tipoBD, String tipoId, String numeroId, String login, int validarUsuario) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionInsertarUsuarioMedico (con, tipoId, numeroId, login, validarUsuario) ;
	}

	/**
	 * Método que implementa la revisión de si se puede insertar un
	 * usuario paciente para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionInsertarUsuarioPaciente (Connection , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionInsertarUsuarioPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String login) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionInsertarUsuarioPaciente (con, tipoId, numeroId, login) ;
	}

	/**
	 * Método que implementa la revisión de permisos de acceso a
	 * pacientes por institución/médico para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionPermisosInstitucionMedico (Connection , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionPermisosInstitucionMedico (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionPermisosInstitucionMedico (con, tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la revisión de si se puede cambiar
	 * el consecutivo de historias clínicas para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCambioConsecutivoHistoriasClinicas (Connection , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionCambioConsecutivoHistoriasClinicas (Connection con, String tipoBD, String nuevoConsecutivoStr ) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionCambioConsecutivoHistoriasClinicas (con, nuevoConsecutivoStr ) ;
	}

	/**
	 * Método que implementa la validación de si se puede activar
	 * un médico para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionActivarMedico (Connection , String , String , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionActivarMedico (Connection con, String tipoBD, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionActivarMedico (con, tipoId, numeroId, codigoInstitucion) ;
	}

	/**
	 * Método que implementa la validación de si se puede cambiar
	 * el password de administrador para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCambiarPasswordAdministrador (Connection , String , String ) throws SQLException
	 */
	public RespuestaValidacion validacionCambiarPasswordAdministrador (Connection con, String login, String codigoInstitucion,String password) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionCambiarPasswordAdministrador (con, login, codigoInstitucion,password) ;
	}

	/**
	 * Método que implementa la validación de si se puede reversar
	 * un egreso por tiempo para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoReversarEgresoPorTiempo(Connection , int ) throws SQLException
	 */
	public ResultSetDecorator puedoReversarEgresoPorTiempo(Connection ac_con, int ai_idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.puedoReversarEgresoPorTiempo(ac_con, ai_idCuenta) ;
	}

	/**
	 * Método que implementa la revisión de si una cuenta tiene
	 * fecha de admisión para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneFechaAdmisionCuenta (Connection , int , int , int ) throws SQLException
	 */
	public RespuestaValidacion tieneFechaAdmisionCuenta (Connection con, int idCuenta, int idAdmision, int anioAdmision) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneFechaAdmisionCuenta (con, idCuenta, idAdmision, anioAdmision) ;
	}

	/**
	 * Método que implementa la revisión de si un paciente en
	 * urgencias ya tiene asignada cama, para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneCamaParaEgresoUrgencias (Connection , int , int ) throws SQLException
	 */
	public boolean tieneCamaParaEgresoUrgencias (Connection con, int idAdmision, int anioAdmision) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneCamaParaEgresoUrgencias (con, idAdmision, anioAdmision) ;
	}

	/**
	 * Método que implementa la validación de si se puede reversar
	 * un egreso de urgencias para una BD Oracle.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoReversarEgresoUrgencias(Connection , int , int ) throws SQLException
	 */
	public RespuestaValidacionTratante puedoReversarEgresoUrgencias(Connection ac_con, int ai_idCuenta, int ai_codigoCentroCosto) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.puedoReversarEgresoUrgencias(ac_con, ai_idCuenta, ai_codigoCentroCosto) ;
	}

	/**
	 * Método implementa la búsqueda del nombre de una
	 * persona para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerNombrePersona (Connection , int ) throws SQLException
	 */
	public String obtenerNombrePersona (Connection con, int codigoPersona) 
	{
		return SqlBaseUtilidadValidacionDao.obtenerNombrePersona (con, codigoPersona) ;
	}

	/**
	 * Método que implementa la revisión de la capacidad
	 * de crear una cuenta en asocio para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoCrearCuentaAsocio (Connection , int )
	 */
	public boolean puedoCrearCuentaAsocio (Connection con, int idIngreso) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.puedoCrearCuentaAsocio (con, idIngreso);
	}

	/**
	 * Método que revisa el estado de la cuenta (si está en facturación)
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCuentaFacturada(Connection , int , boolean ) throws SQLException
	 */
	public boolean validacionCuentaFacturada(Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionCuentaFacturada(con, idIngreso, esHospitalizacion) ;
	}

	/**
	 * Método que implementa la revisión de existencia de un centro
	 * de costo para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeCentroCostoValido (Connection ) throws SQLException
	 */
	public RespuestaValidacion existeCentroCostoValido (Connection con) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeCentroCostoValido (con) ;
	}

	/**
	 * Método que implementa la revisión de si un usuario es
	 * paciente para una institución dada en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esUsuarioPaciente (Connection , int , String ) throws SQLException
	 */
	public ResultSetDecorator esUsuarioPaciente (Connection con, int codigoUsuario, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.esUsuarioPaciente (con, codigoUsuario, codigoInstitucion) ;
	}

	/**
	 * Metodo para saber si un usuario esta activo en el sistema.
	 * @param con
	 * @param codigoPersona
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator esUsuarioActivo (Connection con, String login, int codigoInstitucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.esUsuarioActivo (con, login, codigoInstitucion);
	}
	
	
	/**
	 * Método que implementa la revisión de si una cuenta
	 * tiene semi-egreso para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneSemiEgreso (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator tieneSemiEgreso (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneSemiEgreso (con, idCuenta);
	}

	/**
	 * Método que implementa la revisión de si un centro de costo
	 * es adjunto en cuenta para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esAdjuntoCuenta (Connection , int , String ) throws SQLException
	 */
	public boolean esAdjuntoCuenta (Connection con, int idCuenta, String loginUsuario) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.esAdjuntoCuenta (con, idCuenta, loginUsuario) ;
	}

	/**
	 * Método que implementa la revisión de si para cuenta existen
	 * solicitudes de transferencia de manejo para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeSolicitudTransferenciaManejoPrevia(Connection , int ) throws SQLException
	 */
	public boolean existeSolicitudTransferenciaManejoPrevia(Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeSolicitudTransferenciaManejoPrevia(con, idCuenta) ;
	}

	/**
	 * Método que implementa la revisión de si una cuenta tiene como
	 * vía de ingreso consulta externa para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esViaIngresoConsultaExterna (Connection , int ) throws SQLException
	 */
	public boolean esViaIngresoConsultaExterna (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.esViaIngresoConsultaExterna (con, idCuenta) ;
	}

	/**
	 * Método que revisa si hay solicitudes incompletas para
	 * un adjunto en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#haySolicitudesIncompletasAdjunto (Connection , int , int ) throws SQLException
	 */
	public ResultadoBoolean haySolicitudesIncompletasAdjunto (Connection con, int idCuenta, int codigoCentroCosto, int codigoCuentaAsociada, int institucion) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.haySolicitudesIncompletasAdjunto (con, idCuenta, codigoCentroCosto, codigoCuentaAsociada, institucion);
	}

	/**
	 * Método que revisa si hay solicitudes incompletas para
	 * una cuenta en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#haySolicitudesIncompletasEnCuenta (Connection , int ) throws SQLException
	 */
	public ResultadoBoolean haySolicitudesIncompletasEnCuenta (Connection con, int codigoIngreso, int institucion, boolean esEvolucion, boolean validarMedicamentos) throws SQLException, BDException
	{
		return SqlBaseUtilidadValidacionDao.haySolicitudesIncompletasEnCuenta (con, codigoIngreso, institucion, esEvolucion,validarMedicamentos) ;
	}

	/**
	 * Método que revisa si un centro de costo tiene una solicitud
	 * de cambio de manejo tratante para la cuenta dada en una BD
	 * Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (Connection con, int idCuenta, int codigoCentroCosto) throws SQLException
	 */
	public int existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (Connection con, int idCuenta, int codigoCentroCosto) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (con, idCuenta, codigoCentroCosto) ;
	}

	/**
	* Indica si un servicio es excepcion al convenio de la cuenta.
	* @param	ac_con		Conexión a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @param	ai_servicio	Código del servicio
	* @return	true si el servicio esta paramatrizado como una excepción al convenio de la cuenta.
	*			false de lo contrario
	* @throws	SQLException
	*/
	public boolean esExcepcionServicio(
		Connection	ac_con,
		int			ai_cuenta,
		int			ai_servicio
	)throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.esExcepcionServicio(ac_con, ai_cuenta, ai_servicio);
	}

	/**
	* Cierra el ingreso del paciente y deja la cuenta en estado excenta
	* @param	ac_con		Conexión a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @return	true Si la cuenta pudo ser cerrada de manera exitosa
	*			false de lo contrario
	* @throws	SQLException
	*/
	public boolean cerrarCuentaConsultaExterna(
		Connection	ac_con,
		int			ai_cuenta
	)throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.cerrarCuentaConsultaExterna(ac_con, ai_cuenta);
	}
	/**
	 * Implementación del método que revisa si existe un antecedente
	 * pedíatrico en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAntecedentePediatrico(Connection ,int )throws SQLException
	 */
	public  boolean existeAntecedentePediatrico(Connection con, int codigoPaciente)throws SQLException{
		return SqlBaseUtilidadValidacionDao.existeAntecedentePediatrico(con,codigoPaciente);
	}
	
	/**
	 * Implementación del método que realiza la validación del asocio de
	 * de cuentas en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionAsocioCuenta (Connection , int ) throws SQLException
	 */
	public ResultadoBoolean validacionAsocioCuenta (Connection con, int idCuenta, String viaIngreso, String tipoPaciente) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.validacionAsocioCuenta (con, idCuenta,  viaIngreso,  tipoPaciente);
	}
	/**
	 * Implementación del método que busca el diagnostico a poner
	 * en la valoración de hospitalización después de un asocio en 
	 * una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (Connection , int ) throws SQLException
	 */
	public String[] obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (Connection con, int idCuentaAsociada,String viaIngresoCuentaVieja) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (con, idCuentaAsociada,viaIngresoCuentaVieja) ;
	}

	/**
	 * Implementación del método que busca el código del centro de costo
	 * de un médico en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoCentroCostoDadoMedico (Connection , int ) throw SQLException
	 */
	public int getCodigoCentroCostoDadoMedico (Connection con, String loginMedico) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.getCodigoCentroCostoDadoMedico (con, loginMedico) ;
	}

	public boolean existeManejoConjunto(Connection con, int cuenta, int centroCosto)
	{
		return SqlBaseUtilidadValidacionDao.existeManejoConjunto(con, cuenta, centroCosto);
	}

	/**
	 * Permite verificar que la cuenta tiene manejo conjunto activo de solicitudes de interconsulta
	 * @param cuenta
	 * @return
	 */
	public boolean existeManejoConjuntoActivoSolInterconsulta(Connection con, int cuenta, int centroCosto)
	{
		return SqlBaseUtilidadValidacionDao.existeManejoConjuntoActivoSolInterconsulta(con, cuenta, centroCosto);
	}
	
	/**
	 * Método que permite encontrar resultados de agenda que se 
	 * encuentren en estado activo y con la unidad de consulta que 
	 * llega de parámetro 
	 * @param con
	 * @param unidadConsulta
	 * @param centroAtencion
	 * @return
	 */
	public boolean existeValoresCancelarAgenda(	Connection con, int unidadConsulta,
																					String fechaInicial, String fechaFinal, 
																					String horaInicial, String horaFinal,
																					int consultorio, int dia, int codigoMedico, int centroAtencion,
																					String centrosAtencion, String unidadesAgenda)
	{
		return SqlBaseUtilidadValidacionDao.existeValoresCancelarAgenda(con,unidadConsulta, fechaInicial,fechaFinal, horaInicial,horaFinal, consultorio, dia, codigoMedico, centroAtencion, centrosAtencion, unidadesAgenda);
	}

	/**
	 * Implementación del método que permite revisar si existe
	 * una valoración pediatrica hospitalaria para una cuenta dada
	 * en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneValoracionPediatricaHospitalariaConsultarIngreso (Connection , int ) throws SQLException
	 */
	public int tieneValoracionPediatricaHospitalariaConsultarIngreso (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.tieneValoracionPediatricaHospitalariaConsultarIngreso (con, idCuenta) ;
	}
	
	/**
	 * método que permite activar o no la modificación 
	 * del número de identificación de terceros,
	 * únicamente en los casos en los cuales este tercero 
	 * no ha sido utilizado en empresas.
	 * (a futuro se tiene que validar que no se utilice en ningún lado)
	 * @param con, Conexión
	 * @param codigo, Código del tercero
	 * @return
	 */
	public boolean hanUtilizadoTercero (Connection con, int codigo)
	{
		return SqlBaseUtilidadValidacionDao.hanUtilizadoTercero(con,codigo);
	}

	/**
	 * Implementación del método que permite buscar el código de la
	 * valoración inicial para una cuenta dada en una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#buscarCodigoValoracionInicial (Connection , int ) throws SQLException
	 */
	public int buscarCodigoValoracionInicial (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseUtilidadValidacionDao.buscarCodigoValoracionInicial (con, idCuenta, DaoFactory.ORACLE) ;
	}
	
	/**
	 * Adición sebastián
	 * Método para conocer el código de la solicitud de una valoración inicial para una
	 * cuenta especifica, retorna 0 si no existe la solicitud 
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 */
	public int buscarCodigoSolicitudValoracionInicial (Connection con, int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.buscarCodigoSolicitudValoracionInicial(con,idCuenta);
	}
	
	/**
	 * Implementación del método que dado el código de un 
	 * centro de costo devuelve su nombre, para una BD 
	 * Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getNombreCentroCosto (Connection , int ) throws SQLException
	 */
	public String getNombreCentroCosto (Connection con, int codigoCentroCostoBuscado) throws SQLException
	{
	  return SqlBaseUtilidadValidacionDao.getNombreCentroCosto (con, codigoCentroCostoBuscado) ;
	}

	/**
	 * Implementación del método que dado el código de una
	 * cuenta devuelve la fecha de admisión de la misma,
	 * si esta existe, si no tiene admisión lanza una
	 * excepción para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getFechaHoraAdmision (Connection , int ) throws SQLException
	 */
	public String getFechaAdmision (Connection con, int idCuenta) throws SQLException
	{
	    return SqlBaseUtilidadValidacionDao.getFechaAdmision (con, idCuenta) ;
	}
	
	/**
	 * Implementación del método que dado el código de una
	 * cuenta devuelve la hora de admisión de la misma,
	 * si esta existe, si no tiene admisión lanza una
	 * excepción para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getFechaHoraAdmision (Connection , int ) throws SQLException
	 */
	public String getHoraAdmision (Connection con, int idCuenta) throws SQLException
	{
		String getHoraAdmisionStr="SELECT hora_admision as horaAdmision from admisiones_urgencias where cuenta = ? UNION ALL SELECT hora_admision as horaAdmision from admisiones_hospi where cuenta =?";
	    return SqlBaseUtilidadValidacionDao.getHoraAdmision (con, idCuenta, getHoraAdmisionStr) ;
	}
	
	/**
	 * Implementación del método que revisa si se debe
	 * mostrar el mensaje de la cancelación del tratante
	 * antes de llenar una evolución para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (Connection , int , int ) throws SQLException
	 */
	public boolean deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (Connection con, int idCuenta, int centroCostoMedico) throws SQLException
	{
		String consulta="SELECT count(1) as numResultados from solicitudes sol INNER JOIN sol_cambio_tratante sct  ON(sct.solicitud=sol.numero_solicitud) INNER JOIN cancelacion_tratantes cant ON (sct.codigo=cant.codigo) where sct.activa =" + ValoresPorDefecto.getValorFalseParaConsultas()+" and sct.cuenta=? and sol.centro_costo_solicitado=? and (select count(1) from evoluciones evol where evol.valoracion=sol.numero_solicitud and (evol.fecha_grabacion>cant.fecha or (evol.fecha_grabacion=cant.fecha and (evol.hora_grabacion)>=cant.hora) )  )=0";
		return SqlBaseUtilidadValidacionDao.deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (con, idCuenta, centroCostoMedico, consulta);
	}
	
	/**
	 * Verificar si el paciente (codigoPaciente) tiene citas atendidas por el médico (codigoMedico)
	 * @param con
	 * @param codigoMedico
	 * @param codigoPaciente
	 * @return
	 */
	public boolean tieneCitaRespondida(Connection con, int codigoMedico, int codigoPaciente)
	{
		return SqlBaseUtilidadValidacionDao.tieneCitaRespondida(con, codigoMedico, codigoPaciente);
	}

	/**
	 * Metodo para verificar la existencia de cuenta
	 * asociada
	 * @param con
	 * @param ingreso
	 * @return codigo de la cuenta asociada si existe, 0 de lo contrario
	 */
	public int tieneCuentaAsociada(Connection con, int ingreso)
	{
		if(ingreso > 0){
			PreparedStatementDecorator cuentaAsociada=null;
			ResultSetDecorator resultado = null;
			try
			{
				cuentaAsociada= new PreparedStatementDecorator(con.prepareStatement(SqlBaseUtilidadValidacionDao.getConsultaCuentasAsociadas().toString()));
				cuentaAsociada.setInt(1, Integer.parseInt(ValoresPorDefecto.getValorTrueParaConsultas()));
				cuentaAsociada.setInt(2, ingreso);
				resultado=new ResultSetDecorator(cuentaAsociada.executeQuery());
				while(resultado.next())
				{
					return resultado.getInt("cuentainicial");
				}
				return 0;
			}
			catch (SQLException e)
			{
				logger.error("Error consultando la existencia de la cuenta Asociada "+e);
				return 0;
			}
			finally{
				UtilidadBD.cerrarObjetosPersistencia(cuentaAsociada, resultado, null);
			}
		} else {
			return 0;
		}
	}
	
	/**
	 * Implementación del método que dado el código de una 
	 * cuenta o subcuenta devuelve su estado, para una BD 
	 * Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#cuentaOSubcuentaTieneEstadoDefinido (Connection , int , int , boolean ) throws SQLException
	 */
	public boolean cuentaOSubcuentaTieneEstadoDefinido (Connection con, int idCuenta, int estadoAVerificar, boolean esSubcuenta) throws SQLException
	{
	    return SqlBaseUtilidadValidacionDao.cuentaOSubcuentaTieneEstadoDefinido (con, idCuenta, estadoAVerificar, esSubcuenta) ;
	}
	
	/**
	 * Implementación del método que dado el código de una 
	 * cuenta o subcuenta devuelve el nombre de su estado, 
	 * para una BD Oracle
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getNombreEstadoCuentaOSubcuenta(Connection , int, boolean )throws SQLException
	 */
	public String getNombreEstadoCuentaOSubcuenta(Connection con, int id, boolean esSubCuenta)throws SQLException
	{
	    return SqlBaseUtilidadValidacionDao.getNombreEstadoCuentaOSubcuenta(con, id, esSubCuenta);
	}
	
	/**
	 * Metodo que me indica si una cuenta tiene un determinado estado
	 * @param con, Conexion
	 * @param id, id de la cuenta
	 * @param estado, Estado que se validara.
	 * @return true
	 */
	public boolean igualEstadoCuenta(Connection con,int id,int estado)
	{
		return SqlBaseUtilidadValidacionDao.igualEstadoCuenta(con,id,estado);
	}
	
	/**
	 * Método que indica si la solicitud pasada por parámetros fue originada
	 * en la cuenta pasada por parámetros
	 * La comparación se hace por tiempo
	 * @param con
	 * @param numeroSolicitud
	 * @param idCuenta
	 * @return true si la solicitud pertenece a la cuenta, false de lo contrario
	 */
	public boolean perteneceSolicitudACuentaComparacionXTiempo(Connection con, int numeroSolicitud, int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.perteneceSolicitudACuentaComparacionXTiempo(con, numeroSolicitud, idCuenta);
	}
	
	/**
	 * Método para verificar si el paciente tiene o no evoluciones
	 * @param con Conexión con la BD
	 * @param idCuenta Codigo de la cuenta
	 * @return true si el paciente tiene evoluciones, false de lo contrario
	 */
	public boolean tieneEvoluciones(Connection con, int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.tieneEvoluciones(con, idCuenta);
	}

	/**
	 * Método para validar que la cuenta dada no se encuentre en proceso de facturación
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente
	 * @param idSesionOPCIONAL, si viene cargado eso quiere decir que no vamos a filtrar por ese numero de sesion
	 * @return true si la cuenta está en proceso de facturación por otro usuario, false de lo contrario
	 */
	public boolean estaEnProcesofacturacion(Connection con, int codigoPaciente, String idSesionOPCIONAL)
	{
		return SqlBaseUtilidadValidacionDao.estaEnProcesofacturacion(con, codigoPaciente, idSesionOPCIONAL);
	}


	/**adición sebastián
	 * 
	 * Este metodo valida que el login de usuario que va a ser
	 * ingresado en el sistema no exista previamente
	 * 
	 * @param con
	 * @param login
	 * @return
	 * @throws SQLException
	 */
	public RespuestaValidacion validacionIngresarUsuario(Connection con, String login) throws SQLException {
		return SqlBaseUtilidadValidacionDao.validacionIngresarUsuario(con,login);
	}
	
	/**
	 * Método que verifica si la tarifa del artículo ya existe
	 * @param con Conexión con la BD
	 * @param articulo Código del articulo
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public boolean existeTarifaParaArticulo(Connection con, int articulo, int esquemaTarifario)
	{
		return SqlBaseUtilidadValidacionDao.existeTarifaParaArticulo(con, articulo, esquemaTarifario);
	}
	
	/**
	 * Metodo para realizar la validacion de si exite en la base
	 * de datos un paciente con el mismo nombre y apellidos esta
	 * validacion se esta haciendo por institucion, en caso de 
	 * que se necesite que no se por institucion debe cambiarse
	 * el inner join de la tabla pacientes_instituciones por la 
	 * tabla pacientes;
	 * @param con
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @return
	 */
	public String validacionExistePacienteMismoNombre(Connection con, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido)
	{
		return SqlBaseUtilidadValidacionDao.validacionExistePacienteMismoNombre(con, primerNombre,segundoNombre,primerApellido,segundoApellido);
	}
	
	/**
	 * Metodo que retorna true si el articulo es un medicamento(independiente de que sea pos o no)
	 * @param conexion
	 * @param articulo
	 * @return boolean, respuesta true indica si es medicamento.
	 */
	public boolean esMedicamento(Connection con, int articulo)
	{
		String cadena="SELECT getesmedicamento(?) from dual ";
		return SqlBaseUtilidadValidacionDao.esMedicamento(con,articulo,cadena);
	}

	/**Metodo que retorna true si existe un tipo regimen y naturaleza
	 * @param con
	 * @param codigoRegimen
	 * @param codigoNaturaleza
	 * @return
	 */
	public boolean existeExcNatuRegimen(Connection con, String codigoRegimen, int codigoNaturaleza)
	{
		return SqlBaseUtilidadValidacionDao.existeExcNatuRegimen(con,codigoRegimen,codigoNaturaleza);
	}
	
	/**
	 * Metodo que retorna un boolean indicanto si el diagnostico con ese tipo CIE ya existe en el sitema.
	 * @param con
	 * @param codigo
	 * @param tipoCie
	 * @return
	 */
	public boolean diagnosticoExistente(Connection con, String codigo, int tipoCie)
	{
		return SqlBaseUtilidadValidacionDao.diagnosticoExistente(con,codigo,tipoCie);
	}
	
	/**
	 * Método para cargar el tipo de diagnóstico principal de la última evolución
	 * en caso de no existir evoluciones lo toma de la valoracioçón inicial
	 * @param con
	 * @param codigoCuenta
	 * @param revisarEnEvolucion
	 * Retorna el código del tipo de diagnóstico
	 */
	public int obtenerTipoDiagnosticoPrincipal(Connection con, int codigoCuenta, boolean revisarEnEvolucion)
	{
		return SqlBaseUtilidadValidacionDao.obtenerTipoDiagnosticoPrincipal(con, codigoCuenta, revisarEnEvolucion);
	}

	/**
	 * Método para consultar si un día es festivo o no
	 * @param con Conexión con la BD
	 * @param fecha Fecha que se desea consultar
	 * @param incluirDomingos Tener en cuenta cómo dias festivos los domingos 
	 * @return true si es día festivo
	 */
	public boolean esDiaFestivo(Connection con, String fecha, boolean incluirDomingos)
	{
		String consultarDiaFestivoStr;
		if(incluirDomingos)
		{
			consultarDiaFestivoStr="SELECT true AS esFestivo WHERE EXTRACT(DOW FROM DATE?)=0";
		}
		else
		{
			consultarDiaFestivoStr="SELECT true AS esFestivo FROM dias_festivos WHERE fecha=? OR (to_char(fecha, 'MM-dd') = to_char(DATE?, 'MM-dd') AND tipo="+ConstantesBD.codigoTipoDiaFestivoEstatico+")";
		}
		try
		{
			PreparedStatementDecorator consultaDiaFestivo= new PreparedStatementDecorator(con.prepareStatement(consultarDiaFestivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaDiaFestivo.setString(1, fecha);
			ResultSetDecorator resultado=new ResultSetDecorator(consultaDiaFestivo.executeQuery());
			if(resultado.next())
			{
				return resultado.getBoolean("esFestivo");
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando si la vecha "+fecha+" es día festivo : "+e);
			return false;
		}
	}
	
	/**
	 * Metodo que consulta una funcionalidad particular la cual no tenga una entrada en
	 * dependencias funcionalidades, entonces se filtra segun el rol y el codigo de la 
	 * funcionalidad
	 * @param con
	 * @param login
	 * @param codigoFuncionalidad
	 * @return devuelve el nombre de la funcionalidad con su path 
	 */
	public String funcionalidadADibujarNoEntradaDependencias(Connection con, String login, int codigoFuncionalidad)
	{
	    return SqlBaseUtilidadValidacionDao.funcionalidadADibujarNoEntradaDependencias(con, login, codigoFuncionalidad);
	}
	
	/**
	 * Metodo que determina si un usuario puede interpretar una solicitud de interconsulta o procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCostoUsuario
	 * @return
	 */
	public boolean puedoInterpretarSolicitudInterconsultaProcedimiento(Connection con, int numeroSolicitud, int codigoCentroCostoUsuario)
	{
	    return SqlBaseUtilidadValidacionDao.puedoInterpretarSolicitudInterconsultaProcedimiento(con, numeroSolicitud, codigoCentroCostoUsuario);
	}
	
	/**
	 *Metodo que retorna el manejo en que se encuentra una solicitud específica
	 *retornta true si la solicitud se encuentra en manejo conjunto
	 */
	public boolean isManejoConjunto(Connection con,int numeroSolicitud)
	{
	    return SqlBaseUtilidadValidacionDao.isManejoConjunto(con,numeroSolicitud);
	}
	/**
	 * Metodo que dado el centro de costo de un usuario, y el numero de solicitud me dice si la puedo modificar o no.
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCosto
	 * @return
	 **/
	public boolean medicoPuedeResponderSolicitud(Connection con, int numeroSolicitud, int codigoCentroCosto)
	{
		return SqlBaseUtilidadValidacionDao.medicoPuedeResponderSolicitud(con,numeroSolicitud,codigoCentroCosto);
	}
		
	/**
	 * Metodo que indica si una solicitud de interconsulta tiene manejo conjunto finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudManejoConjuntoFinalizado(Connection con, int numeroSolicitud)
	{
	    return SqlBaseUtilidadValidacionDao.esSolicitudManejoConjuntoFinalizado(con, numeroSolicitud);
	}
	
	/**
	 * Metodo que premite obtener el id de un ingreso dada la cuenta.
	 * @param con
	 * @param idCuenta
	 */
	public int obtenerIngreso(Connection con, int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.obtenerIngreso(con,idCuenta);
	}
	
	/**
	 * Método que verifica si la tarifa ISS del servicio ya existe
	 * @param con Conexión con la BD
	 * @param servicio Código del servicio
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public boolean existeTarifaISSParaServicio(Connection con, int servicio, int esquemaTarifario)
	{
		return SqlBaseUtilidadValidacionDao.existeTarifaISSParaServicio(con, servicio, esquemaTarifario);
	}
	
	/**
	 * Método que verifica si la tarifa SOAT del servicio ya existe
	 * @param con Conexión con la BD
	 * @param servicio Código del servicio
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public boolean existeTarifaSOATParaServicio(Connection con, int servicio, int esquemaTarifario)
	{
		return SqlBaseUtilidadValidacionDao.existeTarifaSOATParaServicio(con, servicio, esquemaTarifario);
	}
	

	/**
	 * @param con
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 */
	public boolean existeCierreSalodosIniciales(Connection con, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeCierreSalodosIniciales(con,institucion);
	}
	
	/**
	 * Método que verifica si el paciente tiene antecedentes gineco-obstetricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public boolean existeAntecedenteGinecoobstetrico(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadValidacionDao.existeAntecedenteGinecoobstetrico(con, codigoPersona);
	}
	
	/**
	 * Método que verifica si el paciente tiene información histórica de antecedentes gineco-obstétricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public boolean existeAntecedenteGinecoHisto(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadValidacionDao.existeAntecedenteGinecoHisto(con, codigoPersona);
	}
	

	/**
	 * Método para validar que la cuenta dada no se encuentre en proceso de distribucion
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente
	 * @param login del usuario que trata de acceder a la facturación
	 * @return true si la cuenta está en proceso de facturación por otro usuario, false de lo contrario
	 */
	public boolean estaEnProcesoDistribucion(Connection con, int codigoPaciente, String loginUsuario)
	{
		return SqlBaseUtilidadValidacionDao.estaEnProcesoDistribucion(con, codigoPaciente, loginUsuario);
	}
	
	/**
	 * Metodo que devuelve el numero de autorización dado el codigo de institucion y un like por 'autorizaci'
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	/*
	public int getCodNumeroAutorizacionAtributosSolicitud (Connection con, int codigoInstitucion)
	{
	    return SqlBaseUtilidadValidacionDao.getCodNumeroAutorizacionAtributosSolicitud(con, codigoInstitucion);
	}
	*/
	/**
	 * Metodo que devuelve la fecha de apertura de la cuenta
	 * @param con Connection
	 * @param idCuenta int
	 * @author jarloc
	 * @return fecha String
	 * @see com.princetonsa.dao.SqlBase.SqlBaseUtilidadValidacionDao#obtenerFechaAperturaCuenta (Connection,int ) 
	 */
	public String obtenerFechaAperturaCuenta (Connection con, int idCuenta)
	{
	    return SqlBaseUtilidadValidacionDao.obtenerFechaAperturaCuenta(con,idCuenta);
	}
	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @return
	 */
	public boolean existeCuentaCobro(Connection con, double cuentaCobro,int institucion)
	{
		 return SqlBaseUtilidadValidacionDao.existeCuentaCobro(con,cuentaCobro,institucion);
	}
	
	/**
	 * @param con
	 * @param mes
	 * @param anio
	 * @param Institucion
	 * @return
	 */
	public boolean existeCierreMensual(Connection con, int mes, int anio, int Institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeCierreMensual(con,mes,anio,Institucion);
	}
	
	/**
	 * @param con
	 * @param mes
	 * @param anio
	 * @param Institucion
	 * @return
	 */
	public boolean fechaMenorIgualAFechaCierreSalodsIniciales(Connection con, int mes, int anio, int Institucion)
	{
		return SqlBaseUtilidadValidacionDao.fechaMenorIgualAFechaCierreSalodsIniciales(con,mes,anio,Institucion);
	}
	
	/**
	 * Método que indica si la cama ha sido o no ocupada en el rango de fecha y hora igual o mayor al entregado 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoAxiomaCama
	 * @return
	 */
	public boolean esCamaOcupadaRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoAxiomaCama)
	{
	    return SqlBaseUtilidadValidacionDao.esCamaOcupadaRangoFechaHoraMayorIgualDado(con, fecha, hora, codigoAxiomaCama);
	}
	
	/**
	 * Método que me indica si el paciente tiene un traslado posterior a la fecha - hora de uno nuevo
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoPaciente
	 * @return
	 */
	public boolean pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoPaciente) 
	{
		return SqlBaseUtilidadValidacionDao.pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(con, fecha, hora, codigoPaciente);
	}
	
	/**
	 * Método que verifica si una cuenta es de hospitalización
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @param estado de la cuenta
	 * @param diferente (true/false) que indica si se valida con
	 * el estado igual a diferente
	 * @param institucion
	 * @return
	 */
	public boolean esCuentaHospitalizacion(Connection con,int idCuenta,int estado,boolean diferente,int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esCuentaHospitalizacion(con,idCuenta,estado,diferente,institucion);
	}
	
	/**
	 * Método que verifica si una cuenta es de hospitalización
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean esCuentaHospitalizacion(Connection con,String idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.esCuentaHospitalizacion(con, idCuenta);
	}
	
	/**
	 * Metodo que retorna true si una cuenta de cobro tiene ajustes en estado generado(pendientes)
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public boolean cuentaCobroAjustesPendientes(Connection con, double cuentaCobro, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.cuentaCobroAjustesPendientes(con,cuentaCobro,institucion);
	}
	
	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public boolean cuentaCobroConFactEnAjustesPendiente(Connection con, double cuentaCobro, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.cuentaCobroConFactEnAjustesPendiente(con,cuentaCobro,institucion);
	}
	
	/**
	 * metodo para obtener el estado de un ajuste especifico
	 * @param con Connection
	 * @param codigo int, código del ajuste
	 * @param institucion int, código de la institución
	 * @return String, con el estado del ajuste y la descripcion
	 * separados por un @
	 * @author jarloc
	 */
	public String obtenerEstadoAjusteCartera(Connection con, double codigo,int institucion)
	{
	    return SqlBaseUtilidadValidacionDao.obtenerEstadoAjusteCartera(con,codigo,institucion);   
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean esAjusteReversado(Connection con, double codigoAjuste)
	{
		return SqlBaseUtilidadValidacionDao.esAjusteReversado(con,codigoAjuste);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean existeDistribucionAjusteNivelFacturas(Connection con, double codigoAjuste)
	{
		return SqlBaseUtilidadValidacionDao.existeDistribucionAjusteNivelFacturas(con,codigoAjuste);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @param factura
	 * @return
	 */
	public boolean existeDistribucionAjusteNivelServicios(Connection con, double codigoAjuste, int factura)
	{
		return SqlBaseUtilidadValidacionDao.existeDistribucionAjusteNivelServicios(con,codigoAjuste,factura);
	}
	
	/**
	 * Método para verificar si un centro de costo es SubAlmacen
	 * @param con Conexión con la BD
	 * @param centroCosto codigo del centro de costo a verificar
	 * @return true si el centro de costo es subalmacen
	 */
	public boolean esCentroCostoSubalmacen(Connection con, int centroCosto)
	{
		return SqlBaseUtilidadValidacionDao.esCentroCostoSubalmacen(con, centroCosto);
	}

	/**
	 * Utilidad que verifica si una solicitud tiene artículos sin cantidad
	 * @param con Conexión con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene artículos sin cantidad, false de lo contrario
	 */
	public boolean esSolicitudSinCantidad(Connection con, int numeroSolicitud)
	{
		String cantidadesString="SELECT essolmedsincant(?) AS numResultados FROM dual";
		return SqlBaseUtilidadValidacionDao.esSolicitudSinCantidad(con, numeroSolicitud, cantidadesString);
	}
	
	/**
	 * Metodo que indica si ya existe la parametrizacion de tipos de asocios
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean esTiposAsociosParametrizados(Connection con, int institucion)
	{
	    return SqlBaseUtilidadValidacionDao.esTiposAsociosParametrizados(con, institucion);
	}
	

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean cajaUtilizadaEnCajerosCaja(Connection con, int consecutivoCaja)
	{
		 return SqlBaseUtilidadValidacionDao.cajaUtilizadaEnCajerosCaja(con,consecutivoCaja);
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean cajaUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja)
	{
		return SqlBaseUtilidadValidacionDao.cajaUtilizadaEnRecibosCaja(con,consecutivoCaja);
	}
	

	/**
	 * @param con
	 * @param consecutivoEntdadFinanciera
	 * @return
	 */
	public boolean entidadFinanceraUtilizadaEnTarjetasFinancieras(Connection con, int consecutivoEntdadFinanciera)
	{
		return SqlBaseUtilidadValidacionDao.entidadFinanceraUtilizadaEnTarjetasFinancieras(con,consecutivoEntdadFinanciera);
	}
	
	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param logginUsuario
	 * @return
	 */
	public boolean cajaCajeroUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja, String logginUsuario)
	{
		return SqlBaseUtilidadValidacionDao.cajaCajeroUtilizadaEnRecibosCaja(con,consecutivoCaja,logginUsuario);
	}
	
	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param logginUsuario
	 * @return
	 */
	public boolean cajaCajeroConTurno(Connection con, int consecutivoCaja, String logginUsuario)
	{
		return SqlBaseUtilidadValidacionDao.cajaCajeroConTurno(con,consecutivoCaja,logginUsuario);
	}
	
	/**
	 * @param con
	 * @param consecutivoTarjeta
	 * @return
	 */
	public boolean terjetaFinancieraUtilizadaEnMovimientosTarjetas(Connection con, int consecutivoTarjeta)
	{
		return SqlBaseUtilidadValidacionDao.terjetaFinancieraUtilizadaEnMovimientosTarjetas(con,consecutivoTarjeta);
	}
	
	/**
	 * @param con
	 * @param formaPago
	 * @return
	 */
	public boolean formaPagoUtilizadaEnRecibosCaja(Connection con, int formaPago)
	{
		return SqlBaseUtilidadValidacionDao.formaPagoUtilizadaEnRecibosCaja(con,formaPago);
	}
	
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public String existeReciboCaja(Connection con, String numeroReciboCaja, int institucion,int codigoCentroAtencion)
    {
        return SqlBaseUtilidadValidacionDao.existeReciboCaja(con,numeroReciboCaja,institucion,codigoCentroAtencion);
    }
    
    /**
     * Dice si esta parametrizado la cobertura de los accidentees de transito
     * @param con
     * @param institucion
     * @return
     */
    public boolean existeParametrizacionCobreturaAccTransito(Connection con, int institucion)
    {
    	return SqlBaseUtilidadValidacionDao.existeParametrizacionCobreturaAccTransito(con, institucion);
    }
    
    /**
     * Dice si esta parametrizado el salario minimo
     * @param con
     * @param institucion
     * @return
     */
    public boolean existeParametrizacionSalarioMinimo(Connection con)
    {
    	return SqlBaseUtilidadValidacionDao.existeParametrizacionSalarioMinimo(con);
    }
    
    /**
	 * Método que indica si una cuenta o subcuenta tiene requisistos paciente por cuenta o subcuenta(distibucion) pendientes
	 * de entregar.
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucion
	 * @param codigoSubcuentaOCuenta
	 * @return
	 */
	public boolean faltanRequisitosPacienteXCuenta( Connection con,  String codigoSubcuentaOCuenta)
	{
		return SqlBaseUtilidadValidacionDao.faltanRequisitosPacienteXCuenta(con,codigoSubcuentaOCuenta);
	}
	
	/**
	 * Método usado para verificar si un médico se encuentra inactivo
	 * 
	 * @param con
	 * @param codigoMedico
	 * @param institucion
	 * @return
	 */
	public boolean estaMedicoInactivo(Connection con,int codigoMedico,int institucion)
	{
		
		return SqlBaseUtilidadValidacionDao.estaMedicoInactivo(con,codigoMedico,institucion);
	}
	
	/**
	 * metodo que indica si un servicio tiene o no excepcion 
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public boolean esServicioConExcepcion(Connection con, String codigoConvenioStr, String codigoServicioStr)
	{
	    return SqlBaseUtilidadValidacionDao.esServicioConExcepcion(con, codigoConvenioStr, codigoServicioStr);
	}
	
	/**
	 * metodo que indica si un servicio es pos o no
	 * @param con
	 * @param codigoServicioStr
	 * @return
	 */
	public boolean esServicioPos(Connection con, String codigoServicioStr)
	{
	    return SqlBaseUtilidadValidacionDao.esServicioPos(con, codigoServicioStr);
	}
	
	/**
	 * metodo que indica si un servicio esta cubierto por el convenio
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public boolean esServicioCubiertoXConvenio(Connection con, String codigoConvenioStr, String codigoServicioStr)
	{
	    return SqlBaseUtilidadValidacionDao.esServicioCubiertoXConvenio(con, codigoConvenioStr, codigoServicioStr);
	}
	
	/**
	 * Metodo que verifica la existencia de los pagos parciales realizados por un paciente de una factura
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEstadoPago
	 * @param codigoAxiomaFactura
	 * @return
	 */
	public boolean existenPagosParcialesFacturaPaciente(Connection con,  int codigoInstitucion, int codigoEstadoPago, String codigoAxiomaFactura)
	{
		String consultaStr="select getPagosFacturaPaciente(?,?,?) AS valor from dual ";
	    return SqlBaseUtilidadValidacionDao.existenPagosParcialesFacturaPaciente(con, codigoInstitucion, codigoEstadoPago, codigoAxiomaFactura, consultaStr);
	}
	
	/**
	 * Método para saber si un médico es de una especialidad específica
	 * @param con
	 * @param codigoMedico
	 * @param codigoEspecialidad
	 * @return true si el médico es de la especialidad mandada como parámetro sino retorna false 
	 */
	public boolean esMedicoEspecialidad (Connection con, int codigoMedico, int codigoEspecialidad)
	{
		return SqlBaseUtilidadValidacionDao.esMedicoEspecialidad (con, codigoMedico, codigoEspecialidad);
	}
	
	/**
	 * Método que me indica si una peticion yçtiene asociada una solicitud 
	 * @param con Conexión con la base de Datos
	 * @param codigoPeticion Petición a evaluar
	 * @return int -1 En caso de error, 0 en caso de no existir orden asociada, o el número de la orden asociada a la petición
	 */
	public int[] estaPeticionAsociada(Connection con, int codigoPeticion)
	{
		return SqlBaseUtilidadValidacionDao.estaPeticionAsociada(con, codigoPeticion);
	}

     /**
     * metodo que indica si una hoja qx ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean esHojaQxAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        return SqlBaseUtilidadValidacionDao.esHojaQxAsociadaSolicitud(con, numeroSolicitud);
    }
    
    /**
     * metodo que indica si una hoja anestesia ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean esHojaAnestesiaAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        return SqlBaseUtilidadValidacionDao.esHojaAnestesiaAsociadaSolicitud(con, numeroSolicitud);
    }
    
    /**
     * metodo que indica si una nota recuperacion ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean esNotaRecuperacionAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        return SqlBaseUtilidadValidacionDao.esNotaRecuperacionAsociadaSolicitud(con, numeroSolicitud);
    }
    
    /**
     * metodo que indica si una nota general enfermeria ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean esNotaGeneralEnfAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        return SqlBaseUtilidadValidacionDao.esNotaGeneralEnfAsociadaSolicitud(con, numeroSolicitud);
    }
    
    /**
     * metodo que indica si un consumo materiales ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean esConsumoMaterialesAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        return SqlBaseUtilidadValidacionDao.esConsumoMaterialesAsociadaSolicitud(con, numeroSolicitud);
    }
    
	/**
	 * Cargar la hora de apertura de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public String obtenerHoraAperturaCuenta(Connection con, int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.obtenerHoraAperturaCuenta(con, idCuenta);
	}

    /**
     * Indica si la ocupacion de un profesional de la salud esta parametrizada como ocupaciones que realizan cirugias 
     * @param con
     * @param codigoOcupacion
     * @param codigoInstitucion
     * @return
     */
    public boolean esOcupacionQueRealizaCx(Connection con, int codigoOcupacion, int codigoInstitucion)
    {
        return SqlBaseUtilidadValidacionDao.esOcupacionQueRealizaCx(con, codigoOcupacion, codigoInstitucion);
    }
    
	/**
	 * Utilidad para verificar la existensia de la hoja de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeHojaAnestesia(Connection con, int numeroSolicitud)
	{
        return SqlBaseUtilidadValidacionDao.existeHojaAnestesia(con, numeroSolicitud);
	}
	
	/**
	 * Utilidad para verificar la existensia de la hoja obstétrica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public boolean existeHojaObstetrica(Connection con, int codigoPaciente)
	{
		return SqlBaseUtilidadValidacionDao.existeHojaObstetrica (con, codigoPaciente);
	}
	
	/**
	 * Método que verifica si la cuenta tiene solicitudes de un tipo
	 * especifico, con un estado medico o de facturacion específico
	 * @param con
	 * @param idCuenta
	 * @param tipo
	 * @param estadoMedico
	 * @param estadoFacturacion
	 * @return
	 */
	public boolean haySolicitudesEnCuenta(Connection con,int idCuenta,int tipo,int estadoMedico)
	{
		return SqlBaseUtilidadValidacionDao.haySolicitudesEnCuenta(con,idCuenta,tipo,estadoMedico);
		
	}
	
	/**
	 * Método implementado para verificar si una cirugía requiere justificacion
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio (cirugia)
	 * @param contrato actual de la cuenta que contiene la solicitud
	 * @return
	 */
	public boolean cirugiaRequiereJustificacion(Connection con,int numeroSolicitud,int servicio,int contrato)
	{
		return SqlBaseUtilidadValidacionDao.cirugiaRequiereJustificacion(con,numeroSolicitud,servicio,contrato);
	}

	/**
	 * Metodo para saber si una solcicitud de Procedimientos es multiple.
	 * @param con
	 * @param numeroSolicitud
	 * @return Retorna True Si la solicitud es multiple de lo contrario False. 
	 */
	public boolean esSolicitudMultiple(Connection con,int numeroSolicitud)
	{
		return SqlBaseUtilidadValidacionDao.esSolicitudMultiple(con,numeroSolicitud);
	}
	
	/**
	 * Utilidad para verificar la existencia de la hoja oftalmológica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public boolean existeHojaOftalmologica(Connection con, int codigoPaciente)
	{
		return SqlBaseUtilidadValidacionDao.existeHojaOftalmologica (con, codigoPaciente);
	}

	/**
	 * Revisar Validez del contrato por fechas
	 * @param con
	 * @param cuenta
	 * @return Nombre Convenio, fecha inicio y fecha fin del contrato, cadena con representacion booleana si es valido o no
	 * Ej:
	 * 	0 - Cafesalud EPS
	 * 	1 - 01/01/2000
	 *  2 - 31/12/2006
	 *  3 - true
	 */
	public String[] revisarValidezContrato(Connection con, int cuenta)
	{
		return SqlBaseUtilidadValidacionDao.revisarValidezContrato(con, cuenta);
	}
	
	/**
	 * Verifica si esciste un monto en la BD
	 * @param con
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @return
	 */
	public boolean existeMontoEnBD(Connection con, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha)
	{
		return SqlBaseUtilidadValidacionDao.existeMontoEnBD(con, convenio, viaIngreso, tipoAfiliado, estratoSocial, tipoMonto, valor, porcentaje, activo, fecha);
	}
	

	/**
	 * 
	 * @param con
	 * @param profesional
	 * @param fecha
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public boolean seCruzaAgendaMedicoFechaHora(Connection con, int profesional, String fecha, String horaInicial, String horaFinal)
	{
		return SqlBaseUtilidadValidacionDao.seCruzaAgendaMedicoFechaHora(con,profesional,fecha,horaInicial,horaFinal);
	}

	/**
	 * metodo que indica si se debe generar recibos de caja automaticos para
	 * una via de ingreso dada
	 * @param con
	 * @param codigoViaIngreso
	 * @return
	 */
	public boolean esReciboCajaAutomaticoViaIngresoDada(Connection con, int codigoViaIngreso)
	{
		return SqlBaseUtilidadValidacionDao.esReciboCajaAutomaticoViaIngresoDada(con, codigoViaIngreso);
	}
	
	/**
	 * 
	 * Método que consulta el código [pos 1] y la descripción [pos 2] del concepto tipo régimen
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param acronimoTipoRegimen
	 * @param tiposIngresoTesoreria
	 * @param esActivo
	 * @return
	 */
	public HashMap<Object, Object> getCodigoDescripcionConceptoTipoRegimen(Connection con, int codigoInstitucion, String acronimoTipoRegimen, Vector<Object> tiposIngresoTesoreria, boolean esActivo)
	{
		return SqlBaseUtilidadValidacionDao.getCodigoDescripcionConceptoTipoRegimen(con, codigoInstitucion, acronimoTipoRegimen, tiposIngresoTesoreria, esActivo);
	}
	
	/**
	 * Método para saber si existe un centro ed costo asociado a una via de ingreso 
	 * de una insitucion determinada
	 * @param con
	 * @param centroCosto
	 * @param via
	 * @return
	 */
	public boolean existeCentroCostoXViaIngreso(Connection con, int centroCosto, int viaIngreso, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeCentroCostoXViaIngreso(con, centroCosto, viaIngreso, institucion);
	}
	
	/**
	 * Método implementado para consultar el codigo de area y el
	 * nombre del área del paciente, el formato es:
	 * codigoArea + constantesBD.separadorSplit + nombreArea
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getAreaPaciente(Connection con,int idCuenta)
	{
		return SqlBaseUtilidadValidacionDao.getAreaPaciente(con,idCuenta);
	}
	
	/**
	 * Método que verifica si un paciente está muerto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean esPacienteMuerto(Connection con,int codigoPaciente)
	{
		return SqlBaseUtilidadValidacionDao.esPacienteMuerto(con,codigoPaciente);
	}
	
	/**
	 * Método implementado para actualizar al paciente como muerto
	 * en la base de datos
	 * @param con
	 * @param codigoPaciente
	 * @param esVivo
	 * @param fechaMuerte
	 * @param estado
	 * @return
	 */
	public boolean actualizarPacienteAMuertoTransaccional(Connection con,int codigoPaciente,boolean esVivo,String fechaMuerte,String horaMuerte, String certificadoDefuncion, String estado)
	{
		return SqlBaseUtilidadValidacionDao.actualizarPacienteAMuertoTransaccional(con,codigoPaciente,esVivo,fechaMuerte,horaMuerte,certificadoDefuncion,  estado);
	}
	
	/**
	 * Método para verificar la existencioa de cuentas asociadas a un contrato
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean existeCuentasAbiertasAsociadasAContrato(Connection con, int codigoContrato)
	{
		return SqlBaseUtilidadValidacionDao.existeCuentasAbiertasAsociadasAContrato(con, codigoContrato);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCategoria
	 * @return
	 */
	public boolean categoriaTriageUtilizadaEnSignosSintomasSistema(Connection con, int consecutivoCategoria)
	{
		return SqlBaseUtilidadValidacionDao.categoriaTriageUtilizadaEnSignosSintomasSistema(con,consecutivoCategoria);
	}

	/**
	 * Obtiene los consecutivos pk del triage 
	 * 
	 * @param con
	 * @param acronimoTipoIdentificacion
	 * @param numeroIdentificacion
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return pos 0 --> consecutivoTriage  , pos 1 --> cpnsecutivofechaTriage
	 */
	public String[] getConsecutivosTriage(Connection con, String acronimoTipoIdentificacion, String numeroIdentificacion, String codigoCentroAtencion, int codigoInstitucion)
	{
		return SqlBaseUtilidadValidacionDao.getConsecutivosTriage(con, acronimoTipoIdentificacion, numeroIdentificacion, codigoCentroAtencion, codigoInstitucion);
	}
	
	/**
	 * Método para saber si un triage determinado para una institucion tiene
	 * un numero de admision asignado
	 * @param con
	 * @param consecutivoTriage
	 * @param consecutivoFechaTriage
	 * @param institucion
	 * @return
	 */
	public boolean existeAdmisionParaTriage(Connection con, String consecutivoTriage, String consecutivoFechaTriage, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeAdmisionParaTriage(con, consecutivoTriage, consecutivoFechaTriage, institucion); 
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCategoria
	 * @return
	 */
	public boolean categoriaTriageUtilizadaEnTriage(Connection con, int consecutivoCategoria)
	{
		return SqlBaseUtilidadValidacionDao.categoriaTriageUtilizadaEnTriage(con,consecutivoCategoria);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param codigoAjuste
	 * @return
	 */
	public boolean cuentaCobroConFactEnAjustesPendienteDiferenteActual(Connection con, double cuentaCobro, int institucion, double codigoAjuste)
	{
		return SqlBaseUtilidadValidacionDao.cuentaCobroConFactEnAjustesPendienteDiferenteActual(con,cuentaCobro,institucion,codigoAjuste);
	}
	
	/**
	 * Método implementado para verificar si un paciente tiene cuentas Urgencias
	 * en estado Cuenta Activa o Asociada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean tieneCuentaUrgenciasAbierta(Connection con,String codigoPaciente)
	{
		return SqlBaseUtilidadValidacionDao.tieneCuentaUrgenciasAbierta(con,codigoPaciente);
	}
	
	/**
	 * Método para saber si una solicitud de cirugia que ya tiene hoja de quirurgica
	 * tiene la participacion de anestesiologo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean solCxNecesitaHojaAntesia(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadValidacionDao.solCxNecesitaHojaAntesia(con, numeroSolicitud);
	}
	
	/**
	 * Utilidad que obtiene los datos del médico que guardó la hoja
	 * de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String getDatosMedicoHojaAnestesia(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadValidacionDao.getDatosMedicoHojaAnestesia (con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param codTipoPrograma
	 * @param institucion
	 * @return
	 */
	public boolean esTipoProgramaPYPUsado(Connection con, String codTipoPrograma, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esTipoProgramaPYPUsado(con,codTipoPrograma,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean existeProgramaSaludPYP(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeProgramaSaludPYP(con,codigo,institucion);
	}
	
	/**
	 * metodo que verifica la existencia de cierre saldos iniciales capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean existeCierreSaldosInicialesCapitacion(Connection con, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeCierreSaldosInicialesCapitacion(con, institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @return
	 */
	public boolean existeRelacionProgramaActividadPYP(Connection con, int institucion, String programa, String actividad)
	{
		return SqlBaseUtilidadValidacionDao.existeRelacionProgramaActividadPYP(con,programa,actividad, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esActividadProgramaUsaoEnActProConvenios(Connection con, String codigo)
	{
		return SqlBaseUtilidadValidacionDao.esActividadProgramaUsaoEnActProConvenios(con,codigo);
	}
	
	/**
	 * existe cuenta cobro capitada
	 * @param con
	 * @param cuentaCobroCapitada
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existeCuentaCobroCapitada(Connection con, String cuentaCobroCapitada, int codigoInstitucion)
	{
		return SqlBaseUtilidadValidacionDao.existeCuentaCobroCapitada(con, cuentaCobroCapitada, codigoInstitucion);
	}
	
	/**
	 * Método que verifica si un convenio es de PYP
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioPYP(Connection con,String codigoConvenio)
	{
		return SqlBaseUtilidadValidacionDao.esConvenioPYP(con,codigoConvenio);
	}
	
	/**
	 * Método que obtiene el año y mes de cierre saldo de capitación
	 * de acuerdo a los parámetros enviados 
	 * @param institucion
	 * @param tipoCierre
	 * @return String[]={año_cierre, mes_cierre}
	 * 					sino exite return vacio String[]={"",""}
	 */
	public String[] obtenerFechaMesCierreSaldoCapitacion(Connection con, int institucion, String tipoCierre)
	{
		return SqlBaseUtilidadValidacionDao.obtenerFechaMesCierreSaldoCapitacion(con, institucion, tipoCierre);
	}
	
	/**
	 * Método que verifica si un paciente ya tiene información de PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean tienePacienteInformacionPYP(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadValidacionDao.tienePacienteInformacionPYP(con,campos);
	}
	
	/**
	 * Utilidad para verificar si un paciente tiene registros de ordenes ambulatorias
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return true si existe sino retorna false
	 */
	public boolean tienePacienteOrdenesAmbulatorias(Connection con, int codigoPaciente, int codigoInstitucion)
	{
		return SqlBaseUtilidadValidacionDao.tienePacienteOrdenesAmbulatorias(con, codigoPaciente, codigoInstitucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoGrupo
	 * @param institucion
	 * @return
	 */
	public boolean puedoEliminarGrupoEtareoCreDes(Connection con, String consecutivoGrupo, String institucion)
	{
		return SqlBaseUtilidadValidacionDao.puedoEliminarGrupoEtareoCreDes(con,consecutivoGrupo,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public boolean esConvenioCapitado(Connection con, int convenio)
	{
		return SqlBaseUtilidadValidacionDao.esConvenioCapitado(con,convenio);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean esAjusteDistribuidoCompletamete(Connection con, double codigoAjuste)
	{
		String consulta = "SELECT codigo from ajustes_empresa where codigo="+codigoAjuste+" and valor_ajuste=(select sum(case when f.tipo_factura_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then adfe.valor_ajuste else afe.valor_ajuste end) from ajus_fact_empresa afe left outer join  ajus_det_fact_empresa adfe on(adfe.codigo=afe.codigo and adfe.factura=afe.factura) inner join facturas f on (afe.factura=f.codigo) where afe.codigo="+codigoAjuste+")";
		
		return SqlBaseUtilidadValidacionDao.esAjusteDistribuidoCompletamete(con, consulta);
	}
	
	/**
	 * Método implementado para verificar que el grupo de la actividad
	 * tenga centros de costo parametrizados en la funcionalidad
	 * Centros Costo x Grupo Servicios con centro de atencion que se encuentren
	 * en Actividades por centro atencion
	 * @param con
	 * @param consecutivoActividad
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public boolean existeCentroCostoXGrupoServicioActividad(Connection con,String consecutivoActividad,int centroAtencion,int institucion)
	{
		return SqlBaseUtilidadValidacionDao.existeCentroCostoXGrupoServicioActividad(con,consecutivoActividad,centroAtencion,institucion);
	}
	
	/**
	 * Método que verifica si el paciente tiene antecedentes vacunas
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public boolean existeAntecedenteVacunas (Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadValidacionDao.existeAntecedenteVacunas (con, codigoPersona);
	}
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean esAccidenteTransito(Connection con, int idIngreso)
	{
		return SqlBaseUtilidadValidacionDao.esAccidenteTransito(con, idIngreso);
	}
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean esAccidenteTransitoEstadoDado(Connection con, int idIngreso, String estado)
	{
		return SqlBaseUtilidadValidacionDao.esAccidenteTransitoEstadoDado(con, idIngreso, estado);
	}

	/**
	 * metodo que indica si por ingreso existe evento catastrofico en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean esEventoCatastroficoEstadoDado(Connection con, int idIngreso, String estado)
	{
		return SqlBaseUtilidadValidacionDao.esEventoCatastroficoEstadoDado(con, idIngreso, estado);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public boolean esServicioMultiple(Connection con, String codigoServicio)
	{
		return SqlBaseUtilidadValidacionDao.esServicioMultiple(con,codigoServicio);
	}
	
	public boolean esServicioViaIngresoCargoProceso(Connection con, String viaIngreso, String servicio,String institucion) throws BDException
	{
		return SqlBaseUtilidadValidacionDao.esServicioViaIngresoCargoProceso(con,viaIngreso,servicio,institucion);
	}

	public boolean esServicioViaIngresoCargoSolicitud(Connection con, String viaIngreso, String servicio,String institucion) throws BDException
	{
		return SqlBaseUtilidadValidacionDao.esServicioViaIngresoCargoSolicitud(con,viaIngreso,servicio,institucion);
	}

	/**
	 * 
	 */
	public boolean estipoTransInvUsada(Connection con, String codigo)
	{
		return SqlBaseUtilidadValidacionDao.estipoTransInvUsada(con,codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public Vector obtenerSolicitudesTipoServicioPartosCesarea(Connection con, String cuenta, String cuentaAsociada)
	{
		return SqlBaseUtilidadValidacionDao.obtenerSolicitudesTipoServicioPartosCesarea(con, cuenta, cuentaAsociada);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public boolean existeInfoRecienNacido(Connection con, String consecutivoCx)
	{
		return SqlBaseUtilidadValidacionDao.existeInfoRecienNacido(con, consecutivoCx);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public boolean existeInfoPartosFinalizada(Connection con, String codigoPaciente, String consecutivoCx)
	{
		return SqlBaseUtilidadValidacionDao.existeInfoPartosFinalizada(con, codigoPaciente, consecutivoCx);
	}
	
	/**
	 * Indica si existen evoluciones en un rango de fechas, devolviendo un arrayList con las fechas que tienen evol
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param idCuentaAsociada
	 * @return
	 * @throws SQLException
	 */
	public ArrayList existeEvolucionesRangoFechas(Connection con, int idCuenta,  String fechaInicial, String fechaFinal, int idCuentaAsociada)
	{
		return SqlBaseUtilidadValidacionDao.existeEvolucionesRangoFechas(con, idCuenta, fechaInicial, fechaFinal, idCuentaAsociada);
	}
	

	/**
	 * 
	 */
	public boolean pacienteTieneIngresoAbierto(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadValidacionDao.pacienteTieneIngresoAbierto(con,codigoPersona);
	}
	
	/**
	 * 
	 */
	public String obtenerFechaEgresoUltimoIngresoPaciente(Connection con, int codigoPersona)
	{
		String cadena=	"SELECT " +
							"fechaegreso " +
						"FROM " +
							"(" +
								"SELECT " +
									"coalesce(to_char(fecha_egreso,'dd/mm/yyyy'), '') as fechaegreso " +
								"FROM " +
									"ingresos " +
								"WHERE " +
									"codigo_paciente="+codigoPersona+" and id in(select id_ingreso from cuentas where id_ingreso=ingresos.id and estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")) order by id desc " +
							") t where 1=1 "+ValoresPorDefecto.getValorLimit1()+" 1";
		return SqlBaseUtilidadValidacionDao.obtenerFechaEgresoUltimoIngresoPaciente(con,codigoPersona,cadena);
	}
	

	/**
	 * 
	 */
	public String obtenerFechaFacturaCuenta(Connection con, int codigoPersona)
	{
		String consulta="SELECT " +
							"fechafacturacuenta " +
						"FROM " +
							"(" +
								"SELECT " +
									"to_char(fecha,'dd/mm/yyyy') as fechafacturacuenta " +
								"FROM " +
									"facturas " +
								"WHERE cod_paciente="+codigoPersona+" ORDER BY codigo desc "+
							") t where 1=1 "+ValoresPorDefecto.getValorLimit1()+" 1";
		return SqlBaseUtilidadValidacionDao.obtenerFechaFacturaCuenta(con,codigoPersona,consulta);
	}
	
	/**
	 * 
	 */
	public boolean articuloTieneCantidadUnidosisActiva(Connection con, int codigoArticulo)
	{
		return SqlBaseUtilidadValidacionDao.articuloTieneCantidadUnidosisActiva(con,codigoArticulo);
	}
	
	/**
	 * 
	 */
	public boolean articuloTieneCantidadUnidosisActivaDadoUnidosis(Connection con, int codigoUnidosis)
	{
		return SqlBaseUtilidadValidacionDao.articuloTieneCantidadUnidosisActivaDadoUnidosis(con, codigoUnidosis);
	}
	
	

	/**
	 * 
	 */
	public boolean esSolicitudMedicamentos(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadValidacionDao.esSolicitudMedicamentos(con,numeroSolicitud);
	}
	

	/**
	 * 
	 * @param con
	 * @param clase
	 * @return
	 */
	public boolean esClaseInventarioUsada(Connection con, int clase)
	{
		return SqlBaseUtilidadValidacionDao.esClaseInventarioUsada(con,clase);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param grupo
	 * @return
	 */
	public boolean esClaseGrupoInventarioUsado(Connection con, int grupo,int clase)
	{
		return SqlBaseUtilidadValidacionDao.esClaseGrupoInventarioUsado(con,grupo, clase);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param subGrupo
	 * @param clase
	 * @param grupo
	 * @return
	 */
	public boolean esClaseGrupoSubGrupoInventarioUsado(Connection con, int subGrupo, int clase, int grupo)
	{
		return SqlBaseUtilidadValidacionDao.esClaseGrupoSubGrupoInventarioUsado(con,subGrupo,clase,grupo);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Centros de Costos  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroCostos
	 * @param int institucion
	 * */
	public boolean esCentroCostoUsado(Connection con,String codigoCentroCosto, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esCentroCostoUsado(con,codigoCentroCosto, institucion);
	}	
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el consecutivo de Centros de Atencion  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroAtencion
	 * @param int institucion
	 * */
	public boolean esCentroAtencionUsado(Connection con, String codigoCentroAtencion, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esCentroAtencionUsado(con, codigoCentroAtencion, institucion);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public boolean esPaqueteUsado(Connection con, String codigoPaquete, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esPaqueteUsado(con,codigoPaquete,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public boolean esConceptoUsado(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esConceptoUsado(con,codigo,institucion);
	}
	
	/**
	 *  esNaturalezaArticulosUsado
	 */
	public boolean esNaturalezaArticulosUsado(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esNaturalezaArticulosUsado(con,codigo,institucion);
	}
	
	/**
	 *  esViasIngresoUsado
	 */
	public boolean esViasIngresoUsado(Connection con, int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esViasIngresoUsado(con,codigo);
	}
	
	/**
	 *  esUbicacionGeograficaUsado
	 */
	public boolean esUbicacionGeograficaUsado(Connection con, String codigo)
	{
		return SqlBaseUtilidadValidacionDao.esUbicacionGeograficaUsado(con,codigo);
	}
	
	/**
	 *  esDepartamentoUsado
	 */
	public boolean esDepartamentoUsado(Connection con, String codigo_departamento, String codigo_pais)
	{
		return SqlBaseUtilidadValidacionDao.esDepartamentoUsado(con,codigo_departamento,codigo_pais);
	}
	
	/**
	 *  esCiudadUsado
	 */
	public boolean esCiudadUsado(Connection con, String codigo_ciudad,String codigo_departamento, String codigo_pais)
	{
		return SqlBaseUtilidadValidacionDao.esCiudadUsado(con,codigo_ciudad,codigo_departamento,codigo_pais);
	}
	
	/**
	 *  esLocalidadUsado
	 */
	public boolean esLocalidadUsado(Connection con, String codigo_localidad, String codigo_ciudad,String codigo_departamento, String codigo_pais)
	{
		return SqlBaseUtilidadValidacionDao.esLocalidadUsado(con,codigo_localidad,codigo_ciudad,codigo_departamento,codigo_pais);
	}
	
	/**
	 * Es tipos de monedas usado
	 */
	public boolean esTiposMonedaUsado(Connection con,int codigo,int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esTiposMonedaUsado(con,codigo,institucion);
	}
	
	
	public boolean esInclusionesExclusionesUsado(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esInclusionesExclusionesUsado(con,codigo,institucion);
	}
	
	
	public boolean esPaqueteConvenioUsado(Connection con, int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esPaqueteConvenioUsado(con,codigo);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del detalle de Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * @param String codigo Encabezado
	 * */
	public boolean esDetallePorcentajeCxMultiUsado(Connection con, int codigo, int codigo_encab)
	{
		return SqlBaseUtilidadValidacionDao.esDetallePorcentajeCxMultiUsado(con, codigo, codigo_encab);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del encabezado del Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param int institucion
	 * @param String codigo Encabezado 
	 * */
	public boolean esEncaPorcentajeCxMultiUsado(Connection con, int institucion, int codigo_encab)
	{
		return SqlBaseUtilidadValidacionDao.esEncaPorcentajeCxMultiUsado(con, institucion, codigo_encab);
	}

	/**
	 * 
	 */
	public boolean esEventoCatastrofico(Connection con, int codigoIngreso)
	{
		return SqlBaseUtilidadValidacionDao.esEventoCatastrofico(con,codigoIngreso);
	}
	
	/**
	 * 
	 */
	public boolean esTiposConvenioUsado(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esTiposConvenioUsado(con,codigo,institucion);
	}
	
	
	
	public boolean esServiciosEsteticosUsado(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esServiciosEsteticosUsado(con,codigo,institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de cobertura
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public boolean esCoberturaUsado(Connection con, String codigoCobertura, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esCoberturaUsado(con, codigoCobertura, institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Condicion de toma de Examen (true = no depende , false = depende)
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public boolean esCondicionTmExamenUsado(Connection con, int codigoExamen, int institucion) 
	{
		return SqlBaseUtilidadValidacionDao.esCondicionTmExamenUsado(con, codigoExamen, institucion);
	}
	
	/**
	 * 
	 */
	public boolean esPisosUsado(Connection con, int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esPisosUsado(con,codigo);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Tipo Habitacion (true = no depende , false = depende)
	 * @param Connection con
	 * @param int codigo
	 * @param int institucion
	 * */
	public boolean esTipoHabitacionUsado(Connection con, String codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esTipoHabitacionUsado(con,codigo,institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Almacen Parametros (true = no depende , false = depende)
	 * @param Connection con
	 * @param int codigo
	 * @param int institucion
	 * */
	public boolean esAlmacenParametroUsado(Connection con,int codigo,int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esAlmacenParametrosUsado(con, codigo, institucion);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Habitaciones (true = no depende , false = depende)
	 * @param Connection con
	 * @param int codigoHabitac
	 * @param int institucion
	 * */
	public boolean esHabitacionesUsado(Connection con, int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esHabitacionesUsado(con,codigo);
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo Instituciones SIRC  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoInstSirc
	 * @param int institucion
	 * */
	public boolean esInstitucionSircUsado(Connection con, String codigoInstSirc, int institucion) 
	{
		return SqlBaseUtilidadValidacionDao.esInstitucionSircUsado(con,codigoInstSirc,institucion);
	}	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Tipos Usuario Cama (true = no depende , false = depende)
	 * @param Connection con
	 * @param int codigo
	 * @param int institucion
	 * */
	public boolean esTiposUsuarioCamaUsado(Connection con, int codigo, int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esTiposUsuarioCamaUsado(con,codigo,institucion);
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Asocio (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoAsocio
	 * @param int institucion
	 * */
	public boolean esAsocioSalaCirugiaUsado(Connection con, String codigoAsocio, int institucion) 
	{
		return SqlBaseUtilidadValidacionDao.esAsocioSalaCirugiaUsado(con, codigoAsocio, institucion);
	}
	
	
	/**
	 * 
	 */
	public boolean esContrarreferencia(Connection con, int codigoIngreso)
	{
		return SqlBaseUtilidadValidacionDao.esContrarreferencia(con,codigoIngreso);
	}
	
	/**
	 * 
	 */
	public boolean esContrarreferenciaEstadoDado(Connection con, int idIngreso, String estado)
	{
		return SqlBaseUtilidadValidacionDao.esContrarreferenciaEstadoDado(con, idIngreso, estado);
	}

	/**
	 * 
	 */
	public boolean convenioManejaComplejidad(Connection con, int codigoConvenio) 
	{
		return SqlBaseUtilidadValidacionDao.convenioManejaComplejidad(con, codigoConvenio);
	}
	
	/**
	 * 
	 */
	public boolean esNumeroCuentaCobroUsado(Connection con, double numeroCuentaCobro) 
	{
		return SqlBaseUtilidadValidacionDao.esNumeroCuentaCobroUsado(con, numeroCuentaCobro);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean esConvenioUsado(Connection con, int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esConvenioUsado(con, codigo);
	}
	
	/**
	 * 
	 */
	public int esCuentaValida(Connection con, int codigoCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.esCuentaValida(con, codigoCuenta);
	}

	/**
	 * 
	 */
	public boolean responsableTieneServicioEstado(Connection con, String subCuenta, int codigoEstado) 
	{
		return SqlBaseUtilidadValidacionDao.responsableTieneServicioEstado(con, subCuenta,codigoEstado);
	}

	/**
	 * 
	 */
	public boolean ingresoEstaEnProcesoDistribucion(Connection con, int codigoIngreso,String usuario) 
	{
		return SqlBaseUtilidadValidacionDao.ingresoEstaEnProcesoDistribucion(con, codigoIngreso,usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean esVigenciaConvenioUsado(Connection con,int codigo,int institucion)
	{
		return SqlBaseUtilidadValidacionDao.esVigenciaConvenioUsado(con, codigo, institucion);
	}
	
	/**
	 * 
	 */
	public boolean esAsocioXUvrTipoSalaUsado(Connection con,int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esAsocioXUvrTipoSalaUsado(con, codigo);
	}
	
	/**
	 * 
	 */
	public boolean esAsociosXUvrUsado(Connection con,int codigo)
	{
		return SqlBaseUtilidadValidacionDao.esAsociosXUvrUsado(con, codigo);
	}
	
	/**
	 * 
	 */
	public boolean existePacientes (Connection con, String tipoBD, String tipoId, String numeroId)
	{
		return SqlBaseUtilidadValidacionDao.existePacientes (con, tipoId, numeroId);
	}
	
	/**
	 * 
	 */
	public boolean pacienteTieneMovimientoAbonos(Connection con, int codigoPersona) 
	{
		return SqlBaseUtilidadValidacionDao.pacienteTieneMovimientoAbonos (con, codigoPersona);
	}

	/**
	 * Método para verificar si el paciente tiene o no evoluciones para el día en que se genera la orden
	 * @param con
	 * @param codigoCuenta
	 * @param fecha
	 * @return
	 */
	public boolean tieneEvolucionesParaElDia(Connection con, int codigoCuenta, String fecha) 
	{
		return SqlBaseUtilidadValidacionDao.tieneEvolucionesParaElDia(con, codigoCuenta, fecha);
	}
	
	/**
	 * Valida si las notas de enfermeria esta cerrada
	 * @param Connection con
	 * @param int codigoCuenta
	 * */
	public boolean esCerradaNotasEnfermeria(Connection con, int codigoCuenta)
	{
		return SqlBaseUtilidadValidacionDao.esCerradaNotasEnfermeria(con, codigoCuenta);
	}
	
	/**
	 * 
	 */
	public String[] obtenerFechaYHoraCuidadoEspecial(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.obtenerFechaYHoraCuidadoEspecial(con, idCuenta);
	}
	
	/**
	 * 
	 */
	public boolean tipoMonitoreoRequiereValoracion(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.tipoMonitoreoRequiereValoracion(con, idCuenta);
	}

	/**
	 * 
	 */
	public boolean tieneValoracionCuidadoEspecial(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.tieneValoracionCuidadoEspecial(con, idCuenta);
	}
	
	/**
	 * 
	 */
	public boolean tieneIngresoActivoCuidadoEspecial(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.tieneIngresoActivoCuidadoEspecial(con, idCuenta);
	}

	/**
	 * 
	 */
	public boolean monitoreoEvolucionRequiereValoracion(Connection con, int codigoEvolucion) 
	{
		return SqlBaseUtilidadValidacionDao.monitoreoEvolucionRequiereValoracion(con, codigoEvolucion);
	}
	
	/**
	 * 
	 */
	public boolean conductaSeguirUltimaEvolucion(Connection con, int codigoEvolucion) 
	{
		return SqlBaseUtilidadValidacionDao.conductaSeguirUltimaEvolucion(con, codigoEvolucion);
	}
	
	/**
	 * 
	 */
	public boolean tieneIngresoCuidadosFinalizado(Connection con, int codigoIngreso) 
	{
		return SqlBaseUtilidadValidacionDao.tieneIngresoCuidadosFinalizado(con, codigoIngreso);
	}
	
	/**
	 * 
	 */
	public boolean pacienteFalleceCirugia(Connection con, int codigoCuenta) 
	{
		return SqlBaseUtilidadValidacionDao.pacienteFalleceCirugia(con, codigoCuenta);
	}

	/**
	 * 
	 */
	public boolean estadoSolicitudAnulacion(Connection con, int codigoFactura) 
	{
		return SqlBaseUtilidadValidacionDao.estadoSolicitudAnulacion(con, codigoFactura);
	}

	/**
	 * 
	 */
	public boolean usuarioAutorizadoAnular(Connection con, String loginUsuario, int centroAtencion) 
	{
		return SqlBaseUtilidadValidacionDao.usuarioAutorizadoAnular(con, loginUsuario, centroAtencion);
	}

	/**
	 * 
	 */
	public boolean facturaAprobadaAnular(Connection con, int codigoFactura) 
	{
		return SqlBaseUtilidadValidacionDao.facturaAprobadaAnular(con, codigoFactura);
	}
	

	/**
	 * Metodo encargado de verificar si un concepto especifico es 
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public boolean esConceptoEspecificoUsado(Connection con, String codigo, String institucion) 
	{
		return SqlBaseUtilidadValidacionDao.esConceptoEspecificoUsado(con, codigo, institucion);
	}
	
	/**
	 * verifica si el paciente se encuentra como paciente con capitacion vigente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString esPacienteCapitadoVigente(Connection con, HashMap parametros)
	{
		String strEsPacienteCapitadoVigente = "SELECT " +
													"s.origen," +
													"s.contrato " +
												"FROM (" +
														"SELECT " +
															"1 as origen," +
															"cuc.contrato " +
														"FROM " +
															"usuarios_capitados uc " +
														"INNER JOIN " +
															"conv_usuarios_capitados cuc ON (cuc.usuario_capitado = uc.codigo) " +
														"WHERE " +
															"'"+parametros.get("fechaVigencia").toString()+"' BETWEEN cuc.fecha_inicial AND cuc.fecha_final " +
															"AND uc.numero_identificacion = '"+parametros.get("numeroId").toString()+"' " +
															"AND uc.tipo_identificacion = '"+parametros.get("tipoId").toString()+"'  " +
														"UNION " +
														"SELECT " +
															"2 as origen, " +
															"uxc.contrato " +
														"FROM " +
															"personas p " +
														"INNER JOIN " +
															"usuario_x_convenio uxc ON (uxc.persona = p.codigo) " +
														"WHERE " +
															"'"+parametros.get("fechaVigencia").toString()+"' BETWEEN to_char(uxc.fecha_inicial, 'YYYY-MM-DD') AND to_char(uxc.fecha_final, 'YYYY-MM-DD') " +
															"AND p.numero_identificacion = '"+parametros.get("numeroId").toString()+"' " +
															"AND p.tipo_identificacion = '"+parametros.get("tipoId").toString()+"' " +
													")s WHERE 1=1 "+ValoresPorDefecto.getValorLimit1()+" 1 ";
		
		return SqlBaseUtilidadValidacionDao.esPacienteCapitadoVigente(con, parametros, strEsPacienteCapitadoVigente);
	}
	
	/**
	 * Método que indica si un contrato se 
	 * esta usando en otras funcionalidades 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean esContratoUsado(Connection con, int codigoContrato)
	{
		return SqlBaseUtilidadValidacionDao.esContratoUsado(con, codigoContrato);
	}
	
	/**
	 * Metodo encargado de verificar si un TextoRespuestaProcedimientos es 
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public boolean esTextoRespuestaProcedimientosUsado(Connection con, String codigo, String institucion)
	{
		return SqlBaseUtilidadValidacionDao.esTextoRespuestaProcedimientosUsado(con, codigo, institucion);
	}

	/**
	 * 
	 */
	public boolean esGrupoEspecialUsado(Connection con, String codigoGrupo) 
	{
		return SqlBaseUtilidadValidacionDao.esGrupoEspecialUsado(con, codigoGrupo);
	}

	/**
	 * 
	 */
	public boolean esConceptoAjusteUsado(Connection con, String codigoAjuste, int codigoInstitucion) 
	{
		return SqlBaseUtilidadValidacionDao.esConceptoAjusteUsado(con, codigoAjuste, codigoInstitucion);
	}
	
	/**
	 * Metodo encargado de idenficar si un consentimiento informado
	 * ha sido usado o no
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean esConsentimientoInformadoUsado(Connection con, String codigo, int institucion) 
	{
		return SqlBaseUtilidadValidacionDao.esConsentimientoInformadoUsado(con, codigo, institucion);
	}
	
	/**
	 * Metodo encargado de consultar el ultimo ingreso
	 * del paciente y verificar si este tuvo tipo evento
	 * catastrófico
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int esUltimoIngreso(Connection con, String codigoPaciente)
	{
		return SqlBaseUtilidadValidacionDao.esUltimoIngreso(con, codigoPaciente, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Metodo encargado de verificar si un textos respuesta procedimientos esta siendo utilizado o no.
	 * @param con Connection
	 * @param codArticulo String
	 * @param institucion int
	 * */
	public boolean esArticuloUsado(Connection con, String codArticulo, String institucion) 
	{
		return SqlBaseUtilidadValidacionDao.esArticuloUsado(con, codArticulo, institucion);
	}
	

	/**
	 * 
	 */
	public String obtenerMaximoConsecutivoJustificacionNoPosArticulos(Connection con) 
	{
		return SqlBaseUtilidadValidacionDao.obtenerMaximoConsecutivoJustificacionNoPosArticulos(con);
	}


	/**
	 * 
	 */
	public String obtenerMaximoConsecutivoJustificacionNoPosServicios(Connection con) 
	{
		return SqlBaseUtilidadValidacionDao.obtenerMaximoConsecutivoJustificacionNoPosServicios(con);
	}

	/**
	 * 
	 */
	public boolean esUsuarioAUtorizadoAnulacionUsado(Connection con, String codigoUsuario, int codigoInstitucion) 
	{
		return SqlBaseUtilidadValidacionDao.esUsuarioAUtorizadoAnulacionUsado(con, codigoUsuario, codigoInstitucion);
	}

	@Override
	public boolean validarPacienteEnValoracion(Connection con, int codigoPersona) {
		return SqlBaseUtilidadValidacionDao.validarPacienteEnValoracion(con, codigoPersona);
	}

	@Override
	public boolean esDeudorCarteraPaciente(Connection con, PersonaBasica paciente) {
		return SqlBaseUtilidadValidacionDao.esDeudorCarteraPaciente(con, paciente);
	}

	@Override
	public double consultarSaldoPaciente(Connection con, PersonaBasica paciente) {
		try{
			String sentencia=
				"SELECT " +
				"(" +
					"SELECT " +
						"sum(cdf.valor_couta) - coalesce(sum(apcp.valor),0) AS saldo " +
					"FROM " +
						"carterapaciente.documentos_garantia dg " +
					"INNER JOIN " +
						"carterapaciente.datos_financiacion df " +
							"ON(df.codigo_pk_docgarantia=dg.codigo_pk) " +
					"INNER JOIN " +
						"carterapaciente.cuotas_datos_financiacion cdf " +
							"ON(cdf.dato_financiacion=df.codigo_pk) " +
					"LEFT OUTER JOIN " +
						"carterapaciente.aplicac_pagos_cartera_pac apcp " +
							"ON(apcp.datos_financiacion=df.codigo_pk) " +
					"WHERE " +
						"dg.codigo_paciente=?" +
				") AS saldo "+
				"FROM DUAL";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, paciente.getCodigoPersona());
			logger.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			double saldo=0;
			if(rsd.next())
			{
				saldo=rsd.getDouble("saldo");
			}
			rsd.close();
			psd.close();
			return saldo;
		}
		catch (SQLException e) {
			logger.error("Error consultando el saldo del paciente "+paciente.getNombrePersona()+" "+paciente.getCodigoTipoIdentificacionPersona()+":"+paciente.getNumeroIdentificacionPersona(), e);
			return -1;
		}
	}

	@Override
	public boolean verificarBloqueoIngresoPacienteODeudor(Connection con, PersonaBasica paciente, int viaIngreso) {
		return SqlBaseUtilidadValidacionDao.verificarBloqueoIngresoPacienteODeudor(con, paciente, viaIngreso);
	}

	@Override
	public boolean tieneCuotasPendientes(Connection con, PersonaBasica paciente, int numeroDiasMora) {
		return SqlBaseUtilidadValidacionDao.tieneCuotasPendientes(con, paciente, numeroDiasMora);
	}

	@Override
	public boolean tieneAutorizacionIngresoSaldoPendiente(Connection con, PersonaBasica paciente, int viaIngreso, String codigoTipoPaciente) {
		try{
			String sentencia="SELECT codigo_pk AS codigo_pk "+
				"FROM carterapaciente.autorizacion_saldo_mora "+
				"WHERE via_ingreso=? "+
				"AND tipos_paciente =? "+
				"AND codigo_paciente=? "+
				"AND fecha "+
				" || ' ' "+
				" || hora > TO_CHAR(CURRENT_TIMESTAMP - NUMTODSINTERVAL(substr(horas_vigencia, 1, 2), 'HOUR') "+
				" - NUMTODSINTERVAL(substr(horas_vigencia, 4, 5), 'MINUTE') "+
				" , 'YYYY-MM-DD HH24:MI')";

  
  
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, viaIngreso);
			psd.setString(2, codigoTipoPaciente);
			psd.setInt(3, paciente.getCodigoPersona());
			logger.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=rsd.next();
			rsd.close();
			psd.close();
			return resultado;
		}catch (SQLException e)
		{
			logger.error("Error verificando si el paciente tiene autorización de ingreso con saldo pendiente", e);
			return false;
		}
	}
	
	public boolean existeConvencionMedico(Connection con, int convencion) {
		return SqlBaseUtilidadValidacionDao.existeConvencionMedico(con, convencion);
	}
	
	public boolean validarPacienteEnValoracionDiferenteUsuario(Connection con,int codigoPersona, String loginUsuario) {
		// TODO Auto-generated method stub
		return SqlBaseUtilidadValidacionDao.validarPacienteEnValoracionDiferenteUsuario(con,codigoPersona,loginUsuario);
	}

	@Override
	public boolean esConsecutvioReciboCajaUsado(int institucion,
			String valorConsecutivo) {
		return SqlBaseUtilidadValidacionDao.esConsecutvioReciboCajaUsado(institucion, valorConsecutivo);
	}
	
	public boolean esNaturalezaValidaTipoRegimen(int natPaciente,
			String tipoRegimen) {
		return SqlBaseUtilidadValidacionDao.esNaturalezaValidaTipoRegimen(natPaciente, tipoRegimen);
	}

	@Override
	public boolean esEvolucionConOrdenSalidaPacienteMuerto(Connection con,int idCuenta, int idEvolucion) {
		return SqlBaseUtilidadValidacionDao.esEvolucionConOrdenSalidaPacienteMuerto(con, idCuenta, idEvolucion);
	}
}