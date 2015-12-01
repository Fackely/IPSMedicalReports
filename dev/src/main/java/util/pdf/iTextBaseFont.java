/*
 * @(#)iTextBaseFont.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package util.pdf;


import com.lowagie.text.pdf.*;
import com.lowagie.text.*;



/**

  Clase que encapsula las propiedades y m&eacute;todos para la manipulaci&oacute;n de fuentes que ser&aacute;n utilizadas en el documento.
  En esta versi&oacute;n est&aacute;n soportadas las siguientes fuentes:

  <li> Courier </li>
  <li> Courier-Bold </li>
  <li> Courier-Oblique </li>
  <li> Courier-BoldOblique </li>
  <br>
  <li> Helvetica </li>
  <li> Helvetica-Bold </li>
  <li> Helvetica-Oblique </li>
  <li> Helvetica-BoldOblique </li>
  <br>
  <li> Times-Roman </li>
  <li> Times-Bold </li>
  <li> Times-Italic </li>
  <li> Times-BoldItalic </li>
  <br>
  <li> Symbol </li>
  <br>
  <li> ZapfDingbats </li>

  @author   Miguel Arturo D&iacute;az L&oacute;pez
  @version  1.0

*/

public class iTextBaseFont{

  /** objeto Font que mantiene la fuente a utilizar actualmente */
  private Font font;
  /** Cadena que apunta a la definici&oacute;n completa de la fuente actual */
  private String fontFamily;
  /** Entero que apunta al tipo de fuente actualmente usado */
  private int fontName;
  /** Color actual de la fuente */
  private int fontColor;
  /** Tama&ntilde;o actual de la fuente */
  private float fontSize;


  // definici&oacute;n de las constantes para la selecci&oacute;n de fuentes
  /** fuente de texto tipo COURIER */
  public static final int FONT_COURIER      = Font.COURIER;
  /** fuente de texto tipo HELVETICA */
  public static final int FONT_HELVETICA    = Font.HELVETICA;
  /** fuente de texto tipo TIMES ROMAN */
  public static final int FONT_TIMES        = Font.TIMES_ROMAN;
  /** fuente de texto tipo SYMBOL */
  public static final int FONT_SYMBOL       = Font.SYMBOL;
  /** fuente de texto tipo ZAPFDINGBATS */
  public static final int FONT_ZAPFDINGBATS = Font.ZAPFDINGBATS;


  /**
      constructor por defecto 
  */
  public iTextBaseFont(){
    font = new Font();
    font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    font.setColor(new java.awt.Color(0x000000));
    font.setSize(12);
    fontFamily = FontFactory.TIMES_ROMAN;
    fontName = FONT_TIMES;
    fontColor = 0x000000;
    fontSize = 12;
  }




  /**
      obtiene una copia de la fuente actualmente utilizada incluyendo todas las caracter&iacute;sticas 

      @return una nueva instancia de un objeto Font, el cual es una copia exacta de la fuente actualmente utilizada
  */
  public Font cloneCurrentFont(){
    Font tfont = null;

    if(font != null){
      tfont = new Font();
      tfont = FontFactory.getFont(fontFamily);
      tfont = tfont.difference(font);
    }

    return tfont;
  }



  /**
      obtiene una referencia a la fuente actual

      @return una referencia a la fuente actual
  */
  public Font getCurrentFont(){
    return font;
  }


  /**
      obtiene la fuente base actualmente utilizada

      @return una referencia a la fuente base utilizada actualmente por la fuente 
  */
  public BaseFont getCurrentBaseFont(){
    return this.font.getBaseFont();
  }


  /**
      establece la fuente que se desea utilizar actualmente. Si la fuente no ha sido 
      seleccionada anteriormente entonces la nueva fuente sera a&ntilde;adida a las fuentes 
      del documento autom&aacute;ticamente

      @param fontType   tipo de la fuente a utilizar
      @param bold       valor booleano que define si la fuente tiene la propiedad <b> negrita </b> o no la tiene
      @param italic     valor booleano que define si la fuente tiene la propiedad <i> it&aacute;lica </i> o no la tiene
      @param oblique    valor booleano que define si la fuente tiene la propiedad <oblique> oblicua </oblique> o no la tiene
  */
  public void useFont(int fontType, boolean bold, boolean italic, boolean oblique){

    fontFamily = "";

    switch(fontType){
      case FONT_COURIER: 

        fontName = FONT_COURIER;
        fontFamily = FontFactory.COURIER;

        if( (bold == true) && (oblique == false) ){
          fontFamily = FontFactory.COURIER_BOLD;
        }

        if( (bold == false) && (oblique == true) ){
          fontFamily = FontFactory.COURIER_OBLIQUE;
        }

        if( (bold == true) && (oblique == true) ){
          fontFamily = FontFactory.COURIER_BOLDOBLIQUE;
        }
      break;

      case FONT_HELVETICA:

        fontName = FONT_HELVETICA;
        fontFamily = FontFactory.HELVETICA;

        if( (bold == true) && (oblique == false) ){
          fontFamily = FontFactory.HELVETICA_BOLD;
        }

        if( (bold == false) && (oblique == true) ){
          fontFamily = FontFactory.HELVETICA_OBLIQUE;
        }

        if( (bold == true) && (oblique == true) ){
          fontFamily = FontFactory.HELVETICA_BOLDOBLIQUE;
        }
      break;

      case FONT_SYMBOL:
        fontName = FONT_SYMBOL;
        fontFamily = FontFactory.SYMBOL;
      break;


      case FONT_TIMES:

        fontName = FONT_TIMES;
        fontFamily = FontFactory.TIMES_ROMAN;

        if( (bold == true) && (italic == false) ){
          fontFamily = FontFactory.TIMES_BOLD;
        }

        if( (bold == false) && (italic == true) ){
          fontFamily = FontFactory.TIMES_ITALIC;
        }

        if( (bold == true) && (italic == true) ){
          fontFamily = FontFactory.TIMES_BOLDITALIC;
        }
      break;

      case FONT_ZAPFDINGBATS:
        fontName = FONT_ZAPFDINGBATS;
        fontFamily = FontFactory.ZAPFDINGBATS;
      break;
    }

    if( fontFamily.equals("") ){
      fontName = FONT_TIMES;
      fontFamily = FontFactory.TIMES_ROMAN;
    }

    font = new Font();
    font = FontFactory.getFont(fontFamily);
  }



  /**
      establece las propiedades de la fuente actualmente seleccionada

      @param color      valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de la fuente
      @param size       tama&ntilde;o de la fuente
      @param bold       valor booleano que define si la fuente tiene la propiedad <b> negrita </b> o no la tiene
      @param italic     valor booleano que define si la fuente tiene la propiedad <i> it&aacute;lica </i> o no la tiene
      @param oblique    valor booleano que define si la fuente tiene la propiedad <o> oblicua </o> o no la tiene
  */

  public void setFontAttributes(int color, float size, boolean bold, boolean italic, boolean oblique){
    useFont(this.fontName, bold, italic, oblique);

    this.fontColor = color;
    font.setColor(new java.awt.Color(color));

    this.fontSize = size;
    font.setSize(size);
  }



  /**
      establece el color y las propiedades de la fuente actualmente seleccionada

      @param color      valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de la fuente
      @param bold       valor booleano que define si la fuente tiene la propiedad <b> negrita </b> o no la tiene
      @param italic     valor booleano que define si la fuente tiene la propiedad <i> it&aacute;lica </i> o no la tiene
      @param oblique    valor booleano que define si la fuente tiene la propiedad <oblique> oblicua </oblique> o no la tiene
  */
  public void setFontColorAndAttributes(int color, boolean bold, boolean italic, boolean oblique){
    useFont(this.fontName, bold, italic, oblique);

    this.fontColor = color;
    font.setColor(new java.awt.Color(color));

    font.setSize(this.fontSize);
  }



  /**
      establece el tama&ntilde;o y las propiedades de la fuente actualmente seleccionada

      @param size       tama&ntilde;o de la fuente
      @param bold       valor booleano que define si la fuente tiene la propiedad <b> negrita </b> o no la tiene
      @param italic     valor booleano que define si la fuente tiene la propiedad <i> it&aacute;lica </i> o no la tiene
      @param oblique    valor booleano que define si la fuente tiene la propiedad <oblique> oblicua </oblique> o no la tiene
  */
  public void setFontSizeAndAttributes(float size, boolean bold, boolean italic, boolean oblique){
    useFont(this.fontName, bold, italic, oblique);

    font.setColor(new java.awt.Color(this.fontColor));

    this.fontSize = size;
    font.setSize(size);
  }
  
  public void setFontSizeAndAttributes(int fontType,float size, boolean bold, boolean italic, boolean oblique){
	    useFont(fontType, bold, italic, oblique);
	    font.setColor(new java.awt.Color(this.fontColor));
	    this.fontSize = size;
	    font.setSize(size);
	  }


}
