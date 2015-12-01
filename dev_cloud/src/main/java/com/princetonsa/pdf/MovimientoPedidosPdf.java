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

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.pedidos.MovimientoPedidosForm; 
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para manejar la impresion de Movimiento de Pedidos 
 * por medio de iText.
 *
 * @version 1.0, 04/10/2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class MovimientoPedidosPdf 
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(MovimientoPedidosPdf.class);

    public static void pdfMovimientoPedidos(String filename, MovimientoPedidosForm forma, UsuarioBasico usuario, HttpServletRequest request) 
    {
        PdfReports report = new PdfReports(); 
        String[] header={"Artículo","Lote","Fecha Vencimiento","Unidad Medida","Cantidad Pedida","Despacho","Valor Total"};
    	  String[] columns = {
   				 "des_articulo",
				 "lote",
				 "fecha_vencimiento",
   				 "unidad_medida_articulo",
   				 "cantidad_pedida",
   				 "cantidad_despachada",
   				 "valor_total"
    	  };        
      
    	  int actObject = forma.getSelectedIndex();
        
        String[] data = report.getDataFromResultset(columns, forma.getArticulos());
        
               
        String filePath=ValoresPorDefecto.getFilePath();
        String tituloDespacho="REPORTE DE DESPACHO DE PEDIDOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")" ;
        
		if(data.length == 0)
		{
	      	return;
	    }
        
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
		
		try
        {
	        report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 12);
	        report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
 		 
	        String prioridad="";
	        if(ValoresPorDefecto.getValorTrueParaConsultas().equals("true")){
	        	prioridad="t";
	        }else{ 
	        	prioridad=ValoresPorDefecto.getValorTrueParaConsultas();
	        }
	        	
	        
 		 String[] datosPrincipales= {
			 		"Num de Pedido: "+forma.getPedidos().get("cod_pedido["+actObject +"]"),  
			 		"Estado: "+forma.getPedidos().get("nom_estado["+actObject +"]") + "   Prioridad: "+(((forma.getPedidos().get("es_urgente["+actObject +"]")+"").equals(prioridad))?"Urgente":"No Urgente"), 
					"Fecha - Hora Pedido: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getPedidos().get("fecha_solicitud["+actObject +"]")+"")  + " - " +UtilidadFecha.convertirHoraACincoCaracteres(forma.getPedidos().get("hora_solicitud["+actObject +"]")+""), 
			 		"Usuario del Pedido: "+forma.getPedidos().get("usuario["+actObject +"]"), 
					"Fecha - Hora Despacho: "+UtilidadFecha.conversionFormatoFechaAAp( forma.getPedidos().get("fecha_despacho["+actObject +"]")+"") +" - " +UtilidadFecha.convertirHoraACincoCaracteres(forma.getPedidos().get("hora_despacho["+actObject +"]")+""), 
					"Usuario Despacho: "+forma.getPedidos().get("usuario_despacho["+actObject +"]"), 
					"Centro Costo Solicitante: "+forma.getPedidos().get("nom_centro_costo["+actObject +"]"), 
					"Farmacia: "+forma.getPedidos().get("nom_farmacia["+actObject +"]")
			 };
			
			//SECCION DATOS PRINCIPALES (numero de pedido, fecha hora, usuario solicitante)
			 	report.createSection("seccionPrincipal", PdfReports.REPORT_HEADER_TABLE, 5, 2, 20);
				report.getSectionReference("seccionPrincipal").setTableCellsColSpan(2);
				report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "DETALLE DESPACHO PEDIDOS", report.font);
				report.getSectionReference("seccionPrincipal").setTableCellsColSpan(1);
				report.font.setFontSizeAndAttributes(12,false,false,false);
				for(int k=0; k<datosPrincipales.length; k++)	
					report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, datosPrincipales[k], report.font);
			
			report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
			
			report.openReport(filename);
			
			report.createColSection("seccion0", "DespachoPedidos", header, data, 10);
			
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
	        
	        
	        //**********SECCIÓN DATOS ANULACION*****************************************
	        int codigoEstado = Integer.parseInt(forma.getPedidos().get("cod_estado["+actObject+"]")+"");
	        if(codigoEstado==ConstantesBD.codigoEstadoPedidoAnulado)
	        {
	        	String[] headAnulacion={"Motivo","Usuario","Fecha/Hora"};
	        	String[] dataAnulacion={
	        			forma.getDatosAnulacion("motivo_0")+"",
						forma.getDatosAnulacion("usuario_0")+"",
	        			forma.getDatosAnulacion("fecha_0")+" - "+forma.getDatosAnulacion("hora_0")};
	        	report.createColSection("seccion1","Datos Anulación",headAnulacion,dataAnulacion,10);
	        	float widths1[]={(float) 7,(float) 1.5,(float) 2.5};
				try
		        {
				    report.getSectionReference("seccion1").getTableReference("parentTable").setWidths(widths1);
		        }
		        catch (BadElementException e)
		        {
		        	logger.error("Se presentó error generando el pdf " + e);
		        }
				
		        report.addSectionToDocument("seccion1");
	        }
	        //**************************************************************************
	        
	        if(! (forma.getPedidos().get("observaciones_generales["+actObject +"]")+"").equals(""))
	        {	
	        	report.addSectionToDocument("seccionObservacion");
	        	report.document.addParagraph("Observaciones: "+(forma.getPedidos().get("observaciones_generales["+actObject +"]")+""), 20);
	        }
	       
	        report.closeReport(); 
			
        }
        catch(Exception e)
        {
			logger.error("Se presentó error generando el pdf DespachoPedidos: " + e);
			e.getStackTrace();
		}
    }
 
	
}
