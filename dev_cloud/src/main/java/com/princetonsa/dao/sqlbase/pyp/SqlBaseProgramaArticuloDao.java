/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Articulo;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseProgramaArticuloDao {

	
	/**
	 * Variable para manejar los las Excepciones por los LOGS    
	 */
	private static Logger logger=Logger.getLogger(SqlBaseProgramaArticuloDao.class);
	
	

	/**
	 *  Metodo para consultar toda la informacion relacionada con la funcionalidad.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	
	public static HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		StringBuffer consulta = new StringBuffer("");
		int nroConsulta = 0;
		
		//-Verificar el tipo de consulta.
		if (UtilidadCadena.noEsVacio(mapaParam.get("nroConsulta")+"")) { nroConsulta = Integer.parseInt(mapaParam.get("nroConsulta")+""); }
		else { return (new HashMap()); }
		
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar los programas de salud PYP.
		if ( nroConsulta ==  1 )
		{
			consulta.append("	SELECT codigo as cod_prog, descripcion as nom_prog 								" +
							"		   FROM pyp.programas_salud_pyp 											" +
							"				WHERE activo = " +  ValoresPorDefecto.getValorTrueParaConsultas() +
							"				  AND institucion  = " + mapaParam.get("codigoInstitucion") +
							"			 		  ORDER BY descripcion");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar la Actividades que sean solo Articulos. 
		if ( nroConsulta ==  2 )
		{
			consulta.append("	SELECT * FROM (																				" +
							"	SELECT consecutivo as cod_act,  															" +
							"		   ap.articulo as cod_art, 																" +
						//	"		   getdescripcionarticulo(ap.articulo) || ' [ ' || na.nombre || ' ]' || ' [ ' || a.minsalud || ' ]'  as nom_act		" +
							"		   getdescarticulo(ap.articulo) as nom_act 			" +
							"		   FROM pyp.actividades_pyp ap  														" +
							"				INNER JOIN articulo a ON (	a.codigo = ap.articulo )							" +
							"				INNER JOIN naturaleza_articulo na ON (	na.acronimo = a.naturaleza  )			" +
							"						  WHERE ap.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
							"				 		    AND ap.institucion = " +  mapaParam.get("codigoInstitucion") +
							"				 		    AND ap.articulo IS NOT NULL " +
							"				 	) x ORDER BY nom_act");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar los articulos por programa.
		if ( nroConsulta ==  3 )
		{
			consulta.append("	SELECT * FROM (																				" +
							"	SELECT ap.consecutivo as cod_act_reg,  getdescarticulo(a.codigo) as nom_act_reg,						" +
							" 		   app.activo as act_act_reg, app.activo as h_act_act_reg								" +
							"		   FROM pyp.actividades_art_pyp_programa app												" +
							"		   		INNER JOIN pyp.actividades_pyp ap ON ( ap.consecutivo = app.articulo )			" +
							"				INNER JOIN articulo a ON ( a.codigo = ap.articulo )								" +
							"						  WHERE app.programa = '" + mapaParam.get("programa") + "'" +
							"				 	) x ORDER BY nom_act_reg");
		}
		try
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
				pst.close();
				return mapaRetorno;
			}
		catch (SQLException e)
			{
				logger.error("Error (SqlBaseProgramaArticuloDao) consultando en (consultarInformacion)   ["+ e + "]");
				return null;
			}
	}


	/**
	 * Metodo para Guardar Modificar Los Articulos por Programa PYP 
	 * @param con
	 * @param operacion
	 * @param programa
	 * @param codigoArticulo
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public static int insertarActividadesCentroAtencion(Connection con, int operacion, String programa, int codigoArticulo, int institucion, boolean activo)
	{
		String cad = "";
		
		if ( operacion == 0 )  //-- Para Insertar  
		{
			cad = " INSERT INTO pyp.actividades_art_pyp_programa ( programa, articulo, institucion, activo  ) " +
				  " 	        VALUES (?,?,?," + ValoresPorDefecto.getValorTrueParaConsultas() + ") 	  ";
		}
		else					    //-- Para Modificar 
		{
			if (activo)
			{
				cad = " UPDATE pyp.actividades_art_pyp_programa SET activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
					  " 	   WHERE programa = ? AND articulo = ? AND institucion = ? " ;
			}
			else
			{
				cad = " UPDATE pyp.actividades_art_pyp_programa SET activo = " + ValoresPorDefecto.getValorFalseParaConsultas() +
					  " 	   WHERE programa = ? AND articulo = ? AND institucion = ? " ;
			}
		} 
				
		PreparedStatementDecorator ps;
	   try {
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, programa);
			ps.setInt(2, codigoArticulo);
			ps.setInt(3, institucion);
			return ps.executeUpdate();
		}
		catch (SQLException e) 
		{
			logger.error("\n\n Error Insertando En insertarActividadesCentroAtencion ( SqlBaseProgramaArticuloDao ) :  [" + e.toString() + "] \n\n");
			return ConstantesBD.codigoNuncaValido;
		}
		
	}
	
	/**
	 * Metodo para eliminar una Articulo de un Programa de Salud PYP Especifico. 
	 * @param con
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @return
	 */
	public static int eliminarActividadesCentroAtencion(Connection con, String programa, int codigoArticulo, int institucion)
	{
		
		
		String cad = " DELETE FROM pyp.actividades_art_pyp_programa WHERE programa = ? AND articulo = ? AND institucion = ? ";
		PreparedStatementDecorator ps;
		
		
		try {
			
				//eliminar primero las vias de ingreso
				String cadena="DELETE from pyp.acti_art_prog_pyp_vias_ing where programa=? and articulo_act=? and institucion=?";
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,programa);
				ps.setDouble(2,Utilidades.convertirADouble(codigoArticulo+""));
				ps.setInt(3,institucion);
				ps.executeUpdate();

				//eliminar los grupos etareos
				cadena="DELETE from pyp.acti_art_prog_pyp_grup_eta where programa=? and articulo_act=? and institucion=?";
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,programa);
				ps.setDouble(2,Utilidades.convertirADouble(codigoArticulo+""));
				ps.setInt(3,institucion);
				ps.executeUpdate();

				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, programa);
				ps.setDouble(2,Utilidades.convertirADouble(codigoArticulo+""));
				ps.setInt(3, institucion);
				return ps.executeUpdate();
			}
		catch (SQLException e) 
		{
			logger.error("\n\n Error Eliminando En eliminarActividadesCentroAtencion ( SqlBaseProgramaArticuloDao ) :  [" + e.toString() + "] \n\n");
			return ConstantesBD.codigoNuncaValido;
		}
	}


	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarViasIngresoActividadPrograma(Connection con, String programa, String actividad, String institucion)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena="SELECT via_ingreso as viaingreso,ocupacion as ocupacion,solicitar as solicitar,programar as programar,ejecutar as ejecutar,'BD' as tiporegistro  from pyp.acti_art_prog_pyp_vias_ing where programa=? and articulo_act=? and institucion=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, programa);
			ps.setDouble(2, Utilidades.convertirADouble(actividad));
			ps.setInt(3, Utilidades.convertirAEntero(institucion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[cargarViasIngresoActividadPrograma] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();

	}


	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @param viaIngreso
	 * @param ocupacion
	 * @return
	 */
	public static boolean eliminarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="DELETE from pyp.acti_art_prog_pyp_vias_ing where " +
										"programa=? " +
										"and articulo_act=? " +
										"and institucion=? " +
										"and via_ingreso=? " +
										"and ocupacion=?";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,programa);
			ps.setDouble(2,Utilidades.convertirADouble(actividad));
			ps.setInt(3,Utilidades.convertirAEntero(institucion));
			ps.setInt(4,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(5,Utilidades.convertirAEntero(ocupacion));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[eliminarRegistroViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;

	}


	public static boolean modificarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar, boolean programar, boolean ejecutar)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="UPDATE acti_art_prog_pyp_vias_ing SET " +
								"solicitar=?," +
								"programar=?," +
								"ejecutar=? " +
								"where programa=? " +
								"and articulo_act=? " +
								"and institucion=? " +
								"and via_ingreso=? " +
								"and ocupacion=?";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBoolean(1,solicitar);
			ps.setBoolean(2,programar);
			ps.setBoolean(3,ejecutar);
			ps.setString(4,programa);
			ps.setDouble(5,Utilidades.convertirADouble(actividad));
			ps.setInt(6,Utilidades.convertirAEntero(institucion));
			ps.setInt(7,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(8,Utilidades.convertirAEntero(ocupacion));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[modificarRegistroViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;
	}


	public static boolean insertarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar, boolean programar, boolean ejecutar)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="INSERT INTO acti_art_prog_pyp_vias_ing(" +
											"programa," +
											"articulo_act," +
											"institucion," +
											"via_ingreso," +
											"ocupacion," +
											"solicitar," +
											"programar," +
											"ejecutar) values(?,?,?,?,?,?,?,?)";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,programa);
			ps.setDouble(2,Utilidades.convertirADouble(actividad));
			ps.setInt(3,Utilidades.convertirAEntero(institucion));
			ps.setInt(4,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(5,Utilidades.convertirAEntero(ocupacion));
			ps.setBoolean(6,solicitar);
			ps.setBoolean(7,programar);
			ps.setBoolean(8,ejecutar);
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[insertarRegistroViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;
	}


	public static HashMap cargarGruposEtareos(Connection con, String programa, String actividad, String institucion, String regimen)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena="SELECT grupo_etareo as grupoetareo,frecuencia as frecuencia,tipo_frecuencia as tipofrecuencia,'BD' as tiporegistro from acti_art_prog_pyp_grup_eta  where programa=? and articulo_act=? and institucion=? and regimen='"+regimen+"'";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, programa);
			ps.setDouble(2,Utilidades.convertirADouble(actividad));
			ps.setInt(3,Utilidades.convertirAEntero(institucion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[cargarGruposEtareos] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}


	public static boolean eliminarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="DELETE FROM acti_art_prog_pyp_grup_eta where programa=? and articulo_act=? and institucion=? and regimen=? and grupo_etareo=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,programa);
			ps.setDouble(2,Utilidades.convertirADouble(actividad));
			ps.setInt(3,Utilidades.convertirAEntero(institucion));
			ps.setString(4,regimen);
			ps.setDouble(5,Utilidades.convertirADouble(grupoEtareo));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[eliminarRegistroGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;
	}


	public static boolean modificarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="UPDATE acti_art_prog_pyp_grup_eta SET " +
											"frecuencia=?, " +
											"tipo_frecuencia=? " +
											"where programa=? " +
											"and articulo_act=? " +
											"and institucion=? " +
											"and regimen=? " +
											"and grupo_etareo=?";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(frecuencia.trim().equals(""))
			{
				ps.setNull(1,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(1,Utilidades.convertirADouble(frecuencia));
			}
			if(tipoFrecuencia.trim().equals(""))
			{
				ps.setNull(2,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(2,Utilidades.convertirADouble(tipoFrecuencia));
			}
			ps.setString(3,programa);
			ps.setDouble(4,Utilidades.convertirADouble(actividad));
			ps.setInt(5,Utilidades.convertirAEntero(institucion));
			ps.setString(6,regimen);
			ps.setDouble(7,Utilidades.convertirADouble(grupoEtareo));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[modificarRegistroGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;	
	}


	public static boolean insertarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="INSERT INTO acti_art_prog_pyp_grup_eta (" +
															"programa," +
															"articulo_act," +
															"institucion," +
															"regimen," +
															"grupo_etareo," +
															"frecuencia," +
															"tipo_frecuencia) values(?,?,?,?,?,?,?)";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,programa);
			ps.setDouble(2,Utilidades.convertirADouble(actividad));
			ps.setInt(3,Utilidades.convertirAEntero(institucion));
			ps.setString(4,regimen);
			ps.setDouble(5,Utilidades.convertirADouble(grupoEtareo));
			if(frecuencia.trim().equals(""))
			{
				ps.setNull(6,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(6,Utilidades.convertirADouble(frecuencia));
			}
			if(tipoFrecuencia.trim().equals(""))
			{
				ps.setNull(7,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(7,Utilidades.convertirADouble(tipoFrecuencia));
			}
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramaArticuloDao[insertarRegistroGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;
	}


}
