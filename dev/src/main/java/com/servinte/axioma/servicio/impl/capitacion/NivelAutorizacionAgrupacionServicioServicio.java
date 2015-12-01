package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionServicioMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionServicioServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de agrupación de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionServicioServicio implements
		INivelAutorizacionAgrupacionServicioServicio {
	
	INivelAutorizacionAgrupacionServicioMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionAgrupacionServicioServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionAgrupacionServicioMundo();
	}

	/** 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de agrupación de servicios registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> buscarNivelAutorizacionServicioArticulo(
			int nivelAutorizacionID){			
		return mundo.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
	}

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de agrupación de servicios
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(
			int idNivelAutorizacionUsuario) {
		return mundo.eliminarNivelAutorizacionServicioArticulo(idNivelAutorizacionUsuario);
	}

}
