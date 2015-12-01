/*
 * @(#)SqlBaseUsuarioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.axioma.util.log.Log4JManager;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.MD5Hash;
import util.RespuestaInsercionPersona;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Persona;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.LogActiinactiUsuarios;
import com.servinte.axioma.orm.LogActiinactiUsuariosHome;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Usuario
 *
 *	@version 1.0, Mar 30, 2004
 */
public class SqlBaseUsuarioDao 
{

	/**
	 * Logs para informar erores
	 */
	//private static Logger logger = Logger.getLogger(SqlBaseUsuarioDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un usuario en la tabla de usuarios.
	 */
	private static final String insertarUsuarioStr = "INSERT INTO usuarios (login, password, codigo_persona, institucion, cargo, contrato_interfaz,fecha_creacion,hora_creacion,usuario_creacion,fecha_ultima_activa_usu,fecha_ultima_activa_passwd,dias_caducidad_password,dias_inactivar_usuario,activo,password_activo,fecha_modifica,hora_modifica,usuario_modifica) values (?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que actualiza el campo cago de la tabla usuarios 
	 */
	private static final String updateUsuarioStr="UPDATE usuarios SET cargo=?,contrato_interfaz=?,activo=?,fecha_ultima_activa_usu=?,fecha_ultima_inact_usu=?,dias_caducidad_password=?,dias_inactivar_usuario=?,fecha_modifica=?,hora_modifica=?,usuario_modifica=?   WHERE login=?";

	/**
	 * Cadena que inserta los centros de costo del usuario
	 */
	private static final String insertarCentrosCostoUsuarioStr = " INSERT INTO administracion.centros_costo_usuario (usuario,centro_costo) VALUES(?,?) ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar los roles de un usuario.
	 */
	private static final String agregarRolesStr = "INSERT INTO administracion.roles_usuarios  values (?,?)";

	/**
	 * Cadena que elimina los centros de costo de un usuario
	 */
	private static final String eliminarCentrosCostoUsuarioStr = " DELETE FROM centros_costo_usuario WHERE usuario = ? ";

	/**
	 * Cadena que consulta el número de centros de costo que tiene un usuario
	 */
	private static final String numeroCentrosCostoUsuarioStr = "SELECT count(1) as cuenta from centros_costo_usuario WHERE usuario = ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para obtener los datos de un usuario.
	 * El Join para sacar la institucion no se hace con la tabla instituciones, ya que el nombre
	 * de la institucion se encuentra en empresas. Se cambio para hacerla más eficiente a través
	 * del INNER JOIN, para gastarse esta eficiencia usando LEFT OUTER JOIN para permitir usuarios
	 * sin rol
	 */
	private static final String cargarUsuarioStr = "SELECT "+
		"per.codigo AS codigoPersona, "+ 
		"per.numero_identificacion AS numeroIdentificacion, "+ 
		"per.tipo_identificacion AS codigoTipoIdentificacion, "+ 
		"administracion.getnombretipoidentificacion(per.tipo_identificacion) AS tipoIdentificacion, "+ 
		"per.codigo_pais_nacimiento AS codigoPaisIdentificacion, "+ 
		"per.codigo_departamento_nacimiento AS codDepartamentoIdentificacion, "+ 
		"per.codigo_ciudad_nacimiento AS codigoCiudadIdentificacion, "+ 
		"administracion.getdescripcionpais(per.codigo_pais_nacimiento) as paisIdentificacion, "+ 
		"administracion.getnombreciudad(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento,per.codigo_ciudad_nacimiento) as ciudadIdentificacion, "+
		"administracion.getnombredepto(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento) AS deptoIdentificacion, "+
		"per.tipo_persona as codigoTipoPersona, "+ 
		"tp.nombre as tipoPersona, "+ 
		"to_char(per.fecha_nacimiento, 'YYYY-MM-DD') as fechaNacimiento, "+ 
		"per.estado_civil as codigoEstadoCivil, "+ 
		"estc.nombre as estadoCivil, "+ 
		"per.sexo as codigoSexo, "+ 
		"administracion.getdescripcionsexo(per.sexo) as sexo, "+ 
		"per.primer_nombre as primerNombrePersona, "+ 
		"per.segundo_nombre as segundoNombrePersona, "+ 
		"per.primer_apellido as primerApellidoPersona, "+ 
		"per.segundo_apellido as segundoApellidoPersona, "+ 
		"per.direccion as direccion, "+ 
		"per.codigo_pais_vivienda as codigoPais, "+ 
		"per.codigo_departamento_vivienda as codigoDepartamento, "+ 
		"per.codigo_ciudad_vivienda as codigoCiudad, "+
		"administracion.getdescripcionpais(per.codigo_pais_vivienda) as pais, "+ 
		"administracion.getnombreciudad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda,per.codigo_ciudad_vivienda) as ciudad, "+
		"administracion.getnombredepto(per.codigo_pais_vivienda,per.codigo_departamento_vivienda) AS departamento, "+
		"coalesce(per.codigo_barrio_vivienda||'','') as codigoBarrio, "+ 
		"coalesce(getdescripcionbarrio(per.codigo_barrio_vivienda),'') as barrio, "+ 
		"per.telefono as telefono, "+ 
		"per.email as email, "+ 
		"coalesce (us.cargo||'','') as codcargo, "+
		"getNomCargoUsuario(us.cargo) as nomcargo, "+
		"us.login as loginUsuario, "+ 
		"us.password as passwordUsuario, "+ 
		"ru.nombre_rol as rolUsuario, "+ 
		"us.institucion as codigoInstitucion, "+ 
		"ins.razon_social as institucion, "+ 
		"per.codigo_pais_id AS codigoPaisId, "+ 
		"per.codigo_depto_id AS codigoDeptoId, "+ 
		"per.codigo_ciudad_id AS codigoCiudadId, "+
		"getdescripcionpais(per.codigo_pais_id) as nombrePaisId, "+
		"administracion.getnombreciudad(per.codigo_pais_id,per.codigo_depto_id,per.codigo_ciudad_id) AS nombreCiudadId, "+
		"administracion.getnombredepto(per.codigo_pais_id,per.codigo_depto_id) AS nombreDeptoId," +
		"coalesce(per.indicativo_interfaz,'') AS indicativoInterfaz," +
		"us.contrato_interfaz AS contratoInterfaz," +
		"cu.numero_contrato AS numeroContrato ," +
		"per.telefono_fijo as telefonoFijo ," +
		"per.telefono_celular as telefonoCelular," +
		"us.fecha_creacion as fechaCreacion," +
		"us.hora_creacion as horaCreacion," +
		"us.usuario_creacion as usuarioCreacion," +
		"us.fecha_ultima_activa_usu as fechaUltimaActivacionUsuario," +
		"us.fecha_ultima_activa_passwd as fechaUltimaActivacionPassword," +
		"us.dias_caducidad_password as diasCaducidadPasswd," +
		"us.dias_inactivar_usuario as diasInactivarUsuario," +
		"us.activo as activo," +
		"us.fecha_ultima_inact_usu as fechaUltimaInactivacionUsuario," +
		"us.fecha_ultima_caducidad_passwd as fechaUltimaCaducidadPasswd," +
		"us.password_activo as passwdactivo "+ 
		"FROM administracion.personas per "+ 
		"INNER JOIN administracion.usuarios us ON (per.codigo=us.codigo_persona) "+ 
		"INNER JOIN administracion.tipos_personas tp ON (per.tipo_persona=tp.codigo ) "+ 
		"LEFT OUTER JOIN administracion.estados_civiles estc ON (per.estado_civil=estc.acronimo) "+ 
		"INNER JOIN administracion.instituciones ins ON(us.institucion=ins.codigo) "+ 
		"LEFT OUTER JOIN  administracion.roles_usuarios ru ON (us.login=ru.login )" +
		"LEFT OUTER JOIN interfaz.contratos_nomina cu ON (cu.codigo_pk=us.contrato_interfaz) "+ 
		"WHERE us.login=?";
	
	/**
	 * Cadena que consulta los centros de costo del usuario
	 */
	private static final String cargarCentrosCostoUsuarioStr = " SELECT "+ 
		"cu.centro_costo AS codigo, "+
		"getnomcentrocosto(cu.centro_costo) As nombre, "+ 
		"ca.consecutivo AS codigocentroatencion, "+
		"ca.descripcion As nombrecentroatencion "+ 
		"FROM centros_costo_usuario cu "+
		"INNER JOIN centros_costo cc ON(cc.codigo=cu.centro_costo) "+
		"INNER JOIN centro_atencion ca ON(cc.centro_atencion=ca.consecutivo) "+ 
		"WHERE cu.usuario = ? ORDER BY nombre ";

	
	
	
	/**
	 * Inserta un usuario en una base de datos Genérica. Usa una conexion abierta. Si no existe, la crea.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param loginUsuario login del usuario en el sistema
	 * @param passwordUsuario contraseña del usuario en el sistema
	 * @param rolesUsuario rol(es) del usuario en el sistema
	 * @param numeroIdentificacion numero de identificacion del usuario
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion del usuario
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedida la identificacion del usuario
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedida la identificacion del usuario
	 * @param codigoTipoPersona tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del usuario
	 * @param mesNacimiento mes de nacimiento del usuario
	 * @param anioNacimiento año de nacimiento del usuario
	 * @param codigoEstadoCivil estado civil del usuario
	 * @param codigoSexo sexo del usuario
	 * @param primerNombrePersona primer nombre del usuario
	 * @param segundoNombrePersona segundo nombre del usuario
	 * @param primerApellidoPersona primer apellido del usuario
	 * @param segundoApellidoPersona segundo apellido del usuario
	 * @param direccion direccion de la residencia del usuario
	 * @param codigoDepartamento departamento donde reside el usuario
	 * @param codigoCiudad codigo de la ciudad donde reside el usuario
	 * @param codigoBarrio codigo del barrio de residencia del usuario
	 * @param telefono telefono del usuario
	 * @param email correo electronico del usuario
	 * @param codigoCago  codigo del cargo que tiene el usuario
	 * @param codigoInstitucion institucion a la que pertenece este usuario
	 * @param centrosCosto Mapa con los centros de costo del usuario
	 * @param diasCaducidadPWD 
	 * @param diasInactivarUsuario 
	 * @param fechaUltimaActivacionPWD 
	 * @param fechaUltimaActivacionUsuario 
	 * @param x 
	 * @param x 
	 * @param usuarioCreacion 
	 * @return numero de filas insertadas en la BD
	 */
	@SuppressWarnings("rawtypes")
	public static int insertarUsuario(Connection con, String loginUsuario, String passwordUsuario, String[] rolesUsuario, String numeroIdentificacion, String codigoTipoIdentificacion,
			String codigoDeptoId, String codigoCiudadId, String codigoPaisId, 
			String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, 
			String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, 
			String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, 
			String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String telefono, String email, String codigoCargo,
			String codigoInstitucion, HashMap centrosCosto,String codigoLocalidad, String indicativoInterfaz, String contratoInterfaz, int diasInactivarUsuario, int diasCaducidadPWD, String usuarioCreacion, String fechaCreacion, String horaCreacion, String fechaUltimaActivacionUsuario, String fechaUltimaActivacionPWD)			
	{
		try{
				int resp, resp1, resp2, resp0, resp3 = 1, resp4, resp5, i, codigoMedico=0;
				int numResultados=0;
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				
				//****************INICIAR TRANSACCION******************************************
				con.setAutoCommit(false);
				myFactory.beginTransaction(con);
				resp0=1;
				
				//**********INSERCIÓN DE PERSONA (EN EL CASO DE QUE NO EXISTA)**************************************
				// Solo se debe insertar en personas si no existe una previamente
				// El caso en que se quiera agregar un usuario con la misma
				// identificacion, ya esta manejado por la clase
				// util.UtilidadValidacion

				Log4JManager.info("codDeptoId=>*"+codigoDeptoId+"*");
				Log4JManager.info("codCiudadId=>*"+codigoCiudadId+"*");
				Log4JManager.info("codPaisId=>*"+codigoPaisId+"*");
				Log4JManager.info("codDeptoIdentificacion=>*"+codigoDepartamentoIdentificacion+"*");
				Log4JManager.info("codCiudadIdentificacion=>*"+codigoCiudadIdentificacion+"*");
				Log4JManager.info("codPaisIdentificacion=>*"+codigoPaisIdentificacion+"*");
				Log4JManager.info("codDepto=>*"+codigoDepartamento+"*");
				Log4JManager.info("codPais=>*"+codigoPais+"*");
				Log4JManager.info("codCiudad=>*"+codigoCiudad+"*");
				Log4JManager.info("codCargo=>*"+codigoCargo+"*");

				
				String deboInsertar = "select codigo as codigoPersona from administracion.personas where tipo_identificacion=? and numero_identificacion=?";
				PreparedStatementDecorator deboInsertarStatement =  new PreparedStatementDecorator(con.prepareStatement(deboInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				deboInsertarStatement.setString(1, codigoTipoIdentificacion);
				deboInsertarStatement.setString(2, numeroIdentificacion);
		
				ResultSetDecorator rs = new ResultSetDecorator(deboInsertarStatement.executeQuery());
				
				while (rs.next()) {
					numResultados = rs.getInt("codigoPersona");
				}
				// Solo si no hay una persona insertada, hacemos la insercion en persona
				if (numResultados==0) 
				{
					RespuestaInsercionPersona respuesta = Persona.insertarPersona(con,numeroIdentificacion, codigoTipoIdentificacion, codigoDeptoId, codigoCiudadId,  codigoPaisId, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, "", telefono, email,"", ConstantesBD.tipoPersonaUsuario,0,"");
					//En ocasiones debemos cambiar el valor del numero de identificacion
					//aca preguntamos si debemos hacerlo
					codigoMedico=respuesta.getCodigoPersona();
					if (respuesta.isNecesitaCambioIdentificacion()) 
					{
						numeroIdentificacion = respuesta.getNuevaIdentificacion();
					}
					//Dependiendo de la respuesta recibida,  asignamos el valor de resp1
					if (respuesta.isSalioBien()) 
					{
						resp1 = 1;
					}
					else {
						resp1 = 0;
					}
		
				}
				//Si ya existía se modifican los datos
				else 
				{
					codigoMedico=numResultados;
					resp1 = Persona.modificarPersona(con, codigoTipoIdentificacion, numeroIdentificacion, codigoDeptoId, codigoCiudadId, codigoPaisId, codigoMedico, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPaisIdentificacion, codigoBarrio, codigoLocalidad, telefono, email,"", ConstantesBD.continuarTransaccion, codigoTipoPersona, Integer.parseInt(codigoInstitucion),"");
					
					
				}
				
				
				//*********INSERTAR EL USUARIO*************************************************************************
				Log4JManager.info("INSERCION DE USUARIO: "+insertarUsuarioStr);
				PreparedStatementDecorator insertarUsuarioStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
				//A) Insertar Datos Básicos del Usuario______________________________________________
				insertarUsuarioStatement.setString(1, loginUsuario);
				insertarUsuarioStatement.setString(2, MD5Hash.hashPassword(passwordUsuario));
				insertarUsuarioStatement.setInt(3,codigoMedico);
				insertarUsuarioStatement.setString(4, codigoInstitucion);
				
				if(Utilidades.convertirAEntero(codigoCargo)>=0)
				 {
				 insertarUsuarioStatement.setInt(5,Utilidades.convertirAEntero(codigoCargo) );
				 }
				  else
			    	{
					  insertarUsuarioStatement.setNull(5,Types.INTEGER);
				   }
				
				//Anexo 958
				if (!contratoInterfaz.equals(""))
					insertarUsuarioStatement.setString(6, contratoInterfaz);
				else
					insertarUsuarioStatement.setNull(6, Types.VARCHAR);
				
				/*
				 *fecha_creacion date;
				 *hora_creacion varchar(5);
				 *usuario_creacion varchar(30);
				 *fecha_ultima_activa_usu date;
				 *fecha_ultima_activa_passwd date;
				 *dias_caducidad_password int;
				 *dias_inactivar_usuario int;
				 */
				insertarUsuarioStatement.setString(7, fechaCreacion);
				insertarUsuarioStatement.setString(8, horaCreacion);
				insertarUsuarioStatement.setString(9, usuarioCreacion);
				insertarUsuarioStatement.setString(10, fechaUltimaActivacionUsuario);
				insertarUsuarioStatement.setString(11, fechaUltimaActivacionPWD);
				   if(diasCaducidadPWD>0)
				   {
					   insertarUsuarioStatement.setInt(12, diasCaducidadPWD );
				   }
				   else
				   {
					   insertarUsuarioStatement.setObject(12, null );
				   }
				   if(diasInactivarUsuario>0)
				   {
					   insertarUsuarioStatement.setInt(13, diasInactivarUsuario);
				   }
				   else
				   {
					   insertarUsuarioStatement.setObject(13, null );
				   }


				insertarUsuarioStatement.setString(14, ConstantesBD.acronimoSi);
				insertarUsuarioStatement.setString(15, ConstantesBD.acronimoSi);
				insertarUsuarioStatement.setString(16, fechaCreacion);
				insertarUsuarioStatement.setString(17, horaCreacion);
				insertarUsuarioStatement.setString(18, usuarioCreacion);
				
				
				//Fin anexo 958
				
				resp2 = insertarUsuarioStatement.executeUpdate();
				
				Log4JManager.info("inserción de usuario exitosa");
				//B) Insertar los centros de costo del usuario_____________________________________________
				resp5 = 1;
				int numCentrosCosto = Integer.parseInt(centrosCosto.get("numRegistros").toString());
				if(numCentrosCosto<=0)
					resp5 = 0;
				
				Log4JManager.info("INSERCION CENTROS COSTO USUARIO: "+insertarCentrosCostoUsuarioStr);
				PreparedStatementDecorator insertarCentrosCosto = null;
				for(int j=0;j<numCentrosCosto;j++)
				{
					insertarCentrosCosto =  new PreparedStatementDecorator(con.prepareStatement(insertarCentrosCostoUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarCentrosCosto.setString(1,loginUsuario);
					insertarCentrosCosto.setInt(2,Integer.parseInt(centrosCosto.get(j+"").toString().split("-")[0]));
					resp5 = insertarCentrosCosto.executeUpdate();
					if(resp5 <= 0)
						j=numCentrosCosto;
				}
				Log4JManager.info("inserción de centros costo usuario exitosa");
				//C) Insertar los roles del usuario____________________________________________________________
				/* Inicialmente asumimos que las inserciones van a salir bien sin embargo, si en el for nos damos cuenta
				   que estabamos mal, cambiamos el valor de resp4 por 0 */
				PreparedStatementDecorator agregarRolesStatement;
				resp4 = 1;
				int numRoles = 0;
				Log4JManager.info("INSERCION ROLES: "+agregarRolesStr);
				for (i = 0; i < rolesUsuario.length; i++) {
					if (rolesUsuario != null) {
						if (!rolesUsuario.equals("")) {
							agregarRolesStatement =  new PreparedStatementDecorator(con.prepareStatement(agregarRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							agregarRolesStatement.setString(1, loginUsuario);
							agregarRolesStatement.setString(2, rolesUsuario[i]);
		
							if (agregarRolesStatement.executeUpdate() == 0){
								resp4 = 0;
							}
							else {
								numRoles++;
							}
						}
					}
				}
				Log4JManager.info("inserción de ROLES EXITOSA");
				
				//***********VERIFICACION DE LA INSERCIÓN******************************************************
				if (resp0 == 0 || resp1 == 0 || resp2 == 0 || resp3 == 0 || resp4 == 0) 
				{
					resp = 0;
					myFactory.abortTransaction(con);
				}
				else 
				{
					myFactory.endTransaction(con);
					resp3 = 1;
					resp = 1;
					//En caso en el que el usuario sea MS o AS no debe retornar
					//1 sino el valor del nuevo tipo de identificacion. Si este
					//es el caso que ocurre, podemos hacer un parseInt, ya que
					//el numero sera un entero generado por una secuencia
					if (codigoTipoIdentificacion.equals("MS") || codigoTipoIdentificacion.equals("AS")) {
						resp = Integer.parseInt(numeroIdentificacion);
					}
				}
				return resp;
		}
		catch(SQLException e){
				Log4JManager.error("Error insertando usuario en SqlBaseUsuarioDao: "+e);
				return -1;
			}
	}

	/**
	 * Dado el login de un usuario, modifica sus datos. Usa una conexion abierta, Si no existe, la crea.
	 * Notese que no se cambia ni el login ni el password del usuario en el sistema.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param rolesUsuario rol(es) del usuario en el sistema
	 * @param login login del usuario en el sistema
	 * @param centrosCosto mapa donde se llevan los centros de costo del usuario
	 * @param activo 
	 * @param diasCaducidadPWD 
	 * @param diasInactivarUsuario 
	 * @return numero de filas modificadas en la BD
	 */
	@SuppressWarnings("rawtypes")
	public static int modificarUsuario(Connection con,	String[] rolesUsuario, String login, String codCargo, HashMap centrosCosto, String contratoInterfaz, String activo, int diasInactivarUsuario, int diasCaducidadPWD,String usuarioModifica) throws SQLException 
	{
		String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con));
		String horaActual=UtilidadFecha.getHoraActual(con);

		//Todos las variables las inicializo en 1
		//porque despuès voy a validar si me cambiaron
		//la cedula, en cuyo caso no voy a ejecutar
		//las mismas operaciones

		int resp = 1, resp1 = 1, resp2 = 1, resp0 = 1, resp3 = 1, resp4 = 1, resp5 = 1, resp6=1, i;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
		//Inicializamos la transaccion
		myFactory.beginTransaction(con);

		
		//***********ACTUALIZACIÓN DE LOS ROLES DEL USUARIO************************************************
		String revisarSiTieneRolesStr="select count(1) as numResultados from roles_usuarios where login=?";
		PreparedStatementDecorator revisarSiTieneRolesStatement= new PreparedStatementDecorator(con.prepareStatement(revisarSiTieneRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		revisarSiTieneRolesStatement.setString(1, login);
		ResultSetDecorator rs=new ResultSetDecorator(revisarSiTieneRolesStatement.executeQuery());
		while (rs.next())
		{
			// Antes de ingresar los nuevos roles, hay que borrar todos los que el usuario tuvo antes
			// Como ahora un usuario puede no tener roles, la variable de control para este caso debe
			// ser diferente de 0 (a pesar que los updates den 0)
		    if (rs.getInt("numResultados")==0)
		    {
		        resp2=1;
		    }
		    else
		    {
				String borrarRoles = "DELETE from roles_usuarios where login=?";
				PreparedStatementDecorator borrarRolesStatement =  new PreparedStatementDecorator(con.prepareStatement(borrarRoles,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				borrarRolesStatement.setString(1, login);
				resp2 = borrarRolesStatement.executeUpdate();
		    }
		}
		// Ahora inserto los nuevos roles
		PreparedStatementDecorator agregarRolesStatement;

			/* Inicialmente asumimos que las inserciones van a salir mal sin embargo, si en el
			   'for' nos damos cuenta que estabamosmal, cambiamos el valor de resp4 por 0 */
		resp4 = 1;
		for (i = 0; i < rolesUsuario.length && resp4!=0; i++) {
			if (rolesUsuario != null) {
				if (!rolesUsuario.equals("")) {
					agregarRolesStatement =  new PreparedStatementDecorator(con.prepareStatement(agregarRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					agregarRolesStatement.setString(1, login);
					agregarRolesStatement.setString(2, rolesUsuario[i]);

					if (agregarRolesStatement.executeUpdate() == 0){
						resp4 = 0;
					}
						
				}
			}
		}
		//******************************************************************************************
		
		//************ACTUALIZACION DE LOS CENTROS DE COSTO DEL USUARIO*******************************
		//Se consulta cuantos centros de costo tiene el usuario
		int numCentrosCosto = 0;
		PreparedStatementDecorator numCentrosCostoSt =  new PreparedStatementDecorator(con.prepareStatement(numeroCentrosCostoUsuarioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		numCentrosCostoSt.setString(1,login);
		rs = new ResultSetDecorator(numCentrosCostoSt.executeQuery());
		if(rs.next())
			numCentrosCosto = rs.getInt("cuenta");
		
		//Primero se elimiman los centros de costo anteriores
		if(numCentrosCosto>0)
		{
			PreparedStatementDecorator eliminarCentrosCosto= new PreparedStatementDecorator(con.prepareStatement(eliminarCentrosCostoUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminarCentrosCosto.setString(1, login);
			resp5 = eliminarCentrosCosto.executeUpdate();
		}
		else
			resp5 = 1;

		//Se insertan los nuevos centros de costo
		if(resp5 > 0)
		{
			numCentrosCosto = Integer.parseInt(centrosCosto.get("numRegistros").toString());
			if(numCentrosCosto<=0)
				resp5 = 0;
			
			PreparedStatementDecorator insertarCentrosCosto = null;
			for(int j=0;j<numCentrosCosto;j++)
			{
				insertarCentrosCosto =  new PreparedStatementDecorator(con.prepareStatement(insertarCentrosCostoUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				insertarCentrosCosto.setString(1,login);
				insertarCentrosCosto.setInt(2,Integer.parseInt(centrosCosto.get(j+"").toString().split("-")[0]));
				resp5 = insertarCentrosCosto.executeUpdate();
				if(resp5 <= 0)
					j=numCentrosCosto;
			}
		}
		
	   PreparedStatementDecorator modificarCargo= new PreparedStatementDecorator(con.prepareStatement(updateUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
	   if (Utilidades.convertirAEntero(codCargo)>=0)
		   modificarCargo.setInt(1,Integer.parseInt(codCargo));
	   else
		   modificarCargo.setNull(1, Types.INTEGER);

	   //Anexo 958
	   if (!contratoInterfaz.equals(""))
		   modificarCargo.setDouble(2,Utilidades.convertirADouble(contratoInterfaz));
	   else
		   modificarCargo.setNull(2, Types.DOUBLE);
	   modificarCargo.setString(3, activo);
	   if(UtilidadTexto.getBoolean(activo))
	   {
		   modificarCargo.setString(4, fechaActual);
		   modificarCargo.setObject(5, null);
	   }
	   else
	   {
		   modificarCargo.setObject(4, null);
		   modificarCargo.setString(5, fechaActual);
	   }
	   if(diasCaducidadPWD>0)
	   {
		   modificarCargo.setInt(6, diasCaducidadPWD );
	   }
	   else
	   {
		   modificarCargo.setObject(6, null );
	   }
	   if(diasInactivarUsuario>0)
	   {
		   modificarCargo.setInt(7, diasInactivarUsuario);
	   }
	   else
	   {
		   modificarCargo.setObject(7, null );
	   }
	   
		
	   modificarCargo.setString(8,fechaActual);
	   modificarCargo.setString(9,horaActual);
	   modificarCargo.setString(10,usuarioModifica);
	   modificarCargo.setString(11,login);
	   resp6 = modificarCargo.executeUpdate();
  
		
	
		
		
		//***********************************************************************************++
		

		if (resp0 == 0 || resp1 == 0 || resp2 == 0 || resp3 == 0 || resp4 == 0 || resp5 == 0 || resp6==0) 
		{
			resp = 0;
			myFactory.abortTransaction(con);

		}
		else {
			resp = 1;
			myFactory.endTransaction(con);
		}
		return resp;
	}

	/**
	 * Dado un login, busca en la base de datos Genérica el usuario correspondiente y recupera sus datos.
	 * @param con una conexion abierta con la BD Genérica
	 * @param login login del usuario que se desea cargar
	 * @return Un objeto <code>Answer</code> con la conexion abierta y un <code>ResultSet</code> con los datos del usuario
	 */
	public static Answer cargarUsuario(Connection con, String login) throws SQLException 
	{
		PreparedStatementDecorator cargarUsuarioStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		Log4JManager.info("CONSULTA USUARIO=> "+cargarUsuarioStr+", login=> "+login);
		cargarUsuarioStatement.setString(1, login);
		ResultSetDecorator rs = new ResultSetDecorator(cargarUsuarioStatement.executeQuery());

		return new Answer(rs, con);
	}

	/**
	 * Lista los roles disponibles en el sistema.
	 * @param con una conexion abierta con la base de datos Genérica
	 * @return el código en HTML que muestra los roles disponibles del sistema
	 */
	public static String listarRoles(Connection con) throws SQLException 
	{

		
		String tmp = "";
		int cont = 1;
		String sb = "";

		// luego se completa el enunciado del rol, tambien de la BD

		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement("SELECT nombre_rol FROM roles where nombre_rol!='superadministrador' and nombre_rol!='paciente' ORDER BY nombre_rol",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		
		sb = "<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"4\" bgcolor=\"#006898\"><tr bgcolor=\"#FFFFFF\">";

		while (rs.next()) 
		{
			tmp = rs.getString("nombre_rol");
			//contenido de la celda
			tmp = "<input type=\"checkbox\" name=\"rolesUsuario\" value=\"" + tmp + "\">" + tmp;
			
			if(cont%4==0)
				sb += "<td>" + tmp + "</td></tr><tr bgcolor=\"#FFFFFF\">";
			else
				sb += "<td>" + tmp + "</td>";
			cont ++;
		}
		
		//Se verifica el modulo actual
		cont --;
		if(cont%4!=0)
		{
			int restante = 4 - (cont%4);
			sb += "<td colspan=\""+restante+"\"></td></tr>";
		}
		
		sb+= "</table>";
		rs.close();
		return sb.toString();
	}

	/**
	 * Lista los roles disponibles en el sistema, con los roles a los que pertenece el usuario elegidos (<i>checked</i>)
	 * @param con una conexion abierta con la base de datos Genérica
	 * @param rolesUsuario roles a los cuales pertenece el usuario
	 * @return el código en HTML que muestra los roles disponibles del sistema
	 */
	public static String listarRolesElegidos(Connection con, String[] rolesUsuario) throws SQLException 
	{

		String tmp = "";
		String sb = "";
		int cont = 1;

								

		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement("SELECT nombre_rol FROM roles where nombre_rol!='superadministrador' and nombre_rol!='paciente' ORDER BY nombre_rol",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		sb = "<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"4\" bgcolor=\"#006898\"><tr bgcolor=\"#FFFFFF\">";
		
		while (rs.next()) {

			tmp = rs.getString("nombre_rol");
			
			
			// SE edita el contenido de la celda
			if (poseeRol(tmp, rolesUsuario)) 
				tmp = "<input type=\"checkbox\" name=\"rolesUsuario\" value=\"" + tmp +"\" checked >"+tmp;
			else 
				tmp = "<input type=\"checkbox\" name=\"rolesUsuario\" value=\"" + tmp +"\" >"+tmp;
			
			if(cont%4==0)
				sb += "<td>" + tmp + "</td></tr><tr bgcolor=\"#FFFFFF\">";
			else
				sb += "<td>" + tmp + "</td>";
			
			cont ++;
		}
		
		//Se verifica el modulo actual
		cont --;
		if(cont%4!=0)
		{
			int restante = 4 - (cont%4);
			sb += "<td colspan=\""+restante+"\"></td></tr>";
		}
		
		sb+= "</table>";
		
		rs.close();
		return sb.toString();
	}

	/**
	 * Método auxiliar, dice si el usuario tiene el rol actual.
	 * @param rol rol del sistema.
	 * @param roles arreglo con los roles que tiene el usuario
	 * @return <b>true</b> si 'rol' está en 'roles', <b>false</b> si no
	 */
	private static boolean poseeRol(String rol, String[] roles) 
	{
		boolean posee = false;
		String tmp = rol.trim();

		for (int i = 0; i < roles.length; i++) 
		{
			if (tmp.equals(roles[i].trim())) 
			{
				posee = true;
				break;
			}
		}
		return posee;
	}

	/**
	 * Método auxiliar, que dados un número y un tipo de identificación retorna
	 * el login respectivo de ese usuario.
	 * @param con una conexión abierta con la BD
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 * @param activo:
	 * 'true' consultará login activo
	 * 'false' consultará login inactivo
	 * '' no se define si es login activo/inactivo
	 * @return cadena con el login del usuario o <b>null</b> si no existía un usuario con esos datos
	 */
	public static String buscarLogin(Connection con, String tipoId, String numeroId,String activo) throws SQLException 
	{
		ResultSetDecorator rs = null;
		String busquedaLoginStr = "SELECT login from usuarios us, personas per where per.numero_identificacion=? and per.tipo_identificacion=? AND us.codigo_persona=per.codigo";
		if(activo.equals("true"))
			busquedaLoginStr+=" AND login NOT IN (SELECT login FROM usuarios_inactivos)";
		else if(activo.equals("false"))
			busquedaLoginStr+=" AND login IN (SELECT login FROM usuarios_inactivos)";
		
		PreparedStatementDecorator busquedaLoginStatement =  new PreparedStatementDecorator(con.prepareStatement(busquedaLoginStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		busquedaLoginStatement.setString(1, numeroId);
		busquedaLoginStatement.setString(2, tipoId);
		rs = new ResultSetDecorator(busquedaLoginStatement.executeQuery());
		if (rs.next()) {
			return rs.getString("login");
		}
		else {
			return null;
		}

	}

	/**
	 * Método que permite desactivar un usuario dado su login
	 * @param con una conexion abierta con la base de datos Genérica
	 * @param login del usuario que desea ser eliminado
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios desactivados
	 */
	public static int desactivarUsuario(Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException 
	{
		int resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0;

		Log4JManager.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Log4JManager.info("login: " + login + " Institucion: " + codigoInstitucion);

		String verificacionStr="SELECT 1 AS numRegistros FROM usuarios WHERE institucion="+ codigoInstitucion+" AND login='"+login+"'";
		int numRegistros=Utilidades.nroRegistrosConsulta(con, verificacionStr);
		Log4JManager.info("numRegistros: " + numRegistros);
		if(numRegistros > 1)
		{
			Log4JManager.info("encontro mas de un usuario en esa institucion .. hay un error");
			return 0;
		}

		//se verifica si el usuario esta inactivo 
		verificacionStr="SELECT 1 AS numRegistros FROM usuarios WHERE institucion="+ codigoInstitucion+" AND login='"+login+"' and activo='"+ConstantesBD.acronimoNo+"'";
		numRegistros=Utilidades.nroRegistrosConsulta(con, verificacionStr);
		Log4JManager.info("numRegistros: " + numRegistros);
		if(numRegistros >= 1)
		{
			Log4JManager.info("El Usuario ya esta inactivo");
			return 0;
		}
		
		DaoFactory myFactory=null;
		try{
			myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			myFactory.beginTransaction(con);
			resp0 = 1;
	
			
			//se verifica si el usuario esta inactivo 
			verificacionStr="SELECT 1 AS numRegistros FROM usuarios_inactivos WHERE login='"+login+"'";
			numRegistros=Utilidades.nroRegistrosConsulta(con, verificacionStr);
			//logger.info("numRegistros: " + numRegistros);
			if(numRegistros < 1)
			{
				
				PreparedStatementDecorator desactivarUs2 =  new PreparedStatementDecorator(con.prepareStatement("INSERT INTO usuarios_inactivos (login,codigo_institucion, password) values (?, ?, (select password from usuarios where login=?))",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				desactivarUs2.setString(1, login);
				desactivarUs2.setString(2, codigoInstitucion);
				desactivarUs2.setString(3, login);
				resp2 = desactivarUs2.executeUpdate();
			}
	
			Random random= new Random();
			double prueba=random.nextDouble();
			String password ="" + prueba;
			password=MD5Hash.hashPassword(password.substring(2));
			
			PreparedStatementDecorator desactivarUs =  new PreparedStatementDecorator(con.prepareStatement("UPDATE Usuarios SET activo='"+ConstantesBD.acronimoNo+"',fecha_ultima_inact_usu=current_date, institucion=? WHERE login= ?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			desactivarUs.setInt(1, Integer.parseInt(codigoInstitucion));
			//desactivarUs.setString(2, password);
			//desactivarUs.setString(3, login);
			desactivarUs.setString(2, login);
			resp1 = desactivarUs.executeUpdate();
			Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
			String horaActual=UtilidadFecha.getHoraActual(con);
			myFactory.endTransaction(con);
			
			
			///log
			//insertar el log
			try{
				HibernateUtil.beginTransaction();
				LogActiinactiUsuarios dto=new LogActiinactiUsuarios();
				LogActiinactiUsuariosHome dao=new LogActiinactiUsuariosHome();
				dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoInactivo);
				dto.setFechaProceso(fechaActual);
				dto.setHoraProceso(horaActual);
				dto.setTipoInactivacion(ConstantesIntegridadDominio.acronimoManual);
				Usuarios usu=new Usuarios();
				UsuariosDelegate usuDao=new UsuariosDelegate();
				usu=usuDao.findById(login);
				dto.setUsuariosByLoginUsuario(usu);
				Usuarios usuPrco=usuDao.findById(usuActual);
				dto.setUsuariosByUsuarioProcesa(usuPrco);
				dao.persist(dto);
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error("ERROR desactivarUsuario Hibernate",e);
				HibernateUtil.abortTransaction();
			}
			resp1=1;
		}catch (Exception e) {
			Log4JManager.error("ERROR desactivarUsuario JDBC",e);
			if(myFactory != null){
				myFactory.abortTransaction(con);
			}
		}
		return resp1;
	}

	/**
	 * Método que permite desactivar un usuario dada su identificación
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId  Tipo de identificacion del usuario que se desea desactivar
	 * @param numeroId  Numero de identificacion del usuario que se desea desactivar
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios desactivados
	 */
	public static int desactivarUsuario(Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException 
	{
		String login = buscarLogin(con, tipoId, numeroId,"");
		return desactivarUsuario(con, login, codigoInstitucion,usuActual);
	}

	/**
	 * Método que permite activar un usuario dado su login
	 * @param con una conexion abierta con la base de datos Genérica
	 * @param login del usuario que desea ser activado
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios activados
	 */
	public static int activarUsuario(Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException 
	{
		int resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		myFactory.beginTransaction(con);
		resp0=1;

		String consultaValidacion="select count(1) AS numResultados from usuarios_inactivos where login='"+login+"'";
		int numResultados=Utilidades.nroRegistrosConsulta(con, consultaValidacion);

		if(numResultados <= 0)
		{
			return 0;
		}
		
		try {
				PreparedStatementDecorator activarUs =  new PreparedStatementDecorator(con.prepareStatement("UPDATE usuarios set institucion=?,activo='"+ConstantesBD.acronimoSi+"',fecha_ultima_activa_usu=current_date where login=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				activarUs.setString(1, codigoInstitucion);
				activarUs.setString(2, login);
				resp1 = activarUs.executeUpdate();
		}
		catch (SQLException e)
		{
			return 0;
		}

		PreparedStatementDecorator activarUs2 =  new PreparedStatementDecorator(con.prepareStatement("delete from usuarios_inactivos where login=? and codigo_institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		activarUs2.setString(1, login);
		activarUs2.setString(2, codigoInstitucion);
		resp2 = activarUs2.executeUpdate();

		myFactory.endTransaction(con);
		
		///log
		//insertar el log
		try{
			HibernateUtil.beginTransaction();
			LogActiinactiUsuarios dto=new LogActiinactiUsuarios();
			LogActiinactiUsuariosHome dao=new LogActiinactiUsuariosHome();
			dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoActivo);
			dto.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
			dto.setHoraProceso(UtilidadFecha.getHoraActual(con));
			dto.setTipoInactivacion(ConstantesIntegridadDominio.acronimoManual);
			Usuarios usu=new Usuarios();
			UsuariosDelegate usuDao=new UsuariosDelegate();
			usu=usuDao.findById(login);
			dto.setUsuariosByLoginUsuario(usu);
			Usuarios usuPrco=usuDao.findById(usuActual);
			dto.setUsuariosByUsuarioProcesa(usuPrco);
			dao.persist(dto);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR activarUsuario", e);
			HibernateUtil.abortTransaction();
		}
		
		resp3=1;

		if (resp0 == 0 || resp1 == 0 || resp2 == 0 || resp3 == 0) 
		{
			myFactory.abortTransaction(con);
			return 0;
		}

		return resp1;
	}

	/**
	 * Método que permite activar un usuario dada su identificación.
	 * @param con una conexion abierta con la base de datos Genérica
	 * @param tipoId  Tipo de identificacion del usuario que se desea activar
	 * @param numeroId  Numero de identificacion del usuario que se desea activar
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios activados
	*/
	public static int activarUsuario(Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException 
	{
		String login = buscarLogin(con, tipoId, numeroId,"");
		return activarUsuario(con, login, codigoInstitucion,usuActual);
	}

	/**
	 * Implementación del método que permite buscar el login de un médico
	 * para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.UsuarioDao#buscarLogin(java.sql.Connection, int)
	 */
	public static String buscarLogin(Connection con, int codigoMedico) throws SQLException 
	{
		ResultSetDecorator rs = null;
		String busquedaLoginStr = "SELECT login AS login from usuarios us, personas per where per.codigo=us.codigo_persona AND per.codigo=?";
		PreparedStatementDecorator busquedaLoginStatement =  new PreparedStatementDecorator(con.prepareStatement(busquedaLoginStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		busquedaLoginStatement.setInt(1, codigoMedico);
		
		rs = new ResultSetDecorator(busquedaLoginStatement.executeQuery());
		if (rs.next()) 
		{
			return rs.getString("login");
		}
		else 
		{
			return null;
		}
	}
	
	
	
	/**
	 * Método implementado para cargar los centros de costo del usuario
	 * @param con
	 * @param login
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarCentrosCostoUsuario(Connection con,String login)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarCentrosCostoUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,login);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en cargarCentrosCostoUsuario de SqlBaseUsuarioDao: "+e);
			return null;
		}
	}


}