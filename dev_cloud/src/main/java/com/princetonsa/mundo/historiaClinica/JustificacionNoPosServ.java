package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.JustificacionNoPosServDao;
import com.princetonsa.dao.inventarios.SeccionesDao;

/**
 * mundo de secciones x almacen
 * @author axioma
 *
 */
public class JustificacionNoPosServ 
{
	//--- Atributos
	private static Logger logger = Logger.getLogger(JustificacionNoPosServ.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static JustificacionNoPosServDao justificacionNoPosServ;
	
	/**
	 * Codigo Centro de ateción
	 */
	private int centroAtencion;
	
	/**
	 * Codigo Institución
	 */
	private int institucion;
	
	/**
	 * 
	 *
	 */
	public JustificacionNoPosServ()
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
		centroAtencion=ConstantesBD.codigoNuncaValido;
		institucion=ConstantesBD.codigoNuncaValido;
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
			justificacionNoPosServ = myFactory.getJustificacionNoPosServDao();
			wasInited = (justificacionNoPosServ != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap consultarSolicitudesJustificaciones(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getJustificacionNoPosServDao().consultarSolicitudesJustificaciones(con, cuenta);
	}
	
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap consultarSolicitudesJustificacionesDiligenciadas(Connection con, int cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getJustificacionNoPosServDao().consultarSolicitudesJustificacionesDiligenciadas(con, cuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static HashMap cargarInfoIngresoRango (Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getJustificacionNoPosServDao().cargarInfoIngresoRango(con, filtros, codigosServicios, fechaInicial, fechaFinal);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRango (Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getJustificacionNoPosServDao().cargarInfoIngresoConsultarModificarRango(con, filtros, codigosServicios, fechaInicial, fechaFinal);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRangoCon (Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getJustificacionNoPosServDao().cargarInfoIngresoConsultarModificarRangoCon(con, filtros, codigosServicios, fechaInicial, fechaFinal);
	}
	
}