
/*
 * Creado   23-feb-2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.util.birt.reports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;

import com.ibm.icu.util.ULocale;


/** 
 *
 * @version 1.0, 23-feb-2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class DesignEngineApi 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(DesignEngineApi.class);
	
    private PropertyHandle computedColumns = null;
    private SessionHandle sessionHandle;
    private ReportDesignHandle reportDesignHandle;
    private ElementFactory elementFactory;
    private DesignElementHandle designElementHandle;
    private OdaDataSourceHandle odaDataSourceHandle;
    private OdaDataSetHandle odaDataSetHandle;
    private EngineConfig engineConfig;
    private GridHandle gridHandle;
    private MasterPageHandle masterPageHandle;
    private TableHandle tableHandle;
    private PropertyHandle propertyHandle;
    private SimpleMasterPageHandle simpleMasterPageHandle;
    private ColumnHandle columnHandle;
    private RowHandle rowHandle;
    private CellHandle cellHandle;
    private ImageHandle imageHandle;
    private LabelHandle labelHandle;
    
    private String reportFileName = null;
    private String pathReportFileName = null;
    private String pathReportDesign = null;
    private String dataSrc = null;
    private String dataSet = null;
    private String pathFileCvs = "";
    
    /**
     * Constructor para inicializar las variables 
     * que contendran el design, para obtener sus 
     * componentes de tipo data set.
     * @param fileName
     * @param dataSrc
     * @param dataSet
     * @author jarloc
     */
    public DesignEngineApi(String pathReportDesign,String fileName)
    {
        this.pathReportDesign=pathReportDesign;
        this.reportFileName = fileName; 
        this.obtenerDesignEngine();
    }   
   /**
    * Metodo para configurar el engine 
    * @author jarloc
    */
    private void obtenerDesignEngine ()
    {   
    	DesignConfig config = new DesignConfig( );
    	IDesignEngine engine = null;
    	
        engineConfig = new EngineConfig( );
        engineConfig.setEngineHome( ParamsBirtApplication.getBirtRuntimePath() );       
        //engineConfig.setEngineHome( "/usr/birtruntime/birt-runtime-2_0_1/ReportEngine" );
        
        config.setProperty("BIRT_HOME", ParamsBirtApplication.getBirtRuntimePath());
        try
		{
			Platform.startup(config);
		} 
        catch (BirtException e1)
		{
			e1.printStackTrace();
		}
        IDesignEngineFactory factor = (IDesignEngineFactory) Platform.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
        engine = factor.createDesignEngine(config);
        sessionHandle = engine.newSessionHandle(ULocale.ENGLISH);
        //sessionHandle = DesignEngine.newSession(null);      
        
        try 
        {
        	logger.info("\nthis.pathReportDesign->"+this.pathReportDesign+this.reportFileName);
            reportDesignHandle = sessionHandle.openDesign( this.pathReportDesign+this.reportFileName );
        } 
        catch (DesignFileException e) 
        {
            e.printStackTrace();
        }       
        elementFactory = reportDesignHandle.getElementFactory();        
        return;
    }   
    /**
     * Metodo para obtener componentes del design de tipo
     * data set.
     * @author jarloc
     */
    public void obtenerComponentesDataSet (String dataSet)
    {        
        this.dataSet = dataSet; 
        String tmp;         
        SlotHandle sloth = reportDesignHandle.getDataSources();
        java.util.Iterator iter = sloth.iterator();            
        // buscar el data set especifico
        sloth = reportDesignHandle.getDataSets();
        iter = sloth.iterator();
        while (iter.hasNext() == true)
        {
            odaDataSetHandle = (OdaDataSetHandle) iter.next();
            tmp = odaDataSetHandle.getName();
            if (tmp.compareTo(dataSet) == 0)
                break;
        }        
        return;
    }
    /**
     * Metodo para obtener componentes del design de tipo
     * data source
     * @param dataSrc
     * @author jarloc
     */
    public void obtenerComponentesDataSource(String dataSrc)
    {
        this.dataSrc = dataSrc;
        String tmp;   
        SlotHandle sloth = reportDesignHandle.getDataSources();
        java.util.Iterator iter = sloth.iterator();            
        // buscar el data source especifico
        while (iter.hasNext() == true)
        {
            odaDataSourceHandle = (OdaDataSourceHandle) iter.next();
            tmp = odaDataSourceHandle.getName();
            if (tmp.compareTo(dataSrc) == 0)
                break;
        }
    }    
    /**
     * 
     * @param gridName
     * @author jarloc
     */
    @SuppressWarnings("unused")
    public void obtenerComponentesGridBody(String gridName)
    {
        String tmp = null; 
        SlotHandle sloth = reportDesignHandle.getBody();
        java.util.Iterator iter=sloth.iterator();        
        while (iter.hasNext())
        {
            gridHandle = (GridHandle) iter.next();
            tmp=gridHandle.getName();
            //if (tmp.compareTo(gridName) == 0)
                //break;
        }        
    }    
    
    public void obtenerComponentesTableBody()
    {
        SlotHandle sloth =  reportDesignHandle.getBody();
        java.util.Iterator iter=sloth.iterator();        
        while (iter.hasNext())
        {
            tableHandle = (TableHandle) iter.next();            
        } 
    }
    
    /**
     * Metodo para insertar una cuadricula en La plantilla del
     * cuerpo (Body) del design, donde se insertara los datos.
     * @param posRow -> fila en la cual se insertara la cuadricula
     * @param posCell -> celda de la fila en la cual se insertara la cuadricula
     * @param numCols -> número de columnas de la cuadricula
     * @param numRows -> número de filas de la cuadricula
     * @param nombre -> Nombre de la Grilla
     */
    public void insertGridInBodyPageWithName(int posRow,int posCell,int numCols,int numRows,String nombre)
    {
        Iterator iter=obtenerBodyPage();
        if (iter.hasNext())
        {
            gridHandle = (GridHandle) iter.next();
            rowHandle=(RowHandle) gridHandle.getRows().get(posRow);
            cellHandle=(CellHandle)rowHandle.getCells().get(posCell);  
            gridHandle=elementFactory.newGridItem(null,numCols,numRows);       
            
            try 
            {
            	/*gridHandle.setProperty(Style.PAGE_BREAK_AFTER_PROP,DesignChoiceConstants.PAGE_BREAK_ALWAYS);
                gridHandle.setProperty(Style.PAGE_BREAK_BEFORE_PROP,DesignChoiceConstants.PAGE_BREAK_ALWAYS);
                gridHandle.setProperty(Style.PAGE_BREAK_INSIDE_PROP,DesignChoiceConstants.PAGE_BREAK_AUTO);
                gridHandle.setWidth("100%");
                gridHandle.setHeight("100%");
                gridHandle.setName(nombre);
                cellHandle.getContent().add(gridHandle);
                cellHandle.setProperty(Style.PAGE_BREAK_BEFORE_PROP,DesignChoiceConstants.PAGE_BREAK_ALWAYS);
                cellHandle.setProperty(Style.PAGE_BREAK_AFTER_PROP,DesignChoiceConstants.PAGE_BREAK_ALWAYS);
                cellHandle.setProperty(Style.PAGE_BREAK_INSIDE_PROP,DesignChoiceConstants.PAGE_BREAK_AUTO);*/
            	gridHandle.setProperty(Style.PAGE_BREAK_AFTER_PROP,DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS);
                gridHandle.setProperty(Style.PAGE_BREAK_BEFORE_PROP,DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS);
                //gridHandle.setProperty(Style.PAGE_BREAK_INSIDE_PROP,DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO);
                gridHandle.setWidth("100%");
                gridHandle.setHeight("100%");
                gridHandle.setName(nombre);
                cellHandle.getContent().add(gridHandle);
                cellHandle.setProperty(Style.PAGE_BREAK_BEFORE_PROP,DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS);
                cellHandle.setProperty(Style.PAGE_BREAK_AFTER_PROP,DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS);
                //cellHandle.setProperty(Style.PAGE_BREAK_INSIDE_PROP,DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO);
            } 
            catch (ContentException e) 
            {
                e.printStackTrace();
            } 
            catch (NameException e) 
            {
                e.printStackTrace();
            } 
            catch (SemanticException e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Metodo para insertar una tabla por fuera de La plantilla del
     * cuerpo (Body) del design, donde se insertara los datos.
     * @param numCols -> número de columnas de la cuadricula
     * @param numRows -> número de filas de la cuadricula
     * @param nombre -> Nombre de la Grilla
     */
    public void insertTableInBodyPageWithName(int numCols,int numRows,String nombre)
    {
    	SlotHandle sloth = reportDesignHandle.getBody();
    	TableHandle tableHandle=elementFactory.newTableItem(null, numCols, 1, numRows, 0);
    	try 
    	{
			tableHandle.setProperty(Style.PAGE_BREAK_AFTER_PROP,DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID);
			tableHandle.setProperty(Style.PAGE_BREAK_BEFORE_PROP,DesignChoiceConstants.PAGE_BREAK_BEFORE_AVOID);
			/**tableHandle.setProperty(Style.PAGE_BREAK_INSIDE_PROP,DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO);*/
	        tableHandle.setWidth("100%");
	        tableHandle.setHeight("100%");
	        tableHandle.setName(nombre);
	        sloth.paste(tableHandle);
	        
		} catch (SemanticException e) 
		{
			logger.error("Error ingresando tabla al cuerpo del documento: "+e);
		}
        
    	
    }
    
    /**
     * Metodo para para obtener el header del master page en
     * el design, para realizar los cambios correspondientes.
     * @return Iterator, con los elementos que posee el header
     * @author jarloc
     */
    private Iterator obtenerMasterPageHeader()
    {
        Iterator iter2=null;
        Iterator iter=obtenerMasterPage();
        if(iter.hasNext())
        {                           
            simpleMasterPageHandle=(SimpleMasterPageHandle) iter.next();               
            SlotHandle sloth=simpleMasterPageHandle.getPageHeader();
            iter2=sloth.iterator();                        
        }        
        return iter2;
    }

/*ORGANIZAR METODO PARA OBTENER NUMERO DE COLUMNAS DEL GRID DEL MASTER PAGE
 * PARA UBICACION DE LOGOS
    public int obtenerNumColsGridHeader() {
	    //Iterator iter=obtenerMasterPageHeader();
        Iterator iter = obtenerGridPpalHeaderMasterPage();
    	
	    int a=0;
	    if (iter.hasNext()) {
	        gridHandle = (GridHandle) iter.next();
	        SlotHandle sloth = gridHandle.getRows();
	        
	        int numCols = gridHandle.getColumnCount();
	        logger.info("MMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	        logger.info("sloth: " + sloth);
	        logger.info("grid handle: " + gridHandle);
	        

	        logger.info("numCols: " + numCols + " -- a: " + a);
	        //Iterator iter1=sloth.iterator();
	        //if (iter1.hasNext())
	        return numCols;
	    }
        //return iter1;
	    return 1;
	    //return ConstantesBD.codigoNuncaValido;
    }
    */    
    
    
    /**
     * Metodo para obtener el MasterPage del
     * design.
     * @return Iterator, con el componente MasterPage
     * @author jarloc
     */
    private Iterator obtenerMasterPage()
    {       
    	if(reportDesignHandle!=null){
    		
    		SlotHandle sloth = reportDesignHandle.getMasterPages();   
            java.util.Iterator iter=sloth.iterator();        
            if (iter.hasNext())
            {
                masterPageHandle=(MasterPageHandle) iter.next(); 
                SlotHandle sloth2=masterPageHandle.getContainerSlotHandle();
                java.util.Iterator iter1=sloth2.iterator();   
                if (iter1.hasNext())           
                   return  iter1;           
            }
    	}
        
        return null;
    }
    
    /**
     * Metodo para obtener el Body del
     * design.
     */
    private Iterator obtenerBodyPage()
    {        
        SlotHandle sloth = reportDesignHandle.getBody();
        java.util.Iterator iter=sloth.iterator();
        return iter;
    }
    
    /**
     * Metodo para para obtener el footer del master page en
     * el design, para realizar los cambios correspondientes.
     * @return Iterator, con los elementos que posee el footer
     */
    private Iterator obtenerMasterPageFooter()
    {
        Iterator iter2=null;
        Iterator iter=obtenerMasterPage();
        
        if(iter!=null){
        	
    	   if(iter.hasNext())
           {
               simpleMasterPageHandle=(SimpleMasterPageHandle) iter.next();               
               SlotHandle sloth=simpleMasterPageHandle.getPageFooter();
               iter2=sloth.iterator(); 
               return iter2; 
           }
        }
     
        return iter2;
    }    
    /**
     * Metodo para insertar el logo en la págian maestra,
     * donde se encuentra el Header que se repite en todas
     * las páginas del reporte.
     * El diseño del master page debe tener definida una 
     * cuadricula, por lo menos con una fila y una columna,
     * donde se insertara la imagen.
     * @param row int, fila donde se insertara la imagen.
     * @param cell int, celda donde se insertara la imagen.
     * @param filePathImage String, ruta de la imagen.
     * @author jarloc
     */
    public void insertImageHeaderOfMasterPage1(int row,int cell,String filePathImage)
    {
        Iterator iter=obtenerMasterPageHeader();     
        if (iter.hasNext())
        {            
            gridHandle = (GridHandle) iter.next();            
            rowHandle=(RowHandle) gridHandle.getRows().get(row);
            cellHandle=(CellHandle)rowHandle.getCells().get(cell);
            imageHandle=elementFactory.newImage(null);
            try 
            {                                  
                imageHandle.setFile( "\""+ filePathImage +"\"");
//                imageHandle.setHeight(ConstantesBD.altoImagenesBirt);
                imageHandle.setWidth(ConstantesBD.anchoImagenesBirt);
                cellHandle.getContent().add(imageHandle);                                 
            } 
            catch (ContentException e) 
            { 
                 e.printStackTrace();
             } catch (NameException e1) 
             {    
                 e1.printStackTrace();
             } catch (SemanticException e2) 
             { 
                 e2.printStackTrace();
             }   
        }        
    }
    
    /**
     * Metodo para insertar el logo en la págian maestra,
     * donde se encuentra el Header que se repite en todas
     * las páginas del reporte.
     * El diseño del master page debe tener definida una 
     * cuadricula, por lo menos con una fila y una columna,
     * donde se insertara la imagen centrada.
     * @param row int, fila donde se insertara la imagen.
     * @param cell int, celda donde se insertara la imagen.
     * @param filePathImage String, ruta de la imagen.
     */
    public void insertImageHeaderOfMasterPageCenter(int row,int cell,String filePathImage)
    {
        Iterator iter=obtenerMasterPageHeader();     
        if (iter.hasNext())
        {            
            gridHandle = (GridHandle) iter.next();            
            rowHandle=(RowHandle) gridHandle.getRows().get(row);
            cellHandle=(CellHandle)rowHandle.getCells().get(cell);
            imageHandle=elementFactory.newImage(null);
            try 
            {                                  
                imageHandle.setFile( "\""+ filePathImage +"\"");
                imageHandle.setHeight(ConstantesBD.altoImagenesBirt);
                imageHandle.setWidth(ConstantesBD.anchoImagenesBirt);
                cellHandle.getContent().add(imageHandle);   
                cellHandle.setProperty(Style.TEXT_ALIGN_PROP,DesignChoiceConstants.TEXT_ALIGN_CENTER);
            } 
            catch (ContentException e) 
            { 
                 e.printStackTrace();
             } catch (NameException e1) 
             {    
                 e1.printStackTrace();
             } catch (SemanticException e2) 
             { 
                 e2.printStackTrace();
             }   
        }        
    }
    
    /**
     * 
     * @param row
     * @param cell
     * @param filePathImage
     */
    public void insertImageFooterOfMasterPage1(int row,int cell,String filePathImage)
    {
        Iterator iter=obtenerMasterPageFooter();     
        
        if(iter!=null){
        	
        	if (iter.hasNext()){
        		
        		gridHandle = (GridHandle) iter.next();            
        		rowHandle=(RowHandle) gridHandle.getRows().get(row);
        		cellHandle=(CellHandle)rowHandle.getCells().get(cell);
        		imageHandle=elementFactory.newImage(null);
        		
        		try{
        			imageHandle.setFile( "\""+ filePathImage +"\"");
        			cellHandle.getContent().add(imageHandle);
        			
        		}catch (ContentException e){
        			
        			e.printStackTrace();
        			
        		}catch (NameException e1) 
        		{
        			e1.printStackTrace();
        			
        		}catch (SemanticException e2) 
        		{
        			e2.printStackTrace();
        		}   
        	}
        }
    }
    
    
    /**
     * 
     * @param row
     * @param cell
     * @param filePathImage
     * @param nombreGrilla
     */
    public void insertImageBodyPage(int row,int cell,String filePathImage, String nombreGrilla)
    {
        Iterator iter=obtenerBodyPage();     
        while (iter.hasNext())
        {
        	try
        	{
	            gridHandle = (GridHandle) iter.next();      
	            if(gridHandle.getName().equals(nombreGrilla))
	            {	
		            rowHandle=(RowHandle) gridHandle.getRows().get(row);
		            cellHandle=(CellHandle)rowHandle.getCells().get(cell);
		            imageHandle=elementFactory.newImage(null);
		            try 
		            {                                  
		                imageHandle.setFile( "\""+ filePathImage +"\"");
		                cellHandle.getContent().add(imageHandle);                                 
		            } 
		            catch (ContentException e) 
		            { 
		                 e.printStackTrace();
		             } catch (NameException e1) 
		             {    
		                 e1.printStackTrace();
		             } catch (SemanticException e2) 
		             { 
		                 e2.printStackTrace();
		             } 
	            }
        	}
        	catch (Exception e) {
			}
        }        
    }
    
    /**
     * 
     * @param row
     * @param cell
     * @param filePathImage
     * @param nombreGrilla
     */
    public void insertLabelBodyPage(int row,int cell,String text, String nombreGrilla)
    {
        Iterator iter=obtenerBodyPage();     
        while (iter.hasNext())
        {
        	try
        	{
	            gridHandle = (GridHandle) iter.next();      
	            if(gridHandle.getName().equals(nombreGrilla))
	            {	
		            rowHandle=(RowHandle) gridHandle.getRows().get(row);
		            cellHandle=(CellHandle)rowHandle.getCells().get(cell);
		            labelHandle=elementFactory.newLabel("pieHisCli");
		            try 
		            {                                  
		                labelHandle.setText(text);
		                cellHandle.getContent().add(labelHandle);                                 
		            } 
		            catch (ContentException e) 
		            { 
		                 e.printStackTrace();
		             } catch (NameException e1) 
		             {    
		                 e1.printStackTrace();
		             } catch (SemanticException e2) 
		             { 
		                 e2.printStackTrace();
		             } 
	            }
        	}
        	catch (Exception e) {
			}
        }        
    }
    
    /**
     * Metodo para insertar una cuadricula en el Header del
     * master page del design, donde se insertara los datos.
     * @param numCols int, número de columnas de la cuadricula
     * @param numRows int, número de filas de la cuadricula
     * @param posRow int,fila en la cual se insertara la cuadricula
     * @param posCell int, celda de la fila en la cual se insertara la cuadricula
     * @author jarloc
     */
    public void insertGridHeaderOfMasterPage(int posRow,int posCell,int numCols,int numRows)
    {
        insertGridHeaderOfMasterPageWithName(posRow,posCell,numCols,numRows,"dataHeaderMasterPage");
    }
    
    /**
     * Metodo para insertar una cuadricula en el Header del
     * master page del design, donde se insertara los datos.
     * Se diferencia del método insertGridHeaderOfMasterPage porque
     * aquí se especifica el nombre de la cuadrícula
     * @param numCols int, número de columnas de la cuadricula
     * @param numRows int, número de filas de la cuadricula
     * @param posRow int,fila en la cual se insertara la cuadricula
     * @param posCell int, celda de la fila en la cual se insertara la cuadricula
     * @param nombre => nombre de la cuadrícula
     */
    public void insertGridHeaderOfMasterPageWithName(int posRow,int posCell,int numCols,int numRows,String nombre)
    {
        Iterator iter=obtenerMasterPageHeader();
        if (iter.hasNext())
        {
            gridHandle = (GridHandle) iter.next();            
            rowHandle=(RowHandle) gridHandle.getRows().get(posRow);
            cellHandle=(CellHandle)rowHandle.getCells().get(posCell);  
            gridHandle=elementFactory.newGridItem(null,numCols,numRows);            
            try 
            {
                gridHandle.setWidth("100%");
                gridHandle.setHeight("100%");
                gridHandle.setName(nombre);
                cellHandle.getContent().add(gridHandle);
            } catch (ContentException e) {
                e.printStackTrace();
            } catch (NameException e) {
                e.printStackTrace();
            } catch (SemanticException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Metodo para insertar labels que contendran la 
     * información de la institución en el header del
     * master page, estos labels seran insertados en 
     * la cuadricula que se inserto en el header, para 
     * este fin.
     * @param posRow int, fila en la que se encuentra la cuadricula
     * @param posCell int, celda en la que se encuentra la cuadricula
     * @author jarloc
     */
    public void insertLabelInGridOfMasterPage(int posRow,int posCell,Vector vDataLabels)
    {
    	insertLabelInGridOfMasterPageWithProperties(posRow,posCell,vDataLabels,DesignChoiceConstants.TEXT_ALIGN_CENTER);
    }
    
    /**
     * Metodo para insertar labels que contendran la 
     * información de la institución en el header del
     * master page, estos labels seran insertados en 
     * la cuadricula que se inserto en el header, para 
     * este fin.
     * @param posRow int, fila en la que se encuentra la cuadricula
     * @param posCell int, celda en la que se encuentra la cuadricula
     * @author jarloc
     */
    public void insertLabelInGridOfMasterPage(int posRow,int posCell,ArrayList<Object> aDataLabels)
    {
    	insertLabelInGridOfMasterPageWithProperties(posRow,posCell,aDataLabels,DesignChoiceConstants.TEXT_ALIGN_CENTER);
    }
    
    /**
     * Metodo para insertar labels que contendran la 
     * información de la institución en el header del
     * master page, estos labels seran insertados en 
     * la cuadricula que se inserto en el header, para 
     * este fin.
     * @param posRow int, fila en la que se encuentra la cuadricula
     * @param posCell int, celda en la que se encuentra la cuadricula
     * @param texto de alineacion: DesignChoiceConstants.TEXT_ALIGN_CENTER 
     * @author sebastian
     */
    public void insertLabelInGridOfMasterPageWithProperties(int posRow,int posCell,Vector vDataLabels,String styleAlign)
    {
    	//Se obtiene encabezado de la página maestra
        Iterator iter=obtenerMasterPageHeader();
        int contLabels=0;
        if(iter.hasNext())
        {
        	//logger.info("paso por aqui A");
        	//Se obtiene la cuadrícula del encabezado
            Iterator iter1=obtenerGridPpalHeaderMasterPage();
            if (iter1.hasNext())
            {
            	//logger.info("paso por aqui B");
            	//Se obtiene la celda de la cuadrícula del encabezado
               Iterator iter2=obtenerCellHeaderOfMasterPage(iter1, posRow, posCell);
               while (iter2.hasNext())
               {   
            	   //logger.info("paso por aqui C");
            	   //se obtiene la cuadrícula de esa celda
                   gridHandle=(GridHandle) iter2.next();
                   //se obtienen las filas de la cuadrícula
                   SlotHandle sloth2=gridHandle.getRows();
                   Iterator iter3=sloth2.iterator();
                   //se iteran las filas de la cuadrícula
                   while (iter3.hasNext())
                   {                       
                	   //logger.info("paso por aqui D");
                       rowHandle=(RowHandle)iter3.next();
                       //Se obtienen las celdas de la cuadrícula
                       SlotHandle sloth3=rowHandle.getCells();
                       java.util.Iterator iter4=sloth3.iterator();
                       //Se iteran las celdas de la fila
                       while (iter4.hasNext())
                       {
                    	   //logger.info("paso por aqui E");
                    	   //se obtiene una celda
                           cellHandle=(CellHandle)iter4.next();
                           //se crea un nuevo label
                           labelHandle=elementFactory.newLabel("label"+contLabels);
                           
                           try 
                           {
                        	   labelHandle.setProperty(Style.TEXT_ALIGN_PROP,styleAlign);
                        	   
                               if(contLabels>=vDataLabels.size())//en el caso de que el vector posea menos datos que numero de labels
                                   labelHandle.setText("");
                               else
                               {
                            	   
                            	   if(vDataLabels.get(contLabels)!=null)
                            		   labelHandle.setText((String) vDataLabels.get(contLabels));
                            	   else
                            		   labelHandle.setText("");
                               }
                               cellHandle.getContent().add(labelHandle);
                               
                               cellHandle.setProperty(Style.VERTICAL_ALIGN_PROP,DesignChoiceConstants.VERTICAL_ALIGN_TOP);
                           } catch (ContentException e) {
                               e.printStackTrace();
                           } catch (NameException e) {
                               e.printStackTrace();
                           } catch (SemanticException e) {
                               e.printStackTrace();
                           }
                           contLabels++;
                       }
                   }
               }              
            }
        }
        
    }
    
    /**
     * Metodo para insertar labels que contendran la 
     * información de la institución en el header del
     * master page, estos labels seran insertados en 
     * la cuadricula que se inserto en el header, para 
     * este fin.
     * @param posRow int, fila en la que se encuentra la cuadricula
     * @param posCell int, celda en la que se encuentra la cuadricula
     * @param texto de alineacion: DesignChoiceConstants.TEXT_ALIGN_CENTER 
     * @author sebastian
     */
    public void insertLabelInGridOfMasterPageWithProperties(int posRow,int posCell,ArrayList<Object> aDataLabels,String styleAlign)
    {
    	//Se obtiene encabezado de la página maestra
        Iterator iter=obtenerMasterPageHeader();
        int contLabels=0;
        if(iter.hasNext())
        {
        	//logger.info("paso por aqui A");
        	//Se obtiene la cuadrícula del encabezado
            Iterator iter1=obtenerGridPpalHeaderMasterPage();
            if (iter1.hasNext())
            {
            	//logger.info("paso por aqui B");
            	//Se obtiene la celda de la cuadrícula del encabezado
               Iterator iter2=obtenerCellHeaderOfMasterPage(iter1, posRow, posCell);
               while (iter2.hasNext())
               {   
            	   //logger.info("paso por aqui C");
            	   //se obtiene la cuadrícula de esa celda
                   gridHandle=(GridHandle) iter2.next();
                   //se obtienen las filas de la cuadrícula
                   SlotHandle sloth2=gridHandle.getRows();
                   Iterator iter3=sloth2.iterator();
                   //se iteran las filas de la cuadrícula
                   while (iter3.hasNext())
                   {                       
                	   //logger.info("paso por aqui D");
                       rowHandle=(RowHandle)iter3.next();
                       //Se obtienen las celdas de la cuadrícula
                       SlotHandle sloth3=rowHandle.getCells();
                       java.util.Iterator iter4=sloth3.iterator();
                       //Se iteran las celdas de la fila
                       while (iter4.hasNext())
                       {
                    	   //logger.info("paso por aqui E");
                    	   //se obtiene una celda
                           cellHandle=(CellHandle)iter4.next();
                           //se crea un nuevo label
                           labelHandle=elementFactory.newLabel("label"+contLabels);
                           
                           try 
                           {
                        	   labelHandle.setProperty(Style.TEXT_ALIGN_PROP,styleAlign);
                        	   
                               if(contLabels>=aDataLabels.size())//en el caso de que el vector posea menos datos que numero de labels
                                   labelHandle.setText("");
                               else
                               {
                            	   
                            	   if(aDataLabels.get(contLabels)!=null)
                            		   labelHandle.setText((String) aDataLabels.get(contLabels));
                            	   else
                            		   labelHandle.setText("");
                               }
                               cellHandle.getContent().add(labelHandle);
                               
                               cellHandle.setProperty(Style.VERTICAL_ALIGN_PROP,DesignChoiceConstants.VERTICAL_ALIGN_TOP);
                           } catch (ContentException e) {
                               e.printStackTrace();
                           } catch (NameException e) {
                               e.printStackTrace();
                           } catch (SemanticException e) {
                               e.printStackTrace();
                           }
                           contLabels++;
                       }
                   }
               }              
            }
        }
        
    }
    
    
    
    /**
     * Metodo para insertar labels que contendran la 
     * información de las columnas en el cuerpo del
     * Body Page del reporte, estos labels seran insertados en 
     * la cuadricula que se inserto en el cuepro, para 
     * este fin.
     * @param posRow
     * @param posCell
     * @param vDataLabels
     * @author cperalta
     */
    public void insertLabelInGridOfBodyPage(int posRow,int posCell,Vector vDataLabels)
    {
    	//Se obtiene el cody
        Iterator iter=obtenerBodyPage();
        int contLabels=0;
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del body
            Iterator iter1=obtenerGridPpalBodyPage();
            if (iter1.hasNext())
            {                
            	//Se obtiene la celda de la cuadrícula del body
               Iterator iter2=obtenerCellOfBodyPage(iter1, posRow, posCell);
               while (iter2.hasNext())
               {   
            	   //se obtiene la cuadrícula de esa celda
                   gridHandle=(GridHandle) iter2.next();
                   //se obtienen las filas de la cuadrícula
                   SlotHandle sloth2=gridHandle.getRows();
                   Iterator iter3=sloth2.iterator();
                   //se iteran las filas de la cuadrícula
                   while (iter3.hasNext())
                   {                       
                       rowHandle=(RowHandle)iter3.next();
                       //Se obtienen las celdas de la cuadrícula
                       SlotHandle sloth3=rowHandle.getCells();
                       java.util.Iterator iter4=sloth3.iterator();
                       //Se iteran las celdas de la fila
                       while (iter4.hasNext())
                       {
                    	   //se obtiene una celda
                           cellHandle=(CellHandle)iter4.next();
                           //se crea un nuevo label
                           labelHandle=elementFactory.newLabel("label"+contLabels);
                           try 
                           {                               
                               if(contLabels>=vDataLabels.size())
                               {//en el caso de que el vector posea menos datos que numero de labels
                                   labelHandle.setText("");
                               }
                               else
                               {
                            	   
                            	   if(vDataLabels.get(contLabels)!=null)
                            	   {
                            		   labelHandle.setText(vDataLabels.get(contLabels)+"");
                            	   }
                            	   else
                            	   {
                            		   labelHandle.setText("");
                            	   }
                               }
                               cellHandle.getContent().add(labelHandle);
                               
                               cellHandle.setProperty(Style.VERTICAL_ALIGN_PROP,DesignChoiceConstants.VERTICAL_ALIGN_TOP);
                           } 
                           catch (ContentException e) 
                           {
                               e.printStackTrace();
                           } 
                           catch (NameException e) 
                           {
                               e.printStackTrace();
                           } 
                           catch (SemanticException e) 
                           {
                               e.printStackTrace();
                           }
                           contLabels++;
                       }
                   }
               }              
            }
        }
        
    }
    
    
    /**
     * Metodo para insertar labels que contendran la 
     * información de las columnas en el cuerpo del
     * Body Page del reporte, estos labels seran insertados en 
     * la cuadricula que se inserto en el cuepro, para 
     * este fin.
     * @param vDataLabels
     * @param nombreGrilla
     * @author sgomez
     */
    public void insertLabelInTableOfBodyPage(Vector vDataLabels,String nombreGrilla)
    {
    	tableHandle = obtenerTablaBodyPageByName(nombreGrilla);
    	
    	int contLabels = 0;
    	
    	//se obtienen las filas de la cuadrícula
        SlotHandle sloth2=tableHandle.getDetail();
        Iterator iter3=sloth2.iterator();
        //se iteran las filas de la cuadrícula
        while (iter3.hasNext())
        { 
        	rowHandle=(RowHandle)iter3.next();
            //Se obtienen las celdas de la cuadrícula
            SlotHandle sloth3=rowHandle.getCells();
            Iterator iter4=sloth3.iterator();
            //Se iteran las celdas de la fila
            while (iter4.hasNext())
            {
            	//se obtiene una celda
                cellHandle=(CellHandle)iter4.next();
                //se crea un nuevo label
                labelHandle=elementFactory.newLabel("label"+contLabels);
                try 
                {                               
                    if(contLabels>=vDataLabels.size())
                    {//en el caso de que el vector posea menos datos que numero de labels
                        labelHandle.setText("");
                    }
                    else
                    {
                 	   
                 	   if(vDataLabels.get(contLabels)!=null)
                 	   {
                 		   logger.info("INSERTARPÉ LABEL DEL VECTOR("+contLabels+"): "+vDataLabels.get(contLabels));
                 		   labelHandle.setText(vDataLabels.get(contLabels)+"");
                 	   }
                 	   else
                 	   {
                 		  logger.info("ES NULO SE INSERTARÁ EN BLANCO ");
                 		   labelHandle.setText("");
                 	   }
                    }
                    cellHandle.getContent().add(labelHandle);
                    
                    cellHandle.setProperty(Style.VERTICAL_ALIGN_PROP,DesignChoiceConstants.VERTICAL_ALIGN_TOP);
                } 
                catch (ContentException e) 
                {
                    e.printStackTrace();
                } 
                catch (NameException e) 
                {
                    e.printStackTrace();
                } 
                catch (SemanticException e) 
                {
                    e.printStackTrace();
                }
                logger.info("el contador va en "+contLabels);
                contLabels++;
            }
        }
    	
    	
    }
    
    
    
    
    /**
     * Metodo para insertar un label en la cuadricula
     * principal del header, en una fila dentro de una
     * celda especifica de esta fila.
     * @param posRow int,fila en la que se encuentra la celda
     * @param posCell int, celda en la que se insertara el label
     * @param data String,con la información que se muestra en el label
     * @author jarloc
     */
    public void insertLabelInGridPpalOfHeader(int posRow,int posCell,String data)
    {
        Iterator iter=obtenerMasterPageHeader();        
        if(iter.hasNext())
        {
            Iterator iter1=obtenerGridPpalHeaderMasterPage();
            if (iter1.hasNext())
            {                
                rowHandle=(RowHandle)iter1.next();               
                rowHandle=(RowHandle)gridHandle.getRows().get(posRow);
                cellHandle=(CellHandle)rowHandle.getCells().get(posCell);
                labelHandle=elementFactory.newLabel("label");
                try
                {
                    labelHandle.setText(data);
                    cellHandle.getContent().add(labelHandle);
                } catch (ContentException e) {
                    e.printStackTrace();
                } catch (NameException e) {
                    e.printStackTrace();
                } catch (SemanticException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Metodo para insertar un label con su respectiva 
     * información, en la cuadricula principal del footer
     * del master page. 
     * 
     * @param posRow int, fila en la cual se encuentra la celda
     * @param posCell int, celda en la cual se insertara el label
     * @param data String, con la información del label
     * @author jarloc
     */
    public void insertLabelInGridPpalOfFooter(int posRow,int posCell,String data)
    {
        Iterator iter=obtenerGridPpalFooterMasterPage(); 
        
        Log4JManager.info("llegue hasta aca" );
        
        if (iter!=null){
        	
        	if(iter.hasNext())
            {
                rowHandle=(RowHandle)iter.next();               
                rowHandle=(RowHandle)gridHandle.getRows().get(posRow);
                cellHandle=(CellHandle)rowHandle.getCells().get(posCell);
                labelHandle=elementFactory.newLabel("label");
                try
                {
                    labelHandle.setText(data);
                    cellHandle.getContent().add(labelHandle);
                } catch (ContentException e) {
                    e.printStackTrace();
                } catch (NameException e) {
                    e.printStackTrace();
                } catch (SemanticException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Metodo para obtener la cuadricula principal del
     * footer en el master page.
     * @return Iterator, con la cuadricula
     */
    private Iterator obtenerGridPpalFooterMasterPage()
    {
        Iterator iter=obtenerMasterPageFooter();
        while (iter.hasNext())
        {
            gridHandle = (GridHandle) iter.next();
            SlotHandle sloth=gridHandle.getRows();
            Iterator iter1=sloth.iterator();
            if (iter1.hasNext())
                return iter1;
        }
        return null;
    }
    /**
     * Metodo para obtener la cuadricula insertada en el
     * header del master page, la cual contiene todos los
     * componentes del header.
     * @return Iterator, con la cuadricula
     * @author jarloc
     */
    private Iterator obtenerGridPpalHeaderMasterPage()
    {
        Iterator iter=obtenerMasterPageHeader();
        while (iter.hasNext())
        {
            gridHandle = (GridHandle) iter.next();
            SlotHandle sloth=gridHandle.getRows();
            
            Iterator iter1=sloth.iterator();
            if (iter1.hasNext())
                return iter1;
        }
        return null;
    }
    
    /**
     * Metodo para obtener el gris principal del 
     * body, el grid ya esta definido por defecto
     * @return iteraror
     * @author cperalta
     */
    private Iterator obtenerGridPpalBodyPage()
    {
        Iterator iter=obtenerBodyPage();
        while (iter.hasNext())
        {
        	try
        	{
        		//Se intenta hacer cast
        		gridHandle = (GridHandle) iter.next();
	            SlotHandle sloth=gridHandle.getRows();
	            Iterator iter1=sloth.iterator();
	            if (iter1.hasNext())
	                return iter1;
        	}
        	catch (ClassCastException e) 
        	{
				logger.info("Error al tratar de hacer CAst de un agrilla ene l BodyPage: "+e);
			}
        }
        return null;
    }
    
    /**
     * Metodo para obtener una grilla del cuerpo
     * @return iteraror
     * @author cperalta
     */
    private TableHandle obtenerTablaBodyPageByName(String nombre)
    {
        Iterator iter=obtenerBodyPage();
        while (iter.hasNext())
        {
        	try
        	{
	            tableHandle = (TableHandle) iter.next();
	            if(tableHandle.getName()!=null&&tableHandle.getName().equals(nombre))
	            {
	            	logger.info("ENCONTRE tabla CON NOMBRE=> "+nombre);
	                return tableHandle;
	            }
        	}
        	catch(Exception e)
        	{
        		logger.error("Falló el cast");
        	}
        }
        return null;
    }
    
    /**
     * Metodo para obtener una celda, de la cuadricula 
     * principal que contiene todos los componentes del 
     * header, Esta celda se obtine para iterar los 
     * componentes de esta.
     * @param iter Iterator, cuadricula principal del header
     * @param posRow int, fila de la cuadricula ppal
     * @param posCell int, celda de la cuadricula ppal
     * @return Iterator, con los componentes de la celda
     * @author jarloc
     */
    private Iterator obtenerCellHeaderOfMasterPage(Iterator iter,int posRow,int posCell)
    {
        rowHandle=(RowHandle)iter.next();
        rowHandle=(RowHandle)gridHandle.getRows().get(posRow);
        cellHandle=(CellHandle)rowHandle.getCells().get(posCell);
        SlotHandle sloth1=cellHandle.getContent();
        Iterator iter1=sloth1.iterator();
        if (iter1.hasNext())
            return iter1;
        return null;
    }
    
    
    /**
     * Metodo para obtener una celda, de la cuadricula 
     * principal que contiene todos los componentes del 
     * cupero del reporte, Esta celda se obtine para iterar los 
     * componentes de esta.
     * @param iter
     * @param posRow
     * @param posCell
     * @return Iterator
     * @author cperalta
     */
    private Iterator obtenerCellOfBodyPage(Iterator iter,int posRow,int posCell)
    {
        //rowHandle=(RowHandle)iter.next();
        rowHandle=(RowHandle)gridHandle.getRows().get(posRow);
        cellHandle=(CellHandle)rowHandle.getCells().get(posCell);
        SlotHandle sloth1=cellHandle.getContent();
        Iterator iter1=sloth1.iterator();
        if (iter1.hasNext())
            return iter1;
        return null;
    }
    
    /**
     * Método implementado para aplicar ColSpan
     * a la celda de una cuadrícula que se encuentre en una
     * celda de la cuarícula del Encabezado
     * @param posRow => Número de fila de la cuadrícula del encabezado
     * @param posCell => numero de Columna de la cuadrícula del encabezado
     * @param posRowGrid => Número de fila de la cuadrícula interna
     * @param posCellGrid => Número de Columna de la cuadrícula interna
     * @param numSpan => número de Colspan a aplicar en la celda
     * @param alineación del texto => constantes de alineación de texto (DesignChoiceConstants)
     */
    public void colSpanCellGridHeaderOfMasterPage(int posRow,int posCell,int posRowGrid,int posCellGrid,int numSpan,String alineacion)
    {
    	//Se obtiene el Encabezado de la Página principal
    	Iterator iter=obtenerMasterPageHeader();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Encabezado
            Iterator iter1=obtenerGridPpalHeaderMasterPage();
            if (iter1.hasNext())
            {                
            	//Se obtiene una celda del encabezado (En donde se encuentra la cuadrícula que se va a modificar)
               Iterator iter2=obtenerCellHeaderOfMasterPage(iter1, posRow, posCell);
               if (iter2.hasNext())
               {                   
                   gridHandle=(GridHandle) iter2.next();
                   //Se obtiene la celda específica de la cuadrícula a la cual se le aplicará el
                   //colpsan
                   rowHandle=(RowHandle)gridHandle.getRows().get(posRowGrid);
                   
                   cellHandle=(CellHandle)rowHandle.getCells().get(posCellGrid);
                   
                   try 
                   {
						cellHandle.setColumnSpan(numSpan);
						//Se asigna la alineacion
						
						cellHandle.setProperty(Style.TEXT_ALIGN_PROP,alineacion);
					   //Se eliminan las celdas restantes
	                   for(int i=numSpan-1;i>0;i--)
	                   {
	                	   if(rowHandle.getCells().get(i).canDrop())
	                		   rowHandle.getCells().get(i).drop();
	                   }
					} 
                   catch (SemanticException e) 
                   {
						e.printStackTrace();
					}
                   
                   
                   
               }              
            }
        }
    }
    
    /**
     *  Método implementado para aplicar Negrilla u otra propiedad (FONT_WEIGHT_BOLD)
     * a la celda de una cuadrícula que se encuentre en una
     * celda de la cuarícula del Encabezado
     * @param posRow => Número de fila de la cuadrícula del encabezado
     * @param posCell => numero de Columna de la cuadrícula del encabezado
     * @param posRowGrid => Número de fila de la cuadrícula interna
     * @param posCellGrid => Número de Columna de la cuadrícula interna
     * @param estilo => constantes de estilo de texto (DesignChoiceConstants.FONT_WEIGHT_...)
     */
    public void fontWeightCellGridHeaderOfMasterPage(int posRow,int posCell,int posRowGrid,int posCellGrid,String estilo)
    {
    	//Se obtiene el Encabezado de la Página principal
    	Iterator iter=obtenerMasterPageHeader();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Encabezado
            Iterator iter1=obtenerGridPpalHeaderMasterPage();
            if (iter1.hasNext())
            {                
            	//Se obtiene una celda del encabezado (En donde se encuentra la cuadrícula que se va a modificar)
               Iterator iter2=obtenerCellHeaderOfMasterPage(iter1, posRow, posCell);
               if (iter2.hasNext())
               {                   
                   gridHandle=(GridHandle) iter2.next();
                   //Se obtiene la celda específica de la cuadrícula a la cual se le aplicará el
                   //colpsan
                   rowHandle=(RowHandle)gridHandle.getRows().get(posRowGrid);
                   
                   cellHandle=(CellHandle)rowHandle.getCells().get(posCellGrid);
                   
                   try 
                   {
						//Se asigna la alineacion
						cellHandle.setProperty(Style.FONT_WEIGHT_PROP,estilo);
					 
					} 
                   catch (SemanticException e) 
                   {
						e.printStackTrace();
					}
                   
                   
                   
               }              
            }
        }
    }
    
    /**
     *  Método implementado para aplicar tamaño letra 
     * a la celda de una cuadrícula que se encuentre en una
     * celda de la cuarícula del Encabezado
     * @param posRow => Número de fila de la cuadrícula del encabezado
     * @param posCell => numero de Columna de la cuadrícula del encabezado
     * @param posRowGrid => Número de fila de la cuadrícula interna
     * @param posCellGrid => Número de Columna de la cuadrícula interna
     * @param tamano => tamaño
     */
    public void fontSizeCellGridHeaderOfMasterPage(int posRow,int posCell,int posRowGrid,int posCellGrid,int size)
    {
    	//Se obtiene el Encabezado de la Página principal
    	Iterator iter=obtenerMasterPageHeader();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Encabezado
            Iterator iter1=obtenerGridPpalHeaderMasterPage();
            if (iter1.hasNext())
            {                
            	//Se obtiene una celda del encabezado (En donde se encuentra la cuadrícula que se va a modificar)
               Iterator iter2=obtenerCellHeaderOfMasterPage(iter1, posRow, posCell);
               if (iter2.hasNext())
               {                   
                   gridHandle=(GridHandle) iter2.next();
                   //Se obtiene la celda específica de la cuadrícula a la cual se le aplicará el
                   //colpsan
                   rowHandle=(RowHandle)gridHandle.getRows().get(posRowGrid);
                   
                   cellHandle=(CellHandle)rowHandle.getCells().get(posCellGrid);
                   
                   try 
                   {
						//Se asigna la alineacion
						cellHandle.setProperty(Style.FONT_SIZE_PROP,size);
					 
					} 
                   catch (SemanticException e) 
                   {
						e.printStackTrace();
					}
                   
                   
                   
               }              
            }
        }
    }
    
    
    /**
     * Método para aplicar negrilla a una fila determinada
     * de una cuadricula
     * @param posRow
     * @param posCell
     * @param posRowGrid -> Numero de fila dentro de un Grid a la que se aplicara negrilla
     * @param estilo -> Estilo que quiero aplicar
     */
    public void fontWeightRowGridBodyPage(int posRow, int posCell, int posRowGrid, String estilo)
    {
    	//Se obtiene el Body de la pagina
    	Iterator iter = obtenerBodyPage();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Body
            Iterator iter1 = obtenerGridPpalBodyPage();
            if (iter1.hasNext())
            {                
            	
            	// Se obtiene la celda de la cuadrícula del body
                Iterator iter2 = obtenerCellOfBodyPage(iter1, posRow, posCell);
                while (iter2.hasNext())
                {   
             	   //se obtiene la cuadrícula de esa celda
                    gridHandle = (GridHandle) iter2.next();
                    //se obtienen las filas de la cuadrícula
                    SlotHandle sloth2 = gridHandle.getRows();
                    Iterator iter3 = sloth2.iterator();
                    //se iteran las filas de la cuadrícula
                    while (iter3.hasNext())
                    {   
                    	rowHandle = (RowHandle) iter3.next();
                        //Se obtiene la fila específica de la cuadrícula a la cual se le aplicará
                        //la negrilla
                        rowHandle = (RowHandle)gridHandle.getRows().get(posRowGrid);
                        
                        try
                        {
                        	rowHandle.setProperty(Style.FONT_WEIGHT_PROP, estilo);
                        }
                        catch (SemanticException e) 
                        {
     						e.printStackTrace();
     					}
                    }
                }
            }
        }
    }
    
    /**
     * Método para aplicar negrilla a una tabla determinada
     * del cuerpo del documento
     * @param posRowTabla -> Numero de fila dentro de un Tabla a la que se aplicara negrilla
     * @param estilo -> Estilo que quiero aplicar
     * @param nombreTabla
     */
    public void fontWeightRowTableBodyPage(int posRowTable, String estilo,String nombreTabla)
    {
    	tableHandle = obtenerTablaBodyPageByName(nombreTabla);
    	
    	rowHandle = (RowHandle)tableHandle.getDetail().get(posRowTable);
        
        try
        {
        	rowHandle.setProperty(Style.FONT_WEIGHT_PROP, estilo);
        }
        catch (SemanticException e) 
        {
				e.printStackTrace();
		}
    	
    }
    
    
    /**
     * Método para aplicar algun tipo de alineamiento a una columna determinada
     * dentro de la grilla del Body
     * @param posRow
     * @param posCell
     * @param posColGrid 
     * @param estilo
     */
    public void colAlingmentGridBodyPage(int posRow, int posCell, int posColGrid, String estilo)
    {
    	//Se obtiene el Body de la pagina
    	Iterator iter = obtenerBodyPage();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Body
            Iterator iter1 = obtenerGridPpalBodyPage();
            if (iter1.hasNext())
            {                
            	
            	// Se obtiene la celda de la cuadrícula del body
                Iterator iter2 = obtenerCellOfBodyPage(iter1, posRow, posCell);
                while (iter2.hasNext())
                {   
             	   //se obtiene la cuadrícula de esa celda
                    gridHandle = (GridHandle) iter2.next();
                    //se obtienen las columnas de la cuadrícula
                    SlotHandle sloth2 = gridHandle.getColumns();
                    Iterator iter3 = sloth2.iterator();
                    //se iteran las columnas de la cuadrícula
                    while (iter3.hasNext())
                    {   
                    	columnHandle = (ColumnHandle) iter3.next();
                        //Se obtiene la columna específica de la cuadrícula a la cual se le aplicará
                        //la alineacion
                    	columnHandle = (ColumnHandle)gridHandle.getColumns().get(posColGrid);
                        
                        try
                        {
                        	columnHandle.setProperty(Style.TEXT_ALIGN_PROP, estilo);
                        }
                        catch (SemanticException e) 
                        {
     						e.printStackTrace();
     					}
                    }
                }
            }
        }
    }
    
    /**
     * Método para aplicar borde a una grilla 
     * dentro de la grilla del Body
     * @param posRow
     * @param posCell
     * @param posColGrid 
     * @param valor (El valor debe ser en pulgadas)
     */
    public void borderGridBodyPage(int posRow, int posCell, String valor)
    {
    	//Se obtiene el Body de la pagina
    	Iterator iter = obtenerBodyPage();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Body
            Iterator iter1 = obtenerGridPpalBodyPage();
            if (iter1.hasNext())
            {                
            	
            	// Se obtiene la celda de la cuadrícula del body
                Iterator iter2 = obtenerCellOfBodyPage(iter1, posRow, posCell);
                if (iter2.hasNext())
                {   
             	   //se obtiene la cuadrícula de esa celda
                    gridHandle = (GridHandle) iter2.next();
                    //se obtienen las filas de la cuadrícula
                    SlotHandle sloth2 = gridHandle.getRows();
                    Iterator iter3 = sloth2.iterator();
                    //se iteran las filas de la cuadrícula
                    while (iter3.hasNext())
                    {   
                    	rowHandle = (RowHandle) iter3.next();
                        
                    	SlotHandle sloth3 = rowHandle.getCells();
                    	Iterator iter4 = sloth3.iterator();
                    	
                    	while(iter4.hasNext())
                    	{
                    		cellHandle = (CellHandle) iter4.next();
                    		try 
                    		{
								cellHandle.setProperty(Style.BORDER_BOTTOM_WIDTH_PROP,valor);
								cellHandle.setProperty(Style.BORDER_BOTTOM_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
								cellHandle.setProperty(Style.BORDER_LEFT_WIDTH_PROP,valor);
								cellHandle.setProperty(Style.BORDER_LEFT_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
	    						cellHandle.setProperty(Style.BORDER_RIGHT_WIDTH_PROP,valor);
	    						cellHandle.setProperty(Style.BORDER_RIGHT_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
	    						cellHandle.setProperty(Style.BORDER_TOP_WIDTH_PROP,valor);
	    						cellHandle.setProperty(Style.BORDER_TOP_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
							} 
                    		catch (SemanticException e) 
                    		{
								
								e.printStackTrace();
							}
    						
                    		
                    	}
                        
                        
                        
                    }
                  
                   
                }
            }
        }
    }
    
    /**
     * Método para aplicar borde a una grilla 
     * dentro de la grilla del Body
     * @param posRow
     * @param posCell
     * @param posColGrid 
     * @param valor (El valor debe ser en pulgadas)
     */
    public void borderTableBodyPage(String valor,String nombreTabla)
    {
    	tableHandle = obtenerTablaBodyPageByName(nombreTabla);
    	
    	//se obtienen las filas de la cuadrícula
        SlotHandle sloth2 = tableHandle.getDetail();
        Iterator iter3 = sloth2.iterator();
        //se iteran las filas de la cuadrícula
        while (iter3.hasNext())
        {   
        	rowHandle = (RowHandle) iter3.next();
            
        	SlotHandle sloth3 = rowHandle.getCells();
        	Iterator iter4 = sloth3.iterator();
        	
        	while(iter4.hasNext())
        	{
        		cellHandle = (CellHandle) iter4.next();
        		try 
        		{
					cellHandle.setProperty(Style.BORDER_BOTTOM_WIDTH_PROP,valor);
					cellHandle.setProperty(Style.BORDER_BOTTOM_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
					cellHandle.setProperty(Style.BORDER_LEFT_WIDTH_PROP,valor);
					cellHandle.setProperty(Style.BORDER_LEFT_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
					cellHandle.setProperty(Style.BORDER_RIGHT_WIDTH_PROP,valor);
					cellHandle.setProperty(Style.BORDER_RIGHT_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
					cellHandle.setProperty(Style.BORDER_TOP_WIDTH_PROP,valor);
					cellHandle.setProperty(Style.BORDER_TOP_STYLE_PROP,DesignChoiceConstants.LINE_STYLE_SOLID);
				} 
        		catch (SemanticException e) 
        		{
					
					e.printStackTrace();
				}
				
        		
        	}
            
            
            
        }
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Método para aplicar WIDTH (pulgadas) a una columna determinada
     * dentro de la grilla del Body
     * @param posRow
     * @param posCell
     * @param posColGrid 
     * @param valor (El valor debe ser en pulgadas)
     */
    public void colWidthGridBodyPage(int posRow, int posCell, int posColGrid, String valor)
    {
    	//Se obtiene el Body de la pagina
    	Iterator iter = obtenerBodyPage();
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del Body
            Iterator iter1 = obtenerGridPpalBodyPage();
            if (iter1.hasNext())
            {                
            	
            	// Se obtiene la celda de la cuadrícula del body
                Iterator iter2 = obtenerCellOfBodyPage(iter1, posRow, posCell);
                while (iter2.hasNext())
                {   
             	   //se obtiene la cuadrícula de esa celda
                    gridHandle = (GridHandle) iter2.next();
                    //se obtienen las columnas de la cuadrícula
                    SlotHandle sloth2 = gridHandle.getColumns();
                    Iterator iter3 = sloth2.iterator();
                    //se iteran las columnas de la cuadrícula
                    while (iter3.hasNext())
                    {   
                    	columnHandle = (ColumnHandle) iter3.next();
                        //Se obtiene la columna específica de la cuadrícula a la cual se le aplicará
                        //la alineacion
                    	columnHandle = (ColumnHandle)gridHandle.getColumns().get(posColGrid);
                        
                        try
                        {
                        	columnHandle.getWidth().setValue(valor);
                        	
                        	columnHandle.getWidth().setAbsolute(Double.parseDouble(valor));
                        }
                        catch (SemanticException e) 
                        {
     						e.printStackTrace();
     					}
                    }
                }
            }
        }
    }
    
    /**
     * Método para aplicar WIDTH (pulgadas) a una columna determinada
     * dentro de la tabla del Body
     * @param posColTabla
     * @param valor (El valor debe ser en pulgadas)
     * @param nombreTabla
     */
    public void colWidthTableBodyPage(int posColTable, String valor,String nombreTabla)
    {
    	tableHandle = obtenerTablaBodyPageByName(nombreTabla);
    	
        columnHandle = (ColumnHandle)tableHandle.getColumns().get(posColTable);
            
        try
        {
        	columnHandle.getWidth().setValue(valor);
        	
        	columnHandle.getWidth().setAbsolute(Double.parseDouble(valor));
        }
        catch (SemanticException e) 
        {
			e.printStackTrace();
		}
        
    }
    
    
    
    
    /**
     * Metodo para modificar el query de un data set
     * especifio
     * @param query String    
     * @author jarloc 
     */
    public void modificarQueryDataSet (String query) 
    {        
        try 
        {
            odaDataSetHandle.setQueryText(query);
        } catch (SemanticException e)
        {        
            e.printStackTrace();
        }       
        return;
    }
    /**
     * Metodo para obtener el query de un data set
     * previamente cargado.
     * @return String, con el query actual
     * @author jarloc
     */
    public String obtenerQueryDataSet()
    {
        return odaDataSetHandle.getQueryText();
    }
    /**
     * metodo para salvar el reporte despues de generar los cambios,
     * el reporte sera guardado con el mismo nombre y la misma extensión
     * en el caso en que el parametro recibido sea true, de lo contrario
     * se gnera un nuevo nombre para el design.
     * @param boolean saveOriginal, si el archivo se desea salvar 
     *        con el mismo nombre del diseño original.
     *@return String, con la ruta donde fue salvado el reporte, 
     *        y si cambio el nombre del design. 
     *        En caso de error retorna vacio.
     *@warning Tener mucho cuidado cuando se envie el boolean como parametro
     *         a este metodo, puesto que si es true se modifica la plantilla 
     *         original, y no hay como recuperarla.     
     *@author jarloc     
     */
    public String saveReport1 (boolean saveOriginal)
    {
        /// salvar el diseño y cerrarlo
        this.pathReportFileName="";
        try
        {            
            if(saveOriginal)
                this.pathReportFileName=this.pathReportDesign+this.reportFileName;
            else
            {
                int posTmp=this.reportFileName.indexOf(".");                
                String tmp=this.reportFileName.substring(0, posTmp);                
                Random r=new Random();
                int rTmp=r.nextInt();      
                File directorio= new File(ParamsBirtApplication.getReportsPathTemp());
                if(!directorio.exists())
                    directorio.mkdir();
                this.pathReportFileName=ParamsBirtApplication.getReportsPathTemp()+tmp+rTmp+".rptdesign";   
            }
            reportDesignHandle.saveAs(this.pathReportFileName);
            
           
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return "";
        }       
        reportDesignHandle.close ();        
        return this.pathReportFileName;
    }
    
   
    /**
     * Método para generar el archivo CSV después de generar el reporte
     * @param saveOriginal
     */
    public void generateCSVFile(boolean saveOriginal)
    {
    	// salvar el diseño y cerrarlo
        
    	
    	String pathReportDocument = "";
    	
    	pathReportDocument = this.pathReportFileName.replaceAll("rptdesign", "rptdocument");
    	logger.info("NOMBRE DEL ARCHIVO .RPTDOCUMNET: "+pathReportDocument);
    	
    	if(saveOriginal)
    	{
    		this.pathFileCvs = this.reportFileName.substring(0, this.reportFileName.indexOf("."))+".csv";
    	}
    	else
    	{
    		int posTmp=this.reportFileName.indexOf(".");                
            String tmp=this.reportFileName.substring(0, posTmp);                
            Random r=new Random();
            int rTmp=r.nextInt();
    		this.pathFileCvs = tmp+rTmp+".csv";
    	}
    	
    	//objetoEngine.exportCSV(pathReportDocument, ValoresPorDefecto.getFilePath()+this.pathFileCvs);
    	EngineApi.exportCSVStatic(this.pathReportFileName,ParamsBirtApplication.getBirtRuntimePath(),pathReportDocument, ValoresPorDefecto.getFilePath()+this.pathFileCvs);
    	
    }
    
    /**
     * actualiza los parametros de conexion en el archivo temporal
     * @param newPathReport
     * @return
     */
    public boolean updateJDBCParameters(String newPathReport)
    {
        UpdateJDBCParameters u= new UpdateJDBCParameters();
        if(u.loadReportDesign(newPathReport))
        {   
            if(!u.writeFragmentDataSource(newPathReport))
            {    
                System.err.println("error en el writeFragmentDataSource");
                return false;
            }    
        }
        else
        {    
            System.err.println("error en el loadReportDesign");
            return false;
        }    
        return true;
    }
    
    
    /**
     * Metodo para insertar el nombre de la persona que imprimio en el Pie de Pagina. 
     * @param nombre -> Nombre del Usuario que imprimio.
     */
    public void insertarUsuarioImprimio(String nombreUsuario)
    {
    	
    	Iterator iter=obtenerMasterPageFooter();
        GridHandle gridHandleAux;
        RowHandle rowHandleAux;

        while (iter.hasNext())
    	{
    		gridHandleAux = (GridHandle) iter.next();
    		if ( gridHandleAux.getName().equals("masterPageFooter") ) 
    		{ 
		    		//----Iterarar Las Filas De La Grilla.	
		    		SlotHandle sloth2=gridHandleAux.getRows();
		            Iterator iter3=sloth2.iterator();
		            int p = 0;
		            while ( iter3.hasNext() )
		            {
		            	rowHandleAux = (RowHandle)iter3.next();
		            	
		            	if (p >= 2)
		            	{
				            SlotHandle sloth3=rowHandleAux.getCells();
		                    java.util.Iterator iter4=sloth3.iterator();
		                    //Se iteran las celdas de la fila
		                    while (iter4.hasNext())
		                    {
		                 	   //se obtiene una celda
		                        cellHandle=(CellHandle)iter4.next();
		                        SlotHandle sloth4 = cellHandle.getContent();
		                        java.util.Iterator iter5 = sloth4.iterator();
		                        
		                        if ( iter5.hasNext() )
		                        {
		                        	gridHandleAux = (GridHandle) iter5.next();
			                        //-Iterar Sobre Las Filas de la Grilla Interna del Detalle.
			                    	SlotHandle sloth5=gridHandleAux.getRows();
			    		            Iterator iter6=sloth5.iterator();
			    		            if ( iter6.hasNext() )
			    		            {
			    		            	rowHandleAux = (RowHandle)iter6.next();
			    		
			    		            	//SlotHandle sloth6=rowHandleAux.getCells();
					                    java.util.Iterator iter7=sloth3.iterator();
					                    //Se iteran las celdas de la fila 
					                    if (iter7.hasNext())
					                    {
					                    	cellHandle = (CellHandle) iter7.next();
					                    	labelHandle = elementFactory.newLabel("label1");
					                    	try {
					                    			labelHandle.setText("Impreso Por : " + nombreUsuario);
					                    			cellHandle.getContent().dropAndClear(0);
					                    			cellHandle.getContent().add(labelHandle);
											} catch (SemanticException e) { e.printStackTrace(); } 
					                    }	            	
			    		            }			    		            	
		                        }
		                    }
		            	}
		            	p++;
		            }
    		}   //----If del masterPageFooter      
    	}  //-While del Grid 

    }
    

    /**
     * Metodo para insertar las filas de un Mapa en un Reporte. 
     * @param columnas
     * @param mapa
     * @param numeroFilaEmpezar = numero de la Fila de se quiere que se comience a insertar los datos del mapa (esta opcion se hizo para dejar subtitulos en la plantilla *.rptdesign)
     */
	public void insertarMapaEnReporte(String[] columnas, HashMap mapa, int numeroFilaEmpezar) 
	{
    	//Se obtiene el body
        Iterator iter=obtenerBodyPage();
        
        if(iter.hasNext())
        {
        	//Se obtiene la cuadrícula del body
            
        	int rows = 0;
        	if ( UtilidadCadena.noEsVacio(mapa.get("numRegistros")+"") ) { rows = Integer.parseInt(mapa.get("numRegistros")+"") - 1; } //--El -1 es para que inserte una fila menos(hay que contar la que esta en la plantilla).
        	
            Iterator iter1 = obtenerGridPpal(columnas.length, rows);

            
            int fil=0;
            while (iter1.hasNext())
            {                
            	rowHandle=(RowHandle)iter1.next();

            	if (numeroFilaEmpezar > 0) { numeroFilaEmpezar--; continue; }
            	
            	SlotHandle sloth1=rowHandle.getCells();
                java.util.Iterator iter2=sloth1.iterator();
                
                int col=0;
                
                while (iter2.hasNext())
                {
                	//--Se Obtiene Una Celda.
                    cellHandle=(CellHandle)iter2.next();
                	
                    //--Se Crea Un Label Para Insertar A La Celda.
                    labelHandle = elementFactory.newLabel("label1");
                    
                    try { 
							labelHandle.setText(mapa.get(columnas[col]+fil) + "");
							
                    		cellHandle.getContent().add(labelHandle);
                    		cellHandle.setProperty (Style.BORDER_BOTTOM_STYLE_PROP, "solid"); cellHandle.setProperty (Style.BORDER_BOTTOM_WIDTH_PROP, "thin");
                    		cellHandle.setProperty (Style.BORDER_LEFT_STYLE_PROP, "solid");   cellHandle.setProperty (Style.BORDER_LEFT_WIDTH_PROP, "thin");
                    		
                    		if (  (columnas.length-1) == col ) 
                    		{
                        		cellHandle.setProperty (Style.BORDER_RIGHT_STYLE_PROP, "solid");   cellHandle.setProperty (Style.BORDER_RIGHT_WIDTH_PROP, "thin");
                    		}
                    		
						} 
                    catch (ContentException e)  {  e.printStackTrace();  }
					catch (NameException e)     {  e.printStackTrace();  }
					catch (SemanticException e) {  e.printStackTrace();  }				

					if ( col ==  (columnas.length-1) ) { col = 0; fil++; }
					col++;
                }
            }
        
        }
        
        
    }

	 /**
     * Metodo para obtener el gris principal del 
     * body, el grid ya esta definido por defecto
     * @return iteraror
     * @author cperalta
     */
    private Iterator obtenerGridPpal(int numeroColumnas, int nroRegistros)
    {
        Iterator iter=obtenerBodyPage();
        while (iter.hasNext())
        {
            gridHandle = (GridHandle) iter.next();
            
            
	       try {
	        	for (int j = 0; j < nroRegistros; j++) 
	        	{
	        		RowHandle rowHandleAux = elementFactory.newTableRow(numeroColumnas);
	        		gridHandle.getRows().add(rowHandleAux);
	        	}
			} 
	    	catch (ContentException e1) { e1.printStackTrace(); } 
			catch (NameException e1) 	{ e1.printStackTrace(); } 

			SlotHandle sloth=gridHandle.getRows();
            Iterator iter1=sloth.iterator();
            if (iter1.hasNext())
            {            
            	return iter1;
            }	
        }
        return null;
    }
	
    /**
     * Método que retorna el CellHandle de una celda x,y de la rejilla principal del BodyPage
     * @param posRow
     * @param posCell
     * @return
     */
    public CellHandle obtenerCeldaDeRejillaBodyPage(int posRow, int posCell)
    {
    	RowHandle fila;
    	CellHandle celda;
    	GridHandle rejilla;
    	
        // Se obtiene el manejador de la rejilla
    	SlotHandle sloth = reportDesignHandle.getBody();
        java.util.Iterator iter=sloth.iterator();        
        while (iter.hasNext())
        {
            rejilla = (GridHandle) iter.next();
            fila=(RowHandle)rejilla.getRows().get(posRow);
            celda=(CellHandle)fila.getCells().get(posCell);
            return celda;
        }
        return null;
    }
    
    /**
     * Método que busca un elemento de diseño en el reporte y lo retorna, de los contrario retorna null
     * @param nombreElemento
     * @return
     */
    public DesignElementHandle buscarElemento(String nombreElemento)
    {
    	return reportDesignHandle.findElement(nombreElemento);
    }
	/**
	 * @return the pathFileCvs
	 */
	public String getPathFileCvs() {
		return pathFileCvs;
	}
	/**
	 * @param pathFileCvs the pathFileCvs to set
	 */
	public void setPathFileCvs(String pathFileCvs) {
		this.pathFileCvs = pathFileCvs;
	}
	/**
	 * @return the reportDesignHandle
	 */
	public ReportDesignHandle getReportDesignHandle() {
		return reportDesignHandle;
	}
	
	/**
	 * Wilson Rios
	 */
	public void lowerAliasDataSet() 
	{
		String query;         
        SlotHandle sloth = reportDesignHandle.getDataSources();
        java.util.Iterator iter = sloth.iterator();            
        // buscar el data set especifico
        sloth = reportDesignHandle.getDataSets();
        iter = sloth.iterator();
        while (iter.hasNext())
        {
            odaDataSetHandle = (OdaDataSetHandle) iter.next();
            query = odaDataSetHandle.getQueryText();
            //logger.info("CONSULTA*****************--->"+query);
            
            query= matchAliasToLower(query);
            
            try 
            {
                odaDataSetHandle.setQueryText(query);
            } 
            catch (SemanticException e)
            {        
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * Wilson Rios
	 * @param query
	 * @return
	 */
	private String matchAliasToLower(String query) 
	{
		//quitamos primero los enter
		query=query.replaceAll("\n", " ");
		query=query.replaceAll(" FROM ", " from ");
		query=query.replaceAll("\tFROM ", " from ");
		query=query.replaceAll("\tFROM\t", " from ");
		query=query.replaceAll(" FROM\t", " from ");

		query=query.replaceAll(" AS ", " as ");
		query=query.replaceAll("\tAS ", " as ");
		query=query.replaceAll("\tAS\t", " as ");
		query=query.replaceAll(" AS\t", " as ");

		
		query=query.replaceAll(" As ", " as ");
		query=query.replaceAll("\tAs ", " as ");
		query=query.replaceAll("\tAs\t", " as ");
		query=query.replaceAll(" As\t", " as ");
		
		
		query=query.replaceAll(" aS ", " as ");
		query=query.replaceAll("\taS ", " as ");
		query=query.replaceAll("\taS\t", " as ");
		query=query.replaceAll(" aS\t", " as ");
		
		//verificamos la expresion " as XXXXXXXXX, "
		Pattern p = Pattern.compile("\\sas([\\s])+([\\w])+([\\s])*,");   // expresion
		Matcher m = p.matcher(query);  
		while(m.find()) 
		{
			query= query.replaceAll(m.group(), " as \""+deleteAsAndComma(m.group().toLowerCase()).trim()+"\",");
		}
		
		//verificamos la expresion " as XXXXXXX from "
		Pattern p1 = Pattern.compile("\\sas([\\s])+([\\w])+([\\s])*from");   // expresion
		Matcher m1 = p1.matcher(query);  
		while(m1.find()) 
		{
			query= query.replaceAll(m1.group(), " as \""+deleteAsAndFrom(m1.group().toLowerCase()).trim()+"\" from ");
		}
		
		//logger.info("\n\n\nquery lower alias-->"+query+"\n\n\n");
		return query;
	}
	
	/**
	 * 
	 * @param lowerCase
	 * @return
	 */
	private String deleteAsAndFrom(String query) 
	{
		query= query.replaceAll(" as ", " ");
		query= query.replaceAll(" from", " ");
		return query;
	}
	/**
	 * 
	 * @param query
	 * @return
	 */
	private String deleteAsAndComma(String query) 
	{
		query= query.replaceAll(" as ", " ");
		query= query.replaceAll(",", " ");
		return query;
	}
	
}