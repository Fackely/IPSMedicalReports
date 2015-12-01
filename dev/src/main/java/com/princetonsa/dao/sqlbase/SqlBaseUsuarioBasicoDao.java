/*
 * @(#)SqlBaseUsuarioBasicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.MD5Hash;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.LogCadupwdUsuarios;
import com.servinte.axioma.orm.LogCadupwdUsuariosHome;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a UsuarioBasico
 *
 *	@version 1.0, Mar 29, 2004
 */
/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseUsuarioBasicoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUsuarioBasicoDao.class);
	
	/**
	* Cadena constante con el <i>statement</i> necesario para obtener los datos de un usuario
	* básico, dado su código
	*/
	private static final String is_cargarUsuarioBasico ="SELECT " +
		"p.codigo AS codigoPersona, " +
		"u.login AS loginUsuario, " +
		"u.password AS passwordUsuario, " +
		"p.numero_identificacion AS numeroIdentificacion, " +
		"p.tipo_identificacion AS codigoTipoIdentificacion, " +
		"u.institucion AS codigoInstitucion, " +
		"p.primer_nombre || ' ' ||  p.segundo_nombre || ' ' || p.primer_apellido || ' ' ||  p.segundo_apellido AS nombreCompleto, " +
		"CASE WHEN u.cargo IS NULL THEN '' ELSE getnomcargousuario(u.cargo) END AS nombre_cargo," +
		"coalesce(u.cargo,"+ConstantesBD.codigoNuncaValido+") AS codigo_cargo " +
		"FROM usuarios u, " +
		"personas p " +
		"WHERE p.codigo=? AND " +
		"u.codigo_persona=p.codigo ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para obtener los datos de un usuario básico, dado su login.
	 */
	private static final String cargarUsuarioBasicoStr = "SELECT " +
		"codigo_persona as codigoPersona, " +
		"login AS loginUsuario, " +
		"password AS passwordUsuario, " +
		"p.numero_identificacion AS numeroIdentificacion, " +
		"p.tipo_identificacion AS codigoTipoIdentificacion, " +
		"u.institucion as codigoInstitucion, " +
		"primer_nombre || ' ' || segundo_nombre || ' ' || primer_apellido || ' ' || segundo_apellido as nombreCompleto, " +
		"CASE WHEN u.cargo IS NULL THEN '' ELSE getnomcargousuario(u.cargo) END AS nombre_cargo, " +
		"coalesce(u.cargo,"+ConstantesBD.codigoNuncaValido+") AS codigo_cargo " +
		"FROM usuarios u, " +
		"personas p " +
		"WHERE login = ? and  " +
		"u.codigo_persona=p.codigo ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para obtener los datos de un usuario básico, dado su tipo y número de identificación.
	 */
	private static final String cargarUsuarioBasicoStr2 = "SELECT " +
		"codigo_persona as codigoPersona, " +
		"login AS loginUsuario, " +
		"password AS passwordUsuario, " +
		"p.numero_identificacion AS numeroIdentificacion, " +
		"p.tipo_identificacion AS codigoTipoIdentificacion, " +
		"u.institucion as codigoInstitucion, " +
		"primer_nombre || ' ' || segundo_nombre || ' ' || primer_apellido || ' ' || segundo_apellido as nombreCompleto, " +
		"CASE WHEN u.cargo IS NULL THEN '' ELSE getnomcargousuario(u.cargo) END AS nombre_cargo ," +
		"coalesce(u.cargo,"+ConstantesBD.codigoNuncaValido+") AS codigo_cargo " +
		"FROM usuarios u, " +
		"personas p " +
		"WHERE p.tipo_identificacion = ? and " +
		"p.numero_identificacion = ? and " +
		"u.codigo_persona=p.codigo ";
	
	/**
	 * Cadena que consulta los centros de costo del usuario
	 */
	private static final String cargarCentrosCostoUsuarioStr = "SELECT "+ 
		"cu.centro_costo AS codigo, "+
		"getnomcentrocosto(cu.centro_costo) As nombre, "+ 
		"ca.consecutivo AS codigocentroatencion, "+
		"ca.descripcion As nombrecentroatencion "+ 
		"FROM centros_costo_usuario cu "+
		"INNER JOIN centros_costo cc ON(cc.codigo=cu.centro_costo) "+
		"INNER JOIN centro_atencion ca ON(cc.centro_atencion=ca.consecutivo) "+ 
		"WHERE cu.usuario = ? ORDER BY nombre";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cambiar el password de un usuario basico.
	 */
	private static final String cambiarPasswordStr = "UPDATE usuarios SET password = ?,password_activo='"+ConstantesBD.acronimoSi+"',fecha_ultima_activa_passwd=current_date WHERE login = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar si existe un usuario con el login dado.
	 */
	private static final String existeUsuarioStr = "SELECT COUNT(1) AS numResultados FROM usuarios WHERE login = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar el
	 * número de registro como médico de este usuario (si es médico).
	 */
	private static final String cargarInformacionMedica="" +
		"select * from (" + 
		"select med.numero_registro as numeroRegistroMedico, med.firma_digital as firmadigital, cat.nombre as ocupacionMedica, cat.codigo as codigoOcupacionMedica, 'ninguna' as especialidad, 0 as codigoEspecialidad, " + ValoresPorDefecto.getValorFalseParaConsultas() + " as especialidadActiva from medicos med, ocupaciones_medicas cat where med.ocupacion_medica=cat.codigo and med.codigo_medico=?  " +
		"UNION  " +
		"SELECT '0' as numeroregistromedico, '' as firmadigital,  'N/A' as ocupacionMedica, 0 as codigoOcupacionMedica, esp.nombre as especialidad, esp.codigo as codigoEspecialidad, espmed.activa_sistema as especialidadActiva from especialidades_medicos espmed, especialidades esp where espmed.codigo_especialidad=esp.codigo and espmed.codigo_medico=? ) tablat " +
		"order by codigoOcupacionMedica desc";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar cuantos roles tiene un usuario particular.
	 */
	private static final String cuantosRolesTienePacienteStr="SELECT count(1) as numResultados from roles_usuarios where login=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para saber si un usuario particular es paciente y solo tiene como rol "paciente"
	 */
	private static final String RolPacienteStr="SELECT ru.nombre_rol as rol from roles_usuarios ru, usuarios us, pacientes pac where us.login=ru.login and us.codigo_persona=pac.codigo_paciente and ru.login=?";

	/**
	 * Cadena para recuperar el nombre de la institución dado el código
	 */
	private static final String nombreInstitucionStr="SELECT razon_social, nit, digito_verificacion from instituciones WHERE codigo=?";
	/**
	 * Cadena para listar los usuarios de un mismo profesional
	 */
	private static final String cargarUsuariosMismoProfesionalStr="SELECT "+ 
		"us.login as login, "+
		"pe.primer_nombre || ' ' ||  pe.segundo_nombre || ' ' || pe.primer_apellido || ' ' ||  pe.segundo_apellido AS nombrecompleto, "+
		"pe.tipo_identificacion as tipoid, "+
		"pe.numero_identificacion as numeroid, " +
		"us.institucion as institucion "+
		"FROM usuarios us "+ 
		"INNER JOIN personas pe ON(pe.codigo=us.codigo_persona) "+ 
		"WHERE "+ 
		"pe.numero_identificacion=? AND "+
		"pe.tipo_identificacion=?";
	
	
	
	/**
	 * Consulta de carga si un usuario tiene rol o no segun el ID Dado
	 */
	private static final String consultarTieneRolDadoStr="select count(u.login) TIENE_ROL " +
			"from usuarios u inner join roles_usuarios ru " +
			"on (u.login = ru.login) inner join roles_funcionalidades rf " +
			"on (ru.nombre_rol=rf.nombre_rol) " +
			"inner join funcionalidades f " +
			"on (rf.codigo_func=f.codigo_func) " +
			"where  " +
			"f.codigo_func=? " +
			"and u.login =? ";
	
	
	
	/**
	 * Cargar el nombre de la institución a la cual pertenece el usuario
	 * @param codigoInstitucion
	 * @return Nombre de la institución
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarNombreInstitucion(Connection con, String codigoInstitucion) throws SQLException
	{
		PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(nombreInstitucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		pst.setInt(1, Utilidades.convertirAEntero(codigoInstitucion));
		return new ResultSetDecorator(pst.executeQuery());
	}
	
	/**
	* Recupera, desde una fuente de datos, los datos basicos de un usuario.
	* @param ac_con				Conexión abierta con una fuente de datos
	* @param ai_codigoPersona	Codigo del usuario que se desea cargar
	* @return un <code>ResultSet</code> con los datos del usuario y una conexion abierta
	*/
	public static ResultSetDecorator cargarUsuarioBasico(Connection ac_con, int ai_codigoPersona)throws SQLException
	{
		PreparedStatementDecorator lps_ps;

		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_cargarUsuarioBasico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		lps_ps.setInt(1, ai_codigoPersona);

		return new ResultSetDecorator(lps_ps.executeQuery());
	}
	
	/**
	 * Recupera, desde una base de datos Genérica, los datos basicos de un usuario.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param login el login del usuario que se desea cargar
	 * @return un <code>ResultSet</code> con los datos del usuario
	 */
	public static ResultSetDecorator cargarUsuarioBasico (Connection con, String login) throws SQLException 
	{
		PreparedStatementDecorator cargarUsuarioBasicoStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarUsuarioBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarUsuarioBasicoStatement.setString(1, login);
		ResultSetDecorator rs = new ResultSetDecorator(cargarUsuarioBasicoStatement.executeQuery());
		return rs;
	}

	/**
	 * Recupera, desde una base de datos Genérica, los datos basicos de un usuario.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 * @return un <code>ResultSet</code> con los datos del usuario
	 */
	public static ResultSetDecorator cargarUsuarioBasico (Connection con, String tipoId, String numeroId) throws SQLException 
	{
		PreparedStatementDecorator cargarUsuarioBasicoStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarUsuarioBasicoStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarUsuarioBasicoStatement.setString(1, tipoId);
		cargarUsuarioBasicoStatement.setString(2, numeroId);
		ResultSetDecorator rs = new ResultSetDecorator(cargarUsuarioBasicoStatement.executeQuery());
		return rs;
	}

	/**
	 * Cambia el password de un usuario.
	 * @param login login el login del usuario del cual se desea cambiar el password
	 * @param newPassword el nuevo password del usuario
	 * @return el nuevo password del usuario, <i>hashed</i> con el algoritmo MD5
	 */
	public static String cambiarPassword (Connection con, final String login, final String newPassword,String usuarioActual) throws SQLException 
	{
		String newHashedPwd = MD5Hash.hashPassword(newPassword);
		int resp=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		pst =  con.prepareStatement(cambiarPasswordStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
		pst.setString(1, newHashedPwd);
		pst.setString(2, login);
		resp = pst.executeUpdate();
		pst.close();
		try{
			HibernateUtil.beginTransaction();
			Usuarios usu=new Usuarios();
			UsuariosDelegate usuDao=new UsuariosDelegate();
			usu=usuDao.findById(login);
			
			LogCadupwdUsuarios dto=new LogCadupwdUsuarios();
			LogCadupwdUsuariosHome dao=new LogCadupwdUsuariosHome();
			if(UtilidadTexto.getBoolean(usu.getActivo()+""))
				dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoActivo);
			else
				dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoInactivo);
			dto.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
			dto.setHoraProceso(UtilidadFecha.getHoraActual());
			dto.setEsactivacion(ConstantesBD.acronimoSi);
			dto.setTipoInactivacion(ConstantesIntegridadDominio.acronimoManual);
			dto.setUsuariosByLoginUsuario(usu);
			
			Usuarios usuAct=new Usuarios();
			usuAct=usuDao.findById(usuarioActual);
			
			
			dto.setUsuariosByUsuarioProcesa(usuAct);
			dao.persist(dto);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR ", e);
			HibernateUtil.abortTransaction();
		}
		return (resp == 1) ? newHashedPwd : "";
	}

	/**
	 * Cambia el password de un usuario. Este método es llamado únicamente
	 * desde la funcionalidad "administrador - cambiar contraseña", ya que cambia el password de "cualquiera" que tenga el login dado.
	 * @param login login el login del usuario del cual se desea cambiar el password
	 * @param newPassword el nuevo password del usuario
	 * @param change parámetro dummy, para que este método tenga una <i>signature</i> diferente al anterior.
	 * @return <b>true</b> si se pudo cambiar el password, <b>false</b> si no.
	 */
	public static boolean cambiarPassword (Connection con, final String login, final String newPassword, final boolean change,String usuarioActual) throws SQLException 
	{
		if (change); // NOP para evitar el warning "The argument change is never read"
		boolean wasChanged = false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst2=null;
		try{
			String newHashedPwd = MD5Hash.hashPassword(newPassword);
			
			pst =  con.prepareStatement(existeUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, login);
			rs = pst.executeQuery();
			if (rs.next() && rs.getInt("numResultados") == 1) 
			{
				pst2 = con.prepareStatement(cambiarPasswordStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setString(1, newHashedPwd);
				pst2.setString(2, login);
				wasChanged = (pst2.executeUpdate() == 1);
				pst2.close();
				try{
					HibernateUtil.beginTransaction();
					Usuarios usu=new Usuarios();
					UsuariosDelegate usuDao=new UsuariosDelegate();
					usu=usuDao.findById(login);
					
					LogCadupwdUsuarios dto=new LogCadupwdUsuarios();
					LogCadupwdUsuariosHome dao=new LogCadupwdUsuariosHome();
					if(UtilidadTexto.getBoolean(usu.getActivo()+""))
						dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoActivo);
					else
						dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoInactivo);
					dto.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
					dto.setHoraProceso(UtilidadFecha.getHoraActual());
					dto.setEsactivacion(ConstantesBD.acronimoSi);
					dto.setTipoInactivacion(ConstantesIntegridadDominio.acronimoManual);
					dto.setUsuariosByLoginUsuario(usu);
					
					Usuarios usuAct=new Usuarios();
					usuAct=usuDao.findById(usuarioActual);
					dto.setUsuariosByUsuarioProcesa(usuAct);
					dao.persist(dto);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error("ERROR cambiarPassword Hibernate", e);
					HibernateUtil.abortTransaction();
				}
			}
			else 
			{
				wasChanged = false;
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR cambiarPassword", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return wasChanged;
	}

	/**
	 * Implementación en Genérica de la cargada de Número de Registro Médico
	 * @see com.princetonsa.dao.UsuarioBasicoDao#cargarDatosMedico(Connection, int)
	 */
	public static ResultSetDecorator cargarDatosMedico (Connection con, int codigoPersona) throws SQLException
	{
		PreparedStatementDecorator buscarNumeroRegistroMedicoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarInformacionMedica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		buscarNumeroRegistroMedicoStatement.setInt(1, codigoPersona);
		buscarNumeroRegistroMedicoStatement.setInt(2, codigoPersona);
		logger.info("\n\n\n Consulta Datos medico>>"+cargarInformacionMedica + " CodPersona "+codigoPersona);
		return new ResultSetDecorator(buscarNumeroRegistroMedicoStatement.executeQuery());
	}

	/**
	 * Implementación del método que permite saber si un usuario es paciente
	 * y solo tiene el rol paciente en una BD Genérica
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagValoresPorDefecto (Connection , String ) throws SQLException
	 */
	public static boolean esUsuarioSoloPaciente (Connection con, String login) throws SQLException
	{
		//Primero revisamos el número de roles que tiene este paciente
		//si tiene más de 1 o menos de 1, sabemos que no es solo paciente
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		boolean result=false;
		try{
			pst= con.prepareStatement(cuantosRolesTienePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, login);
			rs=pst.executeQuery();
			int numRoles=0;
			if (rs.next())
			{
				numRoles=rs.getInt("numResultados");
				if (numRoles>1||numRoles<1){
					result=false;
				}
				else{
					String rol="";
					//Solo tiene un rol, hay que revisar si es el de paciente
					//o no
					pst2= con.prepareStatement(RolPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst2.setString(1, login);
					rs2=pst2.executeQuery();
					if (rs2.next())
					{
						rol=rs2.getString("rol");
						if (rol!=null&&rol.equals("paciente")){
							result=true;
						}
						else{
							result=false;
						}
					}
					else{
						result=false;
					}
				}
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR esUsuarioSoloPaciente", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}
	
	/**
	 * adición sebastián
	 * Método que lista todos los usuarios que hacen parte de un mismo 
	 * profesional o usuarios del sistema
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Collection cargarUsuariosMismoProfesional(Connection con, String tipoId, String numeroId,String activo)
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection result=null;
		try{
			String consulta=cargarUsuariosMismoProfesionalStr;
			if(activo.equals("true"))
				consulta+=" AND us.login NOT IN (SELECT login FROM usuarios_inactivos)";
			else if(activo.equals("false"))
				consulta+=" AND us.login IN (SELECT login FROM usuarios_inactivos)";
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(2,tipoId);
			pst.setString(1,numeroId);
			rs=new ResultSetDecorator(pst.executeQuery());
			result=UtilidadBD.resultSet2Collection(rs);
		}
		catch(Exception e){
			Log4JManager.error("ERROR cargarUsuariosMismoProfesional", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}
	
	/**
	 * Método implementado para cargar los centros de costo de un usuario
	 * @param con
	 * @param login
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarCentrosCostoUsuario(Connection con,String login)
	{
		HashMap mapaRetorno=null;
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			pst = new PreparedStatementDecorator(con.prepareStatement(cargarCentrosCostoUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,login);
			rs=new ResultSetDecorator(pst.executeQuery());
			mapaRetorno=UtilidadBD.cargarValueObject(rs,true,false);
		}
		catch(Exception e){
			Log4JManager.error("ERROR cargarCentrosCostoUsuario", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return mapaRetorno;
	}
	
	/**
	 * @param con
	 * @param loginUsuario
	 * @return si tiene un rol dado 
	 */
	public static Boolean consultarTieneRol(Connection con,String loginUsuario, Integer codigoFuncionalidad){
		Boolean tieneRol = false;
		Integer cantidad = new Integer(0);
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			pst = con.prepareStatement(consultarTieneRolDadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoFuncionalidad);
			pst.setString(2,loginUsuario);
			rs = pst.executeQuery();
			if(rs.next()){
				cantidad = rs.getInt("TIENE_ROL");
			}
			if(cantidad>0){
				tieneRol=true;
			}
		}catch(Exception e){
			Log4JManager.error("ERROR consultarTieneRol", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}		
		return tieneRol;
		
	}
	
	
}
