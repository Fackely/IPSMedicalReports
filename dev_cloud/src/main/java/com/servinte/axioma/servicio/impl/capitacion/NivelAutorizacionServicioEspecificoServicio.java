package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionServicioEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicioEspecificoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de servicios específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioEspecificoServicio implements
		INivelAutorizacionServicioEspecificoServicio {
	
	INivelAutorizacionServicioEspecificoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicioEspecificoServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionServicioEspecificoMundo();

	}
	
	/**
	 * 
	 * Este método se encarga de consultar todos los
	 * niveles de autorización por servicios específicos 
	 * a través del id del nivel de autorización de serivicios -
	 * artículos relacionado
	 * 
	 * @param int id
	 * @return ArrayList<DTOBusquedaNivelAutorServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaNivelAutorServicioEspecifico> buscarNivelAutorizacionServicios(int id,int codigoTarifario){
		return mundo.buscarNivelAutorizacionServicios(id, codigoTarifario);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de servicio específico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id){
		
		return mundo.eliminarServicioEspecifico(id);
	}

}
