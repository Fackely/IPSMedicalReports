package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionServicioMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionServicioServicio;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de agrupaci�n de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionServicioServicio implements
		INivelAutorizacionAgrupacionServicioServicio {
	
	INivelAutorizacionAgrupacionServicioMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionAgrupacionServicioServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionAgrupacionServicioMundo();
	}

	/** 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * de agrupaci�n de servicios registrados en el sistema
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
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n de agrupaci�n de servicios
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
