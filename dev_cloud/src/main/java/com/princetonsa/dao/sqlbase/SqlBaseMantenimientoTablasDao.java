/*
 * @(#)SqlBaseMantenimientoTablasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;
import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Mantenimiento de Tablas
 *
 *	@version 1.0, Apr 6, 2004
 */
public class SqlBaseMantenimientoTablasDao {
	
	
	private static Logger logger = Logger.getLogger(SqlBaseMantenimientoTablasDao.class);
	
	
	public static HashMap getDatosTabla(Connection con, String consulta) throws SQLException
	{
		HashMap result = new HashMap();
		try{
			logger.info("Consulta: "+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			result = UtilidadBD.cargarValueObject(rs,true, false);
			rs.close();
			ps.close();
			//Utilidades.imprimirMapa(result);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+consulta);
		}
		return result; 
	}

	
	/**
	 * 
	 * @param con
	 * @param consulta
	 * @return
	 * @throws SQLException
	 */
	public static int updateDatosTabla(Connection con, String consulta) throws SQLException{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("consulta++++++++++++++++++++++++++++++++++++"+consulta);
			return ps.executeUpdate();
	}
	

	/**
	 * Metodo para validar que los datos nuevos no esten almacenados en la base de datos
	 * @param con, consulta
	 * @return
	 * @throws SQLException
	 */
	public static int buscarDatosTabla(Connection con, String consulta) throws SQLException {
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rst =new ResultSetDecorator(ps.executeQuery());
		int band = 0;
		
		if(rst.next()){
			band = rst.getInt("contador");
		}

		return band;
	}
	
	
	/**
	 * @param con
	 * @param nombreEspecialidad
	 * @return
	 */
	public static boolean existeEspecialidad(Connection con, String nombreEspecialidad) {
		String consulta="SELECT codigo FROM especialidades where upper(nombre)=upper(?)";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, nombreEspecialidad);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}


	public static boolean existeExcepcionAgenda(Connection con, String fecha, String centroAtencion) {
		String consulta="SELECT * FROM excepciones_agenda WHERE fecha= ?  AND centro_atencion= ?"; 
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setInt(2, Utilidades.convertirAEntero(centroAtencion));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}


	public static boolean existeInterconsultaPermitida(Connection con,String ocupacion, String especialidad) {
		
		String consulta="SELECT * FROM interconsu_perm WHERE ocupacion_medica=?  AND especialidad= ?"; 
				
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(ocupacion));
			ps.setInt(2, Utilidades.convertirAEntero(especialidad));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

   /**
    * 
    * @param con
    * @param nombreOcupacion
    * @return
    */
	public static boolean existeOcupacionMedica(Connection con,String nombreOcupacion)
	{
		String consulta="SELECT * FROM ocupaciones_medicas where upper(nombre)=upper(?)";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, nombreOcupacion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
