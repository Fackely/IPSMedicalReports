/*
 * @(#)PacientesUrgenciasPorValorar.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.triage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PacientesUrgenciasPorValorarDao;

public class PacientesUrgenciasPorValorar 
{
	 /**
	 * DAO utilizado por el objeto para acceder a la fuente de datos
	 */
	private static PacientesUrgenciasPorValorarDao pacientesUrgenciasPorValorarDao = null;

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
			pacientesUrgenciasPorValorarDao = myFactory.getPacientesUrgenciasPorValorarDao();
			wasInited = (pacientesUrgenciasPorValorarDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public PacientesUrgenciasPorValorar()
	{
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Método para consultar los todos los datos de los pcientes de triage de via de ingreso de
	 * urgencias pendientes de valoracion inicial de urgencias
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarPacientesUrgPorValorar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException
	{
		return pacientesUrgenciasPorValorarDao.consultarPacientesUrgPorValorar(con, codigoCentroAtencion, institucion);
	}
	
}