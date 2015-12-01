/**
 * 
 */
package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionUsuarioEspecificoServicio implements
		INivelAutorizacionUsuarioEspecificoServicio {
	
	INivelAutorizacionUsuarioEspecificoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public NivelAutorizacionUsuarioEspecificoServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionUsuarioEspecificoMundo();
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
		return mundo.buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(id);		
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
		return mundo.eliminarNivelAutorizacionUsuarioEspecifico(id);
	}

}
