/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dao.sqlbase.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class SqlBasePaquetizacionDao 
{
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBasePaquetizacionDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsultaPaquetesAsociados="SELECT " +
																	" p1.codigo as codpaquetizacion," +
																	" p1.sub_cuenta as subcuenta," +
																	" p1.numero_solicitud_paquete as solicitud," +
																	" p1.codigo_paquete_convenio as codpaqueteconvenio, " +
																	" p2.convenio as convenio," +
																	" p2.contrato as contrato," +
																	" p2.institucion as institucion," +
																	" p2.paquete as codpaquete," +
																	" p2.tipo_paciente as tipopaciente, " +
																	" getDescripcionPaquete(p2.paquete,p2.institucion) as nompaquete," +
																	" via_ingreso as codviaingreso," +
																	" getnombreviaingreso(p2.via_ingreso) as nomviaingreso," +
																	" to_char(fecha_inicial_venc,'dd/mm/yyyy') as fechainicialven," +
																	" to_char(fecha_final_venc,'dd/mm/yyyy') as fechafinalven," +
																	" getCodigoServicioPaquete(p2.paquete,p2.institucion) as serviciopaquete," +
																	" getnombreservicio(getCodigoServicioPaquete(p2.paquete,p2.institucion),"+ConstantesBD.codigoTarifarioCups+") as nomserviciopaquete," +
																	" getNumRespSolServicio(p1.numero_solicitud_paquete,getCodigoServicioPaquete(p2.paquete,p2.institucion)) as numrespaquete," +
																	" getNumRespFacSolServicio(p1.numero_solicitud_paquete,getCodigoServicioPaquete(p2.paquete,p2.institucion)) as numresfacpaquete," +
																	" p1.centro_costo as centrocosto," +
																	" p1.centro_costo_solicita_filtro as ccsolicitafiltro," +
																	" p1.centro_costo_ejecuta_filtro as ccejecutafiltro," +
																	" to_char(fecha_incial_sol_filtro,'dd/mm/yyyy') as fechainisolfiltro," +
																	" to_char(fecha_final_sol_filtro,'dd/mm/yyyy') as fechafinsolfiltro," +
																	" case when getnumrespfacsolservicio(p1.numero_solicitud_paquete,getCodigoServicioPaquete(p2.paquete,p2.institucion)) > 0 then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as facturado," +
																	" 'BD' as tiporegistro " +
															" from paquetizacion p1 " +
															" inner join paquetes_convenio p2 on(p2.codigo=p1.codigo_paquete_convenio) " +
															" where sub_cuenta = ?";
	
	/**
	 * 
	 */
	private static String cadenaConsutalServicioPaquetes="SELECT " +
																	" dp.codigo as codigo," +
																	" dp.codigo_paquetizacion as codpaquetizacion," +
																	" dp.numero_solicitud as solicitud," +
																	" dp.servicio as servicio," +
																	" dp.articulo as articulo," +
																	" dp.cantidad as cantidad," +
																	" dp.servicio_cx as serviciocx," +
																	" dp.tipo_asocio as tipoasocio," +
																	" dp.principal as principal," +
																	" coalesce(dp.det_cx_honorarios,-1) as detcxhonorarios," +
																	" coalesce(dp.det_asocio_cx_salas_mat,-1) as detasicxsalasmat," +
																	" getnombretipoasocio(dp.tipo_asocio) as nomtipoasocio " +
														" from detalle_paquetizacion dp " +
														" where 1=1 ";
	
	/**
	 * 
	 */
	public static String cadenaInsertEncabezadoPaquetizacion="INSERT INTO paquetizacion(" +
																					" codigo," +
																					" sub_cuenta," +
																					" institucion," +
																					" numero_solicitud_paquete," +
																					" codigo_paquete_convenio," +
																					" servicio," +
																					" centro_costo," +
																					" centro_costo_solicita_filtro," +
																					" centro_costo_ejecuta_filtro," +
																					" fecha_incial_sol_filtro," +
																					" fecha_final_sol_filtro," +
																					" usuario_modifica," +
																					" fecha_modifica," +
																					" hora_modifica" +
																				" ) " +
																				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 
	 */
	private static String consultaLiquidacionPaquetesResponsables="SELECT " +
																			" pa.codigo_paquete as codigopaquete," +
																			" pa.descripcion as nombrepaquete, " +
																			" valor_total_cargado as valorpaquete," +
																			" (select sum(valor_total_cargado) from det_cargos where det_cargos.cargo_padre=dc.codigo_detalle_cargo and det_cargos.estado="+ConstantesBD.codigoEstadoFCargada+") as valorpaqueteasignado," +
																			" (select sum(valor_total_cargado) from det_cargos where det_cargos.cargo_padre is null and det_cargos.sub_cuenta=dc.sub_cuenta and det_cargos.tipo_solicitud <>"+ConstantesBD.codigoTipoSolicitudPaquetes+" and det_cargos.estado="+ConstantesBD.codigoEstadoFCargada+") as valorevento " +
																" from det_cargos dc  " +
																" inner join paquetizacion p on(dc.solicitud=p.numero_solicitud_paquete) " +
																" inner join paquetes_convenio pc on (pc.codigo=p.codigo_paquete_convenio) " +
																" inner join paquetes pa on(pa.codigo_paquete=pc.paquete and pa.institucion=pc.institucion) where dc.sub_cuenta=?";
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap consultarPaquetesAsocioadosResponsableSubcuenta(Connection con, String subCuenta) 
	{
		HashMap mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaPaquetesAsociados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			//si encontro resultado debe cargar los componentes de cada paquete.
			if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")>0)
			{
				for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
				{
					mapa.put("componentesServicios_"+i, SqlBaseComponentesPaquetesDao.consultarServiciosPaquete(con,mapa.get("codpaquete_"+i)+"", Utilidades.convertirAEntero(mapa.get("institucion_"+i)+"")));
					mapa.put("componentesAgurpacionServicios_"+i,SqlBaseComponentesPaquetesDao.consultarAgrupacionServiciosPaquete(con,mapa.get("codpaquete_"+i)+"", Utilidades.convertirAEntero(mapa.get("institucion_"+i)+"")));
					mapa.put("componentesArticulos_"+i, SqlBaseComponentesPaquetesDao.consultarArticulosPaquete(con,mapa.get("codpaquete_"+i)+"", Utilidades.convertirAEntero(mapa.get("institucion_"+i)+"")));
					mapa.put("componentesAgurpacionArticulos_"+i, SqlBaseComponentesPaquetesDao.consultarAgrupacionArticulosPaquete(con,mapa.get("codpaquete_"+i)+"", Utilidades.convertirAEntero(mapa.get("institucion_"+i)+"")));
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
		
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquetizaciones
	 * @return
	 */
	public static HashMap<String, Object> consultarServiciosPaquetes(Connection con, String[] codigoPaquetizaciones) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		String codigosPaq=codigoPaquetizaciones[0];
		int numRegistros=0;
		for(int i=1;i<codigoPaquetizaciones.length;i++)
		{
			codigosPaq+="','"+codigoPaquetizaciones[i];
		}
		try
		{
			String cadena=cadenaConsutalServicioPaquetes+" and  dp.codigo_paquetizacion in('"+codigosPaq+"')";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cadenaConsutalServicioPaquetes---->"+cadena);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				String tipoAsocio=UtilidadTexto.isEmpty(rs.getString("tipoasocio")+"")?"-1":rs.getString("tipoasocio");
				String servicioCXTemp=UtilidadTexto.isEmpty(rs.getString("serviciocx")+"")?"-1":rs.getString("serviciocx");
				String servicioTemp=UtilidadTexto.isEmpty(rs.getString("servicio")+"")?"":(rs.getString("servicio")+"");
				String articuloTemp=UtilidadTexto.isEmpty(rs.getString("articulo")+"")?"":(rs.getString("articulo")+"");
				String serArt=UtilidadTexto.isEmpty(servicioTemp)?articuloTemp:servicioTemp;
				
				String prefijo=rs.getString("solicitud")+"_"+serArt+"_"+servicioCXTemp+"_"+tipoAsocio+"_"+rs.getString("codpaquetizacion")+"_"+rs.getString("detcxhonorarios")+"_"+rs.getString("detasicxsalasmat");
				mapa.put("codigo_"+prefijo,rs.getString("codigo"));
				mapa.put("codpaquetizacion_"+prefijo,rs.getString("codpaquetizacion"));
				mapa.put("solicitud_"+prefijo,rs.getString("solicitud"));
				mapa.put("servicio_"+prefijo,rs.getString("servicio")==null?"":rs.getString("servicio"));
				mapa.put("articulo_"+prefijo,rs.getString("articulo")==null?"":rs.getString("articulo"));
				mapa.put("cantidad_"+prefijo,rs.getString("cantidad"));
				mapa.put("serviciocx_"+prefijo,rs.getString("serviciocx"));
				mapa.put("tipoasocio_"+prefijo,rs.getString("tipoasocio"));
				mapa.put("nomtipoasocio_"+prefijo,rs.getString("nomtipoasocio"));
				mapa.put("principal_"+prefijo,rs.getString("principal"));
				numRegistros++;
			}
			Utilidades.imprimirMapa(mapa);
		}
		catch (SQLException e) 
		{
			numRegistros=0;
			e.printStackTrace();
		}
		mapa.put("numRegistros", numRegistros);
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaPaquetizar(Connection con, String subCuenta, String cadenaConsultaSolSubcuenta) 
	{
		ArrayList resultado=new ArrayList();
		try
		{
			String cadena=cadenaConsultaSolSubcuenta+" order by solicitud,serviciocx ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(subCuenta));
			logger.info("cadenaConsultaSolSubcuenta-->"+(cadena+"").replace("?", subCuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				///hago la vadicaion con || por que los campos articulos y servicios son excluyentes.
				//validacion pendiente para cuando se inserte la informacion en det-cargos.
				//if(rs.getInt("numresservicio")==1||rs.getInt("numresarticulo")==1)
				{
					DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
					dto.setNumeroSolicitud(rs.getString("solicitud")==null?"":rs.getString("solicitud"));
					dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
					dto.setServicio(new InfoDatosString(rs.getString("servicio")==null?"":rs.getString("servicio").trim(),rs.getString("nomservicio")==null?"":rs.getString("nomservicio").trim()));
					dto.setCodigoCups(rs.getString("codigocups")==null?"":rs.getString("codigocups").trim());
					dto.setArticulo(new InfoDatosString(rs.getString("articulo")==null?"":rs.getString("articulo").trim(),rs.getString("nomarticulo")==null?"":rs.getString("nomarticulo").trim()));
					dto.setCuenta(rs.getString("cuenta")==null?"":rs.getString("cuenta"));
					dto.setTipoSolicitud(new InfoDatosInt(rs.getInt("tiposolicitud"),rs.getString("nomtiposolicitud")==null?"":rs.getString("nomtiposolicitud")));
					dto.setServicioCX(new InfoDatosString(rs.getString("serviciocx")==null?"":rs.getString("serviciocx").trim(),rs.getString("nomserviciocx")==null?"":rs.getString("nomserviciocx").trim()));
					dto.setCodigoCupsCx(rs.getString("codigocupscx")==null?"":rs.getString("codigocupscx").trim());
					dto.setTipoAsocio(new InfoDatosInt(rs.getInt("tipoasocio"),rs.getString("nomtipoasocio")==null?"":rs.getString("nomtipoasocio")));
					dto.setEstadoCargo(new InfoDatosInt(rs.getInt("estado"),rs.getString("descestado")==null?"":rs.getString("descestado")));
					dto.setFechaSolicitud(rs.getString("fechasolicitud"));
					dto.setConsecutivoSolicitud(rs.getString("consecutivosolicitud"));
					dto.setCentroCostoSolicita(new InfoDatosInt(rs.getInt("codccsolicita"),rs.getString("nomccsolicita")==null?"":rs.getString("nomccsolicita").trim()));
					dto.setCentroCostoEjecuta(new InfoDatosInt(rs.getInt("codccejecuta"),rs.getString("nomccejecuta")==null?"":rs.getString("nomccejecuta").trim()));
					dto.setNumResponsablesFacturadosMismoServicio(rs.getInt("numresfactservicio"));
					dto.setNumResponsablesFacturadosMismoArticulo(rs.getInt("numresfactarticulo"));
					dto.setNumResponsablesMismoServicio(rs.getInt("numresservicio"));
					dto.setNumResponsablesMismoArticulo(rs.getInt("numresarticulo"));
					dto.setCantidad(UtilidadTexto.isEmpty(rs.getString("cantidadcargada"))?"":rs.getString("cantidadcargada"));
					dto.setDetcxhonorarios(rs.getInt("detcxhonorarios"));
					dto.setDetasicxsalasmat(rs.getInt("detasicxsalasmat"));
					dto.setFacturado(rs.getString("facturado"));
					resultado.add(dto);
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codServicio
	 * @return
	 */
	public static HashMap obtenerParametrosServicio(Connection con, String codServicio) 
	{
		String cadena="SELECT grupo_servicio,tipo_servicio,especialidad,espos from servicios where codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codServicio));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			ps.close();
			return mapaRetorno;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return new HashMap();
	}

	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @return
	 */
	public static HashMap obtenerParametrosArticulos(Connection con, String codArticulo) 
	{
		String cadena="SELECT s.clase,s.grupo,s.subgrupo,a.naturaleza,a.institucion,g.grupo_especial as grupoespecial " +
						"FROM articulo a inner join subgrupo_inventario s on(s.codigo=a.subgrupo) left outer join grupo_especial_x_articulo g on(g.articulo=a.codigo and g.activo='S') where a.codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codArticulo));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return new HashMap();
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param codigo
	 * @return
	 */
	public static int insertarEncabezadoPaquetizacion(Connection con, HashMap vo, int codigo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertEncabezadoPaquetizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO paquetizacion(" +
										" codigo," +
										" sub_cuenta," +
										" institucion," +
										" numero_solicitud_paquete," +
										" codigo_paquete_convenio," +
										" servicio," +
										" centro_costo," +
										" centro_costo_solicita_filtro," +
										" centro_costo_ejecuta_filtro," +
										" fecha_incial_sol_filtro," +
										" fecha_final_sol_filtro," +
										" usuario_modifica," +
										" fecha_modifica," +
										" hora_modifica" +
			 */
			
			ps.setInt(1, codigo);
			ps.setLong(2, Utilidades.convertirALong(vo.get("subcuenta")+""));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("numerosolicitud")+""));
			ps.setString(5, vo.get("codpaquete")+"");
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("servicio")+""));
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("centrocosto")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("ccsolicitafiltro")+""))
				ps.setObject(8, null);
			else
				ps.setInt(8, Utilidades.convertirAEntero(vo.get("ccsolicitafiltro")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("ccejecutafiltro")+""))
				ps.setObject(9, null);
			else
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("ccejecutafiltro")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("fechainisolfiltro")+""))
				ps.setObject(10, null);
			else
				ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechainisolfiltro")+"")));
			
			if(UtilidadTexto.isEmpty(vo.get("fechafinsolfiltro")+""))
				ps.setObject(11, null);
			else
				ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechafinsolfiltro")+"")));
			
			ps.setString(12, vo.get("usuario")+"");
			ps.setDate(13, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));
			ps.setString(14, vo.get("hora")+"");
			if(ps.executeUpdate()>0)
				return codigo;
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param codPaquetizacion
	 * @param numeroSolicitud 
	 * @return
	 */
	public static boolean eliminarPaquetizacion(Connection con, String codPaquetizacion, String numeroSolicitud,boolean eliminarEncabezado) 
	{
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try
		{
			int codigoDetalleCargo=ConstantesBD.codigoNuncaValido;
			String cadenaDetalleCargo="SELECT codigo_detalle_cargo from det_cargos where solicitud = "+numeroSolicitud +" AND eliminado='"+ConstantesBD.acronimoNo+"' and facturado='"+ConstantesBD.acronimoNo+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalleCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codigoDetalleCargo=rs.getInt(1);
			
			if(codigoDetalleCargo>0)
			{
				String acutalizarCargosRelacionadas="UPDATE det_cargos set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,cargo_padre=null  where cargo_padre= "+codigoDetalleCargo;
				//String acutalizarCargosRelacionadas="DELETE FROM det_cargos  where cargo_padre= "+codigoDetalleCargo;
				ps= new PreparedStatementDecorator(con.prepareStatement(acutalizarCargosRelacionadas));
				ps.executeUpdate();
				
			}
			else
			{
				return false;
			}
			
			int codigoDetalleSolSub=ConstantesBD.codigoNuncaValido;
			String cadenaDetalleSolSub="SELECT codigo from solicitudes_subcuenta where solicitud = "+numeroSolicitud+" AND eliminado='"+ConstantesBD.acronimoNo+"' and facturado='"+ConstantesBD.acronimoNo+"'";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalleSolSub,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codigoDetalleSolSub=rs.getInt(1);
			
			if(codigoDetalleSolSub>0)
			{
				String acutalizaSolSubRelacionadas="update solicitudes_subcuenta set eliminado='"+ConstantesBD.acronimoSi+"',sub_cuenta=null,sol_subcuenta_padre=null WHERE sol_subcuenta_padre= "+codigoDetalleSolSub;
				//String acutalizaSolSubRelacionadas="DELETE FROM solicitudes_subcuenta WHERE sol_subcuenta_padre= "+codigoDetalleSolSub;
				ps= new PreparedStatementDecorator(con.prepareStatement(acutalizaSolSubRelacionadas));
				ps.executeUpdate();
			}
			else
			{
				return false;
			}
			
			
			
			String cadenaEliminarDetalle="DELETE FROM detalle_paquetizacion where codigo_paquetizacion ="+codPaquetizacion;
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n------------->"+cadenaEliminarDetalle+"\n\n\n");
			ps.executeUpdate();

			if(eliminarEncabezado)
			{
				String anularCargoSolPaquete="UPDATE det_cargos set estado="+ConstantesBD.codigoEstadoFAnulada+" where codigo_detalle_cargo= "+codigoDetalleCargo;
				ps= new PreparedStatementDecorator(con.prepareStatement(anularCargoSolPaquete));
				ps.executeUpdate();

				
				String actualizarSolSub="UPDATE solicitudes set estado_historia_clinica = "+ConstantesBD.codigoEstadoHCAnulada+" where numero_solicitud="+numeroSolicitud;
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarSolSub));
				ps.executeUpdate();
				
				String cadenaEliminarEncabezado="DELETE FROM paquetizacion where codigo="+codPaquetizacion;
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarEncabezado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				return (ps.executeUpdate()>0);
				
			}
			return true;
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
	 * @param codigo 
	 * @return
	 */
	public static boolean guardarDetallePaquete(Connection con, HashMap vo, String cadena) 
	{
		boolean resultado=false;
		try
		{
			for(int i=0;i<Utilidades.convertirAEntero(vo.get("numRegistros")+"");i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO detalle_paquetizacion (" +
														" codigo," +
														" codigo_paquetizacion," +
														" numero_solicitud," +
														" servicio," +
														" articulo," +
														" cantidad," +
														" servicio_cx," +
														" tipo_asocio," +
														" usuario_modifica," +
														" fecha_modifica," +
														" hora_modifica," +
														" principal," +
														" det_cx_honorarios," +
														" det_asocio_cx_salas_mat" +
														") " +
														" values ('seq_det_paquetizacion'),?,?,?,?,?,?,?,?,?,?,?,?,?)
				 */
				
				ps.setLong(1, Utilidades.convertirALong(vo.get("codpaquetizacion_"+i)+""));
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("numerosolicitud_"+i)+""));
				
				if(UtilidadTexto.isEmpty(vo.get("servicio_"+i)+""))
					ps.setObject(3, null);
				else
					ps.setInt(3, Utilidades.convertirAEntero(vo.get("servicio_"+i)+""));
				
				if(UtilidadTexto.isEmpty(vo.get("articulo_"+i)+""))
					ps.setObject(4, null);
				else
					ps.setInt(4, Utilidades.convertirAEntero(vo.get("articulo_"+i)+""));
				
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("cantidad_"+i)+""));
				
				if(UtilidadTexto.isEmpty(vo.get("serviciocx_"+i)+""))
					ps.setObject(6, null);
				else
					ps.setInt(6, Utilidades.convertirAEntero(vo.get("serviciocx_"+i)+""));
				
				if(UtilidadTexto.isEmpty(vo.get("tipoasocio_"+i)+"")||Utilidades.convertirAEntero(vo.get("tipoasocio_"+i)+"")<=0)
					ps.setObject(7, null);
				else
					ps.setInt(7, Utilidades.convertirAEntero(vo.get("tipoasocio_"+i)+""));
				
				ps.setString(8, vo.get("usuario")+"");
				ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));
				ps.setString(10, vo.get("hora")+"");
				ps.setString(11, vo.get("principal_"+i)+"");
				if(Utilidades.convertirAEntero(vo.get("detcxhonorarios_"+i)+"")>0)
				{
					ps.setInt(12,Utilidades.convertirAEntero(vo.get("detcxhonorarios_"+i)+""));
					
				}
				else
				{
					ps.setObject(12, null);
				}
				if(Utilidades.convertirAEntero(vo.get("detasicxsalasmat_"+i)+"")>0)
				{
					ps.setInt(13,Utilidades.convertirAEntero(vo.get("detasicxsalasmat_"+i)+""));
				}
				else
				{
					ps.setObject(13, null);
				}
				if(ps.executeUpdate()>0)
				{
					resultado=true;
					
					int codigoDetalleCargo=ConstantesBD.codigoNuncaValido;
					String cadenaDetalleCargo="SELECT codigo_detalle_cargo from det_cargos where solicitud = "+vo.get("numerosolicitud")+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalleCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
						codigoDetalleCargo=rs.getInt(1);
					
					
					int codigoDetalleSolSub=ConstantesBD.codigoNuncaValido;
					String cadenaDetalleSolSub="SELECT codigo from solicitudes_subcuenta where solicitud = "+vo.get("numerosolicitud")+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalleSolSub,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
						codigoDetalleSolSub=rs.getInt(1);
					
					
					int codSolSub=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_sol_sub_cuentas");
					logger.info("\n\n\n\n-----------CODIGO codSolSub--->"+codSolSub+"\n\n\n");
					
					String cadena1="INSERT INTO solicitudes_subcuenta " +
										" (codigo,solicitud,sub_cuenta,servicio,articulo,porcentaje,cantidad,monto,cubierto,cuenta,tipo_solicitud,paquetizada,sol_subcuenta_padre,servicio_cx,tipo_asocio,tipo_distribucion,fecha_modifica,hora_modifica,usuario_modifica,det_cx_honorarios,det_asocio_cx_salas_mat) " +
								" (select " +
										codSolSub+"," +
										" solicitud," +
										" sub_cuenta," +
										" servicio," +
										" articulo," +
										" porcentaje," +
										vo.get("cantidad_"+i)+"," +
										" monto," +
										" cubierto," +
										" cuenta," +
										" tipo_solicitud," +
										" '"+ConstantesBD.acronimoSi+"'," +
										codigoDetalleSolSub+"," +
										" servicio_cx," +
										" tipo_asocio," +
										" tipo_distribucion," +
										" current_date," +
										" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
										"'"+vo.get("usuario")+"'," +
										" det_cx_honorarios," +
										" det_asocio_cx_salas_mat" +
									" from solicitudes_subcuenta " +
									" where " +
											" solicitud="+vo.get("numerosolicitud_"+i)+" "+
											(UtilidadTexto.isEmpty(vo.get("servicio_"+i)+"")?" and servicio is null and articulo="+vo.get("articulo_"+i)+" ":" and articulo is null and servicio="+vo.get("servicio_"+i)+" ") +
											(UtilidadTexto.isEmpty(vo.get("serviciocx_"+i)+"")?" and servicio_cx is null ":" and servicio_cx="+vo.get("serviciocx_"+i)+" ")  +
											(Utilidades.convertirAEntero(vo.get("detcxhonorarios_"+i)+"")<=0?" and det_cx_honorarios is null ":" and det_cx_honorarios="+vo.get("detcxhonorarios_"+i)+" ")  +
											(Utilidades.convertirAEntero(vo.get("detasicxsalasmat_"+i)+"")<=0?" and det_asocio_cx_salas_mat is null ":" and det_asocio_cx_salas_mat="+vo.get("detasicxsalasmat_"+i)+" ")  +
											" and sol_subcuenta_padre is null AND " +
											" eliminado='"+ConstantesBD.acronimoNo+"' and " +
											" facturado='"+ConstantesBD.acronimoNo+"'  "+ValoresPorDefecto.getValorLimit1()+" 1"+

								")";
					String cadena2=" INSERT INTO det_cargos " +
								" (codigo_detalle_cargo,sub_cuenta,convenio,esquema_tarifario,cantidad_cargada,valor_unitario_tarifa,valor_unitario_cargado,valor_total_cargado,porcentaje_cargado,porcentaje_recargo,valor_unitario_recargo,porcentaje_dcto,valor_unitario_dcto,valor_unitario_iva,nro_autorizacion,estado,cubierto,tipo_distribucion,solicitud,servicio,articulo,servicio_cx,tipo_asocio,facturado,tipo_solicitud,paquetizado,cargo_padre,usuario_modifica,fecha_modifica,hora_modifica,cod_sol_subcuenta,observaciones,requiere_autorizacion,contrato,det_cx_honorarios,det_asocio_cx_salas_mat,es_portatil) " +
						" (select " +
							UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_cargos")+"," +
							" sub_cuenta," +
							" convenio," +
							" esquema_tarifario," +
							vo.get("cantidad_"+i)+"," +
							" valor_unitario_tarifa," +
							" valor_unitario_cargado," +
							vo.get("cantidad_"+i)+"*valor_unitario_cargado," +
							" porcentaje_cargado," +
							" porcentaje_recargo," +
							" valor_unitario_recargo," +
							" porcentaje_dcto," +
							" valor_unitario_dcto, "+
							" valor_unitario_iva," +
							" nro_autorizacion," +
							" estado," +
							" cubierto," +
							" tipo_distribucion," +
							" solicitud," +
							" servicio," +
							" articulo," +
							" servicio_cx," +
							" tipo_asocio," +
							" facturado," +
							" tipo_solicitud," +
							" '"+ConstantesBD.acronimoSi+"'," +
							codigoDetalleCargo+" ," +
							"'"+vo.get("usuario")+"'," +
							" current_date," +
							" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
							codSolSub+"," +
							" observaciones," +
							" requiere_autorizacion," +
							" contrato ," +
							" det_cx_honorarios," +
							" det_asocio_cx_salas_mat," +
							" es_portatil" +
						" from det_cargos " +
						" where " +
								" solicitud="+vo.get("numerosolicitud_"+i)+" "+
								(UtilidadTexto.isEmpty(vo.get("servicio_"+i)+"")?" and servicio is null and articulo="+vo.get("articulo_"+i)+" ":" and articulo is null and servicio="+vo.get("servicio_"+i)+" ") +
								(UtilidadTexto.isEmpty(vo.get("serviciocx_"+i)+"")?" and servicio_cx is null ":" and servicio_cx="+vo.get("serviciocx_"+i)+" ")  +
								(Utilidades.convertirAEntero(vo.get("detcxhonorarios_"+i)+"")<=0?" and det_cx_honorarios is null ":" and det_cx_honorarios="+vo.get("detcxhonorarios_"+i)+" ")  +
								(Utilidades.convertirAEntero(vo.get("detasicxsalasmat_"+i)+"")<=0?" and det_asocio_cx_salas_mat is null ":" and det_asocio_cx_salas_mat="+vo.get("detasicxsalasmat_"+i)+" ")  +
								" and cargo_padre is null AND " +
								" eliminado='"+ConstantesBD.acronimoNo+"' and " +
								" facturado='"+ConstantesBD.acronimoNo+"'   "+ValoresPorDefecto.getValorLimit1()+" 1"+
					" )";
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("cadena 1-->"+cadena1);
					ps.executeUpdate();
					
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("cadena 2-->"+cadena2);
					ps.executeUpdate();
					
				}
				else
				{
					i=Utilidades.convertirAEntero(vo.get("numRegistros")+"");
					resultado=false;
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			resultado=false;
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap consultarLiquidacionPaquete(Connection con, String subCuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaLiquidacionPaquetesResponsables,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param evento
	 * @param numeroSolicitud
	 * @param codServArt
	 * @param codigoDetPadre
	 * @param esServicio
	 * @param facturado 
	 * @return
	 */
	public static boolean actualizarCantidadDetCargo(Connection con, int cantidad,String subCuenta, String numeroSolicitud, String codServArt, int codigoDetPadre, boolean esServicio, String facturado) 
	{
		try 
		{
			///por medio del trigger se actualiza la solicitudes_subcuenta. 
			String acutalizarCargos="UPDATE det_cargos set cantidad_cargada="+cantidad+",valor_total_cargado=valor_unitario_cargado*"+cantidad+" where sub_cuenta= "+Utilidades.convertirALong(subCuenta)+" and solicitud="+numeroSolicitud +" AND eliminado='"+ConstantesBD.acronimoNo+"' and facturado='"+facturado+"'"; 
			if(esServicio)
				acutalizarCargos=acutalizarCargos+" AND servicio="+codServArt;
			else 
				acutalizarCargos=acutalizarCargos+" AND articulo="+codServArt;
			if(codigoDetPadre>0)
				acutalizarCargos=acutalizarCargos+" AND cargo_padre="+codigoDetPadre;
			else
				acutalizarCargos=acutalizarCargos+" AND cargo_padre is null ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(acutalizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			return true;
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
	 * @param codigoPersona
	 * @param aplicarValidaciones 
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona, boolean aplicarValidaciones) 
	{

		String cadena="SELECT " +
								" i.id as idingreso," +
								" i.preingreso AS indpre," +
								" CASE WHEN i.reingreso IS NOT NULL THEN i.reingreso ELSE 0 END AS indrei," +
								" i.estado as estadoingreso," +
								" i.fecha_ingreso as fechaingreso," +
								" i.fecha_egreso as fechaegreso," +
								" c.id as cuenta," +
								" c.estado_cuenta as codestadocuenta," +
								" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
								" c.via_ingreso as codviaingreso, " +
								" c.tipo_paciente as tipopaciente," +
								" getnomcentroatencion(cc.centro_atencion) as centroatencion, " +
								" getNombreViaIngresoTipoPac(c.id) as nomviaingreso " +
						" from cuentas c " +
						" inner join ingresos i on(c.id_ingreso=i.id) " +
						" inner join centros_costo cc on(cc.codigo=c.area) " +
						" where " +
						" c.codigo_paciente= ? " ;
		
		if(aplicarValidaciones)
		{
					cadena=cadena+	" and " +
								" i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and " +
								" (c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+","+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaExcenta+","+ConstantesBD.codigoEstadoCuentaFacturada+") or (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" and getEsCuentaAsocioincompleto(c.id)='S') ) ";
		}
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param cantidad
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param servicio
	 * @param servicioCX
	 * @param tipoAsocio
	 * @param codigoDetPadre
	 * @param esServicio
	 * @param facturado
	 * @return
	 */
	public static boolean actualizarCantidadDetCargoServicioCx(Connection con,int cantidad, String subCuenta, String numeroSolicitud,String servicio, String servicioCX, int tipoAsocio,int codigoDetPadre, String facturado,int detcxhonorarios,int detasicxsalasmat) 
	{
		try 
		{
			///por medio del trigger se actualiza la solicitudes_subcuenta. 
			String acutalizarCargos="UPDATE det_cargos set cantidad_cargada="+cantidad+",valor_total_cargado=valor_unitario_cargado*"+cantidad+" where sub_cuenta= "+Utilidades.convertirALong(subCuenta)+" and solicitud="+numeroSolicitud +" AND eliminado='"+ConstantesBD.acronimoNo+"' and facturado='"+facturado+"'"; 
			acutalizarCargos=acutalizarCargos+" AND servicio="+servicio+" and servicio_cx="+servicioCX+" and tipo_asocio="+tipoAsocio+
			(detcxhonorarios<=0?" and det_cx_honorarios is null ":" and det_cx_honorarios="+detcxhonorarios+" ")  +
			(detasicxsalasmat<=0?" and det_asocio_cx_salas_mat is null ":" and det_asocio_cx_salas_mat="+detasicxsalasmat+" ") ;

			if(codigoDetPadre>0)
				acutalizarCargos=acutalizarCargos+" AND cargo_padre="+codigoDetPadre;
			else
				acutalizarCargos=acutalizarCargos+" AND cargo_padre is null ";
			logger.info("cadena-->"+acutalizarCargos);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(acutalizarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;

	}
}