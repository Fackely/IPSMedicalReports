package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;


public interface ServiciosGruposEsteticosDao 
{
	
	/**
	 * Consulta los paquetes existentes por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */

	public abstract HashMap consultarServiciosEsteticosExistentes(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	
	public abstract boolean insertarServicio(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	/*public abstract boolean modificarServicio(Connection con,HashMap vo);*/
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public abstract boolean eliminarServicio(Connection con, int institucion, int servicio);

	
	
	////////////////////////////////////////////////////////////////////////Grupo
	public abstract boolean eliminarRegistro(Connection con, int institucion, String codigo);
	
	public abstract boolean insertar(Connection con, HashMap vo);
	
	public abstract boolean modificar(Connection con,HashMap vo);
	
	public abstract HashMap consultarGruposEsteticosExistentes(Connection con, HashMap vo);
	
}
