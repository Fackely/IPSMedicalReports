package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.CalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;

/**
 * mundo de Calidad de Atencion
 * @author axioma
 *
 */
public class CalidadAtencion
{
	private static Logger logger = Logger.getLogger(EventosAdversos.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static CalidadAtencionDao calidadAtencionDao;
	
	/**
	 * Login Usuario
	 */
	private String usuario;
	
	/**
	 * Login viasDeIngreso
	 */
	private String viasDeIngreso;
	
	/**
	 * Login tiposPaciente
	 */
	private String tiposPaciente;
	
	/**
	 * Mapa con los filtros ingresados
	 */
	private HashMap filtrosMap;
	
	/**
	 *
	 */
	public CalidadAtencion()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	public void reset() 
	{
		this.usuario="";
		this.filtrosMap=new HashMap();
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
			calidadAtencionDao = myFactory.getCalidadAtencionDao();
			wasInited = (calidadAtencionDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereSatisfaccionGeneral(Connection con, CalidadAtencion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalidadAtencionDao().crearWhereSatisfaccionGeneral(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereMotCalificacionCalidadAtencion(Connection con, CalidadAtencion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalidadAtencionDao().crearWhereMotCalificacionCalidadAtencion(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean registrarGeneracionReporte(Connection con, CalidadAtencion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalidadAtencionDao().registrarGeneracionReporte(con, mundo);
	}
	
	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public static void setLogger(Logger logger) {
		CalidadAtencion.logger = logger;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return the calidadAtencionDao
	 */
	public static CalidadAtencionDao getCalidadAtencionDao() {
		return calidadAtencionDao;
	}

	/**
	 * @param calidadAtencionDao the calidadAtencionDao to set
	 */
	public static void setCalidadAtencionDao(CalidadAtencionDao calidadAtencionDao) {
		CalidadAtencion.calidadAtencionDao = calidadAtencionDao;
	}

	/**
	 * @return the filtrosMap
	 */
	public HashMap getFiltrosMap() {
		return filtrosMap;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(HashMap filtrosMap) {
		this.filtrosMap = filtrosMap;
	}
	
	/**
	 * @return the filtrosMap
	 */
	public Object getFiltrosMap(String llave) {
		return filtrosMap.get(llave);
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(String llave, Object obj) {
		this.filtrosMap.put(llave, obj);
	}

	/**
	 * @return the viasDeIngreso
	 */
	public String getViasDeIngreso() {
		return viasDeIngreso;
	}

	/**
	 * @param viasDeIngreso the viasDeIngreso to set
	 */
	public void setViasDeIngreso(String viasDeIngreso) {
		this.viasDeIngreso = viasDeIngreso;
	}

	/**
	 * @return the tiposPaciente
	 */
	public String getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(String tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}
	
	
}	