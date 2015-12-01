/*
 * Creado el Jun 30, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.AprobacionAjustesDao;


public class AprobacionAjustes {

	/**
	 * Variable de interfaz para acceder a la BD.
	 */
	private AprobacionAjustesDao aprobacionAjustesDao;
	
	/**
	 * Mapa para guardar toda las descripciones de los Niveles de Servicios.
	 */
	private HashMap mapa;

	/**
	 * Constructor de la clase.
	 *
	 */
	public AprobacionAjustes()
	{
		this.init(System.getProperty("TIPOBD"));
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
			aprobacionAjustesDao = myFactory.getAprobacionAjustesDao();
			wasInited = (aprobacionAjustesDao != null);
		}
		return wasInited;
	}

	
	/**
	 * Metodo para cargar Informacion Parametrizada y no Parametrizada.
	 * @param con
	 * @param 
	 * @return
	 * @throws SQLException 
	 */
	public HashMap cargarInformacion(Connection con, HashMap mapaParam) throws SQLException 
	{
		if (aprobacionAjustesDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (AprobacionAjustes - cargarInformacion )"); 
		}
		
		return aprobacionAjustesDao.cargarInformacion(con, mapaParam);
	}

	/**
	 * Metodo para Aprobar el ajuste.
	 * @param con
	 * @param mapaParam
	 * @return
	 * @throws SQLException 
	 */
	public int aprobarAjuste(Connection con, HashMap mapaParam) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (aprobacionAjustesDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (AprobacionAjustes - aprobarAjuste )"); 
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		resp1=aprobacionAjustesDao.aprobarAjuste(con, mapaParam);
		
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapa() {
		return this.mapa;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapa(String key) {
		return this.mapa.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}


}
