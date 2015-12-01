package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class ListaConsultaAutorizacionEntSubPdf {
	
	private static Logger logger=Logger.getLogger(ListaConsultaAutorizacionEntSubPdf.class);
	
	public static String pdfListadoConsultaAutorizacionEntSub(UsuarioBasico usuario, HttpServletRequest request,ArrayList<DtoAutorizacionEntSubContratada> autorizaciones)
	{
		
		//se edita nombre del archivo PDF
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
        String titulo="CONSULTA AUTORIZACIONES DE ORDENES MEDICAS EN ENTIDADES SUBCONTRATADAS" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        String tituloAutorizacion="AUTORIZACION ORDENES MEDICAS";
		report.setReportBaseHeaderIzq(institucionBasica, institucionBasica.getNit(), tituloAutorizacion);
		report.setUsuario(usuario.getLoginUsuario());
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        
        iTextBaseTable section;
		
      //**************SECCION DATOS BUSQUEDA*********************************************************************
        section = report.createSection("resultado", "resultadoTable",autorizaciones.size()+2 , 10, 10);
        section.setTableBorder("resultadoTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("resultadoTable", 1.0f);
	    section.setTableCellPadding("resultadoTable", 1);
	    section.setTableSpaceBetweenCells("resultadoTable", 0.1f);
	    section.setTableCellsDefaultColors("resultadoTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Solicitud Autorizacion
	    section.setTableCellsColSpan(10);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("resultadoTable", "RESULTADO CONSULTA", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("resultadoTable", 0.5f);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "AUTORIZACION ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "FECHA AUT " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "ENTIDAD AUTORIZADA ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "ESTADO ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "FECHA VTO " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "ORDEN ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "PACIENTE ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "ID " , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "CONVENIO ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("resultadoTable", "INGRESO ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    
	    for(int i=0; i < autorizaciones.size();i++)
	    {
	    	DtoAutorizacionEntSubContratada autorizacion = (DtoAutorizacionEntSubContratada)autorizaciones.get(i);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getConsecutivoAutorizacion(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getFechaAutorizacion() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getNomEntidadAutorizada(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", ValoresPorDefecto.getIntegridadDominio(autorizacion.getEstado())+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getFechaVencimiento() , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getNumeroSolicitud(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getNomPaciente(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getTipoIdPacinte() +" "+autorizacion.getNumIdPaciente() , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getNomConvenio(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	  	    section.setTableCellsColSpan(1);
	  	    section.addTableTextCellAligned("resultadoTable", autorizacion.getIngreso(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	  	    
	    }
	    // se añade la seccion al documento    
	    report.addSectionToDocument("resultado");
	    //**************FIN SECCION DATOS DEL PACIENTE*********************************************************************
       		    
	    
	    report.closeReport();
	    return nombreArchivo;
	}
	
	
}
