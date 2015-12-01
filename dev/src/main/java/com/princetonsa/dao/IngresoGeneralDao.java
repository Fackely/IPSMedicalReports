/*
 * @(#)IngresoGeneralDao.java
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.Answer;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoIngresosFactura;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>IngresoGeneral</code>.
 *
 * @version 1.0, Oct 7, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface IngresoGeneralDao {

	/**
	 * Inserta en la fuente de datos un nuevo Ingreso General.
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoPaciente C�digo que identifica al paciente
	 * en el sistema
	 * @param institucion C�digo de la instituci�n donde se realiza
	 * este ingreso
	 * @return n�mero de ingresos efectuados (debe ser 1)
	 */
	public int insertarIngreso(Connection con, int codigoPaciente, String institucion, String estado, String usuario,String consecutivo,String anioConsecutivo,int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso,String transplante) throws SQLException;
	
	/**
	 * Inserta en la fuente de datos un nuevo Ingreso General usando
	 * m�todo Transaccional (Permite definir en que estado de la transacci�n
	 * est�).
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoPaciente C�digo que identifica al paciente
	 * en el sistema
	 * @param institucion C�digo de la instituci�n donde se realiza
	 * este ingreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return n�mero de ingresos efectuados (debe ser 1)
	 */
	public int insertarIngresoTransaccional(Connection con, int codigoPaciente, String institucion, String estado, String estadoIngreso, String usuario,String consecutivo,String anioConsecutivo,int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso,String transplante) throws SQLException;
	
	/**
	 * Carga la informaci�n relevante a un ingreso general y la almacena en su objeto respectivo.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param idIngreso n�mero de identificaci�n del ingreso
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los datos del ingreso
	 * y una conexi�n abierta con la fuente de datos
	 */
	public Answer cargarIngreso(Connection con, String idIngreso) throws SQLException;

	/**
	 * Carga la informaci�n relevante a un ingreso general y la almacena en su objeto respectivo.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param numeroIdentificacionPaciente n�mero de identificaci�n del paciente
	 * @param tipoIdentificacionPaciente tipo de identificaci�n del paciente
	 * @param codigoInstitucion c�digo de la instituci�n de la cual se desea cargar un ingreso
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los datos del ingreso
	 * y una conexi�n abierta con la fuente de datos
	 */
	public Answer cargarIngreso(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException;

	/**
	 * Modifica la informaci�n de un Ingreso General en la fuente de datos.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param anioIngreso a�o en que se efect�a el ingreso
	 * @param mesIngreso mes en que se efect�a el ingreso
	 * @param diaIngreso d�a en que se efect�a el ingreso
	 * @param horaIngreso hora en que se efect�a el ingreso
	 * @param codigoTipoPaciente c�digo del tipo de paciente
	 * @param codigoViaIngreso c�digo de la v�a de ingreso
	 * @param estadoIngreso estado del ingreso
	 * @param idIngreso n�mero de identificaci�n del ingreso
	 * @return n�mero de ingresos modificados en la fuente de datos
	 */
	public int modificarIngreso(Connection con, String anioEgreso, String mesEgreso, String diaEgreso, String horaEgreso, String idIngreso) throws SQLException;

	/**
	 * M�todo que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public int modificarFechaHoraIngreso(Connection con, String idIngreso);
	
	/**
	 * Modifica la informaci�n de un Ingreso General en la fuente de datos.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param anioIngreso a�o en que se efect�a el ingreso
	 * @param mesIngreso mes en que se efect�a el ingreso
	 * @param diaIngreso d�a en que se efect�a el ingreso
	 * @param horaIngreso hora en que se efect�a el ingreso
	 * @param codigoTipoPaciente c�digo del tipo de paciente
	 * @param codigoViaIngreso c�digo de la v�a de ingreso
	 * @param estadoIngreso estado del ingreso
	 * @param numeroIdentificacionPaciente n�mero de identificaci�n del paciente
	 * @param tipoIdentificacionPaciente tipo de identificaci�n del paciente
	 * @param codigoInstitucion c�digo de la instituci�n de la cual se desea modificar un ingreso
	 * @return n�mero de ingresos modificados en la fuente de datos
	 */
	public int modificarIngreso(Connection con, String anioEgreso, String mesEgreso, String diaEgreso, String horaEgreso, int codigoPaciente, String codigoInstitucion) throws SQLException;

	/**
	 * Este m�todo se encarga de arrancar una transaccion al mejor modo
	 * de estados y dem�s (sirve si no se tiene un objeto que inicie la transacci�n)
	 * @param con una conexi�n abierta con una fuente de datos.
	 * @return
	 * @throws SQLException
	 */
	public int empezarTransaccion(Connection con) throws SQLException;
	
	/**
	 * M�todo que obtiene todos los ingresos de un paciente para
	 * una instituci�n en particular
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator getCodigosIngresosPaciente(Connection con, int codigoPaciente, int codigoInstitucion) throws SQLException;
	
	/**
	 * metodo que deja vacias (abierta) la fecha y hora de egreso en la table ingresos
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean reversarFechaHoraEgreso(Connection con, int idCuenta);
	
	
	/**
	 * M�todo para nsertar los datos adicional para la poliza del convenio que lo requiera
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int ingresarInfoAdicionalPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario); 
	
	/**
	 * M�todo para nsertar los datos adicional para la poliza del convenio que lo requiera
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int ingresarInfoAdicionalPolizaBasica(Connection con, int codigoConvenio, int idCuenta, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario);
	
	/**
	 * M�todo para consultar los datos del titular de la poliza
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarInformacionTitularPoliza(Connection con, int idCuenta);
	
	/**
	 * Metodo para consultar los datos de autorizacion de la poliza
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap consultarDatosAutorizacionPoliza(Connection con, int codigoConvenio, int idCuenta);
	
	
	
	/**
	 * Meodo para actualizar los datos del titular de la poliza segun la cuenta
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param usuario
	 * @return
	 */
	public int actualizarInfoTitularPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono); 
	
	
	/**
    * M�todo apra actualizar los datos de la autorizacion de la poliza segun una cuenta dada
    * @param con
    * @param idCuenta
    * @param fechaAutorizacion
    * @param numeroAutorizacion
    * @param valorMonto
    * @return
    */

	public int actualizarDatosAutorizacion(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo, String numeroAutoAux, int montoAux);
	 
	/**
	 * M�todo para actualizar los datos de la poliza
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int actualizarInformacionPoliza(Connection con, int idCuenta, int codigoConvenio, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo , String numeroAutoAux, int montoAux);
	
	/**
	 * Elmimanr un monto autrorizado
	 * @param con
	 * @param codigo
	 * @return
	 */
	public  int eliminarDatosInformacionPoliza(Connection con, int codigo);
	
	/**
	 * retorna el codigo de persona
	 * @param con
	 * @param idIngreso
	 */
	public int cargarPacienteDadoIngreso(Connection con, String idIngreso);
	
	/**
	 * metodo para cargar los ingresos y cuentas corresponduientes dados los estados,
	 * key={idingreso, nombreestadoingreso, codigoestadocuenta, nombreestadocuenta, codigoviaingreso, nombreviaingreso}
	 * @param con
	 * @param estadosIngresoVector
	 * @param estadosCuentaVector
	 * @return
	 */
	public HashMap obtenerIngresosCuenta(Connection con, Vector estadosIngresoVector, Vector estadosCuentaVector);
	
	/**
	 * M�todo que carga un ingreso incompleto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap<String, Object> cargarIngresoIncompleto(Connection con,String codigoPaciente);
	
	/**
	 * M�todo que realiza la anulacion de un ingreso que estaba incompleto por garant�as
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean anulacionIngresoIncompleto(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta el consecutivo del ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public String getConsecutivoIngreso(Connection con,String idIngreso);

	/**
	 * M�todo que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean actualizarEstadoIngreso(Connection con, String idIngreso, String estado, String loginUsuario);
	
	/**
	 * M�todo que modifica el ingreso insertando la hora y fecha actual de la BD ademas del indicativo de control post operatorio.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean actualizarControlPostOperatorioCx(Connection con, String idIngreso, String estado, String loginUsuario);

	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean existeIngresoAbierto(Connection con, String codigoPaciente, String idIngresoRestriccion);
	
	/**
	 * Metodo encargado de consultar si un ingreso fue por
	 * entidades subcontratadas o no.
	 * Adicionado por Jhony Alexander Duque A.
	 * 21/01/2008
	 * @param connection
	 * @param codigoIngreso
	 * @return S/N
	 * 
	 */
	public String esIngresoComoEntidadSubContratada (Connection connection, int codigoIngreso);
	
	/**
	 * 
	 * @param con
	 * @param mapaRestricciones
	 * @return
	 */
	public HashMap<Object, Object> cargarListadoIngresos(Connection con, HashMap<Object, Object> mapaRestricciones);
	
	/**
	 * Metodo encargado de actualizar el campo transplante de la
	 * tabla ingresos. 
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * --transplante --> puede ser (DONANT,RECEPT)
	 * --ingreso
	 * @return true/false
	 */
	public boolean actualizarTransplante (Connection connection, HashMap criterios);
	
	/**
	 * Metodo para obtener los ingresos para factura odontologica dado un paciente,
	 * en este caso la cuenta puede estar activa y los ingresos en estado abierto o cerrado
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public ArrayList<DtoIngresosFactura> obtenerIngresosFacturaOdontologica(int codigoPaciente);
	
}