package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ComparativoUltimoConteoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseComparativoUltimoConteoDao;
import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

/**
 * @author garias@princetonsa.com
 */
public class PostgresqlComparativoUltimoConteoDao implements ComparativoUltimoConteoDao {
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, ComparativoUltimoConteo cuc)
	{
		return SqlBaseComparativoUltimoConteoDao.filtrarArticulos(con, cuc);
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap consultarConteosArticulo(Connection con, ComparativoUltimoConteo cuc)
	{
		return SqlBaseComparativoUltimoConteoDao.consultarConteosArticulo(con, cuc);
	}
	
	/**
	 * 
	 * @param articulos
	 * @param almacen
	 * @return
	 */
	public String sqlComparativoUltimoConteo(String articulos, int almacen)
	{
		String sql = "SELECT " +
						"t.descripcion AS descripcion, " +
						"t.codigo AS codigo, " +
						"getNomUnidadMedida(t.unidad_medida) as unidad_medida, " +
						"t.codigo_interfaz AS codigo_interfaz, "+  
					    "coalesce(t.lote,'No aplica') as lote, " +
					    "coalesce(t.fecha_vencimiento,'No aplica') as fecha_vencimiento, " +
					    "t.existencia AS existencia, " +
					    "t.fecha_toma AS fecha_toma, "+
						"t.conteo AS conteo, " +
						"t.costo_promedio AS costo_promedio, " +
						"t.codigo_lote AS codigo_lote, "+
						"MAX(getCodRegistroConteoInv(t.conteo, t.cod_prep)) AS cod_conteo, " +
						"SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) AS cant_conteo, " +
						"(SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) - t.existencia) AS cant_ajuste, " +
						"(SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) - t.existencia) * t.costo_promedio AS costo_total " +
					"FROM " +
						"( " +
							"SELECT " +
								"getdescarticulosincodigo(y.codigo) AS descripcion, " +
								"y.codigo as codigo, " +
								"y.unidad_medida as unidad_medida, " +
								"y.codigo_interfaz as codigo_interfaz, " +
								"y.lote as lote, " +
								"y.fecha_vencimiento || '' as fecha_vencimiento, " +
								"y.costo_promedio as costo_promedio, " +
								"y.codigo_lote as codigo_lote, " +
								"max(y.conteo) as conteo, " +
								"y.codigo_dapa as codigo_dapa, " +
								"max (getExistenciasArticulo(y.codigo,y.cod_prep )) as existencia, " +
								"MAX(getFechaTomaPreparacion(y.codigo,y.cod_prep )) as fecha_toma, " +
								"y.cod_prep as cod_prep " +
							"FROM " +
								"( " +
									"( " +
										"SELECT " +
											"a.descripcion, " +
											"a.codigo, " +
											"a.unidad_medida, " +
											"a.codigo_interfaz, " +
											"l.lote, " +
											"l.fecha_vencimiento, " +
											"l.codigo as codigo_lote, " +
											"a.concentracion, " +
											"a.forma_farmaceutica, " +
											"a.naturaleza, " +
											"a.costo_promedio, " +
											"pti.codigo_dapa, " +
											"rci.numero_conteo AS conteo, " +
											"MAX(rci.codigo_preparacion) AS cod_prep " +
										"FROM " +
											"articulo a, " +
											"articulo_almacen_x_lote l " +
										"INNER JOIN " +
											"preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote) " +
										"INNER JOIN " +
											"registro_conteo_inventario rci ON (rci.articulo=pti.articulo and rci.codigo_preparacion = pti.codigo) " +
										"WHERE " +
											"a.codigo=l.articulo and a.maneja_lotes='S' " +
											"AND pti.estado = 'PEN' " +
											"AND rci.almacen="+almacen+" "+ 
											"AND a.codigo in ("+articulos+")  " +
										"GROUP BY " +
											"a.descripcion, " +
											"a.codigo, " +
											"a.unidad_medida, " +
											"a.codigo_interfaz, " +
											"l.lote, " +
											"l.fecha_vencimiento, " +
											"codigo_lote, " +
											"a.concentracion, " +
											"a.forma_farmaceutica, " +
											"a.naturaleza, " +
											"a.costo_promedio, " +
											"pti.codigo_dapa, " +
											"conteo " +
									") " +
									"UNION ALL " +
									"( " +
										"SELECT " +
											"a.descripcion, " +
											"a.codigo, " +
											"a.unidad_medida, " +
											"a.codigo_interfaz, " +
											"'No aplica' as lote, " +
											"NULL as fecha_vencimiento, " +
											"-1 as codigo_lote," +
											"a.concentracion, " +
											"a.forma_farmaceutica, " +
											"a.naturaleza, " +
											"a.costo_promedio, " +
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
											"a.maneja_lotes = 'N' " +
											"AND pti.estado = 'PEN' " +
											"AND ex.almacen="+almacen+" "+ 
											"AND a.codigo in ("+articulos+") " +
										"GROUP BY " +
											"a.descripcion, " +
											"a.codigo, " +
											"a.unidad_medida, " +
											"a.codigo_interfaz, " +
											"a.concentracion, " +
											"a.forma_farmaceutica, " +
											"a.naturaleza, " +
											"a.costo_promedio, " +
											"pti.codigo_dapa, " +
											"conteo " +
										") " +
									") y " +
								"GROUP BY " +
									"y.descripcion, " +
									"y.concentracion, " +
									"y.forma_farmaceutica, " +
									"y.naturaleza, " +
									"y.codigo, " +
									"y.unidad_medida, " +
									"y.codigo_interfaz, " +
									"y.lote, " +
									"y.fecha_vencimiento, " +
									"y.cod_prep, " +
									"y.codigo_dapa, " +
									"y.costo_promedio, " +
									"y.codigo_lote " +
							") t " +
						"GROUP BY " +
							"t.descripcion, " +
							"t.codigo, " +
							"t.unidad_medida, " +
							"t.codigo_interfaz, " +
							"t.lote, " +
							"t.existencia, " +
							"t.fecha_toma, " +
							"t.costo_promedio, " +
							"t.conteo, " +
							"t.codigo_lote, " +
							"t.fecha_vencimiento " +
						"ORDER BY " +
							"t.descripcion";
		return sql;
	}
} 