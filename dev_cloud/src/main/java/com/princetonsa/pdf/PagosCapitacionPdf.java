/*
 * Creado en Aug 4, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.pdf;


import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.lowagie.text.BadElementException;
import com.princetonsa.actionform.capitacion.PagosCapitacionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase que realiza la impresión de la aprobación y consulta de pagos
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Aug 4, 2006
 */
public class PagosCapitacionPdf
{
	/**
	 * Metodo para generar el PDF de la aprobación de pago de capitación
	 * @param filename
	 * @param forma
	 * @param usuario
	 * @param con
	 */
	public static void pdfAprobacionPago(String filename, PagosCapitacionForm forma, UsuarioBasico usuario, Connection conec, HttpServletRequest request) 
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        
        //-Crear el String para enviar los datos al reporte 	
        int numRows = UtilidadCadena.noEsVacio(forma.getMapaDetallePagos("numRegistros")+"") ? Integer.parseInt(forma.getMapaDetallePagos("numRegistros")+"") : 0;
        
        int filCero = 0, con = 0;
        int indiceAprobacion=forma.getIndiceAprobacionPago();
        
		//-Verificar si algun valor de pago es igual a cero-- Para No Imprimir Este.
		for(int i=0; i<numRows; i++)
		{ 
			String valor =forma.getMapaDetallePagos("valor_pago_"+i)+"";
			if ( Float.parseFloat(valor) == 0 )
			{
				filCero++;	
			}
		}
        
        String[] datos = new String[(numRows+1-filCero) * 2];
		
		
		
		//--------Se guarda en datos lo que se va ha mostrar -------------//
		for(int i=0; i<numRows; i++)
		{ 
			String valor =forma.getMapaDetallePagos("valor_pago_"+i)+"";
			if ( Float.parseFloat(valor) != 0 )
			{
				datos[con] = forma.getMapaDetallePagos("cuenta_cobro_capitacion_"+i)+"";
				con++;
				datos[con] = UtilidadTexto.formatearValores(valor,2,true,false);
				con++;	 
			}	
		}
		
		//-- Titulos de la tabla  
		String[] titulosCol = {"Cuenta Cobro","Valor Pago"};
		
//		--Esta alineación corresponde a cada una de las  columnas especificadas para los títulos
		int[] alineacionCols = {iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_RIGHT};
		
       //-- Tomando la ruta definida en el web.xml para generar el reporte.
       String filePath=ValoresPorDefecto.getFilePath();
       
       //-- Consultar la informacion de la Empresa.
       InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
       
       //-- Titulo del reporte
       String tituloReporte="  APROBACIÓN DE PAGO";
       
       //------Encabezado de la página ---------------------//
       String encabezado =  " " + institucionBasica.getRazonSocial() + "  \n " +institucionBasica.getTipoIdentificacion()+" "+institucionBasica.getNit() + 
       						"\n Dirección " + institucionBasica.getDireccion() +  "  \n" +" Telefono " + institucionBasica.getTelefono() +  "  ";
       
       //Generacion de la cabecera, validando el tipo de separador e utilizado en el path
       report.setReportBaseHeader1(institucionBasica,"align-left" , tituloReporte);
       
       //-----------------------------------------Abrir Reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
       
       //-------------------------------- Información del detalle de pago ----------------------------------------------
       report.font.setFontSizeAndAttributes(11,true,false,false);
       
       String cad = "(" + forma.getMapaApliPagos("acronimo_tipo_documento_"+indiceAprobacion) + ") No. " + forma.getMapaApliPagos("documento_"+indiceAprobacion);
		  cad += "  Aplicación No. "+forma.getMapaApliPagos("numero_aplicacion_"+indiceAprobacion)+"        Estado:  Aprobado";  
		  report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,35);
		  
	  cad =  " ELABORADO Fecha: " +UtilidadFecha.conversionFormatoFechaAAp(forma.getMapaApliPagos("fecha_elaboracion_"+indiceAprobacion)+"")+  "        Usuario: " + usuario.getNombreUsuario();
	  report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);

	  cad = " APROBADO Fecha: "  + forma.getFechaAprobacionPago() + "        Usuario: " + usuario.getNombreUsuario();
	  report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
		 
	  cad =  " Convenio: " + forma.getMapaApliPagos("nombre_convenio_"+indiceAprobacion);
	  report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	  
	  cad =  " Observaciones: " + forma.getMapaApliPagos("observaciones_"+indiceAprobacion);
      report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
      
      report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
	  report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);
	  
	  if(numRows > 0)
	  {
		  //---------------Se agrega la fila de Valor Total Pagos -------------------------//
		  datos[con] = "                                        VALOR TOTAL PAGOS";
		  con++;
		  datos[con] = UtilidadTexto.formatearValores(forma.getMapaApliPagos("valor_pagos_"+indiceAprobacion)+"",2,true,false);
		  						
		  
	  //---Colocar los datos al reporte (El detalle la inforamcion del cargue).
	  report.createColSectionSinTituloAlignColsData("pagos", titulosCol, datos, alineacionCols, 4);
	  
	  //-- Establecer el tamaño de cada columna.
	  	float widths[]={(float) 4,(float) 4};
	    try {
			report.getSectionReference("pagos").getTableReference("parentTable").setWidths(widths);
			} 
	    catch (BadElementException e) 
	    	{ e.printStackTrace();	}
	    
	    report.addSectionToDocument("pagos");
	  }
	       
	  cad =  "";
      report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,70);
      
	  cad =  "\t               ________________________                                  ________________________";
      report.document.addPhrase(cad);		
      
      cad =  "";
      report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,20);
      
      cad =  "\t               Elaborado                                                                 Revisado";
      report.document.addPhrase(cad);
	    
	    //-- Cerrar el reporte 
        report.closeReport();		
	}
	
	/**
	 * Metodo para generar el PDF de la consulta de
	 * aplicación de pago
	 * @param filename
	 * @param forma
	 * @param usuario
	 * @param con
	 */
	public static void pdfConsultaAplicacionPago(String filename, PagosCapitacionForm forma, UsuarioBasico usuario, Connection conec, HttpServletRequest request) 
	{
		//-- Variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        
        //-Crear el String para enviar los datos al reporte 	
        int numRows = UtilidadCadena.noEsVacio(forma.getMapaDetallePagos("numRegistros")+"") ? Integer.parseInt(forma.getMapaDetallePagos("numRegistros")+"") : 0;
        
        int filCero = 0, con = 0;
        int indiceImprimirPago=forma.getIndiceImprimirPago();
        
		//-Verificar si algun valor de pago es igual a cero-- Para No Imprimir Este.
		for(int i=0; i<numRows; i++)
		{ 
			String valor =forma.getMapaDetallePagos("valor_pago_"+i)+"";
			if ( Float.parseFloat(valor) == 0 )
			{
				filCero++;	
			}
		}
        
        String[] datos = new String[(numRows+1-filCero) * 2];
		
		
		
		//--------Se guarda en datos lo que se va ha mostrar -------------//
		for(int i=0; i<numRows; i++)
		{ 
			String valor =forma.getMapaDetallePagos("valor_pago_"+i)+"";
			if ( Float.parseFloat(valor) != 0 )
			{
				datos[con] =  forma.getMapaDetallePagos("cuenta_cobro_capitacion_"+i)+"";		 	
				con++;
				datos[con] = UtilidadTexto.formatearValores(valor,2,true,false);
				con++;	 
			}	
		}
		
		//-- Titulos de la tabla  
		String[] titulosCol = {"Cuenta Cobro","Valor Pago"};
		
		//--Esta alineación corresponde a cada una de las  columnas especificadas para los títulos
		int[] alineacionCols = {iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_RIGHT};
		
       //-- Tomando la ruta definida en el web.xml para generar el reporte.
       String filePath=ValoresPorDefecto.getFilePath();
       
       //-- Consultar la informacion de la Empresa.
       InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
       
       //-- Titulo del reporte
       String tituloReporte="  CONSULTA DE PAGO";
       
       //------Encabezado de la página ---------------------//
       String encabezado =  " " + institucionBasica.getRazonSocial() + "  \n " +institucionBasica.getTipoIdentificacion()+" "+institucionBasica.getNit() + 
       						"\n Dirección " + institucionBasica.getDireccion() +  "  \n" +" Telefono " + institucionBasica.getTelefono() +  "  ";
       
       //Generacion de la cabecera, validando el tipo de separador e utilizado en el path
       report.setReportBaseHeader1(institucionBasica,"align-left" , tituloReporte);
       
       //-----------------------------------------Abrir Reporte------------------------------------------------------------
       report.openReport(filename);
       report.font.useFont(iTextBaseFont.FONT_TIMES, true, true, true);
       
       //-------------------------------- Información del detalle de pago ----------------------------------------------
       report.font.setFontSizeAndAttributes(11,true,false,false);
       
       String cad = "(" + forma.getMapaConsultaPagos("acronimo_tipo_documento_"+indiceImprimirPago) + ") No. " + forma.getMapaConsultaPagos("documento_"+indiceImprimirPago);
		  cad += "  Aplicación No. "+forma.getMapaConsultaPagos("numero_aplicacion_"+indiceImprimirPago)+"        Estado: "+forma.getMapaConsultaPagos("descripcion_estado_"+indiceImprimirPago);  
		  report.document.addParagraph(cad,report.font,iTextBaseDocument.ALIGNMENT_LEFT,35);
		  
	  cad =  " ELABORADO Fecha: " +UtilidadFecha.conversionFormatoFechaAAp(forma.getMapaConsultaPagos("fecha_elaboracion_"+indiceImprimirPago)+"")+ "        Usuario: " + usuario.getNombreUsuario();
	  report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);

	  cad =  " Convenio: " + forma.getMapaConsultaPagos("nombre_convenio_"+indiceImprimirPago);
	  report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
	  
	  cad =  " Observaciones: " + forma.getMapaConsultaPagos("observaciones_"+indiceImprimirPago);
      report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_LEFT,20);
      
      report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
	  report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 11);
	  
	  if(numRows > 0)
	  {
	  //---------------Se agrega la fila de Valor Total Pagos -------------------------//
	  datos[con] = "                                        VALOR TOTAL PAGOS";
	  con++;
	  datos[con] =UtilidadTexto.formatearValores(forma.getMapaConsultaPagos("valor_pagos_"+indiceImprimirPago)+"",2,true,false); 
		  
	  //---Colocar los datos al reporte (El detalle la inforamcion del cargue).
	  report.createColSectionSinTituloAlignColsData("pagos", titulosCol, datos, alineacionCols, 4);
	  
	  //-- Establecer el tamaño de cada columna.
	  	float widths[]={(float) 4,(float) 4};
	    try {
			report.getSectionReference("pagos").getTableReference("parentTable").setWidths(widths);
			} 
	    catch (BadElementException e) 
	    	{ e.printStackTrace();	}
	    
	    report.addSectionToDocument("pagos");
	  }
	       
	  cad =  "";
      report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,70);
	  
      cad =  "\t               ________________________                                  ________________________";
      report.document.addPhrase(cad);		
      
      cad =  "";
      report.document.addParagraph(cad, report.font,iTextBaseDocument.ALIGNMENT_CENTER,20);
      
      cad =  "\t               Elaborado                                                                 Revisado";
      report.document.addPhrase(cad);
	    
	    //-- Cerrar el reporte 
        report.closeReport();		
	}

}
