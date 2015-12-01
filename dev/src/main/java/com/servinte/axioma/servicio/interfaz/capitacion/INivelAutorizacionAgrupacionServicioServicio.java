package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;

/**
 * Esta clase se encarga de definir los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de agrupaci�n de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public interface INivelAutorizacionAgrupacionServicioServicio {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * de agrupaci�n de servicios registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID);
	
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
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario);

}
