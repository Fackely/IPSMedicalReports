package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

/**
 * Clase para el acceso a la fuente de datos de un procedimiento
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */
public interface ProcedimientoDao
{
	/**
	 * Inserta un nuevo procedimiento con los datos dados.
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaEjecucion
	 * @param resultados
	 * @param observaciones
	 * @param tipoRecargo
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertar(	Connection con,
															int numeroSolicitud,
															String fechaEjecucion,
															String resultados,
															String observaciones,
															int tipoRecargo,
															String comentarioHistoriaClinica);
														
	/**
	 *  Inserta un nuevo procedimiento dentro de una transacción con los
	 * datos dados.
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaEjecucion
	 * @param resultados
	 * @param observaciones
	 * @param tipoRecargo
	 * @param estado, estado dentro de la transacción
	 * @return ResultadoBoolean
	 */														
	public ResultadoBoolean insertarTransaccional(	Connection con,
															int numeroSolicitud,
															String fechaEjecucion,
															String resultados,
															String observaciones,
															int tipoRecargo,
															String comentarioHistoriaClinica,
															String estado );
															
	/**
	 * Modifica los datos dados del procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param resultados
	 * @param observaciones
	 * @return ResultadoBoolean
	 */															
	public ResultadoBoolean modificar(	Connection con,
																int numeroSolicitud,
																String resultados,
																String observaciones,
																String comentarioHistoriaClinica
																);
											
	/**
	 * Modifica dentro de una transacción los datos dados del procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param resultados
	 * @param observaciones
	 * @param estado, estado dentro de la transacción
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con,
																int numeroSolicitud,
																String resultados,
																String observaciones,
																String comentarioHistoriaClinica,
																String estado );
	
	/**
	 * Carga un procedimiento dado el número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoCollectionDB
	 */
	public ResultadoCollectionDB cargar(Connection con, int numeroSolicitud );							
												
	/**
	 * Dice si existe o no una respuesta para el procedimiento con le numero de
	 * solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean existeProcedimiento(Connection con, int numeroSolicitud);
																				
	/**
	 * Inserta una respuesta para centros de costo externos cuando se selecciona otros procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param respuestaOtros
	 * @return
	 */
	public ResultadoBoolean insertarOtros(Connection con, int numeroSolicitud, String respuestaOtros);
	
	/**
	 * Retorna la respuesta para centros de costo externos cuando se selecciona otros procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public ResultadoCollectionDB cargarOtros(Connection con, int numeroSolicitud);	
	
	
	public String cargarExtraProcedimiento(Connection con, int numeroSolicitud);
	
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
	public ResultadoBoolean insertarDiagnostico (Connection con,int numeroSolicitud,String acronimo,int codigoCie,boolean principal,boolean complicacion,int numero,String estado);
	
	/**
	 * Método implementado para cargar los diagnósticos de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarDiagnosticosXSolicitud (Connection con,int numeroSolicitud);
	
	/**
	 * Método implementado para eliminar los diagnósticos de una solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 */
	public ResultadoBoolean eliminarDiagnosticos (Connection con, int numeroSolicitud, String estado);
	
	/**
	 * Método para obtener los codigos de las respuestas de la solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCirugia
	 * @return
	 */
	public ArrayList<String> obtenerCodigosRespuestas(Connection con,String numeroSolicitud,String codigoCirugia);
}
