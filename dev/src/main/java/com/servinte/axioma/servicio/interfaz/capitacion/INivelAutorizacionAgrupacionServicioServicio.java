package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;

/**
 * Esta clase se encarga de definir los métodos de negocio de 
 * la entidad Nivel de Autorización de agrupación de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public interface INivelAutorizacionAgrupacionServicioServicio {
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de agrupación de servicios registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID);
	
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
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario);

}
