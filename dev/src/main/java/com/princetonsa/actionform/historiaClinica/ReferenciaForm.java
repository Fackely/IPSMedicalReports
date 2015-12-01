/*
 * Mayo 15, 2007
 */
package com.princetonsa.actionform.historiaClinica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Referencia
 */
public class ReferenciaForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ReferenciaForm.class);
	
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Tipo de Identificacion del paciente al cual se la va a ingresar la referencia
	 */
	private String tipoIdentificacion;
	
	/**
	 * Código del tipo de identificación
	 */
	private String codigoTipoIdentificacion;
	
	/**
	 * Número de identificacion del paciente al cual se le va a ingresar la referencia
	 */
	private String numeroIdentificacion;
	
	/**
	 * Selección del tipo de referencia
	 */
	private String tipoReferencia;
	
	/**
	 * Campo que almacena el codigo de la institucion
	 */
	private String institucion;
	
	/**
	 * Campo qu eindica la posicion de un registro de algun mapa
	 */
	private int posicion;
	
	/**
	 * Variable que contiene la lista separada por , de los servicios SIRC insertados
	 * su formato es pareja servicioSirc-servicioAxioma
	 */
	private String serviciosInsertados;
	
	/**
	 * Variable que contiene la lista de solicitudes de procedimientos separadas por , de los
	 * resultados examenes diagnosticos ya insertados
	 */
	private String procedimientosInsertados;
	
	//***********ATRIBUTOS QUE INDICAN EL ESTADO DE LAS SECCIONES***********************
	private boolean seccionInstitucionReferencia;
	private boolean seccionAfiliacionSistema;
	private boolean seccionProcedimientosSolicitados;
	private boolean seccionHistoriaClinica;
	
	
	
	//*************ARREGLOS Y VARIABLES PARA LA SELECCION DE DATOS***************************
	/**
	 * Vector donde se guardan los tipos de identificacion
	 */
	private ArrayList tiposIdentificacion = new ArrayList();
	/**
	 * Vector donde se guardan las ciudades
	 */
	private ArrayList ciudades = new ArrayList();
	
	
	private String pais;
	
	/**
	 * Mapa donde se guardan las especialidades
	 */
	private HashMap especialidades = new HashMap();
	/**
	 * Mapa donde se guardan los motivos SIRC
	 */
	private HashMap motivos = new HashMap();
	/**
	 * Vector donde se guardan los convenios
	 */
	private ArrayList convenios = new ArrayList();
	/**
	 * Código del convenio seleccionado
	 */
	private int codigoConvenio;
	/**
	 * Mapa donde se guardan los estratos sociales del convenio
	 */
	private HashMap estratos = new HashMap();
	/**
	 * Mapa donde se guardan los estados de conciencia
	 */
	private HashMap estadosConciencia = new HashMap();
	/**
	 * Mapa donde se guardan los tipos de ambulancia
	 */
	private HashMap tiposAmbulancia = new HashMap();
	/**
	 * Mapa donde se guardan las camas
	 */
	private HashMap camas = new HashMap();
	/**
	 * Vector donde se guardan las naturalezas del paciente
	 */
	private Vector<InfoDatosString> naturalezasPaciente = new Vector<InfoDatosString>();
	
	//***************************************************************************
	
	/**
	 * Variable que almacena el ingreso de la cuenta en el caso de que lo tenga
	 */
	private int idIngreso;
	
	/**
	 * Variable que se usa para saber cual via de ingreso tiene el paciente en el 
	 * caso de que tenga una cuenta abierta. Se utiliza para hacer la validacion
	 * de la reserva de cama en la referencia externa
	 */
	private int viaIngreso;
	
	/**
	 * Variables donde se almacena la fecha/hora de ingreso en el caso de que el paciente tenga
	 * una cuenta abierta
	 */
	private String fechaIngreso;
	private String horaIngreso;
	
	/**
	 * Código del area del paciente en el caso de que tenga cuenta abierta
	 */
	private int codigoArea;
	
	/**
	 * Consecutivo Disponible para la referencia interna
	 */
	private int consecutivoInt;
	private String consecutivoAnio; 
	
	/**
	 * Objeto donde se almacena toda la información de referencia
	 */
	private HashMap referencia = new HashMap();
	
	//***********SUBMAPAS QUE SE ADHIEREN AL MAPA DE REFERENCIA***************************
	private HashMap mapaServicios = new HashMap();
	private HashMap mapaServiciosEliminados = new HashMap();
	
	private HashMap mapaSignosVitales = new HashMap();
	
	private HashMap mapaResultados = new HashMap();
	private HashMap mapaResultadosEliminados = new HashMap();
	
	private HashMap mapaDiagnosticos = new HashMap();
	//**************************************************************************************
	
	/**
	 * Objeto donde se alamcenan las instituciones Sirc para seleccinar del popUp
	 */
	private HashMap institucionesSirc = new HashMap();
	/**
	 * Objeto donde se almacenan los servicios Sirc para seleccionar del popUp
	 */
	private HashMap serviciosSirc = new HashMap();
	/**
	 * Objeto donde se almacenan los procedimientos para seleccionar del popUp
	 */
	private HashMap procedimientos = new HashMap();
	
	//**********ATRIBUTO PARA PAGINACION Y ORDENACION*****************************
	private String linkSiguiente;
	private int offset;
	private String indice;
	private String ultimoIndice;
	private int maxPageItems;
	
	
	/**
	 * Indicativo para saber si después de terminar el proceso de la referncia
	 * se llama a la funcionalidad de asignación de citas (solo aplica para creación de cuentas de consulta externa)
	 */
	private boolean deboAbrirAsignacionCita;
	private String pathFuncAsignacionCita;
	
	/**
	 * Variable que me indica si debo filtrar el tipo de referencia
	 */
	private boolean filtrarTipoReferencia;
	
	//**************************************************************************
	
	/**
	 * Variable usada para almacenar el numero de referencia
	 * (Se usa en Historia de Atenciones)
	 */
	private String numeroReferencia = "";
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.tipoIdentificacion = "";
		this.codigoTipoIdentificacion = "";
		this.numeroIdentificacion = "";
		this.tipoReferencia = "";
		this.institucion = "";
		this.posicion = 0;
		this.serviciosInsertados = "";
		this.procedimientosInsertados = "";
		
		this.tiposIdentificacion = new ArrayList();
		this.ciudades = new ArrayList();
		this.pais="";
		this.especialidades = new HashMap();
		this.idIngreso = 0;
		this.viaIngreso = 0;
		this.fechaIngreso = "";
		this.horaIngreso = "";
		this.codigoArea = 0;
		this.consecutivoInt = 0;
		this.consecutivoAnio = "";
		this.referencia = new HashMap();
		this.motivos = new HashMap();
		this.convenios = new ArrayList();
		this.codigoConvenio = 0;
		this.estratos = new HashMap();
		this.estadosConciencia = new HashMap();
		this.tiposAmbulancia = new HashMap();
		this.camas = new HashMap();
		this.naturalezasPaciente = new Vector<InfoDatosString>();
		
		//******submapas del mapa referencia***********
		this.mapaServicios = new HashMap();
		this.setMapaServicios("numRegistros", "0");
		this.mapaServiciosEliminados = new HashMap();
		this.setMapaServiciosEliminados("numRegistros", "0");
		this.mapaSignosVitales = new HashMap();
		this.setMapaSignosVitales("numRegistros", "0");
		this.mapaResultados = new HashMap();
		this.setMapaResultados("numRegistros", "0");
		this.mapaResultadosEliminados = new HashMap();
		this.setMapaResultadosEliminados("numRegistros", "0");
		this.mapaDiagnosticos = new HashMap();
		this.setMapaDiagnosticos("numRegistros", "0");
		
		this.institucionesSirc = new HashMap();
		this.serviciosSirc = new HashMap();
		this.procedimientos = new HashMap();
		
		//*********atributos para la ordenacion y paginacion********
		this.linkSiguiente = "";
		this.offset = 0;
		this.indice = "";
		this.ultimoIndice = "";
		this.maxPageItems = 10;
		
		//******atributos que identifican el estado de las secciones*******
		this.seccionInstitucionReferencia = true;
		this.seccionAfiliacionSistema = false;
		this.seccionProcedimientosSolicitados = false;
		this.seccionHistoriaClinica = false;
		
		//stributos específicos para el llamado de la funcionalidad de asignacion de citas
		this.deboAbrirAsignacionCita = false;
		this.pathFuncAsignacionCita = "";
		
		this.filtrarTipoReferencia = false;
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
		
		if(this.estado.equals("guardar"))
		{
		
			//**************SECCION INSTITUCION REFERENCIA**********************************************+++
			errores = this.validacionesSeccionInstitucionReferencia(errores);
			//*********************************************************************************************
			
			//***************SECCION AFILIACIOIN AL S.G.S.S. *******************************************
			errores = this.validacionesSeccionAfiliacionSGSS(errores);
			//***************************************************************************************
			
			//***************SECCION PROCEDIMIENTOS SOLICITADOS SIRC********************************
			if(Integer.parseInt(this.getMapaServicios("numRegistros").toString())<=0)
				errores.add("Mínimo un procedimiento es requerido", new ActionMessage("errors.minimoCampos2","ingresar un servicio (sección Procedimientos Solicitados SIRC)","guardar la información de referencia"));
			//**************************************************************************************
			
			//**************SECCION HISTORIA CLÍNICA**********************************************
			errores = this.validacionesSeccionHistoriaClinica(errores);
			//************************************************************************************
			
			//*************SECCION DATOS DEL TRANSPORTE EN QUE LLEGA EL PACIENTE*********************
			errores = this.validacionesDatosTransportePaciente(errores);
			//****************************************************************************************
			
			//*************SECCION TRASLADO PACIENTE***********************************************
			errores = this.validacionesTrasladoPaciente(errores);
			//********************************************************************************************
			
			//**********SECCION RESERVAR CAMAS*********************************************************
			errores = this.validacionesReservarCama(errores);
			//******************************************************************************************
			
			//*********SECCION ANULACION REFERENCIA***************************************************
			errores = this.validacionesAnularReferencia(errores);
			//****************************************************************************************
			
			if(!errores.isEmpty())
				this.estado = "empezar";
				
		}
		
		
		return errores;
	}

	/**
	 * Método implementado para realizar las validaciones de la anulacion de referencia
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesAnularReferencia(ActionErrors errores) 
	{
		if(UtilidadTexto.getBoolean(this.getReferencia("anulacion").toString()))
		{
			//Motivo Anulacion --------------------------------------------------
			if(this.getReferencia("motivoAnulacion").toString().equals(""))
				errores.add("el motivo de anulacion es requerido",new ActionMessage("errors.required","El motivo de anulación (Sección Anular Referencia)"));
		}
		
		return errores;
	}
	
	
	/**
	 * Método que realiza las validaciones de la seccion reservar cama
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesReservarCama(ActionErrors errores) 
	{
		//Solo aplica para externa
		if(this.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna)&&
			this.viaIngreso!=ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			String aux = this.getReferencia("requiereReservarCama").toString();
			if(aux.equals(""))
				errores.add("requiere reservar cama es requierido",new ActionMessage("errors.required","Requiere reservar cama (sección Reservar Cama)"));
			
		}
		return errores;
	}


	/**
	 * Método que realiza las validaciones de la seccion traslado paciente
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesTrasladoPaciente(ActionErrors errores) 
	{
		String aux = "";
		//Solo aplica para interna
		if(this.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			//Requiere reservar cama ---------------------------------------------------
			aux = this.getReferencia("requiereAmbulancia").toString();
			if(aux.equals(""))
				errores.add("requiere ambulancia es requerida", new ActionMessage("errors.required","Requiere ambulancia (sección Traslado Paciente)"));
			else
			{
				//Si requiere reservar cama es Sí el tipo de ambulancia es requerido
				if(UtilidadTexto.getBoolean(aux))
				{
					if(this.getReferencia("codigoTipoAmbulancia").toString().equals(""))
						errores.add("el tipo de ambulancia es requerida", new ActionMessage("errors.required","Tipo de ambulancia (sección Traslado Paciente)"));
				}
			}
		}
		return errores;
	}

	/**
	 * Método que realiza las validaciones de la seccion de datos transporte en que llega el paciente
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesDatosTransportePaciente(ActionErrors errores) 
	{
		///Solo aplica para externa
		if(this.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			//Si se digitó responsable del traslado los demas campos son requeridos
			if(!this.getReferencia("responsableTraslado").toString().equals(""))
			{
				//Número Movil ------------------------------------------------------------------------------
				if(this.getReferencia("numeroMovilTraslado").toString().equals(""))
					errores.add("el número del movil es requerido", new ActionMessage("errors.required","el número del móvil (sección Datos del transporte en que llega el paciente)"));
				
				//Placa ----------------------------------------------------------------------------------
				if(this.getReferencia("placaTraslado").toString().equals(""))
					errores.add("la placa es requerida", new ActionMessage("errors.required","la placa (sección Datos del transporte en que llega el paciente)"));
				
			}
		}
		
		return errores;
	}

	/**
	 * Método que realiza las validaciones de la seccion de Historia Clínica
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesSeccionHistoriaClinica(ActionErrors errores) 
	{
		String aux = "";

		/* Deshabilitados por Tarea 67195
		//Edad Gestacional ----------------------------------------------------------------
		//si se digitó edad gestacional se debe validar
		aux = this.getReferencia("edadGestacional").toString();
		if(!aux.equals(""))
		{
			int edadGestacional = Integer.parseInt(aux);
			if(edadGestacional<0||edadGestacional>45)
				errores.add("Edad Gestacional fuera del rango",new ActionMessage("errors.range","La edad gestacional (sección Historia Clínica)","0","45"));
		}
		Anamnesis ------------------------------------------------------------------------
		if(this.getReferencia("anamnesis").toString().equals(""))
			errores.add("anamnesis es requerido", new ActionMessage("errors.required","Anamnesis (sección Historia Clínica)"));
		
		Antecedentes -----------------------------------------------------------
		if(this.getReferencia("antecedentes").toString().equals(""))
			errores.add("antecedentes es requerido", new ActionMessage("errors.required","Antecedentes (sección Historia Clínica)"));
		
		Examen Fisico --------------------------------------------------
		if(this.getReferencia("examenFisico").toString().equals(""))
			errores.add("examen fisico es requerido", new ActionMessage("errors.required","Examen físico (sección Historia Clínica)"));
		
		//Paciente con oxígeno -----------------------------------------
		aux = this.getReferencia("pacienteConOxigeno").toString();
		if(aux.equals(""))
			errores.add("paciente con oxigeno es requerido", new ActionMessage("errors.required","Paciente con oxígeno (sección Historia Clínica)"));
		else {
			//Si el paciente con oxigeno es Sí se valida la saturacion de oxigeno
			if(UtilidadTexto.getBoolean(aux))
			{
				//Saturacion Oxigeno-------------------------------------------------
				if(this.getReferencia("saturacionOxigeno").toString().equals(""))
					errores.add("saturacion oxigeno es requerido", new ActionMessage("errors.required","Saturación oxígeno (sección Historia Clínica)"));
			}
		}
		*/
		
		Utilidades.imprimirMapa(this.getMapaDiagnosticos());
		Utilidades.imprimirMapa(this.getReferencia());
		
		//Diagnostico Principal -----------------------------------------------------------------------------
		if(this.getMapaDiagnosticos("valorDiagnosticoPrincipal").toString().equals(""))
			errores.add("diagnostico principal es requerido", new ActionMessage("errors.required","El diagnóstico principal (sección Historia Clínica)"));


		//Add por tarea 67195
		//Resumen Historia Clinica -----------------------------------------------------------------------------
		if(!UtilidadCadena.noEsVacio(this.getReferencia("resumenHistoriaClinica").toString()))
			errores.add("resumen historia clinica es requerido", new ActionMessage("errors.required","El Resumen de la Historia Clinica (sección Historia Clínica)"));
		
		
		return errores;
	}

	/**
	 * Método que realiza las validaciones de la seccion de afiliacion al SGSS
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesSeccionAfiliacionSGSS(ActionErrors errores) {
		///Convenio -----------------------------------------------------------------------------------------
		if(this.getReferencia("codigoConvenio").toString().equals(""))
			errores.add("convenio es requerido",new ActionMessage("errors.required","El convenio (Sección Afiliación al S.G.S.S)"));
		
		//Clasificacion socio-económica
		if(this.getReferencia("codigoEstratoSocial").toString().equals(""))
			errores.add("estrato social es requerido", new ActionMessage("errors.required","La clasificación socio-económica (Sección Afiliación al S.G.S.S)"));
		
		return errores;
	}

	/**
	 * Método que realiza las validaciones de la seccion de institucion de referencia
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesSeccionInstitucionReferencia(ActionErrors errores) 
	{
		String aux = "";
		boolean fechaValida = true;
		
		//Institucion Solicita---------------------------------------------------------------
		if(this.getReferencia("codigoSirc").toString().equals(""))
			errores.add("La institucion que solicita es requerida",new ActionMessage("errors.required","La institución que solicita (Sección Institución Referencia)"));
		
		//Consecutivo punto Atencion-----------------------------------------------------
		if(this.getReferencia("consecutivoPuntoAtencion").toString().equals(""))
			errores.add("El consecutivo punto atencion requerido",new ActionMessage("errors.required","El consecutivo de punto de atención (Sección Institución Referencia)"));
		else
		{
			//Se verifica que sea un número entero
			try
			{
				Integer.parseInt(this.getReferencia("consecutivoPuntoAtencion").toString());
			}
			catch(Exception e)
			{
				errores.add("Consecutivo debe ser número entero",new ActionMessage("errors.integer","El consecutivo de punto de atención (Sección Institución Referencia)"));
			}
		}
		logger.info("CODIGO DEL PAIS=>*"+this.getReferencia("codigoPais")+"*");
		logger.info("CODIGO ciudad=>*"+this.getReferencia("codigoCiudad")+"*");
		if(this.getReferencia("codigoPais").toString().equals(" "))
			errores.add("Pais solicita requerido",new ActionMessage("errors.required","El pais solicita (Sección Institución Referencia)"));
		//Ciudad Solicita-------------------------------------------------------------------------
		if(this.getReferencia("codigoCiudad").toString().equals(" "+ConstantesBD.separadorSplit+" "+ConstantesBD.separadorSplit+" "))
			errores.add("ciudad solicita requerida",new ActionMessage("errors.required","La ciudad solicita (Sección Institución Referencia)"));
		
		//Fecha Referencia-------------------------------------------------------------------------------
		aux = this.getReferencia("fechaReferencia").toString();
		if(aux.equals(""))
		{	
			errores.add("fecha referencia requerida",new ActionMessage("errors.required","La fecha de referencia (Sección Institución Referencia)"));
			fechaValida = false;
		}
		else
		{
			if(!UtilidadFecha.validarFecha(aux))
			{
				errores.add("fecha referencia invalida",new ActionMessage("errors.formatoFechaInvalido","de referencia (Sección Institución Referencia)"));
				fechaValida = false;
			}
			else
			{
				if(this.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
				{
					//debe ser menor o igual a la fecha del sistema
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(aux, UtilidadFecha.getFechaActual()))
					{
						errores.add("fecha referencia mayor a fecha actual",new ActionMessage("errors.fechaPosteriorIgualActual","de referencia (Sección Institución Referencia)","actual"));
						fechaValida = false;
					}
					//debe ser mayor o igual a la fecha del ingreso
					if(!this.fechaIngreso.equals("")&&
					   UtilidadFecha.esFechaMenorQueOtraReferencia(aux, this.fechaIngreso))
					{
						errores.add("fecha referencia menor a fecha de ingreso",new ActionMessage("errors.fechaAnteriorIgualActual","de referencia (Sección Institución Referencia)","de ingreso ("+this.fechaIngreso+")"));
						fechaValida = false;
					}
						
				}
				 
			}
				
		}
		
		//Hora de referencia ---------------------------------------------------------------
		aux = this.getReferencia("horaReferencia").toString();
		if(aux.equals(""))
			errores.add("hora referencia es requerida",new ActionMessage("errors.required","La hora de referencia (Sección Institución Referencia)"));
		else
		{
			if(!UtilidadFecha.validacionHora(aux).puedoSeguir)
				errores.add("hora referencia es inválida",new ActionMessage("errors.formatoHoraInvalido","de referencia (Sección Institución Referencia)"));
			else
			{
				if(fechaValida&&this.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
				{
					//debe ser menor o igual a la fecha/hora actual
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getReferencia("fechaReferencia").toString(), aux).isTrue())
						errores.add("fecha/hora referencia mayor a fecha/hora actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de referencia (Sección Institución Referencia)","actual"));
					
					//debe ser mayor o igual a la fecha del ingreso
					if(!this.fechaIngreso.equals("")&&!this.horaIngreso.equals("")&&
							!UtilidadFecha.compararFechas(this.getReferencia("fechaReferencia").toString(), aux,this.fechaIngreso,this.horaIngreso).isTrue())
						errores.add("fecha/hora referencia menor a fecha/hora ingreso",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de referencia (Sección Institución Referencia)","de ingreso ("+this.fechaIngreso+" - "+this.horaIngreso+")"));
				}
			}
		}
		
		//Profesional de la Salud ------------------------------------------------------------
		if(this.getReferencia("profesionalSalud").toString().equals(""))
			errores.add("profesional salud es requerida",new ActionMessage("errors.required","El profesional de la salud (Sección Institución Referencia)"));
		
		//Registro Médico ----------------------------------------------------------------------
		if(this.getReferencia("registroMedico").toString().equals(""))
			errores.add("registro médico es requerido",new ActionMessage("errors.required","El registro médico (Sección Institución Referencia)"));
		
		//Tipo de Usuario ----------------------------------------------------------------------
		if(this.getReferencia("tipoUsuario").toString().equals(""))
			errores.add("el tipo de usuario es requerido",new ActionMessage("errors.required","El tipo de usuario (Sección Institución Referencia)"));
		
		//Tipo de Referencia ----------------------------------------------------------------------
		if(this.getReferencia("tipoRef").toString().equals(""))
			errores.add("tipo de referencia es requerido",new ActionMessage("errors.required","El tipo de referencia (Sección Institución Referencia)"));
		
		//Tipo de atencón ----------------------------------------------------------------------
		if(this.getReferencia("tipoAtencion").toString().equals(""))
			errores.add("tipo atencion es requerido",new ActionMessage("errors.required","El tipo de atención (Sección Institución Referencia)"));
		
		//Motivo de referencia ---------------------------------------------------------------------
		if(this.getReferencia("codigoMotivoReferencia").toString().equals(""))
			errores.add("motivo referencia es requerido",new ActionMessage("errors.required","El motivo de referencia (Sección Institución Referencia)"));
		
		return errores;
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
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the tipoReferencia
	 */
	public String getTipoReferencia() {
		return tipoReferencia;
	}

	/**
	 * @param tipoReferencia the tipoReferencia to set
	 */
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	/**
	 * @return the tiposIdentificacion
	 */
	public ArrayList getTiposIdentificacion() {
		return tiposIdentificacion;
	}

	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacion(ArrayList tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

	/**
	 * @return the idIngreso
	 */
	public int getIdIngreso() {
		return idIngreso;
	}

	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * @return the referencia
	 */
	public HashMap getReferencia() {
		return referencia;
	}

	/**
	 * @param referencia the referencia to set
	 */
	public void setReferencia(HashMap referencia) {
		this.referencia = referencia;
	}
	
	/**
	 * @return Retorna elemento del mapa referencia
	 */
	public Object getReferencia(String key) {
		return referencia.get(key);
	}

	/**
	 * @param Asigna elemento al mapa referencia 
	 */
	public void setReferencia(String key,Object obj) {
		this.referencia.put(key,obj);
	}

	/**
	 * @return the mapaDiagnosticos
	 */
	public HashMap getMapaDiagnosticos() {
		return mapaDiagnosticos;
	}

	/**
	 * @param mapaDiagnosticos the mapaDiagnosticos to set
	 */
	public void setMapaDiagnosticos(HashMap mapaDiagnosticos) {
		this.mapaDiagnosticos = mapaDiagnosticos;
	}
	
	/**
	 * @return retorna elemento del mapa mapaDiagnosticos
	 */
	public Object getMapaDiagnosticos(String key) {
		return mapaDiagnosticos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaDiagnosticos t
	 */
	public void setMapaDiagnosticos(String key,Object obj) {
		this.mapaDiagnosticos.put(key,obj);
	}
	

	/**
	 * @return the mapaResultados
	 */
	public HashMap getMapaResultados() {
		return mapaResultados;
	}

	/**
	 * @param mapaResultados the mapaResultados to set
	 */
	public void setMapaResultados(HashMap mapaResultados) {
		this.mapaResultados = mapaResultados;
	}
	
	/**
	 * @return retorna elemento del mapa mapaResultados
	 */
	public Object getMapaResultados(String key) {
		return mapaResultados.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaResultados 
	 */
	public void setMapaResultados(String key,Object obj) {
		this.mapaResultados.put(key,obj);
	}
	
	

	/**
	 * @return the mapaResultadosEliminados
	 */
	public HashMap getMapaResultadosEliminados() {
		return mapaResultadosEliminados;
	}

	/**
	 * @param mapaResultadosEliminados the mapaResultadosEliminados to set
	 */
	public void setMapaResultadosEliminados(HashMap mapaResultadosEliminados) {
		this.mapaResultadosEliminados = mapaResultadosEliminados;
	}
	
	/**
	 * @return retorna elemento del mapa mapaResultadosEliminados
	 */
	public Object getMapaResultadosEliminados(String key) {
		return mapaResultadosEliminados.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaResultadosEliminados 
	 */
	public void setMapaResultadosEliminados(String key,Object obj) {
		this.mapaResultadosEliminados.put(key,obj);
	}
	

	/**
	 * @return the mapaServicios
	 */
	public HashMap getMapaServicios() {
		return mapaServicios;
	}

	/**
	 * @param mapaServicios the mapaServicios to set
	 */
	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}
	
	/**
	 * @return retorna elemento del mapa mapaServicios
	 */
	public Object getMapaServicios(String key) {
		return mapaServicios.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaServicios 
	 */
	public void setMapaServicios(String key,Object obj) {
		this.mapaServicios.put(key,obj);
	}
	

	/**
	 * @return the mapaServiciosEliminados
	 */
	public HashMap getMapaServiciosEliminados() {
		return mapaServiciosEliminados;
	}

	/**
	 * @param mapaServiciosEliminados the mapaServiciosEliminados to set
	 */
	public void setMapaServiciosEliminados(HashMap mapaServiciosEliminados) {
		this.mapaServiciosEliminados = mapaServiciosEliminados;
	}
	
	/**
	 * @return retorna elemento del mapa mapaServiciosEliminados
	 */
	public Object getMapaServiciosEliminados(String key) {
		return mapaServiciosEliminados.get(key);
	}

	/**
	 * @param asigna elemento al mapa mapaServiciosEliminados 
	 */
	public void setMapaServiciosEliminados(String key,Object obj) {
		this.mapaServiciosEliminados.put(key,obj);
	}

	/**
	 * @return the mapaSignosVitales
	 */
	public HashMap getMapaSignosVitales() {
		return mapaSignosVitales;
	}

	/**
	 * @param mapaSignosVitales the mapaSignosVitales to set
	 */
	public void setMapaSignosVitales(HashMap mapaSignosVitales) {
		this.mapaSignosVitales = mapaSignosVitales;
	}
	
	/**
	 * @return retorna elemento del mapa mapaSignosVitales
	 */
	public Object getMapaSignosVitales(String key) {
		return mapaSignosVitales.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaSignosVitales 
	 */
	public void setMapaSignosVitales(String key,Object obj) {
		this.mapaSignosVitales.put(key,obj);
	}

	/**
	 * @return the institucionesSirc
	 */
	public HashMap getInstitucionesSirc() {
		return institucionesSirc;
	}

	/**
	 * @param institucionesSirc the institucionesSirc to set
	 */
	public void setInstitucionesSirc(HashMap institucionesSirc) {
		this.institucionesSirc = institucionesSirc;
	}

	/**
	 * @return the ciudades
	 */
	public ArrayList getCiudades() {
		return ciudades;
	}

	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(ArrayList ciudades) {
		this.ciudades = ciudades;
	}

	/**
	 * @return the especialidades
	 */
	public HashMap getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(HashMap especialidades) {
		this.especialidades = especialidades;
	}
	
	/**
	 * @return Retorna elemento del mapa especialidades
	 */
	public Object getEspecialidades(String key) {
		return especialidades.get(key);
	}

	/**
	 * @param Asigna elemento al mapa especialidades 
	 */
	public void setEspecialidades(String key,Object obj) {
		this.especialidades.put(key,obj);
	}

	/**
	 * @return the motivos
	 */
	public HashMap getMotivos() {
		return motivos;
	}

	/**
	 * @param motivos the motivos to set
	 */
	public void setMotivos(HashMap motivos) {
		this.motivos = motivos;
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
	 * @return the convenios
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}
	
	

	/**
	 * @return the estratos
	 */
	public HashMap getEstratos() {
		return estratos;
	}

	/**
	 * @param estratos the estratos to set
	 */
	public void setEstratos(HashMap estratos) {
		this.estratos = estratos;
	}
	
	/**
	 * @return retorna elemento del mapa estratos
	 */
	public Object getEstratos(String key) {
		return estratos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa estratos 
	 */
	public void setEstratos(String key,Object obj) {
		this.estratos.put(key,obj);
	}

	/**
	 * @return the serviciosSirc
	 */
	public HashMap getServiciosSirc() {
		return serviciosSirc;
	}

	/**
	 * @param serviciosSirc the serviciosSirc to set
	 */
	public void setServiciosSirc(HashMap serviciosSirc) {
		this.serviciosSirc = serviciosSirc;
	}
	
	/**
	 * @return retorna elemento del mapa serviciosSirc
	 */
	public Object getServiciosSirc(String key) {
		return serviciosSirc.get(key);
	}

	/**
	 * @param Asigna elemento al mapa serviciosSirc 
	 */
	public void setServiciosSirc(String key,Object obj) {
		this.serviciosSirc.put(key,obj);
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

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
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the serviciosInsertados
	 */
	public String getServiciosInsertados() {
		return serviciosInsertados;
	}

	/**
	 * @param serviciosInsertados the serviciosInsertados to set
	 */
	public void setServiciosInsertados(String serviciosInsertados) {
		this.serviciosInsertados = serviciosInsertados;
	}

	/**
	 * @return the seccionAfiliacionSistema
	 */
	public boolean isSeccionAfiliacionSistema() {
		return seccionAfiliacionSistema;
	}

	/**
	 * @param seccionAfiliacionSistema the seccionAfiliacionSistema to set
	 */
	public void setSeccionAfiliacionSistema(boolean seccionAfiliacionSistema) {
		this.seccionAfiliacionSistema = seccionAfiliacionSistema;
	}

	/**
	 * @return the seccionHistoriaClinica
	 */
	public boolean isSeccionHistoriaClinica() {
		return seccionHistoriaClinica;
	}

	/**
	 * @param seccionHistoriaClinica the seccionHistoriaClinica to set
	 */
	public void setSeccionHistoriaClinica(boolean seccionHistoriaClinica) {
		this.seccionHistoriaClinica = seccionHistoriaClinica;
	}

	/**
	 * @return the seccionInstitucionReferencia
	 */
	public boolean isSeccionInstitucionReferencia() {
		return seccionInstitucionReferencia;
	}

	/**
	 * @param seccionInstitucionReferencia the seccionInstitucionReferencia to set
	 */
	public void setSeccionInstitucionReferencia(boolean seccionInstitucionReferencia) {
		this.seccionInstitucionReferencia = seccionInstitucionReferencia;
	}

	/**
	 * @return the seccionProcedimientosSolicitados
	 */
	public boolean isSeccionProcedimientosSolicitados() {
		return seccionProcedimientosSolicitados;
	}

	/**
	 * @param seccionProcedimientosSolicitados the seccionProcedimientosSolicitados to set
	 */
	public void setSeccionProcedimientosSolicitados(
			boolean seccionProcedimientosSolicitados) {
		this.seccionProcedimientosSolicitados = seccionProcedimientosSolicitados;
	}

	/**
	 * @return the estadosConciencia
	 */
	public HashMap getEstadosConciencia() {
		return estadosConciencia;
	}

	/**
	 * @param estadosConciencia the estadosConciencia to set
	 */
	public void setEstadosConciencia(HashMap estadosConciencia) {
		this.estadosConciencia = estadosConciencia;
	}

	/**
	 * @return the procedimientos
	 */
	public HashMap getProcedimientos() {
		return procedimientos;
	}

	/**
	 * @param procedimientos the procedimientos to set
	 */
	public void setProcedimientos(HashMap procedimientos) {
		this.procedimientos = procedimientos;
	}
	
	/**
	 * @return Retorna elemento del mapa procedimientos
	 */
	public Object getProcedimientos(String key) {
		return procedimientos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa procedimientos 
	 */
	public void setProcedimientos(String key,Object obj) {
		this.procedimientos.put(key,obj);
	}

	/**
	 * @return the procedimientosInsertados
	 */
	public String getProcedimientosInsertados() {
		return procedimientosInsertados;
	}

	/**
	 * @param procedimientosInsertados the procedimientosInsertados to set
	 */
	public void setProcedimientosInsertados(String procedimientosInsertados) {
		this.procedimientosInsertados = procedimientosInsertados;
	}

	/**
	 * @return the tiposAmbulancia
	 */
	public HashMap getTiposAmbulancia() {
		return tiposAmbulancia;
	}

	/**
	 * @param tiposAmbulancia the tiposAmbulancia to set
	 */
	public void setTiposAmbulancia(HashMap tiposAmbulancia) {
		this.tiposAmbulancia = tiposAmbulancia;
	}

	/**
	 * @return the viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
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
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * @return the horaIngreso
	 */
	public String getHoraIngreso() {
		return horaIngreso;
	}

	/**
	 * @param horaIngreso the horaIngreso to set
	 */
	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	/**
	 * @return the codigoArea
	 */
	public int getCodigoArea() {
		return codigoArea;
	}

	/**
	 * @param codigoArea the codigoArea to set
	 */
	public void setCodigoArea(int codigoArea) {
		this.codigoArea = codigoArea;
	}

	/**
	 * @return the consecutivoInt
	 */
	public int getConsecutivoInt() {
		return consecutivoInt;
	}

	/**
	 * @param consecutivoInt the consecutivoInt to set
	 */
	public void setConsecutivoInt(int consecutivoInt) {
		this.consecutivoInt = consecutivoInt;
	}

	/**
	 * @return the consecutivoAnio
	 */
	public String getConsecutivoAnio() {
		return consecutivoAnio;
	}

	/**
	 * @param consecutivoAnio the consecutivoAnio to set
	 */
	public void setConsecutivoAnio(String consecutivoAnio) {
		this.consecutivoAnio = consecutivoAnio;
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
	 * @return the deboAbrirAsignacionCita
	 */
	public boolean isDeboAbrirAsignacionCita() {
		return deboAbrirAsignacionCita;
	}

	/**
	 * @param deboAbrirAsignacionCita the deboAbrirAsignacionCita to set
	 */
	public void setDeboAbrirAsignacionCita(boolean deboAbrirAsignacionCita) {
		this.deboAbrirAsignacionCita = deboAbrirAsignacionCita;
	}

	/**
	 * @return the pathFuncAsignacionCita
	 */
	public String getPathFuncAsignacionCita() {
		return pathFuncAsignacionCita;
	}

	/**
	 * @param pathFuncAsignacionCita the pathFuncAsignacionCita to set
	 */
	public void setPathFuncAsignacionCita(String pathFuncAsignacionCita) {
		this.pathFuncAsignacionCita = pathFuncAsignacionCita;
	}

	/**
	 * @return the filtrarTipoReferencia
	 */
	public boolean isFiltrarTipoReferencia() {
		return filtrarTipoReferencia;
	}

	/**
	 * @param filtrarTipoReferencia the filtrarTipoReferencia to set
	 */
	public void setFiltrarTipoReferencia(boolean filtrarTipoReferencia) {
		this.filtrarTipoReferencia = filtrarTipoReferencia;
	}

	/**
	 * @return the numeroReferencia
	 */
	public String getNumeroReferencia() {
		return numeroReferencia;
	}

	/**
	 * @param numeroReferencia the numeroReferencia to set
	 */
	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPais() {
		return pais;
	}
	
	/**
	 * 
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the codigoTipoIdentificacion
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * @param codigoTipoIdentificacion the codigoTipoIdentificacion to set
	 */
	public void setCodigoTipoIdentificacion(String codigoTipoIdentificacion) {
		this.codigoTipoIdentificacion = codigoTipoIdentificacion;
	}

	
	
}
