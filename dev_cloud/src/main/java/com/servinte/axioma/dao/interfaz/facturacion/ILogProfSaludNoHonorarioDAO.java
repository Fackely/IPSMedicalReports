package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.LogProfSaludNoHonorario;

/**
 * Esta clase se encarga de definir los métodos para la entidad
 * LogProfSaludNoHonorario
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 13/01/2011
 */
public interface ILogProfSaludNoHonorarioDAO {

	/**
	 * Método encargado de insertar el log para profesionales de la salud a los
	 * cuales no se les va a generar valor de honorarios.
	 * 
	 * @param logProfSaludNoHonorario  Información del log a insertar
	 * @return retorna  Cuando es true es porque se pudo almacenar el log, en
	 *         caso contrario es false
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	public boolean insertar (LogProfSaludNoHonorario logProfSaludNoHonorario);

}
