package com.princetonsa.action.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.ResponderConsultasEntSubcontratadasForm;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.ordenesmedicas.ResponderConsultasEntSubcontratadas;


public class ResponderConsultasEntSubcontratadasAction extends Action 
{
	Logger logger = Logger.getLogger(ResponderConsultasEntSubcontratadasAction.class);
	ResponderConsultasEntSubcontratadas mundo;
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {

		Connection con = null;

		try {

			if(response == null);

			if (form instanceof ResponderConsultasEntSubcontratadasForm) 
			{			 
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo = new ResponderConsultasEntSubcontratadas();

				ResponderConsultasEntSubcontratadasForm forma = (ResponderConsultasEntSubcontratadasForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset();
					forma.resetArryEntidadesSubcontratadas();
					ActionForward forward = new ActionForward();
					forward=accionValidacionesUsuario(con, forma, usuario, request, mapping);
					if(forward != null)
						return forward;	  	
					UtilidadBD.cerrarConexion(con);	

					return mapping.findForward("principal");

				}
				else if(estado.equals("consultaPaciente"))
				{  
					ActionForward forward = new ActionForward();
					forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
					if(forward != null)
						return forward;	
					UtilidadBD.closeConnection(con);
					logger.info("ESTE es el tamaño del ARREGLO  en action " + forma.getEntidadesSubcontratadas().size()); 
					forma.setOpcionListado("paciente");
					return mapping.findForward("paciente");

				}
				else if(estado.equals("consultarRango"))
				{  
					UtilidadBD.closeConnection(con); 
					forma.setOpcionListado("rango");
					return mapping.findForward("rango");
				}
				else if(estado.equals("buscarSolicitudes"))
				{   
					UtilidadBD.closeConnection(con);
					return listadoSolicitudes(forma, paciente, usuario, request, mapping);

				}
				else if(estado.equals("buscarSolicitudesRango"))
				{   
					UtilidadBD.closeConnection(con);
					return listadoSolicitudesXRango(forma, usuario, request, mapping);

				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("ordenar"))
				{    
					UtilidadBD.closeConnection(con); 
					return ordenarXColumna(forma, mapping);
				}
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	
	

  /**
   * 
   * @param forma
   * @param mapping
   * @return
   */
	private ActionForward ordenarXColumna(ResponderConsultasEntSubcontratadasForm forma, ActionMapping mapping) {
		
			
		ArrayList<DtoSolicitudesSubCuenta> array= new ArrayList<DtoSolicitudesSubCuenta>();
		  logger.info ("\n\n Tamaño Lista Solicitudes  >> "+forma.getSolicitudesaResponder().size());
		  array=mundo.ordenarColumna(forma.getSolicitudesaResponder(),forma.getUltimaPropiedad(), forma.getPropiedadOrdenar());
		  forma.setUltimaPropiedad(forma.getPropiedadOrdenar());
		  forma.resetArraySolicitudesaResponder();
		  forma.setSolicitudesaResponder(array);
		  if(forma.getOpcionListado().equals("paciente"))
		  {
		  return mapping.findForward("resultadoPaciente");
		  }
		  else{
			  if(forma.getOpcionListado().equals("rango"))
			  {
			  return mapping.findForward("resultadoRango");
			  }
		  }
		  
		  return null;
		
	}


	/**
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward listadoSolicitudes(ResponderConsultasEntSubcontratadasForm forma,PersonaBasica paciente, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) 
	{
		
		forma.setSolicitudesaResponder(mundo.obtenerListadoSolicitudes(paciente.getCodigoPersona(), Utilidades.convertirAEntero(forma.getParametrosBusqueda().get("entidadSubcontratada").toString()), usuario.getCodigoInstitucionInt(),usuario.getCodEspecialidadesMedico() ));
		return mapping.findForward("resultadoPaciente");
	}

	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward listadoSolicitudesXRango(ResponderConsultasEntSubcontratadasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
		
		 ActionErrors errores = new ActionErrors();
		 
		 errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());		
		 
			if(errores.isEmpty())
			{	
				forma.getParametrosBusqueda().put("centroAtencion", usuario.getCodigoCentroAtencion()); 
				forma.getParametrosBusqueda().put("especialidadesProf",usuario.getCodEspecialidadesMedico());
				forma.setSolicitudesaResponder(mundo.obtenerSolicitudesXRango(forma.getParametrosBusqueda()));				
				return mapping.findForward("resultadoRango");
			}
			else
			{		
				saveErrors(request, errores);	
				return mapping.findForward("rango");
			}
		}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionValidarPaciente(Connection con,	ResponderConsultasEntSubcontratadasForm forma,PersonaBasica paciente, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}
        
		return null;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionValidacionesUsuario(Connection con,ResponderConsultasEntSubcontratadasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
		HashMap permisos = new HashMap();
		permisos = mundo.validarPermisos(con, usuario);
		ArrayList<DtoEntidadSubcontratada> entidadesSubcontratadas = new ArrayList<DtoEntidadSubcontratada>();
		 CargosEntidadesSubcontratadas cargosEntidadesSubCont = new  CargosEntidadesSubcontratadas();
		
		if(!permisos.get("tipoEntidadEjecuta").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			 UtilidadBD.closeConnection(con);
			 return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Opcion para responder Consultas Entidades Subcontratadas. Por Favor verifique ", false);
		}
		else{
			entidadesSubcontratadas = (ArrayList<DtoEntidadSubcontratada>)permisos.get("entidadesSubContratadas"); 
	       
			if(entidadesSubcontratadas.size() > 0)
	        {
	        	for (int i=0; i < entidadesSubcontratadas.size(); i++)
	        	{
	        		DtoEntidadSubcontratada dto= (DtoEntidadSubcontratada)entidadesSubcontratadas.get(i);
	        		
	        		if(UtilidadesFacturacion.esUsuarioconPermisoResponderConsultasEntSubcont(dto.getConsecutivo(), usuario.getLoginUsuario()))
	        		{
	        			forma.getEntidadesSubcontratadas().add(dto);
	        			
	        		}
	        		
	        	}
	        	if(forma.getEntidadesSubcontratadas().size()<=0 )
	        	{
	        		UtilidadBD.closeConnection(con);
	        		return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Usuario sin Permiso para la Entidad Subcontratada. Por Favor verifique ", false);
	        	}
	        	else
	        	{
	        		if(!UtilidadValidacion.esProfesionalSalud(usuario))
	        		{
	        			UtilidadBD.closeConnection(con);;
	        			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Usuario No es Profesional de la Salud. Permisos denegados. ", false);
	        		}
	        	}
	        	
	        	forma.setNumEntidades(forma.getEntidadesSubcontratadas().size());
	        	
	        	if(forma.getNumEntidades()==1)
	        	{
	        		forma.getParametrosBusqueda().put("entidadSubcontratada", forma.getEntidadesSubcontratadas().get(0).getConsecutivo());
	        	}
	        }else
	        {
	        	UtilidadBD.closeConnection(con);
	        	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Centro de Costo no tiene asociadas Entidad Subcontratada. Por favor verifique ", false);
	        }
				  
		}
		return null;
	}
	 
}
