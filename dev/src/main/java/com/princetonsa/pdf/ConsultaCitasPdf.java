/*
 * @(#)ConsultaCitasPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.agenda.CitaForm;
import com.princetonsa.actionform.agenda.CitasForm;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.util.birt.reports.ExtractCSV;

/**
 * Clase para manejar la generación de PDF's para
 * la consulta de Citas
 * 
 *  @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>,
 *	@version 1.0, 20 /Jun/ 2005
 */
public class ConsultaCitasPdf
{

	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger citasPdfLogger=Logger.getLogger(ConsultaCitasPdf.class);
    
  
	/**
	 * Método que consulta el listado de citas 
	 * @param citasForm
	 * @return
	 */ 
	public static String[] listadoCitasPaciente(CitasForm citasForm)
	{
		
		CitaForm cita=new CitaForm();
		String[] dataListadoCitas;
		int i=0;
		ArrayList resultados=new ArrayList();
		if (citasForm.getListaCitas().size()==0)
		{
		    return new String[0];
		}
		
		for(int cont=0;cont<citasForm.getListaCitas().size();cont++)
		{
			cita=(CitaForm)citasForm.getListaCitas().get(cont);
			resultados.add(i, cita.getNombreUnidadConsulta());
			resultados.add(i+1, cita.getFechaInicio());
		    resultados.add(i+2, cita.getHoraInicio());
		    resultados.add(i+3, cita.getNombreEstadoCita());
		    resultados.add(i+4, cita.getCodigoConsultorioUsua());
		    resultados.add(i+5, UtilidadesConsultaExterna.obtenerListadoCodigosServiciosCita(cita.getCodigoCita(), ConstantesBD.codigoTarifarioCups));
		    resultados.add(i+6, cita.getNombreCompletoMedico().toLowerCase());
		    resultados.add(i+7, cita.getRegistroMedico());
		    resultados.add(i+8, cita.getNombreConvenio().length()>26?cita.getNombreConvenio().substring(0,26):cita.getNombreConvenio());
		    resultados.add(i+9, cita.getLoginUsuCita());		    
		    resultados.add(i+10,cita.getMotivoNoAtencion().toLowerCase()+" "+cita.getObservacion());
		    
			resultados.add(i+11, cita.getNombreCentroAtencion().toUpperCase());
			resultados.add(i+12, cita.getCentroAtencion());
					    
			i=i+13;
		}
		dataListadoCitas=new String[resultados.size()];
		UtilidadTexto.copiarArrayListAArreglo(resultados, dataListadoCitas);
		return dataListadoCitas;
	}
	
	/**
	 * Método que consulta el listado de citas 
	 * @param citasForm
	 * @return
	 */ 
	public static String[] listadoCitasPacienteOdonto(CitasForm citasForm)
	{
		
		String[] dataListadoCitas;
		int i=0;
		ArrayList resultados=new ArrayList();
		if (citasForm.getListCitas().size()==0)
		{
		    return new String[0];
		}
		
		Connection connection = UtilidadBD.abrirConexion();
		for (DtoCitaOdontologica cita: citasForm.getListCitas()){
			
			resultados.add(i, cita.getAgendaOdon().getDescripcionUniAgen());
			resultados.add(i+1, cita.getAgendaOdon().getFecha());
		    resultados.add(i+2, cita.getHoraInicio());
		    resultados.add(i+3, cita.getEstado());
		    resultados.add(i+4, cita.getAgendaOdon().getConsultorio());
		    String servicios = "";
		    for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon()){
		    	servicios += Utilidades.obtenerCodigoPropietarioServicio(connection, elem.getCodigoPk()+"", Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(citasForm.getInstitucion())))+",";
		    }
		    servicios = servicios.substring(0, servicios.length()-1);
		    resultados.add(i+5, servicios);
		    resultados.add(i+6, cita.getAgendaOdon().getNombreMedico().toLowerCase());
		    resultados.add(i+7, cita.getAgendaOdon().getRegistroMedico());
		    String convenio = Utilidades.obtenerNombreConvenio(connection, Utilidades.convertirAEntero(cita.getCuenta()))==null?"":
		    	Utilidades.obtenerNombreConvenio(connection, Utilidades.convertirAEntero(cita.getCuenta()));
		    resultados.add(i+8, convenio.length()>26?convenio.substring(0,26):convenio);
		    resultados.add(i+9, cita.getUsuarioConfirma().getNombreUsuario());		    
		    resultados.add(i+10,cita.getMotivoNoAtencion().toLowerCase());
		    
			resultados.add(i+11, cita.getAgendaOdon().getDescripcionCentAten().toUpperCase());
			resultados.add(i+12, cita.getAgendaOdon().getCentroAtencion());
					    
			i=i+13;
		}
		dataListadoCitas=new String[resultados.size()];
		UtilidadTexto.copiarArrayListAArreglo(resultados, dataListadoCitas);
		return dataListadoCitas;
	}

	
	/**
	 * Méodo que genera el PDF del Listao de Citas por Paciente 
	 * @param filename
	 * @param citasForm
	 * @param medico
	 * @param pacienteActivo
	 */
	public static void pdfCitasPaciente(String filename, CitasForm citasForm, UsuarioBasico medico, PersonaBasica pacienteActivo,HttpServletRequest request)
	{
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
		PdfReports report = new PdfReports("false",true);        
		report.setUsuario(medico.getNombreUsuario()+"("+ medico.getLoginUsuario()+")");
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "CITAS");        
        report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);	    
    	report.openReport(filename);	    

	    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	    report.font.setFontSizeAndAttributes(10, false, false, false);
	    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 8);
	    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 6);

	    iTextBaseTable section;
	    String[] dataListadoCitas = new String[0];
	    if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
	    	dataListadoCitas=listadoCitasPaciente(citasForm);
	    else if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
	    	dataListadoCitas=listadoCitasPacienteOdonto(citasForm);
	    	
	    
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    //Se iteran las citas 
	    float widths[]={(float)1.2,(float)0.5,(float)0.3,(float)1.0 ,(float)0.7,(float)1.4,(float)1.4,(float)1.0,(float)1.3,(float)1.2};
	    int numRows = (dataListadoCitas.length / 10) + 2;	    
	    
	    section = report.createSection("detalle", "detalleTable",numRows,10,0.5f);
		section.setTableBorder("detalleTable", 0x000000, 0.1f);
	    section.setTableCellBorderWidth("detalleTable", 0.1f);
	    section.setTableCellPadding("detalleTable", 1);
	    section.setTableSpaceBetweenCells("detalleTable", 0.1f);
	    section.setTableCellsDefaultColors("detalleTable", 0xFFFFFF, 0x000000);
	    
		    
	    /**
	     * Inc. 506
	     * @author Diana Ruiz
	     * @since 05/08/2011
	     */
	    
		Object cita= new ArrayList<Cita>();
		cita=citasForm.getListaCitas().get(0);	    
	    ((CitaForm) cita).getTelefono();
		    
	    report.document.addParagraph("PACIENTE: "+pacienteActivo.getApellidosNombresPersona(false)+"                         No. Id: "+pacienteActivo.getCodigoTipoIdentificacionPersona()+" "+pacienteActivo.getNumeroIdentificacionPersona()+"                   H.C: "+pacienteActivo.getHistoriaClinica()+"                  Tel: "+((CitaForm) cita).getTelefono(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
	    //report.document.addParagraph("PACIENTE: "+pacienteActivo.getApellidosNombresPersona(false)+"                         No. Id: "+pacienteActivo.getCodigoTipoIdentificacionPersona()+" "+pacienteActivo.getNumeroIdentificacionPersona()+"                   H.C: "+pacienteActivo.getHistoriaClinica()+"                  Tel: "+pacienteActivo.getTelefono(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
		    
	    report.font.setFontAttributes(0x000000, 6, true, false, false);
	    section.setTableCellsColSpan(10);
	    section.addTableTextCellAligned("detalleTable","Listado de Citas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	            
	    String codCA = ConstantesBD.codigoNuncaValido+""; 
	    for(int i=0;(i+12)<dataListadoCitas.length;i+=13)
	    {
	    	if(!codCA.equals(dataListadoCitas[i+12].toString()))
	    	{	    		
	    	    section.setTableCellsColSpan(10);
	    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+11].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    codCA = dataListadoCitas[i+12].toString();	    	    
	    	    
	    	    report.font.setFontAttributes(0x000000, 7, true, false, false);
	    	    //section.setTableCellsColSpan(1);
	    	    //section.addTableTextCellAligned("detalleTable","H.C",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    //section.setTableCellsColSpan(1);
	    	    //section.addTableTextCellAligned("detalleTable","Centro Atención",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Unidad Agenda",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Fecha",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Hora",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Estado Cita",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Consultorio",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Servicios",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Profesional de la Salud",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Registro Medico",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Convenio",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Usuario",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	        
	       	    
	    	    //section.setTableCellsColSpan(1);
	    	    //section.addTableTextCellAligned("detalleTable","Estado Liquidación",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    //section.setTableCellsColSpan(1);
	    	    //section.addTableTextCellAligned("detalleTable","Cuenta",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	       	    
	    	}	    			
	    	
	    	//Primer elemento de cada ciclo:    	
    		report.font.setFontAttributes(0x000000, 7, true, false, false);
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+1].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+2].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+3].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+4].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+5].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+6].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+7].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+8].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	       	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+9].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    	    	
	    	if(!dataListadoCitas[i+10].toString().trim().equals(""))
	    	{
	    	    section.setTableCellsColSpan(2);
	    	    section.addTableTextCellAligned("detalleTable","Observación:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    section.setTableCellsColSpan(8);
	    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+10].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	}
	    }
	    
	    try 
        {
			section.getTableReference("detalleTable").setWidths(widths);
		} 
        catch (BadElementException e) 
        {
        	citasPdfLogger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
		}
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("detalle");	    
	    report.setUsuario(medico.getLoginUsuario());
	    report.closeReport();	    
	}
	
	/**
	 * Méodo que genera el PDF del Listao de Citas por Paciente 
	 * @param filename
	 * @param citasForm
	 * @param medico
	 * @param pacienteActivo
	 */
	public static String CvsCitasPaciente(
			String pathArchivo,
			String nombreArchivo,
			CitasForm citasForm, 
			UsuarioBasico medico,
			PersonaBasica pacienteActivo,
			HttpServletRequest request)
	{
		String mensaje = "";
	    String[] dataListadoCitas = new String[0];
	    
	    if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
	    	dataListadoCitas=listadoCitasPaciente(citasForm);
	    else if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
	    	dataListadoCitas=listadoCitasPacienteOdonto(citasForm);
	    
	    int numRows = (dataListadoCitas.length / 10) + 2;
	    int numCols = 10;   
	    
	    try
	    {
		    //**************INICIALIZACIÓN DEL ARCHIVO CSV***************************************************************************************
		    File archivoCSV=new File(pathArchivo,nombreArchivo+".csv");
			FileWriter streamCSV = new FileWriter(archivoCSV,false);
			BufferedWriter bufferCSV=new BufferedWriter(streamCSV);
		    //***********************************************************************************************************************************
		    		    
		    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
			citasPdfLogger.info("numero de filas: "+numRows);
			citasPdfLogger.info("numero de columnas: "+numCols);
			
			bufferCSV.write("");
			bufferCSV.write("Lista Citas Paciente");
			bufferCSV.write("\n");
		    
		    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************		    
		    //Se va escribiendo en el archivo CSV
			bufferCSV.write("PACIENTE:;");
			bufferCSV.write(pacienteActivo.getApellidosNombresPersona(false)+";");
			bufferCSV.write("No. Id:;");
			bufferCSV.write(pacienteActivo.getNumeroIdentificacionPersona()+";");
			bufferCSV.write("H.C;");
			bufferCSV.write(pacienteActivo.getHistoriaClinica()+";");
			bufferCSV.write("Tel:;");
			bufferCSV.write(pacienteActivo.getTelefono()+";");
			bufferCSV.write("\n");	
			
			
			//************DETALLE***************************************************************************************************
			 String codCA = ConstantesBD.codigoNuncaValido+"";
		    for(int i=0;(i+12)<dataListadoCitas.length;i+=13)
		    {
		    	if(!codCA.equals(dataListadoCitas[i+12].toString()))
		    	{	    		
		    		bufferCSV.write(dataListadoCitas[i+11].toString());
		    		bufferCSV.write("\n");		    	    
		    	    codCA = dataListadoCitas[i+12].toString();
		    	    
		    	    bufferCSV.write("Unidad Agenda;");
					bufferCSV.write("Fecha;");
					bufferCSV.write("Hora;");
					bufferCSV.write("Estado Cita;");
					bufferCSV.write("Consultorio;");
					bufferCSV.write("Servicios;");
					bufferCSV.write("Profesional de la Salud;");
					bufferCSV.write("Registro Medico;");
					bufferCSV.write("Convenio;");
					bufferCSV.write("Usuario;");
					// *** Agregado x tarea 137680
					bufferCSV.write("Observación");
					// ***************************
					bufferCSV.write("\n");
		    	}	    	
		    	
		    	bufferCSV.write(dataListadoCitas[i].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+1].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+2].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+3].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+4].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+5].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+6].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+7].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+8].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+9].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+10].toString());
		    	bufferCSV.write("\n");	    	
	    	
		    	if(!dataListadoCitas[i+10].toString().trim().equals(""))
		    	{
		    		bufferCSV.write("Observación;");
		    		bufferCSV.write(dataListadoCitas[i+10].toString());
		    		bufferCSV.write("\n");		    		    	    
		    	}
		    }
		    
		    //Se finaliza el archivo CSV
		    bufferCSV.close();		   
		   	    		    	    
		    //se genera el archivo en formato Zip
			BackUpBaseDatos.EjecutarComandoSO("zip -j "+pathArchivo+nombreArchivo+".zip "+pathArchivo+nombreArchivo+".csv");		
			
			if(!UtilidadFileUpload.existeArchivo(pathArchivo, nombreArchivo+".zip"))
				mensaje = "";
			else			
				mensaje = System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivo+".zip";			
			
			citasPdfLogger.info("valor del mensaje >> "+mensaje);
	    }	 
	    catch(IOException e)
	    {
	    	citasPdfLogger.error("Error tratando de generar el archivo CSV: "+nombreArchivo+" (Se cancela proceso) :"+e);
	    }   
	    
	    return mensaje;
	}	
	
	
	/**
	 * Méodo que genera el PDF del Listao de Citas por Paciente 
	 * @param filename
	 * @param citasForm
	 * @param medico
	 * @param pacienteActivo
	 */
	public static String CvsCitasFecha(
			String pathArchivo,
			String nombreArchivo,
			CitasForm citasForm, 
			UsuarioBasico medico,
			PersonaBasica pacienteActivo,
			HttpServletRequest request)
	{
		String mensaje = "";		   
	    String[] dataListadoCitas = new String[0];
	    
	    if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
	    	dataListadoCitas=listadoCitasFecha(citasForm);
	    else if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
	    	dataListadoCitas=listadoCitasFechaOdonto(citasForm);
	    int numRows = (dataListadoCitas.length / 10) + 2;
	    int numCols = 10;   
	    
	    try
	    {
		    //**************INICIALIZACIÓN DEL ARCHIVO CSV***************************************************************************************
		    File archivoCSV=new File(pathArchivo,nombreArchivo+".csv");
			FileWriter streamCSV = new FileWriter(archivoCSV,false);
			BufferedWriter bufferCSV=new BufferedWriter(streamCSV);
		    //***********************************************************************************************************************************
		    		    
		    //*****************SE GENERA LA TABLA DEL REPORTE*************************************************************************************
			citasPdfLogger.info("numero de filas: "+numRows);
			citasPdfLogger.info("numero de columnas: "+numCols);
			
			bufferCSV.write("");
			bufferCSV.write("Lista Citas Fecha");
			bufferCSV.write("\n");
		    
		    //**********SE DIBUJAN LOS TÍTULOS DE LAS COLUMNAS******************************************************************		    
		    //Se va escribiendo en el archivo CSV
			bufferCSV.write("FECHA:;");
			bufferCSV.write(citasForm.getFechaInicio()+" - "+citasForm.getFechaFin());			
			bufferCSV.write("\n");			
			
			//************DETALLE***************************************************************************************************
			 String codCA = ConstantesBD.codigoNuncaValido+"";
		    for(int i=0;(i+16)<dataListadoCitas.length;i+=17)
		    {
		    	if(!codCA.equals(dataListadoCitas[i+16].toString()))
		    	{	    		
		    		bufferCSV.write(dataListadoCitas[i+15].toString());
		    		bufferCSV.write("\n");		    	    
		    	    codCA = dataListadoCitas[i+16].toString(); 
		    	    
		    	    bufferCSV.write("Unidad Agenda;");
					bufferCSV.write("Fecha;");
					bufferCSV.write("Hora;");
					bufferCSV.write("Estado Cita;");
					bufferCSV.write("Consultorio;");
					bufferCSV.write("Servicios;");
					bufferCSV.write("Profesional de la Salud;");
					bufferCSV.write("Registro Medico;");
					bufferCSV.write("ID;");
					bufferCSV.write("Paciente;");
					bufferCSV.write("H.C;");
					bufferCSV.write("Telefono;");		
					bufferCSV.write("Convenio;");
					bufferCSV.write("Usuario;");
					bufferCSV.write("Observación");
					bufferCSV.write("\n");
		    	}	    	
		    	
		    	bufferCSV.write(dataListadoCitas[i].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+1].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+2].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+3].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+4].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+5].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+6].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+7].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+8].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+9].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+10].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+11].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+12].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+13].toString()+";");
		    	bufferCSV.write(dataListadoCitas[i+14].toString());
		    	bufferCSV.write("\n");		    	
		    		    	
		    	if(!dataListadoCitas[i+14].toString().trim().equals(""))
		    	{
		    		bufferCSV.write("Observación;");
		    		bufferCSV.write(dataListadoCitas[i+14].toString());
		    		bufferCSV.write("\n");		    		    	    
		    	}
		    }
		    
		    //Se finaliza el archivo CSV
		    bufferCSV.close();		   
		   	    		    	    
		    //se genera el archivo en formato Zip
			BackUpBaseDatos.EjecutarComandoSO("zip -j "+pathArchivo+nombreArchivo+".zip "+pathArchivo+nombreArchivo+".csv");		
			
			if(!UtilidadFileUpload.existeArchivo(pathArchivo, nombreArchivo+".zip"))
				mensaje = "";
			else			
				mensaje = System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivo+".zip";			
			
			citasPdfLogger.info("valor del mensaje >> "+mensaje);
	    }	 
	    catch(IOException e)
	    {
	    	citasPdfLogger.error("Error tratando de generar el archivo CSV: "+nombreArchivo+" (Se cancela proceso) :"+e);
	    }   
	    
	    return mensaje;
	}

	
	/**
	 * Método que consulta el listado de citas cuano se busca por Fecha 
	 * @param citasForm
	 * @return
	 */ 
	public static String[] listadoCitasFecha(CitasForm citasForm)
	{
		CitaForm cita=new CitaForm();
		String[] dataListadoCitas;
		int i=0;
		ArrayList resultados=new ArrayList();
		if (citasForm.getListaCitas().size()==0)
		{
		    return new String[0];
		}
		
		for(int cont=0;cont<citasForm.getListaCitas().size();cont++)
		{			
				cita=(CitaForm)citasForm.getListaCitas().get(cont);
				
				resultados.add(i, cita.getNombreUnidadConsulta().length()>17?cita.getNombreUnidadConsulta().substring(0,18):cita.getNombreUnidadConsulta());
				resultados.add(i+1, cita.getFechaInicio());
			    resultados.add(i+2, cita.getHoraInicio());
			    resultados.add(i+3, cita.getNombreEstadoCita().length()>15?cita.getNombreEstadoCita().substring(0,15):cita.getNombreEstadoCita());
			    resultados.add(i+4, cita.getCodigoConsultorioUsua());
			    resultados.add(i+5, UtilidadesConsultaExterna.obtenerListadoCodigosServiciosCita(cita.getCodigoCita(), ConstantesBD.codigoTarifarioCups));
			    resultados.add(i+6, cita.getNombreCompletoMedico().length()>30?cita.getNombreCompletoMedico().substring(0,30).toUpperCase():cita.getNombreCompletoMedico().toUpperCase());
			    resultados.add(i+7, cita.getRegistroMedico());			    
			    resultados.add(i+8, cita.getNombreTipoDocumento()+" "+cita.getNumeroIdentificacionPaciente());
			    resultados.add(i+9, cita.getNombreCompletoPaciente().length()>30?cita.getNombreCompletoPaciente().substring(0,30).toUpperCase():cita.getNombreCompletoPaciente().toUpperCase());			    
			    resultados.add(i+10, cita.getNumeroHistoriaClinica());
			    resultados.add(i+11, cita.getTelefono());			    
			    resultados.add(i+12, cita.getNombreConvenio().length()>26?cita.getNombreConvenio().substring(0,26):cita.getNombreConvenio());
			    resultados.add(i+13, cita.getLoginUsuCita());		    
			    resultados.add(i+14,cita.getMotivoNoAtencion().toLowerCase()+" "+cita.getObservacion());
			    
				resultados.add(i+15, cita.getNombreCentroAtencion().toUpperCase());
				resultados.add(i+16, cita.getCentroAtencion());		

			    i=i+17;
		}
		dataListadoCitas = new String[resultados.size()];
		UtilidadTexto.copiarArrayListAArreglo(resultados, dataListadoCitas);
		return dataListadoCitas;
	}
	
	/**
	 * Método que consulta el listado de citas cuano se busca por Fecha 
	 * @param citasForm
	 * @return
	 */ 
	public static String[] listadoCitasFechaOdonto(CitasForm citasForm)
	{
		String[] dataListadoCitas;
		int i=0;
		ArrayList resultados=new ArrayList();
		if (citasForm.getListCitas().size()==0)
		{
		    return new String[0];
		}
		Connection connection = UtilidadBD.abrirConexion();
		for (DtoCitaOdontologica cita: citasForm.getListCitas()){
				
				resultados.add(i, cita.getAgendaOdon().getDescripcionUniAgen().length()>17?cita.getAgendaOdon().getDescripcionUniAgen().substring(0,18):cita.getAgendaOdon().getDescripcionUniAgen());
				resultados.add(i+1, cita.getAgendaOdon().getFecha());
			    resultados.add(i+2, cita.getHoraInicio());
			    resultados.add(i+3, cita.getEstado().length()>15?cita.getEstado().substring(0,15):cita.getEstado());
			    resultados.add(i+4, cita.getAgendaOdon().getConsultorio());
			    String servicios = "";
			    for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon()){
			    	servicios += Utilidades.obtenerCodigoPropietarioServicio(connection, elem.getCodigoPk()+"", Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(citasForm.getInstitucion())))+",";
			    }
			    if (servicios.length()>0)
			    	servicios = servicios.substring(0, servicios.length()-1);
			    resultados.add(i+5, servicios);
			    resultados.add(i+6, cita.getAgendaOdon().getNombreMedico().length()>30?cita.getAgendaOdon().getNombreMedico().substring(0,30).toUpperCase():cita.getAgendaOdon().getNombreMedico().toUpperCase());
			    resultados.add(i+7, cita.getAgendaOdon().getRegistroMedico());			    
			    resultados.add(i+8, cita.getNumeroIdentificacionPac());
			    resultados.add(i+9, cita.getNombrePaciente().length()>30?cita.getNombrePaciente().substring(0,30).toUpperCase():cita.getNombrePaciente().toUpperCase());			    
			    resultados.add(i+10, cita.getHistoriaClinica());
			    resultados.add(i+11, cita.getTelefonoPaciente());
			    resultados.add(i+12, Utilidades.obtenerNombreConvenio(connection, Utilidades.convertirAEntero(cita.getCuenta()))==null?"":
			    	Utilidades.obtenerNombreConvenio(connection, Utilidades.convertirAEntero(cita.getCuenta())));//convenio
			    resultados.add(i+13, cita.getUsuarioConfirma().getNombreUsuario());		    
			    resultados.add(i+14,cita.getMotivoNoAtencion().toLowerCase());
				resultados.add(i+15, cita.getAgendaOdon().getDescripcionCentAten().toUpperCase());
				resultados.add(i+16, cita.getAgendaOdon().getCentroAtencion());		

			    i=i+17;
		}
		UtilidadBD.closeConnection(connection); 
		dataListadoCitas = new String[resultados.size()];
		UtilidadTexto.copiarArrayListAArreglo(resultados, dataListadoCitas);
		return dataListadoCitas;
	}

	/**
	 * Méodo que genera el PDF del Listao de Citas todos 
	 * @param filename
	 * @param citasForm
	 * @param medico
	 * @param pacienteActivo
	 */
	public static void pdfCitasFecha(String filename, CitasForm citasForm, UsuarioBasico medico, HttpServletRequest request)
	{	
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
		PdfReports report = new PdfReports("false",true);        
        report.setUsuario(medico.getNombreUsuario()+"("+ medico.getLoginUsuario()+")");
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "CITAS");        
        report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);	    
    	report.openReport(filename);	    

	    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	    report.font.setFontSizeAndAttributes(7, false, false, false);
	    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 8);
	    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 6);

	    iTextBaseTable section;
	    String[] dataListadoCitas = new String[0];
	    if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
	    	dataListadoCitas=listadoCitasFecha(citasForm);
	    else if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
	    	dataListadoCitas=listadoCitasFechaOdonto(citasForm);
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    //Se iteran las citas
	    //float widths[]={(float)0.8,(float)0.5,(float)0.3,(float)0.7 ,(float)0.3,(float)0.5,(float)1.2,(float)0.7,(float)0.9,(float)1.1,(float)0.6,(float)0.6,(float)1.4,(float)0.4};
	    float widths[]={(float)0.8,(float)0.5,(float)0.3,(float)0.7 ,(float)0.3,(float)0.4,(float)1.4,(float)0.5,(float)0.7,(float)1.2,(float)0.6,(float)0.6,(float)1.4,(float)0.6};
	    int numRows = (dataListadoCitas.length / 14) + 2;   
	    int cont = 0;
	    
	    section = report.createSection("detalle", "detalleTable",numRows,14, 0.5f);
		section.setTableBorder("detalleTable", 0x000000, 0.1f);
	    section.setTableCellBorderWidth("detalleTable", 0.1f);
	    section.setTableCellPadding("detalleTable", 1);
	    section.setTableSpaceBetweenCells("detalleTable", 0.1f);
	    section.setTableCellsDefaultColors("detalleTable", 0xFFFFFF, 0x000000);
	    
	    report.font.setFontAttributes(0x000000, 6, true, false, false);
	    section.setTableCellsColSpan(9);
	    section.addTableTextCellAligned("detalleTable","Listado de Citas",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    
	    section.setTableCellsColSpan(5);
	    section.addTableTextCellAligned("detalleTable","FECHA: "+citasForm.getFechaInicio()+" - "+citasForm.getFechaFin(),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	        
	    String codCA = ConstantesBD.codigoNuncaValido+""; 
	    for(int i=0;(i+16)<dataListadoCitas.length;i+=17)
	    {
	    	//citasPdfLogger.info("valores >> "+dataListadoCitas[i+16].toString()+" "+codCA+" "+dataListadoCitas[i+15]);
	    	if(!codCA.equals(dataListadoCitas[i+16].toString()))
	    	{	    		
	    		section.setTableCellBorderWidth("detalleTable", 0.1f);
	    		section.setTableCellsDefaultColors("detalleTable", 0xFFFFFF, 0x000000);
	    	    section.setTableCellsColSpan(14);
	    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+15].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    codCA = dataListadoCitas[i+16].toString();
	    	    
	    	    report.font.setFontAttributes(0x000000, 6, true, false, false);
	      	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Unidad Agenda",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Fecha",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Hora",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Estado Cita",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Cons.",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Servicios",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Profesional de la Salud",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Reg. Medico",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","ID",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Paciente",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","H.C",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Telefono",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	        
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Convenio",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    
	    	    section.setTableCellsColSpan(1);
	    	    section.addTableTextCellAligned("detalleTable","Usuario",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	       	    
	    	}
	    	
	    	//Primer elemento de cada ciclo:	    	
	    	section.setTableCellBorderWidth("detalleTable", 0.0f);    	
    		report.font.setFontAttributes(0x000000, 6, true, false, false);
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+1].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+2].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+3].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+4].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+5].toString(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+6].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+7].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+8].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	        	       	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+9].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
    	    section.setTableCellsColSpan(1);
    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+10].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
    	    
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+11].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    		    	
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+12].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    		    	
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+13].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    
	    	if(!dataListadoCitas[i+14].toString().trim().equals(""))
	    	{
	    	    section.setTableCellsColSpan(2);
	    	    section.addTableTextCellAligned("detalleTable","Observación:",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	    section.setTableCellsColSpan(12);
	    	    section.addTableTextCellAligned("detalleTable",dataListadoCitas[i+14].toString(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_MIDDLE);
	    	}
	    }
	    
	    try 
        {
			section.getTableReference("detalleTable").setWidths(widths);
		} 
        catch (BadElementException e) 
        {
        	citasPdfLogger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
		}
	    
	    //se añade la sección al documento
	    report.addSectionToDocument("detalle");	    
	    report.closeReport();	    
	}
}