package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaTercerosGenericaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaTercerosGenericaDao;

public class BusquedaTercerosGenerica {

	/**
	 * codigo de institucion en sesion
	 */
	private int institucion;
	
	
	/**
	 * dao
	 */
	private BusquedaTercerosGenericaDao busqTercerosDao = null;
	
	
	
	/** 
	 * 
	 * constructores
	 */
	public BusquedaTercerosGenerica()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
		
	public void clean()
	{
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (busqTercerosDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			busqTercerosDao = myFactory.getBusquedaTercerosGenericaDao();
		}	
	}
	
	/**
	 * metodo de consulta de terceros
	 * @param con
	 * @param nit
	 * @param descripcionTercero
	 * @return
	 */
	public HashMap<String, Object> consultarTerceros(Connection con, String nit, String descripcionTercero, String filtrarXEstadoActivo, String filtrarXRelacionEmpresa, String filtrarXDeudor, String tipoTercero)
	{
		return busqTercerosDao.consultarTerceros(con, institucion, nit, descripcionTercero,filtrarXEstadoActivo, filtrarXRelacionEmpresa, filtrarXDeudor, tipoTercero);
	}
	
	/**
	 * metodo de consulta de terceros
	 * @param con
	 * @param nit
	 * @param descripcionTercero
	 * @return
	 */
	public HashMap<String, Object> consultarTerceros(Connection con, String nit, String descripcionTercero, String filtrarXEstadoActivo, String filtrarXRelacionEmpresa, String filtrarXDeudor)
	{
		return busqTercerosDao.consultarTerceros(con, institucion, nit, descripcionTercero,filtrarXEstadoActivo, filtrarXRelacionEmpresa, filtrarXDeudor, "");
	}
	
	/**
	 * Metodo dencargado de obtener los tipos de 
	 * terceros
	 * @param connection
	 * @return
	 */
	public  ArrayList<HashMap<String, Object>> obtenerTiposTerceros (Connection connection)
	{
		return busqTercerosDao.obtenerTiposTerceros(connection);
	}
	
	/**
	 * Metodo encargado de consultar los terceros
	 * pudiendo filtar por otros campos
	 * @param con
	 * @param institucion
	 * @param nit
	 * @param descripcionTercero
	 * @param filtrarXEstadoActivo
	 * @param filtrarXRelacionEmpresa
	 * @param filtrarXDeudor
	 * @param razonSocial
	 * @param tipoTercero
	 * @return
	 */
	public HashMap consultarTercerosAvan (Connection con, int institucion, String nit, String descripcionTercero, String  filtrarXEstadoActivo, String tipoTercero,String esEmpresa)
	{
		return busqTercerosDao.consultarTercerosAvan(con, institucion, nit, descripcionTercero, filtrarXEstadoActivo, tipoTercero,esEmpresa);
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
}
