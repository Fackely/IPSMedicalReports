package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

public class SqlBaseComparativoUltimoConteoDao {
	
	// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	/**
	 * Consulta todos los grupos segun el codigo de una clase
	 */
	private static String consultarGruposStr= "SELECT codigo, nombre FROM grupo_inventario WHERE clase = ?";
	
	/**
	 * Consulta todos los subgrupos segun el codigo del clase y el codigo del grupo
	 */
	private static String consultarSubgruposStr= "SELECT codigo, nombre FROM subgrupo_inventario WHERE clase = ? AND grupo = ?";
	
	/**
	 * Consulta para filtrar articulos segun lo parametros dados
	 */
	private static String filtrarArticulosStr = "SELECT DISTINCT " +
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
	
	private static String consultarConteosArticulo = "SELECT distinct " +
									"getdescarticulosincodigo(t.codigo) as descripcion, " +
									"t.codigo, " +
									"t.unidad_medida, " +
									" coalesce(t.codigo_interfaz,'') as codigo_interfaz, " +
									" coalesce(t.lote,'No aplica') as lote,  "+
									" coalesce(t.fecha_vencimiento,'No aplica') as fecha_vencimiento, " +
									"t.fecha_toma, " +
									"t.existencia, " +
									"t.codigo_dapa, " +
									"t.conteo, " +
									"t.usuario_responsable, " +
									"getCantRegistroConteo(t.cod_prep, t.conteo) AS cant_conteo, " +
									"t.fecha_reg_conteo, " +
									"t.usuario_finaliza, " +
									"t.estado, " +
									"getNombreSeccion(apa.seccion) AS seccion, " +
									"getNombreSubseccion(apa.seccion, apa.subseccion) AS subseccion, " +
									"dapa.ubicacion " +
								"FROM " +
								"( " +
									"(SELECT distinct " +
										"a.descripcion, " +
										"a.codigo, " +
										"a.unidad_medida, " +
										"a.codigo_interfaz, " +
										"l.lote, " +
										"l.codigo as codigo_lote, " +
										"to_char(l.fecha_vencimiento, 'dd/mm/yyyy') || '' as fecha_vencimiento, " +
										"a.concentracion, " +
										"a.forma_farmaceutica, " +
										"a.naturaleza, " +
										"a.subgrupo, " +
										"pti.codigo AS cod_prep, " +
										"pti.fecha_toma, " +
										"pti.existencia, " +
										"pti.codigo_dapa, " +
										"rci.numero_conteo AS conteo, " +
										"rci.usuario_responsable, " +
										"rci.fecha_modifica || ' / ' || rci.hora_modifica AS fecha_reg_conteo, " +
										"rci.usuario_finaliza, " +
										"rci.estado " +
									"FROM " +
										"articulo_almacen_x_lote l " +
									"INNER JOIN " +
										"articulo a ON(a.codigo=l.articulo) " +
									"INNER JOIN " +
										"preparacion_toma_inventario pti ON (pti.articulo=a.codigo  and l.codigo=pti.lote) " +
									"INNER JOIN " +
										"registro_conteo_inventario rci ON rci.codigo_preparacion=pti.codigo " +
									"WHERE " +
										"l.almacen = ? " +
										"AND a.maneja_lotes='S' " +
										"AND rci.articulo =? "+
										"AND l.codigo=? "+
										"AND pti.estado='PEN' " +
									"GROUP BY " +
										"a.descripcion, " +
										"a.codigo, " +
										"a.unidad_medida, " +
										"a.codigo_interfaz, " +
										"l.lote, " +
										"l.codigo, " +
										"l.fecha_vencimiento, " +
										"a.concentracion, " +
										"a.forma_farmaceutica, " +
										"a.naturaleza, " +
										"a.subgrupo, " +
										"pti.codigo, " +
										"pti.fecha_toma, " +
										"pti.existencia, " +
										"pti.codigo_dapa, " +
										"rci.numero_conteo, " +
										"rci.usuario_responsable, " +
										"rci.fecha_modifica,rci.hora_modifica, " +
										"rci.usuario_finaliza, " +
										"rci.estado " +
									") " +
								"UNION ALL " +
									"(SELECT " +
										"a.descripcion, " +
										"a.codigo, " +
										"a.unidad_medida, " +
										"a.codigo_interfaz, " +
										"'No aplica' as lote, " +
										"-1 as codigo_lote, " +
										"'No aplica' as fecha_vencimiento, " +
										"a.concentracion, " +
										"a.forma_farmaceutica, " +
										"a.naturaleza, " +
										"a.subgrupo, " +
										"pti.codigo AS cod_prep, " +
										"pti.fecha_toma, " +
										"pti.existencia, " +
										"pti.codigo_dapa, " +
										"rci.numero_conteo AS conteo, " +
										"rci.usuario_responsable, " +
										"rci.fecha_modifica || ' / ' || rci.hora_modifica AS fecha_reg_conteo, " +
										"rci.usuario_finaliza, " +
										"rci.estado " +
									"FROM " +
										"articulos_almacen ex " +
									"INNER JOIN " +
										"articulo a ON(a.codigo=ex.articulo) " +
									"INNER JOIN " +
										"preparacion_toma_inventario pti ON(pti.articulo=a.codigo) " +
									"INNER JOIN " +
										"registro_conteo_inventario rci ON(rci.codigo_preparacion=pti.codigo) " +
									"WHERE " +
										"ex.almacen =? "+
										"AND a.maneja_lotes = 'N' " +
										"AND rci.articulo =? "+
										"AND pti.estado='PEN' " +
									"GROUP BY " +
										"a.descripcion, " +
										"a.codigo, " +
										"a.unidad_medida, " +
										"a.codigo_interfaz, " +
										"a.concentracion, " +
										"a.forma_farmaceutica, " +
										"a.naturaleza, " +
										"a.subgrupo, " +
										"pti.codigo, " +
										"pti.fecha_toma, " +
										"pti.existencia, " +
										"pti.codigo_dapa, " +
										"rci.numero_conteo, " +
										"rci.usuario_responsable, " +
										"rci.fecha_modifica,rci.hora_modifica, " +
										"rci.usuario_finaliza, " +
										"rci.estado " +
								")) t " +
								"INNER JOIN " +
									"det_articulos_por_almacen dapa ON(dapa.articulo=t.codigo and dapa.codigo_det_pk=t.codigo_dapa) " +
								"INNER JOIN " +
									"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
								"INNER JOIN " +
									"subgrupo_inventario si ON (si.codigo=t.subgrupo)" +
								" ORDER BY seccion, subseccion, descripcion, t.codigo, t.unidad_medida, codigo_interfaz, lote, fecha_vencimiento, t.fecha_toma, t.existencia, t.codigo_dapa, t.conteo, t.usuario_responsable, cant_conteo, t.fecha_reg_conteo, t.usuario_finaliza, t.estado,dapa.ubicacion " +
								"";
									
	// --------------- METODOS
	
	/** 
	 * Consulta todos los grupos segun una el codigo de clase
	 * @param con
	 * @param seccion
	 * @return
	 */	
	public static HashMap consultarGrupos(Connection con, int clase){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/** 
	 * Consulta todos los subgrupos segun el codigo del clase y el codigo del grupo
	 * @param con
	 * @param seccion
	 * @return
	 */	
	public static HashMap consultarSubgrupos(Connection con, int clase, int grupo){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarSubgruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ps.setInt(2, grupo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	public static HashMap filtrarArticulos(Connection con, ComparativoUltimoConteo cuc){
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String filtro = filtrarArticulosStr;
		String codigosFiltrados;

		try
		{
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
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(filtro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				
				codigosFiltrados = crearCadena(mapa);
				
			} 
			else {
				filtro = cuc.getCodigosArticulos()+",-1";
			}
			
			String consultaArticulosPreparados="SELECT articulo as codigo from preparacion_toma_inventario where articulo IN ("+filtro+"-1) AND estado='PEN'";
			PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaArticulosPreparados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap codi=new HashMap();
			codi.put("numRegistros", "0");
			String codigosPreparados=crearCadena(codi=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery())));
			
			String consultarArticulosStr = "SELECT " +
												"t.descripcion AS descripcion, " +
												"t.codigo, " +
												"getunidadmedidaarticulo(t.codigo) as unidad_medida, " +
												"  coalesce(t.codigo_interfaz,'') as codigo_interfaz, " +
												 " coalesce(t.lote,'No aplica') as lote,  "+
												 " coalesce(t.fecha_vencimiento,'No aplica') as fecha_vencimiento, " +
												"t.conteo as conteo, " +
												"t.codigo_lote, " +
												"t.existencia, " +
												"t.fecha_toma," +
												//"t.ubicacion," +
												"SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) AS cant_conteo," +
												"SUM(getCantRegistroConteo(t.cod_prep, t.conteo))-t.existencia AS diferencia " +
											"FROM ( " +
												"SELECT " +
													"getdescarticulosincodigo(y.codigo) AS descripcion," +
													"y.codigo, " +
													"y.unidad_medida, " +
													"y.codigo_interfaz, " +
													"y.lote, " +
													"y.codigo_lote, "+
													"y.fecha_vencimiento || '' as fecha_vencimiento, " +
													"MAX(y.conteo) as conteo, " +
													"y.codigo_dapa, " +
													//"getobtenerUbicacionArticulo(y.codigo_dapa) as ubicacion," +
													"MAX(getExistenciasArticulo(y.codigo,y.cod_prep )) as existencia," +
													"MAX(getFechaTomaPreparacion(y.codigo,y.cod_prep )) as fecha_toma, " +
													"y.cod_prep " +
												"FROM " +
													"((" +
														"SELECT " +
															"a.descripcion, " +
															"a.codigo," +
															"a.unidad_medida," +
															"a.codigo_interfaz," +
															"l.lote," +
															"l.codigo as codigo_lote," +
															"to_char(l.fecha_vencimiento, 'dd/mm/yyyy') as fecha_vencimiento," +
															"a.concentracion," +
															"getnomformafarmaceutica(a.forma_farmaceutica) as forma_farmaceutica, " +
															"getnaturalezaarticulo(a.codigo) as naturaleza, " +
															"a.subgrupo," +
															"pti.codigo_dapa," +
															"rci.numero_conteo AS conteo," +
															"MAX(rci.codigo_preparacion) AS cod_prep " +
														"FROM " +
															"articulo a," +
															"articulo_almacen_x_lote l " +
														"INNER JOIN " +
															"preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote)" +
														"INNER JOIN " +
															"registro_conteo_inventario rci ON (rci.articulo=pti.articulo and rci.codigo_preparacion = pti.codigo)" +
														"WHERE " +
															"a.codigo=l.articulo " +
															"AND a.maneja_lotes='S' "+
															"AND a.codigo IN ("+filtro+") " +
															"AND pti.estado = 'PEN' " +
														"GROUP BY " +
															"a.descripcion, " +
															"a.codigo, " +
															"a.unidad_medida, " +
															"a.codigo_interfaz, " +
															"l.lote, " +
															"l.codigo, " +
															"l.fecha_vencimiento, " +
															"a.concentracion, " +
															"a.forma_farmaceutica, " +
															"a.naturaleza, " +
															"a.subgrupo, " +
															"rci.numero_conteo, " +
															"pti.codigo_dapa " +
														")" +
													"UNION ALL" +
														"(" +
														"SELECT " +
															"a.descripcion, " +
															"a.codigo, " +
															"a.unidad_medida, " +
															"a.codigo_interfaz, " +
															"'No aplica' AS lote, " +
															"-1 as codigo_lote, " +
															"'No aplica' AS fecha_vencimiento, " +
															"a.concentracion, " +
															"getnomformafarmaceutica(a.forma_farmaceutica) as forma_farmaceutica, " +
															"getnaturalezaarticulo(a.codigo) as naturaleza, " +
															"a.subgrupo, " +
															"pti.codigo_dapa, " +
															"rci.numero_conteo AS conteo, " +
															"MAX(rci.codigo_preparacion) AS cod_prep " +
														"FROM " +
															"articulos_almacen ex " +
														"INNER JOIN " +
															"articulo a ON(a.codigo=ex.articulo) " +
														"INNER JOIN " +
															"preparacion_toma_inventario pti ON(pti.articulo=a.codigo) " +
														"INNER JOIN " +
															"registro_conteo_inventario rci ON(rci.articulo=pti.articulo and rci.codigo_preparacion = pti.codigo) " +
														"WHERE " +
															"ex.almacen = "+cuc.getAlmacen()+" " +
															"AND a.maneja_lotes = 'N' " +
															"AND a.codigo IN ("+filtro+") " +
															"AND pti.estado = 'PEN' " +
														"GROUP BY " +
															"a.descripcion, " +
															"a.codigo, " +
															"a.unidad_medida, " +
															"a.codigo_interfaz, " +
															"a.concentracion, " +
															"a.forma_farmaceutica, " +
															"a.naturaleza, " +
															"a.subgrupo, " +
															"rci.numero_conteo, " +
															"pti.codigo_dapa " +
														")) y " +
													"GROUP BY " +
														"y.descripcion, " +
														"y.concentracion, " +
														"y.forma_farmaceutica, " +
														"y.naturaleza, " +
														"y.codigo, " +
														"y.unidad_medida, " +
														"y.codigo_interfaz, " +
														"y.lote, " +
														"y.codigo_lote, " +
														"y.fecha_vencimiento, " +
														"y.cod_prep, " +
														"y.codigo_dapa " +
													//	"ubicacion " +
													") t " +
												"GROUP BY " +
													"t.descripcion, " +
													"t.codigo, " +
													"t.unidad_medida, " +
													"t.codigo_interfaz, " +
													"t.lote, " +
													"t.codigo_lote, " +
													"t.fecha_vencimiento, " +
													"t.conteo, " +
													"t.existencia, " +
													"t.fecha_toma " +
												//	"t.ubicacion " +
												"ORDER BY " + 
													cuc.getPatronOrdenar();
			
			logger.info("CONSULTA: >> "+consultarArticulosStr);
			
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
			
			mapa.put("codigos_preparados", codigosPreparados);
			mapa.put("SqlfiltroArticulosPreparados", filtro);

		}
		catch (SQLException e)
		{
			logger.error("ERROR ", e);
		}
		return mapa;
	}
	
	public static HashMap consultarConteosArticulo(Connection con, ComparativoUltimoConteo cuc){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{

			logger.info("---consulta--->\n"+consultarConteosArticulo);
			logger.info("almacen -->"+cuc.getAlmacen());
			logger.info("getCodigoArticulo -->"+cuc.getCodigoArticulo());
			logger.info("getCodigoLote -->"+cuc.getCodigoLote());
			logger.info("getAlmacen -->"+cuc.getAlmacen());
			logger.info("getCodigoArticulo -->"+cuc.getCodigoArticulo());

			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarConteosArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cuc.getAlmacen());
			ps.setInt(2, cuc.getCodigoArticulo());
			ps.setString(3, cuc.getCodigoLote());
			ps.setInt(4, cuc.getAlmacen());
			ps.setInt(5, cuc.getCodigoArticulo());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("444444444444>>>>>>>>>>>>>>" + mapa);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	return mapa;
	}
	

	private static String crearCadena(HashMap mapa) {
    	String cadena = "";
    	int x;
    	for(x=0; x<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); x++){
    		cadena += mapa.get("codigo_"+x);
    		cadena += ",";
    	}
    	return cadena;
	}
}