package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.inventarios.ImpListaConteoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseImpListaConteoDao;
import com.princetonsa.mundo.inventarios.ImpListaConteo;



/**
 * 
 * @author lgchavez@princetonsa.com
 *
 */
public class PostgresqlImpListaConteoDao implements ImpListaConteoDao {
	

	public HashMap consultar(Connection con, ImpListaConteo a)
	{
		return SqlBaseImpListaConteoDao.consultar(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param secciones
	 * @param subseccion
	 * @param cadenaPreparada
	 * @param codigos
	 * @param almacen
	 * @param indArticulo
	 * @param ordArticulo
	 * @return
	 */
	public String sqlReporteListadoConteo(Connection con, HashMap secciones,
			int subseccion, String cadenaPreparada, HashMap codigos,
			int almacen, String indArticulo, String ordArticulo, int institucion){
		
		String CondicionSeccion="", CondicionSubseccion="";
		
		if (secciones.containsKey("cadenaCodigos") && !secciones.get("cadenaCodigos").equals("") && !secciones.get("cadenaCodigos").equals("null"))
		{
        	CondicionSeccion=" AND apa.seccion IN ("+secciones.get("cadenaCodigos")+")";
		
        	if (subseccion>0)
				CondicionSubseccion=" AND apa.subseccion IN ("+subseccion+") ";
		}
    	
    	for (int x=0; x<Integer.parseInt(codigos.get("numRegistros").toString()); x++)
    	{
    		if(x>0)
    			cadenaPreparada=cadenaPreparada.replaceFirst(","+codigos.get("codigo_"+x).toString()+",", ",-1,");
    		else
    			cadenaPreparada=cadenaPreparada.replaceFirst(""+codigos.get("codigo_"+x).toString()+",", "-1,");
    	}
		
		
		String sql = "SELECT " +
							"getdescarticulosincodigo(t.codigo) as descripcion, " +
							"t.codigo, " +
							"getunidadmedidaarticulo(t.codigo) as unidad_medida, " +
							"coalesce(t.codigo_interfaz,'') as codigo_interfaz, " +
							"coalesce(t.lote,'No Aplica') as lote, " +
							"t.codigo_lote, " +
							"coalesce(t.fecha_vencimiento,'No Aplica') as fecha_vencimiento, " +
							"t.existencias, " +
							"t.subgrupo AS subgrupocon, " +
							"se.descripcion || ' - ' ||  subs.descripcion AS seccion_subseccion, " +
							"se.codigo_pk AS seccion, " +
							"se.descripcion AS seccion_desc, " +
							"subs.codigo_subseccion AS subseccion, " +
							"subs.descripcion AS subseccion_desc, " +
							"si.subgrupo, " +
							"si.grupo, " +
							"si.clase, " +
							"dapa.codigo_det_pk, " +
							"dapa.ubicacion, " +
							"t.codigo_dapa " +
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
										"pti.codigo_dapa " +
									"FROM " +
										"articulo a " +
									"INNER JOIN " +
										"articulo_almacen_x_lote ex ON a.codigo=ex.articulo " +
									"INNER JOIN " +
										"preparacion_toma_inventario pti ON ex.articulo=pti.articulo " +
									"WHERE " +
										"a.maneja_lotes='"+ConstantesBD.acronimoSi+"' "+
										"AND pti.articulo IN ("+cadenaPreparada+ConstantesBD.codigoNuncaValido+") "+
										"AND pti.estado='PEN' and pti.almacen="+almacen+" and ex.almacen="+almacen+" " +
								") UNION ( " +
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
										"pti.codigo_dapa " +
									"FROM " +
										"articulos_almacen ex, " +
										"articulo a " +
									"INNER JOIN " +
										"preparacion_toma_inventario pti ON a.codigo=pti.articulo " +
									"WHERE " +
										"ex.articulo=a.codigo " +
										"AND a.maneja_lotes='N' " +
										"AND pti.articulo IN ("+cadenaPreparada+ConstantesBD.codigoNuncaValido+") "+
										"AND pti.estado='PEN' and pti.almacen="+almacen+" and ex.almacen="+almacen+" " + 
								")" +
							") t " +
						"INNER JOIN " +
							"det_articulos_por_almacen dapa ON (dapa.articulo=t.codigo AND t.codigo_dapa=dapa.codigo_det_pk) " +
						"INNER JOIN " +
							"articulos_por_almacen apa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
						"INNER JOIN " +
							"secciones se ON (se.codigo_pk = apa.seccion) " +
						"INNER JOIN " +
							"subsecciones subs ON (subs.codigo_pk_seccion=apa.seccion AND subs.codigo_subseccion = apa.subseccion) " +
						"INNER JOIN " +
							"subgrupo_inventario si ON (si.codigo=t.subgrupo) ";
					        
		if (indArticulo.equals(ConstantesBD.acronimoSi)){
			sql += 		"INNER JOIN " +
							"preparacion_toma_inventario pti on (t.codigo=pti.articulo and pti.codigo_dapa=dapa.codigo_det_pk and pti.seccion=se.codigo_pk AND pti.subseccion = subs.codigo_subseccion) "+ 
						"LEFT OUTER JOIN " +
							"registro_conteo_inventario rci ON (pti.articulo=rci.articulo  AND rci.codigo_preparacion=pti.codigo AND rci.seccion=pti.seccion AND rci.subseccion = pti.subseccion) ";
		}
		
		sql += 			"WHERE 1=1 ";
							
		if (indArticulo.equals(ConstantesBD.acronimoSi)){		
			sql +=			"(rci.ind_diferencia_conteo='"+ConstantesBD.acronimoSi+"' OR (rci.ind_diferencia_conteo IS NULL AND rci.numero_conteo<"+ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(institucion)+")) "+
							"AND  t.codigo in ("+cadenaPreparada+"-1) " +
							//" AND pti.estado='PEN' " +
							"AND (rci.numero_conteo=1 OR rci.numero_conteo IS NULL) ";
		}
							
		sql += 				CondicionSeccion+
							CondicionSubseccion+
						"ORDER BY " +
							"seccion_desc, " +
							"subseccion_desc, " +
							ordArticulo;
		
		return sql;
	}
}