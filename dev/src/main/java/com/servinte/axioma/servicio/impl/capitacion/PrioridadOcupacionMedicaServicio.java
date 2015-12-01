/**
 * 
 */
package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IPrioridadOcupacionMedicaMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadOcupacionMedicaServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadOcupacionMedicae 
 * @author Angela Aguirre
 *
 */
public class PrioridadOcupacionMedicaServicio implements
		IPrioridadOcupacionMedicaServicio {
	
	IPrioridadOcupacionMedicaMundo mundo;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public PrioridadOcupacionMedicaServicio(){
		mundo = CapitacionFabricaMundo.crearPrioridadOcupacionMedicaMundo();
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
		return mundo.buscarPrioridadOcupacionMedicaPorNivelAutorID(id);
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
		return mundo.eliminarPrioridadOcupacionMedica(id);
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
		return mundo.obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(id);
	}

}
