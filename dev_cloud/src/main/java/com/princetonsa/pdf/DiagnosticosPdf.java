/*
 * @(#)DiagnosticosPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.parametrizacion.DiagnosticosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la generación de PDF's para
 * la consulta de diagnósticos
 * 
 *	@version 1.0, 23/08/2004
 */
public class DiagnosticosPdf
{
    /**
     * Clase para manejar los logs de esta clase
     */
    private static Logger diagnosticosPdfLogger=Logger.getLogger(DiagnosticosPdf.class);
    
    /**
     * Método que imprime una colección de diagnosticos
     * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param diagnosticosForm Form del que se sacan
	 * las colecciones
     * @param medico Médico que está generando el PDF
     */
	public  static void pdfDiagnosticos(String filename, DiagnosticosForm diagnosticosForm, UsuarioBasico medico, HttpServletRequest request) 
	{
		PdfReports report = new PdfReports();
		String[] reportData;
		String[] reportHeader = {
			"Código", 
			"Tipo CIE", 
			"Descripción", 
			"Activo", 
			"Sexo", 
			"Edad Inicial", 
			"Edad Final", 
			"Es Principal", 
			"Es Muerte"
		};
		      
		String[] dbColumns =  {
			"codigo", 
			"nombretipocie", 
			"descripcion", 
			"estado",
			"nomhsexo",
			"edad_inicial",
			"edad_final",
			"es_principal",
			"es_muerte"
		};
		
		reportData = report.getDataFromCollection(dbColumns, diagnosticosForm.getColeccion());
		if(reportData.length == 0)
		{
		    return;
		}	
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String tituloDiagnosticos="CONSULTA DE DIAGNÓSTICOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccionDiagnosticos", "Diagnósticos", reportHeader, reportData, 10);

		float widths[]={(float) 3,(float) 2,(float) 7,(float) 2,(float) 4,(float) 2,(float) 2,(float) 3,(float) 2};
		try
        {
		    report.getSectionReference("seccionDiagnosticos").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            diagnosticosPdfLogger.error("Se presentó error generando el pdf " + e);
        }
		

		report.addSectionToDocument("seccionDiagnosticos");

		report.closeReport(); 
	}

}
