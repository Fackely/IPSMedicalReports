package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.capitacion.EdadCarteraCapitacionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.EdadCarteraCapitacion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


public class EdadCarteraCapitacionAction extends Action 
{
	
Logger logger =Logger.getLogger(EdadCarteraCapitacionAction.class);
	
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof EdadCarteraCapitacionForm) 
		{
			EdadCarteraCapitacionForm forma=(EdadCarteraCapitacionForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			
						
			//forma.setMensaje(new ResultadoBoolean(false));
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				forma.reset(usuario.getCodigoInstitucionInt());
			    return mapping.findForward("principal");
			}
			else if(estado.equals("continuar"))
			{
				UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else if (estado.equals("imprimir"))
			{
				return this.generarReporte(con, forma, usuario, request, mapping);
			}
			else
			{
				forma.reset(usuario.getCodigoInstitucionInt());
				logger.warn("Estado no valido dentro del flujo de EDAD CARTERA CAPITACION ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de EdadCarteraCapitacionForm");
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
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward generarReporte(Connection con, EdadCarteraCapitacionForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		DesignEngineApi comp;
        EdadCarteraCapitacion mundo= new EdadCarteraCapitacion();
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraCapitacion/","EdadCarteraCapitacionResumido.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteResumido");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCarteraCapitacion.armarConsultaReporte(mundo, oldQuery);
           
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
            comp.updateJDBCParameters(newPathReport);
            
            newPathReport += "&fechaCorte="+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte());
            
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
//            comp.updateJDBCParameters(newPathReport);
        }
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraCapitacion/","EdadCarteraCapitacionDetalladoXCxC.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCarteraCapitacion.armarConsultaReporte(mundo, oldQuery); 
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
            comp.updateJDBCParameters(newPathReport);
        }
        UtilidadBD.closeConnection(con);
	    return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param mundo
	 * @param forma
	 */
	private void llenarMundo(EdadCarteraCapitacion mundo, EdadCarteraCapitacionForm forma) 
	{
		mundo.setContrato(forma.getContrato());
		mundo.setConvenioCapitado(forma.getConvenioCapitado());
		mundo.setFechaCorte(forma.getFechaCorte());
		mundo.setTipoConvenio(forma.getTipoConvenio());
		mundo.setTipoReporte(forma.getTipoReporte());
		mundo.setManejaConversionMoneda(forma.getManejaConversionMoneda());
		mundo.setIndex(forma.getIndex());
		mundo.setTiposMonedaTagMap(forma.getTiposMonedaTagMap());
	}


	/**
	 * 
	 * @param comp
	 * @param con
	 * @param usuario
	 * @return
	 */
	private void armarEncabezado(DesignEngineApi comp, Connection con, UsuarioBasico usuario, EdadCarteraCapitacionForm forma, HttpServletRequest request)
	{
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,7);
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
        v.add("CARTERA CAPITACION");
        
        String tipoReporte="";
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
        {
			tipoReporte=" Tipo: Resumido";
	    }
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        {
			tipoReporte=" Tipo: Detallado por CxC,";
	    }
		v.add("EDAD CARTERA CAPITACION");
        v.add("FECHA CORTE: "+forma.getFechaCorte());
        if(forma.getManejaConversionMoneda() && forma.getIndex()>0)
        {
        	logger.info(forma.getTiposMonedaTagMap("descripciontipomoneda_"+forma.getIndex()));
        	logger.info(forma.getTiposMonedaTagMap("descripciontipomoneda_"+forma.getIndex()));
        	v.add("Valores Conversion Moneda: "+forma.getTiposMonedaTagMap("descripciontipomoneda_"+forma.getIndex())+" "+forma.getTiposMonedaTagMap("simbolotipomoneda_"+forma.getIndex())+" "+UtilidadTexto.formatearValores(forma.getTiposMonedaTagMap("factorconversion_"+forma.getIndex())+""));
        }
        else
        {
        	v.add("");
        }
        v.add(tipoReporte+" Usuario: "+usuario.getLoginUsuario());
        comp.insertLabelInGridOfMasterPage(0,1,v);
    }
}