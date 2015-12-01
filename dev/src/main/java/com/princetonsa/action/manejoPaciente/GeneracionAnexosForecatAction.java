/*
 * 30 de Abril, 2007
 */
package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;

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

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.GeneracionAnexosForecatForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.GeneracionAnexosForecat;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Generacion Anexos Forecat
 */
public class GeneracionAnexosForecatAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GeneracionAnexosForecatAction.class);
	
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
			if(form instanceof GeneracionAnexosForecatForm)
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
				GeneracionAnexosForecatForm generacionForm =(GeneracionAnexosForecatForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				String estado=generacionForm.getEstado(); 
				logger.warn("\n\n En GeneracionAnexosForecatAction el Estado ["+estado+"] \n\n");


				//*******VALIDACION DEL CAMPO CONVENIO FISALUD***********************************
				//Se verifica que se haya parametrizado el campo de convenio FISALUD
				if(ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt())==null||
						ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt()).equals("")||
						Integer.parseInt(ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt()))<=0)
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "FALTA PARAMETRO GENERAL", "error.forecat.parametroGeneral", true);
				//***************************************************************



				if(estado == null)
				{
					generacionForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Generacion Anexos Forecat (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,mapping,generacionForm);
				}
				else if(estado.equals("generar"))
				{
					return accionGenerar(con,generacionForm,mapping,request,usuario);
				}
				else if(estado.equals("detalle"))
				{
					return accionDetalle(mapping,usuario,con,generacionForm);
				}
				else
				{
					generacionForm.reset();
					logger.warn("Estado no valido dentro del flujo de GeneracionAnexosForecatAction (null) ");
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
	 * Método para cargar el contenido de un archivo FORECAT generado
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @param generacionForm
	 * @return
	 */
	private ActionForward accionDetalle(ActionMapping mapping, UsuarioBasico usuario, Connection con, GeneracionAnexosForecatForm generacionForm) 
	{
		GeneracionAnexosForecat generacion=new GeneracionAnexosForecat();
		this.llenarMundo(generacionForm,generacion,usuario);
		
		//se consulta el contenido del archivo
		generacionForm.setContenidoArchivo(
				generacion.cargarArchivo(
						con,
						usuario.getCodigoInstitucionInt(),
						generacionForm.getArchivo()
									)
						);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para generar los archivos FORECAT
	 * @param con
	 * @param generacionForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGenerar(Connection con, GeneracionAnexosForecatForm generacionForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		//manejo de errores
		ActionErrors errores = new ActionErrors();
		
		generacionForm.setGeneracion(false); //se reseteta el estado de generacion
		
		//Instanciación del mundo de FORECAT
		GeneracionAnexosForecat generacion=new GeneracionAnexosForecat();
		//llenar parámetros de generación forecat
		this.llenarMundo(generacionForm,generacion,usuario);
		
		logger.info("PASÓ POR AQUI EN LA GENERACIÓN DE ARCHIVO DESDE AL ACTION\nfechaInicial=> "+generacion.getFechaInicial()+", fechaFinal=> "+generacion.getFechaFinal()+", numeroRemision=> "+generacion.getNumeroRemision()+", generacion=> "+generacion.getTipoCodigo());
		//llamado a método del mundo para generar los archivos FORECAT
		generacionForm.setResultados(generacion.generarArchivos(con,usuario.getCodigoInstitucionInt()));
		
		generacionForm.setNumeroRemision(generacion.getNumeroRemision());
		UtilidadBD.closeConnection(con);
		
		//se verifica si se encontraron registros en la generación FORECAT
		if(generacion.isHuboRegistros())
		{
			//Se verifica si hubo errores en la generación de FORECAT
			if(generacionForm.getResultados("error")!=null)
			{
				errores.add("Error al generar Forecat",new ActionMessage(generacionForm.getResultados("error").toString()));
				saveErrors(request,errores);	
			}
			else
			{
				generacionForm.setHuboInconsistencias(generacion.isHuboInconsistencias());
				generacionForm.setPathGeneracion(generacion.getPathGeneracion());
				generacionForm.setGeneracion(true); //se pasa al estado de generación				
				this.generarLog(generacionForm,usuario);
				//Se imprimen los anexos
				this.imprimirAnexos(generacionForm,generacion,request);
			}
		}
		else
		{
			errores.add("no se encontró información",new ActionMessage("errors.notEspecific","No existen Facturas para el período seleccionado. Por favor verifique"));
			saveErrors(request,errores);
		}
		
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Método implementado para la impresión de los anexos
	 * @param generacionForm
	 * @param generacion
	 * @param request 
	 */
	private void imprimirAnexos(GeneracionAnexosForecatForm generacionForm, GeneracionAnexosForecat generacion, HttpServletRequest request) 
	{
		
		//******************************************************************************************
		//*****************IMPRESION DE LOS ANEXOS 1************************************************
		//******************************************************************************************
		String nombreRptDesign = "AnexosForecat1.rptdesign";
		
        DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        //se mandan los parámetros al rreporte
        newPathReport += "&codigoInstitucion="+generacion.getInstitucion().getCodigo()+
        	"&fechaInicial="+UtilidadFecha.conversionFormatoFechaABD(generacionForm.getFechaInicial())+
        	"&fechaFinal="+UtilidadFecha.conversionFormatoFechaABD(generacionForm.getFechaFinal())+
        	"&convenio="+generacion.getConvenio()+
        	"&salario="+generacion.getSalarioMinimo();
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
        
        //******************************************************************************************
		//*****************IMPRESION DE LOS ANEXOS 2************************************************
		//******************************************************************************************
		nombreRptDesign = "AnexosForecat2.rptdesign";
		
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        //se mandan los parámetros al rreporte
        newPathReport += "&razonSocial="+generacion.getInstitucion().getRazonSocial()+
        	"&fechaInicial="+UtilidadFecha.conversionFormatoFechaABD(generacionForm.getFechaInicial())+
        	"&fechaFinal="+UtilidadFecha.conversionFormatoFechaABD(generacionForm.getFechaFinal())+
        	"&convenio="+generacion.getConvenio()+
        	"&salario="+generacion.getSalarioMinimo();
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport2", newPathReport);
        }
        
		
	}

	/**
	 * Método para hacer llamado al log tipo archivo
	 * @param generacionForm
	 * @param usu
	 */
	private void generarLog(GeneracionAnexosForecatForm generacionForm, UsuarioBasico usu) 
	{
		
		String log;
		log="\n            ====ARCHIVOS FORECAT GENERADOS===== ";
		
			if(generacionForm.getResultados(ConstantesBD.forecatAA)!=null)
				log+="\n "+ConstantesBD.forecatAA+generacionForm.getNumeroRemision()+".txt";
			if(generacionForm.getResultados(ConstantesBD.forecatAC)!=null)
				log+="\n "+ConstantesBD.forecatAC+generacionForm.getNumeroRemision()+".txt";
			if(generacionForm.getResultados(ConstantesBD.forecatAV)!=null)
				log+="\n "+ConstantesBD.forecatAV+generacionForm.getNumeroRemision()+".txt";
			if(generacionForm.getResultados(ConstantesBD.forecatVH)!=null)
				log+="\n "+ConstantesBD.forecatVH+generacionForm.getNumeroRemision()+".txt";
			
			//archivo inconsistencias
			if(generacionForm.getResultados(ConstantesBD.ripsInconsistencias)!=null)
				log+="\n "+ConstantesBD.ripsInconsistencias+generacionForm.getNumeroRemision()+".txt";
			
		
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logGeneracionAnexosForecatCodigo, log, ConstantesBD.tipoRegistroLogInsercion,usu.getLoginUsuario());
	}
	

	/**
	 * Método implementado para cargar los datos de la forma al mundo de Generacion Anexos Forecat
	 * @param generacionForm
	 * @param generacion
	 * @param usuario 
	 */
	private void llenarMundo(GeneracionAnexosForecatForm generacionForm, GeneracionAnexosForecat generacion, UsuarioBasico usuario) 
	{
		generacion.setConvenio(Integer.parseInt(ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt())));
		generacion.setTipoCodigo(Integer.parseInt(generacionForm.getTipoManual()));
		generacion.setFechaInicial(generacionForm.getFechaInicial());
		generacion.setFechaFinal(generacionForm.getFechaFinal());
		generacion.setFechaRemision(generacionForm.getFechaRemision());
		generacion.setNumeroRemision(generacionForm.getNumeroRemision());
		
	}

	/**
	 * Método que inicia el flujo de la generacion de los archivos FORECAT
	 * @param con
	 * @param mapping
	 * @param generacionForm
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, GeneracionAnexosForecatForm generacionForm) 
	{
		generacionForm.reset();
		
		//Se cargan los tipos de manuales
		generacionForm.setTiposManuales(UtilidadesFacturacion.cargarTarifariosOficiales(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
