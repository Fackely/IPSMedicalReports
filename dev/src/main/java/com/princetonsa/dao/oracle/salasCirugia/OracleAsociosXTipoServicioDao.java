package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.salasCirugia.AsociosXTipoServicioDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseAsociosXTipoServicioDao;

public class OracleAsociosXTipoServicioDao implements AsociosXTipoServicioDao {
	
	/**
	 * Metodo de consulta de servicios asociados
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> cargarServiciosAsocios(Connection con, int institucion)
	{
		return SqlBaseAsociosXTipoServicioDao.cargarServiciosAsocios(con, institucion);
	}
	
	/**
	 * Metodo para cargar todos los tipos de servicio en un select
	 * @param con
	 * @return
	 */
	
	public HashMap<String, Object> cargarTiposServicio(Connection con)
	{
		return SqlBaseAsociosXTipoServicioDao.cargarTiposServicio(con);
	}
	
	
	/**
	 * Metodo para cargar todos los tipos de asocio dependiendo de la institucion en un select
	 * @param con
	 * @return
	 */
	
	public HashMap<String, Object> cargarTiposAsocios(Connection con, int institucion)
	{
		return SqlBaseAsociosXTipoServicioDao.cargarTiposAsocios(con, institucion);
	}
	
	/**
	 * Metdo de insercion de un nuevo registro de asocios por tipo de servicio
	 */
	public boolean insertarTiposAsocios(Connection con,			
			String tipo_servicio,
			int asocio,
			int servicio,
			int institucion, 
			String usuario)
	{
		String secuencia = "seq_servicios_asociados.nextval";
		return SqlBaseAsociosXTipoServicioDao.insertarTiposAsocios(con, secuencia, tipo_servicio, asocio, servicio, institucion, usuario);
	}
	
	/**
	 * Metodo de eliminacion de un servicio Asocio
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	public boolean eliminarServAsocio(Connection con, int institucion, int codigo)
	{
		return SqlBaseAsociosXTipoServicioDao.eliminarServAsocio(con, institucion, codigo);
	}
	
	
	/**
	 * Metodo de modificacion de un registro de servicio asocios
	 * @param con
	 * @param codigo
	 * @param tipo_servicio
	 * @param asocio
	 * @param servicio
	 * @param activo
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public boolean modificarServAsocio (Connection con, 
															int codigo,
															String tipo_servicio,
															int asocio,
															int servicio,
															int institucion, 
															String usuario)
	{
		return SqlBaseAsociosXTipoServicioDao.modificarServAsocio(con, codigo, tipo_servicio, asocio, servicio,  institucion, usuario);
	}

}
