package com.servinte.axioma.dao.impl.administracion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.administracion.ILocalidadesDAO;
import com.servinte.axioma.dto.administracion.DtoLocalidad;
import com.servinte.axioma.orm.delegate.administracion.LocalidadesDelegate;

public class LocalidadesHibernateDAO implements ILocalidadesDAO{

	private LocalidadesDelegate delegate = new LocalidadesDelegate();
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.ILocalidadesDAO#listarAllLocalidades()
	 */
	@Override
	public List<DtoLocalidad> listarAllLocalidades() {
		return delegate.listarAllLocalidades();
	}

}
