/**
 * 
 */
package com.servinte.axioma.servicio.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionUsuarioMundo;
import com.servinte.axioma.orm.NivelAutorUsuario;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionUsuarioServicio;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionUsuarioServicio implements
		INivelAutorizacionUsuarioServicio {
	
	INivelAutorizacionUsuarioMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 */
	public NivelAutorizacionUsuarioServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionUsuarioMundo();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return DTONivelAutorizacionUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionUsuario buscarNivelAutorizacionUsuario(int nivelAutorizacionID){
		return mundo.buscarNivelAutorizacionUsuario(nivelAutorizacionID);
	}


	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n 
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionUsuario(NivelAutorUsuario registro){
		return mundo.guardarNivelAutorizacionUsuario(registro);
	}



	/**
	 * 
	 * Este M�todo se encarga de actualizar los niveles de autorizaci�n
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionUsuario(NivelAutorUsuario registro){
		return mundo.actualizarNivelAutorizacionUsuario(registro);
	}


	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n de usuario
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionUsuario(int idNivelAutorizacionUsuario){
		return mundo.eliminarNivelAutorizacionUsuario(idNivelAutorizacionUsuario);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * por usuario y sus detalles
	 * 
	 * @return DTONivelAutorizacionUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionUsuario buscarNivelAutorizacionUsuarioDetallado(int nivelAutorizacionID){
		return mundo.buscarNivelAutorizacionUsuarioDetallado(nivelAutorizacionID);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n 
	 * de usuario y sus detalles
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionUsuarioDetallado(DTOBusquedaNivelAutorizacionUsuario dtoNivelAutUsuario){
		return mundo.guardarNivelAutorizacionUsuarioDetallado(dtoNivelAutUsuario);
	}

}
