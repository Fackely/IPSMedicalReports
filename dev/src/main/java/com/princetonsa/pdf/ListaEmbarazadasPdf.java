/*
 * Creado el 18-jun-2005
 * por Julian Montoya
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
import com.princetonsa.actionform.hojaObstetrica.HojaObstetricaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class ListaEmbarazadasPdf {

	

    /**
     * Metodo para manejar los logs de esta clase
     */
   private static Logger categoriaPdfLogger=Logger.getLogger(CategoriaPdf.class);
   
   public  static void pdfListaEmbarazadas(String filename, HojaObstetricaForm hojaObstetricaForm,UsuarioBasico medico, HttpServletRequest request)
   {
       PdfReports report = new PdfReports();
       ArrayList arrayList = new ArrayList();
       String[] reportData;
       String[] finalReport=null;
       String[] reportHeader = {
			"APELLIDOS", 
			"NOMBRES", 
			"ID",
			"EDAD",
			"FPP",
			"EG",
			"MÉDICO"
		};
		      
		String[] dbColumns =  {
	      		"apellidos",
				"nombres",
				"id", 
				"edad",
				"fpp",
				"eg",
				"medico"
				};
		
		
		
		reportData = report.getDataFromCollection(dbColumns, hojaObstetricaForm.getListado());
		
		
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
		
		
		finalReport= new String[arrayList.size()];
		
		for(int i=0; i<arrayList.size(); i++)
		     finalReport[i] = arrayList.get(i) + "";
					
		String filePath=ValoresPorDefecto.getFilePath();
		String tituloDiagnosticos="PACIENTES EMBARAZADAS  (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
			
		//-Enviar los datos al reporte
		report.createColSection("seccion0", "Resultados ", reportHeader, reportData, 7);
		
		float widths[]={(float) 2,(float) 2,(float) 1.2, (float) 0.8, (float) 1.2,(float) 0.5,(float) 2.3};
		try
        {
		    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            categoriaPdfLogger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }
}
