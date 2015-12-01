/**
 * 
 */
package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoMundo;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionUsuarioEspecificoMundo implements
INivelAutorizacionUsuarioEspecificoMundo {
	
	INivelAutorizacionUsuarioEspecificoDAO dao;
	
	public NivelAutorizacionUsuarioEspecificoMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionUsuarioEspecificoDAO();
	}
	
	/**
	 * 
	 * Este método se encarga de consultar los niveles de autorización de
	 * usuario específico 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionUsuarioEspecifico> 
			buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(int id){
		return dao.buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(id);		
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar los niveles de autorización de
	 * usuario específico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionUsuarioEspecifico(int id){
		return dao.eliminarNivelAutorizacionUsuarioEspecifico(id);
	}

}
