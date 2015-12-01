package com.servinte.axioma.dao.interfaz.administracion;

import com.servinte.axioma.orm.LogProcesoInactivacionUsu;

/**
 * Esta clase se encarga de definir los métodos para la entidad
 * LogProcesoInactivacionUsu
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 15/01/2011
 */
public interface ILogProcesoInactivacionUsuDAO {

	/**
	 * Método encargado de insertar el log para los usuarios que se van a inactivar.
	 * 
	 * @param logProcesoInactivacionUsu  Información del log a insertar
	 * @return retorna  Cuando es true es porque se pudo almacenar el log, en
	 *         caso contrario es false
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 15/01/2011
	 */
	public boolean insertar (LogProcesoInactivacionUsu logProcesoInactivacionUsu);

}
