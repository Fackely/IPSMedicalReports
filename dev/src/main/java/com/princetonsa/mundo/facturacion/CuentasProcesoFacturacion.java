/*
 * @(#)CuentasProcesoFacturacion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.CuentasProcesoFacturacionDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de cuentas en proceso facturacion
 * @version 1.0, julio 6, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class CuentasProcesoFacturacion 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static CuentasProcesoFacturacionDao cuentasProcFactDao;
	
	/**
	 * resetea los datos pertinentes al registro de empresa
	 */
	public void reset()
	{
		//@todo resetear los atributos del objetp
	}
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */		
	public CuentasProcesoFacturacion()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
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
			cuentasProcFactDao = myFactory.getCuentasProcesoFacturacionDao();
			wasInited = (cuentasProcFactDao != null);
		}
		return wasInited;
	}
	
	/**
	 * listado X Paciente
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @param codigoPersona
	 * @return
	 */
	public HashMap listadoXPaciente(	Connection con, 
	       								String codigoCentroAtencion,
	       								int codigoInstitucion,
	       								String codigoPersona
	       							)
	{
		return cuentasProcFactDao.listadoXPaciente(con, codigoCentroAtencion, codigoInstitucion, codigoPersona);
	}
	
	/**
	 * listado X Todos
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listadoXTodos(	Connection con, 
	       							String codigoCentroAtencion,
	       							int codigoInstitucion
	       							)
	{
		return cuentasProcFactDao.listadoXTodos(con, codigoCentroAtencion, codigoInstitucion);
	}

}