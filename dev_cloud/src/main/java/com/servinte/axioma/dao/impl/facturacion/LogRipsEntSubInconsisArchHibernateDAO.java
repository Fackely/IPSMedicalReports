package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisArchDAO;
import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubInconsisArchDelegate;

public class LogRipsEntSubInconsisArchHibernateDAO implements ILogRipsEntSubInconsisArchDAO{
	
	
	LogRipsEntSubInconsisArchDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public LogRipsEntSubInconsisArchHibernateDAO(){
		delegate = new LogRipsEntSubInconsisArchDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * inconsistencias de archivos
	 * 
	 * @param logRipsEntSubInconArch log inconsistencia de archivo generada en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubInconsistenciaArchivo(LogRipsEntSubInconsisArch logRipsEntSubInconArch){
		return delegate.guardarLogRipsEntSubInconsistenciaArchivo(logRipsEntSubInconArch);
	}

}
