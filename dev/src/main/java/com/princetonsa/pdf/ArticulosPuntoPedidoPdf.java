package com.princetonsa.pdf;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class ArticulosPuntoPedidoPdf
{
	/**
	 * 
	 * @param filename
	 * @param articulos
	 * @param usuario
	 * @param porcentajeAlerta
	 * @param busCodigo
	 * @param busDescripcion
	 */
	public static void imprimirArticulosPuntoPedido(String filename, HashMap articulos, UsuarioBasico usuario, String porcentajeAlerta, String busCodigo, String busDescripcion, HttpServletRequest request)
	{
		PdfReports report = new PdfReports();
		/**Tamaño del mapa que contien todas las facturas consultadas por el flujo de TODAS**/
		int tamanioMapa=Integer.parseInt(articulos.get("numRegistros")+"");
		if (tamanioMapa==0)
		{
		    return;
		}
		else
		{
		   String[] reportHeader={
					"Cód.",
					"Descripción.",
					"Stock Mín.",
					"Stock Máx.",
					"Punto Ped.",
					"Total Existencia"
				};
			String[] reportData=new String[tamanioMapa*reportHeader.length];
			
			for(int i=0,pos=0;i<tamanioMapa;i++)
			{
				reportData[pos]=articulos.get("codigo_"+i)+"";pos++;
				reportData[pos]=articulos.get("descripcion_"+i)+" Con: "+articulos.get("concentracion_"+i)+" U.M: "+articulos.get("nomunidadmedida_"+i);pos++;
				reportData[pos]=articulos.get("stockminimo_"+i)+"";pos++;
				reportData[pos]=articulos.get("stockmaximo_"+i)+"";pos++;
				reportData[pos]=articulos.get("puntopedido_"+i)+"";pos++;
				reportData[pos]=articulos.get("existencias_"+i)+"";pos++;
			}
			/**Cabezote principal**/
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "ARTICULOS PUNTO DE PEDIDO "+(!porcentajeAlerta.trim().equals("")?"(Porcentaje Alerta: "+porcentajeAlerta+")":""));
		    
		    report.openReport(filename);
		    report.document.addParagraph(UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual(),report.font,iTextBaseDocument.ALIGNMENT_CENTER,25);
		    
		    report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		    

		    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 9);
		    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 7);
		    
		    String paramBusqueda="Todos";
		    if(busCodigo.trim().equals("")&&!busDescripcion.trim().equals(""))
		    	paramBusqueda="Descripcion: "+busDescripcion;
		    else if(!busCodigo.trim().equals("")&&busDescripcion.trim().equals(""))
		    	paramBusqueda="Codigo: "+busCodigo;
		    report.document.addParagraph("Parametros Consulta: "+paramBusqueda,  report.font, iTextBaseDocument.ALIGNMENT_CENTER, 10);
		    
		    report.createColSection("seccionArticulos", "Articulos Punto Pedido", reportHeader, reportData, 10);

			float widths[]={(float) 1,(float) 5,(float) 1,(float) 1,(float) 1,(float) 1};
			try
	        {
			    report.getSectionReference("seccionArticulos").getTableReference("parentTable").setWidths(widths);
	        }
	        catch (BadElementException e)
	        {
	            e.printStackTrace();
	        }
			

			report.addSectionToDocument("seccionArticulos");
		    
		    
		    //cerrar documento
		    report.closeReport(); 
		}
	}
}
