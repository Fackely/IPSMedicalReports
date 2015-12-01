package com.servinte.axioma.orm.delegate.administracion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogProcesoInactivacionUsu;
import com.servinte.axioma.orm.LogProcesoInactivacionUsuHome;

/**
 * Clase delegate de la entidad LogProcesoInactivacionUsu
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 15/01/2011
 */
public class LogProcesoInactivacionUsuDelegate extends
		LogProcesoInactivacionUsuHome {

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
	public boolean insertar (LogProcesoInactivacionUsu logProcesoInactivacionUsu){

		boolean resultado = false;

		try {
			super.persist(logProcesoInactivacionUsu);
			resultado = true;
		} catch (Exception e) {
			resultado = false;
			Log4JManager.error("No se pudo guardar el registro del Log Proceso Inactivación Usuario: ",e);
		}

		return resultado;

	}

}
