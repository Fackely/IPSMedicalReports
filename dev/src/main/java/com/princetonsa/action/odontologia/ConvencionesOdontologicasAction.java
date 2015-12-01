package com.princetonsa.action.odontologia;


import java.sql.Connection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.actionform.odontologia.ConvencionesOdontologicasForm;
import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.ConvencionesOdontologicas;
import com.princetonsa.sort.odontologia.SortGenerico;


/**
 * Controlador para la funcionalidad de convenciones odontológicas
 * @author Jorge Andrés Ortiz
 * @version 1.0
 */
public class ConvencionesOdontologicasAction extends Action{

	private ConvencionesOdontologicas mundo;

	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {
		Connection con = null;
		try{
			if (form instanceof ConvencionesOdontologicasForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				mundo = new ConvencionesOdontologicas();

				ConvencionesOdontologicasForm forma = (ConvencionesOdontologicasForm)form;		
				String estado = forma.getEstado();		 

				Log4JManager.info("Valor del Estado    >> "+forma.getEstado());			 

				if(estado.equals("empezar"))
				{ 
					forma.reset();	  
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuPrincipal");
				}
				else if(estado.equals("ingresarModificar"))
				{
					UtilidadBD.closeConnection(con);
					return empezarIngresarModificar(forma,usuario,request, mapping);	
				}else if(estado.equals("consultar"))
				{
					forma.reset();
					return accionConsultar(forma,usuario,request,mapping);				
				}

				else if(estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(con);
					forma.resetNuevaConvencion();
					return mapping.findForward("ingresarModificarConvencion");
				}
				else if(estado.equals("guardarNuevo"))
				{		
					return accionGuardarNuevaConvencion(con,forma,usuario,request, mapping);			  
				}

				else if(estado.equals("modificar"))
				{
					UtilidadBD.closeConnection(con);
					return cargarDtoConvencionModificar(forma,usuario,request, mapping);
				}
				else if(estado.equals("guardarModificar"))
				{				
					return accionGuardarModificar(con,forma,usuario,request, mapping);
				}
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					return accionEliminarConvencion(forma,usuario,request, mapping);

				}else if(estado.equals("ordenar"))
				{
					UtilidadBD.closeConnection(con);
					return accionOrdenar(forma,request,mapping);

				}else if(estado.equals("refresh"))
				{
					forma.setEstado("modificar"); 
					return mapping.findForward("ingresarModificarConvencion");
				}			
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());				
					return null;
				}
			}
		}catch (Exception e) {
			Log4JManager.debug(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	
	/**
	 * Método para consultar las convenciones odontológicas
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultar(ConvencionesOdontologicasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
		forma.setEstado("consultar");
		forma.setConvencionesOdontologicas(mundo.consultarConvencionesOdontologicas(usuario.getCodigoInstitucionInt()));
		return mapping.findForward("consultaConvencion");
	}

	/**
	 * Método para guardar una nueva Convención Odontológica
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarNuevaConvencion(Connection con,ConvencionesOdontologicasForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		
		ActionErrors errores = new ActionErrors();
		errores = mundo.validacionNuevosDatos(forma.getNuevaConvencion(),forma.getConvencionesOdontologicas());
		if(errores.isEmpty())
		{
			forma.getNuevaConvencion().setColor(forma.getColorFondo());
			forma.getNuevaConvencion().setBorde(forma.getColorBorde());
			UtilidadBD.iniciarTransaccion(con);
			
			int consecutivo = mundo.crearNuevaConvencionOdontologica(con,forma.getNuevaConvencion(),usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt()); 
			if(consecutivo > 0)
			{
				//Genera la imagen
				forma.getNuevaConvencion().setArchivoConvencion(mundo.generarImagen(forma.getNuevaConvencion(),consecutivo+""));
				   
				UtilidadBD.finalizarTransaccion(con);
				forma.reset();
				forma.setConvencionesOdontologicas(mundo.consultarConvencionesOdontologicas(usuario.getCodigoInstitucionInt()));
			} 
			else
			{   
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion de la Nueva Convención"));
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
		    }	
		}else
		{
			forma.setEstado("nuevo");
			saveErrors(request, errores);
		}
		
		if(errores.isEmpty())
		{	
			forma.setEstado("guardoExitoso");
		}	
		return mapping.findForward("ingresarModificarConvencion");
	}


	/**
	 * Método para realizar el proceso de Modificacion de una Convención
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarModificar(Connection con, ConvencionesOdontologicasForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		  ActionErrors errores = new ActionErrors();
		  boolean seModifico=false;
		  DtoConvencionesOdontologicas convencion = new DtoConvencionesOdontologicas();
		  convencion=(DtoConvencionesOdontologicas)forma.getConvencionesOdontologicas().get(forma.getPosConvencion());
		  forma.getNuevaConvencion().setColor(forma.getColorFondo());
		  forma.getNuevaConvencion().setBorde(forma.getColorBorde());
		  
		  // Se valida datos Requeridos y repetidos
		  errores = mundo.validacionNuevosDatos(forma.getNuevaConvencion(),forma.getConvencionesOdontologicas());
		  		  
		  if(errores.isEmpty())
			{
			  int consecutivoConvencion= convencion.getConsecutivo();
			   // Se valida si se hicieron Modificaciones a la información de la Convención
			   seModifico = mundo.validarExistenciaCambios(forma.getNuevaConvencion(),convencion);
			   
			   if(seModifico)
			   {
				   Log4JManager.info("Entro a modificar  >> Validacion Modifico ="+seModifico);
				   UtilidadBD.iniciarTransaccion(con);
				   
				   //Genera la imagen
				   mundo.generarImagen(forma.getNuevaConvencion(),forma.getNuevaConvencion().getConsecutivo()+"");
				   
				   if(mundo.modificarConvencionOdontologica(con,forma.getNuevaConvencion(),consecutivoConvencion,usuario.getLoginUsuario()))
				   {
					   UtilidadBD.finalizarTransaccion(con);
					   forma.reset();
					   forma.setConvencionesOdontologicas(mundo.consultarConvencionesOdontologicas(usuario.getCodigoInstitucionInt()));
				   } 
				   else
				   {
					  UtilidadBD.abortarTransaccion(con);
				   }
			   }
			}
			else
			{
				forma.setEstado("modificar");
				saveErrors(request, errores);	
			}
	 	  
		  if(errores.isEmpty())
			{	
				forma.setEstado("guardoExitoso");
			}
		  
		  UtilidadBD.closeConnection(con);
		  return mapping.findForward("ingresarModificarConvencion");
	}

	/**
	 * Método que realiza la Eliminación de una Convención  
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConvencion(ConvencionesOdontologicasForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		 ActionErrors errores =new ActionErrors();
		  int codigoConvencion = ((DtoConvencionesOdontologicas)forma.getConvencionesOdontologicas().get(forma.getPosConvencion())).getConsecutivo(); 
		
		if(mundo.eliminarConvencion(codigoConvencion))
		{   
			forma.reset();
			forma.setConvencionesOdontologicas(mundo.consultarConvencionesOdontologicas(usuario.getCodigoInstitucionInt()));
		}else
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No se pudo realizar el proceso de Eliminación porque la Convención esta siendo usada en otra funcionalidad."));
		    saveErrors(request, errores);
		}
		if(errores.isEmpty())
		{	
			forma.setEstado("guardoExitoso");
		}
		
		return mapping.findForward("ingresarModificarConvencion");
	}


   
	/**
	 * Método para Cargar la información de la Convención a Modificar
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarDtoConvencionModificar(ConvencionesOdontologicasForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		forma.resetNuevaConvencion();
		
		DtoConvencionesOdontologicas dtoNuevo=new DtoConvencionesOdontologicas();
		try 
		{		
			PropertyUtils.copyProperties(dtoNuevo,(DtoConvencionesOdontologicas)forma.getConvencionesOdontologicas().get(forma.getPosConvencion()));
			
		}catch(Exception e){
			Log4JManager.error(e);
		}
		
		forma.setNuevaConvencion(dtoNuevo);
		forma.setColorFondo(forma.getNuevaConvencion().getColor());
		forma.setColorBorde(forma.getNuevaConvencion().getBorde());
		return mapping.findForward("ingresarModificarConvencion");
	}



	/**
	 * Método que realiza la búsqueda de las Convenciones Existentes
	 * @param con
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward empezarIngresarModificar(ConvencionesOdontologicasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
		forma.setConvencionesOdontologicas(mundo.consultarConvencionesOdontologicas(usuario.getCodigoInstitucionInt()));
		return mapping.findForward("ingresarModificarConvencion");
	}
	
	
	/**
	 * Metodo para ordenar por columna las convenciones odonotologicas 	 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(ConvencionesOdontologicasForm forma, HttpServletRequest request, ActionMapping mapping)
	{
        boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()))
		{
			forma.setEsDescendente(forma.getPatronOrdenar()+"descendente") ;
		}else{
			forma.setEsDescendente(forma.getPatronOrdenar());
		}	
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getConvencionesOdontologicas(),sortG);
		return mapping.findForward("ingresarModificarConvencion");
	}
	
	
}
