/*
 * Created on 12-oct-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.pdf;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.medicamentos.RecepcionDevolucionMedicamentosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author armando
 * Clase para manejar la generacion de reportes de 
 * recepcion devolucion de pedidos
 * Princeton 12-oct-2004
 */
public class RecepcionDevolucionMedicamentosPdf 
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger diagnosticosPdfLogger=Logger.getLogger(RecepcionDevolucionMedicamentosPdf.class);

	/**
	 * Metodo para realizar el reporte de la recepcion devolucion de medicamentos.
	 * @param filename, Nombre del archivo temporal que se creara como soporte para el reporte
	 * @param forma, Objeto que contiene los datos
	 * @param usuario, Usuario que genenra el reporte
	 */
	public static void pdfRecepcionDevolucionMedicamentos(String filename, RecepcionDevolucionMedicamentosForm forma, UsuarioBasico usuario, int codigoConvenio) 
	{
		PdfReports report = new PdfReports();
		
		
		String[] reportData2;
		String[] reportHeader2 = {
			"Solicitud", 
			"Artículo", 
			"Lote",
			"Fecha Vencimiento",
			"Unidad de Medida",
			"Cantidad Devuelta", 
			"Cantidad Recibida"
		};
		      
		String[] dbColumns =  {
			"numerosolicitud", 
			"articulo",
			"lote",
			"fechavencimiento", 
			"unidadmedida",
			"cantidaddevuelta", 
			"cantidadrecibida"
		};
		reportData2 = report.getDataFromCollection(dbColumns, forma.getColeccionDetalle());
		
		if(reportData2.length == 0)
		{
		    return;
		}	
		
		String tituloMedicamentos="RECEPCIÓN DEVOLUCIÓN DE MEDICAMENTOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), codigoConvenio);
        
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
        
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(filename);
		
		
		report.createSection("recepcion",PdfReports.REPORT_PARENT_TABLE,5,4,10);
		report.getSectionReference("recepcion").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.0f);
		report.getSectionReference("recepcion").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
		report.getSectionReference("recepcion").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 9);
		report.getSectionReference("recepcion").setTableCellsColSpan(1);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Número de Devolución:", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getNumeroDevolucion()+"", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Estado:", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, forma.getEstadoDevolucion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Apellidos y Nombre Paciente:", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getPaciente(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Cuenta:", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, forma.getCuenta()+"", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Fecha y Hora de devolución", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getFechaDevolucion()+" - "+forma.getHoraDevolucion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Usuario que devuelve", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getUsuario(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Fecha y Hora de recepción", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getFechaRecepcion()+" - "+forma.getHoraRecepcion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Usuario que recibe", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getUsuarioRecibe(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Área que devuelve", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getCentroCosto()+" - "+forma.getCentroAtencion(), report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, true, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE, "Farmacia", report.font);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.getSectionReference("recepcion").addTableTextCell(PdfReports.REPORT_PARENT_TABLE,forma.getFarmacia(),report.font);
		/*
		float widthsRecepcion[]={(float) 2.9,(float) 3.2,(float) 2.1,(float) 1.8};
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
		report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 14);
		report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
		report.createColSection("detalleRecepcion","ARTÍCULOS DE LA DEVOLUCION", reportHeader2, reportData2, 10);
		float widths[]={(float) 1,(float) 3.7,(float) 1.3,(float) 2,(float) 2};
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
		report.document.addParagraph("\n"+forma.getMotivo()==null?"":"\n"+forma.getMotivo(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,10);
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		report.document.addParagraph("\nObservaciones: "+forma.getObservaciones()==null?"":"\nObservaciones: "+forma.getObservaciones(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,10);
		report.closeReport();
	}
}


