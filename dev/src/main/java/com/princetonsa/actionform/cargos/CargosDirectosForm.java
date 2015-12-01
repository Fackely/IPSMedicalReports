/*
 * @(#)CargosDirectosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;

/**
 * Form que contiene todos los datos específicos para generar 
 * los Cargos Directos
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Junio 20, 2005
 * @author wrios 
 */
public class CargosDirectosForm extends ValidatorForm 
{
	
	//****Atributos para el manejo de Autorizaciones Población Capitada*****
	//****Cambio Versión 1.50 Anexo Cargos Directos de Servicios-134********
	/**
	 * Este atributo se usa para determinar cuando se generó una 
	 * autorización de solicitudes y mostrar el respectivo botón
	 */
	private boolean mostrarImprimirAutorizacion;
	
	/**
	 * lista que contiene los 
	 * nombres de los reportes de las autorizaciones 
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**
	 * Atributo que contiene el diagnóstico y 
	 * tipo CIE del paciente
	 */
	private DtoDiagnostico dtoDiagnostico;
	
	/**
	 * Lista para guardar los id de la solicitudes generadas. 
	 * para realizar el proceso de generacion de autorizaciones.
	 */
	private List<Integer> listaNumerosSolicitud;
	//****Atributos para el manejo de Autorizaciones Población Capitada*****
	
    /**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Variable usada para manejar el caso de la cuenta
	 */
	private String casoCuenta;
	
	/**
	 * Variable para tener el id de la cuenta
	 */
	private String idCuenta;
	
	/**
	 * Variable para tener el id de la cuenta asociada
	 */
	private String idCuentaAsociada;
	
	/**
	 * Variable para retener el id de la subcuenta en el caso
	 * de que la cuenta principal sea distribuida
	 */
	private String idSubCuenta;
	
	/**
	 * id de la cuenta o subcuenta seleccionada
	 */
	private int idCuentaSubCuentaSeleccionado;
	
	/**
	 * Variables para retener los codigos de las vías de ingreso de las cuentas
	 * Principal y Asociada
	 */
	private int codigoViaIngresoCuenta;
	private int codigoViaIngresoCuentaAsociada;
	
	/**
	 * Variables para retener los nombres de las vías de ingreso de las cuentas
	 * Principal y Asociada
	 */
	private String nombreViaIngresoCuenta;
	private String nombreViaIngresoCuentaAsociada;
	
	/**
	 * Variables usadas para indicar si el responsable seleccionado está asociado
	 * a una cuenta distribuida o a la cuenta asociada
	 */
	private boolean esLaAsociada;
	private boolean esLaDistribuida;
	
	/**
	 * Fecha de la solicitud
	 */
	private String fecha;
	
	/**
	 * Hora de la solicitud
	 */
	private String hora;
	
	/**
	 * codigo del centro de costo que solicita
	 */
	private int codigoCentroCostoSolicita;
	
	/**
	 * nombre del centro de costo que solicita
	 */
	private String nombreCentroCostoSolicita;
	
	/**
	 * codigo de farmacia o subalmacen que esta despachando
	 * los articulos
	 */
	private int codigoFarmacia;
	
    /**
     * nombre de la farmacia o subalmacen que esta despachando 
     * los articulos
     */
    private String nombreFarmacia;
    
	/**
	 * Mapa que contiene los N servicios o los N articulos de la
	 * generacion de cargos directos
	 */
	private HashMap serviciosArticulosMap= new HashMap();
	
	/**
	 * Número de filas existentes en el hashmap (este sirve para articulos)
	 */
	private int numeroFilasMapa=0;
	
	/**
	 * numero de filas existentes en el hashmap pero en el caso de servicios
	 */
	private int numeroFilasMapaCasoServicios;
	
	
	/**
	 * codigo del convenio de la cuenta o subcuenta seleccionada
	 */
	private int codigoConvenioCuentaOSubcuentaSeleccionada;
	
	/**
	 * acronimo del tipo Regimen convenio de la cuenta o subcuenta seleccionada
	 */
	private String acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada;
	
	/**
	 * numero de solicitud generado
	 */
	private String numeroSolicitudGenerado;
	
	/**
	 * consecutivo de la orden medica
	 */
	private String consecutivoOrdenMedica;
	
	/*//////////vartiables con los criterios de busqueda///////////////*/
	/**
	 * codigo Axioma de la busqueda
	 */
	private String codigoAxioma;
	
	/**
	 * codigoCups de la busqueda
	 */
	private String codigoCups;
	
	/**
	 * codigo soat de la busqueda
	 */
	private String codigoSoat;
	
	/**
	 * cod iss de la busqueda
	 */
	private String codigoIss;
	
	/**
	 * descarpcion de la busqueda
	 */
	private String descripcion;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	/**
	 * codigos de los servicios insertados para no repetirlos
	 * en la busqueda avanzada de servicios
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * almacena la fecha de la AdmisionOApertura 
	 * sea de urgencias o hospitalizacion.
	 */
	private String fechaAdmisionOApertura;
	
	/**
	 * almacena la hora de la primera AdmisionOApertura
	 */
	private String horaAdmisionOApertura;
	
	private String comentario;
	
    /**
     * String que indica si es cargos de articulos o de sercivicios,
     * carga el String "servicios" - "articulos" o "".
     */
    private String esCargosArticulosOServicios;
    
    /**
     * listado de almacenes del mapa
     */
    private HashMap listadoAlmacenesMap= new HashMap();
    
    /**
     * numero columnas del mapa articulos
     */
    private final int numeroColumnasMapaArticulos=14;
    
    /**
     * numero columnas del mapa servicios
     */
    private final int numeroColumnasMapaServicios=18;
    
    /**
     * codigo de la institucion
     */
    private int codigoInstitucion;
    
    /**
     * mapa que contiene los mensajes de advertencia tanto para 
     * articulos con existencias negativas, articulos que no cumplen con las condiciones de 
     * stock maximo - stock minimo y punto de pedido
     */
    private HashMap mensajesAdvertenciaMap= new HashMap();
        
    /**
     * Mapa utilitario 
     * */
    private HashMap mapaUtilitarioMap = new HashMap();  
    
    /**
     * 
     */
    private String mensajeJustificacion;
    
    /**
     * 
     */
    private HashMap justificacionNoPosMap  = new HashMap();
    
    /**
     * ArrayUtilitario 1
     * */
    private ArrayList arrayUtilitario1;
    
    /**
     * ArrayUtilitario 2
     * */
    private ArrayList arrayUtilitario2;
    
    
    private String fechaEjeccuion;
    
    ArrayList<String> mensajes = new ArrayList<String>();
    
    /***
     * Indicador de la posicion de las cuentas del paciente
     * */
    private String indicadorPosCuentas;   
    
    /**
     * 
     */
    ArrayList<ArrayList<InfoDatosInt>> centrosCostoEjecutanArray = new ArrayList<ArrayList<InfoDatosInt>>();
    
    /**
     * 
     */
    ArrayList<ArrayList<InfoDatosInt>> profesionalesEjecutanArray = new ArrayList<ArrayList<InfoDatosInt>>();
    
    // Cambios Segun Anexo 809
    /**
     * 
     */
    ArrayList<ArrayList<InfoDatosInt>> especialidadProfesionalesEjecutanArray = new ArrayList<ArrayList<InfoDatosInt>>();
    
    /**
     * 
     */
    private String codigoMedicoResponde;
    
    // Fin Cambios Segun Anexo 809
    
    /**
     * 
     */
    private int indice;
    
    /**
	 * Variable para almacenar los Convenios que sean plan especial por ingreso del Paciente
	 */
	private int numConveniosPlanEspecial=0;
    
	/**
	 * Atributo que almacena la info de cobertura de cada articulo
	 */
	private List<InfoResponsableCobertura> infoCoberturaCargoDirecto;
    
	public ArrayList<String> getMensajes() {
		return mensajes;
	}

	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}
	
	public int getSizeMensajes() {
		return mensajes.size();
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
		
        //////////////////////////VALIDACIONES ARTICULOS
		
		if(estado.equals("guardarArticulos"))
		{				
			errores=super.validate(mapping,request);
			if(this.getFecha().equals(""))
			{
			    errores.add("Campo Fecha  vacio", new ActionMessage("errors.required","El campo Fecha"));
			}
			else
			{
			    if(!UtilidadFecha.validarFecha(this.getFecha()))
				{
					errores.add("Fecha ", new ActionMessage("errors.formatoFechaInvalido", this.getFecha()));							
				}
				//else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFecha()))
			    else 
				{	
			        if(this.getHora().equals(""))
				    {
				        errores.add("Campo Hora vacio", new ActionMessage("errors.required","El campo Hora"));
				    }
			        else if (UtilidadFecha.validacionHora(this.getHora()).puedoSeguir)
			        {
			            if ( !  UtilidadFecha.compararFechas(  UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFecha(), this.getHora()  ).isTrue()  )
				            errores.add("Fecha ", new ActionMessage("errors.fechaPosteriorIgualActual",this.getFecha()+" "+this.getHora(), UtilidadFecha.getFechaActual()+ " "+UtilidadFecha.getHoraActual()+" actual"));
			        }
			        else
			        {
			            errores.add("Hora ", new ActionMessage("errors.formatoHoraInvalido", this.getHora()));
			        }
			        //si todavia no hay errores entonces hacer la comparacion con la AdmisionOApertura
			        if(errores.isEmpty())
			        {
			            if (!UtilidadFecha.compararFechas(  this.getFecha(), this.getHora(), this.getFechaAdmisionOApertura(), this.getHoraAdmisionOApertura()  ).isTrue()  )
				            errores.add("Fecha ", new ActionMessage("errors.fechaHoraAnteriorIgualActual",this.getFecha()+" "+this.getHora(), this.getFechaAdmisionOApertura()+" "+this.getHoraAdmisionOApertura()+" "+this.getComentario()));
                        //hacer la validacion con el cierre de inventarios
                        if(UtilidadInventarios.existeCierreInventarioParaFecha(this.getFecha(),this.codigoInstitucion))
                            errores.add("Fecha", new ActionMessage("error.inventarios.existeCierreInventarios", this.getFecha()));
			        }
			 	}
				
			    if(this.getNumeroFilasMapa()==0)
			    {
			        errores.add("Campo Articulo vacio", new ActionMessage("errors.required","El/los Artículo(s) a generar cargo directo "));
			    }
			}
			if(this.getCodigoFarmacia()<0)
			{
			    errores.add("Campo Farmcia no sel", new ActionMessage("errors.required","El campo Farmacia"));
			}
			else
			{
				/*si el usuario tiene acceso a modificar la tarifa entonces se valida*/
		        Connection con= UtilidadBD.abrirConexion();
			    for(int i=0; i<this.getNumeroFilasMapa(); i++)
			    {
			        String codigoArticuloTemp="";
			        
			        if(!(this.getServiciosArticulosMap("fueEliminadoArticulo_"+i)+"").equals("true"))
			        {    
				        try
				        {
				            codigoArticuloTemp= this.getServiciosArticulosMap("codigoArticulo_"+i)+"";
				            Integer.parseInt(this.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"");
				            if(Integer.parseInt(this.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"")==0)
				            {
				                errores.add("Campo cantidad", new ActionMessage("errors.integerMayorQue","El campo Cantidad para el Artículo "+codigoArticuloTemp, "0"));
				            }
                        }
				        catch(NullPointerException ne)
				        {
				            errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Artículo "+codigoArticuloTemp));
				        }
				        catch(NumberFormatException nfe)
				        {
				            errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Artículo "+codigoArticuloTemp));
				        }
				        
				        UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				        if(!UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con, usuario.getLoginUsuario(), 378).trim().equals(""))
						{
				        	if(this.getServiciosArticulosMap("existeTarifa_"+i).toString().equals("si"))
				        	{	
						        try
						        {
						        	codigoArticuloTemp= this.getServiciosArticulosMap("codigoArticulo_"+i)+"";
						        	if((this.getServiciosArticulosMap("valorUnitario_"+i)+"").trim().equals(""))
						        	{
						        		errores.add("Campo Valor Unitario", new ActionMessage("errors.required","El campo Valor Unitario para el Artículo "+codigoArticuloTemp));
						        	}
						        	else if(Double.parseDouble(this.getServiciosArticulosMap("valorUnitario_"+i)+"")<0)
						            {
						                errores.add("Campo Valor Unitario", new ActionMessage("errors.floatMayorOIgualQue","El campo Valor Unitario para el Artículo "+codigoArticuloTemp, "0"));
						            }
						        }
						        catch(NullPointerException ne)
						        {
						            errores.add("Campo Valor Unitario", new ActionMessage("errors.invalid","El campo Valor Unitario para el Artículo "+codigoArticuloTemp));
						        }
						        catch(NumberFormatException nfe)
						        {
						            errores.add("Campo Valor Unitario", new ActionMessage("errors.invalid","El campo Valor Unitario para el Articulo "+codigoArticuloTemp));
						        }
				        	}    
						} 
				    }    
			    }
			    UtilidadBD.closeConnection(con);
			}
			
			//SE HACEN LAS VALIDACIONES DE LOS LOTES!!!!!!!!
			if(errores.isEmpty())
			{
				Connection con= UtilidadBD.abrirConexion();
				for(int i=0; i<this.getNumeroFilasMapa(); i++)
			    {
			        if(!(this.getServiciosArticulosMap("fueEliminadoArticulo_"+i)+"").equals("true"))
			        {
			        	if(Articulo.articuloManejaLote(con, Integer.parseInt(this.getServiciosArticulosMap("codigoArticulo_"+i).toString()), this.getCodigoInstitucion()))
						{
							//como maneja lote entonces la info no puede ser vacia
							if(UtilidadTexto.isEmpty(this.getServiciosArticulosMap("lote_"+i).toString()))
							{
								errores.add("", new ActionMessage("errors.required","La información de Lote para el Artículo "+this.getServiciosArticulosMap("codigoArticulo_"+i).toString()));
							}
						}
			        }
			    }
				UtilidadBD.closeConnection(con);
			}
			
			if(errores.isEmpty())
			{
				Connection con= UtilidadBD.abrirConexion();
				//validaciones de la existencia
				for(int i=0; i<this.getNumeroFilasMapa(); i++)
			    {
					if(!(this.getServiciosArticulosMap("fueEliminadoArticulo_"+i)+"").equals("true"))
			        {
						if(!AlmacenParametros.manejaExistenciasNegativas(this.getCodigoFarmacia(), this.getCodigoInstitucion()))
		                {
		                	if(!Articulo.articuloManejaLote(con, Integer.parseInt(this.getServiciosArticulosMap("codigoArticulo_"+i).toString()), this.getCodigoInstitucion()))
		                	{	
			                	if(Integer.parseInt(this.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"")>Integer.parseInt(this.getServiciosArticulosMap("existenciaXAlmacen_"+i)+""))
			                    {
			                        errores.add("error.inventarios.existenciasInsuficientes", 
			                                new ActionMessage("error.inventarios.existenciasInsuficientes", 
			                                                          this.getServiciosArticulosMap("descripcionArticulo_"+i).toString(), 
			                                                          this.getServiciosArticulosMap("existenciaXAlmacen_"+i).toString(), 
			                                                          this.getNombreFarmacia() ));
			                    }
		                	}
		                	else
		                	{
		                		if(Integer.parseInt(this.getServiciosArticulosMap("cantidadDespachadaArticulo_"+i)+"")>Integer.parseInt(this.getServiciosArticulosMap("existenciaXLote_"+i)+""))
			                    {
			                        errores.add("error.inventarios.existenciasInsuficientesLote", 
			                                new ActionMessage("error.inventarios.existenciasInsuficientesLote", 
			                                                          this.getServiciosArticulosMap("descripcionArticulo_"+i).toString(),
			                                                          this.getServiciosArticulosMap("existenciaXLote_"+i).toString(),
			                                                          this.getNombreFarmacia(),
			                                                          this.getServiciosArticulosMap("lote_"+i).toString()));
			                    }
		                	}
		                }
			        }	
			    }
	            UtilidadBD.closeConnection(con);
			}
		}
		
		////////////////////FIN VALIDACIONES ARTICULOS
		///////////////////INICIO VALIDACIONES SERVICIOS
		
		if(estado.equals("guardarServicios"))
		{				
			Utilidades.imprimirMapa(this.getMapaUtilitarioMap());
			errores=super.validate(mapping,request);
			if(this.getFecha().equals(""))
			{
			    errores.add("Campo Fecha  vacio", new ActionMessage("errors.required","El campo Fecha"));
			}
			else
			{
			    if(!UtilidadFecha.validarFecha(this.getFecha()))
				{
					errores.add("Fecha ", new ActionMessage("errors.formatoFechaInvalido", this.getFecha()));							
				}
				//else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFecha()))
			    else 
				{	
			        if(this.getHora().equals(""))
				    {
				        errores.add("Campo Hora vacio", new ActionMessage("errors.required","El campo Hora"));
				    }
			        else if (UtilidadFecha.validacionHora(this.getHora()).puedoSeguir)
			        {
			            if ( !  UtilidadFecha.compararFechas(  UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFecha(), this.getHora()  ).isTrue()  )
				            errores.add("Fecha ", new ActionMessage("errors.fechaPosteriorIgualActual",this.getFecha()+" "+this.getHora(), UtilidadFecha.getFechaActual()+ " "+UtilidadFecha.getHoraActual()+" actual"));
			        }
			        else
			        {
			            errores.add("Hora ", new ActionMessage("errors.formatoHoraInvalido", this.getHora()));
			        }
			        //si todavia no hay errores entonces hacer la comparacion con la AdmisionOApertura
			        if(errores.isEmpty())
			        {
			            if (!UtilidadFecha.compararFechas(  this.getFecha(), this.getHora(), this.getFechaAdmisionOApertura(), this.getHoraAdmisionOApertura()  ).isTrue()  )
				            errores.add("Fecha ", new ActionMessage("errors.fechaHoraAnteriorIgualActual",this.getFecha()+" "+this.getHora(), this.getFechaAdmisionOApertura()+" "+this.getHoraAdmisionOApertura()+" "+this.getComentario()));
			        }
			    }
				
			    if(this.getNumeroFilasMapaCasoServicios()==0)
			    {
			        errores.add("Campo Servicio vacio", new ActionMessage("errors.required","El/los Servicio(s) a generar cargo directo "));
			    }
			    
			}
			
			
			if(this.getFechaEjeccuion().equals(""))
			{
			    errores.add("Campo Fecha  vacio", new ActionMessage("errors.required","El campo Fecha Ejecucion"));
			}
			else
			{
			    if(!UtilidadFecha.validarFecha(this.getFechaEjeccuion()))
				{
					errores.add("Fecha ", new ActionMessage("errors.formatoFechaInvalido", this.getFechaEjeccuion()));							
				}
				//else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFecha()))
			    else 
				{	
			    	if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), getFechaEjeccuion()))
			    	{
			    		errores.add("Fecha ", new ActionMessage("errors.fechaPosteriorIgualActual","Ejecucion "+this.getFechaEjeccuion(), UtilidadFecha.getFechaActual()+ " actual"));
			    	}
			    	if(UtilidadFecha.esFechaMenorQueOtraReferencia(getFechaEjeccuion(),this.getFechaAdmisionOApertura()))
			    	{
			            errores.add("Fecha ", new ActionMessage("errors.fechaAnteriorIgualActual","Ejecucion "+this.getFechaEjeccuion(), this.getFechaAdmisionOApertura()+" Admision"));
			    	}
			    }
				
			    if(this.getNumeroFilasMapaCasoServicios()==0)
			    {
			        errores.add("Campo Servicio vacio", new ActionMessage("errors.required","El/los Servicio(s) a generar cargo directo "));
			    }
			    
			}
			
			
			for(int i=0; i<this.getNumeroFilasMapaCasoServicios(); i++)
			{
			    String codigoServicioTemp="";
			    if(!(this.getServiciosArticulosMap("fueEliminadoServicio_"+i)+"").equals("true"))
		        {   
				    try
			        {
			            codigoServicioTemp= this.getServiciosArticulosMap("codigoServicio_"+i)+"";
			            if(Integer.parseInt(this.getServiciosArticulosMap("codigoCentroCostoEjecuta_"+i)+"")<0)
			            {
			                errores.add("Campo centro costo ejecuta", new ActionMessage("errors.required","El campo Centro Costo Ejecuta para el servicio "+codigoServicioTemp));
			            }
			        }
			        catch(NullPointerException ne)
			        {
			            errores.add("Campo centro costo ejecuta", new ActionMessage("errors.required","El campo Centro Costo Ejecuta para el servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errores.add("Campo centro costo ejecuta", new ActionMessage("errors.required","El campo Centro Costo Ejecuta para el servicio "+codigoServicioTemp));
			        }
			        try
			        {
			            codigoServicioTemp= this.getServiciosArticulosMap("codigoServicio_"+i)+"";
			            if(Integer.parseInt(this.getServiciosArticulosMap("codigoMedicoResponde_"+i)+"")<=0)
			            {
			                errores.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Médico Responde para el servicio "+codigoServicioTemp));
			            }
			            else
			            {
			                if((this.getServiciosArticulosMap("codigoPoolMedico_"+i)+"").equals("") || (this.getServiciosArticulosMap("codigoPoolMedico_"+i)+"").equals("null") || (this.getServiciosArticulosMap("codigoPoolMedico_"+i)+"").equals("-1"))
			                {
			                    errores.add("Campo Pool X Médico ", new ActionMessage("errors.seleccion","Pool X Médico para el servicio "+codigoServicioTemp));
			                }
			            }
			        }
			        catch(NullPointerException ne)
			        {
			            errores.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Médico Responde para el servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errores.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Médico Responde para el servicio "+codigoServicioTemp));
			        }
			        
			        // Cambio Segun Anexo 809
			        try
			        {
			        	codigoServicioTemp= this.getServiciosArticulosMap("codigoServicio_"+i)+"";
			        	if(Utilidades.convertirAEntero(this.getServiciosArticulosMap("codigoEspMedicoResponde_"+i)+"")==ConstantesBD.codigoNuncaValido )
			        	{
			        		errores.add("Campo Especialidad Médico Responde", new ActionMessage("errors.required","El campo Especialidad para el Servicio "+codigoServicioTemp));
			        	}
			        }catch(NullPointerException ne)
			        {
			            errores.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Especialidad para el servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errores.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Especialidad para el servicio "+codigoServicioTemp));
			        }
			        // Fin Cambio Segun Anexo 809
			        
			        try
			        {
			            codigoServicioTemp= this.getServiciosArticulosMap("codigoServicio_"+i)+"";
			            if(Integer.parseInt(this.getServiciosArticulosMap("cantidadServicios_"+i)+"")<=0)
			            {
			                errores.add("Campo cantidad", new ActionMessage("errors.integerMayorQue","El campo Cantidad para el Servicio "+codigoServicioTemp, "0"));
			            }
			        }
			        catch(NullPointerException ne)
			        {
			            errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Servicio "+codigoServicioTemp));
			        }
			        
			        /*si el usuario tiene acceso a modificar la tarifa entonces se valida*/
			        Connection con= UtilidadBD.abrirConexion();
			        UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			        if(!UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con, usuario.getLoginUsuario(), 377).trim().equals(""))
					{
			        	if(this.getServiciosArticulosMap("existeTarifa_"+i).toString().equals("si"))
			        	{	
					        try
					        {
					        	codigoServicioTemp= this.getServiciosArticulosMap("codigoServicio_"+i)+"";
					        	if((this.getServiciosArticulosMap("valorUnitarioConExcepcion_"+i)+"").trim().equals(""))
					        	{
					        		errores.add("Campo Valor Unitario", new ActionMessage("errors.required","El campo Valor Unitario para el Servicio "+codigoServicioTemp));
					        	}
					        	else if(Double.parseDouble(this.getServiciosArticulosMap("valorUnitarioConExcepcion_"+i)+"")<0)
					            {
					                errores.add("Campo Valor Unitario", new ActionMessage("errors.floatMayorOIgualQue","El campo Valor Unitario para el Servicio "+codigoServicioTemp, "0"));
					            }
					        }
					        catch(NullPointerException ne)
					        {
					            errores.add("Campo Valor Unitario", new ActionMessage("errors.invalid","El campo Valor Unitario para el Servicio "+codigoServicioTemp));
					        }
					        catch(NumberFormatException nfe)
					        {
					            errores.add("Campo Valor Unitario", new ActionMessage("errors.invalid","El campo Valor Unitario para el Servicio "+codigoServicioTemp));
					        }
			        	}    
					} 
			        try {
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e) {
						// @todo Auto-generated catch block
						e.printStackTrace();
					}
		        }     
		    }
		}
		if(!errores.isEmpty())
		{
			this.setEstado("empezarContinuar");
		}	
		return errores;
	}
    
    /**
     * resetea los mensajes de advertencia
     * en el resumen
     *
     */
    public void resetMensajes()
    {
        this.mensajesAdvertenciaMap= new HashMap();
        this.justificacionNoPosMap= new HashMap();
        justificacionNoPosMap.put("numRegistros", 0);
        mensajesAdvertenciaMap.put("numRegistros", 0);
    }
    
	/**
	 * Resetea los valores de la forma
	 */
	public void reset()
	{
	    //this.estado="";
	    this.idCuenta="";
	    this.idCuentaAsociada="";
	    this.casoCuenta="";
	    this.idSubCuenta="";
	    this.codigoViaIngresoCuenta=ConstantesBD.codigoNuncaValido;
	    this.codigoViaIngresoCuentaAsociada=ConstantesBD.codigoNuncaValido;
	    this.nombreViaIngresoCuenta="";
	    this.nombreViaIngresoCuentaAsociada="";
	    this.esLaAsociada=false;
	    this.esLaDistribuida=false;
	    this.fecha="";
	    this.hora="";
	    this.codigoCentroCostoSolicita= ConstantesBD.codigoNuncaValido;
	    //this.codigoFarmacia=ConstantesBD.codigoNuncaValido;
	    this.serviciosArticulosMap= new HashMap();
	    this.nombreCentroCostoSolicita="";
	    this.numeroFilasMapa=0;//ConstantesBD.codigoNuncaValido;
	    this.numeroFilasMapaCasoServicios=ConstantesBD.codigoNuncaValido;
	    this.codigoConvenioCuentaOSubcuentaSeleccionada=ConstantesBD.codigoNuncaValido;
	    this.acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada= "";
	    //this.idCuentaSubCuentaSeleccionado= ConstantesBD.codigoNuncaValido;
	    this.numeroSolicitudGenerado="";
	    
	    // los de la busqueda de servicios
	    this.codigoAxioma="";
	    this.codigoCups="";
	    this.codigoIss="";
	    this.codigoSoat="";
	    this.descripcion="";
	    this.codigosServiciosInsertados="";
	    this.fechaAdmisionOApertura = "";
	    this.horaAdmisionOApertura="";
	    this.comentario="";
	    this.consecutivoOrdenMedica="";
        this.listadoAlmacenesMap= new HashMap();
        this.mensajes = new ArrayList<String>();
        
        this.arrayUtilitario1 = new ArrayList(20);
        this.arrayUtilitario2 = new ArrayList(20);
        this.fechaEjeccuion=UtilidadFecha.getFechaActual();
        this.numConveniosPlanEspecial=0;
        this.centrosCostoEjecutanArray= new ArrayList<ArrayList<InfoDatosInt>>();
        this.profesionalesEjecutanArray= new ArrayList<ArrayList<InfoDatosInt>>();
        this.indice= ConstantesBD.codigoNuncaValido;
        
        // Cambio Segun Anexo 809
        this.especialidadProfesionalesEjecutanArray = new ArrayList<ArrayList<InfoDatosInt>>();
        this.codigoMedicoResponde = "";
        // Fin Cambio Segun Anexo 809
        
        //Cambio Version 1.52 Anexo Cargos Directos Articulos 1059
        this.dtoDiagnostico = new DtoDiagnostico();
		this.mostrarImprimirAutorizacion=false;
		this.listaNombresReportes= new ArrayList<String>();
		this.setListaNumerosSolicitud(new ArrayList<Integer>());
		//Fin Cambio Version 1.52 Anexo Cargos Directos Articulos 1059
	}
	
	/**
	 * Reset para limpiar las solicitudes 
	 */
	public void resetLimpiarNumeroSolicitudes(){
		this.setListaNumerosSolicitud(new ArrayList<Integer>());
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
	 * @return Returns the casoCuenta.
	 */
	public String getCasoCuenta() {
		return casoCuenta;
	}
	/**
	 * @param casoCuenta The casoCuenta to set.
	 */
	public void setCasoCuenta(String casoCuenta) {
		this.casoCuenta = casoCuenta;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public String getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the idCuentaAsociada.
	 */
	public String getIdCuentaAsociada() {
		return idCuentaAsociada;
	}
	/**
	 * @param idCuentaAsociada The idCuentaAsociada to set.
	 */
	public void setIdCuentaAsociada(String idCuentaAsociada) {
		this.idCuentaAsociada = idCuentaAsociada;
	}
	/**
	 * @return the profesionalesEjecutanArray
	 */
	public ArrayList<ArrayList<InfoDatosInt>> getProfesionalesEjecutanArray() {
		return profesionalesEjecutanArray;
	}

	/**
	 * @param profesionalesEjecutanArray the profesionalesEjecutanArray to set
	 */
	public void setProfesionalesEjecutanArray(
			ArrayList<ArrayList<InfoDatosInt>> profesionalesEjecutanArray) {
		this.profesionalesEjecutanArray = profesionalesEjecutanArray;
	}

	/**
	 * @return Returns the idSubCuenta.
	 */
	public String getIdSubCuenta() {
		return idSubCuenta;
	}
	/**
	 * @param idSubCuenta The idSubCuenta to set.
	 */
	public void setIdSubCuenta(String idSubCuenta) {
		this.idSubCuenta = idSubCuenta;
	}
	/**
	 * @return Returns the esLaAsociada.
	 */
	public boolean getEsLaAsociada() {
		return esLaAsociada;
	}
	/**
	 * @param esLaAsociada The esLaAsociada to set.
	 */
	public void setEsLaAsociada(boolean esLaAsociada) {
		this.esLaAsociada = esLaAsociada;
	}
	/**
	 * @return Returns the esLaDistribuida.
	 */
	public boolean getEsLaDistribuida() {
		return esLaDistribuida;
	}
	/**
	 * @param esLaDistribuida The esLaDistribuida to set.
	 */
	public void setEsLaDistribuida(boolean esLaDistribuida) {
		this.esLaDistribuida = esLaDistribuida;
	}
    /**
     * @return Returns the codigoFarmacia.
     */
    public int getCodigoFarmacia() {
        return codigoFarmacia;
    }
    /**
     * @param codigoFarmacia The codigoFarmacia to set.
     */
    public void setCodigoFarmacia(int codigoFarmacia) {
        this.codigoFarmacia = codigoFarmacia;
    }
    /**
     * @return Returns the codigoCentroCostoSolicita.
     */
    public int getCodigoCentroCostoSolicita() {
        return codigoCentroCostoSolicita;
    }
    /**
     * @param codigoCentroCostoSolicita The codigoCentroCostoSolicita to set.
     */
    public void setCodigoCentroCostoSolicita(int codigoCentroCostoSolicita) {
        this.codigoCentroCostoSolicita = codigoCentroCostoSolicita;
    }
    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
	 * @return the centrosCostoEjecutanArray
	 */
	public ArrayList<ArrayList<InfoDatosInt>> getCentrosCostoEjecutanArray() {
		return centrosCostoEjecutanArray;
	}

	/**
	 * @param centrosCostoEjecutanArray the centrosCostoEjecutanArray to set
	 */
	public void setCentrosCostoEjecutanArray(
			ArrayList<ArrayList<InfoDatosInt>> centrosCostoEjecutanArray) {
		this.centrosCostoEjecutanArray = centrosCostoEjecutanArray;
	}

	/**
     * @return Returns the hora.
     */
    public String getHora() {
        return hora;
    }
    /**
     * @param hora The hora to set.
     */
    public void setHora(String hora) {
        this.hora = hora;
    }
    /**
     * @return Returns the serviciosArticulosMap.
     */
    public HashMap getServiciosArticulosMap() {
        return serviciosArticulosMap;
    }
    /**
     * @param serviciosArticulosMap The serviciosArticulosMap to set.
     */
    public void setServiciosArticulosMap(HashMap serviciosArticulosMap) {
        this.serviciosArticulosMap = serviciosArticulosMap;
    }
	/**
	 * Set del mapa de N servicios o N articulos 
	 * @param key
	 * @param value
	 */
	public void setServiciosArticulosMap(String key, Object value) 
	{
		serviciosArticulosMap.put(key, value);
	}
	/**
	 * Get del mapa de N servicios o N articulos
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getServiciosArticulosMap(String key) 
	{
		return serviciosArticulosMap.get(key);
	}
    /**
     * @return Returns the nombreCentroCostoSolicita.
     */
    public String getNombreCentroCostoSolicita() {
        return nombreCentroCostoSolicita;
    }
    /**
     * @param nombreCentroCostoSolicita The nombreCentroCostoSolicita to set.
     */
    public void setNombreCentroCostoSolicita(String nombreCentroCostoSolicita) {
        this.nombreCentroCostoSolicita = nombreCentroCostoSolicita;
    }
    /**
     * @return Returns the numeroFilasMapa.
     */
    public int getNumeroFilasMapa() {
        if(this.serviciosArticulosMap.size()==0)
            return 0;
        else    
            return numeroFilasMapa;//(this.serviciosArticulosMap.size()/this.numeroColumnasMapaArticulos);
        }
    
    /**
     * @return Returns the numeroFilasMapaCasoServicios.
     */
    public int getNumeroFilasMapaCasoServicios() 
    {
    	int size= this.serviciosArticulosMap.size();
    	
    	if(this.serviciosArticulosMap.containsKey("idParaFocus"))
    		size= size-1;
    	
        if(size==0)
            return 0;
        else    
            return (size/this.numeroColumnasMapaServicios);
    }
    /**
     * @param numeroFilasMapa The numeroFilasMapa to set.
     */
    public void setNumeroFilasMapa(int numeroFilasMapa) {
        this.numeroFilasMapa = numeroFilasMapa;
    }
    
    /**
     * @param numeroFilasMapaCasoServicios The numeroFilasMapaCasoServicios to set.
     */
    public void setNumeroFilasMapaCasoServicios(int numeroFilasMapaCasoServicios) {
        this.numeroFilasMapaCasoServicios = numeroFilasMapaCasoServicios;
    }
    /**
     * @return Returns the acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada.
     */
    public String getAcronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada() {
        return acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada;
    }
    /**
     * @param acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada The acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada to set.
     */
    public void setAcronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada(
            String acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada) {
        this.acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada = acronimoTipoRegimenConvenioCuentaOSubcuentaSeleccionada;
    }
    /**
     * @return Returns the nombreViaIngresoCuenta.
     */
    public String getNombreViaIngresoCuenta() {
        return nombreViaIngresoCuenta;
    }
    /**
     * @param nombreViaIngresoCuenta The nombreViaIngresoCuenta to set.
     */
    public void setNombreViaIngresoCuenta(String nombreViaIngresoCuenta) {
        this.nombreViaIngresoCuenta = nombreViaIngresoCuenta;
    }
    /**
     * @return Returns the nombreViaIngresoCuentaAsociada.
     */
    public String getNombreViaIngresoCuentaAsociada() {
        return nombreViaIngresoCuentaAsociada;
    }
    /**
     * @param nombreViaIngresoCuentaAsociada The nombreViaIngresoCuentaAsociada to set.
     */
    public void setNombreViaIngresoCuentaAsociada(
            String nombreViaIngresoCuentaAsociada) {
        this.nombreViaIngresoCuentaAsociada = nombreViaIngresoCuentaAsociada;
    }
    /**
     * @return Returns the codigoViaIngresoCuenta.
     */
    public int getCodigoViaIngresoCuenta() {
        return codigoViaIngresoCuenta;
    }
    /**
     * @param codigoViaIngresoCuenta The codigoViaIngresoCuenta to set.
     */
    public void setCodigoViaIngresoCuenta(int codigoViaIngresoCuenta) {
        this.codigoViaIngresoCuenta = codigoViaIngresoCuenta;
    }
    /**
     * @return Returns the codigoViaIngresoCuentaAsociada.
     */
    public int getCodigoViaIngresoCuentaAsociada() {
        return codigoViaIngresoCuentaAsociada;
    }
    /**
     * @param codigoViaIngresoCuentaAsociada The codigoViaIngresoCuentaAsociada to set.
     */
    public void setCodigoViaIngresoCuentaAsociada(
            int codigoViaIngresoCuentaAsociada) {
        this.codigoViaIngresoCuentaAsociada = codigoViaIngresoCuentaAsociada;
    }
    /**
     * @return Returns the numeroSolicitudGenerado.
     */
    public String getNumeroSolicitudGenerado() {
        return numeroSolicitudGenerado;
    }
    /**
     * @param numeroSolicitudGenerado The numeroSolicitudGenerado to set.
     */
    public void setNumeroSolicitudGenerado(String numeroSolicitudGenerado) {
        this.numeroSolicitudGenerado = numeroSolicitudGenerado;
    }
    /**
     * @return Returns the codigoAxioma.
     */
    public String getCodigoAxioma() {
        return codigoAxioma;
    }
    /**
     * @param codigoAxioma The codigoAxioma to set.
     */
    public void setCodigoAxioma(String codigoAxioma) {
        this.codigoAxioma = codigoAxioma;
    }
    /**
     * @return Returns the codigoCups.
     */
    public String getCodigoCups() {
        return codigoCups;
    }
    /**
     * @param codigoCups The codigoCups to set.
     */
    public void setCodigoCups(String codigoCups) {
        this.codigoCups = codigoCups;
    }
    /**
     * @return Returns the codigoIss.
     */
    public String getCodigoIss() {
        return codigoIss;
    }
    /**
     * @param codigoIss The codigoIss to set.
     */
    public void setCodigoIss(String codigoIss) {
        this.codigoIss = codigoIss;
    }
    /**
     * @return Returns the codigoSoat.
     */
    public String getCodigoSoat() {
        return codigoSoat;
    }
    /**
     * @param codigoSoat The codigoSoat to set.
     */
    public void setCodigoSoat(String codigoSoat) {
        this.codigoSoat = codigoSoat;
    }
    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
	 * Tamanio de la coleccion
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
     * @return Returns the columna.
     */
    public String getColumna() {
        return columna;
    }
    /**
     * @param columna The columna to set.
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }
    /**
     * @return Returns the ultimaPropiedad.
     */
    public String getUltimaPropiedad() {
        return ultimaPropiedad;
    }
    /**
     * @param ultimaPropiedad The ultimaPropiedad to set.
     */
    public void setUltimaPropiedad(String ultimaPropiedad) {
        this.ultimaPropiedad = ultimaPropiedad;
    }
    /**
     * @return Returns the codigosServiciosInsertados.
     */
    public String getCodigosServiciosInsertados() {
        return codigosServiciosInsertados;
    }
    /**
     * @param codigosServiciosInsertados The codigosServiciosInsertados to set.
     */
    public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
        this.codigosServiciosInsertados = codigosServiciosInsertados;
    }    
    /**
     * @return Retorna fechaAdmisionOApertura.
     */
    public String getFechaAdmisionOApertura() {
        return fechaAdmisionOApertura;
    }
    /**
     * @param fechaAdmisionOApertura Asigna fechaAdmisionOApertura.
     */
    public void setFechaAdmisionOApertura(String fechaAdmisionOApertura) {
        this.fechaAdmisionOApertura = fechaAdmisionOApertura;
    }
    /**
     * @return Returns the codigoConvenioCuentaOSubcuentaSeleccionada.
     */
    public int getCodigoConvenioCuentaOSubcuentaSeleccionada() {
        return codigoConvenioCuentaOSubcuentaSeleccionada;
    }
    /**
     * @param codigoConvenioCuentaOSubcuentaSeleccionada The codigoConvenioCuentaOSubcuentaSeleccionada to set.
     */
    public void setCodigoConvenioCuentaOSubcuentaSeleccionada(
            int codigoConvenioCuentaOSubcuentaSeleccionada) {
        this.codigoConvenioCuentaOSubcuentaSeleccionada = codigoConvenioCuentaOSubcuentaSeleccionada;
    }
    /**
     * @return Returns the horaAdmisionOApertura.
     */
    public String getHoraAdmisionOApertura() {
        return horaAdmisionOApertura;
    }
    /**
     * @param horaAdmisionOApertura The horaAdmisionOApertura to set.
     */
    public void setHoraAdmisionOApertura(String horaAdmisionOApertura) {
        this.horaAdmisionOApertura = horaAdmisionOApertura;
    }
   
    /**
     * @return Returns the comentario.
     */
    public String getComentario() {
        return comentario;
    }
    /**
     * @param comentario The comentario to set.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    /**
     * @return Returns the consecutivoOrdenMedica.
     */
    public String getConsecutivoOrdenMedica() {
        return consecutivoOrdenMedica;
    }
    /**
     * @param consecutivoOrdenMedica The consecutivoOrdenMedica to set.
     */
    public void setConsecutivoOrdenMedica(String consecutivoOrdenMedica) {
        this.consecutivoOrdenMedica = consecutivoOrdenMedica;
    }
    /**
     * @return Returns the esCargosArticulosOServicios.
     */
    public String getEsCargosArticulosOServicios() {
        return esCargosArticulosOServicios;
    }
    /**
     * @param esCargosArticulosOServicios The esCargosArticulosOServicios to set.
     */
    public void setEsCargosArticulosOServicios(
            String esCargosArticulosOServicios) {
        this.esCargosArticulosOServicios = esCargosArticulosOServicios;
    }
    /**
     * Set del mapa del listado de los almacenes autorizados 
     * @param key
     * @param value
     */
    public void setListadoAlmacenesMap(String key, Object value) 
    {
        listadoAlmacenesMap.put(key, value);
    }
    /**
     * Get del mapa de N servicios o N articulos
     * Retorna el valor de un campo dado su nombre
     */
    public Object getListadoAlmacenesMap(String key) 
    {
        return listadoAlmacenesMap.get(key);
    }
    
    /**
     * @return Returns the listadoAlmacenesMap.
     */
    public HashMap getListadoAlmacenesMap() {
        return listadoAlmacenesMap;
    }
    /**
     * @param listadoAlmacenesMap The listadoAlmacenesMap to set.
     */
    public void setListadoAlmacenesMap(HashMap listadoAlmacenesMap) {
        this.listadoAlmacenesMap = listadoAlmacenesMap;
    }
    /**
     * @return Returns the nombreFarmacia.
     */
    public String getNombreFarmacia() {
        return nombreFarmacia;
    }
    /**
     * @param nombreFarmacia The nombreFarmacia to set.
     */
    public void setNombreFarmacia(String nombreFarmacia) {
        this.nombreFarmacia = nombreFarmacia;
    }
    /**
     * @return Returns the codigoInstitucion.
     */
    public int getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion The codigoInstitucion to set.
     */
    public void setCodigoInstitucion(int codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
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
	 * @return the mapaUtilitarioMap
	 */
	public HashMap getMapaUtilitarioMap() {
		return mapaUtilitarioMap;
	}

	/**
	 * @param mapaUtilitarioMap the mapaUtilitarioMap to set
	 */
	public void setMapaUtilitarioMap(HashMap mapaUtilitarioMap) {
		this.mapaUtilitarioMap = mapaUtilitarioMap;
	}
	
	/**
	 * @return the mapaUtilitarioMap
	 */
	public Object getMapaUtilitarioMap(String key) {
		return mapaUtilitarioMap.get(key);
	}

	/**
	 * @param mapaUtilitarioMap the mapaUtilitarioMap to set
	 */
	public void setMapaUtilitarioMap(String key,Object value) {
		this.mapaUtilitarioMap.put(key, value);
	}

	/**
	 * @return the arrayUtilitario1
	 */
	public ArrayList getArrayUtilitario1() {
		return arrayUtilitario1;
	}
	
	/**
	 * @return the arrayUtilitario1
	 */
	public ArrayList getArrayUtilitario1Pos(int pos) {
		return (ArrayList)arrayUtilitario1.get(pos);
	}

	/**
	 * @param arrayUtilitario1 the arrayUtilitario1 to set
	 */
	public void setArrayUtilitario1(ArrayList arrayUtilitario1) {
		this.arrayUtilitario1 = arrayUtilitario1;
	}

	/**
	 * @return the arrayUtilitario2
	 */
	public ArrayList getArrayUtilitario2() {
		return arrayUtilitario2;
	}
	
	/**
	 * @return the arrayUtilitario2
	 */
	public ArrayList getArrayUtilitario2Pos(int pos) {
		return (ArrayList)arrayUtilitario2.get(pos);
	}

	/**
	 * @param arrayUtilitario2 the arrayUtilitario2 to set
	 */
	public void setArrayUtilitario2(ArrayList arrayUtilitario2) {
		this.arrayUtilitario2 = arrayUtilitario2;
	}

	public String getMensajeJustificacion() {
		return mensajeJustificacion;
	}

	public void setMensajeJustificacion(String mensajeJustificacion) {
		this.mensajeJustificacion = mensajeJustificacion;
	}

	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}

	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}
	
	public Object getJustificacionNoPosMap(String key) {
		return justificacionNoPosMap.get(key);
	}

	public void setJustificacionNoPosMap(String key,Object value) {
		this.justificacionNoPosMap.put(key, value);
	}

	/**
	 * @return the indicadorPosCuentas
	 */
	public String getIndicadorPosCuentas() {
		return indicadorPosCuentas;
	}

	/**
	 * @param indicadorPosCuentas the indicadorPosCuentas to set
	 */
	public void setIndicadorPosCuentas(String indicadorPosCuentas) {
		this.indicadorPosCuentas = indicadorPosCuentas;
	}

	public String getFechaEjeccuion() {
		return fechaEjeccuion;
	}

	public void setFechaEjeccuion(String fechaEjeccuion) {
		this.fechaEjeccuion = fechaEjeccuion;
	}

	public int getNumConveniosPlanEspecial() {
		return numConveniosPlanEspecial;
	}

	public void setNumConveniosPlanEspecial(int numConveniosPlanEspecial) {
		this.numConveniosPlanEspecial = numConveniosPlanEspecial;
	}

	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}

	/**
	 * @return the especialidadProfesionalesEjecutanArray
	 */
	public ArrayList<ArrayList<InfoDatosInt>> getEspecialidadProfesionalesEjecutanArray() {
		return especialidadProfesionalesEjecutanArray;
	}

	/**
	 * @param especialidadProfesionalesEjecutanArray the especialidadProfesionalesEjecutanArray to set
	 */
	public void setEspecialidadProfesionalesEjecutanArray(
			ArrayList<ArrayList<InfoDatosInt>> especialidadProfesionalesEjecutanArray) {
		this.especialidadProfesionalesEjecutanArray = especialidadProfesionalesEjecutanArray;
	}

	/**
	 * @return the codigoMedicoResponde
	 */
	public String getCodigoMedicoResponde() {
		return codigoMedicoResponde;
	}

	/**
	 * @param codigoMedicoResponde the codigoMedicoResponde to set
	 */
	public void setCodigoMedicoResponde(String codigoMedicoResponde) {
		this.codigoMedicoResponde = codigoMedicoResponde;
	}

	/**
	 * @return Retorna el atributo mostrarImprimirAutorizacion
	 */
	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}

	/**
	 * @param mostrarImprimirAutorizacion Asigna el atributo mostrarImprimirAutorizacion
	 */
	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}

	/**
	 * @return Retorna el atributo listaNombresReportes
	 */
	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	/**
	 * @param listaNombresReportes Asigna el atributo listaNombresReportes
	 */
	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	/**
	 * @return Retorna el atributo dtoDiagnostico
	 */
	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	/**
	 * @param dtoDiagnostico Asigna el atributo dtoDiagnostico
	 */
	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}

	public void setListaNumerosSolicitud(List<Integer> listaNumerosSolicitud) {
		this.listaNumerosSolicitud = listaNumerosSolicitud;
	}

	public List<Integer> getListaNumerosSolicitud() {
		return listaNumerosSolicitud;
	}

	/**
	 * @return infoCoberturaCargoDirecto
	 */
	public List<InfoResponsableCobertura> getInfoCoberturaCargoDirecto() {
		return infoCoberturaCargoDirecto;
	}

	/**
	 * @param infoCoberturaCargoDirecto
	 */
	public void setInfoCoberturaCargoDirecto(
			List<InfoResponsableCobertura> infoCoberturaCargoDirecto) {
		this.infoCoberturaCargoDirecto = infoCoberturaCargoDirecto;
	}

	
}