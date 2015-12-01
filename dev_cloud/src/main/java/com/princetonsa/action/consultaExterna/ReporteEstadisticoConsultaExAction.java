package com.princetonsa.action.consultaExterna;
 
import java.sql.Connection;
import java.util.HashMap;

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

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.consultaExterna.ReporteEstadisticoConsultaExForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.ReporteEstadisticoConsultaEx;
import com.princetonsa.pdf.CancelacionCitaPacienteInstitu;

/**
 * @author Jose Eduardo Arias Doncel 
 * Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class ReporteEstadisticoConsultaExAction extends Action
{	
	Logger logger = Logger.getLogger(ReporteEstadisticoConsultaExAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {		

		Connection con = null;
		try {
			if(response == null);

			if (form instanceof ReporteEstadisticoConsultaExForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");			 
				//ActionErrors
				ActionErrors errores = new ActionErrors();

				ReporteEstadisticoConsultaExForm forma = (ReporteEstadisticoConsultaExForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");
				logger.info("Tipo de Reporte     >> "+forma.getConstanteReporte());			 			 
				logger.info("-------------------------------------");

				if(estado == null)
				{				 
					logger.warn("Estado no Valido dentro del Flujo de Reportes Estadisticos Consulta Externa (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//-----------------------------------------------------------------------------------			 
				//***********************************************************************************
				//*****************Estados del Action ***********************************************
				//-----------------------------------------------------------------------------------			 
				else if(estado.equals("empezar"))
				{
					errores = accionEmpezar(con,forma,usuario,errores);		

					if(!errores.isEmpty())			
						saveErrors(request, errores);											

					UtilidadBD.closeConnection(con);
					return mapping.findForward("tiporeporte");
				}
				else if(estado.equals("busquedaReporte"))
				{				 
					errores = accionBusquedaReporte(con,forma,usuario,errores);

					if(!errores.isEmpty())
						saveErrors(request, errores);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("generarReporte"))
				{
					errores = accionValidacionGenerarReporte(con,forma,errores);

					if(!errores.isEmpty())
						saveErrors(request, errores);
					else
					{
						//Reportes Realizados en Itext
						if(forma.getConstanteReporte() == ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente || 
								forma.getConstanteReporte() == ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion)
						{
							accionGenerarReporteItext(con,forma,request,errores,usuario,forma.getConstanteReporte());
							UtilidadBD.closeConnection(con);
							return mapping.findForward("abrirPdf");
						}
						else
						{
							errores = accionGenerarReporte(con,forma,request,errores,usuario);					 
							if(!errores.isEmpty())
								saveErrors(request, errores); 
						}
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				 
				}
				//************************************************************************************
				else if(estado.equals("especialiadades") || estado.equals("motivoCancelacion") || estado.equals("profesional"))
				{
					metodoMostrarDatosSeleccion(con,forma,usuario);				 				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listados");
				}			 
				else if(estado.equals("addEspecialidad") || estado.equals("addMotivoCancelacion") || estado.equals("addProfesional"))
				{
					metodoAdicionarDatosSeleccion(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}	
				else if(estado.equals("eliminarEspecialidad") || estado.equals("eliminarMotivoCancelacion") || estado.equals("eliminarProfesional"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
			}

			return mapping.findForward("tiporeporte");
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
								 }
	
	
	//**************************************************************************************************
	//**************************************************************************************************
	//**************************************************************************************************	
	
	/**
	 * Acción Empezar 
	 * @param Connection con
	 * @param ReporteEstadisticoConsultaExForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionErrors errores   
	 * */
	public ActionErrors accionEmpezar(Connection con, ReporteEstadisticoConsultaExForm forma, UsuarioBasico usuario, ActionErrors errores)
	{
		//errores = ReporteEstadisticoConsultaEx.metodoValidarUsuario(usuario.getLoginUsuario(),1, errores);
		
		if(errores.isEmpty())		
			forma.resetAtributosGenerales();		
		
		return errores;
	}	
	
	//****************************************************************************************************
	
	/**
	 * Acción Busqueda Reporte 
	 * @param Connection con
	 * @param ReporteEstadisticoConsultaExForm forma
	 * @param UsuarioBasico usuario
	 * @param  ActionErrors errors
	 */	
	public ActionErrors accionBusquedaReporte(Connection con, ReporteEstadisticoConsultaExForm forma, UsuarioBasico usuario, ActionErrors errores)
	{
		//Actualiza el mapa con los parametros de la busqueda dependiendo del tipo de reporte
		forma.setMapaBusqueda(ReporteEstadisticoConsultaEx.metodoActualizarMapaBusqueda(
				con,
				usuario,
				forma.getConstanteReporte()));
		
		if(forma.getMapaBusqueda("existeReporte").toString().equals(ConstantesBD.acronimoNo))
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No Existe Información de Búsqueda para este Reporte"));
			
		return errores;		
	}	
		
	//*****************************************************************************************************
	
	/**
	 * Realiza las validaciones de los datos requeridos
	 * @param Connection con
	 * @param ReporteEstadisticoConsultaExForm forma
	 * @param ActionErrors errores
	 * */
	public ActionErrors accionValidacionGenerarReporte(Connection con,ReporteEstadisticoConsultaExForm forma,ActionErrors errores)
	{				
		return ReporteEstadisticoConsultaEx.metodoValidacionGenerarReporte(
				forma.getMapaBusqueda(),
				forma.getConstanteReporte(),
				errores);		
	}
	
	//*****************************************************************************************************	
	
	/**
	 * Metodo para la generacion del Reporte
	 * @param Connection con
	 * @param ReporteEstadisticoConsultaExForm forma
	 * @param HttpServletRequest request
	 * @param ActionErrors errores
	 * @param UsuarioBasico usuario  
	 * */
	public ActionErrors accionGenerarReporte(
			Connection con,
			ReporteEstadisticoConsultaExForm forma,
			HttpServletRequest request,
			ActionErrors errores,
			UsuarioBasico usuario)
	{		   
		//Actualiza las especialidades
		forma.setMapaBusqueda("estructuraEspecialidad",forma.getEstructuraEspecialidadInfo());
		//Actualiza los Motivos de Cancelacion
		forma.setMapaBusqueda("estructuraMotivoCancelacion",forma.getEstructuraMotivosCancelacionInfo());
		//Actualiza los Profesionales de la salud
		forma.setMapaBusqueda("estructuraProfesional",forma.getEstructuraProfesionalInfo());
		
		//Utilidades.imprimirMapa(forma.getMapaBusqueda());
		HashMap resul;		
		
		try
		{
			resul = ReporteEstadisticoConsultaEx.metodoGeneracionReporte(
					con,
					forma.getConstanteReporte(),
					forma.getMapaBusqueda(),
					(InstitucionBasica)request.getSession().getAttribute("institucionBasica"),
					errores,
					usuario);
			
			if(!resul.get("descripcion").toString().equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport",resul.get("descripcion").toString());
	        	forma.setMapaBusqueda("urlArchivoPlano", resul.get("urlArchivoPlano"));
	        	forma.setMapaBusqueda("pathArchivoPlano", resul.get("pathArchivoPlano"));
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas al Generar el Archivo Plano"));
		}
		
		//Utilidades.imprimirMapa(forma.getMapaBusqueda());
		
		return errores;
	}
	
	//*****************************************************************************************************
	
	/**
	 * Genera el reporte Itext
	 * @param  Connection con
	 * @param ReporteEstadisticoConsultaExForm forma
	 * @param HttpServletRequest request
	 * @param ActionErrors errores
	 * @param UsuarioBasico usuario
	 * */
	public void accionGenerarReporteItext(
			Connection con,
			ReporteEstadisticoConsultaExForm forma,
			HttpServletRequest request,
			ActionErrors errores,
			UsuarioBasico usuario,
			int codigoReporte)
	{
		//Actualiza las especialidades
		forma.setMapaBusqueda("estructuraEspecialidad",forma.getEstructuraEspecialidadInfo());
		//Actualiza los Motivos de Cancelacion
		forma.setMapaBusqueda("estructuraMotivoCancelacion",forma.getEstructuraMotivosCancelacionInfo());
		//Actualiza los Profesionales de la salud
		forma.setMapaBusqueda("estructuraProfesional",forma.getEstructuraProfesionalInfo());
		
		///se Genera el Reporte
		HashMap respuesta = new HashMap();
		if(forma.getConstanteReporte() == ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente)
		{		
	 		respuesta = CancelacionCitaPacienteInstitu.pdfCancelacionCita(
						con,
						ValoresPorDefecto.getFilePath(),				
						forma.getConstanteReporte(), 
						forma.getMapaBusqueda(),
						usuario,
						request);
		}
		else if (forma.getConstanteReporte() == ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion)
		{
			respuesta = CancelacionCitaPacienteInstitu.pdfCancelacionCitaPorInstitucion(
					con,
					ValoresPorDefecto.getFilePath(),				
					forma.getConstanteReporte(), 
					forma.getMapaBusqueda(),
					usuario,
					request);
		}	
    	
    	request.setAttribute("nombreArchivo", System.getProperty("file.separator")+respuesta.get("nombreArchivo").toString());
    	request.setAttribute("nombreVentana", "Cancelación de Cita");	
	}
	
	//*****************************************************************************************************

	/**
	 * Metodo para mostrar los datos se seleccion  
	 * @param Connection con 
	 * @param ReporteEstadisticoConsultaExForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void metodoMostrarDatosSeleccion(Connection con, ReporteEstadisticoConsultaExForm forma, UsuarioBasico usuario)
	{
		if(forma.getEstado().equals("especialiadades"))
			forma.setEspecialidadesMap(
					Utilidades.obtenerEspecialidades(ReporteEstadisticoConsultaEx.metodoArmarCodigosInsertados(forma.getEstructuraEspecialidadInfo())));
		else if(forma.getEstado().equals("motivoCancelacion"))
			forma.setMotivoCancelacionMap(
					ReporteEstadisticoConsultaEx.metodoArmarMotivosCancelacion(con,ReporteEstadisticoConsultaEx.metodoArmarCodigosInsertados(forma.getEstructuraMotivosCancelacionInfo()),forma.getConstanteReporte()));
		else if(forma.getEstado().equals("profesional"))
			forma.setProfesionalMap(
					ReporteEstadisticoConsultaEx.metodoArmarProfesionalSalud(con,usuario,ReporteEstadisticoConsultaEx.metodoArmarCodigosInsertados(forma.getEstructuraProfesionalInfo())));	
	}
	
	//********************************************************************************************************
	
	/**
	 * metodo para adicionar especialidades a la busqueda generica
	 * @param ReporteEstadisticoConsultaExForm forma
	 * */
	public void metodoAdicionarDatosSeleccion(ReporteEstadisticoConsultaExForm forma)
	{	
		if(forma.getEstado().equals("addEspecialidad"))
			forma.setEstructuraEspecialidadInfo(
					ReporteEstadisticoConsultaEx.metodoInsertarDatosSeleccion(forma.getEstructuraEspecialidadInfo(),forma.getCodigoElementoAdd(),forma.getNombreElementoAdd()));
		else if(forma.getEstado().equals("addMotivoCancelacion"))
			forma.setEstructuraMotivosCancelacionInfo(
						ReporteEstadisticoConsultaEx.metodoInsertarDatosSeleccion(forma.getEstructuraMotivosCancelacionInfo(),forma.getCodigoElementoAdd(),forma.getNombreElementoAdd()));
		else if(forma.getEstado().equals("addProfesional"))
			forma.setEstructuraProfesionalInfo(
						ReporteEstadisticoConsultaEx.metodoInsertarDatosSeleccion(forma.getEstructuraProfesionalInfo(),forma.getCodigoElementoAdd(),forma.getNombreElementoAdd()));
		
		forma.setCodigoElementoAdd("");
		forma.setNombreElementoAdd("");		
	}	
	//*****************************************************************************************************
}