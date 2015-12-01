package com.princetonsa.actionform.manejoPaciente;

import com.princetonsa.dto.manejoPaciente.DtoInfoAmparosReclamacion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.RutasArchivosFURIPS;

public class RegistroEventosCatastroficosForm extends ValidatorForm
{
	
	private String estado;
	
	/**
	 * Codigo del registro de accidentes de transito
	 */
	private int codigo;
	
	/**
	 * codigo del ingreso relacionado al registro.
	 */
	private int ingreso;
	
	/**
	 * Empresa donde trabaja la persona accidentada
	 */
	private String empresaTrabaja;

	/**
	 * Ciudad Empresa
	 */
	private String ciudadEmpresa;
	
	/**
	 * Departamento Empresa.
	 */
	private String departamentoEmpresa;
	
	/**
	 * 
	 */
	private String ciudDepEmpresa;

	/**
	 * 
	 */
	private String direccionEvento;
	
	/**
	 * 
	 */
	private String fechaEvento;
	
	/**
	 * 
	 */
	private String ciudadEvento;
	
	/**
	 * 
	 */
	private String departamentoEvento;
	
	/**
	 * 
	 */
	private String ciuDepEvento;
	
	/**
	 * 
	 */
	private String zonaUrbanaEvento;
	
	/**
	 * 
	 */
	private int naturalezaEvento;
	
	
	/**
	 * 
	 */
	private String apellidoNombreTransporta;
	
	/**
	 * 
	 */
	private String tipoIdTransporta;
	
	/**
	 * 
	 */
	private String numeroIdTransporta;
	
	/**
	 * 
	 */
	private String ciudadExpIdTransporta;
	
	/**
	 * 
	 */
	private String deptoExpIdTransporta;
	
	/**
	 * 
	 */
	private String ciuDepExpIdTransporta;
	
	/**
	 * 
	 */
	private String telefonoTransporta; 
	
	/**
	 * 
	 */
    private String direccionTransporta;
	
	/**
	 * 
	 */
    private String ciudadTranporta;
	
	/**
	 * 
	 */
    private String deptoTransporta;
    
    /**
     * 
     */
    private String ciuDepTranporta;
	
	/**
	 * 
	 */
    private String transportaDesde;
	
	/**
	 * 
	 */
    private String transportaHasta;
	
	/**
	 * 
	 */
    private String tipoTransporte;
	
	/**
	 * 
	 */
    private String placaVehiculoTransporta;
	
	/**
	 * 
	 */
    private String estadoRegistro;
	
	/**
	 * 
	 */
    private String fechaModifica;
	
	/**
	 * 
	 */
    private String horaModifica;
	
	/**
	 * 
	 */
    private String usuarioModifica;
    
    
    /**
	 * 
	 */
	private ArrayList ciudades=new ArrayList();

	
	/**
	 * 
	 */
	private ArrayList tiposId=new ArrayList();
	
	
	/**
	 * 
	 */
	private HashMap<String,Object> naturalezaEventosCatastroficos=new HashMap<String, Object>();


	
	/**
	 * 
	 */
	private String linkVolver="";
	
	/**
	 * 
	 */
	private ArrayList profesionalesSalud=new ArrayList();
	
	/**
	 * 
	 */
	private String paisEmpresa;
	
	/**
	 * 
	 */
	private String paisEvento;
	
	/**
	 * 
	 */
	private String paisExpIdTransporta;
	
	/**
	 * 
	 */
	private String paisTransporta;
	
	/**
	 * Campo que indica si se debe ocultar o no encabezado
	 */
	private boolean ocultarEncabezado;
	
///////////////////////////////////////////////////////////////////////
	//cambios por anexo 485
	private HashMap listadoEventos =new HashMap ();
	
	private String index =ConstantesBD.codigoNuncaValido+"";
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;
	
	
	private DtoInfoAmparosReclamacion infoAmparo=new DtoInfoAmparosReclamacion();
	//-------------------------------

	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
////////////////////////////////////////////////////////////////////////////
	
	/*-----------------------------------------------
	 * 			INICIO ATRIBUTOS ANEXO 722
	 ------------------------------------------------*/
	
	/**
	 * Variable para almacenar la Condición de Afiliación SGSSS 
	 */
	private String sgsss;
	
	/**
	 * Variable para almacenar el Tipo de Régimen Seleccionado
	 */
	private String tipoRegimen;
	
	/**
	 * Variable para almacenar la Hora de Ocurrencia
	 */
	private String horaEvento;
	
	/**
	 * Variable para almacenar la Descripción Breve de la Ocurrencia
	 */
	private String descOcurrencia;
	
	/**
	 * Variable para almacenar la Descripción de Otro Evento
	 */
	private String descOtroEvento;
	
	/**
	 * Variable para almacenar el Tipo de Referencia
	 */
	private String tipoReferencia;
	
	/**
	 * Variable para almacenar la Fecha de Remisión
	 */
	private String fechaRemision;
	
	/**
	 * Variable para almacenar la Hora de Remisión
	 */
	private String horaRemision;
	
	/**
	 * Variable para cargar los prestadores remite/recibe. Cargar el select de Instituciones Sirc
	 */
	private ArrayList listInstitucionesSirc;
	
	/**
	 * Variable para almacenar el Prestador que Remite
	 */
	private String prestadorRemite;
	
	/**
	 * Variable para almacenar el Profesional que Remite
	 */
	private String profesionalRemite;
	
	/**
	 * Variable para almacenar el Cargo del Profesional que Remite
	 */
	private String cargoProfesionalRemite;
	
	/**
	 * Variable para almacenar el Prestador que Recibe
	 */
	private String prestadorRecibe;
	
	/**
	 * Variable para almacenar el Profesional que Recibe
	 */
	private String profesionalRecibe;
	
	/**
	 * Variable para almacenar el Cargo del Profesional que Recibe
	 */
	private String cargoProfesionalRecibe;
	
	/**
	 * Variable para almacenar la Fecha de Aceptación
	 */
	private String fechaAceptacion;
	
	/**
	 * Variable para almacenar la Hora de Aceptación
	 */
	private String horaAceptacion;
	
	/**
	 * Variable para almacenar la descripción de Otro Tipo de Transporte
	 */
	private String otroTipoTransporte;
	
	/**
	 * Variable para almacenar la Zona de Transporte
	 */
	private String zonaTransporte;
	
	/**
	 * Variable para almacenar el Valor Total Facturado Médico Qx.
	 */
	private String totalFacturadoMedicoQuirurgico;
	
	/**
	 * Variable para almacenar el Valor Total Reclamado Médico Qx.
	 */
	private String totalReclamadoMedicoQuirurgico;
	
	/**
	 * Variable para almacenar el Valor Total Facturado Médico Qx.
	 */
	private String totalFacturadoTransporte;
	
	/**
	 * Variable para almacenar el Valor Total Reclamado Médico Qx.
	 */
	private String totalReclamadoTransporte;
	
	/**
	 * Variable que indica si es una Reclamación S ó N
	 */
	private String esReclamacion;
	
	/**
	 * Variable que indica si es furips S ó N
	 */
	private String furips;
	
	/**
	 * Variable que indica si es furpro S ó N
	 */
	private String furpro;
	
	/**
	 * Variable que indica si es furtran S ó N
	 */
	private String furtran;
	
	/**
	 * Variable que almacena la respuesta a glosa
	 */
	private String respuestaGlosa;
	
	/**
	 * Variable String que almacena el No. Radicado Anterior
	 */
	private String numeroRadicadoAnterior;
	
	/**
	 * Variable String para almacenar el No. Consecutivo de la Reclamación
	 */
	private String numeroConsecutivoReclamacion;
	
	/**
	 * Variable para almacenear la Descripción de la Protesis
	 */
	private String protesis;
	
	/**
	 * Variable para almacenar el Valor de la Protesis
	 */
	private String valorProtesis;
	
	/**
	 * Variable para almacenear la Descripción de la Adaptación de la Protesis
	 */
	private String adaptacionProtesis;
	
	/**
	 * Variable para almacenar el Valor de la Adaptacion de la Protesis
	 */
	private String valorAdaptacionProtesis;
	
	/**
	 * Variable para almacenear la Descripción de la Rehabilitacion
	 */
	private String rehabilitacion;
	
	/**
	 * Variable para almacenar el Valor de la Rehabilitacion
	 */
	private String valorRehabilitacion;
	
	/**
	 * Variable que indica si el usuario tiene el rol para modificar la Sección Amparo
	 */
	private boolean modificarAmparos;
	
	/**
	 * Variable que indica si el ingreso esta facturado o no
	 */
	private String validacionIngresoFacturado;
	
	/**
	 * Variables Boolean que indica si se esta llamando desde el Consultar/Imprimir ó
	 * desde el Modificar Amparos y Reclamaciones
	 */
	private boolean porConsultarImprimir;
	
	
	/*-----------------------------------------------
	 * 			FIN ATRIBUTOS ANEXO 722
	 ------------------------------------------------*/


	/**
	 * 
	 */
	private DtoReclamacionesAccEveFact amparoXReclamar;
	
	
	/**
	 * 
	 */
	private ArrayList<DtoReclamacionesAccEveFact> listadoReclamaciones;
	

	
	/**
	 * 
	 */
	private int indiceReclamacionSeleccionada=ConstantesBD.codigoNuncaValido;
	
	/**
	 * 
	 */
	private String imprimirFURIPS=ConstantesBD.acronimoNo;
	
	
	/**
	 * 
	 */
	private String imprimirFURIPS2=ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	private RutasArchivosFURIPS rutasArchivos;
	

	/**
	 * 
	 */
	private String numeroFoliosFURIPS="";
	
	/**
	 * 
	 */
	private String imprimirFURPRO=ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	private String numeroFoliosFURPRO="";
	
	/**
	 * 
	 */
	private String imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
	
    public void reset()
    {
    	this.codigo=ConstantesBD.codigoNuncaValido;
		this.empresaTrabaja="";
		this.ciudadEmpresa="";
		this.departamentoEmpresa="";
		this.fechaEvento="";
		this.direccionEvento="";
		this.ciudadEvento="";
		this.departamentoEvento="";
		this.zonaUrbanaEvento="";
		this.naturalezaEvento=ConstantesBD.codigoNuncaValido;
		this.apellidoNombreTransporta="";
		this.tipoIdTransporta="";
		this.numeroIdTransporta="";
		this.ciudadExpIdTransporta="";
		this.deptoExpIdTransporta="";
		this.telefonoTransporta="";
		this.direccionTransporta="";
		this.ciudadTranporta="";
		this.deptoTransporta="";
		this.transportaDesde="";
		this.transportaHasta="";
		this.tipoTransporte="";
		this.placaVehiculoTransporta="";
		this.estadoRegistro="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.paisEmpresa="";
		this.paisEvento="";
		this.paisExpIdTransporta="";
		this.paisTransporta="";
		
		//Atributos para manejar los select;
		this.ciudDepEmpresa = ""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepEvento = ""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepExpIdTransporta = ""+ConstantesBD.separadorSplit+"";
		this.ciuDepTranporta = ""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.linkVolver = "";
		this.ocultarEncabezado = false;
		this.listadoEventos = new HashMap ();
		this.index = ConstantesBD.codigoNuncaValido+"";
		
		//Inicio Variables Anexo 722
		this.sgsss = "";
		this.tipoRegimen = "";
		this.horaEvento = "";
		this.descOcurrencia = "";
		this.descOtroEvento = "";
		this.tipoReferencia = "";
		this.fechaRemision = "";
		this.horaRemision = "";
		this.listInstitucionesSirc = new ArrayList();
		this.prestadorRemite = "";
		this.profesionalRemite = "";
		this.cargoProfesionalRemite = "";
		this.prestadorRecibe = "";
		this.profesionalRecibe = "";
		this.cargoProfesionalRecibe = "";
		this.fechaAceptacion = "";
		this.horaAceptacion = "";
		this.otroTipoTransporte = "";
		this.zonaTransporte = "";
		this.totalFacturadoMedicoQuirurgico = "0.0";
		this.totalReclamadoMedicoQuirurgico = "0.0";
		this.totalFacturadoTransporte = "0.0";
		this.totalReclamadoTransporte = "0.0";
		this.esReclamacion = ConstantesBD.acronimoNo;
		this.respuestaGlosa = "";
		this.numeroRadicadoAnterior = "";
		this.numeroConsecutivoReclamacion = "";
		this.furips = ConstantesBD.acronimoNo;
		this.furpro = ConstantesBD.acronimoNo;
		this.furtran = ConstantesBD.acronimoNo;
		this.protesis = "";
		this.valorProtesis = "";
		this.adaptacionProtesis = "";
		this.valorAdaptacionProtesis = "";
		this.rehabilitacion = "";
		this.valorRehabilitacion = "";
		this.porConsultarImprimir = true;
		this.validacionIngresoFacturado="";
		//Fin Variables Anexo 722
    	this.infoAmparo=new DtoInfoAmparosReclamacion();
    	
    	this.amparoXReclamar=new DtoReclamacionesAccEveFact();
		this.listadoReclamaciones=new ArrayList<DtoReclamacionesAccEveFact>();
		
		this.imprimirFURIPS=ConstantesBD.acronimoNo;
		this.imprimirFURIPS2=ConstantesBD.acronimoNo;
		this.imprimirFURPRO=ConstantesBD.acronimoNo;
		this.imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
		this.numeroFoliosFURIPS="";
    	this.rutasArchivos= new RutasArchivosFURIPS();
		this.numeroFoliosFURPRO="";
    }

    public int numeroReclamacionesAImprimir() 
	{
		int reclamacionesAImprimir=0;
		for(DtoReclamacionesAccEveFact reclamacion:this.listadoReclamaciones)
		{
			if(UtilidadTexto.getBoolean(reclamacion.getImprimirFuripsFurpro()))
			{
				reclamacionesAImprimir++;
			}
		}
		return reclamacionesAImprimir;
	}
    
    
    /**
	 * Metodo para las validaciones de la aplicacion.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		if(estado.equals("generarReporte"))
		{
			if(UtilidadTexto.getBoolean(this.imprimirARCHIVOPLANO))
			{
				if(UtilidadTexto.getBoolean(this.imprimirFURIPS))
				{
					if(Utilidades.convertirAEntero(this.numeroFoliosFURIPS)<=0)
					{
						errores.add("numeroFoliosFURIPS",new ActionMessage("errors.required","El numero de folios (FURIPS) "));
					}
				}
				if(UtilidadTexto.getBoolean(this.imprimirFURPRO))
				{
					if(Utilidades.convertirAEntero(this.numeroFoliosFURPRO)<=0)
					{
						errores.add("numeroFoliosFURPRO",new ActionMessage("errors.required","El numero de folios (FURPRO) "));
					}
				}
			}
		}
		
		//Validamos si el estado es Guardar, sin importar el estado del registro; para validar los formatos de la fecha y hora
		if(estado.equals("guardar"))
		{
			//*************INICIO VALIDACIÓN FECHA/HORA EVENTO*************************
			if(UtilidadCadena.noEsVacio(this.fechaEvento.trim()))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaEvento()))
					errores.add("fechaEvento", new ActionMessage("errors.formatoFechaInvalido", "Fecha de Ocurrencia"));
	            else
				{
	            	if (UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFechaEvento()))
	            		errores.add("fechaEvento", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia"," Ocurrencia","Actual"));
				}
			}
			if(UtilidadCadena.noEsVacio(this.horaEvento.trim()))
			{
				if(!UtilidadFecha.validacionHora(this.horaEvento).puedoSeguir)
					errores.add("horaEvento", new ActionMessage("errors.formatoHoraInvalido","Evento"));
			}
			//*****************FIN VALIDACIÓN FECHA/HORA EVENTO*************************
			
			//*************INICIO VALIDACIÓN FECHA/HORA REMISION************************
			if(UtilidadCadena.noEsVacio(this.fechaRemision.trim()))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaRemision()))
					errores.add("fechaRemision", new ActionMessage("errors.formatoFechaInvalido", "Fecha de Remisión"));
			}
			if(UtilidadCadena.noEsVacio(this.horaRemision.trim()))
			{
				if(!UtilidadFecha.validacionHora(this.horaRemision).puedoSeguir)
					errores.add("horaRemision", new ActionMessage("errors.formatoHoraInvalido","Remisión"));
			}
			//*************FIN VALIDACIÓN FECHA/HORA REMISION***************************
			
			//*************INICIO VALIDACIÓN FECHA/HORA ACEPTACIÓN**********************
			if(UtilidadCadena.noEsVacio(this.fechaAceptacion.trim()))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaAceptacion()))
					errores.add("fechaAceptacion", new ActionMessage("errors.formatoFechaInvalido", "Fecha de Aceptación"));
			}
			if(UtilidadCadena.noEsVacio(this.horaAceptacion.trim()))
			{
				if(!UtilidadFecha.validacionHora(this.horaAceptacion).puedoSeguir)
					errores.add("horaAceptación", new ActionMessage("errors.formatoHoraInvalido","Aceptación"));
			}
			//*************FIN VALIDACIÓN FECHA/HORA ACEPTACION*************************
		}
		
		//Validamos si el estado es guardar y el estado del registro es FINALIZADO para hacer los campos requeridos
		if(estado.equals("guardar") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
		{
			if(!this.empresaTrabaja.trim().equals(""))
			{
				if(this.ciudadEmpresa.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Municipio Empresa"));
			}
			
			if(!UtilidadCadena.noEsVacio(this.sgsss.trim()))
				errores.add("sgsss",new ActionMessage("errors.required","La Condición Afiliación al SGSSS"));
			
			if(UtilidadCadena.noEsVacio(this.sgsss.trim()))
			{
				if(this.sgsss.trim().equals(ConstantesIntegridadDominio.acronimoAfiliadoSgsss))
				{
					if(!UtilidadCadena.noEsVacio(this.tipoRegimen.trim()))
						errores.add("tipoRegimen",new ActionMessage("errors.required","El Tipo Régimen"));
				}
			}
			
			if(this.direccionEvento.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La Dirección donde ocurrio el Evento Catastrófico"));
			if(this.fechaEvento.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La Fecha de Ocurrencia"));
			if(this.horaEvento.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La Hora de Ocurrencia"));
			if(this.ciudadEvento.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","El Municipio Evento"));
			if(this.zonaUrbanaEvento.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La Zona Evento"));
			if(!UtilidadCadena.noEsVacio(this.descOcurrencia.trim()))
				errores.add("",new ActionMessage("errors.required","La Descripción Breve de la Ocurrencia"));
			if(this.naturalezaEvento<=0)
				errores.add("",new ActionMessage("errors.required","La Naturaleza Evento"));
			if(this.naturalezaEvento == ConstantesBD.codigoNatEventoOtrosEnat || this.naturalezaEvento == ConstantesBD.codigoNatEventoOtrosEtec || this.naturalezaEvento == ConstantesBD.codigoNatEventoOtros)
			{
				if(!UtilidadCadena.noEsVacio(this.descOtroEvento.trim()))
					errores.add("",new ActionMessage("errors.required","La Descripción de Otro Evento"));
			}
			
			//Se valida si el campo Tipo Referencia es diferente a Ninguno
			if(UtilidadCadena.noEsVacio(this.tipoReferencia.trim()))
			{
				//Se valida los requerimientos del campo Fecha Remision
				if(!UtilidadCadena.noEsVacio(this.fechaRemision.trim()))
					errores.add("fechaRemision",new ActionMessage("errors.required","La Fecha de Remisión"));
				//Se valida el campo Hora Remision
				if(!UtilidadCadena.noEsVacio(this.horaRemision.trim()))
					errores.add("horaRemision",new ActionMessage("errors.required","La Hora de Remisión"));
				//Se valida el campo Prestador Remite
				if(!UtilidadCadena.noEsVacio(this.prestadorRemite.trim()))
					errores.add("prestadorRemite",new ActionMessage("errors.required","El Prestador que Remite"));
				//Se valida el campo Profesional que Remite
				if(!UtilidadCadena.noEsVacio(this.profesionalRemite.trim()))
					errores.add("profesionalRemite",new ActionMessage("errors.required","El Profesional que Remite"));
				//Se valida el campo Cargo del Profesional que Remite
				if(!UtilidadCadena.noEsVacio(this.cargoProfesionalRemite.trim()))
					errores.add("cargoProfesionalRemite",new ActionMessage("errors.required","El Cargo del Profesional que Remite"));
				//Se valida los requerimientos del campo Fecha Aceptación
				if(!UtilidadCadena.noEsVacio(this.fechaAceptacion.trim()))
					errores.add("fechaAceptacion",new ActionMessage("errors.required","La Fecha de Aceptación"));
				//Se valida el campo Hora Aceptación
				if(!UtilidadCadena.noEsVacio(this.horaAceptacion.trim()))
					errores.add("horaAceptacion",new ActionMessage("errors.required","La Hora de Aceptación"));
				//Se valida el campo Prestador Recibe
				if(!UtilidadCadena.noEsVacio(this.prestadorRecibe.trim()))
					errores.add("prestadorRecibe",new ActionMessage("errors.required","El Prestador que Recibe"));
				//Se valida el campo Profesional que Recibe
				if(!UtilidadCadena.noEsVacio(this.profesionalRecibe.trim()))
					errores.add("profesionalRecibe",new ActionMessage("errors.required","El Profesional que Recibe"));
				//Se valida el campo Cargo del Profesional que Recibe
				if(!UtilidadCadena.noEsVacio(this.cargoProfesionalRecibe.trim()))
					errores.add("cargoProfecionalRecibe",new ActionMessage("errors.required","El Cargo del Profesional que Recibe"));
			}
			
			if(!this.apellidoNombreTransporta.trim().equals(""))
			{
				if(this.tipoIdTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Tipo ID Tranporta"));
				if(this.numeroIdTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Número ID Transporta"));
				if(this.paisExpIdTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El País Expedición ID Transporta"));
				if(this.ciuDepExpIdTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","La Ciudad de Expedición ID Transporta"));
				if(this.direccionTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","La Dirección Transporta"));
				if(this.telefonoTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Teléfono Transporta"));
				if(this.ciudadTranporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Ciudad Transporta"));
				if(this.deptoTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Departamento Transporta"));
				if(this.transportaDesde.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Transporta Victima Desde"));
				if(this.transportaHasta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Transporta Victima Hasta"));
				if(this.tipoTransporte.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El Tipo Transporte"));
				if(this.tipoTransporte.equals(ConstantesIntegridadDominio.acronimoTipoTransporteOtros))
				{
					if(!UtilidadCadena.noEsVacio(this.otroTipoTransporte.trim()))
						errores.add("otroTipoTransporte",new ActionMessage("errors.required","La Descripción del Otro Tipo de Transporte"));
				}
				if(this.placaVehiculoTransporta.trim().equals(""))
					errores.add("",new ActionMessage("errors.required","La Placa Vehículo Transporta"));
			}
			
			//Validamos si la Naturaleza Evento viene como un Tipo de Naturaleza Terrorista para hacer requerido los campos de la sección Servicios Reclamados
			if(this.naturalezaEvento == ConstantesBD.codigoNatEventoCombate ||
			   this.naturalezaEvento == ConstantesBD.codigoNatEventoMasacre ||
			   this.naturalezaEvento == ConstantesBD.codigoNatEventoOtros ||
			   this.naturalezaEvento == ConstantesBD.codigoNatEventoExplosionTerrorista ||
			   this.naturalezaEvento == ConstantesBD.codigoNatEventoTomaGuerrillera ||
			   this.naturalezaEvento == ConstantesBD.codigoNatEventoMinaAntipersonal)
			{
				//Se valida como requerido el campo Protesis
				if(!UtilidadCadena.noEsVacio(this.protesis))
					errores.add("protesis",new ActionMessage("errors.required","La Prótesis"));
				//Se valida como requerido el campo Valor Protesis
				if(!UtilidadCadena.noEsVacio(this.valorProtesis))
					errores.add("valorProtesis",new ActionMessage("errors.required","El Valor de la Prótesis"));
				//Se valida como requerido el campo Adaptación Protesis
				if(!UtilidadCadena.noEsVacio(this.adaptacionProtesis))
					errores.add("adaptacionProtesis",new ActionMessage("errors.required","La Adaptación de Prótesis"));
				//Se valida como requerido el campo Valor Adaptación Protesis
				if(!UtilidadCadena.noEsVacio(this.valorAdaptacionProtesis))
					errores.add("valorAdaptacionProtesis",new ActionMessage("errors.required","El Valor de la Adaptación de Prótesis"));
				//Se valida como requerido el campo Rehabilitacion
				if(!UtilidadCadena.noEsVacio(this.rehabilitacion))
					errores.add("rehabilitacion",new ActionMessage("errors.required","La Rehabilitación"));
				//Se valida como requerido el campo Valor de la Rehabilitacion
				if(!UtilidadCadena.noEsVacio(this.valorRehabilitacion))
					errores.add("valorRehabilitacion",new ActionMessage("errors.required","El Valor de la Rehabilitación"));
			}
			
		}
		
		//Validamos si el estado es para guardar la Sección Amparos por Reclamar
		/*if(estado.equals("actualizarAmparos"))
		{
			if(!UtilidadCadena.noEsVacio(this.totalFacturadoMedicoQuirurgico) && UtilidadCadena.noEsVacio(this.totalReclamadoMedicoQuirurgico))
				errores.add("totalFacturadoMedicoQX",new ActionMessage("errors.required","El Total Facturado Amparo Gastos Médico Quirúrgicos"));
			if(!UtilidadCadena.noEsVacio(this.totalReclamadoMedicoQuirurgico) && UtilidadCadena.noEsVacio(this.totalFacturadoMedicoQuirurgico))
				errores.add("totalFacturadoMedicoQX",new ActionMessage("errors.required","El Total Reclamado Amparo Gastos Médico Quirúrgicos"));
			
			if(!UtilidadCadena.noEsVacio(this.totalFacturadoTransporte) && UtilidadCadena.noEsVacio(this.totalReclamadoTransporte))
				errores.add("totalFacturadoMedicoQX",new ActionMessage("errors.required","El Total Facturado Amparo Gastos de Transporte y Movilización de la Víctima"));
			if(!UtilidadCadena.noEsVacio(this.totalReclamadoTransporte) && UtilidadCadena.noEsVacio(this.totalFacturadoTransporte))
				errores.add("totalFacturadoMedicoQX",new ActionMessage("errors.required","El Total Reclamado Amparo Gastos de Transporte y Movilización de la Víctima"));
		}*/
		
		//Validamos si el estado es para guardar la Sección Datos de la Reclamacion
		/*if(estado.equals("actualizarDatosReclamacion"))
		{
			//Se valida si al menos uno de los Tipos de Reclamación ha sido seleccionado
			if(this.furips.equals(ConstantesBD.acronimoNo) && this.furpro.equals(ConstantesBD.acronimoNo) && this.furtran.equals(ConstantesBD.acronimoNo))
				errores.add("tipoReclamacion",new ActionMessage("errors.required","El Tipo de Reclamación"));
			//Se valida si la Respuesta Glosa ha sido seleccionada
			if(!UtilidadCadena.noEsVacio(this.respuestaGlosa.trim()))
				errores.add("respuestaGlosa",new ActionMessage("errors.required","La Respuesta a Glosa"));
			//Se valida si el campo Número Radicado Anterior vacío
			if(!UtilidadCadena.noEsVacio(this.numeroRadicadoAnterior.trim()))
				errores.add("numRadicadoAnterior",new ActionMessage("errors.required","El Número Radicado Anterior"));
			//Se valida si el campo Número Consecutivo Reclamación vacío
			if(!UtilidadCadena.noEsVacio(this.numeroConsecutivoReclamacion.trim()))
				errores.add("numConsecutivoReclamacion",new ActionMessage("errors.required","El Número Consecutivo de la Reclamación"));
		}*/
		
		return errores;
	}
    
	public String getApellidoNombreTransporta()
	{
		return apellidoNombreTransporta;
	}


	public void setApellidoNombreTransporta(String apellidoNombreTransporta)
	{
		this.apellidoNombreTransporta = apellidoNombreTransporta;
	}


	public String getCiudadEmpresa()
	{
		return ciudadEmpresa;
	}


	public void setCiudadEmpresa(String ciudadEmpresa)
	{
		this.ciudadEmpresa = ciudadEmpresa;
	}


	public String getCiudadEvento()
	{
		return ciudadEvento;
	}


	public void setCiudadEvento(String ciudadEvento)
	{
		this.ciudadEvento = ciudadEvento;
	}


	public String getCiudadExpIdTransporta()
	{
		return ciudadExpIdTransporta;
	}


	public void setCiudadExpIdTransporta(String ciudadExpIdTransporta)
	{
		this.ciudadExpIdTransporta = ciudadExpIdTransporta;
	}


	public String getCiudadTranporta()
	{
		return ciudadTranporta;
	}


	public void setCiudadTranporta(String ciudadTranporta)
	{
		this.ciudadTranporta = ciudadTranporta;
	}


	public String getCiudDepEmpresa()
	{
		return this.ciudadEmpresa+ConstantesBD.separadorSplit+this.departamentoEmpresa+ConstantesBD.separadorSplit+this.paisEmpresa;
	}


	public void setCiudDepEmpresa(String ciudDepEmpresa)
	{
		
		String[] temp=ciudDepEmpresa.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadEmpresa=temp[0];
			this.departamentoEmpresa=temp[1];
			this.paisEmpresa=temp[2];
		}
		this.ciudDepEmpresa = ciudDepEmpresa;
	}

	public String getCiuDepEvento()
	{
		return this.ciudadEvento+ConstantesBD.separadorSplit+this.departamentoEvento+ConstantesBD.separadorSplit+this.paisEvento;
	}


	public void setCiuDepEvento(String ciuDepEvento)
	{
		String[] temp=ciuDepEvento.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadEvento=temp[0];
			this.departamentoEvento=temp[1];
			this.paisEvento=temp[2];
		}
		this.ciuDepEvento = ciuDepEvento;
	}

	public String getCiuDepExpIdTransporta()
	{
		return ciudadExpIdTransporta+ConstantesBD.separadorSplit+deptoExpIdTransporta;
	}

	public void setCiuDepExpIdTransporta(String ciuDepExpIdTransporta)
	{
		String[] temp=ciuDepExpIdTransporta.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadExpIdTransporta=temp[0];
			this.deptoExpIdTransporta=temp[1];
		}
		this.ciuDepExpIdTransporta = ciuDepExpIdTransporta;
	}
	
	public String getCiuDepTranporta()
	{
		return this.ciudadTranporta+ConstantesBD.separadorSplit+this.deptoTransporta+ConstantesBD.separadorSplit+this.paisTransporta;
	}

	public void setCiuDepTranporta(String ciuDepTranporta)
	{
		String[] temp=ciuDepTranporta.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadTranporta=temp[0];
			this.deptoTransporta=temp[1];
			this.paisTransporta=temp[2];
		}
		this.ciuDepTranporta = ciuDepTranporta;
	}

	public int getCodigo()
	{
		return codigo;
	}


	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}


	public String getDepartamentoEmpresa()
	{
		return departamentoEmpresa;
	}


	public void setDepartamentoEmpresa(String departamentoEmpresa)
	{
		this.departamentoEmpresa = departamentoEmpresa;
	}


	public String getDepartamentoEvento()
	{
		return departamentoEvento;
	}


	public void setDepartamentoEvento(String departamentoEvento)
	{
		this.departamentoEvento = departamentoEvento;
	}


	public String getDeptoExpIdTransporta()
	{
		return deptoExpIdTransporta;
	}


	public void setDeptoExpIdTransporta(String deptoExpIdTransporta)
	{
		this.deptoExpIdTransporta = deptoExpIdTransporta;
	}


	public String getDeptoTransporta()
	{
		return deptoTransporta;
	}


	public void setDeptoTransporta(String deptoTransporta)
	{
		this.deptoTransporta = deptoTransporta;
	}


	public String getDireccionTransporta()
	{
		return direccionTransporta;
	}


	public void setDireccionTransporta(String direccionTransporta)
	{
		this.direccionTransporta = direccionTransporta;
	}


	public String getEmpresaTrabaja()
	{
		return empresaTrabaja;
	}


	public void setEmpresaTrabaja(String empresaTrabaja)
	{
		this.empresaTrabaja = empresaTrabaja;
	}


	public String getEstadoRegistro()
	{
		return estadoRegistro;
	}


	public void setEstadoRegistro(String estadoRegistro)
	{
		this.estadoRegistro = estadoRegistro;
	}


	public String getFechaEvento()
	{
		return fechaEvento;
	}


	public void setFechaEvento(String fechaEvento)
	{
		this.fechaEvento = fechaEvento;
	}


	public String getFechaModifica()
	{
		return fechaModifica;
	}


	public void setFechaModifica(String fechaModifica)
	{
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica()
	{
		return horaModifica;
	}


	public void setHoraModifica(String horaModifica)
	{
		this.horaModifica = horaModifica;
	}


	public int getIngreso()
	{
		return ingreso;
	}


	public void setIngreso(int ingreso)
	{
		this.ingreso = ingreso;
	}


	public int getNaturalezaEvento()
	{
		return naturalezaEvento;
	}


	public void setNaturalezaEvento(int naturalezaEvento)
	{
		this.naturalezaEvento = naturalezaEvento;
	}


	public String getNumeroIdTransporta()
	{
		return numeroIdTransporta;
	}


	public void setNumeroIdTransporta(String numeroIdTransporta)
	{
		this.numeroIdTransporta = numeroIdTransporta;
	}

	public String getPlacaVehiculoTransporta()
	{
		return placaVehiculoTransporta;
	}


	public void setPlacaVehiculoTransporta(String placaVehiculoTransporta)
	{
		this.placaVehiculoTransporta = placaVehiculoTransporta;
	}

	public String getTelefonoTransporta()
	{
		return telefonoTransporta;
	}


	public void setTelefonoTransporta(String telefonoTransporta)
	{
		this.telefonoTransporta = telefonoTransporta;
	}


	public String getTipoIdTransporta()
	{
		return tipoIdTransporta;
	}


	public void setTipoIdTransporta(String tipoIdTransporta)
	{
		this.tipoIdTransporta = tipoIdTransporta;
	}


	public String getTipoTransporte()
	{
		return tipoTransporte;
	}


	public void setTipoTransporte(String tipoTransporte)
	{
		this.tipoTransporte = tipoTransporte;
	}


	public String getTransportaDesde()
	{
		return transportaDesde;
	}


	public void setTransportaDesde(String transportaDesde)
	{
		this.transportaDesde = transportaDesde;
	}


	public String getTransportaHasta()
	{
		return transportaHasta;
	}


	public void setTransportaHasta(String transportaHasta)
	{
		this.transportaHasta = transportaHasta;
	}


	public String getUsuarioModifica()
	{
		return usuarioModifica;
	}


	public void setUsuarioModifica(String usuarioModifica)
	{
		this.usuarioModifica = usuarioModifica;
	}


	public String getZonaUrbanaEvento()
	{
		return zonaUrbanaEvento;
	}


	public void setZonaUrbanaEvento(String zonaUrbanaEvento)
	{
		this.zonaUrbanaEvento = zonaUrbanaEvento;
	}


	public String getEstado()
	{
		return estado;
	}


	public void setEstado(String estado)
	{
		this.estado = estado;
	}


	public String getDireccionEvento()
	{
		return direccionEvento;
	}


	public void setDireccionEvento(String direccionEvento)
	{
		this.direccionEvento = direccionEvento;
	}
	
	public ArrayList getCiudades()
	{
		return ciudades;
	}


	public void setCiudades(ArrayList ciudades)
	{
		this.ciudades = ciudades;
	}



	public ArrayList getTiposId()
	{
		return tiposId;
	}


	public void setTiposId(ArrayList tiposId)
	{
		this.tiposId = tiposId;
	}


	public ArrayList getProfesionalesSalud()
	{
		return profesionalesSalud;
	}


	public void setProfesionalesSalud(ArrayList profesionalesSalud)
	{
		this.profesionalesSalud = profesionalesSalud;
	}


	public HashMap<String, Object> getNaturalezaEventosCatastroficos()
	{
		return naturalezaEventosCatastroficos;
	}


	public void setNaturalezaEventosCatastroficos(
			HashMap<String, Object> naturalezaEventosCatastroficos)
	{
		this.naturalezaEventosCatastroficos = naturalezaEventosCatastroficos;
	}



	/**
	 * @return the linkVolver
	 */
	public String getLinkVolver() {
		return linkVolver;
	}

	/**
	 * @param linkVolver the linkVolver to set
	 */
	public void setLinkVolver(String linkVolver) {
		this.linkVolver = linkVolver;
	}

	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}

	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisEmpresa() {
		return paisEmpresa;
	}
	
	/**
	 * 
	 * @param paisEmpresa
	 */
	public void setPaisEmpresa(String paisEmpresa) {
		this.paisEmpresa = paisEmpresa;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisEvento() {
		return paisEvento;
	}
	
	/**
	 * 
	 * @param paisEvento
	 */
	public void setPaisEvento(String paisEvento) {
		this.paisEvento = paisEvento;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisExpIdTransporta() {
		return paisExpIdTransporta;
	}
	
	/**
	 * 
	 * @param paisExpIdTransporta
	 */
	public void setPaisExpIdTransporta(String paisExpIdTransporta) {
		this.paisExpIdTransporta = paisExpIdTransporta;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisTransporta() {
		return paisTransporta;
	}
	
	/**
	 * 
	 * @param paisTransporta
	 */
	public void setPaisTransporta(String paisTransporta) {
		this.paisTransporta = paisTransporta;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public HashMap getListadoEventos() {
		return listadoEventos;
	}

	public void setListadoEventos(HashMap listadoEventos) {
		this.listadoEventos = listadoEventos;
	}

	public Object getListadoEventos(String key) {
		return listadoEventos.get(key);
	}

	public void setListadoEventos(String key, Object value) {
		this.listadoEventos.put(key, value);
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
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

	/**
	 * @return the sgsss
	 */
	public String getSgsss() {
		return sgsss;
	}

	/**
	 * @param sgsss the sgsss to set
	 */
	public void setSgsss(String sgsss) {
		this.sgsss = sgsss;
	}

	/**
	 * @return the tipoRegimen
	 */
	public String getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * @param tipoRegimen the tipoRegimen to set
	 */
	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}

	/**
	 * @return the horaEvento
	 */
	public String getHoraEvento() {
		return horaEvento;
	}

	/**
	 * @param horaEvento the horaEvento to set
	 */
	public void setHoraEvento(String horaEvento) {
		this.horaEvento = horaEvento;
	}

	/**
	 * @return the descOcurrencia
	 */
	public String getDescOcurrencia() {
		return descOcurrencia;
	}

	/**
	 * @param descOcurrencia the descOcurrencia to set
	 */
	public void setDescOcurrencia(String descOcurrencia) {
		this.descOcurrencia = descOcurrencia;
	}

	/**
	 * @return the descOtroEvento
	 */
	public String getDescOtroEvento() {
		return descOtroEvento;
	}

	/**
	 * @param descOtroEvento the descOtroEvento to set
	 */
	public void setDescOtroEvento(String descOtroEvento) {
		this.descOtroEvento = descOtroEvento;
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
	 * @return the fechaRemision
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}

	/**
	 * @param fechaRemision the fechaRemision to set
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}

	/**
	 * @return the horaRemision
	 */
	public String getHoraRemision() {
		return horaRemision;
	}

	/**
	 * @param horaRemision the horaRemision to set
	 */
	public void setHoraRemision(String horaRemision) {
		this.horaRemision = horaRemision;
	}

	/**
	 * @return the listInstitucionesSirc
	 */
	public ArrayList getListInstitucionesSirc() {
		return listInstitucionesSirc;
	}

	/**
	 * @param listInstitucionesSirc the listInstitucionesSirc to set
	 */
	public void setListInstitucionesSirc(ArrayList listInstitucionesSirc) {
		this.listInstitucionesSirc = listInstitucionesSirc;
	}

	/**
	 * @return the prestadorRemite
	 */
	public String getPrestadorRemite() {
		return prestadorRemite;
	}

	/**
	 * @param prestadorRemite the prestadorRemite to set
	 */
	public void setPrestadorRemite(String prestadorRemite) {
		this.prestadorRemite = prestadorRemite;
	}

	/**
	 * @return the prestadorRecibe
	 */
	public String getPrestadorRecibe() {
		return prestadorRecibe;
	}

	/**
	 * @param prestadorRecibe the prestadorRecibe to set
	 */
	public void setPrestadorRecibe(String prestadorRecibe) {
		this.prestadorRecibe = prestadorRecibe;
	}

	/**
	 * @return the profesionalRemite
	 */
	public String getProfesionalRemite() {
		return profesionalRemite;
	}

	/**
	 * @param profesionalRemite the profesionalRemite to set
	 */
	public void setProfesionalRemite(String profesionalRemite) {
		this.profesionalRemite = profesionalRemite;
	}

	/**
	 * @return the cargoProfesionalRemite
	 */
	public String getCargoProfesionalRemite() {
		return cargoProfesionalRemite;
	}

	/**
	 * @param cargoProfesionalRemite the cargoProfesionalRemite to set
	 */
	public void setCargoProfesionalRemite(String cargoProfesionalRemite) {
		this.cargoProfesionalRemite = cargoProfesionalRemite;
	}

	/**
	 * @return the profesionalRecibe
	 */
	public String getProfesionalRecibe() {
		return profesionalRecibe;
	}

	/**
	 * @param profesionalRecibe the profesionalRecibe to set
	 */
	public void setProfesionalRecibe(String profesionalRecibe) {
		this.profesionalRecibe = profesionalRecibe;
	}

	/**
	 * @return the cargoProfesionalRecibe
	 */
	public String getCargoProfesionalRecibe() {
		return cargoProfesionalRecibe;
	}

	/**
	 * @param cargoProfesionalRecibe the cargoProfesionalRecibe to set
	 */
	public void setCargoProfesionalRecibe(String cargoProfesionalRecibe) {
		this.cargoProfesionalRecibe = cargoProfesionalRecibe;
	}

	/**
	 * @return the fechaAceptacion
	 */
	public String getFechaAceptacion() {
		return fechaAceptacion;
	}

	/**
	 * @param fechaAceptacion the fechaAceptacion to set
	 */
	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	/**
	 * @return the horaAceptacion
	 */
	public String getHoraAceptacion() {
		return horaAceptacion;
	}

	/**
	 * @param horaAceptacion the horaAceptacion to set
	 */
	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}

	/**
	 * @return the otroTipoTransporte
	 */
	public String getOtroTipoTransporte() {
		return otroTipoTransporte;
	}

	/**
	 * @param otroTipoTransporte the otroTipoTransporte to set
	 */
	public void setOtroTipoTransporte(String otroTipoTransporte) {
		this.otroTipoTransporte = otroTipoTransporte;
	}

	/**
	 * @return the zonaTransporte
	 */
	public String getZonaTransporte() {
		return zonaTransporte;
	}

	/**
	 * @param zonaTransporte the zonaTransporte to set
	 */
	public void setZonaTransporte(String zonaTransporte) {
		this.zonaTransporte = zonaTransporte;
	}

	/**
	 * @return the totalFacturadoMedicoQuirurgico
	 */
	public String getTotalFacturadoMedicoQuirurgico() {
		return totalFacturadoMedicoQuirurgico;
	}

	/**
	 * @param totalFacturadoMedicoQuirurgico the totalFacturadoMedicoQuirurgico to set
	 */
	public void setTotalFacturadoMedicoQuirurgico(
			String totalFacturadoMedicoQuirurgico) {
		this.totalFacturadoMedicoQuirurgico = totalFacturadoMedicoQuirurgico;
	}

	/**
	 * @return the totalReclamadoMedicoQuirurgico
	 */
	public String getTotalReclamadoMedicoQuirurgico() {
		return totalReclamadoMedicoQuirurgico;
	}

	/**
	 * @param totalReclamadoMedicoQuirurgico the totalReclamadoMedicoQuirurgico to set
	 */
	public void setTotalReclamadoMedicoQuirurgico(
			String totalReclamadoMedicoQuirurgico) {
		this.totalReclamadoMedicoQuirurgico = totalReclamadoMedicoQuirurgico;
	}

	/**
	 * @return the totalFacturadoTransporte
	 */
	public String getTotalFacturadoTransporte() {
		return totalFacturadoTransporte;
	}

	/**
	 * @param totalFacturadoTransporte the totalFacturadoTransporte to set
	 */
	public void setTotalFacturadoTransporte(String totalFacturadoTransporte) {
		this.totalFacturadoTransporte = totalFacturadoTransporte;
	}

	/**
	 * @return the totalReclamadoTransporte
	 */
	public String getTotalReclamadoTransporte() {
		return totalReclamadoTransporte;
	}

	/**
	 * @param totalReclamadoTransporte the totalReclamadoTransporte to set
	 */
	public void setTotalReclamadoTransporte(String totalReclamadoTransporte) {
		this.totalReclamadoTransporte = totalReclamadoTransporte;
	}

	/**
	 * @return the esReclamacion
	 */
	public String getEsReclamacion() {
		return esReclamacion;
	}

	/**
	 * @param esReclamacion the esReclamacion to set
	 */
	public void setEsReclamacion(String esReclamacion) {
		this.esReclamacion = esReclamacion;
	}
	
	/**
	 * @return the respuestaGlosa
	 */
	public String getRespuestaGlosa() {
		return respuestaGlosa;
	}

	/**
	 * @param respuestaGlosa the respuestaGlosa to set
	 */
	public void setRespuestaGlosa(String respuestaGlosa) {
		this.respuestaGlosa = respuestaGlosa;
	}

	/**
	 * @return the numeroRadicadoAnterior
	 */
	public String getNumeroRadicadoAnterior() {
		return numeroRadicadoAnterior;
	}

	/**
	 * @param numeroRadicadoAnterior the numeroRadicadoAnterior to set
	 */
	public void setNumeroRadicadoAnterior(String numeroRadicadoAnterior) {
		this.numeroRadicadoAnterior = numeroRadicadoAnterior;
	}

	/**
	 * @return the numeroConsecutivoReclamacion
	 */
	public String getNumeroConsecutivoReclamacion() {
		return numeroConsecutivoReclamacion;
	}

	/**
	 * @param numeroConsecutivoReclamacion the numeroConsecutivoReclamacion to set
	 */
	public void setNumeroConsecutivoReclamacion(String numeroConsecutivoReclamacion) {
		this.numeroConsecutivoReclamacion = numeroConsecutivoReclamacion;
	}

	/**
	 * @return the furips
	 */
	public String getFurips() {
		return furips;
	}

	/**
	 * @param furips the furips to set
	 */
	public void setFurips(String furips) {
		this.furips = furips;
	}

	/**
	 * @return the furpro
	 */
	public String getFurpro() {
		return furpro;
	}

	/**
	 * @param furpro the furpro to set
	 */
	public void setFurpro(String furpro) {
		this.furpro = furpro;
	}

	/**
	 * @return the furtran
	 */
	public String getFurtran() {
		return furtran;
	}

	/**
	 * @param furtran the furtran to set
	 */
	public void setFurtran(String furtran) {
		this.furtran = furtran;
	}

	/**
	 * @return the protesis
	 */
	public String getProtesis() {
		return protesis;
	}

	/**
	 * @param protesis the protesis to set
	 */
	public void setProtesis(String protesis) {
		this.protesis = protesis;
	}

	/**
	 * @return the valorProtesis
	 */
	public String getValorProtesis() {
		return valorProtesis;
	}

	/**
	 * @param valorProtesis the valorProtesis to set
	 */
	public void setValorProtesis(String valorProtesis) {
		this.valorProtesis = valorProtesis;
	}

	/**
	 * @return the adaptacionProtesis
	 */
	public String getAdaptacionProtesis() {
		return adaptacionProtesis;
	}

	/**
	 * @param adaptacionProtesis the adaptacionProtesis to set
	 */
	public void setAdaptacionProtesis(String adaptacionProtesis) {
		this.adaptacionProtesis = adaptacionProtesis;
	}

	/**
	 * @return the valorAdaptacionProtesis
	 */
	public String getValorAdaptacionProtesis() {
		return valorAdaptacionProtesis;
	}

	/**
	 * @param valorAdaptacionProtesis the valorAdaptacionProtesis to set
	 */
	public void setValorAdaptacionProtesis(String valorAdaptacionProtesis) {
		this.valorAdaptacionProtesis = valorAdaptacionProtesis;
	}

	/**
	 * @return the rehabilitacion
	 */
	public String getRehabilitacion() {
		return rehabilitacion;
	}

	/**
	 * @param rehabilitacion the rehabilitacion to set
	 */
	public void setRehabilitacion(String rehabilitacion) {
		this.rehabilitacion = rehabilitacion;
	}

	/**
	 * @return the valorRehabilitacion
	 */
	public String getValorRehabilitacion() {
		return valorRehabilitacion;
	}

	/**
	 * @param valorRehabilitacion the valorRehabilitacion to set
	 */
	public void setValorRehabilitacion(String valorRehabilitacion) {
		this.valorRehabilitacion = valorRehabilitacion;
	}

	/**
	 * @return the modificarAmparos
	 */
	public boolean isModificarAmparos() {
		return modificarAmparos;
	}

	/**
	 * @param modificarAmparos the modificarAmparos to set
	 */
	public void setModificarAmparos(boolean modificarAmparos) {
		this.modificarAmparos = modificarAmparos;
	}
	
	
	/**
	 * @return the ingresoFacturado
	 */
	public String getValidacionIngresoFacturado() {
		return validacionIngresoFacturado;
	}

	/**
	 * @param IngresoFacturado the IngresoFacturado to set
	 */
	public void setValidacionIngresoFacturado(String validacionIngresoFacturado) {
		this.validacionIngresoFacturado = validacionIngresoFacturado;
	}

	/**
	 * @return the porConsultarImprimir
	 */
	public boolean isPorConsultarImprimir() {
		return porConsultarImprimir;
	}

	/**
	 * @param porConsultarImprimir the porConsultarImprimir to set
	 */
	public void setPorConsultarImprimir(boolean porConsultarImprimir) {
		this.porConsultarImprimir = porConsultarImprimir;
	}

	public DtoInfoAmparosReclamacion getInfoAmparo() {
		return infoAmparo;
	}

	public void setInfoAmparo(DtoInfoAmparosReclamacion infoAmparo) {
		this.infoAmparo = infoAmparo;
	}

	public DtoReclamacionesAccEveFact getAmparoXReclamar() {
		return amparoXReclamar;
	}

	public void setAmparoXReclamar(DtoReclamacionesAccEveFact amparoXReclamar) {
		this.amparoXReclamar = amparoXReclamar;
	}

	public ArrayList<DtoReclamacionesAccEveFact> getListadoReclamaciones() {
		return listadoReclamaciones;
	}

	public void setListadoReclamaciones(
			ArrayList<DtoReclamacionesAccEveFact> listadoReclamaciones) {
		this.listadoReclamaciones = listadoReclamaciones;
	}

	public int getIndiceReclamacionSeleccionada() {
		return indiceReclamacionSeleccionada;
	}

	public void setIndiceReclamacionSeleccionada(int indiceReclamacionSeleccionada) {
		this.indiceReclamacionSeleccionada = indiceReclamacionSeleccionada;
	}


	public String getImprimirFURIPS() {
		return imprimirFURIPS;
	}


	public void setImprimirFURIPS(String imprimirFURIPS) {
		this.imprimirFURIPS = imprimirFURIPS;
	}


	public String getImprimirFURPRO() {
		return imprimirFURPRO;
	}


	public void setImprimirFURPRO(String imprimirFURPRO) {
		this.imprimirFURPRO = imprimirFURPRO;
	}


	public String getImprimirARCHIVOPLANO() {
		return imprimirARCHIVOPLANO;
	}


	public void setImprimirARCHIVOPLANO(String imprimirARCHIVOPLANO) {
		this.imprimirARCHIVOPLANO = imprimirARCHIVOPLANO;
	}


	public void inicializarOpcionesImpresion() 
	{
		this.imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
		this.imprimirFURPRO=ConstantesBD.acronimoNo;
		this.imprimirFURIPS=ConstantesBD.acronimoNo;
		this.imprimirFURIPS2=ConstantesBD.acronimoNo;
	}

	public String getNumeroFoliosFURIPS() {
		return numeroFoliosFURIPS;
	}

	public void setNumeroFoliosFURIPS(String numeroFoliosFURIPS) {
		this.numeroFoliosFURIPS = numeroFoliosFURIPS;
	}

	public String getNumeroFoliosFURPRO() {
		return numeroFoliosFURPRO;
	}

	public void setNumeroFoliosFURPRO(String numeroFoliosFURPRO) {
		this.numeroFoliosFURPRO = numeroFoliosFURPRO;
	}

	public void resetFURIPSFURPRO() 
	{
		this.imprimirFURIPS=ConstantesBD.acronimoNo;
		this.imprimirFURPRO=ConstantesBD.acronimoNo;
		this.imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
		this.numeroFoliosFURIPS="";
		this.numeroFoliosFURPRO="";
    	this.rutasArchivos= new RutasArchivosFURIPS();
	}

	public String getImprimirFURIPS2() {
		return imprimirFURIPS2;
	}

	public void setImprimirFURIPS2(String imprimirFURIPS2) {
		this.imprimirFURIPS2 = imprimirFURIPS2;
	}
	
	/**
	 * 
	 * @param forma
	 */
	public void resetearNombreArchivos(UsuarioBasico usuario, InstitucionBasica institucionBasica,String frechaInicialAP,String fechaFinalAP) 
	{
		String fechaInicial= "_FI_"+((frechaInicialAP+"").replaceAll("/", ""))+"";
		String fechaFinal= "_FF_"+((fechaFinalAP+"").replaceAll("/", ""))+"";
		String rutaGeneral= ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFurips(usuario.getCodigoInstitucionInt())+"C"+ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt())+System.getProperty("file.separator");
		
		this.getRutasArchivos().setRutaGeneral(rutaGeneral);
		String complementoArchivo=institucionBasica.getCodMinsalud()+(UtilidadFecha.getFechaActual().replaceAll("/", ""))+fechaInicial+fechaFinal+".txt";
		
		///FURIPS 1
		this.getRutasArchivos().setRutaFURIPS1(rutaGeneral+"FURIPS"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURIPS1("FURIPS1"+complementoArchivo);
		this.getRutasArchivos().setExisteFURIPS1(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURIPS1("InconFURIPS1"+complementoArchivo);
		
		//FURIPS 2
		this.getRutasArchivos().setRutaFURIPS2(rutaGeneral+"FURIPS"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURIPS2("FURIPS2"+complementoArchivo);
		this.getRutasArchivos().setExisteFURIPS2(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURIPS2("InconFURIPS2"+complementoArchivo);
		
		//FURPRO
		this.getRutasArchivos().setRutaFURPRO(rutaGeneral+"FURPRO"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURPRO("FURPRO1"+complementoArchivo);
		this.getRutasArchivos().setExisteFURPRO(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURPRO("InconFURPRO1"+System.getProperty("file.separator"));
		
		//FURTRAN
		this.getRutasArchivos().setRutaFURTRAN(rutaGeneral+"FURTRAN"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURTRAN("FURTRAN"+complementoArchivo);
		this.getRutasArchivos().setExisteFURTRAN(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURTRAN("InconFURTRAN"+complementoArchivo);
	}

	public RutasArchivosFURIPS getRutasArchivos() {
		return rutasArchivos;
	}

	public void setRutasArchivos(RutasArchivosFURIPS rutasArchivos) {
		this.rutasArchivos = rutasArchivos;
	}
	
}