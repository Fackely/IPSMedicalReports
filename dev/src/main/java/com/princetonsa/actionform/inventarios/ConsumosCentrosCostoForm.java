package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.inventarios.SeccionesAction;

import util.ConstantesBD;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

import util.UtilidadFecha;

public class ConsumosCentrosCostoForm extends ValidatorForm
{
	
	Logger logger = Logger.getLogger(SeccionesAction.class);
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * Mapa de los almacenes
	 */
	private HashMap almacenesMap;
	
	/**
	 * Codigo del centro de atencion
	 */
	private String centroAtencion;
	
	/**
	 * Mapa de los centros de atencion
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * Mapa de las clases
	 */
	private HashMap clasesMap;
	
	/**
	 * Mapa de los Centros de Costo
	 */
	private HashMap centroCostoMap;
	
	/**
	 * Fecha inicial del pedido
	 */
	private String fechaini;
	
	/**
	 * Fecha final del pedido
	 */
	private String fechafin;
	
	/**
	 * Codigo del almacen
	 */
	private String almacen;
	
	/**
	 * Codigo del Centro de Costo
	 */
	private String centroCosto;
	
	/**
	 * Mapa de los pedidos
	 */
	private HashMap pedidosMap;
	
	/**
	 * Codigo de la Clase
	 */
	private String clase;
	
	/**
	 * Variable para manejar la visualización del mensaje
	 */
	private boolean mostrarMensaje = true;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        String fechaaux;
        
        if(this.estado.equals("generarReporte"))
        {
        	if(this.estado.equals("generarReporte"))
            {
            	if(this.getCentroAtencion().equals("-1"))
            	{
            		errores.add("descripcion",new ActionMessage("errors.required","El Centro de Atencion "));
            		mostrarMensaje = false;
            	}
            	if(this.getAlmacen().equals("-1"))
            	{
            		errores.add("descripcion",new ActionMessage("errors.required","El Almacen "));
            		mostrarMensaje = false;
            	}
            	/*if(this.getCentroCosto().equals("-1"))
            	{
            		errores.add("descripcion",new ActionMessage("errors.required","El Centro de Costo "));
            	}
            	if(this.getClase().equals("-1"))
            	{
            		errores.add("descripcion",new ActionMessage("errors.required","La Clase "));
            	}*/
            }
        	
        	if(this.getFechafin().equals("") || this.getFechaini().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y Final "));
				mostrarMensaje = false;
			}
			else
			{
				if(!UtilidadFecha.compararFechas(this.getFechafin().toString(), "00:00", this.getFechaini().toString(), "00:00").isTrue())
				{
					errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Inicial "+this.getFechaini().toString()+" mayor a la Fecha Final "+this.getFechafin().toString()+" "));
					mostrarMensaje = false;
				}
				else
				{    					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaini().toString(), "00:00").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaini().toString(),UtilidadFecha.getFechaActual()));
						mostrarMensaje = false;
					}
					 	
					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechafin().toString(), "00:00").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechafin().toString(),UtilidadFecha.getFechaActual()));
						mostrarMensaje = false;
					}
						
					
					fechaaux=UtilidadFecha.incrementarDiasAFecha(this.getFechaini().toString(), 15, false);
					
					if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.getFechafin().toString(), "00:00").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha inicial y Fecha final supera los 15 dias por lo tanto el rango elegido"));
						mostrarMensaje = false;
					}
						
				}
			}
        }
        return errores;
    }
	
	

	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	public HashMap getClasesMap() {
		return clasesMap;
	}

	public void setClasesMap(HashMap clasesMap) {
		this.clasesMap = clasesMap;
	}

	public HashMap getAlmacenesMap() {
		return almacenesMap;
	}
	
	public String getAlmacen() {
		return almacen;
	}



	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}



	public String getCentroAtencion() {
		return centroAtencion;
	}



	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}



	public String getClase() {
		return clase;
	}



	public void setClase(String clase) {
		this.clase = clase;
	}



	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}
	
	public String getCentroCosto() {
		return centroCosto;
	}


	public void setAlmacenesMap(HashMap almacenesMap) {
		this.almacenesMap = almacenesMap;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void reset(int codigoInstitucion, int centroAtencion) {
		this.centroAtencion=centroAtencion+"";
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.almacen = "";
		this.almacenesMap = UtilidadInventarios.listadoAlmacensActivos(codigoInstitucion, false);
		this.clase = "";
		this.clasesMap = new HashMap();
		this.clasesMap.put("numRegistros", "0");
		this.centroCosto=centroCosto;
		this.centroCostoMap = new HashMap();
		this.centroCostoMap.put("numRegistros", "0");
		this.pedidosMap = new HashMap();
		this.pedidosMap.put("numRegistros", "0");
		this.fechafin="";
		this.fechaini="";
		this.mostrarMensaje = true;
	}
	
	public void resetMap()
	{
		this.centroCostoMap = new HashMap();
		this.centroCostoMap.put("numRegistros", "0");
		this.clasesMap = new HashMap();
		this.clasesMap.put("numRegistros", "0");
		this.pedidosMap = new HashMap();
		this.pedidosMap.put("numRegistros", "0");
	}
	
	public void resetCambiarCentroCosto()
	{
		this.clasesMap = new HashMap();
		this.clasesMap.put("numRegistros", "0");
		this.pedidosMap = new HashMap();
		this.pedidosMap.put("numRegistros", "0");
	}
	
	public void resetCambiarClase()
	{
		this.pedidosMap = new HashMap();
		this.pedidosMap.put("numRegistros", "0");
	}

	public HashMap getCentroCostoMap() {
		return centroCostoMap;
	}

	public void setCentroCostoMap(HashMap centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}

	public String getFechaini() {
		return fechaini;
	}

	public void setFechaini(String fechaini) {
		this.fechaini = fechaini;
	}

	public String getFechafin() {
		return fechafin;
	}

	public void setFechafin(String fechafin) {
		this.fechafin = fechafin;
	}
	
	public HashMap getPedidosMap() {
		return pedidosMap;
	}

	public void setPedidosMap(HashMap pedidosMap) {
		this.pedidosMap = pedidosMap;
	}



	public Object getPedidosMap(String key) {
		return pedidosMap.get(key);
	}


	public void setPedidosMap(String key, Object value) {
		this.pedidosMap.put(key, value);
	}



	public void setMostrarMensaje(boolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}



	public boolean isMostrarMensaje() {
		return mostrarMensaje;
	}
	
}