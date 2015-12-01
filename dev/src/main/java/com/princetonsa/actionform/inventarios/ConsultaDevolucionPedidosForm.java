package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.action.inventarios.SeccionesAction;

public class ConsultaDevolucionPedidosForm extends ValidatorForm
{
	Logger logger = Logger.getLogger(SeccionesAction.class);
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * Codigo del centro de atencion
	 */
	private String centroAtencion;
	
	/**
	 * Codigo del almacen
	 */
	private String almacen;
	
	/**
	 * Codigo Check
	 */
	private String check;
	
	/**
	 * Nombre Usuario Devuelve
	 */
	private String usuarioDevuelve;
	
	/**
	 * Nombre Usuario Recibe
	 */
	private String usuarioRecibe;
	
	/**
	 * Codigo del estado
	 */
	private String estadoDevolucion;
	
	/**
	 * Codigo del Centro de Costo
	 */
	private String centroCosto;
	
	/**
	 * Codigo del Motivo Devolucion
	 */
	private String motivoDevolucion;
	
	/**
	 * Codigo Devolucion
	 */
	private String numeroDevolucion;
	
	/**
	 * Fecha inicial de la Devolucion
	 */
	private String fechaini;
	
	/**
	 * Fecha final de la Devolucion
	 */
	private String fechafin;
	
	/**
	 * 
	 */
	private String indexMap;
	
	/**
	 *
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 *
	 */
	private HashMap devolucionesMap;
	
	/**
	 *
	 */
	private HashMap detalleDevolucionesMap;
	
	/**
	 * Mapa de los almacenes
	 */
	private HashMap almacenesMap;
	
	/**
	 * Mapa de los estados
	 */
	private HashMap estadosDevolucionMap;
	
	/**
	 * Mapa de los Centros de Costo
	 */
	private HashMap centroCostoMap;
	
	/**
	 * Mapa de los Motivos Devolucion
	 */
	private HashMap motivosDevolucionMap;
	
	/**
	 * Mapa Auxiliar
	 */
	private HashMap mapaAuxiliar;
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        String fechaaux;
        
        if(this.estado.equals("consultar")){
        	if(this.getFechaini().equals("") || this.getFechafin().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y la Fecha Final"));
			}
			else
			{
				if(!this.getFechafin().equals("") && !this.getFechaini().equals("")){
					if(!UtilidadFecha.compararFechas(this.getFechafin().toString(), "00:00", this.getFechaini().toString(), "00:00").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Inicial "+this.getFechaini().toString()+" mayor a la Fecha Final "+this.getFechafin().toString()+" "));
					}
					else
					{    					
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaini().toString(), "00:00").isTrue())
						 	errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaini().toString(),UtilidadFecha.getFechaActual()));
						
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechafin().toString(), "00:00").isTrue())
							errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechafin().toString(),UtilidadFecha.getFechaActual()));
						
						fechaaux=UtilidadFecha.incrementarDiasAFecha(this.getFechaini().toString(), 15, false);
						
						if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.getFechafin().toString(), "00:00").isTrue())
							errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha inicial y Fecha final supera los 15 dias por lo tanto el rango elegido"));
					}
				}
			}
        }
        
        return errores;
    }
	
	
	
	public void reset(int codigoInstitucion, int centroAtencion) {
		this.centroAtencion=centroAtencion+"";
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.almacen = "";
		this.almacenesMap = UtilidadInventarios.listadoAlmacensActivos(codigoInstitucion, false);
		this.centroCosto="";
		this.centroCostoMap = new HashMap();
		this.centroCostoMap.put("numRegistros", "0");
		this.estadoDevolucion="";
		this.estadosDevolucionMap = new HashMap();
		this.estadosDevolucionMap.put("numRegistros", "0");
		this.devolucionesMap = new HashMap();
		this.devolucionesMap.put("numRegistros", "0");
		this.motivosDevolucionMap = new HashMap();
		this.motivosDevolucionMap.put("numRegistros", "0");
		this.detalleDevolucionesMap = new HashMap();
		this.detalleDevolucionesMap.put("numRegistros", "0");
		this.fechafin="";
		this.fechaini="";
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.usuarioDevuelve="";
		this.usuarioRecibe="";
		this.numeroDevolucion="";
		this.motivoDevolucion="";
		this.mapaAuxiliar = new HashMap();
		this.mapaAuxiliar.put("numRegistros", "0");
	}
	
	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}


	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public String getAlmacen() {
		return almacen;
	}


	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}


	public HashMap getAlmacenesMap() {
		return almacenesMap;
	}


	public void setAlmacenesMap(HashMap almacenesMap) {
		this.almacenesMap = almacenesMap;
	}


	public String getCentroCosto() {
		return centroCosto;
	}


	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}


	public HashMap getCentroCostoMap() {
		return centroCostoMap;
	}


	public void setCentroCostoMap(HashMap centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}

	public String getFechafin() {
		return fechafin;
	}

	public void setFechafin(String fechafin) {
		this.fechafin = fechafin;
	}

	public String getFechaini() {
		return fechaini;
	}

	public void setFechaini(String fechaini) {
		this.fechaini = fechaini;
	}

	public HashMap getEstadosDevolucionMap() {
		return estadosDevolucionMap;
	}

	public void setEstadosDevolucionMap(HashMap estadosDevolucionMap) {
		this.estadosDevolucionMap = estadosDevolucionMap;
	}

	public String getEstadoDevolucion() {
		return estadoDevolucion;
	}

	public void setEstadoDevolucion(String estadoDevolucion) {
		this.estadoDevolucion = estadoDevolucion;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getMotivoDevolucion() {
		return motivoDevolucion;
	}

	public void setMotivoDevolucion(String motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	public HashMap getMotivosDevolucionMap() {
		return motivosDevolucionMap;
	}

	public void setMotivosDevolucionMap(HashMap motivosDevolucionMap) {
		this.motivosDevolucionMap = motivosDevolucionMap;
	}

	public String getNumeroDevolucion() {
		return numeroDevolucion;
	}

	public void setNumeroDevolucion(String numeroDevolucion) {
		this.numeroDevolucion = numeroDevolucion;
	}

	public String getUsuarioDevuelve() {
		return usuarioDevuelve;
	}

	public void setUsuarioDevuelve(String usuarioDevuelve) {
		this.usuarioDevuelve = usuarioDevuelve;
	}

	public String getUsuarioRecibe() {
		return usuarioRecibe;
	}

	public void setUsuarioRecibe(String usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}

	public HashMap getDevolucionesMap() {
		return devolucionesMap;
	}

	public void setDevolucionesMap(HashMap devolucionesMap) {
		this.devolucionesMap = devolucionesMap;
	}
	
	public Object getDevolucionesMap(String key) {
		return devolucionesMap.get(key);
	}


	public void setDevolucionesMap(String key, Object value) {
		this.devolucionesMap.put(key, value);
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

	public String getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}

	public HashMap getDetalleDevolucionesMap() {
		return detalleDevolucionesMap;
	}

	public void setDetalleDevolucionesMap(HashMap detalleDevolucionesMap) {
		this.detalleDevolucionesMap = detalleDevolucionesMap;
	}

	public HashMap getMapaAuxiliar() {
		return mapaAuxiliar;
	}

	public void setMapaAuxiliar(HashMap mapaAuxiliar) {
		this.mapaAuxiliar = mapaAuxiliar;
	}
	
	public Object getMapaAuxiliar(String key) {
		return mapaAuxiliar.get(key);
	}

	public void setMapaAuxiliar(String key, Object value) {
		this.mapaAuxiliar.put(key, value);
	}
}