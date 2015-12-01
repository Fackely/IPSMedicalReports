package com.princetonsa.action.glosas;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.glosas.ConfirmarAnularGlosasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConfirmarAnularGlosas;

public class ConfirmarAnularGlosasAction extends Action
{
	private Logger logger = Logger.getLogger(ConfirmarAnularGlosasAction.class);	
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof ConfirmarAnularGlosasForm)
			{

				con = UtilidadBD.abrirConexion();

				//se verifica si la conexion esta nula
				if(con == null)
				{	
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}


				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				ConfirmarAnularGlosasForm forma = (ConfirmarAnularGlosasForm)form;		

				//se instancia el mundo
				ConfirmarAnularGlosas mundo = new ConfirmarAnularGlosas();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE CONFIRMAR ANULAR GLOSAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");


				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}			
				else
					if(estado.equals("empezar"))
					{
						return this.accionEmpezar(forma, mundo, con, mapping, usuario);					   			
					}
					else
						if(estado.equals("buscar"))
						{
							UtilidadBD.closeConnection(con);
							return mapping.findForward("confirmarAnularGlosas");				   			
						}
						else
							if(estado.equals("mostrarGlosa"))
							{
								return this.accionMostrarGlosa(forma, mundo, con, mapping, usuario);					   			
							}
							else
								if(estado.equals("guardar"))
								{
									errores=validarGlosa(con,forma,mundo);

									if (!errores.isEmpty())
									{
										saveErrors(request,errores);	
										UtilidadBD.closeConnection(con);
										return mapping.findForward("confirmarAnularGlosas");
									}
									return this.accionGuardar(forma, mundo, con, mapping, usuario);					   			
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
	
	
	/**
     * Inicia en el forward de Confirmar Anular Glosas 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ConfirmarAnularGlosasForm forma, ConfirmarAnularGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.setConveniosMap(ConfirmarAnularGlosas.consultarConvenios(con));
			
		forma.setInformacionGlosa("fechareg", "");
		forma.setInformacionGlosa("fechanot", "");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("confirmarAnularGlosas");
	}
	
	/**
     * Muestra la Glosa de la busqueda 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionMostrarGlosa(ConfirmarAnularGlosasForm forma, ConfirmarAnularGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		logger.info("\n\nINDICATIVO GLOSA->"+forma.getInformacionGlosa("indicativoglosa"));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("confirmarAnularGlosas");
	}
	
	/**
     * Guardar 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionGuardar(ConfirmarAnularGlosasForm forma, ConfirmarAnularGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		if(mundo.guardar(con, forma.getInformacionGlosa("codglosa")+"", forma.getCheckCA(), forma.getMotivo(), usuario.getLoginUsuario(),forma.getInformacionGlosa("indicativoglosa")+""))
		{
			if(forma.getCheckCA().equals("CON"))
				forma.setGuardo("C");
			else if(forma.getCheckCA().equals("ANU"))
				forma.setGuardo("A");
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Proceso no exitoso, debido a que la Glosa tiene detalle a nivel de Solicitudes."));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("confirmarAnularGlosas");
	}
	
	/**
	 * Metodo encargado de validar Suma valor de Glosa
	 */
	public ActionErrors validarGlosa (Connection con, ConfirmarAnularGlosasForm forma, ConfirmarAnularGlosas mundo)
	{
		ActionErrors errors = new ActionErrors();
		boolean mensaje=false;
		
		if((forma.getCheckCA()+"").endsWith("CON"))
		{
			mensaje=mundo.validarAnuConfGlosa(con, forma.getInformacionGlosa("codglosa")+"", forma.getInformacionGlosa("valor")+"");
					
			if(mensaje==false)
			{			
				errors.add("descripcion",new ActionMessage("prompt.generico","El Total de la Glosa esta descuadrado respecto al detalle."));				
			}			
			else if(!forma.getInformacionGlosa("indicativoglosa").equals(ConstantesIntegridadDominio.acronimoIndicativoGlosa))
			{
				errors.add("descripcion",new ActionMessage("prompt.generico","No se puede realizar la Confirmacion de una Pre- Glosa."));
			}
		}
		
		return errors;
	}
}