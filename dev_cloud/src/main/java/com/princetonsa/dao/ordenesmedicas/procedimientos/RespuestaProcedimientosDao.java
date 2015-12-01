package com.princetonsa.dao.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

/**
 * @author Jorge Armando Osorio Velasquez
 * @author Wilson Rios
 *
 */
public interface RespuestaProcedimientosDao
{

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap listadoSolicitudesProcedimientosResponder(Connection con, HashMap vo);

	
	/**
	 * @param finalidad 
	 * 
	 */
	public abstract String insertarRespuestaProcedimiento	(Connection con,
																		String numeroSolicitud,
																		String fechaEjecucion,
																		String resultados,
																		String observaciones,
																		int tipoRecargo,
																		String comentarioHistoriaClinica,
																		String horaEjecucion,
																		int codigoMedicoResponde,
																		String loginUsuarioRegistraRes, 
																		int finalidad,
																		String observacionCapitacion
															);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public abstract boolean finaizarRespuestaSolProcedimiento (	Connection con,
																String numeroSolicitud,
																String acronimofinalizar
															);
	
	/**
	 * 
	 * @param con
	 * @param 
	 * @return
	 */
	public boolean eliminarDiagnosticos (Connection con, String codigoRespuesta);
	
	/**
	 * 
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
	public boolean insertarDiagnostico (Connection con,String codigoRespuesta,String acronimo,int codigoCie,boolean principal,boolean complicacion,int numero,String estado);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap datosHistoriaClinica (Connection con, String numeroSolicitud);
	
	/** 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String cargarResultadosAnteriores (Connection con, HashMap Parametros);
	
	/**
	 * 
	 * @param con
	 * @param codigoRespuesta
	 * @return
	 */
	public ResultSetDecorator cargarRespuestaBasica (Connection con, String codigoRespuesta);
	
	/**
	 * Método implementado para cargar los diagnósticos de un respuesta
	 * @param con
	 * @return
	 */
	public HashMap cargarDiagnosticos (Connection con,String codigoRespuesta);


	/**
	 * 
	 * @param con
	 * @param numeroSolicitudInt
	 * @return
	 */
	public abstract String validacionCita(Connection con, int numeroSolicitudInt);


	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public abstract boolean servicioRequiereInterpretacion(Connection con, int numeroSolicitud);
	
	/**
	 * Consulta la informacion de los articulos incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public HashMap cargarArticulosIncluidosSolicitud(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informacion de los servicios incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public HashMap cargarServiciosIncluidosSolicitud(Connection con, HashMap parametros);
	
	
	/**
	 * Actualiza el campo de Servicio Respuesta Procedimiento
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCodigoCxServicioRespProc (Connection con, HashMap parametros);
	
	/**
	 * Método para eliminar las respuestas de procedimientos de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean eliminarRespuestaProcedimientos(Connection con,HashMap campos);
	
	/**
	 * Cargar Dto Procedimiento
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public DtoProcedimiento cargarDtoProcedimiento(Connection con,HashMap parametros);
	
	
	/**
	 * consulta el numero de respuestas anteriores
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int getNumeroRespuestasAnteriores(Connection con, HashMap parametros);
	
	/**
	 * Guarda Muerte del Paciente desde Respuesta de Procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean guardarMuertePacienteRespProc(Connection con, HashMap parametros);
	
	
	/**
	 * Actualiza el numero de respuestas anteriores
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarNoRespuestasAnteriores(Connection con, HashMap parametros);
	
	
	/**
	 * Guarda Otros Comentarios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean guardarOtrosComentarios(Connection con, HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param codigoResputa
	 * @param observacionesRes
	 * @return
	 */
	public abstract boolean actualizarObservacionesRespuesta(Connection con,String codigoResputa, String observacionesRes);

}