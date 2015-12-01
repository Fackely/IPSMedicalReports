package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.ConsultaEnvioInfAtencionIniUrgForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;

/**
 * @author Jose Eduardo Arias Doncel 
 * 
 * ** Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class ConsultaEnvioInfAtencionIniUrgAction extends Action
{	
	Logger logger = Logger.getLogger(ConsultaEnvioInfAtencionIniUrgAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {		
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof ConsultaEnvioInfAtencionIniUrgForm) 
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

				//ActionErrors
				ActionErrors errores = new ActionErrors();

				ConsultaEnvioInfAtencionIniUrgForm forma = (ConsultaEnvioInfAtencionIniUrgForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("menu"))
				{				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menu");
				}			 			 				 
				else if(estado.equals("empezarPaciente"))
				{
					forma.reset();
					errores = estadoEmpezarPaciente(con,forma,usuario,paciente,request);

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("menu");
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("paciente");
				}
				else if(estado.equals("empezarRango"))
				{
					forma.reset();
					estadoEmpezarRango(con, forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("rango");
				}
				else if(estado.equals("buscarRango"))
				{
					estadoBuscarRango(con,forma,request,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("rango");
				}			 
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado empezar paciente
	 * @param Connection con
	 * @param ConsultaEnvioInfAtencionIniUrgForm forma
	 * @param HttpServletRequest request
	 * */
	public ActionErrors estadoEmpezarPaciente(Connection con,ConsultaEnvioInfAtencionIniUrgForm forma,UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();		
		errores = RegistroEnvioInfAtencionIniUrg.validarPaciente(con,paciente,usuario.getCodigoInstitucionInt());
		
		if(!errores.isEmpty())	
			return errores;		
		else
		{
			forma.setListadoArray(
					RegistroEnvioInfAtencionIniUrg.getListadoInformeInicUrgeXPaciente(con,usuario.getCodigoInstitucionInt(),paciente.getCodigoPersona(),true,false));						
		}		
		
		return errores;
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado empezar rango
	 * @param Connection con
	 * @param ConsultaEnvioInfAtencionIniUrgForm forma
	 * */
	public void estadoEmpezarRango(Connection con,ConsultaEnvioInfAtencionIniUrgForm forma)
	{
		//Almacena la informacion de los convenios
		forma.setListadoArrayConvenio(Utilidades.obtenerConvenios(
				con,
				"",
				ConstantesBD.codigoTipoContratoEvento+"",
				false,
				"",
				false,
				false,
				"",
				true));				
		
		//Inicializa los parametros de busqueda
		forma.setParametrosBusqueda(RegistroEnvioInfAtencionIniUrg.inicializarParametrosBusquedaRango());				
	}
	
	//*****************************************************************
	
	/**
	 * Operaciones del estado busqueda por rango
	 * @param Connection con
	 * @param ConsultaEnvioInfAtencionIniUrgForm forma
	 * @param HttpServletRequest request
	 * */
	public void estadoBuscarRango(Connection con,ConsultaEnvioInfAtencionIniUrgForm forma,HttpServletRequest request,UsuarioBasico usuario)
	{
		//Validaciones para la busqueda por rango
		ActionErrors errores = new ActionErrors();
		errores = RegistroEnvioInfAtencionIniUrg.validacionConsultaBusquedaXRango(forma.getParametrosBusqueda());		
		
		if(errores.isEmpty())
		{
			forma.setListadoArray(
					RegistroEnvioInfAtencionIniUrg.getListadoInformeInicUrge(
						con, 
						"",
						"",
						forma.getParametrosBusqueda("fechaInicialGeneracion").toString(),
						forma.getParametrosBusqueda("fechaFinalGeneracion").toString(),
						forma.getParametrosBusqueda("fechaInicialEnvio").toString(), 
						forma.getParametrosBusqueda("fechaFinalEnvio").toString(),
						forma.getParametrosBusqueda("estadoEnvio").toString(), 
						forma.getParametrosBusqueda("convenio").toString(),
						"",
						usuario.getCodigoInstitucionInt()));			
		}
		else
		{		
			saveErrors(request, errores);			
		}
	}	
		
	//*****************************************************************
}