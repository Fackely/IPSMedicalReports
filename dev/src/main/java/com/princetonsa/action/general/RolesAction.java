package com.princetonsa.action.general;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.general.RolesForm;
import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.business.general.RolesFuncionalidadesMundo;
import com.servinte.axioma.orm.Roles;
import com.servinte.axioma.orm.delegate.FuncionalidadesDelegate;
import com.servinte.axioma.orm.delegate.RolesDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IRolesFuncionalidadesServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

public class RolesAction extends Action{
	
	ILocalizacionServicio localizacionServicio = AdministracionFabricaServicio.crearLocalizacionServicio();
	IRolesFuncionalidadesServicio rolesFuncionalidadesServicio = AdministracionFabricaServicio.crearRolesFuncionalidadesServicio();
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		if(form instanceof RolesForm)
		{
			RolesForm forma=(RolesForm)form;
			String estado = forma.getEstado(); 
			
			forma.setMostrarMensaje(new ResultadoBoolean(false));
			
			Log4JManager.info("Estado RolesAction --> "+estado);
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario, request);
			}
			else if(estado.equals("cambiarRol"))
			{
				return accionCambiarRol(mapping,forma,usuario,request);
			}
			else if(estado.equals("adicionarRol"))
			{
				return accionCrearRol(mapping,forma,usuario,request);
			}
			else if( (estado.equals("adicionarFuncionalidadRol")))
			{
				return accionAdicionarFuncinalidadRol(mapping, forma, usuario, request);
			}
			else if(estado.equals("eliminarFuncionalidad"))
			{
				return eliminarFuncionalidads(mapping, forma, usuario, request);
			}
			
		}
		
		return null;
	}

	
	
	private ActionForward accionCrearRol(ActionMapping mapping,
			RolesForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		RolesDelegate dao=new RolesDelegate();
		Roles rol;
		rol=dao.findById(forma.getNombreRolNuevo());
		if(rol==null)
		{
			rol=new Roles();
			rol.setCodigo(UtilidadBD.obtenerSiguienteValorSecuencia("administracion.seq_roles"));
			rol.setNombreRol(forma.getNombreRolNuevo());
			dao.persist(rol);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Rol adicionado correctamente."));
			forma.setNombreRol(forma.getNombreRolNuevo());
			forma.setRoles(RolesFuncionalidadesMundo.mostrarRoles());
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"El Rol ya existe"));
			forma.setNombreRol(forma.getNombreRolNuevo());
		}
		UtilidadTransaccion.getTransaccion().commit();
		return accionCambiarRol(mapping, forma, usuario, request);
	}



	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionAdicionarFuncinalidadRol(ActionMapping mapping,RolesForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		

		if(rolesFuncionalidadesServicio.adicionarRolFuncionalidad(forma.getNombreRol(), forma.getCodigoFunc()))
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Funcionalidad Adicionada Correctamente."));
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo adicionar la funcionalidad al Rol."));
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return accionCambiarRol(mapping, forma, usuario, request);
	}



	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward eliminarFuncionalidads(ActionMapping mapping,
			RolesForm forma, UsuarioBasico usuario, HttpServletRequest request) {
		
		UtilidadTransaccion.getTransaccion().begin();
		

		if(rolesFuncionalidadesServicio.eliminarRolFuncionalidad(forma.getRolesFuncionalidades().get(forma.getRegEliminar())))
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Funcionalidad Eliminada Correctamente."));
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo eliminar la funcionalidad al Rol."));
		}
		UtilidadTransaccion.getTransaccion().commit();
		
		return accionCambiarRol(mapping, forma, usuario, request);
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionCambiarRol(ActionMapping mapping,
			RolesForm forma, UsuarioBasico usuario, HttpServletRequest request) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.setRolesFuncionalidades(rolesFuncionalidadesServicio.obtenerFuncionalidadesRol(forma.getNombreRol()));
		
		UtilidadTransaccion.getTransaccion().commit();
		
		forma.setCodigoFunc(ConstantesBD.codigoNuncaValido);
		
		return mapping.findForward("principal");
	}


	/**
	 * Buscar Funcionalidades
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionBuscarfuncionalidades(ActionMapping mapping,
			RolesForm forma, UsuarioBasico usuario, HttpServletRequest request) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		//DtoModuloRolFuncionalidad dtoModuloRolFuncionalidad;
		
		
		// FIXME llamar al delegate de funcionalidades no de roles funcionalidades
		ArrayList<DtoModuloRolFuncionalidad> listaDtoModuloRolFuncionalidad 
					= (ArrayList<DtoModuloRolFuncionalidad>)rolesFuncionalidadesServicio.obtenerModuloRolFuncionalidadPorRol(forma.getNombreRol());
		
		
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}



	/**
	 * Empezar
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, RolesForm forma,
			UsuarioBasico usuario, HttpServletRequest request) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.reset();
		
		forma.setRoles(RolesFuncionalidadesMundo.mostrarRoles());
	
		FuncionalidadesDelegate daoFunc=new FuncionalidadesDelegate();
		forma.setFuncionalidades(daoFunc.listarTodasParametrizables());
		
		UtilidadTransaccion.getTransaccion().commit();
		
		
		return mapping.findForward("principal");
	}
	
	
	
}
