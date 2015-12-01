/*
 * Marzo 21 del 2007
 */
package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.actionform.administracion.FactorConversionMonedasForm;

import util.UtilidadTexto;

/**
 * @author Andrés Eugenio Silva Monsalve
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de hoja de Gastos
 */
public class CondicionesXServiciosForm extends ValidatorForm 
{
	
		//---Atributos
	
	Logger logger = Logger.getLogger(CondicionesXServiciosForm.class);
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DEL PAGER 
	 * ---------------------------------------------------*/
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
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
	 * Número de registros del mapaCondiciones
	 */
	private int numCondiciones;
	
	/**
	 * Mapa de las condiciones
	 */
	private HashMap mapaCondiciones = new HashMap();
	
	/**
	 * Codigos de las condiciones insertados separados por comas
	 */
	private String codigoCondicionInsertado;
	
	/**
	 * Consecutivo del registro de la hoja de gastos
	 */
	private int consecutivo;
	
	/**
	 * Posicion de un registro del mapa (sirve tanto para servicios como condiciones)
	 */
	private int pos;
	
	/**
	 * Variable que almacena mensaje que se postula en la seccion de la busqueda de saervicios
	 */
	private String mensaje = "";
	
	
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
		
		this.numCondiciones = 0;
		this.mapaCondiciones = new HashMap();
		this.codigoCondicionInsertado = "";
		
		this.consecutivo = 0;
		this.pos = 0;
		this.mensaje = "";
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
			//se verifica si hay algo para guardar
			if(this.consecutivo<=0&&this.numCondiciones<=0&&this.numServicios<=0)
				this.estado = "empezar";
			else
			{
				//se deben haber ingresado tanto artículos como servicios
				if(obtenerNumRegistrosAIngresar(this.mapaCondiciones, this.numCondiciones)<=0&&
				   obtenerNumRegistrosAIngresar(this.mapaServicios, this.numServicios)>0)
					errores.add("Se debe ingresar al menos 1 Condicion",new ActionMessage("errors.minimoCampos","el ingreso de una Condicion","inserción de la información"));
				if(obtenerNumRegistrosAIngresar(this.mapaCondiciones, this.numCondiciones)>0&&
					obtenerNumRegistrosAIngresar(this.mapaServicios, this.numServicios)<=0)
					errores.add("Se debe ingresar al menos 1 Condicion",new ActionMessage("errors.minimoCampos","el ingreso de un servicio","inserción de la información"));
				
				for (int i=0;i<numCondiciones;i++)
				{
					for (int j=0;j<numCondiciones;j++)
					{
						logger.info("i -->"+i+" j -->"+j+" los valres que se comparan son "+this.mapaCondiciones.get("codigoCondicion_"+j)+" --- "+this.mapaCondiciones.get("codigoCondicion_"+i));
						if (j>i && j!=i && (this.mapaCondiciones.get("codigoCondicion_"+j)+"").equals(this.mapaCondiciones.get("codigoCondicion_"+i)+""))
							errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion"," condiciones"));
						
					}
				}
				
				if(!errores.isEmpty())
					this.estado = "empezar";
			}
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
	 * @return the codigoCondicionInsertado
	 */
	public String getCodigosCondicionInsertados() {
		return codigoCondicionInsertado;
	}

	/**
	 * @param codigoCondicionInsertado the codigoCondicionInsertado to set
	 */
	public void setCodigosCondicionInsertados(String codigoCondicionInsertado) {
		this.codigoCondicionInsertado = codigoCondicionInsertado;
	}

	/**
	 * @return the mapaCondiciones
	 */
	public HashMap getMapaCondiciones() {
		return mapaCondiciones;
	}

	/**
	 * @param mapaCondiciones the mapaCondiciones to set
	 */
	public void setMapaCondiciones(HashMap mapaCondiciones) {
		this.mapaCondiciones = mapaCondiciones;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaCondiciones
	 */
	public Object getMapaCondiciones(String key) {
		return mapaCondiciones.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaCondiciones
	 */
	public void setMapaCondiciones(String key,Object obj) {
		this.mapaCondiciones.put(key,obj);
	}

	/**
	 * @return the numCondiciones
	 */
	public int getNumCondiciones() {
		return numCondiciones;
	}

	/**
	 * @param numCondiciones the numCondiciones to set
	 */
	public void setNumCondiciones(int numCondiciones) {
		this.numCondiciones = numCondiciones;
	}

	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
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

	public String getCodigoCondicionInsertado() {
		return codigoCondicionInsertado;
	}

	public void setCodigoCondicionInsertado(String codigoCondicionInsertado) {
		this.codigoCondicionInsertado = codigoCondicionInsertado;
	}

	
}
