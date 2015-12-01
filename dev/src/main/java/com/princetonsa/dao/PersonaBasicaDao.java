/*
 * @(#)PersonaBasicaDao.java
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
import java.util.Hashtable;

import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implemntar
 * un objeto que preste los servicios de acceso de BD para
 * <code>PersonaBasica</code>
 *
 * @version 1.0, Mar 27, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;pez</a>
 */

public interface PersonaBasicaDao {

	/**
	 * Carga los datos básicos de una persona desde una fuente de datos.
	 *
	 * @param con una conexión abierta con la fuente de datos
	 * @param tipoId tipo de identificación (CC, TI, etc.)
	 * @param numeroId número de la identificación
	 */
	public void cargar(Connection con, String tipoId, String numeroId, PersonaBasica personaBasica) throws SQLException;

	/**
	 * Carga los datos básicos de una persona desde una fuente de datos.
	 *
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigoPersona Código de la persona que se piensa
	 * cargar
	 */
	public boolean cargar(Connection con, int codigoPersona, PersonaBasica personaBasica) throws SQLException;

	/**
	 * Este método debe retornar una tabla de Hash con los datos básicos
	 * del paciente, esta tabla debe tener las siguientes llaves:
	 *
	 * codigoTipoRegimen: El código del Tipo de regimen
	 * convenioPersonaResponsable: Nombre del convenio o del paciente
	 * responsable si entro como particular
	 * codigoIngreso: Código de ingreso abierto del paciente
	 * codigoCuenta: Código de la cuenta abierto del paciente
	 * codigoAdmision: Código de la admisión abierta del paciente
	 * anioAdmision: Año de la admisión de urgencias abierta del paciente
	 * codigoViaIngreso: Código de la vía de ingreso del paciente
	 * viaIngreso: Texto con la vía de ingreso del paciente
	 *
	 *
	 * Si alguno de los campos no existe (Ej no hay cuenta abierta, el
	 * hash debe tener un String "" en esa llave)
	 *
	 * @param con una conexión abierta con la fuente de datos
	 * @param tipoId tipo de identificación (CC, TI, etc.)
	 * @param numeroId número de la identificación
	 * @param codigoInstitucion Código de la institución en la que está la
	 * persona
	 * @param codigoCentroAtencion
	 * @return
	 * @throws SQLException
	 */
	Hashtable cargarPaciente2(Connection con, String tipoId, String numeroId, String codigoInstitucion,String codigoCentroAtencion,boolean validarCentroAtencion) throws SQLException;

	/**
	* Este método debe retornar una tabla de Hash con los datos básicos del paciente, esta tabla
	* debe tener las siguientes llaves:
	*
	* codigoTipoRegimen: El código del Tipo de regimen
	* convenioPersonaResponsable: Nombre del convenio o del paciente
	* responsable si entro como particular
	* codigoIngreso: Código de ingreso abierto del paciente
	* codigoCuenta: Código de la cuenta abierto del paciente
	* codigoAdmision: Código de la admisión abierta del paciente
	* anioAdmision: Año de la admisión de urgencias abierta del paciente
	* codigoViaIngreso: Código de la vía de ingreso del paciente
	* viaIngreso: Texto con la vía de ingreso del paciente
	* codigoTarifarioOficial : Código del Tarifario Oficial (Def. si es ISS o
	* SOAT )
	* codigoConvenio: Código del convenio del paciente 
	* codigoContrato: Código del contrato del paciente 
	* Si alguno de los campos no existe (Ej no hay cuenta abierta, el hash debe tener un String
	* "" en esa llave)
	*
	* @param ac_con					Conexión abierta con la fuente de datos
	* @param ai_codigoPersona		Identificador de la persona
	* @param as_codigoInstitucion	Código de la institución en la que está la persona
	* @param codigoCentroAtencion
	* @throws SQLException
	*/
	Hashtable cargarPaciente2(Connection ac_con, int ai_codigoPersona, String as_codigoInstitucion,String codigoCentroAtencion,boolean validarCentroAtencion)
	throws SQLException;
	
	/**
	 * Metodo que retorna la razon y la empresa de un paciente
	 */
	public void cargarEmpresaRazon(Connection con,int cod,String codRegimen, PersonaBasica personaBasica)throws SQLException;
	/**
	 * Método que obtiene el último contrato (y su respectivo tipo de
	 * tarifario) y lo devuelve en un arreglo (la primera posición es el
	 * codigoTarifarioOficial y la segunda posición codigoContrato)
	 * 
	 * @param con Conexión abierta con la fuente de datos
	 * @param codigoConvenio Código del convenio al cual se quiere
	 * sacar su último contrato
	 * @return
	 * @throws SQLException
	 */
	public int getCodigoUltimoContrato (Connection con, int codigoConvenio) throws SQLException;
	
	/**
	 * Método para verificar si el paciente tiene o no manejo conjunto
	 * @param con
	 * @param codigoPersona
	 * @return true si el paciente tiene manejo conjunto
	 */
	public boolean tieneManejoConjunto(Connection con, int codigoPersona);
	
	/**
	 * Método que dado el código de una cuenta devuelve 
	 * la fecha y hora de apertura de la misma, si esta existe
	
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public String getFechaHoraApertura (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, valida que las cuentas al ingreso sean Cuenta valida
	 *  
	 * @param int codigoPaciente
	 * */
	public ArrayList cargarOtrosIngresosPaciente(Connection con, 
												int codigoPaciente);
	
	/**
	* Carga la informacion del Ingreso dado por la busqueda de Otros Ingresos
	* @param Connection con
	* @param String ingreso 
	*/
	public Hashtable cargarPacienteXingreso(Connection con,
												 String ingreso);
	/**
	 * Metodo para consultar si un ingreso esta marcado como Preingreso Pendiente
	 * @param codigoIngreso
	 * @return
	 */	
	public boolean consultarPreingresoP(int codigoIngreso);
	
	/**
	 * Metodo para consultar si un Ingreso es un Reingreso
	 * @param codigoIngreso
	 * @return
	 */
	public boolean consultarReingreso(int codigoIngreso);

	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, no valida estados de cuenta
	 *
	 * @param con Conexion con la BD
	 * @param int codigoPaciente 
	 * @return Retorna ArrayList<DtoOtrosIngresosPaciente> con el listado de los ingresos
	 */
	public ArrayList<DtoOtrosIngresosPaciente> consultaTodosIngresos(Connection con, int codigoPaciente);
	
	
	
	
	/**
	 * Carga los datos básicos de una persona desde una fuente de datos.
	 * Este método retorna desde el nivel de la base de datos el objeto, evitando perder la referencia del resultSet 
	 *
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigoPersona Código de la persona que se piensa
	 * cargar
	 */
	public void cargarObjeto(Connection con, PersonaBasica personaBasica) throws SQLException;
	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public  String obtenerViaEgreso(Connection con,String codigoPaciente);
	
	/**
	 * @param con
	 * @param solciitud
	 * @return lista de especialidades
	 */
	public String consultarEspecialidadMedicoXSolicitud(Connection con,Integer solciitud);

	/**
	 * @param con
	 * @param codigoPaciente
	 * @param idIngreso
	 * @return fecha y hora de ingreso 
	 */
	public String consultarFechaHoraIngreso(Connection con,String codigoPaciente,String idIngreso);
}