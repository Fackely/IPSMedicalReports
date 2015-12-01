package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.inventarios.RegistroConteoInventarioDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBasePreparacionTomaInvDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseRegistroConteoInventarioDao;
import com.princetonsa.mundo.inventarios.RegistroConteoInventario;




public class PostgresqlRegistroConteoInventarioDao implements RegistroConteoInventarioDao{
	
	
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, RegistroConteoInventario pti)
	{
		return SqlBaseRegistroConteoInventarioDao.filtrarArticulos(con, pti);
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public boolean guardarConteo(Connection con, RegistroConteoInventario pti)
	{
		return SqlBaseRegistroConteoInventarioDao.guardarConteo(con, pti);
	}
	
	/**
	 * 
	 */
	public boolean anularConteos(Connection con, RegistroConteoInventario pti, String estado)
	{
		return SqlBaseRegistroConteoInventarioDao.anularConteos(con, pti, estado);
	}
	
	/**
	 * 
	 * @param secciones
	 * @param subseccion
	 * @param cadenaPreparada
	 * @param almacen
	 * @param ordArticulo
	 * @param codigoInstitucion
	 * @return
	 */
	public String sqlReporteRegistroConteoInventario(HashMap secciones,
			int subseccion, String cadenaPreparada, int almacen,
			String ordArticulo, int codigoInstitucion){
		
		String condicionSeccion="", condicionSubseccion="";
		
		if (secciones.containsKey("cadenaCodigos") && !secciones.get("cadenaCodigos").equals("") && !secciones.get("cadenaCodigos").equals("null"))
        {
        	condicionSeccion=" AND apa.seccion IN ("+secciones.get("cadenaCodigos")+") ";
        	if (subseccion>0)
				condicionSubseccion=" AND apa.subseccion IN ("+subseccion+") ";
		}
		
		String sql = "SELECT 	" +
							"getdescarticulosincodigo(t.codigo) as descripcion, " +
							"t.codigo, " +
							"getunidadmedidaarticulo(t.codigo) as unidad_medida, " +
							"t.codigo_interfaz, " +
							"coalesce(t.lote,'No aplica') as lote, " +
							"coalesce(t.fecha_vencimiento,'No aplica') as fecha_vencimiento, " +
							"t.codigo_lote, " +
							"t.existencias, " +
							"t.concentracion, " +
							"t.forma_farmaceutica, " +
							"t.naturaleza, " +
							"t.subgrupo AS subgrupocon, " +
							"upper (se.descripcion || ' - ' || subs.descripcion) AS seccion_subseccion, " +
							"se.codigo_pk AS seccion, " +
							"subs.codigo_subseccion AS subseccion, " +
							"si.subgrupo, " +
							"si.grupo, " +
							"si.clase, " +
							"dapa.codigo_det_pk, " +
							"t.codigo_preparacion, " +
							"dapa.ubicacion, " +
							"t.codigo_dapa,max(rci.numero_conteo) AS conteo, " +
							"getCantRegistroConteo(t.codigo_preparacion, max(rci.numero_conteo)) AS cantidad " +
						"FROM " +
							"( " +
								"( " +
									"SELECT " +
										"a.descripcion, " +
										"a.codigo, " +
										"a.unidad_medida, " +
										"a.codigo_interfaz, " +
										"ex.lote, " +
										"ex.codigo as codigo_lote, " +
										"to_char(ex.fecha_vencimiento, 'dd/mm/yyyy') || '' as fecha_vencimiento, " +
										"ex.existencias, " +
										"a.concentracion, " +
										"a.forma_farmaceutica, " +
										"a.naturaleza, " +
										"a.subgrupo, " +
										"pti.codigo as codigo_preparacion, " +
										"pti.codigo_dapa " +
									"FROM " +
										"articulo a " +
									"INNER JOIN " +
										"articulo_almacen_x_lote ex ON a.codigo=ex.articulo " +
									"INNER JOIN " +
										"preparacion_toma_inventario pti ON (ex.articulo=pti.articulo and pti.lote=ex.codigo) " +
									"WHERE " +
										"a.maneja_lotes='"+ConstantesBD.acronimoSi+"' "+
										"AND pti.articulo IN (-1"+cadenaPreparada+"-1) "+
										"AND ex.almacen="+almacen+" "+
										"AND pti.estado='PEN' " +
								")  UNION  ( " +
									"SELECT " +
										"a.descripcion, " +
										"a.codigo, " +
										"a.unidad_medida, " +
										"a.codigo_interfaz, " +
										"'No aplica' as lote, " +
										"-1 as codigo_lote, " +
										"'No aplica' as fecha_vencimiento, " +
										"ex.existencias, " +
										"a.concentracion, " +
										"a.forma_farmaceutica, " +
										"a.naturaleza, " +
										"a.subgrupo, " +
										"pti.codigo as codigo_preparacion, " +
										"pti.codigo_dapa " +
									"FROM " +
										"articulos_almacen ex, " +
										"articulo a " +
									"INNER JOIN " +
										"preparacion_toma_inventario pti ON a.codigo=pti.articulo " +
									"WHERE " +
										"ex.articulo=a.codigo " +
										"AND a.maneja_lotes='N' "+
										"AND pti.articulo IN ("+cadenaPreparada+"-1) "+
										"AND ex.almacen="+almacen+" "+
										"AND pti.estado='PEN' " +
								") " +
							") t " +
						"INNER JOIN " +
							"det_articulos_por_almacen dapa ON(dapa.articulo=t.codigo and dapa.codigo_det_pk=t.codigo_dapa) " +
						"INNER JOIN " +
							"articulos_por_almacen apa ON(apa.codigo_pk = dapa.codigo_art_por_almacen) " +
						"INNER JOIN " +
							"secciones se ON (se.codigo_pk = apa.seccion) " +
						"INNER JOIN " +
							"subsecciones subs ON(subs.codigo_pk_seccion=apa.seccion AND subs.codigo_subseccion = apa.subseccion) " +
						"INNER JOIN " +
							"subgrupo_inventario si on(si.codigo=t.subgrupo) " +
						"INNER JOIN " +
							"preparacion_toma_inventario pti on (t.codigo=pti.articulo and pti.codigo_dapa=dapa.codigo_det_pk and pti.seccion=se.codigo_pk AND pti.subseccion = subs.codigo_subseccion) " +
						"INNER JOIN " +
							"registro_conteo_inventario rci ON (pti.articulo=rci.articulo  AND rci.codigo_preparacion=pti.codigo AND rci.seccion=pti.seccion AND rci.subseccion = pti.subseccion) " +
						"WHERE 	" +
							"pti.estado='PEN' " +
							condicionSeccion +
							condicionSubseccion +
							"AND pti.almacen="+almacen+" "+"AND t.codigo IN (-1,"+cadenaPreparada+"-1) "+
						"GROUP BY " +
							"t.descripcion, " +
							"t.codigo, " +
							"t.unidad_medida, " +
							"t.codigo_interfaz, " +
							"t.lote, " +
							"t.codigo_lote, " +
							"t.fecha_vencimiento, " +
							"t.existencias, " +
							"t.concentracion, " +
							"t.forma_farmaceutica, " +
							"t.naturaleza, " +
							"t.subgrupo, " +
							"seccion_subseccion," +
							"se.codigo_pk, " +
							"subs.codigo_subseccion, " +
							"si.subgrupo, " +
							"si.grupo, " +
							"si.clase, " +
							"dapa.codigo_det_pk, " +
							"t.codigo_preparacion, " +
							"dapa.ubicacion, " +
							"t.codigo_dapa " +
						"ORDER BY " +
							"t.descripcion";
		return sql;
		
	}
}