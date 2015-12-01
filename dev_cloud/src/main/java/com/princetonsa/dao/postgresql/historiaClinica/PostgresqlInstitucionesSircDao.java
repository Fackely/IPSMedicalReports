/*
 * Nov 09, 2006
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.InstitucionesSircDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseInstitucionesSircDao;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Instituciones SIRC
 */
public class PostgresqlInstitucionesSircDao implements InstitucionesSircDao 
{
	/**
	 * Método que carga las instituciones SIRC por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInstituciones(Connection con,int institucion)
	{
		return SqlBaseInstitucionesSircDao.cargarInstituciones(con,institucion);
	}
	
	/**
	 * Método que carga los datos de una institucion SIRC
	 * @param con
	 * @param campos (codigo e institucion)
	 * @return
	 */
	public HashMap cargarInstitucion(Connection con,HashMap campos)
	{
		return SqlBaseInstitucionesSircDao.cargarInstitucion(con,campos);
	}
	
	/**
	 * Método que inserta una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos)
	{		
		return SqlBaseInstitucionesSircDao.insertar(con,campos);
	}
	
	/**
	 * Método que modifica una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos)
	{
		return SqlBaseInstitucionesSircDao.modificar(con,campos);
	}
	
	/**
	 * Método que elimina una institucion SIRC
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo, int institucion)
	{
		return SqlBaseInstitucionesSircDao.eliminar(con,codigo,institucion);
	}	

	
	/**
	 * Método que carga los niveles de servicio por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarNivelesServicio(Connection con,int institucion)
	{
		return SqlBaseInstitucionesSircDao.cargarNivelesServicio(con,institucion);
	}
}
