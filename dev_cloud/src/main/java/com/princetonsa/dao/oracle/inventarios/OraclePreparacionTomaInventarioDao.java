package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.inventarios.ArticulosPorAlmacenDao;
import com.princetonsa.dao.inventarios.PreparacionTomaInventarioDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArtPorAlmacenDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBasePreparacionTomaInvDao;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;

/**
 * @author garias@princetonsa.com
 */
public class OraclePreparacionTomaInventarioDao implements PreparacionTomaInventarioDao {
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarGrupos(Connection con, int clase)
	{
		return SqlBasePreparacionTomaInvDao.consultarGrupos(con, clase);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarSubgrupos(Connection con, int clase, int subgrupo)
	{
		return SqlBasePreparacionTomaInvDao.consultarSubgrupos(con, clase, subgrupo);
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, PreparacionTomaInventario pti)
	{
		return SqlBasePreparacionTomaInvDao.filtrarArticulos(con, pti);
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public boolean confirmarPreparacion(Connection con, PreparacionTomaInventario pti)
	{
		return SqlBasePreparacionTomaInvDao.confirmarPreparacion(con, pti);
	}
	
	/**
	 * 
	 */
	public int CodigoPreparacionMax(Connection con, PreparacionTomaInventario pti)
	{
		return SqlBasePreparacionTomaInvDao.CodigoPreparacionMax(con, pti);
	}
	
	/**
	 * 
	 */
	public String sqlListadoConteo(int codigoPreparacion, int almacen, HashMap seccionesElegidas, int subseccion)
	{
		String CondicionSeccion="", CondicionSubseccion="";
		
		if (seccionesElegidas.containsKey("cadenaCodigos") && !seccionesElegidas.get("cadenaCodigos").equals("") && !seccionesElegidas.get("cadenaCodigos").equals("null")){
        	CondicionSeccion=" AND apa.seccion IN ("+seccionesElegidas.get("cadenaCodigos")+") ";
        	if (subseccion>0){
				CondicionSubseccion=" AND apa.subseccion IN ("+subseccion+") ";
			}
	    }
		
		String sql = "SELECT " +
				        "getdescarticulosincodigo(t.codigo) as descripcion, " +
				        "t.codigo as codigo, " +
				        "getunidadmedidaarticulo(t.codigo) as unidad_medida, " +
				        "coalesce(t.codigo_interfaz,'') as codigo_interfaz, " +
				        "coalesce(t.lote,'No Aplica') as lote, " +
				        "t.\"codigo_lote\" as codigo_lote, " +
				        "coalesce(t.\"fecha_vencimiento\",'No Aplica') as fecha_vencimiento, " +
				        "t.existencias as existencias, " +
				        "t.subgrupo AS subgrupocon, " +
				        "se.descripcion || ' - ' ||  subs.descripcion AS seccion_subseccion, " +
				        "se.codigo_pk AS seccion, " +
				        "se.descripcion AS seccion_desc, " +
				        "subs.codigo_subseccion AS subseccion, " +
				        "subs.descripcion AS subseccion_desc, " +
				        "si.subgrupo as subgrupo, " +
				        "si.grupo as grupo, " +
				        "si.clase as clase, " +
				        "dapa.codigo_det_pk as codigo_det_pk, " +
				        "dapa.ubicacion as ubicacion, " +
				        "t.codigo_dapa as codigo_dapa " +
					"FROM " +
						"( " +
							"( " +
								"SELECT " +
						        	"a.descripcion," +
						        	"a.codigo," +
						        	"a.unidad_medida," +
						        	"a.codigo_interfaz," +
						        	"ex.lote," +
						        	"ex.codigo as codigo_lote," +
						        	"to_char(ex.fecha_vencimiento, 'dd/mm/yyyy') || '' as fecha_vencimiento," +
						        	"ex.existencias," +
						        	"a.concentracion," +
						        	"a.forma_farmaceutica," +
						        	"a.naturaleza," +
						        	"a.subgrupo," +
						        	"pti.codigo_dapa " +
						        "FROM " +
						        	"articulo a " +
						        "INNER JOIN " +
						        	"articulo_almacen_x_lote ex ON a.codigo=ex.articulo " +
						        "INNER JOIN " +
						        	"preparacion_toma_inventario pti ON ex.articulo=pti.articulo " +
						        "WHERE " +
						        	"a.maneja_lotes='"+ConstantesBD.acronimoSi+"' "+
						        	"AND pti.codigo_preparacion= "+codigoPreparacion+" "+
						        	"AND ex.almacen="+almacen+" " +
						    ") UNION ( " +
						        "SELECT " +
						        	"a.descripcion, " +
						        	"a.codigo, " +
						        	"a.unidad_medida," +
						        	"a.codigo_interfaz," +
						        	"'No aplica' as lote," +
						        	"-1 as codigo_lote," +
						        	"'No aplica' as fecha_vencimiento," +
						        	"ex.existencias," +
						        	"a.concentracion," +
						        	"a.forma_farmaceutica," +
						        	"a.naturaleza," +
						        	"a.subgrupo," +
						        	"pti.codigo_dapa " +
						        "FROM " +
						        	"articulos_almacen ex," +
						        	"articulo a " +
						        "INNER JOIN " +
						        	"preparacion_toma_inventario pti ON a.codigo=pti.articulo " +
						        "WHERE " +
						        	"ex.articulo=a.codigo " +
						        	"AND a.maneja_lotes='"+ConstantesBD.acronimoNo+"' "+
						        	"AND pti.codigo_preparacion= "+codigoPreparacion+" "+
						        	"AND ex.almacen="+almacen+" " +
						     ") " +
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
						"subgrupo_inventario si ON (si.codigo=t.subgrupo) " +
					"WHERE 1=1" +
						CondicionSeccion + CondicionSubseccion;
		return sql;
	}
	
} 