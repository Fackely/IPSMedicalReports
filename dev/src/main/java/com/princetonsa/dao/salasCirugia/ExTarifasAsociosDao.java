/*
 * Marzo 14, 2006
 */

package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Excepciones Tarifas Asocios
 */
public interface ExTarifasAsociosDao 
{
	/**
	 * Insertar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarEncabezado(Connection con, HashMap parametros);
	
	
	/**
	 * Modificar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarEncabezado(Connection con, HashMap parametros);
	
	
	/**
	 * Eliminar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarEncabezado(Connection con, HashMap parametros);
	
	/**
	 * Consultar informacion tabla Encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarEncabezado(Connection con, HashMap parametros);
	
	/**
	 * Insertar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarMedia(Connection con, HashMap parametros);
	
	/**
	 * Modificar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarMedia(Connection con, HashMap parametros);
	
	/**
	 * Consultar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public  HashMap consultarMedia(Connection con, HashMap parametros);
	
	/**
	 * Eliminar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarMedia(Connection con, HashMap parametros);
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarDetalle(Connection con, HashMap parametros);
	
	/**
	 * Modificar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarDetalle(Connection con, HashMap parametros);
	
	/**
	 * Eliminar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarDetalle(Connection con, HashMap parametros);
	
	/**
	 * Consultar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarDetalle(Connection con, HashMap parametros);	
}