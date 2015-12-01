package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubRegistrDAO;
import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntidadesSubRegistrDelegate;

public class LogRipsEntidadesSubRegistrHibernateDAO implements ILogRipsEntidadesSubRegistrDAO{

	LogRipsEntidadesSubRegistrDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabi�n Becerra
	 */
	public LogRipsEntidadesSubRegistrHibernateDAO(){
		delegate = new LogRipsEntidadesSubRegistrDelegate();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * por registro
	 * 
	 * @param logRipsEntSubReg log generado en el proceso
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg){
		return delegate.guardarLogRipsEntidadesSubRegistr(logRipsEntSubReg);
	}
	
}
