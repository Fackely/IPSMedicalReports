package com.sies.pdf;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Table;
import com.princetonsa.mundo.UsuarioBasico;
import com.sies.mundo.SiEsConstantes;

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
public class TipoTurnoPdf
{
	/**
	 * Manejador de logs de la Clase
	 */
	private static Logger categoriaPdfLogger=Logger.getLogger(TipoTurnoPdf.class);
	
	public static void generarPdfTipoTurno(String filename, Collection<HashMap<String, Object>> turnos,  UsuarioBasico medico)
	{
		PdfReports report = new PdfReports();
		String[] reportData;
		String[] reportHeader = {
			"Turno", 
			"Descripción", 
			"Hora Inicio",
			"Numero Horas",
			"Tipo de Turno",
			"Asignado a Días"
		};
		
		String[] dbColumns = 	
		{
      		"simbolo",
			"descripcion",
			"hora_inicio",
			"numero_horas",
      		"color_letra",
      		"color_fondo",
      		"nombre_tipo_turno",
      		"es_festivo"
		};

		reportData = report.getDataFromCollection(dbColumns, turnos);

		if(reportData.length == 0)
		{
	      	return;
	    }

/*		for(int i=0; i<reportData.length; i++)
		{
			String reportData
		}
*/

		String filePath=ValoresPorDefecto.getFilePath();
		String titulo="TIPOS DE TURNO";
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

		int numRows = (reportData.length / (reportHeader.length+2)) + 2;
		int numCols = reportHeader.length;
		iTextBaseTable section = report.createSection("seccion0", PdfReports.REPORT_PARENT_TABLE, numRows, numCols, 10);

		section.setTableCellsColSpan(section.getTableReference(PdfReports.REPORT_PARENT_TABLE).getColumns());
		int sectionTitleFontColor = 0x000000;
		float sectionTitleFontSize = 14f;
		iTextBaseFont font=new iTextBaseFont();
		font.setFontAttributes(sectionTitleFontColor, sectionTitleFontSize, true, false, false);
		section.setTableCellsBackgroundColor(0xEBEBEB);
		
		section.addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Tipos de Turno", font);
		section.setTableCellsColSpan(1);

		section.setTableCellsColSpan(1);
		section.setTableCellsRowSpan(1);
		section.setTableCellsBackgroundColor(0xFFFFFF);

		// adicionamos las cabeceras de la informacion
		font.setFontAttributes(0x000000, 12f, true, false, false);
		
		for(int iter=0; iter < section.getTableReference(PdfReports.REPORT_PARENT_TABLE).getColumns(); iter++)
		{
			if(reportHeader[iter] != null)
			{
				Table rTable = null;
				Cell  rCell = new Cell();
				
				try
				{
					rTable = section.getTableReference(PdfReports.REPORT_PARENT_TABLE);
					rCell.add(new Chunk(reportHeader[iter], font.getCurrentFont()));
					rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
					rCell.setHorizontalAlignment(1);
					rTable.addCell(rCell, new java.awt.Point(1, iter));    
				}
				catch(BadElementException e)
				{
					e.printStackTrace();
				}
			}
		}

		section.getTableReference(PdfReports.REPORT_PARENT_TABLE).endHeaders();

		numRows = section.getTableReference(PdfReports.REPORT_PARENT_TABLE).size();
		numCols = section.getTableReference(PdfReports.REPORT_PARENT_TABLE).getColumns();

		// adicionamos la informaci&oacute;n
		font.setFontAttributes(0x000000, 12f, false, false, false);
		for(int iRow=2, iter=0; iRow < numRows; iRow++)
		{
			for(int iCol=0; iCol < numCols; iCol++)
			{
				if(iter<reportData.length)
				{
					if(reportData[iter] != null)
					{
						if(iCol==0)
						{
							String simbolo=reportData[iter];
							String colorLetra=reportData[iter+4].replaceAll("#", "0x");
							String colorFondo=reportData[iter+5].replaceAll("#", "0x");
							Table tablaTurno;
							try
							{
								tablaTurno = new Table(3, 1);
								tablaTurno.setBorderWidth(0);
								tablaTurno.setSpacing(0);
								float anchos[]={2.5f, 5f, 2.5f};
								tablaTurno.setWidths(anchos);
								iTextBaseFont fuenteTurno=new iTextBaseFont();
								fuenteTurno.setFontAttributes(Integer.decode(colorLetra), sectionTitleFontSize, true, false, false);
								tablaTurno.addCell(new Cell());
								Cell celdaTurno=new Cell();
								celdaTurno.add(new Chunk(simbolo, fuenteTurno.getCurrentFont()));
								celdaTurno.setBackgroundColor(new Color(Integer.decode(colorFondo)));
								celdaTurno.setHorizontalAlignment(1);
								celdaTurno.setVerticalAlignment(1);
								tablaTurno.addCell(celdaTurno);
								tablaTurno.addCell(new Cell());
								section.getTableReference(PdfReports.REPORT_PARENT_TABLE).insertTable(tablaTurno);
							}
							catch (BadElementException e)
							{
								e.printStackTrace();
							}
						}
						else if(iCol==3)
						{
							Table rTable = null;
							Cell  rCell = new Cell();
							
							try
							{
								rTable = section.getTableReference(PdfReports.REPORT_PARENT_TABLE);
								rCell.add(new Chunk(reportData[iter], font.getCurrentFont()));
								rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
								rCell.setHorizontalAlignment(2);
								rCell.setVerticalAlignment(1);
								rTable.addCell(rCell, new java.awt.Point(iRow, iCol));    
							}
							catch(BadElementException e)
							{
								e.printStackTrace();
							}
							iter+=2;
						}
						else if(iCol==5)
						{
							String asignadoADias=reportData[iter];
							switch (Integer.parseInt(asignadoADias))
							{
								case SiEsConstantes.TURNO_FESTIVO:
									asignadoADias="Festivos y Domingos";
								break;
								case SiEsConstantes.TURNO_ORDINARIO:
									asignadoADias="Ordinarios";
								break;
								case SiEsConstantes.TURNO_TODOS_LOS_DIAS:
									asignadoADias="Todos los Días";
								break;
							}
							Table rTable = null;
							Cell  rCell = new Cell();
							
							try
							{
								rTable = section.getTableReference(PdfReports.REPORT_PARENT_TABLE);
								rCell.add(new Chunk(asignadoADias, font.getCurrentFont()));
								rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
								rCell.setHorizontalAlignment(2);
								rCell.setVerticalAlignment(1);
								rTable.addCell(rCell, new java.awt.Point(iRow, iCol));    
							}
							catch(BadElementException e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							Table rTable = null;
							Cell  rCell = new Cell();
							
							try
							{
								rTable = section.getTableReference(PdfReports.REPORT_PARENT_TABLE);
								rCell.add(new Chunk(reportData[iter], font.getCurrentFont()));
								rCell.setBackgroundColor(new java.awt.Color(0xFFFFFF));
								rCell.setHorizontalAlignment(0);
								rCell.setVerticalAlignment(1);
								rTable.addCell(rCell, new java.awt.Point(iRow, iCol));    
							}
							catch(BadElementException e)
							{
								e.printStackTrace();
							}
						}
					}
					iter++;
				}
			}
	    }

		
		//report.createColSection("seccion0", "Tipos de Turno", reportHeader, reportData, 10);
		
		float widths[]={1f,3f,1f,1f,1.5f,2.5f};
		try
        {
		    report.getSectionReference("seccion0").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
        }
        catch (BadElementException e)
        {
            categoriaPdfLogger.error("Se presentó error generando el pdf " + e);
        }
		
		report.addSectionToDocument("seccion0");
		report.closeReport(); 

	}

}
