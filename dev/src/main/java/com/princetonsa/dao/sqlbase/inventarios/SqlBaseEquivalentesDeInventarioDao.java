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

/**
 * 
 * @author Juan Sebastian Castaño C.
 * 
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad EQUIVALENTES DE INVENTARIO
 *
 */
public class SqlBaseEquivalentesDeInventarioDao {

	private static Logger logger = Logger.getLogger(SqlBaseEquivalentesDeInventarioDao.class);
	
	
	public static final String indices [] = {"artEquiDescripcion_",
												"artEquiNaturaleza_",
												"artEquiFormaFarm_",
												"artEquiUnidadMedida_",
												"artEquiMinsalud_",
												"artEquiConcentracion_",
												"artEquiUnidMedida_", 
												"artEquiCantidad_", 
												"codEqui_", 
												"codPpal_",
												"DescripcionArtEquivalente_", 
												"eliminar_",
												"artEquiCantidadOriginal_"};
	
	public static final String consultaArtEquivalente = "SELECT getdescripcionarticulo(equi_invent.articulo_equivalente) as art_equi_descripcion, " +
																"na.es_pos as art_equi_naturaleza, " +
																"ff.nombre as art_equi_forma_farm, " +
																"um.nombre as art_equi_unidad_medida, " +
																"art.minsalud as art_equi_minsalud, " +
																"art.concentracion as art_equi_concentracion, " +
																"art.unidad_medida as art_equi_unid_medida, " +
																"equi_invent.cantidad as art_equi_cantidad, " +
																"equi_invent.articulo_equivalente as cod_equi, " +
																"equi_invent.articulo_ppal as cod_ppal, " +
																"'"+ConstantesBD.acronimoNo+"' as eliminar " +
														"FROM articulo art " +
																"INNER JOIN equivalentes_inventario equi_invent ON (art.codigo=equi_invent.articulo_ppal) " +
																"LEFT OUTER JOIN forma_farmaceutica ff ON (art.forma_farmaceutica=ff.acronimo AND art.institucion=ff.institucion) " +
																"LEFT OUTER JOIN naturaleza_articulo na ON (art.naturaleza=na.acronimo AND art.institucion=na.institucion) " +
																"LEFT OUTER JOIN unidad_medida um ON (art.unidad_medida=um.acronimo) " +
														"WHERE art.codigo = ? " +
														"ORDER BY equi_invent.articulo_equivalente ";
	
	
	public static String consultaCamposBusqeuda="SELECT a.categoria AS categoria, " +
														"a.forma_farmaceutica AS forma, " +
														"a.unidad_medida AS unidad, " +
														"na.es_pos AS natu, " +
														"sbg.subgrupo AS subgrup, " +
														"sbg.grupo AS grup, " +
														"sbg.clase AS clas " +
												"FROM articulo a " +
														"INNER JOIN subgrupo_inventario sbg ON (a.subgrupo=sbg.codigo) " +
														"INNER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " +
												"WHERE a.codigo=? ";
	
	private static String consultaDatosAd="SELECT a.codigo AS cod, " +
													"a.descripcion AS descr, " +
													"na.es_pos AS natu, " +
													"ff.nombre AS formaf, " +
													"um.nombre AS unidadm, " +
													"a.minsalud AS mins, " +
													"a.concentracion " +
											"FROM articulo a " +
													"INNER JOIN forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo AND a.institucion=ff.institucion) " +
													"INNER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " +
													"INNER JOIN unidad_medida um ON (a.unidad_medida=um.acronimo) " +
											"WHERE a.codigo=? ";
	
	/**
	 * Metodo de consulta de los articulos equivalentes de un articulo seleccionado
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	
	public static HashMap<String, Object> consultaEquivalentes (Connection con, int codigoArtPpal)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;			
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaArtEquivalente, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoArtPpal);			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de articulo equivalente");
		}
		logger.info("CONSULTA TALES>>>>>>>"+consultaArtEquivalente);		
		resultados.put("INDICES",indices);
		return resultados;
	}
	
	/**
	 * Metodo de insercion de un nuevo articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @param cantidadArtEquivalente
	 * @param usuarioModifica
	 * @return
	 */	
	public static boolean insertarArticuloEquivalente (Connection con, int codigoArtPpal, int codigoArtEquivalente, int cantidadArtEquivalente, String usuarioModifica)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			String consulta = "insert into equivalentes_inventario (" +
														"articulo_ppal," +
														"articulo_equivalente," +
														"cantidad," +
														"fecha_modifica," +
														"hora_modifica," +
														"usuario_modifica)" +
								" values (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?) ";
		
			
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoArtPpal);
			ps.setInt(2, codigoArtEquivalente);
			
			ps.setInt(3, cantidadArtEquivalente);				
			
			
			ps.setString(4, usuarioModifica);
			
			if (ps.executeUpdate() <= 0)
				return false;
			
			return true;
		}
		catch (SQLException e)
		{
			logger.warn(e+"Error SqlBaseEquivalentesDeInventarioDao "+e.toString() );
			return false;
		}
	}
	
	/**
	 * Metodo que elimina un registro de articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @return
	 */
	public static boolean eliminarArticuloEquivalente (Connection con,int codigoArtPpal, int codigoArtEquivalente)
	{
		
		try
		{
			PreparedStatementDecorator ps = null;
			String query = "delete from equivalentes_inventario  where articulo_ppal = ? and articulo_equivalente = ?";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoArtPpal);
			ps.setInt(2, codigoArtEquivalente);
			
			if (ps.executeUpdate() <= 0)
				return false;
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseEquivalentesDeInventarioDao "+e.toString() );
			return false;
		}
	
	}
	
	
	/**
	 * Metodo para modificar un registro de articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @param cantidadArtEquivalente
	 * @param usuarioModifica
	 * @return
	 */
	
	
	public static boolean modificarArticuloEquivalente (Connection con,int codigoArtPpal, int codigoArtEquivalente, int cantidadArtEquivalente, String usuarioModifica)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			String query = "update equivalentes_inventario set " +
												"cantidad=?," +
												" fecha_modifica = CURRENT_DATE, " +
												"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
												"usuario_modifica = ?  " +
												"where articulo_ppal = ? " +
												"and articulo_equivalente = ?";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, cantidadArtEquivalente);
			ps.setString(2, usuarioModifica);
			ps.setInt(3, codigoArtPpal);
			ps.setInt(4, codigoArtEquivalente);
			
			if (ps.executeUpdate() <= 0)
				return false;
			
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseEquivalentesDeInventarioDao "+e.toString() );
			return false;
		}
	}
	
	/**
	 * Metodo de Consulta de los Campos del Articulo Principal para Filtrar la Busqueda de Equivalentes
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public static HashMap<String, Object> consultaCamposBusquedaEquivalente (Connection con, int codigoArtPpal)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;			
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCamposBusqeuda, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoArtPpal);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Campos de articulo Principal para Busqeuda de Equivlanete");
		}
		return resultados;
	}
	
	/**
	 * Metodo de Consulta de Datos Adicionales del Articulo Equivalente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap<String, Object> consultaDatosAd (Connection con, int codigo)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;			
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosAd, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			logger.info("consultaDatosAd->"+consultaDatosAd+" ->"+codigo);
			pst.setInt(1, codigo);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Campos de articulo Principal para Busqeuda de Equivlanete");
		}
		return resultados;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPpal
	 * @param codigoEquivalente
	 * @return
	 */
	public static HashMap consultarArticulo(Connection con, int codigoPpal,int codigoEquivalente) {
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		String consultarArticulo = 	"SELECT " +
										"getdescarticulosincodigo(articulo_ppal) AS desc_art_principal, " +
										"getdescarticulosincodigo(articulo_equivalente) AS desc_art_equivalente, " +
										"articulo_ppal AS cod_art_principal, " +
										"articulo_equivalente AS cod_art_equivalente, " +
										"cantidad AS cantidad_equivalente " +
									"FROM " +
										"equivalentes_inventario " +
									"WHERE 1=1 ";
		
		if(codigoPpal!=ConstantesBD.codigoNuncaValido)
			consultarArticulo += " AND articulo_ppal="+codigoPpal;
		
		if(codigoEquivalente!=ConstantesBD.codigoNuncaValido)
			consultarArticulo += " AND articulo_equivalente="+codigoEquivalente;
		
		logger.info("Consulta: "+consultarArticulo);
		 
		try{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarArticulo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
			pst.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR", e);
		}
		return resultados;
	}
	
}
