/*
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 * 
 */
package com.princetonsa.actionform.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.consultaExterna.CitaDto;
import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 * 
 */
public class OrdenesAmbulatoriasForm extends ValidatorForm
{
	
	/**
     * Se guarda la información necesaria para guardar la justificacion de cada servicio No POS
     */
    private HashMap justificacionesServicios;
    
    
	/*  desarrollo justificacion no pos */
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	private HashMap justificacionMap=new HashMap();
	
	/**
	 * Mapa medicamento pos
	 */
	private HashMap medicamentosPosMap=new HashMap();
	
	/**
	 * Mapa medicamento no pos
	 */
	private HashMap medicamentosNoPosMap=new HashMap();
	
	/**
	 * Mapa medicamento sustituto no pos
	 */
	private HashMap sustitutosNoPosMap=new HashMap();
	
	/**
	 * Mapa diagnosticos definitivos
	 */
	private HashMap diagnosticosDefinitivos=new HashMap();
	
	/**
	 * Mapa diagnosticos presuntivos
	 */
	private HashMap diagnosticosPresuntivos=new HashMap();
	
	/**
	 * numero de justificacion
	 */
	private int numjus=0;
	
	/**
	 * Strign hiddens resultadoi de la generacin de hiddens
	 */
	private String hiddens="";
	
	/**
	 * String de articulos insertados donde solo se va a consultar la justificacion
	 */
	private String artConsultaNP="";
	
	//-------*------
    
    /**
     * 
     */
    private Vector<String> consecutivosOrdenesInsertadas1;
	
    /**
     * 
     */
    private String consecutivosOrdenesInsertadas;
    
	/**
	 * 
	 */
	private int codigoPersona=ConstantesBD.codigoNuncaValido;
	
	/**
	 * 
	 */
	private int institucion=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Variable para manejar el fluho de la funcionalidad.
	 */
	private String estado;
	
	/**
	 * mapa que contine todas las ordenes.
	 */
	private HashMap ordenes;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String index;

	/**
	 * 
	 */
	private String tipoOrden;
	
	/**
	 * 
	 */
	private String centroAtencion;
	/**
	 * 
	 */
	private String profesional;
	
	/**
	 * 
	 */
	private String especialidad;
	
	/**
	 * 
	 */
	private String numeroOrden;
	
	/**
	 * 
	 */
	private boolean urgente;
	
	/**
	 * 
	 */
	private String fechaOrden;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private String servicio;
	
	/**
	 * 
	 */
	private String finalidadServicio;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private String cantidad;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private boolean consultaExterna;
	
	/**
	 * 
	 */
	private HashMap articulos;
	
	/**
	 * 
	 */
	private String estadoOrden;

	/**
	 * 
	 */
	private boolean pyp;
	
	/**
	 * 
	 */
	private String numeroSolicitud;
	
	/**
	 * 
	 */
	private String fechaConfirma;
	
	/**
	 * 
	 */
	private String horaConfirma;
	
	/**
	 * 
	 */
	private String usuarioConfirma;
	
	/**
	 * 
	 */
	private String resultado;
	
	/**
	 * 
	 */
	private String motivoAnulacion;
	
	/**
	 * 
	 */
	private HashMap<String, Object> servicios;
	
	/**
	 * codigos de los servicios insetados
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * numero de filas existentes en el hashmap pero en el caso de servicios
	 */
	private int numeroFilasMapaCasoServicios;
	
	/**
	 * numero de filas del mapa de articulos
	 */
	private int numeroFilasMapa;
	
	/**
	 * Variable para indicar si la orden de ambulatorios se
	 * abre como sólo de consulta 
	 */
	private boolean esConsulta;
	
	/**
	 * Variable que indica si se están mostrando todas las ordenes (no solo las pendientes)
	 */
	private boolean esTodas;
	
	
	/**
	 * Variable que indica si se confirma la generacion de una orden de ambulatorios en caso de ser necesario.
	 */
	private boolean generarOrdenArticulosConfirmada;
	
	/**
	 * Variable que indica si se debe mostrar el encabezado
	 */
	private boolean fichaEpidemiologica;
	
	/**
	 * 
	 */
	private ResultadoBoolean mostrarValidacionArticulos;

	/**
	 * 
	 */
	private HashMap<String, Object> articulosConfirmacion=new HashMap<String,Object>();
	
	/**
	 * 
	 */
	private String manejoProgramacionSalas;
	
	/**
	 * Almacena la informacion de otros servicios, medicamentos o insumos
	 * */
	private String otros;
	
	/**
	 * Variable que indica si existe egreso completo para el paciente
	 * con el fin de no mostrar el boton generarSolicitud
	 */
	private boolean existeEgreso;
	
	/**
	 * Indica si un Ingreso es un Preingreso
	 */
	private boolean esPreingreso;
	
	/**
	 * login usuario
	 */
	private String loginUsuario;
	
	private String mostrarMensaje = ConstantesBD.acronimoNo;
	
	/*
	 * Variable para identificar si el usuario viene de Consulta Externa
	 */
	private boolean vieneDeConsultaExterna=false;
	
	/**
	 * Variable para el manejo de check correspondiente a control especial
	 */
	private String checkCE="";
	
	/**
	 * Variable para almacenar el centro de costo seleccionado
	 */
	private String centroCostoSel;
	
	/**
	 * Mapa para almacenar los centros de costo asociado al grupo de servicio
	 */
	private HashMap<String, Object> centrosCostoMap = new HashMap<String, Object>();
	
	/**
	 * Variable que almacena el tipo de entidad que ejecuta correspondiente al centro de costo
	 */
	private String tipoEntidadEjecuta;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	private int errorOrdenCaduca;
	
	/**
	 * 
	 */
	private int cuentaSolicitante;
	
	/**
	 * 
	 */
	private int codigoOrden;
	
	/**
	 * 
	 */
	private int centroCostoSolicitante;
	
	/**
	 * 
	 */
	private int idIngreso;
		
	/**
	 * 
	 */
	private String tipoCieDiagnostico;
	
	/**
	 * 
	 */
	private String acronimoDiagnostico;
	
	/**
	 * 
	 */
	private String activarBotonGenSolOrdAmbulatoria;
	
	/**Orden seleccionada para imprimir*/
	//private boolean seleccionado;
	/**Vector que indica cuales checkBox fueron seleccionados*/
	private String[] seleccionados;
	/**Variable que indica seleccionar todos los checkBox*/  
	private String all;
	/**Variable que indica seleccionar todos los checkBox*/
	private String checkearTodos;
	/**Varaiable que almacena el maximo de registros a mostrar por paginado*/
	private String maxPaginas;
	/**Vairiable que almacena el numero de la pagina actual*/
	private String paginaActual;
		
	//------------------------------------------XPLANNER 2008--176714---------------------------------------------------
	/**Lista de ordenes a imprimir**/	
	private ArrayList<DtoOrdenesAmbulatorias> listaFinalOrdenesImprimir;
	/**Nombre del archivo de impresion*/
	private String nombreArchivoGeneradoOriginal;
	private String nombreArchivoGeneradoCopia;
	/**Nombres Archivos A imprimir (copia , original)*/
	private ArrayList<String> listaNombreArchivos;
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.ordenes.OrdenesAmbulatoriasForm");
	//-----------------------------------------------------------------------------------------------
	
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Atributo para controlar la impresi�n de la autorizaci�n en el resumen de la orden
     */
    private boolean imprimirAutorizacion;
    
    private boolean botonGenerarSolicitud;
    
    /**Indica cuando el centro de costo de la autorizacion corresponde con el de la cuenta del paciente
     * y se debe postular sin permitir modifcar*/
    private boolean centroCostoCorrespondeAutorizacion;
    
    
    /**
     * Atributo que representa la lista de autorizaciones a imprimir
     */
    private ArrayList<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionesAutorizaciones = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
    
    
    /**	 * lista que contiene los nombres de los reportes de las autorzaciones **/
	private ArrayList<String> listaNombresReportes;

	
	/**
	 * Indica si falta informaci�n antes de pasar a la siguiente p�gina
	 */
	private String faltanInformacion;
	
	/** Lista que contiene los numeros de las ordenes ambulatorias para servicios
	 *  generadas al mismo tiempo para la impresion del resumen*/
	private ArrayList<String> listaNumOrdenServicios;
	
	/**Atributo que indica si el servicio esta pendiente por autorizar para concatenar 
	 * los Dias de Tramite Autorizacion Capitacion en la descripcion del servicio en la impresion de la orden*/
	private boolean pendienteAutorizacion;
	
	/**
	 * Flag que sirve para indicar si se muestran o no el boton nuevo
	 */
	private Boolean mostrarBotonNuevo=false;
	
	/**
	 * Flag que sirve para indicar si se muestran o no el boton volver
	 */
	private Boolean mostrarBotonVolver=false;
	
	/**
	  * Flag que sirve para indicar si se muestran o no el boton de los detalles de la orden  
	 */
	private Boolean mostrarBotonesDetalleOrdenAMB=false;
	
	
	/**
	 * flag para validar si se pientan o no los encabezados
	 */
	private Boolean quitarEncabezados=false;
	
	private List<CitaDto>listaCitasAtendidas=new ArrayList<CitaDto>(0);
	
	private String codigoCitaSeleccionada=ConstantesBD.codigoNuncaValido+"";
	
	private boolean provieneAtencionCita=false;
	
	/**
	 * bandera mostrar mensaje de exito en modificacion orden de articulos
	 */
	private boolean procesoExitoso= false;
	
	/**
	 * Mapa con la info de los articulos antes de la modificacion
	 */
	private HashMap<String,Object> articulosModificacion;
	
	/**
	 * bandera cerrar popup en caso de anulacion de orden
	 */
	private boolean procesoExitosoAnulacion = false;
	/**
	 * revisa s el usuario tiene permisos para imprimir cuando entra a esta funcionalidad por historia clinica
	 */
	private Boolean tienePermisoImprimirDetalleItemHC=false;
	
	public Boolean getTienePermisoImprimirDetalleItemHC() {
		return tienePermisoImprimirDetalleItemHC;
	}

	public void setTienePermisoImprimirDetalleItemHC(
			Boolean tienePermisoImprimirDetalleItemHC) {
		this.tienePermisoImprimirDetalleItemHC = tienePermisoImprimirDetalleItemHC;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reset ()
    {
		this.codigoOrden=ConstantesBD.codigoNuncaValido;
		this.cuentaSolicitante=ConstantesBD.codigoNuncaValido;
		this.idIngreso=ConstantesBD.codigoNuncaValido;
		this.centroCostoSolicitante=ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud="";
		this.fechaConfirma="";
		this.horaConfirma="";
		this.usuarioConfirma="";
		this.ordenes=new HashMap();
		this.ordenes.put("numRegistros","0");
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.index ="";
		this.resultado="";
		this.motivoAnulacion="";
		this.tipoOrden="";
		this.centroAtencion="";
		this.profesional="";
		this.especialidad="";
		this.numeroOrden="";
		this.urgente=false;
		this.fechaOrden="";
		this.hora="";
		this.tipoServicio="";
		this.cantidad="";
		this.observaciones="";
		this.consultaExterna=false;
		this.servicio="";
		this.finalidadServicio="";
		this.estadoOrden="";
		this.pyp=false;
		
		this.resetMapaArticulos();
		this.resetMapaServicios();
		this.esConsulta=false;
		this.esTodas = false;
		this.manejoProgramacionSalas="";
		this.generarOrdenArticulosConfirmada=false;
		this.mostrarValidacionArticulos=new ResultadoBoolean(false);
		this.articulosConfirmacion=new HashMap<String, Object>();
		this.otros = "";
		this.mostrarMensaje=ConstantesBD.acronimoNo;
		this.existeEgreso = false;
		this.esPreingreso=false;
		this.consecutivosOrdenesInsertadas1= new Vector<String>();
		this.consecutivosOrdenesInsertadas="";
		this.loginUsuario="";
		//this.vieneDeConsultaExterna = false;
		
		this.checkCE="N";
		
		resetJustificacion();
		
		this.centroCostoSel="";
		this.centrosCostoMap= new HashMap<String, Object>();
		this.tipoEntidadEjecuta="";
		this.mensaje=new ResultadoBoolean(false);
		this.errorOrdenCaduca=0;
		this.acronimoDiagnostico="";
		this.tipoCieDiagnostico="";
		this.activarBotonGenSolOrdAmbulatoria="";
		
		//this.seleccionado=false;
		//this.listaOrdenesImprimir=new ArrayList<HashMap>();
		this.listaFinalOrdenesImprimir=new ArrayList<DtoOrdenesAmbulatorias>();
		this.nombreArchivoGeneradoOriginal="";
		this.nombreArchivoGeneradoCopia="";
		this.listaNombreArchivos=new ArrayList<String>();
		
        this.linkSiguiente = "";
        this.imprimirAutorizacion=false;
        this.listaValidacionesAutorizaciones=new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
        this.listaNombresReportes = new ArrayList<String>();
        this.botonGenerarSolicitud=false;
        this.centroCostoCorrespondeAutorizacion=false;
        this.faltanInformacion = "N";
        this.pendienteAutorizacion	= false;
        this.mostrarBotonNuevo=false;
    	this.mostrarBotonVolver=false;
    	this.mostrarBotonesDetalleOrdenAMB=false;
    	//this.quitarEncabezados=false;
    	//this.codigoCitaSeleccionada=ConstantesBD.codigoNuncaValido+"";
    	//this.provieneAtencionCita=false;
    	this.procesoExitoso = false;
    	this.articulosModificacion = new HashMap<String, Object>();
    	this.procesoExitosoAnulacion = false;
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void resetOrdenesRemoto ()
    {
		this.codigoOrden=ConstantesBD.codigoNuncaValido;
		this.cuentaSolicitante=ConstantesBD.codigoNuncaValido;
		this.idIngreso=ConstantesBD.codigoNuncaValido;
		this.centroCostoSolicitante=ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud="";
		this.fechaConfirma="";
		this.horaConfirma="";
		this.usuarioConfirma="";
		this.ordenes=new HashMap();
		this.ordenes.put("numRegistros","0");
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.index ="";
		this.resultado="";
		this.motivoAnulacion="";
		this.tipoOrden="";
		this.centroAtencion="";
		this.profesional="";
		this.especialidad="";
		this.numeroOrden="";
		this.urgente=false;
		this.fechaOrden="";
		this.hora="";
		this.tipoServicio="";
		this.cantidad="";
		this.observaciones="";
		this.consultaExterna=false;
		this.servicio="";
		this.finalidadServicio="";
		this.estadoOrden="";
		this.pyp=false;
		
		this.resetMapaArticulos();
		this.resetMapaServicios();
		this.esConsulta=false;
		this.esTodas = false;
		this.manejoProgramacionSalas="";
		this.generarOrdenArticulosConfirmada=false;
		this.mostrarValidacionArticulos=new ResultadoBoolean(false);
		this.articulosConfirmacion=new HashMap<String, Object>();
		this.otros = "";
		this.mostrarMensaje=ConstantesBD.acronimoNo;
		this.existeEgreso = false;
		this.esPreingreso=false;
		this.consecutivosOrdenesInsertadas1= new Vector<String>();
		this.consecutivosOrdenesInsertadas="";
		this.loginUsuario="";
		//this.vieneDeConsultaExterna = false;
		
		this.checkCE="N";
		
		resetJustificacion();
		
		this.centroCostoSel="";
		this.centrosCostoMap= new HashMap<String, Object>();
		this.tipoEntidadEjecuta="";
		this.mensaje=new ResultadoBoolean(false);
		this.errorOrdenCaduca=0;
		this.acronimoDiagnostico="";
		this.tipoCieDiagnostico="";
		this.activarBotonGenSolOrdAmbulatoria="";
		
		//this.seleccionado=false;
		//this.listaOrdenesImprimir=new ArrayList<HashMap>();
		this.listaFinalOrdenesImprimir=new ArrayList<DtoOrdenesAmbulatorias>();
		this.nombreArchivoGeneradoOriginal="";
		this.nombreArchivoGeneradoCopia="";
		this.listaNombreArchivos=new ArrayList<String>();
		
        this.linkSiguiente = "";
        this.imprimirAutorizacion=false;
        this.listaValidacionesAutorizaciones=new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
        this.listaNombresReportes = new ArrayList<String>();
        this.botonGenerarSolicitud=false;
        this.centroCostoCorrespondeAutorizacion=false;
        this.faltanInformacion = "N";
        this.pendienteAutorizacion	= false;
        //this.codigoCitaSeleccionada=ConstantesBD.codigoNuncaValido+"";
    	//this.provieneAtencionCita=false;
    }
	
	/**
	 * Inicializa la lista de los numeros de la orden ambulatoria
	 * generada para los servicios al mismo tiempo 
	 */
	public void resetNumerosOrdenesServicio()
	{
	   this.listaNumOrdenServicios=new ArrayList<String>();    
	}
	
	/**
	 * Incializa los checks de la lista de ordenes
	 */
	/*public void resetCheckeados ()
	{
		this.seleccionado=false;
	}*/

	/**
	 * @return the codigoOrden
	 */
	public int getCodigoOrden() {
		return codigoOrden;
	}

	/**
	 * @param codigoOrden the codigoOrden to set
	 */
	public void setCodigoOrden(int codigoOrden) {
		this.codigoOrden = codigoOrden;
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
	 * @return the centroCostoSolicitante
	 */
	public int getCentroCostoSolicitante() {
		return centroCostoSolicitante;
	}

	/**
	 * @param centroCostoSolicitante the centroCostoSolicitante to set
	 */
	public void setCentroCostoSolicitante(int centroCostoSolicitante) {
		this.centroCostoSolicitante = centroCostoSolicitante;
	}

	public int getErrorOrdenCaduca() {
		return errorOrdenCaduca;
	}

	public void setErrorOrdenCaduca(int errorOrdenCaduca) {
		this.errorOrdenCaduca = errorOrdenCaduca;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}

	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}

	public HashMap<String, Object> getCentrosCostoMap() {
		return centrosCostoMap;
	}
	
	public void setCentrosCostoMap(HashMap<String, Object> centrosCostoMap) {
		this.centrosCostoMap = centrosCostoMap;
	}
	
	public Object getCentrosCostoMap(String key) {
		return centrosCostoMap.get(key);
	}
	
	public void setCentrosCostoMap(String key, Object value){
		this.centrosCostoMap.put(key, value);		
	}
	
	public String getCentroCostoSel() {
		return centroCostoSel;
	}
	
	public void setCentroCostoSel(String centroCostoSel) {
		this.centroCostoSel = centroCostoSel;
	}
	
	/**
	 * 
	 *
	 */
	public void resetMapaServicios()
	{
		this.servicios=new HashMap<String, Object>();
		this.servicios.put("numRegistros","0");
		this.numeroFilasMapaCasoServicios=0;
		this.otros = "";
	}
	
	/**
	 * Reset de las variables del formato de justificacion no pos de servicios y articulos.
	 */
	public void resetJustificacion(){
		
		// Servicios
		this.justificacionesServicios=new HashMap();
		
		//Articulos
		this.justificacionMap = new HashMap();
		this.medicamentosPosMap=new HashMap();
		this.medicamentosNoPosMap=new HashMap();
		this.sustitutosNoPosMap=new HashMap();
		this.diagnosticosDefinitivos=new HashMap();
		this.diagnosticosPresuntivos=new HashMap();
		this.numjus=0;
		this.hiddens="";
		this.artConsultaNP="";
	}
	
	public void resetMapaArticulos()
	{
		this.articulos=new HashMap();
		this.articulos.put("numRegistros","0");
		this.numeroFilasMapa=0;
		this.otros = "";
	}

		
	/**
	 * reset nuevo
	 *
	 */
	public void resetNuevo()
	{
		this.tipoOrden="";
		this.especialidad="";
		this.observaciones="";
		this.codigosServiciosInsertados="";
		this.otros = "";
	}
	
	
	public void resetMensaje()
	{
		this.mostrarValidacionArticulos=new ResultadoBoolean(false);
	}
	
	/**
	 * reset Check
	 *
	 */
	public void resetCheck(){
	    this.checkCE = "N";
	}
	
	/**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		Connection con = null;
		
		try {

			con = UtilidadBD.abrirConexion();
			
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

			if(estado.equals("ingresarOrden"))
			{
				resetJustificacion();
			}
			if(estado.equals("guardarOrden"))
			{	
				HttpSession session=request.getSession();	
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				errores=super.validate(mapping,request);

				//validaciones comunes  para los dos tipos de orden
				if(this.getEspecialidad().equals(""))
				{
					errores.add("", new ActionMessage("errors.required","La Especialidad que Ordena")); 
				}

				//validaciones de articulos
				if(this.getTipoOrden().equals(ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos+""))
				{
					int cantidad=0, duracionTratamiento=0; //frecuencia=0 
					String frecuencia="";
					//se valida la seccion de medicamentos

					if((!articulos.containsKey("numRegistros") || (articulos.containsKey("numRegistros")) && articulos.get("numRegistros").equals("0"))
							&& (otros.equals("") || otros.length() <= 0))
					{
						errores.add("", new ActionMessage("errors.notEspecific","Es Requerido por lo menos Uno de los Siguientes Campos: Medicamento(s), Insumo(s) u Otros Medicamentos/Insumos "));
					}
					else if (articulos.containsKey("numRegistros"))
					{
						int numRegistros= Utilidades.convertirAEntero(this.getArticulos("numRegistros").toString());
						boolean centinelaEntroError=false;
						int contadorRegistrosNoEliminados=0;
						if(numRegistros>0)					
						{
							//primero validamos los MEDICAMENTOS
							for(int w=0; w<numRegistros; w++)
							{
								if(UtilidadTexto.getBoolean(this.getArticulos("medicamento_"+w).toString()))
								{	
									if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+w).toString()))
									{	
										//******** JUSTIFICACIÓN NO POS *******
										if(articulos.get("tipoPosArticulo_"+w).equals("NOPOS"))
										{
											if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(this.articulos.get("articulo_"+w)+"")) )
											{
												if(UtilidadCadena.noEsVacio(this.getArticulos("unidosis_"+w).toString())&&UtilidadCadena.noEsVacio(this.getArticulos("tipofrecuencia_"+w).toString()))
												{
													this.setArticulos("insertarJustNP_"+this.getArticulos("articulo_"+w),UtilidadInventarios.validarTiempoTratamiento(con, Utilidades.convertirAEntero(this.getArticulos("articulo_"+w)+""), getArticulos("unidosis_"+w)+"", getArticulos("dosis_"+w)+"",getArticulos("tipofrecuencia_"+w)+"", getArticulos("tipofrecuencia_"+w)+"", paciente));
													if(UtilidadTexto.getBoolean(this.getArticulos("insertarJustNP_"+this.getArticulos("articulo_"+w))))
													{
														if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
																&& !justificacionMap.containsKey(this.getArticulos("articulo_"+w)+"_yajustifico") 
																&& !justificacionMap.containsKey(this.getArticulos("articulo_"+w)+"_pendiente") 
																&& artConsultaNP.indexOf(" "+this.getArticulos("articulo_"+w)+" ")<0	
														)
														{
															errores.add("Justificacion", new ActionMessage("errors.required", "Justificacion del Medicamento - "+this.getArticulos("articulo_"+w)));
														}
		
														if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
																&& artConsultaNP.indexOf(" "+this.getArticulos("articulo_"+w)+" ")<0)
														{
															errores.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+this.getArticulos("articulo_"+w)+" Ya que no cumple con la validacion médico especialista"));								
														}
													}
												}
											}
										}	
										//**************************************

										if(!UtilidadCadena.noEsVacio(this.getArticulos("dosis_"+w).toString()))
										{
											errores.add("", new ActionMessage("errors.required","La dosis del artículo "+this.getArticulos("articulo_"+w)));
										}
										if(!UtilidadCadena.noEsVacio(this.getArticulos("unidosis_"+w).toString()))
										{
											errores.add("", new ActionMessage("errors.required","La Unidosis del artículo "+this.getArticulos("articulo_"+w)));
										}
										try
										{
											//frecuencia=Utilidades.convertirAEntero(this.getArticulos("frecuencia_"+w).toString());
											frecuencia = this.getArticulos("frecuencia_"+w).toString();

										}
										catch (Exception e) 
										{
											errores.add("", new ActionMessage("errors.integerMayorQue", "La frecuencia del artículo "+this.getArticulos("articulo_"+w),"0"));
											centinelaEntroError=true;
										}
										if(!centinelaEntroError)
										{
											if(Utilidades.convertirAEntero(frecuencia)<1)
											{
												errores.add("", new ActionMessage("errors.integerMayorQue", "La frecuencia del artículo "+this.getArticulos("articulo_"+w),"0"));
											}
										}
										if(!UtilidadCadena.noEsVacio(this.getArticulos("tipofrecuencia_"+w).toString()))
										{
											errores.add("", new ActionMessage("errors.required","El tipo de frecuencia [horas-minutos] del artículo "+this.getArticulos("articulo_"+w)));
										}
										if(!UtilidadCadena.noEsVacio(this.getArticulos("via_"+w).toString()))
										{
											errores.add("", new ActionMessage("errors.required","La vía del artículo "+this.getArticulos("articulo_"+w)));
										}
										if(!UtilidadCadena.noEsVacio(this.getArticulos("duraciontratamiento_"+w).toString()))
										{
											errores.add("", new ActionMessage("errors.required","Los Días de Tratamiento del artículo "+this.getArticulos("articulo_"+w)));
										}
										centinelaEntroError=false;
										try
										{
											cantidad = Utilidades.convertirAEntero(this.getArticulos("cantidad_"+w).toString());
										}
										catch (Exception e) 
										{
											errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
											centinelaEntroError=true;
										}
										if(!centinelaEntroError)
										{
											if(cantidad<1)
											{
												errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
											}
										}
										centinelaEntroError=false;

										if(!this.getArticulos("duraciontratamiento_"+w).toString().trim().equals(""))
										{	
											try
											{
												duracionTratamiento = Utilidades.convertirAEntero(this.getArticulos("duraciontratamiento_"+w).toString());
											}
											catch (Exception e) 
											{
												errores.add("", new ActionMessage("errors.integerMayorQue", "La Duración del tratamiento para el artículo "+this.getArticulos("articulo_"+w),"0"));
												centinelaEntroError=true;
											}
											if(!centinelaEntroError)
											{
												if(duracionTratamiento<1)
												{
													errores.add("", new ActionMessage("errors.integerMayorQue", "La Duración del tratamiento para el artículo "+this.getArticulos("articulo_"+w),"0"));
												}
											}
										}
										contadorRegistrosNoEliminados++;
									}
								}
							}
							//luego validamos los insumos
							for(int w=0; w<numRegistros; w++)
							{
								if(!UtilidadTexto.getBoolean(this.getArticulos("medicamento_"+w).toString()))
								{	
									if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+w).toString()))
									{	
										//******** JUSTIFICACIÓN NO POS *******
										if(articulos.get("tipoPosArticulo_"+w).equals("NOPOS"))
										{
											if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(this.articulos.get("articulo_"+w)+"")) )
											{
												if (!UtilidadTexto.getBoolean(this.getArticulos("justificado_"+this.getArticulos("articulo_"+w))+""))
												{
													errores.add("Justificacion", new ActionMessage("errors.required", "Justificacion del Insumo - "+this.getArticulos("articulo_"+w)));
												}

												if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
														&& artConsultaNP.indexOf(" "+this.getArticulos("articulo_"+w)+" ")<0)
												{
													errores.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+this.getArticulos("articulo_"+w)+" Ya que no cumple con la validacion médico especialista"));								
												}
											}
										}	
										//**************************************
										try
										{
											cantidad = Utilidades.convertirAEntero(this.getArticulos("cantidad_"+w).toString());
										}
										catch (Exception e) 
										{
											errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
											centinelaEntroError=true;
										}
										if(!centinelaEntroError)
										{
											if(cantidad<1)
											{
												errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
											}
										}
										contadorRegistrosNoEliminados++;
									}
								}	
							}
							if(contadorRegistrosNoEliminados<1)
							{
								errores.add("", new ActionMessage("errors.required","El/los medicamento(s) - insumo(s)"));
							}
						}
					}		
				}
				//validaciones de servicios
				else if(this.getTipoOrden().equals(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios+""))
				{
					int cantidad=0;
					boolean centinelaEntroError=false;
					int numJustPendientes = 0;

					int numRegistros = 0;

					if(servicios.containsKey("numRegistros"))
						numRegistros = Utilidades.convertirAEntero(this.getServicios("numRegistros").toString());

					int contadorRegistrosNoEliminados=0;
					if(numRegistros<1  && otros.equals(""))
					{
						errores.add("", new ActionMessage("errors.notEspecific","Es Requerido por lo menos Uno de los Siguientes Campos: Servicios u Otros Servicios "));
					}
					else if(numRegistros>0)
					{
						for(int w=0; w<numRegistros; w++)
						{
							if(!UtilidadTexto.getBoolean(this.getServicios("fueEliminadoServicio_"+w).toString()))
							{	

								if (this.getServicios().containsKey("justificar_"+w)){
									if (UtilidadTexto.getBoolean(this.getServicios("justificar_"+w).toString()) && !(UtilidadTexto.getBoolean(this.getServicios("fueEliminadoServicio_"+w)+""))){
										if(!UtilidadTexto.getBoolean(this.getJustificacionesServicios().get(this.getServicios().get("codigo_"+w)+"_yajustifico"))){
											errores.add("No se ha diligenciado el formato de justificación No POS", new ActionMessage("errors.required","Justificación No POS para el servicio "+this.getServicios("descripcionServicio_"+w)));
										}
									}
									if (this.getServicios("justificar_"+w).toString().equals("pendiente")){
										numJustPendientes++;
									}
								}
								if (numJustPendientes==1)
									errores.add("No puede solicitar el servicio No POS", new ActionMessage("errors.warnings","No puede solicitar el servicio No POS "+this.getServicios("descripcionServicio_"+w)+" Ya que no cumple con la validacion médico especialista"));


								//solo se valida la finalidad cuando tipo servicio es DIFERENTE A CONSULTA
								if(!Utilidades.getTipoServicio(this.getServicios("codigo_"+w).toString()).equals(ConstantesBD.codigoServicioInterconsulta+""))
								{	
									if(!UtilidadCadena.noEsVacio(this.getServicios("finalidad_"+w).toString()))
									{
										errores.add("", new ActionMessage("errors.required","La finalidad del servicio con código cups "+this.getServicios("codigoCups_"+w)));
									}
								}	

								try
								{
									cantidad = Utilidades.convertirAEntero(this.getServicios("cantidad_"+w).toString());
								}
								catch (Exception e) 
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del servicio con código cups "+this.getServicios("codigoCups_"+w),"0"));
									centinelaEntroError=true;
								}
								if(!centinelaEntroError)
								{
									if(cantidad<1)
									{
										errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del servicio con código cups "+this.getServicios("codigoCups_"+w),"0"));
									}
								}
								contadorRegistrosNoEliminados++;
							}	
						}

						if(contadorRegistrosNoEliminados<1)
						{
							errores.add("", new ActionMessage("errors.required","El/los servicio(s)"));
						}					
					}

				}
			}

			else if(estado.equals("guardarModificacionesDetalleOrdenArticulos"))
			{
				//alejo
				int cantidad=0; //, frecuencia=0;
				String frecuencia="";
				//se valida la seccion de medicamentos


				int numRegistros= 0; 

				if(articulos.containsKey("numRegistros"))					
					numRegistros = Utilidades.convertirAEntero(this.getArticulos("numRegistros").toString());

				boolean centinelaEntroError=false;
				int contadorRegistrosNoEliminados=0;

				if(numRegistros<1 && (otros.equals("") || otros.length() <= 0))
				{
					errores.add("", new ActionMessage("errors.notEspecific","Es Requerido por lo menos Uno de los Siguientes Campos: Medicamento(s), Insumo(s) u Otros Medicamentos/Insumos "));				
				}
				else
				{
					//primero validamos los MEDICAMENTOS
					for(int w=0; w<numRegistros; w++)
					{
						if(UtilidadTexto.getBoolean(this.getArticulos("medicamento_"+w).toString()))
						{	
							if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+w).toString()))
							{
								if(!UtilidadCadena.noEsVacio(this.getArticulos("dosis_"+w).toString()))
								{
									errores.add("", new ActionMessage("errors.required","La dosis del artículo "+this.getArticulos("articulo_"+w)));
								}
								if(!UtilidadCadena.noEsVacio(this.getArticulos("unidosis_"+w).toString()))
								{
									errores.add("", new ActionMessage("errors.required","La Unidosis del artículo "+this.getArticulos("articulo_"+w)));
								}
								try
								{
									frecuencia = this.getArticulos("frecuencia_"+w).toString();
								}
								catch (Exception e) 
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La frecuencia del artículo "+this.getArticulos("articulo_"+w),"0"));
									centinelaEntroError=true;
								}
								if(!centinelaEntroError)
								{
									if(Utilidades.convertirAEntero(frecuencia)<1)
									{
										errores.add("", new ActionMessage("errors.integerMayorQue", "La frecuencia del artículo "+this.getArticulos("articulo_"+w),"0"));
									}
								}
								if(!UtilidadCadena.noEsVacio(this.getArticulos("tipofrecuencia_"+w).toString()))
								{
									errores.add("", new ActionMessage("errors.required","El tipo de frecuencia [horas-minutos] del artículo "+this.getArticulos("articulo_"+w)));
								}
								if(!UtilidadCadena.noEsVacio(this.getArticulos("via_"+w).toString()))
								{
									errores.add("", new ActionMessage("errors.required","La vía del artículo "+this.getArticulos("articulo_"+w)));
								}
								if(!UtilidadCadena.noEsVacio(this.getArticulos("duraciontratamiento_"+w).toString()))
								{
									errores.add("", new ActionMessage("errors.required","Los Días de Tratamiento del artículo "+this.getArticulos("articulo_"+w)));
								}
								centinelaEntroError=false;
								try
								{
									cantidad = Utilidades.convertirAEntero(this.getArticulos("cantidad_"+w).toString());
								}
								catch (Exception e) 
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
									centinelaEntroError=true;
								}
								if(!centinelaEntroError)
								{
									if(cantidad<1)
									{
										errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
									}
								}
								contadorRegistrosNoEliminados++;
							}
						}	
					}
					//luego validamos los insumos
					for(int w=0; w<numRegistros; w++)
					{
						if(!UtilidadTexto.getBoolean(this.getArticulos("medicamento_"+w).toString()))
						{	
							if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+w).toString()))
							{	
								try
								{
									cantidad = Utilidades.convertirAEntero(this.getArticulos("cantidad_"+w).toString());
								}
								catch (Exception e) 
								{
									errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
									centinelaEntroError=true;
								}
								if(!centinelaEntroError)
								{
									if(cantidad<1)
									{
										errores.add("", new ActionMessage("errors.integerMayorQue", "La cantidad del artículo "+this.getArticulos("articulo_"+w),"0"));
									}
								}
								contadorRegistrosNoEliminados++;
							}
						}	
					}
					if(contadorRegistrosNoEliminados<1)
					{
						errores.add("", new ActionMessage("errors.required","El/los medicamento(s) - insumo(s)"));
					}
				}

				if(!errores.isEmpty())
				{
					this.estado= "continuarDetalleOrdenArticulo";
				}
				
			} else if(estado.equals("guardarModOrdenArticulos")){
				this.setProcesoExitoso(false);
				int contArticulos = 0;
				
				for(int i=0 ; i < Integer.parseInt(this.getArticulos("numRegistros").toString()) ; i++){
					if(UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+i)+"") 
							&& UtilidadTexto.getBoolean(this.getArticulos("estabd_"+i)+"")){
						contArticulos++;
					}
				}
				
				//Validacion articulos requeridos
				if(contArticulos == Integer.parseInt(this.getArticulos("numRegistros").toString())){
					errores.add("", new ActionMessage("errores.ordenesAmbulatorias.requeridoArticulos"));
				}
			}
			
		} catch (Exception e) {
			Log4JManager.error("Error ValidateForm : " + e);
			errores.add("", new ActionMessage("errors.problemasGenericos", "en la validaci�n"));
		} finally {
			try {
				UtilidadBD.closeConnection(con);
			} catch (Exception e) {
				Log4JManager.error("Error cerrando Conexi�n :" + e);
			}
		}
    	return errores;
	}

	   
	
	public String getCheckCE() {
		return checkCE;
	}
	public void setCheckCE(String checkCE) {
		this.checkCE = checkCE;
	}
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	public HashMap getOrdenes() {
		return ordenes;
	}

	public void setOrdenes(HashMap ordenes) {
		this.ordenes = ordenes;
	}


	public Object getOrdenes(String key) {
		return ordenes.get(key);
	}

	public void setOrdenes(String key, Object value) 
	{
		this.ordenes.put(key,value);
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public boolean isConsultaExterna() {
		return consultaExterna;
	}

	public void setConsultaExterna(boolean consultaExterna) {
		this.consultaExterna = consultaExterna;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getFechaOrden() {
		return fechaOrden;
	}

	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getProfesional() {
		return profesional;
	}

	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	public String getTipoOrden() {
		return tipoOrden;
	}

	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public HashMap getArticulos() {
		return articulos;
	}

	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	

	public Object getArticulos(String key) {
		return articulos.get(key);
	}

	public void setArticulos(String key,Object value) {
		this.articulos.put(key,value);
	}

	public String getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(String estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public boolean isPyp() {
		return pyp;
	}

	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}

	public String getFechaConfirma() {
		return fechaConfirma;
	}

	public void setFechaConfirma(String fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	public String getHoraConfirma() {
		return horaConfirma;
	}

	public void setHoraConfirma(String horaConfirma) {
		this.horaConfirma = horaConfirma;
	}

	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getUsuarioConfirma() {
		return usuarioConfirma;
	}

	public void setUsuarioConfirma(String usuarioConfirma) {
		this.usuarioConfirma = usuarioConfirma;
	}

	public String getFinalidadServicio() {
		return finalidadServicio;
	}

	public void setFinalidadServicio(String finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}

	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public HashMap getServicios() {
		return servicios;
	}

	public void setServicios(HashMap servicios) {
		this.servicios = servicios;
	}

	public Object getServicios(String key) {
		return servicios.get(key);
	}

	public void setServicios(String key,Object value) {
		this.servicios.put(key,value);
	}

	/**
	 * @return Returns the codigosServiciosInsertados.
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	
	/**
	 * Retorna el codigo del servicio unicamente sin ning�n c�racter en especial la coma (,).
	 * Este metodo se crea ya que el m�todo getCodigosServiciosInsertados() retorna el c�digo del servicio y una coma. Ej: "1234,".
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @return Returns the codigosServiciosInsertados.
	 */
	public String getCodigosServiciosInsertadosValidado() 
	{
		String codigoServicio = "";
		
		if(!UtilidadTexto.isEmpty(codigosServiciosInsertados))
		{
			String[] codigosServicio = codigosServiciosInsertados.split(",");
			if(codigosServicio.length >= 1){
				codigoServicio = codigosServicio[0];
			}
			else{
				codigoServicio = codigosServiciosInsertados;
			}
		}
		return codigoServicio;
	}
	
	
	
	/**
	 * @param codigosServiciosInsertados The codigosServiciosInsertados to set.
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	/**
	 * @return Returns the numeroFilasMapaCasoServicios.
	 */
	public int getNumeroFilasMapaCasoServicios() 
	{
		return numeroFilasMapaCasoServicios;
	}

	/**
	 * @param numeroFilasMapaCasoServicios The numeroFilasMapaCasoServicios to set.
	 */
	public void setNumeroFilasMapaCasoServicios(int numeroFilasMapaCasoServicios) {
		this.numeroFilasMapaCasoServicios = numeroFilasMapaCasoServicios;
	}

	/**
	 * @return Returns the numeroFilasMapa.
	 */
	public int getNumeroFilasMapa() {
		return numeroFilasMapa;
	}

	/**
	 * @param numeroFilasMapa The numeroFilasMapa to set.
	 */
	public void setNumeroFilasMapa(int numeroFilasMapa) {
		this.numeroFilasMapa = numeroFilasMapa;
	}

	/**
	 * @return Retorna the esConsulta.
	 */
	public boolean getEsConsulta()
	{
		return esConsulta;
	}

	/**
	 * @param esConsulta The esConsulta to set.
	 */
	public void setEsConsulta(boolean esConsulta)
	{
		this.esConsulta = esConsulta;
	}

	public int getCodigoPersona()
	{
		return codigoPersona;
	}

	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
	}

	public int getInstitucion()
	{
		return institucion;
	}

	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}

	/**
	 * @return the esTodas
	 */
	public boolean isEsTodas() {
		return esTodas;
	}

	/**
	 * @param esTodas the esTodas to set
	 */
	public void setEsTodas(boolean esTodas) {
		this.esTodas = esTodas;
	}

	/**
	 * @return the manejoProgramacionSalas
	 */
	public String getManejoProgramacionSalas() {
		return manejoProgramacionSalas;
	}

	/**
	 * @param manejoProgramacionSalas the manejoProgramacionSalas to set
	 */
	public void setManejoProgramacionSalas(String manejoProgramacionSalas) {
		this.manejoProgramacionSalas = manejoProgramacionSalas;
	}

	public HashMap<String, Object> getArticulosConfirmacion() {
		return articulosConfirmacion;
	}

	public void setArticulosConfirmacion(
			HashMap<String, Object> articulosConfirmacion) {
		this.articulosConfirmacion = articulosConfirmacion;
	}

	public boolean isGenerarOrdenArticulosConfirmada() {
		return generarOrdenArticulosConfirmada;
	}

	public void setGenerarOrdenArticulosConfirmada(
			boolean generarOrdenArticulosConfirmada) {
		this.generarOrdenArticulosConfirmada = generarOrdenArticulosConfirmada;
	}

	public ResultadoBoolean getMostrarValidacionArticulos() {
		return mostrarValidacionArticulos;
	}

	public void setMostrarValidacionArticulos(
			ResultadoBoolean mostrarValidacionArticulos) {
		this.mostrarValidacionArticulos = mostrarValidacionArticulos;
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
	 * @return the otros
	 */
	public String getOtros() {
		return otros;
	}

	/**
	 * @param otros the otros to set
	 */
	public void setOtros(String otros) {
		this.otros = otros;
	}

	public String getMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(String mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * @return the existeEgreso
	 */
	public boolean isExisteEgreso() {
		return existeEgreso;
	}

	/**
	 * @param existeEgreso the existeEgreso to set
	 */
	public void setExisteEgreso(boolean existeEgreso) {
		this.existeEgreso = existeEgreso;
	}

	public boolean isEsPreingreso() {
		return esPreingreso;
	}

	public void setEsPreingreso(boolean esPreingreso) {
		this.esPreingreso = esPreingreso;
	}

	/**
	 * @return the consecutivosOrdenesInsertadas
	 */
	public Vector<String> getConsecutivosOrdenesInsertadas1() {
		return consecutivosOrdenesInsertadas1;
	}

	/**
	 * @param consecutivosOrdenesInsertadas the consecutivosOrdenesInsertadas to set
	 */
	public void setConsecutivosOrdenesInsertadas1(
			Vector<String> consecutivosOrdenesInsertadas1) {
		this.consecutivosOrdenesInsertadas1 = consecutivosOrdenesInsertadas1;
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
	 * 
	 * @param key
	 * @param value
	 */
    public void setJustificacionMap(String key, Object value) 
    {
    	this.justificacionMap.put(key, value);
    }

	/**
	 * @return the diagnosticosDefinitivos
	 */
	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * @return the diagnosticosPresuntivos
	 */
	public HashMap getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
	 */
	public void setDiagnosticosPresuntivos(HashMap diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * @return the medicamentosNoPosMap
	 */
	public HashMap getMedicamentosNoPosMap() {
		return medicamentosNoPosMap;
	}

	/**
	 * @param medicamentosNoPosMap the medicamentosNoPosMap to set
	 */
	public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
		this.medicamentosNoPosMap = medicamentosNoPosMap;
	}

	/**
	 * @return the medicamentosPosMap
	 */
	public HashMap getMedicamentosPosMap() {
		return medicamentosPosMap;
	}
	/**
	 * @param medicamentosPosMap the medicamentosPosMap to set
	 */
	public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
		this.medicamentosPosMap = medicamentosPosMap;
	}

	
	public void setMedicamentosPosMap(String key, Object value) {
		this.medicamentosPosMap.put(key, value);
	}
	
	public Object getMedicamentosPosMap(String key) {
        return medicamentosPosMap.get(key);
    }
	
	
	/**
	 * @return the sustitutosNoPosMap
	 */
	public HashMap getSustitutosNoPosMap() {
		return sustitutosNoPosMap;
	}

	/**
	 * @param sustitutosNoPosMap the sustitutosNoPosMap to set
	 */
	public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
		this.sustitutosNoPosMap = sustitutosNoPosMap;
	}

	/**
	 * @return the numjus
	 */
	public int getNumjus() {
		return numjus;
	}

	/**
	 * @param numjus the numjus to set
	 */
	public void setNumjus(int numjus) {
		this.numjus = numjus;
	}

	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		return hiddens;
	}

	/**
	 * @return the artConsultaNP
	 */
	public String getArtConsultaNP() {
		return artConsultaNP;
	}

	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}

	/**
	 * @param artConsultaNP the artConsultaNP to set
	 */
	public void setArtConsultaNP(String artConsultaNP) {
		this.artConsultaNP = artConsultaNP;
	}

	/**
	 * @return the vieneDeConsultaExterna
	 */
	public boolean isVieneDeConsultaExterna() {
		return vieneDeConsultaExterna;
	}

	/**
	 * @param vieneDeConsultaExterna the vieneDeConsultaExterna to set
	 */
	public void setVieneDeConsultaExterna(boolean vieneDeConsultaExterna) {
		this.vieneDeConsultaExterna = vieneDeConsultaExterna;
	}

	public String getTipoCieDiagnostico() {
		return tipoCieDiagnostico;
	}

	public void setTipoCieDiagnostico(String tipoCieDiagnostico) {
		this.tipoCieDiagnostico = tipoCieDiagnostico;
	}

	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}

	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}

	public void setFichaEpidemiologica(boolean fichaEpidemiologica) {
		this.fichaEpidemiologica = fichaEpidemiologica;
	}

	public boolean isFichaEpidemiologica() {
		return fichaEpidemiologica;
	}

	public void setConsecutivosOrdenesInsertadas(
			String consecutivosOrdenesInsertadas) {
		this.consecutivosOrdenesInsertadas = consecutivosOrdenesInsertadas;
	}

	public String getConsecutivosOrdenesInsertadas() {
		return consecutivosOrdenesInsertadas;
	}

	public void setActivarBotonGenSolOrdAmbulatoria(
			String activarBotonGenSolOrdAmbulatoria) {
		this.activarBotonGenSolOrdAmbulatoria = activarBotonGenSolOrdAmbulatoria;
	}

	public String getActivarBotonGenSolOrdAmbulatoria() {
		return activarBotonGenSolOrdAmbulatoria;
	}

	/*public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}*/

	/*public void setListaOrdenesImprimir(ArrayList<HashMap> listaOrdenesImprimir) {
		this.listaOrdenesImprimir = listaOrdenesImprimir;
	}

	public ArrayList<HashMap> getListaOrdenesImprimir() {
		return listaOrdenesImprimir;
	}*/

	public void setListaFinalOrdenesImprimir(
			ArrayList<DtoOrdenesAmbulatorias> listaFinalOrdenesImprimir) {
		this.listaFinalOrdenesImprimir = listaFinalOrdenesImprimir;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaFinalOrdenesImprimir() {
		return listaFinalOrdenesImprimir;
	}

	public void setNombreArchivoGeneradoCopia(String nombreArchivoGeneradoCopia) {
		this.nombreArchivoGeneradoCopia = nombreArchivoGeneradoCopia;
	}

	public String getNombreArchivoGeneradoCopia() {
		return nombreArchivoGeneradoCopia;
	}

	public String[] getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(String[] seleccionados) {
		this.seleccionados = seleccionados;
	}

	public String getAll() {
		return all;
	}

	public void setAll(String all) {
		this.all = all;
	}

	public void setCheckearTodos(String checkearTodos) {
		this.checkearTodos = checkearTodos;
	}

	public String getCheckearTodos() {
		return checkearTodos;
	}

	public void setMaxPaginas(String maxPaginas) {
		this.maxPaginas = maxPaginas;
	}

	public String getMaxPaginas() {
		return maxPaginas;
	}

	public void setPaginaActual(String paginaActual) {
		this.paginaActual = paginaActual;
	}

	public String getPaginaActual() {
		return paginaActual;
	}

	public void setListaNombreArchivos(ArrayList<String> listaNombreArchivos) {
		this.listaNombreArchivos = listaNombreArchivos;
	}

	public ArrayList<String> getListaNombreArchivos() {
		return listaNombreArchivos;
	}

	public void setNombreArchivoGeneradoOriginal(String nombreArchivoGeneradoOriginal) {
		this.nombreArchivoGeneradoOriginal = nombreArchivoGeneradoOriginal;
	}

	public String getNombreArchivoGeneradoOriginal() {
		return nombreArchivoGeneradoOriginal;
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the imprimirAutorizacion
	 */
	public boolean isImprimirAutorizacion() {
		return imprimirAutorizacion;
	}

	/**
	 * @param imprimirAutorizacion the imprimirAutorizacion to set
	 */
	public void setImprimirAutorizacion(boolean imprimirAutorizacion) {
		this.imprimirAutorizacion = imprimirAutorizacion;
	}

	/**
	 * @return the listaNombresReportes
	 */
	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	/**
	 * @param listaNombresReportes the listaNombresReportes to set
	 */
	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	/**
	 * @return the listaValidacionesAutorizaciones
	 */
	public ArrayList<DtoValidacionGeneracionAutorizacionCapitada> getListaValidacionesAutorizaciones() {
		return listaValidacionesAutorizaciones;
	}

	/**
	 * @param listaValidacionesAutorizaciones the listaValidacionesAutorizaciones to set
	 */
	public void setListaValidacionesAutorizaciones(
			ArrayList<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionesAutorizaciones) {
		this.listaValidacionesAutorizaciones = listaValidacionesAutorizaciones;
	}
	
	/**
	 * @return the cuentaSolicitante
	 */
	public int getCuentaSolicitante() {
		return cuentaSolicitante;
	}	

	/**
	 * @param cuentaSolicitante the cuentaSolicitante to set
	 */
	public void setCuentaSolicitante(int cuentaSolicitante) {
		this.cuentaSolicitante = cuentaSolicitante;
	}

	public void setBotonGenerarSolicitud(boolean botonGenerarSolicitud) {
		this.botonGenerarSolicitud = botonGenerarSolicitud;
	}

	public boolean isBotonGenerarSolicitud() {
		return botonGenerarSolicitud;
	}

	public void setCentroCostoCorrespondeAutorizacion(
			boolean centroCostoCorrespondeAutorizacion) {
		this.centroCostoCorrespondeAutorizacion = centroCostoCorrespondeAutorizacion;
	}

	public boolean isCentroCostoCorrespondeAutorizacion() {
		return centroCostoCorrespondeAutorizacion;
	}

	/**
	 * @return valor de faltanInformacion
	 */
	public String getFaltanInformacion() {
		return faltanInformacion;
	}

	/**
	 * @param faltanInformacion el faltanInformacion para asignar
	 */
	public void setFaltanInformacion(String faltanInformacion) {
		this.faltanInformacion = faltanInformacion;
	}

	/**
	 * M�todo que almacena los n�meros de las ordenes ambulatorias de servicios
	 * cuando se generan al mismo tiempo y se imprimen en el resumen.
	 * 
	 * @param listaNumOrdenServicios
	 */
	public void setListaNumOrdenServicios(ArrayList<String> listaNumOrdenServicios) {
		this.listaNumOrdenServicios = listaNumOrdenServicios;
	}

	/**
	 * M�todo que retorna lista de los n�meros de las ordenes ambulatorias de servicios
	 * cuando se generan al mismo tiempo y se imprimen en el resumen. 
	 * 
	 * @return listaNumOrdenServicios
	 */
	public ArrayList<String> getListaNumOrdenServicios() {
		return listaNumOrdenServicios;
	}

	public void setPendienteAutorizacion(boolean pendienteAutorizacion) {
		this.pendienteAutorizacion = pendienteAutorizacion;
	}

	public boolean isPendienteAutorizacion() {
		return pendienteAutorizacion;
	}

	/**
	 * @return the mostrarBotonNuevo
	 */
	public Boolean getMostrarBotonNuevo() {
		return mostrarBotonNuevo;
	}

	/**
	 * @param mostrarBotonNuevo the mostrarBotonNuevo to set
	 */
	public void setMostrarBotonNuevo(Boolean mostrarBotonNuevo) {
		this.mostrarBotonNuevo = mostrarBotonNuevo;
	}

	/**
	 * @return the mostrarBotonVolver
	 */
	public Boolean getMostrarBotonVolver() {
		return mostrarBotonVolver;
	}

	/**
	 * @param mostrarBotonVolver the mostrarBotonVolver to set
	 */
	public void setMostrarBotonVolver(Boolean mostrarBotonVolver) {
		this.mostrarBotonVolver = mostrarBotonVolver;
	}

	/**
	 * @return the mostrarBotonesDetalleOrdenAMB
	 */
	public Boolean getMostrarBotonesDetalleOrdenAMB() {
		return mostrarBotonesDetalleOrdenAMB;
	}

	/**
	 * @param mostrarBotonesDetalleOrdenAMB the mostrarBotonesDetalleOrdenAMB to set
	 */
	public void setMostrarBotonesDetalleOrdenAMB(
			Boolean mostrarBotonesDetalleOrdenAMB) {
		this.mostrarBotonesDetalleOrdenAMB = mostrarBotonesDetalleOrdenAMB;
	}

	/**
	 * @return the quitarEncabezados
	 */
	public Boolean getQuitarEncabezados() {
		return quitarEncabezados;
	}

	/**
	 * @param quitarEncabezados the quitarEncabezados to set
	 */
	public void setQuitarEncabezados(Boolean quitarEncabezados) {
		this.quitarEncabezados = quitarEncabezados;
	}

	/**
	 * @return the citasAtendidas
	 */
	public List<CitaDto> getListaCitasAtendidas() {
		return listaCitasAtendidas;
	}

	/**
	 * @param citasAtendidas the citasAtendidas to set
	 */
	public void setListaCitasAtendidas(List<CitaDto> citasAtendidas) {
		this.listaCitasAtendidas = citasAtendidas;
	}

	/**
	 * @return the codigoCitaSeleccionada
	 */
	public String getCodigoCitaSeleccionada() {
		return codigoCitaSeleccionada;
	}

	/**
	 * @param codigoCitaSeleccionada the codigoCitaSeleccionada to set
	 */
	public void setCodigoCitaSeleccionada(String codigoCitaSeleccionada) {
		this.codigoCitaSeleccionada = codigoCitaSeleccionada;
	}

	/**
	 * @return the provieneAtencionCita
	 */
	public boolean isProvieneAtencionCita() {
		return provieneAtencionCita;
	}

	/**
	 * @param provieneAtencionCita the provieneAtencionCita to set
	 */
	public void setProvieneAtencionCita(boolean provieneAtencionCita) {
		this.provieneAtencionCita = provieneAtencionCita;
	}

	/**
	 * @return the procesoExitoso
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}

	/**
	 * @param procesoExitoso the procesoExitoso to set
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}

	/**
	 * @return the articulosModificacion
	 */
	public HashMap<String, Object> getArticulosModificacion() {
		return articulosModificacion;
	}

	/**
	 * @param articulosModificacion the articulosModificacion to set
	 */
	public void setArticulosModificacion(
			HashMap<String, Object> articulosModificacion) {
		this.articulosModificacion = articulosModificacion;
	}
	
	/**
	 * @return the procesoExitosoAnulacion
	 */
	public boolean isProcesoExitosoAnulacion() {
		return procesoExitosoAnulacion;
	}

	/**
	 * @param procesoExitosoAnulacion the procesoExitosoAnulacion to set
	 */
	public void setProcesoExitosoAnulacion(boolean procesoExitosoAnulacion) {
		this.procesoExitosoAnulacion = procesoExitosoAnulacion;
	}
	
}