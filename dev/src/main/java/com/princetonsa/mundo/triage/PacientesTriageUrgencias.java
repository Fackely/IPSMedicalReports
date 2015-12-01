/*
 * @(#)PacientesTriageUrgencias.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.triage;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PacientesTriageUrgenciasDao;

public class PacientesTriageUrgencias 
{
	 /**
	 * DAO utilizado por el objeto para acceder a la fuente de datos
	 */
	private static PacientesTriageUrgenciasDao ptuDao = null;

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
			ptuDao = myFactory.getPacientesTriageUrgenciasDao();
			wasInited = (ptuDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public PacientesTriageUrgencias()
	{
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * listado de pacientes triage urgencias
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con, 
	        						String codigoCentroAtencion,
	        						int codigoInstitucion
								 )
	{
		return ptuDao.listado(con, codigoCentroAtencion, codigoInstitucion);
	}
	
	/**
	 * listado de pacientes triage urgencias
	 * para un rango determinado de fechas
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listadoBusquedaAvanzada(	Connection con, 
			        						String codigoCentroAtencion,
			        						int codigoInstitucion,
			        						String fechaInicial,
			        						String fechaFinal
										 )
	{
		return ptuDao.listadoBusquedaAvanzada(con, codigoCentroAtencion, codigoInstitucion, fechaInicial, fechaFinal);
	}	
	
	
}