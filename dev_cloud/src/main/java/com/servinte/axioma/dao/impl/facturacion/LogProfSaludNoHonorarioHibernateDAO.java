package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogProfSaludNoHonorarioDAO;
import com.servinte.axioma.orm.LogProfSaludNoHonorario;
import com.servinte.axioma.orm.delegate.facturacion.LogProfSaludNoHonorarioDelegate;

/**
 * Esta clase se encarga de definir los m�todos para la entidad
 * LogProfSaludNoHonorario
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 13/01/2011
 */
public class LogProfSaludNoHonorarioHibernateDAO implements
		ILogProfSaludNoHonorarioDAO {

	/**
	 * Instancia de la clase LogProfSaludNoHonorarioDelegate
	 */
	LogProfSaludNoHonorarioDelegate delegate;

	/**
	 * M�todo constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapi� Ospina
	 * @since 13/01/2011
	 */
	public LogProfSaludNoHonorarioHibernateDAO() {
		delegate = new LogProfSaludNoHonorarioDelegate();
	}

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
	@Override
	public boolean insertar(LogProfSaludNoHonorario logProfSaludNoHonorario) {
		return delegate.insertar(logProfSaludNoHonorario);
	}

}
