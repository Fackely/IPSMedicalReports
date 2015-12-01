package com.servinte.axioma.dao.impl.administracion;

import com.servinte.axioma.dao.interfaz.administracion.ILogProcesoInactivacionUsuDAO;
import com.servinte.axioma.orm.LogProcesoInactivacionUsu;
import com.servinte.axioma.orm.delegate.administracion.LogProcesoInactivacionUsuDelegate;

/**
 * Esta clase se encarga de definir los m�todos para la entidad
 * LogProcesoInactivacionUsu
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 15/01/2011
 */
public class LogProcesoInactivacionUsuHibernateDAO implements
		ILogProcesoInactivacionUsuDAO {

	/**
	 * Instancia de la clase LogProfSaludNoHonorarioDelegate
	 */
	LogProcesoInactivacionUsuDelegate delegate;

	/**
	 * M�todo constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapi� Ospina
	 * @since 13/01/2011
	 */
	public LogProcesoInactivacionUsuHibernateDAO() {
		delegate = new LogProcesoInactivacionUsuDelegate();
	}

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
	@Override
	public boolean insertar (LogProcesoInactivacionUsu logProcesoInactivacionUsu){
		return delegate.insertar(logProcesoInactivacionUsu);
	}

}
