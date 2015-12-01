/*
 * Ene 18, 2006
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.GruposServiciosDao;
import com.princetonsa.dao.sqlbase.SqlBaseGruposServiciosDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrización de Grupos de Servicios
 */
public class OracleGruposServiciosDao implements GruposServiciosDao 
{
	/**
	 * Método implementado para consultar los grupos de servicios
	 * de una institucion
	 * @param con
	 * @return
	 */
	public HashMap consultarGrupos(Connection con, int codInstitucion)
	{
		return SqlBaseGruposServiciosDao.consultarGrupos(con, codInstitucion);
	}
	
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.GruposServiciosDao#busquedaGrupos(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, java.lang.String, int)
	 */
	@SuppressWarnings("rawtypes")
	public HashMap busquedaGrupos(Connection con,int codigo,String acronimo,String descripcion,String activo, String multiple, String tipo, String tipoSalaStandar,
						String numDiasUrgente, String acroDiasUrgente, String numDiasNormal, String acroDiasNormal, int tipoMonto)
	{
		return SqlBaseGruposServiciosDao.busquedaGrupos(con,codigo,acronimo,descripcion,activo, multiple, tipo, tipoSalaStandar,
						numDiasUrgente, acroDiasUrgente, numDiasNormal, acroDiasNormal, tipoMonto);
	}
	

	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.GruposServiciosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, boolean, boolean, java.lang.String, int, java.lang.String, int, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public int insertar(Connection con,String acronimo,String descripcion,boolean activo,boolean multiple, String tipo, int codInstitucion, String tipoSalaStandar,
						String numDiasUrgente, String acroDiasUrgente, String numDiasNormal,String acroDiasNormal, int tipoMonto)
	{
		return SqlBaseGruposServiciosDao.insertar(con,acronimo,descripcion,activo, multiple,tipo, codInstitucion, tipoSalaStandar,
						numDiasUrgente, acroDiasUrgente, numDiasNormal, acroDiasNormal, tipoMonto);
		
	}
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.GruposServiciosDao#modificar(java.sql.Connection, java.lang.String, boolean, int, boolean, java.lang.String, java.lang.String, int, java.lang.String, int, java.lang.String, int)
	 */
	public int modificar(Connection con,String descripcion,boolean activo, int codigo, boolean multiple, String tipo, String tipoSalaStandar,
						String numDiasUrgente, String acroDiasUrgente, String numDiasNormal, String acroDiasNormal, int tipoMonto)
	{
		return SqlBaseGruposServiciosDao.modificar(con,descripcion,activo,codigo,multiple, tipo, tipoSalaStandar,
						numDiasUrgente, acroDiasUrgente, numDiasNormal, acroDiasNormal, tipoMonto);
	}
	
	/**
	 * Método implementado para eliminar un registro de grupos de servicios
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminar(Connection con,int codigo)
	{
		return SqlBaseGruposServiciosDao.eliminar(con,codigo);
	}
	
	/**
	 * Método encargado de obtener los tipos de salas con es_quirurgica = true
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los tipos de salas con es_quirurgica = true
	 */
	public HashMap obtenerListaSalas(Connection con, int codInstitucion)
	{
		return SqlBaseGruposServiciosDao.obtenerListaSalas(con, codInstitucion);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.GruposServiciosDao#obtenerListaTiposMontos(java.sql.Connection)
	 */
	@Override
	
	public HashMap obtenerListaTiposMontos(Connection con) {
		return SqlBaseGruposServiciosDao.obtenerListaTiposMontos(con);
	}
}
