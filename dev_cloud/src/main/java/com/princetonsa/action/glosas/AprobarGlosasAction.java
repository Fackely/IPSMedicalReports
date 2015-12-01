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

import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.glosas.AprobarGlosasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.AprobarGlosas;
import com.princetonsa.mundo.glosas.ConfirmarAnularGlosas;

public class AprobarGlosasAction extends Action
{
	private Logger logger = Logger.getLogger(AprobarGlosasAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	

		Connection con = null;
		try{
			if(form instanceof AprobarGlosasForm)
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
				AprobarGlosasForm forma = (AprobarGlosasForm)form;		

				//se instancia el mundo
				AprobarGlosas mundo = new AprobarGlosas();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.

				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE APROBAR GLOSAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");

				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}	
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("buscar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("aprobarGlosas");				   			
				}
				else if(estado.equals("mostrarGlosa"))
				{
					return this.accionMostrarGlosa(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("guardar"))
				{
					errores=validarGlosa(con,forma,mundo);

					if (!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("aprobarGlosas");
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
	 * Guarda los datos de Glosa Aprobada
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(AprobarGlosasForm forma, AprobarGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(mundo.guardar(con, forma.getInformacionGlosa("glosasis")+"", usuario.getLoginUsuario()))
		{
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			forma.setGuardo("S");
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"La glosa no se actualizó satisfactoriamente."));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("aprobarGlosas");
	}

	/**
	 * Metodo encargado de validar Suma valor de Glosa
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionErrors validarGlosa(Connection con, AprobarGlosasForm forma, AprobarGlosas mundo) 
	{
		ActionErrors errors = new ActionErrors();
		boolean mensaje=false;
		
		if((forma.getCheck()+"").equals(""))
			errors.add("descripcion",new ActionMessage("prompt.generico","El campo de chequeo debe estar activo."));
		else
		{	
			mensaje=ConfirmarAnularGlosas.validarAnuConfGlosa(con, forma.getInformacionGlosa("codglosa")+"", forma.getInformacionGlosa("valor")+"");
		
			if(mensaje==false)
				errors.add("descripcion",new ActionMessage("prompt.generico","El Total de la Glosa esta descuadrado respecto al detalle de solicitudes."));

			mensaje=mundo.validarAprobarGlosa(con, forma.getInformacionGlosa("codglosa")+"", forma.getInformacionGlosa("valor")+"");
			
			if(mensaje==false)
				errors.add("descripcion",new ActionMessage("prompt.generico","El Total de la Glosa esta descuadrado respecto al detalle de facturas."));
		}
		return errors;
	}
	
	/**
     * Muestra la Glosa de la busqueda 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionMostrarGlosa(AprobarGlosasForm forma, AprobarGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("aprobarGlosas");
	}

	/**
    * Inicia en el forward de Aprobar Glosas
    * @param forma
    * @param con
    * @param mapping
    * @return
    */
	private ActionForward accionEmpezar(AprobarGlosasForm forma, AprobarGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.setConveniosMap(mundo.consultarConvenios(con));
									
		UtilidadBD.closeConnection(con);
		return mapping.findForward("aprobarGlosas");
	}
}