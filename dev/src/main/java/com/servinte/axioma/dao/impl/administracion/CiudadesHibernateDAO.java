package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.dao.interfaz.administracion.ICiudadesDAO;
import com.servinte.axioma.dto.administracion.DtoCiudad;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.delegate.administracion.CiudadesDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ICiudadesDAO}.
 * 
 * @author Cristhian Murillo
 * @see CiudadesDelegate.
 */

public class CiudadesHibernateDAO implements ICiudadesDAO{

	
	private CiudadesDelegate delegate = new CiudadesDelegate();

	@Override
	public ArrayList<Ciudades> listarCiudades() {
		return delegate.listarCiudades();
	}

	@Override
	public ArrayList<Ciudades> listarCiudadesPorPais(String codigoPais) {
		return delegate.listarCiudadesPorPais(codigoPais);
	}
	

	@Override
	public ArrayList<DtoCiudades> listarCiudadesDtoPorPais(String codigoPais)
	{
		return delegate.listarCiudadesDtoPorPais(codigoPais);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.ICiudadesDAO#listarAllCiudades()
	 */
	@Override
	public List<DtoCiudad> listarAllCiudades() {
		return delegate.listarAllCiudades();
	}
}
