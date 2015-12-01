/*
 * @(#)ParametrizacionCurvaAlertaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos 
 *
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface ParametrizacionCurvaAlertaDao 
{
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con, 
	       					int codigoInstitucion);
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int eliminar (Connection con, String codigoPK);
	
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
							String codigoPK);
						 
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
							);
}
