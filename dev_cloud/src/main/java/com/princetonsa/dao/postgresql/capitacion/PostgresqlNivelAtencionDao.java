/*
 * Creado el Jun 13, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.postgresql.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.NivelAtencionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseNivelAtencionDao;

public class PostgresqlNivelAtencionDao implements NivelAtencionDao {

	/**
	 * Metodo para cargar los niveles de servcicio
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap cargarInformacion(Connection con, int codigoInstitucion)
	{
		return SqlBaseNivelAtencionDao.cargarInformacion(con, codigoInstitucion);
	}

	/**
	 * Metodo para insertar Niveles de Servicios.
	 * @param con
	 * @param tipoOperacion
	 * @param codigo
	 * @param descripcion
	 * @return
	 */
	public int insertar(Connection con, int tipoOperacion, int codigo, String descripcion,  boolean Activo, int  institucion)
	{
		return SqlBaseNivelAtencionDao.insertar(con, tipoOperacion, codigo, descripcion, Activo, institucion);
	}

	/**
	 * Metodo para eliminar un nivel de servicio. 
	 * @param con
	 * @param nroReg
	 * @return
	 */
	public int eliminar(Connection con, int nroReg)
	{
		return SqlBaseNivelAtencionDao.eliminar(con, nroReg);
	}

}
