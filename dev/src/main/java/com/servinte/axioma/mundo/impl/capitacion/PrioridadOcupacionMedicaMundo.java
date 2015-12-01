/**
 * 
 */
package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadOcupacionMedicaDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IPrioridadOcupacionMedicaMundo;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadOcupacionMedica
 * @author Angela Aguirre
 *
 */
public class PrioridadOcupacionMedicaMundo implements
		IPrioridadOcupacionMedicaMundo {
	
	IPrioridadOcupacionMedicaDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public PrioridadOcupacionMedicaMundo(){
		dao = CapitacionFabricaDAO.crearPrioridadOcupacionMedicaDAO();
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
		return dao.buscarPrioridadOcupacionMedicaPorNivelAutorID(id);
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
		return dao.eliminarPrioridadOcupacionMedica(id);
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
		return dao.obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(id);
	}

}
