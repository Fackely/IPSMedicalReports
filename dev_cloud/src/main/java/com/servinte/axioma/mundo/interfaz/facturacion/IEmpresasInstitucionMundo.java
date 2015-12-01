package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.EmpresasInstitucion;

/**
 * Esta clase se encarga de definir la l&oacute;gica de negocio 
 * para las empresas de las instituciones.
 *
 * @author Yennifer Guerrero
 * @since  27/08/2010
 *
 */
public interface IEmpresasInstitucionMundo {
	
	/**
	 * Este m&eacute;todo se encarga de listar todas las empresas-Instituciones
	 * existentes en el sistema.
	 * @return listaEmpresaIns lista de empresas instituciones existentes en el sistema.
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucion ();
	
	
	
	/**
	 * Retorna las Empresas - Institución por la Institución dada.
	 * @author Cristhian Murillo
	 * @return listaEmpresaIns
	 */
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucionPorInstitucion (int institucion);
}
