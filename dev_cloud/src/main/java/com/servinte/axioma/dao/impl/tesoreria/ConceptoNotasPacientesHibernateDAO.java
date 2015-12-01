package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.orm.ConceptoNotaPaciente;
import com.servinte.axioma.orm.delegate.tesoreria.ConceptoNotasPacientesDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IConceptoNotasPacientesDAO}.
 * 
 * @author diecorqu
 * @see ConceptoNotasPacientesDelegate	
 */
public class ConceptoNotasPacientesHibernateDAO implements
		IConceptoNotasPacientesDAO {

	ConceptoNotasPacientesDelegate delegate;
	
	public ConceptoNotasPacientesHibernateDAO() {
		delegate = new ConceptoNotasPacientesDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO#guardarConceptoNotaPaciente(com.servinte.axioma.orm.ConceptoNotaPaciente)
	 */
	@Override
	public boolean guardarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente) {
		return delegate.guardarConceptoNotaPaciente(conceptoNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO#eliminarConceptoNotaPaciente(com.servinte.axioma.orm.ConceptoNotaPaciente)
	 */
	@Override
	public void eliminarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente) {
		delegate.eliminarConceptoNotaPaciente(conceptoNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO#modificarConceptoNotaPaciente(com.servinte.axioma.orm.ConceptoNotaPaciente)
	 */
	@Override
	public ConceptoNotaPaciente modificarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente) {
		return delegate.modificarConceptoNotaPaciente(conceptoNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO#findById(long)
	 */
	@Override
	public ConceptoNotaPaciente findById(long codigo) {
		return delegate.findById(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO#listarConceptoNotasPacientes()
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes() {
		return delegate.listarConceptoNotasPacientes();
	}

	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes) {
		return delegate.listarConceptoNotasPacientesBusquedaAvanzada(dtoConceptosNotasPacientes);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO#listarConceptoNotasPacientesEstado(boolean)
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(
			boolean estado) {
		return delegate.listarConceptoNotasPacientesEstado(estado);
	}

}
