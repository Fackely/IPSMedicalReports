/*
 * @(#)ArqueosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * los arqueos --> presuntivos - definitivos - cierres caja
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Abr 19, 2006
 * @author wrios 
 */
public class ArqueosForm extends ValidatorForm 
{
	/**
	 * indica el tipo de arqueo:
	 * 394 --> arqueo Provisional
	 * 395 --> arqueo Definitivo 
	 * 396 --> cierre caja
	 */
	private int codigoFuncionalidadTipoArqueo;
	
   /**
    * Estado en el que se encuentra el proceso.       
    */
    private String estado;
	
   /**
    * Mapa que contiene la informacion de los reciboscaja
    */
    private HashMap recibosCajaMap= new HashMap();
    
    /**
     * fecha de arqueo - fecha cierre 
     */
    private String fechaDDMMYYYY; 
    
    /**
     * login del usuario/cajero al cual se le va 
     * ha generar el arqueo - cierre 
     */
    private String loginUsuarioCajero;
    
    /**
     * consecutivo (PK) de la caja
     */
    private String consecutivoCaja;
    
    /**
     * codigo del centro de atencion
     */
    private int codigoCentroAtencion;
    
    /**
     * fecha de generacion de la consulta para evitar problemas
     * de concurrencia
     */
    private String fechaGeneracionConsulta;
    
    /**
     * hora de generacion de la consulta para evitar problemas
     * de concurrencia
     */
	private String horaGeneracionConsulta;
    
	/**
	 * conexion transaccional utilizada para los bloqueos
	 */
	private Connection conexionTransaccionalParaBloqueos;
	
	/**
	 * cpnsecutivo de arqueo
	 */
	private String consecutivoArqueo;
	
	/**
	 * fecha inicial consulta arqueos cierres
	 */
	private String fechaInicialConsultaArqueosCierres;
	
	/**
	 * fecha final consulta arqueos cierres
	 */
	private String fechaFinalConsultaArqueosCierres;
	
	/**
	 * Colección 
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
	 * 
	 */
    private HashMap cajasPpalTagMap;
    
	
	/**
	 * codigo del tipo de arqueo, utilizado en consulta
	 */
	private String codigoTipoArqueoConsultaStr;
	
	/**
	 * 
	 */
	private String consecutivoCajaPpal;
	
	/**
	 * 
	 */
	private String linkVolver="";
	
    /**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.codigoFuncionalidadTipoArqueo=-1;
    	this.recibosCajaMap= new HashMap();
    	this.fechaDDMMYYYY="";
    	this.loginUsuarioCajero="";
    	this.consecutivoCaja="";
    	this.codigoCentroAtencion=-1;
    	this.fechaGeneracionConsulta="";
    	this.horaGeneracionConsulta="";
    	this.conexionTransaccionalParaBloqueos=null;
    	this.consecutivoArqueo="";
    	this.cajasPpalTagMap= new HashMap();
    	this.consecutivoCajaPpal="";
    }
    
    /**
     * 
     * @param loginUsuario
     * @param codigoInstitucion
     * @param codigoTipoCaja
     */
    public void inicializarTags(String loginUsuario, int codigoInstitucion, int codigoCentroAtencion)
    {
    	this.cajasPpalTagMap= Utilidades.obtenerCajasCajero(loginUsuario, codigoInstitucion, ConstantesBD.codigoTipoCajaPpal, codigoCentroAtencion);
    }
    
    /**
	 * resetea los atributos para la consulta 
	 *
	 */
	public void resetConsultaArqueosCierres()
	{
		this.fechaInicialConsultaArqueosCierres="";
		this.fechaFinalConsultaArqueosCierres="";
		this.loginUsuarioCajero="";
		this.consecutivoCaja="";
		this.codigoCentroAtencion=-1;
		this.codigoTipoArqueoConsultaStr="";
		this.col= new ArrayList();
		this.consecutivoCajaPpal="";
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
        errores=super.validate(mapping,request);
        if(!estado.equals("empezar") && !estado.equals("empezarConsulta") 
        	&& !estado.equals("consultarArqueosCierres") && !estado.equals("empezarConsultaConAvisoErrores") 
        	&& !estado.equals("ordenar")
        	&& !estado.equals("cerrarConexiones"))
    	{	
    	    if(codigoFuncionalidadTipoArqueo<=0)
				errores.add("Campo Tipo Arqueo", new ActionMessage("errors.required","El campo Tipo Arqueo"));
			if(loginUsuarioCajero.trim().equals(""))
				errores.add("Campo Usuario Cajero", new ActionMessage("errors.required","El campo Cajero"));
			if(consecutivoCaja.trim().equals(""))
				errores.add("Campo Caja", new ActionMessage("errors.required","El campo Caja"));
			if(!UtilidadFecha.esFechaValidaSegunAp(fechaDDMMYYYY))
				errores.add("Fecha Arqueo Invalida", new ActionMessage("errors.formatoFechaInvalido", "Arqueo "+this.getTituloDependiendoTipoArqueo().toLowerCase()));
			else
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaDDMMYYYY, UtilidadFecha.getFechaActual()))
					errores.add("Fecha", new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaDDMMYYYY(), UtilidadFecha.getFechaActual()+" actual"));
				//errors.fechaAnteriorIgualActual = La fecha {0} debe ser posterior o igual a la fecha {1}. [aa-21]
			}
			
			if(this.codigoFuncionalidadTipoArqueo==ConstantesBD.codigoFuncionalidadTipoArqueoCierre && !estado.equals("detalleConsulta") && !estado.equals("generarReporte"))
			{
				if(this.consecutivoCajaPpal.trim().equals(""))
					errores.add("Campo Caja", new ActionMessage("errors.required","El campo Caja Ppal"));
			}
    	}
        else if(estado.equals("consultarArqueosCierres"))
        {
        	if(!UtilidadFecha.esFechaValidaSegunAp(fechaInicialConsultaArqueosCierres))
        		errores.add("Fecha Inicial Invalida", new ActionMessage("errors.formatoFechaInvalido", "Inicial "));
        	else
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicialConsultaArqueosCierres, UtilidadFecha.getFechaActual()))
					errores.add("Fecha Inicial", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getFechaInicialConsultaArqueosCierres(), UtilidadFecha.getFechaActual()+" actual"));
			}
        	if(!UtilidadFecha.esFechaValidaSegunAp(fechaFinalConsultaArqueosCierres))
        		errores.add("Fecha Final Invalida", new ActionMessage("errors.formatoFechaInvalido", "Final "));
        	else
        	{
        		if(errores.isEmpty())
        		{	
	        		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicialConsultaArqueosCierres, fechaFinalConsultaArqueosCierres))
						errores.add("Fecha", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getFechaInicialConsultaArqueosCierres(), this.getFechaFinalConsultaArqueosCierres()+" final"));
	        		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFinalConsultaArqueosCierres, UtilidadFecha.getFechaActual()))
						errores.add("Fecha Final", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.getFechaFinalConsultaArqueosCierres(), UtilidadFecha.getFechaActual()+" actual"));
        		}	
        	}
        	if(codigoTipoArqueoConsultaStr.equals(""))
        		errores.add("Campo Tipo Arqueo", new ActionMessage("errors.required","El campo Tipo Arqueo"));
        }
        if(!errores.isEmpty())
        {
        	if(estado.equals("generarArqueo"))
        		this.setEstado("empezarConAvisoErrores");
        	else if(estado.equals("consultarArqueosCierres"))
        		this.setEstado("empezarConsultaConAvisoErrores");
        }
        return errores;
    }

	/**
	 * @return Returns the consecutivoCaja.
	 */
	public String getConsecutivoCaja() {
		return consecutivoCaja;
	}

	/**
	 * @param consecutivoCaja The consecutivoCaja to set.
	 */
	public void setConsecutivoCaja(String consecutivoCaja) {
		this.consecutivoCaja = consecutivoCaja;
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
	 * @return Returns the fechaDDMMYYYY.
	 */
	public String getFechaDDMMYYYY() {
		return fechaDDMMYYYY;
	}

	/**
	 * @param fechaDDMMYYYY The fechaDDMMYYYY to set.
	 */
	public void setFechaDDMMYYYY(String fechaDDMMYYYY) {
		this.fechaDDMMYYYY = fechaDDMMYYYY;
	}

	/**
	 * @return Returns the loginUsuarioCajero.
	 */
	public String getLoginUsuarioCajero() {
		return loginUsuarioCajero;
	}

	/**
	 * @param loginUsuarioCajero The loginUsuarioCajero to set.
	 */
	public void setLoginUsuarioCajero(String loginUsuarioCajero) {
		this.loginUsuarioCajero = loginUsuarioCajero;
	}

	/**
	 * @return Returns the recibosCajaMap.
	 */
	public HashMap getRecibosCajaMap() {
		return recibosCajaMap;
	}

	/**
	 * @param recibosCajaMap The recibosCajaMap to set.
	 */
	public void setRecibosCajaMap(HashMap recibosCajaMap) {
		this.recibosCajaMap = recibosCajaMap;
	}

	 /**
     * Set del mapa 
     * @param key
     * @param value
     */
    public void setRecibosCajaMap(String key, Object value) 
    {
        recibosCajaMap.put(key, value);
    }
    
    /**
     * Get del mapa 
     * Retorna el valor de un campo dado su nombre
     */
    public Object getRecibosCajaMap(String key) 
    {
        return recibosCajaMap.get(key);
    }
    
    /**
     * obtiene el titulo dependiendo del tipo de arqueo
     */
    public String getTituloDependiendoTipoArqueo()
    {
    	String str="";
    	if(codigoFuncionalidadTipoArqueo==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional)
    		str="ARQUEO PROVISIONAL";
    	else if(codigoFuncionalidadTipoArqueo==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
    		str="ARQUEO DEFINITIVO";
    	else if(codigoFuncionalidadTipoArqueo==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
    		str="CIERRE CAJA";
    	else
    		str="";
    	return str;
    }

	/**
	 * @return Returns the codigoFuncionalidadTipoArqueo.
	 */
	public int getCodigoFuncionalidadTipoArqueo() {
		return codigoFuncionalidadTipoArqueo;
	}

	/**
	 * @param codigoFuncionalidadTipoArqueo The codigoFuncionalidadTipoArqueo to set.
	 */
	public void setCodigoFuncionalidadTipoArqueo(int codigoFuncionalidadTipoArqueo) {
		this.codigoFuncionalidadTipoArqueo = codigoFuncionalidadTipoArqueo;
	}

	/**
	 * @return Returns the fechaGeneracionConsulta.
	 */
	public String getFechaGeneracionConsulta() {
		return fechaGeneracionConsulta;
	}

	/**
	 * @param fechaGeneracionConsulta The fechaGeneracionConsulta to set.
	 */
	public void setFechaGeneracionConsulta(String fechaGeneracionConsulta) {
		this.fechaGeneracionConsulta = fechaGeneracionConsulta;
	}

	/**
	 * @return Returns the horaGeneracionConsulta.
	 */
	public String getHoraGeneracionConsulta() {
		return horaGeneracionConsulta;
	}

	/**
	 * @param horaGeneracionConsulta The horaGeneracionConsulta to set.
	 */
	public void setHoraGeneracionConsulta(String horaGeneracionConsulta) {
		this.horaGeneracionConsulta = horaGeneracionConsulta;
	}

	/**
	 * @return Returns the conexionTransaccionalParaBloqueos.
	 */
	public Connection getConexionTransaccionalParaBloqueos() {
		return conexionTransaccionalParaBloqueos;
	}

	/**
	 * @param conexionTransaccionalParaBloqueos The conexionTransaccionalParaBloqueos to set.
	 */
	public void setConexionTransaccionalParaBloqueos(
			Connection conexionTransaccionalParaBloqueos) {
		this.conexionTransaccionalParaBloqueos = conexionTransaccionalParaBloqueos;
	}

	/**
	 * @return Returns the consecutivoArqueo.
	 */
	public String getConsecutivoArqueo() {
		return consecutivoArqueo;
	}

	/**
	 * @param consecutivoArqueo The consecutivoArqueo to set.
	 */
	public void setConsecutivoArqueo(String consecutivoArqueo) {
		this.consecutivoArqueo = consecutivoArqueo;
	}

	/**
	 * @return Returns the fechaFinalConsultaArqueosCierres.
	 */
	public String getFechaFinalConsultaArqueosCierres() {
		return fechaFinalConsultaArqueosCierres;
	}

	/**
	 * @param fechaFinalConsultaArqueosCierres The fechaFinalConsultaArqueosCierres to set.
	 */
	public void setFechaFinalConsultaArqueosCierres(
			String fechaFinalConsultaArqueosCierres) {
		this.fechaFinalConsultaArqueosCierres = fechaFinalConsultaArqueosCierres;
	}

	/**
	 * @return Returns the fechaInicialConsultaArqueosCierres.
	 */
	public String getFechaInicialConsultaArqueosCierres() {
		return fechaInicialConsultaArqueosCierres;
	}

	/**
	 * @param fechaInicialConsultaArqueosCierres The fechaInicialConsultaArqueosCierres to set.
	 */
	public void setFechaInicialConsultaArqueosCierres(
			String fechaInicialConsultaArqueosCierres) {
		this.fechaInicialConsultaArqueosCierres = fechaInicialConsultaArqueosCierres;
	}
	
	 /**
	 * Retorna Colección 
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	/**
	 * Asigna Colección 
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	/**
	 * size de la coleccion
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
	 * @return Returns the columna.
	 */
	public String getColumna() {
		return columna;
	}

	/**
	 * @param columna The columna to set.
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}

	/**
	 * @return Returns the ultimaPropiedad.
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}

	/**
	 * @param ultimaPropiedad The ultimaPropiedad to set.
	 */
	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return Returns the codigoTipoArqueoConsultaStr.
	 */
	public String getCodigoTipoArqueoConsultaStr() {
		return codigoTipoArqueoConsultaStr;
	}

	/**
	 * @param codigoTipoArqueoConsultaStr The codigoTipoArqueoConsultaStr to set.
	 */
	public void setCodigoTipoArqueoConsultaStr(String codigoTipoArqueoConsultaStr) {
		this.codigoTipoArqueoConsultaStr = codigoTipoArqueoConsultaStr;
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
	 * @return the cajasPpalTagMap
	 */
	public HashMap getCajasPpalTagMap() {
		return cajasPpalTagMap;
	}


	/**
	 * @param cajasPpalTagMap the cajasPpalTagMap to set
	 */
	public void setCajasPpalTagMap(HashMap cajasPpalTagMap) {
		this.cajasPpalTagMap = cajasPpalTagMap;
	}

	/**
	 * @return the cajasPpalTagMap
	 */
	public String getCajasPpalTagMap(String key) {
		return cajasPpalTagMap.get(key).toString();
	}

	/**
	 * @return the consecutivoCajaPpal
	 */
	public String getConsecutivoCajaPpal() {
		return consecutivoCajaPpal;
	}

	/**
	 * @param consecutivoCajaPpal the consecutivoCajaPpal to set
	 */
	public void setConsecutivoCajaPpal(String consecutivoCajaPpal) {
		this.consecutivoCajaPpal = consecutivoCajaPpal;
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
	
}