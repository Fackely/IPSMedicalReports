/*
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.RegistroSaldosInicialesCapitacionDao;

/**
 * Clase para el manejo de registro saldos iniciales
 * @version 1.0, Agosto 01, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class RegistroSaldosInicialesCapitacion 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RegistroSaldosInicialesCapitacionDao registroDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RegistroSaldosInicialesCapitacion.class);
	
	/**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{}
	
	/**
	 * Constructor vacio
	 *
	 */
	public RegistroSaldosInicialesCapitacion()
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
			registroDao = myFactory.getRegistroSaldosInicialesCapitacionDao();
			wasInited = (registroDao != null);
		}
		return wasInited;
	}
	
	/**
	 * busqueda de las cuentas de cobro 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */	
	public HashMap busquedaCuentasCobro(	Connection con,
												HashMap criteriosBusquedaMap
											  ) throws SQLException
	{
		return registroDao.busquedaCuentasCobro(con, criteriosBusquedaMap);
	}
	
	/**
	 * metodo que carga en el mapa los cargues que tienen saldo pendiente = 0 ó ajustes sin aprobar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap existenCarguesSaldoPendienteCeroOAjustesSinAprobar(Connection con, HashMap criteriosBusquedaMap)
	{
		return registroDao.existenCarguesSaldoPendienteCeroOAjustesSinAprobar(con, criteriosBusquedaMap);
	}
	
	
	
}
