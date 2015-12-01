package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.EmpresasInstitucionHome;

/**
 *Esta clase se encarga de ejecutar los procesos
 * de negocio de la entidad EmpresasInstitucion
 *
 * @author Yennifer Guerrero
 * @since  27/08/2010
 *
 */
public class EmpresasInstitucionDelegate extends EmpresasInstitucionHome {
	
	/**
	 * Este m&eacute;todo se encarga de listar todas las empresas-Instituciones
	 * existentes en el sistema.
	 * @return listaEmpresaIns lista de empresas instituciones existentes en el sistema.
	 *
	 * @author Yennifer Guerrero
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucion (){
		
		ArrayList<EmpresasInstitucion> listaEmpresaIns = (ArrayList<EmpresasInstitucion>) sessionFactory.getCurrentSession()
			.createCriteria(EmpresasInstitucion.class)
			.addOrder(Property.forName("razonSocial").asc())
			.list();
		
		for (EmpresasInstitucion empresas : listaEmpresaIns) {
			empresas.getRazonSocial();
		}
		
		return listaEmpresaIns;
	}
	
	
	
	
	/**
	 * Retorna las Empresas - Institución por la Institución dada.
	 * @author Cristhian Murillo
	 * @return listaEmpresaIns
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucionPorInstitucion (int institucion)
	{
		
		ArrayList<EmpresasInstitucion> listaEmpresaIns = (ArrayList<EmpresasInstitucion>) sessionFactory.getCurrentSession()
			.createCriteria(EmpresasInstitucion.class, "emp_insti")
			.createAlias("emp_insti.instituciones", "instituciones")
			.add(Restrictions.eq("instituciones.codigo", institucion))
			.addOrder(Property.forName("razonSocial").asc())
		.list();
		
		
		for (EmpresasInstitucion empresas : listaEmpresaIns) {
			empresas.getRazonSocial();
		}
		
		return listaEmpresaIns;
	}
	

}
