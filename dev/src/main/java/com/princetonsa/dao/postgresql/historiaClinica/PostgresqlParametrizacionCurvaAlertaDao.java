/*
 * @(#)PostgresqlParametrizacionCurvaAlertaDao
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ParametrizacionCurvaAlertaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseParametrizacionCurvaAlertaDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PostgresqlParametrizacionCurvaAlertaDao implements ParametrizacionCurvaAlertaDao
{
	/**
	 * INSERTAR
	 */
	private static String insertarStr= "INSERT INTO param_curva_alert_partograma (codigo,posicion,paridad,membrana,rango_inicial,rango_final,valor,activo,institucion) VALUES (nextval('seq_param_alert_partograma'), ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con, 
	       					int codigoInstitucion)
	{
		return SqlBaseParametrizacionCurvaAlertaDao.listado(con, codigoInstitucion);
	}
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int eliminar (Connection con, String codigoPK)
	{
		return SqlBaseParametrizacionCurvaAlertaDao.eliminar(con, codigoPK);
	}
	
	/**
	 * modifica
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int modificar (	Connection con, 
							String codigoPosicion,
							String codigoParidad,
							String codigoMembrana,
							String rangoInicial,
							String rangoFinal,
							String valor,
							String activo,
							int codigoInstitucion,
							String codigoPK)
	{
		return SqlBaseParametrizacionCurvaAlertaDao.modificar(con, codigoPosicion, codigoParidad, codigoMembrana, rangoInicial, rangoFinal, valor, activo, codigoInstitucion, codigoPK);
	}
						 
	/**
	 * 
	 * @param con
	 * @param codigoPK
	 * @param codigoPosicion
	 * @param codigoParidad
	 * @param codigoMembrana
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param valor
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertar (	Connection con,
								String codigoPosicion,
								String codigoParidad,
								String codigoMembrana,
								String rangoInicial,
								String rangoFinal,
								String valor,
								String activo,
								int codigoInstitucion
							)
	{
		return SqlBaseParametrizacionCurvaAlertaDao.insertar(con, codigoPosicion, codigoParidad, codigoMembrana, rangoInicial, rangoFinal, valor, activo, codigoInstitucion, insertarStr );
	}
		
}