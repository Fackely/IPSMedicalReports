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
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.glosas.RadicarRespuestasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.AprobarAnularRespuestas;
import com.princetonsa.mundo.glosas.RadicarRespuestas;
import com.princetonsa.mundo.glosas.RegistrarRespuesta;

public class RadicarRespuestasAction extends Action
{
	private Logger logger = Logger.getLogger(RadicarRespuestasAction.class);
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
	{	
		
		Connection con = null;
		try{
		if(form instanceof RadicarRespuestasForm)
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
				RadicarRespuestasForm forma = (RadicarRespuestasForm)form;		
				
				//se instancia el mundo
				RadicarRespuestas mundo = new RadicarRespuestas();
				
				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();
				
				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();
				
				forma.setMensaje(new ResultadoBoolean(false));
				
				//se instancia la variable para manejar los errores.
				
				
				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE RADICAR RESPUESTAS ES ====>> "+estado);
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
					return this.accionBuscar(con, forma, mundo, mapping, usuario);					   			
				}		
				else if(estado.equals("mostrarRespuesta"))
				{
					return this.accionMostrarRespuesta(con, forma, mundo, mapping, usuario);					   			
				}
				else if(estado.equals("buscarGlosa"))
				{
					return this.accionBuscarGlosa(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("radicar"))
				{
					errores=validar(con, forma, mundo, mapping, usuario);  
					
					if (!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("radicarRespuestas");
					}
					return this.accionGuardar(con, forma, mundo, mapping, usuario);   			
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
	 * Metodo que busca la glosa y su respuesta por Glosa sistema
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarGlosa(RadicarRespuestasForm forma, RadicarRespuestas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		ActionErrors errors = new ActionErrors();		
				
		//Se carga un mapa encabezado Glosa con datos basicos de la glosa
		forma.setEncabezadoGlosaMap(RegistrarRespuesta.consultaEncabezadoGlosa(con, forma.getInformacionRespuesta("glosasis")+"", "Radicar"));
		logger.info("\n\nMAPA ENCABEZADO GLOSA----------->"+forma.getEncabezadoGlosaMap());
		
		//Si se encontro glosa se carga la forma
		if(Utilidades.convertirAEntero((forma.getEncabezadoGlosaMap("numRegistros")+""))>0)
		{
			forma.setInformacionRespuesta("fechareg", forma.getEncabezadoGlosaMap("fechaRegistroGlosa")+"");
			forma.setInformacionRespuesta("valor", forma.getEncabezadoGlosaMap("valorGlosa")+"");
			forma.setInformacionRespuesta("nomconvenio", forma.getEncabezadoGlosaMap("nombreConvenio")+"");
			forma.setInformacionRespuesta("contrato", forma.getEncabezadoGlosaMap("contrato")+"");
			forma.setInformacionRespuesta("glosaent", forma.getEncabezadoGlosaMap("glosaEntidad")+"");
			forma.setInformacionRespuesta("fechanot", forma.getEncabezadoGlosaMap("fechaNotificacion")+"");
			forma.setInformacionRespuesta("codglosa", forma.getEncabezadoGlosaMap("codigo")+"");
			forma.setInformacionRespuesta("indicativoglosa", forma.getEncabezadoGlosaMap("indicativoglosa")+"");
			
			forma.setInformacionRespuesta("respuesta", forma.getEncabezadoGlosaMap("respuesta")+"");
			forma.setInformacionRespuesta("observresp", forma.getEncabezadoGlosaMap("observacionesresp")+"");
			forma.setInformacionRespuesta("codrespuesta", forma.getEncabezadoGlosaMap("codigoresp")+"");
			forma.setInformacionRespuesta("fecharadresp", forma.getEncabezadoGlosaMap("fecharadresp")+"");

						
			if((forma.getInformacionRespuesta("codrespuesta")+"").equals(""))
				forma.setInformacionRespuesta("fecharesp", UtilidadFecha.getFechaActual());
			else
				forma.setInformacionRespuesta("fecharesp", forma.getEncabezadoGlosaMap("fecharesp")+"");			
			
		}
		else
		{
			forma.setInformacionRespuesta("glosasis", "");
			errors.add("descripcion",new ActionMessage("prompt.generico","No se encontraron registros para su selecccion."));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("radicarRespuestas");
	}
	
	
	/**
	 * Metodo encargado de validar el estado de los ajustes de Respuesta Glosa 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionErrors validar(Connection con, RadicarRespuestasForm forma,RadicarRespuestas mundo,ActionMapping mapping, UsuarioBasico usuario) 
	{
		ActionErrors errors = new ActionErrors();
		boolean band=true;
		
		String estado="Aprobado"; 
		
		band= AprobarAnularRespuestas.validarEstadoAjustes(con, forma.getInformacionRespuesta("codrespuesta")+"", estado);
				
		if(band==false)
			errors.add("descripcion",new ActionMessage("prompt.generico","El estado de los Ajustes Relacionados con la Respuesta Glosa NO deben tener estado Anulado."));
		
		return errors;
	}
	
	/**
	 * Metodo encargado de guardar la Glosa y la Respuesta 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(Connection con,	RadicarRespuestasForm forma, RadicarRespuestas mundo,ActionMapping mapping, UsuarioBasico usuario) 
	{	
		
		if(mundo.guardar(con, forma.getInformacionRespuesta("codrespuesta")+"", forma.getFecharadicacion(), forma.getNumRad(), forma.getObserv(), usuario.getLoginUsuario()))
		{
			forma.setGuardo("S");
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"La glosa no se actualizó satisfactoriamente."));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("radicarRespuestas");
	}
	
	/**
     * Metodo para mostrar la respuesta consultada 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionMostrarRespuesta(Connection con, RadicarRespuestasForm forma, RadicarRespuestas mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{					
		UtilidadBD.closeConnection(con);
		return mapping.findForward("radicarRespuestas");
	}
	
	/**
	 * Metodo para buscar Respuestas Glosa
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, RadicarRespuestasForm forma, RadicarRespuestas mundo, ActionMapping mapping, UsuarioBasico usuario)
	{
		forma.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		forma.setConvenio("");
		forma.setFecharadicacion("");
		forma.setNumRad("");
		forma.setObserv("");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("radicarRespuestas");
	}
	
	/**
     * Inicia en el forward de Registrar Modificar Glosas 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(RadicarRespuestasForm forma, RadicarRespuestas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("radicarRespuestas");
	}
}