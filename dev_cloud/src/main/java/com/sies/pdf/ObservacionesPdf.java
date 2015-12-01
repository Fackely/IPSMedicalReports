/*
 * Creado en 6/07/2007
 *
 * Andrés Arias López
 * Si.Es.
 */
package com.sies.pdf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Andrés Arias López
 * Si.Es.
 */
public class ObservacionesPdf
{
    /**
     * Logger para manejar los logs de esta clase
     */
   private static Logger observacionesPdfLogger=Logger.getLogger(ObservacionesPdf.class);
   
   /**
    * Generación del pdf
    * @param filename
    * @param generarReporteObservacionesForm
    * @param medico
    */
   public  static void pdfObservaciones(String filename, UsuarioBasico medico, Collection observaciones, int codigoCategoria, String fechaInicio, String fechaFin, String nombreCategoria)
   {
       PdfReports report = new PdfReports();
         
//     Cabecera del documento
		String filePath=ValoresPorDefecto.getFilePath();
		//String tituloDiagnosticos="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n"+ "PERIODO: "+generarReporteObservacionesForm.getFechaInicio()+ " --  "+generarReporteObservacionesForm.getFechaFin()+"\n"+"CATEGORIA: "+generarReporteObservacionesForm.getNombreCategoria();
		String titulo;
		if(codigoCategoria!=0)
		{
			titulo="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n\n"+ "PERIODO: "+fechaInicio+ " -- "+fechaFin+"\n\n";
		}
		else
		{
			titulo="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n\n"+ "PERIODO: "+fechaInicio+ " -- "+fechaFin+"\n\n";
		}
		
		//Definicion del logo del documento
		if(System.getProperty("file.separator").equals("/"))
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"/imagenes/logo-grey.gif", medico.getInstitucion(), medico.getNit(), titulo);
		}
		else
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"\\imagenes\\logo-grey.gif", medico.getInstitucion(), medico.getNit(), titulo);
		}
		
		Iterator<HashMap<String, Object>> iteDatos=observaciones.iterator();
		for(int i=0; i<observaciones.size(); i++)
		{
			HashMap<String, Object> obsBean=iteDatos.next();
			String nombreCate=(String)obsBean.get("nomcate");
			String nombreObservacion=(String)obsBean.get("descripobserva");
			String nombrePersona=(String)obsBean.get("nombre");
        	String simboloTurno=(String)obsBean.get("turno");
        	String fecha=(String)obsBean.get("fecha");
        	String descripObservacion=(String)obsBean.get("observacion");
    	   
			String[] cabecera=new String[12];
			cabecera[0]="Categoría";
			cabecera[1]=nombreCate;
			cabecera[2]="Tipo Observación";
			cabecera[3]=nombreObservacion;
			cabecera[4]="Nombre Persona";
			cabecera[5]="Turno";
			cabecera[6]="Fecha";
			cabecera[7]="Observación";
			cabecera[8]=nombrePersona;
			cabecera[9]=simboloTurno;
			cabecera[10]=fecha;
			cabecera[11]=descripObservacion;
						
			//*******************************************************
			try
			{
				report.createSection("TipObs", PdfReports.REPORT_HEADER_TABLE, 4, 4, 2);
				report.getSectionReference("TipObs").setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
				report.getSectionReference("TipObs").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
				report.getSectionReference("TipObs").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xEBEBEB, 0x000000);
				report.getSectionReference("TipObs").setTableCellsColSpan(2);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[0], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[1], report.font);
				report.getSectionReference("TipObs").setTableCellsColSpan(2);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[2], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[3], report.font);
				report.getSectionReference("TipObs").setTableCellsColSpan(1);
				report.getSectionReference("TipObs").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0x000000);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[4], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[5], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[6], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[7], report.font);
				report.getSectionReference("TipObs").setTableCellsColSpan(1);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[8], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[9], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[10], report.font);
				report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[11], report.font);
			
				float widths[]={(float) 2.3, (float) 2.2,(float) 2.5,(float) 2.5};
				try
				{
					report.getSectionReference("TipObs").getTableReference(PdfReports.REPORT_HEADER_TABLE).setWidths(widths);
				}
				catch (Exception e)
				{
					observacionesPdfLogger.error("Se presentó error generando el pdf de observaciones" + e);
				}
				report.openReport(filename);
			}
			catch(Exception e)
			{
					observacionesPdfLogger.error("Se presentó error generando el pdf de Observaciones" + e);
					report.closeReport();
			}
		}
		
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 12);
		report.font.setFontSizeAndAttributes(12, false, false, false);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }
   
   //--------Reporte obtenido al seleccionar la categoria------------- 
   
   public  static void pdfObservacionesCategoria(String filename, UsuarioBasico medico, Collection observaciones, int codigoCategoria, String fechaInicio, String fechaFin, String nombreCategoria)
   {
       PdfReports report = new PdfReports();
         
//     Cabecera del documento
		String filePath=ValoresPorDefecto.getFilePath();
		//String tituloDiagnosticos="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n"+ "PERIODO: "+generarReporteObservacionesForm.getFechaInicio()+ " --  "+generarReporteObservacionesForm.getFechaFin()+"\n"+"CATEGORIA: "+generarReporteObservacionesForm.getNombreCategoria();
		String titulo;
		if(codigoCategoria!=0)
		{
			titulo="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n\n"+ "PERIODO: "+fechaInicio+ " --  "+fechaFin+"\n\n";
		}
		else
		{
			titulo="REPORTE DE OBSERVACIONES (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")"+"\n\n"+ "PERIODO: "+fechaInicio+ " --  "+fechaFin+"\n\n";
		}
		
		//Definicion del logo del documento
		if(System.getProperty("file.separator").equals("/"))
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"/imagenes/logo-grey.gif", medico.getInstitucion(), medico.getNit(), titulo);
		}
		else
		{
			filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2)));
			report.setReportBaseHeader(filePath+"\\imagenes\\logo-grey.gif", medico.getInstitucion(), medico.getNit(), titulo);
		}
		
		String nombreCate=nombreCategoria;
		
		nombreCate=nombreCategoria.substring(0,1)+nombreCategoria.substring(1).toLowerCase();
		
		
		String[] cabecera=new String[12];
		cabecera[0]="Categoría";
		cabecera[1]=nombreCate;
		cabecera[4]="Nombre Persona";
		cabecera[5]="Turno";
		cabecera[6]="Fecha";
		cabecera[7]="Observación";
		
		String tipoObservAnt=null;
		
		try
		{
			report.createSection("TipObs", PdfReports.REPORT_HEADER_TABLE, 1, 4, 1);
			report.getSectionReference("TipObs").setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
			report.getSectionReference("TipObs").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
			report.getSectionReference("TipObs").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xEBEBEB, 0x000000);
			report.getSectionReference("TipObs").setTableCellsColSpan(2);
			report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[0], report.font);
			report.getSectionReference("TipObs").addTableTextCell(PdfReports.REPORT_HEADER_TABLE,cabecera[1], report.font);
			
			Iterator<HashMap<String, Object>> iteDatos=observaciones.iterator();
			int contador=0;
			for(int i=0; i<observaciones.size(); i++)
			{
				HashMap<String, Object> obsBean=iteDatos.next();
				String nombreObservacion=(String)obsBean.get("descripobserva");
				String nombrePersona=(String)obsBean.get("nombre");
			    String simboloTurno=(String)obsBean.get("turno");
			    String fecha=(String)obsBean.get("fecha");
			    String descripObservacion=(String)obsBean.get("observacionper");
		   	   
				cabecera[2]="Tipo Observación";
				cabecera[3]=nombreObservacion;
				cabecera[8]=nombrePersona;
				cabecera[9]=simboloTurno;
				cabecera[10]=fecha;
				cabecera[11]=descripObservacion;

			    if(tipoObservAnt==null || !tipoObservAnt.equals(nombreObservacion))
			    {
			    	tipoObservAnt=nombreObservacion;
					report.createSection("TipObs_"+contador, PdfReports.REPORT_HEADER_TABLE, 2, 4, 1);
					report.getSectionReference("TipObs_"+contador).setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
					report.getSectionReference("TipObs_"+contador).setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
					report.getSectionReference("TipObs_"+contador).setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xEBEBEB, 0x000000);
					
					report.getSectionReference("TipObs_"+contador).setTableCellsColSpan(2);
					report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[2], report.font);
					report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[3], report.font);

					report.getSectionReference("TipObs_"+contador).setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0x000000);

					report.getSectionReference("TipObs_"+contador).setTableCellsColSpan(1);
					report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[4], report.font);
					report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[5], report.font);
					report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[6], report.font);
					report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[7], report.font);
					

			    }

				//*******************************************************
				
				report.getSectionReference("TipObs_"+contador).setTableCellsColSpan(1);
				report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[8], report.font);
				report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[9], report.font);
				report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[10], report.font);
				report.getSectionReference("TipObs_"+contador).addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[11], report.font);
					
				float widths[]={(float) 2.3, (float) 2.2,(float) 2.5,(float) 2.5};
				try
			    {
				report.getSectionReference("TipObs_"+contador).getTableReference(PdfReports.REPORT_HEADER_TABLE).setWidths(widths);
			    }
			    catch (Exception e)
			    {
			      	observacionesPdfLogger.error("Se presentó error generando el pdf de observaciones" + e);
			    }
			    report.openReport(filename);
			}
		}
		catch(Exception e)
		{
			observacionesPdfLogger.error("Se presentó error generando el pdf de Observaciones por Categoria" + e);
			report.closeReport();
		}	
		
		//Define Los atributos de las tablas
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 12);
		report.font.setFontSizeAndAttributes(10, false, false, false);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 
   }
}
   

