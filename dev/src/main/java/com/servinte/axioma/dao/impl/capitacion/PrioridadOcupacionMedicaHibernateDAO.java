/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadOcupacionMedicaDAO;
import com.servinte.axioma.orm.delegate.capitacion.PrioridadOcupacionMedicaDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadOcupacionMedica
 * @author Angela Aguirre
 *
 */
public class PrioridadOcupacionMedicaHibernateDAO implements
		IPrioridadOcupacionMedicaDAO {
	
	PrioridadOcupacionMedicaDelegate delegate;
	
	public PrioridadOcupacionMedicaHibernateDAO(){
		delegate = new PrioridadOcupacionMedicaDelegate();
	}
	
	/**
	 * 
	 * Este método se encarga de consultar las prioridades para los niveles de
	 * autorización por ocupación médica
	 * 
	 * @param int id
	 * @return ArrayList<DTOPrioridadOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTOPrioridadOcupacionMedica> 
			buscarPrioridadOcupacionMedicaPorNivelAutorID(int id){
		return delegate.buscarPrioridadOcupacionMedicaPorNivelAutorID(id);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar las prioridades para los niveles de
	 * autorización por ocupación médica
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarPrioridadOcupacionMedica(int id){
		return delegate.eliminarPrioridadOcupacionMedica(id);
	}
	
	
	/**
	 * 
	 * Este método se encarga de consultar las prioridades para los niveles de
	 * autorización por ocupación médica, ordenados para la validacion de autorizaciones 
	 * 
	 * @param int id
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> 
		obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(int id){
		return delegate.obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(id);
	}

}
