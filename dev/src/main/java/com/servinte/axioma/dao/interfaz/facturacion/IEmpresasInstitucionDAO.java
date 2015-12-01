package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.EmpresasInstitucion;

/**
 * Interface del DAO de Empresas Institución
 */
public interface IEmpresasInstitucionDAO {
	
	
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
