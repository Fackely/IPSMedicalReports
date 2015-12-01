/*
 * Created on 13/04/2005
 *
 */
package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.UtilidadSiEs;

/**
 * @author Juan David Ramírez
 */
public class SqlBaseNovedadDao {

	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseNovedadDao.class);
	
	private static String insertarStr="INSERT INTO novedad (codigo_novedad, nombre_novedad, desc_novedad, nomina, activo) VALUES (?, ?, ?, ?, ?)";
	
	private static final String consultarModificarStr = "SELECT codigo_novedad AS codigo, nombre_novedad AS nombre, desc_novedad AS descripcion, nomina as nomina, activo AS estado FROM novedad WHERE";
	
	private static final String consultarNovedadesStr = "SELECT codigo_novedad AS codigo, nombre_novedad AS nombre, desc_novedad AS descripcion, nomina AS nomina, activo AS estado, getEliminarNovedad(codigo_novedad) AS eliminar FROM novedad ORDER BY nombre_novedad";
	
	private static String modificarNovedadStr="UPDATE novedad set nombre_novedad=?, desc_novedad=?, nomina=?, activo=? WHERE codigo_novedad=?";
	
	private static String eliminarNovedadStr="DELETE FROM novedad WHERE codigo_novedad=?";
	
	/****************Asignar Novedades a Enfermeras********************/
	
	/**
	 * String que contiene la sentencia sql para consultar si una enfermera dada tiene
	 * asignadas novedades en la fecha dada
	 */
	private static String consultarNovedadEnfermeraStr="SELECT ne.codigoregistro AS codigoregistro, ne.observacion AS observacion, ne.fechaprogramacion AS fechaProgramacion, ne.prioridad AS prioridad, ne.activo AS activo, ne.codigomedico AS codigomedico, ne.codigonovedad AS codigoNovedad, nov.nombre_novedad AS nombrenovedad FROM novedad nov INNER JOIN novedad_enfermera ne ON(ne.codigonovedad=nov.codigo_novedad) WHERE (codigomedico=? AND fechaprogramacion=? )";
	
	/**
	 * Sentencia sql que inserta un nuevo registro en la tabla
	 * novedad_enfermera
	 */
	private static String insertarNovedadEnfermeraStr="INSERT INTO novedad_enfermera(codigoregistro, codigomedico, codigonovedad, prioridad, fecharegistro, fechaprogramacion, observacion, activo) VALUES (?,?,?,?, CURRENT_DATE, ?, ?, true)";

	/**
	 * Sentencia sql que consulta la existencia de un registro en la tabla
	 * novedad_enfermera
	 */
	private static String consultarRegistroNovedadEnfermeraStr="SELECT activo AS activo, codigoregistro AS codigo FROM novedad_enfermera WHERE codigomedico=? AND codigonovedad=? AND fechaprogramacion=?";

	/**
	 * Consulta las novedades que tiene asociada una enferemera 
	 * que aun estan activas incluyendo todas las fechas-----------------> y que no han pasado del día de hoy
	 */
	private static String consultarNovEnfermeraStr="SELECT " +
			"ne.codigoregistro AS codigoregistro, " +
			"ne.observacion AS observacion, " +
			"to_char(ne.fechaprogramacion, 'yyyy-mm-dd') AS fechaProgramacion, " +
			"ne.prioridad AS prioridad, " +
			"ne.activo AS activo, " +
			"ne.codigomedico AS codigomedico, " +
			"ne.codigonovedad AS codigoNovedad, " +
			"nov.nomina AS nomina, " +
			"nov.nombre_novedad AS nombrenovedad, " +
			"getNovedadEnfermeraTieneTurnos(ne.codigoregistro) as tieneTurnos " +
			"FROM novedad nov INNER JOIN novedad_enfermera ne ON(ne.codigonovedad=nov.codigo_novedad) " +
			"WHERE (codigomedico=? AND ne.activo=true) " +
			"AND fechaprogramacion>=? " +
			"AND fechaprogramacion<=?";

	
	/**
	 * Consulta todas las novedades que tiene asociada una enferemera 
	 */
	private static String consultarTodasNovedadesEnfermeraStr=
		"SELECT " +
			"ne.codigoregistro AS codigoregistro, " +
			"ne.observacion AS observacion, " +
			"to_char(ne.fechaprogramacion, 'yyyy-mm-dd') AS fechaProgramacion, " +
			"ne.prioridad AS prioridad, " +
			"ne.activo AS activo, " +
			"ne.codigomedico AS codigomedico, " +
			"ne.codigonovedad AS codigoNovedad, " +
			"nov.nomina AS nomina, " +
			"nov.nombre_novedad AS nombrenovedad, " +
			"getNovedadEnfermeraTieneTurnos(ne.codigoregistro) as tieneTurnos " +
		"FROM " +
			"novedad nov INNER JOIN novedad_enfermera ne ON(ne.codigonovedad=nov.codigo_novedad) " +
		"WHERE " +
			"codigomedico=? " +
		"AND " +
			"ne.activo=?" +
		"ORDER BY ne.fechaprogramacion";

	/**
	 * String que contiene la consulta sql que psas una asignación de un novedad de un
	 * estado activo a inactivo
	 */
	private static String inactivarAsociacionStr="UPDATE novedad_enfermera SET activo=? WHERE codigoregistro=?";
	
	/**
	 * 
	 */
	private static String consultarEstaNovEnfStr="SELECT " +
			"ne.codigoregistro AS codigoregistro, " +
			"ne.observacion AS observacion, " +
			"ne.fechaprogramacion AS fechaProgramacion, " +
			"ne.prioridad AS prioridad, " +
			"ne.activo AS activo, " +
			"ne.codigomedico AS codigomedico, " +
			"ne.codigonovedad AS codigoNovedad, " +
			"nov.nombre_novedad AS nombrenovedad " +
			"FROM novedad nov INNER JOIN novedad_enfermera ne ON (ne.codigonovedad=nov.codigo_novedad) WHERE (codigoregistro=?)";
	
	/**
	 * actualiza la tabla novedad_enfermera, cuando ocurre una modificacion
	 */
	private static String modificarNovEnfStr="UPDATE novedad_enfermera SET codigonovedad=?, prioridad=?, observacion=? WHERE fechaprogramacion=? AND codigomedico=?";
	
	/**
	 * String que consulta los turnos de permiso que coinciden con la fecha de una novedad dada de una enfermera, es decir que ya se ha incluido en un cuatro de turnos, para no dejar modificar ni inactivar la novedad
	 */
	private static String consultarNovedadEnfermeraTieneTurnoStr="select count(ctcodigo) as cantidadturnos from ct_turno, novedad_enfermera where novedad_enfermera.codigomedico = ct_turno.codigomedico and ct_turno.ct_fecha = novedad_enfermera.fechaprogramacion and novedad_enfermera.codigoregistro = ?";
	
	/**
	 * Método para ingresar novedades en el sistema
	 * @param con
	 * @param nombre
	 * @param descripcion
	 * @param nomina
	 * @param activo
	 * @return
	 */
	public static int insertarNovedad(Connection con, String nombre, String descripcion, boolean nomina, boolean activo)
	{
		try
		{
			PreparedStatement insertarNov=con.prepareStatement(insertarStr);
			insertarNov.setInt(1, UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_novedad"));
			insertarNov.setString(2, nombre);
			insertarNov.setString(3, descripcion);
			insertarNov.setBoolean(4, nomina);
			insertarNov.setBoolean(5, activo);
			return insertarNov.executeUpdate();
		} catch (SQLException e)
		{
			logger.error("Error insertando Novedad: "+e);
			return 0;
		}
	}
	
	
	
	public static Collection<HashMap<String, Object>> consultarEsta(Connection con, String nombre)
	{
		PreparedStatement consultarModificarNov;
		String consultaString="";
		consultaString=consultarModificarStr;
		consultaString+=" UPPER(nombre_novedad)=UPPER('"+nombre+"')";
							
		try {
			consultarModificarNov = con.prepareStatement(consultaString);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarModificarNov.executeQuery()));
		} catch (SQLException e) {
			logger.error("Error Al consultar la Novedad: "+e);
			return null;
		}
	}
	
	
	/**
	 * (Juan David)
	 * Método para consultar la novedades existentes en BD
	 * @param con
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarNovedades(Connection con) 
	{
		try
		{
			PreparedStatement cargarNovedadesStatement = con.prepareStatement(consultarNovedadesStr);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarNovedadesStatement.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error listando las novedades existentes en la BD "+e);
		}
		return null;
	
	}
	
	public static ResultSet consultarModificar(Connection con, int codigo)
	{
		PreparedStatement consultarModificarNov;
		ResultSet rs=null;
		String consultaString="";
		consultaString=consultarModificarStr;
		consultaString+=" codigo_novedad="+codigo+"";
					
		try
		{
			consultarModificarNov = con.prepareStatement(consultaString);
			rs=consultarModificarNov.executeQuery();
		}
		catch (SQLException e)
		{
			logger.error("Error al consultar la Novedad: "+e);
		}
			
		return rs;
			
	}
	
	/**
	 * Método para modificar las novedades
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param descripcion
	 * @param nomina
	 * @param activo
	 */
	public static void modificar(Connection con, int codigo, String nombre, String descripcion, boolean nomina, boolean activo)
	{
        try
	    {
	        PreparedStatement modificarNovedadStatement;
	        modificarNovedadStatement = con.prepareStatement(modificarNovedadStr);
	        
	        modificarNovedadStatement.setString(1,nombre);
	        modificarNovedadStatement.setString(2,descripcion);
	        modificarNovedadStatement.setBoolean(3, nomina);
            modificarNovedadStatement.setBoolean(4,activo);
	        modificarNovedadStatement.setInt(5,codigo);
	        modificarNovedadStatement.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en la modificacion-> SqlBaseNovedadDao"+e.toString());
	    }
	}
	
	/**
	 * Mono
	 * Metodo que elimina una Novedad en caso que no tenga ninguna relacion
	 * @param con
	 * @param codigo
	 */
	public static int eliminarNovedad(Connection con, int codigo)
	{
		try
		{
			PreparedStatement eliminarNovedad;
			eliminarNovedad = con.prepareStatement(eliminarNovedadStr);
			eliminarNovedad.setInt(1,codigo);
			return eliminarNovedad.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error Al eliminar la novedad: "+e);
			return 0;
		}
	}
	
	/**********************Asignar Novedad a Enfermera ******************/
	
	/**
	 * Consulta las novedades que concuerda con la enfermera y la fecha
	 */
	public static Collection consultarNovedadEnfermera(Connection con, int codigoMedico, String fechaProgramacion)
	{
	    try
	    {
	    	PreparedStatement consultarNovedadEnfermeraSt = con.prepareStatement(consultarNovedadEnfermeraStr);
            consultarNovedadEnfermeraSt.setInt(1,codigoMedico);
            consultarNovedadEnfermeraSt.setString(2,fechaProgramacion);
        	return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNovedadEnfermeraSt.executeQuery()));
        }
	    catch (SQLException e)
	    {
            logger.error("Error consultando la novedad con la enfermera "+e);
            return null;
        }
	}
	
	/**
	 * Método que permite insertar una nueva asociación
	 * de una enfermera a una novedad
	 * @param con
	 * @param codigoEnfermera
	 * @param codigo
	 * @param prioridad
	 * @param fechaProgramacion
	 * @param observacion
	 * @param fechaRegistro
	 * @param activoAsociacion
	 * @return
	 */
	public static int insertarNovedadEnfermera(Connection con, int codigoEnfermera, int codigo, boolean prioridad, String fechaProgramacion, String observacion)
	{
        try
        {
        	PreparedStatement insertarNovedadEnfermeraSt;
        	insertarNovedadEnfermeraSt=con.prepareStatement(consultarRegistroNovedadEnfermeraStr);
    	    insertarNovedadEnfermeraSt.setInt(1, codigoEnfermera);
    	    insertarNovedadEnfermeraSt.setInt(2, codigo);
    	    insertarNovedadEnfermeraSt.setString(3, fechaProgramacion);
        	ResultSet resultado=insertarNovedadEnfermeraSt.executeQuery();
        	if(resultado.next())
        	{
        		boolean activo=resultado.getBoolean("activo");
        		int codigoAsociacion=resultado.getInt("codigo");
        		if(!activo)
        		{
        			return cambiarEstadoAsociacion(con, codigoAsociacion, true);
        		}
        	}
            insertarNovedadEnfermeraSt = con.prepareStatement(insertarNovedadEnfermeraStr);
            insertarNovedadEnfermeraSt.setInt(1, UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_novedad_enfermera"));
    	    insertarNovedadEnfermeraSt.setInt(2, codigoEnfermera);
    	    insertarNovedadEnfermeraSt.setInt(3, codigo);
    	    insertarNovedadEnfermeraSt.setBoolean(4, prioridad);
    	    insertarNovedadEnfermeraSt.setString(5, fechaProgramacion);
    	    insertarNovedadEnfermeraSt.setString(6,observacion);
    		return insertarNovedadEnfermeraSt.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.error(" Error insertando la asociacion de novedad a enfermera"+e);
            return 0;
        }
        
	}
	
	/**
	 * Metodo que permite cambiar de estado una asociacion de novedad a enfermera
	 * @param con
	 * @param codigoAsociacion
	 * @return
	 */
	public static int cambiarEstadoAsociacion(Connection con, int codigoAsociacion, boolean activo)
	{
	    PreparedStatement inactivarAsociacionSt;
	    
	    try {
            inactivarAsociacionSt= con.prepareStatement(inactivarAsociacionStr);
            inactivarAsociacionSt.setBoolean(1, activo );
            inactivarAsociacionSt.setInt(2,codigoAsociacion);
    	    return inactivarAsociacionSt.executeUpdate(); 
        } catch (SQLException e) {
            logger.error(" Error inactivando la asociacion de novedad a enfermera"+e);
            return 0;
        }
	}
	
	
	
	/**
	 * Consulta las novedades que se encuentran asociadas a una enfermera, que aun est activas
	 * y que su fecha de programación es mayor o igual al día de hoy
	 * @param fechaInicio
	 * @param fechaFin
	 */
	public static Collection consultarNovEnfermera(Connection con, int codigoMedico, String fechaInicio, String fechaFin)
	{
	    try
	    {
            PreparedStatement consultarNovEnfermeraSt = con.prepareStatement(consultarNovEnfermeraStr);
            consultarNovEnfermeraSt.setInt(1,codigoMedico);
            consultarNovEnfermeraSt.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
            consultarNovEnfermeraSt.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
        	return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNovEnfermeraSt.executeQuery()));
        }
	    catch (SQLException e)
	    {
            logger.error("Error consultando la novedad con la enfermera "+e);
            return null;
        }
	
	   
	}
	
	/**
	 * Consulta todas las novedades que se encuentran asociadas a una enfermera
	 */
	public static Collection consultarTodasNovedadesEnfermera(Connection con, int codigoMedico)
	{
		try
		{
			PreparedStatement consultarNovEnfermeraSt = con.prepareStatement(consultarTodasNovedadesEnfermeraStr);
			consultarNovEnfermeraSt.setInt(1,codigoMedico);
			consultarNovEnfermeraSt.setBoolean(2, true);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNovEnfermeraSt.executeQuery()));
		} catch (SQLException e) {
			logger.error("Error consultando la novedad con la enfermera "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar una asociacion específicamente
	 * @param con
	 * @param codigoAsociacion
	 * @return
	 */
	public static Collection consultarEstaNovEnf(Connection con, int codigoAsociacion)
	{
		PreparedStatement consultarEstaNovEnfSt;
		try
		{
			//System.out.println("consultarEstaNovEnfStr "+consultarEstaNovEnfStr+" "+codigoAsociacion);
			consultarEstaNovEnfSt = con.prepareStatement(consultarEstaNovEnfStr);
			consultarEstaNovEnfSt.setInt(1,codigoAsociacion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarEstaNovEnfSt.executeQuery()));
        }catch (SQLException e)
        {
            logger.error("Error consultando asociación "+codigoAsociacion+": "+e);
            return null;
        }
	}
	
	/**
	 * Consulta si un registro de novedad_enfermera coincide con fechas del cuadro de turnos
	 * @param con
	 * @param codigoRegistro
	 * @return
	 * @throws SQLException
	 */
	public static boolean consultarNovedadEnfermeraTieneTurno(Connection con, int codigoRegistro) throws SQLException
	{
		PreparedStatement consultarStatement;
		try
		{
			consultarStatement = con.prepareStatement(SqlBaseNovedadDao.consultarNovedadEnfermeraTieneTurnoStr);
			consultarStatement.setInt(1, codigoRegistro);
			ResultSet rs = consultarStatement.executeQuery();
			rs.next();
			if(rs.getInt("cantidadturnos")>0)
				return true;
			else
				return false;
		}
		catch(SQLException se)
		{
			logger.error("Error consultando novedad enfermera tiene turno: "+se.getMessage());
			throw se;
		}
	}
	
	
	/**
	 * ejecuta la sentencia que actualiza los datos de la tabla novedad_enfermera
	 * cuando se modifica
	 * @param con
	 * @param codigoPersona
	 * @param codigoNovedad
	 * @param fechaProgramacion
	 * @param prioridad
	 * @param observacion
	 * @return
	 */
	public static int modificarNovedadEnfermera(Connection con, int codigoPersona, int codigoNovedad, String fechaProgramacion, boolean prioridad, String observacion)
	{
	    PreparedStatement modificarNovedadEnfermeraSt;
        try {
            
            modificarNovedadEnfermeraSt = con.prepareStatement(modificarNovEnfStr);
            modificarNovedadEnfermeraSt.setInt(1, codigoNovedad);
            modificarNovedadEnfermeraSt.setBoolean(2, prioridad);
            modificarNovedadEnfermeraSt.setString(3, observacion);
            modificarNovedadEnfermeraSt.setString(4, fechaProgramacion);
            modificarNovedadEnfermeraSt.setInt(5, codigoPersona);
                           	    
    				
    		return modificarNovedadEnfermeraSt.executeUpdate();
        } catch (SQLException e) {
            logger.error(" Error modificando la asociacion de novedad a Enfermera"+e);
            return 0;
        }
	}
	
	/**
	 * Método para consultar las novedades que tiene asociada una enfermera en un periodo determinado
	 * incluida la opcion de todas las enfermeras
	 * @param con
	 * @param codigoProfesional
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoNovedad
	 * @param codigoInstitucion
	 * @return
	 */
	public static Collection consultarTurnosReporteNov(Connection con, int codigoProfesional, String fechaInicio, String fechaFin, String fechaProgramacion, int codigoNovedad)
	{
		/**
		 * String que consulta en la base de datos las novedades asociadas a una o todas
		 * las enfermeras que se encuentra en la base de datos
		 */
		String consulta=
			"select " +
			"codigomedico as codigoEnfermera, " +
			"getnombrepersonasies(codigomedico) as nombreEnfermera, " +
			"codigonovedad as codigoNovedad, " +
			"nombre_novedad as nombreNovedad, " +
			"getfechainicionovedad(fechaprogramacion,codigomedico,codigonovedad) as fechainicio, " +
			"getfechafinnovedad(fechaprogramacion,codigomedico,codigonovedad) as fechafin, " +
			"prioridad as prioridad, " +
			"observacion as observacion, " +
			" '' as costo " +
			"from novedad_enfermera, novedad " +
			"where novedad_enfermera.codigonovedad = novedad.codigo_novedad ";
		
		/**
		 * select 
		 * 
		 * 	private static String consultarTurnosReporteNovtr=
		 * "SELECT per.codigo AS codigo, 
		 * UPPER(getnombremedico(codigo)) AS nombre, 
		 * ne.fechaprogramacion AS fecha, 
		 * ne.prioridad AS prioridad, 
		 * nov.nombre_novedad AS nombrenovedad " +
	"FROM personas per "+
	"INNER JOIN novedad_enfermera ne ON(ne.codigomedico=per.codigo) " +
	"INNER JOIN novedad nov ON(nov.codigo_novedad=ne.codigonovedad) ";

		 */
		if (codigoProfesional!=0)
			consulta+="AND codigomedico="+codigoProfesional+" ";
		
		if(UtilidadCadena.noEsVacio(fechaInicio))
			consulta+="AND fechaprogramacion>='"+UtilidadFecha.conversionFormatoFechaABD(fechaInicio)+"' ";
		
		if(UtilidadCadena.noEsVacio(fechaFin))
			consulta+= "AND fechaprogramacion<='"+UtilidadFecha.conversionFormatoFechaABD(fechaFin)+"' ";
		
		if(codigoNovedad!=0)
			consulta+="AND novedad_enfermera.codigonovedad="+codigoNovedad+" ";
		
		consulta+="GROUP BY codigoEnfermera, nombreEnfermera, codigoNovedad, nombreNovedad, fechainicio, fechafin, prioridad, observacion, costo ";
		consulta+=" ORDER BY nombreEnfermera ASC";
		
		try
		{
			//System.out.println(consulta);
			PreparedStatement consultarTurnosReporteNovSt = con.prepareStatement(consulta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarTurnosReporteNovSt.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 * Método que cambia las novedades de una persona en un rango de fechas específico
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoProfesional
	 * @param activo
	 * @return Número de asociaciones modificadas
	 */
	public static int cambiarEstadoAsociacion(Connection con, String fechaInicio, String fechaFin, int codigoProfesional, boolean activo)
	{
		String consulta="UPDATE novedad_enfermera SET activo=?, fecharegistro=CURRENT_DATE WHERE fechaprogramacion=? AND codigomedico=?";
		int numeDias=UtilidadFecha.numeroDiasEntreFechas(fechaInicio, fechaFin);
		String fecha=fechaInicio;
		int resultado=0;
		for(int i=0; i<numeDias; i++)
		{
			try
			{
				PreparedStatement stm=con.prepareStatement(consulta);
				stm.setBoolean(1, activo);
				stm.setString(2, UtilidadFecha.conversionFormatoFechaABD(fecha));
				stm.setInt(3, codigoProfesional);
				resultado+=stm.executeUpdate();
				fecha=UtilidadFecha.incrementarDiasAFecha(fecha, 1, false);
			}catch (SQLException e)
			{
				logger.error("Error modificando el estado de la asociación");
				return 0;
			}
		}
		return resultado;
	}
}
