package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ExcepcionesCCInterconsultasDao;

/**
 * 
 * Mundo de Excepciones CC Interconsutlas
 * @author jfhernandez@princetonsa.com
 *
 */
public class ExcepcionesCCInterconsultas {
	
	// --------------- 	ATRIBUTOS
	
	private static Logger logger = Logger.getLogger(ExcepcionesCCInterconsultas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ExcepcionesCCInterconsultasDao ExcepcionesCCInterconsultasDao;
	
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
			ExcepcionesCCInterconsultasDao = myFactory.getExcepcionesCCInterconsultasDao();
			wasInited = (ExcepcionesCCInterconsultasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * ConsultarXCentroAtencion
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultarXCentroAtencion(Connection con,String centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().consultarXCentroAtencion(con,centroAtencion);
	}

	/**
	 * guardarExcepcion
	 * @param con
	 * @param listadoXCentroAtencion
	 * @param login 
	 * @param institucion
	 * @return
	 */
	public static boolean guardarExcepcion(Connection con, HashMap listadoXCentroAtencion, String login, String institucion, String centroAtencion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().guardarExcepcion(con, listadoXCentroAtencion, login, institucion, centroAtencion);
	}
	
	/**
	 * modificarExcepcion
	 * @param con
	 * @param listadoXCentroAtencion
	 * @return
	 */
	public static boolean eliminarExcepcion(Connection con, int indice) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().eliminarExcepcion(con ,indice);
	}
	/**
	 * Obtener Medicos
	 * @param con
	 * @param Institucion
	 * @return
	 */
	public static HashMap obtenerMedicos(Connection con,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().obtenerMedicos(con,institucion);
	}
	
	/**
	 * modificarExcepcion
	 * @param con
	 * @param listadoXCentroAtencion
	 * @param login 
	 * @param institucion
	 * @return
	 */
	public static boolean modificarExcepcion(Connection con, HashMap listadoXCentroAtencion, String login, String institucion, String centroAtencion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().modificarExcepcion(con, listadoXCentroAtencion, login, institucion, centroAtencion);
	}
	
	/**
	 * obtenerCentrosCosto
	 * @param con
	 * @param centroAtencion
	 * @param tipoAreaDirecto
	 * @return
	 */
	public static HashMap obtenerCentrosCosto(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().obtenerCentrosCosto(con, centroAtencion,tipoAreaDirecto, institucion);
	}
	
	/**
	 * obtenerCentrosCostoEjecutan
	 * @param con
	 * @param centroAtencion
	 * @param tipoAreaDirecto
	 * @return
	 */
	public static HashMap obtenerCentrosCostoEjecutan(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesCCInterconsultasDao().obtenerCentrosCostoEjecutan(con, centroAtencion,tipoAreaDirecto, institucion);
	}
	
	
	
}