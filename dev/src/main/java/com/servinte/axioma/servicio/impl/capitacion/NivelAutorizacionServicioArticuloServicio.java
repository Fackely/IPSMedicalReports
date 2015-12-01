package com.servinte.axioma.servicio.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionServicioArticuloMundo;
import com.servinte.axioma.orm.NivelAutorServMedic;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicioArticuloServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioArticuloServicio implements
		INivelAutorizacionServicioArticuloServicio {
	
	INivelAutorizacionServicioArticuloMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicioArticuloServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionServicioArticuloMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID){
		return mundo.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
	}

	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		return mundo.guardarNivelAutorizacionServicioArticulo(registro);
	}
	

	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		return mundo.actualizarNivelAutorizacionServicioArticulo(registro);
	}


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
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario){
		return mundo.eliminarNivelAutorizacionServicioArticulo(idNivelAutorizacionUsuario);
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios - artículos y sus detalles
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticuloDetallado(DTOBusquedaNivelAutorizacionServicioArticulo registro){
		return mundo.guardarNivelAutorizacionServicioArticuloDetallado(registro);
	}
	
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
			int nivelAutorizacionID, UsuarioBasico usuarioSesion){
		return mundo.buscarNivelAutorizacionServicioArticuloDetallado(nivelAutorizacionID, usuarioSesion);
	}

}
