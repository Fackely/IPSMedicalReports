/*
 * Sep 03, 2008
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

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

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
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ReporteEgresosEstanciasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ReporteEgresosEstancias;
import com.princetonsa.mundo.manejoPaciente.ValoresTipoReporte;
import com.princetonsa.pdf.ReporteEgresosEstanciasPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.generadorReporte.manejoPaciente.egresosEstancias.indicadoresHospitalizacion.GeneradorReporteIndicadoresHospitalizacion;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;

/**
 * @author Sebastián Gómez R.
 * Clase usada para controlar los procesos de la funcionalidad
 * Reporte estadísticos de egresos y estancias
 *
 */
public class ReporteEgresosEstanciasAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ReporteEgresosEstanciasAction.class);
	
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
			if(form instanceof ReporteEgresosEstanciasForm)
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
				ReporteEgresosEstanciasForm reporteForm =(ReporteEgresosEstanciasForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				String estado=reporteForm.getEstado(); 
				logger.warn("\n\n En ReporteEgresosEstanciasAction el Estado ["+estado+"] \n\n");


				if(estado == null)
				{
					reporteForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Reportte Egresos Estancias Action (null) ");
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
				else if (estado.equals("filtrarTiposPaciente"))
				{
					return accionFiltrarTiposPaciente(con,reporteForm,response,usuario);
				}
				else if (estado.equals("filtrarCiudades"))
				{
					return accionFiltrarCiudades(con, reporteForm, response, usuario);
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
	 * Método implementado para realizar la impresión de un tipo de reporte específico
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con,
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) 
	{
		ActionForward actionForward = mapping.findForward("principal");
		reporteForm.setUrlArchivoPlano("");
		reporteForm.setPathArchivoPlano("");
		
		switch(reporteForm.getTipoReporte())
		{
			case ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos:
				actionForward =  generarReporteResumenMensualEgresos(con,reporteForm,usuario,request,mapping);
				
			break;
			case ConstantesBDManejoPaciente.tipoReporteEgresosConvenio:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReporteEgresosConvenio(reporteForm,usuario,request);
			break;
			case ConstantesBDManejoPaciente.tipoReporteEgresosLugarResidencia:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReporteEgresosLugarResidencia(reporteForm,usuario,request);
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteDiagnosticosEgresosRangoEdad:
				actionForward =  generarReporteDxEgresosRangoEdad(con,reporteForm,usuario,request,mapping);
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReporteTotalDxEgreso(reporteForm,usuario,request);
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteNPrimerasCausasMorbilidad:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReporteCausasMorbilidad(reporteForm,usuario,request);
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteEstanciaPromMensualPacEgresadosRan:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReporteEstanciaPromMensualPacEgresadosRan(reporteForm,usuario,request);
			break;
			
			case ConstantesBDManejoPaciente.tipoReportePacEgresadosPediatriaRangoEdad:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReportePacEgresadosPediatriaRanEdad(reporteForm,usuario,request);
			break;
			
			/**
			 * SE PONE SWITCH PARA QUE GENERE EL REPORTE SEGÚN EL TIPO
			**/
			
			case ConstantesBDManejoPaciente.tipoReporteEstanciaPacienteMayorNDias:
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
					generarReporteEstanciaPacienteMayorNDias(reporteForm,usuario,request);
			break;
			
			/**
			 * MT 2211
			 */
			case ConstantesBDManejoPaciente.tipoReporteIndicadoresHospitalizacion:
					generarReporteIndicadoresHospitalizacion(con,reporteForm,usuario,request);
			break;
			
			
			
		}
		
		UtilidadesManejoPaciente.insertarLogReportes(con, reporteForm.getTipoReporte(), usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
		
		UtilidadBD.closeConnection(con);
		return actionForward;
	}

	/**
	 * Método para generar el reporte de estancia pacientes mayor de N días
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteEstanciaPacienteMayorNDias(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "EstadisticasEstanciaPacMayorNDias.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Tipo egreso: "+reporteForm.getDescripcionTipoEgreso()+"] ";
        cadena += "[prioridad: "+reporteForm.getPrioridad()+"] ";
        cadena += "[Estado a la salida: "+reporteForm.getDescripcionEstadoSalida()+"] ";
        String listadoConvenios = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getConvenios("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getConvenios("checkbox_"+i).toString()))
        		listadoConvenios += (listadoConvenios.equals("")?"":", ") + reporteForm.getConvenios("nombre_"+i).toString().toLowerCase();
        cadena += "[Convenios: "+(listadoConvenios.equals("")?"Todos":listadoConvenios)+"]";
        
        
        v.add(cadena);
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Se inserta título del reporte
        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1,"grillaTitulo");
        v = new Vector();
        v.add("ESTANCIA DE PACIENTES MAYOR A N DÍAS");
        comp.insertLabelInGridOfMasterPageWithProperties(2,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReporteEstanciaPacientesMayorNDias(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), reporteForm.getCodigoSexo(), reporteForm.getTipoEgreso(), Utilidades.convertirAEntero(reporteForm.getPrioridad()), reporteForm.getEstadoSalida(), reporteForm.getConvenios());
        
        //******SE MODIFICA DATA SET DE ESTANCIA DE PACIENTES MAYOR A N DÍAS***************
        comp.obtenerComponentesDataSet("dataSetEstadisticasEstanciaPacMayorNDias");     
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
		
	}

	/**
	 * Método implementado para generar el reporte de los pacientes egresados de padiatría por rango de edad
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReportePacEgresadosPediatriaRanEdad(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "EstadisticasPacEgresadosPediatriaRan.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Estado facturación: "+reporteForm.getDescripcionEstadoFacturacion()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        String listadoDxEgreso = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getDiagnosticosEgreso("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosEgreso("checkbox_"+i).toString()))
        	{
        		String[] vector = reporteForm.getDiagnosticosEgreso(i+"").toString().split(ConstantesBD.separadorSplit);
        		listadoDxEgreso += (listadoDxEgreso.equals("")?"":", ") + vector[0];
        	}
        cadena += "[Dx Egreso: "+(listadoDxEgreso.equals("")?"Todos":listadoDxEgreso)+"] ";
        
        v.add(cadena);
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Se inserta título del reporte
        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1,"grillaTitulo");
        v = new Vector();
        v.add("PACIENTES EGRESADOS DE PEDIATRÍA POR RANGO DE EDAD");
        comp.insertLabelInGridOfMasterPageWithProperties(2,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), reporteForm.getCodigoSexo(), reporteForm.getEstadoFacturacion(), reporteForm.getDiagnosticosEgreso(), usuario.getCodigoInstitucionInt());
        
        //******SE MODIFICA DATA SET DE PACIENTES EGRESADOS PEDIATRÍA POR RANGO DE EDAD***************
        comp.obtenerComponentesDataSet("dataSetPacEgresadosPediatriaRanEdad");     
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
		
	}

	/**
	 * Método implementado para generar el reporte de estancia promedio mensual de paciedntes
	 * egresados por rango
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteEstanciaPromMensualPacEgresadosRan(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "EstadisticasEstanciaPromMensual.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Estado facturación: "+reporteForm.getDescripcionEstadoFacturacion()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        String listadoDxEgreso = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getDiagnosticosEgreso("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosEgreso("checkbox_"+i).toString()))
        	{
        		String[] vector = reporteForm.getDiagnosticosEgreso(i+"").toString().split(ConstantesBD.separadorSplit);
        		listadoDxEgreso += (listadoDxEgreso.equals("")?"":", ") + vector[0];
        	}
        cadena += "[Dx Egreso: "+(listadoDxEgreso.equals("")?"Todos":listadoDxEgreso)+"] ";
        
        v.add(cadena);
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Se inserta título del reporte
        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1,"grillaTitulo");
        v = new Vector();
        v.add("ESTANCIA PROMEDIO MENSUAL DE PACIENTES EGRESADOS POR RANGO");
        comp.insertLabelInGridOfMasterPageWithProperties(2,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), reporteForm.getCodigoSexo(), reporteForm.getEstadoFacturacion(), reporteForm.getDiagnosticosEgreso(), usuario.getCodigoInstitucionInt());
        
        //******SE MODIFICA DATA SET DE ESTANCIA PROMEDIO MENSUAL DE PACIENTES EGRESADOS POR RANGO***************
        comp.obtenerComponentesDataSet("dataSetEstadisticaEstanciaPromedio");     
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
		
	}

	/**
	 * Método para generar el reporte de causas de morbilidad 
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteCausasMorbilidad(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "EstadisticasEgresosCausasMorbilidad.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Prioridad: "+reporteForm.getPrioridad()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Estado salida: "+reporteForm.getDescripcionEstadoSalida()+"] ";
        
        v.add(cadena);
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Se inserta título del reporte
        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1,"grillaTitulo");
        v = new Vector();
        v.add("PRIMERAS N CAUSAS DE MORBILIDAD");
        comp.insertLabelInGridOfMasterPageWithProperties(2,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReporteNPrimerasCausasMorbilidad(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), Utilidades.convertirAEntero(reporteForm.getPrioridad()), reporteForm.getCodigoSexo(), reporteForm.getEstadoSalida());
        
        //******SE MODIFICA DATA SET DE PRIMERAS N CAUSAS DE MORBILIDAD***************
        comp.obtenerComponentesDataSet("dataSetCausasMorbilidad");     
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
		
	}

	/**
	 * Método para generar el reporte de total diagnósticos x egreso
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteTotalDxEgreso(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "TotalDxEgresos.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Estado facturación: "+reporteForm.getDescripcionEstadoFacturacion()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Centro Costo: "+reporteForm.getNombreCentroCosto()+"] ";
        
        
        
        String listadoDxEgreso = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getDiagnosticosEgreso("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosEgreso("checkbox_"+i).toString()))
        	{
        		String[] vector = reporteForm.getDiagnosticosEgreso(i+"").toString().split(ConstantesBD.separadorSplit);
        		listadoDxEgreso += (listadoDxEgreso.equals("")?"":", ") + vector[0];
        	}
        cadena += "[Dx Egreso: "+(listadoDxEgreso.equals("")?"Todos":listadoDxEgreso)+"] ";
        v.add(cadena);
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Se inserta título del reporte
        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1,"grillaTitulo");
        v = new Vector();
        v.add("TOTAL DIAGNÓSTICOS DE EGRESO");
        comp.insertLabelInGridOfMasterPageWithProperties(2,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReporteTotalDxEgreso(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), reporteForm.getEstadoFacturacion(), reporteForm.getCodigoSexo(), Utilidades.convertirAEntero(reporteForm.getCentrosCosto("codigo")+""), reporteForm.getDiagnosticosEgreso());
        
        //******SE MODIFICA DATA SET DE TOTAL DIAGNOSTICOS DE EGRESO***************
        comp.obtenerComponentesDataSet("dataSetTotalDxEgresos");     
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
		
	}

	/**
	 * Método implementado para generar la impresión de reporte de diagnósticos de egreso por rango de edad
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward generarReporteDxEgresosRangoEdad(Connection con,
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) 
	{
		///se edita nombre del archivo PDF
		String nombreArchivo;
		String mensaje = "";
    	Random r=new Random();
    	if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
    	{
    		nombreArchivo="DxEgresosRangoEdad" + r.nextInt()  +".pdf";
    		ReporteEgresosEstanciasPdf.pdfDxEgresosRangoEdad(con, ValoresPorDefecto.getFilePath(),nombreArchivo, reporteForm, usuario, request);
    		request.setAttribute("nombreArchivo", System.getProperty("file.separator")+nombreArchivo);
        	request.setAttribute("nombreVentana", "Diagnosticos Egresos Rango Edad");
    	}
    	else
    	{
    		nombreArchivo="DxEgresosRangoEdad" + r.nextInt()  +".zip";
    		mensaje = ReporteEgresosEstanciasPdf.csvDxEgresosRangoEdad(con, ValoresPorDefecto.getFilePath(),nombreArchivo, reporteForm, usuario, request);
    		reporteForm.setUrlArchivoPlano(System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivo);
    		reporteForm.setPathArchivoPlano(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+nombreArchivo);
    	}
    	
    	//Se verifica que no hayan habido mensajes de error
    	if(!mensaje.equals(""))
    	{
    		ActionErrors errores = new ActionErrors();
    		errores.add("", new ActionMessage("errors.notEspecific",mensaje));
    		saveErrors(request, errores);
    		return mapping.findForward("paginaErroresActionErrors");
    	}
    	else
    	{
    		if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
    			return mapping.findForward("abrirPdf");
    		else
    			return mapping.findForward("principal");
    	}
	}

	/**
	 * Método implementado para generar un reporte de los egresos por lugar de residencia
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteEgresosLugarResidencia(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		
		String nombreRptDesign = "EstadisticasEgresosLugarResidencia.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        reporteForm.setNombrePais("");
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        v=new Vector();
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Prioridad: "+reporteForm.getPrioridad()+"] ";
        cadena += "[País: "+reporteForm.getNombrePais()+"] ";
        String listadoCiudades = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getCiudades("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getCiudades("checkbox_"+i).toString()))
        		listadoCiudades += (listadoCiudades.equals("")?"":", ") + reporteForm.getCiudades("nombre_"+i).toString().toLowerCase();
        cadena += "[Ciudades: "+(listadoCiudades.equals("")?"Todas":listadoCiudades)+"] ";
        String listadoDxEgreso = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getDiagnosticosEgreso("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosEgreso("checkbox_"+i).toString()))
        	{
        		String[] vector = reporteForm.getDiagnosticosEgreso(i+"").toString().split(ConstantesBD.separadorSplit);
        		listadoDxEgreso += (listadoDxEgreso.equals("")?"":", ") + vector[0];
        	}
        cadena += "[Dx Egreso: "+(listadoDxEgreso.equals("")?"Todos":listadoDxEgreso)+"] ";
        v.add(cadena);
        
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReporteEgresosLugarResidencia(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), reporteForm.getCodigoSexo(), reporteForm.getPrioridad(), reporteForm.getCodigoPais(), reporteForm.getCiudades(), reporteForm.getDiagnosticosEgreso());
        
        //******SE MODIFICA DATA SET DE EGRESOS X LUGAR RESIDENCIA***************
        comp.obtenerComponentesDataSet("dataSetEgresosLugarResidencia");     
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
	}

	/**
	 * Método para generar la impresion del tipo de reporte egresos x convenio
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteEgresosConvenio(
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		String nombreRptDesign = "EstadisticasEgresosConvenio.rptdesign";
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
        reporteForm.setNombreTipoPaciente("");
        reporteForm.setNombreSexo("");
        
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"grillaBusqueda");
        comp.fontSizeCellGridHeaderOfMasterPage(1,0, 0, 0, 7);
        String cadena = "";
        cadena += "[Centro de Atención: "+reporteForm.getNombreCentroAtencion().toLowerCase()+"] ";
        cadena += "[Fecha inicial - final: "+reporteForm.getFechaInicial()+" - "+reporteForm.getFechaFinal()+"] ";
        cadena += "[Vía de ingreso: "+reporteForm.getNombreViaIngreso()+"] ";
        cadena += "[Tipo paciente: "+reporteForm.getNombreTipoPaciente()+"] ";
        cadena += "[Sexo: "+reporteForm.getNombreSexo()+"] ";
        cadena += "[Prioridad: "+reporteForm.getPrioridad()+"] ";
        String listadoConvenios = "";
        for(int i=0;i<Integer.parseInt(reporteForm.getConvenios("numRegistros").toString());i++)
        	if(UtilidadTexto.getBoolean(reporteForm.getConvenios("checkbox_"+i).toString()))
        		listadoConvenios += (listadoConvenios.equals("")?"":", ") + reporteForm.getConvenios("nombre_"+i).toString().toLowerCase();
        cadena += "[Convenios: "+(listadoConvenios.equals("")?"Todos":listadoConvenios)+"]";
        
        v=new Vector();
        v.add(cadena);
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //************************************************************************************************
        
        String consulta = ReporteEgresosEstancias.obtenerConsultaReporteEgresosConvenio(reporteForm.getCodigoCentroAtencion(), reporteForm.getFechaInicial(), reporteForm.getFechaFinal(), reporteForm.getCodigoViaIngreso(), reporteForm.getCodigoTipoPaciente(), Utilidades.convertirAEntero(reporteForm.getPrioridad()), reporteForm.getEstadoFacturacion(), reporteForm.getCodigoSexo(), reporteForm.getConvenios());
        
        //******SE MODIFICA DATA SET DE EGRESOS X CONVENIO***************
        comp.obtenerComponentesDataSet("dataSetEgresosConvenio");     
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
		
	}

	/**
	 * Método para generar el reporte del resumen mensual de egresos
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 * @param mapping 
	 */
	private ActionForward generarReporteResumenMensualEgresos(Connection con,
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) 
	{
		///se edita nombre del archivo PDF
		String nombreArchivo;
		String mensaje = "";
    	Random r=new Random();
    	if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
    	{
    		nombreArchivo="ResumenMensualEgresos" + r.nextInt()  +".pdf";
    		ReporteEgresosEstanciasPdf.pdfResumenMensualEgresos(con, ValoresPorDefecto.getFilePath(),nombreArchivo, reporteForm, usuario, request);
    		request.setAttribute("nombreArchivo", System.getProperty("file.separator")+nombreArchivo);
        	request.setAttribute("nombreVentana", "Resumen Mensual Egresos");
    	}
    	else
    	{
    		nombreArchivo="ResumenMensualEgresos" + r.nextInt()  +".zip";
    		mensaje = ReporteEgresosEstanciasPdf.csvResumenMensualEgresos(con, ValoresPorDefecto.getFilePath(),nombreArchivo, reporteForm, usuario, request);
    		reporteForm.setUrlArchivoPlano(System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivo);
    		reporteForm.setPathArchivoPlano(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+nombreArchivo);
    	}
    	
    	
    	
    	
    	
    	//Se verifica que no hayan habido mensajes de error
    	if(!mensaje.equals(""))
    	{
    		ActionErrors errores = new ActionErrors();
    		errores.add("", new ActionMessage("errors.notEspecific",mensaje));
    		saveErrors(request, errores);
    		return mapping.findForward("paginaErroresActionErrors");
    	}
    	else
    	{
    		if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
    			return mapping.findForward("abrirPdf");
    		else
    			return mapping.findForward("principal");
    	}
    	
		
	}

	/**
	 * Método para filtrar por método AJAX las ciudades de un país
	 * @param con
	 * @param reporteForm
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrarCiudades(Connection con,
			ReporteEgresosEstanciasForm reporteForm,
			HttpServletResponse response, UsuarioBasico usuario) 
	{
		reporteForm.setArregloCiudades(Utilidades.obtenerCiudadesXPais(con, reporteForm.getCodigoPais()+""));
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>ciudadesTemp</id-select>" +
				"<id-arreglo>ciudad</id-arreglo>" + //nombre de la etiqueta de cada elemento
				//Manejo del DIV
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divCiudades</id-div>" + //id del div a modificar
				"<contenido></contenido>" + //tabla
				//Manejo del Hidden de NumcentrosCosto
				"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
				"<id-hidden>numCiudades</id-hidden>" +
				"<contenido-hidden>0</contenido-hidden>"+
			"</infoid>";
		
		//*********SE LIMPIA EL MAPA DE CIUDADES***********************
		reporteForm.setCiudades(new HashMap<String, Object>());
		reporteForm.setNumCiudades(0);
		//**********************************************************************
		
		for(Object objeto:reporteForm.getArregloCiudades())
		{
			HashMap elemento = (HashMap)objeto;
			
			resultado += "<ciudad>";
				resultado += "<codigo>"+elemento.get("codigoPais")+ConstantesBD.separadorSplit+elemento.get("codigoDepartamento")+ConstantesBD.separadorSplit+elemento.get("codigoCiudad")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombreCiudad")+" ("+elemento.get("nombreDepartamento")+")"+"</descripcion>";
			resultado += "</ciudad>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarCiudades: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para filtrar los tipos de paciente por el cambio de la vía de ingreso
	 * @param con
	 * @param reporteForm
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrarTiposPaciente(Connection con,
			ReporteEgresosEstanciasForm reporteForm,
			HttpServletResponse response, UsuarioBasico usuario) 
	{
		String codigoViaIngreso = reporteForm.getCodigoViaIngreso() + "";
		boolean filtrarCentrosCosto = UtilidadTexto.getBoolean(reporteForm.getIndex());
		boolean consultarViaIngreso = false;
		
		//Si no se eligió una vía de ingreso específica se verifica según el tipo de reporte que vías de ingreso aplican
		if(codigoViaIngreso.equals(ConstantesBD.codigoNuncaValido+""))
		{
			switch(reporteForm.getTipoReporte())
			{
				case ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos:
					codigoViaIngreso = ConstantesBD.codigoViaIngresoHospitalizacion + "," + ConstantesBD.codigoViaIngresoUrgencias + "," + ConstantesBD.codigoViaIngresoConsultaExterna;
				break;
				case ConstantesBDManejoPaciente.tipoReporteEgresosConvenio:
				case ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso:
					codigoViaIngreso = "";
				break;
			}
		}
		
		//**********SE CONSULTA EL ARREGLO DE TIPOS DE PACIENTE**************************************************
		/**
		 * Sólo se consultaran tipos de paciente cuando se ha seleccionado vía de ingreso,
		 * y si no se ha seleccionado solo aplica para reportes que manejen todas las vías de ingresos
		 */
		if(
			!codigoViaIngreso.equals("")
			||
			(
					codigoViaIngreso.equals("")&&
					reporteForm.getTipoReporte()!=ConstantesBDManejoPaciente.tipoReporteDiagnosticosEgresosRangoEdad
			)
		)
		{
			reporteForm.setArregloTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, codigoViaIngreso));
			//Se verifica si hay un solo tipo de paciente ara asignarlo automáticamente--------------------------------
			if(reporteForm.getArregloTiposPaciente().size()==1)
			{
				for(Object objeto:reporteForm.getArregloTiposPaciente())
				{
					HashMap elemento = (HashMap)objeto;
					reporteForm.setCodigoTipoPaciente(elemento.get("tipopaciente").toString());
				}
					
			}
			else
				reporteForm.setCodigoTipoPaciente("");
			//--------------------------------------------------------------------------------------
		}
		else
			reporteForm.setArregloTiposPaciente(new ArrayList());
		//*******************************************************************************************************
		//***************SE CONSULTA EL ARREGLO DE LOS CENTROS DE COSTO***********************************
		if(filtrarCentrosCosto)
		{
			
			//Nota * Algunos reportes requieres que los centros de costo sean especificados por via de ingreso y tipo de paciente
			if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos)
				consultarViaIngreso = true;
			
			reporteForm.setArregloCentrosCosto(Utilidades.getCentrosCosto(con, codigoViaIngreso+"", reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), reporteForm.getCodigoTipoPaciente(), 0, false, false,consultarViaIngreso));
		}
		else
			reporteForm.setArregloCentrosCosto(new ArrayList<HashMap<String,Object>>());
		//**************************************************************************************
		
		//*********SE LIMPIA EL MAPA DE CENTROS DE COSTO***********************
		reporteForm.setCentrosCosto(new HashMap<String, Object>());
		reporteForm.setNumCentrosCosto(0);
		//**********************************************************************
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" ;
		
		if(filtrarCentrosCosto)
		{
			//Si el reporte es TOTAL DIAGNÓSTICOS DE EGRESO los datos para el filtro de select de centros
			//de costo es diferente porque la selección del centro de costo no es múltiple, por tal motivo,
			//no se necesita DIV , ni Hidden.
			if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso)
				resultado+= "" + 
					"<id-select>codigoCentroCosto</id-select>" +
					"<id-arreglo>centro-costo</id-arreglo>" + //nombre de la etiqueta de cada elemento
					"<valor-opcion-default>Seleccione</valor-opcion-default>";
			else
				resultado+= "" + 
					"<id-select>centroCostoTemp</id-select>" +
					"<id-arreglo>centro-costo</id-arreglo>" + //nombre de la etiqueta de cada elemento
					"<valor-opcion-default>Seleccione</valor-opcion-default>"+
					//Manejo del DIV
					"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
					"<id-div>divCentroCosto</id-div>" + //id del div a modificar
					"<contenido></contenido>" + //tabla
					//Manejo del Hidden de NumcentrosCosto
					"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
					"<id-hidden>numCentrosCosto</id-hidden>" +
					"<contenido-hidden>0</contenido-hidden>";
		}
		
		resultado += "" +
				"<id-select>codigoTipoPaciente</id-select>" +
				"<id-arreglo>tipo-paciente</id-arreglo>" + //nombre de la etiqueta de cada elemento
				"<valor-opcion-default>Todos</valor-opcion-default>"+
			"</infoid>";
		
		
		for(HashMap elemento:reporteForm.getArregloCentrosCosto())
		{
			resultado += "<centro-costo>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+(consultarViaIngreso?" ("+elemento.get("nombreViaIngreso")+" "+elemento.get("nombreTipoPaciente")+")":"")+"</descripcion>";
			resultado += "</centro-costo>";
		}
		
		for(Object objeto:reporteForm.getArregloTiposPaciente())
		{
			HashMap elemento = (HashMap)objeto;
			
			resultado += "<tipo-paciente>";
				resultado += "<codigo>"+elemento.get("tipopaciente")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nomtipopaciente")+"</descripcion>";
			resultado += "</tipo-paciente>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarTiposPaciente: "+e);
		}
		return null;
		
	}

	/**
	 * Método implementado para filtrar los centros de costo por evento de cambio de centro de atencion
	 * o tipo de paciente
	 * @param con
	 * @param reporteForm
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrarCentrosCosto(Connection con,
			ReporteEgresosEstanciasForm reporteForm,
			HttpServletResponse response, UsuarioBasico usuario) 
	{
		String codigoViaIngreso = reporteForm.getCodigoViaIngreso() + "";
		
		//Si no se eligió una vía de ingreso específica se verifica según el tipo de reporte que vías de ingreso aplican
		if(codigoViaIngreso.equals(ConstantesBD.codigoNuncaValido+""))
		{
			switch(reporteForm.getTipoReporte())
			{
				case ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos:
					codigoViaIngreso = ConstantesBD.codigoViaIngresoHospitalizacion + "," + ConstantesBD.codigoViaIngresoUrgencias + "," + ConstantesBD.codigoViaIngresoConsultaExterna;
				break;
				
				case ConstantesBDManejoPaciente.tipoReporteEgresosConvenio:
				case ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso:
					codigoViaIngreso = "";
				break;
			}
		}
		
		//***************SE CONSULTA EL ARREGLO DE LOS CENTROS DE COSTO***********************************
		boolean consultarViaIngreso = false;
		//Nota * Algunos reportes requieres que los centros de costo sean especificados por via de ingreso y tipo de paciente
		if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos)
			consultarViaIngreso = true;
		reporteForm.setArregloCentrosCosto(Utilidades.getCentrosCosto(con, codigoViaIngreso+"", reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), reporteForm.getCodigoTipoPaciente(), 0, false, false,consultarViaIngreso));
		//**************************************************************************************
		
		//*********SE LIMPIA EL MAPA DE CENTROS DE COSTO***********************
		reporteForm.setCentrosCosto(new HashMap<String, Object>());
		reporteForm.setNumCentrosCosto(0);
		//**********************************************************************
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" ;
		
		///Si el reporte es TOTAL DIAGNÓSTICOS DE EGRESO los datos para el filtro de select de centros
		//de costo es diferente porque la selección del centro de costo no es múltiple, por tal motivo,
		//no se necesita DIV , ni Hidden.
		if(reporteForm.getTipoReporte()==ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso)
			resultado+= "" + 
				"<id-select>codigoCentroCosto</id-select>" +
				"<id-arreglo>centro-costo</id-arreglo>" ; //nombre de la etiqueta de cada elemento
		else
			resultado+="" +
				"<id-select>centroCostoTemp</id-select>" +
				"<id-arreglo>centro-costo</id-arreglo>" + //nombre de la etiqueta de cada elemento
				//Manejo del DIV
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divCentroCosto</id-div>" + //id del div a modificar
				"<contenido></contenido>" + //tabla
				//Manejo del Hidden de NumcentrosCosto
				"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
				"<id-hidden>numCentrosCosto</id-hidden>" +
				"<contenido-hidden>0</contenido-hidden>";
		resultado += "</infoid>";
		
		for(HashMap elemento:reporteForm.getArregloCentrosCosto())
		{
			resultado += "<centro-costo>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+(consultarViaIngreso?" ("+elemento.get("nombreViaIngreso")+" "+elemento.get("nombreTipoPaciente")+")":"")+"</descripcion>";
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
	 * Método que alista los parámetros de búsqueda al seleccionar un tipo de reporte específico
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionSeleccionTipoReporte(Connection con,ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request) 
	{
		reporteForm.resetParametros(); //se limpian los parámetros
		reporteForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
		
		//********VALIDACIÓN DEL TIPO DE REPORTE**************************
		
		ActionErrors errores = new ActionErrors();
		switch(reporteForm.getTipoReporte())
		{
			case ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Resumen mensual de egresos"));
				reporteForm.setArregloCentrosCosto(Utilidades.getCentrosCosto(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoConsultaExterna, reporteForm.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), "", 0, false, false,true));
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoConsultaExterna));
				reporteForm.setArregloTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoConsultaExterna));
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteEgresosConvenio:
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ""));
				reporteForm.setArregloTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, ""));
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteEgresosLugarResidencia:
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ""));
				reporteForm.setArregloTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, ""));
				
				//Si solo hay un pais de una vez se consultan las ciudades
				if(reporteForm.getArregloPaises().size()==1)
				{
					for(HashMap<String,Object> elemento:reporteForm.getArregloPaises())
						reporteForm.setPais(new InfoDatosString(elemento.get("codigo").toString(),elemento.get("descripcion").toString()));
					
					reporteForm.setArregloCiudades(Utilidades.obtenerCiudadesXPais(con, reporteForm.getCodigoPais()+""));	
				}
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteDiagnosticosEgresosRangoEdad:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para Diagnóstico de egreso por rango de edad"));
				reporteForm.setArregloCentrosCosto(new ArrayList<HashMap<String,Object>>());
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
				reporteForm.setArregloTiposPaciente(new ArrayList());
				reporteForm.setPrioridad("10");
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso:
				reporteForm.setArregloCentrosCosto(new ArrayList<HashMap<String,Object>>());
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ""));
				reporteForm.setArregloTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, ""));
			break;
			
			
			case ConstantesBDManejoPaciente.tipoReporteNPrimerasCausasMorbilidad:
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ""));
				reporteForm.setArregloTiposPaciente(new ArrayList());
				reporteForm.setPrioridad("10");
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteEstanciaPromMensualPacEgresadosRan:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para la estancia promedio mensual de pacientes egresados por rango"));
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
				reporteForm.setArregloTiposPaciente(new ArrayList());
			break;
			
			case ConstantesBDManejoPaciente.tipoReportePacEgresadosPediatriaRangoEdad:
				if(!ValoresTipoReporte.existeParametrizacionValoresTipoReporte(con, reporteForm.getTipoReporte(), usuario.getCodigoInstitucionInt()))
					errores.add("",new ActionMessage("errors.faltaParametrizacion","Rangos de Valores por Tipo Reporte para pacientes egresados de pediatría por rango de edad"));
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
				reporteForm.setArregloTiposPaciente(new ArrayList());
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteIndicadoresHospitalizacion:
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+""));
				
				ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> tiposPac=Utilidades.obtenerTiposPacientePorViaIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+"");
				for(HashMap<String, Object> tipos: tiposPac)
				{
					if(tipos.get("tipopaciente").equals(ConstantesBD.tipoPacienteHospitalizado+"")){
						resultado.add(tipos);
					}
				}
				reporteForm.setArregloTiposPaciente(resultado);
			break;
			
			case ConstantesBDManejoPaciente.tipoReporteEstanciaPacienteMayorNDias:
				reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
				reporteForm.setArregloTiposPaciente(new ArrayList());
				reporteForm.setPrioridad("10");
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
	 * Método implementado para iniciar el flujo de lso reportes estadísticos de egresos y estancias
	 * @param con
	 * @param mapping
	 * @param reporteForm
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping,
			ReporteEgresosEstanciasForm reporteForm, UsuarioBasico usuario) 
	{
		reporteForm.reset();
		reporteForm.setTiposReporte(UtilidadesManejoPaciente.obtenerTiposReporte(con, ConstantesBDManejoPaciente.funcReporteEgresosEstancias, ""));
		//reporteForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
		reporteForm.setSexos(Utilidades.obtenerSexos(con));
		reporteForm.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		
		reporteForm.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		reporteForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
		reporteForm.setArregloPaises(Utilidades.obtenerPaises(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	

	/**
	 * Método que hace el llamado a los métodos para la generación del reporte
	 * indicadores de hospitalización
	 * @param con
	 * @param reporteForm
	 * @param usuario
	 * @param request
	 */
	private void generarReporteIndicadoresHospitalizacion(Connection con,ReporteEgresosEstanciasForm reporteForm,
			UsuarioBasico usuario, HttpServletRequest request)
	{
		
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		DtoFiltroReporteIndicadoresHospitalizacion dtoFiltroReporte= new DtoFiltroReporteIndicadoresHospitalizacion();
		
		//--------DATOS INSTITUCION--------------------
		dtoFiltroReporte.setRazonSocial(ins.getRazonSocial());
		dtoFiltroReporte.setNit(ins.getNit());
		dtoFiltroReporte.setTelefono(ins.getTelefono());
		dtoFiltroReporte.setDireccion(ins.getDireccion());
		dtoFiltroReporte.setRutaLogo(ins.getLogoJsp());
		dtoFiltroReporte.setActividadEconomica(ins.getActividadEconomica());
		
		//-------DATOS USUARIO-------------------------
		dtoFiltroReporte.setNombreUsuario("");
		UsuariosDelegate usu= new UsuariosDelegate();
		Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
		dtoFiltroReporte.setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
				+" "+usuarioCompleto.getPersonas().getPrimerApellido()
				+" ("+usuarioCompleto.getLogin()+")"
				);
			
		//-------DATOS ENCABEZADOS FILTRO------------
		dtoFiltroReporte.setConsecutivoCentroAtencion(reporteForm.getCodigoCentroAtencion());
		dtoFiltroReporte.setFechaInicial(reporteForm.getFechaInicial());
		dtoFiltroReporte.setFechaFinal(reporteForm.getFechaFinal());
		dtoFiltroReporte.setCodigoSexo(reporteForm.getCodigoSexo());
		if(!UtilidadTexto.isEmpty(reporteForm.getConvenios("codigo")+""))
			dtoFiltroReporte.setCodigoConvenio(Utilidades.convertirAEntero(reporteForm.getConvenios("codigo")+""));
		else
			dtoFiltroReporte.setCodigoConvenio(ConstantesBD.codigoNuncaValido);
		dtoFiltroReporte.setDescripcionCentroAtencion(reporteForm.getNombreCentroAtencion());
		dtoFiltroReporte.setDescripcionViaIngreso("Hospitalización");
		dtoFiltroReporte.setDescripcionTipoPaciente("Hospitalizado");
		
		int tamanoDiagnosticos=Utilidades.convertirAEntero(reporteForm.getDiagnosticosEgreso("numRegistros")+"");
		dtoFiltroReporte.setDescripcionDiagnosticosEgreso("");
		if(tamanoDiagnosticos>0)
		{
			dtoFiltroReporte.setDescripcionDiagnosticosEgreso(new String());
			String diagnosticos="";
			for(int i=0;i<tamanoDiagnosticos;i++)
			{
				if(UtilidadTexto.getBoolean(reporteForm.getDiagnosticosEgreso("checkbox_"+i)+""))
				{
					String[] diagnostico=(reporteForm.getDiagnosticosEgreso(i+"")+"").split(ConstantesBD.separadorTags);
					diagnosticos+="'"+diagnostico[0]+"'";
					if(i<tamanoDiagnosticos-1)
						diagnosticos+=", ";
				}
			}
			dtoFiltroReporte.setDescripcionDiagnosticosEgreso(diagnosticos);
		}
		
		if(UtilidadTexto.isEmpty(dtoFiltroReporte.getDescripcionDiagnosticosEgreso()))
			dtoFiltroReporte.setDescripcionDiagnosticosEgreso("Todos");
		
		for(int i=0;i<reporteForm.getSexos().size();i++)
		{
			if(reporteForm.getSexos().get(i).get("codigo").equals(reporteForm.getSexo().getCodigo()+""))
			{
				dtoFiltroReporte.setDescripcionSexo(reporteForm.getSexos().get(i).get("nombre")+"");
				break;
			}
		}
		
		if(UtilidadTexto.isEmpty(dtoFiltroReporte.getDescripcionSexo()))
			dtoFiltroReporte.setDescripcionSexo("Todos");
		dtoFiltroReporte.setDescripcionConvenio((UtilidadTexto.isEmpty(reporteForm.getNombreConvenio())?"Todos":reporteForm.getNombreConvenio()));
		
		//-------CUERPO DEL REPORTE------------------
		ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> listaDtoResultados=
			ReporteEgresosEstancias.consultarIndicadoresHospitalizacion(con, dtoFiltroReporte);
				
		if(!listaDtoResultados.isEmpty())
		{
			if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			{
				GeneradorReporteIndicadoresHospitalizacion generadorReporte = new GeneradorReporteIndicadoresHospitalizacion();
				JasperReportBuilder reporte=generadorReporte.generarReporte(dtoFiltroReporte,listaDtoResultados);
				reporteForm.setNombreReporte(generadorReporte.exportarReportePDF(reporte, "IndicadoresHospitalizacion"));
			}else
				if(reporteForm.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
				{
					GeneradorReporteIndicadoresHospitalizacion generadorReporte = new GeneradorReporteIndicadoresHospitalizacion();
					JasperReportBuilder reporte=generadorReporte.generarReportePlano(dtoFiltroReporte,listaDtoResultados);
					reporteForm.setNombreReporte(generadorReporte.exportarReporteArchivoPlano(reporte, "IndicadoresHospitalizacionPlano"));
				}
			
		}else
		{
			reporteForm.setNombreReporte("");
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.noResultados","No Resultados"));
			saveErrors(request, errores);
		}
			
		
	}
	
}
