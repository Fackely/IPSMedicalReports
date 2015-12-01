package com.servinte.axioma.dao.impl.tesoreria;

import com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO;
import com.servinte.axioma.orm.DetalleNotaPaciente;
import com.servinte.axioma.orm.delegate.tesoreria.DetalleNotaPacienteDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleNotaPacienteDAO}.
 * 
 * @author diecorqu
 * @see DetalleNotaPacienteDelegate	
 */
public class DetalleNotaPacienteHibernateDAO implements IDetalleNotaPacienteDAO {

	DetalleNotaPacienteDelegate delegate;
	
	public DetalleNotaPacienteHibernateDAO() {
		delegate = new DetalleNotaPacienteDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO#guardarDetalleNotaPaciente(com.servinte.axioma.orm.DetalleNotaPaciente)
	 */
	@Override
	public boolean guardarDetalleNotaPaciente(
			DetalleNotaPaciente detalleNotaPaciente) {
		return delegate.guardarDetalleNotaPaciente(detalleNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO#eliminarDetalleNotaPaciente(com.servinte.axioma.orm.DetalleNotaPaciente)
	 */
	@Override
	public boolean eliminarDetalleNotaPaciente(
			DetalleNotaPaciente detalleNotaPaciente) {
		return delegate.eliminarDetalleNotaPaciente(detalleNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO#modificarDetalleNotaPaciente(com.servinte.axioma.orm.DetalleNotaPaciente)
	 */
	@Override
	public DetalleNotaPaciente modificarDetalleNotaPaciente(
			DetalleNotaPaciente detalleNotaPaciente) {
		return delegate.modificarDetalleNotaPaciente(detalleNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO#findById(long)
	 */
	@Override
	public DetalleNotaPaciente findById(long codigo) {
		return delegate.findById(codigo);
	}

}
