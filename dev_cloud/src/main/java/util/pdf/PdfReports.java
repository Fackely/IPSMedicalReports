/*
 * @(#)PdfReports.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package util.pdf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;



/**
  Esta clase permite la creaci&oacute;n de reportes basados en secciones. Las secciones son agrupaciones 
  de tablas ( tipo iTextBaseTable ) que tienen caracter&iacute;sticas generales pero que de ser necesario
  pueden ser redefinidas

  @author   Miguel Arturo D&iacute;az L&oacute;pez
  @version  1.0
*/

public class PdfReports{

  /** objeto para manejo de fuentes del reporte */
  public iTextBaseFont font;

  /** objeto para manejo del documento */
  public iTextBaseDocument document;

  /** listener de los eventos de pagina desencadenados durante la escritura del documento */
  private PdfPageEventListener pageEventListener;


  /** HashMap contenedor de las secciones del reporte */
  private HashMap sections;
  /** ArrayList contenedor de los alias de las secciones a&ntilde;adidas al documento actual */
  private ArrayList aliasArrayList;
  /** &iacute;ndice de la secci&oacute;n a insertar actualmente en el ArrayList */
  private int aliasIndex;


  /** Array de Strings para persistencia de datos de las cabeceras al utilizar el metodo fixDataByHeaders y/o el metodo fixDataByData */ 
  public String[] rHeader;
  /** Array de Strings para persistencia de datos de la informacion al utilizar el metodo fixDataByHeaders y/o el metodo fixDataByData */
  public String[] rData;
  
  
  // Conjunto de atributos que definen las caracter&iacute;sticas del documento
  /** tama&ntilde;o de la pagina del reporte */
  private Rectangle reportPageSize;
  /** titulo del reporte ( solo como pagina de propiedades del reporte ) */
  private String    reportTitle;
  /** tema del reporte ( solo como pagina de propiedades del reporte ) */
  private String    reportSubject;
  /** creador del reporte ( solo como pagina de propiedades del reporte ) */
  private String    reportCreator;
  /** autor del reporte ( solo como pagina de propiedades del reporte ) */
  private String    reportAuthor;


  // Conjunto de colores que definen a las secciones del reporte
  /** color del borde de la barra de titulo de la secci&oacute;n */
  private int   sectionTitleBorderColor;
  /** color de fondo de la barra de titulo de la secci&oacute;n */
  private int   sectionTitleBackgroundColor;
  /** color de la fuente de la barra de titulo de la secci&oacute;n */
  private int   sectionTitleFontColor;
  /** tama&ntilde;o de la fuente de la barra de titulo de la secci&oacute;n */
  private float sectionTitleFontSize;

  /** color del borde del &aacute;rea de datos de la secci&oacute;n */
  private int   sectionInfoBorderColor;
  public int getSectionInfoBorderColor() {
	return sectionInfoBorderColor;
}

public void setSectionInfoBorderColor(int sectionInfoBorderColor) {
	this.sectionInfoBorderColor = sectionInfoBorderColor;
}


/** color de fondo del &aacute;rea de datos de la secci&oacute;n */
  private int   sectionInfoBackgroundColor;
  /** color de la fuente del &aacute;rea de datos de la secci&oacute;n */
  private int   sectionInfoFontColor;
  /** tama&ntilde;o de la fuente del &aacute;rea de datos de la secci&oacute;n */
  private float sectionInfoFontSize;
  
  /**
   * Variable que define el tipo de formato impresion
   */
  private String formatoImpresion;
  /**
   * Mapa para pasar parï¿½metros al pie de pagina
   */
  private HashMap mapaPiePagina = new HashMap();


  /** fecha y hora en la que se genera este reporte */
  public String releaseDate;
/**
 * el usuario que genera el reporte
 */
  public String usuario;
  // definici&oacute;n de constantes que definen los alias de las secciones internas comunes del reporte
  /** nombre de la secci&oacute;n que contiene la cabecera com&uacute;n del reporte */
  public static final String REPORT_HEADER_SECTION = "headerSection";

  // definici&oacute;n de constantes que definen los alias usados al crear las tablas de una secci&oacute;n cualquiera
  /** nombre de la tabla padre de la secci&oacute;n que se define como parte de la cabecera de pagina */
  public static final String REPORT_HEADER_TABLE = "headerTable";
  /** nombre de la tabla padre de la secci&oacute;n que se define generalmente en el reporte */
  public static final String REPORT_PARENT_TABLE = "parentTable";



  /**
      constructor por defecto
  */
  public PdfReports(){
    font = new iTextBaseFont();
    document = new iTextBaseDocument();
    sections = new HashMap();    
    aliasArrayList = new ArrayList();
    aliasIndex = 0;
    rHeader = null;
    rData = null;
    this.formatoImpresion = "";
    this.mapaPiePagina = new HashMap();
    this.usuario="";
    reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER;
    setReportMargins(2.0f, 2.0f, 3.0f, 3.0f);
    reportTitle = "";
    reportSubject = "";
    reportCreator = "";
    reportAuthor = "";

    sectionTitleBorderColor = 0xCCCCCC;
    sectionTitleBackgroundColor = 0xEBEBEB;
    sectionTitleFontColor = 0x000000;
    sectionTitleFontSize = 14f;

    sectionInfoBorderColor = 0xCCCCCC;
    sectionInfoBackgroundColor = 0xFFFFFF;
    sectionInfoFontColor = 0x000000;
    sectionInfoFontSize = 12f;
    
    Calendar rightNow = Calendar.getInstance();
    String horaAnterior=rightNow.get(Calendar.MINUTE)+"";
    String hora=(Integer.parseInt(horaAnterior)<10)?"0"+horaAnterior:horaAnterior;
    releaseDate =      rightNow.get(Calendar.DAY_OF_MONTH) + "/"   + 
                     ( rightNow.get(Calendar.MONTH) + 1 )  + "/"   +  
                       rightNow.get(Calendar.YEAR)         + " - " + 
                       rightNow.get(Calendar.HOUR_OF_DAY)  + ":"   +
                       hora   + ""    ;
  }
  
	/**
	 constructor por defecto
	*/
	public PdfReports(boolean mediaPagina)
	{
		font = new iTextBaseFont();
		document = new iTextBaseDocument();
		sections = new HashMap();    
		aliasArrayList = new ArrayList();
		aliasIndex = 0;
		rHeader = null;
		rData = null;
		this.formatoImpresion = "";
		this.mapaPiePagina = new HashMap();
		this.usuario="";
		if(mediaPagina)
			reportPageSize = iTextBaseDocument.PAGE_SIZE_HALFLETTER.rotate();
		else
			reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER;
		setReportMargins(2.0f, 2.0f, 2.0f, 2.0f);
		reportTitle = "";
		reportSubject = "";
		reportCreator = "";
		reportAuthor = "";
		
		sectionTitleBorderColor = 0xCCCCCC;
		sectionTitleBackgroundColor = 0xEBEBEB;
		sectionTitleFontColor = 0x000000;
		sectionTitleFontSize = 12f;
		
		sectionInfoBorderColor = 0xCCCCCC;
		sectionInfoBackgroundColor = 0xFFFFFF;
		sectionInfoFontColor = 0x000000;
		sectionInfoFontSize = 10f;
		
		Calendar rightNow = Calendar.getInstance();
		String horaAnterior=rightNow.get(Calendar.MINUTE)+"";
		String hora=(Integer.parseInt(horaAnterior)<10)?"0"+horaAnterior:horaAnterior;
		releaseDate =      rightNow.get(Calendar.DAY_OF_MONTH) + "/"   + 
		                 ( rightNow.get(Calendar.MONTH) + 1 )  + "/"   +  
		                   rightNow.get(Calendar.YEAR)         + " - " + 
		                   rightNow.get(Calendar.HOUR_OF_DAY)  + ":"   +
		                   hora   + ""    ;
	}
  
  
  
  /**
  constructor por defecto
*/
  public PdfReports(String mostraFechaHora)
  {
		font = new iTextBaseFont();
		document = new iTextBaseDocument();
		sections = new HashMap();    
		aliasArrayList = new ArrayList();
		aliasIndex = 0;
		this.formatoImpresion = "";
	    this.mapaPiePagina = new HashMap();
		rHeader = null;
		rData = null;
		this.usuario="";
		reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER;
		setReportMargins(2.0f, 2.0f, 3.0f, 3.0f);
		reportTitle = "";
		reportSubject = "";
		reportCreator = "";
		reportAuthor = "";
		
		sectionTitleBorderColor = 0xCCCCCC;
		sectionTitleBackgroundColor = 0xEBEBEB;
		sectionTitleFontColor = 0x000000;
		sectionTitleFontSize = 14f;
		
		sectionInfoBorderColor = 0xCCCCCC;
		sectionInfoBackgroundColor = 0xFFFFFF;
		sectionInfoFontColor = 0x000000;
		sectionInfoFontSize = 12f;
		releaseDate="";
		if(UtilidadTexto.getBoolean(mostraFechaHora))
		{
			Calendar rightNow = Calendar.getInstance();
			String horaAnterior=rightNow.get(Calendar.MINUTE)+"";
			String hora=(Integer.parseInt(horaAnterior)<10)?"0"+horaAnterior:horaAnterior;
			releaseDate =      rightNow.get(Calendar.DAY_OF_MONTH) + "/"   + 
			                 ( rightNow.get(Calendar.MONTH) + 1 )  + "/"   +  
			                   rightNow.get(Calendar.YEAR)         + " - " + 
			                   rightNow.get(Calendar.HOUR_OF_DAY)  + ":"   +
			                   hora   + ""    ;
		}
  }

  public PdfReports(String mostraFechaHora,boolean horizontal)
  {
		font = new iTextBaseFont();
		document = new iTextBaseDocument();
		sections = new HashMap();    
		aliasArrayList = new ArrayList();
		aliasIndex = 0;
		this.formatoImpresion = "";
	    this.mapaPiePagina = new HashMap();
		rHeader = null;
		rData = null;
		this.usuario="";
		if (horizontal)
		{
			reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER.rotate();
			setReportMargins(2.0f, 2.0f, 2.0f, 2.0f);
		}
		else
		{
			reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER;
			setReportMargins(2.0f, 2.0f, 3.0f, 3.0f);
		}
		
		reportTitle = "";
		reportSubject = "";
		reportCreator = "";
		reportAuthor = "";
		
		sectionTitleBorderColor = 0xCCCCCC;
		sectionTitleBackgroundColor = 0xEBEBEB;
		sectionTitleFontColor = 0x000000;
		sectionTitleFontSize = 14f;
		
		sectionInfoBorderColor = 0xCCCCCC;
		sectionInfoBackgroundColor = 0xFFFFFF;
		sectionInfoFontColor = 0x000000;
		sectionInfoFontSize = 12f;
		releaseDate="";
		if(UtilidadTexto.getBoolean(mostraFechaHora))
		{
			Calendar rightNow = Calendar.getInstance();
			String horaAnterior=rightNow.get(Calendar.MINUTE)+"";
			String hora=(Integer.parseInt(horaAnterior)<10)?"0"+horaAnterior:horaAnterior;
			releaseDate =      rightNow.get(Calendar.DAY_OF_MONTH) + "/"   + 
			                 ( rightNow.get(Calendar.MONTH) + 1 )  + "/"   +  
			                   rightNow.get(Calendar.YEAR)         + " - " + 
			                   rightNow.get(Calendar.HOUR_OF_DAY)  + ":"   +
			                   hora   + ""    ;
		}
  }

  /**
   * Metodo para mostrar el usuario en los reportes de iText
   * @param mostraFechaHora
   * @param horizontal
   * @param usuario
   */
  public PdfReports(String mostraFechaHora, boolean horizontal, String usuario)
  {
		font = new iTextBaseFont();
		document = new iTextBaseDocument();
		sections = new HashMap();    
		aliasArrayList = new ArrayList();
		aliasIndex = 0;
		this.formatoImpresion = "";
	    this.mapaPiePagina = new HashMap();
		rHeader = null;
		rData = null;
		this.usuario = usuario;
		if (horizontal)
		{
			reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER.rotate();
			setReportMargins(2.0f, 2.0f, 2.0f, 2.0f);
		}
		else
		{
			reportPageSize = iTextBaseDocument.PAGE_SIZE_LETTER;
			setReportMargins(2.0f, 2.0f, 3.0f, 3.0f);
		}
		
		reportTitle = "";
		reportSubject = "";
		reportCreator = "";
		reportAuthor = "";
		
		sectionTitleBorderColor = 0xCCCCCC;
		sectionTitleBackgroundColor = 0xEBEBEB;
		sectionTitleFontColor = 0x000000;
		sectionTitleFontSize = 14f;
		
		sectionInfoBorderColor = 0xCCCCCC;
		sectionInfoBackgroundColor = 0xFFFFFF;
		sectionInfoFontColor = 0x000000;
		sectionInfoFontSize = 12f;
		releaseDate="";
		if(UtilidadTexto.getBoolean(mostraFechaHora))
		{
			//String hora=(Integer.parseInt(horaAnterior)<10)?"0"+horaAnterior:horaAnterior;
			/*releaseDate =      rightNow.get(Calendar.DAY_OF_MONTH) + "/"   + 
			                 ( rightNow.get(Calendar.MONTH) + 1 )  + "/"   +  
			                   rightNow.get(Calendar.YEAR)         + " - " + 
			                   rightNow.get(Calendar.HOUR_OF_DAY)  + ":"   +
			                   hora   + ""    ;*/
			releaseDate = UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual();
		}
  }
  
  public String getUsuario() {
	return usuario;
}

public void setUsuario(String usuario) {
	this.usuario = usuario;
}

/**
      establece el titulo del reporte ( como pagina de propiedades del reporte )

      @param title  titulo del reporte
  */
  public void setReportTitle(String title){
    this.reportTitle = title;
  }


  /**
      establece el tema del reporte ( como pagina de propiedades del reporte )

      @param subject  tema del reporte
  */
  public void setReportSubject(String subject){
    this.reportSubject = subject;
  }


  /**
      establece el creador del reporte ( como pagina de propiedades del reporte )

      @param creator  creador del reporte
  */
  public void setReportCreator(String creator){
    this.reportCreator = creator;
  }


  /**
      establece el autor del reporte ( como pagina de propiedades del reporte )

      @param author  autor del reporte
  */
  public void setReportAuthor(String author){
    this.reportAuthor = author;
  }



  /**
      establece los atributos de la barra de titulo de la secci&oacute;n

      @param borderColor  valor entero que define las componentes RGB (formato 0xRRGGBB ) del color del borde de la barra de titulo de la secci&oacute;n
      @param bgColor      valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de fondo de la barra de titulo de la secci&oacute;n
      @param fontColor    valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de la fuente utilizada en la barra de titulo de la secci&oacute;n
      @param fontSize     tama&ntilde;o de la fuente de la barra de titulo de la secci&oacute;n
  */
  public void setReportTitleSectionAtributes(int borderColor, int bgColor, int fontColor, float fontSize){
    this.sectionTitleBorderColor = borderColor;
    this.sectionTitleBackgroundColor = bgColor;
    this.sectionTitleFontColor = fontColor;
    this.sectionTitleFontSize = fontSize;
  }



  /**
      establece los atributos del &aacute;rea de datos de la secci&oacute;n

      @param borderColor  valor entero que define las componentes RGB (formato 0xRRGGBB ) del color del borde del &aacute;rea de la secci&oacute;n
      @param bgColor      valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de fondo del &aacute;rea de la secci&oacute;n
      @param fontColor    valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de la fuente utilizada en el &aacute;rea de datos de la secci&oacute;n
      @param fontSize     tama&ntilde;o de la fuente del &aacute;rea de datos de la secci&oacute;n
  */
  public void setReportDataSectionAtributes(int borderColor, int bgColor, int fontColor, float fontSize){
    this.sectionInfoBorderColor = borderColor;
    this.sectionInfoBackgroundColor = bgColor;
    this.sectionInfoFontColor = fontColor;
    this.sectionInfoFontSize = fontSize;
  }


  /**
      establece las margenes del reporte. Las unidades usadas son pulgadas

      @param marginLeft    la margen izquierda
      @param marginRight   la margen derecha
      @param marginTop     la margen superior
      @param marginBottom  la margen inferior
  */
  public void setReportMargins(float marginLeft, float marginRight, float marginTop, float marginBottom){
    float mLeft, mRight, mTop, mBottom;

    // conversion de pulgadas a puntos
    mLeft   = marginLeft   * 72f;
    mRight  = marginRight  * 72f; 
    mTop    = marginTop    * 72f;
    mBottom = marginBottom * 72f;

    this.document.setDocumentMargins(mLeft, mRight, mTop, mBottom);
 }



  /**
      obtiene la referencia de una determinada secci&oacute;n

      @param sectionAlias  alias de la secci&oacute;n
  */

  public iTextBaseTable getSectionReference(String sectionAlias){
    iTextBaseTable iSection;

    iSection = (iTextBaseTable) this.sections.get(sectionAlias);
    return iSection;
  }



  /**
      abre el reporte el cual generara como salida un determinado archivo en formato pdf

      @param pdfReportFilename  ruta y nombre del archivo en formato pdf que generara el reporte
  */

  public void openReport(String pdfReportFilename){ 
	  
    this.pageEventListener = new PdfPageEventListener(this);

    this.document.createDocument(this.reportPageSize);
    this.document.useDocument(pdfReportFilename);    
	this.document.addDocumentInfo(reportTitle, reportSubject, reportCreator, reportAuthor);

    this.document.setPageEventListener(this.pageEventListener);
    this.document.openDocument();
  }




  /**
      cierra el reporte generando el archivo de salida en formato pdf
  */
  public void closeReport(){
    this.pageEventListener.onEndPage(this.document.getPdfWriterReference(), this.document.getDocumentReference());
    this.pageEventListener.onCloseDocument(this.document.getPdfWriterReference(), this.document.getDocumentReference());
    this.document.setPageEventListener(null);      
    this.document.closeDocument();
  }
  
  

  /**
   * Cierra el reporte generando el archivo de salida en formato pdf y coloca un texto especÃ­fico como pie de pÃ¡gina
   * @param texto
 */
 public void closeReport(String texto)
 {
	 this.pageEventListener.onEndPage(this.document.getPdfWriterReference(), this.document.getDocumentReference(), texto);
	 this.pageEventListener.onCloseDocument(this.document.getPdfWriterReference(), this.document.getDocumentReference());
	 this.document.setPageEventListener(null);      
	 this.document.closeDocument();
 }
 
  /**
   * imprime el reporte haciendo uso de la aplicaci&oacute;n  Acrobat Reader
   * @param acrobatHome           ruta y nombre donde se encuentra la aplicaci&oacute;n Acrobat Reader 
   * @param acrobatPrintCommand   par&aacute;metro que pondra en modo de impresi&oacute;n la aplicaci&oacute;n Acrobat Reader 
   * @param reportFilename        ruta y nombre del reporte en formato pdf que se desea imprimir
   */
  public void printReport(String acrobatHome, String acrobatPrintCommand, String reportFilename){
    Runtime rt = Runtime.getRuntime();
    
    try{
      //Process p = rt.exec(acrobatHome + " " + acrobatPrintCommand + " " + reportFilename);
      //p.waitFor(); 
        rt.exec(acrobatHome + " " + acrobatPrintCommand + " " + reportFilename);
    }
  	catch(Exception e){
  	  e.printStackTrace(); 
  	}
  }
  

  
  /**
   * imprime el reporte haciendo uso de la aplicaci&oacute;n  Acrobat Reader
   * @param acrobatHome           ruta y nombre donde se encuentra la aplicaci&oacute;n Acrobat Reader 
   * @param reportFilename        ruta y nombre del reporte en formato pdf que se desea imprimir
   */
  public void viewReport(String acrobatHome, String reportFilename){
    Runtime rt = Runtime.getRuntime();
    
    try{
      //La linea siguiente es la original, sin embargo
      //se quitï¿½ para evitar el Warning
      //Process p = rt.exec(acrobatHome + " " + reportFilename);
      //p.waitFor(); 
        rt.exec(acrobatHome + " " + reportFilename);
    }
  	catch(Exception e){
  	  e.printStackTrace(); 
  	}
  }
  

  /**
   * convierte un conjunto de datos almacenados en un ResultSetDecorator en un array de Strings, ordenados por registro y por columnas
   * @param selectedColumns  array de Strings que contiene los nombres de las columnas que se desean sacar de cada registro del ResultSet
   * @param data  la coleccion que contiene los datos
   * @return  un array de strings, el cual contiene los datos del resultset
   */
  public String[] getDataFromResultset(String[] selectedColumns, ResultSetDecorator data){
 	  ArrayList arrayList = new ArrayList();
  	String[] stringArray =  null; 		
    int i, k;
    
    try {
    	k = 0;
    	data.first();
  	  do{
        for(i=0; i<selectedColumns.length; i++){
          arrayList.add(k, data.getString(selectedColumns[i])); 
          k++;
        }
		  }while(data.next());
	   }
     catch (SQLException e) {
		   e.printStackTrace();
	   }
  
     stringArray = new String[arrayList.size()];
	
     for(i=0; i<arrayList.size(); i++)
       stringArray[i] = arrayList.get(i) + "";
    
     return stringArray;
  }
  

  /**
   * convierte un conjunto de datos almacenados en un HashMap en un array de Strings, ordenados por registro y por columnas
   * @param selectedColumns  array de Strings que contiene los nombres de las columnas que se desean sacar de cada registro del HashMap
   * @param data  El HashMap que contiene los datos
   * @return  un array de strings, el cual contiene los datos del resultset
   */
  public String[] getDataFromResultset(String[] selectedColumns, HashMap data){
  	String[] stringArray =  null; 		
    int i, k, j, numRegs;
    
     stringArray = new String[data.size()];
	   numRegs = data.size() / selectedColumns.length; 
     
     j = 0;
	   for( i=0; i<numRegs; i++){
	   	for(k=0; k<selectedColumns.length; k++ ){
		   	stringArray[j] = data.get(selectedColumns[k] +"[" + i +"]") +"";
		   	j=j+1;
	   	}
	   }
	   
     return stringArray;
  }

  
  /**
   * Mï¿½todo que dado un arreglo con los elementos
   * necesarios y una colecciï¿½n de HashMaps,
   * devuelve un arreglo con los datos presentes
   * en los HashMap
   * 
   * @param selectedColumns Columnas a consultar
   * dentro del HashMap
   * @param col Colecciï¿½n de HashMaps
   * @return
   */
  public String[] getDataFromCollection(String[] selectedColumns, Collection col)
  {
	 ArrayList arrayList = new ArrayList();
	 String[] stringArray =  null; 		
	 HashMap dyn;
	  int i, k;
	  if (col==null)
	  {
	  	return new String[0];
	  }
	  
	  Iterator it=col.iterator();
	  k=0;
	  while (it.hasNext())
	  {
      	dyn=(HashMap)it.next();
	    for(i=0; i<selectedColumns.length; i++)
	    {
	    	Object elemento=dyn.get(selectedColumns[i]);
	    	if(elemento==null)
	      	{
	    		arrayList.add(k, "");
	      	}
	    	else
	    	{
	    		arrayList.add(k, elemento);
	    	}
	      	k++;
	    }
	  }
	  
	   stringArray = new String[arrayList.size()];
		
	   for(i=0; i<arrayList.size(); i++)
	     stringArray[i] = arrayList.get(i) + "";
	  
	   
	   return stringArray;
  }

  /**
   * Mï¿½todo que dado un arreglo con los elementos
   * necesarios y una colecciï¿½n de HashMaps,
   * devuelve un arreglo con los datos presentes
   * en los HashMap
   * 
   * En el caso que encuentre un "true" lo reemplaza por la cadena contenida en "valorTrue"
   * En el caso que encuentre un "false" lo reemplaza por la cadena contenida en "valorFalse"
   * 
   * @param selectedColumns Columnas a consultar
   * dentro del HashMap
   * @param col Colecciï¿½n de HashMaps
   * @return
   */
  public String[] getDataFromCollectionModificarBooleanos(String[] selectedColumns, Collection col, String valorTrue, String valorFalse)
  {
	 ArrayList arrayList = new ArrayList();
	 String[] stringArray =  null; 		
	 HashMap dyn;
	  int i, k;
	  if (col==null)
	  {
	  	return new String[0];
	  }
	  
	  Iterator it=col.iterator();
	  k=0;
	  while (it.hasNext())
	  {
      	dyn=(HashMap)it.next();
	    for(i=0; i<selectedColumns.length; i++)
	    {
	      	arrayList.add(k, dyn.get(selectedColumns[i]));
	      	k++;
	    }
	  }
	  
	   stringArray = new String[arrayList.size()];
		
	   for(i=0; i<arrayList.size(); i++)
	   {
	   		String celda=arrayList.get(i)+"";
			if(celda.equalsIgnoreCase("true") || celda.equalsIgnoreCase("t") || celda.equalsIgnoreCase("1") || celda.equalsIgnoreCase("on") || celda.equalsIgnoreCase("s") || celda.equalsIgnoreCase("S") || celda.equalsIgnoreCase("y") || celda.equalsIgnoreCase("si") || celda.equalsIgnoreCase("yes"))
			{
				celda=valorTrue;
			}
			else if(celda.equalsIgnoreCase("false") || celda.equalsIgnoreCase("f") || celda.equalsIgnoreCase("0") || celda.equalsIgnoreCase("off") || celda.equalsIgnoreCase("n")  || celda.equalsIgnoreCase("N")  ||   celda.equalsIgnoreCase("no") || celda.equalsIgnoreCase(""))
			{
				celda=valorFalse;
			}
			stringArray[i] = celda ;
	   }
	  
	   
	   return stringArray;
  }

  /**
   * Formatea las cabeceras y los datos de tal manera que si una cabecera del arreglo de 
   * cabeceras pasadas como parametro es nula, automaticamenente eliminar&aacute; todas las columnas
   * de datos que respondan bajo esta cabecera; los datos y las cabeceras formateadas se almacenaran
   * en las variables de esta clase rHeader y rData respectivamente 
   * @param header  arreglo de Strings que contiene el conjunto de cabeceras de los datos
   * @param data    arreglo de Strings que contiene el conjunto de los datos
   */
  public void fixDataByHeader(String[] header, String[] data){
    HashMap listHeader = new HashMap();
    HashMap listData = new HashMap();
  	int nCols, nRows, nElements;
    int i, j;
    
    nElements = data.length;
    nCols = header.length; 
    nRows = nElements / nCols ; 
    

    // conversion de las cabeceras a un tipo de datos dinamico
    for(i=0; i < header.length; i++){
      listHeader.put((i+""), header[i]);	
    }

    // conversion de los datos a un tipo de datos dinamico
    for(i=0; i < data.length; i++){
      listData.put((i+""), data[i]);	
    }

    
    // recorrido de columnas ( cabceras de datos )
    // si alguna columna del encabezado es nula, procedemos a eliminar todas 
    // las columnas de  la informacion apuntadas por esa columna de la cabecera 
    for(i=0; i< nCols; i++){
    	if(listHeader.get((i+"")) == null){
    		for(j=0; j< nRows; j++ ){
    			listData.remove(((j*nCols)+i)+"");    			
    		}
    	}
    }
    
   // eliminacion de las columnas de la cabecera que son nulas
    for(i=0; i< nCols; i++){
    	if(listHeader.get((i+"")) == null ){
    		listHeader.remove((i+""));
    	}
    }

    
    // conversion del tipo de dato dinamico al tipo de datos estatico
    this.rHeader = new String[listHeader.size()];
    
    j = 0;
    for(i=0; i < nCols; i++){
    	if(listHeader.get((i+"")) != null){
      	this.rHeader[j] = listHeader.get((i+"")) + "";	
        j++;
    	}
    }

    // conversion del tipo de dato dinamico al tipo de datos estatico
    this.rData = new String[listData.size()];
    
    j = 0;
    for(i=0; i < nElements; i++){
    	if(listData.get((i+"")) != null){
      	this.rData[j] = listData.get((i+"")) + "";	
        j++;
    	}
    }
  }
  

  
  /**
   * Formatea cabeceras y datos de tal manera que al recibir un conjunto de datos, ordenados 
   * por registros y columnas, se realiza una inspeccion para determinar si existen las mismas 
   * columnas vacias en todos los registros; de ser asi se elmina esa columna de todos los registros 
   * y se elimina su correspondiente cabecera; los datos y las cabeceras formateadas se guardaran en 
   * los arrays de strings locales de esta clase rHeader y rData respectivamente.     
   * @param header  array de strings que contiene las cabeceras de los datos
   * @param data    array de strings que contiene los datos ordenados por registros y columnas
   */
  public void fixDataByData(String[] header, String[] data){
  	boolean atLeastOne;
  	int nCols, nRows, nElements;
    int i, j;
    
    nElements = data.length;
    nCols = header.length; 
    nRows = nElements / nCols ; 


    for(i=0; i< nCols; i++){
      atLeastOne = false;
    	
      for(j=0; j< nRows; j++ ){
  			if(data[((j*nCols)+i)] != null){
  				atLeastOne = true;
  			}
  		}
    	
    	if(atLeastOne == false){
    		header[i] = null;
    	}
    }
   
    fixDataByHeader(header, data);  	
  }
  
  
  /**
   * Formatea las columnas apuntadas por el array de enteros columnIndexes al formato de fecha adecuado para reportes.
   *  Este es dd/mm/aaaa.
   * @param header          array de Strings que contiene las cabeceras de los datos
   * @param data            array de Strings que contiene los datos ordenados por registros y columnas
   * @param columnIndexes   array de enteros que contiene los indices de las columnas que deben ser formateadas con el formato de fecha dd/mm/aaaa 
   */
  public void formatDateColumns(String[] header, String[] data, int[] columnIndexes){
  	int nCols, nRows, nElements;
    int i, j;
    
    nElements = data.length;
    nCols = header.length; 
    nRows = nElements / nCols ; 
  	

    for(i=0; i< columnIndexes.length; i++ ){
      for(j=0; j< nRows; j++ ){
  			if(data[((j*nCols)+columnIndexes[i])] != null){
  				data[((j*nCols)+columnIndexes[i])] = UtilidadFecha.conversionFormatoFechaAAp(data[((j*nCols)+columnIndexes[i])]);
   			}
   		}
    }
  }
  
  /**
  crea la secci&oacute;n REPORT_HEADER_SECTION con lo b&aacute;sico que debe tener una cabecera de reporte, esto es: un logo de la compa&ntilde;ia que genera el reporte, la raz&oacute;n social de la compa&ntilde;ia, el nit de la compa&ntilde;ia y el titulo del reporte

  @param companyLogo  ruta y nombre del archivo que contiene el logo de la compa&ntilde;ia
  @param companyName  raz&oacute;n social de la compa&ntilde;ia
  @param companyNit   NIT de la compa&ntilde;ia
  @param reportTitle  titulo de este reporte
*/
  public void setReportBaseHeader(String companyLogo, String companyName, String companyNit, String reportTitle)
  {
	  InstitucionBasica institucion = new InstitucionBasica();
	  institucion.setLogoReportes(companyLogo);
	  institucion.setRazonSocial(companyName);
	  institucion.setNit(companyNit);
	  setReportBaseHeader1(institucion, companyNit, reportTitle);
  }
  
  
  /**
      crea la secci&oacute;n REPORT_HEADER_SECTION con lo b&aacute;sico que debe tener una cabecera de reporte, esto es: un logo de la compa&ntilde;ia que genera el reporte, la raz&oacute;n social de la compa&ntilde;ia, el nit de la compa&ntilde;ia y el titulo del reporte

      @param companyLogo  ruta y nombre del archivo que contiene el logo de la compa&ntilde;ia
      @param companyName  raz&oacute;n social de la compa&ntilde;ia
      @param companyNit   NIT de la compa&ntilde;ia
      @param reportTitle  titulo de este reporte
  */
  public void setReportBaseHeader1(InstitucionBasica institucionBasica,String companyNit, String reportTitle)
  {
    iTextBaseTable iSection;

    iSection = new iTextBaseTable();
    
    //Se modifico por el estï¿½ndar que se empezo a manejar en los reportes en Consulta Externa Tarea 61360
    /*if(!institucionBasica.getActividadEconomica().equals(""))
    	filas = 5;*/
    
    String nitInstitucion = "";
    if(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
    	nitInstitucion =  institucionBasica.getDescripcionTipoIdentificacion()+". "+institucionBasica.getNit()+" - "+institucionBasica.getDigitoVerificacion();
    else
    	nitInstitucion =  institucionBasica.getDescripcionTipoIdentificacion()+". "+institucionBasica.getNit();
    
    iSection.useTable(REPORT_HEADER_TABLE, 5, 3);
    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.0f);
    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 0);
    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.1f);
    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
    
    if (companyNit.equals("align-left"))
    	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_LEFT);
    else
    	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
    
    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

    this.font.setFontAttributes(0x000000, 8, true, false, false);
    
    float[] widths = new float[3];
    widths[0] = 25f;
    widths[1] = 50f;
    widths[2] = 25f;
    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
    
    iSection.setTableCellsRowSpan(4);
    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);
    iSection.setTableCellsRowSpan(1);
    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, institucionBasica.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);
    iSection.setTableCellsRowSpan(4);
    iSection.addTableImageCellAligned(REPORT_HEADER_TABLE, institucionBasica.getLogoReportes(), 75f, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_TOP);
    iSection.setTableCellsRowSpan(1);
    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, nitInstitucion, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);
    //Se modifico por el estï¿½ndar que se empezo a manejar en los reportes en Consulta Externa Tarea 61360
    /*if(!institucionBasica.getActividadEconomica().equals(""))
    	iSection.addTableTextCell(REPORT_HEADER_TABLE, institucionBasica.getActividadEconomica(), this.font);*/
    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, institucionBasica.getDireccion(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);
    
    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Tels. "+institucionBasica.getTelefono(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);
    
    iSection.setTableCellsColSpan(3);
    this.font.setFontAttributes(0x000000, 9, true, false, false);
    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);

    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  
  
  /**
  crea la secci&oacute;n REPORT_HEADER_SECTION con lo b&aacute;sico que debe tener una cabecera de reporte, esto es: un logo de la compa&ntilde;ia que genera el reporte, la raz&oacute;n social de la compa&ntilde;ia, el nit de la compa&ntilde;ia y el titulo del reporte

  @param companyLogo  ruta y nombre del archivo que contiene el logo de la compa&ntilde;ia
  @param companyName  raz&oacute;n social de la compa&ntilde;ia
  @param companyNit   NIT de la compa&ntilde;ia
  @param reportTitle  titulo de este reporte
*/
public void setReportBaseHeaderIzq(InstitucionBasica institucionBasica,String companyNit, String reportTitle)
{
iTextBaseTable iSection;

iSection = new iTextBaseTable();

//Se modifico por el estï¿½ndar que se empezo a manejar en los reportes en Consulta Externa Tarea 61360
/*if(!institucionBasica.getActividadEconomica().equals(""))
	filas = 5;*/

String nitInstitucion = "";
if(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
	nitInstitucion =  institucionBasica.getDescripcionTipoIdentificacion()+". "+institucionBasica.getNit()+" - "+institucionBasica.getDigitoVerificacion();
else
	nitInstitucion =  institucionBasica.getDescripcionTipoIdentificacion()+". "+institucionBasica.getNit();

if(!reportTitle.equals(""))
	iSection.useTable(REPORT_HEADER_TABLE, 5, 3);
else
	iSection.useTable(REPORT_HEADER_TABLE, 4, 3);
iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
iSection.setTableCellPadding(REPORT_HEADER_TABLE, 0);

if (companyNit.equals("align-left"))
	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_LEFT);
else
	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);

iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

this.font.setFontAttributes(0x000000, 9, true, false, false);

float[] widths = new float[3];
widths[0] = 30f;
widths[1] = 40f;
widths[2] = 30f;
setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);


iSection.setTableCellsRowSpan(4);
iSection.addTableImageCellAligned(REPORT_HEADER_TABLE, institucionBasica.getLogoReportes(), 75f, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP);
iSection.setTableCellsRowSpan(1);
iSection.addTableTextCell(REPORT_HEADER_TABLE, institucionBasica.getRazonSocial(), this.font);
iSection.setTableCellsRowSpan(4);
iSection.addTableTextCell(REPORT_HEADER_TABLE, "", this.font);
iSection.setTableCellsRowSpan(1);
iSection.addTableTextCell(REPORT_HEADER_TABLE, nitInstitucion, this.font);
//Se modifico por el estándar que se empezo a manejar en los reportes en Consulta Externa Tarea 61360
/*if(!institucionBasica.getActividadEconomica().equals(""))
	iSection.addTableTextCell(REPORT_HEADER_TABLE, institucionBasica.getActividadEconomica(), this.font);*/
iSection.addTableTextCell(REPORT_HEADER_TABLE, institucionBasica.getDireccion(), this.font);

iSection.addTableTextCell(REPORT_HEADER_TABLE, "Tels. "+institucionBasica.getTelefono(), this.font);

iSection.setTableCellsColSpan(3);
this.font.setFontAttributes(0x000000, 11, true, false, false);
if(!reportTitle.equals(""))
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);

useSection(REPORT_HEADER_SECTION, iSection);
}
  
  /**
  crea el encabezado de un informe estandar solicitado por el ministerio de proteccion social
  @param HashMap parametrosEnc 
*/
public void setReportBaseHeader2(HashMap parametrosEnc)
{
	iTextBaseTable iSection;
	
	iSection = new iTextBaseTable();
	
	iSection.useTable(REPORT_HEADER_TABLE, 4, 4);
	iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	
	this.font.setFontAttributes(0x000000, 12, true, false, false);
	
	float[] widths = new float[4];
	widths[0] = 20f;
	widths[1] = 35f;
	widths[2] = 25f;
	widths[3] = 20f;
	setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	
	iSection.setTableCellsRowSpan(3);
	iSection.setTableCellsColSpan(1);
	String filePathLogo = ValoresPorDefecto.getFilePath(); //path para el logo de la presidencia
	if(System.getProperty("file.separator").equals("/"))
	{
		filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("/",(filePathLogo.length()-2)))+"/imagenes/";
		
	}
	else
	{
		filePathLogo=filePathLogo.substring(0, filePathLogo.lastIndexOf("\\",(filePathLogo.length()-2)))+"\\imagenes\\";
	}
	
	filePathLogo += ConstantesBD.logoprecidencia;
	iSection.addTableImageCellAligned(REPORT_HEADER_TABLE, filePathLogo, 75f, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(3);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, parametrosEnc.get("entidad_requiere").toString(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(3);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(3);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, parametrosEnc.get("titulo").toString(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(1);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_BOTTOM);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(1);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, parametrosEnc.get("numero_atencion").toString(), this.font, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_BOTTOM);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(1);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Fecha: "+parametrosEnc.get("fecha").toString(), this.font, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_BOTTOM);
	iSection.setTableCellsRowSpan(1);
	iSection.setTableCellsColSpan(1);
	iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Hora: "+parametrosEnc.get("hora").toString(), this.font, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_BOTTOM);
	
	
	useSection(REPORT_HEADER_SECTION, iSection);
}
  
  
  /**
  crea la secci&oacute;n REPORT_HEADER_SECTION con lo b&aacute;sico que debe tener una cabecera de reporte, esto es: un logo de la compa&ntilde;ia que genera el reporte, la raz&oacute;n social de la compa&ntilde;ia, el nit de la compa&ntilde;ia y el titulo del reporte

  @param companyLogo  ruta y nombre del archivo que contiene el logo de la compa&ntilde;ia
  @param companyName  raz&oacute;n social de la compa&ntilde;ia
  @param companyNit   NIT de la compa&ntilde;ia
  @param reportTitle  titulo de este reporte
*/
public void setReportBaseHeaderDir(InstitucionBasica institucionBasica,String companyNit, String reportTitle,String direccion)
{
iTextBaseTable iSection;

iSection = new iTextBaseTable();

//Se modifico por el estï¿½ndar que se empezo a manejar en los reportes en Consulta Externa Tarea 61360
/*if(!institucionBasica.getActividadEconomica().equals(""))
	filas = 5;*/

String nitInstitucion = "";
if(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
	nitInstitucion =  institucionBasica.getDescripcionTipoIdentificacion()+". "+institucionBasica.getNit()+" - "+institucionBasica.getDigitoVerificacion();
else
	nitInstitucion =  institucionBasica.getDescripcionTipoIdentificacion()+". "+institucionBasica.getNit();

iSection.useTable(REPORT_HEADER_TABLE, 5, 3);
iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);

if (companyNit.equals("align-left"))
	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_LEFT);
else
	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);

iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

this.font.setFontAttributes(0x000000, 12, true, false, false);

float[] widths = new float[3];
widths[0] = 30f;
widths[1] = 40f;
widths[2] = 30f;
setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);

iSection.setTableCellsRowSpan(4);
iSection.addTableTextCell(REPORT_HEADER_TABLE, "", this.font);
iSection.setTableCellsRowSpan(1);
iSection.addTableTextCell(REPORT_HEADER_TABLE, institucionBasica.getRazonSocial(), this.font);
iSection.setTableCellsRowSpan(4);
iSection.addTableImageCellAligned(REPORT_HEADER_TABLE, institucionBasica.getLogoReportes(), 75f, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_TOP);
iSection.setTableCellsRowSpan(1);
iSection.addTableTextCell(REPORT_HEADER_TABLE, nitInstitucion, this.font);
//Se modifico por el estï¿½ndar que se empezo a manejar en los reportes en Consulta Externa Tarea 61360
/*if(!institucionBasica.getActividadEconomica().equals(""))
	iSection.addTableTextCell(REPORT_HEADER_TABLE, institucionBasica.getActividadEconomica(), this.font);*/

iSection.addTableTextCell(REPORT_HEADER_TABLE, direccion, this.font);

iSection.addTableTextCell(REPORT_HEADER_TABLE, "Tels. "+institucionBasica.getTelefono(), this.font);

iSection.setTableCellsColSpan(3);
this.font.setFontAttributes(0x000000, 11, true, false, false);
iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP);

useSection(REPORT_HEADER_SECTION, iSection);
}


  public void setReportBaseHeaderCensoCamas (InstitucionBasica institucion, String reportTitle, String criterios)
  {
	  iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	  
	     
	    iSection.useTable(REPORT_HEADER_TABLE, 6, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    
	    iSection.setTableCellsRowSpan(4);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, institucion.getLogoReportes(), 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,institucion.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	   	    
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT. "+institucion.getNit(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	  
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Dirección: "+institucion.getDireccion(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Teléfono: "+institucion.getTelefono(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	   
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,criterios, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	  	  
	    
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  public void setReportBaseHeaderTarifasInventario (InstitucionBasica institucion, String reportTitle)
  {
	  iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	  
	     
	    iSection.useTable(REPORT_HEADER_TABLE, 6, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    
	    iSection.setTableCellsRowSpan(4);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, institucion.getLogoReportes(), 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,institucion.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	   	    
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT. "+institucion.getNit(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	  
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Dirección: "+institucion.getDireccion(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Teléfono: "+institucion.getTelefono(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	   
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  public void setReportBaseHeaderIngresos (InstitucionBasica institucion, String reportTitle, String criterios)
  {
	  iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	     
	    iSection.useTable(REPORT_HEADER_TABLE, 2, 3);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    
	    //this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, institucion.getLogoReportes(), 100f, 0, 0);
	    
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,institucion.getRazonSocial()+"\n"+reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_BOTTOM );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_BOTTOM );
	    
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,criterios, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	  	  
	    
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  public void setReportBaseHeaderReportes (InstitucionBasica institucion, String reportTitle, String criterios, String ubicacionLogo)
  {
	  iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	  
	    //int tamano=criterios.size()/2;
	    int ubicacion=0;
	    if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha))
	    	ubicacion=1;
	    
	    iSection.useTable(REPORT_HEADER_TABLE, 5, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    
	    iSection.setTableCellsRowSpan(4);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, institucion.getLogoReportes(), 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,institucion.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	   	    
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT. "+institucion.getNit(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	  
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Dirección: "+institucion.getDireccion(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Teléfono: "+institucion.getTelefono(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	   
	   this.font.setFontAttributes(0x000000, 10, false, false, false);
	   iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,criterios, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	  	 
	    
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  
  public void setReportBaseHeader1(String companyLogo, String companyName, String companyNit, String reportTitle,int tamanioLetra){
	    iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 3, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, tamanioLetra, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    
	    iSection.setTableCellsRowSpan(2);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, companyLogo, 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyName, this.font);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyNit, this.font);
	 
	    
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );

	    useSection(REPORT_HEADER_SECTION, iSection);
	  }
  
  /**
   * El encabezado  con direccin de la insitucion y ademas el telefono
   * @param companyLogo
   * @param companyName
   * @param companyNit
   * @param reportTitle
   * @param direccionInstitucion
   * @param telInstitucion
   */
  public void setReportBaseHeaderCxC1(String companyLogo, String companyName, String companyNit, String reportTitle, String direccionInstitucion, String telInstitucion)
  {
	    iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 1, 3);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    
	    iSection.setTableCellsRowSpan(3);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, companyLogo, 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyName, this.font);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyNit, this.font);
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, direccionInstitucion, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, telInstitucion, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );

	    useSection(REPORT_HEADER_SECTION, iSection);
	 }
  
  /**
   * Mï¿½todo que imprime el encabezado estandar para Sasaima
   * @param ins
   */
  public void setReportBaseHeaderSasaima(InstitucionBasica ins)
  {
	  	iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 1, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 1);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 50f;
	    widths[1] = 50f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,ins.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NO SOMOS AUTORETENEDORES", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT. "+ins.getNit(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"ACTIVIDAD ECONï¿½MICA. "+ins.getActividadEconomica(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Ciudad: "+ins.getCiudad()+"/"+ins.getDepto(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Dirección: "+ins.getDireccion(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"Teléfono: "+ins.getTelefono(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  
  /**
   * Mï¿½todo que imprime el encabezado estandar para Suba Capitacion
   * @param ins
 * @param conCuentaCobro 
   */
  public void setReportBaseHeaderSuba(String filepath, InstitucionBasica ins,String numeroCuentaCobro, boolean conCuentaCobro)
  {
	  	iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, conCuentaCobro?3:2, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 1);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 50f;
	    widths[1] = 50f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.setTableCellsRowSpan(2);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, filepath, 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,ins.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT "+ins.getNit(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    if(conCuentaCobro)
	    {
	    	iSection.setTableCellsColSpan(2);
		    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"FACTURA Nï¿½ "+numeroCuentaCobro, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    }
	    
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  /**
   * Mï¿½todo que imprime el encabezado estandar para Suba Evento
   * @param ins
 * @param conCuentaCobro 
   */
  public void setReportBaseHeaderSubaEvento(String filepath,InstitucionBasica ins,String numeroCuentaCobro, String fechaGeneracion)
  {
	  	iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 4, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 1);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 50f;
	    widths[1] = 50f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,ins.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.setTableCellsRowSpan(4);
	    iSection.addTableImageCellAligned(REPORT_HEADER_TABLE, filepath, 60f, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.setTableCellsRowSpan(1);
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT "+ins.getNit(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"RELACIÓN DE ENVIO No "+numeroCuentaCobro, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    String[] fecha = UtilidadFecha.conversionFormatoFechaAAp(fechaGeneracion).split("/");
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,ins.getCiudad()+", "+UtilidadFecha.obtenerNombreMes(Integer.parseInt(fecha[1]))+" "+Integer.parseInt(fecha[0])+", "+fecha[2], this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  /**
   * Mï¿½todo que imprime el encabezado estandar para impresion Anexos Facturas Suba Evento
   * @param ins
 * @param conCuentaCobro 
   */
  public void setReportBaseHeaderSubaAnexos(String filepath,InstitucionBasica ins,String numeroCuentaCobro, String convenio)
  {
	  	iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 4, 2);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 1);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 14, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 50f;
	    widths[1] = 50f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    
	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.setTableCellsRowSpan(2);
	    iSection.addTableImageCellAligned(REPORT_HEADER_TABLE, filepath, 100f, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.setTableCellsRowSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,ins.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"NIT "+ins.getNit(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
    	iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"RELACIÓN DE ENVÍO No "+numeroCuentaCobro, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,convenio, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    
	    
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
 
  
  /**
   * Mï¿½todo que imprime el encabezado estandar para Sasaima Anexo
   * @param ins
 * @param fechaFinal 
 * @param fechaInicial 
 * @param convenio 
   */
  public void setReportBaseHeaderSasaimaAnexo(InstitucionBasica ins, String fechaInicial, String fechaFinal, String convenio)
  {
	  	iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 3, 1);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 1);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);


	    this.font.setFontAttributes(0x000000, 14, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,ins.getRazonSocial(), this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"RELACIï¿½N DE FACTURAS DE "+convenio, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,"PERï¿½ODO "+fechaInicial+" - "+fechaFinal, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
	    this.font.setFontAttributes(0x000000, 10, false, false, false);
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
  }
  
  
  /**
   * Permite la acomodacion de las cosas basicas del enbezado mas los nombres-apellidos y el tipo id- numero id
   * del paciente para que se muestre en todas las paginas de la impresiï¿½n
   * @param companyLogo
   * @param companyName
   * @param companyNit
   * @param reportTitle
   * @param nombresApellidos
   * @param tipoNumeroId
   */
  public void setReportBaseHeader1(String companyLogo, String companyName, String companyNit, String reportTitle,String nombresApellidos, String tipoNumeroId, String numeroPresupuesto, String fecha)
  {
	    iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 5, 3);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 12, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableCellsRowSpan(2);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, companyLogo, 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyName, this.font);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyNit, this.font);
	 
	    
	    iSection.setTableCellsColSpan(3);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle+" No."+numeroPresupuesto+"					Fecha Elaboraciï¿½n: "+fecha, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
	    
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Informaciï¿½n Personal", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    iSection.setTableCellsColSpan(2);
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Apellidos y Nombres: "+nombresApellidos, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Tipo y Nï¿½mero Id: "+tipoNumeroId, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
	  }
  
  
  
  
  /**
   * Metodo para acomodar el encabezado del anexo de medicamentos para la impresion de la factura
   * @param companyLogo
   * @param companyName
   * @param companyNit
   * @param paciente
   * @param numeroFactura
 * @param infoAnulacion 
   */
  public void setEncabezadoAnexoMedicamentos1(String companyLogo, String companyName, String companyNit, PersonaBasica paciente, String numeroFactura, String infoAnulacion)
  {
	    iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 1, 3);
	    
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 12, true, false, false);

	   /** float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);**/
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.0f);
	    
	    iSection.setTableCellsColSpan(1);
	    iSection.setTableCellsRowSpan(2);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, companyLogo, 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyName, this.font);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyNit, this.font);
	 
	    iSection.setTableCellsRowSpan(1);
	    iSection.setTableCellsColSpan(3);
	    if(!infoAnulacion.trim().equals(""))
	    {
	    	String[] info=infoAnulacion.split(ConstantesBD.separadorSplit);
		    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "F A C T U R A  A N U L A D A "+info[1]+" "+info[2], this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
		    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
	    }
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle+" Anexo Medicamentos, Insumos y Materiales ", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
	    /*****************************INFORMACION DEL PACIENTE*****************************/
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Informaciï¿½n Personal", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(2);
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, paciente.getApellidosNombresPersona() , this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE,  paciente.getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Edad: "+paciente.getEdadDetallada(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Sexo: "+paciente.getSexo(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Teléfono: "+paciente.getTelefono(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(3);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Dirección: "+paciente.getDireccion(), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    /***********************INFORMACION DEL RESPONSABLE***********************/
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Información del Responsable", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(2);
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Responsable: "+Utilidades.getResponsableFactura(Integer.parseInt(numeroFactura)), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Factura: "+Utilidades.obtenerConsecutivoFactura(Integer.parseInt(numeroFactura)), this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    useSection(REPORT_HEADER_SECTION, iSection);
	  }
  

    
    /**
     * metodo que establece los anchos de las columnas de una tabla
     * @param section     una referencia a la seccion
     * @param tableAlias   el alias de la tabla
     * @param widths  un array de flotantes, el cual contiene los anchos de las columnas
     */
    
    private void setColumnsWidths(iTextBaseTable section, String tableAlias, float[] widths){
    	try{
    	  section.getTableReference(tableAlias).setWidths(widths);
    	}
    	catch(Exception e){
    	      e.getMessage();
    	}
    }
        
    
  /**
   * metodo que establece los anchos de las columnas de una tabla
   * @param sectionAlias   el alias de la seccion 
   * @param tableAlias     el alias de la tabla
   * @param widths   un array de flotantes, el cual contiene los anchos de las columnas deseadas
   */
  
  public void setColumnsWidths(String sectionAlias, String tableAlias, float[] widths){
  		setColumnsWidths(getSectionReference(sectionAlias), tableAlias, widths);
  }
  
   
  

  /**
      crea una secci&oacute;n y su determinada tabla padre, la cual tiene un numero inicial de filas, un numero determinado de columnas y un desplazamiento relativo a los otros objetos insertados)
 
      @param sectionAlias     alias de la secci&oacute;n que se creara en este reporte
      @param rootTableAlias   alias de la tabla padre que servir&aacute; como base a la secci&oacute;n de este reporte
      @param numRows          numero de filas de esta secci&oacute;n
      @param numCols          numero de columnas de esta secci&oacute;n
      @param tableSpacing     espaciamiento entre esta secci&oacute;n y objetos anteriores

      @return una referencia de tipo iTextBaseTable a la secci&oacute;n creada

 */
  public iTextBaseTable createSection(String sectionAlias, String rootTableAlias, int numRows, int numCols, float tableSpacing){
    iTextBaseTable iSection;


    // creamos la seccion
    iSection = new iTextBaseTable();

    // establecemos las caracteristicas de la seccion
    iSection.useTable(rootTableAlias, numRows, numCols);
    iSection.setTableWidth(rootTableAlias, 100);
    iSection.setTableOffset(rootTableAlias, tableSpacing);
    iSection.setTableBorder(rootTableAlias, this.sectionTitleBorderColor, 0.5f);
    iSection.setTableCellBorderWidth(rootTableAlias, 0.5f);
    iSection.setTableCellPadding(rootTableAlias, 2f);
    iSection.setTableSpaceBetweenCells(rootTableAlias, 0.5f);
    iSection.setTableCellsDefaultColors(rootTableAlias, this.sectionInfoBackgroundColor, this.sectionInfoBorderColor);
    iSection.setTableCellsDefaultHAlignment(rootTableAlias, iTextBaseDocument.ALIGNMENT_LEFT);
    iSection.setTableCellsDefaultVAlignment(rootTableAlias, iTextBaseDocument.ALIGNMENT_MIDDLE);

    useSection(sectionAlias, iSection);
    return iSection;
  }




  /**
      crea una nueva tabla en una secci&oacute;n en particular. El objetivo de esta tabla es 
      ser insertada posteriormente en una de las celdas de cualquier tabla que se haya 
      definido en esta secci&oacute;n, inclusive la tabla padre de la secci&oacute;n

      @param sectionAlias       alias de la secci&oacute;n que contendr&aacute; la nueva tabla
      @param childTableAlias    alias de la tabla hija que se creara en la secci&oacute;n
      @param numRows            numero de filas iniciales de la tabla hija creada
      @param numCols            numero de columnas de la tabla hija creada

      @return una referencia de tipo Table a la tabla creada

  */
  public Table createSectionTable(String sectionAlias, String childTableAlias, int numRows, int numCols){
    iTextBaseTable iSection;

    iSection = getSectionReference(sectionAlias);
    iSection.useTable(childTableAlias, numRows, numCols); 
    return iSection.getTableReference(childTableAlias);
  }



  /**
      adiciona una barra de titulo a una secci&oacute;n

      @param section        referencia a un objeto de tipo iTextBaseTable que contiene la tabla a la cual se le insertara una barra de titulo
      @param tableAlias     alias de la tabla a la cual se le adicionar&aacute; la barra de titulo
      @param sectionTitle   titulo de la secci&oacute;n que se insertar&aacute; en la barra de  titulo de la tabla
  */
  private void addSectionTitle(iTextBaseTable section, String tableAlias, String sectionTitle){
    // adicionamos la cabecera de la seccion
    section.setTableCellsColSpan(section.getTableReference(tableAlias).getColumns());
    this.font.setFontAttributes(this.sectionTitleFontColor, this.sectionTitleFontSize, true, false, false);
    section.setTableCellsBackgroundColor(this.sectionTitleBackgroundColor);
    section.addTableTextCell(tableAlias, sectionTitle, this.font);
    section.setTableCellsColSpan(1);
  }


  /**
      adiciona una barra de titulo a una secci&oacute;n

      @param sectionAlias   alias de la secci&oacute;n que contiene la tabla a la cual se le insertara una barra de titulo
      @param tableAlias     alias de la tabla a la cual se le adicionar&aacute; la barra de titulo
      @param sectionTitle   titulo de la secci&oacute;n que se insertar&aacute; en la barra de  titulo de la tabla
  */
  public void addSectionTitle(String sectionAlias, String tableAlias, String sectionTitle){
    iTextBaseTable section;

    section = getSectionReference(sectionAlias);
    addSectionTitle(section, tableAlias, sectionTitle);
  }


  /**
      adiciona encabezados de datos por filas a una secci&oacute;n

      @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
      @param tableAlias  alias de la tabla a la cual se le adicionaran las cabeceras de datos por filas
      @param header      array de strings que contiene las cabeceras de datos
      @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  private void addSectionRowHeaders(iTextBaseTable section, String tableAlias, String[] header, boolean hasTitle){
    int iter;

    section.setTableCellsColSpan(1);
    section.setTableCellsRowSpan(1);
    section.setTableCellsBackgroundColor(this.sectionInfoBackgroundColor);

    // adicionamos las cabeceras de la informacion
    this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, true, false, false);
    
    if(hasTitle == true){
      for(iter=0; iter < header.length; iter++){
        if(header[iter] != null)
      	  section.addTableTextCell(tableAlias, header[iter], this.font, (iter+1), 0);      
      }
    }
    else{
      for(iter=0; iter < header.length; iter++){
        if(header[iter] != null)
      	  section.addTableTextCell(tableAlias, header[iter], this.font, iter, 0);      
      }
    }

  }


  /**
      adiciona encabezados de datos por filas a una secci&oacute;n

      @param sectionAlias   alias de la secci&oacute;n contenedora de la tabla
      @param tableAlias     alias de la tabla a la cual se le adicionaran las cabeceras de datos por filas
      @param header         array de strings que contiene las cabeceras de datos
      @param hasTitle       valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  public void addSectionRowHeaders(String sectionAlias, String tableAlias, String[] header, boolean hasTitle){
    iTextBaseTable section;

    section = getSectionReference(sectionAlias);
    addSectionRowHeaders(section, tableAlias, header, hasTitle);
  }



  /**
      adiciona encabezados de datos por columnas a una secci&oacute;n. Si la secci&oacute;n se divide en varias
      paginas, el encabezado de datos de la secci&oacute;n se repetir&aacute; en cada una autom&aacute;ticamente

      @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
      @param tableAlias  alias de la tabla a la cual se le adicionaran las cabeceras de datos por columnas
      @param header      array de strings que contiene las cabeceras de datos
      @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  private void addSectionColHeaders(iTextBaseTable section, String tableAlias, String[] header, boolean hasTitle){
    int iter;

    section.setTableCellsColSpan(1);
    section.setTableCellsRowSpan(1);
    section.setTableCellsBackgroundColor(this.sectionInfoBackgroundColor);

    // adicionamos las cabeceras de la informacion
    this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, true, false, false);
    
    if(hasTitle == true){
      for(iter=0; iter < section.getTableReference(tableAlias).getColumns(); iter++){
        if(header[iter] != null)
        {
      	  section.addTableTextCell(tableAlias, header[iter], this.font, 1, iter);
        }
        
      }
    }
    else{
      for(iter=0; iter < section.getTableReference(tableAlias).getColumns(); iter++){
        if(header[iter] != null)        
        {
      	  section.addTableTextCell(tableAlias, header[iter], this.font, 0, iter);      
        }
      }
    }

    section.getTableReference(tableAlias).endHeaders();
  }

  /**
  adiciona encabezados de datos por columnas a una secci&oacute;n. Si la secci&oacute;n se divide en varias
  paginas, el encabezado de datos de la secci&oacute;n se repetir&aacute; en cada una autom&aacute;ticamente

  @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
  @param tableAlias  alias de la tabla a la cual se le adicionaran las cabeceras de datos por columnas
  @param header      array de strings que contiene las cabeceras de datos
  @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
	*/
	private void addSectionColHeaders(iTextBaseTable section, String tableAlias, String[] header,float tamLetraCabecera, boolean hasTitle)
	{
	int iter;
	
	section.setTableCellsColSpan(1);
	section.setTableCellsRowSpan(1);
	section.setTableCellsBackgroundColor(this.sectionInfoBackgroundColor);
	
	// adicionamos las cabeceras de la informacion
	this.font.setFontAttributes(this.sectionInfoFontColor, tamLetraCabecera, true, false, false);
	if(hasTitle == true){
	  for(iter=0; iter < section.getTableReference(tableAlias).getColumns(); iter++){
	    if(header[iter] != null)
	  	  section.addTableTextCell(tableAlias, header[iter], this.font, 1, iter);      
	  }
	}
	else{
	  for(iter=0; iter < section.getTableReference(tableAlias).getColumns(); iter++){
	    if(header[iter] != null)        
	  	  section.addTableTextCell(tableAlias, header[iter], this.font, 0, iter);      
	  }
	}
	
	section.getTableReference(tableAlias).endHeaders();
	}

  /**
      adiciona encabezados de datos por columnas a una secci&oacute;n. Si la secci&oacute;n se divide en varias 
      paginas, el encabezado de datos de la secci&oacute;n se repetir&aacute; en cada una autom&aacute;ticamente

      @param sectionAlias  referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
      @param tableAlias    alias de la tabla a la cual se le adicionaran las cabeceras de datos por columnas
      @param header        array de strings que contiene las cabeceras de datos
      @param hasTitle      valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  public void addSectionColHeaders(String sectionAlias, String tableAlias, String[] header, boolean hasTitle){
    iTextBaseTable section;

    section = getSectionReference(sectionAlias);
    addSectionColHeaders(section, tableAlias, header, hasTitle);
  }



  
  /**
      adiciona datos por filas a una secci&oacute;n

      @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
      @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por filas
      @param data        array de strings que contiene los datos
      @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  private void addSectionRowData(iTextBaseTable section, String tableAlias, String[] data, boolean hasTitle){
    int numRows, numCols;
    int iter, iCol, iRow;

    numRows = section.getTableReference(tableAlias).size();
    numCols = section.getTableReference(tableAlias).getColumns();

    // adicionamos la informacion
    this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, false, false, false);

    iter=0; 
    for(iCol=1; iCol < numCols; iCol++){
      for(iRow = (hasTitle)?1:0; iRow < numRows; iRow++){ 
        if(iter<data.length){
          if(data[iter] != null)
            section.addTableTextCell(tableAlias, data[iter], this.font, iRow, iCol);      
          iter++;
        }
      }
    }
  }


  /**
   *  Este metodo adiciona datos a una tabla de una seccion sin tomar en cuenta las cabeceras de los datos
      @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
      @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por filas
      @param data        array de strings que contiene los datos
     */
  
  
  private void addSectionData(iTextBaseTable section, String tableAlias, String[] data){
      int iter; 

      // adicionamos la informacion
      this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, false, false, false);
       
      for(iter= 0; iter < data.length;  iter++){ 
         if(data[iter] != null)
              section.addTableTextCell(tableAlias, data[iter], this.font);      
      }
    }

  
  /**  Este metodo adiciona datos a una tabla de una seccion sin tomar en cuenta las cabeceras de los datos
      @param sectionAlias  alias de la secci&oacute;n contenedora de la tabla
      @param tableAlias    alias de la tabla a la cual se le adicionaran los datos por filas
      @param data          array de strings que contiene los datos
     */
  public void addSectionData(String sectionAlias, String tableAlias, String[] data){
      iTextBaseTable section;

      section = getSectionReference(sectionAlias);
      addSectionData(section, tableAlias, data);
    }
  
  
  /**
      adiciona datos por filas a una secci&oacute;n

      @param sectionAlias  alias de la secci&oacute;n contenedora de la tabla
      @param tableAlias    alias de la tabla a la cual se le adicionaran los datos por filas
      @param data          array de strings que contiene los datos
      @param hasTitle      valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  public void addSectionRowData(String sectionAlias, String tableAlias, String[] data, boolean hasTitle){
    iTextBaseTable section;

    section = getSectionReference(sectionAlias);
    addSectionRowData(section, tableAlias, data, hasTitle);
  }



  /**
      adiciona datos por columnas a una secci&oacute;n

      @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
      @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por columnas
      @param data        array de strings que contiene los datos
      @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  private void addSectionColData(iTextBaseTable section, String tableAlias, String[] data, boolean hasTitle){
    int numRows, numCols;
    int iter, iCol, iRow;

    numRows = section.getTableReference(tableAlias).size();
    numCols = section.getTableReference(tableAlias).getColumns();

    // adicionamos la informaci&oacute;n
    this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, false, false, false);
    iter=0; 
    for(iRow=(hasTitle)?2:1; iRow < numRows; iRow++){
      for(iCol=0; iCol < numCols; iCol++){
        if(iter<data.length){
          if(data[iter] != null)
            section.addTableTextCell(tableAlias, data[iter], this.font, iRow, iCol);      
          iter++;
        }
      }
    }
  }
  
  /**
	  adiciona datos por columnas a una secci&oacute;n
	
	  @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
	  @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por columnas
	  @param data        array de strings que contiene los datos
	  @param int[] alignment contiene las alineaciones de los textos
	  @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
   */
  private void addSectionColData(iTextBaseTable section, String tableAlias, String[] data, int[] alignment, boolean hasTitle)
  {
	int numRows, numCols;
	int iter, iCol, iRow;
	
	numRows = section.getTableReference(tableAlias).size();
	numCols = section.getTableReference(tableAlias).getColumns();
	
	// adicionamos la informaci&oacute;n
	this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, false, false, false);
	iter=0; 
	for(iRow=(hasTitle)?2:1; iRow < numRows; iRow++){
	  for(iCol=0; iCol < numCols; iCol++){
	    if(iter<data.length){
	      if(data[iter] != null)	      	   
	      	section.addTableTextCellAligned(tableAlias, data[iter], this.font, alignment[iCol], iTextBaseDocument.ALIGNMENT_MIDDLE, iRow, iCol);	      
	      iter++;
	    }
	  }
	}
  }
  
  
  
  
	/**
	  adiciona datos por columnas a una secci&oacute;n
	
	  @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
	  @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por columnas
	  @param data        array de strings que contiene los datos
	  @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
	*/
	private void addSectionColData(iTextBaseTable section, String tableAlias, String[] data,float tamLetraDatos, boolean hasTitle)
	{
		int numRows, numCols;
		int iter, iCol, iRow;
		
		numRows = section.getTableReference(tableAlias).size();
		numCols = section.getTableReference(tableAlias).getColumns();
		
		// adicionamos la informaci&oacute;n
		this.font.setFontAttributes(this.sectionInfoFontColor, tamLetraDatos, false, false, false);
		iter=0; 
		for(iRow=(hasTitle)?2:1; iRow < numRows; iRow++){
		  for(iCol=0; iCol < numCols; iCol++){
		    if(iter<data.length){
		      if(data[iter] != null)
		        section.addTableTextCell(tableAlias, data[iter], this.font, iRow, iCol);      
		      iter++;
		    }
		  }
		}
	}
	
	/**
	  adiciona datos por columnas a una secci&oacute;n

	  @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
	  @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por columnas
	  @param data        array de strings que contiene los datos
	  @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
	*/
	private void addSectionColData(iTextBaseTable section, String tableAlias, String[] data,int[] alignment,float tamLetraDatos, boolean hasTitle)
	{
		int numRows, numCols;
		int iter, iCol, iRow;
		
		numRows = section.getTableReference(tableAlias).size();
		numCols = section.getTableReference(tableAlias).getColumns();
		
		// adicionamos la informaci&oacute;n
		this.font.setFontAttributes(this.sectionInfoFontColor, tamLetraDatos, false, false, false);
		iter=0; 
		for(iRow=(hasTitle)?2:1; iRow < numRows; iRow++){
		  for(iCol=0; iCol < numCols; iCol++){
		    if(iter<data.length){
		      if(data[iter] != null)
		    	section.addTableTextCellAligned(tableAlias, data[iter], this.font, alignment[iCol], iTextBaseDocument.ALIGNMENT_MIDDLE, iRow, iCol);		              
		      iter++;
		    }
		  }
		}
	}
  
  
  /**
   * adiciona datos por columnas a una secci&oacute;n
   * @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
   * @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por columnas
   * @param data        array de strings que contiene los datos
   */
	private void addSectionColDataSinTitulo(iTextBaseTable section, String tableAlias, String[] data)
	{
		int numRows, numCols;
		int iter, iCol, iRow;
		
		numRows = section.getTableReference(tableAlias).size();
		numCols = section.getTableReference(tableAlias).getColumns();
		
		section.getTableReference(tableAlias).deleteRow(0);
		
		// adicionamos la informaciï¿½n
		this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, false, false, false);
		
		
		iter=0; 
		for(iRow=0; iRow < numRows; iRow++)
		{
			for(iCol=0; iCol < numCols; iCol++)
			{
				if(iter<data.length)
				{
					if(data[iter] != null)
					{
						section.addTableTextCell(tableAlias, data[iter], this.font, iRow, iCol);      
					}
					iter++;
				}
			}
		}
	}


  /**
      adiciona datos por columnas a una secci&oacute;n

      @param sectionAlias  alias de la secci&oacute;n contenedora de la tabla
      @param tableAlias    alias de la tabla a la cual se le adicionaran los datos por columnas
      @param data          array de strings que contiene los datos
      @param hasTitle      valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
  */
  public void addSectionColData(String sectionAlias, String tableAlias, String[] data, boolean hasTitle){
    iTextBaseTable section;

    section = getSectionReference(sectionAlias);
    addSectionColData(section, tableAlias, data, hasTitle);
  }

	/**
	* adiciona datos por columnas a una secci&oacute;n
	* @param sectionAlias  alias de la secci&oacute;n contenedora de la tabla
	* @param tableAlias    alias de la tabla a la cual se le adicionaran los datos por columnas
	* @param data          array de strings que contiene los datos
	* @param hasTitle      valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
	*/
	public void addSectionColDataSinTitulo(String sectionAlias, String tableAlias, String[] data)
	{
		iTextBaseTable section;

		section = getSectionReference(sectionAlias);
		addSectionColDataSinTitulo(section, tableAlias, data);
	}

	  /**
    adiciona datos por columnas a una secci&oacute;n

    @param section     referencia a un objeto de tipo iTextBaseTable que apunta a la secci&oacute;n contenedora de la tabla
    @param tableAlias  alias de la tabla a la cual se le adicionaran los datos por columnas
    @param data        array de strings que contiene los datos
    @param hasTitle    valor boolean que indica si la secci&oacute;n ya contiene una barra de titulo de secci&oacute;n o no
	*/
	private void addSectionColDataAlignColsData(iTextBaseTable section, String tableAlias, String[] data, boolean hasTitle, int[] alignColsData)
	{
	  int numRows, numCols;
	  int iter, iCol, iRow;
	
	  numRows = section.getTableReference(tableAlias).size();
	  numCols = section.getTableReference(tableAlias).getColumns();
	
	  // adicionamos la informaci&oacute;n
	  this.font.setFontAttributes(this.sectionInfoFontColor, this.sectionInfoFontSize, false, false, false);
	  iter=0; 
	  for(iRow=(hasTitle)?2:1; iRow < numRows; iRow++)
	  	{
		    for(iCol=0; iCol < numCols; iCol++)
		    {
		      if(iter<data.length)
			      {
			        if(data[iter] != null)
			          section.addTableTextCellAligned(tableAlias, data[iter], this.font, alignColsData[iCol], iTextBaseDocument.ALIGNMENT_MIDDLE, iRow, iCol);      
			        iter++;
			      }
		    }//for
	  }//for
	}


  /**
      crea una secci&oacute;n organizada por filas y puebla su tabla padre con titulo, cabecera y datos 

      @param sectionAlias  alias de la secci&oacute;n
      @param sectionTitle  titulo que aparecer&aacute; en la barra de titulo de la secci&oacute;n
      @param header        array de strings que contiene las cabeceras de datos
      @param data          array de strings que contiene los datos
      @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
  */
  public void createRowSection(String sectionAlias, String sectionTitle, String[] header, String[] data, float tableSpacing){
    iTextBaseTable iSection;
    int numRows, numCols;

    numRows = header.length + 1;
    numCols = (data.length / header.length) + 1;

    iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
    addSectionTitle(iSection, REPORT_PARENT_TABLE, sectionTitle);
    addSectionRowHeaders(iSection, REPORT_PARENT_TABLE, header, true);
    addSectionRowData(iSection, REPORT_PARENT_TABLE, data, true);
  }



  /**
      crea una secci&oacute;n organizada por filas y llena su tabla padre con cabecera y datos; 
      esta secci&oacute;n no contiene barra de titulo

      @param sectionAlias  alias de la secci&oacute;n
      @param header        array de strings que contiene las cabeceras de datos
      @param data          array de strings que contiene los datos
      @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
  */
  public void createRowSection(String sectionAlias, String[] header, String[] data, float tableSpacing){
    iTextBaseTable iSection;
    int numRows, numCols;

    numRows = header.length;
    numCols = (data.length / header.length) + 1;

    iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
    addSectionRowHeaders(iSection, REPORT_PARENT_TABLE, header, false);
    addSectionRowData(iSection, REPORT_PARENT_TABLE, data, false);
  }



  /**
      crea una secci&oacute;n organizada por columnas y puebla su tabla padre con titulo, cabecera y datos 

      @param sectionAlias  alias de la secci&oacute;n
      @param sectionTitle  titulo que aparecer&aacute; en la barra de titulo de la secci&oacute;n
      @param header        array de strings que contiene las cabeceras de datos
      @param data          array de strings que contiene los datos
      @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
  */
  public void createColSection(String sectionAlias, String sectionTitle, String[] header, String[] data, float tableSpacing){
    iTextBaseTable iSection;
    int numRows, numCols;

    numRows = (data.length / header.length) + 2;
    numCols = header.length;
    iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
    addSectionTitle(iSection, REPORT_PARENT_TABLE, sectionTitle);
    addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, true);
    addSectionColData(iSection, REPORT_PARENT_TABLE, data, true);
  }
  
  /**
	  crea una secci&oacute;n organizada por columnas y puebla su tabla padre con titulo, cabecera y datos 
	
	  @param sectionAlias  alias de la secci&oacute;n
	  @param sectionTitle  titulo que aparecer&aacute; en la barra de titulo de la secci&oacute;n
	  @param header        array de strings que contiene las cabeceras de datos
	  @param data          array de strings que contiene los datos
	  @param int[] alignment contiene las alineaciones de los textos
	  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
   */
	public void createColSection(String sectionAlias, String sectionTitle, String[] header, String[] data, int[] alignment, float tableSpacing){
		iTextBaseTable iSection;
		int numRows, numCols;
		
		numRows = (data.length / header.length) + 2;
		numCols = header.length;
		iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
		addSectionTitle(iSection, REPORT_PARENT_TABLE, sectionTitle);
		addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, true);		
		addSectionColData(iSection, REPORT_PARENT_TABLE, data, alignment, true);
		
	}
  
  
  /**
  crea una secci&oacute;n organizada por columnas y puebla su tabla padre con titulo, cabecera y datos 

  @param sectionAlias  alias de la secci&oacute;n
  @param sectionTitle  titulo que aparecer&aacute; en la barra de titulo de la secci&oacute;n
  @param header        array de strings que contiene las cabeceras de datos
  @param data          array de strings que contiene los datos
  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
*/
public void createColSectionColsPand(String sectionAlias, String sectionTitle, String[] header, String[] data, float tableSpacing,int colSpan,String valorColSpan){
	iTextBaseTable iSection;
	int numRows, numCols;
	
	numRows = (data.length / header.length) + 2;
	numCols = header.length;	
	iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
	
	iSection.setTableCellsColSpan(numCols-colSpan);
	iSection.addTableTextCellAligned(REPORT_PARENT_TABLE,sectionTitle,this.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	iSection.setTableCellsColSpan(colSpan);		
	iSection.addTableTextCellAligned(REPORT_PARENT_TABLE,valorColSpan,this.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	
	addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, true);
	addSectionColData(iSection, REPORT_PARENT_TABLE, data, true);
	}

/**
crea una secci&oacute;n organizada por columnas y puebla su tabla padre con titulo, cabecera y datos 

@param sectionAlias  alias de la secci&oacute;n
@param sectionTitle  titulo que aparecer&aacute; en la barra de titulo de la secci&oacute;n
@param header        array de strings que contiene las cabeceras de datos
@param data          array de strings que contiene los datos
@param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
*/
public void createColSectionColsPand(String sectionAlias, String sectionTitle, String[] header, String[] data,int[] alignment, float tableSpacing,int colSpan,String valorColSpan){
	iTextBaseTable iSection;
	int numRows, numCols;
	
	numRows = (data.length / header.length) + 2;
	numCols = header.length;	
	iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
	
	iSection.setTableCellsColSpan(numCols-colSpan);
	iSection.addTableTextCellAligned(REPORT_PARENT_TABLE,sectionTitle,this.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_TOP);
	iSection.setTableCellsColSpan(colSpan);		
	iSection.addTableTextCellAligned(REPORT_PARENT_TABLE,valorColSpan,this.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_TOP);
	
	addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, true);
	addSectionColData(iSection, REPORT_PARENT_TABLE, data,alignment, true);
	}
  
  

  /**
  crea una secci&oacute;n organizada por columnas y puebla su tabla padre con cabecera y datos 

  @param sectionAlias  alias de la secci&oacute;n
  @param header        array de strings que contiene las cabeceras de datos
  @param data          array de strings que contiene los datos
  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
	*/
	public void createColSectionSinTitulo(String sectionAlias, String[] header, String[] data, float tableSpacing)
	{
		iTextBaseTable iSection;
		int numRows, numCols;
		
		numRows = (data.length / header.length) + 1;
		numCols = header.length;
		iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
		addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, false);
		addSectionColData(iSection, REPORT_PARENT_TABLE, data, false);
	}
	
	  /**
	  Crea una secciï¿½n organizada por columnas y puebla su tabla padre con cabecera y datos
	  y se especifica la alineaciï¿½n de las columnas de la tabla 
	  @param sectionAlias  alias de la secci&oacute;n
	  @param header        array de strings que contiene las cabeceras de datos
	  @param data          array de strings que contiene los datos
	  @param alignColsData  array de int que contiena la alineaciï¿½n de las columnas de datos
	  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
		*/
		public void createColSectionSinTituloAlignColsData(String sectionAlias, String[] header, String[] data, int[] alignColsData, float tableSpacing)
		{
			iTextBaseTable iSection;
			int numRows, numCols;
			
			numRows = (data.length / header.length) + 1;
			numCols = header.length;
			iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
			addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, false);
			addSectionColDataAlignColsData(iSection, REPORT_PARENT_TABLE, data, false, alignColsData);
		}

  

  /**
      crea una secci&oacute;n organizada por columnas y llena su tabla padre con cabecera y datos; 
      esta secci&oacute;n no contiene barra de titulo

      @param sectionAlias  alias de la secci&oacute;n
      @param header        array de strings que contiene las cabeceras de datos
      @param data          array de strings que contiene los datos
      @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
  */
  public void createColSection(String sectionAlias, String[] header, String[] data, float tableSpacing){
    iTextBaseTable iSection;
    int numRows, numCols;

    numRows = (data.length / header.length) + 1;
    numCols = header.length;

    iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
    addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, false);
    addSectionColData(iSection, REPORT_PARENT_TABLE, data, false);
  }
  
	/**
	  crea una secci&oacute;n organizada por columnas y llena su tabla padre con cabecera y datos; 
	  esta secci&oacute;n no contiene barra de titulo
	
	  @param sectionAlias  alias de la secci&oacute;n
	  @param header        array de strings que contiene las cabeceras de datos
	  @param data          array de strings que contiene los datos
	  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
	*/
	public void createColSection(String sectionAlias, String[] header, String[] data,int[] alignment, float tableSpacing)
	{
		iTextBaseTable iSection;
		int numRows, numCols;
		
		numRows = (data.length / header.length) + 1;
		numCols = header.length;
		
		iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
		addSectionColHeaders(iSection, REPORT_PARENT_TABLE, header, false);
		addSectionColData(iSection, REPORT_PARENT_TABLE, data,alignment, false);
	}
  

  /**
  crea una secci&oacute;n organizada por columnas y llena su tabla padre con cabecera y datos; 
  esta secci&oacute;n no contiene barra de titulo, dando el tamanio de la fuente de la cabecera y de los datos.

  @param sectionAlias  alias de la secci&oacute;n
  @param header        array de strings que contiene las cabeceras de datos
  @param data          array de strings que contiene los datos
  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
  @param size tamaï¿½o de la fuente
  */
  public void createColSection(String sectionAlias, String[] header, String[] data, float tamLetraCabecera,float tamLetraDatos,float tableSpacing)
	{
	iTextBaseTable iSection;
	int numRows, numCols;
	numRows = (data.length / header.length) + 1;
	numCols = header.length;
	
	iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
	addSectionColHeaders(iSection, REPORT_PARENT_TABLE,header,tamLetraCabecera, false);
	addSectionColData(iSection, REPORT_PARENT_TABLE, data, tamLetraDatos, false);
	}
  
  
  /**
  crea una secci&oacute;n organizada por columnas y llena su tabla padre con cabecera y datos; 
  esta secci&oacute;n no contiene barra de titulo, dando el tamanio de la fuente de la cabecera y de los datos.

  @param sectionAlias  alias de la secci&oacute;n
  @param header        array de strings que contiene las cabeceras de datos
  @param data          array de strings que contiene los datos
  @param tableSpacing  tama&ntilde;o del desplazamiento de inserci&oacute;n de esta tabla
  @param size tamaï¿½o de la fuente
  */
  public void createColSection(String sectionAlias, String[] header, String[] data,int[] alignment, float tamLetraCabecera,float tamLetraDatos,float tableSpacing)
	{
	iTextBaseTable iSection;
	int numRows, numCols;
	numRows = (data.length / header.length) + 1;
	numCols = header.length;
	
	iSection = createSection(sectionAlias, REPORT_PARENT_TABLE, numRows, numCols, tableSpacing);
	addSectionColHeaders(iSection, REPORT_PARENT_TABLE,header,tamLetraCabecera, false);
	addSectionColData(iSection, REPORT_PARENT_TABLE, data,alignment, tamLetraDatos, false);
	}


  /**
      adiciona una secci&oacute;n en el ArrayList y en el Array asociativo de secciones de este reporte

      @param sectionAlias  alias de la secci&oacute;n a registrar
      @param section       objeto de tipo iTextBaseTable que sera adicionado al array asociativo 
  */

  private void useSection(String sectionAlias, iTextBaseTable section){
    if( this.sections.containsKey(sectionAlias) == true ){
      return;
    }

    aliasArrayList.add(aliasIndex, sectionAlias);
    aliasIndex += 1;
    sections.put(sectionAlias, section);
  }
  
  /**
   * 
   * @param aliasSeccion
   * @param seccion
   */
  public void usarSeccion(String aliasSeccion,iTextBaseTable seccion)
  {
	  this.useSection(aliasSeccion,seccion);
  }

  /**
      inserta una tabla en otra tabla; donde ambas tablas deben ser de la misma secci&oacute;n

      @param sectionAlias       alias de la seccion
      @param parentTableAlias   alias de la tabla donde se insertara la tabla hija
      @param childTableAlias    alias de la tabla hija
      @param row                fila de la tabla padre donde se insertara la hija
      @param col                columna de la tabla padre donde se insertara la hija 
  */
  public void insertSectionTable(String sectionAlias, String parentTableAlias, String childTableAlias, int row, int col){
    iTextBaseTable iSection;

    iSection = (iTextBaseTable) this.sections.get(sectionAlias);
    
    if((iSection.getTableReference(parentTableAlias) == null) || (iSection.getTableReference(childTableAlias) == null)){
      return;
    }

    iSection.insertTableIntoCell(parentTableAlias, childTableAlias, row, col);
  }



  /**
      adiciona una secci&oacute;n al reporte

      @param sectionAlias  alias de la secci&oacute;n que sera a&ntilde;adida del reporte
  */
  public void addSectionToDocument(String sectionAlias){
    iTextBaseTable iSection = null;

    iSection = (iTextBaseTable) sections.get(sectionAlias);

    if( iSection != null )
      this.document.addTable(iSection);
  }


  /**
      obtiene un array de strings, el cual contiene los alias de las secciones actualmente definidas en el reporte

      @return un array de string con los alias de las secciones definidas en este reporte
  */
  public String[] getSectionAliasArray(){
    String[] aliasArray = new String[aliasArrayList.size()];
    
    aliasArray = (String[]) aliasArrayList.toArray(aliasArray);
    return aliasArray;
  }
  
	/**
	 * 
	 * @param companyLogo
	 * @param companyName
	 * @param companyNit
	 * @param reportTitle
	 * @param nombresApellidos
	 * @param tipoNumeroId
	 * @param string2 
	 * @param string 
	 */
	public void setReportBaseHeaderPrefactura1(String companyLogo, String companyName, String companyNit, String reportTitle,String nombresApellidos, String tipoNumeroId, String responsable, String cuenta, String nombreViaIngreso)
	{
		iTextBaseTable iSection;

	    iSection = new iTextBaseTable();
	    

	    iSection.useTable(REPORT_HEADER_TABLE, 8, 3);
	    iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
	    iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
	    iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
	    iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
	    iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
	    iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
	    iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

	    this.font.setFontAttributes(0x000000, 12, true, false, false);

	    float[] widths = new float[2];
	    widths[0] = 30f;
	    widths[1] = 70f;
	    setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
	    iSection.setTableCellPadding(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableCellSpacing(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.0f);
	    iSection.setTableCellsRowSpan(2);
	    iSection.addTableImageCell(REPORT_HEADER_TABLE, companyLogo, 100f, 0, 0);
	    iSection.setTableCellsRowSpan(1);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyName, this.font);
	    iSection.addTableTextCell(REPORT_HEADER_TABLE, companyNit, this.font);

	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );

	    
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Este Documento es Informativo y refleja el Estado de la Cuenta. No es una Factura de Venta", this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Informaciï¿½n Personal", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Paciente: "+nombresApellidos, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, tipoNumeroId, this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(3);
	    this.font.setFontAttributes(0x000000, 10, true, false, false);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Informaciï¿½n Responsable", this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Responsable: "+responsable , this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Cuenta: "+cuenta , this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    
	    this.font.setFontAttributes(0x000000, 8, false, false, false);
	    iSection.setTableCellsColSpan(2);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Via Ingreso: "+nombreViaIngreso , this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    iSection.setTableCellsColSpan(1);
	    iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, "Fecha Impresiï¿½n: "+UtilidadFecha.getFechaActual() , this.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	    
	    useSection(REPORT_HEADER_SECTION, iSection);
	}

	/**
	 * @return the formatoImpresion
	 */
	public String getFormatoImpresion() {
		return formatoImpresion;
	}

	/**
	 * @param formatoImpresion the formatoImpresion to set
	 */
	public void setFormatoImpresion(String formatoImpresion) {
		this.formatoImpresion = formatoImpresion;
	}

	public HashMap getMapaPiePagina() {
		return mapaPiePagina;
	}

	public void setMapaPiePagina(HashMap mapaPiePagina) {
		this.mapaPiePagina = mapaPiePagina;
	}
	
	public Object getMapaPiePagina(String key) {
		return mapaPiePagina.get(key);
	}

	public void setMapaPiePagina(String key,Object obj) {
		this.mapaPiePagina.put(key,obj);
	}

	
	/**
    crea la secci&oacute;n REPORT_HEADER_SECTION con lo b&aacute;sico que debe tener una cabecera de reporte, esto es: un logo de la compa&ntilde;ia que genera el reporte, la raz&oacute;n social de la compa&ntilde;ia, el nit de la compa&ntilde;ia y el titulo del reporte

    @param companyLogo  ruta y nombre del archivo que contiene el logo de la compa&ntilde;ia
    @param companyName  raz&oacute;n social de la compa&ntilde;ia
    @param companyNit   NIT de la compa&ntilde;ia
    @param reportTitle  titulo de este reporte
*/
public void setReportBaseHeaderWithSubtitle(String companyLogo, String companyName, String companyNit, String reportTitle,String reportSubtitle){
  iTextBaseTable iSection;

  iSection = new iTextBaseTable();
  

  iSection.useTable(REPORT_HEADER_TABLE, 4, 2);
  iSection.setTableWidth(REPORT_HEADER_TABLE, 100);
  iSection.setTableOffset(REPORT_HEADER_TABLE, 0);
  iSection.setTableBorder(REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
  iSection.setTableCellBorderWidth(REPORT_HEADER_TABLE, 0.5f);
  iSection.setTableCellPadding(REPORT_HEADER_TABLE, 1);
  iSection.setTableSpaceBetweenCells(REPORT_HEADER_TABLE, 0.5f);
  iSection.setTableCellsDefaultColors(REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
  
  if (companyNit.equals("align-left"))
  {	
  	companyNit = "";
  	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_LEFT);
  }
  else
  {
  	iSection.setTableCellsDefaultHAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER);
  }
  
  iSection.setTableCellsDefaultVAlignment(REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL);

  this.font.setFontAttributes(0x000000, 14, true, false, false);

  float[] widths = new float[2];
  widths[0] = 30f;
  widths[1] = 70f;
  setColumnsWidths(iSection, REPORT_HEADER_TABLE, widths);
  
  
  iSection.setTableCellsRowSpan(2);
  iSection.addTableImageCell(REPORT_HEADER_TABLE, companyLogo, 100f, 0, 0);
  iSection.setTableCellsRowSpan(1);
  iSection.addTableTextCell(REPORT_HEADER_TABLE, companyName, this.font);
  iSection.addTableTextCell(REPORT_HEADER_TABLE, companyNit, this.font);

  
  iSection.setTableCellsColSpan(2);
  iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportTitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
  //se adiciona el subtitulo
  iSection.addTableTextCellAligned(REPORT_HEADER_TABLE, reportSubtitle, this.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_TOP );
  useSection(REPORT_HEADER_SECTION, iSection);
}
	
  
}
