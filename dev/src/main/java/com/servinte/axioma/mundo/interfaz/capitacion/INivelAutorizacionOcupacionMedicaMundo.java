/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;

/**
 * Esta clase se encarga de definir los m?todos de negocio
 * para la entidad NivelAutorizacionOcupacionMedica
 * @author Angela Aguirre
 *
 */
public interface INivelAutorizacionOcupacionMedicaMundo {

	/**
	 * 
	 * Este m?todo se encarga de consultar los niveles de autorizaci?n de
	 * las ocupaciones m?dicas 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionOcupacionMedica> 
			buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(int id);
	
	/**
	 * 
	 * Este m?todo se encarga de eliminar los niveles de autorizaci?n de
	 * usuario espec?fico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionOcupacionMedica(int id);
}
