package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEmpresasInstitucionDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IEmpresasInstitucionMundo;
import com.servinte.axioma.orm.EmpresasInstitucion;

/**
 * Implementaci&oacute;n de la interface {@link IEmpresasInstitucionMundo}.
 *
 * @author Yennifer Guerrero
 * @since  27/08/2010
 *
 */
public class EmpresasInstitucionMundo implements IEmpresasInstitucionMundo {
	
	private IEmpresasInstitucionDAO dao;
	
	/**
	 * M&eacute;todo constructor de la clase EmpresasInstitucionMundo
	 * 
	 * @author Yennifer Guerrero
	 */
	public EmpresasInstitucionMundo() {
		dao = FacturacionFabricaDAO.crearEmpresasInstitucionDAO();
	}
	
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucion (){
		return dao.listarEmpresaInstitucion();
	}

	@Override
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucionPorInstitucion(int institucion) {
		return dao.listarEmpresaInstitucionPorInstitucion(institucion);
	}
}
