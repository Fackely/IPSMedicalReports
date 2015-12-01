package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.IEmpresasInstitucionDAO;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.delegate.facturacion.EmpresasInstitucionDelegate;

/**
 * Implementaci&oacute;n de la interface {@link IEmpresasInstitucionDAO}.
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public class EmpresasInstitucionHibernateDAO implements IEmpresasInstitucionDAO {
	
	private EmpresasInstitucionDelegate delegate = new EmpresasInstitucionDelegate();
	

	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucion (){
		return delegate.listarEmpresaInstitucion();
	}


	@Override
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucionPorInstitucion(int institucion) {
		return delegate.listarEmpresaInstitucionPorInstitucion(institucion);
	}

}
