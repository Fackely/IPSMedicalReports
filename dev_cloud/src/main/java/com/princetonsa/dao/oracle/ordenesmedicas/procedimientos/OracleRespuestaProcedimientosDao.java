package com.princetonsa.dao.oracle.ordenesmedicas.procedimientos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.ordenesmedicas.procedimientos.RespuestaProcedimientosDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.procedimientos.SqlBaseRespuestaProcedimientosDao;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

/**
 * @author Jorge Armando Osorio Velasquez
 * @author Wilson Rios
 *
 */
public class OracleRespuestaProcedimientosDao implements RespuestaProcedimientosDao
{

	/**
	 * 
	 */
	public HashMap listadoSolicitudesProcedimientosResponder(Connection con, HashMap vo)
	{
		return SqlBaseRespuestaProcedimientosDao.listadoSolicitudesProcedimientosResponder(con,vo);
	}

	
	/**
	 * 
	 */
	public String insertarRespuestaProcedimiento	(	Connection con,
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
															)
	{
		return SqlBaseRespuestaProcedimientosDao.insertarRespuestaProcedimiento(con, numeroSolicitud, fechaEjecucion, resultados, observaciones, tipoRecargo, comentarioHistoriaClinica, horaEjecucion,codigoMedicoResponde,loginUsuarioRegistraRes,finalidad, observacionCapitacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean finaizarRespuestaSolProcedimiento (	Connection con,
																String numeroSolicitud,
																String acronimofinalizar
															)
	{
		return SqlBaseRespuestaProcedimientosDao.finaizarRespuestaSolProcedimiento(con, numeroSolicitud, acronimofinalizar);
	}
	
	/**
	 * 
	 * @param con
	 * @param 
	 * @return
	 */
	public boolean eliminarDiagnosticos (Connection con, String codigoRespuesta)
	{
		return SqlBaseRespuestaProcedimientosDao.eliminarDiagnosticos(con, codigoRespuesta);
	}
	
	/**
	 * 
	 * @param con
	 * @param 
	 * @param acronimo
	 * @param codigoCie
	 * @param principal
	 * @param complicacion
	 * @param numero
	 * @param estado
	 * @return
	 */
	public boolean insertarDiagnostico (Connection con,String codigoRespuesta,String acronimo,int codigoCie,boolean principal,boolean complicacion,int numero,String estado)
	{
		return SqlBaseRespuestaProcedimientosDao.insertarDiagnostico(con, codigoRespuesta, acronimo, codigoCie, principal, complicacion, numero, estado);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap datosHistoriaClinica (Connection con, String numeroSolicitud)
	{
		return SqlBaseRespuestaProcedimientosDao.datosHistoriaClinica(con, numeroSolicitud);
	}
	
	/** 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String cargarResultadosAnteriores (Connection con, HashMap parametros)
	{
		return SqlBaseRespuestaProcedimientosDao.cargarResultadosAnteriores(con,parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoRespuesta
	 * @return
	 */
	public ResultSetDecorator cargarRespuestaBasica (Connection con, String codigoRespuesta)
	{
		return SqlBaseRespuestaProcedimientosDao.cargarRespuestaBasica(con, codigoRespuesta);
	}
	
	/**
	 * Método implementado para cargar los diagnósticos de un respuesta
	 * @param con
	 * @return
	 */
	public HashMap cargarDiagnosticos (Connection con,String codigoRespuesta)
	{
		return SqlBaseRespuestaProcedimientosDao.cargarDiagnosticos(con, codigoRespuesta);
	}
	

	/**
	 * 
	 */
	public String validacionCita(Connection con, int numeroSolicitudInt) 
	{
		return SqlBaseRespuestaProcedimientosDao.validacionCita(con, numeroSolicitudInt);
	}
	

	/**
	 * 
	 */
	public boolean servicioRequiereInterpretacion(Connection con, int numeroSolicitud) 
	{
		return SqlBaseRespuestaProcedimientosDao.servicioRequiereInterpretacion(con, numeroSolicitud);
	}
	
	/**
	 * Consulta la informacion de los articulos incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public HashMap cargarArticulosIncluidosSolicitud(Connection con, HashMap parametros)
	{
		return 	SqlBaseRespuestaProcedimientosDao.cargarArticulosIncluidosSolicitud(con,parametros);	
	}
	
	/**
	 * Consulta la informacion de los articulos incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public HashMap cargarServiciosIncluidosSolicitud(Connection con, HashMap parametros)
	{
		return 	SqlBaseRespuestaProcedimientosDao.cargarServiciosIncluidosSolicitud(con,parametros);	
	}
	
	/**
	 * Actualiza el campo de Servicio Respuesta Procedimiento
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarCodigoCxServicioRespProc (Connection con, HashMap parametros)
	{
		return 	SqlBaseRespuestaProcedimientosDao.actualizarCodigoCxServicioRespProc(con, parametros);
	}
	
	/**
	 * Método para eliminar las respuestas de procedimientos de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean eliminarRespuestaProcedimientos(Connection con,HashMap campos)
	{
		return SqlBaseRespuestaProcedimientosDao.eliminarRespuestaProcedimientos(con, campos);
	}
	
	/**
	 * Cargar Dto Procedimiento
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public DtoProcedimiento cargarDtoProcedimiento(Connection con,HashMap parametros)
	{
		return SqlBaseRespuestaProcedimientosDao.cargarDtoProcedimiento(con, parametros);
	}
	
	/**
	 * Guarda Muerte del Paciente desde Respuesta de Procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean guardarMuertePacienteRespProc(Connection con, HashMap parametros)
	{
		return SqlBaseRespuestaProcedimientosDao.guardarMuertePacienteRespProc(con, parametros);
	}
	
	/**
	 * Actualiza el numero de respuestas anteriores
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public  boolean actualizarNoRespuestasAnteriores(Connection con, HashMap parametros)
	{
		return SqlBaseRespuestaProcedimientosDao.actualizarNoRespuestasAnteriores(con, parametros);
	}
	
	/**
	 * Guarda Otros Comentarios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean guardarOtrosComentarios(Connection con, HashMap parametros)
	{
		return SqlBaseRespuestaProcedimientosDao.guardarOtrosComentarios(con, parametros);
	}
	
	/**
	 * consulta el numero de respuestas anteriores
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int getNumeroRespuestasAnteriores(Connection con, HashMap parametros)
	{
		return SqlBaseRespuestaProcedimientosDao.getNumeroRespuestasAnteriores(con, parametros);
	}
	
	@Override
	public boolean actualizarObservacionesRespuesta(Connection con,String codigoResputa, String observacionesRes) 
	{
		return SqlBaseRespuestaProcedimientosDao.actualizarObservacionesRespuesta(con, codigoResputa,observacionesRes);
	}
}