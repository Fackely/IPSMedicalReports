/*
 * @(#)SqlBaseSolicitudInterconsultaDao.java
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
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoServicios;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Solicitud Interconsulta
 *
 *	@version 1.0, Apr 3, 2004
 */
public class SqlBaseSolicitudInterconsultaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseSolicitudInterconsultaDao.class);
	
	/**consulta de los datos propios de interconsulta*/
	private final static String cargarDatosInterconsulta="SELECT  " +
																				"solinter.numero_solicitud AS numeroSolicitud, " +
																				"solinter.codigo_servicio_solicitado AS codigoServicioSolicitado,"+
																				"solinter.nombre_otros AS nombreOtros," +
																				"solinter.motivo_solicitud AS motivoSolicitud, " +
																				"solinter.resumen_historia_clinica AS resumenHistoriaClinica, " +
																				"solinter.respuesta_otros AS respuestaOtros, " +
																				"opinter.nombre AS nombreManejoInterconsulta, " +
																				"opinter.codigo AS codigoManejoInterconsulta, " +
																				"solinter.comentario AS comentario " +
																				"FROM " +
																				"solicitudes_inter solinter, op_man_intercon opinter " +
																				"WHERE " +
																				"solinter.codigo_manejo_interconsulta=opinter.codigo and solinter.numero_solicitud=?";

	/**consulta del código del servicio solicitado**/
	private final static String consultaCodigoServicioSolicitado= "SELECT descripcion as description FROM referencias_servicio WHERE tipo_tarifario=" + ConstantesBD.codigoTarifarioCups + " and servicio = " +
																							"(select codigo_servicio_solicitado " +
																							"from solicitudes_inter where numero_solicitud = ? )";
	
	/**consulta de acronimo días tramite urgente y normal del grupo del servicio solicitado**/
	private final static String consultaDiasUrgenteNormalGrupoServicio = "select gru.acro_dias_urgente, gru.acro_dias_normal " +
																							"from grupos_servicios gru " +
																							"inner join servicios ser " +
																							"on(gru.codigo=ser.grupo_servicio) " +
																							"where ser.codigo=? ";


	/**para actualizar la solicitud de interconsulta **/
	private final static String modificarStr= " UPDATE " +
															"solicitudes_inter " +
															"SET " +
															"motivo_solicitud=?, " +
															"resumen_historia_clinica=?, " +
															"comentario=? " +
															"WHERE numero_solicitud=? ";

	/**para actualizar el numero de solicitud de una interconsulta**/														
	//private final static String modificarNumeroAutorizacion= "UPDATE " +
	//																					"solicitudes " +
	//																					"SET " +
	//																					"numero_autorizacion=? " +
	//																					"WHERE " +
	//																					"numero_solicitud=? ";
	
	
	/**
	 * Sentencia SQL para la inserción de un documento adjunto
	 */
	static String consultaDocumentoAdjunto="INSERT INTO " +
																			"doc_adj_solicitud " +
																			"(numero_solicitud, nombre_archivo, nombre_original, es_solicitud) " +
																			"VALUES ( ? , ? , ? , ? )";

	/**consulta los docs adjuntos*/
	private final static String consultaCargarDocumentoAdjunto="SELECT" +
																								" numero_solicitud AS numero_solicitud," +
																								" nombre_archivo AS nombre_archivo," +
																								" nombre_original AS nombre_original" +
																								" FROM" +
																								" doc_adj_solicitud" +
																								" WHERE" +
																								" numero_solicitud=? AND" +
																								" es_solicitud = " + ValoresPorDefecto.getValorTrueParaConsultas() + "";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar 
	 * el flag de mostrar interconsulta en una BD Genérica.
	 */
	private static final String actualizarFlagMostrarInterconsultaStr="UPDATE solicitudes_inter set manejo_conjunto_finalizado =? where numero_solicitud=?";
	
	/**
	 * Método que  carga  una solicitud 
	 * en una BD PostgresSQL o Hsqldb de Interconsulta
	 */
	public static ResultSetDecorator cargarSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
			PreparedStatementDecorator cargarSolicitudStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosInterconsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarSolicitudStatement.setInt(1, numeroSolicitud);
			return new ResultSetDecorator(cargarSolicitudStatement.executeQuery());
	}

	/**
	 * Método que carga el motivo de anulación de una solicitud
	 * en una BD PostgresSQL o Hsqldb de Interconsulta
	 */
	public static ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		String consulta="SELECT motivo AS motivo from anulaciones_solicitud where solicitud="+numeroSolicitud;
		logger.info("--->"+consulta);
		PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(psd.executeQuery());
	}

	/**
	 * Método que  carga  el código del servicio solicitado 
	 * en una BD PostgresSQL o Hsqldb de Interconsulta
	 *
	*/
	public static ResultSetDecorator cargarCodigoServicioSolicitado (Connection con, int numeroSolicitud) throws SQLException
	{
			PreparedStatementDecorator cargarCodigoServicioSolicitadoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaCodigoServicioSolicitado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarCodigoServicioSolicitadoStatement.setInt(1, numeroSolicitud);
			return new ResultSetDecorator(cargarCodigoServicioSolicitadoStatement.executeQuery());
	}
	
	/**
	 * Método para cargar el acronimo días urgente o normal del grupo del servicio solicitado
	 * dependiendo del campo urgente que se selecciono
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio servicio solicitado
	 * @param urgente
	 * @return acronimoDiasTramite
	 */
	public static String cargarDiasTramiteServicioSolicitado(Connection con, int codigoServicio, boolean urgente)
	{
		String acronimoDiasTramite="";
		PreparedStatement ps= null;
		ResultSet rs=null;
		try {
			ps = con.prepareStatement(consultaDiasUrgenteNormalGrupoServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigoServicio);
			rs= ps.executeQuery();
			if(rs.next())
			{
				if(urgente)
				{
					acronimoDiasTramite=rs.getString("acro_dias_urgente");
				}else
				{
					acronimoDiasTramite=rs.getString("acro_dias_normal");
				}
				
				if(acronimoDiasTramite==null)
				{
					acronimoDiasTramite="";
				}
			}
		} catch (SQLException e) {
			logger.error("Error al cargarDiasTramiteServicioSolicitado "+e);
			e.printStackTrace();
		}
		finally{
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				logger.error("Error al cerrar objetos de persistencia "+e);
				e.printStackTrace();
			}
		}
		
		
		
		return acronimoDiasTramite;
	}

	/**
	 * Método para Actualizar  la solicitud  de interconsulta 
	*/
	public static int cambiarSolicitudInterconsulta(
			Connection con,
			int numeroSolicitud,
			String motivoSolicitud,
			String resumenHistoriaClinica,
			String comentario,
			/*String numeroAutorizacion,*/
			DaoFactory myFactory)throws SQLException
	{
			int resp=0;
			try
			{
					if (con == null || con.isClosed()) 
					{
								con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,motivoSolicitud);
					ps.setString(2,resumenHistoriaClinica);
					ps.setString(3,comentario);
					ps.setInt(4,numeroSolicitud);
					return ps.executeUpdate();
					/*if (ps.executeUpdate()<=0)
					{
							return 0;
					}*/						
					//PreparedStatementDecorator ps1=  new PreparedStatementDecorator(con.prepareStatement(modificarNumeroAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					//ps1.setString(1,numeroAutorizacion);
					//ps1.setInt(2,numeroSolicitud);
					//return ps1.executeUpdate();
			}	
			catch(SQLException e)
			{
						logger.error("Error modificando la Interconsulta: "+e.toString());
						resp=0;
			}
			return resp;
	}

	/**
	 * Método para insertar una nueva solicitud de interconsulta
	 */
	public static int insertarSolicitudInterconsulta(Connection con, int numeroSolicitud, int codigoServicioSolicitado, String nombreOtros, String motivoSolicitud, String resumenHistoriaClinica,String comentario, int codigoManejointerconsulta, boolean flagManejoConjuntoFinalizado, DaoFactory myFactory)
	{
		
		
	//	SqlBaseServiciosDao.obtenerMinutosDuracionServicio(codigoServicio, con);
	//	SqlBaseCitaOdontologicaDao.insertarProximaCitaOdontologia(con, arrayServicios, fechaCita, codigoPaciente, loginUsuario, tipoCita, codigoEvolucion, centroCosto);
	//	SqlBaseCitaOdontologicaDao.insertarServiciosCitaOdontologica(con, dto);
		
		/*
		int duracioHora = SqlBaseServiciosDao.obtenerMinutosDuracionServicio(codigoServicioSolicitado, con);
		DtoCitaOdontologica dto = new DtoCitaOdontologica();
		dto.setEstado(ConstantesIntegridadDominio.acronimoReservado);
		dto.setUsuarioModifica("login");
		dto.setDuracion(duracioHora);
		SqlBaseCitaOdontologicaDao.insertarCitaOdontologica(con, dto);
		*/
		
		
		
		
		
			int resp=0;
			try
			{
					if (con == null || con.isClosed()) 
					{
							con = myFactory.getConnection();
					}
					//- Es para veriifcar que no se inserte varias solicitudes con igual servico a la vez. con el mismo paciente. (porque las dos quedan en estado solicitada) 	
					String inser = " SELECT * FROM solicitudes_inter si " +
								   "			   INNER JOIN solicitudes sol ON ( sol.numero_solicitud = si.numero_solicitud ) " +
								   "						  WHERE sol.cuenta =  " + Utilidades.getCuentaSolicitud(con, numeroSolicitud) +
								   "							AND si.codigo_servicio_solicitado = " + codigoServicioSolicitado +	
								   "							AND sol.estado_historia_clinica = " + ConstantesBD.codigoEstadoHCSolicitada;
					if ( Utilidades.nroRegistrosConsulta(con, inser) > 0 )
					{
						return -1;
					}
					else
					{
						
						String insercion = "INSERT INTO" +
									" solicitudes_inter " +
									"(numero_solicitud, codigo_servicio_solicitado, " +
									"nombre_otros, motivo_solicitud, " +
									"resumen_historia_clinica, comentario, " +
									"codigo_manejo_interconsulta, manejo_conjunto_finalizado)" +
									" VALUES " +
									" ( ? , ? , ? , ? , ? , ? , ? , '"+
									(flagManejoConjuntoFinalizado?ValoresPorDefecto.getValorTrueCortoParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas())
									+"')"; 

						PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, numeroSolicitud);
						if (nombreOtros==null)
						{
								ps.setInt(2,codigoServicioSolicitado);
						}
						else
						{
								ps.setObject(2, null);
						}
						ps.setString(3,nombreOtros);

						String motivo=motivoSolicitud.length()>4000?motivoSolicitud.substring(0, 4000):motivoSolicitud;
						motivo=motivo.length()==0?" ":motivo;
						ps.setString(4,motivo);
						ps.setString(5,resumenHistoriaClinica.length()>4000?resumenHistoriaClinica.substring(0, 4000):resumenHistoriaClinica);
						ps.setString(6,comentario.length()>4000?comentario.substring(0, 4000):comentario);
						ps.setInt(7,codigoManejointerconsulta);

						//ps.setBoolean(8, flagManejoConjuntoFinalizado);
						resp=ps.executeUpdate();
						ps.close();
					}	
			}
			catch(SQLException e)
			{
					logger.error("Error insertando la Interconsulta: "+e.toString());
					e.printStackTrace();
					resp=0;
			}
			return resp;
	}

	/**
	* Insertar documentos adjuntos a la solicitud
	* @param con Conexión
	* @param numeroSolicitud numero de la solicitud
	* @param nombreArchivo nombre del archivo
	* @param es_solicitud
	* @return boolean true si los datos fueron insertados correctamente
	*/
	public static ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud, String nombreArchivo, String nombreOriginal, boolean es_solicitud)
	{
			try
			{
					PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaDocumentoAdjunto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					pst.setString(1,String.valueOf(numeroSolicitud));
					pst.setString(2,nombreArchivo);
					pst.setString(3,nombreOriginal);
					pst.setString(4,String.valueOf(es_solicitud));
					
					if(pst.executeUpdate()>0)
					{
							return new ResultadoBoolean(true);
					}
					else
					{
							return new ResultadoBoolean(false,"No se insertó documento  ");
					}
			}
			catch(SQLException e)
			{
					logger.error("Problemas insertando el archivo adjunto"+e.getMessage());
					return new ResultadoBoolean(false,"No se insertó documento ");
			}
	}

	/**
	 * Método encargado de cargar los docs que 
	 * se soliciten en la interconsulta
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud numero de la solicitud
	 * @return  ResultadoCollectionDB
	 */
	public static ResultadoCollectionDB cargarDocumentosAdjuntos(Connection con, int numeroSolicitud)
	{
			ResultadoBoolean resp=new ResultadoBoolean(false);
			ResultSetDecorator rs=null;
			try
			{
					if (con == null || con.isClosed()) 
					{
						//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
						throw new SQLException ("Error SQL: Conexión cerrada");
					}
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaCargarDocumentoAdjunto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,String.valueOf(numeroSolicitud));
					rs=new ResultSetDecorator(ps.executeQuery());
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos");
					resp.setDescripcion(e+" Error en la inserción de datos");
					return new ResultadoCollectionDB(false, "No existe ninguna solicitud "+numeroSolicitud);
			}
			try
			{
					Collection col=UtilidadBD.resultSet2Collection(rs);
					if( col != null && !col.isEmpty() )
					{
							return new ResultadoCollectionDB(true, "", col);
					}
					else
					{
							return new ResultadoCollectionDB(false, "No existe ningun documento adjunto para el numero de solicitud: "+numeroSolicitud);
					}
			}
			catch(Exception e)
			{
					logger.warn(e+" Error en la creacion de la colección");
					resp.setDescripcion(e+" Error en la creacion de la colección");
					return new ResultadoCollectionDB(false, "Error en la creando coleccion ");
			}
	}

	/**
	 * Implementación del método que actualiza el Flag para manejo de
	 * Interconsulta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudInterconsultaDao#actualizarFlagMostrarInterconsulta (Connection , int , boolean ) throws SQLException
	 */
	public static int actualizarFlagMostrarInterconsulta (Connection con, int numeroSolicitud, boolean nuevoEstadoFlag) throws SQLException
	{
		PreparedStatementDecorator actualizarFlagManejoInterconsultaStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarFlagMostrarInterconsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		actualizarFlagManejoInterconsultaStatement.setBoolean(1, nuevoEstadoFlag);
		actualizarFlagManejoInterconsultaStatement.setInt(2, numeroSolicitud);
		logger.info("\n\n\n actualizarFlagMostrarInterconsulta--->"+actualizarFlagMostrarInterconsultaStr+" nuevoEstadoFlag->"+nuevoEstadoFlag+" numsol->"+numeroSolicitud);
		return actualizarFlagManejoInterconsultaStatement.executeUpdate();
	}
	
	/**
	 * M&eacute;todo encargado de consultar el c&oacute;digo del servicio
	 * asociado a una solicitud de interconsulta 
	 * @param Connection con, 
    		String numeroSolicitud, int codigoTarifario
	 * @return DtoServicios
	 * @author Diana Carolina G
	 */
	public static  DtoServicios buscarServiciosSolicitudInterconsulta(Connection con, 
    		int numeroSolicitud, int codigoTarifario){
		
		DtoServicios dtoServicio= null;
		
		String consulta="select ser.codigo, ref.descripcion " +
				"from ordenes.solicitudes_inter sol, facturacion.servicios ser, " +
				"facturacion.referencias_servicio ref, facturacion.tarifarios_oficiales tar " +
				"where sol.codigo_servicio_solicitado = ser.codigo " +
				"and ref.servicio = ser.codigo " +
				"and ref.tipo_tarifario = tar.codigo " +
				"and numero_solicitud = " + numeroSolicitud + " and tar.codigo = " + codigoTarifario;
		
		try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					dtoServicio = new DtoServicios();
					dtoServicio.setCodigoServicio(rs.getInt(1));
					dtoServicio.setDescripcionServicio(rs.getString(2));
				}
			
		} catch (SQLException e) {
			logger.warn(e + "Error en buscarServiciosSolicitudInterconsulta de: SqlBaseSolicitudInterconsultaDao"+e.toString());
		}
		
		return dtoServicio;
	}


}
