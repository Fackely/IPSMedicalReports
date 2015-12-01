/*
 * Mayo 03, 2006
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.UnidadFuncionalDao;
import com.princetonsa.dao.sqlbase.SqlBaseUnidadFuncionalDao;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrizaci�n de Unidades Funcionales
 */

public class OracleUnidadFuncionalDao implements UnidadFuncionalDao 
{
	/**
	 * M�todo implementado para consultar todas la unidades funcionales 
	 * de la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultar(Connection con,int institucion)
	{
		return SqlBaseUnidadFuncionalDao.consultar( con,institucion);
	}
	
	/**
	 * M�todo que consulta los dats de una unidad funcional
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public HashMap consultar(Connection con,String codigo,int institucion)
	{
		return SqlBaseUnidadFuncionalDao.consultar(con,codigo,institucion); 
	}
	
	/**
	 * M�todo implementado para insertar una nueva unidad funcional
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int insertar(Connection con,String codigo,String descripcion,boolean activo,int institucion)
	{
		return SqlBaseUnidadFuncionalDao.insertar(con,codigo,descripcion,activo,institucion);
	}
	
	/**
	 * M�todo implementado para modificar una unidad funcional
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int modificar(Connection con,String codigo,String descripcion,boolean activo,int institucion)
	{
		return SqlBaseUnidadFuncionalDao.modificar(con,codigo,descripcion,activo,institucion);
	}
	
	/**
	 * M�todo implementado para eliminar una unidad funcional
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo,int institucion)
	{
		return SqlBaseUnidadFuncionalDao.eliminar(con,codigo,institucion);
	}

}
