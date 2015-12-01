package com.princetonsa.action.glosas;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.glosas.ConsultarImprimirGlosasSinRespuestaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConsultarImprimirGlosasSinRespuesta;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultarImprimirGlosasSinRespuestaAction extends Action {

	
Logger logger=Logger.getLogger(ConsultarImprimirGlosasSinRespuesta.class);		
	
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{

			if (form instanceof ConsultarImprimirGlosasSinRespuestaForm) 
			{
				ConsultarImprimirGlosasSinRespuestaForm forma=(ConsultarImprimirGlosasSinRespuestaForm) form;		
				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				HttpSession session=request.getSession();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());		
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				ConsultarImprimirGlosasSinRespuesta mundo = new ConsultarImprimirGlosasSinRespuesta();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultarImprimirGlosasSinRespuestaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				//Se parametriza la busqueda de las glosas sin respuesta
				else if(estado.equals("consultarGlosasSinRespuesta"))
				{
					forma.reset();
					forma.setArregloConvenios(Utilidades.obtenerConvenios (con, "", "", false, "", false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//Realiza la consulta y muestra la lista de Glosas sin Respuesta
				else if(estado.equals("listarGlosas")){

					logger.info("\n\nvalor en la session 1 "+session.getAttribute("elementosMenu"));
					ActionErrors errores = validacionBusqueda(con,forma);

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						logger.info("\n\nvalor en la session 2 "+session.getAttribute("elementosMenu"));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");

					}

					llenarArregloCampos(con, forma);     
					forma.setGlosasSinRespuesta(mundo.consultarListadoGlosas(con, forma.getCodigoConvenio(),String.valueOf(forma.getCodigoContrato()), forma.getFechaNotificacionInicial(),forma.getFechaNotificacionFinal(),forma.getIndicativoFueAuditada(),forma.getConsecutivoFact()));
					UtilidadBD.closeConnection(con);
					logger.info("\n\nvalor en la session 3 "+session.getAttribute("elementosMenu"));
					return mapping.findForward("listadoGlosas");

				}
				//Lista los Contratos segun el convenio seleccionado
				else if (estado.equals ("filtroContratos")){

					return accionFiltrarContratos(con, forma, response);

				}
				//
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}

				else if(estado.equals("imprimir")){

					return this.generarReporte(con, forma, mapping, request, usuario,paciente,mundo);

				}
				else if(estado.equals("crearArchivo")){

					return reporteArchivoPlano(con, forma, mapping, usuario, request);

				}
				else if(estado.equals("ordenar"))
				{
					return accionOrdenar(mapping, forma, con);
				}
				else  
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de CONSULTAR/IMPRIMIR GLOSAS SIN RESPUESTA ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultarImprimirGlosasSinRespuestaForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * 
	 * @param mapping
	 * @param forma
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			ConsultarImprimirGlosasSinRespuestaForm forma, Connection con) 
	{
		for(int w=0; w<forma.getGlosasSinRespuesta().size(); w++)
		{	
			forma.getGlosasSinRespuesta().get(w).setKeyOrdenar(forma.getPatronOrdenar());
		}	
		Collections.sort(forma.getGlosasSinRespuesta());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoGlosas");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	
	
	
//*******************************************************************************************************
	
	/**
	 * 
	 */
	private ActionForward accionFiltrarContratos(
			Connection con,
			ConsultarImprimirGlosasSinRespuestaForm generarForm, 
			HttpServletResponse response) 
	{

		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoContrato</id-select>" +
				"<id-arreglo>contrato</id-arreglo>" +
			"</infoid>" ;
				
		generarForm.setArregloContratos(Utilidades.obtenerContratos(con, Utilidades.convertirAEntero(generarForm.getCodigoConvenio().split(ConstantesBD.separadorSplit)[0]), false, false));
		
		for(HashMap elemento:generarForm.getArregloContratos())
		{
			resultado += "<contrato>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("numerocontrato")+" Vig: "+UtilidadFecha.conversionFormatoFechaAAp(elemento.get("fechainicial")+"")+" - "+UtilidadFecha.conversionFormatoFechaAAp(elemento.get("fechafinal")+"")+"</descripcion>";
			resultado += "</contrato>";
		}
		
		resultado += "</respuesta>";
		
		logger.info("valor de la respuesta >> "+resultado);
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarContratos: "+e);
		}
		
		
		return null;
	}
	
	
	private ActionErrors validacionBusqueda(Connection con,ConsultarImprimirGlosasSinRespuestaForm forma2) 
	{
		ActionErrors errores = new ActionErrors();
		boolean ingresoInfo = false;
		boolean fechaInicialValida = false;
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		 
		//parametros de busqueda**************************************************
		if(forma2.getFechaNotificacionInicial().equals("") && forma2.getFechaNotificacionFinal().equals("") && forma2.getCodigoConvenio().equals("") && (forma2.getCodigoContrato()== ConstantesBD.codigoNuncaValido) &&forma2.getConsecutivoFact().isEmpty()){
		errores.add("",new ActionMessage("errors.required","Algun parametro de Busqueda"));	
		 
		}
		
		
		//Fecha Notificacion inicial ***********************************************************
		if(!forma2.getFechaNotificacionInicial().equals(""))
		{
			ingresoInfo = true;
			if(!UtilidadFecha.validarFecha(forma2.getFechaNotificacionInicial()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","Fecha Notificacion Inicial"));
			else
			{
				fechaInicialValida = true;
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaSistema, forma2.getFechaNotificacionInicial()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Fecha Notificacion Inicial","del sistema: "+fechaSistema));
			}
			
			if(forma2.getFechaNotificacionFinal().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La fecha Notificacion final"));
		}
		//Fecha Notificacion final***********************************************************
		if(!forma2.getFechaNotificacionFinal().equals(""))
		{
			ingresoInfo = true;
			if(!UtilidadFecha.validarFecha(forma2.getFechaNotificacionFinal()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","Fecha Notificacion Final"));
			else
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaSistema, forma2.getFechaNotificacionFinal()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Fecha Notificacion Final","del sistema: "+fechaSistema));
				
				if(fechaInicialValida&&UtilidadFecha.esFechaMenorQueOtraReferencia(forma2.getFechaNotificacionFinal(), forma2.getFechaNotificacionInicial()))
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","Fecha Notificacion Final","Fecha Notificacion inicial"));
				else if(fechaInicialValida&&UtilidadFecha.numeroDiasEntreFechas(forma2.getFechaNotificacionInicial(), forma2.getFechaNotificacionFinal())>180)
					errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias","de respuesta"));
					
				
			}
			
			if(forma2.getFechaNotificacionInicial().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La fecha Notificacion inicial"));
		}
		return errores;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param paciente
	 * @param mundo
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ConsultarImprimirGlosasSinRespuestaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual,PersonaBasica paciente, ConsultarImprimirGlosasSinRespuesta mundo) 
	{
		String nombreRptDesign = "ImpresionGlosasSinResp.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String cadenaCriterios=new String(" ");
		String convenio=new String("-");
		if(!forma.getCodigoConvenio().equals("")){
			convenio=forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[1];	
		} 
		cadenaCriterios+="Convenio:"+convenio+", ";
		cadenaCriterios+="Contrato: "+forma.getCodigoContrato()+", ";
		cadenaCriterios+="Fecha Not Inicial: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaNotificacionInicial())+", ";
		cadenaCriterios+="Fecha Not Final: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaNotificacionFinal())+"";
		cadenaCriterios+="Indicativo Glosa: "+forma.getIndicativoFueAuditada();
	    forma.setCriteriosConsulta(cadenaCriterios);
		
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "                                           GLOSAS");
        comp.insertLabelInGridPpalOfHeader(3,1,"                                  GLOSAS SIN RESPUESTA ");
        comp.insertLabelInGridPpalOfHeader(2,2,"       USUARIO: "+ usuarioActual.getLoginUsuario());
        comp.insertLabelInGridPpalOfHeader(3,2,"       Criterios de busqueda:" + forma.getCriteriosConsulta());
        
       // comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuarioActual.getLoginUsuario());
        //Se habre el dataSet y modifica la consulta
        comp.obtenerComponentesDataSet("GlosasSinRespuesta");     
    	
        String consulta = ConsultarImprimirGlosasSinRespuesta.cadenaConsultaGlosasSinResp( forma.getCodigoConvenio(),forma.getCodigoContrato(),forma.getFechaNotificacionInicial(),forma.getFechaNotificacionFinal(),forma.getIndicativoFueAuditada());
        
        
        comp.modificarQueryDataSet(consulta);
        
        logger.info("Query consulta nueva >>>>>>>>> "+consulta);  
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);       
        logger.info("valor del new path report "+newPathReport);
       
        
       if(!newPathReport.equals(""))
       {
    	   request.setAttribute("isOpenReport", "true");
    	   request.setAttribute("newPathReport", newPathReport);
       }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoGlosas");
	}

	
	private ActionForward reporteArchivoPlano(Connection con, ConsultarImprimirGlosasSinRespuestaForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request){
		HashMap criterios = new HashMap();
		logger.info("el tamaño del array es  >>>>"+forma.getGlosasSinRespuesta().size());
   	    ConsultarImprimirGlosasSinRespuesta.crearCvs(con, forma, usuario);					
		  	
							
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("reporte", "Glosas sin Respuesta");
		criterios.put("tipoSalida", "ARPL");
		criterios.put("criteriosConsulta", forma.getCriteriosConsulta());
		criterios.put("ruta", "../upload");
		
		if(ConsultarImprimirGlosasSinRespuesta.guardar(con, criterios)){
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			logger.info("Mensaje de Exito    >> "+forma.getMensaje());
		} 
			UtilidadBD.closeConnection(con);
        return mapping.findForward("listadoGlosas"); 
	}
	
	
	private void llenarArregloCampos(Connection con,ConsultarImprimirGlosasSinRespuestaForm forma){
		
		ArrayList<String> idsRespuesta = new ArrayList();
		idsRespuesta.add("nomconvenio");
		idsRespuesta.add("contrato");
		idsRespuesta.add("glosaentidad");
		idsRespuesta.add("fechanoti");
		idsRespuesta.add("glosasistema");
		idsRespuesta.add("estadoglosa");
		idsRespuesta.add("valorglosa");
		idsRespuesta.add("resconsecutivo");
		idsRespuesta.add("indicativo");
    	forma.setArregloCampos(idsRespuesta);	
    	forma.setMaxCampos(idsRespuesta.size());
		
	}
	
	
	
	
	
	
}
