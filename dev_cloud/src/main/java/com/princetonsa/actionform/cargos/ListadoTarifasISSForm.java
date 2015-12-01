/*
 * @(#)ListadoTarifasISS.java
 * 
 * Created on 04-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.actionform.cargos;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos generales de un conjunto de tarifas iss. Y adicionalmente hace el manejo de reset 
 * de la forma y de validación de errores de datos. 
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class ListadoTarifasISSForm extends ValidatorForm
{
	private String columna;
	private String ultimaPropiedad;
	/**
	 * accion a realizar sobre la tarifa
	 */
	private String accion;
	/**
	 * Criterio(s) de búsqueda elegido(s).
	 */
	private String criteriosBusqueda[];
	/**
	 * Código de la tarifa en el sistema
	 */
	private int codigo;

	/**
	 * Código del esquema tarifario correspondiente a esta tarifa
	 */
	private int codigoEsquemaTarifario;

	/**
	 * Nombre del esquema tarifario correspondiente a esta tarifa
	 */
	private String nombreEsquemaTarifario;

	/**
	 * Código del servicio que corresponde a esta tarifa
	 */
	private int codigoServicio;

	/**
	 * Nombre del servicio que corresponde a esta tarifa
	 */
	private String nombreServicio;

	/**
	 * Código de la especialidad del servicio que corresponde a esta tarifa
	 */
	private int codigoEspecialidad;

	/**
	 * Nombre de la especialidad del servicio que corresponde a esta tarifa
	 */
	private String nombreEspecialidad;	
	
	/**
	 * Código del tipo de liquidacion de la tarifa
	 */
	private int codigoTipoLiquidacion;

	/**
	 * Valor de la tarifa del servicio que corresponde a esta tarifa
	 */
	private double valorTarifa;

	/**
	 * Porcentaje del iva aplicable a el servicio que corresponde a esta tarifa
	 */
	private double porcentajeIva;

	/**
	 * tipo de modificacion al resultado de la busqueda
	 */
	private String tipoModificacion;
	
	/**
	 * Liqidacion Asocios
	 * */
	private String liquidarAsocios;
	
	
	/**
	 * Estado dentro del flujo
	 */
	private String estado;
		
	/**
	 * Lista con las tarifas iss
	 */
	private ArrayList tarifasISS;
	
	/**
	 * número de filas del paginador
	 */
	private int maxPageItems;
	
	
	 /**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
	
    /**
     * Se agrega este campo por la Tarea 39240, la cual indica que debe
     * realizar la consulta de tarifas no por el codigo del servicio sino
     * por el codigo cups, el cual es el campo codigo propietario el cual
     * es un varchar en la tabla referencias_servicios
     */
    private String codigoServicioPropietario;
    
    /**
     * Variable para el manejo de Fecha Vigencia
     */
    private String fechaVigencia;
    
    /**
     * Mapa para almacenar las fechas de vigencia por esquema servicio
     */
    private HashMap fechasVigenciaMap;
    
    /**
     * Variable para almacenar los codigos de los servicios consultados
     */
    private String cadenaCodigosServicios;
    
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
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			if(this.criteriosBusqueda!=null)
			{
				if(this.criteriosBusqueda.length>0)
				{
					for(int i=0;i<criteriosBusqueda.length;i++)
					{
						if( this.codigoEsquemaTarifario <= 0 && this.criteriosBusqueda[i].equals("codigoEsquemaTarifario"))
							errores.add("esquema tarifario requerido", new ActionMessage("errors.required", "El esquema tarifario"));
						if( this.codigoServicioPropietario.equals("") && this.criteriosBusqueda[i].equals("codigoServicio"))
							errores.add("servicio requerido", new ActionMessage("errors.required", "El servicio"));
						//Se quita validación  por tarea 156795
//						if( this.valorTarifa <= 0.0 && this.criteriosBusqueda[i].equals("valorTarifa"))
//							errores.add("valor negativo", new ActionMessage("errors.floatMayorQue", "La tarifa por valor", "0"));
					}
				}
			}
		}
		return errores;
		
	}	
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset(int codigoInstitucion) 
	{
		this.tarifasISS = new ArrayList();		
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.codigoEsquemaTarifario = ConstantesBD.codigoNuncaValido;
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.criteriosBusqueda = null;
		this.nombreEspecialidad = "";
		this.nombreEsquemaTarifario = "";
		this.nombreServicio = "";
		this.porcentajeIva = 0.0;
		this.tarifasISS = new ArrayList();
		this.codigoTipoLiquidacion = ConstantesBD.codigoNuncaValido;
		this.maxPageItems = 0;
		this.valorTarifa = 0.0;
		this.index = ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda = UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.liquidarAsocios = ConstantesBD.acronimoNo;
        this.inicializarTagMap(codigoInstitucion);
        this.codigoServicioPropietario = "";
        this.fechaVigencia="";
        this.cadenaCodigosServicios="";
        this.fechasVigenciaMap = new HashMap();
        this.fechasVigenciaMap.put("numRegistros", "0");
	}	
	
	/**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }
	
	
	/**
	 * Retorna la tarifa iss correspondiente al indice dado dentro de la colección.
	 * Si el indice es mayor o igual al tamaño de la colección retorna null
	 * @param indice. int, indice a retornar de la colección
	 * @return
	 */
	public TarifaISSForm getTarifaISS(int indice)
	{
		if( indice >= this.tarifasISS.size() )
			return null;
		else
			return (TarifaISSForm)this.tarifasISS.get(indice);		
	}
	
	/**
	 * Adiciona al final de la colección la tarifa dada.
	 * @param tarifa. TarifaISSForm, tarifa ISS a ingresar
	 */
	public void setTarifaISS(TarifaISSForm tarifa)
	{
		this.tarifasISS.add(tarifa);
	}
	
	/**
	 * Retorna el número de tarifas ISS existentes en la colección
	 * @return
	 */
	public int getNumTarifasISS()
	{
		return this.tarifasISS.size();
	}
	
	/**
	 * Retorna la lista con las tarifas iss
	 * @return
	 */
	public ArrayList getTarifasISS()
	{
		return tarifasISS;
	}

	/**
	 * Asigna la lista con las tarifas iss
	 * @param list
	 */
	public void setTarifasISS(ArrayList list)
	{
		tarifasISS = list;
	}
	/**
	 * Retorna el estado dentro del flujo
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado dentro del flujo
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Returns the tipoModificacion.
	 * @return String
	 */
	public String getTipoModificacion()
	{
		return tipoModificacion;
	}

	/**
	 * Sets the tipoModificacion.
	 * @param tipoModificacion The tipoModificacion to set
	 */
	public void setTipoModificacion(String tipoModificacion)
	{
		this.tipoModificacion = tipoModificacion;
	}

	/**
	 * Returns the codigo.
	 * @return int
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Returns the codigoEspecialidad.
	 * @return int
	 */
	public int getCodigoEspecialidad()
	{
		return codigoEspecialidad;
	}

	/**
	 * Returns the codigoEsquemaTarifario.
	 * @return int
	 */
	public int getCodigoEsquemaTarifario()
	{
		return codigoEsquemaTarifario;
	}

	/**
	 * Returns the codigoServicio.
	 * @return int
	 */
	public int getCodigoServicio()
	{
		return codigoServicio;
	}

	/**
	 * Returns the criteriosBusqueda.
	 * @return String[]
	 */
	public String[] getCriteriosBusqueda()
	{
		return criteriosBusqueda;
	}

	/**
	 * Returns the nombreEspecialidad.
	 * @return String
	 */
	public String getNombreEspecialidad()
	{
		return nombreEspecialidad;
	}

	/**
	 * Returns the nombreEsquemaTarifario.
	 * @return String
	 */
	public String getNombreEsquemaTarifario()
	{
		return nombreEsquemaTarifario;
	}

	/**
	 * Returns the nombreServicio.
	 * @return String
	 */
	public String getNombreServicio()
	{
		return nombreServicio;
	}

	/**
	 * Returns the porcentajeIva.
	 * @return double
	 */
	public double getPorcentajeIva()
	{
		return porcentajeIva;
	}

	/**
	 * Returns the valorTarifa.
	 * @return double
	 */
	public double getValorTarifa()
	{
		return valorTarifa;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Sets the codigoEspecialidad.
	 * @param codigoEspecialidad The codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * Sets the codigoEsquemaTarifario.
	 * @param codigoEsquemaTarifario The codigoEsquemaTarifario to set
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario)
	{
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}

	/**
	 * Sets the codigoServicio.
	 * @param codigoServicio The codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Sets the criteriosBusqueda.
	 * @param criteriosBusqueda The criteriosBusqueda to set
	 */
	public void setCriteriosBusqueda(String[] criteriosBusqueda)
	{
		this.criteriosBusqueda = criteriosBusqueda;
	}

	/**
	 * Sets the nombreEspecialidad.
	 * @param nombreEspecialidad The nombreEspecialidad to set
	 */
	public void setNombreEspecialidad(String nombreEspecialidad)
	{
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * Sets the nombreEsquemaTarifario.
	 * @param nombreEsquemaTarifario The nombreEsquemaTarifario to set
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario)
	{
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}

	/**
	 * Sets the nombreServicio.
	 * @param nombreServicio The nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio)
	{
		this.nombreServicio = nombreServicio;
	}

	/**
	 * Sets the porcentajeIva.
	 * @param porcentajeIva The porcentajeIva to set
	 */
	public void setPorcentajeIva(double porcentajeIva)
	{
		this.porcentajeIva = porcentajeIva;
	}

	/**
	 * Sets the valorTarifa.
	 * @param valorTarifa The valorTarifa to set
	 */
	public void setValorTarifa(double valorTarifa)
	{
		this.valorTarifa = valorTarifa;
	}

	/**
	 * Returns the accion.
	 * @return String
	 */
	public String getAccion()
	{
		return accion;
	}

	/**
	 * Sets the accion.
	 * @param accion The accion to set
	 */
	public void setAccion(String accion)
	{
		this.accion = accion;
	}

	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

    /**
     * @return Returns the codigoTipoLiquidacion.
     */
    public int getCodigoTipoLiquidacion() {
        return codigoTipoLiquidacion;
    }
    /**
     * @param codigoTipoLiquidacion The codigoTipoLiquidacion to set.
     */
    public void setCodigoTipoLiquidacion(int codigoTipoLiquidacion) {
        this.codigoTipoLiquidacion = codigoTipoLiquidacion;
    }
	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	
	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean getManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the liquidacionAsocios
	 */
	public String getLiquidarAsocios() {
		return liquidarAsocios;
	}

	/**
	 * @param liquidacionAsocios the liquidacionAsocios to set
	 */
	public void setLiquidarAsocios(String liquidacionAsocios) {
		this.liquidarAsocios = liquidacionAsocios;
	}

	/**
	 * @return the codigoServicioPropietario
	 */
	public String getCodigoServicioPropietario() {
		return codigoServicioPropietario;
	}

	/**
	 * @param codigoServicioPropietario the codigoServicioPropietario to set
	 */
	public void setCodigoServicioPropietario(String codigoServicioPropietario) {
		this.codigoServicioPropietario = codigoServicioPropietario;
	}

	/**
	 * @return the fechaVigencia
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * @param fechaVigencia the fechaVigencia to set
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	/**
	 * @return the fechasVigenciaMap
	 */
	public HashMap getFechasVigenciaMap() {
		return fechasVigenciaMap;
	}

	/**
	 * @param fechasVigenciaMap the fechasVigenciaMap to set
	 */
	public void setFechasVigenciaMap(HashMap fechasVigenciaMap) {
		this.fechasVigenciaMap = fechasVigenciaMap;
	}
	
	public Object getFechasVigenciaMap(String key) {
		return fechasVigenciaMap.get(key);
	}

	public void setFechasVigenciaMap(String key,Object value) {
		this.fechasVigenciaMap.put(key, value);
	}

	/**
	 * @return the cadenaCodigosServicios
	 */
	public String getCadenaCodigosServicios() {
		return cadenaCodigosServicios;
	}

	/**
	 * @param cadenaCodigosServicios the cadenaCodigosServicios to set
	 */
	public void setCadenaCodigosServicios(String cadenaCodigosServicios) {
		this.cadenaCodigosServicios = cadenaCodigosServicios;
	}

}
