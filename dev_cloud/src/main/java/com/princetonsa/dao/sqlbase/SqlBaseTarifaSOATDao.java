/*
 * @(#)SqlBaseTarifaSOATDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;


/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para una tarifa de tipo soat
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class SqlBaseTarifaSOATDao
{
	
	private static String buscarTarifaSOATStr="SELECT " +
													"ts.codigo AS codigo, " +
													"ts.esquema_tarifario AS codigoEsquemaTarifario, " +
													"et.nombre AS nombreEsquemaTarifario, " +
													"ts.unidades AS grupo, " +
													"ts.valor_tarifa AS valorTarifa, " +
													"ts.porcentaje_iva AS porcentajeIva, " +
													"ts.servicio AS codigoServicio, " +
													"rs.descripcion AS nombreServicio, " +
													"e.codigo AS codigoEspecialidad, " +
													"e.nombre AS nombreEspecialidad, " +
													"tls.codigo AS codigoTipoLiquidacion, " +
													"tls.nombre AS nombreTipoLiquidacion, " +
													"ts.liq_asocios AS liqAsocios, " +
													"ts.fecha_vigencia AS fechavigencia " +
												"FROM " +
													"tarifas_soat ts " +
													"INNER JOIN esquemas_tarifarios et ON (ts.esquema_tarifario = et.codigo) " +
													"INNER JOIN servicios s ON (ts.servicio = s.codigo) " +
													"INNER JOIN referencias_servicio rs ON (rs.servicio = s.codigo) " +
													"INNER JOIN especialidades e ON (s.especialidad = e.codigo) " +
													"LEFT OUTER JOIN tipos_liquidacion_soat tls ON (ts.tipo_liquidacion = tls.codigo ) "+
												"WHERE ";
	
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseTarifaSOATDao.class);
	
	/**
	 * Consutla que retorna el número de tarifas para el esquema tarifario y el servicio hay definidas. 
	 * Si hay deberia ser 1, pero por si algose mete el contador 
	 */
	private static String consultaExisteTarifaDefinida = 	"SELECT COUNT(*) AS numTarifasDefinidas " +
																					"FROM tarifas_soat ts " +
																					"WHERE ts.esquema_tarifario = ? " +
																					"AND ts.servicio = ? ";
	
	/**
	 * Consulta todos los campos de la tarifa soat dada por el esquema tarifario y el servicio.
	 * Como deberia haber solo una definida para esos datos, retorna 0 o 1 filas.
	 */
	private static String consultaTarifaPorETyS =	"SELECT ts.codigo AS codigo, " +
																			"ts.esquema_tarifario AS codigoEsquemaTarifario, " +
																			"et.nombre AS nombreEsquemaTarifario, " +
																			"ts.unidades AS grupo, " +
																			"ts.valor_tarifa AS valorTarifa, " +
																			"ts.porcentaje_iva AS porcentajeIva, " +
																			"ts.servicio AS codigoServicio, " +
																			"rs.descripcion AS nombreServicio, " +
																			"e.codigo AS codigoEspecialidad, " +
																			"e.nombre AS nombreEspecialidad, " +
																			"tls.codigo AS codigoTipoLiquidacion, " +
																			"tls.nombre AS nombreTipoLiquidacion, " +
																			"ts.liq_asocios AS liqAsocios " +
																			"FROM esquemas_tarifarios et, referencias_servicio rs, servicios s, especialidades e, tarifas_soat ts " +
																			"LEFT OUTER JOIN tipos_liquidacion_soat tls ON (ts.tipo_liquidacion = tls.codigo ) " +
																			"WHERE ts.esquema_tarifario = ? " +
																			"AND ts.servicio = ? " +
																			"AND ts.servicio = s.codigo " +
																			"AND et.codigo = ts.esquema_tarifario " +
																			"AND rs.servicio = ts.servicio " +
																			"AND s.especialidad = e.codigo " +
																			"AND rs.tipo_tarifario =" +ConstantesBD.codigoTarifarioCups+" ";
	

	/**
	 * Consulta todos los campos de la tarifa soat dados por el código
	 */
	private static String consultaPorCodigoStr =	"SELECT ts.codigo AS codigo, " +
																			"ts.esquema_tarifario AS codigoEsquemaTarifario, " +
																			"et.nombre AS nombreEsquemaTarifario, " +
																			"ts.unidades AS grupo, " +
																			"ts.valor_tarifa AS valorTarifa, " +
																			"ts.porcentaje_iva AS porcentajeIva, " +
																			"ts.servicio AS codigoServicio, " +
																			"rs.descripcion AS nombreServicio, " +
																			"e.codigo AS codigoEspecialidad, " +
																			"e.nombre AS nombreEspecialidad, " +
																			"tls.codigo AS codigoTipoLiquidacion, " +
																			"tls.nombre AS nombreTipoLiquidacion, " +
																			"ts.liq_asocios AS liqAsocios " +
																			"FROM esquemas_tarifarios et, referencias_servicio rs, servicios s, especialidades e, tarifas_soat ts " +
																			"LEFT OUTER JOIN tipos_liquidacion_soat tls ON (ts.tipo_liquidacion = tls.codigo ) " +
																			"WHERE ts.codigo = ? " +
																			"AND ts.servicio = s.codigo " +
																			"AND et.codigo = ts.esquema_tarifario " +
																			"AND rs.servicio = ts.servicio " +
																			"AND s.especialidad = e.codigo " +
																			"AND rs.tipo_tarifario =" +ConstantesBD.codigoTarifarioCups+" ";
																			
	private static String modificarTarifaStr = 	"UPDATE tarifas_soat " +
																	"SET esquema_tarifario = ?, " +
																	"unidades = ?, " +
																	"valor_tarifa = ?, " +
																	"porcentaje_iva = ?, " +
																	"tipo_liquidacion = ?, " +
																	"servicio = ?," +
																	"liq_asocios = ?," +
																	"usuario_modifica=?," +
																	"fecha_modifica=?," +
																	"hora_modifica=?, " +
																	"fecha_vigencia=? " +
																	"WHERE codigo = ? ";
																	
	private static String eliminarTarifaStr = "DELETE FROM tarifas_soat " +
																"WHERE codigo = ? ";
	
	/**
	 * Cadena para la insercion del log tipo BD de Tarifas de servicios.
	 */
	private static final String cadenaLogStr = "INSERT INTO log_tarifas_servicios (codigo, esquema_tarifario, servicio, tipo_liquidacion, valor_tarifa, liq_asocios, tipo_liquidacion_antes, valor_tarifa_antes, liq_asocios_antes,	usuario_modifica, fecha_modifica, hora_modifica, fecha_vigencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
																			
	/**
	 * Modifica una tarifa soat dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa, int, código de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param liq_asocios 
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#modificar(java.sql.Connection, int, int, int, int, int, double, double)
	 */
	public static ResultadoBoolean modificar(	Connection con,
																int codigoTarifa,
																int codigoEsquemaTarifario,
																int codigoServicio,
																double grupo,
																int codigoTipoLiquidacion,
																double valorTarifa,
																String loginUsuario,
																double porcentajeIva,
																int tipoLiquidacionOriginal,																
																double valorTarifaOriginal,
																String liquidarAsocios,
																String liquidarAsociosOriginal,
																String fechaVigencia)
	{
	    
		logger.info("\n\nGRUPO >>>>>>>>>"+grupo);
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			modificar.setInt(1, codigoEsquemaTarifario);
			if(grupo<0)
				modificar.setNull(2,Types.DOUBLE);
			else
				modificar.setDouble(2, grupo);
			if(valorTarifa<0)
				modificar.setNull(3,Types.DOUBLE);
			else
				modificar.setDouble(3, valorTarifa);
			modificar.setDouble(4, porcentajeIva);
			if( codigoTipoLiquidacion <= 0 ) // No definido
			{
				modificar.setString(5, null);
			}
			else
			{
				modificar.setInt(5, codigoTipoLiquidacion);
			}
			modificar.setInt(6, codigoServicio);
			
			logger.info("liquidarAsocios------------->"+liquidarAsocios);
			if(UtilidadTexto.isEmpty(liquidarAsocios))
				modificar.setNull(7, Types.CHAR);
			else
				modificar.setString(7, liquidarAsocios);
			modificar.setObject(8, loginUsuario);
			modificar.setObject(9, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			modificar.setObject(10, UtilidadFecha.getHoraActual());
			
			logger.info("\n\nFECHA VIGENCIAAAAAA>>>>>>>> "+fechaVigencia+" >>CodTarifa>> "+codigoTarifa);
			if(!fechaVigencia.equals(""))
				modificar.setString(11, UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
			else
				modificar.setNull(11, Types.DATE);
			
			modificar.setInt(12, codigoTarifa);
								
			int update = modificar.executeUpdate();
					
			if( update == 0 )
				return new ResultadoBoolean(false);
			
			if(update>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa, liquidarAsocios, tipoLiquidacionOriginal, valorTarifaOriginal, liquidarAsociosOriginal, loginUsuario, fechaVigencia);
			}
			
			if(update>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
			
		}
		catch(SQLException e)
		{
			logger.warn("No se modificó la  tarifa soat"+e);
			return new ResultadoBoolean(false, "No se modificó la tarifa soat\n"+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Modifica una tarifa soat dado su código dentro de una transacción dado el estado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa, int, código de la tarifa a modificar
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param estado. String, estado dentro de la transacción
	 * @param Liquidar Asocio
	 * @param Liquidar Asocios Original
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#modificarTransaccional(java.sql.Connection, int, int, int, int, int, double, double, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
																						int codigoTarifa,
																						int codigoEsquemaTarifario,
																						int codigoServicio,
																						double grupo,
																						int codigoTipoLiquidacion,
																						double valorTarifa,
																						String loginUsuario,
																						double porcentajeIva,
																						int tipoLiquidacionOriginal,
																						double valorTarifaOriginal,																						
																						String estado,
																						String liquidarAsocios,
																						String liquidarAsociosOriginal,
																						String fechaVigencia) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resultado;
		
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resultado = new ResultadoBoolean (myFactory.beginTransaction(con));
			if( !resultado.isTrue() )
			{
				return resultado;			
			}
		}
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			modificar.setInt(1, codigoEsquemaTarifario);
			if(grupo<=0)
				modificar.setNull(2,Types.DOUBLE);
			else
				modificar.setDouble(2, grupo);
			if(valorTarifa<0)
				modificar.setNull(3,Types.DOUBLE);
			else
				modificar.setDouble(3, valorTarifa);
			modificar.setDouble(4, porcentajeIva);
			modificar.setInt(6, codigoServicio);
			modificar.setObject(7,liquidarAsocios);
			modificar.setObject(8,loginUsuario);
			modificar.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			modificar.setObject(10,UtilidadFecha.getHoraActual());
			if(!fechaVigencia.equals(""))
				modificar.setString(11, UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
			else
				modificar.setNull(11, Types.DATE);
			modificar.setInt(12, codigoTarifa);
			
			
			if( codigoTipoLiquidacion <= 0 ) // No definido
			{
				modificar.setString(5, null);
			}
			else
			{
				modificar.setInt(5, codigoTipoLiquidacion);
			}
			
			int update = modificar.executeUpdate();
					
			if( update == 0 )
			{
				myFactory.abortTransaction(con);
				return new ResultadoBoolean(false);
			}
			
			if(update>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa,liquidarAsocios, tipoLiquidacionOriginal, valorTarifaOriginal, liquidarAsociosOriginal, loginUsuario, fechaVigencia);
			}
			
			if(update>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
		}
		catch(SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.warn("No se insertó la tarifa  soat"+e);
			return new ResultadoBoolean(false, "No se insertó la tarifa soat\n"+e);
		}		
		
		if( estado !=null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		} 
		return new ResultadoBoolean(true);

	}

	/**
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a eliminar
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#eliminar(java.sql.Connection, int)
	 */
	public static ResultadoBoolean eliminar(Connection con, int codigoTarifa)
	{
		try
		{			
			PreparedStatementDecorator eliminar =  new PreparedStatementDecorator(con.prepareStatement(eliminarTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			eliminar.setInt(1, codigoTarifa);			

			int update = eliminar.executeUpdate();
					
			if( update == 0 )
				return new ResultadoBoolean(false);
		}
		catch(SQLException e)
		{
			logger.warn("No se eliminó la tarifa soat"+e);
			return new ResultadoBoolean(false, "No se eliminó la tarifa soat\n"+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Consulta una tarifa SOAT dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa a consultar
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int)
	 */
	public static ResultadoCollectionDB consultar(Connection con, int codigoTarifa)
	{
		ResultSetDecorator resultado = null;
		try
		{
			PreparedStatementDecorator consultaTarifaSttmnt =  new PreparedStatementDecorator(con.prepareStatement(consultaPorCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			consultaTarifaSttmnt.setInt(1, codigoTarifa);

			resultado = new ResultSetDecorator(consultaTarifaSttmnt.executeQuery());
			
		}
		catch( SQLException sqe )
		{
			logger.warn("No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
			return new ResultadoCollectionDB(false, "No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
		}		
		
		try
		{
			Collection col = UtilidadBD.resultSet2Collection(resultado);
			if( col != null && !col.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", col);
			}
			else
			{
				return new ResultadoCollectionDB(false);
			}
		}
		catch(Exception e)
		{
			logger.warn(" Error en Consultar tarifa soat al convertir el result set a una colección\n"+e.getMessage());
			return new ResultadoCollectionDB(false, " Error en Consultar tarifa soat al convertir el result set a una colección\n"+e.getMessage());
		}
	}

	/**
	 * Consulta una tarifa SOAT dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 */
	public static ResultadoCollectionDB consultar(	Connection con, 
																				int codigoEsquemaTarifario,
																				int codigoServicio	)
	{
		ResultSetDecorator resultado = null;
		try
		{
			PreparedStatementDecorator consultaTarifaSttmt =  new PreparedStatementDecorator(con.prepareStatement(consultaTarifaPorETyS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTarifaSttmt.setInt(1, codigoEsquemaTarifario);
			consultaTarifaSttmt.setInt(2, codigoServicio);
			
			resultado = new ResultSetDecorator(consultaTarifaSttmt.executeQuery());			
		}
		catch( SQLException sqe )
		{
			logger.warn("No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
			return new ResultadoCollectionDB(false, "No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
		}
		
		try
		{
			Collection col = UtilidadBD.resultSet2Collection(resultado);
			if( col != null && !col.isEmpty() )
			{
				return new ResultadoCollectionDB(true, "", col);
			}
			else
			{
				logger.warn(" No retorno ningún resultado");
				return new ResultadoCollectionDB(false);
			}
		}
		catch(Exception e)
		{
			logger.warn(" Error en Consultar Tarifa al convertir el result set a una colección\n"+e.getMessage());
			return new ResultadoCollectionDB(false, " Error en Consultar Tarifa al convertir el result set a una colección\n"+e.getMessage());
		}		
	}

	/**
	 * Dice si existe o no una tarifa dada por el esquema tarifario y el codigo del servicio asociado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si existe la tarifa, false y sin descripción si no existe o false y con descripción
	 * si se presento un problema en la consulta
	 */
	public static ResultadoBoolean existeTarifaDefinida( 	Connection con, 
																							int codigoEsquemaTarifario,
																							int codigoServicio	)
	{
		try
		{
			PreparedStatementDecorator existeTarifaSttmnt =  new PreparedStatementDecorator(con.prepareStatement(consultaExisteTarifaDefinida,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeTarifaSttmnt.setInt(1, codigoEsquemaTarifario);
			existeTarifaSttmnt.setInt(2, codigoServicio);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeTarifaSttmnt.executeQuery());
			
			if( resultado.next() )
			{
				int numFilas = resultado.getInt("numTarifasDefinidas");
				
				if( numFilas <= 0 )
					return new ResultadoBoolean(false);
				else
					return new ResultadoBoolean(true);
			}
			else
			{
				logger.warn("La consulta de si existe tarifa definida no retorno ningun resultado");
				return new ResultadoBoolean(false, "La consulta de si existe tarifa definida no retorno ningun resultado");
			}			
		}
		catch( SQLException sqe )
		{
			logger.warn("No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
			return new ResultadoBoolean(false, "No se pudo realizar la consulta, problemas con la bd "+sqe.getMessage());
		}
	}
	/**
	 * Busqueda avanzada para tarifas SOAT
	 * @param con
	 * @param codigoTarifa
	 * @param buscarPorCodigoTarifa
	 * @param codigoEsquemaTarifario
	 * @param buscarPorCodigoEsquemaTarifario
	 * @param codigoServicio
	 * @param buscarPorCodigoServicio
	 * @param grupo
	 * @param buscarPorGrupo
	 * @param codigoTipoLiquidacion
	 * @param buscarPorCodigoTipoLiquidacion
	 * @param valorTarifa
	 * @param buscarPorValorTarifa
	 * @param porcentajeIva
	 * @param buscarPorPorcentajeIva
	 * @param liquidacion Servicio
	 * @param buscarPorLiquidacionServicio
	 * @return ResultadoCollectionDB
	 * @throws SQLException
	 */
	public static ResultadoCollectionDB consultar(Connection con,
	int codigoTarifa,boolean buscarPorCodigoTarifa,
	int codigoEsquemaTarifario,boolean buscarPorCodigoEsquemaTarifario,
	String codigoServicio,boolean buscarPorCodigoServicio,
	double grupo,boolean buscarPorGrupo,
	int codigoTipoLiquidacion,boolean buscarPorCodigoTipoLiquidacion,
	double valorTarifa,boolean buscarPorValorTarifa,
	double porcentajeIva,boolean buscarPorPorcentajeIva,
	String nombreServicio, boolean buscarPorNombreServicio,
	String liquidacionServicio, boolean buscarPorLiquidacionServicio, String castDate) throws SQLException
	{
			
				boolean existeAsignacion=false;
				String buscarExcepcionesStr2=new String(buscarTarifaSOATStr);


				if(buscarPorCodigoTarifa){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.codigo="+codigoTarifa+" ");
					existeAsignacion=true;
				}
				if(buscarPorCodigoEsquemaTarifario){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.esquema_tarifario="+codigoEsquemaTarifario+" ");
					existeAsignacion=true;
				}		
				if(buscarPorCodigoServicio){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("rs.codigo_propietario = '"+codigoServicio+"' ");
					existeAsignacion=true;
				}						
				if(buscarPorGrupo){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.unidades="+grupo+" ");
					existeAsignacion=true;
				}
				if(buscarPorCodigoTipoLiquidacion){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.tipo_liquidacion="+codigoTipoLiquidacion+" ");
					existeAsignacion=true;
				}	
				if(buscarPorValorTarifa){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.valor_tarifa="+valorTarifa+" ");
					existeAsignacion=true;
				}			
				if(buscarPorPorcentajeIva){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.porcentaje_iva="+porcentajeIva+" ");
					existeAsignacion=true;
				}
				
				if(buscarPorNombreServicio){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" UPPER(rs.descripcion) LIKE UPPER('%"+nombreServicio+"%') ");
					existeAsignacion=true;
				}
				
				if(buscarPorLiquidacionServicio){
					if(existeAsignacion){
						buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" AND ");
					}
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("ts.liq_asocios = '"+liquidacionServicio+"' ");					
					existeAsignacion=true;
				}
		
				//aqui poner condiciones adicionales
				if(existeAsignacion){
					buscarExcepcionesStr2=buscarExcepcionesStr2.concat("AND ");
				}
				/*buscarExcepcionesStr2=buscarExcepcionesStr2.concat(" ts.servicio = s.codigo " +
																							"AND et.codigo = ts.esquema_tarifario " +
																							"AND rs.servicio = ts.servicio " +
																							"AND s.especialidad = e.codigo " +
																							"AND rs.tipo_tarifario =" +ConstantesBD.codigoTarifarioCups+
																							" ORDER BY codigoServicio ASC");
				*/
				buscarExcepcionesStr2 = buscarExcepcionesStr2.concat(" rs.tipo_tarifario =" +ConstantesBD.codigoTarifarioCups+
						" AND ((getobtenerfechavigenciasoat(ts.esquema_tarifario,ts.servicio) IS NOT NULL " +
								"AND ts.fecha_vigencia=(getobtenerfechavigenciasoat(ts.esquema_tarifario,ts.servicio)"+castDate+")) " +
									"OR (getobtenerfechavigenciasoat(ts.esquema_tarifario,ts.servicio) IS NULL AND ts.fecha_vigencia IS NULL)) " +
										" ORDER BY codigoServicio ASC");
				logger.info("Consulta Servicios SOAT===>"+buscarExcepcionesStr2);
				
				PreparedStatementDecorator buscarRecargosStmnt =  new PreparedStatementDecorator(con.prepareStatement(buscarExcepcionesStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				Collection listadoCollection = UtilidadBD.resultSet2Collection(new ResultSetDecorator(buscarRecargosStmnt.executeQuery()));
				return new ResultadoCollectionDB(true, "", listadoCollection);	
	
			}
	
	/**
	 * Metodo para consultar las fechas vigencia segun esquema tarifario y servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public static HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadenaCodigosServicios)
	{
		logger.info("\n\nCADENA CODIGOS SERVICIOS>>>>>>>>>>>>>"+cadenaCodigosServicios);
		HashMap<String, Object> resultados=new HashMap<String, Object>();
		String cadena="SELECT DISTINCT " +
							"ts.codigo AS codigo, " +
							"ts.esquema_tarifario AS codigoEsquemaTarifario, " +
							"et.nombre AS nombreEsquemaTarifario, " +
							"ts.unidades AS grupo, " +
							"ts.valor_tarifa AS valorTarifa, " +
							"ts.porcentaje_iva AS porcentajeIva, " +
							"ts.servicio AS codigoServicio, " +
							"rs.descripcion AS nombreServicio, " +
							"e.codigo AS codigoEspecialidad, " +
							"e.nombre AS nombreEspecialidad, " +
							"tls.codigo AS codigoTipoLiquidacion, " +
							"tls.nombre AS nombreTipoLiquidacion, " +
							"ts.liq_asocios AS liqAsocios, " +
							"ts.fecha_vigencia AS fechavigencia " +
						"FROM " +
							"tarifas_soat ts " +
							"INNER JOIN esquemas_tarifarios et ON (ts.esquema_tarifario = et.codigo) " +
							"INNER JOIN servicios s ON (ts.servicio = s.codigo) " +
							"INNER JOIN referencias_servicio rs ON (rs.servicio = s.codigo) " +
							"INNER JOIN especialidades e ON (s.especialidad = e.codigo) " +
							"LEFT OUTER JOIN tipos_liquidacion_soat tls ON (ts.tipo_liquidacion = tls.codigo ) "+
						"WHERE rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+" ";
		cadena+="AND ts.esquema_tarifario="+esquemaTarifario;
		if(!servicio.equals(""))
			cadena+=" AND ts.servicio="+servicio+" ORDER BY ts.fecha_vigencia DESC ";
		else
			cadena+=" AND ts.servicio IN ("+cadenaCodigosServicios+") GROUP BY ts.esquema_tarifario," +
						"ts.servicio," +
						"ts.codigo," +
						"et.nombre," +
						"ts.valor_tarifa," +
						"ts.porcentaje_iva," +
						"rs.descripcion," +
						"e.codigo," +
						"e.nombre," +
						"ts.tipo_liquidacion," +
						"tls.nombre," +
						"tls.codigo," +
						"ts.unidades," +
						"ts.liq_asocios," +
						"ts.fecha_vigencia ORDER BY ts.servicio, ts.fecha_vigencia DESC ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
		} 
		catch (SQLException e)
		{
			logger.info("\n\n ERROR. CONSULTANDO FECHAS VIGENCIA POR ESQUEMA SERVICIO>>>>>"+e);
		}
		logger.info("\n\nCONSULTA FECHAS VIGENCIA SQL >>>>>>>>>>"+cadena);
		return resultados;
	}

	/**
	 * Inserta una tarifa soat dentro de una transacción dado el estado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#insertarTransaccional(java.sql.Connection, int, int, int, int, double, double, java.lang.String)
	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
																					int codigoEsquemaTarifario,
																					int codigoServicio,
																					double grupo,
																					int codigoTipoLiquidacion,
																					double valorTarifa,
																					String loginUsuario,
																					double porcentajeIva,
																					int tipoLiquidacionOriginal,
																					double valorTarifaOriginal,
																					String estado,
																					String liquidarAsocio,
																					String liquidarAsocioOriginal,
																					String fechaVigencia,
																					String insertarStr) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resultado;
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resultado = new ResultadoBoolean(myFactory.beginTransaction(con));
			if( !resultado.isTrue() )
			{
				return resultado;
			}
		}
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			insertar.setInt(1, codigoEsquemaTarifario);
			if(grupo<0)
				insertar.setNull(2,Types.DOUBLE);
			else
				insertar.setDouble(2, grupo);
			if(valorTarifa<0)
				insertar.setNull(3,Types.DOUBLE);
			else
				insertar.setDouble(3, valorTarifa);
			insertar.setDouble(4, porcentajeIva);
			
			if( codigoTipoLiquidacion <= 0 )
				insertar.setString(5, null);
			else
				insertar.setInt(5, codigoTipoLiquidacion);
				
			insertar.setInt(6, codigoServicio);
			
			insertar.setObject(7, liquidarAsocio);
			
			insertar.setObject(8, loginUsuario);
			
			insertar.setObject(9, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			insertar.setObject(10, UtilidadFecha.getHoraActual());
			
			if(!fechaVigencia.equals(""))
				insertar.setString(11, UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
			else
				insertar.setNull(11, Types.DATE);
					
			int insert = insertar.executeUpdate();
					
			if( insert == 0 )
			{
				myFactory.abortTransaction(con);
				return new ResultadoBoolean(false);
				
			}
			if(insert>0)
			{
				insertoLog=logTarifasServicios(con, codigoEsquemaTarifario, codigoServicio, codigoTipoLiquidacion, valorTarifa,liquidarAsocio, tipoLiquidacionOriginal, valorTarifaOriginal, liquidarAsocioOriginal, loginUsuario, fechaVigencia);
			}
			
			if(insert>0 && insertoLog>0)
			{
				return new ResultadoBoolean(true);
			}
		}
		catch(SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.warn("No se insertó la tarifa soat "+e);
			return new ResultadoBoolean(false, "No se insertó la tarifa soat \n"+e);
		}		
		
		if( estado !=null && estado.equals(ConstantesBD.finTransaccion) )
		{
			myFactory.endTransaction(con);
		} 
		return new ResultadoBoolean(true);
	}

	
	/**
	 * Inserta una tarifa soat
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @param grupo. int, grupo de esta tarifa
	 * @param codigoTipoLiquidacion, int, código del tipo de liquidación aplicable a esta tarifa
	 * @param valorTarifa. double, valor de esta tarifa
	 * @param porcentajeIva. double, porcentaje del iva de esta tarifa
	 * @param liquidar Asocio
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#insertar(java.sql.Connection, int, int, int, int, double, double) 
	 */
	public static ResultadoBoolean insertar(	Connection con,
															int codigoEsquemaTarifario,
															int codigoServicio,
															double grupo,
															int codigoTipoLiquidacion,
															double valorTarifa,
															String loginUsuario,
															double porcentajeIva,
															int tipoLiquidacionoriginal,
															double valorTarifaOriginal,
															String liquidarAsocio,
															String liquidarAsocioOriginal,
															String fechaVigencia,
															String insertarStr) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    ResultadoBoolean resp=insertarTransaccional(	con, codigoEsquemaTarifario, codigoServicio, grupo, codigoTipoLiquidacion,  valorTarifa, loginUsuario, porcentajeIva, tipoLiquidacionoriginal, valorTarifaOriginal, ConstantesBD.inicioTransaccion, liquidarAsocio,liquidarAsocioOriginal, fechaVigencia, insertarStr);
	    if (resp.isTrue())
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
	 * Método para cargar una tarifa de SOAT dado el código del servicio
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param servicio Código del servicio 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return Coleccion con los datos
	 */
	public static Collection cargar(Connection con, int servicio, int esquemaTarifario, String fechaVigenciaOPCIONAL)
	{
		String cargarTarifaSOATStr="SELECT " +
										"codigo AS codigo, " +
										"coalesce(valor_tarifa,0) AS valor, " +
										"coalesce(porcentaje_iva,0) AS iva, " +
										"tipo_liquidacion as tipo_liquidacion," +
										"getnomtipoliquidacion(tipo_liquidacion) as nom_tipo_liquidacion, " +
										"unidades as grupo,  " +
										"getNombreServicioTarifa(servicio, "+ConstantesBD.codigoTarifarioSoat+") as nombre_servicio, " +
										"getNomEspecialidadServicio(servicio) as nombre_especialidad, " +
										"getCodigoEspecialidad(servicio) as codigo_especialidad, " +
										"liq_asocios " +
									"from " +
										"tarifas_soat " +
									"WHERE " +
										"servicio=? " +
										"AND esquema_tarifario=? ";
		
		
		if(UtilidadTexto.isEmpty(fechaVigenciaOPCIONAL))
		{
			try
			{
				PreparedStatementDecorator cargarStm =  new PreparedStatementDecorator(con.prepareStatement(cargarTarifaSOATStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStm.setInt(1, servicio);
				cargarStm.setInt(2, esquemaTarifario);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStm.executeQuery()));
			}
			catch(SQLException e)
			{
				logger.error("Error cargando la tarifa SOAT para el servicio "+servicio+": "+e);
				return null;
			}
		}
		else
		{
			String consultaArmada= "("+cargarTarifaSOATStr+" AND fecha_vigencia<=to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaVigenciaOPCIONAL)+"','yyyy-mm-dd') ) UNION ALL ("+cargarTarifaSOATStr+" AND fecha_vigencia IS NULL) ";
			
			logger.info("******************************************************************************************************");
			logger.info("TARIFAS --->"+consultaArmada+" --->SERV->"+servicio+" esquta->"+esquemaTarifario);
			logger.info("******************************************************************************************************");
			
			try
			{
				PreparedStatementDecorator cargarStm =  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStm.setInt(1, servicio);
				cargarStm.setInt(2, esquemaTarifario);
				cargarStm.setInt(3, servicio);
				cargarStm.setInt(4, esquemaTarifario);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStm.executeQuery()));
			}
			catch(SQLException e)
			{
				logger.error("Error cargando la tarifa de SOAT para el servicio "+servicio+": "+e);
				return null;
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @param tipoLiquidacion
	 * @param valorTarifa
	 * @param loginUsuario
	 * @return
	 */
	public static int logTarifasServicios(Connection con, int esquemaTarifario, int servicio, int tipoLiquidacion, double valorTarifa, String liquidarAsocios, int tipoLiquidacionOriginal, double valorTarifaOriginal, String liquidarAsocioOriginal, String loginUsuario, String fechaVigencia)
	{
		try{			
			PreparedStatementDecorator insert1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaLogStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insert1.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_log_tarifas_servicios"));
			insert1.setInt(2, esquemaTarifario);
			insert1.setInt(3, servicio);
			insert1.setInt(4, tipoLiquidacion);
			insert1.setDouble(5, valorTarifa);
			insert1.setObject(6,liquidarAsocios);
			insert1.setInt(7, tipoLiquidacionOriginal);
			insert1.setDouble(8, valorTarifaOriginal);
			
			if(!liquidarAsocioOriginal.equals(""))
				insert1.setObject(9, liquidarAsocioOriginal);
			else
				insert1.setString(9,ConstantesBD.acronimoNo);
			
			insert1.setString(10, loginUsuario);
			
			if(!fechaVigencia.equals(""))
				insert1.setString(11, UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
			else
				insert1.setNull(11, Types.DATE);
			
			return insert1.executeUpdate();
		
		}
		catch(SQLException e)
		{
			logger.error("Error insertando la tarifa de servicios: "+e);
			return 0;
		}
	}
	
	/**
	 * Metodo que consulta todas las tarifas por esquema tarifario y servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public static HashMap consultarTodasTarifas(Connection con, int esquemaTarifario, String codServicio, String cadenaConsultaTarifas)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst;
		 
		logger.info("\n\n cadenaConsultaTarifas--->"+cadenaConsultaTarifas+" esquTar-->"+esquemaTarifario+" serv->"+codServicio);
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaTarifas, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, esquemaTarifario);
			pst.setString(2, codServicio);
			pst.setInt(3, esquemaTarifario);
			pst.setString(4, codServicio);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.error("\n\n Error en consulta de Todas Tarifas >>>>>"+e);
		}
		return resultados;
	}
	
}
