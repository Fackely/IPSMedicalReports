/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseTipoCalificacionDao {

	
	/**
	 * Variable para manejar los las Excepciones por los LOGS    
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTipoCalificacionDao.class);
	
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
		//---- Consultar los Tipos de Calificacion
		if ( nroConsulta ==  1 )
		{
			consulta.append("	SELECT tcp.consecutivo as tc_conse, tcp.codigo as tc_cod, tcp.codigo as h_tc_cod, 								" +
							"		   tcp.descripcion as tc_nom, tcp.descripcion as h_tc_nom, tcp.activo as tc_act, tcp.activo as h_tc_act,	" +
							" 		   CASE WHEN ccm.tipo_calificacion IS NULL THEN 'true' ELSE 'false' END as puedoeliminar					" +
							"		   FROM pyp.tipo_calificacion_pyp tcp																		" +
							"		   		LEFT OUTER JOIN ( SELECT tipo_calificacion FROM  pyp.califica_x_cumpli_metas GROUP BY tipo_calificacion ) ccm ON  ( tcp.consecutivo = ccm.tipo_calificacion )		" +
							"						  WHERE tcp.institucion = " + mapaParam.get("codigoInstitucion") +
							"			 		            ORDER BY tcp.codigo");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar la Actividades que sean solo Articulos. 
		/*if ( nroConsulta ==  2 )
		{
			consulta.append("	SELECT * FROM (																				" +
							"	SELECT consecutivo as cod_act,  															" +
							"		   ap.articulo as cod_art, 																" +
							"		   getdescripcionarticulo(ap.articulo) as nom_act										" +
							"		   FROM pyp.actividades_pyp ap  														" +
							"				INNER JOIN articulo a ON (	a.codigo = ap.articulo )							" +
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
							"	SELECT ap.consecutivo as cod_act_reg,  a.descripcion as nom_act_reg,						" +
							" 		   app.activo as act_act_reg, app.activo as h_act_act_reg								" +
							"		   FROM pyp.actividades_pyp_programa app												" +
							"		   		INNER JOIN pyp.actividades_pyp ap ON ( ap.consecutivo = app.articulo )			" +
							"				INNER JOIN articulo a ON ( a.codigo = ap.articulo )								" +
							"						  WHERE app.programa = '" + mapaParam.get("programa") + "'" +
							"				 	) x ORDER BY nom_act_reg");
		}*/
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
	 * Para Insertar los tipos de calificacion de Metas de PYP 
	 * @param con
	 * @param tipoOperacion
	 * @param consecutivo
	 * @param codigo
	 * @param nombre
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public static int insertarTipoCalificacion(Connection con, int tipoOperacion, int consecutivo, String codigo, String nombre, int institucion, boolean activo) 
	{
		String cad = "";
		PreparedStatementDecorator ps;
		
		if ( tipoOperacion == 0 )  //-- Para Insertar  
		{
		 	consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "pyp.seq_tipo_calificacion_pyp");
			cad = " INSERT INTO pyp.tipo_calificacion_pyp ( consecutivo, codigo, descripcion, activo, institucion) " +
				  " 	        VALUES (?,?,?," + ValoresPorDefecto.getValorTrueParaConsultas() + ",?) 	  ";
		}
		else //-- Para Modificar 
		{
			if (activo)
			{
				cad = " UPDATE pyp.tipo_calificacion_pyp SET activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + "," +
					  " 	   codigo = ?, descripcion = ? WHERE consecutivo = ? " ;
			}
			else
			{
				cad = " UPDATE pyp.tipo_calificacion_pyp SET activo = " + ValoresPorDefecto.getValorFalseParaConsultas() + "," +
					  " 	   codigo = ?, descripcion = ? WHERE consecutivo = ? " ;
			}
		}  
				
	   try {
			ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if ( tipoOperacion == 0 )  //-- Para Insertar  
			{
				ps.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
				ps.setString(2, codigo);
				ps.setString(3, nombre);
				ps.setInt(4, institucion);
				return ps.executeUpdate();
			}
			else
			{
				ps.setString(1, codigo);
				ps.setString(2, nombre);
				ps.setDouble(3, Utilidades.convertirADouble(consecutivo+""));
				return ps.executeUpdate();
			}
		}
		catch (SQLException e) 
		{
			logger.error("\n\n Error Insertando En insertarTipoCalificacion ( SqlBaseTipoCalificacionDao ) :  [" + e.toString() + "] \n\n");
			return ConstantesBD.codigoNuncaValido;
		}
		
	}

	/**
	 * Metodo para eliminar un tipo de Calificacion de Metas de PYP
	 * @param con
	 * @param codigoTipoCal
	 * @return
	 */
	public static int eliminarTipoCalificacion(Connection con, int codigoTipoCal) 
	{
		String cad = " DELETE FROM pyp.tipo_calificacion_pyp WHERE consecutivo = ? ";
		
		PreparedStatementDecorator ps;
		try {
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(codigoTipoCal+""));
				return ps.executeUpdate();
			}
		catch (SQLException e) 
		{
			logger.error("\n\n Error Eliminando En eliminarTipoCalificacion ( SqlBaseTipoCalificacionDao ) :  [" + e.toString() + "] \n\n");
			return ConstantesBD.codigoNuncaValido;
		}
	}


}
