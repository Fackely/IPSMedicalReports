/*
 * Creado  04/10/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.pdf;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.inventarios.DespachoPedidoQxForm;
import com.princetonsa.actionform.pedidos.DespachoPedidosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la impresion de Despacho de Pedidos 
 * por medio de iText.
 *
 * @version 1.0, 04/10/2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class DespachoPedidosPdf 
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(DespachoPedidosPdf.class);

    public static void pdfDespachoPedidos(String filename, DespachoPedidosForm forma, UsuarioBasico usuario, HttpServletRequest request) 
    {
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        PdfReports report = new PdfReports(); 
        String[] headCollection={"Artículo","Lote","Fecha Vencimiento","Unidad Medida","Cantidad Pedida","Despacho"};
        String[] dbCollection={ 
        		"infoarticulo",
        		"lote",
        		"fechavencimiento",
				"unidadmedida",
				"cantidadpedido",
				"cantidaddespachada" };
        String[] dataCollection= report.getDataFromCollection(dbCollection,forma.getCol());
        
       
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="REPORTE DE DESPACHO DE PEDIDOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")" ;
        
		if(dataCollection.length == 0)
		{
	      	return;
	    }
        
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        try
        {
	        report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 12);
			report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
			
			 String[] datosPrincipales= {
			 		"Num de Pedido: "+forma.getNumeroPedido(), 
			 		"Estado: "+forma.getEstadoPedido()+ "   Prioridad: "+forma.getIdentificadorPrioridad(), 
					"Fecha - Hora Pedido: "+forma.getFechaHoraPedido(),
			 		"Usuario del Pedido: "+forma.getUsuarioSolicitante(), 
					"Fecha - Hora Despacho: "+forma.getFechaHoraDespacho(), 
					"Usuario Despacho: "+usuario.getLoginUsuario(), 
					"Centro Costo Solicitante: "+forma.getCentroCostoSolicitante(), 
					"Farmacia: "+forma.getFarmacia()
			 };
			
			//SECCION DATOS PRINCIPALES (numero de pedido, fecha hora, usuario solicitante)
			 	report.createSection("seccionPrincipal", PdfReports.REPORT_HEADER_TABLE, 5, 2, 20);
				report.getSectionReference("seccionPrincipal").setTableCellsColSpan(2);
				report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "DETALLE DESPACHO PEDIDOS", report.font);
				report.getSectionReference("seccionPrincipal").setTableCellsColSpan(1);
				report.font.setFontSizeAndAttributes(12,false,false,false);
				for(int k=0; k<8; k++)	
					report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, datosPrincipales[k], report.font);
			
			report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
			
			report.openReport(filename);
			report.createColSection("seccion0", "DespachoPedidos", headCollection, dataCollection, 10);
			/*
			float widths[]={(float) 5,(float) 1.6,(float) 2, (float) 1.4};
			try
	        {
			    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
	        }
	        catch (BadElementException e)
	        {
	        	logger.error("Se presentó error generando el pdf " + e);
	        }
	        */
			report.addSectionToDocument("seccion0");
	        if(!forma.getObservacionesGenerales().equals("") && forma.getObservacionesGenerales()!=null)
	        {	
	        	report.addSectionToDocument("seccionObservacion");
	        	report.document.addParagraph("Observaciones: "+forma.getObservacionesGenerales(), 20);
	        }
	        report.closeReport(); 
        }
        catch(Exception e)
        {
			logger.error("Se presentó error generando el pdf DespachoPedidos: " + e);
			e.printStackTrace();
		}
    }
    
    /**
     * Método para realizar la impresión del despacho del pedido Qx.
     * @param filename
     * @param forma
     * @param usuario
     */
    public static void pdfDespachoPedidosQx(String filename, DespachoPedidoQxForm forma,UsuarioBasico usuario, HttpServletRequest request)
    {
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//*************IMPRESION DEL ENCABEZADO************************************************
    	PdfReports report = new PdfReports(); 
        
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="REPORTE DESPACHO DE PEDIDOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")" ;
        
        filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section;
        
        //**************SECCION ENCABEZADO*********************************************************************
        section = report.createSection("encabezadoDespacho", "encabezadoDespachoTable", 5, 2, 10);
  		section.setTableBorder("encabezadoDespachoTable", 0x000000, 0.2f);
	    section.setTableCellBorderWidth("encabezadoDespachoTable", 0.5f);
	    section.setTableCellPadding("encabezadoDespachoTable", 1);
	    section.setTableSpaceBetweenCells("encabezadoDespachoTable", 0.1f);
	    section.setTableCellsDefaultColors("encabezadoDespachoTable", 0xFFFFFF, 0x000000);
	    
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Despacho Pedido Quirúrgico", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    section.setTableCellsDefaultColors("encabezadoDespachoTable", 0xFFFFFF, 0xFFFFFF);
	    section.setTableCellsColSpan(1);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Nro. Petición: "+forma.getNumeroPeticion(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Paciente: "+forma.getNombrePaciente(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Fecha/Hora Despacho: "+forma.getFechaDespacho()+" / "+forma.getHoraDespacho(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Usuario que Despacha: "+forma.getNombreUsuarioDespacho(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Centro de Costo Solicitante: "+forma.getNombreCentroCostoSolicitante(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Farmacia: "+forma.getNombreFarmacia(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    section.setTableCellsColSpan(2);
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.setTableCellsDefaultColors("encabezadoDespachoTable", 0xFFFFFF, 0x000000);
	    section.addTableTextCellAligned("encabezadoDespachoTable", "Detalle Pedidos No: "+forma.getListadoPedidos(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    //se añade la sección al documento
	    report.addSectionToDocument("encabezadoDespacho");
	    //*****************************************************************************************************************
	    
	    //***********SECCION DETALLE*****************************************************************************************
	    section = report.createSection("detalleDespacho", "detalleDespachoTable", (forma.getNumArticulos()+1), 6, 10);
  		section.setTableBorder("detalleDespachoTable", 0x000000, 0.0f);
	    section.setTableCellBorderWidth("detalleDespachoTable", 0.5f);
	    section.setTableCellPadding("detalleDespachoTable", 1);
	    section.setTableSpaceBetweenCells("detalleDespachoTable", 0.1f);
	    section.setTableCellsDefaultColors("detalleDespachoTable", 0xFFFFFF, 0x000000);
	    section.setTableCellsColSpan(1);
	    
	    report.font.setFontAttributes(0x000000, 8, true, false, false);
	    section.addTableTextCellAligned("detalleDespachoTable", "Artículo", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("detalleDespachoTable", "Lote", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("detalleDespachoTable", "Fecha Vencimiento", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("detalleDespachoTable", "Unidad Medida", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("detalleDespachoTable", "Cantidad Pedida", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    section.addTableTextCellAligned("detalleDespachoTable", "Cantidad Despacho", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    report.font.setFontAttributes(0x000000, 8, false, false, false);
	    
	    for(int i=0;i<forma.getNumArticulos();i++)
	    {
	    	section.addTableTextCellAligned("detalleDespachoTable", forma.getArticulos("articulo_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleDespachoTable", forma.getArticulos("lote_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleDespachoTable", forma.getArticulos("fechaVencimiento_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleDespachoTable", forma.getArticulos("unidadMedidaArticulo_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleDespachoTable", forma.getArticulos("totalPedido_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned("detalleDespachoTable", forma.getArticulos("totalDespacho_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	    }
	    float widths[]={(float) 5,(float) 1, (float) 1,(float) 1, (float) 1, (float) 1};
	    
	    try 
        {
			section.getTableReference("detalleDespachoTable").setWidths(widths);
		} 
        catch (BadElementException e) 
        {
			logger.error("Error al ajustar los anchos de la tabla de UPC: "+e);
		}
	    //se añade la sección al documento
	    report.addSectionToDocument("detalleDespacho");
	    //********************************************************************************************************************
	    report.closeReport();
    }
 
	
}
