package com.princetonsa.mundo.odontologia;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetSuperficieIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetalleIndicePlaca;
import com.princetonsa.sort.odontologia.SortDetalleIndicePlaca;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.Utilidades;
import util.odontologia.UtilidadOdontologia;

/**
 * @author Víctor Hugo Gómez L.
 */

public class ComponenteIndicePlaca 
{
	private static Logger logger = Logger.getLogger(ComponenteIndicePlaca.class);
	
	/**
	 * metodo que administra los estados del componente indice de placa
	 * @param dto
	 * @param estado
	 */
	public static void accionesEstados(DtoComponenteIndicePlaca dto, String estado)
	{
		if(estado.equals("empezar") || estado.equals("actualizar"))
		{
			//*********************************************************************************
			// 1. se carga el xml de rangos
			cargarXMLRangos(dto);
			//*********************************************************************************
			
			//*********************************************************************************
			// 2. validar diente adulto - nino segun la edad del paciente
			activoDientesAdultoNino(dto);
			//*********************************************************************************
			
			//*********************************************************************************
			// 3.cargar el xml de indice de placa
			if(dto.getXmlIndicePlaca().equals(""))
			{
				// 3.1 se carga en el dto los datos del componente y detalle del indice de placa
				cargarComponenteIndicePlaca(dto);
				// 3.2 se construye el xml indice de placa
				getXmlIndicePlaca(dto);
			}
			//logger.info("XmlIndicePlaca(): "+dto.getXmlIndicePlaca());
			//*********************************************************************************
		}
		else if(estado.equals("imprimir"))
		{
			cargarComponenteIndicePlaca(dto);
		}
		dto.setForward("indicePlaca");
	}
	
	
	/**
	 * metodo que obtiene en una estructura xml los rangos
	 * @param dto
	 */
	private static void cargarXMLRangos(DtoComponenteIndicePlaca dto)
	{
		dto.setXmlRangos(UtilidadOdontologia.cargarInterpretacionIndicePlaca());
	}
	
	/**
	 * metod que valida los dientes (Adulto - Nino) segun la edad del paciente
	 * @param dto
	 */
	private static void activoDientesAdultoNino(DtoComponenteIndicePlaca dto) 
	{
		ComponenteOdontograma compOdo = new ComponenteOdontograma();
		InfoDatosString infoDato = new InfoDatosString(); 
		infoDato = compOdo.iniciarValidacionEdades(dto.getCodigoInstitucion(), dto.getEdadPaciente());
		// activo diente adulto
		dto.setActivoDienteAdulto(infoDato.getId());
		// activo diente nino
		dto.setActivoDienteNino(infoDato.getDescripcion());
	}
	
	/**
	 * metod que apartir del DtoComponenteIndicePlaca crea una cadena (String) 
	 * con las propiedades de un XML 
	 * @param dto
	 * @return String
	 */
	private static void getXmlIndicePlaca(DtoComponenteIndicePlaca dto)
	{
		int codigoPieza = ConstantesBD.codigoNuncaValido;
		StringBuffer xmlIndicePlaca = new StringBuffer();  
		
		if(dto.getDetIndicePlaca()!=null && dto.getDetIndicePlaca().size() > 0){
			
			xmlIndicePlaca.append("<contenido>");
		}
		
		// se recorre cada una de las piezas del indice de placa
		for(DtoDetalleIndicePlaca elem: dto.getDetIndicePlaca())
		{
			// se crea la etiqueta del diente
			if(elem.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente))
			{
				if(codigoPieza!=ConstantesBD.codigoNuncaValido){
					xmlIndicePlaca.append("</diente>");
				}
					
				xmlIndicePlaca.append("<diente " +
						"pieza=\""+elem.getPiezaDental().getCodigo()+"\" " +
						"indicativo=\""+ConstantesIntegridadDominio.acronimoAusente+"\">");
			}else{
				
				if(codigoPieza!=elem.getPiezaDental().getCodigo())
				{
					if(codigoPieza!=ConstantesBD.codigoNuncaValido){
						
						xmlIndicePlaca.append("</diente>");
					}
						
					xmlIndicePlaca.append("<diente pieza=\""+elem.getPiezaDental().getCodigo()+"\" indicativo=\""+ConstantesIntegridadDominio.acronimoPlaca+"\">");
				}

				// se crea la etiqueta de la superficie
				xmlIndicePlaca.append("<superficie " +
						"codigo=\""+elem.getSuperficie().getCodigoPk()+"\" " +
						"nombre=\""+elem.getSuperficie().getNombre()+"\" " +
						"sector=\""+elem.getSuperficie().getSector()+"\" >");
				xmlIndicePlaca.append("</superficie>");
			}
			codigoPieza = elem.getPiezaDental().getCodigo();
		}
		
		if(xmlIndicePlaca.length() > 0){
			
			xmlIndicePlaca.append("</diente>");
			xmlIndicePlaca.append("</contenido>");
		}
		
		dto.setXmlIndicePlaca(xmlIndicePlaca.toString());
	}
	
	/**
	 * metod que a partir de una cadena con propiedades de xml se llena los datos en una 
	 * estructura DtoComponenteIndicePlaca
	 * @param xmlCompIndPlaca
	 * @return
	 */
	private static DtoComponenteIndicePlaca getComponenteIndicePlacaDeXML(String xmlIndicePlaca)
	{
		String xmlCompIndPlaca = xmlIndicePlaca;
		DtoComponenteIndicePlaca dto = new DtoComponenteIndicePlaca();
		ArrayList<DtoDetalleIndicePlaca> array = new ArrayList<DtoDetalleIndicePlaca>(); 
		if(!xmlCompIndPlaca.equals(""))
		{
			Document dom;
		    // A. Parsear el archivo XML
		    // 1. Obtener el objeto DocumentBuilderFactory, con el que se creará el documento XML
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try 
	        {
	            // 2. Usar DocumentBuilderFactory para crear un DocumentBuilder
	        	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setNamespaceAware(true);
	            DocumentBuilder builder = builderFactory.newDocumentBuilder();
	        	
	            // 3. Parsear a partir de un Strig
	            StringBuffer xml = new StringBuffer(xmlCompIndPlaca);
	            logger.info("xmlCompIndPlaca "+xmlCompIndPlaca);
	            ByteArrayInputStream bs = new ByteArrayInputStream(xml.toString().getBytes());
				Document docR = builder.parse (bs);
				
	            //Obtener el documento raiz
	        	//Se crea un objeto Element, a partir del documento XML
	            Element docEle = docR.getDocumentElement();
	            
	            //Obtener un nodelist de elementos <contenido>
	            NodeList nl = docEle.getElementsByTagName("diente");
	            
	            if(nl != null && nl.getLength() > 0)
	            {
	            	logger.info("size nodo diente: "+nl.getLength());
	                for(int i = 0; i < nl.getLength(); i++)
	                {
	                	//Toma las piezas dentales del componponente indice de placa
	                    Element piezaElemento = (Element) nl.item(i);
	                    
	                    if(Utilidades.convertirAEntero(piezaElemento.getAttribute("pieza"))!=ConstantesBD.codigoNuncaValido)
	                    {
		                    String indicativoAux = piezaElemento.getAttribute("indicativo");
		                    
		                    if(!indicativoAux.equals(ConstantesIntegridadDominio.acronimoAusente))
		                    {
			                    NodeList superficieNodo = piezaElemento.getElementsByTagName("superficie");
			                    logger.info("size nodo superficie: "+superficieNodo.getLength());
			                    for(int j=0; j<superficieNodo.getLength(); j++)
				                {
			                    	DtoDetalleIndicePlaca detIndPlaca = new DtoDetalleIndicePlaca();
				                    detIndPlaca.setPiezaDental(new InfoDatosInt(Utilidades.convertirAEntero(piezaElemento.getAttribute("pieza"))));
				                    detIndPlaca.setIndicador(piezaElemento.getAttribute("indicativo"));
				                    detIndPlaca.setNuevo(ConstantesBD.acronimoSi);
				                    
			                    	Element superficieElemento = (Element) superficieNodo.item(j);
	
			                    	DtoDetSuperficieIndicePlaca superficieIndPlaca = new DtoDetSuperficieIndicePlaca();
			                    	superficieIndPlaca.setCodigoPk(Utilidades.convertirAEntero(superficieElemento.getAttribute("codigo")));
			                    	superficieIndPlaca.setNombre(superficieElemento.getAttribute("nombre"));
			                    	superficieIndPlaca.setSector(Utilidades.convertirAEntero(superficieElemento.getAttribute("sector").toString()));
			                    	
			                    	// se adiciona las combinaciones que pose la pieza dental en relacion a la superficie
			                    	detIndPlaca.setSuperficie(superficieIndPlaca);
			                    	
			                    	// se adiciona los datos de una pieza al componente indice de placa
				                    dto.getDetIndicePlaca().add(detIndPlaca);
				                }
		                    }else{
		                    	DtoDetalleIndicePlaca detIndPlacaAus = new DtoDetalleIndicePlaca();
		                    	detIndPlacaAus.setPiezaDental(new InfoDatosInt(Utilidades.convertirAEntero(piezaElemento.getAttribute("pieza"))));
		                    	detIndPlacaAus.setIndicador(piezaElemento.getAttribute("indicativo"));
		                    	detIndPlacaAus.setNuevo(ConstantesBD.acronimoSi);
			                    
			                    DtoDetSuperficieIndicePlaca superficieIndPlacaAus = new DtoDetSuperficieIndicePlaca();
			                    // se adiciona las combinaciones que pose la pieza dental en relacion a la superficie
			                    detIndPlacaAus.setSuperficie(superficieIndPlacaAus);
		                    	
			                    // se adiciona los datos de una pieza al componente indice de placa
		                    	dto.getDetIndicePlaca().add(detIndPlacaAus);
		                    }
	                    }else{
	                    	// obtener los atributos generales del componente
	                    	
	                    	// obtner el valor de porcentaje
	                    	Element porcentajeElemento = (Element) piezaElemento.getElementsByTagName("porcentaje").item(0);
	                    	if(!porcentajeElemento.getTextContent().equals(""))
                    			dto.setPorcentaje(new BigDecimal(porcentajeElemento.getTextContent().substring(1, porcentajeElemento.getTextContent().length()-1)));
                    		else
                    			dto.setPorcentaje(new BigDecimal(ConstantesBD.codigoNuncaValido));
	                    	
	                    	// obtner el valor de interpretacion
	                    	Element interpretacionElemento = (Element) piezaElemento.getElementsByTagName("interpretacion").item(0);
	                    	if(!interpretacionElemento.getTextContent().equals(""))
                    			dto.setInterpretacion(interpretacionElemento.getTextContent().substring(1, interpretacionElemento.getTextContent().length()-1));
                    		else
                    			dto.setInterpretacion("");
	                    	
	                    	
	                    }
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
		return dto;
	}
	
	/**
	 * metodo que carga el componente indice de placa
	 * @param dto
	 */
	private static void cargarComponenteIndicePlaca(DtoComponenteIndicePlaca dto)
	{
		DtoComponenteIndicePlaca dtoConsulta = new DtoComponenteIndicePlaca();
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			dtoConsulta = UtilidadOdontologia.consultarComponenteIndicePlaca(con, dto.getPlantillaIngreso(),dto.getPlantillaEvolucionOdo(), dto.getCodigoPaciente());
			
			if(dtoConsulta.getPorConfirmar().equals(ConstantesBD.acronimoSi))
			{
				// se consulta el detalle indice de placa
				dto.setDetIndicePlaca(UtilidadOdontologia.consultarDetalleCompIndicePlaca(
						con, 
						dto.getCodigoInstitucion(), 
						dtoConsulta.getCodigoPk()));
				
				// se transladan los datos al dto que maneja el componente odontologico
				dto.setCodigoPk(dtoConsulta.getCodigoPk());
				dto.setImagen(dtoConsulta.getImagen());
				dto.setPorcentaje(dtoConsulta.getPorcentaje());
				dto.setInterpretacion(dtoConsulta.getInterpretacion());
				dto.setPlantillaIngreso(dtoConsulta.getPlantillaIngreso());
				dto.setPlantillaEvolucionOdo(dtoConsulta.getPlantillaEvolucionOdo());
				dto.setPorConfirmar(dtoConsulta.getPorConfirmar());
				dto.setPorActualizar(ConstantesBD.acronimoSi);
				
			}else{
				logger.info("en esta parte se debe mostrar la imagen generada");
			}
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error en el cargado del componente indice de placa");
		}
	}

	/**
	 * metodo de comparacion y actualizacion indice de placa
	 * @param dto
	 */
	private static void actualizarDtoCompIndiceplaca(DtoComponenteIndicePlaca dto)
	{
		InfoDatosInt superficiesDentales = new InfoDatosInt();
		DtoComponenteIndicePlaca dtoNew = new DtoComponenteIndicePlaca();
		dtoNew = getComponenteIndicePlacaDeXML(dto.getXmlIndicePlaca());
		dto.setPorcentaje(dtoNew.getPorcentaje());
		dto.setInterpretacion(dtoNew.getInterpretacion());
		ArrayList<DtoDetalleIndicePlaca> arrayAux = new ArrayList<DtoDetalleIndicePlaca>(); 
		
		if(dto.getDetIndicePlaca().size()>0)
		{
			for(int w=0;w<dto.getDetIndicePlaca().size();w++)
			{
				DtoDetalleIndicePlaca elem1 = (DtoDetalleIndicePlaca) dto.getDetIndicePlaca().get(w) ;
	
				for(DtoDetalleIndicePlaca elem : dtoNew.getDetIndicePlaca())
				{
					if(elem.getSuperficie().getCodigoPk()<=0)
					{
						if(elem.getSuperficie().getSector()!=ConstantesBD.codigoNuncaValido)
						{
							superficiesDentales = UtilidadOdontologia.obtenerSuperficieDental(
									dto.getCodigoInstitucion(), 
									ConstantesBD.acronimoSi, 
									elem.getSuperficie().getSector(), elem1.getPiezaDental().getCodigo());
							elem.getSuperficie().setCodigoPk(superficiesDentales.getCodigo());
							elem.getSuperficie().setNombre(superficiesDentales.getNombre());
						}
					}
					
					if(elem.getPiezaDental().getCodigo() == elem1.getPiezaDental().getCodigo())
					{
						if(elem.getSuperficie().getCodigoPk() == elem1.getSuperficie().getCodigoPk())
						{
							elem.setEliminar(ConstantesBD.acronimoNo);
							elem.setNuevo(ConstantesBD.acronimoNo);
							elem.setModificar(ConstantesBD.acronimoNo);
							dto.getDetIndicePlaca().get(w).setContado(ConstantesBD.acronimoSi);
						}else{
							if((elem1.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente)
								&& !elem.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente))
								|| (elem1.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente)
										&& elem.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente)))
							{
								// se marca el como una detalle nuevo
								elem.setEliminar(ConstantesBD.acronimoNo);
								elem.setNuevo(ConstantesBD.acronimoSi);
								elem.setModificar(ConstantesBD.acronimoNo);
								
								
								if(elem.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente))
								{
									elem.setIndicador(ConstantesIntegridadDominio.acronimoAusente);
									elem.getSuperficie().setCodigoPk(ConstantesBD.codigoNuncaValido);
								}
								else
									elem.setIndicador(ConstantesIntegridadDominio.acronimoPlaca);
								
								// se marca para eliminar la pieza de con todas sus posibles combinaciones
								dto.getDetIndicePlaca().get(w).setEliminar(ConstantesBD.acronimoSi);
								dto.getDetIndicePlaca().get(w).setNuevo(ConstantesBD.acronimoNo);
								dto.getDetIndicePlaca().get(w).setModificar(ConstantesBD.acronimoNo);
								dto.getDetIndicePlaca().get(w).setContado(ConstantesBD.acronimoSi);
								
								// buscar que el detalle indice placa no haya sido ingresado
								boolean bandAdd = true;
								if(arrayAux.size()>0)
								{
									for(int j=0;j<arrayAux.size();j++)
									{	
										DtoDetalleIndicePlaca data = (DtoDetalleIndicePlaca) arrayAux.get(j);
										if(data.getPiezaDental().getCodigo() == elem1.getPiezaDental().getCodigo())
										{
											bandAdd = false;
											break;
										}
									}
									if(bandAdd)
										arrayAux.add(elem1);
								}else
									arrayAux.add(elem1);
							
								
							}else{
								if(elem1.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente)
									&& elem.getIndicador().equals(ConstantesIntegridadDominio.acronimoAusente))
								{
									boolean bandCam = true;
									if(arrayAux.size()>0)
									{
										for(int j=0;j<arrayAux.size();j++)
										{	
											DtoDetalleIndicePlaca data = (DtoDetalleIndicePlaca) arrayAux.get(j);
											if(data.getPiezaDental().getCodigo() == elem1.getPiezaDental().getCodigo())
											{
												bandCam = false;
												break;
											}
										}
										if(bandCam)
										{
											elem.setEliminar(ConstantesBD.acronimoNo);
											elem.setNuevo(ConstantesBD.acronimoNo);
											elem.setModificar(ConstantesBD.acronimoNo);
											dto.getDetIndicePlaca().get(w).setContado(ConstantesBD.acronimoSi);
										}
									}else{
										elem.setEliminar(ConstantesBD.acronimoNo);
										elem.setNuevo(ConstantesBD.acronimoNo);
										elem.setModificar(ConstantesBD.acronimoNo);
										dto.getDetIndicePlaca().get(w).setContado(ConstantesBD.acronimoSi);
									}
								}
							}
						}
					}
				}
			}
		}else{
			logger.info("el size es cero");
			for(DtoDetalleIndicePlaca elem : dtoNew.getDetIndicePlaca())
			{
				if(elem.getSuperficie().getCodigoPk()<=0)
				{
					if(elem.getSuperficie().getSector()!=ConstantesBD.codigoNuncaValido)
					{
						superficiesDentales = UtilidadOdontologia.obtenerSuperficieDental(
								dto.getCodigoInstitucion(), 
								ConstantesBD.acronimoSi, 
								elem.getSuperficie().getSector(), elem.getPiezaDental().getCodigo());
						elem.getSuperficie().setCodigoPk(superficiesDentales.getCodigo());
						elem.getSuperficie().setNombre(superficiesDentales.getNombre());
					}
				}
			}
		}
		
		
		logger.info("size: "+arrayAux.size());
		for(int i=0;i<arrayAux.size();i++)
		{
			logger.info("codigo Pieza: "+arrayAux.get(i).getPiezaDental().getCodigo());
			dtoNew.getDetIndicePlaca().add(arrayAux.get(i));
		}
		
		
		for(int j=0;j<dto.getDetIndicePlaca().size();j++)
		{
			DtoDetalleIndicePlaca dtoIndPlaca = (DtoDetalleIndicePlaca) dto.getDetIndicePlaca().get(j);
			if(dtoIndPlaca.getContado().equals(ConstantesBD.acronimoNo))
			{
				dtoIndPlaca.setEliminar(ConstantesBD.acronimoSi);
				dtoIndPlaca.setNuevo(ConstantesBD.acronimoNo);
				dtoIndPlaca.setModificar(ConstantesBD.acronimoNo);
				dtoIndPlaca.setContado(ConstantesBD.acronimoSi);
				dtoNew.getDetIndicePlaca().add(dtoIndPlaca);
			}
		}
		
		
		dto.setDetIndicePlaca(dtoNew.getDetIndicePlaca());
	}
	
	/**
	 * medodo de guardado del indice de placa
	 * @param con
	 * @param dto
	 * @return
	 */
	private static boolean guardarIndicePlaca(Connection con, DtoComponenteIndicePlaca dto)
	{
		boolean enTransaccion = true;
		
		/*
		UtilidadFileUpload.generarCopiaArchivo(pathOriginal, nombreOriginal, pathCopia, nombreCopia)
		
		File archi = new File();
		archi.renameTo(dest)
		
		*/
		logger.info("¿Se deb actualizar el índice de placa ? "+dto.getPorActualizar());
		if(dto.getPorActualizar().equals(ConstantesBD.acronimoSi))
		{
			if(!UtilidadOdontologia.actualizarComponenteIndicePlaca(con, dto)) // se actualiza el componenete indice de placa
				enTransaccion = false;
		}else{
			if(UtilidadOdontologia.insertarIndicePlaca(con,dto)==ConstantesBD.codigoNuncaValido) // se inserta un nuevo componente odontologico
				enTransaccion = false;
		}
		logger.info("continuo en transaccion indice de placa? "+enTransaccion);
		if(enTransaccion)
		{
			for(DtoDetalleIndicePlaca elem: dto.getDetIndicePlaca())
			{
				elem.setCodigoIndicePlaca(dto.getCodigoPk());
				if(elem.getEliminar().equals(ConstantesBD.acronimoSi))
				{
					if(!UtilidadOdontologia.eliminarDetalleCompIndicePlaca(con, elem))
					{
						logger.info("fallé al eliminar el etalle del indice de placa!!!");
						return false;
					}
				}else{
					if(elem.getNuevo().equals(ConstantesBD.acronimoSi))
					{
						if(UtilidadOdontologia.insertDetalleCompIndicePlaca(con, elem) <=0)
						{
							logger.info("fallé al insertar el etalle del indice de placa!!!");
							return false;
						}
					}else{
						if(elem.getModificar().equals(ConstantesBD.acronimoSi))
						{
							if(!UtilidadOdontologia.actualizarDetalleCompIndicePlaca(con, elem))
							{
								logger.info("fallé al actualizar el etalle del indice de placa!!!");
								return false;
							}
						}
					}
				}
			}
			return true;
		}else
			return false;
	}
	
	/**
	 * metodo de accion de indice de placa
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean accionIndicePlaca(Connection con, DtoComponenteIndicePlaca dto)
	{
		
		actualizarDtoCompIndiceplaca(dto);
		
		if(dto.getDetIndicePlaca().size()>0)
		{
			SortDetalleIndicePlaca sort= new SortDetalleIndicePlaca();
			sort.setPatronOrdenar("eliminar_descendente");
			Collections.sort(dto.getDetIndicePlaca(), sort);
			logger.info("Vamos a guardar el indice de placa----");
			if(guardarIndicePlaca(con, dto))
				return true;
			else
				return false;
		}else
			return true;
	}
	
	/**
	 * validacion indice de placa
	 * @param dto
	 * @return
	 */
	public static ActionErrors validarIndicePlaca(DtoComponenteIndicePlaca dto)
	{
		ActionErrors errores = new ActionErrors();
		if(!dto.getImagen().equals("")&& dto.getImagen().split(".").length>0)
			errores.add("", new ActionMessage("errors.required","La Confirmación del Indice de Placa "));
		return errores;
	}
}
