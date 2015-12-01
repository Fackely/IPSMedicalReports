/*
 * Creado en 18/12/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.princetonsa.pdf;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.sies.actionform.GenerarReporteObservacionesForm;

/**
 * @author Karenth Marín
 *
 * Si.Es.
 */
public class ObservacionesPdf
{


    /**
     * Metodo para manejar los logs de esta clase
     */
   private static Logger observacionesPdfLogger=Logger.getLogger(ObservacionesPdf.class);
   
   public  static void pdfObservaciones(String filename, GenerarReporteObservacionesForm generarReporteObservacionesForm,  UsuarioBasico medico, HttpServletRequest request )
   {
       PdfReports report = new PdfReports();
       ArrayList arrayList = new ArrayList();
       String[] reportData;
       String[] finalReport=null;
       String[] reportHeader = {
			"Nombre Enfermera", 
			"Fecha", 
			"Turno",
			"Observacion"
		};
		      
		String[] dbColumns =  {
	      		"nombre",
				"fecha",
				"turno", 
				"observacion"
				};
		
		
		
		reportData = report.getDataFromCollection(dbColumns,generarReporteObservacionesForm.getListado());
				
		if(reportData.length == 0)
		{
	      	return;
	    }

		
		finalReport= new String[arrayList.size()];
		
		for(int i=0; i<arrayList.size(); i++)
		     finalReport[i] = arrayList.get(i) + "";
					
		String filePath=ValoresPorDefecto.getFilePath();
		//String tituloDiagnosticos="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n"+ "PERIODO: "+generarReporteObservacionesForm.getFechaInicio()+ " --  "+generarReporteObservacionesForm.getFechaFin()+"\n"+"CATEGORIA: "+generarReporteObservacionesForm.getNombreCategoria();
		String tituloDiagnosticos="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n"+ "PERIODO: "+generarReporteObservacionesForm.getFechaInicio()+ " --  "+generarReporteObservacionesForm.getFechaFin()+"\n";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Observaciones", reportHeader, reportData, 10);
		
		float widths[]={(float) 4,(float) 1.5,(float) 1, (float) 4};
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
