package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.orm.Usuarios;


/**
 * L&oacute;gica de Negocio para todo lo relacionado con los Usuarios
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IUsuariosMundo
 */

public class UsuariosMundo implements IUsuariosMundo {

	private IUsuarioDAO	usuariosDAO;
	
	public UsuariosMundo() {
		inicializar();
	}

	private void inicializar() {
		usuariosDAO		= AdministracionFabricaDAO.crearUsuarioDAO();
	}
	
	
	@Override
	public Usuarios buscarPorLogin(String login) 
	{
		//UtilidadTransaccion.getTransaccion().begin();
		return usuariosDAO.buscarPorLogin(login);
	}

	
	@Override
	public List<DtoUsuarioPersona> obtenerUsuariosActivosDiferenteDe(int codInstitucion,String loginUsuario, boolean incluirInactivos) 
	{
		List<DtoUsuarioPersona> lt = usuariosDAO.obtenerUsuariosActivosDiferenteDe(codInstitucion, loginUsuario, incluirInactivos);
		return lt;
	}

	
	@Override
	public DtoUsuarioPersona obtenerDtoUsuarioPersona(String loginUsuario) {
		return usuariosDAO.obtenerDtoUsuarioPersona(loginUsuario);
	}

	@Override
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosSistemas(
			int institucion, boolean filtrarActivos) {
		return usuariosDAO.obtenerUsuariosSistemas(institucion,filtrarActivos);
	}
	
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(int consecutivoCentroAtencion){
		return usuariosDAO.obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(consecutivoCentroAtencion);
	}
	
	
	
}
