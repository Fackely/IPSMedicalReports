package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.sql.Connection;

import org.apache.log4j.Logger;
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
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;
import util.xml.UtilidadAutorizacionesEDI;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInfAtencionIniUrgForm;
import com.princetonsa.actionform.manejoPaciente.RegistroEnvioInformInconsisenBDForm;
import com.princetonsa.actionform.salasCirugia.ConsultaLiquidacionQxForm;
import com.princetonsa.actionform.salasCirugia.LiquidacionServiciosForm;
import com.princetonsa.autorizaciones.informeUrgencias.Diagnostico;
import com.princetonsa.autorizaciones.informeUrgencias.EstructuraInformeUrgencias;
import com.princetonsa.autorizaciones.informeUrgencias.General;
import com.princetonsa.autorizaciones.informeUrgencias.Identificacion;
import com.princetonsa.autorizaciones.informeUrgencias.InformacionPersonal;
import com.princetonsa.autorizaciones.informeUrgencias.Informante;
import com.princetonsa.autorizaciones.informeUrgencias.NombreCompleto;
import com.princetonsa.autorizaciones.informeUrgencias.Paciente;
import com.princetonsa.autorizaciones.informeUrgencias.Pagador;
import com.princetonsa.autorizaciones.informeUrgencias.PrestadorSSalud;
import com.princetonsa.autorizaciones.informeUrgencias.UbicacionGeografica;
import com.princetonsa.dto.manejoPaciente.DtoDiagInfoIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInformInconsisenBD;

public class AtencionInicialUrgenciasPdf 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(AtencionInicialUrgenciasPdf.class);
	
	
	/**
     * Método para realizar la impresión del informe inicial de urgencias
     * @param usuario
     * @param request
     * @param paciente
     * @param cod_informe
     */
    public static String pdfAtencionInicialUrgencia(UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, int cod_informe)
    {
    	//se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	
    	// se cargan los datos requeridos para el informe 
    	DtoInformeAtencionIniUrg dtoAtenIniUrg = RegistroEnvioInfAtencionIniUrg.getInformeInicUrge(UtilidadBD.abrirConexion(), cod_informe);
    	
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="ATENCION INICIAL URGENCIAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        HashMap encabezado = new HashMap<String,Object>();
        encabezado.put("entidad_requiere", "MINISTERIO DE LA PROTECCION SOCIAL");
        encabezado.put("titulo", "INFORME DE LA ATENCION INICIAL DE URGENCIAS");
        encabezado.put("numero_atencion", "Número Atención: "+dtoAtenIniUrg.getConsecutivoIngreso());
        encabezado.put("fecha", dtoAtenIniUrg.getFechaGeneracion());
        encabezado.put("hora", dtoAtenIniUrg.getHoraGeneracion());
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
	    
	    Iterator iter = dtoAtenIniUrg.getConveniosPaciente().iterator();
	    while(iter.hasNext())
	    {
	    	InfoDatosString datos = (InfoDatosString) iter.next();
	    	section.setTableCellsColSpan(2);
	    	section.addTableTextCellAligned("prestadorTable", "Entidad a al que se le Informa (Pagador): "+datos.getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("prestadorTable", "Código: "+datos.getCodigo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }
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
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("pacienteTable", "Cobertura en Salud: "+dtoAtenIniUrg.getCoberturaSalud().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
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
	    section.addTableTextCellAligned("atencionTable", "Información de la Atención", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("atencionTable", 0.5f);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("atencionTable", "Origen de la Atención: "+dtoAtenIniUrg.getCausaExterna().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Calasificación Triage: "+dtoAtenIniUrg.getColorTriage().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Ingreso a Urgencias", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Fecha: "+dtoAtenIniUrg.getFechaIngUrgencias(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Hora: "+dtoAtenIniUrg.getHoraIngUrgencias(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    String vieneremitido = "";
	    if(!dtoAtenIniUrg.getPacVieneRemitido().equals("N") && !dtoAtenIniUrg.getPacVieneRemitido().equals(""))
	    	vieneremitido = "Si";
	    else
	    	vieneremitido = "No";
	    section.addTableTextCellAligned("atencionTable", "Paciente Viene Remitido: "+vieneremitido, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    if(dtoAtenIniUrg.getInstitucionRemite().size()>0)
	    {
	    	section.setTableCellsColSpan(3);
		    section.addTableTextCellAligned("atencionTable", "Nombre del Prestadorde Servicios de Salud que Remite: "+dtoAtenIniUrg.getInstitucionRemite().get("razon_social").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("atencionTable", "Código: "+dtoAtenIniUrg.getInstitucionRemite().get("cod_min_salud").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", "Departamento: "+dtoAtenIniUrg.getInstitucionRemite().get("departamento").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", "Municipio: "+dtoAtenIniUrg.getInstitucionRemite().get("ciudad").toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }else{
		    section.setTableCellsColSpan(3);
		    section.addTableTextCellAligned("atencionTable", "Nombre del Prestadorde Servicios de Salud que Remite: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("atencionTable", "Código: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", "Departamento: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", "Municipio: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("atencionTable", "Motivo Consulta: "+dtoAtenIniUrg.getMotivoConsulta(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Impresión Diagnóstica: ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("atencionTable", "Código CIE10 ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("atencionTable", "Descripción ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    iter = dtoAtenIniUrg.getDiagInfIniUrg().iterator();
	    DtoDiagInfoIniUrg diagInfoIniUrg = new DtoDiagInfoIniUrg();
	    for(int conta=0;conta<4;conta++) 
	    {
	    	if(iter.hasNext())
	    		diagInfoIniUrg = (DtoDiagInfoIniUrg) iter.next();
	    	else
	    		diagInfoIniUrg.clean();
	    	
	    	if(diagInfoIniUrg.getDiagnostico().isPrincipal()){
	    		section.setTableCellsColSpan(1);
		    	section.addTableTextCellAligned("atencionTable", "Diagnóstico Principal ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}else{
	    		section.setTableCellsColSpan(1);
		    	section.addTableTextCellAligned("atencionTable", "Diagnóstico Relacionado "+conta, report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    	}
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("atencionTable", diagInfoIniUrg.getDiagnostico().getAcronimo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(2);
		    section.addTableTextCellAligned("atencionTable", diagInfoIniUrg.getDiagnostico().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("atencionTable", "Destino del Paciente: "+dtoAtenIniUrg.getDestinoPaciente().getNombre(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	        
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
	    section.addTableTextCellAligned("persinfoTable", "Información de la Persona que Informa", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("persinfoTable", 0.5f);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("persinfoTable", "Apellidos y Nombres: "+dtoAtenIniUrg.getNombreUsuarioGenera(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("persinfoTable", "Teléfono: "+indicativo+institucionBasica.getTelefono()+extension, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("persinfoTable", "Cargo: "+dtoAtenIniUrg.getCargo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    section.addTableTextCellAligned("persinfoTable", "Teléfono Celular: "+dtoAtenIniUrg.getTelefonoCelular(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("persinfo");
	    //**************FIN SECCION INFORMACIÓN DE LA PERSONA QUE INFORMA*********************************************************************
	    
	    
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
    public static String xmlInformeAtenIniUrg(Connection con, 
    		UsuarioBasico usuario, 
    		HttpServletRequest request, 
    		PersonaBasica paciente, int cod_informe, 
    		RegistroEnvioInfAtencionIniUrgForm forma)
    {
//    	logger.info("Medio de Envio: "+forma.getInformeIconsistencias().getMedioEnvio());
//    	if(forma.getInformeIconsistencias().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
//		{
    		int band=ConstantesBD.codigoNuncaValido;
    		boolean exist = false;
		  	//se edita nombre del archivo 
			String nombreArchivo = "";
			String path = "";
			String url = "";
	    	Random r=new Random();
	    	nombreArchivo="AteIniUrgXML"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
	    	path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
	    	url="../upload"+System.getProperty("file.separator");
	    	// se cargan los datos requeridos para el informe
	    	DtoInformeAtencionIniUrg dto = RegistroEnvioInfAtencionIniUrg.getInformeInicUrge(UtilidadBD.abrirConexion(), cod_informe);
	    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		    EstructuraInformeUrgencias informeAtenIniUrg = new EstructuraInformeUrgencias();
			
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
				ubicacionGeo.setDireccionResidencia(paciente.getDireccion());
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
				infoPersonal.setFechaNacimiento(fecha);
				infoPersonal.setUbicacion(ubicacionGeo);
				
				Paciente pacInformeUrg = new Paciente();
				pacInformeUrg.setDatosPersonales(infoPersonal);
				pacInformeUrg.setIdentificacion(identificacion);
				pacInformeUrg.setNombre(nomcompleto);
				// Fin Paciente
				//******************************************************************
				
				//******************************************************************
				// Pagador
				Pagador pagador = new Pagador();
				//Iterator iter = dto.getConveniosPaciente().iterator();
				//iter.hasNext();InfoDatosString datos = (InfoDatosString) iter.next();
				Iterator iter = dto.getConveniosPaciente().iterator();
				while(iter.hasNext())
				{
				  	InfoDatosString datos = (InfoDatosString) iter.next();
				  	pagador.setCodigoEntidad(datos.getCodigo());
				  	pagador.setEntidadResponsable(datos.getNombre());
				}
				// Fin Pagador
				//******************************************************************
				
				//******************************************************************
				// General
				
				General general = new General();
				general.setNumero(Utilidades.convertirAEntero(dto.getConsecutivoIngreso()));
				
				XMLGregorianCalendar fechaGeneral;
				fechaGeneral = DatatypeFactory.newInstance().newXMLGregorianCalendar();
				String fechaAux[] = UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaGeneracion()).split("/");
				fechaGeneral.setDay(Utilidades.convertirAEntero(fechaAux[0]));
				fechaGeneral.setMonth(Utilidades.convertirAEntero(fechaAux[1]));
				fechaGeneral.setYear(Utilidades.convertirAEntero(fechaAux[2]));
				general.setFecha(fechaGeneral);
				
				XMLGregorianCalendar horaGeneral;
				horaGeneral = DatatypeFactory.newInstance().newXMLGregorianCalendar();
				String horaAux[] = dto.getHoraGeneracion().split(":");
				horaGeneral.setTime(Utilidades.convertirAEntero(horaAux[0]), Utilidades.convertirAEntero(horaAux[1]), 0);
				general.setHora(horaGeneral);
				
				general.setPrestador(institucionBasica.getRazonSocial());
				if(institucionBasica.getTipoIdentificacion().equals("NI")){
					general.setTipoIdPrestador(institucionBasica.getDescripcionTipoIdentificacion());
					general.setIdPrestador(institucionBasica.getNit());
					general.setDigVerif(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()));
				}else{
					general.setTipoIdPrestador(institucionBasica.getTipoIdentificacion());
					general.setIdPrestador(institucionBasica.getNit());
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
				// Informate
      			Informante informate = new Informante();
      			informate.setNombre(dto.getNombreUsuarioGenera());
      			informate.setCargo(dto.getCargo());
      			informate.setCelularInstitucional(dto.getTelefonoCelular());
      			try{
      				informate.setTelefono(Utilidades.convertirAEntero(institucionBasica.getTelefono()));
					try{
						informate.setExtTele(Utilidades.convertirAEntero(institucionBasica.getExtension()));
					}catch (Exception e) {
						informate.setExtTele(0);
					}
					try{
						informate.setIndicaTel(Utilidades.convertirAEntero(institucionBasica.getIndicativo()));
					}catch (Exception e) {
						informate.setIndicaTel(0);
					}
				}catch (Exception e) {
					informate.setTelefono(0);
					informate.setExtTele(0);
					informate.setIndicaTel(0);
				}
				// Fin Informate
				//******************************************************************
      			
				
				//******************************************************************
				// Prestador Salud
				PrestadorSSalud prestadoSalud = new PrestadorSSalud();
				prestadoSalud.setDepartamentoPR(institucionBasica.getDepto()+" ("+institucionBasica.getCodigoDepto()+")");
				prestadoSalud.setMuncipioPR(institucionBasica.getCodigo()+" ("+institucionBasica.getCodigoCiudad()+")");
				prestadoSalud.setNombrePrestador(institucionBasica.getRazonSocial());
				// Fin Prestador Salud
				//******************************************************************
				
				//******************************************************************
				// Diagnostico
				Diagnostico diagnostico = new Diagnostico();
				int cont = 0 ;
				Iterator ite = dto.getDiagInfIniUrg().iterator();
				DtoDiagInfoIniUrg diagInfoIniUrg = new DtoDiagInfoIniUrg();
			    while(ite.hasNext()){
			    	diagInfoIniUrg = (DtoDiagInfoIniUrg) ite.next();
			    	if(diagInfoIniUrg.getDiagnostico().isPrincipal()){
			    		diagnostico.setCodigoCIE10Principal(diagInfoIniUrg.getDiagnostico().getAcronimo());
			    		diagnostico.setDescripcionPrincipal(diagInfoIniUrg.getDiagnostico().getNombre());
			    	}else{
			    		switch (cont) {
						case 0:
							diagnostico.setCodigoCIE101(diagInfoIniUrg.getDiagnostico().getAcronimo());
							diagnostico.setDescripcion1(diagInfoIniUrg.getDiagnostico().getNombre());
							break;
						case 1:
							diagnostico.setCodigoCIE102(diagInfoIniUrg.getDiagnostico().getAcronimo());
							diagnostico.setDescripcion2(diagInfoIniUrg.getDiagnostico().getNombre());
							break;
						case 2:
							diagnostico.setCodigoCIE103(diagInfoIniUrg.getDiagnostico().getAcronimo());
							diagnostico.setDescripcion3(diagInfoIniUrg.getDiagnostico().getNombre());
							break;
						case 3:
							diagnostico.setCodigoCIE104(diagInfoIniUrg.getDiagnostico().getAcronimo());
							diagnostico.setDescripcion4(diagInfoIniUrg.getDiagnostico().getNombre());
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
				
				
				
				informeAtenIniUrg.setCoberturaSalud(dto.getCoberturaSalud().getNombre());
				informeAtenIniUrg.setDestinoPaciente(dto.getDestinoPaciente().getCodigo()); 

				XMLGregorianCalendar fechaInforUrgIngr;
				fechaInforUrgIngr = DatatypeFactory.newInstance().newXMLGregorianCalendar();
				String fechaAux1[] = UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaIngreso()).split("/");
				for(String elem: fechaAux1)
					logger.info("elem: "+elem);
				if(!dto.getFechaIngreso().equals(""))
				{
					fechaInforUrgIngr.setDay(Utilidades.convertirAEntero(fechaAux1[0]));
					fechaInforUrgIngr.setMonth(Utilidades.convertirAEntero(fechaAux1[1]));
					fechaInforUrgIngr.setYear(Utilidades.convertirAEntero(fechaAux1[2]));
					informeAtenIniUrg.setFechaIngreso(fechaInforUrgIngr);
				}
				
				informeAtenIniUrg.setMotivoConsulta(dto.getMotivoConsulta());
				informeAtenIniUrg.setOrigenAtencion(dto.getCausaExterna().getNombre());
				informeAtenIniUrg.setPacienteRemitido(dto.getPacVieneRemitido().equals(ConstantesBD.acronimoSi)?true:false);
      			informeAtenIniUrg.setGeneral(general);
      			informeAtenIniUrg.setInformante(informate);
      			informeAtenIniUrg.setPaciente(pacInformeUrg);
      			informeAtenIniUrg.setPagador(pagador);
      			informeAtenIniUrg.setPrestadorRemite(prestadoSalud);
      			informeAtenIniUrg.setImpresionDiagnostica(list);
				
				UtilidadAutorizacionesEDI.generarArchivoEDIInformeUrgencias(path+nombreArchivo+".xml", informeAtenIniUrg);
				
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
		    
		//}
    	return null;
    }
    
}
