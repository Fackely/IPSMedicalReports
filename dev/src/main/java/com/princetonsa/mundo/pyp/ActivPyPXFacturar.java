/*
 * @(#)ActivPyPXFacturar
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ActivPyPXFacturarDao;

/**
 * 
 * @author wrios
 *
 */
public class ActivPyPXFacturar
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static ActivPyPXFacturarDao dao = null;
	
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
			dao = myFactory.getActivPyPXFacturarDao();
			wasInited = (dao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public ActivPyPXFacturar()
	{
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con,
									int codigoConvenio,
									int codigoCentroAtencion,
	        						int codigoInstitucion
								 )
	{
		return dao.listado(con, codigoConvenio, codigoCentroAtencion, codigoInstitucion);
	}
}