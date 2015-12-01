package com.servinte.axioma.dao.impl.administracion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.administracion.IBarriosDAO;
import com.servinte.axioma.dto.administracion.DtoBarrio;
import com.servinte.axioma.orm.Barrios;
import com.servinte.axioma.orm.delegate.administracion.BarriosDelegate;

public class BarriosHibernateDAO implements IBarriosDAO{

	private BarriosDelegate delegate = new BarriosDelegate();
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.IBarriosDAO#findByCodigoBarrio(java.lang.String)
	 */
	@Override
	public Barrios findByCodigoBarrio(String codigoBarrio, String codigoCiudad, String codigoDepartamento, String codigoPais) {
		return delegate.findByCodigoBarrio(codigoBarrio, codigoCiudad, codigoDepartamento, codigoPais);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.IBarriosDAO#listarAllBarrios()
	 */
	@Override
	public List<DtoBarrio> listarAllBarrios() {
		return delegate.listarAllBarrios();
	}

	
}
