/*
 * @(#)SqlBasePersonaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.RespuestaInsercionPersona;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Persona
 *
 * @version 1.0 Jan 6, 2005
 */
public class SqlBasePersonaDao 
{
	/**
	 * Logs para informar erores
	 */
	private static Logger logger = Logger.getLogger(SqlBasePersonaDao.class);
	
	/** Objeto para realizar acciones de registro */
	private static Logger il_logger = Logger.getLogger(SqlBasePersonaDao.class);
	
	/**
	* Cadena constante de validación de unicidad de la combinación tipo de documento, número de
	* documento
	*/
	private static final String is_validacionDocumento =
		"SELECT "	+	"count(*) AS resultados "	+
		"FROM "		+	"personas "						+
		"WHERE "	+	"tipo_identificacion"	+ "=?"	+ " AND "	+
						"numero_identificacion"	+ "=?"	+ " AND "	+
						"codigo"				+ "<>?";

	/** Cadena constante con la sentencia necesaria para obtener todos los datos de una persona */
	private static final String is_consultar =
		"SELECT * FROM personas WHERE codigo=?";
	
	/** Cadena de actualización de datos del triage */
	private static final String modifica_triage = "UPDATE historiaclinica.triage " +
												  "SET " +
												  "tipo_identificacion = ?," +
												  "numero_identificacion= ?," +
												  "primer_nombre = ?," +
												  "segundo_nombre = ?," +
												  "primer_apellido = ?," +
												  "segundo_apellido = ?," +
												  "fecha_nacimiento =?  " +
												  "WHERE tipo_identificacion = ? AND numero_identificacion = ? AND institucion = ? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para hacer un <i>update</i> de una persona en la tabla respectiva.
	 */
	private static final String modificarPersonaStr = "UPDATE administracion.personas set " +
		"codigo_departamento_nacimiento=?," +
		"codigo_ciudad_nacimiento=?, " +
		"codigo_pais_nacimiento = ?, " +
		"tipo_persona=?, " +
		"fecha_nacimiento=?, " +
		"estado_civil=?, " +
		"sexo=?, " +
		"primer_nombre=?, " +
		"segundo_nombre=?, " +
		"primer_apellido=?, " +
		"segundo_apellido=?, " +
		"direccion=?," +
		"codigo_departamento_vivienda=?, " +
		"codigo_ciudad_vivienda=?, " +
		"codigo_pais_vivienda=?, " +
		"codigo_barrio_vivienda=?, " +
		"codigo_localidad_vivienda = ?, " +
		"telefono=?, " +
		"email=?, " +
		"tipo_identificacion= ?, " +
		"numero_identificacion=?, " +
		"codigo_depto_id = ?, " +
		"codigo_ciudad_id = ?, " +
		"codigo_pais_id = ?, " +
		"telefono_celular = ?, " +
		"telefono_fijo = ? " +
		"where codigo=?";
	
	
	private static final String obtenerCodigoPersonaStr="SELECT codigo AS codigo FROM administracion.personas WHERE numero_identificacion=? and tipo_identificacion=?";
	
	/**
	 * obtiene el id persona dado el numero y tipo de identificacion
	 */
	private static final String getIdPersonaDadoNumeroTipoId= "SELECT codigo AS idPersona FROM administracion.personas WHERE numero_identificacion=? and tipo_identificacion=?";
	
	private static final String obtenerApellidosNombresPersonaStr = "SELECT primer_apellido || coalesce(' '||segundo_apellido,'') || ' ' || primer_nombre || coalesce(' '||segundo_nombre,'') AS nombre FROM administracion.personas WHERE codigo = ?";
	
	
	/**
	* Modifica una persona en una base de datos Genérica, ejecutando solamente una parte de la
	* transacción SQL
	* @param ac_con									Conexion abierta con una base de datos
	*												Genérica
	* @param as_tipoId								Tipo de identificación de la persona (cédula,
	*												tarjeta de identidad, etc.)
	* @param as_numeroId							Número de la identificación de la persona
	* @param codigoDeptoId							codigo del departamento de la identificacion
	* @param codigoCiudadId							codigo de la ciudad de la identificacion
	* @param ai_codigoPersona						Identificador interno de la persona
	* @param as_codigoDepartamentoIdentificacion	Código del departamento de expedición de la
	*												identificación de la persona
	* @param as_codigoCiudadIdentificacion			Código de la ciudad de expedición de la
	*												identificación de la persona
	* @param as_codigoTipoPersona					Código del tipo de persona
	* @param as_diaNacimiento						Día de naciemiento de la persona
	* @param as_mesNacimiento						Mes de naciemiento de la persona
	* @param as_anioNacimiento						Año de naciemiento de la persona
	* @param as_codigoEstadoCivil					Código del estado civil de la persona
	* @param as_codigoSexo							Código del sexo de la persona
	* @param as_primerNombrePersona					Primer nombre de la persona
	* @param as_segundoNombrePersona				Segundo nombre de la persona
	* @param as_primerApellidoPersona				Primer apellido de la persona
	* @param as_segundoApellidoPersona				Segundo apellido de la persona
	* @param as_direccion							Dirección de residencia de la persona
	* @param as_codigoDepartamento					Código del departamento de residencia de la
	*												persona
	* @param as_codigoCiudad						Código del departamento de residencia de la
	*												persona
	* @param as_codigoBarrio						Código del barrio de residencia de la persona
	* @param as_telefono							Teléfono de contacto de la persona
	* @param as_email								Correo electrónico de la persona
	* @param as_estado								Estado de la transacción que se quiere realizar
	* @return	1	Si la modificación de la persona fue exitoso
	*			0	Error de base de datos
	*			-1	Si la combinación tipo/número de documento ya existe
	*			-2	Si la fecha de nacimiento es posterior a la actual
	*/
	public static int modificarPersonaTransaccional(
		Connection	ac_con,
		String		as_tipoId,
		String		as_numeroId,
		String		codigoDeptoId,
		String 		codigoCiudadId,
		String 		codigoPaisId,
		int			ai_codigoPersona,
		String		as_codigoDepartamentoIdentificacion,
		String		as_codigoCiudadIdentificacion,
		String		codigoPaisIdentificacion,
		String		as_codigoTipoPersona,
		String		as_diaNacimiento,
		String		as_mesNacimiento,
		String		as_anioNacimiento,
		String		as_codigoEstadoCivil,
		String		as_codigoSexo,
		String		as_primerNombrePersona,
		String		as_segundoNombrePersona,
		String		as_primerApellidoPersona,
		String		as_segundoApellidoPersona,
		String		as_direccion,
		String		as_codigoDepartamento,
		String		as_codigoCiudad,
		String 		codigoPais,
		String		as_codigoBarrio,
		String		codigoLocalidad,
		String		as_telefono,
		String		as_email,
		String 		as_telefonoCelular,
		String		as_estado,
		String		is_aumentarSinIdentificacion,
		String 		as_tipoPersona,
		int 		codigoInstitucion, 
		String 		as_telefonoFijo
		
	)
	{
		logger.info("\n >> COMENZO el SqlBase de Persona");
		try{
				boolean		lb_continuar;
				DaoFactory	ldf_df;
				int			li_resp;
				String		ls_IdAnterior ="";
				String		ls_tipoIdAnterior = "";
		
				try
				{
					String ls_fechaNacimiento;
		
					if(as_estado == null)
					{
						throw
							new SQLException(
								"Error SQL: No se seleccionó ningun estado para la transacción"
							);
					}
		
					/* Convertir el estado a caracters minúsculos para facilitar la comparación */
					as_estado = as_estado.toLowerCase();
		
					/* Iniciar el indicador de errores */
					li_resp = 1;
		
					/* Obtener una instancia del objeto principal de acceso a fuente de datos */
					ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
					/* Obtener la fecha de nacimiento de la persona */
					ls_fechaNacimiento =
						as_anioNacimiento + '-' + as_mesNacimiento + '-' + as_diaNacimiento;
		
					if(as_estado.equals(ConstantesBD.inicioTransaccion) )
					{
						/* Verificar el estado de la conexión y abrir una nueva si es necesario */
						if(ac_con == null || ac_con.isClosed() )
							ac_con = ldf_df.getConnection();
		
						/* Iniciar una nueva transacción */
						lb_continuar = ldf_df.beginTransaction(ac_con);
					}
					else
					{
						/* Indicar que el inicio de la transacción fue exitoso */
						lb_continuar = true;
					}
		
					/* Validar si la combinación tipo/número de documento ya existe en la base de datos */
					if(lb_continuar)
					{
						logger.info("entro a validar existencia paciente ");
						int					li_resultados;
						PreparedStatementDecorator	lps_ps;
						ResultSetDecorator			lrs_rs;
						logger.info("tipoIdentificacion===> "+as_tipoId);
						logger.info("numeroIdentificacion===> "+as_numeroId);
						logger.info("codigo Persona===> "+ai_codigoPersona);
						lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_validacionDocumento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						lps_ps.setString(1,as_tipoId);
						lps_ps.setString(2,as_numeroId);
						lps_ps.setInt(3, ai_codigoPersona);
		
						/* Obtener el nómero de registros que coinciden con el tipo/número de documento */
						if( (lrs_rs = new ResultSetDecorator(lps_ps.executeQuery()) ).next() )
							li_resultados = lrs_rs.getInt("resultados");
						else
							li_resultados = -1;
		
						/* Existe al menos un registro que coincide con el tipo/número de identificación */
						if(li_resultados > 0)
						{
							lb_continuar	= false;
							li_resp			= -1;
						}
						logger.info("va a salir .....estado continuar "+lb_continuar );
					}
		
					/* Validar si la fecha de nacimiento es correcta */
					
						if(lb_continuar)
						{
							Date				ld_fechaActual;
							Date				ld_fechaNacimiento;
							SimpleDateFormat	lsdf_sdf;
			
							try
							{
								ld_fechaActual	= new Date();
								lsdf_sdf		= new SimpleDateFormat("yyyy-MM-dd");
			
								/* Validar el formato de la fecha */
								lsdf_sdf.setLenient(false);
								ld_fechaNacimiento = lsdf_sdf.parse(ls_fechaNacimiento);
			
								/* Validar que la fecha de nacimiento sea anterior a la fecha actual */
								if(ld_fechaActual.before(ld_fechaNacimiento) )
								{
									lb_continuar	= false;
									li_resp			= -2;
								}
							}
							catch (ParseException lpe_e)
							{
								/* El formato de la fecha no es correcto */
								lb_continuar	= false;
								li_resp			= -2;
							}
						}
				      	
					if(lb_continuar)
					{
						/*
							Validar si la persona no tiene identificación en este caso se debe asignar el
							número de indentificación de manera automática
						*/
						if(!as_tipoPersona.equals(ConstantesBD.tipoPersonaPaciente)){
							if(as_tipoId.equals("MS")||as_tipoId.equals("AS") )
							{
								PreparedStatementDecorator	lps_consulta;
								ResultSetDecorator			lrs_rs;																
			
								/*
									Validar si el tipo de identificación anterior es diferente al actual (el
									que se quiere modificar). En caso de que lo sea se debe asignar el
									?umero de identificación
								*/
								lps_consulta = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								lps_consulta.setInt(1, ai_codigoPersona);
			
								(lrs_rs = new ResultSetDecorator(lps_consulta.executeQuery()) ).next();
								ls_tipoIdAnterior = lrs_rs.getString("tipo_identificacion");
								ls_IdAnterior = lrs_rs.getString("numero_identificacion");
			
								/*
									Evita que el número de identificación sea modificado por el usuario cuando
									el tipo de identificación es AS o MS
								*/
								as_numeroId			= lrs_rs.getString("numero_identificacion");
			
								lrs_rs.close();
								lps_consulta.close();
			
								if(!as_tipoId.equals(ls_tipoIdAnterior) )
								{
									lrs_rs =
										new ResultSetDecorator(new PreparedStatementDecorator(ac_con.prepareStatement(is_aumentarSinIdentificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery());
									lrs_rs.next();
			
									as_numeroId = lrs_rs.getString("numero_identificacion");
								}
							}
						}
						else
						{
							if(Utilidades.esAutomaticoTipoId(ac_con,as_tipoId,codigoInstitucion))
							{
								PreparedStatementDecorator	lps_consulta;
								ResultSetDecorator			lrs_rs;														
			
								/*
									Validar si el tipo de identificación anterior es diferente al actual (el
									que se quiere modificar). En caso de que lo sea se debe asignar el
									?umero de identificación
								*/
								lps_consulta = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								lps_consulta.setInt(1, ai_codigoPersona);
			
								(lrs_rs = new ResultSetDecorator(lps_consulta.executeQuery()) ).next();
								ls_tipoIdAnterior = lrs_rs.getString("tipo_identificacion");
								ls_IdAnterior = lrs_rs.getString("numero_identificacion");
								
								/*
									Evita que el número de identificación sea modificado por el usuario cuando
									el tipo de identificación es AS o MS
								*/
								as_numeroId			= lrs_rs.getString("numero_identificacion");
			
								lrs_rs.close();
								lps_consulta.close();
			
								if(!as_tipoId.equals(ls_tipoIdAnterior) )
								{
									lrs_rs =
										new ResultSetDecorator(new PreparedStatementDecorator(ac_con.prepareStatement(is_aumentarSinIdentificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery());
									lrs_rs.next();
			
									as_numeroId = lrs_rs.getString("numero_identificacion");
								}
							}
						}
												
						//************************************************************************************************
						//Actualiza el triage en caso de que hubiera cambiado la informacion de la identificacion
						actualizarTriage(
								ac_con,
								ai_codigoPersona,								 
								as_tipoId, 
								as_numeroId, 
								as_primerNombrePersona,
								as_segundoNombrePersona, 
								as_primerApellidoPersona,
								as_segundoApellidoPersona, 
								ls_fechaNacimiento,
								codigoInstitucion);
								
						//************************************************************************************************
						
		
						PreparedStatementDecorator lps_ps;
						
						logger.info("\n\n departamento nacimiento=> "+as_codigoDepartamentoIdentificacion);
						logger.info("departamento ciudad=> "+as_codigoCiudadIdentificacion);
						logger.info("pais nacimiento=> "+codigoPaisIdentificacion);
						logger.info("tipo persona=> "+as_codigoTipoPersona);
						logger.info("fecha nacimiento=> "+ls_fechaNacimiento);
						logger.info("estado civil=> "+as_codigoEstadoCivil);
						logger.info("sexo=> "+as_codigoSexo);
						logger.info("primer nombre=> "+as_primerNombrePersona);
						logger.info("segundo nombre=> "+as_segundoNombrePersona);
						logger.info("primer apellido=> "+as_primerApellidoPersona);
						logger.info("segundo apellido=> "+as_segundoApellidoPersona);
						logger.info("direccion=> "+as_direccion);
						logger.info("departamento residencia=> "+as_codigoDepartamento);
						logger.info("ciudad residencia=> "+as_codigoCiudad);
						logger.info("pais residencia=> "+codigoPais);
						logger.info("barrio residencia=> "+as_codigoBarrio);
						logger.info("localidad=> "+codigoLocalidad);
						logger.info("telefono=> "+as_telefono);
						logger.info("email=> "+as_email);
						logger.info("tipo identificacion=> "+as_tipoId);
						logger.info("numero identificacion=> "+as_numeroId);
						logger.info("departamento expedicion=> "+codigoDeptoId);
						logger.info("ciudad expedicion=> "+codigoCiudadId);
						logger.info("pais expedicion=> "+codigoPaisId);
						logger.info("persona=> "+ai_codigoPersona);
						logger.info("telefonofijo=> "+as_telefonoFijo);
						
						
						logger.info("Paso por aqui A");
						lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(modificarPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						logger.info ("\n>>>>  VA BIEN en sql 0");
						if(!as_codigoDepartamentoIdentificacion.equals(""))
							lps_ps.setString(1, as_codigoDepartamentoIdentificacion );
						else
							lps_ps.setNull(1, Types.VARCHAR );
						if(!as_codigoCiudadIdentificacion.equals(""))
							lps_ps.setString(2, as_codigoCiudadIdentificacion );
						else
							lps_ps.setNull(2, Types.VARCHAR);
						if(!codigoPaisIdentificacion.equals(""))
							lps_ps.setString(3, codigoPaisIdentificacion );
						else
							lps_ps.setNull(3, Types.VARCHAR );
						logger.info ("\n>>>>  VA BIEN en sql 1 *"+as_codigoTipoPersona+"*");
						lps_ps.setInt(4, Integer.parseInt(as_codigoTipoPersona) );
						lps_ps.setDate(5, java.sql.Date.valueOf(ls_fechaNacimiento));
						if(!as_codigoEstadoCivil.equals(""))
						 lps_ps.setString(6, as_codigoEstadoCivil);
						else
							lps_ps.setNull(6, Types.VARCHAR );
						
						if(!as_codigoSexo.equals(""))
						  lps_ps.setInt(7, Integer.parseInt(as_codigoSexo) );
						else
						  lps_ps.setNull(7, Types.NUMERIC );						
						
						
						lps_ps.setString(8, as_primerNombrePersona );
						lps_ps.setString(9, as_segundoNombrePersona );
						lps_ps.setString(10, as_primerApellidoPersona);
						lps_ps.setString(11, as_segundoApellidoPersona);
						logger.info ("\n>>>>  VA BIEN en sql2 ");
						if(!as_direccion.equals(""))
						
							lps_ps.setString(12, as_direccion);	
						else
							lps_ps.setNull(12, Types.VARCHAR );
						
						if(!as_codigoDepartamento.equals(""))
							lps_ps.setString(13, as_codigoDepartamento );
						else
							lps_ps.setNull(13, Types.VARCHAR );
						if(!as_codigoCiudad.equals(""))
							lps_ps.setString(14, as_codigoCiudad );
						else
							lps_ps.setNull(14, Types.VARCHAR );
						if(!codigoPais.equals(""))
							lps_ps.setString(15, codigoPais );
						else
							lps_ps.setNull(15, Types.VARCHAR );
						if(!as_codigoBarrio.equals(""))
							lps_ps.setInt(16, Integer.parseInt(as_codigoBarrio) );
						else
							lps_ps.setNull(16,Types.INTEGER);
						if(!codigoLocalidad.equals(""))
							lps_ps.setString(17, codigoLocalidad );
						else
							lps_ps.setNull(17, Types.VARCHAR);
						
						
						lps_ps.setString(18, as_telefono);
						lps_ps.setString(19, as_email);
						lps_ps.setString(20, as_tipoId);
						lps_ps.setString(21, as_numeroId);
						if(!codigoDeptoId.equals(""))
							lps_ps.setString(22, codigoDeptoId);
						else
							lps_ps.setNull(22, Types.VARCHAR);
						if(!codigoCiudadId.equals(""))
							lps_ps.setString(23, codigoCiudadId);
						else
							lps_ps.setNull(23, Types.VARCHAR);
						if(!codigoPaisId.equals(""))
							lps_ps.setString(24, codigoPaisId);
						else
							lps_ps.setNull(24, Types.VARCHAR);
						
						logger.info("CONVERTIR A DOUBLE CELULAR: "+as_telefonoCelular+" >> "+Utilidades.convertirADouble(as_telefonoCelular)+", "+ConstantesBD.codigoNuncaValidoDoubleNegativo);
						if(Utilidades.convertirADouble(as_telefonoCelular)!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
							lps_ps.setLong(25,Long.parseLong(as_telefonoCelular));
						else
							lps_ps.setNull(25,Types.NUMERIC);
		
						if(!as_telefonoFijo.equals(""))
							lps_ps.setInt(26, Utilidades.convertirAEntero(as_telefonoFijo));
						else
							lps_ps.setNull(26, Types.NUMERIC);
						
						lps_ps.setInt(27, ai_codigoPersona);
		
						lb_continuar = (lps_ps.executeUpdate() == 1);
					}
		
					if(as_estado.equals(ConstantesBD.finTransaccion) )
					{
						if(lb_continuar)
							ldf_df.endTransaction(ac_con);
						else
							ldf_df.abortTransaction(ac_con);
					}
				}
				catch(SQLException lsqle_e)
				{
					logger.error("Error",lsqle_e);
					
					/* Se presentó un error de base de datos */
					li_resp	= 0;
					lsqle_e.printStackTrace();
					il_logger.warn(lsqle_e);
					throw lsqle_e;
				}
		
				return li_resp;
		}
		catch(SQLException e){
				logger.error("Error al ingresar Persona en SqlBasePersonaDao: "+e);
				return -1;
			}
	}

	/**
	 * Dada una conexion abierta con una base de datos Genérica y los datos basicos de
	 * una persona, hace el insert fisico de la persona, (si no existe una fuente
	 * de datos, NO la crea para no tocar la transaccionalidad del resto de
	 * inserciones / modificaciones que la usen (medico, persona o usuario)
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param numeroIdentificacion numero de identificacion de la persona
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion de la persona
	 * @param codigoDepartamentoIdentificacion código del departamento de expedición de la identificación de la persona
	 * @param codigoCiudadIdentificacion código de la ciudad de expedición de la identificación de la persona
	 * @param codigoTipoPersona código del tipo de persona
	 * @param diaNacimiento día de naciemiento de la persona
	 * @param mesNacimiento mes de naciemiento de la persona
	 * @param anioNacimiento año de naciemiento de la persona
	 * @param codigoEstadoCivil código del estado civil de la persona
	 * @param codigoSexo código del sexo de la persona
	 * @param primerNombrePersona primer nombre de la persona
	 * @param segundoNombrePersona segundo nombre de la persona
	 * @param primerApellidoPersona primer apellido de la persona
	 * @param segundoApellidoPersona segundo apellido de la persona
	 * @param direccion dirección de residencia de la persona
	 * @param codigoDepartamento código del departamento de residencia de la persona
	 * @param codigoCiudad código del departamento de residencia de la persona
	 * @param codigoBarrio código del barrio de residencia de la persona
	 * @param telefono teléfono de contacto de la persona
	 * @param email correo electrónico de la persona
	 * @return un objeto <code>RespuestaInsercionPersona</code> con la informacion de la insercion, y los (posibles) mensajes de error
	*/
	public static RespuestaInsercionPersona insertarPersona(Connection con, String numeroIdentificacion, String codigoTipoIdentificacion,
			String codigoDeptoId, String codigoCiudadId, String codigoPaisId,
			String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, 
			String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, 
			String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, 
			String codigoCiudad, String codigoPais, String codigoBarrio, String codigoLocalidad, String telefono, String email, String telefonoCelular, String insertarPersonaSinDocStr, String insertarPersonaStr, 
			String nombreSecuenciaPersonas, String nombreSecuenciaPersonasSinId,String tipoPersona,int codigoInstitucion, String telefonoFijo) throws SQLException
	{
		return insertarPersona(con, numeroIdentificacion, codigoTipoIdentificacion,
				codigoDeptoId, codigoCiudadId, codigoPaisId,
				codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona,
				diaNacimiento+"/"+mesNacimiento+"/"+anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, 
				segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, 
				codigoCiudad, codigoPais, codigoBarrio, codigoLocalidad, telefono, email, telefonoCelular, insertarPersonaSinDocStr, insertarPersonaStr, 
				nombreSecuenciaPersonas, nombreSecuenciaPersonasSinId,tipoPersona,codigoInstitucion,telefonoFijo);
	}

	/**
	 * Dada una conexion abierta con una base de datos Genérica y los datos basicos de
	 * una persona, hace el insert fisico de la persona, (si no existe una fuente
	 * de datos, NO la crea para no tocar la transaccionalidad del resto de
	 * inserciones / modificaciones que la usen (medico, persona o usuario)
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param numeroIdentificacion numero de identificacion de la persona
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion de la persona
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion código del departamento de expedición de la identificación de la persona
	 * @param codigoCiudadIdentificacion código de la ciudad de expedición de la identificación de la persona
	 * @param codigoTipoPersona código del tipo de persona
	 * @param fechaNacimiento Fecha de nacimiento de naciemiento de la persona (dd/mm/aaaa)
	 * @param codigoEstadoCivil código del estado civil de la persona
	 * @param codigoSexo código del sexo de la persona
	 * @param primerNombrePersona primer nombre de la persona
	 * @param segundoNombrePersona segundo nombre de la persona
	 * @param primerApellidoPersona primer apellido de la persona
	 * @param segundoApellidoPersona segundo apellido de la persona
	 * @param direccion dirección de residencia de la persona
	 * @param codigoDepartamento código del departamento de residencia de la persona
	 * @param codigoCiudad código del departamento de residencia de la persona
	 * @param codigoBarrio código del barrio de residencia de la persona
	 * @param telefono teléfono de contacto de la persona
	 * @param email correo electrónico de la persona
	 * @param tipoPersona2 
	 * @param nombreSecuenciaPersonasSinId2 
	 * @param nombreSecuenciaPersonas2 
	 * @return un objeto <code>RespuestaInsercionPersona</code> con la informacion de la insercion, y los (posibles) mensajes de error
	*/
	public static RespuestaInsercionPersona insertarPersona(Connection con, String numeroIdentificacion, String codigoTipoIdentificacion,
			String codigoDeptoId, String codigoCiudadId, String codigoPaisId,
			String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String fechaNacimiento, 
			String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, 
			String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, 
			String codigoCiudad, String codigoPais, String codigoBarrio, String codigoLocalidad, String telefono, String email, String telefonoCelular, String insertarPersonaSinDocStr, String insertarPersonaStr, 
			String nombreSecuenciaPersonas, String nombreSecuenciaPersonasSinId,String tipoPersona,int codigoInstitucion, String telefonoFijo) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=0;
		String numSeq="";
		RespuestaInsercionPersona respuesta = new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
		//se verifica que la persona no sea paciente (se le da un trato diferente)
		if(!tipoPersona.equals(ConstantesBD.tipoPersonaPaciente))
		{
			if ((codigoTipoIdentificacion.equals("MS")||codigoTipoIdentificacion.equals("AS")))
			{
				PreparedStatementDecorator insertarPersonaStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarPersonaSinDocStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				insertarPersonaStatement.setString(1, codigoTipoIdentificacion);
				insertarPersonaStatement.setString(2, codigoDepartamentoIdentificacion);
				insertarPersonaStatement.setString(3, codigoCiudadIdentificacion);
				insertarPersonaStatement.setString(4, codigoPaisIdentificacion);
				insertarPersonaStatement.setInt(5, Integer.parseInt(codigoTipoPersona) );
				if(!fechaNacimiento.equals(""))
				{
					insertarPersonaStatement.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento));
				}else
				{
					insertarPersonaStatement.setNull(6,Types.VARCHAR);
				}
				
				insertarPersonaStatement.setString(7, codigoEstadoCivil );
				insertarPersonaStatement.setInt(8, Integer.parseInt(codigoSexo) );
				insertarPersonaStatement.setString(9, primerNombrePersona );
				insertarPersonaStatement.setString(10, segundoNombrePersona );
				insertarPersonaStatement.setString(11, primerApellidoPersona);
				insertarPersonaStatement.setString(12, segundoApellidoPersona);
				insertarPersonaStatement.setString(13, direccion);
				insertarPersonaStatement.setString(14, codigoDepartamento);
				insertarPersonaStatement.setString(15, codigoCiudad);
				insertarPersonaStatement.setString(16, codigoPais);
				if(codigoBarrio!= null && !codigoBarrio.equals(""))
					insertarPersonaStatement.setInt(17, Integer.parseInt(codigoBarrio) );
				else
					insertarPersonaStatement.setNull(17, Types.INTEGER );
				if(!codigoLocalidad.equals(""))
					insertarPersonaStatement.setString(18, codigoLocalidad);
				else
					insertarPersonaStatement.setNull(18,Types.VARCHAR);
				insertarPersonaStatement.setString(19, telefono);
				insertarPersonaStatement.setString(20, email);
				insertarPersonaStatement.setString(21, codigoDeptoId);
				insertarPersonaStatement.setString(22, codigoCiudadId);
				insertarPersonaStatement.setString(23, codigoPaisId);
				if(Utilidades.convertirADouble(telefonoCelular)!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
					insertarPersonaStatement.setLong(24,Long.parseLong(telefonoCelular));
				else
					insertarPersonaStatement.setNull(24,Types.NUMERIC);
				
				if(!telefonoFijo.equals(""))
					insertarPersonaStatement.setLong(25, Long.parseLong(telefonoFijo));
				else
					insertarPersonaStatement.setLong(25, Types.NUMERIC);
				
				
				logger.info("sqlbasepersonadao.java---------------------->"+insertarPersonaStatement);	
				resp = insertarPersonaStatement.executeUpdate();
	
				if (resp==0)
				{
					return new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
				}
				else
				{
				    //int codigoPersonaEncontrado=myFactory.obtenerUltimoValorSecuencia(con, nombreSecuenciaPersonas);
					int codigoPersonaEncontrado=getIdPersonaDadaNumeroYTipoIdenticacion(con, numeroIdentificacion, codigoTipoIdentificacion);
					numSeq=myFactory.obtenerUltimoValorSecuencia(con, nombreSecuenciaPersonasSinId)+"";
					respuesta= new RespuestaInsercionPersona(true,true, numSeq, codigoPersonaEncontrado);
				}
			}
			else
			{
	
				PreparedStatementDecorator insertarPersonaStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				insertarPersonaStatement.setString(1, numeroIdentificacion);
				insertarPersonaStatement.setString(2, codigoTipoIdentificacion);
				insertarPersonaStatement.setString(3, codigoDepartamentoIdentificacion);
				insertarPersonaStatement.setString(4, codigoCiudadIdentificacion);
				insertarPersonaStatement.setString(5, codigoPaisIdentificacion);
				insertarPersonaStatement.setInt(6, Integer.parseInt(codigoTipoPersona) );
				insertarPersonaStatement.setString(7, UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento));
				insertarPersonaStatement.setString(8, codigoEstadoCivil );
				insertarPersonaStatement.setInt(9, Integer.parseInt(codigoSexo) );
				insertarPersonaStatement.setString(10, primerNombrePersona );
				insertarPersonaStatement.setString(11, segundoNombrePersona );
				insertarPersonaStatement.setString(12, primerApellidoPersona);
				insertarPersonaStatement.setString(13, segundoApellidoPersona);
				insertarPersonaStatement.setString(14, direccion);
				insertarPersonaStatement.setString(15, codigoDepartamento);
				insertarPersonaStatement.setString(16, codigoCiudad);
				insertarPersonaStatement.setString(17, codigoPais);
				insertarPersonaStatement.setInt(18, Integer.parseInt(codigoBarrio) );
				if(!codigoLocalidad.equals(""))
					insertarPersonaStatement.setString(19, codigoLocalidad);
				else
					insertarPersonaStatement.setNull(19,Types.VARCHAR);
				insertarPersonaStatement.setString(20, telefono);
				insertarPersonaStatement.setString(21, email);
				insertarPersonaStatement.setString(22, codigoDeptoId);
				insertarPersonaStatement.setString(23, codigoCiudadId);
				insertarPersonaStatement.setString(24, codigoPaisId);
				if(Utilidades.convertirADouble(telefonoCelular)!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
					insertarPersonaStatement.setLong(25,Long.parseLong(telefonoCelular));
				else
					insertarPersonaStatement.setNull(25,Types.NUMERIC);
				
				logger.info("valor telefono fijo: "+telefonoFijo);
				if(!telefonoFijo.equals(""))
					insertarPersonaStatement.setLong(26, Long.parseLong(telefonoFijo));
				else
					insertarPersonaStatement.setNull(26, Types.NUMERIC);
	
				logger.info("sqlbasepersonadao.java---------------------->"+insertarPersonaStatement);	
				resp = insertarPersonaStatement.executeUpdate();
				if (resp==0)
				{
					return new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
				}
				else
				{
					//int codigoPersonaEncontrado=myFactory.obtenerUltimoValorSecuencia(con, nombreSecuenciaPersonas);
					int codigoPersonaEncontrado=getIdPersonaDadaNumeroYTipoIdenticacion(con, numeroIdentificacion, codigoTipoIdentificacion);
					return new RespuestaInsercionPersona(true,false, "Sin problemas", codigoPersonaEncontrado);
				}
			}
		}
		else
		{
			if (Utilidades.esAutomaticoTipoId(con,codigoTipoIdentificacion,codigoInstitucion))
			{
				PreparedStatementDecorator insertarPersonaStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarPersonaSinDocStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				insertarPersonaStatement.setString(1, codigoTipoIdentificacion);
				if(!codigoDepartamentoIdentificacion.equals(""))
					insertarPersonaStatement.setString(2,codigoDepartamentoIdentificacion);
				else
					insertarPersonaStatement.setNull(2,Types.VARCHAR);
				if(!codigoCiudadIdentificacion.equals(""))
					insertarPersonaStatement.setString(3,codigoCiudadIdentificacion);
				else
					insertarPersonaStatement.setNull(3,Types.VARCHAR);
				if(!codigoPaisIdentificacion.equals(""))
					insertarPersonaStatement.setString(4,codigoPaisIdentificacion);
				else
					insertarPersonaStatement.setNull(4,Types.VARCHAR);
				insertarPersonaStatement.setInt(5, Integer.parseInt(codigoTipoPersona) );
				insertarPersonaStatement.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento));
				insertarPersonaStatement.setString(7, codigoEstadoCivil );
				insertarPersonaStatement.setInt(8, Integer.parseInt(codigoSexo) );
				insertarPersonaStatement.setString(9, primerNombrePersona );
				insertarPersonaStatement.setString(10, segundoNombrePersona );
				insertarPersonaStatement.setString(11, primerApellidoPersona);
				insertarPersonaStatement.setString(12, segundoApellidoPersona);
				insertarPersonaStatement.setString(13, direccion);
				if(!codigoDepartamento.equals(""))
					insertarPersonaStatement.setString(14,codigoDepartamento);
				else
					insertarPersonaStatement.setNull(14,Types.VARCHAR);
				if(!codigoCiudad.equals(""))
					insertarPersonaStatement.setString(15,codigoCiudad);
				else
					insertarPersonaStatement.setNull(15,Types.VARCHAR);
				if(!codigoPais.equals(""))
					insertarPersonaStatement.setString(16,codigoPais);
				else
					insertarPersonaStatement.setNull(16,Types.VARCHAR);
				if(!codigoBarrio.equals(""))
					insertarPersonaStatement.setInt(17, Integer.parseInt(codigoBarrio) );
				else
					insertarPersonaStatement.setNull(17, Types.INTEGER );
				if(!codigoLocalidad.equals(""))
					insertarPersonaStatement.setString(18, codigoLocalidad);
				else
					insertarPersonaStatement.setNull(18,Types.VARCHAR);
				insertarPersonaStatement.setString(19, telefono);
				insertarPersonaStatement.setString(20, email);
				if(!codigoDeptoId.equals(""))
					insertarPersonaStatement.setString(21, codigoDeptoId);
				else
					insertarPersonaStatement.setNull(21, Types.VARCHAR);
				if(!codigoCiudadId.equals(""))
					insertarPersonaStatement.setString(22, codigoCiudadId);
				else
					insertarPersonaStatement.setNull(22, Types.VARCHAR);
				if(!codigoPaisId.equals(""))
					insertarPersonaStatement.setString(23, codigoPaisId);
				else
					insertarPersonaStatement.setNull(23, Types.VARCHAR);
				if(Utilidades.convertirADouble(telefonoCelular)!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
					insertarPersonaStatement.setLong(24,Long.parseLong(telefonoCelular));
				else
					insertarPersonaStatement.setNull(24,Types.NUMERIC);
				
				logger.info("valor telefono fijo: "+telefonoFijo);
				if(!telefonoFijo.equals(""))
					insertarPersonaStatement.setLong(25, Long.parseLong(telefonoFijo));
				else
					insertarPersonaStatement.setNull(25, Types.NUMERIC);
				
				resp = insertarPersonaStatement.executeUpdate();
	
				if (resp==0)
				{
					return new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
				}
				else
				{
				    
					//int codigoPersonaEncontrado=myFactory.obtenerUltimoValorSecuencia(con, nombreSecuenciaPersonas);
					int codigoPersonaEncontrado= getIdPersonaDadaNumeroYTipoIdenticacion(con, numeroIdentificacion, codigoTipoIdentificacion);
					numSeq=myFactory.obtenerUltimoValorSecuencia(con, nombreSecuenciaPersonasSinId)+"";
					respuesta= new RespuestaInsercionPersona(true,true, numSeq, codigoPersonaEncontrado);
				}
			}
			else
			{
	
				PreparedStatementDecorator insertarPersonaStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("pasó por aqui 2.1");
				insertarPersonaStatement.setString(1, numeroIdentificacion);
				insertarPersonaStatement.setString(2, codigoTipoIdentificacion);
				if(!codigoDepartamentoIdentificacion.equals(""))
					insertarPersonaStatement.setString(3, codigoDepartamentoIdentificacion);
				else
					insertarPersonaStatement.setNull(3, Types.VARCHAR);
				if(!codigoCiudadIdentificacion.equals(""))
					insertarPersonaStatement.setString(4, codigoCiudadIdentificacion);
				else
					insertarPersonaStatement.setNull(4, Types.VARCHAR);
				if(!codigoPaisIdentificacion.equals(""))
					insertarPersonaStatement.setString(5, codigoPaisIdentificacion);
				else
					insertarPersonaStatement.setNull(5, Types.VARCHAR);
				logger.info("Codigo TipoPersona "+codigoTipoPersona);
				insertarPersonaStatement.setInt(6, Integer.parseInt(codigoTipoPersona) );
				
				logger.info("fecha >>"+fechaNacimiento+"<<");
				if(fechaNacimiento == null || fechaNacimiento.equals("") || fechaNacimiento.equals("//"))					
					insertarPersonaStatement.setNull(7,Types.DATE);				
				else
					
					insertarPersonaStatement.setDate(7, java.sql.Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento)));
					
					
				if(!codigoEstadoCivil.equals(""))
					insertarPersonaStatement.setString(8,codigoEstadoCivil);
				else
					insertarPersonaStatement.setNull(8,Types.VARCHAR);
								
				if(!codigoSexo.equals(""))
					insertarPersonaStatement.setInt(9,Integer.parseInt(codigoSexo));
				else
					insertarPersonaStatement.setNull(9,Types.NUMERIC);		
				
				insertarPersonaStatement.setString(10, primerNombrePersona );
				insertarPersonaStatement.setString(11, segundoNombrePersona );
				insertarPersonaStatement.setString(12, primerApellidoPersona);
				insertarPersonaStatement.setString(13, segundoApellidoPersona);
				insertarPersonaStatement.setString(14, direccion);
				
				if(!codigoDepartamento.equals(""))
					insertarPersonaStatement.setString(15,codigoDepartamento);
				else
					insertarPersonaStatement.setNull(15,Types.VARCHAR);
				
				if(!codigoCiudad.equals(""))
					insertarPersonaStatement.setString(16, codigoCiudad);
				else
					insertarPersonaStatement.setNull(16, Types.VARCHAR);
				if(!codigoPais.equals(""))
					insertarPersonaStatement.setString(17, codigoPais);
				else
					insertarPersonaStatement.setNull(17, Types.VARCHAR);
				if(!codigoBarrio.equals(""))
					insertarPersonaStatement.setInt(18, Integer.parseInt(codigoBarrio) );
				else
					insertarPersonaStatement.setNull(18, Types.INTEGER );
				
				logger.info("pasó por aqui 3 codigoLocalidad:*"+codigoLocalidad+"*");
				
				if(codigoLocalidad!=null && !codigoLocalidad.equals("") )
					insertarPersonaStatement.setString(19, codigoLocalidad);
				else
				insertarPersonaStatement.setNull(19,Types.VARCHAR);
				
				insertarPersonaStatement.setString(20, telefono);
				insertarPersonaStatement.setString(21, email);
				if(!codigoDeptoId.equals(""))
					insertarPersonaStatement.setString(22, codigoDeptoId);
				else
					insertarPersonaStatement.setNull(22, Types.VARCHAR);
				if(!codigoCiudadId.equals(""))
					insertarPersonaStatement.setString(23, codigoCiudadId);
				else
					insertarPersonaStatement.setNull(23, Types.VARCHAR);
				if(!codigoPaisId.equals(""))
					insertarPersonaStatement.setString(24, codigoPaisId);
				else
					insertarPersonaStatement.setNull(24, Types.VARCHAR);
				if(Utilidades.convertirADouble(telefonoCelular)!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
					insertarPersonaStatement.setLong(25,Long.parseLong(telefonoCelular));
				else
					insertarPersonaStatement.setNull(25,Types.NUMERIC);
				
				logger.info("valor telefono fijo: "+telefonoFijo);
				if(!telefonoFijo.equals(""))
					insertarPersonaStatement.setLong(26, Long.parseLong(telefonoFijo));
				else
					insertarPersonaStatement.setNull(26, Types.NUMERIC);
				
				resp = insertarPersonaStatement.executeUpdate();
				if (resp==0)
				{
					return new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
				}
				else
				{
					logger.info("Inserto Persona Sin Problema ");
					//int codigoPersonaEncontrado=myFactory.obtenerUltimoValorSecuencia(con, nombreSecuenciaPersonas);
					int codigoPersonaEncontrado=getIdPersonaDadaNumeroYTipoIdenticacion(con, numeroIdentificacion, codigoTipoIdentificacion);
					return new RespuestaInsercionPersona(true,false, "Sin problemas", codigoPersonaEncontrado);
				}
			}
		}
		return respuesta;
	}
	
	/**
	 * Adición de Sebastián
	 * Método para obtener el código de una persona que ya está en el sistema
	 * a través de su número y tipo de identificación
	 * @param con
	 * @param numeroId
	 * @param codigoTipoId
	 * @return
	 */
	public static int obtenerCodigoPersona(Connection con,String numeroId, String codigoTipoId){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(obtenerCodigoPersonaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroId);
			pst.setString(2,codigoTipoId);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
					return rs.getInt("codigo");
				}
			else{
				return -1;
			}
		}
		catch(SQLException e){
			logger.error("Error en obtenerCodigoPersona de SqlBasePersonaDao: "+e);
			return -1;
		}
	}
	
	/**
	 * obtiene el id de persona dado el numero y tipo identificacion
	 * @param con
	 * @param numeroIdentificacion
	 * @param codigoTipoIdentificacion
	 * @return
	 */
	private static int getIdPersonaDadaNumeroYTipoIdenticacion(Connection con, String numeroIdentificacion, String codigoTipoIdentificacion)
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(getIdPersonaDadoNumeroTipoId,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setString(1, numeroIdentificacion);
			statement.setString(2, codigoTipoIdentificacion);
			ResultSetDecorator rs=new ResultSetDecorator(statement.executeQuery());
			if(rs.next())
				resultado=rs.getInt("idPersona");
		}
		catch (SQLException e)
		{
			logger.error("Error getIdPersonaDadaNumeroYTipoIdenticacion (SqlBaseUtilidadValidacionDao): "+e);
			return ConstantesBD.codigoNuncaValido;
		}
		return resultado;
	}
	
	
	/**
	 * Método para obtener apellidos nombres de una persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerApellidosNombresPersona(Connection con,int codigoPersona)
	{
		String nombres = "";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerApellidosNombresPersonaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPersona);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				nombres = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerApellidosNombresPersona: "+e);
		}
		return nombres;
	}
	
	
	//****************************************************************************
	
	/**
	 * Actualiza la informacion del triage
	 * @param Connection ac_con
	 * @param int ai_codigoPersona
	 * @param String ls_tipoIdAnterior
	 * @param String ls_IdAnterior
	 * @param String as_tipoId
	 * @param String as_numeroId
	 * @param String as_primerNombrePersona
	 * @param String as_segundoNombrePersona
	 * @param String as_primerApellidoPersona
	 * @param String as_segundoApellidoPersona
	 * @param String ls_fechaNacimiento
	 * @param int codigoInstitucion
	 * */
	public static boolean actualizarTriage(
											Connection ac_con,
											int ai_codigoPersona,											
											String as_tipoId,
											String as_numeroId,
											String as_primerNombrePersona,
											String as_segundoNombrePersona,
											String as_primerApellidoPersona,
											String as_segundoApellidoPersona,
											String ls_fechaNacimiento,
											int codigoInstitucion
			)
	{
		try
		{			
			PreparedStatementDecorator	lps_consulta;
			ResultSetDecorator			lrs_rs;	
			
			lps_consulta = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_consulta.setInt(1, ai_codigoPersona);

			(lrs_rs = new ResultSetDecorator(lps_consulta.executeQuery()) ).next();
			
			//verifica si el numero y tipo de identificacion cambiaron en ese caso se actualiza la información 
			//de triage si posee una.			
			if(!lrs_rs.getString("tipo_identificacion").equals("") && !lrs_rs.getString("numero_identificacion").equals("") && 
					(!as_tipoId.trim().equals(lrs_rs.getString("tipo_identificacion")) || 
							!as_numeroId.trim().equals(lrs_rs.getString("numero_identificacion")) || 
								!as_primerNombrePersona.equals(lrs_rs.getString("primer_nombre")) ||
									!as_segundoNombrePersona.equals(lrs_rs.getString("segundo_nombre")) ||
										!as_primerApellidoPersona.equals(lrs_rs.getString("primer_apellido")) ||
											!as_segundoApellidoPersona.equals(lrs_rs.getString("segundo_apellido")) ||
												!ls_fechaNacimiento.equals(lrs_rs.getString("fecha_nacimiento")))
							)
			{				
				PreparedStatementDecorator	lps_update;								
				
				lps_update = new PreparedStatementDecorator(ac_con.prepareStatement(modifica_triage,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				lps_update.setString(1,as_tipoId);					
				lps_update.setString(2,as_numeroId);
				lps_update.setString(3,as_primerNombrePersona);
				lps_update.setString(4,as_segundoNombrePersona);
				lps_update.setString(5,as_primerApellidoPersona);
				lps_update.setString(6,as_segundoApellidoPersona);
				lps_update.setString(7,ls_fechaNacimiento);
				
				lps_update.setString(8,lrs_rs.getString("tipo_identificacion"));
				lps_update.setString(9,lrs_rs.getString("numero_identificacion"));
				lps_update.setInt(10,codigoInstitucion);
											
				if(lps_update.executeUpdate()>0)
				{
					logger.info("Se actualizo información del paciente triage. " +
							" tipo Identificacion Anterior >> "+lrs_rs.getString("tipo_identificacion")+" >> "+as_tipoId+
							" Numero identificación Anterior >> "+lrs_rs.getString("numero_identificacion")+" >> "+as_numeroId+
							" Primer nombre >>  "+as_primerNombrePersona+" >>  "+lrs_rs.getString("primer_nombre")+
							" Segundo nombre >> "+as_segundoNombrePersona+" >> "+lrs_rs.getString("segundo_nombre")+
							" Primer Apellido >>  "+as_primerApellidoPersona+" >>  "+lrs_rs.getString("primer_apellido")+
							" Segundo Apellido >>  "+as_segundoApellidoPersona+" >>  "+lrs_rs.getString("segundo_apellido")+
							" fecha Nacimiento >>  "+ls_fechaNacimiento+" >>  "+lrs_rs.getString("fecha_nacimiento")
					);
					return true;
				}		
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Método implementado para cargar los datos de la persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> cargarPersona(Connection con,int codigoPersona)
	{
		String cargarPersonaStr = "SELECT " +
				"p.codigo AS codigopersona, "+ 
				"p.numero_identificacion as numero_identificacion, "+
				"p.tipo_identificacion as codigo_tipo_identificacion, "+
				"administracion.getnombretipoidentificacion(p.tipo_identificacion) as nombre_tipo_identificacion, "+
				"p.codigo_departamento_nacimiento as codigo_depto_nacimiento, "+
				"administracion.getnombredepto(p.codigo_pais_nacimiento,p.codigo_departamento_nacimiento) as nombre_depto_nacimiento, "+
				"p.codigo_ciudad_nacimiento as codigo_ciudad_nacimiento, "+
				"administracion.getnombreciudad(p.codigo_pais_nacimiento,p.codigo_departamento_nacimiento,p.codigo_ciudad_nacimiento) as nombre_ciudad_nacimiento, "+
				"p.codigo_pais_nacimiento as codigo_pais_nacimiento, "+
				"administracion.getdescripcionpais(p.codigo_pais_nacimiento) as nombre_pais_nacimiento, "+
				"p.tipo_persona as codigo_tipo_persona, " +
				"tp.nombre as nombre_tipo_persona, "+
				"to_char(p.fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"') as fecha_nacimiento, "+
				"p.estado_civil as codigo_estado_civil, "+
				"ec.nombre as nombre_estado_civil, "+
				"p.sexo as codigo_sexo, "+
				"administracion.getdescripcionsexo(p.sexo) as nombre_sexo, "+
				"coalesce(p.libreta_militar,'') as libreta_militar, "+
				"p.primer_nombre as primer_nombre, "+
				"coalesce(p.segundo_nombre,'') as segundo_nombre, "+
				"p.primer_apellido as primer_apellido, "+
				"coalesce(p.segundo_apellido,'') as segundo_apellido, "+
				"p.direccion as direccion, "+
				"p.codigo_departamento_vivienda as codigo_depto_vivienda, "+
				"administracion.getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) as nombre_depto_vivienda, "+
				"p.codigo_ciudad_vivienda as codigo_ciudad_vivienda, "+
				"administracion.getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda) as nombre_ciudad_vivienda, "+
				"p.codigo_pais_vivienda as codigo_pais_vivienda, "+
				"administracion.getdescripcionpais(p.codigo_pais_vivienda) as nombre_pais_vivienda, "+
				"p.codigo_barrio_vivienda as codigo_barrio_vivienda, "+
				"carterapaciente.getdescripcionbarrio(p.codigo_barrio_vivienda) as nombre_barrio_vivienda, "+
				"coalesce(p.telefono,'') as telefono, "+
				"coalesce(p.email,'') as email, "+
				"p.codigo_depto_id as codigo_depto_id, "+
				"administracion.getnombredepto(p.codigo_pais_id,p.codigo_depto_id) as nombre_depto_id, "+
				"p.codigo_ciudad_id as codigo_ciudad_id, "+
				"administracion.getnombreciudad(p.codigo_pais_id,p.codigo_depto_id,p.codigo_ciudad_id) as nombre_ciudad_id, "+
				"p.codigo_pais_id as codigo_pais_id, "+
				"administracion.getdescripcionpais(p.codigo_pais_id) as nombre_pais_id, "+
				"coalesce(p.codigo_localidad_vivienda,'') as codigo_localidad, "+
				"getdesclocalidad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda,p.codigo_localidad_vivienda) as nombre_localidad," +
				"coalesce(to_char(p.telefono_fijo,'9999999'),'') AS telefonofijo, "+
				"coalesce(to_char(p.telefono_celular,'9999999999'),'') AS telefonocelular," +
				"p.indicativo_interfaz as indicativoInterfaz "+
			"FROM " +
				"administracion.personas p "+ 
			"INNER JOIN " +
				"administracion.tipos_personas tp ON(tp.codigo = p.tipo_persona) "+ 
			"LEFT OUTER JOIN " +
				"administracion.estados_civiles ec ON(ec.acronimo = p.estado_civil) " +
			"WHERE "+ 
				"p.codigo = ?";

		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,cargarPersonaStr);
			pst.setInt(1,codigoPersona);
			logger.info(pst);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPersona: "+e);
			resultados.put("numRegistros", "0");
			
		}
		return resultados;
	}
	
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return firma del medico 
	 */
	public static  String obtenerFirmaDigitalMedico(Connection con,Integer codigoMedico){
		String res = "";

		String consulta = " SELECT FIRMA_DIGITAL " + 
						  "	FROM ADMINISTRACION.MEDICOS " + 
						  " WHERE CODIGO_MEDICO = ? ";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoMedico);
			ResultSet resu = pst.executeQuery();

			if(resu.next()){
				res = resu.getString("firma_digital");
			}

		}catch(SQLException e)
		{
			logger.error("Error en consultando firmas de medicos: "+e);


		}
		return res;
	}
	
	
}