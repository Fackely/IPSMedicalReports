/*
 * Creado 02/04/07
 */

package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;

import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.UtilidadSiEs;

/**
 *@author mono
 */

public class SqlBaseOcupacionDao {

	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseNovedadDao.class);
	
	private static String insertarOcupacionStr="INSERT INTO ocupaciones_medicas (codigo, nombre) VALUES (?, ?)";
	
	private static String modificarOcupacionStr="UPDATE ocupaciones_medicas SET nombre=? WHERE codigo=?";
	
	private static String consultarOcupacionStr="SELECT nombre AS nombre_ocupacion FROM ocupaciones_medicas ORDER BY codigo";
	
	private static String eliminarOcupacionStr="DELETE FROM ocupaciones_medicas WHERE codigo=?";
	
	/********************** ASIGNAR SALARIO A OCUPACIONES MEDICAS **************************/
		
	private static String modificarOcupacionSalarioBaseStr="UPDATE ocupacion_salario_base SET valor=?, fecha_inicio=?, fecha_fin=?, tipo_vinculacion=? WHERE ocupacion=?";
	
	private static String consultarOcupacionSalarioBaseStr="SELECT o.ocupacion AS ocupacion_salario_base, o.valor AS valor_ocupacion, o.fecha_inicio AS fecha_inicio_ocupacion, o.fecha_fin AS fecha_fin_ocupacion, oo.nombre AS nombre FROM ocupacion_salario_base o INNER JOIN ocupaciones_medicas oo ON(o.ocupacion_salario_base=oo.nombre) ORDER BY o.ocupacion";
	
	private static String listadoTiposVinculacionesStr="SELECT nombre AS nombre_vinculacion FROM tipos_vinculacion ORDER BY codigo"; 
	
	private static String listadoTurnosStr="SELECT acronimo AS identificador_turno, nombre AS nomber_turno, por_defecto AS por_defecto_turno FROM turno ORDER BY acronimo";
	
	/**
	 * Inserta en la B.D una ocupacion
	 */
	public static int insertarOcupacion(Connection con, int codigo, String nombre)
	{
		try
		{
			if(UtilidadSiEs.esAxioma())
			{
				insertarOcupacionStr="INSERT INTO administracion.ocupaciones_medicas (codigo, nombre) VALUES (?, ?)";
				modificarOcupacionStr="UPDATE administracion.ocupaciones_medicas SET nombre=? WHERE codigo=?";
				consultarOcupacionStr="SELECT nombre AS nombre_ocupacion FROM administracion.ocupaciones_medicas ORDER BY codigo";
				eliminarOcupacionStr="DELETE FROM administracion.ocupaciones_medicas WHERE codigo=?";
			}
			PreparedStatement insertarOcupacion=con.prepareStatement(insertarOcupacionStr);
			insertarOcupacion.setInt(1, UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_novedad"));
			insertarOcupacion.setString(2, nombre);
			return insertarOcupacion.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error insertando Novedad: "+e);
			return 0;
		}
	}
	
	/**
	 * Modifica la ocupacion
	 */
	public static void modificarOcupacion (Connection con, int codigo, String nombre)
	{
		try
		{
			PreparedStatement modificarOcupacionStatement;
			modificarOcupacionStatement = con.prepareStatement(modificarOcupacionStr);
			modificarOcupacionStatement.setString(1,nombre);
		}
		catch(SQLException e)
	    {
	        logger.warn("Error en la modificacion-> SqlBaseOcupacionDao"+e.toString());
	    }
	}
	
	public static Collection<HashMap<String, Object>> consultarOcupacion(Connection con)
	{
		try
		{
			PreparedStatement consultarOcupacionStatement = con.prepareStatement(consultarOcupacionStr);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarOcupacionStatement.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error consultando las ocupaciones "+e);
			return null;
		}
	}
	
	/**
	 * Metodo que elimina una ocupacion en caso de no tener relaciones
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarOcupacion(Connection con, int codigo)
	{
		try
		{
			PreparedStatement eliminarOcupacion;
			eliminarOcupacion = con.prepareStatement(eliminarOcupacionStr);
			eliminarOcupacion.setInt(1,codigo);
			return eliminarOcupacion.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error Al eliminar la ocupacion: "+e);
			return 0;
		}
	}
	
	/**************************** ASIGNAR SALARIO BASE A OCUPACION ************************/

	/**
	 * Metodo que permite modificar el valor y las fechas a cada ocupacion
	 */
	public static void modificarOcupacionSalarioBase (Connection con, int ocupacion, int valor, String fecha_inicio, String fecha_fin, int tipo_vinculacion)
	{
		try
	    {
	        PreparedStatement modificarOcupacionStatement;
	        modificarOcupacionStatement = con.prepareStatement(modificarOcupacionSalarioBaseStr);
	        modificarOcupacionStatement.setInt(1,ocupacion);
	        modificarOcupacionStatement.setInt(2,valor);
	        modificarOcupacionStatement.setString(3, fecha_inicio);
	        modificarOcupacionStatement.setString(4,fecha_fin);
	        modificarOcupacionStatement.setInt(5,tipo_vinculacion);
	        modificarOcupacionStatement.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en la modificacion-> SqlBaseOcupacionDao"+e.toString());
	    }
	}
	
	/**
	 * Metodo que consulta la ocupacion con su valor y fechas
	 * @param con
	 * @param ocupacion
	 * @return
	 */
	public static Collection consultarOcupacionSalarioBase (Connection con, int ocupacion)
	{
		try
	    {
			if(UtilidadSiEs.esAxioma())
			{
				consultarOcupacionSalarioBaseStr="SELECT o.ocupacion AS ocupacion_salario_base, o.valor AS valor_ocupacion, o.fecha_inicio AS fecha_inicio_ocupacion, o.fecha_fin AS fecha_fin_ocupacion, oo.nombre AS nombre FROM ocupacion_salario_base o INNER JOIN administracion.ocupaciones_medicas oo ON(o.ocupacion_salario_base=oo.nombre) ORDER BY o.ocupacion";
			}
	    	PreparedStatement consultarOcupacionSalarioBase = con.prepareStatement(consultarOcupacionSalarioBaseStr);
	    	consultarOcupacionSalarioBase.setInt(1,ocupacion);
            return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarOcupacionSalarioBase.executeQuery()));
        }
	    catch (SQLException e)
	    {
            logger.error("Error consultando la ocupacion  "+e);
            return null;
        }
	}
	
	/**
	 * Lista el tipo de vinculaciones que estan almacenado en la tabla tipos_vinculacion
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static Collection<HashMap<String, Object>> listadoTiposVinculaciones(Connection con)
	{
		try
		{
			PreparedStatement listadoVinculaciones = con.prepareStatement(listadoTiposVinculacionesStr);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(listadoVinculaciones.executeQuery()));
		}
		catch (SQLException e)
	    {
            logger.error("Error arrojando el listado  "+e);
            return null;
        }
	}
	
	/**
	 * Lista los turnos que estan almacenados en la tabla turno
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static Collection<HashMap<String, Object>> listadoTiposTurnos (Connection con)
	{
		try
		{
			PreparedStatement listadoTiposTurnos = con.prepareStatement(listadoTurnosStr);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(listadoTiposTurnos.executeQuery()));
		}
		catch (SQLException e)
	    {
            logger.error("Error arrojando el listado  "+e);
            return null;
        }
	}
	
}