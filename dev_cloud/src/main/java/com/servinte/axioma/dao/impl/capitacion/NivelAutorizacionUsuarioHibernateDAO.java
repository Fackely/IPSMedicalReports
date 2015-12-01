/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioDAO;
import com.servinte.axioma.orm.NivelAutorUsuario;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionUsuarioDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionUsuarioHibernateDAO implements
		INivelAutorizacionUsuarioDAO {
	
	NivelAutorizacionUsuarioDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public NivelAutorizacionUsuarioHibernateDAO(){
		delegate = new NivelAutorizacionUsuarioDelegate();
	}

		
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
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
	 * Este Método se encarga de guardar los niveles de autorización 
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
	 * Este Método se encarga de actualizar los niveles de autorización
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
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de usuario
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
	 * Este Método se encarga de consultar los niveles de autorización
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
