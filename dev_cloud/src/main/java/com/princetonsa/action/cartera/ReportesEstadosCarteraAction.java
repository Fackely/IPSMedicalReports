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

import com.princetonsa.actionform.cartera.ReportesEstadosCarteraForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.ReportesEstadosCartera;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * @author wilson
 *
 */
public class ReportesEstadosCarteraAction extends Action 
{
	Logger logger =Logger.getLogger(ReportesEstadosCarteraAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof ReportesEstadosCarteraForm) 
		{
			ReportesEstadosCarteraForm forma=(ReportesEstadosCarteraForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
						
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
				if(UtilidadTexto.isEmpty(forma.getEsCapitado()))
					forma.setEsCapitado(ConstantesBD.acronimoNo);
				forma.reset(usuario.getCodigoInstitucionInt());
				UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else if(estado.equals("recargarConvenios"))
			{
				forma.setConvenio(ConstantesBD.codigoNuncaValido);
				UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else if(estado.equals("continuar"))
			{
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
				logger.warn("Estado no valido dentro del flujo de REPORTES ESTADOS CARTERA ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de ReportesEstadosCarteraForm");
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
	private ActionForward generarArchivoPlano(Connection con, ReportesEstadosCarteraForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		DesignEngineApi comp;
		ReportesEstadosCartera mundo= new ReportesEstadosCartera();
		HashMap datosEstadoCartera = new HashMap();
		HashMap datosTotalesEstadoCartera = new HashMap();
		String nombre = "", oldQuery = "", newQuery = "", encabezado = "", tipoReporte = "", exTxt = ".txt", exZip = ".zip", indiceEmpresa = "", indiceConvenio = "";
		StringBuffer datosArchivos = new StringBuffer();
		String nomRep = "Estado Cartera Evento";
		String periodo = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"-"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
		UtilidadBD.iniciarTransaccion(con);
		
		//***************** INICIO TIPOS DE REPORTES CAPITACION NO *************************************
		//Tipo de Reporte Resumido por Convenio
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraEvento/","EstadoCarteraResumidoXConvenios.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("estadoCarteraEvento", forma.getTipoReporte(), periodo);
            tipoReporte = "Resumido";
            encabezado = "Convenio, Estado, Valor Inicial, Ajustes Debito, Ajustes Credito, Pagos Aplicados, Saldo";
            //Obtengo la primera consulta del birt "Consulta Estado Cartera Evento"
            comp.obtenerComponentesDataSet("ReporteResumido");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\n\n\nNueva Consulta Estado Cartera Evento->"+newQuery);
    		datosEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
    		//Obtengo la segunda consulta del birt "Total Estado Cartera Evento"
    		comp.obtenerComponentesDataSet("totalCartera");    
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery = ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta Total Cartera Evento->"+newQuery);
            //Se mandan las key's de la consulta de empresa y convenio para no mas funciones en el mundo
    		indiceEmpresa = "nombreempresa_";
    		indiceConvenio = "nombreconvenio_";
            datosTotalesEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
    		datosArchivos = ReportesEstadosCartera.cargarMapa(datosEstadoCartera, datosTotalesEstadoCartera, nomRep, forma.getTipoReporte(), tipoReporte, periodo, encabezado, indiceEmpresa, indiceConvenio);
        }
        
		//Tipo de Reporte Detallado por CxC
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraEvento/","EstadoCarteraDetalladoXCuentaCobro.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("estadoCarteraEvento", forma.getTipoReporte(), periodo);
            tipoReporte = "Detallado por CxC";
            encabezado = "Empresa-Convenio/Cuenta de Cobro, Estado, Valor Inicial, Ajustes Debito, Ajustes Credito, Pagos Aplicados, Saldo";
            //Obtengo la primera consulta del birt "Consulta Estado Cartera Evento"
            comp.obtenerComponentesDataSet("ReporteDetallado");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\n\n\nNueva Consulta Estado Cartera Evento->"+newQuery);
    		datosEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
    		//Obtengo la segunda consulta del birt "Total Estado Cartera Evento"
    		comp.obtenerComponentesDataSet("totalCartera");    
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery = ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta Total Cartera Evento->"+newQuery);
            //Se mandan las key's de la consulta de empresa y convenio para no mas funciones en el mundo
    		indiceEmpresa = "nombreempresaconvenio_";
    		indiceConvenio = "numerocuentacobro_";
            datosTotalesEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
            datosArchivos = ReportesEstadosCartera.cargarMapa(datosEstadoCartera, datosTotalesEstadoCartera, nomRep, forma.getTipoReporte(), tipoReporte, periodo, encabezado, indiceEmpresa, indiceConvenio);
        }
		
		//Tipo de Reporte Detallado por Factura
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraEvento/","EstadoCarteraDetalladoXFactura.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("estadoCarteraEvento", forma.getTipoReporte(), periodo);
            tipoReporte = "Detallado por Factura";
            encabezado = "Factura, Estado, Valor Inicial, Ajustes Debito, Ajustes Credito, Pagos Aplicados, Saldo";
            //Obtengo la primera consulta del birt "Consulta Estado Cartera Evento"
            comp.obtenerComponentesDataSet("ReporteDetallado");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\n\n\nNueva Consulta Estado Cartera Evento->"+newQuery);
    		datosEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
    		//Obtengo la segunda consulta del birt "Total Estado Cartera Evento"
    		comp.obtenerComponentesDataSet("totalCartera");    
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery = ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta Total Cartera Evento->"+newQuery);
            //Se mandan las key's de la consulta de empresa y convenio para no mas funciones en el mundo
    		indiceEmpresa = "nomempconvcuentacobro_";
    		indiceConvenio = "factura_";
            datosTotalesEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
            datosArchivos = ReportesEstadosCartera.cargarMapa(datosEstadoCartera, datosTotalesEstadoCartera, nomRep, forma.getTipoReporte(), tipoReporte, periodo, encabezado, indiceEmpresa, indiceConvenio);
        }
		//***************** FIN TIPOS DE REPORTES CAPITACION NO *************************************
		
		
		//***************** INICIO TIPOS DE REPORTES CAPITACION SI -- ES PRACTICAMENTE LO MISMO PERO LO HAGO APARTE POR SI HAY CAMBIOS
		//Tipo de Reporte Resumido por Convenio
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsCapitado().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraCapitacion/","EstadoCarteraResumidoXConveniosCapitacion.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("estadoCarteraEvento", forma.getTipoReporte(), periodo);
            tipoReporte = "Resumido";
            encabezado = "Convenio, Estado, Valor Inicial, Ajustes Debito, Ajustes Credito, Pagos Aplicados, Saldo";
            //Obtengo la primera consulta del birt "Consulta Estado Cartera Evento"
            comp.obtenerComponentesDataSet("ReporteResumido");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\n\n\nNueva Consulta Estado Cartera Evento->"+newQuery);
    		datosEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
    		//Obtengo la segunda consulta del birt "Total Estado Cartera Evento"
    		comp.obtenerComponentesDataSet("totalCartera");    
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery = ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta Total Cartera Evento->"+newQuery);
            //Se mandan las key's de la consulta de empresa y convenio para no mas funciones en el mundo
    		indiceEmpresa = "nombreempresa_";
    		indiceConvenio = "nombreconvenio_";
            datosTotalesEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
            datosArchivos = ReportesEstadosCartera.cargarMapa(datosEstadoCartera, datosTotalesEstadoCartera, nomRep, forma.getTipoReporte(), tipoReporte, periodo, encabezado, indiceEmpresa, indiceConvenio);
        }
		
		//Tipo de Reporte Detallado por CxC
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta) && forma.getEsCapitado().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraCapitacion/","EstadoCarteraDetalladoXCuentaCobroCapitacion.rptdesign");
            nombre = util.TxtFile.armarNombreArchivoConPeriodo("estadoCarteraEvento", forma.getTipoReporte(), periodo);
            tipoReporte = "Detallado por CxC";
            encabezado = "Empresa-Convenio/Cuenta de Cobro, Estado, Valor Inicial, Ajustes Debito, Ajustes Credito, Pagos Aplicados, Saldo";
            //Obtengo la primera consulta del birt "Consulta Estado Cartera Evento"
            comp.obtenerComponentesDataSet("ReporteDetallado");
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\n\n\nNueva Consulta Estado Cartera Evento->"+newQuery);
    		datosEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
    		//Obtengo la segunda consulta del birt "Total Estado Cartera Evento"
    		comp.obtenerComponentesDataSet("totalCartera");    
            oldQuery = comp.obtenerQueryDataSet();
            llenarMundo(mundo, forma);
            newQuery = ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta Total Cartera Evento->"+newQuery);
            //Se mandan las key's de la consulta de empresa y convenio para no mas funciones en el mundo
    		indiceEmpresa = "nombreempresaconvenio_";
    		indiceConvenio = "numerocuentacobro_";
            datosTotalesEstadoCartera = ReportesEstadosCartera.ejecutarConsultaEstadoCarteraEvento(con, newQuery);
            datosArchivos = ReportesEstadosCartera.cargarMapa(datosEstadoCartera, datosTotalesEstadoCartera, nomRep, forma.getTipoReporte(), tipoReporte, periodo, encabezado, indiceEmpresa, indiceConvenio);
        }
		//***************** FIN TIPOS DE REPORTES CAPITACION SI
		
		//Generacion Archivo Plano
		if(Utilidades.convertirAEntero(datosEstadoCartera.get("numRegistros")+"") > 0 && Utilidades.convertirAEntero(datosTotalesEstadoCartera.get("numRegistros")+"") > 0)
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
											ReportesEstadosCarteraForm forma,
											UsuarioBasico usuario,
											HttpServletRequest request,
											ActionMapping mapping) 
    {
        DesignEngineApi comp;
        ReportesEstadosCartera mundo= new ReportesEstadosCartera();
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraEvento/","EstadoCarteraResumidoXConvenios.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteResumido");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("totalCartera");            
            oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
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
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraEvento/","EstadoCarteraDetalladoXCuentaCobro.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("totalCartera");            
            oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
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
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraEvento/","EstadoCarteraDetalladoXFactura.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("totalCartera");            
            oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
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
        
        //PARTE DE CAPITACION -- ES PRACTICAMENTE LO MISMO PERO LO HAGO APARTE POR SI HAY CAMBIOS
        
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteResumido) && forma.getEsCapitado().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraCapitacion/","EstadoCarteraResumidoXConveniosCapitacion.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteResumido");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("totalCartera");            
            oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
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
        else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoCuenta) && forma.getEsCapitado().equals(ConstantesBD.acronimoSi))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"cartera/estadoCarteraCapitacion/","EstadoCarteraDetalladoXCuentaCobroCapitacion.rptdesign");
            armarEncabezado(comp, con, usuario, forma, request);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("ReporteDetallado");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            String newQuery= ReportesEstadosCartera.armarConsultaEstadoCarteraEvento(mundo, oldQuery); 
            logger.info("\nNueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            
            /*modificamos consulta con los criterios de busqueda*/
            comp.obtenerComponentesDataSet("totalCartera");            
            oldQuery=comp.obtenerQueryDataSet();
            logger.info("\nQuery original del design->"+oldQuery);
            llenarMundo(mundo, forma);
            newQuery= ReportesEstadosCartera.armarConsultaTOTALESestadoCarteraEvento(mundo, oldQuery); 
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
	 * @param comp
	 * @param con
	 * @param usuario
	 * @return
	 */
	private void armarEncabezado(DesignEngineApi comp, Connection con, UsuarioBasico usuario, ReportesEstadosCarteraForm forma, HttpServletRequest request)
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
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoFactura) && forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {
			tipoReporte=" Tipo: Detallado por Factura,";
        }
        
        if(forma.getEsCapitado().equals(ConstantesBD.acronimoNo))
        {	
        	v.add("ESTADO CARTERA EVENTO");
        }
        else
        {
        	v.add("ESTADO CARTERA CAPITADA");
        }
        v.add("PERIODO: "+forma.getFechaInicial()+" - "+forma.getFechaFinal());
        
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
	
	
	
	
	/**
	 * 
	 * @param mundo
	 * @param forma
	 */
	private void llenarMundo(ReportesEstadosCartera mundo, ReportesEstadosCarteraForm forma)
	{
		mundo.setConvenio(forma.getConvenio());
		mundo.setEmpresaInstitucion(forma.getEmpresaInstitucion());
		mundo.setEmpresa(forma.getEmpresa());
		mundo.setEsCapitado(forma.getEsCapitado());
		mundo.setEstadoCuenta(forma.getEstadoCuenta());
		mundo.setFechaFinal(forma.getFechaFinal());
		mundo.setFechaInicial(forma.getFechaInicial());
		mundo.setTipoReporte(forma.getTipoReporte());
		mundo.setManejaConversionMoneda(forma.getManejaConversionMoneda());
		mundo.setIndex(forma.getIndex());
		mundo.setTiposMonedaTagMap(forma.getTiposMonedaTagMap());
	}
	

	
}