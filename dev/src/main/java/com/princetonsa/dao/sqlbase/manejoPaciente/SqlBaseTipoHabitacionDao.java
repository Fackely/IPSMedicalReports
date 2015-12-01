/*
 * @(#)SqlBaseTipoHabitacionDao.java
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
import com.princetonsa.mundo.manejoPaciente.TipoHabitacion;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * Consultas estandar de Tipo Habitacion
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class SqlBaseTipoHabitacionDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTipoHabitacionDao.class);
	
	/**
	 * cadena para la insercion de tipo habitacion
	 */
	private static final String cadenaInsertarStr="INSERT INTO tipo_habitacion (codigo, institucion, nombre, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la modificacion de tipo habitacion
	 */
	private static final String cadenaModificarStr= "UPDATE tipo_habitacion SET codigo=?, nombre=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=? AND institucion=?";
	
	/**
	 * cadena para la elimininacion de tipo habitacion
	 */
	private static final String cadenaEliminarStr="DELETE FROM tipo_habitacion WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de consultas de Tipo Habitacion
	 */
	private static final String consultarTipoHabitacionStr="SELECT " +
																"th.codigo as codigo, " +
																"th.nombre as nombre, " +
																"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																"th.codigo as codigoantesmod " +
															"FROM " +
																" tipo_habitacion th " +
															"WHERE " +
																"th.institucion=? order by codigo";

	/**
	 * Cadena de consulta de tipo habitacion por codigo
	 */
	private static final String consultarTipoHabitacionEspecificoStr="SELECT " +
																		"th.codigo as codigo, " +
																		"th.nombre as nombre, " +
																		"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																		"th.codigo as codigoantesmod " +
																		"FROM " +
																		" tipo_habitacion th " +
																		"WHERE " +
																		"th.codigo=?  and th.institucion=?  ";
	/**
	 * Insertar un registro de tipo habitacion
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 * */
	public static boolean insertarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, int codigoInstitucion)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tipo_habitacion (codigo, institucion, nombre, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, tipohabitacion.getCodigo());
			ps.setInt(2, codigoInstitucion);
			ps.setString(3,tipohabitacion.getNombre());	
			ps.setString(4,tipohabitacion.getUsuarioModifica());
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(6, UtilidadFecha.getHoraActual());
			
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
	 * Modifica un tipo de habitacion registrada
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public static boolean modificarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, String codigoAntesMod, int codigoInstitucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1,tipohabitacion.getCodigo());
			ps.setString(2,tipohabitacion.getNombre());
			ps.setString(3,tipohabitacion.getUsuarioModifica());
			ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5,UtilidadFecha.getHoraActual());
			ps.setString(6,codigoAntesMod);
			ps.setInt(7, codigoInstitucion);
			
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
	 * Elimina un tipo de habitacion registrada
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public static boolean eliminarTipoHabitacion(Connection con, String codigo, int institucion)
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
	public static HashMap consultarTipoHabitacion(Connection con, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTipoHabitacionStr-->"+consultarTipoHabitacionStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTipoHabitacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	public static HashMap consultarTipoHabitacionEspecifico(Connection con, int codigoInstitucion,String codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTipoHabitacionEspecificoStr-->"+consultarTipoHabitacionEspecificoStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTipoHabitacionEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
