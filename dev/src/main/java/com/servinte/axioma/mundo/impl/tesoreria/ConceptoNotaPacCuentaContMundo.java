package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO;
import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo;
import com.servinte.axioma.orm.ConcNotaPacCuentaCont;

public class ConceptoNotaPacCuentaContMundo implements
		IConceptoNotaPacCuentaContMundo {

	IConceptoNotaPacCuentaContDAO dao;
	
	public ConceptoNotaPacCuentaContMundo() {
		dao = TesoreriaFabricaDAO.crearConceptoNotaPacCuentaContDAO();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo#guardarConceptoNotaPacCuentaCont(com.servinte.axioma.orm.ConcNotaPacCuentaCont)
	 */
	@Override
	public boolean guardarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		return dao.guardarConceptoNotaPacCuentaCont(conceptoNotaPacCuentaCont);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo#eliminarConceptoNotaPacCuentaCont(com.servinte.axioma.orm.ConcNotaPacCuentaCont)
	 */
	@Override
	public void eliminarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		dao.eliminarConceptoNotaPacCuentaCont(conceptoNotaPacCuentaCont);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo#modificarConceptoNotaPacCuentaCont(com.servinte.axioma.orm.ConcNotaPacCuentaCont)
	 */
	@Override
	public ConcNotaPacCuentaCont modificarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		return dao.modificarConceptoNotaPacCuentaCont(conceptoNotaPacCuentaCont);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo#findById(long)
	 */
	@Override
	public ConcNotaPacCuentaCont findById(long codigo) {
		return dao.findById(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo#listarConceptoNotaPacCuentaCont(long)
	 */
	@Override
	public ArrayList<DtoConcNotaPacCuentaCont> listarConceptoNotaPacCuentaCont(
			long codigoConceptoNotas) {
		return dao.listarConceptoNotaPacCuentaCont(codigoConceptoNotas);
	}

}
