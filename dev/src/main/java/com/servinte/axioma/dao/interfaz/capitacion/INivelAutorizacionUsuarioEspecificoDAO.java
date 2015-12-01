/**
 * 
 */
package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public interface INivelAutorizacionUsuarioEspecificoDAO {
	

	/**
	 * 
	 * Este m�todo se encarga de consultar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionUsuarioEspecifico> 
			buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(int id);
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionUsuarioEspecifico(int id);

}
