package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;


public interface AsocioServicioTarifaDao 
{	
	/**
	 * Método para cargar el listado de los asocios servicios tarifa
	 * parametrizadas por institución
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap cargarEncabAsociosServ(Connection con,HashMap parametros);
	
	/**
	 * Método usado para modificar un registros de encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public boolean actualizarEncaAsociosSer(
			Connection con,
			HashMap parametros);
	
	/**
	 * Método para eliminar un porcentaje de Asocios Servicios de Tarifa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarEncaAsociosServ(Connection con,int codigo);
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros
	 */
	public int insertarEncaAsociosServ(
			Connection con,
			HashMap parametros);
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarDetAsociosServicios(Connection con, HashMap parametros);
	
	/**
	 * Modificar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarDetAsociosServ(Connection con, HashMap parametros);
	
	/**
	 * Eliminar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarDetAsociosServ(Connection con, HashMap parametros);
	
	/**
	 * Consultar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarDetAsociosServ(Connection con, HashMap parametros);
}