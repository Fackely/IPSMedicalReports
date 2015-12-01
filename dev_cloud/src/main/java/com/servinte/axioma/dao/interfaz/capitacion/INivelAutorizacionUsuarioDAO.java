/**
 * 
 */
package com.servinte.axioma.dao.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.servinte.axioma.orm.NivelAutorUsuario;

/**
 * Esta clase se encarga de definir los métodos de negocio de 
 * la entidad Nivel de Autorización de Usuario
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public interface INivelAutorizacionUsuarioDAO {
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return DTONivelAutorizacionUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionUsuario buscarNivelAutorizacionUsuario(int nivelAutorizacionID);


	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionUsuario(NivelAutorUsuario registro);



	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionUsuario(NivelAutorUsuario registro);


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
	public boolean eliminarNivelAutorizacionUsuario(int idNivelAutorizacionUsuario);
	
	/** 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema por su ID
	 * 
	 * @param int id
	 * @return NivelAutorUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public NivelAutorUsuario buscarNivelAutorizacionUsuarioPorID(int id);

}
