/*
 * DevolucionpedidosPdf.java 
 * Autor			:  mdiaz
 * Creado el	:  24-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.pdf;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.princetonsa.mundo.inventarios.MotDevolucionInventarios;
import com.princetonsa.mundo.pedidos.DevolucionPedidos;
import com.princetonsa.actionform.pedidos.DevolucionPedidosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Clase para manejar la generación de PDF's para
 * las devoluciones de pedidos
 *
 * @version 1.0, 24-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class DevolucionPedidosPdf {

	
	public static void pdfDevolucionPedidos(String filename, Connection con, DevolucionPedidosForm form, UsuarioBasico usuario, HttpServletRequest request){
		PdfReports report = new PdfReports();
		String[] dataReporte;
		String[] headerReporte ;
		String[] dataHeader2;
		DevolucionPedidos mundo = new DevolucionPedidos();
		int k;
		
	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
     headerReporte = new String[7];
     headerReporte[0]="Número Pedido";
     headerReporte[1]="Artículo";
     headerReporte[2]="Lote";
     headerReporte[3]="Fecha Vencimiento";
     headerReporte[4]="Unidad Medida";
     headerReporte[5]="Saldo Despacho";
     headerReporte[6]="Cantidad a Devolver";

     String[] columns = {
     		"cod_pedido",
				"des_articulo",
				"lote",
				"fecha_vencimiento",
				"unidad_medida_articulo",
				"saldo_despacho", 
				"cantidad_devolucion"
     };
				
	    dataReporte = report.getDataFromResultset(columns, form.getArticulosDevolucion() );
		        
		    report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "DEVOLUCION DE PEDIDOS");
		    

		     dataHeader2 = new String[9];
		     dataHeader2[0]="Número Devolución: " +form.getCodigoDevolucion();
		     dataHeader2[1]="Estado: Generada";
		     dataHeader2[2]="Fecha Devolución: " +form.getFechaDevolucion() +"  -  " +form.getHoraDevolucion();
		     dataHeader2[3]="Fecha Grabación: " + util.UtilidadFecha.getFechaActual() + "  -  " + util.UtilidadFecha.getHoraActual() ;
		     dataHeader2[4]="Usuario que Devuelve: " +form.getUsuario();
		     dataHeader2[5]="Centro Costo que Devuelve: " +mundo.getNombreCentroCosto(con, form.getCodigoCentroCosto()) ;
		     dataHeader2[6]="Farmacia: " +mundo.getNombreCentroCosto(con, form.getCodigoFarmacia()) ;
		     MotDevolucionInventarios motivo = new MotDevolucionInventarios();
		     dataHeader2[7]="Motivo Devolución: " + motivo.consultarDescripcion(con,form.getMotivo(),usuario.getCodigoInstitucionInt());
		     dataHeader2[8]="Observaciones: " + form.getObservaciones();
		     
		    
		    report.createSection("header2", PdfReports.REPORT_HEADER_TABLE, 5, 2, 3);
		    for(k=0; k< (dataHeader2.length - 3); k++)
	    		report.getSectionReference("header2").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, dataHeader2[k], report.font);

		    report.getSectionReference("header2").setTableCellsColSpan(2);
		    report.getSectionReference("header2").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, dataHeader2[6], report.font);
		    report.getSectionReference("header2").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, dataHeader2[7], report.font);
		    report.getSectionReference("header2").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, dataHeader2[8], report.font);
		    report.getSectionReference("header2").setTableCellsColSpan(1);
		    
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);

		    report.openReport(filename);

		    report.font.setFontAttributes(0x000000, 14, true, false, false);
		    
		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		      
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 14);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);

	    
		    //Añadimos la sección informacion de la consulta
		   report.createColSection("detalleDevolucionSection", "Detalle de la Devolución", headerReporte, dataReporte, 10);
		   report.addSectionToDocument("detalleDevolucionSection");
		    	    
		    report.closeReport();
	}

	
	
	
}
