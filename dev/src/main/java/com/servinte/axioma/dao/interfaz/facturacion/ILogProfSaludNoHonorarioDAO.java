package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.LogProfSaludNoHonorario;

/**
 * Esta clase se encarga de definir los m�todos para la entidad
 * LogProfSaludNoHonorario
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 13/01/2011
 */
public interface ILogProfSaludNoHonorarioDAO {

	/**
	 * M�todo encargado de insertar el log para profesionales de la salud a los
	 * cuales no se les va a generar valor de honorarios.
	 * 
	 * @param logProfSaludNoHonorario  Informaci�n del log a insertar
	 * @return retorna  Cuando es true es porque se pudo almacenar el log, en
	 *         caso contrario es false
	 * 
	 * @author Luis Fernando Hincapi� Ospina
	 * @since 13/01/2011
	 */
	public boolean insertar (LogProfSaludNoHonorario logProfSaludNoHonorario);

}
