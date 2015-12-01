package com.servinte.axioma.dao.interfaz.administracion;



import java.util.ArrayList;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;
import com.servinte.axioma.orm.delegate.administracion.ParametrizacionSemaforizacionDelegate;


public interface IParametrizacionSemaforizacionDAO extends IBaseDAO<ParametrizacionSemaforizacion>{
	

	

	/**
	 * Consulta a la Base de datos los registros
	 * @param reporte
	 * @return Lista de parametrizaciones en el sistema 
	 */
	public ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> consultarParametrizaciones(String reporte);


	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#insertar(java.lang.Object)
	 */
	public void insertar(
			com.servinte.axioma.orm.ParametrizacionSemaforizacion objeto);



	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#modificar(java.lang.Object)
	 */
	public void modificar(
			com.servinte.axioma.orm.ParametrizacionSemaforizacion objeto);



	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#eliminar(java.lang.Object)
	 */
	public void eliminar(
			com.servinte.axioma.orm.ParametrizacionSemaforizacion objeto);



	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#buscarxId(java.lang.Number)
	 */
	public com.servinte.axioma.orm.ParametrizacionSemaforizacion buscarxId(
			Number id) ;
	
}
