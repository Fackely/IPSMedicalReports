package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.CentrosCostoXUnidadConsultaDao;
import com.princetonsa.dao.sqlbase.SqlBaseCentrosCostoXUnidadConsultaDao;

public class PostgresqlCentrosCostoXUnidadConsultaDao implements CentrosCostoXUnidadConsultaDao
{
	/**
	 * Método para consultar las
	 * opciones desplegadas en los select
	 * @param con
	 * @param tipoConsulta
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public Collection consultarListados(Connection con, int tipoConsulta, int institucion, int centroAtencion)
	{
		return SqlBaseCentrosCostoXUnidadConsultaDao.consultarListados(con, tipoConsulta, institucion, centroAtencion);
	}

	/**
	 * Método que consulta los centros de costo por unidad de consulta
	 * de acuerdo con el centro de atencion entregado
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return Mapa con los registros
	 */
	public HashMap consultarRegistros(Connection con, int institucion, int centroAtencion)
	{
		return SqlBaseCentrosCostoXUnidadConsultaDao.consultarRegistros(con, institucion, centroAtencion);
	}
	
	/**
	 * Método para guardar los datos en la BD
	 * @param con
	 * @param elementos
	 * @return true si se insertó bien
	 */
	public boolean guardar(Connection con, Vector elementos, int centroAtencion)
	{
		return SqlBaseCentrosCostoXUnidadConsultaDao.guardar(con, elementos, centroAtencion);
	}

}
