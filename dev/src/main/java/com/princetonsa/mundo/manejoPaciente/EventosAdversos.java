package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;

/**
 * mundo de Motivos Satisfaccion Insatisfaccion
 * @author axioma
 *
 */
public class EventosAdversos
{
	private static Logger logger = Logger.getLogger(EventosAdversos.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EventosAdversosDao eventosAdversosDao;
	
	/**
	 * Codigo de la Institución
	 */
	private String institucion;
	
	/**
	 * Login Usuario
	 */
	private String usuario;
	
	/**
	 * Mapa de Motivos de satisfaccion e insatisfacion
	 */
	private HashMap eventosMap;
	
	/**
	 *
	 */
	public EventosAdversos()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	public void reset() 
	{
		this.institucion="";
		this.usuario="";
		this.eventosMap=new HashMap();
		this.eventosMap.put("numRegistros", "0");
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
			eventosAdversosDao = myFactory.getEventosAdversosDao();
			wasInited = (eventosAdversosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultar(Connection con, EventosAdversos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosAdversosDao().consultar(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean ingresar(Connection con, EventosAdversos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosAdversosDao().ingresar(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean modificar(Connection con, EventosAdversos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosAdversosDao().modificar(con, mundo);
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean inactivar(Connection con, EventosAdversos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEventosAdversosDao().inactivar(con, mundo);
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
		EventosAdversos.logger = logger;
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
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the motivosMap
	 */
	public HashMap getEventosMap() {
		return eventosMap;
	}

	/**
	 * @param motivosMap the motivosMap to set
	 */
	public void setEventosMap(HashMap motivosMap) {
		this.eventosMap = motivosMap;
	}
	
	/**
	 * @return the motivosMap
	 */
	public Object getEventosMap(String llave) {
		return eventosMap.get(llave);
	}

	/**
	 * @param motivosMap the motivosMap to set
	 */
	public void setEventosMap(String llave, Object obj) {
		this.eventosMap.put(llave, obj);
	}

	/**
	 * @return the eventosAdversosDao
	 */
	public static EventosAdversosDao getEventosAdversosDao() {
		return eventosAdversosDao;
	}

	/**
	 * @param eventosAdversosDao the eventosAdversosDao to set
	 */
	public static void setEventosAdversosDao(EventosAdversosDao eventosAdversosDao) {
		EventosAdversos.eventosAdversosDao = eventosAdversosDao;
	}
	
}	