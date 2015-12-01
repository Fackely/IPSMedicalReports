package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;
import util.xml.UtilidadAutorizacionesEDI;

import com.princetonsa.actionform.manejoPaciente.AutorizacionesForm;
import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInfAtencionIniUrgForm;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.Diagnostico;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.EstructuraSolicitudAutorizacion;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.General;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.Identificacion;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.InformacionPersonal;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.NombreCompleto;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.Paciente;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.Pagador;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.ProfesionalSalud;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.ServicioSalud;
import com.princetonsa.autorizaciones.solicitudAutorizacionServicios.UbicacionGeografica;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagInfoIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;

public class SolicitudAutorizacionPdf 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(SolicitudAutorizacionPdf.class);
	
	
	/**
     * Método para Realizar la Impresión del Informe Técnico de Solicitud de Autorizaciones
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe
     */
    public static String pdfInformeTecnicoSolicitudAutorizacion(UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, AutorizacionesForm forma)
    {
    	///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aInfoTec" + r.nextInt()  +".pdf";
    	
    	// se carga los datos de la institucion
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	// se cargan los datos requeridos para el informe 
    	DtoAutorizacion dtoAutorizacion = Autorizaciones.caragarInformeTecnico(UtilidadBD.abrirConexion(), 
    			Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()),
    			institucionBasica.getCodigo());
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="ATENCION INICIAL URGENCIAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        HashMap encabezado = new HashMap<String,Object>();
        encabezado.put("entidad_requiere", "MINISTERIO DE LA PROTECCION SOCIAL");
        encabezado.put("titulo", "SOLICITUD DE AUTORIZACIÓN DE SERVICIOS DE SALUD");
        encabezado.put("numero_atencion", "Número Solicitud: "+dtoAutorizacion.getConsecutivo()+dtoAutorizacion.getAnioConsecutivo());
        encabezado.put("fecha", dtoAutorizacion.getFechaSolicitud());
        encabezado.put("hora", dtoAutorizacion.getHoraSolicitud());
        report.setReportBaseHeader2(encabezado);
		
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        //**************SECCION PRESTADOR*********************************************************************
        iTextBaseTable section;
        section = report.createSection("prestador", "prestadorTable", 3, 3, 10);
        
        section.setTableBorder("prestadorTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("prestadorTable", 1.0f);
	    section.setTableCellPadding("prestadorTable", 1);
	    section.setTableSpaceBetweenCells("prestadorTable", 0.5f);
	    section.setTableCellsDefaultColors("prestadorTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("prestadorTable", "Información del Prestador", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("prestadorTable", 0.5f);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("prestadorTable", "Nombre: "+institucionBasica.getRazonSocial(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", (institucionBasica.getTipoIdentificacion().equals("NI")?institucionBasica.getDescripcionTipoIdentificacion():institucionBasica.getTipoIdentificacion())+" "+(!institucionBasica.getNit().equals("")?institucionBasica.getNit():"")+""+(!institucionBasica.getDigitoVerificacion().equals("")?"-"+institucionBasica.getDigitoVerificacion():""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", "Código: "+institucionBasica.getCodMinsalud(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("prestadorTable", "Dirección Prestador: "+institucionBasica.getDireccion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    String indicativo = institucionBasica.getIndicativo().equals("")?"":"Ind. ("+institucionBasica.getIndicativo()+") - ";
	    String extension = institucionBasica.getExtension().equals("")?"":" - Ext. ("+institucionBasica.getExtension()+")";
	    section.addTableTextCellAligned("prestadorTable", "Teléfono: "+indicativo+institucionBasica.getTelefono()+extension, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    
	    section.addTableTextCellAligned("prestadorTable", "Departamento: "+institucionBasica.getDepto()+" "+(!institucionBasica.getCodigoDepto().equals("")?" ("+institucionBasica.getCodigoDepto()+")":""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("prestadorTable", "Municipio: "+institucionBasica.getCiudad()+" "+(!institucionBasica.getCodigoCiudad().equals("")?" ("+institucionBasica.getCodigoCiudad()+")":""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(2);
    	section.addTableTextCellAligned("prestadorTable", "Entidad a al que se le Informa (Pagador): "+dtoAutorizacion.getConvenioInformeTec().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
    	section.setTableCellsColSpan(1);
    	section.addTableTextCellAligned("prestadorTable", "Código: "+dtoAutorizacion.getConvenioInformeTec().getCodigo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al docuemtno
	    report.addSectionToDocument("prestador");
	    //**************FIN SECCION PRESTADOR*********************************************************************
	    
	    //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("paciente", "pacienteTable", 3, 4, 10);
        section.setTableBorder("pacienteTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("pacienteTable", 1.0f);
	    section.setTableCellPadding("pacienteTable", 1);
	    section.setTableSpaceBetweenCells("pacienteTable", 0.5f);
	    section.setTableCellsDefaultColors("pacienteTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("pacienteTable", "Datos del Paciente", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("pacienteTable", 0.5f);
	    section.addTableTextCellAligned("pacienteTable", "Apellidos y Nombres: "+paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Tipo Identificación: "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona()+" de "+paciente.getNombreCiudadExpedicion()+" ("+paciente.getNombrePaisExpedicion()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Fecha Nacimiento : "+paciente.getFechaNacimiento(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("pacienteTable", "Dirección de Residencia Habitual: "+paciente.getDireccion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Teléfono: "+paciente.getTelefonoFijo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Departamento: "+paciente.getNombreDeptoVivienda()+" "+(!paciente.getCodigoDeptoVivienda().equals("")?" ("+paciente.getCodigoDeptoVivienda()+")":""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Municipio: "+paciente.getNombreCiudadVivienda()+" "+(!paciente.getCodigoCiudadVivienda().equals("")?" ("+paciente.getCodigoCiudadVivienda()+")":""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);

	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Teléfono Celular: "+paciente.getTelefonoCelular(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Correo Electrónico: "+paciente.getEmail(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("pacienteTable", "Cobertura en Salud: "+dtoAutorizacion.getNombreCobertura(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    // se añade la seccion al documento    
	    report.addSectionToDocument("paciente");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
	    
	    
	    //**************SECCION INFORMACIÓN DE LA ATENCIÓN*********************************************************************
        section = report.createSection("atencion", "atencionTable", 3, 4, 10);
        section.setTableBorder("atencionTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("atencionTable", 1.0f);
	    section.setTableCellPadding("atencionTable", 1);
	    section.setTableSpaceBetweenCells("atencionTable", 0.5f);
	    section.setTableCellsDefaultColors("atencionTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("atencionTable", "Información de la Atención y Servicios Solicitados", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("atencionTable", 0.5f);
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("atencionTable", "Origen de la Atención: "+dtoAutorizacion.getNombreAtencio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("atencionTable", "Tipo de Servicios Solicitados: "+dtoAutorizacion.getTipoServicioSolicitado().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("atencionTable", "Proridad de la Atención: "+(dtoAutorizacion.getPrioridadAtencion().equals(ConstantesBD.acronimoNo)?"No Prioritaria":"Prioritaria"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("atencionTable", "Manejo Integral Según Guía de :", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    // ubicacion del  paciente
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("atencionTable", "Ubicación del Paciente al Momento de la Solicitud de Autorizacion: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", dtoAutorizacion.getNombreViaIngreso(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("atencionTable", "Servicio: "+dtoAutorizacion.getServicioHospitalizacion(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Cama: "+dtoAutorizacion.getCamaHospitalizacion(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    // la parte de los servicio
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Código CUPS ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Cantidad ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("atencionTable", "Descripción ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    Iterator iter = dtoAutorizacion.getDetalle().iterator();
	    int i = 1 ;
	    StringBuffer justificacion = new StringBuffer(); 
	    DtoDetAutorizacion dtoDetAuto = new DtoDetAutorizacion();
	    while(iter.hasNext())
	    {
	    	dtoDetAuto = (DtoDetAutorizacion) iter.next();
	    	section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("atencionTable", i+")  "+(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("atencionTable", dtoDetAuto.getCantidad()+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre(),
		    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    justificacion.append(dtoDetAuto.getJustificacionSolicitud().equals("")?"":i+") "+dtoDetAuto.getJustificacionSolicitud()+".  ");
		    i+=1;
	    }
	    
	    // justificacion clinica 
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("atencionTable", "Justificación Clínica: "+justificacion, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    
	    // diagnosticos de la solicitud de autorizacion
	    iter = dtoAutorizacion.getDiagnosticos().iterator();
	    DtoDiagAutorizacion diagAuto = new DtoDiagAutorizacion();
	    for(int conta=0;conta<4;conta++) 
	    {
	    	if(iter.hasNext())
	    		diagAuto = (DtoDiagAutorizacion) iter.next();
	    	else
	    		diagAuto.clean();
	    	
	    	if(diagAuto.getDiagnostico().isPrincipal()){
	    		section.setTableCellsColSpan(1);
		    	section.addTableTextCellAligned("atencionTable", "Diagnóstico Principal ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}else{
	    		section.setTableCellsColSpan(1);
		    	section.addTableTextCellAligned("atencionTable", "Diagnóstico Relacionado "+conta, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("atencionTable", diagAuto.getDiagnostico().getAcronimo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", diagAuto.getDiagnostico().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("atencion");
	    //**************FIN SECCION INFORMACIÓN DE LA ATENCIÓN*********************************************************************
	    
	  //**************SECCION INFORMACIÓN DE LA PERSONA QUE INFORMA*********************************************************************
        section = report.createSection("persinfo", "persinfoTable", 3, 4, 10);
        section.setTableBorder("persinfoTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("persinfoTable", 1.0f);
	    section.setTableCellPadding("persinfoTable", 1);
	    section.setTableSpaceBetweenCells("persinfoTable", 0.5f);
	    section.setTableCellsDefaultColors("persinfoTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("persinfoTable", "Información de la Persona que Solicita", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("persinfoTable", 0.5f);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("persinfoTable", "Apellidos y Nombres: "+dtoAutorizacion.getNombreUsuSol(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("persinfoTable", "Teléfono: "+indicativo+institucionBasica.getTelefono()+extension, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("persinfoTable", "Cargo: "+dtoAutorizacion.getNombreCargoUsuSol(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("persinfoTable", "Teléfono Celular: "+dtoAutorizacion.getTelefonoCelUsuSol(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("persinfo");
	    //**************FIN SECCION INFORMACIÓN DE LA PERSONA QUE INFORMA*********************************************************************
	    
	    
	    report.closeReport();
	    return nombreArchivo;
    }
	
    /**
     * Método para Realizar la Impresión del Informe Estandar de Solicitud de Autorizaciones
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe
     */
    public static String pdfInformeEstandarSolicitudAutorizacion(UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, AutorizacionesForm forma)
    {
    	///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aInfoEst" + r.nextInt()  +".pdf";
    	
    	// se carga los datos de la institucion
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	// se cargan los datos requeridos para el informe 
    	DtoAutorizacion dtoAutorizacion = Autorizaciones.caragarInformeTecnico(UtilidadBD.abrirConexion(), 
    			Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()),
    			institucionBasica.getCodigo());
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="ATENCION INICIAL URGENCIAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        String tituloMedicamentos="SOLICITUD DE AUTORIZACION ";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        iTextBaseTable section;
        
      //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("solauto", "solautoTable", 2, 3, 10);
        section.setTableBorder("solautoTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("solautoTable", 1.0f);
	    section.setTableCellPadding("solautoTable", 1);
	    section.setTableSpaceBetweenCells("solautoTable", 0.5f);
	    section.setTableCellsDefaultColors("solautoTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Solicitud Autorizacion
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("solautoTable", "Solicitud Autorizacion", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("solautoTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("solautoTable", "Número Solicitud: "+dtoAutorizacion.getConsecutivo()+dtoAutorizacion.getAnioConsecutivo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("solautoTable", "Fecha Solicitud: "+dtoAutorizacion.getFechaSolicitud(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("solautoTable", "Hora Solicitud: "+dtoAutorizacion.getHoraSolicitud(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("solauto");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
        
        
         //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("paciente", "pacienteTable", 3, 4, 10);
        section.setTableBorder("pacienteTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("pacienteTable", 1.0f);
	    section.setTableCellPadding("pacienteTable", 1);
	    section.setTableSpaceBetweenCells("pacienteTable", 0.5f);
	    section.setTableCellsDefaultColors("pacienteTable", 0xFFFFFF, 0x000000);
	    
	    // Datos del Prestador
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("pacienteTable", "Datos del Paciente", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("pacienteTable", 0.5f);
	    section.addTableTextCellAligned("pacienteTable", "Apellidos y Nombres: "+paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Tipo Identificación: "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona()+" de "+paciente.getNombreCiudadExpedicion()+" ("+paciente.getNombrePaisExpedicion()+")", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("pacienteTable", "Fecha Nacimiento : "+paciente.getFechaNacimiento(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("paciente");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
	    
	    
	    //**************SECCION INFORMACIÓN DE LA ATENCIÓN*********************************************************************
	    if(dtoAutorizacion.getDetalle().size()>0)
	    {
		    section = report.createSection("atencion", "atencionTable", 3, 4, 10);
	        section.setTableBorder("atencionTable", 0x000000, 1.0f);
		    section.setTableCellBorderWidth("atencionTable", 1.0f);
		    section.setTableCellPadding("atencionTable", 1);
		    section.setTableSpaceBetweenCells("atencionTable", 0.5f);
		    section.setTableCellsDefaultColors("atencionTable", 0xFFFFFF, 0x000000);
		    
		    // la parte de los servicio
		    section.setTableCellsColSpan(4);
		    report.font.setFontAttributes(0x000000, 12, true, false, false);
		    section.addTableTextCellAligned("atencionTable", "Servicios Solicitados ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    Iterator iter = dtoAutorizacion.getDetalle().iterator();
		    DtoDetAutorizacion dtoDetAuto = new DtoDetAutorizacion();
		    while(iter.hasNext())
		    {
		    	dtoDetAuto = (DtoDetAutorizacion) iter.next();
		    	section.setTableCellsColSpan(3);
			    section.addTableTextCellAligned("atencionTable", "Servicio/Articulo: "+(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS()+" - ":"")+(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre()),
			    		report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    	section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("atencionTable", "Cantidad: "+dtoDetAuto.getCantidad(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(4);
			    section.addTableTextCellAligned("atencionTable", "Justificiación Clínica: "+dtoDetAuto.getJustificacionSolicitud(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    }
	    }else{
	    	section = report.createSection("atencion", "atencionTable", 2, 1, 10);
	        section.setTableBorder("atencionTable", 0x000000, 1.0f);
		    section.setTableCellBorderWidth("atencionTable", 1.0f);
		    section.setTableCellPadding("atencionTable", 1);
		    section.setTableSpaceBetweenCells("atencionTable", 0.5f);
		    section.setTableCellsDefaultColors("atencionTable", 0xFFFFFF, 0x000000);
		    report.font.setFontAttributes(0x000000, 12, true, false, false);
		    section.addTableTextCellAligned("atencionTable", "Servicios Solicitados ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    section.addTableTextCellAligned("atencionTable", "No Hay Registro de Servicios Solicitados", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    // se añade la seccion al documento    
	    report.addSectionToDocument("atencion");
	    //**************FIN SECCION INFORMACIÓN DE LA ATENCIÓN*********************************************************************
	    
	    report.closeReport();
	    return nombreArchivo;
    }
    
    /**
     * Método para Realizar la Impresión del Informe de Respuesta de la Solicitud de Autorizaciones
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe
     */
    public static String pdfInformeRespustaSolicitudAutorizacion(UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, AutorizacionesForm forma)
    {
    	///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aInfoEst" + r.nextInt()  +".pdf";
    	
    	// se carga los datos de la institucion
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	// se cargan los datos requeridos para el informe 
    	DtoAutorizacion dtoAutorizacion = Autorizaciones.caragarInformeTecnico(UtilidadBD.abrirConexion(), 
    			Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()),
    			institucionBasica.getCodigo());
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="ATENCION INICIAL URGENCIAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        String tituloMedicamentos="SOLICITUD DE AUTORIZACION ";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        iTextBaseTable section;
        
      //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("respsolauto", "respsolautoTable", 12, 4, 10);
        section.setTableBorder("respsolautoTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("respsolautoTable", 1.0f);
	    section.setTableCellPadding("respsolautoTable", 1);
	    section.setTableSpaceBetweenCells("respsolautoTable", 0.5f);
	    section.setTableCellsDefaultColors("respsolautoTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Solicitud Autorizacion
	    
	    //logger.info("tanano arrayList DtoDetAuto >>>>>>>> "+dtoAutorizacion.getDetalle().size());
	    boolean band = false;
	    String estado = "";
	    Iterator iter = dtoAutorizacion.getDetalle().iterator();
	    DtoDetAutorizacion dto = new DtoDetAutorizacion(); 
	    while(iter.hasNext()&&band==false)
	    {
	    	dto = (DtoDetAutorizacion) iter.next();
	    	//if(forma.getCodigoPkAutoInd().equals(forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK()))
	    	//{
	    		band = true;
			    section.setTableCellsColSpan(4);
			    report.font.setFontAttributes(0x000000, 12, true, false, false);
			    section.addTableTextCellAligned("respsolautoTable", "Respuesta Autorizacion", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellBorderWidth("respsolautoTable", 0.5f);
			    if(dto.getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			    	estado = "Autorizada(o)";
			    else if(dto.getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
			    	estado = "Negada(o)";
			    else if(dto.getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			    	estado = "Anulada(o)";
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Estado Autorización: "+estado, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Vigencia: "+dto.getRespuestaDto().getVigencia()+" "+dto.getRespuestaDto().getTipoVigencia().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Fecha/Hora Autorización: "+dto.getRespuestaDto().getFechaAutorizacion()+" - "+dto.getRespuestaDto().getHoraAutorizacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Número Autorización: "+dto.getRespuestaDto().getNumeroAutorizacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Cantida Solicitada: "+dto.getRespuestaDto().getCantidadSolicitada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Cantidad Autorizada: "+dto.getRespuestaDto().getCantidadAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Fecha Inicial Autorizada: "+dto.getRespuestaDto().getFechaInicialAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Fecha Final Autorizada: "+dto.getRespuestaDto().getFechaFinalAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(4);
			    section.addTableTextCellAligned("respsolautoTable", "Persona que Autoriza: "+dto.getRespuestaDto().getPersonaAutoriza(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(4);
			    section.addTableTextCellAligned("respsolautoTable", "Observacions Autorización: "+dto.getRespuestaDto().getObservacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Valor de la Cobertura: "+dto.getRespuestaDto().getValorCobertura()+" "+(dto.getRespuestaDto().getTipoCobertura().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionValor)?"Valor":"Porcentaje"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(2);
			    section.addTableTextCellAligned("respsolautoTable", "Valor de Pago del Paciente: "+dto.getRespuestaDto().getValorPagoPaciente()+" "+(dto.getRespuestaDto().getTipoPagoPaciente().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionValor)?"Valor":"Porcentaje"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(4);
			    report.font.setFontAttributes(0x000000, 12, true, false, false);
			    section.addTableTextCellAligned("respsolautoTable", "Información Solicitud", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellBorderWidth("respsolautoTable", 0.5f);
			    section.addTableTextCellAligned("respsolautoTable", "Número Solicitud: "+dtoAutorizacion.getConsecutivo()+dtoAutorizacion.getAnioConsecutivo() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(3);
			    section.addTableTextCellAligned("respsolautoTable", "Persona que Recibe Autorización: "+dto.getRespuestaDto().getPersonaRecibe(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("respsolautoTable", "Cargo: "+dto.getRespuestaDto().getNombreCargoPersRecibe(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(3);
			    section.addTableTextCellAligned("respsolautoTable", "Persona que Registra Autorización: "+dto.getRespuestaDto().getPersonaRegistro(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("respsolautoTable", "Cargo: "+dto.getRespuestaDto().getNombreCargoPersRegistro(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(4);
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    section.addTableTextCellAligned("respsolautoTable", "Adjuntos: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    report.font.setFontAttributes(0x000000, 8, false, false, false);
			    // mostrar los archivos adjuntos
			    if(dto.getRespuestaDto().getAdjuntos().size()>0)
			    {
			    	Iterator iteradj = dto.getRespuestaDto().getAdjuntos().iterator();
			    	DtoAdjAutorizacion adjAuto = new DtoAdjAutorizacion(); 
			    	while(iteradj.hasNext())
			    	{
			    		adjAuto = (DtoAdjAutorizacion) iteradj.next();
			    		section.setTableCellsColSpan(2);
					    section.addTableTextCellAligned("respsolautoTable", "Archivo Original: "+adjAuto.getNombreOriginal(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
					    section.setTableCellsColSpan(2);
					    section.addTableTextCellAligned("respsolautoTable", "Archivo Sistema: "+adjAuto.getNombreArchivo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    	}
			    }else{
			    	section.setTableCellsColSpan(4);
				    section.addTableTextCellAligned("respsolautoTable", "No hay Registro de Archivos Adjuntos ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
			    }
	    	//}
	    }
	    // se añade la seccion al documento    
	    report.addSectionToDocument("respsolauto");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
	    
	    report.closeReport();
	    return nombreArchivo;
    }
    
    /**
     * 
     * @param con
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe
     * @param forma
     * @return
     */
    public static String xmlInformeSolAuto(Connection con, 
    		UsuarioBasico usuario, 
    		HttpServletRequest request, 
    		PersonaBasica paciente,  
    		AutorizacionesForm forma, int codigo)
    {
		int band=ConstantesBD.codigoNuncaValido;
		boolean exist = false;
	  	//se edita nombre del archivo 
		String nombreArchivo = "";
		String path = "";
		String url = "";
    	Random r=new Random();
    	nombreArchivo="SolAutoXML"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
    	path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
    	url="../upload"+System.getProperty("file.separator");
    	// se cargan los datos requeridos para el informe
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	logger.info("autorizacion 1: "+forma.getCodigoPkAutoInd());
    	logger.info("autorizacion 2: "+forma.getCodigoPkAutoCon());
    	logger.info("autorizacion 3: "+forma.getAutorizacionDto().getCodigoPK());
    	logger.info("autorizacion 4: "+forma.getAutorizacionDtoAux().getCodigoPK());
    	DtoAutorizacion dto = Autorizaciones.caragarInformeTecnico(con,codigo,institucionBasica.getCodigo());
	    EstructuraSolicitudAutorizacion informeSolAuto = new EstructuraSolicitudAutorizacion();
		
		try 
		{
			//******************************************************************
			// Paciente
			NombreCompleto nomcompleto = new NombreCompleto();
			nomcompleto.setPrimerApellido(paciente.getPrimerApellido());
			nomcompleto.setSegundoApellido(paciente.getSegundoApellido());
			nomcompleto.setPrimerNombre(paciente.getPrimerNombre());
			nomcompleto.setSegundoNombre(paciente.getSegundoNombre());
			
			Identificacion identificacion = new Identificacion();
			identificacion.setNumeroIdentificacion(paciente.getNumeroIdentificacionPersona());
			identificacion.setTipoIdentificacion(paciente.getTipoIdentificacionPersona());
			
			UbicacionGeografica ubicacionGeo = new UbicacionGeografica();
			ubicacionGeo.setDepartamento(paciente.getNombreDeptoVivienda());
			ubicacionGeo.setDireccionResidenciaHabitual(paciente.getDireccion());
			ubicacionGeo.setCiudad(paciente.getNombreCiudadVivienda());
			
			try{
				if(Utilidades.convertirAEntero(paciente.getTelefonoFijo())!=ConstantesBD.codigoNuncaValido)
					ubicacionGeo.setTelefonoFijo(Utilidades.convertirAEntero(paciente.getTelefonoFijo()));
				else
					ubicacionGeo.setTelefonoFijo(0);
			}catch (Exception e) {
				ubicacionGeo.setTelefonoFijo(0);
			}
			
			XMLGregorianCalendar fecha;
			fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			fecha.setDay(Utilidades.convertirAEntero(paciente.getDiaNacimiento()));
			fecha.setMonth(Utilidades.convertirAEntero(paciente.getMesNacimiento()));
			fecha.setYear(Utilidades.convertirAEntero(paciente.getAnioNacimiento()));
			
			InformacionPersonal infoPersonal = new InformacionPersonal();
			infoPersonal.setCorreoElectronico(paciente.getEmail());
			infoPersonal.setTelefonoCelular(paciente.getTelefonoCelular());
			infoPersonal.setFechaNacimiento(fecha.toString());
			infoPersonal.setUbicacion(ubicacionGeo);
			
			Paciente pacInformeSolAuto = new Paciente();
			pacInformeSolAuto.setDatosPersonales(infoPersonal);
			pacInformeSolAuto.setIdentificacion(identificacion);
			pacInformeSolAuto.setNombre(nomcompleto);
			// Fin Paciente
			//******************************************************************
			
			//******************************************************************
			// Pagador
			Pagador pagador = new Pagador();
		  	pagador.setCodigoEntidad(dto.getConvenioInformeTec().getCodigo());
		  	pagador.setEntidadResponsable(dto.getConvenioInformeTec().getNombre());
			// Fin Pagador
			//******************************************************************
			
			//******************************************************************
			// General
			
			General general = new General();
			general.setNumero(dto.getConsecutivo()+dto.getAnioConsecutivo());
			
			XMLGregorianCalendar fechaGeneral;
			fechaGeneral = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			if(!dto.getFechaSolicitud().equals(""))
			{
				String fechaAux[] = UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaSolicitud()).split("/");
				fechaGeneral.setDay(Utilidades.convertirAEntero(fechaAux[0]));
				fechaGeneral.setMonth(Utilidades.convertirAEntero(fechaAux[1]));
				fechaGeneral.setYear(Utilidades.convertirAEntero(fechaAux[2]));
				general.setFecha(fechaGeneral);
			}
			
			XMLGregorianCalendar horaGeneral;
			horaGeneral = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			String horaAux[] = dto.getHoraSolicitud().split(":");
			horaGeneral.setTime(Utilidades.convertirAEntero(horaAux[0]), Utilidades.convertirAEntero(horaAux[1]), 0);
			general.setHora(horaGeneral);
			
			general.setPrestador(institucionBasica.getRazonSocial());
			if(institucionBasica.getTipoIdentificacion().equals("NI")){
				general.setTipoIdPrestador(institucionBasica.getDescripcionTipoIdentificacion());
				general.setIDPrestador(institucionBasica.getNit());
				general.setDigVerif(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()));
			}else{
				general.setTipoIdPrestador(institucionBasica.getTipoIdentificacion());
				general.setIDPrestador(institucionBasica.getNit());
				general.setDigVerif(0);
			}
			
			general.setCodPrestador(institucionBasica.getCodMinsalud());
			general.setDireccionPrestador(institucionBasica.getDireccion());
			try{
				general.setIndicTelefPrestador(Utilidades.convertirAEntero(institucionBasica.getIndicativo()));
			}catch (Exception e) {general.setIndicTelefPrestador(0);}
  			try{
  				general.setTelefonoPrestador(Utilidades.convertirAEntero(institucionBasica.getTelefono()));
  			}catch (Exception e) {general.setTelefonoPrestador(0);}
  			
  			general.setDepartamentoPrestador(institucionBasica.getDepto()+" ("+institucionBasica.getCodigoDepto()+")");
  			general.setMunicipioPrestador(institucionBasica.getCiudad()+" ("+institucionBasica.getCodigoCiudad()+")");
			// Fin General
			//******************************************************************
  			
  			//******************************************************************
			// Profesional Salud
  			ProfesionalSalud profeSalud = new ProfesionalSalud();
  			profeSalud.setCargo(dto.getNombreCargoUsuSol());
  			profeSalud.setNombre(dto.getNombreUsuSol());
  			profeSalud.setTelefonoCelular(dto.getTelefonoCelUsuSol());
  			try{
  				profeSalud.setTelefono(Utilidades.convertirAEntero(institucionBasica.getTelefono()));
				try{
					profeSalud.setExtTele(Utilidades.convertirAEntero(institucionBasica.getExtension()));
				}catch (Exception e) {
					profeSalud.setExtTele(0);
				}
				try{
					profeSalud.setIndicaTel(Utilidades.convertirAEntero(institucionBasica.getIndicativo()));
				}catch (Exception e) {
					profeSalud.setIndicaTel(0);
				}
			}catch (Exception e) {
				profeSalud.setTelefono(0);
				profeSalud.setExtTele(0);
				profeSalud.setIndicaTel(0);
			}
  			// Fin Profesional Salud
  			//******************************************************************
			
			//******************************************************************
			// Diagnostico
			Diagnostico diagnostico = new Diagnostico();  
			int cont = 0 ;
			Iterator ite = dto.getDiagnosticos().iterator();
			DtoDiagAutorizacion diagSolAuto = new DtoDiagAutorizacion();
		    while(ite.hasNext()){
		    	diagSolAuto = (DtoDiagAutorizacion) ite.next();
		    	if(diagSolAuto.getDiagnostico().isPrincipal()){
		    		diagnostico.setCodigoCIE10Principal(diagSolAuto.getDiagnostico().getAcronimo());
		    		diagnostico.setDescripcionPrincipal(diagSolAuto.getDiagnostico().getNombre());
		    	}else{
		    		switch (cont) {
					case 0:
						diagnostico.setCodigoCIE101(diagSolAuto.getDiagnostico().getAcronimo());
						diagnostico.setDescripcion1(diagSolAuto.getDiagnostico().getNombre());
						break;
					case 1:
						diagnostico.setCodigoCIE102(diagSolAuto.getDiagnostico().getAcronimo());
						diagnostico.setDescripcion2(diagSolAuto.getDiagnostico().getNombre());
						break;
					default:
						break;
					}
		    	}
		    	cont++;
		    }
		    ArrayList<Diagnostico> list = new ArrayList<Diagnostico>();
		    list.add(diagnostico);
			// Fin Diagnostico
			//******************************************************************
		    
		 
			//******************************************************************
		    // Servicios Salud
		    ServicioSalud serSalud = new ServicioSalud();
		    Iterator iterar = dto.getDetalle().iterator();
		    int i = 1 ;
		    StringBuffer justificacion = new StringBuffer(); 
		    DtoDetAutorizacion dtoDetAuto = new DtoDetAutorizacion();
		    while(iterar.hasNext())
		    {
		    	dtoDetAuto = (DtoDetAutorizacion) iterar.next();
		    	switch (i) {
				case 1:
					serSalud.setCodigoCUPS1(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad1(dtoDetAuto.getCantidad());
					serSalud.setDescripcion1(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 2:
					serSalud.setCodigoCUPS2(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad2(dtoDetAuto.getCantidad());
					serSalud.setDescripcion2(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 3:
					serSalud.setCodigoCUPS3(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad3(dtoDetAuto.getCantidad());
					serSalud.setDescripcion3(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 4:
					serSalud.setCodigoCUPS4(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad4(dtoDetAuto.getCantidad());
					serSalud.setDescripcion4(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 5:
					serSalud.setCodigoCUPS5(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad5(dtoDetAuto.getCantidad());
					serSalud.setDescripcion5(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 6:
					serSalud.setCodigoCUPS6(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad6(dtoDetAuto.getCantidad());
					serSalud.setDescripcion6(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 7:
					serSalud.setCodigoCUPS7(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad7(dtoDetAuto.getCantidad());
					serSalud.setDescripcion7(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 8:
					serSalud.setCodigoCUPS8(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad8(dtoDetAuto.getCantidad());
					serSalud.setDescripcion8(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 9:
					serSalud.setCodigoCUPS9(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad9(dtoDetAuto.getCantidad());
					serSalud.setDescripcion9(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 10:
					serSalud.setCodigoCUPS10(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad10(dtoDetAuto.getCantidad());
					serSalud.setDescripcion10(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 11:
					serSalud.setCodigoCUPS11(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad11(dtoDetAuto.getCantidad());
					serSalud.setDescripcion11(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				case 12:
					serSalud.setCodigoCUPS12(dtoDetAuto.getEsServicio()!=3?dtoDetAuto.getCodigoCUPS():"");
					serSalud.setCantidad12(dtoDetAuto.getCantidad());
					serSalud.setDescripcion12(dtoDetAuto.getServicioArticulo().getNombre().equals("")?dtoDetAuto.getServicioCx().getNombre():dtoDetAuto.getServicioArticulo().getNombre());
					break;
				default:
					break;
				}
			    justificacion.append(dtoDetAuto.getJustificacionSolicitud().equals("")?"":i+") "+dtoDetAuto.getJustificacionSolicitud()+".  ");
			    i+=1;
		    }
		    ArrayList<ServicioSalud> listSer = new ArrayList<ServicioSalud>();
		    listSer.add(serSalud);
		    // Fin Servicios Salud
			//******************************************************************
		    
		    informeSolAuto.setCamaHospitalizacion(dto.getCamaHospitalizacion());
		    informeSolAuto.setCoberturaSalud(dto.getNombreCobertura());
		    informeSolAuto.setGuiaManejoIntegral("");
		    informeSolAuto.setJustificacionClinica(justificacion.toString());
		    informeSolAuto.setOrigenAtencion(dto.getNombreAtencio());
		    informeSolAuto.setServicioHospitalizacion(dto.getServicioHospitalizacion());
		    informeSolAuto.setUbicacionPaciente(dto.getCodigoViaIngreso());
		    
		    informeSolAuto.setGeneral(general);
		    informeSolAuto.setImpresionDiagnostica(list);
		    informeSolAuto.setPaciente(pacInformeSolAuto);
		    informeSolAuto.setPagador(pagador);
		    informeSolAuto.setProfesionalSolicitante(profeSalud);
		    informeSolAuto.setServiciosSolicitados(listSer);
			
			UtilidadAutorizacionesEDI.generarArchivoEDISolicitudAutorizacionServicios(path+nombreArchivo+".xml", informeSolAuto);
			
			//****************************************************************************
			// Creacion del archivo .zip y verificacion de lo la existencia de los mismos
			//se genera el archivo en formato Zip
			band=BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombreArchivo+".zip -j"+" "+path+nombreArchivo+".xml");
			//se ingresa la direccion donde se almaceno el archivo
			//forma.getRegistroEnvioInfAtencionIniUrgDao().setPathArchivoIncoXml(path+nombreArchivo+".xml");
			//se valida si existe el xml
			exist=UtilidadFileUpload.existeArchivo(path, nombreArchivo+".xml");
			//se valida si existe el zip
			exist=UtilidadFileUpload.existeArchivo(path, nombreArchivo+".zip");
			// Creacion del archivo .zip y verificacion de lo la existencia de los mismos
			//****************************************************************************
			if(exist)
				return nombreArchivo+".zip";
			else
				return null;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    	return null;
    }
    
    
}
