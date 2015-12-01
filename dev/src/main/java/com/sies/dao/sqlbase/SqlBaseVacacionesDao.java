
/*
 * Created on 25/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseVacacionesDao
{
    /**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseVacacionesDao.class);
	
	private static String insertarVacacionesStr="INSERT INTO vacaciones (codigo_medico, fecha_inicio, fecha_fin) VALUES (?, ?, ?)";
	
	private static String consultarModificarStr="" +
			"SELECT " +
				"codigo_medico AS codigo, " +
				"getNombrePersona(codigo_medico) as nombre, " +
				"to_char(fecha_inicio, 'dd/mm/yyyy') AS fecha_inicio, " +
				"to_char(fecha_fin, 'dd/mm/yyyy') AS fecha_fin, " +
				"CASE WHEN " +
					"(getVacacionesTieneTurnos(codigo_medico, fecha_inicio)="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
						"OR fecha_inicio<=CURRENT_DATE) THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE " +
								""+ValoresPorDefecto.getValorTrueParaConsultas()+" END AS puedo_modificar " +
				" FROM vacaciones";
	
	private static String consultarModificar2Str="SELECT codigo_medico AS codigo, to_char(fecha_inicio, 'dd/mm/yyyy') AS fecha_inicio, to_char(fecha_fin, 'dd/mm/yyyy') AS fecha_fin FROM vacaciones WHERE codigo_medico=? AND fecha_inicio=? ORDER BY fecha_inicio";
	
	private static String modificarVacacionesStr="UPDATE vacaciones set fecha_inicio=?, fecha_fin=? WHERE codigo_medico=? AND fecha_inicio=?";
	
	private static String eliminarVacacionesStr="DELETE FROM vacaciones WHERE codigo_medico=? AND fecha_inicio=?";
	
    public static int insertarVacaciones(Connection con, int codigo, String fecha_inicio, String fecha_fin)
	{
        try
		{
			PreparedStatement insertarVacaciones=con.prepareStatement(insertarVacacionesStr);
			insertarVacaciones.setInt(1, codigo);
			insertarVacaciones.setString(2, UtilidadFecha.conversionFormatoFechaABD(fecha_inicio));
			insertarVacaciones.setString(3, UtilidadFecha.conversionFormatoFechaABD(fecha_fin));
			//System.out.println("Esta es la consulta y las fechas"+insertarVacacionesStr+fecha_inicio+fecha_fin);
			
			return insertarVacaciones.executeUpdate();
		} catch (SQLException e)
		{
			logger.error("Error insertando Las vacaciones: "+e);
			return 0;
		}
        
	}
    
    public static Collection consultarVacaciones(Connection con, Integer codigoPersona, String fechaInicio, String fechaFin)
    {
        PreparedStatement consultarModificarVaca;
		ResultSet rs=null;
		String consultaString="";
		String orden=" ORDER BY to_char(fecha_inicio,'YYYY-MM-DD')";
		consultaString=consultarModificarStr;
		
		int indice=0;
		boolean tienePersona=false;
		if(codigoPersona!=null && codigoPersona>0)
		{
			tienePersona=true;
			consultaString+=" WHERE codigo_medico=?";
			indice=1;
		}
		if(fechaInicio!=null && fechaFin!=null)
		{
			if(tienePersona)
			{
				consultaString+=" AND";
			}
			else
			{
				consultaString+=" WHERE";
			}
			consultaString+=" ((fecha_inicio BETWEEN ? AND ?)"+
			" OR"+
			" (? BETWEEN fecha_inicio AND fecha_fin))";
		}

		consultaString+=orden;
		//System.out.println("consultaString "+consultaString+" codigoPersona "+codigoPersona);

		try
		{
			consultarModificarVaca = con.prepareStatement(consultaString);
			if(codigoPersona!=null && codigoPersona>0)
			{
				consultarModificarVaca.setInt(indice, codigoPersona);
			}
			if(fechaInicio!=null && fechaFin!=null)
			{
				consultarModificarVaca.setString(1+indice, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
				consultarModificarVaca.setString(2+indice, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
				consultarModificarVaca.setString(3+indice, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			}
			rs=consultarModificarVaca.executeQuery();
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
		}
		catch (SQLException e)
		{
			logger.error("Error Al consultar las Vacaciones: "+e);
			return null;
		}
    }
    
    
    public static Collection consultarModificar(Connection con, int codigo, String fecha_inicio) 
	{
	    try
	    {
			PreparedStatement consultarModificarStatement = con.prepareStatement(consultarModificar2Str);
			consultarModificarStatement.setInt(1,codigo);
			consultarModificarStatement.setString(2,UtilidadFecha.conversionFormatoFechaABD(fecha_inicio));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarModificarStatement.executeQuery()));
		}
	    catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	
	}
    
    
    
    public static void modificar(Connection con, int codigo, String fecha_inicio, String fecha_fin, String fecha_ant)
	{
        
	    try
	    {
	        PreparedStatement modificarVacacionesStatement;
	        modificarVacacionesStatement = con.prepareStatement(modificarVacacionesStr);
	        
	        modificarVacacionesStatement.setString(1, UtilidadFecha.conversionFormatoFechaABD(fecha_inicio));
	        modificarVacacionesStatement.setString(2, UtilidadFecha.conversionFormatoFechaABD(fecha_fin));
	        modificarVacacionesStatement.setInt(3,codigo);
	        modificarVacacionesStatement.setString(4,UtilidadFecha.conversionFormatoFechaABD(fecha_ant));
	        modificarVacacionesStatement.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en la modificacion-> SqlBaseVacacionesDao"+e.toString());
	    }
	}
    
    public static int eliminarVacaciones (Connection con, int codigo, String fecha_inicio)
    {
        try
		{
			PreparedStatement eliminarVacaciones=con.prepareStatement(eliminarVacacionesStr);
			eliminarVacaciones.setInt(1, codigo);
			eliminarVacaciones.setString(2, UtilidadFecha.conversionFormatoFechaABD(fecha_inicio));
						
			return eliminarVacaciones.executeUpdate();
		} catch (SQLException e)
		{
			logger.error("Error eliminando Las vacaciones: "+e);
			return 0;
		}
        
    }
	
    
}
