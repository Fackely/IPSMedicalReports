/*
 * Marzo 28, del 2007
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;

import com.lowagie.text.BadElementException;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para la impresión de los articulos que estan por lote y fecha de vencimiento
 * haciendo rompimiento por almacen 
 * funcionalidad de Articulos por fecha de vencimiento
 * @author Sebastián Gómez R.
 * Princeton S.A.  
 * @version Mar 28, 2007
 */
public class ArticulosFechaVencimientoPdf 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(ArticulosFechaVencimientoPdf.class);
	
	/**
	 * Método implementado para realizar la impresion de las existencias de los articulos
	 * por fecha de vencimiento y lote haciendo rompimiento por almacen
	 * @param filename
	 * @param listado
	 * @param usuario
	 */
	public static void imprimir(Connection con,String filename,HashMap listado,UsuarioBasico usuario, HttpServletRequest request)
	{
		/**
		 * El mapa listado contiene las siguientes llaves:
		 * codigoAlmacen_
		 * nombreAlmacen_
		 * nombreCentroAtencion_
		 * codigoArticulo_
		 * descripcionArticulo_
		 * unidadMedida_
		 * lote_
		 * fechaVencimiento_
		 * existencias_
		 */
		
		
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        //****************************EDICION DEL ENCABEZADO***********************************************************
        //-- Consultar la informacion de la Empresa.
        InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	//-- Tomando la ruta definida en el web.xml para generar el reporte.
    	String filePath=ValoresPorDefecto.getFilePath();
    	//-- Titulo del reporte
    	String tituloReporte=" ARTICULOS POR FECHA DE VENCIMIENTO (Fecha de corte "+listado.get("fechaBusqueda")+")";
    	String parametros = "";
	    if(!listado.get("codigoBusqueda").toString().equals(""))
	    	parametros += "código artículo: "+listado.get("codigoBusqueda").toString();
	    if(!listado.get("descripcionBusqueda").toString().equals(""))
	    {
	    	if(!parametros.equals(""))
	    		parametros += " - ";
	    	parametros += "descripción artículo: "+listado.get("descripcionBusqueda").toString();
	    }
	    if(!parametros.equals(""))
	    	parametros = "("+parametros+")";
	    tituloReporte += parametros.equals("")?"":"\n\n"+parametros;
	    
	    
    	//------Encabezado de la página ---------------------//
    	String encabezado =  " " + institucionBasica.getRazonSocial() + "  \n " +institucionBasica.getTipoIdentificacion()+" "+institucionBasica.getNit() + 
    						"\n Dirección " + institucionBasica.getDireccion() +  "  \n" +" Telefono " + institucionBasica.getTelefono() +  "  ";
    
        report.setReportBaseHeader1(institucionBasica ,"align-left" , tituloReporte);
        
        //*******************************************************************************************************************
        
        //Se abre el reporte
        report.openReport(filename);
        report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
        iTextBaseTable section = null;
        
        
        //se toma la informacion del mapa
	    int numRegistros = Integer.parseInt(listado.get("numRegistros").toString());
	    int codigoAlmacenAnterior = 0;
	    int subtotales = 0;
        
        //**************************CUERPO DE LA IMPRESION*************************************************************
	    for(int i=0;i<numRegistros;i++)
	    {
	    	//si el almacen es diferente se crea una nueva sección
	    	if(codigoAlmacenAnterior!=Integer.parseInt(listado.get("codigoAlmacen_"+i).toString()))
	    	{
	    		if(codigoAlmacenAnterior!=0)
	    		{
	    			//Se agrega sumatoria subtotales
	    			report.font.setFontAttributes(0x000000, 8, true, false, false);
	    			section.setTableCellsColSpan(5);
	    			section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Total Existencias", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    			report.font.setFontAttributes(0x000000, 8, false, false, false);
	    			section.setTableCellsColSpan(1);
	    			section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, subtotales+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	    			
	    			//se finaliza la sección anterior
	    		    report.addSectionToDocument("detalleAnexos_"+codigoAlmacenAnterior);
	    		}
	    		
	    		subtotales = 0;
	    		codigoAlmacenAnterior = Integer.parseInt(listado.get("codigoAlmacen_"+i).toString()); 
	    		float widths[]={(float) 1,(float) 4.65, (float) 1.35,(float) 1, (float) 1, (float) 1};
	    		int numRegXAlmacen = obtenerNumeroRegistrosXAlmacen(listado,codigoAlmacenAnterior);
	    		
	    		//Se crea una nueva sección
	    		section = report.createSection("detalleAnexos_"+codigoAlmacenAnterior, PdfReports.REPORT_PARENT_TABLE, numRegXAlmacen+3, 6, 8);
		  		section.setTableBorder(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0.5f);
		  		
			    section.setTableCellBorderWidth(PdfReports.REPORT_PARENT_TABLE, 0.5f);
			    section.setTableCellPadding(PdfReports.REPORT_PARENT_TABLE, 1);
			    section.setTableSpaceBetweenCells(PdfReports.REPORT_PARENT_TABLE, 0.5f);
			    section.setTableCellsDefaultColors(PdfReports.REPORT_PARENT_TABLE, 0xFFFFFF, 0xFFFFFF);
			    report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 10);
			    report.font.setFontAttributes(0x000000, 10, true, false, false);
			    
			    //Se añade el titutlo de la sección
			    report.addSectionTitle("detalleAnexos_"+codigoAlmacenAnterior, PdfReports.REPORT_PARENT_TABLE, "Almacén: "+listado.get("nombreAlmacen_"+i).toString().trim()+" - "+listado.get("nombreCentroAtencion_"+i).toString().trim());
			    
			    report.font.setFontAttributes(0x000000, 8, true, false, false);
			    //Se asignan los encabezados de la tabla
			    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Cód.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,0);
			    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Descripción", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,1);
			    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Unidad de Medida", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,2);
			    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Lote", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,3);
			    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Fecha Vencimiento", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,4);
			    section.addTableTextCellHeaderAligned(PdfReports.REPORT_PARENT_TABLE, "Total existencias", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP,1,5);
			    
			    section.getTableReference(PdfReports.REPORT_PARENT_TABLE).endHeaders();
			    
			    try 
	            {
					section.getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widths);
				} 
	            catch (BadElementException e) 
	            {
					logger.error("Error al ajustar los anchos de la seccion de anexos: "+e);
				}
	    	}
	    	
	    	report.font.setFontAttributes(0x000000, 8, false, false, false);
	    	section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, listado.get("codigoArticulo_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, listado.get("descripcionArticulo_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, listado.get("unidadMedida_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);			    
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, listado.get("lote_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, listado.get("fechaVencimiento_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_TOP);
		    section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, listado.get("existencias_"+i).toString(), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
		    subtotales += Integer.parseInt(listado.get("existencias_"+i).toString());
		    
	    	
	    }
    	
	    if(numRegistros>0)
	    {
	    	//Se agrega sumatoria subtotales
			report.font.setFontAttributes(0x000000, 8, true, false, false);
			section.setTableCellsColSpan(5);
			section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, "Total Existencias", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			report.font.setFontAttributes(0x000000, 8, false, false, false);
			section.setTableCellsColSpan(1);
			section.addTableTextCellAligned(PdfReports.REPORT_PARENT_TABLE, subtotales+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
			
			//se finaliza la sección anterior
		    report.addSectionToDocument("detalleAnexos_"+codigoAlmacenAnterior);
	    }
	   //************************************************************************************************************/
	    
	    report.closeReport();
        //*************************************************************************************************************
	}

	/**
	 * Método implementado para obtener el numero de registros que tiene un almacen
	 * @param listado
	 * @param codigoAlmacen
	 * @return
	 */
	private static int obtenerNumeroRegistrosXAlmacen(HashMap mapa, int codigoAlmacen) 
	{
		int cuenta = 0;
		
		for(int i=0;i<Integer.parseInt(mapa.get("numRegistros").toString());i++)
		{
			if(codigoAlmacen==Integer.parseInt(mapa.get("codigoAlmacen_"+i).toString()))
				cuenta++;
		}
		
		return cuenta;
	}
	
}
