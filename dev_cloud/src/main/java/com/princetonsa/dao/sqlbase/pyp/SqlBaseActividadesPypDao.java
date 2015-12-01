/*
 * Ago 02, 2006
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Statement;
import java.sql.Types;


import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Actividades de Promoción y Prevención
 */
public class SqlBaseActividadesPypDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseActividadesPypDao.class);



	/**
	 * Cadena que consulta actividades de promocion y prevención
	 */
	private static final String consultarStr = "SELECT "+ 
		"ap.consecutivo AS consecutivo, "+
		"CASE WHEN ap.articulo IS NULL THEN ap.servicio ELSE ap.articulo END AS codigo, "+
		"CASE WHEN ap.articulo IS NULL THEN " +
			"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || " +
			"getcodigopropservicio2(ap.servicio,"+ConstantesBD.codigoTarifarioCups+")  || ' '  || " +
			"getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' ' || getnivelservicio(ap.servicio) " +
		"ELSE " +
			"getdescarticulo(ap.articulo) END AS descripcion, "+
		"CASE WHEN ap.cod_cumplimiento IS NULL THEN '' ELSE ap.cod_cumplimiento END AS cod_cumplimiento, "+
		"ap.activo AS activo, "+
		"ap.institucion AS institucion," +
		"CASE WHEN getEsUsadaActividadPyp(ap.consecutivo) > 0 THEN 'true' ELSE 'false' END AS es_usado "+ 
		"FROM actividades_pyp ap ";
	
	/**
	 * Cadena que inserta una nueva actividad
	 */
	private static final String insertarStr = "INSERT INTO actividades_pyp " +
		"(consecutivo,articulo,servicio,cod_cumplimiento,activo,institucion) ";
	
	/**
	 * Cadena que modifica una actividad existente
	 */
	private static final String modificarStr = "UPDATE actividades_pyp SET " +
		"articulo = ?, servicio = ? , cod_cumplimiento = ? , activo = ? WHERE consecutivo = ?";
	
	/**
	 * Cadena que elimina una actividad existente
	 */
	private static final String eliminarStr = "DELETE FROM actividades_pyp WHERE consecutivo = ?";
	
	/**
	 * Método implementado para consultar actividades de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultar(Connection con,HashMap campos)
	{
		try
		{
			String consultar = consultarStr + " WHERE ";
			boolean hayCondicion = false;
			
			//se verifica campo institucion
			if(campos.get("institucion")!=null)
			{
				consultar += " ap.institucion = "+campos.get("institucion");
				hayCondicion = true;
			}
			
			///se verifica servicio
			if(campos.get("servicio")!=null)
			{
				consultar += (hayCondicion?" AND ":"")+" ap.servicio = "+campos.get("servicio");
				hayCondicion = true;
			}
			
			//se verifica articulo
			if(campos.get("articulo")!=null)
			{
				consultar += (hayCondicion?" AND ":"")+" ap.articulo = "+campos.get("articulo");
				hayCondicion = true;
			}
			
			//se verifica campo consecutivo
			if(campos.get("consecutivo")!=null)
			{
				consultar += (hayCondicion?" AND ":"")+" ap.consecutivo = "+campos.get("consecutivo");
				hayCondicion = true;
			}
			
			
			
			//se verifica campo tipoActividad
			if(campos.get("tipoActividad")!=null)
			{
				if(campos.get("tipoActividad").toString().equals(ConstantesBD.tipoActividadPYPArticulo))
					consultar += (hayCondicion?" AND ":"")+" ap.servicio IS NULL ";
				else if(campos.get("tipoActividad").toString().equals(ConstantesBD.tipoActividadPYPServicio))
					consultar += (hayCondicion?" AND ":"")+" ap.articulo IS NULL ";
				hayCondicion = true;
			}
				
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			logger.info("consultar--->"+consultar);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultar)),true,false);
			st.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar de SqlBaseActividadesPypDao: "+e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertar(Connection con,HashMap campos)
	{
		try
		{
			String consulta = insertarStr + " VALUES("+campos.get("secuencia")+",?,?,?,?,?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * NSERT INTO actividades_pyp " +
					"(consecutivo,articulo,servicio,cod_cumplimiento,activo,institucion)
			 */
			
			logger.info("\n\n\nINSERTAR-->"+consulta);
			if(campos.get("tipoActividad").toString().equals(ConstantesBD.tipoActividadPYPArticulo))
			{
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigo")+""));
				pst.setNull(2,Types.INTEGER);
				logger.info("cod->"+Utilidades.convertirAEntero(campos.get("codigo")+"")+"  null ");
			}
			else if(campos.get("tipoActividad").toString().equals(ConstantesBD.tipoActividadPYPServicio))
			{
				pst.setNull(1,Types.INTEGER);
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigo")+""));
				logger.info("-->null cod->"+Utilidades.convertirAEntero(campos.get("codigo")+""));
			}
			pst.setString(3,campos.get("codigoCumplimiento")+"");
			pst.setBoolean(4,UtilidadTexto.getBoolean(campos.get("activo").toString()));
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			logger.info(" codCum->"+campos.get("codigoCumplimiento")+"");
			logger.info(" activo->"+UtilidadTexto.getBoolean(campos.get("activo").toString()));
			logger.info("institucion->"+Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			
			int resp = pst.executeUpdate(); 
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_actividades_pyp");
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar de SqlBaseActividadesPypDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para modificar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificar(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(campos.get("tipoActividad").toString().equals(ConstantesBD.tipoActividadPYPArticulo))
			{
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigo")+""));
				pst.setNull(2,Types.INTEGER);
			}
			else if(campos.get("tipoActividad").toString().equals(ConstantesBD.tipoActividadPYPServicio))
			{
				pst.setNull(1,Types.INTEGER);
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigo")+""));
			}
			pst.setString(3,campos.get("codigoCumplimiento")+"");
			pst.setBoolean(4,UtilidadTexto.getBoolean(campos.get("activo").toString()));
			pst.setDouble(5,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar de SqlBaseActividadesPypDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para eliminar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int eliminar(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar de SqlBaseActividadesPypDao: "+e);
			return 0;
		}
	}

	
	
	//----------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------
	
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
		//---- Consultar los Centros de Atencion  
		if ( nroConsulta ==  1 )
		{
			consulta.append("	SELECT consecutivo as cod_ca, descripcion as nom_ca 					" +
							"		   FROM centro_atencion ca 											" +
							"				WHERE activo = " +  ValoresPorDefecto.getValorTrueParaConsultas() +
							"                 AND cod_institucion =  " + mapaParam.get("codigoInstitucion") +
							"			 		  ORDER BY descripcion");
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar las Actividades de Promocion y Prevencion.
		if ( nroConsulta ==  2 )
		{
			consulta.append("	SELECT * FROM (" +
							"	SELECT ap.consecutivo as codigo_act, s.codigo as codigo_ser_act,  								" +
						//	"		   CASE WHEN ap.articulo IS NULL THEN '' ELSE getdescarticulo(a.codigo) END ||				" +
							"		   CASE WHEN ap.servicio IS NULL THEN '' ELSE rs.descripcion END  as nombre_act				" +
							"		   FROM actividades_pyp ap  																" +
//							"				LEFT OUTER JOIN articulo a ON (	a.codigo = ap.articulo )							" +
							"				LEFT OUTER JOIN servicios s ON ( s.codigo = ap.servicio )							" +
							"				LEFT OUTER JOIN referencias_servicio rs ON ( rs.servicio = s.codigo AND rs.tipo_tarifario = " +  ConstantesBD.codigoTarifarioCups + " ) " +
							"						  WHERE ap.activo = " +  ValoresPorDefecto.getValorTrueParaConsultas() +
							"				 		    AND ap.institucion = " +  mapaParam.get("codigoInstitucion") +
							"							AND ap.articulo IS NULL " +
							"				 	) x ORDER BY nombre_act");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar las Actividades de Promocion y Prevencion Por Centro de Atencion.
		if ( nroConsulta ==  3 )
		{
			consulta.append("	SELECT * FROM (" +
							"	SELECT ap.consecutivo as codigo_reg_act, s.codigo as codigo_ser_reg_act,						" +
						//	"		   CASE WHEN ap.articulo IS NULL THEN '' ELSE getdescarticulo(a.codigo) END ||				" +
							"		   CASE WHEN ap.servicio IS NULL THEN '' ELSE rs.descripcion END  as nombre_reg_act,		" +	
							"		   apc.activo as activo_reg, apc.activo as h_activo_reg										" +
							"		   FROM act_pyp_centro_atencion apc															" +
							"				LEFT OUTER JOIN actividades_pyp ap ON (	apc.actividad_pyp = ap.consecutivo )		" +
						//	"				LEFT OUTER JOIN articulo a ON (	a.codigo = ap.articulo )							" +
							"				LEFT OUTER JOIN servicios s ON ( s.codigo = ap.servicio )							" +
							"				LEFT OUTER JOIN referencias_servicio rs ON ( rs.servicio = s.codigo AND rs.tipo_tarifario = " +  ConstantesBD.codigoTarifarioCups + " ) " +
							"						  WHERE ap.activo = " +  ValoresPorDefecto.getValorTrueParaConsultas() +
							"				 		    AND apc.institucion = " +  mapaParam.get("codigoInstitucion") +
							"				 		    AND apc.centro_atencion = " +   mapaParam.get("centroAtencion") +
							"							AND ap.articulo IS NULL " +
							"				 	) x ORDER BY nombre_reg_act");
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
				logger.error("Error (SqlBaseActividadesPypDao) consultando en (consultarInformacion)   ["+ e + "]");
				return null;
			}
	}


	/**
	 * Metodo para insertar Actividades PYP por Centro de Atencion.
	 * @param con
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @param activo 
	 * @param institucion2 
	 * @return
	 */
	public static int insertarActividadesCentroAtencion(Connection con, int tipoOperacion, int centroAtencion, int codigoActividad, int institucion, boolean activo)
	{
		String cad = "";
		
		if ( tipoOperacion == 0 )  //-- Para Insertar  
		{
			cad = " INSERT INTO pyp.act_pyp_centro_atencion ( centro_atencion, actividad_pyp, activo, institucion ) " +
				  " 	        VALUES (?,?," + ValoresPorDefecto.getValorTrueParaConsultas() + ",?) 		  		";
		}
		else					    //-- Para Modificar 
		{
			if (activo)
			{
				cad = " UPDATE pyp.act_pyp_centro_atencion SET activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
					  " 	   WHERE centro_atencion = ? AND actividad_pyp = ? AND institucion = ? " ;
			}
			else
			{
				cad = " UPDATE pyp.act_pyp_centro_atencion SET activo = " + ValoresPorDefecto.getValorFalseParaConsultas() +
				  	  " 	   WHERE centro_atencion = ? AND actividad_pyp = ? AND institucion = ? " ;
			}
		}
				
		PreparedStatementDecorator ps;
	   try {
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, centroAtencion);
			ps.setDouble(2, Utilidades.convertirADouble(codigoActividad+""));
			ps.setInt(3, institucion);
			return ps.executeUpdate();

		}
		catch (SQLException e) 
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}


	/**
	 * Metodo para eliminar una Actividad PYP para un centro de Atencion Especifico. 
	 * @param con
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @return
	 */
	public static int eliminarActividadesCentroAtencion(Connection con, int centroAtencion, int codigoActividad, int institucion)
	{
		String cad = " DELETE FROM pyp.act_pyp_centro_atencion WHERE centro_atencion = ? AND actividad_pyp = ? AND institucion = ? ";
		
		PreparedStatementDecorator ps;
		try {
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, centroAtencion);
				ps.setDouble(2, Utilidades.convertirADouble(codigoActividad+""));
				ps.setInt(3, institucion);
				return ps.executeUpdate();
			}
		catch (SQLException e) 
		{
			logger.error("\n\n Error Eliminando En eliminarActividadesCentroAtencion ( SqlBaseActividadesPypDao ) :  [" + e.toString() + "] \n\n");
			return ConstantesBD.codigoNuncaValido;
		}
	}
		
	
}
