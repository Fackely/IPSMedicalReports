package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IInstitucionesDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.orm.Instituciones;


/**
 * L&oacute;gica de Negocio para todo lo relacionado con Instituciones
 * 
 * @author Cristhian Murillo
 * @see IInstitucionesMundo
 */
public class InstitucionesMundo implements IInstitucionesMundo {
	
	
	private IInstitucionesDAO institucionesDAO;
	
	
	public InstitucionesMundo() {
		inicializar();
	}
	
	
	private void inicializar() {
		institucionesDAO = AdministracionFabricaDAO.crearInstitucionesDAO();
	}

	
	@Override
	public Instituciones buscarPorCodigo(int codigo) {
		return institucionesDAO.findById(codigo);
	}
	
	
	public ArrayList<Instituciones> listarInstituciones(){
		return institucionesDAO.listarInstituciones();
	}

}
