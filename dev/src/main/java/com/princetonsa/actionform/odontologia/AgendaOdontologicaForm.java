package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoBusquedaAgendaRango;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.servinte.axioma.orm.Contratos;


@SuppressWarnings({"unchecked","deprecation"})
public class AgendaOdontologicaForm extends ValidatorForm {
	
	/**
	 * Verisón serial
	 */
	private static final long serialVersionUID = 1L;
	transient Logger logger = Logger.getLogger(AgendaOdontologicaForm.class);
	private String estado;
	
	private String tipoFlujoPaciente;
	
	// Atributos de visualizacion
	private int codigoActividadAutorizacion;

	private ArrayList<HashMap> listMotivoCancelacion;
	
	// Atributos seccion I
	private String paginaLinkSiguienteBen;
	private int posicionBeneficiario;
	private String paginaLinkSiguienteCita;
	
	// Atributos seccion II
	private String fechaInicial;
	private String fechaFinal;
	
	// Atributos Seccion III
	private String fechaDesplazamiento;
	private String sentidoDesplazamiento;
	private int desplazamientoFecha;
	
	//parametros generales
	private String numDiasAntFechaActual;
	private String multiploMinGenCitas;
	private String validaPresupuestoContratado;
	private String utilProgOdonPlanTra;
	private String minutosEsperaAsgCitOdoCad;
	
	// Atributos generales
	private ArrayList<DtoAgendaOdontologica> agendaOdon;
	private ArrayList<InfoDatosString> convencionColores;
	private String seccionConvencionCol;
		
	// Objeto para mostrar en presentacion la informacion
	private DtoCitaOdontologica citaConfirmacion;
	
	private DtoCitaOdontologica citaAgendaSel;
	private DtoCitaOdontologica citaCreada;
	private ArrayList<DtoCitaOdontologica> listaProximasCitasCancelar;
	@SuppressWarnings("unused")
	private int numListaProximasCitas;
	private int posCitaAgen;
	private int codigoCitaAgen;
	private String evenAgenda;
	private String horaSubAgendaSel;
	
	private int codCentroCostoXuniAgen;
	private String codMotivoCancelacion;
	private String fechaReprogramacion;
	private String procesoExito;
	private String functionPintarAgen;
	
	private String urlCitasOdonto;
	private String casoBusServicio;
	private String cambiarSerOdo;
	private String tipoCitaCrear;
	private int codigoPlanTratamiento;
 	
	// atributos para validacion visualizacion agenda odontologica
	private ArrayList<InfoDatosInt> consultoriosAgenOdon;
	private String horaIniAgenOdon;
	private String horaFinAgenOdon;
	private ArrayList<String> intervaloHora;
	private ArrayList<String> intervaloHoraAgen;
	private String patronOrdenarCitas;
	private String patronOrdenarBen;
	private String patronOrdenar;
	private String patronOrdenarSerOdo;
	
	// atributos xml agenda odontologica
	private String xmlCitaAgenda;
	
	// atributo para la visualizacion de servicios por programa
	private TreeSet<String> seccion = new TreeSet<String>();
	private TreeSet<String> programas = new TreeSet<String>();

	// atributos menu
	private String reserva;
	private String asignar;
	private String cancelar;
	private String confirmar;
	private String reprogramar;
	private String cuposExtra;
	private String cambiarServicio;
	private String hayLinks;
	
	//*********************NUEVOS ATRIBUTOS*********************************************
	private ArrayList<HashMap<String, Object>> listTipIdent;
	
	//Atributos Seccion I Ingreso del Paciente
	private String tipoIdentificacionPac;
	private String numeroIdentificacionPac;
	private boolean existePaciente;
	private boolean tieneIngresoPaciente;
	private String urlIngresoPaciente;
	private String urlCuenta;
	
	//Atributos seccion II REcord de citas
	private ArrayList<DtoCitaOdontologica> citasOdonPac;
	private ArrayList<DtoCitaOdontologica> todasCitas;
	private DtoPaciente paciente;
	private String codigoActividad;
	private int posicionCita;
	private int posServicio;
	
	//Atributos pagina confirmacino.
	private String tipoConfirmacion;
	private ArrayList<HashMap<String, Object>> motivosNoConfirmacion;
	private double saldoActualPaciente;
	private String observacionesConfirmacion;
	
	//Atributos seccion III Criterios busqueda
	private int centroAtencion;
	private String unidadAgenda;
	private String profesionalSalud;
	private String tipoCita;
	private String pais;
	private String ciudad;
	private String fecha;
	private String estadoCita;
	private String horaInicio;
	private String horaFinal;
	private String[] indicativoConf;
	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudades = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap> listUnidadesAgendaXUsuario;
	private ArrayList<HashMap<String, Object>> listProfesionalesActivos;
	private ArrayList<HashMap<String,Object>> listCentroAtencion;
	private ArrayList<InfoDatosString> tiposCita = new ArrayList<InfoDatosString>();
	private int codCuentaPaciente;
	
	//Atributos Seccion IV Agenda odontologica
	private String xmlAgenda;
	private int codigoAge;
	
	private int duracionCita;
	private int totalDuracionServicios;
	
	private DtoAgendaOdontologica agendaSel;
	private int posAgendaSel;
	
	//Atributos popUp cita
	private ArrayList<HashMap<String, Object>> listCentrosCostoXUniAgen;
	private ArrayList<DtoServicioOdontologico> serviciosOdon;
	
	//popUp Asignacion y Reserva
	private String habilitarCalcTarifa;
	

	//Atributos Cancelacion.
	/**
	 * Atributo que indica si la cancelacion se hace por paciente o por institución
	 * S: Paciente
	 * N: Institución
	 */
	private String tipoCancelacion;
	private String porPaciente;
	
	//Atributos Cambio Servicio
	private int citaSeleccionada;
	
	//************************************************************************************
	
	/**
	 * Indica si se deben mostrar las tarifas en el flujo de la asignación (Después de validaciones)
	 */
	private boolean mostrarTarifas;
	
	/**
	 * Almacena los abonos diponibles del paciente para ser mostrados en las tarifas
	 */
	private double abonosDisponiblesPaciente;
	
	/**
	 * Listado de los responsables de la cuenta
	 */
	private ArrayList<DtoSubCuentas> responsablesCuenta;
	
	/** sumatoria d elos servicios seleccionados*/
	private double valorTotalServicios;
	
	/** diferencia entre el abono del paciente y el total de los servicios de la cita*/
	private double valorPorAbonar;
	
	
	private boolean permitirCupoExtra;
	
	private DtoCitaOdontologica dtoCitaProgramada;
	
	
	/**
	 * Atributo de presentacion para validar si seleccion tipo de cancelacion
	 * 
	 */
	private boolean seleccionTipoCancelacion;
	
	/**
	 * Atributo de presentacion para validar si se seleccoin un motivo de cancelacion
	 */
	private boolean seleccionMotivoCancelacion;
	
	
	/**
	 * Atributo para validar si 
	 * al cancelar citas odontologicas dejarlas automoticamente en estado a reprogramar
	 */
	private String cancelandoCitaOdoEstadoReprogramar;
	
	/**
	 * Metodo para reprogramar citas
	 * Este atributo sirve para validar si la cita es reprogramada en otro caso es cancelada.
	 */
	private String reprogramaCita;
	
	
	/**
	 * Saldo Total Servicios 
	 * atributo que guarda el total a guardar de los servicios
	 * Utilizado solamente en Interfaz grafica.
	 */
	private String saldoTotalServcios;
	
	/**
	 * Atributo que indica si las flechas utilizadas para
	 * desplazarse entre un rango de fechas, se habilitan o no
	 */
	private boolean mostrarDesplazarFecha;
	
	
	// Sección Asignacion y Reserva de Citas
	/** Lista de convenios Activos del paciente */
	private ArrayList<DtoSeccionConvenioPaciente> listaConveniosPaciente;
	/** Informacion relacionada con convenios-Contrato */
	private DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente;
	/** Lista de contatos asociados al convenio seleccionado */
	private ArrayList<Contratos> listaContratoConvenio;
	/** Indica si hay un convenio seleccionado */
	private boolean convenioSeleccionado;
	/** Lista de las areas (Centro de Costo asociados al centro de atención de la Unidad de agenda de  la cita  */
	private ArrayList<DtoCentroCostosVista> listaAreas;
	/** código del area seleccionada */
	private String codigoArea;
	/** Indica si segun las validaciones de seleccion de convenio, esta sección debe se mostrada */
	private boolean mostrarSeccionconvenio;
	/** Indica el convenio seleccionado maneja bono */
	private boolean convenioManejaBono;
	/** Indica el convenio seleccionado maneja bono y es requerido */
	private boolean convenioManejaBonoRequerido;
	/** * Indica si se debe mostrar el valor de los servicios en la lista */
	private boolean mostrarTarifaServicios;
	/** * Indica si pasó las validaciones para guardar */
	private boolean pasoValidacionesGuardar;
	/** * Indica si se debe mostrar el valor total de la cita */
	private boolean mostrarValorTotalCita;
	/** * Consecutivo de la cita guardada */
	private int codigoCitaGuardada;
	/** * Consecutivo de la cita tomada del record de citas */
	private int codigoCitaRecord;
	/** * Estado de la cita tomada del record de citas */
	private String estadoCitaRecord;
	
	
	

	/**
	 * Atributo que indica si se habilita o no el listado
	 * de Paises para que se pueda cambiar.
	 */
	private boolean inhabilitaPais; 
	

	/**
	 * Atributo que indica si se habilita o no el listado
	 * de Ciudades para que se pueda cambiar.
	 */
	private boolean inhabilitaCiudad; 
	

	/**
	 * Atributo que indica si se habilita o no el listado
	 * de Centros de atenci&oacute;n para que se pueda cambiar.
	 */
	private boolean inhabilitaCentroAtencion; 
	
	
	/**
	 * Atributo que indica si se habilita o no el listado
	 * de tipos de cita para que se pueda cambiar.
	 */
	private boolean inhabilitaTipoCita; 
	
	
	/**
	 * Atributo que indica si se habilita o no el listado
	 * de Unidades de Agenda para que se pueda cambiar.
	 */
	private boolean inhabilitaUnidadAgenda; 
	
	/*
	#############################################################
	#	RESET													#			
	#############################################################
	*/
	
	/**
	 * Atributo con los n&uacute;meros de d&iacute;gitos de captura
	 * para el campo identificaci&oacute;n del paciente
	 */
	private int numDigCaptNumIdPac;
	
	/**
	 * Arreglo que tiene otros servicios que van a estar disponibles
	 * para su selecci&oacute;n 
	 */
	private ArrayList<DtoServicioOdontologico> otrosServiciosOdon;
	
	
	/**
	 * Atributo que indica si se inhabilita o no el link de otros servicios
	 */
	private boolean inhabilitaOtrosServicios;
	
	
	
	/**
	 * Atributo que indica si se inhabilita o no el link de Recibos de Caja
	 */
	private boolean inhabilitaLinkRecibosCaja;

	/**
	 * Atributo que indica si existen registros de servicios
	 * para asociarse a la asignacion o a la reserva de la cita.
	 */
	private boolean existenRegistroServicios;
	
	/**
	 * Atributo que indica si el usuario en sesi&oacute;n tiene el
	 * rol para modificar la duraci&oacute;n de los servicios asociados a la cita
	 */
	private boolean permiteModificarDuracionServicio;
	
	/**
	 * Atributo que indica la ruta para ingresar a la funcionalidad Recibos de caja.
	 */
	private String urlRecibosCaja;
	
	
	/**
	 * Atributo con el nombre del archivo generado cuando se realiza
	 * la impresión de la cita
	 */
	private String nombreArchivoImpresionCita;
	
	/**
	 * Atributo que contiene la página de origen desde la cual se realiza el
	 * llamado a la búsqueda de la Agenda odontológica
	 */
	private String paginaOrigen;
	
	/**
	 * Atributo que indica cual es el caso de cita que se esta 
	 * presentando, para determinar como se debe realizar el
	 * proceso de consulta de servicios odontológicos
	 */
	private String casoCita;
	
	/**
	 * Atributo que indica cuando se debe recargar la página
	 * de Criterios de Búsqueda cuando se regresan 
	 * desde la Asignación o Reserva de Citas.
	 */
	private String recargarCriteriosBusqueda;
	
	
	private boolean tienePlanTratamientoInactivo;
	
	/**
	 * Dto con la información necesaria para
	 * la realización de la búsqueda de la Agenda Odontológica
	 * por Rango.
	 */
	private DtoBusquedaAgendaRango dtoBusquedaAgendaRango;
	
	/**
	 * Atributo que almacena el indice con la cita que ha sido seleccionada
	 * por el usuario para ver el histórico de la misma.
	 */
	private int indiceCitaSeleccionada;
	
	/**
	 * Almacena la información de la cita odontológica
	 * que fue seleccionada con el fin de ver su histórico.
	 */
	private DtoCitaOdontologica citaSeleccionadaHistorico;
	
	/**
	 * Atributo que almacena los datos a mostrar en el histórico
	 * de la cita seleccionada por el usuario.
	 */
	private ArrayList<DtoCitaOdontologica> historicoCita;
	
//	/**
//	 * Atributo utilizado para activar o desactivar el proceso de confirmación
//	 * de la generación del cupo extra.
//	 */
//	private boolean confirmacionCupoExtra;
	
	/**
	 * Atributo que permite definir si se debe mostrar el campo
	 * tipo de cancelación.
	 */
	private String mostrarTipoCancelacion;
	
	/**
	 * Permite bloquear un botón después de que el usuario ha dado clic una vez.
	 */
	private boolean bloquearClic;
	
	private double valorTotalServiciosAbono; 
	
	
	
	/**
	 * Limpia la informacion de la cita
	 */
	public void resetDatosCita(){
		
		this.listaConveniosPaciente		= new ArrayList<DtoSeccionConvenioPaciente>();
		this.dtoSeccionConvenioPaciente	= new DtoSeccionConvenioPaciente();
		this.listaContratoConvenio		= new ArrayList<Contratos>();
		this.listaAreas					= new ArrayList<DtoCentroCostosVista>();
		this.codigoArea					= "";
	}


	/**
	 * Reset de toda la forma
	 */
	public void reset(){
		
		// Atributos de visualizacion
		this.codigoActividadAutorizacion = ConstantesBD.codigoNuncaValido;
		this.listCentrosCostoXUniAgen = new ArrayList<HashMap<String,Object>>();
		this.listMotivoCancelacion = new ArrayList<HashMap>();
		
		// Atributos seccion I
		this.tipoIdentificacionPac = "";
		this.numeroIdentificacionPac = "";
		this.paginaLinkSiguienteBen = "";
		this.posicionBeneficiario = ConstantesBD.codigoNuncaValido;
		this.paginaLinkSiguienteCita = "";
		
		// Atributos seccion II
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.fechaInicial = UtilidadFecha.getFechaActual();
		this.fechaFinal = UtilidadFecha.getFechaActual();
		this.unidadAgenda = "";
		//this.consultorio = "";
		//this.diaSemana = "";
		this.profesionalSalud = "";
		
		// Atributos Seccion III
		this.fechaDesplazamiento = "";//UtilidadFecha.getFechaActual();
		this.sentidoDesplazamiento = "";
		this.desplazamientoFecha = ConstantesBD.codigoNuncaValido;
		
		//parametros generales
		this.numDiasAntFechaActual = "";
		this.multiploMinGenCitas = "";
		this.validaPresupuestoContratado = "";
		this.utilProgOdonPlanTra = "";
		this.minutosEsperaAsgCitOdoCad = "";
		
		// Atributos generales
		this.agendaOdon = new ArrayList<DtoAgendaOdontologica>();
		this.convencionColores = new ArrayList<InfoDatosString>();
		this.seccionConvencionCol = ConstantesBD.acronimoNo;
		
		this.serviciosOdon = new ArrayList<DtoServicioOdontologico>();
		this.setOtrosServiciosOdon(new ArrayList<DtoServicioOdontologico>());
		
		this.agendaSel = new DtoAgendaOdontologica(); 
		this.posAgendaSel = ConstantesBD.codigoNuncaValido;
		
		this.citaAgendaSel = new DtoCitaOdontologica();
		this.citaCreada = new DtoCitaOdontologica();
		this.posCitaAgen = ConstantesBD.codigoNuncaValido;
		this.codigoCitaAgen = ConstantesBD.codigoNuncaValido;
		this.evenAgenda = "";
		this.horaSubAgendaSel = "";
		
		this.duracionCita = 0;
		this.codCentroCostoXuniAgen = ConstantesBD.codigoNuncaValido;
		this.tipoCita = "";
		this.codMotivoCancelacion = "";
		this.fechaReprogramacion = "";
		this.procesoExito = ConstantesBD.acronimoNo;
		this.functionPintarAgen = "";
		
		this.urlCitasOdonto = "../agendaOdontologica/agendaOdontologica.do";
		
		this.casoBusServicio = "";
		this.cambiarSerOdo = ConstantesBD.acronimoNo;
		this.tipoCitaCrear = "";
		this.codigoPlanTratamiento = ConstantesBD.codigoNuncaValido;
		
		// atributos para validacion visualizacion agenda odontologica
		this.consultoriosAgenOdon = new ArrayList<InfoDatosInt>();
		this.horaIniAgenOdon = "";
		this.horaFinAgenOdon = "";
		this.intervaloHora = new ArrayList<String>();
		this.intervaloHoraAgen = new ArrayList<String>();
		this.patronOrdenarCitas = "centroatencion";
		this.patronOrdenarBen = "identificacion";
		this.patronOrdenar = "";
		this.patronOrdenarSerOdo = "pieza";
		
		// atributos xml agenda odontologica
		this.xmlCitaAgenda = "";
		
		// atributos menu
		this.reserva = ConstantesBD.acronimoNo;
		this.asignar = ConstantesBD.acronimoNo;
		this.cancelar = ConstantesBD.acronimoNo;
		this.reprogramar = ConstantesBD.acronimoNo;
		this.confirmar= ConstantesBD.acronimoNo;
		this.cuposExtra = ConstantesBD.acronimoNo;
		this.cambiarServicio = ConstantesBD.acronimoNo;
		this.hayLinks = "";
		
		//**************nuevos atributos**********************************
		this.listTipIdent = new ArrayList<HashMap<String,Object>>();
		this.tipoIdentificacionPac = "";
		this.numeroIdentificacionPac = "";
		this.existePaciente = false;
		this.tieneIngresoPaciente = false;
		this.urlIngresoPaciente = "../ingresarPacienteOdontologia/ingresoPacienteOdontologia.do";
		this.urlCuenta = "../aperturaCuentaPacienteOdontologico/aperturaCuentaPacienteOdontologico.do?estado=empezar";
		this.citasOdonPac = new ArrayList<DtoCitaOdontologica>();
		this.todasCitas = new ArrayList<DtoCitaOdontologica>();
		this.paciente = new DtoPaciente();
		this.posicionCita = ConstantesBD.codigoNuncaValido;
		this.codigoActividad = "";
		this.tipoConfirmacion = "";
		this.motivosNoConfirmacion = new ArrayList<HashMap<String, Object>>();
		this.saldoActualPaciente = 0;
		this.observacionesConfirmacion = "";
		this.posServicio = ConstantesBD.codigoNuncaValido;
		this.resetCriteriosBusqueda();
		this.xmlAgenda = "";
		this.codigoAge = ConstantesBD.codigoNuncaValido;
		this.horaInicio = "";
		this.horaFinal = "";
		this.estadoCita= "";
		this.indicativoConf=new String[0];
		this.duracionCita = 0;
		this.totalDuracionServicios = 0;
		this.agendaSel = new DtoAgendaOdontologica(); 
		this.posAgendaSel = ConstantesBD.codigoNuncaValido;
		this.listCentrosCostoXUniAgen = new ArrayList<HashMap<String,Object>>();
		this.serviciosOdon = new ArrayList<DtoServicioOdontologico>();
		this.tipoFlujoPaciente="";
		this.porPaciente=ConstantesBD.acronimoNo;	
		this.resetCancelacion();
	
		//Atributos Cambio Servicio
		this.citaSeleccionada= ConstantesBD.codigoNuncaValido;
		
		this.mostrarTarifas=false;
		this.abonosDisponiblesPaciente=0;
		this.valorTotalServicios=0;
		this.valorPorAbonar=0;
		this.permitirCupoExtra=false;
		this.listaProximasCitasCancelar=new ArrayList<DtoCitaOdontologica>();
		this.dtoCitaProgramada= new DtoCitaOdontologica();
		this.habilitarCalcTarifa=ConstantesBD.acronimoNo;
		this.codCuentaPaciente= ConstantesBD.codigoNuncaValido;
		this.citaConfirmacion= new DtoCitaOdontologica();
		
		this.listaConveniosPaciente 	= new ArrayList<DtoSeccionConvenioPaciente>();
		this.dtoSeccionConvenioPaciente = new DtoSeccionConvenioPaciente();
		this.listaContratoConvenio 		= new ArrayList<Contratos>();
		this.convenioSeleccionado 		= false;
		this.listaAreas					= new ArrayList<DtoCentroCostosVista>();
		this.codigoArea					= "";
		this.mostrarSeccionconvenio 	= false;
		this.convenioManejaBono 		= false;
		this.convenioManejaBonoRequerido= false;
		this.mostrarTarifaServicios 	= true;
		this.pasoValidacionesGuardar 	= false;
		this.mostrarValorTotalCita		= false;
		this.codigoCitaGuardada			= ConstantesBD.codigoNuncaValido;
		this.codigoCitaRecord			= ConstantesBD.codigoNuncaValido;
		this.estadoCitaRecord			= "";
		

		this.citaConfirmacion= new DtoCitaOdontologica();
		this.cancelandoCitaOdoEstadoReprogramar="";
		this.setMostrarDesplazarFecha(false);
		this.seleccionTipoCancelacion=Boolean.FALSE;
		this.seleccionMotivoCancelacion=Boolean.FALSE;
		
		this.saldoTotalServcios="";
		
		
		this.setMostrarDesplazarFecha(false);
		
		this.setInhabilitaTipoCita(false);
		
		this.setInhabilitaPais(false);
		
		this.setInhabilitaCiudad(false);
		
		this.setInhabilitaCentroAtencion(false);
		
		this.setInhabilitaUnidadAgenda(false);

		this.setNumDigCaptNumIdPac(ConstantesBD.codigoNuncaValido);
		
		this.setInhabilitaOtrosServicios(false);

		this.setInhabilitaLinkRecibosCaja(true);
	
		this.setExistenRegistroServicios(false);
	
		this.setPermiteModificarDuracionServicio(false);
		
		this.setUrlRecibosCaja("");
		
		this.setNombreArchivoImpresionCita("");
		
		this.setPaginaOrigen("");
		
		this.setCasoCita("");
		
		this.recargarCriteriosBusqueda  ="";
		
		this.tienePlanTratamientoInactivo = false;
		
		this.dtoBusquedaAgendaRango = new DtoBusquedaAgendaRango();
		
		this.indiceCitaSeleccionada = 0;
		
		this.citaSeleccionadaHistorico = new DtoCitaOdontologica();
		this.historicoCita = new ArrayList<DtoCitaOdontologica>();
		this.mostrarTipoCancelacion = "";
		this.bloquearClic = false;
		this.valorTotalServiciosAbono= ConstantesBD.codigoNuncaValidoDouble;
	
	}
	
	public void resetCancelacionCita(){
		this.seleccionTipoCancelacion=Boolean.FALSE;
		this.seleccionMotivoCancelacion=Boolean.FALSE;
		
	}
	
	

	public void resetCancelacion()
	{
		this.tipoCancelacion="";
		this.codMotivoCancelacion="";
		this.listaProximasCitasCancelar= new ArrayList<DtoCitaOdontologica>(); 
		this.numListaProximasCitas=0;
	}
	
	
	/**
	 * Reset de los criterios de búsqueda
	 */
	public void resetCriteriosBusqueda()
	{
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.fecha = "";
		this.unidadAgenda = "";
		this.profesionalSalud = "";
		this.tipoCita = "";
		this.pais = "";
		this.ciudad = "";
		this.estadoCita= "";
		this.indicativoConf=new String[0];
		
		this.paises = new ArrayList<HashMap<String,Object>>();
		this.ciudades = new ArrayList<HashMap<String,Object>>();
		this.listProfesionalesActivos = new ArrayList<HashMap<String, Object>>();
		this.listUnidadesAgendaXUsuario = new  ArrayList<HashMap>();
		this.listCentroAtencion = new ArrayList<HashMap<String,Object>>();
		this.tiposCita = new ArrayList<InfoDatosString>();
		this.fechaInicial=UtilidadFecha.getFechaActual();
		this.fechaFinal=UtilidadFecha.getFechaActual();
		this.saldoTotalServcios="";
		
		this.setInhabilitaTipoCita(false);
		this.setInhabilitaPais(false);
		this.setInhabilitaCiudad(false);
		this.setInhabilitaCentroAtencion(false);
		this.setInhabilitaUnidadAgenda(false);
		
		this.citaConfirmacion= new DtoCitaOdontologica();
		
		this.recargarCriteriosBusqueda  ="";
		//this.setConfirmacionCupoExtra(false);
	}

	
	/**
	 * Limpia los timpos de cita
	 */
	public void resetTiposCita()
	{
		this.tiposCita = new ArrayList<InfoDatosString>();		
	}
	
	
	
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validación encontrados. Si no se
	* encontraron errores de validación, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		
		//********************NUEVOS ESTADOS DE VALDIACION*******************************
		if(this.estado.equals("validarPaciente"))
		{
			
			/*
			 * Se habló en documentación y se determina que no es válido sacar este mensaje 
			 * cuando el usuario en sesión es el mismo paciente. 
			 */
			
//			UsuarioBasico usuario=UtilidadSiEs.obtenerUsuarioBasico(request);
			
//			if(usuario.getNumeroIdentificacion().equals(this.numeroIdentificacionPac))
//			{
//				if(usuario.getCodigoTipoIdentificacion().equals(this.tipoIdentificacionPac))
//				{
//					errores.add("paciente mismo usuario", new ActionMessage("errors.notEspecific", "El paciente debe ser diferente del usuario en sesión"));
//				}
//				
//			}

			if(this.tipoIdentificacionPac.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El tipo de identificación"));
			}
			if(this.numeroIdentificacionPac.trim().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El número de identificación"));
			}
			if(UtilidadCadena.tieneCaracteresEspecialesNumeroId(this.numeroIdentificacionPac))
			{
				errores.add("", new ActionMessage("errors.caracteresInvalidos","El número de identificación"));
			}
			
			if(!errores.isEmpty())
			{
				this.estado = "";
			}
		}
		
		//********************************************************************************
		
		
		if(this.estado.equals("buscarAgenOdon") || this.estado.equals("desplazamientoFecha"))
		{
			// Validacion del parametro general Múltiplo en Minutos para la Generación de Citas Odontológicas
			if(this.multiploMinGenCitas.equals("") || this.multiploMinGenCitas==null || Utilidades.convertirAEntero(this.multiploMinGenCitas)<=0)
				errores.add("fechas",new ActionMessage("errors.required", "El Parametro General Múltiplo en Minutos para Generación de Citas Odontológicas "));
			
			if(errores.isEmpty())
			{
				// validacion de las fechas
				boolean fechaInicialValida = false, fechaFinalValida = false;
	
				if(!this.fechaInicial.equals("") && !this.fechaFinal.equals("")){
					fechaInicialValida = true;
					fechaFinalValida = true;
				}else{
					if(!this.fechaInicial.equals("") || !this.fechaFinal.equals(""))
						errores.add("fechas",new ActionMessage("errors.notEspecific", !this.fechaInicial.equals("")?"El Campo Fecha Final":"El Campo Fecha Inicia"));
				}
					
				// se valida que la fecha inicial y la final esten en el formato adecuado
				if(fechaInicialValida && fechaFinalValida)
				{
					if(!UtilidadFecha.validarFecha(this.fechaInicial)){
						errores.add("fechaInicio",new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicial"));
						fechaInicialValida = false;
					}			
					if(!UtilidadFecha.validarFecha(this.fechaFinal)){
						errores.add("fechaFinal",new ActionMessage("errors.formatoFechaInvalido", "Fecha Final"));
						fechaFinalValida = false;
					}
				}
				
				// valida que la fecha cumplan con la estipulaciones de validaciones del anexo 864
				if(fechaInicialValida&& fechaFinalValida)
				{
					
					if(!this.numDiasAntFechaActual.equals("")&&Utilidades.convertirAEntero(this.numDiasAntFechaActual)>0)
					{
						int decremento = Utilidades.convertirAEntero(this.numDiasAntFechaActual)*ConstantesBD.codigoNuncaValido;
						logger.info("valor decremento: "+decremento);
						
						// validar que la fecha de incio se igual o mayor a la fecha de actual
						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(
								UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial), 
								UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(),decremento,false)))
						{
							// se valida la fecha final si es igual o mayor a la fecha inicial
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(
									UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal), 
									UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial))){
								// error en la fecha final
								errores.add("fechaFinal",new ActionMessage("errors.fechaAnteriorIgualActual", "Final", "Inicial"));
								fechaFinalValida = false;
							}
						}else{
							// error en la fecha inicial
							errores.add("fechaInicio",new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Actual menos el número de días anteriores. "));
							fechaInicialValida = false;
						}
					}else{
						// validar que la fecha de incio se igual o mayor a la fecha de actual
						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(
								UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial), 
								UtilidadFecha.getFechaActual()))
						{
							// se valida la fecha final si es igual o mayor a la fecha inicial
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(
									UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal), 
									UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial))){
								// error en la fecha final
								errores.add("fechaFinal",new ActionMessage("errors.fechaAnteriorIgualActual", "Final", "Inicial"));
								fechaFinalValida = false;
							}
						}else{
							// error en la fecha inicial
							errores.add("fechaInicio",new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Actual"));
							fechaInicialValida = false;
						}
					}
					
					
				}
				
				// se valida que el rango entre la fecha inicial y la final no se superior al  tres meses
				// esta parte se valida si las validaciones de la fecha inicial y final han sido correctas
				if(fechaInicialValida&& fechaFinalValida){
					int nroMeses = UtilidadFecha.numeroMesesEntreFechas(
							UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial),
							UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal),true);
					if (nroMeses > 3)
						errores.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "AGENDA ODONTOLOGICA"));
				}
				// fin validacion de las fechas	
			}
		}
		
		if(this.estado.equals("realizarBusquedaRango")){
			
			if (this.dtoBusquedaAgendaRango.getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar) ||
					this.dtoBusquedaAgendaRango.getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) {
				
				if (UtilidadTexto.isEmpty(this.dtoBusquedaAgendaRango.getTipoCancelacion())) {
					errores.add("", new ActionMessage("errors.required","El Tipo de Cancelación"));
				}
			}
			
		}
		
		return errores;
	}
	
	
	
	/*
	#############################################################
	#	SETs y GETs												#			
	#############################################################
	*/
	
	public DtoSeccionConvenioPaciente getDtoSeccionConvenioPaciente() {
		return dtoSeccionConvenioPaciente;
	}


	public void setDtoSeccionConvenioPaciente(
			DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente) {
		this.dtoSeccionConvenioPaciente = dtoSeccionConvenioPaciente;
	}

	
	public DtoCitaOdontologica getDtoCitaProgramada() {
		return dtoCitaProgramada;
	}


	public void setDtoCitaProgramada(DtoCitaOdontologica dtoCitaProgramada) {
		this.dtoCitaProgramada = dtoCitaProgramada;
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
	 * @return the listCentroAtencion
	 */
	public ArrayList<HashMap<String,Object>> getListCentroAtencion() {
		return listCentroAtencion;
	}

	/**
	 * @param listCentroAtencion the listCentroAtencion to set
	 */
	public void setListCentroAtencion(ArrayList<HashMap<String,Object>> listCentroAtencion) {
		this.listCentroAtencion = listCentroAtencion;
	}

	/**
	 * @return the listUnidadesAgendaXUsuario
	 */
	public ArrayList<HashMap> getListUnidadesAgendaXUsuario() {
		return listUnidadesAgendaXUsuario;
	}

	/**
	 * @param listUnidadesAgendaXUsuario the listUnidadesAgendaXUsuario to set
	 */
	public void setListUnidadesAgendaXUsuario(
			ArrayList<HashMap> listUnidadesAgendaXUsuario) {
		this.listUnidadesAgendaXUsuario = listUnidadesAgendaXUsuario;
	}

	/**
	 * @return the listProfesionalesActivos
	 */
	public ArrayList<HashMap<String, Object>> getListProfesionalesActivos() {
		return listProfesionalesActivos;
	}

	/**
	 * @param listProfesionalesActivos the listProfesionalesActivos to set
	 */
	public void setListProfesionalesActivos(
			ArrayList<HashMap<String, Object>> listProfesionalesActivos) {
		this.listProfesionalesActivos = listProfesionalesActivos;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the codigoActividadAutorizacion
	 */
	public int getCodigoActividadAutorizacion() {
		return codigoActividadAutorizacion;
	}

	/**
	 * @param codigoActividadAutorizacion the codigoActividadAutorizacion to set
	 */
	public void setCodigoActividadAutorizacion(int codigoActividadAutorizacion) {
		this.codigoActividadAutorizacion = codigoActividadAutorizacion;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the profesionalSalud
	 */
	public String getProfesionalSalud() {
		return profesionalSalud;
	}

	/**
	 * @param profesionalSalud the profesionalSalud to set
	 */
	public void setProfesionalSalud(String profesionalSalud) {
		this.profesionalSalud = profesionalSalud;
	}

	/**
	 * @return the numDiasAntFechaActual
	 */
	public String getNumDiasAntFechaActual() {
		return numDiasAntFechaActual;
	}

	/**
	 * @param numDiasAntFechaActual the numDiasAntFechaActual to set
	 */
	public void setNumDiasAntFechaActual(String numDiasAntFechaActual) {
		this.numDiasAntFechaActual = numDiasAntFechaActual;
	}

	/**
	 * @return the multiploMinGenCitas
	 */
	public String getMultiploMinGenCitas() {
		return multiploMinGenCitas;
	}

	/**
	 * @param multiploMinGenCitas the multiploMinGenCitas to set
	 */
	public void setMultiploMinGenCitas(String multiploMinGenCitas) {
		this.multiploMinGenCitas = multiploMinGenCitas;
	}
	
	
	/**
	 * @return the agendaOdon
	 */
	public ArrayList<DtoAgendaOdontologica> getAgendaOdon() {
		return agendaOdon;
	}

	/**
	 * @param agendaOdon the agendaOdon to set
	 */
	public void setAgendaOdon(ArrayList<DtoAgendaOdontologica> agendaOdon) {
		this.agendaOdon = agendaOdon;
	}

	/**
	 * @return the convencionColores
	 */
	public ArrayList<InfoDatosString> getConvencionColores() {
		return convencionColores;
	}

	/**
	 * @param convencionColores the convencionColores to set
	 */
	public void setConvencionColores(ArrayList<InfoDatosString> convencionColores) {
		this.convencionColores = convencionColores;
	}

	/**
	 * @return the seccionConvencionCol
	 */
	public String getSeccionConvencionCol() {
		return seccionConvencionCol;
	}

	/**
	 * @param seccionConvencionCol the seccionConvencionCol to set
	 */
	public void setSeccionConvencionCol(String seccionConvencionCol) {
		this.seccionConvencionCol = seccionConvencionCol;
	}

	/**
	 * @return the consultoriosAgenOdon
	 */
	public ArrayList<InfoDatosInt> getConsultoriosAgenOdon() {
		return consultoriosAgenOdon;
	}

	/**
	 * @param consultoriosAgenOdon the consultoriosAgenOdon to set
	 */
	public void setConsultoriosAgenOdon(ArrayList<InfoDatosInt> consultoriosAgenOdon) {
		this.consultoriosAgenOdon = consultoriosAgenOdon;
	}

	/**
	 * @return the horaIniAgenOdon
	 */
	public String getHoraIniAgenOdon() {
		return horaIniAgenOdon;
	}

	/**
	 * @param horaIniAgenOdon the horaIniAgenOdon to set
	 */
	public void setHoraIniAgenOdon(String horaIniAgenOdon) {
		this.horaIniAgenOdon = horaIniAgenOdon;
	}

	/**
	 * @return the horaFinAgenOdon
	 */
	public String getHoraFinAgenOdon() {
		return horaFinAgenOdon;
	}

	/**
	 * @param horaFinAgenOdon the horaFinAgenOdon to set
	 */
	public void setHoraFinAgenOdon(String horaFinAgenOdon) {
		this.horaFinAgenOdon = horaFinAgenOdon;
	}

	/**
	 * @return the fechaDesplazamiento
	 */
	public String getFechaDesplazamiento() {
		return fechaDesplazamiento;
	}

	/**
	 * @param fechaDesplazamiento the fechaDesplazamiento to set
	 */
	public void setFechaDesplazamiento(String fechaDesplazamiento) {
		this.fechaDesplazamiento = fechaDesplazamiento;
	}

	/**
	 * @return the intervaloHora
	 */
	public ArrayList<String> getIntervaloHora() {
		return intervaloHora;
	}

	/**
	 * @param intervaloHora the intervaloHora to set
	 */
	public void setIntervaloHora(ArrayList<String> intervaloHora) {
		this.intervaloHora = intervaloHora;
	}

	/**
	 * @return the sentidoDesplazamiento
	 */
	public String getSentidoDesplazamiento() {
		return sentidoDesplazamiento;
	}

	/**
	 * @param sentidoDesplazamiento the sentidoDesplazamiento to set
	 */
	public void setSentidoDesplazamiento(String sentidoDesplazamiento) {
		this.sentidoDesplazamiento = sentidoDesplazamiento;
	}

	/**
	 * @return the desplazamientoFecha
	 */
	public int getDesplazamientoFecha() {
		return desplazamientoFecha;
	}

	/**
	 * @param desplazamientoFecha the desplazamientoFecha to set
	 */
	public void setDesplazamientoFecha(int desplazamientoFecha) {
		this.desplazamientoFecha = desplazamientoFecha;
	}

	/**
	 * @return the xmlAgenda
	 */
	public String getXmlAgenda() {
		return xmlAgenda;
	}

	/**
	 * @param xmlAgendas the xmlAgendas to set
	 */
	public void setXmlAgenda(String xmlAgenda) {
		this.xmlAgenda = xmlAgenda;
	}

	/**
	 * @return the listTipIdent
	 */
	public ArrayList<HashMap<String, Object>> getListTipIdent() {
		return listTipIdent;
	}

	/**
	 * @param listTipIdent the listTipIdent to set
	 */
	public void setListTipIdent(ArrayList<HashMap<String, Object>> listTipIdent) {
		this.listTipIdent = listTipIdent;
	}

	/**
	 * @return the tipoIdentificacionPac
	 */
	public String getTipoIdentificacionPac() {
		return tipoIdentificacionPac;
	}

	/**
	 * @param tipoIdentificacionPac the tipoIdentificacionPac to set
	 */
	public void setTipoIdentificacionPac(String tipoIdentificacionPac) {
		this.tipoIdentificacionPac = tipoIdentificacionPac;
	}

	/**
	 * @return the numeroIdentificacionPac
	 */
	public String getNumeroIdentificacionPac() {
		return numeroIdentificacionPac;
	}

	/**
	 * @param numeroIdentificacionPac the numeroIdentificacionPac to set
	 */
	public void setNumeroIdentificacionPac(String numeroIdentificacionPac) {
		this.numeroIdentificacionPac = numeroIdentificacionPac;
	}

	/**
	 * @return the paciente
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the paginaLinkSiguienteBen
	 */
	public String getPaginaLinkSiguienteBen() {
		return paginaLinkSiguienteBen;
	}

	/**
	 * @param paginaLinkSiguienteBen the paginaLinkSiguienteBen to set
	 */
	public void setPaginaLinkSiguienteBen(String paginaLinkSiguienteBen) {
		this.paginaLinkSiguienteBen = paginaLinkSiguienteBen;
	}

	/**
	 * @return the posicionBeneficiario
	 */
	public int getPosicionBeneficiario() {
		return posicionBeneficiario;
	}

	/**
	 * @param posicionBeneficiario the posicionBeneficiario to set
	 */
	public void setPosicionBeneficiario(int posicionBeneficiario) {
		this.posicionBeneficiario = posicionBeneficiario;
	}

	/**
	 * @return the citasOdonPac
	 */
	public ArrayList<DtoCitaOdontologica> getCitasOdonPac() {
		return citasOdonPac;
	}

	/**
	 * @param citasOdonPac the citasOdonPac to set
	 */
	public void setCitasOdonPac(ArrayList<DtoCitaOdontologica> citasOdonPac) {
		this.citasOdonPac = citasOdonPac;
	}

	/**
	 * @return the paginaLinkSiguienteCita
	 */
	public String getPaginaLinkSiguienteCita() {
		return paginaLinkSiguienteCita;
	}

	/**
	 * @param paginaLinkSiguienteCita the paginaLinkSiguienteCita to set
	 */
	public void setPaginaLinkSiguienteCita(String paginaLinkSiguienteCita) {
		this.paginaLinkSiguienteCita = paginaLinkSiguienteCita;
	}

	/**
	 * @return the posicionCita
	 */
	public int getPosicionCita() {
		return posicionCita;
	}

	/**
	 * @param posicionCita the posicionCita to set
	 */
	public void setPosicionCita(int posicionCita) {
		this.posicionCita = posicionCita;
	}

	/**
	 * @return the patronOrdenarCitas
	 */
	public String getPatronOrdenarCitas() {
		return patronOrdenarCitas;
	}

	/**
	 * @param patronOrdenarCitas the patronOrdenarCitas to set
	 */
	public void setPatronOrdenarCitas(String patronOrdenarCitas) {
		this.patronOrdenarCitas = patronOrdenarCitas;
	}

	/**
	 * @return the patronOrdenarBen
	 */
	public String getPatronOrdenarBen() {
		return patronOrdenarBen;
	}

	/**
	 * @param patronOrdenarBen the patronOrdenarBen to set
	 */
	public void setPatronOrdenarBen(String patronOrdenarBen) {
		this.patronOrdenarBen = patronOrdenarBen;
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
	 * @return the serviciosOdon
	 */
	public ArrayList<DtoServicioOdontologico> getServiciosOdon() {
		return serviciosOdon;
	}

	/**
	 * @param serviciosOdon the serviciosOdon to set
	 */
	public void setServiciosOdon(ArrayList<DtoServicioOdontologico> serviciosOdon) {
		this.serviciosOdon = serviciosOdon;
	}
	
	/**
	 * Número de servicios odontologicos de la cita
	 * @return
	 */
	public int getNumServiciosOdon()
	{
		return this.serviciosOdon.size();
	}

	public void resetSerOdon()
	{
		this.serviciosOdon = new ArrayList<DtoServicioOdontologico>();
	}
	/**
	 * @return the posServicio
	 */
	public int getPosServicio() {
		return posServicio;
	}

	/**
	 * @param posServicio the posServicio to set
	 */
	public void setPosServicio(int posServicio) {
		this.posServicio = posServicio;
	}

	/**
	 * @return the agendaSel
	 */
	public DtoAgendaOdontologica getAgendaSel() {
		return agendaSel;
	}

	/**
	 * @param agendaSel the agendaSel to set
	 */
	public void setAgendaSel(DtoAgendaOdontologica agendaSel) {
		this.agendaSel = agendaSel;
	}

	/**
	 * @return the posAgendaSel
	 */
	public int getPosAgendaSel() {
		return posAgendaSel;
	}

	/**
	 * @param posAgendaSel the posAgendaSel to set
	 */
	public void setPosAgendaSel(int posAgendaSel) {
		this.posAgendaSel = posAgendaSel;
	}
	
	public void resetAgenSel(){
		this.agendaSel = new DtoAgendaOdontologica();
	}

	/**
	 * @return the horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the horaFinal
	 */
	public String getHoraFinal() {
		return horaFinal;
	}

	/**
	 * @param horaFinal the horaFinal to set
	 */
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	/**
	 * @return the intervaloHoraAgen
	 */
	public ArrayList<String> getIntervaloHoraAgen() {
		return intervaloHoraAgen;
	}

	public boolean isConvenioSeleccionado() {
		return convenioSeleccionado;
	}


	public void setConvenioSeleccionado(boolean convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
	}


	/**
	 * @param intervaloHoraAgen the intervaloHoraAgen to set
	 */
	public void setIntervaloHoraAgen(ArrayList<String> intervaloHoraAgen) {
		this.intervaloHoraAgen = intervaloHoraAgen;
	}

	/**
	 * @return the duracionCita
	 */
	public int getDuracionCita() {
		return duracionCita;
	}

	/**
	 * @param duracionCita the duracionCita to set
	 */
	public void setDuracionCita(int duracionCita) {
		this.duracionCita = duracionCita;
	}

	/**
	 * @return the tipoCita
	 */
	public String getTipoCita() {
		return tipoCita;
	}

	/**
	 * @param tipoCita the tipoCita to set
	 */
	public void setTipoCita(String tipoCita) {
		this.tipoCita = tipoCita;
	}

	/**
	 * @return the codigoAge
	 */
	public int getCodigoAge() {
		return codigoAge;
	}

	/**
	 * @param codigoAge the codigoAge to set
	 */
	public void setCodigoAge(int codigoAge) {
		this.codigoAge = codigoAge;
	}

	/**
	 * @return the procesoExito
	 */
	public String getProcesoExito() {
		return procesoExito;
	}

	/**
	 * @param procesoExito the procesoExito to set
	 */
	public void setProcesoExito(String procesoExito) {
		this.procesoExito = procesoExito;
	}

	/**
	 * @return the functionPintarAgen
	 */
	public String getFunctionPintarAgen() {
		return functionPintarAgen;
	}

	/**
	 * @param functionPintarAgen the functionPintarAgen to set
	 */
	public void setFunctionPintarAgen(String functionPintarAgen) {
		this.functionPintarAgen = functionPintarAgen;
	}

	/**
	 * @return the xmlCitaAgenda
	 */
	public String getXmlCitaAgenda() {
		return xmlCitaAgenda;
	}

	/**
	 * @param xmlCitaAgenda the xmlCitaAgenda to set
	 */
	public void setXmlCitaAgenda(String xmlCitaAgenda) {
		this.xmlCitaAgenda = xmlCitaAgenda;
	}

	public void resetCita()
	{
		// resetReserva
		this.horaInicio = "";
		this.horaFinal = "";
		this.duracionCita = 0;
		this.tipoCita = "";
		this.procesoExito = ConstantesBD.acronimoNo;
		this.codMotivoCancelacion = "";
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.fechaReprogramacion = "";
	}
	
	public void resetPac()
	{
		this.paciente.setTipoId("");
		this.paciente.setNumeroId("");
	}

	/**
	 * @return the urlIngresoPaciente
	 */
	public String getUrlIngresoPaciente() {
		return urlIngresoPaciente;
	}

	/**
	 * @param urlIngresoPaciente the urlIngresoPaciente to set
	 */
	public void setUrlIngresoPaciente(String urlIngresoPaciente) {
		this.urlIngresoPaciente = urlIngresoPaciente;
	}

	/**
	 * @return the urlCitasOdonto
	 */
	public String getUrlCitasOdonto() {
		return urlCitasOdonto;
	}

	/**
	 * @param urlCitasOdonto the urlCitasOdonto to set
	 */
	public void setUrlCitasOdonto(String urlCitasOdonto) {
		this.urlCitasOdonto = urlCitasOdonto;
	}
	
	public int getCodigoPKPaciente()
	{
		return this.paciente.getCodigo();
	}

	public void setCodigoPKPaciente(int codigo)
	{
		this.paciente.setCodigo(codigo);
	}
	
	public String getTipoIdenPac()    
	{
		return this.paciente.getTipoId();
	}

	public void setTipoIdenPac(String tipoId)
	{
		this.paciente.setTipoId(tipoId);
	}
	
	public String getNumeroIdenPac()
	{
		return this.paciente.getNumeroId();
	}

	public void setNumeroIdenPac(String numeroId)
	{
		this.paciente.setNumeroId(numeroId);
	}
	
	public String getPrimerNomPac() 
	{
		return this.paciente.getPrimerNombre();
	}

	public void setPrimerNomPac(String primerNombre)
	{
		this.paciente.setPrimerNombre(primerNombre);
	}
	
	public String getSegundoNomPac()
	{
		return this.paciente.getSegundoNombre();
	}

	public void setSegundoNomPac(String segundoNombre)
	{
		this.paciente.setSegundoNombre(segundoNombre);
	}
	
	public String getPrimerApellPac()
	{
		return this.paciente.getPrimerApellido();
	}

	public void setPrimerApellPac(String primerApellido)
	{
		this.paciente.setPrimerApellido(primerApellido);
	}
	
	public String getSegundoApellPac()
	{
		return this.paciente.getSegundoApellido();
	}

	public void setSegundoApellPac(String segundoApellido)
	{
		this.paciente.setSegundoApellido(segundoApellido);
	}

	public String getConvenioPac()
	{
		return this.paciente.getConvenio();
	}
	
	public void setConvenioPac(String convenio)
	{
		this.paciente.setConvenio(convenio);
	}
	/**
	 * @return the validaPresupuestoContratado
	 */
	public String getValidaPresupuestoContratado() {
		return validaPresupuestoContratado;
	}

	/**
	 * @param validaPresupuestoContratado the validaPresupuestoContratado to set
	 */
	public void setValidaPresupuestoContratado(String validaPresupuestoContratado) {
		this.validaPresupuestoContratado = validaPresupuestoContratado;
	}

	/**
	 * @return the utilProgOdonPlanTra
	 */
	public String getUtilProgOdonPlanTra() {
		return utilProgOdonPlanTra;
	}

	/**
	 * @param utilProgOdonPlanTra the utilProgOdonPlanTra to set
	 */
	public void setUtilProgOdonPlanTra(String utilProgOdonPlanTra) {
		this.utilProgOdonPlanTra = utilProgOdonPlanTra;
	}

	public void resetPath()
	{
		this.urlIngresoPaciente = "../ingresarPacienteOdontologia/ingresoPacienteOdontologia.do";
		this.urlCitasOdonto = "../agendaOdontologica/agendaOdontologica.do";
		this.urlCuenta = "../aperturaCuentaPacienteOdontologico/aperturaCuentaPacienteOdontologico.do?estado=empezar";
		this.cambiarSerOdo = ConstantesBD.acronimoNo;
		this.casoBusServicio = "";
	}

	/**
	 * @return the casoBusServicio
	 */
	public String getCasoBusServicio() {
		return casoBusServicio;
	}

	/**
	 * @param casoBusServicio the casoBusServicio to set
	 */
	public void setCasoBusServicio(String casoBusServicio) {
		this.casoBusServicio = casoBusServicio;
	}

	public void  setSeccion(ArrayList<DtoServicioOdontologico> array)
	{
		for(DtoServicioOdontologico dto: array)
			this.seccion.add(dto.getSeccionPlanTrata());
	}
	
	public TreeSet<String> getSeccion()
	{
		return this.seccion;
	}
	
	public void setProgramas(ArrayList<DtoServicioOdontologico> array)
	{
		for(DtoServicioOdontologico dto: array)
			this.programas.add(dto.getCodigoPrograma()+"");
	}
	
	public TreeSet<String> getProgramas()
	{
		return this.programas;
	}

	/**
	 * @return the patronOrdenarSerOdo
	 */
	public String getPatronOrdenarSerOdo() {
		return patronOrdenarSerOdo;
	}

	/**
	 * @param patronOrdenarSerOdo the patronOrdenarSerOdo to set
	 */
	public void setPatronOrdenarSerOdo(String patronOrdenarSerOdo) {
		this.patronOrdenarSerOdo = patronOrdenarSerOdo;
	}

	/**
	 * @return the cambiarSerOdo
	 */
	public String getCambiarSerOdo() {
		return cambiarSerOdo;
	}

	/**
	 * @param cambiarSerOdo the cambiarSerOdo to set
	 */
	public void setCambiarSerOdo(String cambiarSerOdo) {
		this.cambiarSerOdo = cambiarSerOdo;
	}

	/**
	 * @return the tipoCitaCrear
	 */
	public String getTipoCitaCrear() {
		return tipoCitaCrear;
	}

	/**
	 * @param tipoCitaCrear the tipoCitaCrear to set
	 */
	public void setTipoCitaCrear(String tipoCitaCrear) {
		this.tipoCitaCrear = tipoCitaCrear;
	}

	/**
	 * @return the codigoPlanTratamiento
	 */
	public int getCodigoPlanTratamiento() {
		return codigoPlanTratamiento;
	}

	/**
	 * @param codigoPlanTratamiento the codigoPlanTratamiento to set
	 */
	public void setCodigoPlanTratamiento(int codigoPlanTratamiento) {
		this.codigoPlanTratamiento = codigoPlanTratamiento;
	}

	/**
	 * @return the listCentrosCostoXUniAgen
	 */
	public ArrayList<HashMap<String, Object>> getListCentrosCostoXUniAgen() {
		return listCentrosCostoXUniAgen;
	}

	/**
	 * @param listCentrosCostoXUniAgen the listCentrosCostoXUniAgen to set
	 */
	public void setListCentrosCostoXUniAgen(
			ArrayList<HashMap<String, Object>> listCentrosCostoXUniAgen) {
		this.listCentrosCostoXUniAgen = listCentrosCostoXUniAgen;
	}
	
	/**
	 * Numero de registros
	 * @return
	 */
	public int getNumListCentrosCostoXUniAgen()
	{
		return this.listCentrosCostoXUniAgen.size();
	}

	/**
	 * @return the codCentroCostoXuniAgen
	 */
	public int getCodCentroCostoXuniAgen() {
		return codCentroCostoXuniAgen;
	}

	/**
	 * @param codCentroCostoXuniAgen the codCentroCostoXuniAgen to set
	 */
	public void setCodCentroCostoXuniAgen(int codCentroCostoXuniAgen) {
		this.codCentroCostoXuniAgen = codCentroCostoXuniAgen;
	}

	/**
	 * @return the evenAgenda
	 */
	public String getEvenAgenda() {
		return evenAgenda;
	}

	/**
	 * @param evenAgenda the evenAgenda to set
	 */
	public void setEvenAgenda(String evenAgenda) {
		this.evenAgenda = evenAgenda;
	}

	/**
	 * @return the horaSubAgendaSel
	 */
	public String getHoraSubAgendaSel() {
		return horaSubAgendaSel;
	}

	/**
	 * @param horaSubAgendaSel the horaSubAgendaSel to set
	 */
	public void setHoraSubAgendaSel(String horaSubAgendaSel) {
		this.horaSubAgendaSel = horaSubAgendaSel;
	}

	/**
	 * @return the reserva
	 */
	public String getReserva() {
		return reserva;
	}

	/**
	 * @param reserva the reserva to set
	 */
	public void setReserva(String reserva) {
		this.reserva = reserva;
	}

	/**
	 * @return the asignar
	 */
	public String getAsignar() {
		return asignar;
	}

	/**
	 * @param asignar the asignar to set
	 */
	public void setAsignar(String asignar) {
		this.asignar = asignar;
	}

	/**
	 * @return the cancelar
	 */
	public String getCancelar() {
		return cancelar;
	}

	/**
	 * @param cancelar the cancelar to set
	 */
	public void setCancelar(String cancelar) {
		this.cancelar = cancelar;
	}

	/**
	 * @return the reprogramar
	 */
	public String getReprogramar() {
		return reprogramar;
	}

	/**
	 * @param reprogramar the reprogramar to set
	 */
	public void setReprogramar(String reprogramar) {
		this.reprogramar = reprogramar;
	}

	/**
	 * @return the cuposExtra
	 */
	public String getCuposExtra() {
		return cuposExtra;
	}

	/**
	 * @param cuposExtra the cuposExtra to set
	 */
	public void setCuposExtra(String cuposExtra) {
		this.cuposExtra = cuposExtra;
	}

	/**
	 * @return the cambiarServicio
	 */
	public String getCambiarServicio() {
		return cambiarServicio;
	}

	/**
	 * @param cambiarServicio the cambiarServicio to set
	 */
	public void setCambiarServicio(String cambiarServicio) {
		this.cambiarServicio = cambiarServicio;
	}

	
	
	public void resetMenu()
	{
		this.reserva = ConstantesBD.acronimoNo;
		this.asignar = ConstantesBD.acronimoNo;
		this.cancelar = ConstantesBD.acronimoNo;
		this.reprogramar = ConstantesBD.acronimoNo;
		this.cuposExtra = ConstantesBD.acronimoNo;
		this.cambiarServicio = ConstantesBD.acronimoNo;
		this.hayLinks = "";
	}

	/**
	 * @return the hayLinks
	 */
	public String getHayLinks() {
		return hayLinks;
	}

	/**
	 * @param hayLinks the hayLinks to set
	 */
	public void setHayLinks(String hayLinks) {
		this.hayLinks = hayLinks;
	}

	/**
	 * @return the urlCuenta
	 */
	public String getUrlCuenta() {
		return urlCuenta;
	}

	/**
	 * @param urlCuenta the urlCuenta to set
	 */
	public void setUrlCuenta(String urlCuenta) {
		this.urlCuenta = urlCuenta;
	}

	/**
	 * @return the minutosEsperaAsgCitOdoCad
	 */
	public String getMinutosEsperaAsgCitOdoCad() {
		return minutosEsperaAsgCitOdoCad;
	}

	/**
	 * @param minutosEsperaAsgCitOdoCad the minutosEsperaAsgCitOdoCad to set
	 */
	public void setMinutosEsperaAsgCitOdoCad(String minutosEsperaAsgCitOdoCad) {
		this.minutosEsperaAsgCitOdoCad = minutosEsperaAsgCitOdoCad;
	}

	/**
	 * @return the posCitaAgen
	 */
	public int getPosCitaAgen() {
		return posCitaAgen;
	}

	/**
	 * @param posCitaAgen the posCitaAgen to set
	 */
	public void setPosCitaAgen(int posCitaAgen) {
		this.posCitaAgen = posCitaAgen;
	}

	/**
	 * @return the codigoCitaAgen
	 */
	public int getCodigoCitaAgen() {
		return codigoCitaAgen;
	}

	/**
	 * @param codigoCitaAgen the codigoCitaAgen to set
	 */
	public void setCodigoCitaAgen(int codigoCitaAgen) {
		this.codigoCitaAgen = codigoCitaAgen;
	}

	/**
	 * @return the citaAgendaSel
	 */
	public DtoCitaOdontologica getCitaAgendaSel() {
		return citaAgendaSel;
	}

	/**
	 * @param citaAgendaSel the citaAgendaSel to set
	 */
	public void setCitaAgendaSel(DtoCitaOdontologica citaAgendaSel) {
		this.citaAgendaSel = citaAgendaSel;
	}
	
	public void resetPacBas()
	{
		this.paciente.setTipoId("");
		this.paciente.setNumeroId("");
		this.paciente.setPrimerApellido("");
		this.paciente.setPrimerNombre("");
		this.paciente.setSegundoApellido("");
		this.paciente.setSegundoNombre("");
		
	}

	/**
	 * @return the listMotivoCancelacion
	 */
	public ArrayList<HashMap> getListMotivoCancelacion() {
		return listMotivoCancelacion;
	}

	/**
	 * @param listMotivoCancelacion the listMotivoCancelacion to set
	 */
	public void setListMotivoCancelacion(ArrayList<HashMap> listMotivoCancelacion) {
		this.listMotivoCancelacion = listMotivoCancelacion;

	}

	/**
	 * @return the codMotivoCancelacion
	 */
	public String getCodMotivoCancelacion() {
		return codMotivoCancelacion;
	}

	/**
	 * @param codMotivoCancelacion the codMotivoCancelacion to set
	 */
	public void setCodMotivoCancelacion(String codMotivoCancelacion) {
		this.codMotivoCancelacion = codMotivoCancelacion;
	}

	/**
	 * @return the fechaReprogramacion
	 */
	public String getFechaReprogramacion() {
		return fechaReprogramacion;
	}

	/**
	 * @param fechaReprogramacion the fechaReprogramacion to set
	 */
	public void setFechaReprogramacion(String fechaReprogramacion) {
		this.fechaReprogramacion = fechaReprogramacion;
	}

	/**
	 * @return the citaCreada
	 */
	public DtoCitaOdontologica getCitaCreada() {
		return citaCreada;
	}

	/**
	 * @param citaCreada the citaCreada to set
	 */
	public void setCitaCreada(DtoCitaOdontologica citaCreada) {
		this.citaCreada = citaCreada;
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
	 * @return the tieneIngresoPaciente
	 */
	public boolean isTieneIngresoPaciente() {
		return tieneIngresoPaciente;
	}

	/**
	 * @param tieneIngresoPaciente the tieneIngresoPaciente to set
	 */
	public void setTieneIngresoPaciente(boolean tieneIngresoPaciente) {
		this.tieneIngresoPaciente = tieneIngresoPaciente;
	}
	
	/**
	 * Método para retornar el número de citas del paciente
	 * @return
	 */
	public int getNumCitasOdonPac()
	{
		return this.citasOdonPac.size();
	}

	/**
	 * @return the todasCitas
	 */
	public ArrayList<DtoCitaOdontologica> getTodasCitas() {
		return todasCitas;
	}

	/**
	 * @param todasCitas the todasCitas to set
	 */
	public void setTodasCitas(ArrayList<DtoCitaOdontologica> todasCitas) {
		this.todasCitas = todasCitas;
	}
	
	/**
	 * Método para retornar el numero de citas
	 * @return
	 */
	public int getNumTodasCitas()
	{
		return this.todasCitas.size();
	}

	/**
	 * @return the codigoActividad
	 */
	public String getCodigoActividad() {
		return codigoActividad;
	}

	/**
	 * @param codigoActividad the codigoActividad to set
	 */
	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	/**
	 * @return the tipoConfirmacion
	 */
	public String getTipoConfirmacion() {
		return tipoConfirmacion;
	}

	/**
	 * @param tipoConfirmacion the tipoConfirmacion to set
	 */
	public void setTipoConfirmacion(String tipoConfirmacion) {
		this.tipoConfirmacion = tipoConfirmacion;
	}

	/**
	 * @return the motivosNoConfirmacion
	 */
	public ArrayList<HashMap<String, Object>> getMotivosNoConfirmacion() {
		return motivosNoConfirmacion;
	}

	/**
	 * @param motivosNoConfirmacion the motivosNoConfirmacion to set
	 */
	public void setMotivosNoConfirmacion(
			ArrayList<HashMap<String, Object>> motivosNoConfirmacion) {
		this.motivosNoConfirmacion = motivosNoConfirmacion;
	}

	/**
	 * @return the saldoActualPaciente
	 */
	public double getSaldoActualPaciente() {
		return saldoActualPaciente;
	}

	/**
	 * @param saldoActualPaciente the saldoActualPaciente to set
	 */
	public void setSaldoActualPaciente(double saldoActualPaciente) {
		this.saldoActualPaciente = saldoActualPaciente;
	}

	/**
	 * @return the observacionesConfirmacion
	 */
	public String getObservacionesConfirmacion() {
		return observacionesConfirmacion;
	}

	/**
	 * @param observacionesConfirmacion the observacionesConfirmacion to set
	 */
	public void setObservacionesConfirmacion(String observacionesConfirmacion) {
		this.observacionesConfirmacion = observacionesConfirmacion;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
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
	 * Método que retorna el número de paises
	 * @return
	 */
	public int getNumPaises()
	{
		return this.paises.size();
	}
	
	/**
	 * Método que retorna el número de ciudades
	 * @return
	 */
	public int getNumCiudades()
	{
		return this.ciudades.size();
	}
	
	/**
	 * Método que retorna el numero de centros de atencion
	 * @return
	 */
	public int getNumCentrosAtencion()
	{
		return this.listCentroAtencion.size();
	}
	
	/**
	 * Método para cargar la unidade de agenda
	 * @return
	 */
	public int getNumUnidadesAgenda()
	{
		return this.listUnidadesAgendaXUsuario.size();
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the tiposCita
	 */
	public ArrayList<InfoDatosString> getTiposCita() {
		return tiposCita;
	}

	/**
	 * @param tiposCita the tiposCita to set
	 */
	public void setTiposCita(ArrayList<InfoDatosString> tiposCita) {
		this.tiposCita = tiposCita;
	}

	/**
	 * @return the estadoCita
	 */
	public String getEstadoCita() {
		return estadoCita;
	}

	/**
	 * @param estadoCita the estadoCita to set
	 */
	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	/**
	 * @return the indicativoConf
	 */
	public String[] getIndicativoConf() {
		return indicativoConf;
	}

	/**
	 * @return the indicativoConf
	 */
	public boolean getIndicativoConfirmacion(String valor) {
		boolean respuesta=false;
		for(int i=0; i<indicativoConf.length; i++)
		{
			if(indicativoConf[i].equals(valor))
			{
				respuesta=true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Limpia el indicativo de confirmación para que se dejen deschekear los elementos
	 * @return
	 */
	public boolean getLimpiarIndicativoConfirmacion()
	{
		String[] indicativoTempo=new String[dtoBusquedaAgendaRango.getIndicativoConf().length];
		for(int i=0; i<dtoBusquedaAgendaRango.getIndicativoConf().length; i++)
		{
			indicativoTempo[i]=dtoBusquedaAgendaRango.getIndicativoConf()[i];
		}
		indicativoConf=indicativoTempo;
		dtoBusquedaAgendaRango.getLimpiarIndicativoConfirmacion();
		return true;
	}

	/**
	 * @param indicativoConf the indicativoConf to set
	 */
	public void setIndicativoConf(String[] indicativoConf) {
		this.indicativoConf = indicativoConf;
	}

	

	/**
	 * @return the totalDuracionServicios
	 */
	public int getTotalDuracionServicios() {
		return totalDuracionServicios;
	}

	/**
	 * @param totalDuracionServicios the totalDuracionServicios to set
	 */
	public void setTotalDuracionServicios(int totalDuracionServicios) {
		this.totalDuracionServicios = totalDuracionServicios;
	}
	
	/**
	 * @return the tipoCancelacion
	 */
	public String getTipoCancelacion() {
		return tipoCancelacion;
	}

	/**
	 * @param tipoCancelacion the tipoCancelacion to set
	 */
	public void setTipoCancelacion(String tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}


	/**
	 * @return the tipoFlujoPaciente
	 */
	public String getTipoFlujoPaciente() {
		return tipoFlujoPaciente;
	}


	/**
	 * @param tipoFlujoPaciente the tipoFlujoPaciente to set
	 */
	public void setTipoFlujoPaciente(String tipoFlujoPaciente) {
		this.tipoFlujoPaciente = tipoFlujoPaciente;
	}


	public String getPorPaciente() {
		return porPaciente;
	}


	public void setPorPaciente(String porPaciente) {
		this.porPaciente = porPaciente;
	}


	public int getCitaSeleccionada() {
		return citaSeleccionada;
	}


	public void setCitaSeleccionada(int citaSeleccionada) {
		this.citaSeleccionada = citaSeleccionada;
	}


	/**
	 * @return the mostrarTarifas
	 */
	public boolean isMostrarTarifas() {
		return mostrarTarifas;
	}


	/**
	 * @param mostrarTarifas the mostrarTarifas to set
	 */
	public void setMostrarTarifas(boolean mostrarTarifas) {
		this.mostrarTarifas = mostrarTarifas;
	}


	/**
	 * @return the abonosDisponiblesPaciente
	 */
	public double getAbonosDisponiblesPaciente() {
		return abonosDisponiblesPaciente;
	}


	/**
	 * @param abonosDisponiblesPaciente the abonosDisponiblesPaciente to set
	 */
	public void setAbonosDisponiblesPaciente(double abonosDisponiblesPaciente) {
		this.abonosDisponiblesPaciente = abonosDisponiblesPaciente;
	}


	/**
	 * @return the responsablesCuenta
	 */
	public ArrayList<DtoSubCuentas> getResponsablesCuenta() {
		return responsablesCuenta;
	}


	/**
	 * @param responsablesCuenta the responsablesCuenta to set
	 */
	public void setResponsablesCuenta(ArrayList<DtoSubCuentas> responsablesCuenta) {
		this.responsablesCuenta = responsablesCuenta;
	}


	/**
	 * @return the valorTotalServicios
	 */
	public double getValorTotalServicios() {
		return valorTotalServicios;
	}


	/**
	 * @param valorTotalServicios the valorTotalServicios to set
	 */
	public void setValorTotalServicios(double valorTotalServicios) {
		this.valorTotalServicios = valorTotalServicios;
	}


	/**
	 * @return the confirmar
	 */
	public String getConfirmar() {
		return confirmar;
	}


	/**
	 * @param confirmar the confirmar to set
	 */
	public void setConfirmar(String confirmar) {
		this.confirmar = confirmar;
	}


	/**
	 * @return the permitirCupoExtra
	 */
	public boolean isPermitirCupoExtra() {
		return permitirCupoExtra;
	}


	/**
	 * @param permitirCupoExtra the permitirCupoExtra to set
	 */
	public void setPermitirCupoExtra(boolean permitirCupoExtra) {
		this.permitirCupoExtra = permitirCupoExtra;
	}


	/**
	 * @return the listaProximasCitasCancelar
	 */
	public ArrayList<DtoCitaOdontologica> getListaProximasCitasCancelar() {
		return listaProximasCitasCancelar;
	}


	/**
	 * @param listaProximasCitasCancelar the listaProximasCitasCancelar to set
	 */
	public void setListaProximasCitasCancelar(
			ArrayList<DtoCitaOdontologica> listaProximasCitasCancelar) {
		this.listaProximasCitasCancelar = listaProximasCitasCancelar;
	}


	public int getNumListaProximasCitas() {
		return this.listaProximasCitasCancelar.size();
	}


	public void setNumListaProximasCitas(int numListaProximasCitas) {
		this.numListaProximasCitas = numListaProximasCitas;
	}


	/**
	 * @return the habilitarCalcTarifa
	 */
	public String getHabilitarCalcTarifa() {
		return habilitarCalcTarifa;
	}


	/**
	 * @param habilitarCalcTarifa the habilitarCalcTarifa to set
	 */
	public void setHabilitarCalcTarifa(String habilitarCalcTarifa) {
		this.habilitarCalcTarifa = habilitarCalcTarifa;
	}


	/**
	 * @return the codCuentaPaciente
	 */
	public int getCodCuentaPaciente() {
		return codCuentaPaciente;
	}


	/**
	 * @param codCuentaPaciente the codCuentaPaciente to set
	 */
	public void setCodCuentaPaciente(int codCuentaPaciente) {
		this.codCuentaPaciente = codCuentaPaciente;
	}


	public void setCitaConfirmacion(DtoCitaOdontologica citaConfirmacion) {
		this.citaConfirmacion = citaConfirmacion;
	}


	public DtoCitaOdontologica getCitaConfirmacion() {
		return citaConfirmacion;
	}


	public void setSaldoTotalServcios(String saldoTotalServcios) {
		this.saldoTotalServcios = saldoTotalServcios;
	}


	public String getSaldoTotalServcios() {
		return saldoTotalServcios;
	}


	public ArrayList<DtoSeccionConvenioPaciente> getListaConveniosPaciente() {
		return listaConveniosPaciente;
	}


	public void setListaConveniosPaciente(
			ArrayList<DtoSeccionConvenioPaciente> listaConveniosPaciente) {
		this.listaConveniosPaciente = listaConveniosPaciente;
	}


	public ArrayList<Contratos> getListaContratoConvenio() {
		return listaContratoConvenio;
	}


	public void setListaContratoConvenio(ArrayList<Contratos> listaContratoConvenio) {
		this.listaContratoConvenio = listaContratoConvenio;
	}


	public ArrayList<DtoCentroCostosVista> getListaAreas() {
		return listaAreas;
	}


	public void setListaAreas(ArrayList<DtoCentroCostosVista> listaAreas) {
		this.listaAreas = listaAreas;
	}


	public String getCodigoArea() {
		return codigoArea;
	}


	public void setCodigoArea(String codigoArea) {
		this.codigoArea = codigoArea;
	}

	public void setCancelandoCitaOdoEstadoReprogramar(
			String cancelandoCitaOdoEstadoReprogramar) {
		this.cancelandoCitaOdoEstadoReprogramar = cancelandoCitaOdoEstadoReprogramar;
	}


	public String getCancelandoCitaOdoEstadoReprogramar() {
		return cancelandoCitaOdoEstadoReprogramar;
	}


	public void setReprogramaCita(String reprogramaCita) {
		this.reprogramaCita = reprogramaCita;
	}


	public String getReprogramaCita() {
		return reprogramaCita;
	}


	public void setSeleccionTipoCancelacion(boolean seleccionTipoCancelacion) {
		this.seleccionTipoCancelacion = seleccionTipoCancelacion;
	}
	
	 /**
	 * @param mostrarDesplazarFecha the mostrarDesplazarFecha to set
	 */
	public void setMostrarDesplazarFecha(boolean mostrarDesplazarFecha) {
		this.mostrarDesplazarFecha = mostrarDesplazarFecha;
	}


	/**
	 * @return the mostrarDesplazarFecha
	 */
	public boolean isMostrarDesplazarFecha() {
		return mostrarDesplazarFecha;
	}


	public boolean isSeleccionTipoCancelacion() {
		return seleccionTipoCancelacion;
	}


	public void setSeleccionMotivoCancelacion(boolean seleccionMotivoCancelacion) {
		this.seleccionMotivoCancelacion = seleccionMotivoCancelacion;
	}


	public boolean isSeleccionMotivoCancelacion() {
		return seleccionMotivoCancelacion;
	}


	/**
	 * @return the inhabilitaPais
	 */
	public boolean isInhabilitaPais() {
		return inhabilitaPais;
	}


	/**
	 * @param inhabilitaPais the inhabilitaPais to set
	 */
	public void setInhabilitaPais(boolean inhabilitaPais) {
		this.inhabilitaPais = inhabilitaPais;
	}


	/**
	 * @return the inhabilitaCiudad
	 */
	public boolean isInhabilitaCiudad() {
		return inhabilitaCiudad;
	}


	/**
	 * @param inhabilitaCiudad the inhabilitaCiudad to set
	 */
	public void setInhabilitaCiudad(boolean inhabilitaCiudad) {
		this.inhabilitaCiudad = inhabilitaCiudad;
	}


	/**
	 * @return the inhabilitaCentroAtencion
	 */
	public boolean isInhabilitaCentroAtencion() {
		return inhabilitaCentroAtencion;
	}


	/**
	 * @param inhabilitaCentroAtencion the inhabilitaCentroAtencion to set
	 */
	public void setInhabilitaCentroAtencion(boolean inhabilitaCentroAtencion) {
		this.inhabilitaCentroAtencion = inhabilitaCentroAtencion;
	}


	/**
	 * @return the inhabilitaTipoCita
	 */
	public boolean isInhabilitaTipoCita() {
		return inhabilitaTipoCita;
	}


	/**
	 * @param inhabilitaTipoCita the inhabilitaTipoCita to set
	 */
	public void setInhabilitaTipoCita(boolean inhabilitaTipoCita) {
		this.inhabilitaTipoCita = inhabilitaTipoCita;
	}

	/**
	 * @return the inhabilitaUnidadAgenda
	 */
	public boolean isInhabilitaUnidadAgenda() {
		return inhabilitaUnidadAgenda;
	}


	/**
	 * @param inhabilitaUnidadAgenda the inhabilitaUnidadAgenda to set
	 */
	public void setInhabilitaUnidadAgenda(boolean inhabilitaUnidadAgenda) {
		this.inhabilitaUnidadAgenda = inhabilitaUnidadAgenda;
	}

	
	public boolean isMostrarSeccionconvenio() {
		return mostrarSeccionconvenio;
	}


	public void setMostrarSeccionconvenio(boolean mostrarSeccionconvenio) {
		this.mostrarSeccionconvenio = mostrarSeccionconvenio;
	}
	
	
	

	public boolean isConvenioManejaBono() {
		return convenioManejaBono;
	}


	public void setConvenioManejaBono(boolean convenioManejaBono) {
		this.convenioManejaBono = convenioManejaBono;
	}


	public boolean isConvenioManejaBonoRequerido() {
		return convenioManejaBonoRequerido;
	}


	public void setConvenioManejaBonoRequerido(boolean convenioManejaBonoRequerido) {
		this.convenioManejaBonoRequerido = convenioManejaBonoRequerido;
	}


	/**
	 * @param numDigCaptNumIdPac the numDigCaptNumIdPac to set
	 */
	public void setNumDigCaptNumIdPac(int numDigCaptNumIdPac) {
		this.numDigCaptNumIdPac = numDigCaptNumIdPac;
	}


	/**
	 * @return the numDigCaptNumIdPac
	 */
	public int getNumDigCaptNumIdPac() {
		return numDigCaptNumIdPac;
	}


	/**
	 * @param inhabilitaOtrosServicios the inhabilitaOtrosServicios to set
	 */
	public void setInhabilitaOtrosServicios(boolean inhabilitaOtrosServicios) {
		this.inhabilitaOtrosServicios = inhabilitaOtrosServicios;
	}


	/**
	 * @return the inhabilitaOtrosServicios
	 */
	public boolean isInhabilitaOtrosServicios() {
		return inhabilitaOtrosServicios;
	}


	public double getValorPorAbonar() {
		return valorPorAbonar;
	}
	
	public void setValorPorAbonar(double valorPorAbonar) {
		

		if (valorPorAbonar<0) {
			this.valorPorAbonar = ConstantesBD.codigoNuncaValidoDouble;
		}else{
			this.valorPorAbonar = valorPorAbonar;
		}
	}


	/**
	 * @param existenRegistroServicios the existenRegistroServicios to set
	 */
	public void setExistenRegistroServicios(boolean existenRegistroServicios) {
		this.existenRegistroServicios = existenRegistroServicios;
	}


	/**
	 * @return the existenRegistroServicios
	 */
	public boolean isExistenRegistroServicios() {
		return existenRegistroServicios;
	}


	/**
	 * @param otrosServiciosOdon the otrosServiciosOdon to set
	 */
	public void setOtrosServiciosOdon(ArrayList<DtoServicioOdontologico> otrosServiciosOdon) {
		this.otrosServiciosOdon = otrosServiciosOdon;
	}


	/**
	 * @return the otrosServiciosOdon
	 */
	public ArrayList<DtoServicioOdontologico> getOtrosServiciosOdon() {
		return otrosServiciosOdon;
	}


	/**
	 * @param inhabilitaLinkRecibosCaja the inhabilitaLinkRecibosCaja to set
	 */
	public void setInhabilitaLinkRecibosCaja(boolean inhabilitaLinkRecibosCaja) {
		this.inhabilitaLinkRecibosCaja = inhabilitaLinkRecibosCaja;
	}


	/**
	 * @return the inhabilitaLinkRecibosCaja
	 */
	public boolean isInhabilitaLinkRecibosCaja() {
		return inhabilitaLinkRecibosCaja;
	}


	public boolean isMostrarTarifaServicios() {
		return mostrarTarifaServicios;
	}


	public void setMostrarTarifaServicios(boolean mostrarTarifaServicios) {
		this.mostrarTarifaServicios = mostrarTarifaServicios;
	}


	public boolean isPasoValidacionesGuardar() {
		return pasoValidacionesGuardar;
	}


	public void setPasoValidacionesGuardar(boolean pasoValidacionesGuardar) {
		this.pasoValidacionesGuardar = pasoValidacionesGuardar;
	}


	public boolean isMostrarValorTotalCita() {
		return mostrarValorTotalCita;
	}


	public void setMostrarValorTotalCita(boolean mostrarValorTotalCita) {
		this.mostrarValorTotalCita = mostrarValorTotalCita;
	}


	/**
	 * @param permiteModificarDuracionServicio the permiteModificarDuracionServicio to set
	 */
	public void setPermiteModificarDuracionServicio(
			boolean permiteModificarDuracionServicio) {
		this.permiteModificarDuracionServicio = permiteModificarDuracionServicio;
	}


	/**
	 * @return the permiteModificarDuracionServicio
	 */
	public boolean isPermiteModificarDuracionServicio() {
		return permiteModificarDuracionServicio;
	}


	/**
	 * @param urlRecibosCaja the urlRecibosCaja to set
	 */
	public void setUrlRecibosCaja(String urlRecibosCaja) {
		this.urlRecibosCaja = urlRecibosCaja;
	}


	/**
	 * @return the urlRecibosCaja
	 */
	public String getUrlRecibosCaja() {
		return urlRecibosCaja;
	}


	/**
	 * @param nombreArchivoImpresionCita the nombreArchivoImpresionCita to set
	 */
	public void setNombreArchivoImpresionCita(String nombreArchivoImpresionCita) {
		this.nombreArchivoImpresionCita = nombreArchivoImpresionCita;
	}


	/**
	 * @return the nombreArchivoImpresionCita
	 */
	public String getNombreArchivoImpresionCita() {
		return nombreArchivoImpresionCita;
	}


	public int getCodigoCitaGuardada() {
		return codigoCitaGuardada;
	}


	public void setCodigoCitaGuardada(int codigoCitaGuardada) {
		this.codigoCitaGuardada = codigoCitaGuardada;
	}


	public int getCodigoCitaRecord() {
		return codigoCitaRecord;
	}


	public void setCodigoCitaRecord(int codigoCitaRecord) {
		this.codigoCitaRecord = codigoCitaRecord;
	}


	public String getEstadoCitaRecord() {
		return estadoCitaRecord;
	}


	public void setEstadoCitaRecord(String estadoCitaRecord) {
		this.estadoCitaRecord = estadoCitaRecord;
	}


	/**
	 * @param paginaOrigen the paginaOrigen to set
	 */
	public void setPaginaOrigen(String paginaOrigen) {
		this.paginaOrigen = paginaOrigen;
	}


	/**
	 * @return the paginaOrigen
	 */
	public String getPaginaOrigen() {
		return paginaOrigen;
	}


	/**
	 * @param casoCita the casoCita to set
	 */
	public void setCasoCita(String casoCita) {
		this.casoCita = casoCita;
	}


	/**
	 * @return the casoCita
	 */
	public String getCasoCita() {
		return casoCita;
	}


	/**
	 * @param recargarCriteriosBusqueda the recargarCriteriosBusqueda to set
	 */
	public void setRecargarCriteriosBusqueda(String recargarCriteriosBusqueda) {
		this.recargarCriteriosBusqueda = recargarCriteriosBusqueda;
	}


	/**
	 * @return the recargarCriteriosBusqueda
	 */
	public String getRecargarCriteriosBusqueda() {
		return recargarCriteriosBusqueda;
	}


	/**
	 * @param tienePlanTratamientoInactivo the tienePlanTratamientoInactivo to set
	 */
	public void setTienePlanTratamientoInactivo(boolean tienePlanTratamientoInactivo) {
		this.tienePlanTratamientoInactivo = tienePlanTratamientoInactivo;
	}


	/**
	 * @return the tienePlanTratamientoInactivo
	 */
	public boolean isTienePlanTratamientoInactivo() {
		return tienePlanTratamientoInactivo;
	}

	/**
	 * @return the dtoBusquedaAgendaRango
	 */
	public DtoBusquedaAgendaRango getDtoBusquedaAgendaRango() {
		return dtoBusquedaAgendaRango;
	}


	/**
	 * @param dtoBusquedaAgendaRango the dtoBusquedaAgendaRango to set
	 */
	public void setDtoBusquedaAgendaRango(
			DtoBusquedaAgendaRango dtoBusquedaAgendaRango) {
		this.dtoBusquedaAgendaRango = dtoBusquedaAgendaRango;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  indiceCitaSeleccionada
	 *
	 * @return retorna la variable indiceCitaSeleccionada
	 */
	public int getIndiceCitaSeleccionada() {
		return indiceCitaSeleccionada;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo indiceCitaSeleccionada
	 * @param indiceCitaSeleccionada es el valor para el atributo indiceCitaSeleccionada 
	 */
	public void setIndiceCitaSeleccionada(int indiceCitaSeleccionada) {
		this.indiceCitaSeleccionada = indiceCitaSeleccionada;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citaSeleccionadaHistorico
	 *
	 * @return retorna la variable citaSeleccionadaHistorico
	 */
	public DtoCitaOdontologica getCitaSeleccionadaHistorico() {
		return citaSeleccionadaHistorico;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citaSeleccionadaHistorico
	 * @param citaSeleccionadaHistorico es el valor para el atributo citaSeleccionadaHistorico 
	 */
	public void setCitaSeleccionadaHistorico(
			DtoCitaOdontologica citaSeleccionadaHistorico) {
		this.citaSeleccionadaHistorico = citaSeleccionadaHistorico;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  historicoCita
	 *
	 * @return retorna la variable historicoCita
	 */
	public ArrayList<DtoCitaOdontologica> getHistoricoCita() {
		return historicoCita;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo historicoCita
	 * @param historicoCita es el valor para el atributo historicoCita 
	 */
	public void setHistoricoCita(ArrayList<DtoCitaOdontologica> historicoCita) {
		this.historicoCita = historicoCita;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  mostrarTipoCancelacion
	 *
	 * @return retorna la variable mostrarTipoCancelacion
	 */
	public String getMostrarTipoCancelacion() {
		
		if (this.dtoBusquedaAgendaRango.getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar) ||
				this.dtoBusquedaAgendaRango.getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) {
			this.mostrarTipoCancelacion = ConstantesBD.acronimoSi;
		}else{
			this.mostrarTipoCancelacion = ConstantesBD.acronimoNo;
		}
		
		return mostrarTipoCancelacion;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo mostrarTipoCancelacion
	 * @param mostrarTipoCancelacion es el valor para el atributo mostrarTipoCancelacion 
	 */
	public void setMostrarTipoCancelacion(String mostrarTipoCancelacion) {
		this.mostrarTipoCancelacion = mostrarTipoCancelacion;
	}


	public boolean isBloquearClic() {
		return bloquearClic;
	}


	public void setBloquearClic(boolean bloquearClic) {
		this.bloquearClic = bloquearClic;
	}


	public double getValorTotalServiciosAbono() {
		return valorTotalServiciosAbono;
	}


	public void setValorTotalServiciosAbono(double valorTotalServiciosAbono) {
		this.valorTotalServiciosAbono = valorTotalServiciosAbono;
	}
	

//	/**
//	 * @param confirmacionCupoExtra the confirmacionCupoExtra to set
//	 */
//	public void setConfirmacionCupoExtra(boolean confirmacionCupoExtra) {
//		this.confirmacionCupoExtra = confirmacionCupoExtra;
//	}
//
//
//	/**
//	 * @return the confirmacionCupoExtra
//	 */
//	public boolean isConfirmacionCupoExtra() {
//		return confirmacionCupoExtra;
//	}

}

