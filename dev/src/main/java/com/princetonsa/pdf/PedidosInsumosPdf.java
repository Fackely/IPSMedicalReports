/*
 * Created on 5/10/2004
 *
 * Princeton S.A.
 */
package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.pedidos.PedidosInsumosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Clase para manejar la impresion de Pedidos de Insumos, 
 * por medio de iText.
 *
 * @version 1.0, 21/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Arlen L&oacute;pez Correa.</a>
 */
public class PedidosInsumosPdf 
{
	 /**
	 * Objeto para manejar los logs de esta clase
	*/
    private static Logger logger=Logger.getLogger(PedidosInsumosPdf.class);
    
    public static void pdfPedidosInsumos(String filename, PedidosInsumosForm forma, UsuarioBasico usuario,HashMap datosPeticion, HttpServletRequest request) 
    {
    	
    	 boolean impresionQx = datosPeticion==null?false:true;
    	 PdfReports report = new PdfReports(); 
         String[] headCollection={"Artículo,Concentración,Forma Farmaceútica","Unidad Medida","Existencias","Cantidad Pedida"};
         String estado="";
         String prioridad="";
         ArrayList arrayList= new ArrayList();
         
         String filePath=ValoresPorDefecto.getFilePath();
         String tituloDespacho="REPORTE PEDIDOS DE INSUMOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")" ;
        
         InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
         
         //generacion de la cabecera, validando el tipo de separador e utilizado en el path
         report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloDespacho);
         
         try
	        {
		        report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 12);
				report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
				if(forma.getCheckTerminarPedido().equals("on"))
				{
					estado="Terminado";
				}
				else if(forma.getCheckAnularPedido().equals("on"))
				{
					estado = "Anulado";
				}
				else
				{
					estado="Solicitado";
				}
				
				if(forma.getCheckPrioridadPedido().equals("on"))
				{
					prioridad="Si";
				}
				else
				{
					prioridad="No";
				}
				
				
				 String[] datosPrincipales= new String[0];
				 
				 //Se verifica si la impresion del pedido es Qx o No
				 if(impresionQx)
				 {
					 String[] datosPrincipalesA = {
							 	"Paciente: "+datosPeticion.get("paciente"),
							 	"Número de Ingreso: "+datosPeticion.get("numeroIngreso"),
							 	"Número de Petición: "+datosPeticion.get("numeroPeticion"),
						 		"Número de Pedido: "+forma.getNumeroPedido(), 
						 		"Estado: "+estado,
						 		"Fecha - Hora Pedido: "+forma.getFechaHoraPedido(),
						 		"Usuario Solicitante: "+usuario.getLoginUsuario(), 
								"Fecha - Hora Grabación: "+forma.getPedidosMap("fechaHoraGrabacion_"+0)+"", 
								"Centro de costo que Solicita: "+ forma.getNombreCentroCosto(), 
								"Prioridad Urgente Si/No: "+prioridad,
								"Farmacia: "+ forma.getNombreFarmacia(),
								""
						 };
						 datosPrincipales = datosPrincipalesA;
				 }
				 else
				 {
					 String[] datosPrincipalesA = {
					 		"Número de Pedido: "+forma.getNumeroPedido(), 
					 		"Estado: "+estado,
					 		"Fecha - Hora Pedido: "+forma.getFechaHoraPedido(),
					 		"Usuario Solicitante: "+usuario.getLoginUsuario(), 
							"Fecha - Hora Grabación: "+forma.getPedidosMap("fechaHoraGrabacion_"+0)+"", 
							"Centro de costo que Solicita: "+ forma.getNombreCentroCosto(), 
							"Prioridad Urgente Si/No: "+prioridad,
							"Farmacia: "+ forma.getNombreFarmacia()
					 };
					 datosPrincipales = datosPrincipalesA;
				 }
				 
				 
				 int pos = 0;
				 for(int i=0; i < forma.getNumeroIngresos(); i ++)
				 {
				 	if(forma.getPedidosMap("concentracion_"+i)!=null&&forma.getPedidosMap("formaFarmaceutica_"+i)!=null)
				 	{
				 		String info = (forma.getPedidosMap("articulo_"+i)+"").trim()+
				 			", "+(forma.getPedidosMap("concentracion_"+i)+"").trim()+", "+(forma.getPedidosMap("formaFarmaceutica_"+i)+"").trim()+
				 			", "+(forma.getPedidosMap("unidadMedida_"+i)+"").trim()+", "+(forma.getPedidosMap("naturaleza_"+i)+"").trim();
				 		arrayList.add(pos,info);
				 	}
				 	else
				 		arrayList.add(pos,forma.getPedidosMap("articulo_"+i).toString().trim());
				 	pos ++;
				 	if(forma.getPedidosMap("unidadMedida_"+i)!=null)
				 		arrayList.add(pos,forma.getPedidosMap("unidadMedida_"+i));
				 	else
				 		arrayList.add(pos,forma.getPedidosMap("unidadMedidaArticulo_"+i));
					pos ++;
					arrayList.add(pos,forma.getPedidosMap("existenciaXAlmacen_"+i));
					pos ++;
					arrayList.add(pos,forma.getPedidosMap("cantidadDespacho_"+i));
					pos ++;
				 }
				 
				 String[] data=new String [arrayList.size()];
				 
				 for(int j=0; j < arrayList.size(); j ++)
				 {
				 	data[j]=arrayList.get(j) + "";
				 }
				 
				if(impresionQx)
					report.createSection("seccionPrincipal", PdfReports.REPORT_HEADER_TABLE, 4, 2, 20);
				else
					report.createSection("seccionPrincipal", PdfReports.REPORT_HEADER_TABLE, 6, 2, 20);
				report.getSectionReference("seccionPrincipal").setTableCellsColSpan(2);
				report.font.useFont(iTextBaseFont.FONT_COURIER, true, false, false);
				report.font.setFontSizeAndAttributes(10,true,false,false);
				report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "INFORMACION GENERAL", report.font);
				report.getSectionReference("seccionPrincipal").setTableCellsColSpan(1);
				report.font.setFontSizeAndAttributes(8,false,false,false);
				
				if(impresionQx)
				{
					for(int k=0; k<12; k++)	
						report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, datosPrincipales[k].trim(), report.font);
				}
				else
				{
					for(int k=0; k<8; k++)	
						report.getSectionReference("seccionPrincipal").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, datosPrincipales[k].trim(), report.font);
				}
				
				report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
				
				report.openReport(filename);
				
				//*****************************CREACION SECCIÓN DE CIRUGIAS (solo si aplica)********************************************
				if(impresionQx)
				{
					int numCirugias = Integer.parseInt(datosPeticion.get("numCirugias").toString());
					
					report.createSection("seccionCirugias", PdfReports.REPORT_PARENT_TABLE, (numCirugias+1), 1, 0.5f);
					report.getSectionReference("seccionCirugias").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0x000000, 0.0f);
		            report.getSectionReference("seccionCirugias").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.0f);
		            report.getSectionReference("seccionCirugias").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
		            
		            //INSERCION DEL TÍTULO
		            report.font.setFontSizeAndAttributes(10,true,false,false);
		            report.getSectionReference("seccionCirugias").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Cirugías",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		            report.getSectionReference("seccionCirugias").setTableCellsColSpan(1);
		            
		            //INSERCIÓN DEL CONTENIDO
		            report.font.setFontSizeAndAttributes(8,false,false,false);
		            
		            HashMap cirugias = (HashMap) datosPeticion.get("cirugias");
		            
		            for(int i=0;i<numCirugias;i++)
		        		report.getSectionReference("seccionCirugias").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, (i+1)+". "+cirugias.get("servicio_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		            
		            report.addSectionToDocument("seccionCirugias");
		            
				}
				//**********************************************************************************************************************
				
				
				//**********************************************************************************************************************
			    report.createSection("seccion0",PdfReports.REPORT_PARENT_TABLE,(forma.getNumeroIngresos()+1),4,0.5f);
	            report.getSectionReference("seccion0").setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0x000000, 0.0f);
	            report.getSectionReference("seccion0").setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.0f);
	            report.getSectionReference("seccion0").setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0x000000);
				
	            //INSERCION DEL TÍTULO
	            report.getSectionReference("seccion0").setTableCellsColSpan(4);
	            report.font.setFontSizeAndAttributes(10,true,false,false);
	            report.getSectionReference("seccion0").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Detalle Pedido Insumos",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            report.getSectionReference("seccion0").setTableCellsColSpan(1);
	            
	            //INSERCIÓN DEL ENCABEZADO
	            report.font.setFontSizeAndAttributes(8,true,false,false);
	            int auxI2 = 0;
	            while(auxI2<4)
	            {
	            	report.getSectionReference("seccion0").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, headCollection[auxI2],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	            	auxI2 ++;
	            }
	            //INSERCIÓN DEL CONTENIDO
	            report.font.setFontSizeAndAttributes(8,false,false,false);
	            
	            int auxI1 = 0;
		        while(auxI1<data.length)
		        {
	        		//CAMPO 1
	        		report.getSectionReference("seccion0").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, data[auxI1],report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO 2
	        		report.getSectionReference("seccion0").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, data[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO 3
	        		report.getSectionReference("seccion0").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, data[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		//CAMPO 4
	        		report.getSectionReference("seccion0").addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, data[auxI1],report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
	        		auxI1++;
	        		
		        	
		        }
			    //*************************************************************************************************************************
				
				float widths[]={(float) 5,(float) 1.8,(float) 1.4,(float) 1.8};
				try
		        {
				    report.getSectionReference("seccion0").getTableReference("parentTable").setWidths(widths);
		        }
		        catch (BadElementException e)
		        {
		        	logger.error("Se presentó error generando el pdf " + e);
		        }
		        report.addSectionToDocument("seccion0");
		        
		        //*************************SECCIÓN DE ANULACION**********************************+
		        if(forma.getCheckAnularPedido().equals("on"))
		        {
		        	String[] headAnulacion={"Motivo","Usuario","Fecha/Hora"};
		        	String[] dataAnulacion={
		        			forma.getPedidosMap("motivoAnulacion")+"",
							forma.getPedidosMap("usuarioAnulacion")+"",
		        			forma.getPedidosMap("fechaAnulacion")+" - "+forma.getPedidosMap("horaAnulacion")};
		        	report.createColSection("seccion1","Datos Anulación",headAnulacion,dataAnulacion,10);
		        	float widths1[]={(float) 6,(float) 1.5,(float) 2.5};
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
		        //************************************************************
		        if(!forma.getObservacionesGenerales().equals("") && forma.getObservacionesGenerales()!=null)
		        {	
		        	
		        	
		        	report.addSectionToDocument("seccionObservacion");
		        	report.document.addParagraph("Observaciones: "+forma.getObservacionesGenerales(), 20);
		        }
				
				report.closeReport(); 
				
	        }
         catch(Exception e)
	        {
				logger.error("Se presentó error generando el pdf Pedidos Insumos: " + e);
				e.printStackTrace();
			}
         
         
    	
    }
	
}
