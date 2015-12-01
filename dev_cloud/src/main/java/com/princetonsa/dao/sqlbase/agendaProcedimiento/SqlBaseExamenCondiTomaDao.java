package com.princetonsa.dao.sqlbase.agendaProcedimiento;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.agendaProcedimiento.ExamenCondiToma;

/**
 * Consultas estandar de examen condiciones de toma
 * @author Jose Eduardo Arias Doncel
 *  jearias@princetonsa.com  
 * */

public class SqlBaseExamenCondiTomaDao{

	//---Atributos 
	
	/**
	 * Objeto para manejar los logs de esta clase
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseExamenCondiTomaDao.class);
	
	
	/**
	 * Cadena de insercion de registros de examen condicion de toma
	 * */
	private static final String cadenaInsertarStr = "INSERT INTO facturacion.examen_conditoma(codigo_examenct," +
												   "												 institucion," +
												   "												 descrip_examenct," +
												   "												 activo_examenct," +
												   "												 usuario_modifica," +
												   "												 fecha_modifica," +
												   "												 hora_modifica) " +
												   "VALUES";

	/**
	 * Cadena de modificacion de registros de examen condicion de toma  
	 * */
	private static final String cadenaModificarStr = "UPDATE facturacion.examen_conditoma " +
													 "SET descrip_examenct=?, " +
													 "    activo_examenct=?, " +
													 "    usuario_modifica=?," +
													 "    fecha_modifica=?, " +
													 "    hora_modifica=? " +
													 "WHERE codigo_examenct=? " +
													 "AND   institucion=? ";
	
	/**
	 * Cadena de eliminacion de examen condicion de toma 
	 * */	
	private static final String cadenaEliminarStr = "DELETE FROM facturacion.examen_conditoma " +
													"WHERE codigo_examenct=? " +
													"AND institucion=? ";
	
	/**
	 * Cadena de consulta de examen condicion de toma
	 * */
	private static String  cadenaConsultarStr = "SELECT codigo_examenct AS codigo," +
													  "       institucion AS institucion," +
													  "       descrip_examenct AS descripcion," +
													  "       activo_examenct AS activo," +
													  "       usuario_modifica AS usuariomodifica," +
													  "       fecha_modifica AS fechamodifica," +
													  "       hora_modifica AS horamodifica, " +
													  "		  '"+ConstantesBD.acronimoSi+"' AS estabd "+
													  "FROM   facturacion.examen_conditoma ";
	
	/**
	 * vector string indice de mapa de examen condicion de toma 
	 * */
	private static final String[] indicesMapa = {"codigo_","institucion_","descripcion_","activo_","usuariomodifica_","fechamodifica_","horamodifica_","estabd_"};
	
	//--- Fin Atributos
	
	//--- Metodos
	
	/**
	 * Inserta un registro de condicion de toma de examen 
	 * @param Connection con 
	 * @param ExamenCondiToma exameCt
	 * */
	public static boolean insertarExamenCt(Connection con, ExamenCondiToma examenCt)
	{
		String cadenaInsertar=cadenaInsertarStr;
		try
		{
			cadenaInsertar+="("+examenCt.GetCadenaSecuenciaStr().trim()+",?,?,?,?,?,?) "; 
			logger.info("cadenaInsertar  >------<    "+cadenaInsertar
					+"/1  "+examenCt.getInstitucion()
					+"/2  "+examenCt.getDescripcionExamenCt()
					+"/3  "+examenCt.getActivoExamenCt().trim()
					+"/4  "+examenCt.getUsuarioModifica()
					+"/5  "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(examenCt.getFechaModifica()))
					+"/6  "+examenCt.getHoraModifica());
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
			
			ps.setInt(1,examenCt.getInstitucion());
			ps.setString(2,examenCt.getDescripcionExamenCt());
			ps.setString(3,examenCt.getActivoExamenCt().trim());
			ps.setString(4,examenCt.getUsuarioModifica());
			ps.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(examenCt.getFechaModifica())));
			ps.setString(6,examenCt.getHoraModifica());
			
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
	 * Modificar un registro de condicion de toma de examen 
	 * @param Connection con
	 * @param ExamenCondiToma examenCt 
	 * */
	public static boolean modificarExamenCt(Connection con, ExamenCondiToma examenCt)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1,examenCt.getDescripcionExamenCt());
			ps.setString(2,examenCt.getActivoExamenCt());
			ps.setString(3,examenCt.getUsuarioModifica());
			ps.setDate(4,Date.valueOf(examenCt.getFechaModifica()));
			ps.setString(5,examenCt.getHoraModifica());
			
			ps.setString(6,examenCt.getCodigoExamenCt());
			ps.setInt(7,examenCt.getInstitucion());
			
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
	 * Elimina un registro de condicion de toma de examen
	 * @param Connection con
	 * @param String codigoExamenCt
	 * @param int institucion 
	 * */
	public static boolean eliminarExamenCt(Connection con, String codigoExamenCt, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(codigoExamenCt));			
			ps.setInt(2,institucion);			
			
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
	 * Consulta basica de condiciones de toma de examen
	 * si los parametros son -> ("",int) genera una consulta de todos los campos por institucion
	 * @param String codigoExamenCt
	 * @param int institucion
	 * */
	public static HashMap consultarExamentCtBasica(Connection con, String codigoExamenCt, int institucion)
	{
		HashMap mapa = new HashMap();
		String cadenaConsulta=cadenaConsultarStr;
		mapa.put("numRegistros",0);		
		
		try
		{		
			if(codigoExamenCt.equals(""))
				cadenaConsulta+= " WHERE institucion=?";
						
			else		
				cadenaConsulta+=" WHERE codigo_examenct=? AND institucion=? ";
			
			cadenaConsulta+=" ORDER BY descrip_examenct ASC ";
					
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(codigoExamenCt.equals(""))
				ps.setInt(1,institucion);
			else
			{
				ps.setString(1,codigoExamenCt);
				ps.setInt(2,institucion);				
			}			
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}	
		catch(SQLException e)
		{
			e.printStackTrace();					
		}
		
		mapa.put("INDICES_MAPA",indicesMapa);
		return mapa;
	}
	
	//--- Fin Metodos
}