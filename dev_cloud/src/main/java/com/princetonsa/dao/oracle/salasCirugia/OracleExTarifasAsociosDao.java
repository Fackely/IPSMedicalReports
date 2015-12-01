/*
 * Marzo 14, 2006
 */
package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.salasCirugia.ExTarifasAsociosDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseExTarifasAsociosDao;
/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrización de Excepciones Tarifas Asocios
 */
public class OracleExTarifasAsociosDao implements ExTarifasAsociosDao 
{
	private static String strInsertarEncabezado="INSERT INTO ex_tarifas_asocios VALUES(seq_ex_tarifas_asocios.nextval,?,?,?,?,?,?,?)";
	
	private static String strInsertarMedia="INSERT INTO ex_tarifas_asocios_xvitpcc VALUES(seq_extar_asoc_xvitpcc.nextval,?,?,?,?,?,?,?)";
	
	private static String strInsertarDetalle="INSERT INTO det_ex_tarifas_asocios VALUES(seq_det_ex_tarifa.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Insertar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.insertarEncabezado(con, parametros, strInsertarEncabezado);
	}
	
	
	/**
	 * Modificar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.modificarEncabezado(con, parametros);
	}
	
	
	/**
	 * Eliminar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.EliminarEncabezado(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public  HashMap consultarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.consultarEncabezado(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.insertarMedia(con, parametros, strInsertarMedia);
	}
	
	/**
	 * Modificar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.modificarMedia(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.consultarMedia(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.EliminarMedia(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.insertarDetalle(con, parametros, strInsertarDetalle);
	}
	
	/**
	 * Modificar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.modificarDetalle(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.EliminarDetalle(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.consultarDetalle(con, parametros);
	}
}