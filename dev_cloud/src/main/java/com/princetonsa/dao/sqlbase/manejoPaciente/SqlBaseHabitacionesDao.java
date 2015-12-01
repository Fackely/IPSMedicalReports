/*
 * @(#)SqlBaseHabitacionesDao.java
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
import com.princetonsa.mundo.manejoPaciente.Habitaciones;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * Consultas estandar de habitaciones
 * @author Julián Pacheco
 * jpacheco@princetonsa.com
 */

public class SqlBaseHabitacionesDao 
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseHabitacionesDao.class);
	
	/**
	 * cadena para la insercion de habitaciones
	 */
	private static final String cadenaInsertarStr="INSERT INTO habitaciones (codigo, piso, tipo_habitacion, codigo_habitac, institucion, nombre, centro_atencion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la modificacion de habitaciones
	 */
	private static final String cadenaModificarStr= "UPDATE habitaciones SET piso=?, tipo_habitacion=?, codigo_habitac=?, nombre=?, centro_atencion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=?";

	/**
	 * cadena para la elimininacion de habitaciones
	 */
	private static final String cadenaEliminarStr="DELETE FROM habitaciones WHERE codigo=? ";
	
	/**
	 * consulta la info de habitaciones, EN CASO DE AGREGAR UN NUEVO CAMPO ENTONCES agregarlo al <INDICES_MAPA>
	 */
	private static final String consultarHabitacionesStr="SELECT " +
																	"h.codigo as codigo, " +		
																	"h.piso as piso, " +
																	"h.tipo_habitacion as codigotipohabitac, " +
																	"h.codigo_habitac as codigohabitac, " +
																	"h.nombre as nombre, " +
																	"h.centro_atencion as codigocentroatencion, " +
																	"'"+ConstantesBD.acronimoSi+"' as estabd " +
																	"FROM " +
																		" manejopaciente.habitaciones h " +
																		"INNER  JOIN administracion.centro_atencion ca ON (h.centro_atencion=ca.consecutivo) " +
																	"WHERE " +
																		" h.institucion=?  ";
	
	/**
	 * consulta la info de habitaciones por codigo
	 */
	private static final String consultarHabitacionesEspecificoStr="SELECT " +
																		"h.codigo as codigo, " +
																		"h.piso as piso, " +
																		"h.tipo_habitacion as codigotipohabitac, " +
																		"h.codigo_habitac as codigohabitac, " +
																		"h.nombre as nombre, " +
																		"h.centro_atencion as codigocentroatencion, " +
																		"'"+ConstantesBD.acronimoSi+"' as estabd " +
																		"FROM " +
																			" habitaciones h " +
																			"INNER  JOIN centro_atencion ca ON (h.centro_atencion=ca.consecutivo) " +
																		"WHERE " +
																			" h.codigo=?   ";
	
	/**
	 * Consulta las n habitaciones x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap habitacionesXCentroAtencionTipo(Connection con, int centroAtencion, int codigoInstitucion)
	{
		String cadena= consultarHabitacionesStr;
		
		if(centroAtencion>0)
			cadena+=" AND centro_atencion= "+centroAtencion+" ";
				
		cadena+=" ORDER BY codigohabitac ";
		
		logger.info("habitaciones x centro-->"+cadena+" ->"+centroAtencion);
		
		String[] indicesMapa={"codigo_","piso_", "codigotipohabitac_","codigohabitac_", "nombre_", "codigocentroatencion_", "estabd_"};
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
	 * Insertar un registro de habitaciones
	 * @param con
	 * @param Habitaciones habitaciones
	 * */
	public static boolean insertarHabitaciones(Connection con, Habitaciones habitaciones, int codigoInstitucion)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_habitac"));
			ps.setInt(2, habitaciones.getPiso());
			ps.setString(3, habitaciones.getCodigotipohabitac());
			ps.setString(4, habitaciones.getCodigohabitac());
			ps.setInt(5, codigoInstitucion);
			ps.setString(6,habitaciones.getNombre());
			ps.setInt(7,habitaciones.getCentroAtencion());
			ps.setString(8,habitaciones.getUsuarioModifica());
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
	 * Modifica una habitacion registrada
	 * @param con
	 * @param Habitaciones habitaciones
	 */
	public static boolean modificarHabitaciones(Connection con, Habitaciones habitaciones)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1,habitaciones.getPiso());
			ps.setString(2,habitaciones.getCodigotipohabitac());
			ps.setString(3,habitaciones.getCodigohabitac());
			ps.setString(4,habitaciones.getNombre());
			ps.setInt(5,habitaciones.getCentroAtencion());
			ps.setString(6,habitaciones.getUsuarioModifica());
			ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(8,UtilidadFecha.getHoraActual());
			ps.setInt(9,habitaciones.getCodigo());
		
			
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
	 * Elimina una habitacion registrada
	 * @param con
	 * @param Habitaciones habitaciones
	 */
	public static boolean eliminarHabitaciones(Connection con, int codigo)
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
	public static HashMap consultarHabitaciones(Connection con, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= consultarHabitacionesStr;
		cadena+=" ORDER BY codigohabitac ";
		
		try
		{
			logger.info("consultarHabitacionesStr-->"+consultarHabitacionesStr+" institucion->"+codigoInstitucion);
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
	 * @param codigohabitac
	 * @return
	 */
	public static HashMap consultarHabitacionesEspecifico(Connection con, int codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try
		{
			logger.info("consultarHabitacionesEspecificoStr-->"+consultarHabitacionesEspecificoStr+" codigo->"+codigo);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarHabitacionesEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	
	}
	
	
	/**
	 * Consulta avanzada de habitaciones por cada uno de los campos
	 * @param con
	 * @param HashMap condicion 
	 * */
	
	/*
	public static HashMap consultaHabitacionesAvanzada(Connection con,HashMap condicion)
	{	    
		String cadenaConsulta=""; 
		int numCol=0,i=0;
		String[] cadenaTmp={"","","","","","","","","",""};	
		
		if(!condicion.get("flagInicio").equals("true"))
		{
			if(!condicion.get("pisoC").equals("")){
				cadenaTmp[numCol]= " piso="+condicion.get("pisoC").toString();
				numCol++;
			}
		 
			if(!condicion.get("codigotipohabitacC").equals("")){
				cadenaTmp[numCol]= " tipo_habitacion="+condicion.get("codigotipohabitacC").toString();
				numCol++; 
			}
		  
			if(!condicion.get("codigohabitacC").equals("")){
				cadenaTmp[numCol]= " codigo_habitac="+condicion.get("codigohabitacC").toString();
				numCol++;
			}
		 
			if(!condicion.get("nombreC").equals("")){
				cadenaTmp[numCol]= " nombre="+condicion.get("nombreC").toString();
				numCol++;
			}		
		 
			if(numCol>0){	 
				cadenaConsulta=" WHERE "; 	 
				while(i < numCol){
					cadenaConsulta+=cadenaTmp[i];	  
					if(i < (numCol-1))
						cadenaConsulta+= " AND ";  	
					i++;
				}
			}
		}
		
	
		cadenaConsulta= consultarHabitacionesStr+" "+cadenaConsulta+"  ";		
		
		String[] indicesMapa={"piso_", "codigotipohabitac_","codigohabitac_", "nombre_", };
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;	
		
		try{
		 ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		 mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	 
		}
		catch(SQLException e){		
		 e.printStackTrace(); 	
		}	
		
		mapa.put("INDICES_MAPA", indicesMapa);		
		return mapa;	
	}*/	

	
}
