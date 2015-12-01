package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubRegValorDAO;
import com.servinte.axioma.orm.LogRipsEntSubRegValor;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubInconsisCampDelegate;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntSubRegValorDelegate;

public class LogRipsEntSubRegValorHibernateDAO implements ILogRipsEntSubRegValorDAO{
	
	LogRipsEntSubRegValorDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public LogRipsEntSubRegValorHibernateDAO(){
		delegate = new LogRipsEntSubRegValorDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * valores de los campos leidos
	 * 
	 * @param logRipsEntSubRegVal valores y campos leidos en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubRegValores(LogRipsEntSubRegValor logRipsEntSubRegVal){
		return delegate.guardarLogRipsEntSubRegValores(logRipsEntSubRegVal);
	}

}
