package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntidadesFinancierasDAO;
import com.servinte.axioma.orm.delegate.tesoreria.EntidadesFinancierasDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IEntidadesFinancierasDAO}.
 * 
 * @author Cristhian Murillo
 * @see EntidadesFinancierasDAO.
 */

public class EntidadesFinancierasHibernateDAO implements IEntidadesFinancierasDAO{

	
	private EntidadesFinancierasDelegate entiFinancierasDelegate = new EntidadesFinancierasDelegate();
	

	@Override
	public List<DtoEntidadesFinancieras> obtenerEntidadesPorInstitucion(int codigoInstitucion, boolean activo){
		
		return entiFinancierasDelegate.obtenerEntidadesPorInstitucion(codigoInstitucion, activo);
	}


	@Override
	public ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancieras(boolean todas) {
		
		return entiFinancierasDelegate.consultarEntidadesFinancieras(todas);
	}
	

}
