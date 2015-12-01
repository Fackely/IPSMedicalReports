/*
 * Marzo 21 del 2007
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de hoja de Gastos
 */
public class HojaGastosForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	
	//***********ATRIBUTOS PARA EL DETALLE DEL PAQUETE***********************
	/**
	 * Número de registros del mapaServicios
	 */
	private int numServicios;
	
	/**
	 * Mapa de los servicios
	 */
	private HashMap mapaServicios = new HashMap();
	
	/**
	 * Codigos de los servicios insertados separados por comas
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * Número de registros del mapaArticulos
	 */
	private int numArticulos;
	
	/**
	 * Mapa de los artículos
	 */
	private HashMap mapaArticulos = new HashMap();
	
	/**
	 * Codigos de los articulos insertados separados por comas
	 */
	private String codigosArticulosInsertados;
	
	/**
	 * Consecutivo del registro de la hoja de gastos
	 */
	private String consecutivo;
	
	/**
	 * Posicion de un registro del mapa (sirve tanto para servicios como articulos)
	 */
	private int pos;
	
	/**
	 * Variable que almacena mensaje que se postula en la seccion de la busqueda de saervicios
	 */
	private String mensaje = "";
	
	//********ATTRIBUTOS PARA EL LISTADO DE PAQUETES MATERIALES QX*************
	private HashMap<String, Object> listado = new HashMap<String, Object>();
	private int numRegistros;
	//Mapa donde se alamcenan los paquetes que se van a eliminar
	private HashMap<String, Object> listadoEliminacion = new HashMap<String, Object>();
	private int numRegistrosEliminacion;
	
	//Variable usada para llevar el index del mapa de paquetes
	private int posPaquete;
	
	//atributos que tienen que ver con el pager
	private int maxPageItems;
	private String linkSiguiente;
	private String indice;
	private String ultimoIndice;
	//*****************************************************************************************
	
	//***********ATRIBUTOS PARA LA FUNCIONALIDAD DE PROCEDIMIENTO POR PAQUETES QX**************
	private HashMap<String, Object> seleccion = new HashMap<String, Object>();
	private String nuevoPaquete; //nuevo paquete
	//******************************************************************************************
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.numServicios = 0;
		this.mapaServicios = new HashMap();
		this.codigosServiciosInsertados = "";
		
		this.numArticulos = 0;
		this.mapaArticulos = new HashMap();
		this.codigosArticulosInsertados = "";
		
		this.consecutivo = "";
		this.pos = 0;
		this.mensaje = "";
		
		//Atributos para el listado de paquetes materiales Qx
		this.listado = new HashMap<String, Object>();
		this.numRegistros = 0;
		this.listadoEliminacion = new HashMap<String, Object>();
		this.setListadoEliminacion("numRegistros", "0");
		this.numRegistrosEliminacion = 0;
		
		this.posPaquete = 0;
		
		this.maxPageItems = 10;
		
		this.linkSiguiente = "";
		this.indice = "";
		this.ultimoIndice = "";
		
		//Atributos para la funcionalidad de procedimientos por paquetes qx
		this.seleccion = new HashMap<String, Object>();
		this.nuevoPaquete = "";
		
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
		
		if(this.estado.equals("guardarArticulos"))
		{
			
			//se deben haber ingresado tanto artículos como servicios
			//SE comenta esta parte por tarea [id=30794] Xplanner 2008
			/**if(obtenerNumRegistrosAIngresar(this.mapaArticulos, this.numArticulos)<=0)
				errores.add("Se debe ingresar al menos 1 articulo",new ActionMessage("errors.minimoCampos","el ingreso de un artículo","inserción de la información"));**/
			
			//se verifica que se haya editado cantidad
			int cantidad = 0;
			for(int i=0;i<this.numArticulos;i++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaArticulos("eliminar_"+i).toString()))
				{
					if(this.getMapaArticulos("cantidad_"+i).toString().equals(""))
						errores.add("La cantidad del articulo es requerida",new ActionMessage("errors.required","La cantidad del artículo con código "+this.getMapaArticulos("codigoArticulo_"+i)));
					else
					{
						try
						{
							cantidad = Integer.parseInt(this.getMapaArticulos("cantidad_"+i).toString());
							if(cantidad<=0)
								errores.add("La cantidad del articulo debe ser mayor que 0",new ActionMessage("errors.debeSerNumeroMayor","La cantidad del artículo con código "+this.getMapaArticulos("codigoArticulo_"+i),"0"));
						}
						catch(Exception e)
						{
							errores.add("La cantidad del articulo debe ser un número entero",new ActionMessage("errors.integer","La cantidad del artículo con código "+this.getMapaArticulos("codigoArticulo_"+i)));
						}
					}
				}
			}
			
			if(!errores.isEmpty())
				this.estado = "empezar";
			
		}
		
		return errores;
	}
	
	/**
	 * Método que obtiene el número de servicios que se van a eliminar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private int obtenerNumRegistrosAIngresar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			if(!UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
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
	 * @return Elemento del mapa mapaServicios
	 */
	public Object getMapaServicios(String key) {
		return mapaServicios.get(key);
	}

	/**
	 * @param Asigna elemento al mapaServicios
	 */
	public void setMapaServicios(String key,Object obj) {
		this.mapaServicios.put(key,obj);
	}

	/**
	 * @return the numServicio
	 */
	public int getNumServicios() {
		return numServicios;
	}

	/**
	 * @param numServicio the numServicio to set
	 */
	public void setNumServicios(int numServicio) {
		this.numServicios = numServicio;
	}

	/**
	 * @return the codigosServiciosInsertados
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	/**
	 * @param codigosServiciosInsertados the codigosServiciosInsertados to set
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	/**
	 * @return the codigosArticulosInsertados
	 */
	public String getCodigosArticulosInsertados() {
		return codigosArticulosInsertados;
	}

	/**
	 * @param codigosArticulosInsertados the codigosArticulosInsertados to set
	 */
	public void setCodigosArticulosInsertados(String codigosArticulosInsertados) {
		this.codigosArticulosInsertados = codigosArticulosInsertados;
	}

	/**
	 * @return the mapaArticulos
	 */
	public HashMap getMapaArticulos() {
		return mapaArticulos;
	}

	/**
	 * @param mapaArticulos the mapaArticulos to set
	 */
	public void setMapaArticulos(HashMap mapaArticulos) {
		this.mapaArticulos = mapaArticulos;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaArticulos
	 */
	public Object getMapaArticulos(String key) {
		return mapaArticulos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaArticulos
	 */
	public void setMapaArticulos(String key,Object obj) {
		this.mapaArticulos.put(key,obj);
	}

	/**
	 * @return the numArticulos
	 */
	public int getNumArticulos() {
		return numArticulos;
	}

	/**
	 * @param numArticulos the numArticulos to set
	 */
	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the listado
	 */
	public HashMap<String, Object> getListado() {
		return listado;
	}

	/**
	 * @param listado the listado to set
	 */
	public void setListado(HashMap<String, Object> listado) {
		this.listado = listado;
	}
	
	/**
	 * @return the listado
	 */
	public Object getListado(String key) {
		return listado.get(key);
	}

	/**
	 * @param listado the listado to set
	 */
	public void setListado(String key,Object obj) {
		this.listado.put(key,obj);
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
	 * @return the numRegistros
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros the numRegistros to set
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
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
	 * @return the listadoEliminacion
	 */
	public HashMap<String, Object> getListadoEliminacion() {
		return listadoEliminacion;
	}

	/**
	 * @param listadoEliminacion the listadoEliminacion to set
	 */
	public void setListadoEliminacion(HashMap<String, Object> listadoEliminacion) {
		this.listadoEliminacion = listadoEliminacion;
	}
	
	/**
	 * @return the listadoEliminacion
	 */
	public Object getListadoEliminacion(String key) {
		return listadoEliminacion.get(key);
	}

	/**
	 * @param listadoEliminacion the listadoEliminacion to set
	 */
	public void setListadoEliminacion(String key,Object obj) {
		this.listadoEliminacion.put(key,obj);
	}

	/**
	 * @return the numRegistrosEliminacion
	 */
	public int getNumRegistrosEliminacion() {
		return numRegistrosEliminacion;
	}

	/**
	 * @param numRegistrosEliminacion the numRegistrosEliminacion to set
	 */
	public void setNumRegistrosEliminacion(int numRegistrosEliminacion) {
		this.numRegistrosEliminacion = numRegistrosEliminacion;
	}

	/**
	 * @return the posPaquete
	 */
	public int getPosPaquete() {
		return posPaquete;
	}

	/**
	 * @param posPaquete the posPaquete to set
	 */
	public void setPosPaquete(int posPaquete) {
		this.posPaquete = posPaquete;
	}

	/**
	 * @return the seleccion
	 */
	public HashMap<String, Object> getSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(HashMap<String, Object> seleccion) {
		this.seleccion = seleccion;
	}
	
	/**
	 * @return the seleccion
	 */
	public Object getSeleccion(String key) {
		return seleccion.get(key);
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(String key,Object obj) {
		this.seleccion.put(key,obj);
	}

	/**
	 * @return the nuevoPaquete
	 */
	public String getNuevoPaquete() {
		return nuevoPaquete;
	}

	/**
	 * @param nuevoPaquete the nuevoPaquete to set
	 */
	public void setNuevoPaquete(String nuevoPaquete) {
		this.nuevoPaquete = nuevoPaquete;
	}

	
}
