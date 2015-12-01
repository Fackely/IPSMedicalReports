package com.princetonsa.pdf;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.princetonsa.actionform.carteraPaciente.ConsultarRefinanciarCuotaPacienteForm;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class FormatoGarantiaLetraPdf {

	private static Logger logger=Logger.getLogger(FormatoGarantiaLetraPdf.class);
	
	
	public static String pdfFormatoImpresionGarantiaLetra(UsuarioBasico usuario, HttpServletRequest request,ConsultarRefinanciarCuotaPacienteForm forma)
	{
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aInfoEst" + r.nextInt()  +".pdf";
    	
    	// se carga los datos de la institucion
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	// se cargan los datos requeridos para el informe 
    	DtoIngresosFacturasAtenMedica dto = new DtoIngresosFacturasAtenMedica();
    	dto = (DtoIngresosFacturasAtenMedica)forma.getListaIngresos().get(forma.getPosIngreso());
        
    	
    	logger.info("nombre del archivo: >>>>>>>>>>>>>>>>>"+nombreArchivo);
    	logger.info("\narchivo: >>>>>>>>>>>>>>>>>"+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
    	String filePath=ValoresPorDefecto.getFilePath();
        String titulo="IMPRESION GARANTIA DE LETRA" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
        
        String tituloImpresion="GARANTIA DE LETRA";
		report.setReportBaseHeaderIzq(institucionBasica, institucionBasica.getNit(), tituloImpresion);
		report.setUsuario(usuario.getLoginUsuario());
        
        //Se abre el reporte
        report.openReport(ValoresPorDefecto.getFilePath()+nombreArchivo);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
		
        iTextBaseTable section;
        
        
        
        
      //**************SECCION DATOS GENERALES*********************************************************************
        section = report.createSection("garantia", "garantiaTable", 2, 4, 10);
        section.setTableBorder("garantiaTable", 0x000000, 1.0f);
	    section.setTableCellBorderWidth("garantiaTable", 1.0f);
	    section.setTableCellPadding("garantiaTable", 1);
	    section.setTableSpaceBetweenCells("garantiaTable", 0.5f);
	    section.setTableCellsDefaultColors("garantiaTable", 0xFFFFFF, 0x000000);
	    
	    // Datos Garantia Letra
	    section.setTableCellsColSpan(4);
	    report.font.setFontAttributes(0x000000, 12, true, false, false);
	    section.addTableTextCellAligned("garantiaTable", "DATOS GARANTIA DE LETRA", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellBorderWidth("garantiaTable", 0.5f);
	    section.setTableCellsColSpan(4);
	    section.addTableTextCellAligned("garantiaTable", "Código Garantía: "+dto.getCodGarantia(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", "Fecha ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("garantiaTable", UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFactura())+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);    
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", "Deudor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("garantiaTable", dto.getDeudor().getPrimerApellido()+" "+dto.getDeudor().getSegundoApellido()+" "+dto.getDeudor().getPrimerNombre() +" "+dto.getDeudor().getSegundoNombre()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", "Tipo Identificación Deudor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", dto.getDeudor().getNombreTipoIdentificacion()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", "Nro Identificación Deudor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", dto.getDeudor().getNumeroIdentificacion()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    if(dto.getCoDeudor().getExiteDeudor().equals(ConstantesBD.acronimoSi))
        {
	    	section.setTableCellsColSpan(1);
	    	section.addTableTextCellAligned("garantiaTable", "Codeudor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(3);
		    section.addTableTextCellAligned("garantiaTable", dto.getCoDeudor().getPrimerApellido()+" "+dto.getCoDeudor().getSegundoApellido()+" "+dto.getCoDeudor().getPrimerNombre() +" "+dto.getCoDeudor().getSegundoNombre()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("garantiaTable", "Tipo Identificación Deudor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("garantiaTable", dto.getCoDeudor().getNombreTipoIdentificacion()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("garantiaTable", "Nro Identificación Deudor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.setTableCellsColSpan(1);
		    section.addTableTextCellAligned("garantiaTable", dto.getCoDeudor().getNumeroIdentificacion()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    	
	    }
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("garantiaTable", "Valor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(3);
	    section.addTableTextCellAligned("garantiaTable", dto.getValorDocGarantia()+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	   
	    // se añade la seccion al documento    
	    report.addSectionToDocument("garantia");
	    //**************FIN SECCION DATOS GENERALES*********************************************************************
      
	    report.closeReport();
	    return nombreArchivo;
	}
	
}
