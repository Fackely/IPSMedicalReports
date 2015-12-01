/*
 * Creado en 18/12/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.sies.pdf;

import java.util.Collection;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.UsuarioBasico;
import com.sies.actionform.VacacionesForm;

/**
 * Si.Es.
 */
public class ReporteVacacionesPdf
{

	/**
     * Metodo para manejar los logs de esta clase
     */
   private static Logger logger=Logger.getLogger(ReporteVacacionesPdf.class);
   
   /**
    * Método que genera el PDF para las vacaciones
    * @param filename
    * @param usuario
    * @param novedades
    * @param fechaInicio
    * @param fechaFin
    * @param opcionConsulta
    * @param nombreEnfermera
    */
   public  static void generarPDF(String filename, UsuarioBasico usuario, Collection novedades, String fechaInicio, String fechaFin, int opcionConsulta, String nombreEnfermera)
   {
       PdfReports report = new PdfReports();
       String[] reportData;
       String[] reportHeader = {
   			"Nombre Enfermera", 
			"Fecha Inicio", 
			"Fecha Finalización"
		};
		      
		String[] dbColumns =  {
	      		"nombre",
				"fecha_inicio",
				"fecha_fin" 
				};
		
		
		//System.out.println("estoy en el pdf");
		
		reportData = report.getDataFromCollection(dbColumns, novedades);
				
		if(reportData.length == 0)
		{
	      	return;
	    }
		
		for(int i=0; i<reportData.length; i++) // Se actualiza la prioridad
		{
			if(i%3==1)
			{
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
			}
			else if(i%3==2)
			{
				reportData[i]=UtilidadFecha.conversionFormatoFechaAAp(reportData[i]);
			}
		}


		String filePath=ValoresPorDefecto.getFilePath();
		String titulo="VACACIONES";
		if(VacacionesForm.OPCION_CONSULTA_FECHAS==opcionConsulta)
		{
			titulo+="\nFECHA INICIO "+fechaInicio+" -- FECHA FIN "+fechaFin;
		}
		else if(VacacionesForm.OPCION_CONSULTA_ENFERMERA==opcionConsulta)
		{
			titulo+="\n"+nombreEnfermera;
		}
		titulo+="\n"+usuario.getLoginUsuario()+" ("+UtilidadFecha.getFechaActual()+" -- "+UtilidadFecha.getHoraActual()+")";

		String separador=System.getProperty("file.separator");
		
		filePath=filePath.substring(0, filePath.lastIndexOf(separador, (filePath.length()-2)));
		report.setReportBaseHeader(filePath+separador+"imagenes"+separador+"logoCP.png", usuario.getInstitucion(), usuario.getNit(), titulo);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.createColSection("seccion0", "Vacaciones", reportHeader, reportData, 10);
		
		float widths[]={(float) 5, (float) 2.5, (float) 2.5};
		try
        {
		    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
        }
        catch (BadElementException e)
        {
            logger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }
	
}
