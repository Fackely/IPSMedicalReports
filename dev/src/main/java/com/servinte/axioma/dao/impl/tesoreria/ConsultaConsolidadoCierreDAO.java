package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.tesoreria.IConsultaConsolidadoCierreDAO;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;
import com.servinte.axioma.orm.delegate.facturacion.EmpresasInstitucionDelegate;

public class  ConsultaConsolidadoCierreDAO implements IConsultaConsolidadoCierreDAO {
	
	/**
	 * Delegate de empresas institucion
	 */
	EmpresasInstitucionDelegate empresasInstitucionDelegate = new EmpresasInstitucionDelegate();
	
	/**
	 * Centro de atencion delegate
	 */
	CentroAtencionDelegate centroAtencionDelegate = new CentroAtencionDelegate();
	
	
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConsultaConsolidadoCierreDAO#consultarEmpresaInstitucionXinstitucion(java.lang.Integer)
	 */
	public ArrayList<EmpresasInstitucion> consultarEmpresaInstitucionXinstitucion(Integer institucion){
		return empresasInstitucionDelegate.listarEmpresaInstitucionPorInstitucion(institucion);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IConsultaConsolidadoCierreDAO#consultarCentrosAtencion()
	 */
	public ArrayList<CentroAtencion> consultarCentrosAtencion(){
			return centroAtencionDelegate.consultarTodosCentrosAtencion();
	}
	

	
	

	
}
