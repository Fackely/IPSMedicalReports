package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubArchivoDAO;
import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntidadesSubArchivoDelegate;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntidadesSubcontratadasDelegate;

public class LogRipsEntidadesSubArchivoHibernateDAO implements ILogRipsEntidadesSubArchivoDAO{

	LogRipsEntidadesSubArchivoDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public LogRipsEntidadesSubArchivoHibernateDAO(){
		delegate = new LogRipsEntidadesSubArchivoDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso por achivo de rips entidades subcontratadas
	 * 
	 * @param logRipsEntSubArchivo log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubArchivo(LogRipsEntidadesSubArchivo logRipsEntSubArchivo){
		return delegate.guardarLogRipsEntidadesSubArchivo(logRipsEntSubArchivo);
	}
	
}
