/*Julio 16 de 2007
 * 
 */

package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CondicionesXServicioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCondicionesXServiciosDao;

/**
 * @author Andr�s Eugenio Silva Monsalve
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Condiciones por Servicios
 */
public class PostgresqlCondicionesXServiciosDao implements CondicionesXServicioDao 
{
	/**
	 * M�todo implementado para consultar el consecutivo x servicio
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
	 * M�todo implementado para consultar la parametrizacion de una Condiciones de Servicios por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCondicionServiciosXConsecutivo(Connection con,HashMap campos)
	{
		return SqlBaseCondicionesXServiciosDao.consultarCondicionesXServicioXConsecutivo(con, campos);
	}
	
	/**
	 * M�todo implementado para guardar la Condiciones de Servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_Condiciones_Servicios')");
		return SqlBaseCondicionesXServiciosDao.guardar(con, campos);
	}
	
	/**
	 * M�todo que consulta las condiciones para la toma del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCondicionesTomaXServicio(Connection con,HashMap campos)
	{
		return SqlBaseCondicionesXServiciosDao.obtenerCondicionesTomaXServicio(con, campos);
	}
}
