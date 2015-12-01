package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.SoportesFacturasDao;

/**
 * 
 * Mundo de Soporte de Facturas
 * @author jfhernandez@princetonsa.com
 *
 */
public class SoportesFacturas {
	
	// --------------- 	ATRIBUTOS
	
	private static Logger logger = Logger.getLogger(SoportesFacturas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static SoportesFacturasDao soportesFacturasDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	// ----------------	METODOS
	
	/**
	 * Reset
	 */
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
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
			soportesFacturasDao = myFactory.getSoportesFacturasDao();
			wasInited = (soportesFacturasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Consltar
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap obtenerTiposSoporte(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSoportesFacturasDao().obtenerTiposSoporte(con);
	}

	/**
	 * Guardar
	 * @param con
	 * @param mapaOrganizado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 */
	public static boolean guardar(Connection con, HashMap<String, Object> mapaOrganizado, int codigoInstitucionInt, String loginUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSoportesFacturasDao().guardar(con, mapaOrganizado, codigoInstitucionInt, loginUsuario);
	}

	/**
	 * Consultar
	 * @param con
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap<String, Object> consultar(Connection con,HashMap<String, Object> tiposSoporteMap,HashMap<String, Object> soportesFacturasMap,int codigoInstitucionInt) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSoportesFacturasDao().consultar(con, tiposSoporteMap, soportesFacturasMap, codigoInstitucionInt);
	}
	
	/**
	 * obtener Tipos Soporte Parametrizados por el usuario
	 * @param con
	 * @param tiposSoporteMap
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap<String, Object> obtenerTiposSoporteXConvenio(Connection con, int institucion, int viaIngreso, int convenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSoportesFacturasDao().obtenerTiposSoporteXConvenio(con, institucion, viaIngreso, convenio);
	}
	
	
}