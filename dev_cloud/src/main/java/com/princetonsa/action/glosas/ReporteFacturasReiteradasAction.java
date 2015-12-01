package com.princetonsa.action.glosas;

import java.io.IOException;
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
import org.apache.struts.action.ActionMessage;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ReporteFacturasReiteradasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ReporteFacturasReiteradas;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


public class ReporteFacturasReiteradasAction extends Action
{
	private Logger logger = Logger.getLogger(ReporteFacturasReiteradasAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
	{	
		if(form instanceof ReporteFacturasReiteradasForm)
		{
			
			//se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			//se instancia la forma
			ReporteFacturasReiteradasForm forma = (ReporteFacturasReiteradasForm)form;		
			
			//se instancia el mundo
			ReporteFacturasReiteradas mundo = new ReporteFacturasReiteradas();
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			//se instancia la variable para manejar los errores.
			ActionErrors errores=new ActionErrors();
			
			forma.setMensaje(new ResultadoBoolean(false));
			
			//se instancia la variable para manejar los errores.
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	  EL ESTADO DE REPORTE FACTURAS REITERADAS ES ====>> "+estado);
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
				return this.accionEmpezar(forma, mundo, mapping, usuario, request);					   			
			}
			else if(estado.equals("filtrarConvenios"))
			{
				return this.accionFiltrarConvenios(forma, response);	
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
	private ActionForward accionImprimir(ReporteFacturasReiteradasForm forma, ReporteFacturasReiteradas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{		
		boolean guardo= false;
		
		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			guardo= this.accionGenerarReporte(forma, mapping, usuario, request);
		else
			if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			{
				guardo=mundo.crearCvs(forma, usuario);				
			}
		
		if(guardo)
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"La operaci�n no finaliz� satisfactoriamente"));
		
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
	private boolean accionGenerarReporte(ReporteFacturasReiteradasForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{			
		String nombreRptDesign = "ReporteFacturasReiteradas.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACI�N DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
         
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
        v.add("NIT "+ins.getNit()+"\n\nREPORTE FACTURAS REITERADAS");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        // Parametros de Generaci�n
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
        v=new Vector();
        
        String filtro="Parametros de Generaci�n:";
        
        // Fecha Corte
        filtro += "  Fecha Corte: "+forma.getFechaCorte();
       
        // Tipo Convenio
        if(!forma.getTipoConvenio().equals("-1"))
        	filtro += " Tipo Convenio: "+forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[1];
        	
        // Convenio
        if(!forma.getConvenio().equals("-1") && !forma.getConvenio().equals(""))
        	filtro += "  Convenio: "+forma.getConvenio().split(ConstantesBD.separadorSplit)[1];	          
               	        
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
        	        
        if(!forma.getTipoConvenio().equals("-1"))
        	tipoconvenio=forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[0];
        
        comp.obtenerComponentesDataSet("dataSet");
	       newquery = ConsultasBirt.reporteFacturasReiteradas(
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()), 
					tipoconvenio, 
					convenio, 
					usuario.getCodigoInstitucionInt());
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
        return true;
	}
	
	/**
	 * M�todo para filtrar los contratos al cambiar el convenio
	 * 
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarConvenios(ReporteFacturasReiteradasForm forma,HttpServletResponse response) 
	{
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		String resultado = "<respuesta>" + "<infoid>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>convenio</id-select>"
				+ "<id-arreglo>convenios</id-arreglo>" + "</infoid>";

		if(!forma.getTipoConvenio().equals("-1"))
			forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false, forma.getTipoConvenio().split(ConstantesBD.separadorSplit)[0]));
		else
			forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		
		for (HashMap elemento : forma.getConvenios()) {
			resultado += "<convenios>";
			resultado += "<codigo>" + elemento.get("codigoConvenio")+ConstantesBD.separadorSplit+elemento.get("nombreConvenio") + "</codigo>";
			resultado += "<descripcion>"
					+ elemento.get("nombreConvenio")
					+" "
					+ elemento.get("activo")
					 + "</descripcion>";
			resultado += "</convenios>";
		}

		resultado += "</respuesta>";
		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} catch (IOException e) {
			logger
					.error("Error al enviar respuesta AJAX en accionFiltrarContratos: "
							+ e);
		}
		return null;
	}
	
	/**
     * Inicia en el forward de Reporte Estado Cartera y Glosas
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(ReporteFacturasReiteradasForm forma, ReporteFacturasReiteradas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{	
		String numGlosasPorFactura="";
		ActionErrors errores= new ActionErrors();
		
		numGlosasPorFactura=ValoresPorDefecto.getNumeroGlosasRegistradasXFactura(usuario.getCodigoInstitucionInt());
		logger.info("\n\nel parametro:: "+numGlosasPorFactura);
		if(Utilidades.convertirAEntero(numGlosasPorFactura) > 1)
		{
			forma.reset(usuario.getCodigoInstitucionInt());
		
			Connection con = null;
			con = UtilidadBD.abrirConexion();
					
			forma.setListaTiposConvenio(Utilidades.obtenerTiposConvenio(con, usuario.getCodigoInstitucionInt()+""));	
			forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
			
			UtilidadBD.cerrarConexion(con);						
		}
		else
		{
			forma.setMostrarVolver(ConstantesBD.acronimoSi);
			errores.add("descripcion", new ActionMessage("errors.notEspecific", "No se permite el ingreso de glosas reiteradas en el sistema. Verificar lo definido en el parámetro Número Glosas Registradas por Factura Se cancela el Proceso. El reporte no puede ser generado."));
			saveErrors(request, errores);
		}		
			
		return mapping.findForward("principal");
	}
}