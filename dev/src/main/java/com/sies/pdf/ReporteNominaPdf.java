package com.sies.pdf;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Table;
import com.princetonsa.mundo.UsuarioBasico;

import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

/**
 * 
 * SiEs
 * @author Juan David Ramírez L.
 *
 */
public class ReporteNominaPdf
{
	/**
	 * Manejador de logs de la Clase
	 */
	private static Logger reporteNominaPdfLogger=Logger.getLogger(ReporteNominaPdf.class);
	
	public static void generarPdfReporteNomina(String filename, UsuarioBasico medico, HashMap<String, Object> mapaReporte, String fechaInicio, String fechaFin, String nombreCategoria, int cantProfesionales)
	{
		PdfReports report = new PdfReports();
		String[] reportHeader = {
			"Nombre Persona",
			"Doc. Identificación",
			"NO",   	   	   	   	 
			"NF",
			"DF",
			"Total",
			"THC",
			"ED"
		};
		
		String filePath=ValoresPorDefecto.getFilePath();
		String titulo="REPORTE DE NÓMINA";
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
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);

		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);

		iTextBaseTable section = report.createSection("seccion0", PdfReports.REPORT_PARENT_TABLE, 1, 3, 10);

		int sectionTitleFontColor = 0x000000;
		float sectionTitleFontSize = 14f;
		iTextBaseFont font=new iTextBaseFont();
		font.setFontAttributes(sectionTitleFontColor, sectionTitleFontSize, true, false, false);
		section.setTableCellsBackgroundColor(0xEBEBEB);
		
		section.setTableCellsDefaultHAlignment(PdfReports.REPORT_PARENT_TABLE, 1);
		
		section.addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Fecha Inicio", font);
		section.addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Fecha Fin", font);
		section.addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Categoría", font);

		section.setTableCellsColSpan(1);
		section.setTableCellsRowSpan(1);
		section.setTableCellsBackgroundColor(0xFFFFFF);

		// adicionamos las cabeceras de la informacion
		font.setFontAttributes(0x000000, 12f, true, false, false);

		try
		{
			Table rTable = section.getTableReference(PdfReports.REPORT_PARENT_TABLE);
			
			Cell rCell=new Cell();
			rCell.add(new Chunk(fechaInicio, font.getCurrentFont()));
			rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
			rCell.setHorizontalAlignment(1);
			rTable.addCell(rCell, new java.awt.Point(1, 0));    

			rCell=new Cell();
			rCell.add(new Chunk(fechaFin, font.getCurrentFont()));
			rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
			rCell.setHorizontalAlignment(1);
			rTable.addCell(rCell, new java.awt.Point(1, 1));    

			rCell=new Cell();
			rCell.add(new Chunk(nombreCategoria, font.getCurrentFont()));
			rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
			rCell.setHorizontalAlignment(1);
			rTable.addCell(rCell, new java.awt.Point(1, 2));    

		}
		catch(BadElementException e)
		{
			e.printStackTrace();
		}

		float widths[]={30f, 30f, 40f};
		
		try
        {
		    report.getSectionReference("seccion0").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
        }
        catch (BadElementException e)
        {
            reporteNominaPdfLogger.error("Se presentó error adicionando el encabezado al pdf (Reporte Nómina) " + e);
        }
        report.addSectionToDocument("seccion0");
		
		section = report.createSection("seccion1", PdfReports.REPORT_PARENT_TABLE, cantProfesionales+1, 8, 10);

		//section.setTableCellsColSpan(section.getTableReference(PdfReports.REPORT_PARENT_TABLE).columns());
		sectionTitleFontColor = 0x000000;
		sectionTitleFontSize = 14f;
		font=new iTextBaseFont();
		font.setFontAttributes(sectionTitleFontColor, sectionTitleFontSize, true, false, false);
		section.setTableCellsBackgroundColor(0xEBEBEB);
		
		//section.addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Reporte de Nómina", font);

		section.setTableCellsColSpan(1);
		section.setTableCellsRowSpan(1);

		// adicionamos las cabeceras de la informacion
		font.setFontAttributes(0x000000, 11f, true, false, false);
		
		for(int iter=0; iter < section.getTableReference(PdfReports.REPORT_PARENT_TABLE).getColumns(); iter++)
		{
			if(reportHeader[iter] != null)
			{
				section.addTableTextCell(PdfReports.REPORT_PARENT_TABLE, reportHeader[iter], font);
			}
		}

		section.setTableCellsBackgroundColor(0xFFFFFF);

		Table rTable = section.getTableReference(PdfReports.REPORT_PARENT_TABLE);
		rTable.endHeaders();

		// adicionamos la informaci&oacute;n
		font.setFontAttributes(0x000000, 10f, false, false, false);
		
		for(int iRow=1, iter=0; iter < cantProfesionales; iRow++, iter++)
		{
			try
			{
				Cell rCell = new Cell();
				rCell.add(new Chunk((String)mapaReporte.get("nombreProfesional_"+iter).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(0);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 0));

				rCell = new Cell();
				rCell.add(new Chunk((String)mapaReporte.get("cedulaProfesional_"+iter).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(0);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 1));

				rCell = new Cell();
				rCell.add(new Chunk(((Double)mapaReporte.get("no_"+iter)).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(1);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 2));

				rCell = new Cell();
				rCell.add(new Chunk(((Double)mapaReporte.get("nf_"+iter)).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(1);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 3));

				rCell = new Cell();
				rCell.add(new Chunk(((Double)mapaReporte.get("df_"+iter)).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(1);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 4));

				rCell = new Cell();
				rCell.add(new Chunk(((Double)mapaReporte.get("total_"+iter)).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(1);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 5));

				rCell = new Cell();
				rCell.add(new Chunk(((Double)mapaReporte.get("thc_"+iter)).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(1);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 6));
				
				rCell = new Cell();
				rCell.add(new Chunk(((Double)mapaReporte.get("ed_"+iter)).toString(), font.getCurrentFont()));
				rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
				rCell.setHorizontalAlignment(1);
				rCell.setVerticalAlignment(1);
				rTable.addCell(rCell, new java.awt.Point(iRow, 7));
			}
			catch(BadElementException e)
			{
				e.printStackTrace();
			}
	    }
		
		float widthsTabla[]={29f, 19f, 9f, 9f, 9f, 9f, 9f, 7f};
		try
        {
		    report.getSectionReference("seccion1").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widthsTabla);
        }
        catch (BadElementException e)
        {
            reporteNominaPdfLogger.error("Se presentó error adicionando la tabla al pdf (Reporte Nómina) " + e);
        }
        
		report.addSectionToDocument("seccion1");
		report.closeReport(); 

	}

}
