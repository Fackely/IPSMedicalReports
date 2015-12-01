package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisCampDAO;
import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubInconsisArchDelegate;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubInconsisCampDelegate;

public class LogRipsEntSubInconsisCampHibernateDAO implements ILogRipsEntSubInconsisCampDAO{

	LogRipsEntSubInconsisCampDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public LogRipsEntSubInconsisCampHibernateDAO(){
		delegate = new LogRipsEntSubInconsisCampDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * inconsistencias de campos
	 * 
	 * @param logRipsEntSubInconCamp log inconsistencia de campo generada en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubInconsistenciaCampo(LogRipsEntSubInconsisCamp logRipsEntSubInconCamp){
		return delegate.guardarLogRipsEntSubInconsistenciaCampo(logRipsEntSubInconCamp);
	}
}
