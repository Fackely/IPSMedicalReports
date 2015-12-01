package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisCampDAO;
import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubInconsisArchDelegate;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubInconsisCampDelegate;

public class LogRipsEntSubInconsisCampHibernateDAO implements ILogRipsEntSubInconsisCampDAO{

	LogRipsEntSubInconsisCampDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabi�n Becerra
	 */
	public LogRipsEntSubInconsisCampHibernateDAO(){
		delegate = new LogRipsEntSubInconsisCampDelegate();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * inconsistencias de campos
	 * 
	 * @param logRipsEntSubInconCamp log inconsistencia de campo generada en el proceso
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubInconsistenciaCampo(LogRipsEntSubInconsisCamp logRipsEntSubInconCamp){
		return delegate.guardarLogRipsEntSubInconsistenciaCampo(logRipsEntSubInconCamp);
	}
}
