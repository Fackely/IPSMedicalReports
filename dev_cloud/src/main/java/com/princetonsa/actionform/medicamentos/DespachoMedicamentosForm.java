
/*
 * @(#)DespachoMedicamentosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.medicamentos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.CentrosCostoViaIngreso;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.inventarios.EquivalentesDeInventario;
import com.princetonsa.mundo.manejoPaciente.Habitaciones;
import com.princetonsa.mundo.manejoPaciente.Pisos;

/**
 * Form que contiene todos los datos específicos para generar 
 * el Despacho de medicamentos
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Agosto 31, 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
@SuppressWarnings("all")
public class DespachoMedicamentosForm extends ValidatorForm
{
	/*-----------------------------------------------
	 * 	ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	
	/**
	 * String Patron Ordenar 
	 */
	
	
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 **/
	private String ultimoPatron;
	
	/**
     * Para controlar la página actual del pager.
     */
    //private int offset;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    //private String linkSiguiente;
    
    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber;	
	
	/*-----------------------------------------------
	 * 	FIN ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(DespachoMedicamentosForm.class);
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Número de la solicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * Fecha de la solicitud
	 */
	private String fechaSolicitud;

	/**
	 * Hora de solicitud
	 */
	private String horaSoliciutd;
	
	/**
	 * 
	 */
	private String cantidadeq;
	
	/**
	 * medico que solicita
	 */
	private String medicoSolicitante;
	
	/**
	 * número de autorización
	 */
	//private String numeroAutorizacion;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	private String estadoListado;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=new ArrayList();
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	/**
	 * Cantidad parcial del despacho que se ha realizado en la farmacia
	 */
	private int despachoParcial;
	
	/**
	 * codigo del paciente para cargarlo en session
	 */
	private int codigoPaciente;
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";
	
	/**
	 * Código del Artículo
	 */
	private int codigoArticulo;
	
	/**
	 * Código del artículo sustituto
	 */
	private int codigoArticuloSustituto;
	
	/**
	 * Almacena  descripcion del articulo  
	 */
	private String articulo;	
	
	/**
	 * Alamacena el codigo de la orden ambulatoria si tiene asociada la solicitud
	 */
	private String ordenAmb;
	
	/**
	 * Unidad de Medida del Articulo.
	 */
	private String unidadMedida;
	
	/**
	 * Contiene la suma total de los despachos.
	 */
	private int despachoTotal;
	
	/**
	 * Captura la cantidad a despachar.
	 */
	private int despacho;
	
	/**
	 * Numero de Ingresos de Insumos
	 */
	private int numeroIngresos;
	
	/**
	 * Colección que contendrá el (numeroSustituto-numeroOriginal)
	 */
	private Collection coleccionNumerosSustitutos;
	
	/**
	 *  Contiene los articulos (Principales - Sustitutos) que ya han sido insertados
	 * en la BD para restringir la búsqueda en el tag
	 */
	private Collection coleccionArticulosInsertados;
	
	/**
	 * Contiene todos los insumos existentes en la BD.
	 */
	private Collection coleccionInsumos;
	/**
	 * Contiene los N valores capturados de despacho
	 */
	public  HashMap despachoMap = new HashMap();
	
	/**
	 * Contiene los códigos de los articulos
	 */
	private HashMap codigosArticulosMap = new HashMap();
	
	/**
	 * Almacena el estado Activo/Inactivo del RadioButton
	 * Entrega Diracta al Paciente
	 */
	private String checkDirecto ="off";
	
	/**
	 * Almacena el estado Activo/Inactivo del RadioButton
	 * Despacho Pendiente
	 */
	private String radioPendiente;
	
	/**
	 * Almacena el estado Activo/Inactivo del RadioButton
	 * Despacho Final
	 */
	private String radioFinal;
	
	/**
	 * Código del artículo que va ha ser sustituto
	 */
	private int numeroSustituto;
	
	/**
	 * Código del artículo que es original
	 */
	private int numeroOriginal;

	/**
	 *	Verificar si hay Despacho Total para medicamentos  
	 */
	private int esVacio;
	
	/**
	 *	Verificar si hay Despacho Total para insumos  
	 */
	private int esVacio1;
	
	/**
	 * Estado médico de la solicitud
	 */
	private String estadoMedico;
	
	/**
	 * Observaciones Generales de la solicitud de medicamentos
	 */
	private String observacionesGenerales = "";

	/**
	 * login del usuario
	 */
	private String usuario;

	/**
	 * Contiene el tamaño de la collection con
	 * los medicamentos que ya han sido almacenados 
	 * en la BD
	 */
	private int tamanoCollectionOriginal;
	
	/**
	 * determinar si termino el submit desde el popup 
	 * busquedaSustitutos
	 */
	private static boolean termino;
	
	/**
	 * Consecutivo de orden médica
	 */
	private int orden;
	
    /**
     * nombre del centro de costo solicitado
     */
    private String nombreCentroCostoSolicitado;
    
    /**
     * codigo del centro de costo solicitado
     */
    private int codigoCentroCostoSolicitado;
    
    /**
     * mapa que contiene los mensajes de advertencia tanto para 
     * articulos con existencias negativas, articulos que no cumplen con las condiciones de 
     * stock maximo - stock minimo y punto de pedido
     */
    private HashMap mensajesAdvertenciaMap= new HashMap();
    
    /**
     * obtiene la oreen medica de la mezcla
     */
    private String mezclaOrdenDieta;
    
    /**
     * tipoListado
     */
    private String tipoListado;
    
    /**
	 * Centro de costo seleccionado 
	 * para el filtro del listado de las 
	 * solicitudes
	 */
	private int codigoCentroCosto;
	
	/**
	 * codigo del centro de costo solicitante de la solicitud a la que se esta haciendo el despacho.
	 */
	private int codigoCentroCostoSolicitante;
    
	/**
	 * 
	 */
	private String nombrePersonaRecibe;
	
	/**
	 * 
	 */
	private boolean interfazCompras;
	
	/**
	 * 
	 */
	private boolean esDespachoParcial;
	
	/**
	 * 
	 */
	private boolean esOrdenAmb;
	
	
	public boolean isEsDespachoParcial() {
		return esDespachoParcial;
	}

	public boolean isEsOrdenAmb() {
		return esOrdenAmb;
	}

	public void setEsOrdenAmb(boolean esOrdenAmb) {
		this.esOrdenAmb = esOrdenAmb;
	}

	public void setEsDespachoParcial(boolean esDespachoParcial) {
		this.esDespachoParcial = esDespachoParcial;
	}

	public String getOrdenAmb() {
		return ordenAmb;
	}

	public void setOrdenAmb(String ordenAmb) {
		this.ordenAmb = ordenAmb;
	}
		
	/**
	 * 
	 */
	private HashMap<String, Object> almacenesConsignacion;
	
	/**
	 * 
	 */
	private HashMap<String, Object> conveniosProveedor;
	
	/**
	 * 
	 */
	private HashMap<String, Object> proveedorCatalogo;
	
	/**
	 * 
	 */
	private HashMap<String, Object> equivalentesMap;
	
	/**
	 * 
	 */
	private int areaFiltro;
	
	/**
	 * 
	 */
	private int pisoFiltro;
	
	/**
	 * 
	 */
	private int habitacionFiltro;
	
	
	/**
	 * 
	 */
	private int camaFiltro;
	
	/**
	 * 
	 */
	private HashMap <String, Object> areasTagMap; 
	
	/**
	 * 
	 */
	private HashMap<String, Object> pisosTagMap;
	
	/**
	 * 
	 */
	private HashMap<String, Object> habitacionesTagMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> camasTagMap;
	
	/**
	 * 
	 */
	private boolean entidadControlaDespachoSaldoMultidosis;
	
	/**
	 * 
	 */
	private HashMap<String,String> mapaSustitutosBD;
	
	/**
	 * 
	 */
	private int posColeccion;
	
	
	
	/** * Código de la Autorización que es cargada desde otra funcionalidad  */
	private long codigoAutorizacionEntidadSubcontratada = ConstantesBD.codigoNuncaValidoLong;
	
	/** * Código del despacho guardado */
	private int codigoDespacho = ConstantesBD.codigoNuncaValido;
	
	/** * Contiene un listado de advertencias a mostrar */
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	/** * Indica si la solicitud no tiene ninguna Autorización de entidad sub */
	private boolean tieneAutorizacionEntidadSub = false;
	
	/**
	 * bandera indica maneja existencias negativas para
	 * medicamentos con lotes
	 */
	private String manejaExistenciasNegativas;
	
	
	
    /**
	 * Set del mapa de despacho
	 * @param key
	 * @param value
	 */

	public void setDespachoMap(String key, Object value) 
	{
		despachoMap.put(key, value);
	}

	/**
	 * Get del mapa de despacho
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getDespachoMap(String key) 
	{
		return despachoMap.get(key);
	}

	/**
     * resetea los mensajes de advertencia
     * en el resumen
     *
     */
    public void resetMensajes()
    {
        this.mensajesAdvertenciaMap= new HashMap();
        this.listaAdvertencias = new ArrayList<String>();
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
		
		if(estado.equals("agregar"))
        {
           if(getNumeroIngresos() > 0)
           {
		        
               for(int k=0; k < getNumeroIngresos(); k++)
               {
		           String [] dato= String.valueOf(getDespachoMap("articulo_"+k)).split("-");
		          
	               if(getCodigoArticulo() == Integer.parseInt(dato[0]))
	               {
	            	   String codArticuloStr=UtilidadTexto.isEmpty(getDespachoMap("codigointerfaz_"+k)+"")?"":getDespachoMap("codigointerfaz_"+k)+"-";
	            	   codArticuloStr+=getDespachoMap("articulo_"+k)+"";
	            	   errores.add("campo existente", new ActionMessage("errors.existeCampo",codArticuloStr));
	               }
               }
           }
        }
		
		if(estado.equals("salirGuardar"))
		{
			boolean desFinalTemporalMedicamentos=false;
			boolean desFinalTemporalInsumos=false;
			logger.info("RADIO FINAL --> "+this.radioFinal);
			logger.info("RADIO PENDIENTE --> "+this.radioPendiente);
			
			//hacer la validacion con el cierre de inventarios
            UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
            
		    //UtilidadValidacion.validarNumeroAutorizacion(errores,numeroAutorizacion);//validar que el número de autorización este entre [0...9||a...z||A...Z||-]
			
			if(   ( this.radioFinal==null || this.radioFinal.equals("")) && (this.radioPendiente==null || this.radioPendiente.equals("")) )
			{
				errores.add("", new ActionMessage("errors.required","El campo <Despacho Pendiente> ó <Despacho Final> "));
			}
            if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt()))
                errores.add("Fecha", new ActionMessage("error.inventarios.existeCierreInventarios", UtilidadFecha.getFechaActual()));
            else
            {    
            	desFinalTemporalInsumos=true;
				for(int k=0; k < getNumeroIngresos(); k++)
				{
					String despachoInsumos=String.valueOf(getDespachoMap("despacho_"+k));
					String cantidadInsumos=(String)getDespachoMap("cantidadInsumo_"+k);
					String despachoTotalInsumos=(String)getDespachoMap("despachoTotal_"+k);
					String codArticuloMasInterfaz=	UtilidadTexto.isEmpty(getDespachoMap("codigointerfaz_"+k)+"")?"":getDespachoMap("codigointerfaz_"+k)+"-";
					codArticuloMasInterfaz+=getDespachoMap("articulo_"+k)+"";
					
                    if(cantidadInsumos==null)
					{
						cantidadInsumos="";
					}
					cantidadInsumos=cantidadInsumos.trim();
					int despachoInsumosInt=0;

					Boolean nuevo=(Boolean)getDespachoMap("nuevo_"+k);
					if(nuevo!=null && nuevo.booleanValue())
					{
						if(despachoInsumos!=null && !despachoInsumos.trim().equals(""))
						{
							despachoInsumosInt=Integer.parseInt(despachoInsumos.trim());
							if(despachoInsumosInt>0)
							{
								cantidadInsumos=(despachoInsumosInt+Integer.parseInt(despachoTotalInsumos))+"";
							}
						}
						else
						{
							cantidadInsumos="0";
						}
						setDespachoMap("cantidadInsumo_"+k, cantidadInsumos);
						/*
						 * En este caso se insertó un nuevo insumo entonces
						 * es requerido el despacho
						 */
						if(despachoInsumos==null || despachoInsumos.trim().equals("") || despachoInsumos.trim().equals("null"))
						{
							errores.add("despachoInsumo_"+k, new ActionMessage("error.despachoMed.despachoInsumoRequerido", codArticuloMasInterfaz));
						}
					}
					if(cantidadInsumos.equals("") && !despachoInsumos.trim().equals("null"))
					{
						try
						{
							despachoInsumosInt=Integer.parseInt(despachoInsumos.trim());
							if(despachoInsumosInt>0)
							{
								errores.add("despachoInsumo_"+k, new ActionMessage("error.administracionMed.requeridaCantidadSiDespacho"));
							}
						}
						catch (NumberFormatException e)
						{
							errores.add("despachoInsumo_"+k, new ActionMessage("errors.integerMayorIgualQue", "El despacho de Insumos","0"));
						}
					}
					if(!cantidadInsumos.equals("") && !despachoInsumos.trim().equals("null"))
					{
						int cantidadInt=0;
						try
						{
							cantidadInt=Integer.parseInt(cantidadInsumos);
						}
						catch(NumberFormatException e)
						{
							errores.add("cantidadInsumos_"+k, new ActionMessage("errors.integerMayorIgualQue", "La cantidad del Insumo","0"));
						}
						try
						{
							
							int sumaDespacho=Integer.parseInt(despachoInsumos)+Integer.parseInt(despachoTotalInsumos);
							if(sumaDespacho>cantidadInt)
							{
								errores.add("cantidadInsumo_"+k, new ActionMessage("error.despachoMed.excedeCantFormulada", codArticuloMasInterfaz));
								
							}
							if(sumaDespacho!=cantidadInt)
							{
								desFinalTemporalInsumos=false;
							}
						}
						catch(NumberFormatException e)
						{
							// aqui no hago nada, ya adicioné este error
			    		}
					}
				}
				
				//se valida que se indique la informacion de los lotes para los articulos que tengan un despacho mayor que cero y que tengan manejo d lotes
                if(errores.isEmpty())
                {
                	Connection con= UtilidadBD.abrirConexion();
        			for(int i=0; i<getNumeroIngresos(); i++)
        			{
        				String despachoInsumos=String.valueOf(getDespachoMap("despacho_"+i));
        				String codArticuloMasInterfaz=	UtilidadTexto.isEmpty(getDespachoMap("codigointerfaz_"+i)+"")?"":getDespachoMap("codigointerfaz_"+i)+"-";
    					codArticuloMasInterfaz+=getDespachoMap("articulo_"+i)+"";
    					
        				
        				
                        //validaciones de la existencia
                        if(Integer.parseInt(despachoInsumos)>0)
                        {
        				    if(Articulo.articuloManejaLote(con, Integer.parseInt(getDespachoMap("articulo_"+i).toString().split("-")[0]), usuario.getCodigoInstitucionInt()))
        					{
        						//como maneja lote entonces la info no puede ser vacia
        						if(UtilidadTexto.isEmpty(this.getDespachoMap("loteInsumo_"+i).toString()))
        						{
        							errores.add("", new ActionMessage("errors.required","La información de Lote para el Artículo "+codArticuloMasInterfaz));
        						}
        					}
        			    }
        			}
        			UtilidadBD.closeConnection(con);
                }
                
                //se valida la informacion de la interfaz inventarios - shaio para insumos
                if(errores.isEmpty())
                {
                	for(int k=0; k<getNumeroIngresos(); k++)
        			{
		                //String codigoArticulo= getDespachoMap("articulo_"+k).toString().split("-")[0];
		                String codArticuloMasInterfaz=	UtilidadTexto.isEmpty(getDespachoMap("codigointerfaz_"+k)+"")?"":getDespachoMap("codigointerfaz_"+k)+"-";
    					codArticuloMasInterfaz+= getDespachoMap("articulo_"+k).toString().split("-")[0];
    					
                		boolean valExistConsig=false;
				    	
			    		//si se tiene interfaz hacer la siguiente validacion
			    		if(interfazCompras&&despachoMap.containsKey("tipodespachoInsumo_"+k))
			    		{
			    			if((despachoMap.get("tipodespachoInsumo_"+k)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
			    			{
			    				if(!despachoMap.containsKey("almacenConsignacionInsumo_"+k)||UtilidadTexto.isEmpty(despachoMap.get("almacenConsignacionInsumo_"+k)+""))
			    				{
			    					errores.add("falta campo", new ActionMessage("errors.required","El almacen de consignacion del Articulo "+codArticuloMasInterfaz));
			    				}
			    				else
			    				{
			    					valExistConsig=true;
			    				}
			    				if(!despachoMap.containsKey("proveedorCompraInsumo_"+k)||UtilidadTexto.isEmpty(despachoMap.get("proveedorCompraInsumo_"+k)+""))
			    				{
			    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codArticuloMasInterfaz));
			    				}
			    			}
			    			else if((despachoMap.get("tipodespachoInsumo_"+k)+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
			    			{
			    				if(!despachoMap.containsKey("proveedorCatalogoInsumo_"+k)||UtilidadTexto.isEmpty(despachoMap.get("proveedorCatalogoInsumo_"+k)+""))
			    				{
			    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codArticuloMasInterfaz));
			    				}
			    			}
			    		}
			    		
				        if(valExistConsig)
				        {
				        	int exArticulo=Utilidades.convertirAEntero(UtilidadInventarios.getExistenciasXArticulo(Utilidades.convertirAEntero(getDespachoMap("articulo_"+k).toString().split("-")[0]), Utilidades.convertirAEntero(despachoMap.get("almacenConsignacionInsumo_"+k)+""), usuario.getCodigoInstitucionInt()));
				        	int cantidadDespachada= Integer.parseInt(getDespachoMap("despacho_"+k).toString());
				        	if(cantidadDespachada>exArticulo)
				        	{
				        		errores.add("error.inventarios.existenciasInsuficientes",new ActionMessage("error.inventarios.existenciasInsuficientes",codArticuloMasInterfaz,exArticulo+"",Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(despachoMap.get("almacenConsignacionInsumo_"+k)+""), usuario.getCodigoInstitucionInt())));
				        	}
				        }
				        else
				        {
				        	Connection con= UtilidadBD.abrirConexion();
							String despachoInsumos=String.valueOf(getDespachoMap("despacho_"+k));
	                        
	                        if(Integer.parseInt(despachoInsumos)>0)
	                        {
	                            //validaciones de la existencia
	                        	if(!AlmacenParametros.manejaExistenciasNegativas(this.getCodigoCentroCostoSolicitado(), usuario.getCodigoInstitucionInt()))
	                            {
	                            	if(!Articulo.articuloManejaLote(con, Integer.parseInt(getDespachoMap("articulo_"+k).toString().split("-")[0]), usuario.getCodigoInstitucionInt()))
				                	{
		                            	//parte de medicamentos
		                                String existenciasStr=UtilidadInventarios.getExistenciasXArticulo(Integer.parseInt(String.valueOf(getDespachoMap("articulo_"+k)).split("-")[0]), this.getCodigoCentroCostoSolicitado(), usuario.getCodigoInstitucionInt())+"";
		                                if(existenciasStr.equals("null"))
		                                {
		                                    errores.add("", new ActionMessage("errors.notEspecific", "Error de código al calcular las existencias"));
		                                }
		                                else
		                                {    
		                                    if(Integer.parseInt(despachoInsumos)>Integer.parseInt(existenciasStr))
		                                    {
		                                        errores.add("error.inventarios.existenciasInsuficientes", 
		                                                new ActionMessage("error.inventarios.existenciasInsuficientes", 
		                                                                            String.valueOf(getDespachoMap("articulo_"+k)).split("-")[0], 
		                                                                            existenciasStr , 
		                                                                            this.getNombreCentroCostoSolicitado() ));
		                                    }
		                                }
				                	}
	                            	else
	                            	{
	                            		if(Integer.parseInt(despachoInsumos)>Integer.parseInt(this.getDespachoMap("existenciaXLoteInsumo_"+k)+""))
					                    {
					                        errores.add("error.inventarios.existenciasInsuficientesLote", 
					                                new ActionMessage("error.inventarios.existenciasInsuficientesLote", 
					                                							String.valueOf(getDespachoMap("articulo_"+k)).split("-")[0],
					                                							this.getDespachoMap("existenciaXLoteInsumo_"+k).toString(),
					                                							this.getNombreCentroCostoSolicitado(),
					                                							this.getDespachoMap("loteInsumo_"+k).toString()));
					                    }
	                            	}
	                            }
	                        } //if despacho mayor cero
	                        UtilidadBD.closeConnection(con);
		                }
					}    
                }
                
                
                if(errores.isEmpty())
				{
					Connection con= UtilidadBD.abrirConexion();
					////////////validaciones existencias para los insumos
                    for(int k=0; k < getNumeroIngresos(); k++)
                    {
                    	
                    }//for exist
                    UtilidadBD.closeConnection(con);
				}  
                
                if(getColSize() > 0)
                {    
                	HashMap<String, String> cantidadesTemporales=new HashMap<String, String>();
                	desFinalTemporalMedicamentos=true;
                    for(int k=0; k < getColSize(); k++)
                    {
    					String despachoParcial=String.valueOf(getDespachoMap("parcial_"+(k+1)));
    					String codigoArticulo= codigosArticulosMap.get("articulo_"+(k+1)).toString().split("-")[0];
    					
    					String codArticuloMasInterfaz=  UtilidadInventarios.obtenerCodigoInterfazArticulo(Utilidades.convertirAEntero(codigoArticulo))+" - "+codigoArticulo;
    					
    					
    					if(UtilidadTexto.isEmpty(mezclaOrdenDieta))
						{
	    					String cantidad="";
	    					try
							{
	    						cantidad=((String)getDespachoMap("cantidad_"+k));
							}
	    					catch(NullPointerException ne)
							{
	    						cantidad="";
							}
	    					String despachoTotal= String.valueOf(getDespachoMap("despacho_total_"+k));
	    					String esSustituto=(String)getDespachoMap("sustituto_"+k);
	                        
	    					if(cantidad==null  || cantidad.equals("null"))
	    					{
	    						cantidad="";
	    					}
	    					cantidad=cantidad.trim();
	    					int despachoParcialInt=0;
	    					if(cantidad.equals("") && !despachoParcial.trim().equals("null"))
	    					{
	    						try
	    						{
	    							despachoParcialInt=Integer.parseInt(despachoParcial.trim());
	    							if(despachoParcialInt>0 && esSustituto==null)
	    							{
	    								errores.add("despacho_"+k, new ActionMessage("error.administracionMed.requeridaCantidadSiDespacho"));
	    							}
	    						}
	    						catch (NumberFormatException e)
	    						{
	    							errores.add("despacho_"+k, new ActionMessage("errors.integerMayorIgualQue", "El despacho del Medicamento","0"));
	    						}
	    					}
	    					
	    					if(!cantidad.equals("") && !despachoParcial.trim().equals("null") )//&& esSustituto==null)
		    				{
	    						try
	    						{
	    							//////////////////////////--------------
	    							int cantidadInt=Integer.parseInt(cantidad);
	    							double dosisDespachada=Utilidades.convertirADouble(getDespachoMap("totaldosisdespachada_"+(k+1))+"",true)+Utilidades.convertirADouble(getDespachoMap("saldodosisdespachada_"+(k+1))+"",true);
	    							int unidadesDosisTemp=0;
	    							if(dosisDespachada>0&&Utilidades.convertirADouble(getDespachoMap("cantidadunidosis_"+(k+1))+"")>0)
	    							{
	    								unidadesDosisTemp=UtilidadTexto.aproximarAnteriorUnidad(""+(dosisDespachada/(Utilidades.convertirADouble(getDespachoMap("cantidadunidosis_"+(k+1))+""))));
	    							}
	    							
	    							// ****** Calcular Cantidad Equivalente para los articulos que aplique
	    							Connection con = UtilidadBD.abrirConexion();
	    							int cantidadArtEquivalente = 0;
	    							Iterator ite = this.getCol().iterator();
	    							posColeccion = 0;
	    							while (ite.hasNext()) {
	    								int codAComparar = ConstantesBD.codigoNuncaValido;
		    							int codEquivalente = ConstantesBD.codigoNuncaValido;
	    								HashMap colMap = (HashMap) ite.next();
	    								if(colMap.get("codigosustitutoprincipal").toString().contains("@")){
	    									codEquivalente = Utilidades.convertirAEntero(colMap.get("codigosustitutoprincipal").toString().split("@")[0]); 
	    									codAComparar = Utilidades.convertirAEntero(colMap.get("codigosustitutoprincipal").toString().split("@")[1]);
	    									if(Utilidades.convertirAEntero(codigoArticulo) == codAComparar){
	    										HashMap mapa = EquivalentesDeInventario.consultarArticulo(con, codAComparar, codEquivalente);
	    										cantidadArtEquivalente += Utilidades.convertirAEntero(colMap.get("despacho_total")+"") / Utilidades.convertirAEntero(mapa.get("cantidadEquivalente")+"");
	    									}                                                                        
	    								}
	    							}
	    							UtilidadBD.closeConnection(con);
	    							logger.info("\n Cantidad Equivalente: "+cantidadArtEquivalente);
	    							Utilidades.imprimirMapa(getDespachoMap());
	    							Utilidades.imprimirMapa(getCodigosArticulosMap());
	    							// ******
	    							
	    							
	    							int sumaDespacho=Integer.parseInt(despachoParcial)+Integer.parseInt(despachoTotal)+unidadesDosisTemp;//+cantidadArtEquivalente;
	    							
	    							if(sumaDespacho>cantidadInt)
	    							{
	    								//si es mezcla no se valida
	    								errores.add("cantidad_"+k, new ActionMessage("error.despachoMed.excedeCantFormulada", codArticuloMasInterfaz));
	    							}
	    							//PONER EN UNA VARIABLE TEMPORAL EL ACUMULADO DE CANTIDADES POR ARTICULO PRINCIPAL.
	    	    					//articuloprincipal_
	    							String codArtTempo=codigoArticulo+"";
	    							double sumaDespaTemporal=sumaDespacho;
	    							if(esSustituto!=null)
	    	    					{
	    								codArtTempo=(String)getDespachoMap("articuloprincipal_"+k);
	    								sumaDespaTemporal=sumaDespaTemporal/Utilidades.convertirAEntero((String)getDespachoMap("cantidadeq_"+k));
	    								
	    	    					}
	    	    					if(cantidadesTemporales.containsKey(codArtTempo+""))
	    	    					{
	    	    						double canTempo=Utilidades.convertirADouble(cantidadesTemporales.get(codArtTempo+"")+"")+sumaDespaTemporal;
	    	    						cantidadesTemporales.put(codArtTempo+"", canTempo+"");
	    	    					}
	    	    					else
	    	    					{
	    	    						cantidadesTemporales.put(codArtTempo+"", sumaDespaTemporal+"");
	    	    					}
	    	    					double cantidadComparar=cantidadInt;
	    	    					if(esSustituto!=null)
	    	    					{
	    	    						cantidadComparar=cantidadComparar/Utilidades.convertirAEntero((String)getDespachoMap("cantidadeq_"+k));
	    	    					}
	    	    					if( Utilidades.convertirADouble(cantidadesTemporales.get(codArtTempo+"")+"")>cantidadComparar)
	    							{
	    								//si es mezcla no se valida
	    								errores.add("cantidad_"+k, new ActionMessage("error.despachoMed.excedeCantFormulada", UtilidadInventarios.obtenerCodigoInterfazArticulo(Utilidades.convertirAEntero(codArtTempo))+" - "+codArtTempo));
	    							}
	    	    					
	    						}
	    						catch(NumberFormatException e)
	    						{
	    							errores.add("cantidad_"+k, new ActionMessage("errors.integer", "Cantidad"));
	    			    		}
	    						
	    					}
	    					double dosisDisponible=Utilidades.convertirADouble(getDespachoMap("saldodosisdisponible_"+(k+1))+"");
	    					double dosisDespachada=Utilidades.convertirADouble(getDespachoMap("saldodosisdespachada_"+(k+1))+"");
	    					if(dosisDespachada>dosisDisponible)
	    					{
//	    						si es mezcla no se valida
								errores.add("cantidad_"+k, new ActionMessage("error.despachoMed.excedeDosisDisponible"));
	    					}
	    					
	    					
						}
    					//caso de mezclas
    					else 
    					{
    						try
    						{
    							Integer.parseInt(despachoParcial.trim());
    						}
    						catch (NumberFormatException e)
    						{
    							errores.add("despacho_"+k, new ActionMessage("errors.integerMayorIgualQue", "El despacho del Medicamento","0"));
    						}
    					}
    				}
                    if(this.getRadioPendiente().equals("radioPendiente"))
					{
	                    for(int k=0; k < getColSize(); k++)
	                    {
	                    	String codigoArticulo= codigosArticulosMap.get("articulo_"+(k+1)).toString().split("-")[0];
	                    	String codArtTempo=codigoArticulo+"";
	                    	String esSustituto=(String)getDespachoMap("sustituto_"+k);
							if(esSustituto!=null)
	    					{
								codArtTempo=(String)getDespachoMap("articuloprincipal_"+k);
	    					}
							
							
							if(UtilidadTexto.isEmpty(mezclaOrdenDieta))
							{
		    					String cantidad="";
		    					try
								{
		    						cantidad=((String)getDespachoMap("cantidad_"+k));
								}
		    					catch(NullPointerException ne)
								{
		    						cantidad="";
								}
		    					if(cantidad==null  || cantidad.equals("null"))
		    					{
		    						cantidad="";
		    					}
		    					cantidad=cantidad.trim();
		    					int cantidadInt=Integer.parseInt(cantidad);
								double cantidadComparar=cantidadInt;
		    					if(esSustituto!=null)
		    					{
		    						cantidadComparar=cantidadComparar/Utilidades.convertirAEntero((String)getDespachoMap("cantidadeq_"+k));
		    					}
	    						if(desFinalTemporalMedicamentos)
	    						{
	    							if( Utilidades.convertirADouble(cantidadesTemporales.get(codArtTempo+"")+"")!=cantidadComparar)
	    							{
	    								desFinalTemporalMedicamentos=false;
	    							}
	    						}
							}
	                    }
					}
                    //se valida que se indique la informacion de los lotes para los articulos que tengan un despacho mayor que cero y que tengan manejo d lotes
                    if(errores.isEmpty())
                    {
                    	Connection con= UtilidadBD.abrirConexion();
            			for(int i=0; i<this.getColSize(); i++)
            			{
            				String despachoParcial=String.valueOf(getDespachoMap("parcial_"+(i+1)));
                            //validaciones de la existencia
                            if(Integer.parseInt(despachoParcial)>0)
                            {
            				    if(Articulo.articuloManejaLote(con, Integer.parseInt(codigosArticulosMap.get("articulo_"+(i+1)).toString()), usuario.getCodigoInstitucionInt()))
            					{
            						//como maneja lote entonces la info no puede ser vacia
            						if(UtilidadTexto.isEmpty(this.getDespachoMap("lote_"+(i+1)).toString()))
            						{
            							errores.add("", new ActionMessage("errors.required","La información de Lote para el Artículo "+codigosArticulosMap.get("articulo_"+(i+1)).toString()));
            						}
            					}
            			    }
            			}
            			UtilidadBD.closeConnection(con);
                    }
                    
                    // se valida la informacion de la interfaz inventarios - shaio para medicamentos
                    if(errores.isEmpty())
                    {
                    	for(int k=0; k<this.getColSize(); k++)
            			{
    		                String codigoArticulo= codigosArticulosMap.get("articulo_"+(k+1)).toString().split("-")[0];
    		                String codArticuloMasInterfaz=  UtilidadInventarios.obtenerCodigoInterfazArticulo(Utilidades.convertirAEntero(codigoArticulo))+" - "+codigoArticulo;
    		                boolean valExistConsig=false;
    				    	
    			    		//si se tiene interfaz hacer la siguiente validacion
    			    		if(interfazCompras&&despachoMap.containsKey("tipodespacho_"+(k+1)))
    			    		{
    			    			if((despachoMap.get("tipodespacho_"+(k+1))+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoConsignacion))
    			    			{
    			    				if(!despachoMap.containsKey("almacenConsignacion_"+(k+1))||UtilidadTexto.isEmpty(despachoMap.get("almacenConsignacion_"+(k+1))+""))
    			    				{
    			    					errores.add("falta campo", new ActionMessage("errors.required","El almacen de consignacion del Articulo "+codArticuloMasInterfaz));
    			    				}
    			    				else
    			    				{
    			    					valExistConsig=true;
    			    				}
    			    				if(!despachoMap.containsKey("proveedorCompra_"+(k+1))||UtilidadTexto.isEmpty(despachoMap.get("proveedorCompra_"+(k+1))+""))
    			    				{
    			    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codArticuloMasInterfaz));
    			    				}
    			    			}
    			    			else if((despachoMap.get("tipodespacho_"+(k+1))+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDespachoCompraProveedor))
    			    			{
    			    				if(!despachoMap.containsKey("proveedorCatalogo_"+(k+1))||UtilidadTexto.isEmpty(despachoMap.get("proveedorCatalogo_"+(k+1))+""))
    			    				{
    			    					errores.add("falta campo", new ActionMessage("errors.required","El Proveedor del Articulo "+codArticuloMasInterfaz));
    			    				}
    			    			}
    			    		}
    			    		
    				        if(valExistConsig)
    				        {
    				        	int exArticulo=Utilidades.convertirAEntero(UtilidadInventarios.getExistenciasXArticulo(Utilidades.convertirAEntero(codigoArticulo), Utilidades.convertirAEntero(despachoMap.get("almacenConsignacion_"+(k+1))+""), usuario.getCodigoInstitucionInt()));
    				        	int cantidadDespachada= Integer.parseInt(getDespachoMap("parcial_"+(k+1)).toString());
    				        	if(cantidadDespachada>exArticulo)
    				        	{
    				        		
    				        		errores.add("error.inventarios.existenciasInsuficientes",new ActionMessage("error.inventarios.existenciasInsuficientes",codArticuloMasInterfaz,exArticulo+"",Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(despachoMap.get("almacenConsignacion_"+(k+1))+""), usuario.getCodigoInstitucionInt())));
    				        	}
    				        }
    				        else
    				        {
    				        	Connection con= UtilidadBD.abrirConexion();
    		                    
	                            String despachoParcial=String.valueOf(getDespachoMap("parcial_"+(k+1)));
	                            //validaciones de la existencia
	                            if(Integer.parseInt(despachoParcial)>0)
	                            {
		                            if(!AlmacenParametros.manejaExistenciasNegativas(this.getCodigoCentroCostoSolicitado(), usuario.getCodigoInstitucionInt()))
		                            {
		                            	if(!Articulo.articuloManejaLote(con, Integer.parseInt(codigosArticulosMap.get("articulo_"+(k+1)).toString()), usuario.getCodigoInstitucionInt()))
					                	{
			                            	//parte de medicamentos
			                                String existenciasStr=UtilidadInventarios.getExistenciasXArticulo(Integer.parseInt(codigosArticulosMap.get("articulo_"+(k+1)).toString()), this.getCodigoCentroCostoSolicitado(), usuario.getCodigoInstitucionInt())+"";
			                                if(existenciasStr.equals("null"))
			                                {
			                                    errores.add("", new ActionMessage("errors.notEspecific", "Error de código al calcular las existencias"));
			                                }
			                                else
			                                {    
			                                    if(Integer.parseInt(despachoParcial)>Integer.parseInt(existenciasStr))
			                                    {
			                                        errores.add("error.inventarios.existenciasInsuficientes", 
			                                                new ActionMessage("error.inventarios.existenciasInsuficientes", 
			                                                                            codigosArticulosMap.get("articulo_"+(k+1)).toString(), 
			                                                                            existenciasStr , 
			                                                                            this.getNombreCentroCostoSolicitado() ));
			                                    }
			                                }
					                	}
		                            	else
					                	{
		                            		if(Integer.parseInt(despachoParcial)>Integer.parseInt(this.getDespachoMap("existenciaXLote_"+(k+1))+""))
						                    {
						                        errores.add("error.inventarios.existenciasInsuficientesLote", 
						                                new ActionMessage("error.inventarios.existenciasInsuficientesLote", 
						                                							codigosArticulosMap.get("articulo_"+(k+1)).toString(),
						                                							this.getDespachoMap("existenciaXLote_"+(k+1)).toString(),
						                                							this.getNombreCentroCostoSolicitado(),
						                                							this.getDespachoMap("lote_"+(k+1)).toString()));
						                    }
					                	}
		                            }
		                        }   
    	                        UtilidadBD.closeConnection(con);
    				        }
    					}    
                    }
               }//if getcolsize > 0
            }// else ->no existe cierre inventarios
            if(this.getRadioPendiente().equals("radioPendiente")&&desFinalTemporalInsumos&&desFinalTemporalMedicamentos)
			{
				this.setRadioFinal("radioFinal");
				this.setRadioPendiente("");
			}
		}//estado --> salirGuardar
		return errores;
	}
	
	
	
	
	public void reset(int codigoInstitucion, int codigoCentroAtencion)
	{
		this.numeroSolicitud=0;
		this.fechaSolicitud="";
		this.horaSoliciutd="";
		this.medicoSolicitante="";
		//this.numeroAutorizacion="";
		this.despachoParcial=-1;
		this.codigoPaciente=0;
		this.linkSiguiente="";
		this.codigoArticuloSustituto=-1;
		this.numeroSustituto=-1;
		this.numeroOriginal=-1;
		this.esVacio=0;
		this.esVacio1=0;
		coleccionNumerosSustitutos=new ArrayList();
		this.codigoArticulo=0;
		this.unidadMedida="";
		this.despacho=0;
		this.despachoTotal=0;
		this.numeroIngresos=0;
		this.articulo="";
		this.estadoMedico="";
		this.observacionesGenerales="";
		this.usuario="";
		this.tamanoCollectionOriginal=0;
		this.despachoMap=new HashMap();
		this.codigosArticulosMap= new HashMap();
		termino=false;
		this.esDespachoParcial=false;
		this.esOrdenAmb=false;
		this.radioFinal="";
		this.radioPendiente=""; 
		this.checkDirecto="off";
		this.orden=0;
        this.nombreCentroCostoSolicitado="";
        this.codigoCentroCostoSolicitado=-1;
        this.mezclaOrdenDieta="";
        this.tipoListado="";
        this.codigoCentroCosto=-1;
        
        this.nombrePersonaRecibe="";
        
        ///////////////////XPLANNER 2008 -----> 69470, SEGUN LA TAREA ESTO NO APLICA, CUANDO DIGAN LO CONTRARIO SOLO ES SETEAR EL PARAMETRO////////
        this.interfazCompras=false; //UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(codigoInstitucion));
        this.almacenesConsignacion=UtilidadInventarios.obtenerAlmacenesConsignacion(codigoInstitucion);
		this.conveniosProveedor=UtilidadInventarios.obtenerConveniosProveedor(codigoInstitucion);
		this.proveedorCatalogo=UtilidadInventarios.obtenerProveedoresCatalogo(codigoInstitucion);
		
		this.areaFiltro=ConstantesBD.codigoNuncaValido;
		this.pisoFiltro=ConstantesBD.codigoNuncaValido;
		this.habitacionFiltro=ConstantesBD.codigoNuncaValido;;
		this.camaFiltro= ConstantesBD.codigoNuncaValido;
		
		inicializarTagsMap(codigoInstitucion, codigoCentroAtencion);
		
		this.entidadControlaDespachoSaldoMultidosis=UtilidadTexto.getBoolean(ValoresPorDefecto.getEntidadControlaDespachoSaldosMultidosis(codigoInstitucion));
		this.codigoCentroCostoSolicitante=ConstantesBD.codigoNuncaValido;
		this.mapaSustitutosBD=new HashMap();
		
		this.posColeccion=ConstantesBD.codigoNuncaValido;
		this.equivalentesMap = new HashMap<String, Object>();
		
		this.manejaExistenciasNegativas = ConstantesBD.acronimoSi;
	}	
	
	
	/**
	 * Método que se encarga de llenar los mapas existentes con su debida información.
	 * Estos mapas se estaban llenando en el resetn pero cuando se llama la funcionalidad desde otra parte 
	 * estos quedan nulos.
	 * 
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * 
	 * @author Cristhian Murillo
	 */
	public void inicializarForma(int codigoInstitucion, int codigoCentroAtencion)
	{
		this.almacenesConsignacion=UtilidadInventarios.obtenerAlmacenesConsignacion(codigoInstitucion);
		this.conveniosProveedor=UtilidadInventarios.obtenerConveniosProveedor(codigoInstitucion);
		this.proveedorCatalogo=UtilidadInventarios.obtenerProveedoresCatalogo(codigoInstitucion);
	
		this.fechaSolicitud="";
		this.horaSoliciutd="";
		
		this.despachoParcial=-1;
		this.linkSiguiente="";
		this.codigoArticuloSustituto=-1;
		this.numeroSustituto=-1;
		this.numeroOriginal=-1;
		this.esVacio=0;
		this.esVacio1=0;
		coleccionNumerosSustitutos=new ArrayList();
		this.tamanoCollectionOriginal=0;
		this.despachoMap=new HashMap();
		termino=false;
		this.radioFinal="";
		this.radioPendiente=""; 
		this.checkDirecto="off";
		this.esDespachoParcial=false;
		this.esOrdenAmb=false;
        //this.mezclaOrdenDieta="";//Se quita el campo por tareas 28541 y 28542 cambio realizado por Camilo Gómez Y Cristhian Murrillo
        
		//this.areaFiltro=ConstantesBD.codigoNuncaValido;
		
        this.nombrePersonaRecibe="";
        
        this.interfazCompras=false; //UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(codigoInstitucion));
		
		this.pisoFiltro=ConstantesBD.codigoNuncaValido;
		this.habitacionFiltro=ConstantesBD.codigoNuncaValido;;
		this.camaFiltro= ConstantesBD.codigoNuncaValido;
		
		inicializarTagsMap(codigoInstitucion, codigoCentroAtencion);
		
		this.entidadControlaDespachoSaldoMultidosis=UtilidadTexto.getBoolean(ValoresPorDefecto.getEntidadControlaDespachoSaldosMultidosis(codigoInstitucion));
		this.mapaSustitutosBD=new HashMap();
		this.equivalentesMap = new HashMap<String, Object>();
		
	}
	
	
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 */
	public void inicializarTagsMap(int codigoInstitucion, int codigoCentroAtencion)
	{
		this.areasTagMap= CentrosCostoViaIngreso.consultarCentrosCostoViaIngreso(codigoInstitucion, codigoCentroAtencion);
		this.pisosTagMap= Pisos.pisosXCentroAtencionTipo(codigoCentroAtencion, codigoInstitucion);
		this.habitacionesTagMap= Habitaciones.habitacionesXCentroAtencionTipo(codigoCentroAtencion, codigoInstitucion);
		this.camasTagMap= Camas1.listadoCamas1(codigoInstitucion, codigoCentroAtencion);
		
	}
	
	
	
	/**
	 * @return the areaFiltro
	 */
	public int getAreaFiltro() {
		return areaFiltro;
	}

	/**
	 * @param areaFiltro the areaFiltro to set
	 */
	public void setAreaFiltro(int areaFiltro) {
		this.areaFiltro = areaFiltro;
	}

	/**
	 * @return the camaFiltro
	 */
	public int getCamaFiltro() {
		return camaFiltro;
	}

	/**
	 * @param camaFiltro the camaFiltro to set
	 */
	public void setCamaFiltro(int camaFiltro) {
		this.camaFiltro = camaFiltro;
	}

	/**
	 * @return the habitacionFiltro
	 */
	public int getHabitacionFiltro() {
		return habitacionFiltro;
	}

	/**
	 * @param habitacionFiltro the habitacionFiltro to set
	 */
	public void setHabitacionFiltro(int habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}

	/**
	 * @return the pisoFiltro
	 */
	public int getPisoFiltro() {
		return pisoFiltro;
	}

	/**
	 * @param pisoFiltro the pisoFiltro to set
	 */
	public void setPisoFiltro(int pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}

	/**
	 * @return Returns the numeroSustituto.
	 */
	public int getNumeroSustituto() {
		return numeroSustituto;
	}
	/**
	 * @param numeroSustituto The numeroSustituto to set.
	 */
	public void setNumeroSustituto(int numeroSustituto) {
		this.numeroSustituto = numeroSustituto;
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
	 *//*
	public int getOffset()
	{
		return offset;
	}*/

	/**
	 * Asigna Offset del pager
	 * @param i
	 *//*
	public void setOffset(int i) 
	{
		offset = i;
	}*/
	
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}
	
	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
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
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the fechaSolicitud.
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	/**
	 * @return Returns the horaSoliciutd.
	 */
	public String getHoraSoliciutd() {
		return horaSoliciutd;
	}
	/**
	 * @param horaSoliciutd The horaSoliciutd to set.
	 */
	public void setHoraSoliciutd(String horaSoliciutd) {
		this.horaSoliciutd = horaSoliciutd;
	}
	/**
	 * @return Returns the medicoSolicitante.
	 */
	public String getMedicoSolicitante() {
		return medicoSolicitante;
	}
	/**
	 * @param medicoSolicitante The medicoSolicitante to set.
	 */
	public void setMedicoSolicitante(String medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}
	/**
	 * @return Returns the numeroAutorizacion.
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * @param numeroAutorizacion The numeroAutorizacion to set.
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
	/**
	 * @return Returns the despachoParcial.
	 */
	public int getDespachoParcial() {
		return despachoParcial;
	}
	/**
	 * @param despachoParcial The despachoParcial to set.
	 */
	public void setDespachoParcial(int despachoParcial) {
		this.despachoParcial = despachoParcial;
	}
	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	
	/**
	 * @return Returns the codigoArticuloSustituto.
	 */
	public int getCodigoArticuloSustituto() {
		return codigoArticuloSustituto;
	}
	/**
	 * @param codigoArticuloSustituto The codigoArticuloSustituto to set.
	 */
	public void setCodigoArticuloSustituto(int codigoArticuloSustituto) {
		this.codigoArticuloSustituto = codigoArticuloSustituto;
	}
    
    public String getRadioFinal()
    {
        return radioFinal;
    }
    /**
     * @param radioFinal asigna radioFinal.
     */
    public void setRadioFinal(String radioFinal)
    {
        this.radioFinal = radioFinal;
    }
    /**
     * @return Retorna  radioPendiente.
     */
    public String getRadioPendiente()
    {
        return radioPendiente;
    }
    /**
     * @param radioPendiente asigna radioPendiente.
     */
    public void setRadioPendiente(String radioPendiente)
    {
        this.radioPendiente = radioPendiente;
    }
    /**
     * @return Retorna  checkDirecto.
     */
    public String getCheckDirecto()
    {
        return checkDirecto;
    }
    /**
     * @param checkDirecto asigna checkDirecto.
     */
    public void setCheckDirecto(String checkDirecto)
    {
        this.checkDirecto = checkDirecto;
    }
	/**
	 * @return Returns the coleccionNumerosSustitutos.
	 */
	public Collection getColeccionNumerosSustitutos() {
		return coleccionNumerosSustitutos;
	}
	/**
	 * @param coleccionNumerosSustitutos The coleccionNumerosSustitutos to set.
	 */
	public void setColeccionNumerosSustitutos(
			Collection coleccionNumerosSustitutos) {
		this.coleccionNumerosSustitutos = coleccionNumerosSustitutos;
	}
	/**
	 * @return Returns the numeroOriginal.
	 */
	public int getNumeroOriginal() {
		return numeroOriginal;
	}
	/**
	 * @param numeroOriginal The numeroOriginal to set.
	 */
	public void setNumeroOriginal(int numeroOriginal) {
		this.numeroOriginal = numeroOriginal;
	}
	
	/**
	 * Método que obtiene un String (numeroSustituto-numeroOriginal)
	 * y los parte y los adiciona a estos atributos de la clase.
	 * @param combinado
	 */
	public void setCombinado (String combinado)
	{
		try
		{
			String temp[]=combinado.split("-");
			this.numeroSustituto=Integer.parseInt(temp[0]);
			this.numeroOriginal=Integer.parseInt(temp[1]);
		}
		catch (Exception e){};
	}
    /**
     * @return Retorna  esVacio.
     */
    public int getEsVacio()
    {
        return esVacio;
    }
    /**
     * @param esVacio asigna esVacio.
     */
    public void setEsVacio(int esVacio)
    {
        this.esVacio = esVacio;
    }
	/**
	 * @return Returns the esVacio1.
	 */
	public int getEsVacio1() {
		return esVacio1;
	}
	/**
	 * @param esVacio1 The esVacio1 to set.
	 */
	public void setEsVacio1(int esVacio1) {
		this.esVacio1 = esVacio1;
	}
    /**
     * @return Retorna  codigoArticulo.
     */
    public int getCodigoArticulo()
    {
        return codigoArticulo;
    }
    /**
     * @param codigoArticulo asigna codigoArticulo.
     */
    public void setCodigoArticulo(int codigoArticulo)
    {
        this.codigoArticulo = codigoArticulo;
    }
    
	/**
	 * @return Returns the estadoMedico.
	 */
	public String getEstadoMedico() {
		return estadoMedico;
	}
	/**
	 * @param estadoMedico The estadoMedico to set.
	 */
	public void setEstadoMedico(String estadoMedico) {
		this.estadoMedico = estadoMedico;
	}
	
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
    
    
   /**
     * @return Retorna  despacho.
     */
    public int getDespacho()
    {
        return despacho;
    }
    /**
     * @param despacho asigna despacho.
     */
    public void setDespacho(int despacho)
    {
        this.despacho = despacho;
    }
    /**
     * @return Retorna  despachoTotal.
     */
    public int getDespachoTotal()
    {
        return despachoTotal;
    }
   public boolean isTieneAutorizacionEntidadSub() {
		return tieneAutorizacionEntidadSub;
	}

	public void setTieneAutorizacionEntidadSub(boolean tieneAutorizacionEntidadSub) {
		this.tieneAutorizacionEntidadSub = tieneAutorizacionEntidadSub;
	}

/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
    /**
     * @param despachoTotal asigna despachoTotal.
     */
    public void setDespachoTotal(int despachoTotal)
    {
        this.despachoTotal = despachoTotal;
    }
    /**
     * @return Retorna  unidadMedida.
     */
    public String getUnidadMedida()
    {
        return unidadMedida;
    }
    /**
     * @param unidadMedida asigna unidadMedida.
     */
    public void setUnidadMedida(String unidadMedida)
    {
        this.unidadMedida = unidadMedida;
    }
    
    /**
     * Retorna boolean si es o no directo
     * @return
     */
    public boolean getEsDirecto()
    {
    	if(this.checkDirecto.equals("on"))
    		return true;
		else 
		return false;
    }
	/**
	 * @return Returns the tamañoCollectionOriginal.
	 */
	public int getTamanoCollectionOriginal() {
		return tamanoCollectionOriginal;
	}
	/**
	 * @param tamañoCollectionOriginal The tamañoCollectionOriginal to set.
	 */
	public void setTamanoCollectionOriginal(int tamanoCollectionOriginal) {
		this.tamanoCollectionOriginal = tamanoCollectionOriginal;
	}
    
    /**
     * @return Retorna  despachoMap.
     */
    public HashMap getDespachoMap()
    {
        return despachoMap;
    }
    /**
     * @param despachoMap asigna despachoMap.
     */
    public void setDespachoMap(HashMap despachoMap)
    {
        this.despachoMap = despachoMap;
    }
    
    /**
     * @return Retorna  articulo.
     */
    public String getArticulo()
    {
        return articulo;
    }
    /**
     * @param articulo asigna articulo.
     */
    public void setArticulo(String articulo)
    {
        this.articulo = articulo;
    }
    /**
     * @return Retorna  numeroIngresos.
     */
    public int getNumeroIngresos()
    {
        return numeroIngresos;
    }
    /**
     * @param numeroIngresos asigna numeroIngresos.
     */
    public void setNumeroIngresos(int numeroIngresos)
    {
        this.numeroIngresos = numeroIngresos;
    }
    
	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}

	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}

	/**
	 * @return Returns the coleccionArticulosInsertados.
	 */
	public Collection getColeccionArticulosInsertados() {
		return coleccionArticulosInsertados;
	}
	/**
	 * @param coleccionArticulosInsertados The coleccionArticulosInsertados to set.
	 */
	public void setColeccionArticulosInsertados(
			Collection coleccionArticulosInsertados) {
		this.coleccionArticulosInsertados = coleccionArticulosInsertados;
	}
	
	/**
	 * Set del mapa de articulos
	 * @param key
	 * @param value
	 */

	public void setCodigosArticulosMap(String key, Object value) 
	{
		codigosArticulosMap.put(key, value);
	}

	/**
	 * Get del mapa de articulos
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getCodigosArticulosMap(String key) 
	{
		return codigosArticulosMap.get(key);
	}
	/**
	 * @return Returns the termino.
	 */
	public boolean getTermino() {
		return termino;
	}
	/**
	 * @param termino The termino to set.
	 */
	public void setTermino(boolean terminoF) {
		termino = terminoF;
	}
    /**
     * @return Retorna  coleccionInsumos.
     */
    public Collection getColeccionInsumos()
    {
        return coleccionInsumos;
    }
    /**
     * @param coleccionInsumos asigna coleccionInsumos.
     */
    public void setColeccionInsumos(Collection coleccionInsumos)
    {
        this.coleccionInsumos = coleccionInsumos;
    }
    /**
     * @return Returns the orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * @param orden The orden to set.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
    /**
     * @return Returns the nombreCentroCostoSolicitado.
     */
    public String getNombreCentroCostoSolicitado() {
        return nombreCentroCostoSolicitado;
    }
    /**
     * @param nombreCentroCostoSolicitado The nombreCentroCostoSolicitado to set.
     */
    public void setNombreCentroCostoSolicitado(
            String nombreCentroCostoSolicitado) {
        this.nombreCentroCostoSolicitado = nombreCentroCostoSolicitado;
    }
    /**
     * @return Returns the codigoCentroCostoSolicitado.
     */
    public int getCodigoCentroCostoSolicitado() {
        return codigoCentroCostoSolicitado;
    }
    /**
     * @param codigoCentroCostoSolicitado The codigoCentroCostoSolicitado to set.
     */
    public void setCodigoCentroCostoSolicitado(int codigoCentroCostoSolicitado) {
        this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
    }
    /**
     * @return Returns the codigosArticulosMap.
     */
    public HashMap getCodigosArticulosMap() {
        return codigosArticulosMap;
    }
    /**
     * @param codigosArticulosMap The codigosArticulosMap to set.
     */
    public void setCodigosArticulosMap(HashMap codigosArticulosMap) {
        this.codigosArticulosMap = codigosArticulosMap;
    }
    /**
     * Set del mapa de mensajes 
     * @param key
     * @param value
     */
    public void setMensajesAdvertenciaMap(String key, Object value) 
    {
        mensajesAdvertenciaMap.put(key, value);
    }
    /**
     * Get del mapa de mensajes 
     */
    public Object getMensajesAdvertenciaMap(String key) 
    {
        return mensajesAdvertenciaMap.get(key);
    }
    /**
     * @return Returns the mensajesAdvertenciaMap.
     */
    public HashMap getMensajesAdvertenciaMap() {
        return mensajesAdvertenciaMap;
    }
    /**
     * @param mensajesAdvertenciaMap The mensajesAdvertenciaMap to set.
     */
    public void setMensajesAdvertenciaMap(HashMap mensajesAdvertenciaMap) {
        this.mensajesAdvertenciaMap = mensajesAdvertenciaMap;
    }

	/**
	 * @return Returns the mezclaOrdenMedica.
	 */
	public String getMezclaOrdenDieta() {
		return mezclaOrdenDieta;
	}

	/**
	 * @param mezclaOrdenMedica The mezclaOrdenMedica to set.
	 */
	public void setMezclaOrdenDieta(String mezclaOrdenMedica) {
		this.mezclaOrdenDieta = mezclaOrdenMedica;
	}

	/**
	 * @return Returns the tipoListado.
	 */
	public String getTipoListado() {
		return tipoListado;
	}

	/**
	 * @param tipoListado The tipoListado to set.
	 */
	public void setTipoListado(String tipoListado) {
		this.tipoListado = tipoListado;
	}

	/**
	 * @return Returns the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return Returns the estadoListado.
	 */
	public String getEstadoListado() {
		return estadoListado;
	}

	/**
	 * @param estadoListado The estadoListado to set.
	 */
	public void setEstadoListado(String estadoListado) {
		this.estadoListado = estadoListado;
	}

	/**
	 * @return the nombrePersonaRecibe
	 */
	public String getNombrePersonaRecibe() {
		return nombrePersonaRecibe;
	}

	/**
	 * @param nombrePersonaRecibe the nombrePersonaRecibe to set
	 */
	public void setNombrePersonaRecibe(String nombrePersonaRecibe) {
		this.nombrePersonaRecibe = nombrePersonaRecibe;
	}

	public boolean isInterfazCompras() {
		return interfazCompras;
	}

	public void setInterfazCompras(boolean interfazCompras) {
		this.interfazCompras = interfazCompras;
	}

	/**
	 * @return the almacenesConsignacion
	 */
	public HashMap<String, Object> getAlmacenesConsignacion() {
		return almacenesConsignacion;
	}

	/**
	 * @param almacenesConsignacion the almacenesConsignacion to set
	 */
	public void setAlmacenesConsignacion(
			HashMap<String, Object> almacenesConsignacion) {
		this.almacenesConsignacion = almacenesConsignacion;
	}

	/**
	 * @return the conveniosProveedor
	 */
	public HashMap<String, Object> getConveniosProveedor() {
		return conveniosProveedor;
	}

	/**
	 * @param conveniosProveedor the conveniosProveedor to set
	 */
	public void setConveniosProveedor(HashMap<String, Object> conveniosProveedor) {
		this.conveniosProveedor = conveniosProveedor;
	}

	/**
	 * @return the proveedorCatalogo
	 */
	public HashMap<String, Object> getProveedorCatalogo() {
		return proveedorCatalogo;
	}

	/**
	 * @param proveedorCatalogo the proveedorCatalogo to set
	 */
	public void setProveedorCatalogo(HashMap<String, Object> proveedorCatalogo) {
		this.proveedorCatalogo = proveedorCatalogo;
	}
	
	/**
	 * @return the almacenesConsignacion
	 */
	public Object getAlmacenesConsignacion(String key) 
	{
		return almacenesConsignacion.get(key);
	}

	/**
	 * @param almacenesConsignacion the almacenesConsignacion to set
	 */
	public void setAlmacenesConsignacion(String key,Object value) 
	{
		this.almacenesConsignacion.put(key, value);
	}
	
	/**
	 * @return the conveniosProveedor
	 */
	public Object getConveniosProveedor(String key) 
	{
		return conveniosProveedor.get(key);
	}

	/**
	 * @param conveniosProveedor the conveniosProveedor to set
	 */
	public void setConveniosProveedor(String key,Object value) 
	{
		this.conveniosProveedor.put(key, value);
	}
	
	/**
	 * @return the proveedorCatalogo
	 */
	public Object getProveedorCatalogo(String key) {
		return proveedorCatalogo.get(key);
	}

	/**
	 * @param proveedorCatalogo the proveedorCatalogo to set
	 */
	public void setProveedorCatalogo(String key,Object value) {
		this.proveedorCatalogo.put(key, value);
	}

	/**
	 * @return the areasTagMap
	 */
	public HashMap<String, Object> getAreasTagMap() {
		return areasTagMap;
	}

	/**
	 * @param areasTagMap the areasTagMap to set
	 */
	public void setAreasTagMap(HashMap<String, Object> areasTagMap) {
		this.areasTagMap = areasTagMap;
	}

	/**
	 * @return the camasTagMap
	 */
	public HashMap<Object, Object> getCamasTagMap() {
		return camasTagMap;
	}

	/**
	 * @param camasTagMap the camasTagMap to set
	 */
	public void setCamasTagMap(HashMap<Object, Object> camasTagMap) {
		this.camasTagMap = camasTagMap;
	}

	/**
	 * @return the habitacionesTagMap
	 */
	public HashMap<String, Object> getHabitacionesTagMap() {
		return habitacionesTagMap;
	}

	/**
	 * @param habitacionesTagMap the habitacionesTagMap to set
	 */
	public void setHabitacionesTagMap(HashMap<String, Object> habitacionesTagMap) {
		this.habitacionesTagMap = habitacionesTagMap;
	}

	/**
	 * @return the pisosTagMap
	 */
	public HashMap<String, Object> getPisosTagMap() {
		return pisosTagMap;
	}

	/**
	 * @param pisosTagMap the pisosTagMap to set
	 */
	public void setPisosTagMap(HashMap<String, Object> pisosTagMap) {
		this.pisosTagMap = pisosTagMap;
	}

////////
	
	/**
	 * @return the areasTagMap
	 */
	public Object getAreasTagMap(String key) {
		return areasTagMap.get(key);
	}

	/**
	 * @param areasTagMap the areasTagMap to set
	 */
	public void setAreasTagMap(String key, Object value) {
		this.areasTagMap.put(key, value);
	}

	/**
	 * @return the camasTagMap
	 */
	public Object getCamasTagMap(String key) {
		return camasTagMap.get(key);
	}

	/**
	 * @param camasTagMap the camasTagMap to set
	 */
	public void setCamasTagMap(String key, Object value) {
		this.camasTagMap.put(key, value);
	}

	/**
	 * @return the habitacionesTagMap
	 */
	public Object getHabitacionesTagMap(String key) {
		return habitacionesTagMap.get(key);
	}

	/**
	 * @param habitacionesTagMap the habitacionesTagMap to set
	 */
	public void setHabitacionesTagMap(String key, Object value) {
		this.habitacionesTagMap.put(key, value);
	}

	/**
	 * @return the pisosTagMap
	 */
	public Object getPisosTagMap(String key) {
		return pisosTagMap.get(key);
	}

	/**
	 * @param pisosTagMap the pisosTagMap to set
	 */
	public void setPisosTagMap(String key, Object value) {
		this.pisosTagMap.put(key, value);
	}

	public boolean isEntidadControlaDespachoSaldoMultidosis() {
		return entidadControlaDespachoSaldoMultidosis;
	}

	public void setEntidadControlaDespachoSaldoMultidosis(
			boolean entidadControlaDespachoSaldoMultidosis) {
		this.entidadControlaDespachoSaldoMultidosis = entidadControlaDespachoSaldoMultidosis;
	}

	public int getCodigoCentroCostoSolicitante() {
		return codigoCentroCostoSolicitante;
	}

	public void setCodigoCentroCostoSolicitante(int codigoCentroCostoSolicitante) {
		this.codigoCentroCostoSolicitante = codigoCentroCostoSolicitante;
	}

	public String getCantidadeq() {
		return cantidadeq;
	}

	public void setCantidadeq(String cantidadeq) {
		this.cantidadeq = cantidadeq;
	}

	public HashMap<String, String> getMapaSustitutosBD() {
		return mapaSustitutosBD;
	}

	public void setMapaSustitutosBD(HashMap<String, String> mapaSustitutosBD) {
		this.mapaSustitutosBD = mapaSustitutosBD;
	}
	
	/*
	 * Métodos get y set de los atributos del pager
	 */
	
	/**
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * @return the posColeccion
	 */
	public int getPosColeccion() {
		return posColeccion;
	}

	/**
	 * @param posColeccion the posColeccion to set
	 */
	public void setPosColeccion(int posColeccion) {
		this.posColeccion = posColeccion;
	}

	/**
	 * @return the equivalentesMap
	 */
	public HashMap<String, Object> getEquivalentesMap() {
		return equivalentesMap;
	}

	/**
	 * @param equivalentesMap the equivalentesMap to set
	 */
	public void setEquivalentesMap(HashMap<String, Object> equivalentesMap) {
		this.equivalentesMap = equivalentesMap;
	}
	
	/**
	 * @return the equivalentesMap
	 */
	public Object getEquivalentesMap(String llave) {
		return equivalentesMap.get(llave);
	}

	/**
	 * @param equivalentesMap the equivalentesMap to set
	 */
	public void setEquivalentesMap(String llave, Object obj) {
		this.equivalentesMap.put(llave, obj);
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public long getCodigoAutorizacionEntidadSubcontratada() {
		return codigoAutorizacionEntidadSubcontratada;
	}

	public void setCodigoAutorizacionEntidadSubcontratada(
			long codigoAutorizacionEntidadSubcontratada) {
		this.codigoAutorizacionEntidadSubcontratada = codigoAutorizacionEntidadSubcontratada;
	}

	public int getCodigoDespacho() {
		return codigoDespacho;
	}

	public void setCodigoDespacho(int codigoDespacho) {
		this.codigoDespacho = codigoDespacho;
	}

	public String getManejaExistenciasNegativas() {
		return manejaExistenciasNegativas;
	}

	public void setManejaExistenciasNegativas(String manejaExistenciasNegativas) {
		this.manejaExistenciasNegativas = manejaExistenciasNegativas;
	}

}