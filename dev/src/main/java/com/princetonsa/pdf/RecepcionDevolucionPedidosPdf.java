/*
 * Created on 29-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.pdf;



import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;


import com.lowagie.text.BadElementException;

import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pedidos.RecepcionDevolucionPedidos;

/**
 * @author armando
 * 
 * Clase para manejar la generacion de reportes de 
 * recepcion devolucion de pedidos
 * Princeton 29-sep-2004
 */
public class RecepcionDevolucionPedidosPdf 
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger diagnosticosPdfLogger=Logger.getLogger(RecepcionDevolucionPedidosPdf.class);

	/**
	 * Método para realizar un reprote de RecepcionDevolucionPedidos
     * 
	 * @param filename Nombre del archivo con el que
	 * se desea generar el PDF
	 * @param mundo, objeto que contien los datos
	 * @param usuario, Ususario que esta generando el reporte
	 */
	public static void pdfRecepcionDevolucionPedidos(String filename, RecepcionDevolucionPedidos mundo, UsuarioBasico usuario, HttpServletRequest request) 
	{
		PdfReports report = new PdfReports();
		
		
		String[] reportData2;
		String[] reportHeader2 = {
			"Pedido", 
			"Artículo", 
			"Lote", 
			"Fecha Vencimiento", 
			"Unidad de Medida",
			"Cantidad Devuelta", 
			"Cantidad Recibida"
		};
		      
		String[] dbColumns = {
				"pedido", 
				"articulo",
				"lote",
				"fechavencimiento",
				"unidadmedida",
				"cantidaddevuelta", 
				"cantidadrecibida"
			}; 
		
		//Si la recepción es quirúrgica lleva mas campos
		if(UtilidadTexto.getBoolean(mundo.getEsQuirurgico()))
		{
			String[] dbColumnsTemp = {
					"pedido", 
					"paciente",
					"id_paciente",
					"numero_peticion",
					"numero_ingreso",
					"articulo",
					"lote",
					"fechavencimiento",
					"unidadmedida",
					"cantidaddevuelta", 
					"cantidadrecibida"
				};
			dbColumns = dbColumnsTemp;
			
			String[] reportHeaderTemp = {
				"Ped.", 
				"Paciente",
				"Nro. Id",
				"Petición",
				"Ingreso",
				"Artículo", 
				"Lote", 
				"Fecha Ven.", 
				"U.M.",
				"Cant. Dev.", 
				"Cant. Rec."
			};
			reportHeader2 = reportHeaderTemp;
			
		}
		
		
		reportData2 = report.getDataFromCollection(dbColumns, mundo.getColeccionDetalle());
		if(reportData2.length == 0)
		{
		    return;
		}	
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		String tituloDiagnosticos="RECEPCIÓN DEVOLUCIÓN DE PEDIDOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDiagnosticos);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		
		report.openReport(filename);
		
		report.font.setFontAttributes(0x000000, 10, true, false, false);
		
		report.createSection("recepcion",PdfReports.REPORT_PARENT_TABLE,5,4,10);
		report.getSectionReference("recepcion").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.0f);
		report.getSectionReference("recepcion").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
		report.getSectionReference("recepcion").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 9);
		report.getSectionReference("recepcion").setTableCellsColSpan(1);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Número de Devolución:", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,mundo.getNumeroDevolucion()+"", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Estado:", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, mundo.getEstadoDevolucion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Fecha y Hora de devolución", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,mundo.getFechaDevolucion()+" - "+UtilidadFecha.convertirHoraACincoCaracteres(mundo.getHoraDevolucion()), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Usuario que devuelve", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,mundo.getUsuarioDevolucion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Fecha y Hora de recepción", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,UtilidadFecha.conversionFormatoFechaAAp(mundo.getFechaRecepcion())+" - "+mundo.getHoraRecepcion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Usuario que recibe", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,mundo.getUsuarioRecibe(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Área que devuelve", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").setTableCellsColSpan(3);
		report.getSectionReference("recepcion").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE,mundo.getAreaNombre()+" - "+mundo.getCentroAtencion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").setTableCellsColSpan(1);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Farmacia", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").setTableCellsColSpan(3);
		report.getSectionReference("recepcion").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE,mundo.getNombreFarmacia(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		/*
		float widthsRecepcion[]={(float) 2.8,(float) 2.8,(float) 2.4,(float) 2};
		try
        {
		    report.getSectionReference("recepcion").getTableReference("parentTable").setWidths(widthsRecepcion);
		}
        catch (BadElementException e)
        {
            diagnosticosPdfLogger.error("Se presentó error generando el pdf " + e);
        }
        */
		report.addSectionToDocument("recepcion");
        
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 10);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 8);
		report.createColSection("detalleRecepcion","ARTÍCULOS DE LA DEVOLUCION", reportHeader2, reportData2, 10);
		float widths[]={(float) 1,(float) 3.5,(float) 1.5,(float) 2,(float) 2};
		
		//Si la recepción es Qx se hace otro tipo de procetnajes
		if(UtilidadTexto.getBoolean(mundo.getEsQuirurgico()))
		{
			float widthsQx[]={(float) 0.7,(float) 1.7,(float) 0.8,(float) 0.7,(float) 0.9,(float) 1.7,(float) 0.9,(float) 0.8,(float) 0.9,(float) 0.45,(float) 0.45};
			widths = widthsQx;
		}

		try
        {
			
		    report.getSectionReference("detalleRecepcion").getTableReference("parentTable").setWidths(widths);
		}
        catch (BadElementException e)
        {
            diagnosticosPdfLogger.error("Se presentó error generando el pdf " + e);
        }
        
		report.addSectionToDocument("detalleRecepcion");
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.document.addParagraph("MOTIVO DE LA DEVOLUCIÓN",report.font,iTextBaseDocument.ALIGNMENT_CENTER,30);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.document.addParagraph(mundo.getMotivo(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,10);
		report.closeReport();
	}

}
