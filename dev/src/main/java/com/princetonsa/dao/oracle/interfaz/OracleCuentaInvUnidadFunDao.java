/*
 * Abr 16, 2007
 */
package com.princetonsa.dao.oracle.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.CuentaInvUnidadFunDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCuentaInvUnidadFunDao;

/**
 * 
 * @author sgomez
 * Métodos de oracle para el acceso a la fuente de datos para la funcionalidad
 * Cuenta Inventarios X Unidad Funcional
 */
public class OracleCuentaInvUnidadFunDao implements CuentaInvUnidadFunDao 
{

	/**
	 * Método que consulta las clases de inventario
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarClases(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.consultarClases(con, campos);
	}
	
	/**
	 * Método que consulta los grupos de inventario de una clase específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarGrupos(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.consultarGrupos(con, campos);
	}
	
	/**
	 * Método que consulta los subgrupos de un grupo y una clase específicas
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarSubgrupos(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.consultarSubgrupos(con, campos);
	}
	
	/**
	 * Método que inserta una nueva clase x unidad funcional
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarClase(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.insertarClase(con, campos);
	}
	
	/**
	 * Método que inserta un grupo x unidad funcional de una clase específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarGrupo(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.insertarGrupo(con, campos);
	}
	
	/**
	 * Método que inserta un subgrupo x unidad funcional de un grupo específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarSubgrupo(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.insertarSubgrupo(con, campos);
	}
	
	/**
	 * Método que modifica la clase x unidad funcional
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarClase(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.modificarClase(con, campos);
	}
	
	/**
	 * Método que modifica el grupo x unidad funcional de una clase específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarGrupo(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.modificarGrupo(con, campos);
	}
	
	/**
	 * Método que modifica el subgrupo x unidad funcional de un grupo específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarSubgrupo(Connection con,HashMap campos)
	{
		return SqlBaseCuentaInvUnidadFunDao.modificarSubgrupo(con, campos);
	}
}
