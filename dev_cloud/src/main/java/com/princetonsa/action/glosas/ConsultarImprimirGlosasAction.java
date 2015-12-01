package com.princetonsa.action.glosas;

import java.sql.Connection;
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
import util.CsvFile;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ConsultarImprimirGlosasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConsultarImprimirGlosas;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para consultar e imprimir las glosas
 * Date: 2009-02-27
 * @author jfhernandez@princetonsa.com
 */
public class ConsultarImprimirGlosasAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ConsultarImprimirGlosasAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof ConsultarImprimirGlosasForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			ConsultarImprimirGlosasForm forma = (ConsultarImprimirGlosasForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			logger.info("\n\n\n ESTADO --> "+estado+"\n\n");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de Consultar e Imprimir Glosas (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > empezar
    				 *-------------------------------*/
    				if(estado.equals("empezar"))
    				{   
    					return accionEmpezar(con, forma, usuario, request, mapping);	
    				}
    			/*------------------------------
    			 * 		ESTADO > mostrarGlosa
    			 *-------------------------------*/
    			if(estado.equals("mostrarGlosa"))
    			{   
    				return accionMostrarGlosa(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > cargarConvenios
    			 *-------------------------------*/
    			if(estado.equals("cargarConvenios"))
    			{   
    				return accionCargarConvenios(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > cargarContratos
    			 *-------------------------------*/
    			if(estado.equals("cargarContratos"))
    			{   
    				return accionCargarContratos(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > cargarListadoGlosas
    			 *-------------------------------*/
    			if(estado.equals("cargarListadoGlosas"))
    			{   
    				return accionCargarListadoGlosas(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > ordenar
    			 *-------------------------------*/
    			if(estado.equals("ordenar"))
    			{   
    				return accionOrdenarListadoGlosas(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > mostrarDetalleFacturasGlosa
    			 *-------------------------------*/
    			if(estado.equals("mostrarDetalleFacturasGlosa"))
    			{   
    				return accionMostrarDetalleFacturasGlosa(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > generarReporteDetalleGlosa
    			 *-------------------------------*/
    			if(estado.equals("generarReporteDetalleGlosa"))
    			{   
    				return accionGenerarReporteDetalleGlosa(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > mostrarDetalleFacturasGlosa
    			 *-------------------------------*/
    			if(estado.equals("mostrarDetalleSolicitudesGlosa"))
    			{   
    				return accionMostrarDetalleSolicitudesGlosa(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > generarReporteGlosaFactura
    			 *-------------------------------*/
    			if(estado.equals("generarReporteGlosaFactura"))
    			{   
    				return accionGenerarReporteGlosaFactura(con, forma, usuario, request, mapping);	
    			}
    			/*------------------------------
    			 * 		ESTADO > generarReporteListadoGlosas
    			 *-------------------------------*/
    			if(estado.equals("generarReporteListadoGlosas"))
    			{   
    				return accionGenerarReporteListadoGlosas(con, forma, usuario, request, mapping);	
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
     * 
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionGenerarReporteListadoGlosas(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
    	String nombreRptDesign = "ListadoGlosas.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		Vector v;
		String rutaArchivoPlano="";
		String urlArchivoPlano="";
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nIMPRESIÓN GLOSA FACTURA");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Rango de Fechas
    	String filtroo="Fecha Inicial: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltrosMap("fechaRegIni")+"")+" Fecha Final: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltrosMap("fechaRegFin")+"");
    	
    	if (!forma.getFiltrosMap("glosa").toString().equals(""))
    		filtroo+=", Glosa: "+forma.getFiltrosMap("glosa")+", ";
				
    	if (!forma.getFiltrosMap("indicativoGlosa").toString().equals(""))
			filtroo+=", Indicativo: "+forma.getFiltrosMap("indicativoGlosa")+", ";
    	
    	if (!forma.getFiltrosMap("glosaE").toString().equals(""))
			filtroo+=", Glosa Entidad: "+forma.getFiltrosMap("glosaE")+", ";
		
		if (!forma.getFiltrosMap("convenio").toString().equals(""))
			filtroo+=", Convenio: "+forma.getFiltrosMap("convenio")+", ";
		
		if (!forma.getFiltrosMap("contrato").toString().equals(""))
			filtroo+=", Contrato: "+forma.getFiltrosMap("contrato")+", ";
		
		if (!forma.getFiltrosMap("fechaNoti").toString().equals(""))
			filtroo+=", Fecha Notificación: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltrosMap("fechaNoti")+"")+", ";
		
		if (!forma.getFiltrosMap("estadoGlosa").toString().equals(""))
			filtroo+=", Estado Glosa: "+forma.getFiltrosMap("estadoGlosa");
    	
    	v.add(filtroo);
    	
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //****************** FIN INFORMACIÓN DEL CABEZOTE
		
	    comp.obtenerComponentesDataSet("dataSet");
	    
	    //***************** NUEVA CONSULTA DEL REPORTE
	    logger.info("Consulta > "+forma.getListadoGlosasMap("consultasql"));
	    comp.modificarQueryDataSet(forma.getListadoGlosasMap("consultasql")+"");
	    //*****************
	   
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	    comp.updateJDBCParameters(newPathReport);
	   
	    if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	    
	    //******************* GENERAR ARCHIVO PLANO
	    if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	    {
	    	String encabezado = "Reporte: Listado Glosas\n" +
	    						"Parametros de Generación("+filtroo+")\n";
	    							    	
		   String nombre=CsvFile.armarNombreArchivo("ListadoGlosas", usuario);
		   ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
		   if(resultado.isTrue())
	       {
	       		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	       		forma.setUrlArchivoPlano(rutas[0]);
	       		forma.setPathArchivoPlano(rutas[2]);
	       		rutaArchivoPlano=rutas[0];
	       		urlArchivoPlano=rutas[2];
	       }
	       else
	       {
	       		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       } 
		   
		   //*******************GENERACION DEL LOG
		    String log = "";
			int tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			String separador = System.getProperty("file.separator");
			log = "\n  ============LOG TIPO ARCHIVO=========== \n" +
					"\nFecha: "+UtilidadFecha.getFechaActual()+
					"\nHora: "+UtilidadFecha.getHoraActual()+
					"\nUsuario: "+usuario.getLoginUsuario()+
					"\nReporte: "+urlArchivoPlano+
					"\nTipo de Reporte: Archivo Plano"+
					"\nTipo Salida: CSV "+
					"\nCriterios de Búsqueda: "+filtroo+
					"\nNombre del Reporte: Listado Glosas" +
					"\nRuta del archivo plano:"+ rutaArchivoPlano;
		    
			logger.info("\n\n\nSE GENERA EL LOG TIPO ARCHIVO EN: "+ConstantesBD.logFolderModuloGlosas
					+ separador + ConstantesBD.logFolderArchivosPlanos 
					+ separador + ConstantesBD.logListadoGlosasNombre+"\n\n\n");
			
			
			LogsAxioma.enviarLog(ConstantesBD.logListadoGlosasCodigo,log,tipoLog,usuario.getLoginUsuario());
		    
		    //*******************FIN GENERACION DEL LOG
		}
	    
	    //******************* FIN GENERAR ARCHIVO PLANO
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
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
    private ActionForward accionGenerarReporteGlosaFactura(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
    	String nombreRptDesign = "ImpresionGlosaSolicitud.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		Vector v;
		String rutaArchivoPlano="";
		String urlArchivoPlano="";
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nIMPRESIÓN GLOSA FACTURA");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	/* Centro de Atención
    	String filtroo="";
    	
    	v.add("Parametros de Generación("+filtroo+")");
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);*/
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //****************** FIN INFORMACIÓN DEL CABEZOTE
		
	    comp.obtenerComponentesDataSet("dataSet");
	    
	    //***************** NUEVA CONSULTA DEL REPORTE
	     
	    logger.info("Consulta > "+"SELECT dag.solicitud AS solicitud, CASE WHEN dag.servicio IS NOT NULL THEN dag.servicio||' '||getnombreservicio(dag.servicio, 0) ELSE getdescarticulo(dag.articulo)  END AS descripcion, dag.cantidad AS cantidad, dag.valor AS valor, dag.cantidad_glosa AS cantidadglosa, dag.valor_glosa AS valorglosa, glosas.getConceptosDetAudiGlosas2(dag.codigo, 2, '') AS conceptos FROM det_auditorias_glosas dag WHERE dag.auditoria_glosa='"+forma.getDetalleFacturasGlosaMap().get("codauditoriaglosa_"+forma.getPosMap())+"'");
	    comp.modificarQueryDataSet("SELECT dag.solicitud AS solicitud, CASE WHEN dag.servicio IS NOT NULL THEN dag.servicio||' '||getnombreservicio(dag.servicio, 0) ELSE getdescarticulo(dag.articulo)  END AS descripcion, dag.cantidad AS cantidad, dag.valor AS valor, dag.cantidad_glosa AS cantidadglosa, dag.valor_glosa AS valorglosa, glosas.getConceptosDetAudiGlosas2(dag.codigo, 2, '') AS conceptos FROM det_auditorias_glosas dag WHERE dag.auditoria_glosa='"+forma.getDetalleFacturasGlosaMap().get("codauditoriaglosa_"+forma.getPosMap())+"'");
	    //logger.info("Consulta > "+forma.getDetalleFacturasGlosaMap().get("consultasql"));
	    //comp.modificarQueryDataSet(forma.getDetalleFacturasGlosaMap().get("consultasql")+"");
	    //*****************
	   
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	    comp.updateJDBCParameters(newPathReport);       
	    
	    Utilidades.imprimirMapa(forma.getGlosaMap());
	    
	    
	    if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
	    	newPathReport+=	"&convenio="+forma.getGlosaMap().get("convenio")+
	    					"&glosa="+forma.getGlosaMap().get("glosasistema")+
	    					"&indicativo="+forma.getGlosaMap().get("indicativofueauditada")+
	    					"&valorglosa="+UtilidadTexto.formatearValoresConComas(forma.getGlosaMap().get("valorglosa")+"",2,true,true)+
	    					"&estado="+forma.getGlosaMap().get("estado")+
	    					"&factura="+forma.getDetalleFacturasGlosaMap().get("factura_"+forma.getPosMap())+
	    					"&valorfactura="+UtilidadTexto.formatearValoresConComas(forma.getDetalleFacturasGlosaMap().get("valorglosa_"+forma.getPosMap()).toString(), 2, true, true); 
	    
	    if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	    
	    //******************* GENERAR ARCHIVO PLANO
	    if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	    {
	    	String encabezado = "Reporte: Impresión Detalle Glosa Factura\n" +
	    						"Convenio: "+forma.getGlosaMap().get("convenio")+", Glosa: "+forma.getGlosaMap("glosasistema")+", Ind:"+forma.getGlosaMap("indicativofueauditada")+", Valor Total Glosa: "+forma.getGlosaMap("valorglosa")+", Factura: "+forma.getDetalleFacturasGlosaMap().get("factura_"+forma.getPosMap())+", Valor Glosa Factura: "+forma.getDetalleFacturasGlosaMap().get("valorglosa_"+forma.getPosMap());
    	
		   String nombre=CsvFile.armarNombreArchivo("ImpresionGlosaFactura", usuario);
		   
		   logger.info("\n\npath: "+newPathReport+" nombre: "+nombre+" encabezado: "+encabezado+"\n\n");
		   
		   ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
		   if(resultado.isTrue())
	       {
	       		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	       		forma.setUrlArchivoPlano(rutas[0]);
	       		forma.setPathArchivoPlano(rutas[2]);
	       		rutaArchivoPlano=rutas[0];
	       		urlArchivoPlano=rutas[2];
	       }
	       else
	       {
	       		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       } 
		   
		   //*******************GENERACION DEL LOG TIPO ARCHIVO
		    
		    String log = "";
			int tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			String separador = System.getProperty("file.separator");
			log = "\n  ============LOG TIPO ARCHIVO=========== \n" +
					"\nFecha: "+UtilidadFecha.getFechaActual()+
					"\nHora: "+UtilidadFecha.getHoraActual()+
					"\nUsuario: "+usuario.getLoginUsuario()+
					"\nReporte: "+urlArchivoPlano+
					"\nTipo de Reporte: Archivo Plano"+
					"\nTipo Salida: CSV "+
					"\nCriterios de Búsqueda: "+ "Convenio: "+forma.getGlosaMap().get("convenio")+", Factura: "+forma.getDetalleFacturasGlosaMap().get("factura_"+forma.getPosMap())+", Valor: "+forma.getDetalleFacturasGlosaMap().get("valorglosa_"+forma.getPosMap()) +
					"\nNombre del Reporte: Impresión Glosa Solicitud" +
					"\nRuta del archivo plano:"+ rutaArchivoPlano;
		    
			logger.info("\n\n\nSE GENERA EL LOG TIPO ARCHIVO EN: "+ConstantesBD.logFolderModuloGlosas
					+ separador + ConstantesBD.logFolderArchivosPlanos 
					+ separador + ConstantesBD.logListadoGlosasNombre+"\n\n\n");
			
			
			LogsAxioma.enviarLog(ConstantesBD.logImpresionGlosaSolicitudCodigo,log,tipoLog,usuario.getLoginUsuario());
		    
		    //*******************FIN GENERACION DEL LOG TIPO ARCHIVO
		}
	    //******************* FIN GENERAR ARCHIVO PLANO
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitudesGlosa");
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
    private ActionForward accionMostrarDetalleSolicitudesGlosa(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
    	forma.setEstado("");
   		forma.setDetalleSolicitudesGlosaMap(ConsultarImprimirGlosas.consultarDetalleSolicitudesGlosa(con, forma.getDetalleFacturasGlosaMap().get("codauditoriaglosa_"+forma.getPosMap())+"", usuario.getCodigoInstitucionInt()));
   		if (forma.getDetalleSolicitudesGlosaMap("numRegistros").equals("0"))
   			forma.setMsj("NO SE ENCONTRARON DETALLES PARA LA GLOSA FACTURA");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitudesGlosa");
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
    private ActionForward accionGenerarReporteDetalleGlosa(Connection con,ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
    	
    	String nombreRptDesign = "ImpresionDetalleGlosa.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		Vector v;
		String urlArchivoPlano="";
		String rutaArchivoPlano="";
		
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nIMPRESIÓN DETALLE GLOSA");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	/* Centro de Atención
    	String filtroo="";
    	
    	v.add("Parametros de Generación("+filtroo+")");
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);*/
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //****************** FIN INFORMACIÓN DEL CABEZOTE
		
	    comp.obtenerComponentesDataSet("dataSet");
	    
	    if(request.getParameter("codigoglosa")!=null)
	    {
	    	forma.reset();
	    	forma.setGlosaMap("codigoglosa", request.getParameter("codigoglosa")+"");
	    	forma.setTipoSalida(request.getParameter("tipoSalida")+"");
	    }
	    
	    if ((request.getParameter("convenio")!=null)&&(request.getParameter("estadoG")!=null)&&(request.getParameter("glosasistema")!=null)&&(request.getParameter("valor")!=null)&&(request.getParameter("indicativo")!=null))
	    {
	    	forma.setGlosaMap("convenio", request.getParameter("convenio")+"");
	    	forma.setGlosaMap("estado", request.getParameter("estadoG")+"");
	    	forma.setGlosaMap("glosasistema", request.getParameter("glosasistema")+"");
	    	forma.setGlosaMap("valorglosa", request.getParameter("valor")+"");
	    	forma.setGlosaMap("indicativo", request.getParameter("indicativo")+"");
	    }
	    
	       		
	    //***************** NUEVA CONSULTA DEL REPORTE
	    logger.info("Consulta > "+ConsultasBirt.consultarDetalleFacturasGlosa(forma.getGlosaMap().get("codigoglosa")+"",usuario.getCodigoInstitucionInt()));
	    comp.modificarQueryDataSet(ConsultasBirt.consultarDetalleFacturasGlosa(forma.getGlosaMap().get("codigoglosa")+"",usuario.getCodigoInstitucionInt()));
	    //*****************
	    
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	    comp.updateJDBCParameters(newPathReport);       
	    
	    if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
	    	newPathReport+=	"&convenio="+forma.getGlosaMap().get("convenio")+
	    					"&estado="+forma.getGlosaMap().get("estado")+
	    					"&glosa="+forma.getGlosaMap().get("glosasistema")+
	    					"&valor="+UtilidadTexto.formatearValoresConComas(forma.getGlosaMap().get("valorglosa")+"", 2, true, true)+
	    					"&indicativo="+forma.getGlosaMap().get("indicativofueauditada");
	   
	    if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	    
	    //******************* GENERAR ARCHIVO PLANO
	    if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	    {
	    	String encabezado = "Reporte: Impresión Detalle Glosa\n" +
	    						"Convenio: "+forma.getGlosaMap().get("convenio")+", Estado: "+forma.getGlosaMap().get("estado")+", Glosa: "+forma.getGlosaMap().get("glosasistema")+", Valor Glosa: "+forma.getGlosaMap().get("valorglosa")+", Ind: "+forma.getGlosaMap().get("indicativofueauditada");
	    	
		   String nombre=CsvFile.armarNombreArchivo("ImpresionDetalleGlosa", usuario);
		   ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
		   if(resultado.isTrue())
	       {
	       		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	       		forma.setUrlArchivoPlano(rutas[0]);
	       		forma.setPathArchivoPlano(rutas[2]);
	       		rutaArchivoPlano=rutas[0];
	       		urlArchivoPlano=rutas[2];
	       		
	       }
	       else
	       {
	       		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       } 
		   
		 //*******************GENERACION DEL LOG TIPO ARCHIVO
		    
		    String log = "";
			int tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			String separador = System.getProperty("file.separator");
			log = "\n  ============LOG TIPO ARCHIVO=========== \n" +
					"\nFecha: "+UtilidadFecha.getFechaActual()+
					"\nHora: "+UtilidadFecha.getHoraActual()+
					"\nUsuario: "+usuario.getLoginUsuario()+
					"\nReporte: "+urlArchivoPlano+
					"\nTipo de Reporte: Archivo Plano"+
					"\nTipo Salida: CSV "+
					"\nCriterios de Búsqueda: "+ "Convenio: "+forma.getGlosaMap().get("convenio")+", Factura: "+forma.getDetalleFacturasGlosaMap().get("factura_"+forma.getPosMap())+", Valor: "+forma.getDetalleFacturasGlosaMap().get("valorglosa_"+forma.getPosMap()+", Ind: "+forma.getGlosaMap().get("indicativofueauditada"))+
					"\nNombre del Reporte: Impresión Glosa Solicitud" +
					"\nRuta del archivo plano:"+ rutaArchivoPlano;
		    
			logger.info("\n\n\nSE GENERA EL LOG TIPO ARCHIVO EN: "+ConstantesBD.logFolderModuloGlosas
					+ separador + ConstantesBD.logFolderArchivosPlanos 
					+ separador + ConstantesBD.logListadoGlosasNombre+"\n\n\n");
			
			LogsAxioma.enviarLog(ConstantesBD.logImpresionDetalleGlosaCodigo,log,tipoLog,usuario.getLoginUsuario());
		    
		    //*******************FIN GENERACION DEL LOG TIPO ARCHIVO
		}
	    //******************* FIN GENERAR ARCHIVO PLANO
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturasGlosa");
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
	private ActionForward accionMostrarDetalleFacturasGlosa(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {	
		forma.setMsj("");
		forma.setDetalleFacturasGlosaMap(ConsultarImprimirGlosas.consultarDetalleFacturasGlosa(con, forma.getGlosaMap().get("codigoglosa")+"", usuario.getCodigoInstitucionInt()));
		if (forma.getDetalleFacturasGlosaMap("numRegistros").equals("0"))
		{
			forma.setMsj("N0 SE ENCONTRÓ INFORMACIÓN DEL DETALLE DE LA GLOSA");
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturasGlosa");
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
	private ActionForward accionMostrarGlosa(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.setMsj("");
		forma.setGlosaMap(ConsultarImprimirGlosas.consultarGlosa(con, forma.getListadoGlosasMap("codigo_"+forma.getPosMap())+""));
		if (forma.getGlosaMap("numRegistros").equals("0"))
		{
			forma.setMsj("N0 SE ENCONTRÓ INFORMACIÓN SOBRE LA GLOSA");
		}
		Utilidades.imprimirMapa(forma.getGlosaMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("glosa");
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
	private ActionForward accionEmpezar(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
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
	private ActionForward accionCargarConvenios(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.setMsj("");
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", true, "", true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
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
	private ActionForward accionCargarContratos(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.setMsj("");
		forma.setContratos(Utilidades.obtenerContratos(con, Utilidades.convertirAEntero(forma.getFiltrosMap("convenio").toString()), false, true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
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
	private ActionForward accionCargarListadoGlosas(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		forma.setMsj("");
		forma.setFiltrosMap("institucion", usuario.getCodigoInstitucion());
		forma.setListadoGlosasMap(ConsultarImprimirGlosas.listarGlosas(con,forma.getFiltrosMap()));
		if (Utilidades.convertirAEntero(forma.getListadoGlosasMap("numRegistros").toString())==0)
		{
			forma.setMsj("NO SE ENCONTRARON RESULTADOS PARA LA BÚSQUEDA");
		}
		Utilidades.imprimirMapa(forma.getListadoGlosasMap());
		UtilidadBD.closeConnection(con);	
		return mapping.findForward("filtro");
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
	private ActionForward accionOrdenarListadoGlosas(Connection con, ConsultarImprimirGlosasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		int numReg = Integer.parseInt(forma.getListadoGlosasMap("numRegistros")+"");
		String consulta = forma.getListadoGlosasMap("consultasql").toString();
    	String[] indicesGlosaMap = {"codigo_","glosasis_","fecharegi_","estado_","convenio_","contrato_","entidad_","fechanoti_","valor_","nomconvenio_", "numcontrato_","indicativofueauditada_",""};
    	forma.setListadoGlosasMap(Listado.ordenarMapa(indicesGlosaMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoGlosasMap(), numReg));
    	forma.setUltimoPatron(forma.getPatronOrdenar());
    	forma.setListadoGlosasMap("numRegistros",numReg);
    	forma.setListadoGlosasMap("consultasql",consulta);
		
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("filtro");
	}
}