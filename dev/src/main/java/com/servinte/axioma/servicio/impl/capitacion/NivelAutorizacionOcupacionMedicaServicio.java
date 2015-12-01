/**
 * 
 */
package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionOcupacionMedicaMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionOcupacionMedicaServicio;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionOcupacionMedicaServicio implements
		INivelAutorizacionOcupacionMedicaServicio {
	
	INivelAutorizacionOcupacionMedicaMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 */
	public NivelAutorizacionOcupacionMedicaServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionOcupacionMedicaMundo();
	}

	/**
	 * 
	 * Este m�todo se encarga de consultar los niveles de autorizaci�n de
	 * las ocupaciones m�dicas 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	@Override
	public ArrayList<DTONivelAutorizacionOcupacionMedica> 
			buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(int id){
		return mundo.buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(id);
		
	}
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	@Override
	public boolean eliminarNivelAutorizacionOcupacionMedica(int id){
		return mundo.eliminarNivelAutorizacionOcupacionMedica(id);
	}

}
