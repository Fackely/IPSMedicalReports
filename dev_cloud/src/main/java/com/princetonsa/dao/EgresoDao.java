/*
 * @(#)EgresoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;


/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Egreso</code>.
 *
 * @version 1.0, May 16, 2003
 */
public interface EgresoDao 
{
	/**
	 * M�todo que elimina un egreso dado el n�mero de la cuenta a la
	 * que pertenece. Se utiliza solo para borrar Egresos autom�ticos, 
	 * de ah� su nombre
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @return 0 si no se pudo borrar correctamente, 1 de lo contrario
	 * @throws SQLException
	 */
	public int borrarEgresoAutomatico(Connection con, int numeroCuenta) ;
	
	/**
	 * M�todo que inserta un egreso autom�tico
	 *  
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta C�digo de la cuenta a la que se va a crear el egreso 
	 * @param loginUsuario
	 * @return
	 * @throws SQLException
	 */
	public int insertarEgresoAutomatico(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,int causaExterna) throws SQLException;
	
	/**
	 * Implementaci�n de la inserci�n autom�tica de un egreso cuando en una valoraci�n se 
	 * selecciona conducta a seguir hospitalizar en piso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarEgresoAutomatico(Connection, int, String) 
	 */
	public int insertarEgresoAutomaticoValoracionHospitalizarEnPiso(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,String fechaEgreso,String horaEgreso, int tipoMonitoreo) throws SQLException;
	
	public int borrarPreEgreso(Connection con, int numeroCuenta) ;
	
	public int insertarPreEgreso(Connection con, int numeroCuenta, int codigoMedico) ;
	
	public boolean existeEgresoEfectivo(Connection con, int numeroCuenta) ;
		
	/**
	 * M�todo que inserta los datos iniciales del egreso, cuando el m�dico da
	 * orden de salida
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta a la que se va a crear el egreso 
	 * @param idEvolucion C�digo de la evoluci�n desde la cual se di� orden de salida
	 * @param estadoSalida Estado del paciente a la salida
	 * @param destinoSalida Destino del paciente a la salida
	 * @param otroDestinoSalida Campo de texto donde se especifica el 
	 * destino del paciente a la salida, si el campo destinoSalida se refiere a "otros" 
	 * @param numeroAutorizacion N�mero de autorizaci�n
	 * @param causaExterna Causa externa del ingreso a la instituci�n
	 * @param acronimoDiagnosticoMuerte Acronimo del diagnostico de muerte, 
	 * si el paciente murio
	 * @param diagnosticoMuerteCie TipoCie del diagnostico de muerte, 
	 * si el paciente murio
	 * @param acronimoDiagnosticoPrincipal Acronimo del diagnostico principal
	 * @param diagnosticoPrincipalCie Tipo Cie del diagnostico principal
	 * @param acronimoDiagnosticoRelacionado1 Acronimo del diagnostico 
	 * relacionado 1
	 * @param diagnosticoRelacionado1Cie Tipo Cie del diagnostico 
	 * relacionado 1
	 * @param acronimoDiagnosticoRelacionado2 Acronimo del diagnostico 
	 * relacionado 2
	 * @param diagnosticoRelacionado2Cie Tipo Cie del diagnostico 
	 * relacionado 2
	 * @param acronimoDiagnosticoRelacionado3 Acronimo del diagnostico 
	 * relacionado 3
	 * @param diagnosticoRelacionado3Cie Tipo Cie del diagnostico 
	 * relacionado 3
	 * @param codigoMedico C�digo del m�dico
	 * @return
	 * @throws SQLException
	 */	
	public int crearEgresoDesdeEvolucion (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie) throws SQLException;
	
	/**
	 * M�todo que inserta los datos iniciales del egreso, cuando el m�dico da
	 * orden de salida . Soporta hacer parte de una transacci�n
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta a la que se va a crear el egreso 
	 * @param idCuenta C�digo de la cuenta a la que se va a crear el egreso 
	 * @param idEvolucion C�digo de la evoluci�n desde la cual se di� orden de salida
	 * @param estadoSalida Estado del paciente a la salida
	 * @param destinoSalida Destino del paciente a la salida
	 * @param otroDestinoSalida Campo de texto donde se especifica el 
	 * destino del paciente a la salida, si el campo destinoSalida se refiere a "otros" 
	 * @param numeroAutorizacion N�mero de autorizaci�n
	 * @param causaExterna Causa externa del ingreso a la instituci�n
	 * @param acronimoDiagnosticoMuerte Acronimo del diagnostico de muerte, 
	 * si el paciente murio
	 * @param diagnosticoMuerteCie TipoCie del diagnostico de muerte, 
	 * si el paciente murio
	 * @param acronimoDiagnosticoPrincipal Acronimo del diagnostico principal
	 * @param diagnosticoPrincipalCie Tipo Cie del diagnostico principal
	 * @param acronimoDiagnosticoRelacionado1 Acronimo del diagnostico 
	 * relacionado 1
	 * @param diagnosticoRelacionado1Cie Tipo Cie del diagnostico 
	 * relacionado 1
	 * @param acronimoDiagnosticoRelacionado2 Acronimo del diagnostico 
	 * relacionado 2
	 * @param diagnosticoRelacionado2Cie Tipo Cie del diagnostico 
	 * relacionado 2
	 * @param acronimoDiagnosticoRelacionado3 Acronimo del diagnostico 
	 * relacionado 3
	 * @param diagnosticoRelacionado3Cie Tipo Cie del diagnostico 
	 * relacionado 3
	 * @param codigoTipoIdentificacionMedico C�digo del tipo de identificaci�n
	 * del m�dico
	 * @param numeroIdentificacionMedico N�mero de identificaci�n del 
	 * m�dico
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int crearEgresoDesdeEvolucionTransaccional (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie, String estado) throws SQLException;
	
	/**
	 * Este m�todo cargar el n�mero de autorizaci�n presente en la admisi�n,
	 * ya sea hospitalaria o urgencias (Se especifica en el parametro tipoAdmision
	 * y sus valores validos son 'h' y 'u')
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param codigoAdmision
	 * @param tipoAdmision Caracter donde se define si el c�digo dado corresponde
	 * a una admisi�n hospitalaria o de urgencias ('h' y 'u')
	 * @return
	 * @throws SQLException
	 */
	public String cargarNumeroAutorizacionAdmision(Connection con, int codigoAdmision, char tipoAdmision) throws SQLException;
	
	/**
	 * Este m�todo cargar la causa externa presente en la admisi�n,
	 * ya sea hospitalaria o urgencias (Se especifica en el parametro tipoAdmision
	 * y sus valores validos son 'h' y 'u')
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @param tipoAdmision Caracter donde se define si el c�digo dado corresponde
	 * a una admisi�n hospitalaria o de urgencias ('h' y 'u')
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarCausaExternaUltimaValoracionUH (Connection con, int idCuenta, char tipoAdmision) throws SQLException;
	
	/**
	 * M�todo que carga los datos propios de un egreso con excepci�n
	 * del c�digo de admisi�n y la cama
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEgresoGeneral (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que cargar todos los datos necesarios para una reversi�n de egreso
	 * (Datos de la cama de la admisi�n de hospitalizados, fecha/hora egreso y medico
	 * responsable)
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEgresoReversionEgreso (Connection con, int idCuenta) throws SQLException;

	/**
	 * Este m�todo modifica el egreso creado desde la orden de salida,
	 * a�adiendole nuevas fechas, n�mero de autorizaci�n y el usuario
	 * responsable del egreso
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @param fechaEgreso Fecha en la que se realiz� el egreso 
	 * (Definido por el usuario)
	 * @param horaEgreso Hora en la que se realiz� el egreso 
	 * (Definido por el usuario)
	 * @param fechaGrabacion Fecha en la que se realiz� el egreso 
	 * (del sistema) 
	 * @param horaGrabacion Fecha en la que se realiz� el egreso 
	 * (del sistema) 
	 * @param numeroAutorizacion N�mero de autorizaci�n
	 * @param loginUsuario Login del usuario que genero /termino 
	 * este egreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int modificarEgresoUsuarioFinalizarTransaccional (Connection con, int idCuenta, String fechaEgreso, String horaEgreso, String fechaGrabacion, String horaGrabacion, String numeroAutorizacion, String loginUsuario, String estado) throws SQLException;

	/**
	 * M�todo que actualiza el motivo de reversi�n, en caso que se presente
	 * una reversi�n de egreso
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @param idPersonaRealizaReversion C�digo de la persona que realiza la 
	 * reversi�n de egreso
	 * @param motivoReversionEgreso Cadena de texto donde se explica el M�tivo 
	 * de la reversi�n del egreso 
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */	
	public int actualizarPorReversionEgresoTransaccional (Connection con, int idCuenta, int idPersonaRealizaReversion, String motivoReversionEgreso, String estado) throws SQLException;
	
	/**
	 * M�todo que actualiza los datos del motivo de reversion de egreso que
	 * conciernen a epicrisis
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta C�digo de la cuenta asociada a la que pertenece este egreso
	 * @param deboMostrarMotivoReversionEpicrisis Boolean que indica si el motivo
	 * de reversion del egreso debe mostrarse en la epicrisis
	 * @param idEvolucion Evolucion en la que se debe mostrar el motivo en la
	 * epicrisis
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarInformacionMotivoReversionTransaccional (Connection con, int idCuenta, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException;
	
	/**
	 * M�todo que actualiza el boolean que define si el motivo de reversion va o no
	 * a la epicrisis
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param deboMostrarMotivoReversionEpicrisis Boolean que indica si el motivo
	 * de reversion del egreso debe mostrarse en la epicrisis
	 * @param idEvolucion Evolucion que actualizo el boolean de motivo de reversion
	 * de egreso
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int actualizarInformacionMotivoReversionSoloBooleanTransaccional (Connection con, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException;
//	* codigoDiagnosticoPrincipal String
//	* codigoDiagnosticoPrincipalCie int
//	* nombreDiagnosticoPrincipal String
//	* fechaEgreso String
//	* horaEgreso String 
	
	public ResultSetDecorator getBasicoEgreso(Connection con, int numeroCuenta) throws SQLException;
	
	/**
	 * M�todo que completa un semi-egreso 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroCuenta N�mero de la cuenta a la que se desea
	 * completar el semi-egreso
	 * @param loginUsuario login del usuario que realiz� esta actualizaci�n
	 * @return
	 * @throws SQLException
	 */
	public int completarSemiEgreso (Connection con, int numeroCuenta, String loginUsuario, String acronimo,int tipocie,int codigoTipoMonitoreo) throws SQLException;

	/**
	 * M�todo que reversa un semi-egreso 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroCuenta N�mero de la cuenta en la que se desea
	 * reversar el semi-egreso
	 * @return
	 * @throws SQLException
	 */
	public int reversarSemiEgreso (Connection con, int numeroCuenta) throws SQLException;
	/**
	 * Adici�n de Sebasti�n
	 * M�todo que pemrite consultar un semiEgreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public Collection cargarSemiEgreso (Connection con,int idCuenta);
	
	/**
	 * Adici�n sebasti�n
	 * M�todo que carga los datos b�sicos de una reversi�n de un egreso
	 * @param con
	 * @param idCuenta
	 * @return Colecci�n con la fecha y hora del egreso (apenas tiene esos campos)
	 */
	public Collection cargarReversionEgreso(Connection con,int idCuenta);
	
	/**
	 * Metodo que carga la fecha - hora de egreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public ResultSetDecorator cargarFechaHoraEgreso(Connection con, int idCuenta);
	
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
	public HashMap consultaDatosFactura (Connection connection,HashMap datos);
	
	
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
	public boolean actualizarNuevosDatosEgreso (Connection connection,HashMap datos);
	
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
	public HashMap consultarNuevosDatosEgreso (Connection connection,String cuenta);
	
	/**
	 * Metodo encargado de consultar si existe boleta de salida
	 * @param connection
	 * @param cuenta
	 *  @return false/true
	 */
	public boolean consultarExisteBoletaSalida(Connection connection,int idCuenta);
	
}