/*
 * @(#)iTextBaseTable.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package util.pdf;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;


import com.lowagie.text.*;


/**
  Clase que encapsula las propiedades y los m&eacute;todos de tablas de documentos,
  esta clase puede servir como base para crear tablas secuenciales o jerarquizadas,
  ya que contiene un array asociativo ( tipo HashMap ) que guarda la referencia de
  un conjunto de tablas con caracter&iacute;sticas individuales

  @author   Miguel Arturo D&iacute;az L&oacute;pez
  @version  1.0
*/

public class iTextBaseTable{
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(iTextBaseTable.class);

  /** HashMap contenedor de tablas ( utilizado como array asociativo ) */
  private HashMap tables;

  /** ArrayList contenedor de los alias de las tablas ( para m&eacute;todos indexados ) */
  private ArrayList aliasArrayList;
  /** &iacute;ndice de la tabla a insertar actualmente en el ArrayList */
  private int aliasIndex;

  /** row span para ser usado al insertar celdas en las tablas */
  private int tableCellRowSpan;
  /** col span para ser usado al insertar celdas en las tablas */
  private int tableCellColSpan;
  /** color de fondo usado al insertar celdas en las tablas */
  private int tableCellBackgroundColor;



  /**
      constructor por defecto
  */
  public iTextBaseTable(){
    tables = new HashMap();

    tableCellRowSpan = 1;
    tableCellColSpan = 1;
    tableCellBackgroundColor = 0xFFFFFF;

    aliasArrayList = new ArrayList();
    aliasIndex = 0;
  }


  /**
      obtiene una referencia al array asociativo que contiene las tablas de este objeto

      @return referencia al array asociativo      
  */
  public HashMap getTables(){
    return tables;
  }


  /**
      obtiene la referencia a una tabla

      @param tableAlias  alias de la tabla

      @return referencia a la tabla
  */
  public Table getTableReference(String tableAlias){
    Table rTable = null;

    rTable = (Table)tables.get(tableAlias);
    return rTable;
  }



  /**
      define una nueva tabla para ser usada por este objeto

      @param tableAlias   alias de la nueva tabla ( debe ser &uacute;nico dentro de este objeto )
      @param rows         numero de filas de esta nueva tabla
      @param cols         numero de columnas de esta nueva tabla
  */
  public void useTable(String tableAlias, int rows, int cols){
    Table rTable = null;

    try{
      rTable = new Table(cols, rows);
      if( rTable == null ){
        return;
      }

      if( tables.containsKey(tableAlias) == true ){
        rTable = null;
        return;
      }

      aliasArrayList.add(aliasIndex, tableAlias);
      aliasIndex += 1;
      rTable.setAutoFillEmptyCells(true);
      tables.put(tableAlias, rTable);
    }
    catch(BadElementException e){
        e.getMessage();
    }
  }


  /**
      define una nueva tabla para ser usada por este objeto. Inicialmente contendr&aacute; una fila

      @param tableAlias   alias de la nueva tabla ( debe ser &uacute;nico dentro de este objeto )
      @param cols         numero de columnas de esta nueva tabla
  */
  public void useTable(String tableAlias, int cols){
    Table rTable = null;

    try{
      rTable = new Table(cols);
      if( rTable == null ){
        return;
      }

      if( tables.containsKey(tableAlias) == true ){
        rTable = null;
        return;
      }

      aliasArrayList.add(aliasIndex, tableAlias);
      aliasIndex += 1;
      rTable.setAutoFillEmptyCells(true);
      tables.put(tableAlias, rTable);
    }
    catch(BadElementException e){
        e.getMessage();
    }
  }



  /**
      establece el tama&ntilde;o de una tabla

      @param tableAlias   alias de la tabla
      @param size         tama&ntilde;o de la tabla
  */
  public void setTableWidth(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setWidth(size);
  }


  /**
      establece el desplazamiento de inserci&oacute;n de esta tabla con respecto al espaciado 
      determinado por el objeto anterior

      @param tableAlias   alias de la tabla
      @param size         tama&ntilde;o del desplazamiento
  */
  public void setTableOffset(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setOffset(size);
  }



  /**
      establece el color del borde de una tabla

      @param tableAlias   alias de la tabla
      @param color        valor entero que define las componentes RGB (formato 0xRRGGBB ) del color del borde
  */
  public void setTableBorderColor(String tableAlias, int color){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setBorderColor(new java.awt.Color(color));
  }


  /**
      establece el color de fondo de una tabla

      @param tableAlias   alias de la tabla
      @param color        valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de fondo
  */
  public void setTableBackgroundColor(String tableAlias, int color){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setBackgroundColor(new java.awt.Color(color));
    
  }


  /**
      establece el tama&ntilde;o del borde de una tabla

      @param tableAlias   alias de la tabla
      @param size         tama&ntilde;o del borde
  */
  public void setTableBorderWidth(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    if(size == 0)
      rTable.setBorder(Rectangle.NO_BORDER);
    else
      rTable.setBorderWidth(size);
  }



  /**
      establece las caracter&iacute;sticas del borde de una tabla

      @param tableAlias   alias de la tabla
      @param color        valor entero que define las componentes RGB (formato 0xRRGGBB ) del color del borde
      @param size         tama&ntilde;o del borde
  */
  public void setTableBorder(String tableAlias, int color, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setBorderColor(new java.awt.Color(color));

    if(size == 0)
      rTable.setBorder(Rectangle.NO_BORDER);
    else
      rTable.setBorderWidth(size);
  }


  /**
      establece los colores por defecto que ser&aacute;n usados al insertar celdas en una tabla

      @param tableAlias        alias de la tabla
      @param backgroundColor   valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de fondo de las celdas
      @param borderColor       valor entero que define las componentes RGB (formato 0xRRGGBB ) del color del borde de las celdas
  */
  public void setTableCellsDefaultColors(String tableAlias, int backgroundColor, int borderColor){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    this.tableCellBackgroundColor = backgroundColor;
    rTable.setBackgroundColor(new java.awt.Color(backgroundColor));
    rTable.setBorderColor(new java.awt.Color(borderColor));
  }


  /**
      establece el espacio que existir&aacute; entre el borde de las celdas y su contenido

      @param tableAlias   alias de la tabla
      @param size         tama&ntilde;o entre el borde de las celdas y su contenido
  */
  public void setTableCellPadding(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setPadding(size);
  }



  /**
      establece el espacio entre celdas

      @param tableAlias   alias de la tabla
      @param size         espacio entre celdas
  */
  public void setTableCellSpacing(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setSpacing(size);
  }



  /**
      establece el tama&ntilde;o del borde de las celdas de una tabla

      @param tableAlias   alias de la tabla
      @param size         tama&ntilde;o del borde de las celdas
  */
  public void setTableCellBorderWidth(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setBorderWidth(size);
  }



  /**
      establece el espacio entre celdas

      @param tableAlias   alias de la tabla
      @param size         espacio entre celdas
  */
  public void setTableSpaceBetweenCells(String tableAlias, float size){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    rTable.setSpacing(size);
  }


  /**
      establece la alineaci&oacute;n horizontal que sera utilizada por el contenido de las celdas 
      de una tabla

      @param tableAlias   alias de la tabla
      @param hAlignment   alineaci&oacute;n horizontal
  */
  public void setTableCellsDefaultHAlignment(String tableAlias, int hAlignment){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    //rTable.setAlignment(hAlignment);
  }


  /**
      Establece la alineaci&oacute;n vertical que sera utilizada por el contenido de las celdas 
      de una tabla

      @param tableAlias   alias de la tabla
      @param vAlignment   alineaci&oacute;n vertical
  */
  public void setTableCellsDefaultVAlignment(String tableAlias, int vAlignment){
    Table rTable = null;

    rTable = getTableReference(tableAlias);
    //rTable.set(vAlignment);
  }



  /**
      establece la expansi&oacute;n entre filas de las tabla. Este valor es general a todas las tablas y sera usado al insertar celdas en cualquier tabla

      @param size  numero de filas que se deben expandir
  */
  public void setTableCellsRowSpan(int size){
    this.tableCellRowSpan = size;
  }


  /**
      establece la expansi&oacute;n entre columnas de las tabla. Este valor es general a todas las tablas y sera usado al insertar celdas en cualquier tabla

      @param size  numero de columnas que se deben expandir
  */
  public void setTableCellsColSpan(int size){
    this.tableCellColSpan = size;
  }


  /**
      establece el color de fondo a ser utilizado en la inserci&oacute;n de celdas de una tabla. Este valor es general a todas las tablas y sera usado al insertar celdas en cualquier tabla

      @param color   valor entero que define las componentes RGB (formato 0xRRGGBB ) del color de fondo de las celdas
  */
  public void setTableCellsBackgroundColor(int color){
    this.tableCellBackgroundColor = color;
  }


  /**
      adiciona una celda de texto a una tabla

      @param tableAlias  alias de la tabla
      @param cellText    texto de la celda
      @param iFont       fuente a ser utilizada
  */
  public void addTableTextCell(String tableAlias, String cellText, iTextBaseFont iFont){
    Table rTable = null;

    if(cellText == null)
    	return;

    Cell  rCell = new Cell();
    
    rTable = getTableReference(tableAlias);
    rCell.add(new Chunk(cellText, iFont.getCurrentFont()));
    rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
    rCell.setRowspan(this.tableCellRowSpan);
    rCell.setColspan(this.tableCellColSpan);
    if(rTable.getBorder()<=0)
    {
	    rCell.setBorder(rTable.getBorder());
	    //rCell.setBorder(0);
    }
    rTable.addCell(rCell);    
  }


  /**
      adiciona una celda de texto a una tabla

      @param tableAlias  alias de la tabla
      @param cellText    texto de la celda
      @param iFont       fuente a ser utilizada
      @param row         fila en la cual sera insertada esta celda
      @param col         columna en la cual sera insertada esta celda
  */
  public void addTableTextCell(String tableAlias, String cellText, iTextBaseFont iFont, int row, int col){
    Table rTable = null;
    Cell  rCell = new Cell();
    Chunk chunk = new Chunk(cellText, iFont.getCurrentFont());
    Phrase frase = new Phrase(10f);
    frase.add(chunk);
    
    try{
      rTable = getTableReference(tableAlias);
      rCell.add(frase);
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
      rCell.setBorderWidth(0.1f);
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      rCell.setVerticalAlignment(iTextBaseDocument.ALIGNMENT_TOP);
      rCell.setHorizontalAlignment(iTextBaseDocument.ALIGNMENT_CENTER);
      if(rTable.getBorder()<=0)
      {
  	    rCell.setBorder(rTable.getBorder());
  	    //rCell.setBorder(0);
      }
      rTable.addCell(rCell, new java.awt.Point(row, col));    
    }
    catch(BadElementException e){
        e.getMessage();
    }

  }



  /**
      adiciona una celda con alineaci&oacute;n de texto a una tabla

      @param tableAlias  alias de la tabla
      @param cellText    texto de la celda
      @param iFont       fuente a ser utilizada
      @param hAlignment  alineaci&oacute;n horizontal del contenido de la celda
      @param vAlignment  alineaci&oacute;n vertical del contenido de la celda
  */
  public void addTableTextCellAligned(String tableAlias, String cellText, iTextBaseFont iFont, int hAlignment, int vAlignment){
    Table rTable = null;
    Cell  rCell = new Cell();
    Chunk chunk = new Chunk(cellText, iFont.getCurrentFont());
    Phrase frase = new Phrase(10f);
    //frase.
    frase.add(chunk);
    
    rTable = getTableReference(tableAlias);
    rCell.add(frase);
    rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
    rCell.setHorizontalAlignment(hAlignment);
    rCell.setVerticalAlignment(vAlignment);
    rCell.setRowspan(this.tableCellRowSpan);
    rCell.setColspan(this.tableCellColSpan);
    if(rTable.getBorder()<=0)
    {
	    rCell.setBorder(rTable.getBorder());
	    //rCell.setBorder(0);
    }
    rTable.addCell(rCell);
  }
  
  /**
  adiciona una celda de encabezado con alineaci&oacute;n de texto a una tabla

  @param tableAlias  alias de la tabla
  @param cellText    texto de la celda
  @param iFont       fuente a ser utilizada
  @param hAlignment  alineaci&oacute;n horizontal del contenido de la celda
  @param vAlignment  alineaci&oacute;n vertical del contenido de la celda
*/
public void addTableTextCellHeaderAligned(String tableAlias, String cellText, iTextBaseFont iFont, int hAlignment, int vAlignment,int row,int col)
{
	Table rTable = null;
	Cell  rCell = new Cell();
	Chunk chunk = new Chunk(cellText, iFont.getCurrentFont());
	Phrase frase = new Phrase(10f);
	//frase.
	frase.add(chunk);
	
	rTable = getTableReference(tableAlias);
	//rCell.setHeader(true);
	rCell.add(frase);
	rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
	rCell.setHorizontalAlignment(hAlignment);
	rCell.setVerticalAlignment(vAlignment);
	rCell.setRowspan(this.tableCellRowSpan);
	rCell.setColspan(this.tableCellColSpan);
	if(rTable.getBorder()<=0)
    {
	    rCell.setBorder(rTable.getBorder());
	    //rCell.setBorder(0);
    }
	
	try 
	{
		rTable.addCell(rCell,new java.awt.Point(row, col));
	} catch (BadElementException e) 
	{
		logger.error("Error al ubicar celda: "+e);
	}
}


  /**
      adiciona una celda con alineaci&oacute;n de texto a una tabla

      @param tableAlias  alias de la tabla
      @param cellText    texto de la celda
      @param iFont       fuente a ser utilizada
      @param hAlignment  alineaci&oacute;n horizontal del contenido de la celda
      @param vAlignment  alineaci&oacute;n vertical del contenido de la celda
      @param row         fila en la cual sera insertada esta celda
      @param col         columna en la cual sera insertada esta celda
  */
  public void addTableTextCellAligned(String tableAlias, String cellText, iTextBaseFont iFont, int hAlignment, int vAlignment, int row, int col){
    Table rTable = null;
    Cell  rCell = new Cell();

    try{
      rTable = getTableReference(tableAlias);
      rCell.add(new Chunk(cellText, iFont.getCurrentFont()));
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
      rCell.setHorizontalAlignment(hAlignment);
      rCell.setVerticalAlignment(vAlignment);
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      if(rTable.getBorder()<=0)
      {
  	    rCell.setBorder(rTable.getBorder());
  	    //rCell.setBorder(0);
      }
      rTable.addCell(rCell, new java.awt.Point(row, col));
    }
    catch(BadElementException e){
        e.getMessage();
    }
  }



  /**
      adiciona una celda con una imagen

      @param tableAlias     alias de la tabla
      @param imageFilename  ruta y nombre del archivo que contiene la imagen a ser insertada
      @param imageScale     factor de escalado de la imagen
  */
  public void addTableImageCell(String tableAlias, String imageFilename, float imageScale){
    Table rTable = null;
    Cell  rCell = new Cell(); 
    Image img = null;

    try{
      rTable = getTableReference(tableAlias);
      img = Image.getInstance(imageFilename);
      img.scalePercent(imageScale);
      rCell.add(img);
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      rTable.addCell(rCell);
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
      adiciona una celda con una imagen

      @param tableAlias     alias de la tabla
      @param imageFilename  ruta y nombre del archivo que contiene la imagen a ser insertada
      @param imageScale     factor de escalado de la imagen
      @param row            fila en la cual sera insertada esta celda
      @param col            columna en la cual sera insertada esta celda
  */
  public void addTableImageCell(String tableAlias, String imageFilename, float imageScale, int row, int col){
    Table rTable = null;
    Cell  rCell = new Cell();
    Image img = null;
 
    try{
      rTable = getTableReference(tableAlias);
      img = Image.getInstance(imageFilename);
      img.scalePercent(imageScale);
      rCell.add(img);
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));  
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      rTable.addCell(rCell, new java.awt.Point(row, col));
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
      adiciona una celda con una imagen

      @param tableAlias     alias de la tabla
      @param imageFilename  ruta y nombre del archivo que contiene la imagen a ser insertada
      @param imageScale     factor de escalado de la imagen
      @param hAlignment     alineaci&oacute;n horizontal del contenido de la celda
      @param vAlignment     alineaci&oacute;n vertical del contenido de la celda
  */
  public void addTableImageCellAligned(String tableAlias, String imageFilename, float imageScale, int hAlignment, int vAlignment){
    Table rTable = null;
    Cell  rCell = new Cell();
    Image img = null;

    try{
      rTable = getTableReference(tableAlias);
      img = Image.getInstance(imageFilename);
      img.scalePercent(imageScale);
      rCell.add(img);
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
      rCell.setHorizontalAlignment(hAlignment);
      rCell.setVerticalAlignment(vAlignment);
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      rTable.addCell(rCell);
    }
    catch(java.net.MalformedURLException e)
    {
    	logger.error("Error añadiendo imagen: "+e);
        e.getMessage();
    }
    catch(IOException e){
    	logger.error("Error añadiendo imagen: "+e);
        e.getMessage();
    }
    catch(DocumentException e){
    	logger.error("Error añadiendo imagen: "+e);
        e.getMessage();
    }
  }


  /**
      adiciona una celda con una imagen

      @param tableAlias     alias de la tabla
      @param imageFilename  ruta y nombre del archivo que contiene la imagen a ser insertada
      @param imageScale     factor de escalado de la imagen
      @param hAlignment     alineaci&oacute;n horizontal del contenido de la celda
      @param vAlignment     alineaci&oacute;n vertical del contenido de la celda
      @param row            fila en la cual sera insertada esta celda
      @param col            columna en la cual sera insertada esta celda
  */
  public void addTableImageCellAligned(String tableAlias, String imageFilename, float imageScale, int hAlignment, int vAlignment, int row, int col){
    Table rTable = null;
    Cell  rCell = new Cell();
    Image img = null;

    try{
      rTable = getTableReference(tableAlias);
      img = Image.getInstance(imageFilename);
      img.scalePercent(imageScale);
      //img.scaleAbsoluteHeight(img.height());
      //img.scaleAbsoluteWidth(img.width());
      rCell.add(img);
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
      rCell.setHorizontalAlignment(hAlignment);
      rCell.setVerticalAlignment(vAlignment);
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      if(rTable.getBorder()<=0) {
  	    rCell.setBorder(rTable.getBorder());
  	  }
      rTable.addCell(rCell, new java.awt.Point(row, col));
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
      inserta una tabla ( previamente definida en este objeto ) en otra tabla cualquiera ( tambi&eacute;n previamente definida en este objeto )

      @param parentTableAlias  alias de la tabla padre
      @param nestedTableAlias  alias de la tabla que sera insertada en la tabla padre
  */
  public void insertTableIntoCell(String parentTableAlias, String nestedTableAlias){
    Table pTable = null;
    Table nTable = null;

    pTable = getTableReference(parentTableAlias);  
    nTable = getTableReference(nestedTableAlias);  

    pTable.insertTable(nTable);
  }



  /**
      inserta una tabla ( previamente definida en este objeto ) en otra tabla cualquiera ( tambi&eacute;n previamente definida en este objeto )

      @param parentTableAlias  alias de la tabla padre
      @param nestedTableAlias  alias de la tabla que sera insertada en la tabla padre
      @param row               fila de la tabla padre en la cual sera insertada esta tabla 
      @param col               columna de la tabla padre en la cual sera insertada esta tabla 
  */
  public void insertTableIntoCell(String parentTableAlias, String nestedTableAlias, int row, int col){
    Table pTable = null;
    Table nTable = null;

    pTable = getTableReference(parentTableAlias);  
    nTable = getTableReference(nestedTableAlias);  

    pTable.insertTable(nTable, row, col);
  }



  /**
      inserta una tabla ( previamente definida en este objeto ) en otra tabla cualquiera ( tambi&eacute;n previamente definida en este objeto )

      @param parentTableAlias  alias de la tabla padre
      @param nestedTableAlias  alias de la tabla que sera insertada en la tabla padre
      @param hAlignment        alineaci&oacute;n horizontal de la tabla insertada
      @param vAlignment        alineaci&oacute;n vertical de la tabla insertada
      @param row               fila de la tabla padre en la cual sera insertada esta tabla 
      @param col               columna de la tabla padre en la cual sera insertada esta tabla 
  */
  public void insertTableIntoCell(String parentTableAlias, String nestedTableAlias, int hAlignment, int vAlignment, int row, int col){
    Table pTable = null;
    Table nTable = null;
    Cell  rCell = new Cell();
    
    try{
      pTable = getTableReference(parentTableAlias);  
      nTable = getTableReference(nestedTableAlias);  
      rCell.add(nTable);
      rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
      rCell.setHorizontalAlignment(hAlignment);
      rCell.setVerticalAlignment(vAlignment);
      rCell.setRowspan(this.tableCellRowSpan);
      rCell.setColspan(this.tableCellColSpan);
      pTable.addCell(rCell, new java.awt.Point(row, col));
    }
    catch(DocumentException e){
        e.getMessage();
    }
  }
  
  /**
      Inserta una tabla en otra tabla cualquiera 
      ( previamente definida en este objeto ),
      en una fila y columna especifica de la tabla ya creada.
    
      @param parentTableAlias  alias de la tabla padre
      @param nestedTableAlias  alias de la tabla que sera insertada en la tabla padre
      @param numRows         numero de filas de esta nueva tabla
      @param numCols         numero de columnas de esta nueva tabla
      @param hAlignment        alineaci&oacute;n horizontal de la tabla insertada
      @param vAlignment        alineaci&oacute;n vertical de la tabla insertada
      @param row               fila de la tabla padre en la cual sera insertada esta tabla 
      @param col               columna de la tabla padre en la cual sera insertada esta tabla 
  */
    public void insertTableIntoCell(String parentTableAlias, String tableAlias,int numRows,int numCols,int hAlignment, int vAlignment, int row, int col)
    {
        Table pTable = null;
        Table rTable = null;
        Table nTable = null;
        Cell  rCell = new Cell();
        
        try
        {          
          rTable=new Table(numRows,numCols);
          aliasArrayList.add(aliasIndex, tableAlias);
          aliasIndex += 1;
          rTable.setAutoFillEmptyCells(true);
          tables.put(tableAlias, rTable);  
          pTable = getTableReference(parentTableAlias);  
          nTable = getTableReference(tableAlias);  
          rCell.add(nTable);
          rCell.setBackgroundColor(new java.awt.Color(this.tableCellBackgroundColor));
          rCell.setHorizontalAlignment(hAlignment);
          rCell.setVerticalAlignment(vAlignment);
          rCell.setRowspan(this.tableCellRowSpan);
          rCell.setColspan(this.tableCellColSpan);
          pTable.addCell(rCell, new java.awt.Point(row, col));
        }
        catch(DocumentException e){
            e.getMessage();
        }       
    }



  /**
      obtiene un array de strings, el cual contiene los alias de las tablas definidas en este objeto

      @return array de strings con los alias de las tablas definidas en este objeto
  */
  public String[] getTableAliasArray(){
    String[] aliasArray = new String[aliasArrayList.size()];
    
    aliasArray = (String[]) aliasArrayList.toArray(aliasArray);
    return aliasArray;
  }


}

