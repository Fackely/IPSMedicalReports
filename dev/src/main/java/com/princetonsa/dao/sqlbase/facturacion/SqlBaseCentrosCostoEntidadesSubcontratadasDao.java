package com.princetonsa.dao.sqlbase.facturacion;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public class SqlBaseCentrosCostoEntidadesSubcontratadasDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCentrosCostoEntidadesSubcontratadasDao.class);
	
	private static String consultarCentrosCostoEntiSub = "SELECT cces.consecutivo, " +
															"cces.centro_costo, " +
															"cces.entidad_subcontratada, " +
															"cces.nro_prioridad, " +
															"cces.resp_otros_usuarios, " +
															"cc.nombre AS nombre_centro_costo, " +
															"es.razon_social AS nombre_entidad_sub, " +
															"cc.centro_atencion, " +
															"cc.tipo_area " +
															"FROM facturacion.centros_costo_entidades_sub cces " +
															"INNER JOIN administracion.centros_costo cc ON (cces.centro_costo= cc.codigo) " +
															"INNER JOIN facturacion.entidades_subcontratadas es ON (cces.entidad_subcontratada= es.codigo_pk) " +
															"WHERE cc.centro_atencion=? " +
															"ORDER BY nombre_centro_costo ";
	
	
	private static String obtenerPrioridadCentrosCostoEntiSub = "SELECT cces.consecutivo, " +
																"cces.nro_prioridad " +
																"FROM facturacion.centros_costo_entidades_sub cces " +
																"INNER JOIN administracion.centros_costo cc ON (cces.centro_costo= cc.codigo) " +
																"INNER JOIN facturacion.entidades_subcontratadas es ON (cces.entidad_subcontratada= es.codigo_pk) " +
																"WHERE cc.centro_atencion=? " +
																"AND (cc.tipo_entidad_ejecuta= '"+ConstantesIntegridadDominio.acronimoAmbos+"' OR cc.tipo_entidad_ejecuta= '"+ConstantesIntegridadDominio.acronimoExterna+"') " +
																"ORDER BY cces.nro_prioridad ";
	
	private static String obtenerPrioridadCentrosCostoEntiSubNoCentroAtencion = "SELECT cces.consecutivo, " +
																"cces.nro_prioridad " +
																"FROM facturacion.centros_costo_entidades_sub cces " +
																"INNER JOIN administracion.centros_costo cc ON (cces.centro_costo= cc.codigo) " +
																"INNER JOIN facturacion.entidades_subcontratadas es ON (cces.entidad_subcontratada= es.codigo_pk) " +
																"WHERE (cc.tipo_entidad_ejecuta= '"+ConstantesIntegridadDominio.acronimoAmbos+"' OR cc.tipo_entidad_ejecuta= '"+ConstantesIntegridadDominio.acronimoExterna+"') " +
																"ORDER BY cces.nro_prioridad ";	
	
	
	
	private static String insertarNuevoRegistroCentroCostoEntiSub = "INSERT INTO facturacion.centros_costo_entidades_sub " +
			                                                         "VALUES (?,?,?,?,?,?,CURRENT_DATE,?,?) ";
	
	private static String actualizarCentroCostoEntiSub = "UPDATE facturacion.centros_costo_entidades_sub SET " +
															"nro_prioridad = ?, " +
															"resp_otros_usuarios = ?, " +
															"usuario_modifica = ?, " +
															"fecha_modifica = CURRENT_DATE, " +
															"hora_modifica = ? " +
															"WHERE consecutivo = ? "; 
	
	private static String eliminarCentroCostoEntiSub = "DELETE FROM facturacion.centros_costo_entidades_sub WHERE consecutivo=? ";
	
	
	private static String guardarLogCentrosCostoEntiSub = "INSERT INTO facturacion.log_centros_costo_ent_sub " +															
															"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?) ";
	
	
	
	
	/**
	 * Metodo que guarda un log tipo BD para modificaciones
	 * @param criterios
	 * @return
	 */
	public static boolean guardarLogCentrosCostoEntiSub (HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
								
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(guardarLogCentrosCostoEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));	
							
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_log_centros_costo_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("centroCosto")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("entidadSubcontratada")+""));
			ps.setString(4, criterios.get("eliminar")+"");
			if((criterios.get("nroPrioridadAnterior")+"").equals(""))
				ps.setNull(5,java.sql.Types.VARCHAR);
			else
				ps.setString(5, criterios.get("nroPrioridadAnterior")+"");
			if((criterios.get("nroPrioridadNueva")+"").equals(""))
				ps.setNull(6, java.sql.Types.VARCHAR);
			else
				ps.setString(6, criterios.get("nroPrioridadNueva")+"");
			if((criterios.get("respOtrosUsuariosAnterior")+"").equals(""))
				ps.setNull(7, java.sql.Types.VARCHAR);
			else
				ps.setString(7, criterios.get("respOtrosUsuariosAnterior")+"");
			if((criterios.get("respOtrosUsuariosNueva")+"").equals(""))
				ps.setNull(8, java.sql.Types.VARCHAR);
			else
				ps.setString(8, criterios.get("respOtrosUsuariosNueva")+"");
			ps.setString(9, criterios.get("usuario")+"");
			ps.setString(10, UtilidadFecha.getHoraActual());
			
			logger.info("\n\nGUARDANDO LOG CENTRO COSTO POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+guardarLogCentrosCostoEntiSub);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			
			ps.close();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. GUARDANDO LOG CENTRO COSTO POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return false;
	}
	
	/**
	 * Metodo que elimina un registro de centro de costo por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarCentroCostoEntiSub(int consecutivo)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nELIMINAR NUEVO REGISTRO CENTRO COSTO POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+eliminarCentroCostoEntiSub);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarCentroCostoEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
					
			ps.setInt(1, consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			
			ps.close();
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO CENTRO COSTO POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return false;
	}
	
	/**
	 * Metodo para actualizar un registro de centro costo por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarCentroCostoEntiSub(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
						
		logger.info("\n\nACTUALIZANDO REGISTRO CENTRO COSTO POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+actualizarCentroCostoEntiSub);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCentroCostoEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			
			ps.setString(1, criterios.get("nroPrioridad")+"");
			ps.setString(2, criterios.get("respOtrosUsuarios")+"");
			ps.setString(3, criterios.get("usuario")+"");
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setInt(5, Utilidades.convertirAEntero(criterios.get("consecutivo")+""));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			
			ps.close();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO CENTRO COSTO POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return false;
	}
	
	/**
	 * Metodo para insertar un nuevo registro de centro de costo por entidada subcontratada
	 * @param criterios
	 * @return
	 */
	public static int insertarNuevoRegistro(HashMap criterios)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		logger.info("\n\nCRITERIOS----->"+criterios);
		
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarNuevoRegistroCentroCostoEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_cent_costo_ent_sub");
			ps.setInt(1, valorseq);
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("centroCosto")+""));
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get("entidadSubcontratada")+""));
			ps.setString(4, criterios.get("nroPrioridad")+"");
			ps.setString(5, criterios.get("respOtrosUsuarios")+"");
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setInt(8, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			logger.info("\n\nINSERTANDO NUEVO REGISTRO CENTRO COSTO POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+insertarNuevoRegistroCentroCostoEntiSub);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return valorseq;
			}
			
			ps.close();
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO NUEVO REGISTRO CENTRO COSTO POR ENTIDAD SUBCONTRATADA------>>>>>>"+e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return 0;
	}
	
	
	/**
	 * Metodo para consultar los centro de consto por entidad subcontratada
	 * @return
	 */
	public static HashMap consultarCentrosCostoEntiSub(int centroAtencion)
	{
		HashMap resultados= new HashMap();
		
		Connection con;
		
		con= UtilidadBD.abrirConexion();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, centroAtencion);
					
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			logger.info("\n\nCONSULTA CENTRO COSTO POR ENTIDAD SUBCONTRATADA----->>>>>>>>>>>"+consultarCentrosCostoEntiSub.replace("?", centroAtencion+""));
			
			ps.close();
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO CENTROS DE COSTO POR ENTIDADES SUBCONTRATADAS------>>>>>>"+e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return resultados;
	}
	
	
	/**
	 * Este Método se encarga de consultar las prioridades de las entidades subcontradas
	 * 
	 * @param int centroAtencion
	 * @return HashMap
	 * @author Angela Maria Aguirre, Cristhian Murillo
	 */
	public static HashMap obtenerPrioridadCentrosCostoEntiSub(int centroAtencion)
	{
		HashMap resultados= new HashMap();
		Connection con;
		con= UtilidadBD.abrirConexion();
		
		try
		{
			/* Se modifica para que traiga todos los niveles de todos los Centros de Atención. */
			if(centroAtencion > 0)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerPrioridadCentrosCostoEntiSub, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
				ps.setInt(1, centroAtencion);
				resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
				logger.info("\n\nCONSULTA PRIORIDAD DE ENTIDADES SUBCONTRATADAS----->>>>>>>>>>>"+obtenerPrioridadCentrosCostoEntiSub.replace("?", centroAtencion+""));
				ps.close();
			}
			else
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerPrioridadCentrosCostoEntiSubNoCentroAtencion, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
				resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
				logger.info("\n\nCONSULTA PRIORIDAD DE ENTIDADES SUBCONTRATADAS SIN CENTRO ATENCION----->>>>>>>>>>>"+obtenerPrioridadCentrosCostoEntiSubNoCentroAtencion);
				ps.close();
			}
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO PRIORIDAD DE ENTIDADES SUBCONTRATADAS------>>>>>>"+e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return resultados;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultarEntidadesSubSinInterna(Connection con, int codigoEntidadSubInterna)
	{
		HashMap resultados= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT ent.codigo_pk codigo, ent.razon_social descripcion  FROM entidades_subcontratadas ent WHERE ent.codigo_pk > 0 AND ent.codigo_pk != ? ORDER BY ent.razon_social";
			pst =  con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			pst.setInt(1, codigoEntidadSubInterna);
			rs=pst.executeQuery();
			int cont=0;
			resultados.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					resultados.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			resultados.put("numRegistros", cont+"");		
		}
		catch (SQLException e){
			Log4JManager.error(e);
		}
		finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pst!=null){
					pst.close();
				}
			} catch (SQLException e) {
				Log4JManager.error(e);
			}
		}
		return resultados;
	}
	
}