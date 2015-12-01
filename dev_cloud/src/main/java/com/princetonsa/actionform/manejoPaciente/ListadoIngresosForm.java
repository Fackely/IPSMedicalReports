package com.princetonsa.actionform.manejoPaciente;

/**
 * autor: Jhony Alexander Duque A.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.mundo.manejoPaciente.ListadoIngresos;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

public class ListadoIngresosForm  extends ValidatorForm{
	
	/*-----------------------------------------------
	 * 	ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;

    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber;	
	
	/*-----------------------------------------------
	 * 	FIN ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	
	/// Indices De Criterios-------------------
	String [] indicesCriterios=ListadoIngresos.indicesCriterios;
	
	/*
	 * Indices resultados
	 */
	String [] indicesResultados=ListadoIngresos.indicesResultados;
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase ListadoIngresosForm
	 */
	Logger logger = Logger.getLogger(ListadoIngresosForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	

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
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	

	/*--------------------------------------------------------------------
	 * ATRIBUTOS DE ACTUALIZACION AUTORIZACIONES MEDICAS MODIFICADO SHAIO
	 ---------------------------------------------------------------------*/
	/**
	 * maneja las acciones
	 */
	private String estado;
	
	/**
	 * Almacena la informacion de los ingresos y cuetas de un paciente
	 */
	private HashMap listadoIngresos= new HashMap();

	private String index="";
	
	/**
	 * Almacena los convenios del ingreso en subcuentas
	 */
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * almacena el convenio seleccionado
	 */
	private String convenio = ConstantesBD.codigoNuncaValido+"";
	
	/**
	 * almacena los datos del ingreso en el detalle 
	 */
	private HashMap encabezadoDetalle= new HashMap();
	
	
	/**
	 * Encargado de almacenar la informacion de los servicios
	 * o articulos del convenio de la subcuenta
	 */
	private HashMap cuerpoDetalle = new HashMap();
	
	private HashMap cuerpoDetalleOld = new HashMap();
	
	private HashMap busquedaAvanzada = new HashMap ();
	
	private ArrayList<HashMap<String, Object>> centCosto = new ArrayList<HashMap<String,Object>>();
	
	private boolean operacionTrue = false;
	
	/**
	 * Variable volverXRango encargada de direccionar correctamente el botón volver
	 */
	private boolean volverXRango = false;
	
	/*-----------------------------------------------------------------------
	 * FIN ATRIBUTOS DE ACTUALIZACION AUTORIZACIONES MEDICAS MODIFICADO SHAIO
	 ------------------------------------------------------------------------*/
	
	/*--------------------------------------------------------------------
	 * ATRIBUTOS DE ACTUALIZACION AUTORIZACIONES MEDICAS POR RANGO SHAIO
	 ---------------------------------------------------------------------*/
	
	/**
	 * Almacena los centros de atencion a ser mostrados en 
	 * la jsp
	 */
	private ArrayList<HashMap<String, Object>> centrosAtencion= new ArrayList<HashMap<String, Object>>();
	
	/**
	 * Almacena los datos de busqueda
	 */
	private HashMap criterios = new HashMap ();
	
	/**
	 * Almacena los resultados a mostrar en la JSP
	 */
	private HashMap resultados = new HashMap ();
	
	/**
	 * atributo que indica la direccion para poder
	 * descargar el archivo
	 */
	private String urlArchivo;
	
	/**
	 * Controla si se generaron errores en validate para no mostrar el mensaje
	 */
	private boolean errores;
	
	/**
	 * mensaje para validar si hay datos para mostrar en el reporte de archivo plano
	 * @return
	 */
	private boolean mensaje;
	
	/**
	 * Atributo que almacena si el archivo
	 * .ZIP si se genero
	 */
	private boolean existeArchivo=false; 
	
	/**
     * HashMap con las vias de ingreso
     */
    private HashMap viaIngreso;
    
    /**
	 * String donde se almacena el valor del criterio seleccionado correspondiente a los codigos de las vias de ingreso seleccionadas
	 */
	private String [] viaIngresoSeleccionado;
	
	/**
	 * Fecha Inicial de Ingreso
	 */
	private String fechaIngresoInicial;
	
	/**
	 * Fecha Final de Ingreso
	 */
	private String fechaIngresoFinal;
	
	/**
	 * Fecha Inicial de Solicitud
	 */
	private String fechaSolicitudInicial;
	
	/**
	 * Fecha Final de Solicitud
	 */
	private String fechaSolicitudFinal;
	
	/**
	 * Almacena los convenios del ingreso en subcuentas
	 */
	private HashMap todosLosConvenios = new HashMap();
	
	/**
	 * String del convenio seleccionado
	 */
	private String convenioSeleccionado;
	
	/**
	 * Almacena los convenios del ingreso en subcuentas
	 */
	private HashMap estadoSolicitud = new HashMap();
	
	/**
	 * String del estado de solicitud seleccionado
	 */
	private String estadoSolicitudSeleccionado;
	
	/**
	 * Almacena los convenios del ingreso en subcuentas
	 */
	private HashMap pisos = new HashMap();
	
	/**
	 * String del piso seleccionado
	 */
	private String pisoSeleccionado;
	
	/**
	 * Estado del ingreso seleccionado desde la JSP
	 */
	private String estadoIngreso;
	
	/**
	 * Estado de cuenta activo
	 */
	private String estadoCuentaActiva;
	
	/**
	 * Estado de cuenta asociado
	 */
	private String estadoCuentaAsociada;
	
	/**
	 * Estado de cuenta facturado parcial
	 */
	private String estadoCuentaFacturadaParcial;
	
	/**
	 * Estado de cuenta facturado
	 */
	private String estadoCuentaFacturada;
	
	/**
	 * estado autorizacion
	 */
	private String estadoAutorizacion;
	
	/**
	 * Campo que activa el filtro por órdenes ambulatorias
	 */
	private boolean ordenesAmbulatorias;
	
	/**
	 * Atributo que indica donde se almaceno el archivo,
	 * este es para mostrar la ruta excata donde se genero
	 * el archivo dentro del sistema de directorios del
	 * servidor 
	 */
	private String ruta;
	
	/**
	 * Atributo encargado de dar la orden para carga rlos datos faltantes para mostrar los ingresos
	 */
	boolean XRangoIngreso = false;
	
	
	private String numeroAuto="";
	
	/**
	 * Centros de Atención HashMap donde se almacenan todos los centros de Atención
	 */
	private HashMap centrosDeAtencion = new HashMap ();
	
	/*--------------------------------------------------------------------
	 * FIN ATRIBUTOS DE ACTUALIZACION AUTORIZACIONES MEDICAS POR RANGO SHAIO
	 ---------------------------------------------------------------------*/
	/*
	 * ATRIBUTOS PARA MANEJO DE NUEVAS AUTORIZACIONES*
	 */
	private String tipoUsuarioReportaAuto;
	private int posSolicitud;
	private String estadoAutorizacionSolicitud;
	private String tipoTramiteSolicitud;
	private boolean esVigenteSolicitud;
	private boolean puedoEnviar;
	private boolean puedoRegistrarRespuesta;
	private boolean puedoAnularRespuesta;
	private int codigoConvenio;
	private int posAdjuntos;
	
	private DtoAutorizacion autorizacion;
	
	//Arreglos
	private ArrayList<HashMap<String, Object>> tiposSerSol = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> coberturasSalud = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> origenesAtencion = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> mediosEnvio = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> entidadesEnvio = new ArrayList<HashMap<String,Object>>();
	
	
	/*-------------------------------------------------------
	 * 			METODOS SETTERS AND GETTERS
	 --------------------------------------------------------*/
	public ArrayList<HashMap<String, Object>> getCentCosto() {
		return centCosto;
	}
	public void setCentCosto(ArrayList<HashMap<String, Object>> centCosto) {
		this.centCosto = centCosto;
	}
	
	//------------------Busqueda avanzada -----------------------------
	public HashMap getBusquedaAvanzada() {
		return busquedaAvanzada;
	}
	public void setBusquedaAvanzada(HashMap busquedaAvanzada) {
		this.busquedaAvanzada = busquedaAvanzada;
	}
	public Object getBusquedaAvanzada(String key) {
		return busquedaAvanzada.get(key);
	}
	public void setBusquedaAvanzada(String key,Object value) {
		this.busquedaAvanzada.put(key, value);
	}
	//---------------------------------------------------
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
	
	//------------listado ingresos --------------------
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
	//---------------------------------------------
	
	
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}
	
	
	//------------------------------------------------
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
	
	public String getConvenio() {
		return convenio;
	}
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	//----------------------------------------------------
	
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
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	
	//-------ruta ------------------------------------
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
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
	
	
	/*-------------------------------------------------------
	 * 		FIN	METODOS SETTERS AND GETTERS
	 --------------------------------------------------------*/
	
	/*---------------------------
	 * 		METODOS
	 ---------------------------*/
	
	public void reset()
	{
		resetPager();
		this.index="";
		this.listadoIngresos= new HashMap ();
		this.setListadoIngresos("numRegistros", 0);
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.encabezadoDetalle = new HashMap ();
		this.setEncabezadoDetalle("numRegistros", 0);
		this.convenio=ConstantesBD.codigoNuncaValido+"";
		this.centCosto = new ArrayList<HashMap<String,Object>>();
		this.busquedaAvanzada = new HashMap ();
		this.operacionTrue=false;
		this.estadoCuentaActiva="";
		this.estadoCuentaAsociada="";
		this.estadoCuentaFacturadaParcial="";
		this.estadoCuentaFacturada="";
		this.numeroAuto="";
		this.volverXRango = false;
		this.estadoAutorizacion="";
		this.ordenesAmbulatorias = false;
		this.posSolicitud = ConstantesBD.codigoNuncaValido;
		this.estadoAutorizacionSolicitud = "";
		this.tipoTramiteSolicitud = "";
		this.esVigenteSolicitud = false;
		this.puedoEnviar = false;
		this.puedoRegistrarRespuesta = false;
		this.puedoAnularRespuesta = false;
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.posAdjuntos = ConstantesBD.codigoNuncaValido;
		this.autorizacion = new DtoAutorizacion();
		this.tipoUsuarioReportaAuto = "";
		
		this.tiposSerSol = new ArrayList<HashMap<String,Object>>();
		this.coberturasSalud = new ArrayList<HashMap<String,Object>>();
		this.origenesAtencion = new ArrayList<HashMap<String,Object>>();
		this.mediosEnvio = new ArrayList<HashMap<String,Object>>();
		this.entidadesEnvio = new ArrayList<HashMap<String,Object>>();
	}
	
	public void resetMenosIndex()
	{
		resetPager();
		this.listadoIngresos= new HashMap ();
		this.setListadoIngresos("numRegistros", 0);
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.encabezadoDetalle = new HashMap ();
		this.setEncabezadoDetalle("numRegistros", 0);
		this.convenio=ConstantesBD.codigoNuncaValido+"";
		this.centCosto = new ArrayList<HashMap<String,Object>>();
		this.busquedaAvanzada = new HashMap ();
		this.operacionTrue=false;
		this.estadoCuentaActiva="";
		this.estadoCuentaAsociada="";
		this.estadoCuentaFacturadaParcial="";
		this.estadoCuentaFacturada="";
		this.numeroAuto="";
	}

	public String getNumeroAuto() {
		return numeroAuto;
	}
	public void setNumeroAuto(String numeroAuto) {
		this.numeroAuto = numeroAuto;
	}
	public void resetPager()
	{
		this.linkSiguiente="";
		this.patronOrdenar="";
		this.ultimoPatron="";
	}
	
	public void resetDetalle ()
	{
		this.encabezadoDetalle = new HashMap ();
		this.setEncabezadoDetalle("numRegistros", 0);
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.convenio=ConstantesBD.codigoNuncaValido+"";
		this.cuerpoDetalle= new HashMap ();
		this.setCuerpoDetalle("numRegistros", 0);
		this.setCuerpoDetalleOld("numRegistros",0);
		this.busquedaAvanzada = new HashMap ();
		this.operacionTrue=false;
		this.estadoIngreso="";
	}
	
	public void resetBusqueda()
	{
		this.busquedaAvanzada = new HashMap ();
	}
	
	/*---------------------------
	 * 		FIN METODOS
	 ---------------------------*/
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = super.validate(mapping, request);
		logger.info("\n busqueda --> "+busquedaAvanzada);
		return errores;
		
	}
	
	/*
	 * MÉTODOS ACTUALIZACION AUTORIZACIONES MÉDICAS POR RANGOS
	 */
	
	/*-------------------------------
	 * 		METODOS SET AND GETTERS
	 --------------------------------*/
	
	//-----------centro atencion-------------------
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	//--------------------------------------------
	
	//------------Criterios de busqueda--------------------------
	public HashMap getCriterios() {
		return criterios;
	}
	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}
	public Object getCriterios(String key) {
		return criterios.get(key);
	}
	public void setCriterios(String key,Object value) {
		this.criterios.put(key, value);
	}
	//-----------------------------------------------------------
	
	/*
	 * Resultados De Búsqueda
	 */
	public HashMap getResultados() {
		return resultados;
	}
	public void setResultados(HashMap resultados) {
		this.resultados = resultados;
	}
	public Object getResultados(String key) {
		return resultados.get(key);
	}
	public void setResultados(String key,Object value) {
		this.resultados.put(key, value);
	}
	
	//-------------url archivo-----------------------------------
	public String getUrlArchivo() {
		return urlArchivo;
	}
	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}
	//-----------------------------------------------------------
	
	//--------existe archivo ------------------------------------
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
	//-----------------------------------------------------------
	
	//-Manejo de los errores
	/**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}
	//-FIN MANEJO DE LOS ERRORES
	
	public void setMensaje(boolean mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isMensaje() {
		return mensaje;
	}
	
	//- MANEJO DE LA SELECCION DE LAS VIAS DE INGRESO-
	/**
	 * @return the tipoTransaccionSeleccionado
	 */
	public String[] getViaIngresoSeleccionado() {
		return viaIngresoSeleccionado;
	}

	/**
	 * @param tipoTransaccionSeleccionado the tipoTransaccionSeleccionado to set
	 */
	public void setViaIngresoSeleccionado(String[] viaIngresoSeleccionado) {
		this.viaIngresoSeleccionado = viaIngresoSeleccionado;
	}
	//- FIN MANEJO DE LA SELECCION DE LAS VIAS DE INGRESO-
	
	public HashMap getViaIngreso() {
		return viaIngreso;
	}
	public void setViaIngreso(HashMap viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	public String getFechaIngresoInicial() {
		return fechaIngresoInicial;
	}
	public void setFechaIngresoInicial(String fechaIngresoInicial) {
		this.fechaIngresoInicial = fechaIngresoInicial;
	}
	public String getFechaIngresoFinal() {
		return fechaIngresoFinal;
	}
	public void setFechaIngresoFinal(String fechaIngresoFinal) {
		this.fechaIngresoFinal = fechaIngresoFinal;
	}
	public String getFechaSolicitudInicial() {
		return fechaSolicitudInicial;
	}
	public void setFechaSolicitudInicial(String fechaSolicitudInicial) {
		this.fechaSolicitudInicial = fechaSolicitudInicial;
	}
	public String getFechaSolicitudFinal() {
		return fechaSolicitudFinal;
	}
	public void setFechaSolicitudFinal(String fechaSolicitudFinal) {
		this.fechaSolicitudFinal = fechaSolicitudFinal;
	}
	
	/*
	 * Definición de todos los convenios
	 */
	public void setTodosLosConvenios(HashMap<String, Object> todosLosConvenios) {
		this.todosLosConvenios = todosLosConvenios;
	}
	public HashMap getTodosLosConvenios() {
		return todosLosConvenios;
	}
	public Object getTodosLosConvenios(String key) {
		return todosLosConvenios.get(key);
	}
	public void setTodosLosConvenios(String key,Object value) {
		this.todosLosConvenios.put(key, value);
	}
	
	/*
	 * Definición de estados de solicitud
	 */
	public void setEstadoSolicitud(HashMap estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}
	public HashMap getEstadoSolicitud() {
		return estadoSolicitud;
	}
	public Object getEstadoSolicitud(String key) {
		return estadoSolicitud.get(key);
	}
	public void setEstadoSolicitud(String key,Object value) {
		this.estadoSolicitud.put(key, value);
	}
	
	/*
	 * Definición de Pisos
	 */
	public void setPisos(HashMap pisos) {
		this.pisos = pisos;
	}
	public HashMap getPisos() {
		return pisos;
	}
	public Object getPisos(String key) {
		return pisos.get(key);
	}
	public void setPisos(String key,Object value) {
		this.pisos.put(key, value);
	}
	
	/*
	 * Definición de estado de ingreso
	 */
	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}
	public String getEstadoIngreso() {
		return estadoIngreso;
	}
	
	/*
	 *Definición de estado de cuenta 
	 */
	public String getEstadoCuentaActiva() {
		return estadoCuentaActiva;
	}
	public void setEstadoCuentaActiva(String estadoCuentaActiva) {
		this.estadoCuentaActiva = estadoCuentaActiva;
	}
	public String getEstadoCuentaAsociada() {
		return estadoCuentaAsociada;
	}
	public void setEstadoCuentaAsociada(String estadoCuentaAsociada) {
		this.estadoCuentaAsociada = estadoCuentaAsociada;
	}
	public String getEstadoCuentaFacturadaParcial() {
		return estadoCuentaFacturadaParcial;
	}
	public void setEstadoCuentaFacturadaParcial(String estadoCuentaFacturadaParcial) {
		this.estadoCuentaFacturadaParcial = estadoCuentaFacturadaParcial;
	}
	public String getEstadoCuentaFacturada() {
		return estadoCuentaFacturada;
	}
	public void setEstadoCuentaFacturada(String estadoCuentaFacturada) {
		this.estadoCuentaFacturada = estadoCuentaFacturada;
	}
	
	/*
	 * Centros De Atención
	 */
	public void setCentrosDeAtencion(HashMap centrosDeAtencion) {
		this.centrosDeAtencion = centrosDeAtencion;
	}
	public HashMap getCentrosDeAtencion() {
		return centrosDeAtencion;
	}
	
	public Object getCentrosDeAtencion(String key) {
		return centrosDeAtencion.get(key);
	}
	public void setCentrosDeAtencion(String key,Object value) {
		this.centrosDeAtencion.put(key, value);
	}
	
	
	
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	/*
	 * Definición de Convenio Seleccionado
	 */
	public String getConvenioSeleccionado() {
		return convenioSeleccionado;
	}
	public void setConvenioSeleccionado(String convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
	}
	
	/*
	 * Definición de Estado Solicitud Seleccionado
	 */
	public String getEstadoSolicitudSeleccionado() {
		return estadoSolicitudSeleccionado;
	}
	public void setEstadoSolicitudSeleccionado(String estadoSolicitudSeleccionado) {
		this.estadoSolicitudSeleccionado = estadoSolicitudSeleccionado;
	}
	
	/*
	 * Definición de Piso Seleccionado
	 */
	public String getPisoSeleccionado() {
		return pisoSeleccionado;
	}
	public void setPisoSeleccionado(String pisoSeleccionado) {
		this.pisoSeleccionado = pisoSeleccionado;
	}
	
	/*
	 * Definición de Rango Ingreso
	 */
	public boolean getXRangoIngreso() {
		return XRangoIngreso;
	}
	public void setXRangoIngreso(boolean rangoIngreso) {
		XRangoIngreso = rangoIngreso;
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
	
	/*----------------------------------------
	 * 		FIN METODOS SET AND GETTERS
	 ----------------------------------------*/
	
	//-METODOS ADICIONALES-
	public void resetPorRangos ()
	{
		this.criterios = new HashMap ();
		this.resultados = new HashMap ();
		this.urlArchivo="";
		this.errores = false;
		this.viaIngreso = new HashMap();
    	this.viaIngreso.put("numRegistros", "0");
    	this.viaIngresoSeleccionado = new String []{""};
    	this.convenioSeleccionado="";
    	this.estadoSolicitudSeleccionado="";
    	this.pisoSeleccionado="";
	}
	//-FIN MÉTODOS ADICIONALES-
	
	public void resetMensaje ()
	{
		this.mensaje= false;
	}
	public void setVolverXRango(boolean volverXRango) {
		this.volverXRango = volverXRango;
	}
	public boolean getVolverXRango() {
		return volverXRango;
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
	/**
	 * @return the posSolicitud
	 */
	public int getPosSolicitud() {
		return posSolicitud;
	}
	/**
	 * @param posSolicitud the posSolicitud to set
	 */
	public void setPosSolicitud(int posSolicitud) {
		this.posSolicitud = posSolicitud;
	}
	/**
	 * @return the estadoAutorizacionSolicitud
	 */
	public String getEstadoAutorizacionSolicitud() {
		return estadoAutorizacionSolicitud;
	}
	/**
	 * @param estadoAutorizacionSolicitud the estadoAutorizacionSolicitud to set
	 */
	public void setEstadoAutorizacionSolicitud(String estadoAutorizacionSolicitud) {
		this.estadoAutorizacionSolicitud = estadoAutorizacionSolicitud;
	}
	/**
	 * @return the puedoEnviar
	 */
	public boolean isPuedoEnviar() {
		return puedoEnviar;
	}
	/**
	 * @param puedoEnviar the puedoEnviar to set
	 */
	public void setPuedoEnviar(boolean puedoEnviar) {
		this.puedoEnviar = puedoEnviar;
	}
	/**
	 * @return the puedoRegistrarRespuesta
	 */
	public boolean isPuedoRegistrarRespuesta() {
		return puedoRegistrarRespuesta;
	}
	/**
	 * @param puedoRegistrarRespuesta the puedoRegistrarRespuesta to set
	 */
	public void setPuedoRegistrarRespuesta(boolean puedoRegistrarRespuesta) {
		this.puedoRegistrarRespuesta = puedoRegistrarRespuesta;
	}
	/**
	 * @return the puedoAnularRespuesta
	 */
	public boolean isPuedoAnularRespuesta() {
		return puedoAnularRespuesta;
	}
	/**
	 * @param puedoAnularRespuesta the puedoAnularRespuesta to set
	 */
	public void setPuedoAnularRespuesta(boolean puedoAnularRespuesta) {
		this.puedoAnularRespuesta = puedoAnularRespuesta;
	}
	/**
	 * @return the tipoTramiteSolicitud
	 */
	public String getTipoTramiteSolicitud() {
		return tipoTramiteSolicitud;
	}
	/**
	 * @param tipoTramiteSolicitud the tipoTramiteSolicitud to set
	 */
	public void setTipoTramiteSolicitud(String tipoTramiteSolicitud) {
		this.tipoTramiteSolicitud = tipoTramiteSolicitud;
	}
	/**
	 * @return the esVigenteSolicitud
	 */
	public boolean isEsVigenteSolicitud() {
		return esVigenteSolicitud;
	}
	/**
	 * @param esVigenteSolicitud the esVigenteSolicitud to set
	 */
	public void setEsVigenteSolicitud(boolean esVigenteSolicitud) {
		this.esVigenteSolicitud = esVigenteSolicitud;
	}
	/**
	 * @return the autorizacion
	 */
	public DtoAutorizacion getAutorizacion() {
		return autorizacion;
	}
	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(DtoAutorizacion autorizacion) {
		this.autorizacion = autorizacion;
	}
	/**
	 * @return the tiposSerSol
	 */
	public ArrayList<HashMap<String, Object>> getTiposSerSol() {
		return tiposSerSol;
	}
	/**
	 * @param tiposSerSol the tiposSerSol to set
	 */
	public void setTiposSerSol(ArrayList<HashMap<String, Object>> tiposSerSol) {
		this.tiposSerSol = tiposSerSol;
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
	 * @return the origenesAtencion
	 */
	public ArrayList<HashMap<String, Object>> getOrigenesAtencion() {
		return origenesAtencion;
	}
	/**
	 * @param origenesAtencion the origenesAtencion to set
	 */
	public void setOrigenesAtencion(
			ArrayList<HashMap<String, Object>> origenesAtencion) {
		this.origenesAtencion = origenesAtencion;
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
	 * @return the mediosEnvio
	 */
	public ArrayList<HashMap<String, Object>> getMediosEnvio() {
		return mediosEnvio;
	}
	/**
	 * @param mediosEnvio the mediosEnvio to set
	 */
	public void setMediosEnvio(ArrayList<HashMap<String, Object>> mediosEnvio) {
		this.mediosEnvio = mediosEnvio;
	}
	/**
	 * @return the entidadesEnvio
	 */
	public ArrayList<HashMap<String, Object>> getEntidadesEnvio() {
		return entidadesEnvio;
	}
	/**
	 * @param entidadesEnvio the entidadesEnvio to set
	 */
	public void setEntidadesEnvio(ArrayList<HashMap<String, Object>> entidadesEnvio) {
		this.entidadesEnvio = entidadesEnvio;
	}
	/**
	 * @return the posAdjuntos
	 */
	public int getPosAdjuntos() {
		return posAdjuntos;
	}
	/**
	 * @param posAdjuntos the posAdjuntos to set
	 */
	public void setPosAdjuntos(int posAdjuntos) {
		this.posAdjuntos = posAdjuntos;
	}
	public String getTipoUsuarioReportaAuto() {
		return tipoUsuarioReportaAuto;
	}
	public void setTipoUsuarioReportaAuto(String tipoUsuarioReportaAuto) {
		this.tipoUsuarioReportaAuto = tipoUsuarioReportaAuto;
	}
	
}