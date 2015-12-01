/*
 * Aug 23, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dao.sqlbase.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.Articulo;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class SqlBaseAnulacionCargosFarmaciaDao 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseAnulacionCargosFarmaciaDao.class);
	
	/**
	 * 
	 */
	private static String consultaSolicitudes="SELECT " +
														" s.numero_solicitud as numerosolicitud," +
														" s.tipo as tiposolicitud," +
														" getnomtiposolicitud(s.tipo) as nomtiposolicitud," +
														" s.consecutivo_ordenes_medicas as consecutivosolicitud," +
														" to_char(s.fecha_solicitud,'dd/mm/yyyy')||' '||substr(s.hora_solicitud,0,6) as fechahorasolicitud," +
														" s.centro_costo_solicitado as codigoccsolicitado, " +
														" cc.nombre as nomccsolicitado ," +
														" getnomcentroatencion(cc.centro_atencion) as nomcentroatencion, " +
														" getnombreviaingreso(getviaingresocuenta(s.cuenta)) as nomviaingreso " +
											" from solicitudes s " +
											" inner join despacho d on(d.numero_solicitud=s.numero_solicitud) " +
											" inner join det_cargos dc on(dc.solicitud=d.numero_solicitud) " +
											" inner join centros_costo cc on(cc.codigo=s.centro_costo_solicitado) " +
											" where d.es_directo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and dc.sub_cuenta=?  and dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
											" group by s.cuenta,cc.nombre,cc.centro_atencion,s.numero_solicitud,s.tipo,s.consecutivo_ordenes_medicas,s.fecha_solicitud,s.hora_solicitud,s.centro_costo_solicitado order by s.fecha_solicitud,s.hora_solicitud ";

	
	
	/**
	 * 
	 */
	private static String consultaSolicitudesOrden="SELECT " +
														" s.numero_solicitud as numerosolicitud," +
														" s.tipo as tiposolicitud," +
														" getnomtiposolicitud(s.tipo) as nomtiposolicitud," +
														" s.consecutivo_ordenes_medicas as consecutivosolicitud," +
														" to_char(s.fecha_solicitud,'dd/mm/yyyy')||' '||substr(s.hora_solicitud,0,6) as fechahorasolicitud," +
														" s.centro_costo_solicitado as codigoccsolicitado, " +
														" cc.nombre as nomccsolicitado ," +
														" getnomcentroatencion(cc.centro_atencion) as nomcentroatencion, " +
														" getnombreviaingreso(getviaingresocuenta(s.cuenta)) as nomviaingreso,orden " +
											" from solicitudes s " +
											" inner join despacho d on(d.numero_solicitud=s.numero_solicitud) " +
											" inner join det_cargos dc on(dc.solicitud=d.numero_solicitud) " +
											" inner join centros_costo cc on(cc.codigo=s.centro_costo_solicitado) " +
											" where d.es_directo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and dc.sub_cuenta=?  and dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
											" group by s.cuenta,cc.nombre,cc.centro_atencion,s.numero_solicitud,s.tipo,s.consecutivo_ordenes_medicas,s.fecha_solicitud,s.hora_solicitud,s.centro_costo_solicitado,orden order by s.fecha_solicitud,s.hora_solicitud ";
	
	/**
	 * 
	 */
	private static String consultaDetalleSolicitudes="SELECT " +
																" dt.codigo_detalle_cargo as codigocargo," +
																" dt.cod_sol_subcuenta as codigosolsubcuenta," +
																" dt.articulo as codigoarticulo," +
																" dt.articulo||' '||getdescripcionarticulo(dt.articulo) as articulo " +
																" ,lote, case when  soli.ESTADO_HISTORIA_CLINICA=7   then dt.cantidad_cargada else dd.CANTIDAD end AS cantidadcargada  , dt.cantidad_cargada as flagTipoDespacho,orden " +
													" from det_cargos dt join despacho d on (dt.solicitud=d. numero_solicitud) JOIN detalle_despachos dd  ON (d.orden = dd.despacho  and dt.articulo = dd.articulo  ) join solicitudes soli on (dt.solicitud=soli.NUMERO_SOLICITUD)   " +
													" where dt.facturado = '"+ConstantesBD.acronimoNo+"' and dt.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
													" dt.solicitud=? and" +
													" dt.sub_cuenta=? and " +
													" dt.eliminado='"+ConstantesBD.acronimoNo+"' and " +
													" (getNumRespSolArtinclufac(dt.solicitud,dt.articulo,'"+ConstantesBD.acronimoSi+"')=1 or dt.tipo_distribucion='"+ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad+"')  " +
													" and dt.estado in  ("+ConstantesBD.codigoEstadoFCargada+" , "+ConstantesBD.codigoEstadoFPendiente+") " +
													" AND es_directo = ? ";

	/**
	 * 
	 */
	private static String cadenaInsertAnulacionCargosMaestro="INSERT INTO anulacion_cargos_farmacia(" +
																			" codigo," +
																			" sub_cuenta," +
																			" numero_solicitud," +
																			" motivo_anulacion," +
																			" fecha_modifica," +
																			" hora_modificar," +
																			" usuario_modifica," +
																			" consecutivo_anulacion) " +
																	" values(" +
																	"?,?,?,?," +
																	"current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * 
	 */
	private static String cadenaInsertDetAnulacionCargosMaestro="INSERT INTO det_anul_cargos_farmacia(" +
																			" codigo_anulacion," +
																			" numero_solicitud," +
																			" articulo," +
																			" cantidad_anulada) " +
																	" values(?,?,?,?)";
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> consultarSolicitudes(Connection con, String subCuenta) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} 
		catch (SQLException e) 
		{
			logger.error("Error ejecutando la consulta de solicitudes ");
			e.printStackTrace();
		}
		return mapa;
	}

	
	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap<String, Object> consultarSolicitudesDetalle(Connection con, String subCuenta) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaSolicitudesOrden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(subCuenta));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} 
		catch (SQLException e) 
		{
			logger.error("Error ejecutando la consulta de solicitudes ");
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> consultarDetalleSolicitudes(Connection con, String numeroSolicitud, String subCuenta,HashMap<String, Object> orden) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(numeroSolicitud));
			ps.setLong(2, Utilidades.convertirALong(subCuenta));
			ps.setBoolean(3,true);
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} 
		catch (SQLException e) 
		{
			logger.error("Error ejecutando la consulta de detalle solicitud ",e);
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param motivoAnulacion
	 * @param detalleSolicitudes
	 * @param usuario 
	 * @param codigoCentroCosto 
	 * @return
	 */
	public static boolean guardarAnulacion(Connection con, String subCuenta, String numeroSolicitud, String motivoAnulacion, HashMap<String, Object> detalleSolicitudes, String usuario, String codigoCentroCosto,int institucion,String estado,HashMap<String, Object> detalleSolicitudesOrdenes) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		if(transaccion)
		{
			try 
			{

				for(int a=0;a<Utilidades.convertirAEntero(detalleSolicitudes.get("numRegistros")+"");a++)
				{
					String codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_anul_cargos_farmacia")+"";
					String consecutivoAnulacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_anul_cargos_farmacia")+"";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertAnulacionCargosMaestro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

					ps.setLong(1, Utilidades.convertirALong(codigo));
					ps.setLong(2, Utilidades.convertirALong(subCuenta));
					ps.setInt(3, Utilidades.convertirAEntero(numeroSolicitud));
					ps.setString(4, motivoAnulacion);
					ps.setString(5, usuario);
					ps.setLong(6, Utilidades.convertirALong(consecutivoAnulacion));
					transaccion=ps.executeUpdate()>0;
					if(transaccion)
					{

						if(Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+"")>0)
						{
							if(transaccion)
							{
								ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertDetAnulacionCargosMaestro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

								ps.setDouble(1, Utilidades.convertirADouble(codigo));
								ps.setInt(2, Utilidades.convertirAEntero(numeroSolicitud));
								ps.setInt(3, Utilidades.convertirAEntero(detalleSolicitudes.get("codigoarticulo_"+a).toString()));
								ps.setInt(4, Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a).toString()));
								transaccion=ps.executeUpdate()>0;
								if(transaccion)
								{
									transaccion=generarDistribucionCargo(con,detalleSolicitudes,a,usuario);
								}

								if(transaccion&&UtilidadInventarios.afectaInventariosSolicitud(con, numeroSolicitud))
								{
									String cadenaTemp="SELECT count(1) from despacho where numero_solicitud ="+numeroSolicitud;
									ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									rs=new ResultSetDecorator(ps.executeQuery());
									if(rs.next())
									{
										int numDes=rs.getInt(1);//numero de despachos
										if(numDes==0)
										{
											int codArticulo=Utilidades.convertirAEntero(detalleSolicitudes.get("codigoarticulo_"+a)+"");
											if(Articulo.articuloManejaLote(con, codArticulo, institucion))
											{
												cadenaTemp="SELECT coalesce(lote,'') as lote,coalesce(fecha_vencimiento||'','') as fechavencimiento from detalle_despachos dd inner join despacho d on (d.orden=dd.despacho) where numero_solicitud = "+numeroSolicitud;
												ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
												rs=new ResultSetDecorator(ps.executeQuery());
												if(rs.next())
												{	
													String lote=rs.getString(1);
													String fechaVencimiento=rs.getString(2);
													transaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codArticulo,Utilidades.convertirAEntero(codigoCentroCosto),true,Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+""),institucion,ConstantesBD.continuarTransaccion,lote,fechaVencimiento);
												}
												else
												{
													logger.error("NO SE ENCONTRÓ EL LOTE Y FECHA DE VENCIMIENTO DEL ARTICULO");
													transaccion=false;
												}
											}
											else
												transaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codArticulo,Utilidades.convertirAEntero(codigoCentroCosto),true,Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+""),institucion,ConstantesBD.continuarTransaccion);

										}
										else
										{
											int codArticulo=Utilidades.convertirAEntero(detalleSolicitudes.get("codigoarticulo_"+a)+"");
											if(Articulo.articuloManejaLote(con, codArticulo, institucion)){

												if(estado.equals("guardar2")){

													transaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccionalAnulacion(con,codArticulo,Utilidades.convertirAEntero(codigoCentroCosto),true,Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+""),institucion,ConstantesBD.continuarTransaccion,"","",numeroSolicitud,String.valueOf(detalleSolicitudes.get("lote_"+a)),detalleSolicitudes,a);

												}else{
													transaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,codArticulo,Utilidades.convertirAEntero(codigoCentroCosto),true,Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+""),institucion,ConstantesBD.continuarTransaccion,"","");
												}
											}
											else{
												transaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,codArticulo,Utilidades.convertirAEntero(codigoCentroCosto),true,Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+a)+""),institucion,ConstantesBD.continuarTransaccion);
											}
										}
									}
								}
							}
							else
							{
								a=Utilidades.convertirAEntero(detalleSolicitudes.get("numRegistros")+"");
							}
						}
					}
				}

			} 
			catch (SQLException e) 
			{
				transaccion=false;
				logger.error("Error INSERTANDO EL MAESTRO DE LA ANULACION ");
				e.printStackTrace();
			}
		}
		if(transaccion)
			UtilidadBD.finalizarTransaccion(con);
		else 
			UtilidadBD.abortarTransaccion(con);
		return transaccion;
	}
	
	

	

	/**
	 * 
	 * @param con
	 * @param detalleSolicitudes
	 * @param indice 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private static boolean generarDistribucionCargo(Connection con, HashMap<String, Object> detalleSolicitudes, int indice, String usuario)  
	{
		try 
		{
		
			PreparedStatementDecorator ps=null;
			
			String codSolSub=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_sol_sub_cuentas")+"";
			String cadena1="INSERT INTO solicitudes_subcuenta " +
							" (codigo,solicitud,sub_cuenta,servicio,articulo,porcentaje,cantidad,monto,cubierto,cuenta,tipo_solicitud,paquetizada,sol_subcuenta_padre,servicio_cx,tipo_asocio,tipo_distribucion,fecha_modifica,hora_modifica,usuario_modifica) " +
					" (select " +
							codSolSub+"," +
							" solicitud," +
							" sub_cuenta," +
							" servicio," +
							" articulo," +
							" porcentaje," +
							detalleSolicitudes.get("cantidadanular_"+indice)+"," +
							" monto," +
							" cubierto," +
							" cuenta," +
							" tipo_solicitud," +
							" paquetizada," +
							" null," +
							" servicio_cx," +
							" tipo_asocio," +
							" tipo_distribucion," +
							" current_date," +
							" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
							"'"+usuario+"'" +
						" from solicitudes_subcuenta " +
						" where codigo="+detalleSolicitudes.get("codigosolsubcuenta_"+indice)+" AND eliminado='"+ConstantesBD.acronimoNo+"' " +
					")";
	
			String cadena2=" INSERT INTO det_cargos " +
					" (codigo_detalle_cargo,sub_cuenta,convenio,esquema_tarifario,cantidad_cargada,valor_unitario_tarifa,valor_unitario_cargado,valor_total_cargado,porcentaje_cargado,porcentaje_recargo,valor_unitario_recargo,porcentaje_dcto,valor_unitario_dcto,valor_unitario_iva,nro_autorizacion,estado,cubierto,tipo_distribucion,solicitud,servicio,articulo,servicio_cx,tipo_asocio,facturado,tipo_solicitud,paquetizado,cargo_padre,usuario_modifica,fecha_modifica,hora_modifica,cod_sol_subcuenta,observaciones,requiere_autorizacion,contrato) " +
					" (select " +
					UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_cargos")+"," +
					" sub_cuenta," +
					" convenio," +
					" esquema_tarifario," +
					detalleSolicitudes.get("cantidadanular_"+indice)+"," +
					" valor_unitario_tarifa," +
					" valor_unitario_cargado," +
					detalleSolicitudes.get("cantidadanular_"+indice)+"*valor_unitario_cargado," +
					" porcentaje_cargado," +
					" porcentaje_recargo," +
					" valor_unitario_recargo," +
					" porcentaje_dcto," +
					" valor_unitario_dcto, "+
					" valor_unitario_iva," +
					" nro_autorizacion," +
					ConstantesBD.codigoEstadoFAnulada+ "," +
					" cubierto," +
					" tipo_distribucion," +
					" solicitud," +
					" servicio," +
					" articulo," +
					" servicio_cx," +
					" tipo_asocio," +
					" facturado," +
					" tipo_solicitud," +
					" paquetizado," +
					" null," +
					"'"+usuario+"'," +
					" current_date," +
					" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
					codSolSub+"," +
					" observaciones," +
					" requiere_autorizacion," +
					" contrato " +
					" from det_cargos " +
					" where codigo_detalle_cargo="+detalleSolicitudes.get("codigocargo_"+indice)+" AND eliminado='"+ConstantesBD.acronimoNo+"' "+
					" )";
			
			
					String cadena="update det_cargos set cantidad_cargada=cantidad_cargada-"+detalleSolicitudes.get("cantidadanular_"+indice)+", valor_total_cargado= valor_unitario_cargado * (cantidad_cargada-"+detalleSolicitudes.get("cantidadanular_"+indice)+")  where codigo_detalle_cargo="+detalleSolicitudes.get("codigocargo_"+indice);
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					int resTempo=ps.executeUpdate();
					
					if((Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadcargada_"+indice)+"")-Utilidades.convertirAEntero(detalleSolicitudes.get("cantidadanular_"+indice)+""))>0)
					{
						if(resTempo>0)
						{
							ps= new PreparedStatementDecorator(con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							resTempo=ps.executeUpdate();
							
							if(resTempo>0)
							{
								ps= new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								resTempo=ps.executeUpdate();
							}
						}
					}
					return resTempo>0;
		} 
		catch (SQLException e) 
		{
			logger.error("Error INSERTANDO EL MAESTRO DE LA ANULACION ");
			e.printStackTrace();
		}
		return false;
	}

}
