/*
 * Creado en 18/12/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.princetonsa.pdf;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Si.Es.
 */
public class ReporteNovedadesPdf
{

	/**
     * Metodo para manejar los logs de esta clase
     */
   private static Logger observacionesPdfLogger=Logger.getLogger(ReporteNovedadesPdf.class);
   
   public  static void generarReporteTodasEnfermeras(String filename, UsuarioBasico medico, Collection novedades, String fechaInicio, String fechaFin, HttpServletRequest request)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
			"Nombre Enfermera", 
			"Fecha", 
			"Novedad",
			"Prioridad"
		};
		      
		String[] dbColumns =  {
	      		"nombreenfermera",
				"fechaprogramacion",
				"nombrenovedad", 
				"prioridad"
				};
		
		
		
		reportData = report.getDataFromCollection(dbColumns, novedades);
				
		if(reportData.length == 0)
		{
	      	return;
	    }
		
		for(int i=0; i<reportData.length; i++) // Se actualiza la prioridad
		{
			if(i%4==3)
			{
				if(reportData[i].trim().equals("true"))
					reportData[i]="Alta";
				else
					reportData[i]="Baja";
			}
			else if(i%4==1)
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
		}


		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDiagnosticos="REPORTE DE NOVEDADES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n";
		
		if(UtilidadCadena.noEsVacio(fechaInicio) && UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="PERIODO: "+fechaInicio+ " --  "+fechaFin+"\n";
		else if(UtilidadCadena.noEsVacio(fechaInicio))
			tituloDiagnosticos+="NOVEDADES DESDE: "+fechaInicio+"\n";
		else if(UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="NOVEDADES ANTES DE: "+fechaFin+"\n";
		else
			tituloDiagnosticos+="TODAS LAS NOVEDADES\n";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Novedades", reportHeader, reportData, 10);
		
		float widths[]={(float) 4.5,(float) 2,(float) 2.1, (float) 1.4};
		try
        {
		    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            observacionesPdfLogger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }

   public  static void generarReporteEnfermera(
		   String filename, UsuarioBasico medico, Collection novedades, int codigoProfesional, String nombreProfesional, String fechaInicio, String fechaFin, HttpServletRequest request)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
			"Fecha", 
			"Novedad",
			"Prioridad"
		};
		      
		String[] dbColumns =  {
				"fechaprogramacion",
				"nombrenovedad", 
				"prioridad"
				};
		
		reportData = report.getDataFromCollection(dbColumns, novedades);
				
		if(reportData.length == 0)
		{
	      	return;
	    }
		
		for(int i=0; i<reportData.length; i++) // Se actualiza la prioridad
		{
			if(i%3==2)
			{
				if(reportData[i].trim().equals("true"))
					reportData[i]="Alta";
				else
					reportData[i]="Baja";
			}
			else if(i%3==0)
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
		}

		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDiagnosticos="REPORTE DE NOVEDADES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n";
		
		if(UtilidadCadena.noEsVacio(fechaInicio) && UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="PERIODO: "+fechaInicio+ " --  "+fechaFin+"\n";
		else if(UtilidadCadena.noEsVacio(fechaInicio))
			tituloDiagnosticos+="NOVEDADES DESDE: "+fechaInicio+"\n";
		else if(UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="NOVEDADES ANTES DE: "+fechaFin+"\n";
		else
			tituloDiagnosticos+="TODAS LAS NOVEDADES\n";
		
		if(codigoProfesional!=-1)
			tituloDiagnosticos+="PROFESIONAL: "+codigoProfesional+" - "+nombreProfesional;
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Novedades", reportHeader, reportData, 10);
		
		float widths[]={(float) 4,(float) 4, (float) 2};
		try
        {
		    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            observacionesPdfLogger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }
	
}
