package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsumosXFacturarDao;
import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;

/**
 * 
 * @author wilson
 *
 */
public class ConsumosXFacturar 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ConsumosXFacturar.class);
	
	/**
	 * DAO de este objeto,
	 */
    private ConsumosXFacturarDao dao;
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( dao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			dao= myFactory.getConsumosXFacturarDao();
			if( dao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturar(int institucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoInterfazConsumosXFacturar> arrayDto= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosXFacturarDao().obtenerConsumosXFacturar(con, institucion);
		UtilidadBD.closeConnection(con);
		return arrayDto;
	}
	
	
	/**
	 * Metodo encargado de obtener los datos de la BD local
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturarReproceso(int institucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoInterfazConsumosXFacturar> arrayDto= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosXFacturarDao().obtenerConsumosXFacturarReproceso(con, institucion);
		UtilidadBD.closeConnection(con);
		return arrayDto;
	}
}
