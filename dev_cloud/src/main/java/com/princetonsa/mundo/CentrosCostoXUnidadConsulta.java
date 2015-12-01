/**
 * Juan David Ramírez 11/05/2006
 * Princeton S.A.
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.CentrosCostoXUnidadConsultaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author Juan David Ramírez
 *
 */
public class CentrosCostoXUnidadConsulta
{
	/**
	 * Conexión con la BD
	 */
	private CentrosCostoXUnidadConsultaDao centrosCostoXUnidadConsultaDao;
	
	/**
	 * Centro de atención de los registros
	 */
	private int centroAtencion;
	
	/**
	 * Registros a pertenencientes al centro de atención
	 */
	private Vector registros;
	
	/**
	 * Inicializa el DAO
	 */
	private void init()
	{
		centrosCostoXUnidadConsultaDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCentrosCostoXUnidadConsultaDao();
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public CentrosCostoXUnidadConsulta()
	{
		this.init();
	}
	
	/**
	 * Método para consultar las
	 * opciones desplegadas en los select
	 * @param con
	 * @param tipoConsulta
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static Collection consultarListados(Connection con, int tipoConsulta, int institucion, int centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCentrosCostoXUnidadConsultaDao().consultarListados(con, tipoConsulta, institucion, centroAtencion);
	}

	/**
	 * Método que consulta los centros de costo por unidad de consulta
	 * de acuerdo con el centro de atencion entregado
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return Mapa con los registros
	 */
	public static HashMap consultarRegistros(Connection con, int institucion, int centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCentrosCostoXUnidadConsultaDao().consultarRegistros(con, institucion, centroAtencion);
	}

	/**
	 * Método para guardar los datos en la BD
	 * @param con
	 * @param elementos
	 * @return true si se insertó bien
	 */
	public boolean guardar(Connection con)
	{
		return centrosCostoXUnidadConsultaDao.guardar(con, registros, centroAtencion);
	}

	/**
	 * @return Retorna centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion Asigna centroAtencion.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Retorna registros.
	 */
	public Vector getRegistros()
	{
		return registros;
	}

	/**
	 * @param registros Asigna registros.
	 */
	public void setRegistros(Vector registros)
	{
		this.registros = registros;
	}

}
