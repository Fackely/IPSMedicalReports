package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.ConsultaInventarioFisicoArticulos;

/**
 * 
 * @author axioma
 * @modificated Ing. Felipe Pérez Granda
 *
 */
public class SqlBaseConsultaInventarioFisicoArticulosDao{

// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	
	/**
	 * Consulta para filtrar articulos segun lo parametros dados
	 */
	private static String filtrarArticulosStr = "SELECT DISTINCT  " +
													"a.codigo AS codigo " +
												"FROM " +
													"articulo a, " +
													"subgrupo_inventario si, " +
													"det_articulos_por_almacen dapa, " +
													"articulos_por_almacen apa, " +
													"clase_inventario ci, " +
													"grupo_inventario gi  " +
												"WHERE " +
													" a.subgrupo = si.codigo " +
													" AND a.estado= " +ValoresPorDefecto.getValorTrueParaConsultas()+
													" AND a.codigo = dapa.articulo " +
													" AND dapa.codigo_art_por_almacen = apa.codigo_pk  " +
													" AND ci.codigo = gi.clase " +
													" AND gi.codigo = si.grupo "; 
	
	
	private static String filtrarArticulosStrSin= "SELECT DISTINCT  " +
													"a.codigo AS codigo " +
													"FROM " +
														" subgrupo_inventario si, " +
														" clase_inventario ci, " +
														" grupo_inventario gi,  " +
														" articulo a " +
													" inner join " +
													"	articulos_almacen aa on (a.codigo=aa.articulo) " +
													" inner join " +
													"	centros_costo cc on (cc.codigo=aa.almacen) " +
													" left outer join " +
														" det_articulos_por_almacen dapa on (dapa.articulo=aa.articulo) " +
													" left outer join " +
														" articulos_por_almacen apa on (dapa.codigo_art_por_almacen=apa.codigo_pk) " +
													"WHERE " +
														" a.subgrupo = si.codigo " +
														" AND a.estado=  " +ValoresPorDefecto.getValorTrueParaConsultas()+
														" AND ci.codigo = gi.clase " +
														" AND gi.codigo = si.grupo ";

	
	// --------------- METODOS
	
	
	/**
	 * 
	 */
	public static HashMap filtrarArticulos(Connection con, ConsultaInventarioFisicoArticulos cuc){
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String filtro="";
		String codigosFiltrados="";
		
		logger.info("\n\n\n [articulos]"+cuc.getCodigosArticulos());
		
		if (cuc.getEstado().equals("filtrarArticulos"))
			{
			filtro = filtrarArticulosStr;
			if(cuc.getAlmacen()>0)
				filtro+= " AND apa.almacen = "+cuc.getAlmacen();
			
			if(cuc.getCentroAtencion()>0)
				filtro+= " AND apa.centro_atencion = "+cuc.getCentroAtencion();
			
			if (cuc.getCodigosArticulos().equals("")){	
				if (cuc.getClase() > 0){
					filtro += " AND ci.codigo = "+cuc.getClase();
					if (cuc.getGrupo() >0){
						filtro += " AND gi.codigo = "+cuc.getGrupo();
							if (cuc.getSubgrupo()>0){
								filtro += " AND si.codigo = "+cuc.getSubgrupo();
								}
							}
						}	
					}
				else {
					codigosFiltrados = cuc.getCodigosArticulos();
				}
			}
		else
			if(cuc.getEstado().equals("filtrarArticulosSin"))
			{
				filtro=filtrarArticulosStrSin;
				if(cuc.getAlmacen()>0)
					filtro+= " AND aa.almacen = "+cuc.getAlmacen();
				
				if(cuc.getCentroAtencion()>0)
					filtro+= " AND cc.centro_atencion = "+cuc.getCentroAtencion();
				
				if (cuc.getCodigosArticulos().equals("")){	
					
					if (cuc.getClase() > 0){
						filtro += " AND ci.codigo = "+cuc.getClase();
						if (cuc.getGrupo() >0){
							filtro += " AND gi.codigo = "+cuc.getGrupo();
							if (cuc.getSubgrupo()>0){
								filtro += " AND si.codigo = "+cuc.getSubgrupo();
							}
						}
					}
				} 
				else {
					codigosFiltrados = cuc.getCodigosArticulos();
				}
			}
	
		try
		{
				
				logger.info("\n\n [CONSULTA ARTICULOS GENERACION CADENA CODIGOS] \n\n"+filtro);
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(filtro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info(filtro+"\n\n");
				if (cuc.getCodigosArticulos().equals("")){	
				codigosFiltrados = crearCadena(mapa);
				}

			
			
			String consultaArticulosPreparados="SELECT articulo as codigo from preparacion_toma_inventario where articulo IN ("+codigosFiltrados+"-1) AND estado='FIN'";
			PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaArticulosPreparados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(consultaArticulosPreparados+"\n\n");
			HashMap codi=new HashMap();
			codi.put("numRegistros", "0");
			String codigosPreparados=crearCadena(codi=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery())));
			
			String consultarArticulosStr="";
			
			if (cuc.getEstado().equals("filtrarArticulos"))
			{
			consultarArticulosStr = "SELECT "+
											 " t.descripcion, "+ 
											 " t.codigo,  "+
											 " t.unidad_medida, " +
											 " getunidadmedidaarticulo(t.codigo) as nomunidadmedida, "+ 
											 " coalesce(t.codigo_interfaz,'') as codigo_interfaz,  "+
											 " coalesce(t.lote,'N/A') as lote,  "+
											 " coalesce(t.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
											 " t.codigo_lote,"+ 
											 " max (getExistenciasArticulo(t.codigo,t.ultima_preparacion)) as existencia, " +
											 " max (getFechaTomaPreparacion(t.codigo,t.ultima_preparacion)) as fecha_toma, " +
											 " max (getUsuarioModificaPreparacion(t.codigo,t.ultima_preparacion)) as usuario_modifica, " +
											 " max (t.ajuste) as ajuste,  "+
											 " t.codregistroconteo as codregistroconteo," +
											 " max(axif.codigo) as codajuste, "+
										//	 " SUM (t.numero_conteo) AS cant_conteo "+
											 " SUM (getCantRegistroConteoFin(t.ultima_preparacion, t.numero_conteo)) AS cant_conteo "+ 											 
										//	 " getobtenerubicacionarticulo(t.codigo_dapa) as ubicacion  "+
											 " 	FROM (        "+
											" SELECT  "+
												//" f.descripcion || ' CONC: ' || f.concentracion || ' F.F: ' || getnomformafarmaceutica(f.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(f.codigo) as descripcion, "+
												" getdescarticulosincodigo(f.codigo) AS descripcion, "+
												" f.codigo,  "+
												" f.unidad_medida, "+ 
												" f.codigo_interfaz,  "+
												" f.lote,  "+
												" f.fecha_vencimiento || '' as fecha_vencimiento, " +
												" f.codigo_lote, "+ 
												" max(getNumeroUltimoConteo(f.codigo_preparacion)) as numero_conteo, " +
												" f.codigo_dapa, " +
												" max (f.ajuste) as ajuste, "+
												" max (getExistenciasArticulo(f.codigo,f.codigo_preparacion)) as existencia, " +
												" max (getFechaTomaPreparacion(f.codigo,f.codigo_preparacion)) as fecha_toma, " +
												" max (getUsuarioModificaPreparacion(f.codigo,f.codigo_preparacion)) as usuario_modifica, " +
												" max (getCodRegistroConteoInv(getNumeroUltimoConteo(f.codigo_preparacion),f.codigo_preparacion)) as codregistroconteo, " +
												" max(f.codigo_preparacion) as ultima_preparacion "+
												" FROM ( "+
														" ( "+
														" SELECT "+ 
													" a.descripcion, "+ 
														" a.codigo,  "+
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" l.lote,  "+
														" l.codigo as codigo_lote, "+ 
														" to_char(l.fecha_vencimiento, 'dd/mm/yyyy') as fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza,  "+
														" a.subgrupo,  "+
														" max (rci.codigo_preparacion) as codigo_preparacion, "+
														" pti.codigo_dapa, " +
														" max (rci.codigo_ajuste) as ajuste, "+
														" max(rci.numero_conteo) AS numero_conteo "+
														" FROM  "+
													" articulo a,  "+
														" articulo_almacen_x_lote l "+ 
														" INNER JOIN  "+
													" preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote) "+ 
													" INNER JOIN "+ 
													" registro_conteo_inventario rci ON (rci.articulo=l.articulo and rci.codigo_preparacion=pti.codigo) "+
													" WHERE a.codigo=l.articulo  "+
													" and a.maneja_lotes='S'  " +
													" and rci.almacen="+cuc.getAlmacen()+" " +
													" AND rci.articulo IN (-1,"+codigosPreparados+"-1)"+" "+
													" and pti.estado='FIN' " +
															" GROUP BY  "+
													" a.descripcion, "+ 
														" a.codigo, "+ 
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" l.lote, l.codigo, "+ 
														" l.fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, "+ 
														" a.subgrupo, "+ 
														" pti.codigo_dapa " +
														" )  "+
												" UNION   "+
												" ( "+
														" SELECT "+ 
													" a.descripcion, "+ 
														" a.codigo, "+ 
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" 'N/A' as lote,  "+
														" -1 as codigo_lote,  "+
														" 'N/A' as fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, "+ 
														" a.subgrupo, "+ 
														" max (rci.codigo_preparacion) as codigo_preparacion, " +
														" pti.codigo_dapa, " +
														" max (rci.codigo_ajuste) as ajuste, "+
														" max(rci.numero_conteo) AS numero_conteo " +
														" FROM  "+
													" 	articulos_almacen ex "+ 
													" INNER JOIN articulo a ON(a.codigo=ex.articulo) "+ 
													" INNER JOIN preparacion_toma_inventario pti ON(pti.articulo=a.codigo) "+ 
													" INNER JOIN registro_conteo_inventario rci ON(rci.articulo=pti.articulo and rci.codigo_preparacion=pti.codigo) "+ 
													" WHERE  "+
														" rci.almacen ="+cuc.getAlmacen()+" "+
														" AND a.maneja_lotes = 'N' "+ 
														" AND rci.articulo IN (-1,"+codigosPreparados+"-1)"+" "+
														" and pti.estado='FIN' " +
													" GROUP BY  "+
														" a.descripcion, "+ 
														" a.codigo,  "+
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" a.concentracion, "+ 
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, a.subgrupo, "+ 
														" pti.codigo_dapa " +
														" ) "+
													" ) f "+ 
												" GROUP BY  "+
												" f.descripcion, "+ 
												" f.concentracion,  "+
												" f.forma_farmaceutica, "+ 
												" f.naturaleza,  "+
												" f.codigo, "+
												" f.unidad_medida, "+
												" f.codigo_interfaz, "+
												" f.lote, "+ 
												" f.fecha_vencimiento, " +
												" f.codigo_lote, "+ 
												" f.codigo_dapa, " +
												" f.numero_conteo" +
												" )t " +
											" INNER JOIN ajustes_x_inv_fisico axif ON (axif.codigo=t.ajuste)" +
											" GROUP BY  "+
												"  t.descripcion, "+ 
												 " t.codigo, "+
												 " t.unidad_medida, "+
												 " t.codigo_interfaz, "+
												 " t.lote, "+ 
												 " t.fecha_vencimiento, " +
												 " t.codigo_lote," +
												 " t.codregistroconteo " +
										//		 " t.codigo_dapa  "+ 
											 " ORDER BY  "+
												 " "+cuc.getPatronOrdenar();
			}
			else 
				if(cuc.getEstado().equals("filtrarArticulosSin"))
				{
					logger.info("esta en filtrar sin");
					consultarArticulosStr = " SELECT   "+
													//" f.descripcion || ' CONC: ' || f.concentracion || ' F.F: ' || getnomformafarmaceutica(f.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(f.codigo) as descripcion, "+
													" getdescarticulosincodigo(f.codigo) AS descripcion, "+
													" f.codigo,  "+
													" f.unidad_medida, " +
													" getunidadmedidaarticulo(f.codigo) as nomunidadmedida, "+ 
													" coalesce(f.codigo_interfaz,'') as codigo_interfaz,  "+
													" coalesce(f.lote,'N/A') as lote,  "+
													" coalesce(f.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
													" f.codigo_lote, "+ 
													" f.fecha_toma, "+ 
													" f.existencia,  "+
													" f.ultima_preparacion, "+
													" f.usuario_modifica " +
										//			" getobtenerubicacionarticulo(dapa.codigo_det_pk) as ubicacion  "+
													"FROM (("+
													" SELECT "+ 
															" a.descripcion, "+ 
																" a.codigo,  "+
																" a.unidad_medida, "+ 
																" a.codigo_interfaz,  "+
																" l.lote,  "+
																" l.codigo as codigo_lote, "+ 
																" to_char(l.fecha_vencimiento, 'dd/mm/yyyy') || '' as fecha_vencimiento, "+ 
																" a.concentracion,  "+
																" a.forma_farmaceutica, "+ 
																" a.naturaleza,  "+
																" a.subgrupo,  "+
																" ' ' AS cod_prep, "+ 
																" ' ' AS fecha_toma,  "+
																" ' ' as existencia, "+
																" ' ' as usuario_modifica, "+
																" ' ' as numero_conteo, "+
																" ' ' AS ultima_preparacion "+
																" FROM  "+
																" articulo a,  "+
																" articulo_almacen_x_lote l " +
																" LEFT OUTER JOIN " +
																" det_articulos_por_almacen dapa on (dapa.articulo=l.articulo)" +
																" LEFT OUTER JOIN " +
																" articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen and l.almacen=apa.almacen) "+
															" WHERE a.codigo=l.articulo  "+
																" and a.maneja_lotes='S'  " +
																" and l.almacen="+cuc.getAlmacen()+" " +
																" AND a.codigo NOT IN (select articulo from preparacion_toma_inventario where almacen="+cuc.getAlmacen()+")"+" " +
																" and l.articulo IN (-1,"+codigosFiltrados+"-1) " +
																" )  "+
														" UNION   "+
														" ( "+
																" SELECT "+ 
															" a.descripcion, "+ 
																" a.codigo, "+ 
																" a.unidad_medida, "+ 
																" a.codigo_interfaz,  "+
																" 'N/A' as lote,  "+
																" -1 as codigo_lote,  "+
																" 'N/A' as fecha_vencimiento, "+ 
																" a.concentracion,  "+
																" a.forma_farmaceutica, "+ 
																" a.naturaleza, "+ 
																" a.subgrupo, "+ 
																" ' ' AS cod_prep, "+ 
																" ' ' as fecha_toma,  "+
																" ' ' as existencia,  "+
																" ' ' as usuario_modifica, "+
																" ' ' as numero_conteo, "+
																" ' ' as  ultima_preparacion "+
																" FROM  "+
															" 	articulos_almacen ex "+ 
															" INNER JOIN articulo a ON(a.codigo=ex.articulo) " +
															" LEFT OUTER JOIN " +
															" det_articulos_por_almacen dapa on (dapa.articulo=ex.articulo)" +
															" LEFT OUTER JOIN " +
															" articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen and apa.almacen=ex.almacen) "+
															" WHERE  "+
																" ex.almacen ="+cuc.getAlmacen()+" "+
																" AND a.maneja_lotes = 'N' "+ 
																" AND a.codigo NOT IN (select articulo from preparacion_toma_inventario where almacen="+cuc.getAlmacen()+")"+" " +
																" and ex.articulo IN (-1,"+codigosFiltrados+"-1) " +
																" ) "+
															" ) f " +
															" LEFT OUTER JOIN " +
															" det_articulos_por_almacen dapa ON(dapa.articulo=f.codigo) " +
															" LEFT OUTER JOIN " +
															" articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) "+
															"GROUP BY "+
																	"f.descripcion,  " +
																	"f.codigo,   " +
																	"f.unidad_medida,  " +
																	"f.codigo_interfaz,   " +
																	"f.lote,   " +
																	"f.fecha_vencimiento,  " +
																	"f.codigo_lote,  " +
																	"f.fecha_toma,  " +
																	"f.existencia,   " +
																	"f.ultima_preparacion,  " +
																	"f.usuario_modifica," +
																	"f.concentracion," +
																	"f.forma_farmaceutica," +
																	"f.naturaleza " +
												//					"dapa.codigo_det_pk " +
															"ORDER BY "+cuc.getPatronOrdenar();
													
													
			}
			
			
			
			
			logger.info("CONSULTA: >>> "+consultarArticulosStr);
			
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
			
			if(codigosPreparados.isEmpty())
			{	
				for(int i=0;i<Integer.parseInt(mapa.get("numRegistros").toString());i++ )
				{
					codigosPreparados = crearCadena(mapa);
					mapa.put("codigos_preparados",codigosPreparados);
				}
			}else{
				mapa.put("codigos_preparados", codigosPreparados);
			}
			mapa.put("codigos_filtrados", codigosFiltrados);

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
	 * @param cuc
	 * @return
	 */
	public static HashMap consultarConteosArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String consultarConteosArticulo="";
		if(cuc.getCodigoPreparacion()<0)
		{
			consultarConteosArticulo = " SELECT  "+
												//" f.descripcion || ' CONC: ' || f.concentracion || ' F.F: ' || getnomformafarmaceutica(f.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(f.codigo) as descripcion, "+
												" getdescarticulosincodigo(f.codigo) AS descripcion, "+
												" f.codigo,  "+
												" f.unidad_medida, "+ 
												" f.codigo_interfaz,  "+
												" coalesce(f.lote,'N/A') as lote,  "+
												" coalesce(f.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
												" f.codigo_lote, "+ 
												" f.ultima_preparacion, "+
												" f.numero_conteo as conteo, " +
												" f.codigo_dapa, " +
												" dapa.ubicacion as ubicacion, " +
												" f.usuario_finaliza, " +
												" f.usuario_responsable, " +
												" f.estado, " +
												" getExistenciasArticulo(f.codigo,f.ultima_preparacion) as existencia, " +
												" getFechaTomaPreparacion(f.codigo,f.ultima_preparacion) as fecha_toma, " +
												" getUsuarioModificaPreparacion(f.codigo,f.ultima_preparacion) as usuario_modifica, " +
												" getFechaRegistroConteo(f.ultima_preparacion, f.numero_conteo) AS fecha_reg_conteo,  " +
												" getCantRegistroConteo(f.ultima_preparacion, f.numero_conteo) AS cant_conteo, "+
												" getNombreSeccion(apa.seccion) AS seccion, " +
												" getNombreSubseccion(apa.seccion, apa.subseccion) AS subseccion " +
												" FROM ( "+
														" ( "+
														" SELECT "+ 
														" a.descripcion, "+ 
														" a.codigo,  "+
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" l.lote,  "+
														" l.codigo as codigo_lote, "+ 
														" to_char(l.fecha_vencimiento, 'dd/mm/yyyy')||'' as fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza,  "+
														" a.subgrupo,  "+
														" rci.numero_conteo, "+
														" max(rci.codigo_preparacion) AS ultima_preparacion, " +
														" pti.codigo_dapa, " +
														" rci.usuario_finaliza, " +
														" rci.usuario_responsable, " +
														" rci.estado " +
														" FROM  "+
													" articulo a,  "+
														" articulo_almacen_x_lote l "+ 
														" INNER JOIN  "+
													" preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote) "+ 
													" INNER JOIN "+ 
													" registro_conteo_inventario rci ON (rci.articulo=l.articulo and rci.codigo_preparacion=pti.codigo) "+
													" WHERE a.codigo=l.articulo  "+
													" and a.maneja_lotes='S'  " +
													" and rci.almacen="+cuc.getAlmacen()+" " +
													" AND rci.articulo ="+cuc.getCodigoArticulo()+" "+
													" and rci.lote="+cuc.getCodigoLote()+" " +
													" and pti.estado='FIN'" +
													" and rci.codigo in("+cuc.getCodigosRegConteo()+") "+
															" GROUP BY  "+
													" a.descripcion, "+ 
														" a.codigo, "+ 
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" l.lote, l.codigo, "+ 
														" l.fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, "+ 
														" a.subgrupo, "+ 
														" rci.numero_conteo, " +
														" pti.codigo_dapa, " +
														" rci.usuario_finaliza," +
														" rci.usuario_responsable, " +
														" rci.estado  "+
											
														" )  "+
												" UNION   "+
												" ( "+
													 " SELECT "+ 
													    " a.descripcion, "+ 
														" a.codigo, "+ 
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" 'N/A' as lote,  "+
														" -1 as codigo_lote,  "+
														" 'N/A' as fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, "+ 
														" a.subgrupo, "+ 
														" rci.numero_conteo, "+
														" max(rci.codigo_preparacion) AS ultima_preparacion, " +
														" pti.codigo_dapa, " +
														" rci.usuario_finaliza,  "+
														" rci.usuario_responsable, " +
														" rci.estado " +
														" FROM  "+
													" 	articulos_almacen ex "+ 
													" INNER JOIN articulo a ON(a.codigo=ex.articulo) "+ 
													" INNER JOIN preparacion_toma_inventario pti ON(pti.articulo=a.codigo) "+ 
													" INNER JOIN registro_conteo_inventario rci ON(rci.articulo=pti.articulo and rci.codigo_preparacion=pti.codigo) "+ 
													" WHERE  "+
														" rci.almacen ="+cuc.getAlmacen()+" "+
														" AND a.maneja_lotes = 'N' "+ 
														" AND rci.articulo="+cuc.getCodigoArticulo()+" " +
														" AND pti.estado='FIN' "+
														" and rci.codigo in("+cuc.getCodigosRegConteo()+") "+
													" GROUP BY  "+
														" a.descripcion, "+ 
														" a.codigo,  "+
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" a.concentracion, "+ 
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, a.subgrupo, "+ 
														" rci.numero_conteo, " +
														" pti.codigo_dapa," +
														" rci.usuario_finaliza," +
														" rci.usuario_responsable, " +
														" rci.estado "+
														" ) "+
													" ) f "+
													"INNER JOIN " +
														"det_articulos_por_almacen dapa ON(dapa.articulo=f.codigo and dapa.codigo_det_pk=f.codigo_dapa) " +
													"INNER JOIN " +
														"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " ;
				
				
												
		}
		else
		{
			consultarConteosArticulo = " SELECT  "+
											//" f.descripcion || ' CONC: ' || f.concentracion || ' F.F: ' || getnomformafarmaceutica(f.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(f.codigo) as descripcion, "+
											" getdescarticulosincodigo(f.codigo) AS descripcion, "+
											" f.codigo,  "+
											" f.unidad_medida, "+ 
											" f.codigo_interfaz,  "+
											" coalesce(f.lote,'N/A') as lote,  "+
											" coalesce(f.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
											" f.codigo_lote, "+ 
											" f.fecha_toma, "+ 
											" f.existencia,  "+
											" f.ultima_preparacion, "+
											" f.usuario_modifica,  "+
											" f.numero_conteo as conteo, " +
											" f.codigo_dapa, " +
											" dapa.ubicacion as ubicacion, " +
											" f.usuario_finaliza, " +
											" f.fecha_reg_conteo, " +
											" f.usuario_responsable, " +
											" f.estado, " +
											" getCantRegistroConteo(f.ultima_preparacion, f.numero_conteo) AS cant_conteo, "+
											" getNombreSeccion(apa.seccion) AS seccion, " +
											" getNombreSubseccion(apa.seccion, apa.subseccion) AS subseccion " +
											" FROM ( "+
													" ( "+
													" SELECT "+ 
													" a.descripcion, "+ 
													" a.codigo,  "+
													" a.unidad_medida, "+ 
													" a.codigo_interfaz,  "+
													" l.lote,  "+
													" l.codigo as codigo_lote, "+ 
													" to_char(l.fecha_vencimiento, 'dd/mm/yyyy') ||'' as fecha_vencimiento, "+ 
													" a.concentracion,  "+
													" a.forma_farmaceutica, "+ 
													" a.naturaleza,  "+
													" a.subgrupo,  "+
													" pti.codigo AS cod_prep, "+ 
													" pti.fecha_toma,  "+
													" pti.existencia, "+
													" pti.usuario_modifica, "+
													" rci.numero_conteo, "+
													" rci.codigo_preparacion AS ultima_preparacion, " +
													" pti.codigo_dapa, " +
													" rci.usuario_finaliza, " +
													" rci.fecha_modifica || ' / ' || rci.hora_modifica AS fecha_reg_conteo, " +
													" rci.usuario_responsable, " +
													" rci.estado " +
													" FROM  "+
												" articulo a,  "+
													" articulo_almacen_x_lote l "+ 
													" INNER JOIN  "+
												" preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote) "+ 
												" INNER JOIN "+ 
												" registro_conteo_inventario rci ON (rci.articulo=l.articulo and rci.codigo_preparacion=pti.codigo) "+
												" WHERE a.codigo=l.articulo  "+
												" and a.maneja_lotes='S'  " +
												" and rci.almacen="+cuc.getAlmacen()+" " +
												" AND rci.articulo ="+cuc.getCodigoArticulo()+" "+
												" and rci.lote="+cuc.getCodigoLote()+" " +
												" and rci.codigo_preparacion="+cuc.getCodigoPreparacion()+" " +
												" and pti.estado='FIN' "+
													" )  "+
											" UNION   "+
											" ( "+
												 " SELECT "+ 
												    " a.descripcion, "+ 
													" a.codigo, "+ 
													" a.unidad_medida, "+ 
													" a.codigo_interfaz,  "+
													" 'N/A' as lote,  "+
													" -1 as codigo_lote,  "+
													" 'N/A' as fecha_vencimiento, "+ 
													" a.concentracion,  "+
													" a.forma_farmaceutica, "+ 
													" a.naturaleza, "+ 
													" a.subgrupo, "+ 
													" pti.codigo AS cod_prep, "+ 
													" pti.fecha_toma,  "+
													" pti.existencia,  "+
													" pti.usuario_modifica, "+
													" rci.numero_conteo, "+
													" rci.codigo_preparacion AS ultima_preparacion, " +
													" pti.codigo_dapa, " +
													" rci.usuario_finaliza,  "+
													" rci.fecha_modifica || ' / ' || rci.hora_modifica AS fecha_reg_conteo,  " +
													" rci.usuario_responsable, " +
													" rci.estado " +
													" FROM  "+
												" 	articulos_almacen ex "+ 
												" INNER JOIN articulo a ON(a.codigo=ex.articulo) "+ 
												" INNER JOIN preparacion_toma_inventario pti ON(pti.articulo=a.codigo) "+ 
												" INNER JOIN registro_conteo_inventario rci ON(rci.articulo=pti.articulo and rci.codigo_preparacion=pti.codigo) "+ 
												" WHERE  "+
													" rci.almacen ="+cuc.getAlmacen()+" "+
													" AND a.maneja_lotes = 'N' "+ 
													" AND rci.articulo="+cuc.getCodigoArticulo()+" "+
													" and rci.codigo_preparacion="+cuc.getCodigoPreparacion()+" " +
													" AND pti.estado='FIN'"+
													" ) "+
												" ) f "+
												"INNER JOIN " +
													"det_articulos_por_almacen dapa ON(dapa.articulo=f.codigo and dapa.codigo_det_pk=f.codigo_dapa) " +
												"INNER JOIN " +
													"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
													" WHERE f.ultima_preparacion="+cuc.getCodigoPreparacion()+" ";
										
		
		
			}
		
		consultarConteosArticulo+=" ORDER BY seccion,subseccion,ubicacion,f.descripcion,f.numero_conteo,fecha_reg_conteo "	;
		
		try
		{
			cuc.setCodigoPreparacion(ConstantesBD.codigoNuncaValido);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarConteosArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(consultarConteosArticulo);
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
	 * @param cuc
	 * @return
	 */
	public static HashMap consultarPreparacionesArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		
		String consultarPreparaciones = "SELECT "+
											 " t.descripcion, "+ 
											 " t.codigo,  "+
											 " t.unidad_medida, "+ 
											 " t.codigo_interfaz,  "+
											 " coalesce(t.lote,'N/A') as lote,  "+
											 " coalesce(t.fecha_vencimiento,'N/A') as fecha_vencimiento, " +
											 " t.codigo_lote,"+ 
											 " t.fecha_toma, "+ 
											 " t.existencia,  "+
											 " t.usuario_modifica, " +
											 " t.codigo_preparacion, " +
											 " t.codigo_dapa, " +
											 " t.codigo_ajuste, "+
											 " t.codregistroconteo," +
											 " axif.codigo as codajuste, "+
											 " getNombreSeccion(apa.seccion) AS seccion, " +
											 " getNombreSubseccion(apa.seccion, apa.subseccion) AS subseccion, " +
											 " SUM (getCantRegistroConteo(t.codigo_preparacion, t.ultimo_conteo)) AS cant_conteo "+ 
											 " 	FROM (        "+
											" SELECT  "+
												//" f.descripcion || ' CONC: ' || f.concentracion || ' F.F: ' || getnomformafarmaceutica(f.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(f.codigo) as descripcion, "+
												" getdescarticulosincodigo(f.codigo) AS descripcion, "+
												" f.codigo,  "+
												" f.unidad_medida, "+ 
												" f.codigo_interfaz,  "+
												" f.lote,  "+
												" f.fecha_vencimiento ||'' as fecha_vencimiento, " +
												" f.codigo_lote, "+ 
												" f.fecha_toma, "+ 
												" f.existencia,  "+
												" f.codigo_preparacion, "+
												" f.usuario_modifica,  " +
												" f.codigo_dapa, " +
												" f.codigo_ajuste, "+
												" max(getCodRegistroConteoInv(f.numero_conteo,f.codigo_preparacion)) as codregistroconteo, " +												
												" max(f.numero_conteo) as ultimo_conteo "+
												" FROM ( "+
														" ( "+
														" SELECT "+ 
													" a.descripcion, "+ 
														" a.codigo,  "+
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" l.lote,  "+
														" l.codigo as codigo_lote, "+ 
														" to_char(l.fecha_vencimiento, 'dd/mm/yyyy') as fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza,  "+
														" a.subgrupo,  "+
														" pti.codigo AS cod_prep, "+ 
														" pti.fecha_toma,  "+
														" pti.existencia, "+
														" pti.usuario_modifica, "+
														" rci.numero_conteo, "+
														" rci.codigo_preparacion, " +
														" pti.codigo_dapa, " +
														" rci.codigo_ajuste  "+
														" FROM  "+
													" articulo a,  "+
														" articulo_almacen_x_lote l "+ 
														" INNER JOIN  "+
													" preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote) "+ 
													" INNER JOIN "+ 
													" registro_conteo_inventario rci ON (rci.articulo=l.articulo and rci.codigo_preparacion=pti.codigo) "+
													" WHERE a.codigo=l.articulo  "+
													" and a.maneja_lotes='S'  " +
													" and rci.almacen="+cuc.getAlmacen()+" " +
													" AND rci.articulo ="+cuc.getCodigoArticulo()+" "+
													" AND l.codigo="+cuc.getCodigoLote()+" " +
													" AND pti.estado='FIN' "+
														" )  "+
												" UNION   "+
												" ( "+
														" SELECT "+ 
														" a.descripcion, "+ 
														" a.codigo, "+ 
														" a.unidad_medida, "+ 
														" a.codigo_interfaz,  "+
														" 'N/A' as lote,  "+
														" -1 as codigo_lote,  "+
														" 'N/A' as fecha_vencimiento, "+ 
														" a.concentracion,  "+
														" a.forma_farmaceutica, "+ 
														" a.naturaleza, "+ 
														" a.subgrupo, "+ 
														" pti.codigo AS cod_prep, "+ 
														" pti.fecha_toma,  "+
														" pti.existencia,  "+
														" pti.usuario_modifica, "+
														" rci.numero_conteo, "+
														" rci.codigo_preparacion," +
														" pti.codigo_dapa, " +
														" rci.codigo_ajuste  "+
														" FROM  "+
													" 	articulos_almacen ex "+ 
													" INNER JOIN articulo a ON(a.codigo=ex.articulo) "+ 
													" INNER JOIN preparacion_toma_inventario pti ON(pti.articulo=a.codigo) "+ 
													" INNER JOIN registro_conteo_inventario rci ON(rci.articulo=pti.articulo and rci.codigo_preparacion=pti.codigo) "+ 
													" WHERE  "+
														" rci.almacen ="+cuc.getAlmacen()+" "+
														" AND a.maneja_lotes = 'N' "+ 
														" AND rci.articulo ="+cuc.getCodigoArticulo()+" "+
														" AND pti.estado='FIN' "+
														" ) "+
													" ) f "+ 
												" GROUP BY  "+
												" f.descripcion, "+ 
												" f.concentracion,  "+
												" f.forma_farmaceutica, "+ 
												" f.naturaleza,  "+
												" f.codigo, "+
												" f.unidad_medida, "+
												" f.codigo_interfaz, "+
												" f.lote, "+ 
												" f.fecha_vencimiento, " +
												" f.codigo_lote, "+ 
												" f.fecha_toma,  "+
												" f.existencia, "+
												" f.usuario_modifica, " +
												" f.codigo_dapa, "+
												" f.codigo_ajuste," +
												" f.codigo_preparacion  "+
												" )t " +
											" INNER JOIN " +
												" det_articulos_por_almacen dapa ON(dapa.articulo=t.codigo and dapa.codigo_det_pk=t.codigo_dapa) " +
											" INNER JOIN " +
												" articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
											" INNER JOIN " +
												" ajustes_x_inv_fisico axif ON (axif.codigo=t.codigo_ajuste) "+
											" GROUP BY  "+
											" t.descripcion, "+ 
											 " t.codigo, "+
											 " t.unidad_medida, "+
											 " t.codigo_interfaz, "+
											 " t.lote, "+ 
											 " t.fecha_vencimiento, " +
											 " t.codigo_lote, "+ 
											 " t.fecha_toma,  "+
											 " t.existencia, "+
											 " t.usuario_modifica, " +
											 " t.codigo_preparacion," +
											 " t.codigo_dapa, " +
											 " apa.seccion, " +
											 " apa.subseccion, "+
											 " t.codregistroconteo," +
											 " axif.codigo, " +
											 " t.codigo_ajuste "+
											 " ORDER BY  "+
											 " seccion,subseccion,t.fecha_toma";
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarPreparaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(consultarPreparaciones);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info(mapa);
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
	 * @param cuc
	 * @return
	 */
	public static HashMap consultarUbicacionArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String consultaUbicacion="select " +
										 " dapa.ubicacion, " +
										 " getNombreSeccion(apa.seccion) AS seccion, " +
										 " getNombreSubseccion(apa.seccion, apa.subseccion) AS subseccion, " +
										 " a.descripcion || ' CONC: ' || a.concentracion || ' F.F: ' || getnomformafarmaceutica(a.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(a.codigo) as descripcion, " +
										 " dapa.articulo as articulo" +
									 " FROM " +
									 	" det_articulos_por_almacen dapa" +
									 " INNER JOIN " +
									 	" articulo a ON (dapa.articulo=a.codigo)" +
									 " INNER JOIN " +
										" articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
									 " WHERE dapa.articulo="+cuc.getCodigoArticulo()+" " +
									 		"and apa.almacen="+cuc.getAlmacen()+" " +
											" AND dapa.codigo_det_pk NOT IN (select codigo_dapa from preparacion_toma_inventario where almacen="+cuc.getAlmacen()+")"+" " +
									 " ORDER BY seccion,subseccion,ubicacion";
		
		
		logger.info("UBICACION >>>>"+consultaUbicacion);
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaUbicacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(consultaUbicacion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info(mapa);
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
	 * @param cuc
	 * @return
	 */
	public static HashMap consultarAjusteArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String consultarAjuste="select " +
										 " getNombreTransaccion(cod_transaccion) as ct," +
										 " articulo," +
										 " getNombreLote(lote) as lote," +
										 " ajuste," +
										 " getNombreAlmacen(axif.almacen) as almacen, " +
										 " a.descripcion || ' CONC: ' || a.concentracion || ' F.F: ' || getnomformafarmaceutica(a.forma_farmaceutica) || ' NAT: ' || getnaturalezaarticulo(a.codigo) as descripcion " +
									 " FROM " +
									 	" ajustes_x_inv_fisico axif" +
									 " INNER JOIN " +
									 	" articulo a ON (axif.articulo=a.codigo)" +
									 " WHERE axif.codigo="+cuc.getCodigoAjuste()+" ";
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarAjuste,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(consultarAjuste);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info(mapa);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	
	return mapa;
	}
	
	
	
	
	/**
	 * 
	 * @param mapa
	 * @return
	 */
	private static String crearCadena(HashMap mapa) {
    	String cadena = "";
    	int x;
    	for(x=0; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++){
    		cadena += mapa.get("codigo_"+x);
    		cadena += ",";
    	}
    	return cadena;
	}
}
