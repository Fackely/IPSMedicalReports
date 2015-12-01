package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;

/**
 * mundo de Motivos Satisfaccion Insatisfaccion
 * @author axioma
 *
 */
public class MotivosSatisfaccionInsatisfaccion 
{
	private static Logger logger = Logger.getLogger(MotivosSatisfaccionInsatisfaccion.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static MotivosSatisfaccionInsatisfaccionDao motivosSatisfaccionInsatisfaccionDao;
	
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
	private HashMap motivosMap;
	
	/**
	 *
	 */
	public MotivosSatisfaccionInsatisfaccion()
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
		this.motivosMap=new HashMap();
		this.motivosMap.put("numRegistros", "0");
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
			motivosSatisfaccionInsatisfaccionDao = myFactory.getMotivosSatisfaccionInsatisfaccionDao();
			wasInited = (motivosSatisfaccionInsatisfaccionDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultar(Connection con, MotivosSatisfaccionInsatisfaccion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosSatisfaccionInsatisfaccionDao().consultar(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean ingresar(Connection con, MotivosSatisfaccionInsatisfaccion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosSatisfaccionInsatisfaccionDao().ingresar(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean modificar(Connection con, MotivosSatisfaccionInsatisfaccion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosSatisfaccionInsatisfaccionDao().modificar(con, mundo);
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean eliminar(Connection con, MotivosSatisfaccionInsatisfaccion mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosSatisfaccionInsatisfaccionDao().eliminar(con, mundo);
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
		MotivosSatisfaccionInsatisfaccion.logger = logger;
	}

	/**
	 * @return the motivosSatisfaccionInsatisfaccionDao
	 */
	public static MotivosSatisfaccionInsatisfaccionDao getMotivosSatisfaccionInsatisfaccionDao() {
		return motivosSatisfaccionInsatisfaccionDao;
	}

	/**
	 * @param motivosSatisfaccionInsatisfaccionDao the motivosSatisfaccionInsatisfaccionDao to set
	 */
	public static void setMotivosSatisfaccionInsatisfaccionDao(
			MotivosSatisfaccionInsatisfaccionDao motivosSatisfaccionInsatisfaccionDao) {
		MotivosSatisfaccionInsatisfaccion.motivosSatisfaccionInsatisfaccionDao = motivosSatisfaccionInsatisfaccionDao;
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
	public HashMap getMotivosMap() {
		return motivosMap;
	}

	/**
	 * @param motivosMap the motivosMap to set
	 */
	public void setMotivosMap(HashMap motivosMap) {
		this.motivosMap = motivosMap;
	}
	
	/**
	 * @return the motivosMap
	 */
	public Object getMotivosMap(String llave) {
		return motivosMap.get(llave);
	}

	/**
	 * @param motivosMap the motivosMap to set
	 */
	public void setMotivosMap(String llave, Object obj) {
		this.motivosMap.put(llave, obj);
	}
}	