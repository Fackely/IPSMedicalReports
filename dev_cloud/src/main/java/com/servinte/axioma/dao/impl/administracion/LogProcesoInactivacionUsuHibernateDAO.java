package com.servinte.axioma.dao.impl.administracion;

import com.servinte.axioma.dao.interfaz.administracion.ILogProcesoInactivacionUsuDAO;
import com.servinte.axioma.orm.LogProcesoInactivacionUsu;
import com.servinte.axioma.orm.delegate.administracion.LogProcesoInactivacionUsuDelegate;

/**
 * Esta clase se encarga de definir los métodos para la entidad
 * LogProcesoInactivacionUsu
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 15/01/2011
 */
public class LogProcesoInactivacionUsuHibernateDAO implements
		ILogProcesoInactivacionUsuDAO {

	/**
	 * Instancia de la clase LogProfSaludNoHonorarioDelegate
	 */
	LogProcesoInactivacionUsuDelegate delegate;

	/**
	 * Método constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 13/01/2011
	 */
	public LogProcesoInactivacionUsuHibernateDAO() {
		delegate = new LogProcesoInactivacionUsuDelegate();
	}

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
	@Override
	public boolean insertar (LogProcesoInactivacionUsu logProcesoInactivacionUsu){
		return delegate.insertar(logProcesoInactivacionUsu);
	}

}
