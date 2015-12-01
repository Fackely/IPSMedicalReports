/*
 * @(#)AsociarCxCAFacturasCapitadas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.AsociarCxCAFacturasDao;

/**
 * Clase para el manejo de asociacion cuentas cobro a facturas capitacion
 * @version 1.0, Agosto 01, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class AsociarCxCAFacturasCapitadas 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static AsociarCxCAFacturasDao asociarCxCAFacturasDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AsociarCxCAFacturasCapitadas.class);
	
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
	public AsociarCxCAFacturasCapitadas()
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
			asociarCxCAFacturasDao = myFactory.getAsociarCxCAFacturasDao();
			wasInited = (asociarCxCAFacturasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Busqueda de las cuentas cobro 
	 * @param con
	 * @param criteriosBusquedaMap ( keys= codigoConvenio, fechaInicial, fechaFinal), 
	 * 								las fechas deben estar en formato aplicacion
	 * @return
	 */
	public HashMap busquedaCuentasCobroAAsociar(Connection con, HashMap criteriosBusquedaMap)
	{
		HashMap mapa= new HashMap();
		try
		{
			mapa=asociarCxCAFacturasDao.busquedaCuentasCobroAAsociar(con, criteriosBusquedaMap);
		}
		catch(Exception e)
		{
			logger.warn("Error busquedaCuentasCobroAAsociar " +e.toString());
		}
		return mapa;
	}
	
	/**
	 * insertar
	 * @param con
	 * @param numeroCuentaCobro
	 * @param contabilizado 
	 * @param codigoConvenio
	 * @param loginUsuario
	 * @param institucion
	 * @return
	 */
	public boolean insertar (	Connection con, String numeroCuentaCobro,
								String contabilizado, int codigoConvenio, String loginUsuario,
								int institucion, int cantidadFacturasAsociadas)
	{
		return asociarCxCAFacturasDao.insertar(con, numeroCuentaCobro, contabilizado, codigoConvenio, loginUsuario, institucion, cantidadFacturasAsociadas);
	}
	
	/**
	 * selecciona las facturas a asociar a una cuenta de cobro, recibe un vector con las facturas ya seleccionadas para que
	 * no existan problemas con n cuentas de cobro que tengan la misma factura, es decir, se asigna al primero que llegue
	 * @param con
	 * @param fechaInicialCuentaCobro
	 * @param fechaFinalCuentaCobro
	 * @param codigoConvenio
	 * @param facturasYaSelecionadasVector
	 * @return
	 */
	public HashMap seleccionFacturasAAsociar(	Connection con , String fechaInicialCuentaCobro, String fechaFinalCuentaCobro, int codigoConvenio, Vector facturasYaSelecionadasVector)
	{
		return asociarCxCAFacturasDao.seleccionFacturasAAsociar(con, fechaInicialCuentaCobro, fechaFinalCuentaCobro, codigoConvenio,facturasYaSelecionadasVector);
	}
}
