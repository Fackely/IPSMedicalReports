package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.RegistroEventosAdversosDao;
import com.princetonsa.dao.inventarios.SeccionesDao;
import com.princetonsa.dao.manejoPaciente.EncuestaCalidadAtencionDao;

/**
 * mundo de secciones x almacen
 * @author axioma
 *
 */
public class RegistroEventosAdversos 
{
	//--- Atributos
	private static Logger logger = Logger.getLogger(RegistroEventosAdversos.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RegistroEventosAdversosDao registroEventosAdversosDao;
	
	private static HashMap ingresosMap; 
	
	private static String paciente;
	
	private static String ingreso;
	
	private static String centroCosto;
	
	private static String codigorea;
	
	/**
	 * 
	 *
	 */
	public RegistroEventosAdversos()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.ingresosMap=new HashMap();
		this.ingresosMap.put("numRegistros", 0);
		this.paciente="";
		this.ingreso="";
		this.centroCosto="";
		this.codigorea="";
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
			registroEventosAdversosDao = myFactory.getRegistroEventosAdversosDao();
			wasInited = (registroEventosAdversosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public static HashMap consultar(Connection con, RegistroEventosAdversos encuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEventosAdversosDao().consultar(con, encuesta);
	}
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public static HashMap consultarDetalleXCuenta(Connection con, RegistroEventosAdversos encuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEventosAdversosDao().consultarDetalleXCuenta(con, encuesta);
	}
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public static HashMap consultarDetalleXCuenta2(Connection con, RegistroEventosAdversos encuesta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEventosAdversosDao().consultarDetalleXCuenta2(con, encuesta);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtro
	 * @return
	 */
	public static int guardarEvento(Connection con, HashMap filtro)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEventosAdversosDao().guardarEvento(con, filtro);
	}

	/**
	 * 
	 * @param con
	 * @param filtro
	 * @return
	 */
	public static int modificar(Connection con, HashMap filtro)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEventosAdversosDao().modificar(con, filtro);
	}	
	
	
	/**
	 * @return the paciente
	 */
	public static String getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public static void setPaciente(String paciente) {
		RegistroEventosAdversos.paciente = paciente;
	}

	/**
	 * @return the ingreso
	 */
	public static String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public static void setIngreso(String ingreso) {
		RegistroEventosAdversos.ingreso = ingreso;
	}

	/**
	 * @return the centroCosto
	 */
	public static String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public static void setCentroCosto(String centroCosto) {
		RegistroEventosAdversos.centroCosto = centroCosto;
	}

	/**
	 * @return the codigorea
	 */
	public static String getCodigorea() {
		return codigorea;
	}

	/**
	 * @param codigorea the codigorea to set
	 */
	public static void setCodigorea(String codigorea) {
		RegistroEventosAdversos.codigorea = codigorea;
	}

		
}



