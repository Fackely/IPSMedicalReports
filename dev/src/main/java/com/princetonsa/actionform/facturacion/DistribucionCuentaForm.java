/*
 * Jul 5, 2007
 * Proyect axioma
 * Paquete com.princetonsa.actionform.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.facturacion.DtoMontoCobro;
import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
@SuppressWarnings("serial")
public class DistribucionCuentaForm extends ValidatorForm 
{

	/**
	 * Variable para manejar el estado que se tienen en la aplicacion
	 */
	private String estado;
	
	/**
	 * Mapa para manejar los ingresos que tiene un paciente.
	 */
	private HashMap<String, Object> ingresos;

	/**
	 * Indice seleccionado del ingreso.
	 */
	private int indiceIngresoSeleccionado;
	
	/**
	 * Array list que contiene la informacion de los responsables.
	 */
	private ArrayList<DtoSubCuentas> responsables;

	/**
	 * Array list que contiene la informacion de los responsablesEliminados.
	 */
	private ArrayList<DtoSubCuentas> responsablesEliminados;
	
	/////////datos del encabezado de la distribucion
	
	/**
	 * Atributo que contiene el codigo del ingreso al cual se le esta haciendo la distribucion.
	 */
	private int codigoIngreso;
	
	/**
	 * 
	 */
	private String consecutivoIngreso;
	
	/**
	 * 
	 */
	private int ultimaViaIngreso;
	
	/**
	 * 
	 */
	private int convenioDefectoVia;
	
	/**
	 * Cuenta asociada al ingreso seleccionado.
	 */
	private int cuenta;
	
	/**
	 * Fecha de la Ultima Liquidacion
	 */
	private String fechaHoraUltimaLiquidacion;
	
	
	/**
	 * Tipo de distribucion.
	 */
	private String tipoDistribucion;
	

	/**
	 * Tipo de distribucion.
	 */
	private String tipoDistribucionOriginal;
		
	public String getTipoDistribucionOriginal() {
		return tipoDistribucionOriginal;
	}

	public void setTipoDistribucionOriginal(String tipoDistribucionOriginal) {
		this.tipoDistribucionOriginal = tipoDistribucionOriginal;
	}

	/**
	 * Usuario que realiza la ultma liquidacion
	 */
	private String usuarioUltimaLiquidacion;
	
	/**
	 * Tipo de complejidad que se esta manejando en la cuenta asociada al ingreso.
	 */
	private int tipoComplejidad;
	
	/**
	 * Tipo de complejidad que se esta manejando en la cuenta asociada al ingreso.
	 */
	private String descTipoComplejidad;
	
	/**
	 * Atributo que indica si ya existe una distribucion previa.
	 */
	boolean existeDistribucionPrevia;
	
	/**
	 * 
	 */
	private boolean puedoGrabarConvenioAdicional;
	
	/**
	 * Mapa en el que se maneja la informacion del responsable, cuando se selecciona la opcion de infor Ingreso.
	 * Se carga con el responsable seleccionado, y cuando se modifica se pasa la informacion nuevamente a dto.
	 * Este mapa es solo de trabajo temporal
	 */
	private HashMap infoIngreso;

	/**
	 * Mapa en el que se maneja los parametros distribucion, cuando se selecciona la opcion de  param distribucion.
	 * Se carga con el responsable seleccionado, y cuando se modifica se pasa la informacion nuevamente a dto.
	 * Este mapa es solo de trabajo temporal
	 */
	private HashMap paramDistribucion;
	
	/**
	 * Maneja la informacion de los filtros de distribucion de todo los responsables.
	 * el indice es el mimo del arraylist que contiene los responsables.
	 */
	private HashMap filtroDistribucion;

	/**
	 * Mapa en el que se maneja los requisitos paciente de los responsables, trabaja en conjunto con infoIngreso
	 */
	private HashMap requisitosPaciente;
	
	/**
	 * vairable para manejar genericamente el indice de responsables y paquetes. 
	 */
	private int indice;
	
	/**
	 * Bandera que indica si la seccion nuevo Responsable esta abierta o cerrada.
	 */
	private boolean nuevoResposable;
	
	//Arreglos que tienen estructuras de datos para el ingreso del convenio
	private ArrayList<HashMap<String, Object>> contratosConvenio = new ArrayList<HashMap<String,Object>>();
	private HashMap estratosSocialesConvenio = new HashMap();
	private HashMap tiposAfiliadoConvenio = new HashMap();
	private HashMap tiposAfiliadolista = new HashMap();
	private ArrayList<HashMap<String, Object>> montosCobroConvenio = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> montosCobroResumen = new HashMap<String, Object>();
	private Vector<InfoDatosString> naturalezasPaciente = new Vector<InfoDatosString>();
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> coberturaSalud = new ArrayList<HashMap<String,Object>>();

	/**
	 * 
	 */
	private HashMap coberturaAsignadas;
	
	/**
	 * 
	 */
	private int codigoConvenio;
	
	/**
	 * Bandera que indica si se puede modificar la informacion de un responsable
	 */
	private boolean puedoModificar;
	
	/**
	 * Varriable que indica si estoy modificando un reponsable o lo estoy insertando.
	 */
	private boolean modificacionResponsable;
	
	
	//Atributos para el manejo del filtro de fecha afiliacion
	private String fechaAfiliacion = "";
	
	/**
	 * indice para eliminar el responsable.
	 */
	private String indiceReponsableEliminar;
	
	/**
	 * 
	 */
	private String indiceFiltroDistribucion;
	
	/**
	 * Bandera que indica si podemos modificar el Monto Autorizado.
	 */
	private boolean modificarMontoAutorizado;
	
	/**
	 * Mapa para manejar las solicitudes del paciente.
	 */
	private HashMap detSolicitudesPaciente;
	
	/**
	 * 
	 */
	private HashMap detSolicitudesResponsable;
	
	
	 /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    /**
     * 
     */
    private boolean busquedaAvanzada;
	
    /**
     * Parametros para la Busqueda Avanzada
     */
    private HashMap parametrosBusquedaAvanzada;
    
    /**
     * Parametros para la Busqueda Avanzada de Responsable
     */
    private HashMap parametrosBusquedaAvanzadaResponsable;
    
    /**
     * Mapa que contiene la distribucion de las solicitudes.
     */
    private HashMap distribucionSolicitud;
    
    /**
     * Indice del detalle de la solicitud sobre el cual se esta trabajando.
     */
    private String indiceDetSol;
    
    /**
     * Atributo para manejar el indice del responsable sobre el cual se esta modificando la respectiva solicitud sub_cuenta.
     */
    private int indiceReponsable;
    
    /**
     * Atributo que solo tiene valor para el caso de distribucion manual, y la solicitud es tipo paquete. contiene la subcuenta a la cual
     * se le hizo el paquete.
     */
    private String responsablePaquete;
    
    /**
     * 
     */
    private boolean liquidarAutomaticamente;
    
    /**
     * 
     */
    private ResultadoBoolean mostrarMensaje;
    
    /**
     * Mapa para hacer las validaciones de vigencias y topes en los contratos de los responsables.
     */
    private HashMap vigenciaTopesContratos=new HashMap();
    
    /**
     * 
     */
    private String paginaSiguiente;
    
    /**
     * 
     */
    private boolean pacienteCargado;
    
    /**
     * 
     */
    private String pathRedireccion;
    
    /**
     * 
     */
    private boolean registrandoDistribucion;
    
    /**
     * 
     */
    private ArrayList tiposIdentificacion;
    
    
    private ArrayList<String>mensajes = new ArrayList<String>();
    
    /**
     * 
     */
    private HashMap justificacionNoPosMap  = new HashMap();
    
    private HashMap esquemasInventario;
    private HashMap esquemasProcedimientos;
    
	
	/**
	 * 
	 */
	private HashMap esquemasInventarioEliminados;
	
	/**
	 * 
	 */
	private HashMap esquemasProcedimientosEliminados;
	
	private int posEliminar;
    
	
	 // Manejo de Mensajes de Advertencia de sin Contrato y Controla Anticipos
    private Boolean controlMensaje;
	
    
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /**
     * HashMap Proceso Distribucion de Solicitudes por Porcentaje Tarea 52846
     */
    private HashMap distribucionSolicitudesPorPorcentaje;
    
    private ArrayList<ElementoApResource> mensajesAlerta = new ArrayList<ElementoApResource>();
    
    
    
  //************************************************
    /**
     * Victor Gomez 
     */
    
    /**
	 * Encargado de almacenar la informacion de los servicios
	 * o articulos del convenio de la subcuenta
	 */
	private HashMap cuerpoDetalle = new HashMap();
	private HashMap cuerpoDetalleOld = new HashMap();
	/**
	 * Campo que activa el filtro por 贸rdenes ambulatorias
	 */
	private boolean ordenesAmbulatorias;
	/**
	 * Almacena la informacion de los ingresos y cuetas de un paciente
	 */
	private HashMap listadoIngresos= new HashMap();
	/**
	 * almacena los datos del ingreso en el detalle 
	 */
	private HashMap encabezadoDetalle= new HashMap();
	//************************************************
	
	private HashMap<String, Object> verificacion = new HashMap<String, Object>();
	
	/**
	 * 
	 */
	private  DtoMontoCobro montosCombro;
    
    /**
     * Reset para los mensajes de las Justificacion Pnedientes
     *
     */
    public void resetJust()
    {
    	this.justificacionNoPosMap= new HashMap();
        justificacionNoPosMap.put("numRegistros", 0);
    }
    
	/**
	 * Reset de la funcionalidad.
	 *
	 */
	public void reset()
	{
		this.ingresos=new HashMap<String, Object>();
		this.ingresos.put("numRegistros", "0");
		this.indiceIngresoSeleccionado=ConstantesBD.codigoNuncaValido;
		this.codigoIngreso=ConstantesBD.codigoNuncaValido;
		this.consecutivoIngreso="";
		this.ultimaViaIngreso=ConstantesBD.codigoNuncaValido;
		this.convenioDefectoVia=ConstantesBD.codigoNuncaValido;
		this.responsables=new ArrayList<DtoSubCuentas>();
		this.responsablesEliminados=new ArrayList<DtoSubCuentas>();
		
		this.cuenta=ConstantesBD.codigoNuncaValido;
		this.fechaHoraUltimaLiquidacion="";
		this.tipoDistribucion="";
		this.tipoDistribucionOriginal="";
		this.usuarioUltimaLiquidacion="";
		this.tipoComplejidad=ConstantesBD.codigoNuncaValido;
		this.descTipoComplejidad="";
		this.existeDistribucionPrevia=false;
		this.puedoGrabarConvenioAdicional=true;
		
		this.infoIngreso=new HashMap();
		this.paramDistribucion=new HashMap();
		this.requisitosPaciente=new HashMap();
		this.requisitosPaciente.put("numRegistros", "0");
		this.filtroDistribucion=new HashMap();
		this.filtroDistribucion.put("numRegistros", "0");
		
		this.indice=ConstantesBD.codigoNuncaValido;
		this.nuevoResposable=false;
		
		//arreglos estructuras del ingreso del contrato
		this.contratosConvenio = new ArrayList<HashMap<String,Object>>();
		this.estratosSocialesConvenio = new HashMap();
		this.tiposAfiliadoConvenio = new HashMap();
		this.montosCobroConvenio = new ArrayList<HashMap<String,Object>>();
		this.naturalezasPaciente = new Vector<InfoDatosString>();
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.coberturaSalud = new ArrayList<HashMap<String,Object>>();
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.puedoModificar=false;
		this.modificacionResponsable=false;
		
		//Atributos para el manejo del filtro de fecha afiliacion
		this.fechaAfiliacion = "";
		
		this.indiceReponsableEliminar="";
		
		this.indiceFiltroDistribucion="";
		
		this.modificarMontoAutorizado=true;
		
		this.detSolicitudesPaciente=new HashMap();
		this.detSolicitudesPaciente.put("numRegistros", "0");
		
		this.patronOrdenar="";
		this.ultimoPatron="";
		
		this.busquedaAvanzada=false;
		
		this.parametrosBusquedaAvanzada=new HashMap();
		
		this.distribucionSolicitud=new HashMap();
		this.distribucionSolicitud.put("numRegistros", "0");
		
		this.indiceDetSol=ConstantesBD.codigoNuncaValido+"";
		this.responsablePaquete="";
		this.liquidarAutomaticamente=false;
		this.mostrarMensaje=new ResultadoBoolean(false);
		
		this.vigenciaTopesContratos=new HashMap();
		this.vigenciaTopesContratos.put("numRegistros", "0");
		
		this.detSolicitudesResponsable=new HashMap();
		this.detSolicitudesResponsable.put("numRegistros", "0");
		
		this.parametrosBusquedaAvanzadaResponsable=new HashMap();
		this.paginaSiguiente="";
		
		this.pacienteCargado=false;
		
		this.pathRedireccion="";
		
		this.coberturaAsignadas=new HashMap();
		
		this.registrandoDistribucion=false;
		this.tiposIdentificacion=new ArrayList();
		this.mensajes = new ArrayList<String>();
		
		this.esquemasInventario=new HashMap<String, Object>();
        this.esquemasInventario.put("numRegistros", "0");
        this.esquemasProcedimientos=new HashMap<String, Object>();
        this.esquemasProcedimientos.put("numRegistros", "0");

        this.esquemasInventarioEliminados=new HashMap<String, Object>();
        this.esquemasInventarioEliminados.put("numRegistros", "0");
        this.esquemasProcedimientosEliminados=new HashMap<String, Object>();
        this.esquemasProcedimientosEliminados.put("numRegistros", "0");
        
        this.posEliminar=ConstantesBD.codigoNuncaValido;
        
        this.controlMensaje=false;
        this.distribucionSolicitudesPorPorcentaje = new HashMap<String, Object>();
        this.distribucionSolicitudesPorPorcentaje.put("numRegistros", "0");
        
        this.mensajesAlerta = new ArrayList<ElementoApResource>();
        
        
        //****************************
        // atributos solcitud autorizciones
        this.ordenesAmbulatorias = false;
        
        this.tiposAfiliadolista = new HashMap(); 
        this.montosCobroResumen = new HashMap();
        this.montosCombro= new DtoMontoCobro();
        this.verificacion = new HashMap<String, Object>();
	}
	
	/**
	 * M贸todo que limpia el mapa de ParamDistribucion
	 * Incluido por la Tarea 52989
	 */
	public void resetParamDistribucion()
	{
		this.paramDistribucion = new HashMap<String, Object>();
		this.paramDistribucion.put("numRegistros", "0");
	}
	
	/**
	 * Validacion de errores de la funcionalidad
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
	    ActionErrors errores= new ActionErrors();
	    if(this.estado.equals("guardarPaginaPrincipal") || this.estado.equals("liquidarDistribucion"))
	    {
	    	if(this.requerirComplejidad())
	    	{
	    		if(this.tipoComplejidad<=0)
	    		{
	    			errores.add("errors.required", new ActionMessage("errors.required", "El Tipo de Complejidad"));
	    		}
	    	}
	    	if(!this.esPrioridadCorrecta())
	    	{
	    		errores.add("error.distribucion.prioridadesNoSecuenciales", new ActionMessage("error.distribucion.prioridadesNoSecuenciales"));
	    	}
	    	double porcentajeAutorizado=0;
	    	boolean liquidar=false;
	    	for(int a=0;a<responsables.size();a++)
	    	{
	    		porcentajeAutorizado=porcentajeAutorizado+Utilidades.convertirADouble(responsables.get(a).getPorcentajeAutorizado(),true);
				try {
					if(!liquidar&&responsables.get(a).getNroPrioridad()!=Utilidades.obtenerPrioridadResponsabe(responsables.get(a).getSubCuenta()))
					{
						liquidar=true;
					}
				} catch (IPSException ipse) {
					Log4JManager.error(ipse.getMessage(), ipse);
					errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
				}
	    	}
	    	if(this.tipoDistribucion.trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual)&&(this.estado.equals("liquidarDistribucion")||liquidar))
	    	{
	    		if(porcentajeAutorizado!=100)
	    		{
	    			errores.add("error.distribucion.porcentajAutorizadoDiferenteCien", new ActionMessage("error.distribucion.porcentajAutorizadoDiferenteCien",porcentajeAutorizado+""));

	    		}
	    	}
	    	/*
	    	 * Nunca se dara este caso.
	    	if(this.responsables.get(this.responsables.size()-1).getConvenio().getCodigo()!=this.convenioDefectoVia)
	    	{
	    		errores.add("error.distribucion.prioridadConvenioDefecto", new ActionMessage("error.distribucion.prioridadConvenioDefecto"));
	    	}*/
	    }
	    else if(this.estado.equals("guardarInfoResponsable"))
	    {
	    	if(this.infoIngreso.get("codigoContrato").toString().equals(""))
	    	{
				errores.add("Contrato es requerido",new ActionMessage("errors.required","El Contrato"));
	    	}
	    	else
	    	{
	    		IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
				DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(this.infoIngreso.get("codigoContrato")+""));
				
				if(UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
				{
					if(this.infoIngreso.get("codigoEstratoSocial").toString().equals(""))
						errores.add("Contrato es requerido",new ActionMessage("errors.required","La Clasificaci贸n Socioecon贸mica"));
				}
				if(UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
				{
			    	if(this.infoIngreso.get("codigoTipoAfiliado").toString().equals(""))
						errores.add("Contrato es requerido",new ActionMessage("errors.required","El Tipo de Afiliado"));
				}
				if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
				{
			    	if(this.infoIngreso.get("codigoMontoCobro").toString().equals(""))
						errores.add("Contrato es requerido",new ActionMessage("errors.required","El Monto de Cobro"));
				}
	    	}
	    	
	    	
	    	if(UtilidadTexto.getBoolean(infoIngreso.get("requiereCarnet").toString())&&this.infoIngreso.get("requiereCarnet").toString().equals(""))
	    		errores.add("n煤mero de carnet",new ActionMessage("errors.required","El N煤mero de Carnet"));
	    	
	    	if(UtilidadTexto.getBoolean(this.infoIngreso.get("esReporteAtencionInicialUrgencias")+"")&&this.infoIngreso.get("codigoTipoCobertura").toString().equals(""))
	    	{
	    		errores.add("n煤mero de carnet",new ActionMessage("errors.required","El tipo de cobertura"));
	    	}
	    	
	    	String fecha=infoIngreso.get("fechaAfiliacion")+"";
	    	if(!fecha.trim().equals(""))
	    	{
				if(!UtilidadFecha.validarFecha(fecha))
				{
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",fecha));
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha,UtilidadFecha.getFechaActual()))
				{
					errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Fecha ("+fecha+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
				}
			}
	    	
/*	    	if(UtilidadTexto.getBoolean(infoIngreso.get("esConvenioPoliza").toString()))
	    	{
	    		if(infoIngreso.get("apellidosPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El Apellido del Titular"));
	    		if(infoIngreso.get("nombresPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El Nombre del Titular"));
	    		if(infoIngreso.get("tipoIdPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El Tipo de Identificaci贸n del Titular"));
	    		if(infoIngreso.get("numeroIdPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El N煤mero de Identificaci贸n del Titular"));
	    		if(infoIngreso.get("direccionPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","La Direcci贸n del Titular"));
	    		if(infoIngreso.get("autorizacionPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El N煤mero de Autorizaci贸n de la P贸liza"));
	    		if(infoIngreso.get("valorPoliza").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El Valor para el Monto de la P贸liza"));
	    		else
				{
					try
					{
						if(Double.parseDouble(infoIngreso.get("valorPoliza").toString())<=0)
							errores.add("Debe ser num茅rico", new ActionMessage("errors.MayorQue","El Valor para el Monto de la P贸liza","0"));
					}
					catch(Exception e)
					{
						errores.add("Debe ser numerico", new ActionMessage("errors.float","El Valor para el Monto de la P贸liza"));
					}
				}
				
			}
			*/
	    	if(this.verificacion.get("codigoEstado") != null && !this.verificacion.get("codigoEstado").toString().equals(""))
	    	{
	    		if(this.verificacion.get("codigoTipo") == null || this.verificacion.get("codigoTipo").toString().equals(""))
		    	{
					errores.add("Tipo Verificacion es requerido",new ActionMessage("errors.required","El Tipo Verificaci髇"));
		    	}
		    	if(this.verificacion.get("personaSolicita") == null || this.verificacion.get("personaSolicita").toString().equals(""))
		    	{
					errores.add("Persona Solicita es requerido",new ActionMessage("errors.required","La Persona Solicita"));
		    	}
		    	if(this.verificacion.get("fechaSolicitud") == null || this.verificacion.get("fechaSolicitud").toString().equals(""))
		    	{
					errores.add("Fecha Solicitud es requerido",new ActionMessage("errors.required","La Fecha Solicitud"));
		    	}
		    	if(this.verificacion.get("horaSolicitud") == null || this.verificacion.get("horaSolicitud").toString().equals(""))
		    	{
					errores.add("Hora Solicitud es requerido",new ActionMessage("errors.required","La Hora Solicitud"));
		    	}
		    	if(this.verificacion.get("fechaVerificacion") == null || this.verificacion.get("fechaVerificacion").toString().equals(""))
		    	{
					errores.add("Fecha Verificaci髇 es requerido",new ActionMessage("errors.required","La Fecha Verificaci髇"));
		    	}
		    	if(this.verificacion.get("horaVerificacion") == null || this.verificacion.get("horaVerificacion").toString().equals(""))
		    	{
					errores.add("Hora Verificaci髇 es requerido",new ActionMessage("errors.required","La Hora Verificaci髇"));
		    	}
	    		String codigoEstado=this.verificacion.get("codigoEstado").toString();
	    		if(codigoEstado.equals(ConstantesIntegridadDominio.acronimoEstadoAceptado)
	    				|| codigoEstado.equals(ConstantesIntegridadDominio.acronimoEstadoRechazado)
	    				|| codigoEstado.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado)){
		    		if(this.verificacion.get("personaContactada") == null || this.verificacion.get("personaContactada").toString().equals(""))
			    	{
						errores.add("Persona Contactada es requerido",new ActionMessage("errors.required","La Persona Contactada"));
			    	}
	    		}
	    	}
	    	
	    }
	    else if (estado.equals("guardarParamDistribucion"))
	    {
			if(Utilidades.convertirAEntero(esquemasInventario.get("numRegistros")+"")>0)
			{
				for(int i=0;i<Utilidades.convertirAEntero(esquemasInventario.get("numRegistros")+"");i++)
				{
					if((esquemasInventario.get("tiporegistro_"+i)+"").equals("MEM"))
					{
						if((esquemasInventario.get("esquematarifario_"+i)+"").trim().equals(""))
						{
							errores.add("Fecha Requerido",new ActionMessage("errors.required","El Esquema Tarifario "+(i+1)+" de Esquemas Tarifarios de Inventarios"));

						}
					}
					
					for(int j=0;j<i;j++)
					{
						//if((esquemasInventario.get("claseinventario_"+j)+"").trim().equals(esquemasInventario.get("claseinventario_"+i)+"")&&(esquemasInventario.get("esquematarifario_"+j)+"").trim().equals(esquemasInventario.get("esquematarifario_"+i)+""))
						if((esquemasInventario.get("claseinventario_"+j)+"").trim().equals(esquemasInventario.get("claseinventario_"+i)+""))
						{
							errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios de Inventarios, se encuentra repetido"));
						}
					}
				}
			}
			
			
			//procedimientos.
			if(Utilidades.convertirAEntero(esquemasProcedimientos.get("numRegistros")+"")>0)
			{
				for(int i=0;i<Utilidades.convertirAEntero(esquemasProcedimientos.get("numRegistros")+"");i++)
				{
					if((esquemasProcedimientos.get("tiporegistro_"+i)+"").equals("MEM"))
					{
						if((esquemasProcedimientos.get("esquematarifario_"+i)+"").trim().equals(""))
						{
							errores.add("Fecha Requerido",new ActionMessage("errors.required","El Esquema Tarifario "+(i+1)+" de Esquemas Tarifarios de Procedimientos"));
							
						}
					}
					
					for(int j=0;j<i;j++)
					{
						//if((esquemasProcedimientos.get("gruposervicio_"+j)+"").trim().equals(esquemasProcedimientos.get("gruposervicio_"+i)+"")&&(esquemasProcedimientos.get("esquematarifario_"+j)+"").trim().equals(esquemasProcedimientos.get("esquematarifario_"+i)+""))
						if((esquemasProcedimientos.get("gruposervicio_"+j)+"").trim().equals(esquemasProcedimientos.get("gruposervicio_"+i)+""))
						{
							errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios de Procedimientos, se encuentra repetido"));
						}
					}
				}
			}
	    }
	    //Incluido por la Tarea 52846
	    //Validaci贸n del Total de los Porcentajes
	    else if (estado.equals("distribucionSolicitudesPorPorcentaje"))
	    {
	    	double porcentaje = ConstantesBD.codigoNuncaValidoDouble;
	    	
	    	for(int a=0; a<responsables.size(); a++)
	    		porcentaje = porcentaje + Utilidades.convertirADouble(this.getDistribucionSolicitudesPorPorcentaje("porcentajeDistribuido_"+responsables.get(a).getSubCuenta())+"", true);
			
	    	if(porcentaje != 100)
	    		errores.add("error.distribucion.porcentajDiferenteCien", new ActionMessage("error.distribucion.porcentajDiferenteCien",porcentaje+""));
	    }
	    return errores;
	}

	/**
	 * Metodo que valida las prioridades de los responsables, deben estar consecutivas desde 1.
	 * @return
	 */
	private boolean esPrioridadCorrecta() 
	{
		int[] prioridades=new int[this.responsables.size()];
		//pasar las prioridades a un vector.
		for(int a=0;a<prioridades.length;a++)
		{
			prioridades[a]=this.responsables.get(a).getNroPrioridad();
		}
		
		//ordenar el vector
		for(int a=0;a<prioridades.length;a++)
		{
			int temp=0;
			for(int j=0;j<a;j++)
			{
				if(prioridades[a]<prioridades[j])
				{
					temp=prioridades[a];
					prioridades[a]=prioridades[j];
					prioridades[j]=temp;
				}
			}
		}
		
		//verificar que las prioridades sean consecutivas desde uno, para esto verificamos el valor=(pos+1), ya que pos inicia en 0
		for(int a=0;a<prioridades.length;a++)
		{
			if(prioridades[a]!=(a+1))
				return false;
		}
		return true;
	}


	/**
	 * 
	 * @return
	 */
	private boolean requerirComplejidad() 
	{
		for(int a=0;a<this.responsables.size();a++)
		{
			DtoSubCuentas tempo=this.responsables.get(a);
			if(Utilidades.convenioManejaComplejidad(tempo.getConvenio().getCodigo()))
				return true;
		}
		return false;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the ingresos
	 */
	public HashMap<String, Object> getIngresos() {
		return ingresos;
	}


	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(HashMap<String, Object> ingresos) {
		this.ingresos = ingresos;
	}


	/**
	 * @return the indiceIngresoSeleccionado
	 */
	public int getIndiceIngresoSeleccionado() {
		return indiceIngresoSeleccionado;
	}


	/**
	 * @param indiceIngresoSeleccionado the indiceIngresoSeleccionado to set
	 */
	public void setIndiceIngresoSeleccionado(int indiceIngresoSeleccionado) {
		this.indiceIngresoSeleccionado = indiceIngresoSeleccionado;
	}


	/**
	 * @return the codigoIngreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}


	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}


	/**
	 * @return the responsables
	 */
	public ArrayList<DtoSubCuentas> getResponsables() {
		return responsables;
	}


	/**
	 * @param responsables the responsables to set
	 */
	public void setResponsables(ArrayList<DtoSubCuentas> responsables) {
		this.responsables = responsables;
	}


	/**
	 * @return the cuenta
	 */
	public int getCuenta() {
		return cuenta;
	}


	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}


	/**
	 * @return the fechaUltimaLiquidacion
	 */
	public String getFechaHoraUltimaLiquidacion() {
		return fechaHoraUltimaLiquidacion;
	}


	/**
	 * @param fechaUltimaLiquidacion the fechaUltimaLiquidacion to set
	 */
	public void setFechaHoraUltimaLiquidacion(String fechaHoraUltimaLiquidacion) {
		this.fechaHoraUltimaLiquidacion = fechaHoraUltimaLiquidacion;
	}


	/**
	 * @return the tipoComplejidad
	 */
	public int getTipoComplejidad() {
		return tipoComplejidad;
	}


	/**
	 * @param tipoComplejidad the tipoComplejidad to set
	 */
	public void setTipoComplejidad(int tipoComplejidad) {
		this.tipoComplejidad = tipoComplejidad;
	}


	/**
	 * @return the tipoDistribucion
	 */
	public String getTipoDistribucion() {
		return tipoDistribucion;
	}


	/**
	 * @param tipoDistribucion the tipoDistribucion to set
	 */
	public void setTipoDistribucion(String tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}


	/**
	 * @return the usuarioUltimaLiquidacion
	 */
	public String getUsuarioUltimaLiquidacion() {
		return usuarioUltimaLiquidacion;
	}


	/**
	 * @param usuarioUltimaLiquidacion the usuarioUltimaLiquidacion to set
	 */
	public void setUsuarioUltimaLiquidacion(String usuarioUltimaLiquidacion) {
		this.usuarioUltimaLiquidacion = usuarioUltimaLiquidacion;
	}
	
	/**
	 * 
	 * @param index
	 * @param valor
	 */
	public void setPrioridadResponsable(String index,String valor)
	{
		DtoSubCuentas subCuenta=this.responsables.get(Utilidades.convertirAEntero(index, false));
		subCuenta.setNroPrioridad(Utilidades.convertirAEntero(valor, false));
		this.responsables.remove(Utilidades.convertirAEntero(index, false));
		this.responsables.add(Utilidades.convertirAEntero(index, false),subCuenta);
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getPrioridadResponsable(String index)
	{
		return (this.responsables.get(Utilidades.convertirAEntero(index, false))).getNroPrioridad()+"";
	}


	/**
	 * @return the existeDistribucionPrevia
	 */
	public boolean isExisteDistribucionPrevia() {
		return existeDistribucionPrevia;
	}


	/**
	 * @param existeDistribucionPrevia the existeDistribucionPrevia to set
	 */
	public void setExisteDistribucionPrevia(boolean existeDistribucionPrevia) {
		this.existeDistribucionPrevia = existeDistribucionPrevia;
	}


	/**
	 * @return the filtroDistribucion
	 */
	public HashMap getFiltroDistribucion() {
		return filtroDistribucion;
	}


	/**
	 * @param filtroDistribucion the filtroDistribucion to set
	 */
	public void setFiltroDistribucion(HashMap filtroDistribucion) {
		this.filtroDistribucion = filtroDistribucion;
	}

	/**
	 * @return the filtroDistribucion
	 */
	public Object getFiltroDistribucion(String key) 
	{
		return filtroDistribucion.get(key);
	}


	/**
	 * @param filtroDistribucion the filtroDistribucion to set
	 */
	public void setFiltroDistribucion(String key,Object value) 
	{
		this.filtroDistribucion.put(key, value);
	}


	/**
	 * @return the infoIngreso
	 */
	public HashMap getInfoIngreso() 
	{
		return infoIngreso;
	}


	/**
	 * @param infoIngreso the infoIngreso to set
	 */
	public void setInfoIngreso(HashMap infoIngreso) {
		this.infoIngreso = infoIngreso;
	}

	/**
	 * @return the infoIngreso
	 */
	public Object getInfoIngreso(String key) 
	{
		return infoIngreso.get(key);
	}


	/**
	 * @param infoIngreso the infoIngreso to set
	 */
	public void setInfoIngreso(String key,Object value) 
	{
		this.infoIngreso.put(key, value);
	}


	/**
	 * @return the paramDistribucion
	 */
	public Object getParamDistribucion(String key) 
	{
		return paramDistribucion.get(key);
	}


	/**
	 * @param paramDistribucion the paramDistribucion to set
	 */
	public void setParamDistribucion(String key,Object value) 
	{
		this.paramDistribucion.put(key, value);
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
	 * @return the paramDistribucion
	 */
	public HashMap getParamDistribucion() {
		return paramDistribucion;
	}


	/**
	 * @param paramDistribucion the paramDistribucion to set
	 */
	public void setParamDistribucion(HashMap paramDistribucion) {
		this.paramDistribucion = paramDistribucion;
	}


	/**
	 * @return the requisitosPaciente
	 */
	public HashMap getRequisitosPaciente() {
		return requisitosPaciente;
	}


	/**
	 * @param requisitosPaciente the requisitosPaciente to set
	 */
	public void setRequisitosPaciente(HashMap requisitosPaciente) {
		this.requisitosPaciente = requisitosPaciente;
	}

	/**
	 * @return the requisitosPaciente
	 */
	public Object getRequisitosPaciente(String key) 
	{
		return requisitosPaciente.get(key);
	}


	/**
	 * @param requisitosPaciente the requisitosPaciente to set
	 */
	public void setRequisitosPaciente(String key,Object value) 
	{
		this.requisitosPaciente.put(key,value);
	}


	/**
	 * @return the nuevoResposable
	 */
	public boolean isNuevoResposable() {
		return nuevoResposable;
	}


	/**
	 * @param nuevoResposable the nuevoResposable to set
	 */
	public void setNuevoResposable(boolean nuevoResposable) {
		this.nuevoResposable = nuevoResposable;
	}
	
	/**
	 * @return the contratosConvenio
	 */
	public ArrayList<HashMap<String, Object>> getContratosConvenio() {
		return contratosConvenio;
	}

	/**
	 * @param contratosConvenio the contratosConvenio to set
	 */
	public void setContratosConvenio(
			ArrayList<HashMap<String, Object>> contratosConvenio) {
		this.contratosConvenio = contratosConvenio;
	}

	
	/**
	 * Retorna el n贸mero de contratos del arreglo de contratos
	 * @return
	 */
	public int getNumContratosConvenio()
	{
		return this.contratosConvenio.size();
	}

	/**
	 * @return the estratosSocialesConvenio
	 */
	public HashMap getEstratosSocialesConvenio() {
		return estratosSocialesConvenio;
	}

	/**
	 * @param estratosSocialesConvenio the estratosSocialesConvenio to set
	 */
	public void setEstratosSocialesConvenio(HashMap estratosSocialesConvenio) {
		this.estratosSocialesConvenio = estratosSocialesConvenio;
	}


	/**
	 * @return the montosCobroConvenio
	 */
	public ArrayList<HashMap<String, Object>> getMontosCobroConvenio() {
		return montosCobroConvenio;
	}


	/**
	 * @param montosCobroConvenio the montosCobroConvenio to set
	 */
	public void setMontosCobroConvenio(
			ArrayList<HashMap<String, Object>> montosCobroConvenio) {
		this.montosCobroConvenio = montosCobroConvenio;
	}


	/**
	 * @return the naturalezasPaciente
	 */
	public Vector<InfoDatosString> getNaturalezasPaciente() {
		return naturalezasPaciente;
	}


	/**
	 * @param naturalezasPaciente the naturalezasPaciente to set
	 */
	public void setNaturalezasPaciente(Vector<InfoDatosString> naturalezasPaciente) {
		this.naturalezasPaciente = naturalezasPaciente;
	}


	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	/**
	 * @return the puedoModificar
	 */
	public boolean isPuedoModificar() {
		return puedoModificar;
	}


	/**
	 * @param puedoModificar the puedoModificar to set
	 */
	public void setPuedoModificar(
			boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
	}


	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}


	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}


	/**
	 * @return the modificacionResponsable
	 */
	public boolean isModificacionResponsable() {
		return modificacionResponsable;
	}


	/**
	 * @param modificacionResponsable the modificacionResponsable to set
	 */
	public void setModificacionResponsable(boolean modificacionResponsable) {
		this.modificacionResponsable = modificacionResponsable;
	}


	/**
	 * @return the fechaAfiliacion
	 */
	public String getFechaAfiliacion() {
		return fechaAfiliacion;
	}


	/**
	 * @param fechaAfiliacion the fechaAfiliacion to set
	 */
	public void setFechaAfiliacion(String fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}


	/**
	 * @return the indiceReponsableEliminar
	 */
	public String getIndiceReponsableEliminar() {
		return indiceReponsableEliminar;
	}


	/**
	 * @param indiceReponsableEliminar the indiceReponsableEliminar to set
	 */
	public void setIndiceReponsableEliminar(String indiceReponsableEliminar) {
		this.indiceReponsableEliminar = indiceReponsableEliminar;
	}


	/**
	 * @return the responsablesEliminados
	 */
	public ArrayList<DtoSubCuentas> getResponsablesEliminados() {
		return responsablesEliminados;
	}


	/**
	 * @param responsablesEliminados the responsablesEliminados to set
	 */
	public void setResponsablesEliminados(
			ArrayList<DtoSubCuentas> responsablesEliminados) {
		this.responsablesEliminados = responsablesEliminados;
	}


	/**
	 * @return the indiceFiltroDistribucion
	 */
	public String getIndiceFiltroDistribucion() {
		return indiceFiltroDistribucion;
	}


	/**
	 * @param indiceFiltroDistribucion the indiceFiltroDistribucion to set
	 */
	public void setIndiceFiltroDistribucion(String indiceFiltroDistribucion) {
		this.indiceFiltroDistribucion = indiceFiltroDistribucion;
	}


	/**
	 * @return the modificarMontoAutorizado
	 */
	public boolean isModificarMontoAutorizado() {
		return modificarMontoAutorizado;
	}


	/**
	 * @param modificarMontoAutorizado the modificarMontoAutorizado to set
	 */
	public void setModificarMontoAutorizado(boolean modificarMontoAutorizado) {
		this.modificarMontoAutorizado = modificarMontoAutorizado;
	}


	/**
	 * @return the detSolicitudesPaciente
	 */
	public HashMap getDetSolicitudesPaciente() {
		return detSolicitudesPaciente;
	}


	/**
	 * @param detSolicitudesPaciente the detSolicitudesPaciente to set
	 */
	public void setDetSolicitudesPaciente(HashMap detSolicitudesPaciente) {
		this.detSolicitudesPaciente = detSolicitudesPaciente;
	}

	/**
	 * @return the detSolicitudesPaciente
	 */
	public Object getDetSolicitudesPaciente(String key) 
	{
		return detSolicitudesPaciente.get(key);
	}


	/**
	 * @param detSolicitudesPaciente the detSolicitudesPaciente to set
	 */
	public void setDetSolicitudesPaciente(String key,Object value) 
	{
		this.detSolicitudesPaciente.put(key, value);
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the busquedaAvanzada
	 */
	public boolean isBusquedaAvanzada() {
		return busquedaAvanzada;
	}


	/**
	 * @param busquedaAvanzada the busquedaAvanzada to set
	 */
	public void setBusquedaAvanzada(boolean busquedaAvanzada) {
		this.busquedaAvanzada = busquedaAvanzada;
	}


	/**
	 * @return the parametrosBusquedaAvanzada
	 */
	public HashMap getParametrosBusquedaAvanzada() {
		return parametrosBusquedaAvanzada;
	}


	/**
	 * @param parametrosBusquedaAvanzada the parametrosBusquedaAvanzada to set
	 */
	public void setParametrosBusquedaAvanzada(HashMap parametrosBusquedaAvanzada) {
		this.parametrosBusquedaAvanzada = parametrosBusquedaAvanzada;
	}
	

	/**
	 * @return the parametrosBusquedaAvanzada
	 */
	public Object getParametrosBusquedaAvanzada(String key) 
	{
		return parametrosBusquedaAvanzada.get(key);
	}


	/**
	 * @param parametrosBusquedaAvanzada the parametrosBusquedaAvanzada to set
	 */
	public void setParametrosBusquedaAvanzada(String key, Object value) 
	{
		this.parametrosBusquedaAvanzada.put(key, value);
	}


	/**
	 * @return the distribucionSolicitud
	 */
	public HashMap getDistribucionSolicitud() {
		return distribucionSolicitud;
	}


	/**
	 * @param distribucionSolicitud the distribucionSolicitud to set
	 */
	public void setDistribucionSolicitud(HashMap distribucionSolicitud) {
		this.distribucionSolicitud = distribucionSolicitud;
	}

	/**
	 * @return the distribucionSolicitud
	 */
	public Object getDistribucionSolicitud(String key) 
	{
		return distribucionSolicitud.get(key);
	}


	/**
	 * @param distribucionSolicitud the distribucionSolicitud to set
	 */
	public void setDistribucionSolicitud(String key,Object value) 
	{
		this.distribucionSolicitud.put(key, value);
	}


	/**
	 * @return the indiceDetSol
	 */
	public String getIndiceDetSol() {
		return indiceDetSol;
	}


	/**
	 * @param indiceDetSol the indiceDetSol to set
	 */
	public void setIndiceDetSol(String indiceDetSol) {
		this.indiceDetSol = indiceDetSol;
	}


	/**
	 * @return the indiceReponsable
	 */
	public int getIndiceReponsable() {
		return indiceReponsable;
	}


	/**
	 * @param indiceReponsable the indiceReponsable to set
	 */
	public void setIndiceReponsable(int indiceReponsable) {
		this.indiceReponsable = indiceReponsable;
	}


	/**
	 * @return the convenioDefectoVia
	 */
	public int getConvenioDefectoVia() {
		return convenioDefectoVia;
	}


	/**
	 * @param convenioDefectoVia the convenioDefectoVia to set
	 */
	public void setConvenioDefectoVia(int convenioDefectoVia) {
		this.convenioDefectoVia = convenioDefectoVia;
	}


	/**
	 * @return the ultimaViaIngreso
	 */
	public int getUltimaViaIngreso() {
		return ultimaViaIngreso;
	}


	/**
	 * @param ultimaViaIngreso the ultimaViaIngreso to set
	 */
	public void setUltimaViaIngreso(int ultimaViaIngreso) {
		this.ultimaViaIngreso = ultimaViaIngreso;
	}


	/**
	 * @return the responsablePaquete
	 */
	public String getResponsablePaquete() {
		return responsablePaquete;
	}


	/**
	 * @param responsablePaquete the responsablePaquete to set
	 */
	public void setResponsablePaquete(String responsablePaquete) {
		this.responsablePaquete = responsablePaquete;
	}


	/**
	 * @return the liquidarAutomaticamente
	 */
	public boolean isLiquidarAutomaticamente() {
		return liquidarAutomaticamente;
	}


	/**
	 * @param liquidarAutomaticamente the liquidarAutomaticamente to set
	 */
	public void setLiquidarAutomaticamente(boolean liquidarAutomaticamente) {
		this.liquidarAutomaticamente = liquidarAutomaticamente;
	}


	/**
	 * @return the mostrarMensaje
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}


	/**
	 * @param mostrarMensaje the mostrarMensaje to set
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}


	/**
	 * @return the vigenciaTopesContratos
	 */
	public HashMap getVigenciaTopesContratos() {
		return vigenciaTopesContratos;
	}


	/**
	 * @param vigenciaTopesContratos the vigenciaTopesContratos to set
	 */
	public void setVigenciaTopesContratos(HashMap vigenciaTopesContratos) {
		this.vigenciaTopesContratos = vigenciaTopesContratos;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getDetSolicitudesResponsable() {
		return detSolicitudesResponsable;
	}

	/**
	 * 
	 * @param detSolicitudesResponsable
	 */
	public void setDetSolicitudesResponsable(HashMap detSolicitudesResponsable) {
		this.detSolicitudesResponsable = detSolicitudesResponsable;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getDetSolicitudesResponsable(String key) 
	{
		return detSolicitudesResponsable.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setDetSolicitudesResponsable(String key,Object value) 
	{
		this.detSolicitudesResponsable.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getParametrosBusquedaAvanzadaResponsable() {
		return parametrosBusquedaAvanzadaResponsable;
	}

	/**
	 * 
	 * @param parametrosBusquedaAvanzadaResponsable
	 */
	public void setParametrosBusquedaAvanzadaResponsable(
			HashMap parametrosBusquedaAvanzadaResponsable) {
		this.parametrosBusquedaAvanzadaResponsable = parametrosBusquedaAvanzadaResponsable;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getParametrosBusquedaAvanzadaResponsable(String key) 
	{
		return parametrosBusquedaAvanzadaResponsable.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setParametrosBusquedaAvanzadaResponsable(String key, Object value) 
	{
		this.parametrosBusquedaAvanzadaResponsable.put(key, value);
	}


	/**
	 * @return the paginaSiguiente
	 */
	public String getPaginaSiguiente() {
		return paginaSiguiente;
	}


	/**
	 * @param paginaSiguiente the paginaSiguiente to set
	 */
	public void setPaginaSiguiente(String paginaSiguiente) {
		this.paginaSiguiente = paginaSiguiente;
	}


	/**
	 * @return the pacienteCargado
	 */
	public boolean isPacienteCargado() {
		return pacienteCargado;
	}


	/**
	 * @param pacienteCargado the pacienteCargado to set
	 */
	public void setPacienteCargado(boolean pacienteCargado) {
		this.pacienteCargado = pacienteCargado;
	}


	/**
	 * @return the pathFacturacion
	 */
	public String getPathRedireccion() {
		return pathRedireccion;
	}


	/**
	 * @param pathFacturacion the pathFacturacion to set
	 */
	public void setPathRedireccion(String pathFacturacion) {
		this.pathRedireccion = pathFacturacion;
	}


	/**
	 * @return the descTipoComplejidad
	 */
	public String getDescTipoComplejidad() {
		return descTipoComplejidad;
	}


	/**
	 * @param descTipoComplejidad the descTipoComplejidad to set
	 */
	public void setDescTipoComplejidad(String descTipoComplejidad) {
		this.descTipoComplejidad = descTipoComplejidad;
	}


	/**
	 * @return the coberturaAsignadas
	 */
	public HashMap getCoberturaAsignadas() {
		return coberturaAsignadas;
	}


	/**
	 * @param coberturaAsignadas the coberturaAsignadas to set
	 */
	public void setCoberturaAsignadas(HashMap coberturaAsignadas) {
		this.coberturaAsignadas = coberturaAsignadas;
	}

	/**
	 * @return the coberturaAsignadas
	 */
	public Object getCoberturaAsignadas(String key) 
	{
		return coberturaAsignadas.get(key);
	}


	/**
	 * @param coberturaAsignadas the coberturaAsignadas to set
	 */
	public void setCoberturaAsignadas(String key,Object value) 
	{
		this.coberturaAsignadas.put(key, value);
	}


	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}


	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}


	/**
	 * @return the registrandoDistribucion
	 */
	public boolean isRegistrandoDistribucion() {
		return registrandoDistribucion;
	}


	/**
	 * @param registrandoDistribucion the registrandoDistribucion to set
	 */
	public void setRegistrandoDistribucion(boolean registrandoDistribucion) {
		this.registrandoDistribucion = registrandoDistribucion;
	}


	public ArrayList getTiposIdentificacion() {
		return tiposIdentificacion;
	}


	public void setTiposIdentificacion(ArrayList tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}


	public ArrayList<String> getMensajes() {
		return mensajes;
	}
	

	public int getSizeMensajes() {
		return mensajes.size();
	}


	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
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

	public HashMap getEsquemasInventario() {
		return esquemasInventario;
	}

	public void setEsquemasInventario(HashMap esquemasInventario) {
		this.esquemasInventario = esquemasInventario;
	}

	public HashMap getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}

	public void setEsquemasProcedimientos(HashMap esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}


	public Object getEsquemasInventario(String key) 
	{
		return esquemasInventario.get(key);
	}

	public void setEsquemasInventario(String key,Object value) 
	{
		this.esquemasInventario.put(key, value);
	}

	public Object getEsquemasProcedimientos(String key) 
	{
		return esquemasProcedimientos.get(key);
	}

	public void setEsquemasProcedimientos(String key,Object value) 
	{
		this.esquemasProcedimientos.put(key, value);
	}

	public HashMap getEsquemasInventarioEliminados() {
		return esquemasInventarioEliminados;
	}

	public void setEsquemasInventarioEliminados(HashMap esquemasInventarioEliminados) {
		this.esquemasInventarioEliminados = esquemasInventarioEliminados;
	}

	public HashMap getEsquemasProcedimientosEliminados() {
		return esquemasProcedimientosEliminados;
	}

	public void setEsquemasProcedimientosEliminados(
			HashMap esquemasProcedimientosEliminados) {
		this.esquemasProcedimientosEliminados = esquemasProcedimientosEliminados;
	}

	public int getPosEliminar() {
		return posEliminar;
	}

	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}

	public Boolean getControlMensaje() {
		return controlMensaje;
	}

	public void setControlMensaje(Boolean controlMensaje) {
		this.controlMensaje = controlMensaje;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the distribucionSolicitudesPorPorcentaje
	 */
	public HashMap getDistribucionSolicitudesPorPorcentaje()
	{
		return distribucionSolicitudesPorPorcentaje;
	}

	/**
	 * @param distribucionSolicitudesPorPorcentaje the distribucionSolicitudesPorPorcentaje to set
	 */
	public void setDistribucionSolicitudesPorPorcentaje(HashMap distribucionSolicitudesPorPorcentaje)
	{
		this.distribucionSolicitudesPorPorcentaje = distribucionSolicitudesPorPorcentaje;
	}
	
	/**
	 * @param distribucionSolicitudesPorPorcentaje the distribucionSolicitudesPorPorcentaje to set
	 */
	public Object getDistribucionSolicitudesPorPorcentaje(String key) 
	{
		return distribucionSolicitudesPorPorcentaje.get(key);
	}

	/**
	 * @param distribucionSolicitudesPorPorcentaje the distribucionSolicitudesPorPorcentaje to set
	 */
	public void setDistribucionSolicitudesPorPorcentaje(String key,Object value) 
	{
		this.distribucionSolicitudesPorPorcentaje.put(key, value);
	}

	/**
	 * @return the coberturaSalud
	 */
	public ArrayList<HashMap<String, Object>> getCoberturaSalud() {
		return coberturaSalud;
	}

	/**
	 * @param coberturaSalud the coberturaSalud to set
	 */
	public void setCoberturaSalud(ArrayList<HashMap<String, Object>> coberturaSalud) {
		this.coberturaSalud = coberturaSalud;
	}
	
	/**
	 * Numero de coberturas salud
	 * @return
	 */
	public int getNumCoberturasSalud()
	{
		return this.coberturaSalud.size();
	}

	/**
	 * @return the mensajesAlerta
	 */
	public ArrayList<ElementoApResource> getMensajesAlerta() {
		return mensajesAlerta;
	}

	/**
	 * @param mensajesAlerta the mensajesAlerta to set
	 */
	public void setMensajesAlerta(ArrayList<ElementoApResource> mensajesAlerta) {
		this.mensajesAlerta = mensajesAlerta;
	}
	
	/**
	 * Numero de mensajes de alerta
	 * @return
	 */
	public int getNumMensajesAlerta()
	{
		return this.mensajesAlerta.size();
	}
	
	//----------------------------------------
	public HashMap getCuerpoDetalle() {
		return cuerpoDetalle;
	}
	public void setCuerpoDetalle(HashMap cuerpoDetalle) {
		this.cuerpoDetalle = cuerpoDetalle;
	}
	public Object getCuerpoDetalle(String key) {
		return cuerpoDetalle.get(key);
	}
	public void setCuerpoDetalle(String key,Object value) {
		this.cuerpoDetalle.put(key, value);
	}
	//-------------------------------------------------
	
	//-------------------------------------------------
	public HashMap getCuerpoDetalleOld() {
		return cuerpoDetalleOld;
	}
	public void setCuerpoDetalleOld(HashMap cuerpoDetalleOld) {
		this.cuerpoDetalleOld = cuerpoDetalleOld;
	}
	
	public Object getCuerpoDetalleOld(String key) {
		return cuerpoDetalleOld.get(key);
	}
	public void setCuerpoDetalleOld(String key,Object value) {
		this.cuerpoDetalleOld.put(key, value);
	}
	//-------------------------------------------------
	
	public void resetDetalle ()
	{
		this.listadoIngresos= new HashMap ();
		this.setListadoIngresos("numRegistros", 0);
		this.encabezadoDetalle = new HashMap ();
		this.setEncabezadoDetalle("numRegistros", 0);
		//this.convenios = new ArrayList<HashMap<String,Object>>();
		//this.convenio=ConstantesBD.codigoNuncaValido+"";
		this.cuerpoDetalle= new HashMap ();
		this.setCuerpoDetalle("numRegistros", 0);
		this.setCuerpoDetalleOld("numRegistros",0);
		//this.busquedaAvanzada = new HashMap ();
		//this.operacionTrue=false;
		//this.estadoIngreso="";
	}
	
	/**
	 * @return the ordenesAmbulatorias
	 */
	public boolean isOrdenesAmbulatorias() {
		return ordenesAmbulatorias;
	}
	/**
	 * @param ordenesAmbulatorias the ordenesAmbulatorias to set
	 */
	public void setOrdenesAmbulatorias(boolean ordenesAmbulatorias) {
		this.ordenesAmbulatorias = ordenesAmbulatorias;
	}
	
	public HashMap getListadoIngresos() {
		return listadoIngresos;
	}
	public void setListadoIngresos(HashMap listadoIngresos) {
		this.listadoIngresos = listadoIngresos;
	}
	public Object getListadoIngresos(String key) {
		return listadoIngresos.get(key);
	}
	public void setListadoIngresos(String key,Object value) {
		this.listadoIngresos.put(key, value);
	}
	
	public HashMap getEncabezadoDetalle() {
		return encabezadoDetalle;
	}
	public void setEncabezadoDetalle(HashMap encabezadoDetalle) {
		this.encabezadoDetalle = encabezadoDetalle;
	}
	public Object  getEncabezadoDetalle(String key) {
		return encabezadoDetalle.get(key);
	}
	public void setEncabezadoDetalle(String key,Object value) {
		this.encabezadoDetalle.put(key, value);
	}
	//---------------------------------------------

	public HashMap getTiposAfiliadoConvenio() {
		return tiposAfiliadoConvenio;
	}

	public void setTiposAfiliadoConvenio(HashMap tiposAfiliadoConvenio) {
		this.tiposAfiliadoConvenio = tiposAfiliadoConvenio;
	}

	public boolean isPuedoGrabarConvenioAdicional() {
		return puedoGrabarConvenioAdicional;
	}

	public void setPuedoGrabarConvenioAdicional(boolean puedoGrabarConvenioAdicional) {
		this.puedoGrabarConvenioAdicional = puedoGrabarConvenioAdicional;
	}

	/**
	 * @return the tiposAfiliadolista
	 */
	public HashMap getTiposAfiliadolista() {
		return tiposAfiliadolista;
	}

	/**
	 * @param tiposAfiliadolista the tiposAfiliadolista to set
	 */
	public void setTiposAfiliadolista(HashMap tiposAfiliadolista) {
		this.tiposAfiliadolista = tiposAfiliadolista;
	}



	/**
	 * @return the montosCombro
	 */
	public DtoMontoCobro getMontosCombro() {
		return montosCombro;
	}

	/**
	 * @param montosCombro the montosCombro to set
	 */
	public void setMontosCombro(DtoMontoCobro montosCombro) {
		this.montosCombro = montosCombro;
	}

	/**
	 * @return the montosCobroResumen
	 */
	public HashMap getMontosCobroResumen() {
		return montosCobroResumen;
	}

	/**
	 * @param montosCobroResumen the montosCobroResumen to set
	 */
	public void setMontosCobroResumen(HashMap montosCobroResumen) {
		this.montosCobroResumen = montosCobroResumen;
	}
	
	/**
	 * @return the verificacion
	 */
	public HashMap<String, Object> getVerificacion() {
		return verificacion;
	}
	/**
	 * @param verificacion the verificacion to set
	 */
	public void setVerificacion(HashMap<String, Object> verificacion) {
		this.verificacion = verificacion;
	}
	/**
	 * @return elemento del mapa  verificacion
	 */
	public Object getVerificacion(String key) {
		return verificacion.get(key);
	}
	/**
	 * @param Asigna elemento al mapa verificacion 
	 */
	public void setVerificacion(String key,Object obj) {
		this.verificacion.put(key,obj);
	}
	

}