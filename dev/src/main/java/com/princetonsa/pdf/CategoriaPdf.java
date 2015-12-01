/*
 * Created on 23/03/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.pdf;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
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
public class CategoriaPdf {
	
	

    /**
     * Metodo para manejar los logs de esta clase
     */
   private static Logger categoriaPdfLogger=Logger.getLogger(CategoriaPdf.class);
   
   public  static void generarPdfCategorias(String filename, Collection categorias,  UsuarioBasico medico, HttpServletRequest request)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
//       String[] finalReport=null;
       String[] reportHeader = {
			"Código", 
			"Nombre", 
			"Descripcion",
			"Estado"
		};
		      
		String[] dbColumns =  {
	      		"codigo",
				"nombre",
				"descripcion", 
				"estado"
				};
		
		
		
		reportData = report.getDataFromCollection(dbColumns, categorias);
				
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
		
		/*
		finalReport= new String[arrayList.size()];
		
		for(int i=0; i<arrayList.size(); i++)
		     finalReport[i] = arrayList.get(i) + "";*/
					
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String titulo="REPORTE DE CATEGORIAS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), titulo);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Categoria", reportHeader, reportData, 10);
		
		float widths[]={(float) 1,(float) 2,(float) 4, (float) 1};
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

   public  static void generarPdfEnfermerasSinCategoria(String filename, Collection enfermerasLibres,  UsuarioBasico medico, HttpServletRequest request)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
			"Código", 
			"Nombre"
		};
		      
		String[] dbColumns =  {
	      		"codigo",
				"nombre"
				};
		
		
		
		reportData = report.getDataFromCollection(dbColumns, enfermerasLibres);
				
		if(reportData.length == 0)
		{
	      	return;
	    }

		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String titulo="REPORTE DE ENFERMERAS SIN CATEGORIA (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), titulo);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Enfermeras", reportHeader, reportData, 5);
		
		float widths[]={(float) 1,(float) 4};
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
