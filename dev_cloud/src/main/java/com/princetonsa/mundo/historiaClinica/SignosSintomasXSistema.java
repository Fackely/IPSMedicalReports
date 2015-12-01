/*
 * @(#)SignosSintomasXSistema.java
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
import com.princetonsa.dao.historiaClinica.SignosSintomasXSistemaDao;

public class SignosSintomasXSistema 
{
	 /**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static SignosSintomasXSistemaDao sssDao = null;
	
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
			sssDao = myFactory.getSignosSintomasXSistemaDao();
			wasInited = (sssDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public SignosSintomasXSistema()
	{
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Consulta la info de los asocios x uvr 
	 * @param con
	 * @param codigoMotivoConsulta
	 * @param codigoInstitucion
	 * @param
	 * @return
	 */
	public HashMap listado(	Connection con, 
	       					String codigoMotivoConsulta,
	       					int codigoInstitucion
						 )
	{
		return sssDao.listado(con, codigoMotivoConsulta, codigoInstitucion);
	}
	
	/**
	 * Elimina 
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int eliminar(Connection con,  String codigoPK) throws SQLException
	{
	    return  sssDao.eliminar(con, codigoPK);
	}
	
	/**
	 * evalua si existe un registro unique o no
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param codigoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @param codigoPK
	 * @return
	 */
	public boolean existeRegistro(Connection con,
										 String codigoConsultaMotivo,
										 String codigoSignoSintoma,
										 String consecutivoCalificacionTriage,
										 int codigoInstitucion,
										 String codigoPK)
	{
		return sssDao.existeRegistro(con, codigoConsultaMotivo, codigoSignoSintoma, consecutivoCalificacionTriage, codigoInstitucion, codigoPK);
	}
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int modificar (	Connection con, 
									String codigoConsultaMotivo,
									String codigoSignoSintoma,
									String consecutivoCalificacionTriage,
									int codigoInstitucion,
									String codigoPK)
	{
		return sssDao.modificar(con, codigoConsultaMotivo, codigoSignoSintoma, consecutivoCalificacionTriage, codigoInstitucion, codigoPK);
	}
	
	/**
	 * inserta
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param codigoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertar (	Connection con, 
								String codigoConsultaMotivo,
								String codigoSignoSintoma,
								String consecutivoCalificacionTriage,
								int codigoInstitucion
								)
	{
		return sssDao.insertar(con, codigoConsultaMotivo, codigoSignoSintoma, consecutivoCalificacionTriage, codigoInstitucion);
	}
}