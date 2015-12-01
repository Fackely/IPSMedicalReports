package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.AsocioServicioTarifaDao;

public class AsocioServicioTarifa
{
	
	/**
	 * Instancia el Dao
	 * */
	public static AsocioServicioTarifaDao getAsocioServicioTarifaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsocioServicioTarifaDao();
	}	
	
	/**
	 * Método para cargar el listado de los asocios servicios tarifa
	 * parametrizadas por institución
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static HashMap cargarEncabAsociosServ(Connection con,HashMap parametros)
	{
		return getAsocioServicioTarifaDao().cargarEncabAsociosServ(con, parametros);
	}
	
	/**
	 * Método usado para modificar un registros de encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public static boolean actualizarEncaAsociosSer(
			Connection con,
			HashMap parametros)
	{
		return getAsocioServicioTarifaDao().actualizarEncaAsociosSer(con, parametros);
	}
	
	/**
	 * Método para eliminar un porcentaje de Asocios Servicios de Tarifa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarEncaAsociosServ(Connection con,int codigo)
	{
		return getAsocioServicioTarifaDao().eliminarEncaAsociosServ(con, codigo);
	}
	
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros
	 * @param insertarPorcentajeStr
	 * @return Codigo del encabezado
	 */
	public static int insertarEncaAsociosServ(
			Connection con,
			HashMap parametros)
	{
		return getAsocioServicioTarifaDao().insertarEncaAsociosServ(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean insertarDetAsociosServicios(Connection con, HashMap parametros)
	{
		return getAsocioServicioTarifaDao().insertarDetAsociosServicios(con, parametros);
	}
	
	/**
	 * Modificar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean modificarDetAsociosServ(Connection con, HashMap parametros)
	{
		return getAsocioServicioTarifaDao().modificarDetAsociosServ(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean EliminarDetAsociosServ(Connection con, HashMap parametros)
	{
		return getAsocioServicioTarifaDao().EliminarDetAsociosServ(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static HashMap consultarDetAsociosServ(Connection con, HashMap parametros)
	{
		return getAsocioServicioTarifaDao().consultarDetAsociosServ(con, parametros);
	}		
}