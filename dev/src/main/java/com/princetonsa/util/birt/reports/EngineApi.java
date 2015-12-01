
/*
 * Creado   27-feb-2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.util.birt.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.dataextraction.CSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption;


/**
 * Clase que implementa los constructores y metodos
 * para cofigurar, obtener y correr un determinado 
 * reporte segun el formato especificado, el cual sera
 * ubicado en la ruta determinada. 
 *
 * @version 1.0, 27-feb-2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class EngineApi 
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(EngineApi.class);
	
    /**
     * objeto de tipo IRunAndRenderTask para manejar el reporte
     */
    private IRunAndRenderTask task;
    /**
     * objeto de ReportEngine engine, para configurar el reporte
     */
    private ReportEngine engine;
    /**
     * constante para el formato HTML
     */
    private final String OUTPUT_FORMAT_HTML="html";
    /**
     * constante para el formato HTML
     */
    private final String OUTPUT_FORMAT_PDF="pdf";
    
    private String reportDesign;
    
    /**
     * constructor encargado de realizar los llamados
     * a los metodos que configuran el reporte.
     * @param reportDesign String, ruta del archivo *.rptdesign (donde se encuentra el diseño)
     * @param engineHome String, ruta donde se encuentra la configuración del Engine, plugins, *.jar , etc
     * @author jarloc
     */
    public EngineApi (String reportDesign, String engineHome)
    {
    	this.reportDesign = reportDesign;
        this.engine=this.getEngine(engineHome);
        IReportRunnable report=this.getReport(this.engine, reportDesign);
        this.getRunAndExecuteReport(this.engine, report);
    }
    
    /**
     * Metodo para obtener el manejador del motor de
     * reportes
     * @param engineHome String, ruta del motor (birtRuntime)
     * @return engine ReportEngine
     * @author jarloc
     */
    private ReportEngine getEngine (String engineHome)
    {
        EngineConfig config=new EngineConfig();
        config.setEngineHome( engineHome );
        ReportEngine engine = new ReportEngine( config );
        return engine;
    } 
    /**
     * Metodo para obtener el diseño del Reporte, basado en un diseño
     * ya realizado
     * @param engine ReportEngine
     * @param reportDesign String, ruta del diseño
     * @return  design IReportRunnable
     * @author jarloc
     */
    private IReportRunnable getReport (ReportEngine engine,String reportDesign)
    {
        IReportRunnable design = null;            
        try 
        {
            design = engine.openReportDesign( reportDesign );            
        } 
        catch (EngineException e)
        {            
            e.printStackTrace();
            System.err.println("REPORTE " + reportDesign + " NO ENCONTRADO!\n");
            engine.destroy();
            return null;
        }  
        return design;
    }   
    /**
     * metodo para crear la tarea que ejecutara y correra
     * el reporte segun las caracteristicas que se le deseen
     * dar a este.
     * @param engine ReportEngine 
     * @param report IReportRunnable
     * @return IRunAndRenderTask
     * @author jarloc
     */
    private IRunAndRenderTask getRunAndExecuteReport(ReportEngine engine,IReportRunnable report)
    {
        this.task = engine.createRunAndRenderTask(report);
        return this.task;
    }
    /**
     * Metodo para exportar el reporte a HTML,
     * y sera ubicado en la ruta especificada, con
     * el nombre tambien especificado.
     * @param filePath String
     * @param fileName String
     * @author jarloc
     */
    private void exportHtml(String filePath,String fileName)
    {
    	
        HTMLRenderOption output = new HTMLRenderOption();            
        output.setOutputFileName(filePath+fileName+"."+this.OUTPUT_FORMAT_HTML);
        output.setOutputFormat( this.OUTPUT_FORMAT_HTML );
        this.task.setRenderOption(output);
        try 
        {
            this.task.run();
            this.engine.destroy();
        } 
        catch (EngineException e) 
        {          
            e.printStackTrace();
        }       
    }  
    /**
     * Método implementado para exportar el reporte a PDF
     * @param engineHome
     * @param reportDesign
     * @param reportDoc
     * @param textFile
     */
    public void exportCSV(String reportDoc,String textFile)
    {
    	
    	try
    	{
    	
	    	IReportRunnable design = this.getReport(engine, reportDesign);
	
	    	
	    	// Create task to run the report - use the task to run the report
	    	IRunTask task = engine.createRunTask(design);
	    	
	    	
	    	// Run the report and create the rptdocument
	    	task.run(reportDoc);
	
	
	    	// Open the rptdocument
	    	IReportDocument rptdoc = engine.openReportDocument(reportDoc);
	
	    	
	    	// Create the data extraction task
	    	IDataExtractionTask iDataExtract = engine.createDataExtractionTask(rptdoc);
	
	
	
	    	ICSVDataExtractionOption extractionOptions =
	    	    new CSVDataExtractionOption();
	    	logger.warn("EL ARCHIVO DE SALIDA ES "+textFile);
	    	extractionOptions.setOutputFile(textFile);
	
	
	    	// Not sure why I need set setOutputFormat,
	    	// but BIRT barfs if the line is missing.
	    	extractionOptions.setOutputFormat("csv");
	
	
	    	extractionOptions.setSeparator(",");
	
	    	// The following line work is interesting.
	    	// extract() throws a BirtException, but does not return
	    	// a result or status. It would be nice to returned IExtractionResults
	    	//iDataExtract.extract(extractionOptions);
			
	    	///Get list of result sets		
	    	ArrayList resultSetList = (ArrayList)iDataExtract.getResultSetList( );

	    	//Choose first result set
	    	IResultSetItem resultItem = (IResultSetItem)resultSetList.get( 0 );
	    	String dispName = resultItem.getResultSetName( );
	    	iDataExtract.selectResultSet( dispName );

	
	    	IExtractionResults iExtractResults = iDataExtract.extract();
	    	IDataIterator iData = null;
	    	try
	    	{
	    		logger.info("RESULTADOS EXTRAIDOS=> "+iExtractResults);
	    		if ( iExtractResults != null )
	    		{
	    			iData = iExtractResults.nextResultIterator( );
	    			logger.info("ESTADO DE LOS DATOS=> "+iData);
	    			//iterate through the results
	    			if ( iData != null  ){
	    				while ( iData.next( ) )
	    				{	
	    					Object objColumn1;
	    				    Object objColumn2;
	    					try{
	    						objColumn1 = iData.getValue(0);
	    					}catch(DataException e){
	    						objColumn1 = new String("");
	    					}
	    					try{
	    						objColumn2 = iData.getValue(1);
	    					}catch(DataException e){
	    						objColumn2 = new String("");
	    					}
	    						logger.info("VALOR DE LOS DATOS ENCONTRADOS=> "+ objColumn1 + " , " + objColumn2 );
	    				}
	    				iData.close();
	    			}
	    		}
	    	}
	    	catch( Exception e)
	    	{
	    		logger.error("ESTOY EXTRAYENDO LOS DATOS DEL REPORTE!!!!!!!!!!");
	    	}


	    	// The following line returns IExtractionResults results
	    	/* However, it kind of misses the point of just out to CSV.
	    	if( results!=null ) {
	    	    logger.info(results.getResultMetaData().toString());
	    	}**/
	    	iDataExtract.close();
    	}
    	catch(EngineException e)
    	{
    		logger.error("Error tratando de exportar el reporte a PDF :"+e);
    	} catch (BirtException e) 
    	{
    		logger.error("Error tratando de exportar el reporte a PDF :"+e);
		}
    }
    
	public static void exportCSVStatic(String pathReportFileName,String engineHome,String reportDoc,String textFile)
    {
    	//**************SE CARGA EL ENGINE CONFIG Y SE INSTANCIA EL REPORTE ENGINE (RE)*************************
    	EngineConfig config = new EngineConfig( );
    	config.setEngineHome( engineHome );
    	
    	try 
    	{
			Platform.startup( config );
		} catch (BirtException e) 
		{
			logger.error("Error iniciando la plataforma del engine config "+e);
		}  //If using RE API in Eclipse/RCP application this is not needed.
    	IReportEngineFactory factory = (IReportEngineFactory) Platform
    			.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
    	IReportEngine engine = factory.createReportEngine( config );
    	engine.changeLogLevel( Level.ALL );
    	//****************************************************************************************
    	
    	
    	//****************SE ABRE AL REPORTDESIGN*********************************************
    	IReportRunnable report = null;
    	try 
    	{
			report = engine.openReportDesign( pathReportFileName );
		} 
    	catch (EngineException e) 
    	{
			logger.error("ERROR abriendo el report design: "+e);
		}
    	// Create task to run the report - use the task to run the report
    	IRunTask task = engine.createRunTask(report);
    	
    	
    	// Run the report and create the rptdocument
    	try 
    	{
			task.run(reportDoc);
		} 
    	catch (EngineException e2) 
    	{
			logger.error("Error creando el rptdocument: "+e2);
		}
    	//*****************************************************************************************
    	//***********SE ABRE EL REPORTDOCUMENT**************************************************
    	IReportDocument ird = null;
		try 
		{
			ird = engine.openReportDocument(reportDoc);
		} catch (EngineException e) 
		{
			logger.error("Error abriendo el rptdocument: "+e);
		}
    	//get root node
    	TOCNode td = ird.findTOC(null);
    	List children = td.getChildren( );
    	//Loop through Top Level Children
    	if ( children != null && children.size( ) > 0 )
    	{
    		for ( int i = 0; i < children.size( ); i++ )
    		{
    			TOCNode child = ( TOCNode ) children.get( i );
    			logger.info( "Node ID " + child.getNodeID());
    			logger.info( "Node Display String " + child.getDisplayString());
    			logger.info( "Node Bookmark " + child.getBookmark());
    				
    		}
    	}
    	//*****************************************************************************************
    	//*****************SE GENERA LA EXTRACCIÓN**************************************************

    	//Create Data Extraction Task		
    	IDataExtractionTask iDataExtract = engine.createDataExtractionTask(ird);
    	ICSVDataExtractionOption extractionOptions = new CSVDataExtractionOption();
    	logger.warn("EL ARCHIVO DE SALIDA ES "+textFile);
    	extractionOptions.setOutputFile(textFile);
    	// Not sure why I need set setOutputFormat,
    	// but BIRT barfs if the line is missing.
    	extractionOptions.setOutputFormat("csv");
    	extractionOptions.setSeparator(",");
    	
    	/// The following line work is interesting.
	    // extract() throws a BirtException, but does not return
	    // a result or status. It would be nice to returned IExtractionResults
	    try 
	    {
			iDataExtract.extract(extractionOptions);
		} 
	    catch (BirtException e) 
	    {
			logger.error("Error extrayendo el archivo CSV: "+e);
		}
    	
    	iDataExtract.close();

    	//******************************************************************************************
    	ird.close();
    	engine.destroy();
    	Platform.shutdown();    
    	
    }
}

