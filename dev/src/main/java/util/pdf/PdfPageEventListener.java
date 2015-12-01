/*
 * @(#)PdfPageEventListener.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package util.pdf;



import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


/**
  Esta clase sobrecarga los m&eacute;todos necesarios de la clase que escucha los eventos de pagina,
  con el fin de poder adicionar tablas en las cabeceras de pagina y poder fijar un pie de 
  pagina mas adecuado en los documentos

  @author   Miguel Arturo D&iacute;az L&oacute;pez
  @version  1.0
*/

public class PdfPageEventListener extends PdfPageEventHelper{

  /** referencia al reporte que podr&aacute; actuar en cada evento de paginas */
  private PdfReports lPdfReports;

  /** plantilla para fabricar el pie de pagina */
  private PdfTemplate template;

  /** objeto contenedor de los bytes del writer del documento */
  private PdfContentByte cb;
    
  /** fuente base que sera utilizada al imprimir el pie de pagina en el documento */
  BaseFont bf;



  /**
      constructor por defecto
  */
  public PdfPageEventListener(PdfReports pdfReports){
    super();
    lPdfReports = pdfReports;
  }



  /**
      sobrecarga del m&eacute;todo onOpenDocument para inicializar valores

      @param writer    writer del documento
      @param document  documento
  */
  public void onOpenDocument(PdfWriter writer, Document document) {

    cb = writer.getDirectContent();
    template = cb.createTemplate(50, 50);
    bf = lPdfReports.font.getCurrentBaseFont();
  }



  /**
      sobrecarga del m&eacute;todo onCloseDocument para establecer adecuadamente 
      el numero de paginas del reporte

      @param writer    writer del documento
      @param document  documento
  */
  public void onCloseDocument(PdfWriter writer, Document document){

      template.beginText();
      template.setFontAndSize(bf, 10);
      template.showText(String.valueOf(writer.getPageNumber()));
      template.endText();
  }



  /**
      sobrecarga del m&eacute;todo onStartPage el cual es disparado al comenzar la adici&oacute;n de una 
      pagina al documento

      @param writer    writer del documento
      @param document  documento
  */
  public void onStartPage(PdfWriter writer, Document document){
    String[] sectionAlias;
    iTextBaseTable section;
    int iter;


    sectionAlias = lPdfReports.getSectionAliasArray();
    for(iter=0; iter < sectionAlias.length; iter++){
      section = lPdfReports.getSectionReference(sectionAlias[iter]);
      if(section.getTableReference(PdfReports.REPORT_HEADER_TABLE) != null)
        lPdfReports.addSectionToDocument(sectionAlias[iter]);
    }
  }



  
  /**
  sobrecarga del m&eacute;todo onEndPage el cual es disparado al finalizar la adici&oacute;n de una 
  pagina al documento

  @param writer    writer del documento
  @param document  documento
*/
public void onEndPage(PdfWriter writer, Document document) 
{
	if(lPdfReports.getFormatoImpresion().equals(ConstantesIntegridadDominio.acronimoSuba))
	{
		String text = "www.hospitaldesuba.gov.co facturacion@esesuba.gov.co";
		
		float len = bf.getWidthPoint(text, 7);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 7);
		cb.setTextMatrix(80, 20);
		cb.showText(text);
		cb.endText();
		
		text = lPdfReports.getMapaPiePagina("infoInstitucion").toString();
		
		len = bf.getWidthPoint(text, 7);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 7);
		cb.setTextMatrix(80, 28);
		cb.showText(text);
		cb.endText();
		
		//cb.addTemplate(template, 100 + len, 60);
		try 
		{
			cb.addImage(Image.getInstance(lPdfReports.getMapaPiePagina("filePathLogo").toString()),185,0,0,67,320,10);
		} 
		catch (Exception e) {
		      e.getMessage();
		} 
		//cb.restoreState();
	}
	else if(lPdfReports.getFormatoImpresion().equals("infoPiePaginaInicioMargen"))
	{
		int pageN = writer.getPageNumber();
		String text = lPdfReports.releaseDate + " - " + "P�gina " + pageN + " de ";
		float len = bf.getWidthPoint(text, 10);
		
		
		cb.beginText();
		cb.setFontAndSize(bf, 10);
		cb.setTextMatrix(220, 770);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(template, 220 + len, 770);
	}
	else
	{
		// Longitud total del documento
		float widthTotal = document.getPageSize().getWidth();
		
		//*************** Imprimir Fecha
		String fecha  = "Fecha: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual();
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		// Coordenadas
		cb.setTextMatrix(30, 30);
		cb.showText(fecha);
		cb.endText();
		
		//*************** Imprimir Usuario
		String usuario = "Usuario: ";
		if (UtilidadCadena.noEsVacio(lPdfReports.getUsuario()))	
			usuario += lPdfReports.getUsuario();
		// Longitud del label usuario
		float widthUsuario = bf.getWidthPoint(usuario, 9);
		// Calculo de la coordenada inicial para ubicar el label de usuario
		float coordUsuario = (widthTotal/2)-(widthUsuario/2);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		// Coordenadas
		cb.setTextMatrix(coordUsuario, 30);
		cb.showText(usuario);
		cb.endText();
		
		//*************** Imprimir Página
		int pageN = writer.getPageNumber();
		String pagina = "P�gina " + pageN + " de ";
		// Longitud del label pagina
		float widthPageN = bf.getWidthPoint(pagina, 9);
		// Calculo de la coordenada inicial para ubicar el label de usuario
		float coordPagina = widthTotal - (widthPageN + 30);
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		cb.setTextMatrix(coordPagina, 30);
		cb.showText(pagina);
		cb.endText();
		// Adicionar el numero total de páginas
		cb.addTemplate(template, widthTotal-30, 30);
	}
}

/**
sobrecarga del m&eacute;todo onEndPage el cual es disparado al finalizar la adici&oacute;n de una 
pagina al documento

@param writer    writer del documento
@param document  documento
*/
public void onEndPage(PdfWriter writer, Document document, String texto) 
{
	if(lPdfReports.getFormatoImpresion().equals(ConstantesIntegridadDominio.acronimoSuba))
	{
		String text = "www.hospitaldesuba.gov.co facturacion@esesuba.gov.co";
		
		float len = bf.getWidthPoint(text, 7);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 7);
		cb.setTextMatrix(80, 20);
		cb.showText(text);
		cb.endText();
		
		text = lPdfReports.getMapaPiePagina("infoInstitucion").toString();
		
		len = bf.getWidthPoint(text, 7);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 7);
		cb.setTextMatrix(80, 28);
		cb.showText(text);
		cb.endText();
		
		//cb.addTemplate(template, 100 + len, 60);
		try 
		{
			cb.addImage(Image.getInstance(lPdfReports.getMapaPiePagina("filePathLogo").toString()),185,0,0,67,320,10);
		} 
		catch (Exception e) {
		      e.getMessage();
		} 
		//cb.restoreState();
	}
	else if(lPdfReports.getFormatoImpresion().equals("infoPiePaginaInicioMargen"))
	{
		int pageN = writer.getPageNumber();
		String text = lPdfReports.releaseDate + " - " + "Página " + pageN + " de ";
		float len = bf.getWidthPoint(text, 10);
		
		
		cb.beginText();
		cb.setFontAndSize(bf, 10);
		cb.setTextMatrix(220, 770);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(template, 220 + len, 770);
	}
	else
	{
		// Longitud total del documento
		float widthTotal = document.getPageSize().getWidth();
		
		if(!texto.isEmpty()){
//			cb.saveState();
			cb.beginText();
			cb.setFontAndSize(bf, 9);
			// Longitud del texto
			float widthTexto = bf.getWidthPoint(texto, 9);
			// Calculo de la coordenada inicial para ubicar el texto opcional
			float coordTexto = (widthTotal/2)-(widthTexto/2);
			// Coordenadas
			cb.setTextMatrix(coordTexto, 39);
			cb.showText(texto);
			cb.endText();
		}
		
		//*************** Imprimir Fecha
		String fecha  = "Fecha: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual();
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		// Coordenadas
		cb.setTextMatrix(30, 30);
		cb.showText(fecha);
		cb.endText();
		
		//*************** Imprimir Usuario
		String usuario = "Usuario: ";
		if (UtilidadCadena.noEsVacio(lPdfReports.getUsuario()))	
			usuario += lPdfReports.getUsuario();
		// Longitud del label usuario
		float widthUsuario = bf.getWidthPoint(usuario, 9);
		// Calculo de la coordenada inicial para ubicar el label de usuario
		float coordUsuario = (widthTotal/2)-(widthUsuario/2);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		// Coordenadas
		cb.setTextMatrix(coordUsuario, 30);
		cb.showText(usuario);
		cb.endText();
		
		//*************** Imprimir Página
		int pageN = writer.getPageNumber();
		String pagina = "Página " + pageN + " de ";
		// Longitud del label pagina
		float widthPageN = bf.getWidthPoint(pagina, 9);
		// Calculo de la coordenada inicial para ubicar el label de usuario
		float coordPagina = widthTotal - (widthPageN + 30);
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		cb.setTextMatrix(coordPagina, 30);
		cb.showText(pagina);
		cb.endText();
		// Adicionar el numero total de páginas
		cb.addTemplate(template, widthTotal-30, 30);
	}
}
  
  /**
  sobrecarga del m&eacute;todo onEndPage que se llama cuando se cierra el documento Pdf

  @param writer    writer del documento
  @param document  documento
  @param texto
*/
public void onEndPage(PdfWriter writer, String texto) 
{
	if(lPdfReports.getFormatoImpresion().equals(ConstantesIntegridadDominio.acronimoSuba))
	{
		String text = "www.hospitaldesuba.gov.co facturacion@esesuba.gov.co";
		
		float len = bf.getWidthPoint(text, 7);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 7);
		cb.setTextMatrix(80, 20);
		cb.showText(text);
		cb.endText();
		
		text = lPdfReports.getMapaPiePagina("infoInstitucion").toString();
		
		len = bf.getWidthPoint(text, 7);
//		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 7);
		cb.setTextMatrix(80, 28);
		cb.showText(text);
		cb.endText();
		
		//cb.addTemplate(template, 100 + len, 60);
		try 
		{
			cb.addImage(Image.getInstance(lPdfReports.getMapaPiePagina("filePathLogo").toString()),185,0,0,67,320,10);
					//60,0,0,22,380,20);
		} 
		catch (Exception e) {
		      e.getMessage();
		} 
		//cb.restoreState();
	}
	else
	{
		int pageN = writer.getPageNumber();
		String text ="";
		if (UtilidadCadena.noEsVacio(lPdfReports.getUsuario()))	
			text = lPdfReports.getUsuario()+"  "+ lPdfReports.releaseDate + " - " + "Página " + pageN + " de ";
		else
			text = lPdfReports.releaseDate + " - " + "Página " + pageN + " de ";
		
		float len = bf.getWidthPoint(text, 10);
		
		
		cb.beginText();
		cb.setFontAndSize(bf, 10);
		cb.setTextMatrix(180, 30);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(template, 180 + len, 30);
	}
    
}



}


