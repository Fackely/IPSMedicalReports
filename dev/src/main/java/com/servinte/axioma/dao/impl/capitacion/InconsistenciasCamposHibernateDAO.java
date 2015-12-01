package com.servinte.axioma.dao.impl.capitacion;

import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenciasCamposDAO;
import com.servinte.axioma.orm.InconsistenciasCampos;
import com.servinte.axioma.orm.delegate.capitacion.InconsistenciasCamposDelegate;

public class InconsistenciasCamposHibernateDAO implements IInconsistenciasCamposDAO{

	InconsistenciasCamposDelegate delegate;
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo G�mez
	 */
	public InconsistenciasCamposHibernateDAO(){
		delegate = new InconsistenciasCamposDelegate();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenciasCampos
	 * 
	 * @param InconsistenciasCampos 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciasCampos(InconsistenciasCampos inconsistenciasCampos){
		return delegate.guardarInconsistenciasCampos(inconsistenciasCampos); 
	}
}
