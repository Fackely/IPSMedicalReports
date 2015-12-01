package com.servinte.axioma.dao.impl.capitacion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenSubirPacienteDAO;
import com.servinte.axioma.orm.InconsistenSubirPaciente;
import com.servinte.axioma.orm.delegate.capitacion.InconsistenSubirPacienteDelegate;

public class InconsistenSubirPacienteHibernateDAO implements IInconsistenSubirPacienteDAO{

	InconsistenSubirPacienteDelegate delegate;
	
	/**
	 * Metodo constructor de la clase
	 * 
	 * @author Camilo Gómez
	 */
	public InconsistenSubirPacienteHibernateDAO(){
		delegate = new InconsistenSubirPacienteDelegate();
	}
	
	/**
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de inconsistenSubirPaciente
	 * 
	 * @param inconsistenSubirPaciente 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciaSubirPaciente(InconsistenSubirPaciente inconsistenSubirPaciente){
		return delegate.guardarInconsistenciaSubirPaciente(inconsistenSubirPaciente);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IInconsistenSubirPacienteDAO#buscarInconsistenciasPorLog(long)
	 */
	@Override
	public List<InconsistenSubirPaciente> buscarInconsistenciasPorLog(
			long codigoLogSubirPaciente) {
		return delegate.buscarInconsistenciasPorLog(codigoLogSubirPaciente);
	}
}
