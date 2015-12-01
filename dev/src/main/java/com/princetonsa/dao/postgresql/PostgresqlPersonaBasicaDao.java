/*
 * @(#)PostgresqlPersonaBasicaDao.java
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
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PersonaBasicaDao;
import com.princetonsa.dao.sqlbase.SqlBasePersonaBasicaDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Implementación en PostgreSQL de la <i>interface</i>
 * <code>PersonaBasicaDao</code>, que proporciona los servicios de acceso a BD
 * para <code>PersonaBasica</code>.
 *
 * @version 1.0, Mar 27, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;pez</a>
 */

public class PostgresqlPersonaBasicaDao implements PersonaBasicaDao
{

	/**
	 * Busca el responsable del paciente en las tablas deudorco o paciente
	 */
	private static final String cargarPacienteConsultaResponsableStr="select getnomdeudoringreso(?) ";

	/**
	 * Implementación de cargar persona básica para PostgreSQL.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargar(Connection, String, String)
	 */
	public void cargar(Connection con, String tipoId, String numeroId, PersonaBasica personaBasica) throws SQLException
	{
		SqlBasePersonaBasicaDao.cargar(con, tipoId, numeroId, personaBasica) ;
	}

	/**
	 * Implementación de cargar persona básica para PostgreSQL.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargar(Connection, int)
	 */
	public boolean cargar(Connection con, int codigoPersona, PersonaBasica personaBasica) throws SQLException
	{
		return SqlBasePersonaBasicaDao.cargar(con, codigoPersona, personaBasica) ;
	}

	/**
	* Implementación de cargar persona básica para PostgreSQL.
	* @see com.princetonsa.dao.PersonaBasicaDao#cargarPaciente2 (Connection , String , String , String, String ) throws SQLException
	*/
	public Hashtable cargarPaciente2(Connection con, String tipoId, String numeroId, String codigoInstitucion, String codigoCentroAtencion,boolean validarCentroAtencion) throws SQLException
	{
		return SqlBasePersonaBasicaDao.cargarPaciente2(con, tipoId, numeroId, codigoInstitucion, codigoCentroAtencion, cargarPacienteConsultaResponsableStr,validarCentroAtencion) ;
	}


	/**
	* Implementación de cargar persona básica con institución para 
	* PostgreSQL.
	* @see com.princetonsa.dao.PersonaBasicaDao#cargarPaciente2 (Connection , int , String ) throws SQLException
	*/
	public Hashtable cargarPaciente2(
		Connection	ac_con,
		int			ai_codigoPersona,
		String		as_codigoInstitucion,
		String codigoCentroAtencion,boolean validarCentroAtencion
	)throws SQLException
	{
		return SqlBasePersonaBasicaDao.cargarPaciente2( ac_con, ai_codigoPersona, as_codigoInstitucion,codigoCentroAtencion, cargarPacienteConsultaResponsableStr,validarCentroAtencion);
	}

	/**
	* Implementación de carga el último contrato y su tarifario oficial en 
	* una BD Postgresql.
	* 
	* @see com.princetonsa.dao.PersonaBasicaDao#getCodigoUltimoContrato (Connection , int ) throws SQLException
	*/
	public int getCodigoUltimoContrato (Connection con, int codigoConvenio) throws SQLException
	{
		return SqlBasePersonaBasicaDao.getCodigoUltimoContrato (con, codigoConvenio) ;
	}

	/**
	 * Método para verificar si el paciente tiene o no manejo conjunto
	 * @param con
	 * @param codigoPersona
	 * @return true si el paciente tiene manejo conjunto
	 */
	public boolean tieneManejoConjunto(Connection con, int codigoPersona)
	{
		return SqlBasePersonaBasicaDao.tieneManejoConjunto(con, codigoPersona);
	}


	public void cargarEmpresaRazon(Connection con, int cod, String codRegimen, PersonaBasica personaBasica) throws SQLException {
		String cargarNombresRazonsocialEmpresaPacientestr="SELECT getEmpresaResponsable(?) as empresa,getNomRegimen(?) as regimen";
		SqlBasePersonaBasicaDao.cargarEmpresaRazon(con,cod,codRegimen,cargarNombresRazonsocialEmpresaPacientestr, personaBasica);
	}
	
	/**
	 * Implementación del método que dado el código de una
	 * cuenta devuelve la fecha y hora de apertura de la misma,
	 * si esta existe
	
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getFechaHoraApertura (Connection , int ) throws SQLException
	 */
	public String getFechaHoraApertura (Connection con, int idCuenta) throws SQLException
	{
	    return SqlBasePersonaBasicaDao.getFechaHoraApertura (con, idCuenta,DaoFactory.POSTGRESQL) ;
	}
	

	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, valida que las cuentas al ingreso sean Cuenta valida
	 *  
	 * @param int codigoPaciente	
	 * */
	public ArrayList cargarOtrosIngresosPaciente(Connection con, 
													int codigoPaciente)
	{
		return SqlBasePersonaBasicaDao.cargarOtrosIngresosPaciente(con, codigoPaciente);
	}

	@Override
	public ArrayList<DtoOtrosIngresosPaciente> consultaTodosIngresos(Connection con, int codigoPaciente)
	{
		return SqlBasePersonaBasicaDao.consultaTodosIngresos(con, codigoPaciente);
	}

	/**
	* Carga la informacion del Ingreso dado por la busqueda de Otros Ingresos
	* @param Connection con
	* @param String ingreso 
	*/
	public Hashtable cargarPacienteXingreso(Connection con,
												 String ingreso)
	{
		return SqlBasePersonaBasicaDao.cargarPacienteXingreso(con, ingreso, cargarPacienteConsultaResponsableStr);
	}
	
	/**
	 * Metodo para consultar si un ingreso esta marcado como Preingreso Pendiente
	 * @param codigoIngreso
	 * @return
	 */
	public boolean consultarPreingresoP(int codigoIngreso)
	{
		return SqlBasePersonaBasicaDao.consultarPreingresoP(codigoIngreso);
	}
	
	/**
	 * Metodo que consulta si un Ingreso es un Reingreso
	 */
	public boolean consultarReingreso(int codigoIngreso)
	{
		return SqlBasePersonaBasicaDao.consultarReingreso(codigoIngreso);
	}
	
	/**
	 * Implementación de cargar persona básica para Oracle.
	 * @see com.princetonsa.dao.PersonaBasicaDao#cargar(Connection, int)
	 */
	public void cargarObjeto(Connection con, PersonaBasica personaBasica) throws SQLException {
		SqlBasePersonaBasicaDao.cargarObjeto(con, personaBasica) ;
	}
	
	/**
	 * @see com.princetonsa.dao.PersonaBasicaDao#obtenerViaEgreso(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerViaEgreso(Connection con,String codigoPaciente){
		return SqlBasePersonaBasicaDao.obtenerViaEgreso(con, codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.PersonaBasicaDao#consultarEspecialidadMedicoXSolicitud(java.sql.Connection, java.lang.Integer)
	 */
	public String consultarEspecialidadMedicoXSolicitud(Connection con,Integer solciitud){
		return SqlBasePersonaBasicaDao.consultarEspecialidadMedicoXSolicitud(con,solciitud);
	}

	/**
	 * @see com.princetonsa.dao.PersonaBasicaDao#consultarFechaHoraIngreso(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public String consultarFechaHoraIngreso(Connection con,String codigoPaciente,String idIngreso){
		return SqlBasePersonaBasicaDao.consultarFechaHoraIngreso(con, codigoPaciente, idIngreso);
	}

}