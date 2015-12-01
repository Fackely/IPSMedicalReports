package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoParametrosBusquedaHonorarios;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoPorcentajeValor;
import util.UtilidadBD;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseCalculoHonorariosPoolesDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCalculoHonorariosPoolesDao.class);
	
	/**
	 * 
	 * @param servicio
	 * @param tipoLiquidacion
	 * @param pool
	 * @param convenio
	 * @param esquemaTarifario
	 * @param especialidad
	 * @return
	 */
	public static InfoPorcentajeValor obtenerHonorarioPoolXTipoLiquidacion (DtoParametrosBusquedaHonorarios dto)
	{
		InfoPorcentajeValor resultado= new InfoPorcentajeValor();
		logger.info("\n\n\n**********************************METODO CALCULO HONORARIOS POOL************************************************");
		logger.info("PARAMETROS ENTRADA");
		logger.info("codigoServicio: "+dto.getServicio());
		logger.info("tipoLiquidacion: "+dto.getTipoLiquidacion());
		
		//1.1 CASO EN EL CUAL EL TIPO DE LIQUIDACION ES POOL
		if(dto.getTipoLiquidacion().equals(ConstantesIntegridadDominio.acronimoPool))
		{
			resultado= obtenerHonorarioPoolCasoConvenio(dto.getPool(), dto.getConvenio(), dto.getServicio(), dto.getCentroAtencion());
			if(resultado==null)
			{	
				resultado= obtenerHonorarioPoolCasoEsquemaTarifario(dto.getPool(), dto.getEsquemaTarifario(), dto.getServicio(), dto.getCentroAtencion());
			}	
		}
		else if(dto.getTipoLiquidacion().equals(ConstantesIntegridadDominio.acronimoEspecialidadProfesional))
		{
			resultado= obtenerHonorarioPoolCasoEspecialidad(dto.getEsquemaTarifario(), dto.getServicio(), dto.getEspecialidad(), dto.getCentroAtencion());
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoServicio
	 * @param codigoDetalleCargo
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioPoolCasoEsquemaTarifario(final int pool, final int esquemaTarifario, final int servicio, final int centroAtencion)
	{
		InfoPorcentajeValor resultado= new InfoPorcentajeValor();
		logger.info("----OBTENER HONORARIO POOL CASO ESQUEMA TARIFARIO");
		//1. OBTENEMOS HONORARIO POOL X ESQUEMA TARIFARIO
		double codigoEncabezado= obtenerCodigoEncabezadoHonorario(pool, esquemaTarifario, centroAtencion, false);
		
		if(codigoEncabezado<=0)
		{
			logger.info("No existe encabezado de honorarios pool x esquema tarifario entonces no seguimos buscando");
			logger.info("retorna null");
			return null;
		}
		resultado = obtenerDetalleHonorario(servicio, codigoEncabezado);
		
		return resultado;
	}

	/**
	 * 
	 * @param codigoServicio
	 * @param codigoDetalleCargo
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioPoolCasoConvenio(final int pool, final int convenio, final int servicio, final int centroAtencion)
	{
		InfoPorcentajeValor resultado= new InfoPorcentajeValor();
		logger.info("----OBTENER HONORARIO POOL CASO CONVENIO");
		//1. OBTENEMOS HONO RARIO POOL X CONVENIO
		double codigoEncabezado= obtenerCodigoEncabezadoHonorario(pool, convenio, centroAtencion, true);
		
		if(codigoEncabezado<=0)
		{
			logger.info("No existe encabezado de honorarios pool x convenio entonces no seguimos buscando");
			logger.info("retorna null");
			return null;
		}
		resultado = obtenerDetalleHonorario(servicio, codigoEncabezado);
		
		return resultado;
	}
	
	/**
	 * @param servicio
	 * @param codigoEncabezado
	 * @return
	 */
	private static InfoPorcentajeValor obtenerDetalleHonorario(final int servicio, final double codigoEncabezado)
	{
		InfoPorcentajeValor resultado;
		logger.info("Codigo encabezado encontrado: "+codigoEncabezado);
		logger.info("Ahora buscamos x servicio especifico: "+servicio);
		
		//////BUSQUEDA X SERVICIO
		resultado= obtenerHonorarioXServicio(codigoEncabezado, servicio);
		if(resultado==null)
		{
			logger.info("NO ENCONTRÓ PARA EL SERVICIO ENTONCES LO BUSCAMOS CON SERVICIO NULL");
			resultado= obtenerHonorarioXServicio(codigoEncabezado, ConstantesBD.codigoNuncaValido /*servicio*/);
		}
		
		//////BUSQUEDA X GRUPO
		if(resultado==null)
		{
			logger.info("DEBEMOS BUSCAR X GRUPO SERVICIO");
			int grupoServicio = obtenerGrupoServicio(servicio);
			logger.info("Grupo del servicio-->"+grupoServicio);
			resultado= obtenerHonorarioXGrupo(codigoEncabezado, grupoServicio);
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXGrupo(codigoEncabezado, ConstantesBD.codigoNuncaValido /*grupoServicio*/);
			}
		}
		
		/////BUSQUEDA X TIPO SERVICIO
		if(resultado==null)
		{
			logger.info("DEBEMOS BUSCAR X TIPO SERVICIO");
			String tipoServicio = obtenerTipoServicio(servicio);
			logger.info("Tipo del servicio-->"+tipoServicio);
			resultado= obtenerHonorarioXTipoServ(codigoEncabezado, tipoServicio);
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXTipoServ(codigoEncabezado, "" /*tipoServicio*/);
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param pool
	 * @param codigoNuncaValido
	 * @param esquemaTarifario
	 * @return
	 */
	private static double obtenerCodigoEncabezadoHonorario(final int pool, final int convenioOEsquema, final int centroAtencion, final boolean esConvenio)
	{
		double resultado=0;
		String consulta= "select " +
							"codigo_pk " +
						"from " +
							"facturacion.honorarios_pool " +
						"where " +
							"pool=? " +
							"and (centro_atencion=? or centro_atencion is null) ";
		
		consulta+= (esConvenio)? " AND (convenio="+convenioOEsquema+" or convenio is null) ":" AND (esquema_tarifario= "+convenioOEsquema+" or esquema_tarifario is null) ";
		consulta+=" ORDER BY pool, "+(esConvenio? " convenio ": "esquema_tarifario ")+" , centro_atencion ";
		
		logger.info("\n obtenerCodigoEncabezadoHonorario->"+consulta+" "+pool);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, pool);
			ps.setInt(2, centroAtencion);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= rs.getDouble(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerTipoLiquidacionCargo 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoEncabezado
	 * @param servicio
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioXServicio(final double codigoHonorarioPool, final int servicio)
	{
		InfoPorcentajeValor resultado=null;
		String consulta= 	"SELECT " +
								"coalesce(porcentaje_participacion,"+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_participacion,"+ConstantesBD.codigoNuncaValido+") " +
							"FROM " +
								"facturacion.honorarios_pool_serv " +
							"WHERE " +
								"honorario_pool=? ";
								
		consulta+= (servicio>0)? " AND servicio="+servicio+" ":" AND servicio is null ";
		logger.info("\n obtenerHonorarioXServicio->"+consulta+" "+codigoHonorarioPool);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoHonorarioPool);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= new InfoPorcentajeValor();
				resultado.setPorcentaje(rs.getDouble(1));
				resultado.setValor(rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerTipoLiquidacionCargo 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * @param servicio
	 * @return
	 */
	private static int obtenerGrupoServicio(int servicio)
	{
		Connection con= UtilidadBD.abrirConexion();
		int grupoServicio= SqlBaseUtilidadesDao.obtenerGrupoServicio(con, servicio);
		UtilidadBD.closeConnection(con);
		return grupoServicio;
	}
	
	/**
	 * 
	 * @param codigoEncabezado
	 * @param grupoServicio
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioXGrupo(double codigoHonorarioPool,	int grupoServicio)
	{
		InfoPorcentajeValor resultado=null;
		String consulta= 	" SELECT " +
								"coalesce(porcentaje_participacion,"+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_participacion,"+ConstantesBD.codigoNuncaValido+") " +
							"FROM " +
								"facturacion.agrup_honorarios_pool " +
							"WHERE " +
								"honorario_pool=? ";
								
		consulta+= (grupoServicio>0)? " AND grupo_servicio="+grupoServicio+" ":" AND grupo_servicio is null ";
		logger.info("\n obtenerHonorarioXGrupo->"+consulta+" "+codigoHonorarioPool);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoHonorarioPool);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= new InfoPorcentajeValor();
				resultado.setPorcentaje(rs.getDouble(1));
				resultado.setValor(rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerTipoLiquidacionCargo 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoEncabezado
	 * @param tipoServicio
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioXTipoServ(double codigoHonorarioPool, String tipoServicio)
	{
		InfoPorcentajeValor resultado=null;
		String consulta= 	" SELECT " +
								"coalesce(porcentaje_participacion,"+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_participacion,"+ConstantesBD.codigoNuncaValido+") " +
							"FROM " +
								"facturacion.agrup_honorarios_pool " +
							"WHERE " +
								"honorario_pool=? ";
								
		consulta+= (!UtilidadTexto.isEmpty(tipoServicio))? " AND tipo_servicio='"+tipoServicio+"' ":" AND tipo_servicio is null ";
		logger.info("\n obtenerHonorarioXTipoServ->"+consulta+" "+codigoHonorarioPool);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoHonorarioPool);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= new InfoPorcentajeValor();
				resultado.setPorcentaje(rs.getDouble(1));
				resultado.setValor(rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerTipoLiquidacionCargo 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * @param servicio
	 * @return
	 */
	private static String obtenerTipoServicio(int servicio)
	{
		Connection con= UtilidadBD.abrirConexion();
		String tipoServicio = SqlBaseUtilidadesDao.obtenerTipoServicio(con, servicio+"");
		UtilidadBD.closeConnection(con);
		return tipoServicio;
	}

	/**
	 * 
	 * @param esquemaTarifario
	 * @param servicio
	 * @param especialidad
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioPoolCasoEspecialidad(int esquemaTarifario, int servicio, int especialidad, int centroAtencion)
	{
		InfoPorcentajeValor resultado;
		logger.info("---OBTENER HONORARIO X ESPECIALIDAD");
		double codigoEncabezado= obtenerCodigoEncabezadoHonorarioEspecialidad(esquemaTarifario, centroAtencion);
		
		if(codigoEncabezado<=0)
		{
			logger.info("No existe encabezado de honorarios pool x esquema tarifario entonces no seguimos buscando");
			logger.info("retorna null");
			return null;
		}
		
		logger.info("Codigo encabezado encontrado: "+codigoEncabezado);
		logger.info("Ahora buscamos x servicio especifico: "+servicio);
		
		//////BUSQUEDA X SERVICIO
		resultado= obtenerHonorarioXServicioEspecialidad(codigoEncabezado, servicio, especialidad);
		if(resultado==null)
		{
			logger.info("NO ENCONTRÓ PARA EL SERVICIO ENTONCES LO BUSCAMOS CON SERVICIO NULL");
			resultado= obtenerHonorarioXServicioEspecialidad(codigoEncabezado, servicio, ConstantesBD.codigoNuncaValido/*especialidad*/);
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXServicioEspecialidad(codigoEncabezado, ConstantesBD.codigoNuncaValido /*servicio*/, especialidad);
			}
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXServicioEspecialidad(codigoEncabezado, ConstantesBD.codigoNuncaValido/*servicio*/, ConstantesBD.codigoNuncaValido/*especialidad*/);
			}
			
		}
		
		/////BUSQUEDA X TIPO SERVICIO
		if(resultado==null)
		{
			logger.info("DEBEMOS BUSCAR X TIPO SERVICIO");
			String tipoServicio = obtenerTipoServicio(servicio);
			logger.info("Tipo del servicio-->"+tipoServicio);
			resultado= obtenerHonorarioXTipoServEspecialidad(codigoEncabezado, tipoServicio, especialidad);
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXTipoServEspecialidad(codigoEncabezado, tipoServicio, ConstantesBD.codigoNuncaValido/*especialidad*/);
			}
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXTipoServEspecialidad(codigoEncabezado, "" /*tipoServicio*/, especialidad);
			}
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXTipoServEspecialidad(codigoEncabezado, ""/*tipoServicio*/, ConstantesBD.codigoNuncaValido/*especialidad*/);
			}
		}
		
		//////BUSQUEDA X GRUPO
		if(resultado==null)
		{
			logger.info("DEBEMOS BUSCAR X GRUPO SERVICIO");
			int grupoServicio = obtenerGrupoServicio(servicio);
			logger.info("Grupo del servicio-->"+grupoServicio);
			resultado= obtenerHonorarioXGrupoEspecialidad(codigoEncabezado, grupoServicio, especialidad);
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXGrupoEspecialidad(codigoEncabezado, grupoServicio, ConstantesBD.codigoNuncaValido /*especialidad*/);
			}
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXGrupoEspecialidad(codigoEncabezado, ConstantesBD.codigoNuncaValido /*grupoServicio*/, especialidad);
			}
			
			if(resultado==null)
			{
				resultado= obtenerHonorarioXGrupoEspecialidad(codigoEncabezado, ConstantesBD.codigoNuncaValido /*grupoServicio*/, ConstantesBD.codigoNuncaValido /*especialidad*/);
			}
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param esquemaTarifario
	 * @return
	 */
	private static double obtenerCodigoEncabezadoHonorarioEspecialidad(int esquemaTarifario, int centroAtencion)
	{
		double resultado=0;
		String consulta=" select " +
							"codigo " +
						"from " +
							"odontologia.honorarios_esp_serv " +
						"where " +
							"(esquema_tarifario= ? or esquema_tarifario is null) " +
							"and (centro_atencion=? or centro_atencion is null) " +
						"order by esquema_tarifario, centro_atencion ";
		logger.info("\n obtenerCodigoEncabezadoHonorarioEspecialidad->"+consulta+" "+esquemaTarifario+" "+centroAtencion);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, esquemaTarifario);
			ps.setInt(2, centroAtencion);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= rs.getDouble(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerTipoLiquidacionCargo 1",e);
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoHonorarioEspecialidad
	 * @param servicio
	 * @param especialidad
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioXServicioEspecialidad(double codigoHonorarioEspecialidad, int servicio, int especialidad)
	{
		InfoPorcentajeValor resultado=null;
		String consulta= 	"SELECT " +
								"coalesce(porcentaje_participacion,"+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_participacion,"+ConstantesBD.codigoNuncaValido+") " +
							"from " +
								"odontologia.det_serv_honora_esp_serv " +
							"WHERE " +
								"codigo_honorario=? ";
								
		consulta+= (servicio>0)? " AND servicio="+servicio+" ":" AND servicio is null ";
		consulta+= (especialidad>0)? " AND especialidad="+especialidad+" ":" AND especialidad is null ";
		
		logger.info("\n obtenerHonorarioXServicioEspecialidad->"+consulta+" "+codigoHonorarioEspecialidad);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoHonorarioEspecialidad);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= new InfoPorcentajeValor();
				resultado.setPorcentaje(rs.getDouble(1));
				resultado.setValor(rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerHonorarioXServicioEspecialidad 1",e);
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoEncabezado
	 * @param grupoServicio
	 * @param especialidad
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioXGrupoEspecialidad(	double codigoHonorarioEspecialidad, int grupoServicio, int especialidad)
	{
		InfoPorcentajeValor resultado=null;
		String consulta= 	"SELECT " +
								"coalesce(porcentaje_participacion, "+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_participacion, "+ConstantesBD.codigoNuncaValido+") " +
							"from " +
								"odontologia.det_agru_honora_esp_serv " +
							"where " +
								"codigo_honorario=? ";
								
		consulta+= (grupoServicio>0)? " AND grupo_servicio="+grupoServicio+" ":" AND grupo_servicio is null ";
		consulta+= (especialidad>0)? " AND especialidad="+especialidad+" ":" AND especialidad is null ";
		logger.info("\n obtenerHonorarioXGrupoEspecialidad->"+consulta+" "+codigoHonorarioEspecialidad);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoHonorarioEspecialidad);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= new InfoPorcentajeValor();
				resultado.setPorcentaje(rs.getDouble(1));
				resultado.setValor(rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerHonorarioXGrupoEspecialidad 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoEncabezado
	 * @param tipoServicio
	 * @param especialidad
	 * @return
	 */
	private static InfoPorcentajeValor obtenerHonorarioXTipoServEspecialidad(double codigoHonorarioEspecialidad, String tipoServicio, int especialidad)
	{
		InfoPorcentajeValor resultado=null;
		String consulta= 	"SELECT " +
								"coalesce(porcentaje_participacion, "+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_participacion, "+ConstantesBD.codigoNuncaValido+") " +
							"from " +
								"odontologia.det_agru_honora_esp_serv " +
							"where " +
								"codigo_honorario=? ";
								
		consulta+= (!UtilidadTexto.isEmpty(tipoServicio))? " AND tipo_servicio='"+tipoServicio+"' ":" AND tipo_servicio is null ";
		consulta+= (especialidad>0)? " AND especialidad="+especialidad+" ":" AND especialidad is null ";
		logger.info("\n obtenerHonorarioXTipoServEspecialidad->"+consulta+" "+codigoHonorarioEspecialidad);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoHonorarioEspecialidad);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= new InfoPorcentajeValor();
				resultado.setPorcentaje(rs.getDouble(1));
				resultado.setValor(rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerHonorarioXTipoServEspecialidad 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoPorcentajeValor obtenerHonorariosPromocionesCargos(double codigoDetalleCargo)
	{
		InfoPorcentajeValor resultado=null;
		String consulta="select " +
								"coalesce(porc_honorario_prom_serv, "+ConstantesBD.codigoNuncaValido+"), " +
								"coalesce(valor_honorario_prom_serv,"+ConstantesBD.codigoNuncaValido+") " +
							"from " +
								"facturacion.det_cargos " +
							"where " +
								"codigo_detalle_cargo=? ";
		
		logger.info("obtenerHonorariosPromocionesCargos-->"+consulta+" "+codigoDetalleCargo );
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoDetalleCargo);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				/////OJO ESTO NO SE PUEDE CAMBIAR, DEBE ENVIAR  NULO SI NO EXISTE, CERO ES VALIDO,,,,,,,,,,,,,
				if(rs.getDouble(1)>ConstantesBD.codigoNuncaValido || rs.getBigDecimal(2).doubleValue()>ConstantesBD.codigoNuncaValido)
				{	
					resultado= new InfoPorcentajeValor();
					resultado.setPorcentaje(rs.getDouble(1));
					resultado.setValor(rs.getBigDecimal(2));
				}	
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerHonorarioXTipoServEspecialidad 1",e);
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static DtoParametrosBusquedaHonorarios cargarParametrosBusquedaHonorarios(double codigoDetalleCargo)
	{
		DtoParametrosBusquedaHonorarios resultado= new DtoParametrosBusquedaHonorarios();
		String consulta="select " +
							"coalesce(d.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
							"coalesce(m.tipo_liquidacion, '') as tipo_liquidacion, " +
							"coalesce(s.pool, "+ConstantesBD.codigoNuncaValido+") as pool, " +
							"d.convenio as convenio, " +
							"d.esquema_tarifario as esquema, " +
							"coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") as especialidad_solicitada, " +
							"cc.centro_atencion as centro_atencion, " +
							"coalesce(s.codigo_medico_responde, "+ConstantesBD.codigoNuncaValido+") as medicoresponde " +
						"from " +
							"facturacion.det_cargos d " +
							"inner join ordenes.solicitudes s on (s.numero_solicitud=d.solicitud)  " +
							"inner join administracion.centros_costo cc on (cc.codigo=s.centro_costo_solicitado) " +
							"left outer join administracion.medicos m on(m.codigo_medico=s.codigo_medico_responde) " +
						"where " +
							"d.codigo_detalle_cargo= ?";
		
		logger.info("CARGAR PARAMETROD DE BUSQUEDA consulta ->"+consulta+ " "+codigoDetalleCargo);
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoDetalleCargo);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado.setConvenio(rs.getInt("convenio"));
				resultado.setEspecialidad(rs.getInt("especialidad_solicitada"));
				resultado.setEsquemaTarifario(rs.getInt("esquema"));
				resultado.setPool(rs.getInt("pool"));
				resultado.setServicio(rs.getInt("servicio"));
				resultado.setTipoLiquidacion(rs.getString("tipo_liquidacion"));
				resultado.setCentroAtencion(rs.getInt("centro_atencion"));
				resultado.setCodigoMedicoResponde(rs.getInt("medicoresponde"));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerHonorarioXTipoServEspecialidad 1",e);
			e.printStackTrace();
		}
			
		return resultado;
	}
	
}