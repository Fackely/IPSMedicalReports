/*
 * @(#)Camas1Form.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de camas1
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Junio 1, 2005
 * @author wrios 
 */
public class Camas1Form extends ValidatorForm
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(Camas1Form.class);
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Accion a realizar dependiendo de un estado
	 */
	private String accion;
	
	/**
	 * codigo (axioma) de la cama
	 */
	private int codigo;
	
    /**
     * num habitacion
     */
    private String habitacionStr;
    
    /**
     * Nombre de la habitacion
     */
    private String nombreHabitacion;
    
    /**
     * Piso
     */
    private String piso;
    
    /**
     * Nombre del piso
     */
    private String nombrePiso;
    
    /**
     * Tipo habitacion
     */
    private String tipoHabitacion;
    
    /**
     * Numero de la cama
     */
    private String numeroCama;
    
    /**
     * Desc de la cama
     */
    private String descripcionCama;
    
    /**
     * codigo del estado de la cama
     */
    private int codigoEstadoCama;
    
    /**
     * Nombre del estado de la cama
     */
    private String nombreEstadoCama;
    
    /**
     * 0=false 1=true -1=noSeleccionado
     * es Uci
     */
    private int esUci;
    
    /**
     * String que indica (SI -  NO) si es o no uci 
     */
    private String esUciStr;
    
    /**
     * Indica si es o no de UCI antes de realizar la
     * modificacion
     */
    private int esUciAntesDeModificar;
    
    /**
     * codigo del tipo de usuario
     */
    private int codigoTipoUsuarioCama;
    
    /**
     * nombre del tipo de usuario de la cama
     */
    private String nombreTipoUsuarioCama;
    
    /**
     * codigo del centro de costo de la cama 
     */
    private int codigoCentroCosto;
    
    /**
     * Nombre del centro de costo
     */
    private String nombreCentroCosto;
    
    /**
	 * Colección con los datos del listado,
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
	 * boolean que indica si se debe o no mostrar la ventana de insercion
	 */
	private boolean mostrarVentanaInsertarNueva;
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";
	
	private int offset;
	
	/**
	 * codigo del servicio asociado a la cama
	 */
	private int codigoServicio;
	
	/**
	 * codigo del servicio asociado a la cama
	 * (utilizado para la busqueda)
	 */
	private String codigoServicioStr;
	
	private String asignableAdmision="";
	/**
	 * Descripcion del servicio
	 * (utilizado para la busqueda)
	 */
	private String descripcionServicio;
	
	/**
	 * mapa con los servicios pertececientes a una cama
	 */
	private HashMap serviciosCama1Map = new HashMap();
	
	/**
	 * Mapa temporal que va ha contener la informacion de los servicios
	 * que estan en BD para luego comparar si se deben actualizar o no 
	 */
	private HashMap tempBDServiciosCama1Map = new HashMap();
	
	/**
	 * Número de filas existentes en el hashmap
	 */
	private int numeroFilasMapa;
	
	public String getAsignableAdmision() {
		return asignableAdmision;
	}

	public void setAsignableAdmision(String asignableAdmision) {
		this.asignableAdmision = asignableAdmision;
	}

	/**
	 * indica el numero de filas del mapa antes de hacer la modificacion
	 */
	private int numeroFilasMapaAntesModificar;
	
	/**
	 * cod de los tipos de monitoroeso insertasdoas
	 */
	private String codTiposMonitoreosInsertadosStr;
	
	/**
	 * boolean que indica si debemos lismpiar el mapa o no
	 */
	private boolean limpiarMapaBool;
	
	/**
	 * Indica si el estado es modificable o no
	 */
	private boolean puedoModificarEstadoCama;
	
	/**
	 * indica el numero de camas que estan para liberar
	 */
	private int numeroCamasPosibleLiberacion;
	
	/**
	 * Código del centro de atencion
	 */
	private int codigoCentroAtencion;
	/**
	 * Nombre del centro de atención
	 */
	private String nombreCentroAtencion;
	
	//***********ATRIBUTOS PARA CARGAR ESTRUCTURAS DE DATOS*********************
	private ArrayList<HashMap<String, Object>> pisos = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> habitaciones = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> tiposHabitaciones = new ArrayList<HashMap<String,Object>>();
	//****************************************************************************
	
	/**
	 * Max page items
	 */
	private int maxPageItems;
	
	private String indexEliminar="";
	
	public String getIndexEliminar() {
		return indexEliminar;
	}

	public void setIndexEliminar(String indexEliminar) {
		this.indexEliminar = indexEliminar;
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
		if(estado.equals("guardarModificar") || estado.equals("guardarNueva"))
		{				
			errores=super.validate(mapping,request);
			
			if(this.getPiso().equals("")||this.getPiso().equals("0"))
			{
				errores.add("Descripcion vacio", new ActionMessage("errors.required","El campo Piso"));
			}
			
			if(this.getHabitacionStr().equals("")||this.getHabitacionStr().equals("0"))
			{
				errores.add("Descripcion vacio", new ActionMessage("errors.required","El campo Habitación"));
			}
			
			if(numeroCama.trim().equals(""))
			{	
				errores.add("Campo Cama vacio", new ActionMessage("errors.required","El campo Cama"));
			}
			if(descripcionCama.trim().equals(""))
			{
				errores.add("Descripcion vacio", new ActionMessage("errors.required","El campo Descripción"));
			}
			if(codigoEstadoCama<0)
			{
				errores.add("Codigo Estado Cama No seleccionado ", new ActionMessage("errors.required","El campo Estado Cama"));
			}
			if(codigoTipoUsuarioCama<0)
			{
				errores.add("Código tipo usuario no seleccionado", new ActionMessage("errors.required","El campo Usuario"));
			}
			if(codigoCentroCosto<0)
			{
				errores.add("Código centro costo no seleccionado", new ActionMessage("errors.required","El campo Centro Costo"));
			}
			if(esUci==ConstantesBD.codigoNuncaValido)
			{
			    errores.add("Es Uci no seleccionado", new ActionMessage("errors.required","El campo ¿Es Uci?"));
			}
			if(serviciosCama1Map.size()<=0 && esUci==1)
			{	
				errores.add("Servicio requerido", new ActionMessage("errors.required","El campo Servicio y Tipo de Monitoreo"));
			}
			if(serviciosCama1Map.size()<=0 && esUci==0)
			{	
				errores.add("Servicio requerido", new ActionMessage("errors.required","El campo Servicio"));
			}
			if(!errores.isEmpty())
			{
				if(estado.equals("guardarModificar"))
					this.setEstado("modificar");
				if(estado.equals("guardarNueva"))
				    this.setEstado("ingresarModificar");
			}		
		}
		if(estado.equals("busquedaAvanzada") && accion.equals("realizarBusqueda") )
		{				
			errores=super.validate(mapping,request);
			
			/*
			try
			{
			    if(!this.getCodigoServicioStr().equals(""))
			        Integer.parseInt(this.getCodigoServicioStr());
			}
			catch(NumberFormatException e)
			{
			    errores.add("Campo Servicio codigo es int", new ActionMessage("errors.integerMayorQue","El campo Código Servicio","0"));
			}
			/*if(!errores.isEmpty())
			{
				this.setEstado("modificar");
			}*/		
		}
		return errores;
	}
	
	/**
	 * Resetea los valores para insertar una cama nueva
	 */
	public void resetInsertarNueva()
	{
	    this.codigo=ConstantesBD.codigoNuncaValido;
	    this.habitacionStr="";
	    this.numeroCama="";
	    this.piso = "";
	    this.tipoHabitacion = "";
	    this.descripcionCama="";
	    this.codigoEstadoCama= ConstantesBD.codigoNuncaValido;
	    this.nombreEstadoCama="";
	    this.esUci=ConstantesBD.codigoNuncaValido;
	    this.esUciAntesDeModificar=ConstantesBD.codigoNuncaValido;
	    this.codigoTipoUsuarioCama=ConstantesBD.codigoNuncaValido;
	    this.nombreTipoUsuarioCama="";
	    this.codigoCentroCosto=ConstantesBD.codigoNuncaValido;
	    this.nombreCentroCosto="";
	    this.serviciosCama1Map=new HashMap();
	    this.tempBDServiciosCama1Map= new HashMap();
	    this.numeroFilasMapa=ConstantesBD.codigoNuncaValido;
	    this.numeroFilasMapa=0;
	    this.accion="";
	    this.codTiposMonitoreosInsertadosStr="";
	    this.limpiarMapaBool=false;
	    this.esUciStr="";
	    this.puedoModificarEstadoCama=false;
	    this.codigoServicioStr="";
	    this.asignableAdmision=ConstantesBD.acronimoSi;
	    this.indexEliminar="";
	    
	}
	
	/**
	 * resetea todos los atributos de la forma
	 *
	 */
	public void reset()
	{
	    this.codigo=ConstantesBD.codigoNuncaValido;
	    this.habitacionStr="";
	    this.nombreHabitacion = "";
	    this.piso = "";
	    this.nombrePiso="";
	    this.tipoHabitacion = "";
	    this.numeroCama="";
	    this.descripcionCama="";
	    this.codigoEstadoCama= ConstantesBD.codigoNuncaValido;
	    this.nombreEstadoCama="";
	    this.esUci=ConstantesBD.codigoNuncaValido;
	    this.esUciAntesDeModificar=ConstantesBD.codigoNuncaValido;
	    this.codigoTipoUsuarioCama=ConstantesBD.codigoNuncaValido;
	    this.nombreTipoUsuarioCama="";
	    this.codigoCentroCosto=ConstantesBD.codigoNuncaValido;
	    this.nombreCentroCosto="";
	    this.linkSiguiente="";
	    this.offset = 0;
	    this.serviciosCama1Map=new HashMap();
	    this.tempBDServiciosCama1Map= new HashMap();
	    this.numeroFilasMapa=ConstantesBD.codigoNuncaValido;
	    this.mostrarVentanaInsertarNueva=false;
	    this.codigoServicio=0;
	    this.descripcionServicio="";
	    this.numeroFilasMapa=0;
	    this.accion="";
	    this.codTiposMonitoreosInsertadosStr="";
	    this.limpiarMapaBool=false;
	    this.esUciStr="";
	    this.puedoModificarEstadoCama=false;
	    this.codigoServicioStr="";
	    this.numeroCamasPosibleLiberacion=ConstantesBD.codigoNuncaValido;
	    this.codigoCentroAtencion = 0;
	    this.nombreCentroAtencion = "";
	    
	    this.pisos = new ArrayList<HashMap<String,Object>>();
	    this.habitaciones = new ArrayList<HashMap<String,Object>>();
	    this.tiposHabitaciones = new ArrayList<HashMap<String,Object>>();
	    this.asignableAdmision="";
	    this.maxPageItems = 0;
	}
	
	/**
	 * Resetea el mapa
	 */
	public void resetMapa()
	{
	    this.serviciosCama1Map.clear();
	    this.limpiarMapaBool=false;
	}
	
	/**
	 * Retorna el tamanio de la coleccion
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
     * @return Returns the codigoCentroCosto.
     */
    public int getCodigoCentroCosto() {
        return codigoCentroCosto;
    }
    /**
     * @param codigoCentroCosto The codigoCentroCosto to set.
     */
    public void setCodigoCentroCosto(int codigoCentroCosto) {
        this.codigoCentroCosto = codigoCentroCosto;
    }
    /**
     * @return Returns the codigoEstadoCama.
     */
    public int getCodigoEstadoCama() {
        return codigoEstadoCama;
    }
    /**
     * @param codigoEstadoCama The codigoEstadoCama to set.
     */
    public void setCodigoEstadoCama(int codigoEstadoCama) {
        this.codigoEstadoCama = codigoEstadoCama;
    }
    /**
     * @return Returns the codigoTipoUsuarioCama.
     */
    public int getCodigoTipoUsuarioCama() {
        return codigoTipoUsuarioCama;
    }
    /**
     * @param codigoTipoUsuarioCama The codigoTipoUsuarioCama to set.
     */
    public void setCodigoTipoUsuarioCama(int codigoTipoUsuarioCama) {
        this.codigoTipoUsuarioCama = codigoTipoUsuarioCama;
    }
    /**
     * @return Returns the col.
     */
    public Collection getCol() {
        return col;
    }
    /**
     * @param col The col to set.
     */
    public void setCol(Collection col) {
        this.col = col;
    }
    /**
     * @return Returns the descripcionCama.
     */
    public String getDescripcionCama() {
        return descripcionCama;
    }
    /**
     * @param descripcionCama The descripcionCama to set.
     */
    public void setDescripcionCama(String descripcionCama) {
        this.descripcionCama = descripcionCama;
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
     * @return Returns the esUci.
     */
    public int getEsUci() {
        return esUci;
    }
    /**
     * @param esUci The esUci to set.
     */
    public void setEsUci(int esUci) {
        this.esUci = esUci;
    }
    /**
     * @return Returns the habitacionStr.
     */
    public String getHabitacionStr() {
        return habitacionStr;
    }
    /**
     * @param habitacionStr The habitacionStr to set.
     */
    public void setHabitacionStr(String habitacionStr) {
        this.habitacionStr = habitacionStr;
    }
    /**
     * @return Returns the nombreCentroCosto.
     */
    public String getNombreCentroCosto() {
        return nombreCentroCosto;
    }
    /**
     * @param nombreCentroCosto The nombreCentroCosto to set.
     */
    public void setNombreCentroCosto(String nombreCentroCosto) {
        this.nombreCentroCosto = nombreCentroCosto;
    }
    /**
     * @return Returns the nombreEstadoCama.
     */
    public String getNombreEstadoCama() {
        return nombreEstadoCama;
    }
    /**
     * @param nombreEstadoCama The nombreEstadoCama to set.
     */
    public void setNombreEstadoCama(String nombreEstadoCama) {
        this.nombreEstadoCama = nombreEstadoCama;
    }
    /**
     * @return Returns the nombreTipoUsuarioCama.
     */
    public String getNombreTipoUsuarioCama() {
        return nombreTipoUsuarioCama;
    }
    /**
     * @param nombreTipoUsuarioCama The nombreTipoUsuarioCama to set.
     */
    public void setNombreTipoUsuarioCama(String nombreTipoUsuarioCama) {
        this.nombreTipoUsuarioCama = nombreTipoUsuarioCama;
    }
    /**
     * @return Returns the numeroCama.
     */
    public String getNumeroCama() {
        return numeroCama;
    }
    /**
     * @param numeroCama The numeroCama to set.
     */
    public void setNumeroCama(String numeroCama) {
        this.numeroCama = numeroCama;
    }
    /**
     * @return Returns the accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion The accion to set.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()	{
		return columna;
	}
	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad(){
		return ultimaPropiedad;
	}
	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna){
		this.columna = columna;
	}
	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad){
		this.ultimaPropiedad = ultimaPropiedad;
	}
    /**
     * @return Returns the mostrarVentanaInsertarNueva.
     */
    public boolean getMostrarVentanaInsertarNueva() {
        return mostrarVentanaInsertarNueva;
    }
    /**
     * @param mostrarVentanaInsertarNueva The mostrarVentanaInsertarNueva to set.
     */
    public void setMostrarVentanaInsertarNueva(
            boolean mostrarVentanaInsertarNueva) {
        this.mostrarVentanaInsertarNueva = mostrarVentanaInsertarNueva;
    }
    /**
     * @return Returns the linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente The linkSiguiente to set.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
	/**
	 * Set del mapa de servicios pertenecientes a una cama
	 * @param key
	 * @param value
	 */
	public void setServiciosCama1Map(String key, Object value) 
	{
		serviciosCama1Map.put(key, value);
	}
	/**
	 * Get del mapa servicios
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getServiciosCama1Map(String key) 
	{
		return serviciosCama1Map.get(key);
	}
    /**
     * @return Returns the codigoServicio.
     */
    public int getCodigoServicio() {
        return codigoServicio;
    }
    /**
     * @param codigoServicio The codigoServicio to set.
     */
    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }
    /**
     * @return Returns the serviciosCama1Map.
     */
    public HashMap getServiciosCama1Map() {
        return serviciosCama1Map;
    }
    /**
     * @param serviciosCama1Map The serviciosCama1Map to set.
     */
    public void setServiciosCama1Map(HashMap serviciosCama1Map) {
        this.serviciosCama1Map = serviciosCama1Map;
    }
    /**
     * @return Returns the numeroFilasMapa.
     */
    public int getNumeroFilasMapa() {
        if(this.serviciosCama1Map.size()==0)
            return 0;
        else    
            return (this.serviciosCama1Map.size()/5);
    }
    /**
     * @return Returns the numeroFilasMapaCasoNoUci.
     */
    public int getNumeroFilasMapaCasoNoUci() {
        if(this.serviciosCama1Map.size()==0)
            return 0;
        else    
            return (this.serviciosCama1Map.size()/3);
    }
    /**
     * @param numeroFilasMapa The numeroFilasMapa to set.
     */
    public void setNumeroFilasMapa(int numeroFilasMapa) {
        this.numeroFilasMapa = numeroFilasMapa;
    }
   
    /**
     * @return Returns the codTiposMonitoreosInsertadosStr.
     */
    public String getCodTiposMonitoreosInsertadosStr() {
        return codTiposMonitoreosInsertadosStr;
    }
    /**
     * @param codTiposMonitoreosInsertadosStr The codTiposMonitoreosInsertadosStr to set.
     */
    public void setCodTiposMonitoreosInsertadosStr(
            String codTiposMonitoreosInsertadosStr) {
        this.codTiposMonitoreosInsertadosStr = codTiposMonitoreosInsertadosStr;
    }
    /**
     * @return Returns the limpiarMapaBool.
     */
    public boolean getLimpiarMapaBool() {
        return limpiarMapaBool;
    }
    /**
     * @param limpiarMapaBool The limpiarMapaBool to set.
     */
    public void setLimpiarMapaBool(boolean limpiarMapaBool) {
        this.limpiarMapaBool = limpiarMapaBool;
    }
    /**
     * @return Returns the esUciStr.
     */
    public String getEsUciStr() {
        return esUciStr;
    }
    /**
     * @param esUciStr The esUciStr to set.
     */
    public void setEsUciStr(String esUciStr) {
        this.esUciStr = esUciStr;
    }
    /**
     * @return Returns the puedoModificarEstadoCama.
     */
    public boolean getPuedoModificarEstadoCama() {
        return puedoModificarEstadoCama;
    }
    /**
     * @param puedoModificarEstadoCama The puedoModificarEstadoCama to set.
     */
    public void setPuedoModificarEstadoCama(boolean puedoModificarEstadoCama) {
        this.puedoModificarEstadoCama = puedoModificarEstadoCama;
    }
    /**
     * @return Returns the numeroFilasMapaAntesModificar.
     */
    public int getNumeroFilasMapaAntesModificar() {
        return numeroFilasMapaAntesModificar;
    }
    /**
     * @param numeroFilasMapaAntesModificar The numeroFilasMapaAntesModificar to set.
     */
    public void setNumeroFilasMapaAntesModificar(
            int numeroFilasMapaAntesModificar) {
        this.numeroFilasMapaAntesModificar = numeroFilasMapaAntesModificar;
    }
    /**
     * @return Returns the tempBDServiciosCama1Map.
     */
    public HashMap getTempBDServiciosCama1Map() {
        return tempBDServiciosCama1Map;
    }
    /**
     * @param tempBDServiciosCama1Map The tempBDServiciosCama1Map to set.
     */
    public void setTempBDServiciosCama1Map(HashMap tempBDServiciosCama1Map) {
        this.tempBDServiciosCama1Map = tempBDServiciosCama1Map;
    }
    /**
	 * Set del mapa de servicios originales de la BD pertenecientes a una cama
	 * @param key
	 * @param value
	 */
	public void setTempBDServiciosCama1Map(String key, Object value) 
	{
		tempBDServiciosCama1Map.put(key, value);
	}
	/**
	 * Get del mapa de servicios originales de la BD pertenecientes a una cama
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getTempBDServiciosCama1Map(String key) 
	{
		return tempBDServiciosCama1Map.get(key);
	}
    /**
     * @return Returns the numeroFilasMapa.
     */
    public int getNumeroFilasMapaTemporal() {
        if(this.tempBDServiciosCama1Map.size()==0)
            return 0;
        else    
            return (this.tempBDServiciosCama1Map.size()/5);
    }
    /**
     * @return Returns the numeroFilasMapaCasoNoUci.
     */
    public int getNumeroFilasMapaCasoNoUciTemporal() {
        if(this.tempBDServiciosCama1Map.size()==0)
            return 0;
        else    
            return (this.tempBDServiciosCama1Map.size()/3);
    }
    /**
     * @return Returns the esUciAntesDeModificar.
     */
    public int getEsUciAntesDeModificar() {
        return esUciAntesDeModificar;
    }
    /**
     * @param esUciAntesDeModificar The esUciAntesDeModificar to set.
     */
    public void setEsUciAntesDeModificar(int esUciAntesDeModificar) {
        this.esUciAntesDeModificar = esUciAntesDeModificar;
    }
    /**
     * @return Returns the codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Returns the descripcionServicio.
     */
    public String getDescripcionServicio() {
        return descripcionServicio;
    }
    /**
     * @param descripcionServicio The descripcionServicio to set.
     */
    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }
    /**
     * @return Returns the codigoServicioStr.
     */
    public String getCodigoServicioStr() {
        return codigoServicioStr;
    }
    /**
     * @param codigoServicioStr The codigoServicioStr to set.
     */
    public void setCodigoServicioStr(String codigoServicioStr) {
        this.codigoServicioStr = codigoServicioStr;
    }
    /**
     * @return Returns the numeroCamasPosibleLiberacion.
     */
    public int getNumeroCamasPosibleLiberacion() {
        return numeroCamasPosibleLiberacion;
    }
    /**
     * @param numeroCamasPosibleLiberacion The numeroCamasPosibleLiberacion to set.
     */
    public void setNumeroCamasPosibleLiberacion(int numeroCamasPosibleLiberacion) {
        this.numeroCamasPosibleLiberacion = numeroCamasPosibleLiberacion;
    }

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return Returns the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the habitaciones
	 */
	public ArrayList<HashMap<String, Object>> getHabitaciones() {
		return habitaciones;
	}

	/**
	 * @param habitaciones the habitaciones to set
	 */
	public void setHabitaciones(ArrayList<HashMap<String, Object>> habitaciones) {
		this.habitaciones = habitaciones;
	}

	/**
	 * @return the pisos
	 */
	public ArrayList<HashMap<String, Object>> getPisos() {
		return pisos;
	}

	/**
	 * @param pisos the pisos to set
	 */
	public void setPisos(ArrayList<HashMap<String, Object>> pisos) {
		this.pisos = pisos;
	}

	/**
	 * @return the tiposHabitaciones
	 */
	public ArrayList<HashMap<String, Object>> getTiposHabitaciones() {
		return tiposHabitaciones;
	}

	/**
	 * @param tiposHabitaciones the tiposHabitaciones to set
	 */
	public void setTiposHabitaciones(
			ArrayList<HashMap<String, Object>> tiposHabitaciones) {
		this.tiposHabitaciones = tiposHabitaciones;
	}

	/**
	 * @return the piso
	 */
	public String getPiso() {
		return piso;
	}

	/**
	 * @param piso the piso to set
	 */
	public void setPiso(String piso) {
		this.piso = piso;
	}

	/**
	 * @return the tipoHabitacion
	 */
	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	/**
	 * @param tipoHabitacion the tipoHabitacion to set
	 */
	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	/**
	 * @return the nombreHabitacion
	 */
	public String getNombreHabitacion() {
		return nombreHabitacion;
	}

	/**
	 * @param nombreHabitacion the nombreHabitacion to set
	 */
	public void setNombreHabitacion(String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}

	/**
	 * @return the nombrePiso
	 */
	public String getNombrePiso() {
		return nombrePiso;
	}

	/**
	 * @param nombrePiso the nombrePiso to set
	 */
	public void setNombrePiso(String nombrePiso) {
		this.nombrePiso = nombrePiso;
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
}