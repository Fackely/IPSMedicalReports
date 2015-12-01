package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cartera.EdadCarteraForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.EdadCartera;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * @author wilson
 *
 */
public class EdadCarteraAction extends Action
{
	Logger logger =Logger.getLogger(EdadCarteraAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof EdadCarteraForm) 
		{
			EdadCarteraForm forma=(EdadCarteraForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			forma.setMensaje(new ResultadoBoolean(false));
						
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de EdadCarteraFechaRadicacionForm (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				if(UtilidadTexto.isEmpty(forma.getEsFactura()))
					forma.setEsFactura(ConstantesBD.acronimoNo);
			    forma.reset(usuario.getCodigoInstitucionInt());
			    UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else if(estado.equals("recargarConvenios"))
			{
				forma.setConvenioNormal(ConstantesBD.codigoNuncaValido);
				UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else if (estado.equals("imprimir"))
			{
				if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			    {
			    	return this.generarReporte(con, forma, usuario, request, mapping);
			    }
			    else
			    {
			    	return this.generarArchivoPlano(con, forma, usuario, request, mapping, institucion);
			    }
			}
			else if(estado.equals("cancelarProceso"))
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else
			{
				forma.reset(usuario.getCodigoInstitucionInt());
				logger.warn("Estado no valido dentro del flujo de EDAD CARTERA X FECHA RADICACION ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de EdadCarteraFechaRadicacionForm");
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
	 * Metodo que genera el Archivo Plano
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion 
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, EdadCarteraForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		DesignEngineApi comp;
		EdadCartera mundo = new EdadCartera();
		HashMap datos = new HashMap();
		String nombre = "", oldQuery = "", nomRep = "", encabezado = "", tipoReporte = "", exTxt = ".txt", exZip = ".zip", indiceConvenio = "", indiceFactura = "";
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		
		if(forma.getEsFactura().equals(ConstantesBD.acronimoNo))
		{
			nomRep = "Edad Cartera por Fecha Radicacion";
			encabezado = "Convenio, Por Radicar, 0, 1 a 30, 31 a 60, 61 a 90, 91 a 120, 121 a 150, 151 a 180, 181 a 360, > 360, Total Radicado, Total Cartera Vencida";
		}
		else if(forma.getEsFactura().equals(ConstantesBD.acronimoSi))
		{
			nomRep = "Edad Cartera por Fecha Factura";
			encabezado = "Convenio, 0, 1 a 30, 31 a 60, 61 a 90, 91 a 120, 121 a 150, 151 a 180, 181 a 360, > 360, Total Cartera Vencida";
		}
		
		/*EDAD CARTERA FECHA RADICACION*/
		//Tipo de Reporte Resumido por Convenio
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaRadicacion/","EdadCarteraResumido.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("edadCarteraXFechaRadicacion", forma.getTipoReporte(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()));
            tipoReporte = "Resumido";
    		comp.obtenerComponentesDataSet("ReporteResumido");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
    		String newQuery = EdadCartera.armarConsultaReporte(mundo, oldQuery);
    		logger.info("\n\n\nNueva Consulta->"+newQuery);
    		datos = EdadCartera.ejecutarConsultaReporte(con, newQuery);
    		datosArchivos = EdadCartera.cargarMapaResumidoRadicacion(datos, nomRep, tipoReporte, forma.getFechaCorte(), encabezado);
        }
		
		//Tipo de Reporte Detallado por CxC
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta) && forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaRadicacion/","EdadCarteraDetalladoXCxC.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("edadCarteraXFechaRadicacion", forma.getTipoReporte(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()));
            tipoReporte = "Detallado por CxC";
    		comp.obtenerComponentesDataSet("ReporteDetallado");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
    		String newQuery = EdadCartera.armarConsultaReporte(mundo, oldQuery);
    		logger.info("\n\n\nNueva Consulta->"+newQuery);
    		//Se mandan las key's de la consulta de convenio y cuenta de cobro para no generar otra funcion en el mundo
    		indiceConvenio = "nombreconvenio_";
    		indiceFactura = "numerocuentacobro_";
    		datos = EdadCartera.ejecutarConsultaReporte(con, newQuery);
    		datosArchivos = EdadCartera.cargarMapaDetalladoFacturaCxcRadicacion(datos, nomRep, tipoReporte, forma.getFechaCorte(), encabezado, indiceConvenio, indiceFactura);
        }
		
		//Tipo de Reporte Detallado por Factura
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaRadicacion/","EdadCarteraDetalladoXFactura.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("edadCarteraXFechaRadicacion", forma.getTipoReporte(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()));
            tipoReporte = "Detallado por Factura";
    		comp.obtenerComponentesDataSet("ReporteDetallado");
    		oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
    		String newQuery = EdadCartera.armarConsultaReporte(mundo, oldQuery);
    		logger.info("\n\n\nNueva Consulta->"+newQuery);
    		//Se mandan las key's de la consulta de convenio y factura para no generar otra funcion en el mundo
    		indiceConvenio = "nombreconveniocuentacobro_";
    		indiceFactura = "factura_";
    		datos = EdadCartera.ejecutarConsultaReporte(con, newQuery);
    		datosArchivos = EdadCartera.cargarMapaDetalladoFacturaCxcRadicacion(datos, nomRep, tipoReporte, forma.getFechaCorte(), encabezado, indiceConvenio, indiceFactura);
        }
		
		/*EDAD CARTERA FECHA FACTURA*/
		//Tipo de Reporte Resumido por Convenio
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsFactura().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaFactura/","EdadCarteraResumidoXFechaFactura.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("edadCarteraXFechaFactura", forma.getTipoReporte(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()));
            tipoReporte = "Resumido";
    		comp.obtenerComponentesDataSet("ReporteResumido");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
    		String newQuery = EdadCartera.armarConsultaReporte(mundo, oldQuery);
    		logger.info("\n\n\nNueva Consulta->"+newQuery);
    		datos = EdadCartera.ejecutarConsultaReporte(con, newQuery);
    		datosArchivos = EdadCartera.cargarMapaResumidoFactura(datos, nomRep, tipoReporte, forma.getFechaCorte(), encabezado);
        }
		
		//Tipo de Reporte Detallado por Factura
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsFactura().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaFactura/","EdadCarteraDetalladoXFechaFactura.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("edadCarteraXFechaFactura", forma.getTipoReporte(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte()));
            tipoReporte = "Detallado por Factura";
    		comp.obtenerComponentesDataSet("ReporteDetallado");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
    		String newQuery = EdadCartera.armarConsultaReporte(mundo, oldQuery);
    		logger.info("\n\n\nNueva Consulta->"+newQuery);
    		datos = EdadCartera.ejecutarConsultaReporte(con, newQuery);
    		datosArchivos = EdadCartera.cargarMapaDetalladoFactura(datos, nomRep, tipoReporte, forma.getFechaCorte(), encabezado);
        }
		
		if(Utilidades.convertirAEntero(datos.get("numRegistros")+"") > 0)
	    {
	    	String path = ValoresPorDefecto.getReportPath()+"cartera/";
	    	boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, exTxt);
	    	if(archivo)
		    {
	    		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+exZip+" "+path+nombre+exTxt) != ConstantesBD.codigoNuncaValido)
	    		{
	    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+"cartera/"+nombre+exZip);
		    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "+path+nombre+exTxt+"!!!!!"));
		    		forma.setArchivo(true);
		    		UtilidadBD.finalizarTransaccion(con);
		    		UtilidadBD.closeConnection(con);			    	
			    	return mapping.findForward("principal");
	    		}
	    		else
	    		{
	    			forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO "+path+nombre+exZip+"!!!!!"));
			    	forma.setArchivo(false);
			    	UtilidadBD.abortarTransaccion(con);
			    	UtilidadBD.closeConnection(con);			    	
					return mapping.findForward("principal");
	    		}
		    }
		    else
		    {
		    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO "+path+nombre+exTxt+"!!!!!"));
		    	forma.setArchivo(false);
		    	UtilidadBD.abortarTransaccion(con);
		    	UtilidadBD.closeConnection(con);		    	
				return mapping.findForward("principal");
		    }
	    }
	    else
	    {
	    	forma.setMensaje(new ResultadoBoolean(true,"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);	    	
			return mapping.findForward("principal");
	    }
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
	private ActionForward generarReporte(	Connection con, 
											EdadCarteraForm forma,
											UsuarioBasico usuario,
											HttpServletRequest request,
											ActionMapping mapping) 
    {
        DesignEngineApi comp;
        EdadCartera mundo= new EdadCartera();
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaRadicacion/","EdadCarteraResumido.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteResumido");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCartera.armarConsultaReporte(mundo, oldQuery); 
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
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta) && forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaRadicacion/","EdadCarteraDetalladoXCxC.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCartera.armarConsultaReporte(mundo, oldQuery); 
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
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaRadicacion/","EdadCarteraDetalladoXFactura.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCartera.armarConsultaReporte(mundo, oldQuery); 
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
        
        //X FACTURA, ES PRACTICAMENTE LO MISMO, LO HICE APARTE PARA FUTURAS MODIFICACIONES
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsFactura().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaFactura/","EdadCarteraResumidoXFechaFactura.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteResumido");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCartera.armarConsultaReporte(mundo, oldQuery); 
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
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsFactura().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/edadCarteraXFechaFactura/","EdadCarteraDetalladoXFechaFactura.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= EdadCartera.armarConsultaReporte(mundo, oldQuery); 
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
	private void llenarMundo(EdadCartera mundo, EdadCarteraForm forma) 
	{
		mundo.setConvenioNormal(forma.getConvenioNormal());
		mundo.setEsFactura(forma.getEsFactura());
		mundo.setFechaCorte(forma.getFechaCorte());
		mundo.setTipoConvenio(forma.getTipoConvenio());
		mundo.setTipoReporte(forma.getTipoReporte());
		mundo.setManejaConversionMoneda(forma.getManejaConversionMoneda());
		mundo.setIndex(forma.getIndex());
		mundo.setTiposMonedaTagMap(forma.getTiposMonedaTagMap());
		mundo.setEmpresaInstitucion(forma.getEmpresaInstitucion());
	}

	/**
	 * 
	 * @param comp
	 * @param con
	 * @param usuario
	 * @return
	 */
	private void armarEncabezado(DesignEngineApi comp, Connection con, UsuarioBasico usuario, EdadCarteraForm forma, HttpServletRequest request)
	{
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,7);
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+"  -  "+institucionBasica.getNit());     
        v.add("CUENTAS POR COBRAR");
        
        String tipoReporte="";
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido))
        {
			tipoReporte=" Tipo: Resumido Por Convenio, ";
	    }
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta))
        {
			tipoReporte=" Tipo: Detallado por CxC,";
	    }
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura))
        {
			tipoReporte=" Tipo: Detallado por Factura,";
        }
        
        if(forma.getEsFactura().equals(ConstantesBD.acronimoNo))
        {	
        	v.add("EDAD CARTERA X FECHA RADICACION");
        }
        else
        {
        	v.add("EDAD CARTERA X FECHA FACTURA");
        }
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
