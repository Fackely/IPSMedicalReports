package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ImpresionSoportesFacturasDao;

/**
 * 
 * Mundo de Impresion de Soporte de Facturas
 * @author jfhernandez@princetonsa.com
 *
 */
public class ImpresionSoportesFacturas {
	
	// --------------- 	ATRIBUTOS
	
	private static Logger logger = Logger.getLogger(SoportesFacturas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ImpresionSoportesFacturasDao impresionSoportesFacturasDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	// ----------------	METODOS
	
	/**
	 * Reset
	 */
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
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
			impresionSoportesFacturasDao = myFactory.getImpresionSoportesFacturasDao();
			wasInited = (impresionSoportesFacturasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Guardar
	 * @param con
	 * @param mapaOrganizado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 */
	public static HashMap listarImprimir(Connection con, HashMap<String, Object> listado, int codigoInstitucionInt, String loginUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionSoportesFacturasDao().listarImprimir(con, listado, codigoInstitucionInt, loginUsuario);
	}
}
	
	