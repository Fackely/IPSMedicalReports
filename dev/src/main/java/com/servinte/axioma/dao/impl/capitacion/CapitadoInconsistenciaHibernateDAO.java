package com.servinte.axioma.dao.impl.capitacion;

import com.servinte.axioma.dao.interfaz.capitacion.ICapitadoInconsistenciaDAO;
import com.servinte.axioma.orm.CapitadoInconsistencia;
import com.servinte.axioma.orm.delegate.capitacion.CapitadoInconsistenciaDelegate;

public class CapitadoInconsistenciaHibernateDAO implements ICapitadoInconsistenciaDAO {

	CapitadoInconsistenciaDelegate delegate;
	
	/**
	 * Metodo constructor de la clase
	 * @author Camilo Gómez
	 */
	public CapitadoInconsistenciaHibernateDAO(){
		delegate = new CapitadoInconsistenciaDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de CapitadoInconsistencia
	 * 
	 * @param CapitadoInconsistencia 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarCapitadoInconsistencia(CapitadoInconsistencia capitadoInconsistencia){
		return delegate.guardarCapitadoInconsistencia(capitadoInconsistencia);
	}
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return CapitadoInconsistencia
	 */
	public CapitadoInconsistencia obtenerCapitadoInconsistenciaPorId(Long id) {
		return delegate.obtenerCapitadoInconsistenciaPorId(id);
	}
}
