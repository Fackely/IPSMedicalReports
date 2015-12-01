/*Julio 16 de 2007
 * 
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CondicionesXServicioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCondicionesXServiciosDao;

/**
 * @author Andrés Eugenio Silva Monsalve
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Condiciones por Servicios
 */
public class OracleCondicionesXServiciosDao implements CondicionesXServicioDao 
{
	/**
	 * Método implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de Condiciones por Servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public int consultarConsecutivoXServicio(Connection con,HashMap campos)
	{
		return SqlBaseCondicionesXServiciosDao.consultarConsecutivoXServicio(con, campos);
	}
	
	/**
	 * Método implementado para consultar la parametrizacion de una Condiciones por Servicios por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCondicionServiciosXConsecutivo(Connection con,HashMap campos)
	{
		return SqlBaseCondicionesXServiciosDao.consultarCondicionesXServicioXConsecutivo(con, campos);
	}
	
	/**
	 * Método implementado para guardar la Condiciones de Servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,HashMap campos)
	{
		campos.put("secuencia","seq_Condiciones_Servicios.nextval");
		return SqlBaseCondicionesXServiciosDao.guardar(con, campos);
	}
	
	/**
	 * Método que consulta las condiciones para la toma del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCondicionesTomaXServicio(Connection con,HashMap campos)
	{
		return SqlBaseCondicionesXServiciosDao.obtenerCondicionesTomaXServicio(con, campos);
	}
}
