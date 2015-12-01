/*
 * enero 2, 2008
 */
package com.princetonsa.action.manejoPaciente;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.LecturaPlanosEntidadesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.LecturaPlanosEntidades;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Lectura Planos Entidades Subcontratadas
 */
public class LecturaPlanosEntidadesAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(LecturaPlanosEntidadesAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof LecturaPlanosEntidadesForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				LecturaPlanosEntidadesForm lecturaForm =(LecturaPlanosEntidadesForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				String estado=lecturaForm.getEstado(); 
				logger.warn("\n\n En LecturaPlanosEntidadesAction el Estado ["+estado+"] \n\n");


				if(estado == null)
				{
					lecturaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Lectura Planos Entidades (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,lecturaForm,mapping,usuario,request);
				}
				else if (estado.equals("ejecutar"))
				{
					return accionEjecutar(con,lecturaForm,mapping,usuario,request);
				}
				else if (estado.equals("cargarInconsistencias"))
				{
					return accionCargarInconsistencias(con,lecturaForm,mapping,request);
				}
				//*********ESTADOS RELACIONADOS CON LA SELECCION DE ARCHIVOS POR CLIENTE****************
				else if (estado.equals("seleccionarArchivo"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seleccionarArchivo");
				}
				else if (estado.equals("adjuntarArchivo"))
				{
					return accionAdjuntarArchivo(con,lecturaForm,mapping,request);
				}
				else if (estado.equals("eliminarArchivo"))
				{
					return accionEliminarArchivo(con,lecturaForm,response);
				}
				//***************************************************************************************
				else
				{
					lecturaForm.reset();
					logger.warn("Estado no valido dentro del flujo de LecturaPlanosEntidadesAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	 * Método implementado para eliminar un archivo adjunto
	 * @param con
	 * @param lecturaForm
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarArchivo(Connection con, LecturaPlanosEntidadesForm lecturaForm, HttpServletResponse response) 
	{
		//Se elimina el archivo
		lecturaForm.setArchivos(lecturaForm.getTipoArchivo(), null);
		lecturaForm.setArchivos("archivo"+lecturaForm.getTipoArchivo(), null);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write("<tipo-archivo>"+lecturaForm.getTipoArchivo()+"</tipo-archivo>");
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionEliminarArchivo: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para adjuntar el archivo seleccionado
	 * @param con
	 * @param lecturaForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionAdjuntarArchivo(Connection con, LecturaPlanosEntidadesForm lecturaForm, ActionMapping mapping, HttpServletRequest request) 
	{
		if(lecturaForm.getArchivo().getFileSize()>0)
		{
		
			//se verifica si ya se tiene el directorio temporal donde se guardarán los archivos
			if(lecturaForm.getDirectorioArchivos().equals(""))
			{
				Random r= new Random();
				//Directorio donde se almacenarán temporalmente los archivos RIPS leídos desde el cliente
				String directorioTemporal = "borrableLecturaPlanos"+Math.abs(r.nextInt())+System.getProperty("file.separator");
				lecturaForm.setDirectorioArchivos(ValoresPorDefecto.getFilePath()+directorioTemporal);
			}
			
			logger.info("la ruta temporal cuando es "+lecturaForm.getTipoArchivo()+" es=> "+lecturaForm.getDirectorioArchivos());
			
			File prueba = UtilidadFileUpload.guardarArchivo(lecturaForm.getArchivo(),lecturaForm.getDirectorioArchivos());
			
			lecturaForm.setArchivos(lecturaForm.getTipoArchivo(), lecturaForm.getArchivo().getFileName());
			lecturaForm.setArchivos("archivo"+lecturaForm.getTipoArchivo(), prueba);
			lecturaForm.setNombreArchivo(lecturaForm.getArchivo().getFileName());
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.required","El archivo "+lecturaForm.getTipoArchivo()));
			saveErrors(request, errores);
			lecturaForm.setEstado("seleccionarArchivo"); //se reanuda el estado
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("seleccionarArchivo");
	}

	/**
	 * Método implementado para cargar el archivo de inconsistencias
	 * @param con
	 * @param lecturaForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarInconsistencias(Connection con, LecturaPlanosEntidadesForm lecturaForm, ActionMapping mapping, HttpServletRequest request) 
	{
		lecturaForm.setContenidoArchivoInconsistencias(LecturaPlanosEntidades.cargarArchivoInconsistencias(lecturaForm.getPathInconsistencias()));
		
		if(lecturaForm.getContenidoArchivoInconsistencias()==null)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.problemasGenericos","al tratar de cargar el archivo de inconsistencias"));
			saveErrors(request, errores);
			lecturaForm.setEstado("ejecutar");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implemento para realizar la ejecución del
	 * @param con
	 * @param lecturaForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionEjecutar(Connection con, LecturaPlanosEntidadesForm lecturaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws IPSException 
	{
		//Se limpia el path de inconsistencias
		lecturaForm.setPathInconsistencias("");
		
		LecturaPlanosEntidades mundo = new LecturaPlanosEntidades();
		mundo.ejecutar(con, lecturaForm,usuario);
		lecturaForm.setNumeroIngresosRegistrados(mundo.getNumeroIngresosRegistrados());
		
		//Se verifica errores
		if(!mundo.getErrores().isEmpty())
			saveErrors(request, mundo.getErrores());
		
		//Se verifica si hubo inconsistencias
		if(mundo.isHuboInconsistencias())
			lecturaForm.setPathInconsistencias(mundo.getPathArchivoInconsistencias());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para iniciar el flujo de la lectura de planos pacientes de entidades subcontratadas
	 * @param con
	 * @param lecturaForm
	 * @param mapping
	 * @param usuario 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, LecturaPlanosEntidadesForm lecturaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		lecturaForm.reset();
		lecturaForm.setEstado("empezar");
		
		//Se cargan las estructuras del formulario
		lecturaForm.setEntidadesSubcontradas(UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));
		lecturaForm.setCodigosManuales(Utilidades.obtenerTarifariosOficiales(con, ""));
		
		//Se lee el parámetro de ubicación plano entidades subcontratadas
		lecturaForm.setUbicacionPlanosEntidadesSubcontratadas(ValoresPorDefecto.getUbicacionPlanosEntidadesSubcontratadas(usuario.getCodigoInstitucionInt()));
		
		//Si el parámetro no está definido no se puede continuar
		if(lecturaForm.getUbicacionPlanosEntidadesSubcontratadas().equals(""))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Ubicación planos entidades subcontratadas"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
			
		}
		//Si es por servidor se consulta la ruta del último proceso generado
		else if(lecturaForm.getUbicacionPlanosEntidadesSubcontratadas().equals(ConstantesIntegridadDominio.acronimoServidor))
		{
			;
			/**
			 * PENDIENTE consultar el path por servidor de los planos entidades subcontratadas desde el log
			 */
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
