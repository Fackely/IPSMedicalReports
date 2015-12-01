package com.princetonsa.action.manejoPaciente;

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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.ConsultaEnvioInconsistenciasenBDForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.manejoPaciente.ConsultaEnvioInconsistenciasenBD;
import com.princetonsa.pdf.InconsistenciasBDPdf;






public class ConsultaEnvioInconsistenciasenBDAction extends Action {

	Logger logger = Logger.getLogger(ConsultaEnvioInconsistenciasenBDAction.class);
	ConsultaEnvioInconsistenciasenBD mundo;	
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {	

		Connection con = null;
		try{
			if(response == null);

			if (form instanceof ConsultaEnvioInconsistenciasenBDForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{ 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd"); 
					logger.info("NO PUDO EMPEZAR PAGINA CONSULTA ENVIO INCONSISTENCIAS ........>>>>  ");
					return mapping.findForward("paginaError");

				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");


				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo= new ConsultaEnvioInconsistenciasenBD();
				ConsultaEnvioInconsistenciasenBDForm forma = (ConsultaEnvioInconsistenciasenBDForm)form;		
				String estado = forma.getEstado(); 

				if(estado.equals("empezar"))
				{
					logger.info("ENTRO a EMPEZAR DE CONSULTA INFORME........>>>>  ");
					forma.reset();

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal"); 	

				}else
					if(estado.equals("consultarPaciente"))
					{  
						forma.reset();
						errores = empezarPaciente(con,forma,usuario,paciente,request);

						if(!errores.isEmpty())
						{
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						}

						return cargarIngresosPaciente(con,forma,usuario,paciente,request, mapping);
					}
					else
						if(estado.equals("verInformeInconsistencias"))
						{
							forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);  
							logger.info("EL codigo del convenio en el action es  >> " +forma.getConvenioFiltro());
							cargarInformeInconsistencias(con,forma,usuario,paciente,request, mapping);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("informeInconsistencias");
						}
						else
							if(estado.equals("enviarInforme"))
							{
								return accionEnviarInforme(con,forma,usuario,paciente,request, mapping);

							}else
								if(estado.equals("consultarRango")){

									return accionEmpezarRango(con,forma,usuario,request,mapping);

								}else
									if(estado.equals("buscarRango"))
									{
										estadoBuscarRango(con,forma,request,usuario);
										UtilidadBD.closeConnection(con);
										return mapping.findForward("consultaRango");
									}else if(estado.equals("imprimir")){
										logger.info("Esta entrando a la imprension");
										return accionImprimir(con,mapping,usuario,paciente,request,forma.getInformeIconsistencias().getCodigoPk());
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
	 * Método implementado para realizar la impresión de inconsistencias en la BD
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param id_info_inco
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, int cod_informe_inco) 
	{
    	request.setAttribute("nombreArchivo",InconsistenciasBDPdf.pdfInconsistenciasBD(con, usuario, request, paciente, cod_informe_inco));
    	request.setAttribute("nombreVentana", "Inconsistencias BD");
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("abrirPdf");
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param cod_informe_inco
	 * @param forma
	 * @return
	 */
	private String xmlReportInconsitencias(Connection con, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request,
			int cod_informe_inco, ConsultaEnvioInconsistenciasenBDForm forma) {
		
		// falta generar este archivo en un .zip
		
		String  archivoXMLReport = InconsistenciasBDPdf.xmlInformeInconsistenciaBD(con, usuario, request, paciente, cod_informe_inco, forma);
		logger.info("Archivo xml Generado: "+archivoXMLReport);
		return archivoXMLReport;
	} 
	
	private ActionForward accionEmpezarRango(Connection con,ConsultaEnvioInconsistenciasenBDForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		    
		forma.setListadoConvenio(mundo.consultaConvenios(con));	
		forma.setParametrosBusqueda(mundo.inicializarParametrosBusquedaRango());
		UtilidadBD.closeConnection(con);
  	    return mapping.findForward("consultaRango");
	}

	
	/**
	 * Operaciones del estado busqueda por rango
	 * @param Connection con
	 * @param RegistroEnvioInfAtencionIniUrgForm forma
	 * @param HttpServletRequest request
	 * */
	public void estadoBuscarRango(Connection con, ConsultaEnvioInconsistenciasenBDForm forma,HttpServletRequest request,UsuarioBasico usuario)
	{
		 logger.info("ENTRO A REALIZAR LA BUSQUEDA POR RANGO........>>>>  ");
		//Validaciones para la busqueda por rango
		ActionErrors errores = new ActionErrors();
		errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());		
		
		if(errores.isEmpty())
		{
			forma.setListadoArray(mundo.getListadoInformeInconsistencias(con, 
						forma.getParametrosBusqueda("fechaInicialGeneracion").toString(),
						forma.getParametrosBusqueda("fechaFinalGeneracion").toString(),
						forma.getParametrosBusqueda("fechaInicialEnvio").toString(), 
						forma.getParametrosBusqueda("fechaFinalEnvio").toString(),
						forma.getParametrosBusqueda("estadoEnvio").toString(), 
						forma.getParametrosBusqueda("convenio").toString(),
						usuario.getCodigoInstitucionInt()));
			
		}
		else
		{		
			saveErrors(request, errores);			
		}
	}	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	public ActionErrors empezarPaciente(Connection con,ConsultaEnvioInconsistenciasenBDForm forma,UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();		
		errores = ConsultaEnvioInconsistenciasenBD.validarPaciente(con,paciente,usuario.getCodigoInstitucionInt());
		
		if(!errores.isEmpty())	
			return errores;				
		
		return errores;
	}	
	
	
	/**
	 * 
	 * @param con
	 * @param form
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarIngresosPaciente(Connection con,ConsultaEnvioInconsistenciasenBDForm form, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request, ActionMapping mapping)
	   { 
		  form.reset();
		  form.setIngresosPaciente(mundo.consultarIngresosPaciente(con, usuario.getCodigoInstitucionInt(),paciente.getCodigoPersona()));
		  Utilidades.imprimirMapa(form.getIngresosPaciente());
		  UtilidadBD.closeConnection(con);
		  return mapping.findForward("consultaPaciente");
	   }
	
	/**
	 * 
	 * @param con
	 * @param form
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 */
	private void cargarInformeInconsistencias(Connection con,ConsultaEnvioInconsistenciasenBDForm form, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request, ActionMapping mapping)
	  {	 
		  HashMap convenio = new HashMap(); 
		  String[] valueConvenio= form.getConvenioFiltro().split(ConstantesBD.separadorSplit);
		  String codSubcuenta=valueConvenio[1];
		  String codConvenio=valueConvenio[0];
		  
		  form.setInformeIconsistencias(mundo.cargarInformeIncosistencias(con, form.getIngresoFiltro(), usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona(),codConvenio,codSubcuenta));	  
		  logger.info("EL Codigo del Convenio es >>> "+form.getInformeIconsistencias().getCodigoConvenio());
		  
			  if(form.getInformeIconsistencias().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoEnviado))
			   {
			      form.setEstadoEnviado(ConstantesBD.acronimoSi);
			   }   
			   else
			   {
				   form.setEstadoEnviado(ConstantesBD.acronimoNo);
			   }
			   
		  convenio.put("codigo",form.getInformeIconsistencias().getCodigoConvenio());
		  convenio.put("descripcion",form.getInformeIconsistencias().getDescripcionConvenio());
		  convenio.put("esConvenio",ConstantesBD.acronimoSi);
		 
		  form.setArrayEmpresas(Utilidades.obtenerEmpresas(con,usuario.getCodigoInstitucionInt(),true));
		  form.getArrayEmpresas().add(convenio);
		  form.setListadoArrayMediosEnvio(Convenio.cargarMediosEnvio(con, form.getInformeIconsistencias().getCodigoConvenio()));
		  logger.info("EL tamaño del arreglo de medios de envio es >>> "+form.getListadoArrayMediosEnvio().size());

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
	private ActionForward accionEnviarInforme(Connection con,ConsultaEnvioInconsistenciasenBDForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request,ActionMapping mapping)
	  {
		    HashMap datosEnvio=new HashMap();
	        HashMap resultado = new HashMap();
	        String auxEstadoInicial=new String("");
	        String[] valueConvenio= forma.getConvenioFiltro().split(ConstantesBD.separadorSplit);
			String codConvenio=valueConvenio[0];
			String pathArchivoIncoxml = "";
	        forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
	        auxEstadoInicial=forma.getInformeIconsistencias().getEstadoInforme(); 
	        
	        //********************************************************************************************************************
	        // Generacion del archivo XML de Inconsitencias
	        if(forma.getInformeIconsistencias().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
	        	if((pathArchivoIncoxml=this.xmlReportInconsitencias(con, usuario, paciente, request, forma.getInformeIconsistencias().getCodigoPk(), forma))==null)
	        		pathArchivoIncoxml = " ";
	        // Fin Generacion del archivo XML de Inconsitencias
	        //********************************************************************************************************************
	        
	        datosEnvio.put("usuario",usuario.getLoginUsuario());
	        datosEnvio.put("codInforme",forma.getInformeIconsistencias().getCodigoPk());
	        datosEnvio.put("convenio", forma.getInformeIconsistencias().getEntidad().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoSi)?forma.getInformeIconsistencias().getEntidad().split(ConstantesBD.separadorSplit)[0]:"");
	        datosEnvio.put("empresa", forma.getInformeIconsistencias().getEntidad().split(ConstantesBD.separadorSplit)[1].equals(ConstantesBD.acronimoNo)?forma.getInformeIconsistencias().getEntidad().split(ConstantesBD.separadorSplit)[0]:"");
	        datosEnvio.put("medioenvio",forma.getInformeIconsistencias().getMedioEnvio());
	        datosEnvio.put("patharchivo",pathArchivoIncoxml);
	           
	        UtilidadBD.iniciarTransaccion(con);
	               
	            resultado= mundo.insertarEnvioInformeInconsistencias(con,datosEnvio);
	            if(Utilidades.convertirAEntero(resultado.get("codigoPk").toString())>0)
			     {
	       		  forma.setEstadoEnviado(ConstantesBD.acronimoSi);
	       		  mundo.actualizarEstadoEnvioInforme(con,Utilidades.convertirAEntero(resultado.get("codigoPk").toString()));
	       	      forma.getInformeIconsistencias().setHistorialEnvios(mundo.consultaHistoricosInforme(con,  Utilidades.convertirAEntero(resultado.get("codigoPk").toString())));   
		       	  forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
		       	  forma.setParametrosFiltros("mensaje","Informe Enviado con Exito");
			      UtilidadBD.finalizarTransaccion(con);
				  }	 
	              else
	 		        {
	 			     UtilidadBD.abortarTransaccion(con);
	 			       if(auxEstadoInicial.equals(""))
	 			       {	  
	 			    	   forma.getInformeIconsistencias().setEstadoInforme("");
	 			       }
	 			       forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
	 			       ActionErrors errores = (ActionErrors)resultado.get("error");
	 			       saveErrors(request, errores);			
	 		        }
		     
	        UtilidadBD.closeConnection(con);
		    return mapping.findForward("informeInconsistencias");
	  }
	
	
}
