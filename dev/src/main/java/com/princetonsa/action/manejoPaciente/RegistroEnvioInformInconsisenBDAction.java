package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
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

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInformInconsisenBDForm;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInformInconsisenBD;
import com.princetonsa.pdf.InconsistenciasBDPdf;

public class RegistroEnvioInformInconsisenBDAction extends Action
{
	Logger logger = Logger.getLogger(RegistroEnvioInformInconsisenBDAction.class);
	RegistroEnvioInformInconsisenBD mundo;
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {	
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof RegistroEnvioInformInconsisenBDForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{ 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd"); 
					logger.info("NO PUDO EMPEZAR PAGINA REGISTRO ENVIO........>>>>  ");
					return mapping.findForward("paginaError");

				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");


				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo= new RegistroEnvioInformInconsisenBD();
				RegistroEnvioInformInconsisenBDForm forma = (RegistroEnvioInformInconsisenBDForm)form;		
				String estado = forma.getEstado();
				logger.info("ESTADO=>"+estado);

				if(estado.equals("empezar"))
				{
					logger.info("ENTRO a EMPEZAR DEL INFORME........>>>>  ");
					forma.reset();
					forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
					ActionForward forward = new ActionForward();
					forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
					if(forward != null)
						return forward;	  	
					return cargarIngresosPaciente(con,forma,usuario,paciente,request, mapping);		
				}

				else if(estado.equals("menuPrincipal"))
				{   
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuPrincipal");
				}
				else if(estado.equals("cargarDatosReporte"))
				{
					if(Utilidades.convertirAEntero(forma.getIngresosPaciente().get("numRegistros").toString())==0)
					{
						forma.setNomPaciente(paciente.getCodigoTipoIdentificacionPersona() +"  "+ paciente.getNombrePersona());
						forma.setCuenta(paciente.getCodigoCuenta()+"");
						forma.setCodigoIngreso(paciente.getConsecutivoIngreso());
					}
					return cargarDatosInforme(con,forma,usuario,paciente,request, mapping);
				} 
				else if(estado.equals("menuInformeConvenio"))
				{	 
					forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
					if(cargarConveniosResponsables(con,forma,usuario,paciente,request, mapping)>0)
					{

						if(Utilidades.convertirAEntero(forma.getIngresosPaciente().get("numRegistros").toString())==0)
						{
							forma.setNomPaciente(paciente.getCodigoTipoIdentificacionPersona() +"  "+ paciente.getNombrePersona());
							forma.setCuenta(paciente.getCodigoCuenta()+"");
							forma.setCuentaFiltro(paciente.getCodigoCuenta()+"");
							forma.setIngresoFiltro(paciente.getCodigoIngreso()+"");
							forma.setViaIngresoFiltro(paciente.getCodigoUltimaViaIngreso()+"");
							forma.setCodigoIngreso(paciente.getConsecutivoIngreso());
						}
						else
						{	
							forma.setCuentaFiltro(forma.getIngresosPaciente().get("numcuenta_"+forma.getPosIngreso()).toString());
						}

						if(Utilidades.convertirAEntero(forma.getConveniosResponsables().get("numRegistros").toString())==1)
						{    			
							forma.setConvenioFiltro(forma.getConveniosResponsables().get("codconvenio_0")+ConstantesBD.separadorSplit+forma.getConveniosResponsables().get("codresponsable_0"));
							forma.setSubCuentaFiltro(forma.getConveniosResponsables().get("codresponsable_0").toString());
							cargarInformeInconsistencias(con,forma,usuario,paciente,request, mapping);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("menuInformeInconsistencias");
						}

						UtilidadBD.closeConnection(con);
						return mapping.findForward("menuInformeConvenios");
					}
					UtilidadBD.closeConnection(con);
				}

				else if(estado.equals("menuInformeInconsistencias"))
				{	  
					if(Utilidades.convertirAEntero(forma.getIngresosPaciente().get("numRegistros").toString())==0)
					{	
						forma.setNomPaciente(paciente.getCodigoTipoIdentificacionPersona() +"  "+ paciente.getNombrePersona());
						forma.setCuenta(paciente.getCodigoCuenta()+"");
						forma.setCuentaFiltro(paciente.getCodigoCuenta()+"");
						forma.setIngresoFiltro(paciente.getCodigoIngreso()+"");
						forma.setViaIngresoFiltro(paciente.getCodigoUltimaViaIngreso()+"");
						forma.setCodigoIngreso(paciente.getConsecutivoIngreso());
					}
					forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);  
					cargarInformeInconsistencias(con,forma,usuario,paciente,request, mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuInformeInconsistencias");
				}

				else if(estado.equals("generarInforme"))
				{
					return accionGenerarInforme(con,forma,usuario,paciente,request, mapping);
				}
				else if (estado.equals("enviarInforme"))
				{
					return accionEnviarInforme(con,forma,usuario,paciente,request, mapping);
				}

				else if(estado.equals("filtroCiudades"))
				{
					return accionFiltrarCiudades(con,forma,response);  
				}else if(estado.equals("imprimir"))
				{
					logger.info("Esta entrando a la imprension");
					return accionImprimir(con,mapping,usuario,paciente,request,forma.getInformeIconsistencias().getCodigoPk(),forma);
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

	
	private ActionForward cargarDatosInforme(Connection con,RegistroEnvioInformInconsisenBDForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request,ActionMapping mapping) {
		
		forma.setCodigoPkInforme(forma.getConveniosResponsables().get("codigoinforme_"+forma.getPosConvenio()).toString());
    	forma.setIngresoFiltro(forma.getIngresosPaciente().get("numingreso_"+forma.getPosIngreso()).toString());
    	forma.setConvenioFiltro(forma.getConveniosResponsables().get("codconvenio_"+forma.getPosConvenio())+ConstantesBD.separadorSplit+forma.getConveniosResponsables().get("codresponsable_"+forma.getPosConvenio())+"");
    	forma.setSubCuentaFiltro(forma.getConveniosResponsables().get("codresponsable_"+forma.getPosConvenio()).toString());
        forma.setViaIngresoFiltro(forma.getIngresosPaciente().get("viaingreso_"+forma.getPosIngreso()).toString());
        forma.setCuentaFiltro(forma.getIngresosPaciente().get("numcuenta_"+forma.getPosIngreso()).toString());
    	forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);  
    	
    	cargarInformeInconsistencias(con,forma,usuario,paciente,request, mapping);
    	UtilidadBD.closeConnection(con);
  	    return mapping.findForward("menuInformeInconsistencias");
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
	private ActionForward accionImprimir(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, int cod_informe_inco,RegistroEnvioInformInconsisenBDForm forma) 
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
			int cod_informe_inco, RegistroEnvioInformInconsisenBDForm forma) {
		
		String  archivoXMLReport = InconsistenciasBDPdf.xmlInformeInconsistenciaBD(con, usuario, request, paciente, cod_informe_inco, forma);
		logger.info("Archivo xml Generado: "+archivoXMLReport);
		return archivoXMLReport;
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
private ActionForward accionValidarPaciente(Connection con,RegistroEnvioInformInconsisenBDForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}
		
		return null;
		
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
  private ActionForward cargarIngresosPaciente(Connection con,RegistroEnvioInformInconsisenBDForm form, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request, ActionMapping mapping)
   { 
	  form.reset();
	  form.setIngresosPaciente(mundo.consultarIngresosPaciente(con, usuario.getCodigoInstitucionInt(),paciente.getCodigoPersona()));
	  Utilidades.imprimirMapa(form.getIngresosPaciente());
	  UtilidadBD.closeConnection(con);
	  return mapping.findForward("principal");
   }

  /**
   * 
   * @param con
   * @param forma
   * @param usuario
   * @param paciente
   * @param request
   * @param mapping
   * @return
   */
  private ActionForward accionGenerarInforme(Connection con,RegistroEnvioInformInconsisenBDForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request,ActionMapping mapping) {
		
        
	    DtoInformeInconsisenBD informe=new DtoInformeInconsisenBD(); 	  
        HashMap datosPaciente=new HashMap();
        HashMap resultado = new HashMap();
        String auxEstadoInicial=new String("");
        String pathArchivoIncoxml = "";
        auxEstadoInicial=forma.getInformeIconsistencias().getEstadoInforme();
        datosPaciente.put("primernombre",paciente.getPrimerNombre());
        datosPaciente.put("segundonombre",paciente.getSegundoNombre());
        datosPaciente.put("primerapellido",paciente.getPrimerApellido());
        datosPaciente.put("segundoapellido",paciente.getSegundoApellido());
        datosPaciente.put("tipodocumento",paciente.getTipoIdentificacionPersona(false));
        datosPaciente.put("numeroidentificacion",paciente.getNumeroIdentificacionPersona());
        datosPaciente.put("fechanacimiento",paciente.getFechaNacimiento());
        forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
        String[] valueConvenio= forma.getConvenioFiltro().split(ConstantesBD.separadorSplit);
  	    String codSubcuenta=valueConvenio[1];
  	    String codConvenio=valueConvenio[0];
        
        logger.info("EL estado del informe cuando se va a generar es antes: >> "+forma.getInformeIconsistencias().getEstadoInforme());
        logger.info("La confirmacion de envio esta en : >> "+forma.getConfirmarEnvio());
        logger.info("El codigo del Informe es: >> "+forma.getInformeIconsistencias().getCodigoPk());
        logger.info("El codigo del Ingreso es : >> "+forma.getIngresoFiltro());
        
        if(forma.getConfirmarEnvio().equals(ConstantesBD.acronimoSi))
           {
        	informe.setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoEnviado);
           }
        else
           {
        	informe.setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoGenerado);
           }
        
        logger.info("EL estado del informe cuando se va a generar es despues: >> "+informe.getEstadoInforme());
      
            informe.setCodigoPk(forma.getInformeIconsistencias().getCodigoPk());
        	informe.setPrimerNombre(forma.getInformeIconsistencias().getPrimerNombre());  
            informe.setSegundoNombre(forma.getInformeIconsistencias().getSegundoNombre());
            informe.setPrimerApellido(forma.getInformeIconsistencias().getPrimerApellido());
	        informe.setSegundoApellido(forma.getInformeIconsistencias().getSegundoApellido());
	        informe.setTipoIdentificacion(forma.getInformeIconsistencias().getTipoIdentificacion());
	        informe.setIdPersona(forma.getInformeIconsistencias().getIdPersona());
	        informe.setFechaNacimiento(forma.getInformeIconsistencias().getFechaNacimiento());
	        informe.setDireccionResidencia(forma.getInformeIconsistencias().getDireccionResidencia());
	        informe.setTelefono(forma.getInformeIconsistencias().getTelefono());
	        informe.setDepartamento(forma.getInformeIconsistencias().getDepartamento());
	        informe.setCodigoDepartamento(forma.getInformeIconsistencias().getCodigoDepartamento());
	        informe.setMunicipio(forma.getInformeIconsistencias().getMunicipio());
	        informe.setCodigoMunicipio(forma.getInformeIconsistencias().getCodigoMunicipio());
	        informe.setCobertura(forma.getInformeIconsistencias().getCobertura());
	        informe.setCodCobertura(forma.getInformeIconsistencias().getCodCobertura());
	        informe.setVariablesInco(forma.getInformeIconsistencias().getVariablesInco());
	        informe.setObservaciones(forma.getInformeIconsistencias().getObservaciones());
	        informe.setEntidad(forma.getInformeIconsistencias().getEntidad());
	        informe.setCodigoEntidad(forma.getInformeIconsistencias().getCodigoEntidad());
	        informe.setMedioEnvio(forma.getInformeIconsistencias().getMedioEnvio());
	        informe.setEstadoInforme(forma.getInformeIconsistencias().getEstadoInforme());  
	        
	        informe.setCodigoPaciente(forma.getInformeIconsistencias().getCodigoPaciente());
	        informe.setCodigoConvenio(forma.getInformeIconsistencias().getCodigoConvenio());
	        informe.setDescripcionConvenio(forma.getInformeIconsistencias().getDescripcionConvenio());
	        informe.setCodigoViaIngreso(Utilidades.convertirAEntero(forma.getViaIngresoFiltro()));
	        informe.setCodigoCuenta(forma.getInformeIconsistencias().getCodigoCuenta());
	        informe.setTipoInconsistencia(forma.getInformeIconsistencias().getTipoInconsistencia());
	        informe.setHistorialEnvios(forma.getInformeIconsistencias().getHistorialEnvios());
	        informe.setFiltroVariable(forma.getFiltroTipoInconsistencia());
	        informe.setSubcuenta(forma.getInformeIconsistencias().getSubcuenta());
	        
	        logger.info(" \n \n EL filtro variable viene asi  >> " + forma.getFiltroTipoInconsistencia());
	        
	        logger.info(" \n \n La entidad viene asi  >> " + forma.getInformeIconsistencias().getEntidad());
	        
	        
	        UtilidadBD.iniciarTransaccion(con);
	        
	        if(forma.getConfirmarEnvio().equals(ConstantesBD.acronimoNo))
	        {
	        	informe.setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoGenerado); 
	            if(forma.getInformeIconsistencias().getEstadoInforme().equals(""))
	            {
	            	resultado = mundo.insertarInformeInconsistencia(con,informe,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario(),forma.getIngresoFiltro(),codConvenio,codSubcuenta,datosPaciente);	 
	               	forma.getInformeIconsistencias().setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoGenerado);
	               	forma.getInformeIconsistencias().setCodigoPk(Utilidades.convertirAEntero(resultado.get("codigoPk").toString()));
	            }
	            else if(forma.getInformeIconsistencias().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
	    	    {
	            	resultado = mundo.modificarInformeInconsistencia(con,informe,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario(),forma.getIngresoFiltro(),codConvenio,datosPaciente); 
	            }
           }
	       else if(forma.getConfirmarEnvio().equals(ConstantesBD.acronimoSi))
	       {
    		    informe.setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoEnviado);
    		    
    		    if(forma.getInformeIconsistencias().getEstadoInforme().trim().equals(""))
                {
	    	        resultado=mundo.insertarInformeInconsistencia(con,informe,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario(),forma.getIngresoFiltro(),codConvenio,codSubcuenta,datosPaciente);	 
	                forma.getInformeIconsistencias().setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoEnviado);
	                forma.getInformeIconsistencias().setCodigoPk(Utilidades.convertirAEntero(resultado.get("codigoPk").toString()));
	                
	                //********************************************************************************************************************
        	        // Generacion del archivo XML de Inconsitencias
	                if(forma.getInformeIconsistencias().getCodigoPk()>0)
	                {
	        	        if(forma.getInformeIconsistencias().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
	        	        {
	        	        	if((pathArchivoIncoxml=this.xmlReportInconsitencias(con, usuario, paciente, request, forma.getInformeIconsistencias().getCodigoPk(), forma))!=null){
	        	        		informe.setPathArchivoIncoXml(pathArchivoIncoxml);
	        	        		informe.setArchivoInconGenerado(ConstantesBD.acronimoSi);
	        	        	}else
	        	        		informe.setArchivoInconGenerado(ConstantesBD.acronimoSi);
	        	        }else
	        	        	informe.setArchivoInconGenerado(ConstantesBD.acronimoSi);
	        	        mundo.actualizarPatharchivoIncoBD(con, pathArchivoIncoxml, forma.getInformeIconsistencias().getCodigoPk());
	                }
        	        // Fin Generacion del archivo XML de Inconsitencias
        	        //********************************************************************************************************************
        	        
                }
                else if (forma.getInformeIconsistencias().getEstadoInforme().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado) )
    	        {                     	
                	//********************************************************************************************************************
        	        // Generacion del archivo XML de Inconsitencias
        	        if(forma.getInformeIconsistencias().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
        	        {
        	        	if((pathArchivoIncoxml=this.xmlReportInconsitencias(con, usuario, paciente, request, forma.getInformeIconsistencias().getCodigoPk(), forma))!=null){
        	        		informe.setPathArchivoIncoXml(pathArchivoIncoxml);
        	        		informe.setArchivoInconGenerado(ConstantesBD.acronimoSi);
        	        	}else
        	        		informe.setArchivoInconGenerado(ConstantesBD.acronimoSi);
        	        }else
        	        	informe.setArchivoInconGenerado(ConstantesBD.acronimoSi);
        	        // Fin Generacion del archivo XML de Inconsitencias
        	        //********************************************************************************************************************
        	        
    	           resultado=mundo.modificarInformeInconsistencia(con,informe,usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario(),forma.getIngresoFiltro(),codConvenio,datosPaciente);
    	           forma.getInformeIconsistencias().setEstadoInforme(ConstantesIntegridadDominio.acronimoEstadoEnviado);
                }   		    
    	   }
	        
	       
	        
          if(Utilidades.convertirAEntero(resultado.get("codigoPk").toString())>0)
		  {
        	  if(forma.getConfirmarEnvio().equals(ConstantesBD.acronimoSi))
        	  {
        		  forma.setEstadoEnviado(ConstantesBD.acronimoSi);
        		  forma.getInformeIconsistencias().setHistorialEnvios(mundo.consultaHistoricosInforme(con,  Utilidades.convertirAEntero(resultado.get("codigoPk").toString())));
        	  }
        	  forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
        	  forma.setParametrosFiltros("mensaje","Informe Generado con Exito");
        	  forma.getInformeIconsistencias().setVariablesInco(mundo.consultarVariablesIncorrectasInforme(con, Utilidades.convertirAEntero(resultado.get("codigoPk").toString())));
			  
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
		     return mapping.findForward("menuInformeInconsistencias");
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
  private ActionForward accionEnviarInforme(Connection con,RegistroEnvioInformInconsisenBDForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request,ActionMapping mapping)
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
       	       if(forma.getConfirmarEnvio().equals(ConstantesBD.acronimoSi))
       	         {
       		     forma.setEstadoEnviado(ConstantesBD.acronimoSi);
       	         } 
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
	    return mapping.findForward("menuInformeInconsistencias");
	  
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
  private int cargarConveniosResponsables(Connection con,RegistroEnvioInformInconsisenBDForm form, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request, ActionMapping mapping)
  {
	  logger.info("EL ingreso es >> " + form.getIngresoFiltro());
	  form.setConveniosResponsables(mundo.consultarConveniosResponsables(con,Utilidades.convertirAEntero(form.getIngresoFiltro().toString())));
	  
	  Utilidades.imprimirMapa(form.getConveniosResponsables());
	  return 1;
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
  private void cargarInformeInconsistencias(Connection con,RegistroEnvioInformInconsisenBDForm form, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request, ActionMapping mapping)
  {	 
	  HashMap convenio = new HashMap(); 
	  logger.info("EL VALOR DEL filtro CONVENIO >> "+form.getConvenioFiltro());
	  String[] valueConvenio= form.getConvenioFiltro().split(ConstantesBD.separadorSplit);
	  String codSubcuenta=valueConvenio[1];
	  String codConvenio=valueConvenio[0];
	  
	  if(mundo.consultarEstadoEnvioInforme(con, form.getIngresoFiltro(), codSubcuenta)==1)
	  {
	    form.setEstadoEnviado(ConstantesBD.acronimoSi);
	  }
	  else
	  {
		  form.setEstadoEnviado(ConstantesBD.acronimoNo);
	  }
	  form.setInformeIconsistencias(mundo.cargarInformeIncosistencias(con, form.getIngresoFiltro(), usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona(),codConvenio,codSubcuenta,form.getCuentaFiltro()));	  
	  logger.info("EL Codigo del Convenio es >>> "+form.getInformeIconsistencias().getCodigoConvenio());
	  convenio.put("codigo",form.getInformeIconsistencias().getCodigoConvenio());
	  convenio.put("descripcion",form.getInformeIconsistencias().getDescripcionConvenio());
	  convenio.put("esConvenio",ConstantesBD.acronimoSi);
	 
	  form.setArrayEmpresas(Utilidades.obtenerEmpresas(con,usuario.getCodigoInstitucionInt(),true));
	  form.getArrayEmpresas().add(convenio);
	  
	  form.setListadoArrayMediosEnvio(Convenio.cargarMediosEnvio(con, form.getInformeIconsistencias().getCodigoConvenio()));
	  logger.info("EL tamaño del arreglo de medios de envio es >>> "+form.getListadoArrayMediosEnvio().size());
	  form.setMapVariablesIncorrectas(mundo.cargarVariablesIncorrectas(con));
	  form.setArrayTiposIdentificacion(mundo.cargarTiposDocumentos(con));
	  form.setTiposInconsistencias(mundo.cargarTiposInconsistencias(con, usuario.getCodigoInstitucionInt()));  
	  
	  if(form.getInformeIconsistencias().getCodCobertura()<0)
	    {
		  form.setCoberturasSalud(mundo.consultarCoberturasSaludPaciente(con, form.getInformeIconsistencias().getCodigoConvenio()));
		  logger.info("las Coberturas son :>> \n"+form.getCoberturasSalud());
        }
	 
	  
  }
  
  
  
  /**
	 * Método para filtrar las Ciudades al cambiar el Departamento
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarCiudades(Connection con,RegistroEnvioInformInconsisenBDForm form,HttpServletResponse response) 
	{
		logger.info(" >>>  ENTRO A FILTRAR CIUDADES    ");
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>nomMunicipio</id-select>" +
				"<id-arreglo>ciudades</id-arreglo>" +
			"</infoid>" ;
		
		form.setCiudades(mundo.cargarCiudadesDepto(con, form.getInformeIconsistencias().getCodigoDepartamento()));
					
		
		for(int i=0; i< Utilidades.convertirAEntero(form.getCiudades().get("numRegistros")+"");i++)
		{
			resultado += "<ciudades>";
				resultado += "<codigo>"+form.getCiudades().get("codigociudad_"+i)+"</codigo>";
				resultado += "<descripcion>"+form.getCiudades().get("nombreciudad_"+i)+"</descripcion>";
			resultado += "</ciudades>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarCiudades: "+e);
		}
		return null;
	}
  
  
  
}	