/*
 * @(#)OracleAdmisionHospitalariaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.dao.AdmisionHospitalariaDao;
import com.princetonsa.dao.sqlbase.SqlBaseAdmisionHospitalariaDao;

/**
 * Esta clase implementa el contrato estipulado en
 * <code>AdmisionHospitalariaDao</code>, proporcionando los servicios de acceso
 * a una base de datos Oracle requeridos por la clase
 * <code>AdmisionHospitalaria</code>
 *
 *
 * @version 1.0, Mar 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 */
public class OracleAdmisionHospitalariaDao implements AdmisionHospitalariaDao
{//SIN PROBAR FUNC. SECUENCIA
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una
	 * Admision Hospitalaria usando una BD Oracle.
	 */
	// "numero_autorizacion , " +
	private static final String insertarAdmisionHospitalaria="insert into admisiones_hospi (" +
		"codigo , " +
		"consecutivo, " +
		"origen_admision_hospitalaria , " +
		"fecha_admision , " +
		"hora_admision , " +
		"fecha_grabacion, " +
		"hora_grabacion, " +
		"codigo_medico , " +
		"cama , " +
		"estado_admision , " +
		"diagnostico_admision, " +
		"diagnostico_CIE_admision , " +
		"causa_externa , " +			
		"login_usuario , " +
		"cuenta) values (seq_admisiones_hospi.nextval, ?, ?, ?, ?, CURRENT_DATE, to_char(sysdate,'HH24:MI'), ?, ?, 1, ?, ?, ?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar el código
	 * de una Admision Hospitalaria recien insertada usando una BD Oracle. 
	 */
	private static final String buscarCodigoAdmisionRecienInsertada="select seq_admisiones_hospi.currval as codigoAdmision from dual" ;

	private static final String insertarTrasladoCamaStr = "INSERT INTO his_cama_pac "
		+ "(codigo, codigo_paciente, codigo_admision, codigo_cama, fecha_traslado, hora_traslado, login_usuario) "
		+ "VALUES( seq_his_cama_pac.nextval, ?, ?, ?, ?, ?, ? )";
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar los datos basicos de una admision hospitalaria dado el numero de cuenta
	 */
	private static final String consultarBasicoAdmisionStr = "" +
	"SELECT codigo as idAdmision, fecha_admision || '' as fechaAdmision, substr(hora_admision || '' ,0,6) as horaAdmision, origen_admision_hospitalaria as origenAdmision " +
	"FROM admisiones_hospi " +
	"WHERE cuenta = ?";

	/**
	 * Método que implementa la inserción de una Admision Hospitalaria en
	 * una BD Oracle
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#insertar(Connection , int , String , String ,int , String , String , int , String , String , int , String , String ) 
	 */
	public int insertar(Connection con, int origen, int codigoMedico, int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha) throws SQLException
	{
	    return SqlBaseAdmisionHospitalariaDao.insertar(con, origen, codigoMedico, codigoCama, codigoDiagnostico, codigoCIEDiagnostico, codigoCausaExterna, /*numeroAutorizacion, */loginUsuario, cuenta, hora, fecha, insertarAdmisionHospitalaria, buscarCodigoAdmisionRecienInsertada);
	}


	/**
	 * Método que implementa la inserción de una Admision Hospitalaria en una BD
	 * Oracle, permitiendo al usuario definir la transaccionalidad
	 * 
 	 * @see com.princetonsa.dao. AdmisionHospitalariaDao#insertarTransaccional (Connection , int , String , String , int , String , String , int , String , String , int , String , String , String ) 
	 */

	public int insertarTransaccional(Connection con, int origen, int codigoMedico, int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha, String estado, int institucion) throws SQLException
	{
	    return SqlBaseAdmisionHospitalariaDao.insertarTransaccional(con, origen, codigoMedico, codigoCama, codigoDiagnostico, codigoCIEDiagnostico, 
	    		codigoCausaExterna, /*numeroAutorizacion, */loginUsuario, cuenta, hora, fecha, insertarAdmisionHospitalaria,
	    		buscarCodigoAdmisionRecienInsertada,estado,institucion) ;
	}


	/**
	 * Método que implementa la consulta de una Admision Hospitalaria en una
	 * BD Oracle
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#cargar(Connection, int)
	 */
	public ResultSetDecorator cargar(Connection con, int codigoAdmisionHospitalaria) throws SQLException{
		return SqlBaseAdmisionHospitalariaDao.cargar(con, codigoAdmisionHospitalaria);
	}

	/**
	 * Método que implementa la modificación de una Admision Hospitalaria en
	 * una BD Oracle
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#modificar(Connection, int, String, String, int, String, int)
	 */
	public int modificar(Connection con,  int origen, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */int codigoAdmisionHospitalaria) throws SQLException
	{
		return SqlBaseAdmisionHospitalariaDao.modificar(con, origen, codigoDiagnostico, codigoCIEDiagnostico, codigoCausaExterna, /*numeroAutorizacion, */codigoAdmisionHospitalaria);
	}
	
	/**
	 * Método que implementa el paso de la cama de una Admision Hospitalaria a
	 * estado desinfección en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#pasarAdmisionAEgresoTransaccional (Connection , int , String ) throws SQLException
	 */
	public int pasarAdmisionAEgresoTransaccional (Connection con, int numeroAdmision, String estado, int institucion) throws SQLException
	{
		return SqlBaseAdmisionHospitalariaDao.pasarAdmisionAEgresoTransaccional(con, numeroAdmision, estado, institucion);
	}
	
	/**
	 * Método que implementa el efecto de una reversión de egreso en una 
	 * Admision Hospitalaria en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#reversarEgresoYAdmisionTransaccional (Connection , int , int , String) throws SQLException
	 */
	public int reversarEgresoYAdmisionTransaccional (Connection con, int idCuenta, int codigoCama, String estado) throws SQLException
	{
		return SqlBaseAdmisionHospitalariaDao.reversarEgresoYAdmisionTransaccional(con, idCuenta, codigoCama, estado);
	}

	/**
	 * Ingresa una cama a una admision
	 * @param 	Connection, con
	 * @param 	int, Codigo Cama
	 * @param 	int, Codigo Admision
	 */
	public int cambiarCama(Connection con, int cama, int admision)
	{
		return SqlBaseAdmisionHospitalariaDao.cambiarCama(con, cama, admision);
	}
	
	public int actualizarMedico(Connection con, int medico, int admision)
	{
		return SqlBaseAdmisionHospitalariaDao.actualizarMedico(con, medico, admision);
	}

	/**
	 * Implementación de la consulta de datos basicos de una admision hospitalaria
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#getBasicoAdmision (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */		
	public ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta) throws SQLException
	{
	    return SqlBaseAdmisionHospitalariaDao.getBasicoAdmision(con, numeroCuenta, consultarBasicoAdmisionStr) ;
	} 
	
	/**
	 * Carga en una cadena la fecha y hora de la última asignación o cambio
	 * de cama para el paciente dado.
	 * @param 	Connection, con
	 * @param 	String, tipoIdPaciente. Tipo de identificación del paciente
	 * @param 	String, numeroIdPaciente. Número de identificación del paciente
	 * @param 	int, codAdmision. Código de admisión del paciente.
	 * @return 		String, Cadena con la fecha y la hora --> fecha"- "hora
	 * @throws 	SQLException
	 */	
	public String cargarUltimaFechaHoraRegistroCama(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAdmisionHospitalariaDao.cargarUltimaFechaHoraRegistroCama(con, codigoPaciente);
	}
	
	/**
	 * Mètodo que obtiene la información de la cama actual del paciente, para ser 
	 * postulada en la información del paciente que se encuentra en el cabezote superior
	 * @param con
	 * @param codigoAdmision
	 * @param codigoPaciente
	 */
	public String[] getCama(Connection con, int codigoAdmision, int codigoPaciente) throws SQLException
	{
	    return SqlBaseAdmisionHospitalariaDao.getCama(con, codigoAdmision, codigoPaciente) ;
	}
	
 	public ResultSetDecorator cargarUltimaAdmision(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAdmisionHospitalariaDao.cargarUltimaAdmision(con, codigoPaciente);
	}
 	
	/**
	 * Método para obtener los datos de la cama actual en la que está el paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return Collection -> Con los datos de la cama actual del paciente
	 */
	public Collection consultarDatosCamaActual(Connection con, int codigoPaciente)
	{
		return SqlBaseAdmisionHospitalariaDao.consultarDatosCamaActual(con, codigoPaciente);
	}
	
}
