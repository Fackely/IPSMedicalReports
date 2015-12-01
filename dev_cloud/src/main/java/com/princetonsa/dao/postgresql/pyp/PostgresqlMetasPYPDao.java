/*
 *Ago 09, 2006 
 * 
 */
package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.MetasPYPDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseMetasPYPDao;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Metas PYP
 */
public class PostgresqlMetasPYPDao implements MetasPYPDao 
{
	/**
	 * Método implementado para consultar las actividades del programa, convenio, y año específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarActividades(Connection con,HashMap campos)
	{
		return SqlBaseMetasPYPDao.consultarActividades(con,campos);
	}
	
	/**
	 * Método implementado para consultar los centros de atencion de una actividad, programa, convenio y año específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCentrosAtencion(Connection con,HashMap campos)
	{
		return SqlBaseMetasPYPDao.consultarCentrosAtencion(con,campos);
	}
	
	/**
	 * Método que consulta las ocupaciones del centro atencion, actividad, programa, convenio y año específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOcupaciones(Connection con,HashMap campos)
	{
		return SqlBaseMetasPYPDao.consultarOcupaciones(con,campos);
	}
	
	/**
	 * Método implementado para insertar una actividad de un programa, convenio y año específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividades(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_metas_pyp')");
		return SqlBaseMetasPYPDao.insertarActividades(con,campos);
	}
	
	/**
	 * Método que insertar un centro de atencion para una actividad, programa, convenio y anio específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarCentrosAtencion(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_metas_ca_pyp')");
		return SqlBaseMetasPYPDao.insertarCentrosAtencion(con,campos);
	}
	
	/**
	 * Método implementado para insertar un ocupacion de una actividad, programa, convenio, año
	 * específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarOcupaciones(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_metas_ocupacion_pyp')");
		return SqlBaseMetasPYPDao.insertarOcupaciones(con,campos);
	}
	
	/**
	 * Método implementado para modificar una actividad, centro atencion o ocupacion médica
	 * pertenecientes a un programa, convenio y año específicos.
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valor pueden ser : actividad, centroAtencion y ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos)
	{
		return SqlBaseMetasPYPDao.modificar(con,campos);
	}
	
	/**
	 * Método que elimina un centro de atencion o una ocupacion pertenecientes a
	 * una actividad, programa , convenio y año específicos
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valores pueden ser : centroAtencion o ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos)
	{
		return SqlBaseMetasPYPDao.eliminar(con,campos);
	}
	
	/**
	 * Método que consulta las actividades de un programa por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarActividadesProgramaConvenio(Connection con,HashMap campos)
	{
		return SqlBaseMetasPYPDao.cargarActividadesProgramaConvenio(con,campos);
	}
}
