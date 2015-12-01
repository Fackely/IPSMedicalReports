/**
 * 
 */
package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadUsuarioEspecifico;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IPrioridadUsuarioEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadUsuarioEspecificoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad PrioridadUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class PrioridadUsuarioEspecificoServicio implements
		IPrioridadUsuarioEspecificoServicio {
	
	IPrioridadUsuarioEspecificoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public PrioridadUsuarioEspecificoServicio(){
		mundo = CapitacionFabricaMundo.crearPrioridadUsuarioEspecificoMundo();
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
		return mundo.buscarPrioridadUsuarioEspecificoPorNivelAutorID(id);
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
		return mundo.eliminarPrioridadUsuarioEspecifico(id);
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
		return mundo.obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(id);
	}

}
