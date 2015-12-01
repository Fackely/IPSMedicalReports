/*
 * Ago 20, 2008 
 */
package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ReporteMortalidadForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ReporteMortalidad;
import com.princetonsa.mundo.manejoPaciente.ValoresTipoReporte;
import com.princetonsa.pdf.ReporteMortalidadPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Sebastián Gómez R.
 * Clase usada para controlar los procesos de la funcionalidad
 * Reporte mortalidad
 *
 */
public class ReporteMortalidadAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ReporteMortalidadAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof ReporteMortalidadForm)
		{
			
			//SE ABRE CONEXION
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
						
			//OBJETOS A USAR
			ReporteMortalidadForm reporteForm =(ReporteMortalidadForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			String estado=reporteForm.getEstado(); 
			logger.warn("\n\n En ReporteMortalidadAction el Estado ["+estado+"] \n\n");
			
			
			if(estado == null)
			{
				reporteForm.reset();	
				logger.warn("Estado no valido dentro del flujo de Reportte Mortalidad Action (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}	
			else if (estado.equals("empezar"))
			{
				return accionEmpezar(con,mapping,reporteForm,usuario);
			}
			else if (estado.equals("seleccionTipoReporte"))
			{
				return accionSeleccionTipoReporte(con,reporteForm,usuario,mapping,request);
			}
			else if (estado.equals("imprimir"))
			{
				return accionImprimir(con,reporteForm,usuario,mapping,request);
			}
			//*******ESTADOS PARA LOS FILTROS AJAX*****************************************
			else if(estado.equals("filtrarCentrosCosto"))
			{
				return accionFiltrarCentrosCosto(con,reporteForm,response,usuario);
			}
			else if (estado.equals("filtrarCentrosCostoNormal")) //Este se usa para filtrar los centros de costo que no son por vía de ingreso
			{
				return accionFiltrarCentrosCostoNormal(con,reporteForm,response,usuario);
			}
			//******************************************************************************
			else
			{
				reporteForm.reset();
				logger.warn("Estado no valido dentro del flujo de ReporteMortalidadAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
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
	 * Método implementado para realizar la impresión del reporte segun el tipo que se haya seleccionado
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con,
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) 
	{
		ActionForward actionForward = mapping.findForward("principal"); 
		
		if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteTasaMortalidadRangoEdadSexo)
		{
			generarReporteTasaMortalidadRangoEdadSexo(reporteForm,usuario,request);
		}
		else if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteNumeroPacientesFallecidosDxMuerte)
		{
			generarReporteNumPacFallecidosDxMuerte(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadMensualConvenio)
		{
			generarReporteMortalidadMensualConvenio(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteListadoPacientesFallecidos)
		{
			generarReporteListadoPacientesFallecidos(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadDxMuerteCentroCosto)
		{
			generarReporteMortalidadDxMuerteCentroCosto(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteEstanciaPromPacFallecidosRangoEdad)
		{
			generarReporteEstanciaPromPacFallRangoEdad(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempos)
		{
			generarReporteMortalidadRangoTiempos(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadSexo)
		{
			generarReporteMortalidadSexo(reporteForm,usuario,request);
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadGlobal)
		{
			generarReporteMortalidadGlobal(con,reporteForm,usuario,request);
			if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
				actionForward = mapping.findForward("abrirPdf");
		}
		else if (reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempoCentroCosto)
		{
			generarReporteMortalidadRangoTiempoCentroCosto(reporteForm,usuario,request);
		}

		UtilidadesManejoPaciente.insertarLogReportes(con, reporteForm.getTipoReporte(), usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
		
		UtilidadBD.closeConnection(con);
		return actionForward;
	}
	
	/**
	 * Método para generar la impresión del reporte de mortalidad por rango de tiempo y centro de costo
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteMortalidadRangoTiempoCentroCosto(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadRangoTiempoCentroCosto.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Dx. Muerte: "+(!UtilidadTexto.isEmpty(reporteForm.getDiagnosticosMuerte("valor")+"")?reporteForm.getDiagnosticosMuerte("valor").toString().split(ConstantesBD.separadorSplit)[0]:"")+"] ";
        cadena += "[Dx. Egreso: "+reporteForm.getDiagnosticoEgreso().getAcronimo()+"] ";
        String listadoCentrosCosto = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getCentrosCosto("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getCentrosCosto("checkbox_"+i).toString()))
        		listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":", ") + reporteForm.getCentrosCosto("nombre_"+i);
        cadena += "[Centro Costo: "+listadoCentrosCosto+"] ";
        v.add(cadena);
        v.add("Mortalidad por rango de tiempo y centro de costo");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //************************************************************************************************
        String consulta = ReporteMortalidad.obtenerConsultaMortalidadRangoTiempoCentroCosto(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoSexo(), reporteForm.getDiagnosticosMuerte(), reporteForm.getCentrosCosto(), reporteForm.getDiagnosticoEgreso(), usuario.getCodigoInstitucionInt());
        
        //******SE MODIFICA DATA SET DE MORTALIDAD RANGO DE TIEMPOS X CENTRO DE COSTO***************
        comp.obtenerComponentesDataSet("rangoTiempoCentroCosto");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
        ///SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadRangoTiempoCentroCosto");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
        
	}

	/**
	 * Método para generar la impresión del reporte de mortalidad global
	 * @param con 
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteMortalidadGlobal(
			Connection con, ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	
    	if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
    	{
	    	nombreArchivo="TasaMortalidadGlobal" + r.nextInt()  +".pdf";
	    	ReporteMortalidadPdf.pdfTasaMortalidadGlobal(con, ValoresPorDefecto.getFilePath(),nombreArchivo, reporteForm, usuario, request);
	    	
	    	
	    	request.setAttribute("nombreArchivo", System.getProperty("file.separator")+nombreArchivo);
	    	request.setAttribute("nombreVentana", "Tasa Mortalidad Global");
	    	
    	}
    	else
    	{
    		nombreArchivo="TasaMortalidadGlobal" + r.nextInt()  +".csv";
    		String nombreArchivoZip = nombreArchivo.replaceAll("csv", "zip");
	    	ReporteMortalidadPdf.csvTasaMortalidadGlobal(con, ValoresPorDefecto.getFilePath(),nombreArchivo, reporteForm, usuario, request);
	    	
	    	
	    	//Se comprime el archivo
	    	//se genera el archivo en formato Zip
			BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+nombreArchivoZip+" "+ValoresPorDefecto.getFilePath()+nombreArchivo);
			
			if(!UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getFilePath(), nombreArchivoZip))
			{
				ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific","No se pudo comprimir el archivo. Por favor reportar el error con el administrador del sistema"));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
			}
			else
			{
				//Se concatena la URL del archivo y la ruta real del archivo
				reporteForm.setUrlArchivoPlano(System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivoZip);
				reporteForm.setPathArchivoPlano(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+nombreArchivoZip);	
			}
    	}
		
	}

	/**
	 * Método para generar la impresión del reporte de mortalidad por sexo
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteMortalidadSexo(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadSexo.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        reporteForm.setNombreSexo("");
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Edad: "+reporteForm.getEdad()+"] ";
        cadena += "[Dx. Muerte: "+(!UtilidadTexto.isEmpty(reporteForm.getDiagnosticosMuerte("valor")+"")?reporteForm.getDiagnosticosMuerte("valor").toString().split(ConstantesBD.separadorSplit)[0]:"")+"] ";
        cadena += "[Dx. Egreso: "+reporteForm.getDiagnosticoEgreso().getAcronimo()+"] ";
        cadena += "[Centro costo: "+reporteForm.getNombreCentroCosto()+"] ";
        
        v.add(cadena);
        v.add("Mortalidad por sexo");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //************************************************************************************************
        
        String consulta = ReporteMortalidad.obtenerConsultaMortalidadSexo(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoSexo(), Utilidades.convertirAEntero(reporteForm.getEdad()), reporteForm.getDiagnosticosMuerte(), reporteForm.getCentrosCosto(), reporteForm.getDiagnosticoEgreso());
        
        //******SE MODIFICA DATA SET DE MORTALIDAD SEXOS***************
        comp.obtenerComponentesDataSet("mortalidadSexo");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadSexo");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
		
	}

	/**
	 * Método para generar la impesion del reporte de mortalidad por rango de tiempos
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteMortalidadRangoTiempos(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadRangoTiempos.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Edad: "+reporteForm.getEdad()+"] ";
        cadena += "[Dx. Muerte: "+(!UtilidadTexto.isEmpty(reporteForm.getDiagnosticosMuerte("valor")+"")?reporteForm.getDiagnosticosMuerte("valor").toString().split(ConstantesBD.separadorSplit)[0]:"")+"] ";
        cadena += "[Dx. Egreso: "+reporteForm.getDiagnosticoEgreso().getAcronimo()+"] ";
        cadena += "[Centro Costo: "+reporteForm.getNombreCentroCosto()+"] ";
        
        v.add(cadena);
        v.add("Mortalidad por rango de tiempos");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);        //************************************************************************************************
        
        String consulta = ReporteMortalidad.obtenerConsultaMortalidadRangoTiempos(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getDiagnosticosMuerte(),reporteForm.getCentrosCosto(), reporteForm.getDiagnosticoEgreso(), Utilidades.convertirAEntero(reporteForm.getEdad()),usuario.getCodigoInstitucionInt());
                
        //******SE MODIFICA DATA SET DE MORTALIDAD RANGO DE TIEMPOS***************
        comp.obtenerComponentesDataSet("rangoTiempos");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
		
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadRangoTiempos");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
	}

	/**
	 * Método que realiza la impresión del reporte de estancia promedio de pacientes fallecidos por rango de edad
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteEstanciaPromPacFallRangoEdad(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadEstanciaPromPacFallRangoEdad.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Estancia: "+reporteForm.getEstancia()+"] ";
        cadena += "[Dx. Muerte: "+(!UtilidadTexto.isEmpty(reporteForm.getDiagnosticosMuerte("valor")+"")?reporteForm.getDiagnosticosMuerte("valor").toString().split(ConstantesBD.separadorSplit)[0]:"")+"] ";
        cadena += "[Dx. Egreso: "+reporteForm.getDiagnosticoEgreso().getAcronimo()+"] ";
        cadena += "[Centro Costo: "+reporteForm.getNombreCentroCosto()+"] ";
        v.add(cadena);
        v.add("Estancia promedio de pacientes fallecidos por rango de edad");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //************************************************************************************************
        
        String[] consultas = ReporteMortalidad.obtenerConsultasEstanciaPromPacFallRangoEdad(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), Utilidades.convertirAEntero(reporteForm.getEstancia()), reporteForm.getDiagnosticosMuerte(), reporteForm.getDiagnosticoEgreso(), reporteForm.getCentrosCosto(), usuario.getCodigoInstitucionInt());
        
        //******SE MODIFICA DATA SET DE ESTANCIA PROMEDIO PACIENTE FALLECIDOS RANGO EDAD N°1**********
        comp.obtenerComponentesDataSet("estanciaPromPacFallRanEdad1");     
        comp.modificarQueryDataSet(consultas[0]);
        //****SE MODIFICA DATA SET DE ESTANCIA PROMEDIO PACIENTE FALLECIDOS RANGO EDAD N°2****************
        comp.obtenerComponentesDataSet("estanciaPromPacFallRanEdad2");     
        comp.modificarQueryDataSet(consultas[1]);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadEstanciaPromPacFallRangoEdad");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
		
	}

	/**
	 * Método que realiza la impresion del reporte de mortalidad por diagnostico de muerte
	 * y centro de costo
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteMortalidadDxMuerteCentroCosto(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadDxMuerteCentroCosto.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        String listadoDxMuerte = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getDiagnosticosMuerte("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosMuerte("checkbox_"+i).toString()))
        	{
        		String[]  vector = reporteForm.getDiagnosticosMuerte(i+"").toString().split(ConstantesBD.separadorSplit);
        		listadoDxMuerte += (listadoDxMuerte.equals("")?"":",") + vector[0];
        	}
        cadena += "[Dx. Muerte: "+listadoDxMuerte+"] ";
        String listadoCentrosCosto = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getCentrosCosto("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getCentrosCosto("checkbox_"+i).toString()))
        		listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":", ") + reporteForm.getCentrosCosto("nombre_"+i);
        cadena += "[Centros Costo: "+listadoCentrosCosto+"] ";
        v.add(cadena);
        v.add("Mortalidad por diagnóstico de muerte y centro de costo");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //************************************************************************************************
        
        String consulta = ReporteMortalidad.obtenerConsultaMortalidadDxMuerteCentroCosto(
        	reporteForm.getCodigoCentroAtencion(), 
        	reporteForm.getFechaInicial(), 
        	reporteForm.getFechaFinal(), 
        	reporteForm.getCodigoViaIngreso(), 
        	reporteForm.getDiagnosticosMuerte(), 
        	reporteForm.getCentrosCosto());
            
        //******SE MODIFICA DATA SET DE MORTALIDAD POR DIAGNOSTICO DE MUERTE Y CENTRO DE COSTO***************
        comp.obtenerComponentesDataSet("dxMuerteCentroCosto");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
		
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadDxMuerteCentroCosto");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
	}
	
	/**
	 * Método que realiza la impresion del reporte listado de pacientes fallecidos
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteListadoPacientesFallecidos(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadListadoPacFallecidos.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Edad: "+reporteForm.getEdad()+"] ";
        cadena += "[Dx. Egreso: "+reporteForm.getDiagnosticoEgreso().getAcronimo()+"] ";
        cadena += "[Dx. Muerte: "+(UtilidadTexto.isEmpty(reporteForm.getDiagnosticosMuerte("valor")+"")?"":reporteForm.getDiagnosticosMuerte("valor").toString().split(ConstantesBD.separadorSplit)[0])+"] ";
        cadena += "[Convenio: "+reporteForm.getNombreConvenio()+"] ";
        cadena += "[Estancia: "+reporteForm.getEstancia()+"] ";
        v.add(cadena);
        v.add("Listado pacientes fallecidos");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //************************************************************************************************
        
        String consulta = ReporteMortalidad.obtenerConsultaListadoPacientesFallecidos(
        	reporteForm.getCodigoCentroAtencion(), 
        	reporteForm.getFechaInicial(), 
        	reporteForm.getFechaFinal(), 
        	reporteForm.getCodigoViaIngreso(), 
        	Utilidades.convertirAEntero(reporteForm.getEdad()), 
        	reporteForm.getDiagnosticoEgreso(), 
        	reporteForm.getDiagnosticosMuerte(), 
        	Utilidades.convertirAEntero(reporteForm.getConvenios("codigo")+""), 
        	Utilidades.convertirAEntero(reporteForm.getEstancia()));
        
        //******SE MODIFICA DATA SET DE LISTADO PACIENTES FALLECIDOS***************
        comp.obtenerComponentesDataSet("listadoPacientesFallecidos");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadListadoPacFallecidos");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
	}

	/**
	 * Método implementado para generar la impresion del tipo de reporte
	 * de mortalidad mensual por convenio
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteMortalidadMensualConvenio(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadMensualConvenio.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Edad: "+reporteForm.getEdad()+"] ";
        cadena += "[Dx Egreso: "+reporteForm.getDiagnosticoEgreso().getAcronimo()+"] ";
        String listadoConvenios = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getConvenios("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getConvenios("checkbox_"+i).toString()))
        		listadoConvenios += (listadoConvenios.equals("")?"":", ") + reporteForm.getConvenios("nombre_"+i);
        cadena += "[convenios: "+listadoConvenios+"] ";
        v.add(cadena);
        v.add("Mortalidad Mensual Convenio");
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        
        //************************************************************************************************
		
        String consulta = ReporteMortalidad.obtenerConsultaMortalidadMensualConvenio(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getConvenios(),reporteForm.getDiagnosticoEgreso(),Utilidades.convertirAEntero(reporteForm.getEdad()));
        
        //******SE MODIFICA DATA SET DE MORTALIDAD MENSUAL CONVENIO***************
        comp.obtenerComponentesDataSet("mensualConvenio");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadMensualConvenio");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
	}

	/**
	 * Método implementado para generar la impresion del tipo de reporte
	 * de numero de pacientte fallecidos por diagnóstico de muerte
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteNumPacFallecidosDxMuerte(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadPacFallecidosDxMuerte.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        reporteForm.setNombreSexo("");
       
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        String listadoDiagnosticos = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getDiagnosticosMuerte("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosMuerte("checkbox_"+i).toString()))
        	{
        		String[] vector = reporteForm.getDiagnosticosMuerte(""+i).toString().split(ConstantesBD.separadorSplit);
        		listadoDiagnosticos += (listadoDiagnosticos.equals("")?"":", ") + vector[0];
        	}
        cadena += "[Dx. Muerte: "+listadoDiagnosticos+"] ";
        v.add(cadena);
        v.add("Número de pacientes fallecidos por diagnóstico de muerte");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //************************************************************************************************
        
        String consulta = ReporteMortalidad.obtenerConsultasNumeroPacFallecidosDxMuerte(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getDiagnosticosMuerte());
        
        //******SE MODIFICA DATA SET DE PACIENTES FALLECIDOS POR DIAGNÓSTICO DE MUERTE***************
        comp.obtenerComponentesDataSet("pacFallecidosDxMuerte");     
        comp.modificarQueryDataSet(consulta);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
		
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadPacFallecidosDxMuerte");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }
	}

	/**
	 * Método implementado para generar reporte de la tasa de mortalidad por rango de edad y sexo
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteTasaMortalidadRangoEdadSexo(
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "MortalidadRangoEdadSexo.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp;
        //Se inserta la imagen de la institucion
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 1, ins.getLogoReportes());
        
        //Se inserta la informacion de la institucion
        comp.insertGridHeaderOfMasterPage(0,0,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,0,v);
        
        
        //Se insertan parámetros de busqueda
        reporteForm.setNombreCentroAtencion(""); //se limpian los campos
        reporteForm.setNombreViaIngreso("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        v.add(cadena);
        v.add("Tasa de Mortalidad por Rango de edad y sexo");
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String[] consultas = ReporteMortalidad.obtenerConsultasTasaMortalidadRangoEdadSexo(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoSexo(), usuario.getCodigoInstitucionInt());
        
        //******SE MODIFICA DATA SET DE RANGO EDAD SEXO*************************************************
        comp.obtenerComponentesDataSet("rangoEdadSExo");     
        comp.modificarQueryDataSet(consultas[0]);
        //****SE MODIFICA DATA SET DE CONSULTA TOTAL EGRESOS******************************************
        comp.obtenerComponentesDataSet("consultaTotalEgresos");     
        comp.modificarQueryDataSet(consultas[1]);
        //************************************************************************************************
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {
        	
        	/**
        	 * NOTA: ESTA PORCION DE CÓDIGO ESTÁ DESHABILITADA PORQUE NO ESTÁ FUNCIONANDO, BIRT NO SOPORTA
        	 * ESTE TIPO DE REPORTE PARA LA GENERACIÓN DEL ARCHIVO .CSV. 
        	 */
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport, "MortalidadRangoEdadSexo");
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		reporteForm.setUrlArchivoPlano(rutas[0]);
        		reporteForm.setPathArchivoPlano(rutas[1]);
        	}
        	else
        	{
        		ActionErrors errores = new ActionErrors();
        		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
        		saveErrors(request, errores);
        		reporteForm.setEstado("seleccionTipoReporte");
        	}
        }	
	}

	/**
	 * Método implementado para filtrar los centros de costo normales al cambiar el centro de atencion
	 * @param con
	 * @param reporteForm
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrarCentrosCostoNormal(Connection con,
			ReporteMortalidadForm reporteForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		int codigoViaIngreso = Integer.parseInt(reporteForm.getIndex());
		
		//Si se tiene seleccionado una vía de ingreso válida se filtra por ella
		if(codigoViaIngreso!=ConstantesBD.codigoNuncaValido)
			reporteForm.setMapaCentrosCosto(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, codigoViaIngreso, reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), ""));
		else
			cargarCentrosCostoNormalXViaIngresoTodas(con, reporteForm, usuario);
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoCentroCosto</id-select>" +
				"<id-arreglo>centro-costo</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		for(int i=0;i<Utilidades.convertirAEntero(reporteForm.getMapaCentrosCosto().get("numRegistros")+"");i++)
		{
			resultado += "<centro-costo>";
				resultado += "<codigo>"+reporteForm.getMapaCentrosCosto().get("codigo_"+i)+"</codigo>";
				resultado += "<descripcion>"+reporteForm.getMapaCentrosCosto().get("nombre_"+i)+"</descripcion>";
			resultado += "</centro-costo>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarCentrosCostoNormal: "+e);
		}
		return null;
	}

	/**
	 * Método que recarga un tipo de reporte por el cambio del combo box de tipos de reporte
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionSeleccionTipoReporte(Connection con,
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) 
	{

		reporteForm.resetParametros(); //se limpian los parámetros
		reporteForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
		
		if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadDxMuerteCentroCosto||
				reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempoCentroCosto)
			cargarCentrosCostoXViaIngresoTodas(con, reporteForm, usuario);
		else if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteEstanciaPromPacFallecidosRangoEdad||
				reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempos||
				reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadSexo||
				reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteMortalidadGlobal)
			cargarCentrosCostoNormalXViaIngresoTodas(con,reporteForm,usuario);
			
		
		//********VALIDACIÓN DEL TIPO DE REPORTE**************************
		
		ActionErrors errores = new ActionErrors();
		switch(reporteForm.getTipoReporte())
		{
			case ConstantesBDManejoPaciente.tipoReporteTasaMortalidadRangoEdadSexo:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Tasa de Mortalidad Rango Edad Sexo"));
				reporteForm.setMostrarTipoSalidaArchivo(false);
			break;
			case ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempoCentroCosto:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Mortalidad por Rangos de Tiempos y Centro de Costo"));
			break;
			case ConstantesBDManejoPaciente.tipoReporteMortalidadGlobal:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Tasa Mortalidad Global"));
			break;
			case ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempos:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Mortalidad por rango de tiempos"));
			break;
			case ConstantesBDManejoPaciente.tipoReporteEstanciaPromPacFallecidosRangoEdad:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Estancia promedio de pacientes fallecidos por rango de edad"));
			break;
		}
		
		
		if(!errores.isEmpty())
		{
			reporteForm.setTipoReporte(ConstantesBD.codigoNuncaValido);
			saveErrors(request, errores);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que carga los centros de costo de las vias ingreso hospitalizacion y urgencias para la seleccion
	 * única de centros de costo
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 */
	private void cargarCentrosCostoNormalXViaIngresoTodas(Connection con,
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario) 
	{
		reporteForm.setMapaCentrosCosto(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoHospitalizacion, reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), ""));
		
		HashMap mapaCentrosCostoTemp = Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoUrgencias, reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), "");
		for(int i=0;i<Utilidades.convertirAEntero(mapaCentrosCostoTemp.get("numRegistros")+"");i++)
		{
			boolean existe = false;
			int numRegistros = Utilidades.convertirAEntero(reporteForm.getMapaCentrosCosto().get("numRegistros").toString());
			
			for(int j=0;j<Utilidades.convertirAEntero(reporteForm.getMapaCentrosCosto().get("numRegistros")+"");j++)
				if(reporteForm.getMapaCentrosCosto().get("codigo_"+j).toString().equals(mapaCentrosCostoTemp.get("codigo_"+i).toString()))
					existe = true;
			
			if(!existe)
			{
				reporteForm.getMapaCentrosCosto().put("codigo_"+numRegistros, mapaCentrosCostoTemp.get("codigo_"+i));
				reporteForm.getMapaCentrosCosto().put("nombre_"+numRegistros, mapaCentrosCostoTemp.get("nombre_"+i));
				numRegistros++;
				reporteForm.getMapaCentrosCosto().put("numRegistros",numRegistros+"");
			}
		}
		
	}

	/**
	 * Método implementado para filtrar los centros de costo
	 * @param con
	 * @param reporteForm
	 * @param response
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionFiltrarCentrosCosto(Connection con,
			ReporteMortalidadForm reporteForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		int codigoViaIngreso = Integer.parseInt(reporteForm.getIndex());
		
		//***************SE CONSULTA EL ARREGLO DE LOS CENTROS DE COSTO***********************************
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			reporteForm.setArregloCentrosCosto(Utilidades.getCentrosCosto(con, codigoViaIngreso+"", reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), "", 0, false, false, false));
		else if(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			reporteForm.setArregloCentrosCosto(Utilidades.getCentrosCosto(con, codigoViaIngreso+"", reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), "", 0, false, false, false));
		else
			cargarCentrosCostoXViaIngresoTodas(con,reporteForm,usuario);
		//**************************************************************************************
		
		//*********SE LIMPIA EL MAPA DE CENTROS DE COSTO***********************
		reporteForm.setCentrosCosto(new HashMap<String, Object>());
		reporteForm.setNumCentrosCosto(0);
		//**********************************************************************
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>centroCostoTemp</id-select>" +
				"<id-arreglo>centro-costo</id-arreglo>" + //nombre de la etiqueta de cada elemento
				//Manejo del DIV
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divCentroCosto</id-div>" + //id del div a modificar
				"<contenido></contenido>" + //tabla
				//Manejo del Hidden de NumcentrosCosto
				"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
				"<id-hidden>numCentrosCosto</id-hidden>" +
				"<contenido-hidden>0</contenido-hidden>"+
			"</infoid>";
		
		for(HashMap elemento:reporteForm.getArregloCentrosCosto())
		{
			resultado += "<centro-costo>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
			resultado += "</centro-costo>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarCentrosCosto: "+e);
		}
		return null;
	}

	/**
	 * Método que carga los centros de costo de las vias de ingreso hospitaliacion y urgencias
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 */
	private void cargarCentrosCostoXViaIngresoTodas(Connection con,
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario) 
	{
		reporteForm.setArregloCentrosCosto(Utilidades.getCentrosCosto(con, ConstantesBD.codigoViaIngresoHospitalizacion+"", reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), "", 0, false, false, false));
		ArrayList<HashMap<String, Object>> arregloUrgencias = Utilidades.getCentrosCosto(con, ConstantesBD.codigoViaIngresoUrgencias+"", reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), "", 0, false, false, false);
		
		for(HashMap<String, Object> elementoUrg:arregloUrgencias)
		{
			boolean existe = false;
			for(HashMap<String, Object> elemento:reporteForm.getArregloCentrosCosto())
				if(elementoUrg.get("codigo").toString().equals(elemento.get("codigo").toString()))
					existe = true;
			
			//Si el registro no existía entonces se agrega al arreglo
			if(!existe)
				reporteForm.getArregloCentrosCosto().add(elementoUrg);
						
		}
		
	}

	/**
	 * Método para iniciar el flujo de la funcionalidad
	 * @param con
	 * @param mapping
	 * @param reporteForm
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping,
			ReporteMortalidadForm reporteForm, UsuarioBasico usuario) 
	{
		reporteForm.reset();
		reporteForm.setTiposReporte(UtilidadesManejoPaciente.obtenerTiposReporte(con, ConstantesBDManejoPaciente.funcReporteMortalidad, ""));
		reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
		reporteForm.setSexos(Utilidades.obtenerSexos(con));
		reporteForm.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		
		reporteForm.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		reporteForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
