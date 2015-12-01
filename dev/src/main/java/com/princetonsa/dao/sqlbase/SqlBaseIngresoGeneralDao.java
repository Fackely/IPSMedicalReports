/*
 * @(#)SqlBaseIngresoGeneralDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoIngresosFactura;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosAcronimo;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a IngresoGeneral
 *
 *	@version 1.0, Mar 29, 2004
 */
public class SqlBaseIngresoGeneralDao 
{
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar un Ingreso General desde la base de datos.
	 */
	private static final String cargarIngreso="select per.numero_identificacion as numeroIdentificacion, per.tipo_identificacion as codigoTipoIdentificacion, tipid.nombre as tipoIdentificacion, per.primer_nombre as primerNombre, per.segundo_nombre as segundoNombre, per.primer_apellido as primerApellido, per.segundo_apellido as segundoApellido, parafecha.fechaApertura as fechaIngreso, parafecha.horaApertura as horaIngreso, ing.fecha_egreso as fechaEgreso, ing.hora_egreso as horaEgreso, ing.institucion as codigoInstitucion, emp.razon_social as institucion from ingresos ing, tipos_identificacion tipid, personas per, (SELECT min(fecha_apertura) as fechaApertura, min(hora_apertura) as horaApertura , id_ingreso from cuentas group by id_ingreso) parafecha, empresas emp where per.tipo_identificacion=tipid.acronimo and ing.codigo_paciente=per.codigo and ing.id=parafecha.id_ingreso and ing.institucion=emp.codigo and ing.id=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar un ingreso general en la base de datos.
	 */
	private static final String modificarIngreso="UPDATE ingresos SET fecha_egreso=?, hora_egreso=? where id=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar un ingreso general en la base de datos.
	 */
	private static final String modificarFechaHoraIngresoStr= "UPDATE ingresos SET fecha_egreso=CURRENT_DATE, hora_egreso="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" where id=? ";
	
	/**
	 * Cadena constante con el <i>statemente</i> necesario para consultar los codigos de los ingresos asociados a un paciente en una institucion
	 */
	private static final String buscarIngresoActivoStr="SELECT id from ingresos where codigo_paciente=? and fecha_egreso is null and institucion=?";

	/**
	 * Cadena constante con el <i>statemente</i> necesario para consultar los codigos de los ingresos asociados a un paciente en una institucion
	 */
	private static final String consultarCodigosIngresosPacienteStr =  "SELECT id as idIngreso FROM ingresos WHERE codigo_paciente = ? AND institucion = ? order by id desc";

	/**
	 * Cadena constante con el statement necesario para consultar los datos del titular de la poliza segun una cuenta dada
	 */
	private static final String consultarInfoAdicionalPolizaStr=" SELECT tp.cuenta as cuenta, " +
																" tp.nombres_titular as nombreTitular," +
																" tp.apellidos_titular as apellidoTitular," +
																" tp.tipoid_titular as tipoId, " +
																" getnombretipoidentificacion(tp.tipoid_titular) AS nombreTipoId, "+
																" tp.numeroid_titular as numeroId," +
																" tp.direccion_titular as direccion," +
																" tp.telefono_titular as telefono " +
																" FROM titular_poliza as tp " +
																" WHERE tp.cuenta=? ";
	
	/**
	 * Cadena constante con el statement necesario para saber los datos de autorizacion de la poliza segun una cuenta dada
	 */
	private static final String consultarDatosAutorizacionPolizaStr=" SELECT ip.codigo as codigo, " +
																	" ip.fecha_autorizacion as fechaAutorizacion, " +
																	" ip.numero_autorizacion as numeroAutorizacion," +
																	" ip.valor_monto_autorizado as valorMonto " +
																	" FROM informacion_poliza as ip " +
																	" WHERE ip.convenio=? and ip.cuenta=? ";
	
	/**
	 * Cadena constante con el statememt necesario para acutalizar los datos del titular de la poliza
	 */
	private static final String actualizarDatosTitularPolizaStr=" UPDATE titular_poliza " +
															    " SET nombres_titular = ?, " +
															    " apellidos_titular = ?, " +
															    " tipoid_titular = ?, " +
															    " numeroid_titular = ?, " +
															    " direccion_titular = ?, " +
															    " telefono_titular = ? " +
															    " WHERE cuenta = ? " +
															    " AND convenio= ?";
	
	/**
	 * Cadena con el statemen necesario para insertar la inforacion del titular de la poliza
	 */
	private static final String ingresarDatosTitularPolizaStr=" INSERT INTO  titular_poliza" +
													       " (nombres_titular, " +
													       " apellidos_titular," +
													       " tipoid_titular," +
													       " numeroid_titular," +
													       " direccion_titular," +
													       " telefono_titular," +
													       " cuenta," +
													       " convenio)" +
													       " VALUES(?,?,?,?,?,?,?,?) ";
	
	/**
	 * Cadena constante con el statement necesario para actualizar los datos de la autorizacion
	 */
	public static final String actualizarDatosPolizaStr=" UPDATE informacion_poliza " +
														" SET fecha_autorizacion = ?," +
														" numero_autorizacion = ?, " +
														" valor_monto_autorizado = ?, " +
														" fecha_grabacion = ?, " +
														" hora_grabacion = ?, " +
														" usuario = ? "+
														" WHERE cuenta = ? " +
														" AND convenio = ? " +
														" AND numero_autorizacion = ? " +
														" AND valor_monto_autorizado= ? ";
	
	/**
	 * Cadena con el statement necesario para borrar los datos previos de la autorizacion
	 */
	public static final String borrarDatosPreviosPolizaStr=" DELETE FROM informacion_poliza " +
														   " WHERE cuenta=? AND convenio=? ";
	
	/**Cadena con el statement necesario para saber si existe un dato de autorizacion**/
	public static final String existeDatoAutorizacionPolizaStr=" SELECT count(1) as cantidad " +
															   " FROM informacion_poliza " +
															   " WHERE convenio = ? " +
															   " AND cuenta = ? "+ 
															   " AND codigo = ?";
															   
	
	
	public static final String eliminarDatosInformacionPolizaStr=" DELETE FROM informacion_poliza " +
															  	 " WHERE codigo=?";
	
	/**
	 * Cadena para consultar el codigo del ingreso recien insertado de un paciente
	 */
	private static final String buscarCodigoIngresoRecienInsertadoStr = "SELECT max(id) as codigoIngreso FROM ingresos WHERE codigo_paciente = ?";
	
	private static final String strCadenaActualizarTransplante = " UPDATE ingresos SET transplante=? where id=? ";
	
	
	
	
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseIngresoGeneralDao.class);

	/**
	 * Carga la información relevante a un ingreso general y la almacena en su objeto respectivo.
	 * @param con una conexión abierta con una base de datos Genérica
	 * @param idIngreso número de identificación del ingreso
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los datos del ingreso
	 * y una conexión abierta con la base de datos Genérica
	 */
	public static Answer cargarIngreso(Connection con,String idIngreso) throws SQLException
	{
		ResultSetDecorator rs=null;
		PreparedStatementDecorator cargarIngresoStatement= null;
		try{
			
			cargarIngresoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarIngreso));
			cargarIngresoStatement.setString(1,idIngreso);
			rs=new ResultSetDecorator(cargarIngresoStatement.executeQuery());
		
		}catch(SQLException sqlException){
			throw sqlException;
		}
		return new Answer (rs,con);
		
	}

	/**
	 * Carga la información relevante a un ingreso general y la almacena en su objeto respectivo.
	 * @param con una conexión abierta con una base de datos Genérica
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param tipoIdentificacionPaciente tipo de identificación del paciente
	 * @param codigoInstitucion código de la institución desde la cual se va a cargar este ingreso
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los datos del ingreso
	 * y una conexión abierta con la base de datos Genérica
	 */
	public static Answer cargarIngreso(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		int id;
		id=buscarIdIngreso (con, codigoPaciente, codigoInstitucion);
		return cargarIngreso(con, id + "");
	}

	
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
	public static boolean actualizarTransplante (Connection connection, HashMap criterios)
	{
		logger.info("\n entro a actualizarTransplante --> "+criterios);
		String cadena =strCadenaActualizarTransplante;
		PreparedStatementDecorator ps =  null;
		try
		{
			 ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			//transplante ---> puede ser (DONANT,RECEPT)
		
			if(UtilidadCadena.noEsVacio(criterios.get("transplante")+""))
				ps.setObject(1,criterios.get("transplante"));
			else
				ps.setNull(1,Types.VARCHAR);
			
			ps.setObject(2, criterios.get("ingreso"));
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n problema ingresando el transplante "+e);
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		
		return false;
	}
	
	
	 
	/**
	 * Metodo encargado de consultar si un ingreso fue por
	 * entidades subcontratadas o no.
	 * Adicionado por Jhony Alexander Duque A.
	 * 21/01/2008
	 * @param connection
	 * @param codigoIngreso
	 * @param i 
	 * @return S/N
	 * 
	 */
	public static String esIngresoComoEntidadSubContratada (Connection connection, int codigoIngreso, int tipoBD)
	{
		String result="";
		
		String cadena ="select CASE WHEN pac_entidades_subcontratadas IS NULL THEN 'N' ELSE 'S' end from ingresos where id="+codigoIngreso; 
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			logger.info("-->"+cadena);
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				result = rs.getString(1);
			}
	
		}
		catch (SQLException e)
		{
			logger.info("\n problema consultando es ingreso entidad subcontratada "+e);
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
		
		
		return result;
	}
	
	
	/**
	 * Modifica la información de un Ingreso General en la base de datos Genérica.
	 * @param con una conexión abierta con una base de datos Genérica
	 * @param horaIngreso hora en que se efectúa el ingreso
	 * @param codigoTipoPaciente código del tipo de paciente
	 * @param codigoViaIngreso código de la vía de ingreso
	 * @param estadoIngreso estado del ingreso
	 * @param idIngreso número de identificación del ingreso
	 * @return número de ingresos modificados en la base de datos Genérica
	 */
	public static int modificarIngreso(Connection con, String anioEgreso, String mesEgreso, String diaEgreso, String horaEgreso, String idIngreso) throws SQLException
	{
		int resp=0;
		PreparedStatementDecorator ps= null;
		try {
			
			ps= new PreparedStatementDecorator(con.prepareStatement(modificarIngreso));
			ps.setString(1,diaEgreso + "-" + mesEgreso + "-" + anioEgreso);
			ps.setString(2, horaEgreso);
			ps.setString(3, idIngreso);
			resp=ps.executeUpdate();
		} catch (SQLException e) {
			  logger.error("Error en reversarFechaHoraEgreso de SqlBaseIngresoGeneralDao: "+e);
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;
	}
	
	/**
	 * metodo que deja vacias (abierta) la fecha y hora de egreso en la table ingresos
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean reversarFechaHoraEgreso(Connection con, int idCuenta)
	{
		PreparedStatement ps =  null;
	    try
	    {
		    ps= new PreparedStatementDecorator(con.prepareStatement(modificarIngreso));
			ps.setObject(1,null);
			ps.setObject(2,null);
			ps.setInt(3,idCuenta);
			int resp=ps.executeUpdate();
			ps.close();
			if(resp>0)
			    return true;
			else
			    return false;
	    }	
	    catch(SQLException e)
		{
	        logger.error("Error en reversarFechaHoraEgreso de SqlBaseIngresoGeneralDao: "+e);
			return false;
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}	
	}
	
	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static int modificarFechaHoraIngreso(Connection con, String idIngreso)
	{
	    int resp=0;
	    PreparedStatement pst=null;
	    try
	    {
		    pst =  con.prepareStatement(modificarFechaHoraIngresoStr);
		    pst.setString(1, idIngreso);
		    resp=pst.executeUpdate();
		    return resp;
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR modificarFechaHoraIngreso",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR modificarFechaHoraIngreso", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
	    return 0;
	}

	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean actualizarEstadoIngreso(Connection con, String idIngreso, String estado, String loginUsuario)
	{
		String consulta="UPDATE ingresos SET estado=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE id=? ";
		logger.info("actualizarEstadoIngreso->"+consulta+" estado->"+estado+" idIngreso->"+idIngreso);
		PreparedStatement ps =  null;
		try
	    {
		    ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
		    ps.setString(1, estado);
		    ps.setString(2, loginUsuario);
		    ps.setString(3, idIngreso);
		    if(ps.executeUpdate()>0)
		    	return true;
	    }
	    catch(SQLException e)
	    {
	        logger.error("error actualizarEstadoIngreso");
	        e.printStackTrace();
	    }finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
	    return false;
	}
	
	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean actualizarControlPostOperatorioCx(Connection con, String idIngreso, String estado, String loginUsuario)
	{
		String consulta="UPDATE ingresos SET control_post_operatorio_cx=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE id=? ";
		logger.info("actualizarEstadoIngreso->"+consulta+" estado->"+estado+" idIngreso->"+idIngreso);
		PreparedStatement ps =  null;
		try
	    {
		    ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
		    ps.setString(1, estado);
		    ps.setString(2, loginUsuario);
		    ps.setString(3, idIngreso);
		    if(ps.executeUpdate()>0)
		    	return true;
	    }
	    catch(SQLException e)
	    {
	        logger.error("error actualizarEstadoIngreso");
	        e.printStackTrace();
	    }finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
	    return false;
	}
	
	
	
	
	/**
	 * Modifica la información de un Ingreso General en la base de datos Genérica.
	 * @param con una conexión abierta con una base de datos Genérica
	 * @param horaIngreso hora en que se efectúa el ingreso
	 * @param codigoTipoPaciente código del tipo de paciente
	 * @param codigoViaIngreso código de la vía de ingreso
	 * @param estadoIngreso estado del ingreso
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param tipoIdentificacionPaciente tipo de identificación del paciente
	 * @param codigoInstitucion código de la institución de la cual se desea modificar un ingreso
	 * @return número de ingresos modificados en la base de datos Genérica
	 */
	public static int modificarIngreso(Connection con, String anioEgreso, String mesEgreso, String diaEgreso, String horaEgreso, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		int id = buscarIdIngreso (con, codigoPaciente, codigoInstitucion);
		return modificarIngreso(con, anioEgreso, mesEgreso, diaEgreso, horaEgreso, id + "");
	}

	/**
	 * Este metodo me permite buscar un id de ingreso abierto , dado el codigo del paciente.
	 * OJO debe ser usado con cuidado y siempre junto a una validación importante.
	 * Si no existe un ingreso abierto para este paciente retorna 0
	 * @param con una conexión abierta con una base de datos Genérica
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param tipoIdentificacionPaciente tipo de la identificación del paciente
	 * @param codigoInstitucion código de la institución en la cual se va a buscar un ingreso
	 * @return número de identificación del ingreso correspondiente a los datos del paciente.
	 */
	public static int buscarIdIngreso (Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		int id=0;
		
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;

		try {
			
			ps= new PreparedStatementDecorator(con.prepareStatement(buscarIngresoActivoStr));
			ps.setInt(1, codigoPaciente);
			ps.setString(2, codigoInstitucion);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
			{
				id=rs.getInt("id");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
		}
			
		

		return id;
	}

	/**
	 * Implementación de la consulta de codigos de ingresos asociados a un paciente en una institucion
	 * @see com.princetonsa.dao.IngresoGeneralDao#getCodigosIngresosPaciente (con, String, String, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */	
	public static ResultSetDecorator getCodigosIngresosPaciente(Connection con, int codigoPaciente, int codigoInstitucion) throws SQLException
	{				
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		PreparedStatementDecorator ps= null;
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarCodigosIngresosPacienteStr));
			ps.setInt(1, codigoPaciente);
			ps.setInt(2, codigoInstitucion);										
			return new ResultSetDecorator(ps.executeQuery());
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando ingresos asociados al paciente con id"+codigoPaciente+" en la tabla 'ingresos'.\n Se lanzó la siguiente excepción:\n"+sql);
			throw sql;
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
	}

	public static int empezarTransaccion(Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (myFactory.beginTransaction(con))
	    {
	        return 1;
	    }
	    else
	    {
	        return 0;
	    }
	}

	/**
	 * Inserta en la base de datos Genérica un nuevo Ingreso General
	 * especificando el estado de la transacción.
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoPaciente Código que identifica al paciente
	 * en el sistema
	 * @param institucion Código de la institución donde se realiza
	 * este ingreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @param transplante
	 * @return número de ingresos efectuados (debe ser 1)
	 */
	public static int insertarIngresoTransaccional(Connection con, int codigoPaciente, String institucion, String estado, String estadoIngreso, String usuario, String consecutivo,String anioConsecutivo,int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso,String secuencia,String transplante) throws SQLException
	{
		logger.info("\n entre a insertarIngresoTransaccional -->"+transplante);
		
		int numElementosInsertados=0;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	    	if (!myFactory.beginTransaction(con))
	        {
	        	myFactory.abortTransaction(con);
	        }
	    }
	    PreparedStatement buscarCodigoIngresoStatement=  null;
	    PreparedStatement insertarIngresoStatement=  null;
	    ResultSet rs =  null;
	    try
	    {
	    	if(fechaIngreso.equals(""))
	    		fechaIngreso = "CURRENT_DATE";
	    	else
	    		fechaIngreso = "'" + UtilidadFecha.conversionFormatoFechaABD(fechaIngreso) + "'";
	    	
	    	if(horaIngreso.equals(""))
	    		horaIngreso = "'" + UtilidadFecha.getHoraActual() + "'";
	    	else
	    		horaIngreso = "'" + UtilidadFecha.convertirHoraACincoCaracteres(horaIngreso) + "'";
	    	
	    	
	    	String insertarIngresoStr = "INSERT INTO ingresos (" +
	    		"id, " +
	    		"codigo_paciente, " +
	    		"institucion, " +
	    		"fecha_ingreso, " +
	    		"hora_ingreso, " +
	    		"estado, " +
	    		"fecha_modifica, " +
	    		"hora_modifica, " +
	    		"usuario_modifica," +
	    		"consecutivo," +
	    		"anio_consecutivo," +
	    		"centro_atencion," +
	    		"pac_entidades_subcontratadas," +
	    		"transplante) " +
	    		"VALUES ("+secuencia+", ?,  ?, "+fechaIngreso+", "+horaIngreso+",?,CURRENT_DATE,'"+UtilidadFecha.getHoraActual()+"',?,?,?,?,?,?)";
	    	//""
			 insertarIngresoStatement= con.prepareStatement(insertarIngresoStr);
			insertarIngresoStatement.setInt(1, codigoPaciente);
			insertarIngresoStatement.setString(2, institucion);
			insertarIngresoStatement.setString(3, estadoIngreso);
			insertarIngresoStatement.setString(4, usuario);
			insertarIngresoStatement.setString(5, consecutivo);
			insertarIngresoStatement.setString(6, anioConsecutivo);
			insertarIngresoStatement.setInt(7, centroAtencion);
			if(!pacEntidadSubcontratada.equals(""))
				insertarIngresoStatement.setInt(8,Integer.parseInt(pacEntidadSubcontratada));
			else
				insertarIngresoStatement.setNull(8,Types.NUMERIC);
			
			//transplante
			if(UtilidadCadena.noEsVacio(transplante))
				insertarIngresoStatement.setObject(9,transplante);
			else
				insertarIngresoStatement.setNull(9,Types.VARCHAR);
			
			
	    	numElementosInsertados=insertarIngresoStatement.executeUpdate();
	    	insertarIngresoStatement.close();  
	        if (numElementosInsertados<=0)
	        {
	        	myFactory.abortTransaction(con);
	        	return numElementosInsertados; 
	        }
	        
			buscarCodigoIngresoStatement= con.prepareStatement(buscarCodigoIngresoRecienInsertadoStr);
			buscarCodigoIngresoStatement.setInt(1,codigoPaciente);
			 rs=buscarCodigoIngresoStatement.executeQuery();
			if (rs.next())
			{
			    numElementosInsertados= rs.getInt("codigoIngreso");
			}

			if (numElementosInsertados<=0)
	        {
	        	myFactory.abortTransaction(con);
	        	return numElementosInsertados; 
	        }
		
	    }
	    catch (SQLException e)
	    {
	    	myFactory.abortTransaction(con);
	        throw e;
	    }finally{
			try{
				if(buscarCodigoIngresoStatement!=null){
					buscarCodigoIngresoStatement.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(insertarIngresoStatement!=null){
					insertarIngresoStatement.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
	      
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	    	myFactory.endTransaction(con);
	    }
	    return numElementosInsertados;
	}

	/**
	 * Inserta en la base de datos Genérica un nuevo Ingreso General. Tiene que tener mucho cuidado en
	 * siempre insertar primero el ingreso y DESPUES la cuenta.
	 * @param con una conexión abierta con una base de datos Genérica
	 * @param tipoId tipo de identificación del paciente a ser insertado
	 * @param numeroId número de identificación del paciente
	 * @param anioIngreso año en que se efectúa el ingreso
	 * @param mesIngreso mes en que se efectúa el ingreso
	 * @param diaIngreso día en que se efectúa el ingreso
	 * @param horaIngreso hora en que se efectúa el ingreso
	 * @param institucion nombre de la institución en la que se va a insertar este ingreso
	 * @param transplante
	 * @return número de ingresos efectuados (debe ser 1)
	 */
	public static int insertarIngreso(Connection con, int codigoPaciente, String institucion, String estado, String usuario,String consecutivo,String anioConsecutivo,int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso, String secuencia,String transplante) throws SQLException
	{
		
		logger.info("\n entre a insertarIngreso --> "+transplante);
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    int resp=insertarIngresoTransaccional(con, codigoPaciente, institucion, ConstantesBD.inicioTransaccion, estado, usuario,consecutivo,anioConsecutivo, centroAtencion,pacEntidadSubcontratada,fechaIngreso,horaIngreso,secuencia,transplante);
	    if (resp>0)
	    {
	        myFactory.endTransaction(con);
	    }
	    else
	    {
	        myFactory.abortTransaction(con);
	    }
	    return resp;
	}
	
	/**
	 * Método para nsertar los datos adicional para la poliza del convenio que lo requiera
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
	public static int ingresarInfoAdicionalPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, String ingresarInfoAdicionalPolizaStr) 
	{
		int resp=0;	
		PreparedStatementDecorator ps= null;
		PreparedStatementDecorator pst= null;
		try
		{
			if(!Utilidades.convenioTieneIngresoInfoAdic(con, codigoConvenio))
			{
				resp=1;
			}
			else
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(ingresarInfoAdicionalPolizaStr));
				ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAutorizacion)));
				ps.setString(2, numeroAutorizacion);
				ps.setInt(3, valorMonto);
				ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				ps.setString(5, UtilidadFecha.getHoraActual());
				ps.setString(6, usuario);
				ps.setInt(7, idCuenta);
				ps.setInt(8, codigoConvenio);
				resp=ps.executeUpdate();
				
				if(resp>0)
				{
					pst= new PreparedStatementDecorator(con.prepareStatement(ingresarDatosTitularPolizaStr));
					pst.setString(1, nombreTitular);
					pst.setString(2, apellidoTitular);
					pst.setString(3, tipoId);
					pst.setString(4, numeroId);
					pst.setString(5, direccion);
					pst.setString(6, telefono);
					pst.setInt(7, idCuenta);
					pst.setInt(8, codigoConvenio);
					pst.executeUpdate();
				}
			}
				
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error insercion de los datos del Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			resp=0;			
		}	finally{
			try{
				if(ps!=null){
					ps.close();
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(pst!=null){
					pst.close();
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;	
	}
	
	
	
	/**
	 * Método para consultar los datos del titular de la poliza
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap consultarInformacionTitularPoliza(Connection con, int idCuenta)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarInfoAdicionalPolizaStr));
			ps.setInt(1, idCuenta);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error en la consulta de los datos del Titular de la Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			return null;			
		}	finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}	
		
	}
	
	/**
	 * Metodo para consultar los datos de autorizacion de la poliza
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap consultarDatosAutorizacionPoliza(Connection con, int codigoConvenio, int idCuenta)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarDatosAutorizacionPolizaStr));
			ps.setInt(1, codigoConvenio);
			ps.setInt(2, idCuenta);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error en la consulta de los datos de autorizacion de la Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			return null;			
		}	finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}	
		
	}
	
	/**
	 * Metodo para actualizar los datos del titular de la poliza
	 * @param con
	 * @param codigoConvenio
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
	public static int actualizarInfoTitularPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono) 
	{
		int resp=0;	
		PreparedStatementDecorator ps= null;
		try{
				
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosTitularPolizaStr));
				ps.setString(1, nombreTitular);
				ps.setString(2, apellidoTitular);
				ps.setString(3, tipoId);
				ps.setString(4, numeroId);
				ps.setString(5, direccion);
				ps.setString(6, telefono);
				ps.setInt(7, idCuenta);
				ps.setInt(8, codigoConvenio);
			
				resp=ps.executeUpdate();
				
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error actualizacion de los datos del titular de la  Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			resp=0;			
		}	finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;	
	}
	
	
	/**
	 * Método para actualizar los datos de la poliza
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public static int actualizarInformacionPoliza(Connection con, int idCuenta, int codigoConvenio, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo, String numeroAutoAux, int montoAux, String ingresarInfoAdicionalPolizaStr) 
	{
		int resp=0;	
		int cantidad=0;
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
				
			pst= new PreparedStatementDecorator(con.prepareStatement(existeDatoAutorizacionPolizaStr));
			pst.setInt(1, codigoConvenio);
			pst.setInt(2, idCuenta);
			pst.setInt(3, codigo);
			
			
			rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				cantidad=rs.getInt("cantidad");
			}
			if(cantidad>0)
			{
				actualizarInformacionPolizaBasica(con, codigoConvenio, idCuenta, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, numeroAutoAux, montoAux);
			}
			else
			{
				ingresarInfoAdicionalPolizaBasica(con, codigoConvenio, idCuenta, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, ingresarInfoAdicionalPolizaStr);
			}
				
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error actualizacion de los datos del titular de la  Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			resp=0;			
		}	finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
		return resp;	
	}
	
	
   /**
    * Método apra actualizar los datos de la autorizacion de la poliza segun una cuenta dada
    * @param con
    * @param idCuenta
    * @param fechaAutorizacion
    * @param numeroAutorizacion
    * @param valorMonto
    * @return
    */
	public static int actualizarDatosAutorizacion(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario,int codigo, String numeroAutoAux, int montoAux, String ingresarInfoAdicionalPolizaStr) 
	{
		int resp=0;	
		int cantidad=0;
		
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		
		try
		{
				
				pst= new PreparedStatementDecorator(con.prepareStatement(existeDatoAutorizacionPolizaStr));
				pst.setInt(1, codigoConvenio);
				pst.setInt(2, idCuenta);
				pst.setInt(3, codigo);
				
				rs=new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					cantidad=rs.getInt("cantidad");
				}
				if(cantidad<=0)
				{
					ingresarInfoAdicionalPoliza(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, ingresarInfoAdicionalPolizaStr); 
				}
				else
				{
					actualizarInfoTitularPoliza(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono);
					actualizarInformacionPoliza(con, idCuenta, codigoConvenio, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, codigo, numeroAutoAux, montoAux, ingresarInfoAdicionalPolizaStr);
				}
				
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error actualizando de los datos de la autorizacion de la Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			resp=0;			
		}	finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;	
	}
	
	/**
	 * Método para nsertar los datos adicional para la poliza del convenio que lo requiera
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
	public static int ingresarInfoAdicionalPolizaBasica(Connection con, int codigoConvenio, int idCuenta, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, String ingresarInfoAdicionalPolizaStr) 
	{
		int resp=0;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(ingresarInfoAdicionalPolizaStr));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAutorizacion)));
			ps.setString(2, numeroAutorizacion);
			ps.setInt(3, valorMonto);
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, usuario);
			ps.setInt(7, idCuenta);
			ps.setInt(8, codigoConvenio);
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error actualizando de los datos de la autorizacion de la Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			resp=0;			
		}	finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;
	}
	
	/**
	 * Metodo para actualizar la informacion basica de la poliza
	 * @param con
	 * @param codigoConvenio
	 * @param idCuenta
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @param ingresarInfoAdicionalPolizaStr
	 * @return
	 */
	public static int actualizarInformacionPolizaBasica(Connection con, int codigoConvenio, int idCuenta, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, String numeroAutoAux, int montoAux) 
	{
		int resp=0;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosPolizaStr));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAutorizacion)));
			ps.setString(2, numeroAutorizacion);
			ps.setInt(3, valorMonto);
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, usuario);
			ps.setInt(7, idCuenta);
			ps.setInt(8, codigoConvenio);
			ps.setString(9, numeroAutoAux);
			ps.setInt(10, montoAux);
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			
			logger.warn(e+" Error actualizando de los datos de la autorizacion de la Póliza: SqlBaseIngresoGeneralDao "+e.toString());
			resp=0;			
		}	finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;
	}
	
	/**
	 * Elmimanr un monto autrorizado
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarDatosInformacionPoliza(Connection con, int codigo)
	{
		int resp=0;
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarDatosInformacionPolizaStr));
			ps.setInt(1, codigo);
			resp=ps.executeUpdate();
			return resp;
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la eliminacion de un monto  [SqlBaseFormatoImpresionPresupuestoDao]: "+e.toString() );
				resp=0;
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resp;
	}
	
	/**
	 * retorna el codigo de persona
	 * @param con
	 * @param idIngreso
	 */
	public static int cargarPacienteDadoIngreso(Connection con, String idIngreso)
	{
		int codigoPersona=ConstantesBD.codigoNuncaValido;
		String consulta= "select codigo_paciente as codi from ingresos where id=?";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, idIngreso);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codigoPersona=rs.getInt("codi");
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en lcargarPacienteDadoIngreso: "+e.toString() );
			codigoPersona=ConstantesBD.codigoNuncaValido;
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
		return codigoPersona;
	}
	
	/**
	 * metodo para cargar los ingresos y cuentas corresponduientes dados los estados,
	 * key={idingreso, nombreestadoingreso, codigoestadocuenta, nombreestadocuenta, codigoviaingreso, nombreviaingreso}
	 * @param con
	 * @param estadosIngresoVector
	 * @param estadosCuentaVector
	 * @return
	 */
	public static HashMap obtenerIngresosCuenta(Connection con, Vector estadosIngresoVector, Vector estadosCuentaVector)
	{
		HashMap mapa= new HashMap();
		String consulta="SELECT c.id_ingreso AS idingreso, getintegridaddominio(i.estado) as nombreestadoingreso, c.estado_cuenta as codigoestadocuenta, getnombreestadocuenta(c.estado_cuenta) as nombreestadocuenta, c.via_ingreso as codigoviaingreso, getnombreviaingreso(c.via_ingreso) as nombreviaingreso FROM ingresos i INNER JOIN cuentas c ON (c.id_ingreso=i.id) inner join asocios_cuenta ac on(ac.cuenta_final=c.id) where 1=1 ";
		consulta+=" AND i.estado in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(estadosIngresoVector, true)+")";
		consulta+=" AND c.estado_cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(estadosCuentaVector, false)+")";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=new ResultSetDecorator(ps.executeQuery());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en lcargarPacienteDadoIngreso: "+e.toString() );
			mapa=new HashMap();
			mapa.put("numRegistros", "0");
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
			
		}
		return mapa;
	}
	
	/**
	 * Método que carga un ingreso incompleto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap<String, Object> cargarIngresoIncompleto(Connection con,String codigoPaciente)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		Statement st = null;
		
		try
		{
			String consulta = "SELECT "+ 
				"i.id As id_ingreso, " +
				"i.codigo_paciente AS codigo_paciente, " +
				"i.institucion AS institucion, "+
				"c.id As id_cuenta, "+
				"to_char(i.fecha_ingreso,'"+ConstantesBD.formatoFechaAp+"') AS fecha_ingreso, "+
				"i.hora_ingreso AS hora_ingreso, "+
				"getnombreviaingreso(c.via_ingreso) AS nombre_via_ingreso," +
				"c.via_ingreso AS codigo_via_ingreso," +
				"c.origen_admision AS codigo_origen_admision," +
				"CASE WHEN c.codigo_responsable_paciente IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE rp.tipo_identificacion || '"+ConstantesBD.separadorSplit+"' || rp.numero_identificacion  END AS datos_responsable_paciente," +
				"c.hospital_dia "+ 
				"FROM manejopaciente.ingresos i "+ 
				"INNER JOIN manejopaciente.cuentas c ON(c.id_ingreso=i.id) " +
				"LEFT OUTER JOIN manejopaciente.responsables_pacientes rp ON(rp.codigo=c.codigo_responsable_paciente) "+ 
				"WHERE "+ 
				"i.codigo_paciente = "+codigoPaciente+" AND "+ 
				"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoIncompletoGarantias+"'";
			
			st = con.createStatement();
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), false, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarIngresoIncompleto: "+e);
		}finally{
			try{
				if(st!=null){
					st.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return resultados;
	}
	
	/**
	 * Método que realiza la anulacion de un ingreso que estaba incompleto por garantías
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean anulacionIngresoIncompleto(Connection con,HashMap campos)
	{
		boolean exito = false;
		ResultSetDecorator rs = null;
		Statement st = null;
		try
		{
			
			
			//********************SE CONSULTAN LAS SUB-CUENTA DEL INGRESO****************************
			String consulta = "SELECT " +
				"sc.sub_cuenta AS codigo," +
				"c.info_adic_ingreso_convenios AS es_poliza " +
				"FROM sub_cuentas sc " +
				"INNER JOIN convenios c ON(c.codigo=sc.convenio) " +
				"WHERE sc.ingreso = "+campos.get("idIngreso");
			st = con.createStatement();
			HashMap subCuentas = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,true);
			//*****************************************************************************************
			
			//******************ANULACION DE URGENCIAS*********************************************
			if(campos.get("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
			{
				String codigoAdmision = "", anioAdmision = "";
				
				//1) Se consulta la admision de urgencias
				consulta = "SELECT codigo, anio FROM admisiones_urgencias WHERE cuenta = "+campos.get("idCuenta");
				st = con.createStatement();
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				if(rs.next())
				{
					codigoAdmision = rs.getString("codigo");
					anioAdmision = rs.getString("anio");
				}
				
				//2) Se actualiza el triage
				consulta = "UPDATE triage SET numero_admision = null, anio_admision = null WHERE numero_admision = "+codigoAdmision+" and anio_admision = "+anioAdmision;
				st = con.createStatement();
				st.executeUpdate(consulta);
				
				//3) Se elimina la admision de urgencias
				consulta = "DELETE FROM admisiones_urgencias WHERE codigo = "+codigoAdmision+" AND anio = "+anioAdmision;
				st = con.createStatement();
				st.executeUpdate(consulta);
				
				//4) Se elimina la tabla tratantes cuenta
				consulta = "DELETE FROM tratantes_cuenta WHERE cuenta = "+campos.get("idCuenta");
				st = con.createStatement();
				st.executeUpdate(consulta);
				
			}
			//**************************************************************************************
			
			//******************ANULACION DE HOPSITALIZACION*********************************************
			if(campos.get("codigoViaIngreso").toString().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
			{
				String codigoCama = "", codigoTraslado = "";
				
				//1) Se elimina la admision de hospitalizacion
				consulta = "DELETE FROM admisiones_hospi WHERE cuenta = "+campos.get("idCuenta");
				st = con.createStatement();
				st.executeUpdate(consulta);
				
				//Solo aplica cuando no es hospital dia
				if(!UtilidadTexto.getBoolean(campos.get("hospitalDia").toString()))
				{
				
					//2) Se consultan los datos del traslado
					consulta = "SELECT codigo,codigo_nueva_cama FROM traslado_cama WHERe cuenta = "+campos.get("idCuenta");
					st = con.createStatement();
					rs =  new ResultSetDecorator(st.executeQuery(consulta));
					if(rs.next())
					{
						codigoTraslado = rs.getString("codigo");
						codigoCama = rs.getString("codigo_nueva_cama");
					}
					
					//3) Se libera la cama
					consulta = "UPDATE camas1 SET estado = "+ConstantesBD.codigoEstadoCamaDisponible+" WHERE codigo = "+codigoCama;
					st = con.createStatement();
					st.executeUpdate(consulta);
					
					//4) Se elimina el traslado de la cama
					consulta = "DELETE FROM traslado_cama WHERE codigo = "+codigoTraslado;
					st = con.createStatement();
					st.executeUpdate(consulta);
					
					
					
				}
				else
				{
					//2) Se elimina de la tabla reingreso_salida_hospi_dia
					consulta = "DELETE FROM reingreso_salida_hospi_dia WHERE cuenta = "+campos.get("idCuenta");
					st = con.createStatement();
					st.executeUpdate(consulta);
				}
				
				//5) Se elimina la tabla tratantes cuenta
				consulta = "DELETE FROM tratantes_cuenta WHERE cuenta = "+campos.get("idCuenta");
				st = con.createStatement();
				st.executeUpdate(consulta);
				
				
				
				
			}
			//********************************************************************************************
			
			
			//Si el origen de la admision era remitido se cambia la referencia externa a tramitado
			if(campos.get("codigoOrigenAdmision").equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido+""))
			{
				consulta = "UPDATE referencia SET " +
					"estado = '"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"', " +
					"ingreso = null," +
					"fecha_modifica = CURRENT_DATE," +
					"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
					"usuario_modifica = '"+campos.get("loginUsuario")+"' "+ 
					"WHERE ingreso = "+campos.get("idIngreso");
				st = con.createStatement();
				st.executeUpdate(consulta);
			}
			//*****************************************************************************************
			
			//**************SE ANULA LA INFORMACION DE CADA CONVENIO********************************
			for(int i=0;i<Integer.parseInt(subCuentas.get("numRegistros").toString());i++)
			{
				String codigo = subCuentas.get("codigo_"+i).toString();
				
				if(UtilidadTexto.getBoolean(subCuentas.get("esPoliza_"+i).toString()))
				{
					//1) Se elimina la informacion de la poliza
					consulta = "DELETE FROM informacion_poliza WHERE sub_cuenta = "+Utilidades.convertirALong(codigo);
					st = con.createStatement();
					st.executeUpdate(consulta);
					
					//2) Se elimina informacion del titular de la poliza
					consulta = "DELETE FROM titular_poliza WHERE sub_cuenta =  "+Utilidades.convertirALong(codigo);
					st = con.createStatement();
					st.executeUpdate(consulta);
					
				}
				
				//3) Se eliminan los requisitos del paciente
				consulta = "DELETE FROM requisitos_pac_subcuenta WHERE subcuenta = "+Utilidades.convertirALong(codigo);
				st = con.createStatement();
				st.executeUpdate(consulta);
				
				//4) Se eliminan los cargos de la subcuenta
				consulta = "DELETE FROM det_cargos WHERE sub_cuenta = "+Utilidades.convertirALong(codigo);
				st = con.createStatement();
				st.executeUpdate(consulta);
				
				//5) Se eliminan las solicitudes x sub_cuenta
				consulta = "DELETE FROM solicitudes_subcuenta WHERE sub_cuenta = "+Utilidades.convertirALong(codigo);
				st = con.createStatement();
				st.executeUpdate(consulta);
				
			}
			//**************************************************************************************
			
			//************SE ANULA INFORMACION GENERAL***********************************************
			//1) Se eliminan las solicitudes de la cuenta
			consulta = "DELETE FROM solicitudes WHERE cuenta = "+campos.get("idCuenta");
			st = con.createStatement();
			st.executeUpdate(consulta);
			
			//2) Se elimina la cuenta
			consulta = "DELETE FROM cuentas WHERE id = "+campos.get("idCuenta");
			st = con.createStatement();
			st.executeUpdate(consulta);
			
			//**********SE ELIMINAN TODOS LOS CONVENIOS CON SU INFORMACION ADICIONIAL********************
			//1) Se eliminan las verificaciones de derechos
			consulta = "DELETE FROM verificaciones_derechos WHERE ingreso = "+campos.get("idIngreso");
			st = con.createStatement();
			st.executeUpdate(consulta);
			
			//2) Se eliminan todos los convenios
			consulta = "DELETE FROM sub_cuentas WHERE ingreso = "+campos.get("idIngreso");
			st = con.createStatement();
			st.executeUpdate(consulta);
			//********************************************************************************************
			
			//*************SE ACTUALIZA EL INGRESO A ANULADO******************************************
			consulta = "UPDATE ingresos SET " +
				"estado = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"', " +
				"fecha_modifica=CURRENT_DATE, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"usuario_modifica = '"+campos.get("loginUsuario")+"' " +
				"WHERE id = "+campos.get("idIngreso");
			st = con.createStatement();
			if(st.executeUpdate(consulta)>0)
				exito = true;
			//*****************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en anulacionIngresoIncompleto: "+e);
			exito = false;
		}finally{
			try{
				if(st!=null){
					st.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
		return exito;
	}
	
	/**
	 * Método que consulta el consecutivo del ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static String getConsecutivoIngreso(Connection con,String idIngreso)
	{
		String resultado = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT consecutivo, anio_consecutivo FROM ingresos WHERE id = "+idIngreso;
			
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				String anio = rs.getString("anio_consecutivo");
				resultado = rs.getString("consecutivo") + (anio.equals("")?"":"-"+anio);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getConsecutivoIngreso: "+e);
		}finally{
			try{
				if(st!=null){
					st.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean existeIngresoAbierto(Connection con, String codigoPaciente, String idIngresoRestriccion)
	{
		String consulta="SELECT count(1) as contador from ingresos where codigo_paciente="+codigoPaciente+" and estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";
		
		if(!UtilidadTexto.isEmpty(idIngresoRestriccion))
		{
			consulta+=" and id<> "+idIngresoRestriccion;
		}
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			 st = con.createStatement();
			 rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				return rs.getInt("contador")>0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en existeIngresoAbierto:");
			e.printStackTrace();
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso  "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	/**
	 * Metodo para obtener los ingresos para factura odontologica dado un paciente,
	 * en este caso la cuenta puede estar activa y los ingresos en estado abierto o cerrado
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoIngresosFactura> obtenerIngresosFacturaOdontologica(int codigoPaciente)
	{
		ArrayList<DtoIngresosFactura> lista= new ArrayList<DtoIngresosFactura>();
		String consultaStr="SELECT " +
								"i.id as codigoingreso, " +
								"i.consecutivo as consecutivoingreso, " +
								"i.estado as acronimoestadoingreso, " +
								"getintegridaddominio(i.estado) as estadoingreso, " +
								"to_char(i.fecha_ingreso, 'DD/MM/YYYY') as fechaingreso, " +
								"to_char(i.fecha_egreso, 'DD/MM/YYYY') as fechacierre, " +
								"c.id as cuenta, " +
								"c.estado_cuenta as codigoestadocuenta, " +
								"ec.nombre as nombreestadocuenta, " +
								"c.via_ingreso as codigoviaingreso, " +
								"vi.nombre as nombreviaingreso " +
							"FROM " +
								"cuentas c " +
								"INNER JOIN ingresos i on(i.id=c.id_ingreso) " +
								"INNER JOIN estados_cuenta ec ON(ec.codigo=c.estado_cuenta) " +
								"INNER JOIN manejopaciente.vias_ingreso vi ON(vi.codigo=c.via_ingreso) " +
							"where " +
								"i.codigo_paciente=? " +
								"and i.tipo_ingreso='"+ConstantesIntegridadDominio.acronimoTipoCoberturaOdontologico+ "' "+ 
								"and i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
								"and c.estado_cuenta= "+ConstantesBD.codigoEstadoCuentaActiva;
		
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			logger.info(consultaStr+" ->"+codigoPaciente);
			ps.setInt(1, codigoPaciente);
			
			rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoIngresosFactura dto= new DtoIngresosFactura(	rs.getBigDecimal("codigoingreso"), 
																rs.getString("consecutivoingreso"), 
																new InfoDatosAcronimo(rs.getString("acronimoestadoingreso"), rs.getString("estadoingreso")), 
																rs.getString("fechaingreso"), 
																rs.getString("fechacierre"), 
																rs.getBigDecimal("cuenta"), 
																new InfoDatosInt(rs.getInt("codigoestadocuenta"), rs.getString("nombreestadocuenta")), 
																new InfoDatosInt(rs.getInt("codigoviaingreso"), rs.getString("nombreviaingreso")));
				
				lista.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerIngresosFacturaOdontologica:");
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
		}
		return lista;
	}
}