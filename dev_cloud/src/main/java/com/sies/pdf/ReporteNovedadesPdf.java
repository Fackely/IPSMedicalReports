/*
 * Creado en 18/12/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.sies.pdf;

import java.util.Collection;

import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * SiEs.
 */
public class ReporteNovedadesPdf
{

	/**
     * Método para manejar los logs de esta clase
     */
   private static Logger observacionesPdfLogger=Logger.getLogger(ReporteNovedadesPdf.class);
   
   public  static void generarReporteTodasEnfermeras(String filename, UsuarioBasico medico, Collection novedades, String fechaInicio, String fechaFin)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
			"Nombre Enfermera",
			"Fecha Inicio",
			"Fecha Fin",
			"Novedad",
			"Prioridad",
			"Costo"
		};
		      
		String[] dbColumns =  {
	      		"nombreenfermera",
				"fechainicio",
				"fechafin",
				"nombrenovedad",
				"prioridad",
				"costo"
				};
		
		reportData = report.getDataFromCollection(dbColumns, novedades);
				
		if(reportData.length == 0)
		{
	      	return;
	    }
		
		for(int i=0; i<reportData.length; i++) // Se actualiza la prioridad
		{
			if(i%6==4)
			{
				if(reportData[i].trim().equals("true"))
					reportData[i]="Alta";
				else
					reportData[i]="Baja";
			}
			else if(i%6==2)
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
		}


		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDiagnosticos="REPORTE DE NOVEDADES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n \n";
		
		if(UtilidadCadena.noEsVacio(fechaInicio) && UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="PERIODO: "+fechaInicio+ " --  "+fechaFin+"\n";
		else if(UtilidadCadena.noEsVacio(fechaInicio))
			tituloDiagnosticos+="NOVEDADES DESDE: "+fechaInicio+"\n \n";
		else if(UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="NOVEDADES ANTES DE: "+fechaFin+"\n \n";
		else
			tituloDiagnosticos+="TODAS LAS NOVEDADES\n";
		
		if(System.getProperty("file.separator").equals("/"))
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"/imagenes/logoCP.png", medico.getInstitucion(), medico.getNit(), tituloDiagnosticos);
		}
		else
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"\\imagenes\\logoCP.png", medico.getInstitucion(), medico.getNit(), tituloDiagnosticos);
		}
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
				
		report.createColSection("seccion0", "Novedades", reportHeader, reportData, 10);
		
		float widths[]={(float)2.3, (float) 1.6,(float) 1.3,(float) 1.8, (float) 0.9, (float) 1.2};
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
		   String filename, UsuarioBasico medico, Collection novedades, int codigoProfesional, String nombreProfesional, String fechaInicio, String fechaFin, String fechaProgramacion)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
			"Fecha Inicio",
			"Fecha Fin",
			"Novedad",
			"Prioridad",
			"Costo"
		};
		      
		String[] dbColumns =  {
				"fechainicio",
				"fechafin",
				"nombrenovedad", 
				"prioridad",
				"costo"
				};
		
		reportData = report.getDataFromCollection(dbColumns, novedades);
				
		if(reportData.length == 0)
		{
	      	return;
	    }
		
		for(int i=0; i<reportData.length; i++) // Se actualiza la prioridad
		{
			if(i%5==3)
			{
				if(reportData[i].trim().equals("true"))
					reportData[i]="Alta";
				else
					reportData[i]="Baja";
			}
			else if(i%6==2)
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
			//System.out.println("-->"+reportData[i]);
		}

		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDiagnosticos="REPORTE DE NOVEDADES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n\n";
		
		if(UtilidadCadena.noEsVacio(fechaInicio) && UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="PERIODO: "+fechaInicio+ " --  "+fechaFin+"\n\n";
		else if(UtilidadCadena.noEsVacio(fechaInicio))
			tituloDiagnosticos+="NOVEDADES DESDE: "+fechaInicio+"\n\n";
		else if(UtilidadCadena.noEsVacio(fechaFin))
			tituloDiagnosticos+="NOVEDADES ANTES DE: "+fechaFin+"\n";
		else
			tituloDiagnosticos+="TODAS LAS NOVEDADES\n";
		
		if(codigoProfesional!=0)
			tituloDiagnosticos+="PROFESIONAL: "+codigoProfesional+" - "+nombreProfesional;
		
		if(System.getProperty("file.separator").equals("/"))
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"/imagenes/logoCP.png", medico.getInstitucion(), medico.getNit(), tituloDiagnosticos);
		}
		else
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"\\imagenes\\logoCP.png", medico.getInstitucion(), medico.getNit(), tituloDiagnosticos);
		}
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Novedades", reportHeader, reportData, 10);
		
		float widths[]={(float) 2.7,(float) 1.6,(float) 3, (float) 1.5, (float) 1.2};
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