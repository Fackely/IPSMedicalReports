package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.EstadisticasIngresosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.EstadisticasIngresos;
import com.princetonsa.pdf.AtencionXempresaYConvenioPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo de los reportes
 * de las estadisticas de servicios
 * Date: 2008-08-11
 * @author garias@princetonsa.com
 */
public class EstadisticasIngresosAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(EstadisticasIngresosAction.class);
	
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
    		if(form instanceof EstadisticasIngresosForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			EstadisticasIngresosForm forma = (EstadisticasIngresosForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (EstadisticasIngresosForm) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(estado == null)
    			{ 
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de estadistica calidad de atención (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("principal");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > empezar
    				 *-------------------------------*/
    				if(estado.equals("empezar"))
    				{   
    					return accionEmpezar(con, mapping, forma, usuario);
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > cambiarTipoReporte
    					 *-------------------------------*/
    					if(estado.equals("cambiarTipoReporte"))
    					{   
    						return accionCambiaTipoReporte(con, mapping, forma, usuario);
    					}
    					else
    						/*------------------------------
    						 * 		ESTADO > cambiarViaIngreso
    						 *-------------------------------*/
    						if(estado.equals("cambiarViaIngreso"))
    						{   
    							return accionCambiarViaIngreso(con, mapping, forma, usuario);
    						}
    						else
    							/*------------------------------
    							 * 		ESTADO > adicionarConvenio
    							 *-------------------------------*/
    							if(estado.equals("adicionarConvenio"))
    							{   
    								return accionAdicionarConvenio(con, mapping, forma, usuario);
    							}
    							else
    								/*------------------------------
    								 * 		ESTADO > eliminarConvenio
    								 *-------------------------------*/
    								if(estado.equals("eliminarConvenio"))
    								{   
    									return accionEliminarConvenio(con, mapping, forma, usuario);
    								}
    								else
    									/*------------------------------
    									 * 		ESTADO > adicionarEmpresa
    									 *-------------------------------*/
    									if(estado.equals("adicionarEmpresa"))
    									{   
    										return accionAdicionarEmpresa(con, mapping, forma, usuario);
    									}
    									else
    										/*------------------------------
    										 * 		ESTADO > eliminarEmpresa
    										 *-------------------------------*/
    										if(estado.equals("eliminarEmpresa"))
    										{   
    											return accionEliminarEmpresa(con, mapping, forma, usuario);
    										}
    										else
    											/*------------------------------
    											 * 		ESTADO > generarReporte
    											 *-------------------------------*/
    											if(estado.equals("generarReporte"))
    											{   
    												return accionGenerarReporte(con, mapping, forma, usuario, request);
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
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
	private ActionForward accionEliminarConvenio(Connection con,ActionMapping mapping, EstadisticasIngresosForm forma,UsuarioBasico usuario) {
		forma.setConveniosMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
		HashMap aux = (HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getConveniosMap("posArray_"+forma.getPosMap())+""));
		aux.put("seleccionado", ConstantesBD.acronimoNo);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarConvenio(Connection con, ActionMapping mapping, EstadisticasIngresosForm forma, UsuarioBasico usuario) {
		
		if (!forma.getFiltros("convenio").equals("")){
			// Adicionar el convenio
			forma.getConveniosMap().put("codigoConvenio_"+forma.getConveniosMap("numRegistros"), ((HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getFiltros("convenio")+""))).get("codigoConvenio"));
			forma.getConveniosMap().put("nombreConvenio_"+forma.getConveniosMap("numRegistros"), ((HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getFiltros("convenio")+""))).get("nombreConvenio"));
			forma.getConveniosMap().put("eliminado_"+forma.getConveniosMap("numRegistros"), ConstantesBD.acronimoNo);
			forma.getConveniosMap().put("posArray_"+forma.getConveniosMap("numRegistros"), forma.getFiltros("convenio"));
			forma.setConveniosMap("numRegistros", Utilidades.convertirAEntero(forma.getConveniosMap("numRegistros").toString())+1);
			// "Quitar" el convenio del select
			HashMap aux = (HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getFiltros("convenio")+""));
			aux.put("seleccionado", ConstantesBD.acronimoSi);
		}
		Utilidades.imprimirMapa(forma.getConveniosMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
	private ActionForward accionEliminarEmpresa(Connection con,ActionMapping mapping, EstadisticasIngresosForm forma,UsuarioBasico usuario) {
		forma.setEmpresasMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
		HashMap aux = (HashMap)forma.getEmpresas().get(Utilidades.convertirAEntero(forma.getEmpresasMap("posArray_"+forma.getPosMap())+""));
		aux.put("seleccionado", ConstantesBD.acronimoNo);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarEmpresa(Connection con, ActionMapping mapping, EstadisticasIngresosForm forma, UsuarioBasico usuario) {
		
		if (!forma.getFiltros("empresa").equals(""))
		{
			// Adicionar la empresa
			forma.getEmpresasMap().put("codigoEmpresa_"+forma.getEmpresasMap("numRegistros").toString(), ((HashMap)forma.getEmpresas().get(Utilidades.convertirAEntero(forma.getFiltros("empresa")+""))).get("codigo"));
			forma.getEmpresasMap().put("nombreEmpresa_"+forma.getEmpresasMap("numRegistros").toString(), ((HashMap)forma.getEmpresas().get(Utilidades.convertirAEntero(forma.getFiltros("empresa")+""))).get("razon_social"));
			forma.getEmpresasMap().put("eliminado_"+forma.getEmpresasMap("numRegistros").toString(), ConstantesBD.acronimoNo);
			forma.getEmpresasMap().put("posArray_"+forma.getEmpresasMap("numRegistros").toString(), forma.getFiltros("empresa"));
			forma.setEmpresasMap("numRegistros", Utilidades.convertirAEntero(forma.getEmpresasMap("numRegistros").toString())+1);
			// "Quitar" la empresa del select
			HashMap aux = (HashMap)forma.getEmpresas().get(Utilidades.convertirAEntero(forma.getFiltros("empresa")+""));
			aux.put("seleccionado", ConstantesBD.acronimoSi);
		}
		Utilidades.imprimirMapa(forma.getEmpresasMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCambiarViaIngreso(Connection con, ActionMapping mapping, EstadisticasIngresosForm forma, UsuarioBasico usuario) {
		forma.setTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, forma.getFiltros("viaIngreso").toString()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
    private ActionForward accionGenerarReporte(Connection con, ActionMapping mapping, EstadisticasIngresosForm forma, UsuarioBasico usuario, HttpServletRequest request) {
    	
    	forma.setFiltros("convenio", cadenaConveniosSeleccionados(forma.getConveniosMap()));
    	
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteIngresosPorConvenio+""))
    		generarIngresosPorConvenio(con, forma, usuario, request);
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteReingresos+""))
    		generarReingresos(con, forma, usuario, request);
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteTotalReingresosPorConvenio+""))
    		generarTotalReingresosPorConvenio(con, forma, usuario, request);
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteAtencionPorRangoEdad+""))
    		generarAtencionPorRangoEdad(con, forma, usuario, request);
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteAtencionPorEmpresaYConvenio+"")){
    		forma.setFiltros("empresa", cadenaEmpresasSeleccionados(forma.getEmpresasMap()));
    		return generarAtencionPorEmpresaYConvenio(con, forma, usuario, request, mapping);
    	}	
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
    
    /**
     * 
     * @param conveniosMap
     * @return
     */
    private String cadenaConveniosSeleccionados(HashMap conveniosMap) {
		String convenios="";
		for(int i=0; i<Utilidades.convertirAEntero(conveniosMap.get("numRegistros").toString()); i++){
			if(conveniosMap.get("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
				convenios += conveniosMap.get("codigoConvenio_"+i)+", ";
			}
		}
		convenios += ConstantesBD.codigoNuncaValido;
		logger.info("CONVENIOS - "+convenios);
		return convenios;
	}
    
    /**
     * 
     * @param conveniosMap
     * @return
     */
    private String cadenaEmpresasSeleccionados(HashMap empresasMap) {
		String empresas="";
		for(int i=0; i<Utilidades.convertirAEntero(empresasMap.get("numRegistros").toString()); i++){
			if(empresasMap.get("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
				empresas += empresasMap.get("codigoEmpresa_"+i)+", ";
			}
		}
		empresas += ConstantesBD.codigoNuncaValido;
		logger.info("EMPRESAS - "+empresas);
		return empresas;
	}
    
	/**
     * 
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
    private ActionForward generarAtencionPorEmpresaYConvenio(Connection con,EstadisticasIngresosForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
    	
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/atencionXEmpresaYConvenio" + r.nextInt()  +".pdf";
    	
    	AtencionXempresaYConvenioPdf.pdfAtencionXEmpresaYConvenio(con, ValoresPorDefecto.getFilePath(), nombreArchivo, forma, usuario, request);
    	
    	request.setAttribute("nombreArchivo", nombreArchivo);
    	request.setAttribute("nombreVentana", "Liquidación Servicios");
    	
    	 // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteAtencionPorEmpresaYConvenio, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("abrirPdf");
	}
    
    /**
     * 
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
    private void generarAtencionPorRangoEdad(Connection con,EstadisticasIngresosForm forma, UsuarioBasico usuario, HttpServletRequest request) {
    	String nombreRptDesign = "AtencionXRangoEdad.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nATENCIÓN POR RANGO DE EDAD\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso
    	filtroo+="[Vía de Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getFiltros("viaIngreso").toString()))+"]  ";
    	
    	// Tipo de Paciente
    	if(forma.getFiltros().containsKey("tipoPaciente"))
    		if (!forma.getFiltros("tipoPaciente").equals(""))
    			filtroo+="[Tipo de Paciente: "+Utilidades.obtenerNombreTipoPaciente(con, forma.getFiltros("tipoPaciente").toString())+"]  ";
    	
    	// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("") && !forma.getFiltros("convenio").equals(ConstantesBD.codigoNuncaValido+"")){
			int convenio;
			filtroo+="[Convenio: ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
		
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario()); 
	    //******************
		
	    //***************** NUEVO WHERE DEL REPORTE
	    
	    mundo.setFiltrosMap(forma.getFiltros());
	    mundo.setInstitucion(usuario.getCodigoInstitucionInt());
	    
	    
	    // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteAtencionPorRangoEdad, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
	  
	   
	   //****************** MODIFICAR CONSULTA
	   comp.obtenerComponentesDataSet("dataset"); 
	    
	   String newquery = mundo.crearConsultaAtencionXRangoEdad(con, mundo);
	   logger.info("Query >>>"+newquery);
	   
	   comp.modificarQueryDataSet(newquery);
	   
	  
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);   
	   
	   newPathReport+="&i=0";
	   if(!newPathReport.equals(""))
	   {
		   request.setAttribute("isOpenReport", "true");
		   request.setAttribute("newPathReport", newPathReport);
	   }
	}
    
    /**
     * 
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
    private void generarTotalReingresosPorConvenio(Connection con,EstadisticasIngresosForm forma, UsuarioBasico usuario, HttpServletRequest request) {
    	String nombreRptDesign = "TotalReingresosXConvenio.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nTOTAL REINGRESOS POR CONVENIO\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso
    	filtroo+="[Vía de Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getFiltros("viaIngreso").toString()))+"]  ";
    	
    	// Tipo de Paciente
    	if(forma.getFiltros().containsKey("tipoPaciente"))
    		if (!forma.getFiltros("tipoPaciente").equals(""))
    			filtroo+="[Tipo de Paciente: "+Utilidades.obtenerNombreTipoPaciente(con, forma.getFiltros("tipoPaciente").toString())+"]  ";
    	
    	// Diagnostico de egreso
    	String diagnosticos="";
    	boolean hayDx=false;
    	for(int i=0; i<forma.getNumDiagEgreso(); i++){
			if(forma.getDiagnosticosEgreso("checkbox_"+i).equals("true")){
				diagnosticos+=forma.getDiagnosticosEgreso(i+"").toString().split(ConstantesBD.separadorSplit)[2]+", ";
				hayDx=true;
			}	
		}
    	if(hayDx)
    		filtroo+="[Dx Egreso: "+diagnosticos+"] ";
    	
    	// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("") && !forma.getFiltros("convenio").equals(ConstantesBD.codigoNuncaValido+"")){
			int convenio;
			filtroo+="[Convenio: ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
		
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //******************
		
	    //***************** NUEVO WHERE DEL REPORTE
	    
	    mundo.setFiltrosMap(forma.getFiltros());
	    mundo.setViasDeIngreso(viaIngreso);
	    mundo.setTiposPaciente(tipoPaciente);
	    mundo.setUsuario(usuario.getLoginUsuario());
	   
	    mundo.setDxEgreso(forma.getDiagnosticosEgreso());
	    mundo.setDxEgreso("numRegistros", forma.getNumDiagEgreso());
	    
	    
	    // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteIngresosPorConvenio, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
	    
	    //***************** NUEVO WHERE DEL REPORTE
	    String where = mundo.crearWhereTotalReingresosXConvenio(con, mundo);
	    
	   comp.obtenerComponentesDataSet("dataSet");
	   
	   //****************** MODIFICAR CONSULTA
	   String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
	   comp.modificarQueryDataSet(newquery);
	   
	   logger.info("Query >>>"+newquery);
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);   
	   
	   newPathReport+="&i=0";
	   if(!newPathReport.equals(""))
	   {
		   request.setAttribute("isOpenReport", "true");
		   request.setAttribute("newPathReport", newPathReport);
	   }
	}

	/**
     * 
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
    private void generarReingresos(Connection con,EstadisticasIngresosForm forma, UsuarioBasico usuario,HttpServletRequest request) {
    	String nombreRptDesign = "reingresos.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nREINGRESOS\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso
    	filtroo+="[Vía de Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getFiltros("viaIngreso").toString()))+"]  ";
    	
    	// Tipo de Paciente
    	if(forma.getFiltros().containsKey("tipoPaciente"))
    		if (!forma.getFiltros("tipoPaciente").equals(""))
    			filtroo+="[Tipo de Paciente: "+Utilidades.obtenerNombreTipoPaciente(con, forma.getFiltros("tipoPaciente").toString())+"]  ";
    	
    	// Diagnostico de egreso
    	String diagnosticos="";
    	boolean hayDx=false;
    	for(int i=0; i<forma.getNumDiagEgreso(); i++){
			if(forma.getDiagnosticosEgreso("checkbox_"+i).equals("true")){
				diagnosticos+=forma.getDiagnosticosEgreso(i+"").toString().split(ConstantesBD.separadorSplit)[2]+", ";
				hayDx=true;
			}	
		}
    	if(hayDx)
    		filtroo+="[Dx Egreso: "+diagnosticos+"] ";
    	
    	
    	// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("") && !forma.getFiltros("convenio").equals(ConstantesBD.codigoNuncaValido+"")){
			int convenio;
			filtroo+="[Convenio: ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
		
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //******************
		
	    //***************** NUEVO WHERE DEL REPORTE
	    
	    mundo.setFiltrosMap(forma.getFiltros());
	    mundo.setViasDeIngreso(viaIngreso);
	    mundo.setTiposPaciente(tipoPaciente);
	    mundo.setUsuario(usuario.getLoginUsuario());
	   
	    mundo.setDxEgreso(forma.getDiagnosticosEgreso());
	    mundo.setDxEgreso("numRegistros", forma.getNumDiagEgreso());
	    
	    
	    // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteIngresosPorConvenio, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
	    
	    //***************** NUEVO WHERE DEL REPORTE
	    String where = mundo.crearWhereReingresos(con, mundo);
	    
	   comp.obtenerComponentesDataSet("dataSet");
	   
	   //****************** MODIFICAR CONSULTA
	   String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
	   comp.modificarQueryDataSet(newquery);
	   
	   logger.info("Query >>>"+newquery);
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);   
	   
	   newPathReport+="&i=0";
	   if(!newPathReport.equals(""))
	   {
		   request.setAttribute("isOpenReport", "true");
		   request.setAttribute("newPathReport", newPathReport);
	   }
    }
    	
    /**
     * 
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
	private void generarIngresosPorConvenio(Connection con,EstadisticasIngresosForm forma, UsuarioBasico usuario,HttpServletRequest request) {
		String nombreRptDesign = "IngresosXConvenio.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nINGRESOS POR CONVENIO\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso
    	filtroo+="[Vía de Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getFiltros("viaIngreso").toString()))+"]  ";
    	
    	// Tipo de Paciente
		if (forma.getFiltros().containsKey("tipoPaciente") && !forma.getFiltros("tipoPaciente").equals(""))
			filtroo+="[Tipo de Paciente: "+Utilidades.obtenerNombreTipoPaciente(con, forma.getFiltros("tipoPaciente").toString())+"]  ";
    	
		// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("") && !forma.getFiltros("convenio").equals(ConstantesBD.codigoNuncaValido+"")){
			int convenio;
			filtroo+="[Convenio: ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
		
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //******************
		
	    //***************** NUEVO WHERE DEL REPORTE
	    
	    mundo.setFiltrosMap(forma.getFiltros());
	    mundo.setViasDeIngreso(viaIngreso);
	    mundo.setTiposPaciente(tipoPaciente);
	    mundo.setUsuario(usuario.getLoginUsuario());
	    
	    // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteIngresosPorConvenio, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
	    
	    //***************** NUEVO WHERE DEL REPORTE
	    String where = mundo.crearWhereIngresosXConvenio(con, mundo);
	    
	   comp.obtenerComponentesDataSet("dataSet");
	   
	   //****************** MODIFICAR CONSULTA
	   String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
	   comp.modificarQueryDataSet(newquery);
	   
	   logger.info("Query >>>"+newquery);
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);        
	   
	   if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	}
	

	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
    private ActionForward accionCambiaTipoReporte(Connection con, ActionMapping mapping, EstadisticasIngresosForm forma, UsuarioBasico usuario) {
    	logger.info("NÚMERO DE DIAGNOSTICOS: "+forma.getNumDiagEgreso());
    	if (forma.getNumDiagEgreso()>0){
    		forma.setNumDiagEgreso(0);
    		forma.setDiagnosticosEgreso(new HashMap());
    		forma.setDiagSeleccionados("");
    	}
    		
    	UtilidadBD.closeConnection(con);
    	if(!forma.getFiltros("tipoReporte").toString().equals("")){
	    	int tipoReporte = Integer.parseInt(forma.getFiltros("tipoReporte").toString());
	    	forma.setFiltros(new HashMap());
	    	forma.setFiltros("tipoReporte",tipoReporte);
			forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
			
			// Inicializar mapa de convenios
			HashMap convenios = new HashMap();
	    	convenios.put("numRegistros", 0);
			forma.setConveniosMap(convenios);
			
			// Inicializar mapa de empresas
			HashMap empresas = new HashMap();
	    	empresas.put("numRegistros", 0);
			forma.setEmpresasMap(empresas);
			
    	}
		return mapping.findForward("principal");
	}

	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, EstadisticasIngresosForm forma, UsuarioBasico usuario) {
		forma.reset();
		HashMap mapAux = new HashMap();
		String tiposServicio= "'"+ConstantesBD.codigoServicioProcedimiento+"','"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioNoCruentos+"','"+ConstantesBD.codigoServicioPaquetes+"','"+ConstantesBD.codigoServicioInterconsulta+"'";
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setSexos(Utilidades.obtenerSexos(con));
		forma.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
		
		mapAux.put("institucion", usuario.getCodigoInstitucion());
		mapAux.put("activo", "true");
		
		// Cargar Convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", true, "", true));
		for(int i=0; i<forma.getConvenios().size(); i++){
			HashMap aux = (HashMap)forma.getConvenios().get(i);
			aux.put("seleccionado", ConstantesBD.acronimoNo);
		}
		
		//Cargar Empresas
		forma.setEmpresas(UtilidadesFacturacion.obtenerEmpresas(con));
		for(int i=0; i<forma.getEmpresas().size(); i++){
			HashMap aux = (HashMap)forma.getEmpresas().get(i);
			aux.put("seleccionado", ConstantesBD.acronimoNo);
		}
		
		forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
		Utilidades.imprimirMapa(forma.getFiltros());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @return
	 */
	private String crearCadenaConComas(HashMap mapa, String llave, boolean agregarCodigoNoValido){
		String cadena="";
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); i++){
			cadena += mapa.get(llave+"_"+i)+", ";
		}
		if (agregarCodigoNoValido)
			cadena+=ConstantesBD.codigoNuncaValido+"";
		return "";
	}	
}