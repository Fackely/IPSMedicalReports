/*
 * @(#)SqlBaseProcedimientoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Procedimiento
 *
 *	@version 1.0, Apr 13, 2004
 */
public class SqlBaseProcedimientoDao 
{

	/**
	 * Sentencia SQL para cargar los datos del procedimiento dado el numero de la solicitud
	 */
	private static final String cargarProcedimientoStr="SELECT" +
															" rsp.codigo AS codigoressolproc,  " +
															" rsp.numero_solicitud AS numeroSolicitud," +
															" rsp.fecha_ejecucion AS fechaEjecucion," +
															" rsp.fecha_grabacion AS fechaGrabacionRespuesta," +
															" rsp.hora_grabacion AS horaGrabacionRespuesta," +
															" coalesce(rsp.resultados,'') AS resultados," +
															" rsp.tipo_recargo AS codigoTipoRecargo," +
															" tr.nombre AS nombreTipoRecargo," +
															" rsp.observaciones AS observaciones," +
															" coalesce(rsp.comentario_historia_clinica,'') AS comentarioHistoriaClinica, " +
															" s.espos as espos "+
															" FROM" +
															" res_sol_proc rsp " +
															" INNER JOIN tipos_recargo tr ON(rsp.tipo_recargo = tr.codigo) "+
															" INNER JOIN sol_procedimientos sp ON(rsp.numero_solicitud=sp.numero_solicitud) "+
															" INNER JOIN servicios s ON(sp.codigo_servicio_solicitado=s.codigo) "+
															" WHERE rsp.numero_solicitud=? ";

	/**
	 * Sentencia para cargar la respuesta para centros de costo
	 * externos cuando se selecciona otros procedimientos
	 */
	private static final String cargarRespuestaOtrosStr ="SELECT resultados_otros AS resultadosOtros FROM sol_procedimientos WHERE numero_solicitud = ?";
	
	/**
	 * Sentencia SQL para insertar un procedimiento nuevo
	 */
	private static final String insertarProcedimientoStr="INSERT INTO res_sol_proc (" +
																						   " numero_solicitud," +
																						   " fecha_grabacion," +
																						   " hora_grabacion," +
																						   " fecha_ejecucion," +
																						   " resultados," +
																						   " tipo_recargo," +
																						   " observaciones," +
																						   " comentario_historia_clinica)" +
																						   " VALUES (?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?)";

	/**
	 * Sentencia SQL para modificar los procedimientos
	 */
	 private static final String modificarProcedimientosStr="UPDATE" +
																					" res_sol_proc" +
																					" SET" +
																					" resultados=?," +
																					" observaciones = ?," +
																					" comentario_historia_clinica=?" +
																					" WHERE numero_solicitud=?";

	/**
	 * Sentencia SQL para verificar la existencia de un procedimiento dado el numero de la solicitud 
	 */
	private static final String existeProcedimientoStr="SELECT numero_solicitud AS numeroSolicitud FROM res_sol_proc WHERE numero_solicitud=?";

	/**
	 * Inserta la respuesta para centros de costo externos cuando se selecciona otros
	 * procedimientos
	 */
	private static String insertarRespuestaOtrosStr ="UPDATE sol_procedimientos SET resultados_otros = ? WHERE numero_solicitud = ?";	
	
	
	private static final String cargarExtraPorcedimientoStr="SELECT coalesce(rsp.resultados,'') as resultado, sp.comentario as comentario FROM sol_procedimientos sp INNER JOIN res_sol_proc rsp ON(sp.numero_solicitud=rsp.numero_solicitud) WHERE sp.numero_solicitud=?";
	
	/**
	 * Cadena que inserta un diagnóstico para la respuesta del procedimiento
	 */
	private static final String insertarDiagnosticoStr = "INSERT " +
		"INTO diag_procedimientos " +
		"(numero_solicitud,acronimo,tipo_cie,principal,complicacion,numero) " +
		"VALUES (?,?,?,?,?,?) ";
	
	/**
	 * Cadena que consulta los diagnósticos de un procedimiento
	 */
	private static final String cargarDiagnosticosSolicitudStr = "SELECT dp.acronimo AS acronimo, getnombrediagnostico(dp.acronimo,dp.tipo_cie) As nombre, dp.tipo_cie AS tipo_cie, dp.principal AS principal, dp.complicacion AS complicacion, dp.numero AS numero  FROM diag_procedimientos dp inner join res_sol_proc rsp on(rsp.codigo=dp.codigo_respuesta) where numero_solicitud = ? ORDER BY numero";
	
	/**
	 * Cadena que elimina los diagnósticos de un procedimiento
	 */
	private static final String eliminarDiagnosticosStr = " DELETE FROM diag_procedimientos WHERE numero_solicitud = ? ";
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseProcedimientoDao.class);
	
	/**
	 * Implementación del método utilizado para cargar un procedimiento existente
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#cargar(java.sql.Connection, int)
	 */
	public static ResultadoCollectionDB cargar(Connection con, int numeroSolicitud)
	{
		Collection collection=null;  
		try{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(cargarProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1,numeroSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(cargarStatement.executeQuery());
			collection=UtilidadBD.resultSet2Collection(resultado);
		}
		catch(SQLException e)
		{
			logger.error("Error tratando de cargar los datos de la base de datos: "+e);
			return new ResultadoCollectionDB(false,"Error tratando de cargar los datos de la base de datos: "+e);
		}
		if(collection.isEmpty())
		{
			logger.error("Error cargando procedimiento: No existe ningún procedimiento referente al número de solicitud dado");
			return new ResultadoCollectionDB(false,"Error cargando procedimiento: No existe ningún procedimiento referente al número de solicitud dado");
		}
		else
			return new ResultadoCollectionDB(true,"",collection);
	}
	
	/**
	 * Implementación del método que retorna la respuesta para centros 
	 * de costo externos cuando se selecciona otros procedimientos en 
	 * una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#cargarOtros(Connection , int )
	 */
	public static ResultadoCollectionDB cargarOtros(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator insertarOtros =  new PreparedStatementDecorator(con.prepareStatement(cargarRespuestaOtrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarOtros.setInt(1,numeroSolicitud);
			
			ResultSetDecorator resultadoCargar= new ResultSetDecorator(insertarOtros.executeQuery());
			Collection colResultado = UtilidadBD.resultSet2Collection(resultadoCargar);
			if( !colResultado.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", colResultado);
			}
			else
			{
				return new ResultadoCollectionDB(false, "No se cargó la respuesta para centros de costo externo PostgresqProcedimientoDAO");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Error tratando de cargar la respuesta para centros de costo externo PostgresqProcedimientoDAO: "+e);
			return new ResultadoCollectionDB(false,"Error tratando de cargar la respuesta para centros de costo externo PostgresqProcedimientoDAO: "+e);
		}
	}

	/**
	 * Método privado para manejo de transacciones en una
	 * BD Genérica
	 * 
	 * @param con Conexión con la BD Genérica
	 * @return
	 */
	private static ResultadoBoolean empezarTransaccion(Connection con)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean resp=false;
		try
		{
			resp=myFactory.beginTransaction(con);
		}
		catch (SQLException e)
		{
			resp=false;
		}
		return new ResultadoBoolean(resp);
	}

	/**
	 * Método privado para manejo de finalización de transacción
	 * en una BD Genérica
	 * 
	 * @param con
	 * @return
	 */	
	private static ResultadoBoolean terminarTransaccion(Connection con)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			myFactory.endTransaction(con);
			return new ResultadoBoolean(true);
		}
		catch(SQLException e)
		{
			logger.error("Problemas terminando la transacción en valoración interconsulta \n"+e);			
			return new ResultadoBoolean(false, "Problemas terminando la transacción en valoración interconsulta \n"+e);
		}
	}
	
	/**
	 * Método privado para abortar transacciones en una
	 * BD Genérica
	 * 
	 * @param con Conexión con la BD Genérica
	 * @return
	 */
	private static ResultadoBoolean abortarTransaccion(Connection con)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseProcedimientoDao)"+e1.toString());
		}
		
		try
		{
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(true);
		}
		catch(SQLException e)
		{
			logger.error("Problemas haciendo rollback en valoración interconsulta \n "+e);
			return new ResultadoBoolean(false, "Problemas haciendo rollback en valoración interconsulta \n "+e);
		}
	}

	/**
	 * Implementación del método utilizado para insertar un nuevo 
	 * procedimiento en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#insertar(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public static ResultadoBoolean insertar(	Connection con,
																		int numeroSolicitud,
																		String fechaEjecucion,
																		String resultados,
																		String observaciones,
																		int tipoRecargo,
																		String comentarioHistoriaClinica)
	{
		try
		{
			PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarStatement.setInt(1, numeroSolicitud);
			insertarStatement.setString(2,fechaEjecucion);
			insertarStatement.setString(3,resultados);
			insertarStatement.setInt(4,tipoRecargo);
			insertarStatement.setString(5,observaciones);
			insertarStatement.setString(6,comentarioHistoriaClinica);
			insertarStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion de los datos del procedimiento Genérica: "+e);
			return new ResultadoBoolean(false,"Error en la insercion de los datos del procedimiento Genérica: "+e);
		}
		
		return new ResultadoBoolean(true);
	}

	/**
	 * Implementación del método transaccional utilizado para insertar un 
	 * nuevo procedimiento en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#insertarTransaccional(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	public static ResultadoBoolean insertarTransaccional(Connection con,
																int numeroSolicitud,
																String fechaEjecucion,
																String resultados,
																String observaciones,
																int tipoRecargo,
																String comentarioHistoriaClinica,
																String estado)
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error insertarTransaccional Genérica de procedimientos en setCommit(con, false)");
				return resp;
			}	
			resp = empezarTransaccion(con);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error insertarTransaccional Genérica de procedimientos empezando la transacción");
				return resp;
			}
		}
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO se va a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{			
				PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				insertarStatement.setInt(1, numeroSolicitud);
				insertarStatement.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaEjecucion));
				insertarStatement.setString(3,resultados);
				insertarStatement.setInt(4,tipoRecargo);
				insertarStatement.setString(5,observaciones);
				insertarStatement.setString(6,comentarioHistoriaClinica);					
				int insert=insertarStatement.executeUpdate();
				if( insert == 0 )
					return abortarTransaccion(con);
			}
		}
		catch (SQLException e)
		{
			abortarTransaccion(con);
			logger.error("Error en la insercion de los datos del procedimiento Genérica: "+e);
			return new ResultadoBoolean(false,"Error en la insercion de los datos del procedimiento Genérica: "+e);
		}
		
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			resp = setCommit(con, true);			
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genérica en setCommit(con, false) _ "+resp.getDescripcion());
				return resp;
			}		
					
			resp = terminarTransaccion(con);			
			if( resp.isTrue() == false )
			{
					resp.setDescripcion("Error insertarTransaccional Genérica empezando la transacción _ "+resp.getDescripcion());
					return resp;
			}
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Método privado que establece el valor del autocommit
	 * 
	 * @param con
	 * @param estado
	 * @return
	 */
	private static ResultadoBoolean setCommit(Connection con, boolean estado)
	{
		try
		{
			con.setAutoCommit(estado);
		}
		catch(SQLException e)
		{
			logger.error("Problemas poniendo el commit en  "+estado+", Valoración Interconsulta "+e);
			return new ResultadoBoolean(false, "Problemas poniendo el commit en  "+estado+", Valoración Interconsulta "+e);
		}							
		
		return new ResultadoBoolean(true);
	}

	/**
	 * Implementación del método utilizado para modificar un procedimiento
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#modificar(java.sql.Connection, int, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificar(	Connection con,
																int numeroSolicitud,
																String resultados,
																String observaciones,
																String comentarioHistoriaClinica)
	{
		try
		{
			PreparedStatementDecorator modificarStatment= new PreparedStatementDecorator(con.prepareStatement(modificarProcedimientosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarStatment.setString(1,resultados);
			modificarStatment.setString(2,observaciones);
			modificarStatment.setString(3,comentarioHistoriaClinica);
			modificarStatment.setInt(4, numeroSolicitud);
			modificarStatment.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en la modificacion de los datos del procedimiento Genérica: "+e);
			return new ResultadoBoolean(false,"Error en la modificacion de los datos del procedimiento Genérica: "+e);
		}
		
		return new ResultadoBoolean(true);

	}

	/**
	 * Implementación del método transaccional utilizado para modificar 
	 * un procedimiento en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#modificarTransaccional(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
																						int numeroSolicitud,
																						String resultados,
																						String observaciones,
																						String comentarioHistoriaClinica,
																						String estado)
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error modificarTransaccional Genérica de procedimientos en setCommit(con, false)");
				return resp;
			}	
			resp = empezarTransaccion(con);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error modificarTransaccional Genérica de procedimientos empezando la transacción");
				return resp;
			}
		}
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO se va a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{			
				PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(modificarProcedimientosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				modificarStatement.setString(1,resultados);
				modificarStatement.setString(2,observaciones);
				modificarStatement.setString(3,comentarioHistoriaClinica);
				modificarStatement.setInt(4, numeroSolicitud);				
				int update=modificarStatement.executeUpdate();
				if( update == 0 )
					return abortarTransaccion(con);
			}
		}
		catch (SQLException e)
		{
			abortarTransaccion(con);
			logger.error("Error en la insercion de los datos del procedimiento Genérica: "+e);
			return new ResultadoBoolean(false,"Error en la insercion de los datos del procedimiento Genérica: "+e);
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			resp = setCommit(con, true);			
			if( resp.isTrue() == false ){
				resp.setDescripcion("Error insertarTransaccional Genérica en setCommit(con, false) _ "+resp.getDescripcion());
				return resp;
			}		
				
			resp = terminarTransaccion(con);			
			if( resp.isTrue() == false ){
				resp.setDescripcion("Error insertarTransaccional Genérica empezando la transacción _ "+resp.getDescripcion());
				return resp;
			}
		}
				
		return new ResultadoBoolean(true);
	}

	/**
	 * Implementación del método utilizado para verificar la existencia de un 
	 * procedimiento en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#existeProcedimiento(java.sql.Connection, int)
	 */
	public static ResultadoBoolean existeProcedimiento(	Connection con,
																				int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(existeProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1,numeroSolicitud);			
			ResultSetDecorator resultado=new ResultSetDecorator(cargarStatement.executeQuery());
			Collection collection=UtilidadBD.resultSet2Collection(resultado);
			if(collection.isEmpty())
				return new ResultadoBoolean(false);
			else
				return new ResultadoBoolean(true);
		}
		catch(SQLException e)
		{
			logger.error("Error tratando verificar la existencia del procedimiento: "+e);
			return new ResultadoBoolean(false,"Error tratando verificar la existencia del procedimiento: "+e);
		}
	}

	/**
	 * Implementación inserta una respuesta para centros de costo externos 
	 * cuando se selecciona otros procedimientos en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#insertarOtros(Connection , int , String )
	 */
	public static ResultadoBoolean insertarOtros(Connection con, int numeroSolicitud, String respuestaOtros)
	{
		try
		{
			PreparedStatementDecorator insertarOtros =  new PreparedStatementDecorator(con.prepareStatement(insertarRespuestaOtrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarOtros.setString(1,respuestaOtros);
			insertarOtros.setInt(2,numeroSolicitud);
			
			int reslutadoInsercion = insertarOtros.executeUpdate();
			if(reslutadoInsercion > 0 )
			{
				return new ResultadoBoolean(true);
			}
			else
			{
				return new ResultadoBoolean(false, "No se insertó la respuesta para centros de costo externo PostgresqProcedimientoDAO");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Error tratando de ingresar la respuesta para centros de costo externo PostgresqProcedimientoDAO: "+e);
			return new ResultadoBoolean(false,"Error tratando de ingresar la respuesta para centros de costo externo PostgresqProcedimientoDAO: "+e);
		}
	}
	
	/**
	 * Carga el resultado y el somentario de un procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String cargarExtraProcedimiento(Connection con, int numeroSolicitud)
    {
		String extra="";
		String tmp="";
		String tmp2="";
        try
         {               
	         if (con == null || con.isClosed()) 
	         {
	             DaoFactory myFactory =DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	             con = myFactory.getConnection();
	         }
	         PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarExtraPorcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 ps.setInt(1,numeroSolicitud);
			 
			 //Ejecuto la consulta
			  ResultSetDecorator resultset=new ResultSetDecorator(ps.executeQuery());
			  if(resultset.next())
			  {
			  	if(resultset.getString("resultado")==null||resultset.getString("resultado").equals("null")||resultset.getString("resultado").equals(""))
			  	{
			  		tmp="**";
			  	}
			  	else
			  	{
			  		tmp=resultset.getString("resultado");
			  	}
			  	if(resultset.getString("comentario").equals(""))
			  	{
			  		tmp2="**";
			  	}
				else
				{
					tmp2=resultset.getString("comentario");
				}
			  
			  	extra=tmp+ConstantesBD.separadorSplit+tmp2;
			  }
			  else
			  {
			  	extra="**"+ConstantesBD.separadorSplit+"**";
			  }
			  return extra;
         }
       catch(SQLException e)
       {
           logger.error(" Error en la consulta de datos "+e.toString());
          
       }
        return null;
    }
	
	/**
	 * Método que inserta un diagnóstico a la repsuesta del procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param acronimo
	 * @param codigoCie
	 * @param principal
	 * @param complicacion
	 * @param numero
	 * @param estado
	 * @return
	 */
	public static ResultadoBoolean insertarDiagnostico (Connection con,int numeroSolicitud,String acronimo,int codigoCie,boolean principal,boolean complicacion,int numero,String estado)
	{
		// se inicia la transaccion
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error insertarDiagnostico de procedimientos en setCommit(con, false)");
				return resp;
			}	
			resp = empezarTransaccion(con);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error insertarDiagnostico de procedimientos empezando la transacción");
				return resp;
			}
		}
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO se va a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{			
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(insertarDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,numeroSolicitud);
				pst.setString(2,acronimo);
				pst.setInt(3,codigoCie);
				pst.setBoolean(4, principal);				
				pst.setBoolean(5, complicacion);
				pst.setInt(6, numero);
				int update=pst.executeUpdate();
				if( update == 0 )
					return abortarTransaccion(con);
			}
		}
		catch (SQLException e)
		{
			abortarTransaccion(con);
			logger.error("Error en la insercion de diagnóstico del procedimiento : "+e);
			return new ResultadoBoolean(false,"Error en la insercion de diagnóstico del procedimiento : "+e);
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			resp = setCommit(con, true);			
			if( resp.isTrue() == false ){
				resp.setDescripcion("Error insertarTransaccional Genérica en setCommit(con, false) _ "+resp.getDescripcion());
				return resp;
			}		
				
			resp = terminarTransaccion(con);			
			if( resp.isTrue() == false ){
				resp.setDescripcion("Error insertarTransaccional Genérica empezando la transacción _ "+resp.getDescripcion());
				return resp;
			}
		}
				
		return new ResultadoBoolean(true);
		
		
	}
	
	/**
	 * Método implementado para cargar los diagnósticos de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarDiagnosticosXSolicitud (Connection con,int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDiagnosticosXSolicitud de SqlBaseProcedimientoDao: "+e);
			HashMap mapa = new HashMap();
			mapa.put("numRegistros","0");
			return mapa;
		}
	}
	
	/**
	 * Método implementado para eliminar los diagnósticos de una solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public static ResultadoBoolean eliminarDiagnosticos (Connection con, int numeroSolicitud, String estado)
	{
		//se inicia la transaccion
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error eliminarDiagnosticos de procedimientos en setCommit(con, false)");
				return resp;
			}	
			resp = empezarTransaccion(con);			
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error eliminarDiagnosticos de procedimientos empezando la transacción");
				return resp;
			}
		}
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO se va a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{			
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(eliminarDiagnosticosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,numeroSolicitud);
				
				pst.executeUpdate();
				
			}
		}
		catch (SQLException e)
		{
			abortarTransaccion(con);
			logger.error("Error en la insercion de diagnóstico del procedimiento : "+e);
			return new ResultadoBoolean(false,"Error en la insercion de diagnóstico del procedimiento : "+e);
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			resp = setCommit(con, true);			
			if( resp.isTrue() == false ){
				resp.setDescripcion("Error insertarTransaccional Genérica en setCommit(con, false) _ "+resp.getDescripcion());
				return resp;
			}		
				
			resp = terminarTransaccion(con);			
			if( resp.isTrue() == false ){
				resp.setDescripcion("Error insertarTransaccional Genérica empezando la transacción _ "+resp.getDescripcion());
				return resp;
			}
		}
				
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Método para obtener los codigos de las respuestas de la solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCirugia
	 * @return
	 */
	public static ArrayList<String> obtenerCodigosRespuestas(Connection con,String numeroSolicitud,String codigoCirugia)
	{
		ArrayList<String> resultados = new ArrayList<String>();
		try
		{
			String consulta = "SELECT codigo FROM res_sol_proc WHERE  ";
			
			if(!codigoCirugia.equals(""))
				consulta+= " codigo_cx_serv = "+codigoCirugia+" ";
			else
				consulta+= " numero_solicitud = "+numeroSolicitud+" ";
			
			consulta += " order by fecha_ejecucion,hora_ejecucion";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				resultados.add(rs.getString("codigo"));
			}
		}
		catch(SQLException e)
		{
			logger.info("Error en obtenerCodigosRespuestas: "+e);
		}
		return resultados;
	}
	
}
