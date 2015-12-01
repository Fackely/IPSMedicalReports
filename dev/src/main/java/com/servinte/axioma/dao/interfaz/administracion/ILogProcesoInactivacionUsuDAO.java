package com.servinte.axioma.dao.interfaz.administracion;

import com.servinte.axioma.orm.LogProcesoInactivacionUsu;

/**
 * Esta clase se encarga de definir los m�todos para la entidad
 * LogProcesoInactivacionUsu
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 15/01/2011
 */
public interface ILogProcesoInactivacionUsuDAO {

	/**
	 * M�todo encargado de insertar el log para los usuarios que se van a inactivar.
	 * 
	 * @param logProcesoInactivacionUsu  Informaci�n del log a insertar
	 * @return retorna  Cuando es true es porque se pudo almacenar el log, en
	 *         caso contrario es false
	 * 
	 * @author Luis Fernando Hincapi� Ospina
	 * @since 15/01/2011
	 */
	public boolean insertar (LogProcesoInactivacionUsu logProcesoInactivacionUsu);

}
