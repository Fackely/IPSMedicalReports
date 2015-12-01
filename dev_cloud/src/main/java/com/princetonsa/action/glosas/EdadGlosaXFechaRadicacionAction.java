package com.princetonsa.action.glosas;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.EdadGlosaXFechaRadicacionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.EdadGlosaXFechaRadicacion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class EdadGlosaXFechaRadicacionAction extends Action 
{
	Logger logger = Logger.getLogger(EdadGlosaXFechaRadicacionAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (response == null)
			;
		if (form instanceof EdadGlosaXFechaRadicacionForm) {

			EdadGlosaXFechaRadicacionForm forma = (EdadGlosaXFechaRadicacionForm) form;
			String estado = forma.getEstado();

			ActionErrors errores = new ActionErrors();

			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

			logger.info("*********************************");
			logger.info("\n ESTADO --> " + estado + "\n\n");
			logger.info("*********************************");
			
			if (estado == null) 
			{
				forma.reset();
				logger.warn("Estado No Valido Dentro Del Flujo de Consultar e Imprimir Respuestas de Glosas (null)");
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
				return mapping.findForward("paginaError");
			} 
			
			else if (estado.equals("empezar")) 
			{
				return accionEmpezar(forma, usuario, request, mapping);
			}
			else if (estado.equals("generarReporte"))
			{
				return accionBuscarDocs(forma, usuario, request, mapping);
			}
		}
		return null;
	}

	private ActionForward accionBuscarDocs(EdadGlosaXFechaRadicacionForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) 
	{
		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return accionGenerarReporte(forma, mapping, usuario, request);
		else
			if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			{
				EdadGlosaXFechaRadicacion.crearCvs(forma, usuario);				
			}
		
		return mapping.findForward("busquedaGlosas");
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
	private ActionForward accionGenerarReporte(EdadGlosaXFechaRadicacionForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{			
		String nombreRptDesign = "EdadGlosaXFRad.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
         
        logger.info("\n\nel logo:: "+ins.getUbicacionLogo());
        
        //Logo
        if(ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
        	comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        else
        	comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        
        // Nombre Institución, titulo y rango de fechas
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
        v=new Vector();
        v.add("\n\nHOSPITAL "+ins.getRazonSocial());
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
        	v.add("NIT "+ins.getNit()+"\n\nREPORTE EDAD GLOSA X FECHA DE RADICACIÓN\nRESUMIDO");
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
        	v.add("NIT "+ins.getNit()+"\n\nREPORTE EDAD GLOSA X FECHA DE RADICACIÓN\nDETALLADO POR FACTURA");
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        	v.add("NIT "+ins.getNit()+"\n\nREPORTE EDAD GLOSA X FECHA DE RADICACIÓN\nDETALLADO POR CUENTA DE COBRO");
        
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        // Parametros de Generación
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
        v=new Vector();
        
        String filtro="Parametros de Generación:";
        
        // Fecha Corte
        filtro += "  Fecha Corte: "+forma.getGlosaBusqueda().getFechaRegistroGlosa();
       
        // Tipo Convenio
        if(!(forma.getGlosaBusqueda().getTipoConvenio()).equals("-1"))
        	filtro += " Tipo Convenio: "+forma.getGlosaBusqueda().getTipoConvenio().split(ConstantesBD.separadorSplit)[1];
        	
        // Convenio
        if(!forma.getGlosaBusqueda().getConvenio().getNombre().equals("-1") && !forma.getGlosaBusqueda().getConvenio().getNombre().equals(""))
        	filtro += "  Convenio: "+forma.getGlosaBusqueda().getConvenio().getNombre().split(ConstantesBD.separadorSplit)[1];	          
               	        
        v.add(filtro);
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        
        //***************** NUEVO WHERE DEL REPORTE
        
        String newquery = "";
 	   
        int convenio=-1;
        String tipoconvenio="-1";
        
        if(!forma.getGlosaBusqueda().getConvenio().getNombre().equals("-1"))
        	convenio=Utilidades.convertirAEntero(forma.getGlosaBusqueda().getConvenio().getNombre().split(ConstantesBD.separadorSplit)[0]);
        	        
        if(!forma.getGlosaBusqueda().getTipoConvenio().equals("-1"))
        	tipoconvenio=forma.getGlosaBusqueda().getTipoConvenio().split(ConstantesBD.separadorSplit)[0];
        
        comp.obtenerComponentesDataSet("dataSet");
        
        HashMap criterios = new HashMap();
        
        criterios.put("fecha", forma.getGlosaBusqueda().getFechaRegistroGlosa());
        if(!forma.getGlosaBusqueda().getTipoConvenio().equals("") && !forma.getGlosaBusqueda().getTipoConvenio().equals("-1"))
        	criterios.put("tipoConvenio", forma.getGlosaBusqueda().getTipoConvenio().split(ConstantesBD.separadorSplit)[0]);
        else
        	criterios.put("tipoConvenio", "");
        if(!forma.getGlosaBusqueda().getConvenio().getNombre().equals("") && !forma.getGlosaBusqueda().getConvenio().getNombre().equals("-1"))
        	criterios.put("convenio", forma.getGlosaBusqueda().getConvenio().getNombre().split(ConstantesBD.separadorSplit)[0]);
        else
        	criterios.put("convenio", "");
        
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
        	criterios.put("consulta", "1");
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
        	criterios.put("consulta", "2");
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        	criterios.put("consulta", "3");        
        
        newquery = ConsultasBirt.EdadGlosaFechaRadicacion(criterios);
        comp.modificarQueryDataSet(newquery);
    		       
        //logger.info("\n\nconsulta:: "+newquery);
	       
         //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
         comp.lowerAliasDataSet();
         String newPathReport = comp.saveReport1(false);
         comp.updateJDBCParameters(newPathReport);
       if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }             
		
	    return mapping.findForward("busquedaGlosas");
	}	
	
	private ActionForward accionEmpezar(EdadGlosaXFechaRadicacionForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) 
	{
		Connection con=UtilidadBD.abrirConexion();
		forma.reset();
		forma.setTiposConvenios(Utilidades.obtenerTiposConvenio(con, usuario.getInstitucion()));
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaGlosas");
	}
}