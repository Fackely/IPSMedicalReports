package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaCondicionTmExamenGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaCondicionesTmExamenGenericaDao;

/** 
 * Implementación postgres de las funciones de acceso a la fuente de datos
 * para busqueda de Condiciones de Toma de Examen
 * @author Jose Eduardo Arias Doncel
 */
public class PostgresqlBusquedaCondicionesTmExamenGenericaDao implements BusquedaCondicionTmExamenGenericaDao
{
	/**
	 * Consulta Generica de Condiciones de Toma de Examen
	 * @param Connection con
	 * @param Int codigoExamenCt
	 * @param Int institucion
	 * @param String descripcionExamenCt
	 * @param String activo	 
	 * */
	public HashMap consultarCondicionesTmExamenBasica(Connection con, String codigoExamenCt, int institucion, String descripcionExamenCt, String activo, String codigosExamenesInsertados)
	{
		return SqlBaseBusquedaCondicionesTmExamenGenericaDao.consultarCondicionesTmExamenBasica(con, codigoExamenCt, institucion, descripcionExamenCt, activo, codigosExamenesInsertados);	
	}
}

