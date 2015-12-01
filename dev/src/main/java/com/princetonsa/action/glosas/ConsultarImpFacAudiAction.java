package com.princetonsa.action.glosas;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ConsultarImpFacAudiForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConsultarImpFacAudi;
import com.princetonsa.mundo.glosas.Glosas;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultarImpFacAudiAction extends Action
{
	private Logger logger = Logger.getLogger(ConsultarImpFacAudiAction.class);	
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof ConsultarImpFacAudiForm)
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
				ConsultarImpFacAudiForm forma = (ConsultarImpFacAudiForm)form;		

				//se instancia el mundo
				ConsultarImpFacAudi mundo = new ConsultarImpFacAudi();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.

				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE CONSULTAR IMPRIMIR FACTURAS AUDITADAS ES ====>> "+estado);
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
				else if(estado.equals("filtroContratos"))
				{
					return accionFiltrarContratos(con, forma, response);
				}
				else if (estado.equals("buscar"))
				{
					return this.accionBuscar(con, forma, mundo, mapping, usuario, request, response);
				}
				else if (estado.equals("ordenarFacturas"))
				{
					return accionOrdenarFacturas(con, forma, mapping);
				}
				else if (estado.equals("generar"))
				{
					return accionGenerar(con, forma, mapping, usuario, request);
				}
				else if (estado.equals("consultarFactura"))
				{
					return accionConsultarFactura(con, forma, mundo, mapping, usuario, request,response);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
	 * Accion Consultar Factura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 * @throws IOException 
	 */
	private ActionForward accionConsultarFactura(Connection con, ConsultarImpFacAudiForm forma, ConsultarImpFacAudi mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
		forma.setConsultaDetFacMap(mundo.consultarFactura(con, forma.getCodFactura()));
		logger.info("\n\nMAPA FACTURA DETALLE>>>>>>>>>>>>"+forma.getConsultaDetFacMap());
		UtilidadBD.closeConnection(con);
		forma.setLinkSiguiente("../consultarImpFacAudi/consultarImpFacAudi.jsp?pager.offset=0");
		response.sendRedirect(forma.getLinkSiguiente());
		return mapping.findForward("consultarImpFacAudi");
	}

	/**
	 * Accion Generar
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGenerar(Connection con, ConsultarImpFacAudiForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		HashMap criterios = new HashMap();
		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return accionGenerarReporte(con, forma, mapping, usuario, request);
		else
			if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			{
				ConsultarImpFacAudi.crearCvs(con, forma, usuario);					
				criterios.put("usuario", usuario.getLoginUsuario());
				criterios.put("reporte", forma.getTipoReporte());
				criterios.put("tipoSalida", "ARPL");
				criterios.put("criteriosConsulta", forma.getCriteriosConsulta());
				criterios.put("ruta", forma.getRuta());
				
				if(ConsultarImpFacAudi.guardar(con, criterios))
					forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				UtilidadBD.closeConnection(con);  
				return mapping.findForward("consultarImpFacAudi");
			}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultarImpFacAudi");
	}
		
	/**
	 * Accion Generar Reporte
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGenerarReporte(Connection con, ConsultarImpFacAudiForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(forma.getTipoReporte().equals("ListadoFacturasAuditadas"))
		{			
			String nombreRptDesign = "ListadoFacturasAuditadas.rptdesign";
			String nombreCsv = "ListadoFacturasAuditadas";
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			Vector v;
			
			//***************** INFORMACIÓN DEL CABEZOTE
	        DesignEngineApi comp; 
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
	         
	        // Logo
	        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	        
	        // Nombre Institución, titulo y rango de fechas
	        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	        v=new Vector();
	        v.add(ins.getRazonSocial());
	        v.add("NIT: "+ins.getNit()+"\n\nAUDITORIA\nLISTADO FACTURAS AUDITADAS");
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        
	        // Parametros de Generación
	        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	        v=new Vector();
	        
	        String filtro="Parametros de Generación:";
	        
	        // Fechas
	        if(!forma.getFechaAuditoriaInicial().equals("") && !forma.getFechaAuditoriaFinal().equals(""))
	        	filtro += "  Fecha Inicial: "+forma.getFechaAuditoriaInicial()+"  Fecha Final: "+forma.getFechaAuditoriaFinal();
	        
	        // Consecutivo Facturas
	        if(!forma.getFacturaInicial().equals("") && !forma.getFacturaFinal().equals(""))
	        	filtro += "  Factura Inicial: "+forma.getFacturaInicial()+"  Factura Final: "+forma.getFacturaFinal();
	        	
	        // Convenio
	        if(!forma.getCodigoConvenio().equals(""))
	        	filtro += "  Convenio: "+forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[1];
	        
	        // Contrato
	        if(forma.getCodigoContrato()!=ConstantesBD.codigoNuncaValido)
	           	filtro += "  Contrato: "+forma.getCodigoContrato();
	                
	        // Pre-Glosa
	        if(!forma.getNumeroPreGlosa().equals(""))
	        	filtro += "  Pre-Glosa: "+forma.getNumeroPreGlosa();	        
	        	        
	        v.add(filtro);
	        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	        
	        // Fecha hora de proceso y usuario
	        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	        	        
	        //***************** NUEVO WHERE DEL REPORTE
	       comp.obtenerComponentesDataSet("dataSet");
	       String newquery = ConsultasBirt.listadoFacturasAuditadas(
					con, 
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAuditoriaInicial()), 
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAuditoriaFinal()), 
					forma.getFacturaInicial(), 
					forma.getFacturaFinal(), 
					Utilidades.convertirAEntero(forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[0]), 
					forma.getCodigoContrato(), 
					forma.getNumeroPreGlosa(), 
					usuario.getCodigoInstitucionInt());
	       
	       logger.info("\n\nQUERY ------>>>"+newquery);
	       comp.modificarQueryDataSet(newquery);
	       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	       comp.updateJDBCParameters(newPathReport);
	       
	       if(!newPathReport.equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport", newPathReport);
	        }
	             
	       UtilidadBD.closeConnection(con);
		}
		else if(forma.getTipoReporte().equals("ImpresionGlosaFactura"))
		{
			String nombreRptDesign = "ImpresionGlosaFactura.rptdesign";
			String nombreCsv = "ImpresionGlosaFactura";
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
								
			Vector v;
						
			//***************** INFORMACIÓN DEL CABEZOTE
	        DesignEngineApi comp; 
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
	         
	        // Logo
	        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	        
	        // Nombre Institución, titulo y rango de fechas
	        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	        v=new Vector();
	        v.add(ins.getRazonSocial());
	        v.add("\nIMPRESION GLOSA FACTURA");
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        	       
	        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	        v=new Vector();

	        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	        
	        // Fecha hora de proceso y usuario
	        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	        
	        if(request.getParameter("codigoFactura")!=null)
		    {
		    	forma.reset(usuario.getCodigoInstitucionInt());
		    	forma.setCodFactura(request.getParameter("codigoFactura")+"");
		    }
	        
	        logger.info("\n\ncod factura ------>>>"+forma.getCodFactura());
	        
	      //***************** NUEVO WHERE DEL REPORTE
		       comp.obtenerComponentesDataSet("dataSet");
		       String newquery = ConsultasBirt.ImpresionGlosaFactura(
						con, 
						forma.getCodFactura(), 
						usuario.getCodigoInstitucionInt(), forma.getCodigoGlosa());
		       
		       logger.info("\n\nQUERY 2------>>>"+newquery);
		       comp.modificarQueryDataSet(newquery);
		       
		       forma.reset(usuario.getCodigoInstitucionInt());
		       
		       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
	      	comp.lowerAliasDataSet();
			String newPathReport = comp.saveReport1(false);
		       comp.updateJDBCParameters(newPathReport);
		       
		       if(!newPathReport.equals(""))
		        {
		        	request.setAttribute("isOpenReport", "true");
		        	request.setAttribute("newPathReport", newPathReport);
		        }

		       UtilidadBD.closeConnection(con);
		}
	    return mapping.findForward("consultarImpFacAudi");
	}
	
	/**
     * Inicia en el forward de Consultar Imprimir Facturas Auditadas
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ConsultarImpFacAudiForm forma, ConsultarImpFacAudi mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		forma.reset(usuario.getCodigoInstitucionInt());
		/*
		 * Se cargan los arreglos
		 */
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAuditor(usuario.getCodigoInstitucionInt())))
			forma.setArregloConvenios(UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), 
				ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor, true));
		else
			forma.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
						
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultarImpFacAudi");
	}
	
	/**
	 * Método para filtrar los contratos al cambiar el convenio
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarContratos(
			Connection con,
			ConsultarImpFacAudiForm generarForm, 
			HttpServletResponse response) 
	{

		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoContrato</id-select>" +
				"<id-arreglo>contrato</id-arreglo>" +
			"</infoid>" ;
				
		generarForm.setArregloContratos(Utilidades.obtenerContratos(con, Utilidades.convertirAEntero(generarForm.getCodigoConvenio().split(ConstantesBD.separadorSplit)[0]), false, true));
		
		for(HashMap elemento:generarForm.getArregloContratos())
		{
			resultado += "<contrato>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("numerocontrato")+" Vig: "+UtilidadFecha.conversionFormatoFechaAAp(elemento.get("fechainicialbd")+"")+" - "+UtilidadFecha.conversionFormatoFechaAAp(elemento.get("fechafinal")+"")+"</descripcion>";
			resultado += "</contrato>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarContratos: "+e);
		}
		return null;
	}
	
	/**
	 * Método para buscar las facturas auditadas
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionBuscar(Connection con,ConsultarImpFacAudiForm forma, ConsultarImpFacAudi mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response)
	{
		//******************VALIDACIONES CAMPOS BUSQUEDA ************************************************
		ActionErrors errores = validacionBusqueda(con,forma); 
		//*************************************************************************************************
		
		if(!errores.isEmpty())
		{
			forma.setRealizoBusqueda(false);
			saveErrors(request, errores);
		}
		else
		{
			forma.setRealizoBusqueda(true);
			forma.setListadoFacturas(Glosas.consultarFacturasAuditadas(
				con, 
				forma.getFechaAuditoriaInicial(), 
				forma.getFechaAuditoriaFinal(), 
				forma.getFacturaInicial(), 
				forma.getFacturaFinal(), 
				Utilidades.convertirAEntero(forma.getCodigoConvenio().split(ConstantesBD.separadorSplit)[0]), 
				forma.getCodigoContrato(), 
				forma.getNumeroPreGlosa(), 
				usuario.getCodigoInstitucionInt(),
				"ConsultarImpFactAudi", 
				Glosas.cargarListadoConveniosUsuario(usuario, ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor, true)));
			for(int i=0; i<Utilidades.convertirAEntero(forma.getListadoFacturas("numRegistros")+"");i++){
				HashMap<String,Object> mapaTemp=new HashMap<String,Object>();
				mapaTemp = mundo.consultarFactura(con,forma.getListadoFacturas("codigoGlosa_"+i)+"");
				if(Utilidades.convertirAEntero(mapaTemp.get("numRegistros")+"") > 0)
					forma.setListadoFacturas("mostrarlink_"+i, ConstantesBD.acronimoSi);
				else
					forma.setListadoFacturas("mostrarlink_"+i, ConstantesBD.acronimoNo);
				
				forma.setListadoFacturas("valor_pre_glosa_"+i,UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(forma.getListadoFacturas("valor_pre_glosa_"+i)+"")));
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultarImpFacAudi");
	}
	
	/**
	 * Método implementado para ordenar el listado de facturas
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarFacturas(Connection con,ConsultarImpFacAudiForm forma, ActionMapping mapping) 
	{
		String[] indices = Glosas.indicesListadoFacturasAuditadas;
		int numFacturas = forma.getNumListadoFacturas();
		/*
		logger.info("*************************************************");
		logger.info("valor ultimo indice: "+forma.getUltimoIndice());
		logger.info("valor primer indice: "+forma.getIndice());
		Utilidades.imprimirMapa(forma.getListadoFacturas());
		logger.info("*************************************************");
		*/
		forma.setListadoFacturas(Listado.ordenarMapa(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getListadoFacturas(),
				numFacturas));
		
		forma.setListadoFacturas("numRegistros",numFacturas+"");
		
		forma.setUltimoIndice(forma.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultarImpFacAudi");
	}
	
	/**
	 * Método para realizar las validaciones de busqueda
	 * @param con
	 * @param generarForm
	 * @return
	 */
	private ActionErrors validacionBusqueda(Connection con,ConsultarImpFacAudiForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		boolean ingresoInfo = false;
		boolean fechaInicialValida = false;
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		
		//Fecha auditoria inicial ***********************************************************
		if(!forma.getFechaAuditoriaInicial().equals(""))
		{
			ingresoInfo = true;
			if(!UtilidadFecha.validarFecha(forma.getFechaAuditoriaInicial()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","auditoría inicial"));
			else
			{
				fechaInicialValida = true;
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaSistema, forma.getFechaAuditoriaInicial()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","auditoría inicial","del sistema: "+fechaSistema));
			}
			
			if(forma.getFechaAuditoriaFinal().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La fecha auditoría final"));
		}
		
		//Fecha auditoria final***********************************************************
		if(!forma.getFechaAuditoriaFinal().equals(""))
		{
			ingresoInfo = true;
			if(!UtilidadFecha.validarFecha(forma.getFechaAuditoriaFinal()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","auditoría final"));
			else
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaSistema, forma.getFechaAuditoriaFinal()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","auditoría final","del sistema: "+fechaSistema));
				
				if(fechaInicialValida&&UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaAuditoriaFinal(), forma.getFechaAuditoriaInicial()))
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","auditoría final","auditoría inicial"));
				else if(fechaInicialValida&&UtilidadFecha.numeroDiasEntreFechas(forma.getFechaAuditoriaInicial(), forma.getFechaAuditoriaFinal())>180)
					errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias","de auditoría"));
					
				
			}
			
			if(forma.getFechaAuditoriaInicial().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La fecha auditoría inicial"));
		}
		
		//Factura inicial *******************************************************************
		if(!forma.getFacturaInicial().equals(""))
		{
			ingresoInfo = true;
			if(Utilidades.convertirAEntero(forma.getFacturaInicial())<=0)
				errores.add("", new ActionMessage("errors.integerMayorQue","La factura inicial","0"));
			
			if(forma.getFacturaFinal().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La factura final"));
		}
		
		//Factura final **********************************************************************
		if(!forma.getFacturaFinal().equals(""))
		{
			ingresoInfo = true;
			if(Utilidades.convertirAEntero(forma.getFacturaFinal())<=0)
				errores.add("", new ActionMessage("errors.integerMayorQue","La factura final","0"));
			else if(Utilidades.convertirAEntero(forma.getFacturaFinal(),true)<Utilidades.convertirAEntero(forma.getFacturaInicial(), true))
				errores.add("", new ActionMessage("errors.MayorIgualQue","La factura final","la factura inicial"));
			
			if(forma.getFacturaInicial().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La factura inicial"));
		}
		
		if(!forma.getCodigoConvenio().equals("") || forma.getCodigoContrato()>0)
			ingresoInfo = true;
		
		//PRE-GLOSA ************************************************************************************
		if(!forma.getNumeroPreGlosa().equals(""))
		{
			ingresoInfo = true;
			if(Utilidades.convertirAEntero(forma.getNumeroPreGlosa())==ConstantesBD.codigoNuncaValido)
				errores.add("", new ActionMessage("errors.integer","El campo pre-glosa"));
		}
		
		if(!ingresoInfo)
			errores.add("", new ActionMessage("errors.minimoCampos","ingresar un campo","búsqueda de facturas"));
		
		return errores;
	}
}