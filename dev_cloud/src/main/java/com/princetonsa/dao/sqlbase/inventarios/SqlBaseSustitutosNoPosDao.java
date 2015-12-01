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
import com.princetonsa.mundo.inventarios.SustitutosNoPos;


public class SqlBaseSustitutosNoPosDao
{
	
private static Logger logger = Logger.getLogger(SqlBaseEquivalentesDeInventarioDao.class);
	
	
private static String [] indicesMap={"articulo_ppal_",
										"articuloSustituto_",
										"nombreArtSus_",
										"institucion_",
										"dosificacion_",
										"dosisDiaria_",
										"tiempoTratamiento_",
										"numDosisEquivalentes_",
										"usuario_modifica_",
										"fecha_modifica_",
										"hora_modifica_",
										"puedoEliminar_",
										"umedida_",
										"ffarma_",
										"concentracion_",
										"clase_",
										"grupo_",
										"nomsg_"
										};

private static String [] indicesMapCG={"subgrupo","grupo","clase","nombreg","nombrec","descart","nombresbg"};
	
private static String consultaStr="SELECT s.articulo_ppal, " +
											"s.articulo_sustituto, " +
											"s.dosificacion, " +
											"s.dosis_diaria, " +
											"s.tiempo_tratamiento, " +
											"s.num_dosis_equivalentes, " +
											"getdescripcionarticulo(articulo_sustituto) AS nombre_art_sus, " +
											"um.nombre AS umedida, " +
											"ff.nombre AS ffarma, " +
											"a.concentracion, " +
											"UPPER(ci.nombre) AS clase, " +
											"UPPER(gi.nombre) AS grupo, " +
											"UPPER(sg.nombre) AS nomsg " +
									"FROM " +
											"sustitutos_nopos s " +
											"INNER JOIN articulo a ON (s.articulo_sustituto=a.codigo) " +
											"INNER JOIN subgrupo_inventario sg ON (a.subgrupo=sg.codigo) " +
											"INNER JOIN grupo_inventario gi ON (sg.grupo=gi.codigo AND sg.clase=gi.clase) " +
											"INNER JOIN clase_inventario ci ON (gi.clase=ci.codigo) " +
											"INNER JOIN unidad_medida um ON (a.unidad_medida=um.acronimo) " +
											"INNER JOIN forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo AND a.institucion=ff.institucion) " +
									"WHERE s.articulo_ppal=? " +
									"ORDER BY nombre_art_sus ";


private static String eliminarStr="DELETE FROM sustitutos_nopos WHERE articulo_sustituto=?";


private static String modificarStr="UPDATE sustitutos_nopos " +
									"SET dosificacion=?, " +
										"dosis_diaria=?, " +
										"tiempo_tratamiento=?, " +
										"num_dosis_equivalentes=?, " +
										"institucion=?, " +
										"usuario_modifica=?, " +
										"fecha_modifica=CURRENT_DATE, " +
										"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
									"WHERE " +
										"articulo_ppal=? AND " +
										"articulo_sustituto=?";

private static String insertarStr="INSERT INTO sustitutos_nopos VALUES (?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
																	
	/**
	 * Metodo de consulta de la clase y el grupo del articulo principal seleccionado
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public static HashMap<String, Object> consultaCG (Connection con, int codigoArtPpal, String consultaStr2)
	{
		HashMap<String, Object> resultadosCG = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaStr2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst2.setInt(1, codigoArtPpal);
			
			logger.info("\n\n consultaStr2-->"+consultaStr2+" ->"+codigoArtPpal);
			
			resultadosCG = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),false, true);
			
			logger.info(resultadosCG);
			resultadosCG.put("INDICES",indicesMapCG);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de clase y grupo de Articulo principal");
		}		
		return resultadosCG;
	}

	
	/**
	 * Metodo de consulta de los articulos sustitutos de un articulo seleccionado
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	
	public static HashMap<String, Object> consultaSus (Connection con, int codigoArtPpal)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			logger.info("consulta->"+consultaStr+" ->"+codigoArtPpal);
			pst.setInt(1, codigoArtPpal);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultados.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(resultados.get("articuloSustituto_"+w)+""))
				{
					resultados.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultados.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
			resultados.put("INDICES",indicesMap);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de articulo sustituto");
		}
		return resultados;
	}
	
	public static boolean eliminar(Connection con, String sustitutosNoPos)
	{

		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, sustitutosNoPos);
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;			
	}
	
	private static boolean puedoEliminar(String codigo)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminar=eliminar(con, codigo);
		if(puedoEliminar)
			retorna= true;
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	public static boolean modificar(Connection con, SustitutosNoPos sustitutosNoPos)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			/**
			 * UPDATE sustitutos_nopos " +
									"SET dosificacion=?, " +
										"dosis_diaria=?, " +
										"tiempo_tratamiento=?, " +
										"num_dosis_equivalentes=?, " +
										"institucion=?, " +
										"usuario_modifica=?, " +
										"fecha_modifica=CURRENT_DATE, " +
										"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
									"WHERE " +
										"articulo_ppal=? AND " +
										"articulo_sustituto=?
			 */
			
			ps.setString(1, sustitutosNoPos.getDosificacion());
			ps.setString(2, sustitutosNoPos.getDosisDiaria());
			ps.setString(3, sustitutosNoPos.getTiempoTratamiento());
			ps.setString(4, sustitutosNoPos.getNumDosisEquivalentes());
			ps.setInt(5, sustitutosNoPos.getInstitucion());
			ps.setString(6, sustitutosNoPos.getUsuarioModifica());
			ps.setInt(7, Utilidades.convertirAEntero(sustitutosNoPos.getArticuloPrincipal()));
			ps.setInt(8, Utilidades.convertirAEntero(sustitutosNoPos.getResultadosMap().get("articuloSustituto_"+sustitutosNoPos.getIndexMap()).toString()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean insertar(Connection con, SustitutosNoPos sustitutosNoPos){
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, Utilidades.convertirAEntero(sustitutosNoPos.getArticuloPrincipal()));
			ps.setInt(2, Utilidades.convertirAEntero(sustitutosNoPos.getCodNueArtSus()));
			ps.setInt(3, sustitutosNoPos.getInstitucion());
			ps.setString(4, sustitutosNoPos.getDosificacion());
			ps.setString(5, sustitutosNoPos.getDosisDiaria());
			ps.setString(6, sustitutosNoPos.getTiempoTratamiento());
			ps.setString(7, sustitutosNoPos.getNumDosisEquivalentes());			
			ps.setString(8, sustitutosNoPos.getUsuarioModifica());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
}