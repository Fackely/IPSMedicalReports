package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.ImpListaConteo;
/**
 * 
 * @author lgchavez@princetonsa.com
 *
 */
public class SqlBaseImpListaConteoDao {
	
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
	
	private static String consultarListaconteoStr="SELECT " +
														"* FROM preparacion_toma_inventario pti " +
														"INNER JOIN conteo_toma_inventario cti " +
														"ON (pti.articulo=cti.articulo) " +
														"WHERE " +
															"pti.estado=PEN AND " +
															"and cti.estado=PEN ";
	
		
	
	// --------------- METODOS
	
		public static HashMap consultar(Connection con, ImpListaConteo a){
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena = consultarListaconteoStr;
		
		if(a.getCentroAtencion()>0)
			cadena+= " AND pti.centro_atencion= "+a.getCentroAtencion();
		if(a.getAlmacen()>0)
			cadena+= " AND pti.almacen = "+a.getAlmacen();
		if(a.getSeccionestemp()!=null)
			cadena+= " AND pti.seccion IN ( "+a.getSeccionestemp("cadenaCodigos")+")";
		if(a.getSubseccion()>0)
			cadena+= " AND pti.subseccion = "+a.getSubseccion();
		if(a.getClase()>0)
			cadena+= " AND pti.clase ="+a.getClase();
		if(a.getGrupo()>0)
			cadena+= " AND pti.grupo ="+a.getGrupo();
		if(a.getSubgrupo()>0)
			cadena+= " AND pti.subgrupo ="+a.getSubgrupo();
		if(a.getArticulosMap()!=null)
			cadena+= " AND pti.articulo IN ("+a.getCodigosArticulosInsertados()+")";
		
			
		cadena+=" ORDER BY "+a.getOrdArticulo();
		
		logger.info("CONSULTA >>>"+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			//mapa.put("INDICES_MAPA",indicesMap);	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
			
		return mapa;
		
	}
	
}