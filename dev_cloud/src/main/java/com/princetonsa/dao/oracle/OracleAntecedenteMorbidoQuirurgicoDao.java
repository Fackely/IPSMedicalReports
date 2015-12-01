/*
 * @(#)OracleAntecedenteMorbidoQuirurgicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteMorbidoQuirurgicoDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedenteMorbidoQuirurgicoDao;

/**
 * Implementación de los métodos para acceder a la fuente de datos, la parte de
 * Antecedentes Mórbidos Quirúrgicos para Postgres.
 *
 * @version 1.0, Agosto 12, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleAntecedenteMorbidoQuirurgicoDao implements AntecedenteMorbidoQuirurgicoDao
{
	

	/**
	 * @see com.princetonsa.dao.AntecedenteMorbidoQuirurgicoDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																		int codigoPaciente,
																		int codigo,
																		String nombre,
																		String fecha,
																		String causa,
																		String complicaciones,
																		String recomendaciones,
																		String observaciones,
																		String estado) throws SQLException
	{
	    return SqlBaseAntecedenteMorbidoQuirurgicoDao.insertarTransaccional(	con,
				codigoPaciente,
				codigo,
				nombre,
				fecha,
				causa,
				complicaciones,
				recomendaciones,
				observaciones,
				estado);
	}

	

	/**
	 * @see com.princetonsa.dao.AntecedenteMorbidoQuirurgicoDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con,
																			int codigoPaciente,
																			int codigo,
																			String fecha,
																			String causa,
																			String complicaciones,
																			String recomendaciones,
																			String observaciones,
																			String estado) throws SQLException
	{
	    return SqlBaseAntecedenteMorbidoQuirurgicoDao.modificarTransaccional(	con,
				codigoPaciente,
				codigo,
				fecha,
				causa,
				complicaciones,
				recomendaciones,
				observaciones,
				estado) ;
	}

	public ResultadoBoolean existeAntecedente(	Connection con, 
																	int codigoPaciente, 
																	int codigo)
	{
			return SqlBaseAntecedenteMorbidoQuirurgicoDao.existeAntecedente(con,codigoPaciente,codigo);
	}

}
