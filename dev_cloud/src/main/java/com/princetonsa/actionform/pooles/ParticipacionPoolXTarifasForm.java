/*
 * @(#)ParticipacionPoolXTarifasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.pooles;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * la participación pool X tarifas. 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Nov 30, 2004
 * @author wrios 
 */
public class ParticipacionPoolXTarifasForm extends ValidatorForm
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase censocamasform
	 */
	Logger logger = Logger.getLogger(ParticipacionPoolXTarifasForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
	
	/**
	 * Código del pool
	 */
	private int codigoPool;
	
	/**
	 * Descripción del pool
	 */
	private String descripcionPool;

	/**
	 * Cód del esquema tarifario
	 */
	private int codigoEsquemaTarifario;
    
	/**
	 * Nombre del esquema Tarifario
	 */
	private String nombreEsquemaTarifario;
	
	/**
	 * Porcentaje de participación
	 */
	private String porcentajeParticipacionString;
	
	/**
	 * 
	 */
	private double valorParticipacion;
	
	/**
	 * Cuenta contable por pagar al Pool
	 */
	private String cuentaPoolString;
	
	/**
	 * Cuenta contable por ingresos del pool
	 */
	private String cuentaInstitucionString;
	
	/**
	 * Cuenta contable por ingresos del pool
	 */
	private String cuentaInstitucionStringAnterior;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * Mapa con los pool X Tarifas
	 */
	private HashMap mapaPoolXTarifas;
	
	/**
	 * Mapa con los pool X tarifas que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaPoolXTarifasNoModificado;
	
	/**
	 * Mapa con los pool X tarifas auxiliar
	 */
	private HashMap mapaPoolXTarifasAux;

	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";

	/**
	 * Offset del HashMap
	 */
	private int offsetHash;
	
	/**
	 * La consulta de la info de la participación por pooles está devolviendo 
	 * actualmente estos campos= 
	 * ("codigoEsquemaTarifario", "nombreEsquemaTarifario", "porcentajeParticipacion", 
	 * "cuentaPool", "cuentaInstitucion", "estaBD",  "esEliminada" ),
	 * como cada uno de ellos ocupa una posición en el HashMap, entonces
	 * toca calcular el verdadero tamanio en filas= HashMap.size/7;
	 */
	private int numeroRealFilasMapa;
	
	
	/**
	 * Tipo de Parametrizacion 
	 * */
	private String tipoParame;
	
	/**
	 * //num de columnas que devuelve la consulta de la info de los 
	 * PoolXTarifas en este caso=  
	 * ("codigoEsquemaTarifario", "nombreEsquemaTarifario", "porcentajeParticipacion", 
	 * "cuentaPool", "cuentaInstitucion", "cuentaInstitucionAnterior", "estaBD",  "esEliminada" ),
	 * este valor es necesario a la hora de calcular el numero de filas del mapa 
	 */
	final int tamanioNumeroColumnas=9;
	
	/**
	 * Cód del index del Mapa
	 */
	private int indexMapa;
	
	
	private HashMap poolesXConvenio;	
	/**
	 * Número de Items mostrados por página, 
	 * Se definió en este punto porque al ingresar un nuevo elemento
	 * al pager, se debe hacer un cálculo de l última ésta página y en este punto
	 * se debe conocer el número de items. 
	 * 
	 */
	
	/**
	 * 
	 */
	private String codigoConvenio;
	
	public int maxPagesItemsHash;
	
    /**
     * @return Returns the maxPagesItemsHash.
     */
    public int getMaxPagesItemsHash() {
        return maxPagesItemsHash;
    }
    
    public ArrayList<HashMap<String, Object>> convenios;
    
    
	/**
	 * Método que compara los dos Mapas Original - Modificado, y los compara hasta el
	 * size del original y devuelve un nuevo HashMap con los campos que han sido modificados.
	 * @return
	 */
	public HashMap comparar2HashMap()
	{
	    HashMap mapaCamposModificados=new HashMap();
	    String campoMapaOriginalNoMod="", campoMapaModificado=""; 
	    for(int k=0; k<mapaPoolXTarifasNoModificado.size()/this.tamanioNumeroColumnas; k++)
	    {
	        if((this.getMapaPoolXTarifas("estaBD_"+k)+"").equals("t"))
	        {
	            if((this.getMapaPoolXTarifas("esEliminada_"+k)+"").equals("f"))
		        {
			        campoMapaOriginalNoMod=this.getMapaPoolXTarifasNoModificado("codigoEsquemaTarifario_"+k)+"";
			        if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
			        {
			            campoMapaModificado=this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k)+"";
			            
			            if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
			            {
			                mapaCamposModificados.put("codigoEsquemaTarifario_"+ k, this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k));
			                mapaCamposModificados.put("porcentajeParticipacion_"+ k, this.getMapaPoolXTarifas("porcentajeParticipacion_"+k));
			                mapaCamposModificados.put("valorParticipacion_"+ k, this.getMapaPoolXTarifas("valorParticipacion_"+k));
			        	    mapaCamposModificados.put("cuentaPool_"+ k, this.getMapaPoolXTarifas("cuentaPool_"+k));
			        	    mapaCamposModificados.put("cuentaInstitucion_"+ k, this.getMapaPoolXTarifas("cuentaInstitucion_"+k));
			        	    mapaCamposModificados.put("cuentaInstitucionAnterior_"+ k, this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k));
			            }
			            /*de lo contrario entonces compare el porcentajeParticipacion_*/
			            else
			            {
			                campoMapaOriginalNoMod=this.getMapaPoolXTarifasNoModificado("porcentajeParticipacion_"+k)+"";
			                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
			    	        {
			                    campoMapaModificado=this.getMapaPoolXTarifas("porcentajeParticipacion_"+k)+"";
			                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
			    	            {
			                        mapaCamposModificados.put("codigoEsquemaTarifario_"+ k, this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k));
					        	    mapaCamposModificados.put("porcentajeParticipacion_"+ k, this.getMapaPoolXTarifas("porcentajeParticipacion_"+k));
					        	    mapaCamposModificados.put("valorParticipacion_"+ k, this.getMapaPoolXTarifas("valorParticipacion_"+k));
					        	    mapaCamposModificados.put("cuentaPool_"+ k, this.getMapaPoolXTarifas("cuentaPool_"+k));
					        	    mapaCamposModificados.put("cuentaInstitucion_"+ k, this.getMapaPoolXTarifas("cuentaInstitucion_"+k));
					        	    mapaCamposModificados.put("cuentaInstitucionAnterior_"+ k, this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k));
					        	}
			                    /* de lo contrario compare cuenta pool*/
			                    else
					            {
					                campoMapaOriginalNoMod=this.getMapaPoolXTarifasNoModificado("cuentaPool_"+k)+"";
					                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
					    	        {
					                    campoMapaModificado=this.getMapaPoolXTarifas("cuentaPool_"+k)+"";
					                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
					    	            {
					                        mapaCamposModificados.put("codigoEsquemaTarifario_"+ k, this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k));
							        	    mapaCamposModificados.put("porcentajeParticipacion_"+ k, this.getMapaPoolXTarifas("porcentajeParticipacion_"+k));
							        	    mapaCamposModificados.put("valorParticipacion_"+ k, this.getMapaPoolXTarifas("valorParticipacion_"+k));
							        	    mapaCamposModificados.put("cuentaPool_"+ k, this.getMapaPoolXTarifas("cuentaPool_"+k));
							        	    mapaCamposModificados.put("cuentaInstitucion_"+ k, this.getMapaPoolXTarifas("cuentaInstitucion_"+k));
							        	    mapaCamposModificados.put("cuentaInstitucionAnterior_"+ k, this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k));
							        	}
					                    /* de lo contrario compare cuenta pool*/
					                    else
							            {
							                campoMapaOriginalNoMod=this.getMapaPoolXTarifasNoModificado("cuentaInstitucion_"+k)+"";
							                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
							    	        {
							                    campoMapaModificado=this.getMapaPoolXTarifas("cuentaInstitucion_"+k)+"";
							                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
							    	            {
							                        mapaCamposModificados.put("codigoEsquemaTarifario_"+ k, this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k));
									        	    mapaCamposModificados.put("porcentajeParticipacion_"+ k, this.getMapaPoolXTarifas("porcentajeParticipacion_"+k));
									        	    mapaCamposModificados.put("valorParticipacion_"+ k, this.getMapaPoolXTarifas("valorParticipacion_"+k));
									        	    mapaCamposModificados.put("cuentaPool_"+ k, this.getMapaPoolXTarifas("cuentaPool_"+k));
									        	    mapaCamposModificados.put("cuentaInstitucion_"+ k, this.getMapaPoolXTarifas("cuentaInstitucion_"+k));
									        	    mapaCamposModificados.put("cuentaInstitucionAnterior_"+ k, this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k));
									        	}
							                    else
							                    {
							                    	campoMapaOriginalNoMod=this.getMapaPoolXTarifasNoModificado("cuentaInstitucionAnterior_"+k)+"";
									                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
									    	        {
									                    campoMapaModificado=this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k)+"";
									                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
									    	            {
									                        mapaCamposModificados.put("codigoEsquemaTarifario_"+ k, this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k));
											        	    mapaCamposModificados.put("porcentajeParticipacion_"+ k, this.getMapaPoolXTarifas("porcentajeParticipacion_"+k));
											        	    mapaCamposModificados.put("valorParticipacion_"+ k, this.getMapaPoolXTarifas("valorParticipacion_"+k));
											        	    mapaCamposModificados.put("cuentaPool_"+ k, this.getMapaPoolXTarifas("cuentaPool_"+k));
											        	    mapaCamposModificados.put("cuentaInstitucion_"+ k, this.getMapaPoolXTarifas("cuentaInstitucion_"+k));
											        	    mapaCamposModificados.put("cuentaInstitucionAnterior_"+ k, this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k));
											        	}
									                    else
									                    {
									                    	campoMapaOriginalNoMod=this.getMapaPoolXTarifasNoModificado("valorParticipacion_"+k)+"";
											                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
											    	        {
											                    campoMapaModificado=this.getMapaPoolXTarifas("valorParticipacion_"+k)+"";
											                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
											    	            {
											                        mapaCamposModificados.put("codigoEsquemaTarifario_"+ k, this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k));
													        	    mapaCamposModificados.put("porcentajeParticipacion_"+ k, this.getMapaPoolXTarifas("porcentajeParticipacion_"+k));
													        	    mapaCamposModificados.put("valorParticipacion_"+ k, this.getMapaPoolXTarifas("valorParticipacion_"+k));
													        	    mapaCamposModificados.put("cuentaPool_"+ k, this.getMapaPoolXTarifas("cuentaPool_"+k));
													        	    mapaCamposModificados.put("cuentaInstitucion_"+ k, this.getMapaPoolXTarifas("cuentaInstitucion_"+k));
													        	    mapaCamposModificados.put("cuentaInstitucionAnterior_"+ k, this.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k));
													        	}
											    	        }    
									                    }
									    	        } 
							                    }
							    	        }
							            }	
					    	        }
					            }	
			    	        }
			            }
			        }    
		        }
	        }    
	    }
	    return mapaCamposModificados;
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (estado.equals("guardarConvenio"))
		{
			int numReg=Integer.parseInt(this.getMapaPoolXTarifas("numRegistros")+"");
			
			for (int i=0;i<numReg;i++)
			{
				if( this.getMapaPoolXTarifas("esEliminada_"+i).equals("f"))
				{
					logger.info("entro al for de guardarConvenio");
					//se valida que el convenio no este vacio
					if(this.getMapaPoolXTarifas("codigoConvenio_"+i).toString().trim().equals("") || this.getMapaPoolXTarifas("codigoConvenio_"+i).toString().trim().equals("-1"))
						errores.add("descripcion",new ActionMessage("errors.required","El Convenio del registro "+(i+1)));
					
					//se valida que el pool no este vacio
					if(this.getCodigoPool()==-1)
							errores.add("descripcion",new ActionMessage("errors.required","El Pool"));
					else
					{	
						for (int j=0;j<numReg;j++)
						{
							if (j!=i)
							{	
								if( this.getMapaPoolXTarifas("esEliminada_"+j).equals("f"))
								{
									if (((this.getMapaPoolXTarifas("codigoConvenio_"+i).toString()+this.getCodigoPool()).toString()).equals((this.getMapaPoolXTarifas("codigoConvenio_"+j).toString()+this.getCodigoPool())))
									{	
										if (this.getMapaPoolXTarifas("estaBD_"+i).toString().equals("f"))
										{
											errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","convenio y pool en el registro "+(i+1)));
											j=numReg;
										}
									}
								}
							}	
						}
						
						String tempoNom= this.getMapaPoolXTarifas("nombreConvenio_"+i)+"";
						
						if( ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""))<0) && ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("valorParticipacion_"+i)+""))<0))
		            	{
		            		errores.add("% participación es requerida", new ActionMessage("errors.required","El % o valor de participación "));
		            	}
		            	else if( ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""))>=0) && ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("valorParticipacion_"+i)+""))>=0))
		            	{
		            		errores.add("% participación es requerida", new ActionMessage("errors.notEspecific","El % y valor de participación deben ser exluyentes "));
		            	}
		            	else
		                {   
		            		
		            		double tempDouble= Utilidades.convertirADouble(this.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+"");
			                
		            		if(tempDouble>0)
		            		{	
				                if(tempDouble>100)
				                {
				                    errores.add("% participacion", new ActionMessage("errors.range", "El % participación "+tempDouble+tempoNom+" es entero o decimal y", "cero (0)", "cien (100)"));
				                }
				                else
				                {
				                    boolean esMayorAdosDecimales=UtilidadValidacion.esMayorNdecimales(tempDouble,2);
				                    if(esMayorAdosDecimales)
				                    {
				                        errores.add("% mayor a dos decimales", new ActionMessage("errors.numDecimales", "El % participación "+tempDouble+tempoNom, "dos (2)"));
				                    }
				                }
		            		}    
			            }
					}
				}
			}
		}
		
		
		
		
		if(estado.equals("modificarEsquemaTarifario"))
		{				
			errores=super.validate(mapping,request);
			
			String tempo = this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+this.getIndexMapa())+""; 
			
			if(tempo.equals("0") || tempo.equals("-1")){}
			else
			{    
				for(int i=0; i<this.getNumeroRealFilasMapa(); i++)
				{
				    if(i!=this.getIndexMapa())
					{    
					    if((this.getMapaPoolXTarifas("esEliminada_"+i)+"").equals("f"))
					    {
					        if( (this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+i)+"").equals(tempo) )
					        {
					            i=this.getNumeroRealFilasMapa();
					            this.setMapaPoolXTarifas("codigoEsquemaTarifario_"+this.getIndexMapa(), "-1");
					            this.setMapaPoolXTarifas("nombreEsquemaTarifario_"+this.getIndexMapa(), "Seleccione");
					            errores.add("error.pooles.yaexiste", new ActionMessage("error.pooles.yaexiste"));
					        }
					    }
					}	    
				}
			}
			
			if((this.getMapaPoolXTarifas("esBusquedaAvanzada")+"").equals("t"))
			{
			    /*EN ESTE PUNTO COMPARA LOS DE LA BD QUE NO FUERON LA BUSQUEDA*/
		        for(int k=0; k<(this.getMapaPoolXTarifasAux().size()-1); k++)
		        {
		            if((this.getMapaPoolXTarifasAux("codigoEsquemaTarifario_"+k)+"").equals(tempo))
		            {
		                k=this.getMapaPoolXTarifasAux().size();
		                this.setMapaPoolXTarifas("codigoEsquemaTarifario_"+this.getIndexMapa(), "-1");
			            this.setMapaPoolXTarifas("nombreEsquemaTarifario_"+this.getIndexMapa(), "Seleccione");
			            errores.add("error.pooles.yaexiste", new ActionMessage("error.pooles.yaexiste"));
		            }
		        }
			}
		}
		
		if(estado.equals("guardar") || estado.equals("ingresarNuevoElementoMapa"))
		{
		    errores=super.validate(mapping, request);
		      
		    for(int i=0; i<this.getNumeroRealFilasMapa(); i++)
			{
		    	if((this.getMapaPoolXTarifas("esEliminada_"+i)+"").equals("f") && this.getTipoParame().equals(ConstantesIntegridadDominio.acronimoTarifario))
		        {
		            String tempoNomEsq=" [esquema tarifario: "+this.getMapaPoolXTarifas("nombreEsquemaTarifario_"+i)+"] ";
		            if(    (this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+i)+"").equals("-1")  ||
		                   (this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+i)+"").equals("0")   ||  
		                   (this.getMapaPoolXTarifas("codigoEsquemaTarifario_"+i)+"").equals("null")   )
		            {
		                errores.add("El esquema Tarifario requerido", new ActionMessage("errors.required","El campo esquema Tarifario "));
		            }
	            	if( ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""))<0) && ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("valorParticipacion_"+i)+""))<0))
	            	{
	            		errores.add("% participación es requerida", new ActionMessage("errors.required","El % o valor de participación "));
	            	}
	            	else if( ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""))>=0) && ((Utilidades.convertirADouble(this.getMapaPoolXTarifas("valorParticipacion_"+i)+""))>=0))
	            	{
	            		errores.add("% participación es requerida", new ActionMessage("errors.notEspecific","El % y valor de participación deben ser exluyentes "));
	            	}
	            	else
	                {   
	            		
	            		double tempDouble= Utilidades.convertirADouble(this.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+"");
		                
	            		if(tempDouble>0)
	            		{	
			                if(tempDouble>100)
			                {
			                    errores.add("% participacion", new ActionMessage("errors.range", "El % participación "+tempDouble+tempoNomEsq+" es entero o decimal y", "cero (0)", "cien (100)"));
			                }
			                else
			                {
			                    boolean esMayorAdosDecimales=UtilidadValidacion.esMayorNdecimales(tempDouble,2);
			                    if(esMayorAdosDecimales)
			                    {
			                        errores.add("% mayor a dos decimales", new ActionMessage("errors.numDecimales", "El % participación "+tempDouble+tempoNomEsq, "dos (2)"));
			                    }
			                }
	            		}    
		            }    
		            if(!errores.isEmpty())
					{
							this.setEstado("redireccion");
					}		
		        }
			}
		    
		}
		return errores;
	}

	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset()
	{
	    this.codigoPool=-1;
	    this.tipoParame = "-1";
	    this.descripcionPool="";
	    this.codigoEsquemaTarifario=0;
	    this.nombreEsquemaTarifario="";
	    this.porcentajeParticipacionString="";
	    this.cuentaPoolString="";
	    this.cuentaInstitucionString="";
	    this.cuentaInstitucionStringAnterior="";
	    this.poolesXConvenio = new HashMap ();
	    this.setPoolesXConvenio("numRegistros", "0");
	    this.linkSiguiente="";
	    this.offsetHash=0;
	    this.numeroRealFilasMapa=0;
		this.poolesXConvenio = new HashMap ();
		this.setPoolesXConvenio("numRegistros", "0");
		this.maxPagesItemsHash =0;
		this.valorParticipacion=0;
	}
	
	public void resetMapa()
	{
	    this.mapaPoolXTarifas=new HashMap();
	    this.setMapaPoolXTarifas("numRegistros","0");
	    this.mapaPoolXTarifasNoModificado= new HashMap();
	    this.mapaPoolXTarifasAux= new HashMap();
	    this.poolesXConvenio = new HashMap ();
	    this.setPoolesXConvenio("numRegistros", "0");
	}
	
	/**
	 * Set del mapa de PoolXTarifas paciente
	 * @param key
	 * @param value
	 */
	public void setMapaPoolXTarifas(String key, Object value){
		mapaPoolXTarifas.put(key, value);
	}
	/**
	 * Get del mapa de PoolXTarifas paciente
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaPoolXTarifas(String key){
		return mapaPoolXTarifas.get(key);
	}
	/**
	 * Set del mapa de PoolXTarifas paciente no modificado
	 * @param key
	 * @param value
	 */
	public void setMapaPoolXTarifasNoModificado(String key, Object value){
		mapaPoolXTarifasNoModificado.put(key, value);
	}
	/**
	 * Get del mapa de PoolXTarifas paciente no modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaPoolXTarifasNoModificado(String key){
		return mapaPoolXTarifasNoModificado.get(key);
	}
	/**
	 * Set del mapa de PoolXTarifas paciente auxiliar
	 * @param key
	 * @param value
	 */
	public void setMapaPoolXTarifasAux(String key, Object value){
		mapaPoolXTarifasAux.put(key, value);
		
	}
	/**
	 * Get del mapa de PoolXTarifas paciente auxiliar
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaPoolXTarifasAux(String key){
		return mapaPoolXTarifasAux.get(key);
	}
   /**
     * @return Returns the codigoEsquemaTarifario.
     */
    public int getCodigoEsquemaTarifario() {
        return codigoEsquemaTarifario;
    }
    /**
     * @param codigoEsquemaTarifario The codigoEsquemaTarifario to set.
     */
    public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
        this.codigoEsquemaTarifario = codigoEsquemaTarifario;
    }
    /**
     * @return Returns the codigoPool.
     */
    public int getCodigoPool() {
        return codigoPool;
    }
    /**
     * @param codigoPool The codigoPool to set.
     */
    public void setCodigoPool(int codigoPool) {
        this.codigoPool = codigoPool;
    }
    /**
     * @return Returns the cuentaInstitucionString.
     */
    public String getCuentaInstitucionString() {
        return cuentaInstitucionString;
    }
    /**
     * @param cuentaInstitucionString The cuentaInstitucionString to set.
     */
    public void setCuentaInstitucionString(String cuentaInstitucionString) {
        this.cuentaInstitucionString = cuentaInstitucionString;
    }
    /**
     * @return Returns the cuentaPoolString.
     */
    public String getCuentaPoolString() {
        return cuentaPoolString;
    }
    /**
     * @param cuentaPoolString The cuentaPoolString to set.
     */
    public void setCuentaPoolString(String cuentaPoolString) {
        this.cuentaPoolString = cuentaPoolString;
    }
    /**
     * @return Returns the descripcionPool.
     */
    public String getDescripcionPool() {
        return descripcionPool;
    }
    /**
     * @param descripcionPool The descripcionPool to set.
     */
    public void setDescripcionPool(String descripcionPool) {
        this.descripcionPool = descripcionPool;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Returns the nombreEsquemaTarifario.
     */
    public String getNombreEsquemaTarifario() {
        return nombreEsquemaTarifario;
    }
    /**
     * @param nombreEsquemaTarifario The nombreEsquemaTarifario to set.
     */
    public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
        this.nombreEsquemaTarifario = nombreEsquemaTarifario;
    }
    /**
     * @return Returns the porcentajeParticipacionString.
     */
    public String getPorcentajeParticipacionString() {
        return porcentajeParticipacionString;
    }
    /**
     * @param porcentajeParticipacionString The porcentajeParticipacionString to set.
     */
    public void setPorcentajeParticipacionString(
            String porcentajeParticipacionString) {
        this.porcentajeParticipacionString = porcentajeParticipacionString;
    }
	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	/**
	 * Tamanio de la Col
	 * @return
	 */
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i)	{
		offset = i;
	}
    /**
     * @return Returns the linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente The linkSiguiente to set.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
    /**
     * @return Returns the mapaPoolXTarifas.
     */
    public HashMap getMapaPoolXTarifas() {
        return mapaPoolXTarifas;
    }
    /**
     * @param mapaPoolXTarifas The mapaPoolXTarifas to set.
     */
    public void setMapaPoolXTarifas(HashMap mapaPoolXTarifas) {
        this.mapaPoolXTarifas = mapaPoolXTarifas;
    }
    /**
     * @return Returns the mapaPoolXTarifasNoModificado.
     */
    public HashMap getMapaPoolXTarifasNoModificado() {
        return mapaPoolXTarifasNoModificado;
    }
    /**
     * @param mapaPoolXTarifasNoModificado The mapaPoolXTarifasNoModificado to set.
     */
    public void setMapaPoolXTarifasNoModificado(
            HashMap mapaPoolXTarifasNoModificado) {
        this.mapaPoolXTarifasNoModificado = mapaPoolXTarifasNoModificado;
    }
    /**
     * @return Returns the offsetHash.
     */
    public int getOffsetHash() {
        return offsetHash;
    }
    /**
     * @param offsetHash The offsetHash to set.
     */
    public void setOffsetHash(int offsetHash) {
        this.offsetHash = offsetHash;
    }
    /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa()
    {
    	if(numeroRealFilasMapa==0);//Evitar el warning
      this.numeroRealFilasMapa = mapaPoolXTarifas.size()/tamanioNumeroColumnas;
    }
    /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa(int numeroRealFilasMapa) {
        this.numeroRealFilasMapa = numeroRealFilasMapa;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapa(){
        //return numeroRealFilasMapa=mapaPoolXTarifas.size()/tamanioNumeroColumnas;
    	return Integer.parseInt(mapaPoolXTarifas.get("numRegistros").toString());
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaNoMod(){
        return mapaPoolXTarifasNoModificado.size()/tamanioNumeroColumnas;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaAux(){
        return mapaPoolXTarifasAux.size()/6;
    }
    /**
     * @return Returns the indexMapa.
     */
    public int getIndexMapa() {
        return indexMapa;
    }
    /**
     * @param indexMapa The indexMapa to set.
     */
    public void setIndexMapa(int indexMapa) {
        this.indexMapa = indexMapa;
    }
    /**
     * @return Returns the mapaPoolXTarifasAux.
     */
    public HashMap getMapaPoolXTarifasAux() {
        return mapaPoolXTarifasAux;
    }
    /**
     * @param mapaPoolXTarifasAux The mapaPoolXTarifasAux to set.
     */
    public void setMapaPoolXTarifasAux(HashMap mapaPoolXTarifasAux) {
        this.mapaPoolXTarifasAux = mapaPoolXTarifasAux;
    }
	/**
	 * @return the cuentaInstitucionStringAnterior
	 */
	public String getCuentaInstitucionStringAnterior() {
		return cuentaInstitucionStringAnterior;
	}
	/**
	 * @param cuentaInstitucionStringAnterior the cuentaInstitucionStringAnterior to set
	 */
	public void setCuentaInstitucionStringAnterior(
			String cuentaInstitucionStringAnterior) {
		this.cuentaInstitucionStringAnterior = cuentaInstitucionStringAnterior;
	}
	/**
	 * @return the tipoParame
	 */
	public String getTipoParame() {
		return tipoParame;
	}
	/**
	 * @param tipoParame the tipoParame to set
	 */
	public void setTipoParame(String tipoParame) {
		this.tipoParame = tipoParame;
	}

	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}
	
	
	public void initConvenio (Connection connection)
	{
		this.setConvenios(Utilidades.obtenerConvenios(connection, "", "", false, "", false));
		this.descripcionPool="";
		this.codigoConvenio="";
		this.poolesXConvenio = new HashMap ();
		this.setPoolesXConvenio("numRegistros", "0");
		this.linkSiguiente="";
		this.offsetHash=0;
		this.numeroRealFilasMapa=0;
	}

	public HashMap getPoolesXConvenio() {
		return poolesXConvenio;
	}
	
	public Object getPoolesXConvenio(String key) {
		return this.poolesXConvenio.get(key);
	}

	public void setPoolesXConvenio(HashMap poolesXConvenio) {
		this.poolesXConvenio = poolesXConvenio;
	}
	
	public void setPoolesXConvenio(String key,Object value) {
		this.poolesXConvenio.put(key, value);
	}

	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public void setMaxPagesItemsHash(int maxPagesItemsHash) {
		this.maxPagesItemsHash = maxPagesItemsHash;
	}

	public void setValorParticipacion(double valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}

	public double getValorParticipacion() {
		return valorParticipacion;
	}
} 
