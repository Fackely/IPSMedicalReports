/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadUsuarioEspecifico;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadUsuarioEspecificoDAO;
import com.servinte.axioma.orm.delegate.capitacion.PrioridadUsuarioEspecificoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class PrioridadUsuarioEspecificoHibernateDAO implements
		IPrioridadUsuarioEspecificoDAO {
	
	PrioridadUsuarioEspecificoDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public PrioridadUsuarioEspecificoHibernateDAO(){
		delegate = new PrioridadUsuarioEspecificoDelegate();
	}
	
	/**
	 * 
	 * Este método se encarga de consultar las prioridades para los niveles de
	 * autorización por usuario específico
	 * 
	 * @param int id
	 * @return ArrayList<DTOPrioridadUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTOPrioridadUsuarioEspecifico> 
			buscarPrioridadUsuarioEspecificoPorNivelAutorID(int id){
		return delegate.buscarPrioridadUsuarioEspecificoPorNivelAutorID(id);
	}
	
	
	/**
	 * 
	 * Este método se encarga de eliminar las prioridades para los niveles de
	 * autorización por usuario específico
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarPrioridadUsuarioEspecifico(int id){
		return delegate.eliminarPrioridadUsuarioEspecifico(id);
	}
	
	
	/**
	 * 
	 * Este método se encarga de consultar los numeros de prioridades para los niveles de
	 * autorización por usuario específico, ordenados para la validacion de autorizaciones 
	 * 
	 * @param int id
	 * @return ArrayList<Integer> Lista de Numeros Prioridades
	 */
	public ArrayList<Integer> 
			obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(int id){
		return delegate.obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(id);
	}
	

}
