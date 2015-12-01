/*
 * Mayo 31, 2006
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.DestinoTriageDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseDestinoTriageDao;
/**
 * @author Sebasti�n G�mez 
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Destinos Triage
 */
public class PostgresqlDestinoTriageDao implements DestinoTriageDao 
{
	/**
	 * M�todo implementado para cargar los destinos del paciente
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	public HashMap cargar(Connection con,String institucion,String codigo)
	{
		return SqlBaseDestinoTriageDao.cargar(con,institucion,codigo);
	}
	/**
	 * M�todo implementado para insertar un destino triage
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param indicadorAdmiUrg
	 * @param institucion
	 * @return
	 */
	public int insertar(Connection con,String codigo,String nombre,boolean indicadorAdmiUrg,String institucion)
	{
		return SqlBaseDestinoTriageDao.insertar(con,codigo,nombre,indicadorAdmiUrg,institucion,"nextval('seq_destino_paciente')");
	}
	
	/**
	 * M�todo implementado para modificar un destino Triage
	 * @param con
	 * @param nombre
	 * @param indicadorAdminUrg
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int modificar(Connection con,String nombre,boolean indicadorAdminUrg, String codigo,String institucion)
	{
		return SqlBaseDestinoTriageDao.modificar(con,nombre,indicadorAdminUrg,codigo,institucion);
	}
	
	/**
	 * M�todo implementado para eliminar un registro destino triage
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo,String institucion)
	{
		return SqlBaseDestinoTriageDao.eliminar(con,codigo,institucion);
	}
}
