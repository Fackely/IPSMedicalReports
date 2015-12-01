/*
 * @(#)SolicitudesCxForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.ordenesmedicas.cirugias;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;

/**
 * Form que contiene todos los datos específicos para generar 
 * las solicitudes de cirugias
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Oct 31 , 2005
 * @author wrios 
 */
public class SolicitudesCxForm extends ValidatorForm 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Indica el caso de flujo=
     * 1->No existe petición de cirugía
     * 2->Existe una petición de cirugía
     * 3->Existe mas de una petición de cirugía 
     */
    private int casoFlujo;
    
    /**
     * numero de solicitud 
     */
    private String numeroSolicitud;
    
    /**
     * Se guarda la información necesaria para guardar la justificacion de cada servicio No POS
     */
    private HashMap justificacionesServicios; 
    
    /**
     * codigo de la peticion
     */
    private String codigoPeticion;
    
    /**
     * Estado en el que se encuentra el proceso.       
     */
     private String estado;
    
    //INFORMACION DE LA SOLICITUD****************
    /**
     * fecha de la solicitud
     */
    private String fechaSolicitud;
 
    /**
     *hora de la solicitud
     */
    private String horaSolicitud;
    
    /**
     * codigo  - centro costo solicitado
     */
    private int codigoCentroCostoSolicitado;
    
    /**
     * nombre del cc solicitado
     */
    private String nombreCentroCostoSolicitado;
    
    /**
     * autorizacion
     */
    //private String autorizacion;
    
    /**
     * codigo -  especialidad que ordena 
     */
    private int codigoEspecialidadOrdena;
    
    /**
     * nombre -  especialidad que ordena 
     */
    private String nombreEspecialidadOrdena;
    
    /**
     * indicativo de urgente o no
     */
    private boolean urgente;
    
    /**
     * Se carga el codigo del medico que solicita
     */
    private int codigoMedicoSolicita;
    
    /**
     * consecutivo de la orden medica
     */
    private String consecutivoOrdenMedica;
    
    //**********************************************************
    
    //INFORMACION DE LA PETICION*******************
    
    /**
     * fecha estimada de la cirugia
     */
    private String fechaEstimadaCirugia;
    
    /**
     * duracion estimada de la cirugia
     */
    private String duracionAproximadaCirugia;
    
    /**
     * "false";
     * "true";
     * ""->No sel
     */
    private String requiereUci;
    
    /**
     * 0 --> pendiente
     */
    private boolean puedoModificarPeticion;
    
    
    //***********************************************************
    
    //INFORMACION DE LA BUSQUEDA AVANZADA SERVICIOS
   
   /**
    * codigos de los servicios insertados para no repetirlos
    * en la busqueda avanzada de servicios
    */
   private String codigosServiciosInsertados;
     
   /**
   * Mapa que contiene los N servicios deL LISTADO
   */
   private HashMap serviciosMap= new HashMap();
   
   /**
    * Número de filas existentes en el hashmap de servicios
    */
   private int numeroFilasMapaServicios;
   
   /**
    * Numero de columnas del mapa servicios
    */
   private final static int numeroColumnasMapaServicios=16;
   
    //***********************************************************
    
    //INFORMACION DE LOS PROFESIONALES DE LA SALUD
    // ESTOS DATOS VIENEN DE LA PETICION
    /**
     * Mapa que contiene los N servicios deL LISTADO
     */
    private HashMap profesionalesMap= new HashMap();
   
    //************************************************************
    
    //INFORMACION DE LOS MATERIALES ESPECIALES
    
    /**
     * Mapa que contiene los N articulos deL LISTADO
     */
    private HashMap articulosMap= new HashMap();
   
    /**
     * Número de filas existentes en el hashmap 
     */
    private int numeroFilasMapaArticulos;
    
    /**
     * Mapa que contiene los N otros articulos NO PARAMETRIDOS
     */
    private HashMap otrosArticulosMap= new HashMap();
    
    /**
     * numero de filas existentes en el mpa
     */
    private int numeroFilasMapaOtrosArticulos;
    
    /** 
    * Numero de columnas del mapa articulos
    */
   private final static int numeroColumnasMapaArticulos=6;
   
   /**
    * numero de columnas de los OTROS articulos
    */
   private final static int numeroColumnasMapaOtrosArticulos=3;
       
    //************************************************************
    
    /**
     * almacena la fecha de la AdmisionOApertura 
     * sea de urgencias o hospitalizacion.
     */
    private String fechaAdmisionOApertura;
    
    /**
     * almacena la hora de la primera AdmisionOApertura
     */
    private String horaAdmisionOApertura;
    
    /**
     * comentario de los errores en el validate
     */
    private String comentario;
    
    /**
     * mensaje que pretende advertir al usuario de 
     * la existencia de una orden
     */
    private Vector mensajeAdvertenciaVector= new Vector();
    
    /**
     * Colección con los datos del listado, del encabezado de la peticion
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
     * codigo del motivo de anulacion de la solicitud  cx x institucion
     */
    private int codigoMotivoAnulacionCxXInstitucion;
    
    /**
     * nombre del motivo de anulacion de la solicitud  cx x institucion
     */
    private String nombreMotivoAnulacionCxInstitucion;
    
    /**
     * comentarios de la anulacion de la solicitud  cx
     */
    private String comentariosAnulacion;
    
    //******************respuesta a centro costo externos
    
    /**
     * resultados de la espuesta de la sol cx a centro de costo externo
     */
    private String resultadosRespuestaSolCx;
    
    //******************tipoBusquedaBotonVolver
    /**
     * tipo de busqueda para el link del boton volver
     */
    private String tipoBusquedaBotonVolver;
    
    /**
     * mapa que contiene las solicitudes pendientes 
     */
    private HashMap solicitudesPendientesMap= new HashMap();
    
    //***********ordenar por el mapa
    /**
     * patron de ordenamineto
     */
    private String patronOrdenar;
    
    /**
     * ultimo patyron de ordenamiento
     */
    private String ultimoPatron;
    
    //******atributo para el resumen de atenciones********
    
    /**
     * si es 1 => es resumen de atenciones
     * si es 0 => no es resumen de atenciones
     */
    private int indicador;
    
    /**
     * Código del Estado Historia Clinica de la solicitud
     */
    private int codigoEstadoMedico;
	
    /**
     * Datos de la anulación
     */
    private HashMap datosAnulacion;
	//*****************************************
    
    /**
     * muestra o no el link de volver al listado peticiones
     */
    private boolean mostrarLinkVolverListadoPeticiones;
    
    
    /**
     * Campo para Saber el numero de la orden medica en el momento de
     * Anular una Orden De cirugia.  
     */
    private int nroOrdenMedica; 
    
	/**
	 * boolean que indica si se debe postular un servicio
	 */
	private boolean postularServicio=false;
	
	/**
	 * codigo del servicio a postular
	 */
	private int codigoServicioPostular;
	
	/**
	 * indicativo que indica si la solicitud es de pyp.
	 */
	private boolean solPYP=false;
	
	/**
	 * centros atencion que envian desde pyp sepados por @@@@
	 */
	private String centrosAtencionPYP="";
	
	private ArrayList tiposAnestesia;
	
	private String nombreTipoAnestesia;
	
	private int tipoAnestesia;
	
	private String justificar;
	
	/**
	 * Dto de articulos incluidos solicitud de procedimiento
	 * */
	private ArrayList <DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto ;
	
	/**
	 * HashMap de Justificacion de los articulos Incluidos
	 * */
	private HashMap justificacionMap;
	
	/**
	 * 
	 * */
	private String hiddens;
	
	/**
	 * 
	 * */
	private String cadenaFiltroBusqueda = "";
	
	/**
	 * 
	 * */
	private String justificacionSolicitud;
	
	/**
	 * 
	 * */
	private String justificacionSolicitudNueva;
	
	private int codSexo;
	
	/**
	 * Este atributo se usa para determinar cuando se generó una 
	 * autorización de solicitudes y mostrar el respectivo botón
	 */
	private boolean mostrarImprimirAutorizacion;
	
	/**
	 * lista que contiene los nombres de los reportes de las autorzaciones 
	 * 
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**
	 * Atributo que contiene el diagnóstico y tipo CIE del paciente
	 */
	private DtoDiagnostico dtoDiagnostico;
	
	/**Lista que almacena los servicios que se asignaron en la cita*/
	private ArrayList<String> listaServiciosImpimirOrden;
		
	/**Atributo que se encarga de obtener los datos de la cobertura de la cirugia*/
	private ArrayList<InfoResponsableCobertura> listaInfoRespoCoberturaCx;
	
	/**Atributo que indica si la cirugia qeu se genera se esta asociando a una peticion*/
	private boolean cirugiaAsociadaPeticion;
	
    /**
	 * Lista almacena el codigo de los profesionales y las especialidades
	 */
	private ArrayList<ProfesionalEspecialidadesDto> profesionalEspecialidades;
	
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
        if(estado.equals("guardar") || estado.equals("guardarModificar"))
        {               
            errores=super.validate(mapping,request);
            if(estado.equals("guardar"))
	        { 
	            if(this.getFechaSolicitud().equals(""))
	            {
	                errores.add("Campo Fecha Solicitud vacio", new ActionMessage("errors.required","El campo Fecha Solicitud"));
	            }
	            else
	            {
	                if(!UtilidadFecha.validarFecha(this.getFechaSolicitud()))
	                {
	                    errores.add("Fecha Solicitud", new ActionMessage("errors.formatoFechaInvalido", this.getFechaSolicitud()+" de la solicitud"));                         
	                }
	                else 
	                {   
	                    if(this.getHoraSolicitud().equals(""))
	                    {
	                        errores.add("Campo Hora Solicitud vacio", new ActionMessage("errors.required","El campo Hora Solicitud"));
	                    }
	                    else if (UtilidadFecha.validacionHora(this.getHoraSolicitud()).puedoSeguir)
	                    {
	                        if ( !  UtilidadFecha.compararFechas(  UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFechaSolicitud(), this.getHoraSolicitud()  ).isTrue()  )
	                            errores.add("Fecha Solicitud", new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaSolicitud()+" "+this.getHoraSolicitud()+" de la solicitud ", UtilidadFecha.getFechaActual()+ " "+UtilidadFecha.getHoraActual()+" actual"));
	                    }
	                    else
	                    {
	                        errores.add("Hora Solicitud", new ActionMessage("errors.formatoHoraInvalido", this.getHoraSolicitud()+" de la solicitud"));
	                    }
	                    //si todavia no hay errores entonces hacer la comparacion con la AdmisionOApertura
	                    if(errores.isEmpty())
	                    {
	                        if (!UtilidadFecha.compararFechas(  this.getFechaSolicitud(), this.getHoraSolicitud(), this.getFechaAdmisionOApertura(), this.getHoraAdmisionOApertura()  ).isTrue()  )
	                            errores.add("Fecha Solicitud", new ActionMessage("errors.fechaHoraAnteriorIgualActual",this.getFechaSolicitud()+" "+this.getHoraSolicitud()+ " de la solicitud ", this.getFechaAdmisionOApertura()+" "+this.getHoraAdmisionOApertura()+" "+this.getComentario()));
	                    }
	                }
	            }
	            
	            if(this.getCodigoCentroCostoSolicitado()<=0)
	            {
	                errores.add("Campo Centro Costo Solicitado vacio", new ActionMessage("errors.required","El campo Centro Costo Solicitado"));
	            }
	            //no se toma el 0 y -1 debido a que 0= todas y -1= Ninguna
	            if(this.getCodigoEspecialidadOrdena()==-2)
	            {
	                errores.add("Campo Especialidad Ordena vacio", new ActionMessage("errors.required","El campo Especialidad Ordena"));
	            }
	            
	            // si (no existen errores en la solicitud basica) entonces evaluar la información de cirugia
	            if(errores.isEmpty())
	            {
	                if(!this.getFechaEstimadaCirugia().trim().equals(""))
	                {    
	                    if(!UtilidadFecha.validarFecha(this.getFechaEstimadaCirugia()))
	                    {
	                        errores.add("Fecha Estimada Cx", new ActionMessage("errors.formatoFechaInvalido", this.getFechaEstimadaCirugia()+" estimada de Cx"));                         
	                    }
	                    else 
	                    {   
	                        if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaEstimadaCirugia(), this.getFechaSolicitud()))
	                        {
	                            errores.add("Fecha Estimada Cx", new ActionMessage("errors.fechaAnteriorIgualActual","estimada Cx "+this.getFechaEstimadaCirugia(), this.getFechaSolicitud()+" de la solicitud"));
	                        }
	                    }
	                }
	                if(!this.getDuracionAproximadaCirugia().trim().equals(""))
	                {    
	                    if (!UtilidadFecha.validacionHora(this.getDuracionAproximadaCirugia()).puedoSeguir)
	                    {
	                        errores.add("Duracion Aproximada Cx", new ActionMessage("errors.formatoHoraInvalido", this.getDuracionAproximadaCirugia()));
	                    }
	                    else
	                    {
	                        if(this.getDuracionAproximadaCirugia().equals("00:00"))
	                        {
	                            errores.add("Duracion Aproximada Cx", new ActionMessage("errors.horaDebeSerMayor", "de Duración aproximada Cx", this.getDuracionAproximadaCirugia()));
	                        }
	                    }
	                }    
	            }
	            
	            ///VALIDACIONES DE LOS SERVICIOS (TOCA EVALUAR QUE EXISTA UN SERVICIO)
	            
	            if(this.getNumeroFilasMapaServicios()<=0)
	            {
	                errores.add("Campo Servicio vacio", new ActionMessage("errors.required","El/los Servicio(s) Cx "));
	            }
	            else
	            {
	            	// SE VERIFICA SI SE HA INGRESADO JUSTIFICACIÒN NO POS PARA EL SERVICIO EN CASO DE QUE LO REQUIERA
	            	int numJustPendientes = 0;
	            	for(int i=0; i<this.getNumeroFilasMapaServicios(); i++) {
	            		if (this.getServiciosMap().containsKey("justificar_"+i)){
			            	if (this.getServiciosMap("justificar_"+i).toString().equals("true") && !(this.getServiciosMap("fueEliminadoServicio_"+i)+"").equals("true")){
			                	//if(!this.getJustificacionesServicios().containsKey(i+"_servicio"))
			            		if(!UtilidadTexto.getBoolean(justificacionesServicios.get(serviciosMap.get("codigoServicio_"+i)+"_yajustifico"))){
			            			errores.add("No se ha diligenciado el formato de justificación No POS", new ActionMessage("errors.required","Justificación No POS para el servicio "+this.getServiciosMap("descripcionServicio_"+i)));
			            		}
			                }
			            	if (this.getServiciosMap("justificar_"+i).toString().equals("pendiente")){
			            		numJustPendientes++;
			            	}
	            		}	
	            	}
	            	if (numJustPendientes==1 && this.getNumeroFilasMapaServicios()==1)
	            		errores.add("No puede solicitar el servicio No POS", new ActionMessage("errors.warnings","No puede solicitar el servicio No POS "+this.getServiciosMap("descripcionServicio_"+0)+" Ya que no cumple con la validacion médico especialista"));
	            	
	                boolean sonTodosEliminados=true;
	                
	                for(int i=0; i<this.getNumeroFilasMapaServicios(); i++)
	                {
	                    if(!(this.getServiciosMap("fueEliminadoServicio_"+i)+"").equals("true"))
	                    {   
	                        sonTodosEliminados=false;
	                        i=this.getNumeroFilasMapaServicios();
	                    } 
	                }
	                if(sonTodosEliminados)
	                {
	                    errores.add("Campo Servicio vacio", new ActionMessage("errors.required","El/los Servicio(s) Cx "));
	                }
	                else
	                {
	                	//*********SE VERIFICA QUE HAYA COMO MÍNIMO UN SERVICIO DE TIPO QUIRURGICO Y/O PARTOS Y CESÁREAS***************
	                	Connection con = UtilidadBD.abrirConexion();
	                	int numServQx = this.numeroServicioQxYPartosEncontrados(con);
	                	UtilidadBD.closeConnection(con);
	                	if(numServQx==0)
	                		errores.add("",new ActionMessage("errors.minimoCampos","la selección de un servicio quirúrgico o partos y cesáreas","solicitud de cirugías"));
	                	//*]***************************************************************
	                	
	                }
	            }
	            
	            int numeroProfesionales=Integer.parseInt(this.profesionalesMap.get("numeroProfesionales")+"");
	            for(int i=0; i<numeroProfesionales;i++)
	            {
	            	/*
	            	 * Ya se debe permitir ingresar profesional sin especialidad
	            	if((this.profesionalesMap.get("especialidades_"+i)+"").equals("-1"))
	                {
	            		errores.add("Especialidaes", new ActionMessage("errors.required","La especialidad de los profesionales"));
	                }*/
	            	
	            	if((this.profesionalesMap.get("tipo_participante_"+i)+"").equals("0"))
	                {
	            		errores.add("TipoPart", new ActionMessage("errors.required","El tipo de profesional de los profesionales"));
	                }
	            }
	            Vector codigos=new Vector();
	            for(int i=0; i<numeroProfesionales;i++)
	            {
	                String valor=this.profesionalesMap.get("profesional_"+i)+"";
	                if(codigos.contains(valor))
	                {
	                    errores.add("Error Profesional Repetido", new ActionMessage("error.peticionesCirugias.profesionalRepetido"));
	                    break;
	                }
	                codigos.add(valor);
	            }
	            
	            for(int i=0; i<this.getNumeroFilasMapaArticulos(); i++)
	            {
	                String codigoArticuloTemp="";
	                if(!(this.getArticulosMap("fueEliminadoArticulo_"+i)+"").equals("true"))
	                {    
	                    try
	                    {
	                        codigoArticuloTemp= this.getArticulosMap("codigoArticulo_"+i)+"";
	                        Integer.parseInt(this.getArticulosMap("cantidadDespachadaArticulo_"+i)+"");
	                        if(Integer.parseInt(this.getArticulosMap("cantidadDespachadaArticulo_"+i)+"")==0)
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
	                }    
	            }
	            
	            for(int i=0; i<this.getNumeroFilasMapaOtrosArticulos(); i++)
	            {
	                String descArticuloTemp="";
	                if(!(this.getOtrosArticulosMap("fueEliminadoOtrosArticulo_"+i)+"").equals("true"))
	                {    
	                    try
	                    {
	                        descArticuloTemp= this.getOtrosArticulosMap("descripcionOtrosArticulo_"+i)+"";
	                        Integer.parseInt(this.getOtrosArticulosMap("cantdesotrosarticulos_"+i)+"");
	                        if(Integer.parseInt(this.getOtrosArticulosMap("cantdesotrosarticulos_"+i)+"")==0)
	                        {
	                            errores.add("Campo cantidad", new ActionMessage("errors.integerMayorQue","El campo Cantidad para el Artículo "+descArticuloTemp, "0"));
	                        }
	                    }
	                    catch(NullPointerException ne)
	                    {
	                        errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Artículo "+descArticuloTemp));
	                    }
	                    catch(NumberFormatException nfe)
	                    {
	                        errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Artículo "+descArticuloTemp));
	                    }
	                }    
	            }
	        } 
    }
        if(!errores.isEmpty())
        {
            if(estado.equals("guardar"))
                this.setEstado("continuarPaginaPrincipal");
            else if(estado.equals("guardarModificar"))
                this.setEstado("continuarPaginaModificar");
        }
        return errores;
    }  
    
    
    /**
     * Método que retorna el número de servicios qx encontrados
     * @param con
     * @return
     */
    public int numeroServicioQxYPartosEncontrados(Connection con)
    {
    	String tipoServicio = "";
    	int numServQx = 0;
    	//SE verifica que se haya insertado como mínimo un servicio Quirurgico o Partos y Cesáreas
    	for(int i=0; i<this.getNumeroFilasMapaServicios(); i++)
        {
            if(this.getServiciosMap().containsKey("fueEliminadoServicio_"+i) && 
            		!UtilidadTexto.getBoolean(this.getServiciosMap("fueEliminadoServicio_"+i).toString()))
            {
            	tipoServicio = Utilidades.obtenerTipoServicio(con, this.getServiciosMap("codigoServicio_"+i).toString());
            	if(tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")||tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+""))
    				numServQx ++;
            }
        }
    	
    	return numServQx;
    }
    
    /**
     * Resetea los valores de la forma,
     * datos generales de la solicitud
     */
    public void reset()
    {
        this.casoFlujo= ConstantesBD.codigoNuncaValido;
        this.numeroSolicitud="";
        this.codigoPeticion="";
        
        this.fechaSolicitud="";
        this.horaSolicitud="";
        this.codigoCentroCostoSolicitado=ConstantesBD.codigoNuncaValido;
        this.nombreCentroCostoSolicitado="";
        //this.autorizacion="";
        this.codigoEspecialidadOrdena= ConstantesBD.codigoNuncaValido;
        this.nombreEspecialidadOrdena="";
        this.urgente= false;
        this.codigoMedicoSolicita=ConstantesBD.codigoNuncaValido;
        this.consecutivoOrdenMedica="";
        
        this.fechaAdmisionOApertura = "";
        this.horaAdmisionOApertura="";
        this.comentario="";
        
        this.mensajeAdvertenciaVector=new Vector();
        
        this.solicitudesPendientesMap= new HashMap();
        this.ultimaPropiedad="";
        this.patronOrdenar="";
        
        //datos para el resumen de atenciones
        this.indicador = 0;
        this.codigoEstadoMedico = -1;
        this.datosAnulacion = new HashMap();
        this.mostrarLinkVolverListadoPeticiones=false;
        this.tiposAnestesia=new ArrayList();
        this.tipoAnestesia=ConstantesBD.codigoNuncaValido;
        this.nombreTipoAnestesia="";
        this.justificar="";
        
        this.arrayArticuloIncluidoDto = new ArrayList<DtoArticuloIncluidoSolProc>();
        this.justificacionMap = new HashMap();
        this.hiddens = "";
        this.justificacionSolicitud = "";
        this.justificacionSolicitudNueva = "";
        this.codSexo=0;
        this.dtoDiagnostico = new DtoDiagnostico();
        
        this.listaInfoRespoCoberturaCx	= new ArrayList<InfoResponsableCobertura>();
		this.profesionalEspecialidades = new ArrayList<ProfesionalEspecialidadesDto>();
    }
    
    /**
     * Método que se encarga de inicializar los valores respecto a la 
     * validación del DCU 174 Cambio->1.52 Descripción del servicio +Acronimo
     * del grupo de servicios
     */
    public void resetServicios()
    {
    	this.listaServiciosImpimirOrden=new ArrayList<String>();
    }
    
    /**
     * resetea los valores de la forma concernientes a 
     * la peticion
     *
     */
    public void resetInfoPeticion()
    {
        this.fechaEstimadaCirugia="";
        this.duracionAproximadaCirugia="";
        this.requiereUci="";
        this.puedoModificarPeticion=true;
    }
    
    /**
     * resetea la info de la busdqueda de servicios
     *
     */
    public void resetBusquedaServicios()
    {
        this.serviciosMap= new HashMap();
        this.numeroFilasMapaServicios= ConstantesBD.codigoNuncaValido;
        this.codigosServiciosInsertados="";
    }
    
    /**
     * resetea la info de la busdqueda de profesionales
     *
     */
    public void resetBusquedaProfesionales()
    {
        this.profesionalesMap= new HashMap();
    }
    
    /**
     * resetea la info de la busdqueda de materiales
     *
     */
    public void resetBusquedaArticulos()
    {
        this.articulosMap= new HashMap();
        this.numeroFilasMapaArticulos= ConstantesBD.codigoNuncaValido;
        this.otrosArticulosMap= new HashMap();
        this.numeroFilasMapaOtrosArticulos= ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * resetea toda la info de la forma
     *
     */
    public void resetTodo()
    {
        this.reset();
        this.resetBusquedaArticulos();
        this.resetBusquedaProfesionales();
        this.resetBusquedaServicios();
        this.resetInfoPeticion();
    }
    
    public void resetAnulacion()
    {
        this.comentariosAnulacion="";
        this.codigoMotivoAnulacionCxXInstitucion=ConstantesBD.codigoNuncaValido;
        this.nombreMotivoAnulacionCxInstitucion="";
    }
    
    


	public int getCodSexo() {
		return codSexo;
	}


	public void setCodSexo(int codSexo) {
		this.codSexo = codSexo;
	}


	/**
     * @return Returns the articulosMap.
     */
    public HashMap getArticulosMap() {
        return articulosMap;
    }
    /**
     * @param articulosMap The articulosMap to set.
     */
    public void setArticulosMap(HashMap articulosMap) {
        this.articulosMap = articulosMap;
    }
    /**
     * Set del mapa de rticulos 
     * @param key
     * @param value
     */
    public void setArticulosMap(String key, Object value) 
    {
        articulosMap.put(key, value);
    }
    /**
     * Get del mapa de N articulos
     * Retorna el valor de un campo dado su nombre
     */
    public Object getArticulosMap(String key) 
    {
        return articulosMap.get(key);
    }
    /**
     * Set del mapa de otrosArticulos 
     * @param key
     * @param value
     */
    public void setOtrosArticulosMap(String key, Object value) 
    {
        otrosArticulosMap.put(key, value);
    }
    /**
     * Get del mapa de N otros articulos
     * Retorna el valor de un campo dado su nombre
     */
    public Object getOtrosArticulosMap(String key) 
    {
        return otrosArticulosMap.get(key);
    }
    /**
     * @return Returns the autorizacion.
     */
    /* 
    public String getAutorizacion() {
        return autorizacion;
    }
    */
    /**
     * @param autorizacion The autorizacion to set.
     */
    /*
    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }
    */
    /**
     * @return Returns the codigoPeticion.
     */
    public String getCodigoPeticion() {
        return codigoPeticion;
    }
    /**
     * @param codigoPeticion The codigoPeticion to set.
     */
    public void setCodigoPeticion(String codigoPeticion) {
        this.codigoPeticion = codigoPeticion;
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
     * @return Returns the duracionAproximadaCirugia.
     */
    public String getDuracionAproximadaCirugia() {
        return duracionAproximadaCirugia;
    }
    /**
     * @param duracionAproximadaCirugia The duracionAproximadaCirugia to set.
     */
    public void setDuracionAproximadaCirugia(String duracionAproximadaCirugia) {
        this.duracionAproximadaCirugia = duracionAproximadaCirugia;
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
     * @return Returns the fechaEstimadaCirugia.
     */
    public String getFechaEstimadaCirugia() {
        return fechaEstimadaCirugia;
    }
    /**
     * @param fechaEstimadaCirugia The fechaEstimadaCirugia to set.
     */
    public void setFechaEstimadaCirugia(String fechaEstimadaCirugia) {
        this.fechaEstimadaCirugia = fechaEstimadaCirugia;
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
     * @return Returns the horaSolicitud.
     */
    public String getHoraSolicitud() {
        return horaSolicitud;
    }
    /**
     * @param horaSolicitud The horaSolicitud to set.
     */
    public void setHoraSolicitud(String horaSolicitud) {
        this.horaSolicitud = horaSolicitud;
    }
    /**
     * @return Returns the numeroSolicitud.
     */
    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }
    /**
     * @param numeroSolicitud The numeroSolicitud to set.
     */
    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
    /**
     * @return Returns the profesionalesMap.
     */
    public HashMap getProfesionalesMap() {
        return profesionalesMap;
    }
    /**
     * @param profesionalesMap The profesionalesMap to set.
     */
    public void setProfesionalesMap(HashMap profesionalesMap) {
        this.profesionalesMap = profesionalesMap;
    }
    /**
     * Set del mapa de profesionales 
     * @param key
     * @param value
     */
    public void setProfesionalesMap(String key, Object value) 
    {
        profesionalesMap.put(key, value);
    }
    /**
     * Get del mapa de N profesionales
     * Retorna el valor de un campo dado su nombre
     */
    public Object getProfesionalesMap(String key) 
    {
        return profesionalesMap.get(key);
    }
    /**
     * @return Returns the requiereUci.
     */
    public String getRequiereUci() {
        return requiereUci;
    }
    /**
     * @param requiereUci The requiereUci to set.
     */
    public void setRequiereUci(String requiereUci) {
        this.requiereUci = requiereUci;
    }
    /**
     * @return Returns the serviciosMap.
     */
    public HashMap getServiciosMap() {
        return serviciosMap;
    }
    /**
     * @param serviciosMap The serviciosMap to set.
     */
    public void setServiciosMap(HashMap serviciosMap) {
        this.serviciosMap = serviciosMap;
    }
    /**
     * Set del mapa de servicios 
     * @param key
     * @param value
     */
    public void setServiciosMap(String key, Object value) 
    {
        serviciosMap.put(key, value);
    }
    /**
     * Get del mapa de N servicios
     * Retorna el valor de un campo dado su nombre
     */
    public Object getServiciosMap(String key) 
    {
        return serviciosMap.get(key);
    }
    /**
     * @return Returns the urgente.
     */
    public boolean getUrgente() {
        return urgente;
    }
    /**
     * @param urgente The urgente to set.
     */
    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }
    /**
     * @return Returns the numeroFilasMapaArticulos.
     */
    public int getNumeroFilasMapaArticulos() 
    {
//        int numFil=0;
//        if(this.articulosMap.size()==0)
//            numFil= 0;
//        else
//        {
//            //si el numColumnas es nulo eso quiere decir que no se pudo hacer el put desde la consulta  
//            //de este mapa entonces toca indicarle el tamanio, pero se debe tener en cuenta que 
//            //esto no sucede cuando se carga de la peticion o de la orden porque ya Existe un cargar previo.
//            if((this.getArticulosMap("numColumnas")==null) ||  (this.getArticulosMap("numColumnas")+"").equals("null"))
//                numFil= ((this.articulosMap.size())/numeroColumnasMapaArticulos);
//            else
//            {
//                int numeroFilasBD= Integer.parseInt(this.getArticulosMap("numRegistros")+"");
//                int numCol=(Integer.parseInt(this.getArticulosMap("numColumnas")+""));
//                int tamanioYaContadoEnFilasBD= numeroFilasBD*numCol;
//                numFil= (((this.articulosMap.size()-tamanioYaContadoEnFilasBD-1)/numeroColumnasMapaArticulos)+(numeroFilasBD));
//            }
//        }
//        return numFil;
    	
    	
    	//MT 6891 - se elimina todo lo anterior ya que cuando se agregaban
    	//datos al mapa, la division para saber el numero de registros no era exacta  
    	int i;
    	for (i = 0; i < getArticulosMap().size(); i++)
    	{
			if(getArticulosMap().get("codigoArticulo_"+i)==null)
			{
				break;
			}
		}
    	return i;
    }
    /**
     * @return Returns the numeroFilasMapaOtrosArticulos.
     */
    public int getNumeroFilasMapaOtrosArticulos() 
    {
//        int numFil=0;
//        if(this.otrosArticulosMap.size()==0)
//            numFil= 0;
//        else
//        {
//            //si el numColumnas es nulo eso quiere decir que no se pudo hacer el put desde la consulta  
//            //de este mapa entonces toca indicarle el tamanio, pero se debe tener en cuenta que 
//            //esto no sucede cuando se carga de la peticion o de la orden porque ya Existe un cargar previo.
//            if((this.getOtrosArticulosMap("numColumnas")==null) ||  (this.getOtrosArticulosMap("numColumnas")+"").equals("null"))
//                numFil= ((this.otrosArticulosMap.size())/numeroColumnasMapaOtrosArticulos);
//            else
//            {
//                int numeroFilasBD= Integer.parseInt(this.getOtrosArticulosMap("numRegistros")+"");
//                int numCol=(Integer.parseInt(this.getOtrosArticulosMap("numColumnas")+""));
//                int tamanioYaContadoEnFilasBD= numeroFilasBD*numCol;
//                numFil= (((this.otrosArticulosMap.size()-tamanioYaContadoEnFilasBD-1)/numeroColumnasMapaOtrosArticulos)+(numeroFilasBD));
//            }
//        }
//        return numFil;
    	
    	//MT 6891 - se elimina todo lo anterior ya que cuando se agregaban
    	//datos al mapa, la division para saber el numero de registros no era exacta
    	int i;
    	for (i = 0; i < getOtrosArticulosMap().size(); i++)
    	{
			if(getOtrosArticulosMap().get("descripcionOtrosArticulo_"+i)==null)
			{
				break;
			}
		}
    	return i;
    }
    /**
     * @return Returns the numeroFilasMapaServicios
     */
    public int getNumeroFilasMapaServicios() 
    {
//        int numFil=0;
//        if(this.serviciosMap.size()==0)
//            numFil= 0;
//        else 
//        {
//            //si el numColumnas es nulo eso quiere decir que no se pudo hacer el put desde la consulta  
//            //de este mapa entonces toca indicarle el tamanio, pero se debe tener en cuenta que 
//            //esto no sucede cuando se carga de la peticion o de la orden porque ya Existe un cargar previo.
//            if((this.getServiciosMap("numColumnas")==null) || (this.getServiciosMap("numColumnas")+"").equals("null"))
//                numFil= ((this.getServiciosMap().size())/numeroColumnasMapaServicios);
//            else
//            {
//                int numeroFilasBD= Integer.parseInt(this.getServiciosMap("numRegistros")+"");
//                int numCol=(Integer.parseInt(this.getServiciosMap("numColumnas")+""));
//                int tamanioYaContadoEnFilasBD= numeroFilasBD*numCol;
//                numFil= (((this.serviciosMap.size()-tamanioYaContadoEnFilasBD-1)/numeroColumnasMapaServicios)+(numeroFilasBD));
//            }
//        } 
//        return numFil;
    	
    	//MT 6891 - se elimina todo lo anterior ya que cuando se agregaban
    	//datos al mapa, la division para saber el numero de registros no era exacta
    	int i;
    	for (i = 0; i < getServiciosMap().size(); i++)
    	{
			if(getServiciosMap().get("codigoServicio_"+i)==null)
			{
				break;
			}
		}
    	return i;
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
     * @return Returns the nombreEspecialidadOrdena.
     */
    public String getNombreEspecialidadOrdena() {
        return nombreEspecialidadOrdena;
    }
    /**
     * @param nombreEspecialidadOrdena The nombreEspecialidadOrdena to set.
     */
    public void setNombreEspecialidadOrdena(String nombreEspecialidadOrdena) {
        this.nombreEspecialidadOrdena = nombreEspecialidadOrdena;
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
     * @return Returns the codigoEspecialidadOrdena.
     */
    public int getCodigoEspecialidadOrdena() {
        return codigoEspecialidadOrdena;
    }
    /**
     * @param codigoEspecialidadOrdena The codigoEspecialidadOrdena to set.
     */
    public void setCodigoEspecialidadOrdena(int codigoEspecialidadOrdena) {
        this.codigoEspecialidadOrdena = codigoEspecialidadOrdena;
    }
    /**
     * @return Returns the otrosArticulosMap.
     */
    public HashMap getOtrosArticulosMap() {
        return otrosArticulosMap;
    }
    /**
     * @param otrosArticulosMap The otrosArticulosMap to set.
     */
    public void setOtrosArticulosMap(HashMap otrosArticulosMap) {
        this.otrosArticulosMap = otrosArticulosMap;
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
     * @return Returns the fechaAdmisionOApertura.
     */
    public String getFechaAdmisionOApertura() {
        return fechaAdmisionOApertura;
    }
    /**
     * @param fechaAdmisionOApertura The fechaAdmisionOApertura to set.
     */
    public void setFechaAdmisionOApertura(String fechaAdmisionOApertura) {
        this.fechaAdmisionOApertura = fechaAdmisionOApertura;
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
     * @return Returns the casoFlujo.
     */
    public int getCasoFlujo() {
        return casoFlujo;
    }
    /**
     * @param casoFlujo The casoFlujo to set.
     */
    public void setCasoFlujo(int casoFlujo) {
        this.casoFlujo = casoFlujo;
    }
    /**
     * @return Returns the puedoModificarPeticion.
     */
    public boolean getPuedoModificarPeticion() {
        return puedoModificarPeticion;
    }
    /**
     * @param puedoModificarPeticion The puedoModificarPeticion to set.
     */
    public void setPuedoModificarPeticion(boolean puedoModificarPeticion) {
        this.puedoModificarPeticion = puedoModificarPeticion;
    }
   
    /**
     * @return Returns the mensajeAdvertenciaVector.
     */
    public Vector getMensajeAdvertenciaVector() {
        return mensajeAdvertenciaVector;
    }
    /**
     * @param mensajeAdvertenciaVector The mensajeAdvertenciaVector to set.
     */
    public void setMensajeAdvertenciaVector(Vector mensajeAdvertenciaVector) {
        this.mensajeAdvertenciaVector = mensajeAdvertenciaVector;
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
     * retorna el col size
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
     * @return Returns the codigoMotivoAnulacionCxXInstitucion.
     */
    public int getCodigoMotivoAnulacionCxXInstitucion() {
        return codigoMotivoAnulacionCxXInstitucion;
    }
    /**
     * @param codigoMotivoAnulacionCxXInstitucion The codigoMotivoAnulacionCxXInstitucion to set.
     */
    public void setCodigoMotivoAnulacionCxXInstitucion(
            int codigoMotivoAnulacionCxXInstitucion) {
        this.codigoMotivoAnulacionCxXInstitucion = codigoMotivoAnulacionCxXInstitucion;
    }
    /**
     * @return Returns the comentariosAnulacion.
     */
    public String getComentariosAnulacion() {
        return comentariosAnulacion;
    }
    /**
     * @param comentariosAnulacion The comentariosAnulacion to set.
     */
    public void setComentariosAnulacion(String comentariosAnulacion) {
        this.comentariosAnulacion = comentariosAnulacion;
    }
    /**
     * @return Returns the nombreMotivoAnulacionCxInstitucion.
     */
    public String getNombreMotivoAnulacionCxInstitucion() {
        return nombreMotivoAnulacionCxInstitucion;
    }
    /**
     * @param nombreMotivoAnulacionCxInstitucion The nombreMotivoAnulacionCxInstitucion to set.
     */
    public void setNombreMotivoAnulacionCxInstitucion(
            String nombreMotivoAnulacionCxInstitucion) {
        this.nombreMotivoAnulacionCxInstitucion = nombreMotivoAnulacionCxInstitucion;
    }
  
    /**
     * @return Returns the codigoMedicoSolicita.
     */
    public int getCodigoMedicoSolicita() {
        return codigoMedicoSolicita;
    }
    /**
     * @param codigoMedicoSolicita The codigoMedicoSolicita to set.
     */
    public void setCodigoMedicoSolicita(int codigoMedicoSolicita) {
        this.codigoMedicoSolicita = codigoMedicoSolicita;
    }
    /**
     * @return Returns the resultadosRespuestaSolCx.
     */
    public String getResultadosRespuestaSolCx() {
        return resultadosRespuestaSolCx;
    }
    /**
     * @param resultadosRespuestaSolCx The resultadosRespuestaSolCx to set.
     */
    public void setResultadosRespuestaSolCx(String resultadosRespuestaSolCx) {
        this.resultadosRespuestaSolCx = resultadosRespuestaSolCx;
    }
    /**
     * @return Returns the tipoBusquedaBotonVolver.
     */
    public String getTipoBusquedaBotonVolver() {
        return tipoBusquedaBotonVolver;
    }
    /**
     * @param tipoBusquedaBotonVolver The tipoBusquedaBotonVolver to set.
     */
    public void setTipoBusquedaBotonVolver(String tipoBusquedaBotonVolver) {
        this.tipoBusquedaBotonVolver = tipoBusquedaBotonVolver;
    }
    /**
     * Set del mapa de solicitudesPendientes 
     * @param key
     * @param value
     */
    public void setSolicitudesPendientesMap(String key, Object value) 
    {
        solicitudesPendientesMap.put(key, value);
    }
    /**
     * Get del mapa de N solicitudes pendientes
     * Retorna el valor de un campo dado su nombre
     */
    public Object getSolicitudesPendientesMap(String key) 
    {
        return solicitudesPendientesMap.get(key);
    }
    /**
     * @return Returns the solicitudesPendientesMap.
     */
    public HashMap getSolicitudesPendientesMap() {
        return solicitudesPendientesMap;
    }
    /**
     * @param solicitudesPendientesMap The solicitudesPendientesMap to set.
     */
    public void setSolicitudesPendientesMap(HashMap solicitudesPendientesMap) {
        this.solicitudesPendientesMap = solicitudesPendientesMap;
    }
    /**
     * @return Returns the patronOrdenar.
     */
    public String getPatronOrdenar() {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar The patronOrdenar to set.
     */
    public void setPatronOrdenar(String patronOrdenar) {
        this.patronOrdenar = patronOrdenar;
    }
    /**
     * @return Returns the ultimoPatron.
     */
    public String getUltimoPatron() {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron The ultimoPatron to set.
     */
    public void setUltimoPatron(String ultimoPatron) {
        this.ultimoPatron = ultimoPatron;
    }
	/**
	 * @return Returns the indicador.
	 */
	public int getIndicador() {
		return indicador;
	}
	/**
	 * @param indicador The indicador to set.
	 */
	public void setIndicador(int indicador) {
		this.indicador = indicador;
	}
	/**
	 * @return Returns the codigoEstadoMedico.
	 */
	public int getCodigoEstadoMedico() {
		return codigoEstadoMedico;
	}
	/**
	 * @param codigoEstadoMedico The codigoEstadoMedico to set.
	 */
	public void setCodigoEstadoMedico(int codigoEstadoMedico) {
		this.codigoEstadoMedico = codigoEstadoMedico;
	}
	/**
	 * @return Returns the datosAnulacion.
	 */
	public HashMap getDatosAnulacion() {
		return datosAnulacion;
	}
	/**
	 * @param datosAnulacion The datosAnulacion to set.
	 */
	public void setDatosAnulacion(HashMap datosAnulacion) {
		this.datosAnulacion = datosAnulacion;
	}
	
	/**
	 * @return Retorna un elemento del mapa datosAnulacion.
	 */
	public Object getDatosAnulacion(String key) 
	{
		return datosAnulacion.get(key);
	}
	/**
	 * @param datosAnulacion The datosAnulacion to set.
	 */
	public void setDatosAnulacion(String key,Object obj) {
		this.datosAnulacion.put(key,obj);
	}
    /**
     * @return Returns the mostrarLinkVolverListadoPeticiones.
     */
    public boolean getMostrarLinkVolverListadoPeticiones() {
        return mostrarLinkVolverListadoPeticiones;
    }
    /**
     * @param mostrarLinkVolverListadoPeticiones The mostrarLinkVolverListadoPeticiones to set.
     */
    public void setMostrarLinkVolverListadoPeticiones(
            boolean mostrarLinkVolverListadoPeticiones) {
        this.mostrarLinkVolverListadoPeticiones = mostrarLinkVolverListadoPeticiones;
    }
	/**
	 * @return Retorna nroOrdenMedica.
	 */
	public int getNroOrdenMedica() {
		return nroOrdenMedica;
	}
	/**
	 * @param Asigna nroOrdenMedica.
	 */
	public void setNroOrdenMedica(int nroOrdenMedica) {
		this.nroOrdenMedica = nroOrdenMedica;
	}
	/**
	 * @return ConsecutivoOrdenMedica
	 */
	public String getConsecutivoOrdenMedica() {
		return consecutivoOrdenMedica;
	}
	/**
	 * @param consecutivoOrdenMedica
	 */
	public void setConsecutivoOrdenMedica(String consecutivoOrdenMedica) {
		this.consecutivoOrdenMedica = consecutivoOrdenMedica;
	}

	public int getCodigoServicioPostular() {
		return codigoServicioPostular;
	}

	public void setCodigoServicioPostular(int codigoServicioPostular) {
		this.codigoServicioPostular = codigoServicioPostular;
	}

	public boolean isPostularServicio() {
		return postularServicio;
	}

	public void setPostularServicio(boolean postularServicio) {
		this.postularServicio = postularServicio;
	}

	public boolean isSolPYP() {
		return solPYP;
	}

	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	/**
	 * @return Returns the centrosAtencionPYP.
	 */
	public String getCentrosAtencionPYP() {
		return centrosAtencionPYP;
	}

	/**
	 * @param centrosAtencionPYP The centrosAtencionPYP to set.
	 */
	public void setCentrosAtencionPYP(String centrosAtencionPYP) {
		this.centrosAtencionPYP = centrosAtencionPYP;
	}

	/**
	 * @return the tipoAnestesia
	 */
	public int getTipoAnestesia() {
		return tipoAnestesia;
	}

	/**
	 * @param tipoAnestesia the tipoAnestesia to set
	 */
	public void setTipoAnestesia(int tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}

	/**
	 * @return the tiposAnestesia
	 */
	public ArrayList getTiposAnestesia() {
		return tiposAnestesia;
	}

	/**
	 * @param tiposAnestesia the tiposAnestesia to set
	 */
	public void setTiposAnestesia(ArrayList tiposAnestesia) {
		this.tiposAnestesia = tiposAnestesia;
	}

	/**
	 * @return the nombreTipoAnestesia
	 */
	public String getNombreTipoAnestesia() {
		return nombreTipoAnestesia;
	}

	/**
	 * @param nombreTipoAnestesia the nombreTipoAnestesia to set
	 */
	public void setNombreTipoAnestesia(String nombreTipoAnestesia) {
		this.nombreTipoAnestesia = nombreTipoAnestesia;
	}
	/**
	 * @return the justificacionesServicios
	 */
	public HashMap getJustificacionesServicios() {
		return justificacionesServicios;
	}

	/**
	 * @param justificacionesServicios the justificacionesServicios to set
	 */
	public void setJustificacionesServicios(HashMap justificacionesServicios) {
		this.justificacionesServicios = justificacionesServicios;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionesServicios(String key) {
		return justificacionesServicios.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setJustificacionesServicios(String key, Object obj) {
		this.justificacionesServicios.put(key, obj);
	}
	/**
	 * @return the justificar
	 */
	public String getJustificar() {
		return justificar;
	}

	/**
	 * @param justificar the justificar to set
	 */
	public void setJustificar(String justificar) {
		this.justificar = justificar;
	}


	/**
	 * @return the arrayArticuloIncluidoDto
	 */
	public ArrayList<DtoArticuloIncluidoSolProc> getArrayArticuloIncluidoDto() {
		return arrayArticuloIncluidoDto;
	}


	/**
	 * @param arrayArticuloIncluidoDto the arrayArticuloIncluidoDto to set
	 */
	public void setArrayArticuloIncluidoDto(
			ArrayList<DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto) {
		this.arrayArticuloIncluidoDto = arrayArticuloIncluidoDto;
	}


	/**
	 * @return the justificacionMap
	 */
	public HashMap getJustificacionMap() {
		return justificacionMap;
	}


	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionMap(HashMap justificacionMap) {
		this.justificacionMap = justificacionMap;
	}


	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		return hiddens;
	}


	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}
	
	/**
	 * @return the justificacionInclMap
	 */
	public Object getJustificacionMap(String key) {
		return justificacionMap.get(key);
	}


	/**
	 * @param justificacionInclMap the justificacionInclMap to set
	 */
	public void setJustificacionMap(String key, Object value) {
		this.justificacionMap.put(key, value);
	}


	/**
	 * @return the cadenaFiltroBusqueda
	 */
	public String getCadenaFiltroBusqueda() {
		return cadenaFiltroBusqueda;
	}


	/**
	 * @param cadenaFiltroBusqueda the cadenaFiltroBusqueda to set
	 */
	public void setCadenaFiltroBusqueda(String cadenaFiltroBusqueda) {
		this.cadenaFiltroBusqueda = cadenaFiltroBusqueda;
	}


	public String getJustificacionSolicitud() {
		return justificacionSolicitud;
	}


	public void setJustificacionSolicitud(String justificacionSolicitud) {
		this.justificacionSolicitud = justificacionSolicitud;
	}


	public String getJustificacionSolicitudNueva() {
		return justificacionSolicitudNueva;
	}


	public void setJustificacionSolicitudNueva(String justificacionSolicitudNueva) {
		this.justificacionSolicitudNueva = justificacionSolicitudNueva;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mostrarImprimirAutorizacion
	
	 * @return retorna la variable mostrarImprimirAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mostrarImprimirAutorizacion
	
	 * @param valor para el atributo mostrarImprimirAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaNombresReportes
	
	 * @return retorna la variable listaNombresReportes 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaNombresReportes
	
	 * @param valor para el atributo listaNombresReportes 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoDiagnostico
	
	 * @return retorna la variable dtoDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoDiagnostico
	
	 * @param valor para el atributo dtoDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}


	public void setListaServiciosImpimirOrden(
			ArrayList<String> listaServiciosImpimirOrden) {
		this.listaServiciosImpimirOrden = listaServiciosImpimirOrden;
	}


	public ArrayList<String> getListaServiciosImpimirOrden() {
		return listaServiciosImpimirOrden;
	}	


	public ArrayList<InfoResponsableCobertura> getListaInfoRespoCoberturaCx() {
		return listaInfoRespoCoberturaCx;
	}


	public void setListaInfoRespoCoberturaCx(
			ArrayList<InfoResponsableCobertura> listaInfoRespoCoberturaCx) {
		this.listaInfoRespoCoberturaCx = listaInfoRespoCoberturaCx;
	}


	public boolean isCirugiaAsociadaPeticion() {
		return cirugiaAsociadaPeticion;
	}


	public void setCirugiaAsociadaPeticion(boolean cirugiaAsociadaPeticion) {
		this.cirugiaAsociadaPeticion = cirugiaAsociadaPeticion;
	}

	/**
	 * @return the profesionalEspecialidades
	 */
	public ArrayList<ProfesionalEspecialidadesDto> getProfesionalEspecialidades() {
		return profesionalEspecialidades;
	}

	/**
	 * @param profesionalEspecialidades the profesionalEspecialidades to set
	 */
	public void setProfesionalEspecialidades(
			ArrayList<ProfesionalEspecialidadesDto> profesionalEspecialidades) {
		this.profesionalEspecialidades = profesionalEspecialidades;
	}

	public int getNumeroFilasServiciosParaJS() {
		int resp = getNumeroFilasMapaServicios();
		for (int i=0; i < getNumeroFilasMapaServicios(); i ++){
			if(getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true")){
				resp--;
			}
        }
		return resp;
	}
}