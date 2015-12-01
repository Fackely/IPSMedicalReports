package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.IPSException;

public class SqlBaseCopiarTarifasEsquemaTarifarioDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseCopiarTarifasEsquemaTarifarioDao.class);
	
	/**
	 * 
	 */
	public static final String consultaTarifario="SELECT codigo,nombre,tarifario_oficial as tarifariooficial,metodo_ajuste as metodoajuste,es_inventario as esinventario,cantidad,activo FROM esquemas_tarifarios where codigo=? and institucion=?";
	
	
	/**
	 * 
	 */
	public static final String consultaTarifasInventarios="(" +
															"SELECT " +
																"codigo, " +
																"esquema_tarifario as esquematarifario, " +
																"articulo, " +
																"valor_tarifa as valortarifa, " +
																"porcentaje_iva as porcentajeiva, " +
																"tipo_tarifa as tipotarifa, " +
																"porcentaje, " +
																"actualiz_automatic as actualizautomatic, " +
																"usuario_modifica as usuariomodifica, " +
																"fecha_modifica as fechamodifica, " +
																"hora_modifica as horamodifica, " +
																"fecha_vigencia||'' as fechavigencia " +
															"FROM " +
																"tarifas_inventario " +
															"where " +
																"esquema_tarifario=? " +
																"AND fecha_vigencia<=? " +
																//"order by fecha_vigencia desc " +
															")" +
															"UNION ALL " +
															"(" +
															"SELECT " +
																"codigo, " +
																"esquema_tarifario as esquematarifario, " +
																"articulo, " +
																"valor_tarifa as valortarifa, " +
																"porcentaje_iva as porcentajeiva, " +
																"tipo_tarifa as tipotarifa, " +
																"porcentaje, " +
																"actualiz_automatic as actualizautomatic, " +
																"usuario_modifica as usuariomodifica, " +
																"fecha_modifica as fechamodifica, " +
																"hora_modifica as horamodifica, " +
																"'' as fechavigencia " +
															"FROM " +
																"tarifas_inventario " +
															"where " +
																"esquema_tarifario=? " +
																"AND fecha_vigencia is null " +
																//"order by fecha_vigencia desc " +
															")" ;
	
	
	/**
	 * 
	 */
	public static final String consultaTarifasIss="(" +
													"SELECT " +
														"codigo as codigoiss, " +
														"esquema_tarifario as esquematarifario, " +
														"servicio, " +
														"valor_tarifa as valortarifa, " +
														"porcentaje_iva as porcentajeiva, " +
														"tipo_liquidacion as tipoliquidacion, " +
														"unidades, " +
														"actualiza_automatica as actualizaautomatica, " +
														"liq_asocios as liqasocios, " +
														"fecha_vigencia||'' as fechavigencia " +
													"FROM " +
														"tarifas_iss  " +
													"where " +
														"esquema_tarifario=? " +
														"AND fecha_vigencia<=? " +
														//"order by fecha_vigencia desc" +
													") " +
													"UNION ALL" +
													"(" +
													"SELECT " +
														"codigo as codigoiss, " +
														"esquema_tarifario as esquematarifario, " +
														"servicio, " +
														"valor_tarifa as valortarifa, " +
														"porcentaje_iva as porcentajeiva, " +
														"tipo_liquidacion as tipoliquidacion, " +
														"unidades, " +
														"actualiza_automatica as actualizaautomatica, " +
														"liq_asocios as liqasocios, " +
														"'' as fechavigencia " +
													"FROM " +
														"tarifas_iss  " +
													"where " +
														"esquema_tarifario=? " +
														"AND fecha_vigencia IS NULL " +
														//"order by fecha_vigencia desc" +
													") " ;
	
	
	/**
	 * 
	 */
	public static final String consultaTarifasSoat="(" +
													"SELECT " +
														"codigo as codigosoat, " +
														"esquema_tarifario as esquematarifario, " +
														"servicio, " +
														"valor_tarifa as valortarifa, " +
														"porcentaje_iva as porcentajeiva, " +
														"tipo_liquidacion as tipoliquidacion, " +
														"unidades, " +
														"actualiza_automatica as actualizaautomatica, " +
														"liq_asocios as liqasocios, " +
														"fecha_vigencia||'' as fechavigencia " +
													"FROM " +
														"tarifas_soat  " +
													"where " +
														"esquema_tarifario=? " +
														"AND fecha_vigencia<=? " +
														//"order by fecha_vigencia desc " +
													") " +
													"UNION ALL " +
													"(" +
													"SELECT " +
														"codigo as codigosoat, " +
														"esquema_tarifario as esquematarifario, " +
														"servicio, " +
														"valor_tarifa as valortarifa, " +
														"porcentaje_iva as porcentajeiva, " +
														"tipo_liquidacion as tipoliquidacion, " +
														"unidades, " +
														"actualiza_automatica as actualizaautomatica, " +
														"liq_asocios as liqasocios, " +
														"'' as fechavigencia " +
													"FROM " +
														"tarifas_soat  " +
													"where " +
														"esquema_tarifario=? " +
														"AND fecha_vigencia is null " +
														//"order by fecha_vigencia desc " +
													") " ;
	
	
	/**
	 * cadena para la insercion Tarifas Inventarios
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO tarifas_inventario (codigo, esquema_tarifario, articulo, valor_tarifa, porcentaje_iva, tipo_tarifa, porcentaje, actualiz_automatic, usuario_modifica, fecha_modifica, hora_modifica, fecha_vigencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	
	/**
	 * cadena para la insercion Tarifas Inventarios
	 */
	
	private static final String cadenaInsertarIssStr="INSERT INTO tarifas_iss (codigo, esquema_tarifario, valor_tarifa, porcentaje_iva, servicio, tipo_liquidacion, unidades, actualiza_automatica, liq_asocios, usuario_modifica, fecha_modifica, hora_modifica, fecha_vigencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * cadena para la insercion Tarifas Inventarios
	 */
	
	private static final String cadenaInsertarSoatStr="INSERT INTO tarifas_soat (codigo, esquema_tarifario, valor_tarifa, porcentaje_iva, servicio, tipo_liquidacion, unidades, actualiza_automatica, liq_asocios, usuario_modifica, fecha_modifica, hora_modifica, fecha_vigencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * 
	 * @param con
	 * @param tarifarioOrigen
	 * @param institucion 
	 * @return
	 */
	public static HashMap<String, Object> obtenerTarifario(Connection con, String tarifarioOrigen, String institucion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTarifario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(tarifarioOrigen));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	public static Object obtenerTarifario(Connection con, String tarifarioOrigen, String tarifarioDestino, String porcentaje, String chequeo) {
		
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param tarifarioOrigen
	 * @return
	 */
	public static HashMap<String, Object> consultarTarifas(Connection con, String tarifarioOrigen, String esinventario, String tarifarioOficial) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			if(UtilidadTexto.getBoolean(esinventario))
			{	
				logger.info("Pasa Inventario");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTarifasInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(tarifarioOrigen));
				ps.setDate(2, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				ps.setInt(3, Utilidades.convertirAEntero(tarifarioOrigen));
				mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
				ps.close();
			}
			else
			{
				if(tarifarioOficial.equals(ConstantesBD.codigoTarifarioISS+""))
				{
					logger.info("Pasa ISS");
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTarifasIss,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, Utilidades.convertirAEntero(tarifarioOrigen));
					ps.setDate(2, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
					ps.setInt(3, Utilidades.convertirAEntero(tarifarioOrigen));
					mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
					ps.close();
				}
				else if(tarifarioOficial.equals(ConstantesBD.codigoTarifarioSoat+""))
				{
					logger.info("Pasa Soat");
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTarifasSoat,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, Utilidades.convertirAEntero(tarifarioOrigen));
					ps.setDate(2, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
					ps.setInt(3, Utilidades.convertirAEntero(tarifarioOrigen));
					mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
					ps.close();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 */
	public static boolean insertarInventarioPorcentaje(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			double valorTarifa= Utilidades.convertirADouble(vo.get("valor_tarifa")+"");
			double porcentajeTarifa = Utilidades.convertirADouble(porcentaje);
			
			if(chequeo.equals("true")&&!porcentaje.equals(""))
			{
				double valortotal= valorTarifa+((valorTarifa*porcentajeTarifa)/100) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_tarifas_inventario"));
				
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo").toString()));
				
				ps.setDouble(4, valorfinal);
				
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				ps.setString(6, vo.get("tipo_tarifa").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("porcentaje").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiz_automatic")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiz_automatic").toString());
				
				ps.setString(9, vo.get("usuario_modifica").toString());
				
				ps.setDate(10, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(11, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(12, Types.DATE);
				else
					ps.setDate(12, Date.valueOf(vo.get("fechavigencia")+""));
				
			}	
			if(chequeo.equals("false")&&!porcentaje.equals(""))
			{
				double valortotal= valorTarifa-(valorTarifa*(porcentajeTarifa/100)) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_tarifas_inventario"));
				
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo").toString()));
				
				ps.setDouble(4, valorfinal);
				
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				ps.setString(6, vo.get("tipo_tarifa").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("porcentaje").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiz_automatic")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiz_automatic").toString());
				
				ps.setString(9, vo.get("usuario_modifica").toString());
				
				ps.setDate(10, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(11, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(12, Types.DATE);
				else
					ps.setDate(12, Date.valueOf(vo.get("fechavigencia")+""));
			
			}
			if((chequeo.equals("")&&porcentaje.equals(""))||chequeo.equals("")||porcentaje.equals(""))
			{
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_tarifas_inventario"));
				
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo").toString()));
				
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("valor_tarifa").toString()));
				
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				ps.setString(6, vo.get("tipo_tarifa").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("porcentaje").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiz_automatic")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiz_automatic").toString());
				
				ps.setString(9, vo.get("usuario_modifica").toString());
				
				ps.setObject(10, vo.get("fecha_modifica")+"");
				
				ps.setDate(11, Date.valueOf(vo.get("hora_modifica").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(12, Types.DATE);
				else
					ps.setDate(12, Date.valueOf(vo.get("fechavigencia")+""));
			}
			boolean retorna=ps.executeUpdate()>0;
			ps.close();
			return retorna; 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarInventario(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			/**
			 * INSERT INTO tarifas_inventario 
			 * (codigo, 
			 * esquema_tarifario, 
			 * articulo, 
			 * valor_tarifa, 
			 * porcentaje_iva, 
			 * tipo_tarifa, 
			 * porcentaje, 
			 * actualiz_automatic, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 */
			
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_tarifas_inventario"));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
			
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo").toString()));
			
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("valor_tarifa").toString()));
			
			ps.setDouble(5, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
			
			ps.setString(6, vo.get("tipo_tarifa").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("porcentaje").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("actualiz_automatic")+""))
				ps.setObject(8, null);
			else
				ps.setString(8, vo.get("actualiz_automatic").toString());
			
			ps.setString(9, vo.get("usuario_modifica").toString());
			
			ps.setDate(10, Date.valueOf(vo.get("fecha_modifica").toString()));
			
			ps.setString(11, vo.get("hora_modifica").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setNull(12, Types.DATE);
			else
				ps.setDate(12, Date.valueOf(vo.get("fechavigencia")+""));
			
			boolean retorna= ps.executeUpdate()>0;
			ps.close();
			return retorna;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @param tipoLiquidacion
	 * @return
	 */
	public static boolean insertarTarifasIss(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarIssStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tarifas_iss 
			 * (codigo, 
			 * esquema_tarifario, 
			 * valor_tarifa, 
			 * porcentaje_iva, 
			 * servicio, 
			 * tipo_liquidacion, 
			 * unidades, 
			 * actualiza_automatica, 
			 * liq_asocios, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 */
				
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"SEQ_TARIFAS_ISS"));
			
			if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("valor_tarifa").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("servicio").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
				ps.setObject(6, null);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
				ps.setObject(8, null);
			else
				ps.setString(8, vo.get("actualiza_automatica").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
				ps.setObject(9, null);
			else
				ps.setString(9, vo.get("liq_asocios").toString());
			
			ps.setString(10, vo.get("usuario_modifica").toString());
			
			ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
			
			ps.setString(12, vo.get("hora_modifica").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setNull(13, Types.DATE);
			else
				ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
			
			boolean retorna= ps.executeUpdate()>0;
			ps.close();
			return retorna; 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @param tipoLiquidacion
	 * @return
	 */
	public static boolean insertarTarifasSoat(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSoatStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tarifas_soat 
			 * (codigo, 
			 * esquema_tarifario, 
			 * valor_tarifa, 
			 * porcentaje_iva, 
			 * servicio, 
			 * tipo_liquidacion, 
			 * unidades, 
			 * actualiza_automatica, 
			 * liq_asocios, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 */
			
			
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_tarifas_soat"));
			
			if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("valor_tarifa").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("servicio").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
				ps.setObject(6, null);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
				ps.setObject(8, null);
			else
				ps.setString(8, vo.get("actualiza_automatica").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
				ps.setObject(9, null);
			else
				ps.setString(9, vo.get("liq_asocios").toString());
			
			ps.setString(10, vo.get("usuario_modifica").toString());
			
			ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
			
			ps.setString(12, vo.get("hora_modifica").toString());
				
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setNull(13, Types.DATE);
			else
				ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
			
			boolean retorna=ps.executeUpdate()>0;
			ps.close();
			return retorna;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 */
	public static boolean insertarTarifasIssValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException
	{
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarIssStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			double valorTarifa= Utilidades.convertirADouble(vo.get("valor_tarifa")+"");
			double porcentajeTarifa = Utilidades.convertirADouble(porcentaje);
			
			if(chequeo.equals("true")&&!porcentaje.equals(""))
			{	
				
				/**
				 * INSERT INTO tarifas_iss 
				 * (codigo, 
				 * esquema_tarifario, 
				 * valor_tarifa, 
				 * porcentaje_iva, 
				 * servicio, 
				 * tipo_liquidacion, 
				 * unidades, 
				 * actualiza_automatica, 
				 * liq_asocios, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				 */
				
				double valortotal= valorTarifa+((valorTarifa*porcentajeTarifa)/100) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"SEQ_TARIFAS_ISS"));
				
				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, valorfinal);
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setString(5, vo.get("servicio").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
			}	
			
			if(chequeo.equals("false")&&!porcentaje.equals(""))
			{
				
				double valortotal= valorTarifa-(valorTarifa*(porcentajeTarifa/100)) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"SEQ_TARIFAS_ISS"));
				
				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, valorfinal);
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setString(5, vo.get("servicio").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
					
				
			}
			if((chequeo.equals("")&&porcentaje.equals(""))||chequeo.equals("")||porcentaje.equals(""))
			{
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"SEQ_TARIFAS_ISS"));
				
				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, Utilidades.convertirADouble(vo.get("valor_tarifa").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setString(5, vo.get("servicio").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
			}
			boolean retorna=ps.executeUpdate()>0;
			ps.close();
			return retorna;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
		
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 */
	public static boolean insertarTarifasSoatValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException
	{
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSoatStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			double valorTarifa= Utilidades.convertirADouble(vo.get("valor_tarifa")+"");
			double porcentajeTarifa = Utilidades.convertirADouble(porcentaje);
				
			if(chequeo.equals("true")&&!porcentaje.equals(""))
			{
			
				double valortotal= valorTarifa+((valorTarifa*porcentajeTarifa)/100) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				/**
				 * INSERT INTO tarifas_soat 
				 * (codigo, 
				 * esquema_tarifario, 
				 * valor_tarifa, 
				 * porcentaje_iva, 
				 * servicio, 
				 * tipo_liquidacion, 
				 * unidades, 
				 * actualiza_automatica, 
				 * liq_asocios, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				 */
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_tarifas_soat"));

				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, valorfinal);
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setInt(5, Utilidades.convertirAEntero(vo.get("servicio").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
				
			}
			if(chequeo.equals("false")&&!porcentaje.equals(""))
			{
				
				double valortotal= valorTarifa-(valorTarifa*(porcentajeTarifa/100)) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_tarifas_soat"));

				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, valorfinal);
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setInt(5, Utilidades.convertirAEntero(vo.get("servicio").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
				
			}
			if((chequeo.equals("")&&porcentaje.equals(""))||chequeo.equals("")||porcentaje.equals(""))
			{
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_tarifas_soat"));

				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, Utilidades.convertirADouble(vo.get("valor_tarifa").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setInt(5, Utilidades.convertirAEntero(vo.get("servicio").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
			}
			boolean retorna=ps.executeUpdate()>0;
			ps.close();
			return retorna;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cantidadEsquema
	 * @param unidades
	 * @return
	 */
	public static boolean insertarTarifasIssUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarIssStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			double cantidadDestino= Utilidades.convertirADouble(cantidadEsquema);
			double unidadesOrigen = Utilidades.convertirADouble(unidades);
			
				
				double valortotal= unidadesOrigen*cantidadDestino ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"SEQ_TARIFAS_ISS"));
				
				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, valorfinal);
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setString(5, vo.get("servicio").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));

			boolean retorna= ps.executeUpdate()>0;
			ps.close();
			return retorna;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cantidadEsquema
	 * @param unidades
	 * @return
	 */
	public static boolean insertarTarifasSoatUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSoatStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			double cantidadDestino= Utilidades.convertirADouble(cantidadEsquema);
			double unidadesOrigen = Utilidades.convertirADouble(unidades);
			
			
				double valortotal= unidadesOrigen*(cantidadDestino/30) ;
				
				double valorfinal= UtilidadValidacion.aproximarMetodoAjuste(vo.get("metodo_ajuste")+"", valortotal);
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_tarifas_soat"));

				if(UtilidadTexto.isEmpty(vo.get("esquema_tarifario")+""))
					ps.setObject(2, null);
				else
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("valor_tarifa")+""))
					ps.setObject(3, null);
				else
					ps.setDouble(3, valorfinal);
				
				if(UtilidadTexto.isEmpty(vo.get("porcentaje_iva")+""))
					ps.setObject(4, null);
				else
					ps.setDouble(4, Utilidades.convertirADouble(vo.get("porcentaje_iva").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
					ps.setObject(5, null);
				else
					ps.setInt(5, Utilidades.convertirAEntero(vo.get("servicio").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("tipo_liquidacion")+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("tipo_liquidacion").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("unidades")+""))
					ps.setObject(7, null);
				else
					ps.setDouble(7, Utilidades.convertirADouble(vo.get("unidades").toString()));
				
				if(UtilidadTexto.isEmpty(vo.get("actualiza_automatica")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("actualiza_automatica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("liq_asocios")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("liq_asocios").toString());
				
				ps.setString(10, vo.get("usuario_modifica").toString());
				
				ps.setDate(11, Date.valueOf(vo.get("fecha_modifica").toString()));
				
				ps.setString(12, vo.get("hora_modifica").toString());
				
				if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
					ps.setNull(13, Types.DATE);
				else
					ps.setDate(13, Date.valueOf(vo.get("fechavigencia")+""));
		
				boolean retorna =ps.executeUpdate()>0;
				ps.close();
			return retorna;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean existeTarifaInventarios(Connection con, int codigoEsquematarifario, int codigoArticulo)
	{
		String consulta="SELECT codigo FROM tarifas_inventario where esquema_tarifario=? and articulo=? ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoEsquematarifario);
			ps.setInt(2, codigoArticulo);
			if(ps.executeQuery().next())
			{
				ps.close();
				return true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean existeTarifaServicios(Connection con, int codigoEsquematarifario, int codigoServicio, int codigoTarifario)
	{
		String tabla="";
		tabla=(codigoTarifario==ConstantesBD.codigoTarifarioISS)?"tarifas_iss":"tarifas_soat";
		String consulta="SELECT codigo FROM "+tabla+" where esquema_tarifario=? and servicio=? ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoEsquematarifario);
			ps.setInt(2, codigoServicio);
			if(ps.executeQuery().next())
			{	
				ps.close();
				return true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
