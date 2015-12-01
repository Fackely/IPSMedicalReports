package com.princetonsa.pdf;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class ConsultaImpresionAutorizacionEntSubPdf {

	private static Logger logger=Logger.getLogger(ConsultaImpresionAutorizacionEntSubPdf.class);
	
	public static String pdfDetalleConsultaImpresionAutorizacionEntSub(UsuarioBasico usuario, HttpServletRequest request,DtoAutorizacionEntSubContratada autorizacion)
	{
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aInfoEst" + r.nextInt()  +".pdf";
    	
    	// se carga los datos de la institucion
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String titulo="AUTORIZACION ENTIDADES SUBCONTRATADAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        String tituloMedicamentos="AUTORIZACION ORDENES MEDICAS";
		report.setReportBaseHeaderIzq(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		report.setUsuario(usuario.getLoginUsuario());
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        iTextBaseTable section;
		
      //**************SECCION DATOS DEL PACIENTE*********************************************************************
        section = report.createSection("paciente", "pacienteTable", 2, 3, 10);
        section.setTableBorder("pacienteTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("pacienteTable", 1.0f);
	    section.setTableCellPadding("pacienteTable", 1);
	    section.setTableSpaceBetweenCells("pacienteTable", 0.5f);
	    section.setTableCellsDefaultColors("pacienteTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Solicitud Autorizacion
	    section.setTableCellsColSpan(3);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("pacienteTable", "DATOS PACIENTE", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("pacienteTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Paciente: "+autorizacion.getNomPaciente(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Tipo y Nro ID: "+autorizacion.getTipoIdPacinte() +" "+autorizacion.getNumIdPaciente() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("pacienteTable", "Tipo Contrato Paciente: "+autorizacion.getNomTipoContrato(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("paciente");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
        
        
	    //**************SECCION DATOS AUTORIZACION*********************************************************************
        section = report.createSection("autorizacion", "autorizacionTable", 2, 5, 10);
        section.setTableBorder("autorizacionTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("autorizacionTable", 1.0f);
	    section.setTableCellPadding("autorizacionTable", 1);
	    section.setTableSpaceBetweenCells("autorizacionTable", 0.5f);
	    section.setTableCellsDefaultColors("autorizacionTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Autorizacion
	    section.setTableCellsColSpan(5);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("autorizacionTable", "DATOS AUTORIZACION", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("autorizacionTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Entidad Autorizacion: "+autorizacion.getNomEntidadAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Direccion Entidad: "+autorizacion.getDirEntidadAutorizada() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Telefono Entidad: "+autorizacion.getTelEntidadAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Nro Autorizacion: "+autorizacion.getConsecutivoAutorizacion() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizacionTable", "Estado Autorizacion: "+ValoresPorDefecto.getIntegridadDominio(autorizacion.getEstado()), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("autorizacion");
	    //**************FIN SECCION DATOS DE LA AUTORIZACION*********************************************************************
        
	    
	    //**************SECCION2 (FECHAS) DATOS AUTORIZACION*********************************************************************
        section = report.createSection("autorizaciondos", "autorizaciondosTable", 2, 2, 10);
        section.setTableBorder("autorizaciondosTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("autorizaciondosTable", 1.0f);
	    section.setTableCellPadding("autorizaciondosTable", 1);
	    section.setTableSpaceBetweenCells("autorizaciondosTable", 0.5f);
	    section.setTableCellsDefaultColors("autorizaciondosTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Autorizacion
	
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("autorizaciondosTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Fecha Autorizacion: "+autorizacion.getFechaAutorizacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Fecha Vencimiento: "+autorizacion.getFechaVencimiento() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Entidad que Autoriza: "+autorizacion.getNomInstitucion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("autorizaciondosTable", "Usuario que Autoriza: "+autorizacion.getUsuarioModificacion() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("autorizaciondos");
	    //**************FIN SECCION2 DATOS DE LA AUTORIZACION*********************************************************************
        
	    
	    //**************SECCION DATOS DE LA ORDEN  ********************************************************************************
        section = report.createSection("orden", "ordenTable", 3, 5, 10);
        section.setTableBorder("ordenTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("ordenTable", 1.0f);
	    section.setTableCellPadding("ordenTable", 1);
	    section.setTableSpaceBetweenCells("ordenTable", 0.5f);
	    section.setTableCellsDefaultColors("ordenTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Orden Autorizacion
	    section.setTableCellsColSpan(5);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("ordenTable", "DATOS ORDEN", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("ordenTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", "ORDEN ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", "CODIGO " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", "SERVICIO ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", "CANTIDAD " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", "NIVEL " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", autorizacion.getNumeroSolicitud(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", autorizacion.getCodServicio()+"" , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", autorizacion.getNomServicio(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable",  autorizacion.getCantidad()+"" , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("ordenTable", autorizacion.getNivel() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    // se añade la seccion al documento    
	    report.addSectionToDocument("orden");
	    //**************FIN SECCION DATOS DE LA ORDEN *********************************************************************
        
	    
	    //**************SECCION OBSERVACIONES AUTORIZACION*********************************************************************
        section = report.createSection("observaciones", "observacionesTable", 2, 1, 10);
        section.setTableBorder("observacionesTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("observacionesTable", 1.0f);
	    section.setTableCellPadding("observacionesTable", 1);
	    section.setTableSpaceBetweenCells("observacionesTable", 0.5f);
	    section.setTableCellsDefaultColors("observacionesTable", 0xFFFFFF, 0x000000);
	    
	    // Observaciones
	    section.setTableCellsColSpan(1);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("observacionesTable", "OBSERVACIONES AUTORIZACION", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("observacionesTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("observacionesTable", autorizacion.getObservaciones(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	   
	    // se añade la seccion al documento    
	    report.addSectionToDocument("observaciones");
	    //**************FIN SECCION OBSERVACIONES AUTORIZACION*********************************************************************
	    
	    if(autorizacion.getArrayProrrogas().size()>0)
	    {
	    	//**************SECCION PRORROGAS AUTORIZACION*********************************************************************
	        section = report.createSection("prorroga", "prorrogaTable", 2, 5, 10);
	        section.setTableBorder("prorrogaTable", 0x000000, 1.0f);
		    section.setTableCellBorderWidth("prorrogaTable", 1.0f);
		    section.setTableCellPadding("prorrogaTable", 1);
		    section.setTableSpaceBetweenCells("prorrogaTable", 0.5f);
		    section.setTableCellsDefaultColors("prorrogaTable", 0xFFFFFF, 0x000000);
		    
		    section.setTableCellsColSpan(5);
		    report.font.setFontAttributes(0x000000, 12, true, false, false);
		    section.addTableTextCellAligned("prorrogaTable", "DATOS PRORROGAS AUTORIZACION", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    section.setTableCellBorderWidth("prorrogaTable", 0.5f);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("prorrogaTable","FECHA VTO INI ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("prorrogaTable","FECHA VTO NUEVA " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("prorrogaTable","FECHA PRORROGA " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("prorrogaTable","HORA PRORROGA " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("prorrogaTable","USUARIO PRORROGA ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    // DATOS PRORROGA
		    for(int i=0;i<autorizacion.getArrayProrrogas().size();i++)
		    {
		    	report.font.setFontAttributes(0x000000, 8, false, false, false);
			    section.setTableCellBorderWidth("prorrogaTable", 0.5f);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("prorrogaTable",""+ autorizacion.getArrayProrrogas().get(i).getFechaVencimientoInicial(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("prorrogaTable","" +autorizacion.getArrayProrrogas().get(i).getFechaVencimientoNueva(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("prorrogaTable","" +autorizacion.getArrayProrrogas().get(i).getFechaProrroga(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("prorrogaTable","" +autorizacion.getArrayProrrogas().get(i).getHoraProrroga(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
			    section.setTableCellsColSpan(1);
			    section.addTableTextCellAligned("prorrogaTable",""+autorizacion.getArrayProrrogas().get(i).getUsuarioProrroga(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		   
		    }
		    // se añade la seccion al documento    
		    report.addSectionToDocument("prorroga"); 
	    }
	    
	    
	    
	    if(autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
	    {
	    	
	    	//**************SECCION ANULACION AUTORIZACION*********************************************************************
	        section = report.createSection("anulacion", "anulacionTable", 2, 3, 10);
	        section.setTableBorder("anulacionTable", 0x000000, 1.0f);
		    section.setTableCellBorderWidth("anulacionTable", 1.0f);
		    section.setTableCellPadding("anulacionTable", 1);
		    section.setTableSpaceBetweenCells("anulacionTable", 0.5f);
		    section.setTableCellsDefaultColors("anulacionTable", 0xFFFFFF, 0x000000);
		    
		    
		    // DATOS ANULACION
		    section.setTableCellsColSpan(3);
		    report.font.setFontAttributes(0x000000, 12, true, false, false);
		    section.addTableTextCellAligned("anulacionTable", "DATOS ANULACION AUTORIZACION", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    report.font.setFontAttributes(0x000000, 8, false, false, false);
		    section.setTableCellBorderWidth("anulacionTable", 0.5f);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("anulacionTable","Fecha Anulación: "+ autorizacion.getFechaAnulacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("anulacionTable","Motivo Anulación: " +autorizacion.getMotivoAnulacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("anulacionTable","Usuario Anulación: "+autorizacion.getUsuarioAnulacion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    
		    // se añade la seccion al documento    
		    report.addSectionToDocument("anulacion");
		    
		    //**************FIN SECCION AULACION AUTORIZACION AUTORIZACION*********************************************************************
		   
	    	
	    }
	    
	    
	    
	    report.closeReport();
	    return nombreArchivo;
	}
		
	
}
