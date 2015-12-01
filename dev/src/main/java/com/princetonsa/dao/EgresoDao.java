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
	 * Método que elimina un egreso dado el número de la cuenta a la
	 * que pertenece. Se utiliza solo para borrar Egresos automáticos, 
	 * de ahí su nombre
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta Código de la cuenta asociada a la que pertenece este egreso
	 * @return 0 si no se pudo borrar correctamente, 1 de lo contrario
	 * @throws SQLException
	 */
	public int borrarEgresoAutomatico(Connection con, int numeroCuenta) ;
	
	/**
	 * Método que inserta un egreso automático
	 *  
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta Código de la cuenta a la que se va a crear el egreso 
	 * @param loginUsuario
	 * @return
	 * @throws SQLException
	 */
	public int insertarEgresoAutomatico(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,int causaExterna) throws SQLException;
	
	/**
	 * Implementación de la inserción automática de un egreso cuando en una valoración se 
	 * selecciona conducta a seguir hospitalizar en piso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarEgresoAutomatico(Connection, int, String) 
	 */
	public int insertarEgresoAutomaticoValoracionHospitalizarEnPiso(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,String fechaEgreso,String horaEgreso, int tipoMonitoreo) throws SQLException;
	
	public int borrarPreEgreso(Connection con, int numeroCuenta) ;
	
	public int insertarPreEgreso(Connection con, int numeroCuenta, int codigoMedico) ;
	
	public boolean existeEgresoEfectivo(Connection con, int numeroCuenta) ;
		
	/**
	 * Método que inserta los datos iniciales del egreso, cuando el médico da
	 * orden de salida
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta a la que se va a crear el egreso 
	 * @param idEvolucion Código de la evolución desde la cual se dió orden de salida
	 * @param estadoSalida Estado del paciente a la salida
	 * @param destinoSalida Destino del paciente a la salida
	 * @param otroDestinoSalida Campo de texto donde se especifica el 
	 * destino del paciente a la salida, si el campo destinoSalida se refiere a "otros" 
	 * @param numeroAutorizacion Número de autorización
	 * @param causaExterna Causa externa del ingreso a la institución
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
	 * @param codigoMedico Código del médico
	 * @return
	 * @throws SQLException
	 */	
	public int crearEgresoDesdeEvolucion (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie) throws SQLException;
	
	/**
	 * Método que inserta los datos iniciales del egreso, cuando el médico da
	 * orden de salida . Soporta hacer parte de una transacción
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta a la que se va a crear el egreso 
	 * @param idCuenta Código de la cuenta a la que se va a crear el egreso 
	 * @param idEvolucion Código de la evolución desde la cual se dió orden de salida
	 * @param estadoSalida Estado del paciente a la salida
	 * @param destinoSalida Destino del paciente a la salida
	 * @param otroDestinoSalida Campo de texto donde se especifica el 
	 * destino del paciente a la salida, si el campo destinoSalida se refiere a "otros" 
	 * @param numeroAutorizacion Número de autorización
	 * @param causaExterna Causa externa del ingreso a la institución
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
	 * @param codigoTipoIdentificacionMedico Código del tipo de identificación
	 * del médico
	 * @param numeroIdentificacionMedico Número de identificación del 
	 * médico
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int crearEgresoDesdeEvolucionTransaccional (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie, String estado) throws SQLException;
	
	/**
	 * Este método cargar el número de autorización presente en la admisión,
	 * ya sea hospitalaria o urgencias (Se especifica en el parametro tipoAdmision
	 * y sus valores validos son 'h' y 'u')
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param codigoAdmision
	 * @param tipoAdmision Caracter donde se define si el código dado corresponde
	 * a una admisión hospitalaria o de urgencias ('h' y 'u')
	 * @return
	 * @throws SQLException
	 */
	public String cargarNumeroAutorizacionAdmision(Connection con, int codigoAdmision, char tipoAdmision) throws SQLException;
	
	/**
	 * Este método cargar la causa externa presente en la admisión,
	 * ya sea hospitalaria o urgencias (Se especifica en el parametro tipoAdmision
	 * y sus valores validos son 'h' y 'u')
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta asociada a la que pertenece este egreso
	 * @param tipoAdmision Caracter donde se define si el código dado corresponde
	 * a una admisión hospitalaria o de urgencias ('h' y 'u')
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarCausaExternaUltimaValoracionUH (Connection con, int idCuenta, char tipoAdmision) throws SQLException;
	
	/**
	 * Método que carga los datos propios de un egreso con excepción
	 * del código de admisión y la cama
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta asociada a la que pertenece este egreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEgresoGeneral (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Método que cargar todos los datos necesarios para una reversión de egreso
	 * (Datos de la cama de la admisión de hospitalizados, fecha/hora egreso y medico
	 * responsable)
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta asociada a la que pertenece este egreso
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEgresoReversionEgreso (Connection con, int idCuenta) throws SQLException;

	/**
	 * Este método modifica el egreso creado desde la orden de salida,
	 * añadiendole nuevas fechas, número de autorización y el usuario
	 * responsable del egreso
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta asociada a la que pertenece este egreso
	 * @param fechaEgreso Fecha en la que se realizó el egreso 
	 * (Definido por el usuario)
	 * @param horaEgreso Hora en la que se realizó el egreso 
	 * (Definido por el usuario)
	 * @param fechaGrabacion Fecha en la que se realizó el egreso 
	 * (del sistema) 
	 * @param horaGrabacion Fecha en la que se realizó el egreso 
	 * (del sistema) 
	 * @param numeroAutorizacion Número de autorización
	 * @param loginUsuario Login del usuario que genero /termino 
	 * este egreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int modificarEgresoUsuarioFinalizarTransaccional (Connection con, int idCuenta, String fechaEgreso, String horaEgreso, String fechaGrabacion, String horaGrabacion, String numeroAutorizacion, String loginUsuario, String estado) throws SQLException;

	/**
	 * Método que actualiza el motivo de reversión, en caso que se presente
	 * una reversión de egreso
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta asociada a la que pertenece este egreso
	 * @param idPersonaRealizaReversion Código de la persona que realiza la 
	 * reversión de egreso
	 * @param motivoReversionEgreso Cadena de texto donde se explica el Mótivo 
	 * de la reversión del egreso 
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */	
	public int actualizarPorReversionEgresoTransaccional (Connection con, int idCuenta, int idPersonaRealizaReversion, String motivoReversionEgreso, String estado) throws SQLException;
	
	/**
	 * Método que actualiza los datos del motivo de reversion de egreso que
	 * conciernen a epicrisis
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param idCuenta Código de la cuenta asociada a la que pertenece este egreso
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
	 * Método que actualiza el boolean que define si el motivo de reversion va o no
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
	 * Método que completa un semi-egreso 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroCuenta Número de la cuenta a la que se desea
	 * completar el semi-egreso
	 * @param loginUsuario login del usuario que realizó esta actualización
	 * @return
	 * @throws SQLException
	 */
	public int completarSemiEgreso (Connection con, int numeroCuenta, String loginUsuario, String acronimo,int tipocie,int codigoTipoMonitoreo) throws SQLException;

	/**
	 * Método que reversa un semi-egreso 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroCuenta Número de la cuenta en la que se desea
	 * reversar el semi-egreso
	 * @return
	 * @throws SQLException
	 */
	public int reversarSemiEgreso (Connection con, int numeroCuenta) throws SQLException;
	/**
	 * Adición de Sebastián
	 * Método que pemrite consultar un semiEgreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public Collection cargarSemiEgreso (Connection con,int idCuenta);
	
	/**
	 * Adición sebastián
	 * Método que carga los datos básicos de una reversión de un egreso
	 * @param con
	 * @param idCuenta
	 * @return Colección con la fecha y hora del egreso (apenas tiene esos campos)
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