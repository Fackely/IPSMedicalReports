package com.princetonsa.action.administracion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadBD;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.actionform.administracion.EnvioEmailAutomaticoForm;
import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.EnvioEmailAutomatico;
import com.princetonsa.sort.odontologia.SortGenerico;

public class EnvioEmailAutomaticoAction extends Action{
	
	
	Logger logger =Logger.getLogger(EnvioEmailAutomaticoAction.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			
		
		throws Exception {
			if (response == null);
			if (form instanceof EnvioEmailAutomaticoForm) 
			{
				

				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				EnvioEmailAutomaticoForm forma = (EnvioEmailAutomaticoForm) form;
				 
				
				String estado = forma.getEstado();
				
				logger.warn("************ estado '' "+estado);
			

				if (estado == null) 
				{
					return mapping.findForward("paginaError");
				}
				
				else if (estado.equals("empezar")) 
				{
					return this.accionEmpezar(forma, usuario, request, mapping, false);
				}
				
				
				else if (estado.equals("nuevo")) 
				{
					return accionNuevo(forma,mapping,usuario);
				}
				
				
				else if (estado.equals("guardar")) 
				{
					return this.accionGuarda(forma, request, mapping, usuario);
				}
				
				else if (estado.equals("eliminar")) 
				{
					
				return this.accionEliminar(forma, request, mapping);
				}
				else if(estado.equals("abrirContenidoCancelacionAgenda"))
				{
					return mapping.findForward("contenido");
				}
				
				
				else if(estado.equals("abrirContenidoAperturaCaja"))
				{
					return mapping.findForward("contenidoAperturaCaja");
				}
				
				else if(estado.equals("abrirContenidoCierreCaja"))
				{
					return mapping.findForward("contenidoCierreCaja");
				}
				else if (forma.getEstado().equals("ordenar"))
				{
				  return this.accionOrdenar(mapping, forma, usuario);
				}
				
				
				else {
					forma.reset();
					request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
					return mapping.findForward("paginaError");
				}
			}
			return null;
	}




// Metodo para ordenar los campos del DTO con los link
	private ActionForward accionOrdenar(ActionMapping mapping,
			EnvioEmailAutomaticoForm forma, UsuarioBasico usuario) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaDtoEnvioMail() ,sortG);
		return mapping.findForward("principal");
	}



// Metodo para eliminar un registro especifico por el codigo_pk

	private ActionForward accionEliminar(EnvioEmailAutomaticoForm forma,
			HttpServletRequest request, ActionMapping mapping) {
		
		

      forma.getListaDtoEnvioMail().get(forma.getIndicador1()).setEliminado(true);
	  return mapping.findForward("principal");
		
	}



//Metodo que muestra la pagina principal y trae los datos de la BD

	private ActionForward accionEmpezar(EnvioEmailAutomaticoForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, boolean mostrarExito) {
		
		forma.reset();
		
		forma.setExito(mostrarExito);
		
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoEnvioEmailAutomatico> listaDtoEnvioAuto=EnvioEmailAutomatico.listar(con, usuario.getCodigoInstitucionInt());
		if(listaDtoEnvioAuto==null)
		{
			UtilidadBD.abortarTransaccion(con);
			ActionErrors errores=new ActionErrors();
			errores.add("problema en base de datos", new ActionMessage("errors.problemasBd"));
			saveErrors(request, errores);
			return mapping.findForward("principal");
		}
		
		forma.setListaDtoEnvioMail(listaDtoEnvioAuto);
		
		
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			logger.error("Error cerrando la conexión", e);
			e.printStackTrace();
		}

		return accionCosultarUsuarios(forma,mapping,usuario);
	}


	private ActionForward accionGuarda(EnvioEmailAutomaticoForm forma, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario) {
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		ArrayList<DtoEnvioEmailAutomatico> listaDtoEnvioAuto=forma.getListaDtoEnvioMail();
		for (DtoEnvioEmailAutomatico dtoEnvioEmailAutomatico : listaDtoEnvioAuto) 
		{
			if(!dtoEnvioEmailAutomatico.isEliminado())
			{
				if(dtoEnvioEmailAutomatico.isExisteBD())
				{
					//MODIFICAR
					if(!dtoEnvioEmailAutomatico.getFuncionalidad().equals(dtoEnvioEmailAutomatico.getFuncionalidadAnterior())||!dtoEnvioEmailAutomatico.getUsuario().equals(dtoEnvioEmailAutomatico.getUsuarioAnterior()))
					{
						EnvioEmailAutomatico.modificar(dtoEnvioEmailAutomatico, con);
						//enviar mail.
						//UtilidadEmail.enviarCorreo("adminscso@servinte.com.co", dtoEnvioEmailAutomatico.getEmailUsuario(), "PRUEBA","PRUEBA");

					}
				}
				else
				{
					//INSERTAR
					if(!EnvioEmailAutomatico.insertar(dtoEnvioEmailAutomatico, con))
					{
						UtilidadBD.abortarTransaccion(con);
						ActionErrors errores=new ActionErrors();
						errores.add("problema en base de datos", new ActionMessage("errors.problemasBd"));
						saveErrors(request, errores);
						return mapping.findForward("principal");
					}
					/*
					else
					{
						
						//enviar mail.
						//UtilidadEmail.enviarCorreo("adminscso@servinte.com.co", Utilidades.obtenerEmailUsuario(dtoEnvioEmailAutomatico.getUsuario()), "PRUEBA","PRUEBA");

					}
					*/
				}
				
			}
			else if (dtoEnvioEmailAutomatico.isExisteBD())
			{
				
				EnvioEmailAutomatico.eliminar(dtoEnvioEmailAutomatico, con);
				
				
				
			}
		}
		UtilidadBD.finalizarTransaccion(con);
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			logger.error("Error cerrando la conexión",e);
		}
		return accionEmpezar(forma, usuario, request, mapping, true);
	}

// metodo llena el arreglo dto usuarios
	private ActionForward accionCosultarUsuarios(
			EnvioEmailAutomaticoForm forma, ActionMapping mapping, UsuarioBasico usuario) {

		
		forma.setListaDtoUsuarios(UtilidadesAdministracion.obtenerUsuarios(new Usuario(), usuario.getCodigoInstitucionInt(), false));
		
		return mapping.findForward("principal");
	}

// Metodo crea un nuevo registro 
	private ActionForward accionNuevo(EnvioEmailAutomaticoForm forma,
			ActionMapping mapping, UsuarioBasico usuario) 
	{
		DtoEnvioEmailAutomatico envio = new DtoEnvioEmailAutomatico();
		// botiene la institucion del arreglo dto usuarios
		envio.setInstitucion(usuario.getCodigoInstitucionInt());
		// obtiene el login del arreglo dto de usuarios
		envio.setUsuarioModifica(usuario.getLoginUsuario());
		forma.getListaDtoEnvioMail().add(envio);
		// mensaje de guardado con exito
		forma.setExito(false);
		return mapping.findForward("principal");
	}
}
