package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.CalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;

/**
 * mundo de Estadisticas SErvicios
 * @author axioma
 *
 */
public class EstadisticasServicios
{
	private static Logger logger = Logger.getLogger(EstadisticasServicios.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EstadisticasServiciosDao estadisticasServiciosDao;

	/**
	 * Login Usuario
	 */
	private String usuario;
	
	/**
	 * 
	 */
	private HashMap filtrosMap;
	
	/**
	 * 
	 */
	private String viasDeIngreso;
	
	/**
	 * 
	 */
	private String tiposPaciente;
	
	/**
	 *
	 */
	public EstadisticasServicios()
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
			estadisticasServiciosDao = myFactory.getEstadisticasServiciosDao();
			wasInited = (estadisticasServiciosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereServiciosRealizados(Connection con, EstadisticasServicios mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasServiciosDao().crearWhereServiciosRealizados(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereServiciosRealizadosXConvenio(Connection con, EstadisticasServicios mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasServiciosDao().crearWhereServiciosRealizadosXConvenio(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereServiciosRealizadosXEspecialidad(Connection con, EstadisticasServicios mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasServiciosDao().crearWhereServiciosRealizadosXEspecialidad(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearCampoEgresosPorMes(Connection con, EstadisticasServicios mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasServiciosDao().crearCampoEgresosPorMes(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean registrarGeneracionReporte(Connection con, EstadisticasServicios mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasServiciosDao().registrarGeneracionReporte(con, mundo);
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