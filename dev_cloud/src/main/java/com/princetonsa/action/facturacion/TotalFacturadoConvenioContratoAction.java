package com.princetonsa.action.facturacion;

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
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.TotalFacturadoConvenioContratoForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.TotalFacturadoConvenioContrato;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class TotalFacturadoConvenioContratoAction extends Action 
{

	
	/**
	 * 
	 */
	Logger logger= Logger.getLogger(TotalFacturadoConvenioContratoAction.class);
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof TotalFacturadoConvenioContratoForm) 
		{
			TotalFacturadoConvenioContratoForm forma=(TotalFacturadoConvenioContratoForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			ActionErrors errores = new ActionErrors();
			
			TotalFacturadoConvenioContrato mundo = new TotalFacturadoConvenioContrato();
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de TotalFacturadoConvenioContratoAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("cargarContratos"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("generar"))
			{
				return this.accionGenerar(con, mundo, forma, usuario, mapping, request, institucion);
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de TotalFacturadoConvenioContratoForm");
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
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param institucion
	 * @throws SQLException 
	 */
	private ActionForward accionGenerar(Connection con,TotalFacturadoConvenioContrato mundo, TotalFacturadoConvenioContratoForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion) throws SQLException 
	{
		
		this.generarLog(con, forma, usuario);
		
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
		{
			if(forma.getMes().equals(""))
			{
				return this.generarReporteAnio(con, forma, mapping, request, usuario, mundo);
			}
			else
			{
				return this.generarReporteMes(con, forma, mapping, request, usuario, mundo);
			}
		}
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
		{
			if(forma.getMes().equals(""))
			{	
				return this.generarArchivoPlanoAnio(con, forma, mundo, usuario, request, mapping, institucion);
			}
			else
			{
				return this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
			}
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void generarLog(Connection con, TotalFacturadoConvenioContratoForm forma, UsuarioBasico usuario) 
	{
		String excluirFacturas="";
		
		if(forma.getExcluirFacturas().equals(ConstantesBD.acronimoSi))
		{
			excluirFacturas="SI";
		}
		else
		{
			excluirFacturas="NO";
		}
		
		String log=
		
			"\n ========================INFORMACION CONSULTADA=============================" +
			"\n*  Nombre Reporte			[TOTAL FACTURADO CONVENIO CONTRATO] "+
			"\n*  Tipo Salida				["+ValoresPorDefecto.getIntegridadDominio(forma.getTipoSalida())+"] " +
			"\n*  Año						["+forma.getAnio()+"] " +
			"\n*  Mes                       ["+forma.getMes()+"] " +
			"\n*  Convenio                  ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenio())) +"] " +
			"\n*  Contrato                  ["+forma.getContrato()+"] " +
			"\n*  Excluir Facturas          ["+excluirFacturas+"] "+
			
			"\n ============================================================================\n";
		
		LogsAxioma.enviarLog(ConstantesBD.logTotalFacturadoConvenioContratoCodigo,log,ConstantesBD.tipoRegistroLogInsercion,usuario.getLoginUsuario());
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlanoAnio(Connection con, TotalFacturadoConvenioContratoForm forma, TotalFacturadoConvenioContrato mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".csv", extZip = ".zip" ;
		String periodo = "", encabezadoArchivo = "", mes="", path="";
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		String nombresArchivos="";
		
		for(int i=1;i<13;i++)
		{	
			if(i<10)
				mes="0"+i;
			else
				mes=i+"";
			
			forma.setMapaConsultaFacturado(mundo.consultarTotalFacturado(con, forma, mes));
			encabezadoArchivo = forma.getAnio()+"-"+mes;
			
			
			String encabezado = "Convenio, Contrato, Valor Convenio, Valor Paciente, Valor Total";
			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia("01/"+mes+"/"+forma.getAnio(),UtilidadFecha.getFechaActual()))
			{	
				if(Utilidades.convertirAEntero(forma.getMapaConsultaFacturado().get("numRegistros")+"") > 0)
				{
					//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
					path = ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
					logger.info("====>Path: "+path);
					//Validamos si el path esta vacio o lleno
			    	if(UtilidadTexto.isEmpty(path))
					{
			    		forma.setMensaje(new ResultadoBoolean(true, ""));
			    		forma.setArchivo(false);
				    	forma.setZip(false);
						UtilidadBD.abortarTransaccion(con);
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Facturación", "error.facturacion.rutaNoDefinida", true);
					}
			    	else
			    	{
			    		//Cargamos el path original. La ruta establcecida en el web.xml concatenada con la ruta establecida en el parametro general
			    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+forma.getAnio()+"_"+usuario.getLoginUsuario()+"/";
			    		//Generar el Nombre del Archivo
			    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("totalFacturadoXConvenioContrato", encabezadoArchivo, usuario.getLoginUsuario());
			    		//Cargamos los datos de la consulta en un StringBuffer para ser impreso en el archivo plano
			    		datosArchivos = mundo.totalFacturadoConvenioContrato(forma.getMapaConsultaFacturado(), "Total Facturado Por Convenio/Contrato", encabezadoArchivo, encabezado, usuario.getLoginUsuario());
			    		logger.info("===>Nombre Archivo: "+nombre);
			    		logger.info("===>Path Final Archivo: "+path);
			    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extTxt);
			    		
			    		if(archivo)
			    			nombresArchivos+=" "+path+nombre+extTxt+" ";
			    		
			    		//Validamos si el archivo si se creo
			    		/*if(archivo)
					    {
				    		//Validamos si se genero el .zip o no?. Con la intención de indicarle al usuario que el zip no se genero
			    			if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extTxt) != ConstantesBD.codigoNuncaValido)
				    		{
				    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+nombre+extZip);
				    			logger.info("===>Path Archivo: "+forma.getPathArchivoTxt());
					    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extTxt+"!!!!!"));
					    		forma.setArchivo(true);
					    		forma.setZip(true);
					    		
				    		}
				    		else
				    		{
				    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extTxt+"!!!!!"));
				    			forma.setArchivo(true);
						    	forma.setZip(false);
						    }
					    }
			    		else
					    {
					    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+extTxt+"!!!!!"));
					    	forma.setArchivo(false);
					    	forma.setZip(false);
					    }*/
			    	}
			    }
			    /*else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true,"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
			    	forma.setArchivo(false);
			    	forma.setZip(false);
			    }*/
			}
		}
		
		logger.info("nombreArchivos----->"+nombresArchivos);
		
		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+forma.getAnio()+"_"+usuario.getLoginUsuario()+extZip+" "+nombresArchivos) != ConstantesBD.codigoNuncaValido)
		{
			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+forma.getAnio()+"_"+usuario.getLoginUsuario()+extZip);
			logger.info("===>Path Archivo: "+forma.getPathArchivoTxt());
    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+forma.getAnio()+"_"+usuario.getLoginUsuario()+extZip+"!!!!!"));
    		forma.setArchivo(true);
    		forma.setZip(true);
    		
		}
		else
		{
			logger.info("SE ESTA METIENDO POR ACA >>>>>>>>>> ");
	    }
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, TotalFacturadoConvenioContratoForm forma, TotalFacturadoConvenioContrato mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		//Llenamos el HashMap con los datos de la consulta de consumos por facturar de pacientes
		forma.setMapaConsultaFacturado(mundo.consultarTotalFacturado(con, forma, ""));
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".csv", extZip = ".zip" ;
		String periodo = "", encabezadoArchivo = "";
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		encabezadoArchivo = forma.getAnio()+"-"+forma.getMes();
		String encabezado = "Convenio, Contrato, Valor Convenio, Valor Paciente, Valor Total";
		if(Utilidades.convertirAEntero(forma.getMapaConsultaFacturado().get("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
			logger.info("====>Path: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		forma.setMensaje(new ResultadoBoolean(true, ""));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Facturación", "error.facturacion.rutaNoDefinida", true);
			}
	    	else
	    	{
	    		//Cargamos el path original. La ruta establcecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("totalFacturadoXConvenioContrato", periodo, usuario.getLoginUsuario());
	    		//Cargamos los datos de la consulta en un StringBuffer para ser impreso en el archivo plano
	    		datosArchivos = mundo.totalFacturadoConvenioContrato(forma.getMapaConsultaFacturado(), "Total Facturado Por Convenio/Contrato", encabezadoArchivo, encabezado, usuario.getLoginUsuario());
	    		logger.info("===>Nombre Archivo: "+nombre);
	    		logger.info("===>Path Final Archivo: "+path);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extTxt);
	    		//Validamos si el archivo si se creo
	    		if(archivo)
			    {
		    		//Validamos si se genero el .zip o no?. Con la intención de indicarle al usuario que el zip no se genero
	    			if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extTxt) != ConstantesBD.codigoNuncaValido)
		    		{
		    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+nombre+extZip);
		    			logger.info("===>Path Archivo: "+forma.getPathArchivoTxt());
			    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extTxt+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extTxt+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
		    		}
			    }
	    		else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+extTxt+"!!!!!"));
			    	forma.setArchivo(false);
			    	forma.setZip(false);
			    	UtilidadBD.abortarTransaccion(con);
			    	UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
			    }
	    	}
	    }
	    else
	    {
	    	forma.setMensaje(new ResultadoBoolean(true,"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
	    }
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param mundo
	 */
	private ActionForward generarReporteMes(Connection con, TotalFacturadoConvenioContratoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, TotalFacturadoConvenioContrato mundo) 
	{
		String nombreRptDesign = "TotalFacturadoConvenioContratoMes.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,1, "                                                         FACTURACION");
        comp.insertLabelInGridPpalOfHeader(2,1, "                                TOTAL FACTURADO POR CONVENIO/CONTRATO");
        
        String condiciones="";
        String periodo=forma.getAnio()+"-"+forma.getMes();
        if(!forma.getConvenio().equals(""))
        {
        	condiciones+=" - Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenio()));
        }
        if(!forma.getContrato().equals(""))
        {
        	condiciones+=" - Contrato: "+forma.getContrato();
        }
        comp.insertLabelInGridPpalOfHeader(3,2,"     Parametros de Busqueda.");
        comp.insertLabelInGridPpalOfHeader(4,2,"       Periodo: "+periodo +condiciones );
        
        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
        comp.obtenerComponentesDataSet("TotalFacturadoMes");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        String consultaMod= mundo.cambiarConsulta(con, forma.getExcluirFacturas(), forma.getConvenio(), forma.getContrato(), periodo);
        
        String newQuery=comp.obtenerQueryDataSet().replace("1=1", consultaMod);
        comp.modificarQueryDataSet(newQuery);
        
        logger.info("New >>>>>>>>> "+newQuery);
        logger.info("Mod >>>>>>>>> "+consultaMod);
        
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
        newPathReport += "&Periodo="+periodo;
        
        
       if(!newPathReport.equals(""))
       {
    	   request.setAttribute("isOpenReport", "true");
    	   request.setAttribute("newPathReport", newPathReport);
       }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}

	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param mundo
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward generarReporteAnio(Connection con, TotalFacturadoConvenioContratoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, TotalFacturadoConvenioContrato mundo) throws SQLException 
	{
		
		int numFacturasImpresas = 0;
		Vector archivosGenerados=new Vector();
        HashMap archivosGeneradosBirt=new HashMap();
        archivosGeneradosBirt.put("numRegistros", "0");
        for(int i=1;i<13;i++)
		{
			String nombreRptDesign = "TotalFacturadoConvenioContratoMes.rptdesign";
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
			DesignEngineApi comp;
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
	        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,1,1,4);
	        Vector v=new Vector();
	        v.add(institucion.getRazonSocial());
	        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
	        v.add(institucion.getDireccion());
	        v.add("Tels. "+institucion.getTelefono());
	        comp.insertLabelInGridOfMasterPage(0,1,v);
	        comp.insertLabelInGridPpalOfHeader(1,1, "                                                         FACTURACION");
	        comp.insertLabelInGridPpalOfHeader(2,1, "                                TOTAL FACTURADO POR CONVENIO/CONTRATO");
	        String periodo="";
	        
        	if(i<10)
			{		
        		periodo=forma.getAnio()+"-"+"0"+i;
			}
        	else
        	{
        		periodo=forma.getAnio()+"-"+i;
        	}	
	        String condiciones="";
	        if(!forma.getConvenio().equals(""))
	        {
	        	condiciones+=" - Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenio()));
	        }
	        if(!forma.getContrato().equals(""))
	        {
	        	condiciones+=" - Contrato: "+forma.getContrato();
	        }
	        comp.insertLabelInGridPpalOfHeader(3,2,"     Parametros de Busqueda.");
	        comp.insertLabelInGridPpalOfHeader(4,2,"       Periodo: "+periodo +condiciones );
	        
	        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
	        comp.obtenerComponentesDataSet("TotalFacturadoMes");            
	        String oldQuery=comp.obtenerQueryDataSet();
	        
	        String consultaMod= mundo.cambiarConsulta(con, forma.getExcluirFacturas(), forma.getConvenio(), forma.getContrato(), periodo);
	        
	        String newQuery=comp.obtenerQueryDataSet().replace("1=1", consultaMod);
	        comp.modificarQueryDataSet(newQuery);
	        
	        logger.info("New >>>>>>>>> "+newQuery);
	        logger.info("Mod >>>>>>>>> "+consultaMod);
	        
	        
	       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	       comp.updateJDBCParameters(newPathReport);
	        
	        // se mandan los parámetros al reporte
	        newPathReport += "&Periodo="+periodo;
	        
	        
	        /*if(!newPathReport.equals(""))
	        {
	     	   request.setAttribute("isOpenReport", "true");
	     	   request.setAttribute("newPathReport", newPathReport);
	        }*/
       
	        archivosGeneradosBirt.put("isOpenReport_"+numFacturasImpresas, "true");
		    archivosGeneradosBirt.put("newPathReport_"+numFacturasImpresas, newPathReport);
			numFacturasImpresas++;
	   
		}
	    
       archivosGeneradosBirt.put("numRegistros", numFacturasImpresas+"");
	   request.setAttribute("archivos", archivosGenerados);
	   request.setAttribute("archivosBirt", archivosGeneradosBirt);
       request.setAttribute("nombreVentana", "Nuevo");
       UtilidadBD.closeConnection(con);
       return mapping.findForward("abrirNPdfBirt");
      
	}
	
}
