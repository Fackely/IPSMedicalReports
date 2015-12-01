/**
 * 
 */
package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionOcupacionMedicaDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionOcupacionMedicaMundo;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad NivelAutorizacionOcupacionMedica
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionOcupacionMedicaMundo implements
		INivelAutorizacionOcupacionMedicaMundo {
	
	INivelAutorizacionOcupacionMedicaDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public NivelAutorizacionOcupacionMedicaMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionOcupacionMedicaDAO();
	}

	/**
	 * 
	 * Este método se encarga de consultar los niveles de autorización de
	 * las ocupaciones médicas 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	@Override
	public ArrayList<DTONivelAutorizacionOcupacionMedica> 
			buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(int id){
		return dao.buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(id);
		
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
	@Override
	public boolean eliminarNivelAutorizacionOcupacionMedica(int id){
		return dao.eliminarNivelAutorizacionOcupacionMedica(id);
	}

}
