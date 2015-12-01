package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogProfSaludNoHonorario;
import com.servinte.axioma.orm.LogProfSaludNoHonorarioHome;

/**
 * Clase delegate de la entidad LogProfSaludNoHonorario
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 13/01/2011
 */
public class LogProfSaludNoHonorarioDelegate extends
		LogProfSaludNoHonorarioHome {

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
	public boolean insertar(LogProfSaludNoHonorario logProfSaludNoHonorario) {

		boolean resultado = false;

		try {
			super.persist(logProfSaludNoHonorario);
			resultado = true;
		} catch (Exception e) {
			resultado = false;
			Log4JManager.error("No se pudo guardar el registro del Log Profesional Salud No Honorario: ",e);
		}

		return resultado;

	}

}
