package com.servinte.axioma.servicio.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.NivelAutorServMedic;

/**
 * Esta clase se encarga de definir los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de Servicios y Art�culos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public interface INivelAutorizacionServicioArticuloServicio {
	

	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * de servicios y art�culos registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID);

	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n 
	 * de servicios y art�culos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro);

	/**
	 * 
	 * Este M�todo se encarga de actualizar los niveles de autorizaci�n
	 * de servicios y art�culos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro);


	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n de servicios y art�culos
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario);
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n 
	 * de servicios - art�culos y sus detalles
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticuloDetallado(DTOBusquedaNivelAutorizacionServicioArticulo registro);

	
	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * de servicios / art�culos con sus detalles
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticuloDetallado(
			int nivelAutorizacionID, UsuarioBasico usuarioSesion);

}
