/*
 * @(#)SqlBaseTiposUsuarioCamaDao.java
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
import com.princetonsa.mundo.manejoPaciente.TiposUsuarioCama;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;


/**
 * Consultas estandar de Tipos Usuario Cama
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class SqlBaseTiposUsuarioCamaDao 
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTiposUsuarioCamaDao.class);
	

	/**
	 * cadena para la insercion de tipos usuario cama
	 */
	private static final String cadenaInsertarStr="INSERT INTO tipos_usuario_cama (codigo, institucion, nombre, sexo, ind_sexo_restrictivo, edad_inicial, edad_final, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la modificacion de tipos usuario cama
	 */
	private static final String cadenaModificarStr= "UPDATE tipos_usuario_cama SET codigo=?, nombre=?, sexo=?, ind_sexo_restrictivo=?, edad_inicial=?, edad_final=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=? AND institucion=?";
	
	/**
	 * cadena para la elimininacion de tipos usuario cama
	 */
	private static final String cadenaEliminarStr="DELETE FROM tipos_usuario_cama WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de consultas de Tipo Habitacion
	 */
	private static final String consultarTiposUsuarioCamaStr="SELECT " +
																"tuc.codigo as codigo, " +
																"tuc.nombre as nombre, " +
																"coalesce(tuc.sexo,-1) as codigosexo, " +
																"tuc.ind_sexo_restrictivo as indsexorestrictivo, " +
																"tuc.edad_inicial as edadinicial, " +
																"tuc.edad_final as edadfinal, " +
																"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																"tuc.codigo as codigoantesmod " +
															"FROM " +
																" tipos_usuario_cama tuc " +
															"WHERE " +
																"tuc.institucion=? order by codigo";
	
	/**
	 * Cadena de consultas de Tipos Usuario Cama por Codigo 
	 */
	private static final String consultarTiposUsuarioCamaEspecificoStr="SELECT " +
																			"tuc.codigo as codigo, " +
																			"tuc.nombre as nombre, " +
																			" coalesce(tuc.sexo,-1) as codigosexo, " +
																			"tuc.ind_sexo_restrictivo as indsexorestrictivo, " +
																			"tuc.edad_inicial as edadinicial, " +
																			"tuc.edad_final as edadfinal, " +
																			"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																			"tuc.codigo as codigoantesmod " +
																		"FROM " +
																			" tipos_usuario_cama tuc " +
																		"WHERE " +
																			"tuc.codigo=? AND tuc.institucion=? ";
	
	/**
	 * Insertar un registro de tipos usuario cama 
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama
	 * */
	public static boolean insertarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoInstitucion)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tipos_usuario_cama (
			 * codigo, 
			 * institucion, 
			 * nombre, 
			 * sexo, 
			 * ind_sexo_restrictivo, 
			 * edad_inicial, 
			 * edad_final, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setInt(1, tiposusuariocama.getCodigo());
			ps.setInt(2, codigoInstitucion);
			ps.setString(3,tiposusuariocama.getNombre());	
			if(tiposusuariocama.getCodigoSexo()>0)
			{
				ps.setInt(4,tiposusuariocama.getCodigoSexo());
				
			}
			else
			{	
				ps.setNull(4, Types.INTEGER);
			}	
			ps.setString(5,tiposusuariocama.getIndSexoRestrictivo());
			ps.setDouble(6,Utilidades.convertirADouble(tiposusuariocama.getEdadInicial()+""));
			ps.setDouble(7,Utilidades.convertirADouble(tiposusuariocama.getEdadFinal()+""));
			ps.setString(8,tiposusuariocama.getUsuarioModifica());
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(10, UtilidadFecha.getHoraActual());
			
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
	public static boolean modificarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoAntesMod, int codigoInstitucion)
	{
		try
		{
			logger.info("\n\n modicificar-->"+cadenaModificarStr+" ->cod="+tiposusuariocama.getCodigo()+" nonm->"+tiposusuariocama.getNombre()+" codAntesNMod->"+codigoAntesMod+" inst->"+codigoInstitucion);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE tipos_usuario_cama SET 
			 * codigo=?, 
			 * nombre=?, 
			 * sexo=?, 
			 * ind_sexo_restrictivo=?, 
			 * edad_inicial=?, 
			 * edad_final=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=? 
			 * WHERE codigo=? 
			 * AND institucion=?
			 */
			
			ps.setInt(1,tiposusuariocama.getCodigo());
			ps.setString(2,tiposusuariocama.getNombre());
			if(tiposusuariocama.getCodigoSexo()>0)
			{
				ps.setInt(3,tiposusuariocama.getCodigoSexo());
			}
			else
			{	
				ps.setNull(3, Types.INTEGER);
				
			}	
			ps.setString(4,tiposusuariocama.getIndSexoRestrictivo());
			ps.setDouble(5,Utilidades.convertirADouble(tiposusuariocama.getEdadInicial()+""));
			ps.setDouble(6,Utilidades.convertirADouble(tiposusuariocama.getEdadFinal()+""));
			ps.setString(7,tiposusuariocama.getUsuarioModifica());
			ps.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(9,UtilidadFecha.getHoraActual());
			ps.setInt(10,codigoAntesMod);
			ps.setInt(11, codigoInstitucion);
			
			if(ps.executeUpdate() >0)
			{
				logger.info("MODIFICO!!!!!!!!!!!");
				return true;
			}	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			logger.info(" NO MODIFICO!!!!!!!!!!!"+e.toString());
		}
		logger.info("NO MODIFICO!!!!!!!!!!!");
		return false;
	}
	
	/**
	 * Elimina tipos de usuario cama registrados
	 * @param con
	 * @param TiposUsuarioCama tipousuariocama
	 */
	public static boolean eliminarTiposUsuarioCama(Connection con, int codigo, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigo);
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
	public static HashMap consultarTiposUsuarioCama(Connection con, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTiposUsuarioCamaStr-->"+consultarTiposUsuarioCamaStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposUsuarioCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	public static HashMap consultarTiposUsuarioCamaEspecifico(Connection con, int codigoInstitucion,int codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTiposUsuarioCamaEspecificoStr-->"+consultarTiposUsuarioCamaEspecificoStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposUsuarioCamaEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
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
