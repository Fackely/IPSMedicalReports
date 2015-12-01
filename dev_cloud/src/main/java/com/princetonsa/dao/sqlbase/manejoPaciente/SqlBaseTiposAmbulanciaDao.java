/*
 * @(#)SqlBaseTiposAmbulanciaDao.java
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
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.TiposAmbulancia;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * Consultas estandar de Tipos Ambulancia
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class SqlBaseTiposAmbulanciaDao
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTiposAmbulanciaDao.class);
	
	/**
	 * cadena para la insercion de tipos ambulancia
	 */
	private static final String cadenaInsertarStr="INSERT INTO tipos_ambulancia (codigo, institucion, descripcion, servicio, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";

	/**
	 * cadena para la modificacion de tipos ambulancia
	 */
	private static final String cadenaModificarStr= "UPDATE tipos_ambulancia SET codigo=?, descripcion=?, servicio=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=? AND institucion=?";
	
	/**
	 * cadena para la elimininacion de tipos ambulancia
	 */
	private static final String cadenaEliminarStr="DELETE FROM tipos_ambulancia WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de consultas de Tipos Ambulancia
	 */
	private static final String consultarTiposAmbulanciaStr="SELECT " +
																"ta.codigo as codigo, " +
																"ta.descripcion as descripcion, " +
																"ta.servicio as servicio, " +
																"getnombreservicio(ta.servicio, '"+ConstantesBD.codigoTarifarioCups+"') as nomservicio, " +
																"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																"ta.codigo as codigoantesmod " +
															"FROM " +
																" tipos_ambulancia ta " +
															"WHERE " +
																"ta.institucion=? order by codigo";
	
	/**
	 * Cadena de consultas de Tipos Ambulancia por codigo
	 */
	private static final String consultarTiposAmbulanciaEspecificoStr="SELECT " +
																			"ta.codigo as codigo, " +
																			"ta.descripcion as descripcion, " +
																			"ta.servicio as servicio, " +
																			"getnombreservicio(ta.servicio, '"+ConstantesBD.codigoTarifarioCups+"') as nomservicio, " +
																			"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																			"ta.codigo as codigoantesmod " +
																		"FROM " +
																			" tipos_ambulancia ta " +
																		"WHERE " +
																			"ta.codigo=? AND ta.institucion=? ";
				
	/**
	 * Insertar un registro de tipos ambulancia
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 * */
	public static boolean insertarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, int codigoInstitucion)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tipos_ambulancia (
			 * codigo, 
			 * institucion, 
			 * descripcion, 
			 * servicio, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, tiposambulancia.getCodigo());
			ps.setInt(2, codigoInstitucion);
			ps.setString(3,tiposambulancia.getDescripcion());
			if(tiposambulancia.getCodigoServicio()>0)
			{
				ps.setInt(4,tiposambulancia.getCodigoServicio());
			}
			else
			{	
				ps.setNull(4, Types.INTEGER);
			}	
			ps.setString(5,tiposambulancia.getUsuarioModifica());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7, UtilidadFecha.getHoraActual());
			
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
	 * Modifica tipos de ambulancia registrada
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public static boolean modificarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, String codigoAntesMod, int codigoInstitucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE tipos_ambulancia SET 
			 * codigo=?, 
			 * descripcion=?, 
			 * servicio=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=? 
			 * WHERE codigo=? 
			 * AND institucion=?
			 */
			
			ps.setString(1,tiposambulancia.getCodigo());
			ps.setString(2,tiposambulancia.getDescripcion());
			ps.setInt(3,tiposambulancia.getCodigoServicio());
			ps.setString(4,tiposambulancia.getUsuarioModifica());
			ps.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(6,UtilidadFecha.getHoraActual());
			ps.setString(7,codigoAntesMod);
			ps.setInt(8, codigoInstitucion);
			
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
	 * Elimina tipos de ambulancia registradas
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public static boolean eliminarTiposAmbulancia(Connection con, String codigo, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2, institucion);
			
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
	public static HashMap consultarTiposAmbulancia(Connection con, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTiposAmbulanciaStr-->"+consultarTiposAmbulanciaStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposAmbulanciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarTiposAmbulanciaEspecifico(Connection con, int codigoInstitucion,String codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTiposAmbulanciaEspecificoStr-->"+consultarTiposAmbulanciaEspecificoStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposAmbulanciaEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
}
