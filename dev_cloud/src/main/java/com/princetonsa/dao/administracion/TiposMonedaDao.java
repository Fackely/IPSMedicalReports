package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface TiposMonedaDao {
	
	/**
	 * Insertar los tipos de monedas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarTiposMoneda(Connection con,HashMap vo);
	
	/**
	 * Consultar los tipos de monedas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposMoneda(Connection con);
	
	/**
	 * Modificar los tipos de monedas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarTiposMoneda(Connection con, HashMap vo);
	
	/**
	 * Eliminar los tipos de monedas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarTiposMoneda(Connection con, int codigo);

}
