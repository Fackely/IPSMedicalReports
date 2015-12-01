/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad NivelAutorizacionOcupacionMedica
 * @author Angela Aguirre
 *
 */
public interface INivelAutorizacionOcupacionMedicaMundo {

	/**
	 * 
	 * Este método se encarga de consultar los niveles de autorización de
	 * las ocupaciones médicas 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionOcupacionMedica> 
			buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(int id);
	
	/**
	 * 
	 * Este método se encarga de eliminar los niveles de autorización de
	 * usuario específico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionOcupacionMedica(int id);
}
