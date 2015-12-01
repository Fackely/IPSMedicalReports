package com.princetonsa.mundo.odontologia;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.axioma.util.log.Log4JManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoPlanTratamiento;

public class UtilidadOdontogramaXML
{
	/**
	 * Construye el objeto de InfoPlanTratamiento a partir de un xml
	 * */
	public static InfoPlanTratamiento getInfoPlanTratamientoDeXML(String xml)
	{
		InfoPlanTratamiento info = new InfoPlanTratamiento();
		
//		Log4JManager.info(xml);
		
		if(!xml.equals(""))
		{
	        try 
	        {
	            // 2. Usar DocumentBuilderFactory para crear un DocumentBuilder
	        	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setNamespaceAware(true);
	            DocumentBuilder builder = builderFactory.newDocumentBuilder();
	        	
	            // 3. Parsear a partir de un Strig
	            StringBuffer StringBuffer1 = new StringBuffer(xml);
	            ByteArrayInputStream bs = new ByteArrayInputStream(StringBuffer1.toString().getBytes());
				Document docR = builder.parse (bs);
				
	            //Obtener el documento raíz
	        	//Se crea un objeto Element, a partir del documento XML
	            Element docEle = docR.getDocumentElement();
	            
	            //Obtener un nodelist de elementos <contenido>
	            NodeList nl = docEle.getElementsByTagName("diente");
	            
	            if(nl != null && nl.getLength() > 0)
	            {
	                for(int i = 0; i < nl.getLength(); i++)
	                {
	                	//Toma las piezas dentales
	                    Element piezaElemento = (Element) nl.item(i);
	                    
	                    InfoDetallePlanTramiento infoDiente = new InfoDetallePlanTramiento();
	                    infoDiente.setPieza(new InfoDatosInt(Utilidades.convertirAEntero(piezaElemento.getAttribute("pieza"))));
	                    NodeList superficieNodo = piezaElemento.getElementsByTagName("superficie");
	                    
	                    for(int j=0; j<superficieNodo.getLength(); j++)
		                {
	                    	Element superficieElemento = (Element) superficieNodo.item(j);
	                    	
	                    	InfoHallazgoSuperficie infoHallazgo = new InfoHallazgoSuperficie();
	                    	Element hallazgoElemento = (Element)superficieElemento.getElementsByTagName("hallazgo").item(0);
	                    	InfoDatosInt tmp = new InfoDatosInt();
	                    	
	                    	/*
	                    	 * En este caso se va a buscar el diente
	                    	 */
	                    	if(superficieElemento.getAttribute("nombre").toString().equals("diente") )
	                    	{
	                    		if(UtilidadTexto.getBoolean(superficieElemento.getAttribute("modificable")))
	                    		{
		                    		tmp.setCodigo(Utilidades.convertirAEntero(hallazgoElemento.getElementsByTagName("codigo").item(0).getFirstChild().getNodeValue()));
		                    		tmp.setNombre(hallazgoElemento.getElementsByTagName("descripcion").item(0).getFirstChild().getNodeValue());
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("path").item(0).getTextContent().equals(""))
		                    			tmp.setDescripcion(hallazgoElemento.getElementsByTagName("path").item(0).getFirstChild().getNodeValue()+"");
		                    		else
		                    			tmp.setDescripcion("");
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("convencion").item(0).getTextContent().equals(""))
		                    			infoHallazgo.setCodigoConvencion(hallazgoElemento.getElementsByTagName("convencion").item(0).getFirstChild().getNodeValue()+"");
		                    		else
		                    			infoHallazgo.setCodigoConvencion("");
		                    		
		                    		infoHallazgo.setHallazgoREQUERIDO(tmp);
		                    		
		                    		infoHallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
									infoHallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());

		                    		infoHallazgo.getSuperficieOPCIONAL().setCodigo2(Utilidades.convertirAEntero(superficieElemento.getAttribute("sector").toString()));
		                    		/*
		                    		 * Activo este atributo para verificar que se ingrese al plan de tratamiento
		                    		 */
		                    		infoHallazgo.getExisteBD().setActivo(true);
		                    		infoDiente.getDetalleSuperficie().add(infoHallazgo);
	                    		}
	                    	}
	                    	else
	                    	{
	                    		if(UtilidadTexto.getBoolean(superficieElemento.getAttribute("modificable")))
	                    		{
		                    		tmp.setCodigo(Utilidades.convertirAEntero(hallazgoElemento.getElementsByTagName("codigo").item(0).getFirstChild().getNodeValue()));
		                    		tmp.setNombre(hallazgoElemento.getElementsByTagName("descripcion").item(0).getFirstChild().getNodeValue());
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("path").item(0).getTextContent().equals(""))
		                    		{
		                    			tmp.setDescripcion(hallazgoElemento.getElementsByTagName("path").item(0).getFirstChild().getNodeValue()+"");
		                    		}
		                    		else
		                    		{
		                    			tmp.setDescripcion("");
		                    		}
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("convencion").item(0).getTextContent().equals(""))
		                    		{
		                    			infoHallazgo.setCodigoConvencion(hallazgoElemento.getElementsByTagName("convencion").item(0).getFirstChild().getNodeValue()+"");
		                    		}
		                    		else
		                    		{
		                    			infoHallazgo.setCodigoConvencion("");
		                    		}
		                    		
		                    		infoHallazgo.setHallazgoREQUERIDO(tmp);
		                    		
		                    		tmp = new InfoDatosInt();
		                    		tmp.setCodigo(Utilidades.convertirAEntero(superficieElemento.getAttribute("codigo").toString()));
		                    		tmp.setNombre(superficieElemento.getAttribute("nombre").toString());
		                    		infoHallazgo.setSuperficieOPCIONAL(tmp);
		                    		
		                    		infoHallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
									infoHallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());

		                    		infoHallazgo.getSuperficieOPCIONAL().setCodigo2(Utilidades.convertirAEntero(superficieElemento.getAttribute("sector").toString()));
		                    		/*
		                    		 * Activo este atributo para verificar que se ingrese al plan de tratamiento
		                    		 */
		                    		infoHallazgo.getExisteBD().setActivo(true);
		                    		infoDiente.getDetalleSuperficie().add(infoHallazgo);
                   				}
	                    	}
		                }
	                    
	                    info.getSeccionHallazgosDetalle().add(infoDiente);
	                }
	            }
	        }
	        catch (ParserConfigurationException pce) {  //Capturamos los errores, si los hubiera
	            pce.printStackTrace();
	        } catch (SAXException se) {
	            se.printStackTrace();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
		}
		
		return info;
	}
}
