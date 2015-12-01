package com.princetonsa.actionform.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoInfoAmparosReclamacion;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.RutasArchivosFURIPS;

@SuppressWarnings("serial")
public class RegistroAccidentesTransitoForm extends ValidatorForm
{

	/**
	 * Codigo del registro de accidentes de transito
	 */
	private String codigo;
	
	/**
	 * codigo del ingreso relacionado al registro.
	 */
	private String ingreso;
	
	/**
	 * Empresa donde trabaja la persona accidentada
	 */
	private String empresaTrabaja;
	
	/**
	 * Direccion de la empresa;
	 */
	private String direccionEmpresa;
	
	/**
	 * Telefono de la empresa.
	 */
	private String telefonoEmpresa;
	
	/**
	 * Ciudad Empresa
	 */
	private String ciudadEmpresa;
	
	/**
	 * Departamento Empresa.
	 */
	private String departamentoEmpresa;
	
	/**
	 * Indica si la persona es ocupante o peaton Valores posibres S - N
	 */
	private String ocupante;
	
	
	/**
	 * Hace referencia al la codiccion accidentado referida al documento, valores posibles (AVEH - AMOT - APEA - ACIC).
	 */
	private String codicionAccidentado;
	
	/**
	 * Descripcion de como resulta lesionado.
	 */
	private String resultaLesionadoAl;
	
	/**
	 * En caso de que el accidente sea ocacionado por otro vehiculo, este
	 * atributo contiene la placa.
	 * Este atributo solo tiene valor si el atributo resultaLesionadoAl es AECV (Estrillarse con Vehiculo).
	 */
	private String placaVehiculoOcaciona;
	
	/**
	 * Descripcion del lugar del accidente;
	 */
	private String lugarAccidente;
	
	/**
	 * Fecha del accidente
	 */
	private String fechaAccidente;
	
	/**
	 * Hora del Accidente;
	 */
	private String horaAccidente;
	
	/**
	 * Ciudad del Accidente.
	 */
	private String ciudadAccidente;
	
	/**
	 * Departamento del Accidente
	 */
	private String departamentoAccidente;
	
	/**
	 * Zona del Accidente;
	 */
	private String zonaAccidente;
	
	/**
	 * Informacion del accidente (Descripcion de los Hechos).
	 */
	private String informacionAccidente;
	
	/**
	 * Marca del vehiculo del accidente.
	 */
	private String marcaVehiculo;
	
	/**
	 * Placa del vehiculo del accidente
	 */
	private String placa;
	
	/**
	 * Tipo de vehiculo.
	 */
	private String tipo;
	
	/**
	 * Empresa aseguradora del vehiculo, hace referencia a convenios.
	 */
	private String aseguradora;
	
	/**
	 * Nombre de la sucursal que expidio el nombre de la sucursal.
	 */
	private String agencia;
	
	/**
	 * Numero de poliza SOAT.
	 */
	private String numeroPoliza;
	
	/**
	 * Campo que indica si el vehiculo esta asegurado. valores posibles (S - N - FAN);
	 */
	private String asegurado;
	
	/**
	 * Campo para indicar la poliza. valores posibles(Vigente - Falsa - Vencida)
	 */
	private String poliza;
	
	/**
	 * Fecha inicial de vigencia de la poliza en caso de tenerla
	 */
	private String fechaInicialPoliza;
	
	/**
	 * Fecha final de la poliza en caso de tenerla.
	 */
	private String fechaFinalPoliza;
	
	/**
	 * 
	 */
	private String primerApellidoConductor;
	
	/**
	 * 
	 */
	private String segundoApellidoConductor;
	

	/**
	 * 
	 */
	private String primerNombreConductor;
	
	/**
	 * 
	 */
	private String segundoNombreConductor;
	
	/**
	 * Tipo de Identificacion del conductor
	 */
	private String tipoIdConductor;
	
	/**
	 * Numero Identificacion del Conductor.
	 */
	private String numeroIdConductor;
	
	/**
	 * Ciudad de expedicion de identificacion del conductor
	 */
	private String ciuExpedicionIdConductor;
	
	/**
	 * Departamento de expedicion de identificacion del conductor
	 */
	private String depExpedicionIdConductor;
	
	/**
	 * Dirreccion de residencia del conductor
	 */
	private String direccionConductor;
	
	/**
	 * Cidad de residencia del conductor.
	 */
	private String ciudadConductor;
	
	/**
	 * Departamento de residencia del conductor.
	 */
	private String departamentoConductor;
	
	/**
	 * Telefono del conductor
	 */
	private String telefonoConductor;
	
	/**
	 * Campo para manejar el tipo de referencia que se ha realizdo al paciente.
	 */
	private String tipoReferencia;
	
	/**
	 * Persona que realiza la referencia.
	 */
	private String personaRefiere;
	
	/**
	 * Ciudad que realiza la referencia.
	 */
	private String ciudadRefiere;
	
	/**
	 * Departamento que realiza la referencia.
	 */
	private String departamentoRefiere;
	
	/**
	 * Fecha en que se realiza la referencia.
	 */
	private String fechaReferencia;
	
	/**
	 * Perosnao institucion a la cual es referido el paciente 
	 */
	private String refereidoA;
	
	/**
	 * ciudad a la que es referido el paciente.
	 */
	private String ciudadReferido;
	
	/**
	 * Departamento al que es referido el paciente.
	 */
	private String departamentoReferido;
	
	/**
	 * Fecha de confimacion de la referencia.
	 */
	private String fechaConfirmacionReferido;
	
	
	/**
	 * Estado del fljo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String estadoRegistro;
	
	
	/**
	 * 
	 */
	private String apellidoNombreDeclarante;
	
	/**
	 * 
	 */
	private String tipoIdDeclarante;
	
	/**
	 * 
	 */
	private String numeroIdDeclarante;
	
	/**
	 * 
	 */
	private String ciuExpedicionIdDeclarante;
	
	/**
	 * 
	 */
	private String depExpedicionIdDeclarante;
	
	/**
	 * 
	 */
	private ArrayList ciudades=new ArrayList();
	
	/**
	 * 
	 */
	private ArrayList convenios=new ArrayList();
	
	/**
	 * 
	 */
	private ArrayList tiposId=new ArrayList();
	
	/**
	 * 
	 */
	private HashMap criteriosBusquedaMap;
	
	/**
	 * 
	 */
	private HashMap listadoMap;
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento de la empresa
	 */
	private String ciuDepEmpresa;
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento del acciedente;
	 */
	private String ciuDepAccidente;
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento del ciuDepConductor;
	 */
	private String ciuDepConductor;
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento del ciuDepIdConductor;
	 */
	private String ciuDepIdConductor;
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento del ciuDepRefiere;
	 */
	private String ciuDepRefiere;
	
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento del ciuDepReferido;
	 */
	private String ciuDepReferido;
	
	
	/**
	 * Metodo que tiene concatenado ciudad+separadorsplit+departamento del ciuDepDeclarante;
	 */
	private String ciuDepIdDeclarante;
	
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
    
    //*******atributos para ocultar encabezado*****************+
    private boolean ocultarEncabezado = false;
    //************************************************************
	
    
    
    /**
     * 
     */
	private int codigoViaIngreso=0;
	
	/**
	 * 
	 */
	private String cuenta;
	
	//////////////////////INFORMACION QUE SE RELACIONAD EN EL ANEX 383 - CAMBIOS EN OTRAS FUNCIONALIDES X FOSYGA
    //datos del propietario
	private String primerApellidoProp;
	private String segundoApellidoProp;
	private String primerNombreProp;
	private String segundoNombreProp;
	private String tipoIdProp;
	private String numeroIdProp;
	private String ciuDepExpIdProp;
	private String ciudadExpedicionIdProp;
	private String deptoExpedicionIdProp;
	private String direccionProp;
	private String telefonoProp;
	private String ciuDepProp;
	private String ciudadProp;
	private String deptoProp;
	
	
	private String apellidoNombreTransporta;
	private String tipoIdTransporta;
	private String numeroIdTransporta;
	private String ciudadExpedicionIdTransporta;
	private String deptoExpedicionIdTransporta;
	private String telefonoTransporta;
	private String direccionTransporta;
	private String ciuDepTranporta;
	private String ciuDepExpIdTransporta;
	private String ciudadTransporta;
	private String departamentoTransporta;
	private String transportaVictimaDesde;
	private String transportaVictimaHasta; 
	private String tipoTransporte;
	private String placaVehiculoTransporta;
	
	
	////////////////////////
	
	
	private String paisEmpresa;
	
	private String paisAccidente;
	
	private String paisExpedicionIdConductor;
	
	private String paisConductor;
	
	private String paisExpedicionIdDeclarante;
	
	private String paisExpedicionIdProp;
	
	private String paisExpedicionIdTransporta;
	
	private String paisProp;
	
	private String paisTransporta;
	
	

	
	/**
	 * 
	 */
	private String linkVolver="";
    
	///////////////////////////////////////////////////////////////////////
	//cambios por anexo 485
	private HashMap listadoAccidentes =new HashMap ();
	
	private String index =ConstantesBD.codigoNuncaValido+"";
	
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;
	
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	
	
	
	///////////////////////////////////////////////////////////////////////
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//cambios por anexo 722 cambios por resolucion 1915/08
	private String descripcionBreveOcurrencia="";
	
	private ArrayList<HashMap<String, Object>> tiposServiciosVehiculos= new ArrayList<HashMap<String,Object>>();
	
	private String otroTipoServicioVehiculo="";
	
	private String intervencionAutoridad="";
	
	private String cobroExcedentePoliza="";
	
	private Integer cantidadOtrosVehiAcc;
	
	private String existenOtrosVehiAcc=ConstantesBD.acronimoNo;
	
	private String placa2Vehiculo="";
	
	private String tipoId2Vehiculo="";
	
	private String nroId2Vehiculo="";
	
	private String placa3Vehiculo="";
	
	private String tipoId3Vehiculo="";
	
	private String nroId3Vehiculo="";
	
	private String secDesplegable1=ConstantesBD.acronimoNo;
	
	/////////////-------------------
	private String tipoReferenciaRem="";
	private String fechaRem="";
	private String horaRem="";
	private String prestadorRem="";
	private String profesionalRem="";
	private String cargoProfesionalRem="";
	private String fechaAceptacion="";
	private String horaAceptacion="";
	private String prestadorRecibe="";
	private String profesionalRecibe="";
	private String cargoProfesionalRecibe="";
	private String otroTipoTrans="";

	private String zonaTransporte="";
	

	private int institucion=ConstantesBD.codigoNuncaValido;
	
	private ArrayList<HashMap<String, Object>>prestadores= new ArrayList<HashMap<String,Object>>();
	
	private String secDesplegable2=ConstantesBD.acronimoNo;
	private String secDesplegable3=ConstantesBD.acronimoNo;
	

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
	private RutasArchivosFURIPS rutasArchivos;
	
	/**
	 * Variables Boolean que indica si se esta llamando desde el Consultar/Imprimir ó
	 * desde el Modificar Amparos y Reclamaciones
	 */
	private boolean porConsultarImprimir;
	
	
	public String getSecDesplegable3() {
		return secDesplegable3;
	}

	public void setSecDesplegable3(String secDesplegable3) {
		this.secDesplegable3 = secDesplegable3;
	}

	public long getNroConsReclamacion() {
		return nroConsReclamacion;
	}

	public void setNroConsReclamacion(long nroConsReclamacion) {
		this.nroConsReclamacion = nroConsReclamacion;
	}

	private double totalFacAmpQx=ConstantesBD.codigoNuncaValidoDouble;
	private double totalReclamoAmpQx=ConstantesBD.codigoNuncaValidoDouble;
	private double totalFacAmpTx=ConstantesBD.codigoNuncaValidoDouble;
	private double totalReclamoAmpTx=ConstantesBD.codigoNuncaValidoDouble;
	
	private String esReclamacion="";
	private String furips="";
	private String furtran="";
	private String respGlosa="";
	private String nroRadicadoAnterior="";
	private long nroConsReclamacion=0;
	
	private boolean modificarAmparos=false;
	
	private String validacionIngresoFacturado=ConstantesBD.acronimoNo;
	
	private DtoInfoAmparosReclamacion infoAmparo=new DtoInfoAmparosReclamacion();
	

	/**
	 * 
	 */
	private String imprimirFURIPS=ConstantesBD.acronimoNo;
	
	private String imprimirFURIPS2=ConstantesBD.acronimoNo;
	
	/**
	 * 
	 */
	private String imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
	

	private String numeroFoliosFURIPS="";
	
	private String permitirImprimirReporteParametroGeneral;
	
	
	//-------------------------------



	public boolean isModificarAmparos() {
		return modificarAmparos;
	}

	public void setModificarAmparos(boolean modificarAmparos) {
		this.modificarAmparos = modificarAmparos;
	}

	public String getEsReclamacion() {
		return esReclamacion;
	}

	public void setEsReclamacion(String esReclamacion) {
		this.esReclamacion = esReclamacion;
	}

	public String getFurips() {
		return furips;
	}

	public void setFurips(String furips) {
		this.furips = furips;
	}

	public String getFurtran() {
		return furtran;
	}

	public void setFurtran(String furtran) {
		this.furtran = furtran;
	}

	public String getRespGlosa() {
		return respGlosa;
	}

	public void setRespGlosa(String respGlosa) {
		this.respGlosa = respGlosa;
	}

	public String getNroRadicadoAnterior() {
		return nroRadicadoAnterior;
	}

	public void setNroRadicadoAnterior(String nroRadicadoAnterior) {
		this.nroRadicadoAnterior = nroRadicadoAnterior;
	}

	public String getSecDesplegable2() {
		return secDesplegable2;
	}

	public void setSecDesplegable2(String secDesplegable2) {
		this.secDesplegable2 = secDesplegable2;
	}

	public double getTotalFacAmpQx() {
		return totalFacAmpQx;
	}

	public void setTotalFacAmpQx(double totalFacAmpQx) {
		this.totalFacAmpQx = totalFacAmpQx;
	}

	public double getTotalReclamoAmpQx() {
		return totalReclamoAmpQx;
	}

	public void setTotalReclamoAmpQx(double totalReclamoAmpQx) {
		this.totalReclamoAmpQx = totalReclamoAmpQx;
	}

	public double getTotalFacAmpTx() {
		return totalFacAmpTx;
	}

	public void setTotalFacAmpTx(double totalFacAmpTx) {
		this.totalFacAmpTx = totalFacAmpTx;
	}

	public double getTotalReclamoAmpTx() {
		return totalReclamoAmpTx;
	}

	public void setTotalReclamoAmpTx(double totalReclamoAmpTx) {
		this.totalReclamoAmpTx = totalReclamoAmpTx;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getOtroTipoTrans() {
		return otroTipoTrans;
	}

	public void setOtroTipoTrans(String otroTipoTrans) {
		this.otroTipoTrans = otroTipoTrans;
	}

	public String getZonaTransporte() {
		return zonaTransporte;
	}

	public void setZonaTransporte(String zonaTransporte) {
		this.zonaTransporte = zonaTransporte;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Metodo que resetea los atributos del DTO
	 *
	 */
	public void reset() 
	{
		this.codigo="";
		this.empresaTrabaja="";
		this.direccionEmpresa="";
		this.telefonoEmpresa="";
		this.ciudadEmpresa="";
		this.departamentoEmpresa="";
		this.ocupante="";
		this.codicionAccidentado="";
		this.resultaLesionadoAl="";
		this.placaVehiculoOcaciona="";
		this.lugarAccidente="";
		this.fechaAccidente="";
		this.horaAccidente="";
		this.ciudadAccidente="";
		this.departamentoAccidente="";
		this.zonaAccidente="";
		this.informacionAccidente="";
		this.marcaVehiculo="";
		this.placa="";
		this.tipo="";
		this.aseguradora="";
		this.agencia="";
		this.numeroPoliza="";
		this.asegurado="";
		this.fechaInicialPoliza="";
		this.fechaFinalPoliza="";
		this.primerApellidoConductor="";
		this.segundoApellidoConductor="";
		this.primerNombreConductor="";
		this.segundoNombreConductor="";
		this.tipoIdConductor="";
		this.numeroIdConductor="";
		this.ciuExpedicionIdConductor="";
		this.depExpedicionIdConductor="";
		this.direccionConductor="";
		this.ciudadConductor="";
		this.departamentoConductor="";
		this.telefonoConductor="";
		this.tipoReferencia="";
		this.personaRefiere="";
		this.ciudadRefiere="";
		this.departamentoRefiere="";
		this.fechaReferencia="";
		this.refereidoA="";
		this.ciudadReferido="";
		this.departamentoReferido="";
		this.fechaConfirmacionReferido="";
		this.apellidoNombreDeclarante="";
		this.tipoIdDeclarante="";
		this.numeroIdDeclarante="";
		this.ciuExpedicionIdDeclarante="";
		this.depExpedicionIdDeclarante="";
		this.estadoRegistro="";
		this.criteriosBusquedaMap=new HashMap();
		this.listadoMap= new HashMap();
		this.ciuDepEmpresa=""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepAccidente=""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepConductor=""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepIdConductor=""+ConstantesBD.separadorSplit+"";
		this.ciuDepExpIdProp=""+ConstantesBD.separadorSplit+"";
		this.ciuDepProp=""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepRefiere=""+ConstantesBD.separadorSplit+"";
		this.ciuDepReferido=""+ConstantesBD.separadorSplit+"";
		this.ciuDepIdDeclarante=""+ConstantesBD.separadorSplit+"";
		this.ciuDepTranporta=""+ConstantesBD.separadorSplit+""+ConstantesBD.separadorSplit+"";
		this.ciuDepExpIdTransporta=""+ConstantesBD.separadorSplit+"";
		this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.codigoViaIngreso=0;
        this.poliza="";
    	this.primerApellidoProp="";
    	this.segundoApellidoProp="";
    	this.primerNombreProp="";
    	this.segundoNombreProp="";
    	this.tipoIdProp="";
    	this.numeroIdProp="";
    	this.ciudadExpedicionIdProp="";
    	this.deptoExpedicionIdProp="";
    	this.direccionProp="";
    	this.telefonoProp="";
    	this.ciudadProp="";
    	this.deptoProp="";
    	this.apellidoNombreTransporta="";
    	this.tipoIdTransporta="";
    	this.numeroIdTransporta="";
    	this.ciudadExpedicionIdTransporta="";
    	this.deptoExpedicionIdTransporta="";
    	this.telefonoTransporta="";
    	this.direccionTransporta="";
    	this.ciudadTransporta="";
    	this.departamentoTransporta="";
    	this.transportaVictimaDesde="";
    	this.transportaVictimaHasta=""; 
    	this.tipoTransporte="";
    	this.placaVehiculoTransporta="";
    	this.paisEmpresa="";
    	this.paisAccidente="";
    	this.paisExpedicionIdConductor="";
    	this.paisConductor="";
    	this.paisExpedicionIdDeclarante="";
    	this.paisExpedicionIdProp="";
    	this.paisExpedicionIdTransporta="";
    	this.paisProp="";
    	this.paisTransporta="";
    	this.linkVolver="";
    	this.cuenta="";
    	this.listadoAccidentes= new HashMap ();
    	this.descripcionBreveOcurrencia="";
    	this.tiposServiciosVehiculos = new ArrayList<HashMap<String,Object>>();
    	this.otroTipoServicioVehiculo="";
    	this.intervencionAutoridad="";
    	this.cobroExcedentePoliza="";
    	this.cantidadOtrosVehiAcc=null;
    	this.existenOtrosVehiAcc=ConstantesBD.acronimoNo;
    	this.placa2Vehiculo="";
    	this.tipoId2Vehiculo="";
    	this.nroId2Vehiculo="";
    	this.placa3Vehiculo="";
    	this.tipoId3Vehiculo="";
    	this.nroId3Vehiculo="";
    	this.secDesplegable1=ConstantesBD.acronimoNo;
    	this.secDesplegable2=ConstantesBD.acronimoNo;
    	this.secDesplegable3=ConstantesBD.acronimoNo;
    	this.tipoReferenciaRem="";
    	this.fechaRem="";
    	this.horaRem="";
    	this.prestadorRem="";
    	this.profesionalRem="";
    	this.cargoProfesionalRem="";
    	this.fechaAceptacion="";
    	this.horaAceptacion="";
    	this.prestadorRecibe="";
    	this.profesionalRecibe="";
    	this.cargoProfesionalRecibe="";
    	this.otroTipoTrans="";
    	this.zonaTransporte="";
    	this.institucion=ConstantesBD.codigoNuncaValido;
    	this.totalFacAmpQx=ConstantesBD.codigoNuncaValidoDouble;
    	this.totalFacAmpTx=ConstantesBD.codigoNuncaValidoDouble;
    	this.totalReclamoAmpQx=ConstantesBD.codigoNuncaValidoDouble;
    	this.totalReclamoAmpTx=ConstantesBD.codigoNuncaValidoDouble;
    	this.esReclamacion="";
    	this.furips="";
    	this.furtran="";
    	this.respGlosa="";
    	this.nroRadicadoAnterior="";
    	this.nroConsReclamacion=0;
    	this.porConsultarImprimir = true;
    	this.infoAmparo=new DtoInfoAmparosReclamacion();
    	this.amparoXReclamar=new DtoReclamacionesAccEveFact();
		this.listadoReclamaciones=new ArrayList<DtoReclamacionesAccEveFact>();
		this.imprimirFURIPS=ConstantesBD.acronimoNo;
		this.imprimirFURIPS2=ConstantesBD.acronimoNo;
		this.imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
		this.numeroFoliosFURIPS="";
    	this.rutasArchivos= new RutasArchivosFURIPS();
    	this.permitirImprimirReporteParametroGeneral="";
	}
	
	/**
	 * 
	 */
	public void resetImpresionFURIPS()
	{
		this.imprimirFURIPS=ConstantesBD.acronimoNo;
		this.imprimirFURIPS2=ConstantesBD.acronimoNo;
		this.imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
		this.numeroFoliosFURIPS="";
    	this.rutasArchivos= new RutasArchivosFURIPS();
	}
	
	public ArrayList<HashMap<String, Object>> getPrestadores() {
		return prestadores;
	}

	public void setPrestadores(ArrayList<HashMap<String, Object>> prestadores) {
		this.prestadores = prestadores;
	}

	public String getTipoReferenciaRem() {
		return tipoReferenciaRem;
	}

	public void setTipoReferenciaRem(String tipoReferenciaRem) {
		this.tipoReferenciaRem = tipoReferenciaRem;
	}

	public String getFechaRem() {
		return fechaRem;
	}

	public void setFechaRem(String fechaRem) {
		this.fechaRem = fechaRem;
	}

	public String getHoraRem() {
		return horaRem;
	}

	public void setHoraRem(String horaRem) {
		this.horaRem = horaRem;
	}

	public String getPrestadorRem() {
		return prestadorRem;
	}

	public void setPrestadorRem(String prestadorRem) {
		this.prestadorRem = prestadorRem;
	}

	public String getProfesionalRem() {
		return profesionalRem;
	}

	public void setProfesionalRem(String profesionalRem) {
		this.profesionalRem = profesionalRem;
	}

	public String getCargoProfesionalRem() {
		return cargoProfesionalRem;
	}

	public void setCargoProfesionalRem(String cargoProfesionalRem) {
		this.cargoProfesionalRem = cargoProfesionalRem;
	}

	public String getFechaAceptacion() {
		return fechaAceptacion;
	}

	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	public String getHoraAceptacion() {
		return horaAceptacion;
	}

	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}

	public String getPrestadorRecibe() {
		return prestadorRecibe;
	}

	public void setPrestadorRecibe(String prestadorRecibe) {
		this.prestadorRecibe = prestadorRecibe;
	}

	public String getProfesionalRecibe() {
		return profesionalRecibe;
	}

	public void setProfesionalRecibe(String profesionalRecibe) {
		this.profesionalRecibe = profesionalRecibe;
	}

	public String getCargoProfesionalRecibe() {
		return cargoProfesionalRecibe;
	}

	public void setCargoProfesionalRecibe(String cargoProfesionalRecibe) {
		this.cargoProfesionalRecibe = cargoProfesionalRecibe;
	}

	public String getSecDesplegable1() {
		return secDesplegable1;
	}

	public void setSecDesplegable1(String secDesplegable1) {
		this.secDesplegable1 = secDesplegable1;
	}

	public String getPlaca2Vehiculo() {
		return placa2Vehiculo;
	}

	public void setPlaca2Vehiculo(String placa2Vehiculo) {
		this.placa2Vehiculo = placa2Vehiculo;
	}

	public String getTipoId2Vehiculo() {
		return tipoId2Vehiculo;
	}

	public void setTipoId2Vehiculo(String tipoId2Vehiculo) {
		this.tipoId2Vehiculo = tipoId2Vehiculo;
	}

	public String getNroId2Vehiculo() {
		return nroId2Vehiculo;
	}

	public void setNroId2Vehiculo(String nroId2Vehiculo) {
		this.nroId2Vehiculo = nroId2Vehiculo;
	}

	public String getPlaca3Vehiculo() {
		return placa3Vehiculo;
	}

	public void setPlaca3Vehiculo(String placa3Vehiculo) {
		this.placa3Vehiculo = placa3Vehiculo;
	}

	public String getTipoId3Vehiculo() {
		return tipoId3Vehiculo;
	}

	public void setTipoId3Vehiculo(String tipoId3Vehiculo) {
		this.tipoId3Vehiculo = tipoId3Vehiculo;
	}

	public String getNroId3Vehiculo() {
		return nroId3Vehiculo;
	}

	public void setNroId3Vehiculo(String nroId3Vehiculo) {
		this.nroId3Vehiculo = nroId3Vehiculo;
	}

	public String getExistenOtrosVehiAcc() {
		return existenOtrosVehiAcc;
	}

	public void setExistenOtrosVehiAcc(String existenOtrosVehiAcc) {
		this.existenOtrosVehiAcc = existenOtrosVehiAcc;
	}

	public Integer getCantidadOtrosVehiAcc() {
		return cantidadOtrosVehiAcc;
	}

	public void setCantidadOtrosVehiAcc(Integer cantidadOtrosVehiAcc) {
		this.cantidadOtrosVehiAcc = cantidadOtrosVehiAcc;
	}

	
	public String getCobroExcedentePoliza() {
		return cobroExcedentePoliza;
	}

	public void setCobroExcedentePoliza(String cobroExcedentePoliza) {
		this.cobroExcedentePoliza = cobroExcedentePoliza;
	}

	public String getIntervencionAutoridad() {
		return intervencionAutoridad;
	}

	public void setIntervencionAutoridad(String intervencionAutoridad) {
		this.intervencionAutoridad = intervencionAutoridad;
	}

	public String getOtroTipoServicioVehiculo() {
		return otroTipoServicioVehiculo;
	}

	public void setOtroTipoServicioVehiculo(String otroTipoServicioVehiculo) {
		this.otroTipoServicioVehiculo = otroTipoServicioVehiculo;
	}

	public ArrayList<HashMap<String, Object>> getTiposServiciosVehiculos() {
		return tiposServiciosVehiculos;
	}

	public void setTiposServiciosVehiculos(
			ArrayList<HashMap<String, Object>> tiposServiciosVehiculos) {
		this.tiposServiciosVehiculos = tiposServiciosVehiculos;
	}

	public String getDescripcionBreveOcurrencia() {
		return descripcionBreveOcurrencia;
	}

	public void setDescripcionBreveOcurrencia(String descripcionBreveOcurrencia) {
		this.descripcionBreveOcurrencia = descripcionBreveOcurrencia;
	}

	
	
	
	/**
	 * Metodo para las validaciones de la aplicacion.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if((estado.equals("guardar")||estado.equals("modificar")))
		{	
			/*
			 * ya no es requerido, segun cambio en el anexo de 8 de abril del 2011
			if((this.ocupante.trim().equals("") || this.codicionAccidentado.trim().equals("")) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("ocupante requerido",new ActionMessage("errors.required","La condicion del Accidentado"));

			}
			if(this.resultaLesionadoAl.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("resultaLesionadoAl",new ActionMessage("errors.required","Quien Resulta Lesionado Al"));
			}
			*/
			if(this.resultaLesionadoAl.trim().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstrellarseConVehiculo))
			{
				if(this.placaVehiculoOcaciona.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("placaVehiculoOcaciona",new ActionMessage("errors.required","Placa Vehiculo Ocaciona"));
				}
			}
			else
			{
				//no se debe guardar la placa de vehiculo que ocaciona
				this.placaVehiculoOcaciona="";
			}
			
			if(this.lugarAccidente.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("lugarAccidente",new ActionMessage("errors.required","Sitio donde ocurrió el Accidente"));
			}
			boolean errorFechaHora=false;
			if(this.fechaAccidente.trim().equals(""))
			{
				if(this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errorFechaHora=true;
					errores.add("fechaAccidente",new ActionMessage("errors.required","Fecha Accidente"));
				}
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaAccidente))
				{
					errorFechaHora=true;
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaAccidente));
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAccidente,UtilidadFecha.getFechaActual()))
				{
					errorFechaHora=true;
					errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Fecha Accidente ("+this.fechaAccidente+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
				}
			}
			
			
			if(this.horaAccidente.trim().equals(""))
			{
				if(this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errorFechaHora=true;
					errores.add("horaAccidente",new ActionMessage("errors.required","Hora Accidente"));
				}
			}
			else if(!this.horaAccidente.trim().equals(""))
			{
				RespuestaValidacion val=UtilidadFecha.validacionHora(this.horaAccidente);
				if(!val.puedoSeguir)
				{
					errorFechaHora=true;
					errores.add("error en la hora",new ActionMessage("error.errorEnBlanco","[Hora Accidente] "+val.textoRespuesta+""));
				}
			}
			if(!errorFechaHora)
			{
				if(!this.fechaAccidente.equals("") && !this.horaAccidente.equals(""))
				{
					if(!UtilidadFecha.compararFechas( UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(),this.fechaAccidente, this.horaAccidente).isTrue())
					{
						errores.add("errors.fechaHoraPosteriorIgualActual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","Del Accidente ","Actual"));
					}
				}
			}
			
			if(this.ciudadAccidente.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("ciudadAccidente",new ActionMessage("errors.required","Ciudad Accidente"));
			}
			if(this.zonaAccidente.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("zonaAccidente",new ActionMessage("errors.required","Zona Urbana"));
			}
			if(!UtilidadCadena.noEsVacio(this.descripcionBreveOcurrencia)  && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				errores.add("zonaAccidente",new ActionMessage("errors.required"," Descripción Breve de la Ocurrencia "));
			
	
			
			////////////////////////////////////////////////////////////////////////////////////////////
			if(UtilidadCadena.noEsVacio(this.tipoReferenciaRem))
			{
				
				if(!UtilidadCadena.noEsVacio(this.fechaRem) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("fechaRem",new ActionMessage("errors.required"," La Fecha Remisión "));
			
				if(!UtilidadCadena.noEsVacio(this.horaRem) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("horaRem",new ActionMessage("errors.required"," La Hora Remisión "));
				
				if(!UtilidadCadena.noEsVacio(this.prestadorRem) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("prestadorRem",new ActionMessage("errors.required"," El Prestador que Remite "));
				
				if(!UtilidadCadena.noEsVacio(this.profesionalRem) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("profesionalRem",new ActionMessage("errors.required"," El Profesional que Remite "));
				
				if(!UtilidadCadena.noEsVacio(this.cargoProfesionalRem) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("cargoProfesionalRem",new ActionMessage("errors.required"," El Cargo Profesional Remite "));
				
				if(!UtilidadCadena.noEsVacio(this.fechaAceptacion) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("fechaAceptacion",new ActionMessage("errors.required"," La Fecha Aceptacion "));
				
				if(!UtilidadCadena.noEsVacio(this.horaAceptacion) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("horaAceptacion",new ActionMessage("errors.required"," La Hora Aceptacion "));
				
				if(!UtilidadCadena.noEsVacio(this.prestadorRecibe) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("prestadorRecibe",new ActionMessage("errors.required"," El Prestador que Recibe "));
				
				if(!UtilidadCadena.noEsVacio(this.profesionalRecibe) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("profesionalRecibe",new ActionMessage("errors.required"," El Profesional que Recibe "));
				
				if(!UtilidadCadena.noEsVacio(this.cargoProfesionalRecibe) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("cargoProfesionalRecibe",new ActionMessage("errors.required"," El Cargo Profesional Recibe "));
			}
			
//			////////////////////////////////////////////////////////////////////////////////////////
			
			
			if(UtilidadTexto.getBoolean(existenOtrosVehiAcc))
			{
				if (Utilidades.convertirAEntero(this.cantidadOtrosVehiAcc+"")<=0 && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required"," Cuantos Vehiculos involucrados "));
				else//aqui se valida la informacion requerida de la otros carros involucrados 
				{
					if (Utilidades.convertirAEntero(this.cantidadOtrosVehiAcc+"")==1)
					{
						//placa del segundo vehiculo
						if (!UtilidadCadena.noEsVacio(this.placa2Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
							errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","La placa del segundo vehículo involucrado "));
						//tipo id segundo vehiculo
						if (!UtilidadCadena.noEsVacio(this.tipoId2Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
							errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","El Tipo de Id del propietario del segundo vehículo involucrado "));
						//numero id propietario segundo vehiculo
						if (!UtilidadCadena.noEsVacio(this.nroId2Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
						errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","El numero de Id del propietario del segundo vehículo involucrado "));
					}
					else
						if (Utilidades.convertirAEntero(this.cantidadOtrosVehiAcc+"")==2)
						{
							//placa del segundo vehiculo
							if (!UtilidadCadena.noEsVacio(this.placa2Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
								errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","La placa del segundo vehículo involucrado "));
							//tipo id segundo vehiculo
							if (!UtilidadCadena.noEsVacio(this.tipoId2Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
								errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","El Tipo de Id del propietario del segundo vehículo involucrado "));
							//numero id propietario segundo vehiculo
							if (!UtilidadCadena.noEsVacio(this.nroId2Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
							errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","El numero de Id del propietario del segundo vehículo involucrado "));
							
							//placa del tercer vehiculo
							if (!UtilidadCadena.noEsVacio(this.placa3Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
								errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","La placa del tercer vehículo involucrado "));
							//tipo id tercer vehiculo
							if (!UtilidadCadena.noEsVacio(this.tipoId3Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
								errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","El Tipo de Id del propietario del tercer vehículo involucrado "));
							//numero id propietario tercer vehiculo
							if (!UtilidadCadena.noEsVacio(this.nroId3Vehiculo) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
							errores.add("cantidadOtrosVehiAcc",new ActionMessage("errors.required","El numero de Id del propietario del tercer vehículo involucrado "));
						}
						
				}
			}
			/*
			TAREA id=281563
			if(this.informacionAccidente.trim().equals(""))
			{
				errores.add("informacionAccidente",new ActionMessage("errors.required","Informacion del Accidente"));
			}
			*/
//			tarea 278350, solo deben ser requerido cuando el vehiculo no es fantasma.
			if(!this.asegurado.trim().equals(ConstantesIntegridadDominio.acronimoAseguradoFantasma))
			{
				if(this.marcaVehiculo.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("marcaVehiculo",new ActionMessage("errors.required","Marca del Vehículo"));
				}
				if(this.placa.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("placa",new ActionMessage("errors.required","Placa del Vehiculo"));
				}
				if((!UtilidadCadena.noEsVacio(this.tipo) || this.tipo.equals(ConstantesBD.codigoNuncaValido+"")) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("Tipo",new ActionMessage("errors.required","Tipo de servicio "));
				}
				if(this.agencia.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("agencia",new ActionMessage("errors.required","Sucursal o Agencia"));
				}
				if(this.asegurado.trim().equals(ConstantesBD.acronimoSi))
				{
					if(this.poliza.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					{
						errores.add("agencia",new ActionMessage("errors.required","Poliza"));
					}
				}
			}
			if(this.aseguradora.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("aseguradora",new ActionMessage("errors.required","Codigo Aseguradora"));
			}
			if(this.asegurado.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("asegurado",new ActionMessage("errors.required","Asegurado"));
			}
			else if(this.asegurado.trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.numeroPoliza.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("numeroPoliza",new ActionMessage("errors.required","Póliza SOAT No."));
				}
				boolean errorFechaInicial=false;
				boolean errorFechaFinal=false;
				if((this.fechaInicialPoliza.trim()).equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("Fecha fechaInicialPoliza",new ActionMessage("errors.required","La fecha desde"));
					errorFechaInicial=true;
				}
				else if(!UtilidadFecha.validarFecha(this.fechaInicialPoliza))
				{
						errores.add("fechaInicialPoliza", new ActionMessage("errors.formatoFechaInvalido",this.fechaInicialPoliza));
						errorFechaInicial=true;
				}
				if(this.fechaFinalPoliza.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("Fecha fechaFinalPoliza",new ActionMessage("errors.required","La fecha Hasta"));
					errorFechaFinal=true;
				}
				else if(!UtilidadFecha.validarFecha(this.fechaFinalPoliza))
				{
					errores.add("fechaFinalPoliza", new ActionMessage("errors.formatoFechaInvalido",this.fechaFinalPoliza));
					errorFechaFinal=true;
				}
				if(!errorFechaInicial&&!errorFechaFinal)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicialPoliza,this.fechaFinalPoliza))
					{
						errores.add("Fecha Inicial Mayor Fecha Final",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Desde","Hasta"));
					}
				}
			}
			
			//tarea 278350, solo deben ser requerido cuando el vehiculo no es fantasma.
			if(!this.asegurado.trim().equals(ConstantesIntegridadDominio.acronimoAseguradoFantasma))
			{
				
				//validaciones datos del propietario.
				if(this.primerApellidoProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Apellido Propietario"));
				}
				if(this.primerNombreProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Nombre Propietario"));
				}
				if(this.tipoIdProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Tipo ID Propietario"));
				}
				if(this.numeroIdProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Numero ID Propietario"));
				}
				if(this.paisExpedicionIdProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Pais ID Propietario"));
				}
				if(this.ciudadExpedicionIdProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","DE ID Propietario"));
				}
				if(this.direccionProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Direccion Propietario"));
				}
				if(this.telefonoProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Telefono Propietario"));
				}
				if(this.ciudadProp.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Ciudad Propietario"));
				}
				
				if(this.primerApellidoConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("apellidoNombreConductor",new ActionMessage("errors.required","Apellido del Conductor"));
				}
				if(this.primerNombreConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("apellidoNombreConductor",new ActionMessage("errors.required","Nombre del Conductor"));
				}

				if(this.tipoIdConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("tipoIdConductor",new ActionMessage("errors.required","Tipo ID Conductor"));
				}
				if(this.numeroIdConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("numeroIdConductor",new ActionMessage("errors.required","Numero ID Conductor"));
				}
				if(this.ciuExpedicionIdConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("ciuExpedicionIdConductor",new ActionMessage("errors.required","DE"));
				}
				if(this.paisExpedicionIdConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("paisExpedicionIdConductor",new ActionMessage("errors.required","Pais Expedicion"));
				}
				if(this.direccionConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("direccionConductor",new ActionMessage("errors.required","Direccion Conductor"));
				}
				if(this.ciudadConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("ciudadConductor",new ActionMessage("errors.required","Ciudad Conductor"));
				}
				if(this.telefonoConductor.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("telefonoConductor",new ActionMessage("errors.required","Telefono Conductor"));
				}
			}
			
			
			
/*			if(this.tipoReferencia.trim().equals(""))
			{
				errores.add("tipoReferencia",new ActionMessage("errors.required","Tipo Referencia"));
			}
			*/
			if(!this.tipoReferencia.trim().equals(""))
			{
				if(this.personaRefiere.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("personaRefiere",new ActionMessage("errors.required","Persona Referida Por"));
				}
				if(this.ciudadRefiere.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("ciudadRefiere",new ActionMessage("errors.required","Ciudad que Refiere"));
				}
				if(this.fechaReferencia.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("fechaReferencia",new ActionMessage("errors.required","Fecha de la referencia"));
				}
				else if(this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					if(!UtilidadFecha.validarFecha(this.fechaReferencia))
					{
						errores.add("fechaReferencia", new ActionMessage("errors.formatoFechaInvalido",this.fechaReferencia));
					}
					else
					{
						//validaciones con la fecha
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaReferencia,UtilidadFecha.getFechaActual()))
						{
							errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Referencia "+this.fechaReferencia,"Actual ("+UtilidadFecha.getFechaActual()+")"));
						}
					}
				}
				if(this.refereidoA.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("refereidoA",new ActionMessage("errors.required","Persona Referida A"));
				}
				if(this.ciudadReferido.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("ciudadReferido",new ActionMessage("errors.required","Ciudad Referida"));
				}
				if(this.fechaConfirmacionReferido.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("fechaConfirmacionReferido",new ActionMessage("errors.required","Fecha Confirmacion de la referencia"));
				}
				else if(this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					if(!UtilidadFecha.validarFecha(this.fechaConfirmacionReferido))
					{
						errores.add("fechaConfirmacionReferido", new ActionMessage("errors.formatoFechaInvalido",this.fechaConfirmacionReferido));
					}
					else
					{
						//validaciones con la fecha
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaConfirmacionReferido,UtilidadFecha.getFechaActual()))
						{
							errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Confirmacion Referencia "+this.fechaConfirmacionReferido,"Actual ("+UtilidadFecha.getFechaActual()+")"));
						}
						
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaReferencia,this.fechaConfirmacionReferido))
						{
							errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Fecha Referencia ("+this.fechaReferencia+")","Confirmacion Referencia "+this.fechaConfirmacionReferido));
						}
					}
				}
			}
			else
			{
				this.personaRefiere="";
				this.ciudadRefiere="";
				this.fechaReferencia="";
				this.refereidoA="";
				this.ciudadReferido="";
				this.fechaConfirmacionReferido="";
			}
			
			if(!this.apellidoNombreDeclarante.trim().equals(""))
			{
				if(this.tipoIdDeclarante.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("tipoIdDeclarante",new ActionMessage("errors.required","Tipo ID Declarante"));
				}
				if(this.numeroIdDeclarante.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("numeroIdDeclarante",new ActionMessage("errors.required","Numero ID Declarante"));
				}
				if(this.paisExpedicionIdDeclarante.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("paisExpedicionIdDeclarante",new ActionMessage("errors.required","Pais Expedicion Declarante"));
				}
				if(this.ciuExpedicionIdDeclarante.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("ciuExpedicionIdDeclarante",new ActionMessage("errors.required","DE Declarante"));
				}
			}
			else
			{
				this.tipoIdDeclarante="";
				this.numeroIdDeclarante="";
				this.ciuExpedicionIdDeclarante="";
				this.depExpedicionIdDeclarante="";
			}
			
			///validadciones del conductor que transporta a la victima
			if(!apellidoNombreTransporta.trim().equals(""))
			{
				if(this.tipoIdTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Tipo Id Tranporta"));
				}
				if(this.numeroIdTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Numero ID Transporta"));
				}
				if(this.paisExpedicionIdTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Pais Expedicion ID Transporta"));
				}
				if(this.ciuDepExpIdTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","DE ID Transporta"));
				}
				if(this.telefonoTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Telefono Transporta"));
				}
				if(this.direccionTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Direccion Transporta"));
				}
				if(this.ciudadTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Ciudad Transporta"));
				}
				if(this.departamentoTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Departamento Transporta"));
				}
				if(this.transportaVictimaDesde.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Transporta Victima Desde"));
				}
				if(this.transportaVictimaHasta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Transporta Victima Hasta"));
				}
				
				if(!UtilidadCadena.noEsVacio(this.tipoTransporte) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Tipo Transporte"));
				}
				else
					if(this.tipoTransporte.equals(ConstantesIntegridadDominio.acronimoTipoTransporteOtros))
					{
						if (!UtilidadCadena.noEsVacio(this.otroTipoTrans) && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
						{
							errores.add("",new ActionMessage("errors.required","Cuál "));
						}
					}
				
				if(this.placaVehiculoTransporta.trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					errores.add("",new ActionMessage("errors.required","Placa Vehículo Transporta"));
				}
			}
			
			if(this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				if(!UtilidadTexto.isEmpty(aseguradora))
				{
					Cuenta mundoCuenta=new Cuenta();
					Connection con=UtilidadBD.abrirConexion();
					PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
					mundoCuenta.cargar(con, paciente.getCodigoCuenta()+"");
					boolean tieneConvenioAsociadoAlPaciente=false;
					for(int i=0; i<mundoCuenta.getCuenta().getConvenios().length; i++)
					{
						if(aseguradora.equals(mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+""))
						{
							tieneConvenioAsociadoAlPaciente=true;
							break;
						}
					}
					UtilidadBD.closeConnection(con);
					if(!tieneConvenioAsociadoAlPaciente)
					{
						errores.add("",new ActionMessage("errors.notEspecific","El registro del accidente de tránsito no se puede finalizar porque la aseguradora seleccionada no esta asociada a la cuenta del paciente. Por favor verifique"));
					}
				}
				
			}
		}
		
		if(estado.equals("busquedaAvanzada") && !this.getCriteriosBusquedaMap().containsKey("idIngresoPaciente"))
		{
			errores=super.validate(mapping,request);
			
			if(this.getCriteriosBusquedaMap("fechaInicial").toString().trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("Campo Fecha Inicial vacio", new ActionMessage("errors.required","Si parametriza la fecha final entonces el campo Fecha Inicial"));
			}
			if(this.getCriteriosBusquedaMap("fechaFinal").toString().trim().equals("") && this.estadoRegistro.equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
			{
				errores.add("Campo Fecha Final vacio", new ActionMessage("errors.required","Si parametriza la fecha inicial entonces el campo Fecha Final"));
			}
			if(errores.isEmpty())
			{	
				// primero se valida el formato y que los dos esten parametrizados
				if(!this.getCriteriosBusquedaMap("fechaInicial").toString().trim().equals(""))
				{
					//se valida el formato de la fechaInicial
					if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaInicial").toString().trim()))
					{
						errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
					}
				}
				if(!this.getCriteriosBusquedaMap("fechaFinal").toString().trim().equals(""))
				{
					//se valida el formato de la fechaInicial
					if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaFinal").toString().trim()))
					{
						errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", " Final"));
					}
				}
				//si no existen errores entonces continuar con el resto de validaciones
				if(errores.isEmpty())
				{
					// la fecha inicial debe ser menor a la del sistema
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").toString().trim(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "actual "+UtilidadFecha.getFechaActual()));
					}
					//la fecha final debe ser mayor igual a la inicial
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").toString().trim(), this.getCriteriosBusquedaMap("fechaFinal").toString().trim()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "final "+this.getCriteriosBusquedaMap("fechaFinal").toString().trim()));
					}
					// la fecha final debe ser menor a la del sistema
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").toString().trim(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.getCriteriosBusquedaMap("fechaFinal"), "actual "+UtilidadFecha.getFechaActual()));
					}
				}
			}
			if(criteriosBusquedaMap.containsKey("consecutivoInicial") && criteriosBusquedaMap.containsKey("consecutivoFinal"))
			{
				if(!criteriosBusquedaMap.get("consecutivoInicial").toString().equals("") && !criteriosBusquedaMap.get("consecutivoInicial").toString().equals("null")
						&& !criteriosBusquedaMap.get("consecutivoFinal").toString().equals("") && !criteriosBusquedaMap.get("consecutivoFinal").toString().equals("null"))
				{
					try
					{
						double consecutivoInicial= Double.parseDouble(criteriosBusquedaMap.get("consecutivoInicial").toString());
						double consecutivoFinal= Double.parseDouble(criteriosBusquedaMap.get("consecutivoFinal").toString());
						if(consecutivoInicial>consecutivoFinal)
						{
							errores.add("", new ActionMessage("errors.integerMayorIgualQue", "El Registro de accidente de transito final "+criteriosBusquedaMap.get("consecutivoFinal"), "el inicial "+criteriosBusquedaMap.get("consecutivoInicial")));
						}
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.integer", "Tanto el Registro de accidente de transito inicial como final"));
					}
				}
			}
			if(!errores.isEmpty())
				this.setEstado("busquedaAvanzadaConErrores");
		}
		
		return errores;
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

	public String getAgencia()
	{
		return agencia;
	}

	public void setAgencia(String agencia)
	{
		this.agencia = agencia;
	}

	
	public String getAsegurado()
	{
		return asegurado;
	}

	public void setAsegurado(String asegurado)
	{
		this.asegurado = asegurado;
	}

	public String getAseguradora()
	{
		return aseguradora;
	}

	public void setAseguradora(String aseguradora)
	{
		this.aseguradora = aseguradora;
	}

	public String getCiudadAccidente()
	{
		return ciudadAccidente;
	}

	public void setCiudadAccidente(String ciudadAccidente)
	{
		this.ciudadAccidente = ciudadAccidente;
	}

	public String getCiudadEmpresa()
	{
		return ciudadEmpresa;
	}

	public void setCiudadEmpresa(String ciudadEmpresa)
	{
		this.ciudadEmpresa = ciudadEmpresa;
	}

	public String getCiudadReferido()
	{
		return ciudadReferido;
	}

	public void setCiudadReferido(String ciudadReferido)
	{
		this.ciudadReferido = ciudadReferido;
	}

	public String getCiudadRefiere()
	{
		return ciudadRefiere;
	}

	public void setCiudadRefiere(String ciudadRefiere)
	{
		this.ciudadRefiere = ciudadRefiere;
	}

	public String getCiuExpedicionIdConductor()
	{
		return ciuExpedicionIdConductor;
	}

	public void setCiuExpedicionIdConductor(String ciuExpedicionIdConductor)
	{
		this.ciuExpedicionIdConductor = ciuExpedicionIdConductor;
	}

	public String getCodigo()
	{
		return codigo;
	}

	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}

	public String getDepartamentoAccidente()
	{
		return departamentoAccidente;
	}

	public void setDepartamentoAccidente(String departamentoAccidente)
	{
		this.departamentoAccidente = departamentoAccidente;
	}

	public String getDepartamentoEmpresa()
	{
		return departamentoEmpresa;
	}

	public void setDepartamentoEmpresa(String departamentoEmpresa)
	{
		this.departamentoEmpresa = departamentoEmpresa;
	}

	public String getDepartamentoReferido()
	{
		return departamentoReferido;
	}

	public void setDepartamentoReferido(String departamentoReferido)
	{
		this.departamentoReferido = departamentoReferido;
	}

	public String getDepartamentoRefiere()
	{
		return departamentoRefiere;
	}

	public void setDepartamentoRefiere(String departamentoRefiere)
	{
		this.departamentoRefiere = departamentoRefiere;
	}

	public String getDepExpedicionIdConductor()
	{
		return depExpedicionIdConductor;
	}

	public void setDepExpedicionIdConductor(String depExpedicionIdConductor)
	{
		this.depExpedicionIdConductor = depExpedicionIdConductor;
	}

	public String getDireccionConductor()
	{
		return direccionConductor;
	}

	public void setDireccionConductor(String direccionConductor)
	{
		this.direccionConductor = direccionConductor;
	}

	public String getDireccionEmpresa()
	{
		return direccionEmpresa;
	}

	public void setDireccionEmpresa(String direccionEmpresa)
	{
		this.direccionEmpresa = direccionEmpresa;
	}

	public String getEmpresaTrabaja()
	{
		return empresaTrabaja;
	}

	public void setEmpresaTrabaja(String empresaTrabaja)
	{
		this.empresaTrabaja = empresaTrabaja;
	}

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public String getFechaAccidente()
	{
		return fechaAccidente;
	}

	public void setFechaAccidente(String fechaAccidente)
	{
		this.fechaAccidente = fechaAccidente;
	}

	public String getFechaConfirmacionReferido()
	{
		return fechaConfirmacionReferido;
	}

	public void setFechaConfirmacionReferido(String fechaConfirmacionReferido)
	{
		this.fechaConfirmacionReferido = fechaConfirmacionReferido;
	}

	public String getFechaFinalPoliza()
	{
		return fechaFinalPoliza;
	}

	public void setFechaFinalPoliza(String fechaFinalPoliza)
	{
		this.fechaFinalPoliza = fechaFinalPoliza;
	}


	public String getFechaInicialPoliza()
	{
		return fechaInicialPoliza;
	}

	public void setFechaInicialPoliza(String fechaInicialPoliza)
	{
		this.fechaInicialPoliza = fechaInicialPoliza;
	}

	public String getFechaReferencia()
	{
		return fechaReferencia;
	}

	public void setFechaReferencia(String fechaReferencia)
	{
		this.fechaReferencia = fechaReferencia;
	}

	public String getHoraAccidente()
	{
		return horaAccidente;
	}

	public void setHoraAccidente(String horaAccidente)
	{
		this.horaAccidente = horaAccidente;
	}


	public String getInformacionAccidente()
	{
		return informacionAccidente;
	}

	public void setInformacionAccidente(String informacionAccidente)
	{
		this.informacionAccidente = informacionAccidente;
	}

	public String getIngreso()
	{
		return ingreso;
	}

	public void setIngreso(String ingreso)
	{
		this.ingreso = ingreso;
	}

	public String getLugarAccidente()
	{
		return lugarAccidente;
	}

	public void setLugarAccidente(String lugarAccidente)
	{
		this.lugarAccidente = lugarAccidente;
	}

	public String getMarcaVehiculo()
	{
		return marcaVehiculo;
	}

	public void setMarcaVehiculo(String marcaVehiculo)
	{
		this.marcaVehiculo = marcaVehiculo;
	}

	public String getNumeroIdConductor()
	{
		return numeroIdConductor;
	}

	public void setNumeroIdConductor(String numeroIdConductor)
	{
		this.numeroIdConductor = numeroIdConductor;
	}

	public String getNumeroPoliza()
	{
		return numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza)
	{
		this.numeroPoliza = numeroPoliza;
	}

	public String getOcupante()
	{
		return ocupante;
	}

	public void setOcupante(String ocupante)
	{
		this.ocupante = ocupante;
	}

	public String getPersonaRefiere()
	{
		return personaRefiere;
	}

	public void setPersonaRefiere(String personaRefiere)
	{
		this.personaRefiere = personaRefiere;
	}

	public String getPlaca()
	{
		return placa;
	}

	public void setPlaca(String placa)
	{
		this.placa = placa;
	}

	public String getPlacaVehiculoOcaciona()
	{
		return placaVehiculoOcaciona;
	}

	public void setPlacaVehiculoOcaciona(String placaVehiculoOcaciona)
	{
		this.placaVehiculoOcaciona = placaVehiculoOcaciona;
	}

	public String getRefereidoA()
	{
		return refereidoA;
	}

	public void setRefereidoA(String refereidoA)
	{
		this.refereidoA = refereidoA;
	}

	public String getResultaLesionadoAl()
	{
		return resultaLesionadoAl;
	}

	public void setResultaLesionadoAl(String resultaLesionadoAl)
	{
		this.resultaLesionadoAl = resultaLesionadoAl;
	}

	public String getTelefonoConductor()
	{
		return telefonoConductor;
	}

	public void setTelefonoConductor(String telefonoConductor)
	{
		this.telefonoConductor = telefonoConductor;
	}

	public String getTelefonoEmpresa()
	{
		return telefonoEmpresa;
	}

	public void setTelefonoEmpresa(String telefonoEmpresa)
	{
		this.telefonoEmpresa = telefonoEmpresa;
	}

	public String getTipo()
	{
		return tipo;
	}

	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}

	public String getTipoIdConductor()
	{
		return tipoIdConductor;
	}

	public void setTipoIdConductor(String tipoIdConductor)
	{
		this.tipoIdConductor = tipoIdConductor;
	}

	public String getTipoReferencia()
	{
		return tipoReferencia;
	}

	public void setTipoReferencia(String tipoReferencia)
	{
		this.tipoReferencia = tipoReferencia;
	}


	public String getZonaAccidente()
	{
		return zonaAccidente;
	}

	public void setZonaAccidente(String zonaAccidente)
	{
		this.zonaAccidente = zonaAccidente;
	}

	public String getCiudadConductor()
	{
		return ciudadConductor;
	}

	public void setCiudadConductor(String ciudadConductor)
	{
		this.ciudadConductor = ciudadConductor;
	}

	public String getDepartamentoConductor()
	{
		return departamentoConductor;
	}

	public void setDepartamentoConductor(String departamentoConductor)
	{
		this.departamentoConductor = departamentoConductor;
	}


	public String getCodicionAccidentado()
	{
		return codicionAccidentado;
	}


	public void setCodicionAccidentado(String codicionAccidentado)
	{
		this.codicionAccidentado = codicionAccidentado;
	}


	public String getApellidoNombreDeclarante()
	{
		return apellidoNombreDeclarante;
	}


	public void setApellidoNombreDeclarante(String apellidoNombreDeclarante)
	{
		this.apellidoNombreDeclarante = apellidoNombreDeclarante;
	}


	public String getCiuExpedicionIdDeclarante()
	{
		return ciuExpedicionIdDeclarante;
	}


	public void setCiuExpedicionIdDeclarante(String ciuExpedicionIdDeclarante)
	{
		this.ciuExpedicionIdDeclarante = ciuExpedicionIdDeclarante;
	}


	public String getDepExpedicionIdDeclarante()
	{
		return depExpedicionIdDeclarante;
	}


	public void setDepExpedicionIdDeclarante(String depExpedicionIdDeclarante)
	{
		this.depExpedicionIdDeclarante = depExpedicionIdDeclarante;
	}


	public String getTipoIdDeclarante()
	{
		return tipoIdDeclarante;
	}


	public void setTipoIdDeclarante(String tipoIdDeclarante)
	{
		this.tipoIdDeclarante = tipoIdDeclarante;
	}


	public String getEstadoRegistro()
	{
		return estadoRegistro;
	}


	public void setEstadoRegistro(String estadoRegistro)
	{
		this.estadoRegistro = estadoRegistro;
	}


	public ArrayList getCiudades()
	{
		return ciudades;
	}


	public void setCiudades(ArrayList ciudades)
	{
		this.ciudades = ciudades;
	}


	public HashMap getCriteriosBusquedaMap()
	{
		return criteriosBusquedaMap;
	}

	public void setCriteriosBusquedaMap(HashMap criteriosBusquedaMap)
	{
		this.criteriosBusquedaMap = criteriosBusquedaMap;
	}

	public Object getCriteriosBusquedaMap(String key)
	{
		return criteriosBusquedaMap.get(key);
	}


	@SuppressWarnings("unchecked")
	public void setCriteriosBusquedaMap(String key,Object value)
	{
		this.criteriosBusquedaMap.put(key, value);
	}
	
	/**
	 * @return Returns the listadoMap.
	 */
	public HashMap getListadoMap() {
		return listadoMap;
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setListadoMap(String key,Object value) {
		this.listadoMap.put(key, value);
	}
	
	/**
	 * @return Returns the listadoMap.
	 */
	public Object getListadoMap(String key) {
		return listadoMap.get(key);
	}


	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setListadoMap(HashMap listadoMap) {
		this.listadoMap = listadoMap;
	}


	public String getCiuDepEmpresa()
	{
		return this.ciudadEmpresa+ConstantesBD.separadorSplit+this.departamentoEmpresa+ConstantesBD.separadorSplit+this.paisEmpresa;
	}


	public void setCiuDepEmpresa(String ciuDepEmpresa)
	{
		String[] temp=ciuDepEmpresa.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadEmpresa=temp[0];
			this.departamentoEmpresa=temp[1];
			this.paisEmpresa=temp[2];	
		}
		this.ciuDepEmpresa = ciuDepEmpresa;
	}


	public String getCiuDepAccidente()
	{
		return this.ciudadAccidente+ConstantesBD.separadorSplit+this.departamentoAccidente+ConstantesBD.separadorSplit+this.paisAccidente;
	}


	public void setCiuDepAccidente(String ciuDepAccidente)
	{
		String[] temp=ciuDepAccidente.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadAccidente=temp[0];
			this.departamentoAccidente=temp[1];
			this.paisAccidente=temp[2];
		}
		this.ciuDepAccidente = ciuDepAccidente;
	}


	public ArrayList getConvenios()
	{
		return convenios;
	}


	public void setConvenios(ArrayList convenios)
	{
		this.convenios = convenios;
	}


	public ArrayList getTiposId()
	{
		return tiposId;
	}


	public void setTiposId(ArrayList tiposId)
	{
		this.tiposId = tiposId;
	}


	public String getCiuDepConductor()
	{
		return this.ciudadConductor+ConstantesBD.separadorSplit+this.departamentoConductor+ConstantesBD.separadorSplit+this.paisConductor;
	}


	public void setCiuDepConductor(String ciuDepConductor)
	{
		String[] temp=ciuDepConductor.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadConductor=temp[0];
			this.departamentoConductor=temp[1];
			this.paisConductor=temp[2];
		}
		this.ciuDepConductor = ciuDepConductor;
	}


	public String getCiuDepIdConductor()
	{
		return this.ciuExpedicionIdConductor+ConstantesBD.separadorSplit+this.depExpedicionIdConductor;
	}


	public void setCiuDepIdConductor(String ciuDepIdConductor)
	{
		String[] temp=ciuDepIdConductor.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciuExpedicionIdConductor=temp[0];
			this.depExpedicionIdConductor=temp[1];
		}
		this.ciuDepIdConductor = ciuDepIdConductor;
	}

	public String getCiuDepExpIdProp()
	{
		return this.ciudadExpedicionIdProp+ConstantesBD.separadorSplit+this.deptoExpedicionIdProp;
	}


	public void setCiuDepExpIdProp(String ciuDepExpIdProp)
	{
		String[] temp=ciuDepExpIdProp.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadExpedicionIdProp=temp[0];
			this.deptoExpedicionIdProp=temp[1];
		}
		this.ciuDepExpIdProp = ciuDepExpIdProp;
	}

	public String getCiuDepProp()
	{
		return this.ciudadProp+ConstantesBD.separadorSplit+this.deptoProp+ConstantesBD.separadorSplit+this.paisProp;
	}


	public void setCiuDepProp(String ciuDepProp)
	{
		String[] temp=ciuDepProp.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadProp=temp[0];
			this.deptoProp=temp[1];
			this.paisProp=temp[2];
		}
		this.ciuDepProp = ciuDepProp;
	}

	
	public String getCiuDepRefiere()
	{
		return this.ciudadRefiere+ConstantesBD.separadorSplit+this.departamentoRefiere;
	}


	public void setCiuDepRefiere(String ciuDepRefiere)
	{
		String[] temp=ciuDepRefiere.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadRefiere=temp[0];
			this.departamentoRefiere=temp[1];
		}
		this.ciuDepRefiere = ciuDepRefiere;
	}


	public String getCiuDepReferido()
	{
		return this.ciudadReferido+ConstantesBD.separadorSplit+this.departamentoReferido;
	}


	public void setCiuDepReferido(String ciuDepReferido)
	{
		String[] temp=ciuDepReferido.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadReferido=temp[0];
			this.departamentoReferido=temp[1];
		}
		this.ciuDepReferido = ciuDepReferido;
	}


	public String getNumeroIdDeclarante()
	{
		return numeroIdDeclarante;
	}


	public void setNumeroIdDeclarante(String numeroIdDeclarante)
	{
		this.numeroIdDeclarante = numeroIdDeclarante;
	}


	public String getCiuDepIdDeclarante()
	{
		return this.ciuExpedicionIdDeclarante+ConstantesBD.separadorSplit+this.depExpedicionIdDeclarante;
	}


	public void setCiuDepIdDeclarante(String ciuDepIdDeclarante)
	{
		String[] temp=ciuDepIdDeclarante.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciuExpedicionIdDeclarante=temp[0];
			this.depExpedicionIdDeclarante=temp[1];
		}
		this.ciuDepIdDeclarante = ciuDepIdDeclarante;
	}

	
	public String getCiuDepTranporta()
	{
		return this.ciudadTransporta+ConstantesBD.separadorSplit+this.departamentoTransporta+ConstantesBD.separadorSplit+this.paisTransporta;
	}


	public void setCiuDepTranporta(String ciuDepTranporta)
	{
		String[] temp=ciuDepTranporta.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadTransporta=temp[0];
			this.departamentoTransporta=temp[1];
			this.paisTransporta=temp[2];
		}
		this.ciuDepTranporta = ciuDepTranporta;
	}

	public String getCiuDepExpIdTransporta()
	{
		return this.ciudadExpedicionIdTransporta+ConstantesBD.separadorSplit+this.deptoExpedicionIdTransporta;
	}


	public void setCiuDepExpIdTransporta(String ciuDepExpIdTransporta)
	{
		String[] temp=ciuDepExpIdTransporta.split(ConstantesBD.separadorSplit);
		if(temp.length>1)
		{
			this.ciudadExpedicionIdTransporta=temp[0];
			this.deptoExpedicionIdTransporta=temp[1];
		}
		this.ciuDepExpIdTransporta = ciuDepExpIdTransporta;
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
	 * @return Returns the ocultarEncabezado.
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}


	/**
	 * @param ocultarEncabezado The ocultarEncabezado to set.
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}


	/**
	 * @return Returns the codigoViaIngreso.
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}


	/**
	 * @param codigoViaIngreso The codigoViaIngreso to set.
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}


	public String getPoliza()
	{
		return poliza;
	}


	public void setPoliza(String poliza)
	{
		this.poliza = poliza;
	}


	public String getCiudadProp()
	{
		return ciudadProp;
	}


	public void setCiudadProp(String ciudadProp)
	{
		this.ciudadProp = ciudadProp;
	}

	public String getDeptoProp()
	{
		return deptoProp;
	}


	public void setDeptoProp(String deptoProp)
	{
		this.deptoProp = deptoProp;
	}


	public String getDireccionProp()
	{
		return direccionProp;
	}


	public void setDireccionProp(String direccionProp)
	{
		this.direccionProp = direccionProp;
	}


	public String getNumeroIdProp()
	{
		return numeroIdProp;
	}


	public void setNumeroIdProp(String numeroIdProp)
	{
		this.numeroIdProp = numeroIdProp;
	}


	public String getPrimerApellidoConductor()
	{
		return primerApellidoConductor;
	}


	public void setPrimerApellidoConductor(String primerApellidoConductor)
	{
		this.primerApellidoConductor = primerApellidoConductor;
	}


	public String getPrimerApellidoProp()
	{
		return primerApellidoProp;
	}


	public void setPrimerApellidoProp(String primerApellidoProp)
	{
		this.primerApellidoProp = primerApellidoProp;
	}


	public String getPrimerNombreConductor()
	{
		return primerNombreConductor;
	}


	public void setPrimerNombreConductor(String primerNombreConductor)
	{
		this.primerNombreConductor = primerNombreConductor;
	}


	public String getPrimerNombreProp()
	{
		return primerNombreProp;
	}


	public void setPrimerNombreProp(String primerNombreProp)
	{
		this.primerNombreProp = primerNombreProp;
	}


	public String getSegundoApellidoConductor()
	{
		return segundoApellidoConductor;
	}


	public void setSegundoApellidoConductor(String segundoApellidoConductor)
	{
		this.segundoApellidoConductor = segundoApellidoConductor;
	}


	public String getSegundoApellidoProp()
	{
		return segundoApellidoProp;
	}


	public void setSegundoApellidoProp(String segundoApellidoProp)
	{
		this.segundoApellidoProp = segundoApellidoProp;
	}


	public String getSegundoNombreConductor()
	{
		return segundoNombreConductor;
	}


	public void setSegundoNombreConductor(String segundoNombreConductor)
	{
		this.segundoNombreConductor = segundoNombreConductor;
	}


	public String getSegundoNombreProp()
	{
		return segundoNombreProp;
	}


	public void setSegundoNombreProp(String segundoNombreProp)
	{
		this.segundoNombreProp = segundoNombreProp;
	}


	public String getTelefonoProp()
	{
		return telefonoProp;
	}


	public void setTelefonoProp(String telefonoProp)
	{
		this.telefonoProp = telefonoProp;
	}


	public String getTipoIdProp()
	{
		return tipoIdProp;
	}


	public void setTipoIdProp(String tipoIdProp)
	{
		this.tipoIdProp = tipoIdProp;
	}


	public String getApellidoNombreTransporta()
	{
		return apellidoNombreTransporta;
	}


	public void setApellidoNombreTransporta(String apellidoNombreTransporta)
	{
		this.apellidoNombreTransporta = apellidoNombreTransporta;
	}


	public String getCiudadTransporta()
	{
		return ciudadTransporta;
	}


	public void setCiudadTransporta(String ciudadTransporta)
	{
		this.ciudadTransporta = ciudadTransporta;
	}


	public String getCiudadExpedicionIdProp()
	{
		return ciudadExpedicionIdProp;
	}


	public void setCiudadExpedicionIdProp(String ciudadExpedicionIdProp)
	{
		this.ciudadExpedicionIdProp = ciudadExpedicionIdProp;
	}


	public String getCiudadExpedicionIdTransporta()
	{
		return ciudadExpedicionIdTransporta;
	}


	public void setCiudadExpedicionIdTransporta(String ciudadExpedicionIdTransporta)
	{
		this.ciudadExpedicionIdTransporta = ciudadExpedicionIdTransporta;
	}


	public String getDeptoExpedicionIdProp()
	{
		return deptoExpedicionIdProp;
	}


	public void setDeptoExpedicionIdProp(String deptoExpedicionIdProp)
	{
		this.deptoExpedicionIdProp = deptoExpedicionIdProp;
	}


	public String getDeptoExpedicionIdTransporta()
	{
		return deptoExpedicionIdTransporta;
	}


	public void setDeptoExpedicionIdTransporta(String deptoExpedicionIdTransporta)
	{
		this.deptoExpedicionIdTransporta = deptoExpedicionIdTransporta;
	}


	public String getDepartamentoTransporta()
	{
		return departamentoTransporta;
	}


	public void setDepartamentoTransporta(String departamentoTransporta)
	{
		this.departamentoTransporta = departamentoTransporta;
	}


	public String getDireccionTransporta()
	{
		return direccionTransporta;
	}


	public void setDireccionTransporta(String direccionTransporta)
	{
		this.direccionTransporta = direccionTransporta;
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


	public String getTransportaVictimaDesde()
	{
		return transportaVictimaDesde;
	}


	public void setTransportaVictimaDesde(String transportaVictimaDesde)
	{
		this.transportaVictimaDesde = transportaVictimaDesde;
	}


	public String getTransportaVictimaHasta()
	{
		return transportaVictimaHasta;
	}


	public void setTransportaVictimaHasta(String transportaVictimaHasta)
	{
		this.transportaVictimaHasta = transportaVictimaHasta;
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
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisAccidente() {
		return paisAccidente;
	}
	
	/**
	 * 
	 * @param paisAccidente
	 */
	public void setPaisAccidente(String paisAccidente) {
		this.paisAccidente = paisAccidente;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisConductor() {
		return paisConductor;
	}
	
	/**
	 * 
	 * @param paisConductor
	 */
	public void setPaisConductor(String paisConductor) {
		this.paisConductor = paisConductor;
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
	public String getPaisExpedicionIdConductor() {
		return paisExpedicionIdConductor;
	}
	
	/**
	 * 
	 * @param paisExpedicionIdConductor
	 */
	public void setPaisExpedicionIdConductor(String paisExpedicionIdConductor) {
		this.paisExpedicionIdConductor = paisExpedicionIdConductor;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisExpedicionIdDeclarante() {
		return paisExpedicionIdDeclarante;
	}
	
	/**
	 * 
	 * @param paisExpedicionIdDeclarante
	 */
	public void setPaisExpedicionIdDeclarante(String paisExpedicionIdDeclarante) {
		this.paisExpedicionIdDeclarante = paisExpedicionIdDeclarante;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisExpedicionIdProp() {
		return paisExpedicionIdProp;
	}
	
	/**
	 * 
	 * @param paisExpedicionIdProp
	 */
	public void setPaisExpedicionIdProp(String paisExpedicionIdProp) {
		this.paisExpedicionIdProp = paisExpedicionIdProp;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisExpedicionIdTransporta() {
		return paisExpedicionIdTransporta;
	}
	
	/**
	 * 
	 * @param paisExpedicionIdTransporta
	 */
	public void setPaisExpedicionIdTransporta(String paisExpedicionIdTransporta) {
		this.paisExpedicionIdTransporta = paisExpedicionIdTransporta;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisProp() {
		return paisProp;
	}
	
	/**
	 * 
	 * @param paisProp
	 */
	public void setPaisProp(String paisProp) {
		this.paisProp = paisProp;
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
	///////////////////////////////////////////////////////////////////////////////
	//cambios por anexo 485
	public HashMap getListadoAccidentes() {
		return listadoAccidentes;
	}

	public void setListadoAccidentes(HashMap listadoAccidentes) {
		this.listadoAccidentes = listadoAccidentes;
	}
	public Object getListadoAccidentes(String key) {
		return listadoAccidentes.get(key);
	}

	public void setListadoAccidentes(String key,Object value) {
		this.listadoAccidentes.put(key, value);
	}
	
	


	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	//////////////////////////////////////////////////////////////////////////////

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
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

	public String getValidacionIngresoFacturado() {
		return validacionIngresoFacturado;
	}

	public void setValidacionIngresoFacturado(String validacionIngresoFacturado) {
		this.validacionIngresoFacturado = validacionIngresoFacturado;
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

	public String getImprimirARCHIVOPLANO() {
		return imprimirARCHIVOPLANO;
	}

	public void setImprimirARCHIVOPLANO(String imprimirARCHIVOPLANO) {
		this.imprimirARCHIVOPLANO = imprimirARCHIVOPLANO;
	}

	/**
	 * 
	 */
	public void inicializarOpcionesImpresion() 
	{
		this.imprimirARCHIVOPLANO=ConstantesBD.acronimoNo;
		this.imprimirFURIPS=ConstantesBD.acronimoNo;
		
	}

	public String getNumeroFoliosFURIPS() {
		return numeroFoliosFURIPS;
	}

	public void setNumeroFoliosFURIPS(String numeroFoliosFURIPS) {
		this.numeroFoliosFURIPS = numeroFoliosFURIPS;
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

	/**
	 * @return the permitirImprimirReporteParametroGeneral
	 */
	public String getPermitirImprimirReporteParametroGeneral() {
		return permitirImprimirReporteParametroGeneral;
	}

	/**
	 * @param permitirImprimirReporteParametroGeneral the permitirImprimirReporteParametroGeneral to set
	 */
	public void setPermitirImprimirReporteParametroGeneral(
			String permitirImprimirReporteParametroGeneral) {
		this.permitirImprimirReporteParametroGeneral = permitirImprimirReporteParametroGeneral;
	}
	
	
	
    
}
