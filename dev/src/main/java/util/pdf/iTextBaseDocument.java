/*
 * @(#)iTextBaseDocument.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package util.pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


/**
  Esta clase encapsula las propiedades y m&eacute;todos comunes para la manipulaci&oacute;n de documentos en formato pdf

  @author   Miguel Arturo D&iacute;az L&oacute;pez
  @version  1.0
*/

public class iTextBaseDocument {

	/**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(iTextBaseDocument.class);
  // definici&oacute;n de constantes publicas para alineaci&oacute;n de texto, im&aacute;genes, tablas y celdas

  /** alineaci&oacute;n horizontal a la izquierda */
  public static final int ALIGNMENT_LEFT     = Element.ALIGN_LEFT;
  /** alineaci&oacute;n horizontal a la derecha */
  public static final int ALIGNMENT_RIGHT    = Element.ALIGN_RIGHT;
  /** alineaci&oacute;n horizontal al centro  */ 
  public static final int ALIGNMENT_CENTER   = Element.ALIGN_CENTER;

  /** alineaci&oacute;n vertical al borde superior */
  public static final int ALIGNMENT_TOP      = Element.ALIGN_TOP;
  /** alineaci&oacute;n vertical al borde inferior */
  public static final int ALIGNMENT_BOTTOM   = Element.ALIGN_BOTTOM ;
  /** alineaci&oacute;n vertical al centro */
  public static final int ALIGNMENT_MIDDLE   = Element.ALIGN_MIDDLE;

  /** alineaci&oacute;n al borde de linea  */
  public static final int ALIGNMENT_BASELINE = Element.ALIGN_BASELINE;
  /** alineaci&oacute;n de texto justificado ( sin incluir la ultima linea ) */
  public static final int ALIGNMENT_JUSTIFIED = Element.ALIGN_JUSTIFIED;
  /** alineaci&oacute;n de texto justificado ( todas las lineas ) */
  public static final int ALIGNMENT_JUSTIFIED_ALL = Element.ALIGN_JUSTIFIED_ALL;


  // definici&oacute;n de constantes publicas para definir los tama&ntilde;os de pagina est&aacute;ndar de documentos

  /** tama&ntilde;o de pagina A0 */
  public static final Rectangle PAGE_SIZE_A0         = PageSize.A0;
  /** tama&ntilde;o de pagina A1 */
  public static final Rectangle PAGE_SIZE_A1         = PageSize.A1;
  /** tama&ntilde;o de pagina A2 */
  public static final Rectangle PAGE_SIZE_A2         = PageSize.A2;
  /** tama&ntilde;o de pagina A3 */
  public static final Rectangle PAGE_SIZE_A3         = PageSize.A3;
  /** tama&ntilde;o de pagina A4 */
  public static final Rectangle PAGE_SIZE_A4         = PageSize.A4;
  /** tama&ntilde;o de pagina A5 */
  public static final Rectangle PAGE_SIZE_A5         = PageSize.A5;
  /** tama&ntilde;o de pagina A6 */
  public static final Rectangle PAGE_SIZE_A6         = PageSize.A6;
  /** tama&ntilde;o de pagina A7 */
  public static final Rectangle PAGE_SIZE_A7         = PageSize.A7;
  /** tama&ntilde;o de pagina A8 */
  public static final Rectangle PAGE_SIZE_A8         = PageSize.A8;
  /** tama&ntilde;o de pagina A9 */
  public static final Rectangle PAGE_SIZE_A9         = PageSize.A9;
  /** tama&ntilde;o de pagina A10 */
  public static final Rectangle PAGE_SIZE_A10        = PageSize.A10;
  /** tama&ntilde;o de pagina B0 */
  public static final Rectangle PAGE_SIZE_B0         = PageSize.B0;
  /** tama&ntilde;o de pagina B1 */
  public static final Rectangle PAGE_SIZE_B1         = PageSize.B1;
  /** tama&ntilde;o de pagina B2 */
  public static final Rectangle PAGE_SIZE_B2         = PageSize.B2;
  /** tama&ntilde;o de pagina B3 */
  public static final Rectangle PAGE_SIZE_B3         = PageSize.B3;
  /** tama&ntilde;o de pagina B4 */
  public static final Rectangle PAGE_SIZE_B4         = PageSize.B4;
  /** tama&ntilde;o de pagina B5 */
  public static final Rectangle PAGE_SIZE_B5         = PageSize.B5;
  /** tama&ntilde;o de pagina NOTA */
  public static final Rectangle PAGE_SIZE_NOTE       = PageSize.NOTE;
  /** tama&ntilde;o de pagina LEGAL */
  public static final Rectangle PAGE_SIZE_LEGAL      = PageSize.LEGAL;
  /** tama&ntilde;o de pagina CARTA */
  public static final Rectangle PAGE_SIZE_LETTER     = PageSize.LETTER;
  /** tama&ntilde;o de pagina LEDGER */
  public static final Rectangle PAGE_SIZE_LEDGER     = PageSize.LEDGER;
  /** tama&ntilde;o de pagina  MITAD CARTA */
  public static final Rectangle PAGE_SIZE_HALFLETTER = PageSize.HALFLETTER;


  /** objeto Document para manejo interno del documento */
  private Document document;

  /** objeto PdfWriter para manejo del archivo del documento */
  private PdfWriter writer;



  /** constructor por defecto */
  public iTextBaseDocument(){
    document = new Document();
    
  }


  /** 
      constructor que toma en cuenta el tama&ntilde;o de pagina del documento 
      @param pageSize  objeto de tipo Rectangle que contiene las dimensiones del documento
  */
  public iTextBaseDocument(Rectangle pageSize){
    document = new Document(pageSize);
  }


  /** 
      crea un nuevo documento con un tama&ntilde;o de pagina determinado 
      @param pageSize  objeto de tipo Rectangle que contiene 
                       las dimensiones del documento
  */
  public void createDocument(Rectangle pageSize){
    document = new Document(pageSize);
  }


  /**
      establece el listener de eventos de pagina utilizado por la clase
      @param pageEventListener  objeto de tipo PdfPageEventListener que sera 
                                el encargado de escuchar los eventos y dar respuesta apropiada
                                a ellos
  */
  public void setPageEventListener(PdfPageEventListener pageEventListener){
    this.writer.setPageEvent(pageEventListener); 
  }


  /**
      obtiene una referencia al PdfWriter de este documento
      @return la referencia utilizada por este documento
  */
  public PdfWriter getPdfWriterReference(){
    return this.writer;
  }


  /**
      obtiene una referencia al objeto Document de esta clase
      @return la referencia utilizada por este documento
  */
  public Document getDocumentReference(){
    return this.document;
  }



  /** 
      define el archivo que se va a generar con el documento
      @param filename  la ruta y nombre del archivo que sera generado por este documento
  */
  public void useDocument(String filename){
   try {
     // creamos un listener al objeto document
     writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
   }

    catch(DocumentException e) {
    	logger.error("Error en useDocument: "+e);
      e.getMessage();
    }
    catch(IOException e) 
    {
    	logger.error("Error en useDocument: "+e);
      e.getMessage();
    }
  }



  /**
      establece el tama&ntilde;o de la pagina del documento actual. El tama&ntilde;o sera efectivo a partir 
      de la pr&oacute;xima pagina

      @param pageSize  objeto de tipo Rectangle que contiene 
                       las dimensiones del documento
  */
  public void setDocumentPageSize(Rectangle pageSize){
    document.setPageSize(pageSize);
  }


  /**
      establece las margenes para este documento; la unidad de medida utilizada para las margenes sera la de punto tipogr&aacute;fico, el cual tiene una equivalencia de 1/72 de pulgada. Las margenes ser&aacute;n efectivas de inmediato

      @param marginLeft    la margen izquierda
      @param marginRight   la margen derecha
      @param marginTop     la margen superior
      @param marginBottom  la margen inferior
  */
  public void setDocumentMargins(float marginLeft, float marginRight, float marginTop, float marginBottom){
    document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
  }


  /**
      abre el documento y lo deja listo para adicionar contenidos
  */
  public void openDocument(){
    document.open();
  }


  /**
      cierra el documento, escribiendo el mapa del mismo en el archivo que se especific&oacute;
  */
  public void closeDocument(){
    document.close();
  }


  /**
      adiciona informaci&oacute;n relacionada con el documento ( en modo pagina de propiedades del documento )

      @param title    titulo del documento
      @param subject  tema del documento
      @param creator  creador del documento
      @param author   autor del documento
  */
  public void addDocumentInfo(String title, String subject, String creator, String author){
    document.addTitle(title);
    document.addSubject(subject);
    document.addCreator(creator);
    document.addAuthor(author);
  }



  /**
      adiciona una nueva pagina al documento actual
  */
  public void addNewPage(){
      document.newPage();
  }



  /**
      adiciona la cabecera de pagina al documento

      @param headerText  cadena de texto que contiene la informaci&oacute;n de la cabecera
  */
  public void addHeader(String headerText){
    document.setHeader(new HeaderFooter(new Phrase(headerText), false));
  }
   

  /**
      adiciona la cabecera de pagina al documento

      @param headerText  cadena de texto que contiene la informaci&oacute;n de la cabecera
      @param iFont       fuente a ser utilizada al imprimir la cabecera en el documento
  */
  public void addHeader(String headerText, iTextBaseFont iFont){
    Font headerFont = null;

    headerFont = iFont.cloneCurrentFont();
    document.setHeader(new HeaderFooter(new Phrase(headerText, headerFont), false));
  }


  /**
      adiciona la cabecera de pagina al documento

      @param headerText  cadena de texto que contiene la informaci&oacute;n de la cabecera
      @param alignment   alineaci&oacute;n horizontal de la cabecera
      @param numbered    valor boolean que seg&uacute;n su valor indica: true - la cabecera debe insertar los numeros de pagina; false - no debe insertar n&uacute;meros de pagina
      @param hasBorder   valor boolean que seg&uacute;n su valor indica: true - la cabecera tiene borde; false - la cabecera no tiene borde 
  */
  public void addHeader(String headerText, int alignment, boolean numbered, boolean hasBorder){
    HeaderFooter header;

    header = new HeaderFooter(new Phrase(headerText), numbered);
    header.setAlignment(alignment);
    if(hasBorder == false)
      header.setBorder(Rectangle.NO_BORDER);
    document.setHeader(header);
  }


  /**
      adiciona la cabecera de pagina al documento

      @param headerText  cadena de texto que contiene la informaci&oacute;n de la cabecera
      @param iFont       fuente a ser utilizada al imprimir la cabecera en el documento
      @param alignment   alineaci&oacute;n horizontal de la cabecera
      @param numbered    valor boolean que seg&uacute;n su valor indica: true - la cabecera debe insertar los numeros de pagina; false - no debe insertar n&uacute;meros de pagina
      @param hasBorder   valor boolean que seg&uacute;n su valor indica: true - la cabecera tiene borde; false - la cabecera no tiene borde 
  */
  public void addHeader(String headerText, iTextBaseFont iFont, int alignment, boolean numbered, boolean hasBorder){
    HeaderFooter header;
    Font header_font = null;

    header_font = iFont.cloneCurrentFont();
    header = new HeaderFooter(new Phrase(headerText, header_font), numbered);
    header.setAlignment(alignment);
    if(hasBorder == false)
      header.setBorder(Rectangle.NO_BORDER);
    document.setHeader(header);
  }



  /**
      adiciona el pie de pagina al documento

      @param footerText  cadena de texto que contiene la informaci&oacute;n del pie de pagina
  */
  public void addFooter(String footerText){
    document.setHeader(new HeaderFooter(new Phrase(footerText), false));
  }


  /**
      adiciona el pie de pagina al documento

      @param footerText  cadena de texto que contiene la informaci&oacute;n del pie de pagina
      @param iFont       fuente a ser utilizada al imprimir la cabecera en el documento
  */
  public void addFooter(String footerText, iTextBaseFont iFont){
    Font footer_font = null;

    footer_font = iFont.cloneCurrentFont();
    document.setFooter(new HeaderFooter(new Phrase(footerText, footer_font), false));
  }


  /**
      adiciona el pie de pagina al documento

      @param footerText  cadena de texto que contiene la informaci&oacute;n del pie de pagina
      @param alignment   alineaci&oacute;n horizontal del pie de pagina
      @param numbered    valor boolean que seg&uacute;n su valor indica: true - el pie de pagina debe insertar los n&uacute;meros de pagina; false - no debe insertar n&uacute;meros de pagina
      @param hasBorder   valor boolean que seg&uacute;n su valor indica: true - el pie de pagina tiene borde; false - el pie de pagina no tiene borde 
  */
  public void addFooter(String footerText, int alignment, boolean numbered, boolean hasBorder){
    HeaderFooter footer;

    footer = new HeaderFooter(new Phrase(footerText), numbered);
    footer.setAlignment(alignment);
    if(hasBorder == false)
      footer.setBorder(Rectangle.NO_BORDER);
    document.setFooter(footer);
  }


  /**
      adiciona el pie de pagina al documento

      @param footerText  cadena de texto que contiene la informacion del pie de pagina
      @param iFont       fuente a ser utilizada al imprimir la cabecera en el documento
      @param alignment   alineaci&oacute;n horizontal del pie de pagina
      @param numbered    valor boolean que seg&uacute;n su valor indica: true - el pie de pagina debe insertar los n&uacute;meros de pagina; false - no debe insertar n&uacute;meros de pagina
      @param hasBorder   valor boolean que seg&uacute;n su valor indica: true - el pie de pagina tiene borde; false - el pie de pagina no tiene borde 
  */
  public void addFooter(String footerText, iTextBaseFont iFont, int alignment, boolean numbered, boolean hasBorder){
    HeaderFooter footer;
    Font footer_font = null;

    footer_font = iFont.cloneCurrentFont();
    footer = new HeaderFooter(new Phrase(footerText, footer_font), numbered);
    footer.setAlignment(alignment);
    if(hasBorder == false)
      footer.setBorder(Rectangle.NO_BORDER);
    document.setFooter(footer);
  }



  /** 
      obtiene el numero de pagina actual del documento
      @return el numero de pagina
  */
  public int getPageNumber(){
    return this.document.getPageNumber();    
  }


  /**
      adiciona una imagen al documento

      @param imgFilename  ruta y nombre del archivo que contiene la imagen
  */
  public void addImage(String imgFilename){
    try{
      Image img = Image.getInstance(imgFilename);
      document.add(img);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(java.net.MalformedURLException e){
        e.getMessage();
    }
    catch(IOException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }

  }
   

  /**
      adiciona una imagen al documento

      @param imgFilename  ruta y nombre del archivo que contiene la imagen
      @param aligment     alineaci&oacute;n horizontal de la imagen al ser insertada en el documento
  */
  public void addImage(String imgFilename, int aligment){
    try{
      Image img = Image.getInstance(imgFilename);
      img.setAlignment(aligment);
      document.add(img);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(java.net.MalformedURLException e){
        e.getMessage();
    }
    catch(IOException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }


  /**
      adiciona una imagen al documento

      @param imgFilename  ruta y nombre del archivo que contiene la imagen
      @param absoluteX    coordenada absoluta en x, en donde se debe posicionar la imagen
      @param absoluteY    coordenada absoluta en y, en donde se debe posicionar la imagen
  */
  public void addImage(String imgFilename, int absoluteX, int absoluteY){
    try{
      Image img = Image.getInstance(imgFilename);
      img.setAbsolutePosition(absoluteX, absoluteY);
      document.add(img);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(java.net.MalformedURLException e){
        e.getMessage();
    }
    catch(IOException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }

  /**
  adiciona una imagen al documento y la escala a un tamaño definido

  @param imgFilename  ruta y nombre del archivo que contiene la imagen
  @param absoluteX    valor para escalar en x
  @param absoluteY    valor para escalar en y
*/
  public void addImageWithSize(String imgFilename, long width, long height){
	  try{
		  Image img = Image.getInstance(imgFilename);
		  img.scaleAbsolute(width, height);
		  document.add(img);
	  }
	  catch(BadElementException e){
		  e.getMessage();
	  }
	  catch(java.net.MalformedURLException e){
		  e.getMessage();
	  }
	  catch(IOException e){
		  e.getMessage();
	  }
	  catch(DocumentException e){
		  e.getMessage();
	  }
  }



  /**
      adiciona una frase en el documento actual

      @param  text  texto de la frase
  */
  public void addPhrase(String text){
    try {
      Phrase phrase = new Phrase(text);
      document.add(phrase);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }


  /**
      adiciona una frase en el documento actual

      @param  text  texto de la frase
      @param iFont  fuente a ser utilizada al imprimir la frase en el documento
  */
  public void addPhrase(String text, iTextBaseFont iFont){
    try {
      Phrase phrase = new Phrase(text, iFont.getCurrentFont());
      document.add(phrase);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }



  /**
      adiciona una p&aacute;rrafo en el documento actual

      @param text     texto del p&aacute;rrafo
      @param leading  separaci&oacute;n entre este objeto y el anterior
  */
  public void addParagraph(String text, float leading){
    try {
      Paragraph paragraph = new Paragraph(text);
      paragraph.setLeading(leading);
      document.add(paragraph);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }



  /**
      adiciona una p&aacute;rrafo en el documento actual

      @param text     texto del p&aacute;rrafo
      @param iFont    fuente a ser utilizada al imprimir el p&aacute;rrafo en el documento
      @param leading  separaci&oacute;n entre este objeto y el anterior
  */
  public void addParagraph(String text, iTextBaseFont iFont, float leading){
    try {
      Paragraph paragraph = new Paragraph(text, iFont.getCurrentFont());
      paragraph.setLeading(leading);
      document.add(paragraph);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }


  /**
  adiciona una p&aacute;rrafo en el documento actual

  @param text         texto del p&aacute;rrafo
  @param iFont       fuente a ser utilizada al imprimir el p&aacute;rrafo en el documento
  @param aligment  alineaci&oacute;n del texto de este parrafo
  @param leading    separaci&oacute;n entre este objeto y el anterior

**/
public void addParagraph(String text, iTextBaseFont iFont, int aligment, float leading){
try {
  Paragraph paragraph = new Paragraph(text, iFont.getCurrentFont());
  paragraph.setLeading(leading);
  paragraph.setAlignment(aligment);
  document.add(paragraph);
}
catch(BadElementException e){
    e.getMessage();
}
catch(DocumentException e){
    e.getMessage();
}
}

  
  
  /**
      adiciona la tabla padre de un objeto tipo iTextBaseTable 

      @param iTable  objeto iTextBaseTable contenedor de las tablas
  */
  public void addTable(iTextBaseTable iTable){
    Table rTable = null;

    try {

      rTable = iTable.getTableReference(iTable.getTableAliasArray()[0]);
      document.add(rTable);
    }
    catch(BadElementException e){
        e.getMessage();
    }
    catch(DocumentException e){
        e.getMessage();
    }
    catch (Exception e){
        e.getMessage();
    }
    
  }
  
  
  
  /**
  adiciona la tabla padre de un objeto tipo iTextBaseTable 

  @param iTable  objeto iTextBaseTable contenedor de las tablas
*/
public void addTable(PdfPTable iTable){
			Table rTable = null;
			
			try {
			
			 // rTable = iTable.getTableReference(iTable.getTableAliasArray()[0]);
			  document.add(iTable);
			}
			catch(BadElementException e){
			      e.getMessage();
			}
			catch(DocumentException e){
			      e.getMessage();
			}
			catch (Exception e){
			      e.getMessage();
			}
			
			}
  
  
  

}
