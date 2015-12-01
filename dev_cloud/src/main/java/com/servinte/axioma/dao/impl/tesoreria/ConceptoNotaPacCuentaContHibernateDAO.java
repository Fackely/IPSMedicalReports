package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO;
import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.orm.ConcNotaPacCuentaCont;
import com.servinte.axioma.orm.delegate.tesoreria.ConceptoNotaPacCuentaContDelegate;

public class ConceptoNotaPacCuentaContHibernateDAO implements
		IConceptoNotaPacCuentaContDAO {

	ConceptoNotaPacCuentaContDelegate delegate;
	
	public ConceptoNotaPacCuentaContHibernateDAO() {
		delegate = new ConceptoNotaPacCuentaContDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO#guardarConceptoNotaPacCuentaCont(com.servinte.axioma.orm.ConcNotaPacCuentaCont)
	 */
	@Override
	public boolean guardarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		return delegate.guardarConceptoNotaPacCuentaCont(conceptoNotaPacCuentaCont);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO#eliminarConceptoNotaPacCuentaCont(com.servinte.axioma.orm.ConcNotaPacCuentaCont)
	 */
	@Override
	public void eliminarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		delegate.eliminarConceptoNotaPacCuentaCont(conceptoNotaPacCuentaCont);

	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO#modificarConceptoNotaPacCuentaCont(com.servinte.axioma.orm.ConcNotaPacCuentaCont)
	 */
	@Override
	public ConcNotaPacCuentaCont modificarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		return delegate.modificarConceptoNotaPacCuentaCont(conceptoNotaPacCuentaCont);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO#findById(long)
	 */
	@Override
	public ConcNotaPacCuentaCont findById(long codigo) {
		return delegate.findById(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO#listarConceptoNotaPacCuentaCont(long)
	 */
	@Override
	public ArrayList<DtoConcNotaPacCuentaCont> listarConceptoNotaPacCuentaCont(
			long codigoConceptoNotas) {
		return delegate.listarConceptoNotaPacCuentaCont(codigoConceptoNotas);
	}

}
