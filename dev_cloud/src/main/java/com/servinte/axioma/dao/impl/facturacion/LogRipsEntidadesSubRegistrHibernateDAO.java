package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubRegistrDAO;
import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntidadesSubRegistrDelegate;

public class LogRipsEntidadesSubRegistrHibernateDAO implements ILogRipsEntidadesSubRegistrDAO{

	LogRipsEntidadesSubRegistrDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public LogRipsEntidadesSubRegistrHibernateDAO(){
		delegate = new LogRipsEntidadesSubRegistrDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * por registro
	 * 
	 * @param logRipsEntSubReg log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg){
		return delegate.guardarLogRipsEntidadesSubRegistr(logRipsEntSubReg);
	}
	
}
