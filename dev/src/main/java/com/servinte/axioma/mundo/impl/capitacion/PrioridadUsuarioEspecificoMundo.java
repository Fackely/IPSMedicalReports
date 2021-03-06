/**
 * 
 */
package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPrioridadUsuarioEspecifico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadUsuarioEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IPrioridadUsuarioEspecificoMundo;

/**
 * Esta clase se encarga de ejecutar los m?todos de negocio
 * para la entidad PrioridadUsuarioEspecifico 
 * @author Angela Aguirre
 *
 */
public class PrioridadUsuarioEspecificoMundo implements
		IPrioridadUsuarioEspecificoMundo {
	
	IPrioridadUsuarioEspecificoDAO dao;
	
	public PrioridadUsuarioEspecificoMundo(){
		dao = CapitacionFabricaDAO.crearPrioridadUsuarioEspecificoDAO();
	}
	
	/**
	 * 
	 * Este m?todo se encarga de consultar las prioridades para los niveles de
	 * autorizaci?n por usuario espec?fico
	 * 
	 * @param int id
	 * @return ArrayList<DTOPrioridadUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTOPrioridadUsuarioEspecifico> 
			buscarPrioridadUsuarioEspecificoPorNivelAutorID(int id){
		
		return dao.buscarPrioridadUsuarioEspecificoPorNivelAutorID(id);
	}
	
	/**
	 * 
	 * Este m?todo se encarga de eliminar las prioridades para los niveles de
	 * autorizaci?n por usuario espec?fico
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarPrioridadUsuarioEspecifico(int id){
		return dao.eliminarPrioridadUsuarioEspecifico(id);
	}

	/**
	 * 
	 * Este m?todo se encarga de consultar los numeros de prioridades para los niveles de
	 * autorizaci?n por usuario espec?fico, ordenados para la validacion de autorizaciones 
	 * 
	 * @param int id
	 * @return ArrayList<Integer> Lista de Numeros Prioridades
	 */
	public ArrayList<Integer> 
			obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(int id){
		return dao.obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(id);
	}
}
