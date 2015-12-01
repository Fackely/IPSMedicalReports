/*
 * @(#)SqlBasePisosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;



import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.Pisos;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;


/**
 * Consultas estandar de pisos
 * @author Julián Pacheco
 * jpacheco@princetonsa.com
 */

public class SqlBasePisosDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePisosDao.class);
	
	/**
	 * cadena para la insercion de pisos
	 */
	private static final String cadenaInsertarStr="INSERT INTO pisos (codigo, codigo_piso, institucion, nombre, centro_atencion, usuario_modifica,fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ? ,?, ?, ?)";
	
	/**
	 * cadena para la modificacion de pisos
	 */
	private static final String cadenaModificarStr= "UPDATE pisos SET codigo_piso=?, nombre=?, centro_atencion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=?";
	
	/**
	 * cadena para la elimininacion de pisos
	 */
	private static final String cadenaEliminarStr="DELETE FROM pisos WHERE codigo=?";
	
	/**
	 * consulta la info de pisos, EN CASO DE AGREGAR UN NUEVO CAMPO ENTONCES agregarlo al <INDICES_MAPA>
	 */
	private static final String consultarPisosStr="SELECT " +
														"p.codigo as codigo," +
																	"p.codigo_piso as codigopiso, " +
																	"p.nombre as nombre, " +
																	"p.centro_atencion as codigocentroatencion," +
																	"'"+ConstantesBD.acronimoSi+"' as estabd " +
																	"FROM " +
																		" manejopaciente.pisos p " +
																		"INNER  JOIN administracion.centro_atencion ca ON (p.centro_atencion=ca.consecutivo) " +
																	"WHERE " +
																		" p.institucion=?  ";
	
	/**
	 * 
	 */
	private static final String consultarPisosEspecificoStr="SELECT " +
														"p.codigo as codigo," +
																	"p.codigo_piso as codigopiso, " +
																	"p.nombre as nombre, " +
																	"p.centro_atencion as codigocentroatencion," +
																	"'"+ConstantesBD.acronimoSi+"' as estabd " +
																	"FROM " +
																		" pisos p " +
																		"INNER  JOIN centro_atencion ca ON (p.centro_atencion=ca.consecutivo) " +
																	"WHERE " +
																		" p.codigo=?  ";
	
	
	/**
	 * Consulta los n pisos x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap pisosXCentroAtencionTipo(Connection con, int centroAtencion, int codigoInstitucion)
	{
		String cadena= consultarPisosStr;
		
		if(centroAtencion>0)
			cadena+=" AND centro_atencion= "+centroAtencion+" ";
				
		cadena+=" ORDER BY codigopiso ";
		
		//logger.info("pisos x centro-->"+cadena+" ->"+centroAtencion);
		
		String[] indicesMapa={"codigo_", "codigopiso_", "nombre_", "codigocentroatencion_", "estabd_"};
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		//logger.warn(mapa);
		
		mapa.put("INDICES_MAPA", indicesMapa);
		return mapa;
	}
																
	/**
	 * Insertar un registro de pisos
	 * @param con
	 * @param Pisos pisos
	 * */
	public static boolean insertarPisos(Connection con, Pisos pisos, int codigoInstitucion)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO pisos (
			 * codigo, 
			 * codigo_piso, 
			 * institucion, 
			 * nombre, 
			 * centro_atencion, 
			 * usuario_modifica,
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ? ,?, ?, ?)
			 */
			
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_pisos"));
			ps.setString(2, pisos.getCodigopiso());
			ps.setInt(3, codigoInstitucion);
			ps.setString(4,pisos.getNombre());
			ps.setInt(5,pisos.getCentroAtencion());
			ps.setString(6,pisos.getUsuarioModifica());
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(8, UtilidadFecha.getHoraActual());
			
			if(ps.executeUpdate()>0)
			  return true;	
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();	  
		}
		return false;
	}
			
	/**
	 * Modifica un piso registrado
	 * @param con
	 * @param Pisos pisos
	 */
	public static boolean modificarPisos(Connection con, Pisos pisos)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1,pisos.getCodigopiso());
			ps.setString(2,pisos.getNombre());
			ps.setInt(3,pisos.getCentroAtencion());
			ps.setString(4,pisos.getUsuarioModifica());
			ps.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(6,UtilidadFecha.getHoraActual());
			ps.setInt(7, pisos.getCodigo());
			
			if(ps.executeUpdate() >0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Elimina un piso registrado
	 * @param con
	 * @param Pisos pisos
	 */
	public static boolean eliminarPisos(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigo);
			if(ps.executeUpdate() > 0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarPisos(Connection con, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= consultarPisosStr;
		cadena+=" ORDER BY codigopiso ";
		
		try
		{
			logger.info("consultarPisosStr-->"+consultarPisosStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigopiso
	 * @return
	 */
	public static HashMap consultarPisosEspecifico(Connection con, int codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarPisosEspecificoStr-->"+consultarPisosEspecificoStr+" codigo->"+codigo);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarPisosEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	
	}

}
