/*
 * @(#)IngresoPacienteForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.dto.historiaClinica.DtoClasificacionesTriage;
import com.princetonsa.mundo.triage.Triage;

/**
 * Forma en la que se manejan datos intrinsecos al ingreso
 * del paciente. No tiene muchos datos ya que inicialmente
 * se manejo todo con JSP's.
 *
 *	@version 1.0, Aug 19, 2003
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class IngresoPacienteForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//******************ATRIBUTOS QUE HACEN REFERENCIA A OTRAS FUNCIONALIDADES**************************
	/** Indica si el ingreso se inicio desde la funcionalidad del reserva de citas */
	private boolean ib_ingresoDesdeReservaCita;
	
	/** Indica si el ingreso se inicio desde la funcionalidad del referencia */
	private boolean ingresoDesdeReferencia;
	/**
	 * Variables que me indican si debo abrir la referencia despues de crear la cuenta
	 */
	private boolean deboAbrirReferencia = false;
	private String pathReferencia = "";
	/**
	 * Variable que me indica si debo abrir el registro de accidentes de transito despues de crear la cuenta
	 */
	private boolean deboAbrirAccidentesTransito = false;
	/**
	 * Variable que me indica si debo abrir el registro de evento catastrofico despues de crear la cuenta
	 */
	private boolean deboAbrirEventoCatastrofico = false;
	/**
	 * Variable que me indica si debo abrir la asignacion de citas despues de crear la cuenta de consulta externa
	 */
	private boolean deboAbrirAsignacionCitas = false;
	
	/**
	 * 
	 */
	private String pathAsignacionCitas = "";
	
	/**
	 * Variable que me indica si debo imprimir la admision
	 */
	private boolean deboImprimirAdmision = false;
	
	
	
	//*****************************************************************************************************

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	
	//****************************************************************************
	//****************ATRIBUTOS DEL PACIENTE ***************************************
	//********************************************************************************
	private String numeroHistoriaClinica;
	private String anioHistoriaClincia;
	private String tipoIdentificacion=""; //Maneja el codigoTipo - nombreTipo - esConsecutivo
	private String codigoTipoIdentificacion="";
	private String nombreTipoIdentificacion="";
	private String numeroIdentificacion="";
	private String codigoPaisId;
	private String codigoCiudadId;
	private String primerApellidoPersona;
	private String segundoApellidoPersona;
	private String primerNombrePersona;
	private String segundoNombrePersona;
	private String codigoPaisNacimiento;
	private String codigoCiudadNacimiento;
	private boolean ingresoEdad; //para saber si se debe ingresar edad
	private String anios;
	private String seleccionEdad;
	private String fechaNacimiento;
	private String codigoPaisResidencia;
	private String codigoCiudadResidencia;
	private String codigoBarrio;
	private String nombreBarrio;
	private String criterioBarrio;
	private String codigoLocalidad;
	private String nombreLocalidad;
	private String direccion;
	private String telefono;
	private String telefonoFijo;
	private String telefonoCelular;
	private String email;
	private String zonaDomicilio;
	private String ocupacion;
	private String sexo;
	private String tipoSangre;
	private String tipoPersona;
	private String estadoCivil;
	private String centroAtencion;
	private String etnia;
	private String religion;
	private String leeEscribe;
	private String estudio;
	private String grupoPoblacional;
	private String foto;
	private boolean existePaciente; //indicador para saber si el paciente ya existe o es nuevo
	private boolean identificacionAutomatica; //me indica si maneja identificación automática 
	
	//Atributos del paciente para el manejo del triage
	private String pacienteTriage;
	private String consecutivoTriage;
	private String consecutivoFechaTriage;
	private boolean esPacienteTriage;
	
	
	//Atributos del paciente para el manejo de capitacion
	private boolean esPacienteCapitado;
	private boolean usuarioConvenio ;
	private String codigoConvenioCapitacion;
	private String codigoUsuarioCapitado;
	private boolean usuarioSinCapitacionVigente;
	private String fichaUsuarioCapitado = "";
	private HashMap<String, Object> datosCapitacion = new HashMap<String, Object>();
	
	
	private boolean puedoGrabarConvenioAdicional;
	
	 
	//Atributos para el manejo de validacion de pacientes con mismo numero de identificacion
	private boolean avisoValidacionNumerosId;
	/**
	 * Objeto implementado para mostrar las personas
	 * que tienen el mismo numero id pero diferente tipo identificacion
	 * cuando se ingresa un paciente
	 */
	private HashMap mapaNumerosId = new HashMap();
	//Atributos para el manejo de validacion de pacientes con mismos nombres
	private boolean avisoValidacionMismosNombres;
	
	//atributos para el manejo de la validacion usuario paciente
	private boolean usuarioPaciente;
	private String loginUsuarioPaciente;
	
	
	//Arreglos que tienen estructuras de datos para el ingreso del paciente
	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudades = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudadesExp = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudadesNac = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> localidades = new ArrayList<HashMap<String,Object>>();
	private HashMap zonasDomicilio = new HashMap();
	private HashMap ocupaciones = new HashMap();
	private ArrayList<HashMap<String,Object>> sexos = new ArrayList<HashMap<String,Object>>();
	private HashMap tiposSangre = new HashMap();
	private HashMap estadosCiviles = new HashMap();
	private HashMap centrosAtencion = new HashMap();
	private ArrayList<HashMap<String, Object>> etnias = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> religiones = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> estudios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> pacientesMismosNombres = new ArrayList<HashMap<String,Object>>();
	
	//********************************************************************************
	//********************************************************************************
	//********************************************************************************
	//****************ATRIBUTOS DE LA CUENTA *****************************************
	private HashMap<String, Object> cuenta = new HashMap<String, Object>();
	private HashMap<String, Object> variosConvenios = new HashMap<String, Object>(); //para el manejo de varios convenios
	private HashMap<String, Object> verificacion = new HashMap<String, Object>(); //para el manejo de la verificacion de derechos
	private HashMap<String, Object> responsable = new HashMap<String, Object>(); //para el manejo del responsable del paciente
	private HashMap<String, Object> hospitalizacion = new HashMap<String, Object>(); //para el manejo de la hospitalizacion
	private HashMap<String, Object> ingresoIncompleto = new HashMap<String, Object>(); //para el manejo del ingreso incompleto
	private int posConvenio; 
	private boolean nuevoConvenio;
	private int numConvenios;
	
	//Atributos para el manejo de preingresos
	private String preingreso;
	private int codIngresoPreingreso=ConstantesBD.codigoNuncaValido;
	
	//Atributo para el manejo 
	private ArrayList <InfoDatosString> tipoAsocio;
	
	//Atributos para el manejo del filtro de vias de ingreso
	private String codigoViaIngreso="";
	
	//Atributos para el manejo del filtro de tipo paciente
	private String codigoTipoPaciente="";
	
	//Atributos para el manejo del filtro de convenios
	private String codigoConvenio = "";
	
	/**
	 * Variable que indica si debo enviar registrar el Informe de Iconsistencias
	 */
	private boolean registroInconsistencias;
	
	//Atributos para el manejo del filtro de contratos
	private String codigoContrato = "";
	
	
	
	//Atributos para el filtro de los Tipos de Monitoreo por Area Seleccionada
	private String areaSel="";
	
	//Atributos para el manejo del filtro de fecha afiliacion
	private String fechaAfiliacion = "";
	
	//Atributos para el filtro de responsable paciente
	private String codigoTipoIdResponsable;
	private String numeroIdResponsable;
	private String fechaNacimientoResponsable;
	
	//Atributos para validaciones al guardar la cuenta
	private boolean requeridoDocumentoGarantias;
	private boolean requeridoDeudor;
	private ArrayList<String> alertasSemanasCotizacion = new ArrayList<String>();
	
	//Arreglos que tienen estructuras de datos para el ingreso de la cuenta
	private Vector<InfoDatosString> viasIngreso = new Vector<InfoDatosString>();
	private ArrayList<HashMap<String, Object>> origenesAdmisiones = new ArrayList<HashMap<String,Object>>();
	private HashMap conveniosArp = new HashMap();
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> contratos = new ArrayList<HashMap<String,Object>>();
	private Vector<InfoDatosString> tiposComplejidad = new Vector<InfoDatosString>();
	private HashMap estratosSociales = new HashMap();
	private HashMap tiposAfiliado= new HashMap();
	private ArrayList<HashMap<String, Object>> montosCobro = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> tiposPaciente = new ArrayList<HashMap<String,Object>>();
	private HashMap areas = new HashMap();
	private Vector<InfoDatosString> naturalezasPaciente = new Vector<InfoDatosString>();
	private ArrayList<HashMap<String, Object>> coberturasSalud = new ArrayList<HashMap<String,Object>>(); 
	private ArrayList<HashMap<String, Object>> tiposIdentificacion = new ArrayList<HashMap<String,Object>>(); //se usa para la poliza
	
	//Arreglos que tienen estructuras de datos para el ingreso del convenio
	private ArrayList<HashMap<String, Object>> contratosConvenio = new ArrayList<HashMap<String,Object>>();
	private HashMap estratosSocialesConvenio = new HashMap();
	private HashMap tiposAfiliadoConvenio = new HashMap();
	private ArrayList<HashMap<String, Object>> montosCobroConvenio = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> coberturasSaludConvenio = new ArrayList<HashMap<String,Object>>();
	
	//Arreglos que tienen estructuras de datos para el ingreso del responsable
	private ArrayList<HashMap<String, Object>> tiposIdResponsable = new ArrayList<HashMap<String,Object>>();
	
	//Arreglos que tienen estructuras de datos para el ingreso de la admision hospitalaria
	private ArrayList<HashMap<String, Object>> profesionales = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> farmacias = new ArrayList<HashMap<String,Object>>();
	private HashMap camas = new HashMap();
	
	//Atributos para el manejo de la selección de convenios del paciente
	private HashMap<String, Object> conveniosPaciente = new HashMap<String, Object>(); //para el manejo de los convenios del paciente
	private HashMap<String, Object> conveniosPostulados = new HashMap<String, Object>(); //para el manejo de los convenios seleccionados por el usuario
	private int posSeleccion; //variable que lleva la posicion del convenio seleccionado
	private boolean huboSeleccionConvenio; //variable que me dice si hubo seleccion de convenios
	
	private boolean fueModificadoResponsable; //variable que verifica si cambió el responsable
	
	//Atributo para la Evolucion de la Cuenta de Urgencias en caso de Asocio
	private int evolucion;
//	Atributo para la Valoracion de la Cuenta de Urgencias en caso de Asocio
	private int valoracion;
	
	//Arreglo donde se alamcenan las advertencias x convenio adicional
	private ArrayList<ElementoApResource> mensajesConvenioAdicional = new ArrayList<ElementoApResource>();
	//********************************************************************************
	//********************************************************************************
	//**************ATRIBUTOD QUE TIENEN QUE VER CON INTERFAZ**************************
	private String saldoInterfaz;
	
	///***************************************************************************************
	//********************ATRIBUTOS QUE TIENEN QUE VER CON PRESUPUESTO VENEZUELA***************
	private boolean presupuestoPaciente;
	private HashMap<String, Object> presupuestos = new HashMap<String, Object>();
	private int posPresupuesto;
	//*****************************************************************************************
	
	/**
     * Mensaje que informa sobre la parametrizacion del Contrato
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    // Manejo de Mensajes de Advertencia de sin Contrato y Controla Anticipos
    private Boolean controlMensaje;
    
    // Ingresos cerrados pendientes por facturar
    private String ingresosCerradosPendientesXFacturar="";
    
    //Mapa para Mostrar los Tipos de Monitoreo Filtrados por El Area de la Cuenta
    private HashMap tiposMonitoreoMap;    
    
    /**
     * Almacena el numero de autorización pedido para el caso en que  
     * el parametros 'Realizar Comprobacion solo en cargues vigentes' este en Si 
     * */
    private InfoDatosString autorizacionIngEvento = new InfoDatosString("","");
    
    
    // Manejo de valores S/N para la informacion de la Historia en el sistema anterior
    private boolean infoHistoSistemaAnt;
    
    
    private ArrayList<ElementoApResource> mensajesAlerta = new ArrayList<ElementoApResource>();

    private String mensajeSaldoPendiente;
    
    /**
     * 
     */
    private ArrayList<DtoClasificacionesTriage> clasificacionesTriage;
    
    /**
     * 
     */
    private int clasificacionTriage;
    
    private String codigoEstratoSocial = "";
	private String codigoTipoAfiliado = "";
    
    /** 
     * Indica si el convenio es capitado para hacer la validación del anexo 985.
     * @author Cristhian Murillo 
     */ 
    private boolean esConvenioCapitado = false;
    
    /**
     * 
     */
    private String mensajePacientesIgualNNombre="";
    
    
    
    public boolean isEsConvenioCapitado() {
		return esConvenioCapitado;
	}


	public void setEsConvenioCapitado(boolean esConvenioCapitado) {
		this.esConvenioCapitado = esConvenioCapitado;
	}



	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado
	 * @param request 
	 */
	public void reset (HttpServletRequest request)
	{
		ib_ingresoDesdeReservaCita = false;
		ingresoDesdeReferencia = false;
		this.deboAbrirReferencia = false;
		this.pathReferencia = "";
		this.deboAbrirAccidentesTransito = false;
		this.deboAbrirEventoCatastrofico = false;
		this.pathAsignacionCitas="";
		this.deboAbrirAsignacionCitas = false;
		this.deboImprimirAdmision = false;
		
		//***ATRIBUTOS INGRESO DEL PACIENTE***********************************************************
		this.numeroHistoriaClinica = "";
		this.anioHistoriaClincia = "";
		this.codigoPaisId = "";
		this.codigoCiudadId = "";
		//Si no viene del Triage para urgencias se limpia la informacion
		if(!UtilidadTexto.getBoolean(request.getParameter("postularInfoTriage")))
		{
			this.primerApellidoPersona = "";
			this.segundoApellidoPersona = "";
			this.primerNombrePersona = "";
			this.segundoNombrePersona = "";
			this.fechaNacimiento = "";
		}
		this.codigoPaisNacimiento = "";
		this.codigoCiudadNacimiento = "";
		this.ingresoEdad = false;
		this.anios = "";
		this.seleccionEdad = "";
		this.codigoPaisResidencia = "";
		this.codigoCiudadResidencia = "";
		this.codigoBarrio = "";
		this.nombreBarrio = "";
		this.criterioBarrio = "";
		this.codigoLocalidad = "";
		this.nombreLocalidad = "";
		this.direccion = "";
		this.telefono = "";
		this.telefonoFijo = "";
		this.telefonoCelular = "";
		this.email = "";
		this.zonaDomicilio = "";
		this.ocupacion = "";
		this.sexo = "";
		this.tipoSangre = ConstantesBDManejoPaciente.codigoTipoSangreDesconocido;
		this.tipoPersona = "1-Natural";
		this.estadoCivil = ConstantesBD.acronimoEstadoCivilDesconocido;
		this.centroAtencion = "";
		this.etnia = "";
		this.religion = "";
		this.leeEscribe = "";
		this.estudio = "";
		this.grupoPoblacional = "";
		this.foto = "";
		this.existePaciente = false;
		this.identificacionAutomatica = false;
		
		//atributos para el manejo de triage del paciente
		this.pacienteTriage = "";
		this.consecutivoFechaTriage="";
		this.consecutivoTriage="";
		this.esPacienteTriage = false;
		
		//atributos para el manejo de capitacion del paciente
		this.usuarioConvenio = false;
		this.esPacienteCapitado = false;
		this.puedoGrabarConvenioAdicional=true;
		this.codigoConvenioCapitacion = "";
		this.codigoUsuarioCapitado = "";
		this.usuarioSinCapitacionVigente = false;
		this.datosCapitacion = new HashMap<String, Object>();
		
		//atributos para la validacion de pacientes con mismo numeros identificacion
		this.avisoValidacionNumerosId = false;
		this.mapaNumerosId = new HashMap();
		
		//Atributos para el manejo de validacion de pacientes con mismos nombres
		this.avisoValidacionMismosNombres = false;
		
		//Atributos para el manejo de usuario paciente
		this.usuarioPaciente = false;
		this.loginUsuarioPaciente = "";
		
		//Atributos para verificar si es requerido el documento de garantias
		this.requeridoDocumentoGarantias = false;
		this.requeridoDeudor = false;
		this.alertasSemanasCotizacion = new ArrayList<String>();
		
		//arreglos estructuras del ingreso del paciente
		this.paises = new ArrayList<HashMap<String,Object>>();
		this.ciudades = new ArrayList<HashMap<String,Object>>();
		this.ciudadesExp = new ArrayList<HashMap<String,Object>>();
		this.ciudadesNac = new ArrayList<HashMap<String,Object>>();
		this.localidades = new ArrayList<HashMap<String,Object>>();
		this.zonasDomicilio = new HashMap();
		this.ocupaciones = new HashMap();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		this.tiposSangre = new HashMap();
		this.estadosCiviles = new HashMap();
		this.centrosAtencion = new HashMap();
		this.etnias = new ArrayList<HashMap<String,Object>>();
		this.religiones = new ArrayList<HashMap<String,Object>>();
		this.estudios = new ArrayList<HashMap<String,Object>>();
		this.pacientesMismosNombres = new ArrayList<HashMap<String,Object>>();
		//******************************************
		
		//*********ATRIBUTOS PARA LA CREACIÓN DE LA CUENTA********************************
		this.cuenta = new HashMap<String, Object>();
		this.variosConvenios = new HashMap<String, Object>();
		this.verificacion = new HashMap<String, Object>();
		this.responsable = new HashMap<String, Object>();
		this.hospitalizacion = new HashMap<String, Object>();
		this.ingresoIncompleto = new HashMap<String, Object>();
		this.posConvenio = 0;
		this.nuevoConvenio = false;
		this.numConvenios = 0;
		
		//atributos para el manejo del filtro de vias de ingreso
		this.codigoViaIngreso = "";
		
//		atributos para el manejo del filtro de tipo paciente
		this.codigoTipoPaciente = "";
		
		//Atributos para el manejo del filtro de convenios
		this.codigoConvenio = "";
		this.registroInconsistencias=false;
		
		//Atributos para el manejo del filtro de contratos
		this.codigoContrato = "";
		
		//Atributos para el manejo del filtro de los tipo de monitoreo por area seleccionada
		this.areaSel="";
		
		//Atributos para el manejo del filtro de fecha afiliacion
		this.fechaAfiliacion = "";
		
		//Atributos para el filtro de responsable paciente
		this.codigoTipoIdResponsable = "";
		this.numeroIdResponsable = "";
		this.fechaNacimientoResponsable = "";
		
		//arreglos estructuras del ingreso de la cuenta
		this.viasIngreso = new Vector<InfoDatosString>();
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.contratos = new ArrayList<HashMap<String,Object>>();
		this.tiposComplejidad = new Vector<InfoDatosString>();
		this.estratosSociales = new HashMap();
		this.estratosSociales.put("numRegistros","0");
		this.tiposAfiliado = new HashMap();
		this.tiposAfiliado.put("numRegistros","0");
		
		this.montosCobro = new ArrayList<HashMap<String,Object>>();
		this.tiposPaciente = new ArrayList<HashMap<String,Object>>();
		this.areas = new HashMap();
		this.areas.put("numRegistros", "0");
		this.conveniosArp = new HashMap();
		this.origenesAdmisiones = new ArrayList<HashMap<String,Object>>();
		this.naturalezasPaciente = new Vector<InfoDatosString>();
		this.coberturasSalud = new ArrayList<HashMap<String,Object>>();
		this.tiposIdentificacion = new ArrayList<HashMap<String,Object>>();
		
		//arreglos estructuras del ingreso del contrato
		this.contratosConvenio = new ArrayList<HashMap<String,Object>>();
		this.estratosSocialesConvenio = new HashMap();
		this.tiposAfiliadoConvenio = new HashMap();
		this.montosCobroConvenio = new ArrayList<HashMap<String,Object>>();
		this.coberturasSaludConvenio = new ArrayList<HashMap<String,Object>>();
		
		//arreglosd para el ingreso de l resposnable
		this.tiposIdResponsable = new ArrayList<HashMap<String,Object>>();
		
		//arreglo para el ingreso de admision hopitalaria
		this.profesionales = new ArrayList<HashMap<String,Object>>();
		this.farmacias = new ArrayList<HashMap<String,Object>>();
		this.camas = new HashMap();
		
		//Atributos para el manejo de la selección de convenios del paciente
		this.conveniosPaciente = new HashMap<String, Object>(); //para el manejo de los convenios del paciente
		this.conveniosPostulados = new HashMap<String, Object>(); //para el manejo de los convenios seleccionados por el usuario
		this.posSeleccion = 0;
		this.huboSeleccionConvenio = false; //variable que indica su hubo seleccion de convenios den la pagina de seleccion de convenios
		
		this.fueModificadoResponsable = false;
		this.evolucion=0;
		this.valoracion=0;
		this.mensajesConvenioAdicional = new ArrayList<ElementoApResource>();
		//********************************************************************************
		
		//**********ATRIBUTOS INTERFAZ*******************************************
		this.saldoInterfaz = "";
		
		//********************ATRIBUTOS QUE TIENEN QUE VER CON PRESUPUESTO VENEZUELA***************
		this.presupuestoPaciente = false;
		this.presupuestos = new HashMap<String, Object>();
		this.posPresupuesto = 0;	
		
		this.tipoAsocio = new ArrayList();
		
		this.controlMensaje = false;
		
		//Reset del Mapa de los tipos de Monitoreo
		this.tiposMonitoreoMap=new HashMap();
		tiposMonitoreoMap.put("numRegistros", 0);
		
		this.infoHistoSistemaAnt =false;
		
		this.mensajesAlerta = new ArrayList<ElementoApResource>();
		this.mensajeSaldoPendiente=null;
		this.clasificacionesTriage=Triage.consultarClasificacionesTriage();
		this.clasificacionTriage=ConstantesBD.codigoNuncaValido;
		
		this.mensajePacientesIgualNNombre="";
		
		this.setCodigoEstratoSocial("");
		this.setCodigoTipoAfiliado("");
	}
	
	

	/**
		 * @return the coberturasSaludConvenio
		 */
		public ArrayList<HashMap<String, Object>> getCoberturasSaludConvenio() {
			return coberturasSaludConvenio;
		}



		/**
		 * @param coberturasSaludConvenio the coberturasSaludConvenio to set
		 */
		public void setCoberturasSaludConvenio(
				ArrayList<HashMap<String, Object>> coberturasSaludConvenio) {
			this.coberturasSaludConvenio = coberturasSaludConvenio;
		}
		
		/**
		 * Método para obtener el número de coberturas saluid x convenio
		 * @return
		 */
		public int getNumCoberturasSaludConvenio()
		{
			return this.coberturasSaludConvenio.size();
		}



	/**
		 * @return the farmacias
		 */
		public ArrayList<HashMap<String, Object>> getFarmacias() {
			return farmacias;
		}



		/**
		 * @param farmacias the farmacias to set
		 */
		public void setFarmacias(ArrayList<HashMap<String, Object>> farmacias) {
			this.farmacias = farmacias;
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
		ActionErrors errors =super.validate(mapping, request);
		
		return errors;
	}

	/**
	 * @return the infoHistoSistemaAnt
	 */
	public boolean isInfoHistoSistemaAnt() {
		return infoHistoSistemaAnt;
	}



	/**
	 * @param infoHistoSistemaAnt the infoHistoSistemaAnt to set
	 */
	public void setInfoHistoSistemaAnt(boolean infoHistoSistemaAnt) {
		this.infoHistoSistemaAnt = infoHistoSistemaAnt;
	}



	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	
	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * @return
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @return
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param string
	 */
	public void setNumeroIdentificacion(String string) {
		numeroIdentificacion = string;
	}

	/**
	 * @param string
	 */
	public void setTipoIdentificacion(String string) {
		tipoIdentificacion = string;
	}

	/**
	 * @return
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * @param string
	 */
	public void setCodigoTipoIdentificacion(String string) {
		codigoTipoIdentificacion = string;
	}

	




	/**
	 * @return
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}


	
	/**
	 * @param string
	 */
	public void setCodigoViaIngreso(String string) {
		codigoViaIngreso = string;
	}

	


	/**
	 * @return
	 */
	public String getNombreTipoIdentificacion() {
		return nombreTipoIdentificacion;
	}

	/**
	 * @param string
	 */
	public void setNombreTipoIdentificacion(String string) {
		nombreTipoIdentificacion = string;
	}



	public boolean getIngresoDesdeReservaCita()
	{
		return isIngresoDesdeReservaCita();
	}

	public boolean isIngresoDesdeReservaCita()
	{
		return ib_ingresoDesdeReservaCita;
	}

	public void setIngresoDesdeReservaCita(boolean ab_ingresoDesdeReservaCita)
	{
		ib_ingresoDesdeReservaCita = ab_ingresoDesdeReservaCita;
	}
	
	
	
	/**
	 * @return Returns the ib_ingresoDesdeReservaCita.
	 */
	public boolean isIb_ingresoDesdeReservaCita() {
		return ib_ingresoDesdeReservaCita;
	}
	/**
	 * @param ib_ingresoDesdeReservaCita The ib_ingresoDesdeReservaCita to set.
	 */
	public void setIb_ingresoDesdeReservaCita(boolean ib_ingresoDesdeReservaCita) {
		this.ib_ingresoDesdeReservaCita = ib_ingresoDesdeReservaCita;
	}
	
	


	/**
	 * @return Returns the codigoConvenio.
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio The codigoConvenio to set.
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	

	

	


	/**
	 * @return the codigoContrato
	 */
	public String getCodigoContrato() {
		return codigoContrato;
	}



	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(String codigoContrato) {
		this.codigoContrato = codigoContrato;
	}



	/**
	 * @return Returns the direccion.
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion The direccion to set.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the estadoCivil.
	 */
	public String getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil The estadoCivil to set.
	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	


	/**
	 * @return Returns the ocupacion.
	 */
	public String getOcupacion() {
		return ocupacion;
	}

	/**
	 * @param ocupacion The ocupacion to set.
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * @return Returns the pacienteTriage.
	 */
	public String getPacienteTriage() {
		return pacienteTriage;
	}

	/**
	 * @param pacienteTriage The pacienteTriage to set.
	 */
	public void setPacienteTriage(String pacienteTriage) {
		this.pacienteTriage = pacienteTriage;
	}

	/**
	 * @return Returns the primerApellidoPersona.
	 */
	public String getPrimerApellidoPersona() {
		return primerApellidoPersona;
	}

	/**
	 * @param primerApellidoPersona The primerApellidoPersona to set.
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona) {
		this.primerApellidoPersona = primerApellidoPersona;
	}

	/**
	 * @return Returns the primerNombrePersona.
	 */
	public String getPrimerNombrePersona() {
		return primerNombrePersona;
	}

	/**
	 * @param primerNombrePersona The primerNombrePersona to set.
	 */
	public void setPrimerNombrePersona(String primerNombrePersona) {
		this.primerNombrePersona = primerNombrePersona;
	}

	/**
	 * @return Returns the segundoApellidoPersona.
	 */
	public String getSegundoApellidoPersona() {
		return segundoApellidoPersona;
	}

	/**
	 * @param segundoApellidoPersona The segundoApellidoPersona to set.
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona) {
		this.segundoApellidoPersona = segundoApellidoPersona;
	}

	/**
	 * @return Returns the segundoNombrePersona.
	 */
	public String getSegundoNombrePersona() {
		return segundoNombrePersona;
	}

	/**
	 * @param segundoNombrePersona The segundoNombrePersona to set.
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona) {
		this.segundoNombrePersona = segundoNombrePersona;
	}

	/**
	 * @return Returns the sexo.
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo The sexo to set.
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return Returns the tipoPersona.
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}

	/**
	 * @param tipoPersona The tipoPersona to set.
	 */
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	/**
	 * @return Returns the zonaDomicilio.
	 */
	public String getZonaDomicilio() {
		return zonaDomicilio;
	}

	/**
	 * @param zonaDomicilio The zonaDomicilio to set.
	 */
	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}



	/**
	 * @return Returns the foto.
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * @param foto The foto to set.
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * @return Returns the tipoSangre.
	 */
	public String getTipoSangre() {
		return tipoSangre;
	}

	/**
	 * @param tipoSangre The tipoSangre to set.
	 */
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	/**
	 * @return Returns the consecutivoFechaTriage.
	 */
	public String getConsecutivoFechaTriage() {
		return consecutivoFechaTriage;
	}

	/**
	 * @param consecutivoFechaTriage The consecutivoFechaTriage to set.
	 */
	public void setConsecutivoFechaTriage(String consecutivoFechaTriage) {
		this.consecutivoFechaTriage = consecutivoFechaTriage;
	}

	/**
	 * @return Returns the consecutivoTriage.
	 */
	public String getConsecutivoTriage() {
		return consecutivoTriage;
	}

	/**
	 * @param consecutivoTriage The consecutivoTriage to set.
	 */
	public void setConsecutivoTriage(String consecutivoTriage) {
		this.consecutivoTriage = consecutivoTriage;
	}

	/**
	 * @return Returns the esPacienteTriage.
	 */
	public boolean isEsPacienteTriage() {
		return esPacienteTriage;
	}

	/**
	 * @param esPacienteTriage The esPacienteTriage to set.
	 */
	public void setEsPacienteTriage(boolean esPacienteTriage) {
		this.esPacienteTriage = esPacienteTriage;
	}

	

	/**
	 * @return Returns the usuarioConvenio.
	 */
	public boolean isUsuarioConvenio() {
		return usuarioConvenio;
	}

	/**
	 * @param usuarioConvenio The usuarioConvenio to set.
	 */
	public void setUsuarioConvenio(boolean usuarioConvenio) {
		this.usuarioConvenio = usuarioConvenio;
	}

	

	/**
	 * @return Returns the codigoConvenioCapitacion.
	 */
	public String getCodigoConvenioCapitacion() {
		return codigoConvenioCapitacion;
	}

	/**
	 * @param codigoConvenioCapitacion The codigoConvenioCapitacion to set.
	 */
	public void setCodigoConvenioCapitacion(String codigoConvenioCapitacion) {
		this.codigoConvenioCapitacion = codigoConvenioCapitacion;
	}


	

	
	

	/**
	 * @return the estudio
	 */
	public String getEstudio() {
		return estudio;
	}

	/**
	 * @param estudio the estudio to set
	 */
	public void setEstudio(String estudio) {
		this.estudio = estudio;
	}

	/**
	 * @return the estudios
	 */
	public ArrayList<HashMap<String, Object>> getEstudios() {
		return estudios;
	}

	/**
	 * @param estudios the estudios to set
	 */
	public void setEstudios(ArrayList<HashMap<String, Object>> estudios) {
		this.estudios = estudios;
	}

	/**
	 * @return Returns the codigoCiudadId.
	 */
	public String getCodigoCiudadId() {
		return codigoCiudadId;
	}

	
	/**
	 * @return the mapaNumerosId
	 */
	public HashMap getMapaNumerosId() {
		return mapaNumerosId;
	}

	/**
	 * @param mapaNumerosId the mapaNumerosId to set
	 */
	public void setMapaNumerosId(HashMap mapaNumerosId) {
		this.mapaNumerosId = mapaNumerosId;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaNumerosId
	 */
	public Object getMapaNumerosId(String key) {
		return mapaNumerosId.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaNumerosId 
	 */
	public void setMapaNumerosId(String key,Object obj) {
		this.mapaNumerosId.put(key,obj);
	}

	
	/**
	 * @return the grupoPoblacional
	 */
	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}

	/**
	 * @param grupoPoblacional the grupoPoblacional to set
	 */
	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}

	

	/**
	 * @return the conveniosArp
	 */
	public HashMap getConveniosArp() {
		return conveniosArp;
	}

	/**
	 * @param conveniosArp the conveniosArp to set
	 */
	public void setConveniosArp(HashMap conveniosArp) {
		this.conveniosArp = conveniosArp;
	}
	
	/**
	 * @return retorna elemento mapa conveniosArp
	 */
	public Object getConveniosArp(String key) {
		return conveniosArp.get(key);
	}

	/**
	 * @param asigna elemento al mapa conveniosArp 
	 */
	public void setConveniosArp(String key,Object obj) {
		this.conveniosArp.put(key,obj);
	}

	/**
	 * @return the ingresoDesdeReferencia
	 */
	public boolean isIngresoDesdeReferencia() {
		return ingresoDesdeReferencia;
	}
	
	/**
	 * @return the ingresoDesdeReferencia
	 */
	public boolean getIngresoDesdeReferencia() {
		return isIngresoDesdeReferencia();
	}

	/**
	 * @param ingresoDesdeReferencia the ingresoDesdeReferencia to set
	 */
	public void setIngresoDesdeReferencia(boolean ingresoDesdeReferencia) {
		this.ingresoDesdeReferencia = ingresoDesdeReferencia;
	}

	

	/**
	 * @return the origenesAdmisiones
	 */
	public ArrayList<HashMap<String, Object>> getOrigenesAdmisiones() {
		return origenesAdmisiones;
	}

	/**
	 * @param origenesAdmisiones the origenesAdmisiones to set
	 */
	public void setOrigenesAdmisiones(
			ArrayList<HashMap<String, Object>> origenesAdmisiones) {
		this.origenesAdmisiones = origenesAdmisiones;
	}

	/**
	 * @return the deboAbrirReferencia
	 */
	public boolean isDeboAbrirReferencia() {
		return deboAbrirReferencia;
	}

	/**
	 * @param deboAbrirReferencia the deboAbrirReferencia to set
	 */
	public void setDeboAbrirReferencia(boolean deboAbrirReferencia) {
		this.deboAbrirReferencia = deboAbrirReferencia;
	}

	

	/**
	 * @return the anioHistoriaClincia
	 */
	public String getAnioHistoriaClincia() {
		return anioHistoriaClincia;
	}

	/**
	 * @param anioHistoriaClincia the anioHistoriaClincia to set
	 */
	public void setAnioHistoriaClincia(String anioHistoriaClincia) {
		this.anioHistoriaClincia = anioHistoriaClincia;
	}

	/**
	 * @return the numeroHistoriaClinica
	 */
	public String getNumeroHistoriaClinica() {
		return numeroHistoriaClinica;
	}

	/**
	 * @param numeroHistoriaClinica the numeroHistoriaClinica to set
	 */
	public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
		this.numeroHistoriaClinica = numeroHistoriaClinica;
	}

	/**
	 * @return the codigoPaisId
	 */
	public String getCodigoPaisId() {
		return codigoPaisId;
	}

	/**
	 * @param codigoPaisId the codigoPaisId to set
	 */
	public void setCodigoPaisId(String codigoPaisId) {
		this.codigoPaisId = codigoPaisId;
	}

	/**
	 * @return the paises
	 */
	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}

	/**
	 * @param paises the paises to set
	 */
	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}

	/**
	 * @return the ciudades
	 */
	public ArrayList<HashMap<String, Object>> getCiudades() {
		return ciudades;
	}

	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(ArrayList<HashMap<String, Object>> ciudades) {
		this.ciudades = ciudades;
	}

	/**
	 * @return the codigoCiudadNacimiento
	 */
	public String getCodigoCiudadNacimiento() {
		return codigoCiudadNacimiento;
	}

	/**
	 * @param codigoCiudadNacimiento the codigoCiudadNacimiento to set
	 */
	public void setCodigoCiudadNacimiento(String codigoCiudadNacimiento) {
		this.codigoCiudadNacimiento = codigoCiudadNacimiento;
	}

	/**
	 * @return the codigoPaisNacimiento
	 */
	public String getCodigoPaisNacimiento() {
		return codigoPaisNacimiento;
	}

	/**
	 * @param codigoPaisNacimiento the codigoPaisNacimiento to set
	 */
	public void setCodigoPaisNacimiento(String codigoPaisNacimiento) {
		this.codigoPaisNacimiento = codigoPaisNacimiento;
	}

	/**
	 * @return the ingresoEdad
	 */
	public boolean isIngresoEdad() {
		return ingresoEdad;
	}

	/**
	 * @param ingresoEdad the ingresoEdad to set
	 */
	public void setIngresoEdad(boolean ingresoEdad) {
		this.ingresoEdad = ingresoEdad;
	}

	/**
	 * @return the anios
	 */
	public String getAnios() {
		return anios;
	}

	/**
	 * @param anios the anios to set
	 */
	public void setAnios(String anios) {
		this.anios = anios;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the seleccionEdad
	 */
	public String getSeleccionEdad() {
		return seleccionEdad;
	}

	/**
	 * @param seleccionEdad the seleccionEdad to set
	 */
	public void setSeleccionEdad(String seleccionEdad) {
		this.seleccionEdad = seleccionEdad;
	}

	/**
	 * @return the codigoCiudadResidencia
	 */
	public String getCodigoCiudadResidencia() {
		return codigoCiudadResidencia;
	}

	/**
	 * @param codigoCiudadResidencia the codigoCiudadResidencia to set
	 */
	public void setCodigoCiudadResidencia(String codigoCiudadResidencia) {
		this.codigoCiudadResidencia = codigoCiudadResidencia;
	}

	/**
	 * @return the codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}

	/**
	 * @param codigoPaisResidencia the codigoPaisResidencia to set
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	/**
	 * @param codigoCiudadId the codigoCiudadId to set
	 */
	public void setCodigoCiudadId(String codigoCiudadId) {
		this.codigoCiudadId = codigoCiudadId;
	}

	/**
	 * @return the codigoBarrio
	 */
	public String getCodigoBarrio() {
		return codigoBarrio;
	}

	/**
	 * @param codigoBarrio the codigoBarrio to set
	 */
	public void setCodigoBarrio(String codigoBarrio) {
		this.codigoBarrio = codigoBarrio;
	}

	/**
	 * @return the nombreBarrio
	 */
	public String getNombreBarrio() {
		return nombreBarrio;
	}

	/**
	 * @param nombreBarrio the nombreBarrio to set
	 */
	public void setNombreBarrio(String nombreBarrio) {
		this.nombreBarrio = nombreBarrio;
	}

	/**
	 * @return the criterioBarrio
	 */
	public String getCriterioBarrio() {
		return criterioBarrio;
	}

	/**
	 * @param criterioBarrio the criterioBarrio to set
	 */
	public void setCriterioBarrio(String criterioBarrio) {
		this.criterioBarrio = criterioBarrio;
	}

	/**
	 * @return the codigoLocalidad
	 */
	public String getCodigoLocalidad() {
		return codigoLocalidad;
	}

	/**
	 * @param codigoLocalidad the codigoLocalidad to set
	 */
	public void setCodigoLocalidad(String codigoLocalidad) {
		this.codigoLocalidad = codigoLocalidad;
	}

	/**
	 * @return the localidades
	 */
	public ArrayList<HashMap<String, Object>> getLocalidades() {
		return localidades;
	}

	/**
	 * @param localidades the localidades to set
	 */
	public void setLocalidades(ArrayList<HashMap<String, Object>> localidades) {
		this.localidades = localidades;
	}

	/**
	 * @return the ocupaciones
	 */
	public HashMap getOcupaciones() {
		return ocupaciones;
	}

	/**
	 * @param ocupaciones the ocupaciones to set
	 */
	public void setOcupaciones(HashMap ocupaciones) {
		this.ocupaciones = ocupaciones;
	}

	/**
	 * @return the zonasDomicilio
	 */
	public HashMap getZonasDomicilio() {
		return zonasDomicilio;
	}

	/**
	 * @param zonasDomicilio the zonasDomicilio to set
	 */
	public void setZonasDomicilio(HashMap zonasDomicilio) {
		this.zonasDomicilio = zonasDomicilio;
	}

	/**
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}

	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}

	/**
	 * @return the tiposSangre
	 */
	public HashMap getTiposSangre() {
		return tiposSangre;
	}

	/**
	 * @param tiposSangre the tiposSangre to set
	 */
	public void setTiposSangre(HashMap tiposSangre) {
		this.tiposSangre = tiposSangre;
	}

	/**
	 * @return the estadosCiviles
	 */
	public HashMap getEstadosCiviles() {
		return estadosCiviles;
	}

	/**
	 * @param estadosCiviles the estadosCiviles to set
	 */
	public void setEstadosCiviles(HashMap estadosCiviles) {
		this.estadosCiviles = estadosCiviles;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the etnia
	 */
	public String getEtnia() {
		return etnia;
	}

	/**
	 * @param etnia the etnia to set
	 */
	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}

	/**
	 * @return the religion
	 */
	public String getReligion() {
		return religion;
	}

	/**
	 * @param religion the religion to set
	 */
	public void setReligion(String religion) {
		this.religion = religion;
	}

	/**
	 * @return the centrosAtencion
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the etnias
	 */
	public ArrayList<HashMap<String, Object>> getEtnias() {
		return etnias;
	}

	/**
	 * @param etnias the etnias to set
	 */
	public void setEtnias(ArrayList<HashMap<String, Object>> etnias) {
		this.etnias = etnias;
	}

	/**
	 * @return the religiones
	 */
	public ArrayList<HashMap<String, Object>> getReligiones() {
		return religiones;
	}

	/**
	 * @param religiones the religiones to set
	 */
	public void setReligiones(ArrayList<HashMap<String, Object>> religiones) {
		this.religiones = religiones;
	}

	/**
	 * @return the leeEscribe
	 */
	public String getLeeEscribe() {
		return leeEscribe;
	}

	/**
	 * @param leeEscribe the leeEscribe to set
	 */
	public void setLeeEscribe(String leeEscribe) {
		this.leeEscribe = leeEscribe;
	}

	/**
	 * @return the esPacienteCapitado
	 */
	public boolean isEsPacienteCapitado() {
		return esPacienteCapitado;
	}

	/**
	 * @param esPacienteCapitado the esPacienteCapitado to set
	 */
	public void setEsPacienteCapitado(boolean esPacienteCapitado) {
		this.esPacienteCapitado = esPacienteCapitado;
	}

	/**
	 * @return the existePaciente
	 */
	public boolean isExistePaciente() {
		return existePaciente;
	}

	/**
	 * @param existePaciente the existePaciente to set
	 */
	public void setExistePaciente(boolean existePaciente) {
		this.existePaciente = existePaciente;
	}

	/**
	 * @return the identificacionAutomatica
	 */
	public boolean isIdentificacionAutomatica() {
		return identificacionAutomatica;
	}

	/**
	 * @param identificacionAutomatica the identificacionAutomatica to set
	 */
	public void setIdentificacionAutomatica(boolean identificacionAutomatica) {
		this.identificacionAutomatica = identificacionAutomatica;
	}

	/**
	 * @return the avisoValidacionNumerosId
	 */
	public boolean isAvisoValidacionNumerosId() {
		return avisoValidacionNumerosId;
	}

	/**
	 * @param avisoValidacionNumerosId the avisoValidacionNumerosId to set
	 */
	public void setAvisoValidacionNumerosId(boolean avisoValidacionNumerosId) {
		this.avisoValidacionNumerosId = avisoValidacionNumerosId;
	}

	/**
	 * @return the pacientesMismosNombres
	 */
	public ArrayList<HashMap<String, Object>> getPacientesMismosNombres() {
		return pacientesMismosNombres;
	}

	/**
	 * @param pacientesMismosNombres the pacientesMismosNombres to set
	 */
	public void setPacientesMismosNombres(
			ArrayList<HashMap<String, Object>> pacientesMismosNombres) {
		this.pacientesMismosNombres = pacientesMismosNombres;
	}

	/**
	 * @return the avisoValidacionMismosNombres
	 */
	public boolean isAvisoValidacionMismosNombres() {
		return avisoValidacionMismosNombres;
	}

	/**
	 * @param avisoValidacionMismosNombres the avisoValidacionMismosNombres to set
	 */
	public void setAvisoValidacionMismosNombres(boolean avisoValidacionMismosNombres) {
		this.avisoValidacionMismosNombres = avisoValidacionMismosNombres;
	}

	


	/**
	 * @return the codigoUsuarioCapitado
	 */
	public String getCodigoUsuarioCapitado() {
		return codigoUsuarioCapitado;
	}

	/**
	 * @param codigoUsuarioCapitado the codigoUsuarioCapitado to set
	 */
	public void setCodigoUsuarioCapitado(String codigoUsuarioCapitado) {
		this.codigoUsuarioCapitado = codigoUsuarioCapitado;
	}


	

	


	/**
	 * @return the loginUsuarioPaciente
	 */
	public String getLoginUsuarioPaciente() {
		return loginUsuarioPaciente;
	}

	/**
	 * @param loginUsuarioPaciente the loginUsuarioPaciente to set
	 */
	public void setLoginUsuarioPaciente(String loginUsuarioPaciente) {
		this.loginUsuarioPaciente = loginUsuarioPaciente;
	}

	/**
	 * @return the usuarioPaciente
	 */
	public boolean isUsuarioPaciente() {
		return usuarioPaciente;
	}

	/**
	 * @param usuarioPaciente the usuarioPaciente to set
	 */
	public void setUsuarioPaciente(boolean usuarioPaciente) {
		this.usuarioPaciente = usuarioPaciente;
	}

	/**
	 * @return the cuenta
	 */
	public HashMap<String, Object> getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(HashMap<String, Object> cuenta) {
		this.cuenta = cuenta;
	}
	
	/**
	 * @return retorna elemento del mapa cuenta
	 */
	public Object getCuenta(String key) {
		return cuenta.get(key);
	}

	/**
	 * @param Asigna elemento al mapa cuenta 
	 */
	public void setCuenta(String key,Object obj) {
		this.cuenta.put(key,obj);
	}

	/**
	 * @return the viasIngreso
	 */
	public Vector<InfoDatosString> getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(Vector<InfoDatosString> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}
	
	/**
	 * Retorna el número de vias de ingreso del arreglo de vias de ingreso
	 * @return
	 */
	public int getNumViasIngreso()
	{
		return this.viasIngreso.size();
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
	 * @return the contratos
	 */
	public ArrayList<HashMap<String, Object>> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<HashMap<String, Object>> contratos) {
		this.contratos = contratos;
	}
	
	/**
	 * Retorna el número de contratos del arreglo de contratos
	 * @return
	 */
	public int getNumContratos()
	{
		return this.contratos.size();
	}

	/**
	 * @return the tiposComplejidad
	 */
	public Vector<InfoDatosString> getTiposComplejidad() {
		return tiposComplejidad;
	}

	/**
	 * @param tiposComplejidad the tiposComplejidad to set
	 */
	public void setTiposComplejidad(Vector<InfoDatosString> tiposComplejidad) {
		this.tiposComplejidad = tiposComplejidad;
	}

	/**
	 * @return the estratosSociales
	 */
	public HashMap getEstratosSociales() {
		return estratosSociales;
	}

	/**
	 * @param estratosSociales the estratosSociales to set
	 */
	public void setEstratosSociales(HashMap estratosSociales) {
		this.estratosSociales = estratosSociales;
	}
	
	/**
	 * @return retorna elemento del mapa estratosSociales
	 */
	public Object getEstratosSociales(String key) {
		return estratosSociales.get(key);
	}

	/**
	 * @param Asigna elemento al mapa estratosSociales 
	 */
	public void setEstratosSociales(String key,Object obj) {
		this.estratosSociales.put(key, obj);
	}

	/**
	 * @return the montosCobro
	 */
	public ArrayList<HashMap<String, Object>> getMontosCobro() {
		return montosCobro;
	}

	/**
	 * @param montosCobro the montosCobro to set
	 */
	public void setMontosCobro(ArrayList<HashMap<String, Object>> montosCobro) {
		this.montosCobro = montosCobro;
	}

	/**
	 * @return the tiposPaciente
	 */
	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	/**
	 * @return the areas
	 */
	public HashMap getAreas() {
		return areas;
	}

	/**
	 * @param areas the areas to set
	 */
	public void setAreas(HashMap areas) {
		this.areas = areas;
	}
	
	/**
	 * @return retorna elemento del mapa areas
	 */
	public Object getAreas(String key) {
		return areas.get(key);
	}

	/**
	 * @param Asigna elemento al mapa areas 
	 */
	public void setAreas(String key,Object obj) {
		this.areas.put(key,obj);
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
	 * @return the tiposIdentificacion
	 */
	public ArrayList<HashMap<String, Object>> getTiposIdentificacion() {
		return tiposIdentificacion;
	}

	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacion(
			ArrayList<HashMap<String, Object>> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

	/**
	 * @return the variosConvenios
	 */
	public HashMap<String, Object> getVariosConvenios() {
		return variosConvenios;
	}

	/**
	 * @param variosConvenios the variosConvenios to set
	 */
	public void setVariosConvenios(HashMap<String, Object> variosConvenios) {
		this.variosConvenios = variosConvenios;
	}
	
	/**
	 * @return retorna elemento del mapa variosConvenios
	 */
	public Object getVariosConvenios(String key) {
		return variosConvenios.get(key);
	}
	
	/**
	 * Método para obtener el número de convenios definitivos de la sección otros convenios
	 * @return
	 */
	public int getNumConveniosDefinitivos()
	{
		int contador = 0;
		
		for(int i=0;i<Utilidades.convertirAEntero(this.getVariosConvenios("numRegistros")+"", true);i++)
			if(this.getVariosConvenios("codigoConvenio_"+i)!=null)
				contador ++;
		
		return contador;
	}

	/**
	 * @param Asigna elemento al mapa variosConvenios 
	 */
	public void setVariosConvenios(String key,Object obj) {
		this.variosConvenios.put(key,obj);
	}

	/**
	 * @return the posConvenio
	 */
	public int getPosConvenio() {
		return posConvenio;
	}

	/**
	 * @param posConvenio the posConvenio to set
	 */
	public void setPosConvenio(int posConvenio) {
		this.posConvenio = posConvenio;
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
	 * Retorna el número de contratos del arreglo de contratos
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
	 * @return the nuevoConvenio
	 */
	public boolean isNuevoConvenio() {
		return nuevoConvenio;
	}

	/**
	 * @param nuevoConvenio the nuevoConvenio to set
	 */
	public void setNuevoConvenio(boolean nuevoConvenio) {
		this.nuevoConvenio = nuevoConvenio;
	}

	/**
	 * @return the numConvenios
	 */
	public int getNumConvenios() {
		return numConvenios;
	}

	/**
	 * @param numConvenios the numConvenios to set
	 */
	public void setNumConvenios(int numConvenios) {
		this.numConvenios = numConvenios;
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
	 * @return the deboAbrirAccidentesTransito
	 */
	public boolean isDeboAbrirAccidentesTransito() {
		return deboAbrirAccidentesTransito;
	}

	/**
	 * @param deboAbrirAccidentesTransito the deboAbrirAccidentesTransito to set
	 */
	public void setDeboAbrirAccidentesTransito(boolean deboAbrirAccidentesTransito) {
		this.deboAbrirAccidentesTransito = deboAbrirAccidentesTransito;
	}

	/**
	 * @return the deboAbrirEventoCatastrofico
	 */
	public boolean isDeboAbrirEventoCatastrofico() {
		return deboAbrirEventoCatastrofico;
	}

	/**
	 * @param deboAbrirEventoCatastrofico the deboAbrirEventoCatastrofico to set
	 */
	public void setDeboAbrirEventoCatastrofico(boolean deboAbrirEventoCatastrofico) {
		this.deboAbrirEventoCatastrofico = deboAbrirEventoCatastrofico;
	}

	/**
	 * @return the deboAbrirAsignacionCitas
	 */
	public boolean isDeboAbrirAsignacionCitas() {
		return deboAbrirAsignacionCitas;
	}

	/**
	 * @param deboAbrirAsignacionCitas the deboAbrirAsignacionCitas to set
	 */
	public void setDeboAbrirAsignacionCitas(boolean deboAbrirAsignacionCitas) {
		this.deboAbrirAsignacionCitas = deboAbrirAsignacionCitas;
	}

	/**
	 * @return the pathReferencia
	 */
	public String getPathReferencia() {
		return pathReferencia;
	}

	/**
	 * @param pathReferencia the pathReferencia to set
	 */
	public void setPathReferencia(String pathReferencia) {
		this.pathReferencia = pathReferencia;
	}

	/**
	 * @return the pathAsignacionCitas
	 */
	public String getPathAsignacionCitas() {
		return pathAsignacionCitas;
	}

	/**
	 * @param pathAsignacionCitas the pathAsignacionCitas to set
	 */
	public void setPathAsignacionCitas(String pathAsignacionCitas) {
		this.pathAsignacionCitas = pathAsignacionCitas;
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
	 * @return the verificacion
	 */
	public Object getVerificacion(String key) {
		return verificacion.get(key);
	}

	/**
	 * @param verificacion the verificacion to set
	 */
	public void setVerificacion(String key,Object obj) {
		this.verificacion.put(key,obj);
	}

	/**
	 * @return the responsable
	 */
	public HashMap<String, Object> getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(HashMap<String, Object> responsable) {
		this.responsable = responsable;
	}
	
	/**
	 * @return the responsable
	 */
	public Object getResponsable(String key) {
		return responsable.get(key);
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String key,Object obj) {
		this.responsable.put(key,obj);
	}



	/**
	 * @return the tiposIdResponsable
	 */
	public ArrayList<HashMap<String, Object>> getTiposIdResponsable() {
		return tiposIdResponsable;
	}



	/**
	 * @param tiposIdResponsable the tiposIdResponsable to set
	 */
	public void setTiposIdResponsable(
			ArrayList<HashMap<String, Object>> tiposIdResponsable) {
		this.tiposIdResponsable = tiposIdResponsable;
	}



	/**
	 * @return the ciudadesExp
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesExp() {
		return ciudadesExp;
	}



	/**
	 * @param ciudadesExp the ciudadesExp to set
	 */
	public void setCiudadesExp(ArrayList<HashMap<String, Object>> ciudadesExp) {
		this.ciudadesExp = ciudadesExp;
	}



	/**
	 * @return the ciudadesNac
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesNac() {
		return ciudadesNac;
	}



	/**
	 * @param ciudadesNac the ciudadesNac to set
	 */
	public void setCiudadesNac(ArrayList<HashMap<String, Object>> ciudadesNac) {
		this.ciudadesNac = ciudadesNac;
	}



	/**
	 * @return the codigoTipoIdResponsable
	 */
	public String getCodigoTipoIdResponsable() {
		return codigoTipoIdResponsable;
	}



	/**
	 * @param codigoTipoIdResponsable the codigoTipoIdResponsable to set
	 */
	public void setCodigoTipoIdResponsable(String codigoTipoIdResponsable) {
		this.codigoTipoIdResponsable = codigoTipoIdResponsable;
	}



	/**
	 * @return the numeroIdResponsable
	 */
	public String getNumeroIdResponsable() {
		return numeroIdResponsable;
	}



	/**
	 * @param numeroIdResponsable the numeroIdResponsable to set
	 */
	public void setNumeroIdResponsable(String numeroIdResponsable) {
		this.numeroIdResponsable = numeroIdResponsable;
	}



	/**
	 * @return the hospitalizacion
	 */
	public HashMap<String, Object> getHospitalizacion() {
		return hospitalizacion;
	}



	/**
	 * @param hospitalizacion the hospitalizacion to set
	 */
	public void setHospitalizacion(HashMap<String, Object> hospitalizacion) {
		this.hospitalizacion = hospitalizacion;
	}
	
	/**
	 * @return the hospitalizacion
	 */
	public Object getHospitalizacion(String key) {
		return hospitalizacion.get(key);
	}



	/**
	 * @param hospitalizacion the hospitalizacion to set
	 */
	public void setHospitalizacion(String key,Object obj) {
		this.hospitalizacion.put(key,obj);
	}



	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}



	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}



	/**
	 * @return the camas
	 */
	public HashMap getCamas() {
		return camas;
	}



	/**
	 * @param camas the camas to set
	 */
	public void setCamas(HashMap camas) {
		this.camas = camas;
	}
	
	/**
	 * @return the camas
	 */
	public Object getCamas(String key) {
		return camas.get(key);
	}



	/**
	 * @param camas the camas to set
	 */
	public void setCamas(String key, Object obj) {
		this.camas.put(key, obj);
	}



	/**
	 * @return the requeridoDocumentoGarantias
	 */
	public boolean isRequeridoDocumentoGarantias() {
		return requeridoDocumentoGarantias;
	}



	/**
	 * @param requeridoDocumentoGarantias the requeridoDocumentoGarantias to set
	 */
	public void setRequeridoDocumentoGarantias(boolean requeridoDocumentoGarantias) {
		this.requeridoDocumentoGarantias = requeridoDocumentoGarantias;
	}



	/**
	 * @return the requeridoDeudor
	 */
	public boolean isRequeridoDeudor() {
		return requeridoDeudor;
	}



	/**
	 * @param requeridoDeudor the requeridoDeudor to set
	 */
	public void setRequeridoDeudor(boolean requeridoDeudor) {
		this.requeridoDeudor = requeridoDeudor;
	}



	/**
	 * @return the alertasSemanasCotizacion
	 */
	public ArrayList<String> getAlertasSemanasCotizacion() {
		return alertasSemanasCotizacion;
	}



	/**
	 * @param alertasSemanasCotizacion the alertasSemanasCotizacion to set
	 */
	public void setAlertaSemanaCotizacion(String convenio) 
	{
		this.alertasSemanasCotizacion.add(convenio);
	}



	/**
	 * @return the ingresoIncompleto
	 */
	public HashMap<String, Object> getIngresoIncompleto() {
		return ingresoIncompleto;
	}



	/**
	 * @param ingresoIncompleto the ingresoIncompleto to set
	 */
	public void setIngresoIncompleto(HashMap<String, Object> ingresoIncompleto) {
		this.ingresoIncompleto = ingresoIncompleto;
	}
	
	/**
	 * @return the ingresoIncompleto
	 */
	public Object getIngresoIncompleto(String key) {
		return ingresoIncompleto.get(key);
	}



	/**
	 * @param ingresoIncompleto the ingresoIncompleto to set
	 */
	public void setIngresoIncompleto(String key,Object obj) {
		this.ingresoIncompleto.put(key,obj);
	}



	/**
	 * @return the conveniosPaciente
	 */
	public HashMap<String, Object> getConveniosPaciente() {
		return conveniosPaciente;
	}



	/**
	 * @param conveniosPaciente the conveniosPaciente to set
	 */
	public void setConveniosPaciente(HashMap<String, Object> conveniosPaciente) {
		this.conveniosPaciente = conveniosPaciente;
	}
	
	/**
	 * @return the conveniosPaciente
	 */
	public Object getConveniosPaciente(String key) {
		return conveniosPaciente.get(key);
	}



	/**
	 * @param conveniosPaciente the conveniosPaciente to set
	 */
	public void setConveniosPaciente(String key,Object obj) {
		this.conveniosPaciente.put(key,obj);
	}



	/**
	 * @return the conveniosPostulados
	 */
	public HashMap<String, Object> getConveniosPostulados() {
		return conveniosPostulados;
	}



	/**
	 * @param conveniosPostulados the conveniosPostulados to set
	 */
	public void setConveniosPostulados(HashMap<String, Object> conveniosPostulados) {
		this.conveniosPostulados = conveniosPostulados;
	}
	
	/**
	 * @return the conveniosPostulados
	 */
	public Object getConveniosPostulados(String key) {
		return conveniosPostulados.get(key);
	}



	/**
	 * @param conveniosPostulados the conveniosPostulados to set
	 */
	public void setConveniosPostulados(String key,Object obj) {
		this.conveniosPostulados.put(key,obj);
	}








	/**
	 * @return the posSeleccion
	 */
	public int getPosSeleccion() {
		return posSeleccion;
	}



	/**
	 * @param posSeleccion the posSeleccion to set
	 */
	public void setPosSeleccion(int posSeleccion) {
		this.posSeleccion = posSeleccion;
	}



	/**
	 * @return the huboSeleccionConvenio
	 */
	public boolean isHuboSeleccionConvenio() {
		return huboSeleccionConvenio;
	}



	/**
	 * @param huboSeleccionConvenio the huboSeleccionConvenio to set
	 */
	public void setHuboSeleccionConvenio(boolean huboSeleccionConvenio) {
		this.huboSeleccionConvenio = huboSeleccionConvenio;
	}



	/**
	 * @return the saldoInterfaz
	 */
	public String getSaldoInterfaz() {
		return saldoInterfaz;
	}



	/**
	 * @param saldoInterfaz the saldoInterfaz to set
	 */
	public void setSaldoInterfaz(String saldoInterfaz) {
		this.saldoInterfaz = saldoInterfaz;
	}



	/**
	 * @return the posPresupuesto
	 */
	public int getPosPresupuesto() {
		return posPresupuesto;
	}



	/**
	 * @param posPresupuesto the posPresupuesto to set
	 */
	public void setPosPresupuesto(int posPresupuesto) {
		this.posPresupuesto = posPresupuesto;
	}



	/**
	 * @return the presupuestoPaciente
	 */
	public boolean isPresupuestoPaciente() {
		return presupuestoPaciente;
	}



	/**
	 * @param presupuestoPaciente the presupuestoPaciente to set
	 */
	public void setPresupuestoPaciente(boolean presupuestoPaciente) {
		this.presupuestoPaciente = presupuestoPaciente;
	}



	/**
	 * @return the presupuestos
	 */
	public HashMap<String, Object> getPresupuestos() {
		return presupuestos;
	}



	/**
	 * @param presupuestos the presupuestos to set
	 */
	public void setPresupuestos(HashMap<String, Object> presupuestos) {
		this.presupuestos = presupuestos;
	}
	
	/**
	 * @return the presupuestos
	 */
	public Object getPresupuestos(String key) {
		return presupuestos.get(key);
	}



	/**
	 * @param presupuestos the presupuestos to set
	 */
	public void setPresupuestos(String key,Object obj) {
		this.presupuestos.put(key,obj);
	}



	/**
	 * @return the fechaNacimientoResponsable
	 */
	public String getFechaNacimientoResponsable() {
		return fechaNacimientoResponsable;
	}



	/**
	 * @param fechaNacimientoResponsable the fechaNacimientoResponsable to set
	 */
	public void setFechaNacimientoResponsable(String fechaNacimientoResponsable) {
		this.fechaNacimientoResponsable = fechaNacimientoResponsable;
	}



	/**
	 * @return the nombreLocalidad
	 */
	public String getNombreLocalidad() {
		return nombreLocalidad;
	}



	/**
	 * @param nombreLocalidad the nombreLocalidad to set
	 */
	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}



	/**
	 * @return the deboImprimirAdmision
	 */
	public boolean isDeboImprimirAdmision() {
		return deboImprimirAdmision;
	}



	/**
	 * @param deboImprimirAdmision the deboImprimirAdmision to set
	 */
	public void setDeboImprimirAdmision(boolean deboImprimirAdmision) {
		this.deboImprimirAdmision = deboImprimirAdmision;
	}



	/**
	 * @return the fueModificadoResponsable
	 */
	public boolean isFueModificadoResponsable() {
		return fueModificadoResponsable;
	}



	/**
	 * @param fueModificadoResponsable the fueModificadoResponsable to set
	 */
	public void setFueModificadoResponsable(boolean fueModificadoResponsable) {
		this.fueModificadoResponsable = fueModificadoResponsable;
	}



	/**
	 * @return the fichaUsuarioCapitado
	 */
	public String getFichaUsuarioCapitado() {
		return fichaUsuarioCapitado;
	}



	/**
	 * @param fichaUsuarioCapitado the fichaUsuarioCapitado to set
	 */
	public void setFichaUsuarioCapitado(String fichaUsuarioCapitado) {
		this.fichaUsuarioCapitado = fichaUsuarioCapitado;
	}



	/**
	 * @return the usuarioSinCapitacionVigente
	 */
	public boolean isUsuarioSinCapitacionVigente() {
		return usuarioSinCapitacionVigente;
	}



	/**
	 * @param usuarioSinCapitacionVigente the usuarioSinCapitacionVigente to set
	 */
	public void setUsuarioSinCapitacionVigente(boolean usuarioSinCapitacionVigente) {
		this.usuarioSinCapitacionVigente = usuarioSinCapitacionVigente;
	}



	/**
	 * @return the datosCapitacion
	 */
	public HashMap<String, Object> getDatosCapitacion() {
		return datosCapitacion;
	}



	/**
	 * @param datosCapitacion the datosCapitacion to set
	 */
	public void setDatosCapitacion(HashMap<String, Object> datosCapitacion) {
		this.datosCapitacion = datosCapitacion;
	}
	
	/**
	 * @return the datosCapitacion
	 */
	public Object getDatosCapitacion(String key) {
		return datosCapitacion.get(key);
	}



	/**
	 * @param datosCapitacion the datosCapitacion to set
	 */
	public void setDatosCapitacion(String key,Object obj) {
		this.datosCapitacion.put(key,obj);
	}



	/**
	 * @return the codigoTipoPaciente
	 */
	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}



	/**
	 * @param codigoTipoPaciente the codigoTipoPaciente to set
	 */
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}



	/**
	 * @return the tipoAsocio
	 */
	public ArrayList getTipoAsocio() {
		return tipoAsocio;
	}



	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(ArrayList tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}



	public Boolean getControlMensaje() {
		return controlMensaje;
	}



	public void setControlMensaje(Boolean controlMensaje) {
		this.controlMensaje = controlMensaje;
	}


	/**
	 * @return the preingreso
	 */
	public String getPreingreso() {
		return preingreso;
	}

	/**
	 * @param preingreso the preingreso to set
	 */
	public void setPreingreso(String preingreso) {
		this.preingreso = preingreso;
	}



	/**
	 * @return the codIngresoPreingreso
	 */
	public int getCodIngresoPreingreso() {
		return codIngresoPreingreso;
	}



	/**
	 * @param codIngresoPreingreso the codIngresoPreingreso to set
	 */
	public void setCodIngresoPreingreso(int codIngresoPreingreso) {
		this.codIngresoPreingreso = codIngresoPreingreso;
	}



	/**
	 * @return the ingresosCerradosPendientesXFacturar
	 */
	public String getIngresosCerradosPendientesXFacturar() {
		return ingresosCerradosPendientesXFacturar;
	}



	/**
	 * @param ingresosCerradosPendientesXFacturar the ingresosCerradosPendientesXFacturar to set
	 */
	public void setIngresosCerradosPendientesXFacturar(
			String ingresosCerradosPendientesXFacturar) {
		this.ingresosCerradosPendientesXFacturar = ingresosCerradosPendientesXFacturar;
	}



	public int getEvolucion() {
		return evolucion;
	}



	public void setEvolucion(int evolucion) {
		this.evolucion = evolucion;
	}



	public int getValoracion() {
		return valoracion;
	}



	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}



	public String getAreaSel() {
		return areaSel;
	}



	public void setAreaSel(String areaSel) {
		this.areaSel = areaSel;
	}



	public HashMap getTiposMonitoreoMap() {
		return tiposMonitoreoMap;
	}



	public void setTiposMonitoreoMap(HashMap tiposMonitoreoMap) {
		this.tiposMonitoreoMap = tiposMonitoreoMap;
	}
	
	/**
	 * @return the tiposMonitoreoMap
	 */
	public Object getTiposMonitoreoMap(String key) {
		return tiposMonitoreoMap.get(key);
	}

	/**
	 * @param tiposMonitoreoMap the datosCapitacion to set
	 */
	public void setTiposMonitoreoMap(String key,Object obj) {
		this.tiposMonitoreoMap.put(key,obj);
	}

	/**
	 * @return the autorizacionIngEvento
	 */
	public InfoDatosString getAutorizacionIngEvento() {
		if(autorizacionIngEvento == null)
			return new InfoDatosString("","");
		else
			return autorizacionIngEvento;
	}

	/**
	 * @param autorizacionIngEvento the autorizacionIngEvento to set
	 */
	public void setAutorizacionIngEvento(InfoDatosString autorizacionIngEvento) {
		this.autorizacionIngEvento = autorizacionIngEvento;
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
	 * Método para saber el número de mensaje de alerta
	 * @return
	 */
	public int getNumMensajesAlerta()
	{
		return this.mensajesAlerta.size();
	}
	
	/**
	 * Método para obtener el numero de farmacias del arreglo de farmacias
	 * @return
	 */
	public int getNumFarmacias()
	{
		return this.farmacias.size();
	}



	/**
	 * @return the telefonoCelular
	 */
	public String getTelefonoCelular() {
		return telefonoCelular;
	}



	/**
	 * @param telefonoCelular the telefonoCelular to set
	 */
	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}



	/**
	 * @return the coberturasSalud
	 */
	public ArrayList<HashMap<String, Object>> getCoberturasSalud() {
		return coberturasSalud;
	}



	/**
	 * @param coberturasSalud the coberturasSalud to set
	 */
	public void setCoberturasSalud(
			ArrayList<HashMap<String, Object>> coberturasSalud) {
		this.coberturasSalud = coberturasSalud;
	}
	
	/**
	 * Número de coberturas de salud
	 * @return
	 */
	public int getNumCoberturasSalud()
	{
		return this.coberturasSalud.size();
	}


   /**
    * Se puede realizar Registro envio informe inconsistencias
    * @return
    */
	public boolean isRegistroInconsistencias() {
		return registroInconsistencias;
	}


    /**
     *  Asigna valor a Registro envio inconsistencias
     * @param registroInconsistencias
     */
	public void setRegistroInconsistencias(boolean registroInconsistencias) {
		this.registroInconsistencias = registroInconsistencias;
	}



	/**
	 * @return the mensajesConvenioAdicional
	 */
	public ArrayList<ElementoApResource> getMensajesConvenioAdicional() {
		return mensajesConvenioAdicional;
	}



	/**
	 * @param mensajesConvenioAdicional the mensajesConvenioAdicional to set
	 */
	public void setMensajesConvenioAdicional(
			ArrayList<ElementoApResource> mensajesConvenioAdicional) {
		this.mensajesConvenioAdicional = mensajesConvenioAdicional;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumMensajesConvenioAdicional()
	{
		return this.mensajesConvenioAdicional.size();
	}



	/**
	 * @return the telefonoFijo
	 */
	public String getTelefonoFijo() {
		return telefonoFijo;
	}



	/**
	 * @param telefonoFijo the telefonoFijo to set
	 */
	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}



	/**
	 * @return the mensajeSaldoPendiente
	 */
	public String getMensajeSaldoPendiente() {
		return mensajeSaldoPendiente;
	}



	/**
	 * @param mensajeSaldoPendiente the mensajeSaldoPendiente to set
	 */
	public void setMensajeSaldoPendiente(String mensajeSaldoPendiente) {
		this.mensajeSaldoPendiente = mensajeSaldoPendiente;
	}



	public HashMap getTiposAfiliado() {
		return tiposAfiliado;
	}



	public void setTiposAfiliado(HashMap tiposAfiliado) {
		this.tiposAfiliado = tiposAfiliado;
	}
	
	/**
	 * @return retorna elemento del mapa tiposAfiliado
	 */
	public Object getTiposAfiliado(String key) {
		return tiposAfiliado.get(key);
	}

	/**
	 * @param Asigna elemento al mapa tiposAfiliado 
	 */
	public void setTiposAfiliado(String key,Object obj) {
		this.tiposAfiliado.put(key, obj);
	}



	public HashMap getTiposAfiliadoConvenio() {
		return tiposAfiliadoConvenio;
	}



	public void setTiposAfiliadoConvenio(HashMap tiposAfiliadoConvenio) {
		this.tiposAfiliadoConvenio = tiposAfiliadoConvenio;
	}



	public ArrayList<DtoClasificacionesTriage> getClasificacionesTriage() {
		return clasificacionesTriage;
	}



	public void setClasificacionesTriage(
			ArrayList<DtoClasificacionesTriage> clasificacionesTriage) {
		this.clasificacionesTriage = clasificacionesTriage;
	}



	public int getClasificacionTriage() {
		return clasificacionTriage;
	}



	public void setClasificacionTriage(int clasificacionTriage) {
		this.clasificacionTriage = clasificacionTriage;
	}


	public String getMensajePacientesIgualNNombre() {
		return mensajePacientesIgualNNombre;
	}


	public void setMensajePacientesIgualNNombre(String mensajePacientesIgualNNombre) {
		this.mensajePacientesIgualNNombre = mensajePacientesIgualNNombre;
	}


	public boolean isPuedoGrabarConvenioAdicional() {
		return puedoGrabarConvenioAdicional;
	}


	public void setPuedoGrabarConvenioAdicional(boolean puedoGrabarConvenioAdicional) {
		this.puedoGrabarConvenioAdicional = puedoGrabarConvenioAdicional;
	}


	/**
	 * @param codigoEstratoSocial the codigoEstratoSocial to set
	 */
	public void setCodigoEstratoSocial(String codigoEstratoSocial) {
		this.codigoEstratoSocial = codigoEstratoSocial;
	}


	/**
	 * @return the codigoEstratoSocial
	 */
	public String getCodigoEstratoSocial() {
		return codigoEstratoSocial;
	}


	/**
	 * @param codigoTipoAfiliado the codigoTipoAfiliado to set
	 */
	public void setCodigoTipoAfiliado(String codigoTipoAfiliado) {
		this.codigoTipoAfiliado = codigoTipoAfiliado;
	}


	/**
	 * @return the codigoTipoAfiliado
	 */
	public String getCodigoTipoAfiliado() {
		return codigoTipoAfiliado;
	}
	
}