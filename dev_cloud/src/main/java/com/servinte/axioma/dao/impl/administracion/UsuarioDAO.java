/**
 * 
 */
package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.dao.excepcion.DAOExcepcion;
import com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IUsuarioDAO}.
 * 
 * @author Fernando Ocampo
 * @see com.servinte.axioma.orm.delegate.UsuariosDelegate
 */
public class UsuarioDAO implements IUsuarioDAO{

	private UsuariosDelegate usuarioDAOImpl;

	public UsuarioDAO() {
		usuarioDAOImpl = new UsuariosDelegate();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.usuario.IUsuarioDAO#buscarPorLoginYContrasena(com.princetonsa.dto.usuario.UsuarioDTO)
	 */
	@Override
	public Usuarios buscarPorLoginYContrasena(UsuarioDTO usuario)
			throws DAOExcepcion {
		Usuarios usuarioentidad = null;
		try {
			usuarioentidad = usuarioDAOImpl.validarUsuario(usuario.getUsuario(), usuario
				.getPassword());
		} catch(Throwable ex) {
			String strMensaje = "Ocurrio un error en el momento " +
				"de validar el usuario.";
			Log4JManager.error(strMensaje, ex);
			throw new DAOExcepcion(strMensaje,ex);
		}
		return usuarioentidad;
	}

	
	@Override
	public Usuarios buscarPorLogin(String login) {
		
		return usuarioDAOImpl.findById(login);
	}
	
	
	
	@Override
	public List<DtoUsuarioPersona> obtenerUsuariosActivosDiferenteDe(int codInstitucion, String loginUsuario, boolean incluirInactivos)
	{
		return usuarioDAOImpl.obtenerUsuariosActivosDiferenteDe(codInstitucion, loginUsuario, incluirInactivos);
	}
	
	
	@Override
	public DtoUsuarioPersona obtenerDtoUsuarioPersona(String loginUsuario)
	{
		return usuarioDAOImpl.obtenerDtoUsuarioPersona(loginUsuario);
	}

	@Override
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosSistemas(
			int institucion, boolean filtrarActivos) {
		return usuarioDAOImpl.obtenerUsuariosSistemas(institucion,filtrarActivos);
	}
	
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(int consecutivoCentroAtencion){
		return usuarioDAOImpl.obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(consecutivoCentroAtencion);
	}
}
