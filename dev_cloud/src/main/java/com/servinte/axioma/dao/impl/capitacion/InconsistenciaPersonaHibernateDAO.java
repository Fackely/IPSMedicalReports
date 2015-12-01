package com.servinte.axioma.dao.impl.capitacion;

import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenciaPersonaDAO;
import com.servinte.axioma.orm.InconsistenciaPersona;
import com.servinte.axioma.orm.delegate.capitacion.InconsistenciaPersonaDelegate;

public class InconsistenciaPersonaHibernateDAO implements IInconsistenciaPersonaDAO{

	InconsistenciaPersonaDelegate delegate;
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo Gómez
	 */
	public InconsistenciaPersonaHibernateDAO(){
		delegate = new InconsistenciaPersonaDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de InconsistenciaPersona
	 * 
	 * @param InconsistenciaPersona 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciaPersona(InconsistenciaPersona inconsistenciaPersona){
		return delegate.guardarInconsistenciaPersona(inconsistenciaPersona);
	}
}
