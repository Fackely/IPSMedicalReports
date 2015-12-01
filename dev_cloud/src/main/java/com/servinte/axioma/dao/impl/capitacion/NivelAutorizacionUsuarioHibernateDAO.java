/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioDAO;
import com.servinte.axioma.orm.NivelAutorUsuario;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionUsuarioDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionUsuarioHibernateDAO implements
		INivelAutorizacionUsuarioDAO {
	
	NivelAutorizacionUsuarioDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 */
	public NivelAutorizacionUsuarioHibernateDAO(){
		delegate = new NivelAutorizacionUsuarioDelegate();
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
		return delegate.buscarNivelAutorizacionUsuario(nivelAutorizacionID);
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
		return delegate.guardarNivelAutorizacionUsuario(registro);
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
		return delegate.actualizarNivelAutorizacionUsuario(registro);
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
		return delegate.eliminarNivelAutorizacionUsuario(idNivelAutorizacionUsuario);
	}
	
	/** 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema por su ID
	 * 
	 * @param int id
	 * @return NivelAutorUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public NivelAutorUsuario buscarNivelAutorizacionUsuarioPorID(int id){
		return delegate.findById(id);
	}

}
