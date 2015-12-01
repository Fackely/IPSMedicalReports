package com.servinte.axioma.servicio.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.NivelAutorServMedic;

/**
 * Esta clase se encarga de definir los métodos de negocio de 
 * la entidad Nivel de Autorización de Servicios y Artículos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public interface INivelAutorizacionServicioArticuloServicio {
	

	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID);

	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro);

	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro);


	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de servicios y artículos
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario);
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios - artículos y sus detalles
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticuloDetallado(DTOBusquedaNivelAutorizacionServicioArticulo registro);

	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de servicios / artículos con sus detalles
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticuloDetallado(
			int nivelAutorizacionID, UsuarioBasico usuarioSesion);

}
