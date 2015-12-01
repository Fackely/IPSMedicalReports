package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUbicacionGeograficaDao;
import com.princetonsa.decorator.ResultSetDecorator;


public class SqlBaseUbicacionGeograficaDao {
	

	
	
//INICIO DE CADENAS DE INSERCION	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBaseUbicacionGeograficaDao.class);
	
	/**
	 * cadena para la insercion de pais
	 */
	private static final String cadenaInsertarPais="INSERT INTO paises (codigo_pais, descripcion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la insercion de departamento
	 */
	private static final String cadenaInsertarDepartamento="INSERT INTO administracion.departamentos (codigo_departamento, descripcion, usuario_modifica, fecha_modifica, hora_modifica, codigo_pais) VALUES (?, ?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la insercion de ciudad
	 */
	private static final String cadenaInsertarCiudad="INSERT INTO ciudades (codigo_departamento, codigo_ciudad, descripcion, codigo_pais, localidad, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la insercion de localidad
	 */
	private static final String cadenaInsertarLocalidad="INSERT INTO localidades (codigo_localidad, descripcion, codigo_pais, codigo_departamento, codigo_ciudad, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	 
	
//FIN PARA LAS CADENAS DE INSERCION
	
	
	
	
	
//INICIO DE LAS CADENAS DE CONSULTA
	
	/**
	 * Cadena para la consulta de pais
	 */
	private static final String cadenaConsultaPais= "SELECT " +
															"codigo_pais as codigo_pais, descripcion as descripcion," +
															"'BD' as tiporegistro " +
															"FROM paises ";
	
	/**
	 * Cadena para la consulta de departamento
	 */
	private static final String cadenaConsultaDepartamento= "SELECT " +
															"codigo_departamento as codigo_departamento, descripcion as descripcion," +
															"'BD' as tiporegistro " +
															"FROM departamentos ";
	
	/**
	 * Cadena para la consulta de ciudad
	 */
	private static final String cadenaConsultaCiudad= "SELECT " +
															"codigo_departamento as codigo_departamento, codigo_ciudad as codigo_ciudad, "+
															"descripcion as descripcion, codigo_pais as codigo_pais, localidad as localidad, " +
															"'BD' as tiporegistro " +
															"FROM ciudades ";
	
	/**
	 * Cadena para la consulta de localidad
	 */
	private static final String cadenaConsultaLocalidad= "SELECT " +
															"codigo_localidad as codigo_localidad, descripcion as descripcion, "+
															"codigo_pais as codigo_pais, codigo_departamento as codigo_departamento, "+
															"codigo_ciudad as codigo_ciudad, "+
															"'BD' as tiporegistro " +
															"FROM localidades ";
	
	/**
	 * Cadena para la consulta de barrio
	 */
	private static final String cadenaConsultaBarrio= "SELECT " +
															"codigo_departamento as codigo_departamento, codigo_ciudad as codigo_ciudad, "+
															"codigo_barrio as codigo_barrio, descripcion as descripcion,  "+
															"codigo_pais as codigo_pais, codigo_localidad as codigo_localidad, "+
															"codigo as codigo, 'BD' as tiporegistro " +
															"FROM barrios ";

//FIN PARA LAS CADENAS DE CONSULTA
	
	
	
	
//INICIO PARA LAS CADENAS DE MODIFICACION
	
	/**
	 * cadena para la modificacion de pais
	 */
	private static final String cadenaModificacionPais="UPDATE paises SET descripcion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo_pais=?";
	
	/**
	 * cadena para la modificacion de departamento
	 */
	private static final String cadenaModificacionDepartamento="UPDATE departamentos SET descripcion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo_departamento=? AND codigo_pais=?";
	
	/**
	 * cadena para la modificacion de ciudad
	 */
	private static final String cadenaModificacionCiudad="UPDATE ciudades SET descripcion=?, localidad=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo_ciudad=? AND codigo_departamento=? AND codigo_pais=?";
	
	/**
	 * cadena para la modificacion de localidad
	 */
	private static final String cadenaModificacionLocalidad="UPDATE localidades SET descripcion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo_localidad=? AND codigo_ciudad=? AND codigo_departamento=? AND codigo_pais=?";
	
	/**
	 * cadena para la modificacion de barrio
	 */
	private static final String cadenaModificacionBarrio="UPDATE barrios SET descripcion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=?";
	
//FIN PARA LAS CADENAS DE MODIFICACION
	
	
	
	
	
	
	
//INICIO PARA LAS CADENAS DE ELIMINACION
	
	/**
	 *Cadena para la eliminacion de pais 
	 */
	private static final String cadenaEliminacionPais="DELETE FROM paises WHERE codigo_pais=?";
	
	/**
	 *Cadena para la eliminacion de departamento
	 */
	private static final String cadenaEliminacionDepartamento="DELETE FROM departamentos WHERE codigo_departamento=? AND codigo_pais=?";
	
	/**
	 *Cadena para la eliminacion de ciudad
	 */
	private static final String cadenaEliminacionCiudad="DELETE FROM ciudades WHERE codigo_ciudad=? AND codigo_departamento=? AND codigo_pais=?";
	
	/**
	 *Cadena para la eliminacion de localidad
	 */
	private static final String cadenaEliminacionLocalidad="DELETE FROM localidades WHERE codigo_localidad=? AND codigo_ciudad=? AND codigo_departamento=? AND codigo_pais=?";
	
	/**
	 *Cadena para la eliminacion de barrio
	 */
	private static final String cadenaEliminacionBarrio="DELETE FROM barrios WHERE codigo=?";
	
//FIN PARA LAS CADENAS DE ELIMINACION
	
	
	
	
	
//////////////////////////////INICIO INSERTAR////////////////////////////////// 
	
	/**
	 * INSERTAR PAIS
	 * */
	public static boolean insertarPais(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarPais,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo_pais")+"");
			ps.setString(2, vo.get("descripcion")+"");
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setString(4, vo.get("fecha_modifica")+"");
			ps.setString(5, vo.get("hora_modifica")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * INSERTAR DEPARTAMENTO
	 * */
	public static boolean insertarDepartamento(Connection con, HashMap vo)
	{
	  
		
		try
		{
				
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarDepartamento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, vo.get("codigo_departamento")+"");
			ps.setString(2, vo.get("descripcion")+"");
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setString(4, vo.get("fecha_modifica")+"");
			ps.setString(5, vo.get("hora_modifica")+"");
			ps.setString(6, vo.get("codigo_pais")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * INSERTAR CIUDAD
	 * */
	public static boolean insertarCiudad(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarCiudad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo_departamento")+"");
			ps.setString(2, vo.get("codigo_ciudad")+"");
			ps.setString(3, vo.get("descripcion")+"");
			ps.setString(4, vo.get("codigo_pais")+"");
			if(UtilidadTexto.isEmpty(vo.get("localidad")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("localidad")+"");
			ps.setString(6, vo.get("usuario_modifica")+"");
			ps.setString(7, vo.get("fecha_modifica")+"");
			ps.setString(8, vo.get("hora_modifica")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * INSERTAR LOCALIDAD
	 * */
	public static boolean insertarLocalidad(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarLocalidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo_localidad")+"");
			ps.setString(2, vo.get("descripcion")+"");
			ps.setString(3, vo.get("codigo_pais")+"");
			ps.setString(4, vo.get("codigo_departamento")+"");
			ps.setString(5, vo.get("codigo_ciudad")+"");
			ps.setString(6, vo.get("usuario_modifica")+"");
			ps.setString(7, vo.get("fecha_modifica")+"");
			ps.setString(8, vo.get("hora_modifica")+"");
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * INSERTAR BARRIO
	 * */
	public static boolean insertarBarrio(Connection con, HashMap vo, String cadenaInsertarBarrio)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarBarrio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo_departamento")+"");
			ps.setString(2, vo.get("codigo_ciudad")+"");
			ps.setString(3, vo.get("codigo_barrio")+"");
			ps.setString(4, vo.get("descripcion")+"");
			ps.setString(5, vo.get("codigo_pais")+"");
			if(UtilidadTexto.isEmpty(vo.get("codigo_localidad")+"")||(vo.get("codigo_localidad").equals("null")))
				ps.setObject(6, null);
			else
				ps.setString(6, vo.get("codigo_localidad")+"");
			ps.setString(7, vo.get("usuario_modifica")+"");
			ps.setString(8, vo.get("fecha_modifica")+"");
			ps.setString(9, vo.get("hora_modifica")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

//////////////////////////////FIN INSERTAR//////////////////////////////////
	
	
	
	
	
	
	
//////////////////////////////INICIO CONSULTAR//////////////////////////////////
	
	/**
	 * consultar pais
	 */
	public static HashMap consultarPais(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaPais;
		
		try
		{
			if(vo.containsKey("codigo_pais")&&!UtilidadTexto.isEmpty(vo.get("codigo_pais")+""))
			{
				cadena+=" where codigo_pais='"+vo.get("codigo_pais")+"'";
			}
			cadena+=" ORDER BY descripcion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * consultar departamento
	 */
	public static HashMap consultarDepartamento(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaDepartamento;
		
		try
		{
			cadena+=" where codigo_pais='"+vo.get("codigo_pais")+"'";
			if(vo.containsKey("codigo_departamento"))
				cadena+=" and codigo_departamento='"+vo.get("codigo_departamento")+"'";
			cadena+=" ORDER BY descripcion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * consultar ciudad
	 */
	public static HashMap consultarCiudad(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaCiudad;
		
		try
		{
			cadena+=" where codigo_departamento='"+vo.get("codigo_departamento")+"'";
			cadena+=" and codigo_pais='"+vo.get("codigo_pais")+"'";
			cadena+=" ORDER BY descripcion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * consultar localidad
	 */
	public static HashMap consultarLocalidad(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaLocalidad;
		
		try
		{
			cadena+=" where codigo_ciudad='"+vo.get("codigo_ciudad")+"'";
			cadena+=" and codigo_departamento='"+vo.get("codigo_departamento")+"'";
			cadena+=" and codigo_pais='"+vo.get("codigo_pais")+"'";
			cadena+=" ORDER BY descripcion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * consultar barrio
	 */
	public static HashMap consultarBarrio(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaBarrio;
		
		try
		{
			cadena+=" WHERE codigo_ciudad='"+vo.get("codigo_ciudad")+"'";
			if(vo.containsKey("codigo_localidad")&&(!vo.get("codigo_localidad").equals("null"))&&(!vo.get("codigo_localidad").equals(ConstantesBD.codigoNuncaValido)))
				cadena+=" AND codigo_localidad='"+vo.get("codigo_localidad")+"'";
			cadena+=" AND codigo_departamento='"+vo.get("codigo_departamento")+"'";
			cadena+=" AND codigo_pais='"+vo.get("codigo_pais")+"'";
			cadena+=" ORDER BY descripcion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
		
//////////////////////////////FIN CONSULTAR////////////////////////////////////
	
	
	
	
	
	
	
//////////////////////////////INICIO MODIFICAR////////////////////////////////////
	
	/**
	 * MODIFICAR PAIS
	 * */
	public static boolean modificarPais(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionPais,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("usuario_modifica")+"");
			ps.setString(3, vo.get("fecha_modifica")+"");
			ps.setString(4, vo.get("hora_modifica")+"");
			ps.setString(5, vo.get("codigo_pais")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * MODIFICAR DEPARTAMENTO
	 * */
	public static boolean modificarDepartamento(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionDepartamento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("usuario_modifica")+"");
			ps.setString(3, vo.get("fecha_modifica")+"");
			ps.setString(4, vo.get("hora_modifica")+"");
			ps.setString(5, vo.get("codigo_departamento")+"");
			ps.setString(6, vo.get("codigo_pais")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * MODIFICAR CIUDAD
	 * */
	public static boolean modificarCiudad(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionCiudad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("descripcion")+"");
			if(UtilidadTexto.isEmpty(vo.get("localidad")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("localidad")+"");
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setString(4, vo.get("fecha_modifica")+"");
			ps.setString(5, vo.get("hora_modifica")+"");
			ps.setString(6, vo.get("codigo_ciudad")+"");
			ps.setString(7, vo.get("codigo_departamento")+"");
			ps.setString(8, vo.get("codigo_pais")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * MODIFICAR LOCALIDAD
	 * */
	public static boolean modificarLocalidad(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionLocalidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("usuario_modifica")+"");
			ps.setString(3, vo.get("fecha_modifica")+"");
			ps.setString(4, vo.get("hora_modifica")+"");
			ps.setString(5, vo.get("codigo_localidad")+"");
			ps.setString(6, vo.get("codigo_ciudad")+"");
			ps.setString(7, vo.get("codigo_departamento")+"");
			ps.setString(8, vo.get("codigo_pais")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * MODIFICAR BARRIO
	 * */
	public static boolean modificarBarrio(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionBarrio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("usuario_modifica")+"");
			ps.setString(3, vo.get("fecha_modifica")+"");
			ps.setString(4, vo.get("hora_modifica")+"");
			ps.setString(5, vo.get("codigo")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
//////////////////////////////FIN MODIFICAR////////////////////////////////////
	
	
	
	
	
	
	
//////////////////////////////INICIO ELIMINACION////////////////////////////////////
	
	/**
	 * Eliminar pais
	 * */
	public static boolean eliminarPais(Connection con, String codigo)
	{
		try
		{
			logger.info("cadenaEliminacionPais------------>"+cadenaEliminacionPais);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionPais,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar departamento
	 * */
	public static boolean eliminarDepartamento(Connection con, String codigo_departamento,String codigo_pais)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionDepartamento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo_departamento);
			ps.setString(2, codigo_pais);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar ciudad
	 * */
	public static boolean eliminarCiudad(Connection con, String codigo_ciudad, String codigo_departamento, String codigo_pais)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionCiudad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo_ciudad);
			ps.setString(2, codigo_departamento);
			ps.setString(3, codigo_pais);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar localidad
	 * */
	public static boolean eliminarLocalidad(Connection con, String codigo_localidad, String codigo_ciudad, String codigo_departamento, String codigo_pais)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionLocalidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo_localidad);
			ps.setString(2, codigo_ciudad);
			ps.setString(3, codigo_departamento);
			ps.setString(4, codigo_pais);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar barrio
	 * */
	public static boolean eliminarBarrrio(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionBarrio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
//////////////////////////////FIN ELIMINACION////////////////////////////////////
	
}
