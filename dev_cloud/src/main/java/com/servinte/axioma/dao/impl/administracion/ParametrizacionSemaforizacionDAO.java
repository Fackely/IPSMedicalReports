package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO;
import com.servinte.axioma.orm.delegate.administracion.ParametrizacionSemaforizacionDelegate;

public class ParametrizacionSemaforizacionDAO implements IParametrizacionSemaforizacionDAO{
	
	
	/**
	 * Instanciacion de Delegate 
	 */
	private ParametrizacionSemaforizacionDelegate parametrizacionSemaforizacionDelegate = new ParametrizacionSemaforizacionDelegate();

	

	/**
	 * @see com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO#consultarParametrizaciones(java.lang.String)
	 */
	public ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> consultarParametrizaciones(String reporte){
		
		return parametrizacionSemaforizacionDelegate.consultarParametrizacionSemaforizaciones(reporte);
		
	}


	
	/**
	 * @see com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO#insertar(com.servinte.axioma.orm.ParametrizacionSemaforizacion)
	 */
	public void insertar(
			com.servinte.axioma.orm.ParametrizacionSemaforizacion objeto) {
		parametrizacionSemaforizacionDelegate.guardarActualizar(objeto);
		
	}



	
	/**
	 * @see com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO#modificar(com.servinte.axioma.orm.ParametrizacionSemaforizacion)
	 */
	public void modificar(
			com.servinte.axioma.orm.ParametrizacionSemaforizacion objeto) {
		parametrizacionSemaforizacionDelegate.guardarActualizar(objeto);
		
	}



	
	/**
	 * @see com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO#eliminar(com.servinte.axioma.orm.ParametrizacionSemaforizacion)
	 */
	public void eliminar(
			com.servinte.axioma.orm.ParametrizacionSemaforizacion objeto) {
		parametrizacionSemaforizacionDelegate.eliminarParametrizacion(objeto);
		
	}



	
	/**
	 * @see com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO#buscarxId(java.lang.Number)
	 */
	public com.servinte.axioma.orm.ParametrizacionSemaforizacion buscarxId(
			Number id) {
		return parametrizacionSemaforizacionDelegate.buscarPorID(id);
	}



	
	
	
}
