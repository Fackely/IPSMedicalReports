package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.MonitoreoHemodinamicaDao;
import com.princetonsa.dto.salas.DtoMonitoreoHemoDinamica;

/**
 * 
 * @author wilson
 *
 */
public class MonitoreoHemodinamica 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static MonitoreoHemodinamicaDao monitoreoDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public MonitoreoHemodinamica() 
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			monitoreoDao = myFactory.getMonitoreoHemodinamicaDao();
			wasInited = (monitoreoDao != null);
		}
		return wasInited;
	}
	

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> obtenerListadoMonitoreos(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().obtenerListadoMonitoreos(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static DtoMonitoreoHemoDinamica cargarMonitoreoHemoDto(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().cargarMonitoreoHemoDto(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ArrayList<InfoDatosInt> cargarOpciones(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().cargarOpciones(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreoHemoHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarMonitoreo(Connection con, int codigoMonitoreoHemoHojaAnestesia, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().cargarMonitoreo(con, codigoMonitoreoHemoHojaAnestesia, centroCosto, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean insertarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().insertarDetMonHemoHojaAnestesia(con, codigoMonitoreo, codigoCampo, valor);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @return
	 */
	public static int insertarMonHemoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().insertarMonHemoHojaAnestesia(con, numeroSolicitud, fecha, hora, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean modificarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().modificarDetMonHemoHojaAnestesia(con, codigoMonitoreo, codigoCampo, valor);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param codigoMonHemoHojaAnes
	 * @return
	 */
	public static boolean modificarMonHemoHojaAnestesia(Connection con, String fecha, String hora, String loginUsuario, int codigoMonHemoHojaAnes)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().modificarMonHemoHojaAnestesia(con, fecha, hora, loginUsuario, codigoMonHemoHojaAnes);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean eliminarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().eliminarDetMonHemoHojaAnestesia(con, codigoMonitoreo, codigoCampo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean eliminarMonHemoHojaAnestesia(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().eliminarMonHemoHojaAnestesia(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param codigoMonitoreoHojaAnestsia
	 * @return
	 */
	public static boolean existeMonitoreoHojaAnestesia(int numeroSolicitud, String fecha, String hora, int codigoMonitoreoHojaAnestsia)
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean existe=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMonitoreoHemodinamicaDao().existeMonitoreoHojaAnestesia(con, numeroSolicitud, fecha, hora, codigoMonitoreoHojaAnestsia);
		UtilidadBD.closeConnection(con);
		return existe;
	}
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> cargarCamposOtrasSecciones(Connection con, int numeroSolicitud, int institucion)
	{
		HashMap<String, Object> mapa= new HashMap<String, Object>();
		
		Vector<String> pesoTalla= InfoGeneralHA.cargarPesoTalla(con, numeroSolicitud, institucion);
		if(pesoTalla.size()>1)
		{
			mapa.put("peso", pesoTalla.get(0));
			mapa.put("talla", pesoTalla.get(1));
		}
		else
		{
			mapa.put("peso", "");
			mapa.put("talla", "");
		}
		
		//@todo NO ESPECIFICAN EN EL DOCUMENTO EL VALOR A CARGAR
		mapa.put("tam", 1);
		mapa.put("fc", 1);
		return mapa;
	}
}