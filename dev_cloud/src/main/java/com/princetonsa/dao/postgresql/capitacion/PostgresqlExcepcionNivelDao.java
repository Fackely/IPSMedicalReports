/*
 * Creado el Jun 15, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.postgresql.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.ExcepcionNivelDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseExcepcionNivelDao;

public class PostgresqlExcepcionNivelDao implements ExcepcionNivelDao {

	/**
	 * Metodo para cargar Los convenios
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInformacion(Connection con, int institucion)
	{
		return SqlBaseExcepcionNivelDao.cargarInformacion(con, institucion);
	}

	/**
	 * Metodo para traer los servicios de un convenio especifico
	 * @param con
	 * @return
	 */
	public HashMap cargarServiciosConvenio(Connection con,int tipoInformacion, int codigoConvenio, int institucion, int nroContrato)
	{
		return SqlBaseExcepcionNivelDao.cargarServiciosConvenio(con, tipoInformacion, codigoConvenio, institucion, nroContrato);
	}
	
	/**
	 * Metodo para insertar 
	 * @param con
	 * @param servicio
	 * @param codigoConvenio
	 * @return
	 */
	public int insertarServiciosConvenio(Connection con, int servicio, int contrato, int institucion)
	{
		return SqlBaseExcepcionNivelDao.insertarServiciosConvenio(con, servicio, contrato, institucion);
	}
	

	/**
	 * Metodo para eliminar el contrato.
	 * @param con
	 * @param nroServicio
	 * @param nroContrato
	 * @return
	 */
	public int eliminarServicioContrato(Connection con, int nroServicio, int nroContrato)
	{
		return SqlBaseExcepcionNivelDao.eliminarServicioContrato(con, nroServicio, nroContrato);
	}

}
