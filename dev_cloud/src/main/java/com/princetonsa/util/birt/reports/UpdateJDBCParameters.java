/*
 * @(#)UpdateJDBCParameters.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.util.birt.reports;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tools.codec.Base64Encoder;
import org.xml.sax.SAXException;

/**
 * Clase encargada de modificar los atributos de conexion con la fuente de
 * datos del rptdesign
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 *
 */
public class UpdateJDBCParameters 
{
	/**
	 * para manejor de los erroress.
	 */
	Logger logger =Logger.getLogger(UpdateJDBCParameters.class);
	
	/**
     * file que contiene el rptDesign
     */
    private File rptDesignFile;
    
    /**
	 * rpt design parseado a Document w3c
	 */
	private Document documentRptDesignParseado;
    
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public UpdateJDBCParameters()
	{
		this.reset();
	}
	
	/**
	 * resetea los atributos 
	 * del objeto
	 */
	public void reset()
	{
		this.documentRptDesignParseado=null;
		this.rptDesignFile=null;
	}
	
	/**
	 * Metodo que carga y parsea el  a un objeto de tipo
	 * Document (W3c) validando que cumpla con el DTD
	 * @param pathWebXmlTemp
	 * @return
	 */
	public boolean loadReportDesign(String pathReportDesign)
	{
		logger.info("************************loadReportDesign***************************************");
		logger.info("Path ReportDesigner-->"+pathReportDesign);
		rptDesignFile = new File(pathReportDesign);
		DocumentBuilderFactory myDocumentFactory= DocumentBuilderFactory.newInstance();
		myDocumentFactory.setValidating(true);
		try 
		{
			DocumentBuilder documentBuilder= myDocumentFactory.newDocumentBuilder();
			this.documentRptDesignParseado=documentBuilder.parse(rptDesignFile);
		} 
		catch (ParserConfigurationException e) 
		{
			logger.error("Error ParserConfigurationException: "+e.toString());
			return false;
		} 
		catch (SAXException e) 
		{
			logger.error("Error SAXException: "+e.toString());
			return false;
		} 
		catch (IOException e) 
		{
			logger.error("Error IOException: "+e.toString());
			return false;
		
		}
		logger.info("Cargo!!!!!!!!!!!!!!!![sin problemas]");
		return true;
	}
    
	/**
	 * borra el segmento de 
	 *
	 */
	public void deleteFragmentDataSource()
	{
		Element reportElement= (Element) this.documentRptDesignParseado.getLastChild();
		NodeList nodeListDataSorce= reportElement.getElementsByTagName("data-sources");
		int num=nodeListDataSorce.getLength();
		logger.info("Numero elementos del data source-->"+num);
		
		for(int w=0; w<num; w++)
		{
			Node nodeDataSourceToDelete=nodeListDataSorce.item(0);
			logger.info("nodeDataSourceToDelete-->"+nodeDataSourceToDelete);
			if(nodeDataSourceToDelete!=null)
			{
				logger.info("entro a borrar");
				reportElement.removeChild(nodeDataSourceToDelete);
			}
		}
	}
    
	/**
	 * Crea el segmento <data-source>
	 * @return
	 */
	public DocumentFragment createFragmentDataSource()
	{
		Text txt;
		String idStatic="11111111";
		DocumentFragment dataSourceFragmentXml= this.documentRptDesignParseado.createDocumentFragment();
		Text enter= this.documentRptDesignParseado.createTextNode("\n");
		Element dataSourceElement = this.documentRptDesignParseado.createElement("data-sources");
        dataSourceElement.appendChild(enter);
		Element odaDataSourceElement = this.documentRptDesignParseado.createElement("oda-data-source");
		
		odaDataSourceElement.setAttribute("extensionID", "org.eclipse.birt.report.data.oda.jdbc");
		odaDataSourceElement.setAttribute("name", "axioma");
		odaDataSourceElement.setAttribute("id", idStatic);
		
		logger.info("creo el oda-data-source");
		
		//driver
		Element driverElement=this.documentRptDesignParseado.createElement("property");
		driverElement.setAttribute("name", "odaDriverClass");
		txt = this.documentRptDesignParseado.createTextNode(ParamsBirtApplication.getDriver());
		driverElement.appendChild(txt);
		logger.info("creo el driver = ["+ParamsBirtApplication.getDriver()+"]");
		
		//url
		Element urlElement=this.documentRptDesignParseado.createElement("property");
		urlElement.setAttribute("name", "odaURL");
		txt = this.documentRptDesignParseado.createTextNode(ParamsBirtApplication.getUrlBD());
		urlElement.appendChild(txt);
		logger.info("creo el url = ["+ParamsBirtApplication.getUrlBD()+"]");
		
		//user
		Element userElement=this.documentRptDesignParseado.createElement("property");
		userElement.setAttribute("name", "odaUser");
		txt = this.documentRptDesignParseado.createTextNode(ParamsBirtApplication.getUserBD());
		userElement.appendChild(txt);
		logger.info("creo el user = ["+ParamsBirtApplication.getUserBD()+"]");
		
		//password
		//@todo hacer la implementacion del password encrypted
		Element passwordEncryptedElement=this.documentRptDesignParseado.createElement("encrypted-property");
		passwordEncryptedElement.setAttribute("name", "odaPassword");
		Base64Encoder encoder=new Base64Encoder(ParamsBirtApplication.getPassword());
		String passwordEncoded=encoder.processString();
		txt = this.documentRptDesignParseado.createTextNode(passwordEncoded);
		passwordEncryptedElement.appendChild(txt);
		logger.info("creo el password");
		
		odaDataSourceElement.appendChild(driverElement);
		odaDataSourceElement.appendChild(urlElement);
		odaDataSourceElement.appendChild(userElement);
		odaDataSourceElement.appendChild(passwordEncryptedElement);
		dataSourceElement.appendChild(odaDataSourceElement);
		dataSourceFragmentXml.appendChild(dataSourceElement);
		
		logger.info("fin creacion del fragmento");
		
		return dataSourceFragmentXml;
	}


	/**
	 * Crea el segmento del data source
	 * @return
	 */
	public boolean writeFragmentDataSource(String pathAndNameReportTemp)
	{
		logger.info("pathAndNameReportTemp-->"+pathAndNameReportTemp);
		Transformer transformer=null;
		TransformerFactory myTransformerFactory= TransformerFactory.newInstance();
		try
		{
			Element reportElement= (Element) this.documentRptDesignParseado.getLastChild();
			logger.info("1 tomo el reportElement="+reportElement);
			this.deleteFragmentDataSource();
			logger.info("2 borro el fragmento");
			DocumentFragment dataSourceFragmentXml=this.createFragmentDataSource();
			logger.info("3 creo el fragmento");
			dataSourceFragmentXml.normalize();
			logger.info("4 quedo normalizado");
			NodeList nodeDataSets= reportElement.getElementsByTagName("data-sets");
			logger.info("5 tomo el dataset->"+nodeDataSets);
			reportElement.insertBefore(dataSourceFragmentXml, nodeDataSets.item(0));
			logger.info("6 inserto el fragmento en el documento");
		}
		catch (Exception e) 
		{
			logger.error("Error createFragmentDataSource: "+e.toString());
			return false;
		}
		try 
		{
			transformer= myTransformerFactory.newTransformer();
		} 
		catch (TransformerConfigurationException e) 
		{
			logger.error("Error TransformerConfigurationException= "+e.toString());
			return false;
		}
		synchronized(rptDesignFile)
		{
			DOMSource source=null;
			StreamResult streamResult=null;
			try
			{
				source= new DOMSource(this.documentRptDesignParseado);
				File temporalFile= new File(pathAndNameReportTemp);
				streamResult= new StreamResult(temporalFile);
			}
			catch (Exception e) 
			{
				logger.error("Error createFragmentDataSource 1: "+e.toString());
				return false;
			}
			try 
			{
				transformer.transform(source, streamResult);
			} 
			catch (TransformerException e) 
			{
				logger.error("Error TransformerException= "+e.toString());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return Returns the documentRptDesignParseado.
	 */
	public Document getDocumentRptDesignParseado() {
		return documentRptDesignParseado;
	}

	/**
	 * @return Returns the rptDesignFile.
	 */
	public File getRptDesignFile() {
		return rptDesignFile;
	}

}