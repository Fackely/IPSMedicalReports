/*
 * @(#)ParametrizacionCurvaAlerta
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ParametrizacionCurvaAlertaDao;

public class ParametrizacionCurvaAlerta 
{
	 /**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static ParametrizacionCurvaAlertaDao pcupDao = null;
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
		if (myFactory != null)
		{
			pcupDao = myFactory.getParametrizacionCurvaAlertaDao();
			wasInited = (pcupDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public ParametrizacionCurvaAlerta()
	{
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con, 
	       					int codigoInstitucion)
	{
		return pcupDao.listado(con, codigoInstitucion);
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
		return pcupDao.eliminar(con, codigoPK);
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
		return pcupDao.modificar(con, codigoPosicion, codigoParidad, codigoMembrana, rangoInicial, rangoFinal, valor, activo, codigoInstitucion, codigoPK);
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
		return pcupDao.insertar(con, codigoPosicion, codigoParidad, codigoMembrana, rangoInicial, rangoFinal, valor, activo, codigoInstitucion);
	}
	
}
