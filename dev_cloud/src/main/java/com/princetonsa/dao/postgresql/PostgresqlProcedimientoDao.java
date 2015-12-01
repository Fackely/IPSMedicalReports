package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.dao.ProcedimientoDao;
import com.princetonsa.dao.sqlbase.SqlBaseProcedimientoDao;

/**
 * Clase para el acceso a la fuente de datos postgresql de un procedimiento
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */

public class PostgresqlProcedimientoDao implements ProcedimientoDao
{
	
	/**
	 * Implementaci�n del m�todo utilizado para insertar un nuevo 
	 * procedimiento en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#insertar(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public ResultadoBoolean insertar(	Connection con,
																int numeroSolicitud,
																String fechaEjecucion,
																String resultados,
																String observaciones,
																int tipoRecargo,
																String comentarioHistoriaClinica)
	{
		return SqlBaseProcedimientoDao.insertar(	con, numeroSolicitud, fechaEjecucion, resultados, observaciones, tipoRecargo, comentarioHistoriaClinica);
	}

	/**
	 * Implementaci�n del m�todo transaccional utilizado para insertar un 
	 * nuevo procedimiento en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#insertarTransaccional(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(Connection con,
																int numeroSolicitud,
																String fechaEjecucion,
																String resultados,
																String observaciones,
																int tipoRecargo,
																String comentarioHistoriaClinica,
																String estado)
	{
		return SqlBaseProcedimientoDao.insertarTransaccional(con, numeroSolicitud, fechaEjecucion, resultados, observaciones, tipoRecargo, comentarioHistoriaClinica, estado);
	}

	/**
	 * Implementaci�n del m�todo utilizado para modificar un procedimiento
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#modificar(java.sql.Connection, int, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificar(	Connection con,
																int numeroSolicitud,
																String resultados,
																String observaciones,
																String comentarioHistoriaClinica)
	{
		return SqlBaseProcedimientoDao.modificar(con, numeroSolicitud, resultados, observaciones, comentarioHistoriaClinica);
	}

	/**
	 * Implementaci�n del m�todo transaccional utilizado para modificar 
	 * un procedimiento en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#modificarTransaccional(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con,
																						int numeroSolicitud,
																						String resultados,
																						String observaciones,
																						String comentarioHistoriaClinica,
																						String estado)
	{
		return SqlBaseProcedimientoDao.modificarTransaccional(	con, numeroSolicitud, resultados, observaciones, comentarioHistoriaClinica, estado);
	}

	/**
	 * Implementaci�n del m�todo utilizado para cargar un procedimiento existente
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#cargar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB cargar(Connection con, int numeroSolicitud)
	{
		return SqlBaseProcedimientoDao.cargar(con, numeroSolicitud);
	}
	
	/**
	 * Implementaci�n del m�todo utilizado para verificar la existencia de un 
	 * procedimiento en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#existeProcedimiento(java.sql.Connection, int)
	 */
	public ResultadoBoolean existeProcedimiento(	Connection con,
																				int numeroSolicitud)
	{
		return SqlBaseProcedimientoDao.existeProcedimiento(con, numeroSolicitud);
	}

	/**
	 * Implementaci�n inserta una respuesta para centros de costo externos 
	 * cuando se selecciona otros procedimientos en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#insertarOtros(Connection , int , String )
	 */
	public ResultadoBoolean insertarOtros(Connection con, int numeroSolicitud, String respuestaOtros)
	{
		return SqlBaseProcedimientoDao.insertarOtros(con, numeroSolicitud, respuestaOtros);
	}
	
	/**
	 * Implementaci�n del m�todo que retorna la respuesta para centros 
	 * de costo externos cuando se selecciona otros procedimientos en 
	 * una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ProcedimientoDao#cargarOtros(Connection , int )
	 */
	public ResultadoCollectionDB cargarOtros(Connection con, int numeroSolicitud)
	{
		return SqlBaseProcedimientoDao.cargarOtros(con, numeroSolicitud);
	}
	
	/**
	 * Carga el resultado y el somentario de un procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String cargarExtraProcedimiento(Connection con, int numeroSolicitud)
	{
		return SqlBaseProcedimientoDao.cargarExtraProcedimiento(con, numeroSolicitud);
	}
	
	/**
	 * M�todo que inserta un diagn�stico a la repsuesta del procedimiento
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
	public ResultadoBoolean insertarDiagnostico (Connection con,int numeroSolicitud,String acronimo,int codigoCie,boolean principal,boolean complicacion,int numero,String estado)
	{
		return SqlBaseProcedimientoDao.insertarDiagnostico(con,numeroSolicitud,acronimo,codigoCie,principal,complicacion,numero,estado);
	}
	
	/**
	 * M�todo implementado para cargar los diagn�sticos de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarDiagnosticosXSolicitud (Connection con,int numeroSolicitud)
	{
		return SqlBaseProcedimientoDao.cargarDiagnosticosXSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * M�todo implementado para eliminar los diagn�sticos de una solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public ResultadoBoolean eliminarDiagnosticos (Connection con, int numeroSolicitud, String estado)
	{
		return SqlBaseProcedimientoDao.eliminarDiagnosticos(con,numeroSolicitud,estado);
	}
	
	/**
	 * M�todo para obtener los codigos de las respuestas de la solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCirugia
	 * @return
	 */
	public ArrayList<String> obtenerCodigosRespuestas(Connection con,String numeroSolicitud,String codigoCirugia)
	{
		return SqlBaseProcedimientoDao.obtenerCodigosRespuestas(con, numeroSolicitud, codigoCirugia);
	}
}
