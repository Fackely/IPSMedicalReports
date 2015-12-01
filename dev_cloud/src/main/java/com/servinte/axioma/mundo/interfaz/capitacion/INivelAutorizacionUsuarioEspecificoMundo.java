/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public interface INivelAutorizacionUsuarioEspecificoMundo {
	
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
			buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(int id);
	
	/**
	 * 
	 * Este método se encarga de eliminar los niveles de autorización de
	 * usuario específico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionUsuarioEspecifico(int id);

}
