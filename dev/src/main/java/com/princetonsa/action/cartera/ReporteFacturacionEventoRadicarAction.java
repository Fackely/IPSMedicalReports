package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.cartera.ReporteFacturacionEventoRadicarForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.ReporteFacturacionEventoRadicar;
import com.princetonsa.mundo.glosas.ReporteEstadoCarteraGlosas;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ReporteFacturacionEventoRadicarAction extends Action
{
	private Logger logger = Logger.getLogger(ReporteFacturacionEventoRadicarAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
	{	
		if(form instanceof ReporteFacturacionEventoRadicarForm)
		{
			
			//se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			//se instancia la forma
			ReporteFacturacionEventoRadicarForm forma = (ReporteFacturacionEventoRadicarForm)form;		
			
			//se instancia el mundo
			ReporteFacturacionEventoRadicar mundo = new ReporteFacturacionEventoRadicar();
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			//se instancia la variable para manejar los errores.
			ActionErrors errores=new ActionErrors();
			
			forma.setMensaje(new ResultadoBoolean(false));
			
			//se instancia la variable para manejar los errores.
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	  EL ESTADO DE REPORTE FACTURACION EVENTO POR RADICAR ES ====>> "+estado);
			logger.info("\n***************************************************************************");
								
			// ESTADO --> NULL
			if(estado == null)
			{
				forma.reset(usuario.getCodigoInstitucionInt());
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
								 
				return mapping.findForward("paginaError");
			}	
			else if(estado.equals("empezar"))
			{
				return this.accionEmpezar(forma, mundo, mapping, usuario);					   			
			}
			else if(estado.equals("Imprimir"))
			{
				return this.accionImprimir(forma, mundo, mapping, usuario, request);	
			}
		}
		
		return null;
	}
	
	/** 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionImprimir(ReporteFacturacionEventoRadicarForm forma, ReporteFacturacionEventoRadicar mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		String convenio="-1", centroAtencion="-1", viaIngreso="-1",fechaIni="",fechaFin="",factIni="",factFin="";
        
        if(!forma.getConvenio().equals("-1"))
        	convenio=forma.getConvenio().split(ConstantesBD.separadorSplit)[0];

        if(!forma.getCentroAtencion().equals("-1"))
	        centroAtencion= forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0];

        if(!forma.getFechaElabIni().equals(""))
        	fechaIni= forma.getFechaElabIni();	          

        if(!forma.getFechaElabFin().equals(""))
        	fechaFin= forma.getFechaElabFin();

        if(!forma.getFactIni().equals(""))
        	factIni= "  Facura Inicial: "+forma.getFactIni();	          

        if(!forma.getFactFin().equals(""))
        	factFin= "  Factura Final: "+forma.getFactFin();
        
        if(!forma.getViaIngreso().equals("-1"))
        	viaIngreso= "  Via Ingreso: "+forma.getViaIngreso().split(ConstantesBD.separadorSplit)[1];
		
		String consulta="";
		
		if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 1)
		{	
        	consulta = ConsultasBirt.reporteFacturacionEventoRadicar(centroAtencion, 
						convenio, fechaIni, fechaFin, factIni, factFin, viaIngreso, 1);
		}
        else if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 2)
        {
        	consulta = ConsultasBirt.reporteFacturacionEventoRadicar(centroAtencion, 
					convenio, fechaIni, fechaFin, factIni, factFin, viaIngreso, 2);
        }	
		 
		HashMap tmp=ReporteEstadoCarteraGlosas.ejecutarQuery(consulta);
		 			 		 
		logger.info("\n\ntmp.get(numRegistros):: "+tmp.get("numRegistros"));
		if(Utilidades.convertirAEntero(tmp.get("numRegistros")+"") > 0)
		{
			if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
				return accionGenerarReporte(forma, mapping, usuario, request);
			else
			{
				if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
				{
					mundo.crearCvs(forma, usuario);				
				}
			}
			forma.setNumReg(1);
		}		
		else
			forma.setNumReg(0);
		
		return mapping.findForward("principal");
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
	private ActionForward accionGenerarReporte(ReporteFacturacionEventoRadicarForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{			
		if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 1)
		{		
			String nombreRptDesign = "ReporteFacturacionEventoRadicar.rptdesign";
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			Vector v;
			
			//***************** INFORMACI�N DEL CABEZOTE
	        DesignEngineApi comp; 
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/",nombreRptDesign);
	         
	        logger.info("\n\nel logo:: "+ins.getUbicacionLogo());
	        
	        //Logo
	        if(ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
	        	comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	        else
	        	comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	        
	        // Nombre Instituci�n, titulo y rango de fechas
	        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	        v=new Vector();
	        v.add("\n\nHOSPITAL "+ins.getRazonSocial());
	        v.add("NIT "+ins.getNit()+"\n\nREPORTE FACTURACI�N EVENTO POR RADICAR\nTipo Reporte: Facturas sin Cuenta de Cobro");
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        
	        // Parametros de Generaci�n
	        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	        v=new Vector();
	        
	        String filtro="Parametros de Generaci�n:";
	        
	        // Centro Atencion
	        if(!forma.getCentroAtencion().equals("-1"))
	        filtro += "  Centro Atenci�n: "+forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[1];
	       
	        // Convenio
	        if(!forma.getConvenio().equals("-1"))
	        	filtro += " Convenio: "+forma.getConvenio().split(ConstantesBD.separadorSplit)[1];
	        	
	        // Fecha Elab Inicial
	        if(!forma.getFechaElabIni().equals(""))
	        	filtro += "  Fecha Elaboraci�n Inicial: "+forma.getFechaElabIni();	          

	        // Fecha Elab Final
	        if(!forma.getFechaElabFin().equals(""))
	        	filtro += "  Fecha Elaboraci�n Final: "+forma.getFechaElabFin();

	        // Factura Inicial
	        if(!forma.getFactIni().equals(""))
	        	filtro += "  Facura Inicial: "+forma.getFactIni();	          

	        // Factura Final
	        if(!forma.getFactFin().equals(""))
	        	filtro += "  Factura Final: "+forma.getFactFin();
	        
	        // Via Ingreso
	        if(!forma.getViaIngreso().equals("-1"))
	        	filtro += "  Via Ingreso: "+forma.getViaIngreso().split(ConstantesBD.separadorSplit)[1];
	        
	        v.add(filtro);
	        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	        
	        // Fecha hora de proceso y usuario
	        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	        
	        //***************** NUEVO WHERE DEL REPORTE
	        
	        String newquery = "";
	 	   
	        int convenio=-1;
	        String tipoconvenio="-1";
	        
	        if(!forma.getConvenio().equals("-1"))
	        	convenio=Utilidades.convertirAEntero(forma.getConvenio().split(ConstantesBD.separadorSplit)[0]);
	        
	        comp.obtenerComponentesDataSet("dataSet");
		       newquery = ConsultasBirt.reporteFacturacionEventoRadicar(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0],
		    		   forma.getConvenio().split(ConstantesBD.separadorSplit)[0], forma.getFechaElabIni(), forma.getFechaElabFin(),
		    		   forma.getFactIni(), forma.getFactFin(), forma.getViaIngreso().split(ConstantesBD.separadorSplit)[0],1);
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
		}
		else if(Utilidades.convertirAEntero(forma.getTipoReporte()) == 2)
		{
			String nombreRptDesign = "ReporteFacturacionEventoSinRadicar.rptdesign";
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			Vector v;
			
			//***************** INFORMACI�N DEL CABEZOTE
	        DesignEngineApi comp; 
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/",nombreRptDesign);
	         
	        logger.info("\n\nel logo:: "+ins.getUbicacionLogo());
	        
	        //Logo
	        if(ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
	        	comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
	        else
	        	comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	        
	        // Nombre Instituci�n, titulo y rango de fechas
	        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	        v=new Vector();
	        v.add("\n\nHOSPITAL "+ins.getRazonSocial());
	        v.add("NIT "+ins.getNit()+"\n\nREPORTE FACTURACI�N EVENTO POR RADICAR\nTipo Reporte: Facturas con Cuenta de Cobro sin Radicar");
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        
	        // Parametros de Generaci�n
	        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	        v=new Vector();
	        
	        String filtro="Parametros de Generaci�n:";
	        
	        // Centro Atencion
	        if(!forma.getCentroAtencion().equals("-1"))
	        filtro += "  Centro Atenci�n: "+forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[1];
	       
	        // Convenio
	        if(!forma.getConvenio().equals("-1"))
	        	filtro += " Convenio: "+forma.getConvenio().split(ConstantesBD.separadorSplit)[1];
	        	
	        // Fecha Elab Inicial
	        if(!forma.getFechaElabIni().equals(""))
	        	filtro += "  Fecha Elaboraci�n Inicial: "+forma.getFechaElabIni();	          

	        // Fecha Elab Final
	        if(!forma.getFechaElabFin().equals(""))
	        	filtro += "  Fecha Elaboraci�n Final: "+forma.getFechaElabFin();

	        // Factura Inicial
	        if(!forma.getFactIni().equals(""))
	        	filtro += "  Facura Inicial: "+forma.getFactIni();	          

	        // Factura Final
	        if(!forma.getFactFin().equals(""))
	        	filtro += "  Factura Final: "+forma.getFactFin();
	        
	        // Via Ingreso
	        if(!forma.getViaIngreso().equals("-1"))
	        	filtro += "  Via Ingreso: "+forma.getViaIngreso().split(ConstantesBD.separadorSplit)[1];
	        
	        v.add(filtro);
	        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	        
	        // Fecha hora de proceso y usuario
	        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	        
	        //***************** NUEVO WHERE DEL REPORTE
	        
	        String newquery = "";
	 	   
	        int convenio=-1;
	        String tipoconvenio="-1";
	        
	        if(!forma.getConvenio().equals("-1"))
	        	convenio=Utilidades.convertirAEntero(forma.getConvenio().split(ConstantesBD.separadorSplit)[0]);
	        
	        comp.obtenerComponentesDataSet("dataSet");
		       newquery = ConsultasBirt.reporteFacturacionEventoRadicar(forma.getCentroAtencion().split(ConstantesBD.separadorSplit)[0],
		    		   forma.getConvenio().split(ConstantesBD.separadorSplit)[0], forma.getFechaElabIni(), forma.getFechaElabFin(),
		    		   forma.getFactIni(), forma.getFactFin(), forma.getViaIngreso().split(ConstantesBD.separadorSplit)[0],2);
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
		}
	    return mapping.findForward("principal");
	}	
	
	/**
     * Inicia en el forward de Reporte Facturacion Evento Radicar
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(ReporteFacturacionEventoRadicarForm forma, ReporteFacturacionEventoRadicar mundo, ActionMapping mapping, UsuarioBasico usuario) throws SQLException 
	{	
		forma.reset(usuario.getCodigoInstitucionInt());
		Connection con = null;
		con = UtilidadBD.abrirConexion();
				
		HashMap centrosAtencion= new HashMap();
		centrosAtencion=Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), true);
		forma.getListaCentrosAtencion().add(centrosAtencion);
		forma.setListaViaIngreso(Utilidades.obtenerViasIngresoTipoPaciente(con));
		forma.setListaConvenio(Utilidades.obtenerConvenios (con, "", ConstantesBD.codigoTipoContratoEvento+"", false, "", false));
			
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principal");
	}
}
