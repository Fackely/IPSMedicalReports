/*
 * Aug 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.mundo.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.AnulacionCargosFarmaciaDao;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class AnulacionCargosFarmacia 
{
	
	private AnulacionCargosFarmaciaDao objetoDao;
	/**
	 * 
	 */
	public AnulacionCargosFarmacia() 
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getAnulacionCargosFarmaciaDao();
		}
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap<String, Object> consultarSolicitudes(Connection con, String subCuenta) 
	{
		return objetoDao.consultarSolicitudes(con,subCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta 
	 * @param string
	 * @return
	 */
	public HashMap<String, Object> consultarDetalleSolicitudes(Connection con, String numeroSolicitud, String subCuenta,HashMap<String, Object> ordenes) 
	{
		return objetoDao.consultarDetalleSolicitudes(con,numeroSolicitud,subCuenta,ordenes);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param motivoAnulacion
	 * @param detalleSolicitudes
	 * @param usuario 
	 */
	public boolean guardarAnulacion(Connection con, String subCuenta, String numeroSolicitud, String motivoAnulacion, HashMap<String, Object> detalleSolicitudes, String usuario,String codigoCentroCosto,int institucion,String estado,HashMap<String, Object> detalleSolicitudesOrdenes) 
	{
		return objetoDao.guardarAnulacion(con,subCuenta,numeroSolicitud,motivoAnulacion,detalleSolicitudes,usuario,codigoCentroCosto,institucion,estado,detalleSolicitudesOrdenes);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap<String, Object> consultarDetalleSolicitudesOrdenes(Connection con, String subCuenta) 
	{
		return objetoDao.consultarSolicitudesDetalle(con,subCuenta);
	}
}
