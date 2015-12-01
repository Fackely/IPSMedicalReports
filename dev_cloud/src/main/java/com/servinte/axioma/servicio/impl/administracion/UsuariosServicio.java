package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link IUsuariosServicio}
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 *
 */
public class UsuariosServicio implements IUsuariosServicio {

	IUsuariosMundo usuariosMundoImp;
	
	
	public UsuariosServicio() {
		usuariosMundoImp = AdministracionFabricaMundo.crearUsuariosMundo();
	} 
	
	@Override
	public Usuarios buscarPorLogin(String login) {
		
		return usuariosMundoImp.buscarPorLogin(login);
	}

	@Override
	public DtoUsuarioPersona obtenerDtoUsuarioPersona(String loginUsuario) {
		return usuariosMundoImp.obtenerDtoUsuarioPersona(loginUsuario);
	}

	@Override
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosSistemas(
			int institucion, boolean filtrarActivos) {
		return usuariosMundoImp.obtenerUsuariosSistemas(institucion,filtrarActivos);
	}
	
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(int consecutivoCentroAtencion){
		return usuariosMundoImp.obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(consecutivoCentroAtencion);
	}
	
}
