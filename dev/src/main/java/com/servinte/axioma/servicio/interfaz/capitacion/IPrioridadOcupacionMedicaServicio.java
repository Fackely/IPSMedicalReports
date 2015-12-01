/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * para la entidad PrioridadOcupacionMedica
 * @author Angela Aguirre
 *
 */
public interface IPrioridadOcupacionMedicaServicio {
	
	/**
	 * 
	 * Este m�todo se encarga de consultar las prioridades para los niveles de
	 * autorizaci�n por ocupaci�n m�dica
	 * 
	 * @param int id
	 * @return ArrayList<DTOPrioridadOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTOPrioridadOcupacionMedica> 
			buscarPrioridadOcupacionMedicaPorNivelAutorID(int id);
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar las prioridades para los niveles de
	 * autorizaci�n por ocupaci�n m�dica
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarPrioridadOcupacionMedica(int id);

	
	/**
	 * 
	 * Este m�todo se encarga de consultar las prioridades para los niveles de
	 * autorizaci�n por ocupaci�n m�dica, ordenados para la validacion de autorizaciones 
	 * 
	 * @param int id
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> 
		obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(int id);
}
