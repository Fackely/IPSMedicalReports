/*
 * @(#)GeneracionCargosPendientes.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.GeneracionCargosPendientesDao;

/**
 * Clase para el manejo de la generación de los cargos pendientes
 * @version 1.0
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class GeneracionCargosPendientes 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static GeneracionCargosPendientesDao cargosDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(GeneracionCargosPendientes.class);
	
	/**
	 * mapa con la informacion de la valoracion pendiente
	 */
	private HashMap valoracionPendienteMap;
	
	/**
	 * mapa con la informacion de una solicitud de 
	 * interconsulta - procedimientos -evolucion - cargos directos
	 * que quedo pendiente
	 */
	private HashMap solicitudServicioPendienteMap;
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */		
	public GeneracionCargosPendientes()
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
			cargosDao = myFactory.getGeneracionCargosPendientesDao();
			wasInited = (cargosDao != null);
		}
		return wasInited;
	}

	/**
	 * resetea los datos pertinentes al registro de empresa
	 */
	public void reset()
	{
		this.valoracionPendienteMap= new HashMap();
		this.valoracionPendienteMap.put("numRegistros", "0");
		
		this.solicitudServicioPendienteMap= new HashMap();
		this.solicitudServicioPendienteMap.put("numRegistros", "0");
		
	}

	/**
	 * Carga el listado de solicitudes pendientes x cuenta y responsable
	 * @param con
	 * @param codigosCuenta
	 * @param subCuentas
	 * @return
	 */
	public static HashMap obtenerSolicitudesCargoPendiente(Connection con, Vector codigosCuenta, Vector subCuentas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionCargosPendientesDao().obtenerSolicitudesCargoPendiente(con, codigosCuenta, subCuentas);
	}
	
	/**
	 * Carga los datos del detalle de una solicitud de valoración inicial de urgencias o valoración inicial de hospitalización
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean cargarValoracionPendiente(Connection con, int numeroSolicitud)
	{
		this.valoracionPendienteMap =cargosDao.cargarValoracionPendiente(con,numeroSolicitud);
		if (Integer.parseInt(this.valoracionPendienteMap.get("numRegistros").toString())>0)
			return true;
		return false;
	}

	/**
	 * 
	 * Carga los datos del detalle de una solicitud de procedimientos - interconsulta - evolucion cobrable - cargos directos, 
	 *  
	 * @param con
	 * @param numeroSolicitud
	 * @param tipoSolicitud, (interconsulta - procedimiento)
	 * @return
	 */
	public boolean cargarSolicitudesInterProcEvolCargosDirectos(	Connection con, 
																	int numeroSolicitud, 
																	int tipoSolicitud,
																	String esPortatil)
	{
		this.solicitudServicioPendienteMap= cargosDao.cargarSolicitudesInterProcEvolCargosDirectos(con, numeroSolicitud, tipoSolicitud, esPortatil);
		if (Integer.parseInt(this.solicitudServicioPendienteMap.get("numRegistros").toString())>0)
			return true;
		return false;
	}
	
	/**
	 * Modifica el tipo de recargo de una valoración inicial de hospitalización o urgencias
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numero de solicitud
	 * @param tipo de recargo
	 * @return 
	 */
	public static boolean modificarTipoRecargo(	Connection con, 
												int numeroSolicitud,
												int tipoRecargo	)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionCargosPendientesDao().modificarTipoRecargo(con, numeroSolicitud, tipoRecargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @return
	 */
	public static HashMap obtenerSolicitudMedicamentosPendientesXResponsable( Connection con, int numeroSolicitud, double subCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionCargosPendientesDao().obtenerSolicitudMedicamentosPendientesXResponsable(con, numeroSolicitud, subCuenta);
	}
	
	/**
	 * @return the valoracionPendienteMap
	 */
	public HashMap getValoracionPendienteMap() {
		return valoracionPendienteMap;
	}

	/**
	 * @param valoracionPendienteMap the valoracionPendienteMap to set
	 */
	public void setValoracionPendienteMap(HashMap valoracionPendienteMap) {
		this.valoracionPendienteMap = valoracionPendienteMap;
	}
	
	/**
	 * @return the valoracionPendienteMap
	 */
	public Object getValoracionPendienteMap(Object key) {
		return valoracionPendienteMap.get(key);
	}

	/**
	 * @param valoracionPendienteMap the valoracionPendienteMap to set
	 */
	public void setValoracionPendienteMap(Object key, Object value) {
		this.valoracionPendienteMap.put(key, value);
	}

	/**
	 * @return the solicitudServicioPendienteMap
	 */
	public HashMap getSolicitudServicioPendienteMap() {
		return solicitudServicioPendienteMap;
	}

	/**
	 * @param solicitudServicioPendienteMap the solicitudServicioPendienteMap to set
	 */
	public void setSolicitudServicioPendienteMap(
			HashMap solicitudServicioPendienteMap) {
		this.solicitudServicioPendienteMap = solicitudServicioPendienteMap;
	}
	
	
}
