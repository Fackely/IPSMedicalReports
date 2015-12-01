/*
 * Creado en 2/08/2004
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.Utilidades;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class ActivacionCargosForm extends ValidatorForm
{
    /**
     * Estado para el manejo del flijo de la funcionalidad
     */
    private String estado;
    
    /*********************ATRIBUTOS PARA LA ACTIVACION/INACTIVACION CARGOS**************************************/
    //***********************ATRIBUTOS SECCION LISTADO DE CONVENIOS**************************************
    /**
	 * Listado de las convenios del ingreso del paciente
	 */
	private ArrayList<HashMap<String, Object>> listadoConvenios = new ArrayList<HashMap<String,Object>>();
	
	private String nombreConvenio;
	private String idSubCuenta;
	//******************************************************************************************************
	//**********************ATRIBUTOS SECCCION LISTADO DE SOLICITUDES****************************************
	/**
     * mAPA para el manejo de los listados de solicitudes
     */
    private HashMap<String,Object> listadoSolicitudes = new HashMap<String, Object>();
    
    private int numSolicitudes;
    private int maxPageItems ;
    private String linkSiguiente;
    private int offset;
    private String indice;
    private String ultimoIndice;
    private int posSolicitud;
	//*********************************************************************************************************
    //********************ATRIBUTOS SECCION DETALLE DE LA SOLICITUD********************************************
    /**
     * Mapa para el manejo del detalle de la solicitud
     */
    private HashMap<String, Object> detalleSolicitud = new HashMap<String, Object>();
    
    private String numeroSolicitud;
    private String orden;
    private int tipoSolicitud;
    
    //Atributos para la parte de portatil
    private HashMap<String, Object> detalleSolicitudPortatil = new HashMap<String, Object>();
    
    
    //Atributos relacionados con la solicitud de cirugia
    private HashMap<String, Object> cirugia = new HashMap<String, Object>();
    private int posCirugia;
    private String codigoServicioCx;
    private String tipoAsocio;
    private String tipoServicioAsocio;
    private String consecutivoAsocio;
    //*********************************************************************************************************
    /******************ATRIBUTOS PARA LA CONSULTA DE LAS INACTIVACIONES/ACTIVACIONES***************************/
    private String codigoConvenio;
    private HashMap<String, Object> consulta = new HashMap<String, Object>();
    private int numConsulta;
    /********************************************************************************************************/
    
    
    
    /**
     * Manejar el estado anterior para los listados de las cuentas
     */
    private String estadoAnterior;
    
  
    
    /**
     * Código de la cuenta que se desea modificar
     */
    private int codigoCuenta;
    
    /**
     * Cuando se activa un cargo que ya está activo no hay cambio
     */
    private boolean cambio;
    
    
    
	/**
	 * Codigo del procedimiento(axioma)
	 */
	private String codigoAxioma;
	
	/**
	 * Descripción del procedimiento
	 */
	private String descripcion;
	
	/**
	 * Cantidad de procedimientos
	 */
	private int cantidad;
	
	/**
	 * Valor del procedimiento
	 */
	private double valor;

	/**
	 * Moitvo de la modificación (activación ó inactivación) 
	 */
	private String motivo;
	
	/**
	 * Estado del monto
	 */
	private boolean activacion;

	/**
	 * Estado del monto temporar para mostrar la palabra activación ó inactivación
	 */
	private boolean activacionTempo;

	
    
    /**
     * Mostrar el nombre de la vía de ingreso
     */
    private String viaIngreso;
    
    /**
     * Nombre del estado actual de la cuenta
     */
    private String estadoCuenta;
    
    /**
     * Consecutivo de Ordenes Medicas
     */
    private int consecutivoOrdenesMedicas;
    
    /**
     * Método que verifica si el cargo tiene servicio portatil
     */
    private boolean tieneServicioPortatil;
    
    
   
    
    //*******ATRIBUTOS PARA LA SECCIÓN DE CIRUGÍAS********************
    /**
     * Objeto que almacena las cirugías de una solicitud Cx.
     */
    private HashMap cirugias;
    /**
     * Variable que almacena el número de cirugías
     */
    private int numCirugias;
  
    /**
     * Posicion del mapa de cirugías del asocio elegido
     */
    private int posAsocio;
    //****************************************************************
   
    /**
     * Método para resetear los atributos de la clase
     */
	public void reset()
	{
		this.estado="";
		//atributos para la seccion de los convenios----------------------------
		this.listadoConvenios = new ArrayList<HashMap<String,Object>>();
		this.nombreConvenio = "";
		this.idSubCuenta = "";
		
		//atributos para la seccion del listado de solicitudes----------------------------
		this.listadoSolicitudes = new HashMap<String, Object>();
		this.numSolicitudes = 0;
		this.maxPageItems = 10;
		this.linkSiguiente="";
		this.offset = 0;
		this.indice = "";
		this.ultimoIndice = "";
		this.posSolicitud = 0;
		
		
		//atributos para la seccion del detalle de la solicitud -------------------------
		this.numeroSolicitud="";
		this.orden = "";
		this.tipoSolicitud = 0;
		this.tieneServicioPortatil = false;
		
		//Atributos relacionados con la solicitud de cirugia
		this.cirugia = new HashMap<String, Object>();
		this.posCirugia = 0;
		this.codigoServicioCx = "";
		this.tipoAsocio = "";
		this.tipoServicioAsocio = "";
		this.consecutivoAsocio = "";
		
		//ATRIBUTOS PARA LA CONSULTA INACTIVACIONES/ACTIVACIONES CARGOS
		this.codigoConvenio = "";
		this.consulta = new HashMap<String, Object>();
		this.numConsulta = 0;
		
		
		
		
		
		
		
		codigoAxioma="";
		descripcion="";
		cantidad=0;
		valor=0;
		motivo="";
		activacion=false;
		cambio=false;
		codigoCuenta=0;
		estadoAnterior="";
		viaIngreso="";
		estadoCuenta="";
		
		
		
		//atributos para cirugias
		this.cirugias = new HashMap();
		this.numCirugias = 0;
		this.posCirugia = -1;
		this.posAsocio = -1;
		
    }
    
	
	
	/**
	 * @return the tieneServicioPortatil
	 */
	public boolean isTieneServicioPortatil() {
		return tieneServicioPortatil;
	}



	/**
	 * @param tieneServicioPortatil the tieneServicioPortatil to set
	 */
	public void setTieneServicioPortatil(boolean tieneServicioPortatil) {
		this.tieneServicioPortatil = tieneServicioPortatil;
	}



	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
	}
	/**
	 * @return Returns the consecutivoOrdenesMedicas.
	 */
	public int getConsecutivoOrdenesMedicas() {
		return consecutivoOrdenesMedicas;
	}
	/**
	 * @param consecutivoOrdenesMedicas The consecutivoOrdenesMedicas to set.
	 */
	public void setConsecutivoOrdenesMedicas(int consecutivoOrdenesMedicas) {
		this.consecutivoOrdenesMedicas = consecutivoOrdenesMedicas;
	}
  
    
    /**
     * @return Retorna estado.
     */
    public String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    
	
	
	/**
	 * @return the detalleSolicitud
	 */
	public HashMap<String, Object> getDetalleSolicitud() {
		return detalleSolicitud;
	}



	/**
	 * @param detalleSolicitud the detalleSolicitud to set
	 */
	public void setDetalleSolicitud(HashMap<String, Object> detalleSolicitud) {
		this.detalleSolicitud = detalleSolicitud;
	}
	
	/**
	 * @return retorna elemento del mapa  detalleSolicitud
	 */
	public Object getDetalleSolicitud(String key) {
		return detalleSolicitud.get(key);
	}



	/**
	 * @param Asigna elemento al mapa detalleSolicitud 
	 */
	public void setDetalleSolicitud(String key,Object obj) {
		this.detalleSolicitud.put(key,obj);
	}



	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}



	/**
	 * @return Retorna cantidad.
	 */
	public int getCantidad()
	{
		return cantidad;
	}
	
	/**
	 * @param cantidad Asigna cantidad.
	 */
	public void setCantidad(int cantidad)
	{
		this.cantidad = cantidad;
	}
	
	/**
	 * @return Retorna codigoAxioma.
	 */
	public String getCodigoAxioma()
	{
		return codigoAxioma;
	}
	
	/**
	 * @param codigoAxioma Asigna codigoAxioma.
	 */
	public void setCodigoAxioma(String codigoAxioma)
	{
		this.codigoAxioma = codigoAxioma;
	}
	/**
	 * @return Retorna descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	/**
	 * @param descripcion Asigna descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	/**
	 * @return Retorna valor.
	 */
	public double getValor()
	{
		return valor;
	}
	/**
	 * @param valor Asigna valor.
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}
	/**
	 * @return Retorna motivo.
	 */
	public String getMotivo()
	{
		return motivo;
	}
	/**
	 * @param motivo Asigna motivo.
	 */
	public void setMotivo(String motivo)
	{
		this.motivo = motivo;
	}
	/**
	 * @return Retorna activacion.
	 */
	public boolean getActivacion()
	{
		return activacion;
	}
	/**
	 * @param activacion Asigna activacion.
	 */
	public void setActivacion(boolean activacion)
	{
		this.activacion = activacion;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("guardar"))
		{
			if(motivo.equals("") || !Utilidades.validarEspacios(motivo))
			{
				errores.add("motivo", new ActionMessage("errors.required","El motivo"));
			}
		}
		return errores;
	}

	/**
	 * @return Retorna cambio.
	 */
	public boolean getCambio()
	{
		return cambio;
	}
	
	/**
	 * @param cambio Asigna cambio.
	 */
	public void setCambio(boolean cambio)
	{
		this.cambio = cambio;
	}
	
	/**
	 * @return Retorna codigoCuenta.
	 */
	public int getCodigoCuenta()
	{
		return codigoCuenta;
	}
	
	/**
	 * @param codigoCuenta Asigna codigoCuenta.
	 */
	public void setCodigoCuenta(int codigoCuenta)
	{
		this.codigoCuenta = codigoCuenta;
	}
	

	
	/**
	 * @return Retorna estadoAnterior.
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}
	
	/**
	 * @param estadoAnterior Asigna estadoAnterior.
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
	}
	/**
	 * @return Retorna activacionTempo.
	 */
	public boolean getActivacionTempo()
	{
		return activacionTempo;
	}
	/**
	 * @param activacionTempo Asigna activacionTempo.
	 */
	public void setActivacionTempo(boolean activacionTempo)
	{
		this.activacionTempo = activacionTempo;
	}
	/**
	 * @return Retorna estadoCuenta.
	 */
	public String getEstadoCuenta()
	{
		return estadoCuenta;
	}
	/**
	 * Asigna estadoCuenta
	 * @param estadoCuenta.
	 */
	public void setEstadoCuenta(String estadoCuenta)
	{
		this.estadoCuenta = estadoCuenta;
	}
	/**
	 * @return Retorna viaIngreso.
	 */
	public String getViaIngreso()
	{
		return viaIngreso;
	}
	/**
	 * Asigna viaIngreso
	 * @param viaIngreso.
	 */
	public void setViaIngreso(String viaIngreso)
	{
		this.viaIngreso = viaIngreso;
	}
	/**
	 * @return Returns the cirugias.
	 */
	public HashMap getCirugias() {
		return cirugias;
	}
	/**
	 * @param cirugias The cirugias to set.
	 */
	public void setCirugias(HashMap cirugias) {
		this.cirugias = cirugias;
	}
	/**
	 * @return Retorna un elemento para el mapa cirugias.
	 */
	public Object getCirugias(String key) {
		return cirugias.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa cirugias.
	 */
	public void setCirugias(String key,Object obj) {
		this.cirugias.put(key,obj);
	}
	/**
	 * @return Returns the numCirugias.
	 */
	public int getNumCirugias() {
		return numCirugias;
	}
	/**
	 * @param numCirugias The numCirugias to set.
	 */
	public void setNumCirugias(int numCirugias) {
		this.numCirugias = numCirugias;
	}
	/**
	 * @return Returns the tipoSolicitud.
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}
	/**
	 * @param tipoSolicitud The tipoSolicitud to set.
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	/**
	 * @return Returns the posAsocio.
	 */
	public int getPosAsocio() {
		return posAsocio;
	}
	/**
	 * @param posAsocio The posAsocio to set.
	 */
	public void setPosAsocio(int posAsocio) {
		this.posAsocio = posAsocio;
	}
	/**
	 * @return Returns the posCirugia.
	 */
	public int getPosCirugia() {
		return posCirugia;
	}
	/**
	 * @param posCirugia The posCirugia to set.
	 */
	public void setPosCirugia(int posCirugia) {
		this.posCirugia = posCirugia;
	}



	/**
	 * @return the listadoConvenios
	 */
	public ArrayList<HashMap<String, Object>> getListadoConvenios() {
		return listadoConvenios;
	}



	/**
	 * @param listadoConvenios the listadoConvenios to set
	 */
	public void setListadoConvenios(
			ArrayList<HashMap<String, Object>> listadoConvenios) {
		this.listadoConvenios = listadoConvenios;
	}



	/**
	 * @return the idSubCuenta
	 */
	public String getIdSubCuenta() {
		return idSubCuenta;
	}



	/**
	 * @param idSubCuenta the idSubCuenta to set
	 */
	public void setIdSubCuenta(String idSubCuenta) {
		this.idSubCuenta = idSubCuenta;
	}



	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}



	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}



	/**
	 * @return the listadoSolicitudes
	 */
	public HashMap<String, Object> getListadoSolicitudes() {
		return listadoSolicitudes;
	}



	/**
	 * @param listadoSolicitudes the listadoSolicitudes to set
	 */
	public void setListadoSolicitudes(HashMap<String, Object> listadoSolicitudes) {
		this.listadoSolicitudes = listadoSolicitudes;
	}
	
	/**
	 * @return retorna elemento del mapa listadoSolicitudes
	 */
	public Object getListadoSolicitudes(String key) {
		return listadoSolicitudes.get(key);
	}



	/**
	 * @param Asigna elemento al mapa listadoSolicitudes 
	 */
	public void setListadoSolicitudes(String key,Object obj) {
		this.listadoSolicitudes.put(key,obj);
	}



	/**
	 * @return the numSolicitudes
	 */
	public int getNumSolicitudes() {
		return numSolicitudes;
	}



	/**
	 * @param numSolicitudes the numSolicitudes to set
	 */
	public void setNumSolicitudes(int numSolicitudes) {
		this.numSolicitudes = numSolicitudes;
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
	 * @return the orden
	 */
	public String getOrden() {
		return orden;
	}



	/**
	 * @param orden the orden to set
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}



	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}



	/**
	 * @return the cirugia
	 */
	public HashMap<String, Object> getCirugia() {
		return cirugia;
	}



	/**
	 * @param cirugia the cirugia to set
	 */
	public void setCirugia(HashMap<String, Object> cirugia) {
		this.cirugia = cirugia;
	}
	
	/**
	 * @return the cirugia
	 */
	public Object getCirugia(String key) {
		return cirugia.get(key);
	}



	/**
	 * @param Asigna elemento al mapa cirugia 
	 */
	public void setCirugia(String key,Object obj) {
		this.cirugia.put(key,obj);
	}



	/**
	 * @return the codigoServicioCx
	 */
	public String getCodigoServicioCx() {
		return codigoServicioCx;
	}



	/**
	 * @param codigoServicioCx the codigoServicioCx to set
	 */
	public void setCodigoServicioCx(String codigoServicioCx) {
		this.codigoServicioCx = codigoServicioCx;
	}



	/**
	 * @return the tipoAsocio
	 */
	public String getTipoAsocio() {
		return tipoAsocio;
	}



	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(String tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}



	/**
	 * @return the codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}



	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}



	/**
	 * @return the consulta
	 */
	public HashMap<String, Object> getConsulta() {
		return consulta;
	}



	/**
	 * @param consulta the consulta to set
	 */
	public void setConsulta(HashMap<String, Object> consulta) {
		this.consulta = consulta;
	}
	
	/**
	 * @return the consulta
	 */
	public Object getConsulta(String key) {
		return consulta.get(key);
	}



	/**
	 * @param Asigna elemento al mapa consulta 
	 */
	public void setConsulta(String key,Object obj) {
		this.consulta.put(key, obj);
	}



	/**
	 * @return the numConsulta
	 */
	public int getNumConsulta() {
		return numConsulta;
	}



	/**
	 * @param numConsulta the numConsulta to set
	 */
	public void setNumConsulta(int numConsulta) {
		this.numConsulta = numConsulta;
	}



	/**
	 * @return the consecutivoAsocio
	 */
	public String getConsecutivoAsocio() {
		return consecutivoAsocio;
	}



	/**
	 * @param consecutivoAsocio the consecutivoAsocio to set
	 */
	public void setConsecutivoAsocio(String consecutivoAsocio) {
		this.consecutivoAsocio = consecutivoAsocio;
	}



	/**
	 * @return the tipoServicioAsocio
	 */
	public String getTipoServicioAsocio() {
		return tipoServicioAsocio;
	}



	/**
	 * @param tipoServicioAsocio the tipoServicioAsocio to set
	 */
	public void setTipoServicioAsocio(String tipoServicioAsocio) {
		this.tipoServicioAsocio = tipoServicioAsocio;
	}



	/**
	 * @return the detalleSolicitudPortatil
	 */
	public HashMap<String, Object> getDetalleSolicitudPortatil() {
		return detalleSolicitudPortatil;
	}



	/**
	 * @param detalleSolicitudPortatil the detalleSolicitudPortatil to set
	 */
	public void setDetalleSolicitudPortatil(
			HashMap<String, Object> detalleSolicitudPortatil) {
		this.detalleSolicitudPortatil = detalleSolicitudPortatil;
	}
	
	/**
	 * @return the detalleSolicitudPortatil
	 */
	public Object getDetalleSolicitudPortatil(String key) {
		return detalleSolicitudPortatil.get(key);
	}



	/**
	 * @param detalleSolicitudPortatil the detalleSolicitudPortatil to set
	 */
	public void setDetalleSolicitudPortatil(String key,Object obj) {
		this.detalleSolicitudPortatil.put(key,obj);
	}
}
