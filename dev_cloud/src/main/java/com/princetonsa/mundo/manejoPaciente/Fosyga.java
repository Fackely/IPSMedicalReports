package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.FosygaDao;

/**
 * Mundo de fosyga
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class Fosyga 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static FosygaDao fosygaDao;
	
	/**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		
	}
	
	/**
	 * constructor de la clase
	 *
	 */
	public Fosyga() 
	{
		reset();
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
			fosygaDao = myFactory.getFosygaDao();
			wasInited = (fosygaDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public static HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFosygaDao().busquedaAvanzada(con, criteriosBusquedaMap);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param esAccidenteTransito
	 * @return
	 */
	public static String obtenerQueryAnexoGastosTransporte(String codigo, boolean esAccidenteTransito)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFosygaDao().obtenerQueryAnexoGastosTransporte(codigo, esAccidenteTransito);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param esRegistroAccidenteTransito
	 * @return
	 */
	public static boolean existeInfoGastosTransporteMovilizacionVictima(Connection con, int codigo, boolean esRegistroAccidenteTransito)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFosygaDao().existeInfoGastosTransporteMovilizacionVictima(con, codigo, esRegistroAccidenteTransito);
	}
	
}
