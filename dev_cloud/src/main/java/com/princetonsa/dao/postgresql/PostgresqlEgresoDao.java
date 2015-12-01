/*
 * @(#)PostgresqlEgresoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.EgresoDao;
import com.princetonsa.dao.sqlbase.SqlBaseEgresoDao;

/**
 * Esta clase implementa el contrato estipulado en <code>EgresoDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>Egreso</code>
 *
 * @version 1.0, May 16, 2003
 */
public class PostgresqlEgresoDao implements EgresoDao
{
	/*/**
	 * Para manejar los logs de esta clase
	 */
	//private Logger logger = Logger.getLogger(PostgresqlEgresoDao.class);
	
	/**
	 * Implementación del método que inserta un preegreso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarPreEgreso(Connection , int , String , String ) throws SQLException 
	 */
	public int insertarPreEgreso(Connection con, int numeroCuenta, int codigoMedico) 
	{
		return SqlBaseEgresoDao.insertarPreEgreso(con, numeroCuenta, codigoMedico) ;
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de la factura
	 * para la boleta de salida
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------
	 * --ingreso6
	 * @return  HashMap
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * fechaCierreIngreso0,horaCierreIngreso1
	 * facturasFecha2,cama3,codigoCama4,ingreso6
	 */ 
	public HashMap consultaDatosFactura (Connection connection,HashMap datos)
	{
		return SqlBaseEgresoDao.consultaDatosFactura(connection, datos);
	}
	/**
	 * Implementación de la inserción automática de un egreso.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarEgresoAutomatico(Connection, int, String) 
	 */
	public int insertarEgresoAutomatico(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,int causaExterna) throws SQLException
	{
		return SqlBaseEgresoDao.insertarEgresoAutomatico(con, numeroCuenta, loginUsuario, diagnosticoMuerte, cieDiagnosticoMuerte, diagnosticoPrincipal, cieDiagnosticoPrincipal, diagnosticoRelacionado1, cieDiagnosticoRelacionado1, diagnosticoRelacionado2, cieDiagnosticoRelacionado2, diagnosticoRelacionado3, cieDiagnosticoRelacionado3,causaExterna) ;
	}
	
	/**
	 * Implementación de la inserción automática de un egreso cuando en una valoración se 
	 * selecciona conducta a seguir hospitalizar en piso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarEgresoAutomatico(Connection, int, String) 
	 */
	public int insertarEgresoAutomaticoValoracionHospitalizarEnPiso(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,String fechaEgreso,String horaEgreso,int tipoMonitoreo) throws SQLException
	{
	    return SqlBaseEgresoDao.insertarEgresoAutomaticoValoracionHospitalizarEnPiso(con, numeroCuenta, loginUsuario, diagnosticoMuerte, cieDiagnosticoMuerte, diagnosticoPrincipal, cieDiagnosticoPrincipal, diagnosticoRelacionado1, cieDiagnosticoRelacionado1, diagnosticoRelacionado2, cieDiagnosticoRelacionado2, diagnosticoRelacionado3, cieDiagnosticoRelacionado3,fechaEgreso,horaEgreso, tipoMonitoreo);
	}
	
	/**
	 * Implementación de cargar un egreso de modo general
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarEgresoGeneral (Connection , int ) throws SQLException 
	 */
	public ResultSetDecorator cargarEgresoGeneral (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseEgresoDao.cargarEgresoGeneral (con, idCuenta) ;
	}
	
	/**
	 * Implementación de cargar un egreso para reversión de egreso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarEgresoReversionEgreso (Connection , int ) throws SQLException 
	 */
	public ResultSetDecorator cargarEgresoReversionEgreso (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseEgresoDao.cargarEgresoReversionEgreso (con, idCuenta) ;
	}
	
	/**
	 * Implementación de la consulta de datos basicos de un egreso asociado a una cuenta.
	 * @see com.princetonsa.dao.EgresoDao#getBasicoEgreso (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	public ResultSetDecorator getBasicoEgreso(Connection con, int numeroCuenta) throws SQLException
	{		
		return SqlBaseEgresoDao.getBasicoEgreso(con, numeroCuenta) ;
	}

	/**
	 * Implementación de la búsqueda de número de autorización
	 * desde cualquiera de las admisiones.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarNumeroAutorizacionAdmision(Connection , int , char ) throws SQLException
	 */
	public String cargarNumeroAutorizacionAdmision(Connection con, int codigoAdmision, char tipoAdmision) throws SQLException
	{
		return SqlBaseEgresoDao.cargarNumeroAutorizacionAdmision(con, codigoAdmision, tipoAdmision) ;
	}

	/**
	 * Implementación de la búsqueda de la causa externa
	 * desde cualquiera de las admisiones.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarCausaExternaUltimaValoracionUH (Connection , int , char ) throws SQLException
	 */
	public ResultSetDecorator cargarCausaExternaUltimaValoracionUH (Connection con, int idCuenta, char tipoAdmision) throws SQLException
	{
		return SqlBaseEgresoDao.cargarCausaExternaUltimaValoracionUH (con, idCuenta, tipoAdmision) ;
	}
	
	/**
	 * Implementación de la modificación del egreso cuando un usuario esta a
	 * punto de finalizarlo (El egreso).
	 * 
	 * @see com.princetonsa.dao.EgresoDao#modificarEgresoUsuarioFinalizarTransaccional (Connection , int , String , String , String , String , String, String , String) throws SQLException 
	 */
	public int modificarEgresoUsuarioFinalizarTransaccional (Connection con, int idCuenta, String fechaEgreso, String horaEgreso, String fechaGrabacion, String horaGrabacion, String numeroAutorizacion, String loginUsuario, String estado) throws SQLException
	{
		return SqlBaseEgresoDao.modificarEgresoUsuarioFinalizarTransaccional (con, idCuenta, fechaEgreso, horaEgreso, fechaGrabacion, horaGrabacion, numeroAutorizacion, loginUsuario, estado) ;
	}

	/**
	 * Implementación de la actualización en el egreso, una vez este se 
	 * reverse en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.EgresoDao#actualizarPorReversionEgresoTransaccional (Connection , int , int , String , String ) throws SQLException 
	 */
	public int actualizarPorReversionEgresoTransaccional (Connection con, int idCuenta, int idPersonaRealizaReversion, String motivoReversionEgreso, String estado) throws SQLException
	{
		return SqlBaseEgresoDao.actualizarPorReversionEgresoTransaccional (con, idCuenta, idPersonaRealizaReversion, motivoReversionEgreso, estado) ;
	}
	
	/**
	 * Implementación de la actualización de la informacion del motivo
	 * de reversion en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.EgresoDao#actualizarInformacionMotivoReversionTransaccional (con, idCuenta, deboMostrarMotivoReversionEpicrisis, idEvolucion, estado) throws SQLException 
	 */
	public int actualizarInformacionMotivoReversionTransaccional (Connection con, int idCuenta, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException
	{
		return SqlBaseEgresoDao.actualizarInformacionMotivoReversionTransaccional (con, idCuenta, deboMostrarMotivoReversionEpicrisis, idEvolucion, estado) ;
	}

	/**
	 * Implementación de creación de egreso desde evolución.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#crearEgresoDesdeEvolucion (Connection , int , int , boolean , int , String , String , int , String , int , String , int , String , int , String , int , String , int , String , String , String ) 
	 */
	public int crearEgresoDesdeEvolucion (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie) throws SQLException
	{
		return SqlBaseEgresoDao.crearEgresoDesdeEvolucion (con, idCuenta, idEvolucion, estadoSalida, destinoSalida, otroDestinoSalida, numeroAutorizacion, causaExterna, acronimoDiagnosticoMuerte, diagnosticoMuerteCie, acronimoDiagnosticoPrincipal, diagnosticoPrincipalCie, acronimoDiagnosticoRelacionado1, diagnosticoRelacionado1Cie, acronimoDiagnosticoRelacionado2, diagnosticoRelacionado2Cie, acronimoDiagnosticoRelacionado3, diagnosticoRelacionado3Cie, codigoMedico, acronimoDiagnosticoComplicacion,diagnosticoComplicacionCie);
	}

	/**
	 * Implementación de la creación de un egreso desde una evolucion,
	 * soportando hacer parte de una transacción.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#crearEgresoDesdeEvolucionTransaccional (Connection , int , int , boolean , int , String , String , int , String , int , String , int , String , int , String , int , String , int , String , String , String ) throws SQLException
	 */
	public int crearEgresoDesdeEvolucionTransaccional (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie, String estado) throws SQLException
	{
		return SqlBaseEgresoDao.crearEgresoDesdeEvolucionTransaccional (con, idCuenta, idEvolucion, estadoSalida, destinoSalida, otroDestinoSalida, numeroAutorizacion, causaExterna, acronimoDiagnosticoMuerte, diagnosticoMuerteCie, acronimoDiagnosticoPrincipal, diagnosticoPrincipalCie, acronimoDiagnosticoRelacionado1, diagnosticoRelacionado1Cie, acronimoDiagnosticoRelacionado2, diagnosticoRelacionado2Cie, acronimoDiagnosticoRelacionado3, diagnosticoRelacionado3Cie, codigoMedico, acronimoDiagnosticoComplicacion,diagnosticoComplicacionCie,estado) ;
	}

	/**
	 * Implementación de la actualización del boolean del motivo
	 * de reversion en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.EgresoDao#actualizarInformacionMotivoReversionSoloBooleanTransaccional (Connection , boolean , int , String ) throws SQLException 
	 */
	public int actualizarInformacionMotivoReversionSoloBooleanTransaccional (Connection con, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException
	{
		return SqlBaseEgresoDao.actualizarInformacionMotivoReversionSoloBooleanTransaccional (con, deboMostrarMotivoReversionEpicrisis, idEvolucion, estado) ;
	}

	/**
	 * Implementación del método que completa un semi-egreso en una
	 * Base de datos Postgresql.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#completarSemiEgreso (Connection con, int , String ) throws SQLException 
	 */
	public int completarSemiEgreso (Connection con, int numeroCuenta, String loginUsuario,String acronimo, int tipocie, int codigoTipoMonitoreo) throws SQLException
	{
		return SqlBaseEgresoDao.completarSemiEgreso (con, numeroCuenta, loginUsuario, acronimo, tipocie, codigoTipoMonitoreo) ;
	}

	/**
	 * Implementación del método que reversa un semi-egreso en una
	 * Base de datos Postgresql.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#reversarSemiEgreso (Connection , int ) throws SQLException
	 */
	public int reversarSemiEgreso (Connection con, int numeroCuenta) throws SQLException
	{
		return SqlBaseEgresoDao.reversarSemiEgreso (con, numeroCuenta) ;
	}

	/**
	 * Revisa que existe un egreso asociado al numero de cuenta dado y que ademas tenga lo datos
	 * de medico, usuario y fecha y hora de egreso llenos.
	 */
	public boolean existeEgresoEfectivo(Connection con, int numeroCuenta) 
	{
		return SqlBaseEgresoDao.existeEgresoEfectivo(con, numeroCuenta) ;
	}

	/**
	 * Implementación del método que elimina un preegreso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#borrarPreEgreso(Connection , int ) throws SQLException 
	 */
	public int borrarPreEgreso(Connection con, int numeroCuenta) 
	{
		return SqlBaseEgresoDao.borrarPreEgreso(con, numeroCuenta) ;
	}
	
	/**
	 * Implementación del borrado de un egreso que se habia generado automaticamente.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#borrarEgresoAutomatico(Connection, int) 
	 */
	public int borrarEgresoAutomatico(Connection con, int numeroCuenta) 
	{
		return SqlBaseEgresoDao.borrarEgresoAutomatico(con, numeroCuenta) ;
	}
	/**
	 * Adición de Sebastián
	 * Método que pemrite consultar un semiEgreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public Collection cargarSemiEgreso (Connection con,int idCuenta){
		return SqlBaseEgresoDao.cargarSemiEgreso(con,idCuenta);
	}
	
	/**
	 * Adición sebastián
	 * Método que carga los datos básicos de una reversión de un egreso
	 * @param con
	 * @param idCuenta
	 * @return Colección con la fecha y hora del egreso (apenas tiene esos campos)
	 */
	public Collection cargarReversionEgreso(Connection con,int idCuenta){
		return SqlBaseEgresoDao.cargarReversionEgreso(con,idCuenta);
	}
	
	/**
	 * Metodo que carga la fecha - hora de egreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public ResultSetDecorator cargarFechaHoraEgreso(Connection con, int idCuenta)
	{
	    return SqlBaseEgresoDao.cargarFechaHoraEgreso(con, idCuenta);
	}
	
	/**
	 * Metodo encargado de actualizar los nuevos datos del egreso
	 * @param connection
	 * @param datos
	 * ----------------------------
	 * KEY'S DEL MAPA DATOS
	 * ----------------------------
	 * acompanadoPor6 --> Opcional
	 * remitidoA7 --> Opcional
	 * placa8 --> Opcional
	 * conductor9 --> Opcional
	 * quienRecibe10 --> Opcional
	 * observaciones11 --> Opcional
	 * codigoCuenta12 --> Requerido
	 * @return false/true
	 */
	public boolean actualizarNuevosDatosEgreso (Connection connection,HashMap datos)
	{
		return SqlBaseEgresoDao.actualizarNuevosDatosEgreso(connection, datos);
	}
	
	/**
	 * Metodo encargado de consultar los nuevos datos del egreso
	 * @param connection
	 * @param cuenta
	 *  @return
	 *  -------------------------
	 *  KEY'S DEL MAPA RESULT
	 *  -------------------------
	 *  acompanadoPor6, remitidoA7,
	 *  placa8,conductor9,quienRecibe10,
	 *  observaciones11,codigoCuenta12 
	 */
	public HashMap consultarNuevosDatosEgreso (Connection connection,String cuenta)
	{
		return SqlBaseEgresoDao.consultarNuevosDatosEgreso(connection, cuenta);
	}


	/**
	 * Metodo encargado de consultar si existe boleta de salida
	 * @param connection
	 * @param cuenta
	 *  @return false/true
	 */
	public boolean consultarExisteBoletaSalida(Connection connection,int idCuenta) {
		return SqlBaseEgresoDao.consultarExisteBoletaSalida(connection, idCuenta);
	}
}