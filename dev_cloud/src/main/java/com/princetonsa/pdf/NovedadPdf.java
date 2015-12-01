/*
 * Created on 14/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.pdf;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NovedadPdf {
	
	


    /**
     * Metodo para manejar los logs de esta clase
     */
   private static Logger novedadPdfLogger=Logger.getLogger(NovedadPdf.class);
   
   public  static void generarPdfNovedades(String filename, Collection novedades,  UsuarioBasico medico, HttpServletRequest request )
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
			"Código", 
			"Nombre", 
			"Descripcion",
			"Cuenta para Nomina",
			"Estado"
		};
		      
		String[] dbColumns =  {
	      		"codigo",
				"nombre",
				"descripcion",
				"nomina",
				"estado"
				};
		
		
		
		reportData = report.getDataFromCollection(dbColumns,novedades);
				
		if(reportData.length == 0)
		{
	      	return;
	    }

		for(int k=0;k<reportData.length;k++)
		{
		     if(reportData[k].equals("true"))
		     {
		     	reportData[k]="Activo";        
		     }
		     if(reportData[k].equals("false"))
		     {
		     	reportData[k]="Inactivo";        
		     } 
		}
		
		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDiagnosticos="REPORTE DE NOVEDADES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Categoria", reportHeader, reportData, 10);
		
		float widths[]={(float) 0.5,(float) 1.5,(float) 3, (float) 2.5, (float) 1};
		try
        {
		    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            novedadPdfLogger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }
	

}
